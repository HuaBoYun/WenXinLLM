package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetVersion;
import com.management.accountant.service.eps.EpsBudgetVersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算版本管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算版本管理")
@RestController
@RequestMapping("/eps/budget-version")
@Validated
public class EpsBudgetVersionController {

    @Autowired
    private EpsBudgetVersionService budgetVersionService;

    /**
     * 分页查询预算版本
     */
    @ApiOperation("分页查询预算版本")
    @GetMapping("/page")
    public MyJsonBean<IPage<EpsBudgetVersion>> queryBudgetVersionPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("版本名称") @RequestParam(required = false) String versionName,
            @ApiParam("版本状态") @RequestParam(required = false) String versionStatus,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("预算年度") @RequestParam(required = false) Integer budgetYear) {
        try {
            IPage<EpsBudgetVersion> result = budgetVersionService.queryBudgetVersionPage(
                    current, size, versionName, versionStatus, systemId, budgetYear);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询预算版本失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建预算版本
     */
    @ApiOperation("创建预算版本")
    @PostMapping
    public MyJsonBean<Boolean> createBudgetVersion(
            @ApiParam("预算版本") @RequestBody @Valid EpsBudgetVersion budgetVersion) {
        try {
            boolean result = budgetVersionService.createBudgetVersion(budgetVersion);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建预算版本失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新预算版本
     */
    @ApiOperation("更新预算版本")
    @PutMapping
    public MyJsonBean<Boolean> updateBudgetVersion(
            @ApiParam("预算版本") @RequestBody @Valid EpsBudgetVersion budgetVersion) {
        try {
            boolean result = budgetVersionService.updateBudgetVersion(budgetVersion);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新预算版本失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除预算版本
     */
    @ApiOperation("删除预算版本")
    @DeleteMapping("/{versionId}")
    public MyJsonBean<Boolean> deleteBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            boolean result = budgetVersionService.deleteBudgetVersion(versionId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除预算版本失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除预算版本
     */
    @ApiOperation("批量删除预算版本")
    @DeleteMapping("/batch")
    public MyJsonBean<Boolean> batchDeleteBudgetVersions(
            @ApiParam("版本ID列表") @RequestBody List<Long> versionIds) {
        try {
            boolean result = budgetVersionService.batchDeleteBudgetVersions(versionIds);
            if (result) {
                return MyJsonBean.success("批量删除成功", true);
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除预算版本失败", e);
            return MyJsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询预算版本详情
     */
    @ApiOperation("根据ID查询预算版本详情")
    @GetMapping("/{versionId}")
    public MyJsonBean<EpsBudgetVersion> getBudgetVersionById(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            EpsBudgetVersion result = budgetVersionService.getBudgetVersionById(versionId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("版本不存在");
            }
        } catch (Exception e) {
            log.error("获取预算版本详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据编码查询预算版本
     */
    @ApiOperation("根据编码查询预算版本")
    @GetMapping("/code/{versionCode}")
    public MyJsonBean<EpsBudgetVersion> getBudgetVersionByCode(
            @ApiParam("版本编码") @PathVariable @NotNull String versionCode) {
        try {
            EpsBudgetVersion result = budgetVersionService.getBudgetVersionByCode(versionCode);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("版本不存在");
            }
        } catch (Exception e) {
            log.error("根据编码获取预算版本失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 根据预算体系查询版本列表
     */
    @ApiOperation("根据预算体系查询版本列表")
    @GetMapping("/system/{systemId}")
    public MyJsonBean<List<EpsBudgetVersion>> getBudgetVersionsBySystemId(
            @ApiParam("预算体系ID") @PathVariable @NotNull Long systemId) {
        try {
            List<EpsBudgetVersion> result = budgetVersionService.getBudgetVersionsBySystemId(systemId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("根据预算体系查询版本列表失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询当前版本
     */
    @ApiOperation("查询当前版本")
    @GetMapping("/current/{systemId}")
    public MyJsonBean<EpsBudgetVersion> getCurrentBudgetVersion(
            @ApiParam("预算体系ID") @PathVariable @NotNull Long systemId) {
        try {
            EpsBudgetVersion result = budgetVersionService.getCurrentBudgetVersion(systemId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("未找到当前版本");
            }
        } catch (Exception e) {
            log.error("查询当前版本失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 设置当前版本
     */
    @ApiOperation("设置当前版本")
    @PutMapping("/current/{versionId}")
    public MyJsonBean<Boolean> setCurrentBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            boolean result = budgetVersionService.setCurrentBudgetVersion(versionId);
            if (result) {
                return MyJsonBean.success("设置当前版本成功", true);
            } else {
                return MyJsonBean.error("设置当前版本失败");
            }
        } catch (Exception e) {
            log.error("设置当前版本失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 发布预算版本
     */
    @ApiOperation("发布预算版本")
    @PostMapping("/{versionId}/publish")
    public MyJsonBean<Boolean> publishBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId,
            @ApiParam("发布信息") @RequestBody(required = false) Map<String, Object> publishInfo) {
        try {
            boolean result = budgetVersionService.publishBudgetVersion(versionId, publishInfo);
            if (result) {
                return MyJsonBean.success("发布成功", true);
            } else {
                return MyJsonBean.error("发布失败");
            }
        } catch (Exception e) {
            log.error("发布预算版本失败", e);
            return MyJsonBean.error("发布失败：" + e.getMessage());
        }
    }

    /**
     * 撤销发布预算版本
     */
    @ApiOperation("撤销发布预算版本")
    @PostMapping("/{versionId}/unpublish")
    public MyJsonBean<Boolean> unpublishBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId,
            @ApiParam("撤销原因") @RequestParam(required = false) String reason) {
        try {
            boolean result = budgetVersionService.unpublishBudgetVersion(versionId, reason);
            if (result) {
                return MyJsonBean.success("撤销发布成功", true);
            } else {
                return MyJsonBean.error("撤销发布失败");
            }
        } catch (Exception e) {
            log.error("撤销发布预算版本失败", e);
            return MyJsonBean.error("撤销失败：" + e.getMessage());
        }
    }

    /**
     * 锁定预算版本
     */
    @ApiOperation("锁定预算版本")
    @PostMapping("/{versionId}/lock")
    public MyJsonBean<Boolean> lockBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId,
            @ApiParam("锁定原因") @RequestParam(required = false) String lockReason) {
        try {
            boolean result = budgetVersionService.lockBudgetVersion(versionId, lockReason);
            if (result) {
                return MyJsonBean.success("锁定成功", true);
            } else {
                return MyJsonBean.error("锁定失败");
            }
        } catch (Exception e) {
            log.error("锁定预算版本失败", e);
            return MyJsonBean.error("锁定失败：" + e.getMessage());
        }
    }

    /**
     * 解锁预算版本
     */
    @ApiOperation("解锁预算版本")
    @PostMapping("/{versionId}/unlock")
    public MyJsonBean<Boolean> unlockBudgetVersion(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            boolean result = budgetVersionService.unlockBudgetVersion(versionId);
            if (result) {
                return MyJsonBean.success("解锁成功", true);
            } else {
                return MyJsonBean.error("解锁失败");
            }
        } catch (Exception e) {
            log.error("解锁预算版本失败", e);
            return MyJsonBean.error("解锁失败：" + e.getMessage());
        }
    }

    /**
     * 复制预算版本
     */
    @ApiOperation("复制预算版本")
    @PostMapping("/{versionId}/copy")
    public MyJsonBean<Boolean> copyBudgetVersion(
            @ApiParam("源版本ID") @PathVariable @NotNull Long versionId,
            @ApiParam("目标版本编码") @RequestParam @NotNull String targetVersionCode,
            @ApiParam("目标版本名称") @RequestParam @NotNull String targetVersionName,
            @ApiParam("是否复制数据") @RequestParam(defaultValue = "false") Boolean copyData) {
        try {
            boolean result = budgetVersionService.copyBudgetVersion(versionId, targetVersionCode, targetVersionName, copyData);
            if (result) {
                return MyJsonBean.success("复制成功", true);
            } else {
                return MyJsonBean.error("复制失败");
            }
        } catch (Exception e) {
            log.error("复制预算版本失败", e);
            return MyJsonBean.error("复制失败：" + e.getMessage());
        }
    }

    /**
     * 获取版本比较
     */
    @ApiOperation("获取版本比较")
    @GetMapping("/compare")
    public MyJsonBean<Map<String, Object>> compareBudgetVersions(
            @ApiParam("源版本ID") @RequestParam @NotNull Long sourceVersionId,
            @ApiParam("目标版本ID") @RequestParam @NotNull Long targetVersionId,
            @ApiParam("比较类型") @RequestParam(defaultValue = "ALL") String compareType) {
        try {
            Map<String, Object> result = budgetVersionService.compareBudgetVersions(sourceVersionId, targetVersionId, compareType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("版本比较失败", e);
            return MyJsonBean.error("比较失败：" + e.getMessage());
        }
    }

    /**
     * 获取版本历史
     */
    @ApiOperation("获取版本历史")
    @GetMapping("/{versionId}/history")
    public MyJsonBean<List<Map<String, Object>>> getBudgetVersionHistory(
            @ApiParam("版本ID") @PathVariable @NotNull Long versionId) {
        try {
            List<Map<String, Object>> result = budgetVersionService.getBudgetVersionHistory(versionId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取版本历史失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取版本统计
     */
    @ApiOperation("获取版本统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBudgetVersionStatistics(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("预算年度") @RequestParam(required = false) Integer budgetYear) {
        try {
            Map<String, Object> result = budgetVersionService.getBudgetVersionStatistics(systemId, budgetYear);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取版本统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作版本
     */
    @ApiOperation("批量操作版本")
    @PostMapping("/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateVersions(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = budgetVersionService.batchOperateVersions(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作版本失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }
}
