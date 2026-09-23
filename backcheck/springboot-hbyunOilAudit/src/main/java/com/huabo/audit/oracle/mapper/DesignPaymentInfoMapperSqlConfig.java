package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.DesignPaymentInfoEntity;
import com.hbfk.util.StringUtil;
import cn.hutool.core.date.DateUtil;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author Rui
 * @ClassName DesignPaymentInfoMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class DesignPaymentInfoMapperSqlConfig {

    public String selectByEntity( DesignPaymentInfoEntity designPaymentInfoEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_DESIGN_PAYMENT RS WHERE 1=1 ");


        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getPlanId())){
            sb.append("AND RS.PLAN_ID  LIKE'%"+designPaymentInfoEntity.getPlanId()+"%'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getContractNo())){
            sb.append("AND RS.CONTRACT_NO LIKE '%"+designPaymentInfoEntity.getContractNo()+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(DesignPaymentInfoEntity designPaymentInfoEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_DESIGN_PAYMENT RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getPlanId())){
            sb.append("AND RS.PLAN_ID = '"+designPaymentInfoEntity.getPlanId()+"'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getContractNo())){
            sb.append("AND RS.CONTRACT_NO = '"+designPaymentInfoEntity.getContractNo()+"'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(DesignPaymentInfoEntity designPaymentInfoEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_DESIGN_PAYMENT SET ");
        sb.append("PLAN_ID = '"+designPaymentInfoEntity.getPlanId()+"'");

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getContractNo())){
            sb.append(", CONTRACT_NO = '"+designPaymentInfoEntity.getContractNo()+"'");
        }

        if(StringUtil.isNotEmpty(  designPaymentInfoEntity.getOrgId() )){
            sb.append(", ORG_ID = '"+designPaymentInfoEntity.getOrgId()+"'");
        }

        if(designPaymentInfoEntity.getContractAmount() != null){
            sb.append(", CONTRACT_AMOUNT = '"+designPaymentInfoEntity.getContractAmount()+"'");
        }

        if(designPaymentInfoEntity.getSettlementAmount() != null){
            sb.append(", SETTLEMENT_AMOUNT = '"+designPaymentInfoEntity.getSettlementAmount()+"'");
        }

        if(designPaymentInfoEntity.getMaterialAmount() != null){
            sb.append(", MATERIAL_AMOUNT = '"+designPaymentInfoEntity.getMaterialAmount()+"'");
        }

        if(designPaymentInfoEntity.getPaidAmount() != null){
            sb.append(", PAID_AMOUNT = '"+designPaymentInfoEntity.getPaidAmount()+"'");
        }

        if(designPaymentInfoEntity.getBalancePayment() != null){
            sb.append(", BALANCE_PAYMENT = '"+designPaymentInfoEntity.getBalancePayment()+"'");
        }

        if(designPaymentInfoEntity.getFirstTrialTime() != null){
            sb.append(", FIRST_TRIAL_TIME = '"+DateUtil.format(designPaymentInfoEntity.getFirstTrialTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getFirstTrialAmount() != null){
            sb.append(", FIRST_TRIAL_AMOUNT = '"+designPaymentInfoEntity.getFirstTrialAmount()+"'");
        }

        if(designPaymentInfoEntity.getSecondTrialTime() != null){
            sb.append(", SECOND_TRIAL_TIME = '"+DateUtil.format(designPaymentInfoEntity.getSecondTrialTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getSecondTrialAmount() != null){
            sb.append(", SECOND_TRIAL_AMOUNT = '"+designPaymentInfoEntity.getSecondTrialAmount()+"'");
        }

        if(designPaymentInfoEntity.getDrawingDesignTime() != null){
            sb.append(", DRAWING_DESIGN_TIME = '"+DateUtil.format(designPaymentInfoEntity.getDrawingDesignTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getContractStartTime() != null){
            sb.append(", CONTRACT_START_TIME = '"+DateUtil.format(designPaymentInfoEntity.getContractStartTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getContractEndTime() != null){
            sb.append(", CONTRACT_END_TIME = '"+DateUtil.format(designPaymentInfoEntity.getContractEndTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getWorkStartTime() != null){
            sb.append(", WORK_START_TIME = '"+DateUtil.format(designPaymentInfoEntity.getWorkStartTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getWorkEndTime() != null){
            sb.append(", WORK_END_TIME = '"+DateUtil.format(designPaymentInfoEntity.getWorkEndTime(),"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getDelayTimes() != null){
            sb.append(", DELAY_TIMES = '"+designPaymentInfoEntity.getDelayTimes()+"'");
        }

        if(designPaymentInfoEntity.getDelayDays() != null){
            sb.append(", DELAY_DAYS = '"+designPaymentInfoEntity.getDelayDays()+"'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getDelayReason())){
            sb.append(", DELAY_REASON = '"+designPaymentInfoEntity.getDelayReason()+"'");
        }

        if(designPaymentInfoEntity.getArchiveTime() != null){
            sb.append(", ARCHIVE_TIME = '"+DateUtil.format(designPaymentInfoEntity.getArchiveTime(),"yyyy-MM-dd")+"'");
        }
        
        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getNo())){
        	sb.append(", NO = '"+designPaymentInfoEntity.getNo()+"'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getBiddingSituation())){
            sb.append(", BIDDING_SITUATION = '"+designPaymentInfoEntity.getBiddingSituation()+"'");
        }

        if(designPaymentInfoEntity.getIsConsistentConPlan() != null){
            sb.append(", IS_CONSISTENT_CON_PLAN = '"+designPaymentInfoEntity.getIsConsistentConPlan()+"'");
        }

        if(designPaymentInfoEntity.getIsConsistentConWork() != null){
            sb.append(", IS_CONSISTENT_CON_WORK = '"+designPaymentInfoEntity.getIsConsistentConWork()+"'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getRemark())){
            sb.append(", REMARK = '"+designPaymentInfoEntity.getRemark()+"'");
        }
// ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString1())) {
            sb.append(", RESERVEDSTRING1 = '").append(designPaymentInfoEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString2())) {
            sb.append(", RESERVEDSTRING2 = '").append(designPaymentInfoEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString3())) {
            sb.append(", RESERVEDSTRING3 = '").append(designPaymentInfoEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString4())) {
            sb.append(", RESERVEDSTRING4 = '").append(designPaymentInfoEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString5())) {
            sb.append(", RESERVEDSTRING5 = '").append(designPaymentInfoEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString6())) {
            sb.append(", RESERVEDSTRING6 = '").append(designPaymentInfoEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString7())) {
            sb.append(", RESERVEDSTRING7 = '").append(designPaymentInfoEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString8())) {
            sb.append(", RESERVEDSTRING8 = '").append(designPaymentInfoEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString9())) {
            sb.append(", RESERVEDSTRING9 = '").append(designPaymentInfoEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString10())) {
            sb.append(", RESERVEDSTRING10 = '").append(designPaymentInfoEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent1())) {
            sb.append(", RESERVEDCONTENT1 = '").append(designPaymentInfoEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent2())) {
            sb.append(", RESERVEDCONTENT2 = '").append(designPaymentInfoEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent3())) {
            sb.append(", RESERVEDCONTENT3 = '").append(designPaymentInfoEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent4())) {
            sb.append(", RESERVEDCONTENT4 = '").append(designPaymentInfoEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent5())) {
            sb.append(", RESERVEDCONTENT5 = '").append(designPaymentInfoEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent6())) {
            sb.append(", RESERVEDCONTENT6 = '").append(designPaymentInfoEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent7())) {
            sb.append(", RESERVEDCONTENT7 = '").append(designPaymentInfoEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent8())) {
            sb.append(", RESERVEDCONTENT8 = '").append(designPaymentInfoEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent9())) {
            sb.append(", RESERVEDCONTENT9 = '").append(designPaymentInfoEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent10())) {
            sb.append(", RESERVEDCONTENT10 = '").append(designPaymentInfoEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple1())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '").append(designPaymentInfoEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple2())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '").append(designPaymentInfoEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple3())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '").append(designPaymentInfoEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple4())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '").append(designPaymentInfoEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple5())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '").append(designPaymentInfoEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice1())) {
            sb.append(", RESERVEDMULTIPLECHOICE1 = '").append(designPaymentInfoEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice2())) {
            sb.append(", RESERVEDMULTIPLECHOICE2 = '").append(designPaymentInfoEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice3())) {
            sb.append(", RESERVEDMULTIPLECHOICE3 = '").append(designPaymentInfoEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice4())) {
            sb.append(", RESERVEDMULTIPLECHOICE4 = '").append(designPaymentInfoEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice5())) {
            sb.append(", RESERVEDMULTIPLECHOICE5 = '").append(designPaymentInfoEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个（格式化为 yyyy） ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (designPaymentInfoEntity.getReservedYearTime1() != null) {
            sb.append(", RESERVEDYEARTIME1 = '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime2() != null) {
            sb.append(", RESERVEDYEARTIME2 = '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime3() != null) {
            sb.append(", RESERVEDYEARTIME3 = '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime4() != null) {
            sb.append(", RESERVEDYEARTIME4 = '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime5() != null) {
            sb.append(", RESERVEDYEARTIME5 = '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (designPaymentInfoEntity.getReservedYearAccurateTime1() != null) {
            sb.append(", RESERVEDYEARACCURATETIME1 = '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime2() != null) {
            sb.append(", RESERVEDYEARACCURATETIME2 = '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime3() != null) {
            sb.append(", RESERVEDYEARACCURATETIME3 = '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime4() != null) {
            sb.append(", RESERVEDYEARACCURATETIME4 = '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime5() != null) {
            sb.append(", RESERVEDYEARACCURATETIME5 = '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (designPaymentInfoEntity.getReservedTime1() != null) {
            sb.append(", RESERVEDTIME1 = '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime2() != null) {
            sb.append(", RESERVEDTIME2 = '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime3() != null) {
            sb.append(", RESERVEDTIME3 = '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime4() != null) {
            sb.append(", RESERVEDTIME4 = '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime5() != null) {
            sb.append(", RESERVEDTIME5 = '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice1())) {
            sb.append(", RESERVEDSINGLECHOICE1 = '").append(designPaymentInfoEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice2())) {
            sb.append(", RESERVEDSINGLECHOICE2 = '").append(designPaymentInfoEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice3())) {
            sb.append(", RESERVEDSINGLECHOICE3 = '").append(designPaymentInfoEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice4())) {
            sb.append(", RESERVEDSINGLECHOICE4 = '").append(designPaymentInfoEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice5())) {
            sb.append(", RESERVEDSINGLECHOICE5 = '").append(designPaymentInfoEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice1())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice2())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice3())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice4())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice5())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (designPaymentInfoEntity.getReservedNum1() != null) {
            sb.append(", RESERVEDNUM1 = ").append(designPaymentInfoEntity.getReservedNum1());
        }
        if (designPaymentInfoEntity.getReservedNum2() != null) {
            sb.append(", RESERVEDNUM2 = ").append(designPaymentInfoEntity.getReservedNum2());
        }
        if (designPaymentInfoEntity.getReservedNum3() != null) {
            sb.append(", RESERVEDNUM3 = ").append(designPaymentInfoEntity.getReservedNum3());
        }
        if (designPaymentInfoEntity.getReservedNum4() != null) {
            sb.append(", RESERVEDNUM4 = ").append(designPaymentInfoEntity.getReservedNum4());
        }
        if (designPaymentInfoEntity.getReservedNum5() != null) {
            sb.append(", RESERVEDNUM5 = ").append(designPaymentInfoEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (designPaymentInfoEntity.getStaffId1() != null) {
            sb.append(", STAFFID1 = ").append(designPaymentInfoEntity.getStaffId1());
        }
        if (designPaymentInfoEntity.getStaffId2() != null) {
            sb.append(", STAFFID2 = ").append(designPaymentInfoEntity.getStaffId2());
        }
        if (designPaymentInfoEntity.getStaffId3() != null) {
            sb.append(", STAFFID3 = ").append(designPaymentInfoEntity.getStaffId3());
        }
        if (designPaymentInfoEntity.getStaffId4() != null) {
            sb.append(", STAFFID4 = ").append(designPaymentInfoEntity.getStaffId4());
        }
        if (designPaymentInfoEntity.getStaffId5() != null) {
            sb.append(", STAFFID5 = ").append(designPaymentInfoEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds1())) {
            sb.append(", STAFFIDS1 = '").append(designPaymentInfoEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds2())) {
            sb.append(", STAFFIDS2 = '").append(designPaymentInfoEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds3())) {
            sb.append(", STAFFIDS3 = '").append(designPaymentInfoEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds4())) {
            sb.append(", STAFFIDS4 = '").append(designPaymentInfoEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds5())) {
            sb.append(", STAFFIDS5 = '").append(designPaymentInfoEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (designPaymentInfoEntity.getOrgId1() != null) {
            sb.append(", ORGID1 = ").append(designPaymentInfoEntity.getOrgId1());
        }
        if (designPaymentInfoEntity.getOrgId2() != null) {
            sb.append(", ORGID2 = ").append(designPaymentInfoEntity.getOrgId2());
        }
        if (designPaymentInfoEntity.getOrgId3() != null) {
            sb.append(", ORGID3 = ").append(designPaymentInfoEntity.getOrgId3());
        }
        if (designPaymentInfoEntity.getOrgId4() != null) {
            sb.append(", ORGID4 = ").append(designPaymentInfoEntity.getOrgId4());
        }
        if (designPaymentInfoEntity.getOrgId5() != null) {
            sb.append(", ORGID5 = ").append(designPaymentInfoEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds1())) {
            sb.append(", ORGIDS1 = '").append(designPaymentInfoEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds2())) {
            sb.append(", ORGIDS2 = '").append(designPaymentInfoEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds3())) {
            sb.append(", ORGIDS3 = '").append(designPaymentInfoEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds4())) {
            sb.append(", ORGIDS4 = '").append(designPaymentInfoEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds5())) {
            sb.append(", ORGIDS5 = '").append(designPaymentInfoEntity.getOrgIds5()).append("'");
        }

        sb.append(" WHERE ID = '"+designPaymentInfoEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(DesignPaymentInfoEntity designPaymentInfoEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_DESIGN_PAYMENT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getPlanId())){
            colSb.append(", PLAN_ID");
            valSb.append(", '" + designPaymentInfoEntity.getPlanId() + "'");
        }
        
        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getNo())){
            colSb.append(", NO");
            valSb.append(", '" + designPaymentInfoEntity.getNo() + "'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getContractNo())){
            colSb.append(", CONTRACT_NO");
            valSb.append(", '" + designPaymentInfoEntity.getContractNo() + "'");
        }

        if(StringUtil.isNotEmpty(  designPaymentInfoEntity.getOrgId() )){
            colSb.append(", ORG_ID");
            valSb.append(", '" + designPaymentInfoEntity.getOrgId() + "'");
        }

        if(designPaymentInfoEntity.getContractAmount() != null){
            colSb.append(", CONTRACT_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getContractAmount() + "'");
        }

        if(designPaymentInfoEntity.getSettlementAmount() != null){
            colSb.append(", SETTLEMENT_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getSettlementAmount() + "'");
        }

        if(designPaymentInfoEntity.getMaterialAmount() != null){
            colSb.append(", MATERIAL_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getMaterialAmount() + "'");
        }

        if(designPaymentInfoEntity.getPaidAmount() != null){
            colSb.append(", PAID_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getPaidAmount() + "'");
        }

        if(designPaymentInfoEntity.getBalancePayment() != null){
            colSb.append(", BALANCE_PAYMENT");
            valSb.append(", '" + designPaymentInfoEntity.getBalancePayment() + "'");
        }

        if(designPaymentInfoEntity.getFirstTrialTime() != null){
            colSb.append(", FIRST_TRIAL_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getFirstTrialTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getFirstTrialAmount() != null){
            colSb.append(", FIRST_TRIAL_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getFirstTrialAmount() + "'");
        }

        if(designPaymentInfoEntity.getSecondTrialTime() != null){
            colSb.append(", SECOND_TRIAL_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getSecondTrialTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getSecondTrialAmount() != null){
            colSb.append(", SECOND_TRIAL_AMOUNT");
            valSb.append(", '" + designPaymentInfoEntity.getSecondTrialAmount() + "'");
        }

        if(designPaymentInfoEntity.getDrawingDesignTime() != null){
            colSb.append(", DRAWING_DESIGN_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getDrawingDesignTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getContractStartTime() != null){
            colSb.append(", CONTRACT_START_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getContractStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getContractEndTime() != null){
            colSb.append(", CONTRACT_END_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getContractEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getWorkStartTime() != null){
            colSb.append(", WORK_START_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getWorkStartTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getWorkEndTime() != null){
            colSb.append(", WORK_END_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getWorkEndTime() ,"yyyy-MM-dd")+"'");
        }

        if(designPaymentInfoEntity.getDelayTimes() != null){
            colSb.append(", DELAY_TIMES");
            valSb.append(", '" + designPaymentInfoEntity.getDelayTimes() + "'");
        }

        if(designPaymentInfoEntity.getDelayDays() != null){
            colSb.append(", DELAY_DAYS");
            valSb.append(", '" + designPaymentInfoEntity.getDelayDays() + "'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getDelayReason())){
            colSb.append(", DELAY_REASON");
            valSb.append(", '" + designPaymentInfoEntity.getDelayReason() + "'");
        }

        if(designPaymentInfoEntity.getArchiveTime() != null){
            colSb.append(", ARCHIVE_TIME");
            valSb.append(", '"+DateUtil.format(designPaymentInfoEntity.getArchiveTime() ,"yyyy-MM-dd")+"'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getBiddingSituation())){
            colSb.append(", BIDDING_SITUATION");
            valSb.append(", '" + designPaymentInfoEntity.getBiddingSituation() + "'");
        }

        if(designPaymentInfoEntity.getIsConsistentConPlan() != null){
            colSb.append(", IS_CONSISTENT_CON_PLAN");
            valSb.append(", '" + designPaymentInfoEntity.getIsConsistentConPlan() + "'");
        }

        if(designPaymentInfoEntity.getIsConsistentConWork() != null){
            colSb.append(", IS_CONSISTENT_CON_WORK");
            valSb.append(", '" + designPaymentInfoEntity.getIsConsistentConWork() + "'");
        }

        if(StringUtil.isNotEmpty(designPaymentInfoEntity.getRemark())){
            colSb.append(", REMARK");
            valSb.append(", '" + designPaymentInfoEntity.getRemark() + "'");
        }
// ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString1())) {
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString2())) {
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString3())) {
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString4())) {
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString5())) {
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString6())) {
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString7())) {
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString8())) {
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString9())) {
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedString10())) {
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent1())) {
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent2())) {
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent3())) {
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent4())) {
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent5())) {
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent6())) {
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent7())) {
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent8())) {
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent9())) {
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedContent10())) {
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple1())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple2())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple3())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple4())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownMultiple5())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice1())) {
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice2())) {
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice3())) {
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice4())) {
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedMultipleChoice5())) {
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个 ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (designPaymentInfoEntity.getReservedYearTime1() != null) {
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime2() != null) {
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime3() != null) {
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime4() != null) {
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearTime5() != null) {
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", '").append(sdfYear.format(designPaymentInfoEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (designPaymentInfoEntity.getReservedYearAccurateTime1() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime2() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime3() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime4() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedYearAccurateTime5() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", '").append(sdfDateTime.format(designPaymentInfoEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (designPaymentInfoEntity.getReservedTime1() != null) {
            colSb.append(", RESERVEDTIME1");
            valSb.append(", '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime1())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime2() != null) {
            colSb.append(", RESERVEDTIME2");
            valSb.append(", '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime2())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime3() != null) {
            colSb.append(", RESERVEDTIME3");
            valSb.append(", '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime3())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime4() != null) {
            colSb.append(", RESERVEDTIME4");
            valSb.append(", '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime4())).append("'");
        }
        if (designPaymentInfoEntity.getReservedTime5() != null) {
            colSb.append(", RESERVEDTIME5");
            valSb.append(", '").append(sdfDate.format(designPaymentInfoEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice1())) {
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice2())) {
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice3())) {
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice4())) {
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedSingleChoice5())) {
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice1())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice2())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice3())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice4())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getReservedDropdownSingleChoice5())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '").append(designPaymentInfoEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (designPaymentInfoEntity.getReservedNum1() != null) {
            colSb.append(", RESERVEDNUM1");
            valSb.append(", ").append(designPaymentInfoEntity.getReservedNum1());
        }
        if (designPaymentInfoEntity.getReservedNum2() != null) {
            colSb.append(", RESERVEDNUM2");
            valSb.append(", ").append(designPaymentInfoEntity.getReservedNum2());
        }
        if (designPaymentInfoEntity.getReservedNum3() != null) {
            colSb.append(", RESERVEDNUM3");
            valSb.append(", ").append(designPaymentInfoEntity.getReservedNum3());
        }
        if (designPaymentInfoEntity.getReservedNum4() != null) {
            colSb.append(", RESERVEDNUM4");
            valSb.append(", ").append(designPaymentInfoEntity.getReservedNum4());
        }
        if (designPaymentInfoEntity.getReservedNum5() != null) {
            colSb.append(", RESERVEDNUM5");
            valSb.append(", ").append(designPaymentInfoEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (designPaymentInfoEntity.getStaffId1() != null) {
            colSb.append(", STAFFID1");
            valSb.append(", ").append(designPaymentInfoEntity.getStaffId1());
        }
        if (designPaymentInfoEntity.getStaffId2() != null) {
            colSb.append(", STAFFID2");
            valSb.append(", ").append(designPaymentInfoEntity.getStaffId2());
        }
        if (designPaymentInfoEntity.getStaffId3() != null) {
            colSb.append(", STAFFID3");
            valSb.append(", ").append(designPaymentInfoEntity.getStaffId3());
        }
        if (designPaymentInfoEntity.getStaffId4() != null) {
            colSb.append(", STAFFID4");
            valSb.append(", ").append(designPaymentInfoEntity.getStaffId4());
        }
        if (designPaymentInfoEntity.getStaffId5() != null) {
            colSb.append(", STAFFID5");
            valSb.append(", ").append(designPaymentInfoEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds1())) {
            colSb.append(", STAFFIDS1");
            valSb.append(", '").append(designPaymentInfoEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds2())) {
            colSb.append(", STAFFIDS2");
            valSb.append(", '").append(designPaymentInfoEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds3())) {
            colSb.append(", STAFFIDS3");
            valSb.append(", '").append(designPaymentInfoEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds4())) {
            colSb.append(", STAFFIDS4");
            valSb.append(", '").append(designPaymentInfoEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getStaffIds5())) {
            colSb.append(", STAFFIDS5");
            valSb.append(", '").append(designPaymentInfoEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (designPaymentInfoEntity.getOrgId1() != null) {
            colSb.append(", ORGID1");
            valSb.append(", ").append(designPaymentInfoEntity.getOrgId1());
        }
        if (designPaymentInfoEntity.getOrgId2() != null) {
            colSb.append(", ORGID2");
            valSb.append(", ").append(designPaymentInfoEntity.getOrgId2());
        }
        if (designPaymentInfoEntity.getOrgId3() != null) {
            colSb.append(", ORGID3");
            valSb.append(", ").append(designPaymentInfoEntity.getOrgId3());
        }
        if (designPaymentInfoEntity.getOrgId4() != null) {
            colSb.append(", ORGID4");
            valSb.append(", ").append(designPaymentInfoEntity.getOrgId4());
        }
        if (designPaymentInfoEntity.getOrgId5() != null) {
            colSb.append(", ORGID5");
            valSb.append(", ").append(designPaymentInfoEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds1())) {
            colSb.append(", ORGIDS1");
            valSb.append(", '").append(designPaymentInfoEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds2())) {
            colSb.append(", ORGIDS2");
            valSb.append(", '").append(designPaymentInfoEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds3())) {
            colSb.append(", ORGIDS3");
            valSb.append(", '").append(designPaymentInfoEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds4())) {
            colSb.append(", ORGIDS4");
            valSb.append(", '").append(designPaymentInfoEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(designPaymentInfoEntity.getOrgIds5())) {
            colSb.append(", ORGIDS5");
            valSb.append(", '").append(designPaymentInfoEntity.getOrgIds5()).append("'");
        }

        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_DESIGN_PAYMENT WHERE ID IN (" + ids+")");
        return sb.toString();
    }

    public String insertAttachments(BigDecimal id, String attachmentId){
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO TBL_YQNS_DESIGN_PAYMENT_ATT(DPID,ATTID) VALUES ("+ id + ","+ attachmentId +")");
        return sb.toString();
    }

    public String deleteAttachmentByIds(String ids){
        StringBuffer sb = new StringBuffer("DELETE FROM TBL_YQNS_DESIGN_PAYMENT_ATT WHERE DPID IN ("+ids+")");
        return sb.toString();
    }












}
