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
import com.huabo.audit.oracle.entity.ScoreManageEntity;
import com.huabo.audit.service.ScoreManageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ScoreManageController
 * @Description 评议管理-评分管理
 * @DATE 2023/10/09
 */
@RestController
@Slf4j
@Tag(name="评议管理-评分管理",description="评议管理-评分管理")
@RequestMapping(value = "/reviewManagement/scoreManage")
public class ScorreManageController {
    @Autowired
    private ScoreManageService scoreManageService;

    @GetMapping("/getList")
    @Operation(summary = "评分管理列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "type", description = "评分类型：1.审计实施方案制定及执行2.底稿质量 3.报告质量 4.审计管理系统上线 5.奖惩事项", required = false)@RequestParam(value = "type", required = false, defaultValue = "") Integer type,
                            @Parameter(name = "status", description = "是否启用 1.启用 2.废弃", required = false)@RequestParam(value = "status", required = false, defaultValue = "") Integer status
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = scoreManageService.findAll(token,pageNumber,pageSize,type,status);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @GetMapping("/getAvailableScoreItems")
    @Operation(summary = "获取可用评分标准，质量评议模块 新增时使用")
    public JsonBean getAvailableScoreItems(HttpServletRequest request,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                                @Parameter(name = "type", description = "评分类型：1.审计实施方案制定及执行2.底稿质量 3.报告质量 4.审计管理系统上线 5.奖惩事项", required = false)@RequestParam(value = "type", required = false, defaultValue = "") Integer type
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = scoreManageService.findAvailableScoreItemsByType(type);
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
            jsonBean = scoreManageService.findById(id);
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
                                 @Parameter(name = "scoreManageEntity", description = "实体", required = true) @RequestBody ScoreManageEntity scoreManageEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != scoreManageEntity.getId()){
                scoreManageService.updateEntity(scoreManageEntity);
            }else{
                scoreManageService.saveEntity(token,scoreManageEntity);
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
            scoreManageService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @PostMapping("/changeStatus")
    @Operation(summary = "修改状态")
    public JsonBean changeStatus(HttpServletRequest request,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                 @Parameter(name = "scoreManageEntity", description = "实体", required = true) @RequestBody ScoreManageEntity scoreManageEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(scoreManageEntity != null && scoreManageEntity.getId() != null){
                scoreManageService.updateEntityStatus(scoreManageEntity);
                jsonBean= ResponseFormat.retParam(1,200,null);
            }else{
                jsonBean = ResponseFormat.retParam(0,1000,"ID不能为空");
            }
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
}
