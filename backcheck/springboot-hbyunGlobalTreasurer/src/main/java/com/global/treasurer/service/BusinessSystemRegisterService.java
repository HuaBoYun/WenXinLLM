package com.global.treasurer.service;

import com.global.treasurer.entity.BusinessSystemRegister;
import java.util.List;
import java.util.Map;

/**
 * 业务系统注册服务接口
 *
 * @author AI Developer
 * @date 2025-02-26
 */
public interface BusinessSystemRegisterService {

    /**
     * 分页查询业务系统列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    Map<String, Object> getSystemPage(Map<String, Object> param);

    /**
     * 根据ID查询业务系统
     *
     * @param id 系统ID
     * @return 业务系统信息
     */
    BusinessSystemRegister getSystemById(Long id);

    /**
     * 创建业务系统
     *
     * @param system 业务系统信息
     * @return 影响行数
     */
    int createSystem(BusinessSystemRegister system);

    /**
     * 更新业务系统
     *
     * @param system 业务系统信息
     * @return 影响行数
     */
    int updateSystem(BusinessSystemRegister system);

    /**
     * 批量删除业务系统
     *
     * @param ids 系统ID列表
     * @return 影响行数
     */
    int batchDelete(List<Long> ids);

    /**
     * 测试连接
     *
     * @param id 系统ID
     * @return 测试结果
     */
    Map<String, Object> testConnection(Long id);

    /**
     * 切换状态
     *
     * @param id 系统ID
     * @param status 状态
     * @return 影响行数
     */
    int toggleStatus(Long id, Integer status);

    /**
     * 同步状态
     *
     * @return 同步结果
     */
    Map<String, Object> syncStatus();
}
