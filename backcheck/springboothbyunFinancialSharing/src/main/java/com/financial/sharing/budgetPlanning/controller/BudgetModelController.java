package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetModelQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetModel;
import com.financial.sharing.budgetPlanning.service.BudgetModelService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 预算模型Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "计划预算-预算模型管理")
@RestController
@RequestMapping("/budgetPlanning/budgetModel")
public class BudgetModelController {

    @Autowired
    private BudgetModelService modelService;

    @ApiOperation("查询预算模型列表(分页)")
    @PostMapping("/getModelList")
    public MyJsonBean getModelList(@RequestBody BudgetModelQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetModel> pageInfo = modelService.getModelList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询预算模型列表(不分页)")
    @PostMapping("/getModelListNoPage")
    public MyJsonBean getModelListNoPage(@RequestBody BudgetModelQueryParam param) {
        // 未登录时返回空列表，避免前端下拉选择器渲染异常
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", java.util.Collections.emptyList());
        }
        try {
            List<TblBudgetModel> list = modelService.getModelListNoPage(param);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询预算模型")
    @PostMapping("/getModelById")
    public MyJsonBean getModelById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            TblBudgetModel model = modelService.getModelById(modelId);
            return MyJsonBean.ok("查询成功", model);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增预算模型")
    @PostMapping("/addModel")
    public MyJsonBean addModel(@RequestBody TblBudgetModel model) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            modelService.addModel(model);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改预算模型")
    @PostMapping("/updateModel")
    public MyJsonBean updateModel(@RequestBody TblBudgetModel model) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            modelService.updateModel(model);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预算模型")
    @PostMapping("/deleteModel")
    public MyJsonBean deleteModel(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            modelService.deleteModel(modelId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除预算模型")
    @PostMapping("/batchDeleteModel")
    public MyJsonBean batchDeleteModel(@RequestBody Map<String, List<String>> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<String> modelIds = params.get("modelIds");
            if (modelIds == null || modelIds.isEmpty()) {
                return MyJsonBean.errorData("模型ID列表不能为空");
            }
            modelService.batchDeleteModel(modelIds);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制预算模型")
    @PostMapping("/copyModel")
    public MyJsonBean copyModel(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            String newModelCode = params.get("newModelCode");
            String newModelName = params.get("newModelName");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("源模型ID不能为空");
            }
            if (newModelCode == null || newModelCode.isEmpty()) {
                return MyJsonBean.errorData("新模型编码不能为空");
            }
            if (newModelName == null || newModelName.isEmpty()) {
                return MyJsonBean.errorData("新模型名称不能为空");
            }
            
            String newModelId = modelService.copyModel(modelId, newModelCode, newModelName);
            return MyJsonBean.ok("复制成功", newModelId);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用预算模型")
    @PostMapping("/enableModel")
    public MyJsonBean enableModel(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            modelService.enableModel(modelId);
            return MyJsonBean.ok("启用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("启用失败: " + e.getMessage());
        }
    }

    @ApiOperation("停用预算模型")
    @PostMapping("/disableModel")
    public MyJsonBean disableModel(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            modelService.disableModel(modelId);
            return MyJsonBean.ok("停用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("停用失败: " + e.getMessage());
        }
    }
}

