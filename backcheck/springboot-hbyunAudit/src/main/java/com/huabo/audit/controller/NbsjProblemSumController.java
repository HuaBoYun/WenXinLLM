package com.huabo.audit.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
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
	@OperationLog(
			success = "缺陷管理列表",
			busType = "智能审计",
			fail = "缺陷管理列表",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取缺陷管理列表"
	)
	@GetMapping("/sjbb/defect_list")
	@Operation(summary = "缺陷管理列表")
	public JsonBean workReportList(HttpServletRequest request, TblNbsjBugVo tblNbsjBugVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {

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
	@OperationLog(
			success = "缺陷新增",
			busType = "智能审计",
			fail = "缺陷新增",
			operationType = OperationType.ADD,
			subType = "智能审计——缺陷管理"
	)
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
	@OperationLog(
			success = "底稿新增缺陷",
			busType = "智能审计",
			fail = "底稿新增缺陷",
			operationType = OperationType.ADD,
			subType = "智能审计——我的底稿-缺陷新增与修改"
	)
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
	@OperationLog(
			success = "缺陷新增内规",
			busType = "智能审计",
			fail = "缺陷新增内规",
			operationType = OperationType.ADD,
			subType = "智能审计——缺陷管理-新增与修改-内规选择列表"
	)
	@GetMapping("/inner_common_qxwt")
	@Operation(summary = "缺陷管理-新增与修改-内规选择列表")
	public JsonBean inner_common_qxwt(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) BigDecimal bugid) {

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
	@OperationLog(
			success = "缺陷外规",
			busType = "智能审计",
			fail = "缺陷外规",
			operationType = OperationType.SELECT,
			subType = "智能审计——缺陷管理-新增与修改-外规选择列表"
	)
	@GetMapping("/outer_common_qxwt")
	@Operation(summary = "缺陷管理-新增与修改-外规选择列表")
	public JsonBean outer_common_qxwt(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) BigDecimal bugid) {

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
	@OperationLog(
			success = "内规保存",
			busType = "智能审计",
			fail = "内规保存",
			operationType = OperationType.ADD,
			subType = "智能审计——缺陷管理-新增与修改-选择内规保存"
	)
	@RequestMapping(value = "/qxwt/add_inner_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "缺陷管理-新增与修改-选择内规保存")
    public JsonBean add_inner_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)BigDecimal bugid,
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
	@OperationLog(
			success = "外规保存",
			busType = "智能审计",
			fail = "外规保存",
			operationType = OperationType.ADD,
			subType = "智能审计——缺陷管理-新增与修改-选择外规保存"
	)
	@RequestMapping(value = "/qxwt/add_outer_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "缺陷管理-新增与修改-选择外规保存")
    public JsonBean add_outer_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)BigDecimal bugid,
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
	@OperationLog(
			success = "内规查询",
			busType = "智能审计",
			fail = "内规查询",
			operationType = OperationType.SELECT,
			subType = "智能审计——缺陷管理-新增与修改-关联内规查询"
	)
	@GetMapping("/inner_common_link")
	@Operation(summary = "缺陷管理-关联内规查询")
	public JsonBean inner_common_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) BigDecimal bugid) {

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
	@OperationLog(
			success = "外规查询",
			busType = "智能审计",
			fail = "外规查询",
			operationType = OperationType.SELECT,
			subType = "智能审计——缺陷管理-关联外规查询"
	)
	@GetMapping("/outer_common_link")
	@Operation(summary = "缺陷管理-关联外规查询")
	public JsonBean outer_common_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) BigDecimal bugid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblNbsjBugService.outerCommonLinkList(token, 1, 99,bugid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@OperationLog(
			success = "内规删除",
			busType = "智能审计",
			fail = "内规删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——缺陷管理-关联内规——内规删除"
	)
	@GetMapping("/delete_qx_inner")
	@Operation(summary = "缺陷管理-关联内规-内规删除")
    public JsonBean delete_qx_inner(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) BigDecimal bugid,
    		@Parameter(name = "innrulids", description = "内规ids", required = false)String innrulids) {
        
        try {
			return this.tblNbsjBugService.bugInnrulidsDelete(token,bugid,innrulids);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

	@OperationLog(
			success = "外规删除",
			busType = "智能审计",
			fail = "外规删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——缺陷管理-关联外规——外规删除"
	)
	@GetMapping("/delete_qx_outer")
	@Operation(summary = "缺陷管理-关联外规-删除")
    public JsonBean delete_qx_outer(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) BigDecimal bugid,
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
	@OperationLog(
			success = "保存",
			busType = "智能审计",
			fail = "保存",
			operationType = OperationType.ADD,
			subType = "智能审计——关联缺陷-选择保存"
	)
	@RequestMapping(value = "/qxwt/add_defect_qxwt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "关联缺陷-选择保存")
    public JsonBean add_defect_qxwt(HttpServletRequest request,
								   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
								   @Parameter(name = "bugid", description = "bugid", required = false)BigDecimal bugid,
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
	@OperationLog(
			success = "关联缺陷查询",
			busType = "智能审计",
			fail = "关联缺陷查询",
			operationType = OperationType.SELECT,
			subType = "智能审计——关联缺陷查询"
	)
	@GetMapping("/qxwt/defect_link")
	@Operation(summary = "缺陷管理-关联缺陷查询")
	public JsonBean defect_link(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "bugid", description = "bugid", required = false) @RequestParam(value = "bugid", required = false) BigDecimal bugid) {

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
	@OperationLog(
			success = "缺陷删除",
			busType = "智能审计",
			fail = "缺陷删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——缺陷管理-关联缺陷删除"
	)
	@GetMapping("/delete_qx_child")
	@Operation(summary = "缺陷管理-关联缺陷删除")
    public JsonBean delete_qx_child(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) BigDecimal bugid,
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
	@OperationLog(
			success = "缺陷删除",
			busType = "智能审计",
			fail = "缺陷删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——缺陷管理-删除"
	)
	@GetMapping("/sjbb/defect_del")
	@Operation(summary = "缺陷管理-删除")
    public JsonBean workReportDelete(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) BigDecimal bugid) {
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
	@OperationLog(
			success = "缺陷明细",
			busType = "智能审计",
			fail = "缺陷明细",
			operationType = OperationType.SELECT,
			subType = "智能审计——缺陷管理-明细"
	)
	@GetMapping("/sjbb/defect_detail")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean workReportDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "bugid", description = "主键", required = true)@RequestParam(value = "bugid", required = true) BigDecimal bugid) {
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
	@OperationLog(
			success = "缺陷附件",
			busType = "智能审计",
			fail = "缺陷附件",
			operationType = OperationType.SELECT,
			subType = "智能审计——缺陷管理-获取缺陷附件列表"
	)
	@GetMapping("/sjbb/defect_file_list")
	@Operation(summary = "缺陷管理-附件列表")
	public JsonBean defect_file_list(HttpServletRequest request,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											 @Parameter(name = "bugId", description = "业务主键", required = true) @RequestParam(value = "bugId", required = true) BigDecimal bugId) {

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
	@OperationLog(
			success = "缺陷附件删除",
			busType = "智能审计",
			fail = "缺陷附件删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——缺陷管理-附件删除"
	)
    @GetMapping("/sjbb/defect_file_del")
    @Operation(summary = "缺陷管理-附件删除")
    public R defect_file_del(HttpServletRequest request, HttpServletResponse response,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
    	return this.tblNbsjBugService.removeAttInfoByAttId(token, attId);
    }

	@OperationLog(
			success = "缺陷导出",
			busType = "智能审计",
			fail = "缺陷导出",
			operationType = OperationType.EXPORT,
			subType = "智能审计——缺陷管理-导出"
	)
	@Operation(summary = "缺陷管理-导出")
	@GetMapping("/sjbb/defect_file_export")
	public void export(TblNbsjBugVo tblNbsjBugVo,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {
		try {
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = "缺陷管理" + "_" + date + ".xlsx";
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            List<TblNbsjBugEntity> list = tblNbsjBugService.exportList(token, tblNbsjBugVo, orgid);
            List<Object[]> exportList = new ArrayList<>(list.size());
            // 定义格式化样式
            
            String[] titles = {"缺陷编号","缺陷名称","缺陷种类","缺陷等级", "发生时间","涉及金额(万元)","缺陷类别",  "是否涉诉", "是否境外", "原因分析", "缺陷描述及依据","发现人" };
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {
            	TblNbsjBugEntity r = list.get(i);
                Object[] o = { r.getBugnumber(), r.getDefectsname(),r.getDefecttype(),r.getBugcrilevel(),
                		r.getDiscovertime()!=null?formatter.format(r.getDiscovertime()):"",
                		r.getAmount(),r.getDefectcategory(),
                		r.getLitigation(), r.getOverseas(),
                		r.getCauseanalysis(),r.getBugdescripte(),r.getDiscoverperson()};
                exportList.add(o);
            }

            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
		} catch (Exception e) {
			log.error("缺陷管理- 导出 ...接口 异常", e);
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 缺陷管理-新增修改-获取缺陷级别
	 */
	@OperationLog(
			success = "缺陷级别",
			busType = "智能审计",
			fail = "缺陷级别",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取缺陷级别"
	)
	@GetMapping("/sjbb/bug_criterion_list")
    @Operation(summary = "缺陷管理-明细")
    public JsonBean bug_criterion_list(HttpServletRequest request,
    		@Parameter(name = "bugtype", description = "缺陷类型", required = false)@RequestParam(value = "bugtype", required = false) String bugtype,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblNbsjBugService.findBugCriterion(token,bugtype);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 风险发现列表
	 */
	@OperationLog(
			success = "风险发现列表",
			busType = "智能审计",
			fail = "风险发现列表",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取风险发现列表"
	)
	@GetMapping("/wthz/risk_list")
	@Operation(summary = "风险发现列表")
	public JsonBean risk_list(HttpServletRequest request, TblNbkzRiskVo tblNbkzRiskVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "组织部门id", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {

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
	@OperationLog(
			success = "风险发现新增",
			busType = "智能审计",
			fail = "风险发现新增",
			operationType = OperationType.ADD,
			subType = "智能审计——风险发现-新增/修改"
	)
	@RequestMapping(value = "/wthz/risk_add", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "风险发现-新增与修改")
    public JsonBean risk_add(HttpServletRequest request,@Parameter(name = "risk", description = "实体", required = true)TblNbkzRiskEntity risk,
							 @Parameter(name = "attids", description = "附件id数组", required = false)String attids,
							 @Parameter(name = "rrds", description = "风险容忍度", required = false)String rrds,
							 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.nbkzRiskService.riskAdd(risk,token,attids,rrds);
		} catch (Exception e) {
			return  ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	/**
	 * 风险发现-删除
	 */
	@OperationLog(
			success = "风险发现删除",
			busType = "智能审计",
			fail = "风险发现删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——风险发现-删除"
	)
	@GetMapping("/wthz/risk_del")
	@Operation(summary = "风险发现-删除")
    public JsonBean risk_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) BigDecimal riskid) {
        
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
	@OperationLog(
			success = "风险发现明细",
			busType = "智能审计",
			fail = "风险发现明细",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取风险发现-明细"
	)
	@GetMapping("/wthz/risk_detail")
    @Operation(summary = "风险发现-明细")
    public JsonBean risk_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "riskid", description = "主键", required = true)@RequestParam(value = "riskid", required = true) BigDecimal riskid) {
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
	@OperationLog(
			success = "风险发现附件",
			busType = "智能审计",
			fail = "风险发现附件",
			operationType = OperationType.SELECT,
			subType = "智能审计——获取风险发现-附件列表"
	)
	@GetMapping("/wthz/risk_file_list")
	@Operation(summary = "风险发现-附件列表")
	public JsonBean risk_file_list(HttpServletRequest request,
									 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									 @Parameter(name = "riskId", description = "业务主键", required = true) @RequestParam(value = "riskId", required = true) BigDecimal riskId) {

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
	@OperationLog(
			success = "风险发现附件删除",
			busType = "智能审计",
			fail = "风险发现附件删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——获取风险发现-附件删除"
	)
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
	@OperationLog(
			success = "风险容忍度",
			busType = "智能审计",
			fail = "风险容忍度",
			operationType = OperationType.ADD,
			subType = "智能审计——风险发现-风险容忍度保存"
	)
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
	@OperationLog(
			success = "风险容忍度删除",
			busType = "智能审计",
			fail = "风险容忍度删除",
			operationType = OperationType.DELETE,
			subType = "智能审计——风险发现-风险容忍度删除"
	)
	@GetMapping("/wthz/fxrrd_del")
	@Operation(summary = "风险发现-风险容忍度删除")
    public JsonBean fxrrd_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "toleid", description = "主键", required = true)@RequestParam(value = "toleid", required = true) BigDecimal toleid) {
        
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
	@OperationLog(
			success = "风险容忍度查询",
			busType = "智能审计",
			fail = "风险容忍度查询",
			operationType = OperationType.SELECT,
			subType = "智能审计——风险发现-风险容忍度查询"
	)
	@GetMapping("/wthz/fxrrd_list")
	@Operation(summary = "风险发现-风险容忍度查询")
	public JsonBean fxrrd_list(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "riskid", description = "riskid", required = false) @RequestParam(value = "riskid", required = false) BigDecimal riskid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = nbkzRiskService.fxrrdLinkList(token, 1, 99,riskid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
//	sjbb/defect_list

	@OperationLog(
			success = "评价缺陷导入",
			busType = "智能审计",
			fail = "评价缺陷导入",
			operationType = OperationType.IMPORT,
			subType = "智能审计——评价缺陷列表导入"
	)
	@PostMapping("/sjbb/import")
	@Operation(summary = "评价缺陷——导入")
	public JsonBean importList(HttpServletRequest request, @Parameter(name = "file", description = "导入的文件", required = true) MultipartFile file,
							   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
	) throws IOException {
		InputStream in = file.getInputStream();
		Workbook workbook = null;
		try {
			workbook = WorkbookFactory.create(in);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}
        System.out.println("到此位置没有问题");
		try {
			for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
				Sheet sheet = workbook.getSheetAt(i);

				tblNbsjBugService.resolveSheet(sheet, token);
//				tblNbsjAuditplanService.resolveSheet(sheet, token);
			}
			return ResponseFormat.retParam(1, 200);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("内控管理——评价缺陷 列表导入失败", e);
			return ResponseFormat.retParam(0, 1000, e.getMessage());
		} finally {
			// 读取完毕则关闭流
			in.close();
			workbook.close();
		}
	}



}
