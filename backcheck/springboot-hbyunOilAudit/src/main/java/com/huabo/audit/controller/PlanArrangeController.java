package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
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

import com.alibaba.fastjson.JSON;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.dto.TblYqnsEnginAuditProjectDto;
import com.huabo.audit.oracle.dto.TblYqnsFundAuditProjectDto;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.huabo.audit.oracle.entity.TblYqnsEnginAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsEngintb;
import com.huabo.audit.oracle.entity.TblYqnsFundAuditProjectEntity;
import com.huabo.audit.oracle.entity.TblYqnsFundtb;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzjZjb;
import com.huabo.audit.oracle.entity.TblYqnsJsxmTzwcqk;
import com.huabo.audit.oracle.service.EnginAuditProjectService;
import com.huabo.audit.oracle.service.FundAuditProjectService;
import com.huabo.audit.oracle.service.TblYqnsEnginAuditProjectXFEntityService;
import com.huabo.audit.oracle.service.TblYqnsFundAuditProjectXFEntityService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.TblYqnsJhglJhGLService;
import com.huabo.audit.service.TblYqnsJhglJhcgGLService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @program: springboot-hbyunMonitor
 * @description: 计划编制
 * @author: WangZhenDong
 * @create: 2023-10-14 10:34
 **/
@RestController
@Slf4j
@Tag(name="计划编制",description="计划编制")
@RequestMapping(value = "/audit/planArrange")
public class PlanArrangeController {

    /**
     * 工程审计项目安排服务
     */
    @Autowired
    private EnginAuditProjectService enginAuditProjectService;

    @Autowired
    private TblYqnsEnginAuditProjectXFEntityService tblYqnsEnginAuditProjectXFEntityService;
    /**
     * 财务审计项目安排服务
     */
    @Autowired
    private FundAuditProjectService fundAuditProjectService;

    @Autowired
    private TblYqnsFundAuditProjectXFEntityService tblYqnsFundAuditProjectXFEntityService;
    
    
    @Resource
    TblYqnsJhglJhcgGLService tblYqnsJhglJhcgGLService;
    
    @Resource
    TblYqnsJhglJhGLService tblYqnsJhglJhGLService;
    

    @Operation(summary = "计划编制---工程审计项目安排--新增/更新")
    @PostMapping(value = "enginAuditProject/saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateEnginAuditProject(
            @RequestBody TblYqnsEnginAuditProjectDto param) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = enginAuditProjectService.saveOrUpdate(param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---工程审计项目安排 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }

