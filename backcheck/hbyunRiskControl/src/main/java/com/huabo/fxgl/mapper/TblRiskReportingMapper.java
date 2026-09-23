package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;


@Mapper
public interface TblRiskReportingMapper extends BaseMapper<TblRiskReportingEntity> {

	List<TblRiskReportingEntity> selectAllList(@Param("queryParam")TblRiskReportingEntity queryParam);
	
	List<TblRiskReportingEntity> exportRiskReport(@Param("entname")String entname,@Param("ids")String[] ids);

	
	
    @Select("INSERT INTO TBL_RISK_REPORTING_ATT(PERORTINGID,ATTID) VALUES (#{id},#{aid})")
    void insertAttInfoReporting(@Param("id") BigDecimal id, @Param("aid")String aid);

    @Select(" SELECT ATTID FROM TBL_RISK_REPORTING_ATT WHERE PERORTINGID = #{id}")
    List<BigDecimal> findAttIdListByReporting(@Param("id")String id);

    @Delete("DELETE FROM TBL_RISK_REPORTING_ATT WHERE ATTID = #{attid}")
    void deleteFileInfoByAttId(@Param("id")BigDecimal attid);
}
