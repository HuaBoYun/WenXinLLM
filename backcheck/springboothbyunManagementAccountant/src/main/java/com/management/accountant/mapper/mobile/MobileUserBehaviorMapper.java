package com.management.accountant.mapper.mobile;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.mobile.MobileUserBehavior;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 移动用户行为 Mapper 接口
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Mapper
public interface MobileUserBehaviorMapper extends BaseMapper<MobileUserBehavior> {

    // ==================== 基础查询方法 ====================
    
    /**
     * 根据用户行为编码查询
     */
    MobileUserBehavior getByUserBehaviorCode(@Param("userBehaviorCode") String userBehaviorCode);
    
    /**
     * 根据应用配置ID查询用户行为列表
     */
    List<MobileUserBehavior> getByAppConfigId(@Param("appConfigId") String appConfigId);
    
    /**
     * 根据用户行为类型查询
     */
    List<MobileUserBehavior> getByUserBehaviorType(@Param("userBehaviorType") String userBehaviorType);
    
    /**
     * 根据用户行为分类查询
     */
    List<MobileUserBehavior> getByUserBehaviorCategory(@Param("userBehaviorCategory") String userBehaviorCategory);
    
    /**
     * 根据用户行为模块查询
     */
    List<MobileUserBehavior> getByUserBehaviorModule(@Param("userBehaviorModule") String userBehaviorModule);
    
    /**
     * 根据用户ID查询用户行为
     */
    List<MobileUserBehavior> getByUserId(@Param("userId") String userId);
    
    /**
     * 根据设备ID查询用户行为
     */
    List<MobileUserBehavior> getByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 根据会话ID查询用户行为
     */
    List<MobileUserBehavior> getBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * 根据行为事件查询
     */
    List<MobileUserBehavior> getByBehaviorEvent(@Param("behaviorEvent") String behaviorEvent);
    
    /**
     * 根据行为动作查询
     */
    List<MobileUserBehavior> getByBehaviorAction(@Param("behaviorAction") String behaviorAction);
    
    /**
     * 根据页面路径查询
     */
    List<MobileUserBehavior> getByPagePath(@Param("pagePath") String pagePath);
    
