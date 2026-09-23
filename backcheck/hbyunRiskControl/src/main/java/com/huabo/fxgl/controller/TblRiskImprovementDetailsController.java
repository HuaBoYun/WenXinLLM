package com.huabo.fxgl.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.service.TblRiskImprovementDetailsService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险监督改进详情控制器
 * <p>提供风险报送中风险监督改进详情的查询、编辑等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/improvementDetails")
@RestController
@Tag(name="风险报送-风险监督改进详情",description="风险报送-风险监督改进详情")
@Slf4j
public class TblRiskImprovementDetailsController {

    @Resource
    private TblRiskImprovementDetailsService tblRiskImprovementDetailsService;

    @Resource
    private UserProvider userProvider;

    /**
     * 风险监督改进详情列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "风险监督改进详情列表",
            busType = "重大风险",
            fail = "风险监督改进详情列表",
            operationType = OperationType.SELECT,
            subType = "风险监督改进详情"
    )
    @RequestMapping(value = "/riskImprovementDetailsList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监督改进详情列表")
    public JsonBean riskImprovementDetailsList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                        @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                        @Parameter(name = "riskImplementID", description = "分公司Id") @RequestParam(value = "riskImplementID", required = false) BigDecimal riskImplementID,
                                        @Parameter(name = "orgName", description = "分公司Id") @RequestParam(value = "orgName", required = false) String orgName) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImprovementDetailsService.riskImprovementDetailsList(token, pageNumber, pageSize,riskImplementID,orgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }

    @OperationLog(
            success = "风险监督改进详情导出",
            busType = "重大风险",
            fail = "风险监督改进详情 导出",
            operationType = OperationType.EXPORT,
            subType = "风险监督改进详情"
    )
    @RequestMapping(value = "/exportRiskReport",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
	@ResponseBody
    @Operation(summary = "风险监督改进详情 导出")
	public void  exportRiskReport(HttpServletRequest request, HttpServletResponse response,
											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
											 @Parameter(name = "riskImplementID", description = "分公司Id") @RequestParam(value = "riskImplementID", required = false) BigDecimal riskImplementID,
											 @Parameter(name = "ids", description = "多选id") @RequestParam(value = "ids", required = false) String ids,
		                                     @Parameter(name = "orgName", description = "分公司Id") @RequestParam(value = "orgName", required = false) String orgName ,
                                             Object task
										 ) {
		try {
			log.info("风险监督改进详情-导出Excel");
			response.setContentType("application/binary;charset=UTF-8");
		    TblStaffUtil user = userProvider.get();
		    List<TblRiskImprovementDetailsEntiry>  unitList= tblRiskImprovementDetailsService.exportRiskReport(ids,riskImplementID,orgName);
			String[] cNames = {"风险编号","分公司名称","责任部门","年份", "月份","是否上报","扣分情况"};
			List<Object[]> contractlist = new ArrayList<Object[]>(0);
			Object[] objs = null;
			 for (TblRiskImprovementDetailsEntiry entity : unitList) {
				objs = new Object[7];
				objs[0] = entity.getRisknumber();
				objs[1] = entity.getBranchName();
				objs[2] = entity.getOrgName();
				objs[3] = entity.getYears();
				objs[4] = entity.getMonth();
				objs[5] =entity.getIsReport();
				objs[6] =entity.getScoreDetails();
				contractlist.add(objs);
			 }
			response.setHeader("Content-Disposition", "attachment;filename=" + new String("风险监督改进".getBytes(),"UTF-8") + ".xlsx");
			ServletOutputStream outputStream = response.getOutputStream();
			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    @OperationLog(
            success = "风险监督改进详情页面单条 导出",
            busType = "重大风险",
            fail = "风险监督改进详情页面单条 导出",
            operationType = OperationType.EXPORT,
            subType = "风险监督改进详情"
    )
    @RequestMapping(value = "/exportRiskImprovementDetailsReport",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
   	@ResponseBody
       @Operation(summary = "风险监督改进详情页面单条 导出")
   	public void  exportRiskImprovementDetailsReport(HttpServletRequest request, HttpServletResponse response,
   											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
   											 @Parameter(name = "id", description = "id") @RequestParam(value = "id", required = false) BigDecimal id, Object task
   										 ) {
   		try {
   			log.info("风险监督改进详情-导出Excel");
   			response.setContentType("application/binary;charset=UTF-8");
   		    TblStaffUtil user = userProvider.get();
   		 TblRiskImprovementDetailsEntiry  entity = tblRiskImprovementDetailsService.riskImprovementDetails(token, id);
   			String[] cNames = {"风险描述","分公司名称","年份", "月份","是否上报","扣分情况"};
   			List<Object[]> contractlist = new ArrayList<Object[]>(0);
   			Object[] objs = null;
   			// for (TblRiskImprovementDetailsEntiry entity : unitList) {
   				objs = new Object[11];
   				objs[0] = entity.getImpRiskName();
   				objs[1] = entity.getBranchName();
   				objs[2] = entity.getYears();
   				objs[3] = entity.getMonth();
   				objs[4] =entity.getIsReport();
   				objs[5] =entity.getScoreDetails();
   				contractlist.add(objs);
   		//	 }
   			response.setHeader("Content-Disposition", "attachment;filename=" + new String("风险监督改进详情导出".getBytes(),"UTF-8") + ".xlsx");
   			ServletOutputStream outputStream = response.getOutputStream();
   			ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   	}

    /**
     * 风险监督改进--改分操作
     *集团风控部需要手动改分功能（按公司改分）
     *1.当前分数不能大于100 第二 修改分数不能小于当前分数
     * @return
     * @throws Exception
     */

//    @OperationLog(
//            success = "集团手动改分功能",
//            busType = "重大风险",
//            fail = "集团手动改分功能",
//            operationType = OperationType.UPDATE,
//            subType = "风险监督改进详情"
//    )
//    @RequestMapping(value = "/updateScore",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
//    @Operation(summary = "风险监督改进-集团风控部需要手动改分功能")
//    public JsonBean riskImprovementList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
//										 @Parameter(name = "id", description = "单位id", required = false) @RequestParam(value = "id", required = false) String id,
//										 @Parameter(name = "score", description = "要改的分数", required = false) @RequestParam(value = "score", required = false) Integer score) throws Exception {
//        JsonBean jsonBean = null;
//        try {
//            jsonBean = tblRiskImprovementDetailsService.updateScore(token, id, score);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return ResponseFormat.retParam(1, 200, jsonBean);
//    }
//
    @OperationLog(
            success = "详情列表改分功能",
            busType = "重大风险",
            fail = "详情列表改分功能",
            operationType = OperationType.UPDATE,
            subType = "风险监督改进详情"
    )
    @RequestMapping(value = "/updateScoreNew",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监督改进-详情列表改分功能")
    public JsonBean updateScoreNew(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
										 @Parameter(name = "id", description = "详情列表id", required = false) @RequestParam(value = "id", required = false) BigDecimal id,
										 @Parameter(name = "score", description = "要改的分数", required = false) @RequestParam(value = "score", required = false) String score) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImprovementDetailsService.updateScoreNew(token, id, score);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }
}
