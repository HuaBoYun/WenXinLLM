package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.entity.Find;
import com.huabo.compliance.entity.TblOrganization;
import com.huabo.compliance.entity.TblOrganizationInfo;
import com.huabo.compliance.util.Tree;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

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

	@Update("UPDATE TBL_ORGANIZATION SET (FATHERORGID,ORGNAME,ORGNUMBER,MEMO,ORGMENO,ORGTYPE) VALUES (#{fatherorgid},#{orgname},#{orgnumber},#{memo},#{orgmeno},#{orgtype})")
    void saveTblOrganization(TblOrganization org);

	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and o.ORGID = #{orgid} CONNECT by PRIOR o.FATHERORGID = o.ORGID order BY o.ORGTYPE desc")
	Object excuteFunReturnUnique(String orgid);
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and o.ORGID = #{orgid}  order BY o.ORGTYPE desc")
	List<TblOrganization> findBysqlObj(Object result);

	@Select("select * from TBL_ORGANIZATION where 1=1 and  ORGNAME!='外聘专家库' start with  ORGID= #{id} connect by prior ORGID= FATHERORGID ORDER BY orderid ASC")
	List<TblOrganization> selectId(String id);

	@Select("select * from TBL_ORGANIZATION org where orgtype < 100 and orgtype !=0 and STATUS = 0 start with ORGID = #{orgid} connect by prior orgid = fatherorgid ORDER BY ORGID ASC")
	List<TblOrganization> findAllOrgUserLeft(String orgid);

	@Select("select * from TBL_ORGANIZATION org where orgtype < 100 and orgtype !=0 and STATUS = 0 and fatherorgid= #{orgid} ORDER BY ORGID ASC")
	List<TblOrganization> findAllOrgUserLeftOneLevel(String orgid);


	@Select("select * from TBL_ORGANIZATION org where orgtype = 100 and (STATUS != 1 or STATUS IS NULL) start with fatherorgid = -1 connect by prior orgid = fatherorgid   ORDER BY orderid ASC")
    List<TblOrganization> selectzong();

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

	@Select("select * from TBL_ORGANIZATION where FATHERORGID = #{id} ORDER BY orderid ASC")
	List<TblOrganization> findSql(String id);

	@Select("select * from TBL_ORGANIZATION where ORGID in (select INDUSTRYID from TBL_INDUSTRY_INNER where ORGID = #{hyid}  AND INDUSTRYID in (select ORGID from TBL_ORGANIZATION org where org.orgtype < 100 and ORG.ORGTYPE>0 start with ORGID =#{pid} connect by prior orgid = fatherorgid )) and ORGTYPE != 0")
	List<TblOrganization> findBy(@Param("hyid")String hyid,@Param("pid")String pid);

	@SelectProvider(method="selectListByPageInfo",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> selectListByPageInfo(PageInfo<TblOrganization> pageInfo);

	//@SelectProvider(method="selectCountByPageInfo",type=TblOrganizationMapperSqlConifg.class)
	@Select("SELECT COUNT(*) FROM TBL_ORGANIZATION where ORGTYPE = 100 ")
	Integer selectCountByPageInfo(PageInfo<TblOrganization> pageInfo);

	@SelectProvider(method="selectStaffInfoByModuleIdList",type=TblOrganizationMapperSqlConifg.class)
    List<TblOrganization> selectStaffInfoByModuleIdList(PageInfo<TblOrganization> pageInfo, Integer moduleId);

	@SelectProvider(method="selectStaffInfoByModuleIdCount",type=TblOrganizationMapperSqlConifg.class)
	Integer selectStaffInfoByModuleIdCount(PageInfo<TblOrganization> pageInfo, Integer moduleId);

//	@Select("SELECT * FROM TBL_ORGANIZATION")
	@SelectProvider(method = "selectListByPageInfoo",type = TblOrganizationMapperSqlConifg.class)
    List<TblOrganization> selectListByPageInfoo(PageInfo<TblOrganization> pageInfo);

//	@Select("select * from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = #{pid}  CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY orgtype asc,ORGID desc,rownum ASC")
//    @SelectProvider(method = "findByPageBean",type =TblOrganizationMapperSqlConifg.class )
//	List<TblOrganization> findByPageBean(PageInfo<TblOrganization> pageInfo, Integer pageSize, String staffId);



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

	@Select("select * from tbl_organization order by orderid asc")
	List<TblOrganization> getNodesaGS(BigDecimal nodeId);

	@Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId}  and orgtype < 100 ORDER BY orderid ASC")
	List<TblOrganization> findByNodeId(BigDecimal nodeId);

