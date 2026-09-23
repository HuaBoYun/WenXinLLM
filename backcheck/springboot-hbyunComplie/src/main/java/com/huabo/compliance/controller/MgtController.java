package com.huabo.compliance.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
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
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceDtyOracle;
import com.huabo.compliance.oracle.entity.TblComplianceImOracle;
import com.huabo.compliance.oracle.entity.TblComplianceManualMgtOracle;
import com.huabo.compliance.oracle.entity.TblCompliancePlanMgtOracle;
import com.huabo.compliance.oracle.entity.TblComplianceRiskOracle;
import com.huabo.compliance.service.MgtService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.util.excel.ExcelExport;
import com.huabo.compliance.vo.excel.TblComplianceDtyExcel;
import com.huabo.compliance.vo.excel.TblComplianceImExcel;
import com.huabo.compliance.vo.param.TblComplianceDtyQueryParam;
import com.huabo.compliance.vo.param.TblComplianceImQueryParam;
import com.huabo.compliance.vo.param.TblComplianceManualMgtQueryParam;
import com.huabo.compliance.vo.param.TblCompliancePlanMgtQueryParam;
import com.huabo.compliance.vo.param.TblComplianceRiskQueryParam;
import com.huabo.compliance.vo.result.TblComplianceRiskResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="合规管理",description="合规管理")
@RequestMapping(value = "/api-auth/im")
@Slf4j
public class MgtController {

	@Resource
	private MgtService mgtService;
	
	@Resource
	private UserProvider userProvider;

	@Value("${application.administrators:}")
	private String administrators;

