package cn.com.cgh.romantic.config.aspect;

import cn.com.cgh.romantic.interfac.ClearAndReloadCache;
import cn.com.cgh.romantic.util.SpelUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @author cgh
 */
@Aspect
@Component
public class ClearAndReloadCacheAspect {

    @Autowired
    private RedisTemplate<String, Object> redisTemplateSO;

    @Autowired
    private ClearAsynRun clearAsynRun;

    /**
     * 切入点
     * 切入点,基于注解实现的切入点  加上该注解的都是Aop切面的切入点
     */
    @Pointcut("@annotation(cn.com.cgh.romantic.interfac.ClearAndReloadCache)")
    public void pointCut() {

    }

    /**
     * 环绕通知
     * 环绕通知非常强大，可以决定目标方法是否执行，什么时候执行，执行时是否需要替换方法参数，执行完毕是否需要替换返回值。
     * 环绕通知第一个参数必须是org.aspectj.lang.ProceedingJoinPoint类型
     *
     * @param proceedingJoinPoint
     */
    @Around("pointCut()")
    public Object aroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        Signature signature1 = proceedingJoinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature1;
        //方法对象
        Method targetMethod = methodSignature.getMethod();
        //反射得到自定义注解的方法对象
        ClearAndReloadCache annotation = targetMethod.getAnnotation(ClearAndReloadCache.class);
        String name = "null";
        //获取自定义注解的值，是否使用el表达式
        if (annotation != null) {
            if (StringUtils.isNotBlank(annotation.name())) {
                name = annotation.name();
            }
            //注解上的描述
            if (StringUtils.isNotBlank(annotation.spelName())) {
                name = SpelUtil.generateKeyBySpEL(annotation.spelName(), proceedingJoinPoint);
            }
        }

        Set<String> keys = redisTemplateSO.keys(name);
        //確切刪除
        //模糊删除redis的key值
        if (keys != null && !keys.isEmpty()) {
            redisTemplateSO.delete(keys);
        }
        System.out.println("环绕通知的目标方法名：" + proceedingJoinPoint.getSignature().getName() + ",keys=" + name);
        //执行加入双删注解的改动数据库的业务 即controller中的方法业务
        Object proceed = null;
        try {
            proceed = proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        //开一个线程 延迟1秒（此处是1秒举例，可以改成自己的业务）
        // 在线程中延迟删除  同时将业务代码的结果返回 这样不影响业务代码的执行
        //確切刪除
        Set<String> keys1 = redisTemplateSO.keys(name);
        clearAsynRun.clearRedisCache(keys1);
        //返回业务代码的值
        return proceed;
    }
}