package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "企业人力资源管理", description = "员工、部门、绩效、培训、薪酬、招聘")
@RestController
@RequestMapping("/v1/enterprise/hr")
@Slf4j
public class EnterpriseHrController {

    @Autowired private GzctEnterpriseHrEmployeeMapper employeeMapper;
    @Autowired private GzctEnterpriseHrDepartmentMapper departmentMapper;
    @Autowired private GzctEnterpriseHrPerformanceMapper performanceMapper;
    @Autowired private GzctEnterpriseHrTrainingMapper trainingMapper;
    @Autowired private GzctEnterpriseHrCompensationMapper compensationMapper;
    @Autowired private GzctEnterpriseHrRecruitmentMapper recruitmentMapper;

    // ==================== 员工管理 ====================

    @Operation(summary = "employeeListGet")
    @GetMapping("/employee/list")
    public R<PageResult<GzctEnterpriseHrEmployee>> employeeListGet(@RequestParam(required = false) String enterpriseId, @RequestParam(required = false) String employeeName, @RequestParam(required = false) String department, @RequestParam(required = false) String position, @RequestParam(required = false) String status, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryEmployeeList(enterpriseId, employeeName, department, position, status, pageNumber, pageSize);
    }

    @Operation(summary = "employeeListPost")
    @PostMapping("/employee/list")
    public R<PageResult<GzctEnterpriseHrEmployee>> employeeListPost(@RequestBody Map<String, Object> params) {
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        String employeeName = params.get("employeeName") != null ? params.get("employeeName").toString() : null;
        String department = params.get("department") != null ? params.get("department").toString() : null;
        String position = params.get("position") != null ? params.get("position").toString() : null;
        String status = params.get("status") != null ? params.get("status").toString() : null;
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        return queryEmployeeList(enterpriseId, employeeName, department, position, status, pageNumber, pageSize);
    }

