package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.EnterpriseHierarchy;

import java.util.List;
import java.util.Map;

/**
 * 企业层级关系表 服务类
 *
 * @author system
 * @since 2024-01-01
 */
public interface IEnterpriseHierarchyService extends IService<EnterpriseHierarchy> {

    /**
     * 获取企业层级关系列表
     *
     * @param enterpriseId 企业ID
     * @return 层级关系列表
     */
    List<EnterpriseHierarchy> getHierarchyByEnterpriseId(String enterpriseId);

    /**
     * 新增企业层级关系
     *
     * @param hierarchy 层级关系信息
     * @return 是否成功
     */
    boolean addHierarchy(EnterpriseHierarchy hierarchy);

    /**
     * 更新企业层级关系
     *
     * @param hierarchy 层级关系信息
     * @return 是否成功
     */
    boolean updateHierarchy(EnterpriseHierarchy hierarchy);

    /**
     * 删除企业层级关系
     *
     * @param hierarchyId 层级关系ID
     * @return 是否成功
     */
    boolean deleteHierarchy(String hierarchyId);

    /**
     * 批量删除企业层级关系
     *
     * @param hierarchyIds 层级关系ID列表
     * @return 是否成功
     */
    boolean batchDeleteHierarchy(List<String> hierarchyIds);

    /**
     * 获取企业的所有上级企业（向上穿透）
     *
     * @param enterpriseId 企业ID
     * @param maxLevel 最大层级（防止无限递归）
     * @return 上级企业列表
     */
    List<Map<String, Object>> getParentHierarchy(String enterpriseId, Integer maxLevel);

    /**
     * 获取企业的所有下级企业（向下穿透）
     *
     * @param enterpriseId 企业ID
     * @param maxLevel 最大层级（防止无限递归）
     * @return 下级企业列表
     */
    List<Map<String, Object>> getChildHierarchy(String enterpriseId, Integer maxLevel);

    /**
     * 获取企业层级关系图数据
     *
     * @param enterpriseId 企业ID
     * @return 层级关系图数据
     */
    Map<String, Object> getHierarchyChartData(String enterpriseId);

    /**
     * 检查是否存在循环引用
     *
     * @param parentEnterpriseId 母公司ID
     * @param childEnterpriseId 子公司ID
     * @return 是否存在循环引用
     */
    boolean checkCircularReference(String parentEnterpriseId, String childEnterpriseId);

    /**
     * 获取企业的直接子公司数量
     *
     * @param enterpriseId 企业ID
     * @return 子公司数量
     */
    Integer getDirectChildrenCount(String enterpriseId);

    /**
     * 获取企业的所有子公司数量（包括间接子公司）
     *
     * @param enterpriseId 企业ID
     * @return 所有子公司数量
     */
    Integer getAllChildrenCount(String enterpriseId);

    /**
     * 批量导入企业层级关系
     *
     * @param hierarchyList 层级关系列表
     * @return 导入结果
     */
    Map<String, Object> batchImportHierarchy(List<EnterpriseHierarchy> hierarchyList);

    /**
     * 根据关系类型获取层级关系
     *
     * @param relationshipType 关系类型
     * @return 层级关系列表
     */
    List<EnterpriseHierarchy> getHierarchyByRelationshipType(String relationshipType);

    /**
     * 获取企业的完整层级路径
     *
     * @param enterpriseId 企业ID
     * @return 层级路径
     */
    List<Map<String, Object>> getHierarchyPath(String enterpriseId);

    /**
     * 获取企业层级统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getHierarchyStatistics();

    /**
     * 验证层级关系的有效性
     *
     * @param hierarchy 层级关系
     * @return 验证结果
     */
    Map<String, Object> validateHierarchy(EnterpriseHierarchy hierarchy);

    /**
     * 获取企业的层级级别
     *
     * @param enterpriseId 企业ID
     * @return 层级级别
     */
    Integer getEnterpriseLevel(String enterpriseId);

    /**
     * 根据层级级别获取企业列表
     *
     * @param hierarchyLevel 层级级别
     * @return 企业列表
     */
    List<Map<String, Object>> getEnterprisesByLevel(Integer hierarchyLevel);
}
