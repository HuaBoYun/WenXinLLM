package com.huabo.fxgl.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.cjbdi.CjbdiQueryParam;
import com.huabo.fxgl.entity.YyReportModel;
import com.huabo.fxgl.entity.YyXdfCompany;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.cjbdi.*;
import com.huabo.fxgl.mapper.YyReportModelMapper;
import com.huabo.fxgl.mapper.YyXdfCompanyMapper;
import com.huabo.fxgl.mapper.StaffMapper;
import com.huabo.fxgl.mapper.cjbdi.*;
import com.huabo.fxgl.service.cjbdi.CjbdiMonitorService;
import com.huabo.fxgl.service.cjbdi.CjbdiQueryService;
import com.huabo.fxgl.service.cjbdi.CjbdiVerificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * CJBDI 外部数据查询控制器
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name = "CJBDI外部数据查询", description = "中船投资企业信息查询平台对接")
@Slf4j
public class CjbdiQueryController {

    @Resource
    private CjbdiQueryService cjbdiQueryService;
    @Resource
    private CjbdiVerificationService verificationService;
    @Resource
    private CjbdiMonitorService monitorService;
    @Resource
    private UserProvider userProvider;
    @Resource
    private CjbdiBusinessInfoMapper businessInfoMapper;
    @Resource
    private CjbdiAdminPenaltyMapper adminPenaltyMapper;
    @Resource
    private CjbdiLitigationMapper litigationMapper;
    @Resource
    private CjbdiDishonestyMapper dishonestyMapper;
    @Resource
    private CjbdiRiskDataMapper riskDataMapper;
    @Resource
    private CjbdiInvestmentMapper investmentMapper;
    @Resource
    private CjbdiBiddingMapper biddingMapper;
    @Resource
    private CjbdiPublicOpinionMapper publicOpinionMapper;
    @Resource
    private YyXdfCompanyMapper yyXdfCompanyMapper;
    @Resource
    private YyReportModelMapper yyReportModelMapper;
    @Resource
    private StaffMapper staffMapper;

