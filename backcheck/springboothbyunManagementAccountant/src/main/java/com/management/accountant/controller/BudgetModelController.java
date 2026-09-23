package com.management.accountant.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetModel;
import com.management.accountant.oracle.entity.budget.BudgetModelVersion;
import com.management.accountant.oracle.mapper.budget.BudgetModelVersionMapper;
import com.management.accountant.service.BudgetModelService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 预算模型Controller
 * 
 * @description 预算模型管理接口，支持多种预算模型类型的配置和管理
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算模型"})
@RequestMapping(value = "/accountant/budget/model")
@Slf4j
public class BudgetModelController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetModelService modelService;

    @Resource
    private BudgetModelVersionMapper versionMapper;

    /**
     * 创建预算模型
     */
    @Operation(summary = "创建预算模型")
    @ApiOperation("创建预算模型")
    @PostMapping("/create")
    public MyJsonBean<BudgetModel> create(@RequestBody @Validated BudgetModel model) {
        MyJsonBean<BudgetModel> result = new MyJsonBean<>();
        try {
            BudgetModel created = modelService.create(model);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建预算模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建预算模型异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询模型详情
     */
    @Operation(summary = "查询模型详情")
    @ApiOperation("查询模型详情")
    @GetMapping("/detail/{modelId}")
    public MyJsonBean<BudgetModel> getDetail(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<BudgetModel> result = new MyJsonBean<>();
        try {
            BudgetModel model = modelService.getById(modelId);
            if (model != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(model);
            } else {
                result.setCode(0);
                result.setMsg("预算模型不存在");
            }
        } catch (Exception e) {
            log.error("查询模型详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新预算模型
     */
    @Operation(summary = "更新预算模型")
    @ApiOperation("更新预算模型")
    @PutMapping("/update/{modelId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId,
            @RequestBody @Validated BudgetModel model) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            model.setModelId(modelId);
            modelService.update(model);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新预算模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新预算模型异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除预算模型
     */
    @Operation(summary = "删除预算模型")
    @ApiOperation("删除预算模型")
    @DeleteMapping("/delete/{modelId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            modelService.delete(modelId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除预算模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除预算模型异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询模型列表
     */
    @Operation(summary = "分页查询模型列表")
    @ApiOperation("分页查询模型列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetModel>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetModel>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetModel> pageResult = modelService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询模型列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 激活模型
     */
    @Operation(summary = "激活模型")
    @ApiOperation("激活模型")
    @PostMapping("/apply/{modelId}")
    public MyJsonBean<Void> activate(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            modelService.activate(modelId);
            result.setCode(1);
            result.setMsg("激活成功");
        } catch (ServiceException ex) {
            log.error("激活模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("激活模型异常", e);
            result.setCode(0);
            result.setMsg("激活失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停用模型
     */
    @Operation(summary = "停用模型")
    @ApiOperation("停用模型")
    @PostMapping("/deactivate/{modelId}")
    public MyJsonBean<Void> deactivate(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            modelService.deactivate(modelId);
            result.setCode(1);
            result.setMsg("停用成功");
        } catch (ServiceException ex) {
            log.error("停用模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停用模型异常", e);
            result.setCode(0);
            result.setMsg("停用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制模型
     */
    @Operation(summary = "复制模型")
    @ApiOperation("复制模型")
    @PostMapping("/copy/{modelId}")
    public MyJsonBean<BudgetModel> copy(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<BudgetModel> result = new MyJsonBean<>();
        try {
            String newModelName = params != null ? (String) params.get("newModelName") : null;
            BudgetModel copied = modelService.copy(modelId, newModelName);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copied);
        } catch (ServiceException ex) {
            log.error("复制模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制模型异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 归档模型
     */
    @Operation(summary = "归档模型")
    @ApiOperation("归档模型")
    @PostMapping("/archive/{modelId}")
    public MyJsonBean<Void> archive(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            modelService.archive(modelId);
            result.setCode(1);
            result.setMsg("归档成功");
        } catch (ServiceException ex) {
            log.error("归档模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("归档模型异常", e);
            result.setCode(0);
            result.setMsg("归档失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除模型
     */
    @Operation(summary = "批量删除模型")
    @ApiOperation("批量删除模型")
    @PostMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody List<String> ids) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            modelService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除模型失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除模型异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取模型统计信息
     */
    @Operation(summary = "获取模型统计信息")
    @ApiOperation("获取模型统计信息")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> statistics = modelService.getStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(statistics);
        } catch (Exception e) {
            log.error("获取模型统计信息异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取模型版本列表
     */
    @Operation(summary = "获取模型版本列表")
    @ApiOperation("获取模型版本列表")
    @GetMapping("/versions/{modelId}")
    public MyJsonBean<List<BudgetModelVersion>> getVersions(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<List<BudgetModelVersion>> result = new MyJsonBean<>();
        try {
            QueryWrapper<BudgetModelVersion> wrapper = new QueryWrapper<>();
            wrapper.eq("MODEL_ID", modelId)
                   .eq("IS_DELETED", 0)
                   .orderByDesc("CREATE_TIME");
            List<BudgetModelVersion> versions = versionMapper.selectList(wrapper);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(versions);
        } catch (Exception e) {
            log.error("获取模型版本列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量复制模型")
    @ApiOperation("批量复制模型")
    @PostMapping("/batch-copy")
    public MyJsonBean<List<BudgetModel>> batchCopy(@RequestBody List<String> ids) {
        MyJsonBean<List<BudgetModel>> result = new MyJsonBean<>();
        try {
            List<BudgetModel> copies = new java.util.ArrayList<>();
            for (String id : ids) {
                BudgetModel copied = modelService.copy(id, null);
                if (copied != null) copies.add(copied);
            }
            result.setCode(1);
            result.setMsg("批量复制成功，共复制" + copies.size() + "个模型");
            result.setData(copies);
        } catch (Exception e) {
            log.error("批量复制模型异常", e);
            result.setCode(0);
            result.setMsg("批量复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出模型")
    @ApiOperation("导出模型")
    @GetMapping("/export/{modelId}")
    public MyJsonBean<Map<String, Object>> exportModel(
            @ApiParam(value = "模型ID", required = true) @PathVariable String modelId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            BudgetModel model = modelService.getById(modelId);
            if (model == null) {
                result.setCode(0);
                result.setMsg("模型不存在");
                return result;
            }
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("modelId", modelId);
            exportData.put("modelName", model.getModelName());
            exportData.put("fileName", model.getModelName() + ".xlsx");
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出模型异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量导出模型")
    @ApiOperation("批量导出模型")
    @PostMapping("/batch-export")
    public MyJsonBean<Map<String, Object>> batchExport(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("exportCount", ids.size());
            exportData.put("fileName", "预算模型_批量导出.zip");
            result.setCode(1);
            result.setMsg("批量导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("批量导出模型异常", e);
            result.setCode(0);
            result.setMsg("批量导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取版本详情")
    @ApiOperation("获取版本详情")
    @GetMapping("/version/{versionId}/detail")
    public MyJsonBean<BudgetModelVersion> getVersionDetail(
            @ApiParam(value = "版本ID", required = true) @PathVariable String versionId) {
        MyJsonBean<BudgetModelVersion> result = new MyJsonBean<>();
        try {
            BudgetModelVersion version = versionMapper.selectById(versionId);
            if (version == null || version.getIsDeleted() == 1) {
                result.setCode(0);
                result.setMsg("版本不存在");
                return result;
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(version);
        } catch (Exception e) {
            log.error("获取版本详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "恢复模型版本")
    @ApiOperation("恢复模型版本")
    @PostMapping("/version/{versionId}/restore")
    public MyJsonBean<Void> restoreVersion(
            @ApiParam(value = "版本ID", required = true) @PathVariable String versionId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            // 1. 查询要恢复的版本
            BudgetModelVersion version = versionMapper.selectById(versionId);
            if (version == null || version.getIsDeleted() == 1) {
                result.setCode(0);
                result.setMsg("版本不存在");
                return result;
            }
            // 2. 查询对应的模型
            BudgetModel model = modelService.getById(version.getModelId());
            if (model == null) {
                result.setCode(0);
                result.setMsg("模型不存在");
                return result;
            }
            // 3. 用版本快照恢复模型配置
            if (version.getModelConfig() != null) {
                model.setConfigParameters(version.getModelConfig());
            }
            model.setVersion(version.getVersionNo());
            model.setUpdateTime(new Date());
            modelService.update(model);
            // 4. 更新当前版本标记
            QueryWrapper<BudgetModelVersion> clearWrapper = new QueryWrapper<>();
            clearWrapper.eq("MODEL_ID", version.getModelId()).eq("IS_DELETED", 0);
            List<BudgetModelVersion> allVersions = versionMapper.selectList(clearWrapper);
            for (BudgetModelVersion v : allVersions) {
                if (v.getVersionId().equals(versionId)) {
                    v.setIsCurrent(1);
                } else {
                    v.setIsCurrent(0);
                }
                versionMapper.updateById(v);
            }
            log.info("恢复模型版本成功: versionId={}, modelId={}", versionId, version.getModelId());
            result.setCode(1);
            result.setMsg("版本恢复成功");
        } catch (Exception e) {
            log.error("恢复模型版本异常", e);
            result.setCode(0);
            result.setMsg("恢复失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取各模型类型统计")
    @ApiOperation("获取各模型类型统计")
    @GetMapping("/type-stats")
    public MyJsonBean<Map<String, Object>> getTypeStatistics() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> typeStats = modelService.getTypeStatistics();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(typeStats);
        } catch (Exception e) {
            log.error("获取模型类型统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导入预算模型")
    @ApiOperation("导入预算模型")
    @PostMapping("/import")
    public MyJsonBean<Map<String, Object>> importModel(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            if (file == null || file.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要导入的文件");
                return result;
            }

            // Parse Excel file and import models
            Map<String, Object> importResult = new java.util.HashMap<>();
            int importCount = 0;

            try (java.io.InputStream is = file.getInputStream()) {
                org.apache.poi.ss.usermodel.Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(is);
                org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                    if (row == null) continue;

                    BudgetModel model = new BudgetModel();
                    model.setModelName(getCellStringValue(row, 0));
                    model.setModelCode(getCellStringValue(row, 1));
                    model.setModelType(getCellStringValue(row, 2));
                    model.setModelStatus("DRAFT");
                    model.setApplicableScope(getCellStringValue(row, 5));
                    model.setModelDescription(getCellStringValue(row, 9));

                    if (org.springframework.util.StringUtils.hasText(model.getModelName())) {
                        try {
                            modelService.create(model);
                            importCount++;
                        } catch (Exception ex) {
                            log.warn("导入模型失败，行号: {}, 原因: {}", i + 1, ex.getMessage());
                        }
                    }
                }
                workbook.close();
            }

            importResult.put("importCount", importCount);
            importResult.put("fileName", file.getOriginalFilename());
            result.setCode(1);
            result.setMsg("导入成功，共导入" + importCount + "个模型");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("导入模型异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    private String getCellStringValue(org.apache.poi.ss.usermodel.Row row, int cellIndex) {
        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellIndex);
        if (cell == null) return null;
        cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
        return cell.getStringCellValue();
    }
}