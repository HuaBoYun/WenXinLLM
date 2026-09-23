package com.huabo.monitor.oracle.mapper;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.oracle.entity.TblAssesselement;

public class TblAssesselementMapperSqlConfig {

    public String findByPageBean (PageInfo<TblAssesselement> pageInfo){
        TblAssesselement tblAssesselement = pageInfo.getCondition();
        StringBuffer sbSql = new StringBuffer("SELECT * FROM TBL_ASSESSELEMENT WHERE 1 = 1");
        if (StringUtils.isNotBlank(tblAssesselement.getBusinesstype())){
            sbSql.append(" AND BUSINESSTYPE = " + tblAssesselement.getBusinesstype());
        }
        if (StringUtils.isNotBlank(tblAssesselement.getElementNumber())){
            sbSql.append(" AND ELEMENTNUMBER = "+ tblAssesselement.getElementNumber());
        }
        if (StringUtils.isNotBlank(tblAssesselement.getElementname())){
            sbSql.append(" AND ELEMENTNAME = "+ tblAssesselement.getElementname());
        }
        if (StringUtils.isNotBlank(tblAssesselement.getAuditpoint())){
            sbSql.append(" AND AUDITPOINT = "+ tblAssesselement.getAuditpoint());
        }
        if (StringUtils.isNotBlank(tblAssesselement.getTblComany())){
            sbSql.append(" AND COMANY = "+tblAssesselement.getTblComany());
        }
        String sql = sbSql.toString();
        return sql;
    }

}
