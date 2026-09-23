package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.common.ResultCode;
import com.management.accountant.entity.eps.EpsBudgetSystem;
import com.management.accountant.service.eps.EpsBudgetSystemService;
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

/**
 * 预算体系管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算体系管理")
@RestController
@RequestMapping("/eps/budget-system")
@Validated
public class EpsBudgetSystemController {

    @Autowired
    private EpsBudgetSystemService budgetSystemService;

    /**
     * 分页查询预算体系
     */
    @ApiOperation("分页查询预算体系")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetSystem>> queryBudgetSystemPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("体系名称") @RequestParam(required = false) String systemName,
            @ApiParam("体系类型") @RequestParam(required = false) String systemType,
            @ApiParam("预算年度") @RequestParam(required = false) Integer fiscalYear,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        try {
            IPage<EpsBudgetSystem> result = budgetSystemService.queryBudgetSystemPage(
                    current, size, systemName, systemType, fiscalYear, organizationId, status);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询预算体系失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算体系
     */
    @ApiOperation("创建预算体系")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetSystem(@Valid @RequestBody EpsBudgetSystem budgetSystem) {
        try {
            boolean result = budgetSystemService.createBudgetSystem(budgetSystem);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算体系失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算体系
     */
    @ApiOperation("更新预算体系")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetSystem(@Valid @RequestBody EpsBudgetSystem budgetSystem) {
        try {
            boolean result = budgetSystemService.updateBudgetSystem(budgetSystem);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算体系失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算体系
     */
    @ApiOperation("删除预算体系")
    @DeleteMapping("/{systemId}")
    public MyJsonBean<Boolean> deleteBudgetSystem(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId) {
        try {
            boolean result = budgetSystemService.deleteBudgetSystem(systemId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算体系失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算体系
     */
    @ApiOperation("批量删除预算体系")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetSystem(@RequestBody List<Long> systemIds) {
        try {
            boolean result = budgetSystemService.batchDeleteBudgetSystem(systemIds);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算体系失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算体系详情
     */
    @ApiOperation("根据ID查询预算体系详情")
    @GetMapping("/{systemId}")
    public MyJsonBean<EpsBudgetSystem> getBudgetSystemById(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId) {
        try {
            EpsBudgetSystem result = budgetSystemService.getBudgetSystemById(systemId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("查询预算体系详情失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据体系编码查询预算体系
     */
    @ApiOperation("根据体系编码查询预算体系")
    @GetMapping("/code/{systemCode}")
    public MyJsonBean<EpsBudgetSystem> getBudgetSystemByCode(
            @ApiParam("体系编码") @PathVariable String systemCode) {
        try {
            EpsBudgetSystem result = budgetSystemService.getBudgetSystemByCode(systemCode);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("根据编码查询预算体系失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据组织ID查询预算体系列表
     */
    @ApiOperation("根据组织ID查询预算体系列表")
    @GetMapping("/organization/{organizationId}")
    public MyJsonBean<List<EpsBudgetSystem>> getBudgetSystemsByOrganization(
            @ApiParam("组织ID") @PathVariable @NotNull Long organizationId) {
        try {
            List<EpsBudgetSystem> result = budgetSystemService.getBudgetSystemsByOrganization(organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据组织查询预算体系失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询默认预算体系
     */
    @ApiOperation("查询默认预算体系")
    @GetMapping("/default/{organizationId}")
    public MyJsonBean<EpsBudgetSystem> getDefaultBudgetSystem(
            @ApiParam("组织ID") @PathVariable @NotNull Long organizationId) {
        try {
            EpsBudgetSystem result = budgetSystemService.getDefaultBudgetSystem(organizationId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("查询默认预算体系失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 设置默认预算体系
     */
    @ApiOperation("设置默认预算体系")
    @PutMapping("/default/{systemId}/{organizationId}")
    public MyJsonBean<Boolean> setDefaultBudgetSystem(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId,
            @ApiParam("组织ID") @PathVariable @NotNull Long organizationId) {
        try {
            boolean result = budgetSystemService.setDefaultBudgetSystem(systemId, organizationId);
            if (result) {
                return MyJsonBean.success("设置默认体系成功", true);
            } else {
                return MyJsonBean.error("设置默认体系失败");
            }
        } catch (Exception e) {
            log.error("设置默认预算体系失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 激活预算体系
     */
    @ApiOperation("激活预算体系")
    @PutMapping("/activate/{systemId}")
    public MyJsonBean<Boolean> activateBudgetSystem(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId) {
        try {
            boolean result = budgetSystemService.activateBudgetSystem(systemId);
            if (result) {
                return MyJsonBean.success("激活成功", true);
            } else {
                return MyJsonBean.error("激活失败");
            }
        } catch (Exception e) {
            log.error("激活预算体系失败", e);
            return MyJsonBean.error("激活失败：" + e.getMessage());
        }
    }

    /**
     * 停用预算体系
     */
    @ApiOperation("停用预算体系")
    @PutMapping("/deactivate/{systemId}")
    public MyJsonBean<Boolean> deactivateBudgetSystem(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId) {
        try {
            boolean result = budgetSystemService.deactivateBudgetSystem(systemId);
            if (result) {
                return MyJsonBean.success("停用成功", true);
            } else {
                return MyJsonBean.error("停用失败");
            }
        } catch (Exception e) {
            log.error("停用预算体系失败", e);
            return MyJsonBean.error("停用失败：" + e.getMessage());
        }
    }

    /**
     * 归档预算体系
     */
    @ApiOperation("归档预算体系")
    @PutMapping("/archive/{systemId}")
    public MyJsonBean<Boolean> archiveBudgetSystem(
            @ApiParam("体系ID") @PathVariable @NotNull Long systemId) {
        try {
            boolean result = budgetSystemService.archiveBudgetSystem(systemId);
            if (result) {
                return MyJsonBean.success("归档成功", true);
            } else {
                return MyJsonBean.error("归档失败");
            }
        } catch (Exception e) {
            log.error("归档预算体系失败", e);
            return MyJsonBean.error("归档失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算体系
     */
    @ApiOperation("复制预算体系")
    @PostMapping("/copy")
    public MyJsonBean<Boolean> copyBudgetSystem(
            @ApiParam("源体系ID") @RequestParam @NotNull Long sourceSystemId,
            @ApiParam("目标体系编码") @RequestParam String targetSystemCode,
            @ApiParam("目标体系名称") @RequestParam String targetSystemName,
            @ApiParam("目标预算年度") @RequestParam Integer targetFiscalYear) {
        try {
            boolean result = budgetSystemService.copyBudgetSystem(
                    sourceSystemId, targetSystemCode, targetSystemName, targetFiscalYear);
            if (result) {
                return MyJsonBean.success("复制成功", true);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制预算体系失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 检查体系编码是否存在
     */
    @ApiOperation("检查体系编码是否存在")
    @GetMapping("/check-code")
    public MyJsonBean<Boolean> checkSystemCodeExists(
            @ApiParam("体系编码") @RequestParam String systemCode,
            @ApiParam("排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = budgetSystemService.checkSystemCodeExists(systemCode, excludeId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查体系编码失败", e);
            return MyJsonBean.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新状态
     */
    @ApiOperation("批量更新状态")
    @PutMapping("/batch-status")
    public MyJsonBean<Boolean> batchUpdateStatus(
            @RequestBody List<Long> systemIds,
            @ApiParam("状态") @RequestParam String status,
            @ApiParam("更新人ID") @RequestParam Long updatedBy) {
        try {
            boolean result = budgetSystemService.batchUpdateStatus(systemIds, status, updatedBy);
            if (result) {
                return MyJsonBean.success("批量更新状态成功", true);
            } else {
                return MyJsonBean.error("批量更新状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            return MyJsonBean.error("批量更新失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询预算体系
     */
    @ApiOperation("根据预算年度查询预算体系")
    @GetMapping("/fiscal-year/{fiscalYear}")
    public MyJsonBean<List<EpsBudgetSystem>> getBudgetSystemsByFiscalYear(
            @ApiParam("预算年度") @PathVariable @NotNull Integer fiscalYear,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            List<EpsBudgetSystem> result = budgetSystemService.getBudgetSystemsByFiscalYear(fiscalYear, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据预算年度查询预算体系失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
