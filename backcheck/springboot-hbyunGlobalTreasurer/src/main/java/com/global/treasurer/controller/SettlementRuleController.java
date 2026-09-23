package com.global.treasurer.controller;

import com.global.treasurer.entity.TblSettlementRule;
import com.global.treasurer.service.SettlementRuleService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settlement/rule")
@Api(tags = "结算规则管理")
public class SettlementRuleController {
    @Resource
    private SettlementRuleService ruleService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询结算规则")
    public String getRulePage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = ruleService.getRulePage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询结算规则")
    public String getRuleById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblSettlementRule rule = ruleService.getRuleById(id);
            if (rule == null) {
                return new JsonBean(0, "规则不存在", null).toJson();
            }
            return JsonBean.success(rule);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增结算规则")
    public String addRule(@FlexibleRequestBody TblSettlementRule rule) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = ruleService.createRule(rule);
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

    @PutMapping("")
    @ApiOperation("修改结算规则")
    public String updateRule(@FlexibleRequestBody TblSettlementRule rule) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = ruleService.updateRule(rule);
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

    @DeleteMapping("/{ids}")
    @ApiOperation("删除结算规则")
    public String deleteRule(@PathVariable String ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            String[] idArray = ids.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) {
                idList.add(Long.parseLong(id));
            }
            int result = ruleService.deleteRule(idList);
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

    @PutMapping("/enable")
    @ApiOperation("启用规则")
    public String enableRule(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            @SuppressWarnings("unchecked")
            List<Long> ruleIds = (List<Long>) params.get("ruleIds");
            int result = ruleService.enableRule(ruleIds);
            if (result > 0) {
                return JsonBean.success("启用成功");
            } else {
                return new JsonBean(0, "启用失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "启用失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/disable")
    @ApiOperation("禁用规则")
    public String disableRule(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            @SuppressWarnings("unchecked")
            List<Long> ruleIds = (List<Long>) params.get("ruleIds");
            int result = ruleService.disableRule(ruleIds);
            if (result > 0) {
                return JsonBean.success("禁用成功");
            } else {
                return new JsonBean(0, "禁用失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "禁用失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/applicable")
    @ApiOperation("查询适用规则")
    public String getApplicableRules(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblSettlementRule> list = ruleService.getApplicableRules(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/test")
    @ApiOperation("测试规则")
    public String testRule(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = ruleService.testRule(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/execution-stats")
    @ApiOperation("规则执行统计")
    public String getRuleExecutionStats(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> stats = ruleService.getRuleExecutionStats(orgId);
            return JsonBean.success(stats);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/conflict-detection")
    @ApiOperation("规则冲突检测")
    public String detectRuleConflicts(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Map<String, Object>> conflicts = ruleService.detectRuleConflicts(orgId);
            return JsonBean.success(conflicts);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
