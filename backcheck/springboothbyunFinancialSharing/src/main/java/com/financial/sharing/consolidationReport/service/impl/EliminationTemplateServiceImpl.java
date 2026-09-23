package com.financial.sharing.consolidationReport.service.impl;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EliminationTemplateQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationTemplate;
import com.financial.sharing.consolidationReport.mapper.EliminationTemplateMapper;
import com.financial.sharing.consolidationReport.service.EliminationTemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 抵消凭证模板Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class EliminationTemplateServiceImpl implements EliminationTemplateService {

    @Autowired
    private EliminationTemplateMapper eliminationTemplateMapper;

    @Override
    public PageInfo<TblEliminationTemplate> getTemplateList(EliminationTemplateQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblEliminationTemplate> list = eliminationTemplateMapper.selectTemplateList(param);
        return new PageInfo<>(list);
    }

    @Override
    public TblEliminationTemplate getTemplateById(String templateId) {
        return eliminationTemplateMapper.selectById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTemplate(TblEliminationTemplate template) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查模板编码是否已存在
        int count = eliminationTemplateMapper.checkTemplateCodeExists(
            template.getModelId(), template.getTemplateCode(), null);
        if (count > 0) {
            throw new RuntimeException("模板编码已存在");
        }

        // 设置默认值
        template.setTemplateId(UUID.randomUUID().toString().replace("-", ""));
        template.setTenantId(tenantId);
        template.setCreateUser(userId);
        template.setCreateTime(now);
        template.setUpdateUser(userId);
        template.setUpdateTime(now);

        if (template.getIsActive() == null || template.getIsActive().isEmpty()) {
            template.setIsActive("Y");
        }
        if (template.getSortOrder() == null) {
            template.setSortOrder(0);
        }

        eliminationTemplateMapper.insert(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplate(TblEliminationTemplate template) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 检查模板编码是否已存在
        int count = eliminationTemplateMapper.checkTemplateCodeExists(
            template.getModelId(), template.getTemplateCode(), template.getTemplateId());
        if (count > 0) {
            throw new RuntimeException("模板编码已存在");
        }

        template.setUpdateUser(userId);
        template.setUpdateTime(now);

        eliminationTemplateMapper.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String templateId) {
        eliminationTemplateMapper.deleteById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateTemplateStatus(String templateId, String isActive) {
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        TblEliminationTemplate template = new TblEliminationTemplate();
        template.setTemplateId(templateId);
        template.setIsActive(isActive);
        template.setUpdateUser(userId);
        template.setUpdateTime(now);

        eliminationTemplateMapper.updateById(template);
    }

    @Override
    public List<TblEliminationTemplate> getTemplateListByModelId(String modelId) {
        return eliminationTemplateMapper.selectByModelId(modelId);
    }
}

