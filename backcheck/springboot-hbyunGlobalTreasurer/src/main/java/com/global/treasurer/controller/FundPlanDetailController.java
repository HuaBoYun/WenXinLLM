package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlanDetail;
import com.global.treasurer.mapper.FundPlanDetailMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 资金计划明细控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/plan-detail")
@Api(tags = "资金计划明细管理")
public class FundPlanDetailController {
    @Resource
    private FundPlanDetailMapper fundPlanDetailMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划明细
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划明细")
    public String getFundPlanDetailPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);

            List<TblFundPlanDetail> list = fundPlanDetailMapper.selectDetailPage(params);
            int total = fundPlanDetailMapper.countDetailList(params);

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
     * 根据计划ID查询明细列表
     */
    @GetMapping("/plan/{planId}")
    @ApiOperation("根据计划ID查询明细列表")
    public String getFundPlanDetailsByPlan(@PathVariable Long planId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TblFundPlanDetail> list = fundPlanDetailMapper.selectByPlanId(planId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金计划明细
     */
    @PostMapping("")
    @ApiOperation("创建资金计划明细")
    public String createFundPlanDetail(@FlexibleRequestBody TblFundPlanDetail detail) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // planId 非空校验
            if (detail.getPlanId() == null) {
                return new JsonBean(0, "创建失败: 计划ID(planId)不能为空，请从资金计划列表进入明细页面", null).toJson();
            }

            // 设置默认值
            if (detail.getExecutionStatus() == null || detail.getExecutionStatus().isEmpty()) {
                detail.setExecutionStatus("PENDING");
            }
            detail.setDeleteFlag(0);
            detail.setCreatedTime(new Date());
            detail.setCreatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            detail.setCreatedByName(loginStaff.getRealname() != null ? loginStaff.getRealname() : loginStaff.getUsername());

            int result = fundPlanDetailMapper.insert(detail);
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
     * 更新资金计划明细
     */
    @PutMapping("")
    @ApiOperation("更新资金计划明细")
    public String updateFundPlanDetail(@FlexibleRequestBody TblFundPlanDetail detail) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            detail.setUpdatedTime(new Date());
            detail.setUpdatedBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            detail.setUpdatedByName(loginStaff.getRealname() != null ? loginStaff.getRealname() : loginStaff.getUsername());

            int result = fundPlanDetailMapper.updateById(detail);
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
     * 删除资金计划明细
     */
    @DeleteMapping("/{detailId}")
    @ApiOperation("删除资金计划明细")
    public String deleteFundPlanDetail(@PathVariable Long detailId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanDetailMapper.logicDeleteById(detailId);
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

    /**
     * 获取明细汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取明细汇总信息")
    public String getFundPlanDetailSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> summary = fundPlanDetailMapper.selectDetailSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取明细差异分析
     */
    @GetMapping("/variance-analysis")
    @ApiOperation("获取明细差异分析")
    public String getFundPlanDetailVarianceAnalysis(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TblFundPlanDetail> list = fundPlanDetailMapper.selectVarianceAnalysis(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取明细执行分析
     */
    @GetMapping("/execution-analysis")
    @ApiOperation("获取明细执行分析")
    public String getFundPlanDetailExecutionAnalysis(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 按执行状态统计
            List<Map<String, Object>> statusStatsList = fundPlanDetailMapper.selectExecutionStatusStats(params);
            Map<String, Object> statusStats = new HashMap<>();
            for (Map<String, Object> item : statusStatsList) {
                statusStats.put(String.valueOf(item.get("executionStatus")), item.get("cnt"));
            }

            // 按业务类型统计
            List<Map<String, Object>> typeStatsList = fundPlanDetailMapper.selectTypeStats(params);
            Map<String, Object> typeStats = new HashMap<>();
            for (Map<String, Object> item : typeStatsList) {
                Map<String, Object> typeInfo = new HashMap<>();
                typeInfo.put("count", item.get("cnt"));
                typeInfo.put("plannedAmount", item.get("plannedAmount"));
                typeInfo.put("actualAmount", item.get("actualAmount"));
                typeStats.put(String.valueOf(item.get("businessType")), typeInfo);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("statusStats", statusStats);
            result.put("typeStats", typeStats);

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行明细
     */
    @PutMapping("/{detailId}/execute")
    @ApiOperation("执行明细")
    public String executeFundPlanDetail(@PathVariable Long detailId,
                                        @FlexibleRequestBody Map<String, Object> body) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String actualDate = body.get("actualDate") != null ? body.get("actualDate").toString() : null;
            BigDecimal actualAmount = body.get("actualAmount") != null
                    ? new BigDecimal(body.get("actualAmount").toString()) : null;

            if (actualDate == null || actualAmount == null) {
                return new JsonBean(0, "实际日期和实际金额不能为空", null).toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("detailId", detailId);
            params.put("executionStatus", "EXECUTED");
            params.put("actualDate", actualDate);
            params.put("actualAmount", actualAmount);

            int result = fundPlanDetailMapper.updateExecutionData(params);
            if (result > 0) {
                return JsonBean.success("执行成功");
            } else {
                return new JsonBean(0, "执行失败，明细不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 调整明细
     */
    @PutMapping("/{detailId}/adjust")
    @ApiOperation("调整明细")
    public String adjustFundPlanDetail(@PathVariable Long detailId,
                                       @FlexibleRequestBody Map<String, Object> body) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            BigDecimal plannedAmount = body.get("plannedAmount") != null
                    ? new BigDecimal(body.get("plannedAmount").toString()) : null;
            String plannedDate = body.get("plannedDate") != null ? body.get("plannedDate").toString() : null;
            String description = body.get("remark") != null ? body.get("remark").toString() : "";

            if (plannedAmount == null || plannedDate == null) {
                return new JsonBean(0, "调整后金额和日期不能为空", null).toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("detailId", detailId);
            params.put("plannedAmount", plannedAmount);
            params.put("plannedDate", plannedDate);
            params.put("description", description);

            int result = fundPlanDetailMapper.adjustDetail(params);
            if (result > 0) {
                return JsonBean.success("调整成功");
            } else {
                return new JsonBean(0, "调整失败，明细不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "调整失败: " + e.getMessage(), null).toJson();
        }
    }
}
