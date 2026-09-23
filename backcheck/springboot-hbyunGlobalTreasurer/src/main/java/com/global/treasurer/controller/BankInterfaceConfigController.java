package com.global.treasurer.controller;

import com.global.treasurer.entity.TblBankInterfaceConfig;
import com.global.treasurer.service.BankInterfaceConfigService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settlement/bank-interface-config")
@Api(tags = "银企联配置管理")
public class BankInterfaceConfigController {
    @Resource
    private BankInterfaceConfigService bankConfigService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询银企联配置")
    public String getBankConfigPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = bankConfigService.getBankConfigPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询银企联配置")
    public String getBankConfigById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblBankInterfaceConfig config = bankConfigService.getBankConfigById(id);
            if (config == null) {
                return new JsonBean(0, "配置不存在", null).toJson();
            }
            return JsonBean.success(config);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增银企联配置")
    public String addBankConfig(@FlexibleRequestBody TblBankInterfaceConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = bankConfigService.createBankConfig(config);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("修改银企联配置")
    public String updateBankConfig(@FlexibleRequestBody TblBankInterfaceConfig config) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = bankConfigService.updateBankConfig(config);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除银企联配置")
    public String deleteBankConfig(@PathVariable String ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            String[] idArray = ids.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) {
                idList.add(Long.parseLong(id));
            }
            int result = bankConfigService.deleteBankConfig(idList);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/enable")
    @ApiOperation("启用配置")
    public String enableBankConfig(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Long> configIds = parseIdList(params.get("configIds"));
            if (configIds == null || configIds.isEmpty()) {
                return new JsonBean(0, "请选择要启用的配置", null).toJson();
            }
            int result = bankConfigService.enableBankConfig(configIds);
            if (result > 0) {
                return JsonBean.success("启用成功");
            } else {
                return new JsonBean(0, "启用失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "启用失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/disable")
    @ApiOperation("禁用配置")
    public String disableBankConfig(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<Long> configIds = parseIdList(params.get("configIds"));
            if (configIds == null || configIds.isEmpty()) {
                return new JsonBean(0, "请选择要禁用的配置", null).toJson();
            }
            int result = bankConfigService.disableBankConfig(configIds);
            if (result > 0) {
                return JsonBean.success("禁用成功");
            } else {
                return new JsonBean(0, "禁用失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "禁用失败: " + e.getMessage(), null).toJson();
        }
    }

    /** JSON 反序列化后数字可能是 Integer，统一转为 Long */
    @SuppressWarnings("unchecked")
    private List<Long> parseIdList(Object raw) {
        if (raw == null) return null;
        List<?> list = (List<?>) raw;
        List<Long> result = new java.util.ArrayList<>();
        for (Object item : list) {
            if (item != null) result.add(Long.parseLong(item.toString()));
        }
        return result;
    }

    @PostMapping("/{id}/test-connection")
    @ApiOperation("测试连接")
    public String testConnection(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = bankConfigService.testConnection(id);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{id}/health-check")
    @ApiOperation("健康检查")
    public String healthCheck(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = bankConfigService.healthCheck(id);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "健康检查失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/available")
    @ApiOperation("查询可用配置")
    public String getAvailableConfigs(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblBankInterfaceConfig> list = bankConfigService.getAvailableConfigs(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/summary")
    @ApiOperation("统计配置概要")
    public String getBankConfigSummary(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            // 如果没有传递 orgId，则不限制组织ID，返回所有可见数据的统计结果
            Map<String, Object> summary = bankConfigService.getBankConfigSummary(orgId);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-test")
    @ApiOperation("批量测试连接")
    public String batchTestConnection(@FlexibleRequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return new JsonBean(0, "请选择要测试的配置", null).toJson();
            }
            Map<String, Object> result = bankConfigService.batchTestConnection(ids);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "批量测试失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出配置")
    public void exportConfigs(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return;
            }
            List<TblBankInterfaceConfig> configs = bankConfigService.exportConfigs(params);

            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("银行接口配置_" + System.currentTimeMillis() + ".xlsx", StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName);

            // 这里应该使用Excel工具类生成Excel文件
            // 由于环境限制，这里仅做简单响应
            response.getWriter().write("导出功能需要集成Excel工具类");
            response.getWriter().flush();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
