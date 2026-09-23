package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblInnerruleMySql;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@Mapper
public interface TblInnerruleMySqlMapper extends BaseMapper<TblInnerruleMySql> {

    @SelectProvider(type = TblInnerruleMapperSqlMySqlConfig.class, method = "findListByPageInfoFlow")
    List<TblInnerruleMySql> findListByPageInfoFlow(PageInfo<TblInnerruleMySql> pageInfo, String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlMySqlConfig.class, method = "findCountByPageInfoFlow")
    Integer findCountByPageInfoFlow(String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlMySqlConfig.class, method = "findInnerruleByFolwid")
    List<TblInnerruleMySql> findInnerruleByFolwid(PageInfo<TblInnerruleMySql> pageInfo, String orgid, String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlMySqlConfig.class, method = "findCountInnerruleByFolwid")
    Integer findCountInnerruleByFolwid(String orgid, String flowid);
}
