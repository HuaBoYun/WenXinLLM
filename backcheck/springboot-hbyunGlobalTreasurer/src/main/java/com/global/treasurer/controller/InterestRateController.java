package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.export.ExportInterestRateDTO;
import com.global.treasurer.entity.TblGtInterestRate;
import com.global.treasurer.service.xjgl.dataRulesManage.InterestRateService;
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
 * 利率管理Controller
 * 匹配前端API路径: /qqsk/financial/interestRate/*
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Controller
@RequestMapping({"/financial/xjgl/dataRulesManage/interestRate", "/financial/interestRate"})
@Api(tags = "利率管理")
public class InterestRateController {
    private static final Logger log = LoggerFactory.getLogger(InterestRateController.class);

    @Resource
    private InterestRateService interestRateService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取利率列表
     */
    @PostMapping({"/getList", "/list"})
    @ResponseBody
    @ApiOperation("获取利率列表")
    public String getInterestRateList(@RequestParam(required = false) Map<String, Object> params,
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
            int pageNum = Integer.parseInt(params.getOrDefault("page", "1").toString());
            int pageSize = Integer.parseInt(params.getOrDefault("pageSize", params.getOrDefault("limit", "20")).toString());

            // 筛选参数传入 Service
            Map<String, Object> filterParams = new HashMap<>();
            if (params.get("rateType") != null && !"".equals(params.get("rateType"))) {
                filterParams.put("rateType", params.get("rateType"));
            }
            if (params.get("currencyCode") != null && !"".equals(params.get("currencyCode"))) {
                filterParams.put("currencyCode", params.get("currencyCode"));
            }
            if (params.get("term") != null && !"".equals(params.get("term"))) {
                filterParams.put("term", params.get("term"));
            }
            if (params.get("termUnit") != null && !"".equals(params.get("termUnit"))) {
                filterParams.put("termUnit", params.get("termUnit"));
            }
            if (params.get("rateSource") != null && !"".equals(params.get("rateSource"))) {
                filterParams.put("rateSource", params.get("rateSource"));
            }

            PageInfo<TblGtInterestRate> pageInfo = interestRateService.getInterestRateList(filterParams, pageNum, pageSize);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());

            return new JsonBean(1, "成功", data).toJson();
        } catch (Exception e) {
            log.error("获取利率列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取利率详情
     */
    @GetMapping("/detail")
    @ResponseBody
    @ApiOperation("获取利率详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TblGtInterestRate rate = interestRateService.getById(id);
            if (rate == null) {
                return new JsonBean(0, "利率不存在", null).toJson();
            }
            return new JsonBean(1, "成功", rate).toJson();
        } catch (Exception e) {
            log.error("获取利率详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增利率
     */
    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("新增利率")
    public String createInterestRate(@FlexibleRequestBody TblGtInterestRate interestRate,
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

            interestRate.setCreateBy(loginStaff.getUsername());
            interestRate.setUpdateBy(loginStaff.getUsername());

            int result = interestRateService.createInterestRate(interestRate);
            if (result > 0) {
                return new JsonBean(1, "新增成功", null).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增利率失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新利率
     */
    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新利率")
    public String updateInterestRate(@FlexibleRequestBody TblGtInterestRate interestRate,
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

            interestRate.setUpdateBy(loginStaff.getUsername());

            int result = interestRateService.updateInterestRate(interestRate);
            if (result > 0) {
                return new JsonBean(1, "更新成功", null).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新利率失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除利率
     */
    @PostMapping("/delete")
    @ResponseBody
    @ApiOperation("删除利率")
    public String deleteInterestRate(@RequestParam Map<String, Object> params,
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

            Long rateId = Long.parseLong(params.get("rateId").toString());
            int result = interestRateService.deleteInterestRate(rateId);
            if (result > 0) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除利率失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取利率统计信息
     */
    @RequestMapping(value = {"/getStatistics", "/statistics"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    @ApiOperation("获取利率统计信息")
    public String getInterestRateStatistics(@RequestHeader(value = "token", required = false) String token,
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

            Map<String, Object> statistics = interestRateService.getStatistics();

            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取利率统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 同步利率
     */
    @PostMapping("/sync")
    @ResponseBody
    @ApiOperation("同步利率")
    public String syncInterestRate(@RequestParam(required = false) Map<String, Object> params,
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
            int result = interestRateService.syncInterestRate(sourceType);

            if (result >= 0) {
                return new JsonBean(1, "同步成功", null).toJson();
            } else {
                return new JsonBean(0, "同步失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("同步利率失败", e);
            return new JsonBean(0, "同步失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除利率
     */
    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除利率")
    public String batchDeleteInterestRate(@RequestParam Map<String, Object> params,
                                          @RequestHeader(value = "token", required = false) String token,
                                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            int result = interestRateService.batchDelete(ids);
            if (result > 0) {
                return new JsonBean(1, "批量删除成功", null).toJson();
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除利率失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新利率状态
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    @ApiOperation("更新利率状态")
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
            int result = interestRateService.updateStatus(id, status);
            if (result > 0) {
                return new JsonBean(1, "状态更新成功", null).toJson();
            } else {
                return new JsonBean(0, "状态更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新利率状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出利率数据
     */
    @GetMapping("/export")
    @ResponseBody
    @ApiOperation("导出利率数据")
    public void exportInterestRate(@RequestParam(required = false) String rateType,
                                   @RequestParam(required = false) String currencyCode,
                                   @RequestParam(required = false) Integer status,
                                   HttpServletResponse response) {
        try {
            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (rateType != null) params.put("rateType", rateType);
            if (currencyCode != null) params.put("currencyCode", currencyCode);
            if (status != null) params.put("status", status);

            // 查询数据
            PageInfo<TblGtInterestRate> pageInfo = interestRateService.getInterestRateList(params, 1, 10000);
            List<TblGtInterestRate> list = pageInfo.getList();

            // 转换为导出DTO
            List<ExportInterestRateDTO> exportList = list.stream()
                    .map(ExportInterestRateDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "利率数据_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("利率数据", ExportInterestRateDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出利率数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }
}
