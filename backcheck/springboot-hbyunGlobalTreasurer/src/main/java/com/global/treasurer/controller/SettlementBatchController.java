package com.global.treasurer.controller;

import com.global.treasurer.entity.TblSettlementBatch;
import com.global.treasurer.service.SettlementBatchService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/settlement/batch")
@Api(tags = "结算批次管理")
public class SettlementBatchController {
    @Resource
    private SettlementBatchService batchService;
    @Resource
    private UserProvider userProvider;

    @GetMapping("/page")
    @ApiOperation("分页查询结算批次")
    public String getBatchPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> result = batchService.getBatchPage(params);
            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{id}")
    @ApiOperation("根据ID查询结算批次")
    public String getBatchById(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            TblSettlementBatch batch = batchService.getBatchById(id);
            if (batch == null) {
                return new JsonBean(0, "批次不存在", null).toJson();
            }
            return JsonBean.success(batch);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ApiOperation("新增结算批次")
    public String addBatch(@FlexibleRequestBody TblSettlementBatch batch) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = batchService.createBatch(batch);
            if (result > 0) {
                return JsonBean.success("创建成功");
            } else {
                return new JsonBean(0, "创建失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ApiOperation("修改结算批次")
    public String updateBatch(@FlexibleRequestBody TblSettlementBatch batch) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = batchService.updateBatch(batch);
            if (result > 0) {
                return JsonBean.success("更新成功");
            } else {
                return new JsonBean(0, "更新失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{ids}")
    @ApiOperation("删除结算批次")
    public String deleteBatch(@PathVariable String ids) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            String[] idArray = ids.split(",");
            List<Long> idList = new java.util.ArrayList<>();
            for (String id : idArray) {
                idList.add(Long.parseLong(id));
            }
            int result = batchService.deleteBatch(idList);
            if (result > 0) {
                return JsonBean.success("删除成功");
            } else {
                return new JsonBean(0, "删除失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{id}/start")
    @ApiOperation("启动批次")
    public String startBatch(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = batchService.startBatch(id);
            if (result > 0) {
                return JsonBean.success("启动成功");
            } else {
                return new JsonBean(0, "启动失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "启动失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{id}/stop")
    @ApiOperation("停止批次")
    public String stopBatch(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = batchService.stopBatch(id);
            if (result > 0) {
                return JsonBean.success("停止成功");
            } else {
                return new JsonBean(0, "停止失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "停止失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{id}/retry")
    @ApiOperation("重试批次")
    public String retryBatch(@PathVariable Long id) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            int result = batchService.retryBatch(id);
            if (result > 0) {
                return JsonBean.success("重试成功");
            } else {
                return new JsonBean(0, "重试失败", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "重试失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/executable")
    @ApiOperation("查询可执行批次")
    public String getExecutableBatches(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            List<TblSettlementBatch> list = batchService.getExecutableBatches(orgId);
            return JsonBean.success(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/summary")
    @ApiOperation("统计批次概要")
    public String getBatchSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> summary = batchService.getBatchSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/performance-analysis")
    @ApiOperation("批次性能分析")
    public String analyzeBatchPerformance(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> analysis = batchService.analyzeBatchPerformance(params);
            return JsonBean.success(analysis);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "分析失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/optimization-suggestions")
    @ApiOperation("优化建议")
    public String getBatchOptimizationSuggestions(@RequestParam Long orgId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }
            Map<String, Object> suggestions = batchService.getBatchOptimizationSuggestions(orgId);
            return JsonBean.success(suggestions);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}
