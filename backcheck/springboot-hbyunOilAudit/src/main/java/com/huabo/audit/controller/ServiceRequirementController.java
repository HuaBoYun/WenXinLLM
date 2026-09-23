package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
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
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ServiceRequirementEntity;
import com.huabo.audit.service.ServiceRequirementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ServiceRequirementController
 * @Description 计划管理-服务需求
 * @DATE 2023/9/6
 */
@RestController
@Slf4j
@Tag(name="计划管理-服务需求",description="计划管理-服务需求")
@RequestMapping(value = "/plan/service/requirement")
public class ServiceRequirementController {
    @Autowired
    private ServiceRequirementService serviceRequirementService;

    @GetMapping("/getList")
    @Operation(summary = "需求列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "requirementNo", description = "序号", required = false)@RequestParam(value = "requirementNo", required = false, defaultValue = "") String requirementNo,
                            @Parameter(name = "auditItem", description = "审计事项", required = false)@RequestParam(value = "auditItem", required = false, defaultValue = "") String auditItem,
                            @Parameter(name = "queryYear", description = "创建年度", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear,
                            @Parameter(name = "projectType", description = "项目类别", required = false)@RequestParam(value = "projectType", required = false) String projectType
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = serviceRequirementService.findAll(token,pageNumber,pageSize,requirementNo,auditItem,queryYear,projectType);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/detail")
    @Operation(summary = "根据ID获取详细信息")
    public JsonBean getById(HttpServletRequest request,@Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id){
        JsonBean jsonBean = null;
        try{
            jsonBean = serviceRequirementService.findById(id);
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
                                 @Parameter(name = "rs", description = "实体", required = true) @RequestBody ServiceRequirementEntity serviceRequirementEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != serviceRequirementEntity.getId()){
                serviceRequirementService.updateEntity(serviceRequirementEntity);
            }else{
                serviceRequirementService.saveEntity(token,serviceRequirementEntity,0);
            }
            jsonBean= ResponseFormat.retParam(1,200,null);
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
            serviceRequirementService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
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
                           @Parameter(name = "auditItem", description = "审计项", required = false)@RequestParam(value = "auditItem", required = false, defaultValue = "")String auditItem,
                           @Parameter(name = "queryYear", description = "创建年度", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear) throws Exception{
        log.info("服务需求列表导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "服务需求列表" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            List<ServiceRequirementEntity> list = serviceRequirementService.findExprotList(token, pageNumber, pageSize, id, auditItem,queryYear,ids);
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号", "单位", "审计事项", "审计目的","重点关注内容","单位范围","时间范围", "项目类型", "备注", };
            for (int i = 0; i < list.size(); i++) {
                ServiceRequirementEntity r = list.get(i);
                Object[] o = {r.getRequirementNo(),r.getOrganization()!=null?r.getOrganization().getOrgname():"",r.getAuditItem(), r.getAuditPurpose(), r.getConcernsContent(), r.getUnitRange(), r.getTimeRange(), r.getProjectType(),r.getRemark()};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("服务需求建议列表导出失败",e );
        }
    }

    @PostMapping("/import")
    @Operation(summary = "列表导入")
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
    		@Parameter(name = "isCover", description = "是否覆盖 0-否 ，1-是  ，默认0", required = false)@RequestParam(value = "isCover", required = false, defaultValue = "0") Integer isCover,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        try{

            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                serviceRequirementService.resolveSheet(sheet,token,isCover);
            }
            return ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
            log.error("服务需求列表导入失败",e );
            return  ResponseFormat.retParam(0,1000,e.getMessage());
        }finally {
            //读取完毕则关闭流
            in.close();
            workBook.close();
        }

    }

    @PostMapping(value="/xf" , produces = "application/json; charset=utf-8")
    @Operation(summary = "下发接口")
    public JsonBean distribute(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                               @Parameter(name = "rs", description = "请求参数：ids,personIds", required = true) @RequestBody Map<String,String> params
    ){
        JsonBean jsonBean = null;
        try{
            String ids = params.get("ids");
            String personIds = params.get("personIds");
            if(StringUtil.isEmpty(ids)){
                return ResponseFormat.retParam(30001,"未选择下发的服务需求");
            }
            if(StringUtils.isEmpty(personIds)){
                return ResponseFormat.retParam(30001,"未选择下发的人员");
            }
            serviceRequirementService.distribute(ids,personIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getAutoNo")
    @Operation(summary = "获取序号")
    public JsonBean getAutoNo( HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception{
    	return this.serviceRequirementService.getAutoNo(token);
    }
}
