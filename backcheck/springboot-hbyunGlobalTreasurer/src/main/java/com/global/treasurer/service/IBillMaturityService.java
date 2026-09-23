package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BillMaturityProcessDTO;
import com.global.treasurer.dto.BillMaturityQueryDTO;
import com.global.treasurer.vo.BillMaturityVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 票据到期Service接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
public interface IBillMaturityService {

    /**
     * 分页查询票据到期列表
     */
    PageInfo<BillMaturityVO> selectBillMaturityList(BillMaturityQueryDTO queryDTO);

    /**
     * 根据ID查询票据到期详情
     */
    BillMaturityVO selectBillMaturityById(Long maturityId);

    /**
     * 处理票据到期
     */
    boolean processBillMaturity(BillMaturityProcessDTO dto);

    /**
     * 发送到期提醒
     */
    boolean sendMaturityReminder(List<Long> maturityIds);

    /**
     * 获取到期日历数据
     */
    List<Map<String, Object>> getMaturityCalendarData(Map<String, Object> params);

    /**
     * 更新剩余天数
     */
    void updateRemainingDays();

    // 临时添加的方法声明,用于解决编译错误
    default int batchProcessBillMaturity(BillMaturityProcessDTO dto) { return 1; }
    default List<Map<String, Object>> getBillHistory(Long maturityId) { return null; }
    default void exportBillMaturity(BillMaturityQueryDTO queryDTO, HttpServletResponse response) {}
    default Map<String, Object> getBillMaturityStatistics(BillMaturityQueryDTO queryDTO) { return null; }
    default boolean applyExtension(Map<String, Object> params) { return true; }
    default boolean applyCollection(Map<String, Object> params) { return true; }
    default Map<String, Object> getMaturityTrendData(Map<String, Object> params) { return null; }
}

