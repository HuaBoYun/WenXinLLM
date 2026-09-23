package com.huabo.audit.oracle.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.github.pagehelper.page.PageMethod;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblYqnsJhglJh;
import com.huabo.audit.oracle.mapper.AuditControlAnalysisMapper;
import com.huabo.audit.oracle.mapper.TblYqnsXmdqMapper;
import com.huabo.audit.oracle.service.AuditControlAnalysisService;
import com.huabo.audit.oracle.vo.CommandPlanData;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.vo.result.AuditControlAnalysisResult;
import com.huabo.audit.vo.result.QualityParam;
import com.spire.ms.System.Collections.Generic.List;

/**
 * @ Author: dev@example.com
 * @ Date: 2023/9/11
 * @ TODO:
 **/
@Service
public class AuditControlAnalysisServiceImpl implements AuditControlAnalysisService {

	@Resource
	private TblYqnsXmdqMapper tblYqnsXmdqMapper;
	
	@Resource
	private AuditControlAnalysisMapper auditControlAnalysisMapper;
	
	@Resource
    private UserProvider userProvider;
	
	@Override
	public JsonBean auditControlAnalysisService(String token, Integer preXmnd, Integer endXmnd, Integer choiceType,
			BigDecimal staffId, String staffName, Integer pageNumber, Integer pageSize)  throws Exception {
		/*TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	return ResponseFormat.retParam(0, 20006, null);
        }*/
		
        if(preXmnd == null) {
        	preXmnd = Year.now().getValue();
        }
        
        if(endXmnd == null) {
        	endXmnd = Year.now().getValue();
        }
        
        //生成年份
        String yearView = "CREATE OR REPLACE VIEW YEAV_VIEW AS ";
        for(int i = preXmnd ; i <= endXmnd ; i++){
        	yearView += " SELECT "+i+" AS YEARCOL FROM DUAL ,";
        }
        yearView = yearView.substring(0, yearView.length()-1);
        yearView = yearView.replace(",", " UNION ");
        this.tblYqnsXmdqMapper.executeSql(yearView);
        
        if(choiceType == 2) {
        	String personView = "CREATE OR REPLACE VIEW PERSON_VIEW AS " + 
        			"SELECT TO_NUMBER(TYX.CREATESTAFFID) AS STAFFID,TS.REALNAME AS STAFFNAME  FROM TBL_YQNS_XMQD TYX LEFT JOIN TBL_YQNS_JHGL_JH TYJJ ON TYX.PLANID = TYJJ.JHID LEFT JOIN TBL_STAFF TS ON TYX.CREATESTAFFID = TS.STAFFID WHERE TYX.STATUS = 1 AND TYJJ.XMND >= "+preXmnd+" AND TYJJ.XMND <= " +endXmnd+ 
        			" UNION " + 
        			"SELECT TO_NUMBER(TYAM.CREATEUSER) AS STAFFID,TS.REALNAME AS STAFFNAME FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT TYAM LEFT JOIN TBL_STAFF TS ON TYAM.CREATEUSER = TS.STAFFID WHERE TO_CHAR(TYAM.CREATETIME, 'YYYY') >= "+preXmnd+" AND TO_CHAR(TYAM.CREATETIME, 'YYYY') <= " + endXmnd+
        			" UNION " + 
        			"SELECT TO_NUMBER(TYAM.THEDEPTSTAFFID) AS STAFFID,TYAM.THEDEPTSTAFFNAME AS STAFFNAME FROM TBL_YQNS_AUDIT_MY_MANUSCRIPT TYAM LEFT JOIN TBL_STAFF TS ON TYAM.CREATEUSER = TS.STAFFID WHERE TO_CHAR(TYAM.CREATETIME, 'YYYY') >= "+preXmnd+" AND TO_CHAR(TYAM.CREATETIME, 'YYYY') <= " + endXmnd +
        			" UNION " + 
        			"SELECT EXT1 AS STAFFID,CJR AS STAFFNAME FROM TBL_YQNS_GZFA WHERE SPZT = 6 AND TO_CHAR(CJSJ, 'YYYY') >= "+preXmnd+" AND TO_CHAR(CJSJ, 'YYYY') <= " + endXmnd +
        			" UNION " + 
        			"SELECT TO_NUMBER(ZSSTAFFID) AS STAFFID,ZSNAME AS STAFFNAME FROM TBL_YQNS_IMPLEMENT_PLAN WHERE SPZT = 6 AND PLAN_YEAR >= "+preXmnd+" AND PLAN_YEAR <= " + endXmnd +
        			" UNION " + 
        			"SELECT TO_NUMBER(PROJECT_ORDER_ID) AS STAFFID,PROJECT_ORDER_NAME AS STAFFNAME FROM TBL_YQNS_IMPLEMENT_PLAN WHERE SPZT = 6 AND PLAN_YEAR >= "+preXmnd+" AND PLAN_YEAR <= " + endXmnd +
        			" UNION " + 
        			"SELECT TO_NUMBER(CREATESTAFFID) AS STAFFID,CREATEUSER AS STAFFNAME FROM TBL_YQNS_AUDIT_OVERSEE_RECORDS WHERE STATUS = 6 AND TO_CHAR(CREATETIME, 'YYYY') >= "+preXmnd+" AND TO_CHAR(CREATETIME, 'YYYY') <= " +endXmnd +
        			" UNION " + 
        			"SELECT TO_NUMBER(SUPERVISIONPARTICIPANTSID) AS STAFFID,SUPERVISIONPARTICIPANTS AS STAFFNAME FROM TBL_YQNS_AUDIT_OVERSEE_RECORDS WHERE STATUS = 6 AND TO_CHAR(CREATETIME, 'YYYY') >= "+preXmnd+" AND TO_CHAR(CREATETIME, 'YYYY') <= " + endXmnd +
        			" UNION " + 
        			"SELECT TO_NUMBER(ECONOMICID) AS STAFFID,ECONOMICNAME AS STAFFNAME FROM TBL_YQNS_AUDIT_OVERSEE_RECORDS WHERE STATUS = 6 AND TO_CHAR(CREATETIME, 'YYYY') >= "+preXmnd+" AND TO_CHAR(CREATETIME, 'YYYY') <=  "+ endXmnd;
        	this.tblYqnsXmdqMapper.executeSql(personView);
        }
        
		com.github.pagehelper.PageInfo<AuditControlAnalysisResult> pageInfo = PageMethod.startPage(pageNumber, pageSize)
				.doSelectPageInfo(() -> tblYqnsXmdqMapper.selectAuditControlAnalysisService(choiceType,staffId,staffName));
        PageResult<AuditControlAnalysisResult> page = new PageResult<AuditControlAnalysisResult>().build(pageInfo);
		return ResponseFormat.retParam(1, 200, page);
	}


