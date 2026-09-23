package com.huabo.legal.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.service.TblStaffOracleService;
import com.huabo.legal.service.CommonService;
import com.huabo.legal.vo.param.HeadquartersLegalQueryParam;
import com.huabo.legal.vo.result.StaffResult;
import com.vip.vjtools.vjkit.mapper.JsonMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-通用接口",description="法务管理-通用接口")
@RequestMapping(value = "/api-auth/legal/common")
@Slf4j
public class CommonController {

	@Resource
	private CommonService commonService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	@Resource
	private TblStaffOracleService staffOracleService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "查询该用户是否是总部法务人员 true/false")
	@PostMapping("/isHeadquartersLegal")
	public JsonBean isHeadquartersLegal(@RequestHeader("token") String token, @RequestBody @Validated HeadquartersLegalQueryParam param) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		Integer orgtype = loginStaff.getLinkOrg().getOrgtype();
		if (orgtype != null && (orgtype == 1 || orgtype == 2)) {
			try {
				Long staffid = loginStaff.getStaffid().longValue();
				Long orgid = loginStaff.getLinkOrg().getOrgid().longValue();
				StaffResult userInfoExam = tblStaffOracleService.getUserInfoExam(staffid, orgid, param.getRole());
				if (userInfoExam != null) {
					return ResponseFormat.retParam(200, 200, true);
				}
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("查询该用户是否是总部法务人员 ...接口 异常", e);
			}
		}
		return ResponseFormat.retParam(200, 200, false);
	}

	@Operation(summary = "测试 循环查询 原生")
	@GetMapping("/test")
	public void test() {
		List<Long> tblOrganization = staffOracleService.getTblOrganization(1000L);
		String string = JsonMapper.INSTANCE.toJson(tblOrganization);
		log.info("========================:{}", string);
	}

	@Operation(summary = "测试 递归查询 优化")
	@GetMapping("/test1")
	public void test1() {
		List<Long> tblOrganization = staffOracleService.getTblOrganizationAll(1000L);
		String string = JsonMapper.INSTANCE.toJson(tblOrganization);
		log.info("========================:{}", string);
	}
}
