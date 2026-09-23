package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblJob;

public interface TblJobDao extends BaseMapper<TblJob> {
    @Select("select * from TBL_JOB where COMPANYID= #{companyid}")
    List<TblJob> listBySql(BigDecimal companyid);

    @InsertProvider(method = "saveJob",type = TblJobDaoSqlConfig.class)
    void saveJob(TblJob job);

    @Select("SELECT * FROM TBL_JOB WHERE JOBID = #{jid}")
    TblJob selectJid(String jid);

    @Delete("DELETE FROM TBL_JOB WHERE selectedId = #{selectedId}")
    void deleteSelectedId(TblJob selectedId);

    @SelectProvider(method = "selectListByPageInfo",type = TblJobDaoSqlConfig.class)
    IPage<TblJob> selectListByPageInfo(IPage<TblJob> page, BigDecimal companyId, String orgIds, String jobName, String orgName);
    
    @Select("SELECT * FROM TBL_JOB where COMPANYID= #{companyId}")
    Integer selectListByPageInfoCount(PageInfo<TblJob> pageInfo, BigDecimal companyId);

    @Select("SELECT * FROM TBL_JOB WHERE JOBID = #{jobid}")
    List<TblJob> findByRid(String jobid);

    @UpdateProvider(type=TblJobDaoSqlConfig.class,method="updateJob")
    void updateJob(TblJob newJob);

    @Delete("DELETE FROM TBL_JOB WHERE JOBID = #{jobid}")
    void deleteJob(BigDecimal jobid);
    
    @Select("SELECT * FROM TBL_JOB WHERE historycode = #{historycode}")
    List<TblJob> findbyHistoryId(String historycode);
    
     @Update("update tbl_job j set j.categoryid=(select b.jobid from tbl_job b where b.historycode=j.categoryid) where datasource='zz'")
     void updateZzCategoryid();

     @Select("SELECT JOB.JOBID,JOB.JOBNAME,JOB.CODE,JOB.DESCRIPTION,JOB.CATEGORYNAME,JOB.PKYMJOBID,ORG.PKYMORGID FROM TBL_JOB JOB LEFT JOIN TBL_ORGANIZATION ORG ON JOB.COMPANYID = ORG.ORGID WHERE COMPANYID = #{orgId}")
     @Results({
     	@Result(column="JOBID",property="jobid"),
     	@Result(column="JOBNAME",property="jobname"),
     	@Result(column="CODE",property="code"),
     	@Result(column="DESCRIPTION",property="description"),
     	@Result(column="CATEGORYNAME",property="categoryName"),
     	@Result(column="PKYMJOBID",property="pkymJobId"),
     	@Result(column="PKYMORGID",property="pkymOrgId"),
     })
 	List<TblJob> selectJobListByOrgId(BigDecimal orgId) throws Exception;

     @Update("UPDATE TBL_JOB SET PKYMJOBID = #{pkYmJobId} WHERE JOBID = #{jobid}")
 	void updatePkYmbyRoleId(String pkYmJobId, BigDecimal jobid) throws Exception;

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
 	TblJob selectJobInfoByJobId(BigDecimal jobId) throws Exception;

    @Select("select jobid from tbl_job where jobname like #{jobName} ")
    List<Integer> queryJobidByJobname(String jobName);
}
