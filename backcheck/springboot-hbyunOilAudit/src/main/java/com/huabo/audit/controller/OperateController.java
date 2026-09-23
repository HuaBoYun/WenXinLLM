package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.service.TblYqnsOperateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


/**
 * 操作导航控制器
 * <p>提供审计操作导航的分页查询等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="操作导航接口",description="操作导航接口")
@RequestMapping(value = "/operate")
public class OperateController {
    @Autowired
    private TblYqnsOperateService tblYqnsOperateService;


    @GetMapping("/gethzList")
    @Operation(summary = "消息列表")
    public JsonBean gethzList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "status", description = "状态", required = false)@RequestParam(value = "status", required = false) Integer status,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ){
        JsonBean jsonBean = null;
        try{
            String ssmkid="";
            if(status!=null && status==1){//查询项目准备阶段内容
                ssmkid="1465,1466,1497,1363,1496,1362,60095,1356,662";
            }
            if(status!=null && status==2){//查询审计实施
                ssmkid="1498,1353,1351,1349,1400,1402,22222";
            }
            if(status!=null && status==3){//查询报告内容
                ssmkid="1342,1343";
            }
            if(status!=null && status==4){//查询整改内容
                ssmkid="1490,1494,1491,1492,1493";
            }
            

            jsonBean =tblYqnsOperateService.findhzAllList(token, pageNumber, pageSize,ssmkid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }


    @GetMapping("/getList")
    @Operation(summary = "消息列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "status", description = "状态", required = false)@RequestParam(value = "status", required = false) Integer status,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean =tblYqnsOperateService.findAllList(token, pageNumber, pageSize,status);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean; 
    }
    

    @GetMapping("/detail")
    @Operation(summary = "根据ID获取详细信息")
    public JsonBean getById(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "operid", description = "operid", required = false)@RequestParam(value = "operid", required = false, defaultValue = "") BigDecimal operid){
        JsonBean jsonBean = null;
        try{
            jsonBean = tblYqnsOperateService.findByid(token, operid); 
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    } 

    @PostMapping(value="/complete" , produces = "application/json; charset=utf-8")
    @Operation(summary = "操作完成")
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "operid", description = "主键id", required = true) @RequestParam BigDecimal operid
    ){
        JsonBean jsonBean = null; 
        try{
        	
            jsonBean= tblYqnsOperateService.complete(token, operid);
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
                                @Parameter(name = "operid", description = "主键id", required = true) @RequestParam BigDecimal operid
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean= tblYqnsOperateService.deleteone(token, operid);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    
}
