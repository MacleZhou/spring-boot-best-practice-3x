package com.macle.study.smartdi.annotation;

import com.burukeyou.smartdi.proxyspi.factory.AnnotationProxyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DbProxyFactory implements AnnotationProxyFactory<DBProxySPI> {

    //@Autowired
    //private SysConfigMapper sysConfigDao;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public Object getProxy(Class<?> targetClass, DBProxySPI spi) {
        // 根据注解从数据库获取要注入的实现类
        //String configName = sysConfigDao.getConfig(spi.value());
        String configName = "TencentSMSService2";
        return applicationContext.getBean(configName);
    }
}
