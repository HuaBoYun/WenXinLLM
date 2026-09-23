package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsProjectWeerklyEntity;
import com.huabo.audit.service.ImplementPlanService;
import com.huabo.audit.service.ProjectWeerklyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ProjectWeerklyController
 * @Description 审计实施-审计项目运行情况
 * @DATE 2024/08/15
 */
@RestController
@Slf4j
@Tag(name="审计实施-审计项目运行情况",description="审计实施-审计项目运行情况")
@RequestMapping(value = "/project/weerkly")
public class ProjectWeerklyController {

    @Autowired
    private ImplementPlanService implementPlanService;
	@Autowired
	private ProjectWeerklyService projectWeerklyService;


    @GetMapping("/getList")
    @Operation(summary = "审计实施-审计项目运行情况-周报列表")
    public JsonBean getList(HttpServletRequest request,TblYqnsProjectWeerklyEntity week,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = projectWeerklyService.findAll(token,pageNumber,pageSize,week,null);
        }catch (Exception e){
            e.printStackTrace(); 
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
    
    
    @GetMapping("/export")
    @Operation(summary = "审计实施-审计项目运行情况-导出")
    public void export(HttpServletRequest request,HttpServletResponse response,TblYqnsProjectWeerklyEntity week,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "ids", description = "选择的ID集合", required = false) @RequestParam(value = "ids",required = false) String ids,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ){
        log.info("审计项目运行情况Excel");
        response.setContentType("application/binary;charset=UTF-8");
        try{
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "审计项目运行情况" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            pageSize=9999;
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
             List<TblYqnsProjectWeerklyEntity> alldc = projectWeerklyService.findAlldc(token,pageNumber,pageSize,week,ids);
            List<Object[]> exportList = new ArrayList<>(alldc.size());
            String[] titles = {"序号","项目名称","项目组" ,"实施单位", "被审计单位","组长","主审","助审","现场开始时间","现场实际结束时间","周报时间","本周工作","下周工作" };
            for (int i = 0; i < alldc.size(); i++) {
            	TblYqnsProjectWeerklyEntity r = alldc.get(i);
                Object[] o = {
                        i+1, 
                        r.getImplementPlanEntities()!=null?r.getImplementPlanEntities().getProjectName():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null) ?r.getImplementPlanEntities().getXmqd().getAuditGroup():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null)?r.getImplementPlanEntities().getXmqd().getSsorgname():"",
                        r.getImplementPlanEntities()!=null?r.getImplementPlanEntities().getAuditOrgName():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null)?r.getImplementPlanEntities().getXmqd().getGroupLeader():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null)?r.getImplementPlanEntities().getXmqd().getZsname():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null)?r.getImplementPlanEntities().getXmqd().getAssistApprover():"",
                        (r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null && r.getImplementPlanEntities().getXmqd().getXcsrarttime()!=null)?format.format(r.getImplementPlanEntities().getXmqd().getXcsrarttime()):"",
                        //(r.getImplementPlanEntities()!=null && r.getImplementPlanEntities().getXmqd()!=null && r.getImplementPlanEntities().getXmqd().getXcendtime()!=null)?format.format(r.getImplementPlanEntities().getXmqd().getXcendtime()):"",
                        r.getEndTimeSite()!=null?format.format(r.getEndTimeSite()):"",
                        r.getWeekDate()!=null?format.format(r.getWeekDate()):"",
                        r.getWeekWork()!=null?r.getWeekWork():"",
                        r.getNextWeekWork()!=null?r.getNextWeekWork():""
                };
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, "1");
        }catch (Exception e){
            e.printStackTrace();
            log.error("审计项目运行情况导出失败",e );
        }
    }
    
    

    @PostMapping(value="/saveOrUpdate" , produces = "application/json; charset=utf-8")
    @Operation(summary = "审计实施-审计项目运行情况-周报列表-添加或修改信息")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "Listentity", description = "实体", required = true) @RequestBody List<TblYqnsProjectWeerklyEntity> Listentity
    ){
        JsonBean jsonBean = null;
        try{
            if (Listentity.size() > 0){
                for (TblYqnsProjectWeerklyEntity weerklyEntity : Listentity) {
                    if(null != weerklyEntity.getId()){
                        jsonBean = projectWeerklyService.updateEntity(token, weerklyEntity);
                    }else{ 
                        jsonBean = projectWeerklyService.saveEntity(token,weerklyEntity);
                    }
                }
            }
            jsonBean= ResponseFormat.retParam(1,200,jsonBean);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),jsonBean);
        }
        return jsonBean;
    }



    @Operation(summary = "审计实施-审计项目运行情况-周报列表--删除（直接删除）")
    @GetMapping(value = "delete", produces = "application/json;charset=utf-8")
    public JsonBean delete(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-审计实施-审计项目运行情况-周报列表-ID", required = true) @RequestParam(value = "id") BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = projectWeerklyService.deleteone(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施-审计项目运行情况-周报列表--删除（直接删除） ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }

    
    
    
    @Operation(summary = "审计实施-审计项目运行情况-详情接口")
    @GetMapping(value = "getone", produces = "application/json;charset=utf-8")
    public JsonBean getone(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
            @Parameter(name = "id", description = "-审计实施-审计项目运行情况-周报列表-ID", required = true) @RequestParam(value = "id") BigDecimal id) {
        JsonBean jsonBean = new JsonBean();
        try {
            jsonBean = projectWeerklyService.getone(token, id);
        } catch (ServiceException se) {
            throw se;
        } catch (Exception e) {
            log.error("审计实施-审计项目运行情况-周报列表--删除（直接删除） ", e);
            jsonBean.setCode(0);
            jsonBean.setMsg(e.getMessage());
            jsonBean.setData(null);
        }
        return jsonBean;
    }




    @GetMapping("/findAuditProjectOperationStatusAllList")
    @Operation(summary = "审计实施-审计项目运行情况-审计项目运行情况汇总")
    public JsonBean findAuditProjectOperationStatusAllList(HttpServletRequest request,TblYqnsProjectWeerklyEntity week,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = projectWeerklyService.findAuditProjectOperationStatusAllList(token,pageNumber,pageSize,week,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
	
}
