package com.management.accountant.mapper.eps;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.entity.eps.EpsBudgetVersion;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算版本数据访问接口
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Mapper
public interface EpsBudgetVersionMapper extends BaseMapper<EpsBudgetVersion> {

    /**
     * 根据版本编码查询版本
     * 
     * @param versionCode 版本编码
     * @return 预算版本
     */
    EpsBudgetVersion selectByVersionCode(@Param("versionCode") String versionCode);

    /**
     * 根据预算体系ID查询版本列表
     * 
     * @param systemId 预算体系ID
     * @return 版本列表
     */
    List<EpsBudgetVersion> selectBySystemId(@Param("systemId") Long systemId);

    /**
     * 查询当前版本
     * 
     * @param systemId 预算体系ID
     * @return 当前版本
     */
    EpsBudgetVersion selectCurrentVersion(@Param("systemId") Long systemId);

    /**
     * 根据预算年度查询版本列表
     * 
     * @param budgetYear 预算年度
     * @return 版本列表
     */
    List<EpsBudgetVersion> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据状态查询版本列表
     * 
     * @param versionStatus 版本状态
     * @return 版本列表
     */
    List<EpsBudgetVersion> selectByStatus(@Param("versionStatus") String versionStatus);

    /**
     * 检查版本编码是否存在
     * 
     * @param versionCode 版本编码
     * @param excludeId 排除的ID
     * @return 存在数量
     */
    int checkVersionCodeExists(@Param("versionCode") String versionCode,
                              @Param("excludeId") Long excludeId);

    /**
     * 批量更新状态
     * 
     * @param versionIds 版本ID列表
     * @param versionStatus 版本状态
     * @param updatedBy 更新人ID
     * @return 更新行数
     */
    int batchUpdateStatus(@Param("versionIds") List<Long> versionIds,
                         @Param("versionStatus") String versionStatus,
                         @Param("updatedBy") Long updatedBy);

    /**
     * 清除当前版本标记
     * 
     * @param systemId 预算体系ID
     * @param excludeVersionId 排除的版本ID
     * @return 更新行数
     */
    int clearCurrentVersionFlag(@Param("systemId") Long systemId,
                               @Param("excludeVersionId") Long excludeVersionId);

    /**
     * 设置当前版本
     * 
     * @param versionId 版本ID
     * @return 更新行数
     */
    int setCurrentVersion(@Param("versionId") Long versionId);

    /**
     * 查询版本树结构
     * 
     * @param systemId 预算体系ID
     * @return 版本树
     */
    List<Map<String, Object>> selectVersionTree(@Param("systemId") Long systemId);

    /**
     * 查询子版本列表
     * 
     * @param parentVersionId 父版本ID
     * @return 子版本列表
     */
    List<EpsBudgetVersion> selectChildVersions(@Param("parentVersionId") Long parentVersionId);

    /**
     * 查询版本路径
     * 
     * @param versionId 版本ID
     * @return 版本路径
     */
    List<EpsBudgetVersion> selectVersionPath(@Param("versionId") Long versionId);

    /**
     * 查询版本统计信息
     * 
     * @param systemId 预算体系ID
     * @param budgetYear 预算年度
     * @return 统计信息
     */
    Map<String, Object> selectVersionStatistics(@Param("systemId") Long systemId,
                                               @Param("budgetYear") Integer budgetYear);

    /**
     * 查询版本状态统计
     * 
     * @param systemId 预算体系ID
     * @return 状态统计
     */
    List<Map<String, Object>> selectVersionStatusStatistics(@Param("systemId") Long systemId);

    /**
     * 查询最新版本
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 最新版本列表
     */
    List<EpsBudgetVersion> selectLatestVersions(@Param("systemId") Long systemId,
                                              @Param("limit") Integer limit);

    /**
     * 查询已发布版本
     * 
     * @param systemId 预算体系ID
     * @return 已发布版本列表
     */
    List<EpsBudgetVersion> selectPublishedVersions(@Param("systemId") Long systemId);

    /**
     * 查询草稿版本
     * 
     * @param systemId 预算体系ID
     * @return 草稿版本列表
     */
    List<EpsBudgetVersion> selectDraftVersions(@Param("systemId") Long systemId);

    /**
     * 查询锁定版本
     * 
     * @param systemId 预算体系ID
     * @return 锁定版本列表
     */
    List<EpsBudgetVersion> selectLockedVersions(@Param("systemId") Long systemId);

