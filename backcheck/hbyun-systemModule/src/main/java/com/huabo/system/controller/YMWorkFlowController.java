package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.system.entity.TblSystemSheetTable;
import com.huabo.system.flow.FlowModel;
import com.huabo.system.service.YMBusinessService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 流程平台工作流控制器
 * <p>提供流程平台工作流的流程信息查询、流程复制等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "流程平台工作流Controller", description = "流程平台的工作流Controoler")
@RequestMapping("/ymWrok")
public class YMWorkFlowController {


	@Resource
	public YMBusinessService ymBusinessService;


	@RequestMapping(value = "/getProcessInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计-获取接口服务信息")
    public JsonBean getProcessInfoList(HttpServletRequest reques) throws Exception {
        return this.ymBusinessService.getProcessInfoList();
    }

	@RequestMapping(value = "/copyFlowInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程设计-复制流程")
    public JsonBean copyFlowInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="ymWorkFrom",description="流程Id主键",required=false) @RequestParam(value = "ymWorkFrom", required = true) String ymWorkFrom,
    		@Parameter(name="orgIds",description="选择的组织Id, 英文逗号拼接",required=true) @RequestParam(value = "orgIds", required = true) String orgIds,
    		@Parameter(name="tableId",description="表单ID",required=true) @RequestParam(value = "tableId", required = true) BigDecimal tableId) throws Exception {
        return this.ymBusinessService.copyFlowInfo(token,ymWorkFrom,tableId,orgIds);
    }

	@RequestMapping(value = "/copyContractTypeFlow", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="合同类型流程设计-复制流程")
    public JsonBean copyContractTypeFlow(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="orgIds",description="选择的组织Id, 英文逗号拼接",required=true) @RequestParam(value = "orgIds", required = true) String orgIds,
    		@Parameter(name="activityId",description="流程关联ID",required=true) @RequestParam(value = "activityId", required = true) String activityId) throws Exception {
        return this.ymBusinessService.copyContractTypeFlow(token,activityId,orgIds);
    }

	@RequestMapping(value = "/startYmWorkFlow", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作流设计-工作流，启用弃用")
    public JsonBean startYmWorkFlow(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="ymWorkForm",description="流程平台表单主键",required=true) @RequestParam(value = "ymWorkForm", required = true) String ymWorkForm,
    		@Parameter(name="tablId",description="表单ID",required=true) @RequestParam(value = "tablId", required = true) BigDecimal tablId,
    		@Parameter(name="qystatus",description="启用状态 1-启用  ，其余未启用",required=false) @RequestParam(value = "qystatus", required = false , defaultValue = "1") Integer qystatus) throws Exception {
        return this.ymBusinessService.startYmWorkFlow(token,tablId,ymWorkForm,qystatus);
    }

	@RequestMapping(value = "/startContractTypeFlow", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作流设计-合同工作流，启用弃用")
    public JsonBean startContractTypeFlow(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="activityId",description="流程关联ID",required=true) @RequestParam(value = "activityId", required = true) String activityId,
    		@Parameter(name="qystatus",description="启用状态 1-启用  ，其余未启用",required=false) @RequestParam(value = "qystatus", required = false , defaultValue = "1") Integer qystatus) throws Exception {
        return this.ymBusinessService.startContractTypeFlow(token,activityId,qystatus);
    }

	@RequestMapping(value = "/getSystemFlowType", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计-获取所属模块list")
    public JsonBean getSystemFlowType(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.ymBusinessService.getSystemFlowType(token);
    }

	@RequestMapping(value = "/getSystemFlowList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计-获取系统表单信息")
    public JsonBean getSystemFlowList(HttpServletRequest reques,TblSystemSheetTable sheet,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.ymBusinessService.getSystemFlowList(token,sheet);
    }

	@RequestMapping(value = "/getYmFormData", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计-获取流程平台表单信息")
    public JsonBean getYmFormData(HttpServletRequest reques,
    		@Parameter(name="tableId",description="系统表单数据",required=true) @RequestParam(value = "tableId", required = true) BigDecimal tableId) throws Exception {
        return this.ymBusinessService.getYmFormData(tableId);
    }


	@RequestMapping(value = "/saveWorkFlowFormInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作流设计-新建工作流，保存信息")
    public JsonBean saveWorkFlowFormInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="workName",description="工作流名称",required=true) @RequestParam(value = "workName", required = true) String workName,
    		@Parameter(name="ymWorkId",description="工作流主键",required=false) @RequestParam(value = "ymWorkId", required = false) String ymWorkId,
    		@Parameter(name="tablId",description="表单ID",required=true) @RequestParam(value = "tablId", required = true) BigDecimal tablId,
    		@Parameter(name="typeId",description="合同类型ID",required=false) @RequestParam(value = "typeId", required = false) String typeId,
    		@Parameter(name="flowType",description="流程类型：0-标准流程 1-简单流程 2-任务流程，默认0",required=false) @RequestParam(value = "flowType", required = false, defaultValue = "0") Integer flowType) throws Exception {
        return this.ymBusinessService.saveWorkFlowFormInfo(token,workName,tablId,ymWorkId,typeId,flowType);
    }

	@RequestMapping(value = "/removeWorkFlowFormInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="工作流设计-删除工作流信息")
    public JsonBean removeWorkFlowFormInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="ymWorkId",description="工作流主键",required=false) @RequestParam(value = "ymWorkId", required = false) String ymWorkId,
    		@Parameter(name="tableId",description="表单ID",required=true) @RequestParam(value = "tableId", required = true) BigDecimal tableId) throws Exception {
        return this.ymBusinessService.removeWorkFlowFormInfo(token,tableId,ymWorkId);
    }

	@RequestMapping(value = "/removeWorkFlowContractType", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="合同类型流程设计-删除工作流信息")
    public JsonBean removeWorkFlowContractType(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="activityId",description="合同类型流程设计主键",required=true) @RequestParam(value = "activityId", required = true) BigDecimal activityId) throws Exception {
        return this.ymBusinessService.removeWorkFlowContractType(token,activityId);
    }


	@RequestMapping(value = "/getWorFlowList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计、审批列表-获取所有工作流信息")
    public JsonBean getWorFlowList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="workName",description="工作流名称",required=false) @RequestParam(value = "workName", required = false) String workName,
    		@Parameter(name="tablId",description="表单ID",required=true) @RequestParam(value = "tablId", required = true) BigDecimal tablId,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false,defaultValue="1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false,defaultValue="20") Integer pageSize) throws Exception {
	        return this.ymBusinessService.getWorkFlowList(token,workName,tablId,currentPage,pageSize);
    }

	@RequestMapping(value = "/getContractTypeFlowList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="工作流设计、审批列表-获取所有工作流信息")
    public JsonBean getContractTypeFlowList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="workName",description="工作流名称",required=false) @RequestParam(value = "workName", required = false) String workName,
    		@Parameter(name="tablId",description="表单ID",required=true) @RequestParam(value = "tablId", required = true) BigDecimal tablId,
    		@Parameter(name="typeId",description="合同类型ID",required=true) @RequestParam(value = "typeId", required = true) BigDecimal typeId,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false,defaultValue="1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false,defaultValue="20") Integer pageSize) throws Exception {
        return this.ymBusinessService.getContractTypeFlowList(token,workName,tablId,typeId,currentPage,pageSize);
    }


	@OperationLog(
            success = "流程提交成功",
            busType = "流程审核",
            fail = "流程提交成功",
            operationType = OperationType.SELECT,
            subType = "流程提交"
    )
	@RequestMapping(value = "/submit", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办、我发起的-提交审批")
    public JsonBean submit(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="fromId",description="表单主键",required=true) @RequestParam(value = "fromId", required = true) BigDecimal fromId,
    		@Parameter(name="status",description="流程状态，flowTaskInfo.status",required=false) @RequestParam(value = "status", required = false) Integer status,
    		@Parameter(name="tableId",description="工作流主键",required=false) @RequestParam(value = "tableId", required = false) String tableId,
    		@Parameter(name="flowId",description="工作流主键",required=false) @RequestParam(value = "flowId", required = false) String flowId,
    		@Parameter(name="branchStrs",description="candidateType=1 的时候传入，选择分支，可以多选 用逗号分割",required=false) @RequestParam(value = "branchStrs", required = false) String branchStrs,
    		@Parameter(name="candidateType",description="1-流程分支，2-候选人，3-没有流程分支和候选人",required=false) @RequestParam(value = "candidateType", required = false) String candidateType,
    		@Parameter(name="ymFromId",description="流程平台工作流表单主键",required=false) @RequestParam(value = "ymFromId", required = false) String ymFromId,
    		@Parameter(name="nodeCode",description="candidateType=2 的时候传入，当前节点nodeCode",required=false) @RequestParam(value = "nodeCode", required = false) String nodeCode,
    		@Parameter(name="candidateList",description="candidateType=2 的时候传入，选中的候选人Id，可以多选 用逗号分割",required=false) @RequestParam(value = "candidateList", required = false) String candidateList,
    		@Parameter(name="typeName",description="合同类型名称，合同起草、变更发起流程时传入",required=false) @RequestParam(value = "typeName", required = false) String typeName,
    		@Parameter(name="eventType",description="操作类型 submit提交 此处默认submit",required=false) @RequestParam(value = "eventType", required = false , defaultValue = "submit") String eventType
    		) throws Exception {
        return this.ymBusinessService.submit(token,fromId,tableId,ymFromId,candidateType,branchStrs,nodeCode,candidateList,flowId,status,typeName,eventType);
    }


	@OperationLog(
            success = "流程删除成功",
            busType = "流程审核",
            fail = "流程删除成功",
            operationType = OperationType.DELETE,
            subType = "流程删除"
    )
	@RequestMapping(value = "/delete", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我发起的-删除流程")
    public JsonBean delete(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowId",description="工作流主键",required=false) @RequestParam(value = "flowId", required = false) String flowId,
    		@Parameter(name="id",description="流程平台工作流表单主键" , required=true) @RequestParam(value = "id", required = true) String id
    		) throws Exception {
        return this.ymBusinessService.delete(token,id,flowId);
    }

	@OperationLog(
            success = "列表信息",
            busType = "流程审核",
            fail = "列表信息",
            operationType = OperationType.SELECT,
            subType = "我的发起"
    )
	@RequestMapping(value = "/flowLaunch", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我发起的-列表信息")
    public JsonBean flowLaunch(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程名称",required=false) @RequestParam(value = "flowName", required = false) String flowName,
    		@Parameter(name="status",description="查询条件-流程状态  1-等待审核、2-审核通过、3-审核驳回、4-流程撤回",required=false) @RequestParam(value = "status", required = false) String status) throws Exception {
        return this.ymBusinessService.getFlowLaunch(token,currentPage,pageSize,flowName,status);
    }

	@RequestMapping(value = "/getFlowPkInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我发起的-根据表单主键和tableId获取流程主键信息")
    public JsonBean getFlowPkInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tableId",description="流程配置信息Id",required=true) @RequestParam(value = "tableId", required = true) String tableId,
    		@Parameter(name="formId",description="表单主键Id",required=true) @RequestParam(value = "formId", required = true) String formId) throws Exception {
        return this.ymBusinessService.getFlowPkInfo(token,tableId,formId);
    }

	@OperationLog(
            success = "编辑获取详情",
            busType = "流程审核",
            fail = "编辑获取详情",
            operationType = OperationType.SELECT,
            subType = "我的发起"
    )
	@RequestMapping(value = "/getEditInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我发起的-编辑获取详情")
    public JsonBean getEditInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="id",description="主键Id",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="主键Id",required=true) @RequestParam(value = "flowId", required = true) String flowId) throws Exception {
        return this.ymBusinessService.getEditInfo(token,id,flowId);
    }

	@OperationLog(
            success = "撤回处理",
            busType = "流程审核",
            fail = "撤回处理",
            operationType = OperationType.UPDATE,
            subType = "我的发起"
    )
	@RequestMapping(value = "/actionsWithdraw", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我发起的-撤回处理")
    public JsonBean actionsWithdraw(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="id",description="主键Id",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="主键Id",required=false) @RequestParam(value = "flowId", required = false) String flowId) throws Exception {
        return this.ymBusinessService.actionsWithdraw(token,id,flowId);
    }


	@OperationLog(
            success = "获取列表信息",
            busType = "流程审核",
            fail = "获取列表信息",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/getList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办-列表信息")
    public JsonBean getList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymBusinessService.getDealt(token,currentPage,pageSize,flowName);
    }


	@OperationLog(
            success = "获取办理信息详情",
            busType = "流程审核",
            fail = "获取办理信息详情",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/getInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办、已办事宜、-获取办理信息详情")
    public JsonBean getInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="processId",description="工作流主键",required=true) @RequestParam(value = "processId", required = true) String processId,
    		@Parameter(name="thisStepId",description="审批nodeId",required=false) @RequestParam(value = "thisStepId", required = false) String thisStepId,
    		@Parameter(name="opType",description="操作类型 ，0-查看发起,3-查看待办在办，4-查看已办",required=false) @RequestParam(value = "opType", required = false) String opType,
    		@Parameter(name="id",description="主键Id",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键Id",required=true) @RequestParam(value = "flowId", required = true) String flowId) throws Exception {
        return this.ymBusinessService.getInfo(token,id,thisStepId,processId,flowId,opType);
    }


	@OperationLog(
            success = "查询办理驳回节点集合",
            busType = "流程审核",
            fail = "查询办理驳回节点集合",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/rejectList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="我的待办-查询办理驳回节点集合")
    public JsonBean rejectList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="operatorId",description="operatorId主键",required=true) @RequestParam(value = "operatorId", required = true) String operatorId) throws Exception {
        return this.ymBusinessService.rejectList(token,operatorId);
    }


	@OperationLog(
            success = "办理驳回",
            busType = "流程审核",
            fail = "办理驳回",
            operationType = OperationType.UPDATE,
            subType = "我的待办"
    )
	@RequestMapping(value = "/reject", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="我的待办-办理驳回")
    public JsonBean reject(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="operatorId",description="operatorId主键",required=true) @RequestParam(value = "operatorId", required = true) String operatorId,
    		@Parameter(name="id",description="flowTaskInfo主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="thisStepId",description="当前审批节点Id主键",required=false) @RequestParam(value = "thisStepId", required = false) String thisStepId,
    		@Parameter(name="handleOpinion",description="审批意见",required=true) @RequestParam(value = "handleOpinion", required = true) String handleOpinion,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="copyIds",description="自定义抄送人",required=false) @RequestParam(value = "copyIds", required = false) String copyIds,
    		@Parameter(name="candidateType",description="type",required=false) @RequestParam(value = "candidateType", required = false) String candidateType,
    		@Parameter(name="enCode",description="enCode",required=false) @RequestParam(value = "enCode", required = false) String enCode,
    		@Parameter(name="branchList",description="选择分支",required=false) @RequestParam(value = "branchList", required = false) String branchList,
    		@Parameter(name="rejectStep",description="多节点返回的nodeCode",required=false) @RequestParam(value = "rejectStep", required = false) String rejectStep,
    		@Parameter(name="rejectType",description=" 驳回类型 1.重新审批 2.从当前节点审批",required=false) @RequestParam(value = "rejectType", required = false) String rejectType,
    		@Parameter(name="handleStatus",description="审批处理标识，0.拒绝  1.同意",required=false) @RequestParam(value = "handleStatus", required = false,defaultValue = "0") String handleStatus) throws Exception {
		return this.ymBusinessService.reject(token,flowId,handleOpinion,signImg,copyIds,candidateType,enCode,branchList,id,operatorId,rejectStep,rejectType,thisStepId,handleStatus);
    }


	@OperationLog(
            success = "提交或通过时获取下一步审批节点",
            busType = "流程审核",
            fail = "提交或通过时获取下一步审批节点",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/candidates", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办、我发起的-提交、通过时获取下一步审批节点")
    public JsonBean candidates(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="id",description="flowTaskInfo主键 发起时提交为空",required=false) @RequestParam(value = "id", required = false) String id,
    		@Parameter(name="flowId",description="工作流主键",required=false) @RequestParam(value = "flowId", required = false) String flowId,
    		@Parameter(name="fromId",description="表单主键",required=true) @RequestParam(value = "fromId", required = true) BigDecimal fromId,
    		@Parameter(name="tableId",description="系统表单主键 列表提交时传入",required=false) @RequestParam(value = "tableId", required = false) String tableId,
    		@Parameter(name="flowTaskOperatorId",description="通过时传入当前审批节点Id，提交审批，发起提交时默认为0",required=false) @RequestParam(value = "flowTaskOperatorId", required = false) String flowTaskOperatorId) throws Exception {
        return this.ymBusinessService.candidates(token,flowTaskOperatorId,id,flowId,fromId,tableId);
    }

	@OperationLog(
            success = "提交或通过时获取下一步审批人员",
            busType = "流程审核",
            fail = "提交或通过时获取下一步审批人员",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/candidateUser", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办、我发起的-提交、通过时获取下一步审批人员")
    public JsonBean candidateUser(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false,defaultValue = "1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页记录数",required=false) @RequestParam(value = "pageSize", required = false,defaultValue = "15") Integer pageSize,
    		@Parameter(name="keyword",description="查询关键字",required=false) @RequestParam(value = "keyword", required = false) String keyword,
    		@Parameter(name="id",description="flowTaskInfo主键 发起时提交为空",required=false) @RequestParam(value = "id", required = false) String id,
    		@Parameter(name="flowId",description="工作流主键",required=false) @RequestParam(value = "flowId", required = false) String flowId,
    		@Parameter(name="tableId",description="系统表单主键 列表提交时传入",required=false) @RequestParam(value = "tableId", required = false) String tableId,
    		@Parameter(name="fromId",description="表单主键",required=false) @RequestParam(value = "fromId", required = false) BigDecimal fromId,
    		@Parameter(name="nodeCode",description="节点编号",required=true) @RequestParam(value = "nodeCode", required = true) String nodeCode,
    		@Parameter(name="flowTaskOperatorId",description="通过时传入当前审批节点Id，提交审批，发起提交时默认为0",required=false) @RequestParam(value = "flowTaskOperatorId", required = false) String flowTaskOperatorId) throws Exception {
        return this.ymBusinessService.candidateUser(token,flowTaskOperatorId,id,flowId,fromId,currentPage,pageSize,keyword,nodeCode,tableId);
    }

	@OperationLog(
            success = "提交或通过时获取需要抄送的审批人信息",
            busType = "流程审核",
            fail = "提交或通过时需要抄送的审批人信息",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/copyApprovalStaffList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的待办、我发起的-提交、获取需要抄送的审批人信息")
    public JsonBean copyApprovalStaffList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="processId",description="工作流主键",required=true) @RequestParam(value = "processId", required = true) String processId) throws Exception {
        return this.ymBusinessService.copyApprovalStaffList(token,processId);
    }

	@OperationLog(
            success = "办理通过",
            busType = "流程审核",
            fail = "办理通过",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/audit", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="我的待办-办理通过")
    public JsonBean audit(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="operatorId",description="operatorId[0]主键",required=true) @RequestParam(value = "operatorId", required = true) String operatorId,
    		@Parameter(name="id",description="flowTaskInfo主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="thisStepId",description="当前办理节点nodeId",required=true) @RequestParam(value = "thisStepId", required = true) String thisStepId,
    		@Parameter(name="nextStepId",description="下一步办理节点node",required=false) @RequestParam(value = "nextStepId", required = false) String nextStepId,
    		@Parameter(name="handleOpinion",description="审批意见",required=true) @RequestParam(value = "handleOpinion", required = true) String handleOpinion,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="copyIds",description="自定义抄送人,多选用','逗号分割",required=false) @RequestParam(value = "copyIds", required = false) String copyIds,
    		@Parameter(name="enCode",description="enCode",required=false) @RequestParam(value = "enCode", required = false) String enCode,
    		@Parameter(name="branchStrs",description="candidateType=1的时候传入,选择分支，可以多选 用逗号分割",required=false) @RequestParam(value = "branchStrs", required = false) String branchStrs,
    		@Parameter(name="candidateType",description="type",required=false) @RequestParam(value = "candidateType", required = false) String candidateType,
    		@Parameter(name="freeApproverUserId",description="加签人员 多选 逗号分割",required=false) @RequestParam(value = "freeApproverUserId", required = false)String freeApproverUserId,
    		@Parameter(name="nodeCode",description="candidateType=2 的时候传入，当前节点nodeCode",required=false) @RequestParam(value = "nodeCode", required = false) String nodeCode,
    		@Parameter(name="candidateList",description="candidateType=2 的时候传入，选中的候选人Id，可以多选 用逗号分割",required=false) @RequestParam(value = "candidateList", required = false) String candidateList) throws Exception {
        return this.ymBusinessService.audit(token,flowId,handleOpinion,signImg,copyIds,enCode,branchStrs,id,operatorId,freeApproverUserId,candidateType,nodeCode,candidateList,nextStepId,thisStepId);
    }


	@OperationLog(
            success = "转审办理",
            busType = "流程审核",
            fail = "转审办理",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
	@RequestMapping(value = "/transfer", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="我的待办-转审办理")
    public JsonBean transfer(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowTaskInfoOperatorId",description="flowTaskInfoOperatorId主键",required=true) @RequestParam(value = "flowTaskInfoOperatorId", required = true) String flowTaskInfoOperatorId,
    		@Parameter(name="handleOpinion",description="转审原因",required=true) @RequestParam(value = "handleOpinion", required = true) String handleOpinion,
    		@Parameter(name="transferStaffId",description="转审人",required=true) @RequestParam(value = "transferStaffId", required = true) String transferStaffId,
    		@Parameter(name="id",description="flowTaskInfo主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg) throws Exception {
        return this.ymBusinessService.transfer(token,flowTaskInfoOperatorId,handleOpinion,transferStaffId,signImg,id,flowId);
    }

	@OperationLog(
            success = "获取已办事宜- 列表数据",
            busType = "系统设置",
            fail = "获取已办事宜- 列表数据",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
	@RequestMapping(value = "/alreadyList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="已办事宜- 列表数据")
    public JsonBean alreadyList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymBusinessService.alreadyList(token,currentPage,pageSize,flowName);
    }


	@OperationLog(
            success = "撤回处理",
            busType = "流程审核",
            fail = "撤回处理",
            operationType = OperationType.SELECT,
            subType = "已办事宜"
    )
	@RequestMapping(value = "/alreadyRecall", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="已办事宜-撤回处理")
    public JsonBean alreadyRecall(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowTaskOperatorRecordListId",description="审批记录主键 flowTaskOperatorRecordListMax[id]",required=true) @RequestParam(value = "flowTaskOperatorRecordListId", required = true) String flowTaskOperatorRecordListId,
    		@Parameter(name="freeApproverUserId",description="抄送人Id,多人用逗号分割",required=false) @RequestParam(value = "freeApproverUserId", required = false) String freeApproverUserId,
    		@Parameter(name="handleOpinion",description="撤回原因",required=false) @RequestParam(value = "handleOpinion", required = false) String handleOpinion,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="flowId",description="流程主键",required=false) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="processId",description="流程任务主键",required=false) @RequestParam(value = "processId", required = false) String processId) throws Exception {
        return this.ymBusinessService.alreadyRecall(token,flowTaskOperatorRecordListId,freeApproverUserId,handleOpinion,signImg,flowId,processId);
    }

	@RequestMapping(value = "/copyInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="抄送事宜- 列表数据")
    public JsonBean copyInfoList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymBusinessService.copyInfoList(token,currentPage,pageSize,flowName);
    }

	@RequestMapping(value = "/copyInfoDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="抄送事宜-信息详情")
    public JsonBean copyInfoDetail(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="thisStepId",description="审批nodeId",required=false) @RequestParam(value = "thisStepId", required = false) String thisStepId,
    		@Parameter(name="id",description="流程任务主键Id taskid",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键Id",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="operatorId",description="流程任务节点Id",required=false) @RequestParam(value = "operatorId", required = false) String operatorId
    		) throws Exception {
        return this.ymBusinessService.copyInfoDetail(token,id,thisStepId,flowId,operatorId);
    }

	@RequestMapping(value = "/getWorkCount", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="消息-待办事宜")
    public JsonBean getWorkCount(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token
    		) throws Exception {
        return this.ymBusinessService.getWorkCount(token);
    }


	@RequestMapping(value = "/getListForId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="待办发送oa-获取当前待办人员指定id的参数")
    public JsonBean getListForId(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token ,
    		@Parameter(name="processId",description="流程id",required=true) @RequestParam(value = "processId", required = false) String processId
    		) throws Exception {
        return this.ymBusinessService.getListForId(token,processId);
    }

	@RequestMapping(value = "/informInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程办理详情页面-获取知会分页信息")
    public JsonBean informInfoList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false,defaultValue = "1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
    		@Parameter(name="id",description="flowTaskInfo主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="工作流主键",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="informStaffName",description="查询条件-知会人姓名",required=false) @RequestParam(value = "informStaffName", required = false) String informStaffName,
    		@Parameter(name="createStaffName",description="查询条件-发起人姓名",required=false) @RequestParam(value = "createStaffName", required = false) String createStaffName,
    		@Parameter(name="isRead",description="查询条件-是否已读   0-未读 ，1-已读",required=false) @RequestParam(value = "isRead", required = false) Integer isRead) throws Exception {
        return this.ymBusinessService.informInfoList(token,currentPage,pageSize,informStaffName,isRead,id,flowId,createStaffName);
    }


	@RequestMapping(value = "/getFlowTaskInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="业务数据列表，修改时提交办理流程信息")
    public JsonBean getFlowTaskInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tableId",description="系统初始化数据Id 需要后端开发人员去tbl_system_sheettable表中找对应数据",required=true) @RequestParam(value = "tableId", required = true) String tableId,
    		@Parameter(name="typeName",description="合同类型名称",required=false) @RequestParam(value = "typeName", required = false) String typeName,
    		@Parameter(name="formId",description="表单主键Id",required=true) @RequestParam(value = "formId", required = true) String formId
    		) throws Exception {
        return this.ymBusinessService.getFlowTaskInfo(token,tableId,formId,typeName);
    }

	@PostMapping("/fileupload")
    @Operation(summary="审批节点附件上传接口")
    public JsonBean fileupload(HttpServletRequest request,
    		@Parameter(name="file",description="附件上传实体",required=true)MultipartFile[] file,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowTaskOperatorId",description="通过时传入当前审批节点Id",required=true) @RequestParam(value = "flowTaskOperatorId", required = true) String flowTaskOperatorId,
    		@Parameter(name="flowTaskId",description="flowTaskInfo主键Id",required=true) @RequestParam(value = "flowTaskId", required = true) String flowTaskId
    		) throws Exception {
		return this.ymBusinessService.fileUpload(file,token,flowTaskOperatorId,flowTaskId);
    }


	@PostMapping("/fileuploadZH")
    @Operation(summary="审批节点附件上传接口(中核密标版)")
    public JsonBean fileuploadZH(HttpServletRequest request,
    		@Parameter(name="file",description="附件上传实体",required=true)MultipartFile[] file,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowTaskOperatorId",description="通过时传入当前审批节点Id",required=true) @RequestParam(value = "flowTaskOperatorId", required = true) String flowTaskOperatorId,
    		@Parameter(name="flowTaskId",description="flowTaskInfo主键Id",required=true) @RequestParam(value = "flowTaskId", required = true) String flowTaskId
    		) throws Exception {
		return this.ymBusinessService.fileuploadZH(file,token,flowTaskOperatorId,flowTaskId);
    }

	/**
     * @description 根据ATTID  下载文件
     * @author lyz
     * @date 2022/4/18 17:58
     */
    @RequestMapping(value = "/fildDownload", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="审批节点附件下载接口")
    public void fileDownLoad(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="attId",description="附件主键ID",required=true) @RequestParam("attId") String attId) throws Exception {
    	this.ymBusinessService.fildDownload(token,attId,response);
    }


    /**
     * @description 根据ATTID  下载文件
     * @author lyz
     * @date 2022/4/18 17:58
     */
    @RequestMapping(value = "/fildDownloadZH", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="审批节点附件下载接口(中核密标版)")
    public void fileDownLoad(HttpServletResponse response,
            @Parameter(name = "fileId", description = "文件ID", required = true) @RequestParam("fileId") String fileId) throws Exception{
    	ymBusinessService.fileDownLoadZH(response, fileId,true);
	}

    /**
     * @description 附件删除
     */
    @RequestMapping(value = "/fileRemove", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="审批节点附件删除接口")
    public JsonBean fileRemove(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="attId",description="附件主键ID",required=true) @RequestParam("attId") String attId) throws Exception {
    	return this.ymBusinessService.fileRemove(token,attId);
    }


    @RequestMapping(value = "/fileList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="审批节点获取附件接口")
    public JsonBean fileList(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowTaskOperatorId",description="通过时传入当前审批节点Id，查询该审批节点额所有附件信息",required=false) @RequestParam(value = "flowTaskOperatorId", required = false) String flowTaskOperatorId,
    		@Parameter(name="flowTaskId",description="flowTaskInfo主键Id，查询当前流程信息的所有附件",required=false) @RequestParam(value = "flowTaskId", required = false) String flowTaskId) throws Exception {
    	return this.ymBusinessService.fileList(token,flowTaskOperatorId,flowTaskId);
    }

    @RequestMapping(value = "/getFlowMessage", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="发起人定时获取流程办理结果信息")
    public JsonBean getFlowMessage(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
    	return this.ymBusinessService.getFlowMessage(token);
    }

    @RequestMapping(value = "/press", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="发起人催办功能接口")
    public JsonBean press(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="id",description="主键Id",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="主键Id",required=true) @RequestParam(value = "flowId", required = true) String flowId) throws Exception {
        return this.ymBusinessService.press(token,id,flowId);
    }

    @RequestMapping(value = "/loginGetPress", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="发起人登录获取未处理的催办信息")
    public JsonBean loginGetPress(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
        return this.ymBusinessService.loginGetPress(token);
    }

    @RequestMapping(value = "/getPressInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
   	@Operation(summary="发起人定时获取未处理的催办信息")
    public JsonBean getPressInfo(HttpServletRequest reques,
       		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token) throws Exception {
           return this.ymBusinessService.getPressInfo(token);
    }

    @RequestMapping(value = "/saveFlowTemplate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程审批意见模板保存接口")
    public JsonBean saveFlowTemplate(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="taskNodeId",description="流程审批节点主键ID",required=true) @RequestParam(value = "taskNodeId", required = true) String taskNodeId,
    		@Parameter(name="flowId",description="流程信息ID",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="tempTitle",description="模板标题",required=true) @RequestParam(value = "tempTitle", required = true) String tempTitle,
    		@Parameter(name="tempMemo",description="模板内容",required=true) @RequestParam(value = "tempMemo", required = true) String tempMemo) throws Exception {
        return this.ymBusinessService.saveFlowTemplate(token,taskNodeId,flowId,tempTitle,tempMemo);
    }

    @RequestMapping(value = "/modifyFlowTemplate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程审批意见模板修改接口")
    public JsonBean modifyFlowTemplate(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tempTitle",description="模板标题",required=true) @RequestParam(value = "tempTitle", required = true) String tempTitle,
    		@Parameter(name="tempMemo",description="模板内容",required=true) @RequestParam(value = "tempMemo", required = true) String tempMemo,
    		@Parameter(name="tempId",description="模板主键",required=true) @RequestParam(value = "tempId", required = true) BigDecimal tempId) throws Exception {
        return this.ymBusinessService.modifyFlowTemplate(token,tempTitle,tempMemo,tempId);
    }

    @RequestMapping(value = "/removeFlowTemplate", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程审批意见模板删除接口")
    public JsonBean removeFlowTemplate(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tempId",description="模板内容",required=true) @RequestParam(value = "tempId", required = true) BigDecimal tempId) throws Exception {
        return this.ymBusinessService.removeFlowTemplate(token,tempId);
    }

    @RequestMapping(value = "/getFlowTemplateList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程审批意见模板获取接口")
    public JsonBean getFlowTemplateList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowId",description="流程信息ID",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="taskNodeId",description="流程审批节点主键ID",required=false) @RequestParam(value = "taskNodeId", required = false) String taskNodeId,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="tempTitle",description="模板标题",required=false) @RequestParam(value = "tempTitle", required = false) String tempTitle,
    		@Parameter(name="tempMemo",description="模板内容",required=false) @RequestParam(value = "tempMemo", required = false) String tempMemo) throws Exception {
        return this.ymBusinessService.getFlowTemplateList(token,taskNodeId,flowId,currentPage,pageSize,tempTitle,tempMemo);
    }

    @RequestMapping(value = "/getFlowTemplateInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程审批意见模板删除接口")
    public JsonBean getFlowTemplateInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tempId",description="模板主键",required=true) @RequestParam(value = "tempId", required = true) BigDecimal tempId) throws Exception {
        return this.ymBusinessService.getFlowTemplateInfo(token,tempId);
    }


    @RequestMapping(value = "/getFLowInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程审批意见模板删除接口")
    public JsonBean getFLowInfo(HttpServletRequest reques,
    		@Parameter(name="id",description="模板主键",required=true) @RequestParam(value = "id", required = true) String id) throws Exception {
        return this.ymBusinessService.getFLowInfo(id);
    }

    @RequestMapping(value = "/batchList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="批量审批- 列表数据")
    public JsonBean batchList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "pageNumber", required = false,defaultValue = "1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false,defaultValue = "20") Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymBusinessService.batchList(token,currentPage,pageSize,flowName);
    }

    @RequestMapping(value = "batchCandidate", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="批量审批-通过、拒绝时获取下一步审批节点")
    public JsonBean batchCandidate(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowId",description="流程引擎主键",required=false) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="id",description="选择第一条数据的id",required=false) @RequestParam(value = "id", required = true) String id) throws Exception {
        return this.ymBusinessService.batchCandidate(token,flowId,id);
    }

    @OperationLog(
            success = "通过时获取下一步候选人",
            busType = "流程审核",
            fail = "通过时获取下一步候选人",
            operationType = OperationType.SELECT,
            subType = "我的待办"
    )
    @RequestMapping(value = "/batchCandidateUser", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量审批-通过时获取下一步候选人")
    public JsonBean batchCandidateUser(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false,defaultValue = "1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页记录数",required=false) @RequestParam(value = "pageSize", required = false,defaultValue = "20") Integer pageSize,
    		@Parameter(name="id",description="流程办理节点主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowId",description="流程引擎主键",required=false) @RequestParam(value = "flowId", required = false) String flowId,
    		@Parameter(name="nodeCode",description="节点编号",required=true) @RequestParam(value = "nodeCode", required = true) String nodeCode) throws Exception {
        return this.ymBusinessService.batchCandidateUser(token,id,nodeCode,flowId,currentPage,pageSize);
    }

    @RequestMapping(value = "/batchOperation", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="批量审批-批量操作（通过）")
    public JsonBean batchOperation(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@RequestBody FlowModel flowModel) throws Exception {

        return this.ymBusinessService.batchOperation(token,flowModel);
    }

}
