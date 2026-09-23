package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.ExecutionRecordQueryParam;
import com.financial.sharing.budgetControl.service.ExecutionRecordService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 执行记录Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预算执行控制-执行记录管理")
@RestController
@RequestMapping("/financialSharing/budgetControl/executionRecord")
public class ExecutionRecordController {

    @Autowired
    private ExecutionRecordService executionRecordService;

    @ApiOperation(value = "分页查询执行记录")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody ExecutionRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return executionRecordService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询执行记录")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestParam String recordId) {
        try {
            return executionRecordService.queryById(recordId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询执行记录统计")
    @PostMapping("/queryStatistics")
    public MyJsonBean queryStatistics(@RequestBody ExecutionRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return executionRecordService.queryStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "导出执行记录")
    @PostMapping("/exportRecords")
    public MyJsonBean exportRecords(@RequestBody ExecutionRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return executionRecordService.exportRecords(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }
}

