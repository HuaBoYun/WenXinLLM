package com.financial.sharing.oracle.mapper;

import com.financial.sharing.dto.BalanceQueryParam;
import com.financial.sharing.dto.BalanceRefreshParam;
import com.financial.sharing.dto.BalanceAdjustmentParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 余额管理 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-07
 */
public interface BalanceMapper {

    /**
     * 分页查询科目余额
     *
     * @param param 查询参数
     * @return 余额列表
     */
    List<Map<String, Object>> selectBalancePage(@Param("param") BalanceQueryParam param);

    /**
     * 查询科目余额总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectBalanceCount(@Param("param") BalanceQueryParam param);

    /**
     * 根据条件查询科目余额
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @param currencyCode 币种代码
     * @return 余额信息
     */
    Map<String, Object> selectBalanceByCondition(@Param("bookId") Long bookId,
                                                @Param("accountCode") String accountCode,
                                                @Param("period") String period,
                                                @Param("currencyCode") String currencyCode);

    /**
     * 插入科目余额
     *
     * @param balanceData 余额数据
     * @return 插入结果
     */
    int insertBalance(@Param("balanceData") Map<String, Object> balanceData);

    /**
     * 更新科目余额
     *
     * @param balanceData 余额数据
     * @return 更新结果
     */
    int updateBalance(@Param("balanceData") Map<String, Object> balanceData);

    /**
     * 删除科目余额
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @param currencyCode 币种代码
     * @return 删除结果
     */
    int deleteBalance(@Param("bookId") Long bookId,
                     @Param("accountCode") String accountCode,
                     @Param("period") String period,
                     @Param("currencyCode") String currencyCode);

    /**
     * 批量刷新科目余额
     *
     * @param param 刷新参数
     * @return 刷新结果
     */
    int batchRefreshBalance(@Param("param") BalanceRefreshParam param);

    /**
     * 计算科目余额
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @param currencyCode 币种代码
     * @return 计算结果
     */
    Map<String, Object> calculateAccountBalance(@Param("bookId") Long bookId,
                                              @Param("accountCode") String accountCode,
                                              @Param("period") String period,
                                              @Param("currencyCode") String currencyCode);

    /**
     * 分页查询余额调整申请
     *
     * @param param 查询参数
     * @return 调整申请列表
     */
    List<Map<String, Object>> selectBalanceAdjustmentPage(@Param("param") BalanceQueryParam param);

    /**
     * 查询余额调整申请总数
     *
     * @param param 查询参数
     * @return 总数
     */
    Long selectBalanceAdjustmentCount(@Param("param") BalanceQueryParam param);

    /**
     * 根据ID查询余额调整详情
     *
     * @param adjustmentId 调整ID
     * @return 调整详情
     */
    Map<String, Object> selectBalanceAdjustmentById(@Param("adjustmentId") Long adjustmentId);

    /**
     * 插入余额调整申请
     *
     * @param adjustmentData 调整数据
     * @return 插入结果
     */
    int insertBalanceAdjustment(@Param("adjustmentData") Map<String, Object> adjustmentData);

    /**
     * 更新余额调整申请
     *
     * @param adjustmentData 调整数据
     * @return 更新结果
     */
    int updateBalanceAdjustment(@Param("adjustmentData") Map<String, Object> adjustmentData);

    /**
     * 更新余额调整状态
     *
     * @param adjustmentId 调整ID
     * @param status 状态
     * @param approverId 审批人ID
     * @param approvalComment 审批意见
     * @return 更新结果
     */
    int updateAdjustmentStatus(@Param("adjustmentId") Long adjustmentId,
                              @Param("status") String status,
                              @Param("approverId") Long approverId,
                              @Param("approvalComment") String approvalComment);

    /**
     * 获取余额调整类型列表
     *
     * @return 调整类型列表
     */
    List<Map<String, Object>> selectAdjustmentTypes();

    /**
     * 获取科目余额汇总信息
     *
     * @param param 查询参数
     * @return 汇总信息
     */
    Map<String, Object> selectBalanceSummary(@Param("param") BalanceQueryParam param);

    /**
     * 校验余额数据
     *
     * @param param 查询参数
     * @return 校验结果
     */
    List<Map<String, Object>> validateBalanceData(@Param("param") BalanceQueryParam param);

    /**
     * 获取余额刷新历史
     *
     * @param bookId 账簿ID
     * @param period 期间
     * @param limit 限制条数
     * @return 刷新历史
     */
    List<Map<String, Object>> selectRefreshHistory(@Param("bookId") Long bookId,
                                                  @Param("period") String period,
                                                  @Param("limit") Integer limit);

    /**
     * 插入刷新历史记录
     *
     * @param historyData 历史数据
     * @return 插入结果
     */
    int insertRefreshHistory(@Param("historyData") Map<String, Object> historyData);

    /**
     * 检查余额调整是否可行
     *
     * @param param 调整参数
     * @return 检查结果
     */
    Map<String, Object> checkAdjustmentEligibility(@Param("param") BalanceAdjustmentParam param);

    /**
     * 获取调整影响分析
     *
     * @param param 调整参数
     * @return 影响分析
     */
    List<Map<String, Object>> getAdjustmentImpact(@Param("param") BalanceAdjustmentParam param);

    /**
     * 获取余额调整历史记录
     *
     * @param bookId 账簿ID
     * @param accountCode 科目编码
     * @param period 期间
     * @param limit 限制条数
     * @return 历史记录
     */
    List<Map<String, Object>> selectAdjustmentHistory(@Param("bookId") Long bookId,
                                                     @Param("accountCode") String accountCode,
                                                     @Param("period") String period,
                                                     @Param("limit") Integer limit);

    /**
     * 获取待审批的调整申请数量
     *
     * @param userId 用户ID
     * @return 待审批数量
     */
    Integer selectPendingApprovalCount(@Param("userId") Long userId);

    /**
     * 获取下级科目列表
     *
     * @param parentAccountCode 父科目编码
     * @param bookId 账簿ID
     * @return 下级科目列表
     */
    List<String> selectSubAccountCodes(@Param("parentAccountCode") String parentAccountCode,
                                      @Param("bookId") Long bookId);

    /**
     * 批量删除余额调整申请
     *
     * @param adjustmentIds 调整ID列表
     * @return 删除结果
     */
    int batchDeleteAdjustments(@Param("adjustmentIds") List<Long> adjustmentIds);
}