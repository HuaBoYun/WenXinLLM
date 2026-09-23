package com.financial.sharing.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.business.entity.TblExpenseProvisionReverse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 费用预提冲销记录 Mapper接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Component("bizExpenseProvisionReverseMapper")
public interface ExpenseProvisionReverseMapper extends BaseMapper<TblExpenseProvisionReverse> {

    /**
     * 根据预提单ID查询冲销记录
     *
     * @param provisionId 预提单ID
     * @return 冲销记录列表
     */
    List<TblExpenseProvisionReverse> selectByProvisionId(@Param("provisionId") String provisionId);

    /**
     * 统计冲销金额
     *
     * @param provisionId 预提单ID
     * @return 冲销金额
     */
    java.math.BigDecimal sumReverseAmount(@Param("provisionId") String provisionId);
}
