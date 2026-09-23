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
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemRefopmReminder;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.job.JobService;
import com.huabo.system.service.business.RefopmReminderService;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.MyResponseFormat;
import com.huabo.system.vo.param.AutoRefopmReminderParam;
import com.huabo.system.vo.param.TblSystemRefopmReminderQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 催办管理控制器
 * <p>提供催办管理的列表查询、新增、修改、删除、自动催办等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "催办管理")
@RequestMapping(value = "/refopm/reminder")
@Slf4j
public class RefopmReminderController {

	@Resource
	private RefopmReminderService refopmReminderService;
	@Resource
	private JobService jobService;
	
	@Resource
    private UserProvider userProvider;

	@Operation(summary="催办管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblSystemRefopmReminder> getRefopmReminderList(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefopmReminderQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefopmReminder> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refopmReminderService.getRefopmReminderList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 列表查询 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblSystemRefopmReminder> saveOrUpdateRefopmReminder(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefopmReminder param) throws Exception {
		MyJsonBean<TblSystemRefopmReminder> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refopmReminderService.saveOrUpdateRefopmReminder(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 新增/更新 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteRefopmReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderService.deleteRefopmReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 刪除 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblSystemRefopmReminder> getRefopmReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<TblSystemRefopmReminder> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderService.getRefopmReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 详情 查询  ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办管理 停止催办")
	@PutMapping("/is-stop-reminder/{id}")
	public MyJsonBean<Void> isStopReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderService.isStopReminder(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 停止催办 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办管理 催办")
	@PutMapping("/do-reminder/{id}")
	public MyJsonBean<Void> reminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refopmReminderService.reminder(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办管理 催办 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

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
			throw new ServiceException(500, e.getMessage());
		}
		return MyResponseFormat.retParam(200, 200, null);
	}
}