    /**
     * 获取数据类别列表（按分组）
     */
    @RequestMapping(value = "/cjbdi/categories")
    @Operation(summary = "获取CJBDI数据类别列表")
    public JsonBean getCategories(
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            Map<String, List<CjbdiDataCategory>> grouped = cjbdiQueryService.getCategoriesByGroup();
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(grouped);
            return jsonBean;
        } catch (Exception e) {
            log.error("获取数据类别失败", e);
            return new JsonBean(500, e.getMessage(), null);
        }
    }

    /**
     * 执行外部数据查询（核心接口）
     * 使用 DeferredResult 异步处理：HTTP 线程立即释放，业务线程在后台执行，
     * 彻底避免 Jetty 连接超时导致前端报错而后端仍在处理的问题
     */
    @RequestMapping(value = "/cjbdi/query")
    @Operation(summary = "执行CJBDI外部数据查询")
    public DeferredResult<JsonBean> queryExternalData(
            @RequestBody CjbdiQueryParam param,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {

        // DeferredResult 超时时间 170 秒（略大于 cjbdiRestTemplate.readTimeout=150s）
        DeferredResult<JsonBean> deferredResult = new DeferredResult<>(170000L,
                new JsonBean(504, "查询超时，外部接口响应过慢，请稍后重试", null));

        try {
            TblStaffUtil staffUtil = userProvider.get();
            TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg();

            if (param.getCompanyName() == null || param.getCompanyName().isEmpty()) {
                deferredResult.setResult(new JsonBean(400, "企业名称不能为空", null));
                return deferredResult;
            }
            if (param.getCategoryIds() == null || param.getCategoryIds().isEmpty()) {
                deferredResult.setResult(new JsonBean(400, "请选择至少一个数据类别", null));
                return deferredResult;
            }

            final Long staffId = staffUtil.getStaffid().longValue();
            final Long orgId = selectOrg.getOrgid().longValue();

            // 在独立线程中执行，不阻塞 Jetty HTTP 线程
            new Thread(() -> {
                try {
                    List<CjbdiQueryResult> results = cjbdiQueryService.queryMultipleCategories(
                            param.getCompanyId(),
                            param.getCompanyName(),
                            param.getCreditCode(),
                            param.getCategoryIds(),
                            staffId,
                            orgId);
                    JsonBean jsonBean = new JsonBean();
                    jsonBean.setCode(200);
                    jsonBean.setMsg("查询完成");
                    jsonBean.setData(results);
                    deferredResult.setResult(jsonBean);
                } catch (Exception e) {
                    log.error("CJBDI异步查询失败", e);
                    deferredResult.setResult(new JsonBean(500, "查询失败: " + e.getMessage(), null));
                }
            }, "cjbdi-query-" + System.currentTimeMillis()).start();

        } catch (Exception e) {
            log.error("CJBDI查询参数校验失败", e);
            deferredResult.setResult(new JsonBean(500, "查询失败: " + e.getMessage(), null));
        }

        return deferredResult;
    }

    /**
     * 获取企业某类别的详细数据
     */
    @RequestMapping(value = "/cjbdi/detail")
    @Operation(summary = "获取企业外部数据详情")
    public JsonBean getDetail(
            @Parameter(name = "companyId") @RequestParam Long companyId,
            @Parameter(name = "categoryId") @RequestParam Long categoryId,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();

            // 获取最近一次查询结果
            CjbdiQueryResult latestResult = cjbdiQueryService.getLatestResult(companyId, categoryId);
            if (latestResult == null) {
                JsonBean jsonBean = new JsonBean();
                jsonBean.setCode(200);
                jsonBean.setMsg("暂无数据");
                jsonBean.setData(null);
                return jsonBean;
            }

            // 根据类别查询明细数据
            Map<String, Object> detail = new HashMap<>();
            detail.put("queryResult", latestResult);
            detail.put("items", getDetailItems(companyId, categoryId, latestResult.getResultId()));

            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(detail);
            return jsonBean;
        } catch (Exception e) {
            log.error("获取详情失败", e);
            return new JsonBean(500, e.getMessage(), null);
        }
    }

    /**
     * 获取企业查询历史
     */
    @RequestMapping(value = "/cjbdi/history")
    @Operation(summary = "获取企业查询历史")
    public JsonBean getHistory(
            @Parameter(name = "companyId") @RequestParam Long companyId,
            @Parameter(name = "categoryId") @RequestParam(required = false) Long categoryId,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            List<CjbdiQueryResult> history;
            if (categoryId != null) {
                history = cjbdiQueryService.getQueryHistory(companyId, categoryId);
            } else {
                history = cjbdiQueryService.getQueryHistory(companyId, null);
            }
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(history);
            return jsonBean;
        } catch (Exception e) {
            log.error("获取历史失败", e);
            return new JsonBean(500, e.getMessage(), null);
        }
    }

    /**
     * 企业名录搜索（调用CJBDI 02接口）
     * 返回企业列表供前端展示
     */
    @RequestMapping(value = "/cjbdi/search")
    @Operation(summary = "企业名录搜索")
    public JsonBean searchEnterprise(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            String keyword = (String) params.get("keyword");
            if (keyword == null || keyword.trim().isEmpty()) {
                return new JsonBean(400, "搜索关键词不能为空", null);
            }
            List<Map<String, Object>> enterprises = cjbdiQueryService.searchEnterprise(keyword);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setMsg("查询成功");
            jsonBean.setData(enterprises);
            return jsonBean;
        } catch (Exception e) {
            log.error("企业搜索失败", e);
            return new JsonBean(500, "搜索失败: " + e.getMessage(), null);
        }
    }

    /**
     * 查询单个企业工商基础信息（调用CJBDI 03接口）
     * 前端点击"工商数据"按钮时调用
     */
    @RequestMapping(value = "/cjbdi/businessInfo")
    @Operation(summary = "查询企业工商基础信息")
    public JsonBean queryBusinessInfo(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            String companyName = (String) params.get("companyName");
            // creditCode 可能由前端以数字类型传入，用 String.valueOf 安全转换，避免 ClassCastException
            Object creditCodeRaw = params.get("creditCode");
            String creditCode = (creditCodeRaw != null) ? String.valueOf(creditCodeRaw) : null;
            if ((companyName == null || companyName.trim().isEmpty())
                    && (creditCode == null || creditCode.trim().isEmpty())) {
                return new JsonBean(400, "企业名称或统一社会信用代码不能为空", null);
            }
            Map<String, Object> businessInfo = cjbdiQueryService.queryBusinessInfo(companyName, creditCode);
            if (businessInfo == null) {
                return new JsonBean(500, "未查询到工商信息", null);
            }
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setMsg("查询成功");
            jsonBean.setData(businessInfo);
            return jsonBean;
        } catch (Exception e) {
            log.error("查询工商信息失败", e);
            return new JsonBean(500, "查询失败: " + e.getMessage(), null);
        }
    }

    // ========== 接口01: 身份验证 ==========

    /**
     * 身份证两要素校验
     */
    @RequestMapping(value = "/cjbdi/verification")
    @Operation(summary = "身份证两要素校验")
    public JsonBean verifyIdentity(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            String name = (String) params.get("name");
            String idCard = (String) params.get("idCard");
            if (name == null || name.trim().isEmpty()) {
                return new JsonBean(400, "姓名不能为空", null);
            }
            if (idCard == null || idCard.trim().isEmpty()) {
                return new JsonBean(400, "身份证号不能为空", null);
            }
            Map<String, Object> result = verificationService.verifyIdentity(name, idCard);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("身份验证失败", e);
            return new JsonBean(500, "身份验证失败: " + e.getMessage(), null);
        }
    }

    /**
     * 批量身份验证
     */
    @RequestMapping(value = "/cjbdi/verification/batch")
    @Operation(summary = "批量身份证两要素校验")
    public JsonBean batchVerifyIdentity(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            @SuppressWarnings("unchecked")
            List<Map<String, String>> verifyList = (List<Map<String, String>>) params.get("list");
            if (verifyList == null || verifyList.isEmpty()) {
                return new JsonBean(400, "验证列表不能为空", null);
            }
            List<Map<String, Object>> results = verificationService.batchVerifyIdentity(verifyList);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(results);
            return jsonBean;
        } catch (Exception e) {
            log.error("批量身份验证失败", e);
            return new JsonBean(500, "批量身份验证失败: " + e.getMessage(), null);
        }
    }

    /**
     * 不良记录查询（接口23）
     * 查询个人不良记录，需要姓名和身份证号
     */
    @RequestMapping(value = "/cjbdi/badRecord")
    @Operation(summary = "个人不良记录查询")
    public JsonBean queryBadRecord(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            String name   = (String) params.get("name");
            String idCard = (String) params.get("idCard");
            if (name == null || name.trim().isEmpty()) {
                return new JsonBean(400, "姓名不能为空", null);
            }
            if (idCard == null || idCard.trim().isEmpty()) {
                return new JsonBean(400, "身份证号不能为空", null);
            }
            Map<String, Object> result = verificationService.queryBadRecord(name, idCard);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("不良记录查询失败", e);
            return new JsonBean(500, "不良记录查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 企业纠纷单次查询（接口24）
     * 允许前端指定 company 和 ajlx，直接查询一种案件类型，不循环所有类型
     * 用于身份验证页面的企业纠纷查询卡片
     */
    @RequestMapping(value = "/cjbdi/dispute/query")
    @Operation(summary = "企业纠纷查询")
    public JsonBean queryDisputeCase(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            String company = (String) params.get("company");
            String ajlx    = (String) params.get("ajlx");
            if (ajlx == null || ajlx.trim().isEmpty()) {
                return new JsonBean(400, "案件类型(ajlx)不能为空", null);
            }
            Map<String, Object> result = verificationService.queryDisputeCase(company, ajlx);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("企业纠纷查询失败", e);
            return new JsonBean(500, "企业纠纷查询失败: " + e.getMessage(), null);
        }
    }

    // ========== 接口04-07: 企业监控名单管理 ==========

    /**
     * 04-添加企业到监控名单
     */
    @RequestMapping(value = "/cjbdi/monitor/add")
    @Operation(summary = "添加企业到监控名单")
    public JsonBean addMonitorCompanies(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            @SuppressWarnings("unchecked")
            List<String> companyNames = (List<String>) params.get("companyNames");
            if (companyNames == null || companyNames.isEmpty()) {
                return new JsonBean(400, "企业名称列表不能为空", null);
            }
            Map<String, Object> result = monitorService.addMonitorCompanies(companyNames);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("添加监控名单失败", e);
            return new JsonBean(500, "添加监控名单失败: " + e.getMessage(), null);
        }
    }

    /**
     * 05-查询监控名单
     */
    @RequestMapping(value = "/cjbdi/monitor/query")
    @Operation(summary = "查询监控名单")
    public JsonBean queryMonitorList(
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "1000") int pageSize,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            Map<String, Object> result = monitorService.queryMonitorList(pageIndex, pageSize);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("查询监控名单失败", e);
            return new JsonBean(500, "查询监控名单失败: " + e.getMessage(), null);
        }
    }

    /**
     * 06-从监控名单中删除企业
     */
    @RequestMapping(value = "/cjbdi/monitor/delete")
    @Operation(summary = "从监控名单中删除企业")
    public JsonBean deleteMonitorCompanies(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            @SuppressWarnings("unchecked")
            List<String> companyNames = (List<String>) params.get("companyNames");
            if (companyNames == null || companyNames.isEmpty()) {
                return new JsonBean(400, "企业名称列表不能为空", null);
            }
            Map<String, Object> result = monitorService.deleteMonitorCompanies(companyNames);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("删除监控名单失败", e);
            return new JsonBean(500, "删除监控名单失败: " + e.getMessage(), null);
        }
    }

    /**
     * 07-获取监控信息（通过日期获取监控案件变动）
     */
    @RequestMapping(value = "/cjbdi/monitor/cases")
    @Operation(summary = "获取企业监控信息")
    public JsonBean getMonitorCases(
            @RequestParam String queryDate,
            @RequestParam(defaultValue = "1") int pageIndex,
            @RequestParam(defaultValue = "1000") int pageSize,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            userProvider.get();
            if (queryDate == null || queryDate.trim().isEmpty()) {
                return new JsonBean(400, "查询日期不能为空", null);
            }
            Map<String, Object> result = monitorService.getMonitorCases(queryDate, pageIndex, pageSize);
            JsonBean jsonBean = new JsonBean();
            jsonBean.setCode(200);
            jsonBean.setData(result);
            return jsonBean;
        } catch (Exception e) {
            log.error("获取监控信息失败", e);
            return new JsonBean(500, "获取监控信息失败: " + e.getMessage(), null);
        }
    }

    /**
     * 根据类别获取明细数据（支持companyId或resultId查询）
     */
    private Object getDetailItems(Long companyId, Long categoryId, Long resultId) {
        CjbdiDataCategory cat = cjbdiQueryService.getEnabledCategories().stream()
                .filter(c -> c.getCategoryId().equals(categoryId))
                .findFirst().orElse(null);
        if (cat == null) return Collections.emptyList();

        String apiCode = cat.getApiCode();
        // 当companyId为null时（telescope场景），使用resultId查询
        boolean useResultId = (companyId == null);
        switch (apiCode) {
            case "03":
                LambdaQueryWrapper<CjbdiBusinessInfo> biW = new LambdaQueryWrapper<>();
                if (useResultId) biW.eq(CjbdiBusinessInfo::getResultId, resultId);
                else biW.eq(CjbdiBusinessInfo::getCompanyId, companyId);
                return businessInfoMapper.selectList(biW.orderByDesc(CjbdiBusinessInfo::getCreateTime));
            case "09":
                LambdaQueryWrapper<CjbdiInvestment> invW = new LambdaQueryWrapper<>();
                if (useResultId) invW.eq(CjbdiInvestment::getResultId, resultId);
                else invW.eq(CjbdiInvestment::getCompanyId, companyId);
                return investmentMapper.selectList(invW.orderByDesc(CjbdiInvestment::getCreateTime));
            case "10":
                LambdaQueryWrapper<CjbdiBidding> bidW = new LambdaQueryWrapper<>();
                if (useResultId) bidW.eq(CjbdiBidding::getResultId, resultId);
                else bidW.eq(CjbdiBidding::getCompanyId, companyId);
                return biddingMapper.selectList(bidW.orderByDesc(CjbdiBidding::getCreateTime));
            case "11":
                LambdaQueryWrapper<CjbdiPublicOpinion> poW = new LambdaQueryWrapper<>();
                if (useResultId) poW.eq(CjbdiPublicOpinion::getResultId, resultId);
                else poW.eq(CjbdiPublicOpinion::getCompanyId, companyId);
                return publicOpinionMapper.selectList(poW.orderByDesc(CjbdiPublicOpinion::getCreateTime));
            case "12":
                LambdaQueryWrapper<CjbdiAdminPenalty> apW = new LambdaQueryWrapper<>();
                if (useResultId) apW.eq(CjbdiAdminPenalty::getResultId, resultId);
                else apW.eq(CjbdiAdminPenalty::getCompanyId, companyId);
                return adminPenaltyMapper.selectList(apW.orderByDesc(CjbdiAdminPenalty::getCreateTime));
            case "20":
                LambdaQueryWrapper<CjbdiLitigation> litW = new LambdaQueryWrapper<>();
                if (useResultId) litW.eq(CjbdiLitigation::getResultId, resultId);
                else litW.eq(CjbdiLitigation::getCompanyId, companyId);
                return litigationMapper.selectList(litW.orderByDesc(CjbdiLitigation::getCreateTime));
            case "21":
                LambdaQueryWrapper<CjbdiDishonesty> disW = new LambdaQueryWrapper<>();
                if (useResultId) disW.eq(CjbdiDishonesty::getResultId, resultId);
                else disW.eq(CjbdiDishonesty::getCompanyId, companyId);
                return dishonestyMapper.selectList(disW.orderByDesc(CjbdiDishonesty::getCreateTime));
            default:
                LambdaQueryWrapper<CjbdiRiskData> rdW = new LambdaQueryWrapper<>();
                if (useResultId) rdW.eq(CjbdiRiskData::getResultId, resultId);
                else rdW.eq(CjbdiRiskData::getCompanyId, companyId);
                rdW.eq(CjbdiRiskData::getCategoryId, categoryId);
                return riskDataMapper.selectList(rdW.orderByDesc(CjbdiRiskData::getCreateTime));
        }
    }

    /**
     * 保存企业监控信息
     * @param companyData 企业监控数据
     * @param token 用户token
     * @return 保存结果
     */
    @RequestMapping(value = "/cjbdi/monitor/saveCompany", method = RequestMethod.POST)
    @Operation(summary = "保存企业监控信息")
    public JsonBean saveCompanyMonitor(
            @RequestBody Map<String, Object> companyData,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return new JsonBean(401, "用户未登录或登录已失效", null);
            }

            // 获取企业信息
            String companyname = (String) companyData.get("companyname");
            String creditCode = (String) companyData.get("creditCode");
            String legalPerson = (String) companyData.get("legalPerson");
            String entStatus = (String) companyData.get("entStatus");
            String regCap = (String) companyData.get("regCap");
            String establishDate = (String) companyData.get("establishDate");
            String regOrg = (String) companyData.get("regOrg");
            String businessScope = (String) companyData.get("businessScope");
            
            // 获取监控配置
            Object teamidObj = companyData.get("teamid");
            String fxtype = (String) companyData.get("fxtype");
            String priceid = (String) companyData.get("priceid");
            String pageid = (String) companyData.get("pageid");
            
            // 参数校验
            if (companyname == null || companyname.isEmpty()) {
                return new JsonBean(400, "企业名称不能为空", null);
            }
            if (teamidObj == null) {
                return new JsonBean(400, "请选择所属分组", null);
            }
            if (fxtype == null || fxtype.isEmpty()) {
                return new JsonBean(400, "请选择风险状况", null);
            }

            // 使用统一社会信用代码作为COMPANYID，如果没有则使用企业名称
            String companyId = (creditCode != null && !creditCode.isEmpty()) ? creditCode : companyname;

            // 检查企业是否已存在（只检查正常状态的记录 cstatus=1）
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<YyXdfCompany> existQueryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            existQueryWrapper.eq(YyXdfCompany::getCompanyid, companyId)
                           .eq(YyXdfCompany::getCstatus, new BigDecimal(1));
            YyXdfCompany existCompany = yyXdfCompanyMapper.selectOne(existQueryWrapper);

            if (existCompany != null) {
                return new JsonBean(400, "该企业已添加到监控列表", null);
            }

            // 检查是否有已删除的记录（cstatus=0），如果有则更新它
            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<YyXdfCompany> deletedQueryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            deletedQueryWrapper.eq(YyXdfCompany::getCompanyid, companyId)
                            .eq(YyXdfCompany::getCstatus, new BigDecimal(0));
            YyXdfCompany deletedCompany = yyXdfCompanyMapper.selectOne(deletedQueryWrapper);

            boolean isUpdate = (deletedCompany != null);

            // 创建报告模型
            YyReportModel reportModel = new YyReportModel();
            reportModel.setReportname(companyname);
            reportModel.setOrgid(staffUtil.getCurrentOrg().getOrgid());
            reportModel.setStaffid(staffUtil.getStaffid());
            reportModel.setPriceid(priceid);
            yyReportModelMapper.insert(reportModel);

            // 企业监控记录
            YyXdfCompany company;
            if (isUpdate) {
                // 更新已删除的记录
                company = deletedCompany;
                company.setTeamid(new BigDecimal(teamidObj.toString()));
                company.setOrgid(staffUtil.getCurrentOrg().getOrgid());
                company.setStaffid(staffUtil.getStaffid());
                company.setReportid(reportModel.getReportid());
                company.setFxtype(fxtype);
                company.setPriceid(priceid);
                company.setPageid(pageid);
                company.setUpdatetime(new Date());
                company.setCstatus(new BigDecimal(1)); // 恢复状态为1（正常）

                // 更新企业详细信息
                if (creditCode != null && !creditCode.isEmpty()) {
                    company.setCreditCode(creditCode);
                }
                if (legalPerson != null && !legalPerson.isEmpty()) {
                    company.setLegalPerson(legalPerson);
                }
                if (entStatus != null && !entStatus.isEmpty()) {
                    company.setEntStatus(entStatus);
                }
                if (regCap != null && !regCap.isEmpty()) {
                    company.setRegCap(regCap);
                }
                if (establishDate != null && !establishDate.isEmpty()) {
                    company.setEstablishDate(establishDate);
                }
                if (regOrg != null && !regOrg.isEmpty()) {
                    company.setRegOrg(regOrg);
                }
                if (businessScope != null && !businessScope.isEmpty()) {
                    company.setBusinessScope(businessScope);
                }

                // 更新到数据库
                yyXdfCompanyMapper.updateById(company);
                log.info("更新企业监控（恢复删除记录）：{}，信用代码：{}", companyname, creditCode);
            } else {
                // 插入新记录
                company = new YyXdfCompany();
                company.setCompanyid(companyId); // 使用统一社会信用代码
                company.setCompanyname(companyname);
                company.setTeamid(new BigDecimal(teamidObj.toString()));
                company.setOrgid(staffUtil.getCurrentOrg().getOrgid());
                company.setStaffid(staffUtil.getStaffid());
                company.setReportid(reportModel.getReportid());
                company.setFxtype(fxtype);
                company.setPriceid(priceid);
                company.setPageid(pageid);
                company.setCreatedate(new Date());
                company.setUpdatetime(new Date());
                company.setCstatus(new BigDecimal(1)); // 默认状态为1（正常）

                // 保存企业详细信息
                if (creditCode != null && !creditCode.isEmpty()) {
                    company.setCreditCode(creditCode);
                }
                if (legalPerson != null && !legalPerson.isEmpty()) {
                    company.setLegalPerson(legalPerson);
                }
                if (entStatus != null && !entStatus.isEmpty()) {
                    company.setEntStatus(entStatus);
                }
                if (regCap != null && !regCap.isEmpty()) {
                    company.setRegCap(regCap);
                }
                if (establishDate != null && !establishDate.isEmpty()) {
                    company.setEstablishDate(establishDate);
                }
                if (regOrg != null && !regOrg.isEmpty()) {
                    company.setRegOrg(regOrg);
                }
                if (businessScope != null && !businessScope.isEmpty()) {
                    company.setBusinessScope(businessScope);
                }

                // 插入到数据库
                yyXdfCompanyMapper.insert(company);
                log.info("成功添加企业监控：{}，信用代码：{}", companyname, creditCode);
            }
            
            Map<String, Object> result = new HashMap<>();
            result.put("companyid", companyId);
            result.put("companyname", companyname);
            result.put("reportid", reportModel.getReportid());
            
            return new JsonBean(200, "添加成功", result);
        } catch (Exception e) {
            log.error("保存企业监控信息失败", e);
            return new JsonBean(500, "保存失败：" + e.getMessage(), null);
        }
    }

    /**
     * 查询企业监控列表（从新表TBL_YY_CJBDI_COMPANY_MONITOR）
     * @param pageIndex 页码
     * @param pageSize 每页数量
     * @param teamid 企业分组ID（可选，用于分组筛选）
     * @param token 用户token
     * @return 监控列表
     */
    @RequestMapping(value = "/cjbdi/monitor/queryCompanyList", method = RequestMethod.GET)
    @Operation(summary = "查询企业监控列表")
    public JsonBean queryCompanyMonitorList(
            @Parameter(name = "pageIndex") @RequestParam(defaultValue = "1") Integer pageIndex,
            @Parameter(name = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(name = "teamid") @RequestParam(required = false) String teamid,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return new JsonBean(401, "用户未登录或登录已失效", null);
            }

            // 使用MyBatis-Plus分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<YyXdfCompany> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageIndex, pageSize);

            com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<YyXdfCompany> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
            queryWrapper.eq(YyXdfCompany::getCstatus, new BigDecimal(1)); // 只查询正常状态

            // 如果有分组筛选，按teamid精确查询
            if (teamid != null && !teamid.isEmpty()) {
                queryWrapper.eq(YyXdfCompany::getTeamid, new BigDecimal(teamid));
            }

            queryWrapper.orderByDesc(YyXdfCompany::getCreatedate); // 按创建时间倒序

            com.baomidou.mybatisplus.core.metadata.IPage<YyXdfCompany> resultPage =
                yyXdfCompanyMapper.selectPage(page, queryWrapper);

            // 批量查询 staff 信息（避免 N+1 查询）：先收集所有 staffid，再一次性查回
            Map<String, String> staffNameMap = new HashMap<>();
            List<BigDecimal> staffIds = resultPage.getRecords().stream()
                    .map(YyXdfCompany::getStaffid)
                    .filter(Objects::nonNull)
                    .distinct()
                    .collect(java.util.stream.Collectors.toList());
            if (!staffIds.isEmpty()) {
                try {
                    List<Staff> staffList = staffMapper.selectList(
                            new LambdaQueryWrapper<Staff>().in(Staff::getStaffid, staffIds)
                    );
                    for (Staff s : staffList) {
                        if (s.getStaffid() != null) {
                            staffNameMap.put(s.getStaffid().toPlainString(), s.getRealname());
                        }
                    }
                } catch (Exception ex) {
                    log.warn("批量查询 staff 名称失败，将退化为空", ex);
                }
            }

            // 转换为前端需要的格式
            List<Map<String, Object>> monitorList = new ArrayList<>();
            for (YyXdfCompany company : resultPage.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("companyid", company.getCompanyid());
                // 同时提供两种字段名格式，兼容前端
                item.put("companyname", company.getCompanyname()); // 小写
                item.put("companyName", company.getCompanyname()); // 驼峰
                item.put("creditCode", company.getCreditCode());
                item.put("legalPerson", company.getLegalPerson());
                item.put("entStatus", company.getEntStatus() != null ? company.getEntStatus() : "存续"); // 如果为空，默认为存续
                item.put("regCap", company.getRegCap());
                item.put("establishDate", company.getEstablishDate());
                item.put("regOrg", company.getRegOrg());
                item.put("fxtype", company.getFxtype());
                item.put("priceid", company.getPriceid());
                item.put("pageid", company.getPageid());
                item.put("teamid", company.getTeamid());
                item.put("reportid", company.getReportid());
                item.put("status", company.getCstatus() != null ? company.getCstatus().toString() : "1");
                // 同时提供两种时间字段名格式，兼容前端
                item.put("createdate", company.getCreatedate()); // 小写
                item.put("addTime", company.getCreatedate()); // 驼峰
                item.put("updateTime", company.getUpdatetime());

                // 操作人信息：staffid + staff.realname + staffName（多种字段名兼容前端列定义）
                BigDecimal sid = company.getStaffid();
                String staffIdStr = sid != null ? sid.toPlainString() : null;
                String realname = staffIdStr != null ? staffNameMap.get(staffIdStr) : null;
                item.put("staffid", staffIdStr);
                item.put("staffName", realname); // 扁平字段
                Map<String, Object> staffObj = new HashMap<>();
                staffObj.put("staffid", staffIdStr);
                staffObj.put("realname", realname);
                item.put("staff", staffObj); // 嵌套对象，兼容 prop="staff.realname"

                // 添加list字段用于formatTag显示
                List<Map<String, String>> list = new ArrayList<>();
                if (company.getPriceid() != null && !company.getPriceid().isEmpty()) {
                    String[] priceids = company.getPriceid().split(",");
                    for (String priceid : priceids) {
                        Map<String, String> tagItem = new HashMap<>();
                        tagItem.put("priceid", priceid.trim());
                        tagItem.put("interfacename", priceid.trim()); // 简化显示
                        list.add(tagItem);
                    }
                }
                item.put("list", list);

                monitorList.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("list", monitorList);
            result.put("totalRecord", resultPage.getTotal()); // 使用totalRecord匹配前端
            result.put("pageIndex", pageIndex);
            result.put("pageSize", pageSize);

            return new JsonBean(200, "查询成功", result);
        } catch (Exception e) {
            log.error("查询企业监控列表失败", e);
            return new JsonBean(500, "查询失败：" + e.getMessage(), null);
        }
    }

    /**
     * 从企业监控列表中删除企业（操作本地数据库表TBL_YY_CJBDI_COMPANY_MONITOR）
     * @param companyNames 企业名称列表
     * @param token 用户token
     * @return 删除结果
     */
    @RequestMapping(value = "/cjbdi/monitor/deleteCompany", method = RequestMethod.POST)
    @Operation(summary = "从企业监控列表中删除企业")
    public JsonBean deleteCompanyMonitor(
            @RequestBody Map<String, Object> params,
            @Parameter(name = "token", required = true) @RequestHeader("token") String token) {
        try {
            TblStaffUtil staffUtil = userProvider.get();
            if (staffUtil == null) {
                return new JsonBean(401, "用户未登录或登录已失效", null);
            }

            @SuppressWarnings("unchecked")
            List<String> companyNames = (List<String>) params.get("companyNames");
            if (companyNames == null || companyNames.isEmpty()) {
                return new JsonBean(400, "企业名称列表不能为空", null);
            }

            // 删除企业监控记录（软删除：将cstatus设置为0）
            com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<YyXdfCompany> updateWrapper =
                new com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper<>();
            updateWrapper.in(YyXdfCompany::getCompanyname, companyNames)
                       .set(YyXdfCompany::getCstatus, new BigDecimal(0));

            int deletedCount = yyXdfCompanyMapper.update(null, updateWrapper);

            log.info("从监控列表中删除企业：{}，删除数量：{}", companyNames, deletedCount);

            Map<String, Object> result = new HashMap<>();
            result.put("deletedCount", deletedCount);
            result.put("companyNames", companyNames);

            return new JsonBean(200, "移除成功", result);
        } catch (Exception e) {
            log.error("删除企业监控失败", e);
            return new JsonBean(500, "删除失败：" + e.getMessage(), null);
        }
    }
}