	@OperationLog(
			success = "查询计划管理",
			busType = "合规管理",
			fail = "查询计划管理失败",
			operationType = OperationType.SELECT,
			subType = "计划管理"
	)
	@Operation(summary = "计划管理 列表查询")
	@PostMapping("/conference/getList")
	public JsonBean getTblCompliancePlanMgtList(@RequestBody TblCompliancePlanMgtQueryParam param, @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			if (JudgeRoleRight.judgeRoleRight(administrators, loginStaff.getRoleNames())) {
				param.setAuthorityType(1);
			} else {
				param.setAuthorityType(0);
			}
			jsonBean = mgtService.getTblCompliancePlanMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("计划管理 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@OperationLog(
			success = "计划【{{#param.planName}}】新增成功",
			busType = "合规管理",
			fail = "计划【{{#param.planName}}】新增失败",
			operationType = OperationType.UPDATE,
			subType = "计划管理"
	)
	@Operation(summary = "计划管理 新增/更新")
	@PostMapping("/conference/saveOrUpdate")
	public JsonBean saveOrUpdateTblCompliancePlanMgt(@RequestBody @Validated TblCompliancePlanMgtOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.saveOrUpdateTblCompliancePlanMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("计划管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "计划管理 刪除")
	@DeleteMapping("/conference/{id}")
	public JsonBean deleteTblCompliancePlanMgt(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.deleteTblCompliancePlanMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("计划管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "计划管理 详情 查询")
	@GetMapping("/conference/{id}")
	public JsonBean getTblCompliancePlanMgt(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.getTblCompliancePlanMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("计划管理 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "重点岗位合规责任 列表查询")
	@PostMapping("/dty/getList")
	public JsonBean getTblComplianceDtyList(@RequestBody TblComplianceDtyQueryParam param, @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			if (JudgeRoleRight.judgeRoleRight(administrators, loginStaff.getRoleNames())) {
				param.setAuthorityType(1);
			} else {
				param.setAuthorityType(0);
			}
			jsonBean = mgtService.getTblComplianceDtyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("重点岗位合规责任 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 重点岗位合规责任")
	@GetMapping("/dty/download-express")
	public void downloadExpressTblComplianceIm(@RequestHeader("token") String token, TblComplianceDtyQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().intValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean complianceImList = mgtService.getTblComplianceDtyList(param);
		PageResult<TblComplianceDtyOracle> result = (PageResult<TblComplianceDtyOracle>) complianceImList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean complianceImTempList = mgtService.getTblComplianceDtyList(param);
			PageResult<TblComplianceDtyOracle> tempList = (PageResult<TblComplianceDtyOracle>) complianceImTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "重点岗位合规责任.xlsx";
		try (ExcelExport export = new ExcelExport(TblComplianceDtyExcel.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "重点岗位合规责任 新增/更新")
	@PostMapping("/dty/saveOrUpdate")
	public JsonBean saveOrUpdateTblComplianceDty(@RequestBody @Validated TblComplianceDtyOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.saveOrUpdateTblComplianceDty(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("重点岗位合规责任 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "重点岗位合规责任 刪除")
	@DeleteMapping("/dty/{id}")
	public JsonBean deleteTblComplianceDty(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.deleteTblComplianceDty(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("重点岗位合规责任 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "重点岗位合规责任 详情 查询")
	@GetMapping("/dty/{id}")
	public JsonBean getTblComplianceDty(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.getTblComplianceDty(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("重点岗位合规责任 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规管理员信息管理 列表查询")
	@PostMapping("/info/getList")
	public JsonBean getTblComplianceImList(@RequestBody TblComplianceImQueryParam param, @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			if (JudgeRoleRight.judgeRoleRight(administrators, loginStaff.getRoleNames())) {
				param.setAuthorityType(1);
			} else {
				param.setAuthorityType(0);
			}
			jsonBean = mgtService.getTblComplianceImList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规管理员信息管理 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 合规管理员信息管理")
	@GetMapping("/info/download-express")
	public void downloadExpressTblComplianceIm(@RequestHeader("token") String token, TblComplianceImQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().intValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean complianceImList = mgtService.getTblComplianceImList(param);
		PageResult<TblComplianceImOracle> result = (PageResult<TblComplianceImOracle>) complianceImList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean complianceImTempList = mgtService.getTblComplianceImList(param);
			PageResult<TblComplianceImOracle> tempList = (PageResult<TblComplianceImOracle>) complianceImTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "合规管理员信息管理.xlsx";
		try (ExcelExport export = new ExcelExport(TblComplianceImExcel.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "合规管理员信息管理 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public JsonBean saveOrUpdateTblComplianceIm(@RequestBody @Validated TblComplianceImOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.saveOrUpdateTblComplianceIm(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规管理员信息管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规管理员信息管理 刪除")
	@DeleteMapping("/info/{id}")
	public JsonBean deleteTblComplianceIm(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.deleteTblComplianceIm(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规管理员信息管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规管理员信息管理 详情 查询")
	@GetMapping("/info/{id}")
	public JsonBean getTblComplianceIm(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.getTblComplianceIm(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规管理员信息管理 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规手册管理 列表查询")
	@PostMapping("/manual/getList")
	public JsonBean getTblComplianceManualMgtList(@RequestBody TblComplianceManualMgtQueryParam param, @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			if (JudgeRoleRight.judgeRoleRight(administrators, loginStaff.getRoleNames())) {
				param.setAuthorityType(1);
			} else {
				param.setAuthorityType(0);
			}
			jsonBean = mgtService.getTblComplianceManualMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规手册管理 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规手册管理 新增/更新")
	@PostMapping("/manual/saveOrUpdate")
	public JsonBean saveOrUpdateTblComplianceManualMgt(@RequestBody @Validated TblComplianceManualMgtOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.saveOrUpdateTblComplianceManualMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规手册管理 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规手册管理 刪除")
	@DeleteMapping("/manual/{id}")
	public JsonBean deleteTblComplianceManualMgt(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.deleteTblComplianceManualMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规手册管理 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规手册管理 详情 查询")
	@GetMapping("/manual/{id}")
	public JsonBean getTblComplianceManualMgt(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.getTblComplianceManualMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规手册管理 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规风险 列表查询")
	@PostMapping("/risk/getList")
	public JsonBean<PageResult<TblComplianceRiskOracle>> getTblComplianceRiskList(@RequestBody TblComplianceRiskQueryParam param,
			@RequestHeader("token") String token) {
		JsonBean<PageResult<TblComplianceRiskOracle>> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			if (JudgeRoleRight.judgeRoleRight(administrators, loginStaff.getRoleNames())) {
				param.setAuthorityType(1);
			} else {
				param.setAuthorityType(0);
			}
			jsonBean = mgtService.getTblComplianceRiskList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规风险 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规风险 新增/更新")
	@PostMapping("/risk/saveOrUpdate")
	public JsonBean<TblComplianceRiskOracle> saveOrUpdateTblComplianceRisk(@RequestBody @Validated TblComplianceRiskOracle param) {
		JsonBean<TblComplianceRiskOracle> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.saveOrUpdateTblComplianceRisk(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规风险 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规风险 刪除")
	@DeleteMapping("/risk/{id}")
	public JsonBean deleteTblComplianceRisk(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			jsonBean = mgtService.deleteTblComplianceRisk(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规风险 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规风险 详情查询")
	@GetMapping("/risk/{id}")
	public JsonBean<TblComplianceRiskResult> getTblComplianceRisk(@PathVariable Integer id) {
		JsonBean<TblComplianceRiskResult> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = mgtService.getTblComplianceRisk(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规风险 详情查询  ...接口 异常", e);
		}
		return jsonBean;
	}
}
