package com.huabo.contract.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblYyXdfCompany;

public class TblYyXdfCompanyMapperSqlConifg {

	public String selectListByPageInfo(IPage<TblYyXdfCompany> page, TblYyXdfCompany company) {
        StringBuffer sqlSb = new StringBuffer("SELECT XC.*,TS.REALNAME FROM TBL_YY_XDF_COMPANY XC LEFT JOIN TBL_STAFF TS ON XC.STAFFID= TS.STAFFID WHERE XC.TEAMID="+company.getTeamid());
        if(company.getStaffid()!=null){
            sqlSb.append(" and XC.STAFFID ="+company.getStaffid());
        }
        if(company.getFxtype()!=null && !"".equals(company.getFxtype())){
            sqlSb.append(" and XC.fxtype ='"+company.getFxtype()+"' ");
        }
        if (company.getCompanyname() != null && !"".equals(company.getCompanyname())) {
            sqlSb.append(" AND XC.COMPANYNAME LIKE '%" + company.getCompanyname() + "%'");
        }
        sqlSb.append(" ORDER BY XC.COMPANYID ");
        String sql = sqlSb.toString();
        return sql;
    }
	
	public String findBySqlPage(IPage<TblYyXdfCompany> page, TblYyXdfCompany company) {
        StringBuffer sqlSb = new StringBuffer("SELECT XC.*,TS.REALNAME FROM TBL_YY_XDF_COMPANY XC LEFT JOIN TBL_STAFF TS ON XC.STAFFID= TS.STAFFID WHERE XC.ORGID="+company.getOrgid()+" and XC.STAFFID="+company.getStaffid());
        if(company.getFxtype()!=null && !"".equals(company.getFxtype())){
            sqlSb.append(" and XC.FXTYPE ='"+company.getFxtype()+"' ");
        }
        if (company.getCompanyname() != null && !"".equals(company.getCompanyname())) {
            sqlSb.append(" AND XC.COMPANYNAME LIKE '%" + company.getCompanyname() + "%'");
        }
        sqlSb.append(" ORDER BY XC.COMPANYID");
        String sql = sqlSb.toString();
        return sql;
    }
	
}
