package com.global.treasurer.controller;

import com.global.treasurer.entity.CashPayment;
import com.global.treasurer.service.CashPaymentService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * 付款管理控制器
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/cash/payment")
@Api(tags = "付款管理")
public class CashPaymentController {
    private static final Logger log = LoggerFactory.getLogger(CashPaymentController.class);

    @Resource
    private CashPaymentService cashPaymentService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询付款单列表(GET方式,兼容前端请求)
     */
    @GetMapping("/page")
    @ApiOperation("分页查询付款单列表")
    public String getPaymentPageGet(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashPaymentService.getPaymentPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询付款单列表(POST方式)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询付款单列表")
    public String getPaymentPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashPaymentService.getPaymentPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询付款单详情
     */
    @GetMapping("/{paymentId}")
    @ApiOperation("获取付款单详情")
    public String getPayment(@PathVariable Long paymentId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            CashPayment payment = cashPaymentService.getPaymentById(paymentId);
            return JsonBean.success(payment);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建付款单
     */
    @PostMapping
    @ApiOperation("创建付款单")
    public String createPayment(@FlexibleRequestBody CashPayment payment) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashPaymentService.createPayment(payment);
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
     * 更新付款单
     */
    @PutMapping
    @ApiOperation("更新付款单")
    public String updatePayment(@FlexibleRequestBody CashPayment payment) {
        try {
            log.info("========== 收到更新付款单请求 ==========");
            log.info("paymentId: {}", payment.getPaymentId());
            log.info("paymentNo: {}", payment.getPaymentNo());
            log.info("accountId: {}", payment.getAccountId());
            log.info("accountName: {}", payment.getAccountName());
            log.info("payeeName: {}", payment.getPayeeName());
            log.info("paymentAmount: {}", payment.getPaymentAmount());
            log.info("paymentDate: {}", payment.getPaymentDate());
            log.info("完整对象: {}", payment);
            log.info("=========================================");

            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashPaymentService.updatePayment(payment);
            log.info("更新付款单结果: result={}", result);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新付款单失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除付款单
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除付款单")
    public String deletePayment(javax.servlet.http.HttpServletRequest request) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 从请求体中读取JSON数据
            StringBuilder sb = new StringBuilder();
            String line;
            try (java.io.BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            // 使用Jackson解析JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            Map<String, Object> params = mapper.readValue(sb.toString(), Map.class);

            // 获取ids数组，支持String和Long类型
            Object idsObj = params.get("ids");
            List<Long> ids = new java.util.ArrayList<>();

            if (idsObj instanceof List) {
                List<?> idsList = (List<?>) idsObj;
                for (Object id : idsList) {
                    if (id instanceof String) {
                        ids.add(Long.parseLong((String) id));
                    } else if (id instanceof Number) {
                        ids.add(((Number) id).longValue());
                    }
                }
            }

            log.info("[DEBUG-删除] 接收到的ids: {}", ids);

            int result = cashPaymentService.batchDelete(ids);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除付款单失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 提交付款单
     */
    @PutMapping("/{paymentId}/submit")
    @ApiOperation("提交付款单")
    public String submitPayment(@PathVariable Long paymentId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = new java.util.ArrayList<>();
            ids.add(paymentId);
            int result = cashPaymentService.batchSubmit(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("提交成功");
            } else {
                return new JsonBean(0, "提交失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("提交付款单失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行付款
     */
    @PutMapping("/{paymentId}/execute")
    @ApiOperation("执行付款")
    public String executePayment(@PathVariable Long paymentId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = new java.util.ArrayList<>();
            ids.add(paymentId);
            int result = cashPaymentService.batchExecute(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("执行成功");
            } else {
                return new JsonBean(0, "执行失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("执行付款失败", e);
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量提交审批
     */
    @PutMapping("/submit")
    @ApiOperation("批量提交审批")
    public String submitPaymentBatch(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = cashPaymentService.batchSubmit(ids, loginStaff.getRealname());
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
     * 批量执行付款
     */
    @PutMapping("/execute")
    @ApiOperation("批量执行付款")
    public String executePaymentBatch(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = cashPaymentService.batchExecute(ids, loginStaff.getRealname());
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
     * 取消付款单
     */
    @PutMapping("/{paymentId}/cancel")
    @ApiOperation("取消付款单")
    public String cancelPayment(@PathVariable Long paymentId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashPaymentService.cancelPayment(paymentId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("取消成功");
            } else {
                return new JsonBean(0, "取消失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("取消付款单失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 拒绝付款单
     */
    @PutMapping("/{paymentId}/reject")
    @ApiOperation("拒绝付款单")
    public String rejectPayment(@PathVariable Long paymentId, @RequestParam String rejectUser, @RequestParam String rejectReason) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashPaymentService.rejectPayment(paymentId, rejectUser, rejectReason);
            if (result > 0) {
                return JsonBean.success("拒绝成功");
            } else {
                return new JsonBean(0, "拒绝失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "拒绝失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出付款单列表
     */
    @GetMapping("/export")
    @ApiOperation("导出付款单列表")
    public void exportPayments(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            log.info("[DEBUG-导出] 导出付款单列表，参数: {}", params);
            cashPaymentService.exportPayments(params, response);
        } catch (Exception e) {
            log.error("导出付款单列表失败", e);
            try {
                response.sendError(500, "导出失败: " + e.getMessage());
            } catch (IOException ex) {
                log.error("发送错误响应失败", ex);
            }
        }
    }

    /**
     * 导出单个付款单
     */
    @GetMapping("/export/{paymentId}")
    @ApiOperation("导出单个付款单")
    public void exportPaymentById(@PathVariable Long paymentId, HttpServletResponse response) {
        try {
            log.info("[DEBUG-导出] 导出单个付款单，ID: {}", paymentId);
            cashPaymentService.exportPaymentById(paymentId, response);
        } catch (Exception e) {
            log.error("导出单个付款单失败", e);
            try {
                response.sendError(500, "导出失败: " + e.getMessage());
            } catch (IOException ex) {
                log.error("发送错误响应失败", ex);
            }
        }
    }
}
