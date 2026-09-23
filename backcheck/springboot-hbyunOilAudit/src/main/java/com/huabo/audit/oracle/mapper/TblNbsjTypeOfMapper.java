package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblNbsjType;
import com.huabo.audit.oracle.entity.TblNbsjTypeOf;

public interface TblNbsjTypeOfMapper extends BaseMapper<TblNbsjTypeOf> {
	
	
	public List<TblNbsjTypeOf> findAll(@Param("typeid")Integer typeid,@Param("auditType")String auditType);
 
    @Select("SELECT COUNT(*) FROM TBL_NBSJ_PROJECT WHERE AUDITORGID = #{orgid} AND AUDITTYPE IN (SELECT AUDITTYPE FROM TBL_NBSJ_TYPEOF WHERE TYPEID = #{typeId} UNION SELECT AUDITTYPE FROM TBL_NBSJ_TYPEOF WHERE PARENTID = #{typeId})")
    Integer selectCountByType(@Param("orgid")BigDecimal orgid, @Param("typeId")String typeId);
    
    @Delete("DELETE FROM TBL_NBSJ_TYPEOF WHERE PARENTID = #{typeId}")
    void deleteTypeByParentId(@Param("typeId")String typeId);
    
    @Delete("DELETE FROM TBL_NBSJ_TYPEOF WHERE TYPEID = #{typeId}")
    void deleteTypeByTypeId(@Param("typeId")String typeId);
 
	@Select("SELECT * FROM TBL_NBSJ_TYPEOF where ORGID = #{orgid} and audittype=#{auditType} order by typeid")
	List<TblNbsjTypeOf> findByOrgidAndType(@Param("orgid")BigDecimal orgid,@Param("auditType")String auditType);
	
	@Select("SELECT * FROM TBL_NBSJ_TYPEOF where ORGID = #{orgid} and audittype=#{auditType} and typeid !=#{typeid} ")
	List<TblNbsjTypeOf> findByOrgidAndId(@Param("orgid")BigDecimal orgid,@Param("auditType")String auditType,@Param("typeid")BigDecimal typeid);

	 @Select("SELECT COUNT(*) FROM TBL_NBSJ_PROJECT WHERE AUDITORGID = #{orgid} AND AUDITTYPE = (SELECT AUDITTYPE FROM TBL_NBSJ_TYPEOF WHERE TYPEID = #{typeId})")
	    Integer selectCountByNbsjType( @Param("orgid")BigDecimal orgid, @Param("typeId")String typeId);

	 @Select("SELECT COUNT(*) FROM TBL_NBSJ_TYPEOF WHERE audittype = #{auditType} AND ORGID = #{orgid} AND TYPEID != #{typeId}")
	    Integer selectRepeatCount(@Param("auditType")String auditType, @Param("orgid")BigDecimal orgid, @Param("typeId")String typeId);

		@Select("SELECT * FROM TBL_NBSJ_TYPEOF where ORGID = #{orgid}  and auditcode=1  order by typeid")
		List<TblNbsjTypeOf> getAllParent(@Param("orgid")BigDecimal orgid);
		
		@Select("SELECT * FROM TBL_NBSJ_TYPEOF where ORGID = #{orgid}  and typeid= #{typeid} order by typeid")
		List<TblNbsjTypeOf> getParentById(@Param("orgid")BigDecimal orgid,@Param("typeid")String typeid);
		
		@Select("SELECT * FROM TBL_NBSJ_TYPEOF where ORGID = #{orgid}   order by typeid")
		List<TblNbsjTypeOf> getAllByOrgid(@Param("orgid")BigDecimal orgid);
		
}
