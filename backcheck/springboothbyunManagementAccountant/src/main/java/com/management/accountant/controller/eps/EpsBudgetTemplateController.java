package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.common.ResultCode;
import com.management.accountant.entity.eps.EpsBudgetTemplate;
import com.management.accountant.service.eps.EpsBudgetTemplateService;
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
 * 预算模板管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算模板管理")
@RestController
@RequestMapping("/eps/budget-template")
@Validated
public class EpsBudgetTemplateController {

    @Autowired
    private EpsBudgetTemplateService budgetTemplateService;

    /**
     * 分页查询预算模板
     */
    @ApiOperation("分页查询预算模板")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetTemplate>> queryBudgetTemplatePage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("模板名称") @RequestParam(required = false) String templateName,
            @ApiParam("模板类型") @RequestParam(required = false) String templateType,
            @ApiParam("模板分类") @RequestParam(required = false) String templateCategory,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("状态") @RequestParam(required = false) String status) {
        try {
            IPage<EpsBudgetTemplate> result = budgetTemplateService.queryBudgetTemplatePage(
                    current, size, templateName, templateType, templateCategory, systemId, status);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("分页查询预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算模板
     */
    @ApiOperation("创建预算模板")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetTemplate(@Valid @RequestBody EpsBudgetTemplate budgetTemplate) {
        try {
            boolean result = budgetTemplateService.createBudgetTemplate(budgetTemplate);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算模板失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算模板
     */
    @ApiOperation("更新预算模板")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetTemplate(@Valid @RequestBody EpsBudgetTemplate budgetTemplate) {
        try {
            boolean result = budgetTemplateService.updateBudgetTemplate(budgetTemplate);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算模板失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算模板
     */
    @ApiOperation("删除预算模板")
    @DeleteMapping("/{templateId}")
    public MyJsonBean<Boolean> deleteBudgetTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            boolean result = budgetTemplateService.deleteBudgetTemplate(templateId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算模板失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算模板
     */
    @ApiOperation("批量删除预算模板")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetTemplate(@RequestBody List<Long> templateIds) {
        try {
            boolean result = budgetTemplateService.batchDeleteBudgetTemplate(templateIds);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算模板失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算模板详情
     */
    @ApiOperation("根据ID查询预算模板详情")
    @GetMapping("/{templateId}")
    public MyJsonBean<EpsBudgetTemplate> getBudgetTemplateById(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            EpsBudgetTemplate result = budgetTemplateService.getBudgetTemplateById(templateId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("查询预算模板详情失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据模板编码查询预算模板
     */
    @ApiOperation("根据模板编码查询预算模板")
    @GetMapping("/code/{templateCode}")
    public MyJsonBean<EpsBudgetTemplate> getBudgetTemplateByCode(
            @ApiParam("模板编码") @PathVariable String templateCode) {
        try {
            EpsBudgetTemplate result = budgetTemplateService.getBudgetTemplateByCode(templateCode);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("根据编码查询预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算体系ID查询模板列表
     */
    @ApiOperation("根据预算体系ID查询模板列表")
    @GetMapping("/system/{systemId}")
    public MyJsonBean<List<EpsBudgetTemplate>> getBudgetTemplatesBySystemId(
            @ApiParam("预算体系ID") @PathVariable @NotNull Long systemId) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getBudgetTemplatesBySystemId(systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据体系查询预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询默认模板
     */
    @ApiOperation("查询默认模板")
    @GetMapping("/default")
    public MyJsonBean<EpsBudgetTemplate> getDefaultBudgetTemplate(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("模板分类") @RequestParam String templateCategory) {
        try {
            EpsBudgetTemplate result = budgetTemplateService.getDefaultBudgetTemplate(systemId, templateCategory);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.result(ResultCode.DATA_NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("查询默认预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 设置默认模板
     */
    @ApiOperation("设置默认模板")
    @PutMapping("/default/{templateId}")
    public MyJsonBean<Boolean> setDefaultBudgetTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId,
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("模板分类") @RequestParam String templateCategory) {
        try {
            boolean result = budgetTemplateService.setDefaultBudgetTemplate(templateId, systemId, templateCategory);
            if (result) {
                return MyJsonBean.success("设置默认模板成功", true);
            } else {
                return MyJsonBean.error("设置默认模板失败");
            }
        } catch (Exception e) {
            log.error("设置默认预算模板失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 激活预算模板
     */
    @ApiOperation("激活预算模板")
    @PutMapping("/activate/{templateId}")
    public MyJsonBean<Boolean> activateBudgetTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            boolean result = budgetTemplateService.activateBudgetTemplate(templateId);
            if (result) {
                return MyJsonBean.success("激活成功", true);
            } else {
                return MyJsonBean.error("激活失败");
            }
        } catch (Exception e) {
            log.error("激活预算模板失败", e);
            return MyJsonBean.error("激活失败：" + e.getMessage());
        }
    }

    /**
     * 停用预算模板
     */
    @ApiOperation("停用预算模板")
    @PutMapping("/deactivate/{templateId}")
    public MyJsonBean<Boolean> deactivateBudgetTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            boolean result = budgetTemplateService.deactivateBudgetTemplate(templateId);
            if (result) {
                return MyJsonBean.success("停用成功", true);
            } else {
                return MyJsonBean.error("停用失败");
            }
        } catch (Exception e) {
            log.error("停用预算模板失败", e);
            return MyJsonBean.error("停用失败：" + e.getMessage());
        }
    }

    /**
     * 归档预算模板
     */
    @ApiOperation("归档预算模板")
    @PutMapping("/archive/{templateId}")
    public MyJsonBean<Boolean> archiveBudgetTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            boolean result = budgetTemplateService.archiveBudgetTemplate(templateId);
            if (result) {
                return MyJsonBean.success("归档成功", true);
            } else {
                return MyJsonBean.error("归档失败");
            }
        } catch (Exception e) {
            log.error("归档预算模板失败", e);
            return MyJsonBean.error("归档失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算模板
     */
    @ApiOperation("复制预算模板")
    @PostMapping("/copy")
    public MyJsonBean<Boolean> copyBudgetTemplate(
            @ApiParam("源模板ID") @RequestParam @NotNull Long sourceTemplateId,
            @ApiParam("目标模板编码") @RequestParam String targetTemplateCode,
            @ApiParam("目标模板名称") @RequestParam String targetTemplateName) {
        try {
            boolean result = budgetTemplateService.copyBudgetTemplate(
                    sourceTemplateId, targetTemplateCode, targetTemplateName);
            if (result) {
                return MyJsonBean.success("复制成功", true);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制预算模板失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 检查模板编码是否存在
     */
    @ApiOperation("检查模板编码是否存在")
    @GetMapping("/check-code")
    public MyJsonBean<Boolean> checkTemplateCodeExists(
            @ApiParam("模板编码") @RequestParam String templateCode,
            @ApiParam("排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = budgetTemplateService.checkTemplateCodeExists(templateCode, excludeId);
            return MyJsonBean.success(exists);
        } catch (Exception e) {
            log.error("检查模板编码失败", e);
            return MyJsonBean.error("检查失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新状态
     */
    @ApiOperation("批量更新状态")
    @PutMapping("/batch-status")
    public MyJsonBean<Boolean> batchUpdateStatus(
            @RequestBody List<Long> templateIds,
            @ApiParam("状态") @RequestParam String status,
            @ApiParam("更新人ID") @RequestParam Long updatedBy) {
        try {
            boolean result = budgetTemplateService.batchUpdateStatus(templateIds, status, updatedBy);
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
     * 根据模板类型查询模板
     */
    @ApiOperation("根据模板类型查询模板")
    @GetMapping("/type/{templateType}")
    public MyJsonBean<List<EpsBudgetTemplate>> getBudgetTemplatesByType(
            @ApiParam("模板类型") @PathVariable String templateType,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getBudgetTemplatesByType(templateType, systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据类型查询预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据模板分类查询模板
     */
    @ApiOperation("根据模板分类查询模板")
    @GetMapping("/category/{templateCategory}")
    public MyJsonBean<List<EpsBudgetTemplate>> getBudgetTemplatesByCategory(
            @ApiParam("模板分类") @PathVariable String templateCategory,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getBudgetTemplatesByCategory(templateCategory, systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据分类查询预算模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询热门模板
     */
    @ApiOperation("查询热门模板")
    @GetMapping("/popular")
    public MyJsonBean<List<EpsBudgetTemplate>> getPopularTemplates(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getPopularTemplates(systemId, limit);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询热门模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询最近使用的模板
     */
    @ApiOperation("查询最近使用的模板")
    @GetMapping("/recent")
    public MyJsonBean<List<EpsBudgetTemplate>> getRecentlyUsedTemplates(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("用户ID") @RequestParam Long userId,
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getRecentlyUsedTemplates(systemId, userId, limit);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询最近使用模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据标签查询模板
     */
    @ApiOperation("根据标签查询模板")
    @GetMapping("/tags")
    public MyJsonBean<List<EpsBudgetTemplate>> getBudgetTemplatesByTags(
            @ApiParam("标签列表") @RequestParam List<String> tags,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getBudgetTemplatesByTags(tags, systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据标签查询模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询共享模板
     */
    @ApiOperation("查询共享模板")
    @GetMapping("/shared")
    public MyJsonBean<List<EpsBudgetTemplate>> getSharedTemplates(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId) {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getSharedTemplates(systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询共享模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询系统模板
     */
    @ApiOperation("查询系统模板")
    @GetMapping("/system")
    public MyJsonBean<List<EpsBudgetTemplate>> getSystemTemplates() {
        try {
            List<EpsBudgetTemplate> result = budgetTemplateService.getSystemTemplates();
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询系统模板失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 导入模板
     */
    @ApiOperation("导入模板")
    @PostMapping("/import")
    public MyJsonBean<Boolean> importTemplate(
            @ApiParam("模板数据") @RequestParam String templateData,
            @ApiParam("预算体系ID") @RequestParam Long systemId) {
        try {
            boolean result = budgetTemplateService.importTemplate(templateData, systemId);
            if (result) {
                return MyJsonBean.success("导入成功", true);
            } else {
                return MyJsonBean.error("导入失败");
            }
        } catch (Exception e) {
            log.error("导入模板失败", e);
            return MyJsonBean.error("导入失败：" + e.getMessage());
        }
    }

    /**
     * 导出模板
     */
    @ApiOperation("导出模板")
    @GetMapping("/export/{templateId}")
    public MyJsonBean<String> exportTemplate(
            @ApiParam("模板ID") @PathVariable @NotNull Long templateId) {
        try {
            String result = budgetTemplateService.exportTemplate(templateId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出模板失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 验证模板结构
     */
    @ApiOperation("验证模板结构")
    @PostMapping("/validate")
    public MyJsonBean<Boolean> validateTemplateStructure(
            @ApiParam("模板结构") @RequestParam String templateStructure) {
        try {
            boolean result = budgetTemplateService.validateTemplateStructure(templateStructure);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("验证模板结构失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }
}
