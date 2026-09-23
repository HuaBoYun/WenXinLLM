package com.huabo.cybermonitor.mapper;


import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.StepResult;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;

public class TblAuditModelDataSourceOracleMapperSqlConfig {
	
	
	
	public String insertEntityparem(TblAuditModelDataSourceOracle param){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_GZGL_MODEL_DATA_SOURCE(ID, DATABASETYPE,CREATETYPE, DATABASECONNECTIONADDRESS, DATABASEUSERS, DATABASEPASSWORD, DATABASEOWNERSHIP, STATE, CREATOR,WORKUNIT, BELONGGROUP, CREATEDTIME, UPDATEDTIME");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,'"+param.getDataBaseType()+"','"+param.getCreateType()+"','"+param.getDataBaseConnectionAddress()+"','"+param.getDataBaseUsers()+"','"+param.getDataBasePassWord()+"','"+param.getDataBaseOwnership()+"',"+param.getState()+","+param.getCreator()+","+param.getWorkUnit()+","+param.getBelongGroup()+",TO_DATE('"+DateUtil.parseDate(param.getCreatedTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),TO_DATE('"+DateUtil.parseDate(param.getUpdatedTime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss')");
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}
	
	
	
	public String selectByPageInfo(PageInfo<TblAuditModelDataSourceOracle> pageInfo, TblAuditModelDataSourceQueryParam param) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT * from TBL_GZGL_MODEL_DATA_SOURCE  where 1=1 ");
//		if (param.getCreateType() != null) {
//			sb.append(" and createType="+param.getCreateType());
//		}
		if (param.getBelongGroup() != null) {
			sb.append(" and belongGroup="+param.getBelongGroup());
		}
		sb.append("  ORDER BY ID DESC) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
		
	}
	
	
	public String selectByPageInfocount(TblAuditModelDataSourceQueryParam param) throws Exception{
		StringBuffer sb = new StringBuffer("SELECT count(*) from TBL_GZGL_MODEL_DATA_SOURCE  where 1=1 ");
//		if (param.getCreateType() != null) {
//			sb.append(" and createType="+param.getCreateType());
//		}
		if (param.getBelongGroup() != null) {
			sb.append(" and belongGroup="+param.getBelongGroup());
		}
		sb.append("  ORDER BY ID DESC  ");
		return sb.toString();
		
	}
	
	
	public String insertEntity(StepResult result){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SJMXRESULT (RESULTID, SAVETIME, MEMO, STAFFID, STEPID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,TO_DATE('"+DateUtil.parseDate(result.getSavetime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),'"+result.getMemo()+"',"+result.getStaffid()+","+result.getStepid());
		if(result.getRealname()!=null) {
			colSb.append(",REALNAME");
			valSb.append(",'"+result.getRealname()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

}
