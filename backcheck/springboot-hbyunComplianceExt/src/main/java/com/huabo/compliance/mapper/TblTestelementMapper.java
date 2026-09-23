package com.huabo.compliance.mapper;

import com.huabo.compliance.entity.TblTestelement;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-08
 */
public interface TblTestelementMapper extends BaseMapper<TblTestelement> {


    @Select("${sql}")
    List<TblTestelement> querySql(@Param("sql") String sql);
    @Select("SELECT DISTINCT ment.* from TBL_COM_EXT_TESTELEMENT ment left join TBL_COM_EXT_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE pa.testplanid=#{panid}")
    List<TblTestelement> queryByPanId(BigDecimal panid);
    
    @Select("SELECT * FROM TBL_COM_EXT_TESTELEMENT WHERE TYPEID = #{typeid}")
	List<TblTestelement> selectEleListByTypeId(BigDecimal typeid) throws Exception;
    
    @InsertProvider(method="insertEntity", type=TblTestelementMapperSqlConfig.class)
	void insertEntity(TblTestelement newEle) throws Exception;
    
    @UpdateProvider(method="updateEntity", type=TblTestelementMapperSqlConfig.class)
	void updateEntity(TblTestelement newEle) throws Exception;
    
    @Delete("DELETE FROM TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID = #{elementid}")
	void removeEntity(BigDecimal elementid);
    
    @SelectProvider(method="selectPageList", type=TblTestelementMapperSqlConfig.class)
	List<TblTestelement> selectPageList(PageInfo<TblTestelement> pageInfo) throws Exception;
    
    @SelectProvider(method="selectPageCount", type=TblTestelementMapperSqlConfig.class)
	Integer selectPageCount(PageInfo<TblTestelement> pageInfo) throws Exception;
    
    @Select("SELECT * FROM TBL_COM_EXT_TESTELEMENT WHERE ELEMENTID = #{elementid}")
	TblTestelement selectEntityById(BigDecimal elementid);
    
    
    @Select("SELECT DISTINCT ment.* from TBL_COM_EXT_TESTELEMENT ment left join TBL_COM_EXT_TESTPLAN pa on ment.templId=pa.TESTTEMID WHERE pa.testplanid=#{testplanid}")
   	List<TblTestelement> findByPlanidAll(@Param("testplanid")String testplanid) throws Exception;
       
}
