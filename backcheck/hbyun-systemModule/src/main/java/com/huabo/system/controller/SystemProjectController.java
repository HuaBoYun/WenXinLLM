package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemHomeAuthorizationOracle;
import com.huabo.system.entity.TblSystemHomePageOracle;
import com.huabo.system.entity.TblSystemLoginPageOracle;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.business.SystemProjectService;
import com.huabo.system.utils.JsonBean;
import com.huabo.system.utils.TokenUtils;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationParam;
import com.huabo.system.vo.param.TblSystemHomeAuthorizationQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageQueryParam;
import com.huabo.system.vo.param.TblSystemHomePageStateParam;
import com.huabo.system.vo.param.TblSystemLoginPageQueryParam;
import com.huabo.system.vo.param.TblSystemLoginPageStateParam;
import com.huabo.system.vo.param.TblSystemProjectAuthParam;
import com.huabo.system.vo.param.TblSystemProjectAuthQueryParam;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;
import com.huabo.system.vo.param.UniqueIdentificationParam;
import com.huabo.system.vo.param.UserInfoParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统项目模块与登录页/首页配置控制器
 * <p>提供系统项目模块管理、登录页配置、首页配置、授权管理等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "系统项目模块与登录页/首页配置", description = "系统项目模块与登录页/首页配置")
public class SystemProjectController {

	@Resource
	private SystemProjectService systemProjectService;

	@Resource
    private TblStaffService tblStaffService;

	@Resource
    private UserProvider userProvider;

