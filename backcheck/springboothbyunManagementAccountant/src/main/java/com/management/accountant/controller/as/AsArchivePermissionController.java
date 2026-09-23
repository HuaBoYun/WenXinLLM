package com.management.accountant.controller.as;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.as.AsArchivePermission;
import com.management.accountant.service.as.AsArchivePermissionService;
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
 * 档案权限管理控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/accountant/as/archivePermission")
@Api(tags = "档案权限管理")
public class AsArchivePermissionController {

    @Autowired
    private AsArchivePermissionService archivePermissionService;

    // ==================== 基本CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建权限")
    public MyJsonBean createPermission(
            @ApiParam("权限信息") @RequestBody AsArchivePermission permission,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.createPermission(tenantId, permission, userId);
            if (result) {
                return MyJsonBean.success("权限创建成功", permission);
            } else {
                return MyJsonBean.error("权限创建失败");
            }
        } catch (Exception e) {
            log.error("创建权限异常", e);
            return MyJsonBean.error("创建权限异常：" + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新权限")
    public MyJsonBean updatePermission(
            @ApiParam("权限信息") @RequestBody AsArchivePermission permission,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.updatePermission(tenantId, permission, userId);
            if (result) {
                return MyJsonBean.success("权限更新成功", permission);
            } else {
                return MyJsonBean.error("权限更新失败");
            }
        } catch (Exception e) {
            log.error("更新权限异常", e);
            return MyJsonBean.error("更新权限异常：" + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{permissionId}")
    @ApiOperation("删除权限")
    public MyJsonBean deletePermission(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.deletePermission(tenantId, permissionId, userId);
            if (result) {
                return MyJsonBean.success("权限删除成功");
            } else {
                return MyJsonBean.error("权限删除失败");
            }
        } catch (Exception e) {
            log.error("删除权限异常", e);
            return MyJsonBean.error("删除权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/get/{permissionId}")
    @ApiOperation("获取权限详情")
    public MyJsonBean getPermissionById(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            AsArchivePermission permission = archivePermissionService.getPermissionById(tenantId, permissionId);
            if (permission != null) {
                return MyJsonBean.success("获取权限详情成功", permission);
            } else {
                return MyJsonBean.error("权限不存在");
            }
        } catch (Exception e) {
            log.error("获取权限详情异常", e);
            return MyJsonBean.error("获取权限详情异常：" + e.getMessage());
        }
    }

    @GetMapping("/getByCode/{permissionCode}")
    @ApiOperation("根据编码获取权限")
    public MyJsonBean getPermissionByCode(
            @ApiParam("权限编码") @PathVariable String permissionCode,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            AsArchivePermission permission = archivePermissionService.getPermissionByCode(tenantId, permissionCode);
            if (permission != null) {
                return MyJsonBean.success("获取权限成功", permission);
            } else {
                return MyJsonBean.error("权限不存在");
            }
        } catch (Exception e) {
            log.error("根据编码获取权限异常", e);
            return MyJsonBean.error("根据编码获取权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/page")
    @ApiOperation("分页查询权限")
    public MyJsonBean getPermissionPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestBody(required = false) Map<String, Object> params,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            Page<AsArchivePermission> page = new Page<>(current, size);
            page = (Page<AsArchivePermission>) archivePermissionService.getPermissionPage(tenantId, page, params);
            
            return MyJsonBean.success("分页查询权限成功", page);
        } catch (Exception e) {
            log.error("分页查询权限异常", e);
            return MyJsonBean.error("分页查询权限异常：" + e.getMessage());
        }
    }

    // ==================== 权限验证操作 ====================

    @GetMapping("/checkUserPermission")
    @ApiOperation("检查用户权限")
    public MyJsonBean checkUserPermission(
            @ApiParam("用户ID") @RequestParam String userId,
            @ApiParam("资源类型") @RequestParam String resourceType,
            @ApiParam("资源ID") @RequestParam String resourceId,
            @ApiParam("操作") @RequestParam String operation,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            boolean hasPermission = archivePermissionService.checkUserPermission(tenantId, userId, resourceType, resourceId, operation);
            return MyJsonBean.success("权限检查完成", hasPermission);
        } catch (Exception e) {
            log.error("检查用户权限异常", e);
            return MyJsonBean.error("检查用户权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/checkRolePermission")
    @ApiOperation("检查角色权限")
    public MyJsonBean checkRolePermission(
            @ApiParam("角色ID") @RequestParam String roleId,
            @ApiParam("资源类型") @RequestParam String resourceType,
            @ApiParam("资源ID") @RequestParam String resourceId,
            @ApiParam("操作") @RequestParam String operation,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            boolean hasPermission = archivePermissionService.checkRolePermission(tenantId, roleId, resourceType, resourceId, operation);
            return MyJsonBean.success("权限检查完成", hasPermission);
        } catch (Exception e) {
            log.error("检查角色权限异常", e);
            return MyJsonBean.error("检查角色权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/getUserEffectivePermissions")
    @ApiOperation("获取用户有效权限")
    public MyJsonBean getUserEffectivePermissions(
            @ApiParam("用户ID") @RequestParam String userId,
            @ApiParam("资源类型") @RequestParam(required = false) String resourceType,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            List<AsArchivePermission> permissions = archivePermissionService.getUserEffectivePermissions(tenantId, userId, resourceType);
            return MyJsonBean.success("获取用户有效权限成功", permissions);
        } catch (Exception e) {
            log.error("获取用户有效权限异常", e);
            return MyJsonBean.error("获取用户有效权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/getRoleEffectivePermissions")
    @ApiOperation("获取角色有效权限")
    public MyJsonBean getRoleEffectivePermissions(
            @ApiParam("角色ID") @RequestParam String roleId,
            @ApiParam("资源类型") @RequestParam(required = false) String resourceType,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            List<AsArchivePermission> permissions = archivePermissionService.getRoleEffectivePermissions(tenantId, roleId, resourceType);
            return MyJsonBean.success("获取角色有效权限成功", permissions);
        } catch (Exception e) {
            log.error("获取角色有效权限异常", e);
            return MyJsonBean.error("获取角色有效权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/getResourcePermissions")
    @ApiOperation("获取资源权限列表")
    public MyJsonBean getResourcePermissions(
            @ApiParam("资源类型") @RequestParam String resourceType,
            @ApiParam("资源ID") @RequestParam String resourceId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            
            List<AsArchivePermission> permissions = archivePermissionService.getResourcePermissions(tenantId, resourceType, resourceId);
            return MyJsonBean.success("获取资源权限列表成功", permissions);
        } catch (Exception e) {
            log.error("获取资源权限列表异常", e);
            return MyJsonBean.error("获取资源权限列表异常：" + e.getMessage());
        }
    }

    // ==================== 权限管理操作 ====================

    @PostMapping("/grant")
    @ApiOperation("授予权限")
    public MyJsonBean grantPermission(
            @ApiParam("主体类型") @RequestParam String subjectType,
            @ApiParam("主体ID") @RequestParam String subjectId,
            @ApiParam("资源类型") @RequestParam String resourceType,
            @ApiParam("资源ID") @RequestParam String resourceId,
            @ApiParam("操作权限") @RequestParam String operations,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.grantPermission(tenantId, subjectType, subjectId, resourceType, resourceId, operations, userId);
            if (result) {
                return MyJsonBean.success("权限授予成功");
            } else {
                return MyJsonBean.error("权限授予失败");
            }
        } catch (Exception e) {
            log.error("授予权限异常", e);
            return MyJsonBean.error("授予权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/revoke/{permissionId}")
    @ApiOperation("撤销权限")
    public MyJsonBean revokePermission(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.revokePermission(tenantId, permissionId, userId);
            if (result) {
                return MyJsonBean.success("权限撤销成功");
            } else {
                return MyJsonBean.error("权限撤销失败");
            }
        } catch (Exception e) {
            log.error("撤销权限异常", e);
            return MyJsonBean.error("撤销权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/batchGrant")
    @ApiOperation("批量授予权限")
    public MyJsonBean batchGrantPermissions(
            @ApiParam("主体类型") @RequestParam String subjectType,
            @ApiParam("主体ID") @RequestParam String subjectId,
            @ApiParam("权限列表") @RequestBody List<Map<String, Object>> permissions,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.batchGrantPermissions(tenantId, subjectType, subjectId, permissions, userId);
            if (result) {
                return MyJsonBean.success("批量授予权限成功");
            } else {
                return MyJsonBean.error("批量授予权限失败");
            }
        } catch (Exception e) {
            log.error("批量授予权限异常", e);
            return MyJsonBean.error("批量授予权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/batchRevoke")
    @ApiOperation("批量撤销权限")
    public MyJsonBean batchRevokePermissions(
            @ApiParam("主体类型") @RequestParam String subjectType,
            @ApiParam("主体ID") @RequestParam String subjectId,
            @ApiParam("权限ID列表") @RequestBody List<Long> permissionIds,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.batchRevokePermissions(tenantId, subjectType, subjectId, permissionIds, userId);
            if (result) {
                return MyJsonBean.success("批量撤销权限成功");
            } else {
                return MyJsonBean.error("批量撤销权限失败");
            }
        } catch (Exception e) {
            log.error("批量撤销权限异常", e);
            return MyJsonBean.error("批量撤销权限异常：" + e.getMessage());
        }
    }

    @PutMapping("/updateStatus/{permissionId}")
    @ApiOperation("更新权限状态")
    public MyJsonBean updatePermissionStatus(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            @ApiParam("状态") @RequestParam String status,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");
            
            boolean result = archivePermissionService.updatePermissionStatus(tenantId, permissionId, status, userId);
            if (result) {
                return MyJsonBean.success("权限状态更新成功");
            } else {
                return MyJsonBean.error("权限状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新权限状态异常", e);
            return MyJsonBean.error("更新权限状态异常：" + e.getMessage());
        }
    }

    @PutMapping("/batchUpdateStatus")
    @ApiOperation("批量更新权限状态")
    public MyJsonBean batchUpdatePermissionStatus(
            @ApiParam("权限ID列表") @RequestBody List<Long> permissionIds,
            @ApiParam("状态") @RequestParam String status,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.batchUpdatePermissionStatus(tenantId, permissionIds, status, userId);
            if (result) {
                return MyJsonBean.success("批量更新权限状态成功");
            } else {
                return MyJsonBean.error("批量更新权限状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新权限状态异常", e);
            return MyJsonBean.error("批量更新权限状态异常：" + e.getMessage());
        }
    }

    // ==================== 权限继承和委托 ====================

    @PostMapping("/inherit")
    @ApiOperation("继承父权限")
    public MyJsonBean inheritParentPermissions(
            @ApiParam("父权限ID") @RequestParam Long parentPermissionId,
            @ApiParam("子主体ID") @RequestParam String childSubjectId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.inheritParentPermissions(tenantId, parentPermissionId, childSubjectId, userId);
            if (result) {
                return MyJsonBean.success("继承父权限成功");
            } else {
                return MyJsonBean.error("继承父权限失败");
            }
        } catch (Exception e) {
            log.error("继承父权限异常", e);
            return MyJsonBean.error("继承父权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/delegate")
    @ApiOperation("委托权限")
    public MyJsonBean delegatePermission(
            @ApiParam("权限ID") @RequestParam Long permissionId,
            @ApiParam("委托人ID") @RequestParam String delegatorId,
            @ApiParam("被委托人ID") @RequestParam String delegateId,
            @ApiParam("生效时间") @RequestParam(required = false) LocalDateTime effectiveTime,
            @ApiParam("失效时间") @RequestParam(required = false) LocalDateTime expiryTime,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            boolean result = archivePermissionService.delegatePermission(tenantId, permissionId, delegatorId, delegateId, effectiveTime, expiryTime);
            if (result) {
                return MyJsonBean.success("权限委托成功");
            } else {
                return MyJsonBean.error("权限委托失败");
            }
        } catch (Exception e) {
            log.error("委托权限异常", e);
            return MyJsonBean.error("委托权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/cancelDelegation/{permissionId}")
    @ApiOperation("取消权限委托")
    public MyJsonBean cancelPermissionDelegation(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.cancelPermissionDelegation(tenantId, permissionId, userId);
            if (result) {
                return MyJsonBean.success("取消权限委托成功");
            } else {
                return MyJsonBean.error("取消权限委托失败");
            }
        } catch (Exception e) {
            log.error("取消权限委托异常", e);
            return MyJsonBean.error("取消权限委托异常：" + e.getMessage());
        }
    }

    @GetMapping("/getDelegatedPermissions")
    @ApiOperation("获取委托权限列表")
    public MyJsonBean getDelegatedPermissions(
            @ApiParam("被委托人ID") @RequestParam String delegateId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<AsArchivePermission> permissions = archivePermissionService.getDelegatedPermissions(tenantId, delegateId);
            return MyJsonBean.success("获取委托权限列表成功", permissions);
        } catch (Exception e) {
            log.error("获取委托权限列表异常", e);
            return MyJsonBean.error("获取委托权限列表异常：" + e.getMessage());
        }
    }

    // ==================== 权限层次管理 ====================

    @GetMapping("/getParent/{permissionId}")
    @ApiOperation("获取父权限")
    public MyJsonBean getParentPermission(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            AsArchivePermission parentPermission = archivePermissionService.getParentPermission(tenantId, permissionId);
            return MyJsonBean.success("获取父权限成功", parentPermission);
        } catch (Exception e) {
            log.error("获取父权限异常", e);
            return MyJsonBean.error("获取父权限异常：" + e.getMessage());
        }
    }

    @GetMapping("/getChildren/{parentPermissionId}")
    @ApiOperation("获取子权限列表")
    public MyJsonBean getChildPermissions(
            @ApiParam("父权限ID") @PathVariable Long parentPermissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<AsArchivePermission> childPermissions = archivePermissionService.getChildPermissions(tenantId, parentPermissionId);
            return MyJsonBean.success("获取子权限列表成功", childPermissions);
        } catch (Exception e) {
            log.error("获取子权限列表异常", e);
            return MyJsonBean.error("获取子权限列表异常：" + e.getMessage());
        }
    }

    @GetMapping("/getTree/{rootPermissionId}")
    @ApiOperation("获取权限树结构")
    public MyJsonBean getPermissionTree(
            @ApiParam("根权限ID") @PathVariable Long rootPermissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<AsArchivePermission> permissionTree = archivePermissionService.getPermissionTree(tenantId, rootPermissionId);
            return MyJsonBean.success("获取权限树结构成功", permissionTree);
        } catch (Exception e) {
            log.error("获取权限树结构异常", e);
            return MyJsonBean.error("获取权限树结构异常：" + e.getMessage());
        }
    }

    @GetMapping("/getPath/{permissionId}")
    @ApiOperation("获取权限路径")
    public MyJsonBean getPermissionPath(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<AsArchivePermission> permissionPath = archivePermissionService.getPermissionPath(tenantId, permissionId);
            return MyJsonBean.success("获取权限路径成功", permissionPath);
        } catch (Exception e) {
            log.error("获取权限路径异常", e);
            return MyJsonBean.error("获取权限路径异常：" + e.getMessage());
        }
    }

    // ==================== 权限审批操作 ====================

    @PostMapping("/submitApproval/{permissionId}")
    @ApiOperation("提交权限审批")
    public MyJsonBean submitPermissionApproval(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.submitPermissionApproval(tenantId, permissionId, userId);
            if (result) {
                return MyJsonBean.success("提交权限审批成功");
            } else {
                return MyJsonBean.error("提交权限审批失败");
            }
        } catch (Exception e) {
            log.error("提交权限审批异常", e);
            return MyJsonBean.error("提交权限审批异常：" + e.getMessage());
        }
    }

    @PostMapping("/approve/{permissionId}")
    @ApiOperation("审批权限")
    public MyJsonBean approvePermission(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            @ApiParam("审批意见") @RequestParam(required = false) String approvalComment,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.approvePermission(tenantId, permissionId, userId, approvalComment);
            if (result) {
                return MyJsonBean.success("权限审批成功");
            } else {
                return MyJsonBean.error("权限审批失败");
            }
        } catch (Exception e) {
            log.error("审批权限异常", e);
            return MyJsonBean.error("审批权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/reject/{permissionId}")
    @ApiOperation("拒绝权限")
    public MyJsonBean rejectPermission(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            @ApiParam("拒绝原因") @RequestParam String rejectionReason,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.rejectPermission(tenantId, permissionId, userId, rejectionReason);
            if (result) {
                return MyJsonBean.success("权限拒绝成功");
            } else {
                return MyJsonBean.error("权限拒绝失败");
            }
        } catch (Exception e) {
            log.error("拒绝权限异常", e);
            return MyJsonBean.error("拒绝权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/cancelApproval/{permissionId}")
    @ApiOperation("取消权限审批")
    public MyJsonBean cancelPermissionApproval(
            @ApiParam("权限ID") @PathVariable Long permissionId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            boolean result = archivePermissionService.cancelPermissionApproval(tenantId, permissionId, userId);
            if (result) {
                return MyJsonBean.success("取消权限审批成功");
            } else {
                return MyJsonBean.error("取消权限审批失败");
            }
        } catch (Exception e) {
            log.error("取消权限审批异常", e);
            return MyJsonBean.error("取消权限审批异常：" + e.getMessage());
        }
    }

    @PostMapping("/pendingApprovalPage")
    @ApiOperation("分页查询待审批权限")
    public MyJsonBean getPendingApprovalPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");
            String userId = request.getHeader("userId");

            Page<AsArchivePermission> page = new Page<>(current, size);
            page = (Page<AsArchivePermission>) archivePermissionService.getPendingApprovalPage(tenantId, userId, page);

            return MyJsonBean.success("分页查询待审批权限成功", page);
        } catch (Exception e) {
            log.error("分页查询待审批权限异常", e);
            return MyJsonBean.error("分页查询待审批权限异常：" + e.getMessage());
        }
    }

    // ==================== 统计分析操作 ====================

    @GetMapping("/statistics/count")
    @ApiOperation("统计权限总数")
    public MyJsonBean countPermissions(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Long count = archivePermissionService.countPermissions(tenantId);
            return MyJsonBean.success("统计权限总数成功", count);
        } catch (Exception e) {
            log.error("统计权限总数异常", e);
            return MyJsonBean.error("统计权限总数异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByType")
    @ApiOperation("按权限类型统计")
    public MyJsonBean countByPermissionType(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByPermissionType(tenantId);
            return MyJsonBean.success("按权限类型统计成功", statistics);
        } catch (Exception e) {
            log.error("按权限类型统计异常", e);
            return MyJsonBean.error("按权限类型统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByLevel")
    @ApiOperation("按权限级别统计")
    public MyJsonBean countByPermissionLevel(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByPermissionLevel(tenantId);
            return MyJsonBean.success("按权限级别统计成功", statistics);
        } catch (Exception e) {
            log.error("按权限级别统计异常", e);
            return MyJsonBean.error("按权限级别统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByStatus")
    @ApiOperation("按权限状态统计")
    public MyJsonBean countByPermissionStatus(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByPermissionStatus(tenantId);
            return MyJsonBean.success("按权限状态统计成功", statistics);
        } catch (Exception e) {
            log.error("按权限状态统计异常", e);
            return MyJsonBean.error("按权限状态统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countBySubjectType")
    @ApiOperation("按主体类型统计")
    public MyJsonBean countBySubjectType(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countBySubjectType(tenantId);
            return MyJsonBean.success("按主体类型统计成功", statistics);
        } catch (Exception e) {
            log.error("按主体类型统计异常", e);
            return MyJsonBean.error("按主体类型统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByResourceType")
    @ApiOperation("按资源类型统计")
    public MyJsonBean countByResourceType(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByResourceType(tenantId);
            return MyJsonBean.success("按资源类型统计成功", statistics);
        } catch (Exception e) {
            log.error("按资源类型统计异常", e);
            return MyJsonBean.error("按资源类型统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByAccessLevel")
    @ApiOperation("按访问级别统计")
    public MyJsonBean countByAccessLevel(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByAccessLevel(tenantId);
            return MyJsonBean.success("按访问级别统计成功", statistics);
        } catch (Exception e) {
            log.error("按访问级别统计异常", e);
            return MyJsonBean.error("按访问级别统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/countByRiskLevel")
    @ApiOperation("按风险等级统计")
    public MyJsonBean countByRiskLevel(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.countByRiskLevel(tenantId);
            return MyJsonBean.success("按风险等级统计成功", statistics);
        } catch (Exception e) {
            log.error("按风险等级统计异常", e);
            return MyJsonBean.error("按风险等级统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/trend")
    @ApiOperation("获取权限趋势分析")
    public MyJsonBean getPermissionTrend(
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime,
            @ApiParam("粒度") @RequestParam(defaultValue = "day") String granularity,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> trend = archivePermissionService.getPermissionTrend(tenantId, startTime, endTime, granularity);
            return MyJsonBean.success("获取权限趋势分析成功", trend);
        } catch (Exception e) {
            log.error("获取权限趋势分析异常", e);
            return MyJsonBean.error("获取权限趋势分析异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/userActivity")
    @ApiOperation("获取用户活动统计")
    public MyJsonBean getUserActivityStatistics(
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> statistics = archivePermissionService.getUserActivityStatistics(tenantId, startTime, endTime);
            return MyJsonBean.success("获取用户活动统计成功", statistics);
        } catch (Exception e) {
            log.error("获取用户活动统计异常", e);
            return MyJsonBean.error("获取用户活动统计异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/popularPermissions")
    @ApiOperation("获取热门权限排行")
    public MyJsonBean getPopularPermissions(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> popularPermissions = archivePermissionService.getPopularPermissions(tenantId, limit);
            return MyJsonBean.success("获取热门权限排行成功", popularPermissions);
        } catch (Exception e) {
            log.error("获取热门权限排行异常", e);
            return MyJsonBean.error("获取热门权限排行异常：" + e.getMessage());
        }
    }

    @GetMapping("/statistics/activeUsers")
    @ApiOperation("获取活跃用户排行")
    public MyJsonBean getActiveUsers(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            List<Map<String, Object>> activeUsers = archivePermissionService.getActiveUsers(tenantId, limit);
            return MyJsonBean.success("获取活跃用户排行成功", activeUsers);
        } catch (Exception e) {
            log.error("获取活跃用户排行异常", e);
            return MyJsonBean.error("获取活跃用户排行异常：" + e.getMessage());
        }
    }

    // ==================== 系统维护操作 ====================

    @GetMapping("/system/overview")
    @ApiOperation("获取系统概览")
    public MyJsonBean getSystemOverview(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Map<String, Object> overview = archivePermissionService.getSystemOverview(tenantId);
            return MyJsonBean.success("获取系统概览成功", overview);
        } catch (Exception e) {
            log.error("获取系统概览异常", e);
            return MyJsonBean.error("获取系统概览异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/generateReport")
    @ApiOperation("生成权限报告")
    public MyJsonBean generatePermissionReport(
            @ApiParam("报告类型") @RequestParam String reportType,
            @ApiParam("开始时间") @RequestParam LocalDateTime startTime,
            @ApiParam("结束时间") @RequestParam LocalDateTime endTime,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Map<String, Object> report = archivePermissionService.generatePermissionReport(tenantId, reportType, startTime, endTime);
            return MyJsonBean.success("生成权限报告成功", report);
        } catch (Exception e) {
            log.error("生成权限报告异常", e);
            return MyJsonBean.error("生成权限报告异常：" + e.getMessage());
        }
    }

    @GetMapping("/system/health")
    @ApiOperation("检查系统健康")
    public MyJsonBean checkSystemHealth(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Map<String, Object> health = archivePermissionService.checkSystemHealth(tenantId);
            return MyJsonBean.success("检查系统健康成功", health);
        } catch (Exception e) {
            log.error("检查系统健康异常", e);
            return MyJsonBean.error("检查系统健康异常：" + e.getMessage());
        }
    }

    @GetMapping("/system/assessQuality")
    @ApiOperation("权限质量评估")
    public MyJsonBean assessPermissionQuality(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Map<String, Object> quality = archivePermissionService.assessPermissionQuality(tenantId);
            return MyJsonBean.success("权限质量评估成功", quality);
        } catch (Exception e) {
            log.error("权限质量评估异常", e);
            return MyJsonBean.error("权限质量评估异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/maintenance")
    @ApiOperation("执行维护任务")
    public MyJsonBean executeMaintenanceTask(
            @ApiParam("任务类型") @RequestParam String taskType,
            @ApiParam("任务参数") @RequestBody(required = false) Map<String, Object> taskParams,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            boolean result = archivePermissionService.executeMaintenanceTask(tenantId, taskType, taskParams);
            if (result) {
                return MyJsonBean.success("执行维护任务成功");
            } else {
                return MyJsonBean.error("执行维护任务失败");
            }
        } catch (Exception e) {
            log.error("执行维护任务异常", e);
            return MyJsonBean.error("执行维护任务异常：" + e.getMessage());
        }
    }

    @GetMapping("/system/maintenanceStatus/{taskId}")
    @ApiOperation("获取维护任务状态")
    public MyJsonBean getMaintenanceTaskStatus(
            @ApiParam("任务ID") @PathVariable String taskId,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            Map<String, Object> status = archivePermissionService.getMaintenanceTaskStatus(tenantId, taskId);
            return MyJsonBean.success("获取维护任务状态成功", status);
        } catch (Exception e) {
            log.error("获取维护任务状态异常", e);
            return MyJsonBean.error("获取维护任务状态异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/cleanupExpired")
    @ApiOperation("清理过期权限")
    public MyJsonBean cleanupExpiredPermissions(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            int count = archivePermissionService.cleanupExpiredPermissions(tenantId);
            return MyJsonBean.success("清理过期权限成功", count);
        } catch (Exception e) {
            log.error("清理过期权限异常", e);
            return MyJsonBean.error("清理过期权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/cleanupInvalid")
    @ApiOperation("清理无效权限")
    public MyJsonBean cleanupInvalidPermissions(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            int count = archivePermissionService.cleanupInvalidPermissions(tenantId);
            return MyJsonBean.success("清理无效权限成功", count);
        } catch (Exception e) {
            log.error("清理无效权限异常", e);
            return MyJsonBean.error("清理无效权限异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/optimize")
    @ApiOperation("优化权限存储")
    public MyJsonBean optimizePermissionStorage(HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            boolean result = archivePermissionService.optimizePermissionStorage(tenantId);
            if (result) {
                return MyJsonBean.success("优化权限存储成功");
            } else {
                return MyJsonBean.error("优化权限存储失败");
            }
        } catch (Exception e) {
            log.error("优化权限存储异常", e);
            return MyJsonBean.error("优化权限存储异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/backup")
    @ApiOperation("备份权限数据")
    public MyJsonBean backupPermissionData(
            @ApiParam("备份路径") @RequestParam String backupPath,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            boolean result = archivePermissionService.backupPermissionData(tenantId, backupPath);
            if (result) {
                return MyJsonBean.success("备份权限数据成功");
            } else {
                return MyJsonBean.error("备份权限数据失败");
            }
        } catch (Exception e) {
            log.error("备份权限数据异常", e);
            return MyJsonBean.error("备份权限数据异常：" + e.getMessage());
        }
    }

    @PostMapping("/system/restore")
    @ApiOperation("恢复权限数据")
    public MyJsonBean restorePermissionData(
            @ApiParam("备份路径") @RequestParam String backupPath,
            HttpServletRequest request) {
        try {
            String tenantId = request.getHeader("tenantId");

            boolean result = archivePermissionService.restorePermissionData(tenantId, backupPath);
            if (result) {
                return MyJsonBean.success("恢复权限数据成功");
            } else {
                return MyJsonBean.error("恢复权限数据失败");
            }
        } catch (Exception e) {
            log.error("恢复权限数据异常", e);
            return MyJsonBean.error("恢复权限数据异常：" + e.getMessage());
        }
    }
}