    /**
     * 查询归档版本
     * 
     * @param systemId 预算体系ID
     * @return 归档版本列表
     */
    List<EpsBudgetVersion> selectArchivedVersions(@Param("systemId") Long systemId);

    /**
     * 批量锁定版本
     * 
     * @param versionIds 版本ID列表
     * @param lockedBy 锁定人ID
     * @param lockedByName 锁定人姓名
     * @return 更新行数
     */
    int batchLockVersions(@Param("versionIds") List<Long> versionIds,
                         @Param("lockedBy") Long lockedBy,
                         @Param("lockedByName") String lockedByName);

    /**
     * 批量解锁版本
     * 
     * @param versionIds 版本ID列表
     * @return 更新行数
     */
    int batchUnlockVersions(@Param("versionIds") List<Long> versionIds);

    /**
     * 批量发布版本
     * 
     * @param versionIds 版本ID列表
     * @param publishedBy 发布人ID
     * @param publishedByName 发布人姓名
     * @return 更新行数
     */
    int batchPublishVersions(@Param("versionIds") List<Long> versionIds,
                            @Param("publishedBy") Long publishedBy,
                            @Param("publishedByName") String publishedByName);

    /**
     * 批量撤销发布版本
     * 
     * @param versionIds 版本ID列表
     * @return 更新行数
     */
    int batchUnpublishVersions(@Param("versionIds") List<Long> versionIds);

    /**
     * 查询版本依赖关系
     * 
     * @param versionId 版本ID
     * @return 依赖关系
     */
    List<Map<String, Object>> selectVersionDependencies(@Param("versionId") Long versionId);

    /**
     * 查询被依赖的版本
     * 
     * @param versionId 版本ID
     * @return 被依赖的版本列表
     */
    List<EpsBudgetVersion> selectDependentVersions(@Param("versionId") Long versionId);

    /**
     * 查询版本冲突
     * 
     * @param versionId 版本ID
     * @return 冲突信息
     */
    List<Map<String, Object>> selectVersionConflicts(@Param("versionId") Long versionId);

    /**
     * 查询版本差异
     * 
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @return 差异信息
     */
    List<Map<String, Object>> selectVersionDifferences(@Param("sourceVersionId") Long sourceVersionId,
                                                      @Param("targetVersionId") Long targetVersionId);

    /**
     * 查询版本历史
     * 
     * @param versionId 版本ID
     * @return 版本历史
     */
    List<Map<String, Object>> selectVersionHistory(@Param("versionId") Long versionId);

    /**
     * 保存版本历史记录
     * 
     * @param historyRecord 历史记录
     * @return 插入行数
     */
    int insertVersionHistory(@Param("historyRecord") Map<String, Object> historyRecord);

    /**
     * 查询版本标签
     * 
     * @param versionId 版本ID
     * @return 标签列表
     */
    List<String> selectVersionTags(@Param("versionId") Long versionId);

    /**
     * 保存版本标签
     * 
     * @param versionId 版本ID
     * @param tags 标签列表
     * @return 插入行数
     */
    int insertVersionTags(@Param("versionId") Long versionId,
                         @Param("tags") List<String> tags);

    /**
     * 删除版本标签
     * 
     * @param versionId 版本ID
     * @return 删除行数
     */
    int deleteVersionTags(@Param("versionId") Long versionId);

    /**
     * 查询版本评论
     * 
     * @param versionId 版本ID
     * @return 评论列表
     */
    List<Map<String, Object>> selectVersionComments(@Param("versionId") Long versionId);

    /**
     * 保存版本评论
     * 
     * @param commentData 评论数据
     * @return 插入行数
     */
    int insertVersionComment(@Param("commentData") Map<String, Object> commentData);

    /**
     * 查询版本附件
     * 
     * @param versionId 版本ID
     * @return 附件列表
     */
    List<Map<String, Object>> selectVersionAttachments(@Param("versionId") Long versionId);

    /**
     * 保存版本附件
     * 
     * @param attachmentData 附件数据
     * @return 插入行数
     */
    int insertVersionAttachment(@Param("attachmentData") Map<String, Object> attachmentData);

    /**
     * 删除版本附件
     * 
     * @param versionId 版本ID
     * @param attachmentId 附件ID
     * @return 删除行数
     */
    int deleteVersionAttachment(@Param("versionId") Long versionId,
                               @Param("attachmentId") Long attachmentId);

