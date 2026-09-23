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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaInsideReportedOracle;
import com.huabo.central.enterprises.audit.service.CeaInsideReportedService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.TblCeaInsideReportedQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-内部文件呈报",description="综合管理-内部文件呈报")
@RequestMapping(value = "/api-auth/inside/reported")
@Slf4j
public class CeaInsideReportedController {

	@Resource
	private CeaInsideReportedService ceaInsideReportedService;

	@Operation(summary = "内部文件呈报 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaInsideReportedOracle> getTblCeaInsideReportedList(@RequestBody TblCeaInsideReportedQueryParam param) {
		MyJsonBean<TblCeaInsideReportedOracle> myJsonBean = null;
		try {
			myJsonBean = ceaInsideReportedService.getTblCeaInsideReportedList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部文件呈报 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部文件呈报 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaInsideReportedOracle> saveOrUpdateTblCeaInsideReported(@RequestBody @Validated TblCeaInsideReportedOracle param) {
		MyJsonBean<TblCeaInsideReportedOracle> myJsonBean = null;
		try {
			myJsonBean = ceaInsideReportedService.saveOrUpdateTblCeaInsideReported(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部文件呈报 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部文件呈报 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaInsideReported(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaInsideReportedService.deleteTblCeaInsideReported(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部文件呈报 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "内部文件呈报 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaInsideReportedOracle>> getTblCeaInsideReported(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaInsideReportedOracle>> myJsonBean = null;
		try {
			myJsonBean = ceaInsideReportedService.getTblCeaInsideReported(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("内部文件呈报 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
}
