package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblOrganization;
import org.apache.ibatis.annotations.*;

import com.huabo.audit.util.*;
import com.huabo.audit.oracle.entity.TblNbsjWbProject;
import com.huabo.audit.oracle.vo.TblZgzzProjectVo;


public interface TblNbsjWbProjectMapper extends tk.mybatis.mapper.common.Mapper<TblNbsjWbProject> {
	
	@Select("SELECT * from TBL_NBSJ_WBPROJECT WHERE PROJECTID= #{projectid} ")
    TblNbsjWbProject getById(String projectid);

    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjWbProjectMapperSqlConfig.class)
	Integer selectCountByPageInfo(PageInfo<TblNbsjWbProject> pageInfo) throws Exception;
    
    @Select("SELECT  *   FROM TBL_NBSJ_WBPROJECT   WHERE PROJECTID = #{projectid}")
   	TblNbsjWbProject selectById(Integer projectid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjWbProjectMapperSqlConfig.class)
	List<TblNbsjWbProject> selectListByPageInfo(PageInfo<TblNbsjWbProject> pageInfo) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_WBPROJECT WHERE PROJECTID = #{projectid}")
    void deleteById(Integer projectid) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_WBPROJECT WHERE PROJECTID = #{projectId}")
    void deleteByIssuesId(BigDecimal projectId) throws Exception;
    
    @Select("select * from TBL_NBSJ_WBPROJECT where PROJECTNAME= #{projectname} AND PROJECTTYPE = #{projectType}")
   	List<TblNbsjWbProject> findNbsjxmBynameAll(String projectname,Integer projectType) throws Exception;

    @SelectProvider(method="selectPageInfoListByRectification",type=TblNbsjWbProjectMapperSqlConfig.class)
    @Results({
    	@Result(column = "PROJECTID",property = "planId"),
    	@Result(column = "PROJECTCODE",property = "planCode"),
    	@Result(column = "PROJECTNAME",property = "planName"),
    })
	List<TblZgzzProjectVo> selectPageInfoListByRectification(TblZgzzProjectVo project, Integer planType);

    @Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE ORGNAME = #{org} FETCH FIRST 1 ROWS ONLY;")
    BigDecimal selectauditOrgId(String org);
    @Select("SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID =#{org} AND ORGNAME = #{dept} FETCH FIRST 1 ROWS ONLY")
    BigDecimal selectauditDeptId(@Param("org")BigDecimal org,@Param("dept")String dept);
    @Select("SELECT STAFFID FROM TBL_STAFF WHERE REALNAME = #{staff} AND ORGID = #{org} FETCH FIRST 1 ROWS ONLY")
    BigDecimal selectauditStaffId(@Param("org")BigDecimal org,@Param("staff")String staff);

    @Select("<script>"+
            "SELECT STAFFID FROM TBL_STAFF WHERE ORGID IN " +
            "(SELECT ORGID FROM TBL_ORGANIZATION WHERE FATHERORGID IN " +
            "<foreach collection='orgids' item='id' open='(' separator=',' close=')'>"+
            "#{id}"+
            "</foreach>"+
            ")AND REALNAME = #{staff} FETCH FIRST 1 ROWS ONLY"+
            "</script>")
    BigDecimal selectauditStaffIds(@Param("orgids")List<BigDecimal> orgids,@Param("staff")String staff);

    @Select("<script>"+
            "SELECT orgid FROM TBL_ORGANIZATION WHERE FATHERORGID IN "+
            "<foreach collection='orgids' item='id' open='(' separator=',' close=')'>"+
            "#{id}"+
            "</foreach>"+
            "</script>")
    List<BigDecimal> getOrgidForOrgname(@Param("orgids") List<BigDecimal> orgids);


    @Select("<script>" +
            "SELECT * FROM TBL_ORGANIZATION " +
            "WHERE 1=1 and " +
            "<if test= 'orgids !=null and orgids.size() > 0'>"+
            " (fatherorgid IN " +
            "<foreach collection='orgids' item='id' open='(' separator=',' close=')'>" +
            "#{id.orgid}" +
            "</foreach>" +
            " AND ORGTYPE = 0)" +
            "</if>"+
            "</script>")
    List<TblOrganization> getOrgidForOrgname2( @Param("orgids")List<TblOrganization> orgids);

    @Select("<script>" +
            "SELECT * FROM TBL_ORGANIZATION " +
            "WHERE 1=1 and " +
            "<if test= 'orgid !=null'>"+
            " orgid = #{orgid}" +
            "</if>"+
            "</script>")
    List<TblOrganization> getOrgidForOrgname1(@Param("orgid")BigDecimal orgid);
}
