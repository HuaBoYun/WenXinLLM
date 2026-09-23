package com.global.treasurer.controller;

import com.global.treasurer.entity.BankReconciliation;
import com.global.treasurer.service.BankReconciliationService;
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

/**
 * 银行对账控制器
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/cash/reconciliation")
@Api(tags = "银行对账管理")
public class BankReconciliationController {
    @Resource
    private BankReconciliationService bankReconciliationService;

    @Resource
    private UserProvider userProvider;

    /**
     * 根据ID查询银行对账详情
     */
    @GetMapping("/{reconciliationId}")
    @ApiOperation("获取银行对账详情")
    public String getReconciliation(@PathVariable Long reconciliationId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            BankReconciliation reconciliation = bankReconciliationService.getReconciliationById(reconciliationId);
            return JsonBean.success(reconciliation);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询银行对账列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询银行对账列表")
    public String getReconciliationPage(@RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = bankReconciliationService.getReconciliationPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建银行对账单
     */
    @PostMapping
    @ApiOperation("创建银行对账单")
    public String createReconciliation(@FlexibleRequestBody BankReconciliation reconciliation) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = bankReconciliationService.createReconciliation(reconciliation);
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
     * 更新银行对账单
     */
    @PutMapping
    @ApiOperation("更新银行对账单")
    public String updateReconciliation(@FlexibleRequestBody BankReconciliation reconciliation) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = bankReconciliationService.updateReconciliation(reconciliation);
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
     * 批量删除银行对账单
     */
    @DeleteMapping
    @ApiOperation("批量删除银行对账单")
    public String deleteReconciliation(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = bankReconciliationService.batchDelete(ids);
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
     * 执行银行对账
     */
    @PutMapping("/execute")
    @ApiOperation("执行银行对账")
    public String executeReconciliation(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long reconciliationId = Long.parseLong(params.get("reconciliationId").toString());
            int result = bankReconciliationService.executeReconciliation(reconciliationId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("对账成功");
            } else {
                return new JsonBean(0, "对账失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "对账失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取对账概览统计(GET方式,兼容前端请求)
     */
    @GetMapping("/overview")
    @ApiOperation("获取对账概览统计")
    public String getReconciliationOverviewGet(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = bankReconciliationService.getReconciliationOverview(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取对账概览统计(POST方式)
     */
    @PostMapping("/overview")
    @ApiOperation("获取对账概览统计")
    public String getReconciliationOverviewPost(@RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = bankReconciliationService.getReconciliationOverview(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
