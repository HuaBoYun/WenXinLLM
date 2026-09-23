package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.dto.ReportDataQueryDTO;
import com.financial.sharing.enterpriseReport.entity.TblReportData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表数据Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportDataMapper extends BaseMapper<TblReportData> {

    /**
     * 查询报表数据列表(关联任务、模板、指标名称)
     * 
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param templateId 模板ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @param period 期间
     * @param dataSource 数据来源
     * @return 报表数据列表
     */
    List<TblReportData> selectReportDataList(@Param("tenantId") String tenantId,
                                              @Param("taskId") String taskId,
                                              @Param("templateId") String templateId,
                                              @Param("indicatorId") String indicatorId,
                                              @Param("orgId") String orgId,
                                              @Param("period") String period,
                                              @Param("dataSource") String dataSource);

    /**
     * 批量插入报表数据
     * 
     * @param dataList 报表数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("dataList") List<TblReportData> dataList);

    /**
     * 批量更新报表数据
     * 
     * @param dataList 报表数据列表
     * @return 更新数量
     */
    int batchUpdate(@Param("dataList") List<TblReportData> dataList);

    /**
     * 删除指定条件的报表数据
     * 
     * @param taskId 任务ID
     * @param templateId 模板ID
     * @param orgId 组织ID
     * @param period 期间
     * @param tenantId 租户ID
     * @return 删除数量
     */
    int deleteByCondition(@Param("taskId") String taskId,
                         @Param("templateId") String templateId,
                         @Param("orgId") String orgId,
                         @Param("period") String period,
                         @Param("tenantId") String tenantId);

    /**
     * 按指标汇总数据
     *
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param period 期间
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByIndicator(@Param("tenantId") String tenantId,
                                                   @Param("taskId") String taskId,
                                                   @Param("period") String period);

    /**
     * 按组织汇总数据
     *
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param period 期间
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByOrg(@Param("tenantId") String tenantId,
                                            @Param("taskId") String taskId,
                                            @Param("period") String period);

    /**
     * 按期间汇总数据
     *
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByPeriod(@Param("tenantId") String tenantId,
                                               @Param("taskId") String taskId,
                                               @Param("indicatorId") String indicatorId,
                                               @Param("orgId") String orgId);

    /**
     * 期间对比查询
     *
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @param currentPeriod 当前期间
     * @param comparePeriod 对比期间
     * @return 对比结果
     */
    ReportDataQueryDTO comparePeriod(@Param("tenantId") String tenantId,
                                     @Param("taskId") String taskId,
                                     @Param("indicatorId") String indicatorId,
                                     @Param("orgId") String orgId,
                                     @Param("currentPeriod") String currentPeriod,
                                     @Param("comparePeriod") String comparePeriod);
}

