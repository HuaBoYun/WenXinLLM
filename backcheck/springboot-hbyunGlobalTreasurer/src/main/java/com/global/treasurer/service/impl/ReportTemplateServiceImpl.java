package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblReportTemplate;
import com.global.treasurer.mapper.ReportTemplateMapper;
import com.global.treasurer.service.ReportTemplateService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 报告模板服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class ReportTemplateServiceImpl implements ReportTemplateService {
    @Autowired
    private ReportTemplateMapper templateMapper;

    @Override
    public PageInfo<TblReportTemplate> getTemplateList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblReportTemplate> list = templateMapper.selectTemplateList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblReportTemplate getTemplateById(String templateId) {
        TblReportTemplate template = templateMapper.selectTemplateById(templateId);
        if (template == null) {
            throw new ServiceException(404, "报告模板不存在");
        }
        return template;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportTemplate saveTemplate(TblReportTemplate template) {
        if (template.getTemplateId() == null || template.getTemplateId().isEmpty()) {
            template.setDeleteFlag(0);
            template.setIsEnabled(1);
            template.setCreatedTime(new Date());
            templateMapper.insert(template);
        } else {
            template.setUpdatedTime(new Date());
            templateMapper.updateById(template);
        }
        return template;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String templateId) {
        TblReportTemplate template = getTemplateById(templateId);
        template.setDeleteFlag(1);
        template.setUpdatedTime(new Date());
        templateMapper.updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteTemplates(List<String> templateIds) {
        templateMapper.batchDeleteByIds(templateIds);
    }

    @Override
    public List<TblReportTemplate> getTemplatesByAuthority(String authorityId) {
        return templateMapper.selectByAuthorityId(authorityId);
    }

    @Override
    public List<TblReportTemplate> getUsableTemplates() {
        return templateMapper.selectUsableTemplates();
    }

    @Override
    public List<TblReportTemplate> getExpiringSoonTemplates(Integer days) {
        return templateMapper.selectExpiringSoonTemplates(days);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleTemplateStatus(String templateId, Integer isEnabled) {
        templateMapper.updateTemplateStatus(templateId, isEnabled);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportTemplate copyTemplate(String sourceTemplateId, String newTemplateCode, String newTemplateName) {
        TblReportTemplate source = getTemplateById(sourceTemplateId);
        TblReportTemplate newTemplate = new TblReportTemplate();
        BeanUtils.copyProperties(source, newTemplate);
        newTemplate.setTemplateId(null);
        newTemplate.setTemplateCode(newTemplateCode);
        newTemplate.setTemplateName(newTemplateName);
        newTemplate.setCreatedTime(new Date());
        newTemplate.setUpdatedTime(null);
        templateMapper.insert(newTemplate);
        return newTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblReportTemplate createTemplateVersion(String sourceTemplateId, String newVersion) {
        TblReportTemplate source = getTemplateById(sourceTemplateId);
        TblReportTemplate newTemplate = new TblReportTemplate();
        BeanUtils.copyProperties(source, newTemplate);
        newTemplate.setTemplateId(null);
        newTemplate.setTemplateVersion(newVersion);
        newTemplate.setCreatedTime(new Date());
        newTemplate.setUpdatedTime(null);
        templateMapper.insert(newTemplate);
        return newTemplate;
    }
}

