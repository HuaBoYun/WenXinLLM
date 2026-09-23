package com.huabo.contract.mappersql;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblNbsjInnerrule;
import com.huabo.contract.vo.TblNbsjInnerRuleVo;

public class TblNbsjInnerruleMapperSqlConfig {
	
	public String selectInnerruleListView(IPage<TblNbsjInnerrule> page,TblNbsjInnerRuleVo tblNbsjInnerRuleVo) throws Exception {
		StringBuffer sqlSb = new StringBuffer("select * from TBL_CONTRACT_INNERRULE t where 1=1 and COMPANYID = "+tblNbsjInnerRuleVo.getCompanyid());
		if (tblNbsjInnerRuleVo.getRulename()!= null && tblNbsjInnerRuleVo.getRulename().length() > 0) {
			sqlSb.append(" AND rulename LIKE '%"+tblNbsjInnerRuleVo.getRulename()+"%'");
		}
		if (tblNbsjInnerRuleVo.getPublishorg()!= null && tblNbsjInnerRuleVo.getPublishorg().length() > 0) {
			sqlSb.append(" AND PUBLISHORG= '"+tblNbsjInnerRuleVo.getPublishorg()+"'");
		}
		if (tblNbsjInnerRuleVo.getRulecode()!= null && tblNbsjInnerRuleVo.getRulecode().length() > 0) {
			sqlSb.append(" AND RULENUMBER LIKE '%"+tblNbsjInnerRuleVo.getRulecode()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStatus()!= null && tblNbsjInnerRuleVo.getStatus().length() > 0) {
			sqlSb.append(" AND STATUS LIKE '%"+tblNbsjInnerRuleVo.getStatus()+"%'");
		}
		if (tblNbsjInnerRuleVo.getInnruletype()!= null && tblNbsjInnerRuleVo.getInnruletype().length() > 0) {
			sqlSb.append(" AND INNRULETYPE LIKE '%"+tblNbsjInnerRuleVo.getInnruletype()+"%'");
		}
		if (tblNbsjInnerRuleVo.getStarttime()!=null) {
			sqlSb.append(" AND PUBLISHDATE >= "+DataBaseSqlConfig.getDateStrFormat(tblNbsjInnerRuleVo.getStarttime()));
		}
		if (tblNbsjInnerRuleVo.getEndtime()!=null) {
			sqlSb.append(" AND PUBLISHDATE <= "+DataBaseSqlConfig.getDateStrFormat(tblNbsjInnerRuleVo.getEndtime()));
		}
		sqlSb.append(" ORDER BY INNRULID DESC");
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String insertEntity(TblNbsjInnerrule inner) throws Exception{
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_CONTRACT_INNERRULE(INNRULID");
		StringBuffer valSb = new StringBuffer("  VALUES ("+inner.getInnrulid());
		if(inner.getRulecode() != null && !"".equals(inner.getRulecode())) {
			colSb.append(",rulecode");
			valSb.append(",'"+inner.getRulecode()+"'");
		}
		if(inner.getRulename() != null && !"".equals(inner.getRulename())) {
			colSb.append(",rulename");
			valSb.append(",'"+inner.getRulename()+"'");
		}
		if(inner.getRulenumber() != null && !"".equals(inner.getRulenumber())) {
			colSb.append(",rulenumber");
			valSb.append(",'"+inner.getRulenumber()+"'");
		}
		if(inner.getInnruletype() != null && !"".equals(inner.getInnruletype())) {
			colSb.append(",INNRULETYPE");
			valSb.append(",'"+inner.getInnruletype()+"'");
		}
		if(inner.getPublishorg() != null && !"".equals(inner.getPublishorg())) {
			colSb.append(",Publishorg");
			valSb.append(",'"+inner.getPublishorg()+"'");
		}
		if(inner.getCompanyid() != null && !"".equals(inner.getCompanyid())) {
			colSb.append(",COMPANYID");
			valSb.append(",'"+inner.getCompanyid()+"'");
		}
		if(inner.getStatus() != null && !"".equals(inner.getStatus())) {
			colSb.append(",status");
			valSb.append(",'"+inner.getStatus()+"'");
		}
		if(inner.getPublishdate() != null) {
			colSb.append(",publishdate");
			valSb.append(","+DataBaseSqlConfig.getDateStrFormat(inner.getPublishdate()));
		}
		if(inner.getBodyinfo() != null && !"".equals(inner.getBodyinfo())) {
			colSb.append(",bodyinfo");
			valSb.append(",'"+inner.getBodyinfo()+"'");
		}
		if(inner.getOrgname() != null && !"".equals(inner.getOrgname())) {
			colSb.append(",ORGNAME");
			valSb.append(",'"+inner.getOrgname()+"'");
		}

		//预留备注字段
		if(StringUtils.isNotBlank(inner.getReservedstring1())) {
			colSb.append(",RESERVEDSTRING1");
			valSb.append(",'"+inner.getReservedstring1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedstring2())) {
			colSb.append(",RESERVEDSTRING2");
			valSb.append(",'"+inner.getReservedstring2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedstring3())) {
			colSb.append(",RESERVEDSTRING3");
			valSb.append(",'"+inner.getReservedstring3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedstring4())) {
			colSb.append(",RESERVEDSTRING4");
			valSb.append(",'"+inner.getReservedstring4()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedstring5())) {
			colSb.append(",RESERVEDSTRING5");
			valSb.append(",'"+inner.getReservedstring5()+"'");
		}


		if(StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice1())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE1");
			valSb.append(",'"+inner.getReserveddropdownsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice2())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE2");
			valSb.append(",'"+inner.getReserveddropdownsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice3())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE3");
			valSb.append(",'"+inner.getReserveddropdownsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice4())) {
			colSb.append(",RESERVEDDROPDOWNSINGLECHOICE4");
			valSb.append(",'"+inner.getReserveddropdownsinglechoice4()+"'");
		}


		if(StringUtils.isNotBlank(inner.getReserveddropdownmultiple1())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE1");
			valSb.append(",'"+inner.getReserveddropdownmultiple1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownmultiple2())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE2");
			valSb.append(",'"+inner.getReserveddropdownmultiple2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownmultiple3())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE3");
			valSb.append(",'"+inner.getReserveddropdownmultiple3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReserveddropdownmultiple4())) {
			colSb.append(",RESERVEDDROPDOWNMULTIPLE4");
			valSb.append(",'"+inner.getReserveddropdownmultiple4()+"'");
		}


		if(StringUtils.isNotBlank(inner.getReservedsinglechoice1())) {
			colSb.append(",RESERVEDSINGLECHOICE1");
			valSb.append(",'"+inner.getReservedsinglechoice1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedsinglechoice2())) {
			colSb.append(",RESERVEDSINGLECHOICE2");
			valSb.append(",'"+inner.getReservedsinglechoice2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedsinglechoice3())) {
			colSb.append(",RESERVEDSINGLECHOICE3");
			valSb.append(",'"+inner.getReservedsinglechoice3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedsinglechoice4())) {
			colSb.append(",RESERVEDSINGLECHOICE4");
			valSb.append(",'"+inner.getReservedsinglechoice4()+"'");
		}


		if(StringUtils.isNotBlank(inner.getReservedmultiplechoice1())) {
			colSb.append(",RESERVEDMULTIPLECHOICE1");
			valSb.append(",'"+inner.getReservedmultiplechoice1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedmultiplechoice2())) {
			colSb.append(",RESERVEDMULTIPLECHOICE2");
			valSb.append(",'"+inner.getReservedmultiplechoice2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedmultiplechoice3())) {
			colSb.append(",RESERVEDMULTIPLECHOICE3");
			valSb.append(",'"+inner.getReservedmultiplechoice3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedmultiplechoice4())) {
			colSb.append(",RESERVEDMULTIPLECHOICE4");
			valSb.append(",'"+inner.getReservedmultiplechoice4()+"'");
		}


		if(Objects.nonNull(inner.getReservedtime1())) {
			colSb.append(",RESERVEDTIME1");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime1()));
		}
		if(Objects.nonNull(inner.getReservedtime2())) {
			colSb.append(",RESERVEDTIME2");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime2()));
		}
		if(Objects.nonNull(inner.getReservedtime3())) {
			colSb.append(",RESERVEDTIME3");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime3()));
		}
		if(Objects.nonNull(inner.getReservedtime4())) {
			colSb.append(",RESERVEDTIME4");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime4()));
		}


		if(Objects.nonNull(inner.getReservedyeartime1())) {
			colSb.append(",RESERVEDYEARTIME1");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime1()));
		}
		if(Objects.nonNull(inner.getReservedyeartime2())) {
			colSb.append(",RESERVEDYEARTIME2");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime2()));
		}
		if(Objects.nonNull(inner.getReservedyeartime3())) {
			colSb.append(",RESERVEDYEARTIME3");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime3()));
		}
		if(Objects.nonNull(inner.getReservedyeartime4())) {
			colSb.append(",RESERVEDYEARTIME4");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime4()));
		}


		if(Objects.nonNull(inner.getReservedyearaccuratetime1())) {
			colSb.append(",RESERVEDYEARACCURATETIME1");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime1()));
		}
		if(Objects.nonNull(inner.getReservedyearaccuratetime2())) {
			colSb.append(",RESERVEDYEARACCURATETIME2");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime2()));
		}
		if(Objects.nonNull(inner.getReservedyearaccuratetime3())) {
			colSb.append(",RESERVEDYEARACCURATETIME3");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime3()));
		}
		if(Objects.nonNull(inner.getReservedyearaccuratetime4())) {
			colSb.append(",RESERVEDYEARACCURATETIME1");
			valSb.append(","+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime4()));
		}


		if(StringUtils.isNotBlank(inner.getReservedcontent1())) {
			colSb.append(",RESERVEDCONTENT1");
			valSb.append(",'"+inner.getReservedcontent1()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedcontent2())) {
			colSb.append(",RESERVEDCONTENT2");
			valSb.append(",'"+inner.getReservedcontent2()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedcontent3())) {
			colSb.append(",RESERVEDCONTENT3");
			valSb.append(",'"+inner.getReservedcontent3()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedcontent4())) {
			colSb.append(",RESERVEDCONTENT4");
			valSb.append(",'"+inner.getReservedcontent4()+"'");
		}
		if(StringUtils.isNotBlank(inner.getReservedcontent5())) {
			colSb.append(",RESERVEDCONTENT5");
			valSb.append(",'"+inner.getReservedcontent5()+"'");
		}

		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	
	
	public String updateEntity(TblNbsjInnerrule inner) throws Exception {
		StringBuffer sqlSb = new StringBuffer("UPDATE TBL_CONTRACT_INNERRULE SET ");
		sqlSb.append(" rulecode = '"+inner.getRulecode()+"'");
		if(inner.getRulename() != null && !"".equals(inner.getRulename())) {
			sqlSb.append(" ,rulename = '"+inner.getRulename()+"'");
		}
		if(inner.getStatus() != null && !"".equals(inner.getStatus())) {
			sqlSb.append(" ,status = '"+inner.getStatus()+"'");
		}
		if(inner.getRulenumber() != null && !"".equals(inner.getRulenumber())) {
			sqlSb.append(" ,rulenumber = '"+inner.getRulenumber()+"'");
		}
		if(inner.getPublishorg() != null && !"".equals(inner.getPublishorg())) {
			sqlSb.append(" ,Publishorg = '"+inner.getPublishorg()+"'");
		}
		if(inner.getOrgname() != null && !"".equals(inner.getOrgname())) {
			sqlSb.append(" ,ORGNAME = '"+inner.getOrgname()+"'");
		}
		if(inner.getInnruletype() != null && !"".equals(inner.getInnruletype())) {
			sqlSb.append(" ,INNRULETYPE = '"+inner.getInnruletype()+"'");
		}
		if(inner.getPublishdate() != null) {
			sqlSb.append(" ,publishdate = "+ DataBaseSqlConfig.getDateStrFormat(inner.getPublishdate()));
		}
		if(inner.getBodyinfo() != null && !"".equals(inner.getBodyinfo())) {
			sqlSb.append(" ,bodyinfo = '"+inner.getBodyinfo()+"'");
		}


		if (StringUtils.isNotBlank(inner.getReservedstring1())){
			sqlSb.append(" ,RESERVEDSTRING1 = '"+inner.getReservedstring1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedstring2())){
			sqlSb.append(" ,RESERVEDSTRING2 = '"+inner.getReservedstring2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedstring3())){
			sqlSb.append(" ,RESERVEDSTRING3 = '"+inner.getReservedstring3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedstring4())){
			sqlSb.append(" ,RESERVEDSTRING4 = '"+inner.getReservedstring4()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedstring5())){
			sqlSb.append(" ,RESERVEDSTRING5 = '"+inner.getReservedstring5()+"'");
		}


		if (StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice1())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE1 = '"+inner.getReserveddropdownsinglechoice1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice2())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE2 = '"+inner.getReserveddropdownsinglechoice2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice3())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE3 = '"+inner.getReserveddropdownsinglechoice3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownsinglechoice4())){
			sqlSb.append(" ,RESERVEDDROPDOWNSINGLECHOICE4 = '"+inner.getReserveddropdownsinglechoice4()+"'");
		}


		if (StringUtils.isNotBlank(inner.getReserveddropdownmultiple1())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE1 = '"+inner.getReserveddropdownmultiple1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownmultiple2())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE2 = '"+inner.getReserveddropdownmultiple2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownmultiple3())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE3 = '"+inner.getReserveddropdownmultiple3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReserveddropdownmultiple4())){
			sqlSb.append(" ,RESERVEDDROPDOWNMULTIPLE4 = '"+inner.getReserveddropdownmultiple4()+"'");
		}


		if (StringUtils.isNotBlank(inner.getReservedsinglechoice1())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE1 = '"+inner.getReservedsinglechoice1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedsinglechoice2())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE2 = '"+inner.getReservedsinglechoice2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedsinglechoice3())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE3 = '"+inner.getReservedsinglechoice3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedsinglechoice4())){
			sqlSb.append(" ,RESERVEDSINGLECHOICE4 = '"+inner.getReservedsinglechoice4()+"'");
		}


		if (StringUtils.isNotBlank(inner.getReservedmultiplechoice1())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE1 = '"+inner.getReservedmultiplechoice1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedmultiplechoice2())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE2 = '"+inner.getReservedmultiplechoice2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedmultiplechoice3())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE3 = '"+inner.getReservedmultiplechoice3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedmultiplechoice4())){
			sqlSb.append(" ,RESERVEDMULTIPLECHOICE4 = '"+inner.getReservedmultiplechoice4()+"'");
		}


		if (Objects.nonNull(inner.getReservedtime1())){
			sqlSb.append(" ,RESERVEDTIME1 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime1()));
		}
		if (Objects.nonNull(inner.getReservedtime2())){
			sqlSb.append(" ,RESERVEDTIME2 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime2()));
		}
		if (Objects.nonNull(inner.getReservedtime3())){
			sqlSb.append(" ,RESERVEDTIME3 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime3()));
		}
		if (Objects.nonNull(inner.getReservedtime4())){
			sqlSb.append(" ,RESERVEDTIME4 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedtime4()));
		}


		if (Objects.nonNull(inner.getReservedyeartime1())){
			sqlSb.append(" ,RESERVEDYEARTIME1 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime1()));
		}
		if (Objects.nonNull(inner.getReservedyeartime2())){
			sqlSb.append(" ,RESERVEDYEARTIME2 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime2()));
		}
		if (Objects.nonNull(inner.getReservedyeartime3())){
			sqlSb.append(" ,RESERVEDYEARTIME3 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime3()));
		}
		if (Objects.nonNull(inner.getReservedyeartime4())){
			sqlSb.append(" ,RESERVEDYEARTIME4 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyeartime4()));
		}


		if (Objects.nonNull(inner.getReservedyearaccuratetime1())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME1 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime1()));
		}
		if (Objects.nonNull(inner.getReservedyearaccuratetime2())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME2 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime2()));
		}
		if (Objects.nonNull(inner.getReservedyearaccuratetime3())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME3 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime3()));
		}
		if (Objects.nonNull(inner.getReservedyearaccuratetime4())){
			sqlSb.append(" ,RESERVEDYEARACCURATETIME4 = "+ DataBaseSqlConfig.getDateStrFormat(inner.getReservedyearaccuratetime4()));
		}


		if (StringUtils.isNotBlank(inner.getReservedcontent1())){
			sqlSb.append(" ,RESERVEDCONTENT1 = '"+inner.getReservedcontent1()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedcontent2())){
			sqlSb.append(" ,RESERVEDCONTENT2 = '"+inner.getReservedcontent2()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedcontent3())){
			sqlSb.append(" ,RESERVEDCONTENT3 = '"+inner.getReservedcontent3()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedcontent4())){
			sqlSb.append(" ,RESERVEDCONTENT4 = '"+inner.getReservedcontent4()+"'");
		}
		if (StringUtils.isNotBlank(inner.getReservedcontent5())){
			sqlSb.append(" ,RESERVEDCONTENT5 = '"+inner.getReservedcontent5()+"'");
		}

		sqlSb.append(" WHERE INNRULID = "+inner.getInnrulid());
		System.out.println(sqlSb.toString());
		return sqlSb.toString();
	}
	
	public String selectMaxRulecode(String rulecode) throws Exception{
		String sql = "SELECT "+DataBaseSqlConfig.getMaxNoDeal("RULECODE", "-", rulecode)
				+ " FROM TBL_CONTRACT_INNERRULE "
				+ " WHERE RULECODE LIKE '"+rulecode+"%'";
		return sql;
	}

}
