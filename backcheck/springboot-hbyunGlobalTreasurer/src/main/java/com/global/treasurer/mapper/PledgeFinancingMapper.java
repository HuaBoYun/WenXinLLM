package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblPledgeFinancing;
import com.global.treasurer.vo.PledgeFinancingRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 质押融资Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface PledgeFinancingMapper extends BaseMapper<TblPledgeFinancing> {

    /**
     * 查询质押融资记录列表
     *
     * @param poolId 票据池ID
     * @return 融资记录列表
     */
    List<PledgeFinancingRecordVO> selectFinancingRecordsByPoolId(@Param("poolId") Long poolId);

    /**
     * 根据融资编号查询
     *
     * @param financingNumber 融资编号
     * @return 融资信息
     */
    TblPledgeFinancing selectByFinancingNumber(@Param("financingNumber") String financingNumber);

    /**
     * 批量删除质押融资记录
     *
     * @param financingIds 融资ID数组
     * @return 影响行数
     */
    int deletePledgeFinancingByIds(@Param("financingIds") Long[] financingIds);

    /**
     * 更新还款信息
     *
     * @param financingId 融资ID
     * @param actualRepayDate 实际还款日期
     * @param repayStatus 还款状态
     * @return 影响行数
     */
    int updateRepaymentInfo(@Param("financingId") Long financingId,
                           @Param("actualRepayDate") java.util.Date actualRepayDate,
                           @Param("repayStatus") String repayStatus);
}

