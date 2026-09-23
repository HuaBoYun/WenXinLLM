package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EliminationTemplateQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEliminationTemplate;
import com.financial.sharing.consolidationReport.service.EliminationTemplateService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 抵消凭证模板Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-抵消凭证模板")
@RestController
@RequestMapping("/consolidationReport/eliminationTemplate")
public class EliminationTemplateController {

    @Autowired
    private EliminationTemplateService eliminationTemplateService;

    /**
     * 查询抵消凭证模板列表
     */
    @ApiOperation("查询抵消凭证模板列表")
    @PostMapping("/getTemplateList")
    public MyJsonBean getTemplateList(@RequestBody EliminationTemplateQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            PageInfo<TblEliminationTemplate> pageInfo = eliminationTemplateService.getTemplateList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询抵消凭证模板
     */
    @ApiOperation("根据ID查询抵消凭证模板")
    @PostMapping("/getTemplateById")
    public MyJsonBean getTemplateById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            
            TblEliminationTemplate template = eliminationTemplateService.getTemplateById(templateId);
            return MyJsonBean.ok("查询成功", template);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增抵消凭证模板
     */
    @ApiOperation("新增抵消凭证模板")
    @PostMapping("/saveTemplate")
    public MyJsonBean saveTemplate(@RequestBody TblEliminationTemplate template) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            eliminationTemplateService.saveTemplate(template);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改抵消凭证模板
     */
    @ApiOperation("修改抵消凭证模板")
    @PostMapping("/updateTemplate")
    public MyJsonBean updateTemplate(@RequestBody TblEliminationTemplate template) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            eliminationTemplateService.updateTemplate(template);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除抵消凭证模板
     */
    @ApiOperation("删除抵消凭证模板")
    @PostMapping("/deleteTemplate")
    public MyJsonBean deleteTemplate(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            
            eliminationTemplateService.deleteTemplate(templateId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新模板状态
     */
    @ApiOperation("更新模板状态")
    @PostMapping("/updateTemplateStatus")
    public MyJsonBean updateTemplateStatus(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            String isActive = params.get("isActive");
            
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            if (isActive == null || isActive.isEmpty()) {
                return MyJsonBean.errorData("状态不能为空");
            }
            
            eliminationTemplateService.updateTemplateStatus(templateId, isActive);
            return MyJsonBean.ok("更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("更新失败: " + e.getMessage());
        }
    }
}

