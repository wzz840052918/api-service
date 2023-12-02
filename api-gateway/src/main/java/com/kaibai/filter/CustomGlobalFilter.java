package com.kaibai.filter;

import cn.hutool.core.util.StrUtil;
import com.kaibai.entity.InterfaceInfo;
import com.kaibai.entity.User;
import com.kaibai.service.InnerInterfaceInfoService;
import com.kaibai.service.InnerInterfaceInfoUserService;
import com.kaibai.service.InnerUserService;
import com.kaibai.utils.SignUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    private static final List<String> IP_WHITE_LIST = List.of("127.0.0.1");

    private static final String ACCESS_KEY = "accessKey";
    private static final String BODY = "body";
    private static final String NONCE = "NONCE";
    private static final String TIMESTAMP = "timestamp";
    private static final String SIGN = "sign";

    private static final long FIVE_MINUTES = 60 * 5L;

    private static final String HOST = "http://localhost:8090";

    @DubboReference
    private InnerInterfaceInfoService interfaceInfoService;

    @DubboReference
    private InnerUserService userService;

    @DubboReference
    private InnerInterfaceInfoUserService interfaceInfoUserService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 记录请求日志
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethod().toString();
        String path = request.getPath().value();
        log.info("请求唯一标识：{}, 请求路径：{}, 请求方法：{}, 请求参数：{}, 请求来源地址：{}", request.getId(), path, method, request.getQueryParams(), request.getRemoteAddress().getHostString());
        // 2. 访问控制-黑白名单处理
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(exchange.getRequest().getLocalAddress().getHostString())) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return response.setComplete();
        }
        // 3. 用户鉴权
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst(ACCESS_KEY);
        String nonce = headers.getFirst(NONCE);
        String timestamp = headers.getFirst(TIMESTAMP);
        String sign = headers.getFirst(SIGN);
        String body = headers.getFirst(BODY);
        if (Objects.isNull(accessKey) || Objects.isNull(nonce) || Objects.isNull(timestamp) || Objects.isNull(sign)) {
            return handleNoAuth(response);
        }
        // 3.1 验证accessKey的正确性
        User invokeUser = userService.getInvokeUser(accessKey);
        // 3.2 验证timestamp是否超期
        long currentTimeMillis = System.currentTimeMillis() / 1000;
        if (currentTimeMillis - Long.parseLong(timestamp) > FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 3.3 验证nonce是否正常
        if (Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }

        // 3.4 验证签名的正确性
        String checkSign = SignUtil.generateSign(invokeUser.getSecretKey());
        if (!StrUtil.equals(sign, checkSign)) {
            return handleNoAuth(response);
        }
        // 4. 接口是否存在？
        InterfaceInfo interfaceInfo = null;
        int remaining = 0;
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo(HOST + path, method);
            // 判断当前用户当前是否还有剩余调用次数
            remaining = interfaceInfoUserService.remaining(interfaceInfo.getId(), invokeUser.getId());
        } catch (Exception e) {
            log.error("获取接口信息失败，", e);
        }
        if (remaining <= 0) {
            return handleNoAuth(response);
        }
        // 5. 记录响应日志
        chain.filter(exchange);
        return handleResponse(exchange, chain, interfaceInfo.getId(), invokeUser.getId());
    }

    /**
     * 使用Spring提供的HttpResponse装饰器进行增强
     *
     * @param exchange
     * @param chain
     * @param interfaceId
     * @param invokeUserId
     * @return
     */
    private Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain, Long interfaceId, Long invokeUserId) {
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();

            HttpStatus statusCode = originalResponse.getStatusCode();

            if (statusCode == HttpStatus.OK) {
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7. 调用成功，接口调用次数 + 1
                                try {
                                    interfaceInfoUserService.invokeCount(interfaceId, invokeUserId);
                                } catch (Exception e) {
                                    log.error("调用失败，错误信息:", e);
                                }
                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);
                                String data = new String(content, StandardCharsets.UTF_8);
                                log.info("记录响应结果: {}", data);
                                return bufferFactory.wrap(content);
                            }));
                        } else {
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange);
        } catch (Exception e) {
            log.error("网关增强处理相应异常.\n" + e);
            return chain.filter(exchange);
        }
    }

    @Override
    public int getOrder() {
        // 当此过滤器的优先级小于 NettyWriteResponseFilter的优先级时，会导致请求没有最终返回
        // 但是按照官网文档来说  It runs after all other filters have completed and writes the proxy response back to the gateway client response
        // DEBUG时也确定 NettyWriteResponseFilter 是最后执行的，但就是没有返回
        return -2;
    }

    /**
     * 无权限或者物调用次数处理
     * @param response
     * @return
     */
    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}