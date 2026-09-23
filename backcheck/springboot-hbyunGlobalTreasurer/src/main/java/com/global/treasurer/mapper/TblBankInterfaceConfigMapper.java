package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblBankInterfaceConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 银企联配置Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface TblBankInterfaceConfigMapper extends BaseMapper<TblBankInterfaceConfig> {

    /**
     * 根据ID查询配置（显式定义，使用正确的列名）
     *
     * @param configId 配置ID
     * @return 配置对象
     */
    TblBankInterfaceConfig selectByIdWithCorrectColumns(@Param("configId") Long configId);

    /**
     * 更新配置（显式定义，使用正确的列名）
     *
     * @param config 配置对象
     * @return 影响行数
     */
    int updateConfigById(TblBankInterfaceConfig config);

    /**
     * 分页查询银企联配置
     *
     * @param params 查询参数
     * @return 配置列表
     */
    List<TblBankInterfaceConfig> selectBankConfigPage(Map<String, Object> params);

    /**
     * 统计银企联配置数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countBankConfigList(Map<String, Object> params);

    /**
     * 查询可用配置
     *
     * @param params 查询参数
     * @return 配置列表
     */
    List<TblBankInterfaceConfig> selectAvailableConfigs(Map<String, Object> params);

    /**
     * 统计配置概要
     *
     * @param orgId 组织ID
     * @return 统计数据
     */
    Map<String, Object> selectBankConfigSummary(@Param("orgId") Long orgId);

    /**
     * 批量更新启用状态
     *
     * @param configIds 配置ID列表
     * @param isEnabled 是否启用
     * @return 影响行数
     */
    int batchUpdateEnabled(@Param("configIds") List<Long> configIds, @Param("isEnabled") Integer isEnabled);

    /**
     * 更新连接状态
     *
     * @param configId 配置ID
     * @param status 连接状态
     * @param message 连接信息
     * @return 影响行数
     */
    int updateConnectStatus(@Param("configId") Long configId,
                           @Param("status") String status,
                           @Param("message") String message);

    /**
     * 插入银行接口配置（完整字段）
     *
     * @param config 银行接口配置
     * @return 影响行数
     */
    int insertBankConfig(TblBankInterfaceConfig config);
}
