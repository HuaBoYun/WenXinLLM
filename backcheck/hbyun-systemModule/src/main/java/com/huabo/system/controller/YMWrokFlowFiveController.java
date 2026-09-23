package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.system.service.YMBusinessService;
import com.huabo.system.service.YMWorkEngineFiveService;
import com.huabo.system.util.OrganizeAdminIsTratorCrForm;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 业务中台工作流控制器
 * <p>提供业务中台5.0的工作流信息保存、字典数据查询、流程模板查询等接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "业务中台工作流Controller", description = "业务中台5.0的工作流Controoler")
@RequestMapping("/ywztWork")
public class YMWrokFlowFiveController {

	@Resource
	public YMWorkEngineFiveService ymWorkEngineFiveService;

	@RequestMapping(value = "/saveFlowInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程设计-保存流程信息")
    public JsonBean saveFlowInfo(HttpServletRequest reques,
    		@Parameter(name="tableid",description="业务模块主键",required=true) @RequestParam(value = "tableid", required = true) BigDecimal tableid,
    		@Parameter(name="typeId",description="合同类型ID",required=false) @RequestParam(value = "typeId", required = false) String typeid,
    		@Parameter(name="enCode",description="流程编号",required=true) @RequestParam(value = "enCode", required = true) String enCode,
    		@Parameter(name="fullName",description="流程名称",required=true) @RequestParam(value = "fullName", required = true) String fullName,
    		@Parameter(name="category",description="流程类型主键",required=true) @RequestParam(value = "category", required = true) String category,
    		@Parameter(name="description",description="流程描述",required=false) @RequestParam(value = "description", required = false) String description,
    		@Parameter(name="id",description="流程主键",required=false) @RequestParam(value = "id", required = false) String id,
    		@Parameter(name="flowType",description="流程类型：0-标准流程 1-简单流程 2-任务流程，默认0",required=false) @RequestParam(value = "flowType", required = false, defaultValue = "0") Integer flowType) throws Exception {
        return this.ymWorkEngineFiveService.saveFlowInfoFive(description,enCode,fullName,id,tableid,typeid,category,flowType);
    }

	@RequestMapping(value = "/getDictionaryData", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程设计-获取流程类型信息")
    public JsonBean getDictionaryData(HttpServletRequest request) throws Exception {
        return this.ymWorkEngineFiveService.getDictionaryData();
    }

	@RequestMapping(value = "/getFLowTemplateInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程设计-获取流程类型信息")
    public JsonBean getFLowTemplateInfo(HttpServletRequest request,
    		@Parameter(name="id",description="流程模板主键",required=true) @RequestParam(value = "id", required = true) String id) throws Exception {
        return this.ymWorkEngineFiveService.getFLowTemplateInfo(id);
    }


	@OperationLog(
            success = "程设计-发布后保存流程信息",
            busType = "流程保存",
            fail = "程设计-发布后保存流程信息",
            operationType = OperationType.UPDATE,
            subType = "流程设计"
    )
	@RequestMapping(value = "/saveWorkFlowFormInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程设计-发布后保存流程信息")
    public JsonBean saveWorkFlowEngineInfo(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="flowId",description="流程主键",required=true) @RequestParam(value = "flowId", required = true) String flowId,
    		@Parameter(name="id",description="流程基础信息主键",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="typeId",description="合同类型ID",required=false) @RequestParam(value = "typeId", required = false) String typeId,
		@Parameter(name="tableId",description="表单ID",required=true) @RequestParam(value = "tableId", required = true) String tableId) throws Exception {
        return this.ymWorkEngineFiveService.saveWorkFlowEngineInfo(token,flowId,tableId,id,typeId);
	}

