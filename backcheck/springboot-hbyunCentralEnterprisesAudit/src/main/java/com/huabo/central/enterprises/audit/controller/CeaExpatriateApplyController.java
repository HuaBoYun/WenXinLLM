package com.huabo.central.enterprises.audit.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.central.enterprises.audit.exception.ServiceException;
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaExpatriateApplyOracle;
import com.huabo.central.enterprises.audit.service.CeaExpatriateApplyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaExpatriateApplyQueryParam;
import com.huabo.central.enterprises.audit.vo.param.UserAllQueryParam;
import com.huabo.central.enterprises.audit.vo.result.TblCeaExpatriateApplyExpress;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-外派任务",description="综合管理-外派任务")
@RequestMapping(value = "/api-auth/expatriate/apply")
@Slf4j
public class CeaExpatriateApplyController {

	@Resource
	private CeaExpatriateApplyService ceaExpatriateApplyService;

	@Resource
	private UserProvider userProvider;
	
	@Operation(summary = "外派任务 列表查询")
	@PostMapping("/info/getList")
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyList(@RequestBody TblCeaExpatriateApplyQueryParam param) {
		MyJsonBean<TblCeaExpatriateApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaExpatriateApplyService.getTblCeaExpatriateApplyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外派任务 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外派任务 新增/更新")
	@PostMapping("/info/saveOrUpdate")
	public MyJsonBean<TblCeaExpatriateApplyOracle> saveOrUpdateTblCeaExpatriateApply(@RequestBody @Validated TblCeaExpatriateApplyOracle param) {
		MyJsonBean<TblCeaExpatriateApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaExpatriateApplyService.saveOrUpdateTblCeaExpatriateApply(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外派任务 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外派任务 刪除")
	@DeleteMapping("/info/{id}")
	public MyJsonBean<Void> deleteTblCeaExpatriateApply(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaExpatriateApplyService.deleteTblCeaExpatriateApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外派任务 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外派任务 详情 查询")
	@GetMapping("/info/{id}")
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApply(@PathVariable Long id) {
		MyJsonBean<TblCeaExpatriateApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaExpatriateApplyService.getTblCeaExpatriateApply(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外派任务 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "外派人员台账 列表查询")
	@PostMapping("/info/get-all-List")
	public MyJsonBean<TblCeaExpatriateApplyOracle> getTblCeaExpatriateApplyAllList(@RequestBody UserAllQueryParam param) {
		MyJsonBean<TblCeaExpatriateApplyOracle> myJsonBean = null;
		try {
			myJsonBean = ceaExpatriateApplyService.getTblCeaExpatriateApplyAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("外派人员台账 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出 外派人员台账")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token,@RequestBody UserAllQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaExpatriateApplyService.getTblCeaExpatriateApplyAllList(param);
		PageResult<TblCeaExpatriateApplyOracle> result = (PageResult<TblCeaExpatriateApplyOracle>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean list = ceaExpatriateApplyService.getTblCeaExpatriateApplyAllList(param);
			PageResult<TblCeaExpatriateApplyOracle> tempList = (PageResult<TblCeaExpatriateApplyOracle>) list.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "外派人员台账列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaExpatriateApplyExpress.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				result.getTlist().forEach(item -> {
					if (Objects.nonNull(item.getApplyExpatriateTime())) {
						String format = new SimpleDateFormat("yyyy-MM-dd").format(item.getApplyExpatriateTime());
						item.setApplyExpatriateTime1(format);
					}
					if (Objects.nonNull(item.getApplyReturnTime())) {
						String format = new SimpleDateFormat("yyyy-MM-dd").format(item.getApplyReturnTime());
						item.setApplyReturnTime1(format);
					}
				});
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
