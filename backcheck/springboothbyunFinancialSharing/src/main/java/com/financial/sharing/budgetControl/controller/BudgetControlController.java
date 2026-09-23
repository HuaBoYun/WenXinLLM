package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.BatchReleaseRequest;
import com.financial.sharing.budgetControl.dto.BatchTransferRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlRequest;
import com.financial.sharing.budgetControl.dto.BudgetControlResponse;
import com.financial.sharing.budgetControl.dto.BudgetOccupancyQueryParam;
import com.financial.sharing.budgetControl.dto.BudgetTransferRequest;
import com.financial.sharing.budgetControl.dto.ReleaseRecordQueryParam;
import com.financial.sharing.budgetControl.dto.TransferRecordQueryParam;
import com.financial.sharing.budgetControl.service.BudgetControlService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 预算控制Controller (OpenAPI)
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预算执行控制-预算控制接口(OpenAPI)")
@RestController
@RequestMapping("/financialSharing/budgetControl/api")
public class BudgetControlController {

    @Autowired
    private BudgetControlService budgetControlService;

    @ApiOperation(value = "预算控制检查", notes = "供业务系统调用的OpenAPI接口")
    @PostMapping("/checkBudget")
    public BudgetControlResponse checkBudget(@RequestBody BudgetControlRequest request) {
        return budgetControlService.checkBudget(request);
    }

    @ApiOperation(value = "预算占用")
    @PostMapping("/occupyBudget")
    public MyJsonBean occupyBudget(@RequestBody BudgetControlRequest request) {
        return budgetControlService.occupyBudget(request);
    }

    @ApiOperation(value = "预算释放")
    @PostMapping("/releaseBudget")
    public MyJsonBean releaseBudget(@RequestBody BudgetControlRequest request) {
        return budgetControlService.releaseBudget(request);
    }

    @ApiOperation(value = "预算转移")
    @PostMapping("/transferBudget")
    public MyJsonBean transferBudget(@RequestBody BudgetControlRequest fromRequest,
                                     @RequestBody BudgetControlRequest toRequest) {
        return budgetControlService.transferBudget(fromRequest, toRequest);
    }

    @ApiOperation(value = "查询预算占用情况")
    @PostMapping("/queryOccupancy")
    public MyJsonBean queryOccupancy(@RequestParam String bizOrgId,
                                     @RequestParam String subjectCode,
                                     @RequestParam String period,
                                     @RequestParam String orgId) {
        return budgetControlService.queryOccupancy(bizOrgId, subjectCode, period, orgId);
    }

    @ApiOperation(value = "分页查询预算占用情况")
    @PostMapping("/queryOccupancyPage")
    public MyJsonBean queryOccupancyPage(@RequestBody BudgetOccupancyQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.queryOccupancyPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询预算占用统计")
    @PostMapping("/queryOccupancyStatistics")
    public MyJsonBean queryOccupancyStatistics(@RequestBody BudgetOccupancyQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.queryOccupancyStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询预算占用趋势")
    @PostMapping("/queryOccupancyTrend")
    public MyJsonBean queryOccupancyTrend(@RequestBody BudgetOccupancyQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.queryOccupancyTrend(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量释放预算")
    @PostMapping("/batchReleaseBudget")
    public MyJsonBean batchReleaseBudget(@RequestBody BatchReleaseRequest request) {
        try {
            request.setOrgId(UserUtils.getOrgId());
            request.setOperateUser(UserUtils.getUserId());
            return budgetControlService.batchReleaseBudget(request);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量释放失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询释放记录")
    @PostMapping("/queryReleaseRecords")
    public MyJsonBean queryReleaseRecords(@RequestBody ReleaseRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.queryReleaseRecords(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取释放记录统计")
    @PostMapping("/getReleaseRecordStatistics")
    public MyJsonBean getReleaseRecordStatistics(@RequestBody ReleaseRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.getReleaseRecordStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "执行预算转移")
    @PostMapping("/transferBudgetEnhanced")
    public MyJsonBean transferBudgetEnhanced(@RequestBody BudgetTransferRequest request) {
        try {
            request.setOrgId(UserUtils.getOrgId());
            request.setOperateUser(UserUtils.getUserId());
            return budgetControlService.transferBudgetEnhanced(request);
        } catch (Exception e) {
            return MyJsonBean.errorData("转移失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量转移预算")
    @PostMapping("/batchTransferBudget")
    public MyJsonBean batchTransferBudget(@RequestBody BatchTransferRequest request) {
        try {
            request.setOrgId(UserUtils.getOrgId());
            request.setOperateUser(UserUtils.getUserId());
            return budgetControlService.batchTransferBudget(request);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量转移失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询转移记录")
    @PostMapping("/queryTransferRecords")
    public MyJsonBean queryTransferRecords(@RequestBody TransferRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.queryTransferRecords(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取转移记录统计")
    @PostMapping("/getTransferRecordStatistics")
    public MyJsonBean getTransferRecordStatistics(@RequestBody TransferRecordQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return budgetControlService.getTransferRecordStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}

