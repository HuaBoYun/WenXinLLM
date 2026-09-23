package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.ReportTaskQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportTask;

import java.util.List;

/**
 * 报表任务Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportTaskService extends IService<TblReportTask> {

    /**
     * 查询报表任务列表
     * 
     * @param param 查询参数
     * @return 报表任务列表
     */
    List<TblReportTask> getList(ReportTaskQueryParam param);

    /**
     * 根据ID查询报表任务详情
     * 
     * @param taskId 任务ID
     * @return 报表任务详情
     */
    TblReportTask getDetail(String taskId);

    /**
     * 保存报表任务(新增或修改)
     * 
     * @param reportTask 报表任务信息
     * @return 保存结果
     */
    boolean saveReportTask(TblReportTask reportTask);

    /**
     * 删除报表任务
     * 
     * @param taskId 任务ID
     * @return 删除结果
     */
    boolean deleteReportTask(String taskId);

    /**
     * 发布报表任务
     * 
     * @param taskId 任务ID
     * @return 发布结果
     */
    boolean publishTask(String taskId);

    /**
     * 撤回报表任务
     * 
     * @param taskId 任务ID
     * @return 撤回结果
     */
    boolean withdrawTask(String taskId);

    /**
     * 根据表单组ID查询报表任务列表
     * 
     * @param groupId 表单组ID
     * @return 报表任务列表
     */
    List<TblReportTask> getListByGroupId(String groupId);
}

