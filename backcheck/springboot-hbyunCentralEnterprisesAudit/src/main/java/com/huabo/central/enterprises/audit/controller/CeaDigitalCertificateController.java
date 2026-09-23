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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaDigitalCertificateOracle;
import com.huabo.central.enterprises.audit.service.CeaDigitalCertificateService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaDigitalCertificateQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-数字证书管理",description="综合管理-数字证书管理")
@RequestMapping(value = "/api-auth/digital/certificate")
@Slf4j
public class CeaDigitalCertificateController {

	@Resource
	private CeaDigitalCertificateService ceaDigitalCertificateService;

	@Operation(summary = "数字证书管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificateList(@RequestBody TblCeaDigitalCertificateQueryParam param) {
		MyJsonBean<TblCeaDigitalCertificateOracle> myJsonBean = null;
		try {
			myJsonBean = ceaDigitalCertificateService.getTblCeaDigitalCertificateList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数字证书管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "数字证书管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaDigitalCertificateOracle> saveOrUpdateTblCeaDigitalCertificate(
			@RequestBody @Validated TblCeaDigitalCertificateOracle param) {
		MyJsonBean<TblCeaDigitalCertificateOracle> myJsonBean = null;
		try {
			myJsonBean = ceaDigitalCertificateService.saveOrUpdateTblCeaDigitalCertificate(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数字证书管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "数字证书管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaDigitalCertificate(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaDigitalCertificateService.deleteTblCeaDigitalCertificate(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数字证书管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "数字证书管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaDigitalCertificateOracle> getTblCeaDigitalCertificate(@PathVariable Long id) {
		MyJsonBean<TblCeaDigitalCertificateOracle> myJsonBean = null;
		try {
			myJsonBean = ceaDigitalCertificateService.getTblCeaDigitalCertificate(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("数字证书管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

}
