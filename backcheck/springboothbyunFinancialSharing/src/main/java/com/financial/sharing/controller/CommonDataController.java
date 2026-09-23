package com.financial.sharing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.business.entity.TblContract;
import com.financial.sharing.business.mapper.ContractMapper;
import com.financial.sharing.business.mapper.ExpenseReportMapper;
import com.financial.sharing.business.mapper.LoanApplicationMapper;
import com.financial.sharing.business.mapper.PrepaymentMapper;
import com.financial.sharing.entity.TblExpenseItem;
import com.financial.sharing.entity.TblProjectConfig;
import com.financial.sharing.mapper.TblExpenseItemMapper;
import com.financial.sharing.mapper.TblProjectConfigMapper;
import com.financial.sharing.service.AccountSubjectService;
import com.financial.sharing.service.OrganizationService;
import com.financial.sharing.service.SupplierService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 公共下拉数据控制器
 * 提供用户、部门、供应商、费用项目、会计科目、项目、合同等下拉数据接口
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Slf4j
@RestController
@RequestMapping("/common/data")
@Api(tags = "公共下拉数据")
@CrossOrigin
public class CommonDataController {

    @Resource
    private UserProvider userProvider;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SupplierService supplierService;

    @Autowired
    private TblExpenseItemMapper expenseItemMapper;

    @Autowired
    private AccountSubjectService accountSubjectService;

    @Autowired
    private TblProjectConfigMapper projectConfigMapper;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ExpenseReportMapper expenseReportMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @ApiOperation("获取用户下拉列表")
    @GetMapping("/users")
    public MyJsonBean getUserDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("部门ID") String deptId,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            // 从UserProvider获取当前组织下的用户列表
            List<Map<String, Object>> userList = new ArrayList<>();

            // 构建返回数据 - 基于当前登录用户的组织信息
            Map<String, Object> currentUser = new HashMap<>();
            currentUser.put("userId", loginStaff.getStaffid().toString());
            currentUser.put("userName", loginStaff.getRealname());
            currentUser.put("account", loginStaff.getUsername());
            currentUser.put("deptId", loginStaff.getLinkDetp() != null ? loginStaff.getLinkDetp().getOrgid().toString() : null);
            currentUser.put("deptName", loginStaff.getLinkDetp() != null ? loginStaff.getLinkDetp().getOrgname() : null);
            currentUser.put("orgId", loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgid().toString() : null);
            currentUser.put("orgName", loginStaff.getCurrentOrg() != null ? loginStaff.getCurrentOrg().getOrgname() : null);
            userList.add(currentUser);

