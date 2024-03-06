package cn.com.cgh.auth.handler;

import cn.com.cgh.romantic.em.LoginStatus;
import cn.com.cgh.romantic.pojo.MsgPojo;
import cn.com.cgh.romantic.pojo.oasis.TbLoginLog;
import cn.com.cgh.romantic.pojo.resource.TbCfgUser;
import cn.com.cgh.romantic.util.JwtTokenUtil;
import cn.com.cgh.romantic.util.ResponseImpl;
import cn.com.cgh.romantic.util.SendQueue;
import cn.hutool.json.JSONUtil;
import cn.hutool.jwt.JWTPayload;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static cn.com.cgh.romantic.constant.RomanticConstant.THREAD_LOCAL_LOG_ID;
import static cn.com.cgh.romantic.constant.RomanticConstant.X_REAL_IP;

/**
 * @author cgh
 */
@Slf4j
@Component
public class TokenSuccessHandler implements ServerAuthenticationSuccessHandler {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SendQueue sendQueue;


    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        log.info("登录成功");
        ServerHttpRequest request = webFilterExchange.getExchange().getRequest();
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        TbCfgUser securityUser = (TbCfgUser) authentication.getPrincipal();
        Map<String, String> payload = new HashMap<>();
        String id = request.getHeaders().getFirst(THREAD_LOCAL_LOG_ID);
        payload.put(JWTPayload.JWT_ID, id);
        Map<String, Object> map = jwtTokenUtil.generateTokenAndRefreshToken(securityUser.getId(), securityUser.getUsername(), payload);
        if (StringUtils.isNotEmpty(id)) {
            TbLoginLog loginLog = TbLoginLog.builder()
                    .username(securityUser.getUsername())
                    .userId(securityUser.getId())
                    .clientIp(request.getHeaders().getFirst(X_REAL_IP))
                    .loginStatus(LoginStatus.SUCCESS)
                    .userAgent(String.valueOf(map.get(JwtTokenUtil.ACCESS_TOKEN)))
                    .build();
            loginLog.setId(Long.valueOf(id));
            loginLog.setUpdateTime(new Date());
            log.info(JSONUtil.toJsonStr(loginLog));
            log.info(id);
            MsgPojo<Object> build = MsgPojo.builder().id(securityUser.getId()).msg(
                    loginLog
            ).build();
            sendQueue.doSendLoginQueue(build);
        }
        return response.writeWith(Mono.just(response.bufferFactory().wrap(JSON.toJSONBytes(ResponseImpl.builder().message("登录成功").data(map).build().success()))));
    }
}
