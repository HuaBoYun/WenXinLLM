package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.ReportDataQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblReportData;
import com.financial.sharing.enterpriseReport.service.ReportDataService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 报表数据Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-报表数据管理")
@RestController
@RequestMapping("/enterpriseReport/reportData")
public class ReportDataController {

    @Autowired
    private ReportDataService reportDataService;

    /**
     * 查询报表数据列表
     */
    @ApiOperation("查询报表数据列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody ReportDataQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblReportData> list = reportDataService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询报表数据详情
     */
    @ApiOperation("查询报表数据详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }
            
            TblReportData reportData = reportDataService.getDetail(dataId);
            return MyJsonBean.ok(reportData);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存报表数据
     */
    @ApiOperation("保存报表数据")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblReportData reportData) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (reportData.getTaskId() == null || reportData.getTaskId().isEmpty()) {
                return MyJsonBean.errorData("任务ID不能为空");
            }
            if (reportData.getTemplateId() == null || reportData.getTemplateId().isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            if (reportData.getIndicatorId() == null || reportData.getIndicatorId().isEmpty()) {
                return MyJsonBean.errorData("指标ID不能为空");
            }
            if (reportData.getOrgId() == null || reportData.getOrgId().isEmpty()) {
                return MyJsonBean.errorData("组织ID不能为空");
            }
            if (reportData.getPeriod() == null || reportData.getPeriod().isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            boolean result = reportDataService.saveReportData(reportData);
            if (result) {
                return MyJsonBean.ok("保存成功");
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 批量保存报表数据
     */
    @ApiOperation("批量保存报表数据")
    @PostMapping("/batchSave")
    public MyJsonBean batchSave(@RequestBody List<TblReportData> dataList) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (dataList == null || dataList.isEmpty()) {
                return MyJsonBean.errorData("数据列表不能为空");
            }
            
            boolean result = reportDataService.batchSaveReportData(dataList);
            if (result) {
                return MyJsonBean.ok("保存成功");
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除报表数据
     */
    @ApiOperation("删除报表数据")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String dataId = params.get("dataId");
            if (dataId == null || dataId.isEmpty()) {
                return MyJsonBean.errorData("数据ID不能为空");
            }

            boolean result = reportDataService.deleteReportData(dataId);
            if (result) {
                return MyJsonBean.ok("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据条件删除报表数据
     */
    @ApiOperation("根据条件删除报表数据")
    @PostMapping("/deleteByCondition")
    public MyJsonBean deleteByCondition(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String taskId = params.get("taskId");
            String templateId = params.get("templateId");
            String orgId = params.get("orgId");
            String period = params.get("period");

            boolean result = reportDataService.deleteByCondition(taskId, templateId, orgId, period);
            if (result) {
                return MyJsonBean.ok("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据条件查询报表数据
     */
    @ApiOperation("根据条件查询报表数据")
    @PostMapping("/getDataByCondition")
    public MyJsonBean getDataByCondition(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String taskId = params.get("taskId");
            String templateId = params.get("templateId");
            String orgId = params.get("orgId");
            String period = params.get("period");

            List<TblReportData> list = reportDataService.getDataByCondition(taskId, templateId, orgId, period);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}


