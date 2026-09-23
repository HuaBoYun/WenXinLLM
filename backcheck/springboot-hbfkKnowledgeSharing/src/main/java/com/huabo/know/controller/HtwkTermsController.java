package com.huabo.know.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.know.config.Authenticated;
import com.huabo.know.service.TblZsgxTermsService;
import com.huabo.know.vo.param.CreateTermsParam;
import com.huabo.know.vo.param.TermsListParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Authenticated
@RestController
@RequestMapping(value = "/htwk/terms")
@Tag(name="合同要素库",description="合同要素库")
@Slf4j
public class HtwkTermsController {
	
	 @Resource
	 public TblZsgxTermsService tblZsgxTermsService;

	@PostMapping(value = "/getTermsList")
	@Operation(summary = "合同要素库列表")
	public JsonBean getTermsList(HttpServletRequest request,
										   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										   @Parameter(name = "termsListParam", description = "检索入参") @RequestBody TermsListParam termsListParam) {
		log.info("合同要素库列表请求，入参：{}", JSON.toJSONString(termsListParam));
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTermsService.getTermsList(token, termsListParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}


	@PostMapping(value = "/getTermsInfo")
	@Operation(summary = "合同要素详情")
	public JsonBean getTermsInfo(HttpServletRequest request,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "id", description = "要素ID", required = true) @RequestParam String id
								 ) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTermsService.getTermsInfo(token, id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/createTerms")
	@Operation(summary = "新建合同要素")
	public JsonBean createTerms(HttpServletRequest request,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "createTermsParam", description = "新建合同要素入参", required = true) @RequestBody CreateTermsParam createTermsParam) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTermsService.createTerms(token, createTermsParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/download")
	@Operation(summary = "合同要素下载")
	public JsonBean download(HttpServletRequest request, HttpServletResponse response,
								@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							 @Parameter(name = "termsId", description = "合同要素id", required = true) @RequestParam String termsId) {
		JsonBean jsonBean = ResponseFormat.retParam(1, 200);
		try {
			jsonBean = tblZsgxTermsService.download(termsId,response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/zipDownload")
	@Operation(summary = "合同要素批量下载zip")
	public JsonBean zipDownload(HttpServletRequest request, HttpServletResponse response,
							 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							 @Parameter(name = "termsIds", description = "合同要素ids", required = true) @RequestParam List<String> termsIds) {

		if(CollectionUtils.isEmpty(termsIds)){
			return ResponseFormat.retParam(10002, "参数为空");
		}
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTermsService.zipDownload(termsIds,response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}


}
