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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaSupervisionNoticeOracle;
import com.huabo.central.enterprises.audit.service.CeaSupervisionNotice;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSupervisionNoticeQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.TblCeaSupervisionNoticeExpress;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-督办通知单",description="综合管理-督办通知单")
@RequestMapping(value = "/api-auth/supervision/notice")
@Slf4j
public class CeaSupervisionNoticeController {

	@Resource
	private CeaSupervisionNotice ceaSupervisionNotice;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "督办通知单 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaSupervisionNoticeOracle> getTblCeaSupervisionNoticeList(@RequestBody TblCeaSupervisionNoticeQueryParam param) {
		MyJsonBean<TblCeaSupervisionNoticeOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.getTblCeaSupervisionNoticeList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出 督办通知单")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaSupervisionNoticeQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setCreator(loginStaff.getStaffid().longValue());
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaSupervisionNotice.getTblCeaSupervisionNoticeList(param);
		PageResult<TblCeaSupervisionNoticeOracle> result = (PageResult<TblCeaSupervisionNoticeOracle>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean list = ceaSupervisionNotice.getTblCeaSupervisionNoticeList(param);
			PageResult<TblCeaSupervisionNoticeOracle> tempList = (PageResult<TblCeaSupervisionNoticeOracle>) list.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "督办通知单列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaSupervisionNoticeExpress.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				result.getTlist().forEach(item -> {
					if (Objects.nonNull(item.getNoticeTime())) {
						String format = new SimpleDateFormat("yyyy-MM-dd").format(item.getNoticeTime());
						item.setNoticeTime1(format);
					}
				});
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}

	@Operation(summary = "督办通知单 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaSupervisionNoticeOracle> saveOrUpdateTblCeaSupervisionNotice(
			@RequestBody @Validated TblCeaSupervisionNoticeOracle param) {
		MyJsonBean<TblCeaSupervisionNoticeOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.saveOrUpdateTblCeaSupervisionNotice(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知单 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "督办通知单 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaSupervisionNotice(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.deleteTblCeaSupervisionNotice(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知单 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "督办通知单 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaSupervisionNoticeOracle>> getTblCeaSupervisionNotice(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaSupervisionNoticeOracle>> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.getTblCeaSupervisionNotice(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知单 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "督办通知办理 列表查询")
	@PostMapping("/getBlList")
	public MyJsonBean<TblCeaSupervisionNoticeOracle> getBlList(@RequestBody TblCeaSupervisionNoticeQueryParam param) {
		MyJsonBean<TblCeaSupervisionNoticeOracle> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.getBlList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "督办通知办理 提交")
	@PostMapping("/saveBlSubmit")
	public MyJsonBean<Void> saveBlSubmit(@RequestBody TblCeaSupervisionNoticeQueryParam param) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaSupervisionNotice.saveBlSubmit(param.getId());
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("督办通知办理 提交 ...接口 异常", e);
		}
		return myJsonBean;
	}

}
