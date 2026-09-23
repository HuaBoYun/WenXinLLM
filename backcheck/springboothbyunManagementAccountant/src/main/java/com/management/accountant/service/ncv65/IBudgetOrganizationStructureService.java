package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetOrganizationStructure;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算组织体系服务接口
 * 
 * @description 预算组织体系业务逻辑接口，支持5种组织体系类型
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
public interface IBudgetOrganizationStructureService extends IService<BudgetOrganizationStructure> {

    /**
     * 创建组织体系
     * @param structure 组织体系信息
     * @return 创建结果
     */
    boolean createStructure(BudgetOrganizationStructure structure);

    /**
     * 更新组织体系
     * @param structure 组织体系信息
     * @return 更新结果
     */
    boolean updateStructure(BudgetOrganizationStructure structure);

    /**
     * 根据ID删除组织体系
     * @param id 体系ID
     * @return 删除结果
     */
    boolean deleteStructure(String id);

    /**
     * 批量删除组织体系
     * @param ids ID列表
     * @return 删除结果
     */
    boolean batchDeleteStructures(List<String> ids);

    /**
     * 根据ID查询组织体系详情
     * @param id 体系ID
     * @return 组织体系信息
     */
    BudgetOrganizationStructure getStructureById(String id);

    /**
     * 根据体系编码查询组织体系
     * @param structureCode 体系编码
     * @return 组织体系信息
     */
    BudgetOrganizationStructure getStructureByCode(String structureCode);

    /**
     * 分页查询组织体系列表
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetOrganizationStructure> getStructurePage(Long current, Long size, Map<String, Object> params);

    /**
     * 根据体系类型查询组织体系列表
     * @param structureType 体系类型
     * @return 组织体系列表
     */
    List<BudgetOrganizationStructure> getStructuresByType(String structureType);

    /**
     * 查询启用的组织体系列表
     * @return 启用的组织体系列表
     */
    List<BudgetOrganizationStructure> getEnabledStructures();

    /**
     * 启用组织体系
     * @param id 体系ID
     * @return 操作结果
     */
    boolean enableStructure(String id);

    /**
     * 禁用组织体系
     * @param id 体系ID
     * @return 操作结果
     */
    boolean disableStructure(String id);

    /**
     * 批量更新状态
     * @param ids ID列表
     * @param status 状态
     * @return 操作结果
     */
    boolean batchUpdateStatus(List<String> ids, String status);

    /**
     * 批量启用/禁用
     * @param ids ID列表
     * @param isEnabled 是否启用
     * @return 操作结果
     */
    boolean batchUpdateEnabled(List<String> ids, Boolean isEnabled);

    /**
     * 复制组织体系
     * @param sourceId 源体系ID
     * @param targetName 目标体系名称
     * @param targetCode 目标体系编码
     * @return 复制结果
     */
    BudgetOrganizationStructure copyStructure(String sourceId, String targetName, String targetCode);

    /**
     * 检查体系编码是否存在
     * @param structureCode 体系编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkStructureCodeExists(String structureCode, String excludeId);

    /**
     * 获取组织体系树结构
     * @param structureId 体系ID
     * @return 树结构数据
     */
    Map<String, Object> getStructureTree(String structureId);

    /**
     * 验证组织体系配置
     * @param structure 组织体系信息
     * @return 验证结果
     */
    Map<String, Object> validateStructureConfig(BudgetOrganizationStructure structure);

    /**
     * 发布组织体系
     * @param id 体系ID
     * @return 发布结果
     */
    boolean publishStructure(String id);

    /**
     * 撤销发布组织体系
     * @param id 体系ID
     * @return 撤销结果
     */
    boolean unpublishStructure(String id);

    /**
     * 统计各体系类型的数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByStructureType();

    /**
     * 统计各状态的数量
     * @return 统计结果
     */
    List<Map<String, Object>> countByStatus();

    /**
     * 获取组织体系使用情况统计
     * @param structureId 体系ID
     * @return 使用情况统计
     */
    Map<String, Object> getStructureUsageStatistics(String structureId);

    /**
     * 查询即将过期的组织体系
     * @param days 天数
     * @return 即将过期的组织体系列表
     */
    List<BudgetOrganizationStructure> getExpiringStructures(Integer days);

    /**
     * 查询已过期的组织体系
     * @return 已过期的组织体系列表
     */
    List<BudgetOrganizationStructure> getExpiredStructures();

    /**
     * 获取体系层级统计
     * @return 层级统计
     */
    List<Map<String, Object>> getLevelStatistics();

    /**
     * 根据创建时间范围查询
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 组织体系列表
     */
    List<BudgetOrganizationStructure> getStructuresByCreateTimeRange(String startTime, String endTime);

    /**
     * 获取体系趋势数据
     * @param months 月份数
     * @return 趋势数据
     */
    List<Map<String, Object>> getStructureTrendData(Integer months);

    /**
     * 导出组织体系数据
     * @param params 导出参数
     * @return 导出数据
     */
    List<Map<String, Object>> exportStructureData(Map<String, Object> params);

    /**
     * 导入组织体系数据
     * @param dataList 导入数据
     * @return 导入结果
     */
    Map<String, Object> importStructureData(List<Map<String, Object>> dataList);

    /**
     * 获取组织体系配置模板
     * @param structureType 体系类型
     * @return 配置模板
     */
    Map<String, Object> getStructureConfigTemplate(String structureType);

    /**
     * 应用组织体系配置模板
     * @param structureId 体系ID
     * @param templateConfig 模板配置
     * @return 应用结果
     */
    boolean applyStructureConfigTemplate(String structureId, Map<String, Object> templateConfig);
}
