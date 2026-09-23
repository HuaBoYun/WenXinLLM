package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.DateUtil;
import com.hbfk.util.PageInfo;
import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblJob;

import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

public class TblBiReportMenuMapperSqlConfig {
	
	
	public String selectFirstRpeortListByPerson(BigDecimal staffid,BigDecimal pageId) {
		String sql = "SELECT TBRM.* FROM TBL_BI_REPORT_MENU TBRM LEFT JOIN TBL_BI_USER_PAGE TBUP ON TBRM.PAGEID = TBUP.PAGEID WHERE TBUP.STAFFID = "+staffid;
		
		if(pageId != null) {
			sql += " AND TBRM.PAGEBODY = "+pageId;
		}else {
			sql += " AND TBRM.TYPE = 0";
		}
		sql += " ORDER BY TBRM.PAGEID ASC";
		return sql;
	}
	
	public String selectFirstRpeortListByModuleType(String moduleType, BigDecimal orgid,BigDecimal pageId) {
		String sql = "SELECT TBRM.* FROM TBL_BI_REPORT_MENU TBRM LEFT JOIN TBL_SYSTEM_BIMODULE TSB ON TBRM.PAGEID = TSB.PAGEID WHERE TSB.ORGID = "+orgid+" AND TSB.MODULETYPE = '"+moduleType+"' ";
		
		if(pageId != null) {
			sql += " AND TSB.PAGEBODY = "+pageId;
		}else {
			sql += " AND TBRM.TYPE = 0 AND TSB.PAGEBODY IS NULL";
		}
		sql += " ORDER BY TBRM.PAGEID ASC";
		return sql;
	}
	
    public String selectType(IPage<TblBiReportMenu> page, String type,BigDecimal orgid) {
        		StringBuffer sbSql = new StringBuffer("select TBL_BI_REPORT_MENU.*,O.ORGNAME from TBL_BI_REPORT_MENU LEFT JOIN TBL_ORGANIZATION O ON TBL_BI_REPORT_MENU.UNIT = O.ORGID WHERE 1=1 ");
                if ("0".equals(type)) {
                    sbSql.append(" AND UNIT=" + orgid + " ");
                    sbSql.append(" AND PAGEBODY IS NULL ");
                    sbSql.append(" AND (TYPE = 2 OR TYPE = 0)");
                }else if("1".equals(type)) {
                    sbSql.append(" AND UNIT=" + orgid + " ");
                    sbSql.append(" AND PAGEBODY IS NULL ");
                    sbSql.append(" AND TYPE = 1");
                } else if (!StringUtil.isNullOrEmpty(type)) {
                    sbSql.append(" AND PAGEBODY = " + type);
                }
        		sbSql.append(" order by PAGEID");
                 return sbSql.toString();
    }

    public String selectMenuList(IPage<TblBiReportMenu> page, Integer type, BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("select * from TBL_BI_REPORT_MENU WHERE 1=1 AND UNIT=" + orgid + " AND PAGEBODY IS NULL");
        if (type != null && "0".equals(type)) {
            sbSql.append(" AND (TYPE = 2 OR TYPE = 0)");
        } else {
            sbSql.append(" AND TYPE = 1");
        }
        sbSql.append("  order by PAGEID ");


        return sbSql.toString();
    }

