package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.BillPoolQueryDTO;
import com.global.treasurer.entity.TblBillPool;
import com.global.treasurer.vo.BillPoolVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据池Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillPoolMapper extends BaseMapper<TblBillPool> {

    /**
     * 查询票据池列表
     *
     * @param queryDTO 查询条件
     * @return 票据池列表
     */
    List<BillPoolVO> selectBillPoolList(@Param("query") BillPoolQueryDTO queryDTO);

    /**
     * 根据ID查询票据池详情
     *
     * @param poolId 票据池ID
     * @return 票据池详情
     */
    BillPoolVO selectBillPoolById(@Param("poolId") Long poolId);

    /**
     * 根据票据池编码查询
     *
     * @param poolCode 票据池编码
     * @return 票据池信息
     */
    TblBillPool selectByPoolCode(@Param("poolCode") String poolCode);

    /**
     * 批量删除票据池
     *
     * @param poolIds 票据池ID数组
     * @return 影响行数
     */
    int deleteBillPoolByIds(@Param("poolIds") Long[] poolIds);

    /**
     * 更新票据池统计信息
     *
     * @param poolId 票据池ID
     * @param totalAmount 总金额
     * @param billCount 票据数量
     * @return 影响行数
     */
    int updatePoolStatistics(@Param("poolId") Long poolId,
                           @Param("totalAmount") java.math.BigDecimal totalAmount,
                           @Param("billCount") Integer billCount);
}

