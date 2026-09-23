package com.huabo.audit.oracle.mapper;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.InvestigationEntity;
import com.hbfk.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author Rui
 * @ClassName InvestigationMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class InvestigationMapperSqlConfig {

    public String selectByEntity( InvestigationEntity investigationEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_INVESTIGATION RS WHERE 1=1 ");


        if(StringUtil.isNotEmpty(investigationEntity.getPlanNo())){
            sb.append("AND RS.PLAN_NO LIKE '%"+investigationEntity.getPlanNo()+"%'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+investigationEntity.getProjectName()+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(InvestigationEntity investigationEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_INVESTIGATION RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(investigationEntity.getPlanNo())){
            sb.append("AND RS.PLAN_NO = '"+investigationEntity.getPlanNo()+"'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+investigationEntity.getProjectName()+"%'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(InvestigationEntity investigationEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_INVESTIGATION SET ");
        sb.append("PROJECT_NAME = '"+investigationEntity.getProjectName()+"'");

        if(StringUtil.isNotEmpty(investigationEntity.getContractNo())){
            sb.append(", CONTRACT_NO = '"+investigationEntity.getContractNo()+"'");
        }
 
        if(StringUtil.isNotEmpty(investigationEntity.getNo())){
            sb.append(", NO = "+investigationEntity.getNo());
        }

        if(StringUtil.isNotEmpty(investigationEntity.getPlanNo())){
            sb.append(", PLAN_NO = '"+investigationEntity.getPlanNo()+"'");

        }

        if(StringUtil.isNotEmpty(investigationEntity.getGpNo())){
            sb.append(", GP_NO = '"+investigationEntity.getGpNo()+"'");

        }

        if(investigationEntity.getSettlementAmount() != null){
            sb.append(", SETTLEMENT_AMOUNT = '"+investigationEntity.getSettlementAmount()+"'");
        }

        if(investigationEntity.getMaterialAmount() != null){
            sb.append(", MATERIAL_AMOUNT = '"+investigationEntity.getMaterialAmount()+"'");
        }


        if(investigationEntity.getContractStartTime() != null){
            sb.append(", CONTRACT_START_TIME = '"+DateUtil.format(investigationEntity.getContractStartTime(),"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getContractEndTime() != null){
            sb.append(", CONTRACT_END_TIME = '"+DateUtil.format(investigationEntity.getContractEndTime(),"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getWorkStartTime() != null){
            sb.append(", WORK_START_TIME = '"+ DateUtil.format(investigationEntity.getWorkStartTime(),"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getWorkEndTime() != null){
            sb.append(", WORK_END_TIME = '"+DateUtil.format(investigationEntity.getWorkEndTime(),"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getConstructionContent())){
            sb.append(", CONSTRUCTION_CONTENT = '"+investigationEntity.getConstructionContent()+"'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getSettlementProgress())){
            sb.append(", SETTLEMENT_PROGRESS = '"+investigationEntity.getSettlementProgress()+"'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getProjectAddress())){
            sb.append(", PROJECT_ADDRESS = '"+investigationEntity.getProjectAddress()+"'");
        }

        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString1())) {
            sb.append(", RESERVEDSTRING1 = '").append(investigationEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString2())) {
            sb.append(", RESERVEDSTRING2 = '").append(investigationEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString3())) {
            sb.append(", RESERVEDSTRING3 = '").append(investigationEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString4())) {
            sb.append(", RESERVEDSTRING4 = '").append(investigationEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString5())) {
            sb.append(", RESERVEDSTRING5 = '").append(investigationEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString6())) {
            sb.append(", RESERVEDSTRING6 = '").append(investigationEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString7())) {
            sb.append(", RESERVEDSTRING7 = '").append(investigationEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString8())) {
            sb.append(", RESERVEDSTRING8 = '").append(investigationEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString9())) {
            sb.append(", RESERVEDSTRING9 = '").append(investigationEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString10())) {
            sb.append(", RESERVEDSTRING10 = '").append(investigationEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent1())) {
            sb.append(", RESERVEDCONTENT1 = '").append(investigationEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent2())) {
            sb.append(", RESERVEDCONTENT2 = '").append(investigationEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent3())) {
            sb.append(", RESERVEDCONTENT3 = '").append(investigationEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent4())) {
            sb.append(", RESERVEDCONTENT4 = '").append(investigationEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent5())) {
            sb.append(", RESERVEDCONTENT5 = '").append(investigationEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent6())) {
            sb.append(", RESERVEDCONTENT6 = '").append(investigationEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent7())) {
            sb.append(", RESERVEDCONTENT7 = '").append(investigationEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent8())) {
            sb.append(", RESERVEDCONTENT8 = '").append(investigationEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent9())) {
            sb.append(", RESERVEDCONTENT9 = '").append(investigationEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent10())) {
            sb.append(", RESERVEDCONTENT10 = '").append(investigationEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple1())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '").append(investigationEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple2())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '").append(investigationEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple3())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '").append(investigationEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple4())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '").append(investigationEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple5())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '").append(investigationEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice1())) {
            sb.append(", RESERVEDMULTIPLECHOICE1 = '").append(investigationEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice2())) {
            sb.append(", RESERVEDMULTIPLECHOICE2 = '").append(investigationEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice3())) {
            sb.append(", RESERVEDMULTIPLECHOICE3 = '").append(investigationEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice4())) {
            sb.append(", RESERVEDMULTIPLECHOICE4 = '").append(investigationEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice5())) {
            sb.append(", RESERVEDMULTIPLECHOICE5 = '").append(investigationEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个（格式化为 yyyy） ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (investigationEntity.getReservedYearTime1() != null) {
            sb.append(", RESERVEDYEARTIME1 = '").append(sdfYear.format(investigationEntity.getReservedYearTime1())).append("'");
        }
        if (investigationEntity.getReservedYearTime2() != null) {
            sb.append(", RESERVEDYEARTIME2 = '").append(sdfYear.format(investigationEntity.getReservedYearTime2())).append("'");
        }
        if (investigationEntity.getReservedYearTime3() != null) {
            sb.append(", RESERVEDYEARTIME3 = '").append(sdfYear.format(investigationEntity.getReservedYearTime3())).append("'");
        }
        if (investigationEntity.getReservedYearTime4() != null) {
            sb.append(", RESERVEDYEARTIME4 = '").append(sdfYear.format(investigationEntity.getReservedYearTime4())).append("'");
        }
        if (investigationEntity.getReservedYearTime5() != null) {
            sb.append(", RESERVEDYEARTIME5 = '").append(sdfYear.format(investigationEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (investigationEntity.getReservedYearAccurateTime1() != null) {
            sb.append(", RESERVEDYEARACCURATETIME1 = '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime2() != null) {
            sb.append(", RESERVEDYEARACCURATETIME2 = '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime3() != null) {
            sb.append(", RESERVEDYEARACCURATETIME3 = '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime4() != null) {
            sb.append(", RESERVEDYEARACCURATETIME4 = '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime5() != null) {
            sb.append(", RESERVEDYEARACCURATETIME5 = '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (investigationEntity.getReservedTime1() != null) {
            sb.append(", RESERVEDTIME1 = '").append(sdfDate.format(investigationEntity.getReservedTime1())).append("'");
        }
        if (investigationEntity.getReservedTime2() != null) {
            sb.append(", RESERVEDTIME2 = '").append(sdfDate.format(investigationEntity.getReservedTime2())).append("'");
        }
        if (investigationEntity.getReservedTime3() != null) {
            sb.append(", RESERVEDTIME3 = '").append(sdfDate.format(investigationEntity.getReservedTime3())).append("'");
        }
        if (investigationEntity.getReservedTime4() != null) {
            sb.append(", RESERVEDTIME4 = '").append(sdfDate.format(investigationEntity.getReservedTime4())).append("'");
        }
        if (investigationEntity.getReservedTime5() != null) {
            sb.append(", RESERVEDTIME5 = '").append(sdfDate.format(investigationEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice1())) {
            sb.append(", RESERVEDSINGLECHOICE1 = '").append(investigationEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice2())) {
            sb.append(", RESERVEDSINGLECHOICE2 = '").append(investigationEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice3())) {
            sb.append(", RESERVEDSINGLECHOICE3 = '").append(investigationEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice4())) {
            sb.append(", RESERVEDSINGLECHOICE4 = '").append(investigationEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice5())) {
            sb.append(", RESERVEDSINGLECHOICE5 = '").append(investigationEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice1())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '").append(investigationEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice2())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '").append(investigationEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice3())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '").append(investigationEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice4())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '").append(investigationEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice5())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '").append(investigationEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (investigationEntity.getReservedNum1() != null) {
            sb.append(", RESERVEDNUM1 = ").append(investigationEntity.getReservedNum1());
        }
        if (investigationEntity.getReservedNum2() != null) {
            sb.append(", RESERVEDNUM2 = ").append(investigationEntity.getReservedNum2());
        }
        if (investigationEntity.getReservedNum3() != null) {
            sb.append(", RESERVEDNUM3 = ").append(investigationEntity.getReservedNum3());
        }
        if (investigationEntity.getReservedNum4() != null) {
            sb.append(", RESERVEDNUM4 = ").append(investigationEntity.getReservedNum4());
        }
        if (investigationEntity.getReservedNum5() != null) {
            sb.append(", RESERVEDNUM5 = ").append(investigationEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (investigationEntity.getStaffId1() != null) {
            sb.append(", STAFFID1 = ").append(investigationEntity.getStaffId1());
        }
        if (investigationEntity.getStaffId2() != null) {
            sb.append(", STAFFID2 = ").append(investigationEntity.getStaffId2());
        }
        if (investigationEntity.getStaffId3() != null) {
            sb.append(", STAFFID3 = ").append(investigationEntity.getStaffId3());
        }
        if (investigationEntity.getStaffId4() != null) {
            sb.append(", STAFFID4 = ").append(investigationEntity.getStaffId4());
        }
        if (investigationEntity.getStaffId5() != null) {
            sb.append(", STAFFID5 = ").append(investigationEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds1())) {
            sb.append(", STAFFIDS1 = '").append(investigationEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds2())) {
            sb.append(", STAFFIDS2 = '").append(investigationEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds3())) {
            sb.append(", STAFFIDS3 = '").append(investigationEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds4())) {
            sb.append(", STAFFIDS4 = '").append(investigationEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds5())) {
            sb.append(", STAFFIDS5 = '").append(investigationEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (investigationEntity.getOrgId1() != null) {
            sb.append(", ORGID1 = ").append(investigationEntity.getOrgId1());
        }
        if (investigationEntity.getOrgId2() != null) {
            sb.append(", ORGID2 = ").append(investigationEntity.getOrgId2());
        }
        if (investigationEntity.getOrgId3() != null) {
            sb.append(", ORGID3 = ").append(investigationEntity.getOrgId3());
        }
        if (investigationEntity.getOrgId4() != null) {
            sb.append(", ORGID4 = ").append(investigationEntity.getOrgId4());
        }
        if (investigationEntity.getOrgId5() != null) {
            sb.append(", ORGID5 = ").append(investigationEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds1())) {
            sb.append(", ORGIDS1 = '").append(investigationEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds2())) {
            sb.append(", ORGIDS2 = '").append(investigationEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds3())) {
            sb.append(", ORGIDS3 = '").append(investigationEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds4())) {
            sb.append(", ORGIDS4 = '").append(investigationEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds5())) {
            sb.append(", ORGIDS5 = '").append(investigationEntity.getOrgIds5()).append("'");
        }
        sb.append(" WHERE ID = '"+investigationEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(InvestigationEntity investigationEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_INVESTIGATION (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(investigationEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + investigationEntity.getProjectName() + "'");
        }
        
        if(StringUtil.isNotEmpty(investigationEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + investigationEntity.getNo() + "'");
        }
        
        if(StringUtil.isNotEmpty(investigationEntity.getPlanNo())){
            colSb.append(", PLAN_NO");
            valSb.append(", '" + investigationEntity.getPlanNo() + "'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getGpNo())){
            colSb.append(", GP_NO");
            valSb.append(", '" + investigationEntity.getGpNo() + "'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getContractNo())){
            colSb.append(", CONTRACT_NO");
            valSb.append(", '" + investigationEntity.getContractNo() + "'");
        }



        if(investigationEntity.getSettlementAmount() != null){
            colSb.append(", SETTLEMENT_AMOUNT");
            valSb.append(", '" + investigationEntity.getSettlementAmount() + "'");
        }

        if(investigationEntity.getMaterialAmount() != null){
            colSb.append(", MATERIAL_AMOUNT");
            valSb.append(", '" + investigationEntity.getMaterialAmount() + "'");
        }


        if(investigationEntity.getContractStartTime() != null){
            colSb.append(", CONTRACT_START_TIME");
            valSb.append(", '"+DateUtil.format(investigationEntity.getContractStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getContractEndTime() != null){
            colSb.append(", CONTRACT_END_TIME");
            valSb.append(", '"+DateUtil.format(investigationEntity.getContractEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getWorkStartTime() != null){
            colSb.append(", WORK_START_TIME");
            valSb.append(", '"+DateUtil.format(investigationEntity.getWorkStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(investigationEntity.getWorkEndTime() != null){
            colSb.append(", WORK_END_TIME");
            valSb.append(", '"+DateUtil.format(investigationEntity.getWorkEndTime() ,"yyyy-MM-dd")+"'");
        }


        if(StringUtil.isNotEmpty(investigationEntity.getConstructionContent())){
            colSb.append(", CONSTRUCTION_CONTENT");
            valSb.append(", '" + investigationEntity.getConstructionContent() + "'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getProjectAddress())){
            colSb.append(", PROJECT_ADDRESS");
            valSb.append(", '" + investigationEntity.getProjectAddress() + "'");
        }

        if(StringUtil.isNotEmpty(investigationEntity.getSettlementProgress())){
            colSb.append(", SETTLEMENT_PROGRESS");
            valSb.append(", '" + investigationEntity.getSettlementProgress() + "'");
        }
        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString1())) {
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '").append(investigationEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString2())) {
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '").append(investigationEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString3())) {
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '").append(investigationEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString4())) {
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '").append(investigationEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString5())) {
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '").append(investigationEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString6())) {
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '").append(investigationEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString7())) {
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '").append(investigationEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString8())) {
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '").append(investigationEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString9())) {
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '").append(investigationEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedString10())) {
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '").append(investigationEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent1())) {
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '").append(investigationEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent2())) {
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '").append(investigationEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent3())) {
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '").append(investigationEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent4())) {
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '").append(investigationEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent5())) {
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '").append(investigationEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent6())) {
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '").append(investigationEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent7())) {
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '").append(investigationEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent8())) {
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '").append(investigationEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent9())) {
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '").append(investigationEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedContent10())) {
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '").append(investigationEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple1())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '").append(investigationEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple2())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '").append(investigationEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple3())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '").append(investigationEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple4())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '").append(investigationEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownMultiple5())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '").append(investigationEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice1())) {
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '").append(investigationEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice2())) {
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '").append(investigationEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice3())) {
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '").append(investigationEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice4())) {
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '").append(investigationEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedMultipleChoice5())) {
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '").append(investigationEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个 ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (investigationEntity.getReservedYearTime1() != null) {
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", '").append(sdfYear.format(investigationEntity.getReservedYearTime1())).append("'");
        }
        if (investigationEntity.getReservedYearTime2() != null) {
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", '").append(sdfYear.format(investigationEntity.getReservedYearTime2())).append("'");
        }
        if (investigationEntity.getReservedYearTime3() != null) {
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", '").append(sdfYear.format(investigationEntity.getReservedYearTime3())).append("'");
        }
        if (investigationEntity.getReservedYearTime4() != null) {
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", '").append(sdfYear.format(investigationEntity.getReservedYearTime4())).append("'");
        }
        if (investigationEntity.getReservedYearTime5() != null) {
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", '").append(sdfYear.format(investigationEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (investigationEntity.getReservedYearAccurateTime1() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime2() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime3() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime4() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (investigationEntity.getReservedYearAccurateTime5() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", '").append(sdfDateTime.format(investigationEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (investigationEntity.getReservedTime1() != null) {
            colSb.append(", RESERVEDTIME1");
            valSb.append(", '").append(sdfDate.format(investigationEntity.getReservedTime1())).append("'");
        }
        if (investigationEntity.getReservedTime2() != null) {
            colSb.append(", RESERVEDTIME2");
            valSb.append(", '").append(sdfDate.format(investigationEntity.getReservedTime2())).append("'");
        }
        if (investigationEntity.getReservedTime3() != null) {
            colSb.append(", RESERVEDTIME3");
            valSb.append(", '").append(sdfDate.format(investigationEntity.getReservedTime3())).append("'");
        }
        if (investigationEntity.getReservedTime4() != null) {
            colSb.append(", RESERVEDTIME4");
            valSb.append(", '").append(sdfDate.format(investigationEntity.getReservedTime4())).append("'");
        }
        if (investigationEntity.getReservedTime5() != null) {
            colSb.append(", RESERVEDTIME5");
            valSb.append(", '").append(sdfDate.format(investigationEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice1())) {
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '").append(investigationEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice2())) {
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '").append(investigationEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice3())) {
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '").append(investigationEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice4())) {
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '").append(investigationEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedSingleChoice5())) {
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '").append(investigationEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice1())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '").append(investigationEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice2())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '").append(investigationEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice3())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '").append(investigationEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice4())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '").append(investigationEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getReservedDropdownSingleChoice5())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '").append(investigationEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (investigationEntity.getReservedNum1() != null) {
            colSb.append(", RESERVEDNUM1");
            valSb.append(", ").append(investigationEntity.getReservedNum1());
        }
        if (investigationEntity.getReservedNum2() != null) {
            colSb.append(", RESERVEDNUM2");
            valSb.append(", ").append(investigationEntity.getReservedNum2());
        }
        if (investigationEntity.getReservedNum3() != null) {
            colSb.append(", RESERVEDNUM3");
            valSb.append(", ").append(investigationEntity.getReservedNum3());
        }
        if (investigationEntity.getReservedNum4() != null) {
            colSb.append(", RESERVEDNUM4");
            valSb.append(", ").append(investigationEntity.getReservedNum4());
        }
        if (investigationEntity.getReservedNum5() != null) {
            colSb.append(", RESERVEDNUM5");
            valSb.append(", ").append(investigationEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (investigationEntity.getStaffId1() != null) {
            colSb.append(", STAFFID1");
            valSb.append(", ").append(investigationEntity.getStaffId1());
        }
        if (investigationEntity.getStaffId2() != null) {
            colSb.append(", STAFFID2");
            valSb.append(", ").append(investigationEntity.getStaffId2());
        }
        if (investigationEntity.getStaffId3() != null) {
            colSb.append(", STAFFID3");
            valSb.append(", ").append(investigationEntity.getStaffId3());
        }
        if (investigationEntity.getStaffId4() != null) {
            colSb.append(", STAFFID4");
            valSb.append(", ").append(investigationEntity.getStaffId4());
        }
        if (investigationEntity.getStaffId5() != null) {
            colSb.append(", STAFFID5");
            valSb.append(", ").append(investigationEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds1())) {
            colSb.append(", STAFFIDS1");
            valSb.append(", '").append(investigationEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds2())) {
            colSb.append(", STAFFIDS2");
            valSb.append(", '").append(investigationEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds3())) {
            colSb.append(", STAFFIDS3");
            valSb.append(", '").append(investigationEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds4())) {
            colSb.append(", STAFFIDS4");
            valSb.append(", '").append(investigationEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getStaffIds5())) {
            colSb.append(", STAFFIDS5");
            valSb.append(", '").append(investigationEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (investigationEntity.getOrgId1() != null) {
            colSb.append(", ORGID1");
            valSb.append(", ").append(investigationEntity.getOrgId1());
        }
        if (investigationEntity.getOrgId2() != null) {
            colSb.append(", ORGID2");
            valSb.append(", ").append(investigationEntity.getOrgId2());
        }
        if (investigationEntity.getOrgId3() != null) {
            colSb.append(", ORGID3");
            valSb.append(", ").append(investigationEntity.getOrgId3());
        }
        if (investigationEntity.getOrgId4() != null) {
            colSb.append(", ORGID4");
            valSb.append(", ").append(investigationEntity.getOrgId4());
        }
        if (investigationEntity.getOrgId5() != null) {
            colSb.append(", ORGID5");
            valSb.append(", ").append(investigationEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds1())) {
            colSb.append(", ORGIDS1");
            valSb.append(", '").append(investigationEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds2())) {
            colSb.append(", ORGIDS2");
            valSb.append(", '").append(investigationEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds3())) {
            colSb.append(", ORGIDS3");
            valSb.append(", '").append(investigationEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds4())) {
            colSb.append(", ORGIDS4");
            valSb.append(", '").append(investigationEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(investigationEntity.getOrgIds5())) {
            colSb.append(", ORGIDS5");
            valSb.append(", '").append(investigationEntity.getOrgIds5()).append("'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_INVESTIGATION WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_INVESTIGATION_ATT(INVESTIGATE_ID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_INVESTIGATION_ATT WHERE INVESTIGATE_ID IN ("+ids+")");
        return sb.toString();
    }
}
