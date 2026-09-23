package com.huabo.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.DocumentArchive;
import com.huabo.contract.vo.DocumentArchiveQueryParam;

import java.util.List;

/**
 * 文档归档服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
public interface DocumentArchiveService extends IService<DocumentArchive> {

    /**
     * 分页查询文档归档列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<DocumentArchive> getDocumentArchiveList(DocumentArchiveQueryParam param);

    /**
     * 根据ID获取文档归档详情
     *
     * @param id 主键ID
     * @return 文档归档详情
     */
    DocumentArchive getDocumentArchiveById(Long id);

    /**
     * 保存文档归档（新增或修改）
     *
     * @param documentArchive 文档归档
     * @return 保存结果
     */
    boolean saveDocumentArchive(DocumentArchive documentArchive);

    /**
     * 删除文档归档
     *
     * @param id 主键ID
     * @return 删除结果
     */
    boolean deleteDocumentArchive(Long id);

    /**
     * 批量删除文档归档
     *
     * @param ids ID列表
     * @return 删除结果
     */
    boolean deleteBatchDocumentArchive(List<Long> ids);

    /**
     * 根据项目ID查询文档归档列表
     *
     * @param projectId 项目ID
     * @return 文档归档列表
     */
    List<DocumentArchive> getDocumentArchiveByProjectId(Long projectId);

    /**
     * 根据文档类型查询文档归档列表
     *
     * @param documentType 文档类型
     * @return 文档归档列表
     */
    List<DocumentArchive> getDocumentArchiveByDocumentType(Integer documentType);

    /**
     * 根据归档人ID查询文档归档列表
     *
     * @param archiverId 归档人ID
     * @return 文档归档列表
     */
    List<DocumentArchive> getDocumentArchiveByArchiverId(Long archiverId);

    /**
     * 根据文档状态查询文档归档列表
     *
     * @param documentStatus 文档状态
     * @return 文档归档列表
     */
    List<DocumentArchive> getDocumentArchiveByDocumentStatus(Integer documentStatus);

    /**
     * 根据关键词搜索文档归档
     *
     * @param keywords 关键词
     * @return 文档归档列表
     */
    List<DocumentArchive> searchDocumentArchiveByKeywords(String keywords);

    /**
     * 根据文档编号查询文档归档
     *
     * @param documentNo 文档编号
     * @return 文档归档
     */
    DocumentArchive getDocumentArchiveByDocumentNo(String documentNo);

    /**
     * 更新文档状态
     *
     * @param id 文档ID
     * @param status 状态
     * @return 更新结果
     */
    boolean updateDocumentStatus(Long id, Integer status);

    /**
     * 获取文档归档统计信息
     *
     * @return 统计信息
     */
    List<DocumentArchive> getDocumentArchiveStatistics();

    /**
     * 生成文档编号
     *
     * @return 文档编号
     */
    String generateDocumentNo();
}
