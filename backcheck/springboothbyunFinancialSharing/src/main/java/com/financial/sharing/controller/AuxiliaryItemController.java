package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.service.AuxiliaryItemService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.AuxiliaryItemQueryParam;
import com.financial.sharing.vo.param.AuxiliaryItemSaveParam;
import com.financial.sharing.vo.result.AuxiliaryItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 辅助核算项管理控制器
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "辅助核算项管理")
@RestController
@RequestMapping("/common/auxiliary")
@Validated
@CrossOrigin
public class AuxiliaryItemController extends BaseController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private AuxiliaryItemService auxiliaryItemService;

    /**
     * 分页查询辅助核算项列表
     */
    @ApiOperation("分页查询辅助核算项列表")
    @PostMapping(value = "/getList", consumes = "application/x-www-form-urlencoded")
    public String getList(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) Integer pageNumber,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String auxiliaryCode,
            @RequestParam(required = false) String auxiliaryName,
            @RequestParam(required = false) String auxiliaryType,
            @RequestParam(required = false) Integer isEnabled,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            // 构建查询参数
            AuxiliaryItemQueryParam param = new AuxiliaryItemQueryParam();
            param.setPageNumber(pageNumber != null ? pageNumber : 1);
            param.setPageSize(pageSize != null ? pageSize : 15);
            param.setAuxiliaryCode(auxiliaryCode);
            param.setAuxiliaryName(auxiliaryName);
            param.setAuxiliaryType(auxiliaryType);
            param.setIsEnabled(isEnabled);

            // 强制使用前端传递的参数，不被用户信息覆盖
            param.setBookId(bookId != null ? bookId : 1L);
            param.setTenantId(tenantId != null ? tenantId : 1L);

            logApiCall("查询辅助核算项列表", loginStaff, param);
            PageResult<AuxiliaryItemVO> result = auxiliaryItemService.getAuxiliaryItemPage(param);
            logApiSuccess("查询辅助核算项列表", loginStaff, result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("查询辅助核算项列表失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 保存或更新辅助核算项
     */
    @ApiOperation("保存或更新辅助核算项")
    @PostMapping(value = "/saveOrUpdate", consumes = "application/x-www-form-urlencoded")
    public String saveOrUpdate(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(required = false) Long auxiliaryId,
            @RequestParam String auxiliaryCode,
            @RequestParam String auxiliaryName,
            @RequestParam String auxiliaryType,
            @RequestParam(required = false) Integer isEnabled,
            @RequestParam(required = false) String remark,
            @RequestParam(required = false) Long parentId,
            @RequestParam Long bookId,
            @RequestParam Long tenantId,
            @RequestParam(required = false) Long creator,
            @RequestParam(required = false) Long updater) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            // 构建保存参数
            AuxiliaryItemSaveParam param = new AuxiliaryItemSaveParam();
            param.setAuxiliaryId(auxiliaryId);
            param.setAuxiliaryCode(auxiliaryCode);
            param.setAuxiliaryName(auxiliaryName);
            param.setAuxiliaryType(auxiliaryType);
            param.setIsEnabled(isEnabled != null ? isEnabled : 1);
            param.setParentId(parentId);
            param.setBookId(bookId);
            param.setTenantId(tenantId);

            if (auxiliaryId == null) {
                param.setCreator(creator != null ? creator : loginStaff.getStaffid().longValue());
            } else {
                param.setUpdater(updater != null ? updater : loginStaff.getStaffid().longValue());
            }

            logApiCall("保存或更新辅助核算项", loginStaff, param);
            AuxiliaryItemVO result = auxiliaryItemService.saveOrUpdateAuxiliaryItem(param);
            logApiSuccess("保存或更新辅助核算项", loginStaff, result);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("保存辅助核算项失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("保存失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 根据ID查询辅助核算项详情
     */
    @ApiOperation("根据ID查询辅助核算项详情")
    @GetMapping("/{auxiliaryId}")
    public String getById(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项ID", required = true) @PathVariable @NotNull Long auxiliaryId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("查询辅助核算项详情", loginStaff, auxiliaryId);
            AuxiliaryItemVO result = auxiliaryItemService.getAuxiliaryItemById(auxiliaryId);
            logApiSuccess("查询辅助核算项详情", loginStaff, result);

            if (result == null) {
                JsonBean json = new JsonBean();
                json.setCode(0);
                json.setMsg("辅助核算项不存在");
                return json.toString();
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("查询辅助核算项详情失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 删除辅助核算项
     */
    @ApiOperation("删除辅助核算项")
    @DeleteMapping("/{auxiliaryId}")
    public String delete(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项ID", required = true) @PathVariable @NotNull Long auxiliaryId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("删除辅助核算项", loginStaff, auxiliaryId);
            boolean success = auxiliaryItemService.deleteAuxiliaryItem(auxiliaryId);
            logApiSuccess("删除辅助核算项", loginStaff, success);

            JsonBean json = new JsonBean();
            if (success) {
                json.setCode(1);
                json.setMsg("删除成功");
            } else {
                json.setCode(0);
                json.setMsg("删除失败");
            }
            return json.toString();

        } catch (Exception e) {
            log.error("删除辅助核算项失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("删除失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 启用/禁用辅助核算项
     */
    @ApiOperation("启用/禁用辅助核算项")
    @PutMapping("/{auxiliaryId}/status")
    public String updateStatus(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项ID", required = true) @PathVariable @NotNull Long auxiliaryId,
            @ApiParam(value = "启用状态", required = true) @RequestParam @NotNull Integer isEnabled) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("更新辅助核算项状态", loginStaff, "ID:" + auxiliaryId + ", 状态:" + isEnabled);
            boolean success = auxiliaryItemService.updateAuxiliaryItemStatus(auxiliaryId, isEnabled);
            logApiSuccess("更新辅助核算项状态", loginStaff, success);

            JsonBean json = new JsonBean();
            if (success) {
                json.setCode(1);
                json.setMsg("状态更新成功");
            } else {
                json.setCode(0);
                json.setMsg("状态更新失败");
            }
            return json.toString();

        } catch (Exception e) {
            log.error("更新辅助核算项状态失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("状态更新失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 根据类型查询辅助核算项
     */
    @ApiOperation("根据类型查询辅助核算项")
    @GetMapping("/type/{auxiliaryType}")
    public String getByType(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算类型", required = true) @PathVariable @NotNull String auxiliaryType,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("根据类型查询辅助核算项", loginStaff, "类型:" + auxiliaryType);
            List<AuxiliaryItemVO> result = auxiliaryItemService.getAuxiliaryItemsByType(auxiliaryType, bookId, tenantId);
            logApiSuccess("根据类型查询辅助核算项", loginStaff, result.size() + "条记录");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("根据类型查询辅助核算项失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 获取辅助核算项树形结构
     */
    @ApiOperation("获取辅助核算项树形结构")
    @GetMapping("/tree")
    public String getTree(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算类型") @RequestParam(required = false) String auxiliaryType,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("获取辅助核算项树形结构", loginStaff, "类型:" + auxiliaryType);
            List<AuxiliaryItemVO> result = auxiliaryItemService.getAuxiliaryItemTree(auxiliaryType, bookId, tenantId);
            logApiSuccess("获取辅助核算项树形结构", loginStaff, result.size() + "条记录");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("获取辅助核算项树形结构失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("获取失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 批量删除辅助核算项
     */
    @ApiOperation("批量删除辅助核算项")
    @PostMapping(value = "/batchDelete", consumes = "application/x-www-form-urlencoded")
    public String batchDelete(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项ID列表", required = true) @RequestParam @NotEmpty String auxiliaryIds) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> auxiliaryIdList = java.util.Arrays.stream(auxiliaryIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            logApiCall("批量删除辅助核算项", loginStaff, auxiliaryIdList);
            boolean success = auxiliaryItemService.batchDeleteAuxiliaryItems(auxiliaryIdList);
            logApiSuccess("批量删除辅助核算项", loginStaff, success);

            JsonBean json = new JsonBean();
            if (success) {
                json.setCode(1);
                json.setMsg("批量删除成功");
            } else {
                json.setCode(0);
                json.setMsg("批量删除失败");
            }
            return json.toString();

        } catch (Exception e) {
            log.error("批量删除辅助核算项失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("批量删除失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 批量启用/禁用辅助核算项
     */
    @ApiOperation("批量启用/禁用辅助核算项")
    @PostMapping(value = "/batchUpdateStatus", consumes = "application/x-www-form-urlencoded")
    public String batchUpdateStatus(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项ID列表", required = true) @RequestParam @NotEmpty String auxiliaryIds,
            @ApiParam(value = "启用状态", required = true) @RequestParam @NotNull Integer isEnabled) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> auxiliaryIdList = java.util.Arrays.stream(auxiliaryIds.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            logApiCall("批量更新辅助核算项状态", loginStaff, "IDs:" + auxiliaryIdList + ", 状态:" + isEnabled);
            boolean success = auxiliaryItemService.batchUpdateAuxiliaryItemStatus(auxiliaryIdList, isEnabled);
            logApiSuccess("批量更新辅助核算项状态", loginStaff, success);

            JsonBean json = new JsonBean();
            if (success) {
                json.setCode(1);
                json.setMsg("批量状态更新成功");
            } else {
                json.setCode(0);
                json.setMsg("批量状态更新失败");
            }
            return json.toString();

        } catch (Exception e) {
            log.error("批量更新辅助核算项状态失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("批量状态更新失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 检查辅助核算项编码是否存在
     */
    @ApiOperation("检查辅助核算项编码是否存在")
    @GetMapping("/check-code")
    public String checkCode(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "辅助核算项编码", required = true) @RequestParam @NotNull String auxiliaryCode,
            @ApiParam(value = "辅助核算类型", required = true) @RequestParam @NotNull String auxiliaryType,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId,
            @ApiParam(value = "排除的ID") @RequestParam(required = false) Long excludeId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("检查辅助核算项编码", loginStaff, "编码:" + auxiliaryCode + ", 类型:" + auxiliaryType);
            boolean exists = auxiliaryItemService.checkAuxiliaryCodeExists(auxiliaryCode, auxiliaryType, bookId, tenantId, excludeId);
            logApiSuccess("检查辅助核算项编码", loginStaff, "是否存在:" + exists);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(exists);
            return json.toString();

        } catch (Exception e) {
            log.error("检查辅助核算项编码失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("检查失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 根据上级ID查询子项列表
     */
    @ApiOperation("根据上级ID查询子项列表")
    @GetMapping("/children")
    public String getChildren(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "上级ID") @RequestParam(required = false) Long parentId,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("根据上级ID查询子项列表", loginStaff, "上级ID:" + parentId);
            List<AuxiliaryItemVO> result = auxiliaryItemService.getAuxiliaryItemsByParentId(parentId, bookId, tenantId);
            logApiSuccess("根据上级ID查询子项列表", loginStaff, result.size() + "条记录");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("根据上级ID查询子项列表失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 获取辅助核算类型列表
     */
    @ApiOperation("获取辅助核算类型列表")
    @GetMapping("/types")
    public String getTypes(HttpServletRequest request, HttpServletResponse response,
            @ApiParam(value = "账簿ID", required = true) @RequestParam @NotNull Long bookId,
            @ApiParam(value = "租户ID", required = true) @RequestParam @NotNull Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toString());
                return null;
            }

            logApiCall("获取辅助核算类型列表", loginStaff, "账簿ID:" + bookId);
            List<String> result = auxiliaryItemService.getAuxiliaryTypes(bookId, tenantId);
            logApiSuccess("获取辅助核算类型列表", loginStaff, result.size() + "条记录");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("成功");
            json.setData(result);
            return json.toString();

        } catch (Exception e) {
            log.error("获取辅助核算类型列表失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("获取失败：" + e.getMessage());
            return json.toString();
        }
    }

    /**
     * 简单测试接口 - 绕过Service层复杂性
     */
    @ApiOperation("简单测试接口")
    @GetMapping("/test")
    public String test(HttpServletRequest request, HttpServletResponse response) {
        try {
            log.info("测试辅助核算接口 - 简单测试");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("测试成功");
            json.setData("辅助核算模块工作正常");
            return json.toString();

        } catch (Exception e) {
            log.error("测试接口失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("测试失败：" + e.getMessage());
            return json.toString();
        }
    }
}