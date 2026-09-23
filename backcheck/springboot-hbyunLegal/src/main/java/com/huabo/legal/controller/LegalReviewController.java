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
import com.huabo.legal.oracle.entity.TblFwglInstitutionAuditOracle;
import com.huabo.legal.service.LegalReviewService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglInstitutionAuditQueryParam;
import com.huabo.legal.vo.result.TblFwglInstitutionAudit;
import com.huabo.legal.vo.result.TblFwglInstitutionAuditExt;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-法律审核所有接口",description="法务管理-法律审核所有接口")
@RequestMapping(value = "/api-auth/legal/review")
@Slf4j
public class LegalReviewController {

	@Resource
	private LegalReviewService legalReviewService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "制度审核/经营事项审核列表 查询")
	@PostMapping("/institution/audit/getList")
	public JsonBean getTblFwglInstitutionAuditList(@RequestBody @Validated TblFwglInstitutionAuditQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.getTblFwglInstitutionAuditList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核/经营事项审核列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "制度审核/经营事项审核 新增/更新")
	@PostMapping("/institution/audit/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglInstitutionAudit(@RequestBody @Validated TblFwglInstitutionAudit param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.saveOrUpdateTblFwglInstitutionAudit(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核/经营事项审核 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "制度审核/经营事项审核 刪除")
	@DeleteMapping("/institution/audit/{id}")
	public JsonBean deleteTblFwglInstitutionAudit(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.deleteTblFwglInstitutionAudit(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核/经营事项审核 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "制度审核/经营事项审核详情 查询")
	@GetMapping("/institution/audit/{id}")
	public JsonBean getTblFwglInstitutionAudit(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.getTblFwglInstitutionAudit(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核/经营事项审核详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 制度审核/经营事项审核列表")
	@GetMapping("/institution/audit/download-express")
	public void downloadExpressTblFwglInstitutionAudit(@RequestHeader("token") String token, TblFwglInstitutionAuditQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglInstitutionAuditList = legalReviewService.getTblFwglInstitutionAuditList(param);
		PageResult<TblFwglInstitutionAuditOracle> result = (PageResult<TblFwglInstitutionAuditOracle>) tblFwglInstitutionAuditList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglInstitutionAuditTempList = legalReviewService.getTblFwglInstitutionAuditList(param);
			PageResult<TblFwglInstitutionAuditOracle> tempList = (PageResult<TblFwglInstitutionAuditOracle>) tblFwglInstitutionAuditTempList
					.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "制度审核/经营事项审核列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglInstitutionAudit.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "审核台账列表 查询")
	@PostMapping("/audit/get-all")
	public JsonBean getAllList(@RequestBody TblFwglInstitutionAuditQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.getAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("审核台账列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 审核台账列表")
	@GetMapping("/audit/download-express")
	public void downloadExpressTblFwglInstitutionAudit1(@RequestHeader("token") String token, TblFwglInstitutionAuditQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean allList = legalReviewService.getAllList(param);
		PageResult<TblFwglInstitutionAuditOracle> result = (PageResult<TblFwglInstitutionAuditOracle>) allList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean allTempList = legalReviewService.getAllList(param);
			PageResult<TblFwglInstitutionAuditOracle> tempList = (PageResult<TblFwglInstitutionAuditOracle>) allTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "审核台账列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglInstitutionAudit.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "制度审核-制度 新增/更新")
	@PostMapping("/institution/audi/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglInstitutionAuditExt(@RequestBody @Validated TblFwglInstitutionAuditExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.saveOrUpdateTblFwglInstitutionAuditExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核-制度 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "制度审核-制度 刪除")
	@DeleteMapping("/institution/audit/ext/{id}")
	public JsonBean deleteTblFwglInstitutionAuditExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.deleteTblFwglInstitutionAuditExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核-制度 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "制度审核-制度详情 查询")
	@GetMapping("/institution/audit/ext/{id}")
	public JsonBean getTblFwglInstitutionAuditExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalReviewService.getTblFwglInstitutionAuditExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("制度审核-制度详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

}
