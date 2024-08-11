package com.macle.study.controller;

import com.macle.study.deployment.DynamicLoad;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DynamicLoaderController {

    @Resource
    private DynamicLoad dynamicLoad;

    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/load-jar")
    public String load() throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        String className = "LOAD CHECK != NULL :::";

        Object object = null;
        Object object1 = null;
        Object object2 = null;
        Object object3 = null;
        Object object4 = null;
        try {
            object = applicationContext.getBean("calculator");
        }
        catch (Exception ex){
            System.out.println("BEFORE");
            ex.printStackTrace();
        }

        className += "; BEFORE = " + (null != object);

        dynamicLoad.loadJar("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-dynamic-jar-loading/spring-boot-dynamic-jar-loading-test/target", "spring-boot-dynamic-jar-loading-test-1.0.jar");

        try {
            object = applicationContext.getBean("calculator");
            object1 = applicationContext.getBean("calculator");
            object2 = applicationContext.getBean("calculatorCore");
            object3 = applicationContext.getBean("testController");
            object4 = applicationContext.getBean(Class.forName("com.macle.study.deployment.service.Calculator"));
        }
        catch (Exception ex){
            System.out.println("LOAD AFTER EXCEPTION");
            ex.printStackTrace();
            System.out.println("LOAD AFTER EXCEPTION DONE");
        }
        className += "; AFTER = " + (null != object);
        className += "; AFTER1 = " + (null != object1);
        className += "; AFTER2 = " + (null != object2);
        className += "; AFTER3 = " + (null != object3);
        className += "; AFTER4 = " + (null != object4);
        return className;
    }

    @GetMapping("/unload-jar")
    public String unload() throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException {
        String className = "UNLOAD CHEECK != NULL::: ";

        Object object = null;
        Object object1 = null;
        Object object2 = null;
        Object object3 = null;
        Object object4 = null;
        try {
            object = applicationContext.getBean("calculator");
        }
        catch (Exception ex){
            System.out.println("BEFORE");
            ex.printStackTrace();
        }

        className += "; BEFORE = " + (null != object);

        dynamicLoad.unloadJar("/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-dynamic-jar-loading/spring-boot-dynamic-jar-loading-test/target", "spring-boot-dynamic-jar-loading-test-1.0.jar");

        try {
            object1 = applicationContext.getBean("calculator");
            object2 = applicationContext.getBean("calculatorCore");
            object3 = applicationContext.getBean("testController");
            object4 = applicationContext.getBean(Class.forName("com.macle.study.deployment.service.Calculator"));
        }
        catch (Exception ex){
            System.out.println("UNLOAD AFTER EXCEPTION");
            ex.printStackTrace();
            System.out.println("UNLOAD AFTER EXCEPTION DONE");
        }
        className += "; AFTER1 = " + (null != object1);
        className += "; AFTER2 = " + (null != object2);
        className += "; AFTER3 = " + (null != object3);
        className += "; AFTER4 = " + (null != object4);
        return className;

    }
}
