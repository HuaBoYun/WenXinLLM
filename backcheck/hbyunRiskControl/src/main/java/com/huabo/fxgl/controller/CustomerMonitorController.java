package com.huabo.fxgl.controller;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.service.IBiPageService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.IYyCompanyService;
import com.huabo.fxgl.service.IYyPriceService;
import com.huabo.fxgl.service.IYyReportModelService;
import com.huabo.fxgl.service.IYyTeamService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import io.swagger.v3.oas.annotations.Operation;

/**
 * <p>
 * 对应风险监控子模块 - 客户监控页面
 * </p>
 *
 * @author LiYe
 * @version 1.0.1
 * @since 2022-08-02
 */
@RestController
@RequestMapping(value = "/customer",method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险监控-客户监控",description="风险监控-客户监控")
@Slf4j
public class CustomerMonitorController {
    @Autowired
    private IYyCompanyService yyCompanyService;
    @Autowired
    private IYyPriceService yyPriceService;
    @Autowired
    private IStaffService staffService;
    @Autowired
    private IBiPageService biPageService;
    @Autowired
    private IYyTeamService yyTeamService;
    @Autowired
    private IYyReportModelService yyReportModelService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 根据条件查询监控数据列表的内容
     *
     * @param find
     * @return
     * @author LiYe
     * @version 1.0.1
     * @date 2022/8/2
     *//*
    @RequestMapping(value = "/monitor_list")
    @Operation(summary = "监控数据内容")
    public JsonBean fxyj_fxyj_list(@Parameter(name = "find", description = "过滤查询条件") Find find,
                                   @Parameter(name = "pageNo", description = "页数") @RequestParam(defaultValue = "1") Integer pageNo,
                                   @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "teamid", description = "用户团队ID") @RequestParam(required = false) Integer teamid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        IPage page = new Page(pageNo, pageSize);//分页设置
        IPage pageBean = null;
//        log.info("organization: " + organization);
        log.info("teamid: " + teamid);
        if (teamid != null) {
            pageBean = yyCompanyService.findCompanyListByTeamid(page, find, teamid);
        } else {
            pageBean = yyCompanyService.findCompanyList(page, find, selectOrg.getOrgid().intValue(), staffUtil.getStaffid().intValue());
        }
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        Map result = new HashMap();
        result.put("pageBean", pageBean);
        result.put("teamid", teamid);
        result.put("pageNumber", pageNo);
        result.put("fxtype", find.getFxtype());
        jsonBean.setData(result);

        return jsonBean;
    }


    *//**
     *
     *
     * @return
     * @author liye
     * @Date 2022/8/11
     *//*
    @RequestMapping(value = "/group_list")
    @Operation(summary = "客户分组列表")
    //测试数据：staffid=30, orgid=1
    public JsonBean main(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Map result = new HashMap();
        //监控外部数据
        List<YyPrice> list = yyPriceService.getAll();
        if (list != null && list.size() > 0) {
            result.put("list", list);
        }
        Staff staff = staffService.getById(staffUtil.getStaffid());//使用staffid查找完整的staff数据

        //查询监控内部数据
        List<BiPage> pageList = biPageService.getByOrgidAndStaffid(staffOrg.getOrgid(), staffUtil.getStaffid());
        if (pageList != null && pageList.size() > 0) {
            result.put("pageChilds", pageList);
        }
        //查询分组列表
        List<YyTeam> teamList = yyTeamService.getByOrgidAndStaffid(selectOrg.getOrgid(), staffUtil.getStaffid());
        if (teamList != null && teamList.size() > 0) {
            result.put("teams", teamList);
            result.put("count", teamList.size());
        }

        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        jsonBean.setMsg("success");
        jsonBean.setData(result);
        return jsonBean;
    }

    *//**
     * priceid: 2,3,4
     * pageid: 626107 ,626108
     * fxtype: 一般预警
     * companyName: 测试公司
     * teamid: 654890
     *
     * @param teamid
     * @param priceid
     * @param pageid
     * @param fxtype
     * @param companyName //     * @param token
     * @return
     * @throws Exception
     *//*
    @RequestMapping("/add_company")
    @Operation(summary = "已有组内新增公司")
    public String saveTeam(@Parameter(name = "teamid", description = "用户团队ID") @RequestParam String teamid,
                           @Parameter(name = "priceid", description = "外部数据标签ID") @RequestParam String priceid,
                           @Parameter(name = "pageid", description = "内部数据标签ID") @RequestParam String pageid,
                           @Parameter(name = "fxtype", description = "风险状况") @RequestParam String fxtype,
                           @Parameter(name = "companyName", description = "公司名称") @RequestParam String companyName,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {


        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织


        //保存 TBL_YY_REPORT_MODEL 表数据
        String result = null;//返回新增的 COMPANYID
        YyReportModel yyReportModel = new YyReportModel();
        yyReportModel.setOrgid(staffOrg.getOrgid());
        yyReportModel.setPriceid(priceid);
        yyReportModel.setStaffid(staffUtil.getStaffid());
        yyReportModel.setReportname(companyName);

        //查询选中的Page集合
        Set<BiPage> biPageSet = new HashSet<>(biPageService.getInPageid(pageid));
        //查找对应的分组
        YyTeam team = yyTeamService.getById(teamid);
        Organization organization = new Organization();
        organization.setOrgid(staffOrg.getOrgid());

        YyCompany company = new YyCompany();
        company.setTeam(team);
        company.setReport(yyReportModel);
        company.setCompanyname(companyName);
        company.setOrganization(organization);
        company.setStaff(staffService.getById(staffUtil.getStaffid()));
        company.setCreatedate(LocalDateTime.now());
        company.setFxtype(fxtype);
        company.setBiPageSet(biPageSet);
        if (yyCompanyService.save(company)) {
            result = company.getCompanyid().toString();
        }

        return result;
    }
    @RequestMapping("/group_update")
    @Operation(summary = "新增&修改分组")
    public String saveTeam(@Parameter(name = "teamid", description = "用户团队ID") @RequestParam(required = false) String teamid,
                           @Parameter(name = "groupname", description = "组名称") @RequestParam String groupname,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        log.info("----------------------------------staffUtil Info: " + staffUtil);
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if (StringUtils.isNotBlank(teamid)) {
            //若teamID不为空即为修改其分组名称
            YyTeam team = yyTeamService.getById(teamid);
            team.setTeamname(groupname);//修改分组名称
            yyTeamService.updateById(team);
            return teamid;
        } else {
            //若teamid为提供，认为是新增分组

            YyTeam team = new YyTeam( groupname, LocalDateTime.now(), staffUtil.getStaffid(), selectOrg.getOrgid(), 0L);
//            log.info("Team info: " + team);
//            YyTeam team = new YyTeam(groupname, LocalDateTime.now(), new BigDecimal(staffid), new BigDecimal(orgid), 0L);
            yyTeamService.save(team);
            return String.valueOf(team.getTeamid());
        }
    }*/

}
