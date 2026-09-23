package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.RevenueAllocationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 收入分配Mapper接口
 */
public interface RevenueAllocationMapper extends BaseMapper<RevenueAllocationEntity> {

    /**
     * 分页查询收入分配列表
     *
     * @param allocationNo 分配单号
     * @param allocationType 分配类型
     * @param allocationStatus 分配状态
     * @param allocationPeriod 分配期间
     * @param tenantId 租户ID
     * @param offset 偏移量
     * @param pageSize 每页大小
     * @return 收入分配列表
     */
    List<Map<String, Object>> selectAllocationList(
            @Param("allocationNo") String allocationNo,
            @Param("allocationType") Integer allocationType,
            @Param("allocationStatus") Integer allocationStatus,
            @Param("allocationPeriod") String allocationPeriod,
            @Param("tenantId") Long tenantId,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询收入分配总数
     *
     * @param allocationNo 分配单号
     * @param allocationType 分配类型
     * @param allocationStatus 分配状态
     * @param allocationPeriod 分配期间
     * @param tenantId 租户ID
     * @return 总数
     */
    Integer countAllocationList(
            @Param("allocationNo") String allocationNo,
            @Param("allocationType") Integer allocationType,
            @Param("allocationStatus") Integer allocationStatus,
            @Param("allocationPeriod") String allocationPeriod,
            @Param("tenantId") Long tenantId
    );
}

