package com.financial.sharing.groupControl.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.financial.sharing.groupControl.dto.DimensionMemberQueryParam;
import com.financial.sharing.groupControl.entity.TblDimensionMember;
import com.financial.sharing.groupControl.mapper.DimensionMemberMapper;
import com.financial.sharing.groupControl.service.DimensionMemberService;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 维度成员服务实现类
 * 
 * @author system
 * @since 2026-01-30
 */
@Slf4j
@Service
public class DimensionMemberServiceImpl implements DimensionMemberService {

    @Autowired
    private DimensionMemberMapper dimensionMemberMapper;

    @Override
    public List<TblDimensionMember> getTree(DimensionMemberQueryParam param) {
        try {
            String tenantId = UserUtils.getUser().getOrgid().toString();
            
            // 查询根节点
            List<TblDimensionMember> rootMembers = dimensionMemberMapper.selectRootMembers(
                param.getDimensionId(), tenantId);
            
            // 递归构建树形结构
            for (TblDimensionMember member : rootMembers) {
                buildTree(member, tenantId);
            }
            
            return rootMembers;
        } catch (Exception e) {
            log.error("查询维度成员树失败", e);
            throw new RuntimeException("查询维度成员树失败: " + e.getMessage());
        }
    }

    /**
     * 递归构建树形结构
     */
    private void buildTree(TblDimensionMember parent, String tenantId) {
        List<TblDimensionMember> children = dimensionMemberMapper.selectChildMembers(
            parent.getMemberId(), tenantId);
        
        if (children != null && !children.isEmpty()) {
            parent.setChildren(children);
            for (TblDimensionMember child : children) {
                buildTree(child, tenantId);
            }
        }
    }

