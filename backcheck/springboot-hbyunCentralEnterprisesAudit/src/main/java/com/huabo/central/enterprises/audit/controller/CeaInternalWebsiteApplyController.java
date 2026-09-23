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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInternalWebsiteApplyOracle;
import com.huabo.central.enterprises.audit.service.CeaInternalWebsiteApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInternalWebsiteApplyQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-内部网站申信息发布",description="综合管理-内部网站申信息发布")
@RequestMapping(value = "/api-auth/internal/website")
@Slf4j
public class CeaInternalWebsiteApplyController {

	@Resource
	private CeaInternalWebsiteApplyService ceaInternalWebsiteApplyService;

	@Operation(summary = "内部网站申信息发布 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApplyList(@RequestBody TblCeaInternalWebsiteApplyQueryParam param) {
		MyJsonBean<TblCeaInternalWebsiteApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaInternalWebsiteApplyService.getTblCeaInternalWebsiteApplyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部网站申信息发布 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部网站申信息发布 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> saveOrUpdateTblCeaInternalWebsiteApply(@RequestBody @Validated TblCeaInternalWebsiteApplyOracle param) {
		MyJsonBean<TblCeaInternalWebsiteApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaInternalWebsiteApplyService.saveOrUpdateTblCeaInternalWebsiteApply(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部网站申信息发布 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部网站申信息发布 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaInternalWebsiteApply(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaInternalWebsiteApplyService.deleteTblCeaInternalWebsiteApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部网站申信息发布 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部网站申信息发布 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaInternalWebsiteApplyOracle> getTblCeaInternalWebsiteApply(@PathVariable Long id) {
		MyJsonBean<TblCeaInternalWebsiteApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaInternalWebsiteApplyService.getTblCeaInternalWebsiteApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部网站申信息发布 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
