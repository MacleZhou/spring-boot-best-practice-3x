package com.macle.study.deployment;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;

public class DeploymentService {

    private static String jarAddress = "/Users/maclezhou/JavaWorld/spring-boot-best-practice-3x/spring-boot-dynamic-jar-loading/spring-boot-dynamic-jar-loading-test/target/spring-boot-dynamic-jar-loading-test-1.0.jar";
    private static String jarPath = "file:/" + jarAddress;

    /**
     * 反射方式热部署 - 热加载Calculator接口的实现 反射方式
     * 用户把jar包上传到系统的指定目录下，这里定义上传jar文件路径为jarAddress，jar的Url路径为jarPath。
     * 并且可以要求用户填写jar包中接口实现类的完整类名。接下来系统要把上传的jar包加载到当前线程的类加载器中，然后通过完整类名，加载得到该实现的Class对象。
     * 然后反射调用即可，完整代码：
     *
     */
    public static void hotDeployWithReflect() throws Exception {
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(jarPath)}, Thread.currentThread().getContextClassLoader());
        Class clazz = urlClassLoader.loadClass("com.nci.cetc15.calculator.impl.CalculatorImpl");
        //Calculator calculator = (Calculator) clazz.newInstance();
        //int result = calculator.add(1, 2);
        //System.out.println(result);
    }

    /**
     * 注解方式热部署
     *
     * 如果用户上传的jar包含了spring的上下文，那么就需要扫描jar包里的所有需要注入spring容器的bean，注册到当前系统的spring容器中。
     * 其实，这就是一个类的热加载+动态注册的过程。
     * */
    /**
     * 加入jar包后 动态注册bean到spring容器，包括bean的依赖
     */
    public static void hotDeployWithSpring() throws Exception {
        Set<String> classNameSet = DeployUtils.readJarFile(jarAddress);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(jarPath)}, Thread.currentThread().getContextClassLoader());
        for (String className : classNameSet) {
            Class clazz = urlClassLoader.loadClass(className);
            if (DeployUtils.isSpringBeanClass(clazz)) {
                BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                DeployUtils.defaultListableBeanFactory.registerBeanDefinition(DeployUtils.transformName(className), beanDefinitionBuilder.getBeanDefinition());
            }
        }
    }

    /**
     * 删除jar包时 需要在spring容器删除注入
     */
    public static void delete() throws Exception {
        Set<String> classNameSet = DeployUtils.readJarFile(jarAddress);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new URL(jarPath)}, Thread.currentThread().getContextClassLoader());
        for (String className : classNameSet) {
            Class clazz = urlClassLoader.loadClass(className);
            if (DeployUtils.isSpringBeanClass(clazz)) {
                DeployUtils.defaultListableBeanFactory.removeBeanDefinition(DeployUtils.transformName(className));
            }
        }
    }
}
