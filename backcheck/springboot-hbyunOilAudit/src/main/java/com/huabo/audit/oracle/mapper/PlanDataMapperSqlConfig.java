package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.PlanDataEntity;
import com.hbfk.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
/**
 * @author Rui
 * @ClassName PlanDataMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class PlanDataMapperSqlConfig {

    public String selectByEntity( PlanDataEntity planDataEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PLAN_DATA RS WHERE 1=1 ");

        if(StringUtil.isNotEmpty(planDataEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+planDataEntity.getName()+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(PlanDataEntity planDataEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_PLAN_DATA RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(planDataEntity.getName())){
            sb.append("AND RS.NAME LIKE '%"+planDataEntity.getName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(PlanDataEntity planDataEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_PLAN_DATA SET ");
        sb.append("NAME = '"+planDataEntity.getName()+"'");

        if(StringUtil.isNotEmpty(planDataEntity.getNo())){
        	sb.append("NO = '"+planDataEntity.getNo()+"'");
        }
// ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString1())) {
            sb.append(", RESERVEDSTRING1 = '").append(planDataEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString2())) {
            sb.append(", RESERVEDSTRING2 = '").append(planDataEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString3())) {
            sb.append(", RESERVEDSTRING3 = '").append(planDataEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString4())) {
            sb.append(", RESERVEDSTRING4 = '").append(planDataEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString5())) {
            sb.append(", RESERVEDSTRING5 = '").append(planDataEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString6())) {
            sb.append(", RESERVEDSTRING6 = '").append(planDataEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString7())) {
            sb.append(", RESERVEDSTRING7 = '").append(planDataEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString8())) {
            sb.append(", RESERVEDSTRING8 = '").append(planDataEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString9())) {
            sb.append(", RESERVEDSTRING9 = '").append(planDataEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString10())) {
            sb.append(", RESERVEDSTRING10 = '").append(planDataEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent1())) {
            sb.append(", RESERVEDCONTENT1 = '").append(planDataEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent2())) {
            sb.append(", RESERVEDCONTENT2 = '").append(planDataEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent3())) {
            sb.append(", RESERVEDCONTENT3 = '").append(planDataEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent4())) {
            sb.append(", RESERVEDCONTENT4 = '").append(planDataEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent5())) {
            sb.append(", RESERVEDCONTENT5 = '").append(planDataEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent6())) {
            sb.append(", RESERVEDCONTENT6 = '").append(planDataEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent7())) {
            sb.append(", RESERVEDCONTENT7 = '").append(planDataEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent8())) {
            sb.append(", RESERVEDCONTENT8 = '").append(planDataEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent9())) {
            sb.append(", RESERVEDCONTENT9 = '").append(planDataEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent10())) {
            sb.append(", RESERVEDCONTENT10 = '").append(planDataEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple1())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '").append(planDataEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple2())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '").append(planDataEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple3())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '").append(planDataEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple4())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '").append(planDataEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple5())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '").append(planDataEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice1())) {
            sb.append(", RESERVEDMULTIPLECHOICE1 = '").append(planDataEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice2())) {
            sb.append(", RESERVEDMULTIPLECHOICE2 = '").append(planDataEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice3())) {
            sb.append(", RESERVEDMULTIPLECHOICE3 = '").append(planDataEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice4())) {
            sb.append(", RESERVEDMULTIPLECHOICE4 = '").append(planDataEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice5())) {
            sb.append(", RESERVEDMULTIPLECHOICE5 = '").append(planDataEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个（格式化为 yyyy） ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (planDataEntity.getReservedYearTime1() != null) {
            sb.append(", RESERVEDYEARTIME1 = '").append(sdfYear.format(planDataEntity.getReservedYearTime1())).append("'");
        }
        if (planDataEntity.getReservedYearTime2() != null) {
            sb.append(", RESERVEDYEARTIME2 = '").append(sdfYear.format(planDataEntity.getReservedYearTime2())).append("'");
        }
        if (planDataEntity.getReservedYearTime3() != null) {
            sb.append(", RESERVEDYEARTIME3 = '").append(sdfYear.format(planDataEntity.getReservedYearTime3())).append("'");
        }
        if (planDataEntity.getReservedYearTime4() != null) {
            sb.append(", RESERVEDYEARTIME4 = '").append(sdfYear.format(planDataEntity.getReservedYearTime4())).append("'");
        }
        if (planDataEntity.getReservedYearTime5() != null) {
            sb.append(", RESERVEDYEARTIME5 = '").append(sdfYear.format(planDataEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (planDataEntity.getReservedYearAccurateTime1() != null) {
            sb.append(", RESERVEDYEARACCURATETIME1 = '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime2() != null) {
            sb.append(", RESERVEDYEARACCURATETIME2 = '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime3() != null) {
            sb.append(", RESERVEDYEARACCURATETIME3 = '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime4() != null) {
            sb.append(", RESERVEDYEARACCURATETIME4 = '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime5() != null) {
            sb.append(", RESERVEDYEARACCURATETIME5 = '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (planDataEntity.getReservedTime1() != null) {
            sb.append(", RESERVEDTIME1 = '").append(sdfDate.format(planDataEntity.getReservedTime1())).append("'");
        }
        if (planDataEntity.getReservedTime2() != null) {
            sb.append(", RESERVEDTIME2 = '").append(sdfDate.format(planDataEntity.getReservedTime2())).append("'");
        }
        if (planDataEntity.getReservedTime3() != null) {
            sb.append(", RESERVEDTIME3 = '").append(sdfDate.format(planDataEntity.getReservedTime3())).append("'");
        }
        if (planDataEntity.getReservedTime4() != null) {
            sb.append(", RESERVEDTIME4 = '").append(sdfDate.format(planDataEntity.getReservedTime4())).append("'");
        }
        if (planDataEntity.getReservedTime5() != null) {
            sb.append(", RESERVEDTIME5 = '").append(sdfDate.format(planDataEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice1())) {
            sb.append(", RESERVEDSINGLECHOICE1 = '").append(planDataEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice2())) {
            sb.append(", RESERVEDSINGLECHOICE2 = '").append(planDataEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice3())) {
            sb.append(", RESERVEDSINGLECHOICE3 = '").append(planDataEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice4())) {
            sb.append(", RESERVEDSINGLECHOICE4 = '").append(planDataEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice5())) {
            sb.append(", RESERVEDSINGLECHOICE5 = '").append(planDataEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice1())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '").append(planDataEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice2())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '").append(planDataEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice3())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '").append(planDataEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice4())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '").append(planDataEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice5())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '").append(planDataEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (planDataEntity.getReservedNum1() != null) {
            sb.append(", RESERVEDNUM1 = ").append(planDataEntity.getReservedNum1());
        }
        if (planDataEntity.getReservedNum2() != null) {
            sb.append(", RESERVEDNUM2 = ").append(planDataEntity.getReservedNum2());
        }
        if (planDataEntity.getReservedNum3() != null) {
            sb.append(", RESERVEDNUM3 = ").append(planDataEntity.getReservedNum3());
        }
        if (planDataEntity.getReservedNum4() != null) {
            sb.append(", RESERVEDNUM4 = ").append(planDataEntity.getReservedNum4());
        }
        if (planDataEntity.getReservedNum5() != null) {
            sb.append(", RESERVEDNUM5 = ").append(planDataEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (planDataEntity.getStaffId1() != null) {
            sb.append(", STAFFID1 = ").append(planDataEntity.getStaffId1());
        }
        if (planDataEntity.getStaffId2() != null) {
            sb.append(", STAFFID2 = ").append(planDataEntity.getStaffId2());
        }
        if (planDataEntity.getStaffId3() != null) {
            sb.append(", STAFFID3 = ").append(planDataEntity.getStaffId3());
        }
        if (planDataEntity.getStaffId4() != null) {
            sb.append(", STAFFID4 = ").append(planDataEntity.getStaffId4());
        }
        if (planDataEntity.getStaffId5() != null) {
            sb.append(", STAFFID5 = ").append(planDataEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds1())) {
            sb.append(", STAFFIDS1 = '").append(planDataEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds2())) {
            sb.append(", STAFFIDS2 = '").append(planDataEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds3())) {
            sb.append(", STAFFIDS3 = '").append(planDataEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds4())) {
            sb.append(", STAFFIDS4 = '").append(planDataEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds5())) {
            sb.append(", STAFFIDS5 = '").append(planDataEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (planDataEntity.getOrgId1() != null) {
            sb.append(", ORGID1 = ").append(planDataEntity.getOrgId1());
        }
        if (planDataEntity.getOrgId2() != null) {
            sb.append(", ORGID2 = ").append(planDataEntity.getOrgId2());
        }
        if (planDataEntity.getOrgId3() != null) {
            sb.append(", ORGID3 = ").append(planDataEntity.getOrgId3());
        }
        if (planDataEntity.getOrgId4() != null) {
            sb.append(", ORGID4 = ").append(planDataEntity.getOrgId4());
        }
        if (planDataEntity.getOrgId5() != null) {
            sb.append(", ORGID5 = ").append(planDataEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds1())) {
            sb.append(", ORGIDS1 = '").append(planDataEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds2())) {
            sb.append(", ORGIDS2 = '").append(planDataEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds3())) {
            sb.append(", ORGIDS3 = '").append(planDataEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds4())) {
            sb.append(", ORGIDS4 = '").append(planDataEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds5())) {
            sb.append(", ORGIDS5 = '").append(planDataEntity.getOrgIds5()).append("'");
        }



        sb.append(" WHERE ID = '"+planDataEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(PlanDataEntity planDataEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_PLAN_DATA (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(planDataEntity.getName())){
            colSb.append(", NAME");
            valSb.append(", '" + planDataEntity.getName() + "'");
        }

        if(StringUtil.isNotEmpty(planDataEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + planDataEntity.getNo() + "'");
        }

        if(planDataEntity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + planDataEntity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");
// ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString1())) {
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '").append(planDataEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString2())) {
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '").append(planDataEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString3())) {
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '").append(planDataEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString4())) {
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '").append(planDataEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString5())) {
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '").append(planDataEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString6())) {
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '").append(planDataEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString7())) {
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '").append(planDataEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString8())) {
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '").append(planDataEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString9())) {
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '").append(planDataEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedString10())) {
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '").append(planDataEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent1())) {
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '").append(planDataEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent2())) {
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '").append(planDataEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent3())) {
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '").append(planDataEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent4())) {
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '").append(planDataEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent5())) {
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '").append(planDataEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent6())) {
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '").append(planDataEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent7())) {
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '").append(planDataEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent8())) {
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '").append(planDataEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent9())) {
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '").append(planDataEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedContent10())) {
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '").append(planDataEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple1())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '").append(planDataEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple2())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '").append(planDataEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple3())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '").append(planDataEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple4())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '").append(planDataEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownMultiple5())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '").append(planDataEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice1())) {
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '").append(planDataEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice2())) {
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '").append(planDataEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice3())) {
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '").append(planDataEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice4())) {
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '").append(planDataEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedMultipleChoice5())) {
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '").append(planDataEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个 ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (planDataEntity.getReservedYearTime1() != null) {
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", '").append(sdfYear.format(planDataEntity.getReservedYearTime1())).append("'");
        }
        if (planDataEntity.getReservedYearTime2() != null) {
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", '").append(sdfYear.format(planDataEntity.getReservedYearTime2())).append("'");
        }
        if (planDataEntity.getReservedYearTime3() != null) {
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", '").append(sdfYear.format(planDataEntity.getReservedYearTime3())).append("'");
        }
        if (planDataEntity.getReservedYearTime4() != null) {
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", '").append(sdfYear.format(planDataEntity.getReservedYearTime4())).append("'");
        }
        if (planDataEntity.getReservedYearTime5() != null) {
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", '").append(sdfYear.format(planDataEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (planDataEntity.getReservedYearAccurateTime1() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime2() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime3() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime4() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (planDataEntity.getReservedYearAccurateTime5() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", '").append(sdfDateTime.format(planDataEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (planDataEntity.getReservedTime1() != null) {
            colSb.append(", RESERVEDTIME1");
            valSb.append(", '").append(sdfDate.format(planDataEntity.getReservedTime1())).append("'");
        }
        if (planDataEntity.getReservedTime2() != null) {
            colSb.append(", RESERVEDTIME2");
            valSb.append(", '").append(sdfDate.format(planDataEntity.getReservedTime2())).append("'");
        }
        if (planDataEntity.getReservedTime3() != null) {
            colSb.append(", RESERVEDTIME3");
            valSb.append(", '").append(sdfDate.format(planDataEntity.getReservedTime3())).append("'");
        }
        if (planDataEntity.getReservedTime4() != null) {
            colSb.append(", RESERVEDTIME4");
            valSb.append(", '").append(sdfDate.format(planDataEntity.getReservedTime4())).append("'");
        }
        if (planDataEntity.getReservedTime5() != null) {
            colSb.append(", RESERVEDTIME5");
            valSb.append(", '").append(sdfDate.format(planDataEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice1())) {
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '").append(planDataEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice2())) {
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '").append(planDataEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice3())) {
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '").append(planDataEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice4())) {
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '").append(planDataEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedSingleChoice5())) {
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '").append(planDataEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice1())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '").append(planDataEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice2())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '").append(planDataEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice3())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '").append(planDataEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice4())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '").append(planDataEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getReservedDropdownSingleChoice5())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '").append(planDataEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (planDataEntity.getReservedNum1() != null) {
            colSb.append(", RESERVEDNUM1");
            valSb.append(", ").append(planDataEntity.getReservedNum1());
        }
        if (planDataEntity.getReservedNum2() != null) {
            colSb.append(", RESERVEDNUM2");
            valSb.append(", ").append(planDataEntity.getReservedNum2());
        }
        if (planDataEntity.getReservedNum3() != null) {
            colSb.append(", RESERVEDNUM3");
            valSb.append(", ").append(planDataEntity.getReservedNum3());
        }
        if (planDataEntity.getReservedNum4() != null) {
            colSb.append(", RESERVEDNUM4");
            valSb.append(", ").append(planDataEntity.getReservedNum4());
        }
        if (planDataEntity.getReservedNum5() != null) {
            colSb.append(", RESERVEDNUM5");
            valSb.append(", ").append(planDataEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (planDataEntity.getStaffId1() != null) {
            colSb.append(", STAFFID1");
            valSb.append(", ").append(planDataEntity.getStaffId1());
        }
        if (planDataEntity.getStaffId2() != null) {
            colSb.append(", STAFFID2");
            valSb.append(", ").append(planDataEntity.getStaffId2());
        }
        if (planDataEntity.getStaffId3() != null) {
            colSb.append(", STAFFID3");
            valSb.append(", ").append(planDataEntity.getStaffId3());
        }
        if (planDataEntity.getStaffId4() != null) {
            colSb.append(", STAFFID4");
            valSb.append(", ").append(planDataEntity.getStaffId4());
        }
        if (planDataEntity.getStaffId5() != null) {
            colSb.append(", STAFFID5");
            valSb.append(", ").append(planDataEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds1())) {
            colSb.append(", STAFFIDS1");
            valSb.append(", '").append(planDataEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds2())) {
            colSb.append(", STAFFIDS2");
            valSb.append(", '").append(planDataEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds3())) {
            colSb.append(", STAFFIDS3");
            valSb.append(", '").append(planDataEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds4())) {
            colSb.append(", STAFFIDS4");
            valSb.append(", '").append(planDataEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getStaffIds5())) {
            colSb.append(", STAFFIDS5");
            valSb.append(", '").append(planDataEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (planDataEntity.getOrgId1() != null) {
            colSb.append(", ORGID1");
            valSb.append(", ").append(planDataEntity.getOrgId1());
        }
        if (planDataEntity.getOrgId2() != null) {
            colSb.append(", ORGID2");
            valSb.append(", ").append(planDataEntity.getOrgId2());
        }
        if (planDataEntity.getOrgId3() != null) {
            colSb.append(", ORGID3");
            valSb.append(", ").append(planDataEntity.getOrgId3());
        }
        if (planDataEntity.getOrgId4() != null) {
            colSb.append(", ORGID4");
            valSb.append(", ").append(planDataEntity.getOrgId4());
        }
        if (planDataEntity.getOrgId5() != null) {
            colSb.append(", ORGID5");
            valSb.append(", ").append(planDataEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds1())) {
            colSb.append(", ORGIDS1");
            valSb.append(", '").append(planDataEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds2())) {
            colSb.append(", ORGIDS2");
            valSb.append(", '").append(planDataEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds3())) {
            colSb.append(", ORGIDS3");
            valSb.append(", '").append(planDataEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds4())) {
            colSb.append(", ORGIDS4");
            valSb.append(", '").append(planDataEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(planDataEntity.getOrgIds5())) {
            colSb.append(", ORGIDS5");
            valSb.append(", '").append(planDataEntity.getOrgIds5()).append("'");
        }
        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_PLAN_DATA WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_PLAN_DATA_ATT(PDID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_PLAN_DATA_ATT WHERE PDID IN ("+ids+")");
        return sb.toString();
    }
}
