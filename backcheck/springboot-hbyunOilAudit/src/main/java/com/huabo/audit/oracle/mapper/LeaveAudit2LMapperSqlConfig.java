package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.LeaveAudit2LEntity;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.redis.Random.RandomUtil;

import java.math.BigDecimal;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName LeaveAudit2LMapperSqlConfig
 * @Description
 * @DATE 2023/9/14
 */
public class LeaveAudit2LMapperSqlConfig {
	
	public String findListByAnalysis(Integer xmnd, String projectName) throws Exception{
		StringBuffer sb = new StringBuffer();
        sb.append("SELECT RS.*,TS.STAFFID,TS.REALNAME FROM TBL_YQNS_LEAVE_AUDIT_2L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE ");
        
        sb.append(" RS.ID IN ( SELECT GLID FROM TBL_YQNS_JHGL_JH_GL WHERE JHID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLTYPE = '21' )");
        sb.append(" AND RS.ID NOT IN (SELECT GLJHXMID FROM TBL_YQNS_XMQD WHERE PLANID IN (SELECT JHID FROM TBL_YQNS_JHGL_JH WHERE XMND = "+xmnd+") AND GLJHXMLX = '21')");
        if(StringUtil.isNotEmpty(projectName)){
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+projectName+"%'");
        }
        
