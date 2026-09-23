package com.huabo.cybermonitor.client;

import com.huabo.cybermonitor.entity.TblContractRecord;

import java.util.List;
import java.util.Map;

/**
 * 外部合同数据适配器接口
 * 不同外部合同系统实现此接口，统一数据格式
 * 适配器将外部数据转换为穿透式监管统一的 TblContractRecord 格式
 */
public interface ExternalContractAdapter {

    /**
     * 适配器标识（如 "ERP_CONTRACT", "OA_CONTRACT"）
     */
    String getAdapterCode();

    /**
     * 同步外部合同数据到穿透式监管系统
     *
     * @param companyId 企业ID
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     * @return 转换后的合同记录列表
     */
    List<TblContractRecord> syncContracts(String companyId, String startDate, String endDate);

    /**
     * 查询外部合同详情
     *
     * @param externalContractId 外部合同ID
     * @return 转换后的合同记录
     */
    TblContractRecord getContractDetail(String externalContractId);

    /**
     * 查询外部合同履行状态
     *
     * @param externalContractId 外部合同ID
     * @return 履行状态信息
     */
    Map<String, Object> getPerformanceStatus(String externalContractId);

    /**
     * 测试连接可用性
     *
     * @return true=连接正常
     */
    boolean testConnection();
}

