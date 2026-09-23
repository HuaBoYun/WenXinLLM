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
import com.huabo.legal.mysql.entity.TblFwglPracticeActivityMySql;
import com.huabo.legal.mysql.entity.TblFwglPracticeExamineMySql;
import com.huabo.legal.oracle.entity.TblFwglPracticeApplyOracle;
import com.huabo.legal.service.FirmLawyerService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglPracticeActivityQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeApplyQueryParam;
import com.huabo.legal.vo.param.TblFwglPracticeExamineQueryParam;
import com.huabo.legal.vo.result.TblFwglPracticeActivity;
import com.huabo.legal.vo.result.TblFwglPracticeApply;
import com.huabo.legal.vo.result.TblFwglPracticeApplyExt;
import com.huabo.legal.vo.result.TblFwglPracticeExamine;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-公司律师所有接口",description="法务管理-公司律师所有接口")
@RequestMapping(value = "/api-auth/firm/lawyer")
@Slf4j
public class FirmLawyerController {

	@Resource
	private FirmLawyerService firmLawyerService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "执业申请列表 查询")
	@PostMapping("/practice/apply/getList")
	public JsonBean getTblFwglPracticeApplyList(@RequestBody TblFwglPracticeApplyQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeApplyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 执业申请列表")
	@GetMapping("/practice/apply/download-express")
	public void downloadExpressTblFwglPracticeApply(@RequestHeader("token") String token, TblFwglPracticeApplyQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPracticeApplyList = firmLawyerService.getTblFwglPracticeApplyList(param);
		PageResult<TblFwglPracticeApplyOracle> result = (PageResult<TblFwglPracticeApplyOracle>) tblFwglPracticeApplyList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPracticeApplyTempList = firmLawyerService.getTblFwglPracticeApplyList(param);
			PageResult<TblFwglPracticeApplyOracle> tempList = (PageResult<TblFwglPracticeApplyOracle>) tblFwglPracticeApplyTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "执业申请列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPracticeApply.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "执业申请 新增/更新")
	@PostMapping("/practice/apply/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPracticeApply(@RequestBody @Validated TblFwglPracticeApply param) throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.saveOrUpdateTblFwglPracticeApply(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业申请 刪除")
	@DeleteMapping("/practice/apply/{id}")
	public JsonBean deleteTblFwglPracticeApply(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.deleteTblFwglPracticeApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业申请详情 查询")
	@GetMapping("/practice/apply/{id}")
	public JsonBean getTblFwglPracticeApply(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业申请-简历 新增/更新")
	@PostMapping("/practice/apply/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPracticeApplyExt(@RequestBody @Validated TblFwglPracticeApplyExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.saveOrUpdateTblFwglPracticeApplyExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请-简历 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业申请-简历 刪除")
	@DeleteMapping("/practice/apply/ext/{id}")
	public JsonBean deleteTblFwglPracticeApplyExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.deleteTblFwglPracticeApplyExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请-简历 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业申请-简历详情 查询")
	@GetMapping("/practice/apply/ext/{id}")
	public JsonBean getTblFwglPracticeApplyExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeApplyExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业申请-简历详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "人员台账列表 查询")
	@PostMapping("/personnel/get-all")
	public JsonBean getPersonnelAllList(@RequestBody TblFwglPracticeApplyQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getPersonnelAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员台账列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "人员台账详情 查询")
	@GetMapping("/personnel/{id}")
	public JsonBean getPersonnel(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getPersonnel(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员台账详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业活动-人员详情 查询 {staffId}为人员ID")
	@GetMapping("/personnel/ext/{staffId}")
	public JsonBean getPersonnelExt(@PathVariable Long staffId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getPersonnelExt(staffId);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业活动-人员详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 人员台账列表")
	@GetMapping("/personnel/download-express")
	public void downloadExpressPractice(@RequestHeader("token") String token, TblFwglPracticeApplyQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPracticeApplyList = firmLawyerService.getTblFwglPracticeApplyList(param);
		PageResult<TblFwglPracticeApply> result = (PageResult<TblFwglPracticeApply>) tblFwglPracticeApplyList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPracticeApplyTempList = firmLawyerService.getTblFwglPracticeApplyList(param);
			PageResult<TblFwglPracticeApply> tempList = (PageResult<TblFwglPracticeApply>) tblFwglPracticeApplyTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "人员台账列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPracticeApply.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "执业活动列表 查询")
	@PostMapping("/practice/activity/getList")
	public JsonBean getTblFwglPracticeActivityList(@RequestBody TblFwglPracticeActivityQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeActivityList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业活动列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 执业活动列表")//download-express
	@GetMapping("/practice/download-expressA")
	public void downloadExpressTblFwglPracticeActivity(@RequestHeader("token") String token, TblFwglPracticeActivityQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPracticeActivityList = firmLawyerService.getTblFwglPracticeActivityList(param);
		PageResult<TblFwglPracticeActivityMySql> result = (PageResult<TblFwglPracticeActivityMySql>) tblFwglPracticeActivityList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPracticeActivityTempList = firmLawyerService.getTblFwglPracticeActivityList(param);
			PageResult<TblFwglPracticeActivityMySql> tempList = (PageResult<TblFwglPracticeActivityMySql>) tblFwglPracticeActivityTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "执业活动列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPracticeActivity.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "执业活动 新增/更新")
	@PostMapping("/practice/activity/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPracticeActivity(@RequestBody @Validated TblFwglPracticeActivity param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.saveOrUpdateTblFwglPracticeActivity(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业活动 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业活动 刪除")
	@DeleteMapping("/practice/activity/{id}")
	public JsonBean deleteTblFwglPracticeActivity(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.deleteTblFwglPracticeActivity(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业活动 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业活动详情 查询")
	@GetMapping("/practice/activity/{id}")
	public JsonBean getTblFwglPracticeActivity(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeActivity(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业活动详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业考核列表 查询")
	@PostMapping("/practice/examine/getList")
	public JsonBean getTblFwglPracticeExamineList(@RequestBody TblFwglPracticeExamineQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeExamineList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业考核列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业考核 新增/更新")
	@PostMapping("/practice/examine/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPracticeExamine(@RequestBody @Validated TblFwglPracticeExamine param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.saveOrUpdateTblFwglPracticeExamine(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业考核 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业考核 刪除")
	@DeleteMapping("/practice/examine/{id}")
	public JsonBean deleteTblFwglPracticeExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.deleteTblFwglPracticeExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业考核 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "执业考核详情 查询")
	@GetMapping("/practice/examine/{id}")
	public JsonBean getTblFwglPracticeExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = firmLawyerService.getTblFwglPracticeExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("执业考核详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 执业考核列表")//download-express
	@GetMapping("/practice/examine/download-expressbb")
	public void downloadExpressTblFwglPracticeExamine(@RequestHeader("token") String token, TblFwglPracticeExamineQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPracticeExamineList = firmLawyerService.getTblFwglPracticeExamineList(param);
		PageResult<TblFwglPracticeExamineMySql> result = (PageResult<TblFwglPracticeExamineMySql>) tblFwglPracticeExamineList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPracticeExamineTempList = firmLawyerService.getTblFwglPracticeExamineList(param);
			PageResult<TblFwglPracticeExamineMySql> tempList = (PageResult<TblFwglPracticeExamineMySql>) tblFwglPracticeExamineTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "执业考核列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPracticeExamine.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
