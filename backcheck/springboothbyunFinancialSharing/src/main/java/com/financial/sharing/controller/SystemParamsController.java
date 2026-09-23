package com.financial.sharing.controller;

import com.financial.sharing.service.SystemParamsService;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.vo.param.SystemParamsQueryParam;
import com.financial.sharing.vo.param.SystemParamsSaveParam;
import com.financial.sharing.vo.result.SystemParamsVO;
import com.hbfk.util.JsonBean;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统参数配置控制器
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/common/systemParams")
@Api(tags = "系统参数配置管理")
@Validated
public class SystemParamsController {

    @Autowired
    private SystemParamsService systemParamsService;

    @Resource
    private UserProvider userProvider;

    @PostMapping(value = "/list", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("获取系统参数列表")
    public String getSystemParamsList(SystemParamsQueryParam param,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            // 设置租户ID
            if (loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            } else {
                // 如果没有组织信息，使用默认租户ID
                param.setTenantId(1L);
            }

            Object result = systemParamsService.getSystemParamsPage(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("获取成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取系统参数列表失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("获取系统参数列表失败：" + e.getMessage());
            return JsonMapper.toJson(json);
        }
    }

    @PostMapping(value = "/save", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("保存系统参数")
    public String saveSystemParams(@Valid SystemParamsSaveParam param,
                                 HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            // 设置租户ID
            if (loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            boolean result = systemParamsService.saveSystemParams(param);
            JsonBean json = new JsonBean();
            if (result) {
                json.setCode(1);
                json.setMsg("保存成功");
                json.setData(true);
            } else {
                json.setCode(0);
                json.setMsg("保存失败");
            }
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存系统参数失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("保存失败：" + e.getMessage());
            return JsonMapper.toJson(json);
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID获取参数详情")
    public String getSystemParamsById(@PathVariable Long id,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            SystemParamsVO result = systemParamsService.getSystemParamsById(id);
            if (result == null) {
                return JsonMapper.toJson(new JsonBean(0, "参数不存在", null));
            }

            return JsonMapper.toJson(new JsonBean(1, "获取成功", result));
        } catch (Exception e) {
            log.error("获取参数详情失败", e);
            return JsonMapper.toJson(new JsonBean(0, "获取参数详情失败", null));
        }
    }

    @PostMapping(value = "/update", consumes = "application/x-www-form-urlencoded")
    @ApiOperation("更新系统参数")
    public String updateSystemParams(@Valid SystemParamsSaveParam param,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            // 设置租户ID
            if (loginStaff.getCurrentOrg() != null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            boolean result = systemParamsService.updateSystemParams(param);
            JsonBean json = new JsonBean();
            if (result) {
                json.setCode(1);
                json.setMsg("更新成功");
                json.setData(true);
            } else {
                json.setCode(0);
                json.setMsg("更新失败");
            }
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新系统参数失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("更新失败：" + e.getMessage());
            return JsonMapper.toJson(json);
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除系统参数")
    public String deleteSystemParams(@PathVariable Long id,
                                    HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            boolean result = systemParamsService.deleteSystemParams(id);
            JsonBean json = new JsonBean();
            if (result) {
                json.setCode(1);
                json.setMsg("删除成功");
            } else {
                json.setCode(0);
                json.setMsg("删除失败");
            }
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("删除系统参数失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("删除失败：" + e.getMessage());
            return JsonMapper.toJson(json);
        }
    }

    @PutMapping("/status")
    @ApiOperation("更新系统参数状态")
    public String updateSystemParamsStatus(@RequestParam Long id,
                                          @RequestParam Integer isEnabled,
                                          HttpServletRequest request, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.toJson(json));
                return JsonMapper.toJson(json);
            }

            boolean result = systemParamsService.updateSystemParamsStatus(id, isEnabled);
            JsonBean json = new JsonBean();
            if (result) {
                String statusText = isEnabled == 1 ? "启用" : "禁用";
                json.setCode(1);
                json.setMsg(statusText + "成功");
            } else {
                json.setCode(0);
                json.setMsg("操作失败");
            }
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("更新参数状态失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("更新状态失败");
            return JsonMapper.toJson(json);
        }
    }

}