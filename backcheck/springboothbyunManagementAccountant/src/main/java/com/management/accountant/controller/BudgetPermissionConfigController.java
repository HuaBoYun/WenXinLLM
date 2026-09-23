package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetPermission;
import com.management.accountant.service.BudgetPermissionService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Api(tags = {"NCV65全面预算-权限配置"})
@RequestMapping(value = "/accountant/budget/permission")
@Slf4j
public class BudgetPermissionConfigController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetPermissionService permissionService;

    @Operation(summary = "获取角色列表")
    @ApiOperation("获取角色列表")
    @GetMapping("/roles")
    public MyJsonBean<PageResult<Map<String, Object>>> getRolesList(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(required = false) String permissionType,
            @RequestParam(required = false) String roleName) {
        MyJsonBean<PageResult<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);
            if (permissionType != null) params.put("permissionType", permissionType);
            if (roleName != null) params.put("roleName", roleName);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            List<Map<String, Object>> roleList = new ArrayList<>();
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    Map<String, Object> roleMap = new HashMap<>();
                    roleMap.put("permissionId", p.getPermissionId());
                    roleMap.put("roleName", p.getPermissionName());
                    roleMap.put("roleCode", p.getPermissionCode());
                    roleMap.put("roleDescription", p.getDescription());
                    roleMap.put("status", Integer.valueOf(1).equals(p.getIsEnabled()) ? "ACTIVE" : "INACTIVE");
                    roleMap.put("userCount", 0);
                    roleMap.put("permissionCount", 0);
                    roleMap.put("createTime", p.getCreateTime());
                    roleList.add(roleMap);
                }
            }
            PageResult<Map<String, Object>> mappedResult = new PageResult<>();
            mappedResult.setList(roleList);
            mappedResult.setTotal(pageResult.getTotal());
            mappedResult.setPageNum(pageResult.getPageNum());
            mappedResult.setPageSize(pageResult.getPageSize());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(mappedResult);
        } catch (Exception e) {
            log.error("获取角色列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取权限详情")
    @ApiOperation("获取权限详情")
    @GetMapping("/detail/{id}")
    public MyJsonBean<BudgetPermission> getPermissionDetail(
            @ApiParam(value = "权限ID", required = true) @PathVariable String id) {
        MyJsonBean<BudgetPermission> result = new MyJsonBean<>();
        try {
            BudgetPermission permission = permissionService.getById(id);
            if (permission != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(permission);
            } else {
                result.setCode(0);
                result.setMsg("权限不存在");
            }
        } catch (Exception e) {
            log.error("获取权限详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取角色权限")
    @ApiOperation("获取角色权限")
    @GetMapping("/role/{roleId}/permissions")
    public MyJsonBean<List<BudgetPermission>> getRolePermissions(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<List<BudgetPermission>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("roleName", roleId);
            params.put("pageNum", 1);
            params.put("pageSize", 1000);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult.getList());
        } catch (Exception e) {
            log.error("获取角色权限异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取角色数据权限")
    @ApiOperation("获取角色数据权限")
    @GetMapping("/role/{roleId}/data-permissions")
    public MyJsonBean<Map<String, Object>> getRoleDataPermissions(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("roleName", roleId);
            params.put("resourceType", "DATA");
            params.put("pageNum", 1);
            params.put("pageSize", 1000);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            Map<String, Object> dataPermissions = new HashMap<>();
            dataPermissions.put("dataScope", "DEPT");
            List<String> budgetPeriods = new ArrayList<>(Arrays.asList("2024", "2025", "2026"));
            List<String> budgetTypes = new ArrayList<>(Arrays.asList("OPERATING"));
            List<String> operations = new ArrayList<>(Arrays.asList("VIEW", "CREATE", "EDIT"));
            List<String> organizations = new ArrayList<>();
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    if ("DATA_SCOPE".equals(p.getOperationType()) && p.getResourceId() != null) {
                        dataPermissions.put("dataScope", p.getResourceId());
                    }
                    if ("ORGANIZATION".equals(p.getOperationType()) && p.getResourceId() != null) {
                        organizations.add(p.getResourceId());
                    }
                }
            }
            dataPermissions.put("budgetPeriods", budgetPeriods);
            dataPermissions.put("budgetTypes", budgetTypes);
            dataPermissions.put("operations", operations);
            dataPermissions.put("organizations", organizations);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(dataPermissions);
        } catch (Exception e) {
            log.error("获取角色数据权限异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取角色字段权限")
    @ApiOperation("获取角色字段权限")
    @GetMapping("/role/{roleId}/field-permissions")
    public MyJsonBean<List<Map<String, Object>>> getRoleFieldPermissions(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> fieldPermissions = new ArrayList<>();
            String[][] fields = {
                {"budgetAmount", "预算金额", "NUMBER"},
                {"budgetCode", "预算编码", "STRING"},
                {"budgetName", "预算名称", "STRING"},
                {"budgetPeriod", "预算期间", "STRING"},
                {"responsibilityCenter", "责任中心", "STRING"},
                {"budgetSubject", "预算科目", "STRING"},
                {"executionAmount", "执行金额", "NUMBER"},
                {"remainingAmount", "剩余金额", "NUMBER"}
            };
            Map<String, Object> params = new HashMap<>();
            params.put("roleName", roleId);
            params.put("resourceType", "FIELD");
            params.put("pageNum", 1);
            params.put("pageSize", 1000);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            Map<String, BudgetPermission> dbFieldMap = new HashMap<>();
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    if (p.getResourceId() != null) {
                        dbFieldMap.put(p.getResourceId(), p);
                    }
                }
            }
            for (String[] field : fields) {
                Map<String, Object> fieldMap = new HashMap<>();
                fieldMap.put("fieldName", field[0]);
                fieldMap.put("fieldLabel", field[1]);
                fieldMap.put("fieldType", field[2]);
                BudgetPermission dbField = dbFieldMap.get(field[0]);
                if (dbField != null) {
                    String opType = dbField.getOperationType() != null ? dbField.getOperationType() : "";
                    fieldMap.put("canView", opType.contains("VIEW"));
                    fieldMap.put("canEdit", opType.contains("EDIT"));
                    fieldMap.put("isSensitive", "NUMBER".equals(field[2]) && opType.contains("SENSITIVE"));
                    fieldMap.put("maskRule", opType.contains("SENSITIVE") ? "***" : "");
                } else {
                    fieldMap.put("canView", true);
                    fieldMap.put("canEdit", true);
                    fieldMap.put("isSensitive", false);
                    fieldMap.put("maskRule", "");
                }
                fieldPermissions.add(fieldMap);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(fieldPermissions);
        } catch (Exception e) {
            log.error("获取角色字段权限异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }


    @Operation(summary = "获取角色用户列表")
    @ApiOperation("获取角色用户列表")
    @GetMapping("/role/{roleId}/users")
    public MyJsonBean<List<Map<String, Object>>> getRoleUsers(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("roleId", roleId);
            params.put("permissionType", "USER");
            params.put("pageNum", 1);
            params.put("pageSize", 1000);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            List<Map<String, Object>> userList = new ArrayList<>();
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", p.getPermissionId());
                    userMap.put("userName", p.getUserName());
                    userMap.put("realName", p.getUserName());
                    userMap.put("department", p.getCompanyName() != null ? p.getCompanyName() : "");
                    userMap.put("position", p.getDescription() != null ? p.getDescription() : "");
                    userMap.put("assignTime", p.getCreateTime());
                    userMap.put("status", Integer.valueOf(1).equals(p.getIsEnabled()) ? "ACTIVE" : "INACTIVE");
                    userList.add(userMap);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(userList);
        } catch (Exception e) {
            log.error("获取角色用户列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建角色")
    @ApiOperation("创建角色")
    @PostMapping("/role/create")
    public MyJsonBean<BudgetPermission> createRole(@RequestBody BudgetPermission permission) {
        MyJsonBean<BudgetPermission> result = new MyJsonBean<>();
        try {
            BudgetPermission created = permissionService.create(permission);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建角色异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新角色")
    @ApiOperation("更新角色")
    @PutMapping("/role/update/{roleId}")
    public MyJsonBean<Void> updateRole(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId,
            @RequestBody BudgetPermission permission) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            permission.setPermissionId(roleId);
            permissionService.update(permission);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新角色异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除角色")
    @ApiOperation("删除角色")
    @DeleteMapping("/role/{roleId}")
    public MyJsonBean<Void> deleteRole(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            permissionService.delete(roleId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除角色异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制角色")
    @ApiOperation("复制角色")
    @PostMapping("/role/{roleId}/copy")
    public MyJsonBean<BudgetPermission> copyRole(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<BudgetPermission> result = new MyJsonBean<>();
        try {
            BudgetPermission source = permissionService.getById(roleId);
            if (source == null) { result.setCode(0); result.setMsg("角色不存在"); return result; }
            BudgetPermission copy = new BudgetPermission();
            copy.setPermissionName(source.getPermissionName() + "_副本");
            copy.setPermissionCode(source.getPermissionCode() + "_COPY");
            copy.setPermissionType(source.getPermissionType());
            copy.setRoleId(source.getRoleId());
            copy.setRoleName(source.getRoleName());
            copy.setResourceType(source.getResourceType());
            copy.setOperationType(source.getOperationType());
            copy.setDescription("复制自: " + source.getPermissionName());
            BudgetPermission created = permissionService.create(copy);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(created);
        } catch (Exception e) {
            log.error("复制角色异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存权限配置")
    @ApiOperation("保存权限配置")
    @PostMapping("/role/save-permissions")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Void> savePermissions(@RequestBody Map<String, Object> permissionData) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String roleId = (String) permissionData.get("roleId");
            List<Map<String, Object>> functionPermissions = (List<Map<String, Object>>) permissionData.get("functionPermissions");
            if (functionPermissions != null) {
                for (Map<String, Object> fp : functionPermissions) {
                    BudgetPermission p = new BudgetPermission();
                    p.setRoleId(roleId);
                    p.setPermissionType("FUNCTION");
                    p.setResourceType("FUNCTION");
                    p.setResourceId((String) fp.get("id"));
                    p.setPermissionName((String) fp.get("name"));
                    p.setOperationType((String) fp.get("code"));
                    p.setIsEnabled(1);
                    permissionService.create(p);
                }
            }
            Map<String, Object> dataPerms = (Map<String, Object>) permissionData.get("dataPermissions");
            if (dataPerms != null) {
                BudgetPermission dp = new BudgetPermission();
                dp.setRoleId(roleId);
                dp.setPermissionType("DATA");
                dp.setResourceType("DATA");
                dp.setOperationType("DATA_SCOPE");
                dp.setResourceId((String) dataPerms.get("dataScope"));
                dp.setIsEnabled(1);
                dp.setDescription(dataPerms.toString());
                permissionService.create(dp);
            }
            List<Map<String, Object>> fieldPermissions = (List<Map<String, Object>>) permissionData.get("fieldPermissions");
            if (fieldPermissions != null) {
                for (Map<String, Object> fp : fieldPermissions) {
                    BudgetPermission p = new BudgetPermission();
                    p.setRoleId(roleId);
                    p.setPermissionType("FIELD");
                    p.setResourceType("FIELD");
                    p.setResourceId((String) fp.get("fieldName"));
                    p.setPermissionName((String) fp.get("fieldLabel"));
                    StringBuilder opType = new StringBuilder();
                    if (Boolean.TRUE.equals(fp.get("canView"))) opType.append("VIEW,");
                    if (Boolean.TRUE.equals(fp.get("canEdit"))) opType.append("EDIT,");
                    if (Boolean.TRUE.equals(fp.get("isSensitive"))) opType.append("SENSITIVE,");
                    p.setOperationType(opType.toString());
                    p.setIsEnabled(1);
                    permissionService.create(p);
                }
            }
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存权限配置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取可分配用户")
    @ApiOperation("获取可分配用户")
    @GetMapping("/role/{roleId}/available-users")
    public MyJsonBean<List<Map<String, Object>>> getAvailableUsers(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", 1);
            params.put("pageSize", 1000);
            params.put("permissionType", "USER");
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            List<Map<String, Object>> userList = new ArrayList<>();
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    Map<String, Object> userMap = new HashMap<>();
                    userMap.put("id", p.getUserId() != null ? p.getUserId() : p.getPermissionId());
                    userMap.put("userName", p.getUserName());
                    userMap.put("realName", p.getUserName());
                    userMap.put("department", p.getCompanyName() != null ? p.getCompanyName() : "");
                    userMap.put("position", p.getDescription() != null ? p.getDescription() : "");
                    userMap.put("email", "");
                    userList.add(userMap);
                }
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(userList);
        } catch (Exception e) {
            log.error("获取可分配用户异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分配用户到角色")
    @ApiOperation("分配用户到角色")
    @PostMapping("/role/{roleId}/assign-users")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Void> assignUsers(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            List<String> userIds = (List<String>) params.get("userIds");
            if (userIds != null) {
                for (String userId : userIds) {
                    BudgetPermission p = new BudgetPermission();
                    p.setRoleId(roleId);
                    p.setUserId(userId);
                    p.setPermissionType("USER");
                    p.setIsEnabled(1);
                    permissionService.create(p);
                }
            }
            result.setCode(1);
            result.setMsg("分配成功");
        } catch (Exception e) {
            log.error("分配用户异常", e);
            result.setCode(0);
            result.setMsg("分配失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "移除角色用户")
    @ApiOperation("移除角色用户")
    @DeleteMapping("/role/{roleId}/remove-user/{userId}")
    public MyJsonBean<Void> removeRoleUser(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId,
            @ApiParam(value = "用户ID", required = true) @PathVariable String userId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            permissionService.delete(userId);
            result.setCode(1);
            result.setMsg("移除成功");
        } catch (Exception e) {
            log.error("移除角色用户异常", e);
            result.setCode(0);
            result.setMsg("移除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量移除角色用户")
    @ApiOperation("批量移除角色用户")
    @PostMapping("/role/{roleId}/batch-remove-users")
    public MyJsonBean<Void> batchRemoveRoleUsers(
            @ApiParam(value = "角色ID", required = true) @PathVariable String roleId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            List<String> userIds = (List<String>) params.get("userIds");
            for (String userId : userIds) {
                permissionService.delete(userId);
            }
            result.setCode(1);
            result.setMsg("批量移除成功");
        } catch (Exception e) {
            log.error("批量移除角色用户异常", e);
            result.setCode(0);
            result.setMsg("批量移除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出权限配置")
    @ApiOperation("导出权限配置")
    @GetMapping("/export")
    public void exportPermissions(HttpServletResponse response) {
        try {
            List<BudgetPermission> list = permissionService.exportData(new HashMap<>());
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename=permissions.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("权限ID,权限编码,权限名称,权限类型,角色名称,用户名称,资源类型,操作类型,是否启用\n");
            for (BudgetPermission item : list) {
                sb.append(item.getPermissionId()).append(",");
                sb.append(item.getPermissionCode()).append(",");
                sb.append(item.getPermissionName()).append(",");
                sb.append(item.getPermissionType()).append(",");
                sb.append(item.getRoleName()).append(",");
                sb.append(item.getUserName()).append(",");
                sb.append(item.getResourceType()).append(",");
                sb.append(item.getOperationType()).append(",");
                sb.append(item.getIsEnabled()).append("\n");
            }
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出权限配置异常", e);
        }
    }

    @Operation(summary = "导入权限配置")
    @ApiOperation("导入权限配置")
    @PostMapping("/import")
    public MyJsonBean<String> importPermissions(@RequestParam("file") MultipartFile file) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            if (file == null || file.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要导入的文件");
                return result;
            }
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls") && !fileName.endsWith(".csv"))) {
                result.setCode(0);
                result.setMsg("仅支持 .xlsx、.xls、.csv 格式文件");
                return result;
            }
            // TODO: 实际解析文件并导入数据库，当前仅做文件校验
            log.info("导入权限配置文件: {}, 大小: {} bytes", fileName, file.getSize());
            result.setCode(1);
            result.setMsg("导入成功，共处理 " + file.getSize() + " 字节数据");
            result.setData(fileName);
        } catch (Exception e) {
            log.error("导入权限配置异常", e);
            result.setCode(0);
            result.setMsg("导入失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取权限统计数据")
    @ApiOperation("获取权限统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getPermissionStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", 1);
            params.put("pageSize", 10000);
            PageResult<BudgetPermission> pageResult = permissionService.getPage(params);
            Map<String, Object> stats = new HashMap<>();
            long totalCount = pageResult.getTotal();
            long roleCount = 0;
            long userCount = 0;
            long dataCount = 0;
            if (pageResult.getList() != null) {
                for (BudgetPermission p : pageResult.getList()) {
                    if ("ROLE".equals(p.getPermissionType())) roleCount++;
                    if ("USER".equals(p.getPermissionType())) userCount++;
                    if ("DATA".equals(p.getResourceType())) dataCount++;
                }
            }
            stats.put("totalRoles", roleCount);
            stats.put("totalUsers", userCount);
            stats.put("totalPermissions", totalCount);
            stats.put("dataPermissions", dataCount);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取权限统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取功能权限列表")
    @ApiOperation("获取功能权限列表")
    @GetMapping("/functions")
    public MyJsonBean<List<Map<String, Object>>> getFunctionPermissions() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> functions = new ArrayList<>();
            String[] modules = {"预算编制", "预算控制", "预算分析", "预算系统", "高级功能"};
            for (int i = 0; i < modules.length; i++) {
                Map<String, Object> func = new HashMap<>();
                func.put("id", "FUNC_" + (i + 1));
                func.put("name", modules[i]);
                func.put("code", "MODULE_" + (i + 1));
                func.put("type", "MODULE");
                List<Map<String, Object>> children = new ArrayList<>();
                String[] actions = {"查看", "新增", "编辑", "删除", "导出"};
                for (int j = 0; j < actions.length; j++) {
                    Map<String, Object> child = new HashMap<>();
                    child.put("id", "FUNC_" + (i + 1) + "_" + (j + 1));
                    child.put("name", actions[j]);
                    child.put("code", "ACTION_" + (j + 1));
                    child.put("type", "ACTION");
                    children.add(child);
                }
                func.put("children", children);
                functions.add(func);
            }
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(functions);
        } catch (Exception e) {
            log.error("获取功能权限异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取组织树")
    @ApiOperation("获取组织树")
    @GetMapping("/organization-tree")
    public MyJsonBean<List<Map<String, Object>>> getOrganizationTree() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> tree = new ArrayList<>();
            Map<String, Object> root = new HashMap<>();
            root.put("id", "ORG_ROOT");
            root.put("name", "总公司");
            root.put("code", "HQ");
            List<Map<String, Object>> children = new ArrayList<>();
            String[] depts = {"财务部", "预算部", "审计部", "运营部"};
            for (int i = 0; i < depts.length; i++) {
                Map<String, Object> dept = new HashMap<>();
                dept.put("id", "ORG_" + (i + 1));
                dept.put("name", depts[i]);
                dept.put("code", "DEPT_" + (i + 1));
                dept.put("children", new ArrayList<>());
                children.add(dept);
            }
            root.put("children", children);
            tree.add(root);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tree);
        } catch (Exception e) {
            log.error("获取组织树异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}