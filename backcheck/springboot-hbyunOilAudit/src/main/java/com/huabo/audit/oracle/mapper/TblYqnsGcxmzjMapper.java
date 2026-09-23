package com.huabo.audit.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblYqnsGcxmzj;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author wangys
 * @description 针对表【TBL_YQNS_GCXMZJ(工程项目造价表)】的数据库操作Mapper
 * @createDate 2023-09-07 16:46:40
 * @Entity TblYqnsGcxmzj
 */
public interface TblYqnsGcxmzjMapper extends BaseMapper<TblYqnsGcxmzj> {

    @SelectProvider(method = "selectCountByPageInfo", type = TblYqnsGcxmzjMapperSqlConfig.class)
    Integer selectCountByPageInfo(PageInfo<TblYqnsGcxmzj> pageInfo, TblYqnsGcxmzj vo) throws Exception;

    @SelectProvider(method = "selectListByPageInfo", type = TblYqnsGcxmzjMapperSqlConfig.class)
    @Results({
            @Result(column = "htbh", property = "htbh"),
            @Result(column = "gcmc", property = "gcmc"),
            @Result(column = "jsdw", property = "jsdw"),
            @Result(column = "sgdw", property = "sgdw"),
            @Result(column = "lxr", property = "lxr"),
            @Result(column = "lxdh", property = "lxdh"), 
            @Result(column = "cjr", property = "cjr"),
            @Result(column = "cjsj", property = "cjsj"),
            @Result(column = "esscje", property = "esscje"),
    })
    List<TblYqnsGcxmzj> selectListByPageInfo(PageInfo<TblYqnsGcxmzj> pageInfo, TblYqnsGcxmzj vo) throws Exception;
    
    @InsertProvider(method = "insetEntity", type= TblYqnsGcxmzjMapperSqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "gcxmzjid", keyColumn = "GCXMZJID")
    void insetEntity(TblYqnsGcxmzj entity);

    @Select("SELECT * FROM TBL_YQNS_GCXMZJ WHERE HTBH = #{HTBH} ORDER BY CJSJ DESC")
	List<TblYqnsGcxmzj> selectByHtbh(String htbh);

    @Select("SELECT * FROM TBL_YQNS_GCXMZJ WHERE GCXMZJID IN (SELECT GCXMZJID FROM TBL_YQNS_GCXMZJ_ZJB WHERE GCXMZJZJBID IN (SELECT FORMID FROM TBL_YQNS_JHZGGL_RELA WHERE GLID IN (SELECT ID FROM TBL_YQNS_JHGL_JH_GL WHERE ID IN (SELECT GLJHXMID FROM TBL_YQNS_ENGIN_AUDIT_PROJECT))))  AND (ISSYNC IS NULL OR ISSYNC != 1)")
	List<TblYqnsGcxmzj> selectSyncConstructionProject() throws Exception;
}




