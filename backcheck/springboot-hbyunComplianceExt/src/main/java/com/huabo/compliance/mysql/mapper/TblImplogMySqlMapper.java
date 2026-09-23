package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblImplogMySql;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TblImplogMySqlMapper extends BaseMapper<TblImplogMySql> {


//    @Select("select * from TBL_IMP_LOG t where t.imptype = #{type} and t.impcreateusername= #{username} order by t.impcreatetime desc")
//    List<TblImplog> findByTblImplogList(String username, String type);

    @Select("SELECT COUNT(*) FROM TBL_IMP_LOG  WHERE IMPTYPE = #{type} and IMPCREATEUSERNAME= #{username}")
    Integer selectListByCount(String username, String type);


    @SelectProvider(type = TblImplogMapperSqlMySqlConfig.class, method = "findByTblImplogList")
    @Results({
            @Result(column = "IMPID", property = "impid"),
            @Result(column = "IMPDESC", property = "impdesc"),
            @Result(column = "IMPURL", property = "impurl"),
            @Result(column = "IMPFILENAME", property = "impfilename"),
            @Result(column = "IMPCREATETIME", property = "impcreatetime"),
            @Result(column = "IMPCREATEUSERNAME", property = "impcreateusername"),
            @Result(column = "IMPTYPE", property = "imptype"),
            @Result(column = "IMPSTATE", property = "impstate"),
    })
    List<TblImplogMySql> findByTblImplogList(PageInfo<TblImplogMySql> pageInfo, @Param("username") String username, @Param("type") String type);
}
