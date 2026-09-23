package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetDimension;
import com.management.accountant.oracle.entity.budget.BudgetDimensionAttribute;
import com.management.accountant.oracle.entity.budget.BudgetDimensionRelation;
import com.management.accountant.service.BudgetDimensionService;
import com.management.accountant.util.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 预算维度配置管理Controller
 *
 * @description 预算维度配置管理接口，支持多维度预算分析
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-维度配置管理"})
@RequestMapping(value = "/accountant/budget/dimension")
@Slf4j
public class BudgetDimensionController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDimensionService dimensionService;

    /**
     * 创建维度
     */
    @Operation(summary = "创建维度")
    @ApiOperation("创建维度")
    @PostMapping("/create")
    public MyJsonBean<BudgetDimension> create(@RequestBody @Validated BudgetDimension dimension) {
        MyJsonBean<BudgetDimension> result = new MyJsonBean<>();
        try {
            BudgetDimension created = dimensionService.create(dimension);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建维度失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建维度异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询维度详情
     */
    @Operation(summary = "查询维度详情")
    @ApiOperation("查询维度详情")
    @GetMapping("/detail/{dimensionId}")
    public MyJsonBean<BudgetDimension> getDetail(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<BudgetDimension> result = new MyJsonBean<>();
        try {
            BudgetDimension dimension = dimensionService.getById(dimensionId);
            if (dimension != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(dimension);
            } else {
                result.setCode(0);
                result.setMsg("维度不存在");
            }
        } catch (Exception e) {
            log.error("查询维度详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新维度
     */
    @Operation(summary = "更新维度")
    @ApiOperation("更新维度")
    @PutMapping("/update/{dimensionId}")
    public MyJsonBean<BudgetDimension> update(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId,
            @RequestBody @Validated BudgetDimension dimension) {
        MyJsonBean<BudgetDimension> result = new MyJsonBean<>();
        try {
            dimension.setDimensionId(dimensionId);
            BudgetDimension updated = dimensionService.update(dimension);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新维度失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新维度异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除维度
     */
    @Operation(summary = "删除维度")
    @ApiOperation("删除维度")
    @DeleteMapping("/delete/{dimensionId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.delete(dimensionId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除维度失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除维度异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询维度列表
     */
    @Operation(summary = "分页查询维度列表")
    @ApiOperation("分页查询维度列表")
    @PostMapping("/page")
    public MyJsonBean<Map<String, Object>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dimensionService.getPage(params));
        } catch (Exception e) {
            log.error("分页查询维度列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取维度树结构
     */
    @Operation(summary = "获取维度树结构")
    @ApiOperation("获取维度树结构")
    @GetMapping("/tree")
    public MyJsonBean<List<Map<String, Object>>> getTree() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> tree = dimensionService.getTree();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取维度树结构异常", e);
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
            Map<String, Object> stats = dimensionService.getStats();
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
     * 批量删除维度
     */
    @Operation(summary = "批量删除维度")
    @ApiOperation("批量删除维度")
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
            dimensionService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除维度失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除维度异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取子维度列表
     */
    @Operation(summary = "获取子维度列表")
    @ApiOperation("获取子维度列表")
    @GetMapping("/children/{parentId}")
    public MyJsonBean<List<BudgetDimension>> getChildren(
            @ApiParam(value = "父维度ID", required = true) @PathVariable String parentId) {
        MyJsonBean<List<BudgetDimension>> result = new MyJsonBean<>();
        try {
            List<BudgetDimension> children = dimensionService.getChildren(parentId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(children);
        } catch (Exception e) {
            log.error("获取子维度列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取维度值列表
     */
    @Operation(summary = "获取维度值列表")
    @ApiOperation("获取维度值列表")
    @GetMapping("/values/{dimensionId}")
    public MyJsonBean<List<Map<String, Object>>> getValues(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> values = dimensionService.getValues(dimensionId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(values);
        } catch (Exception e) {
            log.error("获取维度值列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量创建维度值
     */
    @Operation(summary = "批量创建维度值")
    @ApiOperation("批量创建维度值")
    @PostMapping("/values/batch/{dimensionId}")
    public MyJsonBean<Void> batchCreateValues(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId,
            @RequestBody List<Map<String, Object>> values) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.batchCreateValues(dimensionId, values);
            result.setCode(1);
            result.setMsg("批量创建成功");
        } catch (ServiceException ex) {
            log.error("批量创建维度值失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量创建维度值异常", e);
            result.setCode(0);
            result.setMsg("批量创建失败：" + e.getMessage());
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
            @ApiParam(value = "维度编码", required = true) @PathVariable String code) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean exists = dimensionService.checkCodeExists(code);
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
     * 获取维度属性
     */
    @Operation(summary = "获取维度属性")
    @ApiOperation("获取维度属性")
    @GetMapping("/attributes/{dimensionId}")
    public MyJsonBean<List<BudgetDimensionAttribute>> getAttributes(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<List<BudgetDimensionAttribute>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dimensionService.getAttributes(dimensionId));
        } catch (Exception e) {
            log.error("获取维度属性异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取维度关联关系
     */
    @Operation(summary = "获取维度关联关系")
    @ApiOperation("获取维度关联关系")
    @GetMapping("/relations/{dimensionId}")
    public MyJsonBean<List<BudgetDimensionRelation>> getRelations(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<List<BudgetDimensionRelation>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dimensionService.getRelations(dimensionId));
        } catch (Exception e) {
            log.error("获取维度关联关系异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取父级维度列表
     */
    @Operation(summary = "获取父级维度列表")
    @ApiOperation("获取父级维度列表")
    @GetMapping("/parents")
    public MyJsonBean<List<BudgetDimension>> getParents() {
        MyJsonBean<List<BudgetDimension>> result = new MyJsonBean<>();
        try {
            List<BudgetDimension> parents = dimensionService.getParents();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(parents);
        } catch (Exception e) {
            log.error("获取父级维度列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新维度状态
     */
    @Operation(summary = "更新维度状态")
    @ApiOperation("更新维度状态")
    @PutMapping("/{id}/status")
    public MyJsonBean<Void> updateStatus(
            @ApiParam(value = "维度ID", required = true) @PathVariable String id,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            Boolean isActive = (Boolean) params.get("isActive");
            dimensionService.updateStatus(id, isActive);
            result.setCode(1);
            result.setMsg("状态更新成功");
        } catch (ServiceException ex) {
            log.error("更新维度状态失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新维度状态异常", e);
            result.setCode(0);
            result.setMsg("状态更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量验证维度
     */
    @Operation(summary = "批量验证维度")
    @ApiOperation("批量验证维度")
    @PostMapping("/batch-validate")
    public MyJsonBean<Map<String, Object>> batchValidate(@RequestBody List<String> ids) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> validateResult = dimensionService.batchValidate(ids);
            result.setCode(1);
            result.setMsg("批量验证完成");
            result.setData(validateResult);
        } catch (Exception e) {
            log.error("批量验证维度异常", e);
            result.setCode(0);
            result.setMsg("批量验证失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出维度数据
     */
    @Operation(summary = "导出维度数据")
    @ApiOperation("导出维度数据")
    @PostMapping("/export")
    public void exportDimension(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        dimensionService.exportDimension(params, response);
    }

    /**
     * 获取维度成员列表
     */
    @Operation(summary = "获取维度成员列表")
    @ApiOperation("获取维度成员列表")
    @GetMapping("/members/{dimensionId}")
    public MyJsonBean<List<Map<String, Object>>> getMembers(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dimensionService.getMembers(dimensionId));
        } catch (Exception e) {
            log.error("获取维度成员列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存维度成员
     */
    @Operation(summary = "保存维度成员")
    @ApiOperation("保存维度成员")
    @PutMapping("/members/{dimensionId}")
    public MyJsonBean<Void> saveMembers(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId,
            @RequestBody List<Map<String, Object>> members) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.saveMembers(dimensionId, members);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存维度成员异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导入维度成员
     */
    @Operation(summary = "导入维度成员")
    @ApiOperation("导入维度成员")
    @PostMapping("/members/{dimensionId}/import")
    public MyJsonBean<Map<String, Object>> importMembers(
            @ApiParam(value = "维度ID", required = true) @PathVariable String dimensionId,
            @ApiParam("Excel文件") @RequestParam("file") MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(dimensionService.importMembers(dimensionId, file));
        } catch (Exception e) {
            log.error("导入维度成员异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }


    /**
     * 删除维度成员
     */
    @Operation(summary = "删除维度成员")
    @ApiOperation("删除维度成员")
    @DeleteMapping("/members/item/{memberId}")
    public MyJsonBean<Void> deleteMember(
            @ApiParam(value = "成员ID", required = true) @PathVariable String memberId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.deleteMember(memberId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除维度成员异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建维度属性
     */
    @Operation(summary = "创建维度属性")
    @ApiOperation("创建维度属性")
    @PostMapping("/attribute/create")
    public MyJsonBean<BudgetDimensionAttribute> createAttribute(@RequestBody BudgetDimensionAttribute attribute) {
        MyJsonBean<BudgetDimensionAttribute> result = new MyJsonBean<>();
        try {
            BudgetDimensionAttribute created = dimensionService.createAttribute(attribute);
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(created);
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建维度属性异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新维度属性
     */
    @Operation(summary = "更新维度属性")
    @ApiOperation("更新维度属性")
    @PutMapping("/attribute/update/{attributeId}")
    public MyJsonBean<BudgetDimensionAttribute> updateAttribute(
            @PathVariable String attributeId,
            @RequestBody BudgetDimensionAttribute attribute) {
        MyJsonBean<BudgetDimensionAttribute> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(dimensionService.updateAttribute(attributeId, attribute));
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新维度属性异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除维度属性
     */
    @Operation(summary = "删除维度属性")
    @ApiOperation("删除维度属性")
    @DeleteMapping("/attribute/delete/{attributeId}")
    public MyJsonBean<Void> deleteAttribute(@PathVariable String attributeId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.deleteAttribute(attributeId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除维度属性异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 创建维度关联关系
     */
    @Operation(summary = "创建维度关联关系")
    @ApiOperation("创建维度关联关系")
    @PostMapping("/relation/create")
    public MyJsonBean<BudgetDimensionRelation> createRelation(@RequestBody BudgetDimensionRelation relation) {
        MyJsonBean<BudgetDimensionRelation> result = new MyJsonBean<>();
        try {
            BudgetDimensionRelation created = dimensionService.createRelation(relation);
            result.setCode(1);
            result.setMsg("添加成功");
            result.setData(created);
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建维度关联关系异常", e);
            result.setCode(0);
            result.setMsg("添加失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新维度关联关系
     */
    @Operation(summary = "更新维度关联关系")
    @ApiOperation("更新维度关联关系")
    @PutMapping("/relation/update/{relationId}")
    public MyJsonBean<BudgetDimensionRelation> updateRelation(
            @PathVariable String relationId,
            @RequestBody BudgetDimensionRelation relation) {
        MyJsonBean<BudgetDimensionRelation> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(dimensionService.updateRelation(relationId, relation));
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新维度关联关系异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除维度关联关系
     */
    @Operation(summary = "删除维度关联关系")
    @ApiOperation("删除维度关联关系")
    @DeleteMapping("/relation/delete/{relationId}")
    public MyJsonBean<Void> deleteRelation(@PathVariable String relationId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dimensionService.deleteRelation(relationId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除维度关联关系异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出单个维度")
    @ApiOperation("导出单个维度")
    @GetMapping("/{dimensionId}/export")
    public void exportSingle(@PathVariable String dimensionId, HttpServletResponse response) {
        dimensionService.exportSingleDimension(dimensionId, response);
    }

    /**
     * 导入维度数据
     */
    @Operation(summary = "导入维度数据")
    @ApiOperation("导入维度数据")
    @PostMapping("/import/dimensions")
    public MyJsonBean<Map<String, Object>> importDimensions(@RequestParam("file") MultipartFile file) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> importResult = dimensionService.importDimensions(file);
            result.setCode(1);
            result.setMsg("导入成功");
            result.setData(importResult);
        } catch (Exception e) {
            log.error("导入维度数据异常", e);
            result.setCode(0);
            result.setMsg(e.getMessage());
        }
        return result;
    }

    /**
     * 下载维度导入模板
     */
    @Operation(summary = "下载维度导入模板")
    @ApiOperation("下载维度导入模板")
    @GetMapping("/template")
    public void downloadTemplate(HttpServletResponse response) {
        dimensionService.downloadDimensionTemplate(response);
    }
}

