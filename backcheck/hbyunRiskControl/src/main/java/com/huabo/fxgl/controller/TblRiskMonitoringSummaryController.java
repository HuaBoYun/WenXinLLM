package com.huabo.fxgl.controller;


import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.service.TblRiskMonitoringFillService;
import com.huabo.fxgl.service.impl.OrganizationServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险监测指标汇总控制器
 * <p>提供风险监测指标汇总的列表查询、统计分析等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/MonitoringSummary")
@RestController
@Tag(name="风险监测指标汇总",description="风险监测指标汇总")
@Slf4j
public class TblRiskMonitoringSummaryController {

    @Resource
    private TblRiskMonitoringFillService tblRiskMonitoringFillService;

    @Resource
    private OrganizationServiceImpl organizationService;

    @Value("${application.administrators:}")
	private String administrators;

    @Value("${application.auditlegaldepartment:}")
  	private String auditlegaldepartment;

    @Resource
    private UserProvider userProvider;


@OperationLog(
  success = "获取风险监测指标汇总列表 ",
  busType = "重大风险",
  fail = "获取风险监测指标汇总列表",
  operationType = OperationType.SELECT,
  subType = "风险监测指标汇总"
)
@RequestMapping(value = "/getHzList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "获取风险监测指标汇总列表")
public JsonBean getList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd
	) throws Exception {
JsonBean jsonBean = null;
try {
  jsonBean = tblRiskMonitoringFillService.getHzList(token,orgid,year,jd);
} catch (Exception e) {
  e.printStackTrace();
}
return jsonBean;
}




@OperationLog(
success = "作废功能",
busType = "重大风险",
fail = "作废功能",
operationType = OperationType.UPDATE,
subType = "风险监测指标填报"
)
@RequestMapping(value = "/monitoringCancel",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
@Operation(summary = "风险监测指标填报-作废")
public JsonBean monitoringCancel(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "id", description = "表单主键", required = true) @RequestParam(value = "id", required = true) String id
) throws Exception {
JsonBean bean = null;
try {
bean = tblRiskMonitoringFillService.monitoringCancel(token,id);
} catch (Exception e) {
e.printStackTrace();
}
return bean;
}


@OperationLog(
success = "导出",
busType = "重大风险",
fail = "导出",
operationType = OperationType.EXPORT,
subType = "风险监测指标汇总"
)
@Operation(summary = "风险监测指标汇总-导出")
@RequestMapping(value = "/export",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
@ResponseBody
public void exportByTemplate(HttpServletRequest request,HttpServletResponse response,
		 @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
	        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
	        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd,
                             @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
    try {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        Map<String, Object> result= tblRiskMonitoringFillService.getReportHzList(token,orgid,year,jd);
        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        Organization org=organizationService.findById(orgid);
        String fileName = URLEncoder.encode(org.getOrgname()+"第"+year+"年第"+jd+"风险分类监测指标体系表(责任部门填报).xls", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        tblRiskMonitoringFillService.exportByTemplate("template/风险分类监测指标体系表.xls", result, response.getOutputStream(),jd,orgid,year);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


@OperationLog(
		  success = "重大经营指标监测列表 ",
		  busType = "重大风险",
		  fail = "重大经营指标监测列表",
		  operationType = OperationType.SELECT,
		  subType = "风险监测指标汇总"
		)
		@RequestMapping(value = "/getZdHzList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
		@Operation(summary = "重大经营指标监测列表")
		public JsonBean getZdHzList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		        @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
		        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
		        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd
			) throws Exception {
		JsonBean jsonBean = null;
		try {
		  jsonBean = tblRiskMonitoringFillService.getZdHzList(token,orgid,year,jd);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return jsonBean;
		}


@OperationLog(
success = "重大经营导出",
busType = "重大风险",
fail = "重大经营导出",
operationType = OperationType.EXPORT,
subType = "风险监测指标汇总"
)
@Operation(summary = "重大经营-风险监测指标汇总-导出")
@RequestMapping(value = "/zdExport",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
@ResponseBody
public void zdExport(HttpServletRequest request,HttpServletResponse response,
		 @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
	        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
	        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd,
                             @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
    try {
        //验证用户登录
        TblStaffUtil staffUtil = userProvider.get();
        Map<String, Object> result= tblRiskMonitoringFillService.getReportZdHzList(token,orgid,year,jd);
        // 设置响应头
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setCharacterEncoding("utf-8");
        Organization org=organizationService.findById(orgid);
        String fileName = URLEncoder.encode(org.getOrgname()+"第"+year+"年第"+jd+"季度重大经营风险监测预警指标体系表.xls", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        tblRiskMonitoringFillService.exportByZdTemplate("template/季度重大经营风险监测预警指标体系表模板.xls", result, response.getOutputStream(),jd,orgid,year);
    } catch (Exception e) {
        e.printStackTrace();
    }
}




@OperationLog(
		  success = "重大经营指标监测列表 New",
		  busType = "重大风险",
		  fail = "重大经营指标监测列表New",
		  operationType = OperationType.SELECT,
		  subType = "风险监测指标汇总"
		)
		@RequestMapping(value = "/getZdHzListNew",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
		@Operation(summary = "重大经营指标监测列表New")
		public JsonBean getZdHzListNew(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		        @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
		        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
		        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd
			) throws Exception {
		JsonBean jsonBean = null;
		try {
		  jsonBean = tblRiskMonitoringFillService.getZdHzListNew(token,orgid,year,jd);
		} catch (Exception e) {
		  e.printStackTrace();
		}
		return jsonBean;
		}


@OperationLog(
success = "重大经营导出New",
busType = "重大风险",
fail = "重大经营导出New",
operationType = OperationType.EXPORT,
subType = "风险监测指标汇总"
)
@Operation(summary = "重大经营-风险监测指标汇总New-导出")
@RequestMapping(value = "/zdExportNew",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
@ResponseBody
public void zdExportNew(HttpServletRequest request,HttpServletResponse response,
		 @Parameter(name = "orgid", description = "组织架构") @RequestParam(value = "orgid") String orgid,
	        @Parameter(name = "year", description = "年") @RequestParam(value = "year") Integer year,
	        @Parameter(name = "jd", description = "季度") @RequestParam(value = "jd") String jd,
                           @Parameter(name = "token", description = "登录用户token") @RequestHeader("token")String token) {
  try {
      //验证用户登录
      TblStaffUtil staffUtil = userProvider.get();
      Map<String, Object> result= tblRiskMonitoringFillService.getReportZdHzListNew(token,orgid,year,jd);
      // 设置响应头
      response.setContentType("application/octet-stream;charset=UTF-8");
      response.setCharacterEncoding("utf-8");
      Organization org=organizationService.findById(orgid);
      String fileName = URLEncoder.encode(org.getOrgname()+"第"+year+"年第"+jd+"季度重大经营风险监测预警指标体系表.xls", "UTF-8");
      response.setHeader("Content-disposition", "attachment;filename=" + fileName);
      tblRiskMonitoringFillService.exportByZdTemplateNew("template/重大经营风险监测预警指标体系表模板.xls", result, response.getOutputStream(),jd,orgid,year);
  } catch (Exception e) {
      e.printStackTrace();
  }
}
}
