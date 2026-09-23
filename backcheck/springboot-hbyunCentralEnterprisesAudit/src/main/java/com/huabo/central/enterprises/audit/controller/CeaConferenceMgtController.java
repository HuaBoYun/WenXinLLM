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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaConferenceMgtOracle;
import com.huabo.central.enterprises.audit.service.CeaConferenceMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaConferenceMgtQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-会议管理",description="综合管理-会议管理")
@RequestMapping(value = "/api-auth/conference/mgt")
@Slf4j
public class CeaConferenceMgtController {

	@Resource
	private CeaConferenceMgtService ceaConferenceMgtService;

	@Operation(summary = "会议管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaConferenceMgtOracle> getTblCeaConferenceMgtList(@RequestBody TblCeaConferenceMgtQueryParam param) {
		MyJsonBean<TblCeaConferenceMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceMgtService.getTblCeaConferenceMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaConferenceMgtOracle> saveOrUpdateTblCeaConferenceMgt(@RequestBody @Validated TblCeaConferenceMgtOracle param) {
		MyJsonBean<TblCeaConferenceMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceMgtService.saveOrUpdateTblCeaConferenceMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaConferenceMgt(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceMgtService.deleteTblCeaConferenceMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "会议管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaConferenceMgtOracle>> getTblCeaConferenceMgt(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaConferenceMgtOracle>> myJsonBean = null;
		try {
			myJsonBean = ceaConferenceMgtService.getTblCeaConferenceMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("会议管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
