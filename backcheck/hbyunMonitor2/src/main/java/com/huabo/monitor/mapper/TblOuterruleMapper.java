package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.entity.TblOuterrule;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblOuterruleMapper extends BaseMapper<TblOuterrule> {


    @SelectProvider(type = TblOuterruleMapperSqlConfig.class, method = "findListByPageInfoFlow")
    List<TblOuterrule> findListByPageInfoFlow(PageInfo<TblOuterrule> outPageInfo, String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlConfig.class, method = "findCountByPageInfoFlow")
    Integer findCountByPageInfoFlow(String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlConfig.class, method = "findInnerruleByFolwid")
    List<TblOuterrule> findInnerruleByFolwid(PageInfo<TblOuterrule> pageInfo, String orgid, String name, String status, String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlConfig.class, method = "findCountInnerruleByFolwid")
    Integer findCountInnerruleByFolwid(String orgid, String name, String status, String flowid);

    @Select("select * from TBL_OUTERRULE where outrulid = #{outrulid}")
    TblOuterrule findById(@Param("outrulid") BigDecimal outrulid);
}
