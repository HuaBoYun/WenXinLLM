package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.EquityInfoQueryParam;
import com.financial.sharing.consolidationReport.entity.TblEquityInfo;
import com.financial.sharing.consolidationReport.service.EquityInfoService;
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
 * 股权信息Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-股权信息管理")
@RestController
@RequestMapping("/consolidationReport/equityInfo")
public class EquityInfoController {

    @Autowired
    private EquityInfoService equityInfoService;

    /**
     * 查询股权信息列表
     */
    @ApiOperation("查询股权信息列表")
    @PostMapping("/getEquityList")
    public MyJsonBean getEquityList(@RequestBody EquityInfoQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            if (param.getPageNum() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNum(), param.getPageSize());
            }
            
            List<TblEquityInfo> list = equityInfoService.getEquityList(param);
            PageInfo<TblEquityInfo> pageInfo = new PageInfo<>(list);
            
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询股权信息
     */
    @ApiOperation("根据ID查询股权信息")
    @PostMapping("/getEquityById")
    public MyJsonBean getEquityById(@RequestBody TblEquityInfo param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            TblEquityInfo equity = equityInfoService.getEquityById(param.getEquityId());
            return MyJsonBean.ok("查询成功", equity);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 新增股权信息
     */
    @ApiOperation("新增股权信息")
    @PostMapping("/saveEquity")
    public MyJsonBean saveEquity(@RequestBody TblEquityInfo equity) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            equityInfoService.saveEquity(equity);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    /**
     * 修改股权信息
     */
    @ApiOperation("修改股权信息")
    @PostMapping("/updateEquity")
    public MyJsonBean updateEquity(@RequestBody TblEquityInfo equity) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            equityInfoService.updateEquity(equity);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    /**
     * 删除股权信息
     */
    @ApiOperation("删除股权信息")
    @PostMapping("/deleteEquity")
    public MyJsonBean deleteEquity(@RequestBody TblEquityInfo param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            equityInfoService.deleteEquity(param.getEquityId());
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新股权信息状态
     */
    @ApiOperation("更新股权信息状态")
    @PostMapping("/updateEquityStatus")
    public MyJsonBean updateEquityStatus(@RequestBody TblEquityInfo param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            equityInfoService.updateEquityStatus(param.getEquityId(), param.getIsActive());
            return MyJsonBean.ok("状态更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    /**
     * 根据模型ID查询股权信息列表
     */
    @ApiOperation("根据模型ID查询股权信息列表")
    @PostMapping("/getEquityListByModelId")
    public MyJsonBean getEquityListByModelId(@RequestBody TblEquityInfo param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblEquityInfo> list = equityInfoService.getEquityListByModelId(param.getModelId());
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

