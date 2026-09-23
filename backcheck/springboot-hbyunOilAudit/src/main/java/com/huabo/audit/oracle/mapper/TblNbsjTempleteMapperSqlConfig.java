package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import cn.hutool.core.date.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;

import cn.hutool.core.util.StrUtil;

public class TblNbsjTempleteMapperSqlConfig {
	public String selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjTempleteEntity> pageInfo,BigDecimal orgId, TblNbsjTempleteVo templete) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TT.TEMPLETEID, TT.TEMPLETECODE, TT.TEMPLETENAME,TT.TEMPLETETYPE,TT.CREATEDATE, TS.REALNAME createstaffname, TT.STATUS,TT.TEMPTYPE FROM TBL_NBSJ_TEMPLETE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE 1=1  ");
		
		if(templete.getTempType()!=null && templete.getTempType().equals("0")) {
			sqlSb.append(" and TT.ORGID="+orgId);
		}
		
		//TT.ORGID="+orgId
		if(templete.getTempleteName() != null && !"".equals(templete.getTempleteName())) {
			sqlSb.append(" AND TT.TEMPLETENAME LIKE '%"+templete.getTempleteName()+"%'");
		}
		if(templete.getTempType()!= null && !"".equals(templete.getTempType())) {
			sqlSb.append(" AND TT.TEMPTYPE ="+templete.getTempType()+"");
		}
		if(templete.getStatus()!= null) {
			if(templete.getStatus() == 1) {
				sqlSb.append(" AND TT.STATUS ="+templete.getStatus()); //启用
			}else {
				sqlSb.append(" AND TT.STATUS != 1");				//禁用
			}
			
		}
		if(templete.getTempleteType()!= null && !"2".equals(templete.getTempleteType())) {
			sqlSb.append(" AND TT.ORGID="+orgId);
		}
		if(templete.getTempleteType()!= null && !"".equals(templete.getTempleteType())) {
			sqlSb.append(" AND TT.TEMPLETETYPE ='"+templete.getTempleteType()+"'");
		}
		
		sqlSb.append(" ORDER BY TT.TEMPLETEID DESC ");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	public String findInfobyid(String templeteId) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TT.*, TS.REALNAME createstaffname, ( SELECT wm_concat (ORGID) FROM TBL_ORGANIZATION WHERE ORGID IN ( SELECT ORGID FROM TBL_NBSJ_TEMP_ORG WHERE TEMPLATEID = TT.TEMPLETEID )) AS TEMORGIDS, ( SELECT wm_concat (ORGNAME) FROM TBL_ORGANIZATION WHERE ORGID IN ( SELECT ORGID FROM TBL_NBSJ_TEMP_ORG WHERE TEMPLATEID = TT.TEMPLETEID )) AS TEMORGNAME FROM TBL_NBSJ_TEMPLETE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE TT.TEMPLETEID = "+templeteId);
		return sqlSb.toString();
	}
	public String selectNbsjTempleteListCountByPageInfo(PageInfo<TblNbsjTempleteEntity> pageInfo,BigDecimal orgId, TblNbsjTempleteVo templete){
		StringBuffer sqlSb = new StringBuffer("SELECT  COUNT(0) FROM TBL_NBSJ_TEMPLETE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE 1=1 ");
		//TT.ORGID="+orgId
		if(templete.getTempType()!=null && templete.getTempType().equals("0")) {
			sqlSb.append(" and TT.ORGID="+orgId);
		}
		if(templete.getTempleteType()!= null && !"2".equals(templete.getTempleteType())) {
			sqlSb.append(" AND TT.ORGID="+orgId);
		}
		if(templete.getTempleteName() != null && !"".equals(templete.getTempleteName())) {
			sqlSb.append(" AND TEMPLETENAME LIKE '%"+templete.getTempleteName()+"%'");
		}
		if(templete.getTempType()!= null && !"".equals(templete.getTempType())) {
			sqlSb.append(" AND TEMPTYPE ="+templete.getTempType()+"");
		}
		if(templete.getStatus()!= null) {
			sqlSb.append(" AND TT.STATUS ="+templete.getStatus());
		}
		if(templete.getTempleteType()!= null && !"".equals(templete.getTempleteType())) {
			sqlSb.append(" AND TT.TEMPLETETYPE ='"+templete.getTempleteType()+"'");
		}
		return sqlSb.toString();
	}
	public String insertEntity(TblNbsjTempleteEntity templete){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_TEMPLETE(TEMPLETEID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(StrUtil.isNotBlank(templete.getTempleteCode())) {
			colSb.append(",TEMPLETECODE");
			valSb.append(",'"+templete.getTempleteCode()+"'");
		}
		if(StrUtil.isNotBlank(templete.getTempleteName())) {
			colSb.append(",TEMPLETENAME");
			valSb.append(",'"+templete.getTempleteName()+"'");
		}
		if(StrUtil.isNotBlank(templete.getTempleteType())) {
			colSb.append(",TEMPLETETYPE");
			valSb.append(",'"+templete.getTempleteType()+"'");
		}
		if(StrUtil.isNotBlank(templete.getTempleteDesc())) {
			colSb.append(",TEMPLETEDESC");
			valSb.append(",'"+templete.getTempleteDesc()+"'");
		}
		if(templete.getStaffId()!=null) {
			colSb.append(",STAFFID");
			valSb.append(",'"+templete.getStaffId()+"'");
		}
		if(templete.getCreateDate()!=null) {
			colSb.append(",CREATEDATE");
			valSb.append(",sysdate");
		}
		
		if(templete.getStatus()!=null) {
			colSb.append(",STATUS");
			valSb.append(",'"+templete.getStatus()+"'");
		}
		if(templete.getTempType()!=null) {
			colSb.append(",TEMPTYPE");
			valSb.append(",'"+templete.getTempType()+"'");
		}
		if(templete.getOrgId()!=null) {
			colSb.append(",ORGID");
			valSb.append(",'"+templete.getOrgId()+"'");
		}

		// 预留字符串（输入框）10个
		if(StringUtil.isNotEmpty(templete.getReservedString1())){
			colSb.append(", RESERVEDSTRING1");
			valSb.append(", '"+templete.getReservedString1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString2())){
			colSb.append(", RESERVEDSTRING2");
			valSb.append(", '"+templete.getReservedString2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString3())){
			colSb.append(", RESERVEDSTRING3");
			valSb.append(", '"+templete.getReservedString3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString4())){
			colSb.append(", RESERVEDSTRING4");
			valSb.append(", '"+templete.getReservedString4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString5())){
			colSb.append(", RESERVEDSTRING5");
			valSb.append(", '"+templete.getReservedString5()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString6())){
			colSb.append(", RESERVEDSTRING6");
			valSb.append(", '"+templete.getReservedString6()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString7())){
			colSb.append(", RESERVEDSTRING7");
			valSb.append(", '"+templete.getReservedString7()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString8())){
			colSb.append(", RESERVEDSTRING8");
			valSb.append(", '"+templete.getReservedString8()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString9())){
			colSb.append(", RESERVEDSTRING9");
			valSb.append(", '"+templete.getReservedString9()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString10())){
			colSb.append(", RESERVEDSTRING10");
			valSb.append(", '"+templete.getReservedString10()+"'");
		}
		// 预留大文本（文本域）10个
		if(StringUtil.isNotEmpty(templete.getReservedContent1())){
			colSb.append(", RESERVEDCONTENT1");
			valSb.append(", '"+templete.getReservedContent1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent2())){
			colSb.append(", RESERVEDCONTENT2");
			valSb.append(", '"+templete.getReservedContent2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent3())){
			colSb.append(", RESERVEDCONTENT3");
			valSb.append(", '"+templete.getReservedContent3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent4())){
			colSb.append(", RESERVEDCONTENT4");
			valSb.append(", '"+templete.getReservedContent4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent5())){
			colSb.append(", RESERVEDCONTENT5");
			valSb.append(", '"+templete.getReservedContent5()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent6())){
			colSb.append(", RESERVEDCONTENT6");
			valSb.append(", '"+templete.getReservedContent6()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent7())){
			colSb.append(", RESERVEDCONTENT7");
			valSb.append(", '"+templete.getReservedContent7()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent8())){
			colSb.append(", RESERVEDCONTENT8");
			valSb.append(", '"+templete.getReservedContent8()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent9())){
			colSb.append(", RESERVEDCONTENT9");
			valSb.append(", '"+templete.getReservedContent9()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent10())){
			colSb.append(", RESERVEDCONTENT10");
			valSb.append(", '"+templete.getReservedContent10()+"'");
		}
		// 预留下拉多选字符串（多选下拉）5个
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple1())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE1");
			valSb.append(", '"+templete.getReservedDropdownMultiple1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple2())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE2");
			valSb.append(", '"+templete.getReservedDropdownMultiple2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple3())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE3");
			valSb.append(", '"+templete.getReservedDropdownMultiple3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple4())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE4");
			valSb.append(", '"+templete.getReservedDropdownMultiple4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple5())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE5");
			valSb.append(", '"+templete.getReservedDropdownMultiple5()+"'");
		}

		// 预留多选字符串（多选框）5个
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice1())){
			colSb.append(", RESERVEDMULTIPLECHOICE1");
			valSb.append(", '"+templete.getReservedMultipleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice2())){
			colSb.append(", RESERVEDMULTIPLECHOICE2");
			valSb.append(", '"+templete.getReservedMultipleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice3())){
			colSb.append(", RESERVEDMULTIPLECHOICE3");
			valSb.append(", '"+templete.getReservedMultipleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice4())){
			colSb.append(", RESERVEDMULTIPLECHOICE4");
			valSb.append(", '"+templete.getReservedMultipleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice5())){
			colSb.append(", RESERVEDMULTIPLECHOICE5");
			valSb.append(", '"+templete.getReservedMultipleChoice5()+"'");
		}
		// 预留年份（年份）5个

		if(templete.getReservedYearTime1() != null){
			colSb.append(", RESERVEDYEARTIME1");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearTime1(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime2() != null){
			colSb.append(", RESERVEDYEARTIME2");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearTime2(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime3() != null){
			colSb.append(", RESERVEDYEARTIME3");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearTime3(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime4() != null){
			colSb.append(", RESERVEDYEARTIME4");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearTime4(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime5() != null){
			colSb.append(", RESERVEDYEARTIME5");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearTime5(),"yyyy")+"','YYYY')");
		}
		// 预留时间（日期（年月日时分秒））5个
		if(templete.getReservedYearAccurateTime1() != null){
			colSb.append(", RESERVEDYEARACCURATETIME1");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime2() != null){
			colSb.append(", RESERVEDYEARACCURATETIME2");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime3() != null){
			colSb.append(", RESERVEDYEARACCURATETIME3");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime4() != null){
			colSb.append(", RESERVEDYEARACCURATETIME4");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime5() != null){
			colSb.append(", RESERVEDYEARACCURATETIME5");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		// 预留年月日（日期（年月日））5个
		if(templete.getReservedTime1() != null){
			colSb.append(", RESERVEDTIME1");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime2() != null){
			colSb.append(", RESERVEDTIME2");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime3() != null){
			colSb.append(", RESERVEDTIME3");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime4() != null){
			colSb.append(", RESERVEDTIME4");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime5() != null){
			colSb.append(", RESERVEDTIME5");
			valSb.append(", TO_DATE('"+DateUtil.format(templete.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		// 预留单选字符串（单选框）5个
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice1())){
			colSb.append(", RESERVEDSINGLECHOICE1");
			valSb.append(", '"+templete.getReservedSingleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice2())){
			colSb.append(", RESERVEDSINGLECHOICE2");
			valSb.append(", '"+templete.getReservedSingleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice3())){
			colSb.append(", RESERVEDSINGLECHOICE3");
			valSb.append(", '"+templete.getReservedSingleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice4())){
			colSb.append(", RESERVEDSINGLECHOICE4");
			valSb.append(", '"+templete.getReservedSingleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice5())){
			colSb.append(", RESERVEDSINGLECHOICE5");
			valSb.append(", '"+templete.getReservedSingleChoice5()+"'");
		}
		// 预留下拉单选字符串（单选下拉框）5个
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice1())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1");
			valSb.append(", '"+templete.getReservedDropdownSingleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice2())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2");
			valSb.append(", '"+templete.getReservedDropdownSingleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice3())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3");
			valSb.append(", '"+templete.getReservedDropdownSingleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice4())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4");
			valSb.append(", '"+templete.getReservedDropdownSingleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice5())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5");
			valSb.append(", '"+templete.getReservedDropdownSingleChoice5()+"'");
		}
		// 预留数字（数字输入框）5个
		if(templete.getReservedNum1() != null){
			colSb.append(", RESERVEDNUM1");
			valSb.append(", "+templete.getReservedNum1());
		}
		if(templete.getReservedNum2() != null){
			colSb.append(", RESERVEDNUM2");
			valSb.append(", "+templete.getReservedNum2());
		}
		if(templete.getReservedNum3() != null){
			colSb.append(", RESERVEDNUM3");
			valSb.append(", "+templete.getReservedNum3());
		}
		if(templete.getReservedNum4() != null){
			colSb.append(", RESERVEDNUM4");
			valSb.append(", "+templete.getReservedNum4());
		}
		if(templete.getReservedNum5() != null){
			colSb.append(", RESERVEDNUM5");
			valSb.append(", "+templete.getReservedNum5());
		}
		// 预留人员单选 5个
		if(templete.getStaffid1() != null){
			colSb.append(", STAFFID1");
			valSb.append(", "+templete.getStaffid1());
		}
		if(templete.getStaffid2() != null){
			colSb.append(", STAFFID2");
			valSb.append(", "+templete.getStaffid2());
		}
		if(templete.getStaffid3() != null){
			colSb.append(", STAFFID3");
			valSb.append(", "+templete.getStaffid3());
		}
		if(templete.getStaffid4() != null){
			colSb.append(", STAFFID4");
			valSb.append(", "+templete.getStaffid4());
		}
		if(templete.getStaffid5() != null){
			colSb.append(", STAFFID5");
			valSb.append(", "+templete.getStaffid5());
		}
		// 预留人员多选 5个
		if(StringUtil.isNotEmpty(templete.getStaffids1())){
			colSb.append(", STAFFIDS1");
			valSb.append(", '"+templete.getStaffids1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids2())){
			colSb.append(", STAFFIDS2");
			valSb.append(", '"+templete.getStaffids2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids3())){
			colSb.append(", STAFFIDS3");
			valSb.append(", '"+templete.getStaffids3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids4())){
			colSb.append(", STAFFIDS4");
			valSb.append(", '"+templete.getStaffids4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids5())){
			colSb.append(", STAFFIDS5");
			valSb.append(", '"+templete.getStaffids5()+"'");
		}
		// 预留组织单选 5个
		if(templete.getOrgid1() != null){
			colSb.append(", ORGID1");
			valSb.append(", "+templete.getOrgid1());
		}
		if(templete.getOrgid2() != null){
			colSb.append(", ORGID2");
			valSb.append(", "+templete.getOrgid2());
		}
		if(templete.getOrgid3() != null){
			colSb.append(", ORGID3");
			valSb.append(", "+templete.getOrgid3());
		}
		if(templete.getOrgid4() != null){
			colSb.append(", ORGID4");
			valSb.append(", "+templete.getOrgid4());
		}
		if(templete.getOrgid5() != null){
			colSb.append(", ORGID5");
			valSb.append(", "+templete.getOrgid5());
		}
		// 预留组织多选 5个
		if(StringUtil.isNotEmpty(templete.getOrgids1())){
			colSb.append(", ORGIDS1");
			valSb.append(", '"+templete.getOrgids1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids2())){
			colSb.append(", ORGIDS2");
			valSb.append(", '"+templete.getOrgids2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids3())){
			colSb.append(", ORGIDS3");
			valSb.append(", '"+templete.getOrgids3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids4())){
			colSb.append(", ORGIDS4");
			valSb.append(", '"+templete.getOrgids4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids5())){
			colSb.append(", ORGIDS5");
			valSb.append(", '"+templete.getOrgids5()+"'");
		}


		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	public String updateEntity(TblNbsjTempleteEntity templete){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_TEMPLETE set");
		if(StrUtil.isNotBlank(templete.getTempleteCode())) {
			colSb.append(" TEMPLETECODE='"+templete.getTempleteCode()+"',");
		}
		
		if(StrUtil.isNotBlank(templete.getTempleteName())) {
			colSb.append(" TEMPLETENAME='"+templete.getTempleteName()+"',");
		}
		
		if(StrUtil.isNotBlank(templete.getTempleteType())) {
			colSb.append(" TEMPLETETYPE='"+templete.getTempleteType()+"',");
		}
		if(StrUtil.isNotBlank(templete.getTempleteDesc())) {
			colSb.append(" TEMPLETEDESC='"+templete.getTempleteDesc()+"',");
		}
		if(templete.getUpdateDate()!=null) {
			colSb.append(" UPDATEDATE=sysdate,");
		}
		if(templete.getUpdateStaffId()!=null) {
			colSb.append(" UPDATESTAFFID="+templete.getUpdateStaffId());
		}

		// 预留字符串（输入框）10个
		if(StringUtil.isNotEmpty(templete.getReservedString1())){
			colSb.append(", RESERVEDSTRING1 = '"+templete.getReservedString1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString2())){
			colSb.append(", RESERVEDSTRING2 = '"+templete.getReservedString2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString3())){
			colSb.append(", RESERVEDSTRING3 = '"+templete.getReservedString3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString4())){
			colSb.append(", RESERVEDSTRING4 = '"+templete.getReservedString4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString5())){
			colSb.append(", RESERVEDSTRING5 = '"+templete.getReservedString5()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString6())){
			colSb.append(", RESERVEDSTRING6 = '"+templete.getReservedString6()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString7())){
			colSb.append(", RESERVEDSTRING7 = '"+templete.getReservedString7()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString8())){
			colSb.append(", RESERVEDSTRING8 = '"+templete.getReservedString8()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString9())){
			colSb.append(", RESERVEDSTRING9 = '"+templete.getReservedString9()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedString10())){
			colSb.append(", RESERVEDSTRING10 = '"+templete.getReservedString10()+"'");
		}
		// 预留大文本（文本域）10个
		if(StringUtil.isNotEmpty(templete.getReservedContent1())){
			colSb.append(", RESERVEDCONTENT1 = '"+templete.getReservedContent1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent2())){
			colSb.append(", RESERVEDCONTENT2 = '"+templete.getReservedContent2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent3())){
			colSb.append(", RESERVEDCONTENT3 = '"+templete.getReservedContent3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent4())){
			colSb.append(", RESERVEDCONTENT4 = '"+templete.getReservedContent4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent5())){
			colSb.append(", RESERVEDCONTENT5 = '"+templete.getReservedContent5()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent6())){
			colSb.append(", RESERVEDCONTENT6 = '"+templete.getReservedContent6()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent7())){
			colSb.append(", RESERVEDCONTENT7 = '"+templete.getReservedContent7()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent8())){
			colSb.append(", RESERVEDCONTENT8 = '"+templete.getReservedContent8()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent9())){
			colSb.append(", RESERVEDCONTENT9 = '"+templete.getReservedContent9()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedContent10())){
			colSb.append(", RESERVEDCONTENT10 = '"+templete.getReservedContent10()+"'");
		}
		// 预留下拉多选字符串（多选下拉）5个
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple1())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE1 = '"+templete.getReservedDropdownMultiple1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple2())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE2 = '"+templete.getReservedDropdownMultiple2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple3())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE3 = '"+templete.getReservedDropdownMultiple3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple4())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE4 = '"+templete.getReservedDropdownMultiple4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownMultiple5())){
			colSb.append(", RESERVEDDROPDOWNMULTIPLE5 = '"+templete.getReservedDropdownMultiple5()+"'");
		}

		// 预留多选字符串（多选框）5个
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice1())){
			colSb.append(", RESERVEDMULTIPLECHOICE1 = '"+templete.getReservedMultipleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice2())){
			colSb.append(", RESERVEDMULTIPLECHOICE2 = '"+templete.getReservedMultipleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice3())){
			colSb.append(", RESERVEDMULTIPLECHOICE3 = '"+templete.getReservedMultipleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice4())){
			colSb.append(", RESERVEDMULTIPLECHOICE4 = '"+templete.getReservedMultipleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedMultipleChoice5())){
			colSb.append(", RESERVEDMULTIPLECHOICE5 = '"+templete.getReservedMultipleChoice5()+"'");
		}
		// 预留年份（年份）5个

		if(templete.getReservedYearTime1() != null){
			colSb.append(", RESERVEDYEARTIME1 = TO_DATE('"+DateUtil.format(templete.getReservedYearTime1(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime2() != null){
			colSb.append(", RESERVEDYEARTIME2 = TO_DATE('"+DateUtil.format(templete.getReservedYearTime2(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime3() != null){
			colSb.append(", RESERVEDYEARTIME3 = TO_DATE('"+DateUtil.format(templete.getReservedYearTime3(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime4() != null){
			colSb.append(", RESERVEDYEARTIME4 = TO_DATE('"+DateUtil.format(templete.getReservedYearTime4(),"yyyy")+"','YYYY')");
		}
		if(templete.getReservedYearTime5() != null){
			colSb.append(", RESERVEDYEARTIME5 = TO_DATE('"+DateUtil.format(templete.getReservedYearTime5(),"yyyy")+"','YYYY')");
		}
		// 预留时间（日期（年月日时分秒））5个
		if(templete.getReservedYearAccurateTime1() != null){
			colSb.append(", RESERVEDYEARACCURATETIME1 = TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime1(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime2() != null){
			colSb.append(", RESERVEDYEARACCURATETIME2 = TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime2(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime3() != null){
			colSb.append(", RESERVEDYEARACCURATETIME3 = TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime3(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime4() != null){
			colSb.append(", RESERVEDYEARACCURATETIME4 = TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime4(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		if(templete.getReservedYearAccurateTime5() != null){
			colSb.append(", RESERVEDYEARACCURATETIME5 = TO_DATE('"+DateUtil.format(templete.getReservedYearAccurateTime5(),"yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:MI:SS')");
		}
		// 预留年月日（日期（年月日））5个
		if(templete.getReservedTime1() != null){
			colSb.append(", RESERVEDTIME1 = TO_DATE('"+ DateUtil.format(templete.getReservedTime1(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime2() != null){
			colSb.append(", RESERVEDTIME2 = TO_DATE('"+DateUtil.format(templete.getReservedTime2(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime3() != null){
			colSb.append(", RESERVEDTIME3 = TO_DATE('"+DateUtil.format(templete.getReservedTime3(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime4() != null){
			colSb.append(", RESERVEDTIME4 = TO_DATE('"+DateUtil.format(templete.getReservedTime4(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		if(templete.getReservedTime5() != null){
			colSb.append(", RESERVEDTIME5 = TO_DATE('"+DateUtil.format(templete.getReservedTime5(),"yyyy-MM-dd")+"','YYYY-MM-DD')");
		}
		// 预留单选字符串（单选框）5个
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice1())){
			colSb.append(", RESERVEDSINGLECHOICE1 = '"+templete.getReservedSingleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice2())){
			colSb.append(", RESERVEDSINGLECHOICE2 = '"+templete.getReservedSingleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice3())){
			colSb.append(", RESERVEDSINGLECHOICE3 = '"+templete.getReservedSingleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice4())){
			colSb.append(", RESERVEDSINGLECHOICE4 = '"+templete.getReservedSingleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedSingleChoice5())){
			colSb.append(", RESERVEDSINGLECHOICE5 = '"+templete.getReservedSingleChoice5()+"'");
		}
		// 预留下拉单选字符串（单选下拉框）5个
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice1())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE1 = '"+templete.getReservedDropdownSingleChoice1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice2())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE2 = '"+templete.getReservedDropdownSingleChoice2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice3())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE3 = '"+templete.getReservedDropdownSingleChoice3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice4())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE4 = '"+templete.getReservedDropdownSingleChoice4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getReservedDropdownSingleChoice5())){
			colSb.append(", RESERVEDDROPDOWNSINGLECHOICE5 = '"+templete.getReservedDropdownSingleChoice5()+"'");
		}
		// 预留数字（数字输入框）5个
		if(templete.getReservedNum1() != null){
			colSb.append(", RESERVEDNUM1 = "+templete.getReservedNum1());
		}
		if(templete.getReservedNum2() != null){
			colSb.append(", RESERVEDNUM2 = "+templete.getReservedNum2());
		}
		if(templete.getReservedNum3() != null){
			colSb.append(", RESERVEDNUM3 = "+templete.getReservedNum3());
		}
		if(templete.getReservedNum4() != null){
			colSb.append(", RESERVEDNUM4 = "+templete.getReservedNum4());
		}
		if(templete.getReservedNum5() != null){
			colSb.append(", RESERVEDNUM5 = "+templete.getReservedNum5());
		}
		// 预留人员单选 5个
		if(templete.getStaffid1() != null){
			colSb.append(", STAFFID1 = "+templete.getStaffid1());
		}
		if(templete.getStaffid2() != null){
			colSb.append(", STAFFID2 = "+templete.getStaffid2());
		}
		if(templete.getStaffid3() != null){
			colSb.append(", STAFFID3 = "+templete.getStaffid3());
		}
		if(templete.getStaffid4() != null){
			colSb.append(", STAFFID4 = "+templete.getStaffid4());
		}
		if(templete.getStaffid5() != null){
			colSb.append(", STAFFID5 = "+templete.getStaffid5());
		}
		// 预留人员多选 5个
		if(StringUtil.isNotEmpty(templete.getStaffids1())){
			colSb.append(", STAFFIDS1 = '"+templete.getStaffids1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids2())){
			colSb.append(", STAFFIDS2 = '"+templete.getStaffids2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids3())){
			colSb.append(", STAFFIDS3 = '"+templete.getStaffids3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids4())){
			colSb.append(", STAFFIDS4 = '"+templete.getStaffids4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getStaffids5())){
			colSb.append(", STAFFIDS5 = '"+templete.getStaffids5()+"'");
		}
		// 预留组织单选 5个
		if(templete.getOrgid1() != null){
			colSb.append(", ORGID1 = "+templete.getOrgid1());
		}
		if(templete.getOrgid2() != null){
			colSb.append(", ORGID2 = "+templete.getOrgid2());
		}
		if(templete.getOrgid3() != null){
			colSb.append(", ORGID3 = "+templete.getOrgid3());
		}
		if(templete.getOrgid4() != null){
			colSb.append(", ORGID4 = "+templete.getOrgid4());
		}
		if(templete.getOrgid5() != null){
			colSb.append(", ORGID5 = "+templete.getOrgid5());
		}
		// 预留组织多选 5个
		if(StringUtil.isNotEmpty(templete.getOrgids1())){
			colSb.append(", ORGIDS1 = '"+templete.getOrgids1()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids2())){
			colSb.append(", ORGIDS2 = '"+templete.getOrgids2()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids3())){
			colSb.append(", ORGIDS3 = '"+templete.getOrgids3()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids4())){
			colSb.append(", ORGIDS4 = '"+templete.getOrgids4()+"'");
		}
		if(StringUtil.isNotEmpty(templete.getOrgids5())){
			colSb.append(", ORGIDS5 = '"+templete.getOrgids5()+"'");
		}



		colSb.append(" WHERE TEMPLETEID="+templete.getTempleteId());
		String sql = colSb.toString();
		System.out.println(sql);
		return sql;
	}
}
