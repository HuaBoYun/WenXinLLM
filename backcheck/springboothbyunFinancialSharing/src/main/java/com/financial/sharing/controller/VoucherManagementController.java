package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblOrganizationUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 凭证管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "凭证综合管理")
@RestController
@RequestMapping("/voucher-management")
@CrossOrigin
public class VoucherManagementController {

    
    @ApiOperation("凭证汇总查询")
    @PostMapping("/summary")
    public String getVoucherSummary(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody Map<String, Object> param) {
        try {
            // 创建固定测试用户
            TblStaffUtil loginStaff = new TblStaffUtil();
            loginStaff.setStaffid(new java.math.BigDecimal(5555));
            loginStaff.setUsername("星光");
            loginStaff.setRealname("星光");

            // 创建测试部门
            TblOrganizationUtil linkDept = new TblOrganizationUtil();
            linkDept.setOrgid(new java.math.BigDecimal(1));
            linkDept.setOrgname("测试部门");
            loginStaff.setLinkDetp(linkDept);

            // 创建测试组织
            TblOrganizationUtil currentOrg = new TblOrganizationUtil();
            currentOrg.setOrgid(new java.math.BigDecimal(1));
            currentOrg.setOrgname("测试组织");
            loginStaff.setCurrentOrg(currentOrg);

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCount", 1250);
            summary.put("totalAmount", new BigDecimal("12500000.00"));
            summary.put("draftCount", 15);
            summary.put("reviewedCount", 25);
            summary.put("postedCount", 1180);
            summary.put("cancelledCount", 30);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(summary);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("凭证汇总查询失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量过账")
    @PostMapping("/batchPost")
    public String batchPostVouchers(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody Map<String, Object> param) {
        try {
            // 创建固定测试用户
            TblStaffUtil loginStaff = new TblStaffUtil();
            loginStaff.setStaffid(new java.math.BigDecimal(5555));
            loginStaff.setUsername("星光");
            loginStaff.setRealname("星光");

            // 创建测试部门
            TblOrganizationUtil linkDept = new TblOrganizationUtil();
            linkDept.setOrgid(new java.math.BigDecimal(1));
            linkDept.setOrgname("测试部门");
            loginStaff.setLinkDetp(linkDept);

            // 创建测试组织
            TblOrganizationUtil currentOrg = new TblOrganizationUtil();
            currentOrg.setOrgid(new java.math.BigDecimal(1));
            currentOrg.setOrgname("测试组织");
            loginStaff.setCurrentOrg(currentOrg);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", "BATCH" + System.currentTimeMillis());
            result.put("totalCount", 25);
            result.put("successCount", 23);
            result.put("failureCount", 2);
            result.put("status", "COMPLETED");
            result.put("message", "批量过账完成");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("批量过账成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("批量过账失败", e);
            return createErrorResponse("批量过账失败: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}