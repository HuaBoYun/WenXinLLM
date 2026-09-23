package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblFinanceData;
import com.huabo.system.mappersql.TblFinanceDataMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
public interface TblFinanceDataMapper extends BaseMapper<TblFinanceData> {

    @Select("delete from BATHDATA.TBL_FINANCEDATA WHERE ORDERID= #{orderId}")
    void deleteByorderid(String orderId);

    @SelectProvider(type=TblFinanceDataMapperSqlConfig.class,method="selectListByPageInfo")
    IPage<TblFinanceData> selectListByPageInfo(IPage<TblFinanceData> page, BigDecimal orgid);

    @Select("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID WHERE STATUS=1  AND COMPANYID= #{companyid} AND  ORDERID != #{orderId}")
    List<TblFinanceData> selectDateByCompanyidAndOrderId(BigDecimal companyid, BigDecimal orderId);

    @Select("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID WHERE STATUS=1  AND COMPANYID= #{companyid}")
    List<TblFinanceData> selectDateByCompanyid(BigDecimal companyid);


    @Select("SELECT A.*,B.FVENDOR FROM BATHDATA.TBL_FINANCEDATA A LEFT JOIN BATHDATA.TBL_VERSION B ON A.FID=B.FID where 1=1 and A.STATUS = 2 AND A.COMPANYID= #{companyid} and rownum=1")
    TblFinanceData selectModuleName(BigDecimal companyid);

    @Insert("select count(*) from dba_users WHERE USERNAME= #{newBookName}")
    int checkSchema(String newBookName);

    @Select("SELECT * FROM BATHDATA.TBL_FINANCEDATA WHERE ORDERID= #{selectid}")
    TblFinanceData selectByOrderId(String selectid);

    @InsertProvider(type=TblFinanceDataMapperSqlConfig.class,method="saveFirst")
    Integer saveFirst(TblFinanceData tlf);

    @UpdateProvider(type=TblFinanceDataMapperSqlConfig.class,method="updateFin")
    void updateFin(TblFinanceData tlf);

//    @Select("SELECT MODELNAME FROM BATHDATA.TBL_ORG_EXCELMODEL WHERE ORGID = #{orgid} AND TYPEID = #{type}")
//    String selectName(Integer type, BigDecimal orgid);

    @Select("SELECT MODELNAME FROM BATHDATA.TBL_ORG_EXCELMODEL WHERE TYPEID = #{type}")
    String selectName(Integer type, BigDecimal orgid);

}
