package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.hbfk.util.database.GeneralSQLConcatConfig;
import com.huabo.audit.oracle.entity.TblNbsjMb;
import org.apache.commons.lang.StringUtils;

public class TblNbsjMbMapperSqlConfig {
	
	
	public String insertEntity(TblNbsjMb mb){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_MB(MBID,STAFFID,CREATEDTIME,UPDATEDTIME,AUDITTYPE,ORGID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+mb.getStaffid()+",TO_DATE('"+DateUtil.parseDate(mb.getCreatedtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),TO_DATE('"+DateUtil.parseDate(mb.getUpdatedtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),'"+mb.getAudittype()+"',"+mb.getOrgid());
		if(mb.getMbcode()!= null && !"".equals(mb.getMbcode())) {
			colSb.append(",MBCODE");
			valSb.append(",'"+mb.getMbcode()+"'");
		}
		
		if(mb.getMbname() != null && !"".equals(mb.getMbname())) {
			colSb.append(",MBNAME");
			valSb.append(",'"+mb.getMbname()+"'");
		}
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

	
	
	public String updateEntity(TblNbsjMb mb){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_MB  SET MBID=MBID ");
		if(mb.getMbcode()!= null && !"".equals(mb.getMbcode())) {
			colSb.append(" ,MBCODE = '"+mb.getMbcode()+"'");
		}
		
		if(mb.getMbname() != null && !"".equals(mb.getMbname())) {
			colSb.append(" ,MBNAME = '"+mb.getMbname()+"'");
		}
		
		if(mb.getAudittype() != null && !"".equals(mb.getAudittype())) {
			colSb.append(" ,AUDITTYPE = '"+mb.getAudittype()+"'");
		}
		
		if(mb.getUpdatedtime() != null ) {
			colSb.append(" ,UPDATEDTIME = TO_DATE('"+DateUtil.parseDate(mb.getUpdatedtime(), "yyyy-MM-dd")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		
		colSb.append(" WHERE MBID = "+mb.getMbid());
		return colSb.toString();
	}
	
	
	
	
	public String selectNbsjMbByPageInfo(PageInfo<TblNbsjMb> pageInfo, TblStaffUtil loginStaff, TblNbsjMb mb ) throws Exception {
		BigDecimal orgId = loginStaff.getCurrentOrg().getOrgid();
			StringBuffer sb = new StringBuffer("SELECT s.*,STA.REALNAME FROM TBL_NBSJ_MB s " );
			sb.append(" LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID ");
		
			sb.append("  where 1=1 ");

			sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "s.ORGID", "s.ORGID", "s.STAFFID", "s.SECRECTLEVELID", "s.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
			
			if(mb.getMbcode()!=null && mb.getMbcode().trim().length()>0){
				sb.append(" and MBCODE like '%"+mb.getMbcode()+"%'");
			}
			if(mb.getMbname()!=null && mb.getMbname().trim().length()>0){
				sb.append(" and MBNAME like '%"+mb.getMbname()+"%'");
			}
			if(mb.getAudittype()!=null && mb.getAudittype().trim().length()>0){
				sb.append(" and AUDITTYPE like '%"+mb.getAudittype()+"%'");
			}
			
			
			sb.append("  ORDER BY s.AUDITTYPE,MBID ");
			System.out.println(sb.toString());
			return sb.toString();
		}
	
	
	
	public String selectNbsjMbByPageInfoCount(TblStaffUtil loginStaff,TblNbsjMb mb ) throws Exception {
		BigDecimal orgId = loginStaff.getCurrentOrg().getOrgid();
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM TBL_NBSJ_MB s " );
	
		sb.append("  where 1=1 ");
		
		sb.append(GeneralSQLConcatConfig.concatSecrectSql(loginStaff.getCurrentOrg().getUseSecrect(), false, "s.ORGID", "s.ORGID", "s.STAFFID", "s.SECRECTLEVELID", "s.STAFFSCOPEIDS", loginStaff.getStaffid(), loginStaff.getDeptIds(), loginStaff.getSecrectScopeIds()));
		
		if(mb.getMbcode()!=null && mb.getMbcode().trim().length()>0){
			sb.append(" and MBCODE like '%"+mb.getMbcode()+"%'");
		}
		if(mb.getMbname()!=null && mb.getMbname().trim().length()>0){
			sb.append(" and MBNAME like '%"+mb.getMbname()+"%'");
		}
		if(mb.getAudittype()!=null && mb.getAudittype().trim().length()>0){
			sb.append(" and AUDITTYPE like '%"+mb.getAudittype()+"%'");
		}
		
		
		sb.append(" ORDER BY   s.AUDITTYPE,MBID ");
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	public String selectNbsjMbByyy(BigDecimal orgId,BigDecimal mbid ) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM TBL_NBSJ_MB s where s.MBID in (SELECT MBID from TBL_NBSJ_MB_DATAPRE where MBID="+mbid+") " );
		sb.append(" ORDER BY   MBID ");
		return sb.toString();
	}
	public String selectNbsjMbByDateperidPageInfo(Integer dataperid ) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT s.*,STA.REALNAME FROM TBL_NBSJ_MB s " );
		sb.append(" LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID ");
	
		sb.append("  where 1=1 and s.MBID IN ( SELECT DISTINCT MBID from TBL_NBSJ_MB_DATAPRE WHERE DATAOREID ="+dataperid+" ) ");
		
		
		sb.append("  ORDER BY s.AUDITTYPE,MBID ");
		return sb.toString();
	}

	
}
