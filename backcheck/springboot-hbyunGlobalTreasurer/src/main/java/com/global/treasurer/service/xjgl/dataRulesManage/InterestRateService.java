package com.global.treasurer.service.xjgl.dataRulesManage;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TblGtInterestRate;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 利率管理Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
public interface InterestRateService extends IService<TblGtInterestRate> {

    /**
     * 获取利率列表(分页)
     *
     * @param params 查询参数
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 分页结果
     */
    PageInfo<TblGtInterestRate> getInterestRateList(Map<String, Object> params, int pageNum, int pageSize);

    /**
     * 新增利率
     *
     * @param interestRate 利率实体
     * @return 影响行数
     */
    int createInterestRate(TblGtInterestRate interestRate);

    /**
     * 更新利率
     *
     * @param interestRate 利率实体
     * @return 影响行数
     */
    int updateInterestRate(TblGtInterestRate interestRate);

    /**
     * 删除利率
     *
     * @param rateId 利率ID
     * @return 影响行数
     */
    int deleteInterestRate(Long rateId);

    /**
     * 获取利率统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 同步利率
     *
     * @param sourceType 数据源类型
     * @return 同步数量
     */
    int syncInterestRate(String sourceType);

    /**
     * 批量删除利率
     *
     * @param ids 利率ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 更新利率状态
     *
     * @param id 利率ID
     * @param status 状态
     * @return 影响行数
     */
    int updateStatus(Long id, Integer status);
}