package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlanAdjustment;
import com.global.treasurer.mapper.FundPlanAdjustmentMapper;
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
 * 资金计划调整控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/plan-adjustment")
@Api(tags = "资金计划调整管理")
public class FundPlanAdjustmentController {
    @Resource
    private FundPlanAdjustmentMapper fundPlanAdjustmentMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划调整
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划调整")
    public String getFundPlanAdjustmentPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);
            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                params.put("orgId", loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<TblFundPlanAdjustment> list = fundPlanAdjustmentMapper.selectAdjustmentPage(params);
            int total = fundPlanAdjustmentMapper.countAdjustmentList(params);

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
     * 创建资金计划调整
     */
    @PostMapping("")
    @ApiOperation("创建资金计划调整")
    public String createFundPlanAdjustment(@FlexibleRequestBody TblFundPlanAdjustment adjustment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            adjustment.setAdjustmentId(null);
            adjustment.setDeleteFlag(0);
            adjustment.setCreatedTime(new Date());
            adjustment.setUpdatedTime(new Date());
            if (adjustment.getApprovalStatus() == null || adjustment.getApprovalStatus().isEmpty()) {
                adjustment.setApprovalStatus("PENDING");
            }
            adjustment.setAdjustmentNo("ADJ" + System.currentTimeMillis());
            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                adjustment.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            int result = fundPlanAdjustmentMapper.insert(adjustment);
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
     * 更新资金计划调整
     */
    @PutMapping("")
    @ApiOperation("更新资金计划调整")
    public String updateFundPlanAdjustment(@FlexibleRequestBody TblFundPlanAdjustment adjustment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanAdjustmentMapper.updateById(adjustment);
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
     * 删除资金计划调整
     */
    @DeleteMapping("/{adjustmentId}")
    @ApiOperation("删除资金计划调整")
    public String deleteFundPlanAdjustment(@PathVariable Long adjustmentId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanAdjustmentMapper.deleteById(adjustmentId);
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
     * 获取调整汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取调整汇总信息")
    public String getFundPlanAdjustmentSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                params.put("orgId", loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            Map<String, Object> raw = fundPlanAdjustmentMapper.selectAdjustmentSummary(params);
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", raw.getOrDefault("TOTALCOUNT", raw.getOrDefault("totalCount", 0)));
            summary.put("pendingCount", raw.getOrDefault("PENDINGCOUNT", raw.getOrDefault("pendingCount", 0)));
            summary.put("approvedCount", raw.getOrDefault("APPROVEDCOUNT", raw.getOrDefault("approvedCount", 0)));
            summary.put("rejectedCount", raw.getOrDefault("REJECTEDCOUNT", raw.getOrDefault("rejectedCount", 0)));
            summary.put("totalAdjustmentAmount", raw.getOrDefault("TOTALADJUSTMENTAMOUNT", raw.getOrDefault("totalAdjustmentAmount", 0)));
            summary.put("approvedAmount", raw.getOrDefault("APPROVEDAMOUNT", raw.getOrDefault("approvedAmount", 0)));
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批资金计划调整
     */
    @PutMapping("/{adjustmentId}/approve")
    @ApiOperation("审批资金计划调整")
    public String approveFundPlanAdjustment(
            @PathVariable Long adjustmentId,
            @RequestParam Boolean approved,
            @RequestParam String opinion) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanAdjustment adjustment = fundPlanAdjustmentMapper.selectById(adjustmentId);
            if (adjustment != null) {
                adjustment.setApprovalStatus(approved ? "APPROVED" : "REJECTED");
                adjustment.setApproveComment(opinion);
                fundPlanAdjustmentMapper.updateById(adjustment);
                return JsonBean.success("审批成功");
            } else {
                return new JsonBean(0, "调整记录不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }
}
