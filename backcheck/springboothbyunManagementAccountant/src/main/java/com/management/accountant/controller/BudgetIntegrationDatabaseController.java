package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetIntegrationDatabaseService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算数据库集成Controller
 * 
 * @description 预算数据库集成接口，支持多数据源、数据同步、数据迁移等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-数据库集成"})
@RequestMapping(value = "/accountant/budget/integration/database")
@Slf4j
public class BudgetIntegrationDatabaseController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetIntegrationDatabaseService integrationDatabaseService;

    /**
     * 配置数据源
     */
    @Operation(summary = "配置数据源")
    @ApiOperation("配置数据源")
    @PostMapping("/datasource/config")
    public MyJsonBean<Map<String, Object>> configDatasource(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> datasource = integrationDatabaseService.configDatasource(params);
            result.setCode(1);
            result.setMsg("配置成功");
            result.setData(datasource);
        } catch (ServiceException ex) {
            log.error("配置数据源失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("配置数据源异常", e);
            result.setCode(0);
            result.setMsg("配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 测试连接
     */
    @Operation(summary = "测试连接")
    @ApiOperation("测试连接")
    @PostMapping("/connection/test")
    public MyJsonBean<Map<String, Object>> testConnection(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> testResult = integrationDatabaseService.testConnection(params);
            result.setCode(1);
            result.setMsg("测试成功");
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
     * 数据同步
     */
    @Operation(summary = "数据同步")
    @ApiOperation("数据同步")
    @PostMapping("/sync")
    public MyJsonBean<Map<String, Object>> syncData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> syncResult = integrationDatabaseService.syncData(params);
            result.setCode(1);
            result.setMsg("同步成功");
            result.setData(syncResult);
        } catch (ServiceException ex) {
            log.error("数据同步失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据同步异常", e);
            result.setCode(0);
            result.setMsg("同步失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据迁移
     */
    @Operation(summary = "数据迁移")
    @ApiOperation("数据迁移")
    @PostMapping("/migrate")
    public MyJsonBean<Map<String, Object>> migrateData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> migrateResult = integrationDatabaseService.migrateData(params);
            result.setCode(1);
            result.setMsg("迁移成功");
            result.setData(migrateResult);
        } catch (ServiceException ex) {
            log.error("数据迁移失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据迁移异常", e);
            result.setCode(0);
            result.setMsg("迁移失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行查询
     */
    @Operation(summary = "执行查询")
    @ApiOperation("执行查询")
    @PostMapping("/query/execute")
    public MyJsonBean<Map<String, Object>> executeQuery(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> queryResult = integrationDatabaseService.executeQuery(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(queryResult);
        } catch (ServiceException ex) {
            log.error("执行查询失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行查询异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据备份
     */
    @Operation(summary = "数据备份")
    @ApiOperation("数据备份")
    @PostMapping("/backup")
    public MyJsonBean<Map<String, Object>> backupData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> backupResult = integrationDatabaseService.backupData(params);
            result.setCode(1);
            result.setMsg("备份成功");
            result.setData(backupResult);
        } catch (ServiceException ex) {
            log.error("数据备份失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据备份异常", e);
            result.setCode(0);
            result.setMsg("备份失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 数据恢复
     */
    @Operation(summary = "数据恢复")
    @ApiOperation("数据恢复")
    @PostMapping("/restore")
    public MyJsonBean<Map<String, Object>> restoreData(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> restoreResult = integrationDatabaseService.restoreData(params);
            result.setCode(1);
            result.setMsg("恢复成功");
            result.setData(restoreResult);
        } catch (ServiceException ex) {
            log.error("数据恢复失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("数据恢复异常", e);
            result.setCode(0);
            result.setMsg("恢复失败：" + e.getMessage());
        }
        return result;
    }
}

