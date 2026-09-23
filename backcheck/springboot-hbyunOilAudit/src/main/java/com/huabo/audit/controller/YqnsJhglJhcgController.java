package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcg;
import com.huabo.audit.oracle.entity.TblYqnsJhglJhcgGL;
import com.huabo.audit.service.InterimAuditDetailService;
import com.huabo.audit.service.TblYqnsJhglJhcgGLService;
import com.huabo.audit.service.TblYqnsJhglJhcgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理-计划草稿
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_计划草稿",description="计划管理_计划草稿")
@RestController
@RequestMapping(value = "/jhgljhcg")
public class YqnsJhglJhcgController {

    @Resource
    TblYqnsJhglJhcgService service;


    @Resource
    TblYqnsJhglJhcgGLService tblYqnsJhglJhcgGLService;
    
    @Autowired
    private InterimAuditDetailService interimAuditDetailService;

    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "vo", description = "计划管理_计划草稿entity") @RequestBody TblYqnsJhglJhcg vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "列表查询接口")
    @GetMapping("/list")
    public JsonBean list(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsJhglJhcg vo
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口", description = "jhcgid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhcgid", description = "计划草稿id", required = true) @RequestParam("jhcgid") String jhcgid
    ) {
        try {
            return this.service.detail(token, jhcgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划草稿复制接口", description = "jhcgid=?")
    @PostMapping("/copyUniqueCg")
    public JsonBean copyUnique(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhcgid", description = "计划草稿id", required = true) @RequestParam("jhcgid") String jhcgid
    ) {
        try {
            return this.service.copyUnique(token, jhcgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    
    
    

    @Operation(summary = "删除关联引用记录接口", description = "id=?")
    @GetMapping("/deleteGL")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id", description = "删除关联的id", required = true) @RequestParam("id") String id
    ) {
        try {
            return tblYqnsJhglJhcgGLService.deleteGL(token, id);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除关联引用记录接口", description = "id=?")
    @GetMapping("/deleteGLByIds")
    public JsonBean deleteGLByIds(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhcgid", description = "计划草稿主键", required = true) @RequestParam("jhcgid") BigDecimal jhcgid,
                           @Parameter(name = "formid", description = "关联表单主键", required = true) @RequestParam("formid") BigDecimal formid
    ) {
        try {
            return tblYqnsJhglJhcgGLService.deleteGLByIds(token, jhcgid,formid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口", description = "ids=?,?")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJhglJhcg vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               TblYqnsJhglJhcg vo
    ) {
        try {
            return this.service.exportData(response, token, vo);
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
                               @Parameter(name = "file", description = "文件", required = true) @RequestParam("file") MultipartFile file
    ) {
        try {
            return this.service.importData(file, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @GetMapping("/getRzsjmxListDraftPlan")
    @Operation(summary = "计划初稿-任中审计明细列表 22")
    public JsonBean getRzsjmxListDraftPlan(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "orgName", description = "单位名称", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "teamLeaderId", description = "组长", required = false)@RequestParam(value = "teamLeaderId", required = false, defaultValue = "") String teamLeaderId,
                            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid,
                            @Parameter(name = "createyear", description = "年度", required = false)@RequestParam(value = "createyear", required = false, defaultValue = "") String createyear,
                            @Parameter(name = "sourceType", description = "来源类型 1-草稿 ，2-初稿，3-终稿 默认1", required = false)@RequestParam(value = "sourceType", required = false, defaultValue = "1") Integer sourceType,
                            @Parameter(name = "jhid", description = "选择的计划主键", required = false)@RequestParam(value = "jhid", required = false, defaultValue = "1") BigDecimal jhid
                            ){
        JsonBean jsonBean = null;
        try{
            jsonBean = interimAuditDetailService.getRzsjmxListDraftPlan(token,pageNumber,pageSize, orgName,teamLeaderId,projectName,createyear,tbid,sourceType,jhid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @Operation(summary = "计划初稿-工程项目结算汇总 31")
    @GetMapping("/getGcxmjshzList")
    public JsonBean getGcxmjshzList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getGcxmjshzList(token,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-建设项目投资完成情况 32")
    @GetMapping("/getJsxmtzList")
    public JsonBean getJsxmtzList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getJsxmtzList(token,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-三级单位离任审计上报情况 23")
    @GetMapping("/getSjdwlrsjSbList")
    public JsonBean getSjdwlrsjSbList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getSjdwlrsjSbList(token,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿关联数据修改接口")
    @RequestMapping(value = "/updateGl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean updateGl(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "gl", description = "计划管理_关联数据") @RequestBody TblYqnsJhglJhcgGL gl
    ) {
        try {
            return this.tblYqnsJhglJhcgGLService.saveOrUpdate(token, gl);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "计划初稿-工程项目结算汇总查看列表数据 31")
    @GetMapping("/getGcxmjsListByhz")
    public JsonBean getGcxmjsListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id", description = "计划草稿关联主键 -- 保存后传入") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "jsdw", description = "建设单位名称--未保存时传入 ") @RequestParam(name = "jsdw", required = false) String jsdw,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
                           
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getGcxmjsListByhz(token,id,jsdw,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-建设项目投资完成情况 32")
    @GetMapping("/getJsxmtzListByhz")
    public JsonBean getJsxmtzListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id -- 保存后传入", description = "计划草稿关联主键") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "tbdwName", description = "建设单位名称--未保存时传入 ") @RequestParam(name = "tbdwName", required = false) String tbdwName,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getJsxmtzListByhz(token,id,tbdwName,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-三级单位离任审计上报情况 23")
    @GetMapping("/getSjdwlrsjSbListByhz")
    public JsonBean getSjdwlrsjSbListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id", description = "计划草稿关联主键 -- 保存后传入") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "orgId", description = "填报单位主键--未保存时传入 ") @RequestParam(name = "orgId", required = false) BigDecimal orgId,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhcgGLService.getSjdwlrsjSbList(token,id,orgId,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
}

