package com.global.treasurer.service;

import com.global.treasurer.entity.DataMappingConfig;
import java.util.List;
import java.util.Map;

/**
 * 数据映射配置服务接口
 *
 * @author AI Developer
 * @date 2025-02-26
 */
public interface DataMappingConfigService {

    /**
     * 分页查询数据映射列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Map<String, Object> getMappingPage(Map<String, Object> param);

    /**
     * 根据ID查询数据映射
     *
     * @param id 映射ID
     * @return 数据映射信息
     */
    DataMappingConfig getMappingById(Long id);

    /**
     * 创建数据映射
     *
     * @param mapping 数据映射信息
     * @return 影响行数
     */
    int createMapping(DataMappingConfig mapping);

    /**
     * 更新数据映射
     *
     * @param mapping 数据映射信息
     * @return 影响行数
     */
    int updateMapping(DataMappingConfig mapping);

    /**
     * 批量删除数据映射
     *
     * @param ids 映射ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 测试数据映射
     *
     * @param param 测试参数
     * @return 测试结果
     */
    Map<String, Object> testMapping(Map<String, Object> param);
}
