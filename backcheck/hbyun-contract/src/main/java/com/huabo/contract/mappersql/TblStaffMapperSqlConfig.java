package com.huabo.contract.mappersql;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;

public class TblStaffMapperSqlConfig {
	
	public String selectTreeListByPageInfo(IPage<TblStaff> page, BigDecimal orgid) {
		StringBuffer sb = new StringBuffer("SELECT TNA.*,ORG.ORGNAME ORGNAME "
				+ "FROM TBL_STAFF TNA "
				+ "LEFT JOIN TBL_ORGANIZATION ORG ON ORG.ORGID = TNA.ORGID "
				+ "WHERE 1=1 ");
			if(null != orgid){
				sb.append(" AND TNA.ORGID = "+orgid);
			}
		
		sb.append(" ORDER BY TNA.STAFFID DESC");
		return sb.toString();
	}
	
	public String selectContractStaffPageInfo(IPage<TblStaff> page, TblStaff staff) {
		StringBuffer sqlSb = new StringBuffer("select STA.STAFFID,STA.REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,STA.USERNAME,STA.MIBLEPHONE from TBL_STAFF STA INNER JOIN TBL_ORGANIZATION ORG ON STA.ORGID=ORG.ORGID WHERE STA.ORGID = "+staff.getOrgid());

		if(staff.getUsername() != null && !"".equals(staff.getUsername())) {
			sqlSb.append(" AND STA.USERNAME LIKE '%"+staff.getUsername()+"%'");
	    }
		
		if(staff.getRealname() != null && !"".equals(staff.getRealname())) {
			sqlSb.append(" AND STA.REALNAME LIKE '%"+staff.getRealname()+"%'");
	    }
		
	    sqlSb.append(" order by STAFFID desc");
	    String sql = sqlSb.toString();
	    return sql.toString();
	}
	
	public String findAllPageBeanPid(IPage<TblStaff> page, TblOrganization attribute) {
		StringBuffer sqlSb = new StringBuffer("select STAFFID,REALNAME,STA.ORGID,ORG.ORGNAME,STA.ADDRESS,STA.EMAIL,username from TBL_STAFF STA INNER JOIN TBL_ORGANIZATION ORG ON STA.ORGID=ORG.ORGID WHERE 1=1 ");
        if(attribute.getOrgtype()!=null && attribute.getOrgtype().toString().equals("0")){
            sqlSb.append("AND STA.ORGID ="+attribute.getOrgid()+" AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }else {
            sqlSb.append("AND STA.ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID="+attribute.getOrgid()+" AND ORGTYPE=0 ) AND (STA.STATUS is NULL or STA.STATUS != 0)");
        }

        sqlSb.append(" order by STAFFID desc");
        String sql = sqlSb.toString();
        return sql.toString();
	}
	
	public String findUserInfoExam(Integer staffId, Integer rid){
		String sql="select * from TBL_STAFF where ROLEIDSTRS like '%" + rid + "%' and STAFFID=" + staffId;
		return sql;
	}
	
	public String updateTs(TblStaff ts) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_STAFF SET USERNAME = "+ts.getUsername()+" ");

        if(ts.getRealname() != null && !"".equals(ts.getRealname())) {
            sql.append(" , REALNAME = '"+ts.getRealname()+"'");
        }
        if(ts.getFixedphone() != null && !"".equals(ts.getFixedphone())) {
            sql.append(" , FIXEDPHONE = '"+ts.getFixedphone()+"'");
        }
        if(ts.getAddress() != null && !"".equals(ts.getAddress())) {
            sql.append(" , ADDRESS = '"+ts.getAddress()+"'");
        }
        if(ts.getEmail() != null && !"".equals(ts.getEmail())) {
            sql.append(" , EMAIL = '"+ts.getEmail()+"'");
        }
        if(ts.getMiblephone() != null && !"".equals(ts.getMiblephone())) {
            sql.append(" , MIBLEPHONE = '"+ts.getMiblephone()+"'");
        }
        if(ts.getMemo() != null && !"".equals(ts.getMemo())) {
            sql.append(" , MEMO = '"+ts.getMemo()+"'");
        }
        if(ts.getPassword() != null && !"".equals(ts.getPassword())) {
            sql.append(" , PASSWORD = '"+ts.getPassword()+"'");
        }
        if(ts.getJobid() != null && !"".equals(ts.getJobid())) {
            sql.append(" , JOBID = '"+ts.getJobid()+"'");
        }
        if(ts.getCreatetime() != null && !"".equals(ts.getCreatetime())) {
            sql.append(" ,CREATETIME "+ DataBaseSqlConfig.getDateStrFormat(ts.getCreatetime()));
        }
        if(ts.getStatus() != null && !"".equals(ts.getStatus())) {
            sql.append(" , STATUS = '"+ts.getStatus()+"'");
        }

        sql.append(" WHERE staffid = '"+ts.getStaffid()+"'");
        return sql.toString();
    }
}
