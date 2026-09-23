package com.huabo.audit.controller;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsWgzzCljg;
import com.huabo.audit.oracle.entity.TblYqnsWgzzFlczyj;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtdz;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWthc;
import com.huabo.audit.oracle.entity.TblYqnsWgzzWtsl;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYsjgws;
import com.huabo.audit.oracle.entity.TblYqnsWgzzYstz;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblWgzzWghsService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 违规责任追究控制器
 * <p>提供违规责任追究的列表查询、新增、修改、删除等管理接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="违规责任追究",description="违规责任追究")
@RequestMapping(value = "/wgzrzj")
public class TblWgzrzjController {

    @Autowired
    TblWgzzWghsService tblWgzzWghsService;
    
    @Autowired
    TblAttachmentService tblAttachmentService;
    
    @GetMapping("/flczyjList")
    @Operation(summary = "分类处置意见-列表")
    public JsonBean flczyjList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.flczyjList(token,pageNumber,pageSize,tblYqnsWgzzFlczyj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/flczyjDetail")
    @Operation(summary = "分类处置意见-明细")
    public JsonBean flczyjDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.flczyjDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/flczyjSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "分类处置意见-新增/修改")
    public JsonBean flczyjSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzFlczyj", description = "实体", required = false) TblYqnsWgzzFlczyj tblYqnsWgzzFlczyj)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.flczyjSave(token, tblYqnsWgzzFlczyj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/flczyjDelete")
    @Operation(summary = "分类处置意见-删除")
    public JsonBean flczyjDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.flczyjDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/flczyjFileDelete")
    @Operation(summary = "分类处置意见-附件删除")
    public R flczyjFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.flczyjFilesDelete(token,attId);
    }
    
    @GetMapping("/flczyjFileList")
    @Operation(summary = "分类处置意见-附件列表")
    public JsonBean flczyjFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.flczyjFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    //==问题核查BEGIN
    @GetMapping("/wthcList")
    @Operation(summary = "问题核查-列表")
    public JsonBean wthcList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzWthc tblYqnsWgzzWthc){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wthcList(token,pageNumber,pageSize,tblYqnsWgzzWthc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/wthcDetail")
    @Operation(summary = "问题核查-明细")
    public JsonBean wthcDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wthcDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/wthcSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "问题核查-新增/修改")
    public JsonBean wthcSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzWthc", description = "实体", required = false) TblYqnsWgzzWthc tblYqnsWgzzWthc)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wthcSave(token, tblYqnsWgzzWthc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/wthcDelete")
    @Operation(summary = "问题核查-删除")
    public JsonBean wthcDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wthcDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/wthcFileDelete")
    @Operation(summary = "问题核查-附件删除")
    public R wthcFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.wthcFilesDelete(token,attId);
    }
    
    @GetMapping("/wthcFileList")
    @Operation(summary = "问题核查-附件列表")
    public JsonBean wthcFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wthcFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    //==问题定责BEGIN
    @GetMapping("/wtdzList")
    @Operation(summary = "问题定责-列表")
    public JsonBean wtdzList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzWtdz tblYqnsWgzzWtdz){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtdzList(token,pageNumber,pageSize,tblYqnsWgzzWtdz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/wtdzDetail")
    @Operation(summary = "问题定责-明细")
    public JsonBean wtdzDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtdzDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/wtdzSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "问题定责-新增/修改")
    public JsonBean wtdzSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzWtdz", description = "实体", required = false) TblYqnsWgzzWtdz tblYqnsWgzzWtdz)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtdzSave(token, tblYqnsWgzzWtdz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/wtdzDelete")
    @Operation(summary = "问题定责-删除")
    public JsonBean wtdzDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtdzDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/wtdzFileDelete")
    @Operation(summary = "问题定责-附件删除")
    public R wtdzFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.wtdzFilesDelete(token,attId);
    }
    
    @GetMapping("/wtdzFileList")
    @Operation(summary = "问题定责-附件列表")
    public JsonBean wtdzFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wtdzFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/wtdzIssued")
    @Operation(summary = "问题定责-下发")
    public JsonBean wtdzIssued(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "staffIds", description = "下发人员IDs(,分割)", required = true) @RequestParam("staffIds") String staffIds,
                                 @Parameter(name = "ids", description = "问题定责IDs(,分割)", required = true) @RequestParam("ids") String ids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wtdzIssued(token, staffIds, ids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    
    //==移送结果文书BEGIN
    @GetMapping("/ysjgwsList")
    @Operation(summary = "移送结果文书-列表")
    public JsonBean ysjgwsList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ysjgwsList(token,pageNumber,pageSize,tblYqnsWgzzYsjgws);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/ysjgwsDetail")
    @Operation(summary = "移送结果文书-明细")
    public JsonBean ysjgwsDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ysjgwsDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/ysjgwsSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "移送结果文书-新增/修改")
    public JsonBean ysjgwsSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzYsjgws", description = "实体", required = false) TblYqnsWgzzYsjgws tblYqnsWgzzYsjgws)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ysjgwsSave(token, tblYqnsWgzzYsjgws);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/ysjgwsDelete")
    @Operation(summary = "移送结果文书-删除")
    public JsonBean ysjgwsDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ysjgwsDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/ysjgwsFileDelete")
    @Operation(summary = "移送结果文书-附件删除")
    public R ysjgwsFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.ysjgwsFilesDelete(token,attId);
    }
    
    @GetMapping("/ysjgwsFileList")
    @Operation(summary = "移送结果文书-附件列表")
    public JsonBean ysjgwsFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.ysjgwsFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    
    //==移送台账BEGIN
    @GetMapping("/ystzList")
    @Operation(summary = "移送台账-列表")
    public JsonBean ystzList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzYstz tblYqnsWgzzYstz){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ystzList(token,pageNumber,pageSize,tblYqnsWgzzYstz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/ystzDetail")
    @Operation(summary = "移送台账-明细")
    public JsonBean ystzDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ystzDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/ystzSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "移送台账-新增/修改")
    public JsonBean ystzSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzYstz", description = "实体", required = false) TblYqnsWgzzYstz tblYqnsWgzzYstz)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ystzSave(token, tblYqnsWgzzYstz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/ystzDelete")
    @Operation(summary = "移送台账-删除")
    public JsonBean ystzDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.ystzDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/ystzFileDelete")
    @Operation(summary = "移送台账-附件删除")
    public R ystzFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.ystzFilesDelete(token,attId);
    }
    
    @GetMapping("/ystzFileList")
    @Operation(summary = "移送台账-附件列表")
    public JsonBean ystzFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.ystzFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    //==问题线索受理BEGIN
    @GetMapping("/wtslList")
    @Operation(summary = "问题线索受理-列表")
    public JsonBean wtslList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzWtsl tblYqnsWgzzWtsl){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtslList(token,pageNumber,pageSize,tblYqnsWgzzWtsl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/wtslDetail")
    @Operation(summary = "问题线索受理-明细")
    public JsonBean wtslDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtslDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/wtslSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "问题线索受理-新增/修改")
    public JsonBean wtslSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzWtsl", description = "实体", required = false) TblYqnsWgzzWtsl tblYqnsWgzzWtsl)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtslSave(token, tblYqnsWgzzWtsl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/wtslDelete")
    @Operation(summary = "问题线索受理-删除")
    public JsonBean wtslDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.wtslDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/wtslFileDelete")
    @Operation(summary = "问题线索受理-附件删除")
    public R wtslFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.wtslFilesDelete(token,attId);
    }
    
    @GetMapping("/wtslFileList")
    @Operation(summary = "问题线索受理-附件列表")
    public JsonBean wtslFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.wtslFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    //==处理结果BEGIN
    @GetMapping("/cljgList")
    @Operation(summary = "处理结果-列表")
    public JsonBean cljgList(@Parameter(name="token",required=true)@RequestHeader("token")String token,
                                  @Parameter(name="pageNumber",required=false)@RequestParam("pageNumber")Integer pageNumber,
                                  @Parameter(name="pageSize",required=false)@RequestParam("pageSize") Integer pageSize,
                                  TblYqnsWgzzCljg tblYqnsWgzzCljg){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.cljgList(token,pageNumber,pageSize,tblYqnsWgzzCljg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @GetMapping("/cljgDetail")
    @Operation(summary = "处理结果-明细")
    public JsonBean cljgDetail(@Parameter(description="token")@RequestHeader("token")String token,
                                  @Parameter(description="id")@RequestParam("id")BigDecimal id){
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.cljgDetail(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @RequestMapping(value = "/cljgSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "处理结果-新增/修改")
    public JsonBean cljgSave(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                     @Parameter(name = "tblYqnsWgzzCljg", description = "实体", required = false) TblYqnsWgzzCljg tblYqnsWgzzCljg)
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.cljgSave(token, tblYqnsWgzzCljg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @PostMapping("/cljgDelete")
    @Operation(summary = "处理结果-删除")
    public JsonBean cljgDelete(@Parameter(description="token")@RequestHeader("token")String token,
                                    @Parameter(description="id")@RequestParam("id") BigDecimal id) 
    {
        JsonBean jsonBean =null;
        try {
            jsonBean = tblWgzzWghsService.cljgDelete(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    @PostMapping("/cljgFileDelete")
    @Operation(summary = "处理结果-附件删除")
    public R cljgFilesDelete(
            @Parameter(description="token")@RequestHeader("token")String token,
            @Parameter(description="attId") @RequestParam("attId")String attId) throws Exception {
        return this.tblWgzzWghsService.cljgFilesDelete(token,attId);
    }
    
    @GetMapping("/cljgFileList")
    @Operation(summary = "处理结果-附件列表")
    public JsonBean cljgFileList(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "id", description = "业务主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblWgzzWghsService.cljgFileList(token,id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
}
