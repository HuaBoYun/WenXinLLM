package com.financial.sharing.controller;

import com.financial.sharing.exception.ServiceException;
import com.financial.sharing.oracle.entity.TblCeaConferenceMgtOracle;
import com.financial.sharing.service.CeaConferenceMgtService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.TblCeaConferenceMgtQueryParam;
import com.financial.sharing.vo.result.FileVo;
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

	@Resource
	private CeaConferenceMgtService ceaConferenceMgtService;

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
