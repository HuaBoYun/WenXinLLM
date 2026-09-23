package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditLimitDTO;
import com.global.treasurer.dto.CreditLimitQueryDTO;
import com.global.treasurer.entity.TblCreditLimit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 授信额度服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CreditLimitService {

    /**
     * 分页查询授信额度列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblCreditLimit> getLimitList(CreditLimitQueryDTO queryDTO);

    /**
     * 根据ID查询授信额度详情
     *
     * @param limitId 额度ID
     * @return 授信额度
     */
    TblCreditLimit getLimitById(Long limitId);

    /**
     * 根据合同ID查询额度列表
     *
     * @param contractId 合同ID
     * @return 额度列表
     */
    List<TblCreditLimit> getLimitsByContractId(Long contractId);

    /**
     * 保存授信额度（新增或更新）
     *
     * @param dto 授信额度DTO
     * @return 保存后的授信额度
     */
    TblCreditLimit saveLimit(CreditLimitDTO dto);

    /**
     * 删除授信额度
     *
     * @param limitId 额度ID
     */
    void deleteLimit(Long limitId);

    /**
     * 批量删除授信额度
     *
     * @param limitIds 额度ID列表
     */
    void batchDeleteLimits(List<Long> limitIds);

    /**
     * 使用额度
     *
     * @param limitId 额度ID
     * @param amount 使用金额
     */
    void useLimit(Long limitId, BigDecimal amount);

    /**
     * 释放额度
     *
     * @param limitId 额度ID
     * @param amount 释放金额
     */
    void releaseLimit(Long limitId, BigDecimal amount);

    /**
     * 冻结额度
     *
     * @param limitId 额度ID
     * @param amount 冻结金额
     */
    void freezeLimit(Long limitId, BigDecimal amount);

    /**
     * 解冻额度
     *
     * @param limitId 额度ID
     * @param amount 解冻金额
     */
    void unfreezeLimit(Long limitId, BigDecimal amount);

    /**
     * 查询授信额度汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> getLimitSummary(Long companyId);
}

