package com.huabo.monitor.mapper;

import com.huabo.monitor.entity.TblTesttemplType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
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
 * @since 2022-09-13
 */
public interface TblTesttemplTypeMapper extends BaseMapper<TblTesttemplType> {


    @Select("${sql}")
    List<TblTesttemplType> getListBySql(@Param("sql") String sql);

    @Select("select * from  TBL_TESTTEMPL_TYPE t where t.parentid=#{id}")
    List<TblTesttemplType> getchild(BigDecimal id);

    @Select(" select count(*) from TBL_TESTTEMPL_TYPE t where t.parentid  =#{id}")
    Long getChildCount(BigDecimal id);

    @Select("SELECT * FROM TBL_TESTTEMPL_TYPE WHERE TESTTEMPLETAID = #{tempId} AND PARENTID IS NULL")
	List<TblTesttemplType> selectRootListByTempId(String tempId);

    @InsertProvider(type=TblTesttemplTypeMapperSqlConfig.class, method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="typeid", keyColumn="TYPEID")
	void insertEntity(TblTesttemplType newType) throws Exception;

    @Select("SELECT * FROM TBL_TESTTEMPL_TYPE WHERE PARENTID = #{typeid}")
	List<TblTesttemplType> selectChildrenListByTempId(BigDecimal typeid) throws Exception;

    @UpdateProvider(type=TblTesttemplTypeMapperSqlConfig.class, method="updateEntity")
	void updateEntity(TblTesttemplType type) throws Exception;

    @Select("SELECT COUNT(0) FROM TBL_TESTTEMPL_TYPE WHERE PARENTID = #{typeid}")
	Integer selectChidrenCount(BigDecimal typeid) throws Exception;

    @Select("${sql}")
	void executeDelSql(@Param("sql") String sql) throws Exception;

    @Select("SELECT * FROM TBL_TESTTEMPL_TYPE WHERE TYPEID = #{typeid}")
	TblTesttemplType selectEntityById(BigDecimal typeid) throws Exception;

    @SelectProvider(type=TblTesttemplTypeMapperSqlConfig.class, method="selectTreeListInfo")
	List<TblTesttemplType> selectTreeListInfo(BigDecimal parentId, BigDecimal testtempletaid) throws Exception;

    @Select("${sql}")
    List<Integer> getListIdBySql(@Param("sql") String sql);
}
