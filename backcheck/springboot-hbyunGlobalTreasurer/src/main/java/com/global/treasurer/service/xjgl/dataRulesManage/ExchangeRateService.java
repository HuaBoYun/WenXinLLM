package com.global.treasurer.service.xjgl.dataRulesManage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtExchangeRate;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 汇率管理Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
public interface ExchangeRateService extends IService<TblGtExchangeRate> {

    /**
     * 获取汇率列表(分页)
     *
     * @param params 查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<TblGtExchangeRate> getExchangeRateList(Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 新增汇率
     *
     * @param exchangeRate 汇率实体
     * @return 影响行数
     */
    int createExchangeRate(TblGtExchangeRate exchangeRate);

    /**
     * 更新汇率
     *
     * @param exchangeRate 汇率实体
     * @return 影响行数
     */
    int updateExchangeRate(TblGtExchangeRate exchangeRate);

    /**
     * 删除汇率
     *
     * @param rateId 汇率ID
     * @return 影响行数
     */
    int deleteExchangeRate(Long rateId);

    /**
     * 获取汇率统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 同步汇率
     *
     * @param sourceType 数据源类型
     * @return 同步数量
     */
    int syncExchangeRate(String sourceType);

    /**
     * 批量删除汇率
     *
     * @param ids 汇率ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新汇率状态
     *
     * @param id 汇率ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取趋势分析数据
     *
     * @param params 查询参数
     * @return 趋势分析数据
     */
    Map<String, Object> getTrendAnalysis(Map<String, Object> params);
}