package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblAccount;
import com.huabo.system.entity.TblOrgExcel;

public interface TblAccountMapper extends BaseMapper<TblAccount> {
    @Select("SELECT TOE.EXCELID,TOE.ORGID,TOE.STARTYEAR,TOE.ENDYEAR,TOE.FUNCTIONNAME,TOE.STARTROWS FROM dataName.TBL_ORG_EXCEL TOE LEFT JOIN TBL_ACCOUNT TA ON TOE.ORGID = TA.ORGID WHERE TOE.ORGID = #{orgid} AND TOE.STARTYEAR <= #{year} AND TOE.ENDYEAR >= #{year} ")
    TblOrgExcel findTblSqlOrg(String dataName, BigDecimal orgid, String year, String year1);

    @Select("SELECT COUNT(*) FROM TBL_ACCOUNT WHERE ORGID = #{orgid} AND FYEAR =  #{year} ")
    Integer listBySqlPageCount(String dataName, BigDecimal orgid, String year);
}
