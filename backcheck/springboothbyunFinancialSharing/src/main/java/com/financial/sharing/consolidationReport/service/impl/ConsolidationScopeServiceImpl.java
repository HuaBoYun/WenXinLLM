package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ConsolidationScopeQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationScope;
import com.financial.sharing.consolidationReport.mapper.ConsolidationScopeMapper;
import com.financial.sharing.consolidationReport.service.ConsolidationScopeService;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 合并范围配置Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class ConsolidationScopeServiceImpl implements ConsolidationScopeService {

    @Autowired
    private ConsolidationScopeMapper consolidationScopeMapper;

    @Override
    public List<TblConsolidationScope> getScopeList(ConsolidationScopeQueryParam param) {
        return consolidationScopeMapper.selectScopeList(param);
    }

    @Override
    public TblConsolidationScope getScopeById(String scopeId) {
        return consolidationScopeMapper.selectById(scopeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveScope(TblConsolidationScope scope) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查组织是否已在合并范围内
        int count = consolidationScopeMapper.checkOrgExists(scope.getModelId(), scope.getOrgId(), null);
        if (count > 0) {
            throw new RuntimeException("该组织已在合并范围内");
        }

        // 设置默认值
        scope.setScopeId(UUID.randomUUID().toString().replace("-", ""));
        scope.setTenantId(tenantId);
        scope.setCreateUser(userId);
        scope.setCreateTime(now);
        scope.setUpdateUser(userId);
        scope.setUpdateTime(now);

        if (scope.getIsActive() == null || scope.getIsActive().isEmpty()) {
            scope.setIsActive("Y");
        }
        if (scope.getSortOrder() == null) {
            scope.setSortOrder(0);
        }

        consolidationScopeMapper.insert(scope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScope(TblConsolidationScope scope) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查组织是否已在合并范围内(排除自己)
        int count = consolidationScopeMapper.checkOrgExists(
            scope.getModelId(), scope.getOrgId(), scope.getScopeId());
        if (count > 0) {
            throw new RuntimeException("该组织已在合并范围内");
        }

        scope.setUpdateUser(userId);
        scope.setUpdateTime(now);

        consolidationScopeMapper.updateById(scope);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteScope(String scopeId) {
        consolidationScopeMapper.deleteById(scopeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSaveScope(String modelId, List<TblConsolidationScope> scopeList) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 先删除该模型的所有合并范围配置
        consolidationScopeMapper.deleteByModelId(modelId);

        // 批量插入新的合并范围配置
        if (scopeList != null && !scopeList.isEmpty()) {
            for (int i = 0; i < scopeList.size(); i++) {
                TblConsolidationScope scope = scopeList.get(i);
                scope.setScopeId(UUID.randomUUID().toString().replace("-", ""));
                scope.setModelId(modelId);
                scope.setTenantId(tenantId);
                scope.setCreateUser(userId);
                scope.setCreateTime(now);
                scope.setUpdateUser(userId);
                scope.setUpdateTime(now);
                
                if (scope.getIsActive() == null || scope.getIsActive().isEmpty()) {
                    scope.setIsActive("Y");
                }
                if (scope.getSortOrder() == null) {
                    scope.setSortOrder(i);
                }
            }
            consolidationScopeMapper.batchInsert(scopeList);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateScopeStatus(String scopeId, String isActive) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        TblConsolidationScope scope = new TblConsolidationScope();
        scope.setScopeId(scopeId);
        scope.setIsActive(isActive);
        scope.setUpdateUser(userId);
        scope.setUpdateTime(now);

        consolidationScopeMapper.updateById(scope);
    }
}

