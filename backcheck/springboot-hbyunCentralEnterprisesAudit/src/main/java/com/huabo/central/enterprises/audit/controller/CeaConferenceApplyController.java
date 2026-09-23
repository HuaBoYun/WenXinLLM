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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceApplyOracle;
import com.huabo.central.enterprises.audit.service.CeaConferenceApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceApplyQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-会议申请",description="综合管理-会议申请")
@RequestMapping(value = "/api-auth/conference/apply")
@Slf4j
public class CeaConferenceApplyController {

	@Resource
	private CeaConferenceApplyService ceaConferenceApplyService;

	@Operation(summary = "会议申请 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApplyList(@RequestBody TblCeaConferenceApplyQueryParam param) {
		MyJsonBean<TblCeaConferenceApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceApplyService.getTblCeaConferenceApplyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议申请 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议申请 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaConferenceApplyOracle> saveOrUpdateTblCeaConferenceApply(@RequestBody @Validated TblCeaConferenceApplyOracle param) {
		MyJsonBean<TblCeaConferenceApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceApplyService.saveOrUpdateTblCeaConferenceApply(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议申请 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议申请 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaConferenceApply(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceApplyService.deleteTblCeaConferenceApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议申请 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议申请 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaConferenceApplyOracle> getTblCeaConferenceApply(@PathVariable Long id) {
		MyJsonBean<TblCeaConferenceApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceApplyService.getTblCeaConferenceApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议申请 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}


}
