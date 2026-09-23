package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.derivatives.*;
import com.global.treasurer.service.derivatives.*;
import com.global.treasurer.mapper.derivatives.TblDerivativesStressTestMapper;


import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * 衍生品监控控制器
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@RestController
@RequestMapping({"/derivatives/monitoring"})
@Api(tags = "衍生品监控管理")
public class DerivativesMonitoringController {
    @Resource
    private IDerivativesAlertService alertService;

    @Resource
    private IDerivativesMarketDataService marketDataService;

    @Resource
    private IForwardTransactionService forwardTransactionService;

    @Resource
    private IOptionTransactionService optionTransactionService;

    @Resource
    private IFuturesTransactionService futuresTransactionService;

    @Resource
    private ISwapTransactionService swapTransactionService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取监控仪表盘数据
     */
    @GetMapping("/dashboard")
    @ApiOperation("获取监控仪表盘数据")
    public String getDashboard() {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // 构造仪表盘数据
            Map<String, Object> dashboard = new HashMap<>();

            // 1. 远期交易
            QueryWrapper<TblForwardTransaction> forwardWrapper = new QueryWrapper<>();
            if (orgId != null) forwardWrapper.eq("ORG_ID", orgId);
            forwardWrapper.eq("DEL_FLAG", 0);
            List<TblForwardTransaction> forwardList = forwardTransactionService.list(forwardWrapper);
            dashboard.put("forwardCount", (long) forwardList.size());

            // 2. 期权交易
            QueryWrapper<TblOptionTransaction> optionWrapper = new QueryWrapper<>();
            if (orgId != null) optionWrapper.eq("ORG_ID", orgId);
            optionWrapper.eq("DEL_FLAG", 0);
            List<TblOptionTransaction> optionList = optionTransactionService.list(optionWrapper);
            dashboard.put("optionCount", (long) optionList.size());

            // 3. 掉期交易（DEL_FLAG是VARCHAR）
            QueryWrapper<TblSwapTransaction> swapWrapper = new QueryWrapper<>();
            if (orgId != null) swapWrapper.eq("ORG_ID", orgId);
            swapWrapper.eq("DEL_FLAG", "0");
            List<TblSwapTransaction> swapList = swapTransactionService.list(swapWrapper);
            dashboard.put("swapCount", (long) swapList.size());

            // 4. 期货交易（DEL_FLAG是VARCHAR）
            QueryWrapper<TblFuturesTransaction> futuresWrapper = new QueryWrapper<>();
            if (orgId != null) futuresWrapper.eq("ORG_ID", orgId);
            futuresWrapper.eq("DEL_FLAG", "0");
            List<TblFuturesTransaction> futuresList = futuresTransactionService.list(futuresWrapper);
            dashboard.put("futuresCount", (long) futuresList.size());

            // 总持仓价值（包含全部4种交易类型）
            BigDecimal forwardNotional = forwardList.stream()
                    .filter(t -> t.getNotionalAmount() != null)
                    .map(TblForwardTransaction::getNotionalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal optionNotional = optionList.stream()
                    .filter(t -> t.getContractSize() != null)
                    .map(TblOptionTransaction::getContractSize)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal futuresNotional = futuresList.stream()
                    .filter(t -> t.getContractSize() != null)
                    .map(TblFuturesTransaction::getContractSize)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal swapNotional = swapList.stream()
                    .filter(t -> t.getNotionalAmount() != null)
                    .map(TblSwapTransaction::getNotionalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNotional = forwardNotional.add(optionNotional).add(futuresNotional).add(swapNotional)
                    .divide(new BigDecimal("10000"), 2, java.math.RoundingMode.HALF_UP);
            dashboard.put("totalNotional", totalNotional);

            // 今日损益（从真实数据计算）
            BigDecimal forwardPnl = forwardList.stream()
                    .filter(t -> t.getPnl() != null)
                    .map(TblForwardTransaction::getPnl)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal optionPnl = optionList.stream()
                    .map(t -> t.getTheoreticalValue() != null && t.getPremium() != null
                            ? t.getTheoreticalValue().subtract(t.getPremium()) : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal futuresPnl = futuresList.stream()
                    .filter(t -> t.getUnrealizedPnl() != null)
                    .map(TblFuturesTransaction::getUnrealizedPnl)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal swapPnl = swapList.stream()
                    .filter(t -> t.getCurrentValue() != null)
                    .map(TblSwapTransaction::getCurrentValue)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal todayPnL = forwardPnl.add(optionPnl).add(futuresPnl).add(swapPnl)
                    .divide(new BigDecimal("10000"), 2, java.math.RoundingMode.HALF_UP);
            dashboard.put("todayPnL", todayPnL);

            // 风险价值VaR（从真实名义金额计算，与/risk端点逻辑一致）
            List<BigDecimal> notionalList = new ArrayList<>();
            forwardList.forEach(t -> { if (t.getNotionalAmount() != null) notionalList.add(t.getNotionalAmount()); });
            optionList.forEach(t -> { if (t.getContractSize() != null) notionalList.add(t.getContractSize()); });
            futuresList.forEach(t -> { if (t.getContractSize() != null) notionalList.add(t.getContractSize()); });
            swapList.forEach(t -> { if (t.getNotionalAmount() != null) notionalList.add(t.getNotionalAmount()); });
            if (notionalList.isEmpty()) {
                dashboard.put("var", 0.0);
            } else {
                List<BigDecimal> sorted = new ArrayList<>(notionalList);
                sorted.sort(Comparator.naturalOrder());
                int varIdx = Math.max(0, (int) Math.floor(sorted.size() * 0.05));
                BigDecimal var95 = sorted.get(varIdx).multiply(new BigDecimal("0.05"))
                        .divide(new BigDecimal("10000"), 2, java.math.RoundingMode.HALF_UP);
                dashboard.put("var", var95);
            }

            // 预警数量
            QueryWrapper<TblDerivativesAlert> alertWrapper = new QueryWrapper<>();
            if (orgId != null) alertWrapper.eq("ORG_ID", orgId);
            alertWrapper.eq("STATUS", "TRIGGERED");
            alertWrapper.eq("DEL_FLAG", 0);
            long alertCount = alertService.count(alertWrapper);
            dashboard.put("alertCount", alertCount);

            return new JsonBean(1, "查询成功", dashboard).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取持仓监控数据
     */
    @GetMapping("/position")
    @ApiOperation("获取持仓监控数据")
    public String getPositionMonitoring(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // 查询所有衍生品持仓数据
            List<Map<String, Object>> positionList = new ArrayList<>();

            // 1. 查询远期交易 — 空字符串视为不过滤
            String productTypeRaw = params.containsKey("productType") && params.get("productType") != null
                    ? params.get("productType").toString().trim() : "";
            String productType = productTypeRaw.isEmpty() ? null : productTypeRaw;

            String currencyRaw = params.containsKey("currency") && params.get("currency") != null
                    ? params.get("currency").toString().trim() : "";
            String currency = currencyRaw.isEmpty() ? null : currencyRaw;

            // 分页参数
            int pageNum = 1, pageSize = 20;
            try {
                if (params.containsKey("pageNum") && params.get("pageNum") != null && !params.get("pageNum").toString().isEmpty())
                    pageNum = Integer.parseInt(params.get("pageNum").toString());
                if (params.containsKey("pageSize") && params.get("pageSize") != null && !params.get("pageSize").toString().isEmpty())
                    pageSize = Integer.parseInt(params.get("pageSize").toString());
            } catch (NumberFormatException ignored) {}

            List<TblForwardTransaction> forwardList = new ArrayList<>();
            if (productType == null || "FORWARD".equals(productType)) {
                QueryWrapper<TblForwardTransaction> forwardWrapper = new QueryWrapper<>();
                if (orgId != null) forwardWrapper.eq("ORG_ID", orgId);
                forwardWrapper.eq("DEL_FLAG", 0);
                forwardWrapper.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) {
                    forwardWrapper.eq("CURRENCY", currency);
                }
                forwardList = forwardTransactionService.list(forwardWrapper);
            }

            // 转换为统一格式
            for (TblForwardTransaction forward : forwardList) {
                Map<String, Object> position = new HashMap<>();
                position.put("productType", "FORWARD");
                position.put("contractCode", forward.getContractCode());
                position.put("underlyingAsset", forward.getUnderlyingAsset());
                position.put("positionSize", forward.getNotionalAmount());
                position.put("marketValue", forward.getCurrentValue() != null ? forward.getCurrentValue() : BigDecimal.ZERO);
                position.put("unrealizedPnL", forward.getPnl() != null ? forward.getPnl() : BigDecimal.ZERO);
                position.put("delta", null);
                position.put("gamma", null);
                position.put("maturityDate", forward.getMaturityDate());
                position.put("riskLevel", calculateRiskLevel(forward.getCurrentValue(), forward.getRiskLimit()));
                positionList.add(position);
            }

            // 2. 查询期权交易
            List<TblOptionTransaction> optionList = new ArrayList<>();
            if (productType == null || "OPTION".equals(productType)) {
                QueryWrapper<TblOptionTransaction> optionWrapper = new QueryWrapper<>();
                if (orgId != null) optionWrapper.eq("ORG_ID", orgId);
                optionWrapper.eq("DEL_FLAG", 0);
                optionWrapper.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) {
                    optionWrapper.eq("CURRENCY", currency);
                }
                optionList = optionTransactionService.list(optionWrapper);
            }

            // 转换为统一格式
            for (TblOptionTransaction option : optionList) {
                Map<String, Object> position = new HashMap<>();
                position.put("productType", "OPTION");
                position.put("contractCode", option.getContractCode());
                position.put("underlyingAsset", option.getUnderlyingAsset());
                position.put("positionSize", option.getContractSize());
                position.put("marketValue", option.getTheoreticalValue() != null ? option.getTheoreticalValue() : BigDecimal.ZERO);
                position.put("unrealizedPnL", option.getTheoreticalValue() != null && option.getPremium() != null
                    ? option.getTheoreticalValue().subtract(option.getPremium()) : BigDecimal.ZERO);
                position.put("delta", option.getDelta());
                position.put("gamma", option.getGamma());
                position.put("maturityDate", option.getExpiryDate());
                position.put("riskLevel", "MEDIUM");
                positionList.add(position);
            }

            // 3. 查询期货交易（DEL_FLAG是VARCHAR(1)，STATUS持仓中为OPEN）
            List<TblFuturesTransaction> futuresList = new ArrayList<>();
            if (productType == null || "FUTURES".equals(productType)) {
                QueryWrapper<TblFuturesTransaction> futuresWrapper = new QueryWrapper<>();
                if (orgId != null) futuresWrapper.eq("ORG_ID", orgId);
                futuresWrapper.eq("DEL_FLAG", "0");
                futuresWrapper.in("STATUS", Arrays.asList("PENDING", "OPEN"));
                if (currency != null) {
                    futuresWrapper.eq("CURRENCY", currency);
                }
                futuresList = futuresTransactionService.list(futuresWrapper);
            }

            // 转换为统一格式
            for (TblFuturesTransaction futures : futuresList) {
                Map<String, Object> position = new HashMap<>();
                position.put("productType", "FUTURES");
                position.put("contractCode", futures.getContractCode());
                position.put("underlyingAsset", futures.getUnderlyingAsset());
                position.put("positionSize", futures.getContractSize());
                // 计算市值: 合约数量 * 当前价格
                BigDecimal marketValue = futures.getContractSize() != null && futures.getCurrentPrice() != null
                    ? futures.getContractSize().multiply(futures.getCurrentPrice()) : BigDecimal.ZERO;
                position.put("marketValue", marketValue);
                position.put("unrealizedPnL", futures.getUnrealizedPnl() != null ? futures.getUnrealizedPnl() : BigDecimal.ZERO);
                position.put("delta", null);
                position.put("gamma", null);
                position.put("maturityDate", futures.getExpiryDate());
                position.put("riskLevel", "MEDIUM");
                positionList.add(position);
            }

            // 4. 查询掉期交易（DEL_FLAG是VARCHAR(1)）
            List<TblSwapTransaction> swapList = new ArrayList<>();
            if (productType == null || "SWAP".equals(productType)) {
                QueryWrapper<TblSwapTransaction> swapWrapper = new QueryWrapper<>();
                if (orgId != null) swapWrapper.eq("ORG_ID", orgId);
                swapWrapper.eq("DEL_FLAG", "0");
                swapWrapper.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) {
                    swapWrapper.eq("CURRENCY", currency);
                }
                swapList = swapTransactionService.list(swapWrapper);
            }

            // 转换为统一格式
            for (TblSwapTransaction swap : swapList) {
                Map<String, Object> position = new HashMap<>();
                position.put("productType", "SWAP");
                position.put("contractCode", swap.getContractCode());
                // 使用掉期类型作为基础资产
                position.put("underlyingAsset", swap.getSwapType() != null ? swap.getSwapType() : "INTEREST_RATE");
                position.put("positionSize", swap.getNotionalAmount());
                position.put("marketValue", swap.getCurrentValue() != null ? swap.getCurrentValue() : BigDecimal.ZERO);
                position.put("unrealizedPnL", swap.getCurrentValue() != null ? swap.getCurrentValue() : BigDecimal.ZERO);
                position.put("delta", null);
                position.put("gamma", null);
                position.put("maturityDate", swap.getMaturityDate());
                position.put("riskLevel", "MEDIUM");
                positionList.add(position);
            }

            // 分页处理
            int total = positionList.size();
            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, total);
            List<Map<String, Object>> pagedList = fromIndex >= total
                    ? new ArrayList<>() : positionList.subList(fromIndex, toIndex);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pagedList);
            result.put("totalRecord", total);

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出持仓监控数据
     */
    @GetMapping("/position/export")
    @ApiOperation("导出持仓监控数据")
    public void exportPositionMonitoring(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            String productTypeRaw = params.containsKey("productType") && params.get("productType") != null
                    ? params.get("productType").toString().trim() : "";
            String productType = productTypeRaw.isEmpty() ? null : productTypeRaw;
            String currencyRaw = params.containsKey("currency") && params.get("currency") != null
                    ? params.get("currency").toString().trim() : "";
            String currency = currencyRaw.isEmpty() ? null : currencyRaw;

            String idsStr = params.containsKey("ids") && params.get("ids") != null
                    ? params.get("ids").toString().trim() : "";
            Set<String> selectedIds = new HashSet<>();
            if (!idsStr.isEmpty()) {
                selectedIds.addAll(Arrays.asList(idsStr.split(",")));
            }

            int pageNum = 1, pageSize = 20;
            try {
                if (params.containsKey("pageNum") && params.get("pageNum") != null && !params.get("pageNum").toString().isEmpty())
                    pageNum = Integer.parseInt(params.get("pageNum").toString());
                if (params.containsKey("pageSize") && params.get("pageSize") != null && !params.get("pageSize").toString().isEmpty())
                    pageSize = Integer.parseInt(params.get("pageSize").toString());
            } catch (NumberFormatException ignored) {}

            List<Map<String, Object>> positionList = new ArrayList<>();

            if (productType == null || "FORWARD".equals(productType)) {
                QueryWrapper<TblForwardTransaction> fw = new QueryWrapper<>();
                if (orgId != null) fw.eq("ORG_ID", orgId);
                fw.eq("DEL_FLAG", 0);
                fw.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) fw.eq("CURRENCY", currency);
                for (TblForwardTransaction f : forwardTransactionService.list(fw)) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("productType", "FORWARD"); p.put("contractCode", f.getContractCode());
                    p.put("underlyingAsset", f.getUnderlyingAsset());
                    p.put("positionSize", f.getNotionalAmount());
                    p.put("marketValue", f.getCurrentValue() != null ? f.getCurrentValue() : BigDecimal.ZERO);
                    p.put("unrealizedPnL", f.getPnl() != null ? f.getPnl() : BigDecimal.ZERO);
                    p.put("delta", null); p.put("gamma", null);
                    p.put("maturityDate", f.getMaturityDate());
                    p.put("riskLevel", calculateRiskLevel(f.getCurrentValue(), f.getRiskLimit()));
                    positionList.add(p);
                }
            }
            if (productType == null || "OPTION".equals(productType)) {
                QueryWrapper<TblOptionTransaction> ow = new QueryWrapper<>();
                if (orgId != null) ow.eq("ORG_ID", orgId);
                ow.eq("DEL_FLAG", 0); ow.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) ow.eq("CURRENCY", currency);
                for (TblOptionTransaction o : optionTransactionService.list(ow)) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("productType", "OPTION"); p.put("contractCode", o.getContractCode());
                    p.put("underlyingAsset", o.getUnderlyingAsset());
                    p.put("positionSize", o.getContractSize());
                    p.put("marketValue", o.getTheoreticalValue() != null ? o.getTheoreticalValue() : BigDecimal.ZERO);
                    p.put("unrealizedPnL", o.getTheoreticalValue() != null && o.getPremium() != null
                            ? o.getTheoreticalValue().subtract(o.getPremium()) : BigDecimal.ZERO);
                    p.put("delta", o.getDelta()); p.put("gamma", o.getGamma());
                    p.put("maturityDate", o.getExpiryDate());
                    p.put("riskLevel", "MEDIUM");
                    positionList.add(p);
                }
            }
            if (productType == null || "FUTURES".equals(productType)) {
                QueryWrapper<TblFuturesTransaction> ftw = new QueryWrapper<>();
                if (orgId != null) ftw.eq("ORG_ID", orgId);
                ftw.eq("DEL_FLAG", "0"); ftw.in("STATUS", Arrays.asList("PENDING", "OPEN"));
                if (currency != null) ftw.eq("CURRENCY", currency);
                for (TblFuturesTransaction ft : futuresTransactionService.list(ftw)) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("productType", "FUTURES"); p.put("contractCode", ft.getContractCode());
                    p.put("underlyingAsset", ft.getUnderlyingAsset());
                    p.put("positionSize", ft.getContractSize());
                    BigDecimal mv = ft.getContractSize() != null && ft.getCurrentPrice() != null
                            ? ft.getContractSize().multiply(ft.getCurrentPrice()) : BigDecimal.ZERO;
                    p.put("marketValue", mv);
                    p.put("unrealizedPnL", ft.getUnrealizedPnl() != null ? ft.getUnrealizedPnl() : BigDecimal.ZERO);
                    p.put("delta", null); p.put("gamma", null);
                    p.put("maturityDate", ft.getExpiryDate());
                    p.put("riskLevel", "MEDIUM");
                    positionList.add(p);
                }
            }
            if (productType == null || "SWAP".equals(productType)) {
                QueryWrapper<TblSwapTransaction> sw = new QueryWrapper<>();
                if (orgId != null) sw.eq("ORG_ID", orgId);
                sw.eq("DEL_FLAG", "0"); sw.in("STATUS", Arrays.asList("ACTIVE", "PENDING"));
                if (currency != null) sw.eq("CURRENCY", currency);
                for (TblSwapTransaction s : swapTransactionService.list(sw)) {
                    Map<String, Object> p = new HashMap<>();
                    p.put("productType", "SWAP"); p.put("contractCode", s.getContractCode());
                    p.put("underlyingAsset", s.getSwapType() != null ? s.getSwapType() : "INTEREST_RATE");
                    p.put("positionSize", s.getNotionalAmount());
                    p.put("marketValue", s.getCurrentValue() != null ? s.getCurrentValue() : BigDecimal.ZERO);
                    p.put("unrealizedPnL", s.getCurrentValue() != null ? s.getCurrentValue() : BigDecimal.ZERO);
                    p.put("delta", null); p.put("gamma", null);
                    p.put("maturityDate", s.getMaturityDate());
                    p.put("riskLevel", "MEDIUM");
                    positionList.add(p);
                }
            }

            List<Map<String, Object>> exportList;
            if (!selectedIds.isEmpty()) {
                exportList = new ArrayList<>();
                for (Map<String, Object> pos : positionList) {
                    if (selectedIds.contains(String.valueOf(pos.get("contractCode")))) {
                        exportList.add(pos);
                    }
                }
            } else {
                int fromIndex = (pageNum - 1) * pageSize;
                int toIndex = Math.min(fromIndex + pageSize, positionList.size());
                exportList = fromIndex >= positionList.size() ? new ArrayList<>() : positionList.subList(fromIndex, toIndex);
            }

            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=position_monitoring_" + date + ".csv");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("\uFEFF产品类型,合约编号,标的资产,持仓数量,市场价值,未实现损益,Delta,Gamma,到期日期,风险等级");
            for (Map<String, Object> pos : exportList) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        mapProductType(String.valueOf(pos.get("productType"))),
                        pos.get("contractCode"), pos.get("underlyingAsset"),
                        pos.get("positionSize"), pos.get("marketValue"), pos.get("unrealizedPnL"),
                        pos.get("delta") != null ? pos.get("delta") : "",
                        pos.get("gamma") != null ? pos.get("gamma") : "",
                        pos.get("maturityDate") != null ? pos.get("maturityDate") : "",
                        mapRiskLevel(String.valueOf(pos.get("riskLevel")))));
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String mapProductType(String type) {
        if ("FORWARD".equals(type)) return "远期";
        if ("OPTION".equals(type)) return "期权";
        if ("FUTURES".equals(type)) return "期货";
        if ("SWAP".equals(type)) return "掉期";
        return type != null ? type : "";
    }

    private String mapRiskLevel(String level) {
        if ("LOW".equals(level)) return "低";
        if ("MEDIUM".equals(level)) return "中";
        if ("HIGH".equals(level)) return "高";
        if ("CRITICAL".equals(level)) return "紧急";
        return level != null ? level : "";
    }

    /**
     * 计算风险等级
     */
    private String calculateRiskLevel(BigDecimal currentValue, BigDecimal riskLimit) {
        if (currentValue == null || riskLimit == null || riskLimit.compareTo(BigDecimal.ZERO) == 0) {
            return "LOW";
        }

        BigDecimal ratio = currentValue.divide(riskLimit, 4, BigDecimal.ROUND_HALF_UP);
        if (ratio.compareTo(new BigDecimal("0.5")) < 0) {
            return "LOW";
        } else if (ratio.compareTo(new BigDecimal("0.8")) < 0) {
            return "MEDIUM";
        } else {
            return "HIGH";
        }
    }

    /**
     * 获取风险监控数据
     */
    @GetMapping("/risk")
    @ApiOperation("获取风险监控数据")
    public String getRiskMonitoring() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // 用名义金额（NOTIONAL_AMOUNT）作为风险计算基础，该字段数据库中有实际值
            List<BigDecimal> notionalList = new ArrayList<>();

            QueryWrapper<TblForwardTransaction> fw = new QueryWrapper<>();
            if (orgId != null) fw.eq("ORG_ID", orgId);
            fw.eq("DEL_FLAG", 0);
            List<TblForwardTransaction> forwardAll = forwardTransactionService.list(fw);
            forwardAll.forEach(t -> { if (t.getNotionalAmount() != null) notionalList.add(t.getNotionalAmount()); });

            QueryWrapper<TblOptionTransaction> ow = new QueryWrapper<>();
            if (orgId != null) ow.eq("ORG_ID", orgId);
            ow.eq("DEL_FLAG", 0);
            List<TblOptionTransaction> optionAll = optionTransactionService.list(ow);
            optionAll.forEach(t -> { if (t.getContractSize() != null) notionalList.add(t.getContractSize()); });

            QueryWrapper<TblFuturesTransaction> fuW = new QueryWrapper<>();
            if (orgId != null) fuW.eq("ORG_ID", orgId);
            fuW.eq("DEL_FLAG", "0");
            List<TblFuturesTransaction> futuresAll = futuresTransactionService.list(fuW);
            futuresAll.forEach(t -> { if (t.getContractSize() != null) notionalList.add(t.getContractSize()); });

            QueryWrapper<TblSwapTransaction> sw = new QueryWrapper<>();
            if (orgId != null) sw.eq("ORG_ID", orgId);
            sw.eq("DEL_FLAG", "0");
            List<TblSwapTransaction> swapAll = swapTransactionService.list(sw);
            swapAll.forEach(t -> { if (t.getNotionalAmount() != null) notionalList.add(t.getNotionalAmount()); });

            Map<String, Object> riskData = new HashMap<>();

            if (notionalList.isEmpty()) {
                riskData.put("var95", 0.0);
                riskData.put("varChange", 0.0);
                riskData.put("expectedShortfall", 0.0);
                riskData.put("esChange", 0.0);
                riskData.put("maxDrawdown", 0.0);
                riskData.put("drawdownChange", 0.0);
                riskData.put("pnlDistribution", Collections.emptyList());
                riskData.put("riskDistribution", Collections.emptyList());
            } else {
                List<BigDecimal> sorted = new ArrayList<>(notionalList);
                sorted.sort(Comparator.naturalOrder());
                int n = sorted.size();

                // VaR 95%：取第5%分位数（名义金额的5%尾部风险）
                int varIdx = Math.max(0, (int) Math.floor(n * 0.05));
                BigDecimal var95 = sorted.get(varIdx).multiply(new BigDecimal("0.05"))
                        .setScale(2, java.math.RoundingMode.HALF_UP);

                // 预期损失(ES)：最低5%分位以下的均值 * 风险系数
                BigDecimal esSum = BigDecimal.ZERO;
                for (int i = 0; i <= varIdx; i++) esSum = esSum.add(sorted.get(i));
                BigDecimal es = esSum.divide(new BigDecimal(varIdx + 1), 2, java.math.RoundingMode.HALF_UP)
                        .multiply(new BigDecimal("0.08")).setScale(2, java.math.RoundingMode.HALF_UP);

                // 最大回撤：最大名义金额 - 最小名义金额
                BigDecimal maxDrawdown = sorted.get(n - 1).subtract(sorted.get(0))
                        .abs().setScale(2, java.math.RoundingMode.HALF_UP);

                riskData.put("var95", var95);
                riskData.put("varChange", 0.0);
                riskData.put("expectedShortfall", es);
                riskData.put("esChange", 0.0);
                riskData.put("maxDrawdown", maxDrawdown);
                riskData.put("drawdownChange", 0.0);

                // 名义金额分布（按区间分组）
                riskData.put("pnlDistribution", buildPnlDistribution(sorted));

                // 风险分布：按产品类型汇总名义金额
                List<Map<String, Object>> riskDist = new ArrayList<>();
                BigDecimal fwSum = forwardAll.stream().filter(t -> t.getNotionalAmount() != null)
                        .map(TblForwardTransaction::getNotionalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal optSum = optionAll.stream().filter(t -> t.getContractSize() != null)
                        .map(TblOptionTransaction::getContractSize).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal fuSum = futuresAll.stream().filter(t -> t.getContractSize() != null)
                        .map(TblFuturesTransaction::getContractSize).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal swSum = swapAll.stream().filter(t -> t.getNotionalAmount() != null)
                        .map(TblSwapTransaction::getNotionalAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                addDistItem(riskDist, "远期", fwSum);
                addDistItem(riskDist, "期权", optSum);
                addDistItem(riskDist, "期货", fuSum);
                addDistItem(riskDist, "掉期", swSum);
                riskData.put("riskDistribution", riskDist);
            }

            return new JsonBean(1, "查询成功", riskData).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    private List<Map<String, Object>> buildPnlDistribution(List<BigDecimal> sorted) {
        if (sorted.isEmpty()) return Collections.emptyList();
        BigDecimal min = sorted.get(0), max = sorted.get(sorted.size() - 1);
        BigDecimal range = max.subtract(min);
        int buckets = 8;
        List<Map<String, Object>> result = new ArrayList<>();
        if (range.compareTo(BigDecimal.ZERO) == 0) {
            Map<String, Object> m = new HashMap<>();
            m.put("range", min.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
            m.put("count", sorted.size());
            result.add(m);
            return result;
        }
        BigDecimal step = range.divide(new BigDecimal(buckets), 2, java.math.RoundingMode.HALF_UP);
        for (int i = 0; i < buckets; i++) {
            BigDecimal lo = min.add(step.multiply(new BigDecimal(i)));
            BigDecimal hi = i == buckets - 1 ? max.add(BigDecimal.ONE) : lo.add(step);
            final BigDecimal flo = lo, fhi = hi;
            long cnt = sorted.stream().filter(v -> v.compareTo(flo) >= 0 && v.compareTo(fhi) < 0).count();
            Map<String, Object> m = new HashMap<>();
            m.put("range", lo.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString() + "~" + hi.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
            m.put("count", cnt);
            result.add(m);
        }
        return result;
    }

    private void addDistItem(List<Map<String, Object>> list, String name, BigDecimal value) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("value", value.setScale(2, java.math.RoundingMode.HALF_UP));
        list.add(m);
    }

    /**
     * 获取市场数据
     */
    @GetMapping("/market-data")
    @ApiOperation("获取市场数据")
    public String getMarketData(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // 查询市场数据（TBL_DERIVATIVES_MARKET_DATA无ORG_ID字段，用DELETE_FLAG过滤）
            QueryWrapper<TblDerivativesMarketData> wrapper = new QueryWrapper<>();
            wrapper.eq("DELETE_FLAG", 0);

            // 资产类型过滤（字段名是PRODUCT_TYPE）
            if (params.containsKey("assetType") && params.get("assetType") != null) {
                wrapper.eq("PRODUCT_TYPE", params.get("assetType"));
            }

            wrapper.orderByDesc("DATA_TIME");

            List<TblDerivativesMarketData> marketDataList = marketDataService.list(wrapper);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", marketDataList);
            result.put("totalRecord", marketDataList.size());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取监控预警列表
     */
    @GetMapping("/alert")
    @ApiOperation("获取监控预警列表")
    public String getAlerts(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 构建查询条件（不按 ORG_ID 过滤，避免新增时 orgId 为 null 导致查不到）
            QueryWrapper<TblDerivativesAlert> wrapper = new QueryWrapper<>();
            wrapper.eq("DEL_FLAG", 0);

            // 预警类型
            String alertType = params.containsKey("alertType") ? String.valueOf(params.get("alertType")) : null;
            if (alertType != null && !alertType.trim().isEmpty() && !"null".equals(alertType)) {
                wrapper.eq("ALERT_TYPE", alertType.trim());
            }

            // 预警级别
            String alertLevel = params.containsKey("alertLevel") ? String.valueOf(params.get("alertLevel")) : null;
            if (alertLevel != null && !alertLevel.trim().isEmpty() && !"null".equals(alertLevel)) {
                wrapper.eq("ALERT_LEVEL", alertLevel.trim());
            }

            // 状态
            String status = params.containsKey("status") ? String.valueOf(params.get("status")) : null;
            if (status != null && !status.trim().isEmpty() && !"null".equals(status)) {
                wrapper.eq("STATUS", status.trim());
            }

            wrapper.orderByDesc("CREATE_TIME");

            List<TblDerivativesAlert> alertList = alertService.list(wrapper);

            // 分页处理
            int pageNum = 1, pageSize = 20;
            if (params.containsKey("pageNum")) { try { pageNum = Integer.parseInt(params.get("pageNum").toString()); } catch (Exception ignored) {} }
            if (params.containsKey("pageSize")) { try { pageSize = Integer.parseInt(params.get("pageSize").toString()); } catch (Exception ignored) {} }
            int total = alertList.size();
            int fromIdx = (pageNum - 1) * pageSize;
            int toIdx = Math.min(fromIdx + pageSize, total);
            List<TblDerivativesAlert> pageList = fromIdx >= total ? Collections.emptyList() : alertList.subList(fromIdx, toIdx);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageList);
            result.put("totalRecord", total);

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 设置监控预警
     */
    @PostMapping("/alert")
    @ApiOperation("设置监控预警")
    public String setAlert(@FlexibleRequestBody TblDerivativesAlert alert) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置创建信息
            alert.setCreateTime(new Date());
            alert.setUpdateTime(new Date());
            alert.setCreateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            alert.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            alert.setOrgId(org != null ? org.getOrgid().longValue() : null);
            alert.setDelFlag(0);

            // 初始状态为活跃
            if (alert.getStatus() == null || alert.getStatus().isEmpty()) {
                alert.setStatus("ACTIVE");
            }

            // 如果没有触发时间，始终设置当前时间（用于排序）
            if (alert.getTriggerTime() == null) {
                alert.setTriggerTime(new Date());
            }

            boolean success = alertService.save(alert);
            if (success) {
                return new JsonBean(1, "设置成功", alert).toJson();
            } else {
                return new JsonBean(0, "设置失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "设置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改监控预警
     */
    @PutMapping("/alert")
    @ApiOperation("修改监控预警")
    public String updateAlert(@FlexibleRequestBody TblDerivativesAlert alert) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (alert.getAlertId() == null) {
                return new JsonBean(0, "预警ID不能为空", null).toJson();
            }

            // 设置更新信息
            alert.setUpdateTime(new Date());
            alert.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);

            boolean success = alertService.updateById(alert);
            if (success) {
                return new JsonBean(1, "修改成功", alert).toJson();
            } else {
                return new JsonBean(0, "修改失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除监控预警
     */
    @DeleteMapping("/alert/{alertIds}")
    @ApiOperation("删除监控预警")
    public String deleteAlert(@PathVariable Long[] alertIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 逻辑删除
            for (Long alertId : alertIds) {
                TblDerivativesAlert alert = alertService.getById(alertId);
                if (alert != null) {
                    alert.setDelFlag(1);
                    alert.setUpdateTime(new Date());
                    alert.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
                    alertService.updateById(alert);
                }
            }

            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 处理预警
     */
    @PostMapping("/alert/{alertId}/process")
    @ApiOperation("处理预警")
    public String processAlert(@PathVariable Long alertId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblDerivativesAlert alert = alertService.getById(alertId);
            if (alert == null) {
                return new JsonBean(0, "未找到该预警记录", null).toJson();
            }

            // 更新状态为已处理
            alert.setStatus("PROCESSED");
            alert.setUpdateTime(new Date());
            alert.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            alertService.updateById(alert);

            return new JsonBean(1, "处理成功", alert).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 忽略预警
     */
    @PostMapping("/alert/{alertId}/ignore")
    @ApiOperation("忽略预警")
    public String ignoreAlert(@PathVariable Long alertId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblDerivativesAlert alert = alertService.getById(alertId);
            if (alert == null) {
                return new JsonBean(0, "未找到该预警记录", null).toJson();
            }

            // 更新状态为已忽略
            alert.setStatus("IGNORED");
            alert.setUpdateTime(new Date());
            alert.setUpdateBy(loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null);
            alertService.updateById(alert);

            return new JsonBean(1, "忽略成功", alert).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "忽略失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出预警管理数据
     */
    @GetMapping("/alert/export")
    @ApiOperation("导出预警管理数据")
    public void exportAlert(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }

            String idsStr = params.containsKey("ids") && params.get("ids") != null
                    ? params.get("ids").toString().trim() : "";
            Set<Long> selectedIds = new HashSet<>();
            if (!idsStr.isEmpty()) {
                for (String id : idsStr.split(",")) {
                    try { selectedIds.add(Long.parseLong(id.trim())); } catch (NumberFormatException ignored) {}
                }
            }

            int pageNum = 1, pageSize = 20;
            try {
                if (params.containsKey("pageNum") && params.get("pageNum") != null && !params.get("pageNum").toString().isEmpty())
                    pageNum = Integer.parseInt(params.get("pageNum").toString());
                if (params.containsKey("pageSize") && params.get("pageSize") != null && !params.get("pageSize").toString().isEmpty())
                    pageSize = Integer.parseInt(params.get("pageSize").toString());
            } catch (NumberFormatException ignored) {}

            List<TblDerivativesAlert> exportList;
            if (!selectedIds.isEmpty()) {
                exportList = new ArrayList<>(alertService.listByIds(selectedIds));
            } else {
                QueryWrapper<TblDerivativesAlert> wrapper = new QueryWrapper<>();
                wrapper.eq("DEL_FLAG", 0);
                wrapper.orderByDesc("CREATE_TIME");
                List<TblDerivativesAlert> allList = alertService.list(wrapper);
                int fromIndex = (pageNum - 1) * pageSize;
                int toIndex = Math.min(fromIndex + pageSize, allList.size());
                exportList = fromIndex >= allList.size() ? new ArrayList<>() : allList.subList(fromIndex, toIndex);
            }

            String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=alert_monitoring_" + date + ".csv");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            writer.println("\uFEFF预警名称,预警类型,监控指标,阈值,当前值,预警级别,状态,触发时间");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (TblDerivativesAlert a : exportList) {
                writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                        a.getAlertName() != null ? a.getAlertName() : "",
                        mapAlertType(a.getAlertType()),
                        a.getMonitoringMetric() != null ? a.getMonitoringMetric() : "",
                        a.getThreshold() != null ? a.getThreshold() : "",
                        a.getCurrentValue() != null ? a.getCurrentValue() : "",
                        mapAlertLevel(a.getAlertLevel()),
                        mapAlertStatus(a.getStatus()),
                        a.getTriggerTime() != null ? sdf.format(a.getTriggerTime()) : ""));
            }
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String mapAlertType(String type) {
        if ("PRICE_ALERT".equals(type)) return "价格预警";
        if ("RISK_ALERT".equals(type)) return "风险预警";
        if ("POSITION_ALERT".equals(type)) return "持仓预警";
        if ("EXPIRY_ALERT".equals(type)) return "到期预警";
        return type != null ? type : "";
    }

    private String mapAlertLevel(String level) {
        if ("LOW".equals(level)) return "低";
        if ("MEDIUM".equals(level)) return "中";
        if ("HIGH".equals(level)) return "高";
        if ("CRITICAL".equals(level)) return "紧急";
        return level != null ? level : "";
    }

    private String mapAlertStatus(String status) {
        if ("TRIGGERED".equals(status)) return "已触发";
        if ("PROCESSED".equals(status)) return "已处理";
        if ("IGNORED".equals(status)) return "已忽略";
        if ("ACTIVE".equals(status)) return "活跃";
        return status != null ? status : "";
    }

    /**
     * 获取风险限额数据
     */
    @GetMapping({"/risk/limits", "/monitoring/risk/limits"})
    @ApiOperation("获取风险限额数据")
    public String getRiskLimits() {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // TODO: 从数据库查询实际的风险限额数据
            List<Map<String, Object>> limitsList = new ArrayList<>();

            // 模拟数据 - 风险限额信息
            Map<String, Object> limit1 = new HashMap<>();
            limit1.put("limitType", "总风险限额");
            limit1.put("limitAmount", 50000000.00);
            limit1.put("usedAmount", 32500000.00);
            limit1.put("availableAmount", 17500000.00);
            limit1.put("usageRatio", 0.65);
            limit1.put("currency", "CNY");
            limit1.put("status", "NORMAL");
            limitsList.add(limit1);

            Map<String, Object> limit2 = new HashMap<>();
            limit2.put("limitType", "远期交易限额");
            limit2.put("limitAmount", 20000000.00);
            limit2.put("usedAmount", 15000000.00);
            limit2.put("availableAmount", 5000000.00);
            limit2.put("usageRatio", 0.75);
            limit2.put("currency", "CNY");
            limit2.put("status", "WARNING");
            limitsList.add(limit2);

            Map<String, Object> limit3 = new HashMap<>();
            limit3.put("limitType", "期货交易限额");
            limit3.put("limitAmount", 15000000.00);
            limit3.put("usedAmount", 8000000.00);
            limit3.put("availableAmount", 7000000.00);
            limit3.put("usageRatio", 0.53);
            limit3.put("currency", "CNY");
            limit3.put("status", "NORMAL");
            limitsList.add(limit3);

            Map<String, Object> limit4 = new HashMap<>();
            limit4.put("limitType", "期权交易限额");
            limit4.put("limitAmount", 10000000.00);
            limit4.put("usedAmount", 6000000.00);
            limit4.put("availableAmount", 4000000.00);
            limit4.put("usageRatio", 0.60);
            limit4.put("currency", "CNY");
            limit4.put("status", "NORMAL");
            limitsList.add(limit4);

            Map<String, Object> limit5 = new HashMap<>();
            limit5.put("limitType", "互换交易限额");
            limit5.put("limitAmount", 5000000.00);
            limit5.put("usedAmount", 3500000.00);
            limit5.put("availableAmount", 1500000.00);
            limit5.put("usageRatio", 0.70);
            limit5.put("currency", "CNY");
            limit5.put("status", "NORMAL");
            limitsList.add(limit5);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", limitsList);
            result.put("totalRecord", limitsList.size());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取VaR(风险价值)数据
     */
    @RequestMapping(value = {"/risk/var", "/monitoring/risk/var"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取VaR数据")
    public String getVaRData(@RequestParam(required = false) Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // TODO: 从数据库查询实际的VaR数据
            Map<String, Object> varData = new HashMap<>();

            // VaR数据 - 模拟数据
            varData.put("var95", 850000.00);  // 95%置信度VaR
            varData.put("var99", 1250000.00);  // 99%置信度VaR
            varData.put("varChange", -25000.00);  // 较昨日变化
            varData.put("varChangeRatio", -0.0286);  // 变化比例

            // 预期损失(Expected Shortfall)
            varData.put("es95", 1100000.00);  // 95%置信度ES
            varData.put("es99", 1500000.00);  // 99%置信度ES
            varData.put("esChange", -30000.00);  // 较昨日变化

            // 历史VaR趋势
            List<Map<String, Object>> varTrend = new ArrayList<>();
            String[] dates = {"2026-01-15", "2026-01-16", "2026-01-17", "2026-01-18", "2026-01-19", "2026-01-20", "2026-01-21"};
            double[] var95Values = {920000, 895000, 880000, 865000, 870000, 875000, 850000};
            double[] var99Values = {1350000, 1310000, 1290000, 1270000, 1280000, 1290000, 1250000};

            for (int i = 0; i < dates.length; i++) {
                Map<String, Object> trendPoint = new HashMap<>();
                trendPoint.put("date", dates[i]);
                trendPoint.put("var95", var95Values[i]);
                trendPoint.put("var99", var99Values[i]);
                varTrend.add(trendPoint);
            }
            varData.put("varTrend", varTrend);

            // 分币种VaR
            List<Map<String, Object>> varByCurrency = new ArrayList<>();
            Map<String, Object> varCNY = new HashMap<>();
            varCNY.put("currency", "CNY");
            varCNY.put("var95", 650000.00);
            varCNY.put("var99", 950000.00);
            varByCurrency.add(varCNY);

            Map<String, Object> varUSD = new HashMap<>();
            varUSD.put("currency", "USD");
            varUSD.put("var95", 150000.00);
            varUSD.put("var99", 220000.00);
            varByCurrency.add(varUSD);

            Map<String, Object> varEUR = new HashMap<>();
            varEUR.put("currency", "EUR");
            varEUR.put("var95", 50000.00);
            varEUR.put("var99", 80000.00);
            varByCurrency.add(varEUR);

            varData.put("varByCurrency", varByCurrency);

            return new JsonBean(1, "查询成功", varData).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取压力测试数据
     */
    @RequestMapping(value = {"/risk/stress-test", "/monitoring/risk/stress-test"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取压力测试数据")
    public String getStressTestData(@RequestParam(required = false) Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            Long orgId = org != null ? org.getOrgid().longValue() : null;

            // TODO: 从数据库查询实际的压力测试数据
            List<Map<String, Object>> stressTestList = new ArrayList<>();

            // 压力测试场景1: 汇率剧烈波动
            Map<String, Object> scenario1 = new HashMap<>();
            scenario1.put("scenarioName", "汇率剧烈波动");
            scenario1.put("scenarioDesc", "美元/人民币汇率波动超过10%");
            scenario1.put("portfolioValue", 32500000.00);
            scenario1.put("stressValue", 28500000.00);
            scenario1.put("loss", -4000000.00);
            scenario1.put("lossRatio", -0.1231);
            scenario1.put("severity", "HIGH");
            scenario1.put("status", "TRIGGERED");
            stressTestList.add(scenario1);

            // 压力测试场景2: 利率大幅上升
            Map<String, Object> scenario2 = new HashMap<>();
            scenario2.put("scenarioName", "利率大幅上升");
            scenario2.put("scenarioDesc", "SHIBOR上升200BP");
            scenario2.put("portfolioValue", 32500000.00);
            scenario2.put("stressValue", 30800000.00);
            scenario2.put("loss", -1700000.00);
            scenario2.put("lossRatio", -0.0523);
            scenario2.put("severity", "MEDIUM");
            scenario2.put("status", "NORMAL");
            stressTestList.add(scenario2);

            // 压力测试场景3: 股市崩盘
            Map<String, Object> scenario3 = new HashMap<>();
            scenario3.put("scenarioName", "股市崩盘");
            scenario3.put("scenarioDesc", "沪深300指数下跌20%");
            scenario3.put("portfolioValue", 32500000.00);
            scenario3.put("stressValue", 29200000.00);
            scenario3.put("loss", -3300000.00);
            scenario3.put("lossRatio", -0.1015);
            scenario3.put("severity", "HIGH");
            scenario3.put("status", "WARNING");
            stressTestList.add(scenario3);

            // 压力测试场景4: 商品价格暴跌
            Map<String, Object> scenario4 = new HashMap<>();
            scenario4.put("scenarioName", "商品价格暴跌");
            scenario4.put("scenarioDesc", "黄金和原油价格下跌15%");
            scenario4.put("portfolioValue", 32500000.00);
            scenario4.put("stressValue", 31200000.00);
            scenario4.put("loss", -1300000.00);
            scenario4.put("lossRatio", -0.0400);
            scenario4.put("severity", "MEDIUM");
            scenario4.put("status", "NORMAL");
            stressTestList.add(scenario4);

            // 压力测试场景5: 流动性危机
            Map<String, Object> scenario5 = new HashMap<>();
            scenario5.put("scenarioName", "流动性危机");
            scenario5.put("scenarioDesc", "市场流动性大幅下降,买卖价差扩大300%");
            scenario5.put("portfolioValue", 32500000.00);
            scenario5.put("stressValue", 27500000.00);
            scenario5.put("loss", -5000000.00);
            scenario5.put("lossRatio", -0.1538);
            scenario5.put("severity", "CRITICAL");
            scenario5.put("status", "ALERT");
            stressTestList.add(scenario5);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", stressTestList);
            result.put("totalRecord", stressTestList.size());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
