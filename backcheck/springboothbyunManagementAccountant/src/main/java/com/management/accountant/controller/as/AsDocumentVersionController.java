package com.management.accountant.controller.as;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.as.AsDocumentVersion;
import com.management.accountant.service.as.AsDocumentVersionService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 文档版本管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/as/documentVersion")
@Api(tags = "文档版本管理")
public class AsDocumentVersionController {

    @Autowired
    private AsDocumentVersionService documentVersionService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建文档版本")
    public MyJsonBean createVersion(@RequestBody AsDocumentVersion version, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            version.setTenantId(tenantId);
            version.setCreatedBy(userId);
            
            AsDocumentVersion result = documentVersionService.createVersion(version);
            return MyJsonBean.success("创建成功", result);
        } catch (Exception e) {
            log.error("创建文档版本失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新文档版本")
    public MyJsonBean updateVersion(@RequestBody AsDocumentVersion version, HttpServletRequest request) {
        try {
            String userId = request.getHeader("userId");
            version.setUpdatedBy(userId);
            
            AsDocumentVersion result = documentVersionService.updateVersion(version);
            return MyJsonBean.success("更新成功", result);
        } catch (Exception e) {
            log.error("更新文档版本失败", e);
            return MyJsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{versionId}")
    @ApiOperation("删除文档版本")
    public MyJsonBean deleteVersion(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            boolean result = documentVersionService.deleteVersion(tenantId, versionId);
            return result ? MyJsonBean.success("删除成功") : MyJsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除文档版本失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @GetMapping("/get/{versionId}")
    @ApiOperation("获取文档版本详情")
    public MyJsonBean getVersionById(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getVersionById(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取文档版本详情失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getByCode/{versionCode}")
    @ApiOperation("根据版本编号获取版本信息")
    public MyJsonBean getVersionByCode(@PathVariable String versionCode, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getVersionByCode(tenantId, versionCode);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("根据版本编号获取版本信息失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation("分页查询文档版本列表")
    public MyJsonBean getVersionPage(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Integer current = (Integer) params.getOrDefault("current", 1);
            Integer size = (Integer) params.getOrDefault("size", 20);
            
            IPage<AsDocumentVersion> result = documentVersionService.getVersionPage(tenantId, current, size, params);
            return MyJsonBean.success("查询成功", result);
        } catch (Exception e) {
            log.error("分页查询文档版本列表失败", e);
            return MyJsonBean.error("查询失败: " + e.getMessage());
        }
    }

    // ==================== 版本管理操作 ====================

    @PostMapping("/createNewVersion")
    @ApiOperation("创建新版本")
    public MyJsonBean createNewVersion(@RequestParam String documentId,
                                       @RequestParam String versionType,
                                       @RequestParam String description,
                                       HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.createNewVersion(tenantId, documentId, versionType, description, userId);
            return MyJsonBean.success("创建新版本成功", result);
        } catch (Exception e) {
            log.error("创建新版本失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/copyVersion")
    @ApiOperation("复制版本")
    public MyJsonBean copyVersion(@RequestParam String sourceVersionId,
                                  @RequestParam String newVersionName,
                                  HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.copyVersion(tenantId, sourceVersionId, newVersionName, userId);
            return MyJsonBean.success("复制版本成功", result);
        } catch (Exception e) {
            log.error("复制版本失败", e);
            return MyJsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/createBranch")
    @ApiOperation("创建分支版本")
    public MyJsonBean createBranch(@RequestParam String sourceVersionId,
                                   @RequestParam String branchName,
                                   @RequestParam String description,
                                   HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.createBranch(tenantId, sourceVersionId, branchName, description, userId);
            return MyJsonBean.success("创建分支成功", result);
        } catch (Exception e) {
            log.error("创建分支版本失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/mergeBranch")
    @ApiOperation("合并分支版本")
    public MyJsonBean mergeBranch(@RequestParam String sourceVersionId,
                                  @RequestParam String targetVersionId,
                                  @RequestParam String mergeMessage,
                                  HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.mergeBranch(tenantId, sourceVersionId, targetVersionId, mergeMessage, userId);
            return MyJsonBean.success("合并分支成功", result);
        } catch (Exception e) {
            log.error("合并分支版本失败", e);
            return MyJsonBean.error("合并失败: " + e.getMessage());
        }
    }

    @PostMapping("/createTag")
    @ApiOperation("创建标签版本")
    public MyJsonBean createTag(@RequestParam String versionId,
                                @RequestParam String tagName,
                                @RequestParam String description,
                                HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.createTag(tenantId, versionId, tagName, description, userId);
            return MyJsonBean.success("创建标签成功", result);
        } catch (Exception e) {
            log.error("创建标签版本失败", e);
            return MyJsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteTag")
    @ApiOperation("删除标签")
    public MyJsonBean deleteTag(@RequestParam String versionId,
                                @RequestParam String tagName,
                                HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.deleteTag(tenantId, versionId, tagName, userId);
            return result ? MyJsonBean.success("删除标签成功") : MyJsonBean.error("删除标签失败");
        } catch (Exception e) {
            log.error("删除标签失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/rollbackToVersion")
    @ApiOperation("回滚到指定版本")
    public MyJsonBean rollbackToVersion(@RequestParam String documentId,
                                        @RequestParam String targetVersionId,
                                        @RequestParam String rollbackReason,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            AsDocumentVersion result = documentVersionService.rollbackToVersion(tenantId, documentId, targetVersionId, rollbackReason, userId);
            return MyJsonBean.success("回滚成功", result);
        } catch (Exception e) {
            log.error("回滚到指定版本失败", e);
            return MyJsonBean.error("回滚失败: " + e.getMessage());
        }
    }

    @PostMapping("/setCurrentVersion")
    @ApiOperation("设置当前版本")
    public MyJsonBean setCurrentVersion(@RequestParam String documentId,
                                        @RequestParam String versionId,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.setCurrentVersion(tenantId, documentId, versionId, userId);
            return result ? MyJsonBean.success("设置当前版本成功") : MyJsonBean.error("设置当前版本失败");
        } catch (Exception e) {
            log.error("设置当前版本失败", e);
            return MyJsonBean.error("设置失败: " + e.getMessage());
        }
    }

    @PostMapping("/setDefaultVersion")
    @ApiOperation("设置默认版本")
    public MyJsonBean setDefaultVersion(@RequestParam String documentId,
                                        @RequestParam String versionId,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.setDefaultVersion(tenantId, documentId, versionId, userId);
            return result ? MyJsonBean.success("设置默认版本成功") : MyJsonBean.error("设置默认版本失败");
        } catch (Exception e) {
            log.error("设置默认版本失败", e);
            return MyJsonBean.error("设置失败: " + e.getMessage());
        }
    }

    // ==================== 版本状态管理 ====================

    @PostMapping("/submitForApproval")
    @ApiOperation("提交版本审批")
    public MyJsonBean submitForApproval(@RequestParam String versionId,
                                        @RequestParam String approvalProcessId,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.submitForApproval(tenantId, versionId, approvalProcessId, userId);
            return result ? MyJsonBean.success("提交审批成功") : MyJsonBean.error("提交审批失败");
        } catch (Exception e) {
            log.error("提交版本审批失败", e);
            return MyJsonBean.error("提交失败: " + e.getMessage());
        }
    }

    @PostMapping("/approveVersion")
    @ApiOperation("审批版本")
    public MyJsonBean approveVersion(@RequestParam String versionId,
                                     @RequestParam String approvalComment,
                                     HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String approverId = request.getHeader("userId");
            
            boolean result = documentVersionService.approveVersion(tenantId, versionId, approvalComment, approverId);
            return result ? MyJsonBean.success("审批成功") : MyJsonBean.error("审批失败");
        } catch (Exception e) {
            log.error("审批版本失败", e);
            return MyJsonBean.error("审批失败: " + e.getMessage());
        }
    }

    @PostMapping("/rejectVersion")
    @ApiOperation("拒绝版本")
    public MyJsonBean rejectVersion(@RequestParam String versionId,
                                    @RequestParam String rejectReason,
                                    HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String approverId = request.getHeader("userId");
            
            boolean result = documentVersionService.rejectVersion(tenantId, versionId, rejectReason, approverId);
            return result ? MyJsonBean.success("拒绝成功") : MyJsonBean.error("拒绝失败");
        } catch (Exception e) {
            log.error("拒绝版本失败", e);
            return MyJsonBean.error("拒绝失败: " + e.getMessage());
        }
    }

    @PostMapping("/publishVersion")
    @ApiOperation("发布版本")
    public MyJsonBean publishVersion(@RequestParam String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.publishVersion(tenantId, versionId, userId);
            return result ? MyJsonBean.success("发布成功") : MyJsonBean.error("发布失败");
        } catch (Exception e) {
            log.error("发布版本失败", e);
            return MyJsonBean.error("发布失败: " + e.getMessage());
        }
    }

    @PostMapping("/unpublishVersion")
    @ApiOperation("撤销发布")
    public MyJsonBean unpublishVersion(@RequestParam String versionId,
                                       @RequestParam String reason,
                                       HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.unpublishVersion(tenantId, versionId, reason, userId);
            return result ? MyJsonBean.success("撤销发布成功") : MyJsonBean.error("撤销发布失败");
        } catch (Exception e) {
            log.error("撤销发布版本失败", e);
            return MyJsonBean.error("撤销失败: " + e.getMessage());
        }
    }

    @PostMapping("/lockVersion")
    @ApiOperation("锁定版本")
    public MyJsonBean lockVersion(@RequestParam String versionId,
                                  @RequestParam String lockReason,
                                  HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.lockVersion(tenantId, versionId, lockReason, userId);
            return result ? MyJsonBean.success("锁定成功") : MyJsonBean.error("锁定失败");
        } catch (Exception e) {
            log.error("锁定版本失败", e);
            return MyJsonBean.error("锁定失败: " + e.getMessage());
        }
    }

    @PostMapping("/unlockVersion")
    @ApiOperation("解锁版本")
    public MyJsonBean unlockVersion(@RequestParam String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.unlockVersion(tenantId, versionId, userId);
            return result ? MyJsonBean.success("解锁成功") : MyJsonBean.error("解锁失败");
        } catch (Exception e) {
            log.error("解锁版本失败", e);
            return MyJsonBean.error("解锁失败: " + e.getMessage());
        }
    }

    @PostMapping("/archiveVersion")
    @ApiOperation("归档版本")
    public MyJsonBean archiveVersion(@RequestParam String versionId,
                                     @RequestParam String archiveReason,
                                     HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.archiveVersion(tenantId, versionId, archiveReason, userId);
            return result ? MyJsonBean.success("归档成功") : MyJsonBean.error("归档失败");
        } catch (Exception e) {
            log.error("归档版本失败", e);
            return MyJsonBean.error("归档失败: " + e.getMessage());
        }
    }

