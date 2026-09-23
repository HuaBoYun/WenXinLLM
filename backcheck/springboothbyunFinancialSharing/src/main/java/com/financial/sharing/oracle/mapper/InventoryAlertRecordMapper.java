package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.InventoryAlertRecordEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 预警记录 Mapper接口 - Oracle/达梦版本
 *
 * @author system
 * @since 2026-01-26
 */
@Component("oracleInventoryAlertRecordMapper")
public interface InventoryAlertRecordMapper extends BaseMapper<InventoryAlertRecordEntity> {

    /**
     * 分页查询预警记录列表
     *
     * @param page 分页参数
     * @param param 查询参数
     * @return 分页结果
     */
    IPage<Map<String, Object>> selectRecordPage(Page<Map<String, Object>> page, @Param("param") Map<String, Object> param);

    /**
     * 根据规则ID查询记录列表
     *
     * @param ruleId 规则ID
     * @param tenantId 租户ID
     * @return 记录列表
     */
    List<InventoryAlertRecordEntity> selectByRuleId(@Param("ruleId") Long ruleId, @Param("tenantId") Long tenantId);

    /**
     * 根据存货ID查询记录列表
     *
     * @param inventoryId 存货ID
     * @param tenantId 租户ID
     * @return 记录列表
     */
    List<InventoryAlertRecordEntity> selectByInventoryId(@Param("inventoryId") Long inventoryId, @Param("tenantId") Long tenantId);

    /**
     * 根据预警类型查询记录列表
     *
     * @param alertType 预警类型
     * @param tenantId 租户ID
     * @return 记录列表
     */
    List<InventoryAlertRecordEntity> selectByAlertType(@Param("alertType") String alertType, @Param("tenantId") Long tenantId);

    /**
     * 根据处理状态查询记录列表
     *
     * @param processStatus 处理状态
     * @param tenantId 租户ID
     * @return 记录列表
     */
    List<InventoryAlertRecordEntity> selectByProcessStatus(@Param("processStatus") Integer processStatus, @Param("tenantId") Long tenantId);

    /**
     * 处理预警
     *
     * @param recordId 记录ID
     * @param processStatus 处理状态
     * @param processRemark 处理备注
     * @param processorId 处理人ID
     * @param processorName 处理人姓名
     * @return 更新数量
     */
    int processAlert(@Param("recordId") Long recordId,
                    @Param("processStatus") Integer processStatus,
                    @Param("processRemark") String processRemark,
                    @Param("processorId") String processorId,
                    @Param("processorName") String processorName);

    /**
     * 忽略预警
     *
     * @param recordId 记录ID
     * @param processorId 处理人ID
     * @param processorName 处理人姓名
     * @return 更新数量
     */
    int ignoreAlert(@Param("recordId") Long recordId,
                   @Param("processorId") String processorId,
                   @Param("processorName") String processorName);

    /**
     * 批量处理预警
     *
     * @param recordIds 记录ID列表
     * @param processStatus 处理状态
     * @param processorId 处理人ID
     * @return 更新数量
     */
    int batchProcessAlert(@Param("recordIds") List<Long> recordIds,
                         @Param("processStatus") Integer processStatus,
                         @Param("processorId") String processorId);

    /**
     * 统计预警数据
     *
     * @param param 查询参数
     * @return 统计结果
     */
    Map<String, Object> countAlertStatistics(@Param("param") Map<String, Object> param);

    /**
     * 获取预警趋势分析
     *
     * @param param 查询参数
     * @return 趋势数据
     */
    List<Map<String, Object>> selectAlertTrend(@Param("param") Map<String, Object> param);

    /**
     * 获取预警类型分布
     *
     * @param param 查询参数
     * @return 分布数据
     */
    List<Map<String, Object>> selectAlertDistribution(@Param("param") Map<String, Object> param);
}

