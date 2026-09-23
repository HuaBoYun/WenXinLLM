package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundForecast;
import com.global.treasurer.mapper.FundForecastMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金预测控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/forecast")
@Api(tags = "资金预测管理")
public class FundForecastController {
    @Resource
    private FundForecastMapper fundForecastMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金预测
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金预测")
    public String getFundForecastPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);

            List<TblFundForecast> list = fundForecastMapper.selectForecastPage(params);
            int total = fundForecastMapper.countForecastList(params);

            Map<String, Object> result = new HashMap<>();
            result.put("records", list);
            result.put("total", total);
            result.put("current", page);
            result.put("size", limit);

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金预测
     */
    @PostMapping("")
    @ApiOperation("创建资金预测")
    public String createFundForecast(@FlexibleRequestBody TblFundForecast forecast) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (forecast.getOrgId() == null && loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                forecast.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            if (forecast.getDeleteFlag() == null) {
                forecast.setDeleteFlag(0);
            }
            if (forecast.getCreatedTime() == null) {
                forecast.setCreatedTime(new java.util.Date());
            }
            if (loginStaff.getStaffid() != null) {
                try { forecast.setCreatedBy(loginStaff.getStaffid().longValue()); } catch (Exception ignored) {}
            }
            if (loginStaff.getRealname() != null) {
                forecast.setCreatedByName(loginStaff.getRealname());
            }
            int result = fundForecastMapper.insert(forecast);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新资金预测
     */
    @PutMapping("")
    @ApiOperation("更新资金预测")
    public String updateFundForecast(@FlexibleRequestBody TblFundForecast forecast) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundForecastMapper.updateById(forecast);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除资金预测
     */
    @DeleteMapping("/{forecastId}")
    @ApiOperation("删除资金预测")
    public String deleteFundForecast(@PathVariable Long forecastId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundForecastMapper.deleteById(forecastId);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预测汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取预测汇总信息")
    public String getFundForecastSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> summary = fundForecastMapper.selectForecastSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预测准确率分析
     */
    @GetMapping("/accuracy-analysis")
    @ApiOperation("获取预测准确率分析")
    public String getFundForecastAccuracyAnalysis(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                params.put("orgId", loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            List<Map<String, Object>> list = fundForecastMapper.selectAccuracyAnalysis(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预测趋势分析
     */
    @GetMapping("/trend-analysis")
    @ApiOperation("获取预测趋势分析")
    public String getFundForecastTrendAnalysis(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                params.put("orgId", loginStaff.getCurrentOrg().getOrgid().longValue());
            }
            List<Map<String, Object>> list = fundForecastMapper.selectTrendAnalysis(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 执行资金预测
     */
    @PostMapping("/{forecastId}/execute")
    @ApiOperation("执行资金预测")
    public String executeFundForecast(@PathVariable Long forecastId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundForecast forecast = fundForecastMapper.selectById(forecastId);
            if (forecast != null) {
                return JsonBean.success("预测执行成功");
            } else {
                return new JsonBean(0, "预测不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }
}
