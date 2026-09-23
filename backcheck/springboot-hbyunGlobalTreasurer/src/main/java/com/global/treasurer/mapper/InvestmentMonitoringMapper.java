package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.InvestmentMonitoringQueryDTO;
import com.global.treasurer.entity.TblInvestmentMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 投资监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-07
 */
@Mapper
public interface InvestmentMonitoringMapper extends BaseMapper<TblInvestmentMonitoring> {

    /**
     * 根据查询条件查询投资监控列表
     */
    List<TblInvestmentMonitoring> selectByQueryDTO(InvestmentMonitoringQueryDTO queryDTO);

    /**
     * 根据投资ID查询最新监控记录
     */
    TblInvestmentMonitoring selectLatestByInvestmentId(@Param("investmentId") Long investmentId);
}
