package com.financial.sharing.controller;

import com.financial.sharing.service.CurrencyRateService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CurrencyRateQueryParam;
import com.financial.sharing.vo.param.CurrencyRateSaveParam;
import com.financial.sharing.vo.result.CurrencyRateVO;
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
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 币种汇率管理控制器 - 匹配前端API路径
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "币种汇率管理")
@RestController
@RequestMapping("/common/currency-rate")
@Validated
public class CurrencyRateAdapterController {

    @Autowired
    private CurrencyRateService currencyRateService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询币种汇率列表
     */
    @ApiOperation("分页查询币种汇率列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<CurrencyRateVO>> getList(@Valid @RequestBody CurrencyRateQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            PageResult<CurrencyRateVO> result = currencyRateService.getCurrencyRatePage(param);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("查询币种汇率列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存或更新币种汇率
     */
    @ApiOperation("保存或更新币种汇率")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<CurrencyRateVO> saveOrUpdate(@Valid @RequestBody CurrencyRateSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            CurrencyRateVO result = currencyRateService.saveOrUpdateCurrencyRate(param);
            return MyJsonBean.successData("保存成功", result);
        } catch (Exception e) {
            log.error("保存币种汇率失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
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
            // 权限验证
            getLoginStaff();

            CurrencyRateVO result = currencyRateService.getCurrencyRateById(rateId);
            if (result == null) {
                return MyJsonBean.errorData("币种汇率不存在", null);
            }
            return MyJsonBean.successData("查询成功", result);
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
    public MyJsonBean<String> delete(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId) {
        try {
            // 权限验证
            getLoginStaff();

            boolean success = currencyRateService.deleteCurrencyRate(rateId);
            if (success) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除币种汇率失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除币种汇率
     */
    @ApiOperation("批量删除币种汇率")
    @DeleteMapping("/batch")
    public MyJsonBean<String> batchDelete(@RequestBody @NotEmpty List<Long> rateIds) {
        try {
            // 权限验证
            getLoginStaff();

            boolean success = currencyRateService.batchDeleteCurrencyRates(rateIds);
            if (success) {
                return MyJsonBean.successData("批量删除成功", null);
            } else {
                return MyJsonBean.errorData("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除币种汇率失败", e);
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 启用/禁用币种汇率
     */
    @ApiOperation("启用/禁用币种汇率")
    @PutMapping("/{rateId}/status")
    public MyJsonBean<String> updateStatus(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId,
            @ApiParam(value = "启用状态", required = true) @RequestParam @NotNull Integer isEnabled) {
        try {
            // 权限验证
            getLoginStaff();

            boolean success = currencyRateService.updateCurrencyRateStatus(rateId, isEnabled);
            if (success) {
                String statusText = isEnabled == 1 ? "启用" : "禁用";
                return MyJsonBean.successData(statusText + "成功", null);
            } else {
                return MyJsonBean.errorData("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新币种汇率状态失败", e);
            return MyJsonBean.errorData("状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 批量启用/禁用币种汇率
     */
    @ApiOperation("批量启用/禁用币种汇率")
    @PutMapping("/batch/status")
    public MyJsonBean<String> batchUpdateStatus(
            @RequestBody @NotEmpty List<Long> rateIds,
            @ApiParam(value = "启用状态", required = true) @RequestParam @NotNull Integer isEnabled) {
        try {
            // 权限验证
            getLoginStaff();

            boolean success = currencyRateService.batchUpdateCurrencyRateStatus(rateIds, isEnabled);
            if (success) {
                String statusText = isEnabled == 1 ? "启用" : "禁用";
                return MyJsonBean.successData("批量" + statusText + "成功", null);
            } else {
                return MyJsonBean.errorData("批量状态更新失败");
            }
        } catch (Exception e) {
            log.error("批量更新币种汇率状态失败", e);
            return MyJsonBean.errorData("批量状态更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取最新汇率
     */
    @ApiOperation("获取最新汇率")
    @GetMapping("/latest")
    public MyJsonBean<CurrencyRateVO> getLatestRate(
            @ApiParam(value = "币种编码", required = true) @RequestParam @NotNull String currencyCode,
            @ApiParam(value = "账簿ID", required = false) @RequestParam(required = false) Long bookId,
            @ApiParam(value = "租户ID", required = false) @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            CurrencyRateVO result = currencyRateService.getLatestRate(currencyCode, bookId, tenantId);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("获取最新汇率失败", e);
            return MyJsonBean.errorData("获取失败：" + e.getMessage());
        }
    }

    /**
     * 设为本位币
     */
    @ApiOperation("设为本位币")
    @PutMapping("/{rateId}/base")
    public MyJsonBean<String> setBaseCurrency(
            @ApiParam(value = "汇率ID", required = true) @PathVariable @NotNull Long rateId,
            @ApiParam(value = "账簿ID", required = false) @RequestParam(required = false) Long bookId,
            @ApiParam(value = "租户ID", required = false) @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            boolean success = currencyRateService.setBaseCurrency(rateId, bookId, tenantId);
            if (success) {
                return MyJsonBean.successData("设置本位币成功", null);
            } else {
                return MyJsonBean.errorData("设置本位币失败");
            }
        } catch (Exception e) {
            log.error("设置本位币失败", e);
            return MyJsonBean.errorData("设置失败：" + e.getMessage());
        }
    }

    /**
     * 获取登录用户信息
     * @return 登录用户信息
     */
    private TblStaffUtil getLoginStaff() throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            throw new Exception("用户已失效");
        }
        return loginStaff;
    }
}