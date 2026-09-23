package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BankLoanDTO;
import com.global.treasurer.dto.BankLoanQueryDTO;
import com.global.treasurer.dto.LoanContractDTO;
import com.global.treasurer.dto.RepaymentPlanDTO;
import com.global.treasurer.dto.LoanMonitoringDTO;
import com.global.treasurer.entity.TblBankLoan;
import com.global.treasurer.entity.TblLoanContract;
import com.global.treasurer.entity.TblRepaymentPlan;
import com.global.treasurer.entity.TblLoanMonitoring;
import com.global.treasurer.service.BankLoanService;
import com.global.treasurer.service.LoanContractService;
import com.global.treasurer.service.RepaymentPlanService;
import com.global.treasurer.service.LoanMonitoringService;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 银行贷款Controller
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
@Controller
@RequestMapping({"/rzgl/bank-loan",
 "/financial/rzgl/bank-loan",
  "/centralaudit/rzgl/bank-loan"
  })
@Api(tags = "银行贷款管理")
public class BankLoanController {
    private static final Logger log = LoggerFactory.getLogger(BankLoanController.class);

    @Resource
    private BankLoanService bankLoanService;

    @Resource
    private LoanContractService loanContractService;

    @Resource
    private RepaymentPlanService repaymentPlanService;

    @Resource
    private LoanMonitoringService loanMonitoringService;

    @Resource
    private UserProvider userProvider;

