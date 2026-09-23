package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.FormFormulaQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormFormula;
import com.financial.sharing.enterpriseReport.service.FormFormulaService;
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
 * 表单公式Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-表单公式管理")
@RestController
@RequestMapping("/enterpriseReport/formFormula")
public class FormFormulaController {

    @Autowired
    private FormFormulaService formFormulaService;

    /**
     * 查询表单公式列表
     */
    @ApiOperation("查询表单公式列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody FormFormulaQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblFormFormula> list = formFormulaService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单公式详情
     */
    @ApiOperation("查询表单公式详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String formulaId = params.get("formulaId");
            if (formulaId == null || formulaId.isEmpty()) {
                return MyJsonBean.errorData("公式ID不能为空");
            }
            
            TblFormFormula formFormula = formFormulaService.getDetail(formulaId);
            return MyJsonBean.ok(formFormula);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存表单公式
     */
    @ApiOperation("保存表单公式")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblFormFormula formFormula) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (formFormula.getTemplateId() == null || formFormula.getTemplateId().isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }
            if (formFormula.getFormulaCode() == null || formFormula.getFormulaCode().isEmpty()) {
                return MyJsonBean.errorData("公式编码不能为空");
            }
            if (formFormula.getFormulaName() == null || formFormula.getFormulaName().isEmpty()) {
                return MyJsonBean.errorData("公式名称不能为空");
            }
            if (formFormula.getFormulaType() == null || formFormula.getFormulaType().isEmpty()) {
                return MyJsonBean.errorData("公式类型不能为空");
            }
            if (formFormula.getFormulaExpression() == null || formFormula.getFormulaExpression().isEmpty()) {
                return MyJsonBean.errorData("公式表达式不能为空");
            }
            
            boolean result = formFormulaService.saveFormFormula(formFormula);
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
     * 删除表单公式
     */
    @ApiOperation("删除表单公式")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String formulaId = params.get("formulaId");
            if (formulaId == null || formulaId.isEmpty()) {
                return MyJsonBean.errorData("公式ID不能为空");
            }
            
            boolean result = formFormulaService.deleteFormFormula(formulaId);
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
     * 根据模板ID查询表单公式列表
     */
    @ApiOperation("根据模板ID查询表单公式列表")
    @PostMapping("/getListByTemplateId")
    public MyJsonBean getListByTemplateId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String templateId = params.get("templateId");
            if (templateId == null || templateId.isEmpty()) {
                return MyJsonBean.errorData("模板ID不能为空");
            }

            List<TblFormFormula> list = formFormulaService.getListByTemplateId(templateId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除表单公式
     */
    @ApiOperation("批量删除表单公式")
    @PostMapping("/batchDelete")
    public MyJsonBean batchDelete(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            @SuppressWarnings("unchecked")
            List<String> formulaIds = (List<String>) params.get("formulaIds");
            if (formulaIds == null || formulaIds.isEmpty()) {
                return MyJsonBean.errorData("公式ID列表不能为空");
            }

            boolean result = formFormulaService.batchDelete(formulaIds);
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
}
