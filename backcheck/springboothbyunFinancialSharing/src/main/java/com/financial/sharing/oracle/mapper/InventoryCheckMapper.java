package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryCheckEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 存货盘点 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryCheckMapper")
public interface InventoryCheckMapper extends BaseMapper<InventoryCheckEntity> {

    /**
     * 分页查询存货盘点列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectCheckPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据盘点单号查询盘点记录
     *
     * @param checkNo 盘点单号
     * @param tenantId 租户ID
     * @return 盘点记录
     */
    InventoryCheckEntity selectByCheckNo(@Param("checkNo") String checkNo, @Param("tenantId") Long tenantId);

    /**
     * 根据盘点类型查询盘点列表
     *
     * @param checkType 盘点类型
     * @param tenantId 租户ID
     * @return 盘点列表
     */
    List<InventoryCheckEntity> selectByCheckType(@Param("checkType") String checkType, @Param("tenantId") Long tenantId);

    /**
     * 根据盘点状态查询盘点列表
     *
     * @param checkStatus 盘点状态
     * @param tenantId 租户ID
     * @return 盘点列表
     */
    List<InventoryCheckEntity> selectByStatus(@Param("checkStatus") Integer checkStatus, @Param("tenantId") Long tenantId);

    /**
     * 根据仓库ID查询盘点列表
     *
     * @param warehouseId 仓库ID
     * @param tenantId 租户ID
     * @return 盘点列表
     */
    List<InventoryCheckEntity> selectByWarehouseId(@Param("warehouseId") Long warehouseId, @Param("tenantId") Long tenantId);

    /**
     * 批量执行盘点任务
     *
     * @param checkIds 盘点ID列表
     * @param checkStatus 盘点状态
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchExecuteCheck(@Param("checkIds") List<Long> checkIds,
                         @Param("checkStatus") Integer checkStatus,
                         @Param("updaterId") String updaterId);

    /**
     * 统计盘点数据
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> countCheckStatistics(@Param("param") Map<String, Object> param);
}

