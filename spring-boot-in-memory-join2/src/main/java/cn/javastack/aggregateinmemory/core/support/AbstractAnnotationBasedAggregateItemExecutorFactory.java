package cn.javastack.aggregateinmemory.core.support;


import cn.javastack.aggregateinmemory.core.AggregateItemExecutor;
import cn.javastack.aggregateinmemory.core.AggregateItemExecutorFactory;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

abstract class AbstractAnnotationBasedAggregateItemExecutorFactory<A extends Annotation>
    implements AggregateItemExecutorFactory {
    private final Class<A> annotationCls;

    protected AbstractAnnotationBasedAggregateItemExecutorFactory(Class<A> annotationCls) {
        this.annotationCls = annotationCls;
    }

    @Override
    public <DATA> List<AggregateItemExecutor<DATA>> createForType(Class<DATA> cls) {
        // 从 字段 上获取 注解，并将其转换为 JoinItemExecutor
        List<Field> fieldsListWithAnnotation = FieldUtils.getAllFieldsList(cls);

        return fieldsListWithAnnotation.stream()
                .map(field -> createForField(cls, field,
                        AnnotatedElementUtils.findMergedAnnotation(field, annotationCls))
                ).filter(Objects::nonNull)
                .collect(toList());
    }

    private <DATA> AggregateItemExecutor<DATA> createForField(Class<DATA> cls, Field field,A ann) {
        if (ann == null){
            return null;
        }
        AggregateItemExecutorAdapter adapter = AggregateItemExecutorAdapter.builder()
                .name(createName(cls, field, ann))
                .runLevel(createRunLevel(cls, field, ann))
                .createKeysFromSourceData(createKeysFromSourceData(cls, field, ann))
                .aggregateDataLoader(createDataLoader(cls, field, ann))
                .createKeysFromAggregateItemData(createGroupbyKeysFromAggregateItemData(cls, field, ann))
                .convertToResult(createDataConverter(cls, field, ann))
                .foundCallbackList(createFoundListFunction(cls, field, ann))
                .foundCallbackGroupby(createFoundGroupbyFunction(cls, field, ann))
                .lostCallback(createLostFunction(cls, field, ann))
                .build();

        return adapter;
    }

    protected abstract <DATA> Function<Object, Map<String, Object>> createKeysFromSourceData(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Map<String, Object>, List<Object>> createDataLoader(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Map<String, Object>> createGroupbyKeysFromAggregateItemData(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, A ann);

    protected <DATA> BiConsumer<Object, Map<String, Object>> createLostFunction(Class<DATA> cls, Field field, A ann){
        return null;
    }

    protected abstract <DATA> BiConsumer<Object, List<Object>> createFoundListFunction(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> BiConsumer<Object, Map<Map<String, Object>, List<Object>>> createFoundGroupbyFunction(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> int createRunLevel(Class<DATA> cls, Field field, A ann);

    protected <DATA> String createName(Class<DATA> cls, Field field, A ann){
        return new StringBuilder()
                .append("class[").append(cls.getSimpleName()).append("]")
                .append("#field[").append(field.getName()).append("]")
                .append("-").append(ann.getClass().getSimpleName())
                .toString();
    }
}
