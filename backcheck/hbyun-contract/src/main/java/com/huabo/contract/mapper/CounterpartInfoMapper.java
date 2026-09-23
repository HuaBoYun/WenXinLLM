package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.CounterpartInfo;
import com.huabo.contract.vo.CounterpartInfoQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 相对方信息表 Mapper 接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface CounterpartInfoMapper extends BaseMapper<CounterpartInfo> {

    /**
     * 分页查询相对方信息列表
     * 
     * @param param 查询参数
     * @return 相对方信息列表
     */
    List<CounterpartInfo> selectCounterpartInfoList(@Param("param") CounterpartInfoQueryParam param);

    /**
     * 根据公司名称查询相对方信息
     * 
     * @param companyName 公司名称
     * @return 相对方信息
     */
    CounterpartInfo selectByCompanyName(@Param("companyName") String companyName);

    /**
     * 根据公司代码查询相对方信息
     * 
     * @param companyCode 公司代码
     * @return 相对方信息
     */
    CounterpartInfo selectByCompanyCode(@Param("companyCode") String companyCode);

    /**
     * 检查公司名称是否存在
     * 
     * @param companyName 公司名称
     * @param excludeId 排除的ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsCompanyName(@Param("companyName") String companyName, @Param("excludeId") Long excludeId);

    /**
     * 检查公司代码是否存在
     * 
     * @param companyCode 公司代码
     * @param excludeId 排除的ID
     * @return 存在返回true，不存在返回false
     */
    boolean existsCompanyCode(@Param("companyCode") String companyCode, @Param("excludeId") Long excludeId);

    /**
     * 获取黑名单相对方列表
     * 
     * @return 黑名单相对方列表
     */
    List<CounterpartInfo> selectBlacklistCounterparts();

    /**
     * 获取高信用等级相对方列表
     * 
     * @return 高信用等级相对方列表
     */
    List<CounterpartInfo> selectHighCreditCounterparts();

    /**
     * 获取低信用等级相对方列表
     * 
     * @return 低信用等级相对方列表
     */
    List<CounterpartInfo> selectLowCreditCounterparts();

    /**
     * 获取大型企业相对方列表
     * 
     * @return 大型企业相对方列表
     */
    List<CounterpartInfo> selectLargeEnterpriseCounterparts();

    /**
     * 批量更新黑名单状态
     * 
     * @param ids 相对方ID列表
     * @param blacklistFlag 黑名单标识
     * @return 更新数量
     */
    int batchUpdateBlacklistFlag(@Param("ids") List<Long> ids, @Param("blacklistFlag") Integer blacklistFlag);

    /**
     * 批量更新信用等级
     * 
     * @param ids 相对方ID列表
     * @param creditRating 信用等级
     * @return 更新数量
     */
    int batchUpdateCreditRating(@Param("ids") List<Long> ids, @Param("creditRating") String creditRating);

    /**
     * 统计相对方信息数据
     * 
     * @param param 查询参数
     * @return 统计数据
     */
    Map<String, Object> selectCounterpartStatistics(@Param("param") CounterpartInfoQueryParam param);

    /**
     * 获取信用等级分布统计
     * 
     * @param param 查询参数
     * @return 信用等级分布
     */
    List<Map<String, Object>> selectCreditRatingDistribution(@Param("param") CounterpartInfoQueryParam param);

    /**
     * 获取注册资本分布统计
     * 
     * @param param 查询参数
     * @return 注册资本分布
     */
    List<Map<String, Object>> selectRegisteredCapitalDistribution(@Param("param") CounterpartInfoQueryParam param);

    /**
     * 获取地区分布统计
     * 
     * @param param 查询参数
     * @return 地区分布
     */
    List<Map<String, Object>> selectRegionDistribution(@Param("param") CounterpartInfoQueryParam param);

    /**
     * 模糊搜索相对方信息
     * 
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 相对方信息列表
     */
    List<CounterpartInfo> searchCounterparts(@Param("keyword") String keyword, @Param("limit") Integer limit);
}
