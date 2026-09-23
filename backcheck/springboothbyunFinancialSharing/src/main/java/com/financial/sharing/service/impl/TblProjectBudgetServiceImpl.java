package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.entity.TblProjectBudget;
import com.financial.sharing.mapper.TblProjectBudgetMapper;
import com.financial.sharing.service.TblProjectBudgetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

/**
 * 项目预算Service实现
 */
@Slf4j
@Service
public class TblProjectBudgetServiceImpl extends ServiceImpl<TblProjectBudgetMapper, TblProjectBudget> 
        implements TblProjectBudgetService {

    @Override
    public List<TblProjectBudget> getByProjectId(String projectId) {
        return this.baseMapper.selectByProjectId(projectId);
    }

    @Override
    public TblProjectBudget getByProjectAndYearMonth(String projectId, Integer year, Integer month) {
        return this.baseMapper.selectByProjectAndYearMonth(projectId, year, month);
    }

    @Override
    public TblProjectBudget getByBudgetCode(String budgetCode) {
        return this.baseMapper.selectByBudgetCode(budgetCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatchBudgets(List<TblProjectBudget> budgets) {
        try {
            return this.saveBatch(budgets);
        } catch (Exception e) {
            log.error("批量保存项目预算失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByProjectId(String projectId) {
        try {
            return this.baseMapper.deleteByProjectId(projectId) > 0;
        } catch (Exception e) {
            log.error("删除项目预算失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }
}

