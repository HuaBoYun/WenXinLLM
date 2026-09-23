package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.TcDataMapping;

import java.util.List;
import java.util.Map;

/**
 * 数据映射配置Service接口
 *
 * @author 华博云开发团队
 * @since 2024-12-23
 */
public interface TcDataMappingService extends IService<TcDataMapping> {

    /**
     * 分页查询数据映射配置列表
     *
     * @param page 页码
     * @param limit 每页数量
     * @param mappingName 映射名称
     * @param sourceSystem 源系统
     * @param targetSystem 目标系统
     * @param mappingType 映射类型
     * @param status 状态
     * @return 分页结果
     */
    IPage<TcDataMapping> getDataMappingPage(Integer page, Integer limit, String mappingName,
                                            String sourceSystem, String targetSystem,
                                            String mappingType, Integer status);

    /**
     * 根据系统ID获取数据映射配置列表
     *
     * @param systemId 系统ID
     * @return 数据映射配置列表
     */
    List<TcDataMapping> getBySystemId(String systemId);

    /**
     * 刷新数据映射配置
     * 从源系统获取最新字段信息，更新映射配置中的字段列表，标记失效的映射规则
     *
     * @return 刷新结果，包含更新数量、失效数量等信息
     */
    Map<String, Object> refreshMapping();

    /**
     * 检查映射编码是否存在
     *
     * @param mappingCode 映射编码
     * @return true-存在，false-不存在
     */
    boolean existsByMappingCode(String mappingCode);
}
