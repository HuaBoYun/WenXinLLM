package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.DocumentArchive;
import com.huabo.contract.mapper.DocumentArchiveMapper;
import com.huabo.contract.service.DocumentArchiveService;
import com.huabo.contract.vo.DocumentArchiveQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * 文档归档服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class DocumentArchiveServiceImpl extends ServiceImpl<DocumentArchiveMapper, DocumentArchive> implements DocumentArchiveService {

    @Autowired
    private DocumentArchiveMapper documentArchiveMapper;

    @Override
    public PageInfo<DocumentArchive> getDocumentArchiveList(DocumentArchiveQueryParam param) {
        log.info("分页查询文档归档列表，参数：{}", param);
        
        // 设置分页参数
        PageHelper.startPage(param.getPageNumber(), param.getPageSize(), param.getOrderBySql());
        
        // 查询数据
        List<DocumentArchive> list = documentArchiveMapper.selectDocumentArchiveList(param);
        
        return new PageInfo<>(list);
    }

    @Override
    public DocumentArchive getDocumentArchiveById(Long id) {
        log.info("根据ID获取文档归档详情，ID：{}", id);
        return documentArchiveMapper.selectById(id);
    }

    @Override
    public boolean saveDocumentArchive(DocumentArchive documentArchive) {
        log.info("保存文档归档，文档归档：{}", documentArchive);
        
        try {
            Date now = new Date();
            
            if (documentArchive.getId() == null) {
                // 新增
                documentArchive.setCreateTime(now);
                documentArchive.setUpdateTime(now);
                
                // 生成文档编号
                if (!StringUtils.hasText(documentArchive.getDocumentNo())) {
                    documentArchive.setDocumentNo(generateDocumentNo());
                }
                
                return documentArchiveMapper.insert(documentArchive) > 0;
            } else {
                // 修改
                documentArchive.setUpdateTime(now);
                return documentArchiveMapper.updateById(documentArchive) > 0;
            }
        } catch (Exception e) {
            log.error("保存文档归档失败", e);
            return false;
        }
    }

    @Override
    public boolean deleteDocumentArchive(Long id) {
        log.info("删除文档归档，ID：{}", id);
        return documentArchiveMapper.deleteById(id) > 0;
    }

    @Override
    public boolean deleteBatchDocumentArchive(List<Long> ids) {
        log.info("批量删除文档归档，IDs：{}", ids);
        return documentArchiveMapper.deleteBatchByIds(ids) > 0;
    }

    @Override
    public List<DocumentArchive> getDocumentArchiveByProjectId(Long projectId) {
        log.info("根据项目ID查询文档归档列表，项目ID：{}", projectId);
        return documentArchiveMapper.selectByProjectId(projectId);
    }

    @Override
    public List<DocumentArchive> getDocumentArchiveByDocumentType(Integer documentType) {
        log.info("根据文档类型查询文档归档列表，文档类型：{}", documentType);
        return documentArchiveMapper.selectByDocumentType(documentType);
    }

    @Override
    public List<DocumentArchive> getDocumentArchiveByArchiverId(Long archiverId) {
        log.info("根据归档人ID查询文档归档列表，归档人ID：{}", archiverId);
        return documentArchiveMapper.selectByArchiverId(archiverId);
    }

    @Override
    public List<DocumentArchive> getDocumentArchiveByDocumentStatus(Integer documentStatus) {
        log.info("根据文档状态查询文档归档列表，文档状态：{}", documentStatus);
        return documentArchiveMapper.selectByDocumentStatus(documentStatus);
    }

    @Override
    public List<DocumentArchive> searchDocumentArchiveByKeywords(String keywords) {
        log.info("根据关键词搜索文档归档，关键词：{}", keywords);
        return documentArchiveMapper.searchByKeywords(keywords);
    }

    @Override
    public DocumentArchive getDocumentArchiveByDocumentNo(String documentNo) {
        log.info("根据文档编号查询文档归档，文档编号：{}", documentNo);
        return documentArchiveMapper.selectByDocumentNo(documentNo);
    }

    @Override
    public boolean updateDocumentStatus(Long id, Integer status) {
        log.info("更新文档状态，ID：{}，状态：{}", id, status);
        return documentArchiveMapper.updateDocumentStatus(id, status) > 0;
    }

    @Override
    public List<DocumentArchive> getDocumentArchiveStatistics() {
        log.info("获取文档归档统计信息");
        return documentArchiveMapper.getDocumentArchiveStatistics();
    }

    @Override
    public String generateDocumentNo() {
        // 生成文档编号：DA + 年月日 + 4位序号
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(new Date());
        
        // 查询当天已有的文档数量
        String prefix = "DA" + dateStr;
        
        // 这里简化处理，实际应该查询数据库获取当天的最大序号
        long timestamp = System.currentTimeMillis();
        String sequence = String.format("%04d", timestamp % 10000);
        
        return prefix + sequence;
    }
}
