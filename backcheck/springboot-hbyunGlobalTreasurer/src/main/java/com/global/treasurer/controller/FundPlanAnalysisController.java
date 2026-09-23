package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlanAnalysis;
import com.global.treasurer.mapper.FundPlanAnalysisMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划分析控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/plan-analysis")
@Api(tags = "资金计划分析管理")
public class FundPlanAnalysisController {
    @Resource
    private FundPlanAnalysisMapper fundPlanAnalysisMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划分析
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划分析")
    public String getFundPlanAnalysisPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);

            List<TblFundPlanAnalysis> list = fundPlanAnalysisMapper.selectAnalysisPage(params);
            int total = fundPlanAnalysisMapper.countAnalysisList(params);

            Map<String, Object> result = new HashMap<>();
            result.put("records", list);
            result.put("total", total);
            result.put("current", page);
            result.put("size", limit);

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取分析汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取分析汇总信息")
    public String getFundPlanAnalysisSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> summary = fundPlanAnalysisMapper.selectAnalysisSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金计划分析
     */
    @PostMapping("")
    @ApiOperation("创建资金计划分析")
    public String createFundPlanAnalysis(@FlexibleRequestBody TblFundPlanAnalysis analysis) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            analysis.setCreatedBy(loginStaff.getStaffid().longValue());
            analysis.setCreatedByName(loginStaff.getRealname());
            analysis.setCreatedTime(new Date());
            analysis.setDeleteFlag(0);
            // PLAN_ID 非空约束，独立创建时默认为 0
            if (analysis.getPlanId() == null) {
                analysis.setPlanId(0L);
            }
            if (analysis.getOrgId() == null) {
                analysis.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            int result = fundPlanAnalysisMapper.insertAnalysis(analysis);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新资金计划分析
     */
    @PutMapping("")
    @ApiOperation("更新资金计划分析")
    public String updateFundPlanAnalysis(@FlexibleRequestBody TblFundPlanAnalysis analysis) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            analysis.setUpdatedBy(loginStaff.getStaffid().longValue());
            analysis.setUpdatedByName(loginStaff.getRealname());
            analysis.setUpdatedTime(new Date());
            int result = fundPlanAnalysisMapper.updateAnalysis(analysis);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 验证资金计划分析
     */
    @PutMapping("/validate")
    @ApiOperation("验证资金计划分析")
    public String validateFundPlanAnalysis(@FlexibleRequestBody TblFundPlanAnalysis analysis) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            analysis.setIsValid(1);
            analysis.setUpdatedBy(loginStaff.getStaffid().longValue());
            analysis.setUpdatedByName(loginStaff.getRealname());
            analysis.setUpdatedTime(new Date());
            int result = fundPlanAnalysisMapper.updateAnalysis(analysis);
            if (result > 0) {
                return JsonBean.success("验证成功");
            } else {
                return new JsonBean(0, "验证失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "验证失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除资金计划分析
     */
    @DeleteMapping("/{analysisId}")
    @ApiOperation("删除资金计划分析")
    public String deleteFundPlanAnalysis(@PathVariable Long analysisId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanAnalysisMapper.deleteById(analysisId);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }
}
