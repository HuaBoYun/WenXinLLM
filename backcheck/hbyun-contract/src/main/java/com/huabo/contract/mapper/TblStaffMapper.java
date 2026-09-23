package com.huabo.contract.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.entity.TblStaff;
import com.huabo.contract.mappersql.TblStaffMapperSqlConfig;
import com.huabo.contract.vo.StaffResult;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblStaffMapper extends BaseMapper<TblStaff> {

	@Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{staffid}")
	TblStaff findByStaffid(String staffid) throws Exception;

	@UpdateProvider(method = "updateTs",type = TblStaffMapperSqlConfig.class)
	void updateTs(TblStaff ts) throws Exception;

	@Select("SELECT RID from TBL_ROLE where rname = #{type} and COMPANYID =#{orgid}")
	Integer findLikeUser(Integer orgid, String type) throws Exception;

	/**
	 * 获取类型
	 * @param staffId
	 * @param rid
	 * @return
	 */
	@SelectProvider(method="findUserInfoExam",type=TblStaffMapperSqlConfig.class)
	StaffResult findUserInfoExam(@Param("staffId") Integer staffId, @Param("rid") Integer rid);

	@Select("SELECT STAFFID FROM TBL_STAFF WHERE ORGID IN (${deptIdStrs})")
	List<String> selectStaffIdsByCompanyId(@Param("deptIdStrs") String deptIdStrs) throws Exception;

	@SelectProvider(method="findAllPageBeanPid",type=TblStaffMapperSqlConfig.class)
	IPage<TblStaff> findAllPageBeanPid(IPage<TblStaff> page, TblOrganization attribute);

	@Select("SELECT JOBNAME FROM TBL_JOB WHERE JOBID = (SELECT JOBID FROM TBL_STAFF WHERE STAFFID = #{staffid})")
	String selectJobNameByJobId(BigDecimal staffid);

	@SelectProvider(method="selectContractStaffPageInfo",type=TblStaffMapperSqlConfig.class)
	IPage<TblStaff> selectContractStaffPageInfo(IPage<TblStaff> page, TblStaff staff);
	
	@SelectProvider(method="selectTreeListByPageInfo",type=TblStaffMapperSqlConfig.class)
	IPage<TblStaff> selectTreeListByPageInfo(IPage<TblStaff> page, BigDecimal orgid);

	@Select("SELECT STAFFID,ORGID FROM TBL_STAFF WHERE USERNAME = #{userName}")
	TblStaff selectStaffInfoByUsername(String userName);

	@Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME FROM TBL_STAFF TS LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.USERNAME = #{userName}")
	@Results({
			@Result(column="STAFFID",property="staffid"),
			@Result(column="REALNAME",property="realname"),
			@Result(column="USERNAME",property="username"),
			@Result(column="ADDRESS",property="address"),
			@Result(column="EMAIL",property="email"),
			@Result(column="MIBLEPHONE",property="miblephone"),
			@Result(column="MEMO",property="memo"),
			@Result(column="STATUS",property="status"),
			@Result(column="CREATETIME",property="createtime"),
			@Result(column="JOBID",property="jobid"),
			@Result(column="OUTSIDEID",property="outSideId"),
			@Result(column="OUTSIDEOPENDID",property="outSideOpenId"),
			@Result(column="FIXEDPHONE",property="fixedphone"),
			@Result(column="RID",property="trole.rid",id=true),
			@Result(column="RNAME",property="trole.rname"),
			@Result(column="ORGID",property="linkDetp.orgid",id=true),
			@Result(column="ORGNAME",property="linkDetp.orgname"),
	})
	TblStaff getStaffByUsername(@Param("userName")String userName) throws Exception;

	@Select("select REALNAME from TBL_STAFF WHERE STAFFID = #{staffId}")
	String getStaffName(@Param("staffId") Long staffId);

	@Select("select REALNAME from TBL_STAFF WHERE STAFFID in (${staffIds})")
	List<String> getStaffNames(@Param("staffIds") String staffIds);
}
