package com.global.treasurer.controller.derivatives;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.derivatives.TblForwardTransaction;
import com.global.treasurer.service.derivatives.IForwardTransactionService;
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
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 远期交易控制器
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@RestController
@RequestMapping("/derivatives/forward")
@Api(tags = "远期交易管理")
public class ForwardTransactionController {
    @Resource
    private IForwardTransactionService forwardTransactionService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询远期交易列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询远期交易列表")
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
            QueryWrapper<TblForwardTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 交易类型
            if (params.containsKey("transactionType") && !StringUtils.isEmpty(params.get("transactionType"))) {
                queryWrapper.eq("TRANSACTION_TYPE", params.get("transactionType"));
            }

            // 标的资产
            if (params.containsKey("underlyingAsset") && !StringUtils.isEmpty(params.get("underlyingAsset"))) {
                queryWrapper.like("UNDERLYING_ASSET", params.get("underlyingAsset"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 交易对手
            if (params.containsKey("counterparty") && !StringUtils.isEmpty(params.get("counterparty"))) {
                queryWrapper.like("COUNTERPARTY", params.get("counterparty"));
            }

            // 到期日期范围
            if (params.containsKey("maturityDateStart") && !StringUtils.isEmpty(params.get("maturityDateStart"))) {
                queryWrapper.ge("MATURITY_DATE", params.get("maturityDateStart").toString());
            }
            if (params.containsKey("maturityDateEnd") && !StringUtils.isEmpty(params.get("maturityDateEnd"))) {
                queryWrapper.le("MATURITY_DATE", params.get("maturityDateEnd").toString());
            }

            // 交易日期范围
            if (params.containsKey("tradeDateRange") && !StringUtils.isEmpty(params.get("tradeDateRange"))) {
                String[] dateRange = params.get("tradeDateRange").toString().split(",");
                if (dateRange.length == 2) {
                    queryWrapper.ge("TRADE_DATE", dateRange[0]);
                    queryWrapper.le("TRADE_DATE", dateRange[1]);
                }
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TblForwardTransaction> page = new Page<>(current, size);
            IPage<TblForwardTransaction> pageList = forwardTransactionService.page(page, queryWrapper);

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
     * 查询远期交易列表
     */
    @GetMapping("/list")
    @ApiOperation("查询远期交易列表")
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
            QueryWrapper<TblForwardTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 交易类型
            if (params.containsKey("transactionType") && !StringUtils.isEmpty(params.get("transactionType"))) {
                queryWrapper.eq("TRANSACTION_TYPE", params.get("transactionType"));
            }

            // 标的资产
            if (params.containsKey("underlyingAsset") && !StringUtils.isEmpty(params.get("underlyingAsset"))) {
                queryWrapper.like("UNDERLYING_ASSET", params.get("underlyingAsset"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 交易对手
            if (params.containsKey("counterparty") && !StringUtils.isEmpty(params.get("counterparty"))) {
                queryWrapper.like("COUNTERPARTY", params.get("counterparty"));
            }

            // 到期日期范围
            if (params.containsKey("maturityDateStart") && !StringUtils.isEmpty(params.get("maturityDateStart"))) {
                queryWrapper.ge("MATURITY_DATE", params.get("maturityDateStart").toString());
            }
            if (params.containsKey("maturityDateEnd") && !StringUtils.isEmpty(params.get("maturityDateEnd"))) {
                queryWrapper.le("MATURITY_DATE", params.get("maturityDateEnd").toString());
            }

            // 交易日期范围
            if (params.containsKey("tradeDateRange") && !StringUtils.isEmpty(params.get("tradeDateRange"))) {
                String[] dateRange = params.get("tradeDateRange").toString().split(",");
                if (dateRange.length == 2) {
                    queryWrapper.ge("TRADE_DATE", dateRange[0]);
                    queryWrapper.le("TRADE_DATE", dateRange[1]);
                }
            }

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TblForwardTransaction> page = new Page<>(current, size);
            IPage<TblForwardTransaction> pageList = forwardTransactionService.page(page, queryWrapper);

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
     * 获取远期交易详细信息
     */
    @GetMapping("/{transactionId}")
    @ApiOperation("获取远期交易详细信息")
    public String getInfo(@ApiParam("远期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
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
     * 新增远期交易
     */
    @PostMapping
    @ApiOperation("新增远期交易")
    public String add(@FlexibleRequestBody TblForwardTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置创建信息
            transaction.setCreateTime(new Date());
            transaction.setCreateBy(loginStaff.getStaffid().longValue());
            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            transaction.setOrgId(org != null ? org.getOrgid().longValue() : null);
            transaction.setStatus("PENDING"); // 默认状态: 待生效
            transaction.setDelFlag(0); // 默认未删除

            // 初始化估值和损益为0
            if (transaction.getCurrentValue() == null) {
                transaction.setCurrentValue(BigDecimal.ZERO);
            }
            if (transaction.getPnl() == null) {
                transaction.setPnl(BigDecimal.ZERO);
            }

            boolean success = forwardTransactionService.save(transaction);
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
     * 修改远期交易
     */
    @PutMapping
    @ApiOperation("修改远期交易")
    public String update(@FlexibleRequestBody TblForwardTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 验证记录是否存在
            TblForwardTransaction existingTransaction = forwardTransactionService.getById(transaction.getTransactionId());
            if (existingTransaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有待生效状态的交易才能修改
            if (!"PENDING".equals(existingTransaction.getStatus())) {
                return new JsonBean(0, "只有待生效状态的交易才能修改", null).toJson();
            }

            // 设置更新信息
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = forwardTransactionService.updateById(transaction);
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
     * 删除远期交易
     */
    @DeleteMapping("/{transactionIds}")
    @ApiOperation("删除远期交易")
    public String remove(@ApiParam("远期交易ID数组") @PathVariable Long[] transactionIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 验证所有交易是否可以删除
            for (Long transactionId : transactionIds) {
                TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
                if (transaction != null && !"PENDING".equals(transaction.getStatus())) {
                    return new JsonBean(0, "只有待生效状态的交易才能删除", null).toJson();
                }
            }

            boolean success = forwardTransactionService.removeByIds(Arrays.asList(transactionIds));
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
     * 批量删除远期交易（POST方式，支持JSON body）
     */
    @PostMapping("/delete")
    @ApiOperation("批量删除远期交易")
    public String deleteBatch(@RequestBody Long[] transactionIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (transactionIds == null || transactionIds.length == 0) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            // 验证所有交易是否可以删除
            for (Long transactionId : transactionIds) {
                TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
                if (transaction != null && !"PENDING".equals(transaction.getStatus())) {
                    return new JsonBean(0, "只有待生效状态的交易才能删除", null).toJson();
                }
            }

            boolean success = forwardTransactionService.removeByIds(Arrays.asList(transactionIds));
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
     * 执行远期合约
     */
    @PostMapping("/{transactionId}/execute")
    @ApiOperation("执行远期合约")
    public String execute(@ApiParam("远期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有待生效状态才能执行
            if (!"PENDING".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有待生效状态的交易才能执行", null).toJson();
            }

            // 更新状态为生效中
            transaction.setStatus("ACTIVE");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = forwardTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "执行成功", null).toJson();
            } else {
                return new JsonBean(0, "执行失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 交割远期合约
     */
    @PostMapping("/{transactionId}/settle")
    @ApiOperation("交割远期合约")
    public String settle(@ApiParam("远期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有生效中状态才能交割
            if (!"ACTIVE".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有生效中状态的交易才能交割", null).toJson();
            }

            // 更新状态为已交割
            transaction.setStatus("SETTLED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = forwardTransactionService.updateById(transaction);
            if (success) {
                return new JsonBean(1, "交割成功", null).toJson();
            } else {
                return new JsonBean(0, "交割失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "交割失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 远期交易估值
     */
    @GetMapping("/{transactionId}/valuation")
    @ApiOperation("远期交易估值")
    public String valuation(@ApiParam("远期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有生效中或已执行状态才能估值
            if (!"ACTIVE".equals(transaction.getStatus()) && !"EXECUTED".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有生效中或已执行状态的交易才能估值", null).toJson();
            }

            // TODO: 实现实际的估值逻辑
            // 这里简化处理，可以根据实际业务需求调用估值模型
            // 例如：根据当前市场价格计算合约的公允价值

            // 模拟估值结果
            BigDecimal currentValue = transaction.getNotionalAmount()
                    .multiply(transaction.getForwardPrice())
                    .multiply(new BigDecimal("0.01")); // 简化的估值计算

            BigDecimal pnl = currentValue.subtract(transaction.getCurrentValue());

            transaction.setCurrentValue(currentValue);
            transaction.setPnl(pnl);
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = forwardTransactionService.updateById(transaction);
            if (success) {
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("currentValue", currentValue);
                result.put("pnl", pnl);
                return new JsonBean(1, "估值成功", result).toJson();
            } else {
                return new JsonBean(0, "估值失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "估值失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询远期汇率
     */
    @GetMapping("/rates")
    @ApiOperation("查询远期汇率")
    public String getRates(@RequestParam(required = false) String currencyPair,
                           @RequestParam(required = false) String term) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // TODO: 实现实际的汇率查询逻辑
            // 这里应该调用外部数据源或汇率服务获取真实的远期汇率数据
            // 例如：从Bloomberg、Reuters或银行API获取

            // 模拟返回汇率数据
            Map<String, Object> result = new java.util.HashMap<>();
            result.put("USD/CNY_1M", "7.2456");
            result.put("USD/CNY_3M", "7.2678");
            result.put("USD/CNY_6M", "7.3012");
            result.put("USD/CNY_12M", "7.3567");
            result.put("EUR/CNY_1M", "7.8234");
            result.put("EUR/CNY_3M", "7.8567");
            result.put("EUR/CNY_6M", "7.9012");
            result.put("EUR/CNY_12M", "7.9876");
            result.put("updateTime", new Date());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量估值远期交易
     */
    @PostMapping("/valuation/batch")
    @ApiOperation("批量估值远期交易")
    public String batchValuation(@RequestBody(required = false) Long[] transactionIds) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TblForwardTransaction> list;
            if (transactionIds != null && transactionIds.length > 0) {
                list = forwardTransactionService.listByIds(Arrays.asList(transactionIds));
            } else {
                QueryWrapper<TblForwardTransaction> qw = new QueryWrapper<>();
                qw.in("STATUS", "ACTIVE", "EXECUTED").eq("DEL_FLAG", 0);
                list = forwardTransactionService.list(qw);
            }

            int successCount = 0;
            for (TblForwardTransaction t : list) {
                if (!"ACTIVE".equals(t.getStatus()) && !"EXECUTED".equals(t.getStatus())) continue;
                BigDecimal currentValue = t.getNotionalAmount() != null && t.getForwardPrice() != null
                        ? t.getNotionalAmount().multiply(t.getForwardPrice()).multiply(new BigDecimal("0.01"))
                        : BigDecimal.ZERO;
                BigDecimal pnl = t.getCurrentValue() != null
                        ? currentValue.subtract(t.getCurrentValue())
                        : currentValue;
                t.setCurrentValue(currentValue);
                t.setPnl(pnl);
                t.setUpdateTime(new Date());
                t.setUpdateBy(loginStaff.getStaffid().longValue());
                if (forwardTransactionService.updateById(t)) successCount++;
            }

            Map<String, Object> result = new java.util.HashMap<>();
            result.put("total", list.size());
            result.put("successCount", successCount);
            return new JsonBean(1, "批量估值完成，成功处理 " + successCount + "/" + list.size() + " 条记录", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量估值失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消远期交易
     */
    @PostMapping("/{transactionId}/cancel")
    @ApiOperation("取消远期交易")
    public String cancel(@ApiParam("远期交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblForwardTransaction transaction = forwardTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 只有待生效状态才能取消
            if (!"PENDING".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有待生效状态的交易才能取消", null).toJson();
            }

            transaction.setStatus("CANCELLED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = forwardTransactionService.updateById(transaction);
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
     * 导出远期交易列表
     */
    @GetMapping("/export")
    @ApiOperation("导出远期交易列表")
    public void export(@RequestParam Map<String, Object> params, HttpServletResponse response) throws IOException {
        QueryWrapper<TblForwardTransaction> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("DEL_FLAG", 0);
        String transactionType = (String) params.get("transactionType");
        String status = (String) params.get("status");
        String currency = (String) params.get("currency");
        if (!StringUtils.isEmpty(transactionType)) {
            queryWrapper.eq("TRANSACTION_TYPE", transactionType);
        }
        if (!StringUtils.isEmpty(status)) {
            queryWrapper.eq("STATUS", status);
        }
        if (!StringUtils.isEmpty(currency)) {
            queryWrapper.eq("CURRENCY", currency);
        }
        queryWrapper.orderByDesc("CREATE_TIME");
        List<TblForwardTransaction> list = forwardTransactionService.list(queryWrapper);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<List<Object>> dataList = new ArrayList<>();
        for (TblForwardTransaction t : list) {
            List<Object> row = new ArrayList<>();
            row.add(t.getContractCode());
            row.add(t.getTransactionType());
            row.add(t.getUnderlyingAsset());
            row.add(t.getNotionalAmount());
            row.add(t.getCurrency());
            row.add(t.getForwardPrice());
            row.add(t.getTradeDate() != null ? sdf.format(t.getTradeDate()) : "");
            row.add(t.getMaturityDate() != null ? sdf.format(t.getMaturityDate()) : "");
            row.add(t.getStatus());
            row.add(t.getCounterparty());
            row.add(t.getTrader());
            row.add(t.getRemark());
            dataList.add(row);
        }
        List<List<String>> head = Arrays.asList(
            Collections.singletonList("合约编号"),
            Collections.singletonList("交易类型"),
            Collections.singletonList("标的资产"),
            Collections.singletonList("名义本金"),
            Collections.singletonList("币种"),
            Collections.singletonList("远期价格"),
            Collections.singletonList("交易日期"),
            Collections.singletonList("到期日期"),
            Collections.singletonList("状态"),
            Collections.singletonList("交易对手"),
            Collections.singletonList("交易员"),
            Collections.singletonList("备注")
        );

        String fileName = URLEncoder.encode("远期交易列表", "UTF-8");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream())
            .head(head)
            .sheet("远期交易")
            .doWrite(dataList);
    }
}
