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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaRepairExpensesOracle;
import com.huabo.central.enterprises.audit.service.CeaRepairExpensesService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaRepairExpensesQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-修理费支出",description="综合管理-修理费支出")
@RequestMapping(value = "/api-auth/repair/expenses")
@Slf4j
public class CeaRepairExpensesController {

	@Resource
	private CeaRepairExpensesService ceaRepairExpensesService;

	@Operation(summary = "修理费支出 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpensesList(@RequestBody TblCeaRepairExpensesQueryParam param) {
		MyJsonBean<TblCeaRepairExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaRepairExpensesService.getTblCeaRepairExpensesList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("修理费支出 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "修理费支出 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaRepairExpensesOracle> saveOrUpdateTblCeaRepairExpenses(@RequestBody @Validated TblCeaRepairExpensesOracle param) {
		MyJsonBean<TblCeaRepairExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaRepairExpensesService.saveOrUpdateTblCeaRepairExpenses(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("修理费支出 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "修理费支出 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaRepairExpenses(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaRepairExpensesService.deleteTblCeaRepairExpenses(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("修理费支出 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "修理费支出 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaRepairExpensesOracle> getTblCeaRepairExpenses(@PathVariable Long id) {
		MyJsonBean<TblCeaRepairExpensesOracle> myJsonBean = null;
		try {
			myJsonBean = ceaRepairExpensesService.getTblCeaRepairExpenses(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("修理费支出 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}


}
