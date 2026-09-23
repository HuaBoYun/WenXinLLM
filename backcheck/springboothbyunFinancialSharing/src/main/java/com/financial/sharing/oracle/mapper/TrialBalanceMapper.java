package com.financial.sharing.oracle.mapper;

import com.financial.sharing.vo.param.TrialBalanceParam;
import com.financial.sharing.vo.result.TrialBalanceResult;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 试算平衡数据访问接口
 *
 * @author system
 * @since 2024-12-19
 */
public interface TrialBalanceMapper {

    /**
     * 获取科目余额列表
     *
     * @param param 试算平衡参数
     * @return 科目余额列表
     */
    List<TrialBalanceResult.SubjectBalance> getSubjectBalances(@Param("param") TrialBalanceParam param);

    /**
     * 获取指定科目的余额信息
     *
     * @param bookId 账簿ID
     * @param subjectId 科目ID
     * @param accountingPeriod 会计期间
     * @return 科目余额
     */
    TrialBalanceResult.SubjectBalance getSubjectBalance(
            @Param("bookId") Long bookId,
            @Param("subjectId") Long subjectId,
            @Param("accountingPeriod") String accountingPeriod
    );

    /**
     * 计算借方合计
     *
     * @param param 试算平衡参数
     * @return 借方合计金额
     */
    BigDecimal calculateTotalDebit(@Param("param") TrialBalanceParam param);

    /**
     * 计算贷方合计
     *
     * @param param 试算平衡参数
     * @return 贷方合计金额
     */
    BigDecimal calculateTotalCredit(@Param("param") TrialBalanceParam param);

    /**
     * 获取期间内所有凭证的借贷发生额
     *
     * @param bookId 账簿ID
     * @param accountingPeriod 会计期间
     * @param subjectType 科目类型
     * @return 发生额汇总
     */
    List<java.util.Map<String, Object>> getVoucherAmountsByPeriod(
            @Param("bookId") Long bookId,
            @Param("accountingPeriod") String accountingPeriod,
            @Param("subjectType") Integer subjectType
    );

    /**
     * 获取科目期初余额
     *
     * @param bookId 账簿ID
     * @param accountingPeriod 会计期间
     * @param subjectIds 科目ID列表
     * @return 期初余额列表
     */
    List<TrialBalanceResult.SubjectBalance> getOpeningBalances(
            @Param("bookId") Long bookId,
            @Param("accountingPeriod") String accountingPeriod,
            @Param("subjectIds") List<Long> subjectIds
    );

    /**
     * 获取科目本期发生额
     *
     * @param bookId 账簿ID
     * @param accountingPeriod 会计期间
     * @param subjectIds 科目ID列表
     * @param includeUnposted 是否包含未过账凭证
     * @return 发生额列表
     */
    List<TrialBalanceResult.SubjectBalance> getPeriodAmounts(
            @Param("bookId") Long bookId,
            @Param("accountingPeriod") String accountingPeriod,
            @Param("subjectIds") List<Long> subjectIds,
            @Param("includeUnposted") Boolean includeUnposted
    );

    /**
     * 检查科目是否存在余额方向错误
     *
     * @param bookId 账簿ID
     * @param accountingPeriod 会计期间
     * @return 错误科目列表
     */
    List<TrialBalanceResult.BalanceError> checkBalanceDirectionErrors(
            @Param("bookId") Long bookId,
            @Param("accountingPeriod") String accountingPeriod
    );

    /**
     * 获取科目累计发生额
     *
     * @param bookId 账簿ID
     * @param endPeriod 结束期间
     * @param subjectIds 科目ID列表
     * @return 累计发生额列表
     */
    List<TrialBalanceResult.SubjectBalance> getAccumulatedAmounts(
            @Param("bookId") Long bookId,
            @Param("endPeriod") String endPeriod,
            @Param("subjectIds") List<Long> subjectIds
    );

    /**
     * 检查期间是否连续
     *
     * @param bookId 账簿ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 是否连续
     */
    Integer checkPeriodContinuity(
            @Param("bookId") Long bookId,
            @Param("startPeriod") String startPeriod,
            @Param("endPeriod") String endPeriod
    );

    /**
     * 获取试算平衡历史记录
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param period 会计期间
     * @param limit 记录数限制
     * @return 历史记录列表
     */
    List<TrialBalanceResult> getTrialBalanceHistory(
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId,
            @Param("period") String period,
            @Param("limit") Integer limit
    );

    /**
     * 保存试算平衡结果
     *
     * @param result 试算平衡结果
     * @return 影响行数
     */
    int saveTrialBalanceResult(@Param("result") TrialBalanceResult result);

    /**
     * 删除试算平衡记录
     *
     * @param taskId 任务ID
     * @return 影响行数
     */
    int deleteTrialBalance(@Param("taskId") String taskId);

    /**
     * 获取可用的会计期间
     *
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 期间列表
     */
    List<String> getAvailablePeriods(
            @Param("bookId") Long bookId,
            @Param("tenantId") Long tenantId
    );

    /**
     * 验证会计期间是否存在
     *
     * @param bookId 账簿ID
     * @param accountingPeriod 会计期间
     * @return 是否存在
     */
    Integer validateAccountingPeriod(
            @Param("bookId") Long bookId,
            @Param("accountingPeriod") String accountingPeriod
    );
}