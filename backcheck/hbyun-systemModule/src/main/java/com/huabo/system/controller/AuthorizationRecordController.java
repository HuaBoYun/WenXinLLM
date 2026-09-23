package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.service.TblAuthorizationRecordService;
import com.huabo.system.vo.TblAuthorizationRecordVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 系统确认信息控制器
 * <p>提供授权确认记录的详情查询、分页列表、确认操作等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "aurecord", description = "系统确认信息接口")
@RequestMapping(value = "/aurecord")
public class AuthorizationRecordController {

	@Resource
	private TblAuthorizationRecordService tblAuthorizationRecordService;
	
	@Resource
	private UserProvider userProvider;
	
	@GetMapping(value = "/confirm/detail",produces = "application/json; charset=utf-8")
	@Operation(summary="确认信息详情")
	public JsonBean confirm_detail(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="recordId",description="确认记录主键",required=true) @RequestParam(value = "recordId", required = true) String recordId) throws Exception {
		
		return this.tblAuthorizationRecordService.findById(recordId);
	}
	
	
	@GetMapping(value = "/confirm/list",produces = "application/json; charset=utf-8")
	@Operation(summary="确认信息分页列表")
	public JsonBean confirm_list(HttpServletRequest request, HttpServletResponse response,
			TblAuthorizationRecordVo vo) throws Exception {
		return this.tblAuthorizationRecordService.findPageList(vo);
	}
	
	@GetMapping(value = "/confirm/verifyOperation",produces = "application/json; charset=utf-8")
	@Operation(summary="校验是否可以继续操作")
	public JsonBean confirm_verifyOperation(HttpServletRequest request, HttpServletResponse response,
			@Parameter(name="targetId",description="业务单据主键，"
					+ "合同流程启用弃用删除时传入activityId；"
					+ "流程设计 流程启用弃用时传入(loginStaff.currentOrg.orgId-tableId)，流程删除时传入（loginStaff.getCurrentOrg().getOrgid()-ymWorkId）",required=true) 
			@RequestParam(value = "targetId", required = true) String targetId,
			@Parameter(name="targetType",description="业务单据类型，company-公司、dept-部门、user-用户、right-菜单、secrect-密级、commonflow-通用流程、contractflow-合同流程",required=true) 
			@RequestParam(value = "targetType", required = true) String targetType,
			@Parameter(name="operationType",description="操作类型 1-新增，2-修改，3-删除，4-启用，5-弃用，6-重置密码",required=true) @RequestParam(value = "operationType", required = true)
			int operationType) throws Exception {
		return this.tblAuthorizationRecordService.verifyOperation(targetId,targetType,operationType);
	}
	
	
	/**
	 * 测试接口不对外开放
	 */ 
	@RequestMapping(value = "/confirm/testModifyTarget", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="测试系统确认审批通过后修改目标数据")
    public JsonBean screenRight_getRoleRightList(HttpServletRequest reques,
    		@Parameter(name="recordId",description="确认记录主键",required=false) @RequestParam(value = "recordId", required = false) String recordId) throws Exception {
           JsonBean jsonBean = null;
			try {
				jsonBean =  this.tblAuthorizationRecordService.modifyFlowEndModifyOperationData(recordId,userProvider.get());
			} catch (Exception e) {
				e.printStackTrace();
			}
			return jsonBean;
    }
	
	
	
	
	
	
}
