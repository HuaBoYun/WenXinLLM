package com.financial.sharing.groupControl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.groupControl.entity.TblDimensionInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 维度信息Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2026-01-30
 */
public interface DimensionMapper extends BaseMapper<TblDimensionInfo> {

    /**
     * 根据维度编码查询维度信息
     * 
     * @param dimensionCode 维度编码
     * @param tenantId 租户ID
     * @return 维度信息
     */
    TblDimensionInfo selectByCode(@Param("dimensionCode") String dimensionCode, 
                                   @Param("tenantId") String tenantId);

    /**
     * 根据维度类型查询维度列表
     * 
     * @param dimensionType 维度类型
     * @param tenantId 租户ID
     * @return 维度列表
     */
    List<TblDimensionInfo> selectByType(@Param("dimensionType") String dimensionType, 
                                         @Param("tenantId") String tenantId);

    /**
     * 根据状态查询维度列表
     * 
     * @param status 状态
     * @param tenantId 租户ID
     * @return 维度列表
     */
    List<TblDimensionInfo> selectByStatus(@Param("status") String status, 
                                           @Param("tenantId") String tenantId);
}

