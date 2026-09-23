package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractProject;
import com.huabo.contract.service.TblAttachmentService;
import com.huabo.contract.service.TblContractProjectService;
import com.huabo.contract.service.TblLegalDisputregistrationService;
import com.huabo.contract.service.TblOrganizaService;
import com.huabo.contract.service.UserService;
import com.huabo.contract.vo.TblContractProjectVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 项目管理
 */
@RestController
@Slf4j
@Tag(name="项目管理",description="项目管理")
@RequestMapping(value = "/contractPro")
public class ContractProjectController {
	
	@Resource
	public TblContractProjectService tblContractProjectService;
	
	@Resource
	private TblAttachmentService tblAttachmentService;
	
	@Resource
	private TblLegalDisputregistrationService tblLegalDisputregistrationService;
	
	@Resource
	public TblOrganizaService tblOrganizaService;
	
	@Resource
	public UserService userService;
	
	private static String xmip="192.0.2.200";
	
	private static String xmusernme="zsco_tzxmk";
	
	private static String xmpassword="REDACTED";
	
	@Resource
	private UserProvider userProvider;
	
	@Value("${file.path}")
	private String fileUrl;
	
	
	

	/**
	 * 同步资产系统项目
	 */
	@GetMapping("/htxm/tbxm")
	@Operation(summary = "同步资产系统项目")
	public JsonBean tbxm(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		
		JsonBean jsonBean = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
		    String url = "jdbc:oracle:thin:@"+xmip+":1521:orcl";
			 Connection connGRC = DriverManager.getConnection(url,  xmusernme, xmpassword);
		      Statement stmtGRC = connGRC.createStatement();
		      stmtGRC.setQueryTimeout(0);
		      StringBuffer sb = new StringBuffer("select * from ADMIN.V_ZSCO_TZGHXMK  ");
		      ResultSet rsOracle = stmtGRC.executeQuery(sb.toString());
		      ResultSetMetaData meta = rsOracle.getMetaData();
			  int columncount = meta.getColumnCount();
			  System.out.println("====="+columncount);
			  List<TblContractProject> list=new ArrayList<>();
				while (rsOracle.next()) {
					TblContractProject pro=new TblContractProject();
					for (int i = 1; i <= columncount; i++) {
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("CODE")) {
							pro.setProjectcode(rsOracle.getObject(i).toString());
						}
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("NAME")) {
							pro.setProjectname(rsOracle.getObject(i).toString());
						}
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("UNIQUE_ID")) {
							pro.setUniuqeid(rsOracle.getObject(i).toString());
						}
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("IPSORG_NAME")) {
							pro.setXmorgname(rsOracle.getObject(i).toString());
						}
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("BFPROJECTSTATE_CODE")) {
							pro.setStatecode(rsOracle.getObject(i).toString());
						}
						if(meta.getColumnName(i)!=null &&meta.getColumnName(i).equals("BFPROJECTSTATE_NAME")) {
							pro.setStatename(rsOracle.getObject(i).toString());
						}
					}
					list.add(pro);
				}
		      rsOracle.close();
		      stmtGRC.close();
			  connGRC.close();
			jsonBean = tblContractProjectService.tball(token, list);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"同步失败！",null);
		}
		return jsonBean;
	}
	
	
	/**
	 * 立项管理列表
	 */
	@GetMapping("/htlx/contract_pro_list")
	@Operation(summary = "立项管理列表")
	public JsonBean contract_pro_list(HttpServletRequest request, TblContractProjectVo tblContractProjectVo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblContractProjectService.cpPageList(token, pageNumber, pageSize,tblContractProjectVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 立项管理列表
	 */
	@GetMapping("/htlx/contract_pro_getNo")
	@Operation(summary = "立项新增获取自增编号")
	public JsonBean contract_pro_getNo(HttpServletRequest request,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblContractProjectService.getContractProjectNo(token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
	/**
	 * 立项管理-新增与修改
	 */
	@RequestMapping(value = "/htlx/contract_pro_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "立项管理-新增与修改")
    public JsonBean contract_pro_save(HttpServletRequest request,@Parameter(name = "cp", description = "实体", required = true)TblContractProject cp,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attIds", description = "附件主键拼接英文逗号分割 示例：1,2,3,4", required = false)@RequestParam(value = "attIds", required = false) String attIds)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblContractProjectService.cpAdd(cp,token,attIds);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 立项管理-新增与修改
	 */
	@RequestMapping(value = "/htlx/contract_pro_delFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "立项管理-删除附件")
    public JsonBean contract_pro_delFile(HttpServletRequest request,
			 @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token")String token,
			 @Parameter(name = "attId", description = "删除的附件Id", required = false)@RequestParam(value = "attId", required = false) String attId)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblContractProjectService.removeFile(token,attId);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }
	
	/**
	 * 立项管理-删除
	 */
	@GetMapping("/htlx/contract_pro_del")
	@Operation(summary = "立项管理-删除")
    public JsonBean contract_pro_del(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
        
        try {
			return tblContractProjectService.cpDelete(projectid, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }
	/**
	 * 立项管理-明细
	 */
	@GetMapping("/htlx/contract_pro_detail")
    @Operation(summary = "立项管理-明细")
    public JsonBean contract_pro_detail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "projectid", description = "主键", required = true)@RequestParam(value = "projectid", required = true) BigDecimal projectid) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblContractProjectService.findContractProjectDetail(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }
	
	/**
	 * 立项管理-上传附件接口
	 */
	@RequestMapping(value = "/uploadFileAttInfo", produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "立项管理-上传附件接口(type=5)")
	public String uploadFileAttInfo(HttpServletRequest request, HttpServletResponse response, Model map,MultipartFile file,
			@RequestParam(value="bid",required=false)BigDecimal bid,
			@RequestParam(value="type",required=false)Integer type,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,@Parameter(name = "staffId", description = "登录用户主键", required = false)String staffId) throws Exception {
		String result = null;
		BigDecimal aid = null;
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
		
		// 创建一个通用的多部分解析器
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
		// 判断 request 是否有文件上传,即多部分请求
		try {
			if (multipartResolver.isMultipart(request)) {
				// 转换成多部分request
				 MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
				 Map<String,Object> resultMap =  this.tblAttachmentService.uploadAttachment(multiRequest,token,staffId,file);
				 JSONObject jsonObj = new JSONObject(resultMap);
				 result = jsonObj.toString();
				 if(bid != null && type != null){
					 TblAttachment tblAttachment = (TblAttachment)resultMap.get("data");
					 aid = tblAttachment.getAttid();
					 tblLegalDisputregistrationService.saveBidType(type,bid,aid);
				 }

            }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping(value = "/deleteFileRelation",produces = "application/json; charset=utf-8",method = {RequestMethod.POST})
	@Operation(summary = "删除附件")
	public @ResponseBody String deleteFileRelation(HttpServletRequest request,
						@RequestParam(value = "attid",required = false)String attid) throws Exception{
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return JsonBean.error("用户已失效");
        }
			tblLegalDisputregistrationService.deleteRelation(attid);
			//TblAttachment att = tblAttachmentService.findById(attid);
			tblAttachmentService.delete(attid);
			return JsonBean.success();
	}
	
	/**
	 * 文件下载
	 */
	@Operation(summary="文件下载")
	@RequestMapping(value = "/download", produces = "application/json; charset=utf-8",method = {RequestMethod.GET})
	public void fileDownLoad(@RequestParam("id") String id, HttpServletResponse httpServletResponse) throws Exception {
		TblStaffUtil staff = userProvider.get();
		TblAttachment tblAttachmentEntity = tblAttachmentService.findById(id);
		FtpUtil.downUploadFile(tblAttachmentEntity,httpServletResponse);
		//return JsonBean.success();
	}
	
	/**
     * 选择人员列表
     */
    @GetMapping("/user/user_list")
	@Operation(summary = "选择人员列表")
	public JsonBean user_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
			@Parameter(name = "orgid", description = "部门id", required = false) @RequestParam(value = "orgid", required = false) BigDecimal orgid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = userService.findAllPageBeanPid(token, pageNumber, pageSize,orgid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 选择部门列表
     */
    @GetMapping("/user/org_list")
	@Operation(summary = "选择部门列表")
	public JsonBean org_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblOrganizaService.getOrgTreeListByAuditObj(token, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 项目立项-附件列表
     */
    @GetMapping("/htlx/file_list")
	@Operation(summary = "项目立项-附件列表")
	public JsonBean user_list(HttpServletRequest request, 
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "projectid", description = "项目id(projectid)", required = true) @RequestParam(value = "projectid", required = true) BigDecimal projectid) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblAttachmentService.htlxFileList(token,projectid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
    
    /**
     * 
     */
	
}
