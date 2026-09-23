package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcDataSourceConfig;
import com.global.treasurer.service.TcDataSourceConfigService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据源配置管理Controller
 * 完整实现增删改查和特殊操作
 * 适配现有的TcDataSourceConfigService
 */
@RestController
@RequestMapping("/financial/data-rules/data-source-config")
@Api(tags = "数据源配置管理")
public class DataSourceConfigController {

    private static final Logger log = LoggerFactory.getLogger(DataSourceConfigController.class);

    @Autowired
    private TcDataSourceConfigService dataSourceConfigService;

    @Autowired
    private com.global.treasurer.mapper.TcDataSourceConfigMapper dataSourceConfigMapper;

    @Autowired
    private UserProvider userProvider;

    /**
     * 1. 分页查询数据源配置列表 (GET方式)
     * 前端API: GET /qqsk/financial/data-rules/data-source-config/list
     */
    @GetMapping({"/list", ""})
    @ApiOperation("分页查询数据源配置列表(GET)")
    public String getListByGet(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "20") int limit,
            @RequestParam(value = "pageNo", required = false) Integer pageNo,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "dataSourceCode", required = false) String dataSourceCode,
            @RequestParam(value = "dataSourceName", required = false) String dataSourceName,
            @RequestParam(value = "dataSourceType", required = false) String dataSourceType,
            @RequestParam(value = "environment", required = false) String environment,
            @RequestParam(value = "connectionStatus", required = false) String connectionStatus,
            HttpServletResponse response) {

        // 兼容两种分页参数名
        int finalPageNo = pageNo != null ? pageNo : page;
        int finalPageSize = pageSize != null ? pageSize : limit;

        // 构建参数Map
        Map<String, Object> params = new HashMap<>();
        params.put("page", finalPageNo);
        params.put("limit", finalPageSize);
        if (dataSourceCode != null) params.put("dataSourceCode", dataSourceCode);
        if (dataSourceName != null) params.put("dataSourceName", dataSourceName);
        if (dataSourceType != null) params.put("dataSourceType", dataSourceType);
        if (environment != null) params.put("environment", environment);
        if (connectionStatus != null) params.put("connectionStatus", connectionStatus);

        return doGetList(params);
    }

    /**
     * 1. 分页查询数据源配置列表 (POST方式)
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/list
     */
    @PostMapping({"/list"})
    @ApiOperation("分页查询数据源配置列表(POST)")
    public String getListByPost(@RequestParam(required = false) Map<String, Object> params) {
        return doGetList(params);
    }

    /**
     * 实际执行分页查询的方法
     */
    private String doGetList(Map<String, Object> params) {
        try {
            log.info("开始查询数据源配置列表，参数: {}", params);

            int pageNo = 1;
            int pageSize = 20;

            if (params != null) {
                try {
                    if (params.get("page") != null) {
                        pageNo = Integer.parseInt(params.get("page").toString());
                    } else if (params.get("pageNo") != null) {
                        pageNo = Integer.parseInt(params.get("pageNo").toString());
                    }
                    if (params.get("limit") != null) {
                        pageSize = Integer.parseInt(params.get("limit").toString());
                    } else if (params.get("pageSize") != null) {
                        pageSize = Integer.parseInt(params.get("pageSize").toString());
                    }
                } catch (NumberFormatException e) {
                    log.warn("分页参数解析失败，使用默认值: page={}, pageSize={}", pageNo, pageSize);
                }
            }

            // 构建查询条件
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TcDataSourceConfig> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.eq("STATUS", "1");
            if (params != null) {
                if (params.get("dataSourceCode") != null && !"".equals(params.get("dataSourceCode").toString().trim())) {
                    queryWrapper.like("SOURCE_CODE", params.get("dataSourceCode").toString());
                }
                if (params.get("dataSourceName") != null && !"".equals(params.get("dataSourceName").toString().trim())) {
                    queryWrapper.like("SOURCE_NAME", params.get("dataSourceName").toString());
                }
                if (params.get("dataSourceType") != null && !"".equals(params.get("dataSourceType").toString().trim())) {
                    queryWrapper.eq("SOURCE_TYPE", params.get("dataSourceType").toString());
                }
            }
            queryWrapper.orderByDesc("CREATE_TIME");

            // 使用 PageHelper 分页，与其他接口保持一致，避免 MyBatis-Plus 方言问题
            com.github.pagehelper.PageHelper.startPage(pageNo, pageSize);
            java.util.List<TcDataSourceConfig> list = dataSourceConfigMapper.selectList(queryWrapper);
            com.github.pagehelper.PageInfo<TcDataSourceConfig> pageInfo = new com.github.pagehelper.PageInfo<>(list);

            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageInfo.getList());
            data.put("totalRecord", pageInfo.getTotal());
            data.put("pageNo", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());

            log.info("查询数据源配置列表成功，共{}条记录", pageInfo.getTotal());
            return new JsonBean(1, "查询成功", data).toJson();
        } catch (Exception e) {
            log.error("获取数据源配置列表失败", e);
            com.github.pagehelper.PageHelper.clearPage();
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", new java.util.ArrayList<>());
            data.put("totalRecord", 0);
            data.put("pageNo", 1);
            data.put("pageSize", 20);
            return new JsonBean(0, "查询失败: " + e.getMessage(), data).toJson();
        }
    }

    /**
     * 2. 新增数据源配置
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/save
     */
    @PostMapping("/save")
    @ApiOperation("新增数据源配置")
    public String save(@FlexibleRequestBody TcDataSourceConfig dataSourceConfig) {
        try {
            // 验证用户
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String operator = loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system";

            // 调用Service保存
            int result = dataSourceConfigService.saveOrUpdate(dataSourceConfig, operator);
            if (result > 0) {
                return new JsonBean(1, "新增成功", dataSourceConfig).toJson();
            } else {
                return new JsonBean(0, "新增失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("新增数据源配置失败", e);
            return new JsonBean(0, "新增数据源配置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 3. 更新数据源配置
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/update
     */
    @PostMapping("/update")
    @ApiOperation("更新数据源配置")
    public String update(@FlexibleRequestBody TcDataSourceConfig dataSourceConfig) {
        try {
            // 验证用户
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String operator = loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system";

            // 调用Service更新
            int result = dataSourceConfigService.saveOrUpdate(dataSourceConfig, operator);
            if (result > 0) {
                return new JsonBean(1, "更新成功", dataSourceConfig).toJson();
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("更新数据源配置失败", e);
            return new JsonBean(0, "更新数据源配置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 4. 删除数据源配置
     * 前端API: DELETE /qqsk/financial/data-rules/data-source-config/delete/{id}
     */
    @DeleteMapping("/delete/{id}")
    @ApiOperation("删除数据源配置")
    public String delete(@PathVariable String id) {
        try {
            // 验证用户
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String operator = loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system";

            // 调用Service删除
            int result = dataSourceConfigService.delete(id, operator);
            if (result > 0) {
                return new JsonBean(1, "删除成功", null).toJson();
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("删除数据源配置失败", e);
            return new JsonBean(0, "删除数据源配置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 5. 批量删除数据源配置
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/batchDelete
     */
    @PostMapping("/batchDelete")
    @ApiOperation("批量删除数据源配置")
    public String batchDelete(@RequestParam(value = "ids", required = false) List<String> ids) {
        try {
            // 验证用户
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String operator = loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system";
            int successCount = 0;

            // 逐个删除
            for (String id : ids) {
                try {
                    int result = dataSourceConfigService.delete(id, operator);
                    if (result > 0) {
                        successCount++;
                    }
                } catch (Exception e) {
                    log.error("删除ID={}的数据源配置失败", id, e);
                }
            }

            if (successCount > 0) {
                return new JsonBean(1, "批量删除成功，共删除" + successCount + "条记录", null).toJson();
            } else {
                return new JsonBean(0, "批量删除失败", null).toJson();
            }
        } catch (Exception e) {
            log.error("批量删除数据源配置失败", e);
            return new JsonBean(0, "批量删除数据源配置失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 6. 切换数据源配置状态
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/toggleStatus
     */
    @PostMapping("/toggleStatus")
    @ApiOperation("切换数据源配置状态")
    public String toggleStatus(@RequestParam Map<String, Object> params) {
        try {
            // 验证用户
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            String dataSourceId = params.get("dataSourceId").toString();
            String isEnabled = params.get("isEnabled").toString();
            String operator = loginStaff.getStaffid() != null ? loginStaff.getStaffid().toString() : "system";

            // 调用Service更新状态
            dataSourceConfigService.updateEnabledStatus(dataSourceId, isEnabled, operator);

            return new JsonBean(1, "状态更新成功", null).toJson();
        } catch (Exception e) {
            log.error("切换数据源配置状态失败", e);
            return new JsonBean(0, "状态更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 7. 测试数据源连接
     * 前端API: POST /qqsk/financial/data-rules/data-source-config/testConnection
     */
    @PostMapping("/testConnection")
    @ApiOperation("测试数据源连接")
    public String testConnection(@RequestParam Map<String, Object> params) {
        try {
            String dataSourceId = params.get("dataSourceId").toString();

            TcDataSourceConfig config = dataSourceConfigService.getById(dataSourceId);
            if (config == null) {
                return new JsonBean(0, "数据源不存在", null).toJson();
            }

            // 调用Service测试连接
            Map<String, Object> result = dataSourceConfigService.testConnection(config);

            return new JsonBean(1, "连接测试完成", result).toJson();
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return new JsonBean(0, "连接测试失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 8. 根据ID查询数据源配置
     * 前端API: GET /qqsk/financial/data-rules/data-source-config/{id}
     */
    @GetMapping("/{id}")
    @ApiOperation("根据ID查询数据源配置")
    public String getById(@PathVariable String id) {
        try {
            TcDataSourceConfig config = dataSourceConfigService.getById(id);
            if (config != null) {
                return new JsonBean(1, "查询成功", config).toJson();
            } else {
                return new JsonBean(0, "数据不存在", null).toJson();
            }
        } catch (Exception e) {
            log.error("查询数据源配置失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 9. 根据类型查询数据源配置
     * 前端API: GET /qqsk/financial/data-rules/data-source-config/byType/{sourceType}
     */
    @GetMapping("/byType/{sourceType}")
    @ApiOperation("根据类型查询数据源配置")
    public String getByType(@PathVariable String sourceType) {
        try {
            List<TcDataSourceConfig> list = dataSourceConfigService.getBySourceType(sourceType);

            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("根据类型查询数据源配置失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 10. 查询启用的数据源配置
     * 前端API: GET /qqsk/financial/data-rules/data-source-config/enabled
     */
    @GetMapping("/enabled")
    @ApiOperation("查询启用的数据源配置")
    public String getEnabled() {
        try {
            List<TcDataSourceConfig> list = dataSourceConfigService.getEnabledSources(null);

            return new JsonBean(1, "查询成功", list).toJson();
        } catch (Exception e) {
            log.error("查询启用的数据源配置失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 11. 导出数据源配置
     * 前端API: GET /qqsk/financial/data-rules/data-source-config/export
     */
    @GetMapping("/export")
    @ApiOperation("导出数据源配置")
    public void export(@RequestParam(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            // TODO: 实现Excel导出功能
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            response.setHeader("Content-disposition", "attachment;filename=dataSourceConfig.xlsx");
        } catch (Exception e) {
            log.error("导出数据源配置失败", e);
        }
    }
}
