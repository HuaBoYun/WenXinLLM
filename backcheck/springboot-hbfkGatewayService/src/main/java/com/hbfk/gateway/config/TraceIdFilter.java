package com.hbfk.gateway.config;

import cn.hutool.core.util.IdUtil;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Description: 全局过滤器，用于生成或获取链路 ID，并将其添加到请求头和上下文中
 * @Author: 61
 */
@Component
public class TraceIdFilter implements GlobalFilter, Ordered {

    private static final String TRACE_ID_HEADER = "X-Trace-Id";

    /**
     * 链路追踪过滤逻辑
     * <p>为每个请求生成唯一的UUID作为链路追踪ID，并添加到请求头 X-Trace-Id 中</p>
     *
     * @param exchange 请求交换对象
     * @param chain    过滤器链
     * @return Mono<Void> 过滤结果
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, org.springframework.cloud.gateway.filter.GatewayFilterChain chain) {
        // 生成链路 ID
        String traceId = IdUtil.fastUUID();
        // 将链路 ID 添加到请求头
        exchange.getRequest()
                .mutate()
                .header(TRACE_ID_HEADER, traceId)
                .build();
        return chain.filter(exchange);
    }

    /**
     * 获取过滤器执行顺序
     * <p>设置为最高优先级，确保链路ID在所有过滤器之前生成</p>
     *
     * @return 过滤器优先级序号（最高优先级）
     */
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
