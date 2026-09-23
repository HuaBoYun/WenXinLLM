package com.financial.sharing.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.dto.FieldMappingBatchParam;
import com.financial.sharing.dto.FieldMappingImportParam;
import com.financial.sharing.dto.FieldMappingParam;
import com.financial.sharing.dto.FieldMappingSaveParam;
import com.financial.sharing.oracle.entity.FieldMappingEntity;
import com.financial.sharing.oracle.mapper.FieldMappingMapper;
import com.financial.sharing.service.FieldMappingService;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 字段映射服务实现
 */
@Slf4j
@Service
public class FieldMappingServiceImpl implements FieldMappingService {

    @Resource
    private FieldMappingMapper fieldMappingMapper;

    @Override
    public JsonBean getFieldMappingList(FieldMappingParam param) {
        try {
            Page<FieldMappingEntity> page = new Page<>(param.getPageNo(), param.getPageSize());
            Map<String, Object> paramMap = new HashMap<>();
            if (param.getMappingId() != null) {
                paramMap.put("mappingId", param.getMappingId());
            }
            if (param.getDataSourceId() != null) {
                paramMap.put("dataSourceId", param.getDataSourceId());
            }
            if (param.getTargetTable() != null && !param.getTargetTable().isEmpty()) {
                paramMap.put("targetTable", param.getTargetTable());
            }
            if (param.getSourceField() != null && !param.getSourceField().isEmpty()) {
                paramMap.put("sourceField", param.getSourceField());
            }
            if (param.getTargetField() != null && !param.getTargetField().isEmpty()) {
                paramMap.put("targetField", param.getTargetField());
            }
            if (param.getIsEnabled() != null) {
                paramMap.put("isEnabled", param.getIsEnabled());
            }

            IPage<FieldMappingEntity> result = fieldMappingMapper.selectFieldMappingPage(page, paramMap);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            JsonBean jsonBean = new JsonBean(1, "查询成功", data);
            return jsonBean;
        } catch (Exception e) {
            log.error("查询字段映射列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    public JsonBean getFieldMapping(Long mappingId, Long tenantId) {
        try {
            FieldMappingEntity entity = fieldMappingMapper.selectById(mappingId);
            if (entity == null || !tenantId.equals(entity.getTenantId())) {
                return new JsonBean(0, "字段映射不存在", null);
            }
            return new JsonBean(1, "查询成功", entity);
        } catch (Exception e) {
            log.error("获取字段映射详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public Long saveFieldMapping(FieldMappingSaveParam param) {
        try {
            // 检查配置名称是否重复
            QueryWrapper<FieldMappingEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("DATA_SOURCE_ID", param.getDataSourceId())
                    .eq("TARGET_TABLE", param.getTargetTable())
                    .eq("SOURCE_FIELD", param.getSourceField())
                    .eq("TARGET_FIELD", param.getTargetField())
                    .eq("TENANT_ID", param.getTenantId());

            FieldMappingEntity existEntity = fieldMappingMapper.selectOne(wrapper);
            if (existEntity != null) {
                throw new RuntimeException("字段映射已存在");
            }

            // 获取最大排序号
            Integer maxSortOrder = fieldMappingMapper.selectMaxSortOrder(param.getDataSourceId(), param.getTenantId());
            if (param.getSortOrder() == null) {
                param.setSortOrder(maxSortOrder != null ? maxSortOrder + 1 : 1);
            }

            FieldMappingEntity entity = new FieldMappingEntity();
            BeanUtils.copyProperties(param, entity);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            fieldMappingMapper.insert(entity);
            return entity.getMappingId();
        } catch (Exception e) {
            log.error("保存字段映射失败", e);
            throw new RuntimeException("保存失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateFieldMapping(Long mappingId, FieldMappingSaveParam param) {
        try {
            FieldMappingEntity entity = fieldMappingMapper.selectById(mappingId);
            if (entity == null || !param.getTenantId().equals(entity.getTenantId())) {
                throw new RuntimeException("字段映射不存在");
            }

            // 检查是否与其他映射重复
            QueryWrapper<FieldMappingEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("DATA_SOURCE_ID", param.getDataSourceId())
                    .eq("TARGET_TABLE", param.getTargetTable())
                    .eq("SOURCE_FIELD", param.getSourceField())
                    .eq("TARGET_FIELD", param.getTargetField())
                    .eq("TENANT_ID", param.getTenantId())
                    .ne("MAPPING_ID", mappingId);

            FieldMappingEntity existEntity = fieldMappingMapper.selectOne(wrapper);
            if (existEntity != null) {
                throw new RuntimeException("字段映射已存在");
            }

            BeanUtils.copyProperties(param, entity);
            entity.setMappingId(mappingId);
            entity.setUpdateTime(LocalDateTime.now());

            fieldMappingMapper.updateById(entity);
        } catch (Exception e) {
            log.error("更新字段映射失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void deleteFieldMapping(Long mappingId, Long tenantId) {
        try {
            FieldMappingEntity entity = fieldMappingMapper.selectById(mappingId);
            if (entity == null || !tenantId.equals(entity.getTenantId())) {
                throw new RuntimeException("字段映射不存在");
            }
            fieldMappingMapper.deleteById(mappingId);
        } catch (Exception e) {
            log.error("删除字段映射失败", e);
            throw new RuntimeException("删除失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getFieldMappingByDataSourceId(Long dataSourceId, Long tenantId) {
        try {
            List<FieldMappingEntity> list = fieldMappingMapper.selectByDataSourceId(dataSourceId, tenantId);
            return new JsonBean(1, list);
        } catch (Exception e) {
            log.error("根据数据源ID查询字段映射失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public JsonBean batchOperationFieldMapping(FieldMappingBatchParam param) {
        try {
            String operationType = param.getOperationType().toUpperCase();
            List<Long> mappingIds = param.getMappingIds();

            switch (operationType) {
                case "ENABLE":
                case "DISABLE":
                    fieldMappingMapper.batchUpdateStatus(mappingIds, "ENABLE".equals(operationType), param.getUpdateUser());
                    return new JsonBean(1, "操作成功", null);
                case "DELETE":
                    fieldMappingMapper.batchDelete(mappingIds, param.getTenantId());
                    return new JsonBean(1, "删除成功", null);
                default:
                    return new JsonBean(0, "不支持的操作类型", null);
            }
        } catch (Exception e) {
            log.error("批量操作字段映射失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null);
        }
    }

    @Override
    @Transactional
    public JsonBean importFieldMapping(FieldMappingImportParam param) {
        try {
            // 如果是覆盖模式，先删除现有的映射
            if (param.getOverwrite()) {
                fieldMappingMapper.deleteByDataSourceId(param.getDataSourceId(), param.getTenantId());
            }

            // 解析导入数据
            List<FieldMappingEntity> mappings = parseImportData(param);

            // 批量保存
            for (FieldMappingEntity mapping : mappings) {
                mapping.setTenantId(param.getTenantId());
                mapping.setCreateUser(param.getCreateUser());
                mapping.setUpdateUser(param.getCreateUser());
                mapping.setCreateTime(LocalDateTime.now());
                mapping.setUpdateTime(LocalDateTime.now());
            }
            if (!mappings.isEmpty()) {
                fieldMappingMapper.batchInsert(mappings);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("importCount", mappings.size());
            return new JsonBean(1, result);
        } catch (Exception e) {
            log.error("导入字段映射失败", e);
            return new JsonBean(0, "导入失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean exportFieldMapping(Long dataSourceId, Long tenantId) {
        try {
            List<FieldMappingEntity> list = fieldMappingMapper.selectByDataSourceId(dataSourceId, tenantId);

            Map<String, Object> result = new HashMap<>();
            result.put("mappings", list);
            result.put("count", list.size());

            return new JsonBean(1, result);
        } catch (Exception e) {
            log.error("导出字段映射失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void copyFieldMapping(Long sourceDataSourceId, Long targetDataSourceId, Long tenantId, Long createUser) {
        try {
            fieldMappingMapper.copyFieldMappings(sourceDataSourceId, targetDataSourceId, tenantId, createUser);
        } catch (Exception e) {
            log.error("复制字段映射失败", e);
            throw new RuntimeException("复制失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void updateSortOrder(Long mappingId, Integer sortOrder, Long updateUser) {
        try {
            fieldMappingMapper.updateSortOrder(mappingId, sortOrder, updateUser);
        } catch (Exception e) {
            log.error("更新排序失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    /**
     * 解析导入数据
     */
    private List<FieldMappingEntity> parseImportData(FieldMappingImportParam param) {
        List<FieldMappingEntity> mappings = new ArrayList<>();

        // 这里需要根据实际的导入数据格式进行解析
        // 示例代码，需要根据实际需求调整
        if (param.getImportData() instanceof List) {
            List<?> dataList = (List<?>) param.getImportData();
            for (Object item : dataList) {
                if (item instanceof Map) {
                    Map<?, ?> dataMap = (Map<?, ?>) item;
                    FieldMappingEntity entity = new FieldMappingEntity();
                    entity.setDataSourceId(param.getDataSourceId());

                    if (param.getTargetTable() != null) {
                        entity.setTargetTable(param.getTargetTable());
                    }

                    // 设置其他字段
                    if (dataMap.containsKey("sourceField")) {
                        entity.setSourceField(String.valueOf(dataMap.get("sourceField")));
                    }
                    if (dataMap.containsKey("targetField")) {
                        entity.setTargetField(String.valueOf(dataMap.get("targetField")));
                    }
                    if (dataMap.containsKey("fieldType")) {
                        entity.setFieldType(String.valueOf(dataMap.get("fieldType")));
                    }
                    if (dataMap.containsKey("fieldLength")) {
                        entity.setFieldLength((Integer) dataMap.get("fieldLength"));
                    }
                    if (dataMap.containsKey("isNullable")) {
                        entity.setIsNullable((Boolean) dataMap.get("isNullable"));
                    }
                    if (dataMap.containsKey("defaultValue")) {
                        entity.setDefaultValue(String.valueOf(dataMap.get("defaultValue")));
                    }
                    if (dataMap.containsKey("transformationRule")) {
                        entity.setTransformationRule(String.valueOf(dataMap.get("transformationRule")));
                    }
                    if (dataMap.containsKey("validationRule")) {
                        entity.setValidationRule(String.valueOf(dataMap.get("validationRule")));
                    }
                    if (dataMap.containsKey("sortOrder")) {
                        entity.setSortOrder((Integer) dataMap.get("sortOrder"));
                    }
                    if (dataMap.containsKey("isEnabled")) {
                        entity.setIsEnabled((Boolean) dataMap.get("isEnabled"));
                    }
                    if (dataMap.containsKey("description")) {
                        entity.setDescription(String.valueOf(dataMap.get("description")));
                    }

                    mappings.add(entity);
                }
            }
        }
        return mappings;
    }
}