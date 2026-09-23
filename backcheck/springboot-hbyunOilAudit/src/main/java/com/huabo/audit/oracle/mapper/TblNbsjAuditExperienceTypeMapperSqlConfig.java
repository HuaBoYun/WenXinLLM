package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceTypeEntity;

public class TblNbsjAuditExperienceTypeMapperSqlConfig {

	
	
	public String insertEntity(TblNbsjAuditExperienceTypeEntity re){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_AUDITEXPERIENCE_TYPE (TYPEID, TYPENAME, CREATETIME,CREATESTAFFID,PARENTID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,'"+re.getTypeName()+"',TO_DATE('"+DateUtil.parseDate(re.getCreateTime(), "yyyy-MM-dd")+"','YYYY-MM-DD HH24:mi:ss'),"+re.getCreateStaffid()+","+re.getParentId());
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	public String updateEntity(TblNbsjAuditExperienceTypeEntity re){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_AUDITEXPERIENCE_TYPE  SET CREATESTAFFID=CREATESTAFFID ");
		if(re.getTypeName()!= null && !"".equals(re.getTypeName().toString())) {
			colSb.append(" ,TYPENAME = '"+re.getTypeName()+"'");
		}
		
		if(re.getUpdateTime() != null ) {
			colSb.append(" ,UPDATETIME = TO_DATE('"+DateUtil.parseDate(re.getUpdateTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		
		colSb.append(" WHERE TYPEID = "+re.getTypeId());
		return colSb.toString();
	}
}
