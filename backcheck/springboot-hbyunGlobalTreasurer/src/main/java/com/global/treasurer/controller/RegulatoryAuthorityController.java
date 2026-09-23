package com.global.treasurer.controller;

import com.global.treasurer.dto.export.ExportRegulatoryAuthorityDTO;
import com.global.treasurer.util.excel.ExcelExport;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryAuthority;
import com.global.treasurer.service.RegulatoryAuthorityService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 监管机构Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/authority", "/globalTreasurer/regulatory/authority"})
@Api(tags = "监管机构管理")
public class RegulatoryAuthorityController {
    private static final Logger log = LoggerFactory.getLogger(RegulatoryAuthorityController.class);

    @Resource
    private RegulatoryAuthorityService authorityService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询监管机构列表")
    public String getAuthorityList(@RequestParam(required = false) String authorityName,
                                   @RequestParam(required = false) String authorityType,
                                   @RequestParam(required = false) Integer isActive,
                                   @RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("authorityName", authorityName);
            params.put("authorityType", authorityType);
            params.put("isActive", isActive);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblRegulatoryAuthority> pageInfo = authorityService.getAuthorityList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询监管机构列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{authorityId}")
    @ResponseBody
    @ApiOperation("根据ID获取监管机构详情")
    public String getAuthorityById(@PathVariable String authorityId,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblRegulatoryAuthority authority = authorityService.getAuthorityById(authorityId);
            return new JsonBean(1, "成功", authority).toJson();
        } catch (Exception e) {
            log.error("获取监管机构详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增监管机构")
    public String addAuthority(@FlexibleRequestBody TblRegulatoryAuthority authority,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            authority.setAuthorityId(null);
            TblRegulatoryAuthority saved = authorityService.saveAuthority(authority);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增监管机构失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改监管机构")
    public String updateAuthority(@FlexibleRequestBody TblRegulatoryAuthority authority,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (authority.getAuthorityId() == null || authority.getAuthorityId().isEmpty()) {
                return new JsonBean(0, "监管机构ID不能为空", null).toJson();
            }

            TblRegulatoryAuthority saved = authorityService.saveAuthority(authority);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改监管机构失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{authorityIds}")
    @ResponseBody
    @ApiOperation("删除监管机构")
    public String deleteAuthority(@PathVariable String authorityIds,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            String[] ids = authorityIds.split(",");
            if (ids.length == 1) {
                authorityService.deleteAuthority(ids[0]);
            } else {
                authorityService.batchDeleteAuthorities(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除监管机构失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/type/{authorityType}")
    @ResponseBody
    @ApiOperation("根据机构类型查询监管机构")
    public String getAuthoritiesByType(@PathVariable String authorityType,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryAuthority> authorities = authorityService.getAuthoritiesByType(authorityType);
            return new JsonBean(1, "成功", authorities).toJson();
        } catch (Exception e) {
            log.error("根据机构类型查询监管机构失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/active")
    @ResponseBody
    @ApiOperation("查询活跃的监管机构")
    public String getActiveAuthorities(@RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryAuthority> authorities = authorityService.getActiveAuthorities();
            return new JsonBean(1, "成功", authorities).toJson();
        } catch (Exception e) {
            log.error("查询活跃的监管机构失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/important")
    @ResponseBody
    @ApiOperation("查询重要监管机构")
    public String getImportantAuthorities(@RequestHeader(value = "token", required = false) String token,
                                          HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryAuthority> authorities = authorityService.getImportantAuthorities();
            return new JsonBean(1, "成功", authorities).toJson();
        } catch (Exception e) {
            log.error("查询重要监管机构失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/{authorityId}/status/{isActive}")
    @ResponseBody
    @ApiOperation("激活/停用监管机构")
    public String toggleAuthorityStatus(@PathVariable String authorityId,
                                        @PathVariable Integer isActive,
                                        @RequestHeader(value = "token", required = false) String token,
                                        HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            authorityService.toggleAuthorityStatus(authorityId, isActive);
            return new JsonBean(1, "操作成功", null).toJson();
        } catch (Exception e) {
            log.error("激活/停用监管机构失败", e);
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出监管机构数据")
    public void exportAuthority(@FlexibleRequestBody Map<String, Object> params,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }
            List<TblRegulatoryAuthority> list = authorityService.exportAuthorityList(params);
            List<ExportRegulatoryAuthorityDTO> exportList = list.stream()
                    .map(ExportRegulatoryAuthorityDTO::fromEntity)
                    .collect(Collectors.toList());
            String filename = "监管机构数据_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("监管机构数据", ExportRegulatoryAuthorityDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出监管机构数据失败", e);
        }
    }
}
