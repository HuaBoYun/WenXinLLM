package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.KpiIndicator;
import com.global.treasurer.service.IKpiIndicatorService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.global.treasurer.annotation.FlexibleRequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * KPI指标控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@RestController
@RequestMapping({"/financial/decision/kpi", "/xjgl/decision/kpi", "/centralaudit/decision/kpi", "/decision/kpi", "/kpiIndicator"})
@Api(tags = "KPI指标管理")
public class KpiIndicatorController {
    private static final Logger log = LoggerFactory.getLogger(KpiIndicatorController.class);

    @Resource
    private IKpiIndicatorService kpiIndicatorService;

    @Resource
    private UserProvider userProvider;

    @GetMapping({"/list", "/page"})
    @ApiOperation("获取KPI指标列表(GET)")
    public String getKpiListGet(
            @ApiParam(value = "页码", example = "1") @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "KPI编码") @RequestParam(required = false) String kpiCode,
            @ApiParam(value = "KPI名称") @RequestParam(required = false) String kpiName,
            @ApiParam(value = "KPI分类") @RequestParam(required = false) String kpiCategory,
            @ApiParam(value = "KPI类型") @RequestParam(required = false) String kpiType,
            @ApiParam(value = "KPI状态") @RequestParam(required = false) String kpiStatus,
            HttpServletResponse response) {

        try {
            log.info("========== KPI指标查询接口(GET) ==========");
            log.info("接收参数 - pageNo: {}, pageSize: {}", pageNum, pageSize);
            log.info("接收参数 - kpiCode: {}, kpiName: {}, kpiCategory: {}, kpiType: {}, kpiStatus: {}",
                    kpiCode, kpiName, kpiCategory, kpiType, kpiStatus);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (kpiCode != null && !kpiCode.trim().isEmpty()) {
                params.put("kpiCode", kpiCode);
            }
            if (kpiName != null && !kpiName.trim().isEmpty()) {
                params.put("kpiName", kpiName);
            }
            if (kpiCategory != null && !kpiCategory.trim().isEmpty()) {
                params.put("kpiCategory", kpiCategory);
            }
            if (kpiType != null && !kpiType.trim().isEmpty()) {
                params.put("kpiType", kpiType);
            }
            if (kpiStatus != null && !kpiStatus.trim().isEmpty()) {
                params.put("kpiStatus", kpiStatus);
            }

            log.info("构建的params: {}", params);

            // 分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<KpiIndicator> page =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            IPage<KpiIndicator> result = kpiIndicatorService.selectPage(page, params);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 当前页: {}, 每页大小: {}, 总记录数: {}, 当前页记录数: {}",
                    result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("获取KPI指标列表失败", e);
            return JsonBean.error("获取KPI指标列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取KPI指标详情（请求参数）")
    public String getKpiDetail(
            @ApiParam(value = "KPI ID", required = true) @RequestParam Long kpiId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            KpiIndicator kpi = kpiIndicatorService.getById(kpiId);
            if (kpi == null) {
                return JsonBean.error("KPI指标不存在");
            }

            return new JsonBean(1, "查询成功", kpi).toString();

        } catch (Exception e) {
            log.error("获取KPI指标详情失败，kpiId: {}", kpiId, e);
            return JsonBean.error("获取KPI指标详情失败: " + e.getMessage());
        }
    }

    @GetMapping({"/{id}", "/{id}/"})
    @ApiOperation("获取KPI指标详情（路径参数）")
    public String getKpiById(
            @ApiParam(value = "KPI ID", required = true) @PathVariable Long id,
            HttpServletResponse response) {

        try {
            log.info("========== 获取KPI指标详情 ==========");
            log.info("接收参数 - KPI ID: {}", id);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            KpiIndicator kpi = kpiIndicatorService.getById(id);
            if (kpi == null) {
                log.warn("KPI指标不存在，id: {}", id);
                return JsonBean.error("KPI指标不存在");
            }

            log.info("查询到的KPI指标: {}", kpi);
            log.info("KPI指标详细信息 - kpiId: {}, kpiCode: {}, kpiName: {}, currentValue: {}, targetValue: {}",
                    kpi.getKpiId(), kpi.getKpiCode(), kpi.getKpiName(),
                    kpi.getCurrentValue(), kpi.getTargetValue());

            // 尝试序列化返回
            try {
                JsonBean result = new JsonBean(1, "查询成功", kpi);
                return result.toString();
            } catch (Exception ex) {
                log.error("序列化KPI指标失败", ex);
                // 如果序列化失败，返回基本信息
                Map<String, Object> basicInfo = new HashMap<>();
                basicInfo.put("kpiId", kpi.getKpiId());
                basicInfo.put("kpiCode", kpi.getKpiCode());
                basicInfo.put("kpiName", kpi.getKpiName());
                basicInfo.put("kpiCategory", kpi.getKpiCategory());
                basicInfo.put("kpiType", kpi.getKpiType());
                basicInfo.put("kpiStatus", kpi.getKpiStatus());
                basicInfo.put("note", "部分字段可能无法显示");
                return new JsonBean(1, "查询成功（部分数据）", basicInfo).toString();
            }

        } catch (Exception e) {
            log.error("获取KPI指标详情失败，id: {}", id, e);
            log.error("异常类型: {}", e.getClass().getName());
            log.error("异常消息: {}", e.getMessage());
            if (e.getCause() != null) {
                log.error("根本原因: {}", e.getCause().getMessage());
            }
            return JsonBean.error("获取KPI指标详情失败: " + e.getMessage());
        }
    }

    @PostMapping(value = {"", "/create"})
    @ApiOperation("创建KPI指标")
    public String createKpi(@FlexibleRequestBody KpiIndicator kpi, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(kpi.getKpiCode())) {
                return JsonBean.error("KPI编码不能为空");
            }
            if (!StringUtils.hasText(kpi.getKpiName())) {
                return JsonBean.error("KPI名称不能为空");
            }
            if (!StringUtils.hasText(kpi.getKpiCategory())) {
                return JsonBean.error("KPI分类不能为空");
            }
            if (!StringUtils.hasText(kpi.getKpiType())) {
                return JsonBean.error("KPI类型不能为空");
            }

            // 设置默认值
            if (kpi.getKpiStatus() == null) {
                kpi.setKpiStatus("INACTIVE");
            }
            if (kpi.getCalculationFrequency() == null) {
                kpi.setCalculationFrequency("DAILY");
            }
            kpi.setCreateTime(java.time.LocalDateTime.now());
            kpi.setUpdateTime(java.time.LocalDateTime.now());
            kpi.setDelFlag("0");

            // 设置创建人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                kpi.setCreateBy(getStaffId(user));
                kpi.setUpdateBy(getStaffId(user));
                if (user.getCurrentOrg() != null) {
                    kpi.setOrgId(getOrgId(user.getCurrentOrg()));
                }
            }

            boolean success = kpiIndicatorService.save(kpi);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建KPI指标失败", e);
            return JsonBean.error("创建KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping(value = {"/update"})
    @PutMapping(value = {"/update"})
    @ApiOperation("更新KPI指标")
    public String updateKpi(@FlexibleRequestBody KpiIndicator kpi, HttpServletResponse response) {

        try {
            // 调试日志
            log.info("[DEBUG-KPI更新] 接收到的KPI对象: {}", kpi);
            log.info("[DEBUG-KPI更新] kpiId值: {}", kpi != null ? kpi.getKpiId() : "KPI对象为null");
            log.info("[DEBUG-KPI更新] kpiName值: {}", kpi != null ? kpi.getKpiName() : "KPI对象为null");

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (kpi == null) {
                log.error("[DEBUG-KPI更新] KPI对象为null!");
                return JsonBean.error("KPI对象不能为空");
            }

            if (kpi.getKpiId() == null) {
                log.error("[DEBUG-KPI更新] KPI ID为空! 完整对象: {}", kpi);
                return JsonBean.error("KPI ID不能为空");
            }

            // 检查是否存在
            KpiIndicator existing = kpiIndicatorService.getById(kpi.getKpiId());
            if (existing == null) {
                return JsonBean.error("KPI指标不存在");
            }

            kpi.setUpdateTime(java.time.LocalDateTime.now());

            // 设置更新人
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                kpi.setUpdateBy(getStaffId(user));
            }

            boolean success = kpiIndicatorService.updateById(kpi);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新KPI指标失败", e);
            return JsonBean.error("更新KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除KPI指标")
    public String deleteKpi(
            @ApiParam(value = "KPI ID", required = true) @RequestParam Long kpiId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            KpiIndicator existing = kpiIndicatorService.getById(kpiId);
            if (existing == null) {
                return JsonBean.error("KPI指标不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = kpiIndicatorService.updateById(existing);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除KPI指标失败，kpiId: {}", kpiId, e);
            return JsonBean.error("删除KPI指标失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{kpiId}")
    @ApiOperation("删除KPI指标(RESTful)")
    public String deleteKpiRestful(
            @ApiParam(value = "KPI ID", required = true) @PathVariable Long kpiId,
            HttpServletResponse response) {

        try {
            log.info("========== 删除KPI指标(RESTful) ==========");
            log.info("接收参数 - KPI ID: {}", kpiId);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            KpiIndicator existing = kpiIndicatorService.getById(kpiId);
            if (existing == null) {
                log.warn("KPI指标不存在，kpiId: {}", kpiId);
                return JsonBean.error("KPI指标不存在");
            }

            // 逻辑删除
            existing.setDelFlag("1");
            existing.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = kpiIndicatorService.updateById(existing);
            if (success) {
                log.info("KPI指标删除成功，kpiId: {}", kpiId);
                return JsonBean.success("删除成功");
            } else {
                log.error("KPI指标删除失败，kpiId: {}", kpiId);
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除KPI指标失败，kpiId: {}", kpiId, e);
            return JsonBean.error("删除KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/calculate")
    @ApiOperation("计算KPI指标")
    public String calculateKpi(
            @ApiParam(value = "KPI ID", required = true) @RequestParam Long kpiId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = kpiIndicatorService.calculateKpi(kpiId);
            if (success) {
                return JsonBean.success("KPI指标计算成功");
            } else {
                return JsonBean.error("KPI指标计算失败");
            }

        } catch (Exception e) {
            log.error("计算KPI指标失败，kpiId: {}", kpiId, e);
            return JsonBean.error("计算KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateCurrentValue")
    @ApiOperation("更新KPI当前值")
    public String updateCurrentValue(
            @ApiParam(value = "KPI ID", required = true) @RequestParam Long kpiId,
            @ApiParam(value = "当前值", required = true) @RequestParam BigDecimal currentValue,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = kpiIndicatorService.updateCurrentValue(kpiId, currentValue);
            if (success) {
                return JsonBean.success("更新KPI当前值成功");
            } else {
                return JsonBean.error("更新KPI当前值失败");
            }

        } catch (Exception e) {
            log.error("更新KPI当前值失败，kpiId: {}", kpiId, e);
            return JsonBean.error("更新KPI当前值失败: " + e.getMessage());
        }
    }

    @PostMapping("/updateStatus")
    @ApiOperation("更新KPI状态")
    public String updateKpiStatus(
            @ApiParam(value = "KPI ID", required = true) @RequestParam Long kpiId,
            @ApiParam(value = "KPI状态", required = true) @RequestParam String kpiStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            boolean success = kpiIndicatorService.updateKpiStatus(kpiId, kpiStatus);
            if (success) {
                return JsonBean.success("更新KPI状态成功");
            } else {
                return JsonBean.error("更新KPI状态失败");
            }

        } catch (Exception e) {
            log.error("更新KPI状态失败，kpiId: {}, kpiStatus: {}", kpiId, kpiStatus, e);
            return JsonBean.error("更新KPI状态失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除KPI指标")
    public String batchDeleteKpis(
            @ApiParam(value = "KPI ID数组", required = true) @RequestParam(value = "kpiIds", required = false) String kpiIdsStr,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 处理逗号分隔的字符串参数
            if (kpiIdsStr == null || kpiIdsStr.trim().isEmpty()) {
                return JsonBean.error("KPI ID列表不能为空");
            }

            // 将字符串分割为Long列表
            List<Long> kpiIds = new java.util.ArrayList<>();
            String[] ids = kpiIdsStr.split(",");
            for (String id : ids) {
                try {
                    kpiIds.add(Long.parseLong(id.trim()));
                } catch (NumberFormatException e) {
                    log.error("无效的KPI ID: {}", id);
                    return JsonBean.error("KPI ID格式错误: " + id);
                }
            }

            int successCount = 0;
            for (Long kpiId : kpiIds) {
                KpiIndicator existing = kpiIndicatorService.getById(kpiId);
                if (existing != null) {
                    existing.setDelFlag("1");
                    existing.setUpdateTime(java.time.LocalDateTime.now());
                    if (kpiIndicatorService.updateById(existing)) {
                        successCount++;
                    }
                }
            }

            return new JsonBean(1, "成功删除" + successCount + "个KPI指标", successCount).toString();

        } catch (Exception e) {
            log.error("批量删除KPI指标失败，kpiIdsStr: {}", kpiIdsStr, e);
            return JsonBean.error("批量删除KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/{kpiId}/calculate")
    @ApiOperation("计算KPI指标(RESTful)")
    public String calculateKpiRestful(
            @ApiParam(value = "KPI ID", required = true) @PathVariable Long kpiId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查KPI是否存在
            KpiIndicator kpi = kpiIndicatorService.getById(kpiId);
            if (kpi == null) {
                return JsonBean.error("KPI指标不存在");
            }

            boolean success = kpiIndicatorService.calculateKpi(kpiId);
            if (success) {
                // 重新查询获取更新后的数据
                KpiIndicator updatedKpi = kpiIndicatorService.getById(kpiId);
                return new JsonBean(1, "KPI指标计算成功", updatedKpi).toString();
            } else {
                return JsonBean.error("KPI指标计算失败");
            }

        } catch (Exception e) {
            log.error("计算KPI指标失败，kpiId: {}", kpiId, e);
            return JsonBean.error("计算KPI指标失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch/calculate")
    @ApiOperation("批量计算KPI指标")
    public String batchCalculateKpis(
            @ApiParam(value = "KPI ID数组", required = true) @RequestParam(value = "kpiIds", required = false) List<Long> kpiIds,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (kpiIds == null || kpiIds.isEmpty()) {
                return JsonBean.error("KPI ID列表不能为空");
            }

            int successCount = 0;
            java.util.Map<String, Object> result = new java.util.HashMap<>();
            java.util.List<Map<String, Object>> calculationResults = new java.util.ArrayList<>();

            for (Long kpiId : kpiIds) {
                KpiIndicator kpi = kpiIndicatorService.getById(kpiId);
                if (kpi != null) {
                    boolean success = kpiIndicatorService.calculateKpi(kpiId);
                    if (success) {
                        successCount++;
                        KpiIndicator updatedKpi = kpiIndicatorService.getById(kpiId);
                        java.util.Map<String, Object> kpiResult = new java.util.HashMap<>();
                        kpiResult.put("kpiId", kpiId);
                        kpiResult.put("kpiName", kpi.getKpiName());
                        kpiResult.put("currentValue", updatedKpi.getCurrentValue());
                        kpiResult.put("status", "SUCCESS");
                        calculationResults.add(kpiResult);
                    } else {
                        java.util.Map<String, Object> kpiResult = new java.util.HashMap<>();
                        kpiResult.put("kpiId", kpiId);
                        kpiResult.put("kpiName", kpi.getKpiName());
                        kpiResult.put("status", "FAILED");
                        calculationResults.add(kpiResult);
                    }
                }
            }

            result.put("total", kpiIds.size());
            result.put("successCount", successCount);
            result.put("failedCount", kpiIds.size() - successCount);
            result.put("details", calculationResults);

            return new JsonBean(1, "批量计算完成，成功" + successCount + "个", result).toString();

        } catch (Exception e) {
            log.error("批量计算KPI指标失败，kpiIds: {}", kpiIds, e);
            return JsonBean.error("批量计算KPI指标失败: " + e.getMessage());
        }
    }

    @PutMapping("/{kpiId}/status")
    @ApiOperation("更新KPI状态(RESTful)")
    public String updateKpiStatusRestful(
            @ApiParam(value = "KPI ID", required = true) @PathVariable Long kpiId,
            @ApiParam(value = "KPI状态", required = true) @RequestParam String kpiStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查KPI是否存在
            KpiIndicator kpi = kpiIndicatorService.getById(kpiId);
            if (kpi == null) {
                return JsonBean.error("KPI指标不存在");
            }

            boolean success = kpiIndicatorService.updateKpiStatus(kpiId, kpiStatus);
            if (success) {
                KpiIndicator updatedKpi = kpiIndicatorService.getById(kpiId);
                return new JsonBean(1, "更新KPI状态成功", updatedKpi).toString();
            } else {
                return JsonBean.error("更新KPI状态失败");
            }

        } catch (Exception e) {
            log.error("更新KPI状态失败，kpiId: {}, kpiStatus: {}", kpiId, kpiStatus, e);
            return JsonBean.error("更新KPI状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出KPI指标")
    public void exportKpiIndicators(
            @ApiParam(value = "KPI编码") @RequestParam(required = false) String kpiCode,
            @ApiParam(value = "KPI名称") @RequestParam(required = false) String kpiName,
            @ApiParam(value = "KPI分类") @RequestParam(required = false) String kpiCategory,
            @ApiParam(value = "KPI类型") @RequestParam(required = false) String kpiType,
            @ApiParam(value = "KPI状态") @RequestParam(required = false) String kpiStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("kpiCode", kpiCode);
            params.put("kpiName", kpiName);
            params.put("kpiCategory", kpiCategory);
            params.put("kpiType", kpiType);
            params.put("kpiStatus", kpiStatus);

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<KpiIndicator> iPage = kpiIndicatorService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<KpiIndicator> kpiList = iPage.getRecords();

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("KPI指标_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建Excel工作簿
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("KPI指标");

            // 创建标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"KPI编码", "KPI名称", "KPI分类", "KPI类型", "目标值", "当前值",
                               "单位", "KPI状态", "计算周期", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (KpiIndicator kpi : kpiList) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(kpi.getKpiCode() != null ? kpi.getKpiCode() : "");
                row.createCell(1).setCellValue(kpi.getKpiName() != null ? kpi.getKpiName() : "");
                row.createCell(2).setCellValue(kpi.getKpiCategory() != null ? kpi.getKpiCategory() : "");
                row.createCell(3).setCellValue(kpi.getKpiType() != null ? kpi.getKpiType() : "");
                row.createCell(4).setCellValue(kpi.getTargetValue() != null ? kpi.getTargetValue().toString() : "");
                row.createCell(5).setCellValue(kpi.getCurrentValue() != null ? kpi.getCurrentValue().toString() : "");
                row.createCell(6).setCellValue(kpi.getUnit() != null ? kpi.getUnit() : "");
                row.createCell(7).setCellValue(kpi.getKpiStatus() != null ? kpi.getKpiStatus() : "");
                row.createCell(8).setCellValue(kpi.getCalculationCycle() != null ? kpi.getCalculationCycle() : "");
                row.createCell(9).setCellValue(kpi.getCreateTime() != null ? kpi.getCreateTime().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出KPI指标成功，共{}条记录", kpiList.size());

        } catch (Exception e) {
            log.error("导出KPI指标失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (java.io.IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }

    @GetMapping("/category/{kpiCategory}")
    @ApiOperation("根据分类查询KPI指标")
    public String getKpisByCategory(
            @ApiParam(value = "KPI分类", required = true) @PathVariable String kpiCategory,
            @ApiParam(value = "组织ID") @RequestParam(required = false) Long orgId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("kpiCategory", kpiCategory);

            // 如果没有传入orgId，使用当前用户的组织ID
            if (orgId == null) {
                TblStaffUtil user = userProvider.get();
                if (user != null && user.getCurrentOrg() != null) {
                    orgId = getOrgId(user.getCurrentOrg());
                }
            }
            if (orgId != null) {
                params.put("orgId", orgId);
            }

            // 查询所有符合条件的数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<KpiIndicator> iPage = kpiIndicatorService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<KpiIndicator> kpiList = iPage.getRecords();

            return new JsonBean(1, "查询成功", kpiList).toString();

        } catch (Exception e) {
            log.error("根据分类查询KPI指标失败，kpiCategory: {}", kpiCategory, e);
            return JsonBean.error("根据分类查询KPI指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/type/{kpiType}")
    @ApiOperation("根据类型查询KPI指标")
    public String getKpisByType(
            @ApiParam(value = "KPI类型", required = true) @PathVariable String kpiType,
            @ApiParam(value = "组织ID") @RequestParam(required = false) Long orgId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("kpiType", kpiType);

            // 如果没有传入orgId，使用当前用户的组织ID
            if (orgId == null) {
                TblStaffUtil user = userProvider.get();
                if (user != null && user.getCurrentOrg() != null) {
                    orgId = getOrgId(user.getCurrentOrg());
                }
            }
            if (orgId != null) {
                params.put("orgId", orgId);
            }

            // 查询所有符合条件的数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<KpiIndicator> iPage = kpiIndicatorService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<KpiIndicator> kpiList = iPage.getRecords();

            return new JsonBean(1, "查询成功", kpiList).toString();

        } catch (Exception e) {
            log.error("根据类型查询KPI指标失败，kpiType: {}", kpiType, e);
            return JsonBean.error("根据类型查询KPI指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/warning")
    @ApiOperation("查询预警KPI指标")
    public String getWarningKpis(
            @ApiParam(value = "组织ID") @RequestParam(required = false) Long orgId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();

            // 如果没有传入orgId，使用当前用户的组织ID
            if (orgId == null) {
                TblStaffUtil user = userProvider.get();
                if (user != null && user.getCurrentOrg() != null) {
                    orgId = getOrgId(user.getCurrentOrg());
                }
            }
            if (orgId != null) {
                params.put("orgId", orgId);
            }

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<KpiIndicator> iPage = kpiIndicatorService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<KpiIndicator> allKpis = iPage.getRecords();

            // 筛选预警状态的KPI（当前值低于阈值下限的80%）
            java.util.List<KpiIndicator> warningKpis = new java.util.ArrayList<>();
            for (KpiIndicator kpi : allKpis) {
                if (kpi.getCurrentValue() != null && kpi.getTargetValue() != null
                    && kpi.getTargetValue().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal warningThreshold = kpi.getTargetValue().multiply(new BigDecimal("0.8"));
                    if (kpi.getCurrentValue().compareTo(warningThreshold) < 0) {
                        warningKpis.add(kpi);
                    }
                }
            }

            return new JsonBean(1, "查询成功", warningKpis).toString();

        } catch (Exception e) {
            log.error("查询预警KPI指标失败", e);
            return JsonBean.error("查询预警KPI指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/critical")
    @ApiOperation("查询临界KPI指标")
    public String getCriticalKpis(
            @ApiParam(value = "组织ID") @RequestParam(required = false) Long orgId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();

            // 如果没有传入orgId，使用当前用户的组织ID
            if (orgId == null) {
                TblStaffUtil user = userProvider.get();
                if (user != null && user.getCurrentOrg() != null) {
                    orgId = getOrgId(user.getCurrentOrg());
                }
            }
            if (orgId != null) {
                params.put("orgId", orgId);
            }

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<KpiIndicator> iPage = kpiIndicatorService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<KpiIndicator> allKpis = iPage.getRecords();

            // 筛选临界状态的KPI（当前值在阈值下限的80%-95%之间）
            java.util.List<KpiIndicator> criticalKpis = new java.util.ArrayList<>();
            for (KpiIndicator kpi : allKpis) {
                if (kpi.getCurrentValue() != null && kpi.getTargetValue() != null
                    && kpi.getTargetValue().compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal lowerThreshold = kpi.getTargetValue().multiply(new BigDecimal("0.8"));
                    BigDecimal upperThreshold = kpi.getTargetValue().multiply(new BigDecimal("0.95"));
                    if (kpi.getCurrentValue().compareTo(lowerThreshold) >= 0
                        && kpi.getCurrentValue().compareTo(upperThreshold) < 0) {
                        criticalKpis.add(kpi);
                    }
                }
            }

            return new JsonBean(1, "查询成功", criticalKpis).toString();

        } catch (Exception e) {
            log.error("查询临界KPI指标失败", e);
            return JsonBean.error("查询临界KPI指标失败: " + e.getMessage());
        }
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser() {
        try {
            // 开发环境: 暂时绕过认证验证
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }

    /**
     * 获取当前用户的StaffId（处理BigDecimal到Long的转换）
     */
    private Long getStaffId(TblStaffUtil user) {
        if (user == null || user.getStaffid() == null) {
            return null;
        }
        return user.getStaffid().longValue();
    }

    /**
     * 获取组织的OrgId（处理BigDecimal到Long的转换）
     */
    private Long getOrgId(com.hbfk.entity.TblOrganizationUtil org) {
        if (org == null || org.getOrgid() == null) {
            return null;
        }
        return org.getOrgid().longValue();
    }
}
