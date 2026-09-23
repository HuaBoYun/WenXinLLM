package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.global.treasurer.entity.DataSourceConfig;
import com.global.treasurer.mapper.DataSourceConfigMapper;
import com.global.treasurer.service.DataSourceConfigService;
import org.springframework.stereotype.Service;

/**
 * DataSourceConfig Service实现类
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@Service
public class DataSourceConfigServiceImpl extends ServiceImpl<DataSourceConfigMapper, DataSourceConfig> implements DataSourceConfigService {
}
