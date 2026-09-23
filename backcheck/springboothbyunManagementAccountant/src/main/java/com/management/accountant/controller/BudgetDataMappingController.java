package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.integration.BudgetDataMapping;
import com.management.accountant.service.BudgetDataMappingService;
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
import java.util.Map;

/**
 * 预算数据映射Controller
 * 
 * @description 预算数据映射管理接口，支持字段映射、规则配置等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-数据映射"})
@RequestMapping(value = "/accountant/budget/data/mapping")
@Slf4j
public class BudgetDataMappingController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetDataMappingService dataMappingService;

    /**
     * 创建数据映射
     */
    @Operation(summary = "创建数据映射")
    @ApiOperation("创建数据映射")
    @PostMapping("/create")
    public MyJsonBean<BudgetDataMapping> create(@RequestBody @Validated BudgetDataMapping mapping) {
        MyJsonBean<BudgetDataMapping> result = new MyJsonBean<>();
        try {
            BudgetDataMapping created = dataMappingService.create(mapping);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建数据映射失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建数据映射异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询映射详情
     */
    @Operation(summary = "查询映射详情")
    @ApiOperation("查询映射详情")
    @GetMapping("/detail/{mappingId}")
    public MyJsonBean<BudgetDataMapping> getDetail(
            @ApiParam(value = "映射ID", required = true) @PathVariable String mappingId) {
        MyJsonBean<BudgetDataMapping> result = new MyJsonBean<>();
        try {
            BudgetDataMapping mapping = dataMappingService.getById(mappingId);
            if (mapping != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(mapping);
            } else {
                result.setCode(0);
                result.setMsg("数据映射不存在");
            }
        } catch (Exception e) {
            log.error("查询映射详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新数据映射
     */
    @Operation(summary = "更新数据映射")
    @ApiOperation("更新数据映射")
    @PutMapping("/update/{mappingId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "映射ID", required = true) @PathVariable String mappingId,
            @RequestBody @Validated BudgetDataMapping mapping) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            mapping.setMappingId(mappingId);
            dataMappingService.update(mapping);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新数据映射失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新数据映射异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除数据映射
     */
    @Operation(summary = "删除数据映射")
    @ApiOperation("删除数据映射")
    @DeleteMapping("/delete/{mappingId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "映射ID", required = true) @PathVariable String mappingId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            dataMappingService.delete(mappingId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除数据映射失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除数据映射异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询映射列表
     */
    @Operation(summary = "分页查询映射列表")
    @ApiOperation("分页查询映射列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetDataMapping>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetDataMapping>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetDataMapping> pageResult = dataMappingService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询映射列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行映射
     */
    @Operation(summary = "执行映射")
    @ApiOperation("执行映射")
    @PostMapping("/execute/{mappingId}")
    public MyJsonBean<Map<String, Object>> executeMapping(
            @ApiParam(value = "映射ID", required = true) @PathVariable String mappingId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            params.put("mappingId", mappingId);
            Map<String, Object> executeResult = dataMappingService.executeMapping(params);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(executeResult);
        } catch (ServiceException ex) {
            log.error("执行映射失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行映射异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 以下为支持 /accountant/integration/data-mapping 路径的接口 ====================

    /**
     * 创建数据映射 (integration路径)
     */
    @Operation(summary = "创建数据映射")
    @ApiOperation("创建数据映射")
    @PostMapping("/accountant/integration/data-mapping")
    public MyJsonBean<BudgetDataMapping> createDataMapping(@RequestBody @Validated BudgetDataMapping mapping) {
        return create(mapping);
    }

    /**
     * 查询数据映射详情 (integration路径)
     */
    @Operation(summary = "查询数据映射详情")
    @ApiOperation("查询数据映射详情")
    @GetMapping("/accountant/integration/data-mapping/{mappingId}")
    public MyJsonBean<BudgetDataMapping> getDataMappingDetail(@PathVariable String mappingId) {
        return getDetail(mappingId);
    }

    /**
     * 更新数据映射 (integration路径)
     */
    @Operation(summary = "更新数据映射")
    @ApiOperation("更新数据映射")
    @PutMapping("/accountant/integration/data-mapping/{mappingId}")
    public MyJsonBean<Void> updateDataMapping(@PathVariable String mappingId, @RequestBody @Validated BudgetDataMapping mapping) {
        return update(mappingId, mapping);
    }

    /**
     * 删除数据映射 (integration路径)
     */
    @Operation(summary = "删除数据映射")
    @ApiOperation("删除数据映射")
    @DeleteMapping("/accountant/integration/data-mapping/{mappingId}")
    public MyJsonBean<Void> deleteDataMapping(@PathVariable String mappingId) {
        return delete(mappingId);
    }

    /**
     * 分页查询数据映射列表 (integration路径)
     */
    @Operation(summary = "分页查询数据映射列表")
    @ApiOperation("分页查询数据映射列表")
    @PostMapping("/accountant/integration/data-mapping/page")
    public MyJsonBean<PageResult<BudgetDataMapping>> getDataMappingPage(@RequestBody Map<String, Object> params) {
        return getPage(params);
    }

    /**
     * 测试数据映射 (integration路径)
     */
    @Operation(summary = "测试数据映射")
    @ApiOperation("测试数据映射")
    @PostMapping("/accountant/integration/data-mapping/{mappingId}/test")
    public MyJsonBean<Map<String, Object>> testDataMapping(@PathVariable String mappingId, @RequestBody Map<String, Object> params) {
        return executeMapping(mappingId, params);
    }
}

