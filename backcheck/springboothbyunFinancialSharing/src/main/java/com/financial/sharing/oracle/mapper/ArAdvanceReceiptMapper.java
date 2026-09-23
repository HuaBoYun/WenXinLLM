package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dto.param.ArAdvanceReceiptQueryParam;
import com.financial.sharing.oracle.entity.ArAdvanceReceiptEntity;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预收款Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArAdvanceReceiptMapper extends BaseMapper<ArAdvanceReceiptEntity> {

    /**
     * 分页查询预收款
     */
    IPage<ArAdvanceReceiptEntity> selectAdvanceReceiptPage(Page<ArAdvanceReceiptEntity> page, @Param("param") ArAdvanceReceiptQueryParam param);

    /**
     * 查询预收款列表（配合PageHelper使用）
     */
    List<ArAdvanceReceiptEntity> selectAdvanceReceiptList(@Param("param") ArAdvanceReceiptQueryParam param);

    /**
     * 根据预收款单号查询
     */
    ArAdvanceReceiptEntity selectByAdvanceNo(@Param("advanceNo") String advanceNo, @Param("tenantId") Long tenantId);

    /**
     * 查询客户预收款
     */
    List<ArAdvanceReceiptEntity> selectByCustomerId(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 查询可冲销的预收款
     */
    List<ArAdvanceReceiptEntity> selectAvailableForOffset(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 更新已冲销金额
     */
    int updateOffsetAmount(@Param("advanceId") String advanceId, @Param("amount") BigDecimal amount);

    /**
     * 查询预收款统计
     */
    Map<String, Object> selectAdvanceReceiptStatistics(@Param("param") ArAdvanceReceiptQueryParam param);
}

