package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblReportTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表任务Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportTaskMapper extends BaseMapper<TblReportTask> {

    /**
     * 查询报表任务列表(关联表单组名称)
     * 
     * @param tenantId 租户ID
     * @param groupId 表单组ID
     * @param taskName 任务名称
     * @param periodType 周期类型
     * @param status 状态
     * @return 报表任务列表
     */
    List<TblReportTask> selectReportTaskList(@Param("tenantId") String tenantId,
                                              @Param("groupId") String groupId,
                                              @Param("taskName") String taskName,
                                              @Param("periodType") String periodType,
                                              @Param("status") String status);

    /**
     * 检查任务编码是否存在
     * 
     * @param taskCode 任务编码
     * @param tenantId 租户ID
     * @param excludeTaskId 排除的任务ID
     * @return 数量
     */
    int checkTaskCodeExists(@Param("taskCode") String taskCode,
                           @Param("tenantId") String tenantId,
                           @Param("excludeTaskId") String excludeTaskId);
}

