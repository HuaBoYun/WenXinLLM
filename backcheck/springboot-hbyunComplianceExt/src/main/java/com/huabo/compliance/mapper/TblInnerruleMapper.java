package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblInnerrule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblInnerruleMapper extends BaseMapper<TblInnerrule> {

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findListByPageInfoFlow")
    List<TblInnerrule> findListByPageInfoFlow(PageInfo<TblInnerrule> pageInfo, String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findCountByPageInfoFlow")
    Integer findCountByPageInfoFlow(String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findInnerruleByFolwid")
    List<TblInnerrule> findInnerruleByFolwid(PageInfo<TblInnerrule> pageInfo, String orgid, String flowid);

    @SelectProvider(type = TblInnerruleMapperSqlConfig.class, method = "findCountInnerruleByFolwid")
    Integer findCountInnerruleByFolwid(String orgid, String flowid);

    @Select("select * from TBL_INNERRULE where innrulid = #{innrulid}")
    TblInnerrule findById(@Param("innrulid") BigDecimal innrulid);
}
