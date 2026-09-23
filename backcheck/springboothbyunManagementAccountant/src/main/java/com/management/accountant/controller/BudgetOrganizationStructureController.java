package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetOrganizationStructure;
import com.management.accountant.service.BudgetOrganizationStructureService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 预算组织体系管理Controller
 * 
 * @description 预算组织体系管理接口，支持5种组织体系类型
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-组织体系管理"})
@RequestMapping(value = "/accountant/budget/organization-structure")
@Slf4j
public class BudgetOrganizationStructureController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetOrganizationStructureService organizationStructureService;

    /**
     * 创建组织体系
     */
    @Operation(summary = "创建组织体系")
    @ApiOperation("创建组织体系")
    @PostMapping("/create")
    public MyJsonBean<BudgetOrganizationStructure> create(
            @RequestBody @Validated BudgetOrganizationStructure structure) {
        MyJsonBean<BudgetOrganizationStructure> result = new MyJsonBean<>();
        try {
            BudgetOrganizationStructure created = organizationStructureService.create(structure);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建组织体系失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建组织体系异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询组织体系详情
     */
    @Operation(summary = "查询组织体系详情")
    @ApiOperation("查询组织体系详情")
    @GetMapping("/detail/{structureId}")
    public MyJsonBean<BudgetOrganizationStructure> getDetail(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId) {
        MyJsonBean<BudgetOrganizationStructure> result = new MyJsonBean<>();
        try {
            BudgetOrganizationStructure structure = organizationStructureService.getById(structureId);
            if (structure != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(structure);
            } else {
                result.setCode(0);
                result.setMsg("组织体系不存在");
            }
        } catch (Exception e) {
            log.error("查询组织体系详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新组织体系
     */
    @Operation(summary = "更新组织体系")
    @ApiOperation("更新组织体系")
    @PutMapping("/update/{structureId}")
    public MyJsonBean<BudgetOrganizationStructure> update(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId,
            @RequestBody @Validated BudgetOrganizationStructure structure) {
        MyJsonBean<BudgetOrganizationStructure> result = new MyJsonBean<>();
        try {
            structure.setStructureId(structureId);
            BudgetOrganizationStructure updated = organizationStructureService.update(structure);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新组织体系失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新组织体系异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除组织体系
     */
    @Operation(summary = "删除组织体系")
    @ApiOperation("删除组织体系")
    @DeleteMapping("/delete/{structureId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            organizationStructureService.delete(structureId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除组织体系失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除组织体系异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询组织体系列表
     */
    @Operation(summary = "分页查询组织体系列表")
    @ApiOperation("分页查询组织体系列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetOrganizationStructure>> getPage(
            @RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetOrganizationStructure>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetOrganizationStructure> pageResult = organizationStructureService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询组织体系列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织体系树结构
     */
    @Operation(summary = "获取组织体系树结构")
    @ApiOperation("获取组织体系树结构")
    @GetMapping("/tree/{structureId}")
    public MyJsonBean<Map<String, Object>> getTree(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> tree = organizationStructureService.getTree(structureId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取组织体系树结构异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除组织体系
     */
    @Operation(summary = "批量删除组织体系")
    @ApiOperation("批量删除组织体系")
    @DeleteMapping("/batch-delete")
    public MyJsonBean<Void> batchDelete(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的数据");
                return result;
            }
            organizationStructureService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除组织体系失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除组织体系异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用组织体系
     */
    @Operation(summary = "启用组织体系")
    @ApiOperation("启用组织体系")
    @PutMapping("/enable/{structureId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            organizationStructureService.enable(structureId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (Exception e) {
            log.error("启用组织体系异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用组织体系
     */
    @Operation(summary = "禁用组织体系")
    @ApiOperation("禁用组织体系")
    @PutMapping("/disable/{structureId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            organizationStructureService.disable(structureId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (Exception e) {
            log.error("禁用组织体系异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量更新启用状态
     */
    @Operation(summary = "批量更新启用状态")
    @ApiOperation("批量更新启用状态")
    @PutMapping("/batch-update-enabled")
    public MyJsonBean<Void> batchUpdateEnabled(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            Object isEnabledObj = params.get("isEnabled");

            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要操作的数据");
                return result;
            }

            // 兼容Integer和String类型
            int isEnabled = 0;
            if (isEnabledObj instanceof Integer) {
                isEnabled = (Integer) isEnabledObj;
            } else if (isEnabledObj instanceof String) {
                isEnabled = Integer.parseInt((String) isEnabledObj);
            } else if (isEnabledObj instanceof Boolean) {
                isEnabled = (Boolean) isEnabledObj ? 1 : 0;
            }

            organizationStructureService.batchUpdateEnabled(ids, isEnabled);
            result.setCode(1);
            result.setMsg(isEnabled == 1 ? "批量启用成功" : "批量禁用成功");
        } catch (ServiceException ex) {
            log.error("批量更新启用状态失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量更新启用状态异常", e);
            result.setCode(0);
            result.setMsg("批量更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制组织体系
     */
    @Operation(summary = "复制组织体系")
    @ApiOperation("复制组织体系")
    @PostMapping("/copy/{structureId}")
    public MyJsonBean<BudgetOrganizationStructure> copy(
            @ApiParam(value = "组织体系ID", required = true) @PathVariable String structureId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetOrganizationStructure> result = new MyJsonBean<>();
        try {
            String newName = (String) params.get("newName");
            String newCode = (String) params.get("newCode");
            BudgetOrganizationStructure copied = organizationStructureService.copy(structureId, newName, newCode);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copied);
        } catch (ServiceException ex) {
            log.error("复制组织体系失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制组织体系异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 检查编码是否存在
     */
    @Operation(summary = "检查编码是否存在")
    @ApiOperation("检查编码是否存在")
    @GetMapping("/check-code/{code}")
    public MyJsonBean<Boolean> checkCode(
            @ApiParam(value = "体系编码", required = true) @PathVariable String code) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean exists = organizationStructureService.checkCodeExists(code);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(exists);
        } catch (Exception e) {
            log.error("检查编码是否存在异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取统计数据
     */
    @Operation(summary = "获取统计数据")
    @ApiOperation("获取统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = organizationStructureService.getStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导入组织体系数据
     */
    @Operation(summary = "导入组织体系数据")
    @ApiOperation("导入组织体系数据")
    @PostMapping("/import")
    public MyJsonBean<Void> importData(@RequestPart("file") MultipartFile file) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            if (file == null || file.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要导入的文件");
                return result;
            }

            List<BudgetOrganizationStructure> importList = new ArrayList<>();
            try (InputStream is = file.getInputStream();
                 Workbook workbook = new XSSFWorkbook(is)) {
                Sheet sheet = workbook.getSheetAt(0);
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row == null) continue;

                    BudgetOrganizationStructure structure = new BudgetOrganizationStructure();
                    structure.setStructureCode(getCellStringValue(row.getCell(0)));
                    structure.setStructureName(getCellStringValue(row.getCell(1)));
                    structure.setStructureType(getCellStringValue(row.getCell(2)));
                    structure.setControlMode(getCellStringValue(row.getCell(3)));
                    Cell maxLevelsCell = row.getCell(4);
                    if (maxLevelsCell != null && maxLevelsCell.getCellType() == CellType.NUMERIC) {
                        structure.setMaxLevels((int) maxLevelsCell.getNumericCellValue());
                    } else {
                        structure.setMaxLevels(5);
                    }
                    Cell enabledCell = row.getCell(5);
                    if (enabledCell != null) {
                        String val = getCellStringValue(enabledCell);
                        structure.setIsEnabled("启用".equals(val) || "1".equals(val) ? 1 : 0);
                    } else {
                        structure.setIsEnabled(1);
                    }
                    structure.setDescription(getCellStringValue(row.getCell(6)));

                    if (structure.getStructureCode() != null && !structure.getStructureCode().isEmpty()
                            && structure.getStructureName() != null && !structure.getStructureName().isEmpty()) {
                        importList.add(structure);
                    }
                }
            }

            if (importList.isEmpty()) {
                result.setCode(0);
                result.setMsg("文件中无有效数据");
                return result;
            }

            int successCount = 0;
            int failCount = 0;
            for (BudgetOrganizationStructure structure : importList) {
                try {
                    organizationStructureService.create(structure);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    log.warn("导入记录失败，编码: {}, 原因: {}", structure.getStructureCode(), e.getMessage());
                }
            }

            result.setCode(1);
            result.setMsg("导入完成，成功" + successCount + "条" + (failCount > 0 ? "，失败" + failCount + "条" : ""));
        } catch (Exception e) {
            log.error("导入组织体系数据异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
}