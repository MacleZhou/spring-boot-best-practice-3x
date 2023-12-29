package cn.javastack.data.loader.core.support;

import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.annotation.DataLoaderInMemory;
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
 * 根据Annotation，生成各接口
 */
@Slf4j
public class LoaderInMemoryBasedDataItemExecutorFactory extends AbstractAnnotationBasedDataItemExecutorFactory<DataLoaderInMemory> {
    private final ExpressionParser parser = new SpelExpressionParser();
    private final TemplateParserContext templateParserContext = new TemplateParserContext();
    private final BeanResolver beanResolver;

    public LoaderInMemoryBasedDataItemExecutorFactory(BeanResolver beanResolver) {
        super(DataLoaderInMemory.class);
        this.beanResolver = beanResolver;
    }


    @Override
    protected <DATA> BiConsumer<Object, Object> createFoundFunction(Class<DATA> cls, Field field, DataLoaderInMemory ann, DataHolderType dataHolderType) {
        log.info("write field is {} for class {}", field.getName(), cls);
        boolean isCollection = Collection.class.isAssignableFrom(field.getType());
        return new DataSetter(field.getName(), isCollection, dataHolderType);
    }

    @Override
    protected <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, DataLoaderInMemory ann) {
        if (StringUtils.isEmpty(ann.dataConverter())){
            log.info("No Data Convert for class {}, field {}", cls, field.getName());
            return Function.identity();
        }else {
            log.info("Data Convert is {} for class {}, field {}", ann.dataConverter(), cls, field.getName());
            return new DataGetter(ann.dataConverter());
        }
    }

    @Override
    protected <DATA> Function<Object, Object> createKeyGeneratorFromJoinData(Class<DATA> cls, Field field, DataLoaderInMemory ann) {
        log.info("Key from join data is {} for class {}, field {}",
                ann.keyFromJoinData(), cls, field.getName());
        return new DataGetter(ann.keyFromJoinData());
    }

    @Override
    protected <DATA> Function<Object, Object> createDataLoader(Class<DATA> cls, Field field, DataLoaderInMemory ann) {
        log.info("data loader is {} for class {}, field {}",
                ann.loader(), cls, field.getName());
        return new DataGetter(ann.loader());
    }

    @Override
    protected <DATA> Function<Object, Object> createKeyGeneratorFromData(Class<DATA> cls, Field field, DataLoaderInMemory ann) {
        log.info("Key from source data is {} for class {}, field {}",
                ann.keyFromJoinData(), cls, field.getName());
        return new DataGetter(ann.keyFromSourceData());
    }

    @Override
    protected <DATA> int createRunLevel(Class<DATA> cls, Field field, DataLoaderInMemory ann) {
        log.info("run level is {} for class {}, field {}",
                ann.runLevel(), cls, field.getName());
        return ann.runLevel();
    }

    private class DataSetter<T, U> implements BiConsumer<Object, Object>{
        private final String fieldName;
        private final boolean isCollection;
        private final Expression expression;

        private final DataHolderType dataHolderType;

        private DataSetter(String fieldName, boolean isCollection, DataHolderType dataHolderType) {
            this.fieldName = fieldName;
            this.expression = parser.parseExpression(fieldName);
            this.isCollection = isCollection;
            this.dataHolderType = dataHolderType;
        }

        @Override
        public void accept(Object data, Object result) {
            if(null == result){
                log.warn("write join result to {} error, field is {}, data is null",
                        data,
                        fieldName);
                return;
            }
            if(this.dataHolderType == DataHolderType.AGGREGATE) {
                this.expression.setValue(data, result);
            } else {
                List list = (List) result;
                if (isCollection) {
                    this.expression.setValue(data, list);
                } else {
                    int size = list.size();
                    if (size == 1) {
                        this.expression.setValue(data, list.get(0));
                    } else {
                        log.warn("write join result to {} error, field is {}, data is {}",
                                data,
                                fieldName,
                                result);
                    }
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
