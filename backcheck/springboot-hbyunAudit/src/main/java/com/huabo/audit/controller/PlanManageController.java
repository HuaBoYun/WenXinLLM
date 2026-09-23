package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONArray;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjAuditplan;
import com.huabo.audit.oracle.entity.TblNbsjPlanProject;
import com.huabo.audit.oracle.vo.TblNbsjAuditPlanVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblNbsjAuditplanService;
import com.huabo.audit.service.TblNbsjPlanProjectService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计计划管理控制器
 * <p>提供审计计划的列表查询、新增、修改、删除、审批等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="审计计划管理",description="审计计划管理")
@RequestMapping(value = "/auditPlan")
public class PlanManageController {
    /**
     * @description 计划管理列表
     * @author lyz
     * @date 2022/4/15 9:36
     */
    @Autowired
    public TblNbsjAuditplanService tblNbsjAuditplanService;
    
    @Resource
    public TblNbsjPlanProjectService tblNbsjPlanProjectService;
    
    @Resource
    public AttachmentService attachmentService;
    
    @Resource
    public TblNbsjProjectService tblNbsjProjectService;

	@OperationLog(
			success = "审计计划管理查询",
			busType = "智能审计",
			fail = "审计计划管理查询",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计计划管理查询列表"
	)
    @GetMapping("/getAuditPlanPageList")
    @Operation(summary = "审计计划管理-计划管理列表分页功能")
    public JsonBean getAuditPlanPageList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
   		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
   		@Parameter(name = "planStartDate", description = "计划开始时间 格式年-月-日", required = false)@RequestParam(value = "planStartDate", required = false) String planStartDate,
   		@Parameter(name = "planEndDate", description = "计划结束时间 格式年-月-日", required = false)@RequestParam(value = "planEndDate", required = false) String planEndDate,
   		TblNbsjAuditPlanVo TblNbsjAuditPlanVo) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjAuditplanService.planManagePageList(token,pageNumber,pageSize,TblNbsjAuditPlanVo,planStartDate,planEndDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

    /**
     * @description 计划管理详情
     * @author lyz
     * @date 2022/4/15 9:37
     */
	@OperationLog(
			success = "审计计划管理，根据计划主键查找计划信息",
			busType = "智能审计",
			fail = "审计计划管理，根据计划主键查找计划信息",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计计划管理，根据计划主键查找计划信息"
	)
    @GetMapping("/getAuditPlanInfo")
    @Operation(summary = "审计计划管理-根据计划主键查找计划信息")
    public JsonBean getAuditPlanInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planid", description = "审计计划主键", required = true)@RequestParam(value = "planid", required = true) BigDecimal planid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjAuditplanService.findNbsjAuditPlanDetail(token,planid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

    /**
     * @description 删除计划管理
     * @author lyz
     * @date 2022/4/15 10:15
     */
	@OperationLog(
			success = "审计计划管理，根据计划主键查找计划信息",
			busType = "智能审计",
			fail = "审计计划管理，根据计划主键查找计划信息",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计计划管理，根据计划主键查找计划信息"
	)
    @GetMapping("/deleteAuditPlanByPlanId")
    @Operation(summary = "审计计划管理-根据计划主键删除计划信息")
    public JsonBean deleteAuditPlanByPlanId(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planid", description = "审计计划主键", required = true)@RequestParam(value = "planid", required = true) BigDecimal planId) {
        
        try {
			return tblNbsjAuditplanService.deletePlanManageByPlanId(planId, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	//mergePlanProjectManageInfo

	@OperationLog(
			success = "审计计划修改",
			busType = "智能审计",
			fail = "审计计划修改",
			operationType = OperationType.ADD,
			subType = "项目管理——计划管理——审计计划管理-修改/保存审计计划"
	)
    @RequestMapping(value = "/mergeAuditPlanInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计计划管理-修改或保存审计计划")
	public JsonBean mergeAuditPlanInfo(HttpServletRequest request,@Parameter(name = "plan", description = "审计计划实体", required = true)TblNbsjAuditplan plan,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "planStartTime", description = "计划开始时间", required = false) @RequestParam(value = "planStartTime", required = false) String planStartTime,
			 @Parameter(name = "planEndTime", description = "计划完成时间", required = false) @RequestParam(value="planEndTime", required = false) String planEndTime,
			 @Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value="attIds", required = false) String attIds)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.mergePlanManageInfo(plan,token,planStartTime,planEndTime,attIds);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
	@OperationLog(
			success = "审计计划项目新增/修改",
			busType = "智能审计",
			fail = "审计计划项目新增/修改",
			operationType = OperationType.ADD,
			subType = "项目管理——计划管理——审计计划管理-审计计划中计划项目新增或修改"
	)
    @RequestMapping(value = "/mergePlanProjectManageInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计计划管理-审计计划中计划项目新增或修改")
	public JsonBean mergePlanProjectManageInfo(HttpServletRequest request,@Parameter(name = "project", description = "审计计划项目实体", required = true)TblNbsjPlanProject project,
			 @Parameter(name = "plancode", description = "审计计划编码", required = false) @RequestParam(value = "plancode", required = false) String plancode,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjPlanProjectService.mergePlanProjectManageInfo(project, project.getPlanid(),plancode,token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}


	@OperationLog(
			success = "计划项目查询",
			busType = "智能审计",
			fail = "审计计划项目查询",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理—审计计划中计划项目查询"
	)
	@RequestMapping(value = "/mergePlanProjectManageInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
   	@Operation(summary = "审计计划管理-审计计划中计划项目查询")
   	public JsonBean mergePlanProjectManageInfoList(HttpServletRequest request,
   			 @Parameter(name = "planId", description = "审计计划主键 ，如果主键为空则根据编码新增审计计划信息", required = false) @RequestParam(value = "planId", required = false) String planId,
   			 @Parameter(name = "plancode", description = "审计计划编码", required = true) @RequestParam(value = "plancode", required = true) String plancode,
   			 @Parameter(name = "projectListJson", description = "审计计划编码", required = true) @RequestParam(value = "projectListJson", required = true) String projectListJson,
   			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
   		 JsonBean jsonBean = null;
   			try {
   			 List<TblNbsjPlanProject> projectList = JSONArray.parseArray(projectListJson,TblNbsjPlanProject.class);
   				jsonBean = this.tblNbsjPlanProjectService.mergePlanProjectManageInfoList(projectList, planId,plancode,token);
   			} catch (Exception e) {
   				e.printStackTrace();
   				ResponseFormat.retParam(0,1000,e.getMessage());
   			}
   			return jsonBean;
   	}

	@OperationLog(
			success = "计划项目删除",
			busType = "智能审计",
			fail = "审计计划项目删除",
			operationType = OperationType.DELETE,
			subType = "项目管理——计划管理——审计计划中计划项目删除"
	)
    @RequestMapping(value = "/removePlanProjectInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计计划管理-审计计划中计划项目删除")
	public JsonBean removePlanProjectInfo(HttpServletRequest request,
			 @RequestParam(value = "planprojectid", required = true)@Parameter(name = "planprojectid", description = "审计计划项目主键", required = true)BigDecimal planprojectid,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjPlanProjectService.removePlanProjectInfo(token,planprojectid);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
	@OperationLog(
			success = "计划项目集合查询",
			busType = "智能审计",
			fail = "审计计划项目集合查询",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——通过计划主键获取计划项目集合"
	)
    @RequestMapping(value = "/getPlanProjectListByPlanId", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "审计计划管理，通过计划主键获取计划项目集合")
	public JsonBean getPlanProjectListByPlanId(HttpServletRequest request,
			 @RequestParam(value = "planId", required = true)@Parameter(name = "planId", description = "审计计划主键", required = true)BigDecimal planId,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjPlanProjectService.findPlanProjectListInfoByPlanId(token,planId);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}

	@OperationLog(
			success = "计划管理——项目导出",
			busType = "智能审计",
			fail = "计划管理——项目导出",
			operationType = OperationType.EXPORT,
			subType = "项目管理——计划管理——导出项目管理"
	)
	@RequestMapping(value = "/exportPlanProjectListByPlanId",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@Operation(summary = "计划管理-导出项目管理")
	public void project_standard_dg_export(HttpServletRequest request, HttpServletResponse response,
			 @RequestParam(value = "planId", required = true)@Parameter(name = "planId", description = "审计计划主键", required = true)BigDecimal planId,
			 @RequestParam(value = "ids", required = false)@Parameter(name = "ids", description = "项目管理id", required = false)String ids,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token) throws Exception {
		response.setContentType("application/binary;charset=UTF-8");
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + new String("项目管理".getBytes(), "UTF-8") + ".xlsx");// 组装附件名称和格式
			ServletOutputStream outputStream = response.getOutputStream();
			List<Object[]> objList1 =  this.tblNbsjPlanProjectService.exportPlanProjectListInfoByPlanId(token,planId,ids);
			String[] titles = { "项目名称", "工作目标 ", "审计类型", "计划完成时间", "被审计对象", "是否外委",  "项目单位名称",
			  "项目类型","批复总投资(经费:万元)", "批复的项目起止年限", "项目主管部门", "计划审计时间", "计划验收时间", "项目单位地址","项目联系人","联系电话"};
			ImportOrExportExcelUtil.exportExcel(titles, objList1, outputStream, null);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}





	@OperationLog(
			success = "获取计划附件列表",
			busType = "智能审计",
			fail = "获取计划附件列表",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——根据审计计划主键获取审计计划的附件"
	)
    @RequestMapping(value = "/getAuditPlanAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计计划管理，根据审计计划主键获取审计计划的附件")
    public JsonBean getAuditPlanAttInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planId", description = "审计计划主键 ", required = true) @RequestParam(value = "planId", required = false) BigDecimal planId) {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAuditplanService.getAuditPlanAttInfo(token,planId);
		} catch (Exception e) {
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
    }
    
    /**
     * @description 根据attid删除附件
     * @author lyz
     * @date 2022/4/19 9:04
     */
	@OperationLog(
			success = "计划附件删除",
			busType = "智能审计",
			fail = "计划附件删除",
			operationType = OperationType.DELETE,
			subType = "项目管理——计划管理——根据附件主键删除附件接口"
	)
    @GetMapping("/deleteFileById")
    @Operation(summary = "审计计划管理-根据附件主键删除附件接口")
    public R deleteFileById(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") BigDecimal attId) throws Exception {
    	return this.tblNbsjAuditplanService.removeAttInfoByAttId(token,attId);
    }

	@OperationLog(
			success = "查看分页列表",
			busType = "智能审计",
			fail = "查看分页列表",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计计划查看分页列表接口"
	)
    @RequestMapping(value = "/getAuditPlanViewInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计计划查看分页列表接口")
	public JsonBean getAuditPlanViewInfo(HttpServletRequest request,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			@Parameter(name = "planName", description = "查询条件-计划名称", required = false) @RequestParam(value = "planName", required = false) String planName,
			@Parameter(name = "planYear", description = "查询条件-计划年度", required = false) @RequestParam(value = "planYear", required = false) String planYear,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
			) {
    	
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjAuditplanService.getAuditPlanViewInfo(token,planName,planYear,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			ResponseFormat.retParam(0,1000,e.getMessage());
		}
		return jsonBean;
	}

	@OperationLog(
			success = "查看详情",
			busType = "智能审计",
			fail = "查看详情",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计计划查看获取审计计划详情"
	)
    @RequestMapping(value = "/getAuditPlanViewDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计计划查看获取审计计划详情")
    public JsonBean getAuditPlanViewDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "planid", description = "审计计划主键", required = true)@RequestParam(value = "planid", required = true) BigDecimal planid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjAuditplanService.findNbsjAuditPlanViewDetail(token,planid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }


	@OperationLog(
			success = "查看审计档案列表",
			busType = "智能审计",
			fail = "查看审计档案列表",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计档案列表页面"
	)
    @RequestMapping(value = "/getAuditFileInfoList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计档案列表页面")
    public JsonBean getAuditFileInfoList(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	   		@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
	   		@Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false) String projectName) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectService.getAuditFileInfoList(token,projectName,pageNumber,pageSize);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	@OperationLog(
			success = "获取详情页树形菜单",
			busType = "智能审计",
			fail = "获取详情页树形菜单",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——审计档案详情页面获取树形菜单信息"
	)
    @RequestMapping(value = "/getAuditFileDetailTreeInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计计划管理-审计档案详情页面获取树形菜单信息")
    public JsonBean getAuditFileDetailTreeInfo(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	   		@Parameter(name = "projectId", description = "项目主键", required = true)@RequestParam(value = "projectId", required = true) BigDecimal projectId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjProjectService.getAuditFileDetailTreeInfo(token,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    
    /**
     * 查询计划关联的项目
     */
	@OperationLog(
			success = "查询计划关联项目",
			busType = "智能审计",
			fail = "查询计划关联项目",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——查询计划关联的项目"
	)
    @RequestMapping(value = "/getProjectListByWspJhw", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "查询计划关联的项目")
	public JsonBean getProjectListByWspJhw(HttpServletRequest request,
			@RequestParam(value = "planId", required = true)@Parameter(name = "planId", description = "审计计划主键", required = true)BigDecimal planId,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjPlanProjectService.findProjectListInfoByWspJhw(token,planId);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    /**
     * 新增自动计划（计划外）
     */
	@OperationLog(
			success = "新增自动计划",
			busType = "智能审计",
			fail = "新增自动计划",
			operationType = OperationType.ADD,
			subType = "项目管理——计划管理——新增自动计划（计划外）"
	)
    @RequestMapping(value = "/addAutoPlan", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "新增自动计划（计划外）")
	public JsonBean addAutoPlan(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.addAutoPlan(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    
    //自动编号-审计通知
	@OperationLog(
			success = "获取编号",
			busType = "智能审计",
			fail = "获取编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-审计通知"
	)
    @RequestMapping(value = "/getAutoCodeBySjtz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-审计通知")
	public JsonBean getAutoCodeBySjtz(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjtz(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-疑点管理
	@OperationLog(
			success = "疑点编号",
			busType = "智能审计",
			fail = "获取编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-疑点管理"
	)
    @RequestMapping(value = "/getAutoCodeByYdgl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-疑点管理")
	public JsonBean getAutoCodeByYdgl(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByYdgl(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-我的底稿
	@OperationLog(
			success = "底稿编号",
			busType = "智能审计",
			fail = "底稿编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-我的底稿"
	)
    @RequestMapping(value = "/getAutoCodeByWddg", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-我的底稿")
	public JsonBean getAutoCodeByWddg(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByWddg(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-审计建议书
	@OperationLog(
			success = "审计建议书编号",
			busType = "智能审计",
			fail = "审计建议书编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-审计建议书"
	)
    @RequestMapping(value = "/getAutoCodeBySjjys", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-审计建议书")
	public JsonBean getAutoCodeBySjjys(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjjys(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-缺陷管理
	@OperationLog(
			success = "缺陷管理编号",
			busType = "智能审计",
			fail = "缺陷管理编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-缺陷管理"
	)
    @RequestMapping(value = "/getAutoCodeByQxgl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-缺陷管理")
	public JsonBean getAutoCodeByQxgl(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByQxgl(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-风险发现
	@OperationLog(
			success = "风险发现编号",
			busType = "智能审计",
			fail = "风险发现编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-风险发现"
	)
    @RequestMapping(value = "/getAutoCodeByFxfx", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-风险发现")
	public JsonBean getAutoCodeByFxfx(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByFxfx(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-整改方案
	@OperationLog(
			success = "整改方案编号",
			busType = "整改追责",
			fail = "整改方案编号",
			operationType = OperationType.SELECT,
			subType = "整改跟踪-整改方案-获取自动编号"
	)
    @RequestMapping(value = "/getAutoCodeByZgfa", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-整改方案")
	public JsonBean getAutoCodeByZgfa(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByZgfa(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-拟实施审计指引
	@OperationLog(
			success = "拟实施审计指引编号",
			busType = "智能审计",
			fail = "拟实施审计指引编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-拟实施审计指引"
	)
    @RequestMapping(value = "/getAutoCodeBySjmb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-拟实施审计指引")
	public JsonBean getAutoCodeBySjmb(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjmb(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-审计指引模板库
	@OperationLog(
			success = "审计指引模板库编号",
			busType = "智能审计",
			fail = "审计指引模板库编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-审计指引模板库"
	)
    @RequestMapping(value = "/getAutoCodeBySjzy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-审计指引模板库")
	public JsonBean getAutoCodeBySjzy(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeBySjzy(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-管理制度
	@OperationLog(
			success = "管理制度编号",
			busType = "智能审计",
			fail = "管理制度编号",
			operationType = OperationType.SELECT,
			subType = "管理制度-获取自动编号-管理制度"
	)
    @RequestMapping(value = "/getAutoCodeByGlzd", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-管理制度")
	public JsonBean getAutoCodeByGlzd(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByGlzd(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-项目资料
	@OperationLog(
			success = "项目资料编号",
			busType = "智能审计",
			fail = "项目资料编号",
			operationType = OperationType.SELECT,
			subType = "项目管理——计划管理——获取自动编号-项目资料"
	)
    @RequestMapping(value = "/getAutoCodeByXmzl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-项目资料")
	public JsonBean getAutoCodeByXmzl(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByXmzl(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-审计模板
	@OperationLog(
			success = "审计模板编号",
			busType = "智能审计",
			fail = "审计模板编号",
			operationType = OperationType.SELECT,
			subType = "基础配置-审计模板-获取自动编号"
	)
    @RequestMapping(value = "/getAutoCodeByNewSjmb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-审计模板")
	public JsonBean getAutoCodeByNewSjmb(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByNewSjmb(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-审计经验库
	@OperationLog(
			success = "审计经验库编号",
			busType = "智能审计",
			fail = "审计经验库编号",
			operationType = OperationType.SELECT,
			subType = "基础配置-审计经验库-获取自动编号"
	)
    @RequestMapping(value = "/getAutoCodeByNewSjjyk", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "自动编号-审计经验库")
	public JsonBean getAutoCodeByNewSjjyk(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = this.tblNbsjAuditplanService.getAutoCodeByNewSjjyk(token);
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    //自动编号-项目管理
	@OperationLog(
			success = "项目管理编号",
			busType = "智能审计",
			fail = "项目管理编号",
			operationType = OperationType.SELECT,
			subType = "项目管理-项目管理-获取自动编号"
	)
    @RequestMapping(value = "/getAutoCodeByXmgl", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "自动编号-项目管理")
    public JsonBean getAutoCodeByXmgl(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token
                                      ,@Parameter(name = "planid", description = "审计计划主键", required = true) @RequestParam(value = "planid", required = true) BigDecimal planId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByXmgl(token,planId);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    //自动编号-计划管理
	@OperationLog(
			success = "计划管理编号",
			busType = "智能审计",
			fail = "计划管理编号",
			operationType = OperationType.SELECT,
			subType = "项目管理-计划管理-获取自动编号"
	)
    @RequestMapping(value = "/getAutoCodeByJhgl", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "自动编号-计划管理")
    public JsonBean getAutoCodeByJhgl(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditplanService.getAutoCodeByJhgl(token);
        } catch (Exception e) {
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

	//计划管理——导入数据
/*
	@PostMapping("/import")
	@Operation(summary = "计划列表——导入")
	public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	) throws IOException {
		InputStream in = file.getInputStream();
		XSSFWorkbook workBook = new XSSFWorkbook(in);
		System.out.println("到此位置没有问题");
		try{

			for (int i =0; i < workBook.getNumberOfSheets(); i++){
				XSSFSheet sheet = workBook.getSheetAt(i);
				tblNbsjAuditplanService.resolveSheet(sheet,token);
			}
			return ResponseFormat.retParam(1,200);
		}catch (Exception e){
			e.printStackTrace();
			log.error("智能审计——计划管理 列表导入失败",e );
			return  ResponseFormat.retParam(0,1000,e.getMessage());
		}finally {
			//读取完毕则关闭流
			in.close();
			workBook.close();
		}
	}
*/
	@OperationLog(
			success = "计划导入",
			busType = "智能审计",
			fail = "计划导入",
			operationType = OperationType.IMPORT,
			subType = "项目管理——计划管理——计划列表——导入"
	)
	@PostMapping("/import")
	@Operation(summary = "计划列表——导入")
	public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	) throws IOException {
		InputStream in = file.getInputStream();
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(in);
        } catch (InvalidFormatException e) {
            throw new RuntimeException(e);
        }
//        System.out.println("到此位置没有问题");
		try {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				tblNbsjAuditplanService.resolveSheet(sheet, token);
			}
			return ResponseFormat.retParam(1, 200);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("智能审计——计划管理 列表导入失败", e);
			return ResponseFormat.retParam(0, 1000, e.getMessage());
		} finally {
			// 读取完毕则关闭流
			in.close();
			workbook.close();
		}
	}
	@OperationLog(
			success = "计划项目导入",
			busType = "智能审计",
			fail = "计划项目导入",
			operationType = OperationType.IMPORT,
			subType = "项目管理——计划管理——审计计划中计划项目导入"
	)
	@RequestMapping(value = "/mergePlanProjectManageInfoImport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "审计计划管理-审计计划中计划项目导入")
	public JsonBean mergePlanProjectManageInfoImport(HttpServletRequest request,
													 @Parameter(name = "file", description = "要导入的文件", required = true) MultipartFile file,
													 @Parameter(name = "planid", description = "审计计划项目ID", required = false)BigDecimal planid,
													 @Parameter(name = "plancode", description = "审计计划编码", required = false) @RequestParam(value = "plancode", required = false) String plancode,
													 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
//		JsonBean jsonBean = null;
		InputStream in = file.getInputStream();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
		try {
			List<TblNbsjPlanProject> list = new ArrayList<>();
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);
				List<TblNbsjPlanProject> lists = tblNbsjAuditplanService.resolveProjectSheet(sheet, token, planid, plancode);
				if (lists.size() > 0) {
					list =lists;
				}

			}
			return ResponseFormat.retParam(1, 200,list);
//			return ResponseFormat.retParam(1, 200);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("智能审计——计划管理——项目列表 列表导入失败", e);
			return ResponseFormat.retParam(0, 1000, e.getMessage());
		} finally {
			// 读取完毕则关闭流
			in.close();
			workbook.close();
		}
//		try {
//			jsonBean = this.tblNbsjPlanProjectService.mergePlanProjectManageInfo(project, planid,plancode,token);
//		} catch (Exception e) {
//			ResponseFormat.retParam(0,1000,e.getMessage());
//		}
//		return jsonBean;
	}


}
