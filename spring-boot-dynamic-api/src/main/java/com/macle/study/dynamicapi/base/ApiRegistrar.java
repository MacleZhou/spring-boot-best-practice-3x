package com.macle.study.dynamicapi.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ApiRegistrar {
    @Autowired
    private List<DynamicApi> dynamicApis;

    @Autowired
    private RequestHandler requestHandler;

    public void registerApis() {
        for (DynamicApi api : dynamicApis) {
            String apiPath = generateApiPath(api.getClass());
            System.out.println(apiPath);
            requestHandler.registerApi(apiPath, api);
        }
    }

    private String generateApiPath(Class<?> apiClass) {
        // 这里可以根据需要生成接口路径，例如基于类名
        return "/" + apiClass.getSimpleName().toLowerCase();
    }
}
