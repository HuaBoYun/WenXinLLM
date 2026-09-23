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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaGroupOracle;
import com.huabo.central.enterprises.audit.service.CeaGroupService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaGroupQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-群组",description="综合管理-群组")
@RequestMapping(value = "/api-auth/group")
@Slf4j
public class CeaGroupController {

	@Resource
	private CeaGroupService ceaGroupService;

	@Operation(summary = "群组 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaGroupOracle> getTblCeaGroupList(@RequestBody TblCeaGroupQueryParam param) {
		MyJsonBean<TblCeaGroupOracle> myJsonBean = null;
		try {
			myJsonBean = ceaGroupService.getTblCeaGroupList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("群组 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "群组 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaGroupOracle> saveOrUpdateTblCeaGroup(@RequestBody @Validated TblCeaGroupOracle param) {
		MyJsonBean<TblCeaGroupOracle> myJsonBean = null;
		try {
			myJsonBean = ceaGroupService.saveOrUpdateTblCeaGroup(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("群组 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "群组 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaGroup(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaGroupService.deleteTblCeaGroup(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("群组 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "群组 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaGroupOracle> getTblCeaGroup(@PathVariable Long id) {
		MyJsonBean<TblCeaGroupOracle> myJsonBean = null;
		try {
			myJsonBean = ceaGroupService.getTblCeaGroup(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("群组 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
