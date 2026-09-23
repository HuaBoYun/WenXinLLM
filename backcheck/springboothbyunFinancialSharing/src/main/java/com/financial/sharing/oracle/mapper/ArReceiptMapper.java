package com.financial.sharing.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.oracle.entity.ArReceiptEntity;
import com.financial.sharing.vo.param.ArReceiptQueryParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收款单Mapper接口
 * @author system
 * @since 2026-01-04
 */
public interface ArReceiptMapper extends BaseMapper<ArReceiptEntity> {

    /**
     * 分页查询收款单
     */
    IPage<ArReceiptEntity> selectReceiptPage(Page<ArReceiptEntity> page, @Param("param") ArReceiptQueryParam param);

    /**
     * 查询收款单列表（配合PageHelper使用）
     */
    List<ArReceiptEntity> selectReceiptList(@Param("param") ArReceiptQueryParam param);

    /**
     * 根据收款单号查询
     */
    ArReceiptEntity selectByReceiptNo(@Param("receiptNo") String receiptNo, @Param("tenantId") Long tenantId);

    /**
     * 查询客户收款单
     */
    List<ArReceiptEntity> selectByCustomerId(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 查询待核销的收款单
     */
    List<ArReceiptEntity> selectPendingWriteOff(@Param("customerId") String customerId, @Param("tenantId") Long tenantId);

    /**
     * 更新已核销金额
     */
    int updateWriteOffAmount(@Param("receiptId") String receiptId, @Param("amount") BigDecimal amount);

    /**
     * 确认收款
     */
    int confirmReceipt(@Param("receiptId") String receiptId, 
                       @Param("confirmAmount") BigDecimal confirmAmount,
                       @Param("confirmBy") String confirmBy);

    /**
     * 取消收款单
     */
    int cancelReceipt(@Param("receiptId") String receiptId, 
                      @Param("cancelReason") String cancelReason,
                      @Param("cancelBy") String cancelBy);

    /**
     * 查询收款统计
     */
    Map<String, Object> selectReceiptStatistics(@Param("param") ArReceiptQueryParam param);

    /**
     * 生成收款单号
     */
    String generateReceiptNo(@Param("prefix") String prefix, @Param("tenantId") Long tenantId);
}

