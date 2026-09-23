package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.oracle.entity.CostSimulationEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 成本模拟Mapper接口
 *
 * @author Financial Sharing System
 * @since 2024-12-29
 */
public interface CostSimulationMapper extends BaseMapper<CostSimulationEntity> {

    /**
     * 分页查询成本模拟列表
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param simulationNo 模拟编号
     * @param simulationName 模拟名称
     * @param modelId 模型ID
     * @param simulationStatus 模拟状态
     * @param offset 偏移量
     * @param pageSize 每页数量
     * @return 模拟列表
     */
    List<Map<String, Object>> selectSimulationListWithPagination(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("simulationNo") String simulationNo,
            @Param("simulationName") String simulationName,
            @Param("modelId") String modelId,
            @Param("simulationStatus") Integer simulationStatus,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize
    );

    /**
     * 查询总记录数
     *
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @param simulationNo 模拟编号
     * @param simulationName 模拟名称
     * @param modelId 模型ID
     * @param simulationStatus 模拟状态
     * @return 总记录数
     */
    int countSimulationList(
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId,
            @Param("simulationNo") String simulationNo,
            @Param("simulationName") String simulationName,
            @Param("modelId") String modelId,
            @Param("simulationStatus") Integer simulationStatus
    );

    /**
     * 根据模拟编号查询模拟
     *
     * @param simulationNo 模拟编号
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 模拟信息
     */
    Map<String, Object> selectBySimulationNo(
            @Param("simulationNo") String simulationNo,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );

    /**
     * 批量更新模拟状态
     *
     * @param simulationIds 模拟ID列表
     * @param simulationStatus 模拟状态
     * @return 更新数量
     */
    int batchUpdateStatus(
            @Param("simulationIds") List<String> simulationIds,
            @Param("simulationStatus") Integer simulationStatus
    );

    /**
     * 获取模拟结果统计
     *
     * @param modelId 模型ID
     * @param bookId 账套ID
     * @param tenantId 租户ID
     * @return 统计数据
     */
    Map<String, Object> selectSimulationStatistics(
            @Param("modelId") String modelId,
            @Param("bookId") String bookId,
            @Param("tenantId") String tenantId
    );
}

