package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtInterestRate;
import com.global.treasurer.service.xjgl.dataRulesManage.InterestRateService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 利率管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@RestController
@RequestMapping({"/treasury/interestRateManage",
              "/financial/treasury/interestRateManage"})
@Api(tags = "利率管理")
public class InterestRateManageController {
    private static final Logger log = LoggerFactory.getLogger(InterestRateManageController.class);

    @Resource
    private InterestRateService interestRateService;

    @Resource
    private UserProvider userProvider;

    /**
     * 1. 分页查询利率
     */
    @GetMapping({"/page", "/list"})
    @ApiOperation("分页查询利率列表")
    public String getInterestRatePage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Integer pageSize,
            @ApiParam("利率类型") @RequestParam(required = false) String rateType,
            @ApiParam("币种代码") @RequestParam(required = false) String currencyCode,
            @ApiParam("期限") @RequestParam(required = false) Integer term,
            @ApiParam("利率来源") @RequestParam(required = false) String rateSource) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(rateType)) {
                params.put("rateType", rateType);
            }
            if (StringUtils.hasText(currencyCode)) {
                params.put("currencyCode", currencyCode);
            }
            if (term != null) {
                params.put("term", term);
            }
            if (StringUtils.hasText(rateSource)) {
                params.put("rateSource", rateSource);
            }

            // 调用Service查询
            PageInfo<TblGtInterestRate> pageInfo = interestRateService.getInterestRateList(params, pageNo, pageSize);

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageNo);
            result.put("pageSize", pageSize);

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询利率列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 2. 新增利率
     */
    @PostMapping("")
    @ApiOperation("新增利率")
    public String createInterestRate(@FlexibleRequestBody TblGtInterestRate interestRate) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 参数校验
            if (!StringUtils.hasText(interestRate.getRateType())) {
                return new JsonBean(0, "利率类型不能为空", null).toJson();
            }
            if (!StringUtils.hasText(interestRate.getCurrencyCode())) {
                return new JsonBean(0, "币种代码不能为空", null).toJson();
            }
            if (interestRate.getInterestRate() == null || interestRate.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
                return new JsonBean(0, "利率值不能为空且必须大于等于0", null).toJson();
            }

            // 设置创建人
            interestRate.setCreateBy(loginStaff.getRealname());
            interestRate.setUpdateBy(loginStaff.getRealname());

            int result = interestRateService.createInterestRate(interestRate);
            if (result > 0) {
                return JsonBean.success("新增成功");
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增利率失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 3. 更新利率
     */
    @PutMapping("")
    @ApiOperation("更新利率")
    public String updateInterestRate(@FlexibleRequestBody TblGtInterestRate interestRate) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 参数校验
            if (interestRate.getRateId() == null) {
                return new JsonBean(0, "利率ID不能为空", null).toJson();
            }

            // 设置更新人
            interestRate.setUpdateBy(loginStaff.getRealname());

            int result = interestRateService.updateInterestRate(interestRate);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新利率失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 4. 删除利率
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除利率")
    public String deleteInterestRate(@ApiParam("利率ID") @PathVariable Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = interestRateService.deleteInterestRate(id);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除利率失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 5. 批量删除
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除利率")
    public String batchDelete(@RequestParam(value = "ids", required = false) List<Long> ids) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的数据", null).toJson();
            }

            int result = interestRateService.batchDelete(ids);
            if (result > 0) {
                return JsonBean.success("批量删除成功，共删除" + result + "条数据");
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除利率失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 6. 切换状态
     */
    @PutMapping("/status")
    @ApiOperation("切换利率状态")
    public String toggleStatus(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            Integer status = params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;

            if (id == null) {
                return new JsonBean(0, "利率ID不能为空", null).toJson();
            }
            if (status == null) {
                return new JsonBean(0, "状态不能为空", null).toJson();
            }

            int result = interestRateService.updateStatus(id, status);
            if (result > 0) {
                return JsonBean.success("状态更新成功");
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("切换状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 7. 根据ID查询
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询利率")
    public String getInterestRateById(@ApiParam("利率ID") @PathVariable Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtInterestRate interestRate = interestRateService.getById(id);
            if (interestRate == null) {
                return new JsonBean(0, "利率数据不存在", null).toJson();
            }

            return JsonBean.success(interestRate);
        } catch (Exception e) {
            log.error("查询利率详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 8. 获取实时利率
     */
    @GetMapping("/realTime")
    @ApiOperation("获取实时利率")
    public String getRealTimeRate(
            @ApiParam("币种代码") @RequestParam(required = false, defaultValue = "CNY") String currencyCode,
            @ApiParam("利率类型") @RequestParam(required = false, defaultValue = "DEPOSIT") String rateType) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 构建查询条件
            QueryWrapper<TblGtInterestRate> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CURRENCY_CODE", currencyCode);
            queryWrapper.eq("RATE_TYPE", rateType);
            queryWrapper.eq("STATUS", 1);
            queryWrapper.orderByDesc("RATE_DATE");
            queryWrapper.last("LIMIT 1");

            TblGtInterestRate interestRate = interestRateService.getOne(queryWrapper);

            if (interestRate == null) {
                return new JsonBean(0, "未找到对应的利率数据", null).toJson();
            }

            return JsonBean.success(interestRate);
        } catch (Exception e) {
            log.error("获取实时利率失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 9. 同步利率数据
     */
    @PostMapping("/sync")
    @ApiOperation("同步利率数据")
    public String syncInterestRate(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String sourceType = params.get("sourceType") != null ? params.get("sourceType").toString() : "PBOC";

            int result = interestRateService.syncInterestRate(sourceType);
            if (result > 0) {
                return JsonBean.success("同步成功，共同步" + result + "条数据");
            } else {
                return new JsonBean(0, "同步失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("同步利率数据失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 10. 获取基准利率
     */
    @GetMapping("/benchmark")
    @ApiOperation("获取基准利率")
    public String getBenchmarkRate(
            @ApiParam("币种代码") @RequestParam(required = false, defaultValue = "CNY") String currencyCode) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = interestRateService.getStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取基准利率失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
