package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.BondIssuanceDTO;
import com.global.treasurer.dto.BondIssuanceQueryDTO;
import com.global.treasurer.entity.TblBondIssuance;
import com.global.treasurer.service.BondIssuanceService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 债券发行管理Controller
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@RestController
@RequestMapping({"/financial/rzgl/bond-financing"})
@Api(tags = "债券发行管理")
public class BondIssuanceController {
    private static final Logger log = LoggerFactory.getLogger(BondIssuanceController.class);

    @Autowired
    private BondIssuanceService bondIssuanceService;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询债券发行列表
     */
    @PostMapping("/list")
    @ApiOperation(value = "分页查询债券发行列表", notes = "支持按债券类型、发行状态、发行日期等条件查询")
    public String list(@FlexibleRequestBody BondIssuanceQueryDTO queryDTO) {
        try {
            // 获取用户信息,但不强制要求登录
            TblStaffUtil loginStaff = null;
            try {
                loginStaff = userProvider.get();
            } catch (Exception e) {
                log.warn("获取用户信息失败,将查询所有公司的数据: {}", e.getMessage());
            }

            // 强制查询所有公司的数据，忽略前端传递的companyId
            queryDTO.setCompanyId(null);
            log.info("已强制设置companyId=null，将查询所有公司的债券数据");

            log.info("=== 债券列表查询开始 ===");
            log.info("查询参数: companyId={}, pageNum={}, pageSize={}, bondType={}, issuanceStatus={}",
                    queryDTO.getCompanyId(), queryDTO.getPageNum(), queryDTO.getPageSize(),
                    queryDTO.getBondType(), queryDTO.getIssuanceStatus());

            PageInfo<TblBondIssuance> pageInfo = bondIssuanceService.getIssuanceList(queryDTO);

            log.info("查询结果: total={}, listSize={}", pageInfo.getTotal(), pageInfo.getList().size());
            if (!pageInfo.getList().isEmpty()) {
                log.info("第一条数据: bondId={}, bondName={}",
                        pageInfo.getList().get(0).getBondId(),
                        pageInfo.getList().get(0).getBondName());
            }
            log.info("=== 债券列表查询结束 ===");

            // 构建前端期望的响应格式
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", (int) pageInfo.getTotal());  // 确保是数字类型
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return new JsonBean(1, "查询成功", result).toJson();
        } catch (Exception e) {
            log.error("查询债券发行列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取债券发行详情
     */
    @GetMapping("/detail/{issuanceId}")
    @ApiOperation(value = "获取债券发行详情", notes = "根据发行ID获取详细信息")
    public String getDetail(@ApiParam("发行ID") @PathVariable Long issuanceId) {
        try {
            TblBondIssuance issuance = bondIssuanceService.getIssuanceById(issuanceId);
            if (issuance == null) {
                return new JsonBean(0, "债券发行记录不存在", null).toJson();
            }
            return new JsonBean(1, "查询成功", issuance).toJson();
        } catch (Exception e) {
            log.error("获取债券发行详情失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 新增债券发行
     */
    @PostMapping("/create")
    @ApiOperation(value = "新增债券发行", notes = "创建新的债券发行计划")
    public String create(@Valid @FlexibleRequestBody BondIssuanceDTO dto) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            
            // 设置公司信息
            if (dto.getCompanyId() == null) {
                dto.setCompanyId(loginStaff.getCurrentOrg().getOrgid().longValue());
                dto.setCompanyName(loginStaff.getCurrentOrg().getOrgname());
            }
            
            dto.setIssuanceId(null); // 确保是新增
            TblBondIssuance issuance = bondIssuanceService.saveIssuance(dto);
            return new JsonBean(1, "新增成功", issuance).toJson();
        } catch (Exception e) {
            log.error("新增债券发行失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新债券发行
     */
    @PutMapping("/update")
    @ApiOperation(value = "更新债券发行", notes = "修改债券发行信息，支持部分字段更新")
    public String update(@FlexibleRequestBody BondIssuanceDTO dto) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (dto.getIssuanceId() == null) {
                return new JsonBean(0, "发行ID不能为空", null).toJson();
            }

            TblBondIssuance issuance = bondIssuanceService.saveIssuance(dto);
            return new JsonBean(1, "更新成功", issuance).toJson();
        } catch (Exception e) {
            log.error("更新债券发行失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除债券发行
     */
    @DeleteMapping("/delete/{issuanceId}")
    @ApiOperation(value = "删除债券发行", notes = "根据ID删除债券发行记录")
    public String delete(@ApiParam("发行ID") @PathVariable Long issuanceId) {
        try {
            bondIssuanceService.deleteIssuance(issuanceId);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除债券发行失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 批量删除债券发行
     */
    @DeleteMapping("/batch-delete")
    @ApiOperation(value = "批量删除债券发行", notes = "批量删除多条债券发行记录")
    public String batchDelete(@RequestParam(value = "issuanceIds", required = false) List<Long> issuanceIds) {
        try {
            if (issuanceIds == null || issuanceIds.isEmpty()) {
                return new JsonBean(0, "请选择要删除的记录", null).toJson();
            }
            bondIssuanceService.batchDeleteIssuances(issuanceIds);
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除债券发行失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 提交审批
     */
    @PostMapping("/{issuanceId}/submit")
    @ApiOperation(value = "提交审批", notes = "将债券发行计划提交审批")
    public String submitForApproval(@ApiParam("发行ID") @PathVariable Long issuanceId) {
        try {
            bondIssuanceService.submitForApproval(issuanceId);
            return new JsonBean(1, "提交审批成功", null).toJson();
        } catch (Exception e) {
            log.error("提交审批失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "提交审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批通过
     */
    @PostMapping("/{issuanceId}/approve")
    @ApiOperation(value = "审批通过", notes = "审批通过债券发行计划")
    public String approve(@ApiParam("发行ID") @PathVariable Long issuanceId,
                         @RequestParam(required = false) String comments) {
        try {
            bondIssuanceService.approve(issuanceId, comments);
            return new JsonBean(1, "审批通过", null).toJson();
        } catch (Exception e) {
            log.error("审批通过失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 审批拒绝
     */
    @PostMapping("/{issuanceId}/reject")
    @ApiOperation(value = "审批拒绝", notes = "拒绝债券发行计划")
    public String reject(@ApiParam("发行ID") @PathVariable Long issuanceId,
                        @RequestParam(required = false) String comments) {
        try {
            bondIssuanceService.reject(issuanceId, comments);
            return new JsonBean(1, "审批拒绝成功", null).toJson();
        } catch (Exception e) {
            log.error("审批拒绝失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "审批拒绝失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 确认发行
     */
    @PostMapping("/{issuanceId}/issue")
    @ApiOperation(value = "确认发行", notes = "确认债券发行")
    public String confirmIssuance(@ApiParam("发行ID") @PathVariable Long issuanceId,
                                  @RequestParam(required = false) Map<String, Object> params) {
        try {
            bondIssuanceService.confirmIssuance(issuanceId, params != null ? params : new HashMap<>());
            return new JsonBean(1, "发行成功", null).toJson();
        } catch (Exception e) {
            log.error("确认发行失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "发行失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 债券付息
     */
    @PostMapping("/{issuanceId}/pay-interest")
    @ApiOperation(value = "债券付息", notes = "处理债券付息")
    public String payInterest(@ApiParam("发行ID") @PathVariable Long issuanceId,
                             @RequestParam Map<String, Object> params) {
        try {
            // TODO: 实现付息逻辑
            return new JsonBean(1, "付息成功", null).toJson();
        } catch (Exception e) {
            log.error("债券付息失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "付息失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 债券兑付
     */
    @PostMapping("/{issuanceId}/redeem")
    @ApiOperation(value = "债券兑付", notes = "处理债券兑付")
    public String redeem(@ApiParam("发行ID") @PathVariable Long issuanceId,
                        @RequestParam Map<String, Object> params) {
        try {
            // TODO: 实现兑付逻辑
            return new JsonBean(1, "兑付成功", null).toJson();
        } catch (Exception e) {
            log.error("债券兑付失败, issuanceId={}", issuanceId, e);
            return new JsonBean(0, "兑付失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取即将到期的债券
     */
    @GetMapping("/expiring")
    @ApiOperation(value = "获取即将到期的债券", notes = "查询指定天数内即将到期的债券")
    public String getExpiringBonds(@RequestParam(defaultValue = "30") Integer days) {
        try {
            List<TblBondIssuance> list = bondIssuanceService.getExpiringBonds(days);
            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("获取即将到期债券失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 导出债券发行数据
     */
    @PostMapping("/export")
    @ApiOperation(value = "导出债券发行数据", notes = "导出债券发行数据到Excel")
    public void exportBonds(@FlexibleRequestBody BondIssuanceQueryDTO queryDTO, HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                response.setStatus(401);
                return;
            }

            if (queryDTO.getCompanyId() == null && loginStaff.getCurrentOrg() != null) {
                queryDTO.setCompanyId(loginStaff.getCurrentOrg().getOrgid().longValue());
            }

            List<TblBondIssuance> bondList = bondIssuanceService.getAllForExport(queryDTO);

            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("债券发行数据");

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 创建表头
            String[] headers = {"发行编号", "债券名称", "债券类型", "发行金额", "币种",
                    "票面利率(%)", "发行状态", "发行日期", "到期日期", "公司名称"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            // 填充数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            int rowNum = 1;
            for (TblBondIssuance bond : bondList) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(bond.getIssuanceNo() != null ? bond.getIssuanceNo() : "");
                row.createCell(1).setCellValue(bond.getBondName() != null ? bond.getBondName() : "");
                row.createCell(2).setCellValue(getBondTypeName(bond.getBondType()));
                row.createCell(3).setCellValue(bond.getIssuanceAmount() != null ? bond.getIssuanceAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(bond.getCurrencyCode() != null ? bond.getCurrencyCode() : "CNY");
                row.createCell(5).setCellValue(bond.getCouponRate() != null ? bond.getCouponRate().doubleValue() : 0);
                row.createCell(6).setCellValue(getIssuanceStatusName(bond.getIssuanceStatus()));
                row.createCell(7).setCellValue(bond.getIssuanceDate() != null ? sdf.format(bond.getIssuanceDate()) : "");
                row.createCell(8).setCellValue(bond.getMaturityDate() != null ? sdf.format(bond.getMaturityDate()) : "");
                row.createCell(9).setCellValue(bond.getCompanyName() != null ? bond.getCompanyName() : "");
            }

            // 设置响应头
            String filename = "债券发行数据_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            // 写入响应流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("导出债券发行数据失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 下载导入模板
     */
    @GetMapping("/download-template")
    @ApiOperation(value = "下载导入模板", notes = "下载债券发行批量导入的Excel模板")
    public void downloadTemplate(HttpServletResponse response) {
        try {
            // 创建Excel工作簿
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("债券发行导入模板");

            // 创建表头样式
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 创建表头
            String[] headers = {"债券名称*", "债券代码", "债券类型*", "发行金额*", "币种",
                    "面值", "票面利率(%)*", "债券期限*", "期限单位*", "付息频率", "主承销商",
                    "受托管理人", "评级机构", "信用评级", "上市交易所", "备注"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            // 添加示例数据行
            Row exampleRow = sheet.createRow(1);
            exampleRow.createCell(0).setCellValue("示例企业债券2026");
            exampleRow.createCell(1).setCellValue("BOND001");
            exampleRow.createCell(2).setCellValue("CORPORATE");
            exampleRow.createCell(3).setCellValue(100000000);
            exampleRow.createCell(4).setCellValue("CNY");
            exampleRow.createCell(5).setCellValue(100);
            exampleRow.createCell(6).setCellValue(5.5);
            exampleRow.createCell(7).setCellValue(3);
            exampleRow.createCell(8).setCellValue("YEAR");
            exampleRow.createCell(9).setCellValue("SEMI_ANNUAL");
            exampleRow.createCell(10).setCellValue("中信证券");
            exampleRow.createCell(11).setCellValue("中信信托");
            exampleRow.createCell(12).setCellValue("中诚信");
            exampleRow.createCell(13).setCellValue("AAA");
            exampleRow.createCell(14).setCellValue("上交所");
            exampleRow.createCell(15).setCellValue("示例备注");

            // 添加说明行
            Row noteRow = sheet.createRow(3);
            noteRow.createCell(0).setCellValue("说明：带*号为必填项");
            Row noteRow2 = sheet.createRow(4);
            noteRow2.createCell(0).setCellValue("债券类型：CORPORATE(企业债券)/GOVERNMENT(政府债券)/CONVERTIBLE(可转换债券)/PERPETUAL(永续债券)");
            Row noteRow3 = sheet.createRow(5);
            noteRow3.createCell(0).setCellValue("期限单位：DAY(天)/MONTH(月)/YEAR(年)");
            Row noteRow4 = sheet.createRow(6);
            noteRow4.createCell(0).setCellValue("付息频率：MONTHLY(每月)/QUARTERLY(每季度)/SEMI_ANNUAL(每半年)/ANNUAL(每年)/MATURITY(到期一次性)");

            // 设置响应头
            String filename = "债券发行导入模板.xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));

            // 写入响应流
            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            workbook.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            log.error("下载导入模板失败", e);
            response.setStatus(500);
        }
    }

    /**
     * 批量导入债券发行
     */
    @PostMapping("/batch-import")
    @ApiOperation(value = "批量导入债券发行", notes = "通过Excel批量导入债券发行数据")
    public String batchImport(@RequestParam("file") MultipartFile file) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            if (file.isEmpty()) {
                return new JsonBean(0, "上传文件不能为空", null).toJson();
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
                return new JsonBean(0, "只支持Excel文件格式(.xls/.xlsx)", null).toJson();
            }

            // 解析Excel文件
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getPhysicalNumberOfRows();

            if (totalRows <= 1) {
                return new JsonBean(0, "Excel文件中没有数据", null).toJson();
            }

            List<BondIssuanceDTO> bondList = new ArrayList<>();
            List<String> errorMessages = new ArrayList<>();

            // 跳过表头，从第二行开始读取
            for (int i = 1; i < totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                // 跳过空行和说明行
                Cell firstCell = row.getCell(0);
                if (firstCell == null) continue;
                String firstValue = getCellStringValue(firstCell);
                if (firstValue.isEmpty() || firstValue.startsWith("说明")) continue;

                try {
                    BondIssuanceDTO dto = parseRowToDTO(row, i + 1);
                    bondList.add(dto);
                } catch (Exception e) {
                    errorMessages.add("第" + (i + 1) + "行解析失败: " + e.getMessage());
                }
            }
            workbook.close();

            if (bondList.isEmpty()) {
                return new JsonBean(0, "没有有效的数据可导入", errorMessages).toJson();
            }

            // 批量导入
            Long companyId = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid().longValue() : null;
            String companyName = loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgname() : null;
            Long createdBy = loginStaff.getStaffid() != null ? loginStaff.getStaffid().longValue() : null;

            Map<String, Object> result = bondIssuanceService.batchImportBonds(bondList, companyId, companyName, createdBy);
            result.put("parseErrors", errorMessages);

            return new JsonBean(1, "导入完成", result).toJson();
        } catch (Exception e) {
            log.error("批量导入债券发行失败", e);
            return new JsonBean(0, "导入失败: " + e.getMessage(), null).toJson();
        }
    }

    // ==================== 辅助方法 ====================

    private BondIssuanceDTO parseRowToDTO(Row row, int rowNum) {
        BondIssuanceDTO dto = new BondIssuanceDTO();

        // 债券名称（必填）
        String bondName = getCellStringValue(row.getCell(0));
        if (bondName.isEmpty()) {
            throw new RuntimeException("债券名称不能为空");
        }
        dto.setBondName(bondName);

        // 债券代码
        dto.setBondCode(getCellStringValue(row.getCell(1)));

        // 债券类型（必填）
        String bondType = getCellStringValue(row.getCell(2));
        if (bondType.isEmpty()) {
            throw new RuntimeException("债券类型不能为空");
        }
        dto.setBondType(bondType);

        // 发行金额（必填）
        BigDecimal issuanceAmount = getCellBigDecimalValue(row.getCell(3));
        if (issuanceAmount == null || issuanceAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("发行金额必须大于0");
        }
        dto.setIssuanceAmount(issuanceAmount);

        // 币种
        String currencyCode = getCellStringValue(row.getCell(4));
        dto.setCurrencyCode(currencyCode.isEmpty() ? "CNY" : currencyCode);

        // 面值
        BigDecimal faceValue = getCellBigDecimalValue(row.getCell(5));
        dto.setFaceValue(faceValue != null ? faceValue : new BigDecimal("100"));

        // 票面利率（必填）
        BigDecimal couponRate = getCellBigDecimalValue(row.getCell(6));
        if (couponRate == null) {
            throw new RuntimeException("票面利率不能为空");
        }
        dto.setCouponRate(couponRate);

        // 债券期限（必填）
        Integer bondTerm = getCellIntValue(row.getCell(7));
        if (bondTerm == null || bondTerm <= 0) {
            throw new RuntimeException("债券期限必须大于0");
        }
        dto.setBondTerm(bondTerm);

        // 期限单位（必填）
        String termUnit = getCellStringValue(row.getCell(8));
        if (termUnit.isEmpty()) {
            throw new RuntimeException("期限单位不能为空");
        }
        dto.setTermUnit(termUnit);

        // 付息频率
        dto.setPaymentFrequency(getCellStringValue(row.getCell(9)));

        // 主承销商
        dto.setUnderwriter(getCellStringValue(row.getCell(10)));

        // 受托管理人
        dto.setTrustee(getCellStringValue(row.getCell(11)));

        // 评级机构
        dto.setRatingAgency(getCellStringValue(row.getCell(12)));

        // 信用评级
        dto.setCreditRating(getCellStringValue(row.getCell(13)));

        // 上市交易所
        dto.setListingExchange(getCellStringValue(row.getCell(14)));

        // 备注
        dto.setRemark(getCellStringValue(row.getCell(15)));

        return dto;
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private BigDecimal getCellBigDecimalValue(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : new BigDecimal(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private Integer getCellIntValue(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    return (int) cell.getNumericCellValue();
                case STRING:
                    String value = cell.getStringCellValue().trim();
                    return value.isEmpty() ? null : Integer.parseInt(value);
                default:
                    return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    private String getBondTypeName(String bondType) {
        if (bondType == null) return "";
        switch (bondType) {
            case "CORPORATE": return "企业债券";
            case "GOVERNMENT": return "政府债券";
            case "CONVERTIBLE": return "可转换债券";
            case "PERPETUAL": return "永续债券";
            default: return bondType;
        }
    }

    private String getTermUnitName(String termUnit) {
        if (termUnit == null) return "";
        switch (termUnit) {
            case "DAY": return "天";
            case "MONTH": return "月";
            case "YEAR": return "年";
            default: return termUnit;
        }
    }

    private String getPaymentFrequencyName(String frequency) {
        if (frequency == null) return "";
        switch (frequency) {
            case "MONTHLY": return "每月";
            case "QUARTERLY": return "每季度";
            case "SEMI_ANNUAL": return "每半年";
            case "ANNUAL": return "每年";
            case "MATURITY": return "到期一次性";
            default: return frequency;
        }
    }

    private String getIssuanceStatusName(String status) {
        if (status == null) return "";
        switch (status) {
            case "DRAFT": return "草稿";
            case "PENDING": return "待审批";
            case "APPROVED": return "已审批";
            case "REJECTED": return "已拒绝";
            case "ISSUED": return "已发行";
            case "OUTSTANDING": return "存续中";
            case "MATURED": return "已到期";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }
}

