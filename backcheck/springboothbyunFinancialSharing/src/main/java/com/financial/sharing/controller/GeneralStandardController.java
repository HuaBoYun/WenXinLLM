package com.financial.sharing.controller;

import com.financial.sharing.service.TblGeneralStandardService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用标准管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "通用标准管理")
@RestController
@RequestMapping("/general-standards")
@CrossOrigin
public class GeneralStandardController {

    @Autowired
    private TblGeneralStandardService generalStandardService;

    @ApiOperation("查询通用标准列表")
    @GetMapping
    public MyJsonBean getGeneralStandardList(@RequestParam(required = false) String orgId,
                                            @RequestParam(required = false) String standardName,
                                            @RequestParam(required = false) String standardType,
                                            @RequestParam(required = false) Integer isEnabled,
                                            @RequestParam(defaultValue = "1") Integer pageNo,
                                            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("orgId", orgId);
            param.put("standardName", standardName);
            param.put("standardType", standardType);
            param.put("isEnabled", isEnabled);
            param.put("pageNo", pageNo);
            param.put("pageSize", pageSize);

            return generalStandardService.getList(param);
        } catch (Exception e) {
            log.error("查询通用标准列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存通用标准")
    @PostMapping
    public MyJsonBean saveGeneralStandard(@RequestBody Map<String, Object> param) {
        try {
            return generalStandardService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存通用标准失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除通用标准")
    @DeleteMapping("/{standardId}")
    public MyJsonBean deleteGeneralStandard(@PathVariable String standardId) {
        try {
            return generalStandardService.delete(standardId);
        } catch (Exception e) {
            log.error("删除通用标准失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取通用标准详情")
    @GetMapping("/{standardId}")
    public MyJsonBean getGeneralStandardDetail(@PathVariable String standardId) {
        try {
            return generalStandardService.getById(standardId);
        } catch (Exception e) {
            log.error("获取通用标准详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取通用标准分级配置")
    @GetMapping("/{standardId}/levels")
    public MyJsonBean getGeneralStandardLevels(@PathVariable String standardId) {
        try {
            return generalStandardService.getLevelConfigs(standardId);
        } catch (Exception e) {
            log.error("获取通用标准分级配置失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存通用标准分级配置")
    @PostMapping("/{standardId}/levels")
    public MyJsonBean saveGeneralStandardLevels(@PathVariable String standardId,
                                               @RequestBody Object levels) {
        try {
            return generalStandardService.saveLevelConfigs(standardId, levels);
        } catch (Exception e) {
            log.error("保存通用标准分级配置失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取通用标准适用条件")
    @GetMapping("/{standardId}/conditions")
    public MyJsonBean getGeneralStandardConditions(@PathVariable String standardId) {
        try {
            return generalStandardService.getConditions(standardId);
        } catch (Exception e) {
            log.error("获取通用标准适用条件失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存通用标准适用条件")
    @PostMapping("/{standardId}/conditions")
    public MyJsonBean saveGeneralStandardConditions(@PathVariable String standardId,
                                                   @RequestBody Object conditions) {
        try {
            return generalStandardService.saveConditions(standardId, conditions);
        } catch (Exception e) {
            log.error("保存通用标准适用条件失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用通用标准")
    @PutMapping("/{standardId}/status")
    public MyJsonBean updateGeneralStandardStatus(@PathVariable String standardId,
                                                 @RequestParam Integer isEnabled) {
        try {
            return generalStandardService.updateStatus(standardId, isEnabled);
        } catch (Exception e) {
            log.error("更新通用标准状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制通用标准")
    @PostMapping("/{standardId}/copy")
    public MyJsonBean copyGeneralStandard(@PathVariable String standardId) {
        try {
            return generalStandardService.copyStandard(standardId);
        } catch (Exception e) {
            log.error("复制通用标准失败", e);
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    @ApiOperation("标准计算")
    @PostMapping("/{standardId}/calculate")
    public MyJsonBean calculateStandard(@PathVariable String standardId,
                                      @RequestBody Map<String, Object> calculateData) {
        try {
            return generalStandardService.calculate(standardId, calculateData);
        } catch (Exception e) {
            log.error("标准计算失败", e);
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取通用标准使用统计")
    @GetMapping("/{standardId}/usage-statistics")
    public MyJsonBean getGeneralStandardUsageStatistics(@PathVariable String standardId) {
        try {
            return generalStandardService.getStatistics(standardId);
        } catch (Exception e) {
            log.error("获取通用标准使用统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
