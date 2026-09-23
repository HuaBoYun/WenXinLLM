package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblYyOrgDeposit;
import com.huabo.system.entity.TblYyUserQuery;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-20
 */
@Repository
public interface TblYyOrgDepositMapper extends BaseMapper<TblYyOrgDeposit> {


    @Select("Select * FROM TBL_YY_ORG_DEPOSIT WHERE orgid = #{orgid}")
    TblYyOrgDeposit selectOrgDepositByOrgId(BigDecimal orgid);

    @SelectProvider(method="selectListByPageInfo",type=TblYyOrgDepositMapperSqlConifg.class)
    @Results({
            @Result(column="RECORDID",property="recordid"),
            @Result(column="QUERYSTAFF",property="querystaff"),
            @Result(column="ORGID",property="orgid"),
            @Result(column="QUERYTIME",property="querytime"),
            @Result(column="PAYMONEY",property="paymoney"),
            @Result(column="REPORTNAME",property="reportName"),
            @Result(column="REALNAME",property="tblStaff.realname")
    })
    IPage<TblYyUserQuery> selectListByPageInfo(IPage<TblYyUserQuery> page, TblYyUserQuery yuq);
}