    @Resource
    private com.global.treasurer.mapper.BankLoanMapper bankLoanMapper;

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询银行贷款列表")
    public String getBankLoanList(@FlexibleRequestBody BankLoanQueryDTO queryDTO,
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

            PageInfo<TblBankLoan> pageInfo = bankLoanService.getLoanList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询银行贷款列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("根据ID获取银行贷款详情")
    public String getBankLoanById(@PathVariable Long id,
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

            TblBankLoan loan = bankLoanService.getLoanById(id);
            return new JsonBean(1, "成功", loan).toJson();
        } catch (Exception e) {
            log.error("获取银行贷款详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/create")
    @ResponseBody
    @ApiOperation("创建银行贷款")
    public String createBankLoan(@FlexibleRequestBody BankLoanDTO dto,
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

            TblBankLoan loan = bankLoanService.saveLoan(dto);
            return new JsonBean(1, "创建成功", loan).toJson();
        } catch (Exception e) {
            log.error("创建银行贷款失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新银行贷款")
    public String updateBankLoan(@FlexibleRequestBody BankLoanDTO dto,
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

            if (dto.getLoanId() == null) {
                return new JsonBean(0, "贷款ID不能为空", null).toJson();
            }

            TblBankLoan loan = bankLoanService.saveLoan(dto);
            return new JsonBean(1, "更新成功", loan).toJson();
        } catch (Exception e) {
            log.error("更新银行贷款失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除银行贷款")
    public String deleteBankLoan(@PathVariable Long id,
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

            bankLoanService.deleteLoan(id);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除银行贷款失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/submit")
    @ResponseBody
    @ApiOperation("提交银行贷款审批")
    public String submitBankLoan(@RequestParam Long loanId,
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

            bankLoanService.submitForApproval(loanId);
            return new JsonBean(1, "提交成功", null).toJson();
        } catch (Exception e) {
            log.error("提交银行贷款审批失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/approve")
    @ResponseBody
    @ApiOperation("审批银行贷款")
    public String approveBankLoan(@RequestParam Long loanId,
                                  @RequestParam(required = false) String comments,
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

            bankLoanService.approve(loanId, comments);
            return new JsonBean(1, "审批成功", null).toJson();
        } catch (Exception e) {
            log.error("审批银行贷款失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/drawdown")
    @ResponseBody
    @ApiOperation("银行贷款放款")
    public String confirmDrawdown(@RequestParam Long loanId,
                                  @RequestParam(required = false) BigDecimal drawdownAmount,
                                  @RequestParam(required = false) String contractNo,
                                  @RequestParam(required = false) String valueDate,
                                  @RequestParam(required = false) String maturityDate,
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
            if (drawdownAmount != null) {
                params.put("drawdownAmount", drawdownAmount);
            }
            if (contractNo != null && !contractNo.isEmpty()) {
                params.put("contractNo", contractNo);
            }
            if (valueDate != null && !valueDate.isEmpty()) {
                params.put("valueDate", valueDate);
            }
            if (maturityDate != null && !maturityDate.isEmpty()) {
                params.put("maturityDate", maturityDate);
            }

            bankLoanService.confirmDrawdown(loanId, params);
            return new JsonBean(1, "放款成功", null).toJson();
        } catch (Exception e) {
            log.error("银行贷款放款失败", e);
            return new JsonBean(0, "放款失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/banks")
    @ResponseBody
    @ApiOperation("获取银行列表")
    public String getBankList(@RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 返回银行列表(示例数据,实际应从字典表或配置表获取)
            List<Map<String, String>> banks = new java.util.ArrayList<>();

            Map<String, String> bank1 = new HashMap<>();
            bank1.put("code", "ICBC");
            bank1.put("name", "中国工商银行");
            banks.add(bank1);

            Map<String, String> bank2 = new HashMap<>();
            bank2.put("code", "ABC");
            bank2.put("name", "中国农业银行");
            banks.add(bank2);

            Map<String, String> bank3 = new HashMap<>();
            bank3.put("code", "BOC");
            bank3.put("name", "中国银行");
            banks.add(bank3);

            Map<String, String> bank4 = new HashMap<>();
            bank4.put("code", "CCB");
            bank4.put("name", "中国建设银行");
            banks.add(bank4);

            Map<String, String> bank5 = new HashMap<>();
            bank5.put("code", "BCM");
            bank5.put("name", "交通银行");
            banks.add(bank5);

            Map<String, String> bank6 = new HashMap<>();
            bank6.put("code", "CMB");
            bank6.put("name", "招商银行");
            banks.add(bank6);

            Map<String, String> bank7 = new HashMap<>();
            bank7.put("code", "CITIC");
            bank7.put("name", "中信银行");
            banks.add(bank7);

            Map<String, String> bank8 = new HashMap<>();
            bank8.put("code", "CEB");
            bank8.put("name", "中国光大银行");
            banks.add(bank8);

            Map<String, String> bank9 = new HashMap<>();
            bank9.put("code", "HXB");
            bank9.put("name", "华夏银行");
            banks.add(bank9);

            Map<String, String> bank10 = new HashMap<>();
            bank10.put("code", "CIB");
            bank10.put("name", "兴业银行");
            banks.add(bank10);

            Map<String, String> bank11 = new HashMap<>();
            bank11.put("code", "SPDB");
            bank11.put("name", "浦发银行");
            banks.add(bank11);

            Map<String, String> bank12 = new HashMap<>();
            bank12.put("code", "CMBC");
            bank12.put("name", "中国民生银行");
            banks.add(bank12);

            Map<String, String> bank13 = new HashMap<>();
            bank13.put("code", "GDB");
            bank13.put("name", "广发银行");
            banks.add(bank13);

            Map<String, String> bank14 = new HashMap<>();
            bank14.put("code", "PINGAN");
            bank14.put("name", "平安银行");
            banks.add(bank14);

            Map<String, String> bank15 = new HashMap<>();
            bank15.put("code", "BOB");
            bank15.put("name", "北京银行");
            banks.add(bank15);

            Map<String, String> bank16 = new HashMap<>();
            bank16.put("code", "SHB");
            bank16.put("name", "上海银行");
            banks.add(bank16);
            return new JsonBean(1, "成功", banks).toJson();
        } catch (Exception e) {
            log.error("获取银行列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/guaranteeTypes")
    @ResponseBody
    @ApiOperation("获取担保方式列表")
    public String getGuaranteeTypes(@RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 返回担保方式列表(示例数据,实际应从字典表获取)
            List<Map<String, String>> guaranteeTypes = new java.util.ArrayList<>();

            Map<String, String> guaranteeType1 = new HashMap<>();
            guaranteeType1.put("code", "CREDIT");
            guaranteeType1.put("name", "信用贷款");
            guaranteeTypes.add(guaranteeType1);

            Map<String, String> guaranteeType2 = new HashMap<>();
            guaranteeType2.put("code", "MORTGAGE");
            guaranteeType2.put("name", "抵押贷款");
            guaranteeTypes.add(guaranteeType2);

            Map<String, String> guaranteeType3 = new HashMap<>();
            guaranteeType3.put("code", "PLEDGE");
            guaranteeType3.put("name", "质押贷款");
            guaranteeTypes.add(guaranteeType3);

            Map<String, String> guaranteeType4 = new HashMap<>();
            guaranteeType4.put("code", "GUARANTEE");
            guaranteeType4.put("name", "保证贷款");
            guaranteeTypes.add(guaranteeType4);

            Map<String, String> guaranteeType5 = new HashMap<>();
            guaranteeType5.put("code", "Mixed");
            guaranteeType5.put("name", "混合担保");
            guaranteeTypes.add(guaranteeType5);
            return new JsonBean(1, "成功", guaranteeTypes).toJson();
        } catch (Exception e) {
            log.error("获取担保方式列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取银行贷款统计信息")
    public String getLoanStatistics(@RequestParam(required = false) Long companyId,
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

            Map<String, Object> statistics = bankLoanService.getLoanSummary(companyId);
            // 兼容达梦数据库返回大写字段名，统一转换为前端期望的小写字段名
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", getMapValue(statistics, "totalCount", "TOTALCOUNT", 0));
            result.put("totalAmount", getMapValue(statistics, "totalAmount", "TOTALAMOUNT", 0));
            result.put("totalDrawdownAmount", getMapValue(statistics, "totalDrawdownAmount", "TOTALDRAWDOWNAMOUNT", 0));
            result.put("totalOutstandingAmount", getMapValue(statistics, "totalOutstandingAmount", "TOTALOUTSTANDINGAMOUNT", 0));
            result.put("totalInterestExpense", getMapValue(statistics, "totalInterestExpense", "TOTALINTERESTEXPENSE", 0));
            result.put("activeAmount", getMapValue(statistics, "activeAmount", "ACTIVEAMOUNT", 0));
            result.put("pendingAmount", getMapValue(statistics, "pendingAmount", "PENDINGAMOUNT", 0));
            result.put("averageRate", getMapValue(statistics, "averageRate", "AVERAGERATE", 0));
            result.put("approvalRate", getMapValue(statistics, "approvalRate", "APPROVALRATE", 0));
            return new JsonBean(1, "成功", result).toJson();
        } catch (Exception e) {
            log.error("获取银行贷款统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 兼容达梦数据库大小写字段名的辅助方法
     */
    private Object getMapValue(Map<String, Object> map, String lowerKey, String upperKey, Object defaultValue) {
        if (map == null) return defaultValue;
        Object value = map.get(lowerKey);
        if (value == null) {
            value = map.get(upperKey);
        }
        return value != null ? value : defaultValue;
    }

    @GetMapping("/nearExpiry")
    @ResponseBody
    @ApiOperation("获取即将到期的贷款")
    public String getNearExpiryLoans(@RequestParam(defaultValue = "30") Integer days,
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

            List<TblBankLoan> loans = bankLoanService.getExpiringLoans(days);
            return new JsonBean(1, "成功", loans).toJson();
        } catch (Exception e) {
            log.error("获取即将到期贷款失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chart/type-distribution")
    @ResponseBody
    @ApiOperation("获取贷款类型分布图表数据")
    public String getLoanTypeDistribution(@RequestParam(required = false) Long companyId,
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

            List<Map<String, Object>> distribution = bankLoanMapper.selectLoanTypeDistribution(companyId);
            if (distribution == null) {
                distribution = new ArrayList<>();
            }
            // 转换贷款类型名称
            Map<String, String> typeNameMap = new HashMap<>();
            typeNameMap.put("CREDIT_LINE", "信用额度");
            typeNameMap.put("TERM_LOAN", "定期贷款");
            typeNameMap.put("REVOLVING_LOAN", "循环贷款");
            typeNameMap.put("MORTGAGE", "抵押贷款");
            typeNameMap.put("GUARANTEE", "担保贷款");

            for (Map<String, Object> item : distribution) {
                // 兼容达梦数据库返回大写字段名
                String loanType = (String) item.getOrDefault("loanType", item.get("LOANTYPE"));
                if (loanType != null && typeNameMap.containsKey(loanType)) {
                    item.put("name", typeNameMap.get(loanType));
                } else {
                    item.put("name", loanType);
                }
                // 兼容达梦数据库返回大写字段名
                Object count = item.getOrDefault("count", item.get("COUNT"));
                item.put("value", count != null ? Integer.parseInt(count.toString()) : 0);
            }
            return new JsonBean(1, "成功", distribution).toJson();
        } catch (Exception e) {
            log.error("获取贷款类型分布失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/chart/trend")
    @ResponseBody
    @ApiOperation("获取贷款申请趋势图表数据")
    public String getLoanTrend(@RequestParam(required = false, defaultValue = "1Y") String period,
                               @RequestParam(required = false) Long companyId,
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

            // 根据period计算月数
            int months = 12;
            if ("6M".equals(period)) {
                months = 6;
            } else if ("2Y".equals(period)) {
                months = 24;
            }

            List<Map<String, Object>> trend = bankLoanMapper.selectLoanTrend(companyId, months);
            if (trend == null) {
                trend = new ArrayList<>();
            }
            // 兼容达梦数据库返回大写字段名
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> item : trend) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", getMapValue(item, "month", "MONTH", ""));
                row.put("applicationCount", getMapValue(item, "applicationCount", "APPLICATIONCOUNT", 0));
                row.put("applicationAmount", getMapValue(item, "applicationAmount", "APPLICATIONAMOUNT", 0));
                result.add(row);
            }
            return new JsonBean(1, "成功", result).toJson();
        } catch (Exception e) {
            log.error("获取贷款申请趋势失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    // ==================== 合同管理接口 ====================

    @PostMapping("/contract/list")
    @ResponseBody
    @ApiOperation("查询贷款合同列表")
    public String getContractList(@FlexibleRequestBody LoanContractDTO dto,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            PageInfo<TblLoanContract> pageInfo = loanContractService.getContractList(dto);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询合同列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/contract/save")
    @ResponseBody
    @ApiOperation("保存贷款合同")
    public String saveContract(@FlexibleRequestBody LoanContractDTO dto,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblLoanContract contract = loanContractService.saveContract(dto);
            return new JsonBean(1, "保存成功", contract).toJson();
        } catch (Exception e) {
            log.error("保存合同失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/contract/delete/{id}")
    @ResponseBody
    @ApiOperation("删除贷款合同")
    public String deleteContract(@PathVariable String id,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            loanContractService.deleteContract(id);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除合同失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/contract/byLoan/{loanId}")
    @ResponseBody
    @ApiOperation("根据贷款ID查询合同列表")
    public String getContractsByLoanId(@PathVariable String loanId,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblLoanContract> list = loanContractService.getContractsByLoanId(loanId);
            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("查询合同列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/contract/status/{contractId}")
    @ResponseBody
    @ApiOperation("更新合同状态")
    public String updateContractStatus(@PathVariable String contractId,
                                       @RequestParam String status,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            loanContractService.updateContractStatus(contractId, status);
            return new JsonBean(1, "状态更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新合同状态失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    // ==================== 还款计划接口 ====================

    @PostMapping("/repayment/list")
    @ResponseBody
    @ApiOperation("查询还款计划列表")
    public String getRepaymentList(@FlexibleRequestBody RepaymentPlanDTO dto,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            PageInfo<TblRepaymentPlan> pageInfo = repaymentPlanService.getPlanList(dto);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询还款计划失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/repayment/generate")
    @ResponseBody
    @ApiOperation("自动生成还款计划")
    public String generateRepaymentPlans(@FlexibleRequestBody RepaymentPlanDTO dto,
                                         @RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblRepaymentPlan> plans = repaymentPlanService.generateRepaymentPlans(dto);
            return new JsonBean(1, "生成成功", plans).toJson();
        } catch (Exception e) {
            log.error("生成还款计划失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/repayment/pay")
    @ResponseBody
    @ApiOperation("执行还款")
    public String executeRepayment(@FlexibleRequestBody RepaymentPlanDTO dto,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            repaymentPlanService.executeRepayment(dto.getPlanId(), dto.getPayAmount());
            return new JsonBean(1, "还款成功", null).toJson();
        } catch (Exception e) {
            log.error("执行还款失败", e);
            return new JsonBean(0, "还款失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/repayment/byLoan/{loanId}")
    @ResponseBody
    @ApiOperation("根据贷款ID查询还款计划列表")
    public String getRepaymentPlansByLoanId(@PathVariable String loanId,
                                            @RequestHeader(value = "token", required = false) String token,
                                            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblRepaymentPlan> list = repaymentPlanService.getPlansByLoanId(loanId);
            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("查询还款计划失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/repayment/execute/{planId}")
    @ResponseBody
    @ApiOperation("执行还款（按计划ID）")
    public String executeRepaymentByPlanId(@PathVariable Long planId,
                                           @RequestParam BigDecimal amount,
                                           @RequestHeader(value = "token", required = false) String token,
                                           HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            repaymentPlanService.executeRepayment(planId, amount);
            return new JsonBean(1, "还款成功", null).toJson();
        } catch (Exception e) {
            log.error("执行还款失败", e);
            return new JsonBean(0, "还款失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/repayment/delete/{planId}")
    @ResponseBody
    @ApiOperation("删除还款计划")
    public String deleteRepaymentPlan(@PathVariable Long planId,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            repaymentPlanService.deletePlan(planId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除还款计划失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    // ==================== 贷款监控接口 ====================

    @PostMapping("/monitoring/list")
    @ResponseBody
    @ApiOperation("查询贷款监控预警列表")
    public String getMonitoringList(@FlexibleRequestBody LoanMonitoringDTO dto,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            PageInfo<TblLoanMonitoring> pageInfo = loanMonitoringService.getMonitoringList(dto);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询监控预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/monitoring/add")
    @ResponseBody
    @ApiOperation("新增贷款监控预警")
    public String addMonitoring(@FlexibleRequestBody LoanMonitoringDTO dto,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblLoanMonitoring monitoring = loanMonitoringService.addAlert(dto);
            return new JsonBean(1, "新增成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("新增监控预警失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/monitoring/update")
    @ResponseBody
    @ApiOperation("修改贷款监控预警")
    public String updateMonitoring(@FlexibleRequestBody LoanMonitoringDTO dto,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblLoanMonitoring monitoring = loanMonitoringService.updateAlert(dto);
            return new JsonBean(1, "修改成功", monitoring).toJson();
        } catch (Exception e) {
            log.error("修改监控预警失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/monitoring/handle/{monitoringId}")
    @ResponseBody
    @ApiOperation("处理预警")
    public String handleAlert(@PathVariable Long monitoringId,
                              @RequestParam(required = false) Long handlerId,
                              @RequestParam String handleOpinion,
                              @RequestParam String status,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            String handlerName = loginStaff.getRealname();
            loanMonitoringService.handleAlert(monitoringId, handlerId, handlerName, handleOpinion, status);
            return new JsonBean(1, "处理成功", null).toJson();
        } catch (Exception e) {
            log.error("处理预警失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/monitoring/byLoan/{loanId}")
    @ResponseBody
    @ApiOperation("根据贷款ID查询监控预警列表")
    public String getMonitoringByLoanId(@PathVariable String loanId,
                                        @RequestHeader(value = "token", required = false) String token,
                                        HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblLoanMonitoring> list = loanMonitoringService.getMonitoringByLoanId(loanId);
            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("查询监控预警失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/monitoring/delete/{monitoringId}")
    @ResponseBody
    @ApiOperation("删除监控预警")
    public String deleteMonitoring(@PathVariable Long monitoringId,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            loanMonitoringService.deleteMonitoring(monitoringId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除监控预警失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/monitoring/pending-count/{loanId}")
    @ResponseBody
    @ApiOperation("获取待处理预警数量")
    public String getPendingAlertCount(@PathVariable Long loanId,
                                       @RequestHeader(value = "token", required = false) String token,
                                       HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int count = loanMonitoringService.countPendingAlerts(loanId);
            return new JsonBean(1, "成功", count).toJson();
        } catch (Exception e) {
            log.error("获取待处理预警数量失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
