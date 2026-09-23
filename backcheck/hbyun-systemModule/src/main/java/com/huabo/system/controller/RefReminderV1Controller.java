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
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemRefReminderAccessV;
import com.huabo.system.entity.TblSystemRefReminderV;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.business.RefReminderV1Service;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.vo.param.TblSystemRefReminderAccessQueryParam;
import com.huabo.system.vo.param.TblSystemRefReminderQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 催办信息控制器（迭代版本）
 * <p>提供催办信息的列表查询、新增、修改、详情查看等接口（V1版本）</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "催办信息-迭代版本")
@Slf4j
public class RefReminderV1Controller {

	@Resource
	private RefReminderV1Service refReminderV1Service;

	@Resource
	private UserProvider userProvider;

	
	 @OperationLog(
	            success = "列表查询",
	            busType = "催办管理",
	            fail = "列表查询",
	            operationType = OperationType.SELECT,
	            subType = "催办管理"
	    )
	@Operation(summary="催办信息 列表查询")
	@PostMapping("/v1-ref/reminder/getList")
	public MyJsonBean<TblSystemRefReminderV> getRefReminderList(@RequestHeader("token") String token,
			@RequestBody TblSystemRefReminderQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefReminderV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refReminderV1Service.getRefReminderList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 列表查询 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	
	  @OperationLog(
	            success = "详情 查询",
	            busType = "催办管理",
	            fail = "详情 查询",
	            operationType = OperationType.SELECT,
	            subType = "催办管理"
	    )
	@Operation(summary="催办信息 详情 查询")
	@GetMapping("/v1-ref/reminder/{id}")
	public MyJsonBean<TblSystemRefReminderV> getRefReminder(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<TblSystemRefReminderV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refReminderV1Service.getRefReminder(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 详情 查询  ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	
	  @OperationLog(
	            success = "浏览",
	            busType = "催办管理",
	            fail = "浏览",
	            operationType = OperationType.SELECT,
	            subType = "催办管理"
	    )
	@Operation(summary="催办信息 浏览")
	@PutMapping("/v1-ref/reminder/isRead/{id}")
	public MyJsonBean<Void> isRead(@RequestHeader("token") String token, @PathVariable Long id) throws Exception {
		MyJsonBean<Void> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		try {
			myJsonBean = refReminderV1Service.isRead(loginStaff, id, token);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办信息 浏览 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}

	
	  @OperationLog(
	            success = "列表查询",
	            busType = "催办管理",
	            fail = "列表查询",
	            operationType = OperationType.SELECT,
	            subType = "访问记录"
	    )
	@Operation(summary="催办访问记录 列表查询")
	@PostMapping("/v1-ref/reminder/access/getList")
	public MyJsonBean<TblSystemRefReminderAccessV> getRefReminderAccessList(@RequestHeader("token") String token,
			@RequestBody @Validated TblSystemRefReminderAccessQueryParam param) throws Exception {
		MyJsonBean<TblSystemRefReminderAccessV> myJsonBean = null;
		TblStaffUtil loginStaff = userProvider.get();
		param.setCreator(loginStaff.getStaffid().longValue());
		param.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		try {
			myJsonBean = refReminderV1Service.getRefReminderAccessList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("催办访问记录 列表查询 ...接口 异常", e);
			throw e;
		}
		return myJsonBean;
	}
}
