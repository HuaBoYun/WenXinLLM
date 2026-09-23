package com.huabo.monitor.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ws.rs.QueryParam;

import com.huabo.monitor.vo.result.EvaluationTrackingStatisticsResult;
import com.huabo.monitor.vo.result.TestTrackingStatisticsResult;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.One;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblAssesscategory;
import com.huabo.monitor.entity.TblTestTemplate;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTestplanVo;
import com.huabo.monitor.entity.TblTesttempleVo;
import com.huabo.monitor.entity.Tree;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author yhr
 * @since 2022-09-07
 */
public interface TblTestplanMapper extends   BaseMapper<TblTestplan> {

	
	List<TblTestplan> findAllNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);
	
	List<TblTestplan> findAllnoSjNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);
	
	
	//测试跟踪主页列表
	List<TblTestplan> findAllTrackNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);

	//测试跟踪主页列表
	List<TblTestplan> findAllnoSjTrackNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);
	
	//测试任务
	List<TblTestplan> getCsrwFindAllRwToOrgNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);
	
	//测试结果汇总
	List<Map<String, Object>> findAllCSHZNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);

	//测试结果汇总
	List<Map<String, Object>> findAllWCSjCSHZNew(@Param("queryParam")TblTestplan queryParam,@Param("sql")String  sql);

	List<TestTrackingStatisticsResult> findTestTrackingStatisticsNew(@Param("queryParam")TestTrackingStatisticsResult queryParam);
	
	//集团测试结果汇总
	List<Map<String, Object>> findAll(@Param("queryParam")TblTestplan queryParam);

	
	@Select("SELECT COUNT(0) FROM TBL_TESTPLAN WHERE TESTTEMID = #{templId}")
	Integer findPlanCountByTestTempId(Integer templId) throws Exception;

    @Select("${sql}")
    <p extends IPage<TblTestplan>>  p  getSqlPage(p page, @Param("sql") String sql);

    @Select("select t.*,gp.planname as groupplanname,s.realname as createName from TBL_TESTPLAN t left join tbl_group_testplan gp on gp.id=t.groupid left join tbl_staff s on s.staffid=t.creatid where testplanid=#{testplanid}")
    @Results(id="TblTestplanVo",value={
            @Result(property = "testplanid",column = "testplanid",id = true),
            @Result(property = "testtemid",column = "testtemid"),
            @Result(property = "assid",column = "assid"),
            @Result(property = "orgid",column = "orgid"),
            @Result(property = "creatid",column = "creatid"),
            @Result(property = "createName",column = "createName"),
            @Result(property = "staffid",column = "staffid"),
            @Result(property = "testtemple",column = "testtemid",one=@One(select="com.huabo.monitor.mapper.TblTesttempleMapper.selectById")),
            @Result(property = "assidtem",column = "assid",one=@One(select="com.huabo.monitor.mapper.TblAssessPlanMapper.selectById")),
            @Result(property = "groupPlan",column = "groupPlanId",one=@One(select="com.huabo.monitor.mapper.TblGroupTestplanMapper.selectById")),
    })
    TblTestplanVo getOneTblTestplanVo(BigDecimal testplanid);

    @Select("select * from TBL_TESTTEMPLE where testtemid=#{testtemid}")
    @Results(id="TblTesttempleVo",value={
            @Result(property = "staffid",column = "staffid"),
            @Result(property = "chuangjianren",column = "staffid",one=@One(select="com.huabo.monitor.mapper.TblStaffMapper.selectById")),

    })
    TblTesttempleVo getOneTblTesttempleVo(@Param("testtemid") BigDecimal testtemid);



    @Select("${sql}")
    @ResultMap("TblTesttempleVo")
    <p extends IPage<TblTesttempleVo>>  p  getTesttempleVoSqlPage(p page, @Param("sql") String sql);



    @Select("${sql}")
    @ResultMap("TblTesttempleVo")
      List<TblTesttempleVo> getTesttempleVoSqlPage(@Param("sql") String sql);


    
    @Select("select\n" +
            "    decode(CONNECT_BY_ISLEAF,0,'true','false') AS \"isParent\",\n" +
            "    t.typeid as \"id\",\n" +
            "    t.TYPENAME as \"name\",\n" +
            "    t.PARENTID as \"pId\"\n" +
            "from TBL_TESTTEMPL_TYPE t\n" +
            "CONNECT BY PRIOR T.TYPEID = T.PARENTID\n" +
            "START WITH t.parentId is null and t.testTempletaId = #{tempid}")
    List<Tree> getTreeListByTemid(BigDecimal tempid);



    @Select("select\n" +
            "    'false'  AS \"isParent\",\n" +
            "    t.typeid as \"id\",\n" +
            "    t.TYPENAME as \"name\",\n" +
            "    t.PARENTID as \"pId\"\n" +
            "from TBL_TESTTEMPL_TYPE t where  testTempletaId = #{tempid}")
    List<Tree> getTreeListByTemidNew(BigDecimal tempid);


    
    /*
        分配右侧列表
     */
    @Select("${sql}")
    IPage<Map<String,Object>>  fingByTree(IPage<Map<String,Object>> page, @Param("sql") String sql);

    @Select("select * from TBL_TESTPLAN where testplanid=#{testplanid}")
    TblTestplan findById(BigDecimal testplanid);
    
    
    @Delete("DELETE FROM TBL_TESTPLAN where testplanid=#{testplanid}")
    void deleteTestPlan(@Param("testplanid")BigDecimal testplanid);

    @InsertProvider(type=TblTestplanMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="testplanid", keyColumn="TESTPLANID")
	void insertEntity(TblTestplan tblTestplan) throws Exception;

    @UpdateProvider(type=TblTestplanMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTestplan tblTestplan) throws Exception;

	@Select("select PLANSTATUS, count(*) as num from TBL_TESTPLAN where orgid in (select ORGID from TBL_ORGANIZATION where 1 = 1 start with ORGID =#{belongGroup} "
			+ " and ORGTYPE!=0 AND ORGTYPE <100 connect by prior orgid= FATHERORGID) or orgid =#{belongGroup} group by PLANSTATUS")
	List<TestTrackingStatisticsResult> findTestTrackingStatistics(@Param("belongGroup") Integer belongGroup);


    @Select("select count(1) from TBL_TESTPLAN where groupid=#{id} and  creatid=#{staffid}")
    Integer getCountById(BigDecimal id,String staffid);
    
    @Select("SELECT DISTINCT TASK.CPUSERID from TBL_TESTELEMENT  emt left  JOIN TBL_TESTTASK task on emt.ELEMENTID=TASK.ELEMENTID WHERE   EMT.TEMPLID=#{temid} and  TASK.PLANID=#{planid} and task.CPUSERID is not null")
    List<String> findList(BigDecimal planid,BigDecimal temid);

}
