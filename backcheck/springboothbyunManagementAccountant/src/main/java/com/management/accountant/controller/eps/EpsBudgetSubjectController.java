package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetSubject;
import com.management.accountant.service.eps.EpsBudgetSubjectService;
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
 * 预算科目管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算科目管理")
@RestController
@RequestMapping("/eps/budget-subject")
@Validated
public class EpsBudgetSubjectController {

    @Autowired
    private EpsBudgetSubjectService budgetSubjectService;

    /**
     * 分页查询预算科目
     */
    @ApiOperation("分页查询预算科目")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetSubject>> queryBudgetSubjectPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("科目名称") @RequestParam(required = false) String subjectName,
            @ApiParam("科目编码") @RequestParam(required = false) String subjectCode,
            @ApiParam("科目类型") @RequestParam(required = false) String subjectType,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("父科目ID") @RequestParam(required = false) Long parentSubjectId) {
        try {
            IPage<EpsBudgetSubject> result = budgetSubjectService.queryBudgetSubjectPage(
                    current, size, subjectName, subjectCode, subjectType, systemId, parentSubjectId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询预算科目失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算科目
     */
    @ApiOperation("创建预算科目")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetSubject(
            @ApiParam("预算科目") @RequestBody @Valid EpsBudgetSubject budgetSubject) {
        try {
            boolean result = budgetSubjectService.createBudgetSubject(budgetSubject);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算科目失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算科目
     */
    @ApiOperation("更新预算科目")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetSubject(
            @ApiParam("预算科目") @RequestBody @Valid EpsBudgetSubject budgetSubject) {
        try {
            boolean result = budgetSubjectService.updateBudgetSubject(budgetSubject);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算科目失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算科目
     */
    @ApiOperation("删除预算科目")
    @DeleteMapping("/{subjectId}")
    public MyJsonBean<Boolean> deleteBudgetSubject(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId) {
        try {
            boolean result = budgetSubjectService.deleteBudgetSubject(subjectId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算科目失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算科目
     */
    @ApiOperation("批量删除预算科目")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetSubjects(
            @ApiParam("科目ID列表") @RequestBody List<Long> subjectIds) {
        try {
            boolean result = budgetSubjectService.batchDeleteBudgetSubjects(subjectIds);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算科目失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算科目详情
     */
    @ApiOperation("根据ID查询预算科目详情")
    @GetMapping("/{subjectId}")
    public MyJsonBean<EpsBudgetSubject> getBudgetSubjectById(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId) {
        try {
            EpsBudgetSubject result = budgetSubjectService.getBudgetSubjectById(subjectId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("科目不存在");
            }
        } catch (Exception e) {
            log.error("获取预算科目详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算科目
     */
    @ApiOperation("根据编码查询预算科目")
    @GetMapping("/code/{subjectCode}")
    public MyJsonBean<EpsBudgetSubject> getBudgetSubjectByCode(
            @ApiParam("科目编码") @PathVariable @NotNull String subjectCode) {
        try {
            EpsBudgetSubject result = budgetSubjectService.getBudgetSubjectByCode(subjectCode);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("科目不存在");
            }
        } catch (Exception e) {
            log.error("根据编码获取预算科目失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算科目树
     */
    @ApiOperation("获取预算科目树")
    @GetMapping("/tree")
    public MyJsonBean<List<Map<String, Object>>> getBudgetSubjectTree(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("科目类型") @RequestParam(required = false) String subjectType,
            @ApiParam("是否包含禁用") @RequestParam(defaultValue = "false") Boolean includeDisabled) {
        try {
            List<Map<String, Object>> result = budgetSubjectService.getBudgetSubjectTree(systemId, subjectType, includeDisabled);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算科目树失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算体系查询科目列表
     */
    @ApiOperation("根据预算体系查询科目列表")
    @GetMapping("/system/{systemId}")
    public MyJsonBean<List<EpsBudgetSubject>> getBudgetSubjectsBySystemId(
            @ApiParam("预算体系ID") @PathVariable @NotNull Long systemId) {
        try {
            List<EpsBudgetSubject> result = budgetSubjectService.getBudgetSubjectsBySystemId(systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据预算体系查询科目列表失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据科目类型查询科目列表
     */
    @ApiOperation("根据科目类型查询科目列表")
    @GetMapping("/type/{subjectType}")
    public MyJsonBean<List<EpsBudgetSubject>> getBudgetSubjectsByType(
            @ApiParam("科目类型") @PathVariable @NotNull String subjectType,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetSubject> result = budgetSubjectService.getBudgetSubjectsByType(subjectType, systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据科目类型查询科目列表失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取子科目列表
     */
    @ApiOperation("获取子科目列表")
    @GetMapping("/{parentSubjectId}/children")
    public MyJsonBean<List<EpsBudgetSubject>> getChildBudgetSubjects(
            @ApiParam("父科目ID") @PathVariable @NotNull Long parentSubjectId) {
        try {
            List<EpsBudgetSubject> result = budgetSubjectService.getChildBudgetSubjects(parentSubjectId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取子科目列表失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 移动预算科目
     */
    @ApiOperation("移动预算科目")
    @PostMapping("/{subjectId}/move")
    public MyJsonBean<Boolean> moveBudgetSubject(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId,
            @ApiParam("目标父科目ID") @RequestParam Long targetParentId,
            @ApiParam("目标位置") @RequestParam(required = false) Integer targetPosition) {
        try {
            boolean result = budgetSubjectService.moveBudgetSubject(subjectId, targetParentId, targetPosition);
            if (result) {
                return MyJsonBean.success("移动成功", true);
            } else {
                return MyJsonBean.error("移动失败");
            }
        } catch (Exception e) {
            log.error("移动预算科目失败", e);
            return MyJsonBean.error("移动失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算科目
     */
    @ApiOperation("复制预算科目")
    @PostMapping("/{subjectId}/copy")
    public MyJsonBean<Boolean> copyBudgetSubject(
            @ApiParam("源科目ID") @PathVariable @NotNull Long subjectId,
            @ApiParam("目标科目编码") @RequestParam @NotNull String targetSubjectCode,
            @ApiParam("目标科目名称") @RequestParam @NotNull String targetSubjectName,
            @ApiParam("目标父科目ID") @RequestParam(required = false) Long targetParentId,
            @ApiParam("是否复制子科目") @RequestParam(defaultValue = "false") Boolean copyChildren) {
        try {
            boolean result = budgetSubjectService.copyBudgetSubject(subjectId, targetSubjectCode, 
                    targetSubjectName, targetParentId, copyChildren);
            if (result) {
                return MyJsonBean.success("复制成功", true);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制预算科目失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 启用预算科目
     */
    @ApiOperation("启用预算科目")
    @PostMapping("/{subjectId}/enable")
    public MyJsonBean<Boolean> enableBudgetSubject(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId) {
        try {
            boolean result = budgetSubjectService.enableBudgetSubject(subjectId);
            if (result) {
                return MyJsonBean.success("启用成功", true);
            } else {
                return MyJsonBean.error("启用失败");
            }
        } catch (Exception e) {
            log.error("启用预算科目失败", e);
            return MyJsonBean.error("启用失败：" + e.getMessage());
        }
    }

    /**
     * 禁用预算科目
     */
    @ApiOperation("禁用预算科目")
    @PostMapping("/{subjectId}/disable")
    public MyJsonBean<Boolean> disableBudgetSubject(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId) {
        try {
            boolean result = budgetSubjectService.disableBudgetSubject(subjectId);
            if (result) {
                return MyJsonBean.success("禁用成功", true);
            } else {
                return MyJsonBean.error("禁用失败");
            }
        } catch (Exception e) {
            log.error("禁用预算科目失败", e);
            return MyJsonBean.error("禁用失败：" + e.getMessage());
        }
    }

    /**
     * 获取科目路径
     */
    @ApiOperation("获取科目路径")
    @GetMapping("/{subjectId}/path")
    public MyJsonBean<List<EpsBudgetSubject>> getBudgetSubjectPath(
            @ApiParam("科目ID") @PathVariable @NotNull Long subjectId) {
        try {
            List<EpsBudgetSubject> result = budgetSubjectService.getBudgetSubjectPath(subjectId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取科目路径失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 验证科目编码
     */
    @ApiOperation("验证科目编码")
    @GetMapping("/validate-code")
    public MyJsonBean<Boolean> validateSubjectCode(
            @ApiParam("科目编码") @RequestParam @NotNull String subjectCode,
            @ApiParam("排除的科目ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = budgetSubjectService.checkSubjectCodeExists(subjectCode, excludeId);
            if (exists) {
                return MyJsonBean.error("科目编码已存在");
            } else {
                return MyJsonBean.success("科目编码可用", true);
            }
        } catch (Exception e) {
            log.error("验证科目编码失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 获取科目统计
     */
    @ApiOperation("获取科目统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetSubjectStatistics(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("科目类型") @RequestParam(required = false) String subjectType) {
        try {
            Map<String, Object> result = budgetSubjectService.getBudgetSubjectStatistics(systemId, subjectType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取科目统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 导入预算科目
     */
    @ApiOperation("导入预算科目")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importBudgetSubjects(
            @ApiParam("导入数据") @RequestBody Map<String, Object> importData) {
        try {
            Map<String, Object> result = budgetSubjectService.importBudgetSubjects(importData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导入预算科目失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出预算科目
     */
    @ApiOperation("导出预算科目")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportBudgetSubjects(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = budgetSubjectService.exportBudgetSubjects(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出预算科目失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作科目
     */
    @ApiOperation("批量操作科目")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateSubjects(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = budgetSubjectService.batchOperateSubjects(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作科目失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }
}
