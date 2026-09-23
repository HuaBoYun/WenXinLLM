package com.huabo.contract.mappersql;

import java.math.BigDecimal;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblContractProject;
import com.huabo.contract.vo.TblContractProjectVo;

public class TblContractProjectMapperSqlConfig {
	
	public String findAutoNumber(String projectcode, BigDecimal orgid) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getMaxNoDeal("PROJECTCODE", "-", projectcode)+" FROM TBL_CONTRACT_PROJECT WHERE PROJECTCODE LIKE '"+projectcode+"%'";
		return sql;
	}
	
	public String selectPlanCodeByOrgid(TblContractProject plan) {
		StringBuffer sb = new StringBuffer("SELECT COUNT(*) FROM TBL_CONTRACT_PROJECT WHERE 1=1 AND PROJECTCODE='"+plan.getProjectcode()+"' ");
		if(plan.getProjectid() != null) {
			sb.append(" AND PROJECTID != "+plan.getProjectid());
		}
		return sb.toString();
	}
	
	public String selectListByPageInfo(IPage<TblContractProject> page,TblContractProjectVo tblContractProjectVo,Integer staffid,Integer orgid) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,STAFF.REALNAME,ORG.ORGNAME "
				+ "FROM TBL_CONTRACT_PROJECT TNA "
				+ "LEFT JOIN TBL_STAFF STAFF ON STAFF.STAFFID = TNA.UNDERTAKESTAFFID "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.UNDERTAKEORGID "
				+ "WHERE 1=1 "
				+ " AND (TNA.UNDERTAKEORGID='"+staffid+"' or TNA.CREATESTAFFID='"+orgid+"'  )");//+ " AND TNA.CREATESTAFFID='"+staffid+"' "
		
		if(tblContractProjectVo.getProjectcode()!=null && tblContractProjectVo.getProjectcode().length()>0) {
			sb.append(" AND TNA.PROJECTCODE LIKE '%"+tblContractProjectVo.getProjectcode()+"%'");
		}
		
		if(tblContractProjectVo.getProjectname()!=null && tblContractProjectVo.getProjectname().length()>0){
			sb.append(" AND TNA.PROJECTNAME LIKE '%"+tblContractProjectVo.getProjectname()+"%'");
		}
		
		sb.append(" ORDER BY TNA.PROJECTID DESC");
		return sb.toString();
	}
	
	public String updateEntity(TblContractProject plan) throws Exception {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_CONTRACT_PROJECT SET PROJECTNAME = '"+plan.getProjectname()+"' ");
		if(plan.getProjectcode() != null && !"".equals(plan.getProjectcode())) {
			sqlSb.append(" ,PROJECTCODE = '"+plan.getProjectcode()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			sqlSb.append(" ,MEMO = '"+plan.getMemo()+"'");
		}
		if(plan.getUndertakeorgid() != null && !"".equals(plan.getUndertakeorgid())) {
			sqlSb.append(" ,UNDERTAKEORGID = '"+plan.getUndertakeorgid()+"'");
		}
		if(plan.getUndertakestaffid() != null && !"".equals(plan.getUndertakestaffid())) {
			sqlSb.append(" ,UNDERTAKESTAFFID = '"+plan.getUndertakestaffid()+"'");
		}
		
		
		if (StringUtils.isNotBlank(plan.getReservedstring1())){
			sqlSb.append(" ,RESERVEDSTRING1 = '"+plan.getReservedstring1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring2())){
			sqlSb.append(" ,RESERVEDSTRING2 = '"+plan.getReservedstring2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring3())){
			sqlSb.append(" ,RESERVEDSTRING3 = '"+plan.getReservedstring3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring4())){
			sqlSb.append(" ,RESERVEDSTRING4 = '"+plan.getReservedstring4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring5())){
			sqlSb.append(" ,RESERVEDSTRING5 = '"+plan.getReservedstring5()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring6())){
			sqlSb.append(" ,RESERVEDSTRING6 = '"+plan.getReservedstring6()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring7())){
			sqlSb.append(" ,RESERVEDSTRING7 = '"+plan.getReservedstring7()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring8())){
			sqlSb.append(" ,RESERVEDSTRING8 = '"+plan.getReservedstring8()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring9())){
			sqlSb.append(" ,RESERVEDSTRING9 = '"+plan.getReservedstring9()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedstring10())){
			sqlSb.append(" ,RESERVEDSTRING10 = '"+plan.getReservedstring10()+"'");
		}


		if (StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice1())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE1 = '"+plan.getReserveddropdownsinglechoice1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice2())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE2 = '"+plan.getReserveddropdownsinglechoice2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice3())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE3 = '"+plan.getReserveddropdownsinglechoice3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice4())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE4 = '"+plan.getReserveddropdownsinglechoice4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice5())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE5 = '"+plan.getReserveddropdownsinglechoice5()+"'");
		}

		if (Objects.nonNull(plan.getReservednum1())){
			sqlSb.append(" ,RESERVEDNUM1 = '"+plan.getReservednum1()+"'");
		}
		if (Objects.nonNull(plan.getReservednum2())){
			sqlSb.append(" ,RESERVEDNUM2 = '"+plan.getReservednum2()+"'");
		}
		if (Objects.nonNull(plan.getReservednum3())){
			sqlSb.append(" ,RESERVEDNUM3 = '"+plan.getReservednum3()+"'");
		}
		if (Objects.nonNull(plan.getReservednum4())){
			sqlSb.append(" ,RESERVEDNUM4 = '"+plan.getReservednum4()+"'");
		}
		if (Objects.nonNull(plan.getReservednum5())){
			sqlSb.append(" ,RESERVEDNUM5 = '"+plan.getReservednum5()+"'");
		}

		if (StringUtils.isNotBlank(plan.getReserveddropdownmultiple1())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE1 = '"+plan.getReserveddropdownmultiple1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownmultiple2())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE2 = '"+plan.getReserveddropdownmultiple2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownmultiple3())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE3 = '"+plan.getReserveddropdownmultiple3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownmultiple4())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE4 = '"+plan.getReserveddropdownmultiple4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReserveddropdownmultiple5())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE5 = '"+plan.getReserveddropdownmultiple5()+"'");
		}


		if (StringUtils.isNotBlank(plan.getReservedsinglechoice1())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE1 = '"+plan.getReservedsinglechoice1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedsinglechoice2())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE2 = '"+plan.getReservedsinglechoice2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedsinglechoice3())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE3 = '"+plan.getReservedsinglechoice3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedsinglechoice4())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE4 = '"+plan.getReservedsinglechoice4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedsinglechoice5())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE5 = '"+plan.getReservedsinglechoice5()+"'");
		}


		if (StringUtils.isNotBlank(plan.getReservedmultiplechoice1())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE1 = '"+plan.getReservedmultiplechoice1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedmultiplechoice2())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE2 = '"+plan.getReservedmultiplechoice2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedmultiplechoice3())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE3 = '"+plan.getReservedmultiplechoice3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedmultiplechoice4())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE4 = '"+plan.getReservedmultiplechoice4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedmultiplechoice5())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE5 = '"+plan.getReservedmultiplechoice5()+"'");
		}


		if (Objects.nonNull(plan.getReservedtime1())){
			sqlSb.append(" ,RESERVEDTIME1 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime1()));
		}
		if (Objects.nonNull(plan.getReservedtime2())){
			sqlSb.append(" ,RESERVEDTIME2 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime2()));
		}
		if (Objects.nonNull(plan.getReservedtime3())){
			sqlSb.append(" ,RESERVEDTIME3 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime3()));
		}
		if (Objects.nonNull(plan.getReservedtime4())){
			sqlSb.append(" ,RESERVEDTIME4 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime4()));
		}
		if (Objects.nonNull(plan.getReservedtime5())){
			sqlSb.append(" ,RESERVEDTIME5 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime5()));
		}


		if (Objects.nonNull(plan.getReservedyeartime1())){
			sqlSb.append(" ,RESERVEDYEARTIME1 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime1()));
		}
		if (Objects.nonNull(plan.getReservedyeartime2())){
			sqlSb.append(" ,RESERVEDYEARTIME2 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime2()));
		}
		if (Objects.nonNull(plan.getReservedyeartime3())){
			sqlSb.append(" ,RESERVEDYEARTIME3 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime3()));
		}
		if (Objects.nonNull(plan.getReservedyeartime4())){
			sqlSb.append(" ,RESERVEDYEARTIME4 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime4()));
		}
		if (Objects.nonNull(plan.getReservedyeartime5())){
			sqlSb.append(" ,RESERVEDYEARTIME5 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime5()));
		}


		if (Objects.nonNull(plan.getReservedyearaccuratetime1())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME1 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime1()));
		}
		if (Objects.nonNull(plan.getReservedyearaccuratetime2())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME2 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime2()));
		}
		if (Objects.nonNull(plan.getReservedyearaccuratetime3())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME3 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime3()));
		}
		if (Objects.nonNull(plan.getReservedyearaccuratetime4())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME4 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime4()));
		}
		if (Objects.nonNull(plan.getReservedyearaccuratetime5())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME5 = "+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime5()));
		}


		if (StringUtils.isNotBlank(plan.getReservedcontent1())){
			sqlSb.append(" ,RESERVEDCONTENT1 = '"+plan.getReservedcontent1()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedcontent2())){
			sqlSb.append(" ,RESERVEDCONTENT2 = '"+plan.getReservedcontent2()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedcontent3())){
			sqlSb.append(" ,RESERVEDCONTENT3 = '"+plan.getReservedcontent3()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedcontent4())){
			sqlSb.append(" ,RESERVEDCONTENT4 = '"+plan.getReservedcontent4()+"'");
		}
		if (StringUtils.isNotBlank(plan.getReservedcontent5())){
			sqlSb.append(" ,RESERVEDCONTENT5 = '"+plan.getReservedcontent5()+"'");
		}
		
		sqlSb.append(" WHERE PROJECTID= "+plan.getProjectid());
		return sqlSb.toString();
	}
	
	
	
	public String updateEntitytb(TblContractProject plan) {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_CONTRACT_PROJECT SET PROJECTNAME = '"+plan.getProjectname()+"' ");
		if(plan.getProjectcode() != null && !"".equals(plan.getProjectcode())) {
			sqlSb.append(" ,PROJECTCODE = '"+plan.getProjectcode()+"'");
		}
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			sqlSb.append(" ,MEMO = '"+plan.getMemo()+"'");
		}
		if(plan.getStatecode() != null && !"".equals(plan.getStatecode())) {
			sqlSb.append(" ,STATECODE = '"+plan.getStatecode()+"'");
		}
		if(plan.getStatename() != null && !"".equals(plan.getStatename())) {
			sqlSb.append(" ,STATENAME = '"+plan.getStatename()+"'");
		}
		if(plan.getUndertakeorgid() != null && !"".equals(plan.getUndertakeorgid())) {
			sqlSb.append(" ,UNDERTAKEORGID = '"+plan.getUndertakeorgid()+"'");
		}
		if(plan.getUndertakestaffid() != null && !"".equals(plan.getUndertakestaffid())) {
			sqlSb.append(" ,UNDERTAKESTAFFID = '"+plan.getUndertakestaffid()+"'");
		}
		
		sqlSb.append(" WHERE UNIQUEID= '"+plan.getUniuqeid()+"'");
		return sqlSb.toString();
	}
	
	public String insertEntity(TblContractProject plan) throws Exception{
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_CONTRACT_PROJECT(PROJECTID,CREATETIME,STATE,CREATESTAFFID");
		StringBuffer valSb = new StringBuffer("  VALUES ("+plan.getProjectid()+","+DataBaseSqlConfig.getDateStrFormat(plan.getCreatetime())+",0,"+plan.getCreatestaffid());
		
		if(plan.getProjectcode() != null && !"".equals(plan.getProjectcode())) {
			colSb.append(",PROJECTCODE");
			valSb.append(",'"+plan.getProjectcode()+"'");
		}
		
		if(plan.getProjectname() != null && !"".equals(plan.getProjectname())) {
			colSb.append(",PROJECTNAME");
			valSb.append(",'"+plan.getProjectname()+"'");
		}
		
		if(plan.getStatecode() != null && !"".equals(plan.getStatecode())) {
			colSb.append(",STATECODE");
			valSb.append(",'"+plan.getStatecode()+"'");
		}
		if(plan.getStatename() != null && !"".equals(plan.getStatename())) {
			colSb.append(",STATENAME");
			valSb.append(",'"+plan.getStatename()+"'");
		}
		
		if(plan.getMemo() != null && !"".equals(plan.getMemo())) {
			colSb.append(",MEMO");
			valSb.append(",'"+plan.getMemo()+"'");
		}
		
		if(plan.getUndertakeorgid() != null && !"".equals(plan.getUndertakeorgid())) {
			colSb.append(",UNDERTAKEORGID");
			valSb.append(","+plan.getUndertakeorgid()+"");
		}
		
		if(plan.getUndertakestaffid() != null && !"".equals(plan.getUndertakestaffid())) {
			colSb.append(",UNDERTAKESTAFFID");
			valSb.append(","+plan.getUndertakestaffid()+"");
		}
		
		if(plan.getUniuqeid() != null && !"".equals(plan.getUniuqeid())) {
			colSb.append(",UNIQUEID");
			valSb.append(",'"+plan.getUniuqeid()+"'");
		}

		//预留备注字段
		if(StringUtils.isNotBlank(plan.getReservedstring1())) {
			colSb.append(",RESERVEDSTRING1");
			valSb.append(",'"+plan.getReservedstring1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring2())) {
			colSb.append(",RESERVEDSTRING2");
			valSb.append(",'"+plan.getReservedstring2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring3())) {
			colSb.append(",RESERVEDSTRING3");
			valSb.append(",'"+plan.getReservedstring3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring4())) {
			colSb.append(",RESERVEDSTRING4");
			valSb.append(",'"+plan.getReservedstring4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring5())) {
			colSb.append(",RESERVEDSTRING5");
			valSb.append(",'"+plan.getReservedstring5()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring6())) {
			colSb.append(",RESERVEDSTRING6");
			valSb.append(",'"+plan.getReservedstring6()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring7())) {
			colSb.append(",RESERVEDSTRING7");
			valSb.append(",'"+plan.getReservedstring7()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring8())) {
			colSb.append(",RESERVEDSTRING8");
			valSb.append(",'"+plan.getReservedstring8()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring9())) {
			colSb.append(",RESERVEDSTRING9");
			valSb.append(",'"+plan.getReservedstring9()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedstring10())) {
			colSb.append(",RESERVEDSTRING10");
			valSb.append(",'"+plan.getReservedstring10()+"'");
		}


		if(StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice1())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE1");
			valSb.append(",'"+plan.getReserveddropdownsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice2())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE2");
			valSb.append(",'"+plan.getReserveddropdownsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice3())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE3");
			valSb.append(",'"+plan.getReserveddropdownsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice4())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE4");
			valSb.append(",'"+plan.getReserveddropdownsinglechoice4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownsinglechoice5())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE5");
			valSb.append(",'"+plan.getReserveddropdownsinglechoice5()+"'");
		}

		if(Objects.nonNull(plan.getReservednum1())) {
			colSb.append(",RESERVEDNUM1");
			valSb.append(",'"+plan.getReservednum1()+"'");
		}
		if(Objects.nonNull(plan.getReservednum2())) {
			colSb.append(",RESERVEDNUM2");
			valSb.append(",'"+plan.getReservednum2()+"'");
		}
		if(Objects.nonNull(plan.getReservednum3())) {
			colSb.append(",RESERVEDNUM3");
			valSb.append(",'"+plan.getReservednum3()+"'");
		}
		if(Objects.nonNull(plan.getReservednum4())) {
			colSb.append(",RESERVEDNUM4");
			valSb.append(",'"+plan.getReservednum4()+"'");
		}
		if(Objects.nonNull(plan.getReservednum5())) {
			colSb.append(",RESERVEDNUM5");
			valSb.append(",'"+plan.getReservednum5()+"'");
		}

		if(StringUtils.isNotBlank(plan.getReserveddropdownmultiple1())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE1");
			valSb.append(",'"+plan.getReserveddropdownmultiple1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownmultiple2())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE2");
			valSb.append(",'"+plan.getReserveddropdownmultiple2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownmultiple3())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE3");
			valSb.append(",'"+plan.getReserveddropdownmultiple3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownmultiple4())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE4");
			valSb.append(",'"+plan.getReserveddropdownmultiple4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReserveddropdownmultiple5())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE5");
			valSb.append(",'"+plan.getReserveddropdownmultiple5()+"'");
		}


		if(StringUtils.isNotBlank(plan.getReservedsinglechoice1())) {
			colSb.append(",RESERVEDSINGLECHOICE1");
			valSb.append(",'"+plan.getReservedsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedsinglechoice2())) {
			colSb.append(",RESERVEDSINGLECHOICE2");
			valSb.append(",'"+plan.getReservedsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedsinglechoice3())) {
			colSb.append(",RESERVEDSINGLECHOICE3");
			valSb.append(",'"+plan.getReservedsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedsinglechoice4())) {
			colSb.append(",RESERVEDSINGLECHOICE4");
			valSb.append(",'"+plan.getReservedsinglechoice4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedsinglechoice5())) {
			colSb.append(",RESERVEDSINGLECHOICE5");
			valSb.append(",'"+plan.getReservedsinglechoice5()+"'");
		}


		if(StringUtils.isNotBlank(plan.getReservedmultiplechoice1())) {
			colSb.append(",RESERVEDMULTIPLECHOICE1");
			valSb.append(",'"+plan.getReservedmultiplechoice1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedmultiplechoice2())) {
			colSb.append(",RESERVEDMULTIPLECHOICE2");
			valSb.append(",'"+plan.getReservedmultiplechoice2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedmultiplechoice3())) {
			colSb.append(",RESERVEDMULTIPLECHOICE3");
			valSb.append(",'"+plan.getReservedmultiplechoice3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedmultiplechoice4())) {
			colSb.append(",RESERVEDMULTIPLECHOICE4");
			valSb.append(",'"+plan.getReservedmultiplechoice4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedmultiplechoice5())) {
			colSb.append(",RESERVEDMULTIPLECHOICE5");
			valSb.append(",'"+plan.getReservedmultiplechoice5()+"'");
		}


		if(Objects.nonNull(plan.getReservedtime1())) {
			colSb.append(",RESERVEDTIME1");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime1()));
		}
		if(Objects.nonNull(plan.getReservedtime2())) {
			colSb.append(",RESERVEDTIME2");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime2()));
		}
		if(Objects.nonNull(plan.getReservedtime3())) {
			colSb.append(",RESERVEDTIME3");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime3()));
		}
		if(Objects.nonNull(plan.getReservedtime4())) {
			colSb.append(",RESERVEDTIME4");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime4()));
		}
		if(Objects.nonNull(plan.getReservedtime5())) {
			colSb.append(",RESERVEDTIME5");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedtime5()));
		}


		if(Objects.nonNull(plan.getReservedyeartime1())) {
			colSb.append(",RESERVEDYEARTIME1");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime1()));
		}
		if(Objects.nonNull(plan.getReservedyeartime2())) {
			colSb.append(",RESERVEDYEARTIME2");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime2()));
		}
		if(Objects.nonNull(plan.getReservedyeartime3())) {
			colSb.append(",RESERVEDYEARTIME3");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime3()));
		}
		if(Objects.nonNull(plan.getReservedyeartime4())) {
			colSb.append(",RESERVEDYEARTIME4");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime4()));
		}
		if(Objects.nonNull(plan.getReservedyeartime5())) {
			colSb.append(",RESERVEDYEARTIME5");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyeartime5()));
		}



		if(Objects.nonNull(plan.getReservedyearaccuratetime1())) {
			colSb.append(",RESERVEDYEARACCURATETIME1");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime1()));
		}
		if(Objects.nonNull(plan.getReservedyearaccuratetime2())) {
			colSb.append(",RESERVEDYEARACCURATETIME2");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime2()));
		}
		if(Objects.nonNull(plan.getReservedyearaccuratetime3())) {
			colSb.append(",RESERVEDYEARACCURATETIME3");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime3()));
		}
		if(Objects.nonNull(plan.getReservedyearaccuratetime4())) {
			colSb.append(",RESERVEDYEARACCURATETIME1");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime4()));
		}
		if(Objects.nonNull(plan.getReservedyearaccuratetime5())) {
			colSb.append(",RESERVEDYEARACCURATETIME5");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(plan.getReservedyearaccuratetime5()));
		}


		if(StringUtils.isNotBlank(plan.getReservedcontent1())) {
			colSb.append(",RESERVEDCONTENT1");
			valSb.append(",'"+plan.getReservedcontent1()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedcontent2())) {
			colSb.append(",RESERVEDCONTENT2");
			valSb.append(",'"+plan.getReservedcontent2()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedcontent3())) {
			colSb.append(",RESERVEDCONTENT3");
			valSb.append(",'"+plan.getReservedcontent3()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedcontent4())) {
			colSb.append(",RESERVEDCONTENT4");
			valSb.append(",'"+plan.getReservedcontent4()+"'");
		}
		if(StringUtils.isNotBlank(plan.getReservedcontent5())) {
			colSb.append(",RESERVEDCONTENT5");
			valSb.append(",'"+plan.getReservedcontent5()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
}
