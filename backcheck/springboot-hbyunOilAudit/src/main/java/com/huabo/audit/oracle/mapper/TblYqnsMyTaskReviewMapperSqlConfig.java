package com.huabo.audit.oracle.mapper;

import org.apache.commons.lang.StringUtils;

import com.hbfk.entity.TblStaffUtil;
import com.huabo.audit.oracle.entity.TblYqnsMyTaskReviewEntity;

public class TblYqnsMyTaskReviewMapperSqlConfig {
	
	public String selectListByVo(TblYqnsMyTaskReviewEntity vo, TblStaffUtil user, boolean totalFlag) throws Exception{
		StringBuffer sb  = new StringBuffer("SELECT * FROM TBL_YQNS_MY_TASK_REVIEW WHERE TEMPLATEID = ").append(vo.getTemplateId()).append(" AND TYPENAMEID = ").append(vo.getTypeNameId());
		
		if(!totalFlag) {
			sb.append(" AND ( CREATEUSER = ").append(user.getStaffid());
			if(StringUtils.isNotBlank(user.getDeptIds())) {
				sb.append(" OR CREATEUSER IN ( SELECT STAFFID FROM TBL_USER_ORGRELATION WHERE DEPTID IN (").append(user.getDeptIds()).append(") ) ");
			}
			sb.append(" ) ");
		}
		
		if(StringUtils.isNotBlank(vo.getNotepad())) {
			sb.append(" AND NOTEPAD LIKE '%").append(vo.getNotepad()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getSupervisionOpinions())) {
			sb.append(" AND SUPERVISIONOPINIONS LIKE '%").append(vo.getSupervisionOpinions()).append("%'");
		}
		
		if(StringUtils.isNotBlank(vo.getRemarks())) {
			sb.append(" AND REMARKS LIKE '%").append(vo.getRemarks()).append("%'");
		}
		
		sb.append(" ORDER BY CREATETIME ASC");
		String sql = sb.toString();
		return sql;
	}
	
	
	public String selectAttachmentListByPk(String id,String attids) throws Exception{
		StringBuffer sb  = new StringBuffer("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER  FROM TBL_ATTACHMENT WHERE ");
		
		sb.append("ATTID IN (SELECT ATTID FROM TBL_YQNS_MY_TASK_REVIEW_ATTACH WHERE REVIEWID = ").append(id).append(")");
	 
		
		if(StringUtils.isNotBlank(attids)) {
			sb.append("  or ATTID in (").append(attids).append(")");
		}
		String sql = sb.toString();
		return sql;
	}
	 
}
