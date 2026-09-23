package com.huabo.finance.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.finance.entity.BdFinancedate;
import com.huabo.finance.entity.CollectionTask;
import com.huabo.finance.mapper.BdFinancedateMapper;
import com.huabo.finance.mapper.CollectionTaskMapper;
import com.huabo.finance.service.ICollectionTaskService;
import com.huabo.finance.vo.CollectionTaskVO;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/collection")
@Tag(name = "数据采集管理", description = "数据采集")
public class DataCollectionController {
	@Autowired
	private UserProvider userProvider;

	@Autowired
	private ICollectionTaskService collectionTaskService;

	@Autowired
	private CollectionTaskMapper collectionTaskMapper;

	@Autowired
	private BdFinancedateMapper bdFinancedateMapper;

	@PostMapping(value = "/start", produces = "application/json; charset=utf-8", consumes = "application/x-www-form-urlencoded")
	@Operation(summary  = "启动采集任务,POST,创建并启动一个新的数据采集任务")
	public JsonBean startCollection(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskName", description = "任务名称", required = true) @RequestParam String taskName,
			@Parameter(name = "planId", description = "采集方案ID", required = true) @RequestParam String planId,
			@Parameter(name = "dataSourceId", description = "数据源ID", required = true) @RequestParam String dataSourceId,
			@Parameter(name = "collectionType", description = "采集类型", required = true) @RequestParam String collectionType,
			@Parameter(name = "remark", description = "备注", required = false) @RequestParam(required = false) String remark)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			String createUser = staff.getStaffid() != null ? staff.getStaffid().toString() : "SYSTEM";
			return collectionTaskService.startCollection(taskName, planId, dataSourceId,
					collectionType, remark, createUser);
		} catch (Exception e) {
			log.error("启动采集任务失败", e);
			return ResponseFormat.retParam(0, "启动采集任务失败: " + e.getMessage(), null);
		}
	}

	@PostMapping(value = "/pause/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "暂停采集任务 , POST , 暂停指定的采集任务")
	public JsonBean pauseCollection(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.pauseCollection(taskId);
	}

	@PostMapping(value = "/resume/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "恢复采集任务POST恢复指定的采集任务")
	public JsonBean resumeCollection(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.resumeCollection(taskId);
	}

	@PostMapping(value = "/cancel/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "取消采集任务,POST,取消指定的采集任务")
	public JsonBean cancelCollection(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.cancelCollection(taskId);
	}

	@GetMapping(value = "/status/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取采集任务状态,GET,获取指定采集任务的状态信息")
	public JsonBean getCollectionStatus(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.getTaskStatus(taskId);
	}

	@GetMapping(value = "/list", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取采集任务列表,GET,分页查询采集任务列表")
	public JsonBean getCollectionTaskList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "页码", required = true) @RequestParam Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页数量", required = true) @RequestParam Integer pageSize,
			@Parameter(name = "taskName", description = "任务名称", required = false) @RequestParam(required = false) String taskName,
			@Parameter(name = "taskStatus", description = "任务状态", required = false) @RequestParam(required = false) String taskStatus)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		try {
			// 使用自定义SQL查询(兼容达梦数据库)
			int offset = (pageNumber - 1) * pageSize;

			// 调用带条件的分页查询
			List<CollectionTask> taskList = collectionTaskMapper.selectPageListWithFilter(
					offset, pageSize, taskName, taskStatus);
			long total = collectionTaskMapper.selectTotalCountWithFilter(taskName, taskStatus);

			// 转换为VO对象,字段名与前端保持一致
			List<CollectionTaskVO> voList = new ArrayList<>();
			for (CollectionTask task : taskList) {
				CollectionTaskVO vo = new CollectionTaskVO();
				vo.setTaskId(task.getTaskId());
				vo.setTaskName(task.getTaskName());
				vo.setPlanId(task.getPlanId());
				vo.setDataSourceId(task.getDataSourceId());

				// 查询数据源名称
				try {
					if (task.getDataSourceId() != null) {
						BdFinancedate dataSource = bdFinancedateMapper.selectById(task.getDataSourceId());
						if (dataSource != null) {
							vo.setDataSourceName(dataSource.getFintext());
						}
					}
				} catch (Exception e) {
					log.warn("查询数据源名称失败: {}", e.getMessage());
				}

				vo.setCollectionType(task.getCollectionType());
				vo.setStatus(task.getTaskStatus());  // taskStatus -> status
				// 将BigDecimal转换为Integer
				vo.setProgress(task.getProgress() != null ? task.getProgress().intValue() : 0);
				vo.setRecordCount(task.getRecordCount());
				vo.setTotalCount(task.getTotalCount());
				vo.setSuccessCount(task.getSuccessCount());
				vo.setFailedCount(task.getFailedCount());
				vo.setStartTime(task.getStartTime());
				vo.setEndTime(task.getEndTime());
				vo.setElapsedTime(task.getElapsedTime());
				vo.setErrorMessage(task.getErrorMessage());
				vo.setRemark(task.getRemark());
				vo.setCreateTime(task.getCreateTime());
				vo.setUpdateTime(task.getUpdateTime());
				voList.add(vo);
			}

			Map<String, Object> data = new HashMap<>();
			data.put("total", total);
			data.put("pageNum", pageNumber);
			data.put("pageSize", pageSize);
			data.put("list", voList);

			return ResponseFormat.retParam(1, 200, data);
		} catch (Exception e) {
			log.error("获取采集任务列表失败", e);
			return ResponseFormat.retParam(0, "获取采集任务列表失败: " + e.getMessage(), null);
		}
	}

	@GetMapping(value = "/detail/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "获取采集任务详情,GET,获取指定采集任务的详细信息")
	public JsonBean getCollectionTaskDetail(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.getTaskStatus(taskId);
	}

	@DeleteMapping(value = "/delete/{taskId}", produces = "application/json; charset=utf-8")
	@Operation(summary = "删除采集任务,DELETE,删除指定的采集任务")
	public JsonBean deleteCollectionTask(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "taskId", description = "任务ID", required = true) @PathVariable String taskId)
			throws Exception {
		TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
		return collectionTaskService.deleteCollection(taskId);
	}
}

