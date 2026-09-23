package com.huabo.cybermonitor.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.SystemParameter;
import com.huabo.cybermonitor.mapper.SystemParameterMapper;
import com.huabo.cybermonitor.service.ISystemParameterService;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.vo.SystemParameterQueryVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 系统参数配置服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class SystemParameterServiceImpl extends ServiceImpl<SystemParameterMapper, SystemParameter> implements ISystemParameterService {

    @Autowired
    private SystemParameterMapper parameterMapper;

    // 参数缓存
    private final Map<String, String> parameterCache = new ConcurrentHashMap<>();

    @Override
    public IPage<SystemParameter> getParameterList(SystemParameterQueryVO queryVO) {
        Page<SystemParameter> page = new Page<>(queryVO.getPageNumber(), queryVO.getPageSize());
        return parameterMapper.selectParameterList(page, queryVO);
    }

    @Override
    public SystemParameter getParameterDetail(String parameterId) {
        if (StringUtils.isEmpty(parameterId)) {
            return null;
        }
        return parameterMapper.selectById(parameterId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addParameter(SystemParameter parameter) {
        try {
            // 验证参数键名唯一性
            if (validateParameterKey(parameter.getParameterKey(), null)) {
                throw new RuntimeException("参数键名已存在");
            }

            // 设置默认值
            if (parameter.getEnabled() == null) {
                parameter.setEnabled(true);
            }
            if (parameter.getIsSystem() == null) {
                parameter.setIsSystem(false);
            }
            parameter.setCreateTime(LocalDateTime.now());
            parameter.setUpdateTime(LocalDateTime.now());

            boolean success = save(parameter);
            if (success) {
                // 更新缓存
                parameterCache.put(parameter.getParameterKey(), parameter.getParameterValue());
            }
            return success;
        } catch (Exception e) {
            log.error("新增系统参数失败", e);
            throw new RuntimeException("新增系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameter(SystemParameter parameter) {
        try {
            // 验证参数键名唯一性
            if (validateParameterKey(parameter.getParameterKey(), parameter.getParameterId())) {
                throw new RuntimeException("参数键名已存在");
            }

            parameter.setUpdateTime(LocalDateTime.now());
            boolean success = updateById(parameter);
            if (success) {
                // 更新缓存
                parameterCache.put(parameter.getParameterKey(), parameter.getParameterValue());
            }
            return success;
        } catch (Exception e) {
            log.error("更新系统参数失败", e);
            throw new RuntimeException("更新系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteParameter(String parameterId) {
        try {
            SystemParameter parameter = getById(parameterId);
            if (parameter != null && parameter.getIsSystem()) {
                throw new RuntimeException("系统参数不允许删除");
            }

            boolean success = removeById(parameterId);
            if (success && parameter != null) {
                // 清除缓存
                parameterCache.remove(parameter.getParameterKey());
            }
            return success;
        } catch (Exception e) {
            log.error("删除系统参数失败", e);
            throw new RuntimeException("删除系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteParameter(List<String> parameterIds) {
        try {
            // 检查是否包含系统参数
            List<SystemParameter> parameters = listByIds(parameterIds);
            for (SystemParameter parameter : parameters) {
                if (parameter.getIsSystem()) {
                    throw new RuntimeException("系统参数不允许删除");
                }
            }

            boolean success = removeByIds(parameterIds);
            if (success) {
                // 清除缓存
                for (SystemParameter parameter : parameters) {
                    parameterCache.remove(parameter.getParameterKey());
                }
            }
            return success;
        } catch (Exception e) {
            log.error("批量删除系统参数失败", e);
            throw new RuntimeException("批量删除系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean enableParameter(String parameterId) {
        try {
            return parameterMapper.updateParameterStatus(parameterId, true) > 0;
        } catch (Exception e) {
            log.error("启用系统参数失败", e);
            throw new RuntimeException("启用系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean disableParameter(String parameterId) {
        try {
            return parameterMapper.updateParameterStatus(parameterId, false) > 0;
        } catch (Exception e) {
            log.error("禁用系统参数失败", e);
            throw new RuntimeException("禁用系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchEnableParameter(List<String> parameterIds) {
        try {
            return parameterMapper.batchUpdateParameterStatus(parameterIds, true) > 0;
        } catch (Exception e) {
            log.error("批量启用系统参数失败", e);
            throw new RuntimeException("批量启用系统参数失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDisableParameter(List<String> parameterIds) {
        try {
            return parameterMapper.batchUpdateParameterStatus(parameterIds, false) > 0;
        } catch (Exception e) {
            log.error("批量禁用系统参数失败", e);
            throw new RuntimeException("批量禁用系统参数失败：" + e.getMessage());
        }
    }

    @Override
    public List<SystemParameter> getParametersByGroup(String parameterGroup) {
        return parameterMapper.selectParametersByGroup(parameterGroup);
    }

    @Override
    public List<SystemParameter> getParametersByType(String parameterType) {
        return parameterMapper.selectParametersByType(parameterType);
    }

    @Override
    public List<SystemParameter> getEnabledParameters() {
        return parameterMapper.selectEnabledParameters();
    }

    @Override
    public List<SystemParameter> getSystemParameters() {
        return parameterMapper.selectSystemParameters();
    }

    @Override
    public boolean validateParameterKey(String parameterKey, String excludeId) {
        if (StringUtils.isEmpty(parameterKey)) {
            return false;
        }
        SystemParameter existingParameter = parameterMapper.selectByParameterKey(parameterKey, excludeId);
        return existingParameter != null;
    }

    @Override
    public String getParameterValue(String parameterKey) {
        return getParameterValue(parameterKey, null);
    }

    @Override
    public String getParameterValue(String parameterKey, String defaultValue) {
        if (StringUtils.isEmpty(parameterKey)) {
            return defaultValue;
        }

        // 先从缓存获取
        String value = parameterCache.get(parameterKey);
        if (value != null) {
            return value;
        }

        // 从数据库获取
        value = parameterMapper.selectParameterValue(parameterKey);
        if (value != null) {
            // 更新缓存
            parameterCache.put(parameterKey, value);
            return value;
        }

        return defaultValue;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameterValue(String parameterKey, String parameterValue) {
        try {
            boolean success = parameterMapper.updateParameterValue(parameterKey, parameterValue) > 0;
            if (success) {
                // 更新缓存
                parameterCache.put(parameterKey, parameterValue);
            }
            return success;
        } catch (Exception e) {
            log.error("更新参数值失败", e);
            throw new RuntimeException("更新参数值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateParameterValues(Map<String, String> parameters) {
        try {
            boolean success = parameterMapper.batchUpdateParameterValues(parameters) > 0;
            if (success) {
                // 更新缓存
                parameterCache.putAll(parameters);
            }
            return success;
        } catch (Exception e) {
            log.error("批量更新参数值失败", e);
            throw new RuntimeException("批量更新参数值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resetParameterToDefault(String parameterId) {
        try {
            SystemParameter parameter = getById(parameterId);
            if (parameter == null) {
                throw new RuntimeException("参数不存在");
            }

            boolean success = parameterMapper.resetParameterToDefault(parameterId) > 0;
            if (success) {
                // 更新缓存
                parameterCache.put(parameter.getParameterKey(), parameter.getDefaultValue());
            }
            return success;
        } catch (Exception e) {
            log.error("重置参数为默认值失败", e);
            throw new RuntimeException("重置参数为默认值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchResetParametersToDefault(List<String> parameterIds) {
        try {
            List<SystemParameter> parameters = listByIds(parameterIds);
            boolean success = parameterMapper.batchResetParametersToDefault(parameterIds) > 0;
            if (success) {
                // 更新缓存
                for (SystemParameter parameter : parameters) {
                    parameterCache.put(parameter.getParameterKey(), parameter.getDefaultValue());
                }
            }
            return success;
        } catch (Exception e) {
            log.error("批量重置参数为默认值失败", e);
            throw new RuntimeException("批量重置参数为默认值失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateParameterSortOrder(String parameterId, Integer sortOrder) {
        try {
            return parameterMapper.updateParameterSortOrder(parameterId, sortOrder) > 0;
        } catch (Exception e) {
            log.error("更新参数排序失败", e);
            throw new RuntimeException("更新参数排序失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateParameterSortOrder(Map<String, Integer> sortOrders) {
        try {
            return parameterMapper.batchUpdateParameterSortOrder(sortOrders) > 0;
        } catch (Exception e) {
            log.error("批量更新参数排序失败", e);
            throw new RuntimeException("批量更新参数排序失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> validateParameterValue(SystemParameter parameter) {
        Map<String, Object> result = new HashMap<>();
        boolean isValid = true;
        String errorMessage = "";

        try {
            String value = parameter.getParameterValue();
            String type = parameter.getParameterType();
            String validationRule = parameter.getValidationRule();

            if (StringUtils.isEmpty(value)) {
                isValid = false;
                errorMessage = "参数值不能为空";
            } else {
                // 根据参数类型验证
                switch (type) {
                    case SystemParameter.TYPE_NUMBER:
                        try {
                            Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            isValid = false;
                            errorMessage = "参数值必须是数字";
                        }
                        break;
                    case SystemParameter.TYPE_BOOLEAN:
                        if (!"true".equalsIgnoreCase(value) && !"false".equalsIgnoreCase(value)) {
                            isValid = false;
                            errorMessage = "参数值必须是true或false";
                        }
                        break;
                    case SystemParameter.TYPE_EMAIL:
                        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                            isValid = false;
                            errorMessage = "参数值必须是有效的邮箱地址";
                        }
                        break;
                    case SystemParameter.TYPE_URL:
                        if (!value.matches("^(http|https)://.*$")) {
                            isValid = false;
                            errorMessage = "参数值必须是有效的URL地址";
                        }
                        break;
                }

                // 验证规则检查（简化实现）
                if (isValid && !StringUtils.isEmpty(validationRule)) {
                    // 这里可以实现更复杂的验证规则
                    log.debug("验证规则: {}", validationRule);
                }
            }
        } catch (Exception e) {
            isValid = false;
            errorMessage = "参数值验证失败：" + e.getMessage();
        }

        result.put("isValid", isValid);
        result.put("errorMessage", errorMessage);
        return result;
    }

    @Override
    public Map<String, Object> getParameterStatistics() {
        return parameterMapper.selectParameterStatistics();
    }

    @Override
    public List<Map<String, Object>> getParameterGroupDistribution() {
        return parameterMapper.selectParameterGroupDistribution();
    }

    @Override
    public List<Map<String, Object>> getParameterTypeDistribution() {
        return parameterMapper.selectParameterTypeDistribution();
    }

    @Override
    public void exportParameterList(SystemParameterQueryVO queryVO, HttpServletResponse response) {
        try {
            List<SystemParameter> parameterList = parameterMapper.selectParameterListForExport(queryVO);

            // 设置导出的列标题
            String[] headers = {
                "参数键名", "参数名称", "参数值", "参数类型", "参数分组", "是否系统参数", 
                "是否启用", "参数描述", "创建人", "创建时间", "更新时间"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统参数列表", headers);

            // 添加数据行
            for (int i = 0; i < parameterList.size(); i++) {
                SystemParameter parameter = parameterList.get(i);
                Object[] row = {
                    parameter.getParameterKey(),
                    parameter.getParameterName(),
                    parameter.getParameterValue(),
                    getParameterTypeLabel(parameter.getParameterType()),
                    getParameterGroupLabel(parameter.getParameterGroup()),
                    parameter.getIsSystem() ? "是" : "否",
                    parameter.getEnabled() ? "是" : "否",
                    parameter.getParameterDescription(),
                    parameter.getCreateBy(),
                    parameter.getCreateTime(),
                    parameter.getUpdateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "系统参数列表.xls");
        } catch (Exception e) {
            log.error("导出系统参数列表失败", e);
            throw new RuntimeException("导出系统参数列表失败：" + e.getMessage());
        }
    }

    @Override
    public void downloadParameterTemplate(HttpServletResponse response) {
        try {
            // 设置导入模板的列标题
            String[] headers = {
                "参数键名", "参数名称", "参数值", "参数类型", "参数分组", "参数描述", 
                "是否启用", "默认值", "值范围/选项", "验证规则"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统参数导入模板", headers);

            // 添加示例数据
            Object[] row = {
                "SYSTEM_NAME",
                "系统名称",
                "国资国企穿透式监管系统",
                "STRING",
                "SYSTEM",
                "系统显示名称",
                "是",
                "国资国企穿透式监管系统",
                "",
                ""
            };
            excelUtil.addRow(1, row);

            excelUtil.exportExcel(response, "系统参数导入模板.xls");
        } catch (Exception e) {
            log.error("下载系统参数导入模板失败", e);
            throw new RuntimeException("下载系统参数导入模板失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> importParameterList(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化实现，返回模拟结果
            result.put("success", true);
            result.put("totalCount", 0);
            result.put("successCount", 0);
            result.put("failureCount", 0);
            result.put("message", "系统参数导入功能开发中");
        } catch (Exception e) {
            log.error("导入系统参数失败", e);
            result.put("success", false);
            result.put("message", "导入失败：" + e.getMessage());
        }
        return result;
    }

    @Override
    public String getParameterTypeLabel(String parameterType) {
        if (StringUtils.isEmpty(parameterType)) {
            return "";
        }
        switch (parameterType) {
            case SystemParameter.TYPE_STRING:
                return "字符串";
            case SystemParameter.TYPE_NUMBER:
                return "数字";
            case SystemParameter.TYPE_BOOLEAN:
                return "布尔值";
            case SystemParameter.TYPE_DATE:
                return "日期";
            case SystemParameter.TYPE_JSON:
                return "JSON";
            case SystemParameter.TYPE_PASSWORD:
                return "密码";
            case SystemParameter.TYPE_EMAIL:
                return "邮箱";
            case SystemParameter.TYPE_URL:
                return "URL";
            case SystemParameter.TYPE_FILE_PATH:
                return "文件路径";
            case SystemParameter.TYPE_SELECT:
                return "选择";
            default:
                return parameterType;
        }
    }

    @Override
    public String getParameterGroupLabel(String parameterGroup) {
        if (StringUtils.isEmpty(parameterGroup)) {
            return "";
        }
        switch (parameterGroup) {
            case SystemParameter.GROUP_SYSTEM:
                return "系统配置";
            case SystemParameter.GROUP_DATABASE:
                return "数据库配置";
            case SystemParameter.GROUP_SECURITY:
                return "安全配置";
            case SystemParameter.GROUP_NOTIFICATION:
                return "通知配置";
            case SystemParameter.GROUP_MONITORING:
                return "监控配置";
            case SystemParameter.GROUP_REPORT:
                return "报表配置";
            case SystemParameter.GROUP_INTEGRATION:
                return "集成配置";
            case SystemParameter.GROUP_PERFORMANCE:
                return "性能配置";
            case SystemParameter.GROUP_BACKUP:
                return "备份配置";
            case SystemParameter.GROUP_CUSTOM:
                return "自定义配置";
            default:
                return parameterGroup;
        }
    }

    @Override
    public boolean refreshParameterCache() {
        try {
            parameterCache.clear();
            List<SystemParameter> parameters = getEnabledParameters();
            for (SystemParameter parameter : parameters) {
                parameterCache.put(parameter.getParameterKey(), parameter.getParameterValue());
            }
            log.info("参数缓存刷新成功，共加载{}个参数", parameters.size());
            return true;
        } catch (Exception e) {
            log.error("刷新参数缓存失败", e);
            return false;
        }
    }

    @Override
    public void backupParameterConfig(HttpServletResponse response) {
        try {
            List<SystemParameter> parameters = list();
            
            String[] headers = {
                "参数ID", "参数键名", "参数名称", "参数值", "参数类型", "参数分组", 
                "参数描述", "是否系统参数", "是否启用", "排序号", "默认值", 
                "值范围/选项", "验证规则", "创建人", "创建时间", "更新时间"
            };

            ExcelUtil excelUtil = new ExcelUtil("系统参数配置备份", headers);

            for (int i = 0; i < parameters.size(); i++) {
                SystemParameter parameter = parameters.get(i);
                Object[] row = {
                    parameter.getParameterId(),
                    parameter.getParameterKey(),
                    parameter.getParameterName(),
                    parameter.getParameterValue(),
                    parameter.getParameterType(),
                    parameter.getParameterGroup(),
                    parameter.getParameterDescription(),
                    parameter.getIsSystem(),
                    parameter.getEnabled(),
                    parameter.getSortOrder(),
                    parameter.getDefaultValue(),
                    parameter.getValueOptions(),
                    parameter.getValidationRule(),
                    parameter.getCreateBy(),
                    parameter.getCreateTime(),
                    parameter.getUpdateTime()
                };
                excelUtil.addRow(i + 1, row);
            }

            excelUtil.exportExcel(response, "系统参数配置备份_" + System.currentTimeMillis() + ".xls");
        } catch (Exception e) {
            log.error("备份系统参数配置失败", e);
            throw new RuntimeException("备份系统参数配置失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> restoreParameterConfig(MultipartFile file) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 简化实现，返回模拟结果
            result.put("success", true);
            result.put("message", "系统参数配置恢复功能开发中");
        } catch (Exception e) {
            log.error("恢复系统参数配置失败", e);
            result.put("success", false);
            result.put("message", "恢复失败：" + e.getMessage());
        }
        return result;
    }
}
