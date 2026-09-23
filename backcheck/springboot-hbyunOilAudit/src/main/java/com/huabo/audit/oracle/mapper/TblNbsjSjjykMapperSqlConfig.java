package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjSjjyk;

public class TblNbsjSjjykMapperSqlConfig {
	
	
	public String insertEntity(TblNbsjSjjyk jyk){
		StringBuffer colSb = new StringBuffer("INSERT INTO TBL_NBSJ_SJJYK(JYKID,STAFFID,CREATEDTIME,UPDATEDTIME,TATLE,ORGID");
		StringBuffer valSb = new StringBuffer("  VALUES (HIBERNATE_SEQUENCE.nextval,"+jyk.getStaffid()+",TO_DATE('"+DateUtil.parseDate(jyk.getCreatedtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),TO_DATE('"+DateUtil.parseDate(jyk.getUpdatedtime(), "yyyy-MM-dd HH:mm:ss")+"','YYYY-MM-DD HH24:mi:ss'),'"+jyk.getTatle()+"',"+jyk.getOrgid());
		if(jyk.getCode()!= null && !"".equals(jyk.getCode())) {
			colSb.append(",CODE");
			valSb.append(",'"+jyk.getCode()+"'");
		}
		
		if(jyk.getExperiencetype() != null && !"".equals(jyk.getExperiencetype())) {
			colSb.append(",EXPERIENCETYPE");
			valSb.append(",'"+jyk.getExperiencetype()+"'");
		}
		
		if(jyk.getExperiencetatle() != null && !"".equals(jyk.getExperiencetatle())) {
			colSb.append(",EXPERIENCETATLE");
			valSb.append(",'"+jyk.getExperiencetatle()+"'");
		}
		
		if(jyk.getOverview() != null && !"".equals(jyk.getOverview())) {
			colSb.append(",OVERVIEW");
			valSb.append(",'"+jyk.getOverview()+"'");
		}
		
		if(jyk.getJykcontent() != null && !"".equals(jyk.getJykcontent())) {
			colSb.append(",JYKCONTENT");
			valSb.append(",'"+jyk.getJykcontent()+"'");
		}
		
		
		String sql = colSb.toString()+")"+valSb.toString()+")";
		return sql;
	}

	
	
	public String updateEntity(TblNbsjSjjyk jyk){
		StringBuffer colSb = new StringBuffer("UPDATE TBL_NBSJ_SJJYK  SET JYKID=JYKID ");
		if(jyk.getCode()!= null && !"".equals(jyk.getCode())) {
			colSb.append(",CODE='"+jyk.getCode()+"'");
		}
		
		if(jyk.getTatle() != null && !"".equals(jyk.getTatle())) {
			colSb.append(",TATLE='"+jyk.getTatle()+"'");
		}
		
		if(jyk.getExperiencetype() != null && !"".equals(jyk.getExperiencetype())) {
			colSb.append(",EXPERIENCETYPE='"+jyk.getExperiencetype()+"'");
		}
		
		if(jyk.getExperiencetatle() != null && !"".equals(jyk.getExperiencetatle())) {
			colSb.append(",EXPERIENCETATLE='"+jyk.getExperiencetatle()+"'");
		}
		
		if(jyk.getOverview() != null && !"".equals(jyk.getOverview())) {
			colSb.append(",OVERVIEW='"+jyk.getOverview()+"'");
		}
		
		if(jyk.getJykcontent() != null && !"".equals(jyk.getJykcontent())) {
			colSb.append(",JYKCONTENT='"+jyk.getJykcontent()+"'");
		}
		
		
		if(jyk.getUpdatedtime() != null ) {
			colSb.append(" ,UPDATEDTIME = TO_DATE('"+DateUtil.parseDate(jyk.getUpdatedtime(), "yyyy-MM-dd")+"','YYYY-MM-DD HH24:mi:ss')");
		}
		
		
		colSb.append(" WHERE JYKID = "+jyk.getJykid());
		return colSb.toString();
	}
	
	
	
	
	public String selectNbsjjykByPageInfo(PageInfo<TblNbsjSjjyk> pageInfo,BigDecimal orgId,TblNbsjSjjyk jyk ) throws Exception {
			
			StringBuffer sb = new StringBuffer("SELECT * FROM (SELECT T1.*,ROWNUM RN  FROM (SELECT s.*,STA.REALNAME FROM TBL_NBSJ_SJJYK s " );
			sb.append(" LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID ");
		
			sb.append("  where 1=1 ");
			
			if(jyk.getCode()!=null && jyk.getCode().trim().length()>0){
				sb.append(" and CODE like '%"+jyk.getCode()+"%'");
			}
			if(jyk.getTatle()!=null && jyk.getTatle().trim().length()>0){
				sb.append(" and TATLE like '%"+jyk.getTatle()+"%'");
			}
			if(jyk.getOverview()!=null && jyk.getOverview().trim().length()>0){
				sb.append(" and overview like '%"+jyk.getOverview()+"%'");
			}
			if(jyk.getExperiencetype()!=null && jyk.getExperiencetype().trim().length()>0){
				sb.append(" and EXPERIENCETYPE like '%"+jyk.getExperiencetype()+"%'");
			}
			
			sb.append("  ORDER BY s.JYKID desc) T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
			return sb.toString();
		}
	
	
	
	public String selectNbsjjykByPageInfoCount(BigDecimal orgId,TblNbsjSjjyk jyk ) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM TBL_NBSJ_SJJYK s " );
	
		sb.append("  where 1=1 ");
		
		if(jyk.getCode()!=null && jyk.getCode().trim().length()>0){
			sb.append(" and CODE like '%"+jyk.getCode()+"%'");
		}
		if(jyk.getTatle()!=null && jyk.getTatle().trim().length()>0){
			sb.append(" and TATLE like '%"+jyk.getTatle()+"%'");
		}
		if(jyk.getOverview()!=null && jyk.getOverview().trim().length()>0){
			sb.append(" and overview like '%"+jyk.getOverview()+"%'");
		}
		if(jyk.getExperiencetype()!=null && jyk.getExperiencetype().trim().length()>0){
			sb.append(" and EXPERIENCETYPE like '%"+jyk.getExperiencetype()+"%'");
		}
		
		
		sb.append(" ORDER BY s.JYKID desc ");
		return sb.toString();
	}
	
	public String selectNbsjjykByyy(BigDecimal orgId,BigDecimal jykid ) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT count(*) FROM TBL_NBSJ_SJJYK s where s.JYKID in (SELECT JYKID from TBL_NBSJ_SJJYK_DATAPRE where JYKID="+jykid+" ) " );
		sb.append(" ORDER BY   JYKID ");
		return sb.toString();
	}
	public String selectNbsjjykByDateperidPageInfo(Integer dataperid ) throws Exception {
		
		StringBuffer sb = new StringBuffer("SELECT s.*,STA.REALNAME FROM TBL_NBSJ_SJJYK s " );
		sb.append(" LEFT JOIN TBL_STAFF sta on s.STAFFID=STA.STAFFID ");
	
		sb.append("  where 1=1 and s.JYKID IN ( SELECT DISTINCT JYKID from TBL_NBSJ_SJJYK_DATAPRE WHERE DATAOREID ="+dataperid+" ) ");
		
		
		sb.append("  ORDER BY s.JYKID desc ");
		return sb.toString();
	}

	
}
