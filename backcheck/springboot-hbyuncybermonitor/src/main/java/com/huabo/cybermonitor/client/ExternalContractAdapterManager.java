package com.huabo.cybermonitor.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 外部合同适配器管理器
 * 统一管理多个外部合同系统的数据接入
 */
@Component
public class ExternalContractAdapterManager {

    private static final Logger log = LoggerFactory.getLogger(ExternalContractAdapterManager.class);

    @Autowired(required = false)
    private List<ExternalContractAdapter> adapters;

    /**
     * 根据适配器编码获取对应适配器
     *
     * @param adapterCode 适配器编码
     * @return 对应的适配器实例
     */
    public ExternalContractAdapter getAdapter(String adapterCode) {
        if (adapters == null || adapters.isEmpty()) {
            log.warn("未注册任何外部合同适配器");
            return null;
        }
        return adapters.stream()
                .filter(a -> a.getAdapterCode().equals(adapterCode))
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取所有已注册的适配器编码
     *
     * @return 适配器编码列表
     */
    public List<String> getRegisteredAdapters() {
        if (adapters == null || adapters.isEmpty()) {
            return Collections.emptyList();
        }
        return adapters.stream()
                .map(ExternalContractAdapter::getAdapterCode)
                .collect(Collectors.toList());
    }

    /**
     * 检查指定适配器是否可用
     *
     * @param adapterCode 适配器编码
     * @return true=可用
     */
    public boolean isAdapterAvailable(String adapterCode) {
        ExternalContractAdapter adapter = getAdapter(adapterCode);
        if (adapter == null) {
            return false;
        }
        try {
            return adapter.testConnection();
        } catch (Exception e) {
            log.error("测试外部合同适配器连接失败, adapterCode={}", adapterCode, e);
            return false;
        }
    }
}

