package com.huabo.system.controller;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.vo.result.StaffResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 用户相关接口控制器
 * <p>提供用户活跃度、功能使用统计等用户画像数据接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
@Slf4j
public class UserController {

	@Value("${application.admin-name}")
	private String adminName;
	@Resource
	private TblStaffService tblStaffService;

	@Resource
    private UserProvider userProvider;

	@GetMapping("/isAdmin")
	@Operation(summary="判断用户是否是上帝管理员")
	public JsonBean getIsAdminUser(@RequestHeader("token") String token) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (StringUtils.isNotBlank(loginStaff.getRealname()) && StringUtils.equals(loginStaff.getRealname(), adminName)) {
				return ResponseFormat.retParam(200, 200, true);
			}
		} catch (Exception e) {
			log.error("登录页配置-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return ResponseFormat.retParam(200, 200, false);
	}

	@GetMapping("/is-authorization-personnel")
	@Operation(summary="判断用户是否是授权角色")
	public JsonBean isAuthorizationPersonnel(@RequestHeader("token") String token) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			StaffResult staffResult = tblStaffService
					.getUserInfoExam(loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid(), "授权编辑角色");
			if (staffResult != null) {
				return ResponseFormat.retParam(200, 200, true);
			}
		} catch (Exception e) {
			log.error("判断用户是否是授权角色 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return ResponseFormat.retParam(200, 200, false);
	}

	@GetMapping("/is-home-page-auth")
	@Operation(summary="判断用户是否是首页配置角色")
	public JsonBean isHomePageAuth(@RequestHeader("token") String token) {
		try {
			TblStaffUtil loginStaff = userProvider.get();
			StaffResult staffResult = tblStaffService
					.getUserInfoExam(loginStaff.getStaffid(), loginStaff.getLinkOrg().getOrgid(), "首页配置角色");
			if (staffResult != null) {
				return ResponseFormat.retParam(200, 200, true);
			}
		} catch (Exception e) {
			log.error("判断用户是否是首页配置角色 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return ResponseFormat.retParam(200, 200, false);
	}
}