    @Override
    public TblDimensionMember getDetail(String memberId) {
        try {
            return dimensionMemberMapper.selectById(memberId);
        } catch (Exception e) {
            log.error("查询维度成员详情失败", e);
            throw new RuntimeException("查询维度成员详情失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean save(TblDimensionMember member) {
        try {
            String userId = UserUtils.getUser().getStaffid().toString();
            String tenantId = UserUtils.getUser().getOrgid().toString();
            Date now = new Date();

            // 检查编码是否重复
            if (checkCodeExists(member.getDimensionId(), member.getMemberCode(), member.getMemberId())) {
                throw new RuntimeException("成员编码已存在");
            }

            if (StringUtils.hasText(member.getMemberId())) {
                // 修改
                member.setUpdateUser(userId);
                member.setUpdateTime(now);
                
                // 更新父节点的isLeaf状态
                updateParentLeafStatus(member.getParentMemberId(), tenantId);
                
                return dimensionMemberMapper.updateById(member) > 0;
            } else {
                // 新增
                member.setTenantId(tenantId);
                member.setCreateUser(userId);
                member.setCreateTime(now);
                member.setUpdateUser(userId);
                member.setUpdateTime(now);
                
                // 设置默认值
                if (member.getMemberType() == null) {
                    member.setMemberType("NORMAL");
                }
                if (member.getStatus() == null) {
                    member.setStatus("ACTIVE");
                }
                if (member.getIsLeaf() == null) {
                    member.setIsLeaf("Y");
                }
                if (member.getSortNo() == null) {
                    member.setSortNo(0);
                }
                
                // 计算层级
                if (StringUtils.hasText(member.getParentMemberId())) {
                    TblDimensionMember parent = dimensionMemberMapper.selectById(member.getParentMemberId());
                    if (parent != null) {
                        member.setMemberLevel(parent.getMemberLevel() + 1);
                        // 更新父节点为非末级
                        parent.setIsLeaf("N");
                        dimensionMemberMapper.updateById(parent);
                    }
                } else {
                    member.setMemberLevel(1);
                }
                
                return dimensionMemberMapper.insert(member) > 0;
            }
        } catch (Exception e) {
            log.error("保存维度成员失败", e);
            throw new RuntimeException("保存维度成员失败: " + e.getMessage());
        }
    }

    /**
     * 更新父节点的isLeaf状态
     */
    private void updateParentLeafStatus(String parentMemberId, String tenantId) {
        if (StringUtils.hasText(parentMemberId)) {
            int childCount = dimensionMemberMapper.selectChildCount(parentMemberId, tenantId);
            TblDimensionMember parent = dimensionMemberMapper.selectById(parentMemberId);
            if (parent != null) {
                parent.setIsLeaf(childCount > 0 ? "N" : "Y");
                dimensionMemberMapper.updateById(parent);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delete(String memberId) {
        try {
            String tenantId = UserUtils.getUser().getOrgid().toString();

            // 查询成员信息
            TblDimensionMember member = dimensionMemberMapper.selectById(memberId);
            if (member == null) {
                throw new RuntimeException("成员不存在");
            }

            // 级联删除子节点
            deleteChildren(memberId, tenantId);

            // 删除当前节点
            int result = dimensionMemberMapper.deleteById(memberId);

            // 更新父节点的isLeaf状态
            updateParentLeafStatus(member.getParentMemberId(), tenantId);

            return result > 0;
        } catch (Exception e) {
            log.error("删除维度成员失败", e);
            throw new RuntimeException("删除维度成员失败: " + e.getMessage());
        }
    }

    /**
     * 递归删除子节点
     */
    private void deleteChildren(String parentMemberId, String tenantId) {
        List<TblDimensionMember> children = dimensionMemberMapper.selectChildMembers(parentMemberId, tenantId);
        for (TblDimensionMember child : children) {
            deleteChildren(child.getMemberId(), tenantId);
            dimensionMemberMapper.deleteById(child.getMemberId());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDelete(List<String> memberIds) {
        try {
            for (String memberId : memberIds) {
                delete(memberId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量删除维度成员失败", e);
            throw new RuntimeException("批量删除维度成员失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean move(String memberId, String targetParentId, Integer sortNo) {
        try {
            String userId = UserUtils.getUser().getStaffid().toString();
            String tenantId = UserUtils.getUser().getOrgid().toString();
            Date now = new Date();

            // 查询成员信息
            TblDimensionMember member = dimensionMemberMapper.selectById(memberId);
            if (member == null) {
                throw new RuntimeException("成员不存在");
            }

            // 保存原父节点ID
            String oldParentId = member.getParentMemberId();

            // 检查是否移动到自己的子节点下
            if (isDescendant(targetParentId, memberId, tenantId)) {
                throw new RuntimeException("不能移动到自己的子节点下");
            }

            // 更新成员信息
            member.setParentMemberId(targetParentId);
            member.setSortNo(sortNo != null ? sortNo : 0);
            member.setUpdateUser(userId);
            member.setUpdateTime(now);

            // 重新计算层级
            if (StringUtils.hasText(targetParentId)) {
                TblDimensionMember targetParent = dimensionMemberMapper.selectById(targetParentId);
                if (targetParent != null) {
                    member.setMemberLevel(targetParent.getMemberLevel() + 1);
                    // 更新目标父节点为非末级
                    targetParent.setIsLeaf("N");
                    dimensionMemberMapper.updateById(targetParent);
                }
            } else {
                member.setMemberLevel(1);
            }

            // 递归更新子节点层级
            updateChildrenLevel(member, tenantId);

            // 更新成员
            dimensionMemberMapper.updateById(member);

            // 更新原父节点的isLeaf状态
            updateParentLeafStatus(oldParentId, tenantId);

            return true;
        } catch (Exception e) {
            log.error("移动维度成员失败", e);
            throw new RuntimeException("移动维度成员失败: " + e.getMessage());
        }
    }

    /**
     * 检查是否是后代节点
     */
    private boolean isDescendant(String ancestorId, String descendantId, String tenantId) {
        if (!StringUtils.hasText(ancestorId)) {
            return false;
        }
        if (ancestorId.equals(descendantId)) {
            return true;
        }
        TblDimensionMember ancestor = dimensionMemberMapper.selectById(ancestorId);
        if (ancestor != null && StringUtils.hasText(ancestor.getParentMemberId())) {
            return isDescendant(ancestor.getParentMemberId(), descendantId, tenantId);
        }
        return false;
    }

    /**
     * 递归更新子节点层级
     */
    private void updateChildrenLevel(TblDimensionMember parent, String tenantId) {
        List<TblDimensionMember> children = dimensionMemberMapper.selectChildMembers(
            parent.getMemberId(), tenantId);
        for (TblDimensionMember child : children) {
            child.setMemberLevel(parent.getMemberLevel() + 1);
            dimensionMemberMapper.updateById(child);
            updateChildrenLevel(child, tenantId);
        }
    }

    @Override
    public boolean checkCodeExists(String dimensionId, String memberCode, String memberId) {
        try {
            String tenantId = UserUtils.getUser().getOrgid().toString();
            TblDimensionMember existMember = dimensionMemberMapper.selectByCode(
                dimensionId, memberCode, tenantId);

            if (existMember == null) {
                return false;
            }

            // 如果是修改，排除自己
            if (StringUtils.hasText(memberId) && memberId.equals(existMember.getMemberId())) {
                return false;
            }

            return true;
        } catch (Exception e) {
            log.error("检查成员编码失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateStatus(String memberId, String status) {
        try {
            String userId = UserUtils.getUser().getStaffid().toString();
            Date now = new Date();

            LambdaUpdateWrapper<TblDimensionMember> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(TblDimensionMember::getMemberId, memberId)
                .set(TblDimensionMember::getStatus, status)
                .set(TblDimensionMember::getUpdateUser, userId)
                .set(TblDimensionMember::getUpdateTime, now);

            return dimensionMemberMapper.update(null, updateWrapper) > 0;
        } catch (Exception e) {
            log.error("更新维度成员状态失败", e);
            throw new RuntimeException("更新维度成员状态失败: " + e.getMessage());
        }
    }
}

