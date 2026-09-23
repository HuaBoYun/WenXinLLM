package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetMqMessage;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BudgetMqMessageMapper extends BaseMapper<BudgetMqMessage> {
}
