package com.financial.sharing.controller;

import com.financial.sharing.dto.VoucherStatisticsQueryParam;
import com.financial.sharing.dto.VoucherTrendParam;
import com.financial.sharing.service.VoucherStatisticsService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
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
 * 凭证分析控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "凭证分析")
@RestController
@RequestMapping("/voucher-analysis")
@CrossOrigin
public class VoucherAnalysisController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private VoucherStatisticsService voucherStatisticsService;

    // ==================== 凭证统计分析 ====================

    @ApiOperation("凭证汇总统计")
    @PostMapping("/summary")
    public String getVoucherSummary(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证汇总统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            Map<String, Object> result = voucherStatisticsService.getVoucherSummary(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证汇总统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证数量统计(按状态)")
    @PostMapping("/count-by-status")
    public String getVoucherCountByStatus(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证状态统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByStatus(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证状态统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证数量统计(按期间)")
    @PostMapping("/count-by-period")
    public String getVoucherCountByPeriod(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证期间统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByPeriod(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证期间统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证数量统计(按凭证类型)")
    @PostMapping("/count-by-type")
    public String getVoucherCountByType(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证类型统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getVoucherCountByType(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证类型统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证金额统计")
    @PostMapping("/amount-statistics")
    public String getVoucherAmountStatistics(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证金额统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            Map<String, Object> result = voucherStatisticsService.getVoucherAmountStatistics(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证金额统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证生成趋势分析")
    @PostMapping("/generation-trend")
    public String getVoucherGenerationTrend(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody VoucherTrendParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证生成趋势，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getVoucherGenerationTrend(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证生成趋势失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证处理效率分析")
    @PostMapping("/efficiency-analysis")
    public String getVoucherEfficiencyAnalysis(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证处理效率分析，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            Map<String, Object> result = voucherStatisticsService.getVoucherEfficiencyAnalysis(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证处理效率分析失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("异常凭证分析")
    @PostMapping("/abnormal-analysis")
    public String getAbnormalVoucherAnalysis(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询异常凭证分析，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getAbnormalVoucherAnalysis(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询异常凭证分析失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("科目使用频次统计")
    @PostMapping("/subject-usage")
    public String getSubjectUsageStatistics(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询科目使用频次统计，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getSubjectUsageStatistics(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询科目使用频次统计失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("凭证复核质量分析")
    @PostMapping("/review-quality")
    public String getVoucherReviewQualityAnalysis(HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 @RequestBody VoucherStatisticsQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询凭证复核质量分析，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = voucherStatisticsService.getVoucherReviewQualityAnalysis(param, loginStaff);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证复核质量分析失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }
}