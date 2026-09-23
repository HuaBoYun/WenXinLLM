package com.huabo.audit.oracle.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAccBook;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblAccBookMapper extends BaseMapper<TblAccBook> {



    @Select(" SELECT STARTDATE,ENDDATE FROM BATHDATA.TBL_FINANCEDATA WHERE COMPANYID = #{orgid} and STATUS = 1 ORDER BY STARTDATE")
    List<Integer> findAcquisitionYear(BigDecimal orgid);

    @Select("SELECT DESTUSERID FROM BATHDATA.TBL_FINANCEDATA WHERE COMPANYID = #{orgid} AND STATUS = 2")
    String findAcquisitoionName(BigDecimal orgId);

    @Select("SELECT * FROM TBL_ACCBOOK WHERE BOOKDESC IS NULL AND ORGID = #{orgId}")
    List<TblAccBook> findAccBookByOrgId(BigDecimal orgId);

    @Select("SELECT * FROM TBL_ACCBOOK WHERE ORGID = #{orgid} AND BOOKID in (SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staffid})")
    List<TblAccBook> listBySql(@Param("staffid") BigDecimal staffid, @Param("orgid") BigDecimal orgid);

    
    @Select("SELECT * FROM TBL_ACCBOOK WHERE  BOOKID in (SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = #{staffid})")
    List<TblAccBook> listBySqlstaffid( BigDecimal staffid);
    
    @Select("SELECT * FROM TBL_ACCBOOK WHERE BOOKID = #{connectionstrings}")
    TblAccBook findByBookIdOne(BigDecimal connectionstrings);
}
