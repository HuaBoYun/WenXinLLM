package com.huabo.system.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemRefopmReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.job.JobService;
import com.huabo.system.service.business.RefopmReminderV1Service;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.AutoRefopmReminderParam;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 催办管理控制器（迭代版本）
 * <p>提供催办管理的列表查询、新增、修改、删除、自动催办等接口（V1版本）</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "催办管理-迭代版本")
@RequestMapping(value = "/v1-refopm/reminder")
@Slf4j
public class RefopmReminderV1Controller {

	@Resource
	private RefopmReminderV1Service refopmReminderV1Service;
	@Resource
	private JobService jobService;

	@Resource
	private UserProvider userProvider;

	
	  @OperationLog(
	            success = "列表",
	            busType = "催办管理",
	            fail = "列表",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblSystemRefopmReminderV> getRefopmReminderList(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefopmReminderQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefopmReminderV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refopmReminderV1Service.getRefopmReminderList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 列表查询 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = "新增/更新",
	            busType = "催办管理",
	            fail = "新增/更新",
	            operationType = OperationType.ADD,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblSystemRefopmReminderV> saveOrUpdateRefopmReminder(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefopmReminderV param) throws Exception {
		MyJsonBean<TblSystemRefopmReminderV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refopmReminderV1Service.saveOrUpdateRefopmReminder(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 新增/更新 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = "刪除",
	            busType = "催办管理",
	            fail = "刪除",
	            operationType = OperationType.DELETE,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteRefopmReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderV1Service.deleteRefopmReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 刪除 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = "详情 查询",
	            busType = "催办管理",
	            fail = "详情 查询",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblSystemRefopmReminderV> getRefopmReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<TblSystemRefopmReminderV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderV1Service.getRefopmReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 详情 查询  ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = " 停止催办",
	            busType = "催办管理",
	            fail = " 停止催办",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 停止催办")
	@PutMapping("/is-stop-reminder/{id}")
	public MyJsonBean<Void> isStopReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderV1Service.isStopReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 停止催办 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}
	  @OperationLog(
	            success = "启动催办",
	            busType = "催办管理",
	            fail = " 启动催办",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 启动催办")
	@PutMapping("/is-start-reminder/{id}")
	public MyJsonBean<Void> isStartReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderV1Service.isStartReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 停止催办 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = "催办",
	            busType = "催办管理",
	            fail = "催办",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 催办")
	@PutMapping("/do-reminder/{id}")
	public MyJsonBean<Void> reminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderV1Service.reminder(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 催办 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	  
	  @OperationLog(
	            success = "自动催办",
	            busType = "催办管理",
	            fail = "自动催办",
	            operationType = OperationType.SELECT,
	            subType = "催办信息"
	    )
	@Operation(summary="催办管理 自动催办")
	@PutMapping("/auto")
	public MyJsonBean<Void> auto(@RequestHeader("token") String token, @RequestBody AutoRefopmReminderParam param) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		try {
			jobService.refopmReminderAutoMonth(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 自动催办 ...接口 异常", e);
			throw e;
		}
		return MyResponseFormat.retParam(200, 200, null);
	}
}
