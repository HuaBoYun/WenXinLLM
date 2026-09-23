package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblPrepaymentWriteoff;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 预付款核销记录 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizPrepaymentWriteoffMapper")
public interface PrepaymentWriteoffMapper extends BaseMapper<TblPrepaymentWriteoff> {

    /**
     * 根据预付款单ID查询核销记录
     *
     * @param prepaymentId 预付款单ID
     * @return 核销记录列表
     */
    List<TblPrepaymentWriteoff> selectByPrepaymentId(@Param("prepaymentId") String prepaymentId);

    /**
     * 统计已核销金额
     *
     * @param prepaymentId 预付款单ID
     * @return 已核销金额
     */
    BigDecimal sumWriteoffAmount(@Param("prepaymentId") String prepaymentId);
}
