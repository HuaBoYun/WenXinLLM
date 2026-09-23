package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.oracle.entity.TblNbkzRiskEntity;
import com.huabo.audit.oracle.entity.TblNbsjBugEntity;
import com.huabo.audit.oracle.entity.TblNbsjRisktolerability;
import com.huabo.audit.oracle.vo.TblNbkzRiskVo;
import com.huabo.audit.oracle.vo.TblNbsjBugVo;
import com.huabo.audit.service.TblNbkzRiskService;
import com.huabo.audit.service.TblNbsjBugService;
import com.huabo.audit.util.R;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 相关问题汇总
 */
@RestController
@Slf4j
@Tag(name="相关问题汇总",description="相关问题汇总")
@RequestMapping(value = "/auditPS")
public class NbsjProblemSumController {
	
	@Resource
	public TblNbsjBugService tblNbsjBugService;
	
	@Resource
	public TblNbkzRiskService nbkzRiskService;


	/**
	 * 缺陷管理列表
	 */
	@GetMapping("/sjbb/defect_list")
	@Operation(summary = "缺陷管理列表")
	public JsonBean workReportList(HttpServletRequest request, TblNbsjBugVo tblNbsjBugVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.bugPageList(token, pageNumber, pageSize,tblNbsjBugVo,orgid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 缺陷管理-新增与修改
	 */
	@RequestMapping(value = "/sjbb/defect_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "缺陷管理-新增与修改")
    public JsonBean defect_add(HttpServletRequest request,@Parameter(name = "bug", description = "实体", required = true)TblNbsjBugEntity bug,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.bugAdd(bug,token,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 我的底稿 缺陷新建
	 */
	@RequestMapping(value = "/sjss/dg_defect_Add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "我的底稿-缺陷新增与修改")
    public JsonBean dg_defect_Add(HttpServletRequest request,@Parameter(name = "bug", description = "实体", required = true)TblNbsjBugEntity bug,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "attids", description = "附件id数组", required = false)String attids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.dgbugAdd(bug,token,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 缺陷管理-新增与修改-内规选择列表
	 */
	@GetMapping("/inner_common_qxwt")
	@Operation(summary = "缺陷管理-新增与修改-内规选择列表")
	public JsonBean inner_common_qxwt(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.innerCommonQxwtList(token, pageNumber, pageSize,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 缺陷管理-新增与修改-外规选择列表
	 */
	@GetMapping("/outer_common_qxwt")
	@Operation(summary = "缺陷管理-新增与修改-外规选择列表")
	public JsonBean outer_common_qxwt(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.outerCommonQxwtList(token, pageNumber, pageSize,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 缺陷管理-新增与修改-选择内规保存
	 */
	@RequestMapping(value = "/qxwt/add_inner_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "缺陷管理-新增与修改-选择内规保存")
    public JsonBean add_inner_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
								   @Parameter(name = "innrulids", description = "内规ids", required = false)String innrulids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.bugInnrulidsAdd(token,bugid,innrulids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 缺陷管理-新增与修改-选择外规保存
	 */
	@RequestMapping(value = "/qxwt/add_outer_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "缺陷管理-新增与修改-选择外规保存")
    public JsonBean add_outer_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
								   @Parameter(name = "outrulids", description = "外规ids", required = false)String outrulids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.bugOutrulidsAdd(token,bugid,outrulids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 缺陷管理-关联内规查询
	 */
	@GetMapping("/inner_common_link")
	@Operation(summary = "缺陷管理-关联内规查询")
	public JsonBean inner_common_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.innerCommonLinkList(token, 1, 99,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 缺陷管理-关联外规查询
	 */
	@GetMapping("/outer_common_link")
	@Operation(summary = "缺陷管理-关联外规查询")
	public JsonBean outer_common_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.outerCommonLinkList(token, 1, 99,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	@GetMapping("/delete_qx_inner")
	@Operation(summary = "缺陷管理-关联内规-删除")
    public JsonBean delete_qx_inner(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
    		@Parameter(name = "innrulids", description = "内规ids", required = false)String innrulids) {
        
        try {
			return this.tblNbsjBugService.bugInnrulidsDelete(token,bugid,innrulids);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	@GetMapping("/delete_qx_outer")
	@Operation(summary = "缺陷管理-关联外规-删除")
    public JsonBean delete_qx_outer(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
    		@Parameter(name = "outrulids", description = "外规ids", required = false)String outrulids) {
        
        try {
			return this.tblNbsjBugService.bugOutrulidsDelete(token,bugid,outrulids);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	/**
	 * 缺陷管理-关联缺陷-选择保存
	 */
	@RequestMapping(value = "/qxwt/add_defect_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "关联缺陷-选择保存")
    public JsonBean add_defect_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)Integer bugid,
								   @Parameter(name = "bugids", description = "关联bugids", required = false)String bugids)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.defectQxwtAdd(token,bugid,bugids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 缺陷管理-关联缺陷查询
	 */
	@GetMapping("/qxwt/defect_link")
	@Operation(summary = "缺陷管理-关联缺陷查询")
	public JsonBean defect_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) Integer bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.defectLinkList(token, 1, 99,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 缺陷管理-关联缺陷删除
	 */
	@GetMapping("/delete_qx_child")
	@Operation(summary = "缺陷管理-关联缺陷删除")
    public JsonBean delete_qx_child(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid,
    		@Parameter(name = "bugids", description = "关联bugids", required = false)String bugids) {
        
        try {
			return this.tblNbsjBugService.bugFatherDelete(token,bugid,bugids);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	
	
	/**
	 * 缺陷管理-删除
	 */
	@GetMapping("/sjbb/defect_del")
	@Operation(summary = "缺陷管理-删除")
    public JsonBean workReportDelete(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid) {
        
        try {
			return tblNbsjBugService.bugDelete(bugid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 缺陷管理-明细
	 */
	@GetMapping("/sjbb/defect_detail")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean workReportDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) Integer bugid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjBugService.findBugDetail(token,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	/**
	 * 缺陷管理-附件列表
	 */
	@GetMapping("/sjbb/defect_file_list")
	@Operation(summary = "缺陷管理-附件列表")
	public JsonBean defect_file_list(HttpServletRequest request,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											 @Parameter(name = "bugId", description = "业务主键", required = true) @RequestParam(value = "bugId", required = true) Integer bugId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.defectFileList(token,bugId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
     * 缺陷管理-附件删除
     */
    @GetMapping("/sjbb/defect_file_del")
    @Operation(summary = "缺陷管理-附件删除")
    public R defect_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjBugService.removeAttInfoByAttId(token, attId);
    }
	
	/**
	 * 缺陷管理-导出
	 */
	@GetMapping("/sjbb/defect_file_export")
	@Operation(summary = "缺陷管理-导出")
	public JsonBean defect_file_export(HttpServletRequest request, HttpServletResponse response,
										   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
										   @Parameter(name = "orgId", description = "组织编号", required = false)@RequestParam(value = "orgId", required = false) Integer orgId) throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblNbsjBugService.defect_file_export(token, orgId,response);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 缺陷管理-新增修改-获取缺陷级别
	 */
	@GetMapping("/sjbb/bug_criterion_list")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean bug_criterion_list(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjBugService.findBugCriterion(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 风险发现列表
	 */
	@GetMapping("/wthz/risk_list")
	@Operation(summary = "风险发现列表")
	public JsonBean risk_list(HttpServletRequest request, TblNbkzRiskVo tblNbkzRiskVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) Integer orgid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.nbkzRiskService.riskPageList(token, pageNumber, pageSize,tblNbkzRiskVo, orgid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
	 * 风险发现-新增与修改
	 */
	@RequestMapping(value = "/wthz/risk_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "风险发现-新增与修改")
    public JsonBean risk_add(HttpServletRequest request,@Parameter(name = "risk", description = "实体", required = true)TblNbkzRiskEntity risk,
							 @Parameter(name = "attids", description = "附件id数组", required = false)String attids,
							 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.nbkzRiskService.riskAdd(risk,token,attids);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 风险发现-删除
	 */
	@GetMapping("/wthz/risk_del")
	@Operation(summary = "风险发现-删除")
    public JsonBean risk_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) Integer riskid) {
        
        try {
			return this.nbkzRiskService.riskDelete(riskid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 风险发现-明细
	 */
	@GetMapping("/wthz/risk_detail")
    @Operation(summary = "风险发现-明细")
    public JsonBean risk_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) Integer riskid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = this.nbkzRiskService.findRiskDetail(token,riskid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

	/**
	 * 风险发现-附件列表
	 */
	@GetMapping("/wthz/risk_file_list")
	@Operation(summary = "风险发现-附件列表")
	public JsonBean risk_file_list(HttpServletRequest request,
									 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									 @Parameter(name = "riskId", description = "业务主键", required = true) @RequestParam(value = "riskId", required = true) Integer riskId) {

		JsonBean jsonBean = null;
		try {
			jsonBean = this.nbkzRiskService.risk_file_list(token,riskId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	/**
     * 风险发现-附件删除
     */
    @GetMapping("/wthz/risk_file_del")
    @Operation(summary = "风险发现-附件删除")
    public R risk_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.nbkzRiskService.removeAttInfoByAttId(token, attId);
    }
	
	/**
	 * 风险发现-风险容忍度保存
	 */
	@RequestMapping(value = "/wthz/fxrrd_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "风险发现-风险容忍度保存")
    public JsonBean fxrrd_save(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name="tnr",description="",required=false)TblNbsjRisktolerability tnr)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.nbkzRiskService.fxrrdAdd(token,tnr);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 风险发现-风险容忍度删除
	 */
	@GetMapping("/wthz/fxrrd_del")
	@Operation(summary = "风险发现-风险容忍度删除")
    public JsonBean fxrrd_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "toleid", description = "主键", required = true)@RequestParam(value = "toleid", required = true) Integer toleid) {
        
        try {
			return nbkzRiskService.fxrrdDelete(toleid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 风险发现-风险容忍度查询
	 */
	@GetMapping("/wthz/fxrrd_list")
	@Operation(summary = "风险发现-风险容忍度查询")
	public JsonBean fxrrd_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "riskid", description = "riskid", required = false) @RequestParam(value = "riskid", required = false) Integer riskid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = nbkzRiskService.fxrrdLinkList(token, 1, 99,riskid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
}
