package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.QueryTemplate;
import com.huabo.cybermonitor.mapper.QueryTemplateMapper;
import com.huabo.cybermonitor.service.IQueryTemplateService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.QueryTemplateQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 查询模板服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class QueryTemplateServiceImpl extends ServiceImpl<QueryTemplateMapper, QueryTemplate> implements IQueryTemplateService {

    @Autowired
    private QueryTemplateMapper templateMapper;

    @Override
    public IPage<QueryTemplate> getTemplateList(QueryTemplateQueryVO queryVO) {
        Page<QueryTemplate> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return templateMapper.selectTemplateList(page, queryVO);
    }

    @Override
    public QueryTemplate getTemplateDetail(String templateId) {
        if (StringUtils.isEmpty(templateId)) {
            return null;
        }
        return templateMapper.selectById(templateId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addTemplate(QueryTemplate template) {
        try {
            // 验证模板名称唯一性
            if (validateTemplateName(template.getTemplateName(), null)) {
                throw new RuntimeException("模板名称已存在");
            }

            // 设置默认值
            if (template.getEnabled() == null) {
                template.setEnabled(true);
            }
            if (template.getIsPublic() == null) {
                template.setIsPublic(false);
            }
            template.setCreateTime(LocalDateTime.now());
            template.setUpdateTime(LocalDateTime.now());

            return save(template);
        } catch (Exception e) {
            log.error("新增查询模板失败", e);
            throw new RuntimeException("新增查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplate(QueryTemplate template) {
        try {
            // 验证模板名称唯一性
            if (validateTemplateName(template.getTemplateName(), template.getTemplateId())) {
                throw new RuntimeException("模板名称已存在");
            }

            template.setUpdateTime(LocalDateTime.now());
            return updateById(template);
        } catch (Exception e) {
            log.error("更新查询模板失败", e);
            throw new RuntimeException("更新查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTemplate(String templateId) {
        try {
            return removeById(templateId);
        } catch (Exception e) {
            log.error("删除查询模板失败", e);
            throw new RuntimeException("删除查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTemplate(List<String> templateIds) {
        try {
            return removeByIds(templateIds);
        } catch (Exception e) {
            log.error("批量删除查询模板失败", e);
            throw new RuntimeException("批量删除查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableTemplate(String templateId) {
        try {
            return templateMapper.updateTemplateStatus(templateId, true) > 0;
        } catch (Exception e) {
            log.error("启用查询模板失败", e);
            throw new RuntimeException("启用查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableTemplate(String templateId) {
        try {
            return templateMapper.updateTemplateStatus(templateId, false) > 0;
        } catch (Exception e) {
            log.error("禁用查询模板失败", e);
            throw new RuntimeException("禁用查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchEnableTemplate(List<String> templateIds) {
        try {
            return templateMapper.batchUpdateTemplateStatus(templateIds, true) > 0;
        } catch (Exception e) {
            log.error("批量启用查询模板失败", e);
            throw new RuntimeException("批量启用查询模板失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDisableTemplate(List<String> templateIds) {
        try {
            return templateMapper.batchUpdateTemplateStatus(templateIds, false) > 0;
        } catch (Exception e) {
            log.error("批量禁用查询模板失败", e);
            throw new RuntimeException("批量禁用查询模板失败：" + e.getMessage());
        }
    }

    @Override
    public List<QueryTemplate> getTemplatesByType(String templateType) {
        return templateMapper.selectTemplatesByType(templateType);
    }

    @Override
    public List<QueryTemplate> getTemplatesByCreateBy(String createBy) {
        return templateMapper.selectTemplatesByCreateBy(createBy);
    }

    @Override
    public List<QueryTemplate> getPublicTemplates() {
        return templateMapper.selectPublicTemplates();
    }

    @Override
    public List<QueryTemplate> getEnabledTemplates() {
        return templateMapper.selectEnabledTemplates();
    }

    @Override
    public boolean validateTemplateName(String templateName, String excludeId) {
        if (StringUtils.isEmpty(templateName)) {
            return false;
        }
        QueryTemplate existingTemplate = templateMapper.selectByTemplateName(templateName, excludeId);
        return existingTemplate != null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyTemplate(String templateId, String newName) {
        try {
            QueryTemplate sourceTemplate = getById(templateId);
            if (sourceTemplate == null) {
                throw new RuntimeException("源模板不存在");
            }

            // 验证新名称唯一性
            if (validateTemplateName(newName, null)) {
                throw new RuntimeException("模板名称已存在");
            }

            QueryTemplate newTemplate = new QueryTemplate();
            newTemplate.setTemplateName(newName);
            newTemplate.setTemplateType(sourceTemplate.getTemplateType());
            newTemplate.setQuerySql(sourceTemplate.getQuerySql());
            newTemplate.setQueryParams(sourceTemplate.getQueryParams());
            newTemplate.setResultFields(sourceTemplate.getResultFields());
            newTemplate.setTemplateDescription(sourceTemplate.getTemplateDescription() + "（复制）");
            newTemplate.setEnabled(true);
            newTemplate.setIsPublic(false);
            newTemplate.setCreateBy(sourceTemplate.getCreateBy());
            newTemplate.setCreateTime(LocalDateTime.now());
            newTemplate.setUpdateTime(LocalDateTime.now());

            return save(newTemplate);
        } catch (Exception e) {
            log.error("复制查询模板失败", e);
            throw new RuntimeException("复制查询模板失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> executeTemplate(String templateId, Map<String, Object> params) {
        try {
            QueryTemplate template = getById(templateId);
            if (template == null || !template.getEnabled()) {
                throw new RuntimeException("模板不存在或已禁用");
            }

            // 简化实现，返回模拟数据
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, Object> row = new HashMap<>();
            row.put("message", "查询模板执行功能开发中");
            row.put("templateId", templateId);
            row.put("templateName", template.getTemplateName());
            result.add(row);

            return result;
        } catch (Exception e) {
            log.error("执行查询模板失败", e);
            throw new RuntimeException("执行查询模板失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> validateTemplateSql(String querySql) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();

        if (StringUtils.isEmpty(querySql)) {
            errors.add("查询SQL不能为空");
        } else {
            // 简化SQL验证
            String sql = querySql.trim().toUpperCase();
            if (!sql.startsWith("SELECT")) {
                errors.add("只支持SELECT查询语句");
            }
            if (sql.contains("DELETE") || sql.contains("UPDATE") || sql.contains("INSERT") || sql.contains("DROP")) {
                errors.add("不允许包含DML或DDL语句");
            }
        }

        result.put("isValid", errors.isEmpty());
        result.put("errors", errors);
        return result;
    }

    @Override
    public Map<String, Object> getTemplateStatistics() {
        return templateMapper.selectTemplateStatistics();
    }

    @Override
    public List<Map<String, Object>> getTemplateTypeDistribution() {
        return templateMapper.selectTemplateTypeDistribution();
    }

    @Override
    public List<Map<String, Object>> getTemplateUsageStatistics() {
        return templateMapper.selectTemplateUsageStatistics();
    }

    @Override
    public void exportTemplateList(QueryTemplateQueryVO queryVO, HttpServletResponse response) {
        try {
            List<QueryTemplate> templateList = templateMapper.selectTemplateListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "模板名称", "模板类型", "是否启用", "是否公开", "模板描述", "创建人", "创建时间", "更新时间"
            };

            ExcelUtil excelUtil = new ExcelUtil("查询模板列表", headers);

            // 添加数据行
            for (int i = 0; i < templateList.size(); i++) {
                QueryTemplate template = templateList.get(i);
                Object[] row = {
                    template.getTemplateName(),
                    getTemplateTypeLabel(template.getTemplateType()),
                    template.getEnabled() ? "是" : "否",
                    template.getIsPublic() ? "是" : "否",
                    template.getTemplateDescription(),
                    template.getCreateBy(),
                    template.getCreateTime(),
                    template.getUpdateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "查询模板列表.xls");
        } catch (Exception e) {
            log.error("导出查询模板列表失败", e);
            throw new RuntimeException("导出查询模板列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadTemplateTemplate(HttpServletResponse response) {
        try {
            // 设置导入模板的列标题
            String[] headers = {
                "模板名称", "模板类型", "查询SQL", "查询参数配置", "结果字段配置", "模板描述", "是否启用", "是否公开"
            };

            ExcelUtil excelUtil = new ExcelUtil("查询模板导入模板", headers);

            // 添加示例数据
            Object[] row = {
                "示例查询模板",
                "ENTERPRISE_QUERY",
                "SELECT * FROM SYS_ENTERPRISE_INFO WHERE ENTERPRISE_NAME LIKE '%{enterpriseName}%'",
                "{\"enterpriseName\":{\"type\":\"string\",\"required\":true,\"description\":\"企业名称\"}}",
                "{\"enterpriseId\":\"企业ID\",\"enterpriseName\":\"企业名称\",\"creditCode\":\"信用代码\"}",
                "这是一个示例查询模板",
                "是",
                "否"
            };
            excelUtil.addRow(1, row);

            excelUtil.exportExcel(response, "查询模板导入模板.xls");
        } catch (Exception e) {
            log.error("下载查询模板导入模板失败", e);
            throw new RuntimeException("下载查询模板导入模板失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> importTemplateList(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化实现，返回模拟结果
            result.put("success", true);
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failureCount", 0);
            result.put("message", "查询模板导入功能开发中");
        } catch (Exception e) {
            log.error("导入查询模板失败", e);
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public String getTemplateTypeLabel(String templateType) {
        if (StringUtils.isEmpty(templateType)) {
            return "";
        }
        switch (templateType) {
            case QueryTemplate.TYPE_ENTERPRISE_QUERY:
                return "企业查询";
            case QueryTemplate.TYPE_FINANCIAL_QUERY:
                return "财务查询";
            case QueryTemplate.TYPE_RISK_QUERY:
                return "风险查询";
            case QueryTemplate.TYPE_ASSET_QUERY:
                return "资产查询";
            case QueryTemplate.TYPE_CUSTOM_QUERY:
                return "自定义查询";
            case QueryTemplate.TYPE_STATISTICAL_QUERY:
                return "统计查询";
            default:
                return templateType;
        }
    }
}
