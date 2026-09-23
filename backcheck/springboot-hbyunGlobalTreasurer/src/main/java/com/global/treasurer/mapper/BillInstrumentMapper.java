package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BillInstrumentQueryDTO;
import com.global.treasurer.entity.TblBillInstrument;
import com.global.treasurer.vo.BillInstrumentVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 票据Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Mapper
public interface BillInstrumentMapper extends BaseMapper<TblBillInstrument> {

    /**
     * 查询票据列表
     *
     * @param queryDTO 查询条件
     * @return 票据列表
     */
    List<BillInstrumentVO> selectBillInstrumentList(BillInstrumentQueryDTO queryDTO);

    /**
     * 根据ID查询票据详情
     *
     * @param instrumentId 票据ID
     * @return 票据详情
     */
    BillInstrumentVO selectBillInstrumentById(@Param("instrumentId") Long instrumentId);

    /**
     * 批量删除票据
     *
     * @param instrumentIds 票据ID数组
     * @return 影响行数
     */
    int deleteBillInstrumentByIds(@Param("instrumentIds") Long[] instrumentIds);

    // ==================== 统计查询方法 ====================

    /**
     * 获取票据统计数据（总数、总金额、平均金额等）
     *
     * @return 统计数据Map
     */
    Map<String, Object> getBillStatistics();

    /**
     * 按票据类型统计
     *
     * @return 类型统计列表
     */
    List<Map<String, Object>> getBillTypeStatistics();

    /**
     * 按票据状态统计
     *
     * @return 状态统计列表
     */
    List<Map<String, Object>> getBillStatusStatistics();

    /**
     * 按承兑人统计
     *
     * @return 承兑人统计列表
     */
    List<Map<String, Object>> getBillAcceptorStatistics();

    /**
     * 按金额区间统计
     *
     * @return 金额区间统计列表
     */
    List<Map<String, Object>> getBillAmountRangeStatistics();

    /**
     * 按日期统计票据趋势
     *
     * @param params 包含startDate和endDate的参数Map
     * @return 每日趋势数据列表
     */
    List<Map<String, Object>> getBillDailyTrend(Map<String, Object> params);

    /**
     * 获取上月统计数据（用于计算增长率）
     *
     * @return 上月统计数据
     */
    Map<String, Object> getLastMonthStatistics();

    /**
     * 获取本月统计数据
     *
     * @return 本月统计数据
     */
    Map<String, Object> getCurrentMonthStatistics();

    /**
     * 获取统计表格数据（按维度分组）
     *
     * @param params 查询参数（包含dimension、startDate、endDate等）
     * @return 统计表格数据列表
     */
    List<Map<String, Object>> getStatisticsTableData(Map<String, Object> params);
}

