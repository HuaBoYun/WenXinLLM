package com.huabo.legal.controller;

import java.util.ArrayList;
import java.util.List;

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
import com.huabo.legal.mysql.entity.TblFwglAnnualExamineMySql;
import com.huabo.legal.mysql.entity.TblFwglAnnualPlanMySql;
import com.huabo.legal.mysql.entity.TblFwglPlanManagementMySql;
import com.huabo.legal.service.PlanExamineService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.ExamineAllQueryParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineQueryParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtFileParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineScoreExtQueryParam;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtBatchAdd;
import com.huabo.legal.vo.param.TblFwglAnnualExamineTopicExtQueryParam;
import com.huabo.legal.vo.param.TblFwglAnnualPlanQueryParam;
import com.huabo.legal.vo.param.TblFwglPlanManagementQueryParam;
import com.huabo.legal.vo.result.TblFwglAnnualExamine;
import com.huabo.legal.vo.result.TblFwglAnnualPlan;
import com.huabo.legal.vo.result.TblFwglPlanManagement;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-计划考核所有接口",description="法务管理-计划考核所有接口")
@RequestMapping(value = "/api-auth/plan/examine")
@Slf4j
public class PlanExamineController {

	@Resource
	private PlanExamineService planExamineService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "规划管理列表 查询")
	@PostMapping("/plan/management/getList")
	public JsonBean getTblFwglPlanManagementList(@RequestBody TblFwglPlanManagementQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglPlanManagementList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("规划管理列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 规划管理列表")
	@GetMapping("/plan/management/download-express")
	public void downloadExpressTblFwglPlanManagement(@RequestHeader("token") String token, TblFwglPlanManagementQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglPlanManagementList = planExamineService.getTblFwglPlanManagementList(param);
		PageResult<TblFwglPlanManagementMySql> result = (PageResult<TblFwglPlanManagementMySql>) tblFwglPlanManagementList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglPlanManagementTempList = planExamineService.getTblFwglPlanManagementList(param);
			PageResult<TblFwglPlanManagementMySql> tempList = (PageResult<TblFwglPlanManagementMySql>) tblFwglPlanManagementTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "规划管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglPlanManagement.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "规划管理 新增/更新")
	@PostMapping("/plan/management/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPlanManagement(@RequestBody @Validated TblFwglPlanManagement param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglPlanManagement(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("规划管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "规划管理 刪除")
	@DeleteMapping("/plan/management/{id}")
	public JsonBean deleteTblFwglPlanManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.deleteTblFwglPlanManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("规划管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "规划管理详情 查询")
	@GetMapping("/plan/management/{id}")
	public JsonBean getTblFwglPlanManagement(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglPlanManagement(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("规划管理详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度计划列表 查询")
	@PostMapping("/annual/plan/getList")
	public JsonBean getTblFwglAnnualPlanList(@RequestBody TblFwglAnnualPlanQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualPlanList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度计划列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 年度计划列表")
	@GetMapping("/annual/plan/download-express")
	public void downloadExpressTblFwglAnnualPlan(@RequestHeader("token") String token, TblFwglAnnualPlanQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglAnnualPlanList = planExamineService.getTblFwglAnnualPlanList(param);
		PageResult<TblFwglAnnualPlanMySql> result = (PageResult<TblFwglAnnualPlanMySql>) tblFwglAnnualPlanList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglAnnualPlanTempList = planExamineService.getTblFwglAnnualPlanList(param);
			PageResult<TblFwglAnnualPlanMySql> tempList = (PageResult<TblFwglAnnualPlanMySql>) tblFwglAnnualPlanTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "年度计划列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglAnnualPlan.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "年度计划 新增/更新")
	@PostMapping("/annual/plan/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglAnnualPlan(@RequestBody @Validated TblFwglAnnualPlan param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglAnnualPlan(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度计划 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度计划 刪除")
	@DeleteMapping("/annual/plan/{id}")
	public JsonBean deleteTblFwglAnnualPlan(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.deleteTblFwglAnnualPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度计划 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度计划详情 查询")
	@GetMapping("/annual/plan/{id}")
	public JsonBean getTblFwglAnnualPlan(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度计划详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核列表 查询")
	@PostMapping("/annual/examine/getList")
	public JsonBean getTblFwglAnnualExamineList(@RequestBody TblFwglAnnualExamineQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualExamineList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 年度考核列表")
	@GetMapping("/annual/examine/download-express")
	public void downloadExpressTblFwglAnnualExamine(@RequestHeader("token") String token, TblFwglAnnualExamineQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglAnnualExamineList = planExamineService.getTblFwglAnnualExamineList(param);
		PageResult<TblFwglAnnualExamineMySql> result = (PageResult<TblFwglAnnualExamineMySql>) tblFwglAnnualExamineList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglAnnualExamineTempList = planExamineService.getTblFwglAnnualExamineList(param);
			PageResult<TblFwglAnnualExamineMySql> tempList = (PageResult<TblFwglAnnualExamineMySql>) tblFwglAnnualExamineTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "年度考核列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglAnnualExamine.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "年度考核 新增/更新")
	@PostMapping("/annual/examine/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglAnnualExamine(@RequestBody @Validated TblFwglAnnualExamine param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglAnnualExamine(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核 刪除")
	@DeleteMapping("/annual/examine/{id}")
	public JsonBean deleteTblFwglAnnualExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.deleteTblFwglAnnualExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核详情 查询")
	@GetMapping("/annual/examine/{id}")
	public JsonBean getTblFwglAnnualExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核-考核题目列表 查询")
	@PostMapping("/annual/examine/topic/getList")
	@Deprecated
	public JsonBean getTblFwglAnnualExamineTopicExtList(@RequestBody TblFwglAnnualExamineTopicExtQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualExamineTopicExtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核题目列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核-考核题目 新增/更新")
	@PostMapping("/annual/examine/topic/saveOrUpdate")
	@Deprecated
	public JsonBean saveOrUpdateTblFwglAnnualExamineTopicExt(@RequestBody TblFwglAnnualExamineTopicExtBatchAdd param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglAnnualExamineTopicExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核题目 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}


	@Operation(summary = "年度考核-考核评分列表 查询")
	@PostMapping("/annual/examine/score/getList")
	public JsonBean getTblFwglAnnualExamineScoreExtList(@RequestBody TblFwglAnnualExamineScoreExtQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualExamineScoreExtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核评分列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核-考核评分 批量新增/更新")
	@PostMapping("/annual/examine/score/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglAnnualExamineScoreExt(@RequestBody List<TblFwglAnnualExamineScoreExtParam> param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglAnnualExamineScoreExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核评分 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核-考核评分-附件列表 查询 {id}为评分主键 annualExamineScoreExtId")
	@GetMapping("/annual/examine/score/ext/{id}")
	public JsonBean getTblFwglAnnualExamineScoreExtFile(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getTblFwglAnnualExamineScoreExtFile(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核评分-附件列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "年度考核-考核评分-附件-确定按钮")
	@PostMapping("/annual/examine/score/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglAnnualExamineScoreExtFile(@RequestBody TblFwglAnnualExamineScoreExtFileParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.saveOrUpdateTblFwglAnnualExamineScoreExtFile(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("年度考核-考核评分-附件-确定按钮 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核台账列表 查询")
	@PostMapping("/examine/get-all")
	public JsonBean getExamineAllList(@RequestBody ExamineAllQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = planExamineService.getExamineAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核台账列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}


}
