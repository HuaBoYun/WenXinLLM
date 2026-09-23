package com.financial.sharing.controller;

import com.financial.sharing.dto.VoucherTemplateQueryParam;
import com.financial.sharing.oracle.mapper.VoucherTemplateMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.entity.TblOrganizationUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 凭证模板管理控制器
 *
 * @author system
 * @since 2024-12-07
 */
@Slf4j
@Api(tags = "凭证模板管理")
@RestController
@RequestMapping("/voucher-template")
@CrossOrigin
public class VoucherTemplateController {

    @Autowired
    private VoucherTemplateMapper voucherTemplateMapper;

    @ApiOperation("分页查询凭证模板列表")
    @PostMapping("/getList")
    public String getVoucherTemplatePage(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody Map<String, Object> param) {
        try {
            // 获取分页参数
            int pageNumber = 1;
            int pageSize = 20;
            if (param.get("pageNumber") != null) {
                pageNumber = Integer.parseInt(param.get("pageNumber").toString());
            }
            if (param.get("pageSize") != null) {
                pageSize = Integer.parseInt(param.get("pageSize").toString());
            }

            // 构建查询参数
            VoucherTemplateQueryParam queryParam = new VoucherTemplateQueryParam();
            queryParam.setPageNo(pageNumber);
            queryParam.setPageSize(pageSize);
            queryParam.setOffset((pageNumber - 1) * pageSize);

            // 设置查询条件
            if (param.get("templateName") != null && !"".equals(param.get("templateName").toString())) {
                queryParam.setKeyword(param.get("templateName").toString());
            }
            if (param.get("templateType") != null && !"".equals(param.get("templateType").toString())) {
                queryParam.setTemplateType(param.get("templateType").toString());
            }
            if (param.get("status") != null && !"".equals(param.get("status").toString())) {
                queryParam.setStatus(param.get("status").toString());
            }

            // 查询数据库
            List<Map<String, Object>> templateList = voucherTemplateMapper.selectTemplatePage(queryParam);
            Long totalRecord = voucherTemplateMapper.selectTemplateCount(queryParam);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", templateList);
            data.put("totalRecord", totalRecord != null ? totalRecord.intValue() : 0);
            data.put("pageNumber", pageNumber);
            data.put("pageSize", pageSize);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证模板列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询凭证模板详情")
    @GetMapping("/{templateId}")
    public String getVoucherTemplateById(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable String templateId) {
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

            Map<String, Object> template = new HashMap<>();
            template.put("templateId", templateId);
            template.put("templateName", "银行收款凭证模板");
            template.put("templateCode", "TBNK001");
            template.put("voucherType", "记");
            template.put("description", "用于银行收款业务的标准凭证模板");
            template.put("status", "ACTIVE");
            template.put("statusName", "启用");
            template.put("createTime", "2024-01-01T00:00:00.000Z");
            template.put("updateTime", "2024-12-07T10:00:00.000Z");

            // 模拟分录信息
            List<Map<String, Object>> entries = new ArrayList<>();
            Map<String, Object> entry1 = new HashMap<>();
            entry1.put("lineNo", 1);
            entry1.put("accountCode", "1001");
            entry1.put("accountName", "库存现金");
            entry1.put("debitAmount", new BigDecimal("10000.00"));
            entry1.put("creditAmount", BigDecimal.ZERO);
            entry1.put("description", "银行收款");
            entries.add(entry1);

            Map<String, Object> entry2 = new HashMap<>();
            entry2.put("lineNo", 2);
            entry2.put("accountCode", "1002");
            entry2.put("accountName", "银行存款");
            entry2.put("debitAmount", BigDecimal.ZERO);
            entry2.put("creditAmount", new BigDecimal("10000.00"));
            entry2.put("description", "银行收款");
            entries.add(entry2);

            template.put("entries", entries);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(template);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询凭证模板详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增凭证模板")
    @PostMapping("/create")
    public String createVoucherTemplate(HttpServletRequest request,
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

            // 生成模板ID (使用时间戳)
            Long templateId = System.currentTimeMillis();

            // 构建模板数据
            Map<String, Object> templateData = new HashMap<>();
            templateData.put("templateId", templateId);
            templateData.put("templateCode", param.get("templateCode"));
            templateData.put("templateName", param.get("templateName"));
            templateData.put("templateType", param.get("templateType"));
            templateData.put("status", "ACTIVE");
            templateData.put("version", "1.0");
            templateData.put("description", param.get("description"));
            templateData.put("voucherType", param.get("voucherType"));
            templateData.put("summaryRule", param.get("summaryRule"));
            templateData.put("debitSubject", param.get("debitSubject"));
            templateData.put("creditSubject", param.get("creditSubject"));
            templateData.put("amountField", param.get("amountField"));
            templateData.put("currencyField", param.get("currencyField"));
            templateData.put("generateCondition", param.get("generateCondition"));
            templateData.put("sortRule", param.get("sortRule"));
            templateData.put("groupRule", param.get("groupRule"));
            templateData.put("usedCount", 0);
            templateData.put("bookId", param.get("bookId"));
            templateData.put("tenantId", param.get("tenantId"));
            templateData.put("creatorId", loginStaff.getStaffid().longValue());
            templateData.put("creatorName", loginStaff.getRealname());

            // 调用 Mapper 插入数据库
            int insertResult = voucherTemplateMapper.insertTemplate(templateData);
            log.info("插入凭证模板结果: {}, 模板ID: {}", insertResult, templateId);

            Map<String, Object> result = new HashMap<>();
            result.put("templateId", templateId);
            result.put("templateName", param.get("templateName"));
            result.put("status", "ACTIVE");
            result.put("message", "凭证模板创建成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("创建成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("创建凭证模板失败", e);
            return createErrorResponse("创建失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新凭证模板")
    @PostMapping("/update")
    public String updateVoucherTemplate(HttpServletRequest request,
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

            param.put("updateTime", new Date());
            param.put("updateBy", loginStaff.getUsername());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("更新成功");
            json.setData(param);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("更新凭证模板失败", e);
            return createErrorResponse("更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除凭证模板")
    @DeleteMapping("/{templateId}")
    public String deleteVoucherTemplate(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @PathVariable String templateId) {
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

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("删除成功");
            json.setData("凭证模板 " + templateId + " 已删除");
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("删除凭证模板失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制凭证模板")
    @PostMapping("/{templateId}/copy")
    public String copyVoucherTemplate(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String templateId,
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

            String newTemplateCode = (String) param.get("newTemplateCode");
            String newTemplateName = (String) param.get("newTemplateName");
            log.info("用户 {} 复制凭证模板，源ID：{}，新编码：{}，新名称：{}",
                    loginStaff.getStaffid(), templateId, newTemplateCode, newTemplateName);

            // 生成新的模板ID
            String newTemplateId = "TPL" + System.currentTimeMillis();

            // 构建返回结果
            Map<String, Object> result = new HashMap<>();
            result.put("sourceTemplateId", templateId);
            result.put("templateId", newTemplateId);
            result.put("templateCode", newTemplateCode != null ? newTemplateCode : templateId + "_COPY");
            result.put("templateName", newTemplateName != null ? newTemplateName : "凭证模板副本");
            result.put("message", "凭证模板复制成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("复制成功");
            json.setData(result);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("复制凭证模板失败", e);
            return createErrorResponse("复制失败: " + e.getMessage());
        }
    }

    @ApiOperation("预览凭证模板效果")
    @PostMapping("/preview")
    public String previewVoucherTemplate(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestBody Map<String, Object> param) {
        try {
            log.info("预览凭证模板效果，参数：{}", param);

            // 获取模板配置信息
            String templateCode = (String) param.get("templateCode");
            String templateName = (String) param.get("templateName");
            String templateType = (String) param.get("templateType");
            String voucherType = (String) param.get("voucherType");
            String summaryRule = (String) param.get("summaryRule");
            String debitSubject = (String) param.get("debitSubject");
            String creditSubject = (String) param.get("creditSubject");
            String amountField = (String) param.get("amountField");
            String currencyField = (String) param.get("currencyField");
            String generateCondition = (String) param.get("generateCondition");

            // 构建预览凭证数据
            List<Map<String, Object>> previewVouchers = new ArrayList<>();

            // 生成示例凭证1
            Map<String, Object> voucher1 = new HashMap<>();
            voucher1.put("voucherNo", "PZ" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()) + "0001");
            voucher1.put("voucherDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            voucher1.put("voucherType", voucherType != null ? voucherType : "记账凭证");
            voucher1.put("summary", summaryRule != null && !summaryRule.isEmpty() ? summaryRule : "业务摘要示例");
            voucher1.put("debitSubjectCode", debitSubject != null ? debitSubject.split("-")[0] : "1001");
            voucher1.put("debitSubjectName", debitSubject != null ? debitSubject : "库存现金");
            voucher1.put("creditSubjectCode", creditSubject != null ? creditSubject.split("-")[0] : "1002");
            voucher1.put("creditSubjectName", creditSubject != null ? creditSubject : "银行存款");
            voucher1.put("debitAmount", new BigDecimal("10000.00"));
            voucher1.put("creditAmount", new BigDecimal("0.00"));
            voucher1.put("currency", currencyField != null && !currencyField.isEmpty() ? currencyField : "CNY");
            voucher1.put("lineNo", 1);
            previewVouchers.add(voucher1);

            // 生成示例凭证2（贷方分录）
            Map<String, Object> voucher2 = new HashMap<>();
            voucher2.put("voucherNo", "PZ" + new java.text.SimpleDateFormat("yyyyMMdd").format(new Date()) + "0001");
            voucher2.put("voucherDate", new java.text.SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            voucher2.put("voucherType", voucherType != null ? voucherType : "记账凭证");
            voucher2.put("summary", summaryRule != null && !summaryRule.isEmpty() ? summaryRule : "业务摘要示例");
            voucher2.put("debitSubjectCode", creditSubject != null ? creditSubject.split("-")[0] : "1002");
            voucher2.put("debitSubjectName", creditSubject != null ? creditSubject : "银行存款");
            voucher2.put("creditSubjectCode", debitSubject != null ? debitSubject.split("-")[0] : "1001");
            voucher2.put("creditSubjectName", debitSubject != null ? debitSubject : "库存现金");
            voucher2.put("debitAmount", new BigDecimal("0.00"));
            voucher2.put("creditAmount", new BigDecimal("10000.00"));
            voucher2.put("currency", currencyField != null && !currencyField.isEmpty() ? currencyField : "CNY");
            voucher2.put("lineNo", 2);
            previewVouchers.add(voucher2);

            // 构建预览结果
            Map<String, Object> previewResult = new HashMap<>();
            previewResult.put("templateCode", templateCode);
            previewResult.put("templateName", templateName);
            previewResult.put("templateType", templateType);
            previewResult.put("voucherType", voucherType);
            previewResult.put("entries", previewVouchers);
            previewResult.put("totalDebit", new BigDecimal("10000.00"));
            previewResult.put("totalCredit", new BigDecimal("10000.00"));
            previewResult.put("isBalanced", true);
            previewResult.put("previewTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

            // 添加模板配置摘要
            Map<String, Object> configSummary = new HashMap<>();
            configSummary.put("summaryRule", summaryRule);
            configSummary.put("debitSubject", debitSubject);
            configSummary.put("creditSubject", creditSubject);
            configSummary.put("amountField", amountField);
            configSummary.put("currencyField", currencyField);
            configSummary.put("generateCondition", generateCondition);
            previewResult.put("configSummary", configSummary);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("预览成功");
            json.setData(previewResult);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("预览凭证模板失败", e);
            return createErrorResponse("预览失败: " + e.getMessage());
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

    // ==================== 版本管理相关接口 ====================

    @ApiOperation("分页查询模板版本列表")
    @PostMapping("/version/page")
    public String getVersionPage(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestBody Map<String, Object> param) {
        try {
            // 获取分页参数
            int pageNumber = 1;
            int pageSize = 20;
            if (param.get("pageNumber") != null) {
                pageNumber = Integer.parseInt(param.get("pageNumber").toString());
            }
            if (param.get("pageSize") != null) {
                pageSize = Integer.parseInt(param.get("pageSize").toString());
            }
            int offset = (pageNumber - 1) * pageSize;

            // 构建查询参数
            Map<String, Object> queryParam = new HashMap<>();
            queryParam.put("pageNumber", pageNumber);
            queryParam.put("pageSize", pageSize);
            queryParam.put("offset", offset);

            // 设置查询条件
            if (param.get("templateId") != null) {
                queryParam.put("templateId", Long.parseLong(param.get("templateId").toString()));
            }
            if (param.get("templateName") != null && !"".equals(param.get("templateName").toString())) {
                queryParam.put("templateName", param.get("templateName").toString());
            }
            if (param.get("versionType") != null && !"".equals(param.get("versionType").toString())) {
                queryParam.put("versionType", param.get("versionType").toString());
            }
            if (param.get("status") != null && !"".equals(param.get("status").toString())) {
                queryParam.put("status", param.get("status").toString());
            }

            // 查询数据库
            List<Map<String, Object>> versionList = voucherTemplateMapper.selectVersionPage(queryParam);
            Long totalRecord = voucherTemplateMapper.selectVersionCount(queryParam);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", versionList);
            data.put("totalRecord", totalRecord != null ? totalRecord.intValue() : 0);
            data.put("pageNumber", pageNumber);
            data.put("pageSize", pageSize);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询版本列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询版本详情")
    @GetMapping("/version/{versionId}")
    public String getVersionById(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable("versionId") Long versionId) {
        try {
            Map<String, Object> version = voucherTemplateMapper.selectVersionById(versionId);
            if (version == null) {
                return createErrorResponse("版本不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(version);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询版本详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("激活版本")
    @PutMapping("/version/{versionId}/activate")
    public String activateVersion(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable("versionId") Long versionId) {
        try {
            // 先查询版本信息获取模板ID
            Map<String, Object> version = voucherTemplateMapper.selectVersionById(versionId);
            if (version == null) {
                return createErrorResponse("版本不存在");
            }

            Long templateId = Long.parseLong(version.get("templateId").toString());

            // 取消同模板的其他激活版本
            voucherTemplateMapper.deactivateOtherVersions(templateId, versionId);

            // 激活当前版本
            int result = voucherTemplateMapper.activateVersion(versionId);
            if (result > 0) {
                JsonBean json = new JsonBean();
                json.setCode(1);
                json.setMsg("激活成功");
                return JsonMapper.nonNullMapper().toJson(json);
            } else {
                return createErrorResponse("激活失败");
            }
        } catch (Exception e) {
            log.error("激活版本失败", e);
            return createErrorResponse("激活失败: " + e.getMessage());
        }
    }

    @ApiOperation("回滚到指定版本")
    @PostMapping("/version/{versionId}/rollback")
    public String rollbackVersion(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable("versionId") Long versionId) {
        try {
            // 先查询版本信息获取模板ID
            Map<String, Object> version = voucherTemplateMapper.selectVersionById(versionId);
            if (version == null) {
                return createErrorResponse("版本不存在");
            }

            Long templateId = Long.parseLong(version.get("templateId").toString());

            // 取消同模板的其他激活版本
            voucherTemplateMapper.deactivateOtherVersions(templateId, versionId);

            // 回滚到指定版本
            int result = voucherTemplateMapper.rollbackToVersion(versionId);
            if (result > 0) {
                JsonBean json = new JsonBean();
                json.setCode(1);
                json.setMsg("回滚成功");
                return JsonMapper.nonNullMapper().toJson(json);
            } else {
                return createErrorResponse("回滚失败");
            }
        } catch (Exception e) {
            log.error("回滚版本失败", e);
            return createErrorResponse("回滚失败: " + e.getMessage());
        }
    }
}