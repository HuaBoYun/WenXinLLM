package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.huabo.audit.oracle.entity.TblYqnsJhglJh;
import com.huabo.audit.service.TblYqnsJhglJhGLService;
import com.huabo.audit.service.TblYqnsJhglJhService;
import com.huabo.audit.service.TblYqnsJhglJhchugGLService;
import com.huabo.audit.service.TblYqnsJhglJhchugService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangys
 * @description 计划管理_计划
 * @createDate 2023-09-07 16:46:40
 */
@Tag(name="计划管理_计划",description="计划管理_计划")
@RestController
@RequestMapping(value = "/jhgljh")
public class YqnsJhglJhController {

    @Resource
    TblYqnsJhglJhService service;
    
    @Resource
    TblYqnsJhglJhchugService tblYqnsJhglJhchugService;
    
    @Resource
    TblYqnsJhglJhchugGLService tblYqnsJhglJhchugGLService; 
     
    @Resource
    TblYqnsJhglJhGLService tblYqnsJhglJhGLService;
     
    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @RequestBody  TblYqnsJhglJh vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) { 
        	e.printStackTrace();
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
                         TblYqnsJhglJh vo
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "通过项目审计类型列表查询接口", description = "ssxmlx=?")
    @GetMapping("/findListToSsxmlx")
    public JsonBean findListToSsxmlx(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsJhglJh vo
    ) {
        try {
            return this.service.findListToSsxmlx(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "单个详情接口", description = "jhid=?")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJhglJh vo
    ) {
        try {
            return this.service.detail(token, vo);
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
            return tblYqnsJhglJhGLService.deleteGL(token, id);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "删除关联引用记录接口", description = "id=?")
    @GetMapping("/deleteGLByIds")
    public JsonBean deleteGLByIds(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhid", description = "计划终稿主键", required = true) @RequestParam("jhid") BigDecimal jhid,
                           @Parameter(name = "formid", description = "关联表单主键", required = true) @RequestParam("formid") BigDecimal formid
    ) {
        try {
            return tblYqnsJhglJhGLService.deleteGLByIds(token, jhid,formid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "删除记录接口", description = "ids=?,?")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsJhglJh vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "导出数据接口", description = "ids=?,?")
    @GetMapping("/exportData")
    public JsonBean exportData(HttpServletRequest request,
                               HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               TblYqnsJhglJh vo
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
    
    @Operation(summary = "获取计划草稿接口详情数据", description = "jhcgid=?")
    @GetMapping("/detailJhCg")
    public JsonBean detailJhCg(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhcgid", description = "计划草稿id", required = true) @RequestParam("jhcgid") String jhcgid
    ) {
        try {
            return this.tblYqnsJhglJhchugService.detailJhCgByChugao(token, jhcgid);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "计划初稿-三级单位离任审计上报情况 23")
    @GetMapping("/getSjdwlrsjSbList")
    public JsonBean getSjdwlrsjSbList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhchugid", description = "计划初稿id", required = true) @RequestParam("jhchugid") String jhchugid,
                           @Parameter(name = "relaid", description = "当前页面已选择的关联主键", required = false) @RequestParam("relaid") String relaid
    ) throws Exception {
        try {
            return tblYqnsJhglJhchugGLService.getJhChuGHuizongList(token,jhchugid,relaid,"23");
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-工程项目结算汇总 31")
    @GetMapping("/getGcxmjshzList")
    public JsonBean getGcxmjshzList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhchugid", description = "计划初稿id", required = true) @RequestParam("jhchugid") String jhchugid,
                           @Parameter(name = "relaid", description = "当前页面已选择的关联主键", required = false) @RequestParam("relaid") String relaid
    ) throws Exception {
        try {
            return tblYqnsJhglJhchugGLService.getJhChuGHuizongList(token,jhchugid,relaid,"31");
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-建设项目投资完成情况 32")
    @GetMapping("/getJsxmtzList")
    public JsonBean getJsxmtzList(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "jhchugid", description = "计划初稿id", required = true) @RequestParam("jhchugid") String jhchugid,
                           @Parameter(name = "relaid", description = "当前页面已选择的关联主键", required = false) @RequestParam("relaid") String relaid
    ) throws Exception {
        try {
        	return tblYqnsJhglJhchugGLService.getJhChuGHuizongList(token,jhchugid,relaid,"32");
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
  //汇总数据查看明细
    @Operation(summary = "计划初稿-三级单位离任审计上报情况 23")
    @GetMapping("/getSjdwlrsjSbListByhz")
    public JsonBean getSjdwlrsjSbListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "relaId", description = "计划草稿关联主键 -- 未保存") @RequestParam(name = "relaId", required = false) BigDecimal relaId,
                           @Parameter(name = "id", description = "计划初稿关联主键--保存后") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "type", description = "标识从哪块进入") @RequestParam(name = "type", required = false) String type,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhGLService.getSjdwlrsjSbList(token,relaId,id,pageNumber,pageSize,type);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-工程项目结算汇总查看列表数据 31")
    @GetMapping("/getGcxmjsListByhz")
    public JsonBean getGcxmjsListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "relaId", description = "计划草稿关联主键 -- 未保存") @RequestParam(name = "relaId", required = false) BigDecimal relaId,
                           @Parameter(name = "id", description = "计划初稿关联主键--保存后") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
                           
    ) throws Exception {
        try {
            return tblYqnsJhglJhGLService.getGcxmjsListByhz(token,relaId,id,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
    
    @Operation(summary = "计划初稿-建设项目投资完成情况 32")
    @GetMapping("/getJsxmtzListByhz")
    public JsonBean getJsxmtzListByhz(HttpServletRequest request,HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "relaId", description = "计划草稿关联主键 -- 未保存") @RequestParam(name = "relaId", required = false) BigDecimal relaId,
                           @Parameter(name = "id", description = "计划初稿关联主键--保存后") @RequestParam(name = "id", required = false) BigDecimal id,
                           @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ) throws Exception {
        try {
            return tblYqnsJhglJhGLService.getJsxmtzListByhz(token,relaId,id,pageNumber,pageSize);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }
}

