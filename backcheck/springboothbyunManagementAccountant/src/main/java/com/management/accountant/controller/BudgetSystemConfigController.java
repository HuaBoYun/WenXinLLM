 package com.management.accountant.controller;
 
 import com.management.accountant.exception.ServiceException;
 import com.management.accountant.oracle.entity.budget.BudgetSystemConfigEntity;
 import com.management.accountant.service.BudgetSystemConfigService;
 import com.management.accountant.util.MyJsonBean;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import io.swagger.annotations.ApiParam;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.web.bind.annotation.*;
 
 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletResponse;
 import java.util.*;
 import java.util.stream.Collectors;
 
 @RestController
 @Api(tags = {"NCV65全面预算-系统配置"})
 @RequestMapping(value = "/accountant/budget/system-config")
 @Slf4j
 public class BudgetSystemConfigController {
     private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
 
     @Resource
     private BudgetSystemConfigService configService;
 
     @Operation(summary = "获取所有系统配置")
     @ApiOperation("获取所有系统配置")
     @GetMapping("/list")
     public MyJsonBean<Map<String, Object>> getAllConfigurations() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             List<BudgetSystemConfigEntity> allConfigs = configService.getAll();
             Map<String, List<BudgetSystemConfigEntity>> grouped = allConfigs.stream()
                     .collect(Collectors.groupingBy(BudgetSystemConfigEntity::getConfigType));
             Map<String, Object> data = new LinkedHashMap<>();
             for (Map.Entry<String, List<BudgetSystemConfigEntity>> entry : grouped.entrySet()) {
                 List<Map<String, Object>> items = new ArrayList<>();
                 for (BudgetSystemConfigEntity c : entry.getValue()) {
                     Map<String, Object> m = new HashMap<>();
                     m.put("configId", c.getConfigId());
                     m.put("configCode", c.getConfigCode());
                     m.put("configName", c.getConfigName());
                     m.put("configKey", c.getConfigKey());
                     m.put("configValue", c.getConfigValue());
                     m.put("valueType", c.getValueType());
                     m.put("defaultValue", c.getDefaultValue());
                     m.put("isEnabled", c.getIsEnabled());
                     m.put("configDesc", c.getConfigDesc());
                     items.add(m);
                 }
                 data.put(entry.getKey(), items);
             }
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(data);
         } catch (Exception e) {
             log.error("获取系统配置异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取配置统计数据")
     @ApiOperation("获取配置统计数据")
     @GetMapping("/stats")
     public MyJsonBean<Map<String, Object>> getConfigStats() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             List<BudgetSystemConfigEntity> allConfigs = configService.getAll();
             Map<String, Object> stats = new HashMap<>();
             stats.put("totalConfigs", allConfigs.size());
             long enabledCount = allConfigs.stream().filter(c -> Integer.valueOf(1).equals(c.getIsEnabled())).count();
             stats.put("enabledConfigs", enabledCount);
             stats.put("disabledConfigs", allConfigs.size() - enabledCount);
             Map<String, Long> typeCount = allConfigs.stream()
                     .collect(Collectors.groupingBy(BudgetSystemConfigEntity::getConfigType, Collectors.counting()));
             stats.put("typeDistribution", typeCount);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(stats);
         } catch (Exception e) {
             log.error("获取配置统计异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "保存系统配置")
     @ApiOperation("保存系统配置")
     @PostMapping("/save")
     public MyJsonBean<Void> saveConfigurations(@RequestBody List<BudgetSystemConfigEntity> configs) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             for (BudgetSystemConfigEntity config : configs) {
                 if (config.getConfigId() != null && config.getConfigId().length() > 0) {
                     configService.update(config);
                 } else {
                     configService.create(config);
                 }
             }
             result.setCode(1);
             result.setMsg("保存成功");
         } catch (ServiceException ex) {
             result.setCode(0);
             result.setMsg(ex.getMessage());
         } catch (Exception e) {
             log.error("保存系统配置异常", e);
             result.setCode(0);
             result.setMsg("保存失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "保存分类配置")
     @ApiOperation("保存分类配置")
     @PostMapping("/save/{category}")
     public MyJsonBean<Void> saveCategoryConfig(
             @ApiParam(value = "配置分类", required = true) @PathVariable String category,
             @RequestBody List<BudgetSystemConfigEntity> configs) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             for (BudgetSystemConfigEntity config : configs) {
                 config.setConfigType(category);
                 if (config.getConfigId() != null && config.getConfigId().length() > 0) {
                     configService.update(config);
                 } else {
                     configService.create(config);
                 }
             }
             result.setCode(1);
             result.setMsg("保存成功");
         } catch (ServiceException ex) {
             result.setCode(0);
             result.setMsg(ex.getMessage());
         } catch (Exception e) {
             log.error("保存分类配置异常", e);
             result.setCode(0);
             result.setMsg("保存失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "导出系统配置")
     @ApiOperation("导出系统配置")
     @GetMapping("/export")
     public void exportConfigurations(HttpServletResponse response) {
         try {
             List<BudgetSystemConfigEntity> list = configService.getAll();
             response.setContentType("application/vnd.ms-excel");
             response.setHeader("Content-Disposition", "attachment;filename=system_config.csv");
             StringBuilder sb = new StringBuilder();
             sb.append("配置ID,配置编码,配置名称,配置类型,配置键,配置值,值类型,默认值,是否启用\n");
             for (BudgetSystemConfigEntity item : list) {
                 sb.append(item.getConfigId()).append(",");
                 sb.append(item.getConfigCode()).append(",");
                 sb.append(item.getConfigName()).append(",");
                 sb.append(item.getConfigType()).append(",");
                 sb.append(item.getConfigKey()).append(",");
                 sb.append(item.getConfigValue()).append(",");
                 sb.append(item.getValueType()).append(",");
                 sb.append(item.getDefaultValue()).append(",");
                 sb.append(item.getIsEnabled()).append("\n");
             }
             response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
             response.getOutputStream().flush();
         } catch (Exception e) {
             log.error("导出系统配置异常", e);
         }
     }
 
     @Operation(summary = "重置配置为默认值")
     @ApiOperation("重置配置为默认值")
     @PostMapping("/reset/{category}")
     public MyJsonBean<Void> resetConfiguration(
             @ApiParam(value = "配置分类", required = true) @PathVariable String category) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             List<BudgetSystemConfigEntity> configs = configService.getByType(category);
             for (BudgetSystemConfigEntity config : configs) {
                 if (config.getDefaultValue() != null) {
                     config.setConfigValue(config.getDefaultValue());
                     configService.update(config);
                 }
             }
             result.setCode(1);
             result.setMsg("重置成功");
         } catch (Exception e) {
             log.error("重置配置异常", e);
             result.setCode(0);
             result.setMsg("重置失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取配置历史")
     @ApiOperation("获取配置历史")
     @GetMapping("/history")
     public MyJsonBean<List<BudgetSystemConfigEntity>> getConfigHistory(
             @RequestParam(required = false) String category) {
         MyJsonBean<List<BudgetSystemConfigEntity>> result = new MyJsonBean<>();
         try {
             List<BudgetSystemConfigEntity> configs;
             if (category != null && category.length() > 0) {
                 configs = configService.getByType(category);
             } else {
                 configs = configService.getAll();
             }
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(configs);
         } catch (Exception e) {
             log.error("获取配置历史异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 }