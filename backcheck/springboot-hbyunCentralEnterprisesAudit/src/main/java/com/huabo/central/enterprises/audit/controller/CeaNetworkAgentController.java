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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaNetworkAgentOracle;
import com.huabo.central.enterprises.audit.service.NetworkAgentService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaNetworkAgentQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-外网代理服务",description="综合管理-外网代理服务")
@RequestMapping(value = "/api-auth/network/agent")
@Slf4j
public class CeaNetworkAgentController {

	@Resource
	private NetworkAgentService networkAgentService;

	@Operation(summary = "外网代理服务 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgentList(@RequestBody TblCeaNetworkAgentQueryParam param) {
		MyJsonBean<TblCeaNetworkAgentOracle> myJsonBean = null;
		try {
			myJsonBean = networkAgentService.getTblCeaNetworkAgentList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外网代理服务 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外网代理服务 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaNetworkAgentOracle> saveOrUpdateTblCeaNetworkAgent(@RequestBody @Validated TblCeaNetworkAgentOracle param) {
		MyJsonBean<TblCeaNetworkAgentOracle> myJsonBean = null;
		try {
			myJsonBean = networkAgentService.saveOrUpdateTblCeaNetworkAgent(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外网代理服务 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外网代理服务 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaNetworkAgent(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = networkAgentService.deleteTblCeaNetworkAgent(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外网代理服务 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外网代理服务 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaNetworkAgentOracle> getTblCeaNetworkAgent(@PathVariable Long id) {
		MyJsonBean<TblCeaNetworkAgentOracle> myJsonBean = null;
		try {
			myJsonBean = networkAgentService.getTblCeaNetworkAgent(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外网代理服务 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
	
	
}
