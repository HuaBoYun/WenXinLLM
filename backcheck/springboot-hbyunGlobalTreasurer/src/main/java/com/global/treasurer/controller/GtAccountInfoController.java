package com.global.treasurer.controller;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblGtAccountInfo;
import com.global.treasurer.service.TblGtAccountInfoService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 全球司库-账户信息管理Controller
 *
 * @author AI Developer
 * @since 2026-01-15
 */
@RestController
@RequestMapping("/financial/account")
@Api(tags = "全球司库-账户信息管理")
public class GtAccountInfoController {
    private static final Logger log = LoggerFactory.getLogger(GtAccountInfoController.class);

    @Resource
    private TblGtAccountInfoService tblGtAccountInfoService;

    @Resource
    private UserProvider userProvider;

    /**
     * 验证用户权限
     */
    private String validateUser(HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(json.toJson());
                return null;
            }
            return "OK";
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return null;
        }
    }

    /**
     * 分页查询账户列表(POST方式)
     */
    @PostMapping("/page")
    @ApiOperation("分页查询账户列表")
    public String getAccountInfoPage(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 分页参数
            int pageNum = 1;
            int pageSize = 10;
            if (params.get("pageNum") != null) {
                pageNum = Integer.parseInt(params.get("pageNum").toString());
            }
            if (params.get("pageSize") != null) {
                pageSize = Integer.parseInt(params.get("pageSize").toString());
            }

            // 查询参数（优先使用前端传入的 orgId，否则取登录用户的机构）
            BigDecimal orgId;
            if (params.get("orgId") != null) {
                orgId = new BigDecimal(params.get("orgId").toString());
            } else {
                orgId = loginStaff.getCurrentOrg().getOrgid();
            }
            String accountNumber = (String) params.get("accountNumber");
            String accountName = (String) params.get("accountName");
            String accountType = (String) params.get("accountType");
            String bankCode = (String) params.get("bankCode");
            String currencyCode = (String) params.get("currencyCode");
            String accountStatus = (String) params.get("accountStatus");

            // 查询数据
            Page<TblGtAccountInfo> page = new Page<>(pageNum, pageSize);
            IPage<TblGtAccountInfo> pageResult = tblGtAccountInfoService.selectAccountPage(
                    page, orgId, accountNumber, accountName, accountType,
                    bankCode, currencyCode, accountStatus
            );

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("records", pageResult.getRecords());
            result.put("total", pageResult.getTotal());
            result.put("size", pageResult.getSize());
            result.put("current", pageResult.getCurrent());
            result.put("pages", pageResult.getPages());

            return JsonBean.success(result);

        } catch (Exception e) {
            log.error("查询账户列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询账户列表（不分页，GET方式）
     */
    @GetMapping("/list")
    @ApiOperation("查询账户列表（不分页）")
    public String getAccountList(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) String bankCode,
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) String accountStatus,
            HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            BigDecimal orgId = loginStaff.getCurrentOrg().getOrgid();
            List<TblGtAccountInfo> list = tblGtAccountInfoService.selectAccountList(
                    orgId, accountNumber, accountName, accountType, bankCode, currencyCode, accountStatus);
            return JsonBean.success(list);
        } catch (Exception e) {
            log.error("查询账户列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取账户统计数据
     */
    @GetMapping("/stats")
    @ApiOperation("获取账户统计数据")
    public String getAccountStats(HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> stats = tblGtAccountInfoService.getAccountStats(loginStaff.getCurrentOrg().getOrgid());
            return JsonBean.success(stats);

        } catch (Exception e) {
            log.error("获取账户统计失败", e);
            return new JsonBean(0, "获取统计失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取余额概览
     */
    @GetMapping("/balance-overview")
    @ApiOperation("获取余额概览")
    public String getBalanceOverview(HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> overview = tblGtAccountInfoService.getBalanceOverview(loginStaff.getCurrentOrg().getOrgid());
            return JsonBean.success(overview);

        } catch (Exception e) {
            log.error("获取余额概览失败", e);
            return new JsonBean(0, "获取余额概览失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取账户状态统计
     */
    @GetMapping("/status-stats")
    @ApiOperation("获取账户状态统计")
    public String getStatusStats(HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> stats = tblGtAccountInfoService.getStatusStats(loginStaff.getCurrentOrg().getOrgid());
            return JsonBean.success(stats);

        } catch (Exception e) {
            log.error("获取状态统计失败", e);
            return new JsonBean(0, "获取状态统计失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取银行分布
     */
    @GetMapping("/bank-distribution")
    @ApiOperation("获取银行分布")
    public String getBankDistribution(HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<Map<String, Object>> distribution = tblGtAccountInfoService.getBankDistribution(loginStaff.getCurrentOrg().getOrgid());
            return JsonBean.success(distribution);

        } catch (Exception e) {
            log.error("获取银行分布失败", e);
            return new JsonBean(0, "获取银行分布失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增账户
     */
    @PostMapping
    @ApiOperation("新增账户")
    public String createAccount(@FlexibleRequestBody TblGtAccountInfo accountInfo, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 设置机构ID和创建人
            accountInfo.setOrgId(loginStaff.getCurrentOrg().getOrgid());
            accountInfo.setCreateUser(loginStaff.getStaffid());

            boolean success = tblGtAccountInfoService.createAccount(accountInfo);
            if (success) {
                return JsonBean.success(accountInfo);
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("新增账户失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改账户
     */
    @PutMapping
    @ApiOperation("修改账户")
    public String updateAccount(@FlexibleRequestBody TblGtAccountInfo accountInfo, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            accountInfo.setUpdateUser(loginStaff.getStaffid());
            boolean success = tblGtAccountInfoService.updateAccountInfo(accountInfo);

            if (success) {
                return JsonBean.success("修改成功");
            } else {
                return new JsonBean(0, "修改失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("修改账户失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除账户
     */
    @DeleteMapping("/{accountId}")
    @ApiOperation("删除账户")
    public String deleteAccount(@PathVariable BigDecimal accountId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<BigDecimal> accountIds = java.util.Arrays.asList(accountId);
            boolean success = tblGtAccountInfoService.batchDeleteAccounts(accountIds, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("删除账户失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除账户
     */
    @DeleteMapping("/batch")
    @ApiOperation("批量删除账户")
    public String batchDeleteAccounts(@RequestParam(value = "accountIds", required = false) List<BigDecimal> accountIds, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountInfoService.batchDeleteAccounts(accountIds, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("批量删除成功");
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("批量删除账户失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 激活账户
     */
    @PutMapping("/{accountId}/activate")
    @ApiOperation("激活账户")
    public String activateAccount(@PathVariable BigDecimal accountId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountInfoService.activateAccount(accountId, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("激活成功");
            } else {
                return new JsonBean(0, "激活失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("激活账户失败", e);
            return new JsonBean(0, "激活失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 冻结账户
     */
    @PutMapping("/{accountId}/freeze")
    @ApiOperation("冻结账户")
    public String freezeAccount(@PathVariable BigDecimal accountId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountInfoService.freezeAccount(accountId, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("冻结成功");
            } else {
                return new JsonBean(0, "冻结失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("冻结账户失败", e);
            return new JsonBean(0, "冻结失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 解冻账户
     */
    @PutMapping("/{accountId}/unfreeze")
    @ApiOperation("解冻账户")
    public String unfreezeAccount(@PathVariable BigDecimal accountId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountInfoService.unfreezeAccount(accountId, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("解冻成功");
            } else {
                return new JsonBean(0, "解冻失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("解冻账户失败", e);
            return new JsonBean(0, "解冻失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 关闭账户
     */
    @PutMapping("/{accountId}/close")
    @ApiOperation("关闭账户")
    public String closeAccount(@PathVariable BigDecimal accountId, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            boolean success = tblGtAccountInfoService.closeAccount(accountId, loginStaff.getStaffid());

            if (success) {
                return JsonBean.success("关闭成功");
            } else {
                return new JsonBean(0, "关闭失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("关闭账户失败", e);
            return new JsonBean(0, "关闭失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 设置默认账户
     */
    @PutMapping("/default")
    @ApiOperation("设置默认账户")
    public String setDefaultAccount(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            BigDecimal accountId = new BigDecimal(params.get("accountId").toString());
            String currencyCode = (String) params.get("currencyCode");

            boolean success = tblGtAccountInfoService.setDefaultAccount(
                    accountId, currencyCode, loginStaff.getCurrentOrg().getOrgid(), loginStaff.getStaffid()
            );

            if (success) {
                return JsonBean.success("设置成功");
            } else {
                return new JsonBean(0, "设置失败", null).toJson();
            }

        } catch (Exception e) {
            log.error("设置默认账户失败", e);
            return new JsonBean(0, "设置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 检查账户号码是否唯一
     */
    @GetMapping("/check-unique")
    @ApiOperation("检查账户号码是否唯一")
    public String checkAccountNumberUnique(@RequestParam String accountNumber,
                                           @RequestParam(required = false) BigDecimal orgId,
                                           @RequestParam(required = false) BigDecimal excludeId,
                                           HttpServletResponse response) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果没有传入 orgId，使用当前用户的机构 ID
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid();
            }

            boolean isUnique = tblGtAccountInfoService.checkAccountNumberUnique(accountNumber, orgId, excludeId);

            if (isUnique) {
                return JsonBean.success(true);
            } else {
                return JsonBean.success(false);
            }

        } catch (Exception e) {
            log.error("检查账户号码唯一性失败", e);
            return new JsonBean(0, "检查失败：" + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出账户信息")
    public void exportAccountInfo(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) String bankCode,
            @RequestParam(required = false) String currencyCode,
            @RequestParam(required = false) String accountStatus,
            HttpServletResponse response) {
        try {
            TblStaffUtil userInfo = userProvider.get();
            BigDecimal orgId = userInfo != null ? userInfo.getOrgid() : null;

            List<TblGtAccountInfo> list = tblGtAccountInfoService.selectAccountList(
                    orgId, accountNumber, accountName, accountType, bankCode, currencyCode, accountStatus);

            String[] headers = {"账户名称", "账户号码", "账户类型", "银行编码", "币种", "账户状态", "开户日期", "余额", "可用余额", "冻结余额", "备注"};
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("账户信息");
                // 表头样式
                CellStyle headerStyle = workbook.createCellStyle();
                headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerStyle.setFont(headerFont);

                Row headerRow = sheet.createRow(0);
                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    cell.setCellStyle(headerStyle);
                    sheet.setColumnWidth(i, 4000);
                }

                // 数据行
                for (int i = 0; i < list.size(); i++) {
                    TblGtAccountInfo a = list.get(i);
                    Row row = sheet.createRow(i + 1);
                    row.createCell(0).setCellValue(a.getAccountName() != null ? a.getAccountName() : "");
                    row.createCell(1).setCellValue(a.getAccountNumber() != null ? a.getAccountNumber() : "");
                    row.createCell(2).setCellValue(a.getAccountType() != null ? a.getAccountType() : "");
                    row.createCell(3).setCellValue(a.getBankCode() != null ? a.getBankCode() : "");
                    row.createCell(4).setCellValue(a.getCurrencyCode() != null ? a.getCurrencyCode() : "");
                    row.createCell(5).setCellValue(a.getAccountStatus() != null ? a.getAccountStatus() : "");
                    row.createCell(6).setCellValue(a.getOpenDate() != null ? a.getOpenDate().format(fmt) : "");
                    row.createCell(7).setCellValue(a.getBalance() != null ? a.getBalance().toPlainString() : "0");
                    row.createCell(8).setCellValue(a.getAvailableBalance() != null ? a.getAvailableBalance().toPlainString() : "0");
                    row.createCell(9).setCellValue(a.getFrozenBalance() != null ? a.getFrozenBalance().toPlainString() : "0");
                    row.createCell(10).setCellValue(a.getRemark() != null ? a.getRemark() : "");
                }

                String fileName = URLEncoder.encode("账户信息.xlsx", "UTF-8").replaceAll("\\+", "%20");
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + fileName);
                try (OutputStream os = response.getOutputStream()) {
                    workbook.write(os);
                }
            }
        } catch (Exception e) {
            log.error("导出账户信息失败", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
