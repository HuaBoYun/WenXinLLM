package com.huabo.compliance.controller;

import javax.annotation.Resource;

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
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceReportOracle;
import com.huabo.compliance.service.ReportService;
import com.huabo.compliance.vo.param.TblComplianceReportQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="合规报告",description="合规报告")
@RequestMapping(value = "/api-auth/report")
@Slf4j
public class ReportController {

	@Resource
	private ReportService reportService;
	@Value("${application.administrators:}")
	private String administrators;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "合规报告 列表查询")
	@PostMapping("/getList")
	public JsonBean getTblComplianceReportList(@RequestBody TblComplianceReportQueryParam param, @RequestHeader("token") String token) {
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
			jsonBean = reportService.getTblComplianceReportList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规报告 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规报告 新增/更新")
	@PostMapping("/saveOrUpdate")
	public JsonBean saveOrUpdateTblComplianceReport(@RequestBody @Validated TblComplianceReportOracle param) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = reportService.saveOrUpdateTblComplianceReport(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规报告 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规报告 刪除")
	@DeleteMapping("/{id}")
	public JsonBean deleteTblComplianceReport(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = reportService.deleteTblComplianceReport(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规报告 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "合规报告 详情 查询")
	@GetMapping("/{id}")
	public JsonBean getTblComplianceReport(@PathVariable Integer id) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = reportService.getTblComplianceReport(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("合规报告 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}
}