        sb.append(" ORDER BY RS.CREATE_TIME DESC ");
        return sb.toString();
	}

    public String selectByEntity( LeaveAudit2LEntity leaveAudit2LEntity) throws Exception{
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT RS.*,TS.STAFFID,TS.REALNAME FROM TBL_YQNS_LEAVE_AUDIT_2L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        sb.append(" AND ( RS.CREATE_USER = ").append(leaveAudit2LEntity.getCurrentStaffId()).append(" OR ").append(DataBaseSqlConfig.getWhereColumnInStr("RS.PERSON_IDS", leaveAudit2LEntity.getCurrentStaffId().toString(), ","));
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getQueryDeptIds())) {
        	sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(leaveAudit2LEntity.getQueryDeptIds()).append(")) ");
        }
        
        sb.append(" ) ");
        
        if(leaveAudit2LEntity.getAuditOrg() != null && com.hbfk.util.StringUtil.isNotEmpty(leaveAudit2LEntity.getAuditOrg().getOrgname())){
            sb.append(" AND RS.AUDIT_ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+leaveAudit2LEntity.getAuditOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectName())){
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+leaveAudit2LEntity.getProjectName()+"%'");
        }
        
        if(leaveAudit2LEntity.getQueryYear() != null) {
        	sb.append(" AND RS.CREATE_TIME LIKE '").append(leaveAudit2LEntity.getQueryYear()+"-").append("%'");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getIds())){
            sb.append(" AND RS.ID IN (" + leaveAudit2LEntity.getIds()+")");
        }

        sb.append(" ORDER BY RS.CREATE_TIME DESC ");
        return sb.toString();
    }

    public String selectListByDraftPlan(LeaveAudit2LEntity leaveAudit2LEntity, Integer sourceType, BigDecimal jhid)  throws Exception{
    	StringBuffer sb = new StringBuffer();
        sb.append("SELECT RS.*,TS.STAFFID,TS.REALNAME FROM TBL_YQNS_LEAVE_AUDIT_2L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(sourceType == 2) {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE GLTYPE = '21') AND RS.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '21' AND JHCGID = "+jhid+")");
        }else if (sourceType == 3) {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JH_GL WHERE GLTYPE = '21') AND RS.ID IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCHUG_GL WHERE GLTYPE = '21' AND JHCHUGID = "+jhid+" )");
        }else {
        	sb.append(" AND RS.ID NOT IN (SELECT GLID FROM TBL_YQNS_JHGL_JHCG_GL WHERE GLTYPE = '21')");
        }
       
        if(leaveAudit2LEntity.getAuditOrg() != null && com.hbfk.util.StringUtil.isNotEmpty(leaveAudit2LEntity.getAuditOrg().getOrgname())){
            sb.append(" AND RS.AUDIT_ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+leaveAudit2LEntity.getAuditOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectName())){ 
            sb.append(" AND RS.PROJECT_NAME LIKE '%"+leaveAudit2LEntity.getProjectName()+"%'");
        }
        
        if(leaveAudit2LEntity.getQueryYear() != null) {
        	sb.append(" AND RS.CREATE_TIME LIKE '").append(leaveAudit2LEntity.getQueryYear()+"-").append("%'");
        }
        
        if(leaveAudit2LEntity.getStatus() != null) {
        	sb.append(" AND RS.STATUS = "+leaveAudit2LEntity.getStatus());
        }
        
        sb.append(" ORDER BY RS.CREATE_TIME DESC ");
        return sb.toString();
    	
    }
    
    
    public String selectCountByEntity(LeaveAudit2LEntity leaveAudit2LEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_LEAVE_AUDIT_2L RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");

        if(leaveAudit2LEntity.getAuditOrg() != null && com.hbfk.util.StringUtil.isNotEmpty(leaveAudit2LEntity.getAuditOrg().getOrgname())){
            sb.append("AND RS.AUDIT_ORG IN ( SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+leaveAudit2LEntity.getAuditOrg().getOrgname()+"%') ");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+leaveAudit2LEntity.getProjectName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(LeaveAudit2LEntity leaveAudit2LEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_LEAVE_AUDIT_2L SET ");
        sb.append("PROJECT_NAME = '"+leaveAudit2LEntity.getProjectName()+"'");

        if(leaveAudit2LEntity.getLeaveNo() != null){
            sb.append(", LEAVENO = '"+leaveAudit2LEntity.getLeaveNo()+"'");
        }

        
        if(StringUtil.isNotEmpty(  leaveAudit2LEntity.getAuditOrgId() )){
            sb.append(", AUDIT_ORG = '"+leaveAudit2LEntity.getAuditOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getEntrustNo())){
            sb.append(", ENTRUST_NO = '"+leaveAudit2LEntity.getEntrustNo()+"'");
        }

        if(leaveAudit2LEntity.getEntrustTime() != null){
            sb.append(", ENTRUST_TIME = '"+DateUtil.format(leaveAudit2LEntity.getEntrustTime(),"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+leaveAudit2LEntity.getPersonIds()+"'");
        }

        if(leaveAudit2LEntity.getAuditStartTime() != null){
            sb.append(", AUDIT_START_TIME = '"+DateUtil.format(leaveAudit2LEntity.getAuditStartTime(),"yyyy-MM-dd")+"'");
        }

        if(leaveAudit2LEntity.getAuditEndTime() != null){
            sb.append(", AUDIT_END_TIME = '"+DateUtil.format(leaveAudit2LEntity.getAuditEndTime(),"yyyy-MM-dd")+"'");
        }

        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getAuditScope())){
        	sb.append(", AUDITSCOPE = '"+leaveAudit2LEntity.getAuditScope()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectType())){
        	sb.append(", PROJECTTYPE = '"+leaveAudit2LEntity.getProjectType()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getRemarks())){
        	sb.append(", REMARKS = '"+leaveAudit2LEntity.getRemarks()+"'");
        }


        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+leaveAudit2LEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+leaveAudit2LEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+leaveAudit2LEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+leaveAudit2LEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+leaveAudit2LEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+leaveAudit2LEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+leaveAudit2LEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+leaveAudit2LEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+leaveAudit2LEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+leaveAudit2LEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+leaveAudit2LEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+leaveAudit2LEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+leaveAudit2LEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+leaveAudit2LEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+leaveAudit2LEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+leaveAudit2LEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+leaveAudit2LEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+leaveAudit2LEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+leaveAudit2LEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+leaveAudit2LEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+leaveAudit2LEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+leaveAudit2LEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+leaveAudit2LEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+leaveAudit2LEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+leaveAudit2LEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+leaveAudit2LEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+leaveAudit2LEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+leaveAudit2LEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+leaveAudit2LEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+leaveAudit2LEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(leaveAudit2LEntity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(leaveAudit2LEntity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(leaveAudit2LEntity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+leaveAudit2LEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+leaveAudit2LEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+leaveAudit2LEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+leaveAudit2LEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+leaveAudit2LEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+leaveAudit2LEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+leaveAudit2LEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+leaveAudit2LEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+leaveAudit2LEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+leaveAudit2LEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(leaveAudit2LEntity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+leaveAudit2LEntity.getReservedNum1());
        }
        if(leaveAudit2LEntity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+leaveAudit2LEntity.getReservedNum2());
        }
        if(leaveAudit2LEntity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+leaveAudit2LEntity.getReservedNum3());
        }
        if(leaveAudit2LEntity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+leaveAudit2LEntity.getReservedNum4());
        }
        if(leaveAudit2LEntity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+leaveAudit2LEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(leaveAudit2LEntity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+leaveAudit2LEntity.getStaffid1());
        }
        if(leaveAudit2LEntity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+leaveAudit2LEntity.getStaffid2());
        }
        if(leaveAudit2LEntity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+leaveAudit2LEntity.getStaffid3());
        }
        if(leaveAudit2LEntity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+leaveAudit2LEntity.getStaffid4());
        }
        if(leaveAudit2LEntity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+leaveAudit2LEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+leaveAudit2LEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+leaveAudit2LEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+leaveAudit2LEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+leaveAudit2LEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+leaveAudit2LEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(leaveAudit2LEntity.getOrgid1() != null){
            sb.append(", ORGID1 = "+leaveAudit2LEntity.getOrgid1());
        }
        if(leaveAudit2LEntity.getOrgid2() != null){
            sb.append(", ORGID2 = "+leaveAudit2LEntity.getOrgid2());
        }
        if(leaveAudit2LEntity.getOrgid3() != null){
            sb.append(", ORGID3 = "+leaveAudit2LEntity.getOrgid3());
        }
        if(leaveAudit2LEntity.getOrgid4() != null){
            sb.append(", ORGID4 = "+leaveAudit2LEntity.getOrgid4());
        }
        if(leaveAudit2LEntity.getOrgid5() != null){
            sb.append(", ORGID5 = "+leaveAudit2LEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+leaveAudit2LEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+leaveAudit2LEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+leaveAudit2LEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+leaveAudit2LEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+leaveAudit2LEntity.getOrgids5()+"'");
        }

        sb.append(" WHERE ID = '"+leaveAudit2LEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(LeaveAudit2LEntity leaveAudit2LEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_LEAVE_AUDIT_2L (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (").append(RandomUtil.uuBigDecimalId());

        if(leaveAudit2LEntity.getLeaveNo() != null){
            colSb.append(", LEAVENO");
            valSb.append(", '"+ leaveAudit2LEntity.getLeaveNo()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '"+ leaveAudit2LEntity.getProjectName()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getRemarks())){
        	colSb.append(", REMARKS");
            valSb.append(", '"+ leaveAudit2LEntity.getRemarks()+"'");
        }

        if(StringUtil.isNotEmpty(  leaveAudit2LEntity.getAuditOrgId() )){
            colSb.append(", AUDIT_ORG");
            valSb.append(", '"+ leaveAudit2LEntity.getAuditOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getEntrustNo())){
            colSb.append(", ENTRUST_NO");
            valSb.append(", '"+ leaveAudit2LEntity.getEntrustNo()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getAuditScope())){
            colSb.append(", AUDITSCOPE");
            valSb.append(", '"+ leaveAudit2LEntity.getAuditScope()+"'");
        }
        
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getProjectType())){
            colSb.append(", PROJECTTYPE");
            valSb.append(", '"+ leaveAudit2LEntity.getProjectType()+"'");
        }

        if(leaveAudit2LEntity.getEntrustTime() != null){
            colSb.append(", ENTRUST_TIME");
            valSb.append(", '"+DateUtil.format(leaveAudit2LEntity.getEntrustTime() ,"yyyy-MM-dd")+"'");
        }

        if(leaveAudit2LEntity.getAuditStartTime() != null){
            colSb.append(", AUDIT_START_TIME");
            valSb.append(", '"+DateUtil.format(leaveAudit2LEntity.getAuditStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(leaveAudit2LEntity.getAuditEndTime() != null){
            colSb.append(", AUDIT_END_TIME");
            valSb.append(", '"+DateUtil.format(leaveAudit2LEntity.getAuditEndTime() ,"yyyy-MM-dd")+"'");
        }


        if(leaveAudit2LEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + leaveAudit2LEntity.getCreateUser().getStaffid() + "'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+leaveAudit2LEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+leaveAudit2LEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(leaveAudit2LEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(leaveAudit2LEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(leaveAudit2LEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(leaveAudit2LEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(leaveAudit2LEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(leaveAudit2LEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(leaveAudit2LEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+leaveAudit2LEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(leaveAudit2LEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+leaveAudit2LEntity.getReservedNum1());
        }
        if(leaveAudit2LEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+leaveAudit2LEntity.getReservedNum2());
        }
        if(leaveAudit2LEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+leaveAudit2LEntity.getReservedNum3());
        }
        if(leaveAudit2LEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+leaveAudit2LEntity.getReservedNum4());
        }
        if(leaveAudit2LEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+leaveAudit2LEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(leaveAudit2LEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+leaveAudit2LEntity.getStaffid1());
        }
        if(leaveAudit2LEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+leaveAudit2LEntity.getStaffid2());
        }
        if(leaveAudit2LEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+leaveAudit2LEntity.getStaffid3());
        }
        if(leaveAudit2LEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+leaveAudit2LEntity.getStaffid4());
        }
        if(leaveAudit2LEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+leaveAudit2LEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+leaveAudit2LEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+leaveAudit2LEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+leaveAudit2LEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+leaveAudit2LEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+leaveAudit2LEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(leaveAudit2LEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+leaveAudit2LEntity.getOrgid1());
        }
        if(leaveAudit2LEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+leaveAudit2LEntity.getOrgid2());
        }
        if(leaveAudit2LEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+leaveAudit2LEntity.getOrgid3());
        }
        if(leaveAudit2LEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+leaveAudit2LEntity.getOrgid4());
        }
        if(leaveAudit2LEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+leaveAudit2LEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+leaveAudit2LEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+leaveAudit2LEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+leaveAudit2LEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+leaveAudit2LEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(leaveAudit2LEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+leaveAudit2LEntity.getOrgids5()+"'");
        }

 
        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_LEAVE_AUDIT_2L WHERE ID IN (" + ids+")");
        return sb.toString();
    }
    public String findByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("select l.*,ORG.ORGNAME from   TBL_YQNS_LEAVE_AUDIT_2L  l LEFT JOIN TBL_ORGANIZATION org on l.AUDIT_ORG=ORG.ORGID WHERE ID IN (" + ids+")");
        return sb.toString();
    }


}
