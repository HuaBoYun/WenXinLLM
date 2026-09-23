package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblRole;
import com.huabo.compliance.mapper.TblStaffMapperSqlConifg;
import com.huabo.compliance.mysql.entity.FindMySql;
import com.huabo.compliance.mysql.entity.TblManageRightMySql;
import com.huabo.compliance.mysql.entity.TblOrganizationMySql;
import com.huabo.compliance.mysql.entity.TblStaffMySql;

import com.huabo.compliance.service.UserService;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblStaffMySqlMapper extends BaseMapper<TblStaffMySql> {

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE STAFFID = #{staffid}")
	String selectYmPkStaffIdByStaffId(BigDecimal staffid) throws Exception;

	@Results({
		@Result(column="STAFFID",property="staffid"),
		@Result(column="USERNAME",property="username"),
		@Result(column="EMAIL",property="email"),
		@Result(column="MIBLEPHONE",property="miblephone"),
		@Result(column="PKYMSTAFFID",property="pkYmStaffId"),
		@Result(column="REALNAME",property="realname"),
		@Result(column="PKYMORGID",property="orgname"),
		@Result(column="ROLEIDS",property="roleNames"),
		@Result(column="PASSWORD",property="password"),
	})
	@SelectProvider(method = "selectAllYmStaffList", type = TblStaffMapperSqlMySqlConifg.class)
	List<TblStaffMySql> selectAllYmStaffList(String orgIds) throws Exception;

	@Update("UPDATE TBL_STAFF SET PKYMSTAFFID =#{pkYmStaffId} WHERE STAFFID = #{staffid}")
	void updatePkYmStaffIdByStaffId(String pkYmStaffId, BigDecimal staffid);
	
    @Select("SELECT STAFFID,REALNAME,ORGID,USERNAME FROM TBL_STAFF WHERE STAFFID = #{staffId}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "USERNAME", property = "username"),
    })
    TblStaffMySql selectStaff(@Param("staffId") String staffId) throws Exception;

    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.MIBLEPHONE,TS.PKYMSTAFFID,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,(SELECT GROUP_CONCAT(RNAME) FROM TBL_ROLE WHERE INSTR(TS.ROLEIDSTRS,RID)>0) RNAMES FROM TBL_STAFF TS LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.USERNAME = #{userName} AND TS.PASSWORD = #{password}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
            @Result(column = "PKYMSTAFFID",property="pkYmStaffId"),
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
            @Result(column = "RNAMES", property = "roleNames"),
    })
    TblStaffMySql selectUniqueStaffInfo(@Param("userName") String userName, @Param("password") String password) throws Exception;

    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.USERNAME,TS.ADDRESS,TS.MIBLEPHONE,TS.MEMO,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.OUTSIDEID,TS.OUTSIDEOPENDID,TS.EMAIL,TS.FIXEDPHONE,TR.RID,TR.RNAME,ORG.ORGID,ORG.ORGNAME,TS.ROLEIDSTRS,(SELECT GROUP_CONCAT(RNAME) FROM TBL_ROLE WHERE INSTR(TS.ROLEIDSTRS,RID)>0) RNAMES FROM TBL_STAFF TS LEFT JOIN TBL_ROLE TR ON TS.ROLEID = TR.RID LEFT JOIN TBL_ORGANIZATION ORG ON TS.ORGID = ORG.ORGID WHERE TS.USERNAME = #{userName}")
    @Results({
            @Result(column = "STAFFID", property = "staffid"),
            @Result(column = "REALNAME", property = "realname"),
            @Result(column = "USERNAME", property = "username"),
            @Result(column = "ADDRESS", property = "address"),
            @Result(column = "EMAIL", property = "email"),
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
            @Result(column = "RNAMES", property = "roleNames"),
    })
    TblStaffMySql selectUniqueStaffInfoByname(@Param("userName") String userName) throws Exception;

    @Select("select count(*) from TBL_STAFF where ORGID = #{ORGID} and STATUS = 1")
    boolean selectFindChildrenByOrgid(List<TblOrganizationMySql> findChildrenByOrgid);


    @Select("select S.*,O.ORGNAME from TBL_STAFF S LEFT JOIN TBL_ORGANIZATION O ON S.ORGID = O.ORGID WHERE S.STAFFID = #{id}")
    TblStaffMySql findById(BigDecimal id);

    @Update("UPDATE TBL_STAFF SET PASSWORD = #{password} WHERE USERNAME =#{username}")
    void attachDirty(TblStaffMySql username);

    @Select("SELECT s.STAFFID,s.REALNAME,o.ORGNAME FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 and getOrgIdList(ORGID = %s))")
    List<TblStaffMySql> selectDan();

    @Select("SELECT count(*) FROM TBL_STAFF s LEFT JOIN TBL_ORGANIZATION o ON s.ORGID = o.ORGID WHERE s.STAFFID IN (select STAFFID from TBL_MANAGE_USER_BOOK WHERE BOOKID IN (%s)) AND s.ORGID IN (SELECT ORGID FROM TBL_ORGANIZATION where ORGTYPE = 0 and getOrgIdList(ORGID = %s))")
    List<TblStaffMySql> selectCountt();

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID =#{pid}")
    TblStaffMySql selectUserId(BigDecimal pid);

    @SelectProvider(method = "selectListByPageInfoo", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListByPageInfoo(PageInfo<TblStaffMySql> pageInfo, FindMySql find, BigDecimal orgid);

    @SelectProvider(method = "selectListByPageInfo", type = TblStaffMapperSqlMySqlConifg.class)
    List<UserService> selectListByPageInfo(PageInfo<UserService> pageInfo);

    @SelectProvider(method = "selectListByPageIn", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListByPageIn(PageInfo<TblStaffMySql> pageInfo, BigDecimal pid, FindMySql find);

    @SelectProvider(method = "selectListByPageInfoOrgid", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListByPageInfoOrgid(PageInfo<TblStaffMySql> pageInfo, BigDecimal pid, Find find);


    @Select("select * from TBL_STAFF WHERE USERNAME = #{username}")
    TblStaffMySql selectUserName(String username);

    //	@Insert("INSERT INTO TBL_STAFF (REALNAME,STATUS,FIXEDPHONE,MIBLEPHONE,ADDRESS,EMAIL,MEMO,USERNAME,PASSWORD) " +
//			"VALUES(#{realname},#{status},#{fixedphone},#{miblephone},#{address},#{email},#{memo},#{username},#{password})")
    @InsertProvider(method = "insertUser", type = TblStaffMapperSqlMySqlConifg.class)
    @Options(useGeneratedKeys = true, keyProperty = "staffid", keyColumn = "STAFFID")
    void insertUser(TblStaffMySql user);

    @Select("select * from TBL_STAFF WHERE EMAIL = #{email}")
    List<TblStaffMySql> selectEmail(String email);

    @Update("UPDATE TBL_STAFF SET PASSWORD =#{password},PASSWORD = #{possword1} WHERE STAFFID = #{pid}")
        //@UpdateProvider(type=TblStaffMapperSqlConifg.class,method="updateUser")
    void updateUser(TblStaffMySql user);

    @Select("SELECT TS.STAFFID,TS.REALNAME,TS.ADDRESS,TS.EMAIL,TS.MIBLEPHONE,TS.MEMO,TS.USERNAME,TS.PASSWORD,TS.STATUS,TS.CREATETIME,TS.JOBID,TS.ROLEIDSTRS,(SELECT GROUP_CONCAT(RNAME) FROM TBL_ROLE WHERE INSTR(TS.ROLEIDSTRS,RID)>0) RNAMES,TOR.ORGID,TOR.ORGNAME FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.STAFFID = #{id}")
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
            @Result(column = "RNAMES", property = "roleNames"),
            @Result(column = "ORGNAME", property = "orgname"),
    })
    TblStaffMySql selectPid(String id);


    //	@SelectProvider(method = "selectCountFind",type =TblStaffMapperSqlConifg.class )
    @Select("select count(*) from tbl_staff where orgid= #{orgid} order by STATUS desc,STAFFID desc")
    Integer selectCountFind(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid);


    //	@SelectProvider(method = "selectListByPageInfoCount",type =TblStaffMapperSqlConifg.class )
    @Select("select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID =#{pid}")
    Integer selectListByPageInfoCount(BigDecimal pid);

    //	@SelectProvider(method = "selectListByPageInfoCountOrgid",type = TblStaffMapperSqlConifg.class)
    @Select("select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TOR.FATHERORGID =  #{orgid} AND TOR.ORGTYPE = 0 ")
    Integer selectListByPageInfoCountOrgid(BigDecimal orgid);

    //	@SelectProvider(method = "selectListByPageInCount",type = TblStaffMapperSqlConifg.class)
    @Select("select COUNT(*) from tbl_staff where ORGID in (select ORGID from TBL_ORGANIZATION org where FATHERORGID= #{pid} AND ORGTYPE=0 )")
    Integer selectListByPageInCount(PageInfo<TblStaffMySql> pageInfo, BigDecimal pid);

    @SelectProvider(method = "selectListByPageInfoFind", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListByPageInfoFind(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid, Find find);

    @Select("select * from TBL_STAFF where ORGID = #{orgid}")
    String selectFindOrgid(TblOrganizationMySql orgid);

    @Delete("DELETE FROM TBL_STAFF WHERE ORGID = #{orgid}")
    void deleteOrgid(TblOrganizationMySql orgid);

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{staffid}")
    TblStaffMySql findByStaffid(BigDecimal staffid);

    @UpdateProvider(type = TblStaffMapperSqlMySqlConifg.class, method = "updateStaff")
    void updateStaff(TblStaffMySql user);

    @Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{userid}")
    TblStaffMySql selectByUserId(String userid);

    @SelectProvider(method = "selectListByPage", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListByPage(PageInfo<TblStaffMySql> pageInfo, String pid);

    @SelectProvider(method = "selectListByPageCount", type = TblStaffMapperSqlMySqlConifg.class)
    Integer selectListByPageCount(PageInfo<TblStaffMySql> pageInfo, String pid);

    @SelectProvider(method = "selectListBy", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> selectListBy(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid);

    @SelectProvider(method = "selectListByOrgid", type = TblStaffMapperSqlMySqlConifg.class)
    Integer selectListByOrgid(PageInfo<TblStaffMySql> pageInfo, BigDecimal orgid);

    @SelectProvider(method = "findPageListBySql", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> findPageListBySql(PageInfo<TblStaffMySql> pageInfo);

    @SelectProvider(method = "findPageCountBySql", type = TblStaffMapperSqlMySqlConifg.class)
    Integer findPageCountBySql(PageInfo<TblStaffMySql> pageInfo);

    @Select("SELECT * FROM TBL_STAFF where STAFFID = #{staffid}")
    TblStaffMySql getExpert(BigDecimal staffid);

    @SelectProvider(method = "findAllPageInfoByacctid", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> findAllPageInfoByacctid(PageInfo<TblStaffMySql> pageInfo, String bookid);

    @SelectProvider(method = "findAllPageInfoByacctidcCount", type = TblStaffMapperSqlMySqlConifg.class)
    Integer findAllPageInfoByacctidcCount(String bookid);

    @SelectProvider(method = "findByAll", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> findByAll(String pid, PageInfo<TblStaffMySql> pageInfo);

    @Select(" select COUNT(*) from TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID WHERE TS.ORGID = #{pid} AND (TS.STATUS is NULL or TS.STATUS != 0)")
    Integer findByAllCount(String pid);

    @SelectProvider(method = "findByAllORGID", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> findByAllORGID(BigDecimal orgid, PageInfo<TblStaffMySql> pageInfo);

    @Select("SELECT COUNT(*) FROM TBL_STAFF TS LEFT JOIN TBL_ORGANIZATION TOR ON TS.ORGID = TOR.ORGID " +
            "WHERE TOR.FATHERORGID = #{orgid} AND (TS.STATUS IS NULL OR TS.STATUS != 0)")
    Integer findByAllORGIDCount(BigDecimal orgid);

    @SelectProvider(method = "findAllPageBeanPid", type = TblStaffMapperSqlMySqlConifg.class)
    List<TblStaffMySql> findAllPageBeanPid(PageInfo<TblStaffMySql> pageInfo, TblOrganizationMySql attribute);

    @SelectProvider(method = "findCountPageBeanPid", type = TblStaffMapperSqlMySqlConifg.class)
    Integer findCountPageBeanPid(PageInfo<TblStaffMySql> pageInfo, TblOrganizationMySql attribute);

    @Select("select STA.STAFFID,ORA.ORGID,ORA.FATHERORGID ,@ROW := @ROW + 1 AS SIGNED from(SELECT @ROW := 0) R, TBL_STAFF sta  INNER JOIN TBL_ORGANIZATION ora on STA.ORGID=ORA.ORGID  where  STA.STAFFID= #{cystaffid} having signed = 1")
    TblStaffMySql findByOrag(String cystaffid);

    @Select("SELECT * FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{staffid}")
    List<TblManageRightMySql> findMansgeUserRight(String staffid);

    @Select("SELECT * FROM TBL_STAFF WHERE JOBID = #{jobid}")
    List<TblStaffMySql> findByJobid(BigDecimal jobid);

//	@SelectProvider(method = "selectTblExternal",type = TblStaffMapperSqlMySqlConifg.class)
//	List<TblExternalExpert> selectTblExternal(PageInfo<TblExternalExpert> pageInfo, FindMySql find);


//	void insertTblStaff(TblStaffMySql tblStaff);


//	@Select("SELECT * FROM TBL_STAFF WHERE STAFFID = #{userid}")
//	TblStaffMySql selectStaffId(BigDecimal userid);

    @Update("UPDATE TBL_STAFF SET PASSWORD =#{password} WHERE STAFFID = #{staffId}")
    void updateStaffPassWord(Integer staffId, String password) throws Exception;

    @Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE CONCAT(',',CONCAT(FGORGS,','))  LIKE '%,${orgid},%' LIMIT 0,1")
	String selectDeptManagerId(@Param("orgid") BigDecimal orgid) throws Exception;

    @SelectProvider(method="selectListRoleInId",type= TblStaffMapperSqlConifg.class)
	List<TblRole> selectListRoleInId(String roleIdStrs) throws Exception;

	@Results({
		@Result(column="STAFFID",property="staffid"),
		@Result(column="USERNAME",property="username"),
		@Result(column="EMAIL",property="email"),
		@Result(column="MIBLEPHONE",property="miblephone"),
		@Result(column="PKYMSTAFFID",property="pkYmStaffId"),
		@Result(column="REALNAME",property="realname"),
		@Result(column="PKYMORGID",property="orgname"),
		@Result(column="ROLEIDSTRS",property="roleIdStrs"),
		@Result(column="PASSWORD",property="password"),
		@Result(column="PKYMJOBID",property="jobName"),
		@Result(column="ORGID",property="orgid"),
	})
	@SelectProvider(method = "selectAllYmStaffInfo",type = TblStaffMapperSqlConifg.class)
	TblStaffMySql selectAllYmStaffInfo(BigDecimal staffId) throws Exception;

	@Select("SELECT PKYMSTAFFID FROM TBL_STAFF WHERE ','||MANAGEORGS||',' LIKE '%,${orgid},%' AND ROWNUM = 1")
	String selectbmfzrByOrgId(BigDecimal orgid) throws Exception;
	
}
