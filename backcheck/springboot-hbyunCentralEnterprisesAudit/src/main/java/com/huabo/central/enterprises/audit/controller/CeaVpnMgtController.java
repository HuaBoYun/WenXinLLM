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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaVpnMgtOracle;
import com.huabo.central.enterprises.audit.service.CeaVpnMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaVpnMgtQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-VPN账号管理",description="综合管理-VPN账号管理")
@RequestMapping(value = "/api-auth/vpn/mgt")
@Slf4j
public class CeaVpnMgtController {

	@Resource
	private CeaVpnMgtService ceaVpnMgtService;

	@Operation(summary = "VPN账号管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgtList(@RequestBody TblCeaVpnMgtQueryParam param) {
		MyJsonBean<TblCeaVpnMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaVpnMgtService.getTblCeaVpnMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("VPN账号管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "VPN账号管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaVpnMgtOracle> saveOrUpdateTblCeaVpnMgt(@RequestBody @Validated TblCeaVpnMgtOracle param) {
		MyJsonBean<TblCeaVpnMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaVpnMgtService.saveOrUpdateTblCeaVpnMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("VPN账号管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "VPN账号管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaVpnMgt(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaVpnMgtService.deleteTblCeaVpnMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("VPN账号管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "VPN账号管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaVpnMgtOracle> getTblCeaVpnMgt(@PathVariable Long id) {
		MyJsonBean<TblCeaVpnMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaVpnMgtService.getTblCeaVpnMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("VPN账号管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

}
