package com.huabo.system.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.service.TblSystemLanguageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 系统语言切换控制器
 * <p>提供多语言配置的列表查询、合并、详情等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name = "language", description = "系统语言切换相关接口")
@RequestMapping("/language")
public class LanguageController {
	
	@Resource
	private TblSystemLanguageService tblSystemLanguageService;
	
	@RequestMapping(value = "/conversion/getList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="1.1获取语言转换信息分页")
    public JsonBean conversionGetList(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
        @Parameter(name = "infoname",description="筛选条件-标题", required = false)@RequestParam(value = "infoname", required = false) String infoname,
        @Parameter(name = "targetlanguage",description="筛选条件-翻译语言", required = false)@RequestParam(value = "targetlanguage", required = false) String targetlanguage) throws Exception {
        return this.tblSystemLanguageService.conversionGetList(token,pageNumber,pageSize,infoname,targetlanguage);
    }
	
	@RequestMapping(value = "/conversion/menger", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="1.2语言转换信息修改或保存")
    public JsonBean conversionmenger(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoname",description="标题", required = true)@RequestParam(value = "infoname", required = true) String infoname,
        @Parameter(name = "targetlanguage",description="翻译语言", required = true)@RequestParam(value = "targetlanguage", required = true) String targetlanguage,
        @Parameter(name = "infoid",description="语言转换信息主键，为空新增，不为空修改", required = false)@RequestParam(value = "infoid", required = false) String infoid) throws Exception {
        return this.tblSystemLanguageService.conversionmodify(token,infoname,targetlanguage,infoid);
    }
	
	@RequestMapping(value = "/conversion/detial", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="1.3语言转换信息详情")
    public JsonBean conversiondetial(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid) throws Exception {
        return this.tblSystemLanguageService.conversiondetial(token,infoid);
    }
	
	@RequestMapping(value = "/conversion/remove", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="1.4语言转换信息删除")
    public JsonBean conversionremove(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="主键", required = true)@RequestParam(value = "infoid", required = true) String infoid) throws Exception {
        return this.tblSystemLanguageService.conversionremove(token,infoid);
    }
	
	
	@RequestMapping(value = "/conversion/getTranslateList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="1.5语言转换信息获取翻译信息列表分页")
    public JsonBean getTranslateList(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid,
        @Parameter(name = "pageNumber",description="起始页 默认为1", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
        @Parameter(name = "pageSize",description="每页数量默认为20", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
        @Parameter(name = "trantext",description="筛选条件-翻译文本（翻译后的）", required = false)@RequestParam(value = "trantext", required = false) String trantext,
        @Parameter(name = "menuname",description="筛选条件-菜单名称（翻译前的）", required = false)@RequestParam(value = "menuname", required = false) String menuname
    	) throws Exception {
        return this.tblSystemLanguageService.getTranslateList(token,infoid,pageNumber,pageSize,trantext,menuname);
    }
	
	@RequestMapping(value = "/translate/menger", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="1.6翻译信息保存")
    public JsonBean translateMenger(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid,
        @Parameter(name = "configid",description="基础语言配置主键，为空新增，不为空修改", required = false)@RequestParam(value = "configid", required = false) String configid,
        @Parameter(name = "trantext",description="翻译文本（翻译后的）", required = true)@RequestParam(value = "trantext", required = true) String trantext,
        @Parameter(name = "menuname",description="菜单名称（翻译前的）", required = true)@RequestParam(value = "menuname", required = true) String menuname
    	) throws Exception {
        return this.tblSystemLanguageService.translateMenger(token,infoid,configid,trantext,menuname);
    }
	
	@RequestMapping(value = "/translate/clearUp", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="1.7翻译信息清除(只删除翻译信息，保留菜单信息)")
    public JsonBean translateClearUp(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid,
        @Parameter(name = "configid",description="基础语言配置主键，为空新增，不为空修改", required = false)@RequestParam(value = "configid", required = false) String configid
    	) throws Exception {
        return this.tblSystemLanguageService.translateClearUp(token,infoid,configid);
    }
	
	@RequestMapping(value = "/translate/remove", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="1.8翻译信息删除(翻译信息、菜单信息全部删除，菜单信息type=-2 系统预制的无法删除)")
    public JsonBean translateRemove(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid,
        @Parameter(name = "configid",description="基础语言配置主键，为空新增，不为空修改", required = false)@RequestParam(value = "configid", required = false) String configid
    	) throws Exception {
        return this.tblSystemLanguageService.translateRemove(token,infoid,configid);
    }
	
	@RequestMapping(value = "/translate/getUserConfig", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="2. 根据转义信息主键获取 用户配置信息")
    public JsonBean translateGetUserConfig(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "infoid",description="语言转换信息主键", required = true)@RequestParam(value = "infoid", required = true) String infoid
    	) throws Exception {
        return this.tblSystemLanguageService.translateGetUserConfig(token,infoid);
    }
	
	
	
	
	
}
