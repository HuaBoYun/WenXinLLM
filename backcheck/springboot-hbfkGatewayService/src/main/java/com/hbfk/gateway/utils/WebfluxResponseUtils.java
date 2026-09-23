package com.hbfk.gateway.utils;

import cn.hutool.json.JSONUtil;
import org.apache.http.HttpHeaders;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.Charset;

/**
 * 处理WebFlux响应
 * @author GitEgg
 */
public class WebfluxResponseUtils {
    
    /**
     * 向客户端写入错误响应
     * <p>设置HTTP状态码为200，Content-Type为JSON，并返回系统错误信息</p>
     *
     * @param exchange ServerWebExchange请求交换对象
     * @param message  错误提示信息
     * @return Mono<Void> 响应完成信号
     */
    public static Mono<Void> responseWrite(ServerWebExchange exchange, String message) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        response.getHeaders().set("Access-Control-Allow-Origin", "*");
        response.getHeaders().set("Cache-Control", "no-cache");
        String body= JSONUtil.toJsonStr("系统错误");
        DataBuffer buffer =  response.bufferFactory().wrap(body.getBytes(Charset.forName("UTF-8")));
        return response.writeWith(Mono.just(buffer)).doFinally(s -> {
            DataBufferUtils.release(buffer);
        });
    }
}
