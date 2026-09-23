package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblRiskWarning;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * 风险预警Mapper接口
 * 
 * @author 华博云开发团队
 * @since 2025-10-07
 */
@Mapper
public interface TblRiskWarningMapper extends BaseMapper<TblRiskWarning> {

    // 移除自定义分页方法，使用MyBatis-Plus内置分页功能

    /**
     * 统计各预警等级的数量
     * 使用注解方式，简洁高效
     *
     * @return 统计结果
     */
    @Select("SELECT WARNING_LEVEL as RISK_LEVEL, COUNT(*) as count " +
            "FROM TBL_RISK_WARNING " +
            "WHERE WARNING_LEVEL IS NOT NULL " +
            "GROUP BY WARNING_LEVEL " +
            "ORDER BY WARNING_LEVEL")
    List<Map<String, Object>> selectRiskLevelStatistics();

    /**
     * 统计各预警状态的数量
     * 使用注解方式，简洁高效
     *
     * @return 统计结果
     */
    @Select("SELECT WARNING_STATUS as STATUS, COUNT(*) as count " +
            "FROM TBL_RISK_WARNING " +
            "WHERE WARNING_STATUS IS NOT NULL " +
            "GROUP BY WARNING_STATUS " +
            "ORDER BY WARNING_STATUS")
    List<Map<String, Object>> selectStatusStatistics();

    /**
     * 统计各预警类型的数量
     * 使用注解方式，简洁高效
     *
     * @return 统计结果
     */
    @Select("SELECT WARNING_TYPE as BUSINESS_SCENARIO, COUNT(*) as count " +
            "FROM TBL_RISK_WARNING " +
            "WHERE WARNING_TYPE IS NOT NULL " +
            "GROUP BY WARNING_TYPE " +
            "ORDER BY WARNING_TYPE")
    List<Map<String, Object>> selectBusinessScenarioStatistics();

    /**
     * 查询未处理预警数量（使用IS_FALSE_POSITIVE字段）
     *
     * @return 未处理预警数量
     */
    @Select("SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE IS_FALSE_POSITIVE = 'N'")
    Long selectUnreadCount();

    /**
     * 查询待处理预警数量
     *
     * @return 待处理预警数量
     */
    @Select("SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE WARNING_STATUS = 'PENDING'")
    Long selectPendingCount();

    /**
     * 查询高风险预警数量
     *
     * @return 高风险预警数量
     */
    @Select("SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE WARNING_LEVEL IN ('HIGH', 'MEDIUM')")
    Long selectHighRiskCount();

    /**
     * 🔧 修复：查询今日新增预警数量
     * 兼容达梦/Oracle数据库；不对CREATE_TIME列套函数，保证索引可用
     *
     * @return 今日新增预警数量
     */
    @Select("SELECT COUNT(*) FROM TBL_RISK_WARNING WHERE CREATE_TIME >= TRUNC(SYSDATE)")
    Long selectTodayWarningCount();

    /**
     * 批量统计各评估模型的预警数量与待处理预警数量
     * 一次GROUP BY替代逐模型两次COUNT；口径与单模型接口一致（排除误报，待处理=PENDING+PROCESSING）
     * 另含按状态纯净计数（不排误报，与状态统计接口口径一致）：statusPending/statusProcessing/statusProcessed
     *
     * @return 模型预警数量列表（evalModelId / warningCount / pendingCount / statusPending / statusProcessing / statusProcessed）
     */
    @Select("SELECT EVAL_MODEL_ID as evalModelId, " +
            "SUM(CASE WHEN IS_FALSE_POSITIVE = 'N' THEN 1 ELSE 0 END) as warningCount, " +
            "SUM(CASE WHEN IS_FALSE_POSITIVE = 'N' AND WARNING_STATUS IN ('PENDING','PROCESSING') THEN 1 ELSE 0 END) as pendingCount, " +
            "SUM(CASE WHEN WARNING_STATUS = 'PENDING' THEN 1 ELSE 0 END) as statusPending, " +
            "SUM(CASE WHEN WARNING_STATUS = 'PROCESSING' THEN 1 ELSE 0 END) as statusProcessing, " +
            "SUM(CASE WHEN WARNING_STATUS = 'PROCESSED' THEN 1 ELSE 0 END) as statusProcessed " +
            "FROM TBL_RISK_WARNING " +
            "WHERE EVAL_MODEL_ID IS NOT NULL " +
            "GROUP BY EVAL_MODEL_ID")
    List<Map<String, Object>> selectModelWarningCountBatch();

    /**
     * 按预警状态统计各预警级别的数量
     *
     * @param warningStatus 预警状态
     * @return 统计结果（level / count）
     */
    @Select("SELECT WARNING_LEVEL as level, COUNT(*) as count " +
            "FROM TBL_RISK_WARNING " +
            "WHERE WARNING_STATUS = #{warningStatus} AND WARNING_LEVEL IS NOT NULL " +
            "GROUP BY WARNING_LEVEL")
    List<Map<String, Object>> selectLevelStatisticsByStatus(@Param("warningStatus") String warningStatus);

    /**
     * 按预警状态统计各评估模型的预警数量（关联模型名称）
     *
     * @param warningStatus 预警状态
     * @return 统计结果（evalModelId / modelName / count）
     */
    @Select("SELECT w.EVAL_MODEL_ID as evalModelId, m.MODEL_NAME as modelName, COUNT(*) as count " +
            "FROM TBL_RISK_WARNING w " +
            "LEFT JOIN TBL_EVALUATION_MODEL m ON w.EVAL_MODEL_ID = m.EVAL_MODEL_ID " +
            "WHERE w.WARNING_STATUS = #{warningStatus} " +
            "GROUP BY w.EVAL_MODEL_ID, m.MODEL_NAME " +
            "ORDER BY COUNT(*) DESC")
    List<Map<String, Object>> selectModelStatisticsByStatus(@Param("warningStatus") String warningStatus);

    /**
     * 根据评估模型ID查询相关预警
     * 
     * @param evalModelId 评估模型ID
     * @return 预警列表
     */
    @Select("SELECT * FROM TBL_RISK_WARNING WHERE EVAL_MODEL_ID = #{evalModelId} ORDER BY WARNING_TIME DESC")
    List<TblRiskWarning> selectByEvalModelId(@Param("evalModelId") String evalModelId);

    /**
     * 查询最近的预警记录
     * 
     * @param limit 限制数量
     * @return 最近的预警列表
     */
    @Select("SELECT * FROM TBL_RISK_WARNING ORDER BY WARNING_TIME DESC LIMIT #{limit}")
    List<TblRiskWarning> selectRecentWarnings(@Param("limit") Integer limit);

    /**
     * 批量更新预警状态
     * 使用注解方式，简洁高效
     *
     * @param warningIds 预警ID列表
     * @param status 新状态
     * @param handler 处理人
     * @param handleRemark 处理备注
     * @return 更新数量
     */
    @Update("<script>" +
            "UPDATE TBL_RISK_WARNING SET " +
            "WARNING_STATUS = #{status}, " +
            "PROCESS_USER = #{handler}, " +
            "PROCESS_TIME = SYSDATE, " +
            "PROCESS_NOTE = #{handleRemark}, " +
            "UPDATE_TIME = SYSDATE " +
            "WHERE WARNING_ID IN " +
            "<foreach collection='warningIds' item='id' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchUpdateStatus(@Param("warningIds") List<String> warningIds,
                         @Param("status") String status,
                         @Param("handler") String handler,
                         @Param("handleRemark") String handleRemark);
}
