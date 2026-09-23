package com.financial.sharing.controller;

import com.financial.sharing.service.OrganizationService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.OrganizationSaveParam;
import com.financial.sharing.vo.result.OrganizationVO;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 组织架构配置控制器
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/common/organization")
@Api(tags = "组织架构配置管理")
@Validated
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/tree")
    @ApiOperation("获取组织架构树")
    public MyJsonBean<List<OrganizationVO>> getOrganizationTree(@RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            List<OrganizationVO> result = organizationService.getOrganizationTree(tenantId);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("获取组织架构树失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/save", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("保存组织信息")
    public MyJsonBean<String> saveOrganization(@Valid OrganizationSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            boolean result = organizationService.saveOrganization(param);
            if (result) {
                return MyJsonBean.successData("保存成功", null);
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            log.error("保存组织信息失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("获取组织详情")
    public MyJsonBean<OrganizationVO> getOrganizationById(@PathVariable @NotNull(message = "组织ID不能为空") Long id) {
        try {
            // 权限验证
            getLoginStaff();

            OrganizationVO result = organizationService.getOrganizationById(id);
            if (result != null) {
                return MyJsonBean.successData("查询成功", result);
            } else {
                return MyJsonBean.errorData("组织不存在", null);
            }
        } catch (Exception e) {
            log.error("获取组织详情失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @PostMapping(value = "/update", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("更新组织信息")
    public MyJsonBean<String> updateOrganization(@Valid OrganizationSaveParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            boolean result = organizationService.updateOrganization(param);
            if (result) {
                return MyJsonBean.successData("更新成功", null);
            } else {
                return MyJsonBean.errorData("更新失败");
            }
        } catch (Exception e) {
            log.error("更新组织信息失败", e);
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除组织")
    public MyJsonBean<String> deleteOrganization(@PathVariable @NotNull(message = "组织ID不能为空") Long id) {
        try {
            // 权限验证
            getLoginStaff();

            boolean result = organizationService.deleteOrganization(id);
            if (result) {
                return MyJsonBean.successData("删除成功", null);
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            log.error("删除组织失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @ApiOperation("启用/禁用组织")
    public MyJsonBean<String> updateOrganizationStatus(
            @PathVariable @NotNull(message = "组织ID不能为空") Long id,
            @RequestParam @NotNull(message = "启用状态不能为空") Integer isEnabled) {
        try {
            // 权限验证
            getLoginStaff();

            boolean result = organizationService.updateOrganizationStatus(id, isEnabled);
            if (result) {
                String statusText = isEnabled == 1 ? "启用" : "禁用";
                return MyJsonBean.successData(statusText + "成功", null);
            } else {
                return MyJsonBean.errorData("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新组织状态失败", e);
            return MyJsonBean.errorData("状态更新失败：" + e.getMessage());
        }
    }

    @GetMapping("/children/{parentId}")
    @ApiOperation("获取下级组织列表")
    public MyJsonBean<List<OrganizationVO>> getChildOrganizations(
            @PathVariable @NotNull(message = "父级ID不能为空") Long parentId,
            @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            List<OrganizationVO> result = organizationService.getChildOrganizations(parentId, tenantId);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("获取下级组织列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @PostMapping("/sync")
    @ApiOperation("同步组织数据")
    public MyJsonBean<String> syncOrganizations() {
        try {
            // 权限验证
            getLoginStaff();

            String result = organizationService.syncOrganizations();
            return MyJsonBean.successData("同步成功", result);
        } catch (Exception e) {
            log.error("同步组织数据失败", e);
            return MyJsonBean.errorData("同步失败：" + e.getMessage());
        }
    }

    @GetMapping("/type/{orgType}")
    @ApiOperation("根据组织类型查询")
    public MyJsonBean<List<OrganizationVO>> getByOrgType(
            @PathVariable @NotNull(message = "组织类型不能为空") String orgType,
            @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            // 路径变量是 String，service 期望 Integer，做一次安全转换
            Integer orgTypeInt;
            try {
                orgTypeInt = Integer.valueOf(orgType);
            } catch (NumberFormatException nfe) {
                return MyJsonBean.errorData("组织类型必须是整数");
            }
            List<OrganizationVO> result = organizationService.getByOrgType(orgTypeInt, tenantId);
            return MyJsonBean.successData("查询成功", result);
        } catch (Exception e) {
            log.error("根据组织类型查询失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @PutMapping("/move")
    @ApiOperation("移动组织")
    public MyJsonBean<String> moveOrganization(
            @RequestParam @NotNull(message = "组织ID不能为空") Long orgId,
            @RequestParam(required = false) Long newParentId,
            @RequestParam(required = false) Long tenantId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = getLoginStaff();
            if (loginStaff != null && loginStaff.getCurrentOrg() != null) {
                tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            boolean result = organizationService.moveOrganization(orgId, newParentId, tenantId);
            if (result) {
                return MyJsonBean.successData("移动成功", null);
            } else {
                return MyJsonBean.errorData("移动失败");
            }
        } catch (Exception e) {
            log.error("移动组织失败", e);
            return MyJsonBean.errorData("移动失败：" + e.getMessage());
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