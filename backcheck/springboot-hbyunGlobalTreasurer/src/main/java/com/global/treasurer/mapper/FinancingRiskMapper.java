package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资风险监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Mapper
public interface FinancingRiskMapper extends BaseMapper<TblFinancingMonitoring> {

    /**
     * 分页查询风险列表
     *
     * @param params 查询参数
     * @return 风险列表
     */
    List<TblFinancingMonitoring> selectRiskList(Map<String, Object> params);

    /**
     * 根据ID查询风险详情
     *
     * @param monitoringId 监控ID
     * @return 风险监控
     */
    TblFinancingMonitoring selectRiskById(@Param("monitoringId") Long monitoringId);

    /**
     * 根据关联融资ID查询
     *
     * @param relatedFinancingId 关联融资ID
     * @return 风险列表
     */
    List<TblFinancingMonitoring> selectByFinancingId(@Param("relatedFinancingId") Long relatedFinancingId);

    /**
     * 统计风险数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countRiskList(Map<String, Object> params);

    /**
     * 批量删除风险记录（逻辑删除）
     *
     * @param monitoringIds 监控ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("monitoringIds") List<Long> monitoringIds);

    /**
     * 查询风险统计
     *
     * @param companyId 公司ID
     * @return 统计数据
     */
    Map<String, Object> selectRiskStatistics(@Param("companyId") Long companyId);

    /**
     * 查询预警数量统计
     *
     * @param companyId 公司ID
     * @return 统计数据
     */
    List<Map<String, Object>> selectAlertCountByLevel(@Param("companyId") Long companyId);
}
