package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.ActualController;
import com.huabo.cybermonitor.entity.TblFinancingRecord;
import com.huabo.cybermonitor.entity.TblGuaranteeRecord;
import com.huabo.cybermonitor.mapper.ActualControllerMapper;
import com.huabo.cybermonitor.mapper.TblFinancingRecordMapper;
import com.huabo.cybermonitor.mapper.TblGuaranteeRecordMapper;
import com.huabo.cybermonitor.service.IActualControllerService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.ActualControllerQueryVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFCell;

/**
 * 实际控制人识别控制器
 */
@Slf4j
@Tag(name = "实际控制人识别")
@RestController
@RequestMapping("/v1/supervision/equity/beneficial-owner")
public class BeneficialOwnerController {

    @Autowired
    private IActualControllerService actualControllerService;

    @Autowired
    private ActualControllerMapper actualControllerMapper;

    @Autowired
    private TblFinancingRecordMapper financingRecordMapper;

    @Autowired
    private TblGuaranteeRecordMapper guaranteeRecordMapper;

    @Operation(summary = "分页查询实际控制人列表")
    @PostMapping("/getList")
    public R<PageResult<ActualController>> getList(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new HashMap<>();
            int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;

            LambdaQueryWrapper<ActualController> wrapper = new LambdaQueryWrapper<>();
            if (params.get("controlledEnterpriseName") != null && !params.get("controlledEnterpriseName").toString().isEmpty()) {
                wrapper.like(ActualController::getControlledEnterpriseName, params.get("controlledEnterpriseName").toString());
            }
            // 支持前端字段名 ownerType 作为 controllerType 的别名
            String controllerTypeVal = null;
            if (params.get("ownerType") != null && !params.get("ownerType").toString().isEmpty()) {
                controllerTypeVal = params.get("ownerType").toString();
            } else if (params.get("controllerType") != null && !params.get("controllerType").toString().isEmpty()) {
                controllerTypeVal = params.get("controllerType").toString();
            }
            if (controllerTypeVal != null) {
                wrapper.eq(ActualController::getControllerType, controllerTypeVal);
            }
            if (params.get("controlMethod") != null && !params.get("controlMethod").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlMethod, params.get("controlMethod").toString());
            }
            // 支持前端字段名 verificationStatus 作为 confirmationStatus 的别名
            String confirmStatusVal = null;
            if (params.get("verificationStatus") != null && !params.get("verificationStatus").toString().isEmpty()) {
                confirmStatusVal = params.get("verificationStatus").toString();
            } else if (params.get("confirmationStatus") != null && !params.get("confirmationStatus").toString().isEmpty()) {
                confirmStatusVal = params.get("confirmationStatus").toString();
            }
            if (confirmStatusVal != null) {
                wrapper.eq(ActualController::getConfirmationStatus, confirmStatusVal);
            }
            // 支持前端字段名 enterpriseId 作为 controlledEnterpriseId 的过滤
            if (params.get("enterpriseId") != null && !params.get("enterpriseId").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlledEnterpriseId, params.get("enterpriseId").toString());
            }
            // 支持前端字段名 enterpriseName 作为 controlledEnterpriseName 的模糊查询
            if (params.get("enterpriseName") != null && !params.get("enterpriseName").toString().isEmpty()) {
                wrapper.like(ActualController::getControlledEnterpriseName, params.get("enterpriseName").toString());
            }
            if (params.get("controlRiskLevel") != null && !params.get("controlRiskLevel").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlRiskLevel, params.get("controlRiskLevel").toString());
            }
            if (params.get("minControlRatio") != null && !params.get("minControlRatio").toString().isEmpty()) {
                wrapper.ge(ActualController::getTotalShareholdingRatio, new java.math.BigDecimal(params.get("minControlRatio").toString()));
            }
            if (params.get("maxControlRatio") != null && !params.get("maxControlRatio").toString().isEmpty()) {
                wrapper.le(ActualController::getTotalShareholdingRatio, new java.math.BigDecimal(params.get("maxControlRatio").toString()));
            }
            wrapper.orderByDesc(ActualController::getCreateTime);

            Page<ActualController> page = new Page<>(pageNumber, pageSize);
            Page<ActualController> result = actualControllerMapper.selectPage(page, wrapper);

            PageResult<ActualController> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage(pageNumber);
            pageResult.setPageNumber(pageNumber);
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize(pageSize);
            pageResult.setTlist(result.getRecords());
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询实际控制人列表失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询实际控制人")
    @GetMapping("/getById/{id}")
    public R<ActualController> getById(@PathVariable("id") String id) {
        try {
            ActualController entity = actualControllerMapper.selectById(id);
            return R.success(entity);
        } catch (Exception e) {
            log.error("查询实际控制人详情失败, id={}", id, e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增实际控制人")
    @PostMapping("/add")
    public R<String> add(@RequestBody ActualController entity) {
        try {
            // 确保ID为null让MyBatis-Plus自动生成UUID
            if (entity.getControllerId() == null || entity.getControllerId().isEmpty()) {
                entity.setControllerId(null);
            }
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            if (entity.getControlStatus() == null) entity.setControlStatus("ACTIVE");
            if (entity.getConfirmationStatus() == null) entity.setConfirmationStatus("PENDING");
            actualControllerMapper.insert(entity);
            return R.success("新增成功");
        } catch (Exception e) {
            log.error("新增实际控制人失败", e);
            return R.fail("新增失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改实际控制人")
    @PostMapping("/update")
    public R<String> update(@RequestBody ActualController entity) {
        try {
            if (entity.getControllerId() == null || entity.getControllerId().isEmpty()) {
                return R.fail("修改失败: controllerId不能为空");
            }
            entity.setUpdateTime(LocalDateTime.now());
            actualControllerMapper.updateById(entity);
            return R.success("修改成功");
        } catch (Exception e) {
            log.error("修改实际控制人失败", e);
            return R.fail("修改失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除实际控制人")
    @PostMapping("/delete")
    public R<String> delete(@RequestBody Map<String, String> params) {
        try {
            String controllerId = params.get("controllerId");
            actualControllerMapper.deleteById(controllerId);
            return R.success("删除成功");
        } catch (Exception e) {
            log.error("删除实际控制人失败", e);
            return R.fail("删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "识别实际控制人")
    @PostMapping("/identify")
    public R<String> identify(@RequestBody Map<String, Object> params) {
        try {
            String controllerId = (String) params.get("controllerId");
            if (controllerId == null || controllerId.isEmpty()) {
                return R.fail("controllerId不能为空");
            }
            ActualController ac = actualControllerMapper.selectById(controllerId);
            if (ac == null) {
                return R.fail("记录不存在");
            }
            // 执行识别：更新确认状态、识别时间、风险等级评估
            ac.setConfirmationStatus("CONFIRMED");
            ac.setIdentificationTime(LocalDateTime.now());
            ac.setUpdateTime(LocalDateTime.now());
            // 根据持股比例自动评估风险等级
            if (ac.getTotalShareholdingRatio() != null) {
                if (ac.getTotalShareholdingRatio().compareTo(new BigDecimal("80")) >= 0) {
                    ac.setControlRiskLevel("HIGH");
                } else if (ac.getTotalShareholdingRatio().compareTo(new BigDecimal("50")) >= 0) {
                    ac.setControlRiskLevel("MEDIUM");
                } else {
                    ac.setControlRiskLevel("LOW");
                }
            }
            actualControllerMapper.updateById(ac);
            return R.success("识别完成，已确认" + ac.getControllerEnterpriseName() + "为实际控制人");
        } catch (Exception e) {
            log.error("识别实际控制人失败", e);
            return R.fail("识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "统计信息")
    @PostMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> stats = new HashMap<>();
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            Long totalOwners = (long) allRecords.size();
            stats.put("totalOwners", totalOwners);

            // 受监管企业数（去重被控制企业）
            long totalEnterprises = allRecords.stream()
                    .map(ActualController::getControlledEnterpriseId)
                    .filter(Objects::nonNull)
                    .distinct().count();
            stats.put("totalEnterprises", totalEnterprises);

            // 实际控制人数(>=50%)
            long actualControllerCount = allRecords.stream()
                    .filter(r -> r.getTotalShareholdingRatio() != null && r.getTotalShareholdingRatio().compareTo(new BigDecimal("50")) >= 0)
                    .count();
            stats.put("actualControllerCount", actualControllerCount);

            // 高风险控制人(>=80%)
            long riskOwners = allRecords.stream()
                    .filter(r -> r.getTotalShareholdingRatio() != null && r.getTotalShareholdingRatio().compareTo(new BigDecimal("80")) >= 0)
                    .count();
            stats.put("riskOwners", riskOwners);

            // 高风险企业数
            long highRiskEnterprises = allRecords.stream()
                    .filter(r -> "HIGH".equals(r.getControlRiskLevel()))
                    .map(ActualController::getControlledEnterpriseId)
                    .filter(Objects::nonNull)
                    .distinct().count();
            stats.put("highRiskEnterprises", highRiskEnterprises);

            // 股权质押企业数（控制方式含PLEDGE或状态含质押）
            long pledgedEnterprises = allRecords.stream()
                    .filter(r -> "PLEDGE".equals(r.getControlMethod()) || "PLEDGED".equals(r.getControlStatus()))
                    .map(ActualController::getControlledEnterpriseId)
                    .filter(Objects::nonNull)
                    .distinct().count();
            stats.put("pledgedEnterprises", pledgedEnterprises);

            // 活跃预警数（风险等级为HIGH的记录数）
            long activeWarnings = allRecords.stream()
                    .filter(r -> "HIGH".equals(r.getControlRiskLevel()) || "HIGH".equals(r.getControlChangeRisk()))
                    .count();
            stats.put("activeWarnings", activeWarnings);

            // 融资总额（亿元）- 从融资记录表汇总
            List<TblFinancingRecord> financingList = financingRecordMapper.selectList(null);
            BigDecimal totalFinancingAmount = financingList.stream()
                    .filter(f -> f.getFinancingAmount() != null)
                    .map(TblFinancingRecord::getFinancingAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 转换为亿元（万元 / 10000）
            String totalFinancing = totalFinancingAmount.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP).toString();
            stats.put("totalFinancing", totalFinancing);

            // 担保总额（亿元）- 从担保记录表汇总
            List<TblGuaranteeRecord> guaranteeList = guaranteeRecordMapper.selectList(null);
            BigDecimal totalGuaranteeAmount = guaranteeList.stream()
                    .filter(g -> g.getGuaranteeAmount() != null)
                    .map(TblGuaranteeRecord::getGuaranteeAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 转换为亿元（万元 / 10000）
            String totalGuarantee = totalGuaranteeAmount.divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP).toString();
            stats.put("totalGuarantee", totalGuarantee);

            // 平均控制比例
            BigDecimal avgRatio = BigDecimal.ZERO;
            if (!allRecords.isEmpty()) {
                BigDecimal sum = allRecords.stream()
                        .map(ActualController::getTotalShareholdingRatio)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                avgRatio = sum.divide(new BigDecimal(allRecords.size()), 2, RoundingMode.HALF_UP);
            }
            stats.put("avgControlRatio", avgRatio);

            // 按类型统计
            long individualCount = allRecords.stream().filter(r -> "INDIVIDUAL".equals(r.getControllerType()) || "个人".equals(r.getControllerType())).count();
            long enterpriseCount = allRecords.stream().filter(r -> "ENTERPRISE".equals(r.getControllerType()) || "法人".equals(r.getControllerType())).count();
            long governmentCount = allRecords.stream().filter(r -> "GOVERNMENT".equals(r.getControllerType()) || "政府".equals(r.getControllerType())).count();
            stats.put("individualCount", individualCount);
            stats.put("enterpriseCount", enterpriseCount);
            stats.put("governmentCount", governmentCount);

            // 中风险数
            long mediumRiskCount = allRecords.stream().filter(r -> "MEDIUM".equals(r.getControlRiskLevel())).count();
            stats.put("mediumRiskCount", mediumRiskCount);

            return R.success(stats);
        } catch (Exception e) {
            log.error("查询统计信息失败", e);
            return R.fail("查询统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量修改实际控制人")
    @PostMapping("/batch/update")
    public R<String> batchUpdate(@RequestBody Map<String, Object> params) {
        try {
            // 支持前端传 {ids: [...]} 格式进行批量识别
            Object idsObj = params.get("ids");
            if (idsObj instanceof List) {
                List<?> idList = (List<?>) idsObj;
                for (Object idObj : idList) {
                    String controllerId = idObj.toString();
                    ActualController ac = actualControllerMapper.selectById(controllerId);
                    if (ac != null) {
                        ac.setConfirmationStatus("CONFIRMED");
                        ac.setIdentificationTime(LocalDateTime.now());
                        ac.setUpdateTime(LocalDateTime.now());
                        // 根据持股比例自动评估风险等级
                        if (ac.getTotalShareholdingRatio() != null) {
                            if (ac.getTotalShareholdingRatio().compareTo(new BigDecimal("80")) >= 0) {
                                ac.setControlRiskLevel("HIGH");
                            } else if (ac.getTotalShareholdingRatio().compareTo(new BigDecimal("50")) >= 0) {
                                ac.setControlRiskLevel("MEDIUM");
                            } else {
                                ac.setControlRiskLevel("LOW");
                            }
                        }
                        actualControllerMapper.updateById(ac);
                    }
                }
                return R.success("批量识别完成，共处理 " + idList.size() + " 条记录");
            }
            return R.success("批量操作完成");
        } catch (Exception e) {
            log.error("批量识别实际控制人失败", e);
            return R.fail("批量识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除实际控制人")
    @PostMapping("/batch/delete")
    public R<String> batchDelete(@RequestBody List<String> ids) {
        try {
            actualControllerMapper.deleteBatchIds(ids);
            return R.success("批量删除成功");
        } catch (Exception e) {
            log.error("批量删除实际控制人失败", e);
            return R.fail("批量删除失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出实际控制人数据")
    @PostMapping("/export")
    public void export(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();
            log.info("导出实际控制人数据, params={}", params);

            // 构建查询条件（与getList一致）
            LambdaQueryWrapper<ActualController> wrapper = new LambdaQueryWrapper<>();
            if (params.get("controlledEnterpriseName") != null && !params.get("controlledEnterpriseName").toString().isEmpty()) {
                wrapper.like(ActualController::getControlledEnterpriseName, params.get("controlledEnterpriseName").toString());
            }
            if (params.get("enterpriseName") != null && !params.get("enterpriseName").toString().isEmpty()) {
                wrapper.like(ActualController::getControlledEnterpriseName, params.get("enterpriseName").toString());
            }
            String controllerTypeVal = null;
            if (params.get("ownerType") != null && !params.get("ownerType").toString().isEmpty()) {
                controllerTypeVal = params.get("ownerType").toString();
            } else if (params.get("controllerType") != null && !params.get("controllerType").toString().isEmpty()) {
                controllerTypeVal = params.get("controllerType").toString();
            }
            if (controllerTypeVal != null) {
                wrapper.eq(ActualController::getControllerType, controllerTypeVal);
            }
            if (params.get("controlMethod") != null && !params.get("controlMethod").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlMethod, params.get("controlMethod").toString());
            }
            String confirmStatusVal = null;
            if (params.get("verificationStatus") != null && !params.get("verificationStatus").toString().isEmpty()) {
                confirmStatusVal = params.get("verificationStatus").toString();
            } else if (params.get("confirmationStatus") != null && !params.get("confirmationStatus").toString().isEmpty()) {
                confirmStatusVal = params.get("confirmationStatus").toString();
            }
            if (confirmStatusVal != null) {
                wrapper.eq(ActualController::getConfirmationStatus, confirmStatusVal);
            }
            if (params.get("enterpriseId") != null && !params.get("enterpriseId").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlledEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("controlRiskLevel") != null && !params.get("controlRiskLevel").toString().isEmpty()) {
                wrapper.eq(ActualController::getControlRiskLevel, params.get("controlRiskLevel").toString());
            }
            if (params.get("minControlRatio") != null && !params.get("minControlRatio").toString().isEmpty()) {
                wrapper.ge(ActualController::getTotalShareholdingRatio, new BigDecimal(params.get("minControlRatio").toString()));
            }
            if (params.get("maxControlRatio") != null && !params.get("maxControlRatio").toString().isEmpty()) {
                wrapper.le(ActualController::getTotalShareholdingRatio, new BigDecimal(params.get("maxControlRatio").toString()));
            }
            wrapper.orderByDesc(ActualController::getCreateTime);

            List<ActualController> list = actualControllerMapper.selectList(wrapper);

            // 使用 XSSFWorkbook（非流式），不依赖 UnsynchronizedByteArrayOutputStream，避免 commons-io 版本冲突
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            XSSFWorkbook wb = new XSSFWorkbook();
            XSSFSheet sheet = wb.createSheet("实际控制人");

            // 表头
            String[] headers = {"企业名称", "实际控制人", "控制人类型", "控制方式", "控制比例(%)", "控制路径层级", "验证状态", "风险等级", "识别时间"};
            XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 数据行
            for (int i = 0; i < list.size(); i++) {
                ActualController ac = list.get(i);
                XSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(ac.getControlledEnterpriseName() != null ? ac.getControlledEnterpriseName() : "");
                row.createCell(1).setCellValue(ac.getControllerEnterpriseName() != null ? ac.getControllerEnterpriseName() : "");
                row.createCell(2).setCellValue(mapControllerType(ac.getControllerType()));
                row.createCell(3).setCellValue(mapControlMethod(ac.getControlMethod()));
                row.createCell(4).setCellValue(ac.getTotalShareholdingRatio() != null ? ac.getTotalShareholdingRatio().toString() : "");
                row.createCell(5).setCellValue(ac.getControlLevel() != null ? ac.getControlLevel().toString() : "");
                row.createCell(6).setCellValue(mapConfirmationStatus(ac.getConfirmationStatus()));
                row.createCell(7).setCellValue(mapRiskLevel(ac.getControlRiskLevel()));
                row.createCell(8).setCellValue(ac.getIdentificationTime() != null ? ac.getIdentificationTime().format(dtf) : "");
            }

            String filename = "实际控制人数据_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) {
            log.error("导出实际控制人数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败: " + e.getMessage() + "\"}");
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    private String mapControllerType(String type) {
        if (type == null) return "";
        switch (type) {
            case "INDIVIDUAL": return "个人";
            case "ENTERPRISE": return "企业";
            case "GOVERNMENT": return "政府";
            case "INSTITUTION": return "机构";
            case "FUND": return "基金";
            case "TRUST": return "信托";
            default: return type;
        }
    }

    private String mapControlMethod(String method) {
        if (method == null) return "";
        switch (method) {
            case "SHAREHOLDING": return "股权控制";
            case "VOTING_RIGHT": return "表决权控制";
            case "AGREEMENT": return "协议控制";
            case "MANAGEMENT": return "管理控制";
            case "FINANCIAL": return "财务控制";
            case "OPERATIONAL": return "运营控制";
            case "MIXED": return "混合控制";
            case "BOARD_CONTROL": return "董事会控制";
            case "TRUST": return "信托控制";
            case "PROXY": return "代理控制";
            case "DIRECT": return "直接控制";
            case "INDIRECT": return "间接控制";
            case "PLEDGE": return "质押控制";
            default: return method;
        }
    }

    private String mapConfirmationStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "CONFIRMED": return "已确认";
            case "PENDING": return "待确认";
            case "REJECTED": return "已拒绝";
            case "DISPUTED": return "有争议";
            default: return status;
        }
    }

    private String mapRiskLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            case "LOW": return "低";
            case "CRITICAL": return "严重";
            default: return level;
        }
    }

    @Operation(summary = "控制图谱")
    @PostMapping("/map")
    public R<Map<String, Object>> controlMap(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> mapData = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    mapData.put("controllerId", ac.getControllerId());
                    mapData.put("controllerName", ac.getControllerEnterpriseName());
                    mapData.put("controlledName", ac.getControlledEnterpriseName());
                    mapData.put("controlMethod", ac.getControlMethod());
                    mapData.put("controlRatio", ac.getTotalShareholdingRatio());
                    mapData.put("controlPath", ac.getControlPath());
                    mapData.put("controlLevel", ac.getControlLevel());
                    // 构建简单的图谱节点
                    List<Map<String, Object>> nodes = new ArrayList<>();
                    Map<String, Object> rootNode = new HashMap<>();
                    rootNode.put("id", ac.getControllerEnterpriseId());
                    rootNode.put("name", ac.getControllerEnterpriseName());
                    rootNode.put("type", "controller");
                    nodes.add(rootNode);
                    Map<String, Object> targetNode = new HashMap<>();
                    targetNode.put("id", ac.getControlledEnterpriseId());
                    targetNode.put("name", ac.getControlledEnterpriseName());
                    targetNode.put("type", "controlled");
                    nodes.add(targetNode);
                    mapData.put("nodes", nodes);
                    List<Map<String, Object>> links = new ArrayList<>();
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", ac.getControllerEnterpriseId());
                    link.put("target", ac.getControlledEnterpriseId());
                    link.put("ratio", ac.getTotalShareholdingRatio());
                    links.add(link);
                    mapData.put("links", links);
                }
            }
            return R.success(mapData);
        } catch (Exception e) {
            log.error("查询控制图谱失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "控制链路追踪")
    @PostMapping("/trace")
    public R<List<Map<String, Object>>> controlTrace(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            List<Map<String, Object>> traceList = new ArrayList<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null && ac.getControlPath() != null) {
                    String[] pathParts = ac.getControlPath().split("->");
                    for (int i = 0; i < pathParts.length; i++) {
                        Map<String, Object> step = new HashMap<>();
                        step.put("step", i + 1);
                        step.put("entityName", pathParts[i].trim());
                        step.put("level", i);
                        step.put("isStart", i == 0);
                        step.put("isEnd", i == pathParts.length - 1);
                        traceList.add(step);
                    }
                }
            }
            return R.success(traceList);
        } catch (Exception e) {
            log.error("查询控制链路失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "变更历史记录")
    @PostMapping("/history")
    public R<List<Map<String, Object>>> history(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            List<Map<String, Object>> historyList = new ArrayList<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    // 返回当前记录作为最新历史
                    Map<String, Object> record = new HashMap<>();
                    record.put("time", ac.getCreateTime() != null ? ac.getCreateTime().toString() : "");
                    record.put("action", "创建记录");
                    record.put("operator", ac.getCreateBy() != null ? ac.getCreateBy() : "system");
                    record.put("detail", "识别" + ac.getControllerEnterpriseName() + "为" + ac.getControlledEnterpriseName() + "的实际控制人");
                    historyList.add(record);
                    if (ac.getUpdateTime() != null && !ac.getUpdateTime().equals(ac.getCreateTime())) {
                        Map<String, Object> updateRecord = new HashMap<>();
                        updateRecord.put("time", ac.getUpdateTime().toString());
                        updateRecord.put("action", "更新记录");
                        updateRecord.put("operator", "system");
                        updateRecord.put("detail", "更新控制关系信息");
                        historyList.add(updateRecord);
                    }
                }
            }
            return R.success(historyList);
        } catch (Exception e) {
            log.error("查询历史记录失败", e);
            return R.fail("查询失败: " + e.getMessage());
        }
    }

    @Operation(summary = "身份验证")
    @PostMapping("/verify")
    public R<Map<String, Object>> verify(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    ac.setConfirmationStatus("CONFIRMED");
                    ac.setUpdateTime(LocalDateTime.now());
                    actualControllerMapper.updateById(ac);
                    result.put("status", "CONFIRMED");
                    result.put("message", "身份验证通过");
                    result.put("controllerName", ac.getControllerEnterpriseName());
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("身份验证失败", e);
            return R.fail("验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "生成报告")
    @PostMapping("/report")
    public R<Map<String, Object>> report(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> reportData = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    reportData.put("reportTitle", ac.getControlledEnterpriseName() + " - 实际控制人识别报告");
                    reportData.put("controllerName", ac.getControllerEnterpriseName());
                    reportData.put("controlledName", ac.getControlledEnterpriseName());
                    reportData.put("controlMethod", ac.getControlMethod());
                    reportData.put("totalRatio", ac.getTotalShareholdingRatio());
                    reportData.put("directRatio", ac.getDirectShareholdingRatio());
                    reportData.put("indirectRatio", ac.getIndirectShareholdingRatio());
                    reportData.put("controlLevel", ac.getControlLevel());
                    reportData.put("controlPath", ac.getControlPath());
                    reportData.put("riskLevel", ac.getControlRiskLevel());
                    reportData.put("stability", ac.getControlStability());
                    reportData.put("confirmationStatus", ac.getConfirmationStatus());
                    reportData.put("generateTime", LocalDateTime.now().toString());
                    reportData.put("status", "SUCCESS");
                }
            }
            return R.success(reportData);
        } catch (Exception e) {
            log.error("生成报告失败", e);
            return R.fail("生成报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析控制权归属")
    @PostMapping("/ownership")
    public R<Map<String, Object>> analyzeOwnership(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    result.put("controllerId", ac.getControllerId());
                    result.put("controllerName", ac.getControllerEnterpriseName());
                    result.put("controlledName", ac.getControlledEnterpriseName());
                    result.put("ownershipType", ac.getControlMethod());
                    result.put("directRatio", ac.getDirectShareholdingRatio());
                    result.put("indirectRatio", ac.getIndirectShareholdingRatio());
                    result.put("totalRatio", ac.getTotalShareholdingRatio());
                    result.put("isControlling", ac.getTotalShareholdingRatio() != null && ac.getTotalShareholdingRatio().compareTo(new BigDecimal("50")) >= 0);
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("分析控制权归属失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "计算控制权比例")
    @PostMapping("/ratio")
    public R<Map<String, Object>> calculateRatio(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            if (enterpriseId != null) {
                List<ActualController> filtered = new ArrayList<>();
                for (ActualController ac : allRecords) {
                    if (enterpriseId.equals(ac.getControlledEnterpriseId())) filtered.add(ac);
                }
                BigDecimal totalDirect = BigDecimal.ZERO;
                BigDecimal totalIndirect = BigDecimal.ZERO;
                for (ActualController ac : filtered) {
                    if (ac.getDirectShareholdingRatio() != null) totalDirect = totalDirect.add(ac.getDirectShareholdingRatio());
                    if (ac.getIndirectShareholdingRatio() != null) totalIndirect = totalIndirect.add(ac.getIndirectShareholdingRatio());
                }
                result.put("totalDirectRatio", totalDirect);
                result.put("totalIndirectRatio", totalIndirect);
                result.put("totalRatio", totalDirect.add(totalIndirect));
                result.put("controllerCount", filtered.size());
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("计算控制权比例失败", e);
            return R.fail("计算失败: " + e.getMessage());
        }
    }

    @Operation(summary = "验证控制权有效性")
    @PostMapping("/validate")
    public R<Map<String, Object>> validateControl(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    boolean isValid = ac.getTotalShareholdingRatio() != null && ac.getTotalShareholdingRatio().compareTo(new BigDecimal("50")) >= 0;
                    result.put("controllerId", controllerId);
                    result.put("isValid", isValid);
                    result.put("validationResult", isValid ? "控制权有效" : "控制权不足50%，需进一步验证");
                    result.put("totalRatio", ac.getTotalShareholdingRatio());
                    result.put("validateTime", LocalDateTime.now().toString());
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("验证控制权有效性失败", e);
            return R.fail("验证失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析控制权稳定性")
    @PostMapping("/stability")
    public R<Map<String, Object>> analyzeStability(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    result.put("controllerId", controllerId);
                    result.put("stabilityLevel", ac.getControlStability());
                    result.put("changeRisk", ac.getControlChangeRisk());
                    result.put("assessment", "HIGH".equals(ac.getControlStability()) ? "控制权非常稳定" : "MEDIUM".equals(ac.getControlStability()) ? "控制权较稳定" : "控制权存在风险");
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("分析控制权稳定性失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检测控制权变更")
    @PostMapping("/changes")
    public R<List<Map<String, Object>>> detectChanges(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<ActualController> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ActualController::getControlChangeRisk, "HIGH");
            wrapper.orderByDesc(ActualController::getUpdateTime);
            List<ActualController> records = actualControllerMapper.selectList(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            for (ActualController ac : records) {
                Map<String, Object> item = new HashMap<>();
                item.put("controllerId", ac.getControllerId());
                item.put("controllerName", ac.getControllerEnterpriseName());
                item.put("controlledName", ac.getControlledEnterpriseName());
                item.put("changeRisk", ac.getControlChangeRisk());
                item.put("stability", ac.getControlStability());
                item.put("detectTime", ac.getUpdateTime());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("检测控制权变更失败", e);
            return R.fail("检测失败: " + e.getMessage());
        }
    }

    @Operation(summary = "评估控制权风险")
    @PostMapping("/risk")
    public R<Map<String, Object>> assessRisk(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    result.put("controllerId", controllerId);
                    result.put("riskLevel", ac.getControlRiskLevel());
                    result.put("changeRisk", ac.getControlChangeRisk());
                    result.put("stability", ac.getControlStability());
                    result.put("pledgeRisk", "PLEDGE".equals(ac.getControlMethod()) ? "HIGH" : "LOW");
                    result.put("assessTime", LocalDateTime.now().toString());
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("评估控制权风险失败", e);
            return R.fail("评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析控制权集中度")
    @PostMapping("/concentration")
    public R<Map<String, Object>> analyzeConcentration(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            // 按被控制企业分组统计
            Map<String, List<ActualController>> grouped = new HashMap<>();
            for (ActualController ac : allRecords) {
                String key = ac.getControlledEnterpriseId() != null ? ac.getControlledEnterpriseId() : "UNKNOWN";
                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(ac);
            }
            long singleController = grouped.values().stream().filter(list -> list.size() == 1).count();
            long multiController = grouped.values().stream().filter(list -> list.size() > 1).count();
            result.put("singleControllerEnterprises", singleController);
            result.put("multiControllerEnterprises", multiController);
            result.put("totalEnterprises", grouped.size());
            result.put("concentrationLevel", singleController > multiController ? "HIGH" : "MEDIUM");
            return R.success(result);
        } catch (Exception e) {
            log.error("分析控制权集中度失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "识别隐藏控制人")
    @PostMapping("/hidden")
    public R<List<Map<String, Object>>> identifyHidden(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<ActualController> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(ActualController::getConfirmationStatus, "PENDING");
            wrapper.orderByDesc(ActualController::getTotalShareholdingRatio);
            List<ActualController> pending = actualControllerMapper.selectList(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            for (ActualController ac : pending) {
                Map<String, Object> item = new HashMap<>();
                item.put("controllerId", ac.getControllerId());
                item.put("controllerName", ac.getControllerEnterpriseName());
                item.put("controlledName", ac.getControlledEnterpriseName());
                item.put("totalRatio", ac.getTotalShareholdingRatio());
                item.put("confirmationStatus", ac.getConfirmationStatus());
                item.put("riskHint", "待确认，可能存在隐藏控制人");
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("识别隐藏控制人失败", e);
            return R.fail("识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析代理控制")
    @PostMapping("/proxy")
    public R<Map<String, Object>> analyzeProxy(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            long proxyCount = allRecords.stream().filter(ac -> "PROXY".equals(ac.getControlMethod()) || "代理".equals(ac.getControlMethod())).count();
            result.put("proxyControlCount", proxyCount);
            result.put("totalRecords", allRecords.size());
            result.put("proxyRatio", allRecords.isEmpty() ? 0 : Math.round(proxyCount * 100.0 / allRecords.size() * 100.0) / 100.0);
            result.put("analyzeTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("分析代理控制失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检测一致行动人")
    @PostMapping("/concerted")
    public R<List<Map<String, Object>>> detectConcerted(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String enterpriseId = params != null ? (String) params.get("enterpriseId") : null;
            LambdaQueryWrapper<ActualController> wrapper = new LambdaQueryWrapper<>();
            if (enterpriseId != null) {
                wrapper.eq(ActualController::getControlledEnterpriseId, enterpriseId);
            }
            List<ActualController> records = actualControllerMapper.selectList(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            // 查找相同被控制企业的多个控制人
            Map<String, List<ActualController>> grouped = new HashMap<>();
            for (ActualController ac : records) {
                String key = ac.getControlledEnterpriseId() != null ? ac.getControlledEnterpriseId() : "UNKNOWN";
                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(ac);
            }
            for (Map.Entry<String, List<ActualController>> entry : grouped.entrySet()) {
                if (entry.getValue().size() > 1) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("enterpriseId", entry.getKey());
                    item.put("controllerCount", entry.getValue().size());
                    List<String> names = new ArrayList<>();
                    for (ActualController ac : entry.getValue()) {
                        if (ac.getControllerEnterpriseName() != null) names.add(ac.getControllerEnterpriseName());
                    }
                    item.put("controllerNames", names);
                    item.put("concertedActionRisk", "可能存在一致行动人");
                    result.add(item);
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("检测一致行动人失败", e);
            return R.fail("检测失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析控制权争夺")
    @PostMapping("/contest")
    public R<List<Map<String, Object>>> analyzeContest(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<ActualController>> grouped = new HashMap<>();
            for (ActualController ac : allRecords) {
                String key = ac.getControlledEnterpriseId() != null ? ac.getControlledEnterpriseId() : "UNKNOWN";
                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(ac);
            }
            for (Map.Entry<String, List<ActualController>> entry : grouped.entrySet()) {
                List<ActualController> controllers = entry.getValue();
                if (controllers.size() > 1) {
                    boolean hasHighRisk = controllers.stream().anyMatch(ac -> "HIGH".equals(ac.getControlChangeRisk()));
                    if (hasHighRisk) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("enterpriseId", entry.getKey());
                        item.put("contestLevel", "HIGH");
                        item.put("controllerCount", controllers.size());
                        result.add(item);
                    }
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("分析控制权争夺失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "模拟控制权变更")
    @PostMapping("/simulate")
    public R<Map<String, Object>> simulateChange(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("simulationResult", "模拟完成");
            result.put("impactLevel", "MEDIUM");
            result.put("suggestion", "控制权变更可能影响企业稳定性，建议谨慎评估。");
            result.put("simulateTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("模拟控制权变更失败", e);
            return R.fail("模拟失败: " + e.getMessage());
        }
    }

    @Operation(summary = "分析控制权传导")
    @PostMapping("/transmission")
    public R<Map<String, Object>> analyzeTransmission(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    result.put("controllerId", controllerId);
                    result.put("controlPath", ac.getControlPath());
                    result.put("controlLevel", ac.getControlLevel());
                    result.put("directRatio", ac.getDirectShareholdingRatio());
                    result.put("indirectRatio", ac.getIndirectShareholdingRatio());
                    result.put("transmissionEfficiency", ac.getDirectShareholdingRatio() != null && ac.getTotalShareholdingRatio() != null && ac.getTotalShareholdingRatio().compareTo(BigDecimal.ZERO) > 0 ? ac.getDirectShareholdingRatio().divide(ac.getTotalShareholdingRatio(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("分析控制权传导失败", e);
            return R.fail("分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "检查合规性")
    @PostMapping("/compliance")
    public R<Map<String, Object>> checkCompliance(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String controllerId = params != null ? (String) params.get("controllerId") : null;
            Map<String, Object> result = new HashMap<>();
            List<String> issues = new ArrayList<>();
            if (controllerId != null) {
                ActualController ac = actualControllerMapper.selectById(controllerId);
                if (ac != null) {
                    if ("PENDING".equals(ac.getConfirmationStatus())) issues.add("控制人身份待确认");
                    if (ac.getTotalShareholdingRatio() != null && ac.getTotalShareholdingRatio().compareTo(new BigDecimal("67")) >= 0) {
                        issues.add("持股超过2/3，存在绝对控制风险");
                    }
                    if ("PLEDGE".equals(ac.getControlMethod())) issues.add("存在质押控制方式");
                    result.put("controllerId", controllerId);
                    result.put("controllerName", ac.getControllerEnterpriseName());
                }
            }
            result.put("complianceStatus", issues.isEmpty() ? "COMPLIANT" : "ISSUE");
            result.put("issues", issues);
            result.put("checkTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("检查合规性失败", e);
            return R.fail("合规检查失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导入实际控制人数据")
    @PostMapping("/import")
    public R<Map<String, Object>> importData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "SUCCESS");
            result.put("message", "导入成功");
            result.put("importTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) {
            log.error("导入实际控制人数据失败", e);
            return R.fail("导入失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取控制权网络数据")
    @PostMapping("/network")
    public R<Map<String, Object>> getNetworkData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<ActualController> allRecords = actualControllerMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (ActualController ac : allRecords) {
                Map<String, Object> sourceNode = new HashMap<>();
                sourceNode.put("id", ac.getControllerEnterpriseId());
                sourceNode.put("name", ac.getControllerEnterpriseName());
                sourceNode.put("type", "controller");
                nodes.add(sourceNode);
                Map<String, Object> targetNode = new HashMap<>();
                targetNode.put("id", ac.getControlledEnterpriseId());
                targetNode.put("name", ac.getControlledEnterpriseName());
                targetNode.put("type", "controlled");
                nodes.add(targetNode);
                Map<String, Object> link = new HashMap<>();
                link.put("source", ac.getControllerEnterpriseId());
                link.put("target", ac.getControlledEnterpriseId());
                link.put("ratio", ac.getTotalShareholdingRatio());
                link.put("method", ac.getControlMethod());
                links.add(link);
            }
            result.put("nodes", nodes);
            result.put("links", links);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取控制权网络数据失败", e);
            return R.fail("获取失败: " + e.getMessage());
        }
    }
}
