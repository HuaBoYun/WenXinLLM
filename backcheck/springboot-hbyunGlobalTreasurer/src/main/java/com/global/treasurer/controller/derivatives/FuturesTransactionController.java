package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.derivatives.TblFuturesMarginRecord;
import com.global.treasurer.entity.derivatives.TblFuturesTransaction;
import com.global.treasurer.mapper.derivatives.TblFuturesMarginRecordMapper;
import com.global.treasurer.service.derivatives.IFuturesTransactionService;
import com.hbfk.entity.TblOrganizationUtil;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.List;

/**
 * 期货交易控制器
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@RestController
@RequestMapping("/derivatives/futures")
@Api(tags = "期货交易管理")
public class FuturesTransactionController {
    @Resource
    private IFuturesTransactionService futuresTransactionService;

    @Resource
    private TblFuturesMarginRecordMapper futuresMarginRecordMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询期货交易列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询期货交易列表")
    public String page(@RequestParam Map<String, Object> params) {
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
            QueryWrapper<TblFuturesTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 期货类型
            if (params.containsKey("futuresType") && !StringUtils.isEmpty(params.get("futuresType"))) {
                queryWrapper.eq("FUTURES_TYPE", params.get("futuresType"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 交易方向
            if (params.containsKey("direction") && !StringUtils.isEmpty(params.get("direction"))) {
                queryWrapper.eq("DIRECTION", params.get("direction"));
            }

            // 交易所 (EXCHANGE是达梦数据库保留字,需要特殊处理)
            if (params.containsKey("exchange") && !StringUtils.isEmpty(params.get("exchange"))) {
                // 使用apply方法手动添加带引号的字段名
                queryWrapper.apply("\"EXCHANGE\" = {0}", params.get("exchange"));
            }

            // 到期日期范围
            if (params.containsKey("expiryDateStart") && !StringUtils.isEmpty(params.get("expiryDateStart"))) {
                queryWrapper.ge("EXPIRY_DATE", params.get("expiryDateStart"));
            }
            if (params.containsKey("expiryDateEnd") && !StringUtils.isEmpty(params.get("expiryDateEnd"))) {
                queryWrapper.le("EXPIRY_DATE", params.get("expiryDateEnd"));
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TblFuturesTransaction> page = new Page<>(current, size);
            IPage<TblFuturesTransaction> pageList = futuresTransactionService.page(page, queryWrapper);

            // 构造返回结果 - 使用前端期望的格式
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("records", pageList.getRecords());
            result.put("total", pageList.getTotal());
            result.put("current", pageList.getCurrent());
            result.put("size", pageList.getSize());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询期货交易列表
     */
    @GetMapping("/list")
    @ApiOperation("查询期货交易列表")
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
            QueryWrapper<TblFuturesTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 期货类型
            if (params.containsKey("futuresType") && !StringUtils.isEmpty(params.get("futuresType"))) {
                queryWrapper.eq("FUTURES_TYPE", params.get("futuresType"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 交易方向
            if (params.containsKey("direction") && !StringUtils.isEmpty(params.get("direction"))) {
                queryWrapper.eq("DIRECTION", params.get("direction"));
            }

            // 交易所 (EXCHANGE是达梦数据库保留字,需要特殊处理)
            if (params.containsKey("exchange") && !StringUtils.isEmpty(params.get("exchange"))) {
                // 使用apply方法手动添加带引号的字段名
                queryWrapper.apply("\"EXCHANGE\" = {0}", params.get("exchange"));
            }

            // 到期日期范围
            if (params.containsKey("expiryDateStart") && !StringUtils.isEmpty(params.get("expiryDateStart"))) {
                queryWrapper.ge("EXPIRY_DATE", params.get("expiryDateStart"));
            }
            if (params.containsKey("expiryDateEnd") && !StringUtils.isEmpty(params.get("expiryDateEnd"))) {
                queryWrapper.le("EXPIRY_DATE", params.get("expiryDateEnd"));
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TblFuturesTransaction> page = new Page<>(current, size);
            IPage<TblFuturesTransaction> pageList = futuresTransactionService.page(page, queryWrapper);

            // 构造返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("tlist", pageList.getRecords());
            result.put("totalRecord", pageList.getTotal());
            result.put("pageNo", pageList.getCurrent());
            result.put("pageSize", pageList.getSize());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取期货交易详细信息
     */
    @GetMapping("/{transactionId}")
    @ApiOperation("获取期货交易详细信息")
    public String getInfo(@ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
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
     * 新增期货交易
     */
    @PostMapping
    @ApiOperation("新增期货交易")
    public String add(@FlexibleRequestBody TblFuturesTransaction transaction) {
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

            // 初始化浮动盈亏和已实现盈亏
            if (transaction.getUnrealizedPnl() == null) {
                transaction.setUnrealizedPnl(BigDecimal.ZERO);
            }
            if (transaction.getRealizedPnl() == null) {
                transaction.setRealizedPnl(BigDecimal.ZERO);
            }

            boolean success = futuresTransactionService.save(transaction);
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
     * 修改期货交易
     */
    @PutMapping
    @ApiOperation("修改期货交易")
    public String update(@FlexibleRequestBody TblFuturesTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置更新信息
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            boolean success = futuresTransactionService.updateById(transaction);
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
     * 删除期货交易
     */
    @DeleteMapping("/{transactionIds}")
    @ApiOperation("删除期货交易")
    public String remove(@ApiParam("期货交易ID数组") @PathVariable Long[] transactionIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = futuresTransactionService.removeByIds(Arrays.asList(transactionIds));
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
     * 批量删除期货交易（POST方式，支持FormData）
     */
    @PostMapping("/delete")
    @ApiOperation("批量删除期货交易")
    public String deleteBatch(@RequestParam Map<String, Object> params) {
        try {
            // 打印接收到的参数
            System.out.println("=== 删除期货交易 - 接收到的参数 ===");
            System.out.println("params: " + params);
            System.out.println("params size: " + params.size());
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                System.out.println("  key: [" + entry.getKey() + "], value: [" + entry.getValue() + "], value type: " + (entry.getValue() != null ? entry.getValue().getClass().getName() : "null"));
            }
            System.out.println("===========================================");

            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取要删除的 IDs
            java.util.List<Long> transactionIds = new java.util.ArrayList<>();

            // 遍历所有参数，FormData 使用数组索引作为键名
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                // 跳过非数字键名（可能是其他业务参数）
                if (!key.matches("^\\d+$")) {
                    // 尝试从特定参数名获取
                    if ("ids".equals(key) || "transactionIds".equals(key) || "transactionId".equals(key)) {
                        parseIdsObject(value, transactionIds);
                    }
                    continue;
                }

                // 处理数字索引作为键名的情况（FormData 数组格式）
                parseIdsObject(value, transactionIds);
            }

            System.out.println("=== 解析出的 transactionIds: " + transactionIds + " ===");

            if (transactionIds.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            boolean success = futuresTransactionService.removeByIds(transactionIds);
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
     * 解析 IDs 对象
     */
    private void parseIdsObject(Object idsObj, java.util.List<Long> transactionIds) {
        if (idsObj instanceof String[]) {
            String[] arr = (String[]) idsObj;
            for (String s : arr) {
                if (s != null && !s.isEmpty()) {
                    transactionIds.add(Long.parseLong(s));
                }
            }
        } else if (idsObj instanceof String) {
            String s = (String) idsObj;
            // 处理逗号分隔的字符串，如 "1,2,3"
            if (s.contains(",")) {
                String[] parts = s.split(",");
                for (String part : parts) {
                    if (part != null && !part.trim().isEmpty()) {
                        transactionIds.add(Long.parseLong(part.trim()));
                    }
                }
            } else {
                try {
                    transactionIds.add(Long.parseLong(s));
                } catch (NumberFormatException e) {
                    // 忽略无法解析的数字
                }
            }
        } else if (idsObj instanceof java.util.List) {
            for (Object item : (java.util.List<?>) idsObj) {
                if (item != null) {
                    try {
                        transactionIds.add(Long.parseLong(item.toString()));
                    } catch (NumberFormatException e) {
                        // 忽略无法解析的数字
                    }
                }
            }
        } else if (idsObj != null) {
            try {
                transactionIds.add(Long.parseLong(idsObj.toString()));
            } catch (NumberFormatException e) {
                // 忽略无法解析的数字
            }
        }
    }

    /**
     * 保证金管理
     */
    @PostMapping("/{transactionId}/margin")
    @ApiOperation("保证金管理")
    public String manageMargin(
            @ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId,
            @RequestBody Map<String, Object> marginData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取期货交易
            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 获取操作参数
            String operationType = (String) marginData.get("operationType"); // ADD-追加, WITHDRAW-提取
            BigDecimal amount = new BigDecimal(marginData.get("amount").toString());
            String reason = (String) marginData.get("reason");

            // 记录变动前余额
            BigDecimal beforeBalance = transaction.getMargin() != null ? transaction.getMargin() : BigDecimal.ZERO;

            // 根据操作类型计算变动后余额
            BigDecimal afterBalance;
            if ("ADD".equals(operationType)) {
                afterBalance = beforeBalance.add(amount);
            } else if ("WITHDRAW".equals(operationType)) {
                afterBalance = beforeBalance.subtract(amount);
                if (afterBalance.compareTo(BigDecimal.ZERO) < 0) {
                    return new JsonBean(0, "保证金余额不足", null).toJson();
                }
            } else {
                return new JsonBean(0, "操作类型不正确", null).toJson();
            }

            // 更新期货交易保证金余额
            transaction.setMargin(afterBalance);
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));
            futuresTransactionService.updateById(transaction);

            // 记录保证金变动历史
            TblFuturesMarginRecord record = new TblFuturesMarginRecord();
            record.setTransactionId(transactionId);
            record.setOperationType(operationType);
            record.setAmount(amount);
            record.setBeforeBalance(beforeBalance);
            record.setAfterBalance(afterBalance);
            record.setReason(reason);
            record.setOperator(loginStaff.getRealname());
            record.setOperateTime(new Date());
            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            record.setOrgId(org != null ? org.getOrgid().longValue() : null);
            futuresMarginRecordMapper.insert(record);

            return new JsonBean(1, "保证金操作成功", transaction).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "保证金操作失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询保证金信息
     */
    @GetMapping("/{transactionId}/margin")
    @ApiOperation("查询保证金信息")
    public String getMarginInfo(@ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取期货交易
            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 查询保证金变动记录
            QueryWrapper<TblFuturesMarginRecord> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("TRANSACTION_ID", transactionId);
            queryWrapper.orderByDesc("OPERATE_TIME");
            java.util.List<TblFuturesMarginRecord> records = futuresMarginRecordMapper.selectList(queryWrapper);

            // 构造返回结果
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("transaction", transaction);
            result.put("marginRecords", records);

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 平仓
     */
    @PostMapping("/close/{transactionId}")
    @ApiOperation("平仓")
    public String closePosition(
            @ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId,
            @RequestParam Map<String, Object> closeData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取期货交易
            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有持仓中状态才能平仓
            if (!"OPEN".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有持仓中状态的交易才能平仓", null).toJson();
            }

            // 获取平仓价格
            BigDecimal closePrice = new BigDecimal(closeData.get("closePrice").toString());

            // 计算已实现盈亏
            BigDecimal priceDiff = closePrice.subtract(transaction.getOpenPrice());
            BigDecimal realizedPnl = priceDiff.multiply(transaction.getContractSize());

            // 如果是空头,盈亏方向相反
            if ("SHORT".equals(transaction.getDirection())) {
                realizedPnl = realizedPnl.negate();
            }

            // 更新交易状态
            transaction.setCurrentPrice(closePrice);
            transaction.setRealizedPnl(realizedPnl);
            transaction.setStatus("CLOSED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            boolean success = futuresTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "平仓成功,已实现盈亏: " + realizedPnl, transaction).toJson();
            } else {
                return new JsonBean(0, "平仓失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "平仓失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 交割
     */
    @PostMapping("/settle/{transactionId}")
    @ApiOperation("交割")
    public String settle(@ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取期货交易
            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有持仓中状态才能交割
            if (!"OPEN".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有持仓中状态的交易才能交割", null).toJson();
            }

            // 更新交易状态
            transaction.setStatus("SETTLED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(String.valueOf(loginStaff.getStaffid()));

            boolean success = futuresTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "交割成功", transaction).toJson();
            } else {
                return new JsonBean(0, "交割失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "交割失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 每日结算
     */
    @PostMapping("/dailySettlement")
    @ApiOperation("每日结算（批量）")
    public String dailySettlement(@RequestBody(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            if (params != null && params.get("orgId") != null) {
                orgId = Long.parseLong(params.get("orgId").toString());
            }

            QueryWrapper<TblFuturesTransaction> wrapper = new QueryWrapper<>();
            wrapper.eq("STATUS", "OPEN");
            wrapper.eq("DEL_FLAG", "0");
            wrapper.eq("ORG_ID", orgId);
            List<TblFuturesTransaction> openList = futuresTransactionService.list(wrapper);

            if (openList == null || openList.isEmpty()) {
                return new JsonBean(1, "暂无持仓中的期货交易需要结算", null).toJson();
            }

            int successCount = 0;
            int skipCount = 0;
            BigDecimal totalUnrealizedPnl = BigDecimal.ZERO;
            String updateBy = String.valueOf(loginStaff.getStaffid());

            for (TblFuturesTransaction transaction : openList) {
                BigDecimal currentPrice = transaction.getCurrentPrice();
                BigDecimal openPrice = transaction.getOpenPrice();
                BigDecimal contractSize = transaction.getContractSize();

                if (currentPrice == null || openPrice == null || contractSize == null) {
                    skipCount++;
                    continue;
                }

                BigDecimal priceDiff = currentPrice.subtract(openPrice);
                BigDecimal unrealizedPnl = priceDiff.multiply(contractSize);
                if ("SHORT".equals(transaction.getDirection())) {
                    unrealizedPnl = unrealizedPnl.negate();
                }

                transaction.setUnrealizedPnl(unrealizedPnl);
                transaction.setUpdateTime(new Date());
                transaction.setUpdateBy(updateBy);

                if (futuresTransactionService.updateById(transaction)) {
                    successCount++;
                    totalUnrealizedPnl = totalUnrealizedPnl.add(unrealizedPnl);
                }
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("totalCount", openList.size());
            result.put("successCount", successCount);
            result.put("skipCount", skipCount);
            result.put("totalUnrealizedPnl", totalUnrealizedPnl);
            return new JsonBean(1, "每日结算完成,共处理 " + successCount + " 条,跳过 " + skipCount + " 条", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "每日结算失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消期货交易
     */
    @PostMapping("/cancel/{transactionId}")
    @ApiOperation("取消期货交易")
    public String cancel(@ApiParam("期货交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFuturesTransaction transaction = futuresTransactionService.getById(transactionId);
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

            boolean success = futuresTransactionService.updateById(transaction);
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
     * 导出期货交易
     */
    @GetMapping("/export")
    @ApiOperation("导出期货交易")
    public void export(@RequestParam Map<String, Object> params,
                   javax.servlet.http.HttpServletResponse response) {
        org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = null;
        java.io.OutputStream out = null;
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return;
            }

            // 构建查询条件
            QueryWrapper<TblFuturesTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 期货类型
            if (params.containsKey("futuresType") && !StringUtils.isEmpty(params.get("futuresType"))) {
                queryWrapper.eq("FUTURES_TYPE", params.get("futuresType"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 交易方向
            if (params.containsKey("direction") && !StringUtils.isEmpty(params.get("direction"))) {
                queryWrapper.eq("DIRECTION", params.get("direction"));
            }

            // 交易所 (EXCHANGE是达梦数据库保留字,需要特殊处理)
            if (params.containsKey("exchange") && !StringUtils.isEmpty(params.get("exchange"))) {
                queryWrapper.apply("\"EXCHANGE\" = {0}", params.get("exchange"));
            }

            // 到期日期范围
            if (params.containsKey("expiryDateStart") && !StringUtils.isEmpty(params.get("expiryDateStart"))) {
                queryWrapper.ge("EXPIRY_DATE", params.get("expiryDateStart"));
            }
            if (params.containsKey("expiryDateEnd") && !StringUtils.isEmpty(params.get("expiryDateEnd"))) {
                queryWrapper.le("EXPIRY_DATE", params.get("expiryDateEnd"));
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 查询所有数据（不分页）
            List<TblFuturesTransaction> list = futuresTransactionService.list(queryWrapper);

            // 创建工作簿
            workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("期货交易");

            // 创建表头
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"合约编号", "期货类型", "标的资产", "交易方向", "合约数量", "开仓价格", "当前价格", "币种", "到期日期", "保证金", "浮动盈亏", "交易所", "期货公司", "交易员", "状态", "开仓日期"};
            for (int i = 0; i < headers.length; i++) {
                headerRow.createCell(i).setCellValue(headers[i]);
            }

            // 创建表头样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // 应用表头样式
            for (int i = 0; i < headers.length; i++) {
                headerRow.getCell(i).setCellStyle(headerStyle);
            }

            // 创建数据行
            int rowNum = 1;
            for (TblFuturesTransaction item : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(item.getContractCode() != null ? item.getContractCode() : "");
                row.createCell(1).setCellValue(getFuturesTypeText(item.getFuturesType()));
                row.createCell(2).setCellValue(item.getUnderlyingAsset() != null ? item.getUnderlyingAsset() : "");
                row.createCell(3).setCellValue(getDirectionText(item.getDirection()));
                row.createCell(4).setCellValue(item.getContractSize() != null ? item.getContractSize().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getOpenPrice() != null ? item.getOpenPrice().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getCurrentPrice() != null ? item.getCurrentPrice().doubleValue() : 0);
                row.createCell(7).setCellValue(item.getCurrency() != null ? item.getCurrency() : "");
                row.createCell(8).setCellValue(item.getExpiryDate() != null ? item.getExpiryDate().toString() : "");
                row.createCell(9).setCellValue(item.getMargin() != null ? item.getMargin().doubleValue() : 0);
                row.createCell(10).setCellValue(item.getUnrealizedPnl() != null ? item.getUnrealizedPnl().doubleValue() : 0);
                row.createCell(11).setCellValue(item.getExchange() != null ? item.getExchange() : "");
                row.createCell(12).setCellValue(item.getBroker() != null ? item.getBroker() : "");
                row.createCell(13).setCellValue(item.getTrader() != null ? item.getTrader() : "");
                row.createCell(14).setCellValue(getStatusText(item.getStatus()));
                row.createCell(15).setCellValue(item.getTradeDate() != null ? item.getTradeDate().toString() : "");
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("期货交易_" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()), "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".xlsx\"");

            // 输出
            out = response.getOutputStream();
            workbook.write(out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    // ignore
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    // ignore
                }
            }
        }
    }

    private String getFuturesTypeText(String type) {
        if (type == null) return "";
        switch (type) {
            case "COMMODITY": return "商品期货";
            case "FINANCIAL": return "金融期货";
            case "INDEX": return "股指期货";
            case "FX": return "外汇期货";
            default: return type;
        }
    }

    private String getDirectionText(String direction) {
        if (direction == null) return "";
        switch (direction) {
            case "LONG": return "多头";
            case "SHORT": return "空头";
            default: return direction;
        }
    }

    private String getStatusText(String status) {
        if (status == null) return "";
        switch (status) {
            case "PENDING": return "待生效";
            case "OPEN": return "持仓中";
            case "CLOSED": return "已平仓";
            case "SETTLED": return "已交割";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }
}
