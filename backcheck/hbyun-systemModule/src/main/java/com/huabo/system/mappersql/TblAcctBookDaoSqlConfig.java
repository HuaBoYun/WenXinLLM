package com.huabo.system.mappersql;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.entity.TblAcquisitionRecord;

import java.math.BigDecimal;

public class TblAcctBookDaoSqlConfig {

    public String findByTypeNewZB(IPage<TblAcctBook> page,BigDecimal orgid) {
        StringBuffer sbSql = new StringBuffer("SELECT TB.*,TOZ.ORGNAME AS COMPANYNAME FROM TBL_ACCBOOK TB LEFT JOIN TBL_ORGANIZATION TOZ ON TB.ORGID = TOZ.ORGID WHERE TOZ.ORGID = "+orgid+" AND TB.BOOKDESC IS NULL ORDER BY BOOKYEAR DESC");
        return sbSql.toString();
    }

}
