package com.huabo.audit.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.audit.exception.ServiceException;
import com.huabo.audit.oracle.entity.TblYqnsPaper;
import com.huabo.audit.oracle.entity.TblYqnsPaperPx;
import com.huabo.audit.oracle.entity.TblYqnsPaperTb;
import com.huabo.audit.oracle.entity.TblYqnsRuletb;
import com.huabo.audit.oracle.mapper.TblYqnsPaperMapper;
import com.huabo.audit.oracle.service.TblYqnsPaperService;
import com.huabo.audit.oracle.service.TblYqnsPaperTbService;
import com.huabo.audit.oracle.service.TblYqnsRuletbService;
import com.huabo.audit.oracle.vo.SjdwjdVo;
import com.huabo.audit.oracle.vo.XmdqVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 论文评优控制器
 * <p>提供论文的列表查询、新增、修改、删除、导出等接口</p>
 *
 * @author hbyun
 */
@RestController
@Tag(name="论文评优-论文-所有接口",description="论文评优-论文-所有接口")
@Slf4j
public class AuditPaperController {

	@Resource
	private TblYqnsPaperService tblYqnsPaperService ; 
	
	@Resource
	private TblYqnsPaperTbService tblYqnsPaperTbService;
	
	@Resource
	private TblYqnsRuletbService tblYqnsRuletbService;
	 
	 
	
	
	@Operation(summary = "论文上报- 列表查询")
	@GetMapping("/lwsb/getList")
	public JsonBean getsheetList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.findAllList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e); 
		}
		return jsonBean;
	}

	
	
	@Operation(summary = "论文上报- 保存或修改")
	@PostMapping("/lwsb/saveOrupdate")
	public JsonBean saveOrupdate(TblYqnsPaper ry,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attids", description = "上传底稿id拼接字符串", required = false) @RequestParam(value = "attids", required = false) String attids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.saveOrupdate(token, ry, attids);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报-保存或修改 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}

	
	@Operation(summary = "论文上报- 查询详情")
	@GetMapping("/lwsb/getone")
	public JsonBean getone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.findByid(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报-查询详情 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "论文上报- 查询附件列表")
	@GetMapping("/lwsb/getattList")
	public JsonBean getattList(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.findattlistByid(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报-查询附件列表 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "论文上报- 删除")
	@PostMapping("/lwsb/deleteone")
	public JsonBean deleteone(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.deleteone(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报- 删除 ...接口 异常", e); 
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "论文上报- 删除附件")
	@PostMapping("/lwsb/deleteatt")
	public JsonBean deleteatt(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "attid", description = "附件id", required = false) @RequestParam(value = "attid", required = false) String attid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.deleteatt(token, attid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报- 删除附件 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	@Operation(summary = "论文上报- 上报")
	@PostMapping("/lwsb/xfry")
	public JsonBean qdproject(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) String perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.lwsb(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文上报- 上报 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}  
	
	@Operation(summary = "论文台账- 退回")
	@PostMapping("/lwsb/thlw")
	public JsonBean thlw(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) String perid) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.thlw(token, perid);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文台账- 退回 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}  
	
	
	
	@Operation(summary = "论文台账- 列表查询")
	@GetMapping("/lwtz/gettzList")
	public JsonBean gettzList(XmdqVo vo,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.findAlltzList(token, pageNumber, pageSize, vo);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文台账- 列表查询 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}
	
	
	@Operation(summary = "论文台账- 导出")
	@GetMapping("/lwtz/exportt")
	public void exportt(XmdqVo vo,HttpServletResponse response,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		try {
			 String date = String.valueOf(System.currentTimeMillis());
	            String fileName = "理论研究台账" + "_" + date + ".xlsx";
	            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
	            ServletOutputStream outputStream = response.getOutputStream();
	            vo.setPageSize(9999);
	            List<TblYqnsPaper> list = tblYqnsPaperService.findAllexportList(token, pageNumber, pageSize, vo);
	            List<Object[]> exportList = new ArrayList<>(list.size());
	            String[] titles = {"序号","论文名称", "填报单位","撰写人","备注" };
	            for (int i = 0; i < list.size(); i++) { 
	            	TblYqnsPaper r = list.get(i);
	                Object[] o = { i+1,r.getPapername(),r.getTbrgname(), r.getZxrname(), r.getRemarks()};
	                exportList.add(o);
	            }

	            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文台账- 导出 ...接口 异常", e);
		}
	}
	

	
	@Operation(summary = "论文台账- 修改排序")
	@PostMapping("/lwtz/xgpx")
	public JsonBean xgpx(
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "perid", description = "主键perid", required = false) @RequestParam(value = "perid", required = false) BigDecimal perid,
			@Parameter(name = "code", description = "排序", required = false) @RequestParam(value = "code", required = false) Integer code) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblYqnsPaperService.lwpxxg(token, perid, code);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("论文台账- 修改排序 ...接口 异常", e);
			return ResponseFormat.retParam(0,"失败",e);
		}
		return jsonBean;
	}  
	
	
	 @GetMapping("/lwpx/gettdList")
	    @Operation(summary = "论文排序-填报列表") 
	    public JsonBean getjdList(HttpServletRequest request,
	                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	                            @Parameter(name = "pageSize", description = "每页记录数", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
	                            SjdwjdVo vo  
	    ){
	        JsonBean jsonBean = null; 
	        try{
	            jsonBean = tblYqnsPaperTbService.findAllList(token, pageNumber, pageSize, vo);
	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	

	 
	    @PostMapping(value ="/lwpx/tbsaveOrUpdate", produces = "application/json; charset=utf-8")
	    @Operation(summary = "论文排序-添加或修改填报信息")
	    public JsonBean jdsaveOrUpdate(HttpServletRequest request,
	                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                 @Parameter(name = "glids", description = "glids", required = false)@RequestParam(value = "glids", required = false, defaultValue = "") String glids,
	                                 TblYqnsPaperTb jd
	    ){
	        JsonBean jsonBean = null;
	        try{
	        	jsonBean=tblYqnsPaperTbService.saveOrupdate(token, jd, glids);

	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	    
	    
	    @GetMapping("/lwpx/tbdetail")
	    @Operation(summary = "论文排序-根据ID获取填报详细信息")
	    public JsonBean jddetail(HttpServletRequest request,
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
	        JsonBean jsonBean = null;
	        try{
	            jsonBean = tblYqnsPaperTbService.findByid(token, tbid);
	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	    
	    
	    
	    @DeleteMapping("/lwpx/tbdelete")
	    @Operation(summary = "论文排序-删除填报信息")
	    public JsonBean jddelete(HttpServletRequest request,
	                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
	    ){
	        JsonBean jsonBean = null;
	        try{
	            jsonBean=tblYqnsPaperTbService.deleteone(token, tbid);
	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	    
	    
	    
	    
	    
	    @Operation(summary = "论文排序- 选择论文上报列表查询")
		@GetMapping("/lwpx/gettzList")
		public JsonBean getxzList(TblYqnsPaper vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.findAllxztbList(token, pageNumber, pageSize, vo);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文台账- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
	    
	    
	    

		@Operation(summary = "论文排序- 子表论文上报列表查询")
		@GetMapping("/lwpx/getList")
		public JsonBean getzbList(XmdqVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.findbytbnr(token, tbid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文排序- 子表论文上报列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		@Resource
		private TblYqnsPaperMapper tblYqnsPaperMapper;
		
		@Operation(summary = "论文排序- 子表论文上报列表导出")
		@GetMapping("/lwpx/exportList")
		public void lwpxexportList(XmdqVo vo,HttpServletResponse response,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid) {
			try {
				 String date = String.valueOf(System.currentTimeMillis());
		            String fileName = "评选论文" + "_" + date + ".xlsx";
		            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		            ServletOutputStream outputStream = response.getOutputStream();
		            vo.setPageSize(9999);
		            List<TblYqnsPaper> list = tblYqnsPaperMapper.selectByListId(tbid);
		            List<Object[]> exportList = new ArrayList<>(list.size());
		            String[] titles = {"序号","论文名称", "填报单位","撰写人","排序","备注" };
		            for (int i = 0; i < list.size(); i++) { 
		            	TblYqnsPaper r = list.get(i);
		                Object[] o = { i+1,r.getPapername(),r.getTbrgname(), r.getZxrname(),r.getTotal(), r.getRemarks()};
		                exportList.add(o);
		            }

		            ImportOrExportExcelUtil.exportExcel(titles, exportList, outputStream, null);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("评选论文- 导出 ...接口 异常", e);
			}
		}
		
		
		

	    @PostMapping(value ="/lwpx/deletezb", produces = "application/json; charset=utf-8")
	    @Operation(summary = "论文排序-删除关联论文列表数据")
	    public JsonBean deletezb(HttpServletRequest request,
	                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                 @Parameter(name = "perid", description = "perid", required = false)@RequestParam(value = "perid", required = false, defaultValue = "") BigDecimal perid
	    ){
	        JsonBean jsonBean = null;
	        try{
	        	jsonBean=tblYqnsPaperService.deletebyglid(token, perid);

	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
		

		@Operation(summary = "论文排序- 查询打分集合")
		@GetMapping("/lwpx/getfsList")
		public JsonBean getfsList(XmdqVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @Parameter(name = "perid", description = "选择的论文上报的perid", required = false)@RequestParam(value = "perid", required = false, defaultValue = "") BigDecimal perid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.findbyfslist(token, perid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文排序- 查询打分集合 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
 
		@PostMapping(value ="/lwpx/fssaveOrUpdate", produces = "application/json; charset=utf-8")
	    @Operation(summary = "论文排序-添加或修改分数信息")
	    public JsonBean fssaveOrUpdate(HttpServletRequest request,
	                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                 TblYqnsPaperPx ry
	    ){
	        JsonBean jsonBean = null;
	        try{
	        	jsonBean=tblYqnsPaperService.fssaveOrupdate(token, ry);

	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
		 
		
		   
		
		@Operation(summary = "论文汇总排名表- 列表查询")
		@GetMapping("/lwpx/getzpxList")
		public JsonBean getzpxList(XmdqVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.findAllzpxList(token, pageNumber, pageSize, vo); 
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文排序- 论文汇总排名表列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		
		@Operation(summary = "论文获奖规则- 填报列表查询")
		@GetMapping("/hjgz/getList")
		public JsonBean gethjgzList(SjdwjdVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
				@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsRuletbService.findAllList(token, pageNumber, pageSize, vo);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文获奖规则- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
		
		 @GetMapping("/hjgz/yzyear")
		    @Operation(summary = "论文获奖规则-根据年验证规则是否存在")
		    public JsonBean hjgzdetail(HttpServletRequest request,
		    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
		    		@Parameter(name = "year", description = "填报的年度", required = false)@RequestParam(value = "year", required = false, defaultValue = "") String year){
		        JsonBean jsonBean = null;
		        try{
		            jsonBean = tblYqnsRuletbService.yzYear(token, year);
		        }catch (Exception e){
		            e.printStackTrace();
		            return ResponseFormat.retParam(0,e.getMessage(),null);
		        }
		        return jsonBean;
		    }
		
		
		@PostMapping(value ="/hjgz/tbsaveOrUpdate", produces = "application/json; charset=utf-8")
	    @Operation(summary = "论文获奖规则-添加或修改填报信息")
	    public JsonBean hjgzsaveOrUpdate(HttpServletRequest request,
	                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                 @Parameter(name = "gljson", description = "子表json", required = false)@RequestParam(value = "gljson", required = false, defaultValue = "") String gljson,
	                                 TblYqnsRuletb jd
	    ){
	        JsonBean jsonBean = null;
	        try{
	        	jsonBean=tblYqnsRuletbService.saveOrupdate(token, jd, gljson);

	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
		
		
		
		@GetMapping("/hjgz/tbdetail")
	    @Operation(summary = "论文获奖规则-根据ID获取填报详细信息")
	    public JsonBean hjgzdetail(HttpServletRequest request,
	    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	    		@Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid){
	        JsonBean jsonBean = null;
	        try{
	            jsonBean = tblYqnsRuletbService.findByid(token, tbid);
	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	    
	    
	    
	    @DeleteMapping("/hjgz/tbdelete")
	    @Operation(summary = "论文获奖规则-删除填报信息")
	    public JsonBean hjgzdelete(HttpServletRequest request,
	                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
	                                @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid
	    ){
	        JsonBean jsonBean = null;
	        try{
	            jsonBean=tblYqnsRuletbService.deleteone(token, tbid);
	        }catch (Exception e){
	            e.printStackTrace();
	            return ResponseFormat.retParam(0,e.getMessage(),null);
	        }
	        return jsonBean;
	    }
	    
	    @Operation(summary = "论文获奖名单- 列表查询")
		@GetMapping("/hjmd/getList")
		public JsonBean gethjmdList(XmdqVo vo,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.selectByListmc(token);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("文获奖名单- 列表查询 ...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
	   
	    
	    
	    @Operation(summary = "论文排序- 提交审批验证是否评分完成")
		@GetMapping("/lwpx/yzpf")
		public JsonBean yzpf(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				 @Parameter(name = "tbid", description = "tbid", required = false)@RequestParam(value = "tbid", required = false, defaultValue = "") BigDecimal tbid) {
			JsonBean jsonBean = null;
			try {
				jsonBean = tblYqnsPaperService.yzpf(token,tbid);
			} catch (ServiceException ex) {
				throw ex;
			} catch (Exception e) {
				log.error("论文排序- 提交审批验证是否评分完成...接口 异常", e);
				return ResponseFormat.retParam(0,"失败",e);
			}
			return jsonBean;
		}
		
	    
		
}
