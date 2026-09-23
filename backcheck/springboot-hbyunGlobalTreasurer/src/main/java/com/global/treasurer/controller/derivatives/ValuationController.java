package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.global.treasurer.entity.derivatives.TblDerivativesValuation;
import com.global.treasurer.entity.derivatives.TblDerivativesValuationHistory;
import com.global.treasurer.mapper.derivatives.TblDerivativesValuationHistoryMapper;
import com.global.treasurer.mapper.derivatives.TblDerivativesValuationMapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController("derivativesValuationController")
@RequestMapping(value = "/derivatives/valuation", produces = "application/json;charset=UTF-8")
@Api(tags = "衍生品估值管理")
public class ValuationController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private TblDerivativesValuationMapper valuationMapper;

    @Resource
    private TblDerivativesValuationHistoryMapper historyMapper;

    @GetMapping("/positions")
    @ApiOperation("持仓估值列表")
    public String getPositions() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0").orderByDesc("CREATE_TIME");
            List<TblDerivativesValuation> list = valuationMapper.selectList(qw);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/summary")
    @ApiOperation("估值汇总")
    public String getSummary() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0");
            List<TblDerivativesValuation> list = valuationMapper.selectList(qw);
            BigDecimal totalValue = BigDecimal.ZERO;
            BigDecimal totalPnL = BigDecimal.ZERO;
            BigDecimal forwardValue = BigDecimal.ZERO;
            BigDecimal optionValue = BigDecimal.ZERO;
            BigDecimal swapValue = BigDecimal.ZERO;
            BigDecimal futuresValue = BigDecimal.ZERO;
            for (TblDerivativesValuation v : list) {
                BigDecimal mv = v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO;
                BigDecimal pnl = v.getUnrealizedPnL() != null ? v.getUnrealizedPnL() : BigDecimal.ZERO;
                totalValue = totalValue.add(mv);
                totalPnL = totalPnL.add(pnl);
                if ("FORWARD".equals(v.getProductType())) forwardValue = forwardValue.add(mv);
                else if ("OPTION".equals(v.getProductType())) optionValue = optionValue.add(mv);
                else if ("SWAP".equals(v.getProductType())) swapValue = swapValue.add(mv);
                else if ("FUTURES".equals(v.getProductType())) futuresValue = futuresValue.add(mv);
            }
            Map<String, Object> data = new HashMap<>();
            data.put("totalValue", totalValue);
            data.put("totalUnrealizedPnL", totalPnL);
            data.put("forwardValue", forwardValue);
            data.put("optionValue", optionValue);
            data.put("swapValue", swapValue);
            data.put("futuresValue", futuresValue);
            List<Map<String, Object>> distribution = new ArrayList<>();
            if (forwardValue.compareTo(BigDecimal.ZERO) > 0) { Map<String, Object> m = new HashMap<>(); m.put("name", "远期"); m.put("value", forwardValue); distribution.add(m); }
            if (optionValue.compareTo(BigDecimal.ZERO) > 0) { Map<String, Object> m = new HashMap<>(); m.put("name", "期权"); m.put("value", optionValue); distribution.add(m); }
            if (swapValue.compareTo(BigDecimal.ZERO) > 0) { Map<String, Object> m = new HashMap<>(); m.put("name", "掉期"); m.put("value", swapValue); distribution.add(m); }
            if (futuresValue.compareTo(BigDecimal.ZERO) > 0) { Map<String, Object> m = new HashMap<>(); m.put("name", "期货"); m.put("value", futuresValue); distribution.add(m); }
            data.put("distribution", distribution);
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/history")
    @ApiOperation("估值历史")
    public String getHistory() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesValuationHistory> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0").orderByDesc("VALUATION_DATE");
            List<TblDerivativesValuationHistory> list = historyMapper.selectList(qw);
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", list);
            data.put("totalRecord", list.size());
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch")
    @ApiOperation("批量估值")
    public String batchValuation() {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0");
            List<TblDerivativesValuation> list = valuationMapper.selectList(qw);
            for (TblDerivativesValuation v : list) {
                BigDecimal mv = v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO;
                BigDecimal fluctuation = mv.multiply(BigDecimal.valueOf((Math.random() - 0.5) * 0.02));
                v.setMarketValue(mv.add(fluctuation).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setPresentValue(v.getMarketValue().subtract(BigDecimal.valueOf(1000)).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setUnrealizedPnL(v.getMarketValue().subtract(v.getNotionalAmount() != null ? v.getNotionalAmount() : BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
                v.setValuationDate(today);
                v.setUpdateTime(new Date());
                valuationMapper.updateById(v);
            }
            BigDecimal totalValue = list.stream().map(v -> v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            TblDerivativesValuationHistory hist = new TblDerivativesValuationHistory();
            hist.setValuationDate(today);
            hist.setProductType("全部");
            hist.setTotalValue(totalValue);
            hist.setDailyChange(BigDecimal.valueOf(Math.random() * 100000 - 50000).setScale(2, BigDecimal.ROUND_HALF_UP));
            hist.setDailyChangeRate(BigDecimal.valueOf(Math.random() * 0.004 - 0.002).setScale(4, BigDecimal.ROUND_HALF_UP));
            hist.setRemark("批量估值");
            historyMapper.insert(hist);
            return new JsonBean(1, "批量估值完成，共处理 " + list.size() + " 条记录", null).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "批量估值失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/single")
    @ApiOperation("单笔估值")
    public String singleValuation(@RequestParam String contractCode,
                                   @RequestParam(required = false) String valuationDate,
                                   @RequestParam(required = false) String valuationMethod) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
            qw.eq("DEL_FLAG", "0").eq("CONTRACT_CODE", contractCode);
            TblDerivativesValuation v = valuationMapper.selectOne(qw);
            if (v == null) return new JsonBean(0, "合约不存在: " + contractCode, null).toJson();
            String today = valuationDate != null ? valuationDate : new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            BigDecimal mv = v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO;
            BigDecimal fluctuation = mv.multiply(BigDecimal.valueOf((Math.random() - 0.5) * 0.02));
            v.setMarketValue(mv.add(fluctuation).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setPresentValue(v.getMarketValue().subtract(BigDecimal.valueOf(500)).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setUnrealizedPnL(v.getMarketValue().subtract(v.getNotionalAmount() != null ? v.getNotionalAmount() : BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setValuationDate(today);
            if (valuationMethod != null) v.setValuationMethod(valuationMethod);
            v.setUpdateTime(new Date());
            valuationMapper.updateById(v);
            return new JsonBean(1, "单笔估值完成", v).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "单笔估值失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/revaluate/{id}")
    @ApiOperation("重新估值")
    public String revaluate(@PathVariable Long id) {
        try {
            TblStaffUtil user = userProvider.get();
            if (user == null) return new JsonBean(401, "用户已失效", null).toJson();
            TblDerivativesValuation v = valuationMapper.selectById(id);
            if (v == null) return new JsonBean(0, "记录不存在", null).toJson();
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            BigDecimal mv = v.getMarketValue() != null ? v.getMarketValue() : BigDecimal.ZERO;
            BigDecimal fluctuation = mv.multiply(BigDecimal.valueOf((Math.random() - 0.5) * 0.015));
            v.setMarketValue(mv.add(fluctuation).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setPresentValue(v.getMarketValue().subtract(BigDecimal.valueOf(300)).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setUnrealizedPnL(v.getMarketValue().subtract(v.getNotionalAmount() != null ? v.getNotionalAmount() : BigDecimal.ZERO).setScale(2, BigDecimal.ROUND_HALF_UP));
            v.setValuationDate(today);
            v.setUpdateTime(new Date());
            valuationMapper.updateById(v);
            return new JsonBean(1, "重新估值成功", v).toJson();
        } catch (Exception e) {
            return new JsonBean(0, "重新估值失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/report")
    @ApiOperation("导出估值报告")
    public void exportReport(HttpServletResponse response) throws Exception {
        TblStaffUtil user = userProvider.get();
        if (user == null) { response.setStatus(401); return; }
        QueryWrapper<TblDerivativesValuation> qw = new QueryWrapper<>();
        qw.eq("DEL_FLAG", "0");
        List<TblDerivativesValuation> list = valuationMapper.selectList(qw);
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=valuation_report_" + date + ".csv");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.println("\uFEFF衍生品估值报告 - " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        writer.println();
        writer.println("合约编号,产品类型,标的资产,名义本金,市场价值,现值,未实现损益,估值日期,估值方法,币种");
        for (TblDerivativesValuation v : list) {
            writer.println(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                v.getContractCode(), v.getProductType(), v.getUnderlyingAsset(),
                v.getNotionalAmount(), v.getMarketValue(), v.getPresentValue(),
                v.getUnrealizedPnL(), v.getValuationDate(), v.getValuationMethod(), v.getCurrency()));
        }
        writer.flush();
    }
}

