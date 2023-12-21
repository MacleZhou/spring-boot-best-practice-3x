package cn.javastack.jaimini.aim.core.support;

import cn.javastack.jaimini.aim.annotation.AggregateInMemory;
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
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * 通过annotation, 配置执行器
 */
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
    protected <DATA> BiConsumer<Object, Object> createFoundFunction(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("write field is {} for class {}", field.getName(), cls);
        boolean isCollection = Collection.class.isAssignableFrom(field.getType());
        return new DataSetter(field.getName(), isCollection);
    }

    @Override
    protected <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, AggregateInMemory ann) {
        if (StringUtils.isEmpty(ann.itemDataConverter())){
            log.info("No Data Convert for class {}, field {}", cls, field.getName());
            return Function.identity();
        }else {
            log.info("Data Convert is {} for class {}, field {}", ann.itemDataConverter(), cls, field.getName());
            return new DataGetter(ann.itemDataConverter());
        }
    }

    @Override
    protected <DATA> Function<Object, Object> createKeyGroupbyFromJoinData(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("Key from join data is {} for class {}, field {}", ann.keyToGroupbyItemData(), cls, field.getName());
        return new DataGetter(ann.keyToGroupbyItemData());
    }

    @Override
    protected <DATA> Function<Object, List<Object>> createDataLoader(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("data loader is {} for class {}, field {}", ann.loader(), cls, field.getName());
        return new DataGetter(ann.loader());
    }

    @Override
    protected <DATA> Function<Object, Object> createKeyGeneratorFromSourceData(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("Key from source data is {} for class {}, field {}", ann.keyToGroupbyItemData(), cls, field.getName());
        return new DataGetter(ann.keyFromSourceData());
    }

    @Override
    protected <DATA> int createRunLevel(Class<DATA> cls, Field field, AggregateInMemory ann) {
        log.info("run level is {} for class {}, field {}", ann.runLevel(), cls, field.getName());
        return ann.runLevel();
    }

    /**
     * 把计算结果设置到目标对象上
     * */
    private class DataSetter<T, U> implements BiConsumer<Object, Object>{
        private final String fieldName;
        private final boolean isCollection;
        private final Expression expression;

        private DataSetter(String fieldName, boolean isCollection) {
            this.fieldName = fieldName;
            this.expression = parser.parseExpression(fieldName);
            this.isCollection = isCollection;
        }

        @Override
        public void accept(Object data, Object result) {
            this.expression.setValue(data, result);
        }
    }

    /**
     * 从目标对象上获取数据
     * */
    private class DataGetter<T, R> implements Function<T, R>{
        private final String expStr;
        private final Expression expression;
        private final EvaluationContext evaluationContext;

        private DataGetter(String expStr) {
            this.expStr = expStr;
            this.expression = StringUtils.isNoneBlank(this.expStr) ? parser.parseExpression(expStr, templateParserContext) : null;
            StandardEvaluationContext evaluationContext = null;
            if(StringUtils.isNoneBlank(expStr)){
                evaluationContext = new StandardEvaluationContext();
                evaluationContext.setBeanResolver(beanResolver);
            }
            this.evaluationContext = evaluationContext;
        }

        @Override
        public Object apply(Object data) {
            if (data == null){
                return null;
            }

            return StringUtils.isNoneBlank(this.expStr) ? expression.getValue(evaluationContext, data) : null;
        }
    }
}
