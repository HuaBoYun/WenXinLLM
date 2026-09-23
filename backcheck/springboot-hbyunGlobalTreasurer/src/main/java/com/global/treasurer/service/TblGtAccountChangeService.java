package com.global.treasurer.service;
import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountChange;

/**
 * 全球司库-账户变更Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountChangeService extends IService<TblGtAccountChange> {

    /**
     * 分页查询账户变更列表
     *
     * @param page 分页对象
     * @param accountName 账户名称
     * @param changeType 变更类型
     * @param applicationStatus 申请状态
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TblGtAccountChange> getPageList(Page<TblGtAccountChange> page,
                                          String accountName,
                                          String changeType,
                                          String applicationStatus,
                                          BigDecimal orgId);

    /**
     * 新增账户变更
     *
     * @param entity 账户变更实体
     * @return 是否成功
     */
    boolean saveAccountChange(TblGtAccountChange entity);

    /**
     * 更新账户变更
     *
     * @param entity 账户变更实体
     * @return 是否成功
     */
    boolean updateAccountChange(TblGtAccountChange entity);

    /**
     * 删除账户变更
     *
     * @param applicationId 申请ID
     * @return 是否成功
     */
    boolean deleteAccountChange(BigDecimal applicationId);

    /**
     * 审批账户变更
     *
     * @param applicationId 申请ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param applicationStatus 申请状态
     * @return 是否成功
     */
    boolean approveAccountChange(BigDecimal applicationId, BigDecimal approverId,
                                  String approvalOpinion, String applicationStatus);

    /**
     * 批量删除账户变更
     *
     * @param applicationIds 申请ID列表
     * @return 是否成功
     */
    boolean batchDelete(List<BigDecimal> applicationIds);
}
