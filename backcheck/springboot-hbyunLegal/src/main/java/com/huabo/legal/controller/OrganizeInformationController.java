package com.huabo.legal.controller;

import java.util.ArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.oracle.entity.TblFwglLegalAdviserOracle;
import com.huabo.legal.oracle.entity.TblFwglLegalOrganizationOracle;
import com.huabo.legal.oracle.entity.TblFwglLegalPersonnelOracle;
import com.huabo.legal.service.OrganizeInformationService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglFirmLawyerQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalAdviserQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalOrganizationQueryParam;
import com.huabo.legal.vo.param.TblFwglLegalPersonnelQueryParam;
import com.huabo.legal.vo.result.TblFwglFirmLawyer;
import com.huabo.legal.vo.result.TblFwglFirmLawyerExt;
import com.huabo.legal.vo.result.TblFwglLegalAdviser;
import com.huabo.legal.vo.result.TblFwglLegalAdviserExt;
import com.huabo.legal.vo.result.TblFwglLegalOrganization;
import com.huabo.legal.vo.result.TblFwglLegalOrganizationExt;
import com.huabo.legal.vo.result.TblFwglLegalPersonnel;
import com.huabo.legal.vo.result.TblFwglLegalPersonnelExt;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-组织信息所有接口",description="法务管理-组织信息所有接口")
@RequestMapping(value = "/api-auth/legal/organize")
@Slf4j
public class OrganizeInformationController {

	@Resource
	private OrganizeInformationService organizeInformationService;
	
	@Resource
	private UserProvider userProvider;

