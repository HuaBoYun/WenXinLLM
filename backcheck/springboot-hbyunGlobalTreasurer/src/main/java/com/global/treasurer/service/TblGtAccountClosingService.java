package com.global.treasurer.service;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountClosing;

/**
 * 全球司库-销户申请Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountClosingService extends IService<TblGtAccountClosing> {

    /**
     * 分页查询销户申请列表
     *
     * @param page 分页对象
     * @param applicationNo 申请编号
     * @param accountNumber 账户号码
     * @param applicationStatus 申请状态
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TblGtAccountClosing> getPageList(Page<TblGtAccountClosing> page,
                                           String applicationNo,
                                           String accountNumber,
                                           String applicationStatus,
                                           BigDecimal orgId);

    /**
     * 新增销户申请
     *
     * @param entity 销户申请实体
     * @return 是否成功
     */
    boolean saveAccountClosing(TblGtAccountClosing entity);

    /**
     * 更新销户申请
     *
     * @param entity 销户申请实体
     * @return 是否成功
     */
    boolean updateAccountClosing(TblGtAccountClosing entity);

    /**
     * 删除销户申请
     *
     * @param applicationId 申请ID
     * @return 是否成功
     */
    boolean deleteAccountClosing(BigDecimal applicationId);

    /**
     * 审批销户申请
     *
     * @param applicationId 申请ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param applicationStatus 审批状态
     * @return 是否成功
     */
    boolean approveAccountClosing(BigDecimal applicationId, BigDecimal approverId,
                                   String approvalOpinion, String applicationStatus);

    /**
     * 批量审批销户申请
     *
     * @param applicationIds 申请ID列表
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param applicationStatus 审批状态(APPROVED/REJECTED)
     * @return 成功数量
     */
    int batchApproveAccountClosing(List<BigDecimal> applicationIds, BigDecimal approverId,
                                    String approvalOpinion, String applicationStatus);
}
