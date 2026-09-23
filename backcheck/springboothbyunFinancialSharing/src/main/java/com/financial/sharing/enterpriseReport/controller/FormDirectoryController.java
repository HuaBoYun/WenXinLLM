package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.enterpriseReport.dto.FormDirectoryQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormDirectory;
import com.financial.sharing.enterpriseReport.service.FormDirectoryService;
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
 * 表单目录Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-表单目录管理")
@RestController
@RequestMapping("/enterpriseReport/formDirectory")
public class FormDirectoryController {

    @Autowired
    private FormDirectoryService formDirectoryService;

    /**
     * 查询表单目录树形结构
     */
    @ApiOperation("查询表单目录树形结构")
    @PostMapping("/getTree")
    public MyJsonBean getTree(@RequestBody FormDirectoryQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblFormDirectory> tree = formDirectoryService.getDirectoryTree(param);
            return MyJsonBean.ok(tree);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单目录列表
     */
    @ApiOperation("查询表单目录列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody FormDirectoryQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblFormDirectory> list = formDirectoryService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单目录详情
     */
    @ApiOperation("查询表单目录详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String directoryId = params.get("directoryId");
            if (directoryId == null || directoryId.isEmpty()) {
                return MyJsonBean.errorData("目录ID不能为空");
            }
            
            TblFormDirectory directory = formDirectoryService.getDetail(directoryId);
            return MyJsonBean.ok(directory);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存表单目录
     */
    @ApiOperation("保存表单目录")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblFormDirectory directory) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (directory.getDirectoryCode() == null || directory.getDirectoryCode().isEmpty()) {
                return MyJsonBean.errorData("目录编码不能为空");
            }
            if (directory.getDirectoryName() == null || directory.getDirectoryName().isEmpty()) {
                return MyJsonBean.errorData("目录名称不能为空");
            }
            
            boolean result = formDirectoryService.saveDirectory(directory);
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
     * 删除表单目录
     */
    @ApiOperation("删除表单目录")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String directoryId = params.get("directoryId");
            if (directoryId == null || directoryId.isEmpty()) {
                return MyJsonBean.errorData("目录ID不能为空");
            }
            
            boolean result = formDirectoryService.deleteDirectory(directoryId);
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

