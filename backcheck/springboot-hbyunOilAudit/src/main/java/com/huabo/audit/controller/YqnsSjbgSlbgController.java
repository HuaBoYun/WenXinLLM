package com.huabo.audit.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsProposalbg;
import com.huabo.audit.oracle.entity.TblYqnsSjbgSlbg;
import com.huabo.audit.oracle.service.TblYqnsProposalbgService;
import com.huabo.audit.oracle.vo.XmdqVo;
import com.huabo.audit.service.TblYqnsSjbgSlbgService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author wangxilu
 * @description 审计报告_审理报告
 */
@Tag(name="审计报告_审理报告",description="审计报告_审理报告")
@RestController
@RequestMapping(value = "/slbg")
public class YqnsSjbgSlbgController {

    @Resource
    TblYqnsSjbgSlbgService service;
    
    @Resource
    private TblYqnsProposalbgService  tblYqnsProposalbgService;
    

	@Operation(summary = "审计意见书- 列表查询")
	@GetMapping("/sjyjs/getList")
	public JsonBean getsheetList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "审计意见书- 保存或修改")
	@PostMapping("/sjyjs/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsProposalbg ry,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.saveOrupdate(token, ry, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "审计意见书- 查询详情") 
	@GetMapping("/sjyjs/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.findByid(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计意见书- 查询附件列表")
	@GetMapping("/sjyjs/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.findattlistByid(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "审计意见书- 删除")
	@PostMapping("/sjyjs/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.deleteone(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "审计意见书- 删除附件")
	@PostMapping("/sjyjs/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsProposalbgService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
    
    
    @Operation(summary = "新增修改接口")
    @RequestMapping(value = "/saveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public JsonBean saveOrUpdate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 TblYqnsSjbgSlbg vo
    ) {
        try {
            return this.service.saveOrUpdate(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }


    @Operation(summary = "列表查询接口")
    @GetMapping("/list")
    public JsonBean list(HttpServletRequest request,
                         HttpServletResponse response,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pageNumber", description = "分页当前页数") @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @Parameter(name = "pageSize", description = "每页记录数") @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                         TblYqnsSjbgSlbg vo
    ) {
        try {
            return this.service.list(token, pageNumber, pageSize, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "单个详情接口")
    @GetMapping("/detail")
    public JsonBean detail(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjbgSlbg vo
    ) {
        try {
            return this.service.detail(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

    @Operation(summary = "删除记录接口")
    @GetMapping("/delete")
    public JsonBean delete(HttpServletRequest request,
                           HttpServletResponse response,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           TblYqnsSjbgSlbg vo
    ) {
        try {
            return this.service.delete(token, vo);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getCause().getMessage());
        }
    }

}

