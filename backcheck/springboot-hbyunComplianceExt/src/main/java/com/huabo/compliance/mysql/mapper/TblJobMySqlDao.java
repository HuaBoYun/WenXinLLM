package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblJobMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblJobMySqlDao extends BaseMapper<TblJobMySql> {
    @Select("select * from TBL_JOB where COMPANYID= #{companyid}")
    List<TblJobMySql> listBySql(BigDecimal companyid);

    //    @Insert("INSERT INTO TBL_JOB (JOBID,JOBNAME,COMPANYID) VALUES (#{jobid},#{jobname},#{companyId})")
    @InsertProvider(method = "saveJob", type = TblJobDaoSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="jobid", keyColumn="JOBID")
    void saveJob(TblJobMySql job);

    @Select("SELECT * FROM TBL_JOB WHERE JOBID = #{jid}")
    TblJobMySql selectJid(String jid);

    @Delete("DELETE FROM TBL_JOB WHERE selectedId = #{selectedId}")
    void deleteSelectedId(TblJobMySql selectedId);

    //    @Select("select * from TBL_JOB where COMPANYID=#{companyid}")
    @SelectProvider(method = "selectListByPageInfo", type = TblJobDaoSqlConfig.class)
    List<TblJobMySql> selectListByPageInfo(PageInfo<TblJobMySql> pageInfo, BigDecimal companyId);

    @Select("SELECT * FROM TBL_JOB where COMPANYID= #{companyId}")
    Integer selectListByPageInfoCount(PageInfo<TblJobMySql> pageInfo, BigDecimal companyId);

    @Select("SELECT * FROM TBL_JOB WHERE JOBID = #{jobid}")
    List<TblJobMySql> findByRid(String jobid);

    @UpdateProvider(type = TblJobDaoSqlConfig.class, method = "updateJob")
    void updateJob(TblJobMySql newJob);

    @Select("SELECT count(*) FROM TBL_JOB where COMPANYID= #{companyId}")
    Integer selectListByPageCount(PageInfo<TblJobMySql> pageInfo, BigDecimal companyId);

    @Delete("DELETE FROM TBL_JOB WHERE JOBID = #{jobid}")
    void deleteJob(BigDecimal jobid);

    @Select("SELECT JOB.JOBID,JOB.JOBNAME,JOB.CODE,JOB.DESCRIPTION,JOB.CATEGORYNAME,JOB.PKYMJOBID,ORG.PKYMoRGID FROM TBL_JOB JOB LEFT JOIN TBL_ORGANIZATION ORG ON JOB.COMPANYID = ORG.ORGID WHERE COMPANYID = #{orgId}")
    @Results({
    	@Result(column="JOBID",property="jobid"),
    	@Result(column="JOBNAME",property="jobname"),
    	@Result(column="CODE",property="code"),
    	@Result(column="DESCRIPTION",property="description"),
    	@Result(column="CATEGORYNAME",property="categoryName"),
    	@Result(column="PKYMJOBID",property="pkymJobId"),
    	@Result(column="PKYMORGID",property="pkymOrgId"),
    })
	List<TblJobMySql> selectJobListByOrgId(Integer orgId) throws Exception;

    @Update("UPDATE TBL_JOB SET PKYMJOBID = #{pkYmJobId} WHERE JOBID = #{jobid}")
	void updatePkYmbyRoleId(String pkYmJobId, BigDecimal jobid);

    @Select("SELECT JOB.JOBID,JOB.JOBNAME,JOB.CODE,JOB.DESCRIPTION,JOB.CATEGORYNAME,JOB.PKYMJOBID,ORG.PKYMoRGID FROM TBL_JOB JOB LEFT JOIN TBL_ORGANIZATION ORG ON JOB.COMPANYID = ORG.ORGID WHERE JOB.JOBID = #{jobId}")
    @Results({
    	@Result(column="JOBID",property="jobid"),
    	@Result(column="JOBNAME",property="jobname"),
    	@Result(column="CODE",property="code"),
    	@Result(column="DESCRIPTION",property="description"),
    	@Result(column="CATEGORYNAME",property="categoryName"),
    	@Result(column="PKYMJOBID",property="pkymJobId"),
    	@Result(column="PKYMORGID",property="pkymOrgId"),
    })
	TblJobMySql selectJobInfoByJobId(BigDecimal jobId) throws Exception;
}
