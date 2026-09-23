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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaPeopleLeaveOracle;
import com.huabo.central.enterprises.audit.service.CeaPeopleLeaveService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.util.PageResult;
import com.huabo.central.enterprises.audit.util.excel.ExcelExport;
import com.huabo.central.enterprises.audit.vo.param.TblCeaPeopleLeaveQueryParam;
import com.huabo.central.enterprises.audit.vo.result.TblCeaPeopleLeaveExpress;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-人员请假单",description="综合管理-人员请假单")
@RequestMapping(value = "/api-auth/people/leave")
@Slf4j
public class CeaPeopleLeaveController {

	@Resource
	private CeaPeopleLeaveService ceaPeopleLeaveService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "人员请假单 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveList(@RequestBody TblCeaPeopleLeaveQueryParam param) {
		MyJsonBean<TblCeaPeopleLeaveOracle> myJsonBean = null;
		try {
			myJsonBean = ceaPeopleLeaveService.getTblCeaPeopleLeaveList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员请假单 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "人员请假单 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaPeopleLeaveOracle> saveOrUpdateTblCeaPeopleLeave(@RequestBody @Validated TblCeaPeopleLeaveOracle param) {
		MyJsonBean<TblCeaPeopleLeaveOracle> myJsonBean = null;
		try {
			myJsonBean = ceaPeopleLeaveService.saveOrUpdateTblCeaPeopleLeave(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员请假单 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "人员请假单 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaPeopleLeave(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaPeopleLeaveService.deleteTblCeaPeopleLeave(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员请假单 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "人员请假单 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeave(@PathVariable Long id) {
		MyJsonBean<TblCeaPeopleLeaveOracle> myJsonBean = null;
		try {
			myJsonBean = ceaPeopleLeaveService.getTblCeaPeopleLeave(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员请假单 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "人员请假台账")
	@PostMapping("/getAllList")
	public MyJsonBean<TblCeaPeopleLeaveOracle> getTblCeaPeopleLeaveAllList(@RequestBody TblCeaPeopleLeaveQueryParam param) {
		MyJsonBean<TblCeaPeopleLeaveOracle> myJsonBean = null;
		try {
			myJsonBean = ceaPeopleLeaveService.getTblCeaPeopleLeaveAllList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("人员请假台账 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "导出 人员请假台账")
	@PostMapping("/download-express")
	public void downloadExpress(@RequestHeader("token") String token, @RequestBody TblCeaPeopleLeaveQueryParam param, HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		MyJsonBean myJsonBean = ceaPeopleLeaveService.getTblCeaPeopleLeaveAllList(param);
		PageResult<TblCeaPeopleLeaveOracle> result = (PageResult<TblCeaPeopleLeaveOracle>) myJsonBean.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			MyJsonBean list = ceaPeopleLeaveService.getTblCeaPeopleLeaveAllList(param);
			PageResult<TblCeaPeopleLeaveOracle> tempList = (PageResult<TblCeaPeopleLeaveOracle>) list.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = "人员请假台账列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblCeaPeopleLeaveExpress.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				result.getTlist().forEach(item -> {
					if (Objects.nonNull(item.getStaff())) {
						item.setUserName(item.getStaff().getRealName());
					}
					if (Objects.nonNull(item.getParworkdate())) {
						String format = new SimpleDateFormat("yyyy-MM-dd").format(item.getParworkdate());
						item.setParworkdate1(format);
					}
					if (Objects.nonNull(item.getLeavePeriodTimeStart()) && Objects.nonNull(item.getLeavePeriodTimeEnd())) {
						String format1 = new SimpleDateFormat("yyyy-MM-dd").format(item.getLeavePeriodTimeStart());
						String format2 = new SimpleDateFormat("yyyy-MM-dd").format(item.getLeavePeriodTimeEnd());
						String str = format1 + "至" + format2;
						item.setLeaveTimeString(str);
					}
					if (Objects.nonNull(item.getLeavedays()) && Objects.nonNull(item.getActualleavedays())) {
						int day = item.getActualleavedays()-item.getLeavedays();
						item.setDifferDays(day);
					}
				});
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
