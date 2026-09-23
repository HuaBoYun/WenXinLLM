package com.financial.sharing.enterpriseReport.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.financial.sharing.enterpriseReport.dto.ReportDataQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportData;

import java.util.List;

/**
 * 报表数据Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportDataService extends IService<TblReportData> {

    /**
     * 查询报表数据列表
     * 
     * @param param 查询参数
     * @return 报表数据列表
     */
    List<TblReportData> getList(ReportDataQueryParam param);

    /**
     * 根据ID查询报表数据详情
     * 
     * @param dataId 数据ID
     * @return 报表数据详情
     */
    TblReportData getDetail(String dataId);

    /**
     * 保存报表数据(新增或修改)
     * 
     * @param reportData 报表数据信息
     * @return 保存结果
     */
    boolean saveReportData(TblReportData reportData);

    /**
     * 批量保存报表数据
     * 
     * @param dataList 报表数据列表
     * @return 保存结果
     */
    boolean batchSaveReportData(List<TblReportData> dataList);

    /**
     * 删除报表数据
     * 
     * @param dataId 数据ID
     * @return 删除结果
     */
    boolean deleteReportData(String dataId);

    /**
     * 根据条件删除报表数据
     * 
     * @param taskId 任务ID
     * @param templateId 模板ID
     * @param orgId 组织ID
     * @param period 期间
     * @return 删除结果
     */
    boolean deleteByCondition(String taskId, String templateId, String orgId, String period);

    /**
     * 根据任务、模板、组织、期间查询报表数据
     * 
     * @param taskId 任务ID
     * @param templateId 模板ID
     * @param orgId 组织ID
     * @param period 期间
     * @return 报表数据列表
     */
    List<TblReportData> getDataByCondition(String taskId, String templateId, String orgId, String period);
}

