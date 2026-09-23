package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.fxgl.dto.DataModelVersionCreateDTO;
import com.huabo.fxgl.dto.DataModelVersionQueryDTO;
import com.huabo.fxgl.entity.TblDataModelVersion;

import java.util.List;
import java.util.Map;

/**
 * 数据模型版本管理服务接口
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
public interface DataModelVersionService extends IService<TblDataModelVersion> {

    /**
     * 分页查询模型版本列表
     *
     * @param queryDTO 查询参数
     * @return 分页结果
     */
    IPage<TblDataModelVersion> getVersionPage(DataModelVersionQueryDTO queryDTO);

    /**
     * 查询模型的所有版本
     *
     * @param modelId 模型ID
     * @return 版本列表
     */
    List<TblDataModelVersion> getVersionsByModelId(String modelId);

    /**
     * 创建新版本
     *
     * @param createDTO 创建参数
     * @param createUser 创建人
     * @return 新版本信息
     */
    TblDataModelVersion createVersion(DataModelVersionCreateDTO createDTO, String createUser);

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     * @param deleteUser 删除人
     * @return 是否成功
     */
    boolean deleteVersion(String versionId, String deleteUser);

    /**
     * 发布版本
     *
     * @param versionId 版本ID
     * @param publishUser 发布人
     * @return 是否成功
     */
    boolean publishVersion(String versionId, String publishUser);

    /**
     * 归档版本
     *
     * @param versionId 版本ID
     * @param archiveUser 归档人
     * @return 是否成功
     */
    boolean archiveVersion(String versionId, String archiveUser);

    /**
     * 设置当前版本
     *
     * @param versionId 版本ID
     * @param updateUser 更新人
     * @return 是否成功
     */
    boolean setCurrentVersion(String versionId, String updateUser);

    /**
     * 回滚到指定版本
     *
     * @param versionId 版本ID
     * @param rollbackUser 回滚人
     * @return 是否成功
     */
    boolean rollbackToVersion(String versionId, String rollbackUser);

    /**
     * 获取版本详情
     *
     * @param versionId 版本ID
     * @return 版本详情
     */
    TblDataModelVersion getVersionDetail(String versionId);

    /**
     * 获取当前版本
     *
     * @param modelId 模型ID
     * @return 当前版本
     */
    TblDataModelVersion getCurrentVersion(String modelId);

    /**
     * 获取最新版本
     *
     * @param modelId 模型ID
     * @return 最新版本
     */
    TblDataModelVersion getLatestVersion(String modelId);

    /**
     * 比较两个版本
     *
     * @param sourceVersionId 源版本ID
     * @param targetVersionId 目标版本ID
     * @return 比较结果
     */
    Map<String, Object> compareVersions(String sourceVersionId, String targetVersionId);

    /**
     * 获取版本统计信息
     *
     * @param modelId 模型ID
     * @return 统计信息
     */
    Map<String, Object> getVersionStatistics(String modelId);

    /**
     * 检查版本号是否可用
     *
     * @param modelId 模型ID
     * @param versionNo 版本号
     * @return 是否可用
     */
    boolean isVersionNoAvailable(String modelId, String versionNo);

    /**
     * 获取下一个版本号
     *
     * @param modelId 模型ID
     * @return 下一个版本号
     */
    String getNextVersionNo(String modelId);
}
