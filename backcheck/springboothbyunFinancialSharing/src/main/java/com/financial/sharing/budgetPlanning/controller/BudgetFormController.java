package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetFormQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetForm;
import com.financial.sharing.budgetPlanning.service.BudgetFormService;
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
 * 预算表单配置Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "计划预算-预算表单配置")
@RestController
@RequestMapping("/budgetPlanning/budgetForm")
public class BudgetFormController {

    @Autowired
    private BudgetFormService formService;

    @ApiOperation("查询预算表单配置列表(分页)")
    @PostMapping("/getFormList")
    public MyJsonBean getFormList(@RequestBody BudgetFormQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            PageInfo<TblBudgetForm> pageInfo = formService.getFormList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询预算表单配置列表(不分页)")
    @PostMapping("/getFormListNoPage")
    public MyJsonBean getFormListNoPage(@RequestBody BudgetFormQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<TblBudgetForm> list = formService.getFormListNoPage(param);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询预算表单配置")
    @PostMapping("/getFormById")
    public MyJsonBean getFormById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String formId = params.get("formId");
            if (formId == null || formId.isEmpty()) {
                return MyJsonBean.errorData("表单ID不能为空");
            }
            TblBudgetForm form = formService.getFormById(formId);
            return MyJsonBean.ok("查询成功", form);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增预算表单配置")
    @PostMapping("/addForm")
    public MyJsonBean addForm(@RequestBody TblBudgetForm form) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            formService.addForm(form);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改预算表单配置")
    @PostMapping("/updateForm")
    public MyJsonBean updateForm(@RequestBody TblBudgetForm form) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            formService.updateForm(form);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预算表单配置")
    @PostMapping("/deleteForm")
    public MyJsonBean deleteForm(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String formId = params.get("formId");
            if (formId == null || formId.isEmpty()) {
                return MyJsonBean.errorData("表单ID不能为空");
            }
            formService.deleteForm(formId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除预算表单配置")
    @PostMapping("/batchDeleteForm")
    public MyJsonBean batchDeleteForm(@RequestBody Map<String, List<String>> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<String> formIds = params.get("formIds");
            if (formIds == null || formIds.isEmpty()) {
                return MyJsonBean.errorData("表单ID列表不能为空");
            }
            formService.batchDeleteForm(formIds);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制预算表单配置")
    @PostMapping("/copyForm")
    public MyJsonBean copyForm(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String formId = params.get("formId");
            String newFormCode = params.get("newFormCode");
            String newFormName = params.get("newFormName");
            
            if (formId == null || formId.isEmpty()) {
                return MyJsonBean.errorData("源表单ID不能为空");
            }
            if (newFormCode == null || newFormCode.isEmpty()) {
                return MyJsonBean.errorData("新表单编码不能为空");
            }
            if (newFormName == null || newFormName.isEmpty()) {
                return MyJsonBean.errorData("新表单名称不能为空");
            }
            
            String newFormId = formService.copyForm(formId, newFormCode, newFormName);
            return MyJsonBean.ok("复制成功", newFormId);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用预算表单配置")
    @PostMapping("/enableForm")
    public MyJsonBean enableForm(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String formId = params.get("formId");
            if (formId == null || formId.isEmpty()) {
                return MyJsonBean.errorData("表单ID不能为空");
            }
            formService.enableForm(formId);
            return MyJsonBean.ok("启用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("启用失败: " + e.getMessage());
        }
    }

    @ApiOperation("停用预算表单配置")
    @PostMapping("/disableForm")
    public MyJsonBean disableForm(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String formId = params.get("formId");
            if (formId == null || formId.isEmpty()) {
                return MyJsonBean.errorData("表单ID不能为空");
            }
            formService.disableForm(formId);
            return MyJsonBean.ok("停用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("停用失败: " + e.getMessage());
        }
    }
}

