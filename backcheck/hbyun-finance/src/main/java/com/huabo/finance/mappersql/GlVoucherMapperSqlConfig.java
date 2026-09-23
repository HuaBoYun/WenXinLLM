package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlVoucherVo;

public class GlVoucherMapperSqlConfig {
	
	public String exportVoucherList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT distinct V.*,T.NAME AS VOUCHERTYPENAME FROM GL_VOUCHER V LEFT JOIN BD_VOUCHERTYPE T ON V.PK_VOUCHERTYPE = T.PK_VOUCHERTYPE WHERE 1 = 1 ");		
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND V.PK_ORG = '").append(exportRequestVo.getPkOrg()).append("'");
		}
		
		if(exportRequestVo.getPkVouchers() != null) {
			sqlSb.append(" AND V.PK_VOUCHER IN (");
			for (String pka : exportRequestVo.getPkVouchers()) {
				sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
			}
			sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		
		sqlSb.append(" ORDER BY V.PREPAREDDATE ASC ");
		String sql = sqlSb.toString();
		
		return sql;
	}
	
	

	public String selectFinanceDataPage(Page<GlVoucher> page, GlVoucherVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT distinct V.*,T.NAME AS VOUCHERTYPENAME FROM GL_VOUCHER V LEFT JOIN BD_VOUCHERTYPE T ON V.PK_VOUCHERTYPE = T.PK_VOUCHERTYPE AND T.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND T.DATAORIGINFLAG = -2 WHERE V.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND V.DATAORIGINFLAG = -2 ");		
		
		if(vo.getNum() != null) {
			sqlSb.append(" AND V.NUM = ").append(vo.getNum());
		}
		
		if(StringUtils.isNotBlank(vo.getPkOrg())) {
			sqlSb.append(" AND V.PK_ORG = '").append(vo.getPkOrg()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getYear())) {
			sqlSb.append(" AND V.YEAR = '").append(vo.getYear()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMinperiod())) {
			sqlSb.append(" AND V.PERIOD >= ").append(vo.getMinperiod());
		}
		
		if(StringUtils.isNotBlank(vo.getMaxperiod())) {
			sqlSb.append(" AND V.PERIOD <= ").append(vo.getMaxperiod());
		}
		
		if(vo.getMinprepareddate() != null) {
			sqlSb.append(" AND V.PREPAREDDATE >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMinprepareddate()));
		}
		
		if(vo.getMaxprepareddate() != null) {
			sqlSb.append(" AND V.PREPAREDDATE <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxprepareddate()));
		}
		
		if(vo.getMintallydate() != null) {
			sqlSb.append(" AND V.TALLYDATE >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMintallydate()));
		}
		
		if(vo.getMaxtallydate() != null) {
			sqlSb.append(" AND V.TALLYDATE <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxtallydate()));
		}
		
		
		if(StringUtils.isNotBlank(vo.getExplanation())) {
			sqlSb.append(" AND V.EXPLANATION LIKE '%").append(vo.getExplanation()).append("%' ");
		}
		
		if(StringUtils.isNotBlank(vo.getPkAccAsoa())) {
			sqlSb.append(" AND V.PK_VOUCHER IN (").append("SELECT PK_VOUCHER FROM GL_DETAIL WHERE PK_ACCASOA = '").append(UriUtils.decode(vo.getPkAccAsoa(), "utf-8")).append("' AND FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND DATAORIGINFLAG = -2 ");
			if(StringUtils.isNotBlank(vo.getPkOrg())) {
				sqlSb.append(" AND PK_ORG = '").append(vo.getPkOrg()).append("'");
			}
			sqlSb.append(" ) ");
		}
		
		if(vo.getMinTotalcredit() != null) {
			sqlSb.append(" AND V.TOTALCREDIT >= ").append(vo.getMinTotalcredit());
		}
		
		if(vo.getMaxTotalcredit() != null) {
			sqlSb.append(" AND V.TOTALCREDIT <= ").append(vo.getMaxTotalcredit());
		}
		
		if(vo.getMinTotaldebit() != null) {
			sqlSb.append(" AND V.TOTALDEBIT >= ").append(vo.getMinTotaldebit());
		}
		
		if(vo.getMaxTotaldebit() != null) {
			sqlSb.append(" AND V.TOTALDEBIT <= ").append(vo.getMaxTotaldebit());
		}
		
		sqlSb.append(" ORDER BY V.PREPAREDDATE ASC ");
		String sql = sqlSb.toString();
		
		return sql;
	}
}
