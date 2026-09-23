package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.TblCeaConferenceMgtOracle;
import com.management.accountant.service.CeaConferenceMgtService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.vo.param.TblCeaConferenceMgtQueryParam;
import com.management.accountant.vo.result.FileVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@Api(tags = {"综合管理-会议管理"})
@RequestMapping(value = "/api-auth/conference/mgt")
@Slf4j
public class CeaConferenceMgtController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


	@Resource
	private CeaConferenceMgtService ceaConferenceMgtService;

	@Operation(summary = "会议管理 列表查询")
	@ApiOperation("会议管理 列表查询")
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
	@ApiOperation("会议管理 新增/更新")
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
	@ApiOperation("会议管理 刪除")
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
	@ApiOperation("会议管理 详情 查询")
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
