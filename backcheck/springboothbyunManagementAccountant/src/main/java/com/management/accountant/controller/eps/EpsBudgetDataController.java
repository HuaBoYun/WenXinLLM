package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetData;
import com.management.accountant.service.eps.EpsBudgetDataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算数据管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算数据管理")
@RestController
@RequestMapping("/eps/budget-data")
@Validated
public class EpsBudgetDataController {

    @Autowired
    private EpsBudgetDataService budgetDataService;

    /**
     * 分页查询预算数据
     */
    @ApiOperation("分页查询预算数据")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetData>> queryBudgetDataPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("预算版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("预算科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("预算期间") @RequestParam(required = false) String budgetPeriod,
            @ApiParam("数据状态") @RequestParam(required = false) String dataStatus) {
        try {
            IPage<EpsBudgetData> result = budgetDataService.queryBudgetDataPage(
                    current, size, versionId, subjectId, organizationId, budgetPeriod, dataStatus);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询预算数据失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算数据
     */
    @ApiOperation("创建预算数据")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetData(
            @ApiParam("预算数据") @RequestBody @Valid EpsBudgetData budgetData) {
        try {
            boolean result = budgetDataService.createBudgetData(budgetData);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算数据失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算数据
     */
    @ApiOperation("更新预算数据")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetData(
            @ApiParam("预算数据") @RequestBody @Valid EpsBudgetData budgetData) {
        try {
            boolean result = budgetDataService.updateBudgetData(budgetData);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算数据失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算数据
     */
    @ApiOperation("删除预算数据")
    @DeleteMapping("/{dataId}")
    public MyJsonBean<Boolean> deleteBudgetData(
            @ApiParam("数据ID") @PathVariable @NotNull Long dataId) {
        try {
            boolean result = budgetDataService.deleteBudgetData(dataId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算数据失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算数据
     */
    @ApiOperation("批量删除预算数据")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetData(
            @ApiParam("数据ID列表") @RequestBody List<Long> dataIds) {
        try {
            boolean result = budgetDataService.batchDeleteBudgetData(dataIds);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算数据失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算数据详情
     */
    @ApiOperation("根据ID查询预算数据详情")
    @GetMapping("/{dataId}")
    public MyJsonBean<EpsBudgetData> getBudgetDataById(
            @ApiParam("数据ID") @PathVariable @NotNull Long dataId) {
        try {
            EpsBudgetData result = budgetDataService.getBudgetDataById(dataId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("数据不存在");
            }
        } catch (Exception e) {
            log.error("获取预算数据详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据版本查询预算数据
     */
    @ApiOperation("根据版本查询预算数据")
    @GetMapping("/version/{versionId}")
    public MyJsonBean<List<EpsBudgetData>> getBudgetDataByVersionId(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            List<EpsBudgetData> result = budgetDataService.getBudgetDataByVersionId(versionId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据版本查询预算数据失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据科目查询预算数据
     */
    @ApiOperation("根据科目查询预算数据")
    @GetMapping("/subject/{subjectId}")
    public MyJsonBean<List<EpsBudgetData>> getBudgetDataBySubjectId(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId,
            @ApiParam("版本ID") @RequestParam(required = false) Long versionId) {
        try {
            List<EpsBudgetData> result = budgetDataService.getBudgetDataBySubjectId(subjectId, versionId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据科目查询预算数据失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算数据矩阵
     */
    @ApiOperation("获取预算数据矩阵")
    @GetMapping("/matrix")
    public MyJsonBean<Map<String, Object>> getBudgetDataMatrix(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("预算期间") @RequestParam(required = false) String budgetPeriod) {
        try {
            Map<String, Object> result = budgetDataService.getBudgetDataMatrix(versionId, organizationId, budgetPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算数据矩阵失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量保存预算数据
     */
    @ApiOperation("批量保存预算数据")
    @PostMapping("/batch-save")
    public MyJsonBean<Map<String, Object>> batchSaveBudgetData(
            @ApiParam("预算数据列表") @RequestBody List<EpsBudgetData> budgetDataList) {
        try {
            Map<String, Object> result = budgetDataService.batchSaveBudgetData(budgetDataList);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量保存预算数据失败", e);
            return MyJsonBean.error("批量保存失败：" + e.getMessage());
        }
    }

    /**
     * 导入预算数据
     */
    @ApiOperation("导入预算数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importBudgetData(
            @ApiParam("导入数据") @RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = budgetDataService.importBudgetData(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入预算数据失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出预算数据
     */
    @ApiOperation("导出预算数据")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportBudgetData(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = budgetDataService.exportBudgetData(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出预算数据失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 计算预算数据
     */
    @ApiOperation("计算预算数据")
    @PostMapping("/calculate")
    public MyJsonBean<Map<String, Object>> calculateBudgetData(
            @ApiParam("计算参数") @RequestBody Map<String, Object> calculateParams) {
        try {
            Map<String, Object> result = budgetDataService.calculateBudgetData(calculateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("计算预算数据失败", e);
            return MyJsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 验证预算数据
     */
    @ApiOperation("验证预算数据")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateBudgetData(
            @ApiParam("验证参数") @RequestBody Map<String, Object> validateParams) {
        try {
            Map<String, Object> result = budgetDataService.validateBudgetData(validateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("验证预算数据失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 汇总预算数据
     */
    @ApiOperation("汇总预算数据")
    @PostMapping("/summarize")
    public MyJsonBean<Map<String, Object>> summarizeBudgetData(
            @ApiParam("汇总参数") @RequestBody Map<String, Object> summarizeParams) {
        try {
            Map<String, Object> result = budgetDataService.summarizeBudgetData(summarizeParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("汇总预算数据失败", e);
            return MyJsonBean.error("汇总失败：" + e.getMessage());
        }
    }

    /**
     * 分解预算数据
     */
    @ApiOperation("分解预算数据")
    @PostMapping("/decompose")
    public MyJsonBean<Map<String, Object>> decomposeBudgetData(
            @ApiParam("分解参数") @RequestBody Map<String, Object> decomposeParams) {
        try {
            Map<String, Object> result = budgetDataService.decomposeBudgetData(decomposeParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分解预算数据失败", e);
            return MyJsonBean.error("分解失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算数据
     */
    @ApiOperation("复制预算数据")
    @PostMapping("/copy")
    public MyJsonBean<Map<String, Object>> copyBudgetData(
            @ApiParam("复制参数") @RequestBody Map<String, Object> copyParams) {
        try {
            Map<String, Object> result = budgetDataService.copyBudgetData(copyParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("复制预算数据失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 锁定预算数据
     */
    @ApiOperation("锁定预算数据")
    @PostMapping("/{dataId}/lock")
    public MyJsonBean<Boolean> lockBudgetData(
            @ApiParam("数据ID") @PathVariable @NotNull Long dataId,
            @ApiParam("锁定原因") @RequestParam(required = false) String lockReason) {
        try {
            boolean result = budgetDataService.lockBudgetData(dataId, lockReason);
            if (result) {
                return MyJsonBean.success("锁定成功", true);
            } else {
                return MyJsonBean.error("锁定失败");
            }
        } catch (Exception e) {
            log.error("锁定预算数据失败", e);
            return MyJsonBean.error("锁定失败：" + e.getMessage());
        }
    }

    /**
     * 解锁预算数据
     */
    @ApiOperation("解锁预算数据")
    @PostMapping("/{dataId}/unlock")
    public MyJsonBean<Boolean> unlockBudgetData(
            @ApiParam("数据ID") @PathVariable @NotNull Long dataId) {
        try {
            boolean result = budgetDataService.unlockBudgetData(dataId);
            if (result) {
                return MyJsonBean.success("解锁成功", true);
            } else {
                return MyJsonBean.error("解锁失败");
            }
        } catch (Exception e) {
            log.error("解锁预算数据失败", e);
            return MyJsonBean.error("解锁失败：" + e.getMessage());
        }
    }

    /**
     * 提交预算数据
     */
    @ApiOperation("提交预算数据")
    @PostMapping("/submit")
    public MyJsonBean<Map<String, Object>> submitBudgetData(
            @ApiParam("提交参数") @RequestBody Map<String, Object> submitParams) {
        try {
            Map<String, Object> result = budgetDataService.submitBudgetData(submitParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("提交预算数据失败", e);
            return MyJsonBean.error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 审批预算数据
     */
    @ApiOperation("审批预算数据")
    @PostMapping("/approve")
    public MyJsonBean<Map<String, Object>> approveBudgetData(
            @ApiParam("审批参数") @RequestBody Map<String, Object> approveParams) {
        try {
            Map<String, Object> result = budgetDataService.approveBudgetData(approveParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("审批预算数据失败", e);
            return MyJsonBean.error("审批失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算数据统计
     */
    @ApiOperation("获取预算数据统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetDataStatistics(
            @ApiParam("版本ID") @RequestParam(required = false) Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("预算期间") @RequestParam(required = false) String budgetPeriod) {
        try {
            Map<String, Object> result = budgetDataService.getBudgetDataStatistics(versionId, organizationId, budgetPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算数据统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算数据变更历史
     */
    @ApiOperation("获取预算数据变更历史")
    @GetMapping("/{dataId}/history")
    public MyJsonBean<List<Map<String, Object>>> getBudgetDataHistory(
            @ApiParam("数据ID") @PathVariable @NotNull Long dataId) {
        try {
            List<Map<String, Object>> result = budgetDataService.getBudgetDataHistory(dataId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算数据变更历史失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作预算数据
     */
    @ApiOperation("批量操作预算数据")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateBudgetData(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = budgetDataService.batchOperateBudgetData(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作预算数据失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }
}
