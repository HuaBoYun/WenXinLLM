package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblSealUsageRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 印鉴使用记录Mapper接口
 * 基于 01_create_seal_usage_record.sql 表结构
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
@Mapper
public interface TblSealUsageRecordMapper extends BaseMapper<TblSealUsageRecord> {

    /**
     * 根据主键ID查询
     */
    TblSealUsageRecord selectByPrimaryId(@Param("id") Long id);

    /**
     * 条件查询
     */
    List<TblSealUsageRecord> selectByCondition(Map<String, Object> params);

    /**
     * 统计总数
     */
    Long countTotal();

    /**
     * 统计今日使用次数
     */
    Long countToday();

    /**
     * 统计本月使用次数
     */
    Long countMonth();

    /**
     * 按状态统计
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 按业务类型统计
     */
    List<Map<String, Object>> countByBusinessType();

    /**
     * 获取下一个ID
     */
    Long getNextId();
}
