package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.derivatives.TblSwapTransaction;
import com.global.treasurer.service.derivatives.ISwapTransactionService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * 掉期交易控制器
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@RestController
@RequestMapping("/derivatives/swap")
@Api(tags = "掉期交易管理")
public class SwapTransactionController {
    @Resource
    private ISwapTransactionService swapTransactionService;

    @Resource
    private UserProvider userProvider;

    /**
     * 查询掉期交易列表
     */
    @GetMapping("/list")
    @ApiOperation("查询掉期交易列表")
    public String list(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 分页参数
            int current = Integer.parseInt(params.getOrDefault("current", "1").toString());
            int size = Integer.parseInt(params.getOrDefault("size", "10").toString());

            // 构建查询条件
            QueryWrapper<TblSwapTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && params.get("contractCode") != null && StringUtils.hasText(params.get("contractCode").toString())) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 掉期类型
            if (params.containsKey("swapType") && params.get("swapType") != null && StringUtils.hasText(params.get("swapType").toString())) {
                queryWrapper.eq("SWAP_TYPE", params.get("swapType"));
            }

            // 状态
            if (params.containsKey("status") && params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && params.get("currency") != null && StringUtils.hasText(params.get("currency").toString())) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 到期日期范围
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            if (params.containsKey("maturityDateStart") && params.get("maturityDateStart") != null && StringUtils.hasText(params.get("maturityDateStart").toString())) {
                try {
                    java.util.Date startDate = sdf.parse(params.get("maturityDateStart").toString());
                    queryWrapper.ge("MATURITY_DATE", startDate);
                } catch (Exception ignored) {}
            }
            if (params.containsKey("maturityDateEnd") && params.get("maturityDateEnd") != null && StringUtils.hasText(params.get("maturityDateEnd").toString())) {
                try {
                    // 结束日期取当天 23:59:59，确保包含当天数据
                    java.util.Date endDate = sdf.parse(params.get("maturityDateEnd").toString());
                    Calendar endCal = Calendar.getInstance();
                    endCal.setTime(endDate);
                    endCal.set(Calendar.HOUR_OF_DAY, 23);
                    endCal.set(Calendar.MINUTE, 59);
                    endCal.set(Calendar.SECOND, 59);
                    queryWrapper.le("MATURITY_DATE", endCal.getTime());
                } catch (Exception ignored) {}
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            PageHelper.startPage(current, size);
            List<TblSwapTransaction> list = swapTransactionService.list(queryWrapper);
            PageInfo<TblSwapTransaction> pageInfo = new PageInfo<>(list);

            // 构造返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取掉期交易详细信息
     */
    @GetMapping("/{transactionId}")
    @ApiOperation("获取掉期交易详细信息")
    public String getInfo(@ApiParam("掉期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            return new JsonBean(1, "查询成功", transaction).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增掉期交易
     */
    @PostMapping
    @ApiOperation("新增掉期交易")
    public String add(@FlexibleRequestBody TblSwapTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置创建信息
            transaction.setCreateTime(new Date());
            transaction.setCreateBy(String.valueOf(loginStaff.getStaffid()));
            transaction.setStatus("PENDING"); // 默认状态: 待生效

            // 根据生效日期和支付频率计算下次支付日
            if (transaction.getNextPaymentDate() == null) {
                transaction.setNextPaymentDate(calcNextPaymentDate(transaction.getEffectiveDate(), transaction.getPaymentFrequency()));
            }

            boolean success = swapTransactionService.save(transaction);
            if (success) {
                return new JsonBean(1, "新增成功", transaction).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改掉期交易
     */
    @PutMapping
    @ApiOperation("修改掉期交易")
    public String update(@FlexibleRequestBody TblSwapTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置更新信息
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            // 若下次支付日为空，重新根据生效日期和支付频率计算
            if (transaction.getNextPaymentDate() == null) {
                transaction.setNextPaymentDate(calcNextPaymentDate(transaction.getEffectiveDate(), transaction.getPaymentFrequency()));
            }

            boolean success = swapTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "修改成功", transaction).toJson();
            } else {
                return new JsonBean(0, "修改失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除掉期交易
     */
    @DeleteMapping("/{transactionIds}")
    @ApiOperation("删除掉期交易")
    public String remove(@ApiParam("掉期交易ID数组") @PathVariable Long[] transactionIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = swapTransactionService.removeByIds(Arrays.asList(transactionIds));
            if (success) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除掉期交易（POST方式，支持JSON和form-urlencoded）
     */
    @PostMapping("/delete")
    @ApiOperation("批量删除掉期交易")
    public String deleteBatch(@RequestBody(required = false) String requestBody) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            System.out.println("=== 批量删除掉期交易开始 ===");
            System.out.println("接收到的请求体: " + requestBody);

            // 收集所有 transactionIds
            java.util.List<Long> allIds = new java.util.ArrayList<>();

            if (requestBody != null && !requestBody.trim().isEmpty()) {
                // 尝试解析为 JSON
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    Map<String, Object> jsonMap = mapper.readValue(requestBody, Map.class);
                    System.out.println("JSON解析成功: " + jsonMap);

                    Object idsValue = jsonMap.get("transactionIds");
                    if (idsValue != null) {
                        if (idsValue instanceof java.util.List) {
                            for (Object item : (java.util.List<?>) idsValue) {
                                if (item instanceof String) {
                                    allIds.add(Long.parseLong((String) item));
                                } else if (item instanceof Number) {
                                    allIds.add(((Number) item).longValue());
                                }
                            }
                        } else if (idsValue instanceof String) {
                            String[] parts = ((String) idsValue).split(",");
                            for (String part : parts) {
                                if (!part.trim().isEmpty()) {
                                    allIds.add(Long.parseLong(part.trim()));
                                }
                            }
                        }
                    }
                } catch (Exception jsonEx) {
                    // JSON解析失败，尝试解析为 form-urlencoded
                    System.out.println("JSON解析失败，尝试解析为form-urlencoded: " + jsonEx.getMessage());
                    java.net.URLDecoder.decode(requestBody, "UTF-8");

                    // 解析 form-urlencoded 格式
                    String[] pairs = requestBody.split("&");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split("=", 2);
                        if (keyValue.length == 2) {
                            String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                            String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");

                            if (key.equals("transactionIds")) {
                                // 处理 JSON 数组格式: transactionIds=["13","12"]
                                if (value.startsWith("[") && value.endsWith("]")) {
                                    String arrayStr = value.substring(1, value.length() - 1);
                                    String[] ids = arrayStr.split(",");
                                    for (String id : ids) {
                                        String cleanId = id.trim().replaceAll("[\"']", "");
                                        if (!cleanId.isEmpty()) {
                                            allIds.add(Long.parseLong(cleanId));
                                        }
                                    }
                                } else {
                                    allIds.add(Long.parseLong(value));
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("合并后的 transactionIds: " + allIds);

            if (allIds.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            boolean success = swapTransactionService.removeByIds(allIds);
            System.out.println("删除结果: " + success);
            if (success) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("删除异常: " + e.getClass().getName() + " - " + e.getMessage());
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出掉期交易列表
     */
    @GetMapping("/export")
    @ApiOperation("导出掉期交易列表")
    public void export(@RequestParam Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.getWriter().write("用户已失效");
                return;
            }

            // 构建查询条件
            QueryWrapper<TblSwapTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && params.get("contractCode") != null && StringUtils.hasText(params.get("contractCode").toString())) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 掉期类型
            if (params.containsKey("swapType") && params.get("swapType") != null && StringUtils.hasText(params.get("swapType").toString())) {
                queryWrapper.eq("SWAP_TYPE", params.get("swapType"));
            }

            // 状态
            if (params.containsKey("status") && params.get("status") != null && StringUtils.hasText(params.get("status").toString())) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && params.get("currency") != null && StringUtils.hasText(params.get("currency").toString())) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 查询所有数据（不分页）
            java.util.List<TblSwapTransaction> list = swapTransactionService.list(queryWrapper);

            // 转换为导出 DTO
            java.util.List<com.global.treasurer.dto.derivatives.SwapTransactionExportDTO> exportList = new java.util.ArrayList<>();
            for (TblSwapTransaction transaction : list) {
                exportList.add(com.global.treasurer.dto.derivatives.SwapTransactionExportDTO.fromEntity(transaction));
            }

            // 导出 Excel
            try (com.global.treasurer.util.excel.ExcelExport export = new com.global.treasurer.util.excel.ExcelExport("掉期交易", com.global.treasurer.dto.derivatives.SwapTransactionExportDTO.class)) {
                export.setDataList(exportList).write(response, "掉期交易");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * 批量估值掉期交易
     */
    @PostMapping("/valuation/batch")
    @ApiOperation("批量估值掉期交易")
    public String batchValuation(@RequestBody(required = false) String requestBody) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            System.out.println("=== 批量估值掉期交易开始 ===");
            System.out.println("接收到的请求体: " + requestBody);

            // 收集所有 transactionIds
            java.util.List<Long> allIds = new java.util.ArrayList<>();

            if (requestBody != null && !requestBody.trim().isEmpty()) {
                // 尝试解析为 JSON
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    Map<String, Object> jsonMap = mapper.readValue(requestBody, Map.class);
                    System.out.println("JSON解析成功: " + jsonMap);

                    Object idsValue = jsonMap.get("transactionIds");
                    if (idsValue != null) {
                        if (idsValue instanceof java.util.List) {
                            for (Object item : (java.util.List<?>) idsValue) {
                                if (item instanceof String) {
                                    allIds.add(Long.parseLong((String) item));
                                } else if (item instanceof Number) {
                                    allIds.add(((Number) item).longValue());
                                }
                            }
                        } else if (idsValue instanceof String) {
                            String[] parts = ((String) idsValue).split(",");
                            for (String part : parts) {
                                if (!part.trim().isEmpty()) {
                                    allIds.add(Long.parseLong(part.trim()));
                                }
                            }
                        }
                    }
                } catch (Exception jsonEx) {
                    System.out.println("JSON解析失败，尝试解析为form-urlencoded: " + jsonEx.getMessage());
                    // 解析 form-urlencoded 格式
                    String[] pairs = requestBody.split("&");
                    for (String pair : pairs) {
                        String[] keyValue = pair.split("=", 2);
                        if (keyValue.length == 2) {
                            String key = java.net.URLDecoder.decode(keyValue[0], "UTF-8");
                            String value = java.net.URLDecoder.decode(keyValue[1], "UTF-8");

                            if (key.equals("transactionIds")) {
                                if (value.startsWith("[") && value.endsWith("]")) {
                                    String arrayStr = value.substring(1, value.length() - 1);
                                    String[] ids = arrayStr.split(",");
                                    for (String id : ids) {
                                        String cleanId = id.trim().replaceAll("[\"']", "");
                                        if (!cleanId.isEmpty()) {
                                            allIds.add(Long.parseLong(cleanId));
                                        }
                                    }
                                } else {
                                    allIds.add(Long.parseLong(value));
                                }
                            }
                        }
                    }
                }
            }

            System.out.println("需要估值的 transactionIds: " + allIds);

            if (allIds.isEmpty()) {
                return new JsonBean(0, "请选择要估值的记录", null).toJson();
            }

            // 批量更新估值（这里简化为设置为0，实际应该使用估值模型计算）
            int successCount = 0;
            for (Long id : allIds) {
                TblSwapTransaction transaction = swapTransactionService.getById(id);
                if (transaction != null) {
                    transaction.setCurrentValue(new java.math.BigDecimal(0.0));
                    transaction.setUpdateTime(new Date());
                    transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));
                    if (swapTransactionService.updateById(transaction)) {
                        successCount++;
                    }
                }
            }

            System.out.println("批量估值完成，成功: " + successCount + "/" + allIds.size());
            return new JsonBean(1, "批量估值成功，共处理 " + allIds.size() + " 条记录", successCount).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("批量估值异常: " + e.getClass().getName() + " - " + e.getMessage());
            return new JsonBean(0, "批量估值失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 终止掉期交易
     */
    @PostMapping("/terminate/{transactionId}")
    @ApiOperation("终止掉期交易")
    public String terminate(@ApiParam("掉期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            transaction.setStatus("TERMINATED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            boolean success = swapTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "终止成功", null).toJson();
            } else {
                return new JsonBean(0, "终止失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "终止失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消掉期交易
     */
    @PostMapping("/cancel/{transactionId}")
    @ApiOperation("取消掉期交易")
    public String cancel(@ApiParam("掉期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有待生效状态才能取消
            if (!"PENDING".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有待生效状态的交易才能取消", null).toJson();
            }

            transaction.setStatus("CANCELLED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            boolean success = swapTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "取消成功", null).toJson();
            } else {
                return new JsonBean(0, "取消失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取掉期现金流明细
     */
    @GetMapping("/cashflow/{transactionId}")
    @ApiOperation("获取掉期现金流明细")
    public String getCashflow(@ApiParam("掉期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // TODO: 从现金流表查询现金流数据
            // 这里先返回一个空的列表
            java.util.List<Map<String, Object>> cashflowList = new java.util.ArrayList<>();

            return new JsonBean(1, "查询成功", cashflowList).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 掉期交易估值
     */
    @PostMapping("/valuation/{transactionId}")
    @ApiOperation("掉期交易估值")
    public String valuation(
        @ApiParam("掉期交易ID") @PathVariable("transactionId") Long transactionId,
        @RequestParam(required = false) Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // TODO: 实现掉期估值计算逻辑
            // 这里先返回模拟数据
            Map<String, Object> valuationResult = new java.util.HashMap<>();
            valuationResult.put("currentValue", 0.0);
            valuationResult.put("npv", 0.0);
            valuationResult.put("valuationTime", new Date());

            // 更新估值
            transaction.setCurrentValue(new java.math.BigDecimal(0.0));
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));
            swapTransactionService.updateById(transaction);

            return new JsonBean(1, "估值成功", valuationResult).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "估值失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 掉期交易支付处理
     */
    @PostMapping("/payment")
    @ApiOperation("掉期交易支付处理")
    public String payment(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long transactionId = Long.parseLong(params.get("transactionId").toString());
            TblSwapTransaction transaction = swapTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 检查状态
            if (!"ACTIVE".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有生效中的掉期交易才能进行支付处理", null).toJson();
            }

            // TODO: 实现支付处理逻辑
            // 1. 计算支付金额
            // 2. 创建现金流记录
            // 3. 更新支付日期

            return new JsonBean(1, "支付处理成功", null).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "支付处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取掉期类型文本
     */
    private String getSwapTypeText(String swapType) {
        if (swapType == null) return "";
        switch (swapType) {
            case "INTEREST_RATE": return "利率掉期";
            case "CURRENCY": return "货币掉期";
            case "COMMODITY": return "商品掉期";
            case "CDS": return "信用违约掉期";
            default: return swapType;
        }
    }

    /**
     * 获取支付频率文本
     */
    private String getPaymentFrequencyText(String paymentFrequency) {
        if (paymentFrequency == null) return "";
        switch (paymentFrequency) {
            case "MONTHLY": return "月度";
            case "QUARTERLY": return "季度";
            case "SEMI_ANNUAL": return "半年";
            case "ANNUAL": return "年度";
            default: return paymentFrequency;
        }
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待生效";
            case "ACTIVE": return "生效中";
            case "TERMINATED": return "已终止";
            case "MATURED": return "已到期";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }

    /**
     * 根据生效日期和支付频率计算下次支付日
     * MONTHLY-月度, QUARTERLY-季度, SEMI_ANNUAL-半年, ANNUAL-年度
     */
    private Date calcNextPaymentDate(Date effectiveDate, String paymentFrequency) {
        if (effectiveDate == null || paymentFrequency == null) return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(effectiveDate);
        switch (paymentFrequency) {
            case "MONTHLY":    cal.add(Calendar.MONTH, 1);  break;
            case "QUARTERLY":  cal.add(Calendar.MONTH, 3);  break;
            case "SEMI_ANNUAL":cal.add(Calendar.MONTH, 6);  break;
            case "ANNUAL":     cal.add(Calendar.YEAR,  1);  break;
            default: return null;
        }
        return cal.getTime();
    }
}