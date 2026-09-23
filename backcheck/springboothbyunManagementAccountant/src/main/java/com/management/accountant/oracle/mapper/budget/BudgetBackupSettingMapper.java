package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetBackupSetting;
import org.apache.ibatis.annotations.Mapper;

/**
 * 预算备份设置Mapper
 */
@Mapper
public interface BudgetBackupSettingMapper extends BaseMapper<BudgetBackupSetting> {
}
