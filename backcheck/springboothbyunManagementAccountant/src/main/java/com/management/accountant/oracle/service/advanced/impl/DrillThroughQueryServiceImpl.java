package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.DrillThroughQuery;
import com.management.accountant.oracle.mapper.advanced.DrillThroughQueryMapper;
import com.management.accountant.oracle.service.advanced.DrillThroughQueryService;
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
@Service("drillThroughQueryServiceOracle")
public class DrillThroughQueryServiceImpl implements DrillThroughQueryService {

    @Resource
    private DrillThroughQueryMapper queryMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<DrillThroughQuery> selectList(Map<String, Object> params) {
        QueryWrapper<DrillThroughQuery> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("QUERY_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return queryMapper.selectList(w);
    }

    @Override
    public Page<DrillThroughQuery> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        QueryWrapper<DrillThroughQuery> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("QUERY_NAME", keyword);
        }
        w.orderByDesc("CREATE_TIME");
        return queryMapper.selectPage(new Page<>(pageNum, pageSize), w);
    }

    @Override
    public DrillThroughQuery selectById(String queryId) { return queryMapper.selectById(queryId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(DrillThroughQuery query) {
        query.setCreateTime(new Date());
        query.setDelFlag(0);
        return queryMapper.insert(query) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(DrillThroughQuery query) {
        query.setUpdateTime(new Date());
        return queryMapper.updateById(query) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        DrillThroughQuery q = queryMapper.selectById(id);
        if (q != null) { q.setDelFlag(1); q.setUpdateTime(new Date()); return queryMapper.updateById(q) > 0; }
        return false;
    }

    @Override
    public boolean executeQuery(String queryId) {
        DrillThroughQuery q = queryMapper.selectById(queryId);
        if (q == null) return false;
        q.setQueryStatus("RUNNING");
        q.setUpdateTime(new Date());
        return queryMapper.updateById(q) > 0;
    }

    @Override
    public Map<String, Object> getQueryResult(String queryId) {
        Map<String, Object> result = new HashMap<>();
        result.put("queryId", queryId);
        result.put("data", new ArrayList<>());
        return result;
    }
}
