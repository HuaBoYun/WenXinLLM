package com.huabo.contract.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.DocumentArchive;
import com.huabo.contract.vo.DocumentArchiveQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 文档归档Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Mapper
public interface DocumentArchiveMapper extends BaseMapper<DocumentArchive> {

    /**
     * 分页查询文档归档列表
     *
     * @param param 查询参数
     * @return 文档归档列表
     */
    List<DocumentArchive> selectDocumentArchiveList(@Param("param") DocumentArchiveQueryParam param);

    /**
     * 根据项目ID查询文档归档列表
     *
     * @param projectId 项目ID
     * @return 文档归档列表
     */
    List<DocumentArchive> selectByProjectId(@Param("projectId") Long projectId);

    /**
     * 根据文档类型查询文档归档列表
     *
     * @param documentType 文档类型
     * @return 文档归档列表
     */
    List<DocumentArchive> selectByDocumentType(@Param("documentType") Integer documentType);

    /**
     * 根据归档人ID查询文档归档列表
     *
     * @param archiverId 归档人ID
     * @return 文档归档列表
     */
    List<DocumentArchive> selectByArchiverId(@Param("archiverId") Long archiverId);

    /**
     * 根据文档状态查询文档归档列表
     *
     * @param documentStatus 文档状态
     * @return 文档归档列表
     */
    List<DocumentArchive> selectByDocumentStatus(@Param("documentStatus") Integer documentStatus);

    /**
     * 根据关键词搜索文档归档
     *
     * @param keywords 关键词
     * @return 文档归档列表
     */
    List<DocumentArchive> searchByKeywords(@Param("keywords") String keywords);

    /**
     * 根据文档编号查询文档归档
     *
     * @param documentNo 文档编号
     * @return 文档归档
     */
    DocumentArchive selectByDocumentNo(@Param("documentNo") String documentNo);

    /**
     * 统计文档归档数量
     *
     * @param param 查询参数
     * @return 数量
     */
    Long countDocumentArchive(@Param("param") DocumentArchiveQueryParam param);

    /**
     * 批量删除文档归档
     *
     * @param ids ID列表
     * @return 删除数量
     */
    int deleteBatchByIds(@Param("ids") List<Long> ids);

    /**
     * 更新文档状态
     *
     * @param id 文档ID
     * @param status 状态
     * @return 更新数量
     */
    int updateDocumentStatus(@Param("id") Long id, @Param("status") Integer status);

    /**
     * 获取文档归档统计信息
     *
     * @return 统计信息
     */
    List<DocumentArchive> getDocumentArchiveStatistics();
}
