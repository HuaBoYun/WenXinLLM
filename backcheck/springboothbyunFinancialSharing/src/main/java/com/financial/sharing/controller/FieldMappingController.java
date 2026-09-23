package com.financial.sharing.controller;

import com.financial.sharing.dto.FieldMappingBatchParam;
import com.financial.sharing.dto.FieldMappingImportParam;
import com.financial.sharing.dto.FieldMappingParam;
import com.financial.sharing.dto.FieldMappingSaveParam;
import com.financial.sharing.service.FieldMappingService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 字段映射管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/field-mapping")
@CrossOrigin
@Api(tags = "字段映射管理")
public class FieldMappingController {

    @Resource
    private FieldMappingService fieldMappingService;

    @Resource
    private UserProvider userProvider;

    /**
     * 获取当前用户信息
     */
    private TblStaffUtil getCurrentUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            JsonBean json = new JsonBean(401, "用户已失效", null);
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(json.toString());
            return null;
        }
        return loginStaff;
    }

    @PostMapping("/list")
    @ApiOperation("查询字段映射列表")
    public String getFieldMappingList(@RequestBody FieldMappingParam param,
                                     HttpServletRequest request,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            return fieldMappingService.getFieldMappingList(param).toString();
        } catch (Exception e) {
            log.error("查询字段映射列表失败", e);
            return JsonBean.error("查询字段映射列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{mappingId}")
    @ApiOperation("获取字段映射详情")
    public String getFieldMapping(@PathVariable Long mappingId,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            return fieldMappingService.getFieldMapping(mappingId, loginStaff.getCurrentOrg().getOrgid().longValue()).toString();
        } catch (Exception e) {
            log.error("获取字段映射详情失败", e);
            return JsonBean.error("获取字段映射详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/save")
    @ApiOperation("保存字段映射")
    public String saveFieldMapping(@RequestBody FieldMappingSaveParam param,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setCreateUser(loginStaff.getStaffid().longValue());
            param.setUpdateUser(loginStaff.getStaffid().longValue());

            Long mappingId = fieldMappingService.saveFieldMapping(param);
            JsonBean jsonBean = new JsonBean(1, "保存成功", mappingId);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("保存字段映射失败", e);
            return JsonBean.error("保存字段映射失败: " + e.getMessage());
        }
    }

    @PutMapping("/{mappingId}")
    @ApiOperation("更新字段映射")
    public String updateFieldMapping(@PathVariable Long mappingId,
                                    @RequestBody FieldMappingSaveParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setUpdateUser(loginStaff.getStaffid().longValue());

            fieldMappingService.updateFieldMapping(mappingId, param);
            JsonBean jsonBean = new JsonBean(1, "更新成功", null);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("更新字段映射失败", e);
            return JsonBean.error("更新字段映射失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{mappingId}")
    @ApiOperation("删除字段映射")
    public String deleteFieldMapping(@PathVariable Long mappingId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            fieldMappingService.deleteFieldMapping(mappingId, loginStaff.getCurrentOrg().getOrgid().longValue());
            JsonBean jsonBean = new JsonBean(1, "删除成功", null);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("删除字段映射失败", e);
            return JsonBean.error("删除字段映射失败: " + e.getMessage());
        }
    }

    @GetMapping("/datasource/{dataSourceId}")
    @ApiOperation("根据数据源ID查询字段映射")
    public String getFieldMappingByDataSourceId(@PathVariable Long dataSourceId,
                                               HttpServletRequest request,
                                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            return fieldMappingService.getFieldMappingByDataSourceId(
                dataSourceId,
                loginStaff.getCurrentOrg().getOrgid().longValue()
            ).toString();
        } catch (Exception e) {
            log.error("根据数据源ID查询字段映射失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch")
    @ApiOperation("批量操作字段映射")
    public String batchOperationFieldMapping(@RequestBody FieldMappingBatchParam param,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setUpdateUser(loginStaff.getStaffid().longValue());

            return fieldMappingService.batchOperationFieldMapping(param).toString();
        } catch (Exception e) {
            log.error("批量操作字段映射失败", e);
            return JsonBean.error("批量操作失败: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    @ApiOperation("导入字段映射")
    public String importFieldMapping(@RequestBody FieldMappingImportParam param,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            param.setCreateUser(loginStaff.getStaffid().longValue());

            return fieldMappingService.importFieldMapping(param).toString();
        } catch (Exception e) {
            log.error("导入字段映射失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/export/{dataSourceId}")
    @ApiOperation("导出字段映射")
    public String exportFieldMapping(@PathVariable Long dataSourceId,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            return fieldMappingService.exportFieldMapping(dataSourceId, loginStaff.getCurrentOrg().getOrgid().longValue()).toString();
        } catch (Exception e) {
            log.error("导出字段映射失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @PostMapping("/copy")
    @ApiOperation("复制字段映射")
    public String copyFieldMapping(@RequestParam Long sourceDataSourceId,
                                  @RequestParam Long targetDataSourceId,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            fieldMappingService.copyFieldMapping(
                sourceDataSourceId,
                targetDataSourceId,
                loginStaff.getCurrentOrg().getOrgid().longValue(),
                loginStaff.getStaffid().longValue()
            );
            JsonBean jsonBean = new JsonBean(1, "复制成功", null);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("复制字段映射失败", e);
            return JsonBean.error("复制失败: " + e.getMessage());
        }
    }

    @PostMapping("/sort")
    @ApiOperation("更新字段映射排序")
    public String updateSortOrder(@RequestParam Long mappingId,
                                 @RequestParam Integer sortOrder,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = getCurrentUser(request, response);
            if (loginStaff == null) {
                return null;
            }

            fieldMappingService.updateSortOrder(mappingId, sortOrder, loginStaff.getStaffid().longValue());
            JsonBean jsonBean = new JsonBean(1, "排序更新成功", null);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("更新字段映射排序失败", e);
            return JsonBean.error("排序更新失败: " + e.getMessage());
        }
    }
}