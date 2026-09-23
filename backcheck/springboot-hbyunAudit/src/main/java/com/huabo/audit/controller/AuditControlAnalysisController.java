package com.huabo.audit.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.service.AuditControlAnalysisService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName AuditControlAnalysis
 * @Description 审计管控分析
 * @Author ZiYao
 * @Date 2022/4/12 16:27
 * @Version 1.0
 */
@RestController
@Slf4j
@Tag(name="审计管控分析",description="审计管控分析")
@RequestMapping(value = "/auditControlAnalysis")
public class AuditControlAnalysisController {
	
	@Resource
	private AuditControlAnalysisService auditControlAnalysisService;
	
	@Resource
	private UserProvider userProvider;
	
	 /**
	  *审计结果指标分析汇总
     */
    @RequestMapping(value = "/indicator/analysis_summary", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计指标结果分析汇总")
    public JsonBean sjmxgetList(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
        	TblStaffUtil staff = userProvider.get();
    		if(staff == null) {
    			return ResponseFormat.retParam(0, 20006, null);
    		}
    		
    		//云端默认查两个 客户现场注释取消；
    		String limitStr = " AND STEPID IN (809666,900001)";
//    		String limitStr = null;
        	jsonBean = this.auditControlAnalysisService.sumIndicatorAnalysisSummary(staff,limitStr);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, "SQL有误，请修改", null);
        }
        return jsonBean;
    }
	
}