	@Deprecated
	@Operation(summary = "公司律师列表 查询")
	@PostMapping("/firm/lawyer/getList")
	public JsonBean getTblFwglFirmLawyerList(@RequestBody TblFwglFirmLawyerQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglFirmLawyerList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Deprecated
	@Operation(summary = "公司律师 新增/更新")
	@PostMapping("/firm/lawyer/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglFirmLawyer(@RequestBody @Validated TblFwglFirmLawyer param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglFirmLawyer(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Deprecated
	@Operation(summary = "公司律师 刪除")
	@DeleteMapping("/firm/lawyer/{id}")
	public JsonBean deleteTblFwglFirmLawyer(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglFirmLawyer(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Deprecated
	@Operation(summary = "公司律师详情 查询")
	@GetMapping("/firm/lawyer/{id}")
	public JsonBean getTblFwglFirmLawyer(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglFirmLawyer(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("查询公司律师 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Deprecated
	@Operation(summary = "公司律师-简历 新增/更新")
	@PostMapping("/firm/lawyer/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglFirmLawyerExt(@RequestBody @Validated TblFwglFirmLawyerExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglFirmLawyerExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师-简历 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Deprecated
	@Operation(summary = "公司律师-简历 刪除")
	@DeleteMapping("/firm/lawyer/ext/{id}")
	public JsonBean deleteTblFwglFirmLawyerExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglFirmLawyerExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公司律师-简历 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务机构及负责人列表 查询")
	@PostMapping("/legal/organization/getList")
	public JsonBean getTblFwglLegalOrganizationList(@RequestBody TblFwglLegalOrganizationQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalOrganizationList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 法务机构及负责人列表")
	@GetMapping("/legal/organization/download-express")
	public void downloadExpressTblFwglLegalOrganization(@RequestHeader("token") String token, TblFwglLegalOrganizationQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglLegalOrganizationList = organizeInformationService.getTblFwglLegalOrganizationList(param);
		PageResult<TblFwglLegalOrganizationOracle> result = (PageResult<TblFwglLegalOrganizationOracle>) tblFwglLegalOrganizationList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglLegalAdviserTempList = organizeInformationService.getTblFwglLegalOrganizationList(param);
			PageResult<TblFwglLegalOrganizationOracle> tempList = (PageResult<TblFwglLegalOrganizationOracle>) tblFwglLegalAdviserTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "法务机构及负责人列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglLegalOrganization.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "法务机构及负责人 新增/更新")
	@PostMapping("/legal/organization/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalOrganization(@RequestBody @Validated TblFwglLegalOrganization param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalOrganization(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务机构及负责人详情 查询")
	@GetMapping("/legal/organization/{id}")
	public JsonBean getTblFwglLegalOrganization(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalOrganization(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务机构及负责人 刪除")
	@DeleteMapping("/legal/organization/{id}")
	public JsonBean deleteTblFwglLegalOrganization(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalOrganization(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务机构及负责人-年度法律审核情况 新增/更新")
	@PostMapping("/legal/organization/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalOrganizationExt(@RequestBody @Validated TblFwglLegalOrganizationExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalOrganizationExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人-年度法律审核情况 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务机构及负责人-年度法律审核情况 删除")
	@DeleteMapping("/legal/organization/ext/{id}")
	public JsonBean deleteTblFwglLegalOrganizationExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalOrganizationExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务机构及负责人-年度法律审核情况 删除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员列表 查询")
	@PostMapping("/legal/personnel/getList")
	public JsonBean getTblFwglLegalPersonnelList(@RequestBody TblFwglLegalPersonnelQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalPersonnelList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 法务人员列表")
	@GetMapping("/legal/personnel/download-express")
	public void downloadExpressTblFwglLegalPersonnel(@RequestHeader("token") String token, TblFwglLegalPersonnelQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglLegalOrganizationList = organizeInformationService.getTblFwglLegalPersonnelList(param);
		PageResult<TblFwglLegalPersonnelOracle> result = (PageResult<TblFwglLegalPersonnelOracle>) tblFwglLegalOrganizationList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglLegalAdviserTempList = organizeInformationService.getTblFwglLegalPersonnelList(param);
			PageResult<TblFwglLegalPersonnelOracle> tempList = (PageResult<TblFwglLegalPersonnelOracle>) tblFwglLegalAdviserTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "法务人员列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglLegalPersonnel.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "法务人员 新增/更新")
	@PostMapping("/legal/personnel/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalPersonnel(@RequestBody @Validated TblFwglLegalPersonnel param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalPersonnel(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员详情 查询")
	@GetMapping("/legal/personnel/{id}")
	public JsonBean getTblFwglLegalPersonnel(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalPersonnel(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员 删除")
	@DeleteMapping("/legal/personnel/{id}")
	public JsonBean deleteTblFwglLegalPersonnel(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalPersonnel(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员 删除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员-工作经历 新增/更新")
	@PostMapping("/legal/personnel/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalPersonnelExt(@RequestBody @Validated TblFwglLegalPersonnelExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalPersonnelExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员-工作经历 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "法务人员-工作经历 删除")
	@DeleteMapping("/legal/personnel/ext/{id}")
	public JsonBean deleteTblFwglLegalPersonnelExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalPersonnelExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员-工作经历 删除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问列表 查询")
	@PostMapping("/legal/adviser/getList")
	public JsonBean getTblFwglLegalAdviserList(@RequestBody TblFwglLegalAdviserQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalAdviserList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("法务人员列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问 新增/更新")
	@PostMapping("/legal/adviser/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalAdviser(@RequestBody @Validated TblFwglLegalAdviser param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalAdviser(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("总法律顾问 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问详情 查询")
	@GetMapping("/legal/adviser/{id}")
	public JsonBean getTblFwglLegalAdviser(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.getTblFwglLegalAdviser(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("总法律顾问详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问 删除")
	@DeleteMapping("/legal/adviser/{id}")
	public JsonBean deleteTblFwglLegalAdviser(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalAdviser(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("总法律顾问 删除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问-工作经历 新增/更新")
	@PostMapping("/legal/adviser/ext/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLegalAdviserExt(@RequestBody @Validated TblFwglLegalAdviserExt param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.saveOrUpdateTblFwglLegalAdviserExt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("总法律顾问-工作经历 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "总法律顾问-工作经历 删除")
	@PostMapping("/legal/adviser/ext/{id}")
	public JsonBean deleteTblFwglLegalAdviserExt(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = organizeInformationService.deleteTblFwglLegalAdviserExt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("总法律顾问-工作经历 删除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 总法律顾问列表")
	@GetMapping("/legal/adviser/download-express")
	public void downloadExpressTblFwglLegalAdviser(@RequestHeader("token") String token, TblFwglLegalAdviserQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglLegalAdviserList = organizeInformationService.getTblFwglLegalAdviserList(param);
		PageResult<TblFwglLegalAdviserOracle> result = (PageResult<TblFwglLegalAdviserOracle>) tblFwglLegalAdviserList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglLegalAdviserTempList = organizeInformationService.getTblFwglLegalAdviserList(param);
			PageResult<TblFwglLegalAdviserOracle> tempList = (PageResult<TblFwglLegalAdviserOracle>) tblFwglLegalAdviserTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "总法律顾问列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglLegalAdviser.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
