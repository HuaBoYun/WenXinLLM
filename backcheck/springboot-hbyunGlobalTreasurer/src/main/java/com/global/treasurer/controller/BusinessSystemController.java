package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.dto.export.ExportBusinessSystemDTO;
import com.global.treasurer.dto.imports.ImportResultDTO;
import com.global.treasurer.entity.TcBusinessSystem;
import com.global.treasurer.service.TcBusinessSystemService;
import com.global.treasurer.util.excel.ExcelExport;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 业务系统注册管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/system/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping({"/financial/basicConfig/system", "/cwgxAi/basicConfig/system", "/xjgl/basicConfig/system", "/basicConfig/system"})
@Api(tags = "业务系统注册管理")
public class BusinessSystemController {
    private static final Logger log = LoggerFactory.getLogger(BusinessSystemController.class);

    @Autowired
    private TcBusinessSystemService tcBusinessSystemService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ApiOperation("分页查询业务系统列表(GET)")
    public String getListGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String systemCode,
            @RequestParam(required = false) String systemName,
            @RequestParam(required = false) String systemType,
            @RequestParam(required = false) String connectionStatus,
            @RequestParam(required = false) String status,
            HttpServletResponse response) {

        try {
            // 构建参数Map
            Map<String, Object> params = new HashMap<>();

            // 设置默认值
            if (pageNo == null && pageNum == null) {
                params.put("pageNo", 1);
            } else if (pageNo != null) {
                params.put("pageNo", pageNo);
            } else if (pageNum != null) {
                params.put("pageNo", pageNum);
            }

            if (pageSize == null && limit == null) {
                params.put("pageSize", 20);
            } else if (pageSize != null) {
                params.put("pageSize", pageSize);
            } else if (limit != null) {
                params.put("pageSize", limit);
            }

            if (systemCode != null) params.put("systemCode", systemCode);
            if (systemName != null) params.put("systemName", systemName);
            if (systemType != null) params.put("systemType", systemType);
            if (connectionStatus != null) params.put("connectionStatus", connectionStatus);
            if (status != null) params.put("status", status);

            return getList(params, response);
        } catch (Exception e) {
            log.error("GET方式查询业务系统列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询业务系统列表(GET-分页)")
    public String getPageGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String systemCode,
            @RequestParam(required = false) String systemName,
            @RequestParam(required = false) String systemType,
            @RequestParam(required = false) String connectionStatus,
            @RequestParam(required = false) String status,
            HttpServletResponse response) {

        try {
            // 构建参数Map
            Map<String, Object> params = new HashMap<>();

            // 设置默认值
            if (pageNo == null && pageNum == null) {
                params.put("pageNo", 1);
            } else if (pageNo != null) {
                params.put("pageNo", pageNo);
            } else if (pageNum != null) {
                params.put("pageNo", pageNum);
            }

            if (pageSize == null && limit == null) {
                params.put("pageSize", 20);
            } else if (pageSize != null) {
                params.put("pageSize", pageSize);
            } else if (limit != null) {
                params.put("pageSize", limit);
            }

            if (systemCode != null) params.put("systemCode", systemCode);
            if (systemName != null) params.put("systemName", systemName);
            if (systemType != null) params.put("systemType", systemType);
            if (connectionStatus != null) params.put("connectionStatus", connectionStatus);
            if (status != null) params.put("status", status);

            return getList(params, response);
        } catch (Exception e) {
            log.error("GET方式分页查询业务系统列表失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/list")
    @ApiOperation("分页查询业务系统列表(POST)")
    public String getList(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();
            log.info("========== 业务系统查询接口 ==========");
            log.info("接收参数 - params: {}", params);

            // 支持 pageNo、page 两种参数命名（兼容前端）
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) :
                             (params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1);
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                              (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10);
            String systemCode = params.get("systemCode") != null ? params.get("systemCode").toString() : null;
            String systemName = params.get("systemName") != null ? params.get("systemName").toString() : null;
            String systemType = params.get("systemType") != null ? params.get("systemType").toString() : null;
            String connectionStatus = params.get("connectionStatus") != null ? params.get("connectionStatus").toString() : null;
            String status = params.get("status") != null ? params.get("status").toString() : null;

            log.info("解析后参数 - pageNo: {}, pageSize: {}, systemName: {}, systemType: {}", pageNo, pageSize, systemName, systemType);

            IPage<TcBusinessSystem> result = tcBusinessSystemService.getBusinessSystemPage(pageNo, pageSize, systemCode, systemName, systemType, connectionStatus, status);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取业务系统列表失败", e);
            return JsonBean.error("获取业务系统列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取业务系统详情")
    public String getDetail(@RequestParam Long id, HttpServletResponse response) {
        try {
            if (id == null) {
                return new JsonBean(0, "ID不能为空", null).toJson();
            }

            TcBusinessSystem system = tcBusinessSystemService.getById(id);
            if (system == null) {
                return new JsonBean(0, "业务系统不存在", null).toJson();
            }
            return new JsonBean(1, "查询成功", system).toJson();
        } catch (Exception e) {
            log.error("获取业务系统详情失败，ID: {}", id, e);
            return new JsonBean(0, "获取详情失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增业务系统")
    public String create(@FlexibleRequestBody TcBusinessSystem businessSystem, HttpServletResponse response) {
        return add(businessSystem, response);
    }

    @PostMapping("/add")
    @ApiOperation("新增业务系统")
    public String add(@FlexibleRequestBody TcBusinessSystem businessSystem, HttpServletResponse response) {
        try {
            if (!StringUtils.hasText(businessSystem.getSystemName())) {
                return JsonBean.error("系统名称不能为空");
            }
            if (!StringUtils.hasText(businessSystem.getSystemCode())) {
                return JsonBean.error("系统编码不能为空");
            }

            businessSystem.setId(System.currentTimeMillis());
            businessSystem.setCreateTime(new Date());
            businessSystem.setUpdateTime(new Date());
            businessSystem.setCreateUser(getCurrentUser());
            businessSystem.setUpdateUser(getCurrentUser());

            boolean success = tcBusinessSystemService.save(businessSystem);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建业务系统失败", e);
            return JsonBean.error("创建业务系统失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新业务系统(PUT)")
    public String updatePut(@FlexibleRequestBody TcBusinessSystem businessSystem, HttpServletResponse response) {
        return update(businessSystem, response);
    }

    @PostMapping("/update")
    @ApiOperation("更新业务系统(POST)")
    public String update(@FlexibleRequestBody TcBusinessSystem businessSystem, HttpServletResponse response) {
        try {
            if (businessSystem.getId() == null) {
                return JsonBean.error("系统ID不能为空");
            }
            businessSystem.setUpdateTime(new Date());
            businessSystem.setUpdateUser(getCurrentUser());

            boolean success = tcBusinessSystemService.updateById(businessSystem);
            return success ? JsonBean.success("更新成功") : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新业务系统失败", e);
            return JsonBean.error("更新业务系统失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除业务系统(路径参数)")
    public String deleteByPath(@PathVariable Long id, HttpServletResponse response) {
        return deleteByParam(id, response);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除业务系统(请求参数)")
    public String deleteByParam(@RequestParam Long id, HttpServletResponse response) {
        try {
            if (id == null) {
                return new JsonBean(0, "ID不能为空", null).toJson();
            }
            boolean success = tcBusinessSystemService.removeById(id);
            return new JsonBean(success ? 1 : 0, success ? "删除成功" : "删除失败", null).toJson();
        } catch (Exception e) {
            log.error("删除业务系统失败，ID: {}", id, e);
            return new JsonBean(0, "删除业务系统失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/checkCode")
    @ApiOperation("检查系统编码唯一性")
    public String checkCode(@FlexibleRequestBody TcBusinessSystem businessSystem, HttpServletResponse response) {
        try {
            if (businessSystem == null || !StringUtils.hasText(businessSystem.getSystemCode())) {
                return JsonBean.error("系统编码不能为空");
            }

            String systemCode = businessSystem.getSystemCode().trim();
            boolean exists = tcBusinessSystemService.existsBySystemCode(systemCode);

            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            result.put("code", systemCode);

            if (exists) {
                return new JsonBean(0, "编码已存在", result).toString();
            } else {
                return new JsonBean(1, "编码可用", result).toString();
            }
        } catch (Exception e) {
            log.error("检查系统编码失败", e);
            return JsonBean.error("检查编码失败: " + e.getMessage());
        }
    }

    @PostMapping("/testConnection")
    @ApiOperation("测试系统连接")
    public String testConnection(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            log.info("========== 测试系统连接接口 ==========");
            log.info("接收参数 - params: {}", params);

            Long id = null;
            Object idObj = params.get("id");
            if (idObj != null) {
                id = Long.parseLong(idObj.toString());
            } else {
                idObj = params.get("systemId");
                if (idObj != null) {
                    id = Long.parseLong(idObj.toString());
                }
            }

            log.info("解析后的系统ID: {}", id);

            if (id == null) {
                log.warn("系统ID为空");
                return JsonBean.error("系统ID不能为空");
            }

            boolean result = tcBusinessSystemService.testSystemConnection(id);
            log.info("测试连接结果: {}", result);
            return result ? JsonBean.success("连接测试成功") : JsonBean.error("连接测试失败");
        } catch (Exception e) {
            log.error("测试系统连接失败", e);
            return JsonBean.error("测试系统连接失败: " + (e.getMessage() != null ? e.getMessage() : "未知错误"));
        }
    }

    @PostMapping("/syncStatus")
    @ApiOperation("同步系统状态")
    public String syncStatus(HttpServletResponse response) {
        try {
            // 同步所有业务系统的状态（不需要id参数）
            // 这里可以添加实际的同步逻辑，比如调用第三方API同步状态
            return JsonBean.success("同步成功");
        } catch (Exception e) {
            log.error("同步系统状态失败", e);
            return JsonBean.error("同步系统状态失败: " + e.getMessage());
        }
    }

    @GetMapping("/getSystemTypes")
    @ApiOperation("获取系统类型下拉数据")
    public String getSystemTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("ERP", "ERP系统"),
                createOption("CRM", "CRM系统"),
                createOption("OA", "OA系统"),
                createOption("HR", "HR系统"),
                createOption("FINANCE", "财务系统"),
                createOption("OTHER", "其他系统")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取系统类型失败: " + e.getMessage());
        }
    }

    @GetMapping("/getAuthTypes")
    @ApiOperation("获取认证方式下拉数据")
    public String getAuthTypes(HttpServletResponse response) {
        try {
            List<Map<String, String>> types = Arrays.asList(
                createOption("NONE", "无认证"),
                createOption("BASIC", "Basic认证"),
                createOption("TOKEN", "Token认证"),
                createOption("OAUTH2", "OAuth2认证"),
                createOption("CERTIFICATE", "证书认证")
            );
            return new JsonBean(1, "查询成功", types).toString();
        } catch (Exception e) {
            return JsonBean.error("获取认证方式失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出业务系统数据")
    public void export(HttpServletResponse response,
                       @RequestParam(required = false) String systemName,
                       @RequestParam(required = false) String systemType,
                       @RequestParam(required = false) String status) {
        try {
            // 构建查询条件
            LambdaQueryWrapper<TcBusinessSystem> queryWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.hasText(systemName)) {
                queryWrapper.like(TcBusinessSystem::getSystemName, systemName);
            }
            if (StringUtils.hasText(systemType)) {
                queryWrapper.eq(TcBusinessSystem::getSystemType, systemType);
            }
            queryWrapper.orderByDesc(TcBusinessSystem::getCreateTime);

            // 查询数据
            List<TcBusinessSystem> list = tcBusinessSystemService.list(queryWrapper);

            // 转换为导出DTO
            List<ExportBusinessSystemDTO> exportList = list.stream()
                    .map(ExportBusinessSystemDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "业务系统列表_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("业务系统列表", ExportBusinessSystemDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出业务系统数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @PostMapping("/import")
    @ApiOperation("批量导入业务系统")
    public String importData(@RequestParam("file") MultipartFile file) {
        try {
            // 验证文件
            if (file == null || file.isEmpty()) {
                return JsonBean.error("请选择要导入的文件");
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xls") && !filename.endsWith(".xlsx"))) {
                return JsonBean.error("请上传Excel文件（.xls或.xlsx格式）");
            }

            // 执行导入
            ImportResultDTO result = tcBusinessSystemService.importBusinessSystems(file, getCurrentUser());

            // 构建返回信息
            String message = String.format("导入完成：成功%d条，失败%d条",
                    result.getSuccessCount(), result.getFailCount());

            return new JsonBean(1, message, result).toString();
        } catch (Exception e) {
            log.error("导入业务系统失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
    }

    private Map<String, String> createOption(String value, String label) {
        Map<String, String> option = new HashMap<>();
        option.put("value", value);
        option.put("label", label);
        return option;
    }

    private String getCurrentUser() {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            return loginStaff != null ? loginStaff.getUsername() : "system";
        } catch (Exception e) {
            return "system";
        }
    }
}

