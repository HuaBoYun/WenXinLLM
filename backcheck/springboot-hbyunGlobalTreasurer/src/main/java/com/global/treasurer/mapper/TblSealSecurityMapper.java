package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSealOperationLog;
import com.global.treasurer.entity.TblSealSecurityConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 印鉴安全管理Mapper
 *
 * @author 华博云开发团队
 * @since 2025-12-25
 */
@Mapper
public interface TblSealSecurityMapper extends BaseMapper<TblSealSecurityConfig> {

    /**
     * 批量插入操作日志
     *
     * @param logs 操作日志列表
     * @return 插入记录数
     */
    int batchInsertOperationLogs(@Param("logs") List<TblSealOperationLog> logs);

    /**
     * 查询操作日志列表(分页)
     *
     * @param sealId 印鉴ID(可选)
     * @param operationType 操作类型(可选)
     * @param startTime 开始时间(可选)
     * @param endTime 结束时间(可选)
     * @return 操作日志列表
     */
    List<TblSealOperationLog> selectOperationLogs(
            @Param("sealId") String sealId,
            @Param("operationType") String operationType,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 统计操作日志数量
     *
     * @param sealId 印鉴ID(可选)
     * @param operationType 操作类型(可选)
     * @param startTime 开始时间(可选)
     * @param endTime 结束时间(可选)
     * @return 数量
     */
    Long countOperationLogs(
            @Param("sealId") String sealId,
            @Param("operationType") String operationType,
            @Param("startTime") String startTime,
            @Param("endTime") String endTime
    );

    /**
     * 根据印鉴ID查询安全配置
     *
     * @param sealId 印鉴ID
     * @return 安全配置
     */
    TblSealSecurityConfig selectBySealId(@Param("sealId") String sealId);

    /**
     * 查询使用统计数据
     *
     * @param sealId 印鉴ID(可选)
     * @return 统计数据
     */
    Map<String, Object> selectUsageStats(@Param("sealId") String sealId);

    /**
     * 批量更新印鉴状态
     *
     * @param sealIds 印鉴ID列表
     * @param isActive 状态(1-启用,0-停用)
     * @param updateBy 更新人
     * @return 更新记录数
     */
    int batchUpdateSealStatus(@Param("sealIds") List<String> sealIds,
                              @Param("isActive") Integer isActive,
                              @Param("updateBy") String updateBy);
}
