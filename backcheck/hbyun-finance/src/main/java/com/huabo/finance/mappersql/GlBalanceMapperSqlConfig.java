package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlBalanceVo;
import com.huabo.finance.vr.GlBalanceVr;

public class GlBalanceMapperSqlConfig {
	
	public String selectYearAmount(GlBalanceVr vr) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(DataBaseSqlConfig.getNullColumn("SUM(LOCALDEBITAMOUNT)", "0")).append(" AS FYEARDEDITLOCAL ,")
				.append(DataBaseSqlConfig.getNullColumn("SUM(LOCALCREDITAMOUNT)", "0"))
				.append(" AS FYEARCREDITLOCAL FROM GL_BALANCE WHERE DATAORIGINFLAG = -2 AND PK_ORG = '")
				.append(vr.getPkOrg()).append("' AND PK_ACCASOA = '").append(UriUtils.decode(vr.getPkAccasoa(), "utf-8")).append("' AND YEAR = '").append(vr.getYear())
				.append("' AND FPLANID = '").append(vr.getFplanid()).append("' AND PERIOD < ").append(vr.getPeriod());
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String exportSumTotalList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BAC.CODE,BAC.NAME,BAC.BALANORIENT,BL.* FROM GL_BALANCE BL LEFT JOIN BD_ACCOUNT BAC ON BL.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.FPLANID = BAC.FPLANID AND BAC.DATAORIGINFLAG = -2");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND BL.PK_ORG = '").append(exportRequestVo.getPkOrg()).append("'");
		}
		
		if(exportRequestVo.getPkBalances() != null) {
			sqlSb.append(" AND BL.PK_BALANCE IN (");
			for (String pka : exportRequestVo.getPkBalances()) {
				sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
			}
			sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		
		
		sqlSb.append(" ORDER BY TO_NUMBER(BL.YEAR) DESC,TO_NUMBER(BL.PERIOD),BAC.CODE ASC");
		String sql = sqlSb.toString();
		return sql;
		
	}
	
	public String selectMinPeriod(GlBalanceVr vr) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT MIN(").append(DataBaseSqlConfig.toNumberColumn("PERIOD")).append(") FROM GL_BALANCE WHERE PK_ORG = '")
				.append(vr.getPkOrg()).append("' AND PK_ACCASOA = '").append(vr.getPkAccasoa()).append("' AND YEAR = '").append(vr.getYear())
				.append("' AND FPLANID = '").append(vr.getFplanid()).append("' AND DATAORIGINFLAG = -2");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectPageList(Page<GlBalanceVr> page, GlBalanceVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT GB.*,ACC.NAME,ACC.CODE FROM GL_BALANCE GB LEFT JOIN BD_ACCOUNT ACC ON GB.PK_ACCASOA = ACC.PK_ACCOUNT AND ACC.FPLANID = '"+bookInfo.getPkFinanplanid()+"' AND ACC.DATAORIGINFLAG = -2");
		
		sqlSb.append(" WHERE GB.FPLANID = '"+bookInfo.getPkFinanplanid()+"' AND GB.DATAORIGINFLAG = -2 AND GB.PK_ORG = '").append(vo.getPkOrg()).append("'");
		
		if(StringUtils.isNotBlank(vo.getYear())) {
			sqlSb.append(" AND GB.YEAR = '").append(vo.getYear()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPeriod())) {
			sqlSb.append(" AND GB.PERIOD = '").append(vo.getPeriod()).append("'");
		}
		if(StringUtils.isNotBlank(vo.getPkAccasoa())) {
			sqlSb.append(" AND GB.PK_ACCASOA = '").append(UriUtils.decode(vo.getPkAccasoa(), "utf-8")).append("'");
		}
		
		if(vo.getMinfbeginBalanceLocal() != null) {
			sqlSb.append(" AND GB.FBEGINBALANCELOCAL >= ").append(vo.getMinfbeginBalanceLocal());
		}
		
		if(vo.getMaxfbeginBalanceLocal() != null) {
			sqlSb.append(" AND GB.FBEGINBALANCELOCAL <= ").append(vo.getMinfbeginBalanceLocal());
		}
		
		if(vo.getMinfendBalanceLocal() != null) {
			sqlSb.append(" AND GB.FENDBALANCELOCAL >= ").append(vo.getMinfendBalanceLocal());
		}
		
		if(vo.getMaxfendBalanceLocal() != null) {
			sqlSb.append(" AND GB.FENDBALANCELOCAL <= ").append(vo.getMaxfendBalanceLocal());
		}
		
		if(vo.getMinlocalcreditamount() != null) {
			sqlSb.append(" AND GB.LOCALCREDITAMOUNT >= ").append(vo.getMinlocalcreditamount());
		}
		
		if(vo.getMaxlocalcreditamount() != null) {
			sqlSb.append(" AND GB.LOCALCREDITAMOUNT <= ").append(vo.getMaxlocalcreditamount());
		}
		
		if(vo.getMinlocaldebitamount() != null) {
			sqlSb.append(" AND GB.LOCALDEBITAMOUNT >= ").append(vo.getMinlocaldebitamount());
		}
		
		if(vo.getMaxlocaldebitamount() != null) {
			sqlSb.append(" AND GB.LOCALDEBITAMOUNT <= ").append(vo.getMaxlocaldebitamount());
		}
		
		sqlSb.append(" ORDER BY TO_NUMBER(GB.YEAR),TO_NUMBER(GB.PERIOD)  DESC ");
		
		String sql = sqlSb.toString();
		
		return sql;
	}
	
	public String selectFinanceDataSumTotalList(Page<GlBalanceVr> page, GlBalanceVo vo,FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT  BAC.CODE,BAC.NAME,BAC.BALANORIENT,BL.* FROM GL_BALANCE BL LEFT JOIN BD_ACCOUNT BAC ON BL.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.FPLANID = BAC.FPLANID AND BAC.DATAORIGINFLAG = -2");
		
		sqlSb.append(" WHERE BAC.CODE IS NOT NULL AND BL.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND BL.DATAORIGINFLAG = -2 AND BL.PK_ORG = '").append(vo.getPkOrg()).append("'").append(" AND BL.PERIOD != '00' ");
		
		if(StringUtils.isNotBlank(vo.getYear())) {
			sqlSb.append(" AND BL.YEAR = '").append(vo.getYear()).append("'");
		}
		if(StringUtils.isNotBlank(vo.getPeriod())) {
			sqlSb.append(" AND BL.PERIOD = '").append(vo.getPeriod()).append("'");
		}
		if(StringUtils.isNotBlank(vo.getPkAccasoa())) {
			sqlSb.append(" AND BL.PK_ACCASOA = '").append(UriUtils.decode(vo.getPkAccasoa(), "utf-8")).append("'");
		}
		
		if(vo.getMinfbeginBalanceLocal() != null) {
			sqlSb.append(" AND BL.FBEGINBALANCELOCAL >= ").append(vo.getMinfbeginBalanceLocal());
		}
		
		if(vo.getMaxfbeginBalanceLocal() != null) {
			sqlSb.append(" AND BL.FBEGINBALANCELOCAL <= ").append(vo.getMaxfbeginBalanceLocal());
		}
		
		if(vo.getMinfendBalanceLocal() != null) {
			sqlSb.append(" AND BL.FENDBALANCELOCAL >= ").append(vo.getMinfendBalanceLocal());
		}
		
		if(vo.getMaxfendBalanceLocal() != null) {
			sqlSb.append(" AND BL.FENDBALANCELOCAL <= ").append(vo.getMaxfendBalanceLocal());
		}
		
		if(vo.getMinlocalcreditamount() != null) {
			sqlSb.append(" AND BL.LOCALCREDITAMOUNT >= ").append(vo.getMinlocalcreditamount());
		}
		
		if(vo.getMaxlocalcreditamount() != null) {
			sqlSb.append(" AND BL.LOCALCREDITAMOUNT <= ").append(vo.getMaxlocalcreditamount());
		}
		
		if(vo.getMinlocaldebitamount() != null) {
			sqlSb.append(" AND BL.LOCALDEBITAMOUNT >= ").append(vo.getMinlocaldebitamount());
		}
		
		if(vo.getMaxlocaldebitamount() != null) {
			sqlSb.append(" AND BL.LOCALDEBITAMOUNT <= ").append(vo.getMaxlocaldebitamount());
		}
		
		sqlSb.append(" ORDER BY TO_NUMBER(BL.YEAR) DESC,TO_NUMBER(BL.PERIOD),BAC.CODE ASC");
		String sql = sqlSb.toString();
		return sql;
	}

}
