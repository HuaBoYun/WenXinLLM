package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblLoanMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 贷款监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Mapper
public interface LoanMonitoringMapper extends BaseMapper<TblLoanMonitoring> {

    /**
     * 查询监控预警列表
     */
    List<TblLoanMonitoring> selectMonitoringList(@Param("params") Map<String, Object> params);

    /**
     * 根据贷款ID查询监控预警
     */
    List<TblLoanMonitoring> selectByLoanId(@Param("loanId") String loanId);

    /**
     * 查询待处理预警
     */
    List<TblLoanMonitoring> selectPendingAlerts(@Param("loanId") String loanId);

    /**
     * 统计待处理预警数量
     */
    int countPendingAlerts(@Param("loanId") Long loanId);

    /**
     * 统计贷款的预警数量
     */
    int countByLoanId(@Param("loanId") String loanId);
}

