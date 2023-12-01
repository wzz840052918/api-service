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
        try {
            interfaceInfo = interfaceInfoService.getInterfaceInfo("http://localhost:7529/api/interfaceInfo/name", method);
        } catch (Exception e) {
            log.error("获取接口信息失败，", e);
        }
        // 5. 请求转发
        chain.filter(exchange);
        // 6. 记录响应日志
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
                        //log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            return super.writeWith(fluxBody.map(dataBuffer -> {
                                // 7. 调用成功，接口调用次数 + 1
                                interfaceInfoUserService.invokeCount(interfaceId, invokeUserId);
                                // 8. 调用失败，返回一个错误码
                                originalResponse.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                                originalResponse.setComplete();

                                byte[] content = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(content);
                                DataBufferUtils.release(dataBuffer);//释放掉内存
                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode());
                                String data = new String(content, StandardCharsets.UTF_8);//data
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
        return -1;
    }

    private Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }
}