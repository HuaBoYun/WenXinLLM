package com.huabo.compliance.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.compliance.exception.ServiceException;
import com.huabo.compliance.oracle.entity.TblhgglAret;
import com.huabo.compliance.service.TblhgglaretService;
import com.huabo.compliance.util.JsonBean;
import com.huabo.compliance.util.PageResult;
import com.huabo.compliance.util.ResponseFormat;
import com.huabo.compliance.util.TokenUtils;
import com.huabo.compliance.util.excel.ExcelExport;
import com.huabo.compliance.vo.excel.TblhgglAretExcel;
import com.huabo.compliance.vo.param.TblhgglAretQueryParam;
import com.huabo.compliance.vo.param.UserInfoParam;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * @Created with IDEA 2019
 * @package: com.huabo.audit.controller
 * @project_name: SVN4
 * @author:wjh
 * @Date:2023/4/27
 * @Time:19:58
 */
@RestController
@Slf4j
@Tag(name="合规项目",description="合规项目")
@RequestMapping(value = "/hggl/aret")
public class TblhgglAretController {
	@Autowired
	TblhgglaretService tblhgglaretService;
	
	@Resource
	private UserProvider userProvider;

	@PostMapping("/listhz_cx")
	@Operation(summary = "汇总列表查询")
	public JsonBean<PageResult<TblhgglAret>> getListByselect(@RequestHeader("token") String token, @RequestBody TblhgglAretQueryParam param) {
		JsonBean<PageResult<TblhgglAret>> jsonBean = null;
		try {
			TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(1, 20006, null);
			}
			UserInfoParam userInfo = TokenUtils.getUserInfo(loginStaff);
			BeanUtils.copyProperties(userInfo, param);
			jsonBean = tblhgglaretService.getlistByselect(param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@Operation(summary = "导出 汇总列表")
	@GetMapping("/download-express")
	public void downloadExpressTblComplianceIm(@RequestHeader("token") String token, TblhgglAretQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().intValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean jsonBean = tblhgglaretService.getlistByselect(param);
		PageResult<TblhgglAret> result = (PageResult<TblhgglAret>) jsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean complianceImTempList = tblhgglaretService.getlistByselect(param);
			PageResult<TblhgglAret> tempList = (PageResult<TblhgglAret>) complianceImTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "汇总列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblhgglAretExcel.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}


	@GetMapping("/listhz_xq")
	@Operation(summary = "汇总列表查询详情")
	public JsonBean getListByselectID(@Parameter(description="id") Integer id,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		return tblhgglaretService.getlistByselectID(id, token);
	}

	@RequestMapping(value = "/listhz_save_update", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "汇总列表新增AND修改")
	public JsonBean addandupdateListaret(@Parameter(description="tblhgglAret") TblhgglAret tblhgglAret,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
		JsonBean jsonBean = null;
		try {
			jsonBean = tblhgglaretService.addandupdateListaret(tblhgglAret, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

	@GetMapping("/listhz_remove")
	@Operation(summary = "汇总列表查询删除")

	public JsonBean rmoveByList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(description="id") @RequestParam("id") Integer id) throws Exception {
		return tblhgglaretService.removeList(token, id);
	}

	@GetMapping("/summary/{id}")
	@Operation(summary = "汇总列表查询详情 {id}为主键ID")
	public JsonBean getTblhgglAret(@PathVariable Integer id,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
		return tblhgglaretService.getTblhgglAret(id, token);
	}
}