	@Override
	public JsonBean beginYearPlanRate(String token, Integer queryYear) throws Exception {
        Map<String,Object> resultMap = new HashMap<String,Object>(0);
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		
		BigDecimal rate = this.auditControlAnalysisMapper.selectBeginYearPlanRate(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		resultMap.put("ncrate", rate);
		rate = this.auditControlAnalysisMapper.selectAuditYearPlanRate(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		resultMap.put("dnrate", rate);
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	@Override
	public JsonBean planCompletionRate(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		BigDecimal rate = this.auditControlAnalysisMapper.selectPlanCompletionRate(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		return ResponseFormat.retParam(1, 200, rate);
	}


	@Override
	public JsonBean rectificationAmount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		BigDecimal rate = this.auditControlAnalysisMapper.selectRectificationAmount(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		return ResponseFormat.retParam(1, 200, rate);
	}


	@Override
	public JsonBean rectificationRate(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		BigDecimal rate = this.auditControlAnalysisMapper.selectRectificationRate(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		return ResponseFormat.retParam(1, 200, rate);
	}


	@Override
	public JsonBean auditAdoptionRate(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		BigDecimal rate = this.auditControlAnalysisMapper.selectaAditAdoptionRate(queryYear);
		rate = rate.multiply(BigDecimal.valueOf(100));
		rate = rate.setScale(2, RoundingMode.HALF_UP);
		return ResponseFormat.retParam(1, 200, rate);	
	}
	
	
	
	@Override
	public JsonBean selectPlancount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR); 
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0); 
		List<QualityParam> list = this.auditControlAnalysisMapper.selectPlancount(queryYear);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap); 
	}
	
	
	@Override
	public JsonBean selectProjectcount(String token,Integer pageNumber, Integer pageSize, Integer queryYear) throws Exception {
	
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
//		List<ImplementPlanEntity> list = this.auditControlAnalysisMapper.selectBytjEntity(queryYear);
		 com.github.pagehelper.PageInfo<TblYqnsJhglJh> pageInfo = PageMethod.startPage(pageNumber, pageSize)
	                .doSelectPageInfo(() -> this.auditControlAnalysisMapper.selectBytjEntity(queryYear));
	        PageResult<TblYqnsJhglJh> page = new PageResult<TblYqnsJhglJh>().build(pageInfo);
	        return ResponseFormat.retParam(1, 200, page);
	}
	
	
	@Override
	public JsonBean selectPlanyfcount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<QualityParam> list = this.auditControlAnalysisMapper.selectPlanyfcount(queryYear);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	@Override
	public JsonBean selectPlanZtcount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		List<QualityParam> list = this.auditControlAnalysisMapper.selectPlanZtcount(queryYear);
		resultMap.put("list", list);
		return ResponseFormat.retParam(1, 200, resultMap);
	}


