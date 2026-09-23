package com.huabo.legal.controller;


import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglRegisterManagementOracle;
import com.huabo.legal.service.IntellectualPropertyService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglRegisterManagementQueryParam;
import com.huabo.legal.vo.result.TblFwglRegisterManagement;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-知识产权所有接口",description="法务管理-知识产权所有接口")
@RequestMapping(value = "/api-auth/intellectual/property")
@Slf4j
public class IntellectualPropertyController {

	@Resource
	private IntellectualPropertyService intellectualPropertyService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "登记管理列表 查询")
	@PostMapping("/getList")
	public JsonBean getTblFwglRegisterManagementList(@RequestBody TblFwglRegisterManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = intellectualPropertyService.getTblFwglRegisterManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("登记管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 登记管理列表")
	@GetMapping("/register/download-express")
	public void downloadExpressTblFwglRegisterManagement(@RequestHeader("token") String token, TblFwglRegisterManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglRegisterManagementList = intellectualPropertyService.getTblFwglRegisterManagementList(param);
		PageResult<TblFwglRegisterManagementOracle> result = (PageResult<TblFwglRegisterManagementOracle>) tblFwglRegisterManagementList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglRegisterManagementTempList = intellectualPropertyService.getTblFwglRegisterManagementList(param);
			PageResult<TblFwglRegisterManagementOracle> tempList = (PageResult<TblFwglRegisterManagementOracle>) tblFwglRegisterManagementTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "登记管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglRegisterManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "登记管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglRegisterManagement(@RequestBody @Validated TblFwglRegisterManagement param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = intellectualPropertyService.saveOrUpdateTblFwglRegisterManagement(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("登记管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "登记管理 刪除")
	@DeleteMapping("/{id}")
	public JsonBean deleteTblTblFwglRegisterManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = intellectualPropertyService.deleteTblTblFwglRegisterManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("登记管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "登记管理详情 查询")
	@GetMapping("/{id}")
	public JsonBean getTblFwglRegisterManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = intellectualPropertyService.getTblFwglRegisterManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("登记管理详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "登记管理-台账管理列表 查询")
	@PostMapping("/get-all")
	public JsonBean getTblFwglRegisterManagementAllList(@RequestBody TblFwglRegisterManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = intellectualPropertyService.getTblFwglRegisterManagementAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("登记管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 台账管理列表")
	@GetMapping("/account/download-express")
	public void downloadExpressTblFwglRegisterManagement1(@RequestHeader("token") String token, TblFwglRegisterManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglRegisterManagementAllList = intellectualPropertyService.getTblFwglRegisterManagementAllList(param);
		PageResult<TblFwglRegisterManagementOracle> result = (PageResult<TblFwglRegisterManagementOracle>) tblFwglRegisterManagementAllList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglRegisterManagementAllTempList = intellectualPropertyService.getTblFwglRegisterManagementAllList(param);
			PageResult<TblFwglRegisterManagementOracle> tempList = (PageResult<TblFwglRegisterManagementOracle>) tblFwglRegisterManagementAllTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "台账管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglRegisterManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
