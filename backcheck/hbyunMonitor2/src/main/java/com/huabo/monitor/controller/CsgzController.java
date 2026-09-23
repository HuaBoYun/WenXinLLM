package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.service.CsfaService;
import com.huabo.monitor.service.CsgzService;
import com.huabo.monitor.service.ITblStaffService;
import com.huabo.monitor.service.ITblTestplanService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblTestElementService;
import com.huabo.monitor.util.IPageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * @author：yhr
 * @date:2022-09-13 11:33
 * @description:
 */
@RestController
@Slf4j
@Tag(name="内控测试-测试跟踪",description="内控测试-测试跟踪")
@RequestMapping(value = "/nbkz")
public class CsgzController {

    @Resource
    ITblTestplanService testplanService;
    @Autowired
    ITblStaffService iTblStaffService;


    @Autowired
    TblAssessService tblAssessService;

    @Resource
    CsfaService csfaService;

    @Resource
    TblTestElementService tblTestelementService;


    @Resource
    CsgzService csgzService;
    
    @Resource
    private UserProvider userProvider;



    @OperationLog(
            success = "测试跟踪-主页查询成功",
            busType = "内控测试",
            fail = "测试跟踪-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "测试跟踪"
    )
    @GetMapping(value = "/nkcs/impl/control_test_track_list")
    @Operation(summary = "测试跟踪-主页")
    public JsonBean control_test_track_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "plannumber", description = "plannumber") @RequestParam(value = "plannumber", required = false) String plannumber,
            @Parameter(name = "planname", description = "planname") @RequestParam(value = "planname", required = false) String planname,
            @Parameter(name = "starttime_min", description = "starttime_min") @RequestParam(value = "starttime_min", required = false) String starttime_min,
            @Parameter(name = "starttime_max", description = "starttime_max") @RequestParam(value = "starttime_max", required = false) String starttime_max,
            @Parameter(name = "status", description = "status") @RequestParam(value = "status", required = false) String status,
            @Parameter(name = "year", description = "year") @RequestParam(value = "year", required = false) String year,

            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        TblTestplan plan = new TblTestplan();
        if (planname != null && planname.length() > 0) {
            plan.setPlanname(planname);
        }
        if (plannumber != null && plannumber.length() > 0) {
            plan.setPlannumber(plannumber);
        }
        if (status != null && status.length() > 0) {
            plan.setPlanstatus(status);
        }
        if (year != null && year.length() > 0) {
            plan.setPlanyear(year);
        }

//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
//        TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");

        boolean bool = csfaService.isSJByOrgId(user.getLinkDetp().getOrgid().toString());
//        IPage<TblTestplan> iPage;
//        if (bool) {
//            iPage = csgzService.findAllTrack(plan, pageNumber, starttime_min, starttime_max, user.getCurrentOrg().getOrgid());
//        } else {
//            iPage = csgzService.findAllnoSjTrack(plan, pageNumber, starttime_min, starttime_max,
//                    user.getStaffid().toString());
//        }
      PageInfo<TblTestplan> iPage;
      if (bool) {
          iPage = csgzService.findAllTrackNew(plan, pageNumber, starttime_min, starttime_max, user.getCurrentOrg().getOrgid(),user);
      } else {
          iPage = csgzService.findAllnoSjTrackNew(plan, pageNumber, starttime_min, starttime_max,
                  user.getStaffid().toString(),user);
      }
        IPageResult<TblTestplan> pageInfo=new IPageResult<TblTestplan>().buildIpage(iPage);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("starttime_min", starttime_min);
        mv.put("starttime_max", starttime_max);
        mv.put("pageBean", pageInfo);

        mv.put("plan", plan);
        return new JsonBean(200, "success", mv);


    }


    @OperationLog(
            success = "测试跟踪-主页-点击编号查询成功",
            busType = "内控测试",
            fail = "测试跟踪-主页-点击编号查询失败",
            operationType = OperationType.SELECT,
            subType = "测试跟踪"
    )
    @GetMapping(value = "/csgz/nkcs/plan/detail")
    @Operation(summary = "测试跟踪-主页-点击编号")
    public JsonBean nkcs_ctrltest_plan_detail(

            @Parameter(name = "selectProjectid", description = "testplanid") @RequestParam(value = "selectProjectid") BigDecimal selectProjectid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mv = new HashMap<>();
        mv.put("test", csfaService.queryOneTestPlanVo(selectProjectid));

        return new JsonBean(200, "success", mv);
    }


    @OperationLog(
            success = "测试跟踪-主页-点击任务跟踪查询成功",
            busType = "内控测试",
            fail = "测试跟踪-主页-点击任务跟踪查询失败",
            operationType = OperationType.SELECT,
            subType = "测试跟踪"
    )
    @Operation(summary = "测试跟踪-主页-点击任务跟踪")
    @GetMapping(value = "/csgz/csgz_list")
    public JsonBean csgz_list(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name="selectProjectid",description="testplanid",required=false) @RequestParam(value = "selectProjectid", required = false) BigDecimal selectProjectid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil user = userProvider.get();
        if (user == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
//        IPage<Map<String,Object>> page=new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
//        if (selectProjectid != null ) {
//            page = csgzService.findAllnoSjTrack2(selectProjectid, pageNumber);
//        }
        PageInfo<Map<String,Object>> page=new   PageInfo<Map<String,Object>>();
        if (selectProjectid != null ) {
            page = csgzService.findAllnoSjTrack2New(selectProjectid, pageNumber);
        }
        IPageResult<Map<String,Object>> Ipage=new  IPageResult<Map<String,Object>>().buildIpage(page);
        Map<String, Object> mv = new LinkedHashMap<>();
        mv.put("selectProjectid",selectProjectid);
        mv.put("pageBean",Ipage);
        return new JsonBean(200, "success", mv);

    }

}
