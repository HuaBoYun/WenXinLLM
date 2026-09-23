package com.global.treasurer.controller;

import com.global.treasurer.entity.TblPendingData;
import com.global.treasurer.service.PendingDataService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

/**
 * 待结算数据管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping({"/settlement/pending-data"})
@Api(tags = "待结算数据管理")
public class PendingDataController {
    @Resource
    private PendingDataService pendingDataService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询待结算数据
     */
    @GetMapping("/page")
    @ApiOperation("分页查询待结算数据")
    public String getPendingDataPage(@RequestParam Map<String, Object> params) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> result = pendingDataService.getPendingDataPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询待结算数据
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询待结算数据")
    public String getPendingDataById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblPendingData pendingData = pendingDataService.getPendingDataById(id);
            if (pendingData == null) {
                return new JsonBean(0, "待结算数据不存在", null).toJson();
            }
            return JsonBean.success(pendingData);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增待结算数据
     */
    @PostMapping("")
    @ApiOperation("新增待结算数据")
    public String addPendingData(@FlexibleRequestBody TblPendingData pendingData) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = pendingDataService.createPendingData(pendingData);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 修改待结算数据
     */
    @PutMapping("")
    @ApiOperation("修改待结算数据")
    public String updatePendingData(@FlexibleRequestBody TblPendingData pendingData) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = pendingDataService.updatePendingData(pendingData);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除待结算数据
     */
    @DeleteMapping("/{ids}")
    @ApiOperation("删除待结算数据")
    public String deletePendingData(@PathVariable String ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String[] idArray = ids.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) {
                idList.add(Long.parseLong(id));
            }

            int result = pendingDataService.deletePendingData(idList);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量更新结算状态
     */
    @PutMapping("/batch-status")
    @ApiOperation("批量更新结算状态")
    public String batchUpdatePendingStatus(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) params.get("ids");
            String status = (String) params.get("status");

            int result = pendingDataService.batchUpdateStatus(ids, status);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批待结算数据
     */
    @PutMapping("/approve")
    @ApiOperation("审批待结算数据")
    public String approvePendingData(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Long pendingId = Long.parseLong(params.get("pendingId").toString());
            String approvalStatus = (String) params.get("approvalStatus");
            Long approvalBy = loginStaff.getStaffid().longValue();
            String approvalOpinion = (String) params.get("approvalOpinion");

            int result = pendingDataService.approvePendingData(pendingId, approvalStatus, approvalBy, approvalOpinion);
            if (result > 0) {
                return JsonBean.success("审批成功");
            } else {
                return new JsonBean(0, "审批失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询高优先级待结算数据
     */
    @GetMapping("/high-priority")
    @ApiOperation("查询高优先级待结算数据")
    public String getHighPriorityPending(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果前端没有传递orgId,从用户信息中获取
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            List<TblPendingData> list = pendingDataService.getHighPriorityPending(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询逾期待结算数据
     */
    @GetMapping("/overdue")
    @ApiOperation("查询逾期待结算数据")
    public String getOverduePending(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TblPendingData> list = pendingDataService.getOverduePending(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 查询大额待结算数据
     */
    @GetMapping("/large-amount")
    @ApiOperation("查询大额待结算数据")
    public String getLargeAmountPending(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            List<TblPendingData> list = pendingDataService.getLargeAmountPending(params);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 统计待结算数据概要
     */
    @GetMapping("/summary")
    @ApiOperation("统计待结算数据概要")
    public String getPendingDataSummary(@RequestParam(required = false) Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            // 如果前端没有传递orgId,从用户信息中获取
            if (orgId == null) {
                orgId = loginStaff.getCurrentOrg().getOrgid().longValue();
            }

            Map<String, Object> summary = pendingDataService.getPendingDataSummary(orgId);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 风险评估
     */
    @GetMapping("/{id}/risk-assessment")
    @ApiOperation("风险评估")
    public String assessPendingDataRisk(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> assessment = pendingDataService.assessPendingDataRisk(id);
            return JsonBean.success(assessment);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "评估失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出待结算数据
     */
    @GetMapping("/export")
    @ApiOperation("导出待结算数据")
    public void exportPendingData(@RequestParam Map<String, Object> params,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            // 查询数据
            List<TblPendingData> dataList = pendingDataService.exportPendingData(params);

            // 创建Excel工作簿
            Workbook workbook = new SXSSFWorkbook(500);
            Sheet sheet = workbook.createSheet("待结算数据");

            // 创建标题行
            Row titleRow = sheet.createRow(0);
            String[] titles = {"业务编号", "业务类型", "结算金额", "币种", "对方账户", "对方名称",
                               "对方银行", "优先级", "预期结算日期", "结算状态", "审批状态", "创建时间"};
            for (int i = 0; i < titles.length; i++) {
                Cell cell = titleRow.createCell(i);
                cell.setCellValue(titles[i]);
            }

            // 填充数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < dataList.size(); i++) {
                TblPendingData data = dataList.get(i);
                Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(data.getBusinessNo() != null ? data.getBusinessNo() : "");
                row.createCell(1).setCellValue(data.getBusinessType() != null ? data.getBusinessType() : "");
                row.createCell(2).setCellValue(data.getSettlementAmount() != null ? data.getSettlementAmount().toString() : "0");
                row.createCell(3).setCellValue(data.getCurrencyCode() != null ? data.getCurrencyCode() : "");
                row.createCell(4).setCellValue(data.getCounterpartyAccount() != null ? data.getCounterpartyAccount() : "");
                row.createCell(5).setCellValue(data.getCounterpartyName() != null ? data.getCounterpartyName() : "");
                row.createCell(6).setCellValue(data.getCounterpartyBank() != null ? data.getCounterpartyBank() : "");
                row.createCell(7).setCellValue(data.getPriority() != null ? data.getPriority() : "");
                row.createCell(8).setCellValue(data.getExpectedSettlementDate() != null ? sdf.format(data.getExpectedSettlementDate()) : "");
                row.createCell(9).setCellValue(data.getSettlementStatus() != null ? data.getSettlementStatus() : "");
                row.createCell(10).setCellValue(data.getApprovalStatus() != null ? data.getApprovalStatus() : "");
                row.createCell(11).setCellValue(data.getCreatedTime() != null ? sdf.format(data.getCreatedTime()) : "");
            }

            // 设置响应头
            String fileName = "待结算数据_" + System.currentTimeMillis() + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));

            // 输出文件
            workbook.write(response.getOutputStream());
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
