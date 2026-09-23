package com.global.treasurer.service.xjgl.dataRulesManage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtExchangeRateConfig;

import java.util.Map;

/**
 * 汇率配置Service接口
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
public interface ExchangeRateConfigService extends IService<TblGtExchangeRateConfig> {

    /**
     * 获取汇率配置列表(分页)
     *
     * @param params 查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<TblGtExchangeRateConfig> getExchangeRateConfigList(Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 新增汇率配置
     *
     * @param config 汇率配置实体
     * @return 影响行数
     */
    int createExchangeRateConfig(TblGtExchangeRateConfig config);

    /**
     * 更新汇率配置
     *
     * @param config 汇率配置实体
     * @return 影响行数
     */
    int updateExchangeRateConfig(TblGtExchangeRateConfig config);

    /**
     * 删除汇率配置
     *
     * @param id 配置ID
     * @return 影响行数
     */
    int deleteExchangeRateConfig(Long id);

    /**
     * 获取汇率配置统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 同步汇率配置
     *
     * @param sourceType 数据源类型
     * @return 同步数量
     */
    int syncExchangeRateConfig(String sourceType);

    /**
     * 更新汇率配置状态
     *
     * @param id 配置ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(Long id, Integer status);

    /**
     * 批量删除汇率配置
     *
     * @param ids ID列表
     * @return 影响行数
     */
    int batchDelete(java.util.List<Long> ids);
}

