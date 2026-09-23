package com.financial.sharing.controller;

import com.financial.sharing.dto.*;
import com.financial.sharing.service.DataSourceService;
import com.financial.sharing.vo.param.DataSourceBatchParam;
import com.financial.sharing.vo.param.DataSourceImportParam;
import com.financial.sharing.vo.param.DataSourceExportParam;
import com.hbfk.util.JsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 数据源管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/data-source")
@CrossOrigin
@Api(tags = "数据源管理")
public class DataSourceController {

    @Resource
    private DataSourceService dataSourceService;

    private JsonBean createSuccessJsonBean(Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("操作成功");
        json.setData(data);
        return json;
    }

    private JsonBean createErrorJsonBean(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return json;
    }

    @PostMapping("/config/save")
    @ApiOperation("保存数据源配置")
    public JsonBean saveDataSourceConfig(@RequestBody DataSourceConfigParam param) {
        try {
            Long configId = dataSourceService.saveDataSourceConfig(param);
            return createSuccessJsonBean(configId);
        } catch (Exception e) {
            log.error("保存数据源配置失败", e);
            return createErrorJsonBean("保存数据源配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/config/list")
    @ApiOperation("查询数据源配置列表")
    public JsonBean getDataSourceConfigList(@RequestBody DataSourceQueryParam param) {
        try {
            Map<String, Object> result = dataSourceService.getDataSourceConfigList(param);
            return createSuccessJsonBean(result);
        } catch (Exception e) {
            log.error("查询数据源配置列表失败", e);
            return createErrorJsonBean("查询数据源配置列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/config/test")
    @ApiOperation("测试数据源连接")
    public JsonBean testDataSource(@RequestBody DataSourceTestParam param) {
        try {
            Map<String, Object> result = dataSourceService.testDataSource(param);
            return createSuccessJsonBean(result);
        } catch (Exception e) {
            log.error("测试数据源连接失败", e);
            return createErrorJsonBean("测试数据源连接失败: " + e.getMessage());
        }
    }

    @PostMapping("/config/preview")
    @ApiOperation("预览数据源数据")
    public JsonBean previewDataSource(@RequestBody DataSourcePreviewParam param) {
        try {
            Map<String, Object> result = dataSourceService.previewDataSource(param);
            return createSuccessJsonBean(result);
        } catch (Exception e) {
            log.error("预览数据源数据失败", e);
            return createErrorJsonBean("预览数据源数据失败: " + e.getMessage());
        }
    }

    @GetMapping("/config/{configId}")
    @ApiOperation("获取数据源配置详情")
    public JsonBean getDataSourceConfig(@PathVariable Long configId) {
        try {
            return dataSourceService.getDataSourceConfig(configId);
        } catch (Exception e) {
            log.error("获取数据源配置详情失败", e);
            return createErrorJsonBean("获取数据源配置详情失败: " + e.getMessage());
        }
    }

    @PutMapping("/config/{configId}")
    @ApiOperation("更新数据源配置")
    public JsonBean updateDataSourceConfig(@PathVariable Long configId,
                                          @RequestBody DataSourceConfigParam param) {
        try {
            dataSourceService.updateDataSourceConfig(configId, param);
            return createSuccessJsonBean("更新成功");
        } catch (Exception e) {
            log.error("更新数据源配置失败", e);
            return createErrorJsonBean("更新数据源配置失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/config/{configId}")
    @ApiOperation("删除数据源配置")
    public JsonBean deleteDataSourceConfig(@PathVariable Long configId) {
        try {
            dataSourceService.deleteDataSourceConfig(configId);
            return createSuccessJsonBean("删除成功");
        } catch (Exception e) {
            log.error("删除数据源配置失败", e);
            return createErrorJsonBean("删除数据源配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/config/batch")
    @ApiOperation("批量操作数据源配置")
    public JsonBean batchOperationDataSource(@RequestBody DataSourceBatchParam param) {
        try {
            Map<String, Object> result = dataSourceService.batchOperationDataSource(param);
            return createSuccessJsonBean(result);
        } catch (Exception e) {
            log.error("批量操作数据源配置失败", e);
            return createErrorJsonBean("批量操作数据源配置失败: " + e.getMessage());
        }
    }

    @GetMapping("/types")
    @ApiOperation("获取支持的数据源类型")
    public JsonBean getDataSourceTypes() {
        try {
            List<Map<String, Object>> types = dataSourceService.getDataSourceTypes();
            return createSuccessJsonBean(types);
        } catch (Exception e) {
            log.error("获取数据源类型失败", e);
            return createErrorJsonBean("获取数据源类型失败: " + e.getMessage());
        }
    }

    @PostMapping("/import")
    @ApiOperation("导入数据源配置")
    public JsonBean importDataSourceConfig(@RequestBody DataSourceImportParam param) {
        try {
            Map<String, Object> result = dataSourceService.importDataSourceConfig(param);
            return createSuccessJsonBean(result);
        } catch (Exception e) {
            log.error("导入数据源配置失败", e);
            return createErrorJsonBean("导入数据源配置失败: " + e.getMessage());
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出数据源配置")
    public JsonBean exportDataSourceConfig(@RequestBody DataSourceExportParam param) {
        try {
            byte[] data = dataSourceService.exportDataSourceConfig(param);
            return createSuccessJsonBean(data);
        } catch (Exception e) {
            log.error("导出数据源配置失败", e);
            return createErrorJsonBean("导出数据源配置失败: " + e.getMessage());
        }
    }
}