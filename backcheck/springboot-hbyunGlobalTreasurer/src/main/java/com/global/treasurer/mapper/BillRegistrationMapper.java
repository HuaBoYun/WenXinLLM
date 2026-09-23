package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillRegistration;
import com.global.treasurer.dto.BillRegistrationQueryDTO;
import com.global.treasurer.vo.BillRegistrationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 票据登记Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillRegistrationMapper extends BaseMapper<TblBillRegistration> {

    /**
     * 查询票据登记列表
     *
     * @param queryDTO 查询条件
     * @return 票据登记列表
     */
    List<BillRegistrationVO> selectBillRegistrationList(@Param("query") BillRegistrationQueryDTO queryDTO);

    /**
     * 根据ID查询票据登记详情
     *
     * @param billId 票据ID
     * @return 票据登记详情
     */
    BillRegistrationVO selectBillRegistrationById(@Param("billId") Long billId);

    /**
     * 根据票据号码查询票据
     *
     * @param billNumber 票据号码
     * @return 票据信息
     */
    TblBillRegistration selectByBillNumber(@Param("billNumber") String billNumber);

    /**
     * 批量删除票据登记
     *
     * @param billIds 票据ID数组
     * @return 影响行数
     */
    int deleteBillRegistrationByIds(@Param("billIds") Long[] billIds);

    /**
     * 获取票据统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getBillStatistics();

    /**
     * 按票据类型统计
     *
     * @return 类型统计数据列表
     */
    List<Map<String, Object>> getBillTypeStatistics();

    /**
     * 按票据状态统计
     *
     * @return 状态统计数据列表
     */
    List<Map<String, Object>> getBillStatusStatistics();

    /**
     * 按承兑银行统计
     *
     * @return 银行统计数据列表
     */
    List<Map<String, Object>> getBillBankStatistics();

    /**
     * 按金额区间统计
     *
     * @return 金额区间统计数据列表
     */
    List<Map<String, Object>> getBillAmountRangeStatistics();

    /**
     * 按日期统计票据趋势
     *
     * @param params 查询参数（startDate, endDate）
     * @return 每日趋势数据列表
     */
    List<Map<String, Object>> getBillDailyTrend(@Param("startDate") java.util.Date startDate,
                                                 @Param("endDate") java.util.Date endDate);

    /**
     * 获取上月统计数据
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
     * 获取统计表格数据
     *
     * @param params 查询参数
     * @return 统计表格数据列表
     */
    List<Map<String, Object>> getStatisticsTableData(Map<String, Object> params);
}

