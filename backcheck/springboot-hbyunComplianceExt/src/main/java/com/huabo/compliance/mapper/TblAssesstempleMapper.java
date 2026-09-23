package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import com.huabo.compliance.entity.TblAssesstemple;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesstempleMapper extends BaseMapper<TblAssesstemple> {

//    @SelectProvider(type = TblAssesstempleMapperSqlConfig.class, method = "findByPageInfo")
//    List<TblAssesstemple> findByPageInfo(PageInfo<TblAssesstemple> pageInfo);
//
//    @SelectProvider(type = TblAssesstempleMapperSqlConfig.class, method = "findByCount")
//    Integer findByCount(PageInfo<TblAssesstemple> pageInfo);

    @Select("select t.*,(select wm_concat(orgname) from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorgText,(select wm_concat(orgid) from tbl_organization where orgid in (select orgid from Tbl_Temple_Organization where ASSTEMID=t.asstemid)) as reorg from TBL_ASSESSTEMPLE t where t.ASSTEMID = #{tmplId}")
    TblAssesstemple findById(@Param("tmplId") BigDecimal tmplId);

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
