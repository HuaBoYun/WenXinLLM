package com.huabo.fxgl.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.ConstClass;
import com.hbfk.util.JsonBean;
import com.hbfk.util.StringUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.DataModelVersionCreateDTO;
import com.huabo.fxgl.dto.DataModelVersionQueryDTO;
import com.huabo.fxgl.entity.TblDataModelVersion;
import com.huabo.fxgl.service.DataModelVersionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 数据模型版本管理控制器
 *
 * @author AI Assistant
 * @since 2025-09-28
 */
@Slf4j
@RestController
@RequestMapping("/model")
@Tag(name="数据模型版本管理",description="数据模型版本管理")
public class ModelVersionController {

    @Autowired
    private DataModelVersionService versionService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询模型版本列表
     */
    @PostMapping("/version/list")
    @Operation(summary = "分页查询模型版本列表", description = "根据条件分页查询模型版本列表")
    public String getVersionList(@RequestHeader("token") String token,
                                @RequestBody DataModelVersionQueryDTO queryDTO) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            IPage<TblDataModelVersion> pageInfo = versionService.getVersionPage(queryDTO);
            return JsonBean.success("查询成功", pageInfo.getRecords()).toString();

        } catch (Exception e) {
            log.error("查询模型版本列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 查询模型的所有版本
     */
    @RequestMapping(value = "/version/all/{modelId}", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "查询模型的所有版本", description = "查询指定模型的所有版本")
    public String getAllVersions(@RequestHeader("token") String token,
                                @PathVariable String modelId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            if (StringUtil.isEmpty(modelId)) {
                return JsonBean.error("模型ID不能为空").toString();
            }

            List<TblDataModelVersion> versions = versionService.getVersionsByModelId(modelId);
            return JsonBean.success("查询成功", versions).toString();

        } catch (Exception e) {
            log.error("查询模型版本失败: modelId={}", modelId, e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 创建新版本
     */
    @PostMapping("/version/create")
    @Operation(summary = "创建新版本", description = "基于当前模型创建新版本")
    public String createVersion(@RequestHeader("token") String token,
                               @RequestParam String modelId,
                               @RequestParam String versionNo,
                               @RequestParam(required = false) String changeDescription) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String createUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            // 构建DTO对象
            DataModelVersionCreateDTO createDTO = new DataModelVersionCreateDTO();
            createDTO.setModelId(modelId);
            createDTO.setVersionNo(versionNo);
            createDTO.setChangeDescription(changeDescription);

            TblDataModelVersion version = versionService.createVersion(createDTO, createUser);
            return JsonBean.success("创建版本成功", version).toString();

        } catch (Exception e) {
            log.error("创建模型版本失败", e);
            return JsonBean.error("创建失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 删除版本
     */
    @PostMapping("/version/delete/{versionId}")
    @Operation(summary = "删除版本", description = "删除指定版本")
    public String deleteVersion(@RequestHeader("token") String token, 
                               @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String deleteUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            boolean success = versionService.deleteVersion(versionId, deleteUser);
            if (success) {
                return JsonBean.success("删除成功").toString();
            } else {
                return JsonBean.error("删除失败").toString();
            }

        } catch (Exception e) {
            log.error("删除模型版本失败: versionId={}", versionId, e);
            return JsonBean.error("删除失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 发布版本
     */
    @PostMapping("/version/publish/{versionId}")
    @Operation(summary = "发布版本", description = "发布指定版本")
    public String publishVersion(@RequestHeader("token") String token, 
                                @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String publishUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            boolean success = versionService.publishVersion(versionId, publishUser);
            if (success) {
                return JsonBean.success("发布成功").toString();
            } else {
                return JsonBean.error("发布失败").toString();
            }

        } catch (Exception e) {
            log.error("发布模型版本失败: versionId={}", versionId, e);
            return JsonBean.error("发布失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 归档版本
     */
    @PostMapping("/version/archive/{versionId}")
    @Operation(summary = "归档版本", description = "归档指定版本")
    public String archiveVersion(@RequestHeader("token") String token, 
                                @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String archiveUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            boolean success = versionService.archiveVersion(versionId, archiveUser);
            if (success) {
                return JsonBean.success("归档成功").toString();
            } else {
                return JsonBean.error("归档失败").toString();
            }

        } catch (Exception e) {
            log.error("归档模型版本失败: versionId={}", versionId, e);
            return JsonBean.error("归档失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 设置当前版本
     */
    @PostMapping("/version/setCurrent/{versionId}")
    @Operation(summary = "设置当前版本", description = "设置指定版本为当前版本")
    public String setCurrentVersion(@RequestHeader("token") String token,
                                   @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String updateUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            boolean success = versionService.setCurrentVersion(versionId, updateUser);
            if (success) {
                return JsonBean.success("设置成功").toString();
            } else {
                return JsonBean.error("设置失败").toString();
            }

        } catch (Exception e) {
            log.error("设置当前版本失败: versionId={}", versionId, e);
            return JsonBean.error("设置失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 回滚到指定版本
     */
    @PostMapping("/version/rollback/{versionId}")
    @Operation(summary = "回滚版本", description = "回滚到指定版本")
    public String rollbackToVersion(@RequestHeader("token") String token,
                                   @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            // 获取当前用户信息
            String rollbackUser = staffUtil.getRealname() != null ? staffUtil.getRealname() : "系统用户";

            boolean success = versionService.rollbackToVersion(versionId, rollbackUser);
            if (success) {
                return JsonBean.success("回滚成功").toString();
            } else {
                return JsonBean.error("回滚失败").toString();
            }

        } catch (Exception e) {
            log.error("回滚版本失败: versionId={}", versionId, e);
            return JsonBean.error("回滚失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 获取版本详情
     */
    @PostMapping("/version/detail/{versionId}")
    @Operation(summary = "获取版本详情", description = "获取指定版本的详细信息")
    public String getVersionDetail(@RequestHeader("token") String token, 
                                  @PathVariable String versionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            TblDataModelVersion version = versionService.getVersionDetail(versionId);
            if (version != null) {
                return JsonBean.success("查询成功", version).toString();
            } else {
                return JsonBean.error("版本不存在").toString();
            }

        } catch (Exception e) {
            log.error("获取版本详情失败: versionId={}", versionId, e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 比较两个版本
     */
    @PostMapping("/version/compare")
    @Operation(summary = "比较版本", description = "比较两个版本的差异")
    public String compareVersions(@RequestHeader("token") String token, 
                                 @RequestParam String sourceVersionId,
                                 @RequestParam String targetVersionId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            if (StringUtil.isEmpty(sourceVersionId) || StringUtil.isEmpty(targetVersionId)) {
                return JsonBean.error("版本ID不能为空").toString();
            }

            Map<String, Object> result = versionService.compareVersions(sourceVersionId, targetVersionId);
            return JsonBean.success("比较成功", result).toString();

        } catch (Exception e) {
            log.error("比较版本失败: sourceVersionId={}, targetVersionId={}", sourceVersionId, targetVersionId, e);
            return JsonBean.error("比较失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 获取版本统计信息
     */
    @PostMapping("/version/statistics/{modelId}")
    @Operation(summary = "获取版本统计", description = "获取模型的版本统计信息")
    public String getVersionStatistics(@RequestHeader("token") String token, 
                                      @PathVariable String modelId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            Map<String, Object> statistics = versionService.getVersionStatistics(modelId);
            return JsonBean.success("查询成功", statistics).toString();

        } catch (Exception e) {
            log.error("获取版本统计失败: modelId={}", modelId, e);
            return JsonBean.error("查询失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 检查版本号是否可用
     */
    @PostMapping("/version/checkVersionNo")
    @Operation(summary = "检查版本号", description = "检查版本号是否可用")
    public String checkVersionNo(@RequestHeader("token") String token, 
                                @RequestParam String modelId,
                                @RequestParam String versionNo) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            if (StringUtil.isEmpty(modelId) || StringUtil.isEmpty(versionNo)) {
                return JsonBean.error("参数不能为空").toString();
            }

            boolean available = versionService.isVersionNoAvailable(modelId, versionNo);
            return JsonBean.success("检查完成", available).toString();

        } catch (Exception e) {
            log.error("检查版本号失败: modelId={}, versionNo={}", modelId, versionNo, e);
            return JsonBean.error("检查失败: " + e.getMessage()).toString();
        }
    }

    /**
     * 获取下一个版本号
     */
    @PostMapping("/version/nextVersionNo/{modelId}")
    @Operation(summary = "获取下一个版本号", description = "获取模型的下一个版本号")
    public String getNextVersionNo(@RequestHeader("token") String token, 
                                  @PathVariable String modelId) {
        try {
            // Token验证 - 使用userProvider方式
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return ConstClass.tokenFailure().toString();
            }

            String nextVersionNo = versionService.getNextVersionNo(modelId);
            return JsonBean.success("获取成功", nextVersionNo).toString();

        } catch (Exception e) {
            log.error("获取下一个版本号失败: modelId={}", modelId, e);
            return JsonBean.error("获取失败: " + e.getMessage()).toString();
        }
    }
}
