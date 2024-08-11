package com.macle.study.deployment;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Modifier;

public class SpringAnnotationUtils {
    /**
     * 判断一个类是否有 Spring 核心注解
     *
     * @param clazz 要检查的类
     * @return true 如果该类上添加了相应的 Spring 注解；否则返回 false
     */
    public static SpringBean hasSpringAnnotation(Class<?> clazz) {
        SpringBean springClass = new SpringBean();
        if (clazz == null) {
            springClass.setSpringBean(false);
            return springClass;
        }
        //是否是接口
        if (clazz.isInterface()) {
            springClass.setSpringBean(false);
            return springClass;
        }
        //是否是抽象类
        if (Modifier.isAbstract(clazz.getModifiers())) {
            springClass.setSpringBean(false);
            return springClass;
        }

        try {
            Component component = clazz.getAnnotation(Component.class);
            if(component != null){
                springClass.setSpringBean(true);
                springClass.setType("component");
                if(StringUtils.isNotBlank(component.value())){
                    springClass.setBeanName(component.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }


            Repository repository = clazz.getAnnotation(Repository.class);
            if (repository != null) {
                springClass.setSpringBean(true);
                springClass.setType("repository");
                if (StringUtils.isNotBlank(repository.value())) {
                    springClass.setBeanName(repository.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }

            Service service = clazz.getAnnotation(Service.class);
            if (service != null) {
                springClass.setSpringBean(true);
                springClass.setType("service");
                if (StringUtils.isNotBlank(service.value())) {
                    springClass.setBeanName(service.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }

            Controller controller = clazz.getAnnotation(Controller.class);
            if (controller != null) {
                springClass.setSpringBean(true);
                springClass.setType("controller");
                if (StringUtils.isNotBlank(controller.value())) {
                    springClass.setBeanName(controller.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }

            RestController restController = clazz.getAnnotation(RestController.class);
            if (restController != null) {
                springClass.setSpringBean(true);
                springClass.setType("restController");
                if (StringUtils.isNotBlank(restController.value())) {
                    springClass.setBeanName(restController.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }

            Configuration configuration = clazz.getAnnotation(Configuration.class);
            if (configuration != null) {
                springClass.setSpringBean(true);
                if (StringUtils.isNotBlank(configuration.value())) {
                    springClass.setBeanName(configuration.value());
                }
                else {
                    springClass.setBeanName(generateSpringBeanName(clazz));
                }
                return springClass;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return springClass;
    }

    private static String generateSpringBeanName(Class<?> clazz) {
        String className = clazz.getName();
        String tmpstr = className.substring(className.lastIndexOf(".") + 1);
        return tmpstr.substring(0, 1).toLowerCase() + tmpstr.substring(1);
    }

    @Setter @Getter
    public static class SpringBean {
        private boolean springBean;

        private String beanName;

        private String type;
    }
}
