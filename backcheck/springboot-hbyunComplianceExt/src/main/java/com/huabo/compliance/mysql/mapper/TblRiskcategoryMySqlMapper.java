package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblRiskcategoryMySql;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface TblRiskcategoryMySqlMapper extends BaseMapper<TblRiskcategoryMySql> {


    @SelectProvider(type = TblRiskcategoryMapperSqlMySqlConifg.class, method = "findBysql")
    List<TblRiskcategoryMySql> findBysql(String orgid, String type);

    @InsertProvider(type = TblRiskcategoryMapperSqlMySqlConifg.class, method = "save")
    @Options(useGeneratedKeys = true, keyProperty = "riskcatid", keyColumn = "RISKCATID")
    void save(TblRiskcategoryMySql cat);
}
