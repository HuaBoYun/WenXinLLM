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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaLiQing;
import com.huabo.central.enterprises.audit.service.CeaLiQingService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaLiQingQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-员工离庆",description="综合管理-员工离庆")
@RequestMapping(value = "/api-auth/li/qing")
@Slf4j
public class CeaLiQingController {

	@Resource
	private CeaLiQingService ceaLiQingService;

	@Operation(summary = "员工离庆 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaLiQing> getTblCeaLiQingList(@RequestBody TblCeaLiQingQueryParam param) {
		MyJsonBean<TblCeaLiQing> myJsonBean = null;
		try {
			myJsonBean = ceaLiQingService.getTblCeaLiQingList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("员工离庆 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "员工离庆 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaLiQing> saveOrUpdateTblCeaLiQing(@RequestBody @Validated TblCeaLiQing param) {
		MyJsonBean<TblCeaLiQing> myJsonBean = null;
		try {
			myJsonBean = ceaLiQingService.saveOrUpdateTblCeaLiQing(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("员工离庆 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "员工离庆 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaLiQing(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaLiQingService.deleteTblCeaLiQing(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("员工离庆 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "员工离庆 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaLiQing>> getTblCeaLiQing(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaLiQing>> myJsonBean = null;
		try {
			myJsonBean = ceaLiQingService.getTblCeaLiQing(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("员工离庆 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
