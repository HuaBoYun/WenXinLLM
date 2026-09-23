package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblOuterruleMySql;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface TblOuterruleMySqlMapper extends BaseMapper<TblOuterruleMySql> {


    @SelectProvider(type = TblOuterruleMapperSqlMySqlConfig.class, method = "findListByPageInfoFlow")
    List<TblOuterruleMySql> findListByPageInfoFlow(PageInfo<TblOuterruleMySql> outPageInfo, String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlMySqlConfig.class, method = "findCountByPageInfoFlow")
    Integer findCountByPageInfoFlow(String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlMySqlConfig.class, method = "findInnerruleByFolwid")
    List<TblOuterruleMySql> findInnerruleByFolwid(PageInfo<TblOuterruleMySql> pageInfo, String orgid, String name, String status, String flowid);

    @SelectProvider(type = TblOuterruleMapperSqlMySqlConfig.class, method = "findCountInnerruleByFolwid")
    Integer findCountInnerruleByFolwid(String orgid, String name, String status, String flowid);
}
