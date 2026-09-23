package com.global.treasurer.financialProductDefinition.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.financialProductDefinition.entity.TblTransactionType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 交易类型Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Mapper
public interface TblTransactionTypeMapper extends BaseMapper<TblTransactionType> {

    int checkCodeUnique(@Param("transactionTypeCode") String transactionTypeCode, @Param("excludeId") Long excludeId);

    int batchUpdateSort(@Param("list") List<TblTransactionType> list);

    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("isEnabled") Integer isEnabled, @Param("updateBy") String updateBy);

    int countUsage(@Param("transactionTypeId") Long transactionTypeId);
}

