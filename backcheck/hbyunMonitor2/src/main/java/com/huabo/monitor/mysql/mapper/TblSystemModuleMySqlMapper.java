package com.huabo.monitor.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.mysql.entity.TblSystemModelFlowMySql;
import com.huabo.monitor.mysql.entity.TblSystemModuleMySql;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemModuleMySqlMapper extends BaseMapper<TblSystemModuleMySql> {


    @SelectProvider(method = "selectPageInfoByOrgId", type = TblSystemModuleMapperSqlMySqlConifg.class)
    List<TblSystemModuleMySql> selectPageInfoByOrgId(PageInfo<TblSystemModuleMySql> pageInfo, BigDecimal orgid, TblSystemModuleMySql module);


    @SelectProvider(method = "selectCountByPageInfo", type = TblSystemModuleMapperSqlMySqlConifg.class)
    Integer selectCountByPageInfo(BigDecimal orgid, TblSystemModuleMySql module);


    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_MODULE M LEFT JOIN TBL_ORGANIZATION O ON M.MODELORG = O.ORGID WHERE M.MODELID != #{modelId} AND M.MODELNAME = #{modelName} AND O.ORGID = #{orgid}")
    Integer selectUpdateCount(BigDecimal orgid, Integer modelId, String modelName);

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_MODULE M LEFT JOIN TBL_ORGANIZATION O ON M.MODELORG = O.ORGID WHERE (M.MODELNO = #{modelNo} OR M.MODELNAME = #{modelName}) AND O.ORGID = #{orgid}")
    Integer selectInsertCount(BigDecimal orgid, String modelNo, String modelName);

    @Update("UPDATE TBL_SYSTEM_MODULE SET MODELSTATUS = #{moduleStatus} WHERE MODELID = #{moduleId}")
    void updateByModuleId(Integer moduleStatus, Integer moduleId);

    @Select("SELECT MODELTYPE FROM TBL_SYSTEM_MODULE WHERE MODELID = #{moduleId}")
    String selectByModuleId(Integer moduleId);

    @Select("SELECT COUNT(0) FROM TBL_ORG_RIGHT WHERE ORGID = #{id} AND RIGHTID = #{rightId}")
    Integer selectOrgManageRightId(String id, String rightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) VALUES (#{id},#{rightId})")
    void insertModule(String id, String rightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) SELECT #{id} AS ORGID,RIGHTID FROM TBL_MANAGE_RIGHT and getRightList(RIGHTID = #{rightId}) ")
    void insertModuleByrightId(String id, String rightId);

    @Delete("DELETE FROM TBL_SYSTEM_MODELORG WHERE ORGID = #{orgId} AND MODELID = #{moduleId}")
    void deleteByIdAndModuleId(Integer moduleId, String orgId);

    @Insert("INSERT INTO TBL_SYSTEM_MODELORG(MODELID,ORGID) VALUES(#{moduleId},#{orgId})")
    void insertByIdAndModuleId(Integer moduleId, String orgId);

//    @Delete("DELETE FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId} AND ORGID IN (#{orgId})")
//    void deleteBymoduleIdAndOrgId(Integer moduleId, String orgId);

    @Delete("\tDELETE FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId} AND ORGID = #{orgId}")
    void deleteBymoduleIdAndOrgId(Integer moduleId, String orgId);

    @Select("SELECT * FROM TBL_SYSTEM_MODULE WHERE MODELID = #{moduleId}")
    TblSystemModuleMySql selectBymoduleId(Integer moduleId);

    @InsertProvider(method = "insertSystemModule", type = TblSystemModuleMapperSqlMySqlConifg.class)
    void insertSystemModule(TblSystemModuleMySql module);

    @Delete("DELETE FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId}")
    void deleteByModulId(Integer modelId);

    @UpdateProvider(method = "updateByModule", type = TblSystemModuleMapperSqlMySqlConifg.class)
    void updateByModule(TblSystemModuleMySql oldModule);

    @Delete("DELETE FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId} and FLOWID= #{flowid}")
    void removeModelFlowRelation(@Param("flowid") String flowid, @Param("modelId") Integer modelId);

    @Select("SELECT * FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId}")
    List<TblSystemModelFlowMySql> findFlowByModule(Integer modelId);


}
