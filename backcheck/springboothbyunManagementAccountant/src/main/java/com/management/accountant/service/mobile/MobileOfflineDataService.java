package com.management.accountant.service.mobile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileOfflineData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动离线数据服务接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
public interface MobileOfflineDataService extends IService<MobileOfflineData> {

    // ==================== 基础CRUD操作 ====================
    
    /**
     * 创建离线数据
     */
    boolean createOfflineData(MobileOfflineData offlineData);
    
    /**
     * 更新离线数据
     */
    boolean updateOfflineData(MobileOfflineData offlineData);
    
    /**
     * 删除离线数据
     */
    boolean deleteOfflineData(String offlineDataId);
    
    /**
     * 根据ID获取离线数据
     */
    MobileOfflineData getOfflineDataById(String offlineDataId);
    
    /**
     * 根据编码获取离线数据
     */
    MobileOfflineData getOfflineDataByCode(String offlineDataCode);

    // ==================== 查询操作 ====================
    
    /**
     * 根据应用配置ID获取离线数据列表
     */
    List<MobileOfflineData> getOfflineDatasByAppConfigId(String appConfigId);
    
    /**
     * 根据离线数据类型获取列表
     */
    List<MobileOfflineData> getOfflineDatasByType(String offlineDataType);
    
    /**
     * 根据离线数据分类获取列表
     */
    List<MobileOfflineData> getOfflineDatasByCategory(String offlineDataCategory);
    
    /**
     * 根据离线数据模块获取列表
     */
    List<MobileOfflineData> getOfflineDatasByModule(String offlineDataModule);
    
    /**
     * 根据用户ID获取离线数据
     */
    List<MobileOfflineData> getOfflineDatasByUserId(String userId);
    
    /**
     * 根据设备ID获取离线数据
     */
    List<MobileOfflineData> getOfflineDatasByDeviceId(String deviceId);
    
    /**
     * 根据数据状态获取离线数据
     */
    List<MobileOfflineData> getOfflineDatasByDataStatus(String dataStatus);
    
    /**
     * 根据同步状态获取离线数据
     */
    List<MobileOfflineData> getOfflineDatasBySyncStatus(String syncStatus);
    
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

    // ==================== 分页查询 ====================
    
    /**
     * 分页查询离线数据
     */
    IPage<MobileOfflineData> getOfflineDataPage(Page<MobileOfflineData> page, Map<String, Object> params);
    
    /**
     * 条件分页查询离线数据
     */
    IPage<MobileOfflineData> getOfflineDataPageByCondition(Page<MobileOfflineData> page, MobileOfflineData condition);
    
    /**
     * 高级搜索分页查询
     */
    IPage<MobileOfflineData> advancedSearchPage(Page<MobileOfflineData> page, Map<String, Object> params);

    // ==================== 离线数据管理 ====================
    
    /**
     * 启用离线数据
     */
    boolean enableOfflineData(String offlineDataId);
    
    /**
     * 禁用离线数据
     */
    boolean disableOfflineData(String offlineDataId);
    
    /**
     * 设置离线数据为可用
     */
    boolean setOfflineDataAvailable(String offlineDataId);
    
    /**
     * 设置离线数据为不可用
     */
    boolean setOfflineDataUnavailable(String offlineDataId);
    
    /**
     * 标记离线数据为过期
     */
    boolean markOfflineDataExpired(String offlineDataId);
    
    /**
     * 批量更新数据状态
     */
    boolean batchUpdateDataStatus(List<String> offlineDataIds, String dataStatus);
    
    /**
     * 批量更新同步状态
     */
    boolean batchUpdateSyncStatus(List<String> offlineDataIds, String syncStatus);
    
    /**
     * 批量启用离线数据
     */
    boolean batchEnableOfflineData(List<String> offlineDataIds);
    
    /**
     * 批量禁用离线数据
     */
    boolean batchDisableOfflineData(List<String> offlineDataIds);
    
    /**
     * 批量删除离线数据
     */
    boolean batchDeleteOfflineData(List<String> offlineDataIds);

    // ==================== 数据同步管理 ====================
    
    /**
     * 同步离线数据
     */
    boolean syncOfflineData(String offlineDataId);
    
