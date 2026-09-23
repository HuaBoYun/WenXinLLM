package com.huabo.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.dto.TransformTaskDTO;
import com.huabo.finance.entity.CollectionTransformConfig;
import com.huabo.finance.entity.TempCollectionPlan;
import com.huabo.finance.entity.TransformAlgorithmConfig;
import com.huabo.finance.entity.TransformFieldMapping;
import com.huabo.finance.entity.TransformRule;
import com.huabo.finance.service.CollectionTransformConfigService;
import com.huabo.finance.service.ITransformAlgorithmConfigService;
import com.huabo.finance.service.ITransformFieldMappingService;
import com.huabo.finance.service.ITransformTaskService;
import com.huabo.finance.service.TempCollectionPlanService;
import com.huabo.finance.service.TransformRuleService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/transform")
@Tag(name = "数据转换管理", description = "数据转换")
public class DataTransformController {
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private ITransformTaskService transformTaskService;

	@Autowired
	private ITransformFieldMappingService fieldMappingService;

	@Autowired
	private ITransformAlgorithmConfigService algorithmConfigService;

	@Resource
	private TempCollectionPlanService tempCollectionPlanService;

	@Resource
	private CollectionTransformConfigService collectionTransformConfigService;

	@Resource
	private TransformRuleService transformRuleService;

