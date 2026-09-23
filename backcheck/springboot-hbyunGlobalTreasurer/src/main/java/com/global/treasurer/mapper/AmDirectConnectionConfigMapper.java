package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.AmDirectConnectionConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface AmDirectConnectionConfigMapper extends BaseMapper<AmDirectConnectionConfig> {
    List<AmDirectConnectionConfig> selectByParams(Map<String, Object> params);
    Map<String, Object> selectStatistics();
}

