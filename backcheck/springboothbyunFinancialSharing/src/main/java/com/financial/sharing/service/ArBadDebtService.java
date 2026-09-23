package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 坏账管理服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArBadDebtService {

    // ==================== 坏账准备 ====================

    /**
     * 分页查询坏账准备列表
     */
    MyJsonBean<PageResult> getProvisionList(ArBadDebtProvisionQueryParam param);

    /**
     * 根据ID查询坏账准备详情
     */
    MyJsonBean getProvisionById(String provisionId);

    /**
     * 计提坏账准备
     */
    MyJsonBean createProvision(ArBadDebtProvisionSaveParam param);

    /**
     * 批量计提坏账准备
     */
    MyJsonBean batchCreateProvision(List<ArBadDebtProvisionSaveParam> params);

    /**
     * 冲回坏账准备
     */
    MyJsonBean reverseProvision(String provisionId, String operatorId);

    /**
     * 查询坏账准备余额
     */
    MyJsonBean getProvisionBalance(Long tenantId, LocalDate asOfDate);

    /**
     * 查询坏账准备统计
     */
    MyJsonBean getProvisionStatistics(ArBadDebtProvisionQueryParam param);

    // ==================== 坏账核销 ====================

    /**
     * 分页查询坏账核销列表
     */
    MyJsonBean<PageResult> getBadDebtWriteOffList(ArBadDebtWriteOffQueryParam param);

    /**
     * 根据ID查询坏账核销详情
     */
    MyJsonBean getBadDebtWriteOffById(String writeOffId);

    /**
     * 申请坏账核销
     */
    MyJsonBean applyBadDebtWriteOff(ArBadDebtWriteOffSaveParam param);

    /**
     * 审核坏账核销
     */
    MyJsonBean auditBadDebtWriteOff(String writeOffId, Integer status, String auditorId, String auditComments);

    /**
     * 查询坏账核销统计
     */
    MyJsonBean getBadDebtWriteOffStatistics(ArBadDebtWriteOffQueryParam param);

    // ==================== 坏账回收 ====================

    /**
     * 分页查询坏账回收列表
     */
    MyJsonBean<PageResult> getRecoveryList(ArBadDebtRecoveryQueryParam param);

    /**
     * 根据ID查询坏账回收详情
     */
    MyJsonBean getRecoveryById(String recoveryId);

    /**
     * 登记坏账回收
     */
    MyJsonBean createRecovery(ArBadDebtRecoverySaveParam param);

    /**
     * 查询坏账回收统计
     */
    MyJsonBean getRecoveryStatistics(ArBadDebtRecoveryQueryParam param);

    // ==================== 导出 ====================

    /**
     * 导出坏账准备
     */
    MyJsonBean exportProvisions(ArBadDebtProvisionQueryParam param);

    /**
     * 导出坏账核销
     */
    MyJsonBean exportBadDebtWriteOffs(ArBadDebtWriteOffQueryParam param);

    /**
     * 导出坏账回收
     */
    MyJsonBean exportRecoveries(ArBadDebtRecoveryQueryParam param);
}