    /**
     * 根据时间范围查询用户行为
     */
    List<MobileUserBehavior> getByTimeRange(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 分页查询方法 ====================
    
    /**
     * 分页查询用户行为
     */
    IPage<MobileUserBehavior> getUserBehaviorPage(Page<MobileUserBehavior> page, @Param("params") Map<String, Object> params);
    
    /**
     * 条件分页查询用户行为
     */
    IPage<MobileUserBehavior> getUserBehaviorPageByCondition(Page<MobileUserBehavior> page, @Param("condition") MobileUserBehavior condition);
    
    /**
     * 高级搜索分页查询
     */
    IPage<MobileUserBehavior> advancedSearchPage(Page<MobileUserBehavior> page, @Param("params") Map<String, Object> params);

    // ==================== 统计查询方法 ====================
    
    /**
     * 统计用户行为总数
     */
    Long countUserBehaviors();
    
    /**
     * 根据用户行为类型统计
     */
    Long countByUserBehaviorType(@Param("userBehaviorType") String userBehaviorType);
    
    /**
     * 根据用户行为分类统计
     */
    Long countByUserBehaviorCategory(@Param("userBehaviorCategory") String userBehaviorCategory);
    
    /**
     * 根据用户行为模块统计
     */
    Long countByUserBehaviorModule(@Param("userBehaviorModule") String userBehaviorModule);
    
    /**
     * 根据用户ID统计
     */
    Long countByUserId(@Param("userId") String userId);
    
    /**
     * 根据设备ID统计
     */
    Long countByDeviceId(@Param("deviceId") String deviceId);
    
    /**
     * 根据会话ID统计
     */
    Long countBySessionId(@Param("sessionId") String sessionId);
    
    /**
     * 根据行为事件统计
     */
    Long countByBehaviorEvent(@Param("behaviorEvent") String behaviorEvent);
    
    /**
     * 根据行为动作统计
     */
    Long countByBehaviorAction(@Param("behaviorAction") String behaviorAction);
    
    /**
     * 根据页面路径统计
     */
    Long countByPagePath(@Param("pagePath") String pagePath);
    
    /**
     * 根据创建时间统计
     */
    Long countByCreateTime(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 批量操作方法 ====================
    
    /**
     * 批量更新行为状态
     */
    int batchUpdateBehaviorStatus(@Param("userBehaviorIds") List<String> userBehaviorIds, @Param("behaviorStatus") String behaviorStatus);
    
    /**
     * 批量删除用户行为
     */
    int batchDeleteUserBehaviors(@Param("userBehaviorIds") List<String> userBehaviorIds);
    
    /**
     * 批量插入用户行为
     */
    int batchInsertUserBehaviors(@Param("userBehaviors") List<MobileUserBehavior> userBehaviors);

    // ==================== 行为分析方法 ====================
    
    /**
     * 获取用户活跃度统计
     */
    Map<String, Object> getUserActivityStats(@Param("userId") String userId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取页面访问统计
     */
    List<Map<String, Object>> getPageVisitStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取功能使用统计
     */
    List<Map<String, Object>> getFunctionUsageStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取用户路径分析
     */
    List<Map<String, Object>> getUserPathAnalysis(@Param("userId") String userId, @Param("sessionId") String sessionId);
    
    /**
     * 获取热力图数据
     */
    List<Map<String, Object>> getHeatmapData(@Param("pagePath") String pagePath, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取漏斗分析数据
     */
    List<Map<String, Object>> getFunnelAnalysisData(@Param("funnelSteps") List<String> funnelSteps, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取留存分析数据
     */
    Map<String, Object> getRetentionAnalysisData(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取转化率分析数据
     */
    Map<String, Object> getConversionAnalysisData(@Param("sourceEvent") String sourceEvent, @Param("targetEvent") String targetEvent, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 搜索查询方法 ====================
    
    /**
     * 根据关键词搜索用户行为
     */
    List<MobileUserBehavior> searchByKeyword(@Param("keyword") String keyword);
    
    /**
     * 根据标签搜索用户行为
     */
    List<MobileUserBehavior> searchByTags(@Param("tags") List<String> tags);
    
    /**
     * 查找异常用户行为
     */
    List<MobileUserBehavior> findAbnormalUserBehaviors(@Param("params") Map<String, Object> params);
    
    /**
     * 查找高频用户行为
     */
    List<MobileUserBehavior> findHighFrequencyUserBehaviors(@Param("limit") Integer limit);
    
    /**
     * 查找长时间会话
     */
    List<MobileUserBehavior> findLongSessions(@Param("minDuration") Long minDuration);
    
    /**
     * 查找错误行为
     */
    List<MobileUserBehavior> findErrorBehaviors();

    // ==================== 性能分析方法 ====================
    
    /**
     * 获取性能统计数据
     */
    Map<String, Object> getPerformanceStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取页面加载时间统计
     */
    Map<String, Object> getPageLoadTimeStats(@Param("pagePath") String pagePath, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取响应时间统计
     */
    Map<String, Object> getResponseTimeStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 获取错误率统计
     */
    Map<String, Object> getErrorRateStats(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 报告生成方法 ====================
    
    /**
     * 生成用户行为报告
     */
    Map<String, Object> generateUserBehaviorReport(@Param("params") Map<String, Object> params);
    
    /**
     * 生成用户画像报告
     */
    Map<String, Object> generateUserProfileReport(@Param("userId") String userId);
    
    /**
     * 生成应用使用报告
     */
    Map<String, Object> generateAppUsageReport(@Param("appConfigId") String appConfigId, @Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    /**
     * 生成性能分析报告
     */
    Map<String, Object> generatePerformanceReport(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    // ==================== 维护管理方法 ====================
    
    /**
     * 清理过期的用户行为数据
     */
    int cleanExpiredUserBehaviors(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 清理无效的用户行为数据
     */
    int cleanInvalidUserBehaviors();
    
    /**
     * 归档历史用户行为数据
     */
    int archiveHistoricalUserBehaviors(@Param("beforeTime") LocalDateTime beforeTime);
    
    /**
     * 重建用户行为索引
     */
    int rebuildUserBehaviorIndex();
    
    /**
     * 优化用户行为存储
     */
    int optimizeUserBehaviorStorage();
    
    /**
     * 同步用户行为状态
     */
    int syncUserBehaviorStatus();
    
    /**
     * 验证用户行为数据完整性
     */
    List<String> validateUserBehaviorDataIntegrity();
    
    /**
     * 获取系统概览信息
     */
    Map<String, Object> getSystemOverview();
}
