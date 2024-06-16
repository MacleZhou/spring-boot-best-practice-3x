package com.macle.swagger2.config;

import cn.hutool.core.util.ReflectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import springfox.documentation.schema.ModelReference;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListing;
import springfox.documentation.service.Documentation;
import springfox.documentation.service.Operation;
import springfox.documentation.spring.web.DocumentationCache;
import springfox.documentation.spring.web.scanners.ApiDescriptionLookup;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * <p> 搭配 {@code SwaggerAddApiDescriptionPlugin } 实现新增的 ApiDescription属性填充
 * <p> 需要确保执行时机低于 {@code DocumentationPluginsBootstrapper}
 * <p> 但{@code DocumentationPluginsBootstrapper} 这个玩意的执行时机为最低
 * <p> 所以我们转而实现 ApplicationListener<ContextRefreshedEvent>
 * @author fulizhe
 *
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class SwaggerAddAddtionApiDescriptionWithDeferPushValue implements ApplicationListener<ContextRefreshedEvent> {

    private AtomicBoolean initialized = new AtomicBoolean(false);

    private final ApiDescriptionLookup lookup;

    private final SwaggerAddApiDescriptionPlugin swaggerAddApiDescriptionPlugin;

    @Autowired
    private DocumentationCache cocumentationCache;

    public SwaggerAddAddtionApiDescriptionWithDeferPushValue(ApiDescriptionLookup lookup,
                                                             SwaggerAddApiDescriptionPlugin swaggerAddApiDescriptionPlugin) {
        super();
        this.lookup = lookup;
        this.swaggerAddApiDescriptionPlugin = swaggerAddApiDescriptionPlugin;
    }

    void start() {
        if (initialized.compareAndSet(false, true)) {
            if (swaggerAddApiDescriptionPlugin.getNeedDealed().isEmpty()) {
                initialized.compareAndSet(true, false);
                return;
            }
            swaggerAddApiDescriptionPlugin.getNeedDealed().forEach((k, v) -> {
                if (v.ads.isEmpty()) {
                    return;
                }

                ApiDescription sourceDescription = lookup.description(k);
                if (!Objects.isNull(sourceDescription)) { // 如果将 OneInterfaceMultiApiDescriptionController.createFlowService() 设置为hidden, 则这里判断失败
                    List<ApiDescription> ads = v.ads;
                    ApiDescription first = ads.get(0);

                    // 这里所复制的就是请求方式，请求数据类型等等这些信息
                    copyProperties(sourceDescription.getOperations().get(0), first.getOperations().get(0));

                    // ============================== 设置返回值
                    // 这里的思路是这样的:
                    // 1. swagger中对于自定义类型的返回值显示采取的是 ref 引用的方式. (这一点可以随便找个swagger文档F12看一下), 同时将ref所引用的model定义放在整个接口最外层的definitions字段中
                    // 2. 在上面的copyProperties(...)中我们已经复制response相关信息, 接下来我们就只需要将definitions相关信息拷贝到当前document之下就大功告成了
                    Documentation matchedSourceDocumentationByGroup = matchedSourceDocumentationByGroup(
                            sourceDescription);

                    Documentation targetDocumentationByGroup = cocumentationCache
                            .documentationByGroup(first.getGroupName().get());

                    Map<String, List<ApiListing>> tartgetApiListings = targetDocumentationByGroup.getApiListings();

                    String srouceGroupName = sourceDescription.getGroupName().get();
                    List<ApiListing> list = matchedSourceDocumentationByGroup.getApiListings().get(srouceGroupName);

                    // 确保返回值正常显示
                    list.forEach(xv -> {
                        tartgetApiListings.forEach((yk, yv) -> {
                            yv.forEach(m -> ReflectUtil.setFieldValue(m, "models", xv.getModels()));
                        });
                    });
                }

            });
        }

    }

    private Documentation matchedSourceDocumentationByGroup(ApiDescription sourceDescription) {
        String srouceGroupName = sourceDescription.getGroupName().get();

        Optional<Documentation> findFirst = cocumentationCache.all().values().stream()
                .filter(v -> v.getApiListings().keySet().contains(srouceGroupName)).findFirst();

        return findFirst.get();
    }

    private void copyProperties(Operation src, Operation dest) {
        final HttpMethod method = src.getMethod();
        ReflectUtil.setFieldValue(dest, "method", method);

        final ModelReference responseModelOfSource = src.getResponseModel();
        ReflectUtil.setFieldValue(dest, "responseModel", responseModelOfSource);

        final int position = src.getPosition();
        ReflectUtil.setFieldValue(dest, "position", position);

        final Set<String> produces = src.getProduces();
        ReflectUtil.setFieldValue(dest, "produces", produces);

        final Set<String> consumes = src.getConsumes();
        ReflectUtil.setFieldValue(dest, "consumes", consumes);

        final Set<String> protocol = src.getProtocol();
        ReflectUtil.setFieldValue(dest, "protocol", protocol);

        ReflectUtil.setFieldValue(dest, "isHidden", src.isHidden());

        ReflectUtil.setFieldValue(dest, "securityReferences", src.getSecurityReferences());

        ReflectUtil.setFieldValue(dest, "responseMessages", src.getResponseMessages());

        ReflectUtil.setFieldValue(dest, "deprecated", src.getDeprecated());

        ReflectUtil.setFieldValue(dest, "vendorExtensions", src.getVendorExtensions());

        // 不拷貝以下屬性
        //	summary, notes, uniqueId, tags, parameters

        // 無效, 这个拷贝需要目标属性有setXX方法
        //	BeanUtil.copyProperties(src, dest, "parameters", "uniqueId", "summary", "notes", "tags");
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        start();
    }
}
