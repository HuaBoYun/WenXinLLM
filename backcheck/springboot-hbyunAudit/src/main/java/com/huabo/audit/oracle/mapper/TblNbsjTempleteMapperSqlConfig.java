package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;

import cn.hutool.core.util.StrUtil;

public class TblNbsjTempleteMapperSqlConfig {
	public String selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjTempleteEntity> pageInfo,BigDecimal orgId, TblNbsjTempleteVo templete) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT TT.TEMPLETEID, TT.TEMPLETECODE, TT.TEMPLETENAME,TT.TEMPLETETYPE,TT.CREATEDATE, TS.REALNAME createstaffname, TT.STATUS,TT.TEMPTYPE FROM TBL_NBSJ_TEMPLETE TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE 1=1  ");
		
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
		
		sqlSb.append(" ORDER BY TT.TEMPLETEID DESC ) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
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
		colSb.append(" WHERE TEMPLETEID="+templete.getTempleteId());
		String sql = colSb.toString();
		System.out.println(sql);
		return sql;
	}
}
