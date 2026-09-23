package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblAccountMySql;
import com.huabo.compliance.mysql.entity.TblOrgExcelMySql;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;

public interface TblAccountMySqlMapper extends BaseMapper<TblAccountMySql> {
    @Select("SELECT TOE.EXCELID,TOE.ORGID,TOE.STARTYEAR,TOE.ENDYEAR,TOE.FUNCTIONNAME,TOE.STARTROWS FROM dataName.TBL_ORG_EXCEL TOE LEFT JOIN TBL_ACCOUNT TA ON TOE.ORGID = TA.ORGID WHERE TOE.ORGID = #{organization.getOrgid()} AND TOE.STARTYEAR <= #{year} AND TOE.ENDYEAR >= #{year} ")
    TblOrgExcelMySql findTblSqlOrg(String dataName, BigDecimal orgid, String year, String year1);

    @Select("SELECT COUNT(*) FROM TBL_ACCOUNT WHERE ORGID = #{orgid} AND FYEAR =  #{year} ")
    Integer listBySqlPageCount(String dataName, BigDecimal orgid, String year);
}
