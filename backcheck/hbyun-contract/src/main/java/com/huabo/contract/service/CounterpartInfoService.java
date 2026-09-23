package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.CounterpartInfo;
import com.huabo.contract.vo.CounterpartInfoQueryParam;

import java.util.List;
import java.util.Map;

/**
 * 相对方信息表 服务类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface CounterpartInfoService extends IService<CounterpartInfo> {

    /**
     * 分页查询相对方信息列表
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<CounterpartInfo> getCounterpartInfoList(CounterpartInfoQueryParam param);

    /**
     * 保存相对方信息（新增或修改）
     * 
     * @param counterpartInfo 相对方信息
     * @return 保存结果
     */
    boolean saveCounterpartInfo(CounterpartInfo counterpartInfo);

    /**
     * 根据ID获取相对方信息详情
     * 
     * @param id 主键ID
     * @return 相对方信息详情
     */
    CounterpartInfo getCounterpartInfoById(Long id);

    /**
     * 根据公司名称获取相对方信息
     * 
     * @param companyName 公司名称
     * @return 相对方信息
     */
    CounterpartInfo getCounterpartInfoByCompanyName(String companyName);

    /**
     * 根据公司代码获取相对方信息
     * 
     * @param companyCode 公司代码
     * @return 相对方信息
     */
    CounterpartInfo getCounterpartInfoByCompanyCode(String companyCode);

    /**
     * 删除相对方信息
     * 
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteCounterpartInfo(Long id);

    /**
     * 批量删除相对方信息
     * 
     * @param ids 主键ID列表
     * @return 删除结果
     */
    boolean batchDeleteCounterpartInfo(List<Long> ids);

    /**
     * 检查公司名称是否存在
     * 
     * @param companyName 公司名称
     * @param excludeId 排除的ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsCompanyName(String companyName, Long excludeId);

    /**
     * 检查公司代码是否存在
     * 
     * @param companyCode 公司代码
     * @param excludeId 排除的ID（用于修改时排除自己）
     * @return 存在返回true，不存在返回false
     */
    boolean existsCompanyCode(String companyCode, Long excludeId);

    /**
     * 获取黑名单相对方列表
     * 
     * @return 黑名单相对方列表
     */
    List<CounterpartInfo> getBlacklistCounterparts();

    /**
     * 获取高信用等级相对方列表
     * 
     * @return 高信用等级相对方列表
     */
    List<CounterpartInfo> getHighCreditCounterparts();

    /**
     * 获取低信用等级相对方列表
     * 
     * @return 低信用等级相对方列表
     */
    List<CounterpartInfo> getLowCreditCounterparts();

    /**
     * 获取大型企业相对方列表
     * 
     * @return 大型企业相对方列表
     */
    List<CounterpartInfo> getLargeEnterpriseCounterparts();

    /**
     * 更新黑名单状态
     * 
     * @param id 主键ID
     * @param blacklistFlag 黑名单标识
     * @return 更新结果
     */
    boolean updateBlacklistFlag(Long id, Integer blacklistFlag);

    /**
     * 批量更新黑名单状态
     * 
     * @param ids 主键ID列表
     * @param blacklistFlag 黑名单标识
     * @return 更新结果
     */
    boolean batchUpdateBlacklistFlag(List<Long> ids, Integer blacklistFlag);

    /**
     * 更新信用等级
     * 
     * @param id 主键ID
     * @param creditRating 信用等级
     * @return 更新结果
     */
    boolean updateCreditRating(Long id, String creditRating);

    /**
     * 批量更新信用等级
     * 
     * @param ids 主键ID列表
     * @param creditRating 信用等级
     * @return 更新结果
     */
    boolean batchUpdateCreditRating(List<Long> ids, String creditRating);

    /**
     * 统计相对方信息数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> getCounterpartStatistics(CounterpartInfoQueryParam param);

    /**
     * 获取信用等级分布统计
     * 
     * @param param 查询参数
     * @return 信用等级分布
     */
    List<Map<String, Object>> getCreditRatingDistribution(CounterpartInfoQueryParam param);

    /**
     * 获取注册资本分布统计
     * 
     * @param param 查询参数
     * @return 注册资本分布
     */
    List<Map<String, Object>> getRegisteredCapitalDistribution(CounterpartInfoQueryParam param);

    /**
     * 获取地区分布统计
     * 
     * @param param 查询参数
     * @return 地区分布
     */
    List<Map<String, Object>> getRegionDistribution(CounterpartInfoQueryParam param);

    /**
     * 模糊搜索相对方信息
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 相对方信息列表
     */
    List<CounterpartInfo> searchCounterparts(String keyword, Integer limit);

    /**
     * 导入相对方信息
     * 
     * @param counterpartList 相对方信息列表
     * @return 导入结果
     */
    Map<String, Object> importCounterpartInfo(List<CounterpartInfo> counterpartList);

    /**
     * 导出相对方信息
     * 
     * @param param 查询参数
     * @return 导出数据
     */
    List<CounterpartInfo> exportCounterpartInfo(CounterpartInfoQueryParam param);
}
