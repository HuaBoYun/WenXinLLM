package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblFinanceDataMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblFinanceDataMySqlMapper extends BaseMapper<TblFinanceDataMySql> {

    @Select("delete from BATHDATA.TBL_FINANCEDATA WHERE ORDERID= #{orderId}")
    void deleteByorderid(String orderId);


    @SelectProvider(type = TblFinanceDataMapperSqlMySqlConfig.class, method = "selectListByPageInfo")
    List<TblFinanceDataMySql> selectListByPageInfo(PageInfo<TblFinanceDataMySql> pageInfo, BigDecimal orgid);

    @Select("SELECT COUNT(*) FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID where 1=1 and A.STATUS=1 AND A.COMPANYID= #{orgid}")
    Integer selectCountByPageInfo(PageInfo<TblFinanceDataMySql> pageInfo, BigDecimal orgid);


    @Select("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID WHERE STATUS=1  AND COMPANYID= #{companyid} AND  ORDERID != #{orderId}")
    List<TblFinanceDataMySql> selectDateByCompanyidAndOrderId(BigDecimal companyid, BigDecimal orderId);

    @Select("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID WHERE STATUS=1  AND COMPANYID= #{companyid}")
    List<TblFinanceDataMySql> selectDateByCompanyid(BigDecimal companyid);


    @Select("SELECT A.*,B.FVENDOR,@ROW := @ROW + 1 AS SIGNED from(SELECT @ROW := 0) R, BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID where 1=1 and A.STATUS=2 AND A.COMPANYID= #{companyid} HAVING SIGNED = 1 ")
    TblFinanceDataMySql selectModuleName(BigDecimal companyid);

    @Insert("select count(*) from dba_users WHERE USERNAME= #{newBookName}")
    int checkSchema(String newBookName);

    @Select("SELECT * FROM BATHDATA.TBL_FINANCEDATA WHERE ORDERID= #{selectid}")
    TblFinanceDataMySql selectByOrderId(String selectid);

    @InsertProvider(type = TblFinanceDataMapperSqlMySqlConfig.class, method = "saveFirst")
    Integer saveFirst(TblFinanceDataMySql tlf);

    @UpdateProvider(type = TblFinanceDataMapperSqlMySqlConfig.class, method = "updateFin")
    void updateFin(TblFinanceDataMySql tlf);

//    @Select("SELECT MODELNAME FROM BATHDATA.TBL_ORG_EXCELMODEL WHERE ORGID = #{orgid} AND TYPEID = #{type}")
//    String selectName(Integer type, BigDecimal orgid);

    @Select("SELECT MODELNAME FROM BATHDATA.TBL_ORG_EXCELMODEL WHERE TYPEID = #{type}")
    String selectName(Integer type, BigDecimal orgid);

}
