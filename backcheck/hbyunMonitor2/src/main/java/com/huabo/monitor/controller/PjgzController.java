package com.huabo.monitor.controller;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

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
import com.huabo.monitor.entity.TblAssess;
import com.huabo.monitor.entity.TblAssessMarkVo;
import com.huabo.monitor.entity.TblAssessVo;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.service.PjgzService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.util.IPageResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@Tag(name="评价管理-评价跟踪",description="评价管理-评价跟踪")
@RequestMapping(value = "/nbkz")
public class PjgzController {
    @Resource
    private PjgzService pjgzService;
    @Resource
    private TblAssessService tblAssessService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "评价管理-评价跟踪-主页查询成功",
            busType = "内控设置",
            fail = "评价管理-评价跟踪-主页查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @GetMapping(value = "/pjgl/proj_list")
    @Operation(summary = "评价管理-评价跟踪-主页查询")
    public JsonBean proj_listPjgl(
            @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "assNumnber", description = "评价编号") @RequestParam(value = "assNumnber", required = false) String assNumnber,
            @Parameter(name = "assName", description = "项目名称") @RequestParam(value = "assName", required = false) String assName,
            @Parameter(name = "startDate", description = "开始时间:格式年-月-日") @RequestParam(value = "startDate", required = false) String startDate,
            @Parameter(name = "startDates", description = "--开始时间") @RequestParam(value = "startDates", required = false) String startDates,
            @Parameter(name = "endDate", description = "结束日期") @RequestParam(value = "endDate", required = false) String endDate,
            @Parameter(name = "endDates", description = "--结束日期") @RequestParam(value = "endDates", required = false) String endDates,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        boolean isAudit = false;
        TblOrganization organization=this.tblAssessService.queryOrganizationById(staff.getLinkDetp().getOrgid());
        if (organization.getAudittype() != null
                && organization.getAudittype().equals(new BigDecimal(TblOrganization.AUDITTYPE))) {
            isAudit = true;
        }
      //  IPage<TblAssessVo> iPage=tblAssessService.initiatePjgl( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber, assNumnber, assName, startDate, startDates, endDate, endDates,1);

        PageInfo<TblAssessVo> info=tblAssessService.initiatePjglNew( isAudit,staff.getCurrentOrg().getOrgid(),staff.getStaffid(),staff.getRealname(),pageNumber,pageSize, assNumnber, assName, startDate, startDates, endDate, endDates,1,staff);
        IPageResult<TblAssessVo> iPage=new IPageResult<TblAssessVo>().buildIpage(info);
        Map<String, Object> mv = new HashMap<>();
        mv.put("assNumnber", assNumnber);
        mv.put("assName", assName);
        mv.put("startDate", startDate);
        mv.put("startDates", startDates);
        mv.put("endDate", endDate);
        mv.put("endDates", endDates);
        mv.put("pageBean", iPage);
        return new JsonBean(200, "success", mv);
    }


    /**
     * 项目跟踪---评价对象列表
     *
     * @param pageNumber
     * @param selectedPlans
     * @return
     */
    @OperationLog(
            success = "评价管理-评价跟踪-跟踪查询成功",
            busType = "内控设置",
            fail = "评价管理-评价跟踪-跟踪查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @GetMapping(value = "/pjgl/t08_proj_task")
    @Operation(summary = "评价管理-评价跟踪-跟踪")
    public JsonBean t08_proj_task(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="selectedPlans",description="selectedPlans",required=false) @RequestParam(value = "selectedPlans", required = false) BigDecimal selectedPlans,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Map<String, Object> mav = new HashMap<>();
        if (null != selectedPlans) {
           // IPage<TblAssessMarkVo> page = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
          //  IPage<TblAssessMarkVo> pageBean = this.pjgzService.findMarkByOrgGroup(page, selectedPlans);
            TblAssess assess = tblAssessService.getById(selectedPlans);
            PageInfo<TblAssessMarkVo> page=pjgzService.findMarkByOrgGroupNew(pageNumber, selectedPlans);
            IPageResult<TblAssessMarkVo> pageBean=new IPageResult<TblAssessMarkVo>().buildIpage(page);
            mav.put("pageBean", pageBean);
            mav.put("project",assess);
        }
        return new JsonBean(1,"success", mav);
    }


    @OperationLog(
            success = "评价跟踪-跟踪-点击评价对象(评价人列表)查询成功",
            busType = "内控设置",
            fail = "评价跟踪-跟踪-点击评价对象(评价人列表)查询失败",
            operationType = OperationType.SELECT,
            subType = "评价跟踪"
    )
    @Operation(summary = "评价跟踪-跟踪-点击评价对象(评价人列表)")
    @GetMapping(value = "/pjgl/t08_proj_person")
    public JsonBean t08_proj_person(
            @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @Parameter(name="selectedPlans",description="selectedPlans",required=false) @RequestParam(value = "selectedPlans", required = false) BigDecimal selectedPlans,
            @Parameter(name="orgid",description="orgid",required=false) @RequestParam(value = "orgid", required = false) BigDecimal orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
        if (staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
            Map<String, Object> mav = new HashMap<>();
           /// IPage<Map<String,Object>> page = new Page(pageNumber, ConstClass.DEFAULT_SIZE);

          //  pjgzService.findByperson(page,selectedPlans,orgid);


            PageInfo<Map<String,Object>>  page=pjgzService.findBypersonNew(pageNumber, selectedPlans,orgid);
            IPageResult<Map<String,Object>> pageBean=new IPageResult<Map<String,Object>>().buildIpage(page);
            mav.put("pageBean", pageBean);
            mav.put("selectedPlans", selectedPlans);
            mav.put("orgid", orgid);

            return new JsonBean(200,"success", mav);
    }

}



