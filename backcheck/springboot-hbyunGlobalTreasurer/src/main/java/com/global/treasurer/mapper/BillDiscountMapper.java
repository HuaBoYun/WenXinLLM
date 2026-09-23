package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillDiscount;
import com.global.treasurer.dto.BillDiscountQueryDTO;
import com.global.treasurer.vo.BillDiscountVO;
import com.global.treasurer.vo.CirculationRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据贴现Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillDiscountMapper extends BaseMapper<TblBillDiscount> {

    /**
     * 查询票据贴现列表
     *
     * @param queryDTO 查询条件
     * @return 票据贴现列表
     */
    List<BillDiscountVO> selectBillDiscountList(@Param("query") BillDiscountQueryDTO queryDTO);

    /**
     * 根据ID查询票据贴现详情
     *
     * @param discountId 贴现ID
     * @return 票据贴现详情
     */
    BillDiscountVO selectBillDiscountById(@Param("discountId") Long discountId);

    /**
     * 根据贴现编号查询贴现
     *
     * @param discountNumber 贴现编号
     * @return 贴现信息
     */
    TblBillDiscount selectByDiscountNumber(@Param("discountNumber") String discountNumber);

    /**
     * 批量删除票据贴现
     *
     * @param discountIds 贴现ID数组
     * @return 影响行数
     */
    int deleteBillDiscountByIds(@Param("discountIds") Long[] discountIds);

    /**
     * 根据票据ID查询流转记录（贴现）
     *
     * @param billId 票据ID
     * @return 流转记录列表
     */
    List<CirculationRecordVO> selectCirculationByBillId(@Param("billId") Long billId);

    /**
     * 查询可贴现票据列表
     * 查询状态为正常持有且未到期的票据
     *
     * @param params 查询参数
     * @return 可贴现票据列表
     */
    List<java.util.Map<String, Object>> selectAvailableBillsForDiscount(@Param("params") java.util.Map<String, Object> params);

    /**
     * 查询贴现统计数据
     *
     * @param query 查询条件
     * @return 统计数据 Map，包含 totalCount, pendingCount, completedCount, totalAmount
     */
    java.util.Map<String, Object> selectDiscountStatistics(@Param("query") BillDiscountQueryDTO query);
}

