package com.huabo.central.enterprises.audit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpInventory;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaIpInventoryService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.util.excel.ExcelImport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpInventoryQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ExportTblCeaIpInventory;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.StaffResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-IP清单",description="综合管理-IP清单")
@RequestMapping(value = "/api-auth/ip/inventory")
@Slf4j
public class CeaIpInventoryController {

	@Resource
	private CeaIpInventoryService ceaIpInventoryService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "IP清单 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaIpInventory> getTblCeaIpInventoryList(@RequestBody TblCeaIpInventoryQueryParam param) {
		MyJsonBean<TblCeaIpInventory> myJsonBean = null;
		try {
			myJsonBean = ceaIpInventoryService.getTblCeaIpInventoryList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP清单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP清单 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaIpInventory> saveOrUpdateTblCeaIpInventory(@RequestBody @Validated TblCeaIpInventory param) {
		MyJsonBean<TblCeaIpInventory> myJsonBean = null;
		try {
			myJsonBean = ceaIpInventoryService.saveOrUpdateTblCeaIpInventory(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP清单 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP清单 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaIpInventory(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaIpInventoryService.deleteTblCeaIpInventory(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP清单 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP清单 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaIpInventory>> getTblCeaIpInventory(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaIpInventory>> myJsonBean = null;
		try {
			myJsonBean = ceaIpInventoryService.getTblCeaIpInventory(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP清单 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出模板")
	@GetMapping("/template/download-express")
	public void templateDownloadExpress(HttpServletResponse response) {
		// 生成excel下载
		String filename = System.currentTimeMillis() + "IP清单下载模板.xlsx";
		try (ExcelExport export = new ExcelExport(ExportTblCeaIpInventory.class)) {
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "导出 IP清单")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaIpInventoryQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setCreator(loginStaff.getStaffid().longValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaIpInventoryService.getTblCeaIpInventoryList(param);
		PageResult<TblCeaIpInventory> result = (PageResult<TblCeaIpInventory>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean ceaAssetMgtList = ceaIpInventoryService.getTblCeaIpInventoryList(param);
			PageResult<TblCeaIpInventory> tempList = (PageResult<TblCeaIpInventory>) ceaAssetMgtList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "IP清单列表.xlsx";
		try (ExcelExport export = new ExcelExport(ExportTblCeaIpInventory.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "导入 IP清单")
	@PostMapping("/download-express-import")
	@Transactional(rollbackFor = Exception.class)
	public MyJsonBean<Void> downloadExpressImport(@RequestHeader("token") String token, @RequestPart("file") MultipartFile file,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		try {
			ExcelImport excelImport = new ExcelImport(file, 1, 0);
			List<ExportTblCeaIpInventory> dataList = excelImport.getDataList(ExportTblCeaIpInventory.class);
			if (CollectionUtil.isEmpty(dataList)) {
				return MyResponseFormat.retParam(200, 200, null);
			}
			dataList.forEach(item -> {
				item.setCreator(loginStaff.getStaffid().longValue());
				item.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
				item.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
				Long orgid = null;
				if (StringUtils.isNotBlank(item.getUseBelongGroupName())) {
					StaffResult belongGroupName = tblStaffOracleService
							.getUserInfoForBelongGroupName(item.getUseBelongGroupName(), loginStaff.getCurrentOrg().getOrgid().longValue());
					if (Objects.nonNull(belongGroupName)) {
						item.setUseBelongGroupId(belongGroupName.getBelongGroupId());
						orgid = belongGroupName.getBelongGroupId();
					} else {
						throw new ServiceException(400, "IP清单：" + item.getNum() + "，使用单位名称查询单位ID不存在");
					}
				}
				if (StringUtils.isNotBlank(item.getUsePeopleName())) {
					TblStaffOracle userInfoForName = tblStaffOracleService.getUserInfoForName(item.getUsePeopleName(), null);
					if (Objects.nonNull(userInfoForName)) {
						item.setUsePeopleId(userInfoForName.getStaffId());
					} else {
						throw new ServiceException(400, "IP清单：" + item.getNum() + "，使用人名称查询用户ID不存在");
					}
				}
			});
			ceaIpInventoryService.batchSave(dataList);
		} catch (Exception e) {
			log.error("导入异常：", e);
			throw e;
		}
		return MyResponseFormat.retParam(200, 200, null);
	}
}
