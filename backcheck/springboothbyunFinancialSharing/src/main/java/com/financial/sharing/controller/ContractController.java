package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblContract;
import com.financial.sharing.business.entity.TblContractPaymentPlan;
import com.financial.sharing.business.service.ContractService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 合同管理控制器
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Api(tags = "合同管理")
@RestController
@RequestMapping("/contracts")
@CrossOrigin
public class ContractController {

    @Autowired
    private ContractService contractService;

    @ApiOperation("查询合同列表")
    @GetMapping
    public MyJsonBean getContractList(
            @RequestParam(required = false) String contractCode,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new java.util.HashMap<>();
            param.put("contractCode", contractCode);
            param.put("contractName", contractName);
            param.put("contractType", contractType);
            param.put("status", status);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return contractService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取合同详情")
    @GetMapping("/{contractId}")
    public MyJsonBean getContractDetail(@PathVariable String contractId) {
        try {
            return contractService.getById(contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据合同编号查询")
    @GetMapping("/code/{contractCode}")
    public MyJsonBean getContractByCode(@PathVariable String contractCode) {
        try {
            return contractService.getByContractCode(contractCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存合同")
    @PostMapping
    public MyJsonBean saveContract(@RequestBody TblContract contract) {
        try {
            return contractService.saveOrUpdate(contract);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除合同")
    @DeleteMapping("/{contractId}")
    public MyJsonBean deleteContract(@PathVariable String contractId) {
        try {
            return contractService.delete(contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除合同")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteContracts(@RequestBody List<String> contractIds) {
        try {
            return contractService.batchDelete(contractIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交合同")
    @PostMapping("/{contractId}/submit")
    public MyJsonBean submitContract(@PathVariable String contractId) {
        try {
            return contractService.submit(contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批合同")
    @PostMapping("/{contractId}/approve")
    public MyJsonBean approveContract(@PathVariable String contractId,
                                     @RequestBody Map<String, String> approvalData) {
        try {
            String action = approvalData.get("action");
            String opinion = approvalData.get("opinion");
            return contractService.approve(contractId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取收付款计划")
    @GetMapping("/{contractId}/payment-plans")
    public MyJsonBean getPaymentPlans(@PathVariable String contractId) {
        try {
            return contractService.getPaymentPlans(contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存收付款计划")
    @PostMapping("/{contractId}/payment-plans")
    public MyJsonBean savePaymentPlans(@PathVariable String contractId,
                                     @RequestBody List<TblContractPaymentPlan> plans) {
        try {
            return contractService.savePaymentPlans(contractId, plans);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出合同")
    @GetMapping("/export")
    public MyJsonBean exportContracts(
            @RequestParam(required = false) String contractCode,
            @RequestParam(required = false) String contractName,
            @RequestParam(required = false) String contractType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("contractCode", contractCode);
            param.put("contractName", contractName);
            param.put("contractType", contractType);
            param.put("status", status);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return contractService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取合同统计")
    @GetMapping("/statistics")
    public MyJsonBean getContractStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return contractService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("合同履约")
    @PostMapping("/{contractId}/fulfill")
    public MyJsonBean fulfillContract(@PathVariable String contractId,
                                      @RequestBody Map<String, Object> fulfillData) {
        try {
            return contractService.fulfill(contractId, fulfillData);
        } catch (Exception e) {
            return MyJsonBean.errorData("履约失败：" + e.getMessage());
        }
    }

    @ApiOperation("付款申请")
    @PostMapping("/{contractId}/payment-request")
    public MyJsonBean paymentRequest(@PathVariable String contractId,
                                     @RequestBody Map<String, Object> paymentData) {
        try {
            return contractService.paymentRequest(contractId, paymentData);
        } catch (Exception e) {
            return MyJsonBean.errorData("付款申请失败：" + e.getMessage());
        }
    }

    @ApiOperation("合同变更")
    @PostMapping("/{contractId}/modify")
    public MyJsonBean modifyContract(@PathVariable String contractId,
                                     @RequestBody Map<String, Object> modifyData) {
        try {
            return contractService.modify(contractId, modifyData);
        } catch (Exception e) {
            return MyJsonBean.errorData("变更失败：" + e.getMessage());
        }
    }

    @ApiOperation("合同终止")
    @PostMapping("/{contractId}/terminate")
    public MyJsonBean terminateContract(@PathVariable String contractId,
                                        @RequestBody Map<String, Object> terminateData) {
        try {
            return contractService.terminate(contractId, terminateData);
        } catch (Exception e) {
            return MyJsonBean.errorData("终止失败：" + e.getMessage());
        }
    }

    @ApiOperation("合同执行分析")
    @GetMapping("/{contractId}/execution-analysis")
    public MyJsonBean getExecutionAnalysis(@PathVariable String contractId) {
        try {
            return contractService.getExecutionAnalysis(contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}
