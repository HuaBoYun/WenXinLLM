package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.service.TblRiskReportingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险事件填报控制器
 * <p>提供风险报送中相关风险事件填报的列表查询、新增、修改等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/reporting")
@RestController
@Tag(name="风险报送-相关风险事件填报",description="风险报送-相关风险事件填报")
@Slf4j
public class TblRiskReportingController {


    @Resource
    private TblRiskReportingService tblRiskReportingService;

    @Resource
    private UserProvider userProvider;



    /**
     * 相关风险事件填报列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "相关风险事件填报列表",
            busType = "重大风险",
            fail = "相关风险事件填报列表",
            operationType = OperationType.SELECT,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/riskReportingList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "相关风险事件填报列表")
    public JsonBean riskReportingList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                   @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "entName", description = "涉及企业名称") @RequestParam(value = "entName", required = false) String  entName) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskReportingService.queryRiskReportingAll(token, pageNumber, pageSize,entName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }

    /**
     * 相关风险事件填报新增
     * @param token
     * @param tblRiskReporting
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "相关风险事件填报新增",
            busType = "重大风险",
            fail = "相关风险事件填报新增",
            operationType = OperationType.ADD,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/insertOrUpdate",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "相关风险事件填报新增")
    public JsonBean ReportingInsertOrUpdate(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @RequestBody TblRiskReportingEntity tblRiskReporting,String attIds) throws Exception {
        JsonBean bean = null;
        try {
             bean = tblRiskReportingService.insertOrUpdateReporting(tblRiskReporting, token,attIds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 相关风险事件填报详情
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "相关风险事件填报详情【{{#id}}】",
            busType = "重大风险",
            fail = "相关风险事件填报详情【{{#id}}】",
            operationType = OperationType.SELECT,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/riskReportingDetails",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "相关风险事件填报详情")
    public JsonBean riskReportingDetails(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "id", description = "相关风险事件主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        JsonBean bean = null;
        try {
             bean = tblRiskReportingService.riskReportingDetails(id, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, bean);
    }

    /**
     * 相关风险事件填报的附件
     * @param request
     * @param token
     * @param id
     * @return
     */

    @OperationLog(
            success = "相关风险事件填报的附件",
            busType = "重大风险",
            fail = "相关风险事件填报的附件",
            operationType = OperationType.SELECT,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/getRiskReportingAttInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "相关风险事件填报的附件")
    public JsonBean getRiskReportingAttInfo(HttpServletRequest request,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
                                            @Parameter(name = "id", description = "相关风险事件主键", required = true) @RequestParam(value = "id", required = true) BigDecimal id) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblRiskReportingService.getRiskReportingAttInfo(token,id);
        } catch (Exception e) {
            ResponseFormat.retParam(0,1000,e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 相关风险事件填报删除
     * @param token
     * @param id
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "相关风险事件填报删除【{{#id}}】",
            busType = "重大风险",
            fail = "相关风险事件填报删除【{{#id}}】",
            operationType = OperationType.DELETE,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/riskReportingDelete",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "相关风险事件填报删除")
    public JsonBean riskReportingDelete(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "id", description = "相关风险事件主键", required = true) @RequestParam(value = "id", required = true) String id
    ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null){
                return  ResponseFormat.retParam(0, 20006, hashMap);
            }
            tblRiskReportingService.riskReportingDelete(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);
    }


    @OperationLog(
            success = "重大风险事件填报导出--外层【{{#id}}】",
            busType = "重大风险",
            fail = "重大风险事件填报导出--外层【{{#id}}】",
            operationType = OperationType.EXPORT,
            subType = "相关风险事件填报"
    )
    @RequestMapping(value = "/exportRiskReport",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "重大风险事件填报导出--外层")
	public void  exportRiskReport(HttpServletRequest request, HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
											 @Parameter(name = "entName", description = "涉及企业名称") @RequestParam(value = "entName", required = false) String  entName,
											 @Parameter(name = "id", description = "相关风险事件主键", required = false) @RequestParam(value = "id", required = false) String id, Object task
										 ) {
		try {
			log.info("重大风险事件填报-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
		    TblStaffUtil user = userProvider.get();
		   List<TblRiskReportingEntity> entityList = tblRiskReportingService.exportRiskReport(entName, id);
			String[] cNames = {"涉及企业名称","涉及企业层级","风险事件名称", "风险类别","事件发生时间","当期情况描述",
					"损失(风险)金额","处理进展情况", "是否涉诉", "是否境外", "备注"};
			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			 for (TblRiskReportingEntity entity : entityList) {
				objs = new Object[11];
				objs[0] = entity.getEntName();
				objs[1] = entity.getEntLevel();
				objs[2] = entity.getRiskName();
				objs[3] = entity.getRiskType();
				objs[4] =entity.getEveTime()==null?"":com.huabo.fxgl.util.DateUtils.parseDate(entity.getEveTime(), "yyyy-MM-dd");
				objs[5] =entity.getSitDetails();
				objs[6] = entity.getLossAmount();//entity.getLossentitycategory().equals("1")?"一般事件":"重大事件";
				objs[7] = entity.getDisSituation();
				objs[8] = entity.getIsIvn();
				objs[9] =entity.getIsOver();
				objs[10] =entity.getRemarks();
				contractlist.add(objs);
			 }
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("重大风险事件填报".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}


