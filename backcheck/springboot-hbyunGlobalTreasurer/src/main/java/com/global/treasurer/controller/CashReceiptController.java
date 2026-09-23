package com.global.treasurer.controller;

import com.global.treasurer.entity.CashReceipt;
import com.global.treasurer.service.CashReceiptService;
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
import java.util.List;
import java.util.Map;

/**
 * 收款管理控制器
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/cash/receipt")
@Api(tags = "收款管理")
public class CashReceiptController {
    private static final Logger log = LoggerFactory.getLogger(CashReceiptController.class);

    @Resource
    private CashReceiptService cashReceiptService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询收款单列表(GET方式,兼容前端请求)
     */
    @GetMapping("/page")
    @ApiOperation("分页查询收款单列表")
    public String getReceiptPageGet(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashReceiptService.getReceiptPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询收款单列表(POST方式)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询收款单列表")
    public String getReceiptPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashReceiptService.getReceiptPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询收款单详情
     */
    @GetMapping("/{receiptId}")
    @ApiOperation("获取收款单详情")
    public String getReceipt(@PathVariable Long receiptId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            CashReceipt receipt = cashReceiptService.getReceiptById(receiptId);
            return JsonBean.success(receipt);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建收款单
     */
    @PostMapping
    @ApiOperation("创建收款单")
    public String createReceipt(@FlexibleRequestBody CashReceipt receipt) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashReceiptService.createReceipt(receipt);
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
     * 更新收款单
     */
    @PutMapping
    @ApiOperation("更新收款单")
    public String updateReceipt(@FlexibleRequestBody CashReceipt receipt) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashReceiptService.updateReceipt(receipt);
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
     * 批量删除收款单
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除收款单")
    public String deleteReceipt(javax.servlet.http.HttpServletRequest request) {
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

            int result = cashReceiptService.batchDelete(ids);
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
     * 确认收款单
     */
    @PutMapping("/{receiptId}/confirm")
    @ApiOperation("确认收款单")
    public String confirmReceipt(@PathVariable Long receiptId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = new java.util.ArrayList<>();
            ids.add(receiptId);
            int result = cashReceiptService.batchConfirm(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("确认成功");
            } else {
                return new JsonBean(0, "确认失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("确认收款单失败", e);
            return new JsonBean(0, "确认失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量确认收款
     */
    @PutMapping("/confirm")
    @ApiOperation("批量确认收款")
    public String confirmReceiptBatch(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = cashReceiptService.batchConfirm(ids, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("确认成功");
            } else {
                return new JsonBean(0, "确认失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "确认失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 核销收款单
     */
    @PutMapping("/{receiptId}/verify")
    @ApiOperation("核销收款单")
    public String verifyReceipt(@PathVariable Long receiptId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashReceiptService.verifyReceipt(receiptId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("核销成功");
            } else {
                return new JsonBean(0, "核销失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "核销失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消收款单
     */
    @PutMapping("/{receiptId}/cancel")
    @ApiOperation("取消收款单")
    public String cancelReceipt(@PathVariable Long receiptId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashReceiptService.cancelReceipt(receiptId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("取消成功");
            } else {
                return new JsonBean(0, "取消失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("取消收款单失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出收款单列表
     */
    @GetMapping("/export")
    @ApiOperation("导出收款单列表")
    public void exportReceipts(@RequestParam Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            cashReceiptService.exportReceipts(params, response);
        } catch (Exception e) {
            log.error("导出收款单列表失败", e);
        }
    }

    /**
     * 导出单个收款单
     */
    @GetMapping("/export/{receiptId}")
    @ApiOperation("导出单个收款单")
    public void exportReceiptById(@PathVariable Long receiptId, javax.servlet.http.HttpServletResponse response) {
        try {
            cashReceiptService.exportReceiptById(receiptId, response);
        } catch (Exception e) {
            log.error("导出收款单失败", e);
        }
    }
}
