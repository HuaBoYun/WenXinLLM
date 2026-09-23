package com.huabo.system.mapper;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.mappersql.TblAcctBookDaoSqlConfig;
public interface TblAcctBookDao extends BaseMapper<TblAcctBook> {

    @Select("SELECT STARTDATE,ENDDATE FROM bathdata.TBL_FINANCEDATA WHERE COMPANYID = #{orgid} and STATUS = 1 ORDER BY STARTDATE")
    List<Integer> findAcquisitionYear(BigDecimal orgid);

    @Select("SELECT DESTUSERID FROM BATHDATA.TBL_FINANCEDATA WHERE COMPANYID = #{orgid} AND STATUS = 2")
    String findAcquisitoionName(BigDecimal orgId);

    @SelectProvider(method="findByTypeNewZB",type=TblAcctBookDaoSqlConfig.class)
    IPage<TblAcctBook> findByTypeNewZB(IPage<TblAcctBook> page,BigDecimal orgid);


}
