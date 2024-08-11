package com.macle.study.deployment;
import io.swagger.v3.oas.models.OpenAPI;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.api.AbstractOpenApiResource;
import org.springdoc.core.providers.SpringDocProviders;
import org.springdoc.core.providers.SpringWebProvider;
import org.springdoc.core.service.OpenAPIService;
import org.springdoc.webmvc.api.MultipleOpenApiResource;
import org.springdoc.webmvc.api.OpenApiResource;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

@Component
public class DynamicLoad {

    private static Logger logger = LoggerFactory.getLogger(DynamicLoad.class);

    @Autowired
    private ApplicationContext applicationContext;

    private MultipleOpenApiResource multipleOpenApiResource;
    private SpringDocProviders springDocProviders;

    private Map<String, MyClassLoader> myClassLoaderCenter = new ConcurrentHashMap<>();

    private Map<String, List<String>> controllerBeanMap = new ConcurrentHashMap<>();

    /**
     * 动态加载指定路径下指定jar包
     * @param path
     * @param fileName
     * */
    public void loadJar(String path, String fileName) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        File file = new File(path +"/" + fileName);
        System.out.println("file.exist=" + file.exists());
        Map<String, String> jobPar = new HashMap<>();
        // 获取beanFactory
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        // 获取当前项目的执行器
        try {
            // URLClassloader加载jar包规范必须这么写
            URL url = new URL("jar:file:" + file.getAbsolutePath() + "!/");
            URLConnection urlConnection = url.openConnection();
            JarURLConnection jarURLConnection = (JarURLConnection)urlConnection;
            // 获取jar文件
            JarFile jarFile = jarURLConnection.getJarFile();
            Enumeration<JarEntry> entries = jarFile.entries();

            // 创建自定义类加载器，并加到map中方便管理
            MyClassLoader myClassloader = new MyClassLoader(new URL[] { url }, ClassLoader.getSystemClassLoader());
            myClassLoaderCenter.put(fileName, myClassloader);
            Set<Class> initBeanClass = new HashSet<>(jarFile.size());
            // 遍历文件
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    // 1. 加载类到jvm中
                    // 获取类的全路径名
                    String className = jarEntry.getName().replace('/', '.').substring(0, jarEntry.getName().length() - 6);
                    // 1.1进行反射获取
                    myClassloader.loadClass(className);
                }
            }
            Map<String, Class<?>> loadedClasses = myClassloader.getLoadedClasses();
            List<String> controllers = new ArrayList<>();
            for(Map.Entry<String, Class<?>> entry : loadedClasses.entrySet()){
                String className = entry.getKey();
                Class<?> clazz = entry.getValue();
                // 2. 将有@spring注解的类交给spring管理
                // 2.1 判断是否注入spring
                SpringAnnotationUtils.SpringBean springClass = SpringAnnotationUtils.hasSpringAnnotation(clazz);
                if(springClass.isSpringBean()){
                    // 2.2交给spring管理
                    BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
                    AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();
                    // 此处beanName使用全路径名是为了防止beanName重复
                    //String packageName = className.substring(0, className.lastIndexOf(".") + 1);
                    //String beanName = className.substring(className.lastIndexOf(".") + 1);
                    //beanName = packageName + beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
                    // 2.3注册到spring的beanFactory中
                    beanFactory.registerBeanDefinition(springClass.getBeanName(), beanDefinition);
                    // 2.4允许注入和反向注入
                    beanFactory.autowireBean(clazz);
                    beanFactory.initializeBean(clazz, springClass.getBeanName());

                    if(StringUtils.equalsAnyIgnoreCase(springClass.getType(), "controller", "restController")){
                        controllers.add(springClass.getBeanName());
                    }
                    /*
                    if(Arrays.stream(clazz.getInterfaces()).collect(Collectors.toSet()).contains(InitializingBean.class)){
                        initBeanClass.add(clazz);
                    }*/
                    initBeanClass.add(clazz);
                }

            }
            // spring bean实际注册
            initBeanClass.forEach(beanFactory::getBean);

            this.controllerBeanMap.put(fileName, controllers);

            // 配置Controller到RequestMappingHandlerMapping中，这样API就可以调用了
            controllers.forEach(controllerBeanName -> {
                RequestMappingHandlerMapping handlerMapping = applicationContext.getBean(RequestMappingHandlerMapping.class);
                // 注册Controller
                Method method = null;
                try {
                    method = handlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredMethod("detectHandlerMethods", Object.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                // 将private改为可使用
                assert method != null;
                method.setAccessible(true);
                try {
                    method.invoke(handlerMapping, controllerBeanName);

                    Object bean = beanFactory.getBean(controllerBeanName);
                    //刷新open api缓存，包括刷新openAPIService内部的mappingsMap和springWebProvider内部的handlerMethods
                    /**
                    OpenAPIService openAPIService = getOpenApiService();
                    openAPIService.addMappings(Map.of(bean.toString(), bean));
                    Optional<SpringWebProvider> springWebProvider = springDocProviders.getSpringWebProvider();
                    if (springWebProvider.isPresent()) {
                        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
                        springWebProvider.get().getHandlerMethods().putAll(handlerMethods);
                    }

                    // 反射获取 openAPIService中的缓存
                    Field cachedOpenAPIField = OpenAPIService.class.getDeclaredField("cachedOpenAPI");
                    ReflectionUtils.makeAccessible(cachedOpenAPIField);
                    Map<String, OpenAPI> cache = (Map<String, OpenAPI>) cachedOpenAPIField.get(openAPIService);
                    cache.clear();
                     */
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            // 获取容器中所有Bean的名称
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            // 打印每个Bean的名称和对应的类型
            for (String beanName : beanNames) {
                Object bean = applicationContext.getBean(beanName);
                System.out.println("Bean名称: " + beanName + ", 类型: " + bean.getClass().getName());
            }

        } catch (IOException e) {
            logger.error("读取{} 文件异常", fileName);
            e.printStackTrace();
            throw new RuntimeException("读取jar文件异常: " + fileName);
        }
    }


    /**
     * 动态卸载指定路径下指定jar包
     * @param fileName
     * @return map<jobHander, Cron> 创建xxljob任务时需要的参数配置
     */
    public void unloadJar(String path, String fileName) throws IllegalAccessException, NoSuchFieldException {
        // 获取加载当前jar的类加载器
        MyClassLoader myClassLoader = myClassLoaderCenter.get(fileName);

        // 获取beanFactory，准备从spring中卸载
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
        Map<String, Class<?>> loadedClasses = myClassLoader.getLoadedClasses();
        RequestMappingHandlerMapping requestMappingHandlerMapping=(RequestMappingHandlerMapping)applicationContext.getBean("requestMappingHandlerMapping");
        Set<String> beanNames = new HashSet<>();
        for (Map.Entry<String, Class<?>> entry: loadedClasses.entrySet()) {
            // 1. 将xxljob任务从xxljob执行器中移除
            // 1.1 截取beanName
            String key = entry.getKey();
            String packageName = key.substring(0, key.lastIndexOf(".") + 1);
            String beanName = key.substring(key.lastIndexOf(".") + 1);
            beanName = packageName + beanName.substring(0, 1).toLowerCase() + beanName.substring(1);

            // 获取bean，如果获取失败，表名这个类没有加到spring容器中，则跳出本次循环
            Object bean = null;
            try{
                bean = applicationContext.getBean(beanName);

                //如果是controller类
                if(bean != null && this.controllerBeanMap.get(fileName).contains(beanName)){
                    //从RequestMappingHandlerMapping移除controller
                    Class<?> targetClass = bean.getClass();
                    /**
                    ReflectionUtils.doWithMethods(targetClass, new ReflectionUtils.MethodCallback() {
                        @Override
                        public void doWith(Method method) {
                            Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
                            try {
                                Method createMappingMethod = RequestMappingHandlerMapping.class.getDeclaredMethod("getMappingForMethod", Method.class, Class.class);
                                createMappingMethod.setAccessible(true);
                                RequestMappingInfo requestMappingInfo =(RequestMappingInfo)createMappingMethod.invoke(requestMappingHandlerMapping, specificMethod, targetClass);
                                if(requestMappingInfo != null) {
                                    requestMappingHandlerMapping.unregisterMapping(requestMappingInfo);
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, ReflectionUtils.USER_DECLARED_METHODS);
                     */
                }

            }catch (Exception e){
                // 异常说明spring中没有这个bean
                continue;
            }

            // 2.0从spring中移除，这里的移除是仅仅移除的bean，并未移除bean定义
            beanNames.add(beanName);
            beanFactory.destroyBean(beanName, bean);
        }

        // 移除bean定义
        Field mergedBeanDefinitions = beanFactory.getClass()
                .getSuperclass()
                .getSuperclass().getDeclaredField("mergedBeanDefinitions");
        mergedBeanDefinitions.setAccessible(true);
        Map<String, RootBeanDefinition> rootBeanDefinitionMap = ((Map<String, RootBeanDefinition>) mergedBeanDefinitions.get(beanFactory));
        for (String beanName : beanNames) {
            beanFactory.removeBeanDefinition(beanName);
            // 父类bean定义去除
            rootBeanDefinitionMap.remove(beanName);
        }

        // 3.2 从类加载中移除
        try {
            // 从类加载器底层的classes中移除连接
            Field field = ClassLoader.class.getDeclaredField("classes");
            field.setAccessible(true);
            Vector<Class<?>> classes = (Vector<Class<?>>) field.get(myClassLoader);
            classes.removeAllElements();
            // 移除类加载器的引用
            myClassLoaderCenter.remove(fileName);
            // 卸载类加载器
            myClassLoader.unload();
        } catch (NoSuchFieldException e) {
            logger.error("动态卸载的类，从类加载器中卸载失败");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            logger.error("动态卸载的类，从类加载器中卸载失败");
            e.printStackTrace();
        }
        logger.error("{} 动态卸载成功", fileName);
    }

    private OpenAPIService getOpenApiService() {
        try {
            //反射获取openApiResource
            Method getOpenApiResource = MultipleOpenApiResource.class.getDeclaredMethod("getOpenApiResourceOrThrow", String.class);
            ReflectionUtils.makeAccessible(getOpenApiResource);
            OpenApiResource openApiResource = (OpenApiResource) getOpenApiResource.invoke(multipleOpenApiResource, "dynamic");

            // 反射获取 openAPIService
            Field openAPIServiceField = AbstractOpenApiResource.class.getDeclaredField("openAPIService");
            ReflectionUtils.makeAccessible(openAPIServiceField);
            return (OpenAPIService) openAPIServiceField.get(openApiResource);
        } catch (Exception e) {
            System.out.println("获取反射获取openApiResource和openApiService失败");
        }
        return null;
    }
}
