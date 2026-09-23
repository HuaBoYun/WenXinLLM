package com.huabo.central.enterprises.audit.controller;

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
import com.hbfk.util.user.UserProvider;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSealFormOracle;
import com.huabo.central.enterprises.audit.service.CeaSealFormService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;
import com.huabo.central.enterprises.audit.vo.result.ExportTblCeaSealForm;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-印信使用单",description="综合管理-印信使用单")
@RequestMapping(value = "/api-auth/seal/form")
@Slf4j
public class CeaSealFormController {

	@Resource
	private CeaSealFormService ceaSealFormService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "印信使用单 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormList(@RequestBody TblCeaSealFormQueryParam param) {
		MyJsonBean<TblCeaSealFormOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.getTblCeaSealFormList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaSealFormOracle> saveOrUpdateTblCeaSealForm(@RequestBody @Validated TblCeaSealFormOracle param) {
		MyJsonBean<TblCeaSealFormOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.saveOrUpdateTblCeaSealForm(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaSealForm(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.deleteTblCeaSealForm(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealForm(@PathVariable Long id) {
		MyJsonBean<TblCeaSealFormOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.getTblCeaSealForm(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 台账列表查询")
	@PostMapping("/getAllList")
	public MyJsonBean<TblCeaSealFormOracle> getTblCeaSealFormAllList(@RequestBody TblCeaSealFormQueryParam param) {
		MyJsonBean<TblCeaSealFormOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.getTblCeaSealFormAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 台账列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 台账-上移")
	@PostMapping("/getAllList/moveUp/{id}")
	public MyJsonBean<Void> updateTblCeaSealFormMoveUp(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.updateTblCeaSealFormMoveUp(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 台账-上移 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "印信使用单 台账-下移")
	@PostMapping("/getAllList/moveDown/{id}")
	public MyJsonBean<Void> updateTblCeaSealFormMoveDown(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaSealFormService.updateTblCeaSealFormMoveDown(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("印信使用单 台账-下移 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出 印章使用台账")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaSealFormQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(loginStaff.getCurrentOrg().getOrgid().longValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaSealFormService.getTblCeaSealFormAllList(param);
		PageResult<TblCeaSealFormOracle> result = (PageResult<TblCeaSealFormOracle>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean ceaAssetMgtList = ceaSealFormService.getTblCeaSealFormAllList(param);
			PageResult<TblCeaSealFormOracle> tempList = (PageResult<TblCeaSealFormOracle>) ceaAssetMgtList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "印章使用台账列表.xlsx";
		int i = 0;
		for (TblCeaSealFormOracle item : result.getTlist()) {
			item.setSerialNumber(i++);
			item.setSignatory(item.getTransactorName());
		}
		try (ExcelExport export = new ExcelExport(ExportTblCeaSealForm.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
