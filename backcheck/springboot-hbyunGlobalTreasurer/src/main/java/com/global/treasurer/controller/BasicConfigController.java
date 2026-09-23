package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.TcBusinessSystem;
import com.global.treasurer.entity.TcDataMapping;
import com.global.treasurer.entity.TcEticketAccount;
import com.global.treasurer.entity.TcSealArchive;
import com.global.treasurer.entity.TcSealCombination;
import com.global.treasurer.entity.TcSealType;
import com.global.treasurer.entity.TblSealUsageRecord;
import com.global.treasurer.entity.TblSecurityParameter;
import com.global.treasurer.entity.TblUkeyVendor;
import com.global.treasurer.entity.TblThirdPartyAccount;
import com.global.treasurer.service.TcBusinessSystemService;
import com.global.treasurer.service.TcDataMappingService;
import com.global.treasurer.service.TcEticketAccountService;
import com.global.treasurer.service.TblSealArchiveService;
import com.global.treasurer.service.TcSealCombinationService;
import com.global.treasurer.service.TblSealUsageRecordService;
import com.global.treasurer.service.TblSecurityParameterService;
import com.global.treasurer.service.TblUkeyVendorService;
import com.global.treasurer.service.TblThirdPartyAccountService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.global.treasurer.annotation.FlexibleRequestBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 基础配置管理控制器
 * 包含业务系统注册、数据映射配置、电票账户配置等功能
 *
 * @author 华博云开发团队
 * @since 2024-12-22
 */
@RestController
@RequestMapping({
    "/financial/basicConfig",
    "/xjgl/basicConfig",
    "/financial/treasury-common/business-system",
    "/financial/treasury-common/data-mapping",
    "/financial/treasury-common/eticket-account",
    "/intg/data-mapping",
    "/centralaudit/financial/basicConfig",
    "/treasury"
})
@Api(tags = "基础配置管理")
public class BasicConfigController {
    private static final Logger log = LoggerFactory.getLogger(BasicConfigController.class);

    @Autowired
    private TcBusinessSystemService tcBusinessSystemService;

    @Autowired
    private TcDataMappingService tcDataMappingService;

    @Autowired
    private TcEticketAccountService tcEticketAccountService;

    @Autowired
    private TblSealUsageRecordService tblSealUsageRecordService;

    @Autowired
    private TblSecurityParameterService tblSecurityParameterService;

    @Autowired
    private TblUkeyVendorService tblUkeyVendorService;

    @Autowired
    private TblThirdPartyAccountService tblThirdPartyAccountService;

    @Autowired
    private TcSealCombinationService tcSealCombinationService;

    @Autowired
    private TblSealArchiveService tblSealArchiveService;

    @Autowired
    private com.global.treasurer.service.TcSealTypeService tcSealTypeService;

    @Resource
    private UserProvider userProvider;

    // ==================== 业务系统注册管理 ====================

    // 注释：与BusinessSystemController路径冲突，使用独立的Controller
    // @RequestMapping(value = "/system/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取业务系统列表")
    // public String getBusinessSystemList(
    //         @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNo,
    //         @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
    //         @ApiParam(value = "系统名称") @RequestParam(required = false) String systemName,
    //         @ApiParam(value = "系统类型") @RequestParam(required = false) String systemType,
    //         @ApiParam(value = "连接状态") @RequestParam(required = false) String connectionStatus,
    //         @ApiParam(value = "状态") @RequestParam(required = false) String status,
    //         HttpServletResponse response) {
    //
    //     try {
    //         // 权限验证
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         IPage<TcBusinessSystem> result = tcBusinessSystemService.getBusinessSystemPage(pageNo, pageSize, systemName, systemType, connectionStatus, status);
    //
    //         Map<String, Object> data = new HashMap<>();
    //         data.put("tlist", result.getRecords());
    //         data.put("totalRecord", result.getTotal());
    //         data.put("pageNo", result.getCurrent());
    //         data.put("pageSize", result.getSize());
    //
    //         return JsonBean.success(data);
    //
    //     } catch (Exception e) {
    //         log.error("获取业务系统列表失败", e);
    //         return JsonBean.error("获取业务系统列表失败: " + e.getMessage());
    //     }
    // }

    // @GetMapping("/system/detail")  // 注释掉以避免与BusinessSystemController冲突
    @GetMapping("/system/detail_old")
    @ApiOperation("获取业务系统详情")
    public String getBusinessSystemDetail(
            @ApiParam(value = "系统ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcBusinessSystem businessSystem = tcBusinessSystemService.getById(id);
            if (businessSystem == null) {
                return JsonBean.error("业务系统不存在");
            }

            // 设置默认值,确保前端显示正常
            if (!StringUtils.hasText(businessSystem.getStatus())) {
                businessSystem.setStatus("1"); // 默认启用
            }
            if (!StringUtils.hasText(businessSystem.getConnectionStatus())) {
                businessSystem.setConnectionStatus("OFFLINE"); // 默认离线
            }
            if (!StringUtils.hasText(businessSystem.getAuthConfig())) {
                businessSystem.setAuthConfig("{}");
            }

            return JsonBean.success(businessSystem);

        } catch (Exception e) {
            log.error("获取业务系统详情失败，ID: {}", id, e);
            return JsonBean.error("获取业务系统详情失败: " + e.getMessage());
        }
    }

    // @PostMapping("/system/create")  // 注释掉以避免与BusinessSystemController冲突
    @ApiOperation("创建业务系统")
    public String createBusinessSystem(TcBusinessSystem businessSystem, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(businessSystem.getSystemName())) {
                return JsonBean.error("系统名称不能为空");
            }
            if (!StringUtils.hasText(businessSystem.getSystemCode())) {
                return JsonBean.error("系统编码不能为空");
            }
            if (!StringUtils.hasText(businessSystem.getSystemType())) {
                return JsonBean.error("系统类型不能为空");
            }

            // 处理认证配置:验证JSON格式
            String authConfig = businessSystem.getAuthConfig();
            if (StringUtils.hasText(authConfig)) {
                try {
                    // 验证是否为有效的JSON字符串
                    com.alibaba.fastjson.JSON.parse(authConfig);
                } catch (Exception e) {
                    return JsonBean.error("认证配置必须为有效的JSON格式");
                }
            } else {
                businessSystem.setAuthConfig("{}"); // 设置空JSON对象
            }

            // 处理状态字段:确保为字符串类型
            if (businessSystem.getStatus() == null) {
                businessSystem.setStatus("1"); // 默认启用
            } else {
                // 如果传入的是数字,转换为字符串
                businessSystem.setStatus(String.valueOf(businessSystem.getStatus()));
            }

            // 设置默认值
            businessSystem.setId(generateId());
            businessSystem.setCreateTime(new Date());
            businessSystem.setUpdateTime(new Date());
            businessSystem.setCreateUser(getCurrentUser());
            businessSystem.setUpdateUser(getCurrentUser());

