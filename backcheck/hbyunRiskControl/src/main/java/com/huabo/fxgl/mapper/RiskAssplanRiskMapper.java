package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.fxgl.entity.RiskAssplan;
import com.huabo.fxgl.entity.RiskAssplanRisk;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.RiskRiskmarking;
import com.huabo.fxgl.entity.TblOrganization;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-11
 */
@Repository
public interface RiskAssplanRiskMapper extends BaseMapper<RiskAssplanRisk> {
	
	
	
	
	Integer getLevelCountCompany(@Param("company")BigDecimal company,@Param("level")int level);

	Integer getLevelCount(@Param("orgid")BigDecimal orgid,@Param("level")int level);
	
	List<Map<String, Object>>  getRiskCatnameAnalysis(@Param("company")String company);
	
	List<Map<String, Object>>  getRiskAnalysis(@Param("company")String company);
	
	List<Map<String, Object>>  getRiskAreasAnalysis(@Param("company")String company);
	
	List<TblOrganization> getRiskDepartment(@Param("company")String company);
	List<TblOrganization> getRiskCompany();
	List<TblOrganization> getRiskCompanyList();
	
	 @Select("select * from (select r.unit,o.orgname,count(1) as ZS from tbl_risk r left join tbl_organization o on o.orgid=r.unit where r.unit is not null group by r.unit,orgname ) dual ")
	 List<Map<String, Object>> selectRiskGroupUnit();

	 @Select("select count(1) from tbl_risk r where r.riskid not in (select a.riskid from TBL_RISK_ASSPLAN_RISK a  left join tbl_risk s on s.riskid=a.riskid  where assdate is not null and s.unit=r.unit  group by  a.riskid) and r.unit=#{orgid}")
	 Integer getWpgSize(@Param("orgid")String orgid);

