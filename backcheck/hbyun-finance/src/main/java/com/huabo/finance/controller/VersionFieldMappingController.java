package com.huabo.finance.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.VersionFieldMapping;
import com.huabo.finance.service.IVersionFieldMappingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 版本字段映射控制器
 *
 * @author 开发者
 * @date 2025-10-23
 */
@Slf4j
@RestController
@RequestMapping("/versionFieldMapping")
@Tag(name = "版本字段映射管理",description = "版本字段映射管理")
public class VersionFieldMappingController {

    @Autowired
    private IVersionFieldMappingService versionFieldMappingService;

    /**
     * 根据版本FID获取所有字段映射
     */
    @GetMapping("/getByVersionFid/{versionFid}")
    @Operation(summary = "根据版本FID获取字段映射,获取指定版本的所有字段映射配置")
    public String getByVersionFid(@PathVariable String versionFid) {
        try {
            List<VersionFieldMapping> mappings = versionFieldMappingService.getByVersionFid(versionFid);
            if (mappings == null || mappings.isEmpty()) {
                return JsonBean.success("暂无字段映射配置", null);
            }
            return JsonBean.success("获取成功", mappings);
        } catch (Exception e) {
            log.error("获取字段映射失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 根据版本FID和源表名获取字段映射
     */
    @GetMapping("/getBySourceTable/{versionFid}/{sourceTableName}")
    @Operation(summary = "根据源表名获取字段映射,获取指定版本和源表的字段映射")
    public String getBySourceTable(@PathVariable String versionFid, @PathVariable String sourceTableName) {
        try {
            List<VersionFieldMapping> mappings = versionFieldMappingService.getByVersionFidAndSourceTable(versionFid, sourceTableName);
            if (mappings == null || mappings.isEmpty()) {
                return JsonBean.success("暂无字段映射配置", null);
            }
            return JsonBean.success("获取成功", mappings);
        } catch (Exception e) {
            log.error("获取字段映射失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 根据版本FID和目标表名获取字段映射
     */
    @GetMapping("/getByTargetTable/{versionFid}/{targetTableName}")
    @Operation(summary = "根据目标表名获取字段映射,获取指定版本和目标表的字段映射")
    public String getByTargetTable(@PathVariable String versionFid, @PathVariable String targetTableName) {
        try {
            List<VersionFieldMapping> mappings = versionFieldMappingService.getByVersionFidAndTargetTable(versionFid, targetTableName);
            if (mappings == null || mappings.isEmpty()) {
                return JsonBean.success("暂无字段映射配置", null);
            }
            return JsonBean.success("获取成功", mappings);
        } catch (Exception e) {
            log.error("获取字段映射失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 保存字段映射
     */
    @PostMapping("/save")
    @Operation(summary = "保存字段映射,保存或更新字段映射配置")
    public String save(@RequestBody VersionFieldMapping mapping) {
        try {
            if (mapping.getMappingId() == null || mapping.getMappingId().isEmpty()) {
                mapping.setMappingId(UUID.randomUUID().toString());
            }
            versionFieldMappingService.save(mapping);
            return JsonBean.success("保存成功");
        } catch (Exception e) {
            log.error("保存字段映射失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 批量保存字段映射
     * 支持两种格式：
     * 1. JSON格式：Content-Type: application/json
     * 2. Form-urlencoded格式：Content-Type: application/x-www-form-urlencoded
     */
    @PostMapping("/saveBatch")
    @Operation(summary = "批量保存字段映射,批量保存字段映射配置")
    public String saveBatch(HttpServletRequest request) {
        try {
            String contentType = request.getContentType();
            List<VersionFieldMapping> mappingList = null;

            // 根据Content-Type判断请求格式
            if (contentType != null && contentType.contains("application/json")) {
                // JSON格式
                mappingList = parseJsonMappings(request);
            } else if (contentType != null && contentType.contains("application/x-www-form-urlencoded")) {
                // Form-urlencoded格式
                mappingList = parseFormUrlEncodedMappings(request);
            }

            if (mappingList == null || mappingList.isEmpty()) {
                return JsonBean.error("映射配置不能为空");
            }

            for (int i = 0; i < mappingList.size(); i++) {
                VersionFieldMapping mapping = mappingList.get(i);

                // 生成或验证 mappingId（主键，不能为空）
                if (mapping.getMappingId() == null || mapping.getMappingId().trim().isEmpty()) {
                    mapping.setMappingId(UUID.randomUUID().toString());
                }

                // 验证必填字段
                if (mapping.getVersionFid() == null || mapping.getVersionFid().trim().isEmpty()) {
                    return JsonBean.error("版本FID不能为空");
                }
                if (mapping.getSourceTableName() == null || mapping.getSourceTableName().trim().isEmpty()) {
                    return JsonBean.error("源表名不能为空");
                }
                if (mapping.getSourceFieldName() == null || mapping.getSourceFieldName().trim().isEmpty()) {
                    return JsonBean.error("源字段名不能为空");
                }
                if (mapping.getTargetTableName() == null || mapping.getTargetTableName().trim().isEmpty()) {
                    return JsonBean.error("目标表名不能为空");
                }
                if (mapping.getTargetFieldName() == null || mapping.getTargetFieldName().trim().isEmpty()) {
                    return JsonBean.error("目标字段名不能为空");
                }

                // 清理所有空字符串，转换为 null
                if (mapping.getCalculationLogic() != null && mapping.getCalculationLogic().trim().isEmpty()) {
                    mapping.setCalculationLogic(null);
                }
                if (mapping.getQueryCondition() != null && mapping.getQueryCondition().trim().isEmpty()) {
                    mapping.setQueryCondition(null);
                }
                if (mapping.getFieldType() != null && mapping.getFieldType().trim().isEmpty()) {
                    mapping.setFieldType(null);
                }
                if (mapping.getIsKey() != null && mapping.getIsKey().trim().isEmpty()) {
                    mapping.setIsKey(null);
                }
                if (mapping.getIsRequired() != null && mapping.getIsRequired().trim().isEmpty()) {
                    mapping.setIsRequired(null);
                }
                if (mapping.getStatus() != null && mapping.getStatus().trim().isEmpty()) {
                    mapping.setStatus(null);
                }
                if (mapping.getCreateUser() != null && mapping.getCreateUser().trim().isEmpty()) {
                    mapping.setCreateUser(null);
                }
                if (mapping.getUpdateUser() != null && mapping.getUpdateUser().trim().isEmpty()) {
                    mapping.setUpdateUser(null);
                }
                if (mapping.getRemark() != null && mapping.getRemark().trim().isEmpty()) {
                    mapping.setRemark(null);
                }

                // 调试日志：打印每条数据的完整信息（包括所有字段）
                log.info("========== 第{}条映射数据详情 ==========", i);
                log.info("VARCHAR2字段检查（最大长度限制）:");
                log.info("  mappingId: '{}' (长度: {}, 限制: 32)", mapping.getMappingId(), mapping.getMappingId() != null ? mapping.getMappingId().length() : 0);
                log.info("  versionFid: '{}' (长度: {}, 限制: 32)", mapping.getVersionFid(), mapping.getVersionFid() != null ? mapping.getVersionFid().length() : 0);
                log.info("  sourceTableName: '{}' (长度: {}, 限制: 100)", mapping.getSourceTableName(), mapping.getSourceTableName() != null ? mapping.getSourceTableName().length() : 0);
                log.info("  sourceFieldName: '{}' (长度: {}, 限制: 100)", mapping.getSourceFieldName(), mapping.getSourceFieldName() != null ? mapping.getSourceFieldName().length() : 0);
                log.info("  targetTableName: '{}' (长度: {}, 限制: 100)", mapping.getTargetTableName(), mapping.getTargetTableName() != null ? mapping.getTargetTableName().length() : 0);
                log.info("  targetFieldName: '{}' (长度: {}, 限制: 100)", mapping.getTargetFieldName(), mapping.getTargetFieldName() != null ? mapping.getTargetFieldName().length() : 0);
                log.info("  calculationLogic: '{}' (长度: {}, 限制: 2000)", mapping.getCalculationLogic(), mapping.getCalculationLogic() != null ? mapping.getCalculationLogic().length() : 0);
                log.info("  queryCondition: '{}' (长度: {}, 限制: 2000)", mapping.getQueryCondition(), mapping.getQueryCondition() != null ? mapping.getQueryCondition().length() : 0);
                log.info("  fieldType: '{}' (长度: {}, 限制: 50)", mapping.getFieldType(), mapping.getFieldType() != null ? mapping.getFieldType().length() : 0);
                log.info("  isKey: '{}' (长度: {}, 限制: 1)", mapping.getIsKey(), mapping.getIsKey() != null ? mapping.getIsKey().length() : 0);
                log.info("  isRequired: '{}' (长度: {}, 限制: 1)", mapping.getIsRequired(), mapping.getIsRequired() != null ? mapping.getIsRequired().length() : 0);
                log.info("  status: '{}' (长度: {}, 限制: 20)", mapping.getStatus(), mapping.getStatus() != null ? mapping.getStatus().length() : 0);
                log.info("  createUser: '{}' (长度: {}, 限制: 100)", mapping.getCreateUser(), mapping.getCreateUser() != null ? mapping.getCreateUser().length() : 0);
                log.info("  updateUser: '{}' (长度: {}, 限制: 100)", mapping.getUpdateUser(), mapping.getUpdateUser() != null ? mapping.getUpdateUser().length() : 0);
                log.info("  remark: '{}' (长度: {}, 限制: 500)", mapping.getRemark(), mapping.getRemark() != null ? mapping.getRemark().length() : 0);
                log.info("NUMBER字段检查:");
                log.info("  fieldLength: {}", mapping.getFieldLength());
                log.info("  sortOrder: {}", mapping.getSortOrder());
                log.info("========== 第{}条映射数据详情结束 ==========", i);
            }
            // 调用服务层的智能保存方法（新增或修改）
            versionFieldMappingService.saveOrUpdateBatch(mappingList);
            return JsonBean.success("保存成功");
        } catch (Exception e) {
            log.error("批量保存字段映射失败", e);
            return JsonBean.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 解析JSON格式的映射数据
     */
    private List<VersionFieldMapping> parseJsonMappings(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }

        String jsonStr = sb.toString();
        if (jsonStr.isEmpty()) {
            return null;
        }

        // 使用com.alibaba.fastjson解析JSON
        try {
            return com.alibaba.fastjson.JSON.parseArray(jsonStr, VersionFieldMapping.class);
        } catch (Exception e) {
            log.error("解析JSON格式的映射数据失败: {}", jsonStr, e);
            return null;
        }
    }

    /**
     * 解析form-urlencoded格式的映射数据
     * 格式：0[fieldName]=value, 1[fieldName]=value, ...
     */
    private List<VersionFieldMapping> parseFormUrlEncodedMappings(HttpServletRequest request) {
        try {
            Map<String, String[]> parameterMap = request.getParameterMap();
            if (parameterMap == null || parameterMap.isEmpty()) {
                return null;
            }

            List<VersionFieldMapping> mappings = new java.util.ArrayList<>();
            Map<Integer, VersionFieldMapping> mappingMap = new java.util.TreeMap<>();

            // 遍历所有参数，按索引分组
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                String[] values = entry.getValue();
                String value = (values != null && values.length > 0) ? values[0] : "";

                // 解析格式：0[fieldName] -> 索引0，字段fieldName
                if (key.matches("\\d+\\[.*\\]")) {
                    int index = Integer.parseInt(key.substring(0, key.indexOf('[')));
                    String fieldName = key.substring(key.indexOf('[') + 1, key.indexOf(']'));

                    VersionFieldMapping mapping = mappingMap.computeIfAbsent(index, k -> new VersionFieldMapping());

                    // 根据字段名设置值
                    switch (fieldName) {
                        case "mappingId":
                            mapping.setMappingId(value);
                            break;
                        case "versionFid":
                            mapping.setVersionFid(value);
                            break;
                        case "sourceTableName":
                            mapping.setSourceTableName(value);
                            break;
                        case "sourceFieldName":
                            mapping.setSourceFieldName(value);
                            break;
                        case "targetTableName":
                            mapping.setTargetTableName(value);
                            break;
                        case "targetFieldName":
                            mapping.setTargetFieldName(value);
                            break;
                        case "calculationLogic":
                            mapping.setCalculationLogic(value);
                            break;
                        case "queryCondition":
                            mapping.setQueryCondition(value);
                            break;
                        case "fieldType":
                            mapping.setFieldType(value);
                            break;
                        case "fieldLength":
                            if (value != null && !value.isEmpty()) {
                                mapping.setFieldLength(Integer.parseInt(value));
                            }
                            break;
                        case "isKey":
                            mapping.setIsKey(value);
                            break;
                        case "isRequired":
                            mapping.setIsRequired(value);
                            break;
                        case "sortOrder":
                            if (value != null && !value.isEmpty()) {
                                mapping.setSortOrder(Integer.parseInt(value));
                            }
                            break;
                        case "status":
                            mapping.setStatus(value);
                            break;
                    }
                }
            }

            mappings.addAll(mappingMap.values());
            return mappings.isEmpty() ? null : mappings;
        } catch (Exception e) {
            log.error("解析form-urlencoded格式的映射数据失败", e);
            return null;
        }
    }

    /**
     * 删除字段映射
     */
    @PostMapping("/delete/{mappingId}")
    @Operation(summary = "删除字段映射,删除指定的字段映射")
    public String delete(@PathVariable String mappingId) {
        try {
            versionFieldMappingService.removeById(mappingId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除字段映射失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 删除版本的所有字段映射
     */
    @PostMapping("/deleteByVersionFid/{versionFid}")
    @Operation(summary = "删除版本的所有字段映射,删除指定版本的所有字段映射")
    public String deleteByVersionFid(@PathVariable String versionFid) {
        try {
            versionFieldMappingService.deleteByVersionFid(versionFid);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除字段映射失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 获取版本的所有源表名称
     */
    @GetMapping("/getSourceTableNames/{versionFid}")
    @Operation(summary = "获取源表名称列表,获取指定版本的所有源表名称")
    public String getSourceTableNames(@PathVariable String versionFid) {
        try {
            List<String> tableNames = versionFieldMappingService.getSourceTableNames(versionFid);
            return JsonBean.success("获取成功", tableNames);
        } catch (Exception e) {
            log.error("获取源表名称列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取版本的所有目标表名称
     */
    @GetMapping("/getTargetTableNames/{versionFid}")
    @Operation(summary = "获取目标表名称列表,获取指定版本的所有目标表名称")
    public String getTargetTableNames(@PathVariable String versionFid) {
        try {
            List<String> tableNames = versionFieldMappingService.getTargetTableNames(versionFid);
            return JsonBean.success("获取成功", tableNames);
        } catch (Exception e) {
            log.error("获取目标表名称列表失败", e);
            return JsonBean.error("获取失败: " + e.getMessage());
        }
    }
}

