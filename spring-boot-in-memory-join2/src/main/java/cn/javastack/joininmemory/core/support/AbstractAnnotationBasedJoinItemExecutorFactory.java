package cn.javastack.joininmemory.core.support;


import cn.javastack.joininmemory.core.JoinItemExecutor;
import cn.javastack.joininmemory.core.JoinItemExecutorFactory;
import lombok.extern.slf4j.Slf4j;
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
 * Created by taoli on 2022/8/5.
 * gitee : https://gitee.com/litao851025/lego
 * 编程就像玩 Lego
 */
@Slf4j
abstract class AbstractAnnotationBasedJoinItemExecutorFactory<A extends Annotation>
    implements JoinItemExecutorFactory {
    private final Class<A> annotationCls;

    protected AbstractAnnotationBasedJoinItemExecutorFactory(Class<A> annotationCls) {
        this.annotationCls = annotationCls;
        log.debug("jim.AbstractAnnotationBasedJoinItemExecutorFactory.new=" + annotationCls.getCanonicalName());
    }

    @Override
    public <DATA> List<JoinItemExecutor<DATA>> createForType(Class<DATA> cls) {
        log.debug("jim.AbstractAnnotationBasedJoinItemExecutorFactory.createForType begin : " + getClass().getCanonicalName());
        // 从 字段 上获取 注解，并将其转换为 JoinItemExecutor
        List<Field> fieldsListWithAnnotation = FieldUtils.getAllFieldsList(cls);

        return fieldsListWithAnnotation.stream()
                .map(field -> createForField(cls, field,
                        AnnotatedElementUtils.findMergedAnnotation(field, annotationCls))
                ).filter(Objects::nonNull)
                .collect(toList());
    }

    private <DATA> JoinItemExecutor<DATA> createForField(Class<DATA> cls, Field field, A ann) {
        log.debug("jim.AbstractAnnotationBasedJoinItemExecutorFactory.createForField begin: " + cls.getCanonicalName() + "; field=" + field.getName() + "; ann=" + ann.toString());
        if (ann == null){
            return null;
        }
        log.debug("jim.AbstractAnnotationBasedJoinItemExecutorFactory.createForField 1: " + cls.getCanonicalName() + "; field=" + field.getName() + "; ann=" + ann.toString());
        JoinItemExecutorAdapter adapter = JoinItemExecutorAdapter.builder()
                .name(createName(cls, field, ann))
                .runLevel(createRunLevel(cls, field, ann))
                .keyFromSourceData(createKeyGeneratorFromData(cls, field, ann))
                .joinDataLoader(createDataLoader(cls, field, ann))
                .keyFromJoinData(createKeyGeneratorFromJoinData(cls, field, ann))
                .joinDataConverter(createDataConverter(cls, field, ann))
                .foundCallback(createFoundFunction(cls, field, ann))
                .lostCallback(createLostFunction(cls, field, ann))
                .build();

        return adapter;
    }

    protected <DATA> BiConsumer<Object, Object> createLostFunction(Class<DATA> cls, Field field, A ann){
        return null;
    }

    protected abstract <DATA> BiConsumer<Object, List<Object>> createFoundFunction(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createDataConverter(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<Object, Object> createKeyGeneratorFromJoinData(Class<DATA> cls, Field field, A ann);

    protected abstract <DATA> Function<List<Object>, List<Object>> createDataLoader(Class<DATA> cls, Field field, A ann);

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
