package com.management.accountant.service.eps.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.eps.EpsBudgetTemplate;
import com.management.accountant.mapper.eps.EpsBudgetTemplateMapper;
import com.management.accountant.service.eps.EpsBudgetTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预算模板管理服务实现类
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
public class EpsBudgetTemplateServiceImpl extends ServiceImpl<EpsBudgetTemplateMapper, EpsBudgetTemplate> 
        implements EpsBudgetTemplateService {

    @Override
    public IPage<EpsBudgetTemplate> queryBudgetTemplatePage(Long current, Long size, String templateName, 
                                                          String templateType, String templateCategory, 
                                                          Long systemId, String status) {
        Page<EpsBudgetTemplate> page = new Page<>(current, size);
        return baseMapper.selectBudgetTemplatePage(page, templateName, templateType, templateCategory, systemId, status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBudgetTemplate(EpsBudgetTemplate budgetTemplate) {
        try {
            // 检查模板编码是否存在
            if (checkTemplateCodeExists(budgetTemplate.getTemplateCode(), null)) {
                throw new RuntimeException("模板编码已存在");
            }

            // 设置创建时间
            budgetTemplate.setCreatedTime(LocalDateTime.now());
            budgetTemplate.setUpdatedTime(LocalDateTime.now());
            budgetTemplate.setUsageCount(0);
            
            // 如果是默认模板，需要先取消其他默认模板
            if (budgetTemplate.getIsDefault() != null && budgetTemplate.getIsDefault() == 1) {
                baseMapper.unsetDefaultTemplate(budgetTemplate.getSystemId(), budgetTemplate.getTemplateCategory());
            }

            return save(budgetTemplate);
        } catch (Exception e) {
            log.error("创建预算模板失败", e);
            throw new RuntimeException("创建预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetTemplate(EpsBudgetTemplate budgetTemplate) {
        try {
            // 检查模板编码是否存在
            if (checkTemplateCodeExists(budgetTemplate.getTemplateCode(), budgetTemplate.getTemplateId())) {
                throw new RuntimeException("模板编码已存在");
            }

            // 设置更新时间
            budgetTemplate.setUpdatedTime(LocalDateTime.now());
            
            // 如果是默认模板，需要先取消其他默认模板
            if (budgetTemplate.getIsDefault() != null && budgetTemplate.getIsDefault() == 1) {
                baseMapper.unsetDefaultTemplate(budgetTemplate.getSystemId(), budgetTemplate.getTemplateCategory());
            }

            return updateById(budgetTemplate);
        } catch (Exception e) {
            log.error("更新预算模板失败", e);
            throw new RuntimeException("更新预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBudgetTemplate(Long templateId) {
        try {
            // 检查是否可以删除（例如是否有关联的预算数据）
            // TODO: 添加业务规则检查
            
            return removeById(templateId);
        } catch (Exception e) {
            log.error("删除预算模板失败", e);
            throw new RuntimeException("删除预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteBudgetTemplate(List<Long> templateIds) {
        try {
            return removeByIds(templateIds);
        } catch (Exception e) {
            log.error("批量删除预算模板失败", e);
            throw new RuntimeException("批量删除预算模板失败：" + e.getMessage());
        }
    }

    @Override
    public EpsBudgetTemplate getBudgetTemplateById(Long templateId) {
        return getById(templateId);
    }

    @Override
    public EpsBudgetTemplate getBudgetTemplateByCode(String templateCode) {
        return baseMapper.selectByTemplateCode(templateCode);
    }

    @Override
    public List<EpsBudgetTemplate> getBudgetTemplatesBySystemId(Long systemId) {
        return baseMapper.selectBySystemId(systemId);
    }

    @Override
    public EpsBudgetTemplate getDefaultBudgetTemplate(Long systemId, String templateCategory) {
        return baseMapper.selectDefaultTemplate(systemId, templateCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultBudgetTemplate(Long templateId, Long systemId, String templateCategory) {
        try {
            // 先取消其他默认模板
            baseMapper.unsetDefaultTemplate(systemId, templateCategory);
            // 设置新的默认模板
            return baseMapper.setDefaultTemplate(templateId, systemId, templateCategory) > 0;
        } catch (Exception e) {
            log.error("设置默认预算模板失败", e);
            throw new RuntimeException("设置默认预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateBudgetTemplate(Long templateId) {
        try {
            EpsBudgetTemplate budgetTemplate = new EpsBudgetTemplate();
            budgetTemplate.setTemplateId(templateId);
            budgetTemplate.setStatus("ACTIVE");
            budgetTemplate.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetTemplate);
        } catch (Exception e) {
            log.error("激活预算模板失败", e);
            throw new RuntimeException("激活预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deactivateBudgetTemplate(Long templateId) {
        try {
            EpsBudgetTemplate budgetTemplate = new EpsBudgetTemplate();
            budgetTemplate.setTemplateId(templateId);
            budgetTemplate.setStatus("INACTIVE");
            budgetTemplate.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetTemplate);
        } catch (Exception e) {
            log.error("停用预算模板失败", e);
            throw new RuntimeException("停用预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean archiveBudgetTemplate(Long templateId) {
        try {
            EpsBudgetTemplate budgetTemplate = new EpsBudgetTemplate();
            budgetTemplate.setTemplateId(templateId);
            budgetTemplate.setStatus("ARCHIVED");
            budgetTemplate.setUpdatedTime(LocalDateTime.now());
            return updateById(budgetTemplate);
        } catch (Exception e) {
            log.error("归档预算模板失败", e);
            throw new RuntimeException("归档预算模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyBudgetTemplate(Long sourceTemplateId, String targetTemplateCode, String targetTemplateName) {
        try {
            // 获取源模板
            EpsBudgetTemplate sourceTemplate = getById(sourceTemplateId);
            if (sourceTemplate == null) {
                throw new RuntimeException("源预算模板不存在");
            }

            // 检查目标模板编码是否存在
            if (checkTemplateCodeExists(targetTemplateCode, null)) {
                throw new RuntimeException("目标模板编码已存在");
            }

            // 创建新模板
            EpsBudgetTemplate targetTemplate = new EpsBudgetTemplate();
            BeanUtils.copyProperties(sourceTemplate, targetTemplate);
            targetTemplate.setTemplateId(null);
            targetTemplate.setTemplateCode(targetTemplateCode);
            targetTemplate.setTemplateName(targetTemplateName);
            targetTemplate.setStatus("DRAFT");
            targetTemplate.setIsDefault(0);
            targetTemplate.setUsageCount(0);
            targetTemplate.setCreatedTime(LocalDateTime.now());
            targetTemplate.setUpdatedTime(LocalDateTime.now());

            return save(targetTemplate);
        } catch (Exception e) {
            log.error("复制预算模板失败", e);
            throw new RuntimeException("复制预算模板失败：" + e.getMessage());
        }
    }

    @Override
    public boolean checkTemplateCodeExists(String templateCode, Long excludeId) {
        return baseMapper.checkTemplateCodeExists(templateCode, excludeId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> templateIds, String status, Long updatedBy) {
        try {
            return baseMapper.batchUpdateStatus(templateIds, status, updatedBy) > 0;
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            throw new RuntimeException("批量更新状态失败：" + e.getMessage());
        }
    }

    @Override
    public List<EpsBudgetTemplate> getBudgetTemplatesByType(String templateType, Long systemId) {
        return baseMapper.selectByTemplateType(templateType, systemId);
    }

    @Override
    public List<EpsBudgetTemplate> getBudgetTemplatesByCategory(String templateCategory, Long systemId) {
        return baseMapper.selectByTemplateCategory(templateCategory, systemId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUsageCount(Long templateId) {
        try {
            return baseMapper.updateUsageCount(templateId) > 0;
        } catch (Exception e) {
            log.error("更新使用次数失败", e);
            throw new RuntimeException("更新使用次数失败：" + e.getMessage());
        }
    }

    @Override
    public List<EpsBudgetTemplate> getPopularTemplates(Long systemId, Integer limit) {
        return baseMapper.selectPopularTemplates(systemId, limit);
    }

    @Override
    public List<EpsBudgetTemplate> getRecentlyUsedTemplates(Long systemId, Long userId, Integer limit) {
        return baseMapper.selectRecentlyUsedTemplates(systemId, userId, limit);
    }

    @Override
    public List<EpsBudgetTemplate> getBudgetTemplatesByTags(List<String> tags, Long systemId) {
        return baseMapper.selectByTags(tags, systemId);
    }

    @Override
    public List<EpsBudgetTemplate> getSharedTemplates(Long systemId) {
        return baseMapper.selectSharedTemplates(systemId);
    }

    @Override
    public List<EpsBudgetTemplate> getSystemTemplates() {
        return baseMapper.selectSystemTemplates();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importTemplate(String templateData, Long systemId) {
        try {
            // TODO: 实现模板导入逻辑
            // 1. 解析模板数据
            // 2. 验证模板结构
            // 3. 保存模板
            log.info("导入模板功能待实现");
            return true;
        } catch (Exception e) {
            log.error("导入模板失败", e);
            throw new RuntimeException("导入模板失败：" + e.getMessage());
        }
    }

    @Override
    public String exportTemplate(Long templateId) {
        try {
            // TODO: 实现模板导出逻辑
            // 1. 获取模板数据
            // 2. 转换为导出格式
            // 3. 返回模板数据
            log.info("导出模板功能待实现");
            return "";
        } catch (Exception e) {
            log.error("导出模板失败", e);
            throw new RuntimeException("导出模板失败：" + e.getMessage());
        }
    }

    @Override
    public boolean validateTemplateStructure(String templateStructure) {
        try {
            // TODO: 实现模板结构验证逻辑
            // 1. 解析模板结构
            // 2. 验证字段完整性
            // 3. 验证数据类型
            // 4. 验证业务规则
            log.info("模板结构验证功能待实现");
            return true;
        } catch (Exception e) {
            log.error("验证模板结构失败", e);
            return false;
        }
    }
}
