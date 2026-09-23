package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetYear;
import com.management.accountant.oracle.mapper.budget.BudgetYearMapper;
import com.management.accountant.service.BudgetYearService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BudgetYearServiceImpl implements BudgetYearService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetYearMapper yearMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetYear create(BudgetYear year) {
        if (year == null) throw new ServiceException("年度信息不能为空");
        if (year.getDelFlag() == null) year.setDelFlag(0);
        if (year.getYearStatus() == null) year.setYearStatus("CLOSED");
        year.setCreateTime(new Date());
        year.setUpdateTime(new Date());
        yearMapper.insert(year);
        return year;
    }

    @Override
    public BudgetYear getById(String id) {
        QueryWrapper<BudgetYear> w = new QueryWrapper<>();
        w.eq("YEAR_ID", id).eq("DEL_FLAG", 0);
        return yearMapper.selectOne(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetYear year) {
        if (year == null || !StringUtils.hasText(year.getYearId())) {
            throw new ServiceException("年度ID不能为空");
        }
        year.setUpdateTime(new Date());
        yearMapper.updateById(year);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        BudgetYear year = new BudgetYear();
        year.setYearId(id);
        year.setDelFlag(1);
        year.setUpdateTime(new Date());
        yearMapper.updateById(year);
    }

    @Override
    public IPage<BudgetYear> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
        QueryWrapper<BudgetYear> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        w.orderByDesc("BUDGET_YEAR");
        return yearMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public List<BudgetYear> getAllYears() {
        QueryWrapper<BudgetYear> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        w.orderByDesc("BUDGET_YEAR");
        return yearMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void openYear(String yearId) {
        BudgetYear year = new BudgetYear();
        year.setYearId(yearId);
        year.setYearStatus("OPEN");
        year.setUpdateTime(new Date());
        yearMapper.updateById(year);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeYear(String yearId) {
        BudgetYear year = new BudgetYear();
        year.setYearId(yearId);
        year.setYearStatus("CLOSED");
        year.setUpdateTime(new Date());
        yearMapper.updateById(year);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCurrent(String yearId) {
        // 先将所有年度的 IS_CURRENT 清零
        QueryWrapper<BudgetYear> clearWrapper = new QueryWrapper<>();
        clearWrapper.eq("DEL_FLAG", 0).eq("IS_CURRENT", 1);
        List<BudgetYear> currentList = yearMapper.selectList(clearWrapper);
        for (BudgetYear y : currentList) {
            BudgetYear update = new BudgetYear();
            update.setYearId(y.getYearId());
            update.setIsCurrent(0);
            update.setUpdateTime(new Date());
            yearMapper.updateById(update);
        }
        // 再将目标年度设为当前
        BudgetYear target = new BudgetYear();
        target.setYearId(yearId);
        target.setIsCurrent(1);
        target.setUpdateTime(new Date());
        yearMapper.updateById(target);
    }
}
