package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.compliance.mysql.entity.TblVersionMySql;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TblVersionMySqlMapper extends BaseMapper<TblVersionMySql> {


    @Select("select * from BATHDATA.TBL_VERSION where FID= #{fid}")
    List<TblVersionMySql> findByfid(String fid);

    @Select("select * from BATHDATA.TBL_VERSION where FID!=100 order by SORT")
    List<TblVersionMySql> findBysql();

}
