package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblFlow;
import com.huabo.audit.oracle.entity.TblFormControllog;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblFlowMapper extends tk.mybatis.mapper.common.Mapper<TblFlow> {

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowId}")
	TblFlow findFlowInfoById(@Param("flowId") String flowId) throws Exception;

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flowNumber} AND COMPANY = #{orgId}")
	TblFlow findFlowInfoByFlowNumber(@Param("flowNumber")String flowNumber,@Param("orgId") BigDecimal orgId);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	@Options(useGeneratedKeys=true, keyProperty="flowid", keyColumn="FLOWID")
    TblFlow findByFlowid(String flowid);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	TblFlow findByFlow(String flowid);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	TblFlow findFlowid(BigDecimal flowid);

	@InsertProvider(type=TblFlowMapperSqlConfig.class,method="saveTfl")
    void saveTfl(TblFormControllog tfl);

	@UpdateProvider(type=TblFlowMapperSqlConfig.class,method="updateTfl")
	void updateTfl(TblFormControllog tfl);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	String selectFlowid(BigDecimal flowId);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findById(String flowid);
}