package com.huabo.audit.controller;

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

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.QuarterEntity;
import com.huabo.audit.service.QuarterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName QuarterController
 * @Description 计划管理-季度表
 * @DATE 2023/9/23
 */
@RestController
@Slf4j
@Tag(name="季度表",description="季度表")
@RequestMapping(value = "/quarter")
public class QuarterController {

    @Autowired
    private QuarterService quarterService;

    @GetMapping("/getList")
    @Operation(summary = "季度表列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "vo", description = "查询条件", required = false) QuarterEntity vo
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = quarterService.findAll(token,pageNumber,pageSize,vo);
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
            jsonBean = quarterService.findById(id);
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
                                 @Parameter(name = "quarterEntity", description = "实体", required = true) @RequestBody QuarterEntity quarterEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != quarterEntity.getId()){
                quarterService.updateEntity(quarterEntity);
            }else{
                quarterService.saveEntity(token,quarterEntity);
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
            quarterService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

}
