package com.huabo.contract.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.contract.entity.DepositManagement;
import com.huabo.contract.vo.DepositManagementQueryParam;

import java.math.BigDecimal;
import java.util.List;

/**
 * 保证金管理服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface DepositManagementService extends IService<DepositManagement> {

    /**
     * 分页查询保证金管理列表
     * 
     * @param queryParam 查询参数
     * @return 分页结果
     */
    IPage<DepositManagement> getDepositManagementPage(DepositManagementQueryParam queryParam);

    /**
     * 根据招投标项目ID查询保证金列表
     * 
     * @param biddingProjectId 招投标项目ID
     * @return 保证金列表
     */
    List<DepositManagement> getByBiddingProjectId(Long biddingProjectId);

    /**
     * 根据保证金状态查询保证金列表
     * 
     * @param depositStatus 保证金状态
     * @return 保证金列表
     */
    List<DepositManagement> getByDepositStatus(Integer depositStatus);

    /**
     * 查询即将到期的保证金列表（7天内）
     * 
     * @return 即将到期的保证金列表
     */
    List<DepositManagement> getExpiringSoon();

    /**
     * 查询已逾期的保证金列表
     * 
     * @return 已逾期的保证金列表
     */
    List<DepositManagement> getOverdue();

    /**
     * 根据保证金类型统计保证金总额
     * 
     * @param depositType 保证金类型
     * @return 保证金总额
     */
    BigDecimal sumDepositAmountByType(Integer depositType);

    /**
     * 根据招投标项目ID统计保证金总额
     * 
     * @param biddingProjectId 招投标项目ID
     * @return 保证金总额
     */
    BigDecimal sumDepositAmountByProject(Long biddingProjectId);

    /**
     * 根据保证金状态统计数量
     * 
     * @param depositStatus 保证金状态
     * @return 数量
     */
    Integer countByDepositStatus(Integer depositStatus);

    /**
     * 查询负责人的保证金列表
     * 
     * @param managerId 负责人ID
     * @return 保证金列表
     */
    List<DepositManagement> getByManagerId(Long managerId);

    /**
     * 根据银行名称查询保证金列表
     * 
     * @param bankName 银行名称
     * @return 保证金列表
     */
    List<DepositManagement> getByBankName(String bankName);

    /**
     * 根据保函编号查询保证金
     * 
     * @param guaranteeNo 保函编号
     * @return 保证金信息
     */
    DepositManagement getByGuaranteeNo(String guaranteeNo);

    /**
     * 批量更新保证金状态
     * 
     * @param ids 保证金ID列表
     * @param depositStatus 新状态
     * @param updateBy 更新人
     * @return 更新数量
     */
    Integer batchUpdateStatus(List<Long> ids, Integer depositStatus, Long updateBy);

    /**
     * 保证金退还
     * 
     * @param id 保证金ID
     * @param refundAmount 退还金额
     * @param refundReason 退还原因
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean refundDeposit(Long id, BigDecimal refundAmount, String refundReason, Long updateBy);

    /**
     * 保证金没收
     * 
     * @param id 保证金ID
     * @param confiscationReason 没收原因
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean confiscateDeposit(Long id, String confiscationReason, Long updateBy);

    /**
     * 保证金转履约
     * 
     * @param id 保证金ID
     * @param updateBy 更新人
     * @return 是否成功
     */
    Boolean convertToPerformance(Long id, Long updateBy);

    /**
     * 查询保证金统计信息
     * 
     * @return 统计信息
     */
    List<DepositManagement> getDepositStatistics();

    /**
     * 保证金到期提醒
     * 
     * @return 提醒信息
     */
    List<DepositManagement> getExpiryReminders();

    /**
     * 验证保证金信息
     * 
     * @param depositManagement 保证金信息
     * @return 验证结果
     */
    Boolean validateDepositInfo(DepositManagement depositManagement);
}
