package com.huabo.know.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.page.BasePageParam;
import com.huabo.know.service.TblzsgxReviewService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value = "/htwk/review")
@Tag(name="合同风险清单",description="合同风险清单")
public class HtwkReviewController {
	
	 @Resource
	 public TblzsgxReviewService tblzsgxReviewService;
	 
	 @Resource
	 private UserProvider userProvider;


	@PostMapping(value = "/getReviewCheckList")
	@Operation(summary = "风险清单检查列表")
	public JsonBean getReviewCheckList(HttpServletRequest request,
									   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									   @Parameter(name = "pageParam", description = "分页参数", required = true) @RequestBody BasePageParam pageParam) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblzsgxReviewService.getReviewCheckList(pageParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	 @RequestMapping(value = "/getReviewItemTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary = "风险清单配置项树形结构")
	 public JsonBean getReviewItemTree(HttpServletRequest request,
	      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		@Parameter(name = "reviewId", description = "风险清单id", required = true) @RequestParam("reviewId") String reviewId
	 ) throws Exception {
		 
	     JsonBean jsonBean = null;
	     try {
	         jsonBean = tblzsgxReviewService.getReviewItemTree(reviewId);
	     } catch (Exception e) {
	         e.printStackTrace();
	         return ResponseFormat.retParam(1, 1000, e.getMessage());
	     }
	     return jsonBean;
	 }

	@RequestMapping(value = "/getReviewItemInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	@Operation(summary = "风险清单配置项详情")
	public JsonBean getReviewItemInfo(HttpServletRequest request,
									  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									  @Parameter(name = "reviewItemId", description = "风险清单配置项id（联调传5）", required = true) @RequestParam("reviewItemId") String reviewItemId
	) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblzsgxReviewService.getReviewItemInfo(reviewItemId);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

}
