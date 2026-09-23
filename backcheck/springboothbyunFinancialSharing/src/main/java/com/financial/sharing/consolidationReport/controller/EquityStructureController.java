package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.entity.TblEquityStructure;
import com.financial.sharing.consolidationReport.service.EquityStructureService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 股权结构Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-股权结构计算")
@RestController
@RequestMapping("/consolidationReport/equityStructure")
public class EquityStructureController {

    @Autowired
    private EquityStructureService equityStructureService;

    /**
     * 计算股权结构
     */
    @ApiOperation("计算股权结构")
    @PostMapping("/calculateEquityStructure")
    public MyJsonBean calculateEquityStructure(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            
            equityStructureService.calculateEquityStructure(modelId);
            return MyJsonBean.ok("计算成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("计算失败: " + e.getMessage());
        }
    }

    /**
     * 查询股权结构列表
     */
    @ApiOperation("查询股权结构列表")
    @PostMapping("/getEquityStructureList")
    public MyJsonBean getEquityStructureList(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            
            List<TblEquityStructure> list = equityStructureService.getEquityStructureList(modelId);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询股权结构树
     */
    @ApiOperation("查询股权结构树")
    @PostMapping("/getEquityStructureTree")
    public MyJsonBean getEquityStructureTree(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            
            List<TblEquityStructure> tree = equityStructureService.getEquityStructureTree(modelId);
            return MyJsonBean.ok("查询成功", tree);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除股权结构
     */
    @ApiOperation("删除股权结构")
    @PostMapping("/deleteEquityStructure")
    public MyJsonBean deleteEquityStructure(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            
            equityStructureService.deleteEquityStructure(modelId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }
}

