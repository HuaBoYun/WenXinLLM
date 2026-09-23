package com.huabo.contract.mapper;

import java.math.BigDecimal;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblFlow;
import com.huabo.contract.entity.TblFormControllog;
import com.huabo.contract.mappersql.TblFlowMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
public interface TblFlowMapper extends BaseMapper<TblFlow> {

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowId}")
	TblFlow findFlowInfoById(@Param("flowId") String flowId) throws Exception;

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flowNumber} AND COMPANY = #{orgId}")
	TblFlow findFlowInfoByFlowNumber(@Param("flowNumber")String flowNumber,@Param("orgId") BigDecimal orgId);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findByFlowid(String flowid) throws Exception;

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	TblFlow findByFlow(String flowid) throws Exception;

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	TblFlow findFlowid(BigDecimal flowid) throws Exception;

	@InsertProvider(type=TblFlowMapperSqlConfig.class,method="saveTfl")
    void saveTfl(TblFormControllog tfl) throws Exception;

	@UpdateProvider(type=TblFlowMapperSqlConfig.class,method="updateTfl")
	void updateTfl(TblFormControllog tfl) throws Exception;

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
	String selectFlowid(BigDecimal flowId);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWID = #{flowid}")
    TblFlow findById(String flowid);

	@Select("SELECT * FROM TBL_FLOW WHERE FLOWNUMBER = #{flownumber} AND COMPANY = 116821 AND VERSION = (SELECT MAX(VERSION) FROM TBL_FLOW WHERE FLOWNUMBER = #{flownumber} AND COMPANY = 116821)")
	TblFlow findFlowInfoByNumberOrgId(String flownumber, BigDecimal orgid);
}
