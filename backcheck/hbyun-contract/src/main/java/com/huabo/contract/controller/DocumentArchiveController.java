package com.huabo.contract.controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.DocumentArchive;
import com.huabo.contract.service.DocumentArchiveService;
import com.huabo.contract.vo.DocumentArchiveQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 文档归档控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/archive")
@Tag(name="文档归档管理",description="文档归档管理")
@Validated
public class DocumentArchiveController {

    @Autowired
    private DocumentArchiveService documentArchiveService;

    /**
     * 分页查询文档归档列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/document/list")
    @Operation(summary = "分页查询文档归档列表", description = "支持多条件查询和分页")
    public String getDocumentArchiveList(@RequestBody DocumentArchiveQueryParam param) {
        try {
            log.info("分页查询文档归档列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(10);
            }

            PageInfo<DocumentArchive> pageInfo = documentArchiveService.getDocumentArchiveList(param);
            return JsonBean.success(pageInfo, pageInfo.getList());
        } catch (Exception e) {
            log.error("分页查询文档归档列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取文档归档详情
     *
     * @param id 主键ID
     * @return 文档归档详情
     */
    @GetMapping("/document/{id}")
    @Operation(summary = "获取文档归档详情", description = "根据ID获取详细信息")
    public String getDocumentArchiveById(@PathVariable Long id) {
        try {
            log.info("获取文档归档详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("文档ID不能为空");
            }

            DocumentArchive documentArchive = documentArchiveService.getDocumentArchiveById(id);
            if (documentArchive == null) {
                return JsonBean.error("文档归档不存在");
            }

            return JsonBean.success("查询成功", documentArchive);
        } catch (Exception e) {
            log.error("获取文档归档详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建文档归档
     *
     * @param documentArchive 文档归档
     * @return 保存结果
     */
    @PostMapping("/document/create")
    @Operation(summary = "创建文档归档", description = "新增文档归档")
    public String createDocumentArchive(@RequestBody DocumentArchive documentArchive) {
        try {
            log.info("创建文档归档，文档归档：{}", documentArchive);

            // 参数校验
            if (documentArchive.getProjectId() == null) {
                return JsonBean.error("项目ID不能为空");
            }
            if (!StringUtils.hasText(documentArchive.getDocumentName())) {
                return JsonBean.error("文档名称不能为空");
            }
            if (documentArchive.getDocumentType() == null) {
                return JsonBean.error("文档类型不能为空");
            }
            if (documentArchive.getArchiveDate() == null) {
                return JsonBean.error("归档日期不能为空");
            }
            if (documentArchive.getArchiverId() == null) {
                return JsonBean.error("归档人ID不能为空");
            }

            // 设置默认值
            if (documentArchive.getAccessLevel() == null) {
                documentArchive.setAccessLevel(1); // 默认公开
            }
            if (documentArchive.getDocumentStatus() == null) {
                documentArchive.setDocumentStatus(1); // 默认有效
            }

            // 设置创建和更新信息
            Date now = new Date();
            documentArchive.setCreateTime(now);
            documentArchive.setUpdateTime(now);
            documentArchive.setCreateBy(documentArchive.getArchiverId()); // 使用归档人ID作为创建人
            documentArchive.setUpdateBy(documentArchive.getArchiverId()); // 使用归档人ID作为更新人

            boolean result = documentArchiveService.saveDocumentArchive(documentArchive);
            if (result) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建文档归档失败", e);
            return JsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新文档归档
     *
     * @param id 文档ID
     * @param documentArchive 文档归档
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新文档归档", description = "根据ID更新文档归档")
    public String updateDocumentArchive(@PathVariable Long id, @RequestBody DocumentArchive documentArchive) {
        try {
            log.info("更新文档归档，ID：{}，文档归档：{}", id, documentArchive);

            if (id == null) {
                return JsonBean.error("文档ID不能为空");
            }

            // 设置ID和更新时间
            documentArchive.setId(id);
            documentArchive.setUpdateTime(new Date());

            boolean result = documentArchiveService.saveDocumentArchive(documentArchive);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新文档归档失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除文档归档
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/document/{id}")
    @Operation(summary = "删除文档归档", description = "根据ID删除文档归档")
    public String deleteDocumentArchive(@PathVariable Long id) {
        try {
            log.info("删除文档归档，ID：{}", id);

            if (id == null) {
                return JsonBean.error("文档ID不能为空");
            }

            boolean result = documentArchiveService.deleteDocumentArchive(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除文档归档失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 文档检索
     *
     * @param keywords 关键词
     * @param projectId 项目ID
     * @return 检索结果
     */
    @GetMapping("/document/search")
    @Operation(summary = "文档检索", description = "根据关键词和项目ID检索文档")
    public String searchDocumentArchive(@RequestParam(required = false) String keywords,
                                       @RequestParam(required = false) Long projectId) {
        try {
            log.info("文档检索，关键词：{}，项目ID：{}", keywords, projectId);

            List<DocumentArchive> list;
            if (StringUtils.hasText(keywords)) {
                list = documentArchiveService.searchDocumentArchiveByKeywords(keywords);
            } else if (projectId != null) {
                list = documentArchiveService.getDocumentArchiveByProjectId(projectId);
            } else {
                return JsonBean.error("请提供关键词或项目ID");
            }

            return JsonBean.success("检索成功", list);
        } catch (Exception e) {
            log.error("文档检索失败", e);
            return JsonBean.error("检索失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询文档归档列表
     *
     * @param projectId 项目ID
     * @return 文档归档列表
     */
    @GetMapping("/document/project/{projectId}")
    @Operation(summary = "根据项目ID查询文档归档列表", description = "根据项目ID查询文档归档列表")
    public String getDocumentArchiveByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询文档归档列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<DocumentArchive> list = documentArchiveService.getDocumentArchiveByProjectId(projectId);
            return JsonBean.success("查询成功", list);
        } catch (Exception e) {
            log.error("根据项目ID查询文档归档列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取文档归档统计信息
     *
     * @return 统计信息
     */
    @GetMapping("/document/statistics")
    @Operation(summary = "获取文档归档统计信息", description = "获取文档归档统计信息")
    public String getDocumentArchiveStatistics() {
        try {
            log.info("获取文档归档统计信息");

            List<DocumentArchive> statistics = documentArchiveService.getDocumentArchiveStatistics();
            return JsonBean.success("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取文档归档统计信息失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }
}
