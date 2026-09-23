package com.management.accountant.oracle.mapper.integration;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.integration.BudgetDataStreamLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BudgetDataStreamLogMapper extends BaseMapper<BudgetDataStreamLog> {

    List<BudgetDataStreamLog> listByStreamId(@Param("streamId") String streamId);
}
