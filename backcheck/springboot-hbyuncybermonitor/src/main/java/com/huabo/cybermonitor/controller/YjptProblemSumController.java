package com.huabo.cybermonitor.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.cybermonitor.entity.TblNbkzRiskEntity;
import com.huabo.cybermonitor.entity.TblNbsjBugEntity;
import com.huabo.cybermonitor.entity.TblNbsjRisktolerability;
import com.huabo.cybermonitor.service.TblNbkzRiskService;
import com.huabo.cybermonitor.service.TblNbsjBugService;
import com.huabo.cybermonitor.service.TblNbsjSheetService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.TBlNbsjSheetVo;
import com.huabo.cybermonitor.vo.TblNbkzRiskVo;
import com.huabo.cybermonitor.vo.TblNbsjBugVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



@RestController
@Tag(name="问题汇总-缺陷管理",description="问题汇总-缺陷管理")
@RequestMapping(value = "/cyber/YjptProblemSumController")
public class YjptProblemSumController {

	private static final Logger log = LoggerFactory.getLogger(YjptProblemSumController.class);
    @Resource
    public TblNbsjBugService tblNbsjBugService;

    @Resource
    public TblNbkzRiskService nbkzRiskService;

    @Resource
    public TblNbsjSheetService tBlNbsjSheetService;
    /**
     * 缺陷管理列表
     */
    @Operation(summary = "workReportList")
    @GetMapping("/sjbb/defect_list")
    public JsonBean workReportList(HttpServletRequest request, TblNbsjBugVo tblNbsjBugVo,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                   @Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.bugPageList(token, pageNumber, pageSize,tblNbsjBugVo,orgid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-新增与修改
     */
    @RequestMapping(value = "/sjbb/defect_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改")
    public JsonBean defect_add(HttpServletRequest request,@Parameter(name = "bug", description = "实体", required = true) TblNbsjBugEntity bug,
                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
                               @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugAdd(bug,token,attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 缺陷管理-新增与修改-内规选择列表
     */
    @Operation(summary = "inner_common_qxwt")
    @GetMapping("/inner_common_qxwt")
    public JsonBean inner_common_qxwt(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.innerCommonQxwtList(token, pageNumber, pageSize,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-新增与修改-外规选择列表
     */
    @Operation(summary = "outer_common_qxwt")
    @GetMapping("/outer_common_qxwt")
    public JsonBean outer_common_qxwt(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.outerCommonQxwtList(token, pageNumber, pageSize,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 缺陷管理-新增与修改-选择内规保存
     */
    @RequestMapping(value = "/qxwt/add_inner_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-选择内规保存")
    public JsonBean add_inner_qxwt(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
                                   @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
                                   @Parameter(name = "innrulids", description = "内规ids", required = false)String innrulids)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugInnrulidsAdd(token,bugid,innrulids);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-新增与修改-选择外规保存
     */
    @RequestMapping(value = "/qxwt/add_outer_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷管理-新增与修改-选择外规保存")
    public JsonBean add_outer_qxwt(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
                                   @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
                                   @Parameter(name = "outrulids", description = "外规ids", required = false)String outrulids)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.bugOutrulidsAdd(token,bugid,outrulids);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 缺陷管理-关联内规查询
     */
    @Operation(summary = "inner_common_link")
    @GetMapping("/inner_common_link")
    public JsonBean inner_common_link(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.innerCommonLinkList(token, 1, 99,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-关联外规查询
     */
    @Operation(summary = "outer_common_link")
    @GetMapping("/outer_common_link")
    public JsonBean outer_common_link(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.outerCommonLinkList(token, 1, 99,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @Operation(summary = "删除")
    @GetMapping("/delete_qx_inner")
    public JsonBean delete_qx_inner(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "innrulids", description = "内规ids", required = false)String innrulids) {

        try {
            return this.tblNbsjBugService.bugInnrulidsDelete(token,bugid,innrulids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @Operation(summary = "删除")
    @GetMapping("/delete_qx_outer")
    public JsonBean delete_qx_outer(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "outrulids", description = "外规ids", required = false)String outrulids) {

        try {
            return this.tblNbsjBugService.bugOutrulidsDelete(token,bugid,outrulids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 缺陷管理-关联缺陷-选择保存
     */
    @RequestMapping(value = "/qxwt/add_defect_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "关联缺陷-选择保存")
    public JsonBean add_defect_qxwt(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
                                    @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
                                    @Parameter(name = "bugids", description = "关联bugids", required = false)String bugids)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defectQxwtAdd(token,bugid,bugids);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-关联缺陷查询
     */
    @Operation(summary = "defect_link")
    @GetMapping("/qxwt/defect_link")
    public JsonBean defect_link(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.defectLinkList(token, 1, 99,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-关联缺陷删除
     */
    @Operation(summary = "删除")
    @GetMapping("/delete_qx_child")
    public JsonBean delete_qx_child(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                    @Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
                                    @Parameter(name = "bugids", description = "关联bugids", required = false)String bugids) {

        try {
            return this.tblNbsjBugService.bugFatherDelete(token,bugid,bugids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 缺陷管理-删除
     */
    @Operation(summary = "workReportDelete")
    @GetMapping("/sjbb/defect_del")
    public JsonBean workReportDelete(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid) {

        try {
            return tblNbsjBugService.bugDelete(bugid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 缺陷管理-明细
     */
    @Operation(summary = "workReportDetail")
    @GetMapping("/sjbb/defect_detail")
    public JsonBean workReportDetail(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.findBugDetail(token,bugid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 缺陷管理-附件列表
     */
    @Operation(summary = "defect_file_list")
    @GetMapping("/sjbb/defect_file_list")
    public JsonBean defect_file_list(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "bugId", description = "业务主键", required = true) @RequestParam(value = "bugId", required = true) Integer bugId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defectFileList(token,bugId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 缺陷管理-附件删除
     */
    @Operation(summary = "defect_file_del")
    @GetMapping("/sjbb/defect_file_del")
    public R defect_file_del(HttpServletRequest request, HttpServletResponse response,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                             @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjBugService.removeAttInfoByAttId(token, attId);
    }

    /**
     * 缺陷管理-导出
     */
    @Operation(summary = "defect_file_export")
    @GetMapping("/sjbb/defect_file_export")
    public JsonBean defect_file_export(HttpServletRequest request, HttpServletResponse response,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                       @Parameter(name = "orgId", description = "组织编号", required = false)@RequestParam(value = "orgId", required = false) Integer orgId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugService.defect_file_export(token, orgId,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 缺陷管理-新增修改-获取缺陷级别
     */
    @Operation(summary = "bug_criterion_list")
    @GetMapping("/sjbb/bug_criterion_list")
    public JsonBean bug_criterion_list(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugService.findBugCriterion(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 风险发现列表
     */
    @Operation(summary = "risk_list")
    @GetMapping("/wthz/risk_list")
    public JsonBean risk_list(HttpServletRequest request, TblNbkzRiskVo tblNbkzRiskVo,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              @Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.riskPageList(token, pageNumber, pageSize,tblNbkzRiskVo, orgid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 风险发现-新增与修改
     */
    @RequestMapping(value = "/wthz/risk_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "风险发现-新增与修改")
    public JsonBean risk_add(HttpServletRequest request,@Parameter(name = "risk", description = "实体", required = true) TblNbkzRiskEntity risk,
                             @Parameter(name = "attids", description = "附件id数组", required = false)String attids,
                             @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.riskAdd(risk,token,attids);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }
    /**
     * 风险发现-删除
     */
    @Operation(summary = "risk_del")
    @GetMapping("/wthz/risk_del")
    public JsonBean risk_del(HttpServletRequest request,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                             @Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) Integer riskid) {

        try {
            return this.nbkzRiskService.riskDelete(riskid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 风险发现-明细
     */
    @Operation(summary = "risk_detail")
    @GetMapping("/wthz/risk_detail")
    public JsonBean risk_detail(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) Integer riskid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.findRiskDetail(token,riskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 风险发现-附件列表
     */
    @Operation(summary = "risk_file_list")
    @GetMapping("/wthz/risk_file_list")
    public JsonBean risk_file_list(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "riskId", description = "业务主键", required = true) @RequestParam(value = "riskId", required = true) Integer riskId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.risk_file_list(token,riskId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    /**
     * 风险发现-附件删除
     */
    @Operation(summary = "risk_file_del")
    @GetMapping("/wthz/risk_file_del")
    public R risk_file_del(HttpServletRequest request, HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                           @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.nbkzRiskService.removeAttInfoByAttId(token, attId);
    }

    /**
     * 风险发现-风险容忍度保存
     */
    @RequestMapping(value = "/wthz/fxrrd_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "风险发现-风险容忍度保存")
    public JsonBean fxrrd_save(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
                               @Parameter(name="tnr",description="",required=false) TblNbsjRisktolerability tnr)throws Exception{
        JsonBean jsonBean = null;
        try {
            jsonBean = this.nbkzRiskService.fxrrdAdd(token,tnr);
        } catch (Exception e) {
            ResponseFormat.retParam(1,1000,e.getMessage());
        }
        return jsonBean;
    }
    /**
     * 风险发现-风险容忍度删除
     */
    @Operation(summary = "fxrrd_del")
    @GetMapping("/wthz/fxrrd_del")
    public JsonBean fxrrd_del(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                              @Parameter(name = "toleid", description = "主键", required = true)@RequestParam(value = "toleid", required = true) Integer toleid) {

        try {
            return nbkzRiskService.fxrrdDelete(toleid, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 风险发现-风险容忍度查询
     */
    @Operation(summary = "fxrrd_list")
    @GetMapping("/wthz/fxrrd_list")
    public JsonBean fxrrd_list(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "riskid", description = "riskid", required = false) @RequestParam(value = "riskid", required = false) Integer riskid) {

        JsonBean jsonBean = null;
        try {
            jsonBean = nbkzRiskService.fxrrdLinkList(token, 1, 99,riskid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @Operation(summary = "dgAllPageList")
    @GetMapping("/sjss/dgAllPageList")
    public JsonBean dgAllPageList(HttpServletRequest request, TBlNbsjSheetVo tBlNbsjSheetVo,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                  @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tBlNbsjSheetService.dgAllPageList(token, pageNumber, pageSize,tBlNbsjSheetVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 底稿管理 详情
     */
    @Operation(summary = "dggl_detail")
    @GetMapping("/sjss/dggl_detail")
    public JsonBean dggl_detail(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "sheetid", description = "主键", required = true)@RequestParam(value = "sheetid", required = true) Integer sheetid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tBlNbsjSheetService.findNbsjSheetDetail(token,sheetid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }



}
