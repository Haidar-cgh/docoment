package cn.com.cgh.romantic.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@EnableFeignClients
@PropertySource(value = "classpath:bootstrap.properties")
@ComponentScan(value = "cn.com.cgh.romantic")
@Slf4j
public class OpenfeignConfig {
    static {
        log.info("OpenfeignConfig:已启动");
    }

    public AuthRequestInterceptor authRequestInterceptor(){
        return new AuthRequestInterceptor();
    }
}