package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArWriteOffQueryParam;
import com.financial.sharing.vo.param.ArWriteOffSaveParam;

import java.util.List;

/**
 * 核销服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArWriteOffService {

    /**
     * 分页查询核销记录列表
     */
    MyJsonBean<PageResult> getWriteOffList(ArWriteOffQueryParam param);

    /**
     * 根据ID查询核销记录详情
     */
    MyJsonBean getWriteOffById(String writeOffId);

    /**
     * 执行核销
     */
    MyJsonBean executeWriteOff(ArWriteOffSaveParam param);

    /**
     * 自动核销
     */
    MyJsonBean autoWriteOff(String customerId, Long tenantId);

    /**
     * 反核销
     */
    MyJsonBean reverseWriteOff(String receiptId, String operatorId);

    /**
     * 根据收款单ID查询核销记录
     */
    MyJsonBean getWriteOffsByReceiptId(String receiptId);

    /**
     * 根据应收单ID查询核销记录
     */
    MyJsonBean getWriteOffsByReceivableId(String receivableId);

    /**
     * 查询核销统计
     */
    MyJsonBean getWriteOffStatistics(ArWriteOffQueryParam param);

    /**
     * 导出核销记录
     */
    MyJsonBean exportWriteOffs(ArWriteOffQueryParam param);
}

