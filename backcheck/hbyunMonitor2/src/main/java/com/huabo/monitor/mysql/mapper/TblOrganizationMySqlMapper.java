package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.FindMySql;
import com.huabo.monitor.mysql.entity.TblOrganizationMySql;

import com.huabo.monitor.util.Tree;
import org.apache.ibatis.annotations.*;
import org.mockito.internal.matchers.Find;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-19
 */
@Mapper
public interface TblOrganizationMySqlMapper extends BaseMapper<TblOrganizationMySql> {

	@Select("SELECT GROUP_CONCAT(ORGID) FROM tbl_organization WHERE FATHERORGID IN (${orgId}) AND ORGTYPE = 0")
	String selectOrgIdsByParentIds(@Param("orgId")String orgId) throws Exception;
	
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{parentId} AND ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganizationMySql> selectCompanyListParentId(Integer parentId) throws Exception;
	
	@Update("UPDATE TBL_ORGANIZATION SET PKYMORGID = #{ymPkOrgId} WHERE ORGID = #{orgid}")
	void updateYmPkOrgid(String ymPkOrgId, BigDecimal orgid) throws Exception;

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 ")
	List<TblOrganizationMySql> selectAllCompanyList();

	    
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = 0 AND  FATHERORGID = #{orgid} ")
	List<TblOrganizationMySql> selectDeptListByOrgId(BigDecimal orgid) throws Exception;
	
	
	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
	TblOrganizationMySql selectCompanyByIdForYm(Integer orgId) throws Exception;
	
	@Select("SELECT PKYMORGID FROM TBL_ORGANIZATION WHERE ORGID = #{orgId}")
	String selectYmPkOrgid(BigDecimal orgid);
	
