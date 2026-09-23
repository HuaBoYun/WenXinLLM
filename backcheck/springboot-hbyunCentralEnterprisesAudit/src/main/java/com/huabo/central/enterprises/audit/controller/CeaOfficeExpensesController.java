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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaOfficeExpensesOracle;
import com.huabo.central.enterprises.audit.service.CeaOfficeExpensesService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaOfficeExpensesQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-办公经费申请",description="综合管理-办公经费申请")
@RequestMapping(value = "/api-auth/office/expenses")
@Slf4j
public class CeaOfficeExpensesController {

	@Resource
	private CeaOfficeExpensesService ceaOfficeExpensesService;

	@Operation(summary = "办公经费申请 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpensesList(@RequestBody TblCeaOfficeExpensesQueryParam param) {
		MyJsonBean<TblCeaOfficeExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaOfficeExpensesService.getTblCeaOfficeExpensesList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("办公经费申请 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "办公经费申请 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaOfficeExpensesOracle> saveOrUpdateTblCeaOfficeExpenses(@RequestBody @Validated TblCeaOfficeExpensesOracle param) {
		MyJsonBean<TblCeaOfficeExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaOfficeExpensesService.saveOrUpdateTblCeaOfficeExpenses(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("办公经费申请 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "办公经费申请 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaOfficeExpenses(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaOfficeExpensesService.deleteTblCeaOfficeExpenses(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("办公经费申请 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "办公经费申请 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaOfficeExpensesOracle> getTblCeaOfficeExpenses(@PathVariable Long id) {
		MyJsonBean<TblCeaOfficeExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaOfficeExpensesService.getTblCeaOfficeExpenses(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("办公经费申请 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
