package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;
import com.huabo.audit.oracle.service.TblYqnsAuditMyManuVerifyService;
import com.huabo.audit.oracle.service.TblYqnsAuditMyManuscriptService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author GJ.C
 * @CLASS_NAME: TblYqnsAuditOverseeRecordsController
 * @PACKAGE_NAME: com.huabo.audit.controller
 * @date 2023/10/10 14:00.
 * @version: V1.0
 * @description: 央企内审-审计实施-我的底稿
 */
@RestController
@Slf4j
@Tag(name="我的底稿",description="我的底稿")
@RequestMapping(value = "/audit/MyManuscript")
public class TblYqnsAuditMyManuscriptController {

    @Resource
    private TblYqnsAuditMyManuscriptService tblYqnsAuditMyManuscriptService;
    @Resource
    private TblYqnsAuditMyManuVerifyService tblYqnsAuditMyManuVerifyService;

    
    @Operation(summary = "我的底稿-审计工作记录-删除（关联关系）")
    @GetMapping(value = "delWorkRecordRela", produces = "application/json;charset=utf-8")
    public JsonBean delWorkRecordRela(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "workReprotId", description = "工作审计记录主键", required = true) @RequestParam(value = "workReprotId") Long workReprotId,
            @Parameter(name = "myDraftId", description = "我的底稿主键", required = true) @RequestParam(value = "myDraftId") Long myDraftId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.delMyDraftWorkReportRela(token, workReprotId,myDraftId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 删除我的底稿-下方审计查证事实单个信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    

    @Operation(summary = "我的底稿--获取我的底稿列表-分页") 
    @GetMapping(value = "getMyManuscriptPage", produces = "application/json;charset=utf-8")
    public JsonBean getMyManuscriptPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "staffId", description = "筛选条件-人员主键", required = false) @RequestParam(value = "staffId", required = false) BigDecimal staffId,
            @Parameter(name = "xmnd", description = "筛选条件-项目年度起始", required = false) @RequestParam(value = "xmnd", required = false) Integer xmnd,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getMyManuscriptPage(token, pageNumber, pageSize, vo,staffId,xmnd);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean; 
    }



    @Operation(summary = "我的底稿--获取我的底稿列表 通过TypeId")
    @GetMapping(value = "getMyManuscriptListByTypeId", produces = "application/json;charset=utf-8")
    public JsonBean getMyManuscriptListByTypeId(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "templateId", description = "工程项目-ID", required = false) @RequestParam(value = "templateId", required = false) String templateId,
            @Parameter(name = "typeId", description = "类型-ID", required = true) @RequestParam(value = "typeId") String typeId) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getMyManuscriptListByTypeId(token, typeId,templateId);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "我的底稿--获取我的底稿-单个详情信息")
    @GetMapping(value = "getMyManuscriptById", produces = "application/json;charset=utf-8")
    public JsonBean getRecordsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "我的底稿-ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getMyManuscriptById(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取我的底稿-单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    } 


    @Operation(summary = "我的底稿--新增/更新")
    @PostMapping(value = "saveOrUpdate", produces = "application/json;charset=utf-8")
    public JsonBean increaseOrUpdateAuditProject(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "type", description = "我的底稿审批-发起人复核", required = false)@RequestParam(value = "type", required = false, defaultValue = "")String type, 
            @Parameter(name = "vo", description = "我的底稿entity") @RequestBody TblYqnsAuditMyManuscriptEntity vo ) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.saveOrUpdate(token, vo,type);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("基础配置-我的底稿 -- 新增/更新接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
        }

        return jsonBean;
    }


    @Operation(summary = "我的底稿--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "我的底稿ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.delete(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 删除我的底稿单个信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "我的底稿-下方审计查证事实-删除（直接删除）")
    @GetMapping(value = "deleteMyManuVerify", produces = "application/json;charset=utf-8")
    public JsonBean deleteMyManuVerify(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "我的底稿-下方审计查证事实ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuVerifyService.delete(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 删除我的底稿-下方审计查证事实单个信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


    @Operation(summary = "底稿管理列表--获取底稿管理列表-分页")
    @GetMapping(value = "dggl_list", produces = "application/json;charset=utf-8")
    public JsonBean dggl_list(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "底稿entity") TblYqnsAuditMyManuscriptEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.dggl_list(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) { 
            throw se;
        } catch (Exception e) {
            log.error("底稿管理列表 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "底稿管理列表--获取底稿管理列表-单个详情信息")
    @GetMapping(value = "getManuscriptById", produces = "application/json;charset=utf-8")
    public JsonBean getManuscriptById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "我的底稿-ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getManuscriptById(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("底稿管理列表 -- 获取底稿管理列表-单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


//    /**
//     * 底稿管理-导出
//     */
//    @RequestMapping(value = "/sjss/project_standard_dg_export",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
//    @Operation(summary = "底稿管理-导出")
//    public JsonBean project_standard_dg_export(HttpServletRequest request, HttpServletResponse response,
//                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception {
//
//        JsonBean jsonBean = new JsonBean();
//        try {
//            jsonBean =  tblYqnsAuditMyManuscriptService.excelUtils(response,token);
//        } catch (ServiceException se) {
//            throw se;
//        } catch (Exception e) {
//            log.error("底稿管理列表 -- 导出异常", e);
//            jsonBean.setCode(0);
//            jsonBean.setMsg(e.getMessage());
//            jsonBean.setData(null);
//        }
//        return jsonBean;
//
//    }


    @Operation(summary = "审计发现--获取审计发现列表-分页")
    @GetMapping(value = "question_store_list", produces = "application/json;charset=utf-8")
    public JsonBean question_store_list(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
            @Parameter(name = "vo", description = "底稿entity") TblYqnsAuditMyManuscriptEntity vo) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.question_store_list(token, pageNumber, pageSize, vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计发现列表 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    @Operation(summary = "审计发现列表--获取审计发现列表-单个详情信息")
    @GetMapping(value = "getAuditFindingsById", produces = "application/json;charset=utf-8")
    public JsonBean getAuditFindingsById(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "id", description = "我的底稿-ID", required = true) @RequestParam(value = "id") Long id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getManuscriptById(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计发现列表 -- 获取审计发现列表-单个详情信息接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("export")
    @Operation(summary = "我的底稿-列表导出")
    public JsonBean exportList( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo,
            @Parameter(name = "idList", description = "我的底稿id集合") @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList
    ){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{
        	
        	jsonBean =  tblYqnsAuditMyManuscriptService.excelUtils(response,token,vo,idList);
        	
//            String date = String.valueOf(System.currentTimeMillis());
//            String fileName = "我的底稿" + "_" + date + ".xlsx";
//            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
//            ServletOutputStream outputStream = response.getOutputStream();
//
//            JsonBean jsonBean = tblYqnsAuditMyManuscriptService.getMyManuscriptPage(token, 1, 10000, vo);
//            List<TblYqnsAuditMyManuscriptEntity> list = ((com.huabo.audit.util.PageInfo)(((HashMap<String, Object>)jsonBean.getData()).get("pageInfo"))).getTlist();
//            List<Object[]> exportList = new ArrayList<>(list.size());
//            String[] titles = {"底稿编号", "底稿名称", "审计项目名称","审计事项","被审计单位名称","创建时间" };
//            for (int i = 0; i < list.size(); i++) {
//                TblYqnsAuditMyManuscriptEntity r = list.get(i);
//                Object[] o = {r.getDraftNumber(),r.getDraftName(), r.getProjectName(), r.getAuditMatters(), r.getAuditeeName(), r.getCreateTime()};
//                exportList.add(o);
//            }
//
//            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBean; 
    }

    @Operation(summary = "我的底稿--底稿清单--汇总", description = "汇总查询数据-按照底稿导出")
    @GetMapping(value = "/getDraftListPage", produces = "application/json;charset=utf-8")
    public JsonBean getDraftListPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = true) @RequestParam(value = "pageNumber", defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = true) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize
            ) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.getDraftListPage(token, pageNumber, pageSize,vo);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("我的底稿 -- 获取列表接口异常", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("qdexport")
    @Operation(summary = "底稿清单-列表导出")
    public JsonBean qdexport( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo,
            @Parameter(name = "idList", description = "我的底稿id集合") @RequestParam(value = "idList", required = false, defaultValue = "") List<String> idList){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{
        	
        	jsonBean =  tblYqnsAuditMyManuscriptService.qdexcelUtils(response,token,vo,idList);
        	
        }catch (Exception e){
            e.printStackTrace();
        }
        return jsonBean;
    }


    @GetMapping("exportWord")
    @Operation(summary = "底稿清单-列表导出Word")
    public JsonBean exportWord( HttpServletResponse response,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "vo", description = "我的底稿entity") TblYqnsAuditMyManuscriptEntity vo){
        response.setContentType("application/binary;charset=UTF-8");
        JsonBean jsonBean = new JsonBean();
        try{

            jsonBean =  tblYqnsAuditMyManuscriptService.excelWord(response,token,vo);

        }catch (Exception e){ 
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    @Operation(summary = "底稿管理--根据项目底稿自动生成编号")
    @GetMapping(value = "autoCode", produces = "application/json;charset=utf-8")
    public JsonBean autoCode(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = tblYqnsAuditMyManuscriptService.autoCode(token);
        } catch (ServiceException se) { 
            throw se;
        } catch (Exception e) {
            log.error("底稿管理--根据项目底稿自动生成编号", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }


}
