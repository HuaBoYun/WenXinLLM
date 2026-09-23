package com.huabo.central.enterprises.audit.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficialReceptions;
import com.huabo.central.enterprises.audit.service.CeaOfficialReceptionsService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficialReceptionsQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-公务接待",description="综合管理-公务接待")
@RequestMapping(value = "/api-auth/official/receptions")
@Slf4j
public class CeaOfficialReceptionsController {

	@Resource
	private CeaOfficialReceptionsService ceaOfficialReceptionsService;

	@Operation(summary = "公务接待 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaOfficialReceptions> getTblCeaOfficialReceptionsList(@RequestBody TblCeaOfficialReceptionsQueryParam param) {
		MyJsonBean<TblCeaOfficialReceptions> myJsonBean = null;
		try {
			myJsonBean = ceaOfficialReceptionsService.getTblCeaOfficialReceptionsList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公务接待 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "公务接待 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaOfficialReceptions> saveOrUpdateTblCeaOfficialReceptions(@RequestBody @Validated TblCeaOfficialReceptions param) {
		MyJsonBean<TblCeaOfficialReceptions> myJsonBean = null;
		try {
			myJsonBean = ceaOfficialReceptionsService.saveOrUpdateTblCeaOfficialReceptions(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公务接待 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "公务接待 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaOfficialReceptions(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaOfficialReceptionsService.deleteTblCeaOfficialReceptions(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公务接待 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "公务接待 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaOfficialReceptions>> getTblCeaOfficialReceptions(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaOfficialReceptions>> myJsonBean = null;
		try {
			myJsonBean = ceaOfficialReceptionsService.getTblCeaOfficialReceptions(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("公务接待 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
