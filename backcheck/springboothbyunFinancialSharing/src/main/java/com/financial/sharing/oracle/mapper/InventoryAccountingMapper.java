package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryAccountingEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 存货核算 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryAccountingMapper")
public interface InventoryAccountingMapper extends BaseMapper<InventoryAccountingEntity> {

    /**
     * 分页查询存货核算列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectAccountingPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据存货ID和核算期间查询核算记录
     *
     * @param inventoryId 存货ID
     * @param accountingPeriod 核算期间
     * @param tenantId 租户ID
     * @return 核算记录
     */
    InventoryAccountingEntity selectByInventoryAndPeriod(@Param("inventoryId") Long inventoryId,
                                                         @Param("accountingPeriod") String accountingPeriod,
                                                         @Param("tenantId") Long tenantId);

    /**
     * 根据核算期间查询核算列表
     *
     * @param accountingPeriod 核算期间
     * @param tenantId 租户ID
     * @return 核算列表
     */
    List<InventoryAccountingEntity> selectByPeriod(@Param("accountingPeriod") String accountingPeriod, @Param("tenantId") Long tenantId);

    /**
     * 根据核算状态查询核算列表
     *
     * @param accountingStatus 核算状态
     * @param tenantId 租户ID
     * @return 核算列表
     */
    List<InventoryAccountingEntity> selectByStatus(@Param("accountingStatus") Integer accountingStatus, @Param("tenantId") Long tenantId);

    /**
     * 批量执行存货核算
     *
     * @param accountingIds 核算ID列表
     * @param accountingStatus 核算状态
     * @param updaterId 更新人ID
     * @return 更新数量
     */
    int batchExecuteAccounting(@Param("accountingIds") List<Long> accountingIds,
                               @Param("accountingStatus") Integer accountingStatus,
                               @Param("updaterId") String updaterId);

    /**
     * 批量删除核算记录
     *
     * @param accountingIds 核算ID列表
     * @return 删除数量
     */
    int batchDelete(@Param("accountingIds") List<Long> accountingIds);

    /**
     * 获取存货核算历史
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 核算历史列表
     */
    List<Map<String, Object>> selectAccountingHistory(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 统计存货核算数据
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> countAccountingStatistics(@Param("param") Map<String, Object> param);
}

