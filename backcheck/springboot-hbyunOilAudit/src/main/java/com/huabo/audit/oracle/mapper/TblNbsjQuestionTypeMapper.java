package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjQuestionType;
import com.huabo.audit.oracle.entity.TblNbsjStatType;

public interface TblNbsjQuestionTypeMapper extends BaseMapper<TblNbsjQuestionType> {
	
	@SelectProvider(method="selectNbsjQuestionTypeListByPageInfo",type=TblNbsjQuestionTypeMapperSqlConfig.class)
	List<TblNbsjQuestionType> selectNbsjQuestionTypeListByPageInfo(PageInfo<TblNbsjQuestionType> pageInfo, BigDecimal orgid);

    @SelectProvider(method="selectNbsjQuestionTypeListCountByPageInfo",type=TblNbsjQuestionTypeMapperSqlConfig.class)
	Integer selectNbsjQuestionTypeListCountByPageInfo(PageInfo<TblNbsjQuestionType> pageInfo, BigDecimal orgid);
    
    
    @Select("SELECT * from TBL_NBSJ_QUESTION_TYPE  WHERE ORGID=#{orgid}")
    List<TblNbsjQuestionType> selectNbsjQuestionTypeListCount(BigDecimal orgid);
      
    
    @Select("SELECT * FROM TBL_NBSJ_QUESTION_TYPE where  typeid = #{typeid}")
    TblNbsjQuestionType selectNbsjType(@Param("typeid")String typeid);
    
    
    @Select("SELECT * FROM TBL_NBSJ_QUESTION_TYPE where ORGID = #{orgid} and audittype=#{auditType} ")
	List<TblNbsjQuestionType> findByOrgidAndType(@Param("orgid")Integer orgid,@Param("auditType")String auditType);
    
    @Select("SELECT * FROM TBL_NBSJ_QUESTION_TYPE where ORGID = #{orgid} and audittype=#{auditType} and typeid !=#{typeid} ")
	List<TblNbsjQuestionType> findByOrgidAndId(@Param("orgid")Integer orgid,@Param("auditType")String auditType,@Param("typeid")Integer typeid);
    
    @Insert("INSERT INTO TBL_NBSJ_QUESTION_TYPE(TYPEID,AUDITTYPE,status,version,orgid) VALUES(HIBERNATE_SEQUENCE.nextval,#{type},#{status},#{version},#{orgid})")
	void insertTblNbsjType(@Param("type")String type,@Param("version")Integer version,@Param("orgid")Integer orgid,@Param("status")Integer status);
    
    @Update("UPDATE TBL_NBSJ_QUESTION_TYPE SET audittype = #{type} ,status = #{status},version = #{version},orgid = #{orgid}  WHERE typeid = #{id} ")
	void updateTblNbsjType(@Param("id")Integer id,@Param("type")String type,@Param("version")Integer version,@Param("orgid")Integer orgid,@Param("status")Integer status) throws Exception;

    @Delete("delete TBL_NBSJ_QUESTION_TYPE  WHERE typeid = #{id} ")
    void delTblNbsjType(@Param("id")String id);
    
    
    //==统计类型维护
    @SelectProvider(method="selectNbsjStatTypeListByPageInfo",type=TblNbsjQuestionTypeMapperSqlConfig.class)
	List<TblNbsjStatType> selectNbsjStatTypeListByPageInfo(PageInfo<TblNbsjStatType> pageInfo, BigDecimal orgid);

    @SelectProvider(method="selectNbsjStatTypeListCountByPageInfo",type=TblNbsjQuestionTypeMapperSqlConfig.class)
	Integer selectNbsjStatTypeListCountByPageInfo(PageInfo<TblNbsjStatType> pageInfo, BigDecimal orgid);
    
    @Select("SELECT * FROM TBL_NBSJ_STAT_TYPE where  typeid = #{typeid}")
    TblNbsjStatType selectNbsjStatType(@Param("typeid")String typeid);
    
    
    @Select("SELECT * FROM TBL_NBSJ_STAT_TYPE where ORGID = #{orgid} and audittype=#{auditType} ")
	List<TblNbsjStatType> findStatByOrgidAndType(@Param("orgid")Integer orgid,@Param("auditType")String auditType);
    
    @Select("SELECT * FROM TBL_NBSJ_STAT_TYPE where ORGID = #{orgid} and audittype=#{auditType} and typeid !=#{typeid} ")
	List<TblNbsjStatType> findStatByOrgidAndId(@Param("orgid")Integer orgid,@Param("auditType")String auditType,@Param("typeid")Integer typeid);
    
    @Insert("INSERT INTO TBL_NBSJ_STAT_TYPE(TYPEID,AUDITTYPE,status,version,orgid) VALUES(HIBERNATE_SEQUENCE.nextval,#{type},#{status},#{version},#{orgid})")
	void insertTblNbsjStatType(@Param("type")String type,@Param("version")Integer version,@Param("orgid")Integer orgid,@Param("status")Integer status);
    
    @Update("UPDATE TBL_NBSJ_STAT_TYPE SET audittype = #{type} ,status = #{status},version = #{version},orgid = #{orgid}  WHERE typeid = #{id} ")
	void updateTblNbsjStatType(@Param("id")Integer id,@Param("type")String type,@Param("version")Integer version,@Param("orgid")Integer orgid,@Param("status")Integer status) throws Exception;

    @Delete("delete TBL_NBSJ_STAT_TYPE  WHERE typeid = #{id} ")
    void delTblNbsjStatType(@Param("id")String id);
    
}