    /**
     * 查询版本权限
     * 
     * @param versionId 版本ID
     * @param userId 用户ID
     * @return 权限信息
     */
    Map<String, Object> selectVersionPermissions(@Param("versionId") Long versionId,
                                                @Param("userId") Long userId);

    /**
     * 保存版本权限
     * 
     * @param permissionData 权限数据
     * @return 插入或更新行数
     */
    int saveVersionPermissions(@Param("permissionData") Map<String, Object> permissionData);

    /**
     * 查询版本配置
     * 
     * @param versionId 版本ID
     * @return 版本配置
     */
    Map<String, Object> selectVersionConfiguration(@Param("versionId") Long versionId);

    /**
     * 保存版本配置
     * 
     * @param versionId 版本ID
     * @param configuration 配置数据
     * @return 插入或更新行数
     */
    int saveVersionConfiguration(@Param("versionId") Long versionId,
                                @Param("configuration") Map<String, Object> configuration);

    /**
     * 查询版本数据统计
     * 
     * @param versionId 版本ID
     * @return 数据统计
     */
    Map<String, Object> selectVersionDataStatistics(@Param("versionId") Long versionId);

    /**
     * 更新版本数据统计
     * 
     * @param versionId 版本ID
     * @param dataRowCount 数据行数
     * @param dataSize 数据大小
     * @return 更新行数
     */
    int updateVersionDataStatistics(@Param("versionId") Long versionId,
                                   @Param("dataRowCount") Long dataRowCount,
                                   @Param("dataSize") Long dataSize);

    /**
     * 查询版本性能统计
     * 
     * @param versionId 版本ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 性能统计
     */
    Map<String, Object> selectVersionPerformanceStatistics(@Param("versionId") Long versionId,
                                                          @Param("startDate") String startDate,
                                                          @Param("endDate") String endDate);

    /**
     * 查询热门版本
     * 
     * @param systemId 预算体系ID
     * @param limit 限制数量
     * @return 热门版本列表
     */
    List<EpsBudgetVersion> selectPopularVersions(@Param("systemId") Long systemId,
                                               @Param("limit") Integer limit);

    /**
     * 查询最近使用的版本
     * 
     * @param systemId 预算体系ID
     * @param userId 用户ID
     * @param limit 限制数量
     * @return 最近使用版本列表
     */
    List<EpsBudgetVersion> selectRecentlyUsedVersions(@Param("systemId") Long systemId,
                                                    @Param("userId") Long userId,
                                                    @Param("limit") Integer limit);

    /**
     * 更新版本使用次数
     * 
     * @param versionId 版本ID
     * @return 更新行数
     */
    int updateVersionUsageCount(@Param("versionId") Long versionId);

    /**
     * 更新版本最后使用时间
     * 
     * @param versionId 版本ID
     * @return 更新行数
     */
    int updateVersionLastUsedTime(@Param("versionId") Long versionId);

    /**
     * 批量删除版本
     * 
     * @param versionIds 版本ID列表
     * @return 删除行数
     */
    int batchDeleteVersions(@Param("versionIds") List<Long> versionIds);

    /**
     * 恢复已删除的版本
     * 
     * @param versionId 版本ID
     * @return 更新行数
     */
    int restoreVersion(@Param("versionId") Long versionId);

    /**
     * 查询版本分支
     * 
     * @param parentVersionId 父版本ID
     * @return 分支版本列表
     */
    List<EpsBudgetVersion> selectVersionBranches(@Param("parentVersionId") Long parentVersionId);

    /**
     * 查询版本合并历史
     * 
     * @param versionId 版本ID
     * @return 合并历史
     */
    List<Map<String, Object>> selectVersionMergeHistory(@Param("versionId") Long versionId);

    /**
     * 保存版本合并记录
     * 
     * @param mergeRecord 合并记录
     * @return 插入行数
     */
    int insertVersionMergeRecord(@Param("mergeRecord") Map<String, Object> mergeRecord);

    /**
     * 查询版本同步记录
     * 
     * @param versionId 版本ID
     * @return 同步记录
     */
    List<Map<String, Object>> selectVersionSyncRecords(@Param("versionId") Long versionId);

    /**
     * 保存版本同步记录
     * 
     * @param syncRecord 同步记录
     * @return 插入行数
     */
    int insertVersionSyncRecord(@Param("syncRecord") Map<String, Object> syncRecord);
}
