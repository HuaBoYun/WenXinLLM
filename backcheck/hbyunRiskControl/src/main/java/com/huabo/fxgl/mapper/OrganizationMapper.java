package com.huabo.fxgl.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.TblOrganization;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author kangjx
 * @since 2022-07-13
 */
@Repository
public interface OrganizationMapper extends BaseMapper<Organization> {

    /**
     * 模型当中用来获取大量列表的方法
     * 老系统方法:findOrgTreeObjByHY
     * @param orgid 编号
     */
    @Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,level from TBL_ORGANIZATION o inner join TBL_INDUSTRY_INNER i on o.orgid = i.orgid and i.industryid = #{orgid} where o.orgtype = 100  " +
            " start with  o.fatherorgid = -1 connect by prior o.orgid= o.fatherorgid")
    List<Organization> findOrganizationByOrgid(@Param("orgid") String orgid);

    /**
     * 用来生成模型左侧树
     * 行业模型库---》左侧菜单
     * @return
     */
    @Select("select ORGID,ORGNAME,FATHERORGID,ORGNUMBER,ORGMENO,MEMO,ICODE,ORGTYPE,level from TBL_ORGANIZATION where orgtype = 100 " +
            " start with  fatherorgid = -1 connect by prior orgid= fatherorgid")
    List<Organization> findOrganizationAll();

    /**
     * 行业模型库---》左侧菜单
     * 获取组织
     * @return
     */
    @Select("<script>" +
            "select * from TBL_ORGANIZATION where orgtype = #{orgtype}" +
            "<if test='hyzskType != null'> and HYZSKTYPE = #{hyzskType}</if>" +
            " and icode = #{orgid} and orgtype != 100 and (STATUS != 1 or STATUS IS NULL) start with  FATHERORGID = -1 connect by prior ORGID= FATHERORGID ORDER BY orgid ASC" +
            "</script>")
    List<Organization> findOrgTreeByOrgtypeAndOrgid(@Param("orgid")String orgid, @Param("orgtype")int orgtype, @Param("hyzskType")String hyzskType);

    @Select("select * from TBL_ORGANIZATION where orgtype < 100 and STATUS = 0  ORDER BY orderid ASC")
    List<Organization> query_orgtype();


    @Select("select orgname from TBL_ORGANIZATION  where orgid in (${param1}) and  status = 0  ORDER BY orderid ASC")
    List<String> findOrgInOrgId(String orgid);

    @Select("select * from tbl_organization where ORGID = ${param1}  and orgtype < 100 ORDER BY orderid ASC")
    List<Organization> getNodeAllbm(BigDecimal nodeId);

    /*queryWrapper.lt("ORGTYPE",100).eq("orgid",origd).eq("status",0).orderByAsc("orderid");*/
    @Select("select * from TBL_ORGANIZATION where ORGID=#{orgid} and status=0 and ORGTYPE<100")
    @ResultMap("RM_ORGANIZATION")
    List<Organization>selectByOrgId(String orgid);

    @Select("select * from TBL_ORGANIZATION where FATHERORGID=#{orgid} order by orderid")
    @ResultMap("RM_ORGANIZATION")
    List<Organization> selectChildOrgList(String orgid);

    @Select("select * from TBL_ORGANIZATION where FATHERORGID=#{orgid} order by orderid")
    List<Organization> selectbyfathidall(BigDecimal orgid);




    @Select("select * from TBL_ORGANIZATION where FATHERORGID=#{orgid} and status>1 order by orderid")
    @ResultMap("FS_ORGANIZATION")
    List<Organization> selectChildOrgTree(String orgid);

    /**
     * 查看详情
     * @param userName
     * @return
     */
    @Select("select * from TBL_ORGANIZATION t where status = 0 and orgid=(" +
            "select DISTINCT o.fatherorgid from TBL_STAFF s left join TBL_ORGANIZATION o " +
            "on s.orgid=o.orgid where s.username = #{param1}) order by orderid asc")
    List<Organization> findOrgBysql(String userName);
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = ( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 ) AND ROWNUM = 1")
    List<Organization> getHYFirst(QueryWrapper queryWrapper);
    @Select("select orgname from TBL_ORGANIZATION  where orgid in #{orgid} and  status = 0  ORDER BY orderid ASC")
    List findOrgByorgId(@Param("orgid") String orgid);




    @Select("select count(*) from TBL_ORGANIZATION where audittype = '1' and orgid = #{param1}")
    Integer countOrganization(String userOrgid);


    @Select("select * from tbl_organization where ORGID = #{Parma1} and STATUS=0 and orgtype < 100 ORDER BY orderid ASC")
    List<Organization> getNodeAllJT(BigDecimal nodeId);

    @Select("SELECT * FROM TBL_ORGANIZATION  t where t.FATHERORGID = #{nodeId} and STATUS = 0 order by t.ORDERID asc")
	List<TblOrganization> getNode(BigDecimal nodeId);

    @Select("select * from TBL_ORGANIZATION where ORGID = #{nodeId} and STATUS=0 and ORGTYPE < 100 ORDER BY orderid ASC")
	List<TblOrganization> findBysql(BigDecimal nodeId);
    
    @Select("SELECT * FROM TBL_ORGANIZATION WHERE FATHERORGID = #{orgid}")
    Set<TblOrganization> findByfatherorgId(BigDecimal orgid);
    
    
    @Select("SELECT * FROM TBL_ORGANIZATION")
    List<TblOrganization> getAllOrgid();



    @Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN (${orgId})  AND STATUS = 0 AND ORGTYPE < 100 AND ORGTYPE > 0")
    List<String> selectChildrenIdListByFatherOrgId(@Param("orgId")String orgId) throws Exception;

	@Select("SELECT * FROM TBL_ORGANIZATION WHERE ORGID = (SELECT FATHERORGID FROM TBL_ORGANIZATION WHERE ORGID =  #{orgid}  )")
	TblOrganization selectFatherOrgIdInfoByOrgId(BigDecimal orgid);
	
