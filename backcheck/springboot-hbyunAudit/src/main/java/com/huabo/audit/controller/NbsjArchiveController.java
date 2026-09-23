package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbsjBorrowRecordEntity;
import com.huabo.audit.oracle.vo.TblnbsjProjectVo;
import com.huabo.audit.service.TblNbsjBorrowRecordService;
import com.huabo.audit.service.TblNbsjProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计档案管理
 */
@RestController
@Slf4j
@Tag(name="审计档案管理",description="审计档案管理")
@RequestMapping(value = "/auditArchive")
public class NbsjArchiveController {
	
	@Resource
	public TblNbsjProjectService tblnbsjProjectService;
	
	@Resource
	public TblNbsjBorrowRecordService tblNbsjBorrowRecordService;
	
	
	
	/**
	 * 档案列表
	 */
	@OperationLog(
			success = "档案列表",
			busType = "智能审计",
			fail = "档案列表",
			operationType = OperationType.SELECT,
			subType = "审计档案——查询档案列表"
	)
	@GetMapping("/sjgd/sjgd_newlist")
	@Operation(summary = "档案列表")
	public JsonBean sjgd_newlist(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,TblnbsjProjectVo tblnbsjProjectVo,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.sjgdNewPageList(token, pageNumber, pageSize, tblnbsjProjectVo);
		} catch (Exception e) {
            return ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
	}
	/**
	 * 档案列表-明细
	 */
	@OperationLog(
			success = "档案详情",
			busType = "智能审计",
			fail = "档案详情",
			operationType = OperationType.SELECT,
			subType = "审计档案——查询档案详情"
	)
	@GetMapping("/sjgd/sjgd_new_detail")
    @Operation(summary = "档案列表-明细")
    public JsonBean sjgd_new_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblnbsjProjectService.findSjgdNewDetail(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 档案借阅
	 */
	@OperationLog(
			success = "档案借阅",
			busType = "智能审计",
			fail = "档案借阅",
			operationType = OperationType.SELECT,
			subType = "审计档案——查询档案借阅列表"
	)
	@GetMapping("/sjgd/dajy_newlist")
	@Operation(summary = "档案借阅列表")
	public JsonBean dajy_newlist(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.dajyNewPageList(token, pageNumber, pageSize,tblnbsjProjectVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 档案借阅-申请借阅
	 */
	@OperationLog(
			success = "档案借阅",
			busType = "智能审计",
			fail = "档案借阅",
			operationType = OperationType.ADD,
			subType = "审计档案——档案借阅-申请借阅"
	)
	@RequestMapping(value = "/jhgl/tjspBorrow", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "档案借阅-申请借阅")
    public JsonBean tjspBorrow(HttpServletRequest request,@Parameter(name = "br", description = "实体", required = true)TblNbsjBorrowRecordEntity br,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "borrowDate", description = "借阅日期 格式年-月-日", required = false)@RequestParam(value = "borrowDate", required = false) String borrowDate,
		   		@Parameter(name = "backDate", description = "归还日期 格式年-月-日", required = false)@RequestParam(value = "backDate", required = false) String backDate)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBorrowRecordService.tjspBorrow(br,token,borrowDate,backDate);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 档案借阅-详情接口
	 */
	@OperationLog(
			success = "借阅详情",
			busType = "智能审计",
			fail = "借阅详情",
			operationType = OperationType.SELECT,
			subType = "审计档案——获取档案借阅详情"
	)
	@RequestMapping(value = "/jhgl/tjspBorrowDetail", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "档案借阅详情接口")
    public JsonBean tjspBorrowDetail(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "borrowId", description = "id", required = false)@RequestParam(value = "borrowId", required = false) BigDecimal borrowId){
		JsonBean jsonBean = null;
		try {
			System.out.println(111);
			jsonBean = this.tblNbsjBorrowRecordService.tjspBorrowDetail(borrowId,token);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 借阅日志列表
	 */
	@OperationLog(
			success = "借阅列表",
			busType = "智能审计",
			fail = "借阅列表",
			operationType = OperationType.SELECT,
			subType = "审计档案——查看借阅日志列表"
	)
	@GetMapping("/sjgd/jyrz_newlist")
	@Operation(summary = "借阅日志列表")
	public JsonBean jyrz_newlist(HttpServletRequest request, TblnbsjProjectVo tblnbsjProjectVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblnbsjProjectService.jyrzNewPageList(token, pageNumber, pageSize,tblnbsjProjectVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 借阅日志-借阅次数列表
	 */
	@OperationLog(
			success = "借阅次数",
			busType = "智能审计",
			fail = "借阅次数",
			operationType = OperationType.SELECT,
			subType = "审计档案——获取借阅日志中借阅次数列表"
	)
	@GetMapping("/sjgd/jyrz_countlist")
	@Operation(summary = "借阅日志-借阅次数列表")
	public JsonBean jyrz_countlist(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectid", description = "projectid", required = false) @RequestParam("projectid") BigDecimal projectid,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBorrowRecordService.jyrzCountPageList(token, pageNumber, pageSize,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	
}
