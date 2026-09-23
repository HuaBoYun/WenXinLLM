package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.BiPage;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.YyCompany;
import com.huabo.fxgl.entity.YyPrice;
import com.huabo.fxgl.entity.YyReportModel;
import com.huabo.fxgl.entity.YyTeam;
import com.huabo.fxgl.service.IBiPageService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.IYyCompanyService;
import com.huabo.fxgl.service.IYyPriceService;
import com.huabo.fxgl.service.IYyReportModelService;
import com.huabo.fxgl.service.IYyTeamService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 对应风险监控子模块 - 供应商监控页面
 * </p>
 *
 * @author LiYe
 * @version 1.0.1
 * @since 2022-08-02
 */
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险监控-供应商监控",description="风险监控-供应商监控")
@Slf4j
public class SupplierMonitorController {
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
     */
    @OperationLog(
            success = "查询监控数据内容成功",
            busType = "风险监控",
            fail = "查询监控数据内容失败",
            operationType = OperationType.SELECT,
            subType = "供应商监控"
    )
    @RequestMapping(value = "/fxxt/fxyj/fxyj_list")
    @Operation(summary = "监控数据内容")
    public JsonBean fxyj_fxyj_list(@Parameter(name = "find", description = "过滤查询条件") Find find,
                                   @Parameter(name = "pageNo", description = "页数") @RequestParam(defaultValue = "1") Integer pageNo,
                                   @Parameter(name = "pageSize", description = "页数量") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "teamid", description = "用户团队ID") @RequestParam(required = false) Integer teamid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "fxjktype", description = "风险监控类型") @RequestParam(required = true) String fxjktype) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

