package com.huabo.bigmodel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.bigmodel.entity.ScheduledTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI办公定时任务Mapper
 */
@Mapper
public interface ScheduledTaskMapper extends BaseMapper<ScheduledTask> {
}
