package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;

import com.huabo.monitor.entity.TblAssesstemple;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface TblAssesstempleMapper extends BaseMapper<TblAssesstemple> {

	
	 List<TblAssesstemple> selectPageList(@Param("queryParam")TblAssesstemple queryParam,@Param("sql")String sql);
	
	
//    @SelectProvider(type = TblAssesstempleMapperSqlConfig.class, method = "findByPageInfo")
//    List<TblAssesstemple> findByPageInfo(PageInfo<TblAssesstemple> pageInfo);
//
//    @SelectProvider(type = TblAssesstempleMapperSqlConfig.class, method = "findByCount")
//    Integer findByCount(PageInfo<TblAssesstemple> pageInfo);

    @Select("select t.*,(select listagg(orgname,',') WITHIN GROUP(ORDER BY ORGNAME)  from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorgText,(select listagg(orgid,',') WITHIN GROUP(ORDER BY ORGID) from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorg from TBL_ASSESSTEMPLE t where t.ASSTEMID = #{tmplId}")
    TblAssesstemple findById(@Param("tmplId") BigDecimal tmplId);
    
    @Select("select orgid  from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=#{tmplId})")
    List<String> getOrgId(@Param("tmplId") BigDecimal tmplId);
 
    
    @Select("select orgname  from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=#{tmplId})")
    List<String> getOrgname(@Param("tmplId") BigDecimal tmplId);
 
    @Select("select count(0) from TBL_ASSESS WHERE asstemid=#{tmplId}")
    Integer getNumber(@Param("tmplId") BigDecimal tmplId);
 
    

    @Delete("delete from TBL_ASSESSTEMPLE where  ASSTEMID = #{tmplId}")
    void deleteId(@Param("tmplId")BigDecimal asstemid);

    @Delete("delete from TBL_TEMPLE_ORGANIZATION where  ASSTEMID = #{tmplId}")
    void deleteTempleOrg(@Param("tmplId")BigDecimal asstemid);

    @Select("select * from TBL_ASSESSTEMPLE t where t.TEMPLENUMBER = #{templeNumber} AND t.ORGID =#{orgid}")
    List<TblAssesstemple> getTmplByNumber(@Param("templeNumber")String templeNumber,@Param("orgid") BigDecimal orgid);

    void updateByPrimaryKey(TblAssesstemple tblAssesstemple);
    
    @Select("${sql}")
    <p extends IPage<TblAssesstemple>> p getSqlPage(p page, @Param("sql") String sql);
    
    @Delete("delete from Tbl_Temple_Organization where  ASSTEMID = #{tmplId}")
    void deleteTempOrg(@Param("tmplId")BigDecimal tmplId);

    
    @Insert("INSERT INTO Tbl_Temple_Organization(ORGID,ASSTEMID) VALUES(#{orgid},#{tempid} )")
    void insertTempOrg(@Param("orgid")BigDecimal orgid,@Param("tempid")BigDecimal tempid);
    
    
    @InsertProvider(type=TblAssesstempleMapperSqlConfig.class,method="insertTemples")
    @Options(useGeneratedKeys=true, keyProperty="asstemid", keyColumn="ASSTEMID")
	void insertTemples(TblAssesstemple tblAssesstemple) throws Exception;

    @UpdateProvider(type=TblAssesstempleMapperSqlConfig.class,method="updateTemples")
	void updateTemples(TblAssesstemple tblAssesstemple) throws Exception;
}
