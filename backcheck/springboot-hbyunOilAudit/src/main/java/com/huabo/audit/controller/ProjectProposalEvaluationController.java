package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
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

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ProjectProposalEvaluationEntity;
import com.huabo.audit.oracle.entity.TblYqnsLxjytb;
import com.huabo.audit.oracle.service.TblYqnsLxjytbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.service.ProjectProposalEvaluationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author CJ
 * @ClassName ProjectProposalEvaluationController
 * @Description 计划编制-立项建议专业评估
 * @DATE 2024/5/28
 */
@RestController
@Slf4j
@Tag(name="计划编制-立项建议专业评估",description="计划编制-立项建议专业评估")
@RequestMapping(value = "/plan/project/evaluation")
public class ProjectProposalEvaluationController {
    @Autowired
    private ProjectProposalEvaluationService projectProposalEvaluationService;
    @Autowired
    private TblYqnsLxjytbService tblYqnsLxjytbService;
    
    @Resource
    private UserProvider userProvider;
    
    @GetMapping("/getLxZxsjHzChooseList")
    @Operation(summary = "立项专业评估建议汇总-选择立项数据列表")
    public JsonBean getLxZxsjHzChooseList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "rs", description = "实体中projectType字段：生产经营传财务  基建与投资传工程") ProjectProposalEvaluationEntity projectProposalEvaluationEntity

    ){
        JsonBean jsonBean = null;
        try{ 
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            jsonBean = projectProposalEvaluationService.getLxZxsjHzChooseList(token,pageNumber,pageSize,projectProposalEvaluationEntity);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @PostMapping("/setLxZxsjHzShowList")
    @Operation(summary = "立项专业评估建议汇总-保存选中的数据")
    public JsonBean setLxZxsjHzShowList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "idStrs", description = "立项专业评估建议主键逗号拼接字符串,", required = false)@RequestParam(value = "idStrs", required = false) String idStrs){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            jsonBean = projectProposalEvaluationService.setLxZxsjHzShowList(token,idStrs);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getLxZxsjHzDetailList")
    @Operation(summary = "立项专业评估建议汇总详细列表")
    public JsonBean getLxZxsjHzDetailList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "rs", description = "实体中projectType字段：生产经营传财务  基建与投资传工程") ProjectProposalEvaluationEntity projectProposalEvaluationEntity
    ){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            jsonBean = projectProposalEvaluationService.getLxZxsjHzDetailList(token,pageNumber,pageSize,projectProposalEvaluationEntity);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/gettdList")
    @Operation(summary = "立项建议专业评估-填报列表")
    public JsonBean getjdList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            SjdwjdVo vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsLxjytbService.findAllList(token, pageNumber, pageSize, vo);
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
                                 TblYqnsLxjytb jd
    ){
        JsonBean jsonBean = null;
        try{
        	jsonBean=tblYqnsLxjytbService.saveOrupdate(token, jd, glids);

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
            jsonBean = tblYqnsLxjytbService.findByid(token, tbid);
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
            jsonBean=tblYqnsLxjytbService.deleteone(token, tbid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    

    @GetMapping("/getEvaluationList")
    @Operation(summary = "立项建议专业评估列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id,
                            @Parameter(name = "ids", description = "序号", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "projectPurpose", description = "目的及理由", required = false)@RequestParam(value = "projectPurpose", required = false, defaultValue = "") String projectPurpose,
                            @Parameter(name = "projectType", description = "业务类型", required = false)@RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,
                            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid,
                            @Parameter(name = "createyear", description = "创建年度", required = false)@RequestParam(value = "createyear", required = false, defaultValue = "") String createyear
    ){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            jsonBean = projectProposalEvaluationService.findAll(token,pageNumber,pageSize,id,projectName,projectType,projectPurpose,createyear,tbid,ids);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/detail")
    @Operation(summary = "根据ID获取详细信息")
    public JsonBean getById(HttpServletRequest request,@Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") BigDecimal id){
        JsonBean jsonBean = null;
        try{
            jsonBean = projectProposalEvaluationService.findById(id);
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
                                 @Parameter(name = "rs", description = "实体", required = true) @RequestBody ProjectProposalEvaluationEntity projectProposalEvaluationEntity,
                                 @Parameter(name = "attids", description = "附件id数组", required = false) @RequestParam(value = "attids", required = false, defaultValue = "") String attids
    ){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            if(projectProposalEvaluationEntity.getId()!=null&&projectProposalEvaluationEntity.getId().compareTo(BigDecimal.ZERO) != 0){
            	jsonBean=projectProposalEvaluationService.updateEntity(projectProposalEvaluationEntity,attids);
            }else{
            	jsonBean= projectProposalEvaluationService.saveEntity(token,projectProposalEvaluationEntity,attids);
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
                                @Parameter(name = "id", description = "主键id", required = true) @RequestParam BigDecimal id
    ){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
             if (id.compareTo(BigDecimal.ZERO) != 0) {
            	  projectProposalEvaluationService.deleteByIds(id);
            	  jsonBean= ResponseFormat.retParam(1,200,null);
             }
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean; 
    }
 
    @PostMapping("/getLxZxsjList")
    @Operation(summary = "计划初稿-专项审计关联列表")
    public JsonBean getLxZxsjList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "rs", description = "实体中projectType字段：生产经营传财务  基建与投资传工程", required = true) @RequestBody ProjectProposalEvaluationEntity projectProposalEvaluationEntity
    ){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
            jsonBean = projectProposalEvaluationService.getLxZxsjList(token,pageNumber,pageSize,projectProposalEvaluationEntity);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @GetMapping("/export")
    @Operation(summary = "列表导出")
    public void exportList( HttpServletResponse response,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "")String projectName,
                            @Parameter(name = "projectPurpose", description = "目的及理由", required = false)@RequestParam(value = "projectPurpose", required = false, defaultValue = "") String projectPurpose,
                            @Parameter(name = "projectType", description = "业务类型", required = false)@RequestParam(value = "projectType", required = false, defaultValue = "") String projectType,
                            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid,
                            @Parameter(name = "createyear", description = "创建年度", required = false)@RequestParam(value = "createyear", required = false, defaultValue = "") String createyear) {

    	log.info("立项建议专业评估列表导出Excel");
        response.setContentType("application/binary;charset=UTF-8"); 
        try{
        	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "立项建议专业评估列表" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            pageSize=99999;
            JsonBean jsonBean = projectProposalEvaluationService.findAll(token,pageNumber,pageSize,id,projectName,projectType,projectPurpose,createyear,tbid,ids);
            List<ProjectProposalEvaluationEntity> list = ((PageInfo) jsonBean.getData()).getTlist();
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"排序","财务类/工程类", "建议科室", "项目类型","审计项目名称","立项理由及审计目的","重点关注内容", "单位范围", "时间范围/起始","时间范围/结束", };
            for (int i = 0; i < list.size(); i++) {
            	ProjectProposalEvaluationEntity r = list.get(i);
                Object[] o = { r.getSortNumber(),r.getProjectType(),r.getDepartmentName(),r.getItemType(), r.getProjectName(), r.getProjectPurpose(), r.getConcernsContent(), r.getUnitRange(),r.getTimeRangel()!=null?sdf.format(r.getTimeRangel()):"",r.getTimeRangeR()!=null?sdf.format(r.getTimeRangeR()):""};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("立项建议专业评估列表导出失败",e );
        }
    }
    @PostMapping("/import")
    @Operation(summary = "列表导入")
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        JsonBean jsonBean = null;
        try{

            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                jsonBean= projectProposalEvaluationService.resolveSheet(sheet,token);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("立项建议专业评估列表导入失败",e );
            jsonBean=  ResponseFormat.retParam(0,1000,e.getMessage());
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }
        return jsonBean;

    }
    
    
    @PostMapping(value="/updateAuditScope" , produces = "application/json; charset=utf-8")
    @Operation(summary = "计划初稿-修改审计范围")
    public JsonBean updateAuditScope(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "rs", description = "实体", required = true) @RequestBody ProjectProposalEvaluationEntity projectProposalEvaluationEntity
){
        JsonBean jsonBean = null;
        try{
        	 TblStaffUtil loginStaff = userProvider.get();
             if (loginStaff == null) {
                 return ResponseFormat.retParam(0, 20006, null);
             }
                projectProposalEvaluationService.updateAuditScope(projectProposalEvaluationEntity.getId(),projectProposalEvaluationEntity.getAuditScope());
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
}