	@Override
	public JsonBean commandPlanData(String token, Integer queryYear) throws Exception {
		TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
        
        CommandPlanData data = this.auditControlAnalysisMapper.selectCommandPlanData(queryYear);
        data.setQueryYear(queryYear);
		
        if(data.getYearBeginPlanNum() == 0) {
        	data.setYearBeginPlanExcuteRate(BigDecimal.valueOf(0));
        }else {
        	data.setYearBeginPlanExcuteRate(this.calculate(data.getYearBeginPlanExcuteNum(), data.getYearBeginPlanNum()));
        }
        
        if(data.getAuditFindQuesNum() == null) {
        	data.setAuditFindQuesRate(BigDecimal.valueOf(0));
        }else {
        	data.setAuditFindQuesRate(this.calculate(data.getAuditRectQuesNum(), data.getAuditFindQuesNum()));
        }
        
        if(data.getAuditFindMoneyNum().compareTo(BigDecimal.ZERO) == 0) {
        	data.setAuditFindMoneRate(BigDecimal.valueOf(0));
        }else {
        	data.setAuditFindMoneRate(data.getAuditQuesMoneyNum().divide(data.getAuditFindMoneyNum(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).setScale(2, RoundingMode.HALF_UP));
        }
        
        if(data.getAuditadoptionTotalNum() == 0) {
        	data.setAuditadoptionRate(BigDecimal.valueOf(0));
        }else {
        	data.setAuditadoptionRate(this.calculate(data.getAuditadoptionNum(), data.getAuditadoptionTotalNum()));
        }
		
        return ResponseFormat.retParam(1, 200, data);
	}
	
	
	public BigDecimal calculate(Integer dividend, Integer divisor) {

        BigDecimal result = new BigDecimal(dividend)
                .divide(new BigDecimal(divisor), 4, RoundingMode.HALF_UP) // 先保留4位中间精度
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP); // 最终保留2位

        return result;
    }
	
	/**
	 * 项目运行情况；按照审前准备-现场实施-审计报告（交换意见稿/定稿）-审计整改-已完成
	 */
	@Override
	public JsonBean selectProjectYxStageCount(String token, Integer queryYear) throws Exception {
		if(queryYear == null) {
			Calendar calendar = Calendar.getInstance();
			queryYear = calendar.get(Calendar.YEAR);
		}
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//审前准备-运行中数量
		Integer sqzbNum = this.auditControlAnalysisMapper.selectProjectYxSjzbCount(queryYear);
		//现场实施-运行中数量
		Integer xcssNum = this.auditControlAnalysisMapper.selectProjectYxXcssCount(queryYear);
		//审计报告（交换意见稿/定稿）-运行中数量
		Integer sjbgNum = this.auditControlAnalysisMapper.selectProjectYxSjbgCount(queryYear);
		//审计整改-运行中数量
		Integer sjzgNum = this.auditControlAnalysisMapper.selectProjectYxSjzgCount(queryYear);
		//已完成-运行中数量
		Integer ywcNum = this.auditControlAnalysisMapper.selectProjectYxYwcCount(queryYear);

		CommandPlanData cpd = new CommandPlanData();
		cpd.setSqzbNum(sqzbNum);
		cpd.setXcssNum(xcssNum);
		cpd.setSjbgNum(sjbgNum);
		cpd.setSjzgNum(sjzgNum);
		cpd.setYwcNum(ywcNum);
		resultMap.put("data", cpd);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
	/**
	 * 人员情况：请假（请假时间）；在岗（审计部事务工作人员-在岗闲置；开展审计项目人员-在岗项目内）；外派任务（所执行任务的名称、地点）
	 */
	@Override
	public JsonBean selectStaffStateCount(String token) throws Exception {
		Map<String,Object> resultMap = new HashMap<String,Object>(0);
		//请假人员数量
		Integer staffQjNum = this.auditControlAnalysisMapper.selectStaffQjCount();
		//在岗闲置人员数量
		Integer staffZgxzNum = this.auditControlAnalysisMapper.selectStaffZgxzCount();
		//在岗项目内人员数量
		Integer staffZgxmnNum = this.auditControlAnalysisMapper.selectStaffZgxmnCount();
		//外派任务人员数量
		Integer staffWpNum = this.auditControlAnalysisMapper.selectStaffWprwCount();

		CommandPlanData cpd = new CommandPlanData();
		cpd.setStaffQjNum(staffQjNum);
		cpd.setStaffZgxzNum(staffZgxzNum);
		cpd.setStaffZgxmnNum(staffZgxmnNum);
		cpd.setStaffWpNum(staffWpNum);
		resultMap.put("data", cpd);
		return ResponseFormat.retParam(1, 200, resultMap);
	}
	
}
