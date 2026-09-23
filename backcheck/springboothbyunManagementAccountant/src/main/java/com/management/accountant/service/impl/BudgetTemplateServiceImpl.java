package com.management.accountant.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import com.management.accountant.oracle.mapper.budget.BudgetTemplateMapper;
import com.management.accountant.oracle.service.budget.BudgetTemplateService;
import com.management.accountant.vo.param.BudgetTemplateDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 预算模板 ServiceImpl
 */
@Service
@Slf4j
public class BudgetTemplateServiceImpl extends ServiceImpl<BudgetTemplateMapper, BudgetTemplate>
        implements BudgetTemplateService {

    @Override
    public BudgetTemplate getByTemplateCode(String templateCode) {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("TEMPLATE_CODE", templateCode).eq("DEL_FLAG", 0);
        return baseMapper.selectOne(w);
    }

    @Override
    public BudgetTemplate getDefaultTemplate() {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("IS_DEFAULT", 1).eq("IS_ENABLED", 1).eq("DEL_FLAG", 0).last("FETCH FIRST 1 ROWS ONLY");
        return baseMapper.selectOne(w);
    }

    @Override
    public List<BudgetTemplate> getByTemplateType(String templateType) {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("TEMPLATE_TYPE", templateType).eq("DEL_FLAG", 0).orderByAsc("SORT_ORDER");
        return baseMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createTemplate(BudgetTemplate template) {
        if (template == null) return false;
        if (template.getDelFlag() == null) template.setDelFlag(0);
        if (template.getIsEnabled() == null) template.setIsEnabled(1);
        template.setCreateTime(new Date());
        template.setUpdateTime(new Date());
        return save(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplate(BudgetTemplate template) {
        if (template == null || !StringUtils.hasText(template.getTemplateId())) return false;
        template.setUpdateTime(new Date());
        return updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTemplate copyTemplate(String templateId, String newTemplateName) {
        BudgetTemplate src = getById(templateId);
        if (src == null) return null;
        BudgetTemplate copy = new BudgetTemplate();
        copy.setTemplateName(newTemplateName != null ? newTemplateName : src.getTemplateName() + "_副本");
        copy.setTemplateCode(src.getTemplateCode() + "_copy_" + System.currentTimeMillis());
        copy.setTemplateType(src.getTemplateType());
        copy.setTemplateDescription(src.getTemplateDescription());
        copy.setTemplateConfig(src.getTemplateConfig());
        copy.setFieldCount(src.getFieldCount());
        copy.setCategoryId(src.getCategoryId());          // 保留分类，否则按分类过滤时看不到副本
        copy.setApplicableYear(src.getApplicableYear());
        copy.setSortOrder(src.getSortOrder());
        copy.setIsPublic(src.getIsPublic());
        copy.setAllowCopy(src.getAllowCopy());
        copy.setTemplateStatus(src.getTemplateStatus()); // 保留状态，否则被 DRAFT 过滤掉
        copy.setIsDefault(0);
        copy.setIsEnabled(1);
        copy.setIsSystem(0);
        copy.setDelFlag(0);
        copy.setCreateTime(new Date());
        copy.setUpdateTime(new Date());
        save(copy);
        return copy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetTemplate copyTemplate(String templateId) {
        return copyTemplate(templateId, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setDefaultTemplate(String templateId) {
        // 先清除所有默认
        BudgetTemplate clearAll = new BudgetTemplate();
        clearAll.setIsDefault(0);
        clearAll.setUpdateTime(new Date());
        update(clearAll, new QueryWrapper<BudgetTemplate>().eq("DEL_FLAG", 0));
        // 设置目标为默认
        BudgetTemplate target = new BudgetTemplate();
        target.setTemplateId(templateId);
        target.setIsDefault(1);
        target.setUpdateTime(new Date());
        return updateById(target);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleTemplate(String templateId, boolean enabled) {
        BudgetTemplate t = new BudgetTemplate();
        t.setTemplateId(templateId);
        t.setIsEnabled(enabled ? 1 : 0);
        t.setUpdateTime(new Date());
        return updateById(t);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchToggleTemplates(List<String> templateIds, boolean enabled) {
        if (templateIds == null || templateIds.isEmpty()) return false;
        BudgetTemplate t = new BudgetTemplate();
        t.setIsEnabled(enabled ? 1 : 0);
        t.setUpdateTime(new Date());
        return update(t, new QueryWrapper<BudgetTemplate>().in("TEMPLATE_ID", templateIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTemplates(List<String> templateIds) {
        if (templateIds == null || templateIds.isEmpty()) return false;
        BudgetTemplate t = new BudgetTemplate();
        t.setDelFlag(1);
        t.setUpdateTime(new Date());
        return update(t, new QueryWrapper<BudgetTemplate>().in("TEMPLATE_ID", templateIds));
    }

    @Override
    public Page<BudgetTemplate> pageQuery(int pageNum, int pageSize, BudgetTemplate condition) {
        Page<BudgetTemplate> page = new Page<>(pageNum, pageSize);
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);

        log.info("pageQuery 查询条件：pageNum={}, pageSize={}, condition={}", pageNum, pageSize, condition);

        if (condition != null) {
            if (StringUtils.hasText(condition.getTemplateName())) {
                w.like("TEMPLATE_NAME", condition.getTemplateName());
                log.info("添加查询条件：TEMPLATE_NAME LIKE {}", condition.getTemplateName());
            }
            if (StringUtils.hasText(condition.getTemplateType())) {
                w.eq("TEMPLATE_TYPE", condition.getTemplateType());
                log.info("添加查询条件：TEMPLATE_TYPE = {}", condition.getTemplateType());
            }
            if (StringUtils.hasText(condition.getTemplateStatus())) {
                w.eq("TEMPLATE_STATUS", condition.getTemplateStatus());
                log.info("添加查询条件：TEMPLATE_STATUS = {}", condition.getTemplateStatus());
            } else {
                // 默认排除草稿，兼容 Oracle（null != 'DRAFT' 在 Oracle 中不成立）
                w.and(qw -> qw.isNull("TEMPLATE_STATUS").or().ne("TEMPLATE_STATUS", "DRAFT"));
                log.info("添加默认查询条件：TEMPLATE_STATUS IS NULL OR != 'DRAFT'");
            }
            if (StringUtils.hasText(condition.getCategoryId())) {
                // 判断是否是一级分类（CAT001, CAT002, CAT003, CAT004）
                String categoryId = condition.getCategoryId();
                if (categoryId.matches("CAT00[1-4]")) {
                    // 一级分类：查询该分类下的所有子分类（LIKE查询）
                    w.likeRight("CATEGORY_ID", categoryId);
                    log.info("添加查询条件：CATEGORY_ID LIKE '{}%'", categoryId);
                } else {
                    // 二级分类：精确匹配
                    w.eq("CATEGORY_ID", categoryId);
                    log.info("添加查询条件：CATEGORY_ID = {}", categoryId);
                }
            }
        }

        w.orderByAsc("SORT_ORDER");
        w.orderByDesc("CREATE_TIME");

        log.info("最终查询条件：{}", w);

        Page<BudgetTemplate> result = baseMapper.selectPage(page, w);
        log.info("查询结果：总记录数={}, 当前页记录数={}", result.getTotal(), result.getRecords().size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTemplate(BudgetTemplateDTO dto) {
        if (dto == null) return false;
        BudgetTemplate t = new BudgetTemplate();
        t.setTemplateId(dto.getTemplateId());
        t.setTemplateName(dto.getTemplateName());
        t.setTemplateCode(dto.getTemplateCode());
        t.setTemplateType(dto.getTemplateType());
        t.setTemplateDescription(dto.getTemplateDescription());
        t.setIsDefault(dto.getIsDefault());
        t.setIsEnabled(dto.getIsEnabled() != null ? dto.getIsEnabled() : 1);
        t.setIsPublic(dto.getIsPublic());
        t.setAllowCopy(dto.getAllowCopy());
        t.setSortOrder(dto.getSortOrder());

        // 处理 status 字段（前端传 status，映射到 templateStatus）
        if (StringUtils.hasText(dto.getStatus())) {
            t.setTemplateStatus(dto.getStatus());
        }

        // 处理 categoryId（前端可能传数组或字符串，取最后一个元素）
        if (dto.getCategoryId() != null) {
            String catId;
            if (dto.getCategoryId() instanceof List) {
                List<?> list = (List<?>) dto.getCategoryId();
                catId = list.isEmpty() ? null : list.get(list.size() - 1).toString();
            } else {
                catId = dto.getCategoryId().toString();
            }
            t.setCategoryId(catId);
        }

        // 处理 applicableYear（前端传 ISO 日期字符串，取年份）
        if (StringUtils.hasText(dto.getApplicableYear())) {
            try {
                String yearStr = dto.getApplicableYear().substring(0, 4);
                t.setApplicableYear(Integer.parseInt(yearStr));
            } catch (Exception e) {
                log.warn("解析 applicableYear 失败：{}", dto.getApplicableYear());
            }
        }

        // 处理 templateFields（序列化为 JSON 存入 templateConfig，格式统一为 { fields: [...] }）
        if (dto.getTemplateFields() != null && !dto.getTemplateFields().isEmpty()) {
            Map<String, Object> configMap = new HashMap<>();
            configMap.put("fields", dto.getTemplateFields());
            t.setTemplateConfig(JSON.toJSONString(configMap));
            t.setFieldCount(dto.getTemplateFields().size());
        } else if (StringUtils.hasText(dto.getTemplateConfig())) {
            // 前端直接传了 templateConfig 字符串时，原样保存
            t.setTemplateConfig(dto.getTemplateConfig());
        }

        t.setUpdateTime(new Date());
        if (!StringUtils.hasText(dto.getTemplateId())) {
            // 新建：自动生成 templateId 和 templateCode
            String newId = "T" + System.currentTimeMillis();
            t.setTemplateId(newId);
            if (!StringUtils.hasText(dto.getTemplateCode())) {
                t.setTemplateCode("USER_" + newId);
            }
            t.setDelFlag(0);
            t.setCreateTime(new Date());
            return save(t);
        }
        return updateById(t);
    }

    @Override
    public boolean applyTemplate(String templateId, String targetId) {
        BudgetTemplate t = getById(templateId);
        if (t == null) return false;
        BudgetTemplate upd = new BudgetTemplate();
        upd.setTemplateId(templateId);
        upd.setUsageCount(t.getUsageCount() == null ? 1 : t.getUsageCount() + 1);
        upd.setUpdateTime(new Date());
        return updateById(upd);
    }

    @Override
    public List<BudgetTemplate> listByType(String templateType) {
        return getByTemplateType(templateType);
    }

    @Override
    public Map<String, Object> getTemplateDetail(String templateId) {
        Map<String, Object> result = new HashMap<>();
        BudgetTemplate t = getById(templateId);
        result.put("success", t != null);
        result.put("data", t);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleStatus(String templateId, Integer status) {
        BudgetTemplate t = new BudgetTemplate();
        t.setTemplateId(templateId);
        t.setIsEnabled(status != null && status == 1 ? 1 : 0);
        t.setUpdateTime(new Date());
        return updateById(t);
    }

    @Override
    public List<BudgetTemplate> listSystemTemplates() {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("IS_SYSTEM", 1).eq("DEL_FLAG", 0).orderByAsc("SORT_ORDER");
        return baseMapper.selectList(w);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTemplate(String templateId) {
        BudgetTemplate t = new BudgetTemplate();
        t.setTemplateId(templateId);
        t.setDelFlag(1);
        t.setUpdateTime(new Date());
        updateById(t);
    }

    @Override
    public Object listTemplates(Map<String, Object> params) {
        log.info("分页查询模板，参数：{}", params);

        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
        BudgetTemplate condition = new BudgetTemplate();

        if (params.get("templateName") != null) {
            condition.setTemplateName(params.get("templateName").toString());
            log.info("查询条件 - 模板名称：{}", params.get("templateName"));
        }
        if (params.get("templateType") != null) {
            condition.setTemplateType(params.get("templateType").toString());
            log.info("查询条件 - 模板类型：{}", params.get("templateType"));
        }
        if (params.get("templateStatus") != null) {
            condition.setTemplateStatus(params.get("templateStatus").toString());
            log.info("查询条件 - 模板状态：{}", params.get("templateStatus"));
        }
        if (params.get("categoryId") != null) {
            condition.setCategoryId(params.get("categoryId").toString());
            log.info("查询条件 - 分类ID：{}", params.get("categoryId"));
        }

        Page<BudgetTemplate> result = pageQuery(pageNum, pageSize, condition);
        log.info("查询结果，总记录数：{}，当前页记录数：{}", result.getTotal(), result.getRecords().size());

        return result;
    }
}

