package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.Map;

/**
 * 财务配置服务接口
 * 处理财务版本信息、数据源、采集方案等配置管理
 * 
 * @author system
 * @date 2024-12-19
 */
public interface FinanceConfigService {

    // ==================== 财务版本信息管理 ====================

    /**
     * 获取财务版本信息分类
     */
    PageResult<Map<String, Object>> getCwbbxxType();

    /**
     * 获取财务版本信息列表
     */
    PageResult<Map<String, Object>> getCwbbxxList(Map<String, Object> params);

    /**
     * 新增财务版本信息
     */
    Map<String, Object> saveCwbbxx(Map<String, Object> data);

    /**
     * 删除财务版本信息
     */
    boolean deleteCwbbxx(Map<String, Object> params);

    /**
     * 获取财务版本信息详情
     */
    Map<String, Object> getCwbbxxDetail(Map<String, Object> params);

    // ==================== 数据源管理 ====================

    /**
     * 数据源列表
     */
    PageResult<Map<String, Object>> getDataSourceList(Map<String, Object> params);

    /**
     * 数据源详情
     */
    Map<String, Object> getDataSourceDetail(Map<String, Object> params);

    /**
     * 数据源新增
     */
    Map<String, Object> saveDataSource(Map<String, Object> data);

    /**
     * 数据源删除
     */
    boolean deleteDataSource(Map<String, Object> params);

    /**
     * 数据源测试连接
     */
    Map<String, Object> testDataSource(Map<String, Object> data);

    // ==================== 采集方案管理 ====================

    /**
     * 采集方案列表
     */
    PageResult<Map<String, Object>> getCjfaList(Map<String, Object> params);

    /**
     * 采集方案详情
     */
    Map<String, Object> getCjfaDetail(Map<String, Object> params);

    /**
     * 采集方案新增
     */
    Map<String, Object> saveCjfa(Map<String, Object> data);

    /**
     * 采集方案删除
     */
    boolean deleteCjfa(Map<String, Object> params);

    // ==================== 采集SQL管理 ====================

    /**
     * 新增的时候，调详情接口获取sql基本信息
     */
    Map<String, Object> getCjsqlAddDetail(Map<String, Object> params);

    /**
     * 采集sql详情
     */
    Map<String, Object> getCjsqlDetail(Map<String, Object> params);

    /**
     * 采集sql列表
     */
    PageResult<Map<String, Object>> getCjsqlList(Map<String, Object> params);

    /**
     * 采集sql新增
     */
    Map<String, Object> saveCjsql(Map<String, Object> data);

    /**
     * 采集sql删除
     */
    boolean deleteCjsql(Map<String, Object> data);

    /**
     * 采集sql测试连接
     */
    Map<String, Object> testCjsql(Map<String, Object> data);

    /**
     * 采集sql执行
     */
    Map<String, Object> executeCjsql(Map<String, Object> data);

    // ==================== 离线工具下载 ====================

    /**
     * 下载离线工具
     */
    Map<String, Object> downloadOfflineTool();
}