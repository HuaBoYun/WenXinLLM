package com.huabo.financialdata.controller;

import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.financialdata.config.mvn.ApiResponse;
import com.huabo.financialdata.entity.dto.AccSumListPageQuery;
import com.huabo.financialdata.entity.vo.AccSumListPageInfoVO;
import com.huabo.financialdata.entity.vo.accBook.AccBookVO;
import com.huabo.financialdata.service.IAccBookService;
import com.huabo.financialdata.service.IAccSumService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 科目余额  - 接口
 *
 * @author lee
 * @version 1.0.0
 **/
@RestController
@RequestMapping("/accSum")
@Tag(name="科目余额",description="科目余额")
public class AccSumController {

    @Resource
    private IAccBookService accBookService;
    @Resource
    private IAccSumService accSumService;
    
    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @Operation(summary="科目余额 - 分页查询",description="科目余额 - 分页查询")
    public ApiResponse<PageInfo<AccSumListPageInfoVO>> getListByPage(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody AccSumListPageQuery query) {
        //LoginUserInfoDO userInfoDO = LoginTokenUtil.tokenAnalysis(token);
        //todo 测试代码
        try {
        	//查询当前财务账套有效的最大月份
            TblStaffUtil staff = userProvider.get();
            if(staff == null) {
            	return ApiResponse.fail("用户已失效");
            }
            AccBookVO accBookVO = accBookService.getSelectedBookByStaffId(staff.getStaffid(), staff.getCurrentOrg().getOrgid());
            if (Objects.isNull(accBookVO)) {
                return ApiResponse.fail("当前登录用户未选中财务账套");
            }
            return ApiResponse.success(accSumService.getListByPage(accBookVO, query));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return ApiResponse.fail("查询失败");
    }

}
