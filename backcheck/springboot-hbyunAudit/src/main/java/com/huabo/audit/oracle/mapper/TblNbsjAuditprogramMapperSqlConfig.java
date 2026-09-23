package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;

import cn.hutool.core.util.StrUtil;

public class TblNbsjAuditprogramMapperSqlConfig {
	public String insertEntity(TblNbsjAuditprogramEntity program){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDITPROGRAM(PROGRAMID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval");
		if(program.getTargetId()!=null) {
			colSb.append(",TARGETID");
			valSb.append(","+program.getTargetId());
		}
		if(StrUtil.isNotBlank(program.getBusinessType())) {
			colSb.append(",BUSINESSTYPE");
			valSb.append(",'"+program.getBusinessType()+"'");
		}
		if(StrUtil.isNotBlank(program.getRiskSource())) {
			colSb.append(",RISKSOURCE");
			valSb.append(",'"+program.getRiskSource()+"'");
		}
		if(StrUtil.isNotBlank(program.getSuditProcess())) {
			colSb.append(",SUDITPROCESS");
			valSb.append(",'"+program.getSuditProcess()+"'");
		}
		if(StrUtil.isNotBlank(program.getRiskPoint())) {
			colSb.append(",RISKPOINT");
			valSb.append(",'"+program.getRiskPoint()+"'");
		}
		if(StrUtil.isNotBlank(program.getBioData())) {
			colSb.append(",BIODATA");
			valSb.append(",'"+program.getBioData()+"'");
		}
		if(StrUtil.isNotBlank(program.getControl())) {
			colSb.append(",CONTROL");
			valSb.append(",'"+program.getControl()+"'");
		}
		if(program.getTempId()!=null) {
			colSb.append(",TEMPID");
			valSb.append(","+program.getTempId());
		}
		if(program.getCreateTime()!=null) {
			colSb.append(",CREATETIME");
			valSb.append(",sysdate");
		}
		if(program.getUpdateTime()!=null) {
			colSb.append(",UPDATETIME");
			valSb.append(",sysdate");
		}
		if(program.getStatus()!=null) {
			colSb.append(",STATUS");
			valSb.append(",'"+program.getStatus()+"'");
		}
		String sql = colSb.toString()+")"+valSb.toString()+")";
		System.out.println(sql);
		return sql;
	}
	public String updateEntity(TblNbsjAuditprogramEntity program){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_AUDITPROGRAM set");
		if(StrUtil.isNotBlank(program.getBusinessType())) {
			colSb.append(" BUSINESSTYPE='"+program.getBusinessType()+"',");
		}
		if(StrUtil.isNotBlank(program.getRiskSource())) {
			colSb.append(" RISKSOURCE='"+program.getRiskSource()+"',");
		}
		if(StrUtil.isNotBlank(program.getSuditProcess())) {
			colSb.append(" SUDITPROCESS='"+program.getSuditProcess()+"',");
		}
		if(StrUtil.isNotBlank(program.getRiskPoint())) {
			colSb.append(" RISKPOINT='"+program.getRiskPoint()+"',");
		}
		if(StrUtil.isNotBlank(program.getBioData())) {
			colSb.append(" BIODATA='"+program.getBioData()+"',");
		}
		if(StrUtil.isNotBlank(program.getControl())) {
			colSb.append(" CONTROL='"+program.getControl()+"',");
		}
		colSb.append(" UPDATETIME=sysdate WHERE PROGRAMID="+program.getProgramId());
		String sql = colSb.toString();
		System.out.println(sql);
		return sql;
	}
	public String selectTblNbsjAuditprogramListByPageInfo(PageInfo<TblNbsjAuditprogramEntity> pageInfo, String templeteId,String targetId) throws Exception{
		StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (");
		sqlSb.append("SELECT * from TBL_NBSJ_AUDITPROGRAM  WHERE TEMPID="+templeteId);
		if(StrUtil.isNotBlank(targetId)) {
			sqlSb.append(" AND targetid="+targetId);
		}
		sqlSb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE RN > "+pageInfo.getCurrentRecord());
		return sqlSb.toString();
	}
	public String selectTblNbsjAuditprogramCountByPageInfo(PageInfo<TblNbsjAuditprogramEntity> pageInfo, String templeteId,String targetId){
		StringBuffer sqlSb = new StringBuffer("SELECT COUNT(0) from TBL_NBSJ_AUDITPROGRAM  WHERE TEMPID="+templeteId);
		if(StrUtil.isNotBlank(targetId)) {
			sqlSb.append(" AND targetid="+targetId);
		}
		return sqlSb.toString();
	}
}
