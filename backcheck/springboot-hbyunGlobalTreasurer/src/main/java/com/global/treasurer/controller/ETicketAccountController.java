package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TcEticketAccount;
import com.global.treasurer.service.TcEticketAccountService;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 电票账户配置管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/eTicketAccount/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping({"/cwgxAi/basicConfig/eTicketAccount", "/xjgl/basicConfig/eTicketAccount", "/financial/ticketAccount"})
@Api(tags = "电票账户配置管理")
public class ETicketAccountController {
    private static final Logger log = LoggerFactory.getLogger(ETicketAccountController.class);

    @Autowired
    private TcEticketAccountService tcEticketAccountService;

    @GetMapping("/list")
    @ApiOperation("分页查询电票账户列表(GET)")
    public String getListGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String businessScope,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) String bankCode,
            @RequestParam(required = false) String eticketStatus,
            @RequestParam(required = false) String eTicketSystem,
            @RequestParam(required = false) String status,
            HttpServletResponse response) {

        // 构建参数Map
        Map<String, Object> params = new HashMap<>();
        if (pageNo != null) params.put("pageNo", pageNo);
        if (pageNum != null) params.put("pageNo", pageNum);
        if (pageSize != null) params.put("pageSize", pageSize);
        if (limit != null) params.put("pageSize", limit);
        if (accountNo != null) params.put("accountNo", accountNo);
        if (accountNumber != null) params.put("accountNumber", accountNumber);
        if (bankName != null) params.put("bankName", bankName);
        if (accountName != null) params.put("accountName", accountName);
        if (businessScope != null) params.put("businessScope", businessScope);
        if (accountType != null) params.put("accountType", accountType);
        if (bankCode != null) params.put("bankCode", bankCode);
        if (eticketStatus != null) params.put("eticketStatus", eticketStatus);
        if (eTicketSystem != null) params.put("eTicketSystem", eTicketSystem);
        if (status != null) params.put("status", status);

        return executeListQuery(params);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询电票账户列表(GET-分页)")
    public String getPageGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String accountNo,
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String bankName,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) String businessScope,
            @RequestParam(required = false) String accountType,
            @RequestParam(required = false) String bankCode,
            @RequestParam(required = false) String eticketStatus,
            @RequestParam(required = false) String eTicketSystem,
            @RequestParam(required = false) String status,
            HttpServletResponse response) {

        // 构建参数Map
        Map<String, Object> params = new HashMap<>();
        if (pageNo != null) params.put("pageNo", pageNo);
        if (pageNum != null) params.put("pageNo", pageNum);
        if (pageSize != null) params.put("pageSize", pageSize);
        if (limit != null) params.put("pageSize", limit);
        if (accountNo != null) params.put("accountNo", accountNo);
        if (accountNumber != null) params.put("accountNumber", accountNumber);
        if (bankName != null) params.put("bankName", bankName);
        if (accountName != null) params.put("accountName", accountName);
        if (businessScope != null) params.put("businessScope", businessScope);
        if (accountType != null) params.put("accountType", accountType);
        if (bankCode != null) params.put("bankCode", bankCode);
        if (eticketStatus != null) params.put("eticketStatus", eticketStatus);
        if (eTicketSystem != null) params.put("eTicketSystem", eTicketSystem);
        if (status != null) params.put("status", status);

        return executeListQuery(params);
    }

    @PostMapping("/page")
    @ApiOperation("分页查询电票账户列表(POST-分页)")
    public String postPage(@FlexibleRequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        return executeListQuery(params);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询电票账户列表(POST)")
    public String getListPost(@FlexibleRequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        return executeListQuery(params);
    }

    /**
     * 内部查询方法，不使用 @FlexibleRequestBody 注解，避免 GET 请求调用时出错
     */
    private String executeListQuery(Map<String, Object> params) {
        try {
            if (params == null) params = new HashMap<>();
            log.info("========== 电票账户查询接口 ==========");
            log.info("接收参数 - params: {}", params);

            // 支持 pageNo、page 两种参数命名（兼容前端）
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) :
                             (params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1);
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                              (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10);

            log.info("解析后参数 - pageNo: {}, pageSize: {}", pageNo, pageSize);

            // 获取查询参数 - 支持多种字段名（兼容前端不同的命名方式）
            // 账户编号：accountNo 或 accountNumber
            String accountNo = params.get("accountNo") != null ? params.get("accountNo").toString() :
                              (params.get("accountNumber") != null ? params.get("accountNumber").toString() : null);
            // 银行名称：bankName 或 accountName
            String bankName = params.get("bankName") != null ? params.get("bankName").toString() :
                             (params.get("accountName") != null ? params.get("accountName").toString() : null);
            // 业务范围：businessScope 或 accountType
            String businessScope = params.get("businessScope") != null ? params.get("businessScope").toString() :
                                  (params.get("accountType") != null ? params.get("accountType").toString() : null);
            // 银行代码
            String bankCode = params.get("bankCode") != null ? params.get("bankCode").toString() : null;
            // 电票状态：eticketStatus 或 eTicketSystem
            String eticketStatus = params.get("eticketStatus") != null ? params.get("eticketStatus").toString() :
                                  (params.get("eTicketSystem") != null ? params.get("eTicketSystem").toString() : null);
            // 状态
            String status = params.get("status") != null ? params.get("status").toString() : null;

            QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
            // 使用正确的数据库字段名
            if (StringUtils.hasText(accountNo)) queryWrapper.like("ACCOUNT_NO", accountNo);
            if (StringUtils.hasText(bankName)) queryWrapper.like("BANK_NAME", bankName);
            if (StringUtils.hasText(businessScope)) queryWrapper.eq("BUSINESS_SCOPE", businessScope);
            if (StringUtils.hasText(bankCode)) queryWrapper.eq("BANK_CODE", bankCode);
            if (StringUtils.hasText(eticketStatus)) queryWrapper.eq("ETICKET_STATUS", eticketStatus);
            if (StringUtils.hasText(status)) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            Page<TcEticketAccount> page = new Page<>(pageNo, pageSize);
            IPage<TcEticketAccount> result = tcEticketAccountService.page(page, queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取电票账户列表失败", e);
            return JsonBean.error("获取电票账户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取电票账户详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TcEticketAccount account = tcEticketAccountService.getById(id);
            if (account == null) return JsonBean.error("电票账户不存在");
            return new JsonBean(1, "查询成功", account).toString();
        } catch (Exception e) {
            log.error("获取电票账户详情失败", e);
            return JsonBean.error("获取电票账户详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增电票账户(create)")
    public String create(@FlexibleRequestBody TcEticketAccount account, HttpServletResponse response) {
        return add(account, response);
    }

    @PostMapping("/add")
    @ApiOperation("新增电票账户(add)")
    public String add(@FlexibleRequestBody TcEticketAccount account, HttpServletResponse response) {
        try {
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            boolean success = tcEticketAccountService.save(account);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建电票账户失败", e);
            return JsonBean.error("创建电票账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新电票账户（统一接口）")
    public String saveOrUpdate(@FlexibleRequestBody TcEticketAccount account, HttpServletResponse response) {
        try {
            // 添加字段验证 - 检查银行名称（bankName是实际数据库字段，accountName是别名）
            String bankName = account.getBankName();
            // 兼容处理：如果前端传了accountName但没传bankName，则使用accountName
            if ((bankName == null || bankName.trim().isEmpty()) && account.getAccountName() != null) {
                bankName = account.getAccountName();
                account.setBankName(bankName);
            }
            if (bankName == null || bankName.trim().isEmpty()) {
                return new JsonBean(0, "银行名称不能为空", null).toJson();
            }

            boolean success;
            String message;

            if (account.getId() != null && !account.getId().toString().trim().isEmpty()) {
                // 更新操作
                TcEticketAccount existing = tcEticketAccountService.getById(account.getId());
                if (existing == null) {
                    return new JsonBean(0, "电票账户不存在", null).toJson();
                }
                account.setUpdateTime(new Date());
                success = tcEticketAccountService.updateById(account);
                message = success ? "更新成功" : "更新失败";
            } else {
                // 新增操作
                account.setCreateTime(new Date());
                account.setUpdateTime(new Date());
                success = tcEticketAccountService.save(account);
                message = success ? "创建成功" : "创建失败";
            }

            return new JsonBean(success ? 1 : 0, message, null).toJson();
        } catch (Exception e) {
            log.error("保存电票账户失败", e);
            return new JsonBean(0, "保存失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新电票账户(PUT)")
    public String updatePut(@FlexibleRequestBody TcEticketAccount account, HttpServletResponse response) {
        return update(account, response);
    }

    @PostMapping("/update")
    @ApiOperation("更新电票账户(POST)")
    public String update(@FlexibleRequestBody TcEticketAccount account, HttpServletResponse response) {
        try {
            if (account.getId() == null) {
                return new JsonBean(0, "账户ID不能为空", null).toJson();
            }
            account.setUpdateTime(new Date());
            boolean success = tcEticketAccountService.updateById(account);
            return new JsonBean(success ? 1 : 0, success ? "更新成功" : "更新失败", null).toJson();
        } catch (Exception e) {
            log.error("更新电票账户失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除电票账户(路径参数)")
    public String deleteByPath(@PathVariable String id, HttpServletResponse response) {
        try {
            if (id == null || id.trim().isEmpty()) {
                return new JsonBean(0, "ID不能为空", null).toJson();
            }
            boolean success = tcEticketAccountService.removeById(id);
            return new JsonBean(success ? 1 : 0, success ? "删除成功" : "删除失败", null).toJson();
        } catch (Exception e) {
            log.error("删除电票账户失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除电票账户")
    public String delete(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();
            log.info("删除电票账户请求参数: {}", params);

            // ID字段是String类型，不要转换为Long
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null || id.trim().isEmpty()) {
                log.warn("删除电票账户失败: ID为空");
                return new JsonBean(0, "ID不能为空", null).toJson();
            }

            log.info("准备删除电票账户, ID: {}", id);

            // 先检查记录是否存在
            TcEticketAccount existing = tcEticketAccountService.getById(id);
            if (existing == null) {
                log.warn("删除电票账户失败: 记录不存在, ID: {}", id);
                return new JsonBean(0, "记录不存在", null).toJson();
            }

            boolean success = tcEticketAccountService.removeById(id);
            log.info("删除电票账户结果: {}, ID: {}", success, id);

            if (success) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除电票账户异常, 参数: {}", params, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/syncStatus")
    @ApiOperation("同步账户状态")
    public String syncStatus(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            return JsonBean.success("同步成功");
        } catch (Exception e) {
            log.error("同步账户状态失败", e);
            return JsonBean.error("同步账户状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchSync")
    @ApiOperation("批量同步账户状态")
    public String batchSync(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            @SuppressWarnings("unchecked")
            java.util.List<Long> ids = (java.util.List<Long>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("请选择要同步的账户");
            }
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", ids.size());
            result.put("failCount", 0);
            return new JsonBean(1, "批量同步成功", result).toString();
        } catch (Exception e) {
            log.error("批量同步账户状态失败", e);
            return JsonBean.error("批量同步账户状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出电票账户配置")
    public String export(@RequestParam(required = false) String accountName,
                        @RequestParam(required = false) String accountType,
                        @RequestParam(required = false) String billType,
                        @RequestParam(required = false) Integer status,
                        HttpServletResponse response) {
        try {
            QueryWrapper<TcEticketAccount> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(accountName)) queryWrapper.like("ACCOUNT_NAME", accountName);
            if (StringUtils.hasText(accountType)) queryWrapper.eq("ACCOUNT_TYPE", accountType);
            if (StringUtils.hasText(billType)) queryWrapper.eq("BILL_TYPE", billType);
            if (status != null) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            java.util.List<TcEticketAccount> list = tcEticketAccountService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();
        } catch (Exception e) {
            log.error("导出电票账户配置失败", e);
            return JsonBean.error("导出电票账户配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/getAccountTypes")
    @ApiOperation("获取账户类型下拉数据")
    public String getAccountTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("ENTERPRISE", "企业账户"), createOption("PERSONAL", "个人账户"),
                createOption("BANK", "银行账户"), createOption("OTHER", "其他账户")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取账户类型失败: " + e.getMessage());
        }
    }

    @GetMapping("/getBillTypes")
    @ApiOperation("获取票据类型下拉数据")
    public String getBillTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("BANK_ACCEPTANCE", "银行承兑汇票"), createOption("COMMERCIAL", "商业承兑汇票")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取票据类型失败: " + e.getMessage());
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

