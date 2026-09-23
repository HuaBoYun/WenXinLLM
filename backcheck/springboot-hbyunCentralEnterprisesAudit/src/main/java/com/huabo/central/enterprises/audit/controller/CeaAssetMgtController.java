package com.huabo.central.enterprises.audit.controller;

import java.util.ArrayList;
import java.util.Date;
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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetMgtOracle;
import com.huabo.central.enterprises.audit.oracle.entity.TblStaffOracle;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaAssetMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.MyResponseFormat;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.util.excel.ExcelImport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetMgtQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.StaffResult;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-资产管理",description="综合管理-资产管理")
@RequestMapping(value = "/api-auth/asset/mgt")
@Slf4j
public class CeaAssetMgtController {

	@Resource
	private CeaAssetMgtService ceaAssetMgtService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "资产管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaAssetMgtOracle> getTblCeaAssetMgtList(@RequestBody TblCeaAssetMgtQueryParam param) {
		MyJsonBean<TblCeaAssetMgtOracle> myJsonBean = null;
		try {
			
			TblStaffUtil staff = userProvider.get();
	        if (staff == null) {
	            return MyResponseFormat.retParam(0, 20006, null);
	        }
			
			myJsonBean = ceaAssetMgtService.getTblCeaAssetMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "资产管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaAssetMgtOracle> saveOrUpdateTblCeaAssetMgt(@RequestBody @Validated TblCeaAssetMgtOracle param) {
		MyJsonBean<TblCeaAssetMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaAssetMgtService.saveOrUpdateTblCeaAssetMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	//	@OperationLog(success = "资产管理刪除成功", busType = "综合管理", fail = "资产管理【{{#param.assetName}}】刪除失败", operationType = OperationType.SELECT, subType = "资产管理")
	@Operation(summary = "资产管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaAssetMgt(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaAssetMgtService.deleteTblCeaAssetMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "资产管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaAssetMgtOracle>> getTblCeaAssetMgt(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaAssetMgtOracle>> myJsonBean = null;
		try {
			myJsonBean = ceaAssetMgtService.getTblCeaAssetMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出模板")
	@GetMapping("/template/download-express")
	public void templateDownloadExpress(HttpServletResponse response) {
		// 生成excel下载
		String filename = "资产管理下载模板.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaAssetMgtOracle.class)) {
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "导出 资产管理")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaAssetMgtQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setCreator(loginStaff.getStaffid().longValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaAssetMgtService.getTblCeaAssetMgtList(param);
		PageResult<TblCeaAssetMgtOracle> result = (PageResult<TblCeaAssetMgtOracle>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean ceaAssetMgtList = ceaAssetMgtService.getTblCeaAssetMgtList(param);
			PageResult<TblCeaAssetMgtOracle> tempList = (PageResult<TblCeaAssetMgtOracle>) ceaAssetMgtList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "资产管理列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaAssetMgtOracle.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "导入 资产管理")
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
			List<TblCeaAssetMgtOracle> dataList = excelImport.getDataList(TblCeaAssetMgtOracle.class);
			if (CollectionUtil.isEmpty(dataList)) {
				return MyResponseFormat.retParam(200, 200, null);
			}
			dataList.forEach(item -> {
				item.setCreator(loginStaff.getStaffid().longValue());
				item.setWorkUnit(loginStaff.getLinkDetp().getOrgid().longValue());
				item.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
				if (StringUtils.isNotBlank(item.getAssetType())) {
					if (Objects.equals(item.getAssetType(), "0") || Objects.equals(item.getAssetType(), "1") || Objects
							.equals(item.getAssetType(), "2") || Objects.equals(item.getAssetType(), "3") || Objects
							.equals(item.getAssetType(), "4")) {
					} else {
						throw new ServiceException(400, "导入类别字段异常，请填入正确的值，上移资产/审计部上市/审计部未上市/审计中心上市/审计中心未上市，其中一个");
					}
				}
				if (StringUtils.isNotBlank(item.getAddTimeString())) {
					try {
						item.setAddTime(new Date(item.getAddTimeString()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
						throw new ServiceException(400, "增加日期 格式：yyyy/MM/dd 时间填写异常");
					}
				}
				if (StringUtils.isNotBlank(item.getDiscontinuedTimeString())) {
					try {
						item.setDiscontinuedTime(new Date(item.getDiscontinuedTimeString()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
						throw new ServiceException(400, "停产日期 格式：yyyy/MM/dd 时间填写异常");
					}
				}
				if (StringUtils.isNotBlank(item.getFactoryTimeString())) {
					try {
						item.setFactoryTime(new Date(item.getFactoryTimeString()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
						throw new ServiceException(400, "出厂、建筑或完井日期 格式：yyyy/MM/dd 时间填写异常");
					}
				}
				if (StringUtils.isNotBlank(item.getProductionTimeString())) {
					try {
						item.setProductionTime(new Date(item.getProductionTimeString()));
					} catch (Exception e) {
						log.error("导出时间转换异常：", e);
						throw new ServiceException(400, "投产日期 格式：yyyy/MM/dd 时间填写异常");
					}
				}
				if (StringUtils.isNotBlank(item.getCustodianName())) {
					TblStaffOracle userInfoForName = tblStaffOracleService.getUserInfoForName(item.getCustodianName(), null);
					if (Objects.nonNull(userInfoForName)) {
						item.setCustodian(userInfoForName.getStaffId());
					} else {
						throw new ServiceException(400, "资产编码为：" + item.getAssetCode() + "的保管人名称查询用户ID不存在");
					}
				}
				if (StringUtils.isNotBlank(item.getUsePeopleName())) {
					TblStaffOracle userInfoForName = tblStaffOracleService.getUserInfoForName(item.getUsePeopleName(), null);
					if (Objects.nonNull(userInfoForName)) {
						item.setUsePeople(userInfoForName.getStaffId());
					} else {
						throw new ServiceException(400, "资产编码为：" + item.getAssetCode() + "的使用人名称查询用户ID不存在");
					}
				}
				if (StringUtils.isNotBlank(item.getUseDepartmentName())) {
					StaffResult workUnitName = tblStaffOracleService.getUserInfoForWorkUnitName(item.getUseDepartmentName(), null);
					if (Objects.nonNull(workUnitName)) {
						item.setUseDepartment(workUnitName.getWorkUnitId());
					} else {
						throw new ServiceException(400, "资产编码为：" + item.getAssetCode() + "的在用部门名称查询部门ID不存在");
					}
				}
			});
			ceaAssetMgtService.batchSave(dataList);
			return MyResponseFormat.retParam(200, 200, null);
		} catch (ServiceException ex) {
			log.error("导入异常：", ex);
			throw ex;
		} catch (Exception e) {
			log.error("导入异常：", e);
		}
		return MyResponseFormat.retParam(0, 1000, null);
	}

}
