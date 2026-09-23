package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtExchangeRateConfig;
import com.global.treasurer.service.xjgl.dataRulesManage.ExchangeRateConfigService;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 汇率配置管理控制器
 *
 * @author 华博云开发团队
 * @since 2026-01-30
 */
@RestController
@RequestMapping("/financial/xjgl/dataRulesManage/exchangeRateConfig")
@Api(tags = "汇率配置管理")
public class ExchangeRateConfigController {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateConfigController.class);

    @Resource
    private ExchangeRateConfigService exchangeRateConfigService;

    @Resource
    private UserProvider userProvider;

    /**
     * 1. 分页查询汇率配置 (GET)
     */
    @GetMapping({"/page", "/list"})
    @ApiOperation("分页查询汇率配置列表(GET)")
    public String getExchangeRateConfigPage(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer limit,
            @RequestParam(required = false) String baseCurrency,
            @RequestParam(required = false) String targetCurrency,
            @RequestParam(required = false) String rateType,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) String dataSource,
            @RequestParam(required = false) String effectiveDate) {
        return getExchangeRateConfigList(page, limit, baseCurrency, targetCurrency, rateType, status, dataSource, effectiveDate);
    }

    /**
     * 分页查询汇率配置 (POST)
     */
    @PostMapping({"/page", "/list"})
    @ApiOperation("分页查询汇率配置列表(POST)")
    public String postExchangeRateConfigPage(@FlexibleRequestBody(required = false) Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        // 兼容前端 pageNum/pageSize 和 page/limit 两种命名
        Integer page = getIntParam(params, "pageNum", getIntParam(params, "page", 1));
        Integer limit = getIntParam(params, "pageSize", getIntParam(params, "limit", 20));
        String baseCurrency = getStrParam(params, "baseCurrency");
        String targetCurrency = getStrParam(params, "targetCurrency");
        String rateType = getStrParam(params, "rateType");
        Integer status = params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;
        String dataSource = getStrParam(params, "dataSource");
        String effectiveDate = getStrParam(params, "effectiveDate");

        return getExchangeRateConfigList(page, limit, baseCurrency, targetCurrency, rateType, status, dataSource, effectiveDate);
    }

    /**
     * 分页查询汇率配置 (通用方法)
     */
    private String getExchangeRateConfigList(Integer page, Integer limit, String baseCurrency, String targetCurrency, String rateType, Integer status, String dataSource, String effectiveDate) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (StringUtils.hasText(baseCurrency)) {
                params.put("baseCurrency", baseCurrency);
            }
            if (StringUtils.hasText(targetCurrency)) {
                params.put("targetCurrency", targetCurrency);
            }
            if (StringUtils.hasText(rateType)) {
                params.put("rateType", rateType);
            }
            if (status != null) {
                params.put("status", status);
            }
            if (StringUtils.hasText(dataSource)) {
                params.put("dataSource", dataSource);
            }
            if (StringUtils.hasText(effectiveDate)) {
                params.put("effectiveDate", effectiveDate);
            }

            // 分页查询
            PageInfo<TblGtExchangeRateConfig> pageInfo = exchangeRateConfigService
                    .getExchangeRateConfigList(params, page, limit);

            // 构建返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", page);
            result.put("pageSize", limit);

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询汇率配置列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 2. 新增汇率配置
     */
    @PostMapping({"", "/create"})
    @ApiOperation("新增汇率配置")
    public String createExchangeRateConfig(@FlexibleRequestBody TblGtExchangeRateConfig config) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置创建人信息
            config.setCreateBy(loginStaff.getRealname());
            config.setUpdateBy(loginStaff.getRealname());
            if (loginStaff.getCurrentOrg() != null && loginStaff.getCurrentOrg().getOrgid() != null) {
                config.setOrgId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            int result = exchangeRateConfigService.createExchangeRateConfig(config);
            if (result > 0) {
                return JsonBean.success("新增成功");
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增汇率配置失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 3. 更新汇率配置
     */
    @PutMapping("")
    @ApiOperation("更新汇率配置(PUT)")
    public String updateExchangeRateConfigPut(@FlexibleRequestBody TblGtExchangeRateConfig config) {
        return doUpdateExchangeRateConfig(config);
    }

    @PostMapping("/update")
    @ApiOperation("更新汇率配置(POST)")
    public String updateExchangeRateConfig(@FlexibleRequestBody TblGtExchangeRateConfig config) {
        return doUpdateExchangeRateConfig(config);
    }

    private String doUpdateExchangeRateConfig(TblGtExchangeRateConfig config) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置更新人信息
            config.setUpdateBy(loginStaff.getRealname());

            int result = exchangeRateConfigService.updateExchangeRateConfig(config);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新汇率配置失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 4. 删除汇率配置 (DELETE)
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除汇率配置(DELETE)")
    public String deleteExchangeRateConfig(
            @PathVariable @ApiParam("配置ID") Long id) {
        return doDeleteExchangeRateConfig(id);
    }

    /**
     * 4b. 删除汇率配置 (POST /delete)
     */
    @PostMapping("/delete")
    @ApiOperation("删除汇率配置(POST)")
    public String deleteExchangeRateConfigPost(@FlexibleRequestBody Map<String, Object> params) {
        try {
            Object idObj = params.get("id");
            if (idObj == null) {
                return new JsonBean(0, "参数id不能为空", null).toJson();
            }
            Long id = Long.valueOf(idObj.toString());
            return doDeleteExchangeRateConfig(id);
        } catch (Exception e) {
            log.error("删除汇率配置失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    private String doDeleteExchangeRateConfig(Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = exchangeRateConfigService.deleteExchangeRateConfig(id);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除汇率配置失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 5. 批量删除汇率配置
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除汇率配置")
    public String batchDeleteExchangeRateConfig(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取ID列表
            Object idsObj = params.get("ids");
            if (idsObj == null) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            List<Long> ids = new ArrayList<>();
            if (idsObj instanceof List) {
                List<?> list = (List<?>) idsObj;
                for (Object idObj : list) {
                    if (idObj != null) {
                        ids.add(Long.valueOf(idObj.toString()));
                    }
                }
            } else if (idsObj instanceof String) {
                String[] idArray = idsObj.toString().split(",");
                for (String idStr : idArray) {
                    if (StringUtils.hasText(idStr)) {
                        ids.add(Long.valueOf(idStr.trim()));
                    }
                }
            }

            if (ids.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }

            int result = exchangeRateConfigService.batchDelete(ids);
            return JsonBean.success("成功删除" + result + "条记录");
        } catch (Exception e) {
            log.error("批量删除汇率配置失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 6. 切换状态 (PUT)
     */
    @PutMapping("/status")
    @ApiOperation("切换汇率配置状态(PUT)")
    public String toggleStatus(@RequestParam Map<String, Object> params) {
        return doToggleStatus(params);
    }

    /**
     * 6b. 切换状态 (POST /updateStatus)
     */
    @PostMapping("/updateStatus")
    @ApiOperation("切换汇率配置状态(POST)")
    public String toggleStatusPost(@FlexibleRequestBody Map<String, Object> params) {
        return doToggleStatus(params);
    }

    private String doToggleStatus(Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取参数
            Object idObj = params.get("id");
            Object statusObj = params.get("status");

            if (idObj == null || statusObj == null) {
                return new JsonBean(0, "参数不完整", null).toJson();
            }

            Long id = Long.valueOf(idObj.toString());
            Integer status = Integer.valueOf(statusObj.toString());

            int result = exchangeRateConfigService.updateStatus(id, status);
            if (result > 0) {
                return JsonBean.success("状态更新成功");
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("切换汇率配置状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 7. 根据ID查询
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询汇率配置")
    public String getExchangeRateConfigById(
            @PathVariable @ApiParam("配置ID") Long id) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblGtExchangeRateConfig config = exchangeRateConfigService.getById(id);
            if (config == null) {
                return new JsonBean(0, "汇率配置不存在", null).toJson();
            }

            return JsonBean.success(config);
        } catch (Exception e) {
            log.error("查询汇率配置详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 8. 同步汇率 (带ID)
     */
    @PostMapping("/{id}/sync")
    @ApiOperation("同步汇率(带ID)")
    public String syncExchangeRate(
            @PathVariable @ApiParam("配置ID") Long id,
            @RequestParam(required = false) Map<String, Object> params) {
        return doSyncExchangeRate(params);
    }

    /**
     * 8b. 同步汇率 (POST /sync)
     */
    @PostMapping("/sync")
    @ApiOperation("同步汇率")
    public String syncExchangeRatePost(@FlexibleRequestBody(required = false) Map<String, Object> params) {
        return doSyncExchangeRate(params);
    }

    private String doSyncExchangeRate(Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 获取数据源类型
            String sourceType = params != null && params.get("sourceType") != null
                    ? params.get("sourceType").toString()
                    : "API";

            int result = exchangeRateConfigService.syncExchangeRateConfig(sourceType);
            return JsonBean.success("同步成功，同步" + result + "条数据");
        } catch (Exception e) {
            log.error("同步汇率失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 9. 获取实时汇率
     */
    @GetMapping("/realTimeRate")
    @ApiOperation("获取实时汇率")
    public String getRealTimeRate(
            @RequestParam @ApiParam("基准币种") String baseCurrency,
            @RequestParam @ApiParam("目标币种") String targetCurrency) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 查询最新的有效汇率
            QueryWrapper<TblGtExchangeRateConfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("BASE_CURRENCY", baseCurrency);
            queryWrapper.eq("TARGET_CURRENCY", targetCurrency);
            queryWrapper.eq("STATUS", 1);
            queryWrapper.orderByDesc("EFFECTIVE_DATE");
            queryWrapper.last("LIMIT 1");

            TblGtExchangeRateConfig config = exchangeRateConfigService.getOne(queryWrapper);

            if (config == null) {
                return new JsonBean(0, "未找到有效汇率", null).toJson();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("baseCurrency", config.getBaseCurrency());
            result.put("targetCurrency", config.getTargetCurrency());
            result.put("exchangeRate", config.getExchangeRate());
            result.put("rateType", config.getRateType());
            result.put("effectiveDate", config.getEffectiveDate());
            result.put("updateTime", config.getUpdateTime());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("获取实时汇率失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 10. 导出配置
     */
    @PostMapping("/export")
    @ApiOperation("导出汇率配置")
    public String exportExchangeRateConfig(
            @RequestParam(required = false) Map<String, Object> params,
            HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            log.info("导出汇率配置，参数: {}", params);

            // TODO: 实现导出逻辑
            // 1. 根据查询条件获取数据
            // 2. 生成Excel文件
            // 3. 返回文件流

            return JsonBean.success("导出成功");
        } catch (Exception e) {
            log.error("导出汇率配置失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取统计信息
     */
    @GetMapping("/statistics")
    @ApiOperation("获取汇率配置统计信息")
    public String getStatistics() {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> statistics = exchangeRateConfigService.getStatistics();
            return JsonBean.success(statistics);
        } catch (Exception e) {
            log.error("获取统计信息失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    // ========== 辅助方法 ==========
    private String getStrParam(Map<String, Object> params, String key) {
        Object val = params.get(key);
        if (val == null) return null;
        String str = val.toString().trim();
        return str.isEmpty() ? null : str;
    }

    private Integer getIntParam(Map<String, Object> params, String key, Integer defaultVal) {
        Object val = params.get(key);
        if (val == null) return defaultVal;
        try {
            return Integer.parseInt(val.toString());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
