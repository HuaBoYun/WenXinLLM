package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjSjjyk;
import com.huabo.audit.service.TblNbsjSjjykService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 审计经验库控制器
 * <p>提供审计经验库的分页查询、新增、修改、删除等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="审计经验库",description="审计经验库")
@RequestMapping(value = "/audit/jyk")
public class TblNbsjSjjykController {

    @Autowired
    TblNbsjSjjykService tblNbsjSjjykService;


    @GetMapping("/getjykList")
    @Operation(summary = "审计经验库-列表")
    public JsonBean getjykList(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                               @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                               @Parameter(name = "code", description = "编号", required = false) @RequestParam(value = "code", required = false) String code,
                               @Parameter(name = "tatle", description = "标题", required = false) @RequestParam(value = "tatle", required = false) String tatle,
                               @Parameter(name = "overview", description = "概述", required = false) @RequestParam(value = "overview", required = false) String overview,
                               @Parameter(name = "experiencetype", description = "经验类型", required = false) @RequestParam(value = "experiencetype", required = false) String experiencetype) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.findAll(code, tatle, pageNumber, pageSize, token, experiencetype, overview);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/mergejykInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计经验库-新增或修改")
    public JsonBean mergejykInfo(HttpServletRequest request,
                                 @Parameter(name = "jykid", description = "主键 ，如果主键为空则新增信息", required = false) @RequestParam(value = "jykid", required = false) BigDecimal jykid,
                                 @Parameter(name = "jyk", description = "审计模板实体", required = true) TblNbsjSjjyk jyk,
                                 @Parameter(name = "attids", description = "上传附件的ID", required = false) @RequestParam(value = "attids", required = false) String attids,
                                 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.saveOrUpdate(jyk, token, jykid, attids);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @GetMapping("/getjykDetail")
    @Operation(summary = "审计经验库-查看详情信息")
    public JsonBean getjykDetail(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "jykid", description = "主键id", required = true) @RequestParam(value = "jykid", required = true) BigDecimal jykid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.findByjykid(jykid, token);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/deljykInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计经验库-删除信息")
    public JsonBean deljykInfo(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "jykid", description = "模板主键id", required = true) @RequestParam(value = "jykid", required = true) BigDecimal jykid
    ) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.delete(jykid, token);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getjykAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计经验库获取所属的附件")
    public JsonBean getjykAttInfo(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "jykid", description = "模板主键id", required = true) @RequestParam(value = "jykid", required = true) BigDecimal jykid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.getAttListByjykid(token, jykid);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @RequestMapping(value = "/deljykAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计经验库删除选择的附件")
    public JsonBean deljykAttInfo(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "attid", description = "附件ID ", required = true) @RequestParam(value = "attid", required = true) BigDecimal attid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.delAttListByattId(token, attid);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/getjykBydatapreid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "根据资料id获取关联经验库内容")
    public JsonBean getjykBydatapreid(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "dataperid", description = "资料主键id", required = true) @RequestParam(value = "dataperid", required = true) Integer dataperid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.findAllByDatapreID(token, dataperid);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @RequestMapping(value = "/deljykbyid", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "根据id删除关联经验库内容")
    public JsonBean deljykbyid(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "jykid", description = "经验库主键id", required = true) @RequestParam(value = "jykid", required = true) BigDecimal jykid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSjjykService.delAttListByjykId(token, jykid);
        } catch (Exception e) {
            e.printStackTrace();
            jsonBean = ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }
}
