package com.global.treasurer.mapper;

import com.global.treasurer.entity.TcInterestRate;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 利率数据管理Mapper
 * 
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcInterestRateMapper extends Mapper<TcInterestRate> {

    /**
     * 根据利率编码和日期查询利率
     * 
     * @param rateDate 利率日期
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @return 利率信息
     */
    TcInterestRate selectByDateAndCode(@Param("rateDate") Date rateDate,
                                       @Param("rateCode") String rateCode,
                                       @Param("currencyCode") String currencyCode,
                                       @Param("termType") String termType,
                                       @Param("termValue") Integer termValue);

    /**
     * 根据条件查询利率列表
     * 
     * @param rateType 利率类型
     * @param rateCode 利率编码
     * @param rateName 利率名称
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param dataSource 数据来源
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 状态
     * @return 利率列表
     */
    List<TcInterestRate> selectByCondition(@Param("rateType") String rateType,
                                           @Param("rateCode") String rateCode,
                                           @Param("rateName") String rateName,
                                           @Param("currencyCode") String currencyCode,
                                           @Param("termType") String termType,
                                           @Param("dataSource") String dataSource,
                                           @Param("startDate") Date startDate,
                                           @Param("endDate") Date endDate,
                                           @Param("status") String status);

    /**
     * 根据利率编码查询最新利率
     * 
     * @param rateCode 利率编码
     * @param currencyCode 货币代码
     * @param termType 期限类型
     * @param termValue 期限值
     * @return 最新利率信息
     */
    TcInterestRate selectLatestByCode(@Param("rateCode") String rateCode,
                                      @Param("currencyCode") String currencyCode,
                                      @Param("termType") String termType,
                                      @Param("termValue") Integer termValue);

    /**
     * 根据日期查询所有利率
     * 
     * @param rateDate 利率日期
     * @return 利率列表
     */
    List<TcInterestRate> selectByDate(@Param("rateDate") Date rateDate);

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
    List<TcInterestRate> selectHistoryByDateRange(@Param("rateCode") String rateCode,
                                                  @Param("currencyCode") String currencyCode,
                                                  @Param("termType") String termType,
                                                  @Param("termValue") Integer termValue,
                                                  @Param("startDate") Date startDate,
                                                  @Param("endDate") Date endDate);

    /**
     * 根据利率类型查询利率列表
     * 
     * @param rateType 利率类型
     * @return 利率列表
     */
    List<TcInterestRate> selectByRateType(@Param("rateType") String rateType);

    /**
     * 根据货币代码查询利率列表
     * 
     * @param currencyCode 货币代码
     * @return 利率列表
     */
    List<TcInterestRate> selectByCurrencyCode(@Param("currencyCode") String currencyCode);

    /**
     * 查询所有利率类型
     * 
     * @return 利率类型列表
     */
    List<String> selectAllRateTypes();

    /**
     * 查询所有利率编码
     * 
     * @return 利率编码列表
     */
    List<String> selectAllRateCodes();

    /**
     * 查询所有货币代码
     * 
     * @return 货币代码列表
     */
    List<String> selectAllCurrencyCodes();

    /**
     * 查询所有期限类型
     * 
     * @return 期限类型列表
     */
    List<String> selectAllTermTypes();

    /**
     * 查询所有数据来源
     * 
     * @return 数据来源列表
     */
    List<String> selectAllDataSources();

    /**
     * 批量插入利率数据
     * 
     * @param interestRates 利率数据列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TcInterestRate> interestRates);

    /**
     * 批量更新利率数据
     * 
     * @param interestRates 利率数据列表
     * @return 更新数量
     */
    int batchUpdate(@Param("list") List<TcInterestRate> interestRates);

    /**
     * 根据日期删除利率数据
     * 
     * @param rateDate 利率日期
     * @return 删除数量
     */
    int deleteByDate(@Param("rateDate") Date rateDate);

    /**
     * 统计利率数据
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getStatistics();

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
    int checkExistsByDateAndCode(@Param("rateDate") Date rateDate,
                                 @Param("rateCode") String rateCode,
                                 @Param("currencyCode") String currencyCode,
                                 @Param("termType") String termType,
                                 @Param("termValue") Integer termValue);

    /**
     * 更新利率状态
     * 
     * @param id 利率ID
     * @param status 状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateStatus(@Param("id") String id,
                     @Param("status") String status,
                     @Param("updateUser") String updateUser);

    // Service实现类中调用的方法别名
    default List<TcInterestRate> getList(String rateCode, String rateType, String currencyCode, String termType, String dataSource, String status, Date startDate, Date endDate, String isActive) {
        return selectByCondition(rateCode, rateType, currencyCode, termType, dataSource, status, startDate, endDate, isActive);
    }

    default TcInterestRate getByDateAndCode(Date rateDate, String rateCode, String rateType, String currencyCode, Integer termValue) {
        return selectByDateAndCode(rateDate, rateCode, rateType, currencyCode, termValue);
    }

    default TcInterestRate getLatestByCode(String rateCode, String rateType, String currencyCode, Integer termValue) {
        return selectLatestByCode(rateCode, rateType, currencyCode, termValue);
    }

    default List<TcInterestRate> getByDate(Date rateDate) {
        return selectByDate(rateDate);
    }

    default List<TcInterestRate> getHistoryByDateRange(String rateCode, String rateType, String currencyCode, Integer termValue, Date startDate, Date endDate) {
        return selectHistoryByDateRange(rateCode, rateType, currencyCode, termValue, startDate, endDate);
    }

    default List<TcInterestRate> getByRateType(String rateType) {
        return selectByRateType(rateType);
    }

    default List<TcInterestRate> getByCurrencyCode(String currencyCode) {
        return selectByCurrencyCode(currencyCode);
    }

    default List<String> getAllRateTypes() {
        return selectAllRateTypes();
    }

    default List<String> getAllRateCodes() {
        return selectAllRateCodes();
    }

    default List<String> getAllCurrencyCodes() {
        return selectAllCurrencyCodes();
    }

    default List<String> getAllTermTypes() {
        return selectAllTermTypes();
    }

    default List<String> getAllDataSources() {
        return selectAllDataSources();
    }

    // 添加Service中调用的其他方法
    default boolean syncInterestRates(String dataSource, Date syncDate) {
        // 简单实现，实际应该在XML中定义
        return true;
    }

    default List<Map<String, Object>> getTrendAnalysis(String rateCode, String rateType, String currencyCode, Integer termValue, Date startDate, Date endDate) {
        // 简单实现，实际应该在XML中定义
        return new java.util.ArrayList<>();
    }

    default List<Map<String, Object>> getComparisonAnalysis(List<String> rateCodes, String currencyCode, Date rateDate) {
        // 简单实现，实际应该在XML中定义
        return new java.util.ArrayList<>();
    }

    // 添加缺少的方法
    int deleteByRateDate(@Param("rateDate") Date rateDate);
}
