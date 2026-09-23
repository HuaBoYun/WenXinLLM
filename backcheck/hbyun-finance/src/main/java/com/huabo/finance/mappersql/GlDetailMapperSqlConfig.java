package com.huabo.finance.mappersql;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.UriUtils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.finance.config.DatabaseConfig;
import com.huabo.finance.entity.caiji.GlVoucher;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.huabo.finance.vo.ExportRequestVo;
import com.huabo.finance.vo.GlDetailVo;
import com.huabo.finance.vo.GlVoucherVo;
import com.huabo.finance.vr.BdPlanSqlconfigVr;
import com.huabo.finance.vr.GlDetailVr;

import net.sf.jsqlparser.schema.Database;

public class GlDetailMapperSqlConfig {
	
	public String exportMxflzList(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BAC.CODE,BAC.NAME,BAC.BALANORIENT,GL.*,GB.LOCALDEBITAMOUNT AS GBLOCALDEBITAMOUNT,GB.LOCALCREDITAMOUNT AS GBLOCALCREDITAMOUNT,GB.FBEGINBALANCELOCAL AS GBFBEGINBALANCELOCAL,GB.FENDBALANCELOCAL AS GBFENDBALANCELOCAL,GB.FYEARDEDITLOCAL AS GBFYEARDEDITLOCAL,GB.FYEARCREDITLOCAL AS GBFYEARCREDITLOCAL,GV.NUM AS GVNUM,GV.PREPAREDDATE AS GVPREPAREDDATE FROM GL_DETAIL GL") 
				.append(" LEFT JOIN BD_ACCOUNT BAC ON GL.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.PK_ORG = GL.PK_ORG AND BAC.FPLANID = GL.FPLANID AND BAC.DATAORIGINFLAG = -2 ") 
				.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ")
				.append(" LEFT JOIN GL_BALANCE GB ON GL.PK_ACCASOA = GB.PK_ACCASOA AND GL.YEARV = GB.YEAR AND GL.PERIODV = GB.PERIOD AND GB.FPLANID = GL.FPLANID AND GB.PK_ORG = GL.PK_ORG AND GB.DATAORIGINFLAG = -2 ");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND GL.PK_ORG = '").append(exportRequestVo.getPkOrg()).append("'");
		}
		
		if(exportRequestVo.getPkDetails() != null) {
			sqlSb.append(" AND GL.PK_DETAIL IN (");
			for (String pka : exportRequestVo.getPkDetails()) {
				sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
			}
			sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		
		sqlSb.append(" ORDER BY BAC.CODE,GV.PREPAREDDATE,GV.NUM,GL.PREPAREDDATEV,GL.DETAILINDEX ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String exportDetailBookPage(ExportRequestVo exportRequestVo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT BAC.CODE,BAC.NAME,BAC.BALANORIENT,GL.*,GB.LOCALDEBITAMOUNT AS GBLOCALDEBITAMOUNT,GB.LOCALCREDITAMOUNT AS GBLOCALCREDITAMOUNT,GB.FBEGINBALANCELOCAL AS GBFBEGINBALANCELOCAL,GB.FENDBALANCELOCAL AS GBFENDBALANCELOCAL,GB.FYEARDEDITLOCAL AS GBFYEARDEDITLOCAL,GB.FYEARCREDITLOCAL AS GBFYEARCREDITLOCAL,GV.NUM AS GVNUM,GV.PREPAREDDATE AS GVPREPAREDDATE FROM GL_DETAIL GL") 
				.append(" LEFT JOIN BD_ACCOUNT BAC ON GL.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.PK_ORG = GL.PK_ORG AND BAC.FPLANID = GL.FPLANID AND BAC.DATAORIGINFLAG = -2 ") 
				.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ")
				.append(" LEFT JOIN GL_BALANCE GB ON GL.PK_ACCASOA = GB.PK_ACCASOA AND GL.YEARV = GB.YEAR AND GL.PERIODV = GB.PERIOD AND GB.FPLANID = GL.FPLANID AND GB.PK_ORG = GL.PK_ORG AND GB.DATAORIGINFLAG = -2 ");
		
		sqlSb.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(exportRequestVo.getPkOrg())) {
			sqlSb.append(" AND GL.PK_ORG = '").append(exportRequestVo.getPkOrg()).append("'");
		}
		
