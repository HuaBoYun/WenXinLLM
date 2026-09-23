package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.FormTemplateQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormTemplate;
import com.financial.sharing.enterpriseReport.service.FormTemplateService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 表单模板Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-表单模板管理")
@RestController
@RequestMapping("/enterpriseReport/formTemplate")
public class FormTemplateController {

    @Autowired
    private FormTemplateService formTemplateService;

    /**
     * 查询表单模板列表
     */
    @ApiOperation("查询表单模板列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody FormTemplateQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblFormTemplate> list = formTemplateService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单模板详情
     */
    @ApiOperation("查询表单模板详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            
            TblFormTemplate formTemplate = formTemplateService.getDetail(templateId);
            return MyJsonBean.ok(formTemplate);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存表单模板
     */
    @ApiOperation("保存表单模板")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblFormTemplate formTemplate) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (formTemplate.getGroupId() == null || formTemplate.getGroupId().isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }
            if (formTemplate.getTemplateCode() == null || formTemplate.getTemplateCode().isEmpty()) {
                return MyJsonBean.errorData("模板编码不能为空");
            }
            if (formTemplate.getTemplateName() == null || formTemplate.getTemplateName().isEmpty()) {
                return MyJsonBean.errorData("模板名称不能为空");
            }
            
            boolean result = formTemplateService.saveFormTemplate(formTemplate);
            if (result) {
                return MyJsonBean.ok("保存成功");
            } else {
                return MyJsonBean.errorData("保存失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除表单模板
     */
    @ApiOperation("删除表单模板")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            
            boolean result = formTemplateService.deleteFormTemplate(templateId);
            if (result) {
                return MyJsonBean.ok("删除成功");
            } else {
                return MyJsonBean.errorData("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 设置默认版本
     */
    @ApiOperation("设置默认版本")
    @PostMapping("/setDefaultVersion")
    public MyJsonBean setDefaultVersion(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }

            boolean result = formTemplateService.setDefaultVersion(templateId);
            if (result) {
                return MyJsonBean.ok("设置成功");
            } else {
                return MyJsonBean.errorData("设置失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("设置失败: " + e.getMessage());
        }
    }

    /**
     * 复制表单模板
     */
    @ApiOperation("复制表单模板")
    @PostMapping("/copy")
    public MyJsonBean copy(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String templateId = params.get("templateId");
            String newTemplateName = params.get("newTemplateName");
            String newVersionNo = params.get("newVersionNo");

            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            if (newTemplateName == null || newTemplateName.isEmpty()) {
                return MyJsonBean.errorData("新模板名称不能为空");
            }
            if (newVersionNo == null || newVersionNo.isEmpty()) {
                return MyJsonBean.errorData("新版本号不能为空");
            }

            boolean result = formTemplateService.copyTemplate(templateId, newTemplateName, newVersionNo);
            if (result) {
                return MyJsonBean.ok("复制成功");
            } else {
                return MyJsonBean.errorData("复制失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    /**
     * 根据表单组ID查询表单模板列表
     */
    @ApiOperation("根据表单组ID查询表单模板列表")
    @PostMapping("/getListByGroupId")
    public MyJsonBean getListByGroupId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String groupId = params.get("groupId");
            if (groupId == null || groupId.isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }

            List<TblFormTemplate> list = formTemplateService.getListByGroupId(groupId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}


