package com.huabo.compliance.mysql.mapper;

import com.huabo.compliance.mysql.entity.TblRiskcategoryMySql;
import org.apache.commons.lang.StringUtils;

public class TblRiskcategoryMapperSqlMySqlConifg {

    public String findBysql(String orgid, String type) {
        String sql = "select * from TBL_RISKCATEGORY where RISKCATNAME in ('企业风险','业务风险','专项风险') and unit = '" + orgid + "'";
        if (StringUtils.isNotEmpty(type)) {
            sql += " and moduletype = '" + type + "'";
        }
        return sql;
    }

//	public String updatecat(cat cat) {
//		StringBuffer sql = new StringBuffer("UPDATE TBL_COURSE SET COURSENAME = '"+cat.getCoursename()+"'");
//
//		if(cat.getMemo() != null && !"".equals(cat.getMemo())) {
//			sql.append(" , MEMO = '"+cat.getMemo()+"'");
//		}
//		if(cat.getParentid() != null && !"".equals(cat.getParentid())) {
//			sql.append(" , PARENTID = '"+cat.getParentid()+"'");
//		}
//		if(cat.getPicurl() != null && !"".equals(cat.getPicurl())) {
//			sql.append(" , PICURL = '"+cat.getPicurl()+"'");
//		}
//		if(cat.getVideourl() != null && !"".equals(cat.getVideourl())) {
//			sql.append(" , VIDEORUL = '"+cat.getVideourl()+"'");
//		}
//		if(cat.getUserid() != null && !"".equals(cat.getUserid())) {
//			sql.append(" , USERID = '"+cat.getUserid()+"'");
//		}
//		if(cat.getOrgid() != null && !"".equals(cat.getOrgid())) {
//			sql.append(" , ORGID = '"+cat.getOrgid()+"'");
//		}
//		if(cat.getCreateDate() != null) {
//			sql.append(" ,CREATEDATE = STR_TO_DATE('"+ DateUtil.parseDate(cat.getCreateDate(),"%Y-%m-%d %H:%i:%s") +"', '%Y-%m-%d HH24:MI:SS')");
//		}
//		if(cat.getCoursetype() != null && !"".equals(cat.getCoursetype())) {
//			sql.append(" , COURSETYPE = '"+cat.getCoursetype()+"'");
//		}
//		if(cat.getCoursenumber() != null && !"".equals(cat.getCoursenumber())) {
//			sql.append(" , COURSENUMBER = '"+cat.getCoursenumber()+"'");
//		}
//		if(cat.getType() != null && !"".equals(cat.getType())) {
//			sql.append(" , TYPE = '"+cat.getType()+"'");
//		}
//		if(cat.getCourseware() != null && !"".equals(cat.getCourseware())) {
//			sql.append(" , COURSEWARE = '"+cat.getCourseware()+"'");
//		}
//		if(cat.getCoursewareurl() != null && !"".equals(cat.getCoursewareurl())) {
//			sql.append(" , COURSEWAREURL = '"+cat.getCoursewareurl()+"'");
//		}
//		sql.append(" WHERE COURSEID = '"+cat.getCourseid()+"'");
//		return sql.toString();
//	}

    public String save(TblRiskcategoryMySql cat) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_RISKCATEGORY (RISKCATID");
        StringBuffer value = new StringBuffer(" VALUES ( "+ cat.getRiskcatid() + " ");

        if (cat.getRiskcatnumber() != null) {
            column.append(",RISKCATNUMBER");
            value.append(",'" + cat.getRiskcatnumber() + "'");
        }
        if (cat.getRiskcatname() != null) {
            column.append(",RISKCATNAME");
            value.append(",'" + cat.getRiskcatname() + "'");
        }
        if (cat.getRiskcatdes() != null) {
            column.append(",RISKCATDES");
            value.append(",'" + cat.getRiskcatdes() + "'");
        }
        if (cat.getRiskstatus() != null) {
            column.append(",RISKSTATUS");
            value.append(",'" + cat.getRiskstatus() + "'");
        }
        if (cat.getFatherriskcatid() != null) {
            column.append(",FATHERRISKCATID");
            value.append(",'" + cat.getFatherriskcatid() + "'");
        }
        if (cat.getFullpath() != null) {
            column.append(",FULLPATH");
            value.append(",'" + cat.getFullpath() + "'");
        }
        if (cat.getMemo() != null) {
            column.append(",MEMO");
            value.append(",'" + cat.getMemo() + "'");
        }
        if (cat.getIsleaf() != null) {
            column.append(",ISLEAF");
            value.append(",'" + cat.getIsleaf() + "'");
        }
        if (cat.getUnit() != null) {
            column.append(",UNIT");
            value.append(",'" + cat.getUnit() + "'");
        }
        if (cat.getModuletype() != null) {
            column.append(",MODULETYPE");
            value.append(",'" + cat.getModuletype() + "'");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString() + value.toString();
        return sql;
    }


}
