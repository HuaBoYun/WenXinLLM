package com.huabo.audit.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
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
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.ProjectSuggestionNoticeEntity;
import com.huabo.audit.service.ProjectSuggestionNoticeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rui
 * @ClassName ProjectSuggestionController
 * @Description 计划管理-立项建议通知
 * @DATE 2024/03/21
 */
@RestController
@Slf4j
@Tag(name="计划管理-立项建议通知",description="计划管理-立项建议通知")

@RequestMapping(value = "/plan/projectSuggestion/notice")
public class ProjectSuggestionNoticeController {
    @Autowired
    private ProjectSuggestionNoticeService projectSuggestionNoticeService;
    
    @Resource
    private UserProvider userProvider;

    @GetMapping("/getList")
    @Operation(summary = "立项建议通知列表")
    public JsonBean getList(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @Parameter(name = "id", description = "序号", required = false)@RequestParam(value = "id", required = false, defaultValue = "") String id,
                            @Parameter(name = "name", description = "通知名称", required = false)@RequestParam(value = "name", required = false, defaultValue = "") String name
    ){
        JsonBean jsonBean = null;
        try{
            jsonBean = projectSuggestionNoticeService.findAll(token,pageNumber,pageSize,id,name);
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
            jsonBean = projectSuggestionNoticeService.findById(id);
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
                                 @Parameter(name = "rs", description = "实体", required = true) @RequestBody ProjectSuggestionNoticeEntity projectSuggestionNoticeEntity
    ){
        JsonBean jsonBean = null;
        try{
            if(null != projectSuggestionNoticeEntity.getId()){
            	jsonBean=projectSuggestionNoticeService.updateEntity(projectSuggestionNoticeEntity);
            }else{
            	jsonBean= projectSuggestionNoticeService.saveEntity(token,projectSuggestionNoticeEntity);
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
            projectSuggestionNoticeService.deleteByIds(ids);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }

    @PostMapping(value="/xf" , produces = "application/json; charset=utf-8")
    @Operation(summary = "下发接口")
    public JsonBean distribute(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                               @Parameter(name = "rs", description = "请求参数：ids,personIds", required = true) @RequestBody Map<String,String> params
    ){
        JsonBean jsonBean = null;
        try{
        	TblStaffUtil user = userProvider.get();
            if (user == null ){
                return  ResponseFormat.retParam(0,20006,null);
            }

            String ids = params.get("ids");
            String personIds = params.get("personIds");
            if(StringUtil.isEmpty(ids)){
                return ResponseFormat.retParam(30001,"未选择下发的立项建议"); 
            }
            if(StringUtils.isEmpty(personIds)){
                return ResponseFormat.retParam(30001,"未选择下发的人员");
            }
            projectSuggestionNoticeService.distribute(token,ids,personIds);
            jsonBean= ResponseFormat.retParam(1,200,null);
        }catch (Exception e){
            e.printStackTrace();
            jsonBean= ResponseFormat.retParam(0,e.getMessage(),null);
        }
        return jsonBean;
    }
}
