package com.huabo.legal.controller;


import java.util.ArrayList;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.legal.exception.ServiceException;
import com.huabo.legal.mysql.entity.TblFwglLawServiceMySql;
import com.huabo.legal.oracle.entity.TblFwglLawServiceEvaluateOracle;
import com.huabo.legal.service.LegalServiceService;
import com.huabo.legal.util.PageResult;
import com.huabo.legal.util.excel.ExcelExport;
import com.huabo.legal.vo.param.TblFwglLawServiceQueryParam;
import com.huabo.legal.vo.param.TblFwglSpecialLawServiceExamineQueryParam;
import com.huabo.legal.vo.result.TblFwglLawService;
import com.huabo.legal.vo.result.TblFwglLawServiceLawyer;
import com.huabo.legal.vo.result.TblFwglLawServiceWorkRecord;
import com.huabo.legal.vo.result.TblFwglLawServiceWorkReport;
import com.huabo.legal.vo.result.TblFwglPerennialLawServiceGrade;
import com.huabo.legal.vo.result.TblFwglSpecialLawServiceExamine;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="法务管理-法律服务所有接口",description="法务管理-法律服务所有接口")
@RequestMapping(value = "/api-auth/legal/service")
@Slf4j
public class LegalServiceController {

	@Resource
	private LegalServiceService legalServiceService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "常年/专项法律服务列表 查询")
	@PostMapping("/getList")
	public JsonBean getTblFwglLawServiceList(@RequestBody @Validated TblFwglLawServiceQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawServiceList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务列表 查询 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务 新增/更新")
	@PostMapping("/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLawService(@RequestBody @Validated TblFwglLawService param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglLawService(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务 刪除")
	@DeleteMapping("/{id}")
	public JsonBean deleteTblFwglLawService(@PathVariable Long id,
			@Parameter(name = "lawServiceType", description = "法律服务类型1-常年 2-专项", required = true) @RequestParam("lawServiceType") Integer lawServiceType) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglLawService(id, lawServiceType);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务详情 查询")
	@GetMapping("/{id}")
	public JsonBean getTblFwglLawService(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawService(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-律师信息 新增/更新")
	@PostMapping("/lawyer/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLawServiceLawyer(@RequestBody @Validated TblFwglLawServiceLawyer param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglLawServiceLawyer(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-律师信息 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-律师信息详情 查询")
	@GetMapping("/lawyer/{id}")
	public JsonBean getTblFwglLawServiceLawyer(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawServiceLawyer(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-律师信息详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-律师信息 刪除")
	@DeleteMapping("/lawyer/{id}")
	public JsonBean deleteTblFwglLawServiceLawyer(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglLawServiceLawyer(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-律师信息 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作记录 新增/更新")
	@PostMapping("/work/record/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLawServiceWorkRecord(@RequestBody @Validated TblFwglLawServiceWorkRecord param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglLawServiceWorkRecord(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作记录 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作记录详情 查询")
	@GetMapping("/work/record/{id}")
	public JsonBean getTblFwglLawServiceWorkRecord(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawServiceWorkRecord(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作记录详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作记录 刪除")
	@DeleteMapping("/work/record/{id}")
	public JsonBean deleteTblFwglLawServiceWorkRecord(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglLawServiceWorkRecord(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作记录 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作报告表/服务登记 新增/更新")
	@PostMapping("/work/report/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLawServiceWorkReport(@RequestBody @Validated TblFwglLawServiceWorkReport param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglLawServiceWorkReport(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作报告表/服务登记 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作报告表/服务登记详情 查询")
	@GetMapping("/work/report/{id}")
	public JsonBean getTblFwglLawServiceWorkReport(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawServiceWorkReport(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作报告表/服务登记详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年/专项法律服务-工作报告表/服务登记 刪除")
	@DeleteMapping("/work/report/{id}")
	public JsonBean deleteTblFwglLawServiceWorkReport(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglLawServiceWorkReport(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年/专项法律服务-工作报告表/服务登记 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评分 新增/更新")
	@Deprecated
	@PostMapping("/grade/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglPerennialLawServiceGrade(@RequestBody @Validated TblFwglPerennialLawServiceGrade param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglPerennialLawServiceGrade(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评分 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评分详情 查询")
	@Deprecated
	@GetMapping("/grade/{id}")
	public JsonBean getTblFwglPerennialLawServiceGrade(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglPerennialLawServiceGrade(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评分详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评分 刪除")
	@Deprecated
	@DeleteMapping("/grade/{id}")
	public JsonBean deleteTblFwglPerennialLawServiceGrade(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglPerennialLawServiceGrade(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评分 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "专项法律服务-考核 新增/更新")
	@PostMapping("/examine/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglSpecialLawServiceExamine(@RequestBody @Validated TblFwglSpecialLawServiceExamine param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglSpecialLawServiceExamine(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("专项法律服务-考核 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "专项法律服务-考核详情 查询")
	@GetMapping("/examine/{id}")
	public JsonBean getTblFwglSpecialLawServiceExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglSpecialLawServiceExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("专项法律服务-考核详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "专项法律服务-考核 刪除")
	@DeleteMapping("/examine/{id}")
	public JsonBean deleteTblFwglSpecialLawServiceExamine(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglSpecialLawServiceExamine(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("专项法律服务-考核 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评价表 新增/更新")
	@PostMapping("/evaluate/saveOrUpdate")
	public JsonBean saveOrUpdateTblFwglLawServiceEvaluate(@RequestBody TblFwglLawServiceEvaluateOracle param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.saveOrUpdateTblFwglLawServiceEvaluate(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评价表 新增/更新 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评价详情 查询")
	@GetMapping("/evaluate/{id}")
	public JsonBean getTblFwglLawServiceEvaluate(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglLawServiceEvaluate(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评价详情 查询  ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "常年法律服务-评价 刪除")
	@DeleteMapping("/evaluate/{id}")
	public JsonBean deleteTblFwglLawServiceEvaluate(@PathVariable Long id) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.deleteTblFwglLawServiceEvaluate(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("常年法律服务-评价 刪除 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "考核台账列表")
	@PostMapping("/examine/getList")
	public JsonBean getTblFwglSpecialLawServiceExamineList(@RequestBody TblFwglSpecialLawServiceExamineQueryParam param) {
		JsonBean jsonBean = null;
		try {
			jsonBean = legalServiceService.getTblFwglSpecialLawServiceExamineList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("考核台账列表 ...接口 异常", e);
		}
		return jsonBean;
	}

	@Operation(summary = "导出 常年/专项法律服务列表")
	@GetMapping("/work/report/download-express")
	public void downloadExpressTblFwglLawService(@RequestHeader("token") String token, TblFwglLawServiceQueryParam param,
			HttpServletResponse response) throws Exception {
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		param.setBelongGroup(String.valueOf(loginStaff.getCurrentOrg().getOrgid()));
		int perPage = 5000;
		param.setPageNumber(1);
		param.setPageSize(perPage);
		JsonBean tblFwglLawServiceList = legalServiceService.getTblFwglLawServiceList(param);
		PageResult<TblFwglLawServiceMySql> result = (PageResult<TblFwglLawServiceMySql>) tblFwglLawServiceList.getData();
		int currentPage = 2;
		int pages = (int) (result.getTotalPage() / perPage + (result.getTotalPage() % perPage > 0 ? 1 : 0));
		while (currentPage <= pages) {
			param.setPageNumber(currentPage++);
			JsonBean tblFwglLawServiceTempList = legalServiceService.getTblFwglLawServiceList(param);
			PageResult<TblFwglLawServiceMySql> tempList = (PageResult<TblFwglLawServiceMySql>) tblFwglLawServiceTempList.getData();
			result.getTlist().addAll(tempList.getTlist());
		}
		// 生成excel下载
		String filename = System.currentTimeMillis() + "常年/专项法律服务列表.xlsx";
		try (ExcelExport export = new ExcelExport(TblFwglLawService.class)) {
			if (CollectionUtil.isNotEmpty(result.getTlist())) {
				export.setDataList(result.getTlist()).write(response, filename);
				return;
			}
			export.setDataList(new ArrayList<>()).write(response, filename);
		}
	}
}