    @PostMapping("/restoreVersion")
    @ApiOperation("恢复版本")
    public MyJsonBean restoreVersion(@RequestParam String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = documentVersionService.restoreVersion(tenantId, versionId, userId);
            return result ? MyJsonBean.success("恢复成功") : MyJsonBean.error("恢复失败");
        } catch (Exception e) {
            log.error("恢复版本失败", e);
            return MyJsonBean.error("恢复失败: " + e.getMessage());
        }
    }

    // ==================== 版本查询操作 ====================

    @GetMapping("/getDocumentVersions/{documentId}")
    @ApiOperation("获取文档的所有版本")
    public MyJsonBean getDocumentVersions(@PathVariable String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getDocumentVersions(tenantId, documentId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取文档版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getCurrentVersion/{documentId}")
    @ApiOperation("获取当前版本")
    public MyJsonBean getCurrentVersion(@PathVariable String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getCurrentVersion(tenantId, documentId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取当前版本失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getDefaultVersion/{documentId}")
    @ApiOperation("获取默认版本")
    public MyJsonBean getDefaultVersion(@PathVariable String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getDefaultVersion(tenantId, documentId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取默认版本失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getLatestVersion/{documentId}")
    @ApiOperation("获取最新版本")
    public MyJsonBean getLatestVersion(@PathVariable String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getLatestVersion(tenantId, documentId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取最新版本失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionHistory/{documentId}")
    @ApiOperation("获取版本历史")
    public MyJsonBean getVersionHistory(@PathVariable String documentId,
                                        @RequestParam(defaultValue = "10") Integer limit,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionHistory(tenantId, documentId, limit);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本历史失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionTree/{documentId}")
    @ApiOperation("获取版本树结构")
    public MyJsonBean getVersionTree(@PathVariable String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionTree(tenantId, documentId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本树结构失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getChildVersions/{parentVersionId}")
    @ApiOperation("获取子版本列表")
    public MyJsonBean getChildVersions(@PathVariable String parentVersionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getChildVersions(tenantId, parentVersionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取子版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getParentVersion/{versionId}")
    @ApiOperation("获取父版本信息")
    public MyJsonBean getParentVersion(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            AsDocumentVersion result = documentVersionService.getParentVersion(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取父版本信息失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionsByBranch/{branchName}")
    @ApiOperation("根据分支名称获取版本列表")
    public MyJsonBean getVersionsByBranch(@PathVariable String branchName, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionsByBranch(tenantId, branchName);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("根据分支名称获取版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionsByTag/{tagName}")
    @ApiOperation("根据标签名称获取版本列表")
    public MyJsonBean getVersionsByTag(@PathVariable String tagName, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionsByTag(tenantId, tagName);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("根据标签名称获取版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionsByType/{versionType}")
    @ApiOperation("根据版本类型获取版本列表")
    public MyJsonBean getVersionsByType(@PathVariable String versionType, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionsByType(tenantId, versionType);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("根据版本类型获取版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionsByStatus/{versionStatus}")
    @ApiOperation("根据版本状态获取版本列表")
    public MyJsonBean getVersionsByStatus(@PathVariable String versionStatus, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<AsDocumentVersion> result = documentVersionService.getVersionsByStatus(tenantId, versionStatus);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("根据版本状态获取版本列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    // ==================== 版本对比操作 ====================

    @PostMapping("/compareVersions")
    @ApiOperation("比较两个版本")
    public MyJsonBean compareVersions(@RequestParam String sourceVersionId,
                                      @RequestParam String targetVersionId,
                                      HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.compareVersions(tenantId, sourceVersionId, targetVersionId);
            return MyJsonBean.success("比较成功", result);
        } catch (Exception e) {
            log.error("比较版本失败", e);
            return MyJsonBean.error("比较失败: " + e.getMessage());
        }
    }

    @PostMapping("/getVersionDiff")
    @ApiOperation("获取版本差异")
    public MyJsonBean getVersionDiff(@RequestParam String sourceVersionId,
                                     @RequestParam String targetVersionId,
                                     HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.getVersionDiff(tenantId, sourceVersionId, targetVersionId);
            return MyJsonBean.success("获取差异成功", result);
        } catch (Exception e) {
            log.error("获取版本差异失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionChangeHistory/{versionId}")
    @ApiOperation("获取版本变更历史")
    public MyJsonBean getVersionChangeHistory(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.getVersionChangeHistory(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本变更历史失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateComparisonReport")
    @ApiOperation("生成版本对比报告")
    public MyJsonBean generateComparisonReport(@RequestBody List<String> versionIds,
                                               @RequestParam String reportFormat,
                                               HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.generateComparisonReport(tenantId, versionIds, reportFormat);
            return MyJsonBean.success("生成报告成功", result);
        } catch (Exception e) {
            log.error("生成版本对比报告失败", e);
            return MyJsonBean.error("生成失败: " + e.getMessage());
        }
    }

    // ==================== 权限管理操作 ====================

    @GetMapping("/checkVersionAccess")
    @ApiOperation("检查版本访问权限")
    public MyJsonBean checkVersionAccess(@RequestParam String versionId,
                                         @RequestParam String permission,
                                         HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.checkVersionAccess(tenantId, versionId, userId, permission);
            return MyJsonBean.success("检查成功", result);
        } catch (Exception e) {
            log.error("检查版本访问权限失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionPermissions/{versionId}")
    @ApiOperation("获取版本权限列表")
    public MyJsonBean getVersionPermissions(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.getVersionPermissions(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本权限列表失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/setVersionPermission")
    @ApiOperation("设置版本权限")
    public MyJsonBean setVersionPermission(@RequestParam String versionId,
                                           @RequestParam String targetUserId,
                                           @RequestParam String permission,
                                           HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String granterId = request.getHeader("userId");
            boolean result = documentVersionService.setVersionPermission(tenantId, versionId, targetUserId, permission, granterId);
            return result ? MyJsonBean.success("设置权限成功") : MyJsonBean.error("设置权限失败");
        } catch (Exception e) {
            log.error("设置版本权限失败", e);
            return MyJsonBean.error("设置失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/removeVersionPermission")
    @ApiOperation("删除版本权限")
    public MyJsonBean removeVersionPermission(@RequestParam String versionId,
                                              @RequestParam String targetUserId,
                                              @RequestParam String permission,
                                              HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String removerId = request.getHeader("userId");
            boolean result = documentVersionService.removeVersionPermission(tenantId, versionId, targetUserId, permission, removerId);
            return result ? MyJsonBean.success("删除权限成功") : MyJsonBean.error("删除权限失败");
        } catch (Exception e) {
            log.error("删除版本权限失败", e);
            return MyJsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchSetPermissions")
    @ApiOperation("批量设置权限")
    public MyJsonBean batchSetPermissions(@RequestBody List<String> versionIds,
                                          @RequestBody Map<String, String> permissions,
                                          HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchSetPermissions(tenantId, versionIds, permissions, userId);
            return result ? MyJsonBean.success("批量设置权限成功") : MyJsonBean.error("批量设置权限失败");
        } catch (Exception e) {
            log.error("批量设置权限失败", e);
            return MyJsonBean.error("设置失败: " + e.getMessage());
        }
    }

    @PostMapping("/inheritParentPermissions")
    @ApiOperation("继承父版本权限")
    public MyJsonBean inheritParentPermissions(@RequestParam String versionId,
                                               @RequestParam String parentVersionId,
                                               HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.inheritParentPermissions(tenantId, versionId, parentVersionId, userId);
            return result ? MyJsonBean.success("继承权限成功") : MyJsonBean.error("继承权限失败");
        } catch (Exception e) {
            log.error("继承父版本权限失败", e);
            return MyJsonBean.error("继承失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作 ====================

    @PostMapping("/batchCreateVersions")
    @ApiOperation("批量创建版本")
    public MyJsonBean batchCreateVersions(@RequestBody List<AsDocumentVersion> versions, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            List<AsDocumentVersion> result = documentVersionService.batchCreateVersions(tenantId, versions, userId);
            return MyJsonBean.success("批量创建成功", result);
        } catch (Exception e) {
            log.error("批量创建版本失败", e);
            return MyJsonBean.error("批量创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchUpdateStatus")
    @ApiOperation("批量更新版本状态")
    public MyJsonBean batchUpdateStatus(@RequestBody List<String> versionIds,
                                        @RequestParam String status,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchUpdateStatus(tenantId, versionIds, status, userId);
            return result ? MyJsonBean.success("批量更新状态成功") : MyJsonBean.error("批量更新状态失败");
        } catch (Exception e) {
            log.error("批量更新版本状态失败", e);
            return MyJsonBean.error("批量更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchDeleteVersions")
    @ApiOperation("批量删除版本")
    public MyJsonBean batchDeleteVersions(@RequestBody List<String> versionIds, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchDeleteVersions(tenantId, versionIds, userId);
            return result ? MyJsonBean.success("批量删除成功") : MyJsonBean.error("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除版本失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchArchiveVersions")
    @ApiOperation("批量归档版本")
    public MyJsonBean batchArchiveVersions(@RequestBody List<String> versionIds, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchArchiveVersions(tenantId, versionIds, userId);
            return result ? MyJsonBean.success("批量归档成功") : MyJsonBean.error("批量归档失败");
        } catch (Exception e) {
            log.error("批量归档版本失败", e);
            return MyJsonBean.error("批量归档失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchPublishVersions")
    @ApiOperation("批量发布版本")
    public MyJsonBean batchPublishVersions(@RequestBody List<String> versionIds, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchPublishVersions(tenantId, versionIds, userId);
            return result ? MyJsonBean.success("批量发布成功") : MyJsonBean.error("批量发布失败");
        } catch (Exception e) {
            log.error("批量发布版本失败", e);
            return MyJsonBean.error("批量发布失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchLockVersions")
    @ApiOperation("批量锁定版本")
    public MyJsonBean batchLockVersions(@RequestBody List<String> versionIds,
                                        @RequestParam String lockReason,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchLockVersions(tenantId, versionIds, lockReason, userId);
            return result ? MyJsonBean.success("批量锁定成功") : MyJsonBean.error("批量锁定失败");
        } catch (Exception e) {
            log.error("批量锁定版本失败", e);
            return MyJsonBean.error("批量锁定失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchUnlockVersions")
    @ApiOperation("批量解锁版本")
    public MyJsonBean batchUnlockVersions(@RequestBody List<String> versionIds, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.batchUnlockVersions(tenantId, versionIds, userId);
            return result ? MyJsonBean.success("批量解锁成功") : MyJsonBean.error("批量解锁失败");
        } catch (Exception e) {
            log.error("批量解锁版本失败", e);
            return MyJsonBean.error("批量解锁失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析操作 ====================

    @GetMapping("/countVersions")
    @ApiOperation("统计版本数量")
    public MyJsonBean countVersions(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Long result = documentVersionService.countVersions(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("统计版本数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/countByStatus")
    @ApiOperation("按状态统计版本数量")
    public MyJsonBean countByStatus(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.countByStatus(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按状态统计版本数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/countByType")
    @ApiOperation("按类型统计版本数量")
    public MyJsonBean countByType(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.countByType(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按类型统计版本数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/countByDocumentType")
    @ApiOperation("按文档类型统计版本数量")
    public MyJsonBean countByDocumentType(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.countByDocumentType(tenantId);
            return MyJsonBean.success("统计成功", result);
        } catch (Exception e) {
            log.error("按文档类型统计版本数量失败", e);
            return MyJsonBean.error("统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionTrend")
    @ApiOperation("获取版本趋势数据")
    public MyJsonBean getVersionTrend(@RequestParam String startTime,
                                      @RequestParam String endTime,
                                      @RequestParam(defaultValue = "day") String granularity,
                                      HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            List<Map<String, Object>> result = documentVersionService.getVersionTrend(tenantId, start, end, granularity);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本趋势数据失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getUserActivityStats")
    @ApiOperation("获取用户活动统计")
    public MyJsonBean getUserActivityStats(@RequestParam String startTime,
                                           @RequestParam String endTime,
                                           HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);
            List<Map<String, Object>> result = documentVersionService.getUserActivityStats(tenantId, start, end);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取用户活动统计失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getPopularDocuments")
    @ApiOperation("获取热门文档排行")
    public MyJsonBean getPopularDocuments(@RequestParam(defaultValue = "10") Integer limit,
                                          HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.getPopularDocuments(tenantId, limit);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取热门文档排行失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getActiveUsers")
    @ApiOperation("获取活跃用户排行")
    public MyJsonBean getActiveUsers(@RequestParam(defaultValue = "10") Integer limit,
                                     HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.getActiveUsers(tenantId, limit);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取活跃用户排行失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/getStorageUsage")
    @ApiOperation("获取存储使用情况")
    public MyJsonBean getStorageUsage(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.getStorageUsage(tenantId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取存储使用情况失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    // ==================== 数据管理操作 ====================

    @PostMapping("/exportVersionData")
    @ApiOperation("导出版本数据")
    public MyJsonBean exportVersionData(@RequestBody List<String> versionIds,
                                        @RequestParam String exportFormat,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.exportVersionData(tenantId, versionIds, exportFormat);
            return MyJsonBean.success("导出成功", result);
        } catch (Exception e) {
            log.error("导出版本数据失败", e);
            return MyJsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @PostMapping("/importVersionData")
    @ApiOperation("导入版本数据")
    public MyJsonBean importVersionData(@RequestParam String importData,
                                        @RequestParam String importFormat,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            List<AsDocumentVersion> result = documentVersionService.importVersionData(tenantId, importData, importFormat, userId);
            return MyJsonBean.success("导入成功", result);
        } catch (Exception e) {
            log.error("导入版本数据失败", e);
            return MyJsonBean.error("导入失败: " + e.getMessage());
        }
    }

    @PostMapping("/cleanupExpiredVersions")
    @ApiOperation("清理过期版本")
    public MyJsonBean cleanupExpiredVersions(@RequestParam Integer retentionDays, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            int result = documentVersionService.cleanupExpiredVersions(tenantId, retentionDays);
            return MyJsonBean.success("清理成功", result);
        } catch (Exception e) {
            log.error("清理过期版本失败", e);
            return MyJsonBean.error("清理失败: " + e.getMessage());
        }
    }

    @PostMapping("/cleanupInvalidVersions")
    @ApiOperation("清理无效版本")
    public MyJsonBean cleanupInvalidVersions(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            int result = documentVersionService.cleanupInvalidVersions(tenantId);
            return MyJsonBean.success("清理成功", result);
        } catch (Exception e) {
            log.error("清理无效版本失败", e);
            return MyJsonBean.error("清理失败: " + e.getMessage());
        }
    }

    @PostMapping("/optimizeVersionStorage")
    @ApiOperation("优化版本存储")
    public MyJsonBean optimizeVersionStorage(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            boolean result = documentVersionService.optimizeVersionStorage(tenantId);
            return result ? MyJsonBean.success("优化成功") : MyJsonBean.error("优化失败");
        } catch (Exception e) {
            log.error("优化版本存储失败", e);
            return MyJsonBean.error("优化失败: " + e.getMessage());
        }
    }

    @PostMapping("/backupVersionData")
    @ApiOperation("备份版本数据")
    public MyJsonBean backupVersionData(@RequestBody List<String> versionIds,
                                        @RequestParam String backupLocation,
                                        HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            boolean result = documentVersionService.backupVersionData(tenantId, versionIds, backupLocation);
            return result ? MyJsonBean.success("备份成功") : MyJsonBean.error("备份失败");
        } catch (Exception e) {
            log.error("备份版本数据失败", e);
            return MyJsonBean.error("备份失败: " + e.getMessage());
        }
    }

    @PostMapping("/restoreVersionData")
    @ApiOperation("恢复版本数据")
    public MyJsonBean restoreVersionData(@RequestParam String backupLocation, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.restoreVersionData(tenantId, backupLocation, userId);
            return result ? MyJsonBean.success("恢复成功") : MyJsonBean.error("恢复失败");
        } catch (Exception e) {
            log.error("恢复版本数据失败", e);
            return MyJsonBean.error("恢复失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护操作 ====================

    @GetMapping("/getSystemOverview")
    @ApiOperation("获取系统概览")
    public MyJsonBean getSystemOverview(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.getSystemOverview(tenantId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取系统概览失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/generateVersionReport")
    @ApiOperation("生成版本报告")
    public MyJsonBean generateVersionReport(@RequestParam String reportType,
                                            @RequestBody Map<String, Object> params,
                                            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.generateVersionReport(tenantId, reportType, params);
            return MyJsonBean.success("生成报告成功", result);
        } catch (Exception e) {
            log.error("生成版本报告失败", e);
            return MyJsonBean.error("生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/checkSystemHealth")
    @ApiOperation("检查系统健康状态")
    public MyJsonBean checkSystemHealth(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.checkSystemHealth(tenantId);
            return MyJsonBean.success("检查成功", result);
        } catch (Exception e) {
            log.error("检查系统健康状态失败", e);
            return MyJsonBean.error("检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionQualityAssessment/{versionId}")
    @ApiOperation("获取版本质量评估")
    public MyJsonBean getVersionQualityAssessment(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.getVersionQualityAssessment(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本质量评估失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    @PostMapping("/executeMaintenanceTask")
    @ApiOperation("执行版本维护任务")
    public MyJsonBean executeMaintenanceTask(@RequestParam String taskType,
                                             @RequestBody Map<String, Object> taskParams,
                                             HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            boolean result = documentVersionService.executeMaintenanceTask(tenantId, taskType, taskParams);
            return result ? MyJsonBean.success("执行任务成功") : MyJsonBean.error("执行任务失败");
        } catch (Exception e) {
            log.error("执行版本维护任务失败", e);
            return MyJsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @GetMapping("/getMaintenanceTaskStatus/{taskId}")
    @ApiOperation("获取维护任务状态")
    public MyJsonBean getMaintenanceTaskStatus(@PathVariable String taskId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            Map<String, Object> result = documentVersionService.getMaintenanceTaskStatus(tenantId, taskId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取维护任务状态失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }

    // ==================== 通知操作 ====================

    @PostMapping("/sendVersionNotification")
    @ApiOperation("发送版本通知")
    public MyJsonBean sendVersionNotification(@RequestParam String versionId,
                                              @RequestParam String notificationType,
                                              @RequestBody List<String> recipients,
                                              @RequestParam String message,
                                              HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            boolean result = documentVersionService.sendVersionNotification(tenantId, versionId, notificationType, recipients, message);
            return result ? MyJsonBean.success("发送通知成功") : MyJsonBean.error("发送通知失败");
        } catch (Exception e) {
            log.error("发送版本通知失败", e);
            return MyJsonBean.error("发送失败: " + e.getMessage());
        }
    }

    @PostMapping("/subscribeVersionUpdates")
    @ApiOperation("订阅版本更新通知")
    public MyJsonBean subscribeVersionUpdates(@RequestParam String documentId,
                                              @RequestParam String notificationMethod,
                                              HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.subscribeVersionUpdates(tenantId, documentId, userId, notificationMethod);
            return result ? MyJsonBean.success("订阅成功") : MyJsonBean.error("订阅失败");
        } catch (Exception e) {
            log.error("订阅版本更新通知失败", e);
            return MyJsonBean.error("订阅失败: " + e.getMessage());
        }
    }

    @PostMapping("/unsubscribeVersionUpdates")
    @ApiOperation("取消订阅版本更新通知")
    public MyJsonBean unsubscribeVersionUpdates(@RequestParam String documentId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            boolean result = documentVersionService.unsubscribeVersionUpdates(tenantId, documentId, userId);
            return result ? MyJsonBean.success("取消订阅成功") : MyJsonBean.error("取消订阅失败");
        } catch (Exception e) {
            log.error("取消订阅版本更新通知失败", e);
            return MyJsonBean.error("取消订阅失败: " + e.getMessage());
        }
    }

    @GetMapping("/getVersionNotificationHistory/{versionId}")
    @ApiOperation("获取版本通知历史")
    public MyJsonBean getVersionNotificationHistory(@PathVariable String versionId, HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            List<Map<String, Object>> result = documentVersionService.getVersionNotificationHistory(tenantId, versionId);
            return MyJsonBean.success("获取成功", result);
        } catch (Exception e) {
            log.error("获取版本通知历史失败", e);
            return MyJsonBean.error("获取失败: " + e.getMessage());
        }
    }
}
