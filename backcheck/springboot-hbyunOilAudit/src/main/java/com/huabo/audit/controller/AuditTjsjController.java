package com.huabo.audit.controller;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.service.ExpectLeaveService;
import com.huabo.audit.service.InterimAuditDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * 最近一次审计情况控制器
 * <p>提供最近一次审计情况的分页查询等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="最近一次审计情况-所有接口",description="最近一次审计情况-所有接口")
@Slf4j
public class AuditTjsjController {
	
	@Autowired
    private ExpectLeaveService expectLeaveService;
	
	@Autowired
    private InterimAuditDetailService interimAuditDetailService;
	
	@GetMapping("/wwtsj/getList")
    @Operation(summary = "未委托及预计离任列表数据")
    public JsonBean getList1(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "name", description = "姓名", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String name,
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
	
	
	@GetMapping("/ywtsj/getList")
    @Operation(summary = "已委托未实施列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "ids", description = "ids", required = false)@RequestParam(value = "ids", required = false, defaultValue = "") String ids,
                            @Parameter(name = "orgName", description = "单位名称", required = false)@RequestParam(value = "orgName", required = false, defaultValue = "") String orgName,
                            @Parameter(name = "projectName", description = "项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "teamLeaderId", description = "组长", required = false)@RequestParam(value = "teamLeaderId", required = false, defaultValue = "") String teamLeaderId,
                            @Parameter(name = "createyear", description = "年度", required = false)@RequestParam(value = "createyear", required = false, defaultValue = "") String createyear

                            ){
        JsonBean jsonBean = null;
        try{

            jsonBean = interimAuditDetailService.findAll(token,pageNumber,pageSize, orgName,teamLeaderId,projectName,createyear,null,ids);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

}
