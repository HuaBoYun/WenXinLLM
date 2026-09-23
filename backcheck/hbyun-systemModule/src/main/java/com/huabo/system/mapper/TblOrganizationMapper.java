package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
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
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOrganizationInfo;
import com.huabo.system.mappersql.TblOrganizationMapperSqlConifg;
import com.huabo.system.utils.Tree;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Mapper
public interface TblOrganizationMapper extends BaseMapper<TblOrganization> {

	/**
	 * 传入部门Id 查找出当前部门所隶属的公司
	 //* @param deptid
	 * @return
	 * @throws Exception
	 */
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 AND ROWNUM = 1 START WITH o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")
	@Results({
		@Result(column="ORGID",property="orgid"),
		@Result(column="ORGNAME",property="orgname"),
		@Result(column="FATHERORGID",property="fatherorgid"),
		@Result(column="ORGNUMBER",property="orgnumber"),
		@Result(column="ORGMENO",property="orgmeno"),
		@Result(column="MEMO",property="memo"),
		@Result(column="ICODE",property="icode"),
		@Result(column="ORGTYPE",property="orgtype"),
		@Result(column="STATUS",property="status"),
		@Result(column="ISZY",property="iszy"),
	})
	TblOrganization selectCompanyInfoByDeptId(@Param("orgid") BigDecimal orgid);

	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")
	Object excuteFunReturnUnique(String orgid);

	@Select("SELECT * from TBL_ORGANIZATION where  ORGNAME='行业' and ORGTYPE=100 ORDER BY orderid ASC")
	List<TblOrganization> selectGetHY();

	@Select("SELECT * from TBL_ORGANIZATION WHERE ORGNAME=#{orgname}  and ORGTYPE>=100  ")
	List<TblOrganization> findBysql(String orgname);

	@Select("select * from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orgid desc")
	String selectORGTYPE(Integer orgtype);

	@Select("select count(*) from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orgid desc")
	String selectORGTYPECount(Integer orgtype);

	@Select("SELECT ORGNAME,ORGNUMBER,ORGID FROM TBL_ORGANIZATION WHERE 1 = 1 AND ORGTYPE != 0 START WITH FATHERORGID = #{pid} CONNECT BY PRIOR orgid = #{fatherorgid} ORDER BY ORGID, orderid ASC")
    List<TblOrganization> selectPid(String pid);

	@Select("select ORGNAME,ORGNUMBER,ORGID from TBL_ORGANIZATION where 1=1 start with  FATHERORGID= #{pid} and ORGTYPE=0 connect by prior orgid= #{fatherorgid} ORDER BY orderid ASC")
	List<TblOrganization> selectOrganme(String pid);

