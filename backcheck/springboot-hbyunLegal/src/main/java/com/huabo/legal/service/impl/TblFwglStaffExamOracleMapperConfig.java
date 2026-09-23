package com.huabo.legal.service.impl;

import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblStaffOracle;
import com.huabo.legal.vo.TblFwglStaffExamParam;


public class TblFwglStaffExamOracleMapperConfig {
	
	public String selectListByPageInfo(PageInfo<TblStaffOracle> pageInfo,TblFwglStaffExamParam staffexamVo) {
		StringBuffer sb = new StringBuffer(" SELECT * FROM ( SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.MIBLEPHONE,TS.EMAIL,TS.MEMO,ROWNUM RN FROM tbl_staff  TS "
				+ " RIGHT JOIN TBL_FWGL_STAFF_EXAM TFS ON TFS.STAFFID = TS.STAFFID "
				+ " WHERE  TFS.EXAMID =  '"+staffexamVo.getExamId()+"'");
		
		if(staffexamVo.getRealName()!=null && !"".equals(staffexamVo.getRealName())) {
			sb.append(" AND TS.REALNAME LIKE '%"+staffexamVo.getRealName()+"%'");
		}
		
		if(staffexamVo.getUserName()!=null && !"".equals(staffexamVo.getUserName())){
			sb.append(" AND TS.USERNAME = '"+staffexamVo.getUserName()+"'");
		}
		
		sb.append(" AND ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}
	
	public String selectListByPageInfoCount(PageInfo<TblStaffOracle> pageInfo,TblFwglStaffExamParam staffexamVo) {
		StringBuffer sb = new StringBuffer(" SELECT COUNT(0) FROM ( SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.MIBLEPHONE,TS.EMAIL,TS.MEMO,ROWNUM RN FROM tbl_staff  TS "
				+ " RIGHT JOIN TBL_FWGL_STAFF_EXAM TFS ON TFS.STAFFID = TS.STAFFID "
				+ " WHERE TFS.EXAMID =  '"+staffexamVo.getExamId()+"'");
		
		if(staffexamVo.getRealName()!=null && !"".equals(staffexamVo.getRealName())) {
			sb.append(" AND TS.REALNAME LIKE '%"+staffexamVo.getRealName()+"%'");
		}
		
		if(staffexamVo.getUserName()!=null && !"".equals(staffexamVo.getUserName())){
			sb.append(" AND TS.USERNAME = '"+staffexamVo.getUserName()+"'");
		}
		
		sb.append(" AND ROWNUM <= "+(pageInfo.getCurrentRecord()+pageInfo.getPageSize())+" ) T2 WHERE T2.RN > "+pageInfo.getCurrentRecord());
		return sb.toString();
	}

}
