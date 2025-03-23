package cn.javastack.springboot.jpa.annotation;

import cn.javastack.springboot.jpa.handler.ResultHandler;
import cn.javastack.springboot.jpa.handler.DefaultResultHandler;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Parameter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Aspect
@Component
public class SplitQueryAspect implements ApplicationContextAware {
    /**默认使用虚拟线程*/
    private static final Executor defaultExecutor = Executors.newSingleThreadExecutor();

    private ApplicationContext context ;

    @Pointcut("@annotation(sq)")
    private void splitPc(SplitQuery sq) {}

    @Around("splitPc(sq)")
    public Object splitQueryAround(ProceedingJoinPoint pjp, SplitQuery sq) throws Throwable {
        int batchSize = sq.batchSize() ;
        Executor executor = getExecutor(sq.executorName()) ;
        Object[] args = pjp.getArgs() ;
        MethodSignature ms = (MethodSignature) pjp.getSignature() ;

        Parameter[] parameters = ms.getMethod().getParameters() ;
        int index = -1 ;
        for (int i = 0, len = parameters.length; i < len; i++) {
            Parameter param = parameters[i] ;
            BatchParam batchParam = param.getAnnotation(BatchParam.class) ;
            if (batchParam != null) {
                index = i ;
                break ;
            }
        }
        Object arg = args[index] ;
        // 这里只考虑了参数集合是List情况
        if (index == -1
                || !List.class.isAssignableFrom(arg.getClass())
                || ((List<?>) arg).size() <= batchSize) {
            log.info("直接调用目标方法...") ;
            return pjp.proceed() ;
        }
        ResultHandler<?> resultHandler = getResultHandler(sq.handlerName()) ;
        final int paramIndex = index ;
        List<?> data = (List<?>) arg ;
        // 这里我们使用的guava进行拆分集合
        List<?> partitions = Lists.partition(data, batchSize) ;
        List<Object> result = partitions.stream().map(chunk -> {
                    return CompletableFuture.supplyAsync(() -> {
                        try {
                            Object[] newArgs = new Object[args.length] ;
                            System.arraycopy(args, 0, newArgs, 0, args.length) ;
                            newArgs[paramIndex] = chunk ;
                            log.info("处理批次数据: {}", newArgs[paramIndex]) ;
                            return pjp.proceed(newArgs) ;
                        } catch (Throwable e) {
                            return null ;
                        }
                    }, executor) ; // 设置线程池
                }).collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                // 过滤数据为null或空的情况
                .filter(obj -> obj != null && !((List<?>)obj).isEmpty())
                .collect(Collectors.toList()) ;
        return resultHandler.process(result) ;
    }

    private Executor getExecutor(String executorName) {
        if (StringUtils.hasLength(executorName)) {
            try {
                return this.context.getBean(executorName, Executor.class) ;
            } catch (Exception e) {
                log.warn("不存beanName为: {} 的线程池,将使用默认的虚拟线程池对象", executorName);
                return defaultExecutor ;
            }
        }
        return defaultExecutor ;
    }
    private ResultHandler<?> getResultHandler(String handlerName) {
        if (StringUtils.hasLength(handlerName)) {
            try {
                return this.context.getBean(handlerName, ResultHandler.class) ;
            } catch (Exception e) {
                log.warn("不存beanName为: {} 的结果处理器,将使用DefaultResultHandler", handlerName);
                return new DefaultResultHandler() ;
            }
        }
        return new DefaultResultHandler() ;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context ;
    }
}