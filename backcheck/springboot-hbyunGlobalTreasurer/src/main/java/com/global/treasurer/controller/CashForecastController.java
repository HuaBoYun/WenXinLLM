package com.global.treasurer.controller;

import com.global.treasurer.entity.CashForecastConfig;
import com.global.treasurer.entity.CashForecastResult;
import com.global.treasurer.service.CashForecastService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 现金流预测控制器
 *
 * @author AI Developer
 * @date 2025-01-15
 */
@RestController
@RequestMapping("/cash/forecast")
@Api(tags = "现金流预测管理")
public class CashForecastController {
    private static final Logger log = LoggerFactory.getLogger(CashForecastController.class);
    @Resource
    private CashForecastService cashForecastService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询预测配置列表
     */
    @PostMapping("/config/page")
    @ApiOperation("分页查询预测配置列表")
    public String getForecastConfigPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashForecastService.getForecastConfigPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询预测配置详情
     */
    @GetMapping("/config/{configId}")
    @ApiOperation("获取预测配置详情")
    public String getForecastConfig(@PathVariable Long configId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            CashForecastConfig config = cashForecastService.getForecastConfigById(configId);
            return JsonBean.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建预测配置
     */
    @PostMapping("/config")
    @ApiOperation("创建预测配置")
    public String createForecastConfig(@FlexibleRequestBody CashForecastConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashForecastService.createForecastConfig(config);
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
     * 更新预测配置
     */
    @PutMapping("/config")
    @ApiOperation("更新预测配置")
    public String updateForecastConfig(@FlexibleRequestBody CashForecastConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = cashForecastService.updateForecastConfig(config);
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
     * 批量删除预测配置
     */
    @DeleteMapping("/config")
    @ApiOperation("批量删除预测配置")
    public String deleteForecastConfig(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Long> ids = (List<Long>) params.get("ids");
            int result = cashForecastService.batchDeleteConfig(ids);
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
     * 执行现金流预测
     */
    @PutMapping("/execute")
    @ApiOperation("执行现金流预测")
    public String executeForecast(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long configId = Long.parseLong(params.get("configId").toString());
            int result = cashForecastService.executeForecast(configId, loginStaff.getRealname());
            if (result > 0) {
                return JsonBean.success("预测成功,生成" + result + "天预测数据");
            } else {
                return new JsonBean(0, "预测失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "预测失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询预测结果列表(GET方式,兼容前端请求)
     */
    @GetMapping("")
    @ApiOperation("分页查询预测结果列表(GET)")
    public String getForecastResultPageGet(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("GET分页查询预测结果，params={}", params);
            Map<String, Object> result = cashForecastService.getForecastResultPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分页查询预测结果列表(POST方式)
     */
    @PostMapping("/result/page")
    @ApiOperation("分页查询预测结果列表")
    public String getForecastResultPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("POST分页查询预测结果，params={}", params);
            Map<String, Object> result = cashForecastService.getForecastResultPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据配置ID查询预测结果
     */
    @GetMapping("/result/byConfig/{configId}")
    @ApiOperation("根据配置ID查询预测结果")
    public String getForecastResultsByConfigId(@PathVariable Long configId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<CashForecastResult> results = cashForecastService.getForecastResultsByConfigId(configId);
            return JsonBean.success(results);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预测概览统计(GET方式,兼容前端请求)
     */
    @GetMapping("/overview")
    @ApiOperation("获取预测概览统计")
    public String getForecastOverviewGet(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashForecastService.getForecastOverview(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取预测概览统计(POST方式)
     */
    @PostMapping("/overview")
    @ApiOperation("获取预测概览统计")
    public String getForecastOverviewPost(@RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashForecastService.getForecastOverview(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取趋势图数据(GET方式,兼容前端请求)
     */
    @GetMapping("/trend")
    @ApiOperation("获取趋势图数据")
    public String getForecastTrendGet(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashForecastService.getForecastTrend(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取趋势图数据(POST方式)
     */
    @PostMapping("/trend")
    @ApiOperation("获取趋势图数据")
    public String getForecastTrendPost(@RequestParam(required = false) Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = cashForecastService.getForecastTrend(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新预测设置
     */
    @PutMapping("/settings")
    @ApiOperation("更新预测设置")
    public String updateForecastSettings(@RequestParam Map<String, Object> settings) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getStaffid() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("接收到预测设置请求，settings={}", settings);

            // 调用Service保存设置
            int result = cashForecastService.updateForecastSettings(settings, loginStaff);
            if (result > 0) {
                log.info("预测设置保存成功，result={}", result);
                return JsonBean.success("设置保存成功");
            } else {
                log.warn("预测设置保存失败，result={}", result);
                return new JsonBean(0, "设置保存失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("保存预测设置异常", e);
            return new JsonBean(0, "设置保存失败: " + e.getMessage(), null).toJson();
        }
    }
}
