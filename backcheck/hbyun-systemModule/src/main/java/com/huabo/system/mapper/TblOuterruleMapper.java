package com.huabo.system.mapper;

import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblOuterrule;

@org.apache.ibatis.annotations.Mapper
public interface TblOuterruleMapper extends BaseMapper<TblOuterrule> {


    @SelectProvider(type= TblOuterruleMapperSqlConfig.class,method = "findListByPageInfoFlow")
    IPage<TblOuterrule> findListByPageInfoFlow(IPage<TblOuterrule> outpage, String flowid);
    
    
    @SelectProvider(type= TblOuterruleMapperSqlConfig.class,method = "findCountByPageInfoFlow")
    Integer findCountByPageInfoFlow(String flowid);

    @SelectProvider(type= TblOuterruleMapperSqlConfig.class,method = "findInnerruleByFolwid")
    IPage<TblOuterrule> findInnerruleByFolwid(IPage<TblOuterrule> page, String orgid, String name, String status, String flowid);
}
