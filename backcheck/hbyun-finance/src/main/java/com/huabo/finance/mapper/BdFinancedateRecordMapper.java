package com.huabo.finance.mapper;

import com.huabo.finance.entity.BdFinancedateRecord;
import com.huabo.finance.mappersql.BdFinancedateRecordMapperSqlConfig;
import com.huabo.finance.vo.BdFinancedateRecordVo;
import com.huabo.finance.vr.BdFinanceplanVr;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 财务数据采集记录表 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-17
 */
public interface BdFinancedateRecordMapper extends BaseMapper<BdFinancedateRecord> {

	@SelectProvider(type = BdFinancedateRecordMapperSqlConfig.class , method = "selectGoonInfoPageInfo")
	IPage<BdFinanceplanVr> selectGoonInfoPageInfo(IPage<BdFinanceplanVr> page, BdFinancedateRecordVo vo) throws Exception;

	@Select("SELECT COUNT(0) FROM BD_FINANCEDATE_RECORD WHERE ISCOMPLETED != 2 AND PLANID = #{planId} AND PRECORDID = #{pid}")
	Integer selectNoDealCountByPlanId(@Param("planId")String planId,@Param("pid") String pid) throws Exception;

	@UpdateProvider(type = BdFinancedateRecordMapperSqlConfig.class , method = "updatePlanRecordStatus")
	void updatePlanRecordStatus(String planId, String pid) throws Exception;

	@Select("SELECT BFR.* FROM BD_FINANCEDATE_RECORD BFR INNER JOIN (SELECT MAX(CREATETIME) AS LASTTIME FROM BD_FINANCEDATE_RECORD WHERE PLANID = #{planId} AND PRECORDID IS NULL AND SQLID IS NULL " + 
			") T1 ON BFR.CREATETIME = T1.LASTTIME WHERE BFR.PLANID = #{planId} AND BFR.PRECORDID IS NULL AND BFR.SQLID IS NULL")
	BdFinancedateRecord selectPlanLastRecordEntity(@Param("planId")String planId) throws Exception;
	
	
	@Select("SELECT * FROM BD_FINANCEDATE_RECORD WHERE ISCOMPLETED != 2 AND PLANID = #{planId} AND PRECORDID = #{pid}")
	List<BdFinancedateRecord> selectNoDealListByPlanId(@Param("planId")String planId,@Param("pid") String pid) throws Exception;

}
