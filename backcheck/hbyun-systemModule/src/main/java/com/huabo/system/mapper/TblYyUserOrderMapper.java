package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.TblYyUserOrder;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblYyUserOrderMapper extends BaseMapper<TblYyUserOrder> {


    @SelectProvider(method="selectListByPageInfo",type=TblYyUserOrderMapperSqlConifg.class)
    @Results({
            @Result(column="ORDERID",property="orderid"),
            @Result(column="ORDERCODE",property="ordercode"),
            @Result(column="ORDERNO",property="orderno"),
            @Result(column="STATUS",property="status"),
            @Result(column="ORDERMONEY",property="ordermoney"),
            @Result(column="PAYDATE",property="paydate"),
            @Result(column="CREATEDATE",property="createdate"),
            @Result(column="ORGID",property="orgid"),
           @Result(column="REALNAME",property="tblStaff.realname"),
    })
    IPage<TblYyUserOrder> selectListByPageInfo(IPage<TblYyUserOrder> page, TblYyUserOrder yuo);
}
