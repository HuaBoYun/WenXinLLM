package com.huabo.system.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblContractTypeActivity;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblAuthorizationRecordMapper extends BaseMapper<TblAuthorizationRecord> {

	@Select("SELECT * FROM TBL_AUTHORIZATION_RECORD TAR INNER JOIN (SELECT MAX(CREATIONTIME) AS CREATIONTIME FROM TBL_AUTHORIZATION_RECORD WHERE TARGETID = #{targetId} AND STATUS IN (1,2,3)) T1 ON TAR.CREATIONTIME = T1.CREATIONTIME WHERE TAR.TARGETID = #{targetId} AND TAR.STATUS IN (1,2,3)")
	TblAuthorizationRecord selectSpzRecordInfoByTargetId(@Param("targetId")String targetId) throws Exception;

	
	@Select("SELECT COUNT(0) FROM TBL_AUTHORIZATION_RECORD TAR INNER JOIN (SELECT MAX(CREATIONTIME) AS CREATIONTIME FROM TBL_AUTHORIZATION_RECORD WHERE TARGETID = #{targetId} AND STATUS IN (1,2,3)) T1 ON TAR.CREATIONTIME = T1.CREATIONTIME WHERE TAR.TARGETID = #{targetId} AND TAR.STATUS IN (1,2,3)")
	Integer findSpzRecordCountByTargetId(String targetId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_AUTHORIZATION_RECORD TAR INNER JOIN (SELECT MAX(CREATIONTIME) AS CREATIONTIME FROM TBL_AUTHORIZATION_RECORD"
			+ " WHERE TARGETID IN ( SELECT ACTIVITYID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE ORGID = #{orgId} AND TYPEID = #{typeId} AND TABLEID = #{tableId}) AND STATUS IN (1,2,3)) T1 ON TAR.CREATIONTIME = T1.CREATIONTIME"
			+ " WHERE TAR.TARGETID IN ( SELECT ACTIVITYID FROM TBL_CONTRACTTYPE_ACTIVITY WHERE ORGID = #{orgId} AND TYPEID = #{typeId} AND TABLEID = #{tableId}) AND TAR.STATUS IN (1,2,3)")
	Integer findContractFLowSpzRecordCountByTargetId(@Param("targetId")String targetId,@Param("orgId") BigDecimal orgId,@Param("tableId") BigDecimal tableId,
			@Param("typeId")BigDecimal typeId) throws Exception;


}
