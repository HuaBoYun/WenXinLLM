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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaIpManageOracle;
import com.huabo.central.enterprises.audit.service.CeaIpManageService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaIpManageQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-IP管理",description="综合管理-IP管理")
@RequestMapping(value = "/api-auth/ip/manage")
@Slf4j
public class CeaIpManageController {

	@Resource
	private CeaIpManageService ceaIpManageService;

	@Operation(summary = "IP地址管理 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManageList(@RequestBody TblCeaIpManageQueryParam param) {
		MyJsonBean<TblCeaIpManageOracle> myJsonBean = null;
		try {
			myJsonBean = ceaIpManageService.getTblCeaIpManageList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP地址管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP地址管理 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaIpManageOracle> saveOrUpdateTblCeaIpManage(@RequestBody @Validated TblCeaIpManageOracle param) {
		MyJsonBean<TblCeaIpManageOracle> myJsonBean = null;
		try {
			myJsonBean = ceaIpManageService.saveOrUpdateTblCeaIpManage(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP地址管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP地址管理 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaIpManage(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaIpManageService.deleteTblCeaIpManage(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP地址管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "IP地址管理 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaIpManageOracle> getTblCeaIpManage(@PathVariable Long id) {
		MyJsonBean<TblCeaIpManageOracle> myJsonBean = null;
		try {
			myJsonBean = ceaIpManageService.getTblCeaIpManage(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("IP地址管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}


}