            return MyJsonBean.successData("查询成功", userList);
        } catch (Exception e) {
            log.error("获取用户下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取部门下拉列表")
    @GetMapping("/departments")
    public MyJsonBean getDepartmentDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("组织类型(1公司 2部门 3科室 4小组)") Integer orgType,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();

            // 调用OrganizationService获取部门列表，默认查询部门类型(2)
            List<?> departments = organizationService.getByOrgType(
                orgType != null ? orgType : 2, tenantId);

            return MyJsonBean.successData("查询成功", departments);
        } catch (Exception e) {
            log.error("获取部门下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取供应商下拉列表")
    @GetMapping("/suppliers")
    public MyJsonBean getSupplierDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            // 调用已有的SupplierService获取下拉列表
            List<?> suppliers = supplierService.queryDropdownList(keyword);
            return MyJsonBean.successData("查询成功", suppliers);
        } catch (Exception e) {
            log.error("获取供应商下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取费用项目下拉列表")
    @GetMapping("/expense-items")
    public MyJsonBean getExpenseItemDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            QueryWrapper<TblExpenseItem> queryWrapper = new QueryWrapper<>();
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(w -> w.like("ITEM_NAME", keyword).or().like("ITEM_CODE", keyword));
            }
            if (isEnabled != null) {
                queryWrapper.eq("IS_ENABLED", isEnabled ? 1 : 0);
            } else {
                queryWrapper.eq("IS_ENABLED", 1);
            }
            queryWrapper.orderByAsc("ITEM_CODE");

            List<TblExpenseItem> items = expenseItemMapper.selectList(queryWrapper);

            // 转换为下拉列表格式
            List<Map<String, Object>> dropdownList = items.stream().map(item -> {
                Map<String, Object> map = new HashMap<>();
                map.put("itemId", item.getItemId());
                map.put("itemCode", item.getItemCode());
                map.put("itemName", item.getItemName());
                map.put("parentId", item.getParentId());
                map.put("itemLevel", item.getItemLevel());
                map.put("isEnabled", item.getIsEnabled());
                return map;
            }).collect(Collectors.toList());

            return MyJsonBean.successData("查询成功", dropdownList);
        } catch (Exception e) {
            log.error("获取费用项目下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取会计科目下拉列表")
    @GetMapping("/account-subjects")
    public MyJsonBean getAccountSubjectDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("科目类型") Integer subjectType,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            Long tenantId = loginStaff.getCurrentOrg().getOrgid().longValue();
            Long bookId = 1L; // 默认主账簿

            List<?> subjects;
            if (subjectType != null) {
                subjects = accountSubjectService.getAccountSubjectsByType(subjectType, bookId, tenantId);
            } else {
                subjects = accountSubjectService.getAccountSubjectTree(bookId, tenantId);
            }

            return MyJsonBean.successData("查询成功", subjects);
        } catch (Exception e) {
            log.error("获取会计科目下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取项目下拉列表")
    @GetMapping("/projects")
    public MyJsonBean getProjectDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("项目状态") String projectStatus,
            @RequestParam(required = false) @ApiParam("是否启用") Boolean isEnabled) {
        try {
            QueryWrapper<TblProjectConfig> queryWrapper = new QueryWrapper<>();
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(w -> w.like("PROJECT_NAME", keyword).or().like("PROJECT_CODE", keyword));
            }
            if (projectStatus != null && !projectStatus.isEmpty()) {
                queryWrapper.eq("PROJECT_STATUS", projectStatus);
            }
            if (isEnabled != null) {
                queryWrapper.eq("IS_ENABLED", isEnabled ? 1 : 0);
            } else {
                queryWrapper.eq("IS_ENABLED", 1);
            }
            queryWrapper.orderByAsc("PROJECT_CODE");

            List<TblProjectConfig> projects = projectConfigMapper.selectList(queryWrapper);

            // 转换为下拉列表格式
            List<Map<String, Object>> dropdownList = projects.stream().map(project -> {
                Map<String, Object> map = new HashMap<>();
                map.put("projectId", project.getProjectId());
                map.put("projectCode", project.getProjectCode());
                map.put("projectName", project.getProjectName());
                map.put("projectStatus", project.getProjectStatus());
                return map;
            }).collect(Collectors.toList());

            return MyJsonBean.successData("查询成功", dropdownList);
        } catch (Exception e) {
            log.error("获取项目下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取合同下拉列表")
    @GetMapping("/contracts")
    public MyJsonBean getContractDropdownList(
            @RequestParam(required = false) @ApiParam("关键字") String keyword,
            @RequestParam(required = false) @ApiParam("合同状态") String contractStatus,
            @RequestParam(required = false) @ApiParam("供应商ID") String supplierId) {
        try {
            QueryWrapper<TblContract> queryWrapper = new QueryWrapper<>();
            if (keyword != null && !keyword.isEmpty()) {
                queryWrapper.and(w -> w.like("CONTRACT_NAME", keyword).or().like("CONTRACT_CODE", keyword));
            }
            if (contractStatus != null && !contractStatus.isEmpty()) {
                queryWrapper.eq("CONTRACT_STATUS", contractStatus);
            } else {
                // 默认只查询已审批的合同
                queryWrapper.in("CONTRACT_STATUS", "APPROVED", "EXECUTING");
            }
            if (supplierId != null && !supplierId.isEmpty()) {
                queryWrapper.eq("SUPPLIER_ID", supplierId);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblContract> contracts = contractMapper.selectList(queryWrapper);

            // 转换为下拉列表格式
            List<Map<String, Object>> dropdownList = contracts.stream().map(contract -> {
                Map<String, Object> map = new HashMap<>();
                map.put("contractId", contract.getContractId());
                map.put("contractCode", contract.getContractCode());
                map.put("contractName", contract.getContractName());
                map.put("contractType", contract.getContractType());
                map.put("contractStatus", contract.getContractStatus());
                map.put("contractAmount", contract.getContractAmount());
                map.put("supplierId", contract.getSupplierId());
                map.put("supplierName", contract.getSupplierName());
                return map;
            }).collect(Collectors.toList());

            return MyJsonBean.successData("查询成功", dropdownList);
        } catch (Exception e) {
            log.error("获取合同下拉列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取业务单据统计")
    @GetMapping("/document-stats")
    public MyJsonBean getBusinessDocumentStats() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return MyJsonBean.errorData("用户已失效");
            }

            Map<String, Object> stats = new HashMap<>();

            // 报销单统计
            QueryWrapper<com.financial.sharing.business.entity.TblExpenseReport> expenseWrapper = new QueryWrapper<>();
            int expenseClaimCount = (int) expenseReportMapper.selectCount(expenseWrapper);
            stats.put("expenseClaimCount", expenseClaimCount);

            expenseWrapper = new QueryWrapper<>();
            expenseWrapper.eq("REPORT_STATUS", "PENDING");
            int pendingExpenseCount = (int) expenseReportMapper.selectCount(expenseWrapper);
            stats.put("pendingExpenseCount", pendingExpenseCount);

            // 借款单统计
            QueryWrapper<com.financial.sharing.business.entity.TblLoanApplication> loanWrapper = new QueryWrapper<>();
            int loanCount = (int) loanApplicationMapper.selectCount(loanWrapper);
            stats.put("loanCount", loanCount);

            loanWrapper = new QueryWrapper<>();
            loanWrapper.in("RETURN_STATUS", "UNRETURNED", "PARTIAL");
            int pendingLoanCount = (int) loanApplicationMapper.selectCount(loanWrapper);
            stats.put("pendingLoanCount", pendingLoanCount);

            // 预付款统计
            QueryWrapper<com.financial.sharing.business.entity.TblPrepayment> prepaymentWrapper = new QueryWrapper<>();
            int prepaymentCount = (int) prepaymentMapper.selectCount(prepaymentWrapper);
            stats.put("prepaymentCount", prepaymentCount);

            prepaymentWrapper = new QueryWrapper<>();
            prepaymentWrapper.eq("WRITEOFF_STATUS", "UNWRITTEN_OFF");
            int pendingPrepaymentCount = (int) prepaymentMapper.selectCount(prepaymentWrapper);
            stats.put("pendingPrepaymentCount", pendingPrepaymentCount);

            // 合同统计
            QueryWrapper<TblContract> contractWrapper = new QueryWrapper<>();
            int contractCount = (int) contractMapper.selectCount(contractWrapper);
            stats.put("contractCount", contractCount);

            contractWrapper = new QueryWrapper<>();
            contractWrapper.eq("CONTRACT_STATUS", "EXECUTING");
            int activeContractCount = (int) contractMapper.selectCount(contractWrapper);
            stats.put("activeContractCount", activeContractCount);

            return MyJsonBean.successData("查询成功", stats);
        } catch (Exception e) {
            log.error("获取业务单据统计失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}
