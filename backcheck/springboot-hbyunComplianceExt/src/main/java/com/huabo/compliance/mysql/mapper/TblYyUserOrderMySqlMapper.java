package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblYyUserOrderMySql;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface TblYyUserOrderMySqlMapper extends BaseMapper<TblYyUserOrderMySql> {


    @SelectProvider(method = "selectListByPageInfo", type = TblYyUserOrderMapperSqlMySqlConifg.class)
    @Results({
            @Result(column = "ORDERID", property = "orderid"),
            @Result(column = "ORDERCODE", property = "ordercode"),
            @Result(column = "ORDERNO", property = "orderno"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ORDERMONEY", property = "ordermoney"),
            @Result(column = "PAYDATE", property = "paydate"),
            @Result(column = "CREATEDATE", property = "createdate"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "REALNAME", property = "tblStaff.realname"),

    })
    List<TblYyUserOrderMySql> selectListByPageInfo(PageInfo<TblYyUserOrderMySql> pageInfo);

    @SelectProvider(method = "selectCountByPageInfo", type = TblYyUserOrderMapperSqlMySqlConifg.class)
    Integer selectCountByPageInfo(PageInfo<TblYyUserOrderMySql> pageInfo);
}
