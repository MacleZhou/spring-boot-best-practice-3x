package cn.javastack.aggregateinmemory.core.support;

import cn.javastack.aggregateinmemory.annotation.AggregateInMemory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.expression.BeanResolver;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
public class AggregateInMemoryBasedAggregateItemExecutorFactory extends AbstractAnnotationBasedAggregateItemExecutorFactory<AggregateInMemory> {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final TemplateParserContext templateParserContext = new TemplateParserContext();
    private final BeanResolver beanResolver;

    public AggregateInMemoryBasedAggregateItemExecutorFactory(BeanResolver beanResolver) {
        super(AggregateInMemory.class);
        this.beanResolver = beanResolver;
    }

    @Override
    protected <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, AggregateInMemory ann) {
        if (StringUtils.isEmpty(ann.dataConverter())){
            log.info("No Data Convert for class {}, field {}", cls, field.getName());
            return Function.identity();
        }else {
            log.info("Data Convert is {} for class {}, field {}", ann.dataConverter(), cls, field.getName());
            return new DataGetter(ann.dataConverter());
        }
    }

    @Override
    protected <DATA> BiConsumer<Object, List<Object>> createFoundListFunction(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("write field is {} for class {}", field.getName(), cls);
        boolean isCollection = Collection.class.isAssignableFrom(field.getType());
        return new DataSetter(field.getName(), isCollection);
    }

    @Override
    protected <DATA> BiConsumer<Object, Map<Map<String, Object>, List<Object>>> createFoundGroupbyFunction(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("write field is {} for class {}", field.getName(), cls);
        boolean isCollection = Map.class.isAssignableFrom(field.getType());
        return new DataSetter(field.getName(), isCollection);
    }


    @Override
    protected <DATA> Function<Object, Map<String, Object>> createKeysFromSourceData(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("Key from aggregate source data is {} for class {}, field {}",
                ann.keysFromSourceData()[0], cls, field.getName());
        return new DataGetter(ann.keysFromSourceData()[0]);
    }

    @Override
    protected <DATA> Function<Map<String, Object>, List<Object>> createDataLoader(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("data loader is {} for class {}, field {}",
                ann.loader(), cls, field.getName());
        return new DataGetter(ann.loader());
    }

    @Override
    protected <DATA> Function<Object, Map<String, Object>> createGroupbyKeysFromAggregateItemData(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("Key from aggregate item data is {} for class {}, field {}",
                ann.keysFromAggregateData()[0], cls, field.getName());
        return new DataGetter(ann.keysFromAggregateData()[0]);
    }

    @Override
    protected <DATA> int createRunLevel(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("run level is {} for class {}, field {}",
                ann.runLevel(), cls, field.getName());
        return ann.runLevel();
    }

    private class DataSetter<T, U> implements BiConsumer<Object, List<Object>>{
        private final String fieldName;
        private final boolean isCollection;
        private final Expression expression;

        private DataSetter(String fieldName, boolean isCollection) {
            this.fieldName = fieldName;
            this.expression = parser.parseExpression(fieldName);
            this.isCollection = isCollection;
        }

        @Override
        public void accept(Object data, List<Object> result) {
            if (isCollection) {
                this.expression.setValue(data, result);
            }else {
                int size = result.size();
                if (size == 1){
                    this.expression.setValue(data, result.get(0));
                }else {
                    log.warn("write join result to {} error, field is {}, data is {}",
                            data,
                            fieldName,
                            result);
                }
            }
        }
    }

    private class DataGetter<T, R> implements Function<T, R>{
        private final String expStr;
        private final Expression expression;
        private final EvaluationContext evaluationContext;

        private DataGetter(String expStr) {
            this.expStr = expStr;
            this.expression = parser.parseExpression(expStr, templateParserContext);
            StandardEvaluationContext evaluationContext = new StandardEvaluationContext();
            evaluationContext.setBeanResolver(beanResolver);
            this.evaluationContext = evaluationContext;
        }

        @Override
        public Object apply(Object data) {
            if (data == null){
                return null;
            }

            return expression.getValue(evaluationContext, data);
        }
    }
}
