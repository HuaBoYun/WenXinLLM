package com.huabo.monitor.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.service.PjbgService;
import com.huabo.monitor.util.IPageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-09-17 22:55
 * @description:
 */

@RestController
@Slf4j
@Tag(name="内控合规报告-自定义报告",description="内控合规报告-自定义报告")
@RequestMapping(value = "/nbkz/zdybg")
public class ZdybgController {


    @Resource
    PjbgService pjbgService;
    
    @Resource
    private UserProvider userProvider;

    String type="nk_zdy";

	@Value("${application.administrators:}")
	private String administrators;


    @OperationLog(
            success = "自定义评价报告-主页查询成功",
            busType = "内控合规报告",
            fail = "自定义评价报告-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "自定义报告"
    )
    @Operation(summary = "自定义评价报告-主页")
    @GetMapping(value = "/nkbg/list")

    public JsonBean listnkbg(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,

            @Parameter(name = "name", description = "报告名称") @RequestParam(value = "name", required = false)String name,
            @Parameter(name = "startDate", description = "开始日期") @RequestParam(value = "startDate", required = false)String startDate,
            @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false)String endDate,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
		Integer authorityType;
		if (JudgeRoleRight.judgeRoleRight(administrators, user.getRoleNames())) {
			authorityType = 1;
		} else {
			authorityType = 0;
		}
        ///IPage<TblReport> page = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
       // page = pjbgService.findAll(pageNumber,name,startDate,endDate, type, user.getCurrentOrg().getOrgid(),user.getStaffid(),authorityType);
        PageInfo<TblReport> page=pjbgService.findAllNewPage(pageNumber, name, startDate, endDate, type, user.getCurrentOrg().getOrgid(),user.getStaffid(), authorityType,user,null,null);
        IPageResult<TblReport> iPage=new IPageResult<TblReport>().buildIpage(page);

        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("name", name);
        mv.put("startDate", startDate);
        mv.put("endDate", endDate);
        mv.put("pageBean", iPage);
        return new JsonBean(200, "success", mv);

    }
}
