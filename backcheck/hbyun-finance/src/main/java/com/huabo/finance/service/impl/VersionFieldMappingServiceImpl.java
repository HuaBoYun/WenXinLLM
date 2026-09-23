package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.finance.entity.VersionFieldMapping;
import com.huabo.finance.mapper.VersionFieldMappingMapper;
import com.huabo.finance.service.IVersionFieldMappingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 版本字段映射服务实现类
 *
 * @author 开发者
 * @date 2025-10-23
 */
@Slf4j
@Service
public class VersionFieldMappingServiceImpl extends ServiceImpl<VersionFieldMappingMapper, VersionFieldMapping> implements IVersionFieldMappingService {

    @Autowired
    private VersionFieldMappingMapper versionFieldMappingMapper;

    @Override
    public List<VersionFieldMapping> getByVersionFid(String versionFid) {
        try {
            return versionFieldMappingMapper.selectByVersionFid(versionFid);
        } catch (Exception e) {
            log.error("获取版本字段映射失败, versionFid={}", versionFid, e);
            return null;
        }
    }

    @Override
    public List<VersionFieldMapping> getByVersionFidAndSourceTable(String versionFid, String sourceTableName) {
        try {
            return versionFieldMappingMapper.selectByVersionFidAndSourceTable(versionFid, sourceTableName);
        } catch (Exception e) {
            log.error("获取版本字段映射失败, versionFid={}, sourceTableName={}", versionFid, sourceTableName, e);
            return null;
        }
    }

    @Override
    public List<VersionFieldMapping> getByVersionFidAndTargetTable(String versionFid, String targetTableName) {
        try {
            return versionFieldMappingMapper.selectByVersionFidAndTargetTable(versionFid, targetTableName);
        } catch (Exception e) {
            log.error("获取版本字段映射失败, versionFid={}, targetTableName={}", versionFid, targetTableName, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveMappings(List<VersionFieldMapping> mappings) {
        try {
            if (mappings == null || mappings.isEmpty()) {
                return false;
            }
            
            for (VersionFieldMapping mapping : mappings) {
                this.save(mapping);
            }
            return true;
        } catch (Exception e) {
            log.error("保存版本字段映射失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByVersionFid(String versionFid) {
        try {
            versionFieldMappingMapper.deleteByVersionFid(versionFid);
            return true;
        } catch (Exception e) {
            log.error("删除版本字段映射失败, versionFid={}", versionFid, e);
            return false;
        }
    }

    @Override
    public List<String> getSourceTableNames(String versionFid) {
        try {
            List<VersionFieldMapping> mappings = versionFieldMappingMapper.selectByVersionFid(versionFid);
            if (mappings == null || mappings.isEmpty()) {
                return null;
            }
            return mappings.stream()
                    .map(VersionFieldMapping::getSourceTableName)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取源表名称列表失败, versionFid={}", versionFid, e);
            return null;
        }
    }

    @Override
    public List<String> getTargetTableNames(String versionFid) {
        try {
            List<VersionFieldMapping> mappings = versionFieldMappingMapper.selectByVersionFid(versionFid);
            if (mappings == null || mappings.isEmpty()) {
                return null;
            }
            return mappings.stream()
                    .map(VersionFieldMapping::getTargetTableName)
                    .distinct()
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("获取目标表名称列表失败, versionFid={}", versionFid, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(List<VersionFieldMapping> mappings) {
        try {
            if (mappings == null || mappings.isEmpty()) {
                return false;
            }

            log.info("========== 开始批量保存或更新字段映射，共 {} 条 ==========", mappings.size());

            for (int i = 0; i < mappings.size(); i++) {
                VersionFieldMapping mapping = mappings.get(i);

                log.info("处理第 {} 条映射: mappingId={}, versionFid={}, sourceTable={}, sourceField={}, targetTable={}, targetField={}",
                        i, mapping.getMappingId(), mapping.getVersionFid(), mapping.getSourceTableName(),
                        mapping.getSourceFieldName(), mapping.getTargetTableName(), mapping.getTargetFieldName());

                // 检查 mappingId 是否已存在
                if (mapping.getMappingId() != null && !mapping.getMappingId().trim().isEmpty()) {
                    String mappingId = mapping.getMappingId();
                    log.info("  → mappingId 长度: {}, 内容: '{}'", mappingId.length(), mappingId);

                    // 使用 saveOrUpdate 方法（MyBatis-Plus 内置方法）
                    // 这个方法会自动判断是否存在，存在则更新，不存在则新增
                    boolean result = this.saveOrUpdate(mapping);

                    if (result) {
                        log.info("✓ 保存或更新字段映射成功: mappingId={}", mappingId);
                    } else {
                        log.warn("⚠ 保存或更新字段映射返回 false: mappingId={}", mappingId);
                    }
                } else {
                    // mappingId 为空，执行新增
                    log.info("  → mappingId 为空，执行 INSERT");
                    boolean result = this.save(mapping);
                    if (result) {
                        log.info("✓ 新增字段映射成功: versionFid={}, sourceTable={}", mapping.getVersionFid(), mapping.getSourceTableName());
                    } else {
                        log.warn("⚠ 新增字段映射返回 false: versionFid={}, sourceTable={}", mapping.getVersionFid(), mapping.getSourceTableName());
                    }
                }
            }

            log.info("========== 批量保存或更新字段映射完成 ==========");
            return true;
        } catch (Exception e) {
            log.error("保存或更新字段映射失败", e);
            throw new RuntimeException("保存或更新字段映射失败: " + e.getMessage(), e);
        }
    }
}

