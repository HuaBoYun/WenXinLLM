package com.huabo.system.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemRefReminder;
import com.huabo.system.entity.TblSystemRefReminderAccess;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.business.RefReminderService;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 催办信息控制器
 * <p>提供催办信息的列表查询、新增、修改、详情查看等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "催办信息")
@Slf4j
public class RefReminderController {

	@Resource
	private RefReminderService refReminderService;
	
	@Resource
    private UserProvider userProvider;

	@Operation(summary="催办信息 列表查询")
	@PostMapping("/ref/reminder/getList")
	public MyJsonBean<TblSystemRefReminder> getRefReminderList(@RequestHeader("token") String token,
			@RequestBody TblSystemRefReminderQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefReminder> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refReminderService.getRefReminderList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 列表查询 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办信息 详情 查询")
	@GetMapping("/ref/reminder/{id}")
	public MyJsonBean<TblSystemRefReminder> getRefReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<TblSystemRefReminder> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refReminderService.getRefReminder(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 详情 查询  ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办信息 浏览")
	@PutMapping("/ref/reminder/isRead/{id}")
	public MyJsonBean<Void> isRead(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refReminderService.isRead(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 浏览 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}

	@Operation(summary="催办访问记录 列表查询")
	@PostMapping("/ref/reminder/access/getList")
	public MyJsonBean<TblSystemRefReminderAccess> getRefReminderAccessList(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefReminderAccessQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefReminderAccess> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refReminderService.getRefReminderAccessList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办访问记录 列表查询 ...接口 异常", e);
			throw new ServiceException(500, e.getMessage());
		}
		return myJsonBean;
	}
}
