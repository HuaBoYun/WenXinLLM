package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.service.DataSourceTestService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据源连接测试Controller (OpenAPI)
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "数据源连接测试API")
@RestController
@RequestMapping("/financialSharing/dataSourceTest/api")
public class DataSourceTestController {

    @Autowired
    private DataSourceTestService dataSourceTestService;

    @ApiOperation(value = "测试数据库连接（增强版）")
    @PostMapping("/testDatabaseConnection")
    public MyJsonBean testDatabaseConnection(@RequestBody TblDataSource dataSource) {
        try {
            return dataSourceTestService.testDatabaseConnectionEnhanced(dataSource);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试API连接（增强版）")
    @PostMapping("/testApiConnection")
    public MyJsonBean testApiConnection(@RequestBody TblDataSource dataSource) {
        try {
            return dataSourceTestService.testApiConnectionEnhanced(dataSource);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试文件连接（增强版）")
    @PostMapping("/testFileConnection")
    public MyJsonBean testFileConnection(@RequestBody TblDataSource dataSource) {
        try {
            return dataSourceTestService.testFileConnectionEnhanced(dataSource);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试财务共享连接（增强版）")
    @PostMapping("/testFinancialSharingConnection")
    public MyJsonBean testFinancialSharingConnection(@RequestBody TblDataSource dataSource) {
        try {
            return dataSourceTestService.testFinancialSharingConnectionEnhanced(dataSource);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "批量测试数据源连接")
    @PostMapping("/batchTestConnection")
    public MyJsonBean batchTestConnection(@RequestBody Map<String, Object> params) {
        try {
            String[] sourceIds = (String[]) params.get("sourceIds");
            String orgId = UserUtils.getOrgId();
            return dataSourceTestService.batchTestConnection(sourceIds, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取数据库表列表")
    @PostMapping("/getDatabaseTables")
    public MyJsonBean getDatabaseTables(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String orgId = UserUtils.getOrgId();
            return dataSourceTestService.getDatabaseTables(sourceId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取表列表失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取数据库表结构")
    @PostMapping("/getTableStructure")
    public MyJsonBean getTableStructure(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String tableName = params.get("tableName");
            String orgId = UserUtils.getOrgId();
            return dataSourceTestService.getTableStructure(sourceId, tableName, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取表结构失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "执行测试查询")
    @PostMapping("/executeTestQuery")
    public MyJsonBean executeTestQuery(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String sql = params.get("sql");
            String orgId = UserUtils.getOrgId();
            return dataSourceTestService.executeTestQuery(sourceId, sql, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("执行查询失败：" + e.getMessage());
        }
    }
}

