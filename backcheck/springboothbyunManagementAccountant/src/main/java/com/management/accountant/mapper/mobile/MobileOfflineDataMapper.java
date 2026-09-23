package com.management.accountant.mapper.mobile;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileOfflineData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动离线数据 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobileOfflineDataMapper extends BaseMapper<MobileOfflineData> {

    // ==================== 基础查询方法 ====================
    
    /**
     * 根据离线数据编码查询
     */
    MobileOfflineData getByOfflineDataCode(@Param("offlineDataCode") String offlineDataCode);
    
    /**
     * 根据应用配置ID查询离线数据列表
     */
    List<MobileOfflineData> getByAppConfigId(@Param("appConfigId") String appConfigId);
    
    /**
     * 根据离线数据类型查询
     */
    List<MobileOfflineData> getByOfflineDataType(@Param("offlineDataType") String offlineDataType);
    
    /**
     * 根据离线数据分类查询
     */
    List<MobileOfflineData> getByOfflineDataCategory(@Param("offlineDataCategory") String offlineDataCategory);
    
    /**
     * 根据离线数据模块查询
     */
    List<MobileOfflineData> getByOfflineDataModule(@Param("offlineDataModule") String offlineDataModule);
    
    /**
     * 根据用户ID查询离线数据
     */
    List<MobileOfflineData> getByUserId(@Param("userId") String userId);
    
    /**
     * 根据设备ID查询离线数据
     */
    List<MobileOfflineData> getByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 根据数据状态查询
     */
    List<MobileOfflineData> getByDataStatus(@Param("dataStatus") String dataStatus);
    
    /**
     * 根据同步状态查询
     */
    List<MobileOfflineData> getBySyncStatus(@Param("syncStatus") String syncStatus);
    
    /**
     * 获取可用的离线数据
     */
    List<MobileOfflineData> getAvailableOfflineData();
    
    /**
     * 获取已过期的离线数据
     */
    List<MobileOfflineData> getExpiredOfflineData();
    
    /**
     * 获取需要同步的离线数据
     */
    List<MobileOfflineData> getNeedSyncOfflineData();

    // ==================== 分页查询方法 ====================
    
    /**
     * 分页查询离线数据
     */
    IPage<MobileOfflineData> getOfflineDataPage(Page<MobileOfflineData> page, @Param("params") Map<String, Object> params);
    
    /**
     * 条件分页查询离线数据
     */
    IPage<MobileOfflineData> getOfflineDataPageByCondition(Page<MobileOfflineData> page, @Param("condition") MobileOfflineData condition);
    
    /**
     * 高级搜索分页查询
     */
    IPage<MobileOfflineData> advancedSearchPage(Page<MobileOfflineData> page, @Param("params") Map<String, Object> params);

    // ==================== 统计查询方法 ====================
    
    /**
     * 统计离线数据总数
     */
    Long countOfflineData();
    
    /**
     * 根据离线数据类型统计
     */
    Long countByOfflineDataType(@Param("offlineDataType") String offlineDataType);
    
    /**
     * 根据离线数据分类统计
     */
    Long countByOfflineDataCategory(@Param("offlineDataCategory") String offlineDataCategory);
    
    /**
     * 根据离线数据模块统计
     */
    Long countByOfflineDataModule(@Param("offlineDataModule") String offlineDataModule);
    
    /**
     * 根据数据状态统计
     */
    Long countByDataStatus(@Param("dataStatus") String dataStatus);
    
    /**
     * 根据同步状态统计
     */
    Long countBySyncStatus(@Param("syncStatus") String syncStatus);
    
    /**
     * 根据用户ID统计
     */
    Long countByUserId(@Param("userId") String userId);
    
    /**
     * 根据设备ID统计
     */
    Long countByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 根据创建时间统计
     */
    Long countByCreateTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 批量操作方法 ====================
    
    /**
     * 批量更新数据状态
     */
    int batchUpdateDataStatus(@Param("offlineDataIds") List<String> offlineDataIds, @Param("dataStatus") String dataStatus);
    
    /**
     * 批量更新同步状态
     */
    int batchUpdateSyncStatus(@Param("offlineDataIds") List<String> offlineDataIds, @Param("syncStatus") String syncStatus);
    
    /**
     * 批量设置可用状态
     */
    int batchSetAvailable(@Param("offlineDataIds") List<String> offlineDataIds, @Param("isAvailable") Boolean isAvailable);
    
    /**
     * 批量设置过期状态
     */
    int batchSetExpired(@Param("offlineDataIds") List<String> offlineDataIds, @Param("isExpired") Boolean isExpired);
    
    /**
     * 批量删除离线数据
     */
    int batchDeleteOfflineData(@Param("offlineDataIds") List<String> offlineDataIds);

    // ==================== 数据同步方法 ====================
    
    /**
     * 更新同步状态
     */
    int updateSyncStatus(@Param("offlineDataId") String offlineDataId, @Param("syncStatus") String syncStatus);
    
    /**
     * 更新同步次数
     */
    int updateSyncCount(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 更新最后访问时间
     */
    int updateLastAccessTime(@Param("offlineDataId") String offlineDataId, @Param("lastAccessTime") LocalDateTime lastAccessTime);
    
    /**
     * 更新访问次数
     */
    int updateAccessCount(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 更新下载次数
     */
    int updateDownloadCount(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 更新上传次数
     */
    int updateUploadCount(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 更新错误次数
     */
    int updateErrorCount(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 更新错误信息
     */
    int updateErrorMessage(@Param("offlineDataId") String offlineDataId, @Param("errorMessage") String errorMessage);

    // ==================== 搜索查询方法 ====================
    
    /**
     * 根据关键词搜索离线数据
     */
    List<MobileOfflineData> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据标签搜索离线数据
     */
    List<MobileOfflineData> searchByTags(@Param("tags") List<String> tags);
    
    /**
     * 查找相似的离线数据
     */
    List<MobileOfflineData> findSimilarOfflineData(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 查找热门离线数据
     */
    List<MobileOfflineData> findPopularOfflineData(@Param("limit") Integer limit);
    
    /**
     * 查找推荐的离线数据
     */
    List<MobileOfflineData> findRecommendedOfflineData(@Param("userId") String userId, @Param("limit") Integer limit);
    
    /**
     * 查找需要清理的离线数据
     */
    List<MobileOfflineData> findDataNeedingCleanup();
    
    /**
     * 查找大文件离线数据
     */
    List<MobileOfflineData> findLargeFiles(@Param("minSize") Long minSize);

    // ==================== 分析统计方法 ====================
    
    /**
     * 获取离线数据使用统计
     */
    Map<String, Object> getOfflineDataUsageStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取离线数据性能统计
     */
    Map<String, Object> getOfflineDataPerformanceStats(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 获取离线数据质量统计
     */
    Map<String, Object> getOfflineDataQualityStats(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 获取离线数据健康评分
     */
    BigDecimal getOfflineDataHealthScore(@Param("offlineDataId") String offlineDataId);
    
    /**
     * 生成离线数据报告
     */
    Map<String, Object> generateOfflineDataReport(@Param("params") Map<String, Object> params);
    
    /**
     * 获取离线数据监控数据
     */
    List<Map<String, Object>> getOfflineDataMonitoringData(@Param("params") Map<String, Object> params);
    
    /**
     * 获取离线数据异常记录
     */
    List<Map<String, Object>> getOfflineDataExceptionRecords(@Param("offlineDataId") String offlineDataId);

    // ==================== 维护管理方法 ====================
    
    /**
     * 清理过期的离线数据
     */
    int cleanExpiredOfflineData();
    
    /**
     * 清理无效的离线数据
     */
    int cleanInvalidOfflineData();
    
    /**
     * 清理重复的离线数据
     */
    int cleanDuplicateOfflineData();
    
    /**
     * 重建离线数据索引
     */
    int rebuildOfflineDataIndex();
    
    /**
     * 优化离线数据存储
     */
    int optimizeOfflineDataStorage();
    
    /**
     * 同步离线数据状态
     */
    int syncOfflineDataStatus();
    
    /**
     * 验证离线数据完整性
     */
    List<String> validateOfflineDataIntegrity();
    
    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();
}
