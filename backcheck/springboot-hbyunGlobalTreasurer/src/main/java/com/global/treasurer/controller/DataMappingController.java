package com.global.treasurer.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.global.treasurer.dto.export.ExportDataMappingDTO;
import com.global.treasurer.entity.TcDataMapping;
import com.global.treasurer.service.TcDataMappingService;
import com.global.treasurer.util.excel.ExcelExport;
import com.global.treasurer.util.excel.ExcelImport;
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
 * 数据映射配置管理Controller
 * 匹配前端API路径: /qqsk/financial/basicConfig/mapping/*
 *
 * @author 华博云开发团队
 * @since 2026-01-27
 */
@RestController
@RequestMapping("/financial/basicConfig/mapping")
@Api(tags = "数据映射配置管理")
public class DataMappingController {
    private static final Logger log = LoggerFactory.getLogger(DataMappingController.class);

    @Autowired
    private TcDataMappingService tcDataMappingService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ApiOperation("分页查询数据映射配置列表(GET)")
    public String getListGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String mappingName,
            @RequestParam(required = false) String sourceSystem,
            @RequestParam(required = false) String targetSystem,
            @RequestParam(required = false) String mappingType,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response) {

        // 构建参数Map
        Map<String, Object> params = new HashMap<>();
        if (pageNo != null) params.put("pageNo", pageNo);
        if (pageNum != null) params.put("pageNo", pageNum);
        if (pageSize != null) params.put("pageSize", pageSize);
        if (limit != null) params.put("pageSize", limit);
        if (mappingName != null) params.put("mappingName", mappingName);
        if (sourceSystem != null) params.put("sourceSystem", sourceSystem);
        if (targetSystem != null) params.put("targetSystem", targetSystem);
        if (mappingType != null) params.put("mappingType", mappingType);
        if (status != null) params.put("status", status);

        return getList(params, response);
    }

    @GetMapping("/page")
    @ApiOperation("分页查询数据映射配置列表(GET-分页)")
    public String getPageGet(
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String pageNum,
            @RequestParam(required = false) Integer limit,
            @RequestParam(required = false) String mappingName,
            @RequestParam(required = false) String sourceSystem,
            @RequestParam(required = false) String targetSystem,
            @RequestParam(required = false) String mappingType,
            @RequestParam(required = false) Integer status,
            HttpServletResponse response) {

        // 构建参数Map
        Map<String, Object> params = new HashMap<>();
        if (pageNo != null) params.put("pageNo", pageNo);
        if (pageNum != null) params.put("pageNo", pageNum);
        if (pageSize != null) params.put("pageSize", pageSize);
        if (limit != null) params.put("pageSize", limit);
        if (mappingName != null) params.put("mappingName", mappingName);
        if (sourceSystem != null) params.put("sourceSystem", sourceSystem);
        if (targetSystem != null) params.put("targetSystem", targetSystem);
        if (mappingType != null) params.put("mappingType", mappingType);
        if (status != null) params.put("status", status);

        return getList(params, response);
    }

    @PostMapping("/list")
    @ApiOperation("分页查询数据映射配置列表(POST)")
    public String getList(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();
            log.info("========== 数据映射查询接口 ==========");
            log.info("接收参数 - params: {}", params);

            // 支持 pageNo、page 两种参数命名（兼容前端）
            Integer pageNo = params.get("pageNo") != null ? Integer.parseInt(params.get("pageNo").toString()) :
                             (params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1);
            Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                              (params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 10);
            String mappingName = params.get("mappingName") != null ? params.get("mappingName").toString() : null;
            String sourceSystem = params.get("sourceSystem") != null ? params.get("sourceSystem").toString() : null;
            String targetSystem = params.get("targetSystem") != null ? params.get("targetSystem").toString() : null;
            String mappingType = params.get("mappingType") != null ? params.get("mappingType").toString() : null;
            Integer status = params.get("status") != null ? Integer.parseInt(params.get("status").toString()) : null;

            log.info("解析后参数 - pageNo: {}, pageSize: {}, mappingName: {}, sourceSystem: {}, targetSystem: {}, mappingType: {}, status: {}",
                    pageNo, pageSize, mappingName, sourceSystem, targetSystem, mappingType, status);

            IPage<TcDataMapping> result = tcDataMappingService.getDataMappingPage(pageNo, pageSize, mappingName, sourceSystem, targetSystem, mappingType, status);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 总记录数: {}, 当前页记录数: {}", result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();
        } catch (Exception e) {
            log.error("获取数据映射配置列表失败", e);
            return JsonBean.error("获取数据映射配置列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/create")
    @ApiOperation("新增数据映射配置(create)")
    public String create(@FlexibleRequestBody TcDataMapping dataMapping, HttpServletResponse response) {
        return add(dataMapping, response);
    }

    @PostMapping("/add")
    @ApiOperation("新增数据映射配置(add)")
    public String add(@FlexibleRequestBody TcDataMapping dataMapping, HttpServletResponse response) {
        try {
            if (!StringUtils.hasText(dataMapping.getMappingName())) {
                return JsonBean.error("映射名称不能为空");
            }

            dataMapping.setId(String.valueOf(System.currentTimeMillis()));
            dataMapping.setCreateTime(new Date());
            dataMapping.setUpdateTime(new Date());
            dataMapping.setCreateUser(getCurrentUser());
            dataMapping.setUpdateUser(getCurrentUser());

            boolean success = tcDataMappingService.save(dataMapping);
            return success ? JsonBean.success("创建成功") : JsonBean.error("创建失败");
        } catch (Exception e) {
            log.error("创建数据映射配置失败", e);
            return JsonBean.error("创建数据映射配置失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新数据映射配置(PUT)")
    public String updatePut(@FlexibleRequestBody TcDataMapping dataMapping, HttpServletResponse response) {
        return update(dataMapping, response);
    }

    @PostMapping("/update")
    @ApiOperation("更新数据映射配置(POST)")
    public String update(@FlexibleRequestBody TcDataMapping dataMapping, HttpServletResponse response) {
        try {
            if (dataMapping.getId() == null) {
                return JsonBean.error("映射ID不能为空");
            }
            dataMapping.setUpdateTime(new Date());
            dataMapping.setUpdateUser(getCurrentUser());

            boolean success = tcDataMappingService.updateById(dataMapping);
            return success ? JsonBean.success("更新成功") : JsonBean.error("更新失败");
        } catch (Exception e) {
            log.error("更新数据映射配置失败", e);
            return JsonBean.error("更新数据映射配置失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除数据映射配置(路径参数)")
    public String deleteByPath(@PathVariable String id, HttpServletResponse response) {
        try {
            if (id == null) return JsonBean.error("ID不能为空");

            boolean success = tcDataMappingService.removeById(id);
            return success ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除数据映射配置失败", e);
            return JsonBean.error("删除数据映射配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/delete")
    @ApiOperation("删除数据映射配置(POST)")
    public String delete(@FlexibleRequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            if (params == null) params = new HashMap<>();
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return JsonBean.error("ID不能为空");

            boolean success = tcDataMappingService.removeById(id);
            return success ? JsonBean.success("删除成功") : JsonBean.error("删除失败");
        } catch (Exception e) {
            log.error("删除数据映射配置失败", e);
            return JsonBean.error("删除数据映射配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/checkCode")
    @ApiOperation("检查映射编码唯一性")
    public String checkCode(@FlexibleRequestBody TcDataMapping dataMapping, HttpServletResponse response) {
        try {
            if (dataMapping == null || !StringUtils.hasText(dataMapping.getMappingCode())) {
                return JsonBean.error("映射编码不能为空");
            }

            String mappingCode = dataMapping.getMappingCode().trim();
            boolean exists = tcDataMappingService.existsByMappingCode(mappingCode);

            Map<String, Object> result = new HashMap<>();
            result.put("exists", exists);
            result.put("code", mappingCode);

            if (exists) {
                return new JsonBean(0, "编码已存在", result).toString();
            } else {
                return new JsonBean(1, "编码可用", result).toString();
            }
        } catch (Exception e) {
            log.error("检查映射编码失败", e);
            return JsonBean.error("检查编码失败: " + e.getMessage());
        }
    }

    @PostMapping("/testMapping")
    @ApiOperation("测试数据映射")
    public String testMapping(@RequestParam Map<String, Object> params, HttpServletResponse response) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            String testData = params.get("testData") != null ? params.get("testData").toString() : null;

            if (id == null || testData == null) {
                return JsonBean.error("参数不完整");
            }

            // 获取映射配置
            TcDataMapping dataMapping = tcDataMappingService.getById(id);
            if (dataMapping == null) {
                return JsonBean.error("数据映射配置不存在");
            }

            // 执行映射转换
            String mappingType = dataMapping.getMappingType();
            String mappingRule = dataMapping.getMappingRule();
            String result = testData;

            if ("FUNCTION".equals(mappingType) && mappingRule != null) {
                // 函数映射
                if (mappingRule.contains("parseFloat")) {
                    try {
                        double value = Double.parseDouble(testData);
                        result = String.format("%.2f", value);
                    } catch (NumberFormatException e) {
                        result = testData;
                    }
                } else if (mappingRule.contains("parseInt")) {
                    try {
                        int value = Integer.parseInt(testData);
                        result = String.valueOf(value);
                    } catch (NumberFormatException e) {
                        result = testData;
                    }
                } else if (mappingRule.contains("toUpperCase")) {
                    result = testData.toUpperCase();
                } else if (mappingRule.contains("toLowerCase")) {
                    result = testData.toLowerCase();
                } else if (mappingRule.contains("trim")) {
                    result = testData.trim();
                }
            } else if ("VALUE".equals(mappingType) && mappingRule != null) {
                // 值映射
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    Map<String, String> valueMapping = mapper.readValue(mappingRule, Map.class);
                    result = valueMapping.getOrDefault(testData, testData);
                } catch (Exception e) {
                    log.warn("解析值映射规则失败", e);
                }
            } else if ("FORMAT".equals(mappingType) && mappingRule != null) {
                // 格式化映射
                try {
                    if (mappingRule.contains("yyyy-MM-dd")) {
                        java.text.SimpleDateFormat fromFormat = new java.text.SimpleDateFormat("yyyyMMdd");
                        java.text.SimpleDateFormat toFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                        java.util.Date date = fromFormat.parse(testData);
                        result = toFormat.format(date);
                    } else if (mappingRule.contains("yyyy/MM/dd")) {
                        java.text.SimpleDateFormat fromFormat = new java.text.SimpleDateFormat("yyyyMMdd");
                        java.text.SimpleDateFormat toFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
                        java.util.Date date = fromFormat.parse(testData);
                        result = toFormat.format(date);
                    }
                } catch (Exception e) {
                    log.warn("日期格式转换失败", e);
                }
            }

            // 构建测试结果
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("input", testData);
            testResult.put("output", result);
            testResult.put("mappingType", mappingType);
            testResult.put("mappingRule", mappingRule);
            testResult.put("mappingName", dataMapping.getMappingName());

            return new JsonBean(1, "映射测试成功", testResult).toString();
        } catch (Exception e) {
            log.error("测试数据映射失败", e);
            return JsonBean.error("测试数据映射失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail")
    @ApiOperation("获取数据映射详情")
    public String getDetail(@RequestParam String id, HttpServletResponse response) {
        try {
            TcDataMapping dataMapping = tcDataMappingService.getById(id);
            if (dataMapping == null) {
                return JsonBean.error("数据映射配置不存在");
            }
            return new JsonBean(1, "查询成功", dataMapping).toString();
        } catch (Exception e) {
            log.error("获取数据映射详情失败", e);
            return JsonBean.error("获取数据映射详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/getBySystemId")
    @ApiOperation("根据系统ID获取映射列表")
    public String getBySystemId(@RequestParam String systemId, HttpServletResponse response) {
        try {
            java.util.List<TcDataMapping> list = tcDataMappingService.getBySystemId(systemId);
            return new JsonBean(1, "查询成功", list).toString();
        } catch (Exception e) {
            log.error("根据系统ID获取映射列表失败", e);
            return JsonBean.error("获取映射列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/refresh")
    @ApiOperation("刷新数据映射配置")
    public String refresh(HttpServletResponse response) {
        try {
            log.info("开始刷新数据映射配置...");

            // 调用Service层的刷新方法
            Map<String, Object> result = tcDataMappingService.refreshMapping();

            if (Boolean.TRUE.equals(result.get("success"))) {
                return new JsonBean(1, (String) result.get("message"), result).toString();
            } else {
                return JsonBean.error((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("刷新数据映射配置失败", e);
            return JsonBean.error("刷新失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出数据映射配置")
    public void export(@RequestParam(required = false) String mappingName,
                       @RequestParam(required = false) String sourceSystem,
                       @RequestParam(required = false) String targetSystem,
                       @RequestParam(required = false) Integer status,
                       HttpServletResponse response) {
        try {
            // 查询数据
            IPage<TcDataMapping> result = tcDataMappingService.getDataMappingPage(1, 10000, mappingName, sourceSystem, targetSystem, null, status);

            // 转换为导出DTO
            List<ExportDataMappingDTO> exportList = result.getRecords().stream()
                    .map(ExportDataMappingDTO::fromEntity)
                    .collect(Collectors.toList());

            // 生成Excel并导出
            String filename = "数据映射配置_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("数据映射配置", ExportDataMappingDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出数据映射配置失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(JsonBean.error("导出失败: " + e.getMessage()));
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @PostMapping("/import")
    @ApiOperation("导入数据映射配置")
    public String importMapping(@RequestPart("file") MultipartFile file, HttpServletResponse response) {
        try {
            if (file == null || file.isEmpty()) {
                return JsonBean.error("请选择要导入的文件");
            }

            String filename = file.getOriginalFilename();
            if (filename == null || (!filename.endsWith(".xlsx") && !filename.endsWith(".xls"))) {
                return JsonBean.error("请上传Excel文件(.xlsx或.xls格式)");
            }

            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            try (ExcelImport excelImport = new ExcelImport(file, 1, 0)) {
                List<TcDataMapping> dataList = excelImport.getDataList(TcDataMapping.class);

                for (TcDataMapping mapping : dataList) {
                    try {
                        // 设置基本信息
                        mapping.setId(String.valueOf(System.currentTimeMillis() + successCount));
                        mapping.setCreateTime(new Date());
                        mapping.setUpdateTime(new Date());
                        mapping.setCreateUser(getCurrentUser());
                        mapping.setUpdateUser(getCurrentUser());

                        // 保存数据
                        boolean saved = tcDataMappingService.save(mapping);
                        if (saved) {
                            successCount++;
                        } else {
                            failCount++;
                            errorMessages.add("保存失败: " + mapping.getMappingName());
                        }
                    } catch (Exception e) {
                        failCount++;
                        errorMessages.add("导入失败[" + mapping.getMappingName() + "]: " + e.getMessage());
                    }
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);
            result.put("message", String.format("导入完成，成功%d条，失败%d条", successCount, failCount));

            return new JsonBean(1, "导入完成", result).toString();
        } catch (Exception e) {
            log.error("导入数据映射配置失败", e);
            return JsonBean.error("导入失败: " + e.getMessage());
        }
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