	@PostMapping(value = "/project/module/getList")
	@Operation(summary="系统项目模块列表")
	public JsonBean getTblSystemProjectList(@RequestHeader("token") String token, @RequestBody TblSystemProjectQueryParam param) {
		JsonBean jsonBean;
		try {
			log.info("系统项目模块列表接口");
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.getTblSystemProjectList(param);
		} catch (Exception e) {
			log.error("系统项目模块列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/project/module/saveOrUpdate")
	@Operation(summary="系统项目模块-新增或修改")
	public JsonBean saveOrUpdateTblSystemProject(@RequestHeader("token") String token, @RequestBody @Validated TblSystemProjectOracle param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff;
			if (StringUtils.isNotBlank(param.getToken())) {
				loginStaff = userProvider.get(param.getToken());
			} else {
				loginStaff = userProvider.get();
			}
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.saveOrUpdateTblSystemProject(param);
		} catch (Exception e) {
			log.error("系统项目模块-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/project/new/module/saveOrUpdate")
	@Operation(summary="系统项目模块-新增或修改")
	public JsonBean saveOrUpdateTblSystemProjectNew(@RequestBody @Validated TblSystemProjectOracle param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff;
			if (StringUtils.isNotBlank(param.getToken())) {
				loginStaff = userProvider.get(param.getToken());
			} else {
				loginStaff = userProvider.get();
			}
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.saveOrUpdateTblSystemProject(param);
		} catch (Exception e) {
			log.error("系统项目模块-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/project/new/module/isUniqueIdentification")
	@Operation(summary="系统项目模块-查询编码流程平台项目编码是否存在")
	public JsonBean isOtherNo(@RequestBody UniqueIdentificationParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff;
			if (StringUtils.isNotBlank(param.getToken())) {
				loginStaff = userProvider.get(param.getToken());
			} else {
				loginStaff = userProvider.get();
			}
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.isUniqueIdentification(param.getUniqueIdentification());
		} catch (Exception e) {
			log.error("系统项目模块-查询编码流程平台项目编码是否存在 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/project/module/{id}")
	@Operation(summary="系统项目模块-信息 {id}为主键id")
	public JsonBean getTblSystemProject(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.getTblSystemProject(id);
		} catch (Exception e) {
			log.error("系统项目模块-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@DeleteMapping("/project/module/{id}")
	@Operation(summary="系统项目模块-信息删除 {id}为主键id")
	public JsonBean deleteTblSystemProject(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.deleteTblSystemProject(id);
		} catch (Exception e) {
			log.error("系统项目模块-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/login/page/getList")
	@Operation(summary="登录页配置列表")
	public JsonBean getTblSystemLoginPageList(@RequestHeader("token") String token, @RequestBody TblSystemLoginPageQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.getTblSystemLoginPageList(param);
		} catch (Exception e) {
			log.error("登录页配置列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/login/page/saveOrUpdate")
	@Operation(summary="登录页配置-新增或修改")
	public JsonBean saveOrUpdateTblSystemLoginPage(@RequestHeader("token") String token, @RequestBody TblSystemLoginPageOracle param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.saveOrUpdateTblSystemLoginPage(param);
		} catch (Exception e) {
			log.error("登录页配置-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/login/page/{id}")
	@Operation(summary="登录页配置-信息 {id}为主键id")
	public JsonBean getTblSystemLoginPage(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.getTblSystemLoginPage(id);
		} catch (Exception e) {
			log.error("登录页配置-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@DeleteMapping("/login/page/{id}")
	@Operation(summary="登录页配置-信息删除 {id}为主键id")
	public JsonBean deleteTblSystemLoginPage(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.deleteTblSystemLoginPage(id);
		} catch (Exception e) {
			log.error("登录页配置-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/login/page/updateState")
	@Operation(summary="登录页配置-状态变更")
	public JsonBean updateStateTblSystemLoginPage(@RequestHeader("token") String token, @RequestBody @Validated TblSystemLoginPageStateParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.updateStateTblSystemLoginPage(param);
		} catch (Exception e) {
			log.error("登录页配置-状态变更 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/login/page/info/{belongGroup}")
	@Operation(summary="登录页配置-登录页信息")
	public JsonBean getTblSystemLoginPageInfo(@PathVariable BigDecimal belongGroup) {
		JsonBean jsonBean;
		try {
			jsonBean = systemProjectService.getTblSystemLoginPageInfo(belongGroup);
		} catch (Exception e) {
			log.error("登录页配置-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/home/page/getList")
	@Operation(summary="首页配置列表")
	public JsonBean getTblSystemHomePageList(@RequestHeader("token") String token, @RequestBody TblSystemHomePageQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.getTblSystemHomePageList(param);
		} catch (Exception e) {
			log.error("首页配置列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/home/page/saveOrUpdate")
	@Operation(summary="首页配置-新增或修改")
	public JsonBean saveOrUpdateTblSystemHomePage(@RequestHeader("token") String token, @RequestBody TblSystemHomePageOracle param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.saveOrUpdateTblSystemHomePage(param);
		} catch (Exception e) {
			log.error("首页配置-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/home/page/{id}")
	@Operation(summary="首页配置-信息 {id}为主键id")
	public JsonBean getTblSystemHomePage(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.getTblSystemHomePage(id);
		} catch (Exception e) {
			log.error("首页配置-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@DeleteMapping("/home/page/{id}")
	@Operation(summary="首页配置-信息删除 {id}为主键id")
	public JsonBean deleteTblSystemHomePage(@RequestHeader("token") String token, @PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.deleteTblSystemHomePage(id);
		} catch (Exception e) {
			log.error("首页配置-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/home/page/updateState")
	@Operation(summary="首页配置-状态变更")
	public JsonBean updateStateTblSystemHomePage(@RequestHeader("token") String token, @RequestBody @Validated TblSystemHomePageStateParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.updateStateTblSystemHomePage(param);
		} catch (Exception e) {
			log.error("首页配置-状态变更 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/project/auth/getList")
	@Operation(summary="系统项目授权列表")
	public JsonBean getTblSystemProjectAuthList(@RequestHeader("token") String token, @RequestBody @Validated TblSystemProjectAuthQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemProjectService.getTblSystemProjectAuthList(param);
		} catch (Exception e) {
			log.error("系统项目授权列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/project/auth/saveOrUpdate")
	@Operation(summary="系统项目授权-新增")
	public JsonBean saveOrUpdateTblSystemProjectAuth(@RequestHeader("token") String token, @RequestBody TblSystemProjectAuthParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.getList().forEach(x -> {
				x.setProjectId(param.getProjectId());
				x.setCreator(userInfo.getCreator());
				x.setWorkUnit(userInfo.getWorkUnit());
				x.setBelongGroup(userInfo.getBelongGroup());
			});
			jsonBean = systemProjectService.saveOrUpdateTblSystemProjectAuth(param);
		} catch (Exception e) {
			log.error("系统项目授权-新增 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/home/page/auth/getList")
	@Operation(summary="首页配置-授权列表")
	public JsonBean<List<TblSystemHomeAuthorizationOracle>> getTblSystemHomePageAuthList(@RequestHeader("token") String token,
			@RequestBody TblSystemHomeAuthorizationQueryParam param) {
		JsonBean<List<TblSystemHomeAuthorizationOracle>> jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.getTblSystemHomePageAuthList(param);
		} catch (Exception e) {
			log.error("首页配置-授权列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/home/page/auth/saveOrUpdate")
	@Operation(summary="首页配置-授权-新增")
	public JsonBean saveOrUpdateTblSystemHomePageAuth(@RequestHeader("token") String token, @RequestBody TblSystemHomeAuthorizationParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.saveOrUpdateTblSystemHomePageAuth(param);
		} catch (Exception e) {
			log.error("首页配置-授权-新增 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/home/page/auth/company")
	@Operation(summary="首页配置-公司")
	public JsonBean<TblSystemHomePageOracle> getTblSystemHomePageAuthCompany(
			HttpServletRequest request,@RequestHeader("token") String token) {
		JsonBean<TblSystemHomePageOracle> jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemProjectService.getTblSystemHomePageAuthCompany(userInfo.getBelongGroup());
//			String ip = IpUtil.getIpAddr(request);
//			System.out.println("===========用户访问IP地址："+ip);
//			UserLoginLog log=new UserLoginLog(IdUtil.getSnowflakeNextId(),new Date(),null,
//					loginStaff.getStaffid().longValue(),loginStaff.getUsername(),ip,"","用户登录成功","0",new Date(),loginStaff.getRealname());
//			tblStaffService.addLoginLog(log);

		} catch (Exception e) {
			log.error("首页配置-公司 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}
}
