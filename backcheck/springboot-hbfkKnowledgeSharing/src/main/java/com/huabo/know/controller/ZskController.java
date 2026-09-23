package com.huabo.know.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.know.service.ZshService;
import com.huabo.know.vo.param.JudicialCaseListParam;
import com.huabo.know.vo.param.LawRegulationParam;
import com.huabo.know.vo.param.LegalPracticeListParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value = "/htwk/zsk")
@Tag(name="知识库接口(法律法规、法律案例、法律实务)",description="知识库接口(法律法规、法律案例、法律实务)")
public class ZskController {
	
	 @Resource
	 public ZshService zshService;
	 
	@PostMapping(value = "/getLegalPracticeList")
	@Operation(summary = "知识库-法律实务列表")
	public JsonBean getLegalPracticeList(HttpServletRequest request,
										   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										   @Parameter(name = "legalPracticeListParam", description = "检索入参") @RequestBody LegalPracticeListParam legalPracticeListParam) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getLegalPracticeList(token, legalPracticeListParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/getLegalPracticeInfo")
	@Operation(summary = "知识库-法律实务详情")
	public JsonBean getLegalPracticeInfo(HttpServletRequest request,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "id", description = "法律实务ID", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getLegalPracticeInfo(token, id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/getJudicialCaseList")
	@Operation(summary = "知识库-法律案例列表")
	public JsonBean getJudicialCaseList(HttpServletRequest request,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "judicialCaseListParam", description = "检索入参") @RequestBody JudicialCaseListParam judicialCaseListParam) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getJudicialCaseList(token, judicialCaseListParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/getJudicialCaseInfo")
	@Operation(summary = "知识库-法律案例详情")
	public JsonBean getJudicialCaseInfo(HttpServletRequest request,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "id", description = "法律案例ID", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getJudicialCaseInfo(token, id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/getLawRegulationList")
	@Operation(summary = "知识库-法律法规列表")
	public JsonBean getLawRegulationList(HttpServletRequest request,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										@Parameter(name = "lawRegulationParam", description = "检索入参") @RequestBody LawRegulationParam lawRegulationParam) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getLawRegulationList(token, lawRegulationParam);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/getLawRegulationInfo")
	@Operation(summary = "知识库-法律法规详情")
	public JsonBean getLawRegulationInfo(HttpServletRequest request,
										@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										@Parameter(name = "id", description = "法律法规ID", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.getLawRegulationInfo(token, id);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/sidebar")
	@Operation(summary = "导航栏")
	public JsonBean sidebar(HttpServletRequest request,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							@Parameter(name = "types", description = "类型数组", required = true) @RequestParam List<String> types) throws Exception {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.sidebar(types);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/legalPractice/download")
	@Operation(summary = "法律实务下载")
	public JsonBean legalPracticeDownload(HttpServletRequest request, HttpServletResponse response,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							@Parameter(name = "id", description = "法律实务主键id", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.legalPracticeDownload(id, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/legalPractice/zipDownload")
	@Operation(summary = "法律实务批量下载")
	public JsonBean legalPracticeZipDownload(HttpServletRequest request, HttpServletResponse response,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
							@Parameter(name = "ids", description = "法律实务主键ids", required = true) @RequestParam List<String> ids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.legalPracticeZipDownload(ids, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/judicialCase/download")
	@Operation(summary = "法律案例下载")
	public JsonBean judicialCaseDownload(HttpServletRequest request, HttpServletResponse response,
										  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										  @Parameter(name = "id", description = "法律案例主键id", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.judicialCaseDownload(id, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/judicialCase/zipDownload")
	@Operation(summary = "法律案例批量下载")
	public JsonBean judicialCaseZipDownload(HttpServletRequest request, HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											 @Parameter(name = "ids", description = "法律案例主键ids", required = true) @RequestParam List<String> ids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.judicialCaseZipDownload(ids, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/lawRegulation/download")
	@Operation(summary = "法律法规下载")
	public JsonBean lawRegulationDownload(HttpServletRequest request, HttpServletResponse response,
										 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										 @Parameter(name = "id", description = "法律法规主键id", required = true) @RequestParam String id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.lawRegulationDownload(id, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/lawRegulation/zipDownload")
	@Operation(summary = "法律法规批量下载")
	public JsonBean lawRegulationZipDownload(HttpServletRequest request, HttpServletResponse response,
											@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
											@Parameter(name = "ids", description = "法律法规主键ids", required = true) @RequestParam List<String> ids) {
		JsonBean jsonBean = null;
		try {
			jsonBean = zshService.lawRegulationZipDownload(ids, response);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}
}
