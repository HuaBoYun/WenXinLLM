package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetFormula;
import com.management.accountant.service.eps.EpsBudgetFormulaService;
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
 * 预算公式计算控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算公式计算")
@RestController
@RequestMapping("/eps/budget-formula")
@Validated
public class EpsBudgetFormulaController {

    @Autowired
    private EpsBudgetFormulaService budgetFormulaService;

    /**
     * 分页查询预算公式
     */
    @ApiOperation("分页查询预算公式")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetFormula>> queryBudgetFormulaPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("公式名称") @RequestParam(required = false) String formulaName,
            @ApiParam("公式类型") @RequestParam(required = false) String formulaType,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        try {
            IPage<EpsBudgetFormula> result = budgetFormulaService.queryBudgetFormulaPage(
                    current, size, formulaName, formulaType, systemId, status);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询预算公式失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算公式
     */
    @ApiOperation("创建预算公式")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetFormula(@Valid @RequestBody EpsBudgetFormula budgetFormula) {
        try {
            boolean result = budgetFormulaService.createBudgetFormula(budgetFormula);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算公式失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算公式
     */
    @ApiOperation("更新预算公式")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetFormula(@Valid @RequestBody EpsBudgetFormula budgetFormula) {
        try {
            boolean result = budgetFormulaService.updateBudgetFormula(budgetFormula);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算公式失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算公式
     */
    @ApiOperation("删除预算公式")
    @DeleteMapping("/{formulaId}")
    public MyJsonBean<Boolean> deleteBudgetFormula(
            @ApiParam("公式ID") @PathVariable @NotNull Long formulaId) {
        try {
            boolean result = budgetFormulaService.deleteBudgetFormula(formulaId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算公式失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算公式详情
     */
    @ApiOperation("根据ID查询预算公式详情")
    @GetMapping("/{formulaId}")
    public MyJsonBean<EpsBudgetFormula> getBudgetFormulaById(
            @ApiParam("公式ID") @PathVariable @NotNull Long formulaId) {
        try {
            EpsBudgetFormula result = budgetFormulaService.getBudgetFormulaById(formulaId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("未找到指定公式");
            }
        } catch (Exception e) {
            log.error("查询预算公式详情失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 验证公式语法
     */
    @ApiOperation("验证公式语法")
    @PostMapping("/validate")
    public MyJsonBean<Map<String, Object>> validateFormulaExpression(
            @ApiParam("公式表达式") @RequestParam @NotNull String formulaExpression,
            @ApiParam("公式类型") @RequestParam(required = false) String formulaType) {
        try {
            Map<String, Object> result = budgetFormulaService.validateFormulaExpression(formulaExpression, formulaType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("验证公式语法失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 计算公式结果
     */
    @ApiOperation("计算公式结果")
    @PostMapping("/calculate")
    public MyJsonBean<Map<String, Object>> calculateFormulaResult(
            @ApiParam("公式ID") @RequestParam @NotNull Long formulaId,
            @ApiParam("计算参数") @RequestBody Map<String, Object> parameters) {
        try {
            Map<String, Object> result = budgetFormulaService.calculateFormulaResult(formulaId, parameters);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("计算公式结果失败", e);
            return MyJsonBean.error("计算失败：" + e.getMessage());
        }
    }

    /**
     * 批量计算公式
     */
    @ApiOperation("批量计算公式")
    @PostMapping("/batch-calculate")
    public MyJsonBean<List<Map<String, Object>>> batchCalculateFormulas(
            @ApiParam("公式ID列表") @RequestParam List<Long> formulaIds,
            @ApiParam("计算参数") @RequestBody Map<String, Object> parameters) {
        try {
            List<Map<String, Object>> result = budgetFormulaService.batchCalculateFormulas(formulaIds, parameters);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量计算公式失败", e);
            return MyJsonBean.error("批量计算失败：" + e.getMessage());
        }
    }

    /**
     * 测试公式计算
     */
    @ApiOperation("测试公式计算")
    @PostMapping("/test")
    public MyJsonBean<Map<String, Object>> testFormulaCalculation(
            @ApiParam("公式表达式") @RequestParam @NotNull String formulaExpression,
            @ApiParam("测试参数") @RequestBody Map<String, Object> testParameters) {
        try {
            Map<String, Object> result = budgetFormulaService.testFormulaCalculation(formulaExpression, testParameters);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("测试公式计算失败", e);
            return MyJsonBean.error("测试失败：" + e.getMessage());
        }
    }

    /**
     * 获取公式依赖关系
     */
    @ApiOperation("获取公式依赖关系")
    @GetMapping("/dependencies/{formulaId}")
    public MyJsonBean<Map<String, Object>> getFormulaDependencies(
            @ApiParam("公式ID") @PathVariable @NotNull Long formulaId) {
        try {
            Map<String, Object> result = budgetFormulaService.getFormulaDependencies(formulaId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取公式依赖关系失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算体系查询公式
     */
    @ApiOperation("根据预算体系查询公式")
    @GetMapping("/system/{systemId}")
    public MyJsonBean<List<EpsBudgetFormula>> getBudgetFormulasBySystemId(
            @ApiParam("预算体系ID") @PathVariable @NotNull Long systemId) {
        try {
            List<EpsBudgetFormula> result = budgetFormulaService.getBudgetFormulasBySystemId(systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据体系查询预算公式失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据公式类型查询公式
     */
    @ApiOperation("根据公式类型查询公式")
    @GetMapping("/type/{formulaType}")
    public MyJsonBean<List<EpsBudgetFormula>> getBudgetFormulasByType(
            @ApiParam("公式类型") @PathVariable String formulaType,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetFormula> result = budgetFormulaService.getBudgetFormulasByType(formulaType, systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据类型查询预算公式失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 复制公式
     */
    @ApiOperation("复制公式")
    @PostMapping("/copy")
    public MyJsonBean<Boolean> copyBudgetFormula(
            @ApiParam("源公式ID") @RequestParam @NotNull Long sourceFormulaId,
            @ApiParam("目标公式编码") @RequestParam String targetFormulaCode,
            @ApiParam("目标公式名称") @RequestParam String targetFormulaName) {
        try {
            boolean result = budgetFormulaService.copyBudgetFormula(sourceFormulaId, targetFormulaCode, targetFormulaName);
            if (result) {
                return MyJsonBean.success("复制成功", true);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制预算公式失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 激活公式
     */
    @ApiOperation("激活公式")
    @PutMapping("/activate/{formulaId}")
    public MyJsonBean<Boolean> activateBudgetFormula(
            @ApiParam("公式ID") @PathVariable @NotNull Long formulaId) {
        try {
            boolean result = budgetFormulaService.activateBudgetFormula(formulaId);
            if (result) {
                return MyJsonBean.success("激活成功", true);
            } else {
                return MyJsonBean.error("激活失败");
            }
        } catch (Exception e) {
            log.error("激活预算公式失败", e);
            return MyJsonBean.error("激活失败：" + e.getMessage());
        }
    }

    /**
     * 停用公式
     */
    @ApiOperation("停用公式")
    @PutMapping("/deactivate/{formulaId}")
    public MyJsonBean<Boolean> deactivateBudgetFormula(
            @ApiParam("公式ID") @PathVariable @NotNull Long formulaId) {
        try {
            boolean result = budgetFormulaService.deactivateBudgetFormula(formulaId);
            if (result) {
                return MyJsonBean.success("停用成功", true);
            } else {
                return MyJsonBean.error("停用失败");
            }
        } catch (Exception e) {
            log.error("停用预算公式失败", e);
            return MyJsonBean.error("停用失败：" + e.getMessage());
        }
    }

    /**
     * 获取公式变量列表
     */
    @ApiOperation("获取公式变量列表")
    @GetMapping("/variables")
    public MyJsonBean<List<Map<String, Object>>> getFormulaVariables(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("变量类型") @RequestParam(required = false) String variableType) {
        try {
            List<Map<String, Object>> result = budgetFormulaService.getFormulaVariables(systemId, variableType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取公式变量列表失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取公式函数列表
     */
    @ApiOperation("获取公式函数列表")
    @GetMapping("/functions")
    public MyJsonBean<List<Map<String, Object>>> getFormulaFunctions(
            @ApiParam("函数分类") @RequestParam(required = false) String functionCategory) {
        try {
            List<Map<String, Object>> result = budgetFormulaService.getFormulaFunctions(functionCategory);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取公式函数列表失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 导出公式配置
     */
    @ApiOperation("导出公式配置")
    @GetMapping("/export")
    public MyJsonBean<String> exportFormulaConfig(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("公式ID列表") @RequestParam(required = false) List<Long> formulaIds) {
        try {
            String result = budgetFormulaService.exportFormulaConfig(systemId, formulaIds);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出公式配置失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 导入公式配置
     */
    @ApiOperation("导入公式配置")
    @PostMapping("/import")
    public MyJsonBean<Boolean> importFormulaConfig(
            @ApiParam("公式配置数据") @RequestParam String formulaConfigData,
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId) {
        try {
            boolean result = budgetFormulaService.importFormulaConfig(formulaConfigData, systemId);
            if (result) {
                return MyJsonBean.success("导入成功", true);
            } else {
                return MyJsonBean.error("导入失败");
            }
        } catch (Exception e) {
            log.error("导入公式配置失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }
}
