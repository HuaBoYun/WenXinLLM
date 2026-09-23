package com.management.accountant.mapper.ncv65;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ncv65.BudgetVersion;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算版本Mapper接口
 * 
 * @description 预算版本数据访问层，支持版本控制和历史追溯
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
@Mapper
public interface BudgetVersionMapper extends BaseMapper<BudgetVersion> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据版本编码查询预算版本
     * @param versionCode 版本编码
     * @param tenantId 租户ID
     * @return 预算版本信息
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE VERSION_CODE = #{versionCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVersion selectByVersionCode(@Param("versionCode") String versionCode, @Param("tenantId") String tenantId);

    /**
     * 根据版本名称查询预算版本
     * @param versionName 版本名称
     * @param tenantId 租户ID
     * @return 预算版本信息
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE VERSION_NAME = #{versionName} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVersion selectByVersionName(@Param("versionName") String versionName, @Param("tenantId") String tenantId);

    /**
     * 根据预算年度查询版本列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectByFiscalYear(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 根据预算模型ID查询版本列表
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE MODEL_ID = #{modelId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectByModelId(@Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 根据版本类型查询版本列表
     * @param versionType 版本类型
     * @param tenantId 租户ID
     * @return 版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE VERSION_TYPE = #{versionType} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectByVersionType(@Param("versionType") String versionType, @Param("tenantId") String tenantId);

    /**
     * 根据审批状态查询版本列表
     * @param approvalStatus 审批状态
     * @param tenantId 租户ID
     * @return 版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE APPROVAL_STATUS = #{approvalStatus} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus, @Param("tenantId") String tenantId);

    // ==================== 复杂查询方法 ====================

    /**
     * 分页查询预算版本
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetVersion> selectBudgetVersionPage(Page<BudgetVersion> page, @Param("params") Map<String, Object> params);

    /**
     * 查询当前版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 当前版本
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND MODEL_ID = #{modelId} AND IS_CURRENT = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVersion selectCurrentVersion(@Param("fiscalYear") Integer fiscalYear, @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 查询基准版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 基准版本
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND MODEL_ID = #{modelId} AND IS_BASELINE = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVersion selectBaselineVersion(@Param("fiscalYear") Integer fiscalYear, @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 查询最终版本
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 最终版本
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND MODEL_ID = #{modelId} AND IS_FINAL = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    BudgetVersion selectFinalVersion(@Param("fiscalYear") Integer fiscalYear, @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 查询已发布的版本列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 已发布的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND IS_PUBLISHED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY PUBLISH_TIME DESC")
    List<BudgetVersion> selectPublishedVersions(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询锁定的版本列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 锁定的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND IS_LOCKED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY LOCK_TIME DESC")
    List<BudgetVersion> selectLockedVersions(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询启用的版本列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 启用的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND IS_ENABLED = 1 AND STATUS = 'active' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectEnabledVersions(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询归档的版本列表
     * @param fiscalYear 预算年度
     * @param tenantId 租户ID
     * @return 归档的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE FISCAL_YEAR = #{fiscalYear} AND IS_ARCHIVED = 1 AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY ARCHIVE_TIME DESC")
    List<BudgetVersion> selectArchivedVersions(@Param("fiscalYear") Integer fiscalYear, @Param("tenantId") String tenantId);

    /**
     * 查询版本历史记录
     * @param versionCode 版本编码
     * @param tenantId 租户ID
     * @return 版本历史记录
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE VERSION_CODE = #{versionCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY MAJOR_VERSION DESC, MINOR_VERSION DESC, PATCH_VERSION DESC")
    List<BudgetVersion> selectVersionHistory(@Param("versionCode") String versionCode, @Param("tenantId") String tenantId);

    /**
     * 查询最新版本号
     * @param versionCode 版本编码
     * @param tenantId 租户ID
     * @return 最新版本
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE VERSION_CODE = #{versionCode} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY MAJOR_VERSION DESC, MINOR_VERSION DESC, PATCH_VERSION DESC LIMIT 1")
    BudgetVersion selectLatestVersion(@Param("versionCode") String versionCode, @Param("tenantId") String tenantId);

    /**
     * 查询我创建的版本列表
     * @param createBy 创建人ID
     * @param tenantId 租户ID
     * @return 我创建的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE CREATE_BY = #{createBy} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectMyVersions(@Param("createBy") String createBy, @Param("tenantId") String tenantId);

    /**
     * 查询待审批的版本列表
     * @param approverId 审批人ID
     * @param tenantId 租户ID
     * @return 待审批的版本列表
     */
    @Select("SELECT * FROM BUDGET_VERSION WHERE APPROVER_ID = #{approverId} AND APPROVAL_STATUS = 'submitted' AND TENANT_ID = #{tenantId} AND IS_DELETED = 0 ORDER BY CREATE_TIME DESC")
    List<BudgetVersion> selectPendingApprovalVersions(@Param("approverId") String approverId, @Param("tenantId") String tenantId);

    // ==================== 统计查询方法 ====================

    /**
     * 统计版本总数
     * @param tenantId 租户ID
     * @return 版本总数
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VERSION WHERE TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int countTotalVersions(@Param("tenantId") String tenantId);

    /**
     * 按版本类型统计数量
     * @param tenantId 租户ID
     * @return 各类型版本数量统计
     */
    List<Map<String, Object>> countVersionsByType(@Param("tenantId") String tenantId);

    /**
     * 按审批状态统计数量
     * @param tenantId 租户ID
     * @return 各审批状态版本数量统计
     */
    List<Map<String, Object>> countVersionsByApprovalStatus(@Param("tenantId") String tenantId);

    /**
     * 按年度统计版本数量
     * @param tenantId 租户ID
     * @return 各年度版本数量统计
     */
    List<Map<String, Object>> countVersionsByYear(@Param("tenantId") String tenantId);

    /**
     * 获取版本统计信息
     * @param tenantId 租户ID
     * @return 统计信息
     */
    Map<String, Object> selectVersionStatistics(@Param("tenantId") String tenantId);

    /**
     * 获取用户版本统计信息
     * @param userId 用户ID
     * @param tenantId 租户ID
     * @return 用户版本统计信息
     */
    Map<String, Object> selectUserVersionStatistics(@Param("userId") String userId, @Param("tenantId") String tenantId);

    // ==================== 业务操作方法 ====================

    /**
     * 发布版本
     * @param versionId 版本ID
     * @param publishedBy 发布人ID
     * @param publishedByName 发布人姓名
     * @param publishTime 发布时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VERSION SET IS_PUBLISHED = 1, PUBLISHED_BY = #{publishedBy}, PUBLISHED_BY_NAME = #{publishedByName}, PUBLISH_TIME = #{publishTime} WHERE ID = #{versionId}")
    int publishVersion(@Param("versionId") String versionId, @Param("publishedBy") String publishedBy, 
                      @Param("publishedByName") String publishedByName, @Param("publishTime") LocalDateTime publishTime);

    /**
     * 取消发布版本
     * @param versionId 版本ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VERSION SET IS_PUBLISHED = 0, PUBLISHED_BY = NULL, PUBLISHED_BY_NAME = NULL, PUBLISH_TIME = NULL WHERE ID = #{versionId}")
    int unpublishVersion(@Param("versionId") String versionId);

    /**
     * 锁定版本
     * @param versionId 版本ID
     * @param lockedBy 锁定人ID
     * @param lockedByName 锁定人姓名
     * @param lockTime 锁定时间
     * @param lockReason 锁定原因
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VERSION SET IS_LOCKED = 1, LOCKED_BY = #{lockedBy}, LOCKED_BY_NAME = #{lockedByName}, LOCK_TIME = #{lockTime}, LOCK_REASON = #{lockReason} WHERE ID = #{versionId}")
    int lockVersion(@Param("versionId") String versionId, @Param("lockedBy") String lockedBy, 
                   @Param("lockedByName") String lockedByName, @Param("lockTime") LocalDateTime lockTime, @Param("lockReason") String lockReason);

    /**
     * 解锁版本
     * @param versionId 版本ID
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VERSION SET IS_LOCKED = 0, LOCKED_BY = NULL, LOCKED_BY_NAME = NULL, LOCK_TIME = NULL, LOCK_REASON = NULL WHERE ID = #{versionId}")
    int unlockVersion(@Param("versionId") String versionId);

    /**
     * 设置当前版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int setCurrentVersion(@Param("versionId") String versionId, @Param("fiscalYear") Integer fiscalYear, 
                         @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 设置基准版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int setBaselineVersion(@Param("versionId") String versionId, @Param("fiscalYear") Integer fiscalYear, 
                          @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 设置最终版本
     * @param versionId 版本ID
     * @param fiscalYear 预算年度
     * @param modelId 预算模型ID
     * @param tenantId 租户ID
     * @return 更新数量
     */
    int setFinalVersion(@Param("versionId") String versionId, @Param("fiscalYear") Integer fiscalYear, 
                       @Param("modelId") String modelId, @Param("tenantId") String tenantId);

    /**
     * 归档版本
     * @param versionId 版本ID
     * @param archivedBy 归档人ID
     * @param archivedByName 归档人姓名
     * @param archiveTime 归档时间
     * @return 更新数量
     */
    @Update("UPDATE BUDGET_VERSION SET IS_ARCHIVED = 1, ARCHIVED_BY = #{archivedBy}, ARCHIVED_BY_NAME = #{archivedByName}, ARCHIVE_TIME = #{archiveTime} WHERE ID = #{versionId}")
    int archiveVersion(@Param("versionId") String versionId, @Param("archivedBy") String archivedBy, 
                      @Param("archivedByName") String archivedByName, @Param("archiveTime") LocalDateTime archiveTime);

    /**
     * 批量更新版本状态
     * @param versionIds 版本ID列表
     * @param status 状态
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateVersionStatus(@Param("versionIds") List<String> versionIds, @Param("status") String status, 
                                @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    /**
     * 批量更新版本启用状态
     * @param versionIds 版本ID列表
     * @param isEnabled 是否启用
     * @param updateBy 更新人
     * @param updateTime 更新时间
     * @return 更新数量
     */
    int batchUpdateVersionEnabled(@Param("versionIds") List<String> versionIds, @Param("isEnabled") Boolean isEnabled, 
                                 @Param("updateBy") String updateBy, @Param("updateTime") LocalDateTime updateTime);

    // ==================== 数据清理方法 ====================

    /**
     * 清理过期的版本
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupExpiredVersions(@Param("tenantId") String tenantId);

    /**
     * 清理归档的版本
     * @param days 归档天数
     * @param tenantId 租户ID
     * @return 清理数量
     */
    int cleanupArchivedVersions(@Param("days") Integer days, @Param("tenantId") String tenantId);

    // ==================== 验证方法 ====================

    /**
     * 检查版本编码是否存在
     * @param versionCode 版本编码
     * @param excludeId 排除的版本ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VERSION WHERE VERSION_CODE = #{versionCode} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkVersionCodeExists(@Param("versionCode") String versionCode, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查版本名称是否存在
     * @param versionName 版本名称
     * @param excludeId 排除的版本ID
     * @param tenantId 租户ID
     * @return 是否存在
     */
    @Select("SELECT COUNT(*) FROM BUDGET_VERSION WHERE VERSION_NAME = #{versionName} AND ID != #{excludeId} AND TENANT_ID = #{tenantId} AND IS_DELETED = 0")
    int checkVersionNameExists(@Param("versionName") String versionName, @Param("excludeId") String excludeId, @Param("tenantId") String tenantId);

    /**
     * 检查版本是否被使用
     * @param versionId 版本ID
     * @param tenantId 租户ID
     * @return 是否被使用
     */
    int checkVersionInUse(@Param("versionId") String versionId, @Param("tenantId") String tenantId);
}
