package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeRecord;

public interface TblFeeRecordMapper extends BaseMapper<TblFeeRecord> {

    @Select("SELECT HBYUN_BILLING_RECORD_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    // Check duplicate billing: same user + same API URL within 10 seconds
    @Select("SELECT COUNT(*) FROM TBL_BILLING_RECORD WHERE STAFF_ID = #{staffId} AND API_URL = #{apiUrl} AND CREATE_TIME > #{timeThreshold}")
    int countRecentRecord(@Param("staffId") String staffId, @Param("apiUrl") String apiUrl, @Param("timeThreshold") Date timeThreshold);

    // Personal fee records (直接读表，小模块和页面已存入记录)
    @Select("<script>" +
            "SELECT * FROM TBL_BILLING_RECORD WHERE STAFF_ID = #{staffId}" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            "<if test='moduleType != null and moduleType != \"\"'> AND RIGHT_ID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE = #{moduleType})</if>" +
            "<if test='rightId != null'> AND RIGHT_ID = #{rightId}</if>" +
            "<if test='subModuleName != null and subModuleName != \"\"'> AND SUB_MODULE_NAME = #{subModuleName}</if>" +
            " ORDER BY CREATE_TIME DESC" +
            "</script>")
    List<TblFeeRecord> findPersonalRecords(@Param("staffId") String staffId,
                                            @Param("startTime") Date startTime,
                                            @Param("endTime") Date endTime,
                                            @Param("moduleType") String moduleType,
                                            @Param("rightId") BigDecimal rightId,
                                            @Param("subModuleName") String subModuleName);

    // Sum fee amount by staff
    @Select("<script>" +
            "SELECT COALESCE(SUM(FEE_AMOUNT), 0) FROM TBL_BILLING_RECORD WHERE STAFF_ID = #{staffId}" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            "</script>")
    BigDecimal sumFeeByStaff(@Param("staffId") String staffId,
                             @Param("startTime") Date startTime,
                             @Param("endTime") Date endTime);

    // Statistics: group by module for a company
    @Select("<script>" +
            "SELECT MODULE_NAME, COALESCE(SUM(FEE_AMOUNT), 0) AS TOTAL_FEE, COUNT(*) AS CALL_COUNT " +
            "FROM TBL_BILLING_RECORD WHERE COMPANY_ORG_ID = #{companyOrgId}" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            " GROUP BY MODULE_NAME ORDER BY TOTAL_FEE DESC" +
            "</script>")
    List<Map<String, Object>> statsByModuleForCompany(@Param("companyOrgId") BigDecimal companyOrgId,
                                                       @Param("startTime") Date startTime,
                                                       @Param("endTime") Date endTime);

    // Statistics: group by module for a group
    @Select("<script>" +
            "SELECT MODULE_NAME, COALESCE(SUM(FEE_AMOUNT), 0) AS TOTAL_FEE, COUNT(*) AS CALL_COUNT " +
            "FROM TBL_BILLING_RECORD WHERE GROUP_ORG_ID = #{groupOrgId}" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            " GROUP BY MODULE_NAME ORDER BY TOTAL_FEE DESC" +
            "</script>")
    List<Map<String, Object>> statsByModuleForGroup(@Param("groupOrgId") BigDecimal groupOrgId,
                                                     @Param("startTime") Date startTime,
                                                     @Param("endTime") Date endTime);

    // Statistics: group by module for a person
    @Select("<script>" +
            "SELECT MODULE_NAME, COALESCE(SUM(FEE_AMOUNT), 0) AS TOTAL_FEE, COUNT(*) AS CALL_COUNT " +
            "FROM TBL_BILLING_RECORD WHERE STAFF_ID = #{staffId}" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            " GROUP BY MODULE_NAME ORDER BY TOTAL_FEE DESC" +
            "</script>")
    List<Map<String, Object>> statsByModuleForPerson(@Param("staffId") String staffId,
                                                      @Param("startTime") Date startTime,
                                                      @Param("endTime") Date endTime);

    // Statistics: group by sub-module for a person (统计图用)
    @Select("<script>" +
            "SELECT SUB_MODULE_NAME, COALESCE(SUM(FEE_AMOUNT), 0) AS TOTAL_FEE, COUNT(*) AS CALL_COUNT " +
            "FROM TBL_BILLING_RECORD WHERE STAFF_ID = #{staffId} AND SUB_MODULE_NAME IS NOT NULL" +
            "<if test='startTime != null'> AND CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND CREATE_TIME &lt;= #{endTime}</if>" +
            "<if test='moduleType != null and moduleType != \"\"'> AND RIGHT_ID IN (SELECT ID FROM TBL_SYSTEM_RIGHT WHERE MODULETYPE = #{moduleType})</if>" +
            " GROUP BY SUB_MODULE_NAME ORDER BY TOTAL_FEE DESC" +
            "</script>")
    List<Map<String, Object>> statsBySubModuleForPerson(@Param("staffId") String staffId,
                                                         @Param("startTime") Date startTime,
                                                         @Param("endTime") Date endTime,
                                                         @Param("moduleType") String moduleType);

    // Drilldown: detail by sub-module within a module type
    @Select("<script>" +
            "SELECT sr.NAME AS RIGHT_NAME, fr.RIGHT_ID, COALESCE(SUM(fr.FEE_AMOUNT), 0) AS TOTAL_FEE, COUNT(*) AS CALL_COUNT " +
            "FROM TBL_BILLING_RECORD fr " +
            "INNER JOIN TBL_SYSTEM_RIGHT sr ON fr.RIGHT_ID = sr.ID " +
            "WHERE sr.MODULETYPE = #{moduleType}" +
            "<if test='companyOrgId != null'> AND fr.COMPANY_ORG_ID = #{companyOrgId}</if>" +
            "<if test='groupOrgId != null'> AND fr.GROUP_ORG_ID = #{groupOrgId}</if>" +
            "<if test='staffId != null and staffId != \"\"'> AND fr.STAFF_ID = #{staffId}</if>" +
            "<if test='startTime != null'> AND fr.CREATE_TIME &gt;= #{startTime}</if>" +
            "<if test='endTime != null'> AND fr.CREATE_TIME &lt;= #{endTime}</if>" +
            " GROUP BY sr.NAME, fr.RIGHT_ID ORDER BY TOTAL_FEE DESC" +
            "</script>")
    List<Map<String, Object>> drilldownByModule(@Param("moduleType") String moduleType,
                                                 @Param("companyOrgId") BigDecimal companyOrgId,
                                                 @Param("groupOrgId") BigDecimal groupOrgId,
                                                 @Param("staffId") String staffId,
                                                 @Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime);
}
