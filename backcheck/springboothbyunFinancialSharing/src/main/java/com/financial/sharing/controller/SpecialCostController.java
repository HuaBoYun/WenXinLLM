package com.financial.sharing.controller;

import com.financial.sharing.service.SpecialCostService;
import com.financial.sharing.util.MyResponseFormat;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.SpecialCostParam;
import com.financial.sharing.vo.result.SpecialCostResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 专项成本管理控制器
 * 
 * @author system
 * @date 2024-12-19
 */
@Api(tags = "专项成本管理")
@RestController
@RequestMapping("/ma/specialcost")
public class SpecialCostController {

    @Autowired
    private SpecialCostService specialCostService;

    // ==================== 专项成本统计 ====================

    @ApiOperation("获取专项成本统计概览")
    @GetMapping("/stats")
    public MyResponseFormat getSpecialCostStats() {
        try {
            Map<String, Object> stats = specialCostService.getSpecialCostStats();
            return MyResponseFormat.success(stats);
        } catch (Exception e) {
            return MyResponseFormat.error("获取统计数据失败: " + e.getMessage());
        }
    }

    // ==================== 项目成本 ====================

    @ApiOperation("分页查询项目成本列表")
    @PostMapping("/project/getList")
    public MyResponseFormat<PageResult<SpecialCostResult.ProjectCost>> getProjectCostPage(
            @RequestBody @Valid PageableParam<SpecialCostParam.ProjectCostQuery> param) {
        try {
            PageResult<SpecialCostResult.ProjectCost> result = specialCostService.getProjectCostPage(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("查询项目成本列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新项目成本")
    @PostMapping("/project/saveOrUpdate")
    public MyResponseFormat<String> saveOrUpdateProjectCost(
            @RequestBody @Valid SpecialCostParam.ProjectCostSave param) {
        try {
            specialCostService.saveOrUpdateProjectCost(param);
            return MyResponseFormat.success("操作成功");
        } catch (Exception e) {
            return MyResponseFormat.error("保存项目成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目成本详情")
    @GetMapping("/project/{projectId}")
    public MyResponseFormat<SpecialCostResult.ProjectCostDetail> getProjectCostById(
            @ApiParam("项目ID") @PathVariable Long projectId) {
        try {
            SpecialCostResult.ProjectCostDetail result = specialCostService.getProjectCostById(projectId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取项目成本详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除项目成本")
    @DeleteMapping("/project/{projectId}")
    public MyResponseFormat<String> deleteProjectCost(
            @ApiParam("项目ID") @PathVariable Long projectId) {
        try {
            specialCostService.deleteProjectCost(projectId);
            return MyResponseFormat.success("删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("删除项目成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除项目成本")
    @DeleteMapping("/project/batchDelete")
    public MyResponseFormat<String> batchDeleteProjectCost(
            @RequestBody List<Long> projectIds) {
        try {
            specialCostService.batchDeleteProjectCost(projectIds);
            return MyResponseFormat.success("批量删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("批量删除项目成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取项目成本分析数据")
    @GetMapping("/project/analysis")
    public MyResponseFormat<SpecialCostResult.ProjectCostAnalysis> getProjectCostAnalysis(
            @RequestParam Long projectId) {
        try {
            SpecialCostResult.ProjectCostAnalysis result = specialCostService.getProjectCostAnalysis(projectId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取项目成本分析失败: " + e.getMessage());
        }
    }

    // ==================== 作业成本 ====================

    @ApiOperation("分页查询作业成本列表")
    @PostMapping("/activity/getList")
    public MyResponseFormat<PageResult<SpecialCostResult.ActivityCost>> getActivityCostPage(
            @RequestBody @Valid PageableParam<SpecialCostParam.ActivityCostQuery> param) {
        try {
            PageResult<SpecialCostResult.ActivityCost> result = specialCostService.getActivityCostPage(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("查询作业成本列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新作业成本")
    @PostMapping("/activity/saveOrUpdate")
    public MyResponseFormat<String> saveOrUpdateActivityCost(
            @RequestBody @Valid SpecialCostParam.ActivityCostSave param) {
        try {
            specialCostService.saveOrUpdateActivityCost(param);
            return MyResponseFormat.success("操作成功");
        } catch (Exception e) {
            return MyResponseFormat.error("保存作业成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取作业成本详情")
    @GetMapping("/activity/{activityId}")
    public MyResponseFormat<SpecialCostResult.ActivityCostDetail> getActivityCostById(
            @ApiParam("作业ID") @PathVariable Long activityId) {
        try {
            SpecialCostResult.ActivityCostDetail result = specialCostService.getActivityCostById(activityId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取作业成本详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除作业成本")
    @DeleteMapping("/activity/{activityId}")
    public MyResponseFormat<String> deleteActivityCost(
            @ApiParam("作业ID") @PathVariable Long activityId) {
        try {
            specialCostService.deleteActivityCost(activityId);
            return MyResponseFormat.success("删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("删除作业成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取作业成本分配数据")
    @PostMapping("/activity/allocation")
    public MyResponseFormat<String> getActivityCostAllocation(
            @RequestBody SpecialCostParam.ActivityAllocation param) {
        try {
            specialCostService.processActivityCostAllocation(param);
            return MyResponseFormat.success("分配成功");
        } catch (Exception e) {
            return MyResponseFormat.error("作业成本分配失败: " + e.getMessage());
        }
    }

    // ==================== 质量成本 ====================

    @ApiOperation("分页查询质量成本列表")
    @PostMapping("/quality/getList")
    public MyResponseFormat<PageResult<SpecialCostResult.QualityCost>> getQualityCostPage(
            @RequestBody @Valid PageableParam<SpecialCostParam.QualityCostQuery> param) {
        try {
            PageResult<SpecialCostResult.QualityCost> result = specialCostService.getQualityCostPage(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("查询质量成本列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新质量成本")
    @PostMapping("/quality/saveOrUpdate")
    public MyResponseFormat<String> saveOrUpdateQualityCost(
            @RequestBody @Valid SpecialCostParam.QualityCostSave param) {
        try {
            specialCostService.saveOrUpdateQualityCost(param);
            return MyResponseFormat.success("操作成功");
        } catch (Exception e) {
            return MyResponseFormat.error("保存质量成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取质量成本详情")
    @GetMapping("/quality/{qualityId}")
    public MyResponseFormat<SpecialCostResult.QualityCostDetail> getQualityCostById(
            @ApiParam("质量成本ID") @PathVariable Long qualityId) {
        try {
            SpecialCostResult.QualityCostDetail result = specialCostService.getQualityCostById(qualityId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取质量成本详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除质量成本")
    @DeleteMapping("/quality/{qualityId}")
    public MyResponseFormat<String> deleteQualityCost(
            @ApiParam("质量成本ID") @PathVariable Long qualityId) {
        try {
            specialCostService.deleteQualityCost(qualityId);
            return MyResponseFormat.success("删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("删除质量成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取质量成本分类统计")
    @GetMapping("/quality/categoryStats")
    public MyResponseFormat<Map<String, Object>> getQualityCostCategoryStats() {
        try {
            Map<String, Object> stats = specialCostService.getQualityCostCategoryStats();
            return MyResponseFormat.success(stats);
        } catch (Exception e) {
            return MyResponseFormat.error("获取质量成本分类统计失败: " + e.getMessage());
        }
    }

    // ==================== 环境成本 ====================

    @ApiOperation("分页查询环境成本列表")
    @PostMapping("/environment/getList")
    public MyResponseFormat<PageResult<SpecialCostResult.EnvironmentCost>> getEnvironmentCostPage(
            @RequestBody @Valid PageableParam<SpecialCostParam.EnvironmentCostQuery> param) {
        try {
            PageResult<SpecialCostResult.EnvironmentCost> result = specialCostService.getEnvironmentCostPage(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("查询环境成本列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新环境成本")
    @PostMapping("/environment/saveOrUpdate")
    public MyResponseFormat<String> saveOrUpdateEnvironmentCost(
            @RequestBody @Valid SpecialCostParam.EnvironmentCostSave param) {
        try {
            specialCostService.saveOrUpdateEnvironmentCost(param);
            return MyResponseFormat.success("操作成功");
        } catch (Exception e) {
            return MyResponseFormat.error("保存环境成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取环境成本详情")
    @GetMapping("/environment/{environmentId}")
    public MyResponseFormat<SpecialCostResult.EnvironmentCostDetail> getEnvironmentCostById(
            @ApiParam("环境成本ID") @PathVariable Long environmentId) {
        try {
            SpecialCostResult.EnvironmentCostDetail result = specialCostService.getEnvironmentCostById(environmentId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取环境成本详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除环境成本")
    @DeleteMapping("/environment/{environmentId}")
    public MyResponseFormat<String> deleteEnvironmentCost(
            @ApiParam("环境成本ID") @PathVariable Long environmentId) {
        try {
            specialCostService.deleteEnvironmentCost(environmentId);
            return MyResponseFormat.success("删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("删除环境成本失败: " + e.getMessage());
        }
    }

    // ==================== 研发成本 ====================

    @ApiOperation("分页查询研发成本列表")
    @PostMapping("/rd/getList")
    public MyResponseFormat<PageResult<SpecialCostResult.RdCost>> getRdCostPage(
            @RequestBody @Valid PageableParam<SpecialCostParam.RdCostQuery> param) {
        try {
            PageResult<SpecialCostResult.RdCost> result = specialCostService.getRdCostPage(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("查询研发成本列表失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存或更新研发成本")
    @PostMapping("/rd/saveOrUpdate")
    public MyResponseFormat<String> saveOrUpdateRdCost(
            @RequestBody @Valid SpecialCostParam.RdCostSave param) {
        try {
            specialCostService.saveOrUpdateRdCost(param);
            return MyResponseFormat.success("操作成功");
        } catch (Exception e) {
            return MyResponseFormat.error("保存研发成本失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取研发成本详情")
    @GetMapping("/rd/{rdId}")
    public MyResponseFormat<SpecialCostResult.RdCostDetail> getRdCostById(
            @ApiParam("研发成本ID") @PathVariable Long rdId) {
        try {
            SpecialCostResult.RdCostDetail result = specialCostService.getRdCostById(rdId);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取研发成本详情失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除研发成本")
    @DeleteMapping("/rd/{rdId}")
    public MyResponseFormat<String> deleteRdCost(
            @ApiParam("研发成本ID") @PathVariable Long rdId) {
        try {
            specialCostService.deleteRdCost(rdId);
            return MyResponseFormat.success("删除成功");
        } catch (Exception e) {
            return MyResponseFormat.error("删除研发成本失败: " + e.getMessage());
        }
    }

    // ==================== 专项分析 ====================

    @ApiOperation("获取专项成本综合分析数据")
    @PostMapping("/analysis/comprehensive")
    public MyResponseFormat<SpecialCostResult.ComprehensiveAnalysis> getSpecialCostAnalysis(
            @RequestBody SpecialCostParam.AnalysisQuery param) {
        try {
            SpecialCostResult.ComprehensiveAnalysis result = specialCostService.getSpecialCostAnalysis(param);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取专项成本分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取专项成本对比分析数据")
    @GetMapping("/analysis/compare")
    public MyResponseFormat<SpecialCostResult.CompareAnalysis> getSpecialCostCompareAnalysis(
            @RequestParam String startDate,
            @RequestParam String endDate,
            @RequestParam(required = false) String compareType) {
        try {
            SpecialCostResult.CompareAnalysis result = specialCostService.getSpecialCostCompareAnalysis(startDate, endDate, compareType);
            return MyResponseFormat.success(result);
        } catch (Exception e) {
            return MyResponseFormat.error("获取专项成本对比分析失败: " + e.getMessage());
        }
    }
}
