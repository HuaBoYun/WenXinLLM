package com.huabo.audit.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.service.TblAutonoNumberService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 自动编码控制器
 * <p>提供内部审计业务的自动编号生成接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="自动编码",description="自动编码")
@RequestMapping(value = "/autoCode")
public class NbsjAutoCodeController {

	@Autowired
	public TblAutonoNumberService tblAutonoNumberService;
	
	/**
     * 获取自动编码
     */
	@OperationLog(
			success = "自动编码",
			busType = "智能审计",
			fail = "自动编码",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取自动编码结果"
	)
    @RequestMapping(value = "/code/findAutoNumber", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "获取自动编码")
	public JsonBean findAutoNumber(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name="tblName",description="tblName",required=false)@RequestParam(value = "tblName", required = false) String tblName,
			 @Parameter(name="column",description="column",required=false)@RequestParam(value = "column", required = false) String column,
			 @Parameter(name="orgCol",description="orgCol",required=false)@RequestParam(value = "orgCol", required = false) String orgCol,
			 @Parameter(name="noId",description="noId",required=false)@RequestParam(value = "noId", required = false) BigDecimal noId)throws Exception{
		 JsonBean jsonBean = null;
			try {
				
				jsonBean = this.tblAutonoNumberService.findFlowNextId(tblName, column, orgCol, noId,null, null, null,token);
				
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}

    /**
     * 通用获取设置的编号 通过上级取下级 上下级无关
     */
	@OperationLog(
			success = "自动编码",
			busType = "智能审计",
			fail = "自动编码",
			operationType = OperationType.SELECT,
			subType = "智能审计——根据上下级关系获取根据自动编码结果"
	)
    @RequestMapping(value = "/code/findRootNumberByParentId", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "获取自动编码")
	public JsonBean findRootNumberByParentId(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			String chilNumberCol, String chilTblName, String chilParentCol, String parentIdCol, String parentTblName,
			String parnetOrgCol, BigDecimal noId, String middleTblname, String middleChilCol, String middleParentCol,
			String fltype)throws Exception{
		 JsonBean jsonBean = null;
			try {
				String flowNextId = tblAutonoNumberService.findRootNumberByParentId(chilNumberCol, chilTblName, chilParentCol,
						parentIdCol, parentTblName, parnetOrgCol, token, noId, middleTblname, middleChilCol,
						middleParentCol, fltype);
				
				Map<String,Object> resultMap = new HashMap<String,Object>(0);
				resultMap.put("data", flowNextId);
		    	return ResponseFormat.retParam(1,200,resultMap);
				
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
    
    /**
     * 通用获取设置的编号 传入条件 获取一级编号或没有级别编号的编号，通过组织划分
     */
	@OperationLog(
			success = "自动编码",
			busType = "智能审计",
			fail = "自动编码",
			operationType = OperationType.SELECT,
			subType = "智能审计——根据表明获取对应自动编码结果"
	)
    @RequestMapping(value = "/code/findAutoNumberByChoice", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "获取自动编码")
	public JsonBean findAutoNumberByChoice(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			String tblName,String column, String orgCol, BigDecimal noId, String chChoiceCol, String choiceVal, String bjf)throws Exception{
		 JsonBean jsonBean = null;
			try {
				jsonBean = tblAutonoNumberService.findFlowNextId(tblName, column, orgCol, noId,
						chChoiceCol, choiceVal, bjf,token);
				
			} catch (Exception e) {
				ResponseFormat.retParam(0,1000,e.getMessage());
			}
			return jsonBean;
	}
    
}
