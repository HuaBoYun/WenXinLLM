package com.huabo.contract.mappersql;

import com.hbfk.util.database.DataBaseSqlConfig;
import com.huabo.contract.entity.TblFormControllog;

public class TblFlowMapperSqlConfig {


    public String saveTfl(TblFormControllog tfl) throws Exception {
        StringBuffer column = new StringBuffer("INSERT INTO TBL_FORM_CONTROLLOG (RULELOGID");
        StringBuffer value = new StringBuffer(" VALUES ("+tfl.getRulelogid());

        if(tfl.getCreatedate() != null){
            column.append(",CREATEDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tfl.getCreatedate()));
        }
        if(tfl.getOperation() != null ){
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
        if(tfl.getReturndate() != null){
            column.append(",RETURNDATE");
            value.append(","+ DataBaseSqlConfig.getDateStrFormat(tfl.getReturndate()));
        }
        column.append(")");
        value.append(")");
        String sql = column.toString()+value.toString();
        return sql;
    }


    public String updateTfl(TblFormControllog tfl) throws Exception {
        StringBuffer sql = new StringBuffer("UPDATE TBL_FORM_CONTROLLOG SET EXECUTESTAFF = '"+tfl.getExecutestaff()+"'");

        if(tfl.getCreatedate() != null) {
            sql.append(" ,CREATEDATE = "+ DataBaseSqlConfig.getDateStrFormat(tfl.getCreatedate()));
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
        if(tfl.getReturndate() != null) {
            sql.append(" ,RETURNDATE = "+ DataBaseSqlConfig.getDateStrFormat(tfl.getCreatedate()));
        }

        sql.append(" WHERE RULELOGID = '"+tfl.getRulelogid()+"'");
        return sql.toString();
    }
}
