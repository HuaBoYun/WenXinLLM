package com.global.treasurer.controller;

import com.global.treasurer.entity.TblFundPlanExecution;
import com.global.treasurer.mapper.FundPlanExecutionMapper;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资金计划执行控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@RestController
@RequestMapping("/fund/plan-execution")
@Api(tags = "资金计划执行管理")
public class FundPlanExecutionController {
    @Resource
    private FundPlanExecutionMapper fundPlanExecutionMapper;

    @Resource
    private UserProvider userProvider;

    /**
     * 分页查询资金计划执行
     */
    @GetMapping("/page")
    @ApiOperation("分页查询资金计划执行")
    public String getFundPlanExecutionPage(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Integer page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            Integer limit = params.get("limit") != null ? Integer.parseInt(params.get("limit").toString()) : 20;

            params.put("offset", (page - 1) * limit);
            params.put("limit", limit);

            List<TblFundPlanExecution> list = fundPlanExecutionMapper.selectExecutionPage(params);
            int total = fundPlanExecutionMapper.countExecutionList(params);

            Map<String, Object> result = new HashMap<>();
            result.put("records", list);
            result.put("total", total);
            result.put("current", page);
            result.put("size", limit);

            return JsonBean.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建资金计划执行
     */
    @PostMapping("")
    @ApiOperation("创建资金计划执行")
    public String createFundPlanExecution(@FlexibleRequestBody TblFundPlanExecution execution) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanExecutionMapper.insert(execution);
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

    /**
     * 更新资金计划执行
     */
    @PutMapping("")
    @ApiOperation("更新资金计划执行")
    public String updateFundPlanExecution(@FlexibleRequestBody TblFundPlanExecution execution) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanExecutionMapper.updateById(execution);
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

    /**
     * 删除资金计划执行
     */
    @DeleteMapping("/{executionId}")
    @ApiOperation("删除资金计划执行")
    public String deleteFundPlanExecution(@PathVariable Long executionId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            int result = fundPlanExecutionMapper.deleteById(executionId);
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

    /**
     * 获取执行汇总信息
     */
    @GetMapping("/summary")
    @ApiOperation("获取执行汇总信息")
    public String getFundPlanExecutionSummary(@RequestParam Map<String, Object> params) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            Map<String, Object> summary = fundPlanExecutionMapper.selectExecutionSummary(params);
            return JsonBean.success(summary);
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 开始资金计划执行
     */
    @PutMapping("/{executionId}/start")
    @ApiOperation("开始资金计划执行")
    public String startFundPlanExecution(@PathVariable Long executionId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanExecution execution = fundPlanExecutionMapper.selectById(executionId);
            if (execution != null) {
                execution.setExecutionStatus("IN_PROGRESS");
                fundPlanExecutionMapper.updateById(execution);
                return JsonBean.success("开始执行成功");
            } else {
                return new JsonBean(0, "执行记录不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 暂停资金计划执行
     */
    @PutMapping("/{executionId}/pause")
    @ApiOperation("暂停资金计划执行")
    public String pauseFundPlanExecution(@PathVariable Long executionId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanExecution execution = fundPlanExecutionMapper.selectById(executionId);
            if (execution != null) {
                execution.setExecutionStatus("PAUSED");
                fundPlanExecutionMapper.updateById(execution);
                return JsonBean.success("暂停成功");
            } else {
                return new JsonBean(0, "执行记录不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 完成资金计划执行
     */
    @PutMapping("/{executionId}/complete")
    @ApiOperation("完成资金计划执行")
    public String completeFundPlanExecution(@PathVariable Long executionId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanExecution execution = fundPlanExecutionMapper.selectById(executionId);
            if (execution != null) {
                execution.setExecutionStatus("COMPLETED");
                fundPlanExecutionMapper.updateById(execution);
                return JsonBean.success("完成成功");
            } else {
                return new JsonBean(0, "执行记录不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 取消资金计划执行
     */
    @PutMapping("/{executionId}/cancel")
    @ApiOperation("取消资金计划执行")
    public String cancelFundPlanExecution(@PathVariable Long executionId) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null) {
                return new JsonBean(401, "用户已失效", null).toJson();
            }

            TblFundPlanExecution execution = fundPlanExecutionMapper.selectById(executionId);
            if (execution != null) {
                execution.setExecutionStatus("CANCELLED");
                fundPlanExecutionMapper.updateById(execution);
                return JsonBean.success("取消成功");
            } else {
                return new JsonBean(0, "执行记录不存在", null).toJson();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new JsonBean(0, "操作失败: " + e.getMessage(), null).toJson();
        }
    }
}
