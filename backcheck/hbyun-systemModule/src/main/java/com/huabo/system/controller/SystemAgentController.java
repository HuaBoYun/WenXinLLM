package com.huabo.system.controller;

import javax.annotation.Resource;

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
import com.huabo.system.entity.SystemAgentDialogue;
import com.huabo.system.entity.SystemAgentInfo;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.business.SystemAgentService;
import com.huabo.system.utils.MyJsonBean;
import com.huabo.system.utils.TokenUtils;
import com.huabo.system.vo.param.SystemAgentDialogueQueryParam;
import com.huabo.system.vo.param.SystemAgentInfoQueryParam;
import com.huabo.system.vo.param.SystemAgentModuleParam;
import com.huabo.system.vo.param.UpdateSystemAgentHttpParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedModuleParam;
import com.huabo.system.vo.param.UpdateSystemAgentIssuedPermissionParam;
import com.huabo.system.vo.param.UserInfoParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 智能体设置控制器
 * <p>提供系统智能体的配置、模块管理、对话记录等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "设置智能体", description = "设置智能体")
public class SystemAgentController {

	@Resource
	private SystemAgentService systemAgentService;

	@Resource
	private UserProvider userProvider;

	@PostMapping(value = "/agent/getList")
	@Operation(summary="智能体信息-列表")
	public MyJsonBean<SystemAgentInfo> getSystemAgentList(@RequestHeader("token") String token, @RequestBody SystemAgentInfoQueryParam param) {
		MyJsonBean<SystemAgentInfo> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			myJsonBean = systemAgentService.getSystemAgentList(param);
		} catch (Exception e) {
			log.error("智能体信息-列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent/saveOrUpdate")
	@Operation(summary="智能体信息-新增或修改")
	public MyJsonBean<SystemAgentInfo> saveOrUpdateSystemAgent(@RequestHeader("token") String token, @RequestBody SystemAgentInfo param) {
		MyJsonBean<SystemAgentInfo> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.setCreator(userInfo.getCreator().longValue());
			param.setWorkUnit(userInfo.getWorkUnit().longValue());
			param.setBelongGroup(userInfo.getBelongGroup().longValue());
			myJsonBean = systemAgentService.saveOrUpdateSystemAgent(param);
		} catch (Exception e) {
			log.error("智能体信息-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@GetMapping("/agent/{id}")
	@Operation(summary="智能体信息-信息 {id}为主键id")
	public MyJsonBean<SystemAgentInfo> getSystemAgent(@RequestHeader("token") String token, @PathVariable Long id) {
		MyJsonBean<SystemAgentInfo> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.getSystemAgent(id);
		} catch (Exception e) {
			log.error("智能体信息-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@DeleteMapping("/agent/{id}")
	@Operation(summary="智能体信息-信息删除 {id}为主键id")
	public MyJsonBean<Void> deleteSystemAgent(@RequestHeader("token") String token, @PathVariable Long id) {
		MyJsonBean<Void> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.deleteSystemAgent(id);
		} catch (Exception e) {
			log.error("智能体信息-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent/update-http")
	@Operation(summary="智能体信息-更新HTTP地址")
	public MyJsonBean<Void> updateSystemAgentHttp(@RequestHeader("token") String token, @RequestBody @Validated UpdateSystemAgentHttpParam param) {
		MyJsonBean<Void> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			myJsonBean = systemAgentService.updateSystemAgentHttp(param);
		} catch (Exception e) {
			log.error("智能体信息-更新HTTP地址 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent-issued/getList")
	@Operation(summary="智能体下发-列表")
	public MyJsonBean<SystemAgentInfo> getSystemAgentIssuedList(@RequestHeader("token") String token, @RequestBody SystemAgentInfoQueryParam param) {
		MyJsonBean<SystemAgentInfo> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.setCreator(userInfo.getCreator().longValue());
			param.setWorkUnit(userInfo.getWorkUnit().longValue());
			param.setBelongGroup(userInfo.getBelongGroup().longValue());
			myJsonBean = systemAgentService.getSystemAgentIssuedList(param);
		} catch (Exception e) {
			log.error("智能体下发-列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/update-agent-issued-module")
	@Operation(summary="智能体下发-下发模块 (含取消下发)")
	public MyJsonBean<Void> updateSystemAgentIssuedModule(@RequestHeader("token") String token,
			@RequestBody UpdateSystemAgentIssuedModuleParam param) {
		MyJsonBean<Void> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.updateSystemAgentIssuedModule(param);
		} catch (Exception e) {
			log.error("智能体下发-下发模块 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent-issued-permission")
	@Operation(summary="智能体下发-下发权限(个人、角色、公司)")
	public MyJsonBean<Void> updateSystemAgentIssuedPermission(@RequestHeader("token") String token,
			@RequestBody UpdateSystemAgentIssuedPermissionParam param) {
		MyJsonBean<Void> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.updateSystemAgentIssuedPermission(param);
		} catch (Exception e) {
			log.error("智能体下发-下发权限(个人、角色、公司) 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent-module/getList")
	@Operation(summary="不同模块下的智能体-列表")
	public MyJsonBean<SystemAgentInfo> getSystemAgentModuleList(@RequestHeader("token") String token, @RequestBody SystemAgentModuleParam param) {
		MyJsonBean<SystemAgentInfo> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.setCreator(userInfo.getCreator().longValue());
			param.setWorkUnit(userInfo.getWorkUnit().longValue());
			param.setBelongGroup(userInfo.getBelongGroup().longValue());
			myJsonBean = systemAgentService.getSystemAgentModuleList(param);
		} catch (Exception e) {
			log.error("不同模块下的智能体-列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent-dialogue/getList")
	@Operation(summary="智能体-最近对话-列表")
	public MyJsonBean<SystemAgentDialogue> getSystemAgentDialogueList(@RequestHeader("token") String token,
			@RequestBody SystemAgentDialogueQueryParam param) {
		MyJsonBean<SystemAgentDialogue> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			myJsonBean = systemAgentService.getSystemAgentDialogueList(param);
		} catch (Exception e) {
			log.error("智能体-最近对话-列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@PostMapping(value = "/agent-dialogue/saveOrUpdate")
	@Operation(summary="智能体-最近对话-新增或修改")
	public MyJsonBean<SystemAgentDialogue> saveOrUpdateSystemAgentDialogue(@RequestHeader("token") String token,
			@RequestBody SystemAgentDialogue param) {
		MyJsonBean<SystemAgentDialogue> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.setCreator(userInfo.getCreator().longValue());
			param.setWorkUnit(userInfo.getWorkUnit().longValue());
			param.setBelongGroup(userInfo.getBelongGroup().longValue());
			myJsonBean = systemAgentService.saveOrUpdateSystemAgentDialogue(param);
		} catch (Exception e) {
			log.error("智能体-最近对话-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@GetMapping("/agent-dialogue/{id}")
	@Operation(summary="智能体-最近对话 {id}为主键id")
	public MyJsonBean<SystemAgentDialogue> getSystemAgentDialogue(@RequestHeader("token") String token, @PathVariable Long id) {
		MyJsonBean<SystemAgentDialogue> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.getSystemAgentDialogue(id);
		} catch (Exception e) {
			log.error("智能体-最近对话 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}

	@DeleteMapping("/agent-dialogue/{id}")
	@Operation(summary="智能体-最近对话 删除 {id}为主键id")
	public MyJsonBean<Void> deleteSystemAgentDialogue(@RequestHeader("token") String token, @PathVariable Long id) {
		MyJsonBean<Void> myJsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			myJsonBean = systemAgentService.deleteSystemAgentDialogue(id);
		} catch (Exception e) {
			log.error("智能体-最近对话 删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return myJsonBean;
	}
}