	@SelectProvider(method="selectStaffInfoByModuleIdList",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> selectStaffInfoByModuleIdList(IPage<TblOrganization> page, BigDecimal moduleId, TblOrganization tblOrganization);

//	@Select("SELECT * FROM TBL_ORGANIZATION")
	@SelectProvider(method = "selectListByPageInfoo",type = TblOrganizationMapperSqlConifg.class)
    List<TblOrganization> selectListByPageInfoo(PageInfo<TblOrganization> pageInfo);

	@SelectProvider(method = "findAllCommpanyPageBeanGSXj",type = TblOrganizationMapperSqlConifg.class)
	@Results({
		@Result(column="ORGID",property="orgid"),
		@Result(column="ORGNAME",property="orgname"),
		@Result(column="ORGNUMBER",property="orgnumber"),
		@Result(column="ORGMENO",property="orgmeno"),
		@Result(column="MEMO",property="memo"),
		@Result(column="ORGTYPE",property="orgtype"),
		@Result(column="STATUS",property="status"),
		@Result(column="ISZY",property="iszy"),
	})
	List<TblOrganization> findAllCommpanyPageBeanGSXj(PageInfo<TblOrganization> pageInfo, Find find, String orgId);

	@Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = #{orgId} CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID")
	Integer findAllCommpanyPageBeanGS(PageInfo<TblOrganization> pageInfo, Find find, String orgId);

	@SelectProvider(method = "findAllCommpanyPageBeanStaffid",type = TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> findAllCommpanyPageBeanStaffid(IPage<TblOrganization> page, String orgIdStrs, String orgname, String orgnumber);

	@Update("UPDATE TBL_ORGANIZATION SET (ORGID,FATHERORGID,ORGNAME,ORGNUMBER,MEMO,ORGMENO,ORGTYPE) VALUES(#{orgid},#{fatherorgid},#{orgname},#{orgnumber},#{memo},#{orgmeno},#{orgtype}")
	void saveOrg(TblOrganization org);

	@Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    int deleteOrgid(TblOrganization orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{pid}")
    TblOrganization findIdOrgid(String pid);

	@Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	void deleteOrg(BigDecimal orgid);


	@SelectProvider(method="findByPageBean",type=TblOrganizationMapperSqlConifg.class)
	@Results({
		@Result(column="ORGID",property="orgid"),
		@Result(column="ORGNAME",property="orgname"),
		@Result(column="ORGNUMBER",property="orgnumber"),
		@Result(column="ORGMENO",property="orgmeno"),
		@Result(column="MEMO",property="memo"),
		@Result(column="ORGTYPE",property="orgtype"),
		@Result(column="STATUS",property="status"),
		@Result(column="ISZY",property="iszy"),
		@Result(column="JTORGID",property="jtorgid"),
		@Result(column="JTORGNAME",property="jtorgname"),
		@Result(column="BGIMAGE",property="bgimage"),
		@Result(column="BGNAME",property="bgname"),
		@Result(column="LOGOIMAGE",property="logoimage"),
		@Result(column="LOGONAME",property="logoname"),
		@Result(column="JDZTIMAGE",property="jdztimage"),
		@Result(column="JDZTNAME",property="jdztname"),
		@Result(column="CTZTIMAGE",property="ctztimage"),
		@Result(column="CTZTNAME",property="ctztname"),
		@Result(column="BANAME",property="baname"),
		@Result(column = "ISAUTONUMBER",property = "isautonumber")
	})
	IPage<TblOrganization> findByPageBean(IPage<TblOrganization> page, String orgIdStrs,Find find);

	@SelectProvider(method="getNodesa",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> getNodesa(IPage<TblOrganization> page);

	@SelectProvider(method="getNodesaNodeId",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> getNodesaNodeId(IPage<TblOrganization> page, BigDecimal nodeId);

	@UpdateProvider(method = "saveModiOrganization",type = TblOrganizationMapperSqlConifg.class)
	void saveModiOrganization(TblOrganization organization);

	@InsertProvider(method = "addReturnId",type = TblOrganizationMapperSqlConifg.class)
	@Options(useGeneratedKeys=true, keyProperty="orgid", keyColumn="ORGID")
	Integer addReturnId(TblOrganization organization);

	@UpdateProvider(method = "saveModiOrganiza",type = TblOrganizationMapperSqlConifg.class)
	Integer saveModiOrganiza(TblOrganization organization);

	@InsertProvider(method = "saveModiOrgan",type = TblOrganizationMapperSqlConifg.class)
	TblOrganization saveModiOrgan(TblOrganization o);

	@UpdateProvider(method = "updateZuZhi",type = TblOrganizationMapperSqlConifg.class)
	void updateZuZhi(TblOrganization organization);

	@InsertProvider(method = "saveAtionHangYe",type = TblOrganizationMapperSqlConifg.class)
	void saveAtionHangYe(TblOrganization ation);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{pid}")
	TblOrganization selectByPid(String pid);

	@UpdateProvider(method = "updateAtionHangYe",type = TblOrganizationMapperSqlConifg.class)
	void updateAtionHangYe(TblOrganization organization);


	@Select("select * from TBL_ORGANIZATION  t where t.fatherorgid = #{nodeId} and status = 0 order by t.orderid asc")
    List<Tree> getNodes(BigDecimal nodeId);

	@Select("select * from tbl_organization where ORGID = #{nodeId} and STATUS=0 and orgtype < 100 ORDER BY orderid ASC")
	List<TblOrganization> findByNode(BigDecimal nodeId);

	@Select("select * from TBL_ORGANIZATION where ORGID = #{orgid}")
	String selectFindOrgid(TblOrganization orgid);

	@Select("select * from TBL_ORGANIZATION  WHERE ORGTYPE <100 and orgid= #{orgid} and  status = 0 ORDER BY ORDERID ASC")
	List<TblOrganization> findByOrg(String orgid);

	@Select("select * from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
    List<TblOrganization> getRoot(BigDecimal orgid);

	@Select("select count(*) from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
	int getChildCount(BigDecimal orgid);

	@Select("select * from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
	List<TblOrganization> getChild(BigDecimal orgid);

	@Select("select * from TBL_ORGANIZATION WHERE FIND_IN_SET(ORGID,#{result})")
    List<TblOrganization> findByResult(Object result);

	@Select("select * from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 START WITH ORGID = #{orgid} CONNECT by PRIOR orgid = fatherorgid")
	List<TblOrganization> findBysqlOrgids(String orgid);


	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = ( SELECT MAX(ORGTYPE) FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID ) AND  ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID")
	List<TblOrganizationUtil> findByOrgid(String orgId);

    @Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} order by t.orderid asc")
	List<TblOrganization> getNodeId(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and orgtype = 100 ORDER BY orderid ASC")
	List<TblOrganization> findByNodeIdNew(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where 1=1 AND ORGTYPE<>0 and ORGTYPE<100 and STATUS=0  start with   FATHERORGID= #{nodeId} connect by prior ORGID= FATHERORGID")
	List<TblOrganization> findByfather(BigDecimal nodeId);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganization> selectAllCompanyList();
    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
    TblOrganization selectCompanyByIdForYm(BigDecimal orgId) throws Exception;

    @Select("SELECT PKYMORGID FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
	String selectYmPkOrgid(BigDecimal fatherorgid);

    @Select("select * from Tbl_Organization where   historycode=#{historycode}")
	List<TblOrganization> getOrgByHistoryid(String historycode);

	@Select("SELECT * FROM TBL_ORGANIZATION_INFO WHERE ORGID = #{orgid} ")
    TblOrganizationInfo findOrganInfoDetail(String orgid);
	
	@InsertProvider(method = "insertOrganInfo",type = TblOrganizationMapperSqlConifg.class)
	void insertOrganInfo(TblOrganizationInfo info);
	
	@UpdateProvider(method = "updateOrganInfo",type = TblOrganizationMapperSqlConifg.class)
	void updateOrganInfo(TblOrganizationInfo info);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = (SELECT FATHERORGID FROM TBL_ORGANIZATION WHERE ORGID =  #{orgid}  )")
	TblOrganization selectFatherOrgIdInfoByOrgId(BigDecimal orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid} AND ORGNAME = '外聘专家库' ")
	TblOrganization selectWpOrgInfoByFatherId(BigDecimal orgid);

	@Select("SELECT ORGID,ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${deptIds})")
	List<TblOrganization> selectDeptListByDeptIds(@Param("deptIds") String deptIds) throws Exception;

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT ORGID FROM TBL_SYSTEM_DATA_RIGHT WHERE ROLEID IN (${roleIdStrs}))")
	List<TblOrganization> selectOrgListByDataRealtion(@Param("roleIdStrs")String roleIdStrs) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME = #{orgname}")
	Integer selectorgidByOrgname(String orgname) throws Exception;

	@SelectProvider(method="selectDeptInfoByGranDataList",type=TblOrganizationMapperSqlConifg.class)
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "FATHERORGID",property = "fatherorgid"),
		@Result(column = "FATHERORGNAME",property="fahterOrgName"),
	})
	IPage<TblOrganization> selectDeptInfoByGranDataList(IPage<TblOrganization> page , String deptName,String companyName, BigDecimal roleId) throws Exception;
	
	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID = -1 AND ROWNUM = 1")
	BigDecimal selectRootCompanyIdByOrgId() throws Exception;

	@Select("SELECT ORGID,ORGNAME,FATHERORGID,ORGTYPE FROM TBL_ORGANIZATION WHERE FATHERORGID = #{fatherId} ORDER BY ORGTYPE ASC , ORGID ASC")
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "FATHERORGID",property = "fatherorgid"),
	})
	List<TblOrganization> selectAllListByParentId(BigDecimal fatherId) throws Exception;

	@Select("SELECT WM_CONCAT(ORGID) FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%${orgName}%'")
	String selectAllListByOrgName(@Param("orgName") String orgName) throws Exception;

	@SelectProvider(method="selectGrantDataRightDeptListByCompanyId",type=TblOrganizationMapperSqlConifg.class)
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "ORGNUMBER",property = "orgnumber"),
		@Result(column = "ISCHECKED",property="isChecked"),
		@Result(column = "ORGTYPE",property="orgtype"),
	})
	IPage<TblOrganization> selectGrantDataRightDeptListByCompanyId(IPage<TblOrganization> page, String deptIds,
			String deptName, String deptNumber, BigDecimal roleId, BigDecimal pid) throws Exception;
	
	@SelectProvider(method="selectGrantDataRightDeptCountByCompanyId",type=TblOrganizationMapperSqlConifg.class)
	Integer selectGrantDataRightDeptCountByCompanyId(PageInfo<TblOrganization> pageInfo, BigDecimal pid,
			String deptName, String deptNumber, Integer roleId) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%${companyName}%' AND ORGTYPE != 0 ")
	List<String> selectOrgIdsByOrgName(@Param("companyName") String companyName) throws Exception;


	@Select("SELECT DISTINCT DEPTID FROM TBL_USER_ORGRELATION WHERE STAFFID IN (SELECT STAFFID FROM TBL_STAFF WHERE USERNAME IN (${preUserAccounts})) AND ORGID = #{orgId}")
	List<String> selectDeptIdsByUserOrganization(@Param("preUserAccounts")String preUserAccounts, @Param("orgId")BigDecimal orgId);

	@Select("SELECT ORG.ORGID,ORG.ORGNAME,ORG.FATHERORGID,ORG.ORGTYPE,(SELECT COUNT(0) FROM TBL_ORG_ROLE WHERE ROLEID = #{roleId} AND ORGID = ORG.ORGID) AS ISCHECKED FROM TBL_ORGANIZATION ORG WHERE ORG.FATHERORGID = #{fatherId} AND ORGTYPE != 0 ORDER BY ORG.ORGTYPE ASC , ORG.ORGID ASC")
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "FATHERORGID",property = "fatherorgid"),
		@Result(column = "ISCHECKED",property = "isChecked"),
	})
	List<TblOrganization> selectAllListByParentIdToRoleId(BigDecimal fatherId, BigDecimal roleId);

	@Select("SELECT ORG.ORGID,ORG.ORGNAME,ORG.FATHERORGID,ORG.ORGTYPE,(SELECT COUNT(0) FROM TBL_ORG_ROLE WHERE ROLEID = #{roleId} AND ORGID = ORG.ORGID) AS ISCHECKED FROM TBL_ORGANIZATION ORG WHERE ORG.ORGTYPE = #{orgtype} AND ORG.ORGID IN (${allOrgIdStrs}) ")
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "FATHERORGID",property = "fatherorgid"),
		@Result(column = "ISCHECKED",property = "isChecked"),
	})
	List<TblOrganization> selectAllListByOrgtypeAndOrgIdsByRole(int orgtype, String allOrgIdStrs, BigDecimal roleId);

	@Select("SELECT ORG.ORGID,ORG.ORGNAME,ORG.FATHERORGID,ORG.ORGTYPE,(SELECT COUNT(0) FROM TBL_ORG_ROLE WHERE ROLEID = #{roleId} AND ORGID = ORG.ORGID) AS ISCHECKED FROM TBL_ORGANIZATION ORG WHERE ORG.FATHERORGID = #{orgid} AND ORG.ORGID IN (${allOrgIdStrs}) ")
	@Results({
		@Result(column = "ORGID",property = "orgid"),
		@Result(column = "ORGNAME",property = "orgname"),
		@Result(column = "FATHERORGID",property = "fatherorgid"),
		@Result(column = "ISCHECKED",property = "isChecked"),
	})
	List<TblOrganization> selectChilrenListByOrgIdScopeToRole(BigDecimal orgid, String allOrgIdStrs, BigDecimal roleId);

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 AND FATHERORGID = #{orgid} ")
	List<String> selectSetDeptIdListByOrgId(BigDecimal orgid) throws Exception;

	@Insert("INSERT INTO TBL_SYSTEM_ORG_RIGHT(ORGID,RIGHTID,RIGHTSTATUS) SELECT #{orgid},ID,1 FROM TBL_SYSTEM_RIGHT")
	void initOrgRightInfo(@Param("orgid")BigDecimal orgid) throws Exception;

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 and STATUS = 0  AND ORGTYPE = ( SELECT MAX(ORGTYPE) FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 and STATUS = 0 START WITH ORGID = #{orgid} CONNECT BY PRIOR FATHERORGID = ORGID ) START WITH ORGID = #{orgid} CONNECT BY PRIOR FATHERORGID = ORGID ")
	TblOrganization selectOrgInfoByChildren(@Param("orgid")BigDecimal orgid) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGTYPE = ( SELECT MAX(ORGTYPE) FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID ) AND  ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID ")
	Integer findCompanyIdByDeptId(@Param("orgId")String orgId) throws Exception;

	
	//新方法
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    TblOrganization selectByOrgid(BigDecimal orgid);

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${fatherids})  AND ORGTYPE = 0 AND STATUS = 0")
	List<String> selectDeptIdsByCompanyId(@Param("fatherids")String fatherids) throws Exception;
	
	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId}) AND ORGTYPE = 0 AND STATUS = 0")
	List<String> selectChildrenDeptIdsByFatherOrgIds(@Param("orgId") String orgId) throws Exception;
	
	@Select("SELECT ORG.*,TS.REALNAME AS PRINCIPALNAME FROM TBL_ORGANIZATION ORG LEFT JOIN TBL_STAFF TS ON ORG.PRINCIPALSTAFFID = TS.STAFFID WHERE ORG.ORGID = #{orgid} ")
	TblOrganization findByid(String id);
	
	@Select("SELECT ORG.*,TS.REALNAME AS PRINCIPALNAME FROM TBL_ORGANIZATION ORG LEFT JOIN TBL_STAFF TS ON ORG.PRINCIPALSTAFFID = TS.STAFFID WHERE ORG.ORGID = #{orgid} ")
	TblOrganization findByoId(BigDecimal orgid);
	
	@Select("SELECT ORG.*,TS.REALNAME AS PRINCIPALNAME FROM TBL_ORGANIZATION ORG LEFT JOIN TBL_STAFF TS ON ORG.PRINCIPALSTAFFID = TS.STAFFID WHERE ORG.ORGID = #{orgid} ")
    TblOrganization selectByOrgId(BigDecimal orgid);
	
	@Select("select* from TBL_ORGANIZATION t where t.fatherorgid = #{nodeId} order by t.orderid asc")
	List<TblOrganization> getNodesaGSNodeId(BigDecimal nodeId);
	
    @Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%${orgName}%' AND ORGTYPE != 0 ")
	List<String> selectAllCompanyListByOrgName(@Param("orgName")String orgName);
    
	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT FATHERORGID FROM TBL_ORGANIZATION WHERE ORGID IN (${orgStrIds})) AND ORGID NOT IN (${allOrgIdStrs}) ")
	List<String> selectAllListByChildrenOrgName(@Param("orgStrIds") String orgStrIds,@Param("allOrgIdStrs") String allOrgIdStrs) throws Exception;
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = #{orgtype} AND ORGID IN (${allOrgIdStrs}) ")
	List<TblOrganization> selectAllListByOrgtypeAndOrgIds(@Param("orgtype")int orgtype,@Param("allOrgIdStrs") String allOrgIdStrs) throws Exception;
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid} AND ORGID IN (${allOrgIdStrs}) ")
	List<TblOrganization> selectChilrenListByOrgIdScope(@Param("orgid")BigDecimal orgid,@Param("allOrgIdStrs") String allOrgIdStrs) throws Exception;

	@SelectProvider(method = "selectRepeatNumber",type = TblOrganizationMapperSqlConifg.class)
	Integer selectRepeatNumber(String orgnumber, String uniqueNumber, BigDecimal orgId) throws Exception;
	
	@SelectProvider(method = "selectRepeatName",type = TblOrganizationMapperSqlConifg.class)
	Integer selectRepeatName(String orgname, String uniqueNumber, BigDecimal orgId) throws Exception;
	
    @Update("UPDATE TBL_ORGANIZATION SET PKYMORGID = #{ymPkOrgId} WHERE ORGID = #{orgid}")
	void updateYmPkOrgid(String ymPkOrgId, BigDecimal orgid) throws Exception;
    
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	TblOrganization selectOrgid(String orgid);
	
	@Select("select * from TBL_ORGANIZATION where 1=1 and  ORGNAME!='外聘专家库' start with  ORGID= #{id} connect by prior ORGID= FATHERORGID ORDER BY orderid ASC")
	List<TblOrganization> selectId(String id);
	
	@Select("SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE FATHERORGID = #{fatherOrgid}")
	Integer selectChildrenCount(BigDecimal fatherOrgid);
	
	@SelectProvider(method="selectListByPageInfoOrgid",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> selectListByPageInfoOrgid(Find find,BigDecimal pid, String orgIdStrs);

	@Select("select * from TBL_ORGANIZATION org where orgtype = 100 and (STATUS != 1 or STATUS IS NULL) AND fatherorgid = #{orgid} ORDER BY orderid ASC")
    List<TblOrganization> selectHyzong(BigDecimal orgid);

	@SelectProvider(method="selectListByPageInfo",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> selectListByPageInfo(IPage<TblOrganization> page);
	
	@SelectProvider(method="selectListByStaffid",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> selectListByStaffid(IPage<TblOrganization> page,String pid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{id} ORDER BY orderid ASC")
	TblOrganization findPid(String pid);
	
	@Select("select * from TBL_ORGANIZATION where FATHERORGID = #{id} ORDER BY orderid ASC")
	List<TblOrganization> findSql(String id);
	
	@Select("select * from TBL_ORGANIZATION where ORGID in (select INDUSTRYID from TBL_INDUSTRY_INNER where ORGID = #{hyid}  AND INDUSTRYID in (${pid})) and ORGTYPE != 0")
	List<TblOrganization> findBy(@Param("hyid")String hyid,@Param("pid")String pid);
	
	@Select("select DISTINCT o.* from TBL_ORGANIZATION o where o.ORGID in (${orgids}) BY orderid ASC ")
	List<TblOrganization> findByorgids(@Param("orgids") String orgids);
	
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and o.ORGID = #{orgid}  order BY o.ORGTYPE desc")
	List<TblOrganization> findBysqlObj(Object result);
	
	@Select("select ORGID,ORGNAME from TBL_ORGANIZATION  where ORGID IN (#{orgid}) ")
	List<TblOrganization> findBysqlOrgid(@Param("orgid") String orgid);
	
	@SelectProvider(method="selectDeptListByroleid",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> selectDeptListByroleid(String roleid, IPage<TblOrganization> page, String orgname);
	
	@Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId}  and orgtype < 100 ORDER BY orderid ASC")
	List<TblOrganization> findByNodeId(BigDecimal nodeId);
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid} ORDER BY ORGTYPE ,ORDERID")
	Set<TblOrganization> findByfatherorgId(BigDecimal orgid);
	
	@Select("select * from tbl_organization order by orderid asc")
	List<TblOrganization> getNodesaGS(BigDecimal nodeId);
	
	@SelectProvider(method="findByWPZJK",type=TblOrganizationMapperSqlConifg.class)
	TblOrganization findByWPZJK(BigDecimal orgid, String wpzjk);
	
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where 1=1 and o.orgtype = 100  "+
			" AND o.fatherorgid = #{fatherOrgId}")
	List<TblOrganization> selectCurrentOrgHyList(BigDecimal orgid,BigDecimal fatherOrgId);
	
	@SelectProvider(method="findAllorganization",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> findAllorganization();
	
    @Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID = #{companyid}")
    String findByorgid(BigDecimal companyid);
    
    @SelectProvider(method="selectListByPid",type=TblOrganizationMapperSqlConifg.class)
	IPage<TblOrganization> selectListByPid(IPage<TblOrganization> page, String pid);
    
    @Select("SELECT ISINITIALIZATION FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	String findIniStatus(BigDecimal orgid);
    
    @Select("SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE ORGNAME = #{companyName}")
	Integer selectCountByName(@Param("companyName")String companyName) throws Exception;

	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME = #{orgName}")
	BigDecimal selectOrgIdByCompanyName(@Param("orgName")String companyName);
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 AND  FATHERORGID = #{orgid} ")
	List<TblOrganization> selectDeptListByOrgId(BigDecimal orgid) throws Exception;
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{parentId} AND ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganization> selectCompanyListParentId(BigDecimal parentId) throws Exception;

	@InsertProvider(method="insertSystemProjectAuth",type=TblOrganizationMapperSqlConifg.class)
	void insertSystemProjectAuth(BigDecimal orgid, BigDecimal staffid, BigDecimal projectId, BigDecimal id) throws Exception;

	@Update("UPDATE TBL_ORGANIZATION SET STATUS = #{status} WHERE ORGID IN (${allOrgIdStrs})")
	void updateOrgInfoStatusByIds(@Param("allOrgIdStrs")String allOrgIdStrs,@Param("status") Integer status);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGNUMBER = #{orgnumber}")
	TblOrganization selectInfoByOrgNumber(@Param("orgnumber")String orgnumber) throws Exception;
	
	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE fatherorgid IN (${orgid})  AND STATUS = 0 ")
	List<String> selectFatherIdStrsByChidrenOrgId(String orgid) throws Exception;
	
	
	@Select("SELECT COUNT(*) FROM TBL_ORGANIZATION WHERE ORGNUMBER = #{orgnumber}")
	Integer selectCntByOrgNumber(@Param("orgnumber")String orgnumber) throws Exception;
	
	@Select("SELECT COUNT(*) FROM TBL_ORGANIZATION WHERE ORGNUMBER = #{orgnumber} AND FATHERORGID = -1")
	Integer selectCountByRootName(String companyName) throws Exception;
	
	
	@Select("SELECT MAX(ORGID) FROM TBL_ORGANIZATION WHERE ORGNAME = #{deptname} AND FATHERORGID = (SELECT MAX(ORGID) FROM TBL_ORGANIZATION WHERE ORGNAME = #{orgname})")
	BigDecimal selectOrgidByOrgDeptName(@Param("deptname")String deptname,@Param("orgname")String orgname) throws Exception;

	/**
	 * 通过传入的用户主键 获取用户所属部门的流程平台主键
	 * @param staffId --用户主键
	 * @return
	 */
	@Select("SELECT PKYMORGID FROM TBL_ORGANIZATION WHERE ORGID = (SELECT ORGID FROM TBL_STAFF WHERE PKYMSTAFFID = #{staffId})")
	String selectPkYmIdByStaffId(@Param("staffId")String staffId) throws Exception;

	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${organizationTrees}) ORDER BY ORGID ASC ")
	List<String> selectTreeNamesByOrgTreeId(@Param("organizationTrees")String organizationTrees);

	@Select("SELECT ORGID,ORGNAME,FATHERORGID,ORGANIZATIONTREES,ORGNUMBER FROM TBL_ORGANIZATION WHERE ORGID IN (SELECT DEPTID FROM TBL_USER_ORGRELATION WHERE STAFFID = #{staffId} ) ")
	List<TblOrganization> selectParttimeDeptByStaffId(@Param("staffId")BigDecimal staffId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_SYSTEM_PROJECT_AUTH WHERE PROJECTID = #{id} AND (AUTHORGID = #{orgid} OR WORKUNIT = #{orgid} OR BELONGGROUP = #{orgid})")
	Integer selectSystemOrgPorjectAuth(@Param("orgid")BigDecimal orgid,@Param("id") BigDecimal id) throws Exception;
	
	//按名称查询公司
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGNAME=#{orgname}")
	List<TblOrganization> getOrgByName(String orgname);
	
	//按number查询组织架构
	@Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNUMBER=#{orgnumber}")
	BigDecimal getOrgByNumber(String orgnumber);

	//查询所有ORGANIZATIONTREES 为 null 的列表
	@Select("SELECT ORGID,FATHERORGID FROM TBL_ORGANIZATION WHERE ORGANIZATIONTREES is NULL ORDER BY ORGID ASC")
	List<TblOrganization> selectAllOrgTreeNull();

	//查询对应组织架构的树形结构
	@Select("SELECT ORGID,ORGANIZATIONTREES FROM TBL_ORGANIZATION WHERE ORGID = #{orgID}")
	TblOrganization selectOrgTreeByOrgTreeId(BigDecimal orgID);

	//修改
	@Update("UPDATE TBL_ORGANIZATION SET ORGANIZATIONTREES = #{organizationTrees} WHERE ORGID = #{orgID};")
	void updateOrgTree(@Param("organizationTrees")String organizationTrees,@Param("orgID")BigDecimal orgID);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGNUMBER = #{orgnumber} AND UNIQUENUMBER = #{uniqueNumer}")
	TblOrganization selectDeptInfoByUniqueNumber(@Param("orgnumber")String orgnumber,@Param("uniqueNumer")String uniqueNumer) throws Exception;

	@Select("SELECT ORGNUMBER FROM TBL_ORGANIZATION WHERE FATHERORGID = -1")
	List<String> selectRootComPanyNumber() throws Exception;
	
	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID IN (${orgids})")
	List<String> selectNameListByOrgIds(@Param("orgids")String orgids) throws Exception;

	@Select("SELECT PKYMORGID FROM TBL_ORGANIZATION WHERE ORGID IN (${orgIdTrees}) ORDER BY ORGID ASC")
	List<String> selectYmPkOrgIdTrees(@Param("orgIdTrees")String orgIdTrees) throws Exception;

	@Select("SELECT " +
			"    o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGTYPE,o.STATUS,o.ORDERID,o.ORGANIZATIONTREES " +
			"FROM TBL_ORGANIZATION o " +
			"WHERE " +
			"    o.ORGTYPE BETWEEN 1 AND 200 " +
			"    AND o.status = 0 " +
			"    AND EXISTS ( " +
			"        SELECT 1 " +
			"        FROM TBL_SYSTEM_PROJECT_AUTH r  " +
			"        WHERE r.AUTHORGID = o.ORGID  " +
			"    ) " +
			"ORDER BY o.ORGTYPE,o.ORDERID")
	List<TblOrganization> selectOrgTreeBySystemRight() throws Exception;
}
