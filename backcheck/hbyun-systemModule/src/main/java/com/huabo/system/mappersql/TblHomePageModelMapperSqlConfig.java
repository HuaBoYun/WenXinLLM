package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblHomePageModel;

import java.math.BigDecimal;

public class TblHomePageModelMapperSqlConfig {

    public String homePageModels(IPage<TblHomePageModel> page, BigDecimal staffid) {
        StringBuffer sbSql = new StringBuffer("select PA.name,pa.url,pa.type,j.tblWidth,pa.id from TBL_HOME_PAGE_MODEL pa " +
                "inner join TBL_HOME_PAGE_JURISDICTION j on pa.id=j.MODELID and j.STAFFID= "+staffid );
        String sql = sbSql.toString();
        return sql;
    }

}
