package com.financial.sharing.controller;

import com.financial.sharing.service.InfluenceFactorService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.InfluenceFactorQueryParam;
import com.financial.sharing.vo.param.InfluenceFactorSaveParam;
import com.financial.sharing.vo.result.InfluenceFactorVO;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 影响因素定义控制器
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/common/influence-factor")
@Api(tags = "影响因素定义管理")
@Validated
public class InfluenceFactorController {

    @Autowired
    private InfluenceFactorService influenceFactorService;

    @Resource
    private UserProvider userProvider;

    @PostMapping(value = "/page", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("分页查询影响因素")
    public MyJsonBean<PageResult<InfluenceFactorVO>> getInfluenceFactorPage(@Valid InfluenceFactorQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            PageResult<InfluenceFactorVO> result = influenceFactorService.getInfluenceFactorPage(param);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("查询影响因素列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/{factorId}")
    @ApiOperation("根据ID查询影响因素详情")
    public MyJsonBean<InfluenceFactorVO> getInfluenceFactorById(
            @PathVariable @ApiParam("影响因素ID") @NotNull Long factorId) {
        try {
            InfluenceFactorVO result = influenceFactorService.getInfluenceFactorById(factorId);
            if (result != null) {
                return MyJsonBean.successData("查询成功", result);
            } else {
                return MyJsonBean.errorData("影响因素不存在");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/code/{factorCode}")
    @ApiOperation("根据编码查询影响因素")
    public MyJsonBean<InfluenceFactorVO> getInfluenceFactorByCode(
            @PathVariable @ApiParam("影响因素编码") String factorCode,
            @RequestParam @ApiParam("租户ID") @NotNull Long tenantId,
            @RequestParam @ApiParam("账簿ID") @NotNull Long bookId) {
        try {
            InfluenceFactorVO result = influenceFactorService.getInfluenceFactorByCode(factorCode, tenantId, bookId);
            if (result != null) {
                return MyJsonBean.successData("查询成功", result);
            } else {
                return MyJsonBean.errorData("影响因素不存在");
            }
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @GetMapping("/type/{factorType}")
    @ApiOperation("根据类型查询影响因素列表")
    public MyJsonBean<List<InfluenceFactorVO>> getInfluenceFactorsByType(
            @PathVariable @ApiParam("影响因素类型") @NotNull Integer factorType,
            @RequestParam @ApiParam("租户ID") @NotNull Long tenantId,
            @RequestParam @ApiParam("账簿ID") @NotNull Long bookId) {
        try {
            List<InfluenceFactorVO> result = influenceFactorService.getInfluenceFactorsByType(factorType, tenantId, bookId);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/save", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("保存影响因素")
    public MyJsonBean<Long> saveInfluenceFactor(@Valid InfluenceFactorSaveParam param) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            Long factorId = influenceFactorService.saveInfluenceFactor(param, userId);
            return MyJsonBean.successData("保存成功", factorId);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/update", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("更新影响因素")
    public MyJsonBean<Boolean> updateInfluenceFactor(@Valid InfluenceFactorSaveParam param) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            boolean result = influenceFactorService.updateInfluenceFactor(param, userId);
            return MyJsonBean.successData(result ? "更新成功" : "更新失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    
    @DeleteMapping("/batch")
    @ApiOperation("批量删除影响因素")
    public MyJsonBean<Boolean> batchDeleteInfluenceFactors(
            @RequestBody @ApiParam("影响因素ID列表") @NotEmpty List<Long> factorIds) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            boolean result = influenceFactorService.batchDeleteInfluenceFactors(factorIds, userId);
            return MyJsonBean.successData(result ? "批量删除成功" : "批量删除失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    
    @PostMapping(value = "/updateStatus", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("启用/禁用影响因素")
    public MyJsonBean<Boolean> updateInfluenceFactorStatus(
            @RequestParam @ApiParam("影响因素ID") @NotNull Long id,
            @RequestParam @ApiParam("启用状态(0禁用1启用)") @NotNull Integer isEnabled) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            boolean result = influenceFactorService.updateInfluenceFactorStatus(id, isEnabled, userId);
            String message = isEnabled == 1 ? "启用成功" : "禁用成功";
            return MyJsonBean.successData(result ? message : "操作失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/delete", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("删除影响因素")
    public MyJsonBean<Boolean> deleteInfluenceFactor(@RequestParam @ApiParam("影响因素ID") @NotNull Long id) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            boolean result = influenceFactorService.deleteInfluenceFactor(id, userId);
            return MyJsonBean.successData(result ? "删除成功" : "删除失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/batchDelete", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("批量删除影响因素")
    public MyJsonBean<Boolean> batchDeleteInfluenceFactors(
            @RequestParam @ApiParam("影响因素ID列表") @NotEmpty String ids) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> factorIds = java.util.Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean result = influenceFactorService.batchDeleteInfluenceFactors(factorIds, userId);
            return MyJsonBean.successData(result ? "批量删除成功" : "批量删除失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/batchUpdateStatus", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("批量启用/禁用影响因素")
    public MyJsonBean<Boolean> batchUpdateInfluenceFactorStatus(
            @RequestParam @ApiParam("影响因素ID列表") @NotEmpty String ids,
            @RequestParam @ApiParam("启用状态(0禁用1启用)") @NotNull Integer isEnabled) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值

            // 将逗号分隔的字符串转换为List<Long>
            List<Long> factorIds = java.util.Arrays.stream(ids.split(","))
                    .map(String::trim)
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());

            boolean result = influenceFactorService.batchUpdateInfluenceFactorStatus(factorIds, isEnabled, userId);
            String message = isEnabled == 1 ? "批量启用成功" : "批量禁用成功";
            return MyJsonBean.successData(result ? message : "操作失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @PutMapping("/batch/status")
    @ApiOperation("批量启用/禁用影响因素")
    public MyJsonBean<Boolean> batchUpdateInfluenceFactorStatus(
            @RequestBody @ApiParam("影响因素ID列表") @NotEmpty List<Long> factorIds,
            @RequestParam @ApiParam("启用状态(0禁用1启用)") @NotNull Integer isEnabled) {
        try {
            // TODO: 从请求头或上下文中获取用户ID
            Long userId = 1L; // 临时使用固定值
            boolean result = influenceFactorService.batchUpdateInfluenceFactorStatus(factorIds, isEnabled, userId);
            String message = isEnabled == 1 ? "批量启用成功" : "批量禁用成功";
            return MyJsonBean.successData(result ? message : "操作失败", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @GetMapping("/check-code")
    @ApiOperation("检查编码是否存在")
    public MyJsonBean<Boolean> checkFactorCodeExists(
            @RequestParam @ApiParam("影响因素编码") String factorCode,
            @RequestParam @ApiParam("租户ID") @NotNull Long tenantId,
            @RequestParam @ApiParam("账簿ID") @NotNull Long bookId,
            @RequestParam(required = false) @ApiParam("排除的ID") Long excludeId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            boolean exists = influenceFactorService.checkFactorCodeExists(factorCode, tenantId, bookId, excludeId);
            return MyJsonBean.successData("检查完成", exists);
        } catch (Exception e) {
            log.error("检查影响因素编码失败", e);
            return MyJsonBean.errorData("检查失败：" + e.getMessage());
        }
    }

    /**
     * 获取登录用户信息
     * @return 登录用户信息
     */
    private TblStaffUtil getLoginStaff() throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            throw new Exception("用户已失效");
        }
        return loginStaff;
    }
}
