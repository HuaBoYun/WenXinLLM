package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.Flow;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-04
 */
@Repository
public interface FlowMapper extends BaseMapper<Flow> {
    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY= #{orgid}")
    List<Flow> findList(@Param("orgid") BigDecimal orgid);

    @Select("select * from TBL_FLOW where FLOWID in " +
            "(select FLOWID from TBL_RISK_FLOW where RISKID = #{riskid})" +
            " order by FLOWID desc ")
    List<Flow> findTblFlowByRiskId(@Param("riskid") String riskid);

    @Select("select * from TBL_FLOW where flowid in (SELECT FLOWID FROM TBL_RISK_FLOW where RISKID = #{param})")
    List<Flow> selectFlowByRiskId(String riskid);

    @Select("SELECT flowid,FLOWNAME FROM TBL_FLOW where FLOWBYSYSTEM='1' and FATHERFLOWID=0 and COMPANY = #{orgid}")
    List<Flow> findTblFlowByorgId(BigDecimal orgid);

    @Select("select count(*) from TBL_FLOW where FLOWNUMBER = #{param1} AND COMPANY= #{param2}")
    Integer selectNumberIsSole(String number, String orgid);

    @Select("select MAX(FLOWID) from TBL_FLOW")
    Long selectMaxFlowId();

    @Select("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,"
            + "f.FLOWSTATUS FROM TBL_FLOW f WHERE f.FLOWID IN (" +
            "SELECT max(w.FLOWID) from TBL_FLOW w WHERE	w.FATHERFLOWID = 0 and w.InFlowDB=1 and company=#{orgid} and VERSIONTYPE is NULL	GROUP BY w.flownumber)" +
            " and ${ew.sqlSegment}")
    IPage<Flow> hyFlowPage1(IPage page, QueryWrapper<Flow> queryWrapper, @Param("orgid") String orgid);

    @Select("SELECT f.FLOWID,f.FLOWNUMBER,f.FLOWNAME,f.COMPANY,f.DEPARTINCHARGE,f.CREATETIME,"
            + "f.FLOWSTATUS FROM TBL_FLOW f WHERE f.FLOWID IN (		" +
            "SELECT		max(w.FLOWID) from TBL_FLOW w	WHERE w.FATHERFLOWID = #{fathlowid} and w.InFlowDB=1 and w.company=#{orgid} and VERSIONTYPE is NULL	GROUP BY w.flownumber)" +
            " and ${ew.sqlSegment}")
    IPage<Flow> hyFlowPage2(IPage page, @Param("ew") QueryWrapper<Flow> queryWrapper, @Param("orgid") String orgid, @Param("fathlowid") String fathlowid);

    @Select("SELECT CONMATID FROM TBL_FLOW_MATRIX WHERE FLOWID = #{flowid} AND ROWNUM <= 1 ORDER BY CONMATID DESC")
    String selectTop1ConmatidByFlowId(BigDecimal flowid);

    @Insert("INSERT INTO TBL_FLOW_MATRIX(FLOWID, CONMATID) VALUE(#{param1}, #{param2})")
    Integer insertFlowMatrix(BigDecimal flowid, BigDecimal conmatid);
}
