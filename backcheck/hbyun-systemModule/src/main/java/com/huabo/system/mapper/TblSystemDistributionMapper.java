package com.huabo.system.mapper;



import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblSystemDistribution;
import com.huabo.system.mappersql.TblSystemDistributionMapperSqlConifg;
import com.huabo.system.vo.TblSystemDistributionVo;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 系统业务单据下发通知表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
public interface TblSystemDistributionMapper extends Mapper<TblSystemDistribution> {

	@Select("SELECT TSD.*,CTS.REALNAME AS CREATESTAFFNAME FROM TBL_SYSTEM_DISTRIBUTION TSD LEFT JOIN TBL_STAFF CTS ON TSD.CREATESTAFF = CTS.STAFFID WHERE RECIVER = #{staffid} AND TSD.ISREAD = #{isRead} ORDER BY TSD.CREATETIME ASC")
	List<TblSystemDistributionVo> selectDistributionInfoList(@Param("staffid") BigDecimal staffid,@Param("isRead") Integer isRead) throws Exception;

	@SelectProvider(method = "selectDistributionListPageInfo" ,type = TblSystemDistributionMapperSqlConifg.class)
	List<TblSystemDistributionVo> selectDistributionListPageInfo(PageInfo<TblSystemDistributionVo> pageInfo) throws Exception;
	
	@SelectProvider(method = "selectDistributionCountPageInfo" ,type = TblSystemDistributionMapperSqlConifg.class)
	Integer selectDistributionCountPageInfo(PageInfo<TblSystemDistributionVo> pageInfo) throws Exception;
	
	@SelectProvider(method = "getDistributionCount" ,type = TblSystemDistributionMapperSqlConifg.class)
	Integer getDistributionCount(@Param("staffid") BigDecimal staffid) throws Exception;
	
	
	
	@Select("SELECT * FROM TBL_SYSTEM_DISTRIBUTION where DISTRIBUTIONID=#{distributionId} ")
	TblSystemDistribution findbyid(String distributionId);
	
	@Delete("delete FROM TBL_SYSTEM_DISTRIBUTION WHERE FORMID in (${ids}) ")
	void deletebyids(String ids);

	@Update("UPDATE TBL_SYSTEM_DISTRIBUTION SET ISREAD = 1 WHERE DISTRIBUTIONID IN (${idStr})")
	void updateBatchReadStatus(@Param("idStr")String idStr) throws Exception;

	@SelectProvider(method = "selectDistributionAllType" ,type = TblSystemDistributionMapperSqlConifg.class)
	List<TblSystemDistributionVo> selectDistributionAllType(TblSystemDistributionVo distribution) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_DISTRIBUTION WHERE RECIVER = #{staffid} AND ISREAD != 1")
	Integer selectNoConfirmCount(@Param("staffid")BigDecimal staffid) throws Exception;

}
