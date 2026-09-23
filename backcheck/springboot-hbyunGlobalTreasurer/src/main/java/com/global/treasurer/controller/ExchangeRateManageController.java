package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtExchangeRate;
import com.global.treasurer.service.xjgl.dataRulesManage.ExchangeRateService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇率管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-30
 */
@Controller
@RequestMapping({"/treasury/exchangeRateManage", "/financial/treasury/exchangeRateManage"})
@Api(tags = "汇率管理")
public class ExchangeRateManageController {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateManageController.class);

    @Resource
    private ExchangeRateService exchangeRateService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询汇率列表
     */
    @GetMapping({"/page", "/list"})
    @ResponseBody
    @ApiOperation("分页查询汇率列表")
    public String getExchangeRatePage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam("每页大小") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam("源币种") @RequestParam(required = false) String fromCurrency,
            @ApiParam("目标币种") @RequestParam(required = false) String toCurrency,
            @ApiParam("汇率来源") @RequestParam(required = false) String rateSource,
            @ApiParam("生效日期") @RequestParam(required = false) String effectiveDate,
            @RequestHeader(value = "token", required = false) String token,
            HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (fromCurrency != null && !fromCurrency.trim().isEmpty()) {
                params.put("fromCurrency", fromCurrency);
            }
            if (toCurrency != null && !toCurrency.trim().isEmpty()) {
                params.put("toCurrency", toCurrency);
            }
            if (rateSource != null && !rateSource.trim().isEmpty()) {
                params.put("rateSource", rateSource);
            }
            if (effectiveDate != null && !effectiveDate.trim().isEmpty()) {
                params.put("effectiveDate", effectiveDate);
            }

            // 调用Service查询
            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, page, limit);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("rows", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", page);
            data.put("pageSize", limit);

            return JsonBean.success(data);
        } catch (Exception e) {
            log.error("获取汇率列表失败", e);
            return new JsonBean(0, "获取汇率列表失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增汇率
     */
    @PostMapping
    @ResponseBody
    @ApiOperation("新增汇率")
    public String addExchangeRate(@FlexibleRequestBody TblGtExchangeRate exchangeRate,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 参数校验
            if (exchangeRate.getFromCurrency() == null || exchangeRate.getFromCurrency().trim().isEmpty()) {
                return new JsonBean(0, "源币种不能为空", null).toJson();
            }
            if (exchangeRate.getToCurrency() == null || exchangeRate.getToCurrency().trim().isEmpty()) {
                return new JsonBean(0, "目标币种不能为空", null).toJson();
            }
            if (exchangeRate.getExchangeRate() == null || exchangeRate.getExchangeRate().compareTo(BigDecimal.ZERO) <= 0) {
                return new JsonBean(0, "汇率必须大于0", null).toJson();
            }

            // 设置创建人
            exchangeRate.setCreateBy(loginStaff.getRealname());
            exchangeRate.setUpdateBy(loginStaff.getRealname());

            int result = exchangeRateService.createExchangeRate(exchangeRate);
            if (result > 0) {
                return new JsonBean(1, "新增成功", exchangeRate).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增汇率失败", e);
            return new JsonBean(0, "新增汇率失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新汇率
     */
    @PutMapping
    @ResponseBody
    @ApiOperation("更新汇率")
    public String updateExchangeRate(@FlexibleRequestBody TblGtExchangeRate exchangeRate,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 参数校验
            if (exchangeRate.getRateId() == null) {
                return new JsonBean(0, "汇率ID不能为空", null).toJson();
            }
            if (exchangeRate.getExchangeRate() == null || exchangeRate.getExchangeRate().compareTo(BigDecimal.ZERO) <= 0) {
                return new JsonBean(0, "汇率必须大于0", null).toJson();
            }

            // 设置更新人
            exchangeRate.setUpdateBy(loginStaff.getRealname());

            int result = exchangeRateService.updateExchangeRate(exchangeRate);
            if (result > 0) {
                return new JsonBean(1, "更新成功", exchangeRate).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新汇率失败", e);
            return new JsonBean(0, "更新汇率失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除汇率
     */
    @DeleteMapping("/{id}")
    @ResponseBody
    @ApiOperation("删除汇率")
    public String deleteExchangeRate(@ApiParam("汇率ID") @PathVariable Long id,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            int result = exchangeRateService.deleteExchangeRate(id);
            if (result > 0) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除汇率失败", e);
            return new JsonBean(0, "删除汇率失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除汇率
     */
    @DeleteMapping("/batch")
    @ResponseBody
    @ApiOperation("批量删除汇率")
    public String batchDeleteExchangeRate(@RequestParam(value = "ids", required = false) List<Long> ids,
                                           @RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的数据", null).toJson();
            }

            int result = exchangeRateService.batchDelete(ids);
            return new JsonBean(1, "成功删除" + result + "条数据", result).toJson();
        } catch (Exception e) {
            log.error("批量删除汇率失败", e);
            return new JsonBean(0, "批量删除汇率失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 切换汇率状态
     */
    @PutMapping("/status")
    @ResponseBody
    @ApiOperation("切换汇率状态")
    public String toggleStatus(@ApiParam("汇率ID") @RequestParam Long id,
                               @ApiParam("状态") @RequestParam Integer status,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            int result = exchangeRateService.updateStatus(id, status);
            if (result > 0) {
                return new JsonBean(1, "状态更新成功", null).toJson();
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新汇率状态失败", e);
            return new JsonBean(0, "更新汇率状态失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询汇率详情
     */
    @GetMapping("/{id}")
    @ResponseBody
    @ApiOperation("根据ID查询汇率详情")
    public String getExchangeRateById(@ApiParam("汇率ID") @PathVariable Long id,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblGtExchangeRate exchangeRate = exchangeRateService.getById(id);
            if (exchangeRate != null) {
                return new JsonBean(1, "查询成功", exchangeRate).toJson();
            } else {
                return new JsonBean(0, "数据不存在", null).toJson();
            }
        } catch (Exception e) {
            log.error("查询汇率详情失败", e);
            return new JsonBean(0, "查询汇率详情失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 货币转换
     */
    @GetMapping("/convert")
    @ResponseBody
    @ApiOperation("货币转换")
    public String convertCurrency(@ApiParam("源币种") @RequestParam String fromCurrency,
                                   @ApiParam("目标币种") @RequestParam String toCurrency,
                                   @ApiParam("金额") @RequestParam BigDecimal amount,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 查询最新汇率
            Map<String, Object> params = new HashMap<>();
            params.put("fromCurrency", fromCurrency);
            params.put("toCurrency", toCurrency);

            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, 1, 1);

            if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
                return new JsonBean(0, "未找到汇率数据", null).toJson();
            }

            TblGtExchangeRate rate = pageInfo.getList().get(0);
            BigDecimal convertedAmount = amount.multiply(rate.getExchangeRate());

            Map<String, Object> result = new HashMap<>();
            result.put("fromCurrency", fromCurrency);
            result.put("toCurrency", toCurrency);
            result.put("amount", amount);
            result.put("exchangeRate", rate.getExchangeRate());
            result.put("convertedAmount", convertedAmount);
            result.put("effectiveDate", rate.getEffectiveDate());

            return new JsonBean(1, "转换成功", result).toJson();
        } catch (Exception e) {
            log.error("货币转换失败", e);
            return new JsonBean(0, "货币转换失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取实时汇率
     */
    @GetMapping("/realTime")
    @ResponseBody
    @ApiOperation("获取实时汇率")
    public String getRealTimeRate(@ApiParam("源币种") @RequestParam String fromCurrency,
                                   @ApiParam("目标币种") @RequestParam String toCurrency,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 查询最新汇率
            Map<String, Object> params = new HashMap<>();
            params.put("fromCurrency", fromCurrency);
            params.put("toCurrency", toCurrency);

            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, 1, 1);

            if (pageInfo.getList() == null || pageInfo.getList().isEmpty()) {
                return new JsonBean(0, "未找到汇率数据", null).toJson();
            }

            TblGtExchangeRate rate = pageInfo.getList().get(0);
            return new JsonBean(1, "查询成功", rate).toJson();
        } catch (Exception e) {
            log.error("获取实时汇率失败", e);
            return new JsonBean(0, "获取实时汇率失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步汇率数据
     */
    @PostMapping("/sync")
    @ResponseBody
    @ApiOperation("同步汇率数据")
    public String syncExchangeRate(@ApiParam("数据源类型") @RequestParam String sourceType,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            int count = exchangeRateService.syncExchangeRate(sourceType);
            return new JsonBean(1, "同步完成，共同步" + count + "条数据", count).toJson();
        } catch (Exception e) {
            log.error("同步汇率数据失败", e);
            return new JsonBean(0, "同步汇率数据失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取汇率统计信息
     */
    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取汇率统计信息")
    public String getStatistics(@RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> statistics = exchangeRateService.getStatistics();
            return new JsonBean(1, "查询成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取汇率统计信息失败", e);
            return new JsonBean(0, "获取汇率统计信息失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取汇率趋势分析
     */
    @GetMapping("/trend")
    @ResponseBody
    @ApiOperation("获取汇率趋势分析")
    public String getTrendAnalysis(@ApiParam("源币种") @RequestParam(defaultValue = "USD") String fromCurrency,
                                    @ApiParam("目标币种") @RequestParam(defaultValue = "CNY") String toCurrency,
                                    @ApiParam("天数") @RequestParam(defaultValue = "7") Integer days,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            // 用户权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("fromCurrency", fromCurrency);
            params.put("toCurrency", toCurrency);
            params.put("days", days);

            Map<String, Object> trend = exchangeRateService.getTrendAnalysis(params);
            return new JsonBean(1, "查询成功", trend).toJson();
        } catch (Exception e) {
            log.error("获取汇率趋势分析失败", e);
            return new JsonBean(0, "获取汇率趋势分析失败: " + e.getMessage(), null).toJson();
        }
    }
}
