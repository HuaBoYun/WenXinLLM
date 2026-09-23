package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.TblRiskcategory;
import com.huabo.system.entity.TblYyUserQuery;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblRiskcategoryMapper extends BaseMapper<TblRiskcategory> {


    @SelectProvider(type=TblRiskcategoryMapperSqlConifg.class,method="findBysql")
    List<TblRiskcategory> findBysql(String orgid, String type);

    @InsertProvider(type=TblRiskcategoryMapperSqlConifg.class,method="save")
    @Options(useGeneratedKeys=true, keyProperty="riskcatid", keyColumn="RISKCATID")
    void save(TblRiskcategory cat);
}
