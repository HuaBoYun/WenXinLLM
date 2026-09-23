package com.global.treasurer.service;

import com.global.treasurer.entity.AmDirectConnectionConfig;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.Map;

/**
 * 银行直连配置服务接口
 * @author AI Assistant
 * @date 2025-01-26
 */
public interface AmDirectConnectionConfigService {

    PageInfo<AmDirectConnectionConfig> list(Integer pageNum, Integer pageSize, Map<String, Object> params);

    AmDirectConnectionConfig getById(Long id);

    boolean save(AmDirectConnectionConfig config);

    boolean update(AmDirectConnectionConfig config);

    boolean delete(Long id);

    boolean batchDelete(List<Long> ids);

    Map<String, Object> testConnection(Long id);

    Map<String, Object> batchTestConnection(List<Long> ids);

    Map<String, Object> getStatistics();

    List<AmDirectConnectionConfig> exportConfigs(Map<String, Object> params);
}

