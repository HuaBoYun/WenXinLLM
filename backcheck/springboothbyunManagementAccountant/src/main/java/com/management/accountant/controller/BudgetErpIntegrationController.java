package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.integration.BudgetErpIntegration;
import com.management.accountant.service.BudgetErpIntegrationService;
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
import java.util.List;
import java.util.Map;

/**
 * 预算ERP集成Controller
 * 
 * @description 预算ERP系统集成接口，支持SAP、Oracle、用友、金蝶等ERP系统集成
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-ERP集成"})
@RequestMapping(value = "/accountant/budget/erp")
@Slf4j
public class BudgetErpIntegrationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetErpIntegrationService erpService;

    /**
     * 创建ERP集成
     */
    @Operation(summary = "创建ERP集成")
    @ApiOperation("创建ERP集成")
    @PostMapping("/create")
    public MyJsonBean<BudgetErpIntegration> create(@RequestBody @Validated BudgetErpIntegration erp) {
        MyJsonBean<BudgetErpIntegration> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration created = erpService.create(erp);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建ERP集成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建ERP集成异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询ERP集成详情
     */
    @Operation(summary = "查询ERP集成详情")
    @ApiOperation("查询ERP集成详情")
    @GetMapping("/detail/{erpId}")
    public MyJsonBean<BudgetErpIntegration> getDetail(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<BudgetErpIntegration> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration erp = erpService.getById(erpId);
            if (erp != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(erp);
            } else {
                result.setCode(0);
                result.setMsg("ERP集成不存在");
            }
        } catch (Exception e) {
            log.error("查询ERP集成详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新ERP集成
     */
    @Operation(summary = "更新ERP集成")
    @ApiOperation("更新ERP集成")
    @PutMapping("/update/{erpId}")
    public MyJsonBean<Void> update(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId,
            @RequestBody @Validated BudgetErpIntegration erp) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            erp.setErpId(erpId);
            erpService.update(erp);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            log.error("更新ERP集成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新ERP集成异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除ERP集成
     */
    @Operation(summary = "删除ERP集成")
    @ApiOperation("删除ERP集成")
    @DeleteMapping("/delete/{erpId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            erpService.delete(erpId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除ERP集成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除ERP集成异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询ERP集成列表
     */
    @Operation(summary = "分页查询ERP集成列表")
    @ApiOperation("分页查询ERP集成列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetErpIntegration>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetErpIntegration>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetErpIntegration> pageResult = erpService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询ERP集成列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试连接
     */
    @Operation(summary = "测试连接")
    @ApiOperation("测试连接")
    @PostMapping("/test/{erpId}")
    public MyJsonBean<Map<String, Object>> testConnection(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = erpService.testConnection(erpId);
            result.setCode(1);
            result.setMsg("测试完成");
            result.setData(testResult);
        } catch (ServiceException ex) {
            log.error("测试连接失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("测试连接异常", e);
            result.setCode(0);
            result.setMsg("测试失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行同步
     */
    @Operation(summary = "执行同步")
    @ApiOperation("执行同步")
    @PostMapping("/sync/{erpId}")
    public MyJsonBean<Map<String, Object>> executeSync(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> syncResult = erpService.executeSync(erpId);
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (ServiceException ex) {
            log.error("执行同步失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行同步异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用ERP集成
     */
    @Operation(summary = "启用ERP集成")
    @ApiOperation("启用ERP集成")
    @PostMapping("/enable/{erpId}")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            erpService.enable(erpId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用ERP集成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用ERP集成异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 停用ERP集成
     */
    @Operation(summary = "停用ERP集成")
    @ApiOperation("停用ERP集成")
    @PostMapping("/disable/{erpId}")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            erpService.disable(erpId);
            result.setCode(1);
            result.setMsg("停用成功");
        } catch (ServiceException ex) {
            log.error("停用ERP集成失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("停用ERP集成异常", e);
            result.setCode(0);
            result.setMsg("停用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取同步历史
     */
    @Operation(summary = "获取同步历史")
    @ApiOperation("获取同步历史")
    @GetMapping("/history/{erpId}")
    public MyJsonBean<List<Map<String, Object>>> getSyncHistory(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> history = erpService.getSyncHistory(erpId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(history);
        } catch (Exception e) {
            log.error("获取同步历史异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询所有ERP集成列表
     */
    @Operation(summary = "查询所有ERP集成列表")
    @ApiOperation("查询所有ERP集成列表")
    @GetMapping("/list")
    public MyJsonBean<List<BudgetErpIntegration>> list(
            @ApiParam(value = "ERP类型", required = false) @RequestParam(required = false) String erpType) {
        MyJsonBean<List<BudgetErpIntegration>> result = new MyJsonBean<>();
        try {
            List<BudgetErpIntegration> list = erpService.getList(erpType);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(list);
        } catch (Exception e) {
            log.error("查询ERP集成列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取ERP集成统计数据
     */
    @Operation(summary = "获取ERP集成统计数据")
    @ApiOperation("获取ERP集成统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = erpService.getStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取ERP集成统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取字段映射配置
     */
    @Operation(summary = "获取字段映射配置")
    @ApiOperation("获取字段映射配置")
    @GetMapping("/field-mapping/{erpId}")
    public MyJsonBean<String> getFieldMappings(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            BudgetErpIntegration erp = erpService.getById(erpId);
            if (erp != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(erp.getDataMapping());
            } else {
                result.setCode(0);
                result.setMsg("ERP集成不存在");
            }
        } catch (Exception e) {
            log.error("获取字段映射配置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存字段映射配置
     */
    @Operation(summary = "保存字段映射配置")
    @ApiOperation("保存字段映射配置")
    @PostMapping("/field-mapping/{erpId}")
    public MyJsonBean<Void> saveFieldMappings(
            @ApiParam(value = "ERP集成ID", required = true) @PathVariable String erpId,
            @RequestBody String mappings) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            erpService.saveFieldMappings(erpId, mappings);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (ServiceException ex) {
            log.error("保存字段映射失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("保存字段映射异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }
}

