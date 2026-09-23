package com.huabo.monitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.monitor.entity.TblTesttaskProblemFind;
import com.huabo.monitor.vo.result.DefectGradeStatisticsResult;
import com.huabo.monitor.vo.result.OneLevelProcessStatisticsResult;
import com.huabo.monitor.vo.result.YearStatisticsResult;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblTesttaskProblemFindMapper extends BaseMapper<TblTesttaskProblemFind> {
	
	List<TblTesttaskProblemFind>  findALLProblemLedgerListNew(@Param("queryParam")TblTesttaskProblemFind query,@Param("sql")String sql);
	
	List<YearStatisticsResult>  findYearStatisticsNew(@Param("queryParam")YearStatisticsResult query);
	
	List<DefectGradeStatisticsResult> findDefectGradeStatisticsNew(@Param("queryParam") DefectGradeStatisticsResult queryParam);

	List<Map<String, Object>> getDefectProjectsYearAnalysis(@Param("orgId")BigDecimal orgId,@Param("year")String year);
	
	
	List<Map<String, Object>> getDistributionDefectTypes(@Param("orgId")BigDecimal orgId,@Param("year")String year,@Param("qx")String qx);
	
	
	List<OneLevelProcessStatisticsResult> findOneLevelProcessStatisticsNew(@Param("queryParam") OneLevelProcessStatisticsResult queryParam);

	@Select("select o.ORGNAME as name,count(1) as value from TBL_TESTTASK_PROBLEMFIND p left join TBL_ORGANIZATION o on o.ORGID=P.LINKORG where p.status in (6)  group by o.ORGNAME")
	List<Map<String, Object>> getDefectQuantityIssues();
	
	@Select("select '一般' as name,count(1) as value from TBL_TESTTASK_PROBLEMFIND where   year(createtime)=#{year} and linkorg=#{orgid}  and status in (6)  and defectlevel='一般' "+
	" union "+
	" select '重要' as name,count(1) as value from TBL_TESTTASK_PROBLEMFIND where  year(createtime) =#{year} and linkorg=#{orgid}  and status in (6)   and defectlevel='重要' "+
	" union "+
	" select '重大' as name,count(1) as value from TBL_TESTTASK_PROBLEMFIND where  year(createtime) =#{year} and linkorg=#{orgid}  and status in (6)   and defectlevel='重大'")
	List<Map<String, Object>>  getProblemDiscoveryDefectGradeStatistics(@Param("orgid")BigDecimal orgid,@Param("year")String year);
	
	@Select("select o.ORGNAME as name,count(1) as value from TBL_TESTTASK_PROBLEMFIND p left join TBL_ORGANIZATION o on o.ORGID=p.mainorg where p.linkorg=#{orgid} and year(p.createtime)=#{year} and p.status in (6) group by o.ORGNAME")
	List<Map<String, Object>> getDefectQuantityIssuesByDep(@Param("orgid")BigDecimal orgid,@Param("year")String year);
	
	
	@Select("${sql}")
    List<TblTesttaskProblemFind> getListBySql(@Param("sql") String sql);
    
    @Delete("DELETE FROM TBL_TESTTASK_PROBLEMFIND where findid=#{findid}")
    void delTesttaskProblemFind(@Param("findid")BigDecimal findid);

    @InsertProvider(type=TblTesttaskProblemFindMapperSqlConfig.class,method="insertEntity")
    @Options(useGeneratedKeys=true, keyProperty="findid", keyColumn="FINDID")
	void insertEntity(TblTesttaskProblemFind tblTesttask) throws Exception;

    @UpdateProvider(type=TblTesttaskProblemFindMapperSqlConfig.class,method="updateEntity")
	void updateEntity(TblTesttaskProblemFind tblTesttask) throws Exception;
    
    @Select("SELECT TTP.*,TS.REALNAME,TORG.ORGNAME,LK.RISKID,LK.RISKNUMBER FROM TBL_TESTTASK_PROBLEMFIND TTP "
			+ " LEFT JOIN TBL_RISK LK ON LK.RISKID=TTP.RISKNUMBERID "
    		+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID "
    		+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG "
    		+ " where testtaskid=#{testtaskid}")
    List<TblTesttaskProblemFind> getTesttaskProblemFindByTesttaskid(@Param("testtaskid")BigDecimal testtaskid);
    
    @Select("SELECT TTP.*,TS.REALNAME,TORG.ORGNAME,LK.RISKID,LK.RISKNUMBER FROM TBL_TESTTASK_PROBLEMFIND TTP "
			+ " LEFT JOIN TBL_RISK LK ON LK.RISKID=TTP.RISKNUMBERID "
    		+ " LEFT JOIN TBL_STAFF TS ON TS.STAFFID=TTP.REFORMSTAFFID "
    		+ " LEFT JOIN TBL_ORGANIZATION TORG ON TORG.ORGID=TTP.MAINORG "
    		+ " where findid=#{findid}")
    TblTesttaskProblemFind getById(@Param("findid")BigDecimal findid);
    
    @Select("SELECT * FROM TBL_TESTTASK_PROBLEMFIND  where findid=#{findid}")
    TblTesttaskProblemFind findById(@Param("findid")BigDecimal findid);
    
    @Select("${sql}")
    <p extends IPage<TblTesttaskProblemFind>>  p  getSqlPage(p page, @Param("sql") String sql);

	@Select("select ONEPROCESS,count(*) as num from TBL_TESTTASK_PROBLEMFIND where "
			+ "        mainorg in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID=#{belongGroup} "
			+ "        and ORGTYPE!=0 AND ORGTYPE<100  connect by prior orgid= FATHERORGID) or mainorg =#{belongGroup} "
			+ "group by ONEPROCESS")
	List<OneLevelProcessStatisticsResult> findOneLevelProcessStatistics(@Param("belongGroup") Integer belongGroup);

	@Select("select DEFECTTYPE,count(*) as num from TBL_TESTTASK_PROBLEMFIND where "
			+ "        mainorg in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID=#{belongGroup} "
			+ "        and ORGTYPE!=0 AND ORGTYPE<100  connect by prior orgid= FATHERORGID) or mainorg =#{belongGroup} "
			+ "group by DEFECTTYPE")
	List<DefectGradeStatisticsResult> findDefectGradeStatistics(@Param("belongGroup") Integer belongGroup);

	@Select("select TESTYEAR,count(*) as num from TBL_TESTTASK_PROBLEMFIND where "
			+ "        mainorg in (select ORGID from TBL_ORGANIZATION where 1=1 start with ORGID=#{belongGroup} "
			+ "        and ORGTYPE!=0 AND ORGTYPE<100  connect by prior orgid= FATHERORGID) or mainorg =#{belongGroup} "
			+ "group by TESTYEAR")
	List<YearStatisticsResult> findYearStatistics(@Param("belongGroup")Integer belongGroup);
	
	@Update("update TBL_TESTTASK_PROBLEMFIND set zgstatus=1 where findid=#{findid}")
    void sendreform(BigDecimal findid);
}
