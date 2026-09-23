package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblLeasePayment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 租金计划Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-02-09
 */
@Mapper
public interface LeasePaymentMapper extends BaseMapper<TblLeasePayment> {

    /**
     * 根据租赁ID查询租金计划列表
     */
    List<TblLeasePayment> selectByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 根据付款ID查询付款详情
     */
    TblLeasePayment selectByPaymentId(@Param("paymentId") Long paymentId);

    /**
     * 更新付款状态
     */
    int updatePaymentStatus(@Param("paymentId") Long paymentId, 
                           @Param("status") String status,
                           @Param("paidAmount") BigDecimal paidAmount,
                           @Param("paidDate") Date paidDate);

    /**
     * 批量更新付款状态
     */
    int batchUpdatePaymentStatus(@Param("paymentIds") List<Long> paymentIds,
                                @Param("status") String status,
                                @Param("paidDate") Date paidDate);

    /**
     * 根据租赁ID删除所有租金计划
     */
    int deleteByLeaseId(@Param("leaseId") Long leaseId);

    /**
     * 统计租金汇总信息
     */
    Map<String, Object> selectPaymentSummary(@Param("leaseId") Long leaseId);

    /**
     * 查询逾期租金
     */
    List<TblLeasePayment> selectOverduePayments(@Param("leaseId") Long leaseId);

    /**
     * 批量插入租金计划
     */
    int batchInsert(@Param("payments") List<TblLeasePayment> payments);
}

