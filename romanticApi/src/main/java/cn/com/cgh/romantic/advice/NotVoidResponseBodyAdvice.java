package cn.com.cgh.romantic.advice;

import cn.com.cgh.romantic.util.ResponseImpl;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * 非空返回值的处理.
 *
 * @author cgh
 * @version 0.1
 * @since 0.1
 */
@Aspect
@ControllerAdvice
@Order(value = 1000)
public class NotVoidResponseBodyAdvice {

    @Around(value = "execution(* org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.handleResult(..)) && args(exchange, result)", argNames = "point,exchange,result")
    public Object handleResult(ProceedingJoinPoint point, ServerWebExchange exchange, HandlerResult result) throws Throwable {
        Object body = result.getReturnValue();
        if (body instanceof Mono || body instanceof Flux || body == null){
            return point.proceed();
        } else if (!(body instanceof ResponseImpl)) {
            return point.proceed(Arrays.asList(
                    exchange,
                    new HandlerResult(result.getHandler(), Mono.just(ResponseImpl.builder().data(body).build().success()), result.getReturnTypeSource())
            ).toArray());
        }
        return point.proceed();
    }
}