package com.huabo.compliance.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.compliance.mysql.entity.TblManageRightMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblManageRightMySqlDAO extends BaseMapper<TblManageRightMySql> {

    @Select("select * from TBL_MANAGE_RIGHT where RIGHTID = #{id}")
    TblManageRightMySql findById(BigDecimal id);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=-1 and RIGHTID in ( SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID= #{orgid})  and t.indicatorstatus= + 1")
    List<TblManageRightMySql> getTreebyorgidRoots(String toString);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=#{pId} and t.indicatorstatus= 1")
    List<TblManageRightMySql> getTreeByNodeId(BigDecimal pId);

    //    @Select("select TMR.RIGHTID,TMR.RIGHTNAME FROM TBL_MANAGE_RIGHT TMR WHERE TMR.FATHERRIGHTID = 1 AND TMR.RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID = #{ORGID} AND INDICATORSTATUS = 1) AND RIGHTID NOT IN (570,101,252) " +
//            "AND RIGHTNAME like '%RIGHTNAME%' " +
//            "ORDER BY TMR.FUNCORDER ASC , TMR.RIGHTID ASC")
    @SelectProvider(method = "selectListSql", type = TblManageRightDAOSqlMySqlConfig.class)
    List<TblManageRightMySql> selectListSql(PageInfo<TblManageRightMySql> pageInfo, BigDecimal orgid);

    //
//    @Select("select COUNT(*)  FROM TBL_MANAGE_RIGHT TMR WHERE TMR.FATHERRIGHTID = 1 AND TMR.RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID = #{ORGID} AND INDICATORSTATUS = 1) AND RIGHTID NOT IN (570,101,252) " +
//            "AND RIGHTNAME like '%RIGHTNAME%'" +
//            "ORDER BY TMR.FUNCORDER ASC , TMR.RIGHTID ASC")
    @SelectProvider(method = "countRightid", type = TblManageRightDAOSqlMySqlConfig.class)
    Integer countRightid(PageInfo<TblManageRightMySql> pageInfo, BigDecimal rightid);

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_BIMODULE")
    Integer selectRightCoun(BigDecimal attribute, String rightids);

//    @Insert("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULERIGHTID) VALUES (#{orgid}, #{pageid}, #{rightid})")
//    void executeSql(String sql, Object[] objects);

    @Select("SELECT PAGEID FROM TBL_BI_REPORT_MENU WHERE PAGEBODY =  #{pageid} ")
    List<Object> selectChildrenListByFahterId(String pageid);

    @Select("SELECT COUNT(0) FROM TBL_SYSTEM_BIMODULE WHERE ORGID =  #{orgid}  AND PAGEID =  #{pageid}  AND MODULERIGHTID =  #{rightid} ")
    String selectSql();


    @Insert("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULERIGHTID) VALUES (#{orgid}, #{pageid}, #{rightid})")
    String insertOrgidPAGEIDMODULERIGHTID();

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID =  #{orgid}  AND MODULERIGHTID =  #{rightid}  AND (PAGEID =  #{pageid}  OR PAGEBODY =  #{pageid} )")
    String deletePageId(String pageid);


    @Select("SELECT RIGHTID,RIGHTNAME FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = 1 AND INDICATORSTATUS = 1 AND RIGHTID NOT IN (102,101,103,252,570,124823,124850,2,70,235727) AND RIGHTID IN (SELECT RIGHTID FROM TBL_ORG_RIGHT WHERE ORGID = #{orgid})")
    List<TblManageRightMySql> selectRightListbyModule(BigDecimal orgid);

    @Select("select * from TBL_MANAGE_RIGHT where FATHERRIGHTID = #{tmplId} and rightid in (select rightid  from TBL_ORG_RIGHT where ORGID= #{orgid}) ORDER BY FUNCORDER DESC")
    List<TblManageRightMySql> findBySql(BigDecimal tmplId, BigDecimal orgid);

    @Select("select count(*) from TBL_MANAGE_RIGHT where FATHERRIGHTID= #{rightid}")
    Integer selectChildrenList(BigDecimal rightid);

    @Select("select t1.RIGHTID,rc.RIGHTNAME RIGHTNAME1,t1.RIGHTURL,t1.FATHERRIGHTID ,t1.FUNCORDER,rc.INDICATORSTATUS,t1.RIGHTNAME,t1.RIGHTCONTENT  from TBL_MANAGE_RIGHT t1 " +
            "left join  TBL_ORG_RIGHT_NEW rc ON t1.RIGHTID = rc.RIGHTID where t1.FATHERRIGHTID = #{fatherrightid}  and rc.ORGID= #{orgid}")
    List<TblManageRightMySql> OBJlistBySql(BigDecimal fatherrightid, BigDecimal orgid);

    @Select("SELECT NEW TblManageRight(rightid,rightname,righturl) from TblManageRight WHERE rightname = #{rightname} and fatherrightid=1  AND indicatorstatus = 1 limit 1")
    TblManageRightMySql selectWorkRightInfo(String rightname);

    @Select("select t1.rightid,rc.rightname,t1.righturl,t1.fatherrightid ,t1.funcorder,rc.indicatorstatus,T1.RIGHTNAME RIGHTNAME1 " +
            "from TBL_MANAGE_RIGHT t1 " +
            "left join  TBL_ORG_RIGHT_NEW rc ON T1.RIGHTID=RC.RIGHTID " +
            "left join  TBL_MANAGE_USER_RIGHT t2 on T1.RIGHTID=T2.RIGHTID " +
            "where t2.STAFFID = #{userId} and rc.indicatorstatus = " + TblManageRightMySql.ISSHOW_INDICATORSTATUS + "  and RC.ORGID= #{orgid}")
    List<Object[]> findListById(BigDecimal userId, BigDecimal orgid);

    @Insert("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULERIGHTID) VALUES (#{orgid}, #{pageid}, #{rightid})")
    void executeSql(BigDecimal orgid, Object childrenId, String rightid, String pageid);

    @Select("SELECT COUNT(0) FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND PAGEID = #{pageid} AND MODULERIGHTID = #{rightid}")
    Integer selectRightCount(@Param("orgid") BigDecimal orgid, @Param("pageid") String pageid, @Param("rightid") String rightid);

    @Select("select * from TBL_MANAGE_RIGHT t where t.fatherrightid=-1   and t.indicatorstatus=1")
    List<TblManageRightMySql> getTreeRoots(String staffid);

    @Select("select * from TBL_MANAGE_RIGHT t where  RIGHTID in ( SELECT RIGHTID FROM TBL_ORG_RIGHT_NEW WHERE ORGID=#{orgid}) and t.indicatorstatus= 1")
    List<TblManageRightMySql> getOrgRight(BigDecimal orgid);

    @Select("SELECT TMR.RIGHTID,NVL(TORN.RIGHTNAME, TMR.RIGHTNAME) RIGHTNAME,TMR.RIGHTURL,TMR.FATHERRIGHTID,TMR.FUNCORDER,TMR.INDICATORSTATUS FROM TBL_MANAGE_RIGHT TMR LEFT JOIN TBL_MANAGE_USER_RIGHT TMUR ON TMR.RIGHTID = TMUR.RIGHTID LEFT JOIN TBL_ORG_RIGHT_NEW TORN ON TMR.RIGHTID = TORN.RIGHTID AND TORN.ORGID = #{orgid} WHERE TMR.INDICATORSTATUS = 1 AND TMR.FATHERRIGHTID = #{rightId} AND TORN.INDICATORSTATUS = 1 AND TMUR.STAFFID = #{staffid} ORDER BY FUNCORDER ASC")
    @Results({
            @Result(column = "RIGHTID", property = "rightid"),
            @Result(column = "RIGHTNAME", property = "rightname"),
            @Result(column = "RIGHTURL", property = "righturl"),
            @Result(column = "FATHERRIGHTID", property = "fatherrightid"),
            @Result(column = "FUNCORDER", property = "funcorder"),
            @Result(column = "INDICATORSTATUS", property = "indicatorstatus"),
    })
    List<TblManageRightMySql> selectChildrenRightListByUser(Integer rightId, BigDecimal staffid, BigDecimal orgid) throws Exception;

    @InsertProvider(method = "insertTblManageRight", type = TblManageRightDAOSqlMySqlConfig.class)
    @Options(useGeneratedKeys = true, keyProperty = "rightid", keyColumn = "RIGHTID")
    void insertTblManageRight(TblManageRightMySql right);

    @UpdateProvider(method = "updateTblManageRight", type = TblManageRightDAOSqlMySqlConfig.class)
    void updateTblManageRight(TblManageRightMySql right) throws Exception;

    @Delete("${sql}")
    void executeDeleteSql(String sql);

    @Select("SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE RIGHTNAME = '合同管理'")
    String selectByRightName();

    @Select("SELECT COUNT(0) FROM TBL_ORG_RIGHT_NEW WHERE ORGID = #{orgid} AND RIGHTID = #{htRightId}")
    Integer selectCountHtrightId(BigDecimal orgid, String htRightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT_NEW(ORGID,RIGHTID,INDICATORSTATUS) VALUES(#{orgid},#{htRightId},1)")
    void insertRightNew(BigDecimal orgid, String htRightId);

    @Select("SELECT RIGHTID FROM TBL_MANAGE_RIGHT WHERE FATHERRIGHTID = #{htRightId}")
    List selectRightIdsByParentId(String htRightId);

    @Update("UPDATE TBL_ORG_RIGHT_NEW SET INDICATORSTATUS = 1 WHERE ORGID = #{orgid} AND RIGHTID =#{htRightId}")
    void UpdateRightNew(BigDecimal orgid, String htRightId);

    @Select("SELECT COUNT(0) FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{staffid} AND RIGHTID = #{htRightId}")
    Integer selectManageUserRight(BigDecimal staffid, String htRightId);

    @Delete("DELETE FROM TBL_MANAGE_USER_RIGHT WHERE STAFFID = #{userid}")
    void deleteUserId(String userid);

    @Insert("INSERT INTO TBL_MANAGE_USER_RIGHT (STAFFID,RIGHTID) VALUES ( #{userid}, #{rightId} )")
    void insertUserId(@Param("userid") String userid, @Param("rightId") String rightId);

    @Select("SELECT * FROM TBL_MANAGE_RIGHT where RIGHTID in (select RIGHTID from TBL_MANAGE_USER_RIGHT where STAFFID= #{userid})")
    List<TblManageRightMySql> findByUserAll(String userid);

    @Select("SELECT * FROM TBL_ORG_RIGHT WHERE ORGID = #{id}")
    List<TblManageRightMySql> findByorgid(BigDecimal id);

    @Delete("DELETE FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND MODULERIGHTID = #{rightid} AND (PAGEID = #{pageid} OR PAGEBODY =#{pageid})")
    void deleteBySystemBimodule(@Param("orgid") BigDecimal orgid, @Param("rightid") String rightid, @Param("pageid") String pageid);

    @Select("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULERIGHTID) VALUES (#{orgid},#{pageid},#{rightid})")
    void insertMangeRight(@Param("orgid") BigDecimal orgid, @Param("pageid") String pageid, @Param("rightid") String rightid);

    @Select("INSERT INTO TBL_SYSTEM_BIMODULE(ORGID, PAGEID, MODULERIGHTID, PAGEBODY) VALUES (#{orgid},#{childrenId},#{rightid},#{pageid})")
    void insertMangeRightByPageBody(@Param("orgid") BigDecimal orgid, @Param("childrenId") Object childrenId, @Param("rightid") String rightid, @Param("pageid") String pageid);

}
