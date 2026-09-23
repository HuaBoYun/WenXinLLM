package com.huabo.central.enterprises.audit.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.service.CommonService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.UpdateUserOnDutyStatusParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.UserAllResult;
import com.huabo.central.enterprises.audit.vo.result.UserAllResultExpress;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="通用接口",description="通用接口")
@RequestMapping(value = "/api-auth/common")
@Slf4j
public class CommonController {

	@Resource
	private CommonService commonService;
	
	@Resource
	private UserProvider userProvider;
	

	@Operation(summary = "用户信息查询")
	@GetMapping("/user/{id}")
	public MyJsonBean<TblStaffOracle> getUserInfo(@PathVariable Long id) {
		MyJsonBean<TblStaffOracle> myJsonBean = null;
		try {
			myJsonBean = commonService.getUserInfo(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("用户信息查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "更新用户在岗状态")
	@PostMapping("/user/update-on-duty-status")
	public MyJsonBean<Void> updateUserOnDutyStatus(@RequestBody UpdateUserOnDutyStatusParam param) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = commonService.updateUserOnDutyStatus(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("更新用户在岗状态 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "人员台账列表")
	@PostMapping("/user/get-all-list")
	public MyJsonBean<UserAllResult> getUserAllList(@RequestBody UserAllQueryParam param) {
		MyJsonBean<UserAllResult> myJsonBean = null;
		try {
			myJsonBean = commonService.getUserAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员台账列表 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出 人员台账列表")
	@PostMapping("/user/get-all-list/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody UserAllQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = commonService.getUserAllList(param);
		PageResult<UserAllResult> result = (PageResult<UserAllResult>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean list = commonService.getUserAllList(param);
			PageResult<UserAllResult> tempList = (PageResult<UserAllResult>) list.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "人员台账列表.xlsx";
		try (ExcelExport export = new ExcelExport(UserAllResultExpress.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
