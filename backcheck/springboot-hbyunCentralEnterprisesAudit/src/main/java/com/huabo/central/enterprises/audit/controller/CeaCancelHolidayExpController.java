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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaCancelHolidayExp;
import com.huabo.central.enterprises.audit.service.CeaCancelHolidayExpService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaCancelHolidayQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-销假单",description="综合管理-销假单")
@RequestMapping(value = "/api-auth/cancel/holiday/exp")
@Slf4j
public class CeaCancelHolidayExpController {

	@Resource
	private CeaCancelHolidayExpService ceaCancelHolidayExpService;

	@Operation(summary = "销假单 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExpList(@RequestBody TblCeaCancelHolidayQueryParam param) {
		MyJsonBean<TblCeaCancelHolidayExp> myJsonBean = null;
		try {
			myJsonBean = ceaCancelHolidayExpService.getTblCeaCancelHolidayExpList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("销假单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "销假单 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaCancelHolidayExp> saveOrUpdateTblCeaCancelHolidayExp(@RequestBody @Validated TblCeaCancelHolidayExp param) {
		MyJsonBean<TblCeaCancelHolidayExp> myJsonBean = null;
		try {
			myJsonBean = ceaCancelHolidayExpService.saveOrUpdateTblCeaCancelHolidayExp(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("销假单 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "销假单 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaCancelHolidayExp(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaCancelHolidayExpService.deleteTblCeaCancelHolidayExp(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("销假单 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "销假单 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaCancelHolidayExp> getTblCeaCancelHolidayExp(@PathVariable Long id) {
		MyJsonBean<TblCeaCancelHolidayExp> myJsonBean = null;
		try {
			myJsonBean = ceaCancelHolidayExpService.getTblCeaCancelHolidayExp(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("销假单 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

}
