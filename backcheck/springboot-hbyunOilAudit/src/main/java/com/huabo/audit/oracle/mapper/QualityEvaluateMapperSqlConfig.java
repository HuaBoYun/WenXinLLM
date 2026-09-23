package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.QualityEvaluateEntity;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName QualityEvaluateMapperSqlConfig
 * @Description
 * @DATE 2023/10/9
 */
public class QualityEvaluateMapperSqlConfig {

    public String selectByEntity( QualityEvaluateEntity qualityEvaluateEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUALITY_EVALUATE RS WHERE 1=1 ");

        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+qualityEvaluateEntity.getName()+"%'");
        }

        if(qualityEvaluateEntity.getStatus() != null){
            sb.append("AND RS.STATUS = '"+qualityEvaluateEntity.getStatus()+"'");
        }

//        sb.append(" ORDER BY RS.STATUS ASC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        sb.append(" ORDER BY RS.STATUS ASC");
        return sb.toString();
    }

    public String selectCountByEntity(QualityEvaluateEntity qualityEvaluateEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUALITY_EVALUATE RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+qualityEvaluateEntity.getName()+"%'");
        }

        if(qualityEvaluateEntity.getStatus() != null){
            sb.append("AND RS.STATUS = '"+qualityEvaluateEntity.getStatus()+"'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(QualityEvaluateEntity qualityEvaluateEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_QUALITY_EVALUATE SET ");
        sb.append("NAME = '"+qualityEvaluateEntity.getName()+"'");

        if(qualityEvaluateEntity.getStatus() != null){
            sb.append(", STATUS = '"+qualityEvaluateEntity.getStatus()+"'");
        }

        if(StringUtils.isNotBlank(qualityEvaluateEntity.getNo())) {
        	sb.append(", NO = '"+qualityEvaluateEntity.getNo()+"'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+qualityEvaluateEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+qualityEvaluateEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+qualityEvaluateEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+qualityEvaluateEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+qualityEvaluateEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+qualityEvaluateEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+qualityEvaluateEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+qualityEvaluateEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+qualityEvaluateEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+qualityEvaluateEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+qualityEvaluateEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+qualityEvaluateEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+qualityEvaluateEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+qualityEvaluateEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+qualityEvaluateEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+qualityEvaluateEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+qualityEvaluateEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+qualityEvaluateEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+qualityEvaluateEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+qualityEvaluateEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+qualityEvaluateEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+qualityEvaluateEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+qualityEvaluateEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+qualityEvaluateEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+qualityEvaluateEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+qualityEvaluateEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+qualityEvaluateEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+qualityEvaluateEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+qualityEvaluateEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+qualityEvaluateEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(qualityEvaluateEntity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(qualityEvaluateEntity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(qualityEvaluateEntity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+qualityEvaluateEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+qualityEvaluateEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+qualityEvaluateEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+qualityEvaluateEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+qualityEvaluateEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+qualityEvaluateEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+qualityEvaluateEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+qualityEvaluateEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+qualityEvaluateEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+qualityEvaluateEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(qualityEvaluateEntity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+qualityEvaluateEntity.getReservedNum1());
        }
        if(qualityEvaluateEntity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+qualityEvaluateEntity.getReservedNum2());
        }
        if(qualityEvaluateEntity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+qualityEvaluateEntity.getReservedNum3());
        }
        if(qualityEvaluateEntity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+qualityEvaluateEntity.getReservedNum4());
        }
        if(qualityEvaluateEntity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+qualityEvaluateEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(qualityEvaluateEntity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+qualityEvaluateEntity.getStaffid1());
        }
        if(qualityEvaluateEntity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+qualityEvaluateEntity.getStaffid2());
        }
        if(qualityEvaluateEntity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+qualityEvaluateEntity.getStaffid3());
        }
        if(qualityEvaluateEntity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+qualityEvaluateEntity.getStaffid4());
        }
        if(qualityEvaluateEntity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+qualityEvaluateEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+qualityEvaluateEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+qualityEvaluateEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+qualityEvaluateEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+qualityEvaluateEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+qualityEvaluateEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(qualityEvaluateEntity.getOrgid1() != null){
            sb.append(", ORGID1 = "+qualityEvaluateEntity.getOrgid1());
        }
        if(qualityEvaluateEntity.getOrgid2() != null){
            sb.append(", ORGID2 = "+qualityEvaluateEntity.getOrgid2());
        }
        if(qualityEvaluateEntity.getOrgid3() != null){
            sb.append(", ORGID3 = "+qualityEvaluateEntity.getOrgid3());
        }
        if(qualityEvaluateEntity.getOrgid4() != null){
            sb.append(", ORGID4 = "+qualityEvaluateEntity.getOrgid4());
        }
        if(qualityEvaluateEntity.getOrgid5() != null){
            sb.append(", ORGID5 = "+qualityEvaluateEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+qualityEvaluateEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+qualityEvaluateEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+qualityEvaluateEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+qualityEvaluateEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+qualityEvaluateEntity.getOrgids5()+"'");
        }

        sb.append(" WHERE ID = '"+qualityEvaluateEntity.getId()+"'");

        return sb.toString();
    }

    public String updateEntityStatus(QualityEvaluateEntity qualityEvaluateEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_QUALITY_EVALUATE SET ");
        sb.append("STATUS = '"+qualityEvaluateEntity.getStatus()+"'");

        
        sb.append(" WHERE ID = '"+qualityEvaluateEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(QualityEvaluateEntity qualityEvaluateEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_QUALITY_EVALUATE (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getName())){
            colSb.append(", NAME");
            valSb.append(", '" + qualityEvaluateEntity.getName() + "'");
        }

        if(qualityEvaluateEntity.getStatus() != null){
            colSb.append(", STATUS");
            valSb.append(", '" + qualityEvaluateEntity.getStatus() + "'");
        }
        
        if(StringUtils.isNotBlank(qualityEvaluateEntity.getNo())) {
        	colSb.append(", NO");
            valSb.append(", '" + qualityEvaluateEntity.getNo() + "'");
        }

        if(qualityEvaluateEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + qualityEvaluateEntity.getCreateUser().getStaffid() + "'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+qualityEvaluateEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+qualityEvaluateEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(qualityEvaluateEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(qualityEvaluateEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(qualityEvaluateEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(qualityEvaluateEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(qualityEvaluateEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(qualityEvaluateEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(qualityEvaluateEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+qualityEvaluateEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(qualityEvaluateEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+qualityEvaluateEntity.getReservedNum1());
        }
        if(qualityEvaluateEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+qualityEvaluateEntity.getReservedNum2());
        }
        if(qualityEvaluateEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+qualityEvaluateEntity.getReservedNum3());
        }
        if(qualityEvaluateEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+qualityEvaluateEntity.getReservedNum4());
        }
        if(qualityEvaluateEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+qualityEvaluateEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(qualityEvaluateEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+qualityEvaluateEntity.getStaffid1());
        }
        if(qualityEvaluateEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+qualityEvaluateEntity.getStaffid2());
        }
        if(qualityEvaluateEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+qualityEvaluateEntity.getStaffid3());
        }
        if(qualityEvaluateEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+qualityEvaluateEntity.getStaffid4());
        }
        if(qualityEvaluateEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+qualityEvaluateEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+qualityEvaluateEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+qualityEvaluateEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+qualityEvaluateEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+qualityEvaluateEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+qualityEvaluateEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(qualityEvaluateEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+qualityEvaluateEntity.getOrgid1());
        }
        if(qualityEvaluateEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+qualityEvaluateEntity.getOrgid2());
        }
        if(qualityEvaluateEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+qualityEvaluateEntity.getOrgid3());
        }
        if(qualityEvaluateEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+qualityEvaluateEntity.getOrgid4());
        }
        if(qualityEvaluateEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+qualityEvaluateEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+qualityEvaluateEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+qualityEvaluateEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+qualityEvaluateEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+qualityEvaluateEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(qualityEvaluateEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+qualityEvaluateEntity.getOrgids5()+"'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_QUALITY_EVALUATE WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertQualityEvaluateItem(BigDecimal qeId, BigDecimal smId ){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_QUALITY_EVALUATE_ITEM(QEID,SMID) VALUES ("+ qeId + ","+ smId +")");
        return sb.toString();
    }

    public String deleteQualityEvaluateItemsById(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_QUALITY_EVALUATE_ITEM WHERE QEID IN ("+ids+")");
        return sb.toString();
    }
}
