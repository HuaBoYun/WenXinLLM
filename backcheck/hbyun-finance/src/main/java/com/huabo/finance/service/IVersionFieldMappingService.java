package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.finance.entity.VersionFieldMapping;

import java.util.List;

/**
 * 版本字段映射服务接口
 *
 * @author 开发者
 * @date 2025-10-23
 */
public interface IVersionFieldMappingService extends IService<VersionFieldMapping> {

    /**
     * 根据版本FID获取所有字段映射
     *
     * @param versionFid 版本FID
     * @return 字段映射列表
     */
    List<VersionFieldMapping> getByVersionFid(String versionFid);

    /**
     * 根据版本FID和源表名获取字段映射
     *
     * @param versionFid 版本FID
     * @param sourceTableName 源表名
     * @return 字段映射列表
     */
    List<VersionFieldMapping> getByVersionFidAndSourceTable(String versionFid, String sourceTableName);

    /**
     * 根据版本FID和目标表名获取字段映射
     *
     * @param versionFid 版本FID
     * @param targetTableName 目标表名
     * @return 字段映射列表
     */
    List<VersionFieldMapping> getByVersionFidAndTargetTable(String versionFid, String targetTableName);

    /**
     * 保存版本字段映射列表
     *
     * @param mappings 字段映射列表
     * @return 是否成功
     */
    boolean saveMappings(List<VersionFieldMapping> mappings);

    /**
     * 删除版本的所有字段映射
     *
     * @param versionFid 版本FID
     * @return 是否成功
     */
    boolean deleteByVersionFid(String versionFid);

    /**
     * 获取版本的所有源表名称
     *
     * @param versionFid 版本FID
     * @return 源表名称列表
     */
    List<String> getSourceTableNames(String versionFid);

    /**
     * 获取版本的所有目标表名称
     *
     * @param versionFid 版本FID
     * @return 目标表名称列表
     */
    List<String> getTargetTableNames(String versionFid);

    /**
     * 智能保存或更新字段映射列表
     * 如果 mappingId 已存在，则更新；否则新增
     *
     * @param mappings 字段映射列表
     * @return 是否成功
     */
    boolean saveOrUpdateBatch(List<VersionFieldMapping> mappings);
}

