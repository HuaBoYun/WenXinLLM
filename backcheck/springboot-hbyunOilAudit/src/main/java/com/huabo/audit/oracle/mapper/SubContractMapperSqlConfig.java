package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.SubContractEntity;
import com.hbfk.util.StringUtil;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * @author Rui
 * @ClassName SubContractMapperSqlConfig
 * @Description
 * @DATE 2023/9/30
 */
public class SubContractMapperSqlConfig {

    public String selectByEntity( SubContractEntity subContractEntity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_SUB_CONTRACT RS WHERE 1=1 ");

        if(StringUtil.isNotEmpty(subContractEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+subContractEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubContractNo())){
            sb.append("AND RS.SUB_CONTRACT_NO LIKE '%"+subContractEntity.getSubContractNo()+"%'");
        }
        
        sb.append(" ORDER BY RS.ID DESC ");

        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public String selectCountByEntity(SubContractEntity subContractEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_SUB_CONTRACT RS  WHERE 1=1 ");


        if(StringUtil.isNotEmpty(subContractEntity.getProjectName())){
            sb.append("AND RS.PROJECT_NAME LIKE '%"+subContractEntity.getProjectName()+"%'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubContractNo())){
            sb.append("AND RS.CONTRACT_NO = '"+subContractEntity.getSubContractNo()+"'");
        }

        sb.append(")");
        return sb.toString();
    }

    public String updateEntity(SubContractEntity subContractEntity){
        StringBuffer sb = new StringBuffer();
        sb.append("UPDATE TBL_YQNS_SUB_CONTRACT SET ");
        sb.append("PROJECT_NAME = '"+subContractEntity.getProjectName()+"'");

        if(StringUtil.isNotEmpty(subContractEntity.getGeneralContractNo())){
            sb.append(", GENERAL_CONTRACT_NO = '"+subContractEntity.getGeneralContractNo()+"'");
        }
        
        if(StringUtil.isNotEmpty(subContractEntity.getNo())){
            sb.append(", NO = '"+subContractEntity.getNo()+"'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubContractNo())){
            sb.append(", SUB_CONTRACT_NO = '"+subContractEntity.getSubContractNo()+"'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getSubOrgId() )){
            sb.append(", SUB_ORG_ID = '"+subContractEntity.getSubOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubType())){
            sb.append(", SUB_TYPE = '"+subContractEntity.getSubType()+"'");
        }

        if(subContractEntity.getSubContractAmount() != null){
            sb.append(", SUB_CONTRACT_AMOUNT = '"+subContractEntity.getSubContractAmount()+"'");
        }

        if(subContractEntity.getSubSettlementAmount() != null){
            sb.append(", SUB_SETTLEMENT_AMOUNT = '"+subContractEntity.getSubSettlementAmount()+"'");
        }

        if(subContractEntity.getQuota() != null){
            sb.append(", QUOTA = '"+subContractEntity.getQuota()+"'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getProjectAddress())){
            sb.append(", PROJECT_ADDRESS = '"+subContractEntity.getProjectAddress()+"'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubCheckMode())){
            sb.append(", SUB_CHECK_MODE = '"+subContractEntity.getSubCheckMode()+"'");
        }

        if(subContractEntity.getHasProceedings() != null){
            sb.append(", HAS_PROCEEDINGS = '"+subContractEntity.getHasProceedings()+"'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getBuildOrgId() )){
            sb.append(", BUILD_ORG_ID = '"+subContractEntity.getBuildOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getGeneralOrgId() )){
            sb.append(", GENERAL_ORG_ID = '"+subContractEntity.getGeneralOrgId()+"'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getAuditPersonId() )){
            sb.append(", AUDIT_PERSON_ID = '"+subContractEntity.getAuditPersonId()+"'");
        }

        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString1())) {
            sb.append(", RESERVEDSTRING1 = '").append(subContractEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString2())) {
            sb.append(", RESERVEDSTRING2 = '").append(subContractEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString3())) {
            sb.append(", RESERVEDSTRING3 = '").append(subContractEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString4())) {
            sb.append(", RESERVEDSTRING4 = '").append(subContractEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString5())) {
            sb.append(", RESERVEDSTRING5 = '").append(subContractEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString6())) {
            sb.append(", RESERVEDSTRING6 = '").append(subContractEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString7())) {
            sb.append(", RESERVEDSTRING7 = '").append(subContractEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString8())) {
            sb.append(", RESERVEDSTRING8 = '").append(subContractEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString9())) {
            sb.append(", RESERVEDSTRING9 = '").append(subContractEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString10())) {
            sb.append(", RESERVEDSTRING10 = '").append(subContractEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent1())) {
            sb.append(", RESERVEDCONTENT1 = '").append(subContractEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent2())) {
            sb.append(", RESERVEDCONTENT2 = '").append(subContractEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent3())) {
            sb.append(", RESERVEDCONTENT3 = '").append(subContractEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent4())) {
            sb.append(", RESERVEDCONTENT4 = '").append(subContractEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent5())) {
            sb.append(", RESERVEDCONTENT5 = '").append(subContractEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent6())) {
            sb.append(", RESERVEDCONTENT6 = '").append(subContractEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent7())) {
            sb.append(", RESERVEDCONTENT7 = '").append(subContractEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent8())) {
            sb.append(", RESERVEDCONTENT8 = '").append(subContractEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent9())) {
            sb.append(", RESERVEDCONTENT9 = '").append(subContractEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent10())) {
            sb.append(", RESERVEDCONTENT10 = '").append(subContractEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple1())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE1 = '").append(subContractEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple2())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE2 = '").append(subContractEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple3())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE3 = '").append(subContractEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple4())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE4 = '").append(subContractEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple5())) {
            sb.append(", RESERVEDDROPDOWNMULTIPLE5 = '").append(subContractEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice1())) {
            sb.append(", RESERVEDMULTIPLECHOICE1 = '").append(subContractEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice2())) {
            sb.append(", RESERVEDMULTIPLECHOICE2 = '").append(subContractEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice3())) {
            sb.append(", RESERVEDMULTIPLECHOICE3 = '").append(subContractEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice4())) {
            sb.append(", RESERVEDMULTIPLECHOICE4 = '").append(subContractEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice5())) {
            sb.append(", RESERVEDMULTIPLECHOICE5 = '").append(subContractEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个（格式化为 yyyy） ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (subContractEntity.getReservedYearTime1() != null) {
            sb.append(", RESERVEDYEARTIME1 = '").append(sdfYear.format(subContractEntity.getReservedYearTime1())).append("'");
        }
        if (subContractEntity.getReservedYearTime2() != null) {
            sb.append(", RESERVEDYEARTIME2 = '").append(sdfYear.format(subContractEntity.getReservedYearTime2())).append("'");
        }
        if (subContractEntity.getReservedYearTime3() != null) {
            sb.append(", RESERVEDYEARTIME3 = '").append(sdfYear.format(subContractEntity.getReservedYearTime3())).append("'");
        }
        if (subContractEntity.getReservedYearTime4() != null) {
            sb.append(", RESERVEDYEARTIME4 = '").append(sdfYear.format(subContractEntity.getReservedYearTime4())).append("'");
        }
        if (subContractEntity.getReservedYearTime5() != null) {
            sb.append(", RESERVEDYEARTIME5 = '").append(sdfYear.format(subContractEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (subContractEntity.getReservedYearAccurateTime1() != null) {
            sb.append(", RESERVEDYEARACCURATETIME1 = '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime2() != null) {
            sb.append(", RESERVEDYEARACCURATETIME2 = '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime3() != null) {
            sb.append(", RESERVEDYEARACCURATETIME3 = '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime4() != null) {
            sb.append(", RESERVEDYEARACCURATETIME4 = '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime5() != null) {
            sb.append(", RESERVEDYEARACCURATETIME5 = '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (subContractEntity.getReservedTime1() != null) {
            sb.append(", RESERVEDTIME1 = '").append(sdfDate.format(subContractEntity.getReservedTime1())).append("'");
        }
        if (subContractEntity.getReservedTime2() != null) {
            sb.append(", RESERVEDTIME2 = '").append(sdfDate.format(subContractEntity.getReservedTime2())).append("'");
        }
        if (subContractEntity.getReservedTime3() != null) {
            sb.append(", RESERVEDTIME3 = '").append(sdfDate.format(subContractEntity.getReservedTime3())).append("'");
        }
        if (subContractEntity.getReservedTime4() != null) {
            sb.append(", RESERVEDTIME4 = '").append(sdfDate.format(subContractEntity.getReservedTime4())).append("'");
        }
        if (subContractEntity.getReservedTime5() != null) {
            sb.append(", RESERVEDTIME5 = '").append(sdfDate.format(subContractEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice1())) {
            sb.append(", RESERVEDSINGLECHOICE1 = '").append(subContractEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice2())) {
            sb.append(", RESERVEDSINGLECHOICE2 = '").append(subContractEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice3())) {
            sb.append(", RESERVEDSINGLECHOICE3 = '").append(subContractEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice4())) {
            sb.append(", RESERVEDSINGLECHOICE4 = '").append(subContractEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice5())) {
            sb.append(", RESERVEDSINGLECHOICE5 = '").append(subContractEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice1())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '").append(subContractEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice2())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '").append(subContractEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice3())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '").append(subContractEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice4())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '").append(subContractEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice5())) {
            sb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '").append(subContractEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (subContractEntity.getReservedNum1() != null) {
            sb.append(", RESERVEDNUM1 = ").append(subContractEntity.getReservedNum1());
        }
        if (subContractEntity.getReservedNum2() != null) {
            sb.append(", RESERVEDNUM2 = ").append(subContractEntity.getReservedNum2());
        }
        if (subContractEntity.getReservedNum3() != null) {
            sb.append(", RESERVEDNUM3 = ").append(subContractEntity.getReservedNum3());
        }
        if (subContractEntity.getReservedNum4() != null) {
            sb.append(", RESERVEDNUM4 = ").append(subContractEntity.getReservedNum4());
        }
        if (subContractEntity.getReservedNum5() != null) {
            sb.append(", RESERVEDNUM5 = ").append(subContractEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (subContractEntity.getStaffId1() != null) {
            sb.append(", STAFFID1 = ").append(subContractEntity.getStaffId1());
        }
        if (subContractEntity.getStaffId2() != null) {
            sb.append(", STAFFID2 = ").append(subContractEntity.getStaffId2());
        }
        if (subContractEntity.getStaffId3() != null) {
            sb.append(", STAFFID3 = ").append(subContractEntity.getStaffId3());
        }
        if (subContractEntity.getStaffId4() != null) {
            sb.append(", STAFFID4 = ").append(subContractEntity.getStaffId4());
        }
        if (subContractEntity.getStaffId5() != null) {
            sb.append(", STAFFID5 = ").append(subContractEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds1())) {
            sb.append(", STAFFIDS1 = '").append(subContractEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds2())) {
            sb.append(", STAFFIDS2 = '").append(subContractEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds3())) {
            sb.append(", STAFFIDS3 = '").append(subContractEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds4())) {
            sb.append(", STAFFIDS4 = '").append(subContractEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds5())) {
            sb.append(", STAFFIDS5 = '").append(subContractEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (subContractEntity.getOrgId1() != null) {
            sb.append(", ORGID1 = ").append(subContractEntity.getOrgId1());
        }
        if (subContractEntity.getOrgId2() != null) {
            sb.append(", ORGID2 = ").append(subContractEntity.getOrgId2());
        }
        if (subContractEntity.getOrgId3() != null) {
            sb.append(", ORGID3 = ").append(subContractEntity.getOrgId3());
        }
        if (subContractEntity.getOrgId4() != null) {
            sb.append(", ORGID4 = ").append(subContractEntity.getOrgId4());
        }
        if (subContractEntity.getOrgId5() != null) {
            sb.append(", ORGID5 = ").append(subContractEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds1())) {
            sb.append(", ORGIDS1 = '").append(subContractEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds2())) {
            sb.append(", ORGIDS2 = '").append(subContractEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds3())) {
            sb.append(", ORGIDS3 = '").append(subContractEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds4())) {
            sb.append(", ORGIDS4 = '").append(subContractEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds5())) {
            sb.append(", ORGIDS5 = '").append(subContractEntity.getOrgIds5()).append("'");
        }
        sb.append(" WHERE ID = '"+subContractEntity.getId()+"'");

        return sb.toString();
    }

    public String insertEntity(SubContractEntity subContractEntity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_SUB_CONTRACT (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(StringUtil.isNotEmpty(subContractEntity.getProjectName())){
            colSb.append(", PROJECT_NAME");
            valSb.append(", '" + subContractEntity.getProjectName() + "'");
        }
        
        if(StringUtil.isNotEmpty(subContractEntity.getNo())){
        	colSb.append(", NO");
            valSb.append(", '" + subContractEntity.getNo() + "'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getGeneralContractNo())){
            colSb.append(", GENERAL_CONTRACT_NO");
            valSb.append(", '" + subContractEntity.getGeneralContractNo() + "'");
        }


        if(StringUtil.isNotEmpty(subContractEntity.getSubContractNo())){
            colSb.append(", SUB_CONTRACT_NO");
            valSb.append(", '" + subContractEntity.getSubContractNo() + "'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getSubOrgId() )){
            colSb.append(", SUB_ORG_ID");
            valSb.append(", '" + subContractEntity.getSubOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubType())){
            colSb.append(", SUB_TYPE");
            valSb.append(", '" + subContractEntity.getSubType() + "'");
        }

        if(subContractEntity.getSubContractAmount() != null){
            colSb.append(", SUB_CONTRACT_AMOUNT");
            valSb.append(", '" + subContractEntity.getSubContractAmount() + "'");
        }

        if(subContractEntity.getSubSettlementAmount() != null){
            colSb.append(", SUB_SETTLEMENT_AMOUNT");
            valSb.append(", '" + subContractEntity.getSubSettlementAmount() + "'");
        }

        if(subContractEntity.getQuota() != null){
            colSb.append(", QUOTA");
            valSb.append(", '" + subContractEntity.getQuota() + "'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getProjectAddress())){
            colSb.append(", PROJECT_ADDRESS");
            valSb.append(", '" + subContractEntity.getProjectAddress() + "'");
        }

        if(StringUtil.isNotEmpty(subContractEntity.getSubCheckMode())){
            colSb.append(", SUB_CHECK_MODE");
            valSb.append(", '" + subContractEntity.getSubCheckMode() + "'");
        }

        if(subContractEntity.getHasProceedings() != null){
            colSb.append(", HAS_PROCEEDINGS");
            valSb.append(", '" + subContractEntity.getHasProceedings() + "'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getBuildOrgId() )){
            colSb.append(", BUILD_ORG_ID");
            valSb.append(", '" + subContractEntity.getBuildOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getGeneralOrgId() )){
            colSb.append(", GENERAL_ORG_ID");
            valSb.append(", '" + subContractEntity.getGeneralOrgId() + "'");
        }

        if(StringUtil.isNotEmpty(  subContractEntity.getAuditPersonId() )){
            colSb.append(", AUDIT_PERSON_ID");
            valSb.append(", '" + subContractEntity.getAuditPersonId() + "'");
        }

        // ========== 预留字符串（输入框）10个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString1())) {
            colSb.append(", RESERVEDSTRING1");
            valSb.append(", '").append(subContractEntity.getReservedString1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString2())) {
            colSb.append(", RESERVEDSTRING2");
            valSb.append(", '").append(subContractEntity.getReservedString2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString3())) {
            colSb.append(", RESERVEDSTRING3");
            valSb.append(", '").append(subContractEntity.getReservedString3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString4())) {
            colSb.append(", RESERVEDSTRING4");
            valSb.append(", '").append(subContractEntity.getReservedString4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString5())) {
            colSb.append(", RESERVEDSTRING5");
            valSb.append(", '").append(subContractEntity.getReservedString5()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString6())) {
            colSb.append(", RESERVEDSTRING6");
            valSb.append(", '").append(subContractEntity.getReservedString6()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString7())) {
            colSb.append(", RESERVEDSTRING7");
            valSb.append(", '").append(subContractEntity.getReservedString7()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString8())) {
            colSb.append(", RESERVEDSTRING8");
            valSb.append(", '").append(subContractEntity.getReservedString8()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString9())) {
            colSb.append(", RESERVEDSTRING9");
            valSb.append(", '").append(subContractEntity.getReservedString9()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedString10())) {
            colSb.append(", RESERVEDSTRING10");
            valSb.append(", '").append(subContractEntity.getReservedString10()).append("'");
        }

// ========== 预留大文本（文本域）10个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent1())) {
            colSb.append(", RESERVEDCONTENT1");
            valSb.append(", '").append(subContractEntity.getReservedContent1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent2())) {
            colSb.append(", RESERVEDCONTENT2");
            valSb.append(", '").append(subContractEntity.getReservedContent2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent3())) {
            colSb.append(", RESERVEDCONTENT3");
            valSb.append(", '").append(subContractEntity.getReservedContent3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent4())) {
            colSb.append(", RESERVEDCONTENT4");
            valSb.append(", '").append(subContractEntity.getReservedContent4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent5())) {
            colSb.append(", RESERVEDCONTENT5");
            valSb.append(", '").append(subContractEntity.getReservedContent5()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent6())) {
            colSb.append(", RESERVEDCONTENT6");
            valSb.append(", '").append(subContractEntity.getReservedContent6()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent7())) {
            colSb.append(", RESERVEDCONTENT7");
            valSb.append(", '").append(subContractEntity.getReservedContent7()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent8())) {
            colSb.append(", RESERVEDCONTENT8");
            valSb.append(", '").append(subContractEntity.getReservedContent8()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent9())) {
            colSb.append(", RESERVEDCONTENT9");
            valSb.append(", '").append(subContractEntity.getReservedContent9()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedContent10())) {
            colSb.append(", RESERVEDCONTENT10");
            valSb.append(", '").append(subContractEntity.getReservedContent10()).append("'");
        }

// ========== 预留下拉多选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple1())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
            valSb.append(", '").append(subContractEntity.getReservedDropdownMultiple1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple2())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
            valSb.append(", '").append(subContractEntity.getReservedDropdownMultiple2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple3())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
            valSb.append(", '").append(subContractEntity.getReservedDropdownMultiple3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple4())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
            valSb.append(", '").append(subContractEntity.getReservedDropdownMultiple4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownMultiple5())) {
            colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
            valSb.append(", '").append(subContractEntity.getReservedDropdownMultiple5()).append("'");
        }

// ========== 预留多选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice1())) {
            colSb.append(", RESERVEDMULTIPLECHOICE1");
            valSb.append(", '").append(subContractEntity.getReservedMultipleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice2())) {
            colSb.append(", RESERVEDMULTIPLECHOICE2");
            valSb.append(", '").append(subContractEntity.getReservedMultipleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice3())) {
            colSb.append(", RESERVEDMULTIPLECHOICE3");
            valSb.append(", '").append(subContractEntity.getReservedMultipleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice4())) {
            colSb.append(", RESERVEDMULTIPLECHOICE4");
            valSb.append(", '").append(subContractEntity.getReservedMultipleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedMultipleChoice5())) {
            colSb.append(", RESERVEDMULTIPLECHOICE5");
            valSb.append(", '").append(subContractEntity.getReservedMultipleChoice5()).append("'");
        }

// ========== 预留年份5个 ==========
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
        if (subContractEntity.getReservedYearTime1() != null) {
            colSb.append(", RESERVEDYEARTIME1");
            valSb.append(", '").append(sdfYear.format(subContractEntity.getReservedYearTime1())).append("'");
        }
        if (subContractEntity.getReservedYearTime2() != null) {
            colSb.append(", RESERVEDYEARTIME2");
            valSb.append(", '").append(sdfYear.format(subContractEntity.getReservedYearTime2())).append("'");
        }
        if (subContractEntity.getReservedYearTime3() != null) {
            colSb.append(", RESERVEDYEARTIME3");
            valSb.append(", '").append(sdfYear.format(subContractEntity.getReservedYearTime3())).append("'");
        }
        if (subContractEntity.getReservedYearTime4() != null) {
            colSb.append(", RESERVEDYEARTIME4");
            valSb.append(", '").append(sdfYear.format(subContractEntity.getReservedYearTime4())).append("'");
        }
        if (subContractEntity.getReservedYearTime5() != null) {
            colSb.append(", RESERVEDYEARTIME5");
            valSb.append(", '").append(sdfYear.format(subContractEntity.getReservedYearTime5())).append("'");
        }

// ========== 预留时间（年月日时分秒）5个 ==========
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (subContractEntity.getReservedYearAccurateTime1() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME1");
            valSb.append(", '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime1())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime2() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME2");
            valSb.append(", '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime2())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime3() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME3");
            valSb.append(", '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime3())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime4() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME4");
            valSb.append(", '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime4())).append("'");
        }
        if (subContractEntity.getReservedYearAccurateTime5() != null) {
            colSb.append(", RESERVEDYEARACCURATETIME5");
            valSb.append(", '").append(sdfDateTime.format(subContractEntity.getReservedYearAccurateTime5())).append("'");
        }

// ========== 预留年月日5个 ==========
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        if (subContractEntity.getReservedTime1() != null) {
            colSb.append(", RESERVEDTIME1");
            valSb.append(", '").append(sdfDate.format(subContractEntity.getReservedTime1())).append("'");
        }
        if (subContractEntity.getReservedTime2() != null) {
            colSb.append(", RESERVEDTIME2");
            valSb.append(", '").append(sdfDate.format(subContractEntity.getReservedTime2())).append("'");
        }
        if (subContractEntity.getReservedTime3() != null) {
            colSb.append(", RESERVEDTIME3");
            valSb.append(", '").append(sdfDate.format(subContractEntity.getReservedTime3())).append("'");
        }
        if (subContractEntity.getReservedTime4() != null) {
            colSb.append(", RESERVEDTIME4");
            valSb.append(", '").append(sdfDate.format(subContractEntity.getReservedTime4())).append("'");
        }
        if (subContractEntity.getReservedTime5() != null) {
            colSb.append(", RESERVEDTIME5");
            valSb.append(", '").append(sdfDate.format(subContractEntity.getReservedTime5())).append("'");
        }

// ========== 预留单选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice1())) {
            colSb.append(", RESERVEDSINGLECHOICE1");
            valSb.append(", '").append(subContractEntity.getReservedSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice2())) {
            colSb.append(", RESERVEDSINGLECHOICE2");
            valSb.append(", '").append(subContractEntity.getReservedSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice3())) {
            colSb.append(", RESERVEDSINGLECHOICE3");
            valSb.append(", '").append(subContractEntity.getReservedSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice4())) {
            colSb.append(", RESERVEDSINGLECHOICE4");
            valSb.append(", '").append(subContractEntity.getReservedSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedSingleChoice5())) {
            colSb.append(", RESERVEDSINGLECHOICE5");
            valSb.append(", '").append(subContractEntity.getReservedSingleChoice5()).append("'");
        }

// ========== 预留下拉单选字符串5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice1())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
            valSb.append(", '").append(subContractEntity.getReservedDropdownSingleChoice1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice2())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
            valSb.append(", '").append(subContractEntity.getReservedDropdownSingleChoice2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice3())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
            valSb.append(", '").append(subContractEntity.getReservedDropdownSingleChoice3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice4())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
            valSb.append(", '").append(subContractEntity.getReservedDropdownSingleChoice4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getReservedDropdownSingleChoice5())) {
            colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
            valSb.append(", '").append(subContractEntity.getReservedDropdownSingleChoice5()).append("'");
        }

// ========== 预留数字5个 ==========
        if (subContractEntity.getReservedNum1() != null) {
            colSb.append(", RESERVEDNUM1");
            valSb.append(", ").append(subContractEntity.getReservedNum1());
        }
        if (subContractEntity.getReservedNum2() != null) {
            colSb.append(", RESERVEDNUM2");
            valSb.append(", ").append(subContractEntity.getReservedNum2());
        }
        if (subContractEntity.getReservedNum3() != null) {
            colSb.append(", RESERVEDNUM3");
            valSb.append(", ").append(subContractEntity.getReservedNum3());
        }
        if (subContractEntity.getReservedNum4() != null) {
            colSb.append(", RESERVEDNUM4");
            valSb.append(", ").append(subContractEntity.getReservedNum4());
        }
        if (subContractEntity.getReservedNum5() != null) {
            colSb.append(", RESERVEDNUM5");
            valSb.append(", ").append(subContractEntity.getReservedNum5());
        }

// ========== 预留人员单选5个 ==========
        if (subContractEntity.getStaffId1() != null) {
            colSb.append(", STAFFID1");
            valSb.append(", ").append(subContractEntity.getStaffId1());
        }
        if (subContractEntity.getStaffId2() != null) {
            colSb.append(", STAFFID2");
            valSb.append(", ").append(subContractEntity.getStaffId2());
        }
        if (subContractEntity.getStaffId3() != null) {
            colSb.append(", STAFFID3");
            valSb.append(", ").append(subContractEntity.getStaffId3());
        }
        if (subContractEntity.getStaffId4() != null) {
            colSb.append(", STAFFID4");
            valSb.append(", ").append(subContractEntity.getStaffId4());
        }
        if (subContractEntity.getStaffId5() != null) {
            colSb.append(", STAFFID5");
            valSb.append(", ").append(subContractEntity.getStaffId5());
        }

// ========== 预留人员多选5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds1())) {
            colSb.append(", STAFFIDS1");
            valSb.append(", '").append(subContractEntity.getStaffIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds2())) {
            colSb.append(", STAFFIDS2");
            valSb.append(", '").append(subContractEntity.getStaffIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds3())) {
            colSb.append(", STAFFIDS3");
            valSb.append(", '").append(subContractEntity.getStaffIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds4())) {
            colSb.append(", STAFFIDS4");
            valSb.append(", '").append(subContractEntity.getStaffIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getStaffIds5())) {
            colSb.append(", STAFFIDS5");
            valSb.append(", '").append(subContractEntity.getStaffIds5()).append("'");
        }

// ========== 预留组织单选5个 ==========
        if (subContractEntity.getOrgId1() != null) {
            colSb.append(", ORGID1");
            valSb.append(", ").append(subContractEntity.getOrgId1());
        }
        if (subContractEntity.getOrgId2() != null) {
            colSb.append(", ORGID2");
            valSb.append(", ").append(subContractEntity.getOrgId2());
        }
        if (subContractEntity.getOrgId3() != null) {
            colSb.append(", ORGID3");
            valSb.append(", ").append(subContractEntity.getOrgId3());
        }
        if (subContractEntity.getOrgId4() != null) {
            colSb.append(", ORGID4");
            valSb.append(", ").append(subContractEntity.getOrgId4());
        }
        if (subContractEntity.getOrgId5() != null) {
            colSb.append(", ORGID5");
            valSb.append(", ").append(subContractEntity.getOrgId5());
        }

// ========== 预留组织多选5个 ==========
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds1())) {
            colSb.append(", ORGIDS1");
            valSb.append(", '").append(subContractEntity.getOrgIds1()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds2())) {
            colSb.append(", ORGIDS2");
            valSb.append(", '").append(subContractEntity.getOrgIds2()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds3())) {
            colSb.append(", ORGIDS3");
            valSb.append(", '").append(subContractEntity.getOrgIds3()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds4())) {
            colSb.append(", ORGIDS4");
            valSb.append(", '").append(subContractEntity.getOrgIds4()).append("'");
        }
        if (StringUtil.isNotEmpty(subContractEntity.getOrgIds5())) {
            colSb.append(", ORGIDS5");
            valSb.append(", '").append(subContractEntity.getOrgIds5()).append("'");
        }
        colSb.append(")");
        valSb.append(")");

        colSb.append(valSb);
        return colSb.toString();
    }

    public String deleteByIds(String ids){
        StringBuffer sb = new StringBuffer();
        sb.append("DELETE FROM TBL_YQNS_SUB_CONTRACT WHERE ID IN (" + ids+")");
        return sb.toString();
    }

}
