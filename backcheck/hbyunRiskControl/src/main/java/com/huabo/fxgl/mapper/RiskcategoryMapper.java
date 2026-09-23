package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huabo.fxgl.entity.Risk;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Riskcategory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Repository
public interface RiskcategoryMapper extends BaseMapper<Riskcategory> {
    @Select("select * from tbl_riskcategory t where RISKCATNAME in('企业风险','业务风险','专项风险') and ${ew.sqlSegment} ")
    List<Riskcategory> findQYFXByOrgid(@Param("ew") QueryWrapper queryWrapper);

//    void findRiskcatidByChildNode(String riskcatid);
    @Select("select RISKCATID from TBL_RISKCATEGORY where 1=1 start with RISKCATID= #{param1} connect by prior RISKCATID = FATHERRISKCATID")
    List<BigDecimal> selectChildCatIds(String parentId);

    @Select( "select * from tbl_risk where risknumber = " +
            "(select risknumber from tbl_risk where riskid = #{riskid}) " +
            "and riskid !=#{riskid} order by VERSION ")
     List<Risk> findRiskByHistoricalVersion(String riskid);

    @Select("select count(*) from TBL_RISKCATEGORY where RISKCATNUMBER = #{param1} and unit = #{param2}")
    Integer selectRiskOrganBynumber(String number,String org);
    @Select("select count(*) from TBL_RISKCATEGORY where RISKCATNAME = #{param1} and unit = #{param2}")
    Integer selectRiskOrganByName(String name,String org, QueryWrapper queryWrapper);


    @Select("select * from tbl_RiskCategory a start with a.Riskcatid  = #{rootId} connect by prior a.Riskcatid = a.fatherriskcatid")
    List<Riskcategory> findRiskCateByRoot(BigDecimal rootId);

    /**
     * @author wanghongtuo
     * @Date 2022/8/10
     * @Des:根据评估计划查询风险的所属风险分类
     */
    @Select("select DISTINCT r.* from TBL_RISKCATEGORY r where 1=1 start with r.RISKCATID in " +
            "(select RISKCATID from TBL_RISK where RISKID in (select RISKID from TBL_RISK_ASSPLAN_RISK where ASSPLANID = #{asspanid})) " +
            "CONNECT by PRIOR r.FATHERRISKCATID = r.RISKCATID order by riskcatid")
    List<Riskcategory> findRiskCateParentByAssPanid(String asspanid);


    @Select(" select r.riskid,r.risknumber,r.riskname,o.orgname as UNIT,r.riskcreatedt,r.version,r.riskdes,trc.riskcopingid,trc.status from tbl_risk r " +
            " left join tbl_organization o on r.unit = to_char(o.orgid) "
            + " left join TBL_RISK_COPING trc on trc.riskid = r.riskid  "
            + "where r.riskid  " +
            " in (select max(riskid) as riskcreatedt from tbl_risk where riskcatid in (${riskCatId}) group by risknumber) " +
            " AND ${ew.sqlSegment}")
    IPage<Risk> selectRiskAndOrgData(IPage page, String riskCatId, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    @Select(" select r.riskid,r.risknumber,r.riskname,o.orgname as UNIT,r.riskcreatedt,r.version,r.riskdes,trc.riskcopingid,trc.status from tbl_risk r " +
            " left join tbl_organization o on r.unit = to_char(o.orgid) "
            + " left join TBL_RISK_COPING trc on trc.riskid = r.riskid  "
            + "where r.riskid  " +
            " in (select max(riskid) as riskcreatedt from tbl_risk where riskcatid in (${riskCatId}) group by risknumber) " +
            " AND ${ew.sqlSegment}")
    List<Risk> selectRiskAndOrgData(String riskCatId, @Param(Constants.WRAPPER) QueryWrapper wrapper);
 
    List<Risk> selectRisk(@Param("queryParam")Risk queryParam,@Param("authorityType")Integer authorityType,@Param("idList")List<BigDecimal> idList);

	@Select("select riskcatid from TBL_RISKCATEGORY  where unit=#{unit} and riskcatname=#{name}")
	BigDecimal getRiskCatid(@Param("unit") BigDecimal unit, @Param("name") String name);
	
	@Select("select * from tbl_RiskCategory where fatherriskcatid=#{riskcatid} ")
    List<Riskcategory> findSubRiskCateById(BigDecimal riskcatid);
    
}