    @Operation(summary = "计划编制---工程审计项目安排--删除")
    @PostMapping(value = "enginAuditProject/delete", produces = "application/json;charset=utf-8")
    public JsonBean deleteEnginAuditProject(
            @Parameter(name="id",required=true) @RequestParam Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = enginAuditProjectService.delete(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---工程审计项目安排 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }

    @Operation(summary = "计划编制---工程审计项目安排---根据ID查询工程审计项目安排")  
    @GetMapping(value = "enginAuditProject/findById", produces = "application/json;charset=utf-8")
    public JsonBean getEnginAuditProjectById(
            @Parameter(name = "id", description = "工程审计项目id", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = enginAuditProjectService.findById(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制---工程审计项目安排---根据ID查询工程审计项目安排异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "计划编制---工程审计项目安排---查询工程审计项目安排列表")
    @GetMapping(value = "enginAuditProject/findList", produces = "application/json;charset=utf-8")
    public JsonBean findEnginAuditProjectList(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "name", description = "查询条件，根据审计项目名称查询") @RequestParam(value = "name", defaultValue = "") String name) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = enginAuditProjectService.findList(pageNumber, pageSize, name);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制---工程审计项目安排---查询工程审计项目安排列表异常", e); 
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    
 
    @Operation(summary = "工程审计项目安排- 填报列表查询")
	@GetMapping("/enginAuditProject/getgcList")
	public JsonBean getgcList(SjdwjdVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = enginAuditProjectService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("工程审计项目安排- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	@PostMapping(value ="/enginAuditProject/tbgcsaveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "工程审计项目安排-添加或修改填报信息")
    public JsonBean tbgcsaveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "attids", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attids", required = false) String attids,
                                 @Parameter(name = "gljson", description = "子表ids", required = false)@RequestParam(value = "gljson", required = false, defaultValue = "") String gljson,
                                 TblYqnsEngintb jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=enginAuditProjectService.saveOrupdate(token, jd, gljson,attids);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
	
	 /**
     *	  工程审计项目安排填报表-附件列表
     */
    @GetMapping("/enginAuditProject/file_list")
    @Operation(summary = "工程审计项目安排填报表-附件列表")
    public JsonBean gcfile_list(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "tbid", description = "业务主键", required = false) @RequestParam(value = "tbid", required = false) String tbid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.enginAuditProjectService.outFileList(token, tbid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 	工程审计项目安排填报表-附件删除
     */
    @GetMapping("/enginAuditProject/file_del")
    @Operation(summary = "工程审计项目安排填报表-附件删除")
    public JsonBean gcfile_del(HttpServletRequest request, HttpServletResponse response,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "attid", description = "附件主键ID", required = true) @RequestParam("attid") String attid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = enginAuditProjectService.deleteattid(token, attid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
	
	
	
	@GetMapping("/enginAuditProject/tbgcdetail")
    @Operation(summary = "工程审计项目安排-根据ID获取填报详细信息")
    public JsonBean tbgcdetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
        JsonBean jsonBean = null;
        try{ 
            jsonBean = enginAuditProjectService.findByid(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @DeleteMapping("/enginAuditProject/tbgcdelete")
    @Operation(summary = "工程审计项目安排-删除填报信息")
    public JsonBean tbgcdelete(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean=enginAuditProjectService.deleteone(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }


    

    /**
	 * 下发专业科室人员
	 */
	@RequestMapping(value = "/enginAuditProject/gcxfksry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "工程审计项目安排-下发专业科室人员")
	public JsonBean gcxfksry(HttpServletRequest request,
		@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
		@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
		@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
		@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.enginAuditProjectService.xfzyksry(token, ids, ryids, names);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
    
	 	/**
		 * 下发项目组成员人员
		 */
		@RequestMapping(value = "/enginAuditProject/gcxfxmzry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = "工程审计项目安排-下发项目组成员人员")
			public JsonBean gcxfxmzry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
				@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.enginAuditProjectService.xfxmzry(token, ids, ryids, names);
				
			} catch (Exception e) {
				ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
		}
    


    @Operation(summary = "分发工程（删除项目,重新增加人员）")
    @GetMapping(value = "distributeEngin", produces = "application/json;charset=utf-8")
    public JsonBean distributeEngin(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "enginId", description = "工程审计项目安排ID", required = true) @RequestParam(value = "enginId") String enginId,
            @Parameter(name = "userIds", description = "分发的userIds 格式: 1,2)", required = true) @RequestParam(value = "userIds") String userIds) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsEnginAuditProjectXFEntityService.saveOrUpdate(token, enginId, userIds);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计项目安排 --分发（删除项目id,重新增加人员） 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "通过工程审计项目安排id 查询分发人员")
    @GetMapping(value = "getDistributeEnginList", produces = "application/json;charset=utf-8")
    public JsonBean getDistributeEnginList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "enginId", description = "工程审计项目安排ID", required = true) @RequestParam(value = "enginId") String enginId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsEnginAuditProjectXFEntityService.getListByEnginId(token, enginId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("工程审计项目安排 --查新分发List 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "计划编制---财务审计项目安排--新增/更新")
    @PostMapping(value = "fundAuditProject/saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateFundAuditProject(
            @RequestBody TblYqnsFundAuditProjectDto param) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = fundAuditProjectService.saveOrUpdate(param);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---财务审计项目安排 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        } 

        return jsonBean;
    }

    @Operation(summary = "计划编制---财务审计项目安排--删除")
    @PostMapping(value = "fundAuditProject/delete", produces = "application/json;charset=utf-8")
    public JsonBean deleteFundAuditProject(
            @Parameter(name="id",required=true) @RequestParam Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = fundAuditProjectService.delete(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施---财务审计项目安排 -- 删除接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }
        return jsonBean;
    }

    @Operation(summary = "计划编制---财务审计项目安排---根据ID查询财务审计项目安排")
    @GetMapping(value = "fundAuditProject/findById", produces = "application/json;charset=utf-8")
    public JsonBean getFundAuditProjectById(
            @Parameter(name = "id", description = "财务审计项目id", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = fundAuditProjectService.findById(id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制---财务审计项目安排---根据ID查询财务审计项目安排异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "计划编制---财务审计项目安排---查询财务审计项目安排列表")
    @GetMapping(value = "fundAuditProject/findList", produces = "application/json;charset=utf-8")
    public JsonBean findFundAuditProjectList(
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "name", description = "查询条件，根据审计项目名称查询") @RequestParam(value = "name", defaultValue = "") String name) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = fundAuditProjectService.findList(pageNumber, pageSize, name);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("计划编制---财务审计项目安排---查询财务审计项目安排列表异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }



    @Operation(summary = "分发财务（删除项目,重新增加人员）")
    @GetMapping(value = "distributeFund", produces = "application/json;charset=utf-8")
    public JsonBean distributeFund(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "fundId", description = "财务审计项目安排ID", required = true) @RequestParam(value = "fundId") String fundId,
            @Parameter(name = "userIds", description = "分发的userIds 格式: 1,2)", required = true) @RequestParam(value = "userIds") String userIds) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsFundAuditProjectXFEntityService.saveOrUpdate(token, fundId, userIds);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("财务审计项目安排 --分发（删除项目id,重新增加人员） 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "通过财务审计项目安排id 查询分发人员")
    @GetMapping(value = "getDistributeFundList", produces = "application/json;charset=utf-8")
    public JsonBean getDistributeFundList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "fundId", description = "财务审计项目安排ID", required = true) @RequestParam(value = "fundId") String fundId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsFundAuditProjectXFEntityService.getListByFundId(token, fundId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("财务审计项目安排 --查新分发List 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    
    
    @Operation(summary = "财务审计项目安排- 填报列表查询")
	@GetMapping("/fundAuditProject/getList")
	public JsonBean getfundAuditProjectList(SjdwjdVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = fundAuditProjectService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("财务审计项目安排- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	
	
	@PostMapping(value ="/fundAuditProject/tbsaveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "财务审计项目安排-添加或修改填报信息")
    public JsonBean fundAuditProjectsaveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "attids", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attids", required = false) String attids,
                                 @Parameter(name = "gljson", description = "子表ids", required = false)@RequestParam(value = "gljson", required = false, defaultValue = "") String gljson,
                                 TblYqnsFundtb jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=fundAuditProjectService.saveOrupdate(token, jd, gljson,attids);

        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
	
	
	
	 /**
     *	  财务项目安排填报表-附件列表
     */
    @GetMapping("/fundAuditProject/file_list")
    @Operation(summary = "财务项目安排填报表-附件列表")
    public JsonBean file_list(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "tbid", description = "业务主键", required = false) @RequestParam(value = "tbid", required = false) String tbid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.fundAuditProjectService.outFileList(token, tbid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 	财务项目安排填报表-附件删除
     */
    @GetMapping("/fundAuditProject/file_del")
    @Operation(summary = "财务项目安排填报表-附件删除")
    public JsonBean file_del(HttpServletRequest request, HttpServletResponse response,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "attid", description = "附件主键ID", required = true) @RequestParam("attid") String attid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = fundAuditProjectService.deleteattid(token, attid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
	
	
	
	@GetMapping("/fundAuditProject/tbdetail")
    @Operation(summary = "财务审计项目安排-根据ID获取填报详细信息")
    public JsonBean fundAuditProjectdetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
        JsonBean jsonBean = null;
        try{
            jsonBean = fundAuditProjectService.findByid(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @DeleteMapping("/fundAuditProject/tbdelete")
    @Operation(summary = "财务审计项目安排-删除填报信息")
    public JsonBean fundAuditProjectdelete(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean=fundAuditProjectService.deleteone(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }


    

    /**
	 * 下发专业科室人员
	 */
	@RequestMapping(value = "/fundAuditProject/xfksry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "财务审计项目安排-下发专业科室人员")
	public JsonBean fpzyksry(HttpServletRequest request,
		@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
		@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
		@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
		@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.fundAuditProjectService.xfzyksry(token, ids, ryids, names);
			
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
    
	 	/**
		 * 下发项目组成员人员
		 */
		@RequestMapping(value = "/fundAuditProject/xfxmzry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		@Operation(summary = "财务审计项目安排-下发项目组成员人员")
			public JsonBean xfxmzry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
				@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
			JsonBean jsonBean = null;
			try {
				jsonBean = this.fundAuditProjectService.xfxmzry(token, ids, ryids, names);
				
			} catch (Exception e) {
				e.printStackTrace();
				jsonBean = ResponseFormat.retParam(1,1000,e.getMessage());
			}
			return jsonBean;
		}
		
		
		
		
		@Operation(summary = "工程审计项目安排- 督导分工列表查询")
		@GetMapping("/enginAuditProject/getddfgList")
		public JsonBean getddfgList(SjdwjdVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = enginAuditProjectService.findAllddfgList(token, pageNumber, pageSize, vo);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("工程审计项目安排- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		
		@Operation(summary = "财务审计项目安排- 督导分工列表查询")
		@GetMapping("/fundAuditProject/getddfgList")
		public JsonBean getfundgetddfgList(SjdwjdVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = fundAuditProjectService.findAlcwddfglList(token, pageNumber, pageSize, vo);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("财务审计项目安排- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		 
		 	/**
			 * 财务审计项目安排-分配专业科室督导人员
			 */
			@RequestMapping(value = "/fundAuditProject/fpddksry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary = "财务审计项目安排-分配专业科室督导人员")
			public JsonBean fpddksry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
				@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
				JsonBean jsonBean = null;
				try {
					jsonBean = this.fundAuditProjectService.fpzyksry(token, ids, ryids, names);
					
				} catch (Exception e) {
					e.printStackTrace();
					jsonBean =ResponseFormat.retParam(1,1000,e.getMessage());
				}
				return jsonBean;
			} 
			
			
			  /**
			 * 工程审计项目安排-分配专业科室督导人员
			 */
			@RequestMapping(value = "/enginAuditProject/gcfpksry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary = "工程审计项目安排-分配专业科室督导人员")
			public JsonBean gcfpksry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
				@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
				@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
				JsonBean jsonBean = null;
				try {
					jsonBean = this.enginAuditProjectService.fpzyksry(token, ids, ryids, names);
					
				} catch (Exception e) {
					ResponseFormat.retParam(1,1000,e.getMessage());
				}
				return jsonBean;
			}
		    
			
			/**
			 * 财务审计项目安排-分配审理科室人员
			 */
			@RequestMapping(value = "/fundAuditProject/fpslkry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary = "财务审计项目安排-分配审理科室人员")
			public JsonBean finance_fpslkry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "fpslkryid", description = "选择人员ids", required = false)@RequestParam(value = "fpslkryid", required = false) String fpslkryid,
				@Parameter(name = "fpslkryname", description = "选择人员名称s", required = false)@RequestParam(value = "fpslkryname", required = false) String fpslkryname)throws Exception{
				JsonBean jsonBean = null;
				try {
					jsonBean = this.fundAuditProjectService.fpslkry( ids, fpslkryid, fpslkryname);
					
				} catch (Exception e) {
					e.printStackTrace();
					jsonBean =ResponseFormat.retParam(1,1000,e.getMessage());
				}
				return jsonBean;
			} 
			
			
			  /**
			 * 工程审计项目安排-分配审理科室人员
			 */
			@RequestMapping(value = "/enginAuditProject/fpslkry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary = "工程审计项目安排-分配审理科室人员")
			public JsonBean gcfpslkry(HttpServletRequest request,
				@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
				@Parameter(name = "fpslkryid", description = "选择人员ids", required = false)@RequestParam(value = "fpslkryid", required = false) String fpslkryid,
				@Parameter(name = "fpslkryname", description = "选择人员名称s", required = false)@RequestParam(value = "fpslkryname", required = false) String fpslkryname
				)throws Exception{
				JsonBean jsonBean = null;
				try {
					jsonBean = this.enginAuditProjectService.fpslkry(ids, fpslkryid, fpslkryname);
					
				} catch (Exception e) {
					ResponseFormat.retParam(1,1000,e.getMessage());
				}
				return jsonBean;
			}
			
			
			 @Operation(summary = "项目管理---实施方案---选择财务审计项目安排列表")
			    @GetMapping(value = "fundAuditProject/xzfindList", produces = "application/json;charset=utf-8")
			    public JsonBean xzfindList(
			    		@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
			            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			            @Parameter(name = "name", description = "查询条件，根据审计项目名称查询") @RequestParam(value = "name", defaultValue = "") String name) {
			        JsonBean jsonBean = new JsonBean();
			        try {
			            jsonBean = fundAuditProjectService.xzfindList(token, pageNumber, pageSize, name);
			        } catch (ServiceException se) {
			            throw se;
			        } catch (Exception e) {
			            log.error("计划编制---财务审计项目安排---查询财务审计项目安排列表异常", e);
			            jsonBean.setCode(0);
			            jsonBean.setMsg(e.getMessage());
			            jsonBean.setData(null);
			        }
			        return jsonBean;
			    }
			
			
			
			  @Operation(summary = "项目管理---实施方案---选择工程审计项目安排列表")
			    @GetMapping(value = "enginAuditProject/xzfindList", produces = "application/json;charset=utf-8")
			    public JsonBean xzgcfindList(
			    		@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
			            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
			            @Parameter(name = "name", description = "查询条件，根据审计项目名称查询") @RequestParam(value = "name", defaultValue = "") String name) {
			        JsonBean jsonBean = new JsonBean();
			        try {
			            jsonBean = enginAuditProjectService.xzfindList(token, pageNumber, pageSize, name);
			        } catch (ServiceException se) {
			            throw se;
			        } catch (Exception e) {
			            log.error("计划编制---工程审计项目安排---查询工程审计项目安排列表异常", e);
			            jsonBean.setCode(0);
			            jsonBean.setMsg(e.getMessage());
			            jsonBean.setData(null);
			        }
			        return jsonBean;
			    }
			  
			  
			  
			  
			  
			  
			  
			  @Operation(summary = "工程项目人员上报-列表查询")
				@GetMapping("/enginAuditProject/getgcsbList")
				public JsonBean getgcsbList(SjdwjdVo vo,
						@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
						@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
					JsonBean jsonBean = null;
					try {
						jsonBean = enginAuditProjectService.getgcsbList(token, pageNumber, pageSize, vo);
					} catch (ServiceException ex) {
						throw ex;
					} catch (Exception e) {
						log.error("工程审计项目安排- 列表查询 ...接口 异常", e);
						return ResponseFormat.retParam(0,"失败",e);
					}
					return jsonBean;
				}
				
				
				@Operation(summary = "财务项目人员上报-列表查询")
				@GetMapping("/fundAuditProject/getswsbList")
				public JsonBean getswsbList(SjdwjdVo vo,
						@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
						@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
						@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
					JsonBean jsonBean = null;
					try {
						jsonBean = fundAuditProjectService.getswsbList(token, pageNumber, pageSize, vo);
					} catch (ServiceException ex) {
						throw ex;
					} catch (Exception e) {
						log.error("财务审计项目安排- 列表查询 ...接口 异常", e);
						return ResponseFormat.retParam(0,"失败",e);
					}
					return jsonBean;
				}
			  
			  
			  
				/**
				 * 财务审计项目安排-批量下发助审人员
				 */
				@RequestMapping(value = "/fundAuditProject/xfzsrys", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "财务审计项目安排-批量下发助审人员")
					public JsonBean xfzsrys(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
						@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.fundAuditProjectService.fpkzsrys(token, ids, ryids, names);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				}  
			  
			  
				
				

			 	/**
				 * 工程审计项目安排-批量下发助审人员
				 */
				@RequestMapping(value = "/enginAuditProject/gcxfzsrys", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "工程审计项目安排-批量下发助审人员")
					public JsonBean gcxfzsrys(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "ryids", description = "选择人员ids", required = false)@RequestParam(value = "ryids", required = false) String ryids,
						@Parameter(name = "names", description = "选择人员名称ids", required = false)@RequestParam(value = "names", required = false) String names,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.enginAuditProjectService.fpkzsrys(token, ids, ryids, names);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				}
				
				

				/**
				 * 财务审计项目安排-批量上报
				 */
				@RequestMapping(value = "/fundAuditProject/xmzsb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "财务审计项目安排-批量上报")
					public JsonBean xmzsb(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.fundAuditProjectService.xmzsb(token, ids);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				}  
				
				/**
				 * 财务审计项目安排-批量退回
				 */
				@RequestMapping(value = "/fundAuditProject/xmzsbth", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "财务审计项目安排-批量退回")
					public JsonBean xmzsbth(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.fundAuditProjectService.xmzsbth(token, ids);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				} 
				
				
				
				/**
				 * 工程审计项目安排-批量上报
				 */
				@RequestMapping(value = "/enginAuditProject/xmzsb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "工程审计项目安排-批量上报")
					public JsonBean gcxmzsb(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.enginAuditProjectService.xmzsb(token, ids);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				}  
				
				/**
				 * 工程审计项目安排-批量退回
				 */
				@RequestMapping(value = "/enginAuditProject/xmzsbth", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary = "工程审计项目安排-批量退回")
					public JsonBean gcxmzsbth(HttpServletRequest request,
						@Parameter(name = "ids", description = "项目id集合", required = false)@RequestParam(value = "ids", required = false) String ids,
						@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
					JsonBean jsonBean = null;
					try {
						jsonBean = this.enginAuditProjectService.xmzsbth(token, ids);
						
					} catch (Exception e) {
						e.printStackTrace();
						jsonBean=ResponseFormat.retParam(1,1000,e.getMessage());
					}
					return jsonBean;
				} 
				
				
		
				   //导出
			    @GetMapping("/fundAuditProject/export")
			    @Operation(summary = "财务项目安排表-列表导出")
			    public void exportList( HttpServletResponse response,
			    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			    		@Parameter(name = "batc", description = "批次", required = false)@RequestParam(value = "batc", required = false) String batc,
			            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") String tbid,
                        @Parameter(name = "idList", description = "财务审计项目id集合", required = false) @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList
			            ){
			        log.info("财务项目安排表导出Excel,param batc is {}, tbid is {}, idList is {}", batc, tbid, JSON.toJSONString(idList));
			        response.setContentType("application/binary;charset=UTF-8");
			        try{
			            String date = String.valueOf(System.currentTimeMillis());
			            String fileName = "财务项目安排表" + "_" + batc + ".xlsx";
			            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			            ServletOutputStream outputStream = response.getOutputStream();
			            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			            List<TblYqnsFundAuditProjectEntity> list = fundAuditProjectService.findbytbidgetlist(token, tbid, idList);
			            List<Object[]> exportList = new ArrayList<>(list.size());
			            String[] titles = {"序号","实施单位","小组","被审计单位","审计范围","三级单位", "组长","副组长", "主审","助审","人数","现场时间", "备注",};
			            for (int i = 0; i < list.size(); i++) {
			            	 TblYqnsFundAuditProjectEntity r = list.get(i);
			            	 String str="";
			            	 if(r.getXcsrarttime()!=null) {
			            		 str=outputFormatter.format(r.getXcsrarttime());
			            	 }
			            	 if(r.getXcendtime()!=null) {
			            		 str+="-"+outputFormatter.format(r.getXcendtime());
			            	 }
			                Object[] o = {i+1,r.getExePhraseUnit(),r.getAuditGroup(),r.getAuditUnit(),r.getAuditRange(),r.getGljhxmlx()!=null && r.getGljhxmlx().equals("23")?r.getXmsl():"",r.getGroupLeader(), r.getFzzName(),
			                		r.getApprover(),r.getAssistApprover(),r.getRsyq(),str,r.getRemarks()};
			                exportList.add(o);
			            }

			            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			        }catch (Exception e){
			            e.printStackTrace();
			            log.error("财务项目安排表导出失败",e );
			        }
			    }
			    
			    
			    
			    
			    @Operation(summary = "财务项目安排表-三级单位离任审计导出")
			    @GetMapping("/fundAuditProject/exportsjdw")
			    public void getSjdwlrsjSbListByhz(HttpServletRequest request,HttpServletResponse response,
			                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			                           @Parameter(name = "glid", description = "项目安排表填报表主键") @RequestParam(name = "glid", required = false) BigDecimal glid,
			                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
			    ) throws Exception {
			    	 log.info("三级单位导出Excel");
			         response.setContentType("application/binary;charset=UTF-8");
			        try {
			        	
			        	 String date = String.valueOf(System.currentTimeMillis());
			             String fileName = "三级单位" + "_" + date + ".xlsx";
			             response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			             ServletOutputStream outputStream = response.getOutputStream();
			             JsonBean jsonBean = tblYqnsJhglJhGLService.getcwanbList(token,glid,null,pageNumber,pageSize);
			             SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			             List<LeaveAudit3LEntity> list = ((PageInfo) jsonBean.getData()).getTlist();
			             List<Object[]> exportList = new ArrayList<>(list.size());
			             String[] titles = {"序号","项目名称","实施范围", "组长","副组长", "主审","助审","人数","现场时间",};
			             for (int i = 0; i < list.size(); i++) {
			            	 LeaveAudit3LEntity r = list.get(i);
			            	 String str="";
			            	 if(r.getXcsrarttime()!=null) {
			            		 str=outputFormatter.format(r.getXcsrarttime());
			            	 }
			            	 if(r.getXcendtime()!=null) {
			            		 str+="-"+outputFormatter.format(r.getXcendtime());
			            	 }
			            	 String str1="";
			            	 if(r.getOldJobStartTime()!=null) {
			            		 str1=outputFormatter.format(r.getOldJobStartTime());
			            	 }
			            	 if(r.getOldJobEndTime()!=null) {
			            		 str1+="-"+outputFormatter.format(r.getOldJobEndTime());
			            	 }
			            	 
			                 Object[] o = {i+1,r.getOldOrg().getOrgname()+"经济责任审计",str1,r.getZznames(),r.getFznames(),r.getZsname(),r.getFzname(),r.getRsyq(),str};
			                 exportList.add(o);
			             }

			             ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			        } catch (Exception e) {
			        	e.printStackTrace();
			        }
			    }
			    
			    
			    
			    //导出
			    @GetMapping("/enginAuditProject/export")
			    @Operation(summary = "工程项目安排表-列表导出")
			    public void gcexportList( HttpServletResponse response,
			    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
			    		@Parameter(name = "batc", description = "批次", required = false)@RequestParam(value = "batc", required = false) String batc,
			            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") String tbid,
                        @Parameter(name = "idList", description = "工程审计项目id集合", required = false) @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList
			            ){
			        log.info("工程项目安排表导出Excel, param idList is {}", JSON.toJSONString(idList));
			        response.setContentType("application/binary;charset=UTF-8");
			        try{
			            String date = String.valueOf(System.currentTimeMillis());
			            String fileName = "工程项目安排表" + "_" + batc + ".xlsx";
			            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			            ServletOutputStream outputStream = response.getOutputStream();
			            SimpleDateFormat outputFormatter = new SimpleDateFormat("yyyy-MM-dd");
			      	  
			            List<TblYqnsEnginAuditProjectEntity> list = enginAuditProjectService.findbytbidgetlist(token, tbid, idList);
			            List<Object[]> exportList = new ArrayList<>(list.size());
			            String[] titles = {"序号","实施单位","审计项目名称","被审计单位","项目数量","金额（万元）", "组长","副组长", "主审","助审","人数","现场时间", "备注",};
			            for (int i = 0; i < list.size(); i++) {
			            	TblYqnsEnginAuditProjectEntity r = list.get(i);
			            	String str="";
			            	 if(r.getXcsrarttime()!=null) {
			            		 str=outputFormatter.format(r.getXcsrarttime());
			            	 }
			            	 if(r.getXcendtime()!=null) {
			            		 str+="-"+outputFormatter.format(r.getXcendtime());
			            	 }
			                Object[] o = {i+1,r.getExePhraseUnit(),r.getName(),r.getAuditUnit(),r.getProjectNum(),r.getAmount(),r.getGroupLeader(), r.getFzzName(),
			                		r.getApprover(),r.getAssistApprover(),r.getRsyq(),str,r.getRemarks()};
			                exportList.add(o);
			            }

			            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			        }catch (Exception e){
			            e.printStackTrace();
			            log.error("工程项目安排表导出失败",e );
			        }
			    }
			    
			    
			    
			    @Operation(summary = "工程项目安排表-工程项目结算汇总查看列表数据 31导出")
			    @GetMapping("/enginAuditProject/exportgcjs")
			    public void getGcxmjsListByhz(HttpServletRequest request,HttpServletResponse response,
			                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			                           @Parameter(name = "id", description = "计划草稿关联主键 ") @RequestParam(name = "id", required = false) BigDecimal id
			                           
			    ) throws Exception {
			    	
			    	
			    	  log.info("工程项目结算汇总查看列表数据 31导出Excel");
			          response.setContentType("application/binary;charset=UTF-8");
			          try{
			              String date = String.valueOf(System.currentTimeMillis());
			              String fileName = "工程结算项目" + "_" + date + ".xlsx";
			              response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			              ServletOutputStream outputStream = response.getOutputStream();

			              List<TblYqnsGcxmzjZjb> list=tblYqnsJhglJhcgGLService.selectListByRwalllist(token,id);
			              
			              List<Object[]> exportList = new ArrayList<>(list.size());
			              String[] titles = {"序号","合同编号","施工单位","二审审查金额（元）","本次审计人员",};
			              for (int i = 0; i < list.size(); i++) {
			            	  TblYqnsGcxmzjZjb r = list.get(i);
			                  Object[] o = {i+1,r.getTblYqnsGcxmzj().getHtbh(),r.getTblYqnsGcxmzj().getSgdw(),r.getTblYqnsGcxmzj().getEsscje(),r.getRwnames()};
			                  exportList.add(o);
			              }

			              ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			          }catch (Exception e){
			              e.printStackTrace();
			              log.error("工程项目结算汇总查看列表数据 31导出导出失败",e );
			          }
			    	
			    
			    }
			    
			    
			    
			    @Operation(summary = "工程项目安排表-建设项目投资完成情况 32导出")
			    @GetMapping("/enginAuditProject/exporttzwc") 
			    public void getJsxmtzListByhz(HttpServletRequest request,HttpServletResponse response,
			                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			                           @Parameter(name = "id", description = "计划草稿关联主键") @RequestParam(name = "id", required = false) BigDecimal id
			    ) throws Exception {
			    	
			    	
			    	  log.info("建设项目投资完成情况32导出Excel");
			          response.setContentType("application/binary;charset=UTF-8");
			          try{
			              String date = String.valueOf(System.currentTimeMillis());
			              String fileName = "竣工决算项目" + "_" + date + ".xlsx";
			              response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
			              ServletOutputStream outputStream = response.getOutputStream();

			              List<TblYqnsJsxmTzwcqk> list=tblYqnsJhglJhcgGLService.selectListByIdalllist(token,id);
			              
			              List<Object[]> exportList = new ArrayList<>(list.size());
			              String[] titles = {"序号","工程或费用名称","实施单位","批复概算投资","合同金额","结算金额","投资节超（概算-实际完成）","本次审计人员",};
			              for (int i = 0; i < list.size(); i++) {
			            	  TblYqnsJsxmTzwcqk r = list.get(i);
			                  Object[] o = {i+1,r.getGchfymc(),r.getSsdw(),r.getPfgstzje(),r.getHtje(),r.getTzjc(),r.getRwnames()};
			                  exportList.add(o);
			              }

			              ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			          }catch (Exception e){
			              e.printStackTrace();
			              log.error("建设项目投资完成情况32导出导出失败",e );
			          }
			    	
			    
			    }
			    
				
			
}
