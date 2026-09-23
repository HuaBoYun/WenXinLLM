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
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import com.huabo.audit.service.ExpectLeaveService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ExpectLeaveController
 * @Description 计划管理-未委托及预计离任
 * @DATE 2023/9/14
 */
@RestController
@Slf4j
@Tag(name="计划管理-未委托及预计离任",description="计划管理-未委托及预计离任")
@RequestMapping(value = "/plan/leave/expect")
public class ExpectLeaveController {
    @Autowired
    private ExpectLeaveService expectLeaveService;

    @GetMapping("/getList")
    @Operation(summary = "未委托及预计离任列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "name", description = "姓名", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String name,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "teamLeader", description = "组长", required = false)@RequestParam(value = "teamLeader", required = false, defaultValue = "") String teamLeader
    ){
        JsonBean jsonBean = null;
        try{

            jsonBean = expectLeaveService.findAll(token,pageNumber,pageSize,name,teamLeader,projectName,ids);
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
            jsonBean = expectLeaveService.findById(id);
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
                                 @Parameter(name = "expectLeaveEntity", description = "实体", required = true) @RequestBody ExpectLeaveEntity expectLeaveEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != expectLeaveEntity.getId()){
                expectLeaveService.updateEntity(expectLeaveEntity);
            }else{
                expectLeaveService.saveEntity(token,expectLeaveEntity);
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
            expectLeaveService.deleteByIds(ids);
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
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids") String ids,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "name", description = "姓名", required = false)@RequestParam(value = "name", required = false, defaultValue = "") String name,
                            @Parameter(name = "teamLeader", description = "组长", required = false)@RequestParam(value = "teamLeader", required = false, defaultValue = "") String teamLeader
    ){
        log.info("未委托及预计离任导出Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "未委托及预计离任" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();

            JsonBean jsonBean = expectLeaveService.findAll(token,pageNumber,pageSize,name,teamLeader, projectName,ids);
            List<ExpectLeaveEntity> list = ((PageInfo) jsonBean.getData()).getTlist();
            List<Object[]> exportList = new ArrayList<>(list.size());
            String[] titles = {"序号","单位" ,"姓名", "预计退二线时间","审计时间","项目名称","任职时间(审计范围)","审计实施时间","组长","牵头人","主审","助审" };
            for (int i = 0; i < list.size(); i++) {
                ExpectLeaveEntity r = list.get(i);
                Object[] o = {
                        i+1,
                        r.getTblOrganization()!=null?r.getTblOrganization().getOrgname():"",
                        r.getName(),
                        r.getRetireTime(),
                        r.getAuditTime(),
                        r.getProjectName(),
                        r.getWorkStartTime()+"~"+r.getWorkEndTime(),
                        r.getDoAuditTime(),
                        r.getTeamLeaderName()!=null?r.getTeamLeaderName():"",
                        r.getLeaderName()!=null?r.getLeaderName():"",
                        r.getChiefReviewerName()!=null?r.getChiefReviewerName():"",
                        r.getDeputyReviewerName()!=null?r.getDeputyReviewerName():""
                };
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
        }catch (Exception e){
            e.printStackTrace();
            log.error("未委托及预计离任导出失败",e );
        }
    }
    @PostMapping("/import")
    @Operation(summary = "列表导入")
    public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws IOException {
        InputStream in = file.getInputStream();
        XSSFWorkbook workBook = new XSSFWorkbook(in);
        try{

            for (int i =0; i < workBook.getNumberOfSheets(); i++){
                XSSFSheet sheet = workBook.getSheetAt(i);
                expectLeaveService.resolveSheet(sheet,token);
            }
            return ResponseFormat.retParam(1,200);
        }catch (Exception e){
            e.printStackTrace();
            log.error("未委托及预计离任导入失败",e );
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
            expectLeaveService.distribute(ids,personIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
}
