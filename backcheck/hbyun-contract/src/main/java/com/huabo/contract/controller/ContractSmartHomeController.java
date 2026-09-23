package com.huabo.contract.controller;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.mapper.ContractSmartHomeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 智慧合同首页Controller
 *
 * @author 华博云开发团队
 * @since 2025-06-01
 */
@RestController
@Slf4j
@Tag(name = "智慧合同首页", description = "智慧合同首页接口")
public class ContractSmartHomeController {

    @Autowired
    private UserProvider userProvider;

    @Autowired
    private ContractSmartHomeMapper smartHomeMapper;

    @RequestMapping(value = "/smartHomeData", method = {RequestMethod.GET})
    @Operation(summary = "智慧合同首页聚合数据")
    public @ResponseBody String smartHomeData(HttpServletRequest request,
            @RequestParam(value = "year", required = false) Integer year,
            @RequestParam(value = "month", required = false) Integer month) {
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                return JsonBean.error("用户已失效");
            }
            if (year == null) year = Calendar.getInstance().get(Calendar.YEAR);
            String orgid = staff.getLinkOrg().getOrgid().toString();

            Map<String, Object> resultMap = new HashMap<>();
            Integer totalAmount = smartHomeMapper.getYearMoneyInt(orgid, year, month);
            // 兜底：合同总额为聚合金额，若因冲销/脏数据出现负值，统一按绝对值返回，确保前端展示为正数
            if (totalAmount != null && totalAmount < 0) {
                totalAmount = Math.abs(totalAmount);
            }
            Integer contractCount = smartHomeMapper.getContractCountInt(orgid, year, month);
            Integer breachCount = smartHomeMapper.getBreachCountInt(orgid, year, month);
            Integer pendingCount = smartHomeMapper.getPendingCount(orgid, year);
            Integer executingCount = smartHomeMapper.getExecutingCount(orgid, year);
            Integer receivables = smartHomeMapper.getReceivablesInt(orgid, year, month);
            Integer actualCollection = smartHomeMapper.getActualCollectionInt(orgid, year, month);

            double paymentRate = 0;
            if (receivables != null && receivables > 0 && actualCollection != null) {
                paymentRate = Math.round(actualCollection * 1000.0 / receivables) / 10.0;
            }

            Map<String, Object> kpi = new HashMap<>();
            kpi.put("totalAmount", totalAmount != null ? totalAmount : 0);
            kpi.put("contractCount", contractCount != null ? contractCount : 0);
            kpi.put("pendingCount", pendingCount != null ? pendingCount : 0);
            kpi.put("executingCount", executingCount != null ? executingCount : 0);
            kpi.put("breachCount", breachCount != null ? breachCount : 0);
            kpi.put("paymentRate", paymentRate);
            resultMap.put("kpi", kpi);

            List<Map<String, Object>> contractTypeList = smartHomeMapper.getContractTypeStats(orgid, year, month);
            resultMap.put("contractTypeStats", contractTypeList != null ? contractTypeList : new ArrayList<>());

            Map<String, Object> welcome = new HashMap<>();
            welcome.put("contractCount", contractCount != null ? contractCount : 0);
            welcome.put("totalAmount", totalAmount != null ? totalAmount : 0);
            welcome.put("pendingCount", pendingCount != null ? pendingCount : 0);
            welcome.put("breachCount", breachCount != null ? breachCount : 0);
            resultMap.put("welcome", welcome);
            resultMap.put("codes", "1");

            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            log.error("智慧合同首页数据获取失败", e);
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/smartHome/riskWarning", method = {RequestMethod.GET})
    @Operation(summary = "智慧合同首页-风险预警列表")
    public @ResponseBody String smartHomeRiskWarning(HttpServletRequest request,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();
            List<Map<String, Object>> riskList = smartHomeMapper.getRiskWarningList(orgid, pageSize);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("riskList", riskList != null ? riskList : new ArrayList<>());
            resultMap.put("codes", "1");
            result = new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("智慧合同首页风险预警获取失败", e);
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/smartHome/expiring", method = {RequestMethod.GET})
    @Operation(summary = "智慧合同首页-即将到期合同")
    public @ResponseBody String smartHomeExpiring(HttpServletRequest request,
            @RequestParam(value = "days", required = false, defaultValue = "30") Integer days) {
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();
            List<Map<String, Object>> expiringList = smartHomeMapper.getExpiringList(orgid, days);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("expiringList", expiringList != null ? expiringList : new ArrayList<>());
            resultMap.put("codes", "1");
            result = new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("智慧合同首页即将到期获取失败", e);
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/smartHome/counterpartRisk", method = {RequestMethod.GET})
    @Operation(summary = "智慧合同首页-相对方风险监控")
    public @ResponseBody String smartHomeCounterpartRisk(HttpServletRequest request) {
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();
            List<Map<String, Object>> list = smartHomeMapper.getCounterpartRiskList(orgid);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("counterpartRiskList", list != null ? list : new ArrayList<>());
            resultMap.put("codes", "1");
            result = new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("智慧合同首页相对方风险获取失败", e);
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }

    @RequestMapping(value = "/smartHome/keyContracts", method = {RequestMethod.GET})
    @Operation(summary = "智慧合同首页-重点合同履行进度")
    public @ResponseBody String smartHomeKeyContracts(HttpServletRequest request,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize) {
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) return JsonBean.error("用户已失效");
            String orgid = staff.getLinkOrg().getOrgid().toString();
            List<Map<String, Object>> list = smartHomeMapper.getKeyContractList(orgid, pageSize);
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("keyContractList", list != null ? list : new ArrayList<>());
            resultMap.put("codes", "1");
            result = new JSONObject(resultMap).toString();
        } catch (Exception e) {
            log.error("智慧合同首页重点合同获取失败", e);
            ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return result;
    }
}
