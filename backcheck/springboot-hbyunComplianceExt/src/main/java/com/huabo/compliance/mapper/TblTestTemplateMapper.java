package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.TblTestTemplate;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;


public interface TblTestTemplateMapper extends BaseMapper<TblTestTemplate> {

    @Delete("DELETE FROM TBL_COM_EXT_TESTTEMPLE  WHERE TEMPLENUMBER = #{templeNumber}")
    void deleteByNumber(@Param("templeNumber") String templeNumber)throws Exception;

    @SelectProvider(type=TblTestTemplateMapperSqlConfig.class,method="selectPageInfo")
    @Results({
    	@Result(column="TESTTEMID",property="testtemid"),
    	@Result(column="TEMPLENUMBER",property="templeNumber"),
    	@Result(column="TEMPLENAME",property="templename"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="CREATEDATETIME",property="createDateTime"),
    	@Result(column="TEMPLESTATUS",property="templeStatus"),
    	@Result(column="TEMPLEDESC",property="templeDesc"),
    	@Result(column="SOURCE",property="source"),
    	@Result(column="TBLCOMANY",property="tblComany"),
    	@Result(column="REALNAME",property="staff.realname"),
    	@Result(column="STAFFID",property="staff.staffid",id=true),
    	@Result(column="ISSUED",property="issued"),
    })
	List<TblTestTemplate> selectPageInfo(PageInfo<TblTestTemplate> pageInfo) throws Exception;

    @SelectProvider(type=TblTestTemplateMapperSqlConfig.class,method="selectPageCount")
	Integer selectPageCount(PageInfo<TblTestTemplate> pageInfo) throws Exception;

    @InsertProvider(type=TblTestTemplateMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="testtemid", keyColumn="TESTTEMID")
	void insertEntity(TblTestTemplate tblTestTemplate) throws Exception;

    @UpdateProvider(type=TblTestTemplateMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTestTemplate tblTestTemplate) throws Exception;

    @Delete("${sql}")
	void executeDelSql(@Param("sql")String sql) throws Exception;

    @Select("SELECT TT.TESTTEMID,TT.TEMPLENUMBER,TT.TEMPLENAME,TT.MEMO,TT.STAFFID,TT.CREATEDATETIME,TT.TEMPLESTATUS,TT.TEMPLEDESC,TT.SOURCE,TT.TBLCOMANY,TS.REALNAME FROM TBL_COM_EXT_TESTTEMPLE  TT LEFT JOIN TBL_STAFF TS ON TT.STAFFID = TS.STAFFID WHERE TT.TESTTEMID = #{tempId}")
    @Results({
    	@Result(column="TESTTEMID",property="testtemid"),
    	@Result(column="TEMPLENUMBER",property="templeNumber"),
    	@Result(column="TEMPLENAME",property="templename"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="CREATEDATETIME",property="createDateTime"),
    	@Result(column="TEMPLESTATUS",property="templeStatus"),
    	@Result(column="TEMPLEDESC",property="templeDesc"),
    	@Result(column="SOURCE",property="source"),
    	@Result(column="TBLCOMANY",property="tblComany"),
    	@Result(column="REALNAME",property="staff.realname"),
    	@Result(column="STAFFID",property="staff.staffid",id=true),
    })
	TblTestTemplate selectEntityById(String tempId) throws Exception;

    @Select("select count(*) from TBL_COM_EXT_TESTTEMPLE where TEMPLENUMBER = #{templeNumber} and tblComany=#{tblComany}")
    Integer selectCountForNumber(@Param("templeNumber")String templeNumber,@Param("tblComany")String tblComany) throws Exception;
    
}
