package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.InterimAuditDetailEntity;
import com.huabo.audit.oracle.entity.TblYqnsRzmxtb;
import com.huabo.audit.oracle.service.TblYqnsRzmxtbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.InterimAuditDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName InterimAuditDetailController
 * @Description 计划管理-任中审计明细
 * @DATE 2024/04/15
 */
@RestController
@Slf4j
@Tag(name="计划管理-任中审计明细",description="计划管理-任中审计明细")
@RequestMapping(value = "/plan/interim/audit")
public class InterimAuditDetailController {
    @Autowired
    private InterimAuditDetailService interimAuditDetailService;
    
    
    
    
    
    @Autowired
    private TblYqnsRzmxtbService tblYqnsRzmxtbService;
    
    
    
    
    
    
    @GetMapping("/gettdList")
    @Operation(summary = "任中审计明细-填报列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            SjdwjdVo vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsRzmxtbService.findAllList(token, pageNumber, pageSize, vo);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @PostMapping(value ="/tbsaveOrUpdate", produces = "application/json; charset=utf-8")
    @Operation(summary = "添加或修改填报信息")
    public JsonBean jdsaveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "glids", description = "glids", required = false)@RequestParam(value = "glids", required = false, defaultValue = "") String glids,
                                 TblYqnsRzmxtb jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=tblYqnsRzmxtbService.saveOrupdate(token, jd, glids);

        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("/tbdetail")
    @Operation(summary = "根据ID获取填报详细信息")
    public JsonBean jddetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsRzmxtbService.findByid(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @DeleteMapping("/tbdelete")
    @Operation(summary = "删除填报信息")
    public JsonBean jddelete(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean=tblYqnsRzmxtbService.deleteone(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    

    @GetMapping("/getList")
    @Operation(summary = "任中审计明细列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "orgName", description = "单位名称", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "teamLeaderId", description = "组长", required = false)@RequestParam(value = "teamLeaderId", required = false, defaultValue = "") String teamLeaderId,
                            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid,
                            @Parameter(name = "createyear", description = "年度", required = false)@RequestParam(value = "createyear", required = false, defaultValue = "") String createyear

                            ){
        JsonBean jsonBean = null;
        try{

            jsonBean = interimAuditDetailService.findAll(token,pageNumber,pageSize, orgName,teamLeaderId,projectName,createyear,tbid,ids);
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
            jsonBean = interimAuditDetailService.findById(id);
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
                                 @Parameter(name = "interimAuditDetailEntity", description = "实体", required = true) @RequestBody InterimAuditDetailEntity interimAuditDetailEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != interimAuditDetailEntity.getId()){
            	jsonBean= interimAuditDetailService.updateEntity(interimAuditDetailEntity);
            }else{
            	jsonBean=interimAuditDetailService.saveEntity(token,interimAuditDetailEntity);
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
            interimAuditDetailService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @PostMapping("/import")
    @Operation(summary = "导入")
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
    	JsonBean jsonBean = null;
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        try{

            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                jsonBean=interimAuditDetailService.resolveSheet(sheet,token);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("任中审计明细导入失败",e );
            jsonBean=  ResponseFormat.retParam(0,1000,e.getMessage());
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }
        return jsonBean;
    }
    
    //导出
    @GetMapping("/export")
    @Operation(summary = "列表导出")
    public void exportList( HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
            @Parameter(name = "orgName", description = "单位名称", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid,
            @Parameter(name = "teamLeader", description = "组长", required = false)@RequestParam(value = "teamLeader", required = false, defaultValue = "") String teamLeader
            ){
        log.info("任中审计明细导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "任中审计明细" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            JsonBean jsonBean = interimAuditDetailService.findAll(token,pageNumber,pageSize, orgName,teamLeader,projectName,null,tbid,ids);
            List<InterimAuditDetailEntity> list = ((PageInfo) jsonBean.getData()).getTlist();
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号","单位","现任领导名称","职务","出生年月","预计二线时间", "审计情况","项目名称", "审计范围","审计时间","截至本年底未审年限","最近一次审计项目名称", "是否建议审计", "原因", "任职起始时间", "任职结束时间", "审计实施时间", "组长", "副组长", "牵头人", "主审", "助审", "项目类型","备注",};
            for (int i = 0; i < list.size(); i++) {
            	InterimAuditDetailEntity r = list.get(i);
                Object[] o = {r.getSerialNumber(),r.getOrg().getOrgname(),r.getLdname(),r.getLdzw(),r.getCsym(),r.getYjexsj(),r.getAuditInfo(), r.getProjectName(),r.getSjfwstarttime()+"-"+r.getSjfwendtime(), r.getAuditTime(), r.getUnauditYear(), r.getZjprojectname(),
                		r.getProjectType(),r.getTextarea(),r.getWorkStartTime(),r.getWorkEndTime(),r.getDoAuditTime(),r.getTeamLeaderId(),r.getSubTeamLeaderId()
                		,r.getLeaderId(),r.getChiefReviewerId(),r.getDeputyReviewerId(),r.getProjectlx(),r.getRemarks()};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("任中审计明细导出失败",e );
        }
    }




}
