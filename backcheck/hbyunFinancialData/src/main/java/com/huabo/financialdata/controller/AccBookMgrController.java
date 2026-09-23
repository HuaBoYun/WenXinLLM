package com.huabo.financialdata.controller;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.dto.accBook.AccountBookListQuery;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.service.IAccBookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 账簿管理 - 接口
 *
 * @author lee
 * @version 1.0.0
 **/
@RestController
@RequestMapping("/accBookMgr")
@Tag(name="账簿管理",description="账簿管理")
public class AccBookMgrController {

    @Resource
    private IAccBookService accBookService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @Operation(summary="账簿管理 - 分页查询",description="账簿管理 - 分页查询")
    public ApiResponse<PageInfo<AccBookVO>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Validated @RequestBody AccountBookListQuery query) {

        try {
        	//查询当前财务账套有效的最大月份
            TblStaffUtil staff = userProvider.get();
            
            if(staff == null) {
            	return ApiResponse.fail("用户已失效");
            }
            //请求参数设置
            query.setStaffId(staff.getStaffid());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.success(accBookService.getListByPage(query));
    }


    @PostMapping("/checkBook")
    @Operation(summary="账簿管理 - 选择年份账套",description="账簿管理 - 选择年份账套")
    public ApiResponse<Boolean> checkBook(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "bookId", description = "账套ID") @RequestParam(value = "bookId") String bookId) throws Exception {

        //1. 用户token
    	//查询当前财务账套有效的最大月份
        TblStaffUtil staff = userProvider.get();
        
        if(staff == null) {
        	return ApiResponse.fail("用户已失效");
        }
        return ApiResponse.success(accBookService.chooseBook(staff.getStaffid(), staff.getCurrentOrg().getOrgid(), bookId));
    }

    @PostMapping("/getSelectedBookInfo")
    @Operation(summary="账簿管理 - 获取当前登录用户选中的账套数据",description="账簿管理 - 获取当前登录用户选中的账套数据")
    public ApiResponse<AccBookVO> getSelectedBookByStaffId(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        //LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(token);

        try {
        	//查询当前财务账套有效的最大月份
            TblStaffUtil staff = userProvider.get();
            
            if(staff == null) {
            	return ApiResponse.fail("用户已失效");
            }
            //请求参数设置
            return ApiResponse.success(accBookService.getSelectedBookByStaffId(staff.getStaffid(), staff.getCurrentOrg().getOrgid()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ApiResponse.fail("查询失败");
    }

}
