package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.TblFundFlowDispatch;
import com.huabo.cybermonitor.entity.TblFundFlowPenetration;
import com.huabo.cybermonitor.mapper.TblFundFlowDispatchMapper;
import com.huabo.cybermonitor.mapper.TblFundFlowPenetrationMapper;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 资金流向穿透控制器
 * 专用于资金流向穿透页面的所有接口
 */
@Tag(name = "资金流向穿透", description = "资金流向穿透页面接口(列表/统计/详情/派单)")
@RestController
@RequestMapping("/v1/supervision/financial/fund-flow-penetration")
@Slf4j
public class FundFlowPenetrationController {

    @Autowired
    private TblFundFlowPenetrationMapper flowMapper;

    @Autowired
    private TblFundFlowDispatchMapper dispatchMapper;

    @Autowired
    private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    @Operation(summary = "资金流向穿透列表")
    @GetMapping("/list")
    public R<List<TblFundFlowPenetration>> list(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String flowType,
            @RequestParam(required = false) String fundNature,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount) {
        try {
            LambdaQueryWrapper<TblFundFlowPenetration> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyName)) {
                w.and(ww -> ww.like(TblFundFlowPenetration::getFromCompany, companyName)
                        .or().like(TblFundFlowPenetration::getToCompany, companyName));
            }
            if (StringUtils.isNotBlank(flowType)) {
                w.eq(TblFundFlowPenetration::getFlowType, flowType);
            }
            if (StringUtils.isNotBlank(fundNature)) {
                w.eq(TblFundFlowPenetration::getFundNature, fundNature);
            }
            if (StringUtils.isNotBlank(riskLevel)) {
                w.eq(TblFundFlowPenetration::getRiskLevel, riskLevel);
            }
            if (StringUtils.isNotBlank(startDate)) {
                w.ge(TblFundFlowPenetration::getOccurTime, startDate);
            }
            if (StringUtils.isNotBlank(endDate)) {
                w.le(TblFundFlowPenetration::getOccurTime, endDate);
            }
            if (minAmount != null) {
                w.ge(TblFundFlowPenetration::getAmount, minAmount);
            }
            if (maxAmount != null) {
                w.le(TblFundFlowPenetration::getAmount, maxAmount);
            }
            w.orderByDesc(TblFundFlowPenetration::getOccurTime);
            List<TblFundFlowPenetration> list = flowMapper.selectList(w);
            return R.success(list);
        } catch (Exception e) {
            log.error("查询资金流向穿透列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资金流向穿透统计")
    @GetMapping("/stats")
    public R<Map<String, Object>> stats(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<TblFundFlowPenetration> baseWrapper = new LambdaQueryWrapper<>();
            if (org.apache.commons.lang.StringUtils.isNotBlank(companyId)) {
                String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
                if (orgPattern != null) baseWrapper.and(w -> w.like(TblFundFlowPenetration::getOrgPath, orgPattern).or(sub -> sub.isNull(TblFundFlowPenetration::getOrgPath).eq(TblFundFlowPenetration::getFromCompany, companyId)));
            }
            long total = flowMapper.selectCount(baseWrapper);

            // 异常流向：RISK_LEVEL 不为 LOW 的记录
            LambdaQueryWrapper<TblFundFlowPenetration> abnormalWrapper = new LambdaQueryWrapper<>();
            if (org.apache.commons.lang.StringUtils.isNotBlank(companyId)) {
                String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
                if (orgPattern != null) abnormalWrapper.and(w -> w.like(TblFundFlowPenetration::getOrgPath, orgPattern).or(sub -> sub.isNull(TblFundFlowPenetration::getOrgPath).eq(TblFundFlowPenetration::getFromCompany, companyId)));
            }
            abnormalWrapper.ne(TblFundFlowPenetration::getRiskLevel, "LOW");
            long abnormal = flowMapper.selectCount(abnormalWrapper);

            // 高风险流向：RISK_LEVEL = HIGH
            LambdaQueryWrapper<TblFundFlowPenetration> hiddenWrapper = new LambdaQueryWrapper<>();
            if (org.apache.commons.lang.StringUtils.isNotBlank(companyId)) {
                String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
                if (orgPattern != null) hiddenWrapper.and(w -> w.like(TblFundFlowPenetration::getOrgPath, orgPattern).or(sub -> sub.isNull(TblFundFlowPenetration::getOrgPath).eq(TblFundFlowPenetration::getFromCompany, companyId)));
            }
            hiddenWrapper.eq(TblFundFlowPenetration::getRiskLevel, "HIGH");
            long hidden = flowMapper.selectCount(hiddenWrapper);

            // 异常金额合计
            LambdaQueryWrapper<TblFundFlowPenetration> abnormalListWrapper = new LambdaQueryWrapper<>();
            if (org.apache.commons.lang.StringUtils.isNotBlank(companyId)) {
                String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
                if (orgPattern != null) abnormalListWrapper.and(w -> w.like(TblFundFlowPenetration::getOrgPath, orgPattern).or(sub -> sub.isNull(TblFundFlowPenetration::getOrgPath).eq(TblFundFlowPenetration::getFromCompany, companyId)));
            }
            abnormalListWrapper.ne(TblFundFlowPenetration::getRiskLevel, "LOW");
            List<TblFundFlowPenetration> abnormalList = flowMapper.selectList(abnormalListWrapper);
            BigDecimal abnormalAmount = abnormalList.stream()
                    .map(TblFundFlowPenetration::getAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("abnormal", abnormal);
            result.put("hidden", hidden);
            result.put("abnormalAmount", abnormalAmount);
            // 前端 STAT_MAP 期望字段
            result.put("totalFlows", total);
            result.put("flowCount", total);
            result.put("abnormalCount", abnormal);
            result.put("abnormalFlows", abnormal);
            result.put("riskCount", abnormal + hidden);
            result.put("warningCount", abnormal + hidden);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询资金流向统计失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资金流向详情")
    @GetMapping("/detail/{id}")
    public R<TblFundFlowPenetration> detail(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            TblFundFlowPenetration flow = flowMapper.selectById(id);
            if (flow == null) return R.fail("记录不存在");
            return R.success(flow);
        } catch (Exception e) {
            log.error("查询资金流向详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "异常资金核查派单")
    @PostMapping("/dispatch")
    public R<Boolean> dispatch(@RequestBody Map<String, Object> params) {
        try {
            String flowId = params.get("flowId") != null ? params.get("flowId").toString() : null;
            String flowNo = params.get("flowNo") != null ? params.get("flowNo").toString() : null;
            String anomalyType = params.get("anomalyType") != null ? params.get("anomalyType").toString() : "";
            String owner = params.get("owner") != null ? params.get("owner").toString() : null;
            String deadline = params.get("deadline") != null ? params.get("deadline").toString() : null;
            String requirement = params.get("requirement") != null ? params.get("requirement").toString() : "";

            if (StringUtils.isBlank(owner)) return R.fail("核查责任人不能为空");
            if (StringUtils.isBlank(deadline)) return R.fail("核查期限不能为空");

            TblFundFlowDispatch dispatch = new TblFundFlowDispatch();
            dispatch.setFlowId(flowId);
            dispatch.setFlowNo(flowNo);
            dispatch.setAnomalyType(anomalyType);
            dispatch.setOwner(owner);
            dispatch.setDeadline(deadline);
            dispatch.setRequirement(requirement);
            dispatch.setStatus("待处理");
            dispatch.setCreateTime(LocalDateTime.now());
            dispatch.setUpdateTime(LocalDateTime.now());
            dispatchMapper.insert(dispatch);
            return R.success(true);
        } catch (Exception e) {
            log.error("派单失败", e);
            return R.fail("派单失败：" + e.getMessage());
        }
    }

    @Operation(summary = "派单记录列表")
    @GetMapping("/dispatch/list")
    public R<List<TblFundFlowDispatch>> dispatchList(
            @RequestParam(required = false) String status) {
        try {
            LambdaQueryWrapper<TblFundFlowDispatch> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(status)) {
                w.eq(TblFundFlowDispatch::getStatus, status);
            }
            w.orderByDesc(TblFundFlowDispatch::getCreateTime);
            List<TblFundFlowDispatch> list = dispatchMapper.selectList(w);
            return R.success(list);
        } catch (Exception e) {
            log.error("查询派单列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }
}
