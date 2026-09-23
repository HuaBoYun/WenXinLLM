package com.huabo.monitor.mapper;

import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblLogintype;

import java.math.BigDecimal;

public class TblLoginTypeMapperSqlConfig {


    public String selectListByPageInfo(PageInfo<TblLogintype> pageInfo, BigDecimal orgid) {

        StringBuffer sqlSb = new StringBuffer("SELECT * FROM (SELECT BUDGET.*,ROWNUM RN FROM (SELECT * from TBL_LOGINTYPE WHERE ORGID =" +orgid);
        sqlSb.append(" ) BUDGET WHERE rownum <= "+(pageInfo.getCurrentPage()*pageInfo.getPageSize())+" ) WHERE RN > "+pageInfo.getCurrentRecord());
        String sql = sqlSb.toString();
        return sql;
    }

    public String updatetblLoginType(TblLogintype tblt) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_LOGINTYPE SET LOGINURL = '"+tblt.getLoginurl()+"'");

        if(tblt.getSalehotline() != null && !"".equals(tblt.getSalehotline())) {
            sql.append(" , SALEHOTLINE = '"+tblt.getSalehotline()+"'");
        }
        if(tblt.getEchnicalsupportphone() != null && !"".equals(tblt.getEchnicalsupportphone())) {
            sql.append(" , ECHNICALSUPPORTPHONE = '"+tblt.getEchnicalsupportphone()+"'");
        }
        if(tblt.getLoginpage() != null && !"".equals(tblt.getLoginpage())) {
            sql.append(" , LOGINPAGE = '"+tblt.getLoginpage()+"'");
        }
        if(tblt.getLoginpagetwo() != null && !"".equals(tblt.getLoginpagetwo())) {
            sql.append(" , LOGINPAGETWO = '"+tblt.getLoginpagetwo()+"'");
        }
        if(tblt.getLoginpagethree() != null && !"".equals(tblt.getLoginpagethree())) {
            sql.append(" , LOGINPAGETHREE = '"+tblt.getLoginpagethree()+"'");
        }
        if(tblt.getHomepage() != null && !"".equals(tblt.getHomepage())) {
            sql.append(" , HOMEPAGE = '"+tblt.getHomepage()+"'");
        }
        if(tblt.getHomepagepic() != null && !"".equals(tblt.getHomepagepic())) {
            sql.append(" , HOMEPAGEPIC = '"+tblt.getHomepagepic()+"'");
        }
        if(tblt.getLoginpic() != null && !"".equals(tblt.getLoginpic())) {
            sql.append(" , LOGINPIC = '"+tblt.getLoginpic()+"'");
        }
        if(tblt.getLoginname() != null && !"".equals(tblt.getLoginname())) {
            sql.append(" , LOGINNAME = '"+tblt.getLoginname()+"'");
        }
        if(tblt.getOrgid() != null && !"".equals(tblt.getOrgid())) {
            sql.append(" , ORGID = '"+tblt.getOrgid()+"'");
        }
        sql.append(" WHERE LOGINID = '"+tblt.getLoginid()+"'");
        return sql.toString();
    }

    public String saveTblLoginType(TblLogintype tnt) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_LOGINTYPE (LOGINID");
        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(tnt.getLoginurl() != null) {
            column.append(",LOGINURL");
            value.append(",'"+tnt.getLoginurl()+"'");
        }
        if(tnt.getSalehotline() != null) {
            column.append(",SALEHOTLINE");
            value.append(",'"+tnt.getSalehotline()+"'");
        }
        if(tnt.getEchnicalsupportphone() != null) {
            column.append(",ECHNICALSUPPORTPHONE");
            value.append(",'"+tnt.getEchnicalsupportphone()+"'");
        }
        if(tnt.getLoginpage() != null) {
            column.append(",LOGINPAGE");
            value.append(",'"+tnt.getLoginpage()+"'");
        }
        if(tnt.getLoginpagetwo() != null) {
            column.append(",LOGINPAGETWO");
            value.append(",'"+tnt.getLoginpagetwo()+"'");
        }
        if(tnt.getLoginpagethree() != null) {
            column.append(",LOGINPAGETHREE");
            value.append(",'"+tnt.getLoginpagethree()+"'");
        }
        if(tnt.getHomepage() != null) {
            column.append(",HOMEPAGE");
            value.append(",'"+tnt.getHomepage()+"'");
        }
        if(tnt.getHomepagepic() != null) {
            column.append(",HOMEPAGEPIC");
            value.append(",'"+tnt.getHomepagepic()+"'");
        }
        if(tnt.getLoginpic() != null) {
            column.append(",LOGINPIC");
            value.append(",'"+tnt.getLoginpic()+"'");
        }
        if(tnt.getLoginname() != null) {
            column.append(",LOGINNAME");
            value.append(",'"+tnt.getLoginname()+"'");
        }
        if(tnt.getOrgid() != null) {
            column.append(",ORGID");
            value.append(",'"+tnt.getOrgid()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


}
