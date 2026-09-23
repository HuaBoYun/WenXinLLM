package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.ReminderStrategy;
import com.management.accountant.oracle.mapper.advanced.ReminderStrategyMapper;
import com.management.accountant.oracle.service.advanced.ReminderStrategyService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Primary
@Service("reminderStrategyServiceOracle")
public class ReminderStrategyServiceImpl implements ReminderStrategyService {

    @Resource
    private ReminderStrategyMapper strategyMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<ReminderStrategy> selectList(Map<String, Object> params) {
        QueryWrapper<ReminderStrategy> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("STRATEGY_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return strategyMapper.selectList(w);
    }

    @Override
    public Page<ReminderStrategy> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        QueryWrapper<ReminderStrategy> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("STRATEGY_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return strategyMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public ReminderStrategy selectById(String strategyId) { return strategyMapper.selectById(strategyId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(ReminderStrategy strategy) {
        strategy.setStrategyId("RS" + idWorker.nextId());
        strategy.setCreateTime(new Date());
        strategy.setDelFlag(0);
        return strategyMapper.insert(strategy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(ReminderStrategy strategy) {
        strategy.setUpdateTime(new Date());
        return strategyMapper.updateById(strategy) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        ReminderStrategy s = strategyMapper.selectById(id);
        if (s != null) { s.setDelFlag(1); s.setUpdateTime(new Date()); return strategyMapper.updateById(s) > 0; }
        return false;
    }

    @Override
    public boolean executeStrategy(String id) {
        ReminderStrategy s = strategyMapper.selectById(id);
        if (s == null) return false;
        s.setLastExecutionTime(new Date());
        s.setExecutionCount(s.getExecutionCount() != null ? s.getExecutionCount() + 1 : 1);
        s.setUpdateTime(new Date());
        return strategyMapper.updateById(s) > 0;
    }

    @Override
    public boolean enableStrategy(String id) {
        ReminderStrategy s = strategyMapper.selectById(id);
        if (s == null) return false;
        s.setIsEnabled(true);
        s.setUpdateTime(new Date());
        return strategyMapper.updateById(s) > 0;
    }

    @Override
    public boolean disableStrategy(String id) {
        ReminderStrategy s = strategyMapper.selectById(id);
        if (s == null) return false;
        s.setIsEnabled(false);
        s.setUpdateTime(new Date());
        return strategyMapper.updateById(s) > 0;
    }

    @Override
    public List<ReminderStrategy> selectByReminderType(String reminderType) {
        QueryWrapper<ReminderStrategy> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0).eq("REMINDER_TYPE", reminderType);
        return strategyMapper.selectList(w);
    }
}
