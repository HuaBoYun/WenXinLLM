package com.financial.sharing.controller;

import com.financial.sharing.dto.BalanceRecalculateParam;
import com.financial.sharing.dto.GeneralLedgerQueryParam;
import com.financial.sharing.oracle.entity.GeneralLedgerEntity;
import com.financial.sharing.oracle.entity.SubjectBalanceEntity;
import com.financial.sharing.service.BalanceService;
import com.financial.sharing.service.GeneralLedgerService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 总账管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Api(tags = "总账管理")
@RestController
@RequestMapping("/general-ledger")
@CrossOrigin
public class GeneralLedgerController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private GeneralLedgerService generalLedgerService;

    @Resource
    private BalanceService balanceService;

    // ==================== 明细账管理 ====================

    @ApiOperation("分页查询明细账数据")
    @PostMapping("/detail/getList")
    public String getDetailLedgerPage(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody GeneralLedgerQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L); // 默认账簿ID
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue()); // 从用户信息获取租户ID
            }

            log.info("用户 {} 查询明细账，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            PageInfo<GeneralLedgerEntity> pageInfo = generalLedgerService.getDetailLedgerPage(param);

            // 转换为前端需要的格式
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("查询明细账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取特定科目的明细账")
    @GetMapping("/detail/subject")
    public String getSubjectDetailLedger(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @ApiParam(value = "科目编码", required = true) @RequestParam String subjectCode,
                                        @ApiParam(value = "期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取科目明细账，科目：{}，期间：{}", loginStaff.getStaffid(), subjectCode, period);

            // 调用服务查询真实数据
            Map<String, Object> result = generalLedgerService.getSubjectDetailLedger(
                    subjectCode, period, 1L, loginStaff.getCurrentOrg().getOrgid().longValue()); // 使用默认的账簿ID和用户租户ID

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("获取科目明细账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出明细账数据")
    @PostMapping("/detail/export")
    public String exportDetailLedger(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody GeneralLedgerQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L);
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 导出明细账，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务导出数据
            Map<String, Object> result = generalLedgerService.exportDetailLedger(param);

            return createSuccessResponse("导出任务创建成功", result);
        } catch (Exception e) {
            log.error("导出明细账失败", e);
            return createErrorResponse("导出失败: " + e.getMessage());
        }
    }

    // ==================== 账户余额管理 ====================

    @ApiOperation("分页查询科目余额数据")
    @PostMapping("/balance/getList")
    public String getGeneralLedgerBalancePage(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @RequestBody GeneralLedgerQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L);
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 查询科目余额，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务查询真实数据
            PageInfo<SubjectBalanceEntity> pageInfo = balanceService.getGeneralLedgerBalancePage(param);

            // 转换为前端需要的格式
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return createSuccessResponse("查询成功", data);
        } catch (Exception e) {
            log.error("查询科目余额失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("重新计算科目余额")
    @PostMapping("/balance/recalculate")
    public String recalculateSubjectBalance(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @RequestBody BalanceRecalculateParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            if (param.getPeriod() == null || param.getPeriod().trim().isEmpty()) {
                return createErrorResponse("会计期间不能为空");
            }

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L);
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 重新计算科目余额，参数：{}", loginStaff.getStaffid(), param);

            // 调用服务重新计算
            Map<String, Object> result = balanceService.recalculateSubjectBalance(param);

            return createSuccessResponse("重新计算任务已启动", result);
        } catch (Exception e) {
            log.error("重新计算科目余额失败", e);
            return createErrorResponse("重新计算失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取科目余额详情")
    @GetMapping("/balance/detail")
    public String getSubjectBalanceDetail(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(value = "科目编码", required = true) @RequestParam String subjectCode,
                                         @ApiParam(value = "期间", required = true) @RequestParam String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取科目余额详情，科目：{}，期间：{}", loginStaff.getStaffid(), subjectCode, period);

            // 调用服务查询真实数据
            Map<String, Object> detail = balanceService.getSubjectBalanceDetail(
                    subjectCode, period, 1L, loginStaff.getCurrentOrg().getOrgid().longValue()); // 使用默认的账簿ID和用户租户ID

            return createSuccessResponse("查询成功", detail);
        } catch (Exception e) {
            log.error("获取科目余额详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    // ==================== 账簿查询管理 ====================

    @ApiOperation("分页查询总账数据")
    @PostMapping("/query")
    public String queryGeneralLedger(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestBody Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询总账数据，参数：{}", loginStaff.getStaffid(), params);

            // 返回模拟数据
            List<Map<String, Object>> dataList = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("ledgerId", "LEDGER001");
            item.put("accountCode", "1001");
            item.put("accountName", "库存现金");
            item.put("debitAmount", 50000.00);
            item.put("creditAmount", 30000.00);
            item.put("balance", 20000.00);
            dataList.add(item);

            Map<String, Object> result = new HashMap<>();
            result.put("tlist", dataList);
            result.put("totalRecord", 1);
            result.put("pageNo", 1);
            result.put("pageSize", 10);

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("查询总账数据失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取总账查询统计数据")
    @GetMapping("/query/statistics")
    public String getLedgerQueryStatistics(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @ApiParam(value = "会计期间", required = true) @RequestParam String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 获取总账查询统计，期间：{}", loginStaff.getStaffid(), period);

            // 调用服务查询真实统计数据
            Map<String, Object> statistics = generalLedgerService.getLedgerQueryStatistics(period, 1L, loginStaff.getCurrentOrg().getOrgid().longValue());

            return createSuccessResponse("查询成功", statistics);
        } catch (Exception e) {
            log.error("获取总账查询统计失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询科目明细账（别名接口）")
    @GetMapping("/query/subject-detail")
    public String getSubjectDetailLedgerQuery(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @ApiParam(value = "科目编码", required = true) @RequestParam String subjectCode,
                                             @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询科目明细账，科目：{}，期间：{}", loginStaff.getStaffid(), subjectCode, period);

            // 调用现有的 detail/subject 接口逻辑
            return getSubjectDetailLedger(request, response, subjectCode, period);
        } catch (Exception e) {
            log.error("查询科目明细账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询科目总账")
    @GetMapping("/query/subject")
    public String getSubjectLedgerQuery(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode,
                                       @ApiParam(value = "科目名称", required = false) @RequestParam(required = false) String subjectName,
                                       @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询科目总账，科目：{}，期间：{}", loginStaff.getStaffid(), subjectCode, period);

            // 构建查询参数
            GeneralLedgerQueryParam param = new GeneralLedgerQueryParam();
            param.setSubjectCode(subjectCode);
            param.setAccountingPeriod(period);
            param.setBookId(1L);
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());

            // 调用服务查询真实数据
            List<Map<String, Object>> ledgers = generalLedgerService.getSubjectLedgerQuery(param);

            return createSuccessResponse("查询成功", ledgers);
        } catch (Exception e) {
            log.error("查询科目总账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("多栏式总账查询")
    @GetMapping("/query/multi-column")
    public String getMultiColumnLedgerQuery(HttpServletRequest request,
                                            HttpServletResponse response,
                                            @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode,
                                            @ApiParam(value = "期间范围", required = false) @RequestParam(required = false) String periodRange,
                                            @ApiParam(value = "显示方式", required = false) @RequestParam(required = false) String displayMode) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询多栏式总账，科目：{}", loginStaff.getStaffid(), subjectCode);

            // 调用服务查询真实数据
            Map<String, Object> result = generalLedgerService.getMultiColumnLedgerQuery(
                    subjectCode, periodRange, 1L, loginStaff.getCurrentOrg().getOrgid().longValue());

            return createSuccessResponse("查询成功", result);
        } catch (Exception e) {
            log.error("查询多栏式总账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("辅助核算总账查询")
    @GetMapping("/query/auxiliary")
    public String getAuxiliaryLedgerQuery(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @ApiParam(value = "科目编码", required = false) @RequestParam(required = false) String subjectCode,
                                         @ApiParam(value = "辅助核算类型", required = false) @RequestParam(required = false) String auxiliaryType,
                                         @ApiParam(value = "辅助核算值", required = false) @RequestParam(required = false) String auxiliaryValue,
                                         @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询辅助核算总账", loginStaff.getStaffid());

            // 调用服务查询真实数据
            List<Map<String, Object>> ledgers = generalLedgerService.getAuxiliaryLedgerQuery(
                    subjectCode, auxiliaryType, auxiliaryValue, period, 1L, loginStaff.getCurrentOrg().getOrgid().longValue());

            return createSuccessResponse("查询成功", ledgers);
        } catch (Exception e) {
            log.error("查询辅助核算总账失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("总账汇总查询")
    @GetMapping("/query/summary")
    public String getLedgerSummaryQuery(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @ApiParam(value = "科目级次", required = false) @RequestParam(required = false) String subjectLevel,
                                        @ApiParam(value = "科目类型", required = false) @RequestParam(required = false) String subjectType,
                                        @ApiParam(value = "会计期间", required = false) @RequestParam(required = false) String period) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            log.info("用户 {} 查询总账汇总", loginStaff.getStaffid());

            // 构建查询参数
            GeneralLedgerQueryParam param = new GeneralLedgerQueryParam();
            param.setSubjectLevel(subjectLevel);
            param.setSubjectType(subjectType);
            param.setAccountingPeriod(period);
            param.setBookId(1L);
            param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());

            // 调用服务查询真实数据
            Map<String, Object> summary = generalLedgerService.getLedgerSummaryQuery(param);

            return createSuccessResponse("查询成功", summary);
        } catch (Exception e) {
            log.error("查询总账汇总失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出总账查询结果")
    @PostMapping("/query/export")
    public String exportLedgerQuery(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody GeneralLedgerQueryParam param) {
        try {
            TblStaffUtil loginStaff = validateUser(request, response);
            if (loginStaff == null) return null;

            // 设置账簿和租户ID
            if (param.getBookId() == null) {
                param.setBookId(1L);
            }
            if (param.getTenantId() == null) {
                param.setTenantId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            log.info("用户 {} 导出总账查询结果", loginStaff.getStaffid());

            // 调用服务导出数据
            Map<String, Object> result = generalLedgerService.exportLedgerQuery(param);

            return createSuccessResponse("导出任务创建成功", result);
        } catch (Exception e) {
            log.error("导出总账查询结果失败", e);
            return createErrorResponse("导出失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private TblStaffUtil validateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
            JsonBean json = new JsonBean();
            json.setCode(401);
            json.setMsg("用户已失效");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Type", "application/json;charset=UTF-8");
            response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
            return null;
        }
        return loginStaff;
    }

    private String createSuccessResponse(String message, Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg(message);
        json.setData(data);
        return JsonMapper.nonNullMapper().toJson(json);
    }

    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}