    public String insertTbrm(TblBiReportMenu tbrm) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_BI_REPORT_MENU (PAGEID");
        StringBuffer value = new StringBuffer(" VALUES (").append(tbrm.getPageid());

        if(tbrm.getPagename() != null) {
            column.append(",PAGENAME");
            value.append(",'"+tbrm.getPagename()+"'");
        }
        if(tbrm.getUrl() != null) {
            column.append(",URL");
            value.append(",'"+tbrm.getUrl()+"'");
        }
        if(tbrm.getForbidden() != null) {
            column.append(",FORBIDDEN");
            value.append(",'"+tbrm.getForbidden()+"'");
        }
        if(tbrm.getUnit() != null) {
            column.append(",UNIT");
            value.append(",'"+tbrm.getUnit()+"'");
        }
        if(tbrm.getPageuser() != null) {
            column.append(",PAGEUSER");
            value.append(",'"+tbrm.getPageuser()+"'");
        }
        if(tbrm.getCreater() != null) {
            column.append(",CREATER");
            value.append(",'"+tbrm.getCreater()+"'");
        }
        if(tbrm.getMemo1() != null) {
            column.append(",MEMO1");
            value.append(",'"+tbrm.getMemo1()+"'");
        }
        if(tbrm.getMemo2() != null) {
            column.append(",MEMO2");
            value.append(",'"+tbrm.getMemo2()+"'");
        }
        if(tbrm.getPagecode() != null) {
            column.append(",PAGECODE");
            value.append(",'"+tbrm.getPagecode()+"'");
        }
        if(tbrm.getTheme() != null) {
            column.append(",THEME");
            value.append(",'"+tbrm.getTheme()+"'");
        }
        if(tbrm.getTreeid() != null) {
            column.append(",TREEID");
            value.append(",'"+tbrm.getTreeid()+"'");
        }
        if(tbrm.getCreatedate() != null) {
            column.append(",CREATEDATE");
            value.append(",").append(DataBaseSqlConfig.getDateStrFormat(tbrm.getCreatedate()));
        }
        if(tbrm.getPagebody() != null) {
            column.append(",PAGEBODY");
            value.append(",'"+tbrm.getPagebody()+"'");
        }
        if(tbrm.getPageDes() != null) {
            column.append(",PAGEDES");
            value.append(",'"+tbrm.getPageDes()+"'");
        }
        if(tbrm.getRqurl() != null) {
            column.append(",RQURL");
            value.append(",'"+tbrm.getRqurl()+"'");
        }
        if(tbrm.getType() != null) {
            column.append(",TYPE");
            value.append(",'"+tbrm.getType()+"'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }

    public String updateReportMenu(TblBiReportMenu page) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_BI_REPORT_MENU SET ");

        if(page.getPagename() != null && !"".equals(page.getPagename())) {
            sql.append("  PAGENAME = '"+page.getPagename()+"'");
        }
        if(page.getUrl() != null && !"".equals(page.getUrl())) {
            sql.append(" , URL = '"+page.getUrl()+"'");
        }
        if(page.getForbidden() != null && !"".equals(page.getForbidden())) {
            sql.append(" , FORBIDDEN = '"+page.getForbidden()+"'");
        }
        if(page.getUnit() != null && !"".equals(page.getUnit())) {
            sql.append(" , UNIT = '"+page.getUnit()+"'");
        }
        if(page.getPageuser() != null && !"".equals(page.getPageuser())) {
            sql.append(" , PAGEUSER = '"+page.getPageuser()+"'");
        }
        if(page.getCreater() != null && !"".equals(page.getCreater())) {
            sql.append(" , CREATER = '"+page.getCreater()+"'");
        }
        if(page.getMemo1() != null && !"".equals(page.getMemo1())) {
            sql.append(" , MEMO1 = '"+page.getMemo1()+"'");
        }
        if(page.getMemo2() != null && !"".equals(page.getMemo2())) {
            sql.append(" , MEMO2 = '"+page.getMemo2()+"'");
        }
        if(page.getPagecode() != null && !"".equals(page.getPagecode())) {
            sql.append(" , PAGECODE = '"+page.getPagecode()+"'");
        }
        if(page.getTheme() != null && !"".equals(page.getTheme())) {
            sql.append(" , THEME = '"+page.getTheme()+"'");
        }
        if(page.getTreeid() != null && !"".equals(page.getTreeid())) {
            sql.append(" , TREEID = '"+page.getTreeid()+"'");
        }
        if(page.getCreatedate() != null && !"".equals(page.getCreatedate())) {
            sql.append(" , CREATEDATE = '"+page.getCreatedate()+"'");
        }
        if(page.getPagebody() != null && !"".equals(page.getPagebody())) {
            sql.append(" , PAGEBODY = '"+page.getPagebody()+"'");
        }
        if(page.getPageDes() != null && !"".equals(page.getPageDes())) {
            sql.append(" , PAGEDES = '"+page.getPageDes()+"'");
        }
        if(page.getRqurl() != null && !"".equals(page.getRqurl())) {
            sql.append(" , RQURL = '"+page.getRqurl()+"'");
        }
        if(page.getType() != null && !"".equals(page.getType())) {
            sql.append(" , TYPE = '"+page.getType()+"'");
        }

        sql.append(" WHERE PAGEID = '"+page.getPageid()+"'");
        return sql.toString();
    }

    public String addPage(TblBiReportMenu page) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_BI_REPORT_MENU (PAGEID");
        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval ");

        if(page.getUrl() != null) {
            column.append(",URL");
            value.append(",'"+page.getUrl()+"'");
        }
        if(page.getForbidden() != null) {
            column.append(",FORBIDDEN");
            value.append(",'"+page.getForbidden()+"'");
        }
        if(page.getUnit() != null) {
            column.append(",UNIT");
            value.append(",'"+page.getUnit()+"'");
        }
        if(page.getPageuser() != null) {
            column.append(",PAGEUSER");
            value.append(",'"+page.getPageuser()+"'");
        }
        if(page.getCreater() != null) {
            column.append(",CREATER");
            value.append(",'"+page.getCreater()+"'");
        }
        if(page.getMemo1() != null) {
            column.append(",MEMO1");
            value.append(",'"+page.getMemo1()+"'");
        }
        if(page.getMemo2() != null) {
            column.append(",MEMO2");
            value.append(",'"+page.getMemo2()+"'");
        }
        if(page.getPagecode() != null) {
            column.append(",PAGECODE");
            value.append(",'"+page.getPagecode()+"'");
        }
        if(page.getTheme() != null) {
            column.append(",THEME");
            value.append(",'"+page.getTheme()+"'");
        }
        if(page.getTreeid() != null) {
            column.append(",TREEID");
            value.append(",'"+page.getTreeid()+"'");
        }
        if(page.getCreatedate() != null) {
            column.append(",CREATEDATE");
            value.append(",TO_DATE('"+ DateUtil.parseDate(page.getCreatedate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(page.getPagebody() != null) {
            column.append(",PAGEBODY");
            value.append(",'"+page.getPagebody()+"'");
        }
        if(page.getPageDes() != null) {
            column.append(",PAGEDES");
            value.append(",'"+page.getPageDes()+"'");
        }
        if(page.getRqurl() != null) {
            column.append(",RQURL");
            value.append(",'"+page.getRqurl()+"'");
        }
        if(page.getType() != null) {
            column.append(",TYPE");
            value.append(",'"+page.getType()+"'");
        }

        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }
}
