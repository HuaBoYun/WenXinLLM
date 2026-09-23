package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.export.ExportExchangeRateDTO;
import com.global.treasurer.entity.TblGtExchangeRate;
import com.global.treasurer.service.xjgl.dataRulesManage.ExchangeRateService;
import com.global.treasurer.util.excel.ExcelExport;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 汇率管理Controller
 * 匹配前端API路径: /qqsk/treasury/exchangeRateManage/*
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Controller
@RequestMapping({"/financial/xjgl/dataRulesManage/exchangeRate", "/financial/exchangeRateManage"})
@Api(tags = "汇率管理")
public class ExchangeRateController {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateController.class);

    @Resource
    private ExchangeRateService exchangeRateService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取汇率列表
     */
    @PostMapping({"/getList", "/list", "/page"})
    @GetMapping({"/getList", "/list", "/page"})
    @ResponseBody
    @ApiOperation("获取汇率列表")
    public String getExchangeRateList(@RequestParam(required = false) Map<String, Object> params,
                                      @RequestParam(required = false) Map<String, Object> queryParams,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            // 合并requestBody和requestParam参数
            if (params == null) params = new HashMap<>();
            if (queryParams != null && !queryParams.isEmpty()) {
                params.putAll(queryParams);
            }

            int pageNum = Integer.parseInt(params.getOrDefault("page", "1").toString());
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", params.getOrDefault("limit", "20")).toString());

            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, pageNum, pageSize);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());

            return new JsonBean(1, "成功", data).toJson();
        } catch (Exception e) {
            log.error("获取汇率列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取汇率详情
     */
    @GetMapping("/detail")
    @ResponseBody
    @ApiOperation("获取汇率详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TblGtExchangeRate rate = exchangeRateService.getById(id);
            if (rate == null) {
                return new JsonBean(0, "汇率不存在", null).toJson();
            }
            return new JsonBean(1, "成功", rate).toJson();
        } catch (Exception e) {
            log.error("获取汇率详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增汇率
     */
    @PostMapping({"/create", "/add"})
    @ResponseBody
    @ApiOperation("新增汇率")
    public String createExchangeRate(@FlexibleRequestBody TblGtExchangeRate exchangeRate,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            exchangeRate.setCreateBy(loginStaff.getUsername());
            exchangeRate.setUpdateBy(loginStaff.getUsername());

            int result = exchangeRateService.createExchangeRate(exchangeRate);
            if (result > 0) {
                return new JsonBean(1, "新增成功", null).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增汇率失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新汇率
     */
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新汇率")
    public String updateExchangeRate(@FlexibleRequestBody TblGtExchangeRate exchangeRate,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            exchangeRate.setUpdateBy(loginStaff.getUsername());

            int result = exchangeRateService.updateExchangeRate(exchangeRate);
            if (result > 0) {
                return new JsonBean(1, "更新成功", null).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新汇率失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除汇率
     */
    @PostMapping("/delete")
    @DeleteMapping({"/delete/{id}", "/delete"})
    @ResponseBody
    @ApiOperation("删除汇率")
    public String deleteExchangeRate(@RequestParam(required = false) Map<String, Object> params,
                                     @PathVariable(required = false) Long id,
                                     @RequestParam(required = false) Long rateId,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            // 支持多种参数获取方式
            Long finalRateId = id != null ? id :
                             (rateId != null ? rateId :
                             (params != null && params.get("rateId") != null ? Long.parseLong(params.get("rateId").toString()) :
                             (params != null && params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null)));

            if (finalRateId == null) {
                return new JsonBean(0, "汇率ID不能为空", null).toJson();
            }

            int result = exchangeRateService.deleteExchangeRate(finalRateId);
            if (result > 0) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除汇率失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取汇率统计信息
     */
    @RequestMapping(value = {"/getStatistics", "/statistics"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation("获取汇率统计信息")
    public String getExchangeRateStatistics(@RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            Map<String, Object> statistics = exchangeRateService.getStatistics();

            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取汇率统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步汇率
     */
    @PostMapping("/sync")
    @ResponseBody
    @ApiOperation("同步汇率")
    public String syncExchangeRate(@RequestParam(required = false) Map<String, Object> params,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }

            if (params == null) params = new HashMap<>();
            String sourceType = params.getOrDefault("sourceType", "PBOC").toString();
            int result = exchangeRateService.syncExchangeRate(sourceType);

            if (result >= 0) {
                return new JsonBean(1, "同步成功", null).toJson();
            } else {
                return new JsonBean(0, "同步失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("同步汇率失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除汇率
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除汇率")
    public String batchDeleteExchangeRate(@RequestParam Map<String, Object> params,
                                          @RequestHeader(value = "token", required = false) String token,
                                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            int result = exchangeRateService.batchDelete(ids);
            if (result > 0) {
                return new JsonBean(1, "批量删除成功", null).toJson();
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除汇率失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新汇率状态
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    @ApiOperation("更新汇率状态")
    public String updateStatus(@RequestParam Map<String, Object> params,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long id = Long.parseLong(params.get("id").toString());
            Integer status = Integer.parseInt(params.get("status").toString());
            int result = exchangeRateService.updateStatus(id, status);
            if (result > 0) {
                return new JsonBean(1, "状态更新成功", null).toJson();
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新汇率状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 趋势分析
     */
    @PostMapping("/trendAnalysis")
    @ResponseBody
    @ApiOperation("汇率趋势分析")
    public String trendAnalysis(@RequestParam Map<String, Object> params,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = exchangeRateService.getTrendAnalysis(params);
            return new JsonBean(1, "成功", result).toJson();
        } catch (Exception e) {
            log.error("汇率趋势分析失败", e);
            return new JsonBean(0, "分析失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出汇率数据
     */
    @GetMapping("/export")
    @ResponseBody
    @ApiOperation("导出汇率数据")
    public void exportExchangeRate(@RequestParam(required = false) String fromCurrency,
                                   @RequestParam(required = false) String toCurrency,
                                   @RequestParam(required = false) String rateType,
                                   @RequestParam(required = false) Integer status,
                                   HttpServletResponse response) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (fromCurrency != null) params.put("fromCurrency", fromCurrency);
            if (toCurrency != null) params.put("toCurrency", toCurrency);
            if (rateType != null) params.put("rateType", rateType);
            if (status != null) params.put("status", status);

            // 查询数据
            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, 1, 10000);
            List<TblGtExchangeRate> list = pageInfo.getList();

            // 转换为导出DTO
            List<ExportExchangeRateDTO> exportList = list.stream()
                    .map(ExportExchangeRateDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "汇率数据_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("汇率数据", ExportExchangeRateDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出汇率数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /**
     * 导出汇率趋势报告
     */
    @GetMapping("/exportTrendReport")
    @ResponseBody
    @ApiOperation("导出汇率趋势报告")
    public void exportTrendReport(@RequestParam(required = false) String fromCurrency,
                                  @RequestParam(required = false) String toCurrency,
                                  @RequestParam(required = false) String startDate,
                                  @RequestParam(required = false) String endDate,
                                  HttpServletResponse response) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (fromCurrency != null) params.put("fromCurrency", fromCurrency);
            if (toCurrency != null) params.put("toCurrency", toCurrency);
            if (startDate != null) params.put("startDate", startDate);
            if (endDate != null) params.put("endDate", endDate);

            // 查询数据
            PageInfo<TblGtExchangeRate> pageInfo = exchangeRateService.getExchangeRateList(params, 1, 10000);
            List<TblGtExchangeRate> list = pageInfo.getList();

            // 转换为导出DTO
            List<ExportExchangeRateDTO> exportList = list.stream()
                    .map(ExportExchangeRateDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "汇率趋势报告_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("汇率趋势报告", ExportExchangeRateDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出汇率趋势报告失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}
