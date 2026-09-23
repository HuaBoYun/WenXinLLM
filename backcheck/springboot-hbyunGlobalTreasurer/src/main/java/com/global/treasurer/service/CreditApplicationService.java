package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.CreditApplicationDTO;
import com.global.treasurer.dto.CreditApplicationQueryDTO;
import com.global.treasurer.entity.TblCreditApplication;

import java.util.List;
import java.util.Map;

/**
 * 授信申请服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface CreditApplicationService {

    /**
     * 分页查询授信申请列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblCreditApplication> getApplicationList(CreditApplicationQueryDTO queryDTO);

    /**
     * 根据ID查询授信申请详情
     *
     * @param applicationId 申请ID
     * @return 授信申请
     */
    TblCreditApplication getApplicationById(Long applicationId);

    /**
     * 保存授信申请（新增或更新）
     *
     * @param dto 授信申请DTO
     * @return 保存后的授信申请
     */
    TblCreditApplication saveApplication(CreditApplicationDTO dto);

    /**
     * 删除授信申请
     *
     * @param applicationId 申请ID
     */
    void deleteApplication(Long applicationId);

    /**
     * 批量删除授信申请
     *
     * @param applicationIds 申请ID列表
     */
    void batchDeleteApplications(List<Long> applicationIds);

    /**
     * 提交审批
     *
     * @param applicationId 申请ID
     */
    void submitForApproval(Long applicationId);

    /**
     * 审批通过
     *
     * @param applicationId 申请ID
     * @param approvedAmount 批准金额
     * @param comments 审批意见
     */
    void approve(Long applicationId, java.math.BigDecimal approvedAmount, String comments);

    /**
     * 审批拒绝
     *
     * @param applicationId 申请ID
     * @param rejectReason 拒绝原因
     */
    void reject(Long applicationId, String rejectReason);

    /**
     * 查询授信申请汇总
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> getApplicationSummary(Long companyId);
}

