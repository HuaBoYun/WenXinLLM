package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TblThirdPartyAccount;
import com.global.treasurer.service.TblThirdPartyAccountService;
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
 * 第三方账户管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/thirdPartyAccount/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/thirdPartyAccount")
@Api(tags = "第三方账户管理")
public class ThirdPartyAccountController {
    private static final Logger log = LoggerFactory.getLogger(ThirdPartyAccountController.class);

    @Autowired
    private TblThirdPartyAccountService tblThirdPartyAccountService;

    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("分页查询第三方账户列表")
    public String getList(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit,
                          @RequestParam(required = false) String accountCode, @RequestParam(required = false) String accountName,
                          @RequestParam(required = false) String accountType,
                          @RequestParam(required = false) String platformType, @RequestParam(required = false) String connectionStatus,
                          HttpServletResponse response) {
        try {
            int pageNo = page != null ? page : 1;
            int pageSize = limit != null ? limit : 20;

            QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(accountCode)) queryWrapper.like("ACCOUNT_CODE", accountCode);
            if (StringUtils.hasText(accountName)) queryWrapper.like("ACCOUNT_NAME", accountName);
            if (StringUtils.hasText(accountType)) queryWrapper.eq("ACCOUNT_TYPE", accountType);
            if (StringUtils.hasText(platformType)) queryWrapper.eq("THIRD_PARTY_SYSTEM", platformType);
            if (StringUtils.hasText(connectionStatus)) queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
            queryWrapper.orderByDesc("CREATE_TIME");

            Page<TblThirdPartyAccount> pageObj = new Page<>(pageNo, pageSize);
            IPage<TblThirdPartyAccount> result = tblThirdPartyAccountService.page(pageObj, queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取第三方账户列表失败", e);
            return JsonBean.error("获取第三方账户列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取第三方账户详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
            if (account == null) return JsonBean.error("第三方账户不存在");
            return new JsonBean(1, "查询成功", account).toString();
        } catch (Exception e) {
            log.error("获取第三方账户详情失败", e);
            return JsonBean.error("获取第三方账户详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/add")
    @ApiOperation("新增第三方账户")
    public String add(@FlexibleRequestBody TblThirdPartyAccount account, HttpServletResponse response) {
        try {
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            boolean success = tblThirdPartyAccountService.save(account);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建第三方账户失败", e);
            return JsonBean.error("创建第三方账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新第三方账户（统一接口）")
    public String saveOrUpdate(@FlexibleRequestBody TblThirdPartyAccount account, HttpServletResponse response) {
        try {
            boolean success;
            String message;

            if (account.getId() != null) {
                // 更新操作
                TblThirdPartyAccount existing = tblThirdPartyAccountService.getById(account.getId());
                if (existing == null) {
                    return JsonBean.error("第三方账户不存在");
                }
                account.setUpdateTime(new Date());
                success = tblThirdPartyAccountService.updateById(account);
                message = success ? "更新成功" : "更新失败";
            } else {
                // 新增操作
                account.setCreateTime(new Date());
                account.setUpdateTime(new Date());
                success = tblThirdPartyAccountService.save(account);
                message = success ? "创建成功" : "创建失败";
            }

            return success ? JsonBean.success(message) : JsonBean.error(message);
        } catch (Exception e) {
            log.error("保存第三方账户失败", e);
            return JsonBean.error("保存第三方账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新第三方账户")
    public String update(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            log.info("========== 更新第三方账户开始 ==========");
            log.info("接收到的原始参数: {}", params);

            // 从 Map 中获取 ID，处理可能的类型问题
            Object idObj = params.get("id");
            if (idObj == null) {
                return JsonBean.error("账户ID不能为空");
            }

            Long id;
            if (idObj instanceof Number) {
                id = ((Number) idObj).longValue();
            } else {
                id = Long.parseLong(idObj.toString());
            }
            log.info("解析后的ID: {}, 原始值: {}, 原始类型: {}", id, idObj, idObj.getClass().getName());

            // 先查询数据库中是否存在这条记录
            TblThirdPartyAccount existingRecord = tblThirdPartyAccountService.getById(id);
            log.info("查询现有记录结果: {}", existingRecord);

            if (existingRecord == null) {
                // 查询所有记录的ID，看看数据库中有哪些
                List<TblThirdPartyAccount> allRecords = tblThirdPartyAccountService.list();
                log.info("数据库中所有记录的ID: {}", allRecords.stream().map(TblThirdPartyAccount::getId).collect(java.util.stream.Collectors.toList()));
                log.warn("找不到ID={}的记录，可能是ID精度丢失问题", id);
            }

            // 构建实体对象
            TblThirdPartyAccount account = new TblThirdPartyAccount();
            account.setId(id);
            account.setAccountCode(params.get("accountCode") != null ? params.get("accountCode").toString() : null);
            account.setAccountName(params.get("accountName") != null ? params.get("accountName").toString() : null);
            account.setThirdPartySystem(params.get("thirdPartySystem") != null ? params.get("thirdPartySystem").toString() : null);
            account.setAccountType(params.get("accountType") != null ? params.get("accountType").toString() : null);
            account.setAccountIdentifier(params.get("accountIdentifier") != null ? params.get("accountIdentifier").toString() : null);
            account.setAccountSecret(params.get("accountSecret") != null ? params.get("accountSecret").toString() : null);
            account.setApiUrl(params.get("apiUrl") != null ? params.get("apiUrl").toString() : null);
            account.setSyncFrequency(params.get("syncFrequency") != null ? params.get("syncFrequency").toString() : null);
            account.setDescription(params.get("description") != null ? params.get("description").toString() : null);
            if (params.get("isEnabled") != null) {
                account.setIsEnabled(Integer.parseInt(params.get("isEnabled").toString()));
            }
            account.setUpdateTime(new Date());

            log.info("构建的更新实体: id={}, accountName={}", account.getId(), account.getAccountName());

            // 执行更新
            boolean success = tblThirdPartyAccountService.updateById(account);
            log.info("updateById 返回结果: {}", success);

            // 更新后再次查询验证
            TblThirdPartyAccount afterUpdate = tblThirdPartyAccountService.getById(id);
            log.info("更新后查询结果: {}", afterUpdate);
            log.info("========== 更新第三方账户结束 ==========");

            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新第三方账户失败", e);
            return JsonBean.error("更新第三方账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除第三方账户")
    public String delete(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) return JsonBean.error("ID不能为空");
            boolean success = tblThirdPartyAccountService.removeById(id);
            return success ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除第三方账户失败", e);
            return JsonBean.error("删除第三方账户失败: " + e.getMessage());
        }
    }

    // 注释：与BasicConfigController路径冲突（/thirdPartyAccount/testConnection），使用BasicConfigController中的实现
    // @PostMapping("/testConnection")
    // @ApiOperation("测试第三方账户连接")
    // public String testConnection(...) { ... }

    @PostMapping("/sync")
    @ApiOperation("同步第三方账户")
    public String sync(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Object idParam = params.get("id");
            if (idParam == null || idParam.toString().trim().isEmpty()) {
                return JsonBean.error("ID不能为空");
            }
            Long id = Long.parseLong(idParam.toString().trim());
            TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("账户不存在");
            }
            account.setLastSyncTime(new Date());
            account.setUpdateTime(new Date());
            tblThirdPartyAccountService.updateById(account);
            return new JsonBean(1, "同步成功", account).toString();
        } catch (Exception e) {
            log.error("同步第三方账户失败", e);
            return JsonBean.error("同步第三方账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/batchSync")
    @ApiOperation("批量同步第三方账户")
    public String batchSync(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            Object idsParam = params.get("ids");
            if (idsParam == null || idsParam.toString().trim().isEmpty()) {
                return JsonBean.error("请选择要同步的账户");
            }
            // ids 通过 @RequestParam Map 接收时是 String 类型（如 "1,2,3"），需要手动解析
            java.util.List<Long> ids = java.util.Arrays.stream(idsParam.toString().split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(Long::valueOf)
                    .collect(java.util.stream.Collectors.toList());
            if (ids.isEmpty()) {
                return JsonBean.error("请选择要同步的账户");
            }
            // 批量更新 lastSyncTime
            Date now = new Date();
            for (Long id : ids) {
                TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
                if (account != null) {
                    account.setLastSyncTime(now);
                    account.setUpdateTime(now);
                    tblThirdPartyAccountService.updateById(account);
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", ids.size());
            result.put("failCount", 0);
            result.put("syncTime", now);
            return new JsonBean(1, "批量同步成功", result).toString();
        } catch (Exception e) {
            log.error("批量同步第三方账户失败", e);
            return JsonBean.error("批量同步第三方账户失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出第三方账户")
    public String export(@RequestParam(required = false) String accountName,
                        @RequestParam(required = false) String accountType,
                        @RequestParam(required = false) String platformType,
                        @RequestParam(required = false) Integer status,
                        HttpServletResponse response) {
        try {
            QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(accountName)) queryWrapper.like("ACCOUNT_NAME", accountName);
            if (StringUtils.hasText(accountType)) queryWrapper.eq("ACCOUNT_TYPE", accountType);
            if (StringUtils.hasText(platformType)) queryWrapper.eq("PLATFORM_TYPE", platformType);
            if (status != null) queryWrapper.eq("STATUS", status);
            queryWrapper.orderByDesc("CREATE_TIME");

            java.util.List<TblThirdPartyAccount> list = tblThirdPartyAccountService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();
        } catch (Exception e) {
            log.error("导出第三方账户失败", e);
            return JsonBean.error("导出第三方账户失败: " + e.getMessage());
        }
    }

    @GetMapping("/systems")
    @ApiOperation("获取第三方系统选项")
    public String getSystems(HttpServletResponse response) {
        try {
            List<Map<String, String>> systems = Arrays.asList(
                createOption("ALIPAY", "支付宝"),
                createOption("WECHAT", "微信支付"),
                createOption("UNIONPAY", "银联"),
                createOption("PAYPAL", "PayPal"),
                createOption("SWIFT", "SWIFT"),
                createOption("OTHER", "其他")
            );
            return new JsonBean(1, "查询成功", systems).toString();
        } catch (Exception e) {
            return JsonBean.error("获取第三方系统选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/accountTypes")
    @ApiOperation("获取账户类型选项")
    public String getAccountTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("PAYMENT", "支付账户"),
                createOption("COLLECTION", "收款账户"),
                createOption("SETTLEMENT", "结算账户"),
                createOption("RESERVE", "备付金账户"),
                createOption("OTHER", "其他账户")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取账户类型选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/platformTypes")
    @ApiOperation("获取平台类型选项")
    public String getPlatformTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("DOMESTIC", "境内平台"),
                createOption("OVERSEAS", "境外平台"),
                createOption("CROSS_BORDER", "跨境平台")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取平台类型选项失败: " + e.getMessage());
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }
}

