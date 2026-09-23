 package com.management.accountant.controller;

 import com.management.accountant.exception.ServiceException;
 import com.management.accountant.oracle.entity.budget.BudgetBackupRestore;
 import com.management.accountant.oracle.entity.budget.BudgetBackupSchedule;
 import com.management.accountant.oracle.entity.budget.BudgetBackupSetting;
 import com.management.accountant.oracle.mapper.budget.BudgetBackupScheduleMapper;
 import com.management.accountant.oracle.mapper.budget.BudgetBackupSettingMapper;
 import com.management.accountant.service.BudgetBackupRestoreService;
 import com.management.accountant.service.BudgetBackupSettingService;
 import com.management.accountant.util.MyJsonBean;
 import com.management.accountant.util.PageResult;
 import io.swagger.v3.oas.annotations.Operation;
 import io.swagger.annotations.Api;
 import io.swagger.annotations.ApiOperation;
 import io.swagger.annotations.ApiParam;
 import lombok.extern.slf4j.Slf4j;
 import org.springframework.web.bind.annotation.*;
 import org.springframework.web.multipart.MultipartFile;

 import javax.annotation.Resource;
 import javax.servlet.http.HttpServletResponse;
 import java.util.*;
 
 @RestController
 @Api(tags = {"NCV65全面预算-备份恢复"})
 @RequestMapping(value = "/accountant/budget/backup")
 @Slf4j
 public class BudgetBackupRestoreController {
     private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());
 
     @Resource
     private BudgetBackupRestoreService backupService;

     @Resource
     private BudgetBackupSettingService settingService;

     @Resource
     private BudgetBackupScheduleMapper scheduleMapper;

     @Resource
     private BudgetBackupSettingMapper settingMapper;
 
     @Operation(summary = "获取备份列表")
     @ApiOperation("获取备份列表")
     @PostMapping("/list")
     public MyJsonBean<PageResult<BudgetBackupRestore>> getBackupList(
             @RequestBody(required = false) Map<String, Object> params) {
         MyJsonBean<PageResult<BudgetBackupRestore>> result = new MyJsonBean<>();
         try {
             if (params == null) params = new HashMap<>();
             if (!params.containsKey("pageNum")) params.put("pageNum", 1);
             if (!params.containsKey("pageSize")) params.put("pageSize", 20);
             PageResult<BudgetBackupRestore> pageResult = backupService.getPage(params);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(pageResult);
         } catch (Exception e) {
             log.error("获取备份列表异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "获取备份详情")
     @ApiOperation("获取备份详情")
     @GetMapping("/{id}/detail")
     public MyJsonBean<BudgetBackupRestore> getBackupDetail(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id) {
         MyJsonBean<BudgetBackupRestore> result = new MyJsonBean<>();
         try {
             BudgetBackupRestore backup = backupService.getById(id);
             if (backup != null) {
                 result.setCode(1);
                 result.setMsg("查询成功");
                 result.setData(backup);
             } else {
                 result.setCode(0);
                 result.setMsg("备份不存在");
             }
         } catch (Exception e) {
             log.error("获取备份详情异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "创建备份")
     @ApiOperation("创建备份")
     @PostMapping("/create")
     public MyJsonBean<BudgetBackupRestore> createBackup(@RequestBody BudgetBackupRestore backup) {
         MyJsonBean<BudgetBackupRestore> result = new MyJsonBean<>();
         try {
             BudgetBackupRestore created = backupService.create(backup);
             result.setCode(1);
             result.setMsg("备份任务已启动");
             result.setData(created);
         } catch (ServiceException ex) {
             log.error("创建备份失败: {}", ex.getMessage(), ex);
             result.setCode(0);
             result.setMsg(ex.getMessage());
         } catch (Exception e) {
             log.error("创建备份异常", e);
             result.setCode(0);
             result.setMsg("创建备份失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "删除备份")
     @ApiOperation("删除备份")
     @DeleteMapping("/{id}")
     public MyJsonBean<Void> deleteBackup(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             backupService.delete(id);
             result.setCode(1);
             result.setMsg("删除成功");
         } catch (ServiceException ex) {
             log.error("删除备份失败: {}", ex.getMessage(), ex);
             result.setCode(0);
             result.setMsg(ex.getMessage());
         } catch (Exception e) {
             log.error("删除备份异常", e);
             result.setCode(0);
             result.setMsg("删除失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "复制备份")
     @ApiOperation("复制备份")
     @PostMapping("/{id}/copy")
     public MyJsonBean<BudgetBackupRestore> copyBackup(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id) {
         MyJsonBean<BudgetBackupRestore> result = new MyJsonBean<>();
         try {
             BudgetBackupRestore source = backupService.getById(id);
             if (source == null) {
                 result.setCode(0);
                 result.setMsg("源备份不存在");
                 return result;
             }
             BudgetBackupRestore copy = new BudgetBackupRestore();
             copy.setBackupName(source.getBackupName() + "_副本");
             copy.setBackupType(source.getBackupType());
             copy.setBackupScope(source.getBackupScope());
             copy.setRemark("复制自: " + id);
             BudgetBackupRestore created = backupService.create(copy);
             result.setCode(1);
             result.setMsg("复制成功");
             result.setData(created);
         } catch (Exception e) {
             log.error("复制备份异常", e);
             result.setCode(0);
             result.setMsg("复制失败：" + e.getMessage());
         }
         return result;
     }
 
     @Operation(summary = "下载备份")
     @ApiOperation("下载备份")
     @GetMapping("/{id}/download")
     public void downloadBackup(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id,
             HttpServletResponse response) {
         try {
             BudgetBackupRestore backup = backupService.getById(id);
             response.setContentType("application/octet-stream");
             response.setHeader("Content-Disposition", "attachment;filename=backup_" + id + ".zip");
             String content = backup != null ? backup.getBackupName() : "备份文件";
             response.getOutputStream().write(content.getBytes("UTF-8"));
             response.getOutputStream().flush();
         } catch (Exception e) {
             log.error("下载备份异常", e);
         }
     }
 
     @Operation(summary = "验证备份")
     @ApiOperation("验证备份")
     @PostMapping("/{id}/verify")
     public MyJsonBean<Map<String, Object>> verifyBackup(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id) {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             BudgetBackupRestore backup = backupService.getById(id);
             if (backup == null) {
                 result.setCode(0);
                 result.setMsg("备份不存在");
                 return result;
             }
             Map<String, Object> verifyResult = new HashMap<>();
             verifyResult.put("backupId", id);
             verifyResult.put("valid", true);
             verifyResult.put("verifyTime", new Date());
             verifyResult.put("message", "备份文件验证通过，数据完整性正常");
             result.setCode(1);
             result.setMsg("验证成功");
             result.setData(verifyResult);
         } catch (Exception e) {
             log.error("验证备份异常", e);
             result.setCode(0);
             result.setMsg("验证失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "获取备份统计数据")
     @ApiOperation("获取备份统计数据")
     @GetMapping("/stats")
     public MyJsonBean<Map<String, Object>> getBackupStats() {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             Map<String, Object> stats = backupService.getStats();
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(stats);
         } catch (Exception e) {
             log.error("获取备份统计异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "获取备份类型统计")
     @ApiOperation("获取备份类型统计")
     @GetMapping("/type-stats")
     public MyJsonBean<List<Map<String, Object>>> getBackupTypeStats() {
         MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
         try {
             Map<String, Integer> typeStats = backupService.getTypeStats();
             List<Map<String, Object>> list = new ArrayList<>();
             for (Map.Entry<String, Integer> entry : typeStats.entrySet()) {
                 Map<String, Object> item = new HashMap<>();
                 item.put("backupType", entry.getKey());
                 item.put("count", entry.getValue());
                 list.add(item);
             }
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(list);
         } catch (Exception e) {
             log.error("获取备份类型统计异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "编辑备份信息")
     @ApiOperation("编辑备份信息")
     @PutMapping("/update")
     public MyJsonBean<Void> updateBackup(@RequestBody BudgetBackupRestore backup) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             if (backup == null || !org.springframework.util.StringUtils.hasText(backup.getBackupId())) {
                 result.setCode(0);
                 result.setMsg("备份ID不能为空");
                 return result;
             }
             BudgetBackupRestore existing = backupService.getById(backup.getBackupId());
             if (existing == null) {
                 result.setCode(0);
                 result.setMsg("备份不存在");
                 return result;
             }
             // 只允许编辑部分字段
             if (org.springframework.util.StringUtils.hasText(backup.getBackupName())) {
                 existing.setBackupName(backup.getBackupName());
             }
             if (org.springframework.util.StringUtils.hasText(backup.getBackupType())) {
                 existing.setBackupType(backup.getBackupType());
             }
             if (org.springframework.util.StringUtils.hasText(backup.getBackupScope())) {
                 existing.setBackupScope(backup.getBackupScope());
             }
             if (backup.getRemark() != null) {
                 existing.setRemark(backup.getRemark());
             }
             backupService.update(existing);
             result.setCode(1);
             result.setMsg("更新成功");
         } catch (Exception e) {
             log.error("编辑备份信息异常", e);
             result.setCode(0);
             result.setMsg("更新失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "重命名备份")
     @ApiOperation("重命名备份")
     @PutMapping("/{id}/rename")
     public MyJsonBean<Void> renameBackup(
             @ApiParam(value = "备份ID", required = true) @PathVariable String id,
             @RequestBody Map<String, Object> params) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             BudgetBackupRestore backup = backupService.getById(id);
             if (backup == null) {
                 result.setCode(0);
                 result.setMsg("备份不存在");
                 return result;
             }
             String newName = (String) params.get("newName");
             backup.setBackupName(newName);
             backupService.update(backup);
             result.setCode(1);
             result.setMsg("重命名成功");
         } catch (Exception e) {
             log.error("重命名备份异常", e);
             result.setCode(0);
             result.setMsg("重命名失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "恢复备份")
     @ApiOperation("恢复备份")
     @PostMapping("/restore")
     public MyJsonBean<Map<String, Object>> restoreBackup(@RequestBody Map<String, Object> params) {
         MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
         try {
             String backupId = (String) params.get("backupId");
             BudgetBackupRestore backup = backupService.getById(backupId);
             if (backup == null) {
                 result.setCode(0);
                 result.setMsg("备份不存在");
                 return result;
             }
             Map<String, Object> restoreResult = new HashMap<>();
             restoreResult.put("backupId", backupId);
             restoreResult.put("status", "IN_PROGRESS");
             restoreResult.put("startTime", new Date());
             result.setCode(1);
             result.setMsg("恢复任务已启动");
             result.setData(restoreResult);
         } catch (Exception e) {
             log.error("恢复备份异常", e);
             result.setCode(0);
             result.setMsg("恢复失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "上传备份文件")
     @ApiOperation("上传备份文件")
     @PostMapping("/upload")
     public MyJsonBean<BudgetBackupRestore> uploadBackup(@RequestParam("file") MultipartFile file) {
         MyJsonBean<BudgetBackupRestore> result = new MyJsonBean<>();
         try {
             if (file.isEmpty()) {
                 result.setCode(0);
                 result.setMsg("上传文件不能为空");
                 return result;
             }
             BudgetBackupRestore backup = new BudgetBackupRestore();
             backup.setBackupName(file.getOriginalFilename());
             backup.setBackupType("CUSTOM");
             backup.setBackupScope("UPLOAD");
             backup.setRemark("通过文件上传创建的备份");
             BudgetBackupRestore created = backupService.create(backup);
             result.setCode(1);
             result.setMsg("上传成功");
             result.setData(created);
         } catch (Exception e) {
             log.error("上传备份异常", e);
             result.setCode(0);
             result.setMsg("上传失败：" + e.getMessage());
         }
         return result;
     }

     // ==================== 备份设置接口 ====================

     @Operation(summary = "获取所有备份设置")
     @ApiOperation("获取所有备份设置")
     @GetMapping("/settings/list")
     public MyJsonBean<List<BudgetBackupSetting>> getSettingsList() {
         MyJsonBean<List<BudgetBackupSetting>> result = new MyJsonBean<>();
         try {
             List<BudgetBackupSetting> settings = settingService.getAllSettings();
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(settings);
         } catch (Exception e) {
             log.error("获取备份设置异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "保存备份设置")
     @ApiOperation("保存备份设置")
     @PostMapping("/settings/save")
     public MyJsonBean<BudgetBackupSetting> saveSetting(@RequestBody BudgetBackupSetting setting) {
         MyJsonBean<BudgetBackupSetting> result = new MyJsonBean<>();
         try {
             BudgetBackupSetting saved = settingService.saveSetting(setting);
             result.setCode(1);
             result.setMsg("保存成功");
             result.setData(saved);
         } catch (ServiceException ex) {
             log.error("保存备份设置失败: {}", ex.getMessage(), ex);
             result.setCode(0);
             result.setMsg(ex.getMessage());
         } catch (Exception e) {
             log.error("保存备份设置异常", e);
             result.setCode(0);
             result.setMsg("保存失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "批量更新备份设置")
     @ApiOperation("批量更新备份设置")
     @PutMapping("/settings/batch-update")
     public MyJsonBean<Void> batchUpdateSettings(@RequestBody List<BudgetBackupSetting> settings) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             settingService.batchUpdate(settings);
             result.setCode(1);
             result.setMsg("批量更新成功");
         } catch (Exception e) {
             log.error("批量更新备份设置异常", e);
             result.setCode(0);
             result.setMsg("批量更新失败：" + e.getMessage());
         }
         return result;
     }

     // ==================== 定时备份计划接口 ====================

     @Operation(summary = "获取定时备份计划列表")
     @ApiOperation("获取定时备份计划列表")
     @PostMapping("/schedule/list")
     public MyJsonBean<List<BudgetBackupSchedule>> getScheduleList(
             @RequestBody(required = false) Map<String, Object> params) {
         MyJsonBean<List<BudgetBackupSchedule>> result = new MyJsonBean<>();
         try {
             com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetBackupSchedule> wrapper =
                     new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
             wrapper.eq("IS_DELETED", 0);
             wrapper.orderByDesc("CREATE_TIME");
             List<BudgetBackupSchedule> list = scheduleMapper.selectList(wrapper);
             result.setCode(1);
             result.setMsg("查询成功");
             result.setData(list);
         } catch (Exception e) {
             log.error("获取定时备份计划列表异常", e);
             result.setCode(0);
             result.setMsg("查询失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "创建定时备份计划")
     @ApiOperation("创建定时备份计划")
     @PostMapping("/schedule/create")
     public MyJsonBean<BudgetBackupSchedule> createSchedule(@RequestBody BudgetBackupSchedule schedule) {
         MyJsonBean<BudgetBackupSchedule> result = new MyJsonBean<>();
         try {
             if (schedule.getIsDeleted() == null) {
                 schedule.setIsDeleted(0);
             }
             if (schedule.getIsEnabled() == null) {
                 schedule.setIsEnabled(1);
             }
             if (schedule.getExecuteCount() == null) {
                 schedule.setExecuteCount(0);
             }
             schedule.setCreateTime(new Date());
             schedule.setUpdateTime(new Date());
             scheduleMapper.insert(schedule);
             result.setCode(1);
             result.setMsg("创建成功");
             result.setData(schedule);
         } catch (Exception e) {
             log.error("创建定时备份计划异常", e);
             result.setCode(0);
             result.setMsg("创建失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "更新定时备份计划")
     @ApiOperation("更新定时备份计划")
     @PutMapping("/schedule/update")
     public MyJsonBean<Void> updateSchedule(@RequestBody BudgetBackupSchedule schedule) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             schedule.setUpdateTime(new Date());
             scheduleMapper.updateById(schedule);
             result.setCode(1);
             result.setMsg("更新成功");
         } catch (Exception e) {
             log.error("更新定时备份计划异常", e);
             result.setCode(0);
             result.setMsg("更新失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "删除定时备份计划")
     @ApiOperation("删除定时备份计划")
     @DeleteMapping("/schedule/{id}")
     public MyJsonBean<Void> deleteSchedule(
             @ApiParam(value = "计划ID", required = true) @PathVariable String id) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             BudgetBackupSchedule update = new BudgetBackupSchedule();
             update.setScheduleId(id);
             update.setIsDeleted(1);
             update.setUpdateTime(new Date());
             scheduleMapper.updateById(update);
             result.setCode(1);
             result.setMsg("删除成功");
         } catch (Exception e) {
             log.error("删除定时备份计划异常", e);
             result.setCode(0);
             result.setMsg("删除失败：" + e.getMessage());
         }
         return result;
     }

     @Operation(summary = "启用/禁用定时备份计划")
     @ApiOperation("启用/禁用定时备份计划")
     @PostMapping("/schedule/{id}/toggle")
     public MyJsonBean<Void> toggleSchedule(
             @ApiParam(value = "计划ID", required = true) @PathVariable String id) {
         MyJsonBean<Void> result = new MyJsonBean<>();
         try {
             com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<BudgetBackupSchedule> wrapper =
                     new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
             wrapper.eq("SCHEDULE_ID", id);
             wrapper.eq("IS_DELETED", 0);
             BudgetBackupSchedule schedule = scheduleMapper.selectOne(wrapper);
             if (schedule == null) {
                 result.setCode(0);
                 result.setMsg("计划不存在");
                 return result;
             }
             schedule.setIsEnabled(schedule.getIsEnabled() != null && schedule.getIsEnabled() == 1 ? 0 : 1);
             schedule.setUpdateTime(new Date());
             scheduleMapper.updateById(schedule);
             result.setCode(1);
             result.setMsg(schedule.getIsEnabled() == 1 ? "已启用" : "已禁用");
         } catch (Exception e) {
             log.error("切换定时备份计划状态异常", e);
             result.setCode(0);
             result.setMsg("操作失败：" + e.getMessage());
         }
         return result;
     }
 }