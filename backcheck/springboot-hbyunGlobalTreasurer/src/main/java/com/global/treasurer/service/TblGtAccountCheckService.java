package com.global.treasurer.service;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtAccountCheck;

/**
 * 全球司库-账户检查Service接口
 *
 * @author AI Developer
 * @since 2026-01-16
 */
public interface TblGtAccountCheckService extends IService<TblGtAccountCheck> {

    /**
     * 分页查询账户检查列表
     *
     * @param page 分页对象
     * @param accountNumber 账户号码
     * @param checkType 检查类型
     * @param checkStatus 检查状态
     * @param checkId 检查ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param orgId 机构ID
     * @return 分页结果
     */
    IPage<TblGtAccountCheck> getPageList(Page<TblGtAccountCheck> page,
                                        String accountNumber,
                                        String checkType,
                                        String checkStatus,
                                        Long checkId,
                                        String startDate,
                                        String endDate,
                                        BigDecimal orgId);

    /**
     * 新增账户检查
     *
     * @param entity 账户检查实体
     * @return 是否成功
     */
    boolean saveAccountCheck(TblGtAccountCheck entity);

    /**
     * 更新账户检查
     *
     * @param entity 账户检查实体
     * @return 是否成功
     */
    boolean updateAccountCheck(TblGtAccountCheck entity);

    /**
     * 删除账户检查
     *
     * @param checkId 检查ID
     * @return 是否成功
     */
    boolean deleteAccountCheck(BigDecimal checkId);

    /**
     * 重新检查（清空检查结果，状态重置为PENDING）
     *
     * @param checkId    检查ID
     * @param updateUser 操作人ID
     * @return 是否成功
     */
    boolean recheckAccountCheck(Long checkId, BigDecimal updateUser);

    /**
     * 审批账户检查
     *
     * @param checkId 检查ID
     * @param approverId 审批人ID
     * @param approvalOpinion 审批意见
     * @param checkStatus 审批状态
     * @return 是否成功
     */
    boolean approveAccountCheck(BigDecimal checkId, BigDecimal approverId,
                                 String approvalOpinion, String checkStatus);
}
