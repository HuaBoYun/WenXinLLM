package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSecrectLevel;
import com.huabo.system.service.SecrectLevelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 密级信息控制器
 * <p>提供文档密级信息的查询和管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "secrect", description = "密级信息Controller")
@RequestMapping("/secrect")
public class SecrectLevelController {

	@Resource
    public SecrectLevelService secrectLevelService;


    @OperationLog(
            success = "新增保存信息",
            busType = "系统配置",
            fail = "新增保存信息",
            operationType = OperationType.ADD,
            subType = "密级设置"
    )
	@PostMapping(value = "/saveInfo", produces = "application/json; charset=utf-8")
    @Operation(summary="新增保存信息")
    public JsonBean saveInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblSecrectLevel secrect) throws Exception {
        return this.secrectLevelService.saveInfo(token,secrect);
    }

    @OperationLog(
            success = "修改保存信息",
            busType = "系统配置",
            fail = "修改保存信息",
            operationType = OperationType.UPDATE,
            subType = "密级设置"
    )
	@PostMapping(value = "/modifyInfo", produces = "application/json; charset=utf-8")
    @Operation(summary="修改保存信息")
    public JsonBean modifyInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        TblSecrectLevel secrect) throws Exception {
        return this.secrectLevelService.modifyInfo(token,secrect);
    }

    @OperationLog(
            success = "获取单个信息详情",
            busType = "系统配置",
            fail = "获取单个信息详情",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getDetail", produces = "application/json; charset=utf-8")
    @Operation(summary="获取单个信息详情")
    public JsonBean getDetail(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "levelId", description = "主键", required = true) @RequestParam(value = "levelId",required = true)BigDecimal levelId) throws Exception {
        return this.secrectLevelService.getDetail(token,levelId);
    }

    @OperationLog(
            success = "删除单个信息",
            busType = "系统配置",
            fail = "删除单个信息",
            operationType = OperationType.DELETE,
            subType = "密级设置"
    )
	@GetMapping(value = "/removeInfo", produces = "application/json; charset=utf-8")
    @Operation(summary="删除单个信息")
    public JsonBean removeInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "levelId", description = "主键", required = true) @RequestParam(value = "levelId",required = true)BigDecimal levelId) throws Exception {
        return this.secrectLevelService.removeInfo(token,levelId);
    }

    @OperationLog(
            success = "获取密级列表数据",
            busType = "系统配置",
            fail = "获取密级列表数据",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getPageInfo", produces = "application/json; charset=utf-8")
    @Operation(summary="分页信息接口")
    public JsonBean getPageInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblSecrectLevel secrect,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
    	@Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) throws Exception {
        return this.secrectLevelService.getPageInfo(token,secrect,pageNumber,pageSize);
    }

    @OperationLog(
            success = "通过当前密级类型获取其需要设置的下属类型密级集合",
            busType = "系统配置",
            fail = "通过当前密级类型获取其需要设置的下属类型密级集合",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getScopeSecrectListByType", produces = "application/json; charset=utf-8")
    @Operation(summary="通过当前密级类型获取其需要设置的下属类型密级集合")
    public JsonBean getScopeSecrectListByType(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "levelType",description="密级类型，1-功能模块，2-业务单据，3-人员，4-附件", required = true)@RequestParam(value = "levelType", required = true) Integer levelType) throws Exception {
        return this.secrectLevelService.getScopeSecrectListByType(token,levelType);
    }

    @OperationLog(
            success = "通过密级类型获取密级集合",
            busType = "系统配置",
            fail = "通过密级类型获取密级集合",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getSecrectListByType", produces = "application/json; charset=utf-8")
    @Operation(summary="通过密级类型获取密级集合")
    public JsonBean getSecrectListByType(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "levelType",description="密级类型，1-功能模块，2-业务单据，3-人员，4-附件", required = true)@RequestParam(value = "levelType", required = true) Integer levelType) throws Exception {
        return this.secrectLevelService.getSecrectListByType(token,levelType);
    }
	
	/**
	 * 业务单据开始调用密级
	 */
    @OperationLog(
            success = "获取当前功能模块所能创建的业务单据密级范围",
            busType = "系统配置",
            fail = "获取当前功能模块所能创建的业务单据密级范围",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getSecrectListForRight", produces = "application/json; charset=utf-8")
    @Operation(summary="获取当前功能模块所能创建的业务单据密级范围")
    public JsonBean getSecrectListForRight(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "rightId",description="系统权限主键", required = true)@RequestParam(value = "rightId", required = true) BigDecimal rightId) throws Exception {
        return this.secrectLevelService.getSecrectListForRight(token,rightId);
    }


    @OperationLog(
            success = "通过当前密级主键获取所能操作下级密级范围",
            busType = "系统配置",
            fail = "通过当前密级主键获取所能操作下级密级范围",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getSecrectListByManage", produces = "application/json; charset=utf-8")
    @Operation(summary="通过当前密级主键获取所能操作下级密级范围")
    public JsonBean getSecrectListByManage(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "levelId",description="级别主键", required = true)@RequestParam(value = "levelId", required = true) BigDecimal levelId) throws Exception {
        return this.secrectLevelService.getSecrectListByManage(token,levelId);
    }
	
	/**
	 * 登录人获取流程编辑时  能设置的密级范围
	 */
    @OperationLog(
            success = "通过当前密级主键获取所能操作下级密级范围",
            busType = "系统配置",
            fail = "通过当前密级主键获取所能操作下级密级范围",
            operationType = OperationType.SELECT,
            subType = "密级设置"
    )
	@GetMapping(value = "/getSecrectListByLoginUser", produces = "application/json; charset=utf-8")
    @Operation(summary="通过当前密级主键获取所能操作下级密级范围")
    public JsonBean getSecrectListByLoginUser(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        return this.secrectLevelService.getSecrectListByLoginUser(token);
    }
}
