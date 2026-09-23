package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.ExpectLeaveEntity;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
import com.huabo.audit.oracle.entity.TblStaff;
import org.apache.commons.lang.StringUtils;

/**
 * @author Rui
 * @ClassName ExpectLeaveMapperSqlConfig
 * @Description
 * @DATE 2023/9/14
 */
public class ExpectLeaveMapperSqlConfig {

    public String selectByEntity( ExpectLeaveEntity expectLeaveEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_EXPECT_LEAVE RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(StringUtil.isNotEmpty(expectLeaveEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+expectLeaveEntity.getName()+"%'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getIds())){
            sb.append("AND RS.ID IN ( "+expectLeaveEntity.getIds()+")");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+expectLeaveEntity.getProjectName()+"%'");
        }

        //用户创建只能看见自己的。部门负责人要查看全部内容；下发人员可以查询下发给自己的
        sb.append(" AND (RS.CREATE_USER = "+ expectLeaveEntity.getCreateUserId() );
        sb.append(" OR INSTR (',' || PERSON_IDS || ',',',"+expectLeaveEntity.getCreateUserId()+",') > 0" );
        if(StringUtils.isNotBlank(expectLeaveEntity.getQueryDeptIds())) {
            sb.append(" OR RS.CREATE_USER IN (SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(expectLeaveEntity.getQueryDeptIds()).append("))");
        }
        sb.append(") ");

