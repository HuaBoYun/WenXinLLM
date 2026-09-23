package com.global.treasurer.mapper;

import com.global.treasurer.entity.TblGuaranteeMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 担保监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-14
 */
@Mapper
public interface GuaranteeMonitoringMapper {

    /**
     * 查询担保监控预警列表
     */
    List<TblGuaranteeMonitoring> selectMonitoringList(Map<String, Object> params);

    /**
     * 根据ID查询
     */
    TblGuaranteeMonitoring selectById(@Param("alertId") Long alertId);

    /**
     * 插入
     */
    int insert(TblGuaranteeMonitoring monitoring);

    /**
     * 更新
     */
    int updateById(TblGuaranteeMonitoring monitoring);

    /**
     * 删除（软删除）
     */
    int deleteById(@Param("alertId") Long alertId);

    /**
     * 更新预警状态
     */
    int updateAlertStatus(@Param("alertId") Long alertId, @Param("status") String status);
}