		if(exportRequestVo.getPkDetails() != null) {
			sqlSb.append(" AND GL.PK_DETAIL IN (");
			for (String pka : exportRequestVo.getPkDetails()) {
				sqlSb.append("'").append(UriUtils.decode(pka, "utf-8")).append("',");
			}
			sqlSb.deleteCharAt(sqlSb.length()-1).append(")");
		}
		
		sqlSb.append(" ORDER BY BAC.CODE,GV.PREPAREDDATE,GV.NUM,GL.PREPAREDDATEV,GL.DETAILINDEX ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectCurrentDaySumDetail(GlDetailVr vr) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(DataBaseSqlConfig.getNullColumn("SUM(GL.DEBITAMOUNT)", "0")).append(" AS DEBITAMOUNT,").append(DataBaseSqlConfig.getNullColumn("SUM(GL.CREDITAMOUNT)", "0")).append(" AS CREDITAMOUNT FROM GL_DETAIL GL");
		sqlSb.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ");
		sqlSb.append(" WHERE GL.YEARV = '").append(vr.getYearv()).append("' AND GL.PERIODV = '").append(vr.getPeriodv()).append("' AND  GL.PK_ACCASOA = '").append(vr.getPkAccasoa()).append("'  ");
		sqlSb.append(" AND GL.PK_ORG = '").append(vr.getPkOrg()).append("' AND GL.FPLANID = '").append(vr.getFplanid()).append("' AND GL.DATAORIGINFLAG = -2");
		sqlSb.append(" AND GL.PREPAREDDATEV = ").append(DataBaseSqlConfig.getDateStrFormat(vr.getPrepareddatev()));
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	
	public String selectNextDetailDate(GlDetailVr vr, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM ( SELECT GL.PREPAREDDATEV FROM GL_DETAIL GL");
		sqlSb.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ");
		sqlSb.append(" WHERE GL.YEARV = '").append(vr.getYearv()).append("' AND GL.PERIODV = '").append(vr.getPeriodv()).append("' AND  GL.PK_ACCASOA = '").append(vr.getPkAccasoa()).append("'  ");
		sqlSb.append(" AND GL.PK_ORG = '").append(vr.getPkOrg()).append("' AND GL.FPLANID = '").append(vr.getFplanid()).append("' AND GL.DATAORIGINFLAG = -2");
		sqlSb.append(" AND (GL.PREPAREDDATEV > ").append(DataBaseSqlConfig.getDateStrFormat(vr.getPrepareddatev())).append(" OR ( GL.PREPAREDDATEV = ").append(DataBaseSqlConfig.getDateStrFormat(vr.getPrepareddatev()))
		.append(" AND GL.DETAILINDEX > ").append(vr.getDetailindex()).append(") OR (GV.PREPAREDDATE = ").append(DataBaseSqlConfig.getDateStrFormat(vr.getGvprepareddate())).append(" AND GV.NUM > ").append(vr.getGvnum()).append(") ) ORDER BY GV.PREPAREDDATE,GV.NUM,GL.PREPAREDDATEV,GL.DETAILINDEX ASC ) T1 WHERE ROWNUM = 1");
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectPreDetailSum(GlDetailVr vr) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ").append(DataBaseSqlConfig.getNullColumn("SUM(GL.DEBITAMOUNT)", "0")).append(" AS DEBITAMOUNT,").append(DataBaseSqlConfig.getNullColumn("SUM(GL.CREDITAMOUNT)", "0")).append(" AS CREDITAMOUNT FROM GL_DETAIL GL");
		sqlSb.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ");
		sqlSb.append(" WHERE GL.YEARV = '").append(vr.getYearv()).append("' AND GL.PERIODV = '").append(vr.getPeriodv()).append("' AND  GL.PK_ACCASOA = '").append(vr.getPkAccasoa()).append("'  ");
		sqlSb.append(" AND GL.PK_ORG = '").append(vr.getPkOrg()).append("' AND GL.FPLANID = '").append(vr.getFplanid()).append("' AND GL.DATAORIGINFLAG = -2");
		sqlSb.append(" AND (GL.PREPAREDDATEV < ").append(DataBaseSqlConfig.getDateStrFormat(vr.getPrepareddatev())).append(" OR ( GL.PREPAREDDATEV = ").append(DataBaseSqlConfig.getDateStrFormat(vr.getPrepareddatev()))
		.append(" AND GL.DETAILINDEX < ").append(vr.getDetailindex()).append(") OR (GV.PREPAREDDATE = ").append(DataBaseSqlConfig.getDateStrFormat(vr.getGvprepareddate())).append(" AND GV.NUM < ").append(vr.getGvnum()).append(") )");
		String sql = sqlSb.toString();
		return sql;
	}
	

	public String selectFinanceDataPage(Page<GlDetailVr> page, GlDetailVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT distinct GD.*,BA.NAME AS ACCASOANAME,OBA.NAME AS OPPOSITESUBJNAME FROM GL_DETAIL GD LEFT JOIN BD_ACCOUNT BA ON GD.PK_ACCASOA = BA.PK_ACCOUNT LEFT JOIN BD_ACCOUNT OBA ON GD.OPPOSITESUBJ = OBA.PK_ACCOUNT WHERE 1 = 1 ");		
		
		if(StringUtils.isNotBlank(bookInfo.getPkFinanplanid())) {
			sqlSb.append(" AND GD.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND GD.DATAORIGINFLAG = -2");
		}
		
		if(StringUtils.isNotBlank(vo.getPkOrg())) {
			sqlSb.append(" AND GD.PK_ORG = '").append(vo.getPkOrg()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPkVoucher())) {
			sqlSb.append(" AND GD.PK_VOUCHER = '").append(UriUtils.decode(vo.getPkVoucher(), "utf-8")).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPkAccasoa())) {
			sqlSb.append(" AND GD.PK_ACCASOA = '").append(UriUtils.decode(vo.getPkAccasoa(), "utf-8")).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getExplanation())) {
			sqlSb.append(" AND GD.EXPLANATION LIKE '%").append(vo.getExplanation()).append("%'");
		}
		
		if(vo.getMindebitamount() != null) {
			sqlSb.append(" AND GD.DEBITAMOUNT >= ").append(vo.getMindebitamount());
		}
		
		if(vo.getMaxdebitamount() != null) {
			sqlSb.append(" AND GD.DEBITAMOUNT <= ").append(vo.getMaxdebitamount());
		}
		
		if(vo.getMincreditamount() != null) {
			sqlSb.append(" AND GD.CREDITAMOUNT >= ").append(vo.getMincreditamount());
		}
		
		if(vo.getMaxcreditamount() != null) {
			sqlSb.append(" AND GD.CREDITAMOUNT <= ").append(vo.getMaxcreditamount());
		}
		
		if(StringUtils.isNotBlank(vo.getMinyearv())) {
			sqlSb.append(" AND GD.YEARV >= '").append(vo.getMinyearv()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMaxyearv())) {
			sqlSb.append(" AND GD.YEARV <= '").append(vo.getMaxyearv()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMinperiodv())) {
			sqlSb.append(" AND GD.PERIODV >= ").append(vo.getMinperiodv());
		}
		
		if(StringUtils.isNotBlank(vo.getMaxperiodv())) {
			sqlSb.append(" AND GD.PERIODV <= ").append(vo.getMaxperiodv());
		}
		
		if(vo.getNov() != null) {
			sqlSb.append(" AND GD.NOV = ").append(vo.getNov());
		}
		
		if(StringUtils.isNotBlank(vo.getManagervname())) {
			sqlSb.append(" AND GD.MANAGERVNAME LIKE '%").append("").append("%'");
		}
		
		if(vo.getMinprepareddatev() != null) {
			sqlSb.append(" AND GD.PREPAREDDATEV >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMinprepareddatev()));
		}
		
		if(vo.getMaxprepareddatev() != null) {
			sqlSb.append(" AND GD.PREPAREDDATEV <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxprepareddatev()));
		}
		
		sqlSb.append(" ORDER BY GD.YEARV,GD.PERIODV,GD.PREPAREDDATEV,GD.PK_VOUCHER,GD.DETAILINDEX ASC");
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectFinanceDetailBookPage(Page<GlDetailVr> page, GlDetailVo vo, FaAccbookinfoUtil bookInfo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT distinct BAC.CODE,BAC.NAME,BAC.BALANORIENT,GL.*,GB.LOCALDEBITAMOUNT AS GBLOCALDEBITAMOUNT,GB.LOCALCREDITAMOUNT AS GBLOCALCREDITAMOUNT,GB.FBEGINBALANCELOCAL AS GBFBEGINBALANCELOCAL,GB.FENDBALANCELOCAL AS GBFENDBALANCELOCAL,GB.FYEARDEDITLOCAL AS GBFYEARDEDITLOCAL,GB.FYEARCREDITLOCAL AS GBFYEARCREDITLOCAL,GV.NUM AS GVNUM,GV.PREPAREDDATE AS GVPREPAREDDATE FROM GL_DETAIL GL") 
				.append(" LEFT JOIN BD_ACCOUNT BAC ON GL.PK_ACCASOA = BAC.PK_ACCOUNT AND BAC.PK_ORG = GL.PK_ORG AND BAC.FPLANID = GL.FPLANID AND BAC.DATAORIGINFLAG = -2 ") 
				.append(" LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.PK_ORG = GL.PK_ORG AND GL.FPLANID = GV.FPLANID AND GV.DATAORIGINFLAG = -2 ")
				.append(" LEFT JOIN GL_BALANCE GB ON GL.PK_ACCASOA = GB.PK_ACCASOA AND GL.YEARV = GB.YEAR AND GL.PERIODV = GB.PERIOD AND GB.FPLANID = GL.FPLANID AND GB.PK_ORG = GL.PK_ORG AND GB.DATAORIGINFLAG = -2 ");
		
		sqlSb.append(" WHERE GL.PK_ORG = '").append(vo.getPkOrg()).append("' AND GL.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND GL.DATAORIGINFLAG = -2");
		
		if(StringUtils.isNotBlank(vo.getPkVoucher())) {
			sqlSb.append(" AND GL.PK_VOUCHER = '").append(UriUtils.decode(vo.getPkVoucher(), "utf-8")).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getPkAccasoa())) {
			sqlSb.append(" AND GL.PK_ACCASOA = '").append(UriUtils.decode(vo.getPkAccasoa(), "utf-8")).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getExplanation())) {
			sqlSb.append(" AND GL.EXPLANATION LIKE '%").append(vo.getExplanation()).append("%'");
		}
		
		if(vo.getMindebitamount() != null) {
			sqlSb.append(" AND GL.DEBITAMOUNT >= ").append(vo.getMindebitamount());
		}
		
		if(vo.getMaxdebitamount() != null) {
			sqlSb.append(" AND GL.DEBITAMOUNT <= ").append(vo.getMaxdebitamount());
		}
		
		if(vo.getMincreditamount() != null) {
			sqlSb.append(" AND GL.CREDITAMOUNT >= ").append(vo.getMincreditamount());
		}
		
		if(vo.getMaxcreditamount() != null) {
			sqlSb.append(" AND GL.CREDITAMOUNT <= ").append(vo.getMaxcreditamount());
		}
		
		if(StringUtils.isNotBlank(vo.getMinyearv())) {
			sqlSb.append(" AND GL.YEARV >= '").append(vo.getMinyearv()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMaxyearv())) {
			sqlSb.append(" AND GL.YEARV <= '").append(vo.getMaxyearv()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMinperiodv())) {
			sqlSb.append(" AND GL.PERIODV >= '").append(vo.getMinperiodv()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getMaxperiodv())) {
			sqlSb.append(" AND GL.PERIODV <= '").append(vo.getMaxperiodv()).append("'");
		}
		
		if(vo.getNov() != null) {
			sqlSb.append(" AND GL.NOV = ").append(vo.getNov());
		}
		
		if(StringUtils.isNotBlank(vo.getManagervname())) {
			sqlSb.append(" AND GL.MANAGERVNAME LIKE '%").append("").append("%'");
		}
		
		if(vo.getMinprepareddatev() != null) {
			sqlSb.append(" AND GL.PREPAREDDATEV >= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMinprepareddatev()));
		}
		
		if(vo.getMaxprepareddatev() != null) {
			sqlSb.append(" AND GL.PREPAREDDATEV <= ").append(DataBaseSqlConfig.getDateStrFormat(vo.getMaxprepareddatev()));
		}
		
		if(StringUtils.isNotBlank(vo.getGvnum())) {
			sqlSb.append(" AND GV.NUM LIKE '%").append(vo.getGvnum()).append("%'");
		}
		
		sqlSb.append(" ORDER BY BAC.CODE,GV.PREPAREDDATE,GV.NUM,GL.PREPAREDDATEV,GL.DETAILINDEX ASC");
		String sql = sqlSb.toString();
		return sql;
		
	}
	
	public String selectFirstDayYear(GlDetailVo vo, FaAccbookinfoUtil bookInfo, String prePkAccasoa, String year, String period) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT GL.PK_DETAIL FROM GL_DETAIL GL LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.FPLANID = GL.FPLANID AND GV.PK_ORG = GL.PK_ORG AND GV.DATAORIGINFLAG = '-2' ")
				.append(" INNER JOIN (SELECT MIN(GL.PREPAREDDATEV) AS PREPAREDDATEV,MIN(GV.PREPAREDDATE) AS PREPAREDDATE FROM GL_DETAIL GL LEFT JOIN GL_VOUCHER GV ON GL.PK_VOUCHER = GV.PK_VOUCHER AND GV.FPLANID = GL.FPLANID AND GV.PK_ORG = GL.PK_ORG AND GV.DATAORIGINFLAG = '-2' ")
				.append(" WHERE GL.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND GL.DATAORIGINFLAG = -2 AND GL.PK_ORG = '").append(vo.getPkOrg()).append("' AND GL.PK_ACCASOA = '").append(prePkAccasoa).append("' AND GL.YEARV = '").append(year).append("'").append(" AND GL.PERIODV = '").append(period).append("' ")
				.append(" ) MINDETAIL ON GL.PREPAREDDATEV = MINDETAIL.PREPAREDDATEV AND GV.PREPAREDDATE = MINDETAIL.PREPAREDDATE ")
				.append(" WHERE GL.FPLANID = '").append(bookInfo.getPkFinanplanid()).append("' AND GL.DATAORIGINFLAG = -2 AND GL.PK_ORG = '").append(vo.getPkOrg()).append("' AND GL.PK_ACCASOA = '").append(prePkAccasoa).append("' AND GL.YEARV = '").append(year).append("'").append(" AND GL.PERIODV = '").append(period).append("' ")
				.append(" ORDER BY GV.PREPAREDDATE,GV.NUM,GL.PREPAREDDATEV,GL.DETAILINDEX ASC");
		String sql = sqlSb.toString();
		return sql;
	}
}
