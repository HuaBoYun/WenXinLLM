package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;

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
import com.huabo.system.entity.TblManageRight;

@Mapper
public interface TblManageRightDAO extends BaseMapper<TblManageRight> {

    @Select("select * from TBL_MANAGE_RIGHT where RIGHTID = #{id}")
    TblManageRight findById(BigDecimal id);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=-1 and RIGHTID in ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID= #{orgid})  and t.indicatorstatus= + 1")
    List<TblManageRight> getTreebyorgidRoots(String toString);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=#{pId} and t.indicatorstatus= 1")
    List<TblManageRight> getTreeByNodeId(BigDecimal pId);

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_BIMODULE")
    Integer selectRightCoun(BigDecimal attribute,String rightids);

    @Select("SELECT PAGEID FROM TBL_BI_REPORT_MENU WHERE PAGEBODY =  #{pageid} ")
    List<Object> selectChildrenListByFahterId(String pageid);

    @Select("SELECT COUNT(0) FROM TBL_SYSTEM_BIMODULE WHERE ORGID =  #{orgid}  AND PAGEID =  #{pageid}  AND MODULERIGHTID =  #{rightid} ")
    String selectSql();

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID =  #{orgid}  AND MODULERIGHTID =  #{rightid}  AND (PAGEID =  #{pageid}  OR PAGEBODY =  #{pageid} )")
    String deletePageId(String pageid);

    @Select("SELECT RIGHTID,RIGHTNAME FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = 1 AND INDICATORSTATUS = 1 AND RIGHTID NOT IN (102,101,103,252,570,124823,124850,2,70,235727) AND RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = #{orgid})")
    List<TblManageRight> selectRightListbyModule(BigDecimal orgid);

    @Select("select * from TBL_MANAGE_RIGHT where FATHERRIGHTID = #{tmplId} and rightid in (select rightid  from TBL_ORG_RIGHT where ORGID= #{orgid}) ORDER BY FUNCORDER DESC")
    List<TblManageRight> findBySql(BigDecimal tmplId, BigDecimal orgid);

    @Select("select count(*) from TBL_MANAGE_RIGHT where FATHERRIGHTID= #{rightid}")
    Integer selectChildrenList(BigDecimal rightid);

    @Select("select t1.RIGHTID,rc.RIGHTNAME RIGHTNAME1,t1.RIGHTURL,t1.FATHERRIGHTID ,t1.FUNCORDER,rc.INDICATORSTATUS,t1.RIGHTNAME,t1.RIGHTCONTENT  from TBL_MANAGE_RIGHT t1 " +
            "left join  TBL_ORG_RIGHT_NEW rc ON t1.RIGHTID = rc.RIGHTID where t1.FATHERRIGHTID = #{fatherrightid}  and rc.ORGID= #{orgid}")
    List<TblManageRight> OBJlistBySql(BigDecimal fatherrightid, BigDecimal orgid);

    @SelectProvider(method="selectWorkRightInfo",type=TblManageRightDAOSqlConfig.class)
    TblManageRight selectWorkRightInfo(String rightname);

    @Select("select t1.rightid,rc.rightname,t1.righturl,t1.fatherrightid ,t1.funcorder,rc.indicatorstatus,T1.RIGHTNAME RIGHTNAME1 " +
            "from TBL_MANAGE_RIGHT t1 " +
            "left join  TBL_ORG_RIGHT_NEW rc ON T1.RIGHTID=RC.RIGHTID " +
            "left join  TBL_MANAGE_USER_RIGHT t2 on T1.RIGHTID=T2.RIGHTID " +
            "where t2.STAFFID = #{userId} and rc.indicatorstatus = "+TblManageRight.ISSHOW_INDICATORSTATUS+"  and RC.ORGID= #{orgid}")
    List<Object[]> findListById(BigDecimal userId, BigDecimal orgid);

    @Select("SELECT COUNT(0) FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND PAGEID = #{pageid} AND MODULETYPE = #{moduleType}")
    Integer selectRightCount(@Param("orgid") BigDecimal orgid, @Param("pageid")String pageid, @Param("moduleType")String moduleType);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=-1   and t.indicatorstatus=1")
    List<TblManageRight> getTreeRoots(String staffid);

    @Select("select * from TBL_MANAGE_RIGHT t where  RIGHTID in ( SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID=#{orgid}) and t.indicatorstatus= 1")
    List<TblManageRight> getOrgRight(BigDecimal orgid);

