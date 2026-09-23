package com.huabo.audit.oracle.mapper;

import com.hbfk.util.PageInfo;
import com.hbfk.util.StringUtil;
import com.huabo.audit.oracle.entity.QuarterEntity;
import com.huabo.audit.oracle.entity.RequireSuggestionEntity;
import java.util.Date;
import cn.hutool.core.date.DateUtil;
public class QuarterMapperSqlConfig {

    public String selectByEntity( QuarterEntity entity){
        StringBuffer sb = new StringBuffer();
        //	sb.append("SELECT * FROM (SELECT T1.* , ROWNUM RN FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUARTER RS  WHERE 1=1 ");
        addQuery(sb,entity);
        //	sb.append(") T1 WHERE ROWNUM <= "+(pageInfo.getCurrentRecord() + pageInfo.getPageSize() )+" ) T2 WHERE T2.RN > "+ pageInfo.getCurrentRecord());
        return sb.toString();
    }

    public void addQuery(StringBuffer sb,QuarterEntity entity){
        if(entity.getQuarter() != null){
            sb.append("AND RS.QUARTER = "+ entity.getQuarter());
        }
        if(StringUtil.isNotEmpty(entity.getOrgName())){
            sb.append("AND RS.ORG_ID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%"+entity.getOrgName()+"%')");
        }
        if(entity.getOrgId() != null){
            sb.append("AND RS.ORG_ID = "+ entity.getOrgId());
        }
        if(entity.getType() != null){
            sb.append("AND RS.TYPE = "+ entity.getType());
        }
    }

    public String selectCountByEntity(QuarterEntity entity){
        StringBuffer sb = new StringBuffer();
        sb.append("SELECT COUNT(0) FROM (");
        sb.append("SELECT * FROM TBL_YQNS_QUARTER RS  WHERE 1=1 ");
        addQuery(sb,entity);
        sb.append(")");
        return sb.toString();
    }

    public String insertEntity(QuarterEntity entity){
        StringBuffer colSb = new StringBuffer();
        colSb.append("INSERT INTO TBL_YQNS_QUARTER (ID");

        StringBuffer valSb = new StringBuffer();
        valSb.append(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(entity.getQuarter() != null){
            colSb.append(", QUARTER");
            valSb.append(", '"+ entity.getQuarter()+"'");
        }

        if(entity.getQuarter() != null){
            colSb.append(", ORG_ID");
            valSb.append(", '"+ entity.getOrgId()+"'");
        }

        if(entity.getCreateUser() != null){
            colSb.append(", CREATE_USER");
            valSb.append(", '" + entity.getCreateUser().getStaffid() + "'");
        }

        colSb.append(", CREATE_TIME)");
        valSb.append(", '"+ DateUtil.format(new Date(),"yyyy-MM-dd")+"')");

        colSb.append(valSb);
        return colSb.toString();
    }
}
