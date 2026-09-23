package com.huabo.system.controller;


import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.service.ProcessApprovalService;

import cn.hutool.http.server.HttpServerRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 工作流审批节点控制器
 * <p>提供工作流动态获取审批节点信息，包括部门负责人、直属主管、分管领导等</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping("/process")
@Tag(name = "工作流动态获取审批节点信息", description = "工作流动态获取审批节点信息Controoler")
public class ProcessApprovalController {

	@Resource
	private ProcessApprovalService processApprovalService;
	
	/**
	 *  根据taskId 获取当前流程发起人的部门负责人
	 */
	 @Operation(summary="根据taskId获取当前流程发起人的部门负责人")
	 @GetMapping("/departmentHeader")
	 public JsonBean departmentHeader(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.getDepartmentHeader(taskId,taskNodeId);
	 }
	 
	 /**
	  *    根据taskId 获取发起人的直属主管
	  */
	 @Operation(summary="根据taskId获取发起人的直属主管")
	 @GetMapping("/directLeader")
	 public JsonBean directLeader(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.getDirectLeader(taskId,taskNodeId);
	 }
	 
	 
	 /**
	  *  根绝taskId 获取合同所属部门 中的 部门负责人
	  */
	 @Operation(summary="根据taskId获取合同所属部门的部门负责人")
	 @GetMapping("/departmentHeadByContract")
	 public JsonBean departmentHeadByContract(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.getDepartmentHeadByContract(taskId,taskNodeId);
	 }
	 
	 /**
	  *  根绝taskId 获取合同所属部门 中的 部门负责人 的 分管领导 Direct supervisor of department head
	  */

	 @Operation(summary="根据taskId获取合同所属部门负责人的分管领导")
	 @GetMapping("/dsoDheadContract")
	 public JsonBean dsoDheadContract(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.dsoDheadContract(taskId,taskNodeId);
	 }
	 
	 /**
	  *  根绝taskId 获取合同所属部门 中的 部门负责人 的 直属主管的直属主管 Direct supervisor of department head
	  */

	 @Operation(summary="根据taskId获取合同所属部门负责人的直属主管的直属主管")
	 @GetMapping("/getTwoDsoDheadContract")
	 public JsonBean getTwoDsoDheadContract(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.getTwoDsoDheadContract(taskId,taskNodeId);
	 }
	 
	 /**
	  *  根绝taskId 获取合同所属部门 中的 部门负责人 的 直属主管 所属的部门负责人，部门领导部长
	  */
	 @Operation(summary="根据taskId获取合同所属部门负责人的直属主管所属的部门负责人")
	 @GetMapping("/directLeaderDsoDheadContract")
	 public JsonBean directLeaderDsoDheadContract(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.directLeaderDsoDheadContract(taskId,taskNodeId);
	 }
	 
	 
	 /** http://192.0.2.200:9000/setting/process/getDeputyDirectorOfDeptCounerSign
	  *  根绝taskId 获取科室会签后对应审批的副部长
	  */
	 @Operation(summary="根据taskId获取科室会签后对应审批的副部长")
	 @GetMapping("/getDeputyDirectorOfDeptCounerSign")
	 public JsonBean getDeputyDirectorOfDeptCounerSign(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	     return this.processApprovalService.getDeputyDirectorOfDeptCounerSign(taskId,taskNodeId);
	 }
	 
	 /**
	  * 根据taskId 获取合同所属部门 的  直属主管
	  */
	@Operation(summary="根据taskId获取合同所属部门的直属主管")
	@GetMapping("/directLeaderByContract")
	public JsonBean directLeaderByContract(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getDirectLeaderByContract(taskId,taskNodeId);
	}

	/**
	  * 根据taskId 获取发起人所属部门的合同联络人
	  */
	@Operation(summary="根据taskId获取发起人所属部门的合同联络人")
	@GetMapping("/getContractperson")
	public JsonBean getContractperson(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getContractperson(taskId,taskNodeId);
	}
	
	
	/**
	  * 根据taskId 获取 节点名称角色  得到所有的审批人
	  */
	@Operation(summary="根据taskId获取节点名称角色的所有审批人")
	@GetMapping("/getApproverByNodeName")
	public JsonBean getApproverByNodeName(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getApproverByNodeName(taskId,taskNodeId);
	}
	
	/**
	  * 根据taskId 获取上一步审批人员的部门负责人
	  */
	@Operation(summary="根据taskId获取上一步审批人员的部门负责人")
	@GetMapping("/getDepartmentHeadByPreviousApprover")
	public JsonBean getDepartmentHeadByPreviousApprover(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getDepartmentHeadByPreviousApprover(taskId,taskNodeId);
	}
	
	/**
	  * 根据taskId 获取上一步审批人员的分管领导
	  */
	@Operation(summary="根据taskId获取上一步审批人员的分管领导")
	@GetMapping("/getDirectSupervisorByPreviousApprover")
	public JsonBean getDirectSupervisorByPreviousApprover(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getDirectSupervisorByPreviousApprover(taskId,taskNodeId);
	}
	
	
	/**Candidate approver
	  * 根据taskId 获取上一步审批人员  选择的候选审批人员
	  */
	@Operation(summary="根据taskId获取上一步审批人员选择的候选审批人员")
	@GetMapping("/getCandidateApproverInfo")
	public JsonBean getCandidateApproverInfo(HttpServerRequest request,String taskId, String taskNodeId) throws Exception{
	    return this.processApprovalService.getCandidateApproverInfo(taskId,taskNodeId);
	}
}
