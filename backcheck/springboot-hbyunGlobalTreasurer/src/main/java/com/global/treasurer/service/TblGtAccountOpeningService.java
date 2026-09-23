package com.global.treasurer.service;
import java.math.BigDecimal;

import java.util.Map;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountOpening;

/**
 * 全球司库-开户申请Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountOpeningService extends IService<TblGtAccountOpening> {

    /**
     * 分页查询开户申请列表
     *
     * @param page 分页对象
     * @param accountName 账户名称
     * @param applicationStatus 申请状态
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TblGtAccountOpening> getPageList(Page<TblGtAccountOpening> page,
                                           String applicationNo,
                                           String accountName,
                                           String applicationStatus,
                                           String bankCode,
                                           BigDecimal orgId);

    /**
     * 新增开户申请
     *
     * @param entity 开户申请实体
     * @return 是否成功
     */
    boolean saveAccountOpening(TblGtAccountOpening entity);

    /**
     * 更新开户申请
     *
     * @param entity 开户申请实体
     * @return 是否成功
     */
    boolean updateAccountOpening(TblGtAccountOpening entity);

    /**
     * 删除开户申请
     *
     * @param applicationId 申请ID
     * @return 是否成功
     */
    boolean deleteAccountOpening(BigDecimal applicationId);

    /**
     * 审批开户申请
     *
     * @param applicationId 申请ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param applicationStatus 审批状态
     * @return 是否成功
     */
    boolean approveAccountOpening(BigDecimal applicationId, BigDecimal approverId,
                                   String approvalOpinion, String applicationStatus);

    /**
     * 取消开户申请
     *
     * @param applicationId 申请ID
     * @param updateUser 操作人ID
     * @return 是否成功
     */
    boolean cancelAccountOpening(BigDecimal applicationId, BigDecimal updateUser);

    /**
     * 按状态统计开户申请数量
     *
     * @param orgId 机构ID
     * @return 统计数据
     */
    Map<String, Object> getStatusStatistics(BigDecimal orgId);
}
