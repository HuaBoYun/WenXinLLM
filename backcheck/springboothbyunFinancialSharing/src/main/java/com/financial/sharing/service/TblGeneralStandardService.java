package com.financial.sharing.service;

import com.financial.sharing.entity.TblGeneralStandard;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 通用标准服务接口
 *
 * @author Financial Sharing System
 * @since 2025-01-30
 */
public interface TblGeneralStandardService {

    /**
     * 分页查询通用标准
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean<PageResult<TblGeneralStandard>> getList(Object param);

    /**
     * 根据ID查询标准详情
     *
     * @param standardId 标准ID
     * @return 标准详情
     */
    MyJsonBean<TblGeneralStandard> getById(String standardId);

    /**
     * 保存或更新通用标准
     *
     * @param param 保存参数
     * @return 操作结果
     */
    MyJsonBean saveOrUpdate(Object param);

    /**
     * 删除通用标准
     *
     * @param standardId 标准ID
     * @return 操作结果
     */
    MyJsonBean delete(String standardId);

    /**
     * 更新标准状态
     *
     * @param standardId 标准ID
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    MyJsonBean updateStatus(String standardId, Integer isEnabled);

    /**
     * 复制标准
     *
     * @param standardId 标准ID
     * @return 操作结果
     */
    MyJsonBean copyStandard(String standardId);

    /**
     * 获取级别配置
     *
     * @param standardId 标准ID
     * @return 级别配置列表
     */
    MyJsonBean<List<Map<String, Object>>> getLevelConfigs(String standardId);

    /**
     * 保存级别配置
     *
     * @param standardId 标准ID
     * @param levels 级别配置数据
     * @return 操作结果
     */
    MyJsonBean saveLevelConfigs(String standardId, Object levels);

    /**
    * 获取适用条件
    *
    * @param standardId 标准ID
    * @return 适用条件列表
    */
    MyJsonBean<List<Map<String, Object>>> getConditions(String standardId);

    /**
     * 保存适用条件
     *
     * @param standardId 标准ID
     * @param conditions 适用条件数据
     * @return 操作结果
     */
    MyJsonBean saveConditions(String standardId, Object conditions);

    /**
     * 标准计算
     *
     * @param standardId 标准ID
     * @param params 计算参数
     * @return 计算结果
     */
    MyJsonBean<Map<String, Object>> calculate(String standardId, Map<String, Object> params);

    /**
     * 获取使用统计
     *
     * @param standardId 标准ID
     * @return 统计数据
     */
    MyJsonBean<Map<String, Object>> getStatistics(String standardId);
}
