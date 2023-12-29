package cn.javastack.data.loader.core.support;


import cn.javastack.data.loader.annotation.DataHolderType;
import cn.javastack.data.loader.core.JoinItemExecutor;
import cn.javastack.data.loader.core.JoinItemExecutorFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * 根据类创建类的所有执行器
 */
abstract class AbstractAnnotationBasedJoinItemExecutorFactory<A extends Annotation>
    implements JoinItemExecutorFactory {
    private final Class<A> annotationCls;

    protected AbstractAnnotationBasedJoinItemExecutorFactory(Class<A> annotationCls) {
        this.annotationCls = annotationCls;
    }

    @Override
    public <DATA> List<JoinItemExecutor<DATA>> createForType(Class<DATA> cls, DataHolderType dataHolderType) {
        // 从 字段 上获取 注解，并将其转换为 JoinItemExecutor
        List<Field> fieldsListWithAnnotation = FieldUtils.getAllFieldsList(cls);

        return fieldsListWithAnnotation.stream()
                .map(field -> createForField(cls, field,
                        AnnotatedElementUtils.findMergedAnnotation(field, annotationCls), dataHolderType)
                ).filter(Objects::nonNull)
                .collect(toList());
    }

    private <DATA> JoinItemExecutor<DATA> createForField(Class<DATA> cls, Field field, A loadDataToField, DataHolderType dataHolderType) {
        if (loadDataToField == null){
            return null;
        }
        JoinItemExecutorAdapter adapter = JoinItemExecutorAdapter.builder()
                .name(createName(cls, field, loadDataToField))
                .runLevel(createRunLevel(cls, field, loadDataToField))
                .keyFromSourceData(createKeyGeneratorFromData(cls, field, loadDataToField))
                .joinDataLoader(createDataLoader(cls, field, loadDataToField))
                .keyFromJoinData(createKeyGeneratorFromJoinData(cls, field, loadDataToField))
                .joinDataConverter(createDataConverter(cls, field, loadDataToField))
                .foundCallback(createFoundFunction(cls, field, loadDataToField, dataHolderType))
                .lostCallback(createLostFunction(cls, field, loadDataToField))
                .build();

        return adapter;
    }

    protected <DATA> BiConsumer<Object, Object> createLostFunction(Class<DATA> cls, Field field, A ann){
        return null;
    }

    protected abstract <DATA> BiConsumer<Object, Object> createFoundFunction(Class<DATA> cls, Field field, A ann, DataHolderType dataHolderType);

    protected abstract <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createKeyGeneratorFromJoinData(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createDataLoader(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createKeyGeneratorFromData(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> int createRunLevel(Class<DATA> cls, Field field, A ann);

    protected <DATA> String createName(Class<DATA> cls, Field field, A ann){
        return new StringBuilder()
                .append("class[").append(cls.getSimpleName()).append("]")
                .append("#field[").append(field.getName()).append("]")
                .append("-").append(ann.getClass().getSimpleName())
                .toString();
    }
}
