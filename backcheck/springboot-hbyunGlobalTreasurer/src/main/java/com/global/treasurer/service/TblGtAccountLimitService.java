package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountLimit;

import java.math.BigDecimal;

/**
 * 全球司库-账户限额Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountLimitService extends IService<TblGtAccountLimit> {

    /**
     * 分页查询账户限额列表
     *
     * @param page 分页对象
     * @param accountNumber 账户号码
     * @param limitType 限额类型
     * @param limitStatus 限额状态
     * @return 分页结果
     */
    IPage<TblGtAccountLimit> getPageList(Page<TblGtAccountLimit> page,
                                         String accountNumber,
                                         String limitType,
                                         String limitStatus);

    /**
     * 新增账户限额
     *
     * @param entity 账户限额实体
     * @return 是否成功
     */
    boolean saveAccountLimit(TblGtAccountLimit entity);

    /**
     * 更新账户限额
     *
     * @param entity 账户限额实体
     * @return 是否成功
     */
    boolean updateAccountLimit(TblGtAccountLimit entity);

    /**
     * 删除账户限额
     *
     * @param limitId 限额ID
     * @return 是否成功
     */
    boolean deleteAccountLimit(BigDecimal limitId);

    /**
     * 更新限额状态
     *
     * @param limitId 限额ID
     * @param limitStatus 限额状态
     * @return 是否成功
     */
    boolean updateLimitStatus(BigDecimal limitId, String limitStatus);

    /**
     * 审批账户限额
     *
     * @param limitId 限额ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param limitStatus 限额状态
     * @return 是否成功
     */
    boolean approveAccountLimit(BigDecimal limitId, BigDecimal approverId,
                                 String approvalOpinion, String limitStatus);

    /**
     * 暂停账户限额
     *
     * @param limitId 限额ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean suspendAccountLimit(BigDecimal limitId, BigDecimal operatorId);

    /**
     * 激活账户限额
     *
     * @param limitId 限额ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean activateAccountLimit(BigDecimal limitId, BigDecimal operatorId);
}
