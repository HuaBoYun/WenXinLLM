package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.MappingConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblMappingConfig;
import com.financial.sharing.budgetControl.service.MappingConfigService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 映射配置Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预算执行控制-映射配置管理")
@RestController
@RequestMapping("/financialSharing/budgetControl/mappingConfig")
public class MappingConfigController {

    @Autowired
    private MappingConfigService mappingConfigService;

    @ApiOperation(value = "分页查询映射配置")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody MappingConfigQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return mappingConfigService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询映射配置")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestParam String mappingId) {
        try {
            return mappingConfigService.queryById(mappingId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "新增映射配置")
    @PostMapping("/add")
    public MyJsonBean add(@RequestBody TblMappingConfig config) {
        try {
            config.setOrgId(UserUtils.getOrgId());
            return mappingConfigService.add(config);
        } catch (Exception e) {
            return MyJsonBean.errorData("新增失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "修改映射配置")
    @PostMapping("/modify")
    public MyJsonBean modify(@RequestBody TblMappingConfig config) {
        try {
            return mappingConfigService.modify(config);
        } catch (Exception e) {
            return MyJsonBean.errorData("修改失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除映射配置")
    @PostMapping("/remove")
    public MyJsonBean remove(@RequestParam String mappingId) {
        try {
            return mappingConfigService.remove(mappingId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据来源系统查询映射配置")
    @PostMapping("/queryBySourceSystem")
    public MyJsonBean queryBySourceSystem(@RequestParam String sourceSystem) {
        try {
            String orgId = UserUtils.getOrgId();
            return mappingConfigService.queryBySourceSystem(sourceSystem, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}

