package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.audit.oracle.entity.ScoreManageEntity;
import com.huabo.audit.oracle.entity.ScoreManageItemEntity;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName ScoreManageMapperSqlConfig
 * @Description
 * @DATE 2023/10/9
 */
public class ScoreManageMapperSqlConfig {

    public String selectByEntity( ScoreManageEntity scoreManageEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_SCORE_MANAGE RS WHERE 1=1 ");

        if(scoreManageEntity.getType() != null){
            sb.append("AND RS.TYPE = '"+scoreManageEntity.getType()+"'");
        }

        if(scoreManageEntity.getStatus() != null){
            sb.append("AND RS.STATUS = '"+scoreManageEntity.getStatus()+"'");
        }

//        sb.append(" ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(ScoreManageEntity scoreManageEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_SCORE_MANAGE RS  WHERE 1=1 ");


        if(scoreManageEntity.getType() != null){
            sb.append("AND RS.TYPE = '"+scoreManageEntity.getType()+"'");
        }

        if(scoreManageEntity.getStatus() != null){
            sb.append("AND RS.STATUS = '"+scoreManageEntity.getStatus()+"'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(ScoreManageEntity scoreManageEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_SCORE_MANAGE SET ");
        sb.append("TYPE = '"+scoreManageEntity.getType()+"'");


        if(scoreManageEntity.getScore() != null){
            sb.append(", SCORE = '"+scoreManageEntity.getScore()+"'");
        }

        if(scoreManageEntity.getStatus() != null){
            sb.append(", STATUS = '"+scoreManageEntity.getStatus()+"'");
        }

        if(StringUtils.isNotBlank(scoreManageEntity.getNo())) {
        	sb.append(", NO = '"+scoreManageEntity.getNo()+"'");
        }

        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString1())){
            sb.append(", RESERVEDSTRING1 = '"+scoreManageEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString2())){
            sb.append(", RESERVEDSTRING2 = '"+scoreManageEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString3())){
            sb.append(", RESERVEDSTRING3 = '"+scoreManageEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString4())){
            sb.append(", RESERVEDSTRING4 = '"+scoreManageEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString5())){
            sb.append(", RESERVEDSTRING5 = '"+scoreManageEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString6())){
            sb.append(", RESERVEDSTRING6 = '"+scoreManageEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString7())){
            sb.append(", RESERVEDSTRING7 = '"+scoreManageEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString8())){
            sb.append(", RESERVEDSTRING8 = '"+scoreManageEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString9())){
            sb.append(", RESERVEDSTRING9 = '"+scoreManageEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString10())){
            sb.append(", RESERVEDSTRING10 = '"+scoreManageEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent1())){
            sb.append(", RESERVEDCONTENT1 = '"+scoreManageEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent2())){
            sb.append(", RESERVEDCONTENT2 = '"+scoreManageEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent3())){
            sb.append(", RESERVEDCONTENT3 = '"+scoreManageEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent4())){
            sb.append(", RESERVEDCONTENT4 = '"+scoreManageEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent5())){
            sb.append(", RESERVEDCONTENT5 = '"+scoreManageEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent6())){
            sb.append(", RESERVEDCONTENT6 = '"+scoreManageEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent7())){
            sb.append(", RESERVEDCONTENT7 = '"+scoreManageEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent8())){
            sb.append(", RESERVEDCONTENT8 = '"+scoreManageEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent9())){
            sb.append(", RESERVEDCONTENT9 = '"+scoreManageEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent10())){
            sb.append(", RESERVEDCONTENT10 = '"+scoreManageEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple1())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+scoreManageEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple2())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+scoreManageEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple3())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+scoreManageEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple4())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+scoreManageEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple5())){
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+scoreManageEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice1())){
            sb.append(", RESERVEDMULTIPLECHOICE1 = '"+scoreManageEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice2())){
            sb.append(", RESERVEDMULTIPLECHOICE2 = '"+scoreManageEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice3())){
            sb.append(", RESERVEDMULTIPLECHOICE3 = '"+scoreManageEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice4())){
            sb.append(", RESERVEDMULTIPLECHOICE4 = '"+scoreManageEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice5())){
            sb.append(", RESERVEDMULTIPLECHOICE5 = '"+scoreManageEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(scoreManageEntity.getReservedYearTime1() != null){
            sb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime2() != null){
            sb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime3() != null){
            sb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime4() != null){
            sb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime5() != null){
            sb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(scoreManageEntity.getReservedYearAccurateTime1() != null){
            sb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime2() != null){
            sb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime3() != null){
            sb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime4() != null){
            sb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime5() != null){
            sb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(scoreManageEntity.getReservedTime1() != null){
            sb.append(", RESERVEDTIME1 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime2() != null){
            sb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime3() != null){
            sb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime4() != null){
            sb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime5() != null){
            sb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice1())){
            sb.append(", RESERVEDSINGLECHOICE1 = '"+scoreManageEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice2())){
            sb.append(", RESERVEDSINGLECHOICE2 = '"+scoreManageEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice3())){
            sb.append(", RESERVEDSINGLECHOICE3 = '"+scoreManageEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice4())){
            sb.append(", RESERVEDSINGLECHOICE4 = '"+scoreManageEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice5())){
            sb.append(", RESERVEDSINGLECHOICE5 = '"+scoreManageEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice1())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+scoreManageEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice2())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+scoreManageEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice3())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+scoreManageEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice4())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+scoreManageEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice5())){
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+scoreManageEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(scoreManageEntity.getReservedNum1() != null){
            sb.append(", RESERVEDNUM1 = "+scoreManageEntity.getReservedNum1());
        }
        if(scoreManageEntity.getReservedNum2() != null){
            sb.append(", RESERVEDNUM2 = "+scoreManageEntity.getReservedNum2());
        }
        if(scoreManageEntity.getReservedNum3() != null){
            sb.append(", RESERVEDNUM3 = "+scoreManageEntity.getReservedNum3());
        }
        if(scoreManageEntity.getReservedNum4() != null){
            sb.append(", RESERVEDNUM4 = "+scoreManageEntity.getReservedNum4());
        }
        if(scoreManageEntity.getReservedNum5() != null){
            sb.append(", RESERVEDNUM5 = "+scoreManageEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(scoreManageEntity.getStaffid1() != null){
            sb.append(", STAFFID1 = "+scoreManageEntity.getStaffid1());
        }
        if(scoreManageEntity.getStaffid2() != null){
            sb.append(", STAFFID2 = "+scoreManageEntity.getStaffid2());
        }
        if(scoreManageEntity.getStaffid3() != null){
            sb.append(", STAFFID3 = "+scoreManageEntity.getStaffid3());
        }
        if(scoreManageEntity.getStaffid4() != null){
            sb.append(", STAFFID4 = "+scoreManageEntity.getStaffid4());
        }
        if(scoreManageEntity.getStaffid5() != null){
            sb.append(", STAFFID5 = "+scoreManageEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids1())){
            sb.append(", STAFFIDS1 = '"+scoreManageEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids2())){
            sb.append(", STAFFIDS2 = '"+scoreManageEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids3())){
            sb.append(", STAFFIDS3 = '"+scoreManageEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids4())){
            sb.append(", STAFFIDS4 = '"+scoreManageEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids5())){
            sb.append(", STAFFIDS5 = '"+scoreManageEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(scoreManageEntity.getOrgid1() != null){
            sb.append(", ORGID1 = "+scoreManageEntity.getOrgid1());
        }
        if(scoreManageEntity.getOrgid2() != null){
            sb.append(", ORGID2 = "+scoreManageEntity.getOrgid2());
        }
        if(scoreManageEntity.getOrgid3() != null){
            sb.append(", ORGID3 = "+scoreManageEntity.getOrgid3());
        }
        if(scoreManageEntity.getOrgid4() != null){
            sb.append(", ORGID4 = "+scoreManageEntity.getOrgid4());
        }
        if(scoreManageEntity.getOrgid5() != null){
            sb.append(", ORGID5 = "+scoreManageEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids1())){
            sb.append(", ORGIDS1 = '"+scoreManageEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids2())){
            sb.append(", ORGIDS2 = '"+scoreManageEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids3())){
            sb.append(", ORGIDS3 = '"+scoreManageEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids4())){
            sb.append(", ORGIDS4 = '"+scoreManageEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids5())){
            sb.append(", ORGIDS5 = '"+scoreManageEntity.getOrgids5()+"'");
        }


        sb.append(" WHERE ID = '"+scoreManageEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(ScoreManageEntity scoreManageEntity) throws Exception {
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_SCORE_MANAGE (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(scoreManageEntity.getType() != null){
            colSb.append(", TYPE");
            valSb.append(", " + scoreManageEntity.getType());
        }

        if(scoreManageEntity.getScore() != null){
            colSb.append(", SCORE");
            valSb.append(", '" + scoreManageEntity.getScore() + "'");
        }

        if(scoreManageEntity.getStatus() != null){
            colSb.append(", STATUS");
            valSb.append(", '" + scoreManageEntity.getStatus() + "'");
        }

        if(StringUtils.isNotBlank(scoreManageEntity.getNo())) {
        	colSb.append(", NO");
            valSb.append(", '" + scoreManageEntity.getNo() + "'");
        }
        
        if(scoreManageEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + scoreManageEntity.getCreateUser().getStaffid() + "'");
        }


        // 预留字符串（输入框）10个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString1())){
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '"+scoreManageEntity.getReservedString1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString2())){
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '"+scoreManageEntity.getReservedString2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString3())){
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '"+scoreManageEntity.getReservedString3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString4())){
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '"+scoreManageEntity.getReservedString4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString5())){
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '"+scoreManageEntity.getReservedString5()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString6())){
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '"+scoreManageEntity.getReservedString6()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString7())){
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '"+scoreManageEntity.getReservedString7()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString8())){
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '"+scoreManageEntity.getReservedString8()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString9())){
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '"+scoreManageEntity.getReservedString9()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedString10())){
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '"+scoreManageEntity.getReservedString10()+"'");
        }
        // 预留大文本（文本域）10个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent1())){
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '"+scoreManageEntity.getReservedContent1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent2())){
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '"+scoreManageEntity.getReservedContent2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent3())){
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '"+scoreManageEntity.getReservedContent3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent4())){
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '"+scoreManageEntity.getReservedContent4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent5())){
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '"+scoreManageEntity.getReservedContent5()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent6())){
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '"+scoreManageEntity.getReservedContent6()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent7())){
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '"+scoreManageEntity.getReservedContent7()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent8())){
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '"+scoreManageEntity.getReservedContent8()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent9())){
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '"+scoreManageEntity.getReservedContent9()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedContent10())){
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '"+scoreManageEntity.getReservedContent10()+"'");
        }
        // 预留下拉多选字符串（多选下拉）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple1())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownMultiple1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple2())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownMultiple2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple3())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownMultiple3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple4())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownMultiple4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownMultiple5())){
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownMultiple5()+"'");
        }

        // 预留多选字符串（多选框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice1())){
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '"+scoreManageEntity.getReservedMultipleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice2())){
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '"+scoreManageEntity.getReservedMultipleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice3())){
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '"+scoreManageEntity.getReservedMultipleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice4())){
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '"+scoreManageEntity.getReservedMultipleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedMultipleChoice5())){
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '"+scoreManageEntity.getReservedMultipleChoice5()+"'");
        }
        // 预留年份（年份）5个

        if(scoreManageEntity.getReservedYearTime1() != null){
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime1(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime2() != null){
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime2(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime3() != null){
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime3(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime4() != null){
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime4(),"yyyy")+"','YYYY')");
        }
        if(scoreManageEntity.getReservedYearTime5() != null){
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearTime5(),"yyyy")+"','YYYY')");
        }
        // 预留时间（日期（年月日时分秒））5个
        if(scoreManageEntity.getReservedYearAccurateTime1() != null){
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime2() != null){
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime3() != null){
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime4() != null){
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        if(scoreManageEntity.getReservedYearAccurateTime5() != null){
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
        }
        // 预留年月日（日期（年月日））5个
        if(scoreManageEntity.getReservedTime1() != null){
            colSb.append(", RESERVEDTIME1");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime2() != null){
            colSb.append(", RESERVEDTIME2");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime3() != null){
            colSb.append(", RESERVEDTIME3");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime4() != null){
            colSb.append(", RESERVEDTIME4");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        if(scoreManageEntity.getReservedTime5() != null){
            colSb.append(", RESERVEDTIME5");
            valSb.append(", TO_DATE('"+DateUtil.format(scoreManageEntity.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
        }
        // 预留单选字符串（单选框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice1())){
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '"+scoreManageEntity.getReservedSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice2())){
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '"+scoreManageEntity.getReservedSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice3())){
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '"+scoreManageEntity.getReservedSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice4())){
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '"+scoreManageEntity.getReservedSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedSingleChoice5())){
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '"+scoreManageEntity.getReservedSingleChoice5()+"'");
        }
        // 预留下拉单选字符串（单选下拉框）5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice1())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownSingleChoice1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice2())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownSingleChoice2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice3())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownSingleChoice3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice4())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownSingleChoice4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getReservedDropdownSingleChoice5())){
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '"+scoreManageEntity.getReservedDropdownSingleChoice5()+"'");
        }
        // 预留数字（数字输入框）5个
        if(scoreManageEntity.getReservedNum1() != null){
            colSb.append(", RESERVEDNUM1");
            valSb.append(", "+scoreManageEntity.getReservedNum1());
        }
        if(scoreManageEntity.getReservedNum2() != null){
            colSb.append(", RESERVEDNUM2");
            valSb.append(", "+scoreManageEntity.getReservedNum2());
        }
        if(scoreManageEntity.getReservedNum3() != null){
            colSb.append(", RESERVEDNUM3");
            valSb.append(", "+scoreManageEntity.getReservedNum3());
        }
        if(scoreManageEntity.getReservedNum4() != null){
            colSb.append(", RESERVEDNUM4");
            valSb.append(", "+scoreManageEntity.getReservedNum4());
        }
        if(scoreManageEntity.getReservedNum5() != null){
            colSb.append(", RESERVEDNUM5");
            valSb.append(", "+scoreManageEntity.getReservedNum5());
        }
        // 预留人员单选 5个
        if(scoreManageEntity.getStaffid1() != null){
            colSb.append(", STAFFID1");
            valSb.append(", "+scoreManageEntity.getStaffid1());
        }
        if(scoreManageEntity.getStaffid2() != null){
            colSb.append(", STAFFID2");
            valSb.append(", "+scoreManageEntity.getStaffid2());
        }
        if(scoreManageEntity.getStaffid3() != null){
            colSb.append(", STAFFID3");
            valSb.append(", "+scoreManageEntity.getStaffid3());
        }
        if(scoreManageEntity.getStaffid4() != null){
            colSb.append(", STAFFID4");
            valSb.append(", "+scoreManageEntity.getStaffid4());
        }
        if(scoreManageEntity.getStaffid5() != null){
            colSb.append(", STAFFID5");
            valSb.append(", "+scoreManageEntity.getStaffid5());
        }
        // 预留人员多选 5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids1())){
            colSb.append(", STAFFIDS1");
            valSb.append(", '"+scoreManageEntity.getStaffids1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids2())){
            colSb.append(", STAFFIDS2");
            valSb.append(", '"+scoreManageEntity.getStaffids2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids3())){
            colSb.append(", STAFFIDS3");
            valSb.append(", '"+scoreManageEntity.getStaffids3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids4())){
            colSb.append(", STAFFIDS4");
            valSb.append(", '"+scoreManageEntity.getStaffids4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getStaffids5())){
            colSb.append(", STAFFIDS5");
            valSb.append(", '"+scoreManageEntity.getStaffids5()+"'");
        }
        // 预留组织单选 5个
        if(scoreManageEntity.getOrgid1() != null){
            colSb.append(", ORGID1");
            valSb.append(", "+scoreManageEntity.getOrgid1());
        }
        if(scoreManageEntity.getOrgid2() != null){
            colSb.append(", ORGID2");
            valSb.append(", "+scoreManageEntity.getOrgid2());
        }
        if(scoreManageEntity.getOrgid3() != null){
            colSb.append(", ORGID3");
            valSb.append(", "+scoreManageEntity.getOrgid3());
        }
        if(scoreManageEntity.getOrgid4() != null){
            colSb.append(", ORGID4");
            valSb.append(", "+scoreManageEntity.getOrgid4());
        }
        if(scoreManageEntity.getOrgid5() != null){
            colSb.append(", ORGID5");
            valSb.append(", "+scoreManageEntity.getOrgid5());
        }
        // 预留组织多选 5个
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids1())){
            colSb.append(", ORGIDS1");
            valSb.append(", '"+scoreManageEntity.getOrgids1()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids2())){
            colSb.append(", ORGIDS2");
            valSb.append(", '"+scoreManageEntity.getOrgids2()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids3())){
            colSb.append(", ORGIDS3");
            valSb.append(", '"+scoreManageEntity.getOrgids3()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids4())){
            colSb.append(", ORGIDS4");
            valSb.append(", '"+scoreManageEntity.getOrgids4()+"'");
        }
        if(StringUtil.isNotEmpty(scoreManageEntity.getOrgids5())){
            colSb.append(", ORGIDS5");
            valSb.append(", '"+scoreManageEntity.getOrgids5()+"'");
        }



        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_SCORE_MANAGE WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertScoreItem(ScoreManageItemEntity scoreItem){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_SCORE_MANAGE_ITEM (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(scoreItem.getScoreContent())){
            colSb.append(", SCORE_CONTENT");
            valSb.append(", '" + scoreItem.getScoreContent() + "'");
        }

        if(scoreItem.getSmId() != null){
            colSb.append(", SMID");
            valSb.append(", '" + scoreItem.getSmId() + "'");
        }

        if(scoreItem.getScore() != null){
            colSb.append(", SCORE");
            valSb.append(", '" + scoreItem.getScore() + "'");
        }

        if(scoreItem.getSort() != null){
            colSb.append(", SORT");
            valSb.append(", '" + scoreItem.getSort() + "'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteScoreItemsByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_SCORE_MANAGE_ITEM WHERE SMID IN ("+ids+")");
        return sb.toString();
    }
}
