package com.global.treasurer.service;

import com.global.treasurer.entity.TcInterestRate;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 利率数据管理Service
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcInterestRateService {

    /**
     * 分页查询利率数据列表
     * 
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param rateType 利率类型
     * @param rateCode 利率编码
     * @param rateName 利率名称
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param dataSource 数据来源
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 状态
     * @return 分页结果
     */
    PageInfo<TcInterestRate> getList(int pageNum, int pageSize, String rateType, String rateCode, String rateName,
                                     String currencyCode, String termType, String dataSource, Date startDate, 
                                     Date endDate, String status);

    /**
     * 新增或更新利率数据
     * 
     * @param interestRate 利率数据信息
     * @return 保存后的利率数据信息
     */
    TcInterestRate saveOrUpdate(TcInterestRate interestRate);

    /**
     * 根据ID删除利率数据
     * 
     * @param id 利率ID
     */
    void delete(String id);

    /**
     * 根据ID查询利率数据详情
     * 
     * @param id 利率ID
     * @return 利率数据信息
     */
    TcInterestRate getById(String id);

    /**
     * 根据利率编码和日期查询利率
     * 
     * @param rateDate 利率日期
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @return 利率数据信息
     */
    TcInterestRate getByDateAndCode(Date rateDate, String rateCode, String currencyCode, String termType, Integer termValue);

    /**
     * 根据利率编码查询最新利率
     * 
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @return 最新利率数据信息
     */
    TcInterestRate getLatestByCode(String rateCode, String currencyCode, String termType, Integer termValue);

    /**
     * 根据日期查询所有利率
     * 
     * @param rateDate 利率日期
     * @return 利率数据列表
     */
    List<TcInterestRate> getByDate(Date rateDate);

    /**
     * 查询指定日期范围内的利率历史
     * 
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 利率历史列表
     */
    List<TcInterestRate> getHistoryByDateRange(String rateCode, String currencyCode, String termType, Integer termValue,
                                               Date startDate, Date endDate);

    /**
     * 根据利率类型查询利率列表
     * 
     * @param rateType 利率类型
     * @return 利率数据列表
     */
    List<TcInterestRate> getByRateType(String rateType);

    /**
     * 根据货币代码查询利率列表
     * 
     * @param currencyCode 货币代码
     * @return 利率数据列表
     */
    List<TcInterestRate> getByCurrencyCode(String currencyCode);

    /**
     * 查询所有利率类型
     * 
     * @return 利率类型列表
     */
    List<String> getAllRateTypes();

    /**
     * 查询所有利率编码
     * 
     * @return 利率编码列表
     */
    List<String> getAllRateCodes();

    /**
     * 查询所有货币代码
     * 
     * @return 货币代码列表
     */
    List<String> getAllCurrencyCodes();

    /**
     * 查询所有期限类型
     * 
     * @return 期限类型列表
     */
    List<String> getAllTermTypes();

    /**
     * 查询所有数据来源
     * 
     * @return 数据来源列表
     */
    List<String> getAllDataSources();

    /**
     * 批量导入利率数据
     * 
     * @param interestRates 利率数据列表
     * @param updateMode 更新模式：INSERT-仅插入，UPDATE-仅更新，UPSERT-插入或更新
     * @return 导入结果
     */
    Map<String, Object> batchImport(List<TcInterestRate> interestRates, String updateMode);

    /**
     * 批量更新利率数据
     * 
     * @param interestRates 利率数据列表
     * @return 更新结果
     */
    Map<String, Object> batchUpdate(List<TcInterestRate> interestRates);

    /**
     * 根据日期删除利率数据
     * 
     * @param rateDate 利率日期
     * @return 删除数量
     */
    int deleteByDate(Date rateDate);

    /**
     * 获取利率统计信息
     * 
     * @return 统计结果
     */
    Map<String, Object> getStatistics();

    /**
     * 检查利率数据是否存在
     * 
     * @param rateDate 利率日期
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @return 是否存在
     */
    boolean checkExists(Date rateDate, String rateCode, String currencyCode, String termType, Integer termValue);

    /**
     * 更新利率状态
     * 
     * @param id 利率ID
     * @param status 状态
     * @param updateUser 更新人
     */
    void updateStatus(String id, String status, String updateUser);

    /**
     * 同步利率数据
     * 
     * @param dataSource 数据来源
     * @param rateDate 利率日期
     * @return 同步结果
     */
    Map<String, Object> syncInterestRates(String dataSource, Date rateDate);

    /**
     * 验证利率数据
     * 
     * @param interestRate 利率数据
     * @return 验证结果
     */
    Map<String, Object> validateInterestRate(TcInterestRate interestRate);

    /**
     * 获取利率趋势分析
     * 
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势分析结果
     */
    Map<String, Object> getTrendAnalysis(String rateCode, String currencyCode, String termType, Integer termValue,
                                         Date startDate, Date endDate);

    /**
     * 获取利率对比分析
     * 
     * @param rateCodes 利率编码列表
     * @param currencyCode 货币代码
     * @param rateDate 利率日期
     * @return 对比分析结果
     */
    Map<String, Object> getComparisonAnalysis(List<String> rateCodes, String currencyCode, Date rateDate);
}
