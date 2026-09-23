package com.financial.sharing.oracle.service;

import com.financial.sharing.dto.BalanceAdjustmentParam;
import com.financial.sharing.dto.BalanceQueryParam;
import com.financial.sharing.util.PageResult;
import com.hbfk.entity.TblStaffUtil;
import java.util.List;
import java.util.Map;

/**
 * 余额调整服务接口
 *
 * @author system
 * @since 2024-12-07
 */
public interface BalanceAdjustmentService {

    /**
     * 创建余额调整申请
     *
     * @param param 调整参数
     * @param loginStaff 登录用户
     * @return 调整申请ID
     */
    Long createAdjustment(BalanceAdjustmentParam param, TblStaffUtil loginStaff);

    /**
     * 分页查询余额调整申请列表
     *
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<Map<String, Object>> selectAdjustmentPage(BalanceQueryParam queryParam);

    /**
     * 根据ID查询余额调整详情
     *
     * @param adjustmentId 调整ID
     * @return 调整详情
     */
    Map<String, Object> selectAdjustmentById(Long adjustmentId);

    /**
     * 审批余额调整
     *
     * @param adjustmentId 调整ID
     * @param action 审批动作：APPROVE-通过, REJECT-拒绝
     * @param comment 审批意见
     * @param loginStaff 登录用户
     * @return 审批结果
     */
    boolean approveAdjustment(Long adjustmentId, String action, String comment, TblStaffUtil loginStaff);

    /**
     * 批量审批余额调整
     *
     * @param adjustmentIds 调整ID列表
     * @param action 审批动作
     * @param comment 审批意见
     * @param loginStaff 登录用户
     * @return 审批结果
     */
    boolean batchApproveAdjustment(List<Long> adjustmentIds, String action, String comment, TblStaffUtil loginStaff);

    /**
     * 撤销余额调整申请
     *
     * @param adjustmentId 调整ID
     * @param reason 撤销原因
     * @param loginStaff 登录用户
     * @return 撤销结果
     */
    boolean cancelAdjustment(Long adjustmentId, String reason, TblStaffUtil loginStaff);

    /**
     * 获取余额调整类型列表
     *
     * @return 调整类型列表
     */
    List<Map<String, Object>> getAdjustmentTypes();

    /**
     * 获取待审批的调整申请数量
     *
     * @param loginStaff 登录用户
     * @return 待审批数量
     */
    Integer getPendingApprovalCount(TblStaffUtil loginStaff);

    /**
     * 获取我的调整申请列表
     *
     * @param loginStaff 登录用户
     * @param queryParam 查询参数
     * @return 我的申请列表
     */
    PageResult<Map<String, Object>> getMyAdjustments(TblStaffUtil loginStaff, BalanceQueryParam queryParam);

    /**
     * 执行余额调整
     *
     * @param adjustmentId 调整ID
     * @param loginStaff 登录用户
     * @return 执行结果
     */
    boolean executeAdjustment(Long adjustmentId, TblStaffUtil loginStaff);

    /**
     * 回滚余额调整
     *
     * @param adjustmentId 调整ID
     * @param reason 回滚原因
     * @param loginStaff 登录用户
     * @return 回滚结果
     */
    boolean rollbackAdjustment(Long adjustmentId, String reason, TblStaffUtil loginStaff);

    /**
     * 获取调整影响分析
     *
     * @param param 调整参数
     * @return 影响分析
     */
    Map<String, Object> getAdjustmentImpact(BalanceAdjustmentParam param);

    /**
     * 检查是否可以调整
     *
     * @param param 调整参数
     * @return 检查结果
     */
    Map<String, Object> checkAdjustmentEligibility(BalanceAdjustmentParam param);

    /**
     * 获取调整历史记录
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @return 历史记录
     */
    List<Map<String, Object>> getAdjustmentHistory(Long bookId, String accountCode, String period);
}