package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.List;

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
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.TblSystemCustomizeScene;
import com.huabo.system.entity.TblSystemCustomizeShow;
import com.huabo.system.entity.TblSystemCustomizeShowExt;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.service.business.SystemCustomizeService;
import com.huabo.system.utils.TokenUtils;
import com.huabo.system.vo.param.BatchUpdateCustomizeShowExtParam;
import com.huabo.system.vo.param.CustomizeShowExtPreviewDetailsQueryParam;
import com.huabo.system.vo.param.TblSystemCustomizeSceneExtParam;
import com.huabo.system.vo.param.TblSystemCustomizeSceneParam;
import com.huabo.system.vo.param.TblSystemCustomizeSceneQueryParam;
import com.huabo.system.vo.param.TblSystemCustomizeShowExtQueryParam;
import com.huabo.system.vo.param.TblSystemCustomizeShowQueryParam;
import com.huabo.system.vo.param.UserInfoParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 自定义场景控制器
 * <p>提供系统自定义场景的配置、展示、预览等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "自定义场景", description = "自定义场景")
public class SystemCustomizeController {

	@Resource
	private SystemCustomizeService systemCustomizeService;

	@Resource
    private UserProvider userProvider;

	@PostMapping(value = "/customize/customizeScene/getList")
	@Operation(summary="获取自定义场景分页列表")
	public JsonBean getCustomizeScenePage(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TblSystemCustomizeSceneQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.getCustomizeScenePage(param);
		} catch (Exception e) {
			log.error("获取自定义场景分页列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeScene/saveOrUpdate")
	@Operation(summary="自定义场景-新增或修改")
	public JsonBean saveOrUpdateCustomizeScene(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody TblSystemCustomizeScene param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.saveOrUpdateCustomizeScene(param);
		} catch (Exception e) {
			log.error("自定义场景-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/customize/customizeScene/{id}")
	@Operation(summary="自定义场景-信息 {id}为主键id")
	public JsonBean getCustomizeScene(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.getCustomizeScene(id);
		} catch (Exception e) {
			log.error("自定义场景-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@DeleteMapping("/customize/customizeScene/{id}")
	@Operation(summary="自定义场景-信息删除 {id}为主键id")
	public JsonBean deleteCustomizeScene(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.deleteCustomizeScene(id);
		} catch (Exception e) {
			log.error("自定义场景-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeScene/updateState")
	@Operation(summary="自定义场景-状态变更")
	public JsonBean updateStateCustomizeScene(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody @Validated TblSystemCustomizeSceneParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.updateStateCustomizeScene(param);
		} catch (Exception e) {
			log.error("自定义场景-状态变更 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}


	@PostMapping(value = "/customize/customizeShow/getList")
	@Operation(summary="自定义展示列表")
	public JsonBean getCustomizeShowList(@RequestHeader("token") String token, @RequestBody @Validated TblSystemCustomizeShowQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.getCustomizeShowList(param);
		} catch (Exception e) {
			log.error("自定义展示列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShow/saveOrUpdate")
	@Operation(summary="自定义展示-新增或修改")
	public JsonBean saveOrUpdateCustomizeShow(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody @Validated TblSystemCustomizeShow param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.saveOrUpdateCustomizeShow(param);
		} catch (Exception e) {
			log.error("自定义展示-新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShow/savesOrUpdates")
	@Operation(summary="自定义展示-批量新增或修改")
	public JsonBean savesOrUpdatesCustomizeShow(@RequestHeader("token") String token, @RequestBody @Validated List<TblSystemCustomizeShow> param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.forEach(item -> {
				item.setCreator(userInfo.getCreator());
				item.setWorkUnit(userInfo.getWorkUnit());
				item.setBelongGroup(userInfo.getBelongGroup());
			});
			jsonBean = systemCustomizeService.savesOrUpdatesCustomizeShow(param);
		} catch (Exception e) {
			log.error("自定义展示-批量新增或修改 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}


	@GetMapping("/customize/customizeShow/{id}")
	@Operation(summary="自定义展示-信息 {id}为主键id")
	public JsonBean getCustomizeShow(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.getCustomizeShow(id);
		} catch (Exception e) {
			log.error("自定义展示-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@DeleteMapping("/customize/customizeShow/{id}")
	@Operation(summary="自定义展示-信息删除 {id}为主键id")
	public JsonBean deleteCustomizeShow(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.deleteCustomizeShow(id);
		} catch (Exception e) {
			log.error("自定义展示-信息删除 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShow/ext/getList")
	@Operation(summary="自定义展示(编辑模块) 列表")
	public JsonBean getCustomizeShowExtList(@RequestHeader("token") String token, @RequestBody @Validated TblSystemCustomizeShowExtQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.getCustomizeShowExtList(param);
		} catch (Exception e) {
			log.error("自定义展示(编辑模块) 列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShowExt/batchUpdateState")
	@Operation(summary="自定义展示(编辑模块)-批量更新")
	public JsonBean batchUpdateCustomizeShowExt(@RequestHeader("token") String token, @RequestBody BatchUpdateCustomizeShowExtParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.batchUpdateCustomizeShowExt(param);
		} catch (Exception e) {
			log.error("自定义展示(编辑模块)-批量更新：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShowExt/saveOrUpdate")
	@Operation(summary="自定义展示(编辑模块)-新增或修改")
	public JsonBean saveOrUpdateCustomizeShowExt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody @Validated TblSystemCustomizeShowExt param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.saveOrUpdateCustomizeShowExt(param);
		} catch (Exception e) {
			log.error("自定义展示-新增或修改(编辑模块) 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShowExt/savesOrUpdates")
	@Operation(summary="自定义展示(编辑模块)-批量新增或修改")
	public JsonBean savesOrUpdatesCustomizeShowExt(@RequestHeader("token") String token,
			@RequestBody @Validated List<TblSystemCustomizeShowExt> param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			param.forEach(item -> {
				item.setCreator(userInfo.getCreator());
				item.setWorkUnit(userInfo.getWorkUnit());
				item.setBelongGroup(userInfo.getBelongGroup());
			});
			jsonBean = systemCustomizeService.savesOrUpdatesCustomizeShowExt(param);
		} catch (Exception e) {
			log.error("自定义展示-批量新增或修改(编辑模块) 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping("/customize/customizeShowExt/{id}")
	@Operation(summary="自定义展示(编辑模块)-信息 {id}为主键id")
	public JsonBean getCustomizeShowExt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@PathVariable BigDecimal id) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.getCustomizeShowExt(id);
		} catch (Exception e) {
			log.error("自定义展示(编辑模块)-信息 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/customize/customizeShowExt/updateState")
	@Operation(summary="自定义展示(编辑模块)-状态变更")
	public JsonBean updateStateCustomizeSceneExt(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody @Validated TblSystemCustomizeSceneExtParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = systemCustomizeService.updateStateCustomizeSceneExt(param);
		} catch (Exception e) {
			log.error("自定义展示(编辑模块)-状态变更 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping("/customize/preview/details")
	@Operation(summary="预览展示-详情")
	public JsonBean getCustomizeShowExtPreviewDetails(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody CustomizeShowExtPreviewDetailsQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.getCustomizeShowExtPreviewDetails(param);
		} catch (Exception e) {
			log.error("预览展示-详情 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping("/customize/preview/list")
	@Operation(summary="预览展示-列表")
	public JsonBean getCustomizeShowExtPreviewList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@RequestBody CustomizeShowExtPreviewDetailsQueryParam param) {
		JsonBean jsonBean;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				throw new ServiceException(401, 20006);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			jsonBean = systemCustomizeService.getCustomizeShowExtPreviewList(param);
		} catch (Exception e) {
			log.error("预览展示-列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
	}
}
