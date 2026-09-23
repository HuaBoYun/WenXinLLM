package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ConsolidationScopeQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationScope;
import com.financial.sharing.consolidationReport.service.ConsolidationScopeService;
import com.github.pagehelper.PageHelper;
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
 * 合并范围配置Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-合并范围配置")
@RestController
@RequestMapping("/consolidationReport/consolidationScope")
public class ConsolidationScopeController {

    @Autowired
    private ConsolidationScopeService consolidationScopeService;

    /**
     * 查询合并范围配置列表
     */
    @ApiOperation("查询合并范围配置列表")
    @PostMapping("/getScopeList")
    public MyJsonBean getScopeList(@RequestBody ConsolidationScopeQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (param.getPageNum() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNum(), param.getPageSize());
            }
            
            List<TblConsolidationScope> list = consolidationScopeService.getScopeList(param);
            PageInfo<TblConsolidationScope> pageInfo = new PageInfo<>(list);
            
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询合并范围配置
     */
    @ApiOperation("根据ID查询合并范围配置")
    @PostMapping("/getScopeById")
    public MyJsonBean getScopeById(@RequestBody TblConsolidationScope param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            TblConsolidationScope scope = consolidationScopeService.getScopeById(param.getScopeId());
            return MyJsonBean.ok("查询成功", scope);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增合并范围配置
     */
    @ApiOperation("新增合并范围配置")
    @PostMapping("/saveScope")
    public MyJsonBean saveScope(@RequestBody TblConsolidationScope scope) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationScopeService.saveScope(scope);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改合并范围配置
     */
    @ApiOperation("修改合并范围配置")
    @PostMapping("/updateScope")
    public MyJsonBean updateScope(@RequestBody TblConsolidationScope scope) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationScopeService.updateScope(scope);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除合并范围配置
     */
    @ApiOperation("删除合并范围配置")
    @PostMapping("/deleteScope")
    public MyJsonBean deleteScope(@RequestBody TblConsolidationScope param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationScopeService.deleteScope(param.getScopeId());
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量保存合并范围配置
     */
    @ApiOperation("批量保存合并范围配置")
    @PostMapping("/batchSaveScope")
    public MyJsonBean batchSaveScope(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = (String) params.get("modelId");
            @SuppressWarnings("unchecked")
            List<TblConsolidationScope> scopeList = (List<TblConsolidationScope>) params.get("scopeList");
            
            consolidationScopeService.batchSaveScope(modelId, scopeList);
            return MyJsonBean.ok("保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 更新范围配置状态
     */
    @ApiOperation("更新范围配置状态")
    @PostMapping("/updateScopeStatus")
    public MyJsonBean updateScopeStatus(@RequestBody TblConsolidationScope param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationScopeService.updateScopeStatus(param.getScopeId(), param.getIsActive());
            return MyJsonBean.ok("状态更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }
}