	@RequestMapping(value = "/transact", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程引擎-开始办理")
    public JsonBean transact(HttpServletRequest reques,
    		@Parameter(name="ids",description="流程OperatorId（id）数组",required=true) @RequestParam(value = "ids", required = true) String[] ids) throws Exception {
        return this.ymWorkEngineFiveService.transact(ids);
	}

	@RequestMapping(value = "/sendBack", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程引擎-退回")
    public JsonBean sendBack(HttpServletRequest reques,
    		@Parameter(name="backNodeCode",description="退回节点编号",required=true) @RequestParam(value = "backNodeCode", required = true) String backNodeCode,
    		@Parameter(name="backNodeName",description="退回节点名称",required=true) @RequestParam(value = "backNodeName", required = true) String backNodeName,
    		@Parameter(name="handleOpinion",description="审批意见",required=false) @RequestParam(value = "handleOpinion", required = false,defaultValue = "无") String handleOpinion,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="copyIds",description="抄送人主键多个用,分割",required=false) @RequestParam(value = "copyIds", required = false) String copyIds,
    		@Parameter(name="candidateList",description="候选人列表",required=false) @RequestParam(value = "candidateList", required = false) String candidateList,
    		@Parameter(name="backType",description="退回类型 1-重新走  2-直接提交给我",required=true) @RequestParam(value = "backType", required = true , defaultValue = "1") String backType,
    		@Parameter(name="id",description="流程OperatorId",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowid",description="流程引擎主键",required=true) @RequestParam(value = "flowid", required = true) String flowid,
    		@Parameter(name="taskid",description="流程任务主键",required=true) @RequestParam(value = "taskid", required = true) String taskid) throws Exception {
        return this.ymWorkEngineFiveService.sendBack(id,flowid,backNodeCode,handleOpinion,signImg,copyIds,candidateList,backType,taskid,backNodeName);
	}

	@RequestMapping(value = "/assist", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程引擎-协办")
    public JsonBean assist(HttpServletRequest reques,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="handleOpinion",description="审批意见",required=false) @RequestParam(value = "handleOpinion", required = false,defaultValue = "无") String handleOpinion,
    		@Parameter(name="nodeCode",description="审批节点",required=false) @RequestParam(value = "nodeCode", required = false) String nodeCode,
    		@Parameter(name="pause",description="状态",required=false) @RequestParam(value = "pause", required = false,defaultValue = "0") String pause,
    		@Parameter(name="handleIds",description="协办人主键(星光系统)多个用,分割",required=true) @RequestParam(value = "handleIds", required = true) String handleIds,
    		@Parameter(name="id",description="流程OperatorId",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowid",description="流程引擎主键",required=true) @RequestParam(value = "flowid", required = true) String flowid,
    		@Parameter(name="taskid",description="流程任务主键",required=true) @RequestParam(value = "taskid", required = true) String taskid) throws Exception {
        return this.ymWorkEngineFiveService.assist(id,flowid,handleIds,handleOpinion,signImg,nodeCode,pause,taskid);
	}

	@RequestMapping(value = "/addSign", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程引擎-加签")
    public JsonBean addSign(HttpServletRequest reques,
    		@Parameter(name="id",description="流程OperatorId",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowid",description="流程引擎主键",required=true) @RequestParam(value = "flowid", required = true) String flowid,
    		@Parameter(name="taskid",description="流程任务主键",required=true) @RequestParam(value = "taskid", required = true) String taskid,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="handleOpinion",description="审批意见",required=false) @RequestParam(value = "handleOpinion", required = false,defaultValue = "无") String handleOpinion,
    		@Parameter(name="addSignType",description="加签类型 1-加签前，2-加签后",required=true) @RequestParam(value = "addSignType", required = true) String addSignType,
    		@Parameter(name="counterSign",description="0：或签 1：会签 ",required=true) @RequestParam(value = "counterSign", required = true) Integer counterSign,
    		@Parameter(name="auditRatio",description="会签比例",required=false) @RequestParam(value = "auditRatio", required = false) Integer auditRatio,
    		@Parameter(name="addSignUserIdList",description="加签人主键多个用,分割 集合",required=true) @RequestParam(value = "addSignUserIdList", required = true) String addSignUserIds
    		) throws Exception {
        return this.ymWorkEngineFiveService.addSign(id,flowid,taskid,signImg,handleOpinion,addSignType,counterSign,auditRatio,addSignUserIds);
	}

	@RequestMapping(value = "/transfer", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程引擎-转审")
    public JsonBean transfer(HttpServletRequest reques,
    		@Parameter(name="id",description="流程OperatorId",required=true) @RequestParam(value = "id", required = true) String id,
    		@Parameter(name="flowid",description="流程引擎主键",required=true) @RequestParam(value = "flowid", required = true) String flowid,
    		@Parameter(name="taskid",description="流程任务主键",required=true) @RequestParam(value = "taskid", required = true) String taskid,
    		@Parameter(name="signImg",description="电子签名",required=false) @RequestParam(value = "signImg", required = false) String signImg,
    		@Parameter(name="handleOpinion",description="审批意见",required=false) @RequestParam(value = "handleOpinion", required = false,defaultValue = "无") String handleOpinion,
    		@Parameter(name="handleIds",description="转审人主键，多个用,分割",required=true) @RequestParam(value = "handleIds", required = true) String handleIds,
    		@Parameter(name="nodeCode",description="当前节点编号",required=false) @RequestParam(value = "nodeCode", required = false) String nodeCode,
    		@Parameter(name="pause",description="操作类型",required=false) @RequestParam(value = "pause", required = false,defaultValue = "0") Integer pause
    		) throws Exception {
        return this.ymWorkEngineFiveService.transfer(id,flowid,taskid,signImg,handleOpinion,handleIds,nodeCode,pause);
	}


	@RequestMapping(value = "/getInProgressList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的在办-列表信息")
    public JsonBean getInProgressList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymWorkEngineFiveService.getInProgressList(token,currentPage,pageSize,flowName);
    }

	@RequestMapping(value = "/getPendingSignList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="我的代签-列表信息")
    public JsonBean getPendingSignList(HttpServletRequest reques,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false) Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false) Integer pageSize,
    		@Parameter(name="flowName",description="查询条件-流程标题",required=false) @RequestParam(value = "flowName", required = false) String flowName) throws Exception {
        return this.ymWorkEngineFiveService.getPendingSignList(token,currentPage,pageSize,flowName);
    }


	@RequestMapping(value = "/getAdministratorList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程设计-获取管理员列表信息")
    public JsonBean getAdministratorList(HttpServletRequest request,
    		@Parameter(name="keyword",description="筛选条件-管理组名称",required=false) @RequestParam(value = "keyword", required = false) String keyword,
    		@Parameter(name="currentPage",description="当前页",required=false) @RequestParam(value = "currentPage", required = false ,defaultValue = "1") Integer currentPage,
    		@Parameter(name="pageSize",description="每页数量",required=false) @RequestParam(value = "pageSize", required = false , defaultValue = "20" ) Integer pageSize) throws Exception {
        return this.ymWorkEngineFiveService.getAdministratoList(keyword,currentPage,pageSize);
    }

	@RequestMapping(value = "/getAdministratoSelector", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程设计-新增管理员信息获取授权信息")
    public JsonBean getAdministratoSelector(HttpServletRequest request,
    		@Parameter(name="staffId",description="星光系统用户主键",required=true) @RequestParam(value = "staffId", required = true) String staffId) throws Exception {
        return this.ymWorkEngineFiveService.getAdministratoSelector(staffId);
    }

	@RequestMapping(value = "/saveAdministratorInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary="流程设计-保存管理信息")
    public JsonBean saveAdministratorInfo(HttpServletRequest request,
    		@RequestBody @Valid OrganizeAdminIsTratorCrForm organizeAdminIsTratorCrForm,
    		@Parameter(name="id",description="管理信息主键",required=false) @RequestParam(value = "id", required = false) String id) throws Exception {
        return this.ymWorkEngineFiveService.saveAdministratorInfo(organizeAdminIsTratorCrForm,id);
    }

	@RequestMapping(value = "/removeAdministratorInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary="流程设计-删除管理员信息")
    public JsonBean removeAdministratorInfo(HttpServletRequest request,
    		@Parameter(name="id",description="管理信息主键",required=true) @RequestParam(value = "id", required = true) String id) throws Exception {
        return this.ymWorkEngineFiveService.removeAdministratorInfo(id);
    }
}
