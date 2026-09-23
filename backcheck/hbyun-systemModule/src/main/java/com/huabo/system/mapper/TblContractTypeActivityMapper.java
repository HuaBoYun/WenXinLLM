package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblContractTypeActivity;

public interface TblContractTypeActivityMapper extends BaseMapper<TblContractTypeActivity> {

	@Select("SELECT ACTIVITYID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE TYPEID = #{typeId} AND YMWORKFROM = #{workId} AND ORGID = #{orgid} ")
	String selectTableYmFlowCount(@Param("typeId")BigDecimal typeId,@Param("workId") String workId,@Param("orgid") BigDecimal orgid) throws Exception;

	@Select("SELECT MAX(VERSION) FROM TBL_CONTRACTTYPE_ACTIVITY WHERE TYPEID = #{typeId} AND ORGID = #{orgid}")
	Integer selectMaxVersion(@Param("typeId") BigDecimal typeId,@Param("orgid") BigDecimal orgid) throws Exception;

	@Update("UPDATE TBL_CONTRACTTYPE_ACTIVITY SET QYSTATS = 0 WHERE ORGID = #{orgId} AND TYPEID = #{typeId} AND TABLEID = #{tableId}")
	void removeYmWorkFormInfo(TblContractTypeActivity act) throws Exception;

	@Select("SELECT TYPEID FROM TBL_CONTRACT_TYPEOF WHERE TYPENAME = #{typeName}")
	BigDecimal selectTypeIdByTypeName(String typeName) throws Exception;

	@Select("SELECT YMWORKFROM FROM TBL_CONTRACTTYPE_ACTIVITY WHERE TABLEID = #{tableId} AND TYPEID = #{typeId} AND ORGID = #{orgid} AND QYSTATS = 1")
	String selectYmFlowIdByTypeId(@Param("tableId") String tableId,@Param("typeId") BigDecimal typeId,@Param("orgid") BigDecimal orgid) throws Exception;

	@Select("SELECT PARENTID FROM TBL_CONTRACT_TYPEOF WHERE TYPEID = #{typeId}")
	BigDecimal selectFatherTypeIdByTypeId(BigDecimal typeId) throws Exception;

	@Select("SELECT * FROM TBL_CONTRACTTYPE_ACTIVITY WHERE TYPEID = #{typeId} AND ORGID = #{orgid} AND TABLEID = #{tableId} AND FLOWTEMPLATEID = #{id}")
	TblContractTypeActivity selectContractFlowInfo(@Param("typeId")String typeId,@Param("id") String id,@Param("orgid") BigDecimal orgid,@Param("tableId") String tableId) throws Exception;

	@Select("SELECT ACTIVITYID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE TYPEID = #{ctypeId} AND FLOWTEMPLATEID = #{ctypeId} AND ORGID = #{orgid} AND TABLEID = #{tableid} ")
	String selectTableYmFlowFiveCount(BigDecimal ctypeId, String id, BigDecimal orgid, BigDecimal tableid) throws Exception;


}
