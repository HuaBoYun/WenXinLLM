package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblProductTransactionEvent;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 产品交易事件Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblProductTransactionEventMapper extends BaseMapper<TblProductTransactionEvent> {

    int checkCodeUnique(@Param("eventCode") String eventCode, @Param("excludeId") Long excludeId);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    List<TblProductTransactionEvent> selectByProductType(@Param("productType") String productType, @Param("orgId") Long orgId);

    List<TblProductTransactionEvent> selectByTransactionTypeId(@Param("transactionTypeId") Long transactionTypeId, @Param("orgId") Long orgId);

    int countUsage(@Param("eventId") Long eventId);
}

