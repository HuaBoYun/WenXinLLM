package com.huabo.monitor.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.monitor.entity.TblAssesscategory;
import com.huabo.monitor.entity.TblTestTemplate;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesscategoryMapper extends BaseMapper<TblAssesscategory> {



    @Select("${sql}")
    List<TblAssesscategory> listBySql(String sql);
    
    TblAssesscategory get(Class<TblAssesscategory> tblAssesscategoryClass, BigDecimal id);
    
    @Select("select * FROM TBL_ASSESSCATEGORY F WHERE FATHERASSCATID IS NULL AND F.ASSTEMID = #{tmplId}")
    List<TblAssesscategory> getTreeRoot(@Param("tmplId") BigDecimal tmplId);


    @Select("select * FROM TBL_ASSESSCATEGORY F WHERE FATHERASSCATID IS NULL AND F.ASSCATID = #{pId}")
    List<TblAssesscategory> getTreeByNodeId(@Param("pId") BigDecimal pId);

    @Select("select * FROM TBL_ASSESSCATEGORY F WHERE F.ASSCATID = #{tmplId}")
    List<TblAssesscategory> findAllAssesscategory(@Param("tmplId") String tmplId);

    @Select("select * FROM TBL_ASSESSCATEGORY F WHERE F.ASSTEMID = #{tmplId}")
    List<TblAssesscategory> findByTempleId(@Param("tmplId") BigDecimal tmplId);
    
    @Delete("delete from TBL_ASSESSCATEGORY where ASSTEMID = #{tmplId}")
    void deleteByTempleId(@Param("tmplId") BigDecimal tmplId);
    
    List<TblAssesscategory> getParentList(BigDecimal id);
    
    List<Object[]> getHengXiang(BigDecimal id);
    
    @InsertProvider(type=TblAssesscategoryMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="asscatid", keyColumn="ASSCATID")
	void insertEntity(TblAssesscategory tblAssesscategory) throws Exception;

    @UpdateProvider(type=TblAssesscategoryMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblAssesscategory tblAssesscategory) throws Exception;
    
    @Delete("delete from TBL_ASSESSCATEGORY where ASSCATID = #{asscatid}")
    void deleteEntity(@Param("asscatid") BigDecimal asscatid);
    
    
    @Select("select * FROM TBL_ASSESSCATEGORY F WHERE F.ASSCATID = #{asscatid} order by ASSCATID FETCH FIRST 1 ROW ONLY")
    TblAssesscategory  get(@Param("asscatid") BigDecimal asscatid);

    
}
