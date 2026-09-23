package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.DrillThroughQuery;

import java.util.List;
import java.util.Map;

/**
 * 穿透查询Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface DrillThroughQueryService {

    /**
     * 查询穿透查询列表
     */
    List<DrillThroughQuery> selectList(Map<String, Object> params);

    /**
     * 分页查询穿透查询列表
     */
    Page<DrillThroughQuery> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询穿透查询
     */
    DrillThroughQuery selectById(String queryId);

    /**
     * 新增穿透查询
     */
    boolean insert(DrillThroughQuery query);

    /**
     * 修改穿透查询
     */
    boolean update(DrillThroughQuery query);

    /**
     * 删除穿透查询
     */
    boolean deleteById(String queryId);

    /**
     * 执行穿透查询
     */
    boolean executeQuery(String queryId);

    /**
     * 获取查询结果
     */
    Map<String, Object> getQueryResult(String queryId);
}