//        IPage page = new Page(pageNo, pageSize);//分页设置
//        IPage pageBean = null;
//        log.info("organization: " + organization);
        log.info("teamid: " + teamid);
        PageInfo<YyCompany> pageBean=null;
        if (teamid != null) {
            pageBean = yyCompanyService.findCompanyListByTeamid(pageNo, pageSize, find, teamid,fxjktype);
        } else {
            pageBean = yyCompanyService.findCompanyList(pageNo, pageSize, find, selectOrg.getOrgid(), staffUtil.getStaffid(),fxjktype);
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


    /**
     *
     *
     * @return
     * @author liye
     * @Date 2022/8/11
     */
    @OperationLog(
            success = "查询供应商/下属公司/投资机构/客户监控/竞争对手 分组列表成功",
            busType = "风险监控",
            fail = "查询供应商/下属公司/投资机构/客户监控/竞争对手 分组列表失败",
            operationType = OperationType.SELECT,
            subType = "供应商监控"
    )
    @RequestMapping(value = "/fxxt/riskwarning/main")
    @Operation(summary = "供应商/下属公司/投资机构/客户监控/竞争对手 分组列表")
    //测试数据：staffid=30, orgid=1
    public JsonBean main(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "fxjktype", description = "风险监控类型") @RequestParam(required = true) String fxjktype) throws Exception {
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
        List<BiPage> pageList = biPageService.getByOrgidAndStaffid(selectOrg.getOrgid(), staffUtil.getStaffid());
        if (pageList != null && pageList.size() > 0) {
            result.put("pageChilds", pageList);
        }
        //查询分组列表
        List<YyTeam> teamList = yyTeamService.getByOrgidAndStaffid(selectOrg.getOrgid(), staffUtil.getStaffid(),fxjktype);
        if (teamList != null && teamList.size() > 0) {
            result.put("teams", teamList);

        }
        QueryWrapper queryWrapper2 = new QueryWrapper();
        queryWrapper2.eq("STAFFID", staffUtil.getStaffid());
        queryWrapper2.eq("ORGID", selectOrg.getOrgid());
        queryWrapper2.eq("fxjktype",fxjktype );
        Integer totalCompanyCount = (int) this.yyCompanyService.count(queryWrapper2);

        result.put("count", totalCompanyCount);

        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        jsonBean.setMsg("success");
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
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
     */
    @OperationLog(
            success = "已有组内新增公司成功",
            busType = "风险监控",
            fail = "已有组内新增公司失败",
            operationType = OperationType.ADD,
            subType = "供应商监控"
    )
    @RequestMapping("/fxyj/savecompany")
    @Operation(summary = "已有组内新增公司")
    public String saveTeam(@Parameter(name = "teamid", description = "用户团队ID") @RequestParam String teamid,
                           @Parameter(name = "priceid", description = "外部数据标签ID") @RequestParam String priceid,
                           @Parameter(name = "pageid", description = "内部数据标签ID") @RequestParam String pageid,
                           @Parameter(name = "fxtype", description = "风险状况") @RequestParam String fxtype,
                           @Parameter(name = "companyName", description = "公司名称") @RequestParam String companyName,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "fxjktype", description = "风险监控类型") @RequestParam(required = true) String fxjktype) throws Exception {


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

        // 将 pageid 字符串拆分为列表，直接存入中间表，不查 TBL_BI_PAGE
        List<String> pageIdList = new ArrayList<>();
        if (pageid != null && !pageid.trim().isEmpty()) {
            pageIdList = Arrays.stream(pageid.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
        //查找对应的分组
        YyTeam team = yyTeamService.getById(teamid);
        Organization organization = new Organization();
        organization.setOrgid(staffOrg.getOrgid());

        YyCompany company = new YyCompany();
        company.setTeamid(teamid);
        company.setReport(yyReportModel);
        company.setCompanyname(companyName);
        company.setOrganization(organization);
        company.setStaff(staffService.getById(staffUtil.getStaffid()));
        company.setCreatedate(LocalDateTime.now());
        company.setFxtype(fxtype);
        company.setBiPageSet(new HashSet<>());
        company.setFxjktype(fxjktype);

        if (yyCompanyService.save(company)) {
            result = company.getCompanyid().toString();
            // 保存监控内部数据关联
            if (!pageIdList.isEmpty()) {
                yyCompanyService.insertPageIdsByStrList(company.getCompanyid(), pageIdList);
            }
        }

        return result;
    }
    @OperationLog(
            success = "新增&修改分组成功",
            busType = "风险监控",
            fail = "新增&修改分组失败",
            operationType = OperationType.UPDATE,
            subType = "供应商监控"
    )
    @RequestMapping("/fxyj/saveteam")
    @Operation(summary = "新增&修改分组")
    public String saveTeam(@Parameter(name = "teamid", description = "用户团队ID") @RequestParam(required = false) String teamid,
                           @Parameter(name = "groupname", description = "组名称") @RequestParam String groupname,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "fxjktype", description = "风险监控类型") @RequestParam(required = true) String fxjktype
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

            YyTeam team = new YyTeam( groupname, LocalDateTime.now(), staffUtil.getStaffid(), selectOrg.getOrgid(), 0L,fxjktype);
//            log.info("Team info: " + team);
//            YyTeam team = new YyTeam(groupname, LocalDateTime.now(), new BigDecimal(staffid), new BigDecimal(orgid), 0L);
            yyTeamService.save(team);
            return String.valueOf(team.getTeamid());
        }
    }

    /**
     * 风险创建---分组---删除
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/17
     */
    @OperationLog(
            success = "删除分组成功",
            busType = "风险监控",
            fail = "删除分组失败",
            operationType = OperationType.DELETE,
            subType = "供应商监控"
    )
    @Operation(summary = "删除分组")
    @RequestMapping(value = "/fxyj/delteam",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    public JsonBean delete_team(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "teamid", description = "分组主键", required = true)@RequestParam(value = "teamid", required = true) BigDecimal teamid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("TEAMID", teamid);

        //==删除CompanyPage
        yyCompanyService.deleteComPageByTeamId(teamid);

        //删除分组下公司
        yyCompanyService.remove(queryWrapper);

        yyTeamService.remove(queryWrapper);



        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
     * 风险创建---内外规---删除
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/17
     */
    @OperationLog(
            success = "删除公司成功",
            busType = "风险监控",
            fail = "删除公司失败",
            operationType = OperationType.DELETE,
            subType = "供应商监控"
    )
    @Operation(summary = "删除公司")
    @RequestMapping(value = "/fxyj/delcompany",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    public JsonBean delete_company(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "companyid", description = "公司主键", required = true)@RequestParam(value = "companyid", required = true) BigDecimal companyid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("COMPANYID", companyid);

        yyCompanyService.deleteComPageByCompanyId(companyid);
        yyCompanyService.remove(queryWrapper);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }

    /**
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
     */
    @OperationLog(
            success = "已有组内编辑公司成功",
            busType = "风险监控",
            fail = "已有组内编辑公司失败",
            operationType = OperationType.UPDATE,
            subType = "供应商监控"
    )
    @RequestMapping("/fxyj/updatecompany")
    @Operation(summary = "已有组内编辑公司")
    public String updatecompany(@Parameter(name = "teamid", description = "用户团队ID") @RequestParam String teamid,
                                @Parameter(name = "companyid", description = "公司ID") @RequestParam String companyid,
                           @Parameter(name = "priceid", description = "外部数据标签ID") @RequestParam String priceid,
                           @Parameter(name = "pageid", description = "内部数据标签ID") @RequestParam String pageid,
                           @Parameter(name = "fxtype", description = "风险状况") @RequestParam String fxtype,
                           @Parameter(name = "companyName", description = "公司名称") @RequestParam String companyName,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "fxjktype", description = "风险监控类型") @RequestParam(required = true) String fxjktype) throws Exception {


        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        // 将 pageid 字符串拆分为列表，直接存入中间表，不查 TBL_BI_PAGE
        List<String> pageIdList = new ArrayList<>();
        if (pageid != null && !pageid.trim().isEmpty()) {
            pageIdList = Arrays.stream(pageid.split(","))
                    .map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
        }
        //查找对应的分组
        YyTeam team = yyTeamService.getById(teamid);
        Organization organization = new Organization();
        organization.setOrgid(staffOrg.getOrgid());

        YyCompany company = yyCompanyService.getById(new BigDecimal(companyid));
        company.setBiPageSet(new HashSet<>());
        company.setOrganization(organization);
        company.setCompanyname(companyName);
        company.setFxjktype(fxjktype);
        company.setFxtype(fxtype);

        YyReportModel yyReportModel = company.getReport();
        yyReportModel.setPriceid(priceid);

        // updateEntity 内部会 deleteComPageByCompanyId + insertBiPageSet(空集合)
        // 此处先删除旧记录，再用新方法插入 stepid 列表
        yyCompanyService.updateEntity(company);
        if (!pageIdList.isEmpty()) {
            yyCompanyService.insertPageIdsByStrList(new BigDecimal(companyid), pageIdList);
        }
        return companyid;
    }


    /**
     * 供应商监控查询明细（修改页面）
     */
    @OperationLog(
            success = "供应商监控查询明细（修改页面）成功",
            busType = "风险监控",
            fail = "供应商监控查询明细（修改页面）失败",
            operationType = OperationType.SELECT,
            subType = "供应商监控"
    )
    @RequestMapping(value = "/fxxt/gysjkDetail")
    @Operation(summary = "供应商监控查询明细（修改页面）")
    //测试数据：staffid=30, orgid=1
    public JsonBean gysjkDetail(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "companyid", description = "companyid") @RequestParam(required = true) String companyid) throws Exception {
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

        //查询公司信息
        YyCompany yyCompany = yyCompanyService.getById(new BigDecimal(companyid));
        result.put("yyCompany", yyCompany);

        //已选的内部数据
        List<String> checkNb = biPageService.getBiPageByCompanyid(new BigDecimal(companyid));
        if (checkNb != null && checkNb.size() > 0) {
            result.put("checkNb", checkNb);
        }


        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        jsonBean.setMsg("success");
        jsonBean.setData(result);
        return jsonBean;
    }


}
