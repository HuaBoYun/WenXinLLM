package com.financial.sharing.controller;

import com.financial.sharing.service.CurrencyRateService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.BatchUpdateStatusParam;
import com.financial.sharing.vo.param.CurrencyRateQueryParam;
import com.financial.sharing.vo.param.CurrencyRateSaveParam;
import com.financial.sharing.vo.result.CurrencyRateVO;
import com.hbfk.util.JsonBean;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.hbfk.config.Constants.AUTHORIZATION;

/**
 * 币种汇率管理控制器
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "币种汇率管理")
@RestController
@RequestMapping("/public/currency/exchangeRate")
@Validated
public class CurrencyRateController {

    @Autowired
    private CurrencyRateService currencyRateService;

    @Resource
    private UserProvider userProvider;

    private TblStaffUtil loginStaff;

    /**
     * 分页查询币种汇率列表
     */
    @ApiOperation("分页查询币种汇率列表")
    @PostMapping(value = "/getList", consumes = "application/x-www-form-urlencoded")
    public MyJsonBean<PageResult<CurrencyRateVO>> getList(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) String currencyName,
            @RequestParam(required = false) Integer rateType,
            @RequestParam(required = false) String rateDateStart,
            @RequestParam(required = false) String rateDateEnd,
            @RequestParam(required = false) Integer isBaseCurrency,
            @RequestParam(required = false) Integer isEnabled,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long tenantId) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 构建查询参数
            CurrencyRateQueryParam param = new CurrencyRateQueryParam();
            param.setPageNumber(pageNumber != null ? pageNumber : 1);
            param.setPageSize(pageSize != null ? pageSize : 15);
            param.setCurrencyCode(currencyCode);
            param.setCurrencyName(currencyName);
            param.setRateType(rateType);

            // 处理日期字符串转换
            if (rateDateStart != null && !rateDateStart.isEmpty()) {
                try {
                    param.setRateDateStart(LocalDate.parse(rateDateStart, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception e) {
                    log.warn("日期格式错误: {}", rateDateStart);
                }
            }
            if (rateDateEnd != null && !rateDateEnd.isEmpty()) {
                try {
                    param.setRateDateEnd(LocalDate.parse(rateDateEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                } catch (Exception e) {
                    log.warn("日期格式错误: {}", rateDateEnd);
                }
            }

            param.setIsBaseCurrency(isBaseCurrency);
            param.setIsEnabled(isEnabled);

            // 强制使用前端传递的参数，不被用户信息覆盖
            param.setBookId(bookId != null ? bookId : 1L);
            param.setTenantId(tenantId != null ? tenantId : 1L);

            PageResult<CurrencyRateVO> result = currencyRateService.getCurrencyRatePage(param);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询币种汇率列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新币种汇率
     */
    @ApiOperation("保存或更新币种汇率")
    @PostMapping(value = "/saveOrUpdate", consumes = "application/x-www-form-urlencoded")
    public String saveOrUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) Long rateId,
            @RequestParam String currencyCode,
            @RequestParam String currencyName,
            @RequestParam Integer rateType,
            @RequestParam BigDecimal exchangeRate,
            @RequestParam String rateDate,
            @RequestParam Integer isBaseCurrency,
            @RequestParam(required = false) Integer isEnabled,
            @RequestParam(required = false) String remark,
            @RequestParam Long bookId,
            @RequestParam Long tenantId,
            @RequestParam(required = false) Long creator,
            @RequestParam(required = false) Long updater) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 构建保存参数
            CurrencyRateSaveParam param = new CurrencyRateSaveParam();
            param.setRateId(rateId);
            param.setCurrencyCode(currencyCode);
            param.setCurrencyName(currencyName);
            param.setRateType(rateType);
            param.setExchangeRate(exchangeRate);

            // 处理日期字符串转换
            LocalDate localDate = null;
            if (rateDate != null && !rateDate.isEmpty()) {
                try {
                    localDate = LocalDate.parse(rateDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (Exception e) {
                    log.warn("日期格式错误: {}", rateDate);
                    localDate = LocalDate.now(); // 默认使用当前日期
                }
            } else {
                localDate = LocalDate.now();
            }
            param.setRateDate(localDate);

            param.setIsBaseCurrency(isBaseCurrency);
            param.setIsEnabled(isEnabled != null ? isEnabled : 1);
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            currencyRateService.saveOrUpdateCurrencyRate(param);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "保存成功", true));
        } catch (Exception e) {
            log.error("保存币种汇率失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "保存失败：" + e.getMessage(), null));
        }
    }

    /**
     * 根据ID查询币种汇率详情
     */
    @ApiOperation("根据ID查询币种汇率详情")
    @GetMapping("/{rateId}")
    public MyJsonBean<CurrencyRateVO> getById(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId) {
        try {
            CurrencyRateVO result = currencyRateService.getCurrencyRateById(rateId);
            if (result == null) {
                return MyJsonBean.errorData("币种汇率不存在");
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询币种汇率详情失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 删除币种汇率
     */
    @ApiOperation("删除币种汇率")
    @DeleteMapping("/{rateId}")
    public String delete(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            boolean success = currencyRateService.deleteCurrencyRate(rateId);
            if (success) {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "删除成功", null));
            } else {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "删除失败", null));
            }
        } catch (Exception e) {
            log.error("删除币种汇率失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "删除失败：" + e.getMessage(), null));
        }
    }

    /**
     * 批量删除币种汇率
     */
    @ApiOperation("批量删除币种汇率")
    @PostMapping(value = "/batchDelete", consumes = "application/x-www-form-urlencoded")
    public String batchDelete(@RequestParam @NotEmpty String rateIds, HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> rateIdList = java.util.Arrays.stream(rateIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean success = currencyRateService.batchDeleteCurrencyRates(rateIdList);
            if (success) {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "批量删除成功", null));
            } else {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "批量删除失败", null));
            }
        } catch (Exception e) {
            log.error("批量删除币种汇率失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "批量删除失败：" + e.getMessage(), null));
        }
    }

    /**
     * 启用/禁用币种汇率
     */
    @ApiOperation("启用/禁用币种汇率")
    @PutMapping("/{rateId}/status")
    public String updateStatus(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId,
            @ApiParam(value = "启用状态", required = true) @RequestParam @NotNull Integer isEnabled,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            boolean success = currencyRateService.updateCurrencyRateStatus(rateId, isEnabled);
            if (success) {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "状态更新成功", null));
            } else {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "状态更新失败", null));
            }
        } catch (Exception e) {
            log.error("更新币种汇率状态失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "状态更新失败：" + e.getMessage(), null));
        }
    }

    /**
     * 批量启用/禁用币种汇率
     */
    @ApiOperation("批量启用/禁用币种汇率")
    @PostMapping(value = "/batchUpdateStatus", consumes = "application/x-www-form-urlencoded")
    public String batchUpdateStatus(
            @RequestParam @NotEmpty String rateIds,
            @RequestParam @NotNull Integer isEnabled,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> rateIdList = java.util.Arrays.stream(rateIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean success = currencyRateService.batchUpdateCurrencyRateStatus(rateIdList, isEnabled);
            if (success) {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "批量状态更新成功", null));
            } else {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "批量状态更新失败", null));
            }
        } catch (Exception e) {
            log.error("批量更新币种汇率状态失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "批量状态更新失败：" + e.getMessage(), null));
        }
    }

    /**
     * 检查币种和日期是否存在
     */
    @ApiOperation("检查币种和日期是否存在")
    @GetMapping("/check")
    public MyJsonBean<Boolean> checkExists(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "汇率日期", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate rateDate,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId,
            @ApiParam(value = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            boolean exists = currencyRateService.checkCurrencyAndDateExists(
                currencyCode, rateDate, bookId, tenantId, excludeId);
            return MyJsonBean.successData(exists);
        } catch (Exception e) {
            log.error("检查币种汇率失败", e);
            return MyJsonBean.errorData("检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取最新汇率
     */
    @ApiOperation("获取最新汇率")
    @GetMapping("/latest")
    public MyJsonBean<CurrencyRateVO> getLatestRate(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            CurrencyRateVO result = currencyRateService.getLatestRate(currencyCode, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取最新汇率失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据日期范围查询汇率
     */
    @ApiOperation("根据日期范围查询汇率")
    @GetMapping("/range")
    public MyJsonBean<List<CurrencyRateVO>> getByDateRange(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "开始日期", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate startDate,
            @ApiParam(value = "结束日期", required = true) @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @NotNull LocalDate endDate,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<CurrencyRateVO> result = currencyRateService.getCurrencyRatesByDateRange(
                currencyCode, startDate, endDate, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("根据日期范围查询汇率失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有启用的币种列表
     */
    @ApiOperation("获取所有启用的币种列表")
    @GetMapping("/currencies")
    public MyJsonBean<List<CurrencyRateVO>> getEnabledCurrencies(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<CurrencyRateVO> result = currencyRateService.getEnabledCurrencies(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取启用币种列表失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取本位币信息
     */
    @ApiOperation("获取本位币信息")
    @GetMapping("/base")
    public MyJsonBean<CurrencyRateVO> getBaseCurrency(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            CurrencyRateVO result = currencyRateService.getBaseCurrency(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取本位币信息失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 设置本位币
     */
    @ApiOperation("设置本位币")
    @PutMapping("/{rateId}/base")
    public String setBaseCurrency(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId,
            HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            boolean success = currencyRateService.setBaseCurrency(rateId, bookId, tenantId);
            if (success) {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(1, "设置本位币成功", null));
            } else {
                return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "设置本位币失败", null));
            }
        } catch (Exception e) {
            log.error("设置本位币失败", e);
            return JsonMapper.nonNullMapper().toJson(new JsonBean(0, "设置失败：" + e.getMessage(), null));
        }
    }

    /**
     * 获取币种编码列表
     */
    @ApiOperation("获取币种编码列表")
    @GetMapping("/codes")
    public MyJsonBean<List<String>> getCurrencyCodes(
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            List<String> result = currencyRateService.getCurrencyCodes(bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("获取币种编码列表失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 币种转换
     */
    @ApiOperation("币种转换")
    @GetMapping("/convert")
    public MyJsonBean<BigDecimal> convertCurrency(
            @ApiParam(value = "金额", required = true) @RequestParam @NotNull BigDecimal amount,
            @ApiParam(value = "源币种", required = true) @RequestParam @NotNull String fromCurrency,
            @ApiParam(value = "目标币种", required = true) @RequestParam @NotNull String toCurrency,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            BigDecimal result = currencyRateService.convertCurrency(amount, fromCurrency, toCurrency, bookId, tenantId);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("币种转换失败", e);
            return MyJsonBean.errorData("转换失败：" + e.getMessage());
        }
    }
}
