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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaMailMgtOracle;
import com.huabo.central.enterprises.audit.service.CeaMailMgtService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaMailMgtQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-中石油邮箱管理",description="综合管理-中石油邮箱管理")
@RequestMapping(value = "/api-auth/mail/mgt")
@Slf4j
public class CeaMailMgtController {

	@Resource
	private CeaMailMgtService ceaMailMgtService;

	@Operation(summary = "中石油邮箱管理 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgtList(@RequestBody TblCeaMailMgtQueryParam param) {
		MyJsonBean<TblCeaMailMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaMailMgtService.getTblCeaMailMgtList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("中石油邮箱管理 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "中石油邮箱管理 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaMailMgtOracle> saveOrUpdateTblCeaMailMgt(@RequestBody @Validated TblCeaMailMgtOracle param) {
		MyJsonBean<TblCeaMailMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaMailMgtService.saveOrUpdateTblCeaMailMgt(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("中石油邮箱管理 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "中石油邮箱管理 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaMailMgt(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaMailMgtService.deleteTblCeaMailMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("中石油邮箱管理 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "中石油邮箱管理 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaMailMgtOracle> getTblCeaMailMgt(@PathVariable Long id) {
		MyJsonBean<TblCeaMailMgtOracle> myJsonBean = null;
		try {
			myJsonBean = ceaMailMgtService.getTblCeaMailMgt(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("中石油邮箱管理 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
