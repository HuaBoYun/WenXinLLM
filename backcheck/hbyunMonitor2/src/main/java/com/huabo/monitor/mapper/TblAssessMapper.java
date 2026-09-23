package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.huabo.monitor.vo.param.MaxNumberParam;
import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessPlan;
import com.huabo.monitor.entity.TblAssessPlanVo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblStaff;
import com.huabo.monitor.mapper.Provider.TblAssessProvider;

@Mapper
public interface TblAssessMapper extends BaseMapper<TblAssess> {

	List<TblAssess> findList(@Param("queryParam")TblAssessVo queryParam,@Param("sql")String sql);

	List<TblAssessVo> findPageBeanNew(@Param("queryParam")TblAssessVo queryParam,@Param("sql")String sql);

	List<TblAssessVo> findPageBeanPJLXNew(@Param("queryParam")TblAssessVo queryParam,@Param("sql")String sql);
	
	List<TblAssessVo> findPageBeanPJJGNew(@Param("queryParam")TblAssessVo queryParam,@Param("sql")String sql);
 
	List<TblAssessVo> getEvaluationResultsList(@Param("orgId")BigDecimal orgId,@Param("year")String year ,@Param("authType")Integer authType,@Param("staffid")BigDecimal staffid);
	
	Long findAssessCount(@Param("queryParam")TblAssessVo queryParam);
	
	Map<String, Object>  getInfo(@Param("orgid")BigDecimal  orgid,@Param("NoId")Integer NoId);
	
	@Select("select t.orgid,o.orgname from (select tblcomany as orgid from TBL_ASSESS group by tblcomany)t left join tbl_organization o on o.orgid=t.orgid")
	List<TblOrganization>getAssessCompanyList();
	
	List<EvaluationTrackingStatisticsResult> findEvaluationTrackingStatisticsNew(@Param("queryParam") EvaluationTrackingStatisticsResult queryParam);

	@Select("select  TAT.CHECKLEVEL as name ,COUNT(DISTINCT t.assid ) as value from "+
		" TBL_ASSESS t "+
		" inner join TBL_ASSESSTEMPLE ta on t.asstemid=ta.asstemid "+
		" left join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID "+
		" left join TBL_ASSESS_TARGET TAT on TAT.assid = t.ASSID where t.assstatus "+
		" in (3,4) and TAT.FINALSCORE is not null and TAT.CHECKLEVEL is not null AND t.TBLCOMANY=#{company} and YEAR(t.createtime)=#{year} group by TAT.CHECKLEVEL "
//		" union "+
//		" select  TAT.FINALLEVEL as name  ,count(1) as value from "+
//		" TBL_ASSESS t "+
//		" inner join TBL_ASSESSTEMPLE ta on t.asstemid=ta.asstemid "+
//		" left join TBL_ASSESS_MARK TAM on t.ASSID = TAM.ASSID "+
//		" left join TBL_ASSESS_TARGET TAT on TAT.assid = t.ASSID where t.assstatus "+
//		" in (3,4) and TAT.FINALSCORE is not null and TAT.FINALLEVEL is not null AND t.TBLCOMANY=#{company} and YEAR(t.assstartday)=#{year} group by TAT.FINALLEVEL
 )
	List<Map<String, Object>> getProjectMaturityAnalysis(@Param("company")BigDecimal company,@Param("year")String year);
	
	@Select("select o.orgname as name,count(1) as value from TBL_ASSESS a left join tbl_organization o  on o.orgid=a.TBLCOMANY   group by o.orgname ")
	List<Map<String, Object>> getCompanyProjectEvaluations(@Param("year")String year);
	
	String getMaxNumberForNkpj(@Param("queryParam")MaxNumberParam queryParam);
	
    @SelectProvider(type = TblAssessProvider.class,method = "findPageBean" )
    <P extends IPage<TblAssessVo>> P findPageBean(
            P page,
            @Param("staffid") BigDecimal staffid,
            @Param("projkey") String projkey,
            @Param("projname") String projname,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate,
            @Param("assstatus") String assstatus);

    /**
     *  yhr
     * @param <P>
     * @return
     */
    @SelectProvider(type = TblAssessProvider.class,method = "findPageBeanPJLX" )
    <P extends IPage<TblAssessVo>> P findPageBeanPJLX(
            P page,
            @Param("isAudit") boolean isAudit,
            @Param("orgid") BigDecimal orgid,
            @Param("staffid") BigDecimal staffid,
            @Param("realName") String realName,
            @Param("assNumnber") String assNumnber,
            @Param("assName") String assName,
            @Param("startDate") String startDate,
            @Param("startDates") String startDates,
            @Param("endDate") String endDate,
            @Param("endDates") String endDates,
			@Param("authorityType")Integer authorityType
          );


