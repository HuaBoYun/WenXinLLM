package com.huabo.bigmodel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.bigmodel.entity.ScheduledTaskLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * AI办公定时任务执行日志Mapper
 */
@Mapper
public interface ScheduledTaskLogMapper extends BaseMapper<ScheduledTaskLog> {
}
