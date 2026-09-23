package com.financial.sharing.enterpriseReport.controller;

import com.financial.sharing.enterpriseReport.dto.FormGroupQueryParam;
import com.financial.sharing.enterpriseReport.entity.TblFormGroup;
import com.financial.sharing.enterpriseReport.service.FormGroupService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
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
 * 表单组Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "企业报表-表单组管理")
@RestController
@RequestMapping("/enterpriseReport/formGroup")
public class FormGroupController {

    @Autowired
    private FormGroupService formGroupService;

    /**
     * 查询表单组列表
     */
    @ApiOperation("查询表单组列表")
    @PostMapping("/getList")
    public MyJsonBean getList(@RequestBody FormGroupQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblFormGroup> list = formGroupService.getList(param);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询表单组详情
     */
    @ApiOperation("查询表单组详情")
    @PostMapping("/detail")
    public MyJsonBean detail(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String groupId = params.get("groupId");
            if (groupId == null || groupId.isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }
            
            TblFormGroup formGroup = formGroupService.getDetail(groupId);
            return MyJsonBean.ok(formGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存表单组
     */
    @ApiOperation("保存表单组")
    @PostMapping("/save")
    public MyJsonBean save(@RequestBody TblFormGroup formGroup) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            // 参数校验
            if (formGroup.getDirectoryId() == null || formGroup.getDirectoryId().isEmpty()) {
                return MyJsonBean.errorData("目录ID不能为空");
            }
            if (formGroup.getGroupCode() == null || formGroup.getGroupCode().isEmpty()) {
                return MyJsonBean.errorData("表单组编码不能为空");
            }
            if (formGroup.getGroupName() == null || formGroup.getGroupName().isEmpty()) {
                return MyJsonBean.errorData("表单组名称不能为空");
            }
            
            boolean result = formGroupService.saveFormGroup(formGroup);
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
     * 删除表单组
     */
    @ApiOperation("删除表单组")
    @PostMapping("/delete")
    public MyJsonBean delete(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String groupId = params.get("groupId");
            if (groupId == null || groupId.isEmpty()) {
                return MyJsonBean.errorData("表单组ID不能为空");
            }
            
            boolean result = formGroupService.deleteFormGroup(groupId);
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
     * 根据目录ID查询表单组列表
     */
    @ApiOperation("根据目录ID查询表单组列表")
    @PostMapping("/getListByDirectoryId")
    public MyJsonBean getListByDirectoryId(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String directoryId = params.get("directoryId");
            if (directoryId == null || directoryId.isEmpty()) {
                return MyJsonBean.errorData("目录ID不能为空");
            }
            
            List<TblFormGroup> list = formGroupService.getListByDirectoryId(directoryId);
            return MyJsonBean.ok(list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

