package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.ncv65.BudgetVersion;
import com.management.accountant.service.ncv65.IBudgetVersionService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算版本控制器
 * 
 * @description 预算版本管理API接口，支持版本控制和历史追溯
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算版本管理")
@RestController
@RequestMapping("/budget/version")
@Validated
public class BudgetVersionController {

    @Resource
    private IBudgetVersionService budgetVersionService;

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算版本
     */
    @ApiOperation("创建预算版本")
    @PostMapping
    public MyJsonBean<String> createBudgetVersion(@Valid @RequestBody BudgetVersion version) {
        try {
            boolean result = budgetVersionService.createBudgetVersion(version);
            if (result) {
                return MyJsonBean.successData(version.getId(),  "预算版本创建成功");
            } else {
                return MyJsonBean.error("预算版本创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("创建预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算版本
     */
    @ApiOperation("更新预算版本")
    @PutMapping("/{id}")
    public MyJsonBean<Boolean> updateBudgetVersion(
            @ApiParam("版本ID") @PathVariable String id,
            @Valid @RequestBody BudgetVersion version) {
        try {
            version.setId(id);
            boolean result = budgetVersionService.updateBudgetVersion(version);
            if (result) {
                return MyJsonBean.successData(true,  "预算版本更新成功");
            } else {
                return MyJsonBean.error("预算版本更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("更新预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算版本
     */
    @ApiOperation("删除预算版本")
    @DeleteMapping("/{id}")
    public MyJsonBean<Boolean> deleteBudgetVersion(@ApiParam("版本ID") @PathVariable String id) {
        try {
            boolean result = budgetVersionService.deleteBudgetVersion(id);
            if (result) {
                return MyJsonBean.successData(true,  "预算版本删除成功");
            } else {
                return MyJsonBean.error("预算版本删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("删除预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算版本
     */
    @ApiOperation("批量删除预算版本")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetVersions(@RequestBody List<String> ids) {
        try {
            boolean result = budgetVersionService.batchDeleteBudgetVersions(ids);
            if (result) {
                return MyJsonBean.successData(true,  "批量删除预算版本成功");
            } else {
                return MyJsonBean.error("批量删除预算版本失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("批量删除预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算版本
     */
    @ApiOperation("根据ID查询预算版本")
    @GetMapping("/{id}")
    public MyJsonBean<BudgetVersion> getBudgetVersionById(@ApiParam("版本ID") @PathVariable String id) {
        try {
            BudgetVersion version = budgetVersionService.getBudgetVersionById(id);
            return MyJsonBean.successData(version,  "查询成功");
        } catch (Exception e) {
            log.error("查询预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算版本
     */
    @ApiOperation("根据编码查询预算版本")
    @GetMapping("/code/{versionCode}")
    public MyJsonBean<BudgetVersion> getBudgetVersionByCode(@ApiParam("版本编码") @PathVariable String versionCode) {
        try {
            BudgetVersion version = budgetVersionService.getBudgetVersionByCode(versionCode);
            return MyJsonBean.successData(version,  "查询成功");
        } catch (Exception e) {
            log.error("根据编码查询预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据编码查询预算版本失败：" + e.getMessage());
        }
    }

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算版本
     */
    @ApiOperation("分页查询预算版本")
    @PostMapping("/page")
    public MyJsonBean<IPage<BudgetVersion>> getBudgetVersionPage(@RequestBody Map<String, Object> params) {
        try {
            Integer current = (Integer) params.getOrDefault("current", 1);
            Integer size = (Integer) params.getOrDefault("size", 10);
            IPage<BudgetVersion> page = budgetVersionService.getBudgetVersionPage(current, size, params);
            return MyJsonBean.successData(page,  "查询成功");
        } catch (Exception e) {
            log.error("分页查询预算版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("分页查询预算版本失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算年度查询版本列表
     */
    @ApiOperation("根据预算年度查询版本列表")
    @GetMapping("/fiscal-year/{fiscalYear}")
    public MyJsonBean<List<BudgetVersion>> getBudgetVersionsByFiscalYear(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getBudgetVersionsByFiscalYear(fiscalYear);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("根据预算年度查询版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算年度查询版本列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算模型ID查询版本列表
     */
    @ApiOperation("根据预算模型ID查询版本列表")
    @GetMapping("/model/{modelId}")
    public MyJsonBean<List<BudgetVersion>> getBudgetVersionsByModelId(@ApiParam("预算模型ID") @PathVariable String modelId) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getBudgetVersionsByModelId(modelId);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("根据预算模型ID查询版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据预算模型ID查询版本列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据版本类型查询版本列表
     */
    @ApiOperation("根据版本类型查询版本列表")
    @GetMapping("/type/{versionType}")
    public MyJsonBean<List<BudgetVersion>> getBudgetVersionsByType(@ApiParam("版本类型") @PathVariable String versionType) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getBudgetVersionsByType(versionType);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("根据版本类型查询版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("根据版本类型查询版本列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询当前版本
     */
    @ApiOperation("查询当前版本")
    @GetMapping("/current")
    public MyJsonBean<BudgetVersion> getCurrentVersion(
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("预算模型ID") @RequestParam String modelId) {
        try {
            BudgetVersion version = budgetVersionService.getCurrentVersion(fiscalYear, modelId);
            return MyJsonBean.successData(version,  "查询成功");
        } catch (Exception e) {
            log.error("查询当前版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询当前版本失败：" + e.getMessage());
        }
    }

    /**
     * 查询基准版本
     */
    @ApiOperation("查询基准版本")
    @GetMapping("/baseline")
    public MyJsonBean<BudgetVersion> getBaselineVersion(
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("预算模型ID") @RequestParam String modelId) {
        try {
            BudgetVersion version = budgetVersionService.getBaselineVersion(fiscalYear, modelId);
            return MyJsonBean.successData(version,  "查询成功");
        } catch (Exception e) {
            log.error("查询基准版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询基准版本失败：" + e.getMessage());
        }
    }

    /**
     * 查询最终版本
     */
    @ApiOperation("查询最终版本")
    @GetMapping("/final")
    public MyJsonBean<BudgetVersion> getFinalVersion(
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("预算模型ID") @RequestParam String modelId) {
        try {
            BudgetVersion version = budgetVersionService.getFinalVersion(fiscalYear, modelId);
            return MyJsonBean.successData(version,  "查询成功");
        } catch (Exception e) {
            log.error("查询最终版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询最终版本失败：" + e.getMessage());
        }
    }

    /**
     * 查询已发布的版本列表
     */
    @ApiOperation("查询已发布的版本列表")
    @GetMapping("/published/{fiscalYear}")
    public MyJsonBean<List<BudgetVersion>> getPublishedVersions(@ApiParam("预算年度") @PathVariable Integer fiscalYear) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getPublishedVersions(fiscalYear);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("查询已发布的版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询已发布的版本列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询版本历史记录
     */
    @ApiOperation("查询版本历史记录")
    @GetMapping("/history/{versionCode}")
    public MyJsonBean<List<BudgetVersion>> getVersionHistory(@ApiParam("版本编码") @PathVariable String versionCode) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getVersionHistory(versionCode);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("查询版本历史记录失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询版本历史记录失败：" + e.getMessage());
        }
    }

    /**
     * 查询我创建的版本列表
     */
    @ApiOperation("查询我创建的版本列表")
    @GetMapping("/my")
    public MyJsonBean<List<BudgetVersion>> getMyVersions(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getMyVersions(userId);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("查询我创建的版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询我创建的版本列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待审批的版本列表
     */
    @ApiOperation("查询待审批的版本列表")
    @GetMapping("/pending")
    public MyJsonBean<List<BudgetVersion>> getPendingApprovalVersions(@ApiParam("用户ID") @RequestParam String userId) {
        try {
            List<BudgetVersion> versions = budgetVersionService.getPendingApprovalVersions(userId);
            return MyJsonBean.successData(versions,  "查询成功");
        } catch (Exception e) {
            log.error("查询待审批的版本列表失败：{}", e.getMessage(), e);
            return MyJsonBean.error("查询待审批的版本列表失败：" + e.getMessage());
        }
    }

    // ==================== 业务操作 ====================

    /**
     * 发布版本
     */
    @ApiOperation("发布版本")
    @PostMapping("/{id}/publish")
    public MyJsonBean<Boolean> publishVersion(@ApiParam("版本ID") @PathVariable String id) {
        try {
            boolean result = budgetVersionService.publishVersion(id);
            if (result) {
                return MyJsonBean.successData(true,  "版本发布成功");
            } else {
                return MyJsonBean.error("版本发布失败");
            }
        } catch (Exception e) {
            log.error("发布版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("发布版本失败：" + e.getMessage());
        }
    }

    /**
     * 取消发布版本
     */
    @ApiOperation("取消发布版本")
    @PostMapping("/{id}/unpublish")
    public MyJsonBean<Boolean> unpublishVersion(@ApiParam("版本ID") @PathVariable String id) {
        try {
            boolean result = budgetVersionService.unpublishVersion(id);
            if (result) {
                return MyJsonBean.successData(true,  "取消发布版本成功");
            } else {
                return MyJsonBean.error("取消发布版本失败");
            }
        } catch (Exception e) {
            log.error("取消发布版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("取消发布版本失败：" + e.getMessage());
        }
    }

    /**
     * 锁定版本
     */
    @ApiOperation("锁定版本")
    @PostMapping("/{id}/lock")
    public MyJsonBean<Boolean> lockVersion(
            @ApiParam("版本ID") @PathVariable String id,
            @ApiParam("锁定原因") @RequestParam String lockReason) {
        try {
            boolean result = budgetVersionService.lockVersion(id, lockReason);
            if (result) {
                return MyJsonBean.successData(true,  "版本锁定成功");
            } else {
                return MyJsonBean.error("版本锁定失败");
            }
        } catch (Exception e) {
            log.error("锁定版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("锁定版本失败：" + e.getMessage());
        }
    }

    /**
     * 解锁版本
     */
    @ApiOperation("解锁版本")
    @PostMapping("/{id}/unlock")
    public MyJsonBean<Boolean> unlockVersion(@ApiParam("版本ID") @PathVariable String id) {
        try {
            boolean result = budgetVersionService.unlockVersion(id);
            if (result) {
                return MyJsonBean.successData(true,  "版本解锁成功");
            } else {
                return MyJsonBean.error("版本解锁失败");
            }
        } catch (Exception e) {
            log.error("解锁版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("解锁版本失败：" + e.getMessage());
        }
    }

    /**
     * 设置当前版本
     */
    @ApiOperation("设置当前版本")
    @PostMapping("/{id}/set-current")
    public MyJsonBean<Boolean> setCurrentVersion(
            @ApiParam("版本ID") @PathVariable String id,
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("预算模型ID") @RequestParam String modelId) {
        try {
            boolean result = budgetVersionService.setCurrentVersion(id, fiscalYear, modelId);
            if (result) {
                return MyJsonBean.successData(true,  "设置当前版本成功");
            } else {
                return MyJsonBean.error("设置当前版本失败");
            }
        } catch (Exception e) {
            log.error("设置当前版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("设置当前版本失败：" + e.getMessage());
        }
    }

    /**
     * 设置基准版本
     */
    @ApiOperation("设置基准版本")
    @PostMapping("/{id}/set-baseline")
    public MyJsonBean<Boolean> setBaselineVersion(
            @ApiParam("版本ID") @PathVariable String id,
            @ApiParam("预算年度") @RequestParam Integer fiscalYear,
            @ApiParam("预算模型ID") @RequestParam String modelId) {
        try {
            boolean result = budgetVersionService.setBaselineVersion(id, fiscalYear, modelId);
            if (result) {
                return MyJsonBean.successData(true,  "设置基准版本成功");
            } else {
                return MyJsonBean.error("设置基准版本失败");
            }
        } catch (Exception e) {
            log.error("设置基准版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("设置基准版本失败：" + e.getMessage());
        }
    }

    /**
     * 归档版本
     */
    @ApiOperation("归档版本")
    @PostMapping("/{id}/archive")
    public MyJsonBean<Boolean> archiveVersion(@ApiParam("版本ID") @PathVariable String id) {
        try {
            boolean result = budgetVersionService.archiveVersion(id);
            if (result) {
                return MyJsonBean.successData(true,  "版本归档成功");
            } else {
                return MyJsonBean.error("版本归档失败");
            }
        } catch (Exception e) {
            log.error("归档版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("归档版本失败：" + e.getMessage());
        }
    }

    /**
     * 复制版本
     */
    @ApiOperation("复制版本")
    @PostMapping("/{id}/copy")
    public MyJsonBean<String> copyVersion(
            @ApiParam("版本ID") @PathVariable String id,
            @ApiParam("新版本编码") @RequestParam String newVersionCode,
            @ApiParam("新版本名称") @RequestParam String newVersionName) {
        try {
            String newVersionId = budgetVersionService.copyVersion(id, newVersionCode, newVersionName);
            if (newVersionId != null) {
                return MyJsonBean.successData(newVersionId,  "版本复制成功");
            } else {
                return MyJsonBean.error("版本复制失败");
            }
        } catch (Exception e) {
            log.error("复制版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("复制版本失败：" + e.getMessage());
        }
    }

    /**
     * 比较版本
     */
    @ApiOperation("比较版本")
    @PostMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareVersions(
            @ApiParam("版本1ID") @RequestParam String version1Id,
            @ApiParam("版本2ID") @RequestParam String version2Id) {
        try {
            Map<String, Object> result = budgetVersionService.compareVersions(version1Id, version2Id);
            return MyJsonBean.successData(result,  "版本比较成功");
        } catch (Exception e) {
            log.error("比较版本失败：{}", e.getMessage(), e);
            return MyJsonBean.error("比较版本失败：" + e.getMessage());
        }
    }

    // ==================== 统计分析 ====================

    /**
     * 获取版本统计信息
     */
    @ApiOperation("获取版本统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetVersionStatistics() {
        try {
            Map<String, Object> statistics = budgetVersionService.getBudgetVersionStatistics();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("获取版本统计信息失败：{}", e.getMessage(), e);
            return MyJsonBean.error("获取版本统计信息失败：" + e.getMessage());
        }
    }

    /**
     * 按版本类型统计数量
     */
    @ApiOperation("按版本类型统计数量")
    @GetMapping("/statistics/by-type")
    public MyJsonBean<List<Map<String, Object>>> getBudgetVersionCountByType() {
        try {
            List<Map<String, Object>> statistics = budgetVersionService.getBudgetVersionCountByType();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按版本类型统计数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按版本类型统计数量失败：" + e.getMessage());
        }
    }

    /**
     * 按年度统计版本数量
     */
    @ApiOperation("按年度统计版本数量")
    @GetMapping("/statistics/by-year")
    public MyJsonBean<List<Map<String, Object>>> getBudgetVersionCountByYear() {
        try {
            List<Map<String, Object>> statistics = budgetVersionService.getBudgetVersionCountByYear();
            return MyJsonBean.successData(statistics,  "查询成功");
        } catch (Exception e) {
            log.error("按年度统计版本数量失败：{}", e.getMessage(), e);
            return MyJsonBean.error("按年度统计版本数量失败：" + e.getMessage());
        }
    }

    // ==================== 数据导入导出 ====================

    /**
     * 导出版本数据
     */
    @ApiOperation("导出版本数据")
    @PostMapping("/export")
    public MyJsonBean<String> exportVersions(@RequestBody List<String> versionIds) {
        try {
            String filePath = budgetVersionService.exportVersions(versionIds);
            return MyJsonBean.successData(filePath,  "导出成功");
        } catch (Exception e) {
            log.error("导出版本数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导出版本数据失败：" + e.getMessage());
        }
    }

    /**
     * 导入版本数据
     */
    @ApiOperation("导入版本数据")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importVersions(@ApiParam("文件路径") @RequestParam String filePath) {
        try {
            Map<String, Object> result = budgetVersionService.importVersions(filePath);
            return MyJsonBean.successData(result,  "导入成功");
        } catch (Exception e) {
            log.error("导入版本数据失败：{}", e.getMessage(), e);
            return MyJsonBean.error("导入版本数据失败：" + e.getMessage());
        }
    }
}
