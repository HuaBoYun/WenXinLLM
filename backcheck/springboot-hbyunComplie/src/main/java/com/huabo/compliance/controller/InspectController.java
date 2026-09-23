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
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblComplianceInspectImpOracle;
import com.huabo.compliance.oracle.entity.TblComplianceInspectPlanOracle;
import com.huabo.compliance.oracle.entity.TblComplianceRectificationOracle;
import com.huabo.compliance.service.InspectService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.vo.param.TblComplianceInspectImpQueryParam;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanAutoNumParam;
import com.huabo.compliance.vo.param.TblComplianceInspectPlanQueryParam;
import com.huabo.compliance.vo.param.TblComplianceRectificationQueryParam;
import com.huabo.compliance.vo.result.FileVo;
import com.huabo.compliance.vo.result.TblComplianceRectificationFileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="合规检查",description="合规检查")
@RequestMapping(value = "/api-auth/inspect")
@Slf4j
public class InspectController {

	@Resource
	private InspectService inspectService;
	
	@Resource
	private UserProvider userProvider;
	
	@Value("${application.administrators:}")
	private String administrators;

	@Operation(summary = "检查方案 列表查询")
	@PostMapping("/plan/getList")
	public JsonBean<PageResult<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlanList(@RequestBody TblComplianceInspectPlanQueryParam param,
			@RequestHeader("token") String token) {
		JsonBean<PageResult<TblComplianceInspectPlanOracle>> jsonBean = null;
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
			jsonBean = inspectService.getTblComplianceInspectPlanList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查方案 新增/更新")
	@PostMapping("/plan/saveOrUpdate")
	public JsonBean<TblComplianceInspectPlanOracle> saveOrUpdateTblComplianceInspectPlan(
			@RequestBody @Validated TblComplianceInspectPlanOracle param) {
		JsonBean<TblComplianceInspectPlanOracle> jsonBean = null;
		try {
			
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			
			jsonBean = inspectService.saveOrUpdateTblComplianceInspectPlan(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查方案 刪除")
	@DeleteMapping("/plan/{id}")
	public JsonBean<Void> deleteTblComplianceInspectPlan(@PathVariable Integer id) {
		JsonBean<Void> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.deleteTblComplianceInspectPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查方案 详情 查询")
	@GetMapping("/plan/{id}")
	public JsonBean<FileVo<TblComplianceInspectPlanOracle>> getTblComplianceInspectPlan(@PathVariable Integer id) {
		JsonBean<FileVo<TblComplianceInspectPlanOracle>> jsonBean = null;
		try {
			
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.getTblComplianceInspectPlan(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查方案-自动编码")
	@GetMapping("/plan/auto-num")
	public JsonBean<String> getTblComplianceInspectPlanAutoNum(TblComplianceInspectPlanAutoNumParam param) {
		JsonBean<String> jsonBean = null;
		try {
			
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.getTblComplianceInspectPlanAutoNum(param.getYear());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案-自动编码 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查实施 列表查询")
	@PostMapping("/imp/getList")
	public JsonBean<PageResult<TblComplianceInspectImpOracle>> getTblComplianceInspectImpList(@RequestBody TblComplianceInspectImpQueryParam param,
			@RequestHeader("token") String token) {
		JsonBean<PageResult<TblComplianceInspectImpOracle>> jsonBean = null;
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
			jsonBean = inspectService.getTblComplianceInspectImpList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查实施 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查实施 新增/更新")
	@PostMapping("/imp/saveOrUpdate")
	public JsonBean<TblComplianceInspectImpOracle> saveOrUpdateTblComplianceInspectImp(@RequestBody @Validated TblComplianceInspectImpOracle param) {
		JsonBean<TblComplianceInspectImpOracle> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.saveOrUpdateTblComplianceInspectImp(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查实施 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查实施 刪除")
	@DeleteMapping("/imp/{id}")
	public JsonBean<Void> deleteTblComplianceInspectImp(@PathVariable Integer id) {
		JsonBean<Void> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.deleteTblComplianceInspectImp(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查实施 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "检查实施 详情 查询")
	@GetMapping("/imp/{id}")
	public JsonBean<FileVo<TblComplianceInspectImpOracle>> getTblComplianceInspectImp(@PathVariable Integer id) {
		JsonBean<FileVo<TblComplianceInspectImpOracle>> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.getTblComplianceInspectImp(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查实施 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "问题整改 列表查询")
	@PostMapping("/rectification/getList")
	public JsonBean<PageResult<TblComplianceRectificationOracle>> getTblComplianceRectificationList(
			@RequestBody TblComplianceRectificationQueryParam param,
			@RequestHeader("token") String token) {
		JsonBean<PageResult<TblComplianceRectificationOracle>> jsonBean = null;
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
			jsonBean = inspectService.getTblComplianceRectificationList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("问题整改 列表查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "问题整改 新增/更新")
	@PostMapping("/rectification/saveOrUpdate")
	public JsonBean<TblComplianceRectificationOracle> saveOrUpdateTblComplianceRectification(
			@RequestBody @Validated TblComplianceRectificationOracle param) {
		JsonBean<TblComplianceRectificationOracle> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.saveOrUpdateTblComplianceRectification(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("问题整改 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "问题整改 刪除")
	@DeleteMapping("/rectification/{id}")
	public JsonBean<Void> deleteTblComplianceRectification(@PathVariable Integer id) {
		JsonBean<Void> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.deleteTblComplianceRectification(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("检查方案 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "问题整改 详情 查询")
	@GetMapping("/rectification/{id}")
	public JsonBean<TblComplianceRectificationFileVo<TblComplianceRectificationOracle>> getTblComplianceRectification(@PathVariable Integer id) {
		JsonBean<TblComplianceRectificationFileVo<TblComplianceRectificationOracle>> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.getTblComplianceRectification(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("问题整改 详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "首页-合规审查数量")
	@PostMapping("/main/compcnt")
	public JsonBean compcnt(@RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.compcnt();
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("首页-合规审查数量 ...接口 异常", e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "首页-顶部统计数量")
	@PostMapping("/main/topcnt")
	public JsonBean topcnt(@RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			jsonBean = inspectService.topcnt();
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("首页-顶部统计数量 ...接口 异常", e);
		}
		return jsonBean;
	}
	
}
