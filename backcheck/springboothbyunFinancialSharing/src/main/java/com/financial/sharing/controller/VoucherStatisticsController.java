package com.financial.sharing.controller;

import com.financial.sharing.dto.VoucherStatisticsQueryParam;
import com.financial.sharing.dto.VoucherTrendParam;
import com.financial.sharing.service.VoucherStatisticsService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 凭证统计控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/voucher/statistics")
@CrossOrigin
@Api(tags = "凭证统计管理")
public class VoucherStatisticsController {

    @Resource
    private VoucherStatisticsService voucherStatisticsService;

    @Resource
    private UserProvider userProvider;

    /**
     * 创建成功响应
     */
    private JsonBean createSuccessJsonBean(String message, Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        json.setData(data);
        return json;
    }

    /**
     * 创建成功响应
     */
    private JsonBean createSuccessJsonBean(Object data) {
        return createSuccessJsonBean("操作成功", data);
    }

    /**
     * 创建错误响应
     */
    private JsonBean createErrorJsonBean(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return json;
    }

    /**
     * 权限验证
     */
    private TblStaffUtil validateUser() throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            return null;
        }
        return loginStaff;
    }

    @ApiOperation("凭证汇总统计")
    @PostMapping("/summary")
    public JsonBean getVoucherSummary(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证汇总统计，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = voucherStatisticsService.getVoucherSummary(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证汇总统计失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证数量统计(按状态)")
    @PostMapping("/count-by-status")
    public JsonBean getVoucherCountByStatus(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证数量统计(按状态)，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByStatus(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证数量统计(按状态)失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证数量统计(按期间)")
    @PostMapping("/count-by-period")
    public JsonBean getVoucherCountByPeriod(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证数量统计(按期间)，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByPeriod(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证数量统计(按期间)失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证数量统计(按凭证类型)")
    @PostMapping("/count-by-type")
    public JsonBean getVoucherCountByType(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证数量统计(按凭证类型)，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByType(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证数量统计(按凭证类型)失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证数量统计(按制单人)")
    @PostMapping("/count-by-preparer")
    public JsonBean getVoucherCountByPreparer(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证数量统计(按制单人)，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByPreparer(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证数量统计(按制单人)失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("分币种统计")
    @PostMapping("/count-by-currency")
    public JsonBean getVoucherCountByCurrency(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询分币种统计，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByCurrency(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询分币种统计失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证分录统计")
    @PostMapping("/entry-statistics")
    public JsonBean getVoucherEntryStatistics(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证分录统计，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = voucherStatisticsService.getVoucherEntryStatistics(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证分录统计失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证统计趋势")
    @PostMapping("/trend")
    public JsonBean getVoucherStatisticsTrend(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherTrendParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 查询凭证统计趋势，参数：{}", loginStaff.getStaffid(), param);

            List<Map<String, Object>> result = voucherStatisticsService.getVoucherStatisticsTrend(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("查询凭证统计趋势失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取统计筛选条件")
    @GetMapping("/filter-options")
    public JsonBean getFilterOptions(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @ApiParam(value = "账簿ID", required = false) @RequestParam(required = false) Long bookId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 获取统计筛选条件，账簿ID：{}", loginStaff.getStaffid(), bookId);

            Map<String, Object> result = voucherStatisticsService.getFilterOptions(bookId, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("获取统计筛选条件失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证统计报表导出")
    @PostMapping("/export")
    public JsonBean exportVoucherStatistics(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 导出凭证统计报表，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = voucherStatisticsService.exportVoucherStatistics(param, loginStaff);
            return createSuccessJsonBean("导出任务创建成功", result);
        } catch (Exception e) {
            log.error("导出凭证统计报表失败", e);
            return createErrorJsonBean("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取凭证统计仪表盘数据")
    @PostMapping("/dashboard")
    public JsonBean getVoucherStatisticsDashboard(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return createErrorJsonBean("用户已失效");
            }

            log.info("用户 {} 获取凭证统计仪表盘数据，参数：{}", loginStaff.getStaffid(), param);

            Map<String, Object> result = voucherStatisticsService.getVoucherStatisticsDashboard(param, loginStaff);
            return createSuccessJsonBean("查询成功", result);
        } catch (Exception e) {
            log.error("获取凭证统计仪表盘数据失败", e);
            return createErrorJsonBean("查询失败: " + e.getMessage());
        }
    }
}