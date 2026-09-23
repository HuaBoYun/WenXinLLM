package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.audit.oracle.entity.TblAuditOption;



public interface TblAuditOptionMapper{
	@SelectProvider(method="selectListByCyId",type=TblAuditOptionMapperSqlConfig.class)
	@Results({
		@Result(column="REALNAME",property="staffidName"),
		@Result(column="OPT_DESC",property="optDesc"),
		@Result(column="OPT_STATE",property="optState"),
		@Result(column="OPT_STAFFID",property="optStaffid"),
		@Result(column="CREATE_DATE",property="createDate"),
		@Result(column="OPT_ID",property="optId"),
	})
	List<TblAuditOption> selectListByCyId(String id, Integer cyId) throws Exception;

	@InsertProvider(method="saveEnity",type=TblAuditOptionMapperSqlConfig.class)
	void saveEnity(TblAuditOption opt) throws Exception;
	
	
	
	
	
	
	//==
	@SelectProvider(method="getOptionByRelationId",type=TblAuditOptionMapperSqlConfig.class)
	@Results({
		@Result(column="REALNAME",property="staffidName"),
		@Result(column="OPT_DESC",property="optDesc"),
		@Result(column="OPT_STATE",property="optState"),
		@Result(column="OPT_STAFFID",property="optStaffid"),
		@Result(column="CREATE_DATE",property="createDate"),
		@Result(column="OPT_ID",property="optId"),
	})
	List<TblAuditOption> getOptionByRelationId(String id) throws Exception;

}