    /**
     * 传入部门Id 查找出当前部门所隶属的公司
     * //* @param deptid
     *
     * @return
     * @throws Exception
     */
    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY FROM TBL_ORGANIZATION o where ORGID = getParentOrgList(#{orgid}) ")
    @Results({
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "FATHERORGID", property = "fatherorgid"),
            @Result(column = "ORGNUMBER", property = "orgnumber"),
            @Result(column = "ORGMENO", property = "orgmeno"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "ICODE", property = "icode"),
            @Result(column = "ORGTYPE", property = "orgtype"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ISZY", property = "iszy"),
    })
    TblOrganizationMySql selectCompanyInfoByDeptId(@Param("orgid") BigDecimal orgid) throws Exception;

    @Update("UPDATE TBL_ORGANIZATION SET (FATHERORGID,ORGNAME,ORGNUMBER,MEMO,ORGMENO,ORGTYPE) VALUES (#{fatherorgid},#{orgname},#{orgnumber},#{memo},#{orgmeno},#{orgtype})")
    void saveTblOrganization(TblOrganizationMySql org);

    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and getparentHyorgList( #{orgid}) order BY o.ORGTYPE desc")
    String excuteFunReturnUnique(String orgid);

    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE != 0 and o.ORGID = #{orgid}  order BY o.ORGTYPE desc")
    List<TblOrganizationMySql> findBysqlObj(Object result);

    @Select("select * from TBL_ORGANIZATION where 1=1 and  ORGNAME!='外聘专家库' and getOrgIdList(ORGID= #{id}) ORDER BY orderid ASC")
    List<TblOrganizationMySql> selectId(String id);

    @Select("select * from TBL_ORGANIZATION org where orgtype < 100 and orgtype !=0 and STATUS = 0 and getOrgIdList(ORGID = #{orgid}) ORDER BY ORGID ASC")
    List<TblOrganizationMySql> findAllOrgUserLeft(String orgid);

    @Select("select * from TBL_ORGANIZATION org where orgtype < 100 and orgtype !=0 and STATUS = 0 and fatherorgid= #{orgid} ORDER BY ORGID ASC")
    List<TblOrganizationMySql> findAllOrgUserLeftOneLevel(String orgid);


    @Select("select * from TBL_ORGANIZATION org where orgtype = 100 and (STATUS != 1 or STATUS IS NULL) and getOrgIdList(fatherorgid = -1) ORDER BY orderid ASC")
    List<TblOrganizationMySql> selectzong();

    @Select("SELECT * from TBL_ORGANIZATION where  ORGNAME='行业' and ORGTYPE=100 ORDER BY orderid ASC")
    List<TblOrganizationMySql> selectGetHY();

    @Select("SELECT * from TBL_ORGANIZATION WHERE ORGNAME=#{orgname}  and ORGTYPE>=100  ")
    List<TblOrganizationMySql> findBysql(String orgname);

    @Select("select * from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orgid desc")
    String selectORGTYPE(Integer orgtype);

    @Select("select count(*) from TBL_ORGANIZATION where ORGTYPE = 100  ORDER BY orgid desc")
    String selectORGTYPECount(Integer orgtype);

    @Select("SELECT ORGNAME,ORGNUMBER,ORGID FROM TBL_ORGANIZATION WHERE 1 = 1 AND ORGTYPE != 0 and getFatherList(FATHERORGID = #{pid}) ORDER BY ORGID, orderid ASC")
    List<TblOrganizationMySql> selectPid(String pid);

    @Select("select ORGNAME,ORGNUMBER,ORGID from TBL_ORGANIZATION where 1=1 and getFatherList(FATHERORGID= #{pid}) and ORGTYPE=0 ORDER BY orderid ASC")
    List<TblOrganizationMySql> selectOrganme(String pid);

    @Select("select * from TBL_ORGANIZATION where FATHERORGID = #{id} ORDER BY orderid ASC")
    List<TblOrganizationMySql> findSql(String id);

    @Select("select * from TBL_ORGANIZATION where ORGID in (select INDUSTRYID from TBL_INDUSTRY_INNER where ORGID = #{hyid}  AND INDUSTRYID in (select ORGID from TBL_ORGANIZATION org where org.orgtype < 100 and ORG.ORGTYPE>0 and  getparentHyorgList (#{pid})  )) and ORGTYPE != 0")
    List<TblOrganizationMySql> findBy(@Param("hyid") String hyid, @Param("pid") String pid);

    @SelectProvider(method = "selectListByPageInfo", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectListByPageInfo(PageInfo<TblOrganizationMySql> pageInfo);

    //@SelectProvider(method="selectCountByPageInfo",type=TblOrganizationMapperSqlConifg.class)
    @Select("SELECT COUNT(*) FROM TBL_ORGANIZATION where ORGTYPE = 100 ")
    Integer selectCountByPageInfo(PageInfo<TblOrganizationMySql> pageInfo);

    @SelectProvider(method = "selectStaffInfoByModuleIdList", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectStaffInfoByModuleIdList(PageInfo<TblOrganizationMySql> pageInfo, Integer moduleId);

    @SelectProvider(method = "selectStaffInfoByModuleIdCount", type = TblOrganizationMapperSqlMySqlConifg.class)
    Integer selectStaffInfoByModuleIdCount(PageInfo<TblOrganizationMySql> pageInfo, Integer moduleId);

    //	@Select("SELECT * FROM TBL_ORGANIZATION")
    @SelectProvider(method = "selectListByPageInfoo", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectListByPageInfoo(PageInfo<TblOrganizationMySql> pageInfo);

//	@Select("select * from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = #{pid}  CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID ORDER BY orgtype asc,ORGID desc,START WITHSTART WITH ASC")
//    @SelectProvider(method = "findByPageBean",type =TblOrganizationMapperSqlConifg.class )
//	List<TblOrganization> findByPageBean(PageInfo<TblOrganization> pageInfo, Integer pageSize, String staffId);


    @SelectProvider(method = "findAllCommpanyPageBeanGSXj", type = TblOrganizationMapperSqlMySqlConifg.class)
    @Results({
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "ORGNUMBER", property = "orgnumber"),
            @Result(column = "ORGMENO", property = "orgmeno"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "ORGTYPE", property = "orgtype"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ISZY", property = "iszy"),
    })
    List<TblOrganizationMySql> findAllCommpanyPageBeanGSXj(PageInfo<TblOrganizationMySql> pageInfo, FindMySql find, String orgId);

    @Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0 and getOrgIdList(ORG.FATHERORGID = #{orgId})")
    Integer findAllCommpanyPageBeanGS(PageInfo<TblOrganizationMySql> pageInfo, FindMySql find, String orgId);

    @Select("select * from tbl_organization order by orderid asc")
    List<TblOrganizationMySql> getNodesaGS(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId}  and orgtype < 100 ORDER BY orderid ASC")
    List<TblOrganizationMySql> findByNodeId(BigDecimal nodeId);

    //	@Select("select * from TBL_ORGANIZATION org where ORGTYPE != 0 and ORGTYPE<100 and status=0  and ORG.FATHERORGID = #{pid}  ORDER BY orgid,orderid ASC")
    @SelectProvider(method = "findAllCommpanyPageBeanStaffid", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> findAllCommpanyPageBeanStaffid(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal pid);

    @Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0  and ORGTYPE<100 and status=0  and ORG.FATHERORGID = #{pid}   ORDER BY orgtype,orderid ASC")
        //@SelectProvider(method = "findAllCommpanyPage",type = TblOrganizationMapperSqlConifg.class)
    Integer findAllCommpanyPage(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal pid);


    @SelectProvider(method = "selectListByPid", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectListByPid(PageInfo<TblOrganizationMySql> pageInfo, String pid);

    @Select("select count(*) from TBL_ORGANIZATION where ORGTYPE = 100 and FATHERORGID= #{pid}")
    Integer selectCountByPid(PageInfo<TblOrganizationMySql> pageInfo, String pid);


    @Update("UPDATE TBL_ORGANIZATION SET (ORGID,FATHERORGID,ORGNAME,ORGNUMBER,MEMO,ORGMENO,ORGTYPE) VALUES(#{orgid},#{fatherorgid},#{orgname},#{orgnumber},#{memo},#{orgmeno},#{orgtype}")
    void saveOrg(TblOrganizationMySql org);

    @Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    int deleteOrgid(TblOrganizationMySql orgid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{pid}")
    TblOrganizationMySql findIdOrgid(String pid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{id}")
    TblOrganizationMySql findByid(String id);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{id} ORDER BY orderid ASC")
    TblOrganizationMySql findPid(String pid);

    @SelectProvider(method = "selectListByPageInfoOrgid", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectListByPageInfoOrgid(PageInfo<TblOrganizationMySql> pageInfo, Find find, BigDecimal pid);

//	@SelectProvider(method="selectListByPageInOrgid",type=TblOrganizationMapperSqlConifg.class)
//	List<TblOrganization> selectListByPageInOrgid(PageInfo<TblOrganization> pageInfo, BigDecimal orgid);


    @Delete("DELETE FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    void deleteOrg(BigDecimal orgid);


    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid} ")
        //@Options(useGeneratedKeys=true, keyProperty="orgid", keyColumn="ORGID")
    TblOrganizationMySql selectByOrgId(BigDecimal orgid);


    //	@SelectProvider(method="selectCountPage",type=TblOrganizationMapperSqlConifg.class)
    @Select("select count(*) from TBL_ORGANIZATION where 1=1 and getOrgIdList(FATHERORGID= #{pid}) and ORGTYPE=0 ORDER BY ORGID desc,orderid ASC")
    Integer selectCountPage(PageInfo<TblOrganizationMySql> pageInfo, Find find, BigDecimal pid);

    @SelectProvider(method = "selectListByStaffid", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> selectListByStaffid(PageInfo<TblOrganizationMySql> pageInfo, String pid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    TblOrganizationMySql selectByOrgid(BigDecimal orgid);

    @SelectProvider(method = "findByPageBean", type = TblOrganizationMapperSqlMySqlConifg.class)
    @Results({
            @Result(column = "ORGID", property = "orgid"),
            @Result(column = "ORGNAME", property = "orgname"),
            @Result(column = "ORGNUMBER", property = "orgnumber"),
            @Result(column = "ORGMENO", property = "orgmeno"),
            @Result(column = "MEMO", property = "memo"),
            @Result(column = "ORGTYPE", property = "orgtype"),
            @Result(column = "STATUS", property = "status"),
            @Result(column = "ISZY", property = "iszy"),
            @Result(column = "ISAUTONUMBER", property = "isautonumber")
    })
    List<TblOrganizationMySql> findByPageBean(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid, Find find);

    //@Select("select count(*) from TBL_ORGANIZATION org where ORGTYPE != 0 START WITH ORG.FATHERORGID = #{orgid} CONNECT BY PRIOR ORG.ORGID = ORG.FATHERORGID")
    @SelectProvider(method = "findByPage", type = TblOrganizationMapperSqlMySqlConifg.class)
    Integer findByPage(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal orgid, Find find);

    @SelectProvider(method = "getNodesa", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> getNodesa(PageInfo<TblOrganizationMySql> pageInfo);

    @Select("select count(*) from tbl_organization")
    Integer getNode();

    @SelectProvider(method = "getNodesaNodeId", type = TblOrganizationMapperSqlMySqlConifg.class)
    List<TblOrganizationMySql> getNodesaNodeId(PageInfo<TblOrganizationMySql> pageInfo, BigDecimal nodeId);

    @Select("select * from tbl_organization where ORGID = #{nodeId}  and orgtype < 100 ORDER BY orderid ASC")
    Integer getNodeNodeId(BigDecimal nodeId);

    @UpdateProvider(method = "saveModiOrganization", type = TblOrganizationMapperSqlMySqlConifg.class)
    void saveModiOrganization(TblOrganizationMySql organization);

    @InsertProvider(method = "addReturnId", type = TblOrganizationMapperSqlMySqlConifg.class)
    @Options(useGeneratedKeys = true, keyProperty = "orgid", keyColumn = "ORGID")
    Integer addReturnId(TblOrganizationMySql organization);

    @UpdateProvider(method = "saveModiOrganiza", type = TblOrganizationMapperSqlMySqlConifg.class)
    Integer saveModiOrganiza(TblOrganizationMySql organization);

    @InsertProvider(method = "saveModiOrgan", type = TblOrganizationMapperSqlMySqlConifg.class)
    TblOrganizationMySql saveModiOrgan(TblOrganizationMySql o);

    @UpdateProvider(method = "updateZuZhi", type = TblOrganizationMapperSqlMySqlConifg.class)
    void updateZuZhi(TblOrganizationMySql organization);

    @InsertProvider(method = "saveAtionHangYe", type = TblOrganizationMapperSqlMySqlConifg.class)
    void saveAtionHangYe(TblOrganizationMySql ation);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{pid}")
    TblOrganizationMySql selectByPid(String pid);

    @UpdateProvider(method = "updateAtionHangYe", type = TblOrganizationMapperSqlMySqlConifg.class)
    void updateAtionHangYe(TblOrganizationMySql organization);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    TblOrganizationMySql selectOrgid(String orgid);

    @Select("select * from TBL_ORGANIZATION  t where t.fatherorgid = #{nodeId} and status = 0 order by t.orderid asc")
    List<Tree> getNodes(BigDecimal nodeId);

    @Select("select * from tbl_organization where ORGID = #{nodeId} and STATUS=0 and orgtype < 100 ORDER BY orderid ASC")
    List<TblOrganizationMySql> findByNode(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{orgid}")
    String selectFindOrgid(TblOrganizationMySql orgid);

    @Select("select * from TBL_ORGANIZATION  WHERE ORGTYPE <100 and orgid= #{orgid} and  status = 0 ORDER BY ORDERID ASC")
    List<TblOrganizationMySql> findByOrg(String orgid);

    @Select("select * from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
    List<TblOrganizationMySql> getRoot(BigDecimal orgid);

    @Select("select count(*) from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
    int getChildCount(BigDecimal orgid);

    @Select("select * from TBL_ORGANIZATION t where t.FATHERORGID = #{orgid} and t.STATUS = 0 and t.ORGTYPE !=0 and t.ORGTYPE < 100")
    List<TblOrganizationMySql> getChild(BigDecimal orgid);

    @Select("select* from TBL_ORGANIZATION t where t.fatherorgid = #{nodeId} order by t.orderid asc")
    List<TblOrganizationMySql> getNodesaGSNodeId(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION WHERE FIND_IN_SET(ORGID,#{result})")
    List<TblOrganizationMySql> findByResult(Object result);

    @Select("select ORGID,ORGNAME from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 and getOrgIdList(ORGID = #{orgid})")
    List<TblOrganizationMySql> findBysqlOrgid(String orgid);

    @Select("select * from TBL_ORGANIZATION  where orgtype < 100 and status=0 and orgtype !=0 and getOrgIdList(ORGID = #{orgid}) ")
    List<TblOrganizationMySql> findBysqlOrgids(String orgid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    TblOrganizationMySql findByoId(BigDecimal orgid);


    @Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGTYPE = ( SELECT MAX(ORGTYPE) FROM TBL_ORGANIZATION WHERE ORGTYPE != 0 AND ORGTYPE < 100 and getOrgIdList(ORGID = #{orgId})) AND  ORGTYPE != 0 AND ORGTYPE < 100 and getOrgIdList(ORGID = #{orgId}) ")
    List<TblOrganizationUtil> findByOrgId(String orgId);

    @Select("SELECT ISINITIALIZATION FROM TBL_ORGANIZATION WHERE ORGID = #{orgid}")
    String findIniStatus(BigDecimal orgid);

    @Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE ORGID = #{companyid}")
    String findByorgidname(BigDecimal companyid);

    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where 1=1 and o.orgtype = 100  " +
            " and getFatherList(o.fatherorgid = -1) ")
    List<TblOrganizationMySql> findByOrgis(String orgid);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) LIMIT 1,1")
    List<TblOrganizationMySql> findAllorganization();

    @Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} order by t.orderid asc")
    List<TblOrganizationMySql> getNodeId(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and orgtype = 100 ORDER BY orderid ASC")
    List<TblOrganizationMySql> findByNodeIdNew(BigDecimal nodeId);

    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid}")
    Set<TblOrganizationMySql> findByfatherorgId(BigDecimal orgid);

    @Select("select * from TBL_ORGANIZATION where 1=1 AND ORGTYPE<>0 and ORGTYPE<100 and STATUS=0 and getFatherList(FATHERORGID= #{nodeId})")
    List<TblOrganizationMySql> findByfather(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where FIND_IN_SET(ORGID,(SELECT getparentHyorgList (#{orgid})))")
    List<TblOrganizationMySql> findByorgid(String orgid);


    @Select("select * from TBL_ORGANIZATION where FATHERORGID = #{orgid} and ISZY = #{wpzjk}  ORDER BY orderid ASC LIMIT 1,1")
    TblOrganizationMySql findByWPZJK(BigDecimal orgid, String wpzjk);

    @Select("SELECT * FROM TBL_ORGANIZATION_INFO WHERE FATHERORGID = #{orgid} ")
	TblOrganizationMySql selectFatherOrgIdInfoByOrgId(BigDecimal orgid) throws Exception;


//    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where 1=1 and o.orgtype = 100  "+
//			" start with  o.fatherorgid = -1 connect by prior o.orgid= o.fatherorgid")
//    List<TblOrganization> optionHYOrgTree(BigDecimal orgid);


//	@Select("select * from TBL_ORGANIZATION where FATHERORGID = #{orgid} and ISZY = #{iszy} ORDER BY orderid ASC")
//	List<TblOrganization> selectWpzjk(String orgid, String iszy);
}
