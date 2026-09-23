package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryAuthority;
import com.global.treasurer.mapper.RegulatoryAuthorityMapper;
import com.global.treasurer.service.RegulatoryAuthorityService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 监管机构服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class RegulatoryAuthorityServiceImpl implements RegulatoryAuthorityService {
    @Autowired
    private RegulatoryAuthorityMapper authorityMapper;

    @Override
    public PageInfo<TblRegulatoryAuthority> getAuthorityList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblRegulatoryAuthority> list = authorityMapper.selectAuthorityList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblRegulatoryAuthority getAuthorityById(String authorityId) {
        TblRegulatoryAuthority authority = authorityMapper.selectAuthorityById(authorityId);
        if (authority == null) {
            throw new ServiceException(404, "监管机构不存在");
        }
        return authority;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblRegulatoryAuthority saveAuthority(TblRegulatoryAuthority authority) {
        if (authority.getAuthorityId() == null || authority.getAuthorityId().isEmpty()) {
            authority.setDeleteFlag(0);
            authority.setIsActive(1);
            authority.setCreatedTime(new Date());
            authorityMapper.insert(authority);
        } else {
            authority.setUpdatedTime(new Date());
            authorityMapper.updateById(authority);
        }
        return authority;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAuthority(String authorityId) {
        TblRegulatoryAuthority authority = getAuthorityById(authorityId);
        authority.setDeleteFlag(1);
        authority.setUpdatedTime(new Date());
        authorityMapper.updateById(authority);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteAuthorities(List<String> authorityIds) {
        authorityMapper.batchDeleteByIds(authorityIds);
    }

    @Override
    public List<TblRegulatoryAuthority> getAuthoritiesByType(String authorityType) {
        return authorityMapper.selectByAuthorityType(authorityType);
    }

    @Override
    public List<TblRegulatoryAuthority> getActiveAuthorities() {
        return authorityMapper.selectActiveAuthorities();
    }

    @Override
    public List<TblRegulatoryAuthority> getImportantAuthorities() {
        return authorityMapper.selectImportantAuthorities();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleAuthorityStatus(String authorityId, Integer isActive) {
        authorityMapper.updateAuthorityStatus(authorityId, isActive);
    }

    @Override
    public List<TblRegulatoryAuthority> exportAuthorityList(Map<String, Object> params) {
        return authorityMapper.selectAuthorityList(params);
    }
}

