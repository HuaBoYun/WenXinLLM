package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.EnterpriseHierarchy;
import com.huabo.cybermonitor.mapper.EnterpriseHierarchyMapper;
import com.huabo.cybermonitor.service.IEnterpriseHierarchyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 企业层级关系表 服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EnterpriseHierarchyServiceImpl extends ServiceImpl<EnterpriseHierarchyMapper, EnterpriseHierarchy> implements IEnterpriseHierarchyService {

    @Autowired
    private EnterpriseHierarchyMapper hierarchyMapper;

    @Override
    public List<EnterpriseHierarchy> getHierarchyByEnterpriseId(String enterpriseId) {
        return hierarchyMapper.selectHierarchyByEnterpriseId(enterpriseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addHierarchy(EnterpriseHierarchy hierarchy) {
        try {
            // 验证层级关系的有效性
            Map<String, Object> validateResult = validateHierarchy(hierarchy);
            if (!(Boolean) validateResult.get("valid")) {
                throw new RuntimeException(validateResult.get("message").toString());
            }
            
            // 检查是否存在循环引用
            if (checkCircularReference(hierarchy.getParentEnterpriseId(), hierarchy.getChildEnterpriseId())) {
                throw new RuntimeException("存在循环引用，无法建立层级关系");
            }
            
            // 计算层级级别
            Integer parentLevel = getEnterpriseLevel(hierarchy.getParentEnterpriseId());
            hierarchy.setHierarchyLevel(parentLevel + 1);
            
            // 设置创建时间
            hierarchy.setCreateTime(LocalDateTime.now());
            
            return save(hierarchy);
        } catch (Exception e) {
            log.error("新增企业层级关系失败", e);
            throw new RuntimeException("新增企业层级关系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateHierarchy(EnterpriseHierarchy hierarchy) {
        try {
            // 验证层级关系的有效性
            Map<String, Object> validateResult = validateHierarchy(hierarchy);
            if (!(Boolean) validateResult.get("valid")) {
                throw new RuntimeException(validateResult.get("message").toString());
            }
            
            // 检查是否存在循环引用（排除当前记录）
            EnterpriseHierarchy existingHierarchy = getById(hierarchy.getHierarchyId());
            if (existingHierarchy != null && 
                (!existingHierarchy.getParentEnterpriseId().equals(hierarchy.getParentEnterpriseId()) ||
                 !existingHierarchy.getChildEnterpriseId().equals(hierarchy.getChildEnterpriseId()))) {
                if (checkCircularReference(hierarchy.getParentEnterpriseId(), hierarchy.getChildEnterpriseId())) {
                    throw new RuntimeException("存在循环引用，无法更新层级关系");
                }
            }
            
            // 重新计算层级级别
            Integer parentLevel = getEnterpriseLevel(hierarchy.getParentEnterpriseId());
            hierarchy.setHierarchyLevel(parentLevel + 1);
            
            // 设置更新时间
            hierarchy.setUpdateTime(LocalDateTime.now());
            
            return updateById(hierarchy);
        } catch (Exception e) {
            log.error("更新企业层级关系失败", e);
            throw new RuntimeException("更新企业层级关系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteHierarchy(String hierarchyId) {
        try {
            return removeById(hierarchyId);
        } catch (Exception e) {
            log.error("删除企业层级关系失败", e);
            throw new RuntimeException("删除企业层级关系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteHierarchy(List<String> hierarchyIds) {
        try {
            return removeByIds(hierarchyIds);
        } catch (Exception e) {
            log.error("批量删除企业层级关系失败", e);
            throw new RuntimeException("批量删除企业层级关系失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getParentHierarchy(String enterpriseId, Integer maxLevel) {
        if (maxLevel == null || maxLevel <= 0) {
            maxLevel = 10; // 默认最大10层
        }
        return hierarchyMapper.selectParentHierarchy(enterpriseId, maxLevel);
    }

    @Override
    public List<Map<String, Object>> getChildHierarchy(String enterpriseId, Integer maxLevel) {
        if (maxLevel == null || maxLevel <= 0) {
            maxLevel = 10; // 默认最大10层
        }
        return hierarchyMapper.selectChildHierarchy(enterpriseId, maxLevel);
    }

    @Override
    public Map<String, Object> getHierarchyChartData(String enterpriseId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取层级关系数据
            List<Map<String, Object>> hierarchyData = hierarchyMapper.selectHierarchyChartData(enterpriseId);
            
            // 构建节点和边的数据结构
            Set<String> nodeIds = new HashSet<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> edges = new ArrayList<>();
            
            for (Map<String, Object> item : hierarchyData) {
                String parentId = (String) item.get("PARENT_ENTERPRISE_ID");
                String childId = (String) item.get("CHILD_ENTERPRISE_ID");
                String parentName = (String) item.get("parentEnterpriseName");
                String childName = (String) item.get("childEnterpriseName");
                String relationshipType = (String) item.get("RELATIONSHIP_TYPE");
                
                // 添加父节点
                if (!nodeIds.contains(parentId)) {
                    Map<String, Object> parentNode = new HashMap<>();
                    parentNode.put("id", parentId);
                    parentNode.put("label", parentName);
                    parentNode.put("type", "enterprise");
                    nodes.add(parentNode);
                    nodeIds.add(parentId);
                }
                
                // 添加子节点
                if (!nodeIds.contains(childId)) {
                    Map<String, Object> childNode = new HashMap<>();
                    childNode.put("id", childId);
                    childNode.put("label", childName);
                    childNode.put("type", "enterprise");
                    nodes.add(childNode);
                    nodeIds.add(childId);
                }
                
                // 添加边
                Map<String, Object> edge = new HashMap<>();
                edge.put("source", parentId);
                edge.put("target", childId);
                edge.put("label", getRelationshipTypeLabel(relationshipType));
                edges.add(edge);
            }
            
            result.put("nodes", nodes);
            result.put("edges", edges);
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("获取企业层级关系图数据失败", e);
            result.put("success", false);
            result.put("message", "获取层级关系图数据失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean checkCircularReference(String parentEnterpriseId, String childEnterpriseId) {
        try {
            Integer count = hierarchyMapper.checkCircularReference(parentEnterpriseId, childEnterpriseId);
            return count != null && count > 0;
        } catch (Exception e) {
            log.error("检查循环引用失败", e);
            return false;
        }
    }

    @Override
    public Integer getDirectChildrenCount(String enterpriseId) {
        return hierarchyMapper.countDirectChildren(enterpriseId);
    }

    @Override
    public Integer getAllChildrenCount(String enterpriseId) {
        return hierarchyMapper.countAllChildren(enterpriseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchImportHierarchy(List<EnterpriseHierarchy> hierarchyList) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();
            
            for (int i = 0; i < hierarchyList.size(); i++) {
                try {
                    EnterpriseHierarchy hierarchy = hierarchyList.get(i);
                    addHierarchy(hierarchy);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("第" + (i + 1) + "条记录：" + e.getMessage());
                }
            }
            
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);
            result.put("totalCount", hierarchyList.size());
            
        } catch (Exception e) {
            log.error("批量导入企业层级关系失败", e);
            throw new RuntimeException("批量导入企业层级关系失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<EnterpriseHierarchy> getHierarchyByRelationshipType(String relationshipType) {
        QueryWrapper<EnterpriseHierarchy> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("RELATIONSHIP_TYPE", relationshipType);
        queryWrapper.isNull("EXPIRY_DATE").or().gt("EXPIRY_DATE", new Date());
        return list(queryWrapper);
    }

    @Override
    public List<Map<String, Object>> getHierarchyPath(String enterpriseId) {
        // 获取从根节点到当前企业的完整路径
        List<Map<String, Object>> parentHierarchy = getParentHierarchy(enterpriseId, 10);
        
        // 反转列表，使其从根节点开始
        Collections.reverse(parentHierarchy);
        
        return parentHierarchy;
    }

    @Override
    public Map<String, Object> getHierarchyStatistics() {
        Map<String, Object> statistics = new HashMap<>();
        
        try {
            // 统计总的层级关系数量
            QueryWrapper<EnterpriseHierarchy> totalWrapper = new QueryWrapper<>();
            totalWrapper.isNull("EXPIRY_DATE").or().gt("EXPIRY_DATE", new Date());
            long totalCount = count(totalWrapper);
            
            // 按关系类型统计
            Map<String, Long> typeStatistics = new HashMap<>();
            for (String type : Arrays.asList("SUBSIDIARY", "BRANCH", "HOLDING", "PARTICIPATING")) {
                QueryWrapper<EnterpriseHierarchy> typeWrapper = new QueryWrapper<>();
                typeWrapper.eq("RELATIONSHIP_TYPE", type);
                typeWrapper.isNull("EXPIRY_DATE").or().gt("EXPIRY_DATE", new Date());
                typeStatistics.put(type, count(typeWrapper));
            }
            
            statistics.put("totalCount", totalCount);
            statistics.put("typeStatistics", typeStatistics);
            
        } catch (Exception e) {
            log.error("获取企业层级统计信息失败", e);
            statistics.put("error", "获取统计信息失败：" + e.getMessage());
        }
        
        return statistics;
    }

    @Override
    public Map<String, Object> validateHierarchy(EnterpriseHierarchy hierarchy) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 检查父企业和子企业是否相同
            if (hierarchy.getParentEnterpriseId().equals(hierarchy.getChildEnterpriseId())) {
                result.put("valid", false);
                result.put("message", "父企业和子企业不能相同");
                return result;
            }
            
            // 检查是否已存在相同的层级关系
            QueryWrapper<EnterpriseHierarchy> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("PARENT_ENTERPRISE_ID", hierarchy.getParentEnterpriseId());
            queryWrapper.eq("CHILD_ENTERPRISE_ID", hierarchy.getChildEnterpriseId());
            queryWrapper.isNull("EXPIRY_DATE").or().gt("EXPIRY_DATE", new Date());
            
            if (hierarchy.getHierarchyId() != null) {
                queryWrapper.ne("HIERARCHY_ID", hierarchy.getHierarchyId());
            }
            
            long existingCount = count(queryWrapper);
            if (existingCount > 0) {
                result.put("valid", false);
                result.put("message", "该层级关系已存在");
                return result;
            }
            
            result.put("valid", true);
            result.put("message", "验证通过");
            
        } catch (Exception e) {
            log.error("验证企业层级关系失败", e);
            result.put("valid", false);
            result.put("message", "验证失败：" + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Integer getEnterpriseLevel(String enterpriseId) {
        // 获取企业的层级级别（从根节点开始计算）
        List<Map<String, Object>> parentHierarchy = getParentHierarchy(enterpriseId, 10);
        return parentHierarchy.size();
    }

    @Override
    public List<Map<String, Object>> getEnterprisesByLevel(Integer hierarchyLevel) {
        // 这里需要根据具体的业务逻辑实现
        // 可以通过递归查询或者预计算的方式获取指定层级的企业
        return new ArrayList<>();
    }

    // 私有方法

    private String getRelationshipTypeLabel(String type) {
        switch (type) {
            case "SUBSIDIARY": return "子公司";
            case "BRANCH": return "分公司";
            case "HOLDING": return "控股公司";
            case "PARTICIPATING": return "参股公司";
            default: return type;
        }
    }
}
