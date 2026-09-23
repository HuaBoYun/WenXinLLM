package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

import com.huabo.audit.oracle.entity.TblYqnsSjssWdrwSjnr;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangxilu
 * @description 针对表【TBL_YQNS_SJSS_WDRW_SJNR(审减内容表)】的数据库操作Mapper
 * @createDate 2023-10-8 16:46:40
 * @Entity TblYqnsSjssWdrwSjnr
 */
public interface TblYqnsSjssWdrwSjnrMapper extends BaseMapper<TblYqnsSjssWdrwSjnr> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsSjssWdrwSjnrMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsSjssWdrwSjnr> pageInfo, TblYqnsSjssWdrwSjnr vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsSjssWdrwSjnrMapperSqlConfig.class)
    @Results({
            @Result(column = "document", property = "document"),
            @Result(column = "title", property = "title"),
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "gxr", property = "gxr"),
    })
    List<TblYqnsSjssWdrwSjnr> selectListByPageInfo(PageInfo<TblYqnsSjssWdrwSjnr> pageInfo, TblYqnsSjssWdrwSjnr vo) throws Exception;

    @Delete("DELETE FROM TBL_YQNS_SJZG_ZGBG_ATTACH WHERE ZGBGID = #{id}")
    void deleteAttByPk(String id);

}




