package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.dataCollection.dto.DataSourceQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.service.DataSourceService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 数据归集数据源配置Controller (OpenAPI)
 *
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "数据归集数据源配置API")
@RestController("dataCollectionSourceController")
@RequestMapping("/financialSharing/dataSource/api")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    @ApiOperation(value = "分页查询数据源配置")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody DataSourceQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return dataSourceService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询数据源配置")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String orgId = UserUtils.getOrgId();
            return dataSourceService.queryById(sourceId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "保存数据源配置")
    @PostMapping("/saveDataSource")
    public MyJsonBean saveDataSource(@RequestBody TblDataSource dataSource) {
        try {
            dataSource.setOrgId(UserUtils.getOrgId());
            if (dataSource.getSourceId() == null) {
                dataSource.setCreateUser(UserUtils.getUserId());
            } else {
                dataSource.setUpdateUser(UserUtils.getUserId());
            }
            return dataSourceService.saveDataSource(dataSource);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除数据源配置")
    @PostMapping("/deleteDataSource")
    public MyJsonBean deleteDataSource(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String orgId = UserUtils.getOrgId();
            return dataSourceService.deleteDataSource(sourceId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "启用/禁用数据源")
    @PostMapping("/toggleEnabled")
    public MyJsonBean toggleEnabled(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String isEnabled = params.get("isEnabled");
            String orgId = UserUtils.getOrgId();
            return dataSourceService.toggleEnabled(sourceId, isEnabled, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试数据源连接")
    @PostMapping("/testConnection")
    public MyJsonBean testConnection(@RequestBody Map<String, String> params) {
        try {
            String sourceId = params.get("sourceId");
            String orgId = UserUtils.getOrgId();
            return dataSourceService.testConnection(sourceId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }
}

