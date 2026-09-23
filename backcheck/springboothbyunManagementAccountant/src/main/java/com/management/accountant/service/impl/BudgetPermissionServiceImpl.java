package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetPermission;
import com.management.accountant.oracle.mapper.budget.BudgetPermissionMapper;
import com.management.accountant.service.BudgetPermissionService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BudgetPermissionServiceImpl implements BudgetPermissionService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetPermissionMapper permissionMapper;

    @Override
    public PageResult<BudgetPermission> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);

        if (params.get("permissionType") != null && StringUtils.hasText(params.get("permissionType").toString())) {
            wrapper.eq("PERMISSION_TYPE", params.get("permissionType"));
        }
        if (params.get("permissionName") != null && StringUtils.hasText(params.get("permissionName").toString())) {
            wrapper.like("PERMISSION_NAME", params.get("permissionName"));
        }
        if (params.get("roleName") != null && StringUtils.hasText(params.get("roleName").toString())) {
            wrapper.like("ROLE_NAME", params.get("roleName"));
        }
        if (params.get("resourceType") != null && StringUtils.hasText(params.get("resourceType").toString())) {
            wrapper.eq("RESOURCE_TYPE", params.get("resourceType"));
        }
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetPermission> page = new Page<>(pageNum, pageSize);
        IPage<BudgetPermission> pageResult = permissionMapper.selectPage(page, wrapper);

        PageResult<BudgetPermission> result = new PageResult<>();
        result.setList(pageResult.getRecords());
        result.setTotal((int) pageResult.getTotal());
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        return result;
    }

    @Override
    public BudgetPermission getById(String permissionId) {
        if (!StringUtils.hasText(permissionId)) {
            return null;
        }
        QueryWrapper<BudgetPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("PERMISSION_ID", permissionId).eq("IS_DELETED", 0);
        return permissionMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetPermission create(BudgetPermission permission) {
        if (permission == null) {
            throw new ServiceException("权限信息不能为空");
        }
        if (!StringUtils.hasText(permission.getPermissionName())) {
            throw new ServiceException("权限名称不能为空");
        }
        if (permission.getIsDeleted() == null) {
            permission.setIsDeleted(0);
        }
        if (permission.getIsEnabled() == null) {
            permission.setIsEnabled(1);
        }
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());

        int result = permissionMapper.insert(permission);
        if (result <= 0) {
            throw new ServiceException("创建权限失败");
        }
        log.info("创建权限成功，ID: {}", permission.getPermissionId());
        return permission;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetPermission permission) {
        if (permission == null || !StringUtils.hasText(permission.getPermissionId())) {
            throw new ServiceException("权限ID不能为空");
        }
        BudgetPermission existing = getById(permission.getPermissionId());
        if (existing == null) {
            throw new ServiceException("权限不存在");
        }
        permission.setUpdateTime(new Date());
        permissionMapper.updateById(permission);
        log.info("更新权限成功，ID: {}", permission.getPermissionId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String permissionId) {
        if (!StringUtils.hasText(permissionId)) {
            throw new ServiceException("权限ID不能为空");
        }
        BudgetPermission existing = getById(permissionId);
        if (existing == null) {
            throw new ServiceException("权限不存在");
        }
        BudgetPermission update = new BudgetPermission();
        update.setPermissionId(permissionId);
        update.setIsDeleted(1);
        update.setUpdateTime(new Date());
        permissionMapper.updateById(update);
        log.info("删除权限成功，ID: {}", permissionId);
    }

    @Override
    public List<BudgetPermission> exportData(Map<String, Object> params) {
        QueryWrapper<BudgetPermission> wrapper = new QueryWrapper<>();
        wrapper.eq("IS_DELETED", 0);
        wrapper.orderByDesc("CREATE_TIME");
        return permissionMapper.selectList(wrapper);
    }
}

