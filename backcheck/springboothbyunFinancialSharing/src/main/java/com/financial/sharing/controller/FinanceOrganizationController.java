package com.financial.sharing.controller;

import com.financial.sharing.dto.param.FinanceOrganizationDTO;
import com.financial.sharing.service.FinanceOrganizationService;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.vo.result.FinanceOrganizationVO;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 财务组织管理Controller
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/common/finance-organization")
@Api(tags = "财务组织管理")
public class FinanceOrganizationController {

    @Autowired
    private FinanceOrganizationService financeOrganizationService;

    @Autowired
    private UserProvider userProvider;

    private TblStaffUtil loginStaff;

    /**
     * 获取组织树形结构
     */
    @GetMapping("/tree")
    @ApiOperation("获取组织树形结构")
    public String getOrganizationTree(HttpServletRequest request, HttpServletResponse response) {
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

            // 暂时使用默认租户ID和账簿ID，后续可以从用户信息或参数中获取
            Long tenantId = 1000L;
            Long bookId = 1L;

            List<FinanceOrganizationVO> tree = financeOrganizationService.getOrganizationTree(tenantId, bookId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("获取成功");
            json.setData(tree);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取组织树失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("获取组织树失败：" + e.getMessage());
            return JsonMapper.toJson(json);
        }
    }

    /**
     * 根据ID获取组织详情
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID获取组织详情")
    public String getOrganizationById(@PathVariable Long id,
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

            FinanceOrganizationVO organization = financeOrganizationService.getOrganizationById(id);
            if (organization == null) {
                return JsonMapper.toJson(new JsonBean(0, "组织不存在", null));
            }

            return JsonMapper.toJson(new JsonBean(1, "获取成功", organization));
        } catch (Exception e) {
            log.error("获取组织详情失败", e);
            return JsonMapper.toJson(new JsonBean(0, "获取组织详情失败", null));
        }
    }

    /**
     * 保存或更新组织
     */
    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新组织")
    public String saveOrUpdateOrganization(@RequestBody FinanceOrganizationDTO dto,
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

            // 设置默认值
            if (dto.getTenantId() == null) {
                dto.setTenantId(1000L); // 默认租户ID
            }
            if (dto.getBookId() == null) {
                dto.setBookId(1L); // 默认账簿ID
            }
            if (dto.getIsEnabled() == null) {
                dto.setIsEnabled(1); // 默认启用
            }

            boolean result = financeOrganizationService.saveOrUpdateOrganization(dto);
            if (result) {
                return JsonMapper.toJson(new JsonBean(1, "保存成功", true));
            } else {
                return JsonMapper.toJson(new JsonBean(0, "保存失败", null));
            }
        } catch (Exception e) {
            log.error("保存或更新组织失败", e);
            return JsonMapper.toJson(new JsonBean(0, "保存失败：" + e.getMessage(), null));
        }
    }

    /**
     * 删除组织
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除组织")
    public String deleteOrganization(@PathVariable Long id,
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

            boolean result = financeOrganizationService.deleteOrganization(id);
            if (result) {
                return JsonMapper.toJson(new JsonBean(1, "删除成功", null));
            } else {
                return JsonMapper.toJson(new JsonBean(0, "删除失败", null));
            }
        } catch (Exception e) {
            log.error("删除组织失败", e);
            return JsonMapper.toJson(new JsonBean(0, "删除失败：" + e.getMessage(), null));
        }
    }

    /**
     * 更新组织状态
     */
    @PutMapping("/status")
    @ApiOperation("更新组织状态")
    public String updateOrganizationStatus(@RequestParam Long id,
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

            boolean result = financeOrganizationService.updateOrganizationStatus(id, isEnabled);
            if (result) {
                String statusText = isEnabled == 1 ? "启用" : "禁用";
                return JsonMapper.toJson(new JsonBean(1, statusText + "成功", null));
            } else {
                return JsonMapper.toJson(new JsonBean(0, "操作失败", null));
            }
        } catch (Exception e) {
            log.error("更新组织状态失败", e);
            return JsonMapper.toJson(new JsonBean(0, "更新状态失败", null));
        }
    }

    /**
     * 同步组织数据
     */
    @PostMapping("/sync")
    @ApiOperation("同步组织数据")
    public String syncOrganizations(HttpServletRequest request, HttpServletResponse response) {
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

            boolean result = financeOrganizationService.syncOrganizations();
            if (result) {
                return JsonMapper.toJson(new JsonBean(1, "同步成功", null));
            } else {
                return JsonMapper.toJson(new JsonBean(0, "同步失败", null));
            }
        } catch (Exception e) {
            log.error("同步组织数据失败", e);
            return JsonMapper.toJson(new JsonBean(0, "同步失败", null));
        }
    }

  }