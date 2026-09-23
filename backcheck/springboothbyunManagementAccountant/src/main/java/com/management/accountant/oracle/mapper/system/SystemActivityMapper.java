package com.management.accountant.oracle.mapper.system;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.system.SystemActivity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统活动记录Mapper接口
 * 
 * @description 系统活动记录数据访问层
 * @author AI Agent
 * @date 2026-01-30
 */
@Mapper
public interface SystemActivityMapper extends BaseMapper<SystemActivity> {

    /**
     * 查询最近活动列表
     * 
     * @param companyId 公司ID
     * @param limit 限制数量
     * @return 活动列表
     */
    List<SystemActivity> selectRecentActivities(@Param("companyId") String companyId,
                                                 @Param("limit") Integer limit);

    /**
     * 根据用户ID查询活动列表
     * 
     * @param userId 用户ID
     * @param companyId 公司ID
     * @param limit 限制数量
     * @return 活动列表
     */
    List<SystemActivity> selectByUserId(@Param("userId") String userId,
                                         @Param("companyId") String companyId,
                                         @Param("limit") Integer limit);

    /**
     * 根据模块名称查询活动列表
     * 
     * @param moduleName 模块名称
     * @param companyId 公司ID
     * @param limit 限制数量
     * @return 活动列表
     */
    List<SystemActivity> selectByModule(@Param("moduleName") String moduleName,
                                         @Param("companyId") String companyId,
                                         @Param("limit") Integer limit);

    /**
     * 根据目标类型和ID查询活动列表
     * 
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @param companyId 公司ID
     * @return 活动列表
     */
    List<SystemActivity> selectByTarget(@Param("targetType") String targetType,
                                         @Param("targetId") String targetId,
                                         @Param("companyId") String companyId);

    /**
     * 统计操作次数
     * 
     * @param action 操作动作
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param companyId 公司ID
     * @return 操作次数
     */
    int countByAction(@Param("action") String action,
                      @Param("startTime") Date startTime,
                      @Param("endTime") Date endTime,
                      @Param("companyId") String companyId);

    /**
     * 统计用户活跃度
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param companyId 公司ID
     * @return 用户活跃度统计
     */
    List<Map<String, Object>> countUserActivity(@Param("startTime") Date startTime,
                                                  @Param("endTime") Date endTime,
                                                  @Param("companyId") String companyId);

    /**
     * 统计模块使用情况
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param companyId 公司ID
     * @return 模块使用统计
     */
    List<Map<String, Object>> countModuleUsage(@Param("startTime") Date startTime,
                                                 @Param("endTime") Date endTime,
                                                 @Param("companyId") String companyId);

    /**
     * 统计操作成功率
     * 
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param companyId 公司ID
     * @return 成功率统计
     */
    Map<String, Object> countSuccessRate(@Param("startTime") Date startTime,
                                          @Param("endTime") Date endTime,
                                          @Param("companyId") String companyId);

    /**
     * 查询平均执行时间
     * 
     * @param action 操作动作
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param companyId 公司ID
     * @return 平均执行时间（毫秒）
     */
    Double selectAvgExecuteTime(@Param("action") String action,
                                 @Param("startTime") Date startTime,
                                 @Param("endTime") Date endTime,
                                 @Param("companyId") String companyId);

    /**
     * 批量插入活动记录
     * 
     * @param activityList 活动列表
     * @return 影响行数
     */
    int batchInsert(@Param("activityList") List<SystemActivity> activityList);

    /**
     * 清理历史数据
     * 
     * @param beforeDate 清理此日期之前的数据
     * @param companyId 公司ID
     * @return 影响行数
     */
    int cleanHistoryData(@Param("beforeDate") Date beforeDate,
                         @Param("companyId") String companyId);
}

