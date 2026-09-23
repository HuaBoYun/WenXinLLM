package com.huabo.audit.oracle.mapper;

import com.hbfk.entity.TblStaffUtil;

public class TblProcessAnalysisMapperSqlConfig {
	public String getByModuel(String flownumber, TblStaffUtil staff) {
		String sql ="SELECT Count(*) from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if(staff!=null && staff.getTrole() != null){
			sql+=" and (PAU.STAFFID='"+staff.getTrole().getRname()+"' or PAU.STAFFID='"+staff.getRealname()+"' ) ";
		}else{
			sql+=" and PAU.STAFFID='"+staff.getRealname()+"'  ";
		}
		return sql;
	}

	public String getByModu(String flownumber, TblStaffUtil staff) {
		String sql ="SELECT Count(*) from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if(staff!=null && staff.getTrole() != null){
			sql+=" and (PAU.STAFFID='"+staff.getTrole().getRname()+"' or PAU.STAFFID='"+staff.getRealname()+"' ) ";
		}else{
			sql+=" and PAU.STAFFID='"+staff.getRealname()+"'  ";
		}
		return sql;
	}

	public String getByTaskCount(String flownumber, TblStaffUtil staff) {
		String sql ="SELECT Count(*) from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if(staff!=null && staff.getTrole() != null){
			sql+=" and (PAU.STAFFID='"+staff.getTrole().getRname()+"' or PAU.STAFFID='"+staff.getRealname()+"' ) ";
		}else{
			sql+=" and PAU.STAFFID='"+staff.getRealname()+"'  ";
		}
		return sql;
	}

	public String getByStaff(String flownumber, TblStaffUtil staff) {
		String sql ="SELECT Count(*) from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if(staff!=null && staff.getTrole() != null){
			sql+=" and (PAU.STAFFID='"+staff.getTrole().getRname()+"' or PAU.STAFFID='"+staff.getRealname()+"' ) ";
		}else{
			sql+=" and PAU.STAFFID='"+staff.getRealname()+"'  ";
		}
		return sql;
	}

	public String getByModuelStaff(TblStaffUtil staff) {
		String sql ="SELECT Count(*) from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if(staff!=null && staff.getTrole() != null){
			sql+=" and (PAU.STAFFID='"+staff.getTrole().getRname()+"' or PAU.STAFFID='"+staff.getRealname()+"' ) ";
		}else{
			sql+=" and PAU.STAFFID='"+staff.getRealname()+"'  ";
		}
		return sql;
	}

	public String findOndBytakdidstart(String usertaskid,String anid) {
		StringBuffer sql = new StringBuffer("SELECT * FROM (SELECT * FROM TBL_PROCESS_ANALYSIS  where 1=1 ");
		if(usertaskid!=null && usertaskid!=""){
			sql.append(" and USERTASKID='"+usertaskid+"' ");
		}
		if(anid!=null && anid!=""){
			sql.append(" and PROCESSNAME='"+anid+"' ");
		}
		sql.append(" order by to_number(regexp_substr(USERTASKID,'[0-9]*[0-9]',1)) ) WHERE ROWNUM = 1 ");
		System.out.println(sql.toString());
		return sql.toString();
	}


	public String listBySql(String usertaskid, String anid) {
		StringBuffer sql = new StringBuffer("SELECT * FROM TBL_PROCESS_ANALYSIS  where 1=1 ");
		if(usertaskid!=null && usertaskid!=""){
			sql.append(" and USERTASKID='"+usertaskid+"' ");
		}
		if(anid!=null && anid!=""){
			sql.append(" and PROCESSNAME='"+anid+"' ");
		}
		sql.append(" ORDER BY  USERTASKID ASC,ANALID DESC");
		return sql.toString();
	}

	public String findOndBytakdid(String usertaskid) {
		String sql="SELECT * FROM TBL_PROCESS_ANALYSIS where ROWNUM = 1";
		if(usertaskid==null || usertaskid==""){
			sql+=" ORDER BY USERTASKID  ";
		}else{
			sql+="AND USERTASKID='"+usertaskid+"' ORDER BY USERTASKID desc ";
		}
		return sql;
	}

	public String findOndBytakdidAnId(String usertaskid, String anid) {
		String sql="SELECT * FROM TBL_PROCESS_ANALYSIS  where ROWNUM = 1";
		if(usertaskid!=null && usertaskid!=""){
			sql+=" and USERTASKID='"+usertaskid+"'   ";
		}
		if(anid!=null && anid!=""){
			sql+=" and PROCESSNAME='"+anid+"'   ";
		}

		sql+=" ORDER BY ANALID  DESC";
		return sql;
	}


	public String listBySqlUser(String flownumber, TblStaffUtil user) {
		String sql = "SELECT pa.* from TBL_PROCESS_ANALYSIS   pa LEFT JOIN  TBL_PROCESS_ANALUSIS_USER pau  on PA.ANALID=PAU.ANALID where 1=1";
		if (user != null && user.getTrole() != null) {
			sql = sql + " and (PAU.STAFFID='" + user.getTrole().getRname() + "' or PAU.STAFFID='" + user.getRealname() + "' ) ";
		} else {
			sql = sql + " and PAU.STAFFID='" + user.getRealname() + "'  ";


			sql += " ORDER BY ANALID  DESC";

		}
		return sql;
	}
}