//	@Select("select orgid from tbl_organization where (orgid=#{orgid} or fatherorgid=#{orgid} ) and orgname=#{name}")
@Select("<script>" +
        "SELECT * FROM tbl_organization " +
        "WHERE 1=1 and " +
        "<if test= 'orgid !=null'>"+
        " orgid = #{orgid}" +
        "</if>"+
        "<if test= 'orgids !=null'>"+
        " (fatherorgid IN " +
        "<foreach collection='orgids' item='id' open='(' separator=',' close=')'>" +
        "#{id.orgid}" +
        "</foreach>" +
        " AND ORGTYPE = 0)" +
        "</if>"+
        "</script>")
	List<TblOrganization> getOrgidForOrgname(@Param("orgid")BigDecimal orgid,@Param("orgids")List<TblOrganization> orgids);

	List<Organization> getOrgNameByOrgids(@Param("ids")String[]  ids);

	List<Organization> getOrgIdsByNames(@Param("ids")String[]  ids,@Param("orgid")BigDecimal  orgid);
	
	
	List<Organization> getOrgIdsByNames2(@Param("ids")String[]  ids,@Param("orgids")List<BigDecimal>  orgids);

	
	@Select("select o.ORGID,o.ORGNAME,o.FATHERORGID,o.ORGNUMBER,o.ORGMENO,o.MEMO,o.ICODE,o.ORGTYPE,o.STATUS,o.ISZY from TBL_ORGANIZATION o where o.ORGTYPE = 0   START WITH o.ORGID = #{orgid} CONNECT by PRIOR o.ORGID=o.FATHERORGID  order BY o.ORGTYPE desc")
	List<Organization> getChildOrg(@Param("orgid")String  orgid);
	
	
	List<Organization> selectOrgList(@Param("staffids")String[]  staffids);
	
	@Select("select count(1) from tbl_organization where orgid in ( SELECT orgid FROM tbl_organization where orgtype=0 START WITH FATHERORGID =#{companyid} CONNECT BY PRIOR FATHERORGID=orgid  ) and orgid=#{orgid}")
	Integer checkDeptByOrgid(@Param("companyid")String  companyid,@Param("orgid")String  orgid);

	
}
