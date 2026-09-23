package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlan;
import com.global.treasurer.entity.TblFundPlanDetail;
import com.global.treasurer.entity.TblFundPlanTemplate;
import com.global.treasurer.entity.TblFundForecast;
import com.global.treasurer.entity.TblFundPlanAdjustment;
import com.global.treasurer.entity.TblFundPlanAnalysis;
import com.global.treasurer.entity.TblFundPlanExecution;
import com.global.treasurer.service.FundPlanService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/plan")
@Api(tags = "资金计划管理")
public class FundPlanController {
    @Resource
    private FundPlanService fundPlanService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划")
    public String getFundPlanPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = fundPlanService.getFundPlanPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询资金计划
     */
    @GetMapping("/{planId}")
    @ApiOperation("根据ID查询资金计划")
    public String getFundPlan(@PathVariable Long planId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlan plan = fundPlanService.getFundPlanById(planId);
            if (plan == null) {
                return new JsonBean(0, "资金计划不存在", null).toJson();
            }
            return JsonBean.success(plan);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金计划
     */
    @PostMapping("")
    @ApiOperation("创建资金计划")
    public String createFundPlan(@FlexibleRequestBody TblFundPlan fundPlan) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.createFundPlan(fundPlan);
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
     * 更新资金计划
     */
    @PutMapping("")
    @ApiOperation("更新资金计划")
    public String updateFundPlan(@FlexibleRequestBody TblFundPlan fundPlan) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.updateFundPlan(fundPlan);
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
     * 删除资金计划
     */
    @DeleteMapping("/{planId}")
    @ApiOperation("删除资金计划")
    public String deleteFundPlan(@PathVariable Long planId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.deleteFundPlan(planId);
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
     * 提交资金计划
     */
    @PutMapping("/{planId}/submit")
    @ApiOperation("提交资金计划")
    public String submitFundPlan(@PathVariable Long planId, @RequestParam(required = false) Long updateUser) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.submitFundPlan(planId, updateUser);
            if (result > 0) {
                return JsonBean.success("提交成功");
            } else {
                return new JsonBean(0, "提交失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批资金计划
     */
    @PutMapping("/{planId}/approve")
    @ApiOperation("审批资金计划")
    public String approveFundPlan(
            @PathVariable Long planId,
            @RequestParam Boolean approved,
            @RequestParam(required = false) Long approvalUser,
            @RequestParam(required = false) String approvalOpinion) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.approveFundPlan(planId, approvalUser, approved, approvalOpinion);
            if (result > 0) {
                return JsonBean.success("审批成功");
            } else {
                return new JsonBean(0, "审批失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行资金计划
     */
    @PutMapping("/{planId}/execute")
    @ApiOperation("执行资金计划")
    public String executeFundPlan(@PathVariable Long planId, @RequestParam(required = false) Long executeUser) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.executeFundPlan(planId, executeUser);
            if (result > 0) {
                return JsonBean.success("执行成功");
            } else {
                return new JsonBean(0, "执行失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 完成资金计划
     */
    @PutMapping("/{planId}/complete")
    @ApiOperation("完成资金计划")
    public String completeFundPlan(@PathVariable Long planId, @RequestParam(required = false) Long updateUser) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.completeFundPlan(planId, updateUser);
            if (result > 0) {
                return JsonBean.success("完成成功");
            } else {
                return new JsonBean(0, "完成失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "完成失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消资金计划
     */
    @PutMapping("/{planId}/cancel")
    @ApiOperation("取消资金计划")
    public String cancelFundPlan(@PathVariable Long planId, @RequestParam(required = false) Long updateUser) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanService.cancelFundPlan(planId, updateUser);
            if (result > 0) {
                return JsonBean.success("取消成功");
            } else {
                return new JsonBean(0, "取消失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }
}
