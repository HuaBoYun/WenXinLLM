package com.global.treasurer.controller.derivatives;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.derivatives.TblOptionTransaction;
import com.global.treasurer.service.derivatives.IOptionTransactionService;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 期权交易控制器
 *
 * @author AI Developer
 * @date 2026-01-21
 */
@RestController
@RequestMapping("/derivatives/option")
@Api(tags = "期权交易管理")
public class OptionTransactionController {
    @Resource
    private IOptionTransactionService optionTransactionService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询期权交易列表
     */
    @GetMapping("/page")
    @ApiOperation("分页查询期权交易列表")
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
            QueryWrapper<TblOptionTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 期权类型
            if (params.containsKey("optionType") && !StringUtils.isEmpty(params.get("optionType"))) {
                queryWrapper.eq("OPTION_TYPE", params.get("optionType"));
            }

            // 期权风格
            if (params.containsKey("optionStyle") && !StringUtils.isEmpty(params.get("optionStyle"))) {
                queryWrapper.eq("OPTION_STYLE", params.get("optionStyle"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 币种
            if (params.containsKey("currency") && !StringUtils.isEmpty(params.get("currency"))) {
                queryWrapper.eq("CURRENCY", params.get("currency"));
            }

            // 标的资产
            if (params.containsKey("underlyingAsset") && !StringUtils.isEmpty(params.get("underlyingAsset"))) {
                queryWrapper.like("UNDERLYING_ASSET", params.get("underlyingAsset"));
            }

            // 到期日期范围
            if (params.containsKey("expiryDateStart") && !StringUtils.isEmpty(params.get("expiryDateStart"))) {
                queryWrapper.ge("EXPIRY_DATE", params.get("expiryDateStart"));
            }
            if (params.containsKey("expiryDateEnd") && !StringUtils.isEmpty(params.get("expiryDateEnd"))) {
                queryWrapper.le("EXPIRY_DATE", params.get("expiryDateEnd"));
            }

            // 机构ID (临时注释，用于调试)
            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            // if (org != null) {
            //     queryWrapper.eq("ORG_ID", org.getOrgid());
            // }
            // 调试：打印当前用户的机构ID
            System.out.println("当前用户机构ID: " + (org != null ? org.getOrgid() : "null"));

            // 删除标志
            queryWrapper.eq("DEL_FLAG", 0);

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TblOptionTransaction> page = new Page<>(current, size);
            IPage<TblOptionTransaction> pageList = optionTransactionService.page(page, queryWrapper);

            // 构造返回结果
            Map<String, Object> result = new HashMap<>();
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
     * 查询期权交易列表
     */
    @GetMapping("/list")
    @ApiOperation("查询期权交易列表")
    public String list(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 构建查询条件
            QueryWrapper<TblOptionTransaction> queryWrapper = new QueryWrapper<>();

            // 合约编号
            if (params.containsKey("contractCode") && !StringUtils.isEmpty(params.get("contractCode"))) {
                queryWrapper.like("CONTRACT_CODE", params.get("contractCode"));
            }

            // 期权类型
            if (params.containsKey("optionType") && !StringUtils.isEmpty(params.get("optionType"))) {
                queryWrapper.eq("OPTION_TYPE", params.get("optionType"));
            }

            // 期权风格
            if (params.containsKey("optionStyle") && !StringUtils.isEmpty(params.get("optionStyle"))) {
                queryWrapper.eq("OPTION_STYLE", params.get("optionStyle"));
            }

            // 状态
            if (params.containsKey("status") && !StringUtils.isEmpty(params.get("status"))) {
                queryWrapper.eq("STATUS", params.get("status"));
            }

            // 机构ID (临时注释，用于测试)
            // TblOrganizationUtil org = loginStaff.getCurrentOrg();
            // if (org != null) {
            //     queryWrapper.eq("ORG_ID", org.getOrgid());
            // }

            // 删除标志
            queryWrapper.eq("DEL_FLAG", 0);

            // 按创建时间倒序
            queryWrapper.orderByDesc("CREATE_TIME");

            // 查询列表
            java.util.List<TblOptionTransaction> list = optionTransactionService.list(queryWrapper);

            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取期权交易详细信息
     */
    @GetMapping("/{transactionId}")
    @ApiOperation("获取期权交易详细信息")
    public String getInfo(@ApiParam("期权交易ID") @PathVariable("transactionId") Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
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
     * 新增期权交易
     */
    @PostMapping
    @ApiOperation("新增期权交易")
    public String add(@FlexibleRequestBody TblOptionTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置创建信息
            transaction.setCreateTime(new Date());
            transaction.setUpdateTime(new Date());
            transaction.setCreateBy(loginStaff.getStaffid().longValue());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());
            TblOrganizationUtil org = loginStaff.getCurrentOrg();
            transaction.setOrgId(org != null ? org.getOrgid().longValue() : null);
            transaction.setDelFlag(0);

            // 初始状态为待生效
            if (StringUtils.isEmpty(transaction.getStatus())) {
                transaction.setStatus("PENDING");
            }

            // 保存
            boolean success = optionTransactionService.save(transaction);
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
     * 修改期权交易
     */
    @PutMapping
    @ApiOperation("修改期权交易")
    public String edit(@FlexibleRequestBody TblOptionTransaction transaction) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (transaction.getTransactionId() == null) {
                return new JsonBean(0, "交易ID不能为空", null).toJson();
            }

            // 设置更新信息
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            // 更新
            boolean success = optionTransactionService.updateById(transaction);
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
     * 删除期权交易
     */
    @DeleteMapping("/{transactionIds}")
    @ApiOperation("删除期权交易")
    public String remove(@ApiParam("期权交易ID数组") @PathVariable Long[] transactionIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 逻辑删除
            for (Long transactionId : transactionIds) {
                TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
                if (transaction != null) {
                    transaction.setDelFlag(1);
                    transaction.setUpdateTime(new Date());
                    transaction.setUpdateBy(loginStaff.getStaffid().longValue());
                    optionTransactionService.updateById(transaction);
                }
            }

            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除期权交易（POST方式，支持form-data和JSON body）
     */
    @PostMapping("/delete")
    @ApiOperation("批量删除期权交易")
    public String deleteBatch(
        @ApiParam("期权交易ID数组") @RequestParam(value = "transactionIds", required = false) List<Long> transactionIdsList,
        javax.servlet.http.HttpServletRequest request) {
        try {
            // 调试：打印请求信息
            System.out.println("=============================================");
            System.out.println("=== deleteBatch 方法被调用 ===");
            System.out.println("transactionIdsList: " + transactionIdsList);

            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (transactionIdsList == null || transactionIdsList.isEmpty()) {
                System.out.println("=== transactionIdsList 为空，无法删除 ===");
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            System.out.println("=== 最终提取的ID列表: " + transactionIdsList);

            // 逻辑删除
            for (Long transactionId : transactionIdsList) {
                TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
                if (transaction != null) {
                    transaction.setDelFlag(1);
                    transaction.setUpdateTime(new Date());
                    transaction.setUpdateBy(loginStaff.getStaffid().longValue());
                    optionTransactionService.updateById(transaction);
                }
            }

            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 期权定价计算
     */
    @PostMapping("/pricing")
    @ApiOperation("期权定价计算")
    public String calculatePrice(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取定价参数
            String pricingModel = (String) params.getOrDefault("pricingModel", "BLACK_SCHOLES");
            String optionType = (String) params.get("optionType"); // CALL 或 PUT
            Double underlyingPrice = Double.parseDouble(params.getOrDefault("underlyingPrice", "0").toString());
            Double strikePrice = Double.parseDouble(params.getOrDefault("strikePrice", "0").toString());
            Double riskFreeRate = Double.parseDouble(params.getOrDefault("riskFreeRate", "0").toString());
            Double volatility = Double.parseDouble(params.getOrDefault("volatility", "0").toString());
            Double timeToExpiry = Double.parseDouble(params.getOrDefault("timeToExpiry", "0").toString());
            Double dividendYield = Double.parseDouble(params.getOrDefault("dividendYield", "0").toString());

            // 计算期权价格和希腊字母
            Map<String, Object> result = calculateOptionGreeks(
                optionType, underlyingPrice, strikePrice, riskFreeRate,
                volatility, timeToExpiry, dividendYield
            );

            result.put("pricingModel", pricingModel);
            result.put("optionType", optionType);

            return new JsonBean(1, "计算成功", result).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "计算失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 期权行权
     */
    @PostMapping("/{transactionId}/exercise")
    @ApiOperation("期权行权")
    public String exercise(
        @ApiParam("期权交易ID") @PathVariable Long transactionId,
        @RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 检查状态
            if (!"ACTIVE".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有生效中的期权才能行权", null).toJson();
            }

            // 更新状态为已行权
            transaction.setStatus("EXERCISED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());
            optionTransactionService.updateById(transaction);

            return new JsonBean(1, "行权成功", transaction).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "行权失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 期权到期处理
     */
    @PostMapping("/{transactionId}/expire")
    @ApiOperation("期权到期处理")
    public String expire(@ApiParam("期权交易ID") @PathVariable Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 检查状态
            if (!"ACTIVE".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有生效中的期权才能到期处理", null).toJson();
            }

            // 更新状态为已到期
            transaction.setStatus("EXPIRED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());
            optionTransactionService.updateById(transaction);

            return new JsonBean(1, "到期处理成功", transaction).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "到期处理失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 计算期权希腊字母
     */
    @GetMapping("/{transactionId}/greeks")
    @ApiOperation("计算期权希腊字母")
    public String calculateGreeks(@ApiParam("期权交易ID") @PathVariable Long transactionId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 计算希腊字母
            Map<String, Object> greeks = calculateOptionGreeks(
                transaction.getOptionType(),
                transaction.getUnderlyingPrice() != null ? transaction.getUnderlyingPrice().doubleValue() : 0,
                transaction.getStrikePrice() != null ? transaction.getStrikePrice().doubleValue() : 0,
                transaction.getRiskFreeRate() != null ? transaction.getRiskFreeRate().doubleValue() : 0,
                transaction.getVolatility() != null ? transaction.getVolatility().doubleValue() : 0,
                calculateTimeToExpiry(transaction.getExpiryDate()),
                transaction.getDividendYield() != null ? transaction.getDividendYield().doubleValue() : 0
            );

            // 更新希腊字母到数据库
            transaction.setDelta(new BigDecimal(greeks.get("delta").toString()));
            transaction.setGamma(new BigDecimal(greeks.get("gamma").toString()));
            transaction.setTheta(new BigDecimal(greeks.get("theta").toString()));
            transaction.setVega(new BigDecimal(greeks.get("vega").toString()));
            transaction.setRho(new BigDecimal(greeks.get("rho").toString()));
            optionTransactionService.updateById(transaction);

            return new JsonBean(1, "计算成功", greeks).toJson();
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "计算失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * Black-Scholes期权定价模型计算希腊字母
     */
    private Map<String, Object> calculateOptionGreeks(
        String optionType, double S, double K, double r,
        double sigma, double T, double q) {

        Map<String, Object> result = new HashMap<>();

        // 防止除零和非法数值
        if (T <= 0 || sigma <= 0) {
            result.put("theoreticalValue", 0.0);
            result.put("delta", 0.0);
            result.put("gamma", 0.0);
            result.put("theta", 0.0);
            result.put("vega", 0.0);
            result.put("rho", 0.0);
            return result;
        }

        // 计算d1和d2
        double d1 = (Math.log(S / K) + (r - q + 0.5 * sigma * sigma) * T) / (sigma * Math.sqrt(T));
        double d2 = d1 - sigma * Math.sqrt(T);

        // 计算标准正态分布函数和概率密度函数
        double Nd1 = normalCDF(d1);
        double Nd2 = normalCDF(d2);
        double NNd1 = normalPDF(d1);
        double NNd2 = normalPDF(d2);

        // 看涨期权或看跌期权
        boolean isCall = "CALL".equalsIgnoreCase(optionType);

        // 理论价值
        double theoreticalValue;
        if (isCall) {
            theoreticalValue = S * Math.exp(-q * T) * Nd1 - K * Math.exp(-r * T) * Nd2;
        } else {
            theoreticalValue = K * Math.exp(-r * T) * normalCDF(-d2) - S * Math.exp(-q * T) * normalCDF(-d1);
        }

        // Delta
        double delta;
        if (isCall) {
            delta = Math.exp(-q * T) * Nd1;
        } else {
            delta = Math.exp(-q * T) * (Nd1 - 1);
        }

        // Gamma
        double gamma = Math.exp(-q * T) * NNd1 / (S * sigma * Math.sqrt(T));

        // Theta (每年)
        double theta;
        if (isCall) {
            theta = -(S * NNd1 * sigma * Math.exp(-q * T)) / (2 * Math.sqrt(T))
                - r * K * Math.exp(-r * T) * Nd2
                + q * S * Math.exp(-q * T) * Nd1;
        } else {
            theta = -(S * NNd1 * sigma * Math.exp(-q * T)) / (2 * Math.sqrt(T))
                + r * K * Math.exp(-r * T) * normalCDF(-d2)
                - q * S * Math.exp(-q * T) * normalCDF(-d1);
        }

        // Vega
        double vega = S * Math.exp(-q * T) * Math.sqrt(T) * NNd1 / 100;

        // Rho
        double rho;
        if (isCall) {
            rho = K * T * Math.exp(-r * T) * Nd2 / 100;
        } else {
            rho = -K * T * Math.exp(-r * T) * normalCDF(-d2) / 100;
        }

        result.put("theoreticalValue", round(theoreticalValue, 4));
        result.put("delta", round(delta, 4));
        result.put("gamma", round(gamma, 4));
        result.put("theta", round(theta, 4));
        result.put("vega", round(vega, 4));
        result.put("rho", round(rho, 4));

        return result;
    }

    /**
     * 标准正态分布累积分布函数
     */
    private double normalCDF(double x) {
        return 0.5 * (1 + erf(x / Math.sqrt(2)));
    }

    /**
     * 标准正态分布概率密度函数
     */
    private double normalPDF(double x) {
        return Math.exp(-0.5 * x * x) / Math.sqrt(2 * Math.PI);
    }

    /**
     * 误差函数近似计算
     */
    private double erf(double x) {
        // 常数
        double a1 = 0.254829592;
        double a2 = -0.284496736;
        double a3 = 1.421413741;
        double a4 = -1.453152027;
        double a5 = 1.061405429;
        double p = 0.3275911;

        // 保存符号
        int sign = x < 0 ? -1 : 1;
        x = Math.abs(x);

        // A&S公式7.1.26
        double t = 1.0 / (1.0 + p * x);
        double y = 1.0 - (((((a5 * t + a4) * t) + a3) * t + a2) * t + a1) * t * Math.exp(-x * x);

        return sign * y;
    }

    /**
     * 四舍五入
     */
    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
    }

    /**
     * 计算到期时间(年)
     */
    private double calculateTimeToExpiry(Date expiryDate) {
        if (expiryDate == null) {
            return 0.0;
        }
        long diff = expiryDate.getTime() - new Date().getTime();
        return Math.max(0, diff / (1000.0 * 60 * 60 * 24 * 365));
    }

    /**
     * 取消期权交易
     */
    @PostMapping("/{transactionId}/cancel")
    @ApiOperation("取消期权交易")
    public String cancel(
        @ApiParam("期权交易ID") @PathVariable("transactionId") Long transactionId,
        @ApiParam("取消原因") @RequestParam(required = false) String reason) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取期权交易
            TblOptionTransaction transaction = optionTransactionService.getById(transactionId);
            if (transaction == null) {
                return new JsonBean(0, "未找到该交易记录", null).toJson();
            }

            // 验证状态：只有待生效状态的交易才能取消
            if (!"PENDING".equals(transaction.getStatus())) {
                return new JsonBean(0, "只有待生效状态的交易才能取消", null).toJson();
            }

            // 更新状态为已取消
            transaction.setStatus("CANCELLED");
            transaction.setUpdateTime(new Date());
            transaction.setUpdateBy(loginStaff.getStaffid().longValue());

            boolean success = optionTransactionService.updateById(transaction);
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
}
