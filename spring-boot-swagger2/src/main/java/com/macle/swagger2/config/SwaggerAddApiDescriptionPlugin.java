package com.macle.swagger2.config;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ReflectUtil;
import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.macle.swagger2.annotations.FlowType;
import com.macle.swagger2.annotations.FlowTypes;
import io.swagger.annotations.ApiModelProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.RequestHandler;
import springfox.documentation.RequestHandlerKey;
import springfox.documentation.builders.ApiDescriptionBuilder;
import springfox.documentation.builders.OperationBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.schema.ResolvedTypes;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.ApiListingScannerPlugin;
import springfox.documentation.spi.service.contexts.DocumentationContext;
import springfox.documentation.spring.web.readers.operation.CachingOperationNameGenerator;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import java.util.Collections;

import java.util.stream.Collectors;

import lombok.Data;
import lombok.Builder;
import static springfox.documentation.schema.Collections.collectionElementType;
import static springfox.documentation.schema.Collections.isContainerType;
import static springfox.documentation.schema.Types.typeNameFor;



@Component
public class SwaggerAddApiDescriptionPlugin implements ApiListingScannerPlugin {

    private final TypeResolver typeResolver;

    /**
     * 参考 ApiDescriptionLookup.java
     */
    private Map<RequestHandler, GeneratedApis> needDealed = new HashMap<>();

    public Map<RequestHandler, GeneratedApis> getNeedDealed() {
        return Collections.unmodifiableMap(needDealed);
    }

    @Autowired
    public SwaggerAddApiDescriptionPlugin(TypeResolver typeResolver) {
        this.typeResolver = typeResolver;
    }

    @Override
    public List<ApiDescription> apply(DocumentationContext documentationContext) {
        return generateApiDesc(documentationContext);
    }

    private List<ApiDescription> generateApiDesc(final DocumentationContext documentationContext) {
        List<RequestHandler> requestHandlers = documentationContext.getRequestHandlers();
        List<ApiDescription> newArrayList = new ArrayList<>();
        requestHandlers.stream().filter(s -> s.findAnnotation(FlowTypes.class).isPresent())
                .forEach(handler -> {
                    List<ApiDescription> apiDescriptions = addApiDescriptions(documentationContext, handler);
                    newArrayList.addAll(apiDescriptions);
                    if (!apiDescriptions.isEmpty()) {
                        needDealed.put(handler, GeneratedApis.builder().ads(apiDescriptions).build());
                    }
                });

        return newArrayList;
    }

    private List<ApiDescription> addApiDescriptions(DocumentationContext documentationContext,
                                                    RequestHandler handler) {
        Optional<FlowTypes> annotation = handler.findAnnotation(FlowTypes.class);
        List<ApiDescription> apiDescriptionList = new ArrayList<>();
        if (annotation.isPresent()) {
            FlowTypes FlowTypes = annotation.get();
            String tagName = FlowTypes.name();
            // 确保归类在不同的group下, 以实现相同path的共存
            Arrays.stream(FlowTypes.value()).filter(FlowType -> FlowType.value()
                            .equalsIgnoreCase(documentationContext.getGroupName()))
                    .forEach(FlowType -> apiDescriptionList
                            .addAll(addApiDescription(handler, documentationContext, FlowType, tagName)));
        }
        return apiDescriptionList;
    }

