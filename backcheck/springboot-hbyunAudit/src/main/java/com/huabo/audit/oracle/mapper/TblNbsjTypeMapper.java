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
import com.huabo.audit.oracle.entity.TblNbsjType;

public interface TblNbsjTypeMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjType> {

	@Select("SELECT * FROM TBL_NBSJ_TYPE where STATUS =2 and ORGID = #{orgid}")
	List<TblNbsjType> selectNbsjTypeListForMerge(BigDecimal orgid);
	
	
	@Select("SELECT * FROM TBL_NBSJ_TYPE where  typeid = #{typeid}")
	 TblNbsjType selectNbsjType(@Param("typeid")String typeid);
	
	@Select("SELECT * FROM TBL_NBSJ_TYPE where  AUDITTYPE = #{audittype} AND ORGID=#{orgid} ")
	TblNbsjType selectNbsjTypeByName(String audittype,String orgid);
	
	@Select("SELECT * FROM TBL_NBSJ_TYPE where ORGID = #{orgid} and audittype=#{auditType} ")
	List<TblNbsjType> findByOrgidAndType(@Param("orgid")BigDecimal orgid,@Param("auditType")String auditType);
	
	@Select("SELECT * FROM TBL_NBSJ_TYPE where ORGID = #{orgid} and audittype=#{auditType} and typeid !=#{typeid} ")
	List<TblNbsjType> findByOrgidAndId(@Param("orgid")BigDecimal orgid,@Param("auditType")String auditType,@Param("typeid")BigDecimal typeid);

	@Insert("INSERT INTO TBL_NBSJ_TYPE(TYPEID,AUDITTYPE,status,version,orgid,auditCode) VALUES(HIBERNATE_SEQUENCE.nextval,#{type},#{status},#{version},#{orgid},#{auditCode})")
	void insertTblNbsjType(@Param("type")String type,@Param("version")String version,@Param("orgid")String orgid,@Param("status")Integer status,@Param("auditCode")Integer auditCode);
 
	@Update("UPDATE TBL_NBSJ_TYPE SET audittype = #{type} ,status = #{status},version = #{version},orgid = #{orgid},auditCode = #{auditCode}  WHERE typeid = #{id} ")
	void updateTblNbsjType(@Param("id")BigDecimal id,@Param("type")String type,@Param("version")String version,@Param("orgid")String orgid,@Param("status")Integer status,@Param("auditCode")Integer auditCode) throws Exception;
 
    @Delete("delete TBL_NBSJ_TYPE  WHERE typeid = #{id} ")
    void delTblNbsjType(@Param("id")String id);

    @SelectProvider(method="selectNbsjTempleteListByPageInfo",type=TblNbsjTypeMapperSqlConfig.class)
	List<TblNbsjType> selectNbsjTempleteListByPageInfo(PageInfo<TblNbsjType> pageInfo, BigDecimal orgid,String auditType);

    @SelectProvider(method="selectNbsjTempleteListCountByPageInfo",type=TblNbsjTypeMapperSqlConfig.class)
	Integer selectNbsjTempleteListCountByPageInfo(PageInfo<TblNbsjType> pageInfo, BigDecimal orgid,String auditType);

    @Select("SELECT * from TBL_NBSJ_TYPE  WHERE ORGID = #{orgid} AND STATUS = 2")
	List<TblNbsjType> selectNbsjTypeAllList(BigDecimal orgid) throws Exception;
}
