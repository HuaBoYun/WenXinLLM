package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBankLoan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 银行贷款Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface BankLoanMapper extends BaseMapper<TblBankLoan> {

    /**
     * 分页查询银行贷款列表
     *
     * @param params 查询参数
     * @return 银行贷款列表
     */
    List<TblBankLoan> selectLoanList(Map<String, Object> params);

    /**
     * 根据ID查询银行贷款详情
     *
     * @param loanId 贷款ID
     * @return 银行贷款
     */
    TblBankLoan selectLoanById(@Param("loanId") Long loanId);

    /**
     * 根据申请编号查询
     *
     * @param applicationNo 申请编号
     * @return 银行贷款
     */
    TblBankLoan selectByApplicationNo(@Param("applicationNo") String applicationNo);

    /**
     * 统计银行贷款数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countLoanList(Map<String, Object> params);

    /**
     * 更新贷款状态
     *
     * @param loanId 贷款ID
     * @param status 状态
     * @return 影响行数
     */
    int updateLoanStatus(@Param("loanId") Long loanId, @Param("status") String status);

    /**
     * 批量删除银行贷款（逻辑删除）
     *
     * @param loanIds 贷款ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("loanIds") List<Long> loanIds);

    /**
     * 查询贷款汇总统计
     *
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> selectLoanSummary(@Param("companyId") Long companyId);

    /**
     * 查询即将到期的贷款
     *
     * @param days 天数
     * @return 贷款列表
     */
    List<TblBankLoan> selectExpiringLoans(@Param("days") Integer days);

    /**
     * 查询贷款类型分布
     *
     * @param companyId 公司ID
     * @return 类型分布数据
     */
    List<Map<String, Object>> selectLoanTypeDistribution(@Param("companyId") Long companyId);

    /**
     * 查询贷款申请趋势
     *
     * @param companyId 公司ID
     * @param months 月数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectLoanTrend(@Param("companyId") Long companyId, @Param("months") Integer months);
}