	@PostMapping(value = "/start", produces = "application/json; charset=utf-8", consumes = "application/x-www-form-urlencoded")
	@Operation(summary = "启动转换任务,POST,创建并启动一个新的数据转换任务")
	public JsonBean startTransform(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskName", description = "任务名称", required = true) @RequestParam String taskName,
			@Parameter(name = "collectionTaskId", description = "采集任务ID", required = true) @RequestParam String collectionTaskId,
			@Parameter(name = "transformType", description = "转换类型", required = true) @RequestParam String transformType,
			@Parameter(name = "remark", description = "备注", required = false) @RequestParam(required = false) String remark)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			// 此接口已废弃,请使用 /create 接口
			return ResponseFormat.retParam(0, "此接口已废弃,请使用 /create 接口", null);
		} catch (Exception e) {
			log.error("启动转换任务失败", e);
			return ResponseFormat.retParam(0, "启动转换任务失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/pause/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "暂停转换任务,POST,暂停指定的转换任务")
	public JsonBean pauseTransform(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.pauseTask(taskId);
		} catch (Exception e) {
			log.error("暂停转换任务失败", e);
			return ResponseFormat.retParam(0, "暂停转换任务失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/resume/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "恢复转换任务,POST,恢复指定的转换任务")
	public JsonBean resumeTransform(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.resumeTask(taskId);
		} catch (Exception e) {
			log.error("恢复转换任务失败", e);
			return ResponseFormat.retParam(0, "恢复转换任务失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/cancel/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "取消转换任务,POST,取消指定的转换任务")
	public JsonBean cancelTransform(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.cancelTask(taskId);
		} catch (Exception e) {
			log.error("取消转换任务失败", e);
			return ResponseFormat.retParam(0, "取消转换任务失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/status/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取转换任务状态,GET,获取指定转换任务的状态信息")
	public JsonBean getTransformStatus(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.getTaskDetail(taskId);
		} catch (Exception e) {
			log.error("获取转换任务状态失败", e);
			return ResponseFormat.retParam(0, "获取转换任务状态失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/list", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取转换任务列表,GET,分页查询转换任务列表")
	public JsonBean getTransformTaskList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "页码", required = true) @RequestParam Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页数量", required = true) @RequestParam Integer pageSize)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.getTaskList(pageNumber, pageSize);
		} catch (Exception e) {
			log.error("获取转换任务列表失败", e);
			return ResponseFormat.retParam(0, "获取转换任务列表失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/detail/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取转换任务详情,GET,获取指定转换任务的详细信息")
	public JsonBean getTransformTaskDetail(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.getTaskDetail(taskId);
		} catch (Exception e) {
			log.error("获取转换任务详情失败", e);
			return ResponseFormat.retParam(0, "获取转换任务详情失败: " + e.getMessage(), null);
		}
	}

	@DeleteMapping(value = "/delete/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "删除转换任务,DELETE,删除指定的转换任务")
	public JsonBean deleteTransformTask(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.deleteTask(taskId);
		} catch (Exception e) {
			log.error("删除转换任务失败", e);
			return ResponseFormat.retParam(0, "删除转换任务失败: " + e.getMessage(), null);
		}
	}

	// ==================== 新增接口 ====================

	@GetMapping(value = "/collection/tables/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取采集任务的源表列表,GET,获取指定采集任务采集的所有表")
	public JsonBean getSourceTables(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "采集任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformTaskService.getSourceTables(taskId);
	}

	@GetMapping(value = "/collection/fields/{taskId}/{tableName}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取源表字段列表,GET,获取指定源表的所有字段信息")
	public JsonBean getSourceFields(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "采集任务ID", required = true) @PathVariable String taskId,
			@Parameter(name = "tableName", description = "表名", required = true) @PathVariable String tableName)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformTaskService.getSourceFields(taskId, tableName);
	}

	@GetMapping(value = "/system/tables", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取系统表列表,GET,获取所有系统表列表")
	public JsonBean getSystemTables(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformTaskService.getSystemTables();
	}

	@GetMapping(value = "/system/fields/{tableName}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取系统表字段列表,GET,获取指定系统表的所有字段信息")
	public JsonBean getSystemFields(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "tableName", description = "表名", required = true) @PathVariable String tableName)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformTaskService.getSystemFields(tableName);
	}

	@PostMapping(value = "/validate", produces = "application/json; charset=utf-8")
	@Operation(summary = "验证字段类型匹配,POST,验证源字段类型是否可以转换为目标字段类型")
	public JsonBean validateTypeConversion(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "sourceType", description = "源字段类型", required = true) @RequestParam String sourceType,
			@Parameter(name = "targetType", description = "目标字段类型", required = true) @RequestParam String targetType,
			@Parameter(name = "sampleData", description = "样本数据(可选)", required = false) @RequestParam(required = false) String sampleData)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		// 将sampleData字符串转换为List
		List<String> sampleDataList = null;
		if (sampleData != null && !sampleData.isEmpty()) {
			// 简单处理:按逗号分割
			sampleDataList = java.util.Arrays.asList(sampleData.split(","));
		}
		return transformTaskService.validateTypeConversion(sourceType, targetType, sampleDataList);
	}

	@PostMapping(value = "/create", produces = "application/json; charset=utf-8", consumes = "application/json")
	@Operation(summary = "创建转换任务,POST,创建新的数据转换任务(带字段映射和计算规则)")
	public JsonBean createTransformTask(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskDTO", description = "转换任务配置", required = true) @RequestBody TransformTaskDTO taskDTO)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			String createUser = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";
			return transformTaskService.createTransformTask(taskDTO, createUser);
		} catch (Exception e) {
			log.error("创建转换任务失败", e);
			return ResponseFormat.retParam(0, "创建转换任务失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/execute/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "执行转换任务,POST,立即执行指定的转换任务")
	public JsonBean executeTransformTask(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.executeTransform(taskId);
		} catch (Exception e) {
			log.error("执行转换任务失败", e);
			return ResponseFormat.retParam(0, "执行转换任务失败: " + e.getMessage(), null);
		}
	}

	// ==================== 临时采集方案接口 ====================

	@PostMapping(value = "/tempPlan/save", produces = "application/json; charset=utf-8")
	@Operation(summary = "保存临时采集方案,POST,保存临时采集方案配置")
	public JsonBean saveTempPlan(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TempCollectionPlan plan) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return tempCollectionPlanService.saveTempPlan(staff, plan);
	}

	@GetMapping(value = "/tempPlan/getByTaskId", produces = "application/json; charset=utf-8")
	@Operation(summary = "根据任务ID获取临时方案,GET,根据采集任务ID获取临时方案配置")
	public JsonBean getTempPlanByTaskId(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @RequestParam String taskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return tempCollectionPlanService.getTempPlanByTaskId(taskId);
	}

	// ==================== 转化配置接口 ====================

	@PostMapping(value = "/config/save", produces = "application/json; charset=utf-8")
	@Operation(summary = "保存转化配置,POST,保存采集任务的转化配置")
	public JsonBean saveTransformConfig(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody CollectionTransformConfig config) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTransformConfigService.saveTransformConfig(staff, config);
	}

	@PostMapping(value = "/config/executeAuto", produces = "application/json; charset=utf-8")
	@Operation(summary = "执行自动转化,POST,采集完成后自动执行转化规则")
	public JsonBean executeAutoTransform(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "collectionTaskId", description = "采集任务ID", required = true) @RequestParam String collectionTaskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTransformConfigService.executeAutoTransform(collectionTaskId);
	}

	// ==================== 转化规则接口 ====================

	@PostMapping(value = "/rule/save", produces = "application/json; charset=utf-8")
	@Operation(summary = "保存转化规则,POST,保存数据转化规则配置")
	public JsonBean saveTransformRule(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TransformRule rule) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformRuleService.saveTransformRule(staff, rule);
	}

	@GetMapping(value = "/rule/getByCollectionTaskId", produces = "application/json; charset=utf-8")
	@Operation(summary = "根据采集任务ID获取转化规则列表,GET,获取指定采集任务的所有转化规则")
	public JsonBean getRulesByCollectionTaskId(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "collectionTaskId", description = "采集任务ID", required = true) @RequestParam String collectionTaskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformRuleService.getRulesByCollectionTaskId(collectionTaskId);
	}

	@PostMapping(value = "/rule/previewSql", produces = "application/json; charset=utf-8")
	@Operation(summary = "预览转化SQL,POST,预览转化规则生成的SQL语句")
	public JsonBean previewTransformSql(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TransformRule rule) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return transformRuleService.previewTransformSql(rule);
	}

	// ==================== 字段映射配置接口 ====================

	@PostMapping(value = "/mapping/save", produces = "application/json; charset=utf-8")
	@Operation(summary = "保存字段映射配置,POST,保存转化任务的字段映射配置")
	public JsonBean saveFieldMappings(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transformTaskId", description = "转化任务ID", required = true) @RequestParam String transformTaskId,
			@RequestBody List<TransformFieldMapping> mappings) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return fieldMappingService.saveMappings(mappings, transformTaskId, staff.getStaffid().toString());
		} catch (Exception e) {
			log.error("保存字段映射配置失败", e);
			return ResponseFormat.retParam(0, "保存失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/mapping/list/{transformTaskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "查询字段映射列表,GET,查询指定转化任务的字段映射配置")
	public JsonBean getFieldMappingList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transformTaskId", description = "转化任务ID", required = true) @PathVariable String transformTaskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return fieldMappingService.getMappingList(transformTaskId);
		} catch (Exception e) {
			log.error("查询字段映射列表失败", e);
			return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/mapping/auto-match", produces = "application/json; charset=utf-8")
	@Operation(summary = "智能字段匹配,POST,自动匹配源表和目标表的字段")
	public JsonBean autoMatchFields(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transformTaskId", description = "转化任务ID", required = true) @RequestParam String transformTaskId,
			@Parameter(name = "sourceTable", description = "源表名", required = true) @RequestParam String sourceTable,
			@Parameter(name = "targetTable", description = "目标表名", required = true) @RequestParam String targetTable) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return fieldMappingService.autoMatchFields(transformTaskId, sourceTable, targetTable);
		} catch (Exception e) {
			log.error("智能字段匹配失败", e);
			return ResponseFormat.retParam(0, "匹配失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/mapping/delete/{mappingId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "删除字段映射,POST,删除指定的字段映射配置")
	public JsonBean deleteFieldMapping(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "mappingId", description = "映射ID", required = true) @PathVariable String mappingId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return fieldMappingService.deleteMapping(mappingId);
		} catch (Exception e) {
			log.error("删除字段映射失败", e);
			return ResponseFormat.retParam(0, "删除失败: " + e.getMessage(), null);
		}
	}

	// ==================== 算法配置接口 ====================

	@PostMapping(value = "/algorithm/save", produces = "application/json; charset=utf-8")
	@Operation(summary = "保存算法配置,POST,保存转化任务的算法配置")
	public JsonBean saveAlgorithmConfigs(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transformTaskId", description = "转化任务ID", required = true) @RequestParam String transformTaskId,
			@RequestBody List<TransformAlgorithmConfig> configs) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return algorithmConfigService.saveConfigs(configs, transformTaskId, staff.getStaffid().toString());
		} catch (Exception e) {
			log.error("保存算法配置失败", e);
			return ResponseFormat.retParam(0, "保存失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/algorithm/list/{transformTaskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "查询算法配置列表,GET,查询指定转化任务的算法配置")
	public JsonBean getAlgorithmConfigList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "transformTaskId", description = "转化任务ID", required = true) @PathVariable String transformTaskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return algorithmConfigService.getConfigList(transformTaskId);
		} catch (Exception e) {
			log.error("查询算法配置列表失败", e);
			return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/algorithm/available", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取可用算法列表,GET,获取所有可用的转化算法")
	public JsonBean getAvailableAlgorithms(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return algorithmConfigService.getAvailableAlgorithms();
		} catch (Exception e) {
			log.error("获取可用算法列表失败", e);
			return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/algorithm/delete/{configId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "删除算法配置,POST,删除指定的算法配置")
	public JsonBean deleteAlgorithmConfig(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "configId", description = "配置ID", required = true) @PathVariable String configId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return algorithmConfigService.deleteConfig(configId);
		} catch (Exception e) {
			log.error("删除算法配置失败", e);
			return ResponseFormat.retParam(0, "删除失败: " + e.getMessage(), null);
		}
	}

	// ==================== 财务版本和转换配置查询接口 ====================

	@GetMapping(value = "/version/query/{collectionTaskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "根据采集任务ID查询财务版本信息,GET,查询采集任务关联的财务版本FID和名称")
	public JsonBean getFinanceVersionByCollectionTask(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "collectionTaskId", description = "采集任务ID", required = true) @PathVariable String collectionTaskId) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.getFinanceVersionByCollectionTask(collectionTaskId);
		} catch (Exception e) {
			log.error("查询财务版本信息失败", e);
			return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/config/query/{versionFid}", produces = "application/json; charset=utf-8")
	@Operation(summary = "根据财务版本FID查询转换配置,GET,查询指定财务版本的所有转换配置信息")
	public JsonBean getTransformConfigByVersion(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "versionFid", description = "财务版本FID", required = true) @PathVariable String versionFid) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			return transformTaskService.getTransformConfigByVersion(versionFid);
		} catch (Exception e) {
			log.error("查询转换配置失败", e);
			return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
		}
	}
}
