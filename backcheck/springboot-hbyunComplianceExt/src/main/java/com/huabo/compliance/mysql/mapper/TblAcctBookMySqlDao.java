package com.huabo.compliance.mysql.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblAcctBookMySql;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblAcctBookMySqlDao extends BaseMapper<TblAcctBookMySql> {


    //    @SelectProvider(method="selectCountt",type=TblAcctBookDaoSqlConfig.class)
    @Select("SELECT COUNT(*) FROM TBL_ACCBOOK tb left join TBL_ORGANIZATION toz on tb.ORGID = toz.ORGID WHERE toz.ORGID = '116821' AND BOOKDESC IS NULL")
    Integer selectCountt(BigDecimal orgid);

    @Select("SELECT STARTDATE,ENDDATE FROM bathdata.TBL_FINANCEDATA WHERE COMPANYID = #{orgid} and STATUS = 1 ORDER BY STARTDATE")
    List<Integer> findAcquisitionYear(BigDecimal orgid);

    @Select("SELECT DESTUSERID FROM BATHDATA.TBL_FINANCEDATA WHERE COMPANYID = #{orgId} AND STATUS = 2")
    String findAcquisitoionName(BigDecimal orgId);

    @SelectProvider(method = "findByTypeNewZB", type = TblAcctBookMySqlDaoSqlConfig.class)
    List<TblAcctBookMySql> findByTypeNewZB(PageInfo<TblAcctBookMySql> pageInfo, BigDecimal orgid);


}
