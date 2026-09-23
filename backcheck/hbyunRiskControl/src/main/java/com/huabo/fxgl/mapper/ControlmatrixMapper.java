package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.TblOrganization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Repository
public interface ControlmatrixMapper extends BaseMapper<Controlmatrix> {

    @Select("select * from TBL_CONTROLMATRIX where CONMATID =#{conmatid}")
    List<Controlmatrix> getControlmatrix(String conmatid);

    @Select("select * from TBL_CONTROLMATRIX WHERE CONMATID in (SELECT CONMATID FROM TBL_FLOW_MATRIX WHERE FLOWID = #{flowid})")
    List<Controlmatrix> selectByFlowId(String flowid);

    @Select("select * from TBL_CONTROLMATRIX where CONMATID in (select CONMATID from TBL_RISK_COPING_CMATRIX where RISKCOPINGID = #{copingid})")
    List<Controlmatrix> selectByCopingId(String copingid);
    
  
    @Delete("  DELETE TBL_CONTROLMATRIX where CONMATID in (select CONMATID from TBL_RISK_COPING_CMATRIX where RISKCOPINGID = #{copingid})")
    int deleteControlmatrix(@Param("copingid") BigDecimal copingid);
    

	 @Delete("DELETE TBL_RISK_COPING_CMATRIX WHERE RISKCOPINGID = #{copingid}")
	    int deleTblriskcopingcmatrix(@Param("copingid") BigDecimal copingid);
	 
     @Select("select c.* from TBL_CONTROLMATRIX c  where c.extjson is not null and c.extjson !='' and c.extjson !='[]' ")
	 List<Controlmatrix> getControlmatrixList();

	 List<Controlmatrix> getControlmatrixCount(@Param("orgid")BigDecimal orgid);

     List<TblOrganization> getControlmatrixOrg(@Param("orgid") String orgid);
     
     List<Controlmatrix> getControlmatrixInfo(@Param("orgid")String orgid,@Param("type")String type);
}