    @Results({
    	@Result(column="RIGHTID",property="rightid"),
    	@Result(column="RIGHTNAME",property="rightname"),
    	@Result(column="RIGHTURL",property="righturl"),
    	@Result(column="FATHERRIGHTID",property="fatherrightid"),
    	@Result(column="FUNCORDER",property="funcorder"),
    	@Result(column="INDICATORSTATUS",property="indicatorstatus"),
    })
    @SelectProvider(method="selectChildrenRightListByUser",type=TblManageRightDAOSqlConfig.class)
	List<TblManageRight> selectChildrenRightListByUser(BigDecimal rightId, BigDecimal staffid, BigDecimal orgid) throws Exception;

    @InsertProvider(method="insertTblManageRight",type=TblManageRightDAOSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="rightid", keyColumn="RIGHTID")
	void insertTblManageRight(TblManageRight right);

    @UpdateProvider(method="updateTblManageRight",type=TblManageRightDAOSqlConfig.class)
	void updateTblManageRight(TblManageRight right) throws Exception;

    @Delete("${sql}")
	void executeDeleteSql(String sql);

    @Select("SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE RIGHTNAME = '合同管理'")
    String selectByRightName();

    @Select("SELECT COUNT(0) FROM TBL_ORG_RIGHT_NEW WHERE ORGID = #{orgid} AND RIGHTID = #{htRightId}")
    Integer selectCountHtrightId(BigDecimal orgid, String htRightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT_NEW(ORGID,RIGHTID,INDICATORSTATUS) VALUES(#{orgid},#{htRightId},1)")
    void insertRightNew(BigDecimal orgid, String htRightId);

    @Select("SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = #{htRightId}" )
    List selectRightIdsByParentId(String htRightId);

    @Update("UPDATE TBL_ORG_RIGHT_NEW SET INDICATORSTATUS = 1 WHERE ORGID = #{orgid} AND RIGHTID =#{htRightId}" )
    void UpdateRightNew(BigDecimal orgid, String htRightId);

    @Select("SELECT COUNT(0) FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{staffid} AND RIGHTID = #{htRightId}")
    Integer selectManageUserRight(BigDecimal staffid, String htRightId);

    @Delete("DELETE FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{userid}")
    void deleteUserId(String userid);

    @Insert("INSERT INTO TBL_MANAGE_USER_RIGHT (STAFFID,RIGHTID) VALUES ( #{userid}, #{rightId} )")
    void insertUserId(@Param("userid") String userid, @Param("rightId")String rightId);

    @Select("SELECT * FROM TBL_MANAGE_RIGHT where RIGHTID in (select RIGHTID from TBL_MANAGE_USER_RIGHT where STAFFID= #{userid})")
    List<TblManageRight> findByUserAll(String userid);

    @Select("SELECT * FROM TBL_ORG_RIGHT WHERE ORGID = #{id}")
    List<TblManageRight> findByorgid(BigDecimal id);

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND MODULETYPE = #{moduleType} AND (PAGEID = #{pageid} OR PAGEBODY =#{pageid})")
    void deleteBySystemBimodule(@Param("orgid")BigDecimal orgid, @Param("moduleType")String moduleType, @Param("pageid")String pageid);
    
    
    @Insert("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULETYPE,MODULERIGHTID) VALUES (#{orgid},#{pageid},#{moduleType},0)")
    void insertMangeRight(@Param("orgid")BigDecimal orgid, @Param("pageid")String pageid, @Param("moduleType")String moduleType);
    
    @Insert("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULETYPE, PAGEBODY,MODULERIGHTID) VALUES (#{orgid},#{childrenId},#{moduleType},#{pageid},0)")
    void insertMangeRightByPageBody(@Param("orgid")BigDecimal orgid, @Param("childrenId")Object childrenId, @Param("moduleType")String moduleType, @Param("pageid")String pageid);

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND PAGEID = #{pageId} AND MODULETYPE = #{module}")
	void deleteBiModule(@Param("module")String module,@Param("pageId") String pageId,@Param("orgid") BigDecimal orgid);

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND PAGEID IN (SELECT PAGEID FROM TBL_BI_REPORT_MENU WHERE PAGEBODY = #{pageId}) AND MODULETYPE = #{module} ")
	void deleteBiModuleChildren(@Param("module") String module,@Param("pageId") String pageId,@Param("orgid") BigDecimal orgid);

    @Delete("DELETE FROM TBL_BI_USER_PAGE WHERE PAGEID = #{pageId} AND STAFFID = #{staffid}")
	void deleteBiStaff(String staffid, String pageId) throws Exception;

    @Delete("DELETE FROM TBL_BI_USER_PAGE WHERE STAFFID = #{staffid} AND PAGEID IN (SELECT PAGEID FROM TBL_BI_REPORT_MENU WHERE PAGEBODY = #{pageId}) ")
	void deleteBiStaffChildren(String staffid, String pageId);
	
    
}
