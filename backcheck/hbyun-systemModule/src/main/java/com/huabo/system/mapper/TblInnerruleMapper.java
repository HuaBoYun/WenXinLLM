package com.huabo.system.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.system.entity.TblInnerrule;

@Mapper
public interface TblInnerruleMapper extends BaseMapper<TblInnerrule> {

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findListByPageInfoFlow")
    IPage<TblInnerrule> findListByPageInfoFlow(IPage<TblInnerrule> innpage, String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findInnerruleByFolwid")
    IPage<TblInnerrule> findInnerruleByFolwid(IPage<TblInnerrule> page, String orgIdStrs, String flowid);
}
