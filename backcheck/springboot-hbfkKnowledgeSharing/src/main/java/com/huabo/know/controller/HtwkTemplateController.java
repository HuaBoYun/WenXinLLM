package com.huabo.know.controller;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.know.entity.TblZsgxTemplate;
import com.huabo.know.service.TblZsgxTemplateService;
import com.huabo.know.utils.HtmlDownloadUtil;
import com.huabo.know.vo.param.TemplateListParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping(value = "/htwk/template")
@Tag(name="合同模板库",description="合同模板库")
public class HtwkTemplateController {
	
	 @Resource
	 public TblZsgxTemplateService tblZsgxTemplateService;
	 
	 @Resource
	 private UserProvider userProvider;

	@GetMapping(value = "/getCategoryTree")
	@Operation(summary = "查询模板类型树形结构")
	public JsonBean getTemplateCategoryTree(HttpServletRequest request,
							@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTemplateService.getTemplateCategoryTree();
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@GetMapping(value = "/preview")
	@Operation(summary = "查看模板详情")
	public JsonBean getTemplateDetail(HttpServletRequest request,
					@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									  @Parameter(name = "templateNumber", description = "模板number", required = true) @RequestParam Integer templateNumber) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTemplateService.getTemplateDetail(templateNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@PostMapping(value = "/list")
	@Operation(summary = "查询模板列表")
	public JsonBean list(HttpServletRequest request,
									  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									  @Parameter(name = "param", description = "列表查询入参", required = true) @RequestBody TemplateListParam param
	) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblZsgxTemplateService.list(param);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(1, 1000, e.getMessage());
		}
		return jsonBean;
	}

	@Operation(summary = "模板下载")
	@GetMapping(value = "/download")
	public JsonBean templateDownload(HttpServletRequest request, HttpServletResponse response,
								 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
								 @Parameter(name = "templateNumber", description = "模板number", required = true) @RequestParam Integer templateNumber) throws Exception {
		
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		TblZsgxTemplate template = tblZsgxTemplateService.getTemplateByNumber(templateNumber);
		if (template == null) {
			return ResponseFormat.retParam(50001, "数据未找到");
		}
		String title = template.getTitle();
		if (StringUtils.isBlank(template.getTitle())) {
			title = "模板"+template.getTemplateNumber();
		}
		HtmlDownloadUtil.h2w(template.getPreview(),title,response);
		return ResponseFormat.retParam(1, 200);
	}

	@Operation(summary = "批量模板下载")
	@GetMapping(value = "/zipDownload")
	public JsonBean templateDownloadZip(HttpServletRequest request, HttpServletResponse response,
									@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
									@Parameter(name = "templateNumbers", description = "模板numbers", required = true) @RequestParam List<Integer> templateNumbers) throws Exception {
		
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		if (templateNumbers.size() > 20) {
			return ResponseFormat.retParam(10003, "最多支持下载20条记录");

		}
		List<TblZsgxTemplate> templateList = tblZsgxTemplateService.getTemplateByNumbers(templateNumbers);
		if (CollectionUtils.isEmpty(templateList)) {
			// 校验是否存在
			return ResponseFormat.retParam(50001, "数据未找到");
		}

		//整理数据格式
		Map<String,String> map = new HashMap<>();
		for (TblZsgxTemplate template : templateList) {
			map.put(template.getTitle()+template.getTemplateNumber(),template.getPreview());
		}

		HtmlDownloadUtil.zipDownload(map,"合同模板",response);
		return ResponseFormat.retParam(1, 200);
	}

}