//	@Select("select * from TBL_ORGANIZATION org where ORGTYPE != 0 and ORGTYPE<100 and status=0  and ORG.FATHERORGID = #{pid}  ORDER BY orgid,orderid ASC")
	@SelectProvider(method = "findAllCommpanyPageBeanStaffid",type = TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> findAllCommpanyPageBeanStaffid(PageInfo<TblOrganization> pageInfo, BigDecimal pid);

	@Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0  and ORGTYPE<100 and status=0  and ORG.FATHERORGID = #{pid}   ORDER BY orgtype,orderid ASC")
	//@SelectProvider(method = "findAllCommpanyPage",type = TblOrganizationMapperSqlConifg.class)
	Integer findAllCommpanyPage(PageInfo<TblOrganization> pageInfo, BigDecimal pid);



	@SelectProvider(method="selectListByPid",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> selectListByPid(PageInfo<TblOrganization> pageInfo, String pid);

	@Select("select count(*) from TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= #{pid}")
	Integer selectCountByPid(PageInfo<TblOrganization> pageInfo, String pid);


	@Update("UPDATE TBL_ORGANIZATION SET (ORGID,FATHERORGID,ORGNAME,ORGNUMBER,MEMO,ORGMENO,ORGTYPE) VALUES(#{orgid},#{fatherorgid},#{orgname},#{orgnumber},#{memo},#{orgmeno},#{orgtype}")
	void saveOrg(TblOrganization org);

	@Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    int deleteOrgid(TblOrganization orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{pid}")
    TblOrganization findIdOrgid(String pid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{id}")
	TblOrganization findByid(String id);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{id} ORDER BY orderid ASC")
	TblOrganization findPid(String pid);

	@SelectProvider(method="selectListByPageInfoOrgid",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> selectListByPageInfoOrgid(PageInfo<TblOrganization> pageInfo,Find find,BigDecimal pid);

//	@SelectProvider(method="selectListByPageInOrgid",type=TblOrganizationMapperSqlConifg.class)
//	List<TblOrganization> selectListByPageInOrgid(PageInfo<TblOrganization> pageInfo, BigDecimal orgid);



	@Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	void deleteOrg(BigDecimal orgid);


	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid} ")
	//@Options(useGeneratedKeys=true, keyProperty="orgid", keyColumn="ORGID")
    TblOrganization selectByOrgId(BigDecimal orgid);


//	@SelectProvider(method="selectCountPage",type=TblOrganizationMapperSqlConifg.class)
	@Select("select count(*) from TBL_ORGANIZATION where 1=1 start with  FATHERORGID= #{pid} and ORGTYPE=0  connect by prior orgid= FATHERORGID ORDER BY ORGID desc,orderid ASC")
	Integer selectCountPage(PageInfo<TblOrganization> pageInfo,Find find,BigDecimal pid);

	@SelectProvider(method="selectListByStaffid",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> selectListByStaffid(PageInfo<TblOrganization> pageInfo,String pid);
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    TblOrganization selectByOrgid(BigDecimal orgid);

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
			@Result(column = "ISAUTONUMBER",property = "isautonumber")
	})
	List<TblOrganization> findByPageBean(PageInfo<TblOrganization> pageInfo, BigDecimal orgid,Find find);

	//@Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = #{orgid} CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID")
	@SelectProvider(method = "findByPage",type = TblOrganizationMapperSqlConifg.class)
	Integer findByPage(PageInfo<TblOrganization> pageInfo, BigDecimal orgid,Find find);

	@SelectProvider(method="getNodesa",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> getNodesa(PageInfo<TblOrganization> pageInfo);

	@Select("select count(*) from tbl_organization")
	Integer getNode();

	@SelectProvider(method="getNodesaNodeId",type=TblOrganizationMapperSqlConifg.class)
	List<TblOrganization> getNodesaNodeId(PageInfo<TblOrganization> pageInfo, BigDecimal nodeId);

	@Select("select * from tbl_organization where ORGID = #{nodeId}  and orgtype < 100 ORDER BY orderid ASC")
	Integer getNodeNodeId(BigDecimal nodeId);

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

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	TblOrganization selectOrgid(String orgid);

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

	@Select("select* from TBL_ORGANIZATION t where t.fatherorgid = #{nodeId} order by t.orderid asc")
	List<TblOrganization> getNodesaGSNodeId(BigDecimal nodeId);

	@Select("select * from TBL_ORGANIZATION WHERE FIND_IN_SET(ORGID,#{result})")
    List<TblOrganization> findByResult(Object result);

	@Select("select level,ORGID,ORGNAME from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 START WITH ORGID = #{orgid} CONNECT by PRIOR orgid = fatherorgid")
	List<TblOrganization> findBysqlOrgid(String orgid);

	@Select("select * from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 START WITH ORGID = #{orgid} CONNECT by PRIOR orgid = fatherorgid")
	List<TblOrganization> findBysqlOrgids(String orgid);

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	TblOrganization findByoId(BigDecimal orgid);



	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = ( SELECT MAX(ORGTYPE) FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID ) AND  ORGTYPE != 0 AND ORGTYPE < 100  START WITH ORGID = #{orgId} CONNECT BY PRIOR FATHERORGID = ORGID")
	List<TblOrganizationUtil> findByOrgid(String orgId);
    @Select("SELECT ISINITIALIZATION FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
	String findIniStatus(BigDecimal orgid);

    @Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID = #{companyid}")
    String findByorgid(BigDecimal companyid);

    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where 1=1 and o.orgtype = 100  "+
		" start with  o.fatherorgid = -1 connect by prior o.orgid= o.fatherorgid")
	List<TblOrganization> findByOrgis(String orgid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) AND ROWNUM = 1")
	List<TblOrganization> findAllorganization();

    @Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} order by t.orderid asc")
	List<TblOrganization> getNodeId(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and orgtype = 100 ORDER BY orderid ASC")
	List<TblOrganization> findByNodeIdNew(BigDecimal nodeId);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid}")
	Set<TblOrganization> findByfatherorgId(BigDecimal orgid);

    @Select("select * from TBL_ORGANIZATION where 1=1 AND ORGTYPE<>0 and ORGTYPE<100 and STATUS=0  start with   FATHERORGID= #{nodeId} connect by prior ORGID= FATHERORGID")
	List<TblOrganization> findByfather(BigDecimal nodeId);

    @Select("select DISTINCT o.* from TBL_ORGANIZATION o where 1=1 and ORGTYPE=100 AND o.ORGID not in (${orgids}) start with o.ORGID in (${orgids}) connect by prior o.FATHERORGID = o.ORGID ORDER BY orderid ASC ")
	List<TblOrganization> findByorgids(@Param("orgids") String orgids);

    @Select("select * from TBL_ORGANIZATION where FATHERORGID = #{orgid} and ISZY = #{wpzjk} and rownum = 1 ORDER BY orderid ASC")
	TblOrganization findByWPZJK(BigDecimal orgid, String wpzjk);

    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{parentId} AND ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganization> selectCompanyListParentId(Integer parentId) throws Exception;

    @Update("UPDATE TBL_ORGANIZATION SET PKYMORGID = #{ymPkOrgId} WHERE ORGID = #{orgid}")
	void updateYmPkOrgid(String ymPkOrgId, BigDecimal orgid) throws Exception;

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganization> selectAllCompanyList();

    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 AND  FATHERORGID = #{orgid} ")
	List<TblOrganization> selectDeptListByOrgId(BigDecimal orgid) throws Exception;

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
    TblOrganization selectCompanyByIdForYm(Integer orgId) throws Exception;

    @Select("SELECT PKYMORGID FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
	String selectYmPkOrgid(BigDecimal fatherorgid);


//    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where 1=1 and o.orgtype = 100  "+
//			" start with  o.fatherorgid = -1 connect by prior o.orgid= o.fatherorgid")
//    List<TblOrganization> optionHYOrgTree(BigDecimal orgid);


//	@Select("select * from TBL_ORGANIZATION where FATHERORGID = #{orgid} and ISZY = #{iszy} ORDER BY orderid ASC")
//	List<TblOrganization> selectWpzjk(String orgid, String iszy);
    

    @Select("select * from Tbl_Organization where   historycode=#{historycode}")
	List<TblOrganization> getOrgByHistoryid(String historycode);

    

	@Select("SELECT * FROM TBL_ORGANIZATION_INFO WHERE ORGID = #{orgid} ")
    TblOrganizationInfo findOrganInfoDetail(String orgid);
	
	
	@InsertProvider(method = "insertOrganInfo",type = TblOrganizationMapperSqlConifg.class)
	void insertOrganInfo(TblOrganizationInfo info);
	
	@UpdateProvider(method = "updateOrganInfo",type = TblOrganizationMapperSqlConifg.class)
	void updateOrganInfo(TblOrganizationInfo info);

	@Select("SELECT * FROM TBL_ORGANIZATION_INFO WHERE FATHERORGID = #{orgid} ")
	TblOrganization selectFatherOrgIdInfoByOrgId(BigDecimal orgid);
}
