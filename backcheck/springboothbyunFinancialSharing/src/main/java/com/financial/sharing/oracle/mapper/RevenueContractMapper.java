package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.RevenueContractEntity;
import com.financial.sharing.vo.param.RevenueContractQueryParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 收入合同 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2024-12-19
 */
@Component("oracleRevenueContractMapper")
public interface RevenueContractMapper extends BaseMapper<RevenueContractEntity> {

    /**
     * 查询收入合同列表
     *
     * @param param 查询参数
     * @return 合同列表
     */
    List<RevenueContractEntity> selectRevenueContractList(@Param("param") RevenueContractQueryParam param);

    /**
     * 查询收入结构分析数据
     *
     * @param dimension 分析维度 (1-按产品 2-按客户 3-按部门)
     * @param dateRange 日期范围
     * @return 结构分析数据
     */
    List<Map<String, Object>> selectRevenueStructureData(@Param("dimension") Integer dimension, @Param("dateRange") String[] dateRange);

    /**
     * 查询收入质量指标
     *
     * @return 质量指标数据
     */
    Map<String, Object> selectRevenueQualityMetrics();

    /**
     * 查询历史收入数据
     *
     * @param months 查询月数
     * @return 历史收入数据
     */
    List<Map<String, Object>> selectHistoricalRevenueData(@Param("months") Integer months);
}