    private List<ApiDescription> addApiDescription(RequestHandler handler,
                                                   DocumentationContext documentationContext,
                                                   FlowType FlowType, String tagName) {
        RequestHandlerKey requestHandlerKey = handler.key();
        final String value = FlowType.value();
        OperationBuilder operationBuilder = new OperationBuilder(new CachingOperationNameGenerator())
                .summary(value)
                .notes(value)
                .tags(CollUtil.newHashSet(tagName + "-" + value));

        final ApiDescriptionBuilder builder = new ApiDescriptionBuilder(
                documentationContext.operationOrdering());
        builder.description(value)
                .groupName(documentationContext.getGroupName())
                .hidden(false);
        List<ApiDescription> apiDescriptionList = new ArrayList<>();
        Iterator<RequestMethod> methodIterator = requestHandlerKey.getSupportedMethods().iterator();
        Iterator<String> pathIterator = requestHandlerKey.getPathMappings().iterator();
        while (methodIterator.hasNext()) {
            List<Parameter> parameters = createParameter(FlowType,
                    requestHandlerKey.getSupportedMediaTypes(), operationBuilder.build().getMethod());
            // 设置参数
            operationBuilder.parameters(parameters);
            operationBuilder.uniqueId(value + IdUtil.fastUUID());
            while (pathIterator.hasNext()) {
                // 设置请求路径
                builder.path(pathIterator.next());
                List<Operation> operations = Arrays.asList(operationBuilder.build());
                apiDescriptionList.add(builder.operations(operations).build());
            }
        }
        return apiDescriptionList;
    }


    /**
     * 解析参数
     *
     * @param FlowType
     * @param consumes
     * @param method
     * @return
     */
    private List<Parameter> createParameter(FlowType FlowType,
                                            Set<? extends MediaType> consumes, HttpMethod method) {
        final Class<?> paramType = FlowType.paramType();
        final Map<String, Field> fieldMap = ReflectUtil.getFieldMap(paramType);
        return fieldMap.entrySet().stream().map(kv -> {
            Field field = kv.getValue();
            ApiModelProperty annotation = field.getAnnotation(ApiModelProperty.class);
            ParameterBuilder parameterBuilder = new ParameterBuilder();
            ResolvedType resolve = typeResolver.resolve(field.getType());
            return parameterBuilder.description(annotation.value())
                    //参数数据类型
                    .type(resolve)
                    //参数名称
                    .name(field.getName())
                    //参数默认值
                    .defaultValue(annotation.name())
                    //参数类型 query、form、formdata
                    .parameterType(findParameterType(resolve, consumes, method))
                    .parameterAccess(annotation.access())
                    //是否必填
                    .required(annotation.required())
                    //参数数据类型
                    .modelRef(modelReference(resolve)).build();
        }).collect(Collectors.toList());
    }

    /**
     * 设置返回值model
     *
     * @param type
     * @return
     */
    private ModelReference modelReference(ResolvedType type) {
        if (Void.class.equals(type.getErasedType()) || Void.TYPE.equals(type.getErasedType())) {
            return new ModelRef("void");
        }
        if (MultipartFile.class.isAssignableFrom(type.getErasedType()) || isListOfFiles(type)) {
            return new ModelRef("__file");
        }
        return new ModelRef(
                type.getTypeName(),
                type.getBriefDescription(),
                null,
                ResolvedTypes.allowableValues(type),
                type.getBriefDescription());
    }

    private static String findParameterType(ResolvedType resolvedType,
                                            Set<? extends MediaType> consumes, HttpMethod method) {
        //Multi-part file trumps any other annotations
        if (isFileType(resolvedType) || isListOfFiles(resolvedType)) {
            return "form";
        } else {
            return determineScalarParameterType(consumes, method);
        }
    }

    private static String determineScalarParameterType(Set<? extends MediaType> consumes,
                                                       HttpMethod method) {
        String parameterType = "query";
        if (consumes.contains(MediaType.APPLICATION_FORM_URLENCODED)
                && method == HttpMethod.POST) {
            parameterType = "form";
        } else if (consumes.contains(MediaType.MULTIPART_FORM_DATA)
                && method == HttpMethod.POST) {
            parameterType = "formData";
        }
        return parameterType;
    }

    private static boolean isListOfFiles(ResolvedType parameterType) {
        return isContainerType(parameterType) && isFileType(collectionElementType(parameterType));
    }

    private static boolean isFileType(ResolvedType parameterType) {
        return MultipartFile.class.isAssignableFrom(parameterType.getErasedType());
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return DocumentationType.SWAGGER_2.equals(documentationType);
    }

    @Builder(toBuilder = true)
    @Data
    public static class GeneratedApis {
        List<ApiDescription> ads;
        // 来源于哪个group
        //String groupNameOfSource;
    }
}
