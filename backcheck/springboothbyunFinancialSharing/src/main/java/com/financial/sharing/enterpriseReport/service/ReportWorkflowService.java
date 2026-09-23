package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.ReportWorkflowQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportWorkflow;

import java.util.List;

/**
 * 报表工作流Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportWorkflowService extends IService<TblReportWorkflow> {

    /**
     * 查询报表工作流列表
     * 
     * @param param 查询参数
     * @return 报表工作流列表
     */
    List<TblReportWorkflow> getList(ReportWorkflowQueryParam param);

    /**
     * 根据ID查询报表工作流详情
     * 
     * @param workflowId 流程ID
     * @return 报表工作流详情
     */
    TblReportWorkflow getDetail(String workflowId);

    /**
     * 保存报表工作流(新增或修改)
     * 
     * @param reportWorkflow 报表工作流信息
     * @return 保存结果
     */
    boolean saveReportWorkflow(TblReportWorkflow reportWorkflow);

    /**
     * 删除报表工作流
     * 
     * @param workflowId 流程ID
     * @return 删除结果
     */
    boolean deleteReportWorkflow(String workflowId);

    /**
     * 根据任务ID查询报表工作流列表
     * 
     * @param taskId 任务ID
     * @return 报表工作流列表
     */
    List<TblReportWorkflow> getListByTaskId(String taskId);

    /**
     * 更新工作流状态
     * 
     * @param workflowId 流程ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateWorkflowStatus(String workflowId, String status);
}

