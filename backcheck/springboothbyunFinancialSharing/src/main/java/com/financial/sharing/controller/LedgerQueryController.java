package com.financial.sharing.controller;

import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.service.GeneralLedgerService;
import com.financial.sharing.service.LedgerQueryExportService;
import com.financial.sharing.vo.param.LedgerQueryExportParam;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账本查询控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "账本查询")
@RestController
@RequestMapping("/ledger-query")
@CrossOrigin
public class LedgerQueryController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private GeneralLedgerService generalLedgerService;

    @Resource
    private LedgerQueryExportService ledgerQueryExportService;

    // ==================== 账本查询 ====================

    @ApiOperation("分页查询总账数据")
    @PostMapping("/getList")
    public String getLedgerQueryPage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody GeneralLedgerQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L); // 默认账簿ID
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue()); // 从用户信息获取租户ID
            }

            log.info("用户 {} 查询账本数据，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> ledgerList = generalLedgerService.getSubjectLedgerQuery(param);

            // 手动构建分页信息
            PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(ledgerList);
            if (param.getPageNo() != null) {
                pageInfo.setPageNum(param.getPageNo());
            }
            if (param.getPageSize() != null) {
                pageInfo.setPageSize(param.getPageSize());
            }

            // 转换为前端需要的格式
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询账本数据失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("查询科目明细账")
    @GetMapping("/detail")
    public String getSubjectDetailLedger(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @ApiParam(value = "科目编码", required = true) @RequestParam String subjectCode,
                                       @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period,
                                       @ApiParam(value = "账簿ID", required = false) @RequestParam(required = false) Long bookId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 默认账簿ID
            if (bookId == null) {
                bookId = 1L;
            }

            log.info("用户 {} 查询科目明细账，科目：{}，期间：{}", loginStaff.getStaffid(), subjectCode, period);

            // 调用服务查询真实数据
            Map<String, Object> result = generalLedgerService.getSubjectDetailLedger(
                    subjectCode, period, bookId, loginStaff.getCurrentOrg().getOrgid().longValue());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询科目明细账失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("查询科目总账")
    @PostMapping("/subject")
    public String getSubjectLedgerQuery(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody GeneralLedgerQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询科目总账，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = generalLedgerService.getSubjectLedgerQuery(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询科目总账失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("多栏式账本查询")
    @GetMapping("/multi-column")
    public String getMultiColumnLedgerQuery(HttpServletRequest request,
                                           HttpServletResponse response,
                                           @ApiParam(value = "科目编码", required = true) @RequestParam String subjectCode,
                                           @ApiParam(value = "期间范围", required = false) @RequestParam(required = false) String periodRange) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            log.info("用户 {} 查询多栏式账本，科目：{}，期间范围：{}", loginStaff.getStaffid(), subjectCode, periodRange);

            // 调用服务查询真实数据
            Map<String, Object> result = generalLedgerService.getMultiColumnLedgerQuery(
                    subjectCode, periodRange, 1L, loginStaff.getCurrentOrg().getOrgid().longValue());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询多栏式账本失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("辅助核算账本查询")
    @GetMapping("/auxiliary")
    public String getAuxiliaryLedgerQuery(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode,
                                         @ApiParam(value = "辅助核算类型", required = false) @RequestParam(required = false) String auxiliaryType,
                                         @ApiParam(value = "辅助核算值", required = false) @RequestParam(required = false) String auxiliaryValue,
                                         @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            log.info("用户 {} 查询辅助核算账本，科目：{}，类型：{}，期间：{}",
                    loginStaff.getStaffid(), subjectCode, auxiliaryType, period);

            // 调用服务查询真实数据
            List<Map<String, Object>> result = generalLedgerService.getAuxiliaryLedgerQuery(
                    subjectCode, auxiliaryType, auxiliaryValue, period,
                    1L, loginStaff.getCurrentOrg().getOrgid().longValue());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询辅助核算账本失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("账本汇总查询")
    @PostMapping("/summary")
    public String getLedgerSummaryQuery(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestBody GeneralLedgerQueryParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询账本汇总，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            Map<String, Object> result = generalLedgerService.getLedgerSummaryQuery(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询账本汇总失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    // ==================== 数据导出 ====================

    @ApiOperation("导出账本查询数据")
    @PostMapping("/export")
    public String exportLedgerQuery(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody LedgerQueryExportParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 导出账本查询数据，参数：{}", loginStaff.getStaffid(), param);

            // 异步导出数据
            String taskId = ledgerQueryExportService.asyncExportLedgerQuery(param);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("message", "导出任务已创建");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("导出账本查询数据失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("导出失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("查询导出进度")
    @GetMapping("/export/progress")
    public String getExportProgress(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @ApiParam(value = "任务ID", required = true) @RequestParam String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            log.info("用户 {} 查询导出进度，任务ID：{}", loginStaff.getStaffid(), taskId);

            // 查询导出进度
            Object progress = ledgerQueryExportService.getExportProgress(taskId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(progress);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询导出进度失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("查询失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("导出预览")
    @PostMapping("/export/preview")
    public String exportPreview(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody LedgerQueryExportParam param) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 设置租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 导出预览，参数：{}", loginStaff.getStaffid(), param);

            // 导出预览
            Object preview = ledgerQueryExportService.exportPreview(param);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(preview);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("导出预览失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("预览失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }

    @ApiOperation("取消导出任务")
    @DeleteMapping("/export/{taskId}")
    public String cancelExportTask(HttpServletRequest request,
                                  HttpServletResponse response,
                                  @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            log.info("用户 {} 取消导出任务，任务ID：{}", loginStaff.getStaffid(), taskId);

            // 取消导出任务
            ledgerQueryExportService.cancelExport(taskId);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("cancelled", true);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("操作成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("取消导出任务失败", e);
            JsonBean json = new JsonBean();
            json.setCode(0);
            json.setMsg("取消失败: " + e.getMessage());
            json.setData(null);
            return JsonMapper.nonNullMapper().toJson(json);
        }
    }
}