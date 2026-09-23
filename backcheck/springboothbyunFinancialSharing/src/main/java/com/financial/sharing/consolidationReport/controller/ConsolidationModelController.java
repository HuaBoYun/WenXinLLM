package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ConsolidationModelQueryParam;
import com.financial.sharing.consolidationReport.entity.TblConsolidationModel;
import com.financial.sharing.consolidationReport.service.ConsolidationModelService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 合并模型Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-合并模型管理")
@RestController
@RequestMapping("/consolidationReport/consolidationModel")
public class ConsolidationModelController {

    @Autowired
    private ConsolidationModelService consolidationModelService;

    /**
     * 查询合并模型列表
     */
    @ApiOperation("查询合并模型列表")
    @PostMapping("/getModelList")
    public MyJsonBean getModelList(@RequestBody ConsolidationModelQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (param.getPageNum() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNum(), param.getPageSize());
            }
            
            List<TblConsolidationModel> list = consolidationModelService.getModelList(param);
            PageInfo<TblConsolidationModel> pageInfo = new PageInfo<>(list);
            
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询合并模型
     */
    @ApiOperation("根据ID查询合并模型")
    @PostMapping("/getModelById")
    public MyJsonBean getModelById(@RequestBody TblConsolidationModel param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            TblConsolidationModel model = consolidationModelService.getModelById(param.getModelId());
            return MyJsonBean.ok("查询成功", model);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增合并模型
     */
    @ApiOperation("新增合并模型")
    @PostMapping("/saveModel")
    public MyJsonBean saveModel(@RequestBody TblConsolidationModel model) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationModelService.saveModel(model);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改合并模型
     */
    @ApiOperation("修改合并模型")
    @PostMapping("/updateModel")
    public MyJsonBean updateModel(@RequestBody TblConsolidationModel model) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationModelService.updateModel(model);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除合并模型
     */
    @ApiOperation("删除合并模型")
    @PostMapping("/deleteModel")
    public MyJsonBean deleteModel(@RequestBody TblConsolidationModel param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationModelService.deleteModel(param.getModelId());
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新模型状态
     */
    @ApiOperation("更新模型状态")
    @PostMapping("/updateModelStatus")
    public MyJsonBean updateModelStatus(@RequestBody TblConsolidationModel param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            consolidationModelService.updateModelStatus(param.getModelId(), param.getStatus());
            return MyJsonBean.ok("状态更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }
}

