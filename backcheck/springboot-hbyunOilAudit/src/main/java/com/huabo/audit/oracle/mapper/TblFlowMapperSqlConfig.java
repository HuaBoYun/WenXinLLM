package com.huabo.audit.oracle.mapper;

import com.hbfk.util.DateUtil;
import com.huabo.audit.oracle.entity.TblFormControllog;

public class TblFlowMapperSqlConfig {
	public String saveTfl(TblFormControllog tfl) {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_FORM_CONTROLLOG (RULELOGID");
        StringBuffer value = new StringBuffer(" VALUES (HIBERNATE_SEQUENCE.nextval");

        if(tfl.getCreatedate() != null && !"".equals(tfl.getCreatedate())){
            column.append(",CREATEDATE");
            value.append(",TO_DATE('"+ DateUtil.parseDate(tfl.getCreatedate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(tfl.getOperation() != null && !"".equals(tfl.getOperation())){
            column.append(",OPERATION");
            value.append(",'"+tfl.getOperation()+"'");
        }
        if(tfl.getReturnresult() != null && !"".equals(tfl.getReturnresult())){
            column.append(",RETURNRESULT");
            value.append(",'"+tfl.getReturnresult()+"'");
        }
        if(tfl.getInparam() != null && !"".equals(tfl.getInparam())){
            column.append(",INPARAM");
            value.append(",'"+tfl.getInparam()+"'");
        }
        if(tfl.getRuleno() != null && !"".equals(tfl.getRuleno())){
            column.append(",RULENO");
            value.append(",'"+tfl.getRuleno()+"'");
        }
        if(tfl.getExecutestaff() != null && !"".equals(tfl.getExecutestaff())){
            column.append(",EXECUTESTAFF");
            value.append(",'"+tfl.getExecutestaff()+"'");
        }
        if(tfl.getReturndate() != null && !"".equals(tfl.getReturndate())){
            column.append(",RETURNDATE");
            value.append(",TO_DATE('"+ DateUtil.parseDate(tfl.getReturndate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateTfl(TblFormControllog tfl) {
        StringBuffer sql = new StringBuffer("UPDATE TBL_FORM_CONTROLLOG SET EXECUTESTAFF = '"+tfl.getExecutestaff()+"'");

        if(tfl.getCreatedate() != null && !"".equals(tfl.getCreatedate())) {
            sql.append(" ,CREATEDATE = TO_DATE('"+DateUtil.parseDate(tfl.getCreatedate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }
        if(tfl.getOperation() != null && !"".equals(tfl.getOperation())) {
            sql.append(" , OPERATION = '"+tfl.getOperation()+"'");
        }
        if(tfl.getReturnresult() != null && !"".equals(tfl.getReturnresult())) {
            sql.append(" , RETURNRESULT = '"+tfl.getReturnresult()+"'");
        }
        if(tfl.getInparam() != null && !"".equals(tfl.getInparam())) {
            sql.append(" , INPARAM = '"+tfl.getInparam()+"'");
        }
        if(tfl.getRuleno() != null && !"".equals(tfl.getRuleno())) {
            sql.append(" , RULENO = '"+tfl.getRuleno()+"'");
        }
        if(tfl.getReturndate() != null && !"".equals(tfl.getReturndate())) {
            sql.append(" ,RETURNDATE = TO_DATE('"+DateUtil.parseDate(tfl.getReturndate(),"yyyy-MM-dd HH:mm:ss") +"', 'YYYY-MM-DD HH24:MI:SS')");
        }

        sql.append(" WHERE RULELOGID = '"+tfl.getRulelogid()+"'");
        return sql.toString();
    }
}
