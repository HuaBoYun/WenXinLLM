package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillMaturity;
import com.global.treasurer.dto.BillMaturityQueryDTO;
import com.global.treasurer.vo.BillMaturityVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 票据到期Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillMaturityMapper extends BaseMapper<TblBillMaturity> {

    /**
     * 查询票据到期列表
     *
     * @param queryDTO 查询条件
     * @return 票据到期列表
     */
    List<BillMaturityVO> selectBillMaturityList(@Param("query") BillMaturityQueryDTO queryDTO);

    /**
     * 根据ID查询票据到期详情
     *
     * @param maturityId 到期记录ID
     * @return 票据到期详情
     */
    BillMaturityVO selectBillMaturityById(@Param("maturityId") Long maturityId);

    /**
     * 批量更新处理状态
     *
     * @param maturityIds 到期记录ID列表
     * @param processType 处理类型
     * @param processDate 处理日期
     * @param processDescription 处理说明
     * @return 影响行数
     */
    int batchUpdateProcessStatus(@Param("maturityIds") List<Long> maturityIds,
                                  @Param("processType") String processType,
                                  @Param("processDate") Date processDate,
                                  @Param("processDescription") String processDescription);

    /**
     * 批量更新提醒状态
     *
     * @param maturityIds 到期记录ID列表
     * @param reminderStatus 提醒状态
     * @return 影响行数
     */
    int batchUpdateReminderStatus(@Param("maturityIds") List<Long> maturityIds,
                                   @Param("reminderStatus") String reminderStatus);

    /**
     * 获取到期日历数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param companyId 公司ID
     * @return 日历数据
     */
    List<Map<String, Object>> selectMaturityCalendarData(@Param("startDate") Date startDate,
                                                          @Param("endDate") Date endDate,
                                                          @Param("companyId") Long companyId);

    /**
     * 更新所有记录的剩余天数
     *
     * @return 影响行数
     */
    int updateAllRemainingDays();

    /**
     * 获取票据到期统计数据
     *
     * @param queryDTO 查询条件
     * @return 统计数据
     */
    Map<String, Object> selectBillMaturityStatistics(@Param("query") BillMaturityQueryDTO queryDTO);

    /**
     * 获取票据到期趋势数据
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据列表
     */
    List<Map<String, Object>> selectMaturityTrendData(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}