    private R<PageResult<GzctEnterpriseHrEmployee>> queryEmployeeList(String enterpriseId, String employeeName, String department, String position, String status, Integer pageNumber, Integer pageSize) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrEmployee> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrEmployee::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(employeeName)) w.like(GzctEnterpriseHrEmployee::getEmployeeName, employeeName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrEmployee::getDepartment, department);
            if (StringUtils.isNotBlank(position)) w.like(GzctEnterpriseHrEmployee::getPosition, position);
            if (StringUtils.isNotBlank(status)) w.eq(GzctEnterpriseHrEmployee::getStatus, status);
            w.orderByDesc(GzctEnterpriseHrEmployee::getCreateTime);
            Page<GzctEnterpriseHrEmployee> page = employeeMapper.selectPage(new Page<>(pageNumber, pageSize), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询员工列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "employeeDetail")
    @GetMapping("/employee/{id}")
    public R<GzctEnterpriseHrEmployee> employeeDetail(@PathVariable String id) { try { return R.success(employeeMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/employee")
    public R<Boolean> addEmployee(@RequestBody GzctEnterpriseHrEmployee record) { try { record.setCreateTime(LocalDateTime.now()); employeeMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PutMapping("/employee/{id}")
    public R<Boolean> updateEmployee(@PathVariable String id, @RequestBody GzctEnterpriseHrEmployee record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); employeeMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/employee/{id}")
    public R<Boolean> deleteEmployee(@PathVariable String id) { try { return R.success(employeeMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
    @Operation(summary = "transferEmployee")
    @PostMapping("/employee/{id}/transfer")
    public R<Boolean> transferEmployee(@PathVariable String id, @RequestBody Map<String, Object> params) { try { GzctEnterpriseHrEmployee emp = employeeMapper.selectById(id); if (emp != null) { if (params.get("department") != null) emp.setDepartment(params.get("department").toString()); if (params.get("position") != null) emp.setPosition(params.get("position").toString()); emp.setUpdateTime(LocalDateTime.now()); employeeMapper.updateById(emp); } return R.success(true); } catch (Exception e) { return R.fail("调岗失败：" + e.getMessage()); } }
    @Operation(summary = "批量导入-JSON")
    @PostMapping(value = "/employee/batch-import", consumes = "application/json")
    public R<Boolean> batchImportEmployeeJson(@RequestBody List<GzctEnterpriseHrEmployee> records) { try { for (GzctEnterpriseHrEmployee r : records) { r.setCreateTime(LocalDateTime.now()); employeeMapper.insert(r); } return R.success(true); } catch (Exception e) { return R.fail("导入失败：" + e.getMessage()); } }

    @Operation(summary = "批量导入-文件上传")
    @PostMapping(value = "/employee/batch-import", consumes = "multipart/form-data")
    public R<Boolean> batchImportEmployeeFile(@RequestParam("file") org.springframework.web.multipart.MultipartFile file) {
        try {
            org.apache.poi.ss.usermodel.Workbook workbook = org.apache.poi.ss.usermodel.WorkbookFactory.create(file.getInputStream());
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.getSheetAt(0);
            int lastRow = sheet.getLastRowNum();
            int importCount = 0;
            for (int i = 1; i <= lastRow; i++) {
                org.apache.poi.ss.usermodel.Row row = sheet.getRow(i);
                if (row == null) continue;
                GzctEnterpriseHrEmployee emp = new GzctEnterpriseHrEmployee();
                emp.setEmployeeName(getCellStringValue(row, 0));
                emp.setEmployeeNo(getCellStringValue(row, 1));
                emp.setGender(getCellStringValue(row, 2));
                emp.setDepartment(getCellStringValue(row, 3));
                emp.setPosition(getCellStringValue(row, 4));
                emp.setSalaryLevel(getCellStringValue(row, 5));
                emp.setEducation(getCellStringValue(row, 6));
                String entryDateStr = getCellStringValue(row, 7);
                if (StringUtils.isNotBlank(entryDateStr)) { try { emp.setEntryDate(LocalDate.parse(entryDateStr)); } catch (Exception ignored) {} }
                emp.setPhone(getCellStringValue(row, 8));
                emp.setEmail(getCellStringValue(row, 9));
                emp.setEnterpriseId("ent001");
                emp.setEnterpriseName("贵州国资投资集团");
                emp.setStatus("在职");
                emp.setCreateTime(LocalDateTime.now());
                if (StringUtils.isNotBlank(emp.getEmployeeName())) { employeeMapper.insert(emp); importCount++; }
            }
            workbook.close();
            return R.success(true);
        } catch (Exception e) { log.error("文件导入失败", e); return R.fail("导入失败：" + e.getMessage()); }
    }

    private String getCellStringValue(org.apache.poi.ss.usermodel.Row row, int cellIndex) {
        org.apache.poi.ss.usermodel.Cell cell = row.getCell(cellIndex);
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) { java.util.Date d = cell.getDateCellValue(); return d != null ? d.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString() : ""; } return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }
    @Operation(summary = "导出")
    @GetMapping("/employee/export")
    public void exportEmployee(@RequestParam(required = false) String enterpriseId, HttpServletResponse response) { try { LambdaQueryWrapper<GzctEnterpriseHrEmployee> w = new LambdaQueryWrapper<>(); if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrEmployee::getEnterpriseId, enterpriseId); List<GzctEnterpriseHrEmployee> list = employeeMapper.selectList(w); response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); response.setHeader("Content-Disposition", "attachment;filename=employee_" + System.currentTimeMillis() + ".xlsx"); org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook(); org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("员工信息"); org.apache.poi.ss.usermodel.Row header = sheet.createRow(0); String[] headers = {"员工姓名","员工编号","企业名称","部门","职位","学历","入职日期","状态","联系电话"}; for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]); for (int i = 0; i < list.size(); i++) { GzctEnterpriseHrEmployee item = list.get(i); org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1); row.createCell(0).setCellValue(item.getEmployeeName() != null ? item.getEmployeeName() : ""); row.createCell(1).setCellValue(item.getEmployeeNo() != null ? item.getEmployeeNo() : ""); row.createCell(2).setCellValue(item.getEnterpriseName() != null ? item.getEnterpriseName() : ""); row.createCell(3).setCellValue(item.getDepartment() != null ? item.getDepartment() : ""); row.createCell(4).setCellValue(item.getPosition() != null ? item.getPosition() : ""); row.createCell(5).setCellValue(item.getEducation() != null ? item.getEducation() : ""); row.createCell(6).setCellValue(item.getEntryDate() != null ? item.getEntryDate().toString() : ""); row.createCell(7).setCellValue(item.getStatus() != null ? item.getStatus() : ""); row.createCell(8).setCellValue(item.getPhone() != null ? item.getPhone() : ""); } workbook.write(response.getOutputStream()); workbook.close(); } catch (Exception e) { log.error("导出失败", e); } }

    // ==================== 组织架构（部门）管理 ====================

    @Operation(summary = "")
    @GetMapping("/department/list")
    public R<List<Map<String, Object>>> departmentListGet(@RequestParam(required = false) String enterpriseId) {
        return queryDepartmentTree(enterpriseId);
    }

    @Operation(summary = "")
    @PostMapping("/department/list")
    public R<List<Map<String, Object>>> departmentListPost(@RequestBody(required = false) Map<String, Object> params) {
        String enterpriseId = params != null && params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        return queryDepartmentTree(enterpriseId);
    }

    private R<List<Map<String, Object>>> queryDepartmentTree(String enterpriseId) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrDepartment> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrDepartment::getEnterpriseId, enterpriseId);
            List<GzctEnterpriseHrDepartment> all = departmentMapper.selectList(w);
            List<Map<String, Object>> tree = buildDeptTree(all, null);
            return R.success(tree);
        } catch (Exception e) { log.error("查询部门树失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "")
    @GetMapping("/organization/tree")
    public R<List<Map<String, Object>>> getOrganizationTree(@RequestParam(required = false) String enterpriseId) {
        return queryDepartmentTree(enterpriseId);
    }
    @Operation(summary = "departmentDetail")
    @GetMapping("/organization/department/{id}")
    public R<GzctEnterpriseHrDepartment> departmentDetail(@PathVariable String id) { try { return R.success(departmentMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/organization/department")
    public R<Boolean> addDepartment(@RequestBody GzctEnterpriseHrDepartment record) { try { record.setCreateTime(LocalDateTime.now()); departmentMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PutMapping("/organization/department/{id}")
    public R<Boolean> updateDepartment(@PathVariable String id, @RequestBody GzctEnterpriseHrDepartment record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); departmentMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/organization/department/{id}")
    public R<Boolean> deleteDepartment(@PathVariable String id) { try { return R.success(departmentMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    // ==================== 薪酬福利管理 ====================

    @Operation(summary = "compensationListGet")
    @GetMapping("/compensation/list")
    public R<PageResult<GzctEnterpriseHrCompensation>> compensationListGet(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String level,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryCompensationList(enterpriseId, employeeName, department, level, status, pageNumber, pageSize);
    }

    @Operation(summary = "compensationListPost")
    @PostMapping("/compensation/list")
    public R<PageResult<GzctEnterpriseHrCompensation>> compensationListPost(@RequestBody Map<String, Object> params) {
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        String employeeName = params.get("employeeName") != null ? params.get("employeeName").toString() : null;
        String department = params.get("department") != null ? params.get("department").toString() : null;
        String level = params.get("level") != null ? params.get("level").toString() : null;
        String status = params.get("status") != null ? params.get("status").toString() : null;
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        return queryCompensationList(enterpriseId, employeeName, department, level, status, pageNumber, pageSize);
    }

    private R<PageResult<GzctEnterpriseHrCompensation>> queryCompensationList(String enterpriseId, String employeeName, String department, String level, String status, Integer pageNumber, Integer pageSize) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrCompensation> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrCompensation::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(employeeName)) w.like(GzctEnterpriseHrCompensation::getEmployeeName, employeeName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrCompensation::getDepartment, department);
            if (StringUtils.isNotBlank(level)) w.eq(GzctEnterpriseHrCompensation::getLevel, level);
            if (StringUtils.isNotBlank(status)) w.eq(GzctEnterpriseHrCompensation::getStatus, status);
            w.orderByDesc(GzctEnterpriseHrCompensation::getCreateTime);
            Page<GzctEnterpriseHrCompensation> page = compensationMapper.selectPage(new Page<>(pageNumber, pageSize), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询薪酬列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "compensationDetail")
    @GetMapping("/compensation/{id}")
    public R<GzctEnterpriseHrCompensation> compensationDetail(@PathVariable String id) {
        try { return R.success(compensationMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); }
    }

    @Operation(summary = "新增")
    @PostMapping("/compensation")
    public R<Boolean> addCompensation(@RequestBody GzctEnterpriseHrCompensation record) {
        try { record.setCreateTime(LocalDateTime.now()); compensationMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新")
    @PutMapping("/compensation/{id}")
    public R<Boolean> updateCompensation(@PathVariable String id, @RequestBody GzctEnterpriseHrCompensation record) {
        try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); compensationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/compensation/{id}")
    public R<Boolean> deleteCompensation(@PathVariable String id) {
        try { return R.success(compensationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); }
    }

    @Operation(summary = "adjustSalary")
    @PostMapping("/compensation/adjust")
    public R<Boolean> adjustSalary(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("缺少id参数");
            GzctEnterpriseHrCompensation comp = compensationMapper.selectById(id);
            if (comp != null) {
                if (params.get("baseSalary") != null) comp.setBaseSalary(new BigDecimal(params.get("baseSalary").toString()));
                if (params.get("performanceBonus") != null) comp.setPerformanceBonus(new BigDecimal(params.get("performanceBonus").toString()));
                if (params.get("allowance") != null) comp.setAllowance(new BigDecimal(params.get("allowance").toString()));
                // 重新计算总薪酬
                BigDecimal total = (comp.getBaseSalary() != null ? comp.getBaseSalary() : BigDecimal.ZERO)
                    .add(comp.getPerformanceBonus() != null ? comp.getPerformanceBonus() : BigDecimal.ZERO)
                    .add(comp.getAllowance() != null ? comp.getAllowance() : BigDecimal.ZERO);
                comp.setTotalSalary(total);
                comp.setStatus("正常");
                comp.setUpdateTime(LocalDateTime.now());
                compensationMapper.updateById(comp);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("调薪失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出薪酬数据")
    @GetMapping("/compensation/export")
    public void exportCompensation(@RequestParam(required = false) String enterpriseId, @RequestParam(required = false) String employeeName, @RequestParam(required = false) String department, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrCompensation> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrCompensation::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(employeeName)) w.like(GzctEnterpriseHrCompensation::getEmployeeName, employeeName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrCompensation::getDepartment, department);
            List<GzctEnterpriseHrCompensation> list = compensationMapper.selectList(w);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=compensation_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("薪酬数据");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"员工编号","员工姓名","部门","职位","职级","基本工资","绩效奖金","津贴补贴","总薪酬","社保","公积金","最后调薪日期","状态"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseHrCompensation item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getEmployeeNo() != null ? item.getEmployeeNo() : "");
                row.createCell(1).setCellValue(item.getEmployeeName() != null ? item.getEmployeeName() : "");
                row.createCell(2).setCellValue(item.getDepartment() != null ? item.getDepartment() : "");
                row.createCell(3).setCellValue(item.getPosition() != null ? item.getPosition() : "");
                row.createCell(4).setCellValue(item.getLevel() != null ? item.getLevel() : "");
                row.createCell(5).setCellValue(item.getBaseSalary() != null ? item.getBaseSalary().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getPerformanceBonus() != null ? item.getPerformanceBonus().doubleValue() : 0);
                row.createCell(7).setCellValue(item.getAllowance() != null ? item.getAllowance().doubleValue() : 0);
                row.createCell(8).setCellValue(item.getTotalSalary() != null ? item.getTotalSalary().doubleValue() : 0);
                row.createCell(9).setCellValue(item.getSocialInsurance() != null ? item.getSocialInsurance().doubleValue() : 0);
                row.createCell(10).setCellValue(item.getHousingFund() != null ? item.getHousingFund().doubleValue() : 0);
                row.createCell(11).setCellValue(item.getLastAdjustDate() != null ? item.getLastAdjustDate().toString() : "");
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出薪酬数据失败", e); }
    }

    @Operation(summary = "compensationSchemeList")
    @GetMapping("/compensation/scheme/list")
    public R<PageResult<GzctEnterpriseHrCompensation>> compensationSchemeList(@RequestParam(required = false) String enterpriseId, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryCompensationList(enterpriseId, null, null, null, null, pageNumber, pageSize);
    }
    @Operation(summary = "salaryRecord")
    @GetMapping("/compensation/salary/record")
    public R<PageResult<GzctEnterpriseHrCompensation>> salaryRecord(@RequestParam(required = false) String enterpriseId, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryCompensationList(enterpriseId, null, null, null, null, pageNumber, pageSize);
    }
    @Operation(summary = "计算")
    @PostMapping("/compensation/salary/calculate")
    public R<Boolean> calculateSalary(@RequestBody Map<String, Object> params) { return R.success(true); }
    @Operation(summary = "benefitsList")
    @GetMapping("/compensation/benefits/list")
    public R<PageResult<GzctEnterpriseHrCompensation>> benefitsList(@RequestParam(required = false) String enterpriseId, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryCompensationList(enterpriseId, null, null, null, null, pageNumber, pageSize);
    }

    // ==================== 绩效考核管理 ====================

    @Operation(summary = "performanceListGet")
    @GetMapping("/performance/list")
    public R<PageResult<GzctEnterpriseHrPerformance>> performanceListGet(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String assessmentYear,
            @RequestParam(required = false) String assessmentPeriod,
            @RequestParam(required = false) String grade,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryPerformanceList(enterpriseId, employeeName, department, assessmentYear, assessmentPeriod, grade, status, pageNumber, pageSize);
    }

    @Operation(summary = "performanceListPost")
    @PostMapping("/performance/list")
    public R<PageResult<GzctEnterpriseHrPerformance>> performanceListPost(@RequestBody Map<String, Object> params) {
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        String employeeName = params.get("employeeName") != null ? params.get("employeeName").toString() : null;
        String department = params.get("department") != null ? params.get("department").toString() : null;
        String assessmentYear = params.get("assessmentYear") != null ? params.get("assessmentYear").toString() : null;
        String assessmentPeriod = params.get("evaluationPeriod") != null ? params.get("evaluationPeriod").toString() : null;
        String grade = params.get("grade") != null ? params.get("grade").toString() : null;
        String status = params.get("status") != null ? params.get("status").toString() : null;
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        return queryPerformanceList(enterpriseId, employeeName, department, assessmentYear, assessmentPeriod, grade, status, pageNumber, pageSize);
    }

    private R<PageResult<GzctEnterpriseHrPerformance>> queryPerformanceList(String enterpriseId, String employeeName, String department, String assessmentYear, String assessmentPeriod, String grade, String status, Integer pageNumber, Integer pageSize) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrPerformance> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrPerformance::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(employeeName)) w.like(GzctEnterpriseHrPerformance::getEmployeeName, employeeName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrPerformance::getDepartment, department);
            if (StringUtils.isNotBlank(assessmentYear)) w.eq(GzctEnterpriseHrPerformance::getAssessmentYear, assessmentYear);
            if (StringUtils.isNotBlank(assessmentPeriod)) w.eq(GzctEnterpriseHrPerformance::getAssessmentPeriod, assessmentPeriod);
            if (StringUtils.isNotBlank(grade)) w.eq(GzctEnterpriseHrPerformance::getGrade, grade);
            w.orderByDesc(GzctEnterpriseHrPerformance::getCreateTime);
            Page<GzctEnterpriseHrPerformance> page = performanceMapper.selectPage(new Page<>(pageNumber, pageSize), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询绩效列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "performanceEvaluationList")
    @GetMapping("/performance/evaluation/list")
    public R<PageResult<GzctEnterpriseHrPerformance>> performanceEvaluationList(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String employeeName,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryPerformanceList(enterpriseId, employeeName, null, null, null, null, null, pageNumber, pageSize);
    }
    @Operation(summary = "performanceDetail")
    @GetMapping("/performance/evaluation/{id}")
    public R<GzctEnterpriseHrPerformance> performanceDetail(@PathVariable String id) { try { return R.success(performanceMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/performance/evaluation")
    public R<Boolean> addPerformance(@RequestBody GzctEnterpriseHrPerformance record) { try { record.setCreateTime(LocalDateTime.now()); performanceMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/performance/evaluation/{id}")
    public R<Boolean> updatePerformance(@PathVariable String id, @RequestBody GzctEnterpriseHrPerformance record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); performanceMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/performance/evaluation/{id}")
    public R<Boolean> deletePerformance(@PathVariable String id) { try { return R.success(performanceMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "提交")
    @PostMapping("/performance/evaluation/{id}/score")
    public R<Boolean> submitScore(@PathVariable String id, @RequestBody Map<String, Object> params) { try { GzctEnterpriseHrPerformance perf = performanceMapper.selectById(id); if (perf != null) { Object scoreObj = params.get("score"); if (scoreObj != null) perf.setScore(new BigDecimal(scoreObj.toString())); Object totalScoreObj = params.get("totalScore"); if (totalScoreObj != null) perf.setTotalScore(new BigDecimal(totalScoreObj.toString())); String grade = (String) params.get("grade"); if (StringUtils.isNotBlank(grade)) perf.setGrade(grade); Object wpObj = params.get("workPerformance"); if (wpObj != null) perf.setWorkPerformance(new BigDecimal(wpObj.toString())); Object aeObj = params.get("abilityEvaluation"); if (aeObj != null) perf.setAbilityEvaluation(new BigDecimal(aeObj.toString())); Object atObj = params.get("attitudeEvaluation"); if (atObj != null) perf.setAttitudeEvaluation(new BigDecimal(atObj.toString())); perf.setEvaluateTime(LocalDateTime.now()); perf.setUpdateTime(LocalDateTime.now()); performanceMapper.updateById(perf); } return R.success(true); } catch (Exception e) { return R.fail("评分失败：" + e.getMessage()); } }
    @Operation(summary = "")
    @GetMapping("/performance/statistics")
    public R<Map<String, Object>> performanceStatistics(@RequestParam(required = false) String enterpriseId) { try { Map<String, Object> result = new HashMap<>(); List<GzctEnterpriseHrPerformance> all = performanceMapper.selectList(null); result.put("totalCount", all.size()); result.put("excellentCount", all.stream().filter(p -> "优秀".equals(p.getGrade())).count()); double avg = all.stream().filter(p -> p.getScore() != null).mapToDouble(p -> p.getScore().doubleValue()).average().orElse(0); result.put("avgScore", Math.round(avg * 10.0) / 10.0); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }

    @Operation(summary = "导出绩效考核数据")
    @GetMapping("/performance/export")
    public void exportPerformance(@RequestParam(required = false) String enterpriseId, @RequestParam(required = false) String employeeName, @RequestParam(required = false) String department, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrPerformance> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrPerformance::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(employeeName)) w.like(GzctEnterpriseHrPerformance::getEmployeeName, employeeName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrPerformance::getDepartment, department);
            List<GzctEnterpriseHrPerformance> list = performanceMapper.selectList(w);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=performance_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("绩效考核");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"员工ID","员工姓名","部门","职位","考核年度","考核周期","工作绩效","能力评价","态度评价","总分","考核等级","考核人"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseHrPerformance item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getEmployeeId() != null ? item.getEmployeeId() : "");
                row.createCell(1).setCellValue(item.getEmployeeName() != null ? item.getEmployeeName() : "");
                row.createCell(2).setCellValue(item.getDepartment() != null ? item.getDepartment() : "");
                row.createCell(3).setCellValue(item.getPosition() != null ? item.getPosition() : "");
                row.createCell(4).setCellValue(item.getAssessmentYear() != null ? item.getAssessmentYear() : "");
                row.createCell(5).setCellValue(item.getAssessmentPeriod() != null ? item.getAssessmentPeriod() : "");
                row.createCell(6).setCellValue(item.getWorkPerformance() != null ? item.getWorkPerformance().doubleValue() : 0);
                row.createCell(7).setCellValue(item.getAbilityEvaluation() != null ? item.getAbilityEvaluation().doubleValue() : 0);
                row.createCell(8).setCellValue(item.getAttitudeEvaluation() != null ? item.getAttitudeEvaluation().doubleValue() : 0);
                row.createCell(9).setCellValue(item.getTotalScore() != null ? item.getTotalScore().doubleValue() : 0);
                row.createCell(10).setCellValue(item.getGrade() != null ? item.getGrade() : "");
                row.createCell(11).setCellValue(item.getEvaluator() != null ? item.getEvaluator() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出绩效数据失败", e); }
    }

    // ==================== 培训发展管理 ====================

    @Operation(summary = "trainingListGet")
    @GetMapping("/training/list")
    public R<PageResult<GzctEnterpriseHrTraining>> trainingListGet(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String trainingName,
            @RequestParam(required = false) String trainingType,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryTrainingList(enterpriseId, trainingName, trainingType, status, pageNumber, pageSize);
    }

    @Operation(summary = "trainingListPost")
    @PostMapping("/training/list")
    public R<PageResult<GzctEnterpriseHrTraining>> trainingListPost(@RequestBody Map<String, Object> params) {
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        String trainingName = params.get("trainingName") != null ? params.get("trainingName").toString() : null;
        String trainingType = params.get("trainingType") != null ? params.get("trainingType").toString() : null;
        String status = params.get("status") != null ? params.get("status").toString() : null;
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        return queryTrainingList(enterpriseId, trainingName, trainingType, status, pageNumber, pageSize);
    }

    private R<PageResult<GzctEnterpriseHrTraining>> queryTrainingList(String enterpriseId, String trainingName, String trainingType, String status, Integer pageNumber, Integer pageSize) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrTraining> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrTraining::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(trainingName)) w.like(GzctEnterpriseHrTraining::getTrainingName, trainingName);
            if (StringUtils.isNotBlank(trainingType)) w.eq(GzctEnterpriseHrTraining::getTrainingType, trainingType);
            if (StringUtils.isNotBlank(status)) w.eq(GzctEnterpriseHrTraining::getStatus, status);
            w.orderByDesc(GzctEnterpriseHrTraining::getCreateTime);
            Page<GzctEnterpriseHrTraining> page = trainingMapper.selectPage(new Page<>(pageNumber, pageSize), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询培训列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "trainingPlanList")
    @GetMapping("/training/plan/list")
    public R<PageResult<GzctEnterpriseHrTraining>> trainingPlanList(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String trainingName,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryTrainingList(enterpriseId, trainingName, null, status, pageNumber, pageSize);
    }
    @Operation(summary = "trainingDetail")
    @GetMapping("/training/plan/{id}")
    public R<GzctEnterpriseHrTraining> trainingDetail(@PathVariable String id) { try { return R.success(trainingMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }

    @Operation(summary = "新增")
    @PostMapping("/training/plan")
    public R<Boolean> addTrainingPlan(@RequestBody GzctEnterpriseHrTraining record) { try { record.setCreateTime(LocalDateTime.now()); trainingMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/training/plan/{id}")
    public R<Boolean> updateTrainingPlan(@PathVariable String id, @RequestBody GzctEnterpriseHrTraining record) { try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); trainingMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "删除")
    @DeleteMapping("/training/plan/{id}")
    public R<Boolean> deleteTrainingPlan(@PathVariable String id) { try { return R.success(trainingMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "trainingRecord")
    @GetMapping("/training/record")
    public R<PageResult<GzctEnterpriseHrTraining>> trainingRecord(@RequestParam(required = false) String enterpriseId, @RequestParam(defaultValue = "1") Integer pageNumber, @RequestParam(defaultValue = "15") Integer pageSize) { return trainingPlanList(enterpriseId, null, null, pageNumber, pageSize); }
    @Operation(summary = "enrollTraining")
    @PostMapping("/training/enroll")
    public R<Boolean> enrollTraining(@RequestBody Map<String, Object> params) { try { String id = params.get("id") != null ? params.get("id").toString() : null; if (id == null) return R.fail("缺少id参数"); GzctEnterpriseHrTraining t = trainingMapper.selectById(id); if (t != null) { int current = t.getCurrentParticipants() != null ? t.getCurrentParticipants() : 0; int max = t.getMaxParticipants() != null ? t.getMaxParticipants() : 0; if (max > 0 && current >= max) return R.fail("培训已满员"); t.setCurrentParticipants(current + 1); t.setUpdateTime(LocalDateTime.now()); trainingMapper.updateById(t); } return R.success(true); } catch (Exception e) { return R.fail("报名失败：" + e.getMessage()); } }
    @Operation(summary = "evaluateTraining")
    @PostMapping("/training/{id}/evaluate")
    public R<Boolean> evaluateTraining(@PathVariable String id, @RequestBody Map<String, Object> params) { try { GzctEnterpriseHrTraining t = trainingMapper.selectById(id); if (t != null) { Object scoreObj = params.get("score"); if (scoreObj != null) t.setScore(new BigDecimal(scoreObj.toString())); Object rateObj = params.get("completionRate"); if (rateObj != null) t.setCompletionRate(new BigDecimal(rateObj.toString())); t.setUpdateTime(LocalDateTime.now()); trainingMapper.updateById(t); } return R.success(true); } catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); } }

    @Operation(summary = "导出培训数据")
    @GetMapping("/training/export")
    public void exportTraining(@RequestParam(required = false) String enterpriseId, @RequestParam(required = false) String trainingName, @RequestParam(required = false) String trainingType, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrTraining> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrTraining::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(trainingName)) w.like(GzctEnterpriseHrTraining::getTrainingName, trainingName);
            if (StringUtils.isNotBlank(trainingType)) w.eq(GzctEnterpriseHrTraining::getTrainingType, trainingType);
            List<GzctEnterpriseHrTraining> list = trainingMapper.selectList(w);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=training_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("培训记录");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"培训编号","培训名称","培训类型","讲师","开始日期","结束日期","最大人数","当前人数","完成率(%)","预算金额","实际金额","评分","状态"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseHrTraining item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getTrainingNo() != null ? item.getTrainingNo() : "");
                row.createCell(1).setCellValue(item.getTrainingName() != null ? item.getTrainingName() : "");
                row.createCell(2).setCellValue(item.getTrainingType() != null ? item.getTrainingType() : "");
                row.createCell(3).setCellValue(item.getTrainer() != null ? item.getTrainer() : "");
                row.createCell(4).setCellValue(item.getStartDate() != null ? item.getStartDate().toString() : "");
                row.createCell(5).setCellValue(item.getEndDate() != null ? item.getEndDate().toString() : "");
                row.createCell(6).setCellValue(item.getMaxParticipants() != null ? item.getMaxParticipants() : 0);
                row.createCell(7).setCellValue(item.getCurrentParticipants() != null ? item.getCurrentParticipants() : 0);
                row.createCell(8).setCellValue(item.getCompletionRate() != null ? item.getCompletionRate().doubleValue() : 0);
                row.createCell(9).setCellValue(item.getBudgetAmount() != null ? item.getBudgetAmount().doubleValue() : 0);
                row.createCell(10).setCellValue(item.getActualAmount() != null ? item.getActualAmount().doubleValue() : 0);
                row.createCell(11).setCellValue(item.getScore() != null ? item.getScore().doubleValue() : 0);
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出培训数据失败", e); }
    }

    // ==================== 招聘管理 ====================

    @Operation(summary = "recruitmentListGet")
    @GetMapping("/recruitment/list")
    public R<PageResult<GzctEnterpriseHrRecruitment>> recruitmentListGet(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String positionName,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String positionType,
            @RequestParam(required = false) String urgency,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryRecruitmentList(enterpriseId, positionName, department, positionType, urgency, status, pageNumber, pageSize);
    }

    @Operation(summary = "recruitmentListPost")
    @PostMapping("/recruitment/list")
    public R<PageResult<GzctEnterpriseHrRecruitment>> recruitmentListPost(@RequestBody Map<String, Object> params) {
        String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
        String positionName = params.get("positionName") != null ? params.get("positionName").toString() : null;
        String department = params.get("department") != null ? params.get("department").toString() : null;
        String positionType = params.get("positionType") != null ? params.get("positionType").toString() : null;
        String urgency = params.get("urgency") != null ? params.get("urgency").toString() : null;
        String status = params.get("status") != null ? params.get("status").toString() : null;
        int pageNumber = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        return queryRecruitmentList(enterpriseId, positionName, department, positionType, urgency, status, pageNumber, pageSize);
    }

    private R<PageResult<GzctEnterpriseHrRecruitment>> queryRecruitmentList(String enterpriseId, String positionName, String department, String positionType, String urgency, String status, Integer pageNumber, Integer pageSize) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrRecruitment> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrRecruitment::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(positionName)) w.like(GzctEnterpriseHrRecruitment::getPositionName, positionName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrRecruitment::getDepartment, department);
            if (StringUtils.isNotBlank(positionType)) w.eq(GzctEnterpriseHrRecruitment::getPositionType, positionType);
            if (StringUtils.isNotBlank(urgency)) w.eq(GzctEnterpriseHrRecruitment::getUrgency, urgency);
            if (StringUtils.isNotBlank(status)) w.eq(GzctEnterpriseHrRecruitment::getStatus, status);
            w.orderByDesc(GzctEnterpriseHrRecruitment::getCreateTime);
            Page<GzctEnterpriseHrRecruitment> page = recruitmentMapper.selectPage(new Page<>(pageNumber, pageSize), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询招聘列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "recruitmentDetail")
    @GetMapping("/recruitment/{id}")
    public R<GzctEnterpriseHrRecruitment> recruitmentDetail(@PathVariable String id) {
        try { return R.success(recruitmentMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); }
    }

    @Operation(summary = "新增")
    @PostMapping("/recruitment")
    public R<Boolean> addRecruitment(@RequestBody GzctEnterpriseHrRecruitment record) {
        try { record.setCreateTime(LocalDateTime.now()); recruitmentMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新")
    @PutMapping("/recruitment/{id}")
    public R<Boolean> updateRecruitment(@PathVariable String id, @RequestBody GzctEnterpriseHrRecruitment record) {
        try { record.setId(id); record.setUpdateTime(LocalDateTime.now()); recruitmentMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除")
    @DeleteMapping("/recruitment/{id}")
    public R<Boolean> deleteRecruitment(@PathVariable String id) {
        try { return R.success(recruitmentMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); }
    }

    @Operation(summary = "recruitmentPositionList")
    @GetMapping("/recruitment/position/list")
    public R<PageResult<GzctEnterpriseHrRecruitment>> recruitmentPositionList(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(defaultValue = "1") Integer pageNumber,
            @RequestParam(defaultValue = "15") Integer pageSize) {
        return queryRecruitmentList(enterpriseId, null, null, null, null, null, pageNumber, pageSize);
    }

    @Operation(summary = "publishPosition")
    @PostMapping("/recruitment/position")
    public R<Boolean> publishPosition(@RequestBody GzctEnterpriseHrRecruitment record) {
        try { record.setCreateTime(LocalDateTime.now()); recruitmentMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("发布失败：" + e.getMessage()); }
    }

    @Operation(summary = "scheduleInterview")
    @GetMapping("/recruitment/candidate/list")
    public R<List<Map<String, Object>>> candidateList(@RequestParam(required = false) String enterpriseId) { return R.success(new ArrayList<>()); }

    @Operation(summary = "scheduleInterview")
    @PostMapping("/recruitment/interview/schedule")
    public R<Boolean> scheduleInterview(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "hireCandidate")
    @PostMapping("/recruitment/candidate/{id}/hire")
    public R<Boolean> hireCandidate(@PathVariable String id, @RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "导出招聘数据")
    @GetMapping("/recruitment/export")
    public void exportRecruitment(@RequestParam(required = false) String enterpriseId, @RequestParam(required = false) String positionName, @RequestParam(required = false) String department, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseHrRecruitment> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(enterpriseId)) w.eq(GzctEnterpriseHrRecruitment::getEnterpriseId, enterpriseId);
            if (StringUtils.isNotBlank(positionName)) w.like(GzctEnterpriseHrRecruitment::getPositionName, positionName);
            if (StringUtils.isNotBlank(department)) w.like(GzctEnterpriseHrRecruitment::getDepartment, department);
            List<GzctEnterpriseHrRecruitment> list = recruitmentMapper.selectList(w);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=recruitment_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("招聘数据");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"职位编号","职位名称","部门","职位类型","需求人数","应聘人数","面试人数","录用人数","薪资范围","紧急程度","发布日期","截止日期","状态"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctEnterpriseHrRecruitment item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getPositionNo() != null ? item.getPositionNo() : "");
                row.createCell(1).setCellValue(item.getPositionName() != null ? item.getPositionName() : "");
                row.createCell(2).setCellValue(item.getDepartment() != null ? item.getDepartment() : "");
                row.createCell(3).setCellValue(item.getPositionType() != null ? item.getPositionType() : "");
                row.createCell(4).setCellValue(item.getRequiredCount() != null ? item.getRequiredCount() : 0);
                row.createCell(5).setCellValue(item.getApplicantCount() != null ? item.getApplicantCount() : 0);
                row.createCell(6).setCellValue(item.getInterviewCount() != null ? item.getInterviewCount() : 0);
                row.createCell(7).setCellValue(item.getHiredCount() != null ? item.getHiredCount() : 0);
                row.createCell(8).setCellValue(item.getSalaryRange() != null ? item.getSalaryRange() : "");
                row.createCell(9).setCellValue(item.getUrgency() != null ? item.getUrgency() : "");
                row.createCell(10).setCellValue(item.getPublishDate() != null ? item.getPublishDate().toString() : "");
                row.createCell(11).setCellValue(item.getDeadline() != null ? item.getDeadline().toString() : "");
                row.createCell(12).setCellValue(item.getStatus() != null ? item.getStatus() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出招聘数据失败", e); }
    }

    // ==================== 统计接口 ====================

    @Operation(summary = "")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> hrOverviewStats() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 员工总数
            long totalEmployees = employeeMapper.selectCount(null);
            result.put("totalEmployees", totalEmployees);
            // 优秀员工数（绩效等级为优秀的在职员工）
            long highPerformers = performanceMapper.selectCount(
                new LambdaQueryWrapper<GzctEnterpriseHrPerformance>().eq(GzctEnterpriseHrPerformance::getGrade, "优秀"));
            result.put("highPerformers", highPerformers);
            // 培训项目数
            long trainingPrograms = trainingMapper.selectCount(null);
            result.put("trainingPrograms", trainingPrograms);
            // 平均薪资（万元）
            List<GzctEnterpriseHrCompensation> comps = compensationMapper.selectList(null);
            double avgSalary = comps.stream()
                .filter(c -> c.getTotalSalary() != null)
                .mapToDouble(c -> c.getTotalSalary().doubleValue())
                .average().orElse(0) / 10000.0;
            result.put("avgSalary", Math.round(avgSalary * 10.0) / 10.0);
            // 额外统计
            result.put("activeEmployees", employeeMapper.selectCount(new LambdaQueryWrapper<GzctEnterpriseHrEmployee>().eq(GzctEnterpriseHrEmployee::getStatus, "在职")));
            result.put("totalDepartments", departmentMapper.selectCount(null));
            List<GzctEnterpriseHrPerformance> perfs = performanceMapper.selectList(null);
            double avgPerf = perfs.stream().filter(p -> p.getScore() != null).mapToDouble(p -> p.getScore().doubleValue()).average().orElse(0);
            result.put("avgPerformanceScore", Math.round(avgPerf * 10.0) / 10.0);
            return R.success(result);
        } catch (Exception e) { log.error("统计查询失败", e); return R.fail("统计失败：" + e.getMessage()); }
    }
    @Operation(summary = "")
    @GetMapping("/statistics/employee-distribution")
    public R<Map<String, Object>> employeeDistribution(@RequestParam(required = false) String enterpriseId) { try { Map<String, Object> result = new HashMap<>(); result.put("totalEmployees", employeeMapper.selectCount(null)); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }
    @Operation(summary = "")
    @GetMapping("/statistics/salary")
    public R<Map<String, Object>> salaryStatistics(@RequestParam(required = false) String enterpriseId) { try { Map<String, Object> result = new HashMap<>(); result.put("employeeCount", employeeMapper.selectCount(null)); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }
    @Operation(summary = "")
    @GetMapping("/statistics/training")
    public R<Map<String, Object>> trainingStatistics(@RequestParam(required = false) String enterpriseId) { try { Map<String, Object> result = new HashMap<>(); List<GzctEnterpriseHrTraining> trainings = trainingMapper.selectList(null); result.put("totalTrainings", trainings.size()); result.put("completed", trainings.stream().filter(t -> "已完成".equals(t.getStatus())).count()); result.put("totalParticipants", trainings.stream().mapToInt(t -> t.getParticipantCount() != null ? t.getParticipantCount() : 0).sum()); return R.success(result); } catch (Exception e) { return R.fail("统计失败：" + e.getMessage()); } }
    @Operation(summary = "")
    @GetMapping("/statistics/performance")
    public R<Map<String, Object>> performanceStatisticsData(@RequestParam(required = false) String enterpriseId) { return performanceStatistics(enterpriseId); }

    private List<Map<String, Object>> buildDeptTree(List<GzctEnterpriseHrDepartment> all, String parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        for (GzctEnterpriseHrDepartment dept : all) {
            String pid = dept.getParentId() != null ? dept.getParentId() : "0";
            String targetPid = parentId != null ? parentId : "0";
            if (pid.equals(targetPid)) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", dept.getId());
                node.put("deptName", dept.getDeptName());
                node.put("deptCode", dept.getDeptCode());
                node.put("managerName", dept.getManagerName());
                node.put("employeeCount", dept.getEmployeeCount());
                node.put("deptLevel", dept.getDeptLevel());
                node.put("status", dept.getStatus());
                List<Map<String, Object>> children = buildDeptTree(all, dept.getId());
                if (!children.isEmpty()) node.put("children", children);
                tree.add(node);
            }
        }
        return tree;
    }
}