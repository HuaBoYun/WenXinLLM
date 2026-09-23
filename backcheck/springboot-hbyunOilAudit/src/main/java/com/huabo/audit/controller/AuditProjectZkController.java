package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.AuditProjectZkEntity;
import com.huabo.audit.oracle.entity.ImplementPlanEntity;
import com.huabo.audit.oracle.mapper.ImplementPlanMapper;
import com.huabo.audit.oracle.mapper.TblNbsjStaffSelectMapper;
import com.huabo.audit.service.AuditProjectZkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName AuditProjectZkController
 * @Description 审计实施-审计项目追款
 * @DATE 2023/10/17
 */
@RestController
@Slf4j
@Tag(name="审计实施-审计项目追款",description="审计实施-审计项目追款")
@RequestMapping(value = "/audit/auditProjectZk")
public class AuditProjectZkController {
    @Autowired
    private AuditProjectZkService auditProjectZkService;

    @Resource
    private TblNbsjStaffSelectMapper tblNbsjStaffSelectMapper;

    @Autowired
    private ImplementPlanMapper implementPlanMapper;
    
    @Resource
    private UserProvider userProvider;

    @GetMapping("/getList")
    @Operation(summary = "审计实施-审计项目追款列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "projectName", description = "审计项目名称", required = false)@RequestParam(value = "projectName", required = false, defaultValue = "") String projectName,
                            @Parameter(name = "money", description = "追款金额", required = true)@RequestParam(value = "money", required = true, defaultValue = "") BigDecimal money
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = auditProjectZkService.findAll(token,pageNumber,pageSize,projectName,money);
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
            jsonBean = auditProjectZkService.findById(id);
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
                                 @Parameter(name = "auditProjectZkEntity", description = "实体", required = true) @RequestBody AuditProjectZkEntity auditProjectZkEntity
    ) throws Exception {
        JsonBean jsonBean = null;
        TblStaffUtil loginStaff = userProvider.get();
        if(loginStaff == null) {
            return ResponseFormat.retParam(0,20006,null);
        }
        //==查询当前实施的项目！
        ImplementPlanEntity tnp = this.getCurrenNbsjProjectByLoginStaff(loginStaff.getStaffid());
        if(tnp == null) {
            return ResponseFormat.retParam(0,30003,null);
        }
        BigDecimal projectId = tnp.getId();
        if(null == projectId) {
            return ResponseFormat.retParam(0,30003,null);
        }
        auditProjectZkEntity.setProjectId(projectId);
        auditProjectZkEntity.setCreatorStaffId(loginStaff.getStaffid());
        try{
            if(null != auditProjectZkEntity.getId()){
                auditProjectZkService.updateEntity(auditProjectZkEntity);
            }else{
                auditProjectZkService.saveEntity(token,auditProjectZkEntity);
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
            auditProjectZkService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    //获取当前实施项目；
    public ImplementPlanEntity getCurrenNbsjProjectByLoginStaff(BigDecimal staffid) throws Exception {
    	BigDecimal projectId = this.tblNbsjStaffSelectMapper.selectProjectIdByStaffId(staffid);
        if (projectId == null) {
            return null;
        }
        return implementPlanMapper.selectById(projectId.toString());
    }

}
