package com.huabo.system.controller;

import java.math.BigDecimal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.*;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.entity.Tblzsgxlibrary;
import com.huabo.system.oracle.service.TblzsgxlibraryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 知识共享控制器
 * <p>提供知识库的查询、新增、修改、删除等知识共享管理接口</p>
 *
 * @author hbyun
 */
@RestController
@RequestMapping(value = "/zsgx")
@Tag(name = "知识共享接口", description = "知识共享接口")
public class ZsgxController  {

	 @Resource
	 public TblzsgxlibraryService tblzsgxlibraryService;


	 @RequestMapping(value = "/getzswk", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="知识文库-列表")
	 public JsonBean getNbsjQuestionTypeList(HttpServletRequest request,Tblzsgxlibrary library,
	      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
          @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
	     JsonBean jsonBean = null;
	     try {
	         jsonBean = tblzsgxlibraryService.getList(token, "3", library, pageNumber, pageSize);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseFormat.retParam(1, 1000, e.getMessage());
	     }
	     return jsonBean;
	 }

	    @RequestMapping(value = "/zswkaveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	    @Operation(summary="知识文库-新增或修改")
	    public JsonBean zswkaveOrUpdate(HttpServletRequest request, Tblzsgxlibrary library,
	                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                     @Parameter(name="attids",description="上传附件的ID",required=false) @RequestParam(value="attids", required = false) String attids) throws Exception {
	        JsonBean jsonBean = null;
	        try {
	        	library.setLrtype("3");
	            jsonBean = tblzsgxlibraryService.saveOrUpdate(token, library, attids);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }

	    @GetMapping("/getZswkDetail")
	    @Operation(summary="知识文库-详情")
	    public JsonBean getZswkDetail(HttpServletRequest request,
	                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                  @Parameter(name = "libraryid", description = "主键libraryid", required = true) @RequestParam(value = "libraryid", required = true) BigDecimal libraryid) {

	        JsonBean jsonBean = null;
	        try {
	            jsonBean = tblzsgxlibraryService.findById(token, libraryid);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }

	    @GetMapping("/getZswkattlist")
	    @Operation(summary="知识文库-根据ID获取附件列表")
	    public JsonBean getZswkattlist(HttpServletRequest request,
	                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	                                  @Parameter(name = "libraryid", description = "主键libraryid", required = true) @RequestParam(value = "libraryid", required = true) BigDecimal libraryid) {

	        JsonBean jsonBean = null;
	        try {
	            jsonBean = tblzsgxlibraryService.findByattId(token, libraryid);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }


	    @RequestMapping(value = "/deleteZswk", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	    @Operation(summary="知识文库-删除")
	    public JsonBean deleteZswk(HttpServletRequest request,
	                               @Parameter(name = "libraryid", description = "主键libraryid", required = true) @RequestParam(value = "libraryid", required = true) BigDecimal libraryid,
	                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
	        JsonBean jsonBean = null;
	        try {
	            jsonBean = tblzsgxlibraryService.delete(token, libraryid);
	        } catch (Exception e) {
	            e.printStackTrace();
	            return ResponseFormat.retParam(1, 1000, e.getMessage());
	        }
	        return jsonBean;
	    }




	    @RequestMapping(value = "/getflwklist", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
		 @Operation(summary="法律文库-列表")
		 public JsonBean getflwklist(HttpServletRequest request,Tblzsgxlibrary library,
		      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
	          @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
		     JsonBean jsonBean = null;
		     try {
		         jsonBean = tblzsgxlibraryService.getList(token, "1", library, pageNumber, pageSize);
		     } catch (Exception e) {
		         e.printStackTrace();
		         return ResponseFormat.retParam(1, 1000, e.getMessage());
		     }
		     return jsonBean;
		 }

		  @RequestMapping(value = "/flwksaveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
		  @Operation(summary="法律文库-新增或修改")
		  public JsonBean flwksaveOrUpdate(HttpServletRequest request, Tblzsgxlibrary library,
		                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		                                   @Parameter(name="attids",description="上传附件的ID",required=false) @RequestParam(value="attids", required = false) String attids) throws Exception {
		      JsonBean jsonBean = null;
		      try {
		      	library.setLrtype("1");
		          jsonBean = tblzsgxlibraryService.saveOrUpdate(token, library, attids);
		      } catch (Exception e) {
		          e.printStackTrace();
		          return ResponseFormat.retParam(1, 1000, e.getMessage());
		      }
		      return jsonBean;
		  }


		  @RequestMapping(value = "/gethtfblist", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
			 @Operation(summary="合同范本模板-列表")
			 public JsonBean gethtfblist(HttpServletRequest request,Tblzsgxlibrary library,
			      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
		        @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
			     JsonBean jsonBean = null;
			     try {
			         jsonBean = tblzsgxlibraryService.getList(token, "2", library, pageNumber, pageSize);
			     } catch (Exception e) {
			         e.printStackTrace();
			         return ResponseFormat.retParam(1, 1000, e.getMessage());
			     }
			     return jsonBean;
			 }

			@RequestMapping(value = "/htfbsaveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary="合同范本模板-新增或修改")
			public JsonBean htfbsaveOrUpdate(HttpServletRequest request, Tblzsgxlibrary library,
			                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			                                 @Parameter(name="attids",description="上传附件的ID",required=false) @RequestParam(value="attids", required = false) String attids) throws Exception {
			    JsonBean jsonBean = null;
			    try {
			    	library.setLrtype("2");
			        jsonBean = tblzsgxlibraryService.saveOrUpdate(token, library, attids);
			    } catch (Exception e) {
			        e.printStackTrace();
			        return ResponseFormat.retParam(1, 1000, e.getMessage());
			    }
			    return jsonBean;
			}

			@RequestMapping(value = "/deleteatt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
			@Operation(summary="知识文库-关联附件删除")
			public JsonBean deleteatt(HttpServletRequest request,
			                           @Parameter(name = "attid", description = "主键attid", required = true) @RequestParam(value = "attid", required = true) BigDecimal attid,
			                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
			    JsonBean jsonBean = null;
			    try {
			        jsonBean = tblzsgxlibraryService.deleteattid(token, attid);
			    } catch (Exception e) {
			        e.printStackTrace();
			        return ResponseFormat.retParam(1, 1000, e.getMessage());
			    }
			    return jsonBean;
			}


			    @RequestMapping(value = "/getBytypelist", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
				@Operation(summary="根据类别汇总数量")
				 public JsonBean getBytypelist(HttpServletRequest request,
				      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				      @Parameter(name = "lrtype", description = "lrtype 类别：1、法律知识 2、合同文本3、知识文库", required = false) @RequestParam(value = "lrtype", required = false) String lrtype,
				      @Parameter(name = "type", description = "type类别：1、效力位阶 2、专题分类 3、制定机关 4、时效性    5、法规类别/文件夹名称  6、公布年份", required = false) @RequestParam(value = "type", required = false) String type) {
				     JsonBean jsonBean = null;
				     try {
				         jsonBean = tblzsgxlibraryService.getBytypelist(token, type,lrtype);
				     } catch (Exception e) {
				         e.printStackTrace();
				         return ResponseFormat.retParam(1, 1000, e.getMessage());
				     }
				     return jsonBean;
				 }

			    @RequestMapping(value = "/addcxcount", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary="根据ID增加查询次数")
				public JsonBean addcxcount(HttpServletRequest request,
				@Parameter(name = "libraryid", description = "主键libraryid", required = true) @RequestParam(value = "libraryid", required = true) BigDecimal libraryid,
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
				    JsonBean jsonBean = null;
				    try {
				        jsonBean = tblzsgxlibraryService.addcxcount(token, libraryid);
				    } catch (Exception e) {
				        e.printStackTrace();
				        return ResponseFormat.retParam(1, 1000, e.getMessage());
				    }
				    return jsonBean;
				}

			    @RequestMapping(value = "/addxzcount", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
				@Operation(summary="根据ID增加下载次数")
				public JsonBean addxzcount(HttpServletRequest request,
						 @Parameter(name = "libraryid", description = "主键libraryid", required = true) @RequestParam(value = "libraryid", required = true) BigDecimal libraryid,
				         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
				    JsonBean jsonBean = null;
				    try {
				        jsonBean = tblzsgxlibraryService.addxzcount(token, libraryid);
				    } catch (Exception e) {
				        return ResponseFormat.retParam(1, 1000, e.getMessage());
				    }
				    return jsonBean;
				}
}
