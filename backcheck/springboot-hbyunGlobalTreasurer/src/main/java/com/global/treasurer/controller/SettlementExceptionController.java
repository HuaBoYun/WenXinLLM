package com.global.treasurer.controller;

import com.global.treasurer.entity.TblSettlementException;
import com.global.treasurer.service.SettlementExceptionService;
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
@RequestMapping({"/settlement/exception"})
@Api(tags = "结算异常管理")
public class SettlementExceptionController {
    @Resource
    private SettlementExceptionService exceptionService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询结算异常")
    public String getExceptionPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = exceptionService.getExceptionPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询结算异常")
    public String getExceptionById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblSettlementException exception = exceptionService.getExceptionById(id);
            if (exception == null) {
                return new JsonBean(0, "异常不存在", null).toJson();
            }
            return JsonBean.success(exception);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增结算异常")
    public String addException(@FlexibleRequestBody TblSettlementException exception) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = exceptionService.createException(exception);
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
    @ApiOperation("修改结算异常")
    public String updateException(@FlexibleRequestBody TblSettlementException exception) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = exceptionService.updateException(exception);
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
    @ApiOperation("删除结算异常")
    public String deleteException(@PathVariable String ids) {
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
            int result = exceptionService.deleteException(idList);
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

    @PutMapping("/assign")
    @ApiOperation("分配异常")
    public String assignException(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Long exceptionId = Long.parseLong(params.get("exceptionId").toString());
            String assignee = (String) params.get("assignee");
            int result = exceptionService.assignException(exceptionId, assignee);
            if (result > 0) {
                return JsonBean.success("分配成功");
            } else {
                return new JsonBean(0, "分配失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "分配失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/resolve")
    @ApiOperation("解决异常")
    public String resolveException(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Long exceptionId = Long.parseLong(params.get("exceptionId").toString());
            String resolveNotes = (String) params.get("resolveNotes");
            int result = exceptionService.resolveException(exceptionId, resolveNotes);
            if (result > 0) {
                return JsonBean.success("解决成功");
            } else {
                return new JsonBean(0, "解决失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "解决失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/escalate")
    @ApiOperation("升级异常")
    public String escalateException(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Long exceptionId = Long.parseLong(params.get("exceptionId").toString());
            String escalateTo = (String) params.get("escalateTo");
            int result = exceptionService.escalateException(exceptionId, escalateTo);
            if (result > 0) {
                return JsonBean.success("升级成功");
            } else {
                return new JsonBean(0, "升级失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "升级失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/unresolved")
    @ApiOperation("查询未解决异常")
    public String getUnresolvedExceptions(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblSettlementException> list = exceptionService.getUnresolvedExceptions(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/critical")
    @ApiOperation("查询严重异常")
    public String getCriticalExceptions(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果前端没有传递orgId,从用户信息中获取
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            List<TblSettlementException> list = exceptionService.getCriticalExceptions(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/summary")
    @ApiOperation("统计异常概要")
    public String getExceptionSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> summary = exceptionService.getExceptionSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/trend")
    @ApiOperation("异常趋势分析")
    public String getExceptionTrend(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Map<String, Object>> trend = exceptionService.getExceptionTrend(params);
            return JsonBean.success(trend);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/recurring-patterns")
    @ApiOperation("重复异常模式分析")
    public String getRecurringPatterns(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Map<String, Object>> patterns = exceptionService.getRecurringPatterns(params);
            return JsonBean.success(patterns);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
