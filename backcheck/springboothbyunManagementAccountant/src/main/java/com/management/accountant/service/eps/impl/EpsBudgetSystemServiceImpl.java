package com.management.accountant.service.eps.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.eps.EpsBudgetSystem;
import com.management.accountant.mapper.eps.EpsBudgetSystemMapper;
import com.management.accountant.service.eps.EpsBudgetSystemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预算体系管理服务实现类
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
public class EpsBudgetSystemServiceImpl extends ServiceImpl<EpsBudgetSystemMapper, EpsBudgetSystem> 
        implements EpsBudgetSystemService {

    @Override
    public IPage<EpsBudgetSystem> queryBudgetSystemPage(Long current, Long size, String systemName, 
                                                      String systemType, Integer fiscalYear, 
                                                      Long organizationId, String status) {
        Page<EpsBudgetSystem> page = new Page<>(current, size);
        return baseMapper.selectBudgetSystemPage(page, systemName, systemType, fiscalYear, organizationId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBudgetSystem(EpsBudgetSystem budgetSystem) {
        try {
            // 检查体系编码是否存在
            if (checkSystemCodeExists(budgetSystem.getSystemCode(), null)) {
                throw new RuntimeException("体系编码已存在");
            }

            // 设置创建时间
            budgetSystem.setCreatedTime(LocalDateTime.now());
            budgetSystem.setUpdatedTime(LocalDateTime.now());
            
            // 如果是默认体系，需要先取消其他默认体系
            if (budgetSystem.getIsDefault() != null && budgetSystem.getIsDefault() == 1) {
                baseMapper.unsetDefaultSystem(budgetSystem.getOrganizationId());
            }

            return save(budgetSystem);
        } catch (Exception e) {
            log.error("创建预算体系失败", e);
            throw new RuntimeException("创建预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetSystem(EpsBudgetSystem budgetSystem) {
        try {
            // 检查体系编码是否存在
            if (checkSystemCodeExists(budgetSystem.getSystemCode(), budgetSystem.getSystemId())) {
                throw new RuntimeException("体系编码已存在");
            }

            // 设置更新时间
            budgetSystem.setUpdatedTime(LocalDateTime.now());
            
            // 如果是默认体系，需要先取消其他默认体系
            if (budgetSystem.getIsDefault() != null && budgetSystem.getIsDefault() == 1) {
                baseMapper.unsetDefaultSystem(budgetSystem.getOrganizationId());
            }

            return updateById(budgetSystem);
        } catch (Exception e) {
            log.error("更新预算体系失败", e);
            throw new RuntimeException("更新预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBudgetSystem(Long systemId) {
        try {
            // 检查是否可以删除（例如是否有关联的预算数据）
            // TODO: 添加业务规则检查
            
            return removeById(systemId);
        } catch (Exception e) {
            log.error("删除预算体系失败", e);
            throw new RuntimeException("删除预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBudgetSystem(List<Long> systemIds) {
        try {
            return removeByIds(systemIds);
        } catch (Exception e) {
            log.error("批量删除预算体系失败", e);
            throw new RuntimeException("批量删除预算体系失败：" + e.getMessage());
        }
    }

    @Override
    public EpsBudgetSystem getBudgetSystemById(Long systemId) {
        return getById(systemId);
    }

    @Override
    public EpsBudgetSystem getBudgetSystemByCode(String systemCode) {
        return baseMapper.selectBySystemCode(systemCode);
    }

    @Override
    public List<EpsBudgetSystem> getBudgetSystemsByOrganization(Long organizationId) {
        return baseMapper.selectByOrganizationId(organizationId);
    }

    @Override
    public EpsBudgetSystem getDefaultBudgetSystem(Long organizationId) {
        return baseMapper.selectDefaultSystem(organizationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultBudgetSystem(Long systemId, Long organizationId) {
        try {
            // 先取消其他默认体系
            baseMapper.unsetDefaultSystem(organizationId);
            // 设置新的默认体系
            return baseMapper.setDefaultSystem(systemId, organizationId) > 0;
        } catch (Exception e) {
            log.error("设置默认预算体系失败", e);
            throw new RuntimeException("设置默认预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateBudgetSystem(Long systemId) {
        try {
            EpsBudgetSystem budgetSystem = new EpsBudgetSystem();
            budgetSystem.setSystemId(systemId);
            budgetSystem.setStatus("ACTIVE");
            budgetSystem.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetSystem);
        } catch (Exception e) {
            log.error("激活预算体系失败", e);
            throw new RuntimeException("激活预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deactivateBudgetSystem(Long systemId) {
        try {
            EpsBudgetSystem budgetSystem = new EpsBudgetSystem();
            budgetSystem.setSystemId(systemId);
            budgetSystem.setStatus("INACTIVE");
            budgetSystem.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetSystem);
        } catch (Exception e) {
            log.error("停用预算体系失败", e);
            throw new RuntimeException("停用预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveBudgetSystem(Long systemId) {
        try {
            EpsBudgetSystem budgetSystem = new EpsBudgetSystem();
            budgetSystem.setSystemId(systemId);
            budgetSystem.setStatus("ARCHIVED");
            budgetSystem.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetSystem);
        } catch (Exception e) {
            log.error("归档预算体系失败", e);
            throw new RuntimeException("归档预算体系失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyBudgetSystem(Long sourceSystemId, String targetSystemCode, 
                                  String targetSystemName, Integer targetFiscalYear) {
        try {
            // 获取源体系
            EpsBudgetSystem sourceSystem = getById(sourceSystemId);
            if (sourceSystem == null) {
                throw new RuntimeException("源预算体系不存在");
            }

            // 检查目标体系编码是否存在
            if (checkSystemCodeExists(targetSystemCode, null)) {
                throw new RuntimeException("目标体系编码已存在");
            }

            // 创建新体系
            EpsBudgetSystem targetSystem = new EpsBudgetSystem();
            BeanUtils.copyProperties(sourceSystem, targetSystem);
            targetSystem.setSystemId(null);
            targetSystem.setSystemCode(targetSystemCode);
            targetSystem.setSystemName(targetSystemName);
            targetSystem.setFiscalYear(targetFiscalYear);
            targetSystem.setStatus("DRAFT");
            targetSystem.setIsDefault(0);
            targetSystem.setCreatedTime(LocalDateTime.now());
            targetSystem.setUpdatedTime(LocalDateTime.now());

            return save(targetSystem);
        } catch (Exception e) {
            log.error("复制预算体系失败", e);
            throw new RuntimeException("复制预算体系失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkSystemCodeExists(String systemCode, Long excludeId) {
        return baseMapper.checkSystemCodeExists(systemCode, excludeId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> systemIds, String status, Long updatedBy) {
        try {
            return baseMapper.batchUpdateStatus(systemIds, status, updatedBy) > 0;
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            throw new RuntimeException("批量更新状态失败：" + e.getMessage());
        }
    }

    @Override
    public List<EpsBudgetSystem> getBudgetSystemsByFiscalYear(Integer fiscalYear, Long organizationId) {
        return baseMapper.selectByFiscalYear(fiscalYear, organizationId);
    }
}
