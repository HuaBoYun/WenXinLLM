package com.huabo.fxgl.controller;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.service.TblRiskImprovementService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 风险监督改进控制器
 * <p>提供风险报送中风险监督改进的列表查询、新增、修改等接口</p>
 *
 * @author hbyun
 */
@RequestMapping(value = "/improvement")
@RestController
@Tag(name="风险报送-风险监督改进",description="风险报送-风险监督改进")
@Slf4j
public class TblRiskImprovementController {

    @Resource
    private TblRiskImprovementService tblRiskImprovementService;

    @Resource
    private UserProvider userProvider;

    /**
     * 风险监督改进列表
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "风险监督改进列表",
            busType = "重大风险",
            fail = "风险监督改进列表",
            operationType = OperationType.SELECT,
            subType = "风险监督改进"
    )
    @RequestMapping(value = "/riskImprovementList",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "风险监督改进列表")
    public JsonBean riskImprovementList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "pageNumber", description = "页数") @RequestParam(defaultValue = "1") Integer pageNumber,
                                        @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
										 @Parameter(name = "name", description = "单位名称", required = false) @RequestParam(value = "name", required = false) String name,
										 @Parameter(name = "orgName", description = "责任单位", required = false) @RequestParam(value = "orgName", required = false) String orgName) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblRiskImprovementService.riskImprovementList(token, pageNumber, pageSize,name,orgName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, jsonBean);
    }


    @OperationLog(
            success = "风险监督改进列表导出",
            busType = "重大风险",
            fail = "风险监督改进列表导出",
            operationType = OperationType.EXPORT,
            subType = "风险监督改进"
    )
    @RequestMapping(value = "/exportRiskImprovementList",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
   	@ResponseBody
       @Operation(summary = "风险监督改进列表导出")
   	public void  exportRiskImprovementList(HttpServletRequest request, HttpServletResponse response,
   											 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
   											 @Parameter(name = "jsonString", description = "json内容串", required = false) @RequestParam(value = "jsonString", required = false) String jsonString,
   											@Parameter(name = "name", description = "单位名称", required = false) @RequestParam(value = "name", required = false) String name,
   										 @Parameter(name = "orgName", description = "责任单位", required = false) @RequestParam(value = "orgName", required = false) String orgName) throws Exception {
   		try {
   			log.info("风险监督改进列表-导出Excel");
   			response.setContentType("application/binary;charset=UTF-8");
   		    TblStaffUtil user = userProvider.get();
   		    List<JSONObject> array=null;
		    List<Object[]> oList=new ArrayList<Object[]>();
		    if(StringUtils.isNotBlank(jsonString)&&jsonString.length()>2){
		    	 array=JSONObject.parseArray(jsonString,JSONObject.class);
		    	 for(JSONObject o:array){
		    		 Object[] obj=new Object[3];
		    		 obj[0]=o.get("branchName");
		    		 obj[1]=o.get("orgName");
		    		 obj[2]=o.get("score");
		    		oList.add(obj);
		    	 }
   		    }else {
   		     List<TblRiskImprovementDetailsEntiry> oList2= tblRiskImprovementService.reportList(token,name,orgName);
   			    for(TblRiskImprovementDetailsEntiry o:oList2){
		    		 Object[] obj=new Object[3];
		    		 obj[0]=o.getBranchName();
		    		 obj[1]=o.getOrgName();
		    		 obj[2]=o.getScoreDetails();
		    		 oList.add(obj);
		    	 }
   		    }
   			String[] cNames = {"分公司名称","责任部门","总分"};
   			response.setHeader("Content-Disposition", "attachment;filename=" + new String("风险监督改进列表".getBytes(),"UTF-8") + ".xlsx");
   			ServletOutputStream outputStream = response.getOutputStream();
   			ImportOrExportExcelUtil.exportExcel(cNames, oList, outputStream, null);
   		} catch (Exception e) {
   			e.printStackTrace();
   		}
   	}



}