    /**
     * 查找风险类型
     *
     * @param typeId 风险类型ID
     * @param planId 风险计划ID
     * @return
     * @author wanghongtuo
     * @Date 2022/8/10
     */
    @Select("select R.*" +
            " from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK TR on TR.RISKID = R.RISKID" +
            " LEFT JOIN TBL_RISKCATEGORY T on TR.RISKCATID = T.RISKCATID" +
            " LEFT JOIN TBL_RISK_ASSPLAN TRA ON TRA.ASSPLANID=R.ASSPLANID" +
            " WHERE T.RISKCATID in (${param1}) and TRA.ASSPLANID = #{param2}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    List<RiskAssplanRisk> findRiskByRiskType(String typeId, BigDecimal planId);
    
    @Select("select R.*" +
            " from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK TR on TR.RISKID = R.RISKID" +
            " LEFT JOIN TBL_RISKCATEGORY T on TR.RISKCATID = T.RISKCATID" +
            " LEFT JOIN TBL_RISK_ASSPLAN TRA ON TRA.ASSPLANID=R.ASSPLANID" +
            " WHERE TRA.ASSPLANID = #{param2}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
	List<RiskAssplanRisk> findRiskByPlanId(BigDecimal planId);

    @Select("select R.*" +
            "    from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK_ASSPLAN TRA on R.ASSPLANID = TRA.ASSPLANID" +
            "        LEFT JOIN TBL_RISK TR on TR.RISKID = R.RISKID" +
            "    WHERE TRA.ASSPLANID = #{param2} and R.RISKID IN (${param1})")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> findRiskInRiskIdAndAssId(String ids, BigDecimal planId, IPage pageBean);


    @Select("select R.*" +
            "    from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK_ASSPLAN TRA on R.ASSPLANID = TRA.ASSPLANID" +
            "    WHERE TRA.ASSPLANID = #{param1}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    List<RiskAssplanRisk> get1(BigDecimal planid);

    @Select("select R.*" +
            "   from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK TR ON R.RISKID = TR.RISKID" +
            "   WHERE TR.RISKID = #{param2} AND R.ASSPLANID = #{param1}")
    List<RiskAssplanRisk> get2(BigDecimal planid, BigDecimal riskid);

    @Select("select R.*" +
            "   from TBL_RISK_ASSPLAN_RISK R LEFT JOIN TBL_RISK_ASSPLAN TRA on R.ASSPLANID = TRA.ASSPLANID" +
            "   WHERE TRA.ASSPLANID = #{param1} ORDER BY ASSRISKID DESC")
    List<RiskAssplanRisk> findRiskByRisk(BigDecimal id);
//        ("from RiskAssPlanRisk r where r.assPlanId.assplanid = ? order by r.assriskid desc",id);


    @Select("select * from TBL_RISK_ASSPLAN_RISK WHERE ASSPLANID=#{param1}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    List<RiskAssplanRisk> findRiskAssplanRisk(BigDecimal assplanid);

    @Select("select * from TBL_RISK_ASSPLAN_RISK r where assplanid =#{param1} order by r.assriskid desc")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    List<RiskAssplanRisk> findRiskByRiskid(BigDecimal id);

    @Select("select  r.* from TBL_RISK_RISKMARKING rb INNER JOIN TBL_RISK_ASSPLAN_RISK r ON rb.ASSRISKID=r.ASSRISKID" +
            " where r.assplanid=#{param1} and rb.staffid=#{param3}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> findRiskandRiskAssPlan(BigDecimal assplanid, IPage page, BigDecimal staffid);

    
    @Select("select  r.* from TBL_RISK_RISKMARKING rb INNER JOIN TBL_RISK_ASSPLAN_RISK r ON rb.ASSRISKID=r.ASSRISKID" +
            " where r.assplanid=#{param1}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> findRiskandRiskAssPlanById(BigDecimal assplanid, IPage page);
    
    
    @Select("select DISTINCT r.* from TBL_RISK_RISKMARKING rb INNER JOIN TBL_RISK_ASSPLAN_RISK r ON rb.ASSRISKID=r.ASSRISKID" +
            " where r.assplanid=#{planId} and r.riskLevel =  #{level}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> findRiskByRiskAndLevel(BigDecimal planId, IPage pageBean, BigDecimal staffid, String level);
    
    @Select("select r.* from TBL_RISK_RISKMARKING rb INNER JOIN TBL_RISK_ASSPLAN_RISK r ON rb.ASSRISKID=r.ASSRISKID" +
            " where r.assriskid=#{assrisks} and r.riskId =  #{riskid}")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> fingRiskByAssIdAndRiskId(BigDecimal assrisks, BigDecimal riskid, IPage page);
    
    
    @Select("select RISKLEVEL from TBL_RISK_ASSPLAN_RISK where RISKID=#{riskid} AND ASSDATE = (SELECT MAX(ASSDATE) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = #{riskid})")
    String selectMaxLevelByRiskId(BigDecimal riskid);

    @SelectProvider(type=RiskAssplanRiskMapperSqlConfig.class,method="selectRiskInRiskIdAndAssId")
    @Results({
    	@Result(column="ASSRISKID",property="assriskid"),
    	@Result(column="ASSPLANID",property="assplanid"),
    	@Result(column="RISKID",property="risk.riskid",id=true),
    	@Result(column="RISKLEVEL",property="risklevel"),
    	@Result(column="ASSDATE",property="assdate"),
    	@Result(column="RISKNUMBER",property="risk.risknumber"),
    	@Result(column="RISKDES",property="risk.riskdes"),
    	@Result(column="RISKNAME",property="risk.riskname"),
    })
	List<RiskAssplanRisk> selectRiskInRiskIdAndAssId(PageInfo<RiskAssplanRisk> pageInfo) throws Exception;

    @SelectProvider(type=RiskAssplanRiskMapperSqlConfig.class,method="selectRiskInRiskIdCount")
	Integer selectRiskInRiskIdCount(PageInfo<RiskAssplanRisk> pageInfo) throws Exception;

    @Select("SELECT TRAR.RISKID,TRAR.ASSPLANID,TRAR.ASSRISKID,TRAR.ASSSTATUS,TRAR.RISKLEVEL,TRAR.FREQUENCY,TRAR.SEVERITY,TRAR.ASSDATE, TR.RISKNUMBER,TR.RISKDES,TR.RISKNAME,TRA.PLANCODE,TRA.PLANNAME,TRA.STARTDATE,TRA.ENDDATE,TRA.PLANDES FROM TBL_RISK_ASSPLAN_RISK TRAR LEFT JOIN TBL_RISK TR ON TRAR.RISKID = TR.RISKID LEFT JOIN TBL_RISK_ASSPLAN TRA ON TRAR.ASSPLANID = TRA.ASSPLANID WHERE TRAR.RISKID = #{riskid}  AND TRAR.ASSDATE = (SELECT MAX(ASSDATE) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = #{riskid} ) AND TRAR.ASSDATE IS NOT NULL")
    @Results({
    	@Result(column="RISKID",property="riskid"),
    	@Result(column="ASSRISKID",property="assriskid"),
    	@Result(column="ASSPLANID",property="assplanid"),
    	@Result(column="ASSSTATUS",property="assstatus"),
    	@Result(column="RISKLEVEL",property="risklevel"),
    	@Result(column="FREQUENCY",property="frequency"),
    	@Result(column="SEVERITY",property="severity"),
    	@Result(column="ASSDATE",property="assdate"),
    	@Result(column="RISKID",property="risk.riskid",id=true),
    	@Result(column="RISKNUMBER",property="risk.risknumber"),
    	@Result(column="RISKDES",property="risk.riskdes"),
    	@Result(column="RISKNAME",property="risk.riskname"),
    	@Result(column="ASSPLANID",property="assplan.assplanid",id=true),
    	@Result(column="PLANCODE",property="assplan.plancode"),
    	@Result(column="PLANNAME",property="assplan.planName"),
    	@Result(column="STARTDATE",property="assplan.startDate"),
    	@Result(column="ENDDATE",property="assplan.endDate"),
    	@Result(column="PLANDES",property="assplan.plandes"),
    })
	List<RiskAssplanRisk> selectRiskAssplanRiskByRiskIdList(BigDecimal riskid) throws Exception;

    @Select("SELECT COUNT(0) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = #{riskid} AND TRAR.ASSDATE IS NOT NULL")
	Integer selectRiskAssplanRiskByRiskIdCount(BigDecimal riskid) throws Exception;

    Integer queryNumberRisksYiBan(@Param("orgid") BigDecimal orgid,@Param("yiban")String yiban);

    Integer queryNumberRisksZhongDa(@Param("orgid") BigDecimal orgid, @Param("zhongda") String zhongda);
    
    
    @Select("select DISTINCT r.* from TBL_RISK_RISKMARKING rb INNER JOIN TBL_RISK_ASSPLAN_RISK r ON rb.ASSRISKID=r.ASSRISKID" +
            " where riskid=#{riskid} and  r.RISKLEVEL is not null ")
    @ResultMap("RM_RISK_ASSPLAN_RISK")
    IPage<RiskAssplanRisk> findRiskResultById(@Param("riskid")BigDecimal riskid, IPage pageBean, @Param("staffid")BigDecimal staffid);
    

    List<RiskAssplanRisk> getSyFxtjRlt(@Param("orgid")String orgid,@Param("value")String value);
    
    List<RiskAssplanRisk> getSyFxtjRltYgb(@Param("orgid")String orgid);
    
    @Select("select a.*  from TBL_RISK_ASSPLAN_RISK a  left join tbl_risk s on s.riskid=a.riskid   where a.assdate=(SELECT MAX(ASSDATE) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = a.riskid) "
    		+ " and s.unit=#{orgid} and ( s.riskstatus !=0 or s.riskstatus is null)")
    List<RiskAssplanRisk> getYpg(@Param("orgid")String orgid);
    
    @Select("select a.*  from TBL_RISK_ASSPLAN_RISK a  left join tbl_risk s on s.riskid=a.riskid   where a.assdate=(SELECT MAX(ASSDATE) FROM TBL_RISK_ASSPLAN_RISK WHERE RISKID = a.riskid) "
    		+ " and s.unit=#{orgid} and s.riskstatus =0 ")
    List<RiskAssplanRisk> getYgb(@Param("orgid")String orgid);
}
