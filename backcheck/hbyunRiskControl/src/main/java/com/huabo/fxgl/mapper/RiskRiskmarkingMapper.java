package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Repository
public interface RiskRiskmarkingMapper extends BaseMapper<RiskRiskmarking> {
    //使用ASSRISKID查询属于同一个PLAN_RISK的多个MARKING
    @Select("select * from TBL_RISK_RISKMARKING WHERE ASSRISKID=#{param1}")
    @ResultMap("RM_RISK_RISK_MARKING")
    Set<RiskRiskmarking> selectListByAssriskid(BigDecimal assriskid);

    @Select("select DISTINCT i.riskid,i.riskname,i.riskdes,i.risknumber,r.*,s.realname from TBL_RISK_RISKMARKING r left join TBL_RISK_ASSPLAN_RISK tr on r.assriskid=tr.assriskid left join TBL_RISK_ASSPLAN ta on tr.assplanid=ta.assplanid left join tbl_staff s on s.staffid=r.staffid left join tbl_risk  i on i.riskid=tr.riskid where ta.assplanid=#{param1} order by markingid  ")
    IPage<RiskRiskmarking> getMarkingList(BigDecimal planId, IPage page);
    
    @Select("select DISTINCT  r.STAFFID from TBL_RISK_RISKMARKING r left join TBL_RISK_ASSPLAN_RISK tr on r.assriskid=tr.assriskid left join TBL_RISK_ASSPLAN ta on tr.assplanid=ta.assplanid left join tbl_staff s on s.staffid=r.staffid left join tbl_risk  i on i.riskid=tr.riskid where ta.assplanid=#{param1}  ")
   List<String> findList(BigDecimal planId);
    


//    void deleteRiskMarking(RiskRiskmarking riskMarking);
//        baseMapper.deleteById(riskMarking.getMarkingid());
//    }
//select DISTINCT * from TBL_RISK_RISKMARKING r left join TBL_RISK_ASSPLAN_RISK tr on r.assriskid=tr.assriskid left join TBL_RISK_ASSPLAN ta on ta.assplanid=#{param1} where r.asssatus!=2
//    @Select("select DISTINCT * from TBL_RISK_RISKMARKING r where r.assriskid= (select tr.assriskid from TBL_RISK_ASSPLAN_RISK tr,TBL_RISK_ASSPLAN ta where tr.assplanid=ta.assplanid and ta.assplanid=#{param1}) and r.asssatus!=2 ")
   @Select("select DISTINCT r.* from TBL_RISK_RISKMARKING r left join TBL_RISK_ASSPLAN_RISK tr on r.assriskid=tr.assriskid left join TBL_RISK_ASSPLAN ta on tr.assplanid=ta.assplanid where ta.assplanid=#{param1} and r.asssatus!=2")
    List<RiskRiskmarking> checkSubmit(BigDecimal planId);
//	("from RiskMarking r where r.riskAssPlanRisk.assPlanId.assplanid = ? and r.assSatus != 2 ", planId);



    @Select("select * from TBL_RISK_RISKMARKING WHERE ASSRISKID=${param1}")
    @ResultMap("RM_RISK_RISK_MARKING")
    List<RiskRiskmarking> findByRiskId(BigDecimal assriskid);


    @Select("SELECT TS.REALNAME,TRR.ASSSATUS,COUNT(0) PGCOUNT FROM TBL_RISK_RISKMARKING TRR LEFT JOIN TBL_STAFF TS ON TRR.STAFFID = TS.STAFFID WHERE tRR.ASSRISKID IN (SELECT ASSRISKID FROM TBL_RISK_ASSPLAN_RISK WHERE ASSPLANID = #{assplanid}) GROUP BY TS.REALNAME,TRR.ASSSATUS ")
	@Results({
		@Result(column="REALNAME",property="realname"),
		@Result(column="ASSSATUS",property="asssatus"),
		@Result(column="PGCOUNT",property="pgcount"),
	})
    List<RiskRiskmarking> selectPgList(BigDecimal assplanid);

}
