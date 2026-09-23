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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaAssetAdjustmentOracle;
import com.huabo.central.enterprises.audit.service.CeaAssetAdjustmentService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaAssetAdjustmentQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-资产调剂申请",description="综合管理-资产调剂申请")
@RequestMapping(value = "/api-auth/asset/adjustment")
@Slf4j
public class CeaAssetAdjustmentController {

	@Resource
	private CeaAssetAdjustmentService ceaAssetAdjustmentService;

	@Operation(summary = "资产调剂申请 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaAssetAdjustmentOracle> getTblCeaAssetAdjustmentList(@RequestBody TblCeaAssetAdjustmentQueryParam param) {
		MyJsonBean<TblCeaAssetAdjustmentOracle> myJsonBean = null;
		try {
			myJsonBean = ceaAssetAdjustmentService.getTblCeaAssetAdjustmentList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产调剂申请 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "资产调剂申请 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaAssetAdjustmentOracle> saveOrUpdateTblCeaAssetAdjustment(@RequestBody @Validated TblCeaAssetAdjustmentOracle param) {
		MyJsonBean<TblCeaAssetAdjustmentOracle> myJsonBean = null;
		try {
			myJsonBean = ceaAssetAdjustmentService.saveOrUpdateTblCeaAssetAdjustment(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产调剂申请 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "资产调剂申请 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaAssetAdjustment(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaAssetAdjustmentService.deleteTblCeaAssetAdjustment(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产调剂申请 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "资产调剂申请 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaAssetAdjustmentOracle>> getTblCeaAssetAdjustment(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaAssetAdjustmentOracle>> myJsonBean = null;
		try {
			myJsonBean = ceaAssetAdjustmentService.getTblCeaAssetAdjustment(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("资产调剂申请 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}


}
