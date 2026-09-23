package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.ShareholderAnalysis;
import org.apache.ibatis.annotations.Mapper;

/**
 * 股东穿透分析数据访问接口
 *
 * @author system
 * @since 2025-01-01
 */
@Mapper
public interface ShareholderAnalysisMapper extends BaseMapper<ShareholderAnalysis> {
}
