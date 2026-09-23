package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.LeaveAudit3LEntity;
import com.huabo.audit.oracle.entity.TblYqnsSjdwjd;
import com.huabo.audit.oracle.service.LeaveAudit3LXFEntityService;
import com.huabo.audit.oracle.service.TblYqnsSjdwjdService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.LeaveAudit3LService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName LeaveAudit3LController
 * @Description 计划管理-三级单位离任审计
 * @DATE 2023/9/14
 */
@RestController
@Slf4j
@Tag(name="计划管理-三级单位离任审计",description="计划管理-三级单位离任审计")
@RequestMapping(value = "/plan/leave/audit3L")
public class LeaveAudit3LController {
    @Autowired
    private LeaveAudit3LService leaveAudit3LService;

    @Autowired
    private LeaveAudit3LXFEntityService leaveAudit3LXFEntityService;
    @Autowired
    private TblYqnsSjdwjdService tblYqnsSjdwjdService;
    
    @GetMapping("/getDetailDistributeList")
    @Operation(summary = "三级单位离任审计明细 -- 获取下发数据列表")
    public JsonBean getDetailDistributeList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            LeaveAudit3LEntity vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = this.leaveAudit3LService.getDetailDistributeList(token, pageNumber, pageSize, vo);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getDistributeReceiveList")
    @Operation(summary = "三级单位离任审计明细 -- 下发人获取自己的处理数据")
    public JsonBean getDistributeReceiveList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            LeaveAudit3LEntity vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = this.leaveAudit3LService.getDistributeReceiveList(token, pageNumber, pageSize, vo);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @PostMapping("/saveDistributionPerson")
	@Operation(summary = "分页获取业务单据下发待处理信息")
	public JsonBean saveDistributionPerson(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "idStrs", description = "三级单位离任审计明细主键，逗号拼接", required = false) @RequestParam(value = "idStrs", required = true) String idStrs,
			@Parameter(name = "disFirstPerson", description = "第一次下发接收人", required = false) @RequestParam(value = "disFirstPerson", required = false) BigDecimal disFirstPerson,
			@Parameter(name = "disSecondPerson", description = "第二次下发接收人", required = false) @RequestParam(value = "disSecondPerson", required = false) BigDecimal disSecondPerson) {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.leaveAudit3LService.saveDistributionPerson(token,idStrs,disFirstPerson,disSecondPerson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    
    @GetMapping("/getjdList")
    @Operation(summary = "三级单位离任审计-季度列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            SjdwjdVo vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsSjdwjdService.findAllList(token, pageNumber, pageSize, vo);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @PostMapping(value ="/jdsaveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "添加或修改季度信息")
    public JsonBean jdsaveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "glids", description = "glids", required = false)@RequestParam(value = "glids", required = false, defaultValue = "") String glids,
                                 TblYqnsSjdwjd jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=tblYqnsSjdwjdService.saveOrupdate(token, jd, glids);

        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("/jddetail")
    @Operation(summary = "根据ID获取季度详细信息")
    public JsonBean jddetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsSjdwjdService.findByid(token, jdid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @DeleteMapping("/jddelete")
    @Operation(summary = "删除")
    public JsonBean jddelete(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean=tblYqnsSjdwjdService.deleteone(token, jdid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    


    @GetMapping("/getList")
    @Operation(summary = "根据季度ID获取三级单位离任审计列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid,
                            LeaveAudit3LEntity leaveAudit3LEntity
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = leaveAudit3LService.findAll(token,pageNumber,pageSize,leaveAudit3LEntity,jdid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/getListDraftPlan")
    @Operation(summary = "根据季度ID获取三级单位离任审计列表")
    public JsonBean getListDraftPlan(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid,
                            LeaveAudit3LEntity leaveAudit3LEntity
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = leaveAudit3LService.getListDraftPlan(token,pageNumber,pageSize,leaveAudit3LEntity,jdid);
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
            jsonBean = leaveAudit3LService.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @PostMapping(value ="/saveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "添加或修改信息")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "leaveAudit3LEntity", description = "实体", required = true) @RequestBody LeaveAudit3LEntity leaveAudit3LEntity
    ){
        JsonBean jsonBean = null; 
        try{
            if(null != leaveAudit3LEntity.getId()){
            	jsonBean= leaveAudit3LService.updateEntity(token,leaveAudit3LEntity);
            }else{
                jsonBean= leaveAudit3LService.saveEntity(token, leaveAudit3LEntity);
            }

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
            leaveAudit3LService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }




    @Operation(summary = "下发（删除项目,重新增加人员）")
    @GetMapping(value = "distributeFund", produces = "application/json;charset=utf-8")
    public JsonBean distributeFund(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "auditIds", description = "三级单位 ID 格式: 1,2", required = true) @RequestParam(value = "auditIds") String auditIds,
            @Parameter(name = "userIds", description = "分发的userIds 格式: 1,2)", required = true) @RequestParam(value = "userIds") String userIds) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = leaveAudit3LXFEntityService.saveOrUpdate(token, auditIds, userIds);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("三级单位离任审计安排 --分发（删除项目id,重新增加人员） 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "通过三级单位离任审计安排id 查询分发人员")
    @GetMapping(value = "getDistributeFundList", produces = "application/json;charset=utf-8")
    public JsonBean getDistributeFundList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "auditIds", description = "三级单位离任审计ID", required = true) @RequestParam(value = "auditIds") String auditIds) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = leaveAudit3LXFEntityService.getListByAuditId(token, auditIds);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("三级单位离任审计安排 --查新分发List 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "获取三级单位离任审计汇总表")
    @GetMapping(value = "selectLeaveAudit3LSummary", produces = "application/json;charset=utf-8")
    public JsonBean selectLeaveAudit3LSummary(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "queryYear", description = "筛选年度", required = false) @RequestParam(value = "queryYear", required = false) Integer queryYear) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = leaveAudit3LService.selectLeaveAudit3LSummary(token,queryYear);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("三级单位离任审计汇总表 --汇总List 接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    } 



    @Operation(summary = "导出数据接口", description = "导出全部")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request, 
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid,
                               LeaveAudit3LEntity vo 
    ) { 
        try {
            return leaveAudit3LService.exportData(response, token, vo,jdid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
 
    @Operation(summary = "导入数据接口", description = "file=?.xlsx")
    @PostMapping("/importData")
    public JsonBean importData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "jdid", description = "jdid", required = false)@RequestParam(value = "jdid", required = false, defaultValue = "") BigDecimal jdid,
                               @Parameter(name = "file", description = "文件", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {

            return leaveAudit3LService.importData(file, token,jdid);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }



}
