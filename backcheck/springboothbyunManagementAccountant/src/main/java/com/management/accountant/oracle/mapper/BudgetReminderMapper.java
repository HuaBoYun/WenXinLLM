package com.management.accountant.oracle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetReminder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 预算催报管理Mapper接口
 *
 * @description 预算催报管理数据访问层
 * @author AI Assistant
 * @date 2025-01-04
 */
@Mapper
public interface BudgetReminderMapper extends BaseMapper<BudgetReminder> {

    /**
     * 查询今日催报发送统计（发送数、目标数、响应数）
     */
    Map<String, Object> selectTodayStats();

    /**
     * 分页查询催报记录
     */
    List<Map<String, Object>> selectReminderRecords(@Param("reminderId") String reminderId,
                                                     @Param("offset") int offset,
                                                     @Param("pageSize") int pageSize);

    /**
     * 查询催报记录总数
     */
    int countReminderRecords(@Param("reminderId") String reminderId);
}