        if(expectLeaveEntity.getTeamLeader() != null && StringUtil.isNotEmpty(expectLeaveEntity.getTeamLeader().getUsername())){
            sb.append("AND RS.TEAM_LEADER_ID IN (SELECT STAFFID FROM TBL_STAFF WHERE REALNAME LIKE '%"+expectLeaveEntity.getTeamLeader().getRealname()+"%')");
        }
        sb.append(" ORDER BY RS.CREATE_TIME DESC ");
        System.out.println(sb.toString());
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(ExpectLeaveEntity expectLeaveEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_EXPECT_LEAVE RS LEFT JOIN TBL_STAFF TS ON RS.CREATE_USER = TS.STAFFID WHERE 1=1 ");
        if(StringUtil.isNotEmpty(expectLeaveEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+expectLeaveEntity.getName()+"%'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+expectLeaveEntity.getProjectName()+"%'");
        }

        if(expectLeaveEntity.getTeamLeader() != null && StringUtil.isNotEmpty(expectLeaveEntity.getTeamLeader().getUsername())){
            sb.append("AND RS.TEAM_LEADER_ID IN (SELECT STAFFID FROM TBL_STAFF WHERE REALNAME LIKE '%"+expectLeaveEntity.getTeamLeader().getRealname()+"%')");
        }
        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(ExpectLeaveEntity expectLeaveEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_EXPECT_LEAVE SET ");
        sb.append("NAME = '"+expectLeaveEntity.getName()+"'");

        if(expectLeaveEntity.getRetireTime() != null){
            sb.append(", RETIRE_TIME = '"+DateUtil.format(expectLeaveEntity.getRetireTime(),"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getAuditTime() != null){
            sb.append(", AUDIT_TIME = '"+DateUtil.format(expectLeaveEntity.getAuditTime(),"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+expectLeaveEntity.getProjectName()+"'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getNo())){
            sb.append(", NO = '"+expectLeaveEntity.getNo()+"'");
        }
        
        if(expectLeaveEntity.getWorkStartTime() != null){
            sb.append(", WORK_START_TIME = '"+DateUtil.format(expectLeaveEntity.getWorkStartTime(),"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getWorkEndTime() != null){
            sb.append(", WORK_END_TIME = '"+DateUtil.format(expectLeaveEntity.getWorkEndTime(),"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getDoAuditTime() != null){
            sb.append(", DO_AUDIT_TIME = '"+DateUtil.format(expectLeaveEntity.getDoAuditTime(),"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getTeamLeaderId() )){
            sb.append(", TEAM_LEADER_ID = '"+expectLeaveEntity.getTeamLeaderId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getTeamLeaderName() )){
            sb.append(", TEAM_LEADER_NAME = '"+expectLeaveEntity.getTeamLeaderName()+"'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getPersonIds())){
            sb.append(", PERSON_IDS = '"+expectLeaveEntity.getPersonIds()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getLeaderId() )){
            sb.append(", LEADER_ID = '"+expectLeaveEntity.getLeaderId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getLeaderName() )){
            sb.append(", LEADER_NAME = '"+expectLeaveEntity.getLeaderName()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getChiefReviewerId() )){
            sb.append(", CHIEF_REVIEWER_ID = '"+expectLeaveEntity.getChiefReviewerId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getChiefReviewerName() )){
            sb.append(", CHIEF_REVIEWER_NAME = '"+expectLeaveEntity.getChiefReviewerName()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getDeputyReviewerName() )){
            sb.append(", DEPUTY_REVIEWER_NAME = '"+expectLeaveEntity.getDeputyReviewerName()+"'");
        }

        if(expectLeaveEntity.getUnitId() != null){
            sb.append(", UNITID = '"+expectLeaveEntity.getUnitId()+"'");
        }

// 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+expectLeaveEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+expectLeaveEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+expectLeaveEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+expectLeaveEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+expectLeaveEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+expectLeaveEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+expectLeaveEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+expectLeaveEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+expectLeaveEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+expectLeaveEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+expectLeaveEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+expectLeaveEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+expectLeaveEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+expectLeaveEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+expectLeaveEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+expectLeaveEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+expectLeaveEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+expectLeaveEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+expectLeaveEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+expectLeaveEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+expectLeaveEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+expectLeaveEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+expectLeaveEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+expectLeaveEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+expectLeaveEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+expectLeaveEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+expectLeaveEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+expectLeaveEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+expectLeaveEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+expectLeaveEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(expectLeaveEntity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(expectLeaveEntity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(expectLeaveEntity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+expectLeaveEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+expectLeaveEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+expectLeaveEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+expectLeaveEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+expectLeaveEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+expectLeaveEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+expectLeaveEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+expectLeaveEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+expectLeaveEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+expectLeaveEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(expectLeaveEntity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+expectLeaveEntity.getReservedNum1());
        }
        if(expectLeaveEntity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+expectLeaveEntity.getReservedNum2());
        }
        if(expectLeaveEntity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+expectLeaveEntity.getReservedNum3());
        }
        if(expectLeaveEntity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+expectLeaveEntity.getReservedNum4());
        }
        if(expectLeaveEntity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+expectLeaveEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(expectLeaveEntity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+expectLeaveEntity.getStaffid1());
        }
        if(expectLeaveEntity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+expectLeaveEntity.getStaffid2());
        }
        if(expectLeaveEntity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+expectLeaveEntity.getStaffid3());
        }
        if(expectLeaveEntity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+expectLeaveEntity.getStaffid4());
        }
        if(expectLeaveEntity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+expectLeaveEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+expectLeaveEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+expectLeaveEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+expectLeaveEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+expectLeaveEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+expectLeaveEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(expectLeaveEntity.getOrgid1() != null){
            sb.append(", ORGID1 = "+expectLeaveEntity.getOrgid1());
        }
        if(expectLeaveEntity.getOrgid2() != null){
            sb.append(", ORGID2 = "+expectLeaveEntity.getOrgid2());
        }
        if(expectLeaveEntity.getOrgid3() != null){
            sb.append(", ORGID3 = "+expectLeaveEntity.getOrgid3());
        }
        if(expectLeaveEntity.getOrgid4() != null){
            sb.append(", ORGID4 = "+expectLeaveEntity.getOrgid4());
        }
        if(expectLeaveEntity.getOrgid5() != null){
            sb.append(", ORGID5 = "+expectLeaveEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+expectLeaveEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+expectLeaveEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+expectLeaveEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+expectLeaveEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+expectLeaveEntity.getOrgids5()+"'");
        }


        sb.append(" WHERE ID = '"+expectLeaveEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ExpectLeaveEntity expectLeaveEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_EXPECT_LEAVE (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(expectLeaveEntity.getName())){
            colSb.append(", NAME");
            valSb.append(", '"+ expectLeaveEntity.getName()+"'");
        }

        if(expectLeaveEntity.getRetireTime() != null){
            colSb.append(", RETIRE_TIME");
            valSb.append(", '"+DateUtil.format(expectLeaveEntity.getRetireTime() ,"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getAuditTime() != null){
            colSb.append(", AUDIT_TIME");
            valSb.append(", '"+DateUtil.format(expectLeaveEntity.getAuditTime() ,"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '"+ expectLeaveEntity.getProjectName()+"'");
        }

        if(StringUtil.isNotEmpty(expectLeaveEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '"+ expectLeaveEntity.getNo()+"'");
        }
        
        if(expectLeaveEntity.getWorkStartTime() != null){
            colSb.append(", WORK_START_TIME");
            valSb.append(", '"+DateUtil.format(expectLeaveEntity.getWorkStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getWorkEndTime() != null){
            colSb.append(", WORK_END_TIME");
            valSb.append(", '"+DateUtil.format(expectLeaveEntity.getWorkEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(expectLeaveEntity.getDoAuditTime() != null){
            colSb.append(", DO_AUDIT_TIME");
            valSb.append(", '"+DateUtil.format(expectLeaveEntity.getDoAuditTime() ,"yyyy-MM-dd")+"'");
        }


        if(StringUtil.isNotEmpty(  expectLeaveEntity.getTeamLeaderId() )){
            colSb.append(", TEAM_LEADER_ID");
            valSb.append(", '"+ expectLeaveEntity.getTeamLeaderId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getTeamLeaderName() )){
            colSb.append(", TEAM_LEADER_NAME");
            valSb.append(", '"+ expectLeaveEntity.getTeamLeaderName()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getLeaderId() )){
            colSb.append(", LEADER_ID");
            valSb.append(", '"+ expectLeaveEntity.getLeaderId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getLeaderName() )){
            colSb.append(", LEADER_NAME");
            valSb.append(", '"+ expectLeaveEntity.getLeaderName()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getChiefReviewerId() )){
            colSb.append(", CHIEF_REVIEWER_ID");
            valSb.append(", '"+ expectLeaveEntity.getChiefReviewerId()+"'");
        }
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getChiefReviewerName() )){
            colSb.append(", CHIEF_REVIEWER_NAME");
            valSb.append(", '"+ expectLeaveEntity.getChiefReviewerName()+"'");
        }

        if(StringUtil.isNotEmpty(  expectLeaveEntity.getDeputyReviewerName() )){
            colSb.append(", DEPUTY_REVIEWER_NAME");
            valSb.append(", '"+ expectLeaveEntity.getDeputyReviewerName()+"'");
        }
        
        if(StringUtil.isNotEmpty(  expectLeaveEntity.getDeputyReviewerId() )){
            colSb.append(", DEPUTY_REVIEWER_ID");
            valSb.append(", '"+ expectLeaveEntity.getDeputyReviewerId()+"'");
        }


        if(expectLeaveEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + expectLeaveEntity.getCreateUser().getStaffid() + "'");
        }

        if(expectLeaveEntity.getUnitId() != null){
            colSb.append(", UNITID");
            valSb.append(", '"+expectLeaveEntity.getUnitId()+"'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+expectLeaveEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+expectLeaveEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+expectLeaveEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+expectLeaveEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+expectLeaveEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+expectLeaveEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+expectLeaveEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+expectLeaveEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+expectLeaveEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+expectLeaveEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+expectLeaveEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+expectLeaveEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+expectLeaveEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+expectLeaveEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+expectLeaveEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+expectLeaveEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+expectLeaveEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+expectLeaveEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+expectLeaveEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+expectLeaveEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+expectLeaveEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+expectLeaveEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+expectLeaveEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+expectLeaveEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+expectLeaveEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(expectLeaveEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(expectLeaveEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(expectLeaveEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(expectLeaveEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(expectLeaveEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(expectLeaveEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(expectLeaveEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+expectLeaveEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+expectLeaveEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+expectLeaveEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+expectLeaveEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+expectLeaveEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+expectLeaveEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(expectLeaveEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+expectLeaveEntity.getReservedNum1());
        }
        if(expectLeaveEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+expectLeaveEntity.getReservedNum2());
        }
        if(expectLeaveEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+expectLeaveEntity.getReservedNum3());
        }
        if(expectLeaveEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+expectLeaveEntity.getReservedNum4());
        }
        if(expectLeaveEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+expectLeaveEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(expectLeaveEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+expectLeaveEntity.getStaffid1());
        }
        if(expectLeaveEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+expectLeaveEntity.getStaffid2());
        }
        if(expectLeaveEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+expectLeaveEntity.getStaffid3());
        }
        if(expectLeaveEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+expectLeaveEntity.getStaffid4());
        }
        if(expectLeaveEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+expectLeaveEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+expectLeaveEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+expectLeaveEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+expectLeaveEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+expectLeaveEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+expectLeaveEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(expectLeaveEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+expectLeaveEntity.getOrgid1());
        }
        if(expectLeaveEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+expectLeaveEntity.getOrgid2());
        }
        if(expectLeaveEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+expectLeaveEntity.getOrgid3());
        }
        if(expectLeaveEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+expectLeaveEntity.getOrgid4());
        }
        if(expectLeaveEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+expectLeaveEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+expectLeaveEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+expectLeaveEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+expectLeaveEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+expectLeaveEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(expectLeaveEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+expectLeaveEntity.getOrgids5()+"'");
        }


        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_EXPECT_LEAVE WHERE ID IN (" + ids+")");
        return sb.toString();
    }
}
