package com.financial.sharing.controller;

import com.financial.sharing.service.ExportService;
import com.financial.sharing.dto.ExportTaskQueryParam;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 导出功能控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/export")
@CrossOrigin
@Api(tags = "导出管理")
public class ExportController {

    @Resource
    private ExportService exportService;

    @Resource
    private UserProvider userProvider;

    
    /**
     * 权限验证
     */
    private TblStaffUtil validateUser() throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            return null;
        }
        return loginStaff;
    }

    @ApiOperation("科目余额表导出")
    @PostMapping("/account-balance")
    public String exportAccountBalance(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出科目余额表，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportAccountBalance(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出科目余额表失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("总账导出")
    @PostMapping("/general-ledger")
    public String exportGeneralLedger(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出总账，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportGeneralLedger(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出总账失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("凭证清单导出")
    @PostMapping("/voucher-list")
    public String exportVoucherList(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出凭证清单，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportVoucherList(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出凭证清单失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("辅助核算明细导出")
    @PostMapping("/auxiliary-detail")
    public String exportAuxiliaryDetail(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出辅助核算明细，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportAuxiliaryDetail(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出辅助核算明细失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("明细账导出")
    @PostMapping("/detail-ledger")
    public String exportDetailLedger(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出明细账，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportDetailLedger(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出明细账失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("日记账导出")
    @PostMapping("/journal")
    public String exportJournal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 导出日记账，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.exportJournal(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("导出日记账失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量导出")
    @PostMapping("/batch")
    public String batchExport(HttpServletRequest request,
                               HttpServletResponse response,
                               @RequestBody Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 批量导出，参数：{}", loginStaff.getStaffid(), params);

            Map<String, Object> result = exportService.batchExport(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "批量导出任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("批量导出失败", e);
            return JsonBean.error("导出失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询导出任务列表")
    @GetMapping("/tasks")
    public String getExportTasks(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @ApiParam(value = "任务状态", required = false) @RequestParam(required = false) String status,
                                  @ApiParam(value = "页码", required = false) @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                  @ApiParam(value = "每页大小", required = false) @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 查询导出任务列表，状态：{}", loginStaff.getStaffid(), status);

            Map<String, Object> params = new java.util.HashMap<>();
            params.put("status", status);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            Map<String, Object> result = exportService.getExportTasks(params, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "查询成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("查询导出任务列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询导出任务详情")
    @GetMapping("/tasks/{taskId}")
    public String getExportTaskDetail(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 查询导出任务详情，任务ID：{}", loginStaff.getStaffid(), taskId);

            Map<String, Object> result = exportService.getExportTaskDetail(taskId, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "查询成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("查询导出任务详情失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询导出进度")
    @GetMapping("/progress/{taskId}")
    public String getExportProgress(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 查询导出进度，任务ID：{}", loginStaff.getStaffid(), taskId);

            Map<String, Object> result = exportService.getExportProgress(taskId, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "查询成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("查询导出进度失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("取消导出任务")
    @PostMapping("/cancel/{taskId}")
    public String cancelExportTask(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 取消导出任务，任务ID：{}", loginStaff.getStaffid(), taskId);

            boolean result = exportService.cancelExportTask(taskId, loginStaff);
            if (result) {
                JsonBean jsonBean = new JsonBean(1, "取消任务成功", null);
                return jsonBean.toString();
            } else {
                return JsonBean.error("取消任务失败，任务可能已完成或不存在");
            }
        } catch (Exception e) {
            log.error("取消导出任务失败", e);
            return JsonBean.error("取消任务失败: " + e.getMessage());
        }
    }

    @ApiOperation("重新执行导出任务")
    @PostMapping("/retry/{taskId}")
    public String retryExportTask(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 重新执行导出任务，任务ID：{}", loginStaff.getStaffid(), taskId);

            Map<String, Object> result = exportService.retryExportTask(taskId, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "重新执行任务创建成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("重新执行导出任务失败", e);
            return JsonBean.error("重新执行任务失败: " + e.getMessage());
        }
    }

    @ApiOperation("下载导出文件")
    @GetMapping("/download/{taskId}")
    public void downloadExportFile(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            log.info("用户 {} 下载导出文件，任务ID：{}", loginStaff.getStaffid(), taskId);

            exportService.downloadExportFile(taskId, loginStaff, request, response);
        } catch (Exception e) {
            log.error("下载导出文件失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("下载失败: " + e.getMessage());
            } catch (Exception ex) {
                log.error("写入响应失败", ex);
            }
        }
    }

    @ApiOperation("删除导出任务")
    @DeleteMapping("/tasks/{taskId}")
    public String deleteExportTask(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 删除导出任务，任务ID：{}", loginStaff.getStaffid(), taskId);

            boolean result = exportService.deleteExportTask(taskId, loginStaff);
            if (result) {
                JsonBean jsonBean = new JsonBean(1, "删除任务成功", null);
                return jsonBean.toString();
            } else {
                return JsonBean.error("删除任务失败，任务可能不存在或无权限删除");
            }
        } catch (Exception e) {
            log.error("删除导出任务失败", e);
            return JsonBean.error("删除任务失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取导出历史统计")
    @GetMapping("/statistics")
    public String getExportStatistics(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @ApiParam(value = "统计类型", required = false) @RequestParam(required = false) String statisticsType) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 获取导出历史统计，统计类型：{}", loginStaff.getStaffid(), statisticsType);

            Map<String, Object> result = exportService.getExportStatistics(statisticsType, loginStaff);
            JsonBean jsonBean = new JsonBean(1, "查询成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("获取导出历史统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取导出模板列表")
    @GetMapping("/templates")
    public String getExportTemplates(HttpServletRequest request,
                                       HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = validateUser();
            if (loginStaff == null) {
                return JsonBean.error("用户已失效");
            }

            log.info("用户 {} 获取导出模板列表", loginStaff.getStaffid());

            List<Map<String, Object>> result = exportService.getExportTemplates(loginStaff);
            JsonBean jsonBean = new JsonBean(1, "查询成功", result);
            return jsonBean.toString();
        } catch (Exception e) {
            log.error("获取导出模板列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }
}