package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.FaAccbookinfoUtil;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.UserLoginLog;
import com.huabo.system.service.UserService;
import com.huabo.system.vo.result.StaffResult;

import net.sf.json.JSONObject;

public interface TblStaffMapper extends BaseMapper<TblStaff> {

    @Select("SELECT STAFFID,REALNAME,ORGID,USERNAME FROM TBL_STAFF WHERE STAFFID = #{staffId}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "USERNAME", property = "username"),
    })
    TblStaff selectStaff(@Param("staffId") String staffId) throws Exception;

    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.PKYMSTAFFID,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,TS.HISTORYCODE,TS.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.USERNAME = #{userName} AND TS.PASSWORD = #{password} AND TS.STATUS = 1")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATETIME", property = "createDate"),
            @Result(column = "JOBID", property = "jobid"),
            @Result(column = "OUTSIDEID", property = "outSideId"),
            @Result(column = "OUTSIDEOPENDID", property = "outSideOpenId"),
            @Result(column = "FIXEDPHONE", property = "fixedphone"),
            @Result(column = "RID", property = "trole.rid", id = true),
            @Result(column = "RNAME", property = "trole.rname"),
            @Result(column = "ORGID", property = "linkDetp.orgid", id = true),
            @Result(column = "ORGNAME", property = "linkDetp.orgname"),
            @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
            @Result(column = "HISTORYCODE", property = "historycode"),
            @Result(column = "SECRECTLEVELID", property = "secrectLevelId"),
            @Result(column = "SECRECTLEVELNAME", property = "secrectLevelName"),
    })
    TblStaff selectUniqueStaffInfo(@Param("userName") String userName, @Param("password") String password) throws Exception;
    
    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.PKYMSTAFFID,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,TS.HISTORYCODE,TS.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID  LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.STAFFID = #{staffId} /*AND TS.STATUS = 1*/")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATETIME", property = "createDate"),
            @Result(column = "JOBID", property = "jobid"),
            @Result(column = "OUTSIDEID", property = "outSideId"),
            @Result(column = "OUTSIDEOPENDID", property = "outSideOpenId"),
            @Result(column = "FIXEDPHONE", property = "fixedphone"),
            @Result(column = "RID", property = "trole.rid", id = true),
            @Result(column = "RNAME", property = "trole.rname"),
            @Result(column = "ORGID", property = "linkDetp.orgid", id = true),
            @Result(column = "ORGNAME", property = "linkDetp.orgname"),
            @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
            @Result(column = "HISTORYCODE", property = "historycode"),
            @Result(column = "SECRECTLEVELID", property = "secrectLevelId"),
            @Result(column = "SECRECTLEVELNAME", property = "secrectLevelName"),
    })
    TblStaff selectUniqueStaffInfoByStaffId(@Param("staffId") BigDecimal staffId) throws Exception;

    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.PKYMSTAFFID,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,TS.HISTORYCODE,TS.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID  LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.USERNAME = #{userName} /*and  TS.STATUS =1*/")
    @Results({
        @Result(column = "STAFFID", property = "staffid"),
        @Result(column = "REALNAME", property = "realname"),
        @Result(column = "USERNAME", property = "username"),
        @Result(column = "ADDRESS", property = "address"),
        @Result(column = "EMAIL", property = "email"),
        @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
        @Result(column = "MIBLEPHONE", property = "miblephone"),
        @Result(column = "MEMO", property = "memo"),
        @Result(column = "STATUS", property = "status"),
        @Result(column = "CREATETIME", property = "createDate"),
        @Result(column = "JOBID", property = "jobid"),
        @Result(column = "OUTSIDEID", property = "outSideId"),
        @Result(column = "OUTSIDEOPENDID", property = "outSideOpenId"),
        @Result(column = "FIXEDPHONE", property = "fixedphone"),
        @Result(column = "RID", property = "trole.rid", id = true),
        @Result(column = "RNAME", property = "trole.rname"),
        @Result(column = "ORGID", property = "linkDetp.orgid", id = true),
        @Result(column = "ORGNAME", property = "linkDetp.orgname"),
        @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
        @Result(column = "HISTORYCODE", property = "historycode"),
        @Result(column = "SECRECTLEVELID", property = "secrectLevelId"),
        @Result(column = "SECRECTLEVELNAME", property = "secrectLevelName"),
    })
    TblStaff selectUniqueStaffInfoByname(@Param("userName") String userName) throws Exception;
    
    //
    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.PKYMSTAFFID,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,TS.HISTORYCODE,TS.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_STAFF TS LEFT JOIN TBL_SECRECT_LEVEL TSL ON TS.SECRECTLEVELID = TSL.LEVELID  LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.MIBLEPHONE = #{miblephone}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATETIME", property = "createDate"),
            @Result(column = "JOBID", property = "jobid"),
            @Result(column = "OUTSIDEID", property = "outSideId"),
            @Result(column = "OUTSIDEOPENDID", property = "outSideOpenId"),
            @Result(column = "FIXEDPHONE", property = "fixedphone"),
            @Result(column = "RID", property = "trole.rid", id = true),
            @Result(column = "RNAME", property = "trole.rname"),
            @Result(column = "ORGID", property = "linkDetp.orgid", id = true),
            @Result(column = "ORGNAME", property = "linkDetp.orgname"),
            @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
            //@Result(column = "RNAMES", property = "roleNames"),,(SELECT WM_CONCAT(RNAME) FROM TBL_ROLE WHERE INSTR(','||TS.ROLEIDSTRS||',',','||RID||',')>0) RNAMES
            @Result(column = "HISTORYCODE", property = "historycode"),
            @Result(column = "SECRECTLEVELID", property = "secrectLevelId"),
            @Result(column = "SECRECTLEVELNAME", property = "secrectLevelName"),
    })
    TblStaff selectByMiblePhone(@Param("miblephone") String miblephone);

    @Select("select count(*) from TBL_STAFF where ORGID = #{ORGID} and STATUS = 1")
    boolean selectFindChildrenByOrgid(List<TblOrganization> findChildrenByOrgid);

    @Update("UPDATE TBL_STAFF SET PASSWORD = #{password} WHERE USERNAME =#{username}")
    void attachDirty(TblStaff username);

    @Select("SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = %s connect by prior ORGID = FATHERORGID)")
    List<TblStaff> selectDan();

    @Select("SELECT count(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 start with ORGID = %s connect by prior ORGID = FATHERORGID)")
    List<TblStaff> selectCountt();

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{pid}")
    TblStaff selectUserId(BigDecimal pid);

    @SelectProvider(method = "selectListByPageInfoo", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListByPageInfoo(IPage<TblStaff> page, Find find, BigDecimal orgid);

    @SelectProvider(method = "selectListByPageInfo", type = TblStaffMapperSqlConifg.class)
    List<UserService> selectListByPageInfo(PageInfo<UserService> pageInfo);

    @SelectProvider(method = "selectListByPageIn", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListByPageIn(IPage<TblStaff> page, BigDecimal pid, Find find);

    @SelectProvider(method = "selectAllStaffListByPageInfo", type = TblStaffMapperSqlConifg.class)
    List<TblStaff> selectAllStaffListByPageInfo(TblStaff staff) throws Exception;
    
    @Select("select * from TBL_STAFF where ORGID = #{orgid}")
    String selectFindOrgid(TblOrganization orgid);

    @Delete("DELETE FROM TBL_STAFF WHERE ORGID = #{orgid}")
    void deleteOrgid(TblOrganization orgid);

    @Select("SELECT STAFF.*,TOR.ORGNAME,ZS.REALNAME AS LEADERNAME,STAFF.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME FROM TBL_STAFF STAFF"
            + " LEFT JOIN TBL_ORGANIZATION TOR ON STAFF.ORGID = TOR.ORGID LEFT JOIN TBL_SECRECT_LEVEL TSL ON STAFF.SECRECTLEVELID = TSL.LEVELID "
            + " LEFT JOIN TBL_STAFF ZS ON STAFF.CHARGELEADERSTAFFID = ZS.STAFFID "
            + " WHERE STAFF.STAFFID = #{staffid}")
    TblStaff findByStaffid(BigDecimal staffid);


    @UpdateProvider(type = TblStaffMapperSqlConifg.class, method = "updateStaffByUsername")
    void updateStaffByUsername(TblStaff user);

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{userid}")
    TblStaff selectByUserId(String userid);

    @SelectProvider(method = "selectListByPage", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListByPage(IPage<TblStaff> page, String pid);

    @SelectProvider(method = "selectListBy", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListBy(IPage<TblStaff> page, BigDecimal orgid);

    @SelectProvider(method = "findPageListBySql", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> findPageListBySql(IPage<TblStaff> page,TblStaff staff, String sqlStrs);

    @SelectProvider(method = "findPageCountBySql", type = TblStaffMapperSqlConifg.class)
    Integer findPageCountBySql(PageInfo<TblStaff> pageInfo);

    @Select("SELECT * FROM TBL_STAFF where STAFFID = #{staffid}")
    TblStaff getExpert(BigDecimal staffid);

    @Select("select STA.STAFFID,ORA.ORGID,ORA.FATHERORGID,@ROW := @ROW + 1 AS SIGNED from(SELECT @ROW := 0) R,TBL_STAFF sta  INNER JOIN TBL_ORGANIZATION ora on STA.ORGID=ORA.ORGID  where  STA.STAFFID= #{cystaffid} HAVING SIGNED = 1")
    TblStaff findByOrag(String cystaffid);

    @Select("SELECT * FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{staffid}")
    List<TblManageRight> findMansgeUserRight(String staffid);

    @Select("SELECT * FROM TBL_STAFF WHERE JOBID = #{jobid}")
    List<TblStaff> findByJobid(BigDecimal jobid);

    @Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = #{staffid}")
    String selectYmPkStaffIdByStaffId(BigDecimal staffid) throws Exception;

    @Update("UPDATE TBL_STAFF SET PASSWORD =#{password} WHERE STAFFID = #{staffId}")
    void updateStaffPassWord(BigDecimal staffId, String password) throws Exception;

    @SelectProvider(method = "selectStaffListByPageInfo", type = TblStaffMapperSqlConifg.class)
    List<TblStaff> selectStaffListByPageInfo(PageInfo<TblStaff> pageInfo, @Param("orgId") BigDecimal orgId, TblStaff staff)
            throws Exception;

    @SelectProvider(method = "selectStaffCountByPageInfo", type = TblStaffMapperSqlConifg.class)
    Integer selectStaffCountByPageInfo(BigDecimal orgId, TblStaff staff) throws Exception;

    //@Insert("INSERT INTO TBL_TRAININGSTAFF_ATT(STAFFID,ATTID) VALUES (${id},${attid})")
    //void insertAttInfoForStaff(@Param("id")BigDecimal id, @Param("attid")String attid) throws Exception;


    @InsertProvider(method = "insertAttInfoForStaff", type = TblStaffMapperSqlConifg.class)
    void insertAttInfoForStaff(BigDecimal id, String attid) throws Exception;

    @InsertProvider(method = "insertAttInfoForTrain", type = TblStaffMapperSqlConifg.class)
    void insertAttInfoForTrain(BigDecimal id, String attid) throws Exception;

    @Delete("DELETE FROM TBL_TRAININGSTAFF_ATT WHERE ATTID = #{attid}")
    void deleteStaffFileInfoByAttId(@Param("attid") BigDecimal attid) throws Exception;


    @Delete("DELETE FROM tbl_training_att WHERE ATTID = #{attid}")
    void deleteTrainFileInfoByAttId(@Param("attid") BigDecimal attid) throws Exception;

    @SelectProvider(method = "getAuditorInformationList", type = TblStaffMapperSqlConifg.class)
    JSONObject getAuditorInformationList(String orgid) throws Exception;

    @Select("select * from tbl_staff where historycode=#{historycode}")
    List<TblStaff> getStffListByHistorycode(String historycode);


    @Update("update tbl_staff s set s.orgid=(select orgid from tbl_organization where historycode=s.historydepartmentid ) where s.datasource='zz' ")
    void updateTblStaffSyncOrg();


    @Update("update tbl_staff s set s.jobid=(select jobid from tbl_job where historycode=s.jobid ) where s.datasource='zz'")
    void updateTblStaffSyncJob();

    @Update("update tbl_staff s set s.jobgradeid=(select gradeid from TBL_JOB_GRADE where historycode=s.jobgradeid ),s.title=(select aliasname from TBL_JOB_GRADE where historycode=s.jobgradeid) where s.datasource='zz'")
    void updateTblStaffSyncJobGrade();

    //==
    @SelectProvider(method = "selectListOrgInId", type = TblStaffMapperSqlConifg.class)
    List<TblOrganization> selectListOrgInId(@Param("sysorgid") String sysorgid) throws Exception;

    @Select("select * from TBL_ROLE TR WHERE TR.COMPANYID = #{orgid} AND TR.RNAME = '部门负责人' ")
    List<TblRole> selectRoleByBmfzr(BigDecimal orgid);

    @SelectProvider(method = "findAllLeader", type = TblStaffMapperSqlConifg.class)
    List<TblStaff> findAllLeader(PageInfo<TblStaff> pageInfo, String orgid);

    @SelectProvider(method = "findAllLeaderCount", type = TblStaffMapperSqlConifg.class)
    Integer findAllLeaderCount(String orgid);

    @Select("SELECT DISTINCT TS.STAFFID,TS.REALNAME,TS.USERNAME,ORG.ORGNAME,TFK.NEXTROLE,TS.PKYMSTAFFID FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID LEFT JOIN TBL_FLOW_TASKINFO TFK ON TFK.NEXTSTAFFID = TS.STAFFID WHERE TFK.PROCESSID = #{processId} AND TFK.NEXTSTAFFID IS NOT NULL AND TFK.NEXTROLE != '提交人'")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "NEXTROLE", property = "jobName"),
            @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
    })
    List<TblStaff> selectCopyStaffList(String processId) throws Exception;

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID IN (${formStaffId})")
    List<TblStaff> selectListInfoByIds(@Param("formStaffId") String formStaffId) throws Exception;

    @Select("SELECT * FROM TBL_STAFF WHERE ORGID = #{orgid}")
	List<TblStaff> selectAllListByOrgid(TblOrganization orgid);
    @Select("SELECT RID from TBL_ROLE where rname = #{type} and COMPANYID =#{orgid}")
    BigDecimal findLikeUser(@org.apache.ibatis.annotations.Param("orgid") BigDecimal orgid, @org.apache.ibatis.annotations.Param("type") String type);

	/**
	 * 根据用户ID查找   用户所在的所有公司ID拼接的字符串  1,2,3
	 * @param staffid
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT WM_CONCAT(ORGID) FROM TBL_USER_ORGRELATION WHERE STAFFID = #{staffid}")
	String selectOrgIdStrsByStaffId(BigDecimal staffid) throws Exception;

	 @Update("UPDATE TBL_STAFF SET ROLEIDSTRS = #{roleids} WHERE STAFFID = #{id}")
	 void updateuserRole(String  id,String roleids);

	@SelectProvider(method = "selectStaffListByRoleId", type = TblStaffMapperSqlConifg.class)
	@Results({
		@Result(column="STAFFID",property="staffid"),
		@Result(column="REALNAME",property="realname"),
		@Result(column="USERNAME",property="username"),
		@Result(column="MIBLEPHONE",property="miblephone"),
		@Result(column="EMAIL",property="email"),
		@Result(column="FIXEDPHONE",property="fixedphone"),
		@Result(column="ROLEIDSTRS",property="roleIdStrs"),
		@Result(column="ORGID",property="dataSource"),
		@Result(column="ORGNAME",property="orgname"),
	})
	IPage<TblStaff> selectStaffListByRoleId(IPage<TblStaff> page, String userName, String realName,
			BigDecimal roleId,String deptName, String deptIds) throws Exception;

	@SelectProvider(method = "selectStaffCountByRoleId", type = TblStaffMapperSqlConifg.class)
	Integer selectStaffCountByRoleId(PageInfo<TblStaff> pageInfo, String userName, String realName, Integer roleId,
			String deptName, String deptIds) throws Exception;

	@SelectProvider(method = "selectStaffListToGrantRole", type = TblStaffMapperSqlConifg.class)
	@Results({
		@Result(column="STAFFID",property="staffid"),
		@Result(column="REALNAME",property="realname"),
		@Result(column="USERNAME",property="username"),
		@Result(column="MIBLEPHONE",property="miblephone"),
		@Result(column="EMAIL",property="email"),
		@Result(column="FIXEDPHONE",property="fixedphone"),
		@Result(column="ROLEIDSTRS",property="roleIdStrs"),
		@Result(column="ORGID",property="dataSource"),
		@Result(column="ORGNAME",property="orgname"),
		@Result(column="ISCHECKED",property="checked"),
	})
	IPage<TblStaff> selectStaffListToGrantRole(IPage<TblStaff> page, String userName, String realName,
			BigDecimal roleId, String deptName, String deptIds, Integer isAll) throws Exception;
	
	@SelectProvider(method = "selectStaffCountToGrantRole", type = TblStaffMapperSqlConifg.class)
	Integer selectStaffCountToGrantRole(PageInfo<TblStaff> pageInfo, String userName, String realName, Integer roleId,
			String deptName, String deptIds, Integer isAll) throws Exception;

	@SelectProvider(method = "selectListByThemeHouse", type = TblStaffMapperSqlConifg.class)
	IPage<TblStaff> selectListByThemeHouse(IPage<TblStaff> page,TblStaff staff) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_STAFF WHERE STAFFID NOT IN( SELECT STAFFID FROM  TBL_USER_ORGRELATION  )")
	Integer selectAllCount() throws Exception;

	@SelectProvider(type=TblStaffMapperSqlConifg.class,method="findByStaffManOrgs")
	public TblStaff findByStaffManOrgs(String orgid);

	@Insert("INSERT INTO TBL_WX_POPULARIZE_STAFF_ORGID(STAFFID,ORGID) VALUES(#{staffId},#{orgId})")
	void insertPupularizeStaffOrgRelation(@Param("staffId")Integer sid,@Param("orgId") BigDecimal oid) throws Exception;

    //新方法
    
    @Select("SELECT DEPTIDSTRS FROM TBL_SYSTEM_DATA_RIGHT WHERE ORGID = #{orgid} AND ROLEID IN (${roleIdStrs})")
 	List<String> selectDateDeptRelation(@Param("roleIdStrs")String roleIdStrs,@Param("orgid") BigDecimal orgid) throws Exception;
    
    @Update("UPDATE TBL_STAFF SET ORGID = #{deptId} WHERE STAFFID = #{staffid}")
	void updateDeptIdByStaffId(BigDecimal staffid, BigDecimal deptId) throws Exception;
    
    @Select("select S.*,O.ORGNAME from TBL_STAFF S LEFT JOIN TBL_ORGANIZATION O ON S.ORGID = O.ORGID WHERE S.STAFFID = #{id}")
    TblStaff findById(BigDecimal id);
    
    @SelectProvider(method = "findByAll", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> findByAll(String pid, IPage<TblStaff> page);
    
    @SelectProvider(method = "findByAllORGID", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> findByAllORGID(BigDecimal orgid, IPage<TblStaff> page);
    
    @SelectProvider(method = "selectListByPageInfoFind", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListByPageInfoFind(IPage<TblStaff> page, Find find);
    
    @SelectProvider(method = "selectListByPageInfoOrgid", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> selectListByPageInfoOrgid(IPage<TblStaff> page, String orgIdStrs, Find find);
    
    @SelectProvider(method = "selectListBmfzrByPageInfo", type = TblStaffMapperSqlConifg.class)
    @Results({
            @Result(column = "ORGNAME", property = "orgname"),
    })
    IPage<TblStaff> selectListBmfzrByPageInfo(IPage<TblStaff> page, BigDecimal roleid);
    
    @SelectProvider(method = "selectListFgldByPageInfo", type = TblStaffMapperSqlConifg.class)
    @Results({
            @Result(column = "ORGNAME", property = "orgname"),
    })
    IPage<TblStaff> selectListFgldByPageInfo(IPage<TblStaff> page, BigDecimal gongsiId);
    
    @Select("select * from TBL_STAFF WHERE USERNAME = #{username}")
    TblStaff selectUserName(String username);
    
    @Select("select * from TBL_STAFF WHERE EMAIL = #{email}")
    List<TblStaff> selectEmail(String email);
    
    @SelectProvider(method = "selectUniqueCountUserName", type = TblStaffMapperSqlConifg.class)
    Integer selectUniqueCountUserName(String username, BigDecimal staffid) throws Exception;

    @SelectProvider(method = "selectUniqueCountFgld", type = TblStaffMapperSqlConifg.class)
    Integer selectUniqueCountFgld(String orgId, BigDecimal staffid) throws Exception;

    @SelectProvider(method = "selectUniqueCountBmfzr", type = TblStaffMapperSqlConifg.class)
    Integer selectUniqueCountBmfzr(String orgId, BigDecimal staffid) throws Exception;
    
    @UpdateProvider(type = TblStaffMapperSqlConifg.class, method = "updateStaff")
    void updateStaff(TblStaff user);
    
    @InsertProvider(method = "insertUser", type = TblStaffMapperSqlConifg.class)
    @Options(useGeneratedKeys = true, keyProperty = "staffid", keyColumn = "STAFFID")
    void insertUser(TblStaff user);
    
    @Results({
        @Result(column = "STAFFID", property = "staffid"),
        @Result(column = "USERNAME", property = "username"),
        @Result(column = "EMAIL", property = "email"),
        @Result(column = "MIBLEPHONE", property = "miblephone"),
        @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
        @Result(column = "REALNAME", property = "realname"),
        @Result(column = "PKYMORGID", property = "orgname"),
        @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
        @Result(column = "PASSWORD", property = "password"),
        @Result(column = "PKYMJOBID", property = "jobName"),
        @Result(column = "ORGID", property = "orgid"),
	})
	@SelectProvider(method = "selectAllYmStaffInfo", type = TblStaffMapperSqlConifg.class)
	TblStaff selectAllYmStaffInfo(BigDecimal staffId) throws Exception;
    
    @SelectProvider(method = "selectListRoleInId", type = TblStaffMapperSqlConifg.class)
    List<TblRole> selectListRoleInId(@Param("roleids") String roleids) throws Exception;
    
    @Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = #{staffId} ")
    String selectbmfzrByStaffId(BigDecimal staffId);
    
    @Update("UPDATE TBL_STAFF SET PKYMSTAFFID = #{pkYmStaffId} WHERE STAFFID = #{staffid}")
    void updatePkYmStaffIdByStaffId(String pkYmStaffId, BigDecimal staffid);
    
    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.ADDRESS,TS.EMAIL,TS.MIBLEPHONE,TS.MEMO,TS.USERNAME,TS.PASSWORD,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.ROLEIDSTRS,TOR.ORGID,TOR.ORGNAME FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.STAFFID = #{id}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "PASSWORD", property = "password"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "CREATETIME", property = "createDate"),
            @Result(column = "JOBID", property = "jobid"),
            @Result(column = "ROLEIDSTRS", property = "roleIdStrs"),
            @Result(column = "ORGNAME", property = "orgname"),
    })
    TblStaff selectPid(String id);
    
    @Update("UPDATE TBL_STAFF SET PASSWORD =#{password} WHERE STAFFID = #{staffid}")
    void updateUser(String password, BigDecimal staffid);
    
    @SelectProvider(method = "selectYmPkStaffIdByRoleId", type = TblStaffMapperSqlConifg.class)
	List<String> selectYmPkStaffIdByRoleId(String roleId);
    
    @SelectProvider(method = "selectAllListByroleid", type = TblStaffMapperSqlConifg.class)
	@Results({
		@Result(column="STAFFID",property="staffid"),
		@Result(column="REALNAME",property="realname"),
		@Result(column="USERNAME",property="username"),
		@Result(column="MIBLEPHONE",property="miblephone"),
		@Result(column="EMAIL",property="email"),
		@Result(column="FIXEDPHONE",property="fixedphone"),
		@Result(column="ROLEIDSTRS",property="roleIdStrs"),
		@Result(column="ORGID",property="dataSource"),
		@Result(column="ORGNAME",property="orgname"),
	})
	IPage<TblStaff> selectAllListByroleid(String  roleid,String username,String realname,IPage<TblStaff> page);
    
    @SelectProvider(method = "findAllPageInfoByacctid", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> findAllPageInfoByacctid(IPage<TblStaff> page, String bookid);
    
    @SelectProvider(method = "findAllPageBeanPid", type = TblStaffMapperSqlConifg.class)
    IPage<TblStaff> findAllPageBeanPid(IPage<TblStaff> page, TblOrganization attribute);
    
    @SelectProvider(method = "findUserInfoExam", type = TblStaffMapperSqlConifg.class)
    StaffResult findUserInfoExam(BigDecimal staffId, BigDecimal rid);
    
	@SelectProvider(method = "selectWxappAdmin", type = TblStaffMapperSqlConifg.class)
	TblStaff selectWxappAdmin(String orgIds) throws Exception;
	
	@Select("SELECT * FROM TBL_STAFF WHERE STAFFID NOT IN( SELECT STAFFID FROM  TBL_USER_ORGRELATION  )")
	IPage<TblStaff> selectListByPageInfoAll(IPage<TblStaff> page);
	

    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "MIBLEPHONE", property = "miblephone"),
            @Result(column = "PKYMSTAFFID", property = "pkYmStaffId"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "PKYMORGID", property = "orgname"),
            @Result(column = "ROLEIDS", property = "roleNames"),
            @Result(column = "PASSWORD", property = "password"),
    })
    @SelectProvider(method = "selectAllYmStaffList", type = TblStaffMapperSqlConifg.class)
    List<TblStaff> selectAllYmStaffList(BigDecimal orgId) throws Exception;
    
    @Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = (SELECT PRINCIPALSTAFFID FROM TBL_ORGANIZATION WHERE ORGID = #{orgid} )")
    String selectDeptManagerId(@Param("orgid")BigDecimal orgid) throws Exception;
    
    @SelectProvider(method = "selectbmfzrByOrgId", type = TblStaffMapperSqlConifg.class)
    String selectbmfzrByOrgId(String orgid) throws Exception;

    @SelectProvider(method = "findAllLeader2", type = TblStaffMapperSqlConifg.class)
	IPage<TblStaff> findAllLeader2(IPage<TblStaff> page, String orgids);

    @Select("SELECT STAFFID FROM TBL_STAFF WHERE USERNAME = #{userName}")
	BigDecimal selectStaffIdByUserName(@Param("userName")String userName) throws Exception;

    @SelectProvider(method = "selectExprotList", type = TblStaffMapperSqlConifg.class)
	List<TblStaff> selectExprotList(TblStaff staff) throws Exception;

    @Select("SELECT * FROM TBL_STAFF WHERE PKYMSTAFFID = #{handleId}")
	TblStaff selectStaffInfoByYmStaffId(@Param("handleId")String handleId) throws Exception;

    @Select("SELECT * FROM TBL_STAFF WHERE PKYMSTAFFID IN (${copyYmIds})")
	List<TblStaff> selectCopyStaffListByYmId(@Param("copyYmIds")String copyYmIds) throws Exception;
    
    
    
    @SelectProvider(method = "selectStaffListByPageInfocs", type = TblStaffMapperSqlConifg.class)
    List<TblStaff> selectStaffListByPageInfocs(PageInfo<TblStaff> pageInfo,String orgId, TblStaff staff)
            throws Exception;
    
    
    @Select("SELECT COUNT(0) FROM TBL_STAFF WHERE USERNAME = #{username}")
	Integer selectCntByUsername(@Param("username")String username) throws Exception;

    @Update("UPDATE TBL_STAFF SET ORGID =#{orgid},REALNAME=#{realname} WHERE USERNAME = #{username}")
    void updateUserOrgByUsername(String username, BigDecimal orgid,String realname);

    @Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID IN (${staffids})")
	List<String> selectYmPkStaffIdByStaffIds(@Param("staffids")String staffids);

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID = (SELECT USERID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{formId} AND FLOWID = #{flowId} AND YMFORMID = #{id})")
	TblStaff selectSubmitStaffByFormIdTaskId(@Param("formId")BigDecimal formId,@Param("id") String id,@Param("flowId") String flowId) throws Exception;

	@SelectProvider(type=TblStaffMapperSqlConifg.class,method="findByRoleName")
	public TblStaff findByRoleName(String roleId);

	@Select("SELECT * FROM TBL_ROLE WHERE RNAME =#{roleName} and companyid=#{companyid} AND ROWNUM = 1")
	TblRole findRoleByName(String roleName,String companyid);

	@Select("SELECT * FROM TBL_STAFF WHERE REALNAME =#{realname}")
	TblStaff findByRealname(String realname);

	@Select("SELECT * FROM TBL_STAFF WHERE USERNAME =#{username}")
	TblStaff findByUsername(String username);

	@Select("SELECT * from TBL_STAFF WHERE STAFFID= #{staffid} ")
	TblStaff getById(String staffid);

	@Select("SELECT PK_ACCBOOKINFO,PK_SETOFBOOK,CONVERT_DATE,LOCALORIGINVALUE,BODYVOS,BOOKNAME,PK_FINANPLANID,ACCBOOKTYPECODE,ACCBOOKTYPENAME FROM FA_ACCBOOKINFO WHERE PK_ACCBOOKINFO = (SELECT ACCBOOKID FROM FA_ACCBOOK_USER WHERE STAFFID = #{staffid})")
	@Results({
		@Result(column = "PK_ACCBOOKINFO",property = "pkAccbookinfo"),
		@Result(column = "PK_SETOFBOOK",property = "pkSetofbook"),
		@Result(column = "CONVERT_DATE",property = "convertDate"),
		@Result(column = "LOCALORIGINVALUE",property = "localoriginvalue"),
		@Result(column = "BODYVOS",property = "bodyvos"),
		@Result(column = "BOOKNAME",property = "bookName"),
		@Result(column = "PK_FINANPLANID",property = "pkFinanplanid"),
		@Result(column = "ACCBOOKTYPECODE",property = "accbooktypecode"),
		@Result(column = "ACCBOOKTYPENAME",property = "accbooktypename")
	})
	FaAccbookinfoUtil selectUserBookInfo(@Param("staffid")BigDecimal staffid) throws Exception;


    @Select("select ts.staffid,username,realname,ts.orgid " +
            "from tbl_staff ts left join tbl_user_orgrelation tuo on ts.staffid=tuo.staffid " +
            "where tuo.staffid is null AND ts.ORGID IS NOT NULL")
    List<TblStaff> selectStaffNotInOrgRelation();

    @SelectProvider(method = "selectFlowHxrBySecrect", type = TblStaffMapperSqlConifg.class)
	List<String> selectFlowHxrBySecrect(List<String> pkYmStaffIdList, String secrectId, String scopeStaffIds) throws Exception;

    @Select("SELECT REALNAME FROM TBL_STAFF WHERE STAFFID IN (${staffids})")
	List<String> selectRealNameListByStaffIds(@Param("staffids")String staffids) throws Exception;


    //中核主人员信息主数据同步所需查询
    @Select("SELECT ORGID,ORGNUMBER FROM TBL_ORGANIZATION WHERE ORGID IS NOT NULL AND ORGNUMBER IS NOT NULL")
    List<TblOrganization> selectOrgList() throws Exception;

    @Select("SELECT STAFFID,USERNAME FROM TBL_STAFF WHERE STAFFID IS NOT NULL AND USERNAME IS NOT NULL")
    List<TblStaff> selectStaffList() throws Exception;

}
