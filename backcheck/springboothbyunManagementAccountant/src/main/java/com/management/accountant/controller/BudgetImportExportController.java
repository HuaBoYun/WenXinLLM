package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.BudgetParameter;
import com.management.accountant.oracle.entity.budget.BudgetParameterExportDTO;
import com.management.accountant.oracle.service.common.ImportExportService;
import com.management.accountant.service.BudgetFreezeService;
import com.management.accountant.service.BudgetParameterService;
import com.management.accountant.util.ExcelUtil;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导入导出Controller
 * 
 * @description 提供统一的导入导出接口
 * @author AI Assistant
 * @date 2026-02-06
 */
@Slf4j
@RestController
@RequestMapping("/accountant/budget")
@Api(tags = "预算管理-导入导出")
public class BudgetImportExportController {

    @Resource
    private ImportExportService importExportService;

    @Resource
    private BudgetFreezeService budgetFreezeService;

    @Resource
    private BudgetParameterService budgetParameterService;

    // ==================== 预算编制模块导入接口 ====================

    @Operation(summary = "参数导入")
    @ApiOperation("参数导入")
    @PostMapping("/parameter/import")
    public MyJsonBean<Map<String, Object>> importParameter(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (!ExcelUtil.validateExcelFile(file)) {
                result.setCode(0); result.setMsg("请上传 .xlsx 或 .xls 格式的Excel文件"); return result;
            }
            List<BudgetParameterExportDTO> dtoList = ExcelUtil.importExcel(file, BudgetParameterExportDTO.class);
            if (dtoList == null || dtoList.isEmpty()) {
                result.setCode(0); result.setMsg("Excel文件中没有数据"); return result;
            }
            List<BudgetParameter> entities = new ArrayList<>();
            for (BudgetParameterExportDTO dto : dtoList) { entities.add(dto.toEntity()); }
            Map<String, Object> importResult = budgetParameterService.importParameters(entities);
            result.setCode(1);
            result.setMsg("导入完成");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("参数导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 期间导入
     */
    @Operation(summary = "期间导入")
    @ApiOperation("期间导入")
    @PostMapping("/period/import")
    public MyJsonBean<Map<String, Object>> importPeriod(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "period", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("期间导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 假设导入
     */
    @Operation(summary = "假设导入")
    @ApiOperation("假设导入")
    @PostMapping("/scenario/assumption/import")
    public MyJsonBean<Map<String, Object>> importAssumption(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "assumption", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("假设导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 公式导入
     */
    @Operation(summary = "公式导入")
    @ApiOperation("公式导入")
    @PostMapping("/formula/import")
    public MyJsonBean<Map<String, Object>> importFormula(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "formula", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("公式导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 规则导入
     */
    @Operation(summary = "规则导入")
    @ApiOperation("规则导入")
    @PostMapping("/rule/import")
    public MyJsonBean<Map<String, Object>> importRule(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "rule", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("规则导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 字段导入
     */
    @Operation(summary = "字段导入")
    @ApiOperation("字段导入")
    @PostMapping("/template/field/import")
    public MyJsonBean<Map<String, Object>> importTemplateField(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "template_field", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("字段导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 维度成员导入
     */
    @Operation(summary = "维度成员导入")
    @ApiOperation("维度成员导入")
    @PostMapping("/dimension/member/import")
    public MyJsonBean<Map<String, Object>> importDimensionMember(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "dimension_member", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("维度成员导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 维度导入
     */
    @Operation(summary = "维度导入")
    @ApiOperation("维度导入")
    @PostMapping("/dimension/import")
    public MyJsonBean<Map<String, Object>> importDimension(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "dimension", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("维度导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 工作流节点导入
     */
    @Operation(summary = "工作流节点导入")
    @ApiOperation("工作流节点导入")
    @PostMapping("/workflow/node/import")
    public MyJsonBean<Map<String, Object>> importWorkflowNode(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");
            
            Map<String, Object> importResult = importExportService.importExcel(
                    file, "workflow_node", companyId, userId);
            
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("工作流节点导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 工作流导入
     */
    @Operation(summary = "工作流导入")
    @ApiOperation("工作流导入")
    @PostMapping("/workflow/import")
    public MyJsonBean<Map<String, Object>> importWorkflow(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "workflow", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("工作流导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 合并规则导入
     */
    @Operation(summary = "合并规则导入")
    @ApiOperation("合并规则导入")
    @PostMapping("/consolidation/rule/import")
    public MyJsonBean<Map<String, Object>> importConsolidationRule(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "consolidation_rule", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("合并规则导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 预算控制模块导入接口 ====================

    /**
     * 控制条件导入
     */
    @Operation(summary = "控制条件导入")
    @ApiOperation("控制条件导入")
    @PostMapping("/control/condition/import")
    public MyJsonBean<Map<String, Object>> importControlCondition(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "control_condition", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("控制条件导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 控制规则导入
     */
    @Operation(summary = "控制规则导入")
    @ApiOperation("控制规则导入")
    @PostMapping("/control/rule/import")
    public MyJsonBean<Map<String, Object>> importControlRule(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "control_rule", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("控制规则导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 预警条件导入
     */
    @Operation(summary = "预警条件导入")
    @ApiOperation("预警条件导入")
    @PostMapping("/warning/condition/import")
    public MyJsonBean<Map<String, Object>> importWarningCondition(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "warning_condition", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("预警条件导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 预警导入
     */
    @Operation(summary = "预警导入")
    @ApiOperation("预警导入")
    @PostMapping("/warning/import")
    public MyJsonBean<Map<String, Object>> importWarning(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "warning", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("预警导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 警报导入
     */
    @Operation(summary = "警报导入")
    @ApiOperation("警报导入")
    @PostMapping("/alert/import")
    public MyJsonBean<Map<String, Object>> importAlert(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "alert", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("警报导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 限额导入
     */
    @Operation(summary = "限额导入")
    @ApiOperation("限额导入")
    @PostMapping("/limit/import")
    public MyJsonBean<Map<String, Object>> importLimit(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "limit", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("限额导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 冻结导入
     */
    @Operation(summary = "冻结导入")
    @ApiOperation("冻结导入")
    @PostMapping("/freeze/import")
    public MyJsonBean<Map<String, Object>> importFreeze(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (!ExcelUtil.validateExcelFile(file)) {
                result.setCode(0);
                result.setMsg("请上传 .xlsx 或 .xls 格式的Excel文件");
                return result;
            }

            List<Map<String, Object>> dataList = ExcelUtil.importExcelToMap(file);
            if (dataList == null || dataList.isEmpty()) {
                result.setCode(0);
                result.setMsg("Excel文件中没有数据");
                return result;
            }

            Map<String, Object> importResult = budgetFreezeService.importData(dataList);
            result.setCode(1);
            result.setMsg("导入完成");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("冻结导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 配额导入
     */
    @Operation(summary = "配额导入")
    @ApiOperation("配额导入")
    @PostMapping("/quota/import")
    public MyJsonBean<Map<String, Object>> importQuota(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "quota", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("配额导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 释放导入
     */
    @Operation(summary = "释放导入")
    @ApiOperation("释放导入")
    @PostMapping("/release/import")
    public MyJsonBean<Map<String, Object>> importRelease(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "release", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("释放导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 储备导入
     */
    @Operation(summary = "储备导入")
    @ApiOperation("储备导入")
    @PostMapping("/reserve/import")
    public MyJsonBean<Map<String, Object>> importReserve(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "reserve", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("储备导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 转移导入
     */
    @Operation(summary = "转移导入")
    @ApiOperation("转移导入")
    @PostMapping("/transfer/import")
    public MyJsonBean<Map<String, Object>> importTransfer(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "transfer", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("转移导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 监控导入
     */
    @Operation(summary = "监控导入")
    @ApiOperation("监控导入")
    @PostMapping("/monitor/import")
    public MyJsonBean<Map<String, Object>> importMonitor(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");
            String userId = request.getHeader("userId");

            Map<String, Object> importResult = importExportService.importExcel(
                    file, "monitor", companyId, userId);

            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("监控导入异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 导出接口 ====================

    /**
     * 维度配置导出
     */
    @Operation(summary = "维度配置导出")
    @ApiOperation("维度配置导出")
    @GetMapping("/dimension/config/export")
    public void exportDimensionConfig(
            @RequestParam(required = false) String dimensionId,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String companyId = request.getHeader("companyId");

            Map<String, Object> params = new HashMap<>();
            params.put("dimensionId", dimensionId);

            importExportService.exportExcel("dimension_config", params, companyId, response);
        } catch (Exception e) {
            log.error("维度配置导出异常", e);
        }
    }

    /**
     * 指标管理导出
     */
    @Operation(summary = "指标管理导出")
    @ApiOperation("指标管理导出")
    @GetMapping("/indicator/export")
    public void exportIndicator(
            @RequestParam(required = false) String indicatorType,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String companyId = request.getHeader("companyId");

            Map<String, Object> params = new HashMap<>();
            params.put("indicatorType", indicatorType);

            importExportService.exportExcel("indicator", params, companyId, response);
        } catch (Exception e) {
            log.error("指标管理导出异常", e);
        }
    }

    /**
     * 组织结构导出
     */
    @Operation(summary = "组织结构导出")
    @ApiOperation("组织结构导出")
    @GetMapping("/organization-structure/export")
    public void exportOrganizationStructure(
            @RequestParam(required = false) String structureName,
            @RequestParam(required = false) String structureType,
            @RequestParam(required = false) String controlMode,
            @RequestParam(required = false) String status,
            HttpServletRequest request,
            HttpServletResponse response) {
        try {
            String companyId = request.getHeader("companyId");

            Map<String, Object> params = new HashMap<>();
            if (structureName != null && !structureName.trim().isEmpty()) {
                params.put("structureName", structureName);
            }
            if (structureType != null && !structureType.trim().isEmpty()) {
                params.put("structureType", structureType);
            }
            if (controlMode != null && !controlMode.trim().isEmpty()) {
                params.put("controlMode", controlMode);
            }
            if (status != null && !status.trim().isEmpty()) {
                params.put("status", status);
            }

            importExportService.exportExcel("organization_structure", params, companyId, response);
        } catch (Exception e) {
            log.error("组织结构导出异常", e);
        }
    }

    // ==================== 辅助接口 ====================

    /**
     * 下载导入模板
     */
    @Operation(summary = "下载导入模板")
    @ApiOperation("下载导入模板")
    @GetMapping("/template/download")
    public void downloadTemplate(
            @ApiParam("模板类型") @RequestParam String templateType,
            HttpServletResponse response) {
        try {
            importExportService.downloadTemplate(templateType, response);
        } catch (Exception e) {
            log.error("下载模板异常", e);
        }
    }

    /**
     * 获取导入历史
     */
    @Operation(summary = "获取导入历史")
    @ApiOperation("获取导入历史")
    @GetMapping("/import/history")
    public MyJsonBean<Map<String, Object>> getImportHistory(
            @RequestParam String importType,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "15") Integer pageSize,
            HttpServletRequest request) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            String companyId = request.getHeader("companyId");

            Map<String, Object> history = importExportService.getImportHistory(
                    importType, companyId, pageNo, pageSize);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取导入历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 验证导入数据
     */
    @Operation(summary = "验证导入数据")
    @ApiOperation("验证导入数据")
    @PostMapping("/import/validate")
    public MyJsonBean<Map<String, Object>> validateImportData(
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file,
            @ApiParam("导入类型") @RequestParam String importType) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = importExportService.validateImportData(file, importType);

            result.setCode(1);
            result.setMsg("验证完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("验证导入数据异常", e);
            result.setCode(0);
            result.setMsg("验证失败：" + e.getMessage());
        }
        return result;
    }
}

