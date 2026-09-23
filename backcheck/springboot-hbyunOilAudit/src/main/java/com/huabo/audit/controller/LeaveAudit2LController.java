package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
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
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.huabo.audit.service.LeaveAudit2LService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName LeaveAudit2LController
 * @Description 计划管理-二级单位离任审计
 * @DATE 2023/9/14
 */
@RestController
@Slf4j
@Tag(name="计划管理-二级单位离任审计",description="计划管理-二级单位离任审计")
@RequestMapping(value = "/plan/leave/audit2L")
public class LeaveAudit2LController {
    @Autowired
    private LeaveAudit2LService leaveAudit2LService;

    @GetMapping("/getList")
    @Operation(summary = "二级单位离任审计列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "auditOrg", description = "被审计单位", required = false)@RequestParam(value = "auditOrg", required = false, defaultValue = "") String auditOrg,
                            @Parameter(name = "queryYear", description = "创建年度", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = leaveAudit2LService.findAll(token,pageNumber,pageSize,projectName,auditOrg,queryYear);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    
    @GetMapping("/getListDraftPlan")
    @Operation(summary = "计划初稿-二级单位离任审计列表")
    public JsonBean getListDraftPlan(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "auditOrg", description = "被审计单位", required = false)@RequestParam(value = "auditOrg", required = false, defaultValue = "") String auditOrg,
                            @Parameter(name = "queryYear", description = "创建年度", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear,
                            @Parameter(name = "sourceType", description = "查询来源 ，1-草稿 2，初稿 3-终稿 默认1", required = false)@RequestParam(value = "sourceType", required = false ,defaultValue = "1") Integer sourceType,
                            @Parameter(name = "jhid", description = "选择的计划主键", required = false)@RequestParam(value = "jhid", required = false ,defaultValue = "1") BigDecimal jhid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = leaveAudit2LService.getListByDraftPlan(token,pageNumber,pageSize,projectName,auditOrg,queryYear,sourceType,jhid);
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
            jsonBean = leaveAudit2LService.findById(id);
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
                                 @Parameter(name = "leaveAudit2LEntity", description = "实体", required = true) @RequestBody LeaveAudit2LEntity leaveAudit2LEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != leaveAudit2LEntity.getId()){
            	 jsonBean= leaveAudit2LService.updateEntity(leaveAudit2LEntity);
            }else{
            	 jsonBean=leaveAudit2LService.saveEntity(token,leaveAudit2LEntity,0);
            }
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
            leaveAudit2LService.deleteByIds(ids);
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
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "auditOrg", description = "被审计单位", required = false)@RequestParam(value = "auditOrg", required = false, defaultValue = "") String auditOrg,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "queryYear", description = "创建年度", required = false)@RequestParam(value = "queryYear", required = false) Integer queryYear
    ){
        log.info("二级单位离任审计导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "二级单位离任审计" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            List<LeaveAudit2LEntity> list = leaveAudit2LService.findExportList(token,pageNumber,pageSize,projectName,auditOrg,queryYear,ids);
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号", "审计项目名称", "被审计单位","审计任职期间","委托书编号","委托时间" };
            for (int i = 0; i < list.size(); i++) {
                LeaveAudit2LEntity r = list.get(i);
                Object[] o = {
                        r.getLeaveNo(),
                        r.getProjectName(),
                        r.getAuditOrg()!=null?r.getAuditOrg().getOrgname():"",
                        (r.getAuditStartTime()!=null?r.getAuditStartTime().toString():"") + "~" + (r.getAuditEndTime()!=null?r.getAuditEndTime().toString():""),
                        r.getEntrustNo(),
                        r.getEntrustTime()
                };
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("二级单位离任审计导出失败",e );
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
                leaveAudit2LService.resolveSheet(sheet,token,isCover);
            }
            return ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
            log.error("二级单位离任审计导入失败",e );
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
            leaveAudit2LService.distribute(ids,personIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    @GetMapping("/getAutoNo")
    @Operation(summary = "获取编号")
    public JsonBean getAutoNo( HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) throws Exception{
    	return this.leaveAudit2LService.getAutoNo(token);
    }
    
}
