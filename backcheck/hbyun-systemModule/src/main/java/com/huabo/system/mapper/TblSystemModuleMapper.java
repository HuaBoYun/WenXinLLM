package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblAccBook;
import com.huabo.system.entity.TblAcctBook;
import com.huabo.system.entity.TblSystemModelFlow;
import com.huabo.system.entity.TblSystemModule;

import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface TblSystemModuleMapper extends BaseMapper<TblSystemModule> {

    @SelectProvider(method="selectPageInfoByOrgId",type=TblSystemModuleMapperSqlConifg.class)
    IPage<TblSystemModule> selectPageInfoByOrgId(IPage<TblSystemModule> page, BigDecimal orgid,TblSystemModule module);

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_MODULE M LEFT JOIN TBL_ORGANIZATION O ON M.MODELORG = O.ORGID WHERE M.MODELID != #{modelId} AND M.MODELNAME = #{modelName} AND O.ORGID = #{orgid}")
    Integer selectUpdateCount(BigDecimal orgid, BigDecimal modelId, String modelName);

    @Select("SELECT COUNT(*) FROM TBL_SYSTEM_MODULE M LEFT JOIN TBL_ORGANIZATION O ON M.MODELORG = O.ORGID WHERE (M.MODELNO = #{modelNo} OR M.MODELNAME = #{modelName}) AND O.ORGID = #{orgid}")
    Integer selectInsertCount(BigDecimal orgid, String modelNo, String modelName);

    @Update("UPDATE TBL_SYSTEM_MODULE SET MODELSTATUS = #{moduleStatus} WHERE MODELID = #{moduleId}")
    void updateByModuleId(Integer moduleStatus, BigDecimal moduleId);

    @Select("SELECT MODELTYPE FROM TBL_SYSTEM_MODULE WHERE MODELID = #{moduleId}")
    String selectByModuleId(BigDecimal moduleId);

    @Select("SELECT COUNT(0) FROM TBL_ORG_RIGHT WHERE ORGID = #{id} AND RIGHTID = #{rightId}")
    Integer selectOrgManageRightId(String id, String rightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) VALUES (#{id},#{rightId})")
    void insertModule(String id, String rightId);

    @Insert("INSERT INTO TBL_ORG_RIGHT (ORGID,RIGHTID) SELECT #{id} AS ORGID,RIGHTID FROM TBL_MANAGE_RIGHT START WITH RIGHTID = #{rightId} CONNECT BY PRIOR RIGHTID = FATHERRIGHTID")
    void insertModuleByrightId(String id, String rightId);

    @Delete("DELETE FROM TBL_SYSTEM_MODELORG WHERE ORGID = #{orgId} AND MODELID = #{moduleId}")
    void deleteByIdAndModuleId(BigDecimal moduleId, String orgId);

    @Insert("INSERT INTO TBL_SYSTEM_MODELORG(MODELID,ORGID) VALUES(#{moduleId},#{orgId})")
    void insertByIdAndModuleId(BigDecimal moduleId, String orgId);

//    @Delete("DELETE FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId} AND ORGID IN (#{orgId})")
//    void deleteBymoduleIdAndOrgId(Integer moduleId, String orgId);

    @Delete("DELETE FROM TBL_SYSTEM_MODELORG WHERE MODELID = #{moduleId} AND ORGID IN (#{orgId}) IS NOT NULL)")
    void deleteBymoduleIdAndOrgId(@Param("moduleId")BigDecimal moduleId,@Param("orgId") String orgId);

    @Select("SELECT * FROM TBL_SYSTEM_MODULE WHERE MODELID = #{moduleId}")
    TblSystemModule selectBymoduleId(BigDecimal moduleId);

    @InsertProvider(method="insertSystemModule",type=TblSystemModuleMapperSqlConifg.class)
    void insertSystemModule(TblSystemModule module);

    @Delete("DELETE FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId}")
    void deleteByModulId(BigDecimal modelId);

    @UpdateProvider(method="updateByModule",type=TblSystemModuleMapperSqlConifg.class)
    void updateByModule(TblSystemModule oldModule);

    @Delete("DELETE FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId} and FLOWID= #{flowid}")
    void removeModelFlowRelation(@Param("flowid") String flowid, @Param("modelId")Integer modelId);

    @Select("SELECT * FROM TBL_SYSTEM_MODELFLOW WHERE MODELID = #{modelId}")
    List<TblSystemModelFlow> findFlowByModule(BigDecimal modelId);


}
