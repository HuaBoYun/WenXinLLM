package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBillPoolDetail;
import com.global.treasurer.vo.BillInPoolVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 票据池明细Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Mapper
public interface BillPoolDetailMapper extends BaseMapper<TblBillPoolDetail> {

    /**
     * 查询票据池内的票据列表
     *
     * @param poolId 票据池ID
     * @return 票据列表
     */
    List<BillInPoolVO> selectBillsByPoolId(@Param("poolId") Long poolId);

    /**
     * 批量添加票据到池
     *
     * @param details 明细列表
     * @return 影响行数
     */
    int batchInsertBillPoolDetails(@Param("details") List<TblBillPoolDetail> details);

    /**
     * 批量从池中移除票据
     *
     * @param detailIds 明细ID数组
     * @return 影响行数
     */
    int batchRemoveBills(@Param("detailIds") Long[] detailIds);

    /**
     * 查询票据所在池
     *
     * @param billId 票据ID
     * @return 票据池ID
     */
    Long selectPoolIdByBillId(@Param("billId") Long billId);
}

