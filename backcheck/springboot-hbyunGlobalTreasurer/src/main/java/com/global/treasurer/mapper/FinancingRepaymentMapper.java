package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingRepayment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资还款Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-02-05
 */
@Mapper
public interface FinancingRepaymentMapper extends BaseMapper<TblFinancingRepayment> {

    /**
     * 分页查询还款列表
     * @param params 查询参数
     * @return 还款列表
     */
    List<TblFinancingRepayment> selectRepaymentList(Map<String, Object> params);

    /**
     * 根据ID查询还款详情
     * @param repaymentId 还款ID
     * @return 还款详情
     */
    TblFinancingRepayment selectRepaymentById(@Param("repaymentId") Long repaymentId);

    /**
     * 根据融资ID查询还款列表
     * @param financingId 融资ID
     * @return 还款列表
     */
    List<TblFinancingRepayment> selectRepaymentsByFinancingId(@Param("financingId") Long financingId);

    /**
     * 更新还款状态
     * @param repaymentId 还款ID
     * @param status 状态
     * @return 影响行数
     */
    int updateRepaymentStatus(@Param("repaymentId") Long repaymentId, @Param("status") String status);

    /**
     * 批量删除还款记录
     * @param repaymentIds 还款ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("repaymentIds") List<Long> repaymentIds);

    /**
     * 查询即将到期的还款
     * @param days 天数
     * @return 还款列表
     */
    List<TblFinancingRepayment> selectUpcomingRepayments(@Param("days") Integer days);

    /**
     * 查询逾期还款
     * @return 还款列表
     */
    List<TblFinancingRepayment> selectOverdueRepayments();

    /**
     * 统计还款数据
     * @param companyId 公司ID
     * @return 统计结果
     */
    Map<String, Object> selectRepaymentSummary(@Param("companyId") Long companyId);
}

