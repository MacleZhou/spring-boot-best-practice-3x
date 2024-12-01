package com.atshuo.audit.dataPermission;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 类 MybatisPlusCustomConfig
 *
 * @author ChenQi
 * @date 2023/3/28
 */
@Configuration
public class MybatisPlusCustomConfig {

    @Bean
    public MybatisPlusInterceptor customMybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 数据权限
        DataPermissionInterceptor dataPermissionInterceptor = new DataPermissionInterceptor(new CustomizedDataPermissionHandlerImpl());
        interceptor.addInnerInterceptor(dataPermissionInterceptor);
        return interceptor;
    }
}