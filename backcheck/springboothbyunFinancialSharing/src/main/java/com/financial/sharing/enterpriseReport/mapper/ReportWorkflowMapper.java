package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblReportWorkflow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 报表工作流Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportWorkflowMapper extends BaseMapper<TblReportWorkflow> {

    /**
     * 查询报表工作流列表(关联任务名称)
     * 
     * @param tenantId 租户ID
     * @param taskId 任务ID
     * @param workflowName 流程名称
     * @param workflowType 流程类型
     * @param status 状态
     * @return 报表工作流列表
     */
    List<TblReportWorkflow> selectReportWorkflowList(@Param("tenantId") String tenantId,
                                                      @Param("taskId") String taskId,
                                                      @Param("workflowName") String workflowName,
                                                      @Param("workflowType") String workflowType,
                                                      @Param("status") String status);

    /**
     * 检查流程编码是否存在
     * 
     * @param workflowCode 流程编码
     * @param tenantId 租户ID
     * @param excludeWorkflowId 排除的流程ID
     * @return 数量
     */
    int checkWorkflowCodeExists(@Param("workflowCode") String workflowCode,
                               @Param("tenantId") String tenantId,
                               @Param("excludeWorkflowId") String excludeWorkflowId);
}