    /**
     * 批量同步离线数据
     */
    boolean batchSyncOfflineData(List<String> offlineDataIds);
    
    /**
     * 下载离线数据
     */
    boolean downloadOfflineData(String offlineDataId);
    
    /**
     * 上传离线数据
     */
    boolean uploadOfflineData(String offlineDataId);
    
    /**
     * 更新同步状态
     */
    boolean updateSyncStatus(String offlineDataId, String syncStatus);
    
    /**
     * 更新访问时间
     */
    boolean updateLastAccessTime(String offlineDataId);
    
    /**
     * 更新访问次数
     */
    boolean updateAccessCount(String offlineDataId);
    
    /**
     * 更新下载次数
     */
    boolean updateDownloadCount(String offlineDataId);
    
    /**
     * 更新上传次数
     */
    boolean updateUploadCount(String offlineDataId);

    // ==================== 搜索功能 ====================
    
    /**
     * 根据关键词搜索离线数据
     */
    List<MobileOfflineData> searchOfflineDatasByKeyword(String keyword);
    
    /**
     * 根据标签搜索离线数据
     */
    List<MobileOfflineData> searchOfflineDatasByTags(List<String> tags);
    
    /**
     * 查找相似的离线数据
     */
    List<MobileOfflineData> findSimilarOfflineData(String offlineDataId);
    
    /**
     * 查找热门离线数据
     */
    List<MobileOfflineData> findPopularOfflineData(Integer limit);
    
    /**
     * 查找推荐的离线数据
     */
    List<MobileOfflineData> findRecommendedOfflineData(String userId, Integer limit);

    // ==================== 验证功能 ====================
    
    /**
     * 验证离线数据
     */
    boolean validateOfflineData(MobileOfflineData offlineData);
    
    /**
     * 验证离线数据完整性
     */
    boolean validateOfflineDataIntegrity(String offlineDataId);
    
    /**
     * 测试离线数据连接
     */
    boolean testOfflineDataConnection(String offlineDataId);

    // ==================== 统计分析 ====================
    
    /**
     * 获取离线数据使用统计
     */
    Map<String, Object> getOfflineDataUsageStats(LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 获取离线数据性能统计
     */
    Map<String, Object> getOfflineDataPerformanceStats(String offlineDataId);
    
    /**
     * 获取离线数据质量统计
     */
    Map<String, Object> getOfflineDataQualityStats(String offlineDataId);
    
    /**
     * 获取离线数据健康评分
     */
    Double getOfflineDataHealthScore(String offlineDataId);
    
    /**
     * 生成离线数据报告
     */
    Map<String, Object> generateOfflineDataReport(Map<String, Object> params);
    
    /**
     * 统计离线数据总数
     */
    Long countOfflineData();
    
    /**
     * 根据类型统计离线数据
     */
    Long countOfflineDataByType(String offlineDataType);
    
    /**
     * 根据分类统计离线数据
     */
    Long countOfflineDataByCategory(String offlineDataCategory);
    
    /**
     * 根据状态统计离线数据
     */
    Long countOfflineDataByStatus(String status);
    
    /**
     * 根据创建时间统计离线数据
     */
    Long countOfflineDataByCreateTime(LocalDateTime startTime, LocalDateTime endTime);

    // ==================== 导入导出 ====================
    
    /**
     * 导入离线数据
     */
    boolean importOfflineData(List<MobileOfflineData> offlineDataList);
    
    /**
     * 导出离线数据
     */
    List<MobileOfflineData> exportOfflineData(Map<String, Object> params);
    
    /**
     * 导出离线数据模板
     */
    List<Map<String, Object>> exportOfflineDataTemplate();

    // ==================== 数据清理 ====================
    
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

    // ==================== 系统维护 ====================
    
    /**
     * 重建离线数据索引
     */
    boolean rebuildOfflineDataIndex();
    
    /**
     * 优化离线数据存储
     */
    boolean optimizeOfflineDataStorage();
    
    /**
     * 同步离线数据状态
     */
    boolean syncOfflineDataStatus();
    
    /**
     * 验证离线数据配置
     */
    List<String> validateOfflineDataConfig();
    
    /**
     * 获取系统概览
     */
    Map<String, Object> getSystemOverview();
}
