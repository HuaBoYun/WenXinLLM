package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.SystemParameter;
import com.huabo.cybermonitor.service.ISystemParameterService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.SystemParameterQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 系统参数配置管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="系统参数配置管理",description="系统参数配置管理")
@RestController
@RequestMapping("/v1/system/parameter")
public class SystemParameterController {

	private static final Logger log = LoggerFactory.getLogger(SystemParameterController.class);

    @Autowired
    private ISystemParameterService parameterService;

    @Operation(summary = "分页查询系统参数列表")
    @PostMapping("/list")
    public R<PageResult<SystemParameter>> getParameterList(@RequestBody SystemParameterQueryVO queryVO) {
        try {
            IPage<SystemParameter> page = parameterService.getParameterList(queryVO);
            PageResult<SystemParameter> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询系统参数列表失败", e);
            return R.fail("查询系统参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统参数详情")
    @PostMapping("/detail")
    public R<SystemParameter> getParameterDetail(@RequestBody Map<String, String> params) {
        try {
            String parameterId = params.get("parameterId");
            if (parameterId == null || parameterId.trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            SystemParameter parameter = parameterService.getParameterDetail(parameterId);
            return R.success(parameter);
        } catch (Exception e) {
            log.error("获取系统参数详情失败", e);
            return R.fail("获取系统参数详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增系统参数")
    @PostMapping("/add")
    public R<String> addParameter(@RequestBody SystemParameter parameter) {
        try {
            boolean success = parameterService.addParameter(parameter);
            if (success) {
                return R.success("新增系统参数成功");
            } else {
                return R.fail("新增系统参数失败");
            }
        } catch (Exception e) {
            log.error("新增系统参数失败", e);
            return R.fail("新增系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新系统参数")
    @PostMapping("/update")
    public R<String> updateParameter(@RequestBody SystemParameter parameter) {
        try {
            if (parameter.getParameterId() == null || parameter.getParameterId().trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            boolean success = parameterService.updateParameter(parameter);
            if (success) {
                return R.success("更新系统参数成功");
            } else {
                return R.fail("更新系统参数失败");
            }
        } catch (Exception e) {
            log.error("更新系统参数失败", e);
            return R.fail("更新系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除系统参数")
    @PostMapping("/delete")
    public R<String> deleteParameter(@RequestBody Map<String, String> params) {
        try {
            String parameterId = params.get("parameterId");
            if (parameterId == null || parameterId.trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            boolean success = parameterService.deleteParameter(parameterId);
            if (success) {
                return R.success("删除系统参数成功");
            } else {
                return R.fail("删除系统参数失败");
            }
        } catch (Exception e) {
            log.error("删除系统参数失败", e);
            return R.fail("删除系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除系统参数")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteParameter(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> parameterIds = params.get("parameterIds");
            if (parameterIds == null || parameterIds.isEmpty()) {
                return R.fail("参数ID列表不能为空");
            }
            
            boolean success = parameterService.batchDeleteParameter(parameterIds);
            if (success) {
                return R.success("批量删除系统参数成功");
            } else {
                return R.fail("批量删除系统参数失败");
            }
        } catch (Exception e) {
            log.error("批量删除系统参数失败", e);
            return R.fail("批量删除系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "启用系统参数")
    @PostMapping("/enable")
    public R<String> enableParameter(@RequestBody Map<String, String> params) {
        try {
            String parameterId = params.get("parameterId");
            if (parameterId == null || parameterId.trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            boolean success = parameterService.enableParameter(parameterId);
            if (success) {
                return R.success("启用系统参数成功");
            } else {
                return R.fail("启用系统参数失败");
            }
        } catch (Exception e) {
            log.error("启用系统参数失败", e);
            return R.fail("启用系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "禁用系统参数")
    @PostMapping("/disable")
    public R<String> disableParameter(@RequestBody Map<String, String> params) {
        try {
            String parameterId = params.get("parameterId");
            if (parameterId == null || parameterId.trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            boolean success = parameterService.disableParameter(parameterId);
            if (success) {
                return R.success("禁用系统参数成功");
            } else {
                return R.fail("禁用系统参数失败");
            }
        } catch (Exception e) {
            log.error("禁用系统参数失败", e);
            return R.fail("禁用系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量启用系统参数")
    @PostMapping("/batch-enable")
    public R<String> batchEnableParameter(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> parameterIds = params.get("parameterIds");
            if (parameterIds == null || parameterIds.isEmpty()) {
                return R.fail("参数ID列表不能为空");
            }
            
            boolean success = parameterService.batchEnableParameter(parameterIds);
            if (success) {
                return R.success("批量启用系统参数成功");
            } else {
                return R.fail("批量启用系统参数失败");
            }
        } catch (Exception e) {
            log.error("批量启用系统参数失败", e);
            return R.fail("批量启用系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量禁用系统参数")
    @PostMapping("/batch-disable")
    public R<String> batchDisableParameter(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> parameterIds = params.get("parameterIds");
            if (parameterIds == null || parameterIds.isEmpty()) {
                return R.fail("参数ID列表不能为空");
            }
            
            boolean success = parameterService.batchDisableParameter(parameterIds);
            if (success) {
                return R.success("批量禁用系统参数成功");
            } else {
                return R.fail("批量禁用系统参数失败");
            }
        } catch (Exception e) {
            log.error("批量禁用系统参数失败", e);
            return R.fail("批量禁用系统参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据参数分组查询参数列表")
    @PostMapping("/list-by-group")
    public R<List<SystemParameter>> getParametersByGroup(@RequestBody Map<String, String> params) {
        try {
            String parameterGroup = params.get("parameterGroup");
            if (parameterGroup == null || parameterGroup.trim().isEmpty()) {
                return R.fail("参数分组不能为空");
            }
            
            List<SystemParameter> parameters = parameterService.getParametersByGroup(parameterGroup);
            return R.success(parameters);
        } catch (Exception e) {
            log.error("根据参数分组查询参数列表失败", e);
            return R.fail("根据参数分组查询参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据参数类型查询参数列表")
    @PostMapping("/list-by-type")
    public R<List<SystemParameter>> getParametersByType(@RequestBody Map<String, String> params) {
        try {
            String parameterType = params.get("parameterType");
            if (parameterType == null || parameterType.trim().isEmpty()) {
                return R.fail("参数类型不能为空");
            }
            
            List<SystemParameter> parameters = parameterService.getParametersByType(parameterType);
            return R.success(parameters);
        } catch (Exception e) {
            log.error("根据参数类型查询参数列表失败", e);
            return R.fail("根据参数类型查询参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询启用的参数列表")
    @PostMapping("/enabled")
    public R<List<SystemParameter>> getEnabledParameters() {
        try {
            List<SystemParameter> parameters = parameterService.getEnabledParameters();
            return R.success(parameters);
        } catch (Exception e) {
            log.error("查询启用的参数列表失败", e);
            return R.fail("查询启用的参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询系统参数列表")
    @PostMapping("/system")
    public R<List<SystemParameter>> getSystemParameters() {
        try {
            List<SystemParameter> parameters = parameterService.getSystemParameters();
            return R.success(parameters);
        } catch (Exception e) {
            log.error("查询系统参数列表失败", e);
            return R.fail("查询系统参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证参数键名唯一性")
    @PostMapping("/validate-key")
    public R<Map<String, Object>> validateParameterKey(@RequestBody Map<String, String> params) {
        try {
            String parameterKey = params.get("parameterKey");
            String excludeId = params.get("excludeId");
            
            if (parameterKey == null || parameterKey.trim().isEmpty()) {
                return R.fail("参数键名不能为空");
            }
            
            boolean isDuplicate = parameterService.validateParameterKey(parameterKey, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "参数键名已存在" : "参数键名可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证参数键名唯一性失败", e);
            return R.fail("验证参数键名唯一性失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据参数键名获取参数值")
    @PostMapping("/get-value")
    public R<String> getParameterValue(@RequestBody Map<String, String> params) {
        try {
            String parameterKey = params.get("parameterKey");
            String defaultValue = params.get("defaultValue");
            
            if (parameterKey == null || parameterKey.trim().isEmpty()) {
                return R.fail("参数键名不能为空");
            }
            
            String value = parameterService.getParameterValue(parameterKey, defaultValue);
            return R.success(value);
        } catch (Exception e) {
            log.error("获取参数值失败", e);
            return R.fail("获取参数值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据参数键名更新参数值")
    @PostMapping("/update-value")
    public R<String> updateParameterValue(@RequestBody Map<String, String> params) {
        try {
            String parameterKey = params.get("parameterKey");
            String parameterValue = params.get("parameterValue");
            
            if (parameterKey == null || parameterKey.trim().isEmpty()) {
                return R.fail("参数键名不能为空");
            }
            
            boolean success = parameterService.updateParameterValue(parameterKey, parameterValue);
            if (success) {
                return R.success("更新参数值成功");
            } else {
                return R.fail("更新参数值失败");
            }
        } catch (Exception e) {
            log.error("更新参数值失败", e);
            return R.fail("更新参数值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新参数值")
    @PostMapping("/batch-update-values")
    public R<String> batchUpdateParameterValues(@RequestBody Map<String, String> parameters) {
        try {
            if (parameters == null || parameters.isEmpty()) {
                return R.fail("参数列表不能为空");
            }
            
            boolean success = parameterService.batchUpdateParameterValues(parameters);
            if (success) {
                return R.success("批量更新参数值成功");
            } else {
                return R.fail("批量更新参数值失败");
            }
        } catch (Exception e) {
            log.error("批量更新参数值失败", e);
            return R.fail("批量更新参数值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "重置参数为默认值")
    @PostMapping("/reset-default")
    public R<String> resetParameterToDefault(@RequestBody Map<String, String> params) {
        try {
            String parameterId = params.get("parameterId");
            if (parameterId == null || parameterId.trim().isEmpty()) {
                return R.fail("参数ID不能为空");
            }
            
            boolean success = parameterService.resetParameterToDefault(parameterId);
            if (success) {
                return R.success("重置参数为默认值成功");
            } else {
                return R.fail("重置参数为默认值失败");
            }
        } catch (Exception e) {
            log.error("重置参数为默认值失败", e);
            return R.fail("重置参数为默认值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量重置参数为默认值")
    @PostMapping("/batch-reset-default")
    public R<String> batchResetParametersToDefault(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> parameterIds = params.get("parameterIds");
            if (parameterIds == null || parameterIds.isEmpty()) {
                return R.fail("参数ID列表不能为空");
            }
            
            boolean success = parameterService.batchResetParametersToDefault(parameterIds);
            if (success) {
                return R.success("批量重置参数为默认值成功");
            } else {
                return R.fail("批量重置参数为默认值失败");
            }
        } catch (Exception e) {
            log.error("批量重置参数为默认值失败", e);
            return R.fail("批量重置参数为默认值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证参数值")
    @PostMapping("/validate-value")
    public R<Map<String, Object>> validateParameterValue(@RequestBody SystemParameter parameter) {
        try {
            Map<String, Object> result = parameterService.validateParameterValue(parameter);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证参数值失败", e);
            return R.fail("验证参数值失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取参数统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getParameterStatistics() {
        try {
            Map<String, Object> statistics = parameterService.getParameterStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取参数统计信息失败", e);
            return R.fail("获取参数统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取参数分组分布统计")
    @PostMapping("/statistics/group-distribution")
    public R<List<Map<String, Object>>> getParameterGroupDistribution() {
        try {
            List<Map<String, Object>> distribution = parameterService.getParameterGroupDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取参数分组分布统计失败", e);
            return R.fail("获取参数分组分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取参数类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getParameterTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = parameterService.getParameterTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取参数类型分布统计失败", e);
            return R.fail("获取参数类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "刷新系统参数缓存")
    @PostMapping("/refresh-cache")
    public R<String> refreshParameterCache() {
        try {
            boolean success = parameterService.refreshParameterCache();
            if (success) {
                return R.success("刷新系统参数缓存成功");
            } else {
                return R.fail("刷新系统参数缓存失败");
            }
        } catch (Exception e) {
            log.error("刷新系统参数缓存失败", e);
            return R.fail("刷新系统参数缓存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出参数列表")
    @PostMapping("/export")
    public void exportParameterList(@RequestBody SystemParameterQueryVO queryVO, HttpServletResponse response) {
        try {
            parameterService.exportParameterList(queryVO, response);
        } catch (Exception e) {
            log.error("导出参数列表失败", e);
            throw new RuntimeException("导出参数列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下载参数导入模板")
    @GetMapping("/template")
    public void downloadParameterTemplate(HttpServletResponse response) {
        try {
            parameterService.downloadParameterTemplate(response);
        } catch (Exception e) {
            log.error("下载参数导入模板失败", e);
            throw new RuntimeException("下载参数导入模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入参数")
    @PostMapping("/import")
    public R<Map<String, Object>> importParameterList(
            @Parameter(description = "导入文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("导入文件不能为空");
            }
            
            Map<String, Object> result = parameterService.importParameterList(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入参数失败", e);
            return R.fail("批量导入参数失败：" + e.getMessage());
        }
    }

    @Operation(summary = "备份系统参数配置")
    @GetMapping("/backup")
    public void backupParameterConfig(HttpServletResponse response) {
        try {
            parameterService.backupParameterConfig(response);
        } catch (Exception e) {
            log.error("备份系统参数配置失败", e);
            throw new RuntimeException("备份系统参数配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "恢复系统参数配置")
    @PostMapping("/restore")
    public R<Map<String, Object>> restoreParameterConfig(
            @Parameter(description = "备份文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("备份文件不能为空");
            }
            
            Map<String, Object> result = parameterService.restoreParameterConfig(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("恢复系统参数配置失败", e);
            return R.fail("恢复系统参数配置失败：" + e.getMessage());
        }
    }
}
