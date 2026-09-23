package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.oracle.entity.OrganizationEntity;
import com.financial.sharing.oracle.mapper.OrganizationMapper;
import com.financial.sharing.service.OrganizationService;
import com.financial.sharing.vo.param.OrganizationSaveParam;
import com.financial.sharing.vo.result.OrganizationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 组织架构配置Service实现类
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationMapper organizationMapper;

    @Override
    public List<OrganizationVO> getOrganizationTree(Long tenantId) {
        List<OrganizationEntity> allOrgs = organizationMapper.getOrganizationTree(tenantId);
        if (allOrgs == null || allOrgs.isEmpty()) {
            return new ArrayList<>();
        }

        // 转换为VO并设置统计信息
        List<OrganizationVO> allVOs = allOrgs.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        // 构建树形结构
        return buildTree(allVOs);
    }

    @Override
    public OrganizationVO getOrganizationById(Long id) {
        OrganizationEntity entity = organizationMapper.selectById(id);
        return entity != null ? convertToVO(entity) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrganization(OrganizationSaveParam param) {
        // 检查组织编码是否存在
        int exists = organizationMapper.checkOrgCodeExists(param.getOrgCode(), param.getTenantId(), null);
        if (exists > 0) {
            throw new RuntimeException("组织编码已存在：" + param.getOrgCode());
        }

        // 验证父级组织
        if (param.getParentId() != null) {
            OrganizationEntity parent = organizationMapper.selectById(param.getParentId());
            if (parent == null) {
                throw new RuntimeException("上级组织不存在");
            }
            // 设置组织层级
            param.setOrgLevel(parent.getOrgLevel() + 1);
        } else {
            param.setOrgLevel(1);
        }

        OrganizationEntity entity = new OrganizationEntity();
        BeanUtils.copyProperties(param, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsDeleted(0);

        return organizationMapper.insert(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrganization(OrganizationSaveParam param) {
        if (param.getId() == null) {
            throw new RuntimeException("组织ID不能为空");
        }

        // 检查组织编码是否存在
        int exists = organizationMapper.checkOrgCodeExists(param.getOrgCode(), param.getTenantId(), param.getId());
        if (exists > 0) {
            throw new RuntimeException("组织编码已存在：" + param.getOrgCode());
        }

        // 验证父级组织
        if (param.getParentId() != null) {
            OrganizationEntity parent = organizationMapper.selectById(param.getParentId());
            if (parent == null) {
                throw new RuntimeException("上级组织不存在");
            }
            // 防止循环引用
            if (param.getId().equals(param.getParentId())) {
                throw new RuntimeException("不能将组织设置为自己的子级");
            }
            param.setOrgLevel(parent.getOrgLevel() + 1);
        } else {
            param.setOrgLevel(1);
        }

        OrganizationEntity entity = new OrganizationEntity();
        BeanUtils.copyProperties(param, entity);
        entity.setUpdateTime(LocalDateTime.now());

        return organizationMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrganization(Long id) {
        OrganizationEntity entity = organizationMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("组织不存在");
        }

        // 检查是否有子组织
        int childCount = organizationMapper.countChildOrganizations(id, entity.getTenantId());
        if (childCount > 0) {
            throw new RuntimeException("存在子组织，无法删除");
        }

        // 逻辑删除
        entity.setIsDeleted(1);
        entity.setUpdateTime(LocalDateTime.now());
        return organizationMapper.updateById(entity) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrganizationStatus(Long id, Integer isEnabled) {
        OrganizationEntity entity = organizationMapper.selectById(id);
        if (entity == null) {
            throw new RuntimeException("组织不存在");
        }
        entity.setIsEnabled(isEnabled);
        entity.setUpdateTime(LocalDateTime.now());
        return organizationMapper.updateById(entity) > 0;
    }

    @Override
    public List<OrganizationVO> getChildOrganizations(Long parentId, Long tenantId) {
        List<OrganizationEntity> entities = organizationMapper.getChildOrganizations(parentId, tenantId);
        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public String syncOrganizations() {
        // TODO: 实现从外部系统同步组织数据的功能
        return "同步功能开发中...";
    }

    @Override
    public List<OrganizationVO> getByOrgType(Integer orgType, Long tenantId) {
        List<OrganizationEntity> entities = organizationMapper.getByOrgType(orgType, tenantId);
        return entities.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveOrganization(Long orgId, Long newParentId, Long tenantId) {
        OrganizationEntity entity = organizationMapper.selectById(orgId);
        if (entity == null) {
            throw new RuntimeException("组织不存在");
        }

        // 验证新父级组织
        if (newParentId != null) {
            OrganizationEntity newParent = organizationMapper.selectById(newParentId);
            if (newParent == null) {
                throw new RuntimeException("目标上级组织不存在");
            }
            // 防止循环引用
            if (orgId.equals(newParentId) || isChildOf(newParentId, orgId, tenantId)) {
                throw new RuntimeException("不能移动到自己的子级下");
            }
            entity.setParentId(newParentId);
            entity.setOrgLevel(newParent.getOrgLevel() + 1);
        } else {
            entity.setParentId(null);
            entity.setOrgLevel(1);
        }
        entity.setUpdateTime(LocalDateTime.now());
        return organizationMapper.updateById(entity) > 0;
    }

    /**
     * 实体转VO
     * @param entity 实体对象
     * @return VO对象
     */
    private OrganizationVO convertToVO(OrganizationEntity entity) {
        OrganizationVO vo = new OrganizationVO();
        BeanUtils.copyProperties(entity, vo);

        // 设置枚举值名称
        vo.setIsEnabledName(entity.getIsEnabled() == 1 ? "启用" : "禁用");
        vo.setOrgTypeName(getOrgTypeName(entity.getOrgType()));

        // 设置上级组织名称
        if (entity.getParentId() != null) {
            OrganizationEntity parent = organizationMapper.selectById(entity.getParentId());
            if (parent != null) {
                vo.setParentOrgName(parent.getOrgName());
            }
        }

        // 设置统计信息 - 分别调用三个查询避免数组越界
        try {
            int childCount = organizationMapper.getChildOrganizationsCount(entity.getId());
            int deptCount = organizationMapper.getChildDepartmentsCount(entity.getId());
            int userCount = 0; // 暂时设为0，后续可以从用户表查询

            vo.setChildCount(childCount);
            vo.setDeptCount(deptCount);
            vo.setUserCount(userCount);
        } catch (Exception e) {
            log.warn("获取组织统计信息失败，组织ID: {}", entity.getId(), e);
            // 设置默认值避免空指针异常
            vo.setChildCount(0);
            vo.setDeptCount(0);
            vo.setUserCount(0);
        }
        return vo;
    }

    /**
     * 构建树形结构
     * @param allOrgs 所有组织列表
     * @return 树形结构
     */
    private List<OrganizationVO> buildTree(List<OrganizationVO> allOrgs) {
        List<OrganizationVO> rootOrgs = new ArrayList<>();
        Map<Long, OrganizationVO> orgMap = allOrgs.stream()
                .collect(Collectors.toMap(OrganizationVO::getId, org -> org));

        for (OrganizationVO org : allOrgs) {
            if (org.getParentId() == null || org.getParentId() == 0) {
                rootOrgs.add(org);
            } else {
                OrganizationVO parent = orgMap.get(org.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(org);
                }
            }
        }

        // 排序
        sortOrganizations(rootOrgs);
        return rootOrgs;
    }

    /**
     * 递归排序组织
     * @param orgs 组织列表
     */
    private void sortOrganizations(List<OrganizationVO> orgs) {
        if (orgs != null && !orgs.isEmpty()) {
            orgs.sort((a, b) -> {
                if (a.getSortOrder() != null && b.getSortOrder() != null) {
                    return a.getSortOrder().compareTo(b.getSortOrder());
                }
                return a.getId().compareTo(b.getId());
            });
            orgs.forEach(org -> sortOrganizations(org.getChildren()));
        }
    }

    /**
     * 检查是否为子组织
     * @param childId 子组织ID
     * @param parentId 父组织ID
     * @param tenantId 租户ID
     * @return 是否为子组织
     */
    private boolean isChildOf(Long childId, Long parentId, Long tenantId) {
        List<OrganizationVO> children = getChildOrganizations(childId, tenantId);
        for (OrganizationVO child : children) {
            if (child.getId().equals(parentId)) {
                return true;
            }
            if (isChildOf(child.getId(), parentId, tenantId)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取组织类型名称
     * @param orgType 组织类型(1公司 2部门 3科室 4小组)
     * @return 类型名称
     */
    private String getOrgTypeName(Integer orgType) {
        if (orgType == null) return "";
        switch (orgType) {
            case 1:
                return "公司";
            case 2:
                return "部门";
            case 3:
                return "科室";
            case 4:
                return "小组";
            default:
                return String.valueOf(orgType);
        }
    }
}