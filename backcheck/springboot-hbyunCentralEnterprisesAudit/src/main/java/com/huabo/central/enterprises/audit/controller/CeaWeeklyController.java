package com.huabo.central.enterprises.audit.controller;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
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
import com.huabo.central.enterprises.audit.oracle.entity.TblCeaWeekly;
import com.huabo.central.enterprises.audit.oracle.service.TblCeaWeeklyService;
import com.huabo.central.enterprises.audit.oracle.service.TblStaffOracleService;
import com.huabo.central.enterprises.audit.service.CeaWeeklyService;
import com.huabo.central.enterprises.audit.util.MyJsonBean;
import com.huabo.central.enterprises.audit.vo.param.RemindTblCeaWeeklyParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaSealFormQueryParam;
import com.huabo.central.enterprises.audit.vo.param.TblCeaWeeklyQueryParam;
import com.huabo.central.enterprises.audit.vo.result.FileVo;
import com.huabo.central.enterprises.audit.vo.result.RemindTblCeaWeeklyResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Tag(name="综合管理-周报",description="综合管理-周报")
@RequestMapping(value = "/api-auth/weekly")
@Slf4j
public class CeaWeeklyController {

	@Resource
	private CeaWeeklyService ceaWeeklyService;
	
	@Resource
	private UserProvider userProvider;

	@Operation(summary = "周报 列表查询")
	@PostMapping("/getList")
	public MyJsonBean<TblCeaWeekly> getTblCeaWeeklyList(@RequestBody TblCeaWeeklyQueryParam param) {
		MyJsonBean<TblCeaWeekly> myJsonBean = null;
		try {
			myJsonBean = ceaWeeklyService.getTblCeaWeeklyList(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("周报 列表查询 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "周报 新增/更新")
	@PostMapping("/saveOrUpdate")
	public MyJsonBean<TblCeaWeekly> saveOrUpdateTblCeaWeekly(@RequestBody @Validated TblCeaWeekly param) {
		MyJsonBean<TblCeaWeekly> myJsonBean = null;
		try {
			myJsonBean = ceaWeeklyService.saveOrUpdateTblCeaWeekly(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("周报 新增/更新 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "周报 刪除")
	@DeleteMapping("/{id}")
	public MyJsonBean<Void> deleteTblCeaWeekly(@PathVariable Long id) {
		MyJsonBean<Void> myJsonBean = null;
		try {
			myJsonBean = ceaWeeklyService.deleteTblCeaWeekly(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("周报 刪除 ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "周报 详情 查询")
	@GetMapping("/{id}")
	public MyJsonBean<FileVo<TblCeaWeekly>> getTblCeaWeekly(@PathVariable Long id) {
		MyJsonBean<FileVo<TblCeaWeekly>> myJsonBean = null;
		try {
			myJsonBean = ceaWeeklyService.getTblCeaWeekly(id);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("周报 详情 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}

	@Operation(summary = "周报每周四提醒-指定角色 查询")
	@PostMapping("/remind")
	public MyJsonBean<RemindTblCeaWeeklyResult> remindTblCeaWeekly(@RequestBody RemindTblCeaWeeklyParam param) {
		MyJsonBean<RemindTblCeaWeeklyResult> myJsonBean = null;
		try {
			myJsonBean = ceaWeeklyService.remindTblCeaWeekly(param);
		} catch (ServiceException ex) {
			throw ex;
		} catch (Exception e) {
			log.error("周报每周四提醒 查询  ...接口 异常", e);
		}
		return myJsonBean;
	}
	
	
	
	@Resource
	private TblCeaWeeklyService tblCeaWeeklyService;
	@Resource
	private TblStaffOracleService tblStaffOracleService;
	
	@Operation(summary = "周报-导出Word")
	@PostMapping("/exportWord")
	public void exportWord(@RequestHeader("token") String token, @RequestBody TblCeaSealFormQueryParam param, HttpServletResponse response) throws Exception {
		
		TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			throw new ServiceException(401, 20006);
		}
		
		//查询周报信息
		TblCeaWeekly model = tblCeaWeeklyService.findById(param.getId());
		if (Objects.nonNull(model.getStaffId())) {
			model.setStaffName(tblStaffOracleService.getCreatorUserInfo(model.getStaffId()));
		}
		if (Objects.nonNull(model.getCreator())) {
			model.setCreatorName(tblStaffOracleService.getCreatorUserInfo(model.getCreator()));
		}
		if (Objects.nonNull(model.getReportWorkUnit())) {
			model.setReportWorkUnitName(tblStaffOracleService.getWorkUnitIdUserInfo(model.getReportWorkUnit()));
		}
		
        // 创建一个空的 Word 文档
        XWPFDocument document = new XWPFDocument();
        // 创建一个段落
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.createRun().setText("上报部门");
        paragraph.createRun().addBreak();
        paragraph.createRun().setText(model.getReportWorkUnitName());
        paragraph.createRun().addBreak();
        paragraph.createRun().addBreak();
        paragraph.createRun().setText("上报人员");
        paragraph.createRun().addBreak();
        paragraph.createRun().setText(model.getCreatorName());
        paragraph.createRun().addBreak();
        paragraph.createRun().addBreak();
        paragraph.createRun().setText("本周工作完成情况");
        paragraph.createRun().addBreak();
        paragraph.createRun().setText(model.getThisWeekComplete());
        paragraph.createRun().addBreak();
        paragraph.createRun().addBreak();
        paragraph.createRun().setText("下周重点工作安排");
        paragraph.createRun().addBreak();
        paragraph.createRun().setText(model.getNextWeekComplete());
        paragraph.createRun().addBreak();
        // 设置响应头
        response.setHeader("Content-Disposition", "attachment; filename=\"周报.docx\"");
        // 写入响应
        try {
			document.write(response.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
}
