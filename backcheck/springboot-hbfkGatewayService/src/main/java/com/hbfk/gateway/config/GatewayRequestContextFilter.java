package com.hbfk.gateway.config;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;

import java.net.URI;
import java.util.List;

/**
 * 网关请求上下文过滤器
 * <p>根据日志配置属性判断是否需要读取和缓存请求数据</p>
 * <p>支持按服务ID、请求路径、日志类型等维度进行过滤判断</p>
 *
 * @author hbyun
 */
@Slf4j
@AllArgsConstructor
public class GatewayRequestContextFilter {
    private GatewayPluginProperties gatewayPluginProperties;

    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();
    /**
     * check should read request data whether or not
     * @return boolean
     */
    private boolean shouldReadRequestData(ServerWebExchange exchange) {
        // 检查是否需要记录所有请求数据
        if (gatewayPluginProperties.getLogRequest().getRequestLog() &&
                GatewayLogTypeEnum.ALL.getType().equals(gatewayPluginProperties.getLogRequest().getLogType())) {
            log.debug("[GatewayContext]Properties Set Read All Request Data");
            return true;
        }

        boolean serviceFlag = false;
        boolean pathFlag = false;
        boolean lbFlag = false;

        // 获取需要记录日志的服务ID列表
        List<String> readRequestDataServiceIdList = gatewayPluginProperties.getLogRequest().getServiceIdList();
        // 获取需要记录日志的路径列表
        List<String> readRequestDataPathList = gatewayPluginProperties.getLogRequest().getPathList();

        // 检查路径列表中的配置
        if (!CollectionUtils.isEmpty(readRequestDataPathList) &&
                (GatewayLogTypeEnum.PATH.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()) ||
                        GatewayLogTypeEnum.CONFIGURE.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()))) {
            String requestPath = exchange.getRequest().getPath().pathWithinApplication().value();
            for (String path : readRequestDataPathList) {
                if (ANT_PATH_MATCHER.match(path, requestPath)) {
                    log.debug("[GatewayContext]Properties Set Read Specific Request Data With Request Path:{}, Math Pattern:{}", requestPath, path);
                    pathFlag = true;
                    break;
                }
            }
        }

        // 获取路由信息
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        URI routeUri = route.getUri();
        // 检查是否为负载均衡请求
        if (!"lb".equalsIgnoreCase(routeUri.getScheme())) {
            lbFlag = true;
        }

        String routeServiceId = routeUri.getHost().toLowerCase();
        // 检查服务ID列表中的配置
        if (!CollectionUtils.isEmpty(readRequestDataServiceIdList) &&
                (GatewayLogTypeEnum.SERVICE.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()) ||
                        GatewayLogTypeEnum.CONFIGURE.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()))) {
            if (readRequestDataServiceIdList.contains(routeServiceId)) {
                log.debug("[GatewayContext]Properties Set Read Specific Request Data With ServiceId:{}", routeServiceId);
                serviceFlag = true;
            }
        }

        // 根据日志类型和标志位决定是否读取请求数据
        if (GatewayLogTypeEnum.CONFIGURE.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()) &&
                serviceFlag && pathFlag && !lbFlag) {
            return true;
        } else if (GatewayLogTypeEnum.SERVICE.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()) &&
                serviceFlag && !lbFlag) {
            return true;
        } else if (GatewayLogTypeEnum.PATH.getType().equals(gatewayPluginProperties.getLogRequest().getLogType()) &&
                pathFlag) {
            return true;
        }

        // 如果以上条件都不满足，则不读取请求数据
        return false;
    }

}
