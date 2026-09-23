package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.FinanceOrganizationDTO;
import com.financial.sharing.oracle.mapper.FinanceOrganizationMapper;
import com.financial.sharing.oracle.entity.FinanceOrganizationEntity;
import com.financial.sharing.service.FinanceOrganizationService;
import com.financial.sharing.vo.result.FinanceOrganizationVO;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务组织Service实现类
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class FinanceOrganizationServiceImpl implements FinanceOrganizationService {

    @Autowired
    private FinanceOrganizationMapper financeOrganizationMapper;

    @Autowired
    private UserProvider userProvider;

    @Override
    public List<FinanceOrganizationVO> getOrganizationTree(Long tenantId, Long bookId) {
        log.info("查询组织树，租户ID: {}, 账簿ID: {}", tenantId, bookId);

        List<FinanceOrganizationVO> allOrganizations = financeOrganizationMapper.selectOrganizationTree(tenantId, bookId);

        if (CollectionUtils.isEmpty(allOrganizations)) {
            return new ArrayList<>();
        }

        // 构建树形结构
        return buildOrganizationTree(allOrganizations);
    }

    @Override
    public FinanceOrganizationVO getOrganizationById(Long id) {
        log.info("根据ID查询组织: {}", id);
        return financeOrganizationMapper.selectOrganizationById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateOrganization(FinanceOrganizationDTO dto) {
        try {
            Long currentUserId = getCurrentUserId();

            if (dto.getId() == null) {
                // 新增
                return saveOrganization(dto, currentUserId);
            } else {
                // 更新
                return updateOrganization(dto, currentUserId);
            }
        } catch (Exception e) {
            log.error("保存或更新组织失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteOrganization(Long id) {
        try {
            log.info("删除组织: {}", id);

            // 检查是否有子组织
            Integer childCount = financeOrganizationMapper.selectChildCount(id);
            if (childCount != null && childCount > 0) {
                throw new RuntimeException("存在子组织，无法删除");
            }

            Long currentUserId = getCurrentUserId();
            int result = financeOrganizationMapper.deleteOrganization(id, currentUserId);

            return result > 0;
        } catch (Exception e) {
            log.error("删除组织失败: {}", id, e);
            throw e;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateOrganizationStatus(Long id, Integer isEnabled) {
        try {
            log.info("更新组织状态: {}, 状态: {}", id, isEnabled);

            Long currentUserId = getCurrentUserId();
            int result = financeOrganizationMapper.updateOrganizationStatus(id, isEnabled, currentUserId);

            return result > 0;
        } catch (Exception e) {
            log.error("更新组织状态失败: {}", id, e);
            return false;
        }
    }

    @Override
    public boolean syncOrganizations() {
        try {
            log.info("开始同步组织数据");
            // TODO: 实现同步逻辑
            return true;
        } catch (Exception e) {
            log.error("同步组织数据失败", e);
            return false;
        }
    }

    /**
     * 新增组织
     */
    private boolean saveOrganization(FinanceOrganizationDTO dto, Long currentUserId) {
        // 检查组织编码是否存在
        Integer existsCount = financeOrganizationMapper.checkOrgCodeExists(
                dto.getOrgCode(), dto.getBookId(), dto.getTenantId(), null);
        if (existsCount != null && existsCount > 0) {
            throw new RuntimeException("组织编码已存在");
        }

        FinanceOrganizationEntity entity = new FinanceOrganizationEntity();
        BeanUtils.copyProperties(dto, entity);

        // 设置默认值
        entity.setCreateBy(currentUserId);
        entity.setUpdateBy(currentUserId);
        entity.setVersion(1);
        entity.setIsDeleted(0);

        // 如果没有设置组织级次，根据父组织计算
        if (entity.getOrgLevel() == null) {
            entity.setOrgLevel(calculateOrgLevel(entity.getParentId()));
        }

        // 如果没有设置排序号，自动生成
        if (entity.getSortOrder() == null) {
            Integer maxSortOrder = financeOrganizationMapper.selectMaxSortOrder(
                    entity.getParentId(), entity.getBookId(), entity.getTenantId());
            entity.setSortOrder((maxSortOrder != null ? maxSortOrder : 0) + 1);
        }

        int result = financeOrganizationMapper.insertOrganization(entity);
        return result > 0;
    }

    /**
     * 更新组织
     */
    private boolean updateOrganization(FinanceOrganizationDTO dto, Long currentUserId) {
        // 检查组织编码是否存在（排除自己）
        Integer existsCount = financeOrganizationMapper.checkOrgCodeExists(
                dto.getOrgCode(), dto.getBookId(), dto.getTenantId(), dto.getId());
        if (existsCount != null && existsCount > 0) {
            throw new RuntimeException("组织编码已存在");
        }

        FinanceOrganizationEntity entity = new FinanceOrganizationEntity();
        BeanUtils.copyProperties(dto, entity);

        entity.setUpdateBy(currentUserId);

        // 如果变更了父组织，需要重新计算组织级次
        if (entity.getParentId() != null) {
            entity.setOrgLevel(calculateOrgLevel(entity.getParentId()));
        }

        int result = financeOrganizationMapper.updateOrganization(entity);
        return result > 0;
    }

    /**
     * 构建组织树形结构
     */
    private List<FinanceOrganizationVO> buildOrganizationTree(List<FinanceOrganizationVO> allOrganizations) {
        Map<Long, FinanceOrganizationVO> nodeMap = new HashMap<>();
        List<FinanceOrganizationVO> rootNodes = new ArrayList<>();

        // 构建节点映射
        for (FinanceOrganizationVO node : allOrganizations) {
            nodeMap.put(node.getId(), node);
        }

        // 构建树形结构
        for (FinanceOrganizationVO node : allOrganizations) {
            if (node.getParentId() == null || node.getParentId() == 0) {
                rootNodes.add(node);
            } else {
                FinanceOrganizationVO parent = nodeMap.get(node.getParentId());
                if (parent != null) {
                    if (parent.getChildren() == null) {
                        parent.setChildren(new ArrayList<>());
                    }
                    parent.getChildren().add(node);
                }
            }
        }
        return rootNodes;
    }

    /**
     * 计算组织级次
     */
    private Integer calculateOrgLevel(Long parentId) {
        if (parentId == null || parentId == 0) {
            return 1; // 顶级组织
        }

        FinanceOrganizationVO parent = financeOrganizationMapper.selectOrganizationById(parentId);
        if (parent != null) {
            return parent.getOrgLevel() + 1;
        }
        return 1;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        try {
            TblStaffUtil staff = userProvider.get();
            return staff != null && staff.getStaffid() != null ? staff.getStaffid().longValue() : 1L;
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            return 1L; // 默认用户ID
        }
    }
}