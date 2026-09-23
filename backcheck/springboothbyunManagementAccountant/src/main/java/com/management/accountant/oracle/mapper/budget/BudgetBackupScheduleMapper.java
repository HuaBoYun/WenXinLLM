package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetBackupSchedule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预算定时备份计划Mapper
 */
@Mapper
public interface BudgetBackupScheduleMapper extends BaseMapper<BudgetBackupSchedule> {
}