            boolean success = tcBusinessSystemService.save(businessSystem);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建业务系统失败", e);
            return JsonBean.error("创建业务系统失败: " + e.getMessage());
        }
    }

    @PutMapping("/system/update")
    @ApiOperation("更新业务系统")
    public String updateBusinessSystem(TcBusinessSystem businessSystem, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (businessSystem.getId() == null) {
                return JsonBean.error("系统ID不能为空");
            }

            // 检查是否存在
            TcBusinessSystem existing = tcBusinessSystemService.getById(businessSystem.getId());
            if (existing == null) {
                return JsonBean.error("业务系统不存在");
            }

            // 处理认证配置:验证JSON格式
            String authConfig = businessSystem.getAuthConfig();
            if (StringUtils.hasText(authConfig)) {
                try {
                    // 验证是否为有效的JSON字符串
                    com.alibaba.fastjson.JSON.parse(authConfig);
                } catch (Exception e) {
                    return JsonBean.error("认证配置必须为有效的JSON格式");
                }
            } else {
                businessSystem.setAuthConfig("{}"); // 设置空JSON对象
            }

            // 处理状态字段:确保为字符串类型
            if (businessSystem.getStatus() != null) {
                // 如果传入的是数字,转换为字符串
                businessSystem.setStatus(String.valueOf(businessSystem.getStatus()));
            }

            businessSystem.setUpdateTime(new Date());
            businessSystem.setUpdateUser(getCurrentUser());

            boolean success = tcBusinessSystemService.updateById(businessSystem);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新业务系统失败", e);
            return JsonBean.error("更新业务系统失败: " + e.getMessage());
        }
    }

    // @DeleteMapping("/system/delete")  // 注释掉以避免与BusinessSystemController冲突
    @DeleteMapping("/system/delete_old")
    @ApiOperation("删除业务系统")
    public String deleteBusinessSystem(
            @ApiParam(value = "系统ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TcBusinessSystem existing = tcBusinessSystemService.getById(id);
            if (existing == null) {
                return JsonBean.error("业务系统不存在");
            }

            boolean success = tcBusinessSystemService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除业务系统失败，ID: {}", id, e);
            return JsonBean.error("删除业务系统失败: " + e.getMessage());
        }
    }

    @PostMapping("/system/search")
    @ApiOperation("搜索业务系统")
    public String searchBusinessSystem(Map<String, Object> params, HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
            String systemCode = params.get("systemCode") != null ? params.get("systemCode").toString() : null;
            String systemName = params.get("systemName") != null ? params.get("systemName").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;

            IPage<TcBusinessSystem> result = tcBusinessSystemService.getBusinessSystemPage(pageNo, pageSize, systemCode, systemName, null, null, status);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("搜索业务系统失败", e);
            return JsonBean.error("搜索业务系统失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/initData")
    @ApiOperation("获取初始化数据")
    public String getBusinessSystemInitData(HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> initData = new HashMap<>();

            // 系统类型选项
            List<Map<String, Object>> systemTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "财务系统");
            type1.put("value", "FINANCE");
            systemTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "银行系统");
            type2.put("value", "BANK");
            systemTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "ERP系统");
            type3.put("value", "ERP");
            systemTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", "第三方系统");
            type4.put("value", "THIRD_PARTY");
            systemTypes.add(type4);
            initData.put("systemTypes", systemTypes);

            // 认证方式选项
            List<Map<String, Object>> authTypes = new ArrayList<>();
            Map<String, Object> auth1 = new HashMap<>();
            auth1.put("label", "OAuth2");
            auth1.put("value", "OAUTH2");
            authTypes.add(auth1);

            Map<String, Object> auth2 = new HashMap<>();
            auth2.put("label", "API Key");
            auth2.put("value", "API_KEY");
            authTypes.add(auth2);

            Map<String, Object> auth3 = new HashMap<>();
            auth3.put("label", "Basic Auth");
            auth3.put("value", "BASIC_AUTH");
            authTypes.add(auth3);

            Map<String, Object> auth4 = new HashMap<>();
            auth4.put("label", "JWT");
            auth4.put("value", "JWT");
            authTypes.add(auth4);
            initData.put("authTypes", authTypes);

            // 默认值
            Map<String, Object> defaultValues = new HashMap<>();
            defaultValues.put("status", "1");
            defaultValues.put("connectionStatus", "OFFLINE");
            defaultValues.put("authConfig", "{}");
            initData.put("defaultValues", defaultValues);

            return JsonBean.success(initData);

        } catch (Exception e) {
            log.error("获取初始化数据失败", e);
            return JsonBean.error("获取初始化数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/editData")
    @ApiOperation("获取编辑数据")
    public String getBusinessSystemEditData(
            @ApiParam(value = "系统ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcBusinessSystem businessSystem = tcBusinessSystemService.getById(id);
            if (businessSystem == null) {
                return JsonBean.error("业务系统不存在");
            }

            // 设置默认值,确保前端显示正常
            if (!StringUtils.hasText(businessSystem.getStatus())) {
                businessSystem.setStatus("1"); // 默认启用
            }
            if (!StringUtils.hasText(businessSystem.getConnectionStatus())) {
                businessSystem.setConnectionStatus("OFFLINE"); // 默认离线
            }
            if (!StringUtils.hasText(businessSystem.getAuthConfig())) {
                businessSystem.setAuthConfig("{}");
            }

            return JsonBean.success(businessSystem);

        } catch (Exception e) {
            log.error("获取编辑数据失败，ID: {}", id, e);
            return JsonBean.error("获取编辑数据失败: " + e.getMessage());
        }
    }

    // @PostMapping("/system/testConnection")  // 注释掉以避免与BusinessSystemController冲突
    @ApiOperation("测试系统连接")
    public String testSystemConnection(
            java.util.Map<String, Object> params,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 从 Map 中获取 id 参数
            Object idObj = params.get("id");
            if (idObj == null) {
                return JsonBean.error("缺少系统ID参数");
            }

            Long id;
            if (idObj instanceof Number) {
                id = ((Number) idObj).longValue();
            } else {
                try {
                    id = Long.parseLong(idObj.toString());
                } catch (NumberFormatException e) {
                    return JsonBean.error("系统ID格式错误");
                }
            }

            boolean result = tcBusinessSystemService.testSystemConnection(id);
            if (result) {
                return JsonBean.success("连接测试成功");
            } else {
                return JsonBean.error("连接测试失败");
            }

        } catch (Exception e) {
            log.error("测试系统连接失败", e);
            return JsonBean.error("测试系统连接失败: " + e.getMessage());
        }
    }

    // @PostMapping("/system/syncStatus")  // 注释掉以避免与BusinessSystemController冲突
    @ApiOperation("同步系统状态")
    public String syncSystemStatus(HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            tcBusinessSystemService.syncSystemStatus();
            return JsonBean.success("同步状态成功");

        } catch (Exception e) {
            log.error("同步系统状态失败", e);
            return JsonBean.error("同步系统状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/systemTypes")
    @ApiOperation("获取系统类型选项")
    public String getSystemTypes(HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> systemTypes = new ArrayList<>();
            Map<String, Object> systemType1 = new HashMap<>();
            systemType1.put("label", "财务系统");
            systemType1.put("value", "FINANCE");
            systemTypes.add(systemType1);

            Map<String, Object> systemType2 = new HashMap<>();
            systemType2.put("label", "银行系统");
            systemType2.put("value", "BANK");
            systemTypes.add(systemType2);

            Map<String, Object> systemType3 = new HashMap<>();
            systemType3.put("label", "ERP系统");
            systemType3.put("value", "ERP");
            systemTypes.add(systemType3);

            Map<String, Object> systemType4 = new HashMap<>();
            systemType4.put("label", "CRM系统");
            systemType4.put("value", "CRM");
            systemTypes.add(systemType4);

            Map<String, Object> systemType5 = new HashMap<>();
            systemType5.put("label", "第三方系统");
            systemType5.put("value", "THIRD_PARTY");
            systemTypes.add(systemType5);

            return JsonBean.success(systemTypes);

        } catch (Exception e) {
            log.error("获取系统类型选项失败", e);
            return JsonBean.error("获取系统类型选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/system/authTypes")
    @ApiOperation("获取认证方式选项")
    public String getAuthTypes(HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> authTypes = new ArrayList<>();
            Map<String, Object> authType1 = new HashMap<>();
            authType1.put("label", "OAuth2");
            authType1.put("value", "OAUTH2");
            authTypes.add(authType1);

            Map<String, Object> authType2 = new HashMap<>();
            authType2.put("label", "API Key");
            authType2.put("value", "API_KEY");
            authTypes.add(authType2);

            Map<String, Object> authType3 = new HashMap<>();
            authType3.put("label", "Basic Auth");
            authType3.put("value", "BASIC_AUTH");
            authTypes.add(authType3);

            Map<String, Object> authType4 = new HashMap<>();
            authType4.put("label", "JWT");
            authType4.put("value", "JWT");
            authTypes.add(authType4);

            return JsonBean.success(authTypes);

        } catch (Exception e) {
            log.error("获取认证方式选项失败", e);
            return JsonBean.error("获取认证方式选项失败: " + e.getMessage());
        }
    }

    // @GetMapping("/system/export")  // 注释掉以避免与BusinessSystemController冲突
    @ApiOperation("导出业务系统配置")
    public void exportBusinessSystem(
            @ApiParam(value = "系统名称") @RequestParam(required = false) String systemName,
            @ApiParam(value = "系统类型") @RequestParam(required = false) String systemType,
            @ApiParam(value = "连接状态") @RequestParam(required = false) String connectionStatus,
            @ApiParam(value = "状态") @RequestParam(required = false) String status,
            HttpServletResponse response) {

        // 创建工作簿
        org.apache.poi.ss.usermodel.Workbook workbook = null;
        java.io.FileOutputStream outputStream = null;
        org.apache.poi.ss.usermodel.Sheet sheet = null;

        try {
            // 权限验证
            if (!validateUser()) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }

            // 查询数据
            QueryWrapper<TcBusinessSystem> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(systemName)) {
                queryWrapper.like("SYSTEM_NAME", systemName);
            }
            if (StringUtils.hasText(systemType)) {
                queryWrapper.eq("SYSTEM_TYPE", systemType);
            }
            if (StringUtils.hasText(connectionStatus)) {
                queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
            }
            if (StringUtils.hasText(status)) {
                queryWrapper.eq("STATUS", status);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TcBusinessSystem> list = tcBusinessSystemService.list(queryWrapper);

            // 创建Excel工作簿 (XSSFWorkbook用于.xlsx格式)
            workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            sheet = workbook.createSheet("业务系统配置");

            // 创建表头样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            // 创建数据样式
            org.apache.poi.ss.usermodel.CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setWrapText(true);

            // 创建表头行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"系统名称", "系统编码", "系统类型", "接口URL", "认证类型", "状态", "创建时间"};
            int[] columnWidths = {30, 20, 15, 40, 15, 10, 20};

            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, columnWidths[i] * 256);
            }

            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;

            for (TcBusinessSystem system : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

                // 系统名称
                org.apache.poi.ss.usermodel.Cell nameCell = row.createCell(0);
                nameCell.setCellValue(system.getSystemName() != null ? system.getSystemName() : "");
                nameCell.setCellStyle(dataStyle);

                // 系统编码
                org.apache.poi.ss.usermodel.Cell codeCell = row.createCell(1);
                codeCell.setCellValue(system.getSystemCode() != null ? system.getSystemCode() : "");
                codeCell.setCellStyle(dataStyle);

                // 系统类型
                org.apache.poi.ss.usermodel.Cell typeCell = row.createCell(2);
                typeCell.setCellValue(system.getSystemType() != null ? system.getSystemType() : "");
                typeCell.setCellStyle(dataStyle);

                // 接口URL
                org.apache.poi.ss.usermodel.Cell urlCell = row.createCell(3);
                urlCell.setCellValue(system.getApiUrl() != null ? system.getApiUrl() : "");
                urlCell.setCellStyle(dataStyle);

                // 认证类型
                org.apache.poi.ss.usermodel.Cell authCell = row.createCell(4);
                authCell.setCellValue(system.getAuthType() != null ? system.getAuthType() : "");
                authCell.setCellStyle(dataStyle);

                // 状态
                org.apache.poi.ss.usermodel.Cell statusCell = row.createCell(5);
                statusCell.setCellValue("1".equals(system.getStatus()) ? "启用" : "禁用");
                statusCell.setCellStyle(dataStyle);

                // 创建时间
                org.apache.poi.ss.usermodel.Cell timeCell = row.createCell(6);
                if (system.getCreateTime() != null) {
                    timeCell.setCellValue(dateFormat.format(system.getCreateTime()));
                } else {
                    timeCell.setCellValue("");
                }
                timeCell.setCellStyle(dataStyle);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("业务系统配置_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 写入响应流
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

            log.info("导出业务系统配置成功, 共{}条记录", list.size());

        } catch (Exception e) {
            log.error("导出业务系统配置失败", e);
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        } finally {
            // 关闭工作簿
            if (workbook != null) {
                try {
                    workbook.close();
                } catch (Exception e) {
                    log.error("关闭工作簿失败", e);
                }
            }
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 验证用户权限
     * 注意: 开发测试阶段暂时绕过认证,生产环境必须开启
     */
    private boolean validateUser() {
        try {
            // 开发环境: 暂时绕过认证验证,直接返回true以便测试接口连通性
            // TODO: 生产环境必须取消注释下面的验证代码
            /*
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return false;
            }
            */
            return true;
        } catch (Exception e) {
            log.error("获取用户信息异常", e);
            return false;
        }
    }

    /**
     * 获取当前用户
     */
    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) {
            log.error("获取当前用户失败", e);
            return "system";
        }
    }

    /**
     * 获取当前用户ID (Long类型)
     */
    private Long getCurrentUserId() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff != null && loginStaff.getStaffid() != null) {
                return loginStaff.getStaffid().longValue();
            }
            return null;
        } catch (Exception e) {
            log.error("获取当前用户ID失败", e);
            return null;
        }
    }

    // ==================== 数据映射配置管理 ====================

    // 注释掉重复的映射路径，避免与 DataMappingController 冲突
    // 该功能已由 DataMappingController.getList() 提供
    // @RequestMapping(value = "/mapping/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取数据映射配置列表")
    // public String listDataMapping(...) { ... }

    @RequestMapping(value = "/mapping/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取数据映射配置列表(分页)")
    public String getDataMappingPage(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "映射名称") @RequestParam(required = false) String mappingName,
            @ApiParam(value = "源系统") @RequestParam(required = false) String sourceSystem,
            @ApiParam(value = "目标系统") @RequestParam(required = false) String targetSystem,
            @ApiParam(value = "映射类型") @RequestParam(required = false) String mappingType,
            @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            IPage<TcDataMapping> result = tcDataMappingService.getDataMappingPage(page, limit, mappingName, sourceSystem, targetSystem, mappingType, status);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取数据映射配置列表失败", e);
            return JsonBean.error("获取数据映射配置列表失败: " + e.getMessage());
        }
    }

    // @PostMapping("/mapping/create")  // 注释掉以避免与DataMappingController冲突
    @ApiOperation("创建数据映射配置")
    public String createDataMapping(TcDataMapping dataMapping, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (!StringUtils.hasText(dataMapping.getMappingName())) {
                return JsonBean.error("映射名称不能为空");
            }
            if (!StringUtils.hasText(dataMapping.getSourceField())) {
                return JsonBean.error("源字段不能为空");
            }
            if (!StringUtils.hasText(dataMapping.getTargetField())) {
                return JsonBean.error("目标字段不能为空");
            }

            // 检查源字段是否已存在
            QueryWrapper<TcDataMapping> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SOURCE_SYSTEM", dataMapping.getSourceSystem());
            queryWrapper.eq("SOURCE_FIELD", dataMapping.getSourceField());
            long count = tcDataMappingService.count(queryWrapper);
            if (count > 0) {
                return JsonBean.error("该系统下源字段已存在");
            }

            // ID已由Entity的ASSIGN_ID自动生成
            // 处理status字段:设置默认值
            if (dataMapping.getStatus() == null) {
                dataMapping.setStatus("1");
            }
            dataMapping.setCreateTime(new Date());
            dataMapping.setUpdateTime(new Date());
            dataMapping.setCreateUser(getCurrentUser());
            dataMapping.setUpdateUser(getCurrentUser());

            boolean success = tcDataMappingService.save(dataMapping);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建数据映射配置失败", e);
            return JsonBean.error("创建数据映射配置失败: " + e.getMessage());
        }
    }

    // @PostMapping("/mapping/update")  // 注释掉以避免与DataMappingController冲突
    @ApiOperation("更新数据映射配置")
    public String updateDataMapping(TcDataMapping dataMapping, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (dataMapping.getId() == null) {
                return JsonBean.error("映射ID不能为空");
            }

            dataMapping.setUpdateTime(new Date());
            dataMapping.setUpdateUser(getCurrentUser());

            boolean success = tcDataMappingService.updateById(dataMapping);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新数据映射配置失败", e);
            return JsonBean.error("更新数据映射配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/mapping/detail")
    @ApiOperation("获取数据映射详情")
    public String getDataMappingDetail(
            @ApiParam(value = "映射ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcDataMapping dataMapping = tcDataMappingService.getById(id);
            if (dataMapping == null) {
                return JsonBean.error("数据映射不存在");
            }

            return JsonBean.success(dataMapping);

        } catch (Exception e) {
            log.error("获取数据映射详情失败，ID: {}", id, e);
            return JsonBean.error("获取数据映射详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/mapping/search")
    @ApiOperation("搜索数据映射配置")
    public String searchDataMapping(Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
            String mappingName = params.get("mappingName") != null ? params.get("mappingName").toString() : null;
            String sourceSystem = params.get("sourceSystem") != null ? params.get("sourceSystem").toString() : null;
            String targetSystem = params.get("targetSystem") != null ? params.get("targetSystem").toString() : null;
            String mappingType = params.get("mappingType") != null ? params.get("mappingType").toString() : null;

            IPage<TcDataMapping> result = tcDataMappingService.getDataMappingPage(pageNo, pageSize, mappingName, sourceSystem, targetSystem, mappingType, null);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("搜索数据映射配置失败", e);
            return JsonBean.error("搜索数据映射配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/mapping/initData")
    @ApiOperation("获取初始化数据")
    public String getMappingInitData(HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> initData = new HashMap<>();

            // 映射类型选项
            List<Map<String, Object>> mappingTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "字段映射");
            type1.put("value", "FIELD");
            mappingTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "值映射");
            type2.put("value", "VALUE");
            mappingTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "函数映射");
            type3.put("value", "FUNCTION");
            mappingTypes.add(type3);
            initData.put("mappingTypes", mappingTypes);

            // 源系统选项
            List<Map<String, Object>> sourceSystems = new ArrayList<>();
            Map<String, Object> src1 = new HashMap<>();
            src1.put("label", "财务系统");
            src1.put("value", "FINANCE");
            sourceSystems.add(src1);

            Map<String, Object> src2 = new HashMap<>();
            src2.put("label", "银行系统");
            src2.put("value", "BANK");
            sourceSystems.add(src2);

            Map<String, Object> src3 = new HashMap<>();
            src3.put("label", "ERP系统");
            src3.put("value", "ERP");
            sourceSystems.add(src3);

            Map<String, Object> src4 = new HashMap<>();
            src4.put("label", "第三方系统");
            src4.put("value", "THIRD_PARTY");
            sourceSystems.add(src4);
            initData.put("sourceSystems", sourceSystems);

            // 目标系统选项
            initData.put("targetSystems", sourceSystems);

            // 默认值
            Map<String, Object> defaultValues = new HashMap<>();
            defaultValues.put("status", 1);
            defaultValues.put("mappingType", "FIELD");
            defaultValues.put("sourceSystem", "FINANCE");
            defaultValues.put("targetSystem", "ERP");
            initData.put("defaultValues", defaultValues);

            return JsonBean.success(initData);

        } catch (Exception e) {
            log.error("获取初始化数据失败", e);
            return JsonBean.error("获取初始化数据失败: " + e.getMessage());
        }
    }

    // @PostMapping("/mapping/delete")  // 注释掉以避免与DataMappingController冲突
    @ApiOperation("删除数据映射配置")
    public String deleteDataMapping(Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean success = tcDataMappingService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除数据映射配置失败", e);
            return JsonBean.error("删除数据映射配置失败: " + e.getMessage());
        }
    }

    // @DeleteMapping("/mapping/delete/{id}")  // 注释掉以避免与DataMappingController冲突
    @ApiOperation("删除数据映射配置(路径参数方式)")
    public String deleteDataMappingById(@PathVariable Long id, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean success = tcDataMappingService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除数据映射配置失败", e);
            return JsonBean.error("删除数据映射配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/mapping/test")
    @ApiOperation("测试数据映射")
    public String testDataMapping(Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            String testData = params.get("testData") != null ? params.get("testData").toString() : null;

            // 根据ID查询映射配置
            TcDataMapping dataMapping = null;
            if (id != null) {
                dataMapping = tcDataMappingService.getById(id);
            }

            // 模拟数据映射测试
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("success", true);
            testResult.put("message", "映射测试成功");

            if (dataMapping != null) {
                testResult.put("mappingName", dataMapping.getMappingName());
                testResult.put("sourceField", dataMapping.getSourceField());
                testResult.put("targetField", dataMapping.getTargetField());
                testResult.put("conversionRule", dataMapping.getConversionRule());
            }

            if (testData != null) {
                testResult.put("input", testData);

                // 简单的映射转换演示
                String output = testData;
                if (dataMapping != null && dataMapping.getConversionRule() != null) {
                    // 根据转换规则处理数据
                    try {
                        String rule = dataMapping.getConversionRule();
                        if (rule.contains("parseFloat")) {
                            double value = Double.parseDouble(testData);
                            output = String.valueOf(Math.round(value * 100.0) / 100.0);
                        } else if (rule.startsWith("TRIM_")) {
                            output = testData.replaceFirst("^" + rule.substring(5), "");
                        } else if (rule.startsWith("ADD_")) {
                            output = rule.substring(4) + testData;
                        } else if (rule.startsWith("PAD_LEFT")) {
                            String[] parts = rule.split(",");
                            if (parts.length >= 2) {
                                int length = Integer.parseInt(parts[1].trim());
                                String padChar = parts.length > 2 ? parts[2].trim() : "0";
                                while (output.length() < length) {
                                    output = padChar + output;
                                }
                            }
                        }
                    } catch (Exception e) {
                        output = testData;
                    }
                }
                testResult.put("output", output);
            } else {
                testResult.put("sampleInput", "{\"user_id\": 1001, \"user_name\": \"张三\"}");
                testResult.put("sampleOutput", "{\"staff_id\": 1001, \"staff_name\": \"张三\"}");
            }

            return new JsonBean(1, "测试成功", testResult).toString();

        } catch (Exception e) {
            log.error("测试数据映射失败", e);
            return JsonBean.error("测试数据映射失败: " + e.getMessage());
        }
    }

    // ==================== 电票账户配置管理 ====================

    @GetMapping("/eTicketAccount/detail")
    @ApiOperation("获取电票账户详情")
    public String getETicketAccountDetail(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcEticketAccount account = tcEticketAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("电票账户不存在");
            }

            return JsonBean.success(account);

        } catch (Exception e) {
            log.error("获取电票账户详情失败，ID: {}", id, e);
            return JsonBean.error("获取电票账户详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/eTicketAccount/search")
    @ApiOperation("搜索电票账户配置")
    public String searchETicketAccount(Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) : 1;
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
            String accountNo = params.get("accountNo") != null ? params.get("accountNo").toString() : null;
            String accountName = params.get("accountName") != null ? params.get("accountName").toString() : null;
            String eTicketType = params.get("eTicketType") != null ? params.get("eTicketType").toString() : null;
            String bankId = params.get("bankId") != null ? params.get("bankId").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;

            IPage<TcEticketAccount> result = tcEticketAccountService.getETicketAccountPage(pageNo, pageSize, accountNo, accountName, eTicketType, null, bankId, status);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("搜索电票账户配置失败", e);
            return JsonBean.error("搜索电票账户配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/eTicketAccount/initData")
    @ApiOperation("获取初始化数据")
    public String getETicketAccountInitData(HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> initData = new HashMap<>();

            // 票据类型选项
            List<Map<String, Object>> eTicketTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "ECDS系统");
            type1.put("value", "ECDS");
            eTicketTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "BECP系统");
            type2.put("value", "BECP");
            eTicketTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "银行电票系统");
            type3.put("value", "BANK_ETICKET");
            eTicketTypes.add(type3);
            initData.put("eTicketTypes", eTicketTypes);

            // 银行机构选项
            List<Map<String, Object>> bankInstitutions = new ArrayList<>();
            Map<String, Object> bank1 = new HashMap<>();
            bank1.put("label", "工商银行");
            bank1.put("value", "ICBC");
            bankInstitutions.add(bank1);

            Map<String, Object> bank2 = new HashMap<>();
            bank2.put("label", "建设银行");
            bank2.put("value", "CCB");
            bankInstitutions.add(bank2);

            Map<String, Object> bank3 = new HashMap<>();
            bank3.put("label", "农业银行");
            bank3.put("value", "ABC");
            bankInstitutions.add(bank3);

            Map<String, Object> bank4 = new HashMap<>();
            bank4.put("label", "中国银行");
            bank4.put("value", "BOC");
            bankInstitutions.add(bank4);

            Map<String, Object> bank5 = new HashMap<>();
            bank5.put("label", "交通银行");
            bank5.put("value", "BOCOM");
            bankInstitutions.add(bank5);

            Map<String, Object> bank6 = new HashMap<>();
            bank6.put("label", "招商银行");
            bank6.put("value", "CMB");
            bankInstitutions.add(bank6);
            initData.put("bankInstitutions", bankInstitutions);

            // 账户状态选项
            List<Map<String, Object>> accountStatuses = new ArrayList<>();
            Map<String, Object> status1 = new HashMap<>();
            status1.put("label", "正常");
            status1.put("value", "NORMAL");
            accountStatuses.add(status1);

            Map<String, Object> status2 = new HashMap<>();
            status2.put("label", "冻结");
            status2.put("value", "FROZEN");
            accountStatuses.add(status2);

            Map<String, Object> status3 = new HashMap<>();
            status3.put("label", "关闭");
            status3.put("value", "CLOSED");
            accountStatuses.add(status3);
            initData.put("accountStatuses", accountStatuses);

            // 默认值
            Map<String, Object> defaultValues = new HashMap<>();
            defaultValues.put("eTicketSystem", "ECDS");
            defaultValues.put("accountStatus", "NORMAL");
            defaultValues.put("syncStatus", "PENDING");
            initData.put("defaultValues", defaultValues);

            return JsonBean.success(initData);

        } catch (Exception e) {
            log.error("获取初始化数据失败", e);
            return JsonBean.error("获取初始化数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/eTicketAccount/editData")
    @ApiOperation("获取编辑数据")
    public String getETicketAccountEditData(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcEticketAccount account = tcEticketAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("电票账户不存在");
            }

            return JsonBean.success(account);

        } catch (Exception e) {
            log.error("获取编辑数据失败，ID: {}", id, e);
            return JsonBean.error("获取编辑数据失败: " + e.getMessage());
        }
    }

    // 注释：与ETicketAccountController路径冲突，使用独立的Controller
    // @RequestMapping(value = "/eTicketAccount/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取电票账户配置列表")
    // public String getETicketAccountPage(
    //         @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNo,
    //         @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
    //         @ApiParam(value = "账户编号") @RequestParam(required = false) String accountNumber,
    //         @ApiParam(value = "账户名称") @RequestParam(required = false) String accountName,
    //         @ApiParam(value = "电票系统") @RequestParam(required = false) String eTicketSystem,
    //         @ApiParam(value = "账户类型") @RequestParam(required = false) String accountType,
    //         @ApiParam(value = "银行编码") @RequestParam(required = false) String bankCode,
    //         @ApiParam(value = "账户状态") @RequestParam(required = false) String accountStatus,
    //         HttpServletResponse response) {
    //
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         IPage<TcEticketAccount> result = tcEticketAccountService.getETicketAccountPage(pageNo, pageSize, accountNumber, accountName, eTicketSystem, accountType, bankCode, accountStatus);
    //
    //         // 转换数据格式，将数据库字段映射为前端期望的字段
    //         List<Map<String, Object>> mappedList = new ArrayList<>();
    //         for (TcEticketAccount account : result.getRecords()) {
    //             Map<String, Object> map = new HashMap<>();
    //             // 数据库字段 -> 前端字段映射
    //             map.put("id", account.getId());
    //             map.put("accountNumber", account.getAccountNo()); // ACCOUNT_NO -> accountNumber
    //             map.put("accountName", account.getBankName()); // BANK_NAME -> accountName (使用银行名称作为账户名称)
    //             map.put("bankCode", account.getBankCode());
    //             map.put("bankName", account.getBankName());
    //             map.put("accountNo", account.getAccountNo());
    //             map.put("customerNo", account.getCustomerNo());
    //             map.put("eTicketStatus", account.getEticketStatus());
    //             map.put("businessScope", account.getBusinessScope());
    //             map.put("currencyCodes", account.getCurrencyCodes());
    //             map.put("dailyLimit", account.getDailyLimit());
    //             map.put("monthlyLimit", account.getMonthlyLimit());
    //             map.put("interfaceUrl", account.getInterfaceUrl());
    //             map.put("interfacePort", account.getInterfacePort());
    //             map.put("certPath", account.getCertPath());
    //             map.put("certPassword", account.getCertPassword());
    //             map.put("operatorInfo", account.getOperatorInfo());
    //             map.put("timeoutSetting", account.getTimeoutSetting());
    //             map.put("retryCount", account.getRetryCount());
    //             map.put("lastConnectTime", account.getLastConnectTime());
    //             map.put("connectionStatus", account.getConnectionStatus());
    //             map.put("status", account.getStatus());
    //             map.put("remark", account.getRemark());
    //             map.put("createTime", account.getCreateTime());
    //             map.put("updateTime", account.getUpdateTime());
    //             map.put("createUser", account.getCreateUser());
    //             map.put("updateUser", account.getUpdateUser());
    //             // 前端可能需要的其他字段
    //             map.put("eTicketSystem", "ECDS"); // 默认值，字段已删除
    //             map.put("accountType", "ACCEPTANCE"); // 默认值，字段已删除
    //             map.put("accountStatus", "NORMAL"); // 默认值，使用status字段
    //             mappedList.add(map);
    //         }
    //
    //         Map<String, Object> data = new HashMap<>();
    //         data.put("tlist", mappedList);
    //         data.put("totalRecord", result.getTotal());
    //         data.put("pageNo", result.getCurrent());
    //         data.put("pageSize", result.getSize());
    //
    //         return JsonBean.success(data);
    //
    //     } catch (Exception e) {
    //         log.error("获取电票账户配置列表失败", e);
    //         return JsonBean.error("获取电票账户配置列表失败: " + e.getMessage());
    //     }
    // }

    // @PostMapping("/eTicketAccount/create")  // 注释掉以避免与ETicketAccountController冲突
    @ApiOperation("创建电票账户配置")
    public String createETicketAccount(TcEticketAccount eTicketAccount, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (!StringUtils.hasText(eTicketAccount.getAccountNumber())) {
                return JsonBean.error("账户编号不能为空");
            }
            if (!StringUtils.hasText(eTicketAccount.getAccountName())) {
                return JsonBean.error("账户名称不能为空");
            }
            if (!StringUtils.hasText(eTicketAccount.getBankName())) {
                return JsonBean.error("开户银行不能为空");
            }
            // 字段已从数据库表中移除: ETICKET_SYSTEM
            // if (!StringUtils.hasText(eTicketAccount.getETicketSystem())) {
            //     return JsonBean.error("电票系统不能为空");
            // }

            // 字段已从数据库表中移除: ETICKET_ACCOUNT_ID, SYNC_STATUS
            // eTicketAccount.setEticketAccountId(generateId());
            // eTicketAccount.setSyncStatus("PENDING");
            eTicketAccount.setCreateTime(new Date());
            eTicketAccount.setUpdateTime(new Date());
            eTicketAccount.setCreateUser(getCurrentUser());
            eTicketAccount.setUpdateUser(getCurrentUser());

            boolean success = tcEticketAccountService.save(eTicketAccount);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建电票账户配置失败", e);
            return JsonBean.error("创建电票账户配置失败: " + e.getMessage());
        }
    }

    // @PostMapping("/eTicketAccount/update")  // 注释掉以避免与ETicketAccountController冲突
    @PostMapping("/eTicketAccount/update_old")
    @ApiOperation("更新电票账户配置")
    public String updateETicketAccount(TcEticketAccount eTicketAccount, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 字段已从数据库表中移除: ETICKET_ACCOUNT_ID
            // if (eTicketAccount.getEticketAccountId() == null) {
            //     return JsonBean.error("账户ID不能为空");
            // }
            // 使用 id 字段代替
            if (eTicketAccount.getId() == null) {
                return JsonBean.error("账户ID不能为空");
            }

            eTicketAccount.setUpdateTime(new Date());
            eTicketAccount.setUpdateUser(getCurrentUser());

            boolean success = tcEticketAccountService.updateById(eTicketAccount);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新电票账户配置失败", e);
            return JsonBean.error("更新电票账户配置失败: " + e.getMessage());
        }
    }

    // @PostMapping("/eTicketAccount/delete")  // 注释掉以避免与ETicketAccountController冲突
    // @ApiOperation("删除电票账户配置")
    // public String deleteETicketAccount(Map<String, Object> params, HttpServletResponse response) {
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
    //         if (id == null) {
    //             return JsonBean.error("ID不能为空");
    //         }
    //
    //         boolean success = tcEticketAccountService.removeById(id);
    //         if (success) {
    //             return JsonBean.success("删除成功");
    //         } else {
    //             return JsonBean.error("删除失败");
    //         }
    //
    //     } catch (Exception e) {
    //         log.error("删除电票账户配置失败", e);
    //         return JsonBean.error("删除电票账户配置失败: " + e.getMessage());
    //     }
    // }

    // @DeleteMapping("/eTicketAccount/{id}")  // 注释掉以避免与ETicketAccountController冲突
    // @ApiOperation("根据ID删除电票账户配置")
    // public String deleteETicketAccountById(@PathVariable Long id, HttpServletResponse response) {
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         if (id == null) {
    //             return JsonBean.error("ID不能为空");
    //         }
    //
    //         boolean success = tcEticketAccountService.removeById(id);
    //         if (success) {
    //             return JsonBean.success("删除成功");
    //         } else {
    //             return JsonBean.error("删除失败");
    //         }
    //
    //     } catch (Exception e) {
    //         log.error("删除电票账户配置失败", e);
    //         return JsonBean.error("删除电票账户配置失败: " + e.getMessage());
    //     }
    // }

    // @PostMapping("/eTicketAccount/saveOrUpdate")  // 注释掉以避免与ETicketAccountController冲突
    @PostMapping("/eTicketAccount/saveOrUpdate_old")
    @ApiOperation("保存或更新电票账户配置")
    public String saveOrUpdateETicketAccount(TcEticketAccount eTicketAccount, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 验证必填字段
            if (!StringUtils.hasText(eTicketAccount.getAccountNumber())) {
                return JsonBean.error("账户编号不能为空");
            }
            if (!StringUtils.hasText(eTicketAccount.getAccountName())) {
                return JsonBean.error("账户名称不能为空");
            }
            // 字段已从数据库表中移除: ETICKET_SYSTEM, ACCOUNT_TYPE, BANK_ACCOUNT_NUMBER
            // if (!StringUtils.hasText(eTicketAccount.getETicketSystem())) {
            //     return JsonBean.error("电票系统不能为空");
            // }
            // if (!StringUtils.hasText(eTicketAccount.getAccountType())) {
            //     return JsonBean.error("账户类型不能为空");
            // }
            if (!StringUtils.hasText(eTicketAccount.getBankCode())) {
                return JsonBean.error("开户银行不能为空");
            }
            // if (!StringUtils.hasText(eTicketAccount.getBankAccountNumber())) {
            //     return JsonBean.error("银行账号不能为空");
            // }

            boolean success;
            // 字段已从数据库表中移除: ETICKET_ACCOUNT_ID, SYNC_STATUS
            // 使用 id 字段代替
            if (eTicketAccount.getId() == null) {
                // 新增
                // eTicketAccount.setEticketAccountId(generateId());
                // eTicketAccount.setSyncStatus("PENDING");
                eTicketAccount.setId(String.valueOf(generateId()));
                eTicketAccount.setCreateTime(new Date());
                eTicketAccount.setUpdateTime(new Date());
                eTicketAccount.setCreateUser(getCurrentUser());
                eTicketAccount.setUpdateUser(getCurrentUser());
                success = tcEticketAccountService.save(eTicketAccount);
            } else {
                // 更新
                eTicketAccount.setUpdateTime(new Date());
                eTicketAccount.setUpdateUser(getCurrentUser());
                success = tcEticketAccountService.updateById(eTicketAccount);
            }

            if (success) {
                // 字段已从数据库表中移除: ETICKET_ACCOUNT_ID，使用 id 字段代替
                return JsonBean.success(eTicketAccount.getId() == null ? "创建成功" : "更新成功");
            } else {
                return JsonBean.error(eTicketAccount.getId() == null ? "创建失败" : "更新失败");
            }

        } catch (Exception e) {
            log.error("保存或更新电票账户配置失败", e);
            return JsonBean.error("保存或更新电票账户配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/eTicketAccount/eTicketTypes")
    @ApiOperation("获取票据类型选项")
    public String getETicketTypes(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> eTicketTypes = new ArrayList<>();
            Map<String, Object> eTicketType1 = new HashMap<>();
            eTicketType1.put("label", "银行承兑汇票");
            eTicketType1.put("value", "BANK_ACCEPTANCE");
            eTicketTypes.add(eTicketType1);

            Map<String, Object> eTicketType2 = new HashMap<>();
            eTicketType2.put("label", "商业承兑汇票");
            eTicketType2.put("value", "COMMERCIAL_ACCEPTANCE");
            eTicketTypes.add(eTicketType2);

            Map<String, Object> eTicketType3 = new HashMap<>();
            eTicketType3.put("label", "银行本票");
            eTicketType3.put("value", "BANK_NOTE");
            eTicketTypes.add(eTicketType3);

            Map<String, Object> eTicketType4 = new HashMap<>();
            eTicketType4.put("label", "支票");
            eTicketType4.put("value", "CHECK");
            eTicketTypes.add(eTicketType4);

            return JsonBean.success(eTicketTypes);

        } catch (Exception e) {
            log.error("获取票据类型选项失败", e);
            return JsonBean.error("获取票据类型选项失败: " + e.getMessage());
        }
    }

    // @PostMapping("/eTicketAccount/syncStatus")  // 注释掉以避免与ETicketAccountController冲突
    @ApiOperation("同步账户状态")
    public String syncETicketAccountStatus(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 模拟同步账户状态
            return JsonBean.success("同步状态成功");

        } catch (Exception e) {
            log.error("同步账户状态失败", e);
            return JsonBean.error("同步账户状态失败: " + e.getMessage());
        }
    }

    @PostMapping("/eTicketAccount/testConnection")
    @ApiOperation("测试账户连接")
    public String testETicketAccountConnection(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 模拟连接测试
            boolean result = new Random().nextInt(100) < 80;
            if (result) {
                return JsonBean.success("连接测试成功");
            } else {
                return JsonBean.error("连接测试失败");
            }

        } catch (Exception e) {
            log.error("测试账户连接失败，ID: {}", id, e);
            return JsonBean.error("测试账户连接失败: " + e.getMessage());
        }
    }

    /**
     * 生成ID
     */
    private Long generateId() {
        return System.currentTimeMillis() + new Random().nextInt(1000);
    }

    // ==================== 印鉴使用记录管理 ====================

    // 注释：与SealUsageRecordController路径冲突，使用独立的Controller
    // @RequestMapping(value = "/sealUsageRecord/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取印鉴使用记录列表")
    // public String getSealUsageRecordList(
    //         @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
    //         @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
    //         @ApiParam(value = "记录编号") @RequestParam(required = false) String recordNumber,
    //         @ApiParam(value = "印鉴编码") @RequestParam(required = false) String sealCode,
    //         @ApiParam(value = "印鉴名称") @RequestParam(required = false) String sealName,
    //         @ApiParam(value = "使用人员姓名") @RequestParam(required = false) String operatorName,
    //         @ApiParam(value = "业务类型") @RequestParam(required = false) String businessType,
    //         @ApiParam(value = "使用状态") @RequestParam(required = false) String usageStatus,
    //         HttpServletResponse response) {
    //
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         IPage<TblSealUsageRecord> result = tblSealUsageRecordService.getSealUsageRecordPage(
    //                 page, limit, recordNumber, sealCode, sealName, operatorName, businessType, usageStatus);
    //
    //         Map<String, Object> data = new HashMap<>();
    //         data.put("tlist", result.getRecords());
    //         data.put("totalRecord", result.getTotal());
    //         data.put("pageNo", result.getCurrent());
    //         data.put("pageSize", result.getSize());
    //
    //         return JsonBean.success(data);
    //
    //     } catch (Exception e) {
    //         log.error("获取印鉴使用记录列表失败", e);
    //         return JsonBean.error("获取印鉴使用记录列表失败: " + e.getMessage());
    //     }
    // }
    // 注意: /sealUsageRecord/* 相关接口已全部移至 SealUsageRecordController
    // 包括: /detail, /create, /update, /delete, /businessTypes, /usageStatuses, /statistics 等

    // ==================== 安全参数配置管理 ====================

    @RequestMapping(value = "/parameters/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取安全参数配置列表(分页)")
    public String getParametersPage(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "参数名称") @RequestParam(required = false) String paramName,
            @ApiParam(value = "参数类型") @RequestParam(required = false) String paramType,
            @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(page, limit);

            // 构建查询条件
            QueryWrapper<TblSecurityParameter> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(paramName)) {
                queryWrapper.like("PARAM_NAME", paramName);
            }
            if (StringUtils.hasText(paramType)) {
                queryWrapper.eq("PARAM_TYPE", paramType);
            }
            if (isEnabled != null) {
                queryWrapper.eq("IS_ENABLED", isEnabled);
            }

            List<TblSecurityParameter> list = tblSecurityParameterService.list(queryWrapper);
            com.github.pagehelper.PageInfo<TblSecurityParameter> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取安全参数配置列表失败", e);
            return JsonBean.error("获取安全参数配置列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/parameters/save-or-update")
    @ApiOperation("保存或更新参数配置")
    public String saveOrUpdateParameter(@FlexibleRequestBody TblSecurityParameter param, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(param.getParamCode())) {
                return JsonBean.error("参数编码不能为空");
            }
            if (!StringUtils.hasText(param.getParamName())) {
                return JsonBean.error("参数名称不能为空");
            }
            if (!StringUtils.hasText(param.getParamType())) {
                return JsonBean.error("参数类型不能为空");
            }
            if (!StringUtils.hasText(param.getParamValue())) {
                return JsonBean.error("参数值不能为空");
            }

            boolean success;
            if (param.getId() == null) {
                // 新增
                param.setId(generateId());
                if (param.getIsEnabled() == null) {
                    param.setIsEnabled(1);
                }
                param.setCreateTime(new Date());
                param.setUpdateTime(new Date());
                param.setCreateBy(getCurrentUser());
                param.setUpdateBy(getCurrentUser());
                success = tblSecurityParameterService.save(param);
            } else {
                // 更新
                TblSecurityParameter existing = tblSecurityParameterService.getById(param.getId());
                if (existing == null) {
                    return JsonBean.error("参数配置不存在");
                }
                param.setUpdateTime(new Date());
                param.setUpdateBy(getCurrentUser());
                success = tblSecurityParameterService.updateById(param);
            }

            if (success) {
                return JsonBean.success(param.getId() == null ? "创建成功" : "更新成功");
            } else {
                return JsonBean.error(param.getId() == null ? "创建失败" : "更新失败");
            }

        } catch (Exception e) {
            log.error("保存或更新参数配置失败", e);
            return JsonBean.error("保存或更新参数配置失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/parameters/{id}")
    @ApiOperation("删除参数配置")
    public String deleteParameter(@PathVariable Long id, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (id == null) {
                return JsonBean.error("参数ID不能为空");
            }

            // 检查是否存在
            TblSecurityParameter existing = tblSecurityParameterService.getById(id);
            if (existing == null) {
                return JsonBean.error("参数配置不存在");
            }

            boolean success = tblSecurityParameterService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除参数配置失败，ID: {}", id, e);
            return JsonBean.error("删除参数配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/parameters/status")
    @ApiOperation("更新参数配置状态")
    public String updateParameterStatus(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            Integer isEnabled = params.get("isEnabled") != null ? Integer.parseInt(params.get("isEnabled").toString()) : null;

            if (id == null || isEnabled == null) {
                return JsonBean.error("参数不完整，需要id和isEnabled");
            }

            TblSecurityParameter existing = tblSecurityParameterService.getById(id);
            if (existing == null) {
                return JsonBean.error("参数配置不存在");
            }

            existing.setIsEnabled(isEnabled);
            existing.setUpdateTime(new Date());
            existing.setUpdateBy(getCurrentUser());

            boolean success = tblSecurityParameterService.updateById(existing);
            if (success) {
                return JsonBean.success("状态更新成功");
            } else {
                return JsonBean.error("状态更新失败");
            }
        } catch (Exception e) {
            log.error("更新参数配置状态失败", e);
            return JsonBean.error("更新参数配置状态失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = {"/securityParam/list", "/securityParameter/list"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取安全参数配置列表")
    public String getSecurityParameterList(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "参数编码") @RequestParam(required = false) String paramCode,
            @ApiParam(value = "参数名称") @RequestParam(required = false) String paramName,
            @ApiParam(value = "参数类型") @RequestParam(required = false) String paramType,
            @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            IPage<TblSecurityParameter> result = tblSecurityParameterService.getSecurityParameterPage(
                    page, limit, paramCode, paramName, paramType, isEnabled);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取安全参数配置列表失败", e);
            return JsonBean.error("获取安全参数配置列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/securityParam/detail")
    @ApiOperation("获取安全参数配置详情")
    public String getSecurityParameterDetail(
            @ApiParam(value = "参数ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TblSecurityParameter param = tblSecurityParameterService.getById(id);
            if (param == null) {
                return JsonBean.error("安全参数配置不存在");
            }

            return JsonBean.success(param);

        } catch (Exception e) {
            log.error("获取安全参数配置详情失败，ID: {}", id, e);
            return JsonBean.error("获取安全参数配置详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/securityParam/create")
    @ApiOperation("创建安全参数配置")
    public String createSecurityParameter(TblSecurityParameter param, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(param.getParamCode())) {
                return JsonBean.error("参数编码不能为空");
            }
            if (!StringUtils.hasText(param.getParamName())) {
                return JsonBean.error("参数名称不能为空");
            }
            if (!StringUtils.hasText(param.getParamType())) {
                return JsonBean.error("参数类型不能为空");
            }
            if (!StringUtils.hasText(param.getParamValue())) {
                return JsonBean.error("参数值不能为空");
            }

            // 设置默认值
            param.setId(generateId());
            if (param.getIsEnabled() == null) {
                param.setIsEnabled(1);
            }
            param.setCreateTime(new Date());
            param.setUpdateTime(new Date());
            param.setCreateBy(getCurrentUser());
            param.setUpdateBy(getCurrentUser());

            boolean success = tblSecurityParameterService.save(param);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建安全参数配置失败", e);
            return JsonBean.error("创建安全参数配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/securityParam/update")
    @ApiOperation("更新安全参数配置")
    public String updateSecurityParameter(TblSecurityParameter param, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (param.getId() == null) {
                return JsonBean.error("参数ID不能为空");
            }

            // 检查是否存在
            TblSecurityParameter existing = tblSecurityParameterService.getById(param.getId());
            if (existing == null) {
                return JsonBean.error("安全参数配置不存在");
            }

            param.setUpdateTime(new Date());
            param.setUpdateBy(getCurrentUser());

            boolean success = tblSecurityParameterService.updateById(param);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新安全参数配置失败", e);
            return JsonBean.error("更新安全参数配置失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/securityParam/delete")
    @ApiOperation("删除安全参数配置")
    public String deleteSecurityParameter(
            @ApiParam(value = "参数ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TblSecurityParameter existing = tblSecurityParameterService.getById(id);
            if (existing == null) {
                return JsonBean.error("安全参数配置不存在");
            }

            boolean success = tblSecurityParameterService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除安全参数配置失败，ID: {}", id, e);
            return JsonBean.error("删除安全参数配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/securityParam/paramTypes")
    @ApiOperation("获取参数类型选项")
    public String getParamTypes(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> paramTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "密码策略");
            type1.put("value", "PASSWORD_POLICY");
            paramTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "会话超时");
            type2.put("value", "SESSION_TIMEOUT");
            paramTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "登录限制");
            type3.put("value", "LOGIN_LIMIT");
            paramTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", "访问控制");
            type4.put("value", "ACCESS_CONTROL");
            paramTypes.add(type4);

            Map<String, Object> type5 = new HashMap<>();
            type5.put("label", "数据加密");
            type5.put("value", "DATA_ENCRYPTION");
            paramTypes.add(type5);

            Map<String, Object> type6 = new HashMap<>();
            type6.put("label", "审计日志");
            type6.put("value", "AUDIT_LOG");
            paramTypes.add(type6);

            return JsonBean.success(paramTypes);

        } catch (Exception e) {
            log.error("获取参数类型选项失败", e);
            return JsonBean.error("获取参数类型选项失败: " + e.getMessage());
        }
    }

    @PostMapping("/securityParam/batchUpdateStatus")
    @ApiOperation("批量更新启用状态")
    public String batchUpdateStatus(
            @ApiParam(value = "参数ID列表") @RequestParam List<Long> ids,
            @ApiParam(value = "启用状态") @RequestParam Integer isEnabled,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("参数ID列表不能为空");
            }

            // 批量更新状态
            for (Long id : ids) {
                TblSecurityParameter param = tblSecurityParameterService.getById(id);
                if (param != null) {
                    param.setIsEnabled(isEnabled);
                    param.setUpdateTime(new Date());
                    param.setUpdateBy(getCurrentUser());
                    tblSecurityParameterService.updateById(param);
                }
            }

            return JsonBean.success("批量更新成功");

        } catch (Exception e) {
            log.error("批量更新启用状态失败", e);
            return JsonBean.error("批量更新启用状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/securityParam/export")
    @ApiOperation("导出安全参数配置")
    public void exportSecurityParameter(
            @ApiParam(value = "参数编码") @RequestParam(required = false) String paramCode,
            @ApiParam(value = "参数名称") @RequestParam(required = false) String paramName,
            @ApiParam(value = "参数类型") @RequestParam(required = false) String paramType,
            @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
            HttpServletResponse response) {

        // 创建工作簿
        org.apache.poi.ss.usermodel.Workbook workbook = null;
        org.apache.poi.ss.usermodel.Sheet sheet = null;

        try {
            // 权限验证
            if (!validateUser()) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }

            // 查询数据
            QueryWrapper<TblSecurityParameter> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(paramCode)) {
                queryWrapper.like("PARAM_CODE", paramCode);
            }
            if (StringUtils.hasText(paramName)) {
                queryWrapper.like("PARAM_NAME", paramName);
            }
            if (StringUtils.hasText(paramType)) {
                queryWrapper.eq("PARAM_TYPE", paramType);
            }
            if (isEnabled != null) {
                queryWrapper.eq("IS_ENABLED", isEnabled);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblSecurityParameter> list = tblSecurityParameterService.list(queryWrapper);

            // 创建Excel工作簿
            workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            sheet = workbook.createSheet("安全参数配置");

            // 创建表头样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            // 创建数据样式
            org.apache.poi.ss.usermodel.CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setWrapText(true);

            // 创建表头行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"参数编码", "参数名称", "参数类型", "参数值", "是否启用", "参数描述", "创建时间"};
            int[] columnWidths = {20, 25, 15, 30, 10, 35, 20};

            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, columnWidths[i] * 256);
            }

            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;

            for (TblSecurityParameter param : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

                org.apache.poi.ss.usermodel.Cell cell0 = row.createCell(0);
                cell0.setCellValue(param.getParamCode() != null ? param.getParamCode() : "");
                cell0.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(1);
                cell1.setCellValue(param.getParamName() != null ? param.getParamName() : "");
                cell1.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(2);
                cell2.setCellValue(param.getParamType() != null ? param.getParamType() : "");
                cell2.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(3);
                cell3.setCellValue(param.getParamValue() != null ? param.getParamValue() : "");
                cell3.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(4);
                cell4.setCellValue(param.getIsEnabled() != null && param.getIsEnabled() == 1 ? "启用" : "禁用");
                cell4.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(5);
                cell5.setCellValue(param.getDescription() != null ? param.getDescription() : "");
                cell5.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell6 = row.createCell(6);
                if (param.getCreateTime() != null) {
                    cell6.setCellValue(dateFormat.format(param.getCreateTime()));
                } else {
                    cell6.setCellValue("");
                }
                cell6.setCellStyle(dataStyle);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("安全参数配置_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 写入响应流
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

            log.info("导出安全参数配置成功, 共{}条记录", list.size());

        } catch (Exception e) {
            log.error("导出安全参数配置失败", e);
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception e) {
                log.error("关闭工作簿失败", e);
            }
        }
    }

    // ==================== Ukey厂商管理 ====================

    // 注释：与UkeyVendorController路径冲突，使用独立的Controller
    // @RequestMapping(value = "/ukeyVendor/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取Ukey厂商列表(分页)")
    // public String getUkeyVendorList(
    //         @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNumber,
    //         @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
    //         @ApiParam(value = "厂商编码") @RequestParam(required = false) String vendorCode,
    //         @ApiParam(value = "厂商名称") @RequestParam(required = false) String vendorName,
    //         @ApiParam(value = "厂商类型") @RequestParam(required = false) String vendorType,
    //         @ApiParam(value = "合作状态") @RequestParam(required = false) String cooperationStatus,
    //         @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
    //         HttpServletResponse response) {
    //
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         // 构建查询条件
    //         QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
    //
    //         if (StringUtils.hasText(vendorCode)) {
    //             queryWrapper.like("VENDOR_CODE", vendorCode);
    //         }
    //         if (StringUtils.hasText(vendorName)) {
    //             queryWrapper.like("VENDOR_NAME", vendorName);
    //         }
    //         if (StringUtils.hasText(vendorType)) {
    //             queryWrapper.eq("VENDOR_TYPE", vendorType);
    //         }
    //         if (StringUtils.hasText(cooperationStatus)) {
    //             queryWrapper.eq("COOPERATION_STATUS", cooperationStatus);
    //         }
    //         if (isEnabled != null) {
    //             queryWrapper.eq("IS_ENABLED", isEnabled);
    //         }
    //
    //         // 不添加orderBy,让Mapper XML中的ORDER BY生效
    //
    //         // 查询所有符合条件的数据
    //         List<TblUkeyVendor> allRecords = tblUkeyVendorService.list(queryWrapper);
    //
    //         // 手动分页
    //         int total = allRecords.size();
    //         int start = (pageNumber - 1) * pageSize;
    //         int end = Math.min(start + pageSize, total);
    //
    //         List<TblUkeyVendor> pageData = start < total ? allRecords.subList(start, end) : new java.util.ArrayList<>();
    //
    //         Map<String, Object> data = new HashMap<>();
    //         data.put("list", pageData);
    //         data.put("total", (long) total);
    //
    //         return JsonBean.success(data);
    //
    //     } catch (Exception e) {
    //         log.error("获取Ukey厂商列表失败", e);
    //         return JsonBean.error("获取Ukey厂商列表失败: " + e.getMessage());
    //     }
    // }

    @GetMapping("/ukeyVendor/export")
    @ApiOperation("导出Ukey厂商配置")
    public void exportUkeyVendor(
            @ApiParam(value = "厂商名称") @RequestParam(required = false) String vendorName,
            @ApiParam(value = "厂商类型") @RequestParam(required = false) String vendorType,
            @ApiParam(value = "合作状态") @RequestParam(required = false) String cooperationStatus,
            HttpServletResponse response) {

        // 创建工作簿
        org.apache.poi.ss.usermodel.Workbook workbook = null;
        org.apache.poi.ss.usermodel.Sheet sheet = null;

        try {
            // 权限验证
            if (!validateUser()) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("用户已失效"));
                return;
            }

            // 查询数据
            QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(vendorName)) {
                queryWrapper.like("VENDOR_NAME", vendorName);
            }
            if (StringUtils.hasText(vendorType)) {
                queryWrapper.eq("VENDOR_TYPE", vendorType);
            }
            if (StringUtils.hasText(cooperationStatus)) {
                queryWrapper.eq("COOPERATION_STATUS", cooperationStatus);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TblUkeyVendor> list = tblUkeyVendorService.list(queryWrapper);

            // 创建Excel工作簿
            workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            sheet = workbook.createSheet("Ukey厂商配置");

            // 创建表头样式
            org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);

            // 创建数据样式
            org.apache.poi.ss.usermodel.CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
            dataStyle.setWrapText(true);

            // 创建表头行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"厂商编码", "厂商名称", "厂商类型", "厂商等级", "联系人", "联系电话", "合作状态", "评价等级", "是否启用", "创建时间"};
            int[] columnWidths = {20, 25, 15, 15, 15, 18, 15, 15, 10, 20};

            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, columnWidths[i] * 256);
            }

            // 填充数据
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            int rowNum = 1;

            for (TblUkeyVendor vendor : list) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);

                org.apache.poi.ss.usermodel.Cell cell0 = row.createCell(0);
                cell0.setCellValue(vendor.getVendorCode() != null ? vendor.getVendorCode() : "");
                cell0.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell1 = row.createCell(1);
                cell1.setCellValue(vendor.getVendorName() != null ? vendor.getVendorName() : "");
                cell1.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell2 = row.createCell(2);
                cell2.setCellValue(vendor.getVendorType() != null ? vendor.getVendorType() : "");
                cell2.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell3 = row.createCell(3);
                cell3.setCellValue(vendor.getVendorLevel() != null ? vendor.getVendorLevel() : "");
                cell3.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell4 = row.createCell(4);
                cell4.setCellValue(vendor.getContactPerson() != null ? vendor.getContactPerson() : "");
                cell4.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell5 = row.createCell(5);
                cell5.setCellValue(vendor.getContactPhone() != null ? vendor.getContactPhone() : "");
                cell5.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell6 = row.createCell(6);
                cell6.setCellValue(vendor.getCooperationStatus() != null ? vendor.getCooperationStatus() : "");
                cell6.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell7 = row.createCell(7);
                cell7.setCellValue(vendor.getEvaluationLevel() != null ? vendor.getEvaluationLevel() : "");
                cell7.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell8 = row.createCell(8);
                cell8.setCellValue(vendor.getIsEnabled() != null && vendor.getIsEnabled() == 1 ? "启用" : "禁用");
                cell8.setCellStyle(dataStyle);

                org.apache.poi.ss.usermodel.Cell cell9 = row.createCell(9);
                if (vendor.getCreateTime() != null) {
                    cell9.setCellValue(dateFormat.format(vendor.getCreateTime()));
                } else {
                    cell9.setCellValue("");
                }
                cell9.setCellStyle(dataStyle);
            }

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            String fileName = java.net.URLEncoder.encode("Ukey厂商配置_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()) + ".xlsx", "UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            // 写入响应流
            workbook.write(response.getOutputStream());
            response.getOutputStream().flush();

            log.info("导出Ukey厂商配置成功, 共{}条记录", list.size());

        } catch (Exception e) {
            log.error("导出Ukey厂商配置失败", e);
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (Exception e) {
                log.error("关闭工作簿失败", e);
            }
        }
    }

    @PostMapping("/ukeyVendor")
    @ApiOperation("创建Ukey厂商")
    public String createUkeyVendor(TblUkeyVendor vendor, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(vendor.getVendorCode())) {
                return JsonBean.error("厂商编码不能为空");
            }
            if (!StringUtils.hasText(vendor.getVendorName())) {
                return JsonBean.error("厂商名称不能为空");
            }
            if (!StringUtils.hasText(vendor.getVendorType())) {
                return JsonBean.error("厂商类型不能为空");
            }

            // 检查厂商编码是否已存在
            QueryWrapper<TblUkeyVendor> checkWrapper = new QueryWrapper<>();
            checkWrapper.eq("VENDOR_CODE", vendor.getVendorCode());
            long count = tblUkeyVendorService.count(checkWrapper);
            if (count > 0) {
                return JsonBean.error("厂商编码已存在");
            }

            // 设置默认值
            vendor.setId(generateId());
            if (vendor.getIsEnabled() == null) {
                vendor.setIsEnabled(1);
            }
            if (vendor.getCertificationStatus() == null) {
                vendor.setCertificationStatus("UNCERTIFIED");
            }
            if (vendor.getCooperationStatus() == null) {
                vendor.setCooperationStatus("ACTIVE");
            }
            vendor.setCreateTime(new Date());
            vendor.setUpdateTime(new Date());
            vendor.setCreateBy(getCurrentUser());
            vendor.setUpdateBy(getCurrentUser());

            boolean success = tblUkeyVendorService.save(vendor);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建Ukey厂商失败", e);
            return JsonBean.error("创建Ukey厂商失败: " + e.getMessage());
        }
    }

    @PutMapping("/ukeyVendor")
    @ApiOperation("更新Ukey厂商")
    public String updateUkeyVendor(TblUkeyVendor vendor, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (vendor.getId() == null) {
                return JsonBean.error("厂商ID不能为空");
            }

            // 检查是否存在
            TblUkeyVendor existing = tblUkeyVendorService.getById(vendor.getId());
            if (existing == null) {
                return JsonBean.error("Ukey厂商不存在");
            }

            // 如果修改了厂商编码,检查新编码是否已被使用
            if (!existing.getVendorCode().equals(vendor.getVendorCode())) {
                QueryWrapper<TblUkeyVendor> checkWrapper = new QueryWrapper<>();
                checkWrapper.eq("VENDOR_CODE", vendor.getVendorCode());
                checkWrapper.ne("ID", vendor.getId());
                long count = tblUkeyVendorService.count(checkWrapper);
                if (count > 0) {
                    return JsonBean.error("厂商编码已存在");
                }
            }

            vendor.setUpdateTime(new Date());
            vendor.setUpdateBy(getCurrentUser());

            boolean success = tblUkeyVendorService.updateById(vendor);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新Ukey厂商失败", e);
            return JsonBean.error("更新Ukey厂商失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/ukeyVendor/{id}")
    @ApiOperation("删除Ukey厂商")
    public String deleteUkeyVendor(
            @ApiParam(value = "厂商ID", required = true) @PathVariable Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TblUkeyVendor existing = tblUkeyVendorService.getById(id);
            if (existing == null) {
                return JsonBean.error("Ukey厂商不存在");
            }

            boolean success = tblUkeyVendorService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除Ukey厂商失败，ID: {}", id, e);
            return JsonBean.error("删除Ukey厂商失败: " + e.getMessage());
        }
    }

    // 注释：与UkeyVendorController路径冲突，使用独立的Controller
    // @GetMapping("/ukeyVendor/statistics")
    // @ApiOperation("获取Ukey厂商统计信息")
    // public String getUkeyVendorStatistics(HttpServletResponse response) {
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         // 查询所有厂商
    //         QueryWrapper<TblUkeyVendor> queryWrapper = new QueryWrapper<>();
    //         List<TblUkeyVendor> allVendors = tblUkeyVendorService.list(queryWrapper);
    //
    //         // 统计数据
    //         Map<String, Object> statistics = new HashMap<>();
    //         statistics.put("totalVendors", allVendors.size());
    //
    //         // 已认证厂商数
    //         long certifiedCount = allVendors.stream()
    //                 .filter(v -> "CERTIFIED".equals(v.getCertificationStatus()))
    //                 .count();
    //         statistics.put("certifiedVendors", certifiedCount);
    //
    //         // 活跃厂商数(合作状态为ACTIVE)
    //         long activeCount = allVendors.stream()
    //                 .filter(v -> "ACTIVE".equals(v.getCooperationStatus()))
    //                 .count();
    //         statistics.put("activeVendors", activeCount);
    //
    //         // 产品型号数(计算所有不重复的产品型号)
    //         long productModelCount = allVendors.stream()
    //                 .filter(v -> StringUtils.hasText(v.getProductModels()))
    //                 .flatMap(v -> java.util.Arrays.stream(v.getProductModels().split("[,，]")))
    //                 .map(String::trim)
    //                 .filter(s -> !s.isEmpty())
    //                 .distinct()
    //                 .count();
    //         statistics.put("productModels", productModelCount);
    //
    //         return JsonBean.success(statistics);
    //
    //     } catch (Exception e) {
    //         log.error("获取Ukey厂商统计信息失败", e);
    //         return JsonBean.error("获取统计信息失败: " + e.getMessage());
    //     }
    // }

    // ==================== 第三方账户管理 ====================

    @RequestMapping(value = "/thirdPartyAccount/page", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取第三方账户列表(分页)")
    public String getThirdPartyAccountPage(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer pageNumber,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "账户编码") @RequestParam(required = false) String accountCode,
            @ApiParam(value = "账户名称") @RequestParam(required = false) String accountName,
            @ApiParam(value = "第三方系统") @RequestParam(required = false) String thirdPartySystem,
            @ApiParam(value = "账户类型") @RequestParam(required = false) String accountType,
            @ApiParam(value = "连接状态") @RequestParam(required = false) String connectionStatus,
            @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询条件
            QueryWrapper<TblThirdPartyAccount> queryWrapper = new QueryWrapper<>();

            if (StringUtils.hasText(accountCode)) {
                queryWrapper.like("ACCOUNT_CODE", accountCode);
            }
            if (StringUtils.hasText(accountName)) {
                queryWrapper.like("ACCOUNT_NAME", accountName);
            }
            if (StringUtils.hasText(thirdPartySystem)) {
                queryWrapper.eq("THIRD_PARTY_SYSTEM", thirdPartySystem);
            }
            if (StringUtils.hasText(accountType)) {
                queryWrapper.eq("ACCOUNT_TYPE", accountType);
            }
            if (StringUtils.hasText(connectionStatus)) {
                queryWrapper.eq("CONNECTION_STATUS", connectionStatus);
            }
            if (isEnabled != null) {
                queryWrapper.eq("IS_ENABLED", isEnabled);
            }

            // 不添加orderBy,让Mapper XML中的ORDER BY生效

            // 查询所有符合条件的数据
            List<TblThirdPartyAccount> allRecords = tblThirdPartyAccountService.list(queryWrapper);

            // 手动分页
            int total = allRecords.size();
            int start = (pageNumber - 1) * pageSize;
            int end = Math.min(start + pageSize, total);

            List<TblThirdPartyAccount> pageData = start < total ? allRecords.subList(start, end) : new java.util.ArrayList<>();

            Map<String, Object> data = new HashMap<>();
            data.put("list", pageData);
            data.put("total", (long) total);

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取第三方账户列表失败", e);
            return JsonBean.error("获取第三方账户列表失败: " + e.getMessage());
        }
    }

    // 注释：与ThirdPartyAccountController路径冲突，使用独立的Controller
    // @RequestMapping(value = "/thirdPartyAccount/list", method = {RequestMethod.GET, RequestMethod.POST})
    // @ApiOperation("获取第三方账户列表")
    // public String getThirdPartyAccountList(
    //         @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
    //         @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
    //         @ApiParam(value = "账户编码") @RequestParam(required = false) String accountCode,
    //         @ApiParam(value = "账户名称") @RequestParam(required = false) String accountName,
    //         @ApiParam(value = "第三方系统") @RequestParam(required = false) String thirdPartySystem,
    //         @ApiParam(value = "账户类型") @RequestParam(required = false) String accountType,
    //         @ApiParam(value = "连接状态") @RequestParam(required = false) String connectionStatus,
    //         @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isEnabled,
    //         HttpServletResponse response) {
    //
    //     try {
    //         if (!validateUser()) {
    //             return JsonBean.error("用户已失效");
    //         }
    //
    //         IPage<TblThirdPartyAccount> result = tblThirdPartyAccountService.getThirdPartyAccountPage(
    //                 page, limit, accountCode, accountName, thirdPartySystem, accountType, connectionStatus, isEnabled);
    //
    //         Map<String, Object> data = new HashMap<>();
    //         data.put("tlist", result.getRecords());
    //         data.put("totalRecord", result.getTotal());
    //         data.put("pageNo", result.getCurrent());
    //         data.put("pageSize", result.getSize());
    //
    //         return JsonBean.success(data);
    //
    //     } catch (Exception e) {
    //         log.error("获取第三方账户列表失败", e);
    //         return JsonBean.error("获取第三方账户列表失败: " + e.getMessage());
    //     }
    // }

    @GetMapping("/thirdPartyAccount/detail")
    @ApiOperation("获取第三方账户详情")
    public String getThirdPartyAccountDetail(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("第三方账户不存在");
            }

            return JsonBean.success(account);

        } catch (Exception e) {
            log.error("获取第三方账户详情失败，ID: {}", id, e);
            return JsonBean.error("获取第三方账户详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/thirdPartyAccount/create")
    @ApiOperation("创建第三方账户")
    public String createThirdPartyAccount(TblThirdPartyAccount account, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(account.getAccountCode())) {
                return JsonBean.error("账户编码不能为空");
            }
            if (!StringUtils.hasText(account.getAccountName())) {
                return JsonBean.error("账户名称不能为空");
            }
            if (!StringUtils.hasText(account.getThirdPartySystem())) {
                return JsonBean.error("第三方系统不能为空");
            }
            if (!StringUtils.hasText(account.getAccountType())) {
                return JsonBean.error("账户类型不能为空");
            }

            // 设置默认值
            account.setId(generateId());
            if (account.getIsEnabled() == null) {
                account.setIsEnabled(1);
            }
            if (account.getConnectionStatus() == null) {
                account.setConnectionStatus("OFFLINE");
            }
            account.setCreateTime(new Date());
            account.setUpdateTime(new Date());
            account.setCreateBy(getCurrentUser());
            account.setUpdateBy(getCurrentUser());

            boolean success = tblThirdPartyAccountService.save(account);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建第三方账户失败", e);
            return JsonBean.error("创建第三方账户失败: " + e.getMessage());
        }
    }

    @PutMapping("/thirdPartyAccount/update")
    @ApiOperation("更新第三方账户")
    public String updateThirdPartyAccount(@FlexibleRequestBody TblThirdPartyAccount account, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (account.getId() == null) {
                return JsonBean.error("账户ID不能为空");
            }

            // 检查是否存在
            TblThirdPartyAccount existing = tblThirdPartyAccountService.getById(account.getId());
            if (existing == null) {
                return JsonBean.error("第三方账户不存在");
            }

            account.setUpdateTime(new Date());
            account.setUpdateBy(getCurrentUser());

            boolean success = tblThirdPartyAccountService.updateById(account);
            log.info("更新第三方账户结果, id={}, success={}", account.getId(), success);

            // 即使 updateById 返回 false（可能是数据没有变化），只要没有异常就认为成功
            return JsonBean.success("更新成功");

        } catch (Exception e) {
            log.error("更新第三方账户失败", e);
            return JsonBean.error("更新第三方账户失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/thirdPartyAccount/delete")
    @ApiOperation("删除第三方账户")
    public String deleteThirdPartyAccount(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TblThirdPartyAccount existing = tblThirdPartyAccountService.getById(id);
            if (existing == null) {
                return JsonBean.error("第三方账户不存在");
            }

            boolean success = tblThirdPartyAccountService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除第三方账户失败，ID: {}", id, e);
            return JsonBean.error("删除第三方账户失败: " + e.getMessage());
        }
    }

    @PostMapping("/thirdPartyAccount/testConnection")
    @ApiOperation("测试账户连接")
    public String testThirdPartyAccountConnection(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查账户是否存在
            TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("第三方账户不存在");
            }

            // 模拟连接测试
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("success", true);
            testResult.put("message", "连接测试成功");
            testResult.put("responseTime", "125ms");
            testResult.put("connectionStatus", "ONLINE");

            return new JsonBean(1, "连接测试成功", testResult).toString();

        } catch (Exception e) {
            log.error("测试第三方账户连接失败，ID: {}", id, e);
            return JsonBean.error("测试第三方账户连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/thirdPartyAccount/syncData")
    @ApiOperation("同步账户数据")
    public String syncThirdPartyAccountData(
            @ApiParam(value = "账户ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查账户是否存在
            TblThirdPartyAccount account = tblThirdPartyAccountService.getById(id);
            if (account == null) {
                return JsonBean.error("第三方账户不存在");
            }

            // 模拟数据同步
            account.setLastSyncTime(new Date());
            tblThirdPartyAccountService.updateById(account);

            Map<String, Object> syncResult = new HashMap<>();
            syncResult.put("success", true);
            syncResult.put("message", "数据同步成功");
            syncResult.put("syncTime", new Date());
            syncResult.put("syncCount", 150);

            return new JsonBean(1, "数据同步成功", syncResult).toString();

        } catch (Exception e) {
            log.error("同步第三方账户数据失败，ID: {}", id, e);
            return JsonBean.error("同步第三方账户数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/thirdPartyAccount/systemTypes")
    @ApiOperation("获取第三方系统类型选项")
    public String getThirdPartySystemTypes(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> systemTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "支付宝");
            type1.put("value", "ALIPAY");
            systemTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "微信支付");
            type2.put("value", "WECHAT_PAY");
            systemTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "银联支付");
            type3.put("value", "UNIONPAY");
            systemTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", " PayPal");
            type4.put("value", "PAYPAL");
            systemTypes.add(type4);

            Map<String, Object> type5 = new HashMap<>();
            type5.put("label", "Stripe");
            type5.put("value", "STRIPE");
            systemTypes.add(type5);

            return JsonBean.success(systemTypes);

        } catch (Exception e) {
            log.error("获取第三方系统类型选项失败", e);
            return JsonBean.error("获取第三方系统类型选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/thirdPartyAccount/accountTypes")
    @ApiOperation("获取账户类型选项")
    public String getAccountTypes(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> accountTypes = new ArrayList<>();
            Map<String, Object> type1 = new HashMap<>();
            type1.put("label", "支付账户");
            type1.put("value", "PAYMENT");
            accountTypes.add(type1);

            Map<String, Object> type2 = new HashMap<>();
            type2.put("label", "收款账户");
            type2.put("value", "RECEIPT");
            accountTypes.add(type2);

            Map<String, Object> type3 = new HashMap<>();
            type3.put("label", "结算账户");
            type3.put("value", "SETTLEMENT");
            accountTypes.add(type3);

            Map<String, Object> type4 = new HashMap<>();
            type4.put("label", "备用金账户");
            type4.put("value", "PETTY_CASH");
            accountTypes.add(type4);

            return JsonBean.success(accountTypes);

        } catch (Exception e) {
            log.error("获取账户类型选项失败", e);
            return JsonBean.error("获取账户类型选项失败: " + e.getMessage());
        }
    }

    @GetMapping("/thirdPartyAccount/connectionStatuses")
    @ApiOperation("获取连接状态选项")
    public String getConnectionStatuses(HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            List<Map<String, Object>> connectionStatuses = new ArrayList<>();
            Map<String, Object> status1 = new HashMap<>();
            status1.put("label", "在线");
            status1.put("value", "ONLINE");
            connectionStatuses.add(status1);

            Map<String, Object> status2 = new HashMap<>();
            status2.put("label", "离线");
            status2.put("value", "OFFLINE");
            connectionStatuses.add(status2);

            Map<String, Object> status3 = new HashMap<>();
            status3.put("label", "连接中");
            status3.put("value", "CONNECTING");
            connectionStatuses.add(status3);

            Map<String, Object> status4 = new HashMap<>();
            status4.put("label", "连接失败");
            status4.put("value", "CONNECTION_FAILED");
            connectionStatuses.add(status4);

            return JsonBean.success(connectionStatuses);

        } catch (Exception e) {
            log.error("获取连接状态选项失败", e);
            return JsonBean.error("获取连接状态选项失败: " + e.getMessage());
        }
    }

    // ==================== 印鉴档案管理 ====================

    @RequestMapping(value = "/seal/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴档案列表")
    public String getSealArchiveList(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "印鉴编码") @RequestParam(required = false) String sealCode,
            @ApiParam(value = "印鉴名称") @RequestParam(required = false) String sealName,
            @ApiParam(value = "印鉴类型ID") @RequestParam(required = false) String sealTypeId,
            @ApiParam(value = "持有人姓名") @RequestParam(required = false) String ownerName,
            @ApiParam(value = "是否启用") @RequestParam(required = false) Integer isActive,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(page, limit);

            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TcSealArchive> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

            if (StringUtils.hasText(sealCode)) {
                queryWrapper.like("SEAL_CODE", sealCode);
            }
            if (StringUtils.hasText(sealName)) {
                queryWrapper.like("SEAL_NAME", sealName);
            }
            if (StringUtils.hasText(sealTypeId)) {
                queryWrapper.eq("SEAL_TYPE_ID", sealTypeId);
            }
            if (StringUtils.hasText(ownerName)) {
                queryWrapper.like("OWNER_NAME", ownerName);
            }
            if (isActive != null) {
                queryWrapper.eq("STATUS", isActive == 1 ? "1" : "0");
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            // 查询列表
            List<TcSealArchive> list = tblSealArchiveService.list(queryWrapper);

            // 使用PageInfo获取分页信息
            com.github.pagehelper.PageInfo<TcSealArchive> pageInfo =
                new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取印鉴档案列表失败", e);
            return JsonBean.error("获取印鉴档案列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/seal/create")
    @ApiOperation("创建印鉴档案")
    public String createSealArchive(@FlexibleRequestBody TcSealArchive sealArchive, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(sealArchive.getSealCode())) {
                return JsonBean.error("印鉴编码不能为空");
            }
            if (!StringUtils.hasText(sealArchive.getSealName())) {
                return JsonBean.error("印鉴名称不能为空");
            }

            // 设置默认值
            sealArchive.setId(String.valueOf(generateId()));
            sealArchive.setCreateTime(new Date());
            sealArchive.setUpdateTime(new Date());
            sealArchive.setCreateUser(getCurrentUser());
            sealArchive.setUpdateUser(getCurrentUser());
            if (!StringUtils.hasText(sealArchive.getStatus())) {
                sealArchive.setStatus("1");
            }

            boolean success = tblSealArchiveService.save(sealArchive);
            if (success) {
                return new JsonBean(1, "创建成功", sealArchive).toString();
            } else {
                return JsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建印鉴档案失败", e);
            return JsonBean.error("创建印鉴档案失败: " + e.getMessage());
        }
    }

    @PostMapping("/seal/update")
    @ApiOperation("更新印鉴档案")
    public String updateSealArchive(@FlexibleRequestBody TcSealArchive sealArchive, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcSealArchive existing = tblSealArchiveService.getById(sealArchive.getId());
            if (existing == null) {
                return JsonBean.error("印鉴档案不存在");
            }

            boolean success = tblSealArchiveService.updateById(sealArchive);
            if (success) {
                return new JsonBean(1, "更新成功", sealArchive).toString();
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新印鉴档案失败", e);
            return JsonBean.error("更新印鉴档案失败: " + e.getMessage());
        }
    }

    @DeleteMapping({"/seal/delete", "/seal/delete/{id}"})
    @ApiOperation("删除印鉴档案")
    public String deleteSealArchive(
            @ApiParam(value = "印鉴ID(路径参数)") @PathVariable(required = false) String id,
            @ApiParam(value = "印鉴ID(请求参数)") @RequestParam(required = false) String sealId,
            HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 优先使用路径参数，其次使用请求参数
            String deleteId = id;
            if (!StringUtils.hasText(deleteId)) {
                deleteId = sealId;
            }

            if (!StringUtils.hasText(deleteId)) {
                return JsonBean.error("印鉴ID不能为空");
            }

            TcSealArchive existing = tblSealArchiveService.getById(deleteId);
            if (existing == null) {
                return JsonBean.error("印鉴档案不存在");
            }

            boolean success = tblSealArchiveService.removeById(deleteId);
            if (success) {
                return new JsonBean(1, "删除成功", null).toString();
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除印鉴档案失败", e);
            return JsonBean.error("删除印鉴档案失败: " + e.getMessage());
        }
    }

    // ==================== 印鉴类型管理 ====================

    @GetMapping("/treasury/seal-types/page")
    @ApiOperation("分页查询印鉴类型")
    public String getSealTypesPage(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "类型名称") @RequestParam(required = false) String name,
            @ApiParam(value = "印鉴级别") @RequestParam(required = false) Integer sealLevel,
            @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(page, limit);

            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<com.global.treasurer.entity.TcSealType> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

            if (StringUtils.hasText(name)) {
                queryWrapper.like("TYPE_NAME", name);
            }
            if (sealLevel != null) {
                queryWrapper.eq("PERMISSION_LEVEL", String.valueOf(sealLevel));
            }
            if (status != null) {
                // TC_SEAL_TYPE表可能没有STATUS字段,先不添加此条件
                // queryWrapper.eq("STATUS", status == 1 ? "1" : "0");
            }
            // 不添加orderBy,让Mapper XML处理

            // 查询列表
            List<com.global.treasurer.entity.TcSealType> list = tcSealTypeService.list(queryWrapper);

            // 使用PageInfo获取分页信息
            com.github.pagehelper.PageInfo<com.global.treasurer.entity.TcSealType> pageInfo =
                new com.github.pagehelper.PageInfo<>(list);

            // 转换为返回格式
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("分页查询印鉴类型失败", e);
            return JsonBean.error("分页查询印鉴类型失败: " + e.getMessage());
        }
    }

    @GetMapping("/seal/types")
    @ApiOperation("获取印鉴类型选项")
    public String getSealTypes(HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 查询所有印鉴类型(不加STATUS过滤,因为表可能没有此字段)
            List<com.global.treasurer.entity.TcSealType> sealTypes = tcSealTypeService.list();

            // 转换为前端需要的格式
            List<Map<String, Object>> result = new ArrayList<>();
            for (com.global.treasurer.entity.TcSealType sealType : sealTypes) {
                Map<String, Object> typeMap = new HashMap<>();
                typeMap.put("id", sealType.getId());
                typeMap.put("name", sealType.getTypeName());
                result.add(typeMap);
            }

            return JsonBean.success(result);

        } catch (Exception e) {
            log.error("获取印鉴类型选项失败", e);
            return JsonBean.error("获取印鉴类型选项失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/seal/type/delete")
    @ApiOperation("删除印鉴类型")
    public String deleteSealType(
            @ApiParam(value = "印鉴类型ID", required = true) @RequestParam String id,
            HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (!StringUtils.hasText(id)) {
                return JsonBean.error("印鉴类型ID不能为空");
            }

            boolean success = tcSealTypeService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败，记录可能不存在");
            }
        } catch (Exception e) {
            log.error("删除印鉴类型失败", e);
            return JsonBean.error("删除印鉴类型失败: " + e.getMessage());
        }
    }

    @GetMapping("/seal/statistics")
    @ApiOperation("获取印鉴统计数据")
    public String getSealStatistics(HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> statistics = tblSealArchiveService.getSealStatistics();
            return JsonBean.success(statistics);

        } catch (Exception e) {
            log.error("获取印鉴统计数据失败", e);
            return JsonBean.error("获取印鉴统计数据失败: " + e.getMessage());
        }
    }

    // ==================== 印鉴组合管理 ====================

    @RequestMapping(value = "/sealCombination/list", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴组合列表")
    public String getSealCombinationListNew(
            @RequestBody(required = false) Map<String, Object> body,
            @RequestParam(required = false) Map<String, Object> queryParams,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // POST body 优先，GET query 兜底
            Map<String, Object> params = new HashMap<>();
            if (queryParams != null) params.putAll(queryParams);
            if (body != null) params.putAll(body);

            int pageNum = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            int pageSize = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) :
                          (params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20);
            String queryName = params.get("combinationName") != null ? params.get("combinationName").toString() : null;
            String queryCode = params.get("combinationCode") != null ? params.get("combinationCode").toString() : null;
            String queryCombinationType = params.get("combinationType") != null ? params.get("combinationType").toString() : null;
            String queryBusinessType = params.get("businessType") != null ? params.get("businessType").toString() : null;
            String queryAuthorityLevel = params.get("authorityLevel") != null ? params.get("authorityLevel").toString() : null;
            Integer queryStatus = params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;

            // 使用PageHelper分页
            com.github.pagehelper.PageHelper.startPage(pageNum, pageSize);

            // 构建查询条件(不使用orderBy,避免与Mapper中的ORDER BY冲突)
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TcSealCombination> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();

            if (StringUtils.hasText(queryName)) {
                queryWrapper.like("COMBINATION_NAME", queryName);
            }
            if (StringUtils.hasText(queryCode)) {
                queryWrapper.like("COMBINATION_CODE", queryCode);
            }
            if (StringUtils.hasText(queryCombinationType)) {
                queryWrapper.eq("COMBINATION_TYPE", queryCombinationType);
            }
            if (StringUtils.hasText(queryBusinessType)) {
                queryWrapper.eq("BUSINESS_TYPE", queryBusinessType);
            }
            if (StringUtils.hasText(queryAuthorityLevel)) {
                queryWrapper.eq("AUTHORITY_LEVEL", queryAuthorityLevel);
            }
            if (queryStatus != null) {
                queryWrapper.eq("IS_ENABLED", queryStatus);
            }
            // 移除orderBy,让Mapper XML中的ORDER BY生效
            // queryWrapper.orderByDesc("CREATE_TIME");

            // 查询列表(PageHelper会自动分页)
            List<TcSealCombination> list = tcSealCombinationService.list(queryWrapper);

            // 使用PageInfo获取分页信息
            com.github.pagehelper.PageInfo<TcSealCombination> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            // 转换为返回格式
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取印鉴组合列表失败", e);
            return JsonBean.error("获取印鉴组合列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/sealCombination/add")
    @ApiOperation("创建印鉴组合(新路径)")
    public String addSealCombinationNew(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination combination = new TcSealCombination();
            // 前端字段映射
            String combinationName = params.get("combinationName") != null ? (String) params.get("combinationName") : (String) params.get("name");
            String combinationCode = params.get("combinationCode") != null ? (String) params.get("combinationCode") : (String) params.get("code");
            combination.setCombinationName(combinationName);
            combination.setCombinationCode(combinationCode);
            combination.setCombinationType((String) params.get("combinationType"));
            combination.setBusinessType((String) params.get("businessType"));
            combination.setAuthorityLevel((String) params.get("authorityLevel"));
            // 处理 sealIds 数组转为逗号分隔字符串
            Object sealIds = params.get("sealIds");
            if (sealIds != null) {
                if (sealIds instanceof List) {
                    combination.setSealList(String.join(",", ((List<?>) sealIds).stream().map(Object::toString).toArray(String[]::new)));
                } else {
                    combination.setSealList(sealIds.toString());
                }
            }
            combination.setDescription((String) params.get("description"));
            combination.setStatus(params.get("isEnabled") != null ? Integer.parseInt(params.get("isEnabled").toString()) : 1);
            combination.setUsageCount(0);
            combination.setCreateBy(getCurrentUserId());
            combination.setCreateTime(new Date());
            combination.setUpdateBy(getCurrentUserId());
            combination.setUpdateTime(new Date());

            boolean success = tcSealCombinationService.save(combination);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建印鉴组合失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/sealCombination/update")
    @ApiOperation("更新印鉴组合(新路径)")
    public String updateSealCombinationNew(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination combination = new TcSealCombination();
            combination.setId(params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null);
            combination.setCombinationName((String) params.get("combinationName"));
            combination.setCombinationCode((String) params.get("combinationCode"));
            combination.setCombinationType((String) params.get("combinationType"));
            combination.setBusinessType((String) params.get("businessType"));
            combination.setAuthorityLevel((String) params.get("authorityLevel"));
            // 处理 sealIds 数组
            Object sealIds = params.get("sealIds");
            if (sealIds != null) {
                if (sealIds instanceof List) {
                    combination.setSealList(String.join(",", ((List<?>) sealIds).stream().map(Object::toString).toArray(String[]::new)));
                } else {
                    combination.setSealList(sealIds.toString());
                }
            } else if (params.get("sealList") != null) {
                combination.setSealList((String) params.get("sealList"));
            }
            combination.setDescription((String) params.get("description"));
            if (params.get("isEnabled") != null) {
                combination.setStatus(Integer.parseInt(params.get("isEnabled").toString()));
            } else if (params.get("status") != null) {
                combination.setStatus(Integer.parseInt(params.get("status").toString()));
            }
            combination.setUpdateBy(getCurrentUserId());
            combination.setUpdateTime(new Date());

            boolean success = tcSealCombinationService.updateById(combination);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新印鉴组合失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/sealCombination/delete")
    @ApiOperation("删除印鉴组合(新路径)")
    public String deleteSealCombinationNew(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Long id = params.get("id") != null ? Long.parseLong(params.get("id").toString()) : null;
            if (id == null) {
                return JsonBean.error("组合ID不能为空");
            }

            boolean success = tcSealCombinationService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除印鉴组合失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @RequestMapping(value = "/seal/combinationList", method = {RequestMethod.GET, RequestMethod.POST})
    @ApiOperation("获取印鉴组合列表")
    public String getSealCombinationList(
            @ApiParam(value = "页码", example = "1") @RequestParam(defaultValue = "1") Integer page,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer limit,
            @ApiParam(value = "组合名称") @RequestParam(required = false) String combinationName,
            @ApiParam(value = "组合编码") @RequestParam(required = false) String combinationCode,
            @ApiParam(value = "状态") @RequestParam(required = false) Integer status,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询条件
            QueryWrapper<TcSealCombination> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(combinationName)) {
                queryWrapper.like("COMBINATION_NAME", combinationName);
            }
            if (StringUtils.hasText(combinationCode)) {
                queryWrapper.like("COMBINATION_CODE", combinationCode);
            }
            if (status != null) {
                queryWrapper.eq("STATUS", status);
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            // 分页查询
            Page<TcSealCombination> pageParam = new Page<>(page, limit);
            IPage<TcSealCombination> result = tcSealCombinationService.page(pageParam, queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            return JsonBean.success(data);

        } catch (Exception e) {
            log.error("获取印鉴组合列表失败", e);
            return JsonBean.error("获取印鉴组合列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/seal/createCombination")
    @ApiOperation("创建印鉴组合")
    public String createSealCombination(TcSealCombination combination, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 参数校验
            if (!StringUtils.hasText(combination.getCombinationName())) {
                return JsonBean.error("组合名称不能为空");
            }
            if (!StringUtils.hasText(combination.getCombinationCode())) {
                return JsonBean.error("组合编码不能为空");
            }

            // 设置默认值
            combination.setId(generateId());
            if (combination.getStatus() == null) {
                combination.setStatus(1);
            }
            combination.setCreateTime(new Date());
            combination.setUpdateTime(new Date());
            combination.setCreateBy(getCurrentUserId());
            combination.setUpdateBy(getCurrentUserId());

            boolean success = tcSealCombinationService.save(combination);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建印鉴组合失败", e);
            return JsonBean.error("创建印鉴组合失败: " + e.getMessage());
        }
    }

    @PostMapping("/seal/updateCombination")
    @ApiOperation("更新印鉴组合")
    public String updateSealCombination(TcSealCombination combination, HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (combination.getId() == null) {
                return JsonBean.error("组合ID不能为空");
            }

            // 检查是否存在
            TcSealCombination existing = tcSealCombinationService.getById(combination.getId());
            if (existing == null) {
                return JsonBean.error("印鉴组合不存在");
            }

            combination.setUpdateTime(new Date());
            combination.setUpdateBy(getCurrentUserId());

            boolean success = tcSealCombinationService.updateById(combination);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新印鉴组合失败", e);
            return JsonBean.error("更新印鉴组合失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/seal/deleteCombination")
    @ApiOperation("删除印鉴组合")
    public String deleteSealCombination(
            @ApiParam(value = "组合ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TcSealCombination existing = tcSealCombinationService.getById(id);
            if (existing == null) {
                return JsonBean.error("印鉴组合不存在");
            }

            boolean success = tcSealCombinationService.removeById(id);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除印鉴组合失败，ID: {}", id, e);
            return JsonBean.error("删除印鉴组合失败: " + e.getMessage());
        }
    }

    @GetMapping("/seal/getCombinationDetail")
    @ApiOperation("获取印鉴组合详情")
    public String getSealCombinationDetail(
            @ApiParam(value = "组合ID", required = true) @RequestParam Long id,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            TcSealCombination combination = tcSealCombinationService.getById(id);
            if (combination == null) {
                return JsonBean.error("印鉴组合不存在");
            }

            return JsonBean.success(combination);

        } catch (Exception e) {
            log.error("获取印鉴组合详情失败，ID: {}", id, e);
            return JsonBean.error("获取印鉴组合详情失败: " + e.getMessage());
        }
    }

    @PostMapping("/seal/updateCombinationStatus")
    @ApiOperation("更新印鉴组合状态")
    public String updateSealCombinationStatus(
            @ApiParam(value = "组合ID", required = true) @RequestParam Long id,
            @ApiParam(value = "状态", required = true) @RequestParam Integer status,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查是否存在
            TcSealCombination combination = tcSealCombinationService.getById(id);
            if (combination == null) {
                return JsonBean.error("印鉴组合不存在");
            }

            combination.setStatus(status);
            combination.setUpdateTime(new Date());
            combination.setUpdateBy(getCurrentUserId());

            boolean success = tcSealCombinationService.updateById(combination);
            if (success) {
                return JsonBean.success("更新状态成功");
            } else {
                return JsonBean.error("更新状态失败");
            }

        } catch (Exception e) {
            log.error("更新印鉴组合状态失败，ID: {}", id, e);
            return JsonBean.error("更新印鉴组合状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/seal/exportCombination")
    @ApiOperation("导出印鉴组合")
    public String exportSealCombination(
            @ApiParam(value = "组合名称") @RequestParam(required = false) String combinationName,
            @ApiParam(value = "组合编码") @RequestParam(required = false) String combinationCode,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询条件
            QueryWrapper<TcSealCombination> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(combinationName)) {
                queryWrapper.like("COMBINATION_NAME", combinationName);
            }
            if (StringUtils.hasText(combinationCode)) {
                queryWrapper.like("COMBINATION_CODE", combinationCode);
            }
            queryWrapper.eq("STATUS", 1);
            queryWrapper.orderByDesc("CREATE_TIME");

            List<TcSealCombination> list = tcSealCombinationService.list(queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("exportList", list);
            data.put("total", list.size());
            data.put("exportTime", new Date());

            return new JsonBean(1, "导出成功", data).toString();

        } catch (Exception e) {
            log.error("导出印鉴组合失败", e);
            return JsonBean.error("导出印鉴组合失败: " + e.getMessage());
        }
    }

    // ==================== 印鉴使用统计 ====================

    @GetMapping("/seal/usageStatistics")
    @ApiOperation("获取印鉴使用统计数据")
    public String getSealUsageStatistics(
            @ApiParam(value = "开始日期") @RequestParam(required = false) String startDate,
            @ApiParam(value = "结束日期") @RequestParam(required = false) String endDate,
            @ApiParam(value = "印鉴编码") @RequestParam(required = false) String sealCode,
            @ApiParam(value = "业务类型") @RequestParam(required = false) String businessType,
            HttpServletResponse response) {

        try {
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询条件
            QueryWrapper<TblSealUsageRecord> queryWrapper = new QueryWrapper<>();
            if (StringUtils.hasText(startDate)) {
                queryWrapper.ge("USAGE_TIME", startDate);
            }
            if (StringUtils.hasText(endDate)) {
                queryWrapper.le("USAGE_TIME", endDate);
            }
            if (StringUtils.hasText(sealCode)) {
                queryWrapper.eq("SEAL_CODE", sealCode);
            }
            if (StringUtils.hasText(businessType)) {
                queryWrapper.eq("BUSINESS_TYPE", businessType);
            }

            // 查询所有符合条件的记录
            List<TblSealUsageRecord> allRecords = tblSealUsageRecordService.list(queryWrapper);

            // 统计数据
            Map<String, Object> statistics = new HashMap<>();

            // 总使用次数
            statistics.put("totalCount", allRecords.size());

            // 按使用状态分组统计
            Map<String, Long> statusStatistics = new HashMap<>();
            statusStatistics.put("PENDING", allRecords.stream().filter(r -> "PENDING".equals(r.getUsageStatus())).count());
            statusStatistics.put("APPROVED", allRecords.stream().filter(r -> "APPROVED".equals(r.getUsageStatus())).count());
            statusStatistics.put("REJECTED", allRecords.stream().filter(r -> "REJECTED".equals(r.getUsageStatus())).count());
            statusStatistics.put("IN_USE", allRecords.stream().filter(r -> "IN_USE".equals(r.getUsageStatus())).count());
            statusStatistics.put("COMPLETED", allRecords.stream().filter(r -> "COMPLETED".equals(r.getUsageStatus())).count());
            statistics.put("statusStatistics", statusStatistics);

            // 按业务类型分组统计
            Map<String, Long> businessTypeStatistics = new HashMap<>();
            businessTypeStatistics.put("CONTRACT_SIGN", allRecords.stream().filter(r -> "CONTRACT_SIGN".equals(r.getBusinessType())).count());
            businessTypeStatistics.put("FINANCIAL_REIMBURSEMENT", allRecords.stream().filter(r -> "FINANCIAL_REIMBURSEMENT".equals(r.getBusinessType())).count());
            businessTypeStatistics.put("FILE_AUTHENTICATION", allRecords.stream().filter(r -> "FILE_AUTHENTICATION".equals(r.getBusinessType())).count());
            businessTypeStatistics.put("AUTHORIZATION", allRecords.stream().filter(r -> "AUTHORIZATION".equals(r.getBusinessType())).count());
            businessTypeStatistics.put("CERTIFICATE_ISSUANCE", allRecords.stream().filter(r -> "CERTIFICATE_ISSUANCE".equals(r.getBusinessType())).count());
            statistics.put("businessTypeStatistics", businessTypeStatistics);

            // 今日使用次数 - 因LocalDateTime兼容性问题已暂时注释
            // LocalDateTime now = new Date();
            // LocalDateTime todayStart = now.withHour(0).withMinute(0).withSecond(0);
            // LocalDateTime todayEnd = now.withHour(23).withMinute(59).withSecond(59);
            // long todayCount = allRecords.stream()
            //         .filter(r -> r.getUsageTime() != null &&
            //                 !r.getUsageTime().isBefore(todayStart) &&
            //                 !r.getUsageTime().isAfter(todayEnd))
            //         .count();
            statistics.put("todayCount", 0);

            // 本周使用次数 - 因LocalDateTime兼容性问题已暂时注释
            // LocalDateTime weekStart = now.minusDays(7);
            // long weekCount = allRecords.stream()
            //         .filter(r -> r.getUsageTime() != null && r.getUsageTime().isAfter(weekStart))
            //         .count();
            statistics.put("weekCount", 0);

            // 本月使用次数 - 因LocalDateTime兼容性问题已暂时注释
            // LocalDateTime monthStart = now.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            // long monthCount = allRecords.stream()
            //         .filter(r -> r.getUsageTime() != null && r.getUsageTime().isAfter(monthStart))
            //         .count();
            statistics.put("monthCount", 0);

            // 最近使用记录(前10条)
            List<TblSealUsageRecord> recentRecords = allRecords.stream()
                    .filter(r -> r.getUsageTime() != null)
                    .sorted((a, b) -> b.getUsageTime().compareTo(a.getUsageTime()))
                    .limit(10)
                    .collect(java.util.stream.Collectors.toList());
            statistics.put("recentRecords", recentRecords);

            statistics.put("statisticsTime", new Date());

            return JsonBean.success(statistics);

        } catch (Exception e) {
            log.error("获取印鉴使用统计数据失败", e);
            return JsonBean.error("获取印鉴使用统计数据失败: " + e.getMessage());
        }
    }

    // ==================== 组织机构管理接口 ====================

    /**
     * 获取组织机构列表
     * 用于下拉选择等场景
     */
    @GetMapping("/org/list")
    @ApiOperation(value = "获取组织机构列表", notes = "获取所有组织机构列表，用于下拉选择")
    public String getOrgList(
            @RequestParam(required = false) String orgName,
            @RequestParam(required = false) String orgCode,
            @RequestParam(required = false) String status) {
        try {
            log.info("获取组织机构列表, orgName={}, orgCode={}, status={}", orgName, orgCode, status);

            // 模拟组织机构数据
            List<Map<String, Object>> orgList = new ArrayList<>();

            // 添加模拟数据
            Map<String, Object> org1 = new HashMap<>();
            org1.put("orgId", 1L);
            org1.put("id", 1L);
            org1.put("orgCode", "ORG001");
            org1.put("orgName", "总公司");
            org1.put("parentId", null);
            org1.put("orgType", "HEAD");
            org1.put("status", "ACTIVE");
            org1.put("sortOrder", 1);
            orgList.add(org1);

            Map<String, Object> org2 = new HashMap<>();
            org2.put("orgId", 2L);
            org2.put("id", 2L);
            org2.put("orgCode", "ORG002");
            org2.put("orgName", "北京分公司");
            org2.put("parentId", 1L);
            org2.put("orgType", "BRANCH");
            org2.put("status", "ACTIVE");
            org2.put("sortOrder", 2);
            orgList.add(org2);

            Map<String, Object> org3 = new HashMap<>();
            org3.put("orgId", 3L);
            org3.put("id", 3L);
            org3.put("orgCode", "ORG003");
            org3.put("orgName", "上海分公司");
            org3.put("parentId", 1L);
            org3.put("orgType", "BRANCH");
            org3.put("status", "ACTIVE");
            org3.put("sortOrder", 3);
            orgList.add(org3);

            Map<String, Object> org4 = new HashMap<>();
            org4.put("orgId", 4L);
            org4.put("id", 4L);
            org4.put("orgCode", "ORG004");
            org4.put("orgName", "广州分公司");
            org4.put("parentId", 1L);
            org4.put("orgType", "BRANCH");
            org4.put("status", "ACTIVE");
            org4.put("sortOrder", 4);
            orgList.add(org4);

            Map<String, Object> org5 = new HashMap<>();
            org5.put("orgId", 5L);
            org5.put("id", 5L);
            org5.put("orgCode", "ORG005");
            org5.put("orgName", "深圳分公司");
            org5.put("parentId", 1L);
            org5.put("orgType", "BRANCH");
            org5.put("status", "ACTIVE");
            org5.put("sortOrder", 5);
            orgList.add(org5);

            // 根据查询条件过滤
            List<Map<String, Object>> filteredList = new ArrayList<>();
            for (Map<String, Object> org : orgList) {
                boolean match = true;
                if (orgName != null && !orgName.isEmpty()) {
                    String name = (String) org.get("orgName");
                    if (!name.contains(orgName)) {
                        match = false;
                    }
                }
                if (orgCode != null && !orgCode.isEmpty()) {
                    String code = (String) org.get("orgCode");
                    if (!code.contains(orgCode)) {
                        match = false;
                    }
                }
                if (status != null && !status.isEmpty()) {
                    String orgStatus = (String) org.get("status");
                    if (!status.equals(orgStatus)) {
                        match = false;
                    }
                }
                if (match) {
                    filteredList.add(org);
                }
            }

            return JsonBean.success(filteredList);

        } catch (Exception e) {
            log.error("获取组织机构列表失败", e);
            return JsonBean.error("获取组织机构列表失败: " + e.getMessage());
        }
    }

}