    @SelectProvider(type = TblAssessProvider.class,method = "findPageBeanPJJG" )
    <P extends IPage<TblAssessVo>> P findPageBeanPJJG(
            P page,
            @Param("assNumnber") String assNumnber,
            @Param("assName") String assName,
            @Param("startDate") String startDate,
            @Param("startDates") String startDates,
            @Param("endDate") String endDate,
            @Param("endDates") String endDates,
            @Param("staff") TblStaffUtil staff,
			@Param("authorityType")Integer authorityType
	);

    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);


    @Select("select * from tbl_organization o where o.orgid in(select max(assorgid)  from TBL_ASSESS_MARK t where t.ASSID =#{assId} group by assorgid) and o.status = 0 ")
    List<TblOrganization> getOrgByassId(@Param("assId") BigDecimal assId);




    @Select("select Ta.*,Ta2.TEMPLENAME,T.ORGNAME,T.ORGID , TS.REALNAME,Ts.USERNAME from TBL_ASSESS_TARGET tat inner join TBL_ASSESS TA on tat.ASSID = TA.ASSID\n" +
            "inner join TBL_ORGANIZATION T on tat.ORGID = T.ORGID inner join TBL_STAFF  TS on ta.LEADERID=ts.STAFFID\n" +
            "inner  join TBL_ASSESSTEMPLE TA2 on TA2.ASSTEMID=Ta.ASSTEMID\n" +
            "where tat.ASSID=#{assId} and tat.ORGID=#{orgid}")
    TblAssessVo queryTblAssessVoByAssidAndOrgid(@Param("assId") BigDecimal assId,@Param("orgid") BigDecimal orgid);



    @Select("select count(0) from TBL_ASSESS WHERE asstemid=#{asstemid}")
    Integer findTblAssessByTempid(@Param("asstemid")BigDecimal asstemid);
    
    
    @Insert("insert into TBL_ASSESS_TEAMMEMBER (id,assid,staffid) values (HIBERNATE_SEQUENCE.nextval,#{assid},#{assteammemberid})")
	int insAssessTeamMember(String assteammemberid, BigDecimal assid);
    
    @Delete("DELETE FROM TBL_ASSESS_TEAMMEMBER WHERE assid=#{assid}")
	int delAssessTeamMember( BigDecimal assid);
    
    @Select("select ts.* from TBL_ASSESS_TEAMMEMBER tat "
    		+ " LEFT JOIN TBL_STAFF ts on tat.staffid=ts.staffid "
    		+ " where tat.assId=#{asstemid} ")
    List<TblStaff> gettAssteamMembers(BigDecimal assId);
    
    
    @SelectProvider(type = TblAssessProvider.class,method = "findPageBeanPJJH" )
    <P extends IPage<TblAssessPlanVo>> P findPageBeanPJJH(
            P page,
            @Param("isAudit") boolean isAudit,
            @Param("orgid") BigDecimal orgid,
            @Param("staffid") BigDecimal staffid,
            @Param("realName") String realName,
            @Param("assNumnber") String assNumnber,
            @Param("assName") String assName,
            @Param("startDate") String startDate,
            @Param("startDates") String startDates,
            @Param("endDate") String endDate,
            @Param("endDates") String endDates,
            @Param("status") Integer status,
			@Param("authorityType")Integer authorityType
          );

     //tbl_assess_mark  中核 修改为负责人进行计算操作
    @Select("select count(1) from tbl_assess where assid=#{assid} and leaderid=#{staffid}")
    int getMajorByAssid(@Param("assid")BigDecimal assid,@Param("staffid")String staffid);

	@Select("select STATUS, count(*) as num from TBL_ASSESS where tblcomany in (select ORGID from TBL_ORGANIZATION where 1 = 1 start with ORGID =#{belongGroup} "
			+ " and ORGTYPE!=0 AND ORGTYPE <100 connect by prior orgid= FATHERORGID) or tblcomany =#{belongGroup} group by STATUS")
	List<EvaluationTrackingStatisticsResult> findEvaluationTrackingStatistics(@Param("belongGroup") Integer belongGroup);


     @Select("select max(assorgid)  from TBL_ASSESS_MARK t where t.ASSID =#{assid} group by assorgid")
     List<BigDecimal> getMaxAssorgid(@Param("assid")BigDecimal assid);

     
     @Select("select  LEVELID  from TBL_SECRECT_LEVEL where LEVELTYPE=2 and level >=(select level from TBL_SECRECT_LEVEL where   levelid=#{id} )")
     List<String> getSecrectLevel(@Param("id")String id);
     
     
     @Select("select distinct ass.staffid  from TBL_ASSESS_STAFF ass left join Tbl_Assess_Mark  am on  am.assmarkid=ass.ASSMARKID where am.ASSID=#{id} ")
     List<String> findXfList(@Param("id")String id);
}
