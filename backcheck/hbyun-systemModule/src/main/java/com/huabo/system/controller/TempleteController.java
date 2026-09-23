package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.system.service.TblTemplateDuService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计准备阶段
 */
@RestController
@Slf4j
@Tag(name = "百度编辑器模板接口", description = "百度编辑器模板接口")
@RequestMapping(value = "/temp")
public class TempleteController {

	@Resource
	public TblTemplateDuService tblTemplateDuService;
	
	/**
     * 	#  百度编辑器 保存魔板
     */
    @PostMapping("/save")
    @Operation(summary="百度编辑器 保存魔板")
    public JsonBean save(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tempType",description="模板类型，审计通知书传入tempType=sjtzs",required=true)@RequestParam(value = "tempType", required = true) String tempType,
    		@Parameter(name="tempTitle",description="模板标题",required=true)@RequestParam(value = "tempTitle", required = true) String tempTitle,
    		@Parameter(name="tempContent",description="模板内容",required=true)@RequestParam(value = "tempContent", required = true) String tempContent) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblTemplateDuService.saveEntity(token,tempType,tempTitle,tempContent);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    
    /**
     * 	#  百度编辑器 删除魔板
     */
    @GetMapping("/delete")
    @Operation(summary="百度编辑器 删除魔板")
    public JsonBean delete(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="tempId",description="模板Id",required=true)@RequestParam(value = "tempId", required = true) BigDecimal tempId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblTemplateDuService.removeEntity(token,tempId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
    

    /**
     * 	#  百度编辑器 获取魔板
     */
    @GetMapping("/find")
    @Operation(summary="百度编辑器 获取集合")
	public JsonBean find(HttpServletRequest request,@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			@Parameter(name="tempType",description="模板类型，审计通知书传入tempType=sjtzs",required=true)@RequestParam(value = "tempType", required = true) String tempType) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblTemplateDuService.getList(token,tempType);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
	}
    
    /**
     * 	#  百度编辑器 获取魔板
     */
    @GetMapping("/get")
    @Operation(summary="百度编辑器 获取详情")
	public JsonBean get(HttpServletRequest request,@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
			@Parameter(name="tempId",description="模板Id",required=true)@RequestParam(value = "tempId", required = true) BigDecimal tempId) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblTemplateDuService.get(token,tempId);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
	}
}
