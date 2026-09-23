package com.huabo.audit.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsAuditSuperReport;
import com.huabo.audit.oracle.service.TblYqnsAuditSuperReportService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计督导报告控制器
 * <p>提供审计督导报告的分页查询、新增、修改、删除等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/audit/report")
@Tag(name="审计督导报告",description="审计督导报告")
public class TblYqnsAuditSuperReportController {

    @Autowired
    private TblYqnsAuditSuperReportService tblYqnsAuditSuperReportService;

    @GetMapping(value = "/getList", produces = "application/json;charset=utf-8")
    @Operation(summary = "审计督导报告列表")
    public JsonBean getList(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
            @Parameter(name = "tblYqnsAuditSuperReport", description = "查询条件实体", required = true) TblYqnsAuditSuperReport tblYqnsAuditSuperReport)
    {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblYqnsAuditSuperReportService.selectReportList(token, pageNumber, pageSize, tblYqnsAuditSuperReport);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @GetMapping("/getOne")
    @Operation(summary = "根据ID获取详细信息")
    public JsonBean getById(HttpServletRequest request, @Parameter(name = "id", description = "ID", required = false)@RequestParam(value = "id", required = false, defaultValue = "") Long id){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsAuditSuperReportService.selectReportById(id);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @PostMapping("/saveOrUpdateReport")
    @Operation(summary = "添加或修改报告")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "tblYqnsAuditSuperReport", description = "实体", required = true) @RequestBody TblYqnsAuditSuperReport entity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != entity.getId()){
                tblYqnsAuditSuperReportService.updateReport(token,entity);
            }else{
                tblYqnsAuditSuperReportService.saveReport(token,entity);
            }
            jsonBean= ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    @DeleteMapping("/deleteReport")
    @Operation(summary = "删除")
    public JsonBean deleteByIds(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "ids", description = "Id,多个用,号隔开", required = true) @RequestParam String ids
    ){
        JsonBean jsonBean = null;
        try{
            tblYqnsAuditSuperReportService.deleteReoprt(token,ids);
            jsonBean= ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }


    @Operation(summary = "审计督导报告 附件-删除（直接删除）")
    @GetMapping(value = "deleteFileAttach", produces = "application/json;charset=utf-8")
    public JsonBean deleteFileAttach(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "attId", description = "附件ID", required = true) @RequestParam(value = "attId") String attId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditSuperReportService.deleteFileAttach(token,attId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

}
