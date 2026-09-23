package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.StringUtil;
import com.hbfk.util.redis.Random.RandomUtil;
import com.huabo.audit.oracle.entity.ProjectEvaluationEntity;
import com.huabo.audit.oracle.entity.ProjectEvaluationItemEntity;
import com.huabo.audit.oracle.entity.QualityEntity;
import com.huabo.audit.oracle.entity.QualityItemEntity;

import java.math.BigDecimal;

/**
 * @author zkl
 * @InterfaceName ProjectEvaluationMapperSqlConfig
 * @Description
 * @DATE 2024/04/13
 */
public class ProjectEvaluationMapperSqlConfig {

    public String selectByEntity( ProjectEvaluationEntity entity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT * FROM TBL_PROJECT_EVALUATION RS WHERE 1=1  ");

        if(StringUtil.isNotEmpty(entity.getProjectName())){
            sb.append("AND RS.PROJECT_ID IN (SELECT ID FROM TBL_YQNS_IMPLEMENT_PLAN WHERE PRJOECTNAME LIKE '%"+entity.getProjectName()+"%')");
        }
        return sb.toString();
    }

    public String updateEntity(ProjectEvaluationEntity entity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_PROJECT_EVALUATION SET ");
        sb.append("PROJECT_ID = '"+entity.getProjectId()+"'");

        if(StringUtil.isNotEmpty(  entity.getOrgId() )){
            sb.append(", ORG_ID = '" + entity.getOrgId()+"'");
        }

        if(entity.getStartTime() != null){
            sb.append(", START_TIME = '"+ DateUtil.format(entity.getStartTime(),"yyyy-MM-dd")+"'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(entity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+entity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+entity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+entity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+entity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+entity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+entity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+entity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+entity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+entity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+entity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(entity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+entity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+entity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+entity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+entity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+entity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+entity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+entity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+entity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+entity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+entity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(entity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+entity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+entity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+entity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+entity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+entity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(entity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+entity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+entity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+entity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+entity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+entity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(entity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(entity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(entity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(entity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(entity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(entity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(entity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(entity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(entity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(entity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(entity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(entity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(entity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(entity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(entity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(entity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(entity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(entity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(entity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(entity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(entity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(entity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(entity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(entity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(entity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(entity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(entity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(entity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(entity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(entity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(entity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+entity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+entity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+entity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+entity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+entity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(entity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+entity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+entity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+entity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+entity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+entity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(entity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+entity.getReservedNum1());
        }
        if(entity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+entity.getReservedNum2());
        }
        if(entity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+entity.getReservedNum3());
        }
        if(entity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+entity.getReservedNum4());
        }
        if(entity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+entity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(entity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+entity.getStaffid1());
        }
        if(entity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+entity.getStaffid2());
        }
        if(entity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+entity.getStaffid3());
        }
        if(entity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+entity.getStaffid4());
        }
        if(entity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+entity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(entity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+entity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+entity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+entity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+entity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+entity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(entity.getOrgid1() != null){
            sb.append(", ORGID1 = "+entity.getOrgid1());
        }
        if(entity.getOrgid2() != null){
            sb.append(", ORGID2 = "+entity.getOrgid2());
        }
        if(entity.getOrgid3() != null){
            sb.append(", ORGID3 = "+entity.getOrgid3());
        }
        if(entity.getOrgid4() != null){
            sb.append(", ORGID4 = "+entity.getOrgid4());
        }
        if(entity.getOrgid5() != null){
            sb.append(", ORGID5 = "+entity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(entity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+entity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+entity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+entity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+entity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(entity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+entity.getOrgids5()+"'");
        }

        sb.append(" WHERE ID = '"+entity.getId()+"'");

        return sb.toString();
    }


    public String insertEntity(ProjectEvaluationEntity qualityEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_PROJECT_EVALUATION (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if( StringUtil.isNotEmpty(qualityEntity.getProjectId())){
            colSb.append(", PROJECT_ID");
            valSb.append(", '" + qualityEntity.getProjectId() + "'");
        }
        if(StringUtil.isNotEmpty(  qualityEntity.getCreateUser() )){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + qualityEntity.getCreateUser() + "'");
        }
        if(StringUtil.isNotEmpty(  qualityEntity.getOrgId() )){
            colSb.append(", ORG_ID");
            valSb.append(", '" + qualityEntity.getOrgId() + "'");
        }

        if(qualityEntity.getStartTime() != null){
            colSb.append(", START_TIME");
            valSb.append(", '"+DateUtil.format(qualityEntity.getStartTime() ,"yyyy-MM-dd")+"'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+qualityEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+qualityEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+qualityEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+qualityEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+qualityEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+qualityEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+qualityEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+qualityEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+qualityEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+qualityEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+qualityEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+qualityEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+qualityEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+qualityEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+qualityEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+qualityEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+qualityEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+qualityEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+qualityEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+qualityEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+qualityEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+qualityEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+qualityEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+qualityEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+qualityEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+qualityEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+qualityEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+qualityEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+qualityEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+qualityEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(qualityEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(qualityEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(qualityEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(qualityEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(qualityEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(qualityEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(qualityEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+qualityEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+qualityEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+qualityEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+qualityEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+qualityEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+qualityEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+qualityEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+qualityEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+qualityEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+qualityEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(qualityEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+qualityEntity.getReservedNum1());
        }
        if(qualityEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+qualityEntity.getReservedNum2());
        }
        if(qualityEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+qualityEntity.getReservedNum3());
        }
        if(qualityEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+qualityEntity.getReservedNum4());
        }
        if(qualityEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+qualityEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(qualityEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+qualityEntity.getStaffid1());
        }
        if(qualityEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+qualityEntity.getStaffid2());
        }
        if(qualityEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+qualityEntity.getStaffid3());
        }
        if(qualityEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+qualityEntity.getStaffid4());
        }
        if(qualityEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+qualityEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(qualityEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+qualityEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+qualityEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+qualityEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+qualityEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+qualityEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(qualityEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+qualityEntity.getOrgid1());
        }
        if(qualityEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+qualityEntity.getOrgid2());
        }
        if(qualityEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+qualityEntity.getOrgid3());
        }
        if(qualityEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+qualityEntity.getOrgid4());
        }
        if(qualityEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+qualityEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(qualityEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+qualityEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+qualityEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+qualityEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+qualityEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+qualityEntity.getOrgids5()+"'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_PROJECT_EVALUATION WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertQualityItem(BigDecimal qualityId, ProjectEvaluationItemEntity qualityItemEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_PROJECT_EVALUATION_ITEM (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(null != qualityItemEntity.getBaseScore()){
            colSb.append(", BASE_SCORE");
            valSb.append(", " + qualityItemEntity.getBaseScore());
        }

        if(qualityItemEntity.getUserId() != null){
            colSb.append(", USER_ID");
            valSb.append(", '" + qualityItemEntity.getUserId() + "'");
        }

        if(qualityItemEntity.getIncreaseScore() != null){
            colSb.append(", INCREASE_SCORE");
            valSb.append(", '" + qualityItemEntity.getIncreaseScore() + "'");
        }

        if(qualityItemEntity.getIncreaseScoreReason() != null){
            colSb.append(", INCREASE_SCORE_REASON");
            valSb.append(", '" + qualityItemEntity.getIncreaseScoreReason() + "'");
        }

        if(StringUtil.isNotEmpty(qualityItemEntity.getProjectRole())){
            colSb.append(", PROJECT_ROLE");
            valSb.append(", '" + qualityItemEntity.getProjectRole() + "'");
        }

        if(qualityId != null){
            colSb.append(", PROJECT_EVALUATION_ID");
            valSb.append(", '" + qualityId + "'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteQualityItemsById(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_PROJECT_EVALUATION_ITEM WHERE PROJECT_EVALUATION_ID IN ("+ids+")");
        return sb.toString();
    }
}
