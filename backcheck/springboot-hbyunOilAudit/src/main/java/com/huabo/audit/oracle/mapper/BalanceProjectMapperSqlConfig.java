package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.BalanceProjectEntity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author Rui
 * @ClassName BalanceProjectMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class BalanceProjectMapperSqlConfig {

    public String selectByEntity( BalanceProjectEntity balanceProjectEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_BALANCE_PROJECT RS WHERE 1=1 ");


        if(StringUtil.isNotEmpty(balanceProjectEntity.getPlanNo())){
            sb.append("AND RS.PLAN_NO LIKE '%"+balanceProjectEntity.getPlanNo()+"%'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+balanceProjectEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getContractNo())){
            sb.append("AND RS.CONTRACT_NO LIKE '%"+balanceProjectEntity.getContractNo()+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(BalanceProjectEntity balanceProjectEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_BALANCE_PROJECT RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(balanceProjectEntity.getPlanNo())){
            sb.append("AND RS.PLAN_NO = '"+balanceProjectEntity.getPlanNo()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+balanceProjectEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getContractNo())){
            sb.append("AND RS.CONTRACT_NO = '"+balanceProjectEntity.getContractNo()+"'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(BalanceProjectEntity balanceProjectEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_BALANCE_PROJECT SET ");
        sb.append("PLAN_NO = '"+balanceProjectEntity.getPlanNo()+"'");

        if(StringUtil.isNotEmpty(balanceProjectEntity.getContractNo())){
            sb.append(", CONTRACT_NO = "+balanceProjectEntity.getContractNo());
        }


        if(StringUtil.isNotEmpty(balanceProjectEntity.getProjectName())){
            sb.append(", PROJECT_NAME = '"+balanceProjectEntity.getProjectName()+"'");

        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructionContent())){
            sb.append(", CONSTRUCTION_CONTENT = '"+balanceProjectEntity.getConstructionContent()+"'");

        }

        if(balanceProjectEntity.getSettlementAmount() != null){
            sb.append(", SETTLEMENT_AMOUNT = '"+balanceProjectEntity.getSettlementAmount()+"'");
        }

        if(balanceProjectEntity.getMaterialAmount() != null){
            sb.append(", MATERIAL_AMOUNT = '"+balanceProjectEntity.getMaterialAmount()+"'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getSurveyOrgId() )){
            sb.append(", SURVEY_ORG_ID = '"+balanceProjectEntity.getSurveyOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getDesignOrgId() )){
            sb.append(", DESIGN_ORG_ID = '"+balanceProjectEntity.getDesignOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getConstructOrgId() )){
            sb.append(", CONSTRUCT_ORG_ID = '"+balanceProjectEntity.getConstructOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getSupervisionOrgId() )){
            sb.append(", SUPERVISION_ORG_ID = '"+balanceProjectEntity.getSupervisionOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getBuildManager())){
            sb.append(", BUILD_MANAGER = '"+balanceProjectEntity.getBuildManager()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManagerPhone())){
            sb.append(", BUILD_MANAGER_PHONE = '"+balanceProjectEntity.getManagerPhone()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getMaterialManager())){
            sb.append(", MATERIAL_MANAGER = '"+balanceProjectEntity.getMaterialManager()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getMaterialManagerPhone())){
            sb.append(", MATERIAL_MANAGER_PHONE = '"+balanceProjectEntity.getMaterialManagerPhone()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructManager())){
            sb.append(", CONSTRUCT_MANAGER = '"+balanceProjectEntity.getConstructManager()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructManagerPhone())){
            sb.append(", CONSTRUCT_MANAGER_PHONE = '"+balanceProjectEntity.getConstructManagerPhone()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManager())){
            sb.append(", MANAGER = '"+balanceProjectEntity.getManager()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManagerPhone())){
            sb.append(", MANAGER_PHONE = '"+balanceProjectEntity.getManagerPhone()+"'");
        }
        
        if(StringUtil.isNotEmpty(balanceProjectEntity.getNo())){
            sb.append(", NO = '"+balanceProjectEntity.getNo()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getDesignManager())){
            sb.append(", DESIGN_MANAGER = '"+balanceProjectEntity.getDesignManager()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getDesignManagerPhone())){
            sb.append(", DESIGN_MANAGER_PHONE = '"+balanceProjectEntity.getDesignManagerPhone()+"'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getRemark())){
            sb.append(", REMARK = '"+balanceProjectEntity.getRemark()+"'");
        }

        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString1())) {
            sb.append(", RESERVEDSTRING1 = '").append(balanceProjectEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString2())) {
            sb.append(", RESERVEDSTRING2 = '").append(balanceProjectEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString3())) {
            sb.append(", RESERVEDSTRING3 = '").append(balanceProjectEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString4())) {
            sb.append(", RESERVEDSTRING4 = '").append(balanceProjectEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString5())) {
            sb.append(", RESERVEDSTRING5 = '").append(balanceProjectEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString6())) {
            sb.append(", RESERVEDSTRING6 = '").append(balanceProjectEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString7())) {
            sb.append(", RESERVEDSTRING7 = '").append(balanceProjectEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString8())) {
            sb.append(", RESERVEDSTRING8 = '").append(balanceProjectEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString9())) {
            sb.append(", RESERVEDSTRING9 = '").append(balanceProjectEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString10())) {
            sb.append(", RESERVEDSTRING10 = '").append(balanceProjectEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent1())) {
            sb.append(", RESERVEDCONTENT1 = '").append(balanceProjectEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent2())) {
            sb.append(", RESERVEDCONTENT2 = '").append(balanceProjectEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent3())) {
            sb.append(", RESERVEDCONTENT3 = '").append(balanceProjectEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent4())) {
            sb.append(", RESERVEDCONTENT4 = '").append(balanceProjectEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent5())) {
            sb.append(", RESERVEDCONTENT5 = '").append(balanceProjectEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent6())) {
            sb.append(", RESERVEDCONTENT6 = '").append(balanceProjectEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent7())) {
            sb.append(", RESERVEDCONTENT7 = '").append(balanceProjectEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent8())) {
            sb.append(", RESERVEDCONTENT8 = '").append(balanceProjectEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent9())) {
            sb.append(", RESERVEDCONTENT9 = '").append(balanceProjectEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent10())) {
            sb.append(", RESERVEDCONTENT10 = '").append(balanceProjectEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple1())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '").append(balanceProjectEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple2())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '").append(balanceProjectEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple3())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '").append(balanceProjectEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple4())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '").append(balanceProjectEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple5())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '").append(balanceProjectEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice1())) {
            sb.append(", RESERVEDMULTIPLECHOICE1 = '").append(balanceProjectEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice2())) {
            sb.append(", RESERVEDMULTIPLECHOICE2 = '").append(balanceProjectEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice3())) {
            sb.append(", RESERVEDMULTIPLECHOICE3 = '").append(balanceProjectEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice4())) {
            sb.append(", RESERVEDMULTIPLECHOICE4 = '").append(balanceProjectEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice5())) {
            sb.append(", RESERVEDMULTIPLECHOICE5 = '").append(balanceProjectEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个（格式化为 yyyy） ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (balanceProjectEntity.getReservedYearTime1() != null) {
            sb.append(", RESERVEDYEARTIME1 = '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime2() != null) {
            sb.append(", RESERVEDYEARTIME2 = '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime3() != null) {
            sb.append(", RESERVEDYEARTIME3 = '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime4() != null) {
            sb.append(", RESERVEDYEARTIME4 = '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime5() != null) {
            sb.append(", RESERVEDYEARTIME5 = '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (balanceProjectEntity.getReservedYearAccurateTime1() != null) {
            sb.append(", RESERVEDYEARACCURATETIME1 = '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime2() != null) {
            sb.append(", RESERVEDYEARACCURATETIME2 = '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime3() != null) {
            sb.append(", RESERVEDYEARACCURATETIME3 = '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime4() != null) {
            sb.append(", RESERVEDYEARACCURATETIME4 = '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime5() != null) {
            sb.append(", RESERVEDYEARACCURATETIME5 = '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (balanceProjectEntity.getReservedTime1() != null) {
            sb.append(", RESERVEDTIME1 = '").append(sdfDate.format(balanceProjectEntity.getReservedTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedTime2() != null) {
            sb.append(", RESERVEDTIME2 = '").append(sdfDate.format(balanceProjectEntity.getReservedTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedTime3() != null) {
            sb.append(", RESERVEDTIME3 = '").append(sdfDate.format(balanceProjectEntity.getReservedTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedTime4() != null) {
            sb.append(", RESERVEDTIME4 = '").append(sdfDate.format(balanceProjectEntity.getReservedTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedTime5() != null) {
            sb.append(", RESERVEDTIME5 = '").append(sdfDate.format(balanceProjectEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice1())) {
            sb.append(", RESERVEDSINGLECHOICE1 = '").append(balanceProjectEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice2())) {
            sb.append(", RESERVEDSINGLECHOICE2 = '").append(balanceProjectEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice3())) {
            sb.append(", RESERVEDSINGLECHOICE3 = '").append(balanceProjectEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice4())) {
            sb.append(", RESERVEDSINGLECHOICE4 = '").append(balanceProjectEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice5())) {
            sb.append(", RESERVEDSINGLECHOICE5 = '").append(balanceProjectEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice1())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '").append(balanceProjectEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice2())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '").append(balanceProjectEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice3())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '").append(balanceProjectEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice4())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '").append(balanceProjectEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice5())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '").append(balanceProjectEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (balanceProjectEntity.getReservedNum1() != null) {
            sb.append(", RESERVEDNUM1 = ").append(balanceProjectEntity.getReservedNum1());
        }
        if (balanceProjectEntity.getReservedNum2() != null) {
            sb.append(", RESERVEDNUM2 = ").append(balanceProjectEntity.getReservedNum2());
        }
        if (balanceProjectEntity.getReservedNum3() != null) {
            sb.append(", RESERVEDNUM3 = ").append(balanceProjectEntity.getReservedNum3());
        }
        if (balanceProjectEntity.getReservedNum4() != null) {
            sb.append(", RESERVEDNUM4 = ").append(balanceProjectEntity.getReservedNum4());
        }
        if (balanceProjectEntity.getReservedNum5() != null) {
            sb.append(", RESERVEDNUM5 = ").append(balanceProjectEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (balanceProjectEntity.getStaffId1() != null) {
            sb.append(", STAFFID1 = ").append(balanceProjectEntity.getStaffId1());
        }
        if (balanceProjectEntity.getStaffId2() != null) {
            sb.append(", STAFFID2 = ").append(balanceProjectEntity.getStaffId2());
        }
        if (balanceProjectEntity.getStaffId3() != null) {
            sb.append(", STAFFID3 = ").append(balanceProjectEntity.getStaffId3());
        }
        if (balanceProjectEntity.getStaffId4() != null) {
            sb.append(", STAFFID4 = ").append(balanceProjectEntity.getStaffId4());
        }
        if (balanceProjectEntity.getStaffId5() != null) {
            sb.append(", STAFFID5 = ").append(balanceProjectEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds1())) {
            sb.append(", STAFFIDS1 = '").append(balanceProjectEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds2())) {
            sb.append(", STAFFIDS2 = '").append(balanceProjectEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds3())) {
            sb.append(", STAFFIDS3 = '").append(balanceProjectEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds4())) {
            sb.append(", STAFFIDS4 = '").append(balanceProjectEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds5())) {
            sb.append(", STAFFIDS5 = '").append(balanceProjectEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (balanceProjectEntity.getOrgId1() != null) {
            sb.append(", ORGID1 = ").append(balanceProjectEntity.getOrgId1());
        }
        if (balanceProjectEntity.getOrgId2() != null) {
            sb.append(", ORGID2 = ").append(balanceProjectEntity.getOrgId2());
        }
        if (balanceProjectEntity.getOrgId3() != null) {
            sb.append(", ORGID3 = ").append(balanceProjectEntity.getOrgId3());
        }
        if (balanceProjectEntity.getOrgId4() != null) {
            sb.append(", ORGID4 = ").append(balanceProjectEntity.getOrgId4());
        }
        if (balanceProjectEntity.getOrgId5() != null) {
            sb.append(", ORGID5 = ").append(balanceProjectEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds1())) {
            sb.append(", ORGIDS1 = '").append(balanceProjectEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds2())) {
            sb.append(", ORGIDS2 = '").append(balanceProjectEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds3())) {
            sb.append(", ORGIDS3 = '").append(balanceProjectEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds4())) {
            sb.append(", ORGIDS4 = '").append(balanceProjectEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds5())) {
            sb.append(", ORGIDS5 = '").append(balanceProjectEntity.getOrgIds5()).append("'");
        }

        sb.append(" WHERE ID = '"+balanceProjectEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(BalanceProjectEntity balanceProjectEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_BALANCE_PROJECT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(balanceProjectEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + balanceProjectEntity.getProjectName() + "'");
        }
        
        if(StringUtil.isNotEmpty(balanceProjectEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + balanceProjectEntity.getNo() + "'");
        }
        
        if(StringUtil.isNotEmpty(balanceProjectEntity.getPlanNo())){
            colSb.append(", PLAN_NO");
            valSb.append(", '" + balanceProjectEntity.getPlanNo() + "'");
        }


        if(StringUtil.isNotEmpty(balanceProjectEntity.getContractNo())){
            colSb.append(", CONTRACT_NO");
            valSb.append(", '" + balanceProjectEntity.getContractNo() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructionContent())){
            colSb.append(", CONSTRUCTION_CONTENT");
            valSb.append(", '" + balanceProjectEntity.getConstructionContent() + "'");
        }

        if(balanceProjectEntity.getSettlementAmount() != null){
            colSb.append(", SETTLEMENT_AMOUNT");
            valSb.append(", '" + balanceProjectEntity.getSettlementAmount() + "'");
        }

        if(balanceProjectEntity.getMaterialAmount() != null){
            colSb.append(", MATERIAL_AMOUNT");
            valSb.append(", '" + balanceProjectEntity.getMaterialAmount() + "'");
        }


        if(StringUtil.isNotEmpty(  balanceProjectEntity.getSurveyOrgId() )){
            colSb.append(", SURVEY_ORG_ID");
            valSb.append(", '" + balanceProjectEntity.getSurveyOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getDesignOrgId() )){
            colSb.append(", DESIGN_ORG_ID");
            valSb.append(", '" + balanceProjectEntity.getDesignOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getConstructOrgId() )){
            colSb.append(", CONSTRUCT_ORG_ID");
            valSb.append(", '" + balanceProjectEntity.getConstructOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  balanceProjectEntity.getSupervisionOrgId() )){
            colSb.append(", SUPERVISION_ORG_ID");
            valSb.append(", '" + balanceProjectEntity.getSupervisionOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getBuildManager())){
            colSb.append(", BUILD_MANAGER");
            valSb.append(", '" + balanceProjectEntity.getBuildManager() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManagerPhone())){
            colSb.append(", BUILD_MANAGER_PHONE");
            valSb.append(", '" + balanceProjectEntity.getManagerPhone() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getMaterialManager())){
            colSb.append(", MATERIAL_MANAGER");
            valSb.append(", '" + balanceProjectEntity.getMaterialManager() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getMaterialManagerPhone())){
            colSb.append(", MATERIAL_MANAGER_PHONE");
            valSb.append(", '" + balanceProjectEntity.getMaterialManagerPhone() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructManager())){
            colSb.append(", CONSTRUCT_MANAGER");
            valSb.append(", '" + balanceProjectEntity.getConstructManager() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getConstructManagerPhone())){
            colSb.append(", CONSTRUCT_MANAGER_PHONE");
            valSb.append(", '" + balanceProjectEntity.getMaterialManagerPhone() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManager())){
            colSb.append(", MANAGER");
            valSb.append(", '" + balanceProjectEntity.getManager() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getManagerPhone())){
            colSb.append(", MANAGER_PHONE");
            valSb.append(", '" + balanceProjectEntity.getManagerPhone() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getDesignManager())){
            colSb.append(", DESIGN_MANAGER");
            valSb.append(", '" + balanceProjectEntity.getDesignManager() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getDesignManagerPhone())){
            colSb.append(", DESIGN_MANAGER_PHONE");
            valSb.append(", '" + balanceProjectEntity.getDesignManagerPhone() + "'");
        }

        if(StringUtil.isNotEmpty(balanceProjectEntity.getRemark())){
            colSb.append(", REMARK");
            valSb.append(", '" + balanceProjectEntity.getRemark() + "'");
        }

        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString1())) {
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '").append(balanceProjectEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString2())) {
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '").append(balanceProjectEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString3())) {
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '").append(balanceProjectEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString4())) {
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '").append(balanceProjectEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString5())) {
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '").append(balanceProjectEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString6())) {
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '").append(balanceProjectEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString7())) {
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '").append(balanceProjectEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString8())) {
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '").append(balanceProjectEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString9())) {
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '").append(balanceProjectEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedString10())) {
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '").append(balanceProjectEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent1())) {
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent2())) {
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent3())) {
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent4())) {
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent5())) {
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent6())) {
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent7())) {
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent8())) {
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent9())) {
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedContent10())) {
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '").append(balanceProjectEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple1())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple2())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple3())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple4())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownMultiple5())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice1())) {
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '").append(balanceProjectEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice2())) {
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '").append(balanceProjectEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice3())) {
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '").append(balanceProjectEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice4())) {
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '").append(balanceProjectEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedMultipleChoice5())) {
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '").append(balanceProjectEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个 ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (balanceProjectEntity.getReservedYearTime1() != null) {
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime2() != null) {
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime3() != null) {
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime4() != null) {
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedYearTime5() != null) {
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", '").append(sdfYear.format(balanceProjectEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (balanceProjectEntity.getReservedYearAccurateTime1() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime2() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime3() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime4() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedYearAccurateTime5() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", '").append(sdfDateTime.format(balanceProjectEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (balanceProjectEntity.getReservedTime1() != null) {
            colSb.append(", RESERVEDTIME1");
            valSb.append(", '").append(sdfDate.format(balanceProjectEntity.getReservedTime1())).append("'");
        }
        if (balanceProjectEntity.getReservedTime2() != null) {
            colSb.append(", RESERVEDTIME2");
            valSb.append(", '").append(sdfDate.format(balanceProjectEntity.getReservedTime2())).append("'");
        }
        if (balanceProjectEntity.getReservedTime3() != null) {
            colSb.append(", RESERVEDTIME3");
            valSb.append(", '").append(sdfDate.format(balanceProjectEntity.getReservedTime3())).append("'");
        }
        if (balanceProjectEntity.getReservedTime4() != null) {
            colSb.append(", RESERVEDTIME4");
            valSb.append(", '").append(sdfDate.format(balanceProjectEntity.getReservedTime4())).append("'");
        }
        if (balanceProjectEntity.getReservedTime5() != null) {
            colSb.append(", RESERVEDTIME5");
            valSb.append(", '").append(sdfDate.format(balanceProjectEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice1())) {
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '").append(balanceProjectEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice2())) {
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '").append(balanceProjectEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice3())) {
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '").append(balanceProjectEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice4())) {
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '").append(balanceProjectEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedSingleChoice5())) {
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '").append(balanceProjectEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice1())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice2())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice3())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice4())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getReservedDropdownSingleChoice5())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '").append(balanceProjectEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (balanceProjectEntity.getReservedNum1() != null) {
            colSb.append(", RESERVEDNUM1");
            valSb.append(", ").append(balanceProjectEntity.getReservedNum1());
        }
        if (balanceProjectEntity.getReservedNum2() != null) {
            colSb.append(", RESERVEDNUM2");
            valSb.append(", ").append(balanceProjectEntity.getReservedNum2());
        }
        if (balanceProjectEntity.getReservedNum3() != null) {
            colSb.append(", RESERVEDNUM3");
            valSb.append(", ").append(balanceProjectEntity.getReservedNum3());
        }
        if (balanceProjectEntity.getReservedNum4() != null) {
            colSb.append(", RESERVEDNUM4");
            valSb.append(", ").append(balanceProjectEntity.getReservedNum4());
        }
        if (balanceProjectEntity.getReservedNum5() != null) {
            colSb.append(", RESERVEDNUM5");
            valSb.append(", ").append(balanceProjectEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (balanceProjectEntity.getStaffId1() != null) {
            colSb.append(", STAFFID1");
            valSb.append(", ").append(balanceProjectEntity.getStaffId1());
        }
        if (balanceProjectEntity.getStaffId2() != null) {
            colSb.append(", STAFFID2");
            valSb.append(", ").append(balanceProjectEntity.getStaffId2());
        }
        if (balanceProjectEntity.getStaffId3() != null) {
            colSb.append(", STAFFID3");
            valSb.append(", ").append(balanceProjectEntity.getStaffId3());
        }
        if (balanceProjectEntity.getStaffId4() != null) {
            colSb.append(", STAFFID4");
            valSb.append(", ").append(balanceProjectEntity.getStaffId4());
        }
        if (balanceProjectEntity.getStaffId5() != null) {
            colSb.append(", STAFFID5");
            valSb.append(", ").append(balanceProjectEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds1())) {
            colSb.append(", STAFFIDS1");
            valSb.append(", '").append(balanceProjectEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds2())) {
            colSb.append(", STAFFIDS2");
            valSb.append(", '").append(balanceProjectEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds3())) {
            colSb.append(", STAFFIDS3");
            valSb.append(", '").append(balanceProjectEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds4())) {
            colSb.append(", STAFFIDS4");
            valSb.append(", '").append(balanceProjectEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getStaffIds5())) {
            colSb.append(", STAFFIDS5");
            valSb.append(", '").append(balanceProjectEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (balanceProjectEntity.getOrgId1() != null) {
            colSb.append(", ORGID1");
            valSb.append(", ").append(balanceProjectEntity.getOrgId1());
        }
        if (balanceProjectEntity.getOrgId2() != null) {
            colSb.append(", ORGID2");
            valSb.append(", ").append(balanceProjectEntity.getOrgId2());
        }
        if (balanceProjectEntity.getOrgId3() != null) {
            colSb.append(", ORGID3");
            valSb.append(", ").append(balanceProjectEntity.getOrgId3());
        }
        if (balanceProjectEntity.getOrgId4() != null) {
            colSb.append(", ORGID4");
            valSb.append(", ").append(balanceProjectEntity.getOrgId4());
        }
        if (balanceProjectEntity.getOrgId5() != null) {
            colSb.append(", ORGID5");
            valSb.append(", ").append(balanceProjectEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds1())) {
            colSb.append(", ORGIDS1");
            valSb.append(", '").append(balanceProjectEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds2())) {
            colSb.append(", ORGIDS2");
            valSb.append(", '").append(balanceProjectEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds3())) {
            colSb.append(", ORGIDS3");
            valSb.append(", '").append(balanceProjectEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds4())) {
            colSb.append(", ORGIDS4");
            valSb.append(", '").append(balanceProjectEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(balanceProjectEntity.getOrgIds5())) {
            colSb.append(", ORGIDS5");
            valSb.append(", '").append(balanceProjectEntity.getOrgIds5()).append("'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_BALANCE_PROJECT WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_BALANCE_PROJECT_ATT(PID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_BALANCE_PROJECT_ATT WHERE PID IN ("+ids+")");
        return sb.toString();
    }
}
