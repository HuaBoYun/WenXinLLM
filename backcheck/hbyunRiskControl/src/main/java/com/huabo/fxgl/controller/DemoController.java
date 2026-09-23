package com.huabo.fxgl.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 风险管控测试控制器（已废弃）
 * <p>风险管控模块的测试接口，仅用于开发调试</p>
 *
 * @author hbyun
 */
//@RestController
//@RequestMapping(value = "/demo", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险管控模块的测试Controller",description="风险管控模块的测试Controller")
public class DemoController {

    @RequestMapping("/index")
    @ResponseBody
    @Operation(summary = "风险管控测试接口首页")
    public JsonBean index(@Parameter(name = "author", description = "开发者名字") String author,
                          @Parameter(name = "version", description = "版本号")String version) {
        JsonBean jsonBean = new JsonBean();
        JsonBean.success("success");
//        jsonBean.setCode(200);
        jsonBean.setData("Author: " + author + ", Version: " + version);
        return jsonBean;
    }

    /**
     * 风险管控 - 风险监控 - 供应商监控 - 供应商列表
     * @author LiYe
     * @version 1.0.1
     * @date 2022/8/2
     * @param find
     * @return
     */
    @Operation(summary = "风险预警-供应商监控列表")
    @RequestMapping(value = "/fxyj/fxyj_list")
//    @Operation(summary = "/fxxt/fxyj/fxyj_list")
    public ModelAndView fxyj_fxyj_list(@Parameter(name = "find", description = "find") Find find,
                                        @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(defaultValue = "1") Integer pageNumber,
                                       @Parameter(name = "limit", description = "limit") @RequestParam(defaultValue = "15") Integer limit,
                                       @Parameter(name = "teamid", description = "teamid") Integer teamid,
                                       @Parameter(name = "staff", description = "staff") Staff staff,
                                       @Parameter(name = "organization", description = "organization") Organization organization) {
        /*String number = request.getParameter("pageNumber");
        String teamid = request.getParameter("teamid");
        ModelAndView mv = new ModelAndView();
        Integer pageNumber = 1;
        if (number != null && number.length() > 0) {
            pageNumber = Integer.parseInt(number);
        }*/
//        TblStaff tblStaff = (TblStaff) request.getSession().getAttribute("longUser");
//        TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构

       /* IPage pageBean = new Page(pageNumber, limit);//分页设置
        if(teamid!=null && teamid.trim().length()>0) {
            pageBean = tblyyCompanyService.findByCompay(find, pageNumber, pageBean.getPageSize(),  new BigDecimal(teamid));
            @SuppressWarnings("unchecked")
            List<TblyyCompany> list = pageBean.getRecordList();
            List<TblyyCompany> newlist =new ArrayList<TblyyCompany>();
            if(list!=null && list.size()>0) {
                for (TblyyCompany tblyyCompany : list) {
                    List<Tblyyprice> list2 = tblyypriceService.findByIs(tblyyCompany.getReportmodel().getPriceid());
                    tblyyCompany.setList(list2);
                    newlist.add(tblyyCompany);
                }
                pageBean.setRecordList(newlist);
            }
        }else {
            pageBean =tblyyCompanyService.findBySqlPage(find,tblStaff.getStaffid(), pageNumber, pageBean.getPageSize(), attribute1.getOrgid());
            List<TblyyCompany> list = pageBean.getRecordList();
            List<TblyyCompany> newlist =new ArrayList<TblyyCompany>();
            if(list!=null && list.size()>0) {
                for (TblyyCompany tblyyCompany : list) {
                    List<Tblyyprice> list2 = tblyypriceService.findByIs(tblyyCompany.getReportmodel().getPriceid());
                    tblyyCompany.setList(list2);
                    newlist.add(tblyyCompany);
                }
                pageBean.setRecordList(newlist);
            }
        }
        mv.setViewName("/fxgl/fxyj/fxyj_list");
        mv.addObject("pageBean", pageBean);
        mv.addObject("teamid", teamid);
        mv.addObject("pageNumber", pageNumber);
        return mv;*/
        return null;
    }
/*
    @PostMapping(path = "/index", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "demo-index")
    public JsonBean index(@Parameter(name = "params", description = "params") @RequestBody Map params) {
        JsonBean jsonBean = new JsonBean();
        JsonBean.success("success");
//        jsonBean.setCode(200);
        jsonBean.setData("Author: " + params.get("author") + ", Version: " + params.get("version"));
        return jsonBean;
    }*/
}
