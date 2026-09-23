package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblYyOrgDepositMySql;
import com.huabo.compliance.mysql.entity.TblYyUserQueryMySql;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Repository
public interface TblYyOrgDepositMySqlMapper extends BaseMapper<TblYyOrgDepositMySql> {


    @Select("Select * FROM TBL_YY_ORG_DEPOSIT WHERE orgid = #{orgid}")
    TblYyOrgDepositMySql selectOrgDepositByOrgId(BigDecimal orgid);

    @SelectProvider(method = "selectListByPageInfo", type = TblYyOrgDepositMapperSqlMySqlConifg.class)
    @Results({
            @Result(column = "RECORDID", property = "recordid"),
            @Result(column = "QUERYSTAFF", property = "querystaff"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "QUERYTIME", property = "querytime"),
            @Result(column = "PAYMONEY", property = "paymoney"),
            @Result(column = "REPORTNAME", property = "reportName"),
            @Result(column = "REALNAME", property = "tblStaff.realname")

    })
    List<TblYyUserQueryMySql> selectListByPageInfo(PageInfo<TblYyUserQueryMySql> pageInfo);

    @SelectProvider(method = "selectCountByPageInfo", type = TblYyOrgDepositMapperSqlMySqlConifg.class)
    Integer selectCountByPageInfo(PageInfo<TblYyUserQueryMySql> pageInfo);

}
