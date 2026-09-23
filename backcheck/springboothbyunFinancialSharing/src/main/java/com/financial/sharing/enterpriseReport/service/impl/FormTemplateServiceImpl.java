package com.financial.sharing.enterpriseReport.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.financial.sharing.enterpriseReport.dto.FormTemplateQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormTemplate;
import com.financial.sharing.enterpriseReport.mapper.FormTemplateMapper;
import com.financial.sharing.enterpriseReport.service.FormTemplateService;
import com.financial.sharing.util.UserUtils;
import com.hbfk.util.user.UserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 表单模板Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class FormTemplateServiceImpl extends ServiceImpl<FormTemplateMapper, TblFormTemplate> 
        implements FormTemplateService {

    @Override
    public List<TblFormTemplate> getList(FormTemplateQueryParam param) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        return baseMapper.selectFormTemplateList(
            tenantId,
            param.getGroupId(),
            param.getTemplateName(),
            param.getTemplateType(),
            param.getStatus()
        );
    }

    @Override
    public TblFormTemplate getDetail(String templateId) {
        TblFormTemplate formTemplate = this.getById(templateId);
        if (formTemplate != null) {
            // 补充关联信息
            String tenantId = UserUtils.getUser().getOrgid().toString();
            List<TblFormTemplate> list = baseMapper.selectFormTemplateList(
                tenantId, null, null, null, null
            );
            for (TblFormTemplate template : list) {
                if (template.getTemplateId().equals(templateId)) {
                    formTemplate.setGroupName(template.getGroupName());
                    break;
                }
            }
        }
        return formTemplate;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveFormTemplate(TblFormTemplate formTemplate) {
        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();
        
        formTemplate.setTenantId(tenantId);
        
        // 检查模板编码是否重复
        int count = baseMapper.checkTemplateCodeExists(
            formTemplate.getTemplateCode(),
            tenantId,
            formTemplate.getTemplateId()
        );
        if (count > 0) {
            throw new RuntimeException("模板编码已存在");
        }
        
        if (StringUtils.isEmpty(formTemplate.getTemplateId())) {
            // 新增
            formTemplate.setCreateUser(userId);
            formTemplate.setCreateTime(now);
            formTemplate.setUpdateUser(userId);
            formTemplate.setUpdateTime(now);
            
            // 设置默认排序号
            if (formTemplate.getSortNo() == null) {
                formTemplate.setSortNo(0);
            }
            
            // 设置默认状态
            if (StringUtils.isEmpty(formTemplate.getStatus())) {
                formTemplate.setStatus("ACTIVE");
            }
            
            // 设置默认版本号
            if (StringUtils.isEmpty(formTemplate.getVersionNo())) {
                formTemplate.setVersionNo("1.0");
            }
            
            // 设置默认模板类型
            if (StringUtils.isEmpty(formTemplate.getTemplateType())) {
                formTemplate.setTemplateType("FIXED");
            }
            
            // 如果设置为默认版本,先取消同组其他模板的默认标识
            if ("Y".equals(formTemplate.getIsDefault())) {
                baseMapper.cancelDefaultVersion(formTemplate.getGroupId(), tenantId);
            } else {
                formTemplate.setIsDefault("N");
            }
            
            return this.save(formTemplate);
        } else {
            // 修改
            formTemplate.setUpdateUser(userId);
            formTemplate.setUpdateTime(now);
            
            // 如果设置为默认版本,先取消同组其他模板的默认标识
            if ("Y".equals(formTemplate.getIsDefault())) {
                baseMapper.cancelDefaultVersion(formTemplate.getGroupId(), tenantId);
            }
            
            return this.updateById(formTemplate);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFormTemplate(String templateId) {
        // TODO: 检查是否有公式关联
        // TODO: 检查是否有报表数据关联
        
        return this.removeById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultVersion(String templateId) {
        TblFormTemplate formTemplate = this.getById(templateId);
        if (formTemplate == null) {
            throw new RuntimeException("模板不存在");
        }
        
        String tenantId = UserUtils.getUser().getOrgid().toString();
        
        // 取消同组其他模板的默认标识
        baseMapper.cancelDefaultVersion(formTemplate.getGroupId(), tenantId);
        
        // 设置当前模板为默认版本
        formTemplate.setIsDefault("Y");
        formTemplate.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        formTemplate.setUpdateTime(new Date());
        
        return this.updateById(formTemplate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyTemplate(String templateId, String newTemplateName, String newVersionNo) {
        TblFormTemplate sourceTemplate = this.getById(templateId);
        if (sourceTemplate == null) {
            throw new RuntimeException("源模板不存在");
        }

        String tenantId = UserUtils.getUser().getOrgid().toString();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 创建新模板
        TblFormTemplate newTemplate = new TblFormTemplate();
        newTemplate.setGroupId(sourceTemplate.getGroupId());
        newTemplate.setTemplateCode(sourceTemplate.getTemplateCode() + "_COPY");
        newTemplate.setTemplateName(newTemplateName);
        newTemplate.setTemplateType(sourceTemplate.getTemplateType());
        newTemplate.setTemplateContent(sourceTemplate.getTemplateContent());
        newTemplate.setVersionNo(newVersionNo);
        newTemplate.setStartPeriod(sourceTemplate.getStartPeriod());
        newTemplate.setEndPeriod(sourceTemplate.getEndPeriod());
        newTemplate.setIsDefault("N");
        newTemplate.setSortNo(sourceTemplate.getSortNo());
        newTemplate.setStatus("ACTIVE");
        newTemplate.setTenantId(tenantId);
        newTemplate.setCreateUser(userId);
        newTemplate.setCreateTime(now);
        newTemplate.setUpdateUser(userId);
        newTemplate.setUpdateTime(now);

        return this.save(newTemplate);
    }

    @Override
    public List<TblFormTemplate> getListByGroupId(String groupId) {
        String tenantId = UserUtils.getUser().getOrgid().toString();

        QueryWrapper<TblFormTemplate> wrapper = new QueryWrapper<>();
        wrapper.eq("TENANT_ID", tenantId);
        wrapper.eq("GROUP_ID", groupId);
        wrapper.eq("STATUS", "ACTIVE");
        wrapper.orderByAsc("SORT_NO", "CREATE_TIME");

        return this.list(wrapper);
    }
}
