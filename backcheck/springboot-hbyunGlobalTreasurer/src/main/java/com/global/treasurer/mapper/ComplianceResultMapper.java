package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblComplianceResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 合规检查结果Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface ComplianceResultMapper extends BaseMapper<TblComplianceResult> {

    /**
     * 分页查询合规检查结果列表
     *
     * @param params 查询参数
     * @return 合规检查结果列表
     */
    List<TblComplianceResult> selectResultList(Map<String, Object> params);

    /**
     * 根据ID查询合规检查结果详情
     *
     * @param resultId 结果ID
     * @return 合规检查结果
     */
    TblComplianceResult selectResultById(@Param("resultId") String resultId);

    /**
     * 查询需要关注的检查结果
     *
     * @return 合规检查结果列表
     */
    List<TblComplianceResult> selectResultsNeedingAttention();

    /**
     * 查询需要立即处理的检查结果
     *
     * @return 合规检查结果列表
     */
    List<TblComplianceResult> selectResultsNeedingImmediateAction();

    /**
     * 批量删除合规检查结果（逻辑删除）
     *
     * @param resultIds 结果ID列表
     * @return 影响行数
     */
    int batchDeleteByIds(@Param("resultIds") List<String> resultIds);

    /**
     * 更新结果状态
     *
     * @param resultId 结果ID
     * @param checkStatus 检查状态
     * @return 影响行数
     */
    int updateResultStatus(@Param("resultId") String resultId, @Param("checkStatus") String checkStatus);
}

