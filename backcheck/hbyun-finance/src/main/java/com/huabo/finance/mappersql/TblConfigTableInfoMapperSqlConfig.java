package com.huabo.finance.mappersql;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.vo.BusinessDataVo;
import com.huabo.finance.vo.TblConfigTableInfoVo;
import com.huabo.finance.vr.TblConfigTableInfoVr;

public class TblConfigTableInfoMapperSqlConfig {
	
	public String selectEntityById(String fid) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TCT.*,BFP.FNAME AS PLANNAME,BFD.FINTEXT AS DATATEXT FROM TBL_CONFIG_TABLEINFO TCT LEFT JOIN BD_FINANCEPLAN BFP ON TCT.PLANID = BFP.FID LEFT JOIN BD_FINANCEDATE BFD ON TCT.DATACONFIG = BFD.FID")
				.append(" WHERE TCT.FID = '").append(fid).append("'");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	public String selectPageInfo(Page<TblConfigTableInfoVr> page, TblConfigTableInfoVo vo) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT TCT.FID,TCT.FNAME,TCT.OURSTABLENAME,TCT.OUTSTABLENAME,TCT.PLANID,TCT.FSTATUS,TCT.CREATETIME,TCT.MODIFYTIME,BFP.FNAME AS PLANNAME,T2.RECORDID,T2.RECORDNAME,T2.SQLID,T2.STARTDATE,T2.ENDDATE,T2.RECORDTYPE,T2.ISCOMPLETED,T2.ISRESULT,BFD.FINTEXT AS DATATEXT,TCT.FINANCETYPE FROM TBL_CONFIG_TABLEINFO TCT LEFT JOIN BD_FINANCEPLAN BFP ON TCT.PLANID = BFP.FID LEFT JOIN BD_FINANCEDATE BFD ON TCT.DATACONFIG = BFD.FID ")
				.append(" LEFT JOIN (SELECT BFR.* FROM BD_FINANCEDATE_RECORD BFR INNER JOIN ( SELECT MAX(STARTDATE) AS MAXSTARTDATE,PLANID,SQLID  FROM BD_FINANCEDATE_RECORD GROUP BY PLANID,SQLID)")
				.append(" T1 ON BFR.STARTDATE = MAXSTARTDATE AND BFR.PLANID = T1.PLANID AND BFR.SQLID = T1.SQLID ) T2 ON TCT.PLANID = T2.PLANID AND T2.SQLID = TCT.FID")
				.append(" WHERE 1 = 1 ");
		
		if(StringUtils.isNotBlank(vo.getPlanid())) {
			sqlSb.append(" AND TCT.PLANID = '").append(vo.getPlanid()).append("'");
		}
		
		if(StringUtils.isNotBlank(vo.getFname())) {
			sqlSb.append(" AND TCT.FNAME LIKE '%").append(vo.getFname()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getPlanName())) {
			sqlSb.append(" AND BFP.FNAME LIKE '%").append(vo.getPlanName()).append("%'");
		}
		
		sqlSb.append(" ORDER BY TCT.CREATETIME ASC");
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
	public String selectBusinessDatePage(Page<Map<String, Object>> page, BusinessDataVo vo, TblConfigTableInfoVr info, List<TblConfigColumnInfo> pageColList, TblConfigColumnInfo primaryCol) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ");
		
		for (TblConfigColumnInfo col : pageColList) {
			sqlSb.append(" ").append(col.getOursColname().toUpperCase()).append(",");
		}
		
		sqlSb.deleteCharAt(sqlSb.length() - 1);
		
		sqlSb.append(" FROM ").append(info.getOursTableName()).append(" WHERE 1 = 1 ");
		
		if(vo.getFilterColList() != null && vo.getFilterColList().size() > 0) {
			for (TblConfigColumnInfo col : vo.getFilterColList()) {
				sqlSb.append(" AND ").append(col.getOursColname().toUpperCase()).append(" LIKE '%").append(col.getQueryData()).append("%' ");
			}
		}
		
		sqlSb.append(" ORDER BY ").append(primaryCol.getOursColname().toUpperCase()).append(" DESC");
		
		String sql = sqlSb.toString();
		
		return sql;
	}
	
	public String selectBusinessDateEntity(BusinessDataVo vo, TblConfigTableInfoVr info, List<TblConfigColumnInfo> colList) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT ");
		
		for (TblConfigColumnInfo col : colList) {
			sqlSb.append(" ").append(col.getOursColname().toUpperCase()).append(",");
		}
		
		sqlSb.deleteCharAt(sqlSb.length() - 1);
		
		sqlSb.append(" FROM ").append(info.getOursTableName()).append(" WHERE 1 = 1 ");
		
		if(vo.getFilterColList() != null && vo.getFilterColList().size() > 0) {
			for (TblConfigColumnInfo col : vo.getFilterColList()) {
				sqlSb.append(" AND ").append(col.getOursColname().toUpperCase()).append(" = '").append(col.getQueryData()).append("' ");
			}
		}
		
		String sql = sqlSb.toString();
		return sql;
	}
	
	
}
