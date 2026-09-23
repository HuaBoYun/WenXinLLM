package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.entity.ImplementPlanTeamEntity;
import com.huabo.audit.oracle.vo.TblNbsjOperateVo;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.service.TblNbsjOperateService;
import com.huabo.audit.service.TblYqnsJhglJhGLService;
import com.huabo.audit.service.TblYqnsJhglJhcgGLService;
import com.huabo.audit.vo.result.QualityParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ImplementPlanController
 * @Description 项目管理-实施方案
 * @DATE 2023/10/27
 */
@RestController
@Slf4j
@Tag(name="项目管理-实施方案",description="项目管理-实施方案")
@RequestMapping(value = "/project/implementPlan")
public class ImplementPlanController {
	
    @Autowired
    private ImplementPlanService implementPlanService;
    
    @Resource
    TblYqnsJhglJhcgGLService tblYqnsJhglJhcgGLService;
    
    
    @Resource
    public TblNbsjOperateService tblNbsjOperateService;
    
    
    @Resource
    TblYqnsJhglJhGLService tblYqnsJhglJhGLService;

    @GetMapping("/getList")
    @Operation(summary = "项目管理-实施方案列表")
    public JsonBean getList(HttpServletRequest request, 
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "projectOrderId", description = "项目负责人", required = false)@RequestParam(value = "projectOrderId", required = false, defaultValue = "") BigDecimal projectOrderId,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "xctype", description = "筛选类型:sjtzsp-审计通知审批选择项目", required = false)@RequestParam(value = "xctype", required = false, defaultValue = "") String xctype,
                            @Parameter(name = "planStarttime", description = "项目计划起始时间", required = false)@RequestParam(value = "planStarttime", required = false, defaultValue = "") String planStarttime,
                            @Parameter(name = "planEndtime", description = "项目计划结束时间", required = false)@RequestParam(value = "planEndtime", required = false, defaultValue = "") String planEndtime,
                            @Parameter(name = "xmnd", description = "筛选条件-项目年度结束", required = false) @RequestParam(value = "xmnd", required = false) Integer xmnd,
                            @Parameter(name = "spzt", description = "审批状态", required = false) @RequestParam(value = "spzt", required = false) String spzt,
                			@Parameter(name = "staffId", description = "筛选条件-人员主键", required = false) @RequestParam(value = "staffId", required = false) BigDecimal staffId
    ){
        JsonBean jsonBean = null;
        try{

            jsonBean = implementPlanService.findAll(token,pageNumber,pageSize,projectOrderId,projectName,planStarttime,planEndtime,staffId,xmnd, spzt,xctype);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

	@GetMapping("/getListcCompleted")
	@Operation(summary = "项目管理-实施方案状态已完成")
	public JsonBean getListcCompleted(HttpServletRequest request,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
							@Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
							@Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
							@Parameter(name = "projectOrderId", description = "项目负责人", required = false)@RequestParam(value = "projectOrderId", required = false, defaultValue = "") BigDecimal projectOrderId,
							@Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
							@Parameter(name = "planStarttime", description = "项目计划起始时间", required = false)@RequestParam(value = "planStarttime", required = false, defaultValue = "") String planStarttime,
							@Parameter(name = "planEndtime", description = "项目计划结束时间", required = false)@RequestParam(value = "planEndtime", required = false, defaultValue = "") String planEndtime,
							@Parameter(name = "xmnd", description = "筛选条件-项目年度结束", required = false) @RequestParam(value = "xmnd", required = false) Integer xmnd,
							@Parameter(name = "staffId", description = "筛选条件-人员主键", required = false) @RequestParam(value = "staffId", required = false) BigDecimal staffId
	){
		JsonBean jsonBean = null;
		try{

			jsonBean = implementPlanService.getListcCompleted(token,pageNumber,pageSize,projectOrderId,projectName,planStarttime,planEndtime,staffId,xmnd);
		}catch (Exception e){
			e.printStackTrace();
			jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
		}
		return jsonBean;
	}
    
    @GetMapping("/getReviewStatusList")
    @Operation(summary = "项目管理-实施方案审核情况")
    public JsonBean getReviewStatusList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "projectOrderId", description = "项目负责人", required = false)@RequestParam(value = "projectOrderId", required = false, defaultValue = "") BigDecimal projectOrderId,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "planStarttime", description = "项目计划起始时间", required = false)@RequestParam(value = "planStarttime", required = false, defaultValue = "") String planStarttime,
                            @Parameter(name = "planEndtime", description = "项目计划结束时间", required = false)@RequestParam(value = "planEndtime", required = false, defaultValue = "") String planEndtime
    ){
        JsonBean jsonBean = null;
        try{

            jsonBean = implementPlanService.getReviewStatusList(token,pageNumber,pageSize,projectOrderId,projectName,planStarttime,planEndtime);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/detail")
    @Operation(summary = "根据ID获取详细信息")
    public JsonBean getById(HttpServletRequest request,@Parameter(name = "id", description = "ID", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id){
        JsonBean jsonBean = null;
        try{ 
            jsonBean = implementPlanService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("/getteams")
    @Operation(summary = "根据ID获取项目组信息")
    public JsonBean getteams(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "id", description = "ID", required = false)@RequestParam(value = "id", required = false, defaultValue = "") BigDecimal id){
        JsonBean jsonBean = null;
        try{
            jsonBean = implementPlanService.findByIdTeams(token, id);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
 
    @PostMapping(value="/saveOrUpdate" , produces = "application/json; charset=utf-8")
    @Operation(summary = "添加或修改信息")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "implementPlanEntity", description = "实体", required = true) @RequestBody ImplementPlanEntity implementPlanEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != implementPlanEntity.getId()){
            	jsonBean=implementPlanService.updateEntity(implementPlanEntity);
            }else{
            	jsonBean=implementPlanService.saveEntity(token,implementPlanEntity);
            }
//            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        } 
        return jsonBean;
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除")
    public JsonBean deleteByIds(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "ids", description = "Id,多个用,号隔开", required = true) @RequestParam String ids
    ){
        JsonBean jsonBean = null;
        try{
            implementPlanService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    /**
     * 项目实施
     */
    @GetMapping("/xmproject_plan_cycurr")
    @Operation(summary = "项目实施")
    public JsonBean xmproject_plan_cycurr(HttpServletRequest request,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                          @Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) String projectid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = implementPlanService.projectSS(token,projectid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    /**
	 * 获取当前实施项目
	 */
	@GetMapping("/curr_ss_project")
    @Operation(summary = "获取当前实施项目")
    public JsonBean curr_ss_project(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = implementPlanService.findProjectDetail(token,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	
	
	 @GetMapping("/getRwfpList")
	    @Operation(summary = "项目管理-任务分配列表") 
	    public JsonBean getRwfpList(HttpServletRequest request,
	                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
	                            @Parameter(name = "projectOrderId", description = "项目负责人", required = false)@RequestParam(value = "projectOrderId", required = false, defaultValue = "") BigDecimal projectOrderId,
	                            @Parameter(name = "prjoectName", description = "项目名称", required = false)@RequestParam(value = "prjoectName", required = false, defaultValue = "") String prjoectName,
	                            @Parameter(name = "planStarttime", description = "项目计划起始时间", required = false)@RequestParam(value = "planStarttime", required = false, defaultValue = "") String planStarttime,
	                            @Parameter(name = "planEndtime", description = "项目计划结束时间", required = false)@RequestParam(value = "planEndtime", required = false, defaultValue = "") String planEndtime
	    ){
	        JsonBean jsonBean = null;
	        try{ 
	            jsonBean = implementPlanService.findRwfpAll(token,pageNumber,pageSize,projectOrderId,prjoectName,planStarttime,planEndtime);
	        }catch (Exception e){
	            e.printStackTrace();
	            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }

	 
	 
	 /**
	 * 启动
	 */
	@RequestMapping(value = "/proj_pj_start", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "启动")
		public JsonBean proj_pj_start(HttpServletRequest request,
			@Parameter(name = "projectid", description = "项目id", required = false)@RequestParam(value = "projectid", required = false) BigDecimal projectid,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.implementPlanService.pjStart(token,projectid);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
		}
	
	
	
	/**
     * 任务管理-左侧树
     */
    @GetMapping("/rwfp/getTree")
    @Operation(summary = "任务管理-左侧树")
    public JsonBean getTree(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name="nodeId",description="nodeId",required=false) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
                            @Parameter(name="projectId",description="projectId",required=false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
                            @Parameter(name = "type", description = "all,my", required = false) @RequestParam(value = "type", required = false) String type) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOperateService.getRwfpTree(token, projectId, nodeId, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    /**
	 * 我的任务获取左侧树结构
	 */
	@GetMapping("/myWork/getTree")
	@Operation(summary = "我的任务获取左侧树结构")
	public JsonBean myWork_getTree(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)BigDecimal projectId) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.getMyWorkTree(token,projectId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

    /**
     * 任务管理列表
     */ 
    @GetMapping("/rwfp/check_list_my_all")
    @Operation(summary = "任务管理列表")
    public JsonBean check_list_my_all(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name="businessType",description="businessType",required=false) @RequestParam(value = "businessType", required = false) String businessType,
                                      @Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)BigDecimal projectId,
                                      @Parameter(name="targetId",description="targetId",required=false) @RequestParam(value = "targetId", required = false) BigDecimal targetId) {

        JsonBean jsonBean = null; 
        try {
            jsonBean = tblNbsjOperateService.checkAllListMyrwPageList(token, pageNumber, pageSize, businessType, targetId,projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
	/**
	 * 我的任务清单列表
	 */
	@GetMapping("/sjss/check_list_my")
	@Operation(summary = "我的任务清单列表")
	public JsonBean check_list_my(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblNbsjOperateVo tblNbsjOperateVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.checkListMyPageList(token, pageNumber, pageSize,tblNbsjOperateVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    
    /**
	 *任务 分配-选择人员保存
	 */
	@RequestMapping(value = "/rwfp/jsfp_role_manage_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "分配-选择人员保存")
    public JsonBean jsfp_role_manage_save(HttpServletRequest request,
    		@Parameter(name="ids",description="ids",required=false)@RequestParam(value = "ids", required = false)String ids,
    		@Parameter(name="teamStaffId",description="teamStaffId",required=false)@RequestParam(value = "teamStaffId", required = false)BigDecimal teamStaffId,
    		@Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)BigDecimal projectId,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.implementPlanService.jsfpRoleManageSave(token, ids, teamStaffId, projectId);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
			e.printStackTrace();
		}
		return jsonBean;
    }
	
	
	
	
	 @GetMapping("/getsqList")
	    @Operation(summary = "项目管理-项目延期申请选择实施方案列表")
	    public JsonBean getsqList(HttpServletRequest request,
	                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
	                            @Parameter(name = "projectOrderId", description = "项目负责人", required = false)@RequestParam(value = "projectOrderId", required = false, defaultValue = "") BigDecimal projectOrderId,
	                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
	                            @Parameter(name = "planStarttime", description = "项目计划起始时间", required = false)@RequestParam(value = "planStarttime", required = false, defaultValue = "") String planStarttime,
	                            @Parameter(name = "planEndtime", description = "项目计划结束时间", required = false)@RequestParam(value = "planEndtime", required = false, defaultValue = "") String planEndtime
	    ){
	        JsonBean jsonBean = null;
	        try{

	            jsonBean = implementPlanService.findsqAll(token,pageNumber,pageSize,projectOrderId,projectName,planStarttime,planEndtime);
	        }catch (Exception e){
	            e.printStackTrace();
	            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	 
	 
	 @Operation(summary = "任务分配-工程项目结算汇总查看列表数据 31")
	    @GetMapping("/rwfp/getGcxmjsListByhz")
	    public JsonBean getGcxmjsListByhz(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "id", description = "计划草稿关联主键 ") @RequestParam(name = "id", required = false) BigDecimal id
	                           
	    ) throws Exception {
	        try {
	            return tblYqnsJhglJhcgGLService.selectListByRwall(token,id);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	 
	 
	 @Operation(summary = "任务分配-工程项目结算汇总查看拆分列表数据 31")
	    @GetMapping("/rwfp/getGcxmjsListByhzcf")
	    public JsonBean getGcxmjsListByhzcf(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "gcxmzjzjbid", description = "gcxmzjzjbid") @RequestParam(name = "gcxmzjzjbid", required = false) BigDecimal gcxmzjzjbid
	                           
	    ) throws Exception {
	        try {
	            return tblYqnsJhglJhcgGLService.selectListByRwcfll(token,gcxmzjzjbid);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	 
	 
	 
	 
	 
	    
	    @Operation(summary = "任务分配-建设项目投资完成情况 32")
	    @GetMapping("/rwfp/getJsxmtzListByhz") 
	    public JsonBean getJsxmtzListByhz(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "id", description = "计划草稿关联主键") @RequestParam(name = "id", required = false) BigDecimal id
	    ) throws Exception {
	        try {
	            return tblYqnsJhglJhcgGLService.selectListByIdall(token,id);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	 
	    
	    
	    
	    
	    @Operation(summary = "任务分配-建设项目投资完成情况拆分列表 32")
	    @GetMapping("/rwfp/getJsxmtzListByhzcf") 
	    public JsonBean getJsxmtzListByhzcf(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "jsxmtzwcqkid", description = "jsxmtzwcqkid") @RequestParam(name = "jsxmtzwcqkid", required = false) BigDecimal jsxmtzwcqkid
	    ) throws Exception {
	        try {
	            return tblYqnsJhglJhcgGLService.selectListByIdallcf(token,jsxmtzwcqkid);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	    
	    
	     
	    @Operation(summary = "三级单位离任审计上报情况 23")
	    @GetMapping("/rwfp/getSjdwlrsjSbListByhz")
	    public JsonBean getSjdwlrsjSbListByhz(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "id", description = "计划关联主键") @RequestParam(name = "id", required = false) BigDecimal id,
	                           @Parameter(name = "glid", description = "项目安排表填报表主键") @RequestParam(name = "glid", required = false) BigDecimal glid,
	                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
	    ) throws Exception {
	        try { 
	            return tblYqnsJhglJhGLService.getcwanbList(token,glid,id,pageNumber,pageSize);
	        } catch (Exception e) {
	        	e.printStackTrace();
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	    
	    
	    
	    
	    /**
		 * 工程项目任务分配-选择人员保存
		 */
		@RequestMapping(value = "/rwfp/jsfp_gc_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = "工程项目分配-选择人员保存")
	    public JsonBean jsfp_gc_save(HttpServletRequest request,
	    		@Parameter(name = "ids", description = "选择工程项目id集合", required = false)@RequestParam(value = "ids", required = false)String ids,
	    		@Parameter(name = "ryids", description = "选择人员id集合", required = false)@RequestParam(value = "ryids", required = false)String ryids,
	    		@Parameter(name = "rynames", description = "选择人员名称集合", required = false)@RequestParam(value = "rynames", required = false)String rynames,
	    		@Parameter(name = "xmtype", description = "项目类别：31或32", required = false)@RequestParam(value = "xmtype", required = false)String xmtype,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.tblYqnsJhglJhcgGLService.rwfpry(token, ids, ryids, rynames, xmtype);
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
				e.printStackTrace();
			}
			return jsonBean;
	    }
	    
	    
		
		 /**
		 * 工程督导分工-工程项目选择人员保存
		 */
		@RequestMapping(value = "/ddfp/jsfp_gc_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = " 工程督导分工-工程项目选择人员选择人员保存")
	    public JsonBean ddfp_gcxm(HttpServletRequest request,
	    		@Parameter(name = "ids", description = "选择工程项目id集合", required = false)@RequestParam(value = "ids", required = false)String ids,
	    		@Parameter(name = "ryids", description = "选择人员id集合", required = false)@RequestParam(value = "ryids", required = false)String ryids,
	    		@Parameter(name = "rynames", description = "选择人员名称集合", required = false)@RequestParam(value = "rynames", required = false)String rynames,
	    		@Parameter(name = "xmtype", description = "项目类别：31或32", required = false)@RequestParam(value = "xmtype", required = false)String xmtype,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.tblYqnsJhglJhcgGLService.rwfpddry(token, ids, ryids, rynames, xmtype);
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
				e.printStackTrace();
			}
			return jsonBean;
	    }
	    
	    
	    
	/**
	 * 我的任务-完成
	 */
	@GetMapping("/sjss/my_task_finish")
	@Operation(summary = "我的任务-完成")
	public JsonBean my_task_finish(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "operateid", description = "我的任务ID", required = false)@RequestParam(value = "operateid", required = false, defaultValue = "") Integer operateid
			) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjOperateService.MyTaskFinish(token, operateid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
	
	
	
	
	
	
	 @Operation(summary = "我的任务-工程项目结算汇总查看列表数据 31")
	    @GetMapping("/rwfp/getgcmyrwList")
	    public JsonBean getgcmyrwList(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "projectId", description = "查询的项目ID") @RequestParam(name = "projectId", required = false) BigDecimal projectId,
	                           @Parameter(name = "id", description = "计划草稿关联主键 ") @RequestParam(name = "id", required = false) BigDecimal id
	                           
	    ) throws Exception {
	        try { 
	            return tblYqnsJhglJhcgGLService.selectListBymtRw(token, id,projectId);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	    
	    @Operation(summary = "我的任务-建设项目投资完成情况 32")
	    @GetMapping("/rwfp/getJsxmmyrwlist")
	    public JsonBean getJsxmmyrwlist(HttpServletRequest request,HttpServletResponse response,
	                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                           @Parameter(name = "id", description = "计划草稿关联主键") @RequestParam(name = "id", required = false) BigDecimal id
	    ) throws Exception {
	        try {
	            return tblYqnsJhglJhcgGLService.selectListByIdmyrw(token,id);
	        } catch (Exception e) {
	            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
	        }
	    }
	 
	
	    
	    /**
		 * 分配专业科室人员
		 */
		@RequestMapping(value = "/fpzyksry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = "分配专业科室人员")
			public JsonBean fpzyksry(HttpServletRequest request,
				@Parameter(name = "projectid", description = "项目id", required = false)@RequestParam(value = "projectid", required = false) BigDecimal projectid,
				@Parameter(name = "zyksryids", description = "选择人员ids", required = false)@RequestParam(value = "zyksryids", required = false) String zyksryids,
				@Parameter(name = "zyksryrwnames", description = "选择人员名称ids", required = false)@RequestParam(value = "zyksryrwnames", required = false) String zyksryrwnames,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.implementPlanService.fpzyksry(token, projectid, zyksryids, zyksryrwnames);
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
			}
		
	
		 /**
		 * 分配-修改项目组成员
		 */
		@RequestMapping(value = "/rwfp/updateTeam", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = "分配-修改项目组成员")
	    public JsonBean updateTeam(HttpServletRequest request,ImplementPlanTeamEntity team,
	    		@Parameter(name="projectId",description="projectId",required=false)@RequestParam(value = "projectId", required = false)Integer projectId,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.implementPlanService.updateImplementPlanTeamWidthId(token,  team);
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
				e.printStackTrace();
			}
			return jsonBean;
	    }
		
		
		  @Operation(summary = "实施方案-选择模板后验证模板是否有误业务单元")
		    @GetMapping("/project/yzmb")
		    public JsonBean yzmb(HttpServletRequest request,HttpServletResponse response,
		                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		                           @Parameter(name = "tempid", description = "选择的模板ID") @RequestParam(name = "tempid", required = false) String tempid
		    ) throws Exception {
		        try {
		            return implementPlanService.yzmb(token, tempid);
		        } catch (Exception e) {
		            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
		        }
		    }
		 
		  @Operation(summary = "审计管理-质量分析统计表") 
		    @GetMapping("/sjgl/qutlitgetlist")
		    public JsonBean qutlitgetlist(HttpServletRequest request,HttpServletResponse response,QualityParam param,
		                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		    ) throws Exception {
		        try {
		            return implementPlanService.findBytjsj(token,param);
		        } catch (Exception e) {
		            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
		        }
		    }
		  
		  
		  @GetMapping("/findbyOrgiddetail")
		    @Operation(summary = "任中审计明细-根据公司id获取项目内容")
		    public JsonBean findbyOrgiddetail(HttpServletRequest request,
		    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		    		@Parameter(name = "orgid", description = "orgid", required = false)@RequestParam(value = "orgid", required = false, defaultValue = "") String orgid){
		        JsonBean jsonBean = null;
		        try{ 
		            jsonBean = implementPlanService.findbyOrgidLast(token, orgid);
		        }catch (Exception e){
		            e.printStackTrace();
		            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
		        }
		        return jsonBean;
		    }
	
}
