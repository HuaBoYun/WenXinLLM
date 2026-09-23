package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.entity.Flow;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.huabo.cybermonitor.entity.MonitorIndicatorresult;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.TblBiDatasource;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IBiDatasourceService;
import com.huabo.cybermonitor.service.IControlmatrixService;
import com.huabo.cybermonitor.service.IFlowService;
import com.huabo.cybermonitor.service.IIndicatorCmService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IIndicatorthresholdService;
import com.huabo.cybermonitor.service.IMonitorIndicatorresultService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IRiskeventService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.ITblBiDatasourceService;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.ConstClass;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;



@RestController
@Slf4j
@Tag(name="指标监控-指标管理",description="指标监控-指标管理")
@RequestMapping(value = "/cyber/ZbglController")
@SuppressWarnings("all")
public class ZbglController {
    private static final Logger logger = LoggerFactory.getLogger(MxglController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    TreeService treeService;

    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IBiDatasourceService iBiDatasourceService;


    @Autowired
    IIndicatorthresholdService indicatorthresholdService;

    @Autowired
    IWarningResultService iWarningResultService;

    @Autowired
    IMonitorIndicatorresultService iMonitorIndicatorresultService;

    @Autowired
    IFlowService iFlowService;

    @Autowired
    IControlmatrixService iControlmatrixService;

    @Autowired
    IRiskeventService iRiskeventService;

    @Autowired
    IIndicatorCmService iIndicatorCmService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    ITblBiDatasourceService  iTblBiDatasourceService;

    @Resource
    private UserProvider userProvider;


    @Resource
    YhrPageMapper pageMapper;
    /**
     * 行业指标库---main页面
     *
     * @return
     */
    @Operation(summary = "info_index")
    @GetMapping(value = "/zbjk/info_index")
    public JsonBean info_index(@Parameter(name = "ty", description = "ty") @RequestHeader("ty") String ty,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业指标库---main页面");
        TblOrganizationUtil organization = null;
            organization = staff.getCurrentOrg(); //当前用户选择的组织hbOrgEntityOrgid
        Map map = new HashMap();
        List<Organization> findOrgTree = iOrganizationService.findOrgTreeObjByHY(organization.getOrgid().toString());
        map.put("ty", ty);
        map.put("list", findOrgTree);
        map.put("number", findOrgTree.size());
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-左侧菜单
     *
     * @param
     * @return
     */
    @Operation(summary = "info_left")
    @GetMapping(value = "/zbjk/info_left")
    public JsonBean info_left(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Organization userOrg = iOrganizationService.belongToCompany(staff.getOrgid().toString());
        Map<String, Object> map = new HashMap<>(2);
        map.put("userOrgId", userOrg.getOrgid());// 查询 TblOrganization 表
        map.put("orgid", staff.getOrgid());
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-列表
     */
    @Operation(summary = "kri_infoZbjk")
    @GetMapping(value = "/zbjk/kri_info")
    public JsonBean kri_infoZbjk(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) String orgId,
                                 @Parameter(name = "kricode", description = "kricode") @RequestParam(value = "kricode",required = false) String kricode,
                                 @Parameter(name = "kriname", description = "kriname") @RequestParam(value = "kriname",required = false) String kriname,
                                 @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",required = false, defaultValue = "1") Integer pageNumber,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
            //选则的机构
            attribute = staff.getCurrentOrg();
            //当前用户的机构
            attribute1 = staff.getLinkOrg();
        if (attribute.getOrgid().toString().equals(attribute1.getOrgid().toString())) {
            if (StringUtils.isBlank(orgId)) {
                orgId = staff.getLinkDetp().getOrgid().toString();
            }
        }
        else {
            if (StringUtils.isBlank(orgId)) {
                orgId = attribute.toString();
            }
        }

        IPage ip = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("ORGID", orgId);
        if(StringUtils.isNotBlank(kriname)){
            queryWrapper.like("INDICATORNAME", kriname );
        }
        if(StringUtils.isNotBlank(kricode)){
            queryWrapper.like("INDICATORCODE", kricode);
        }


        queryWrapper.eq("indicatordb", Indicator.IS_HY0);
        queryWrapper.orderByDesc("createDate", "INDICATORID");
        iIndicatorService.page(ip, queryWrapper);
        Map map = new HashMap();
        map.put("orgid", orgId);
        map.put("tblStaff", staff);
        map.put("kricode", kricode);
        map.put("kriname", kriname);
        map.put("pageBean", ip);
        return new JsonBean(200, "成功", map);
    }

    /**
     * RedisOrg
     *
     * @param
     * @param
     * @returnhttp://huabao.example.com/redisorg/findOrganizationByTreeAllbm?nodeId=116821
     */
    @Operation(summary = "查询树形结构")
    @GetMapping(value = "/zbjk/findOrganizationByTreeAllbm")
    public JsonBean findOrganizationByTreeAllbm(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId", required = false) String nodeId,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        return new JsonBean(200, "成功", "");
    }

//    dev@example.com
//    love0913

    /**
     * 指标管理-添加
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_addZbjk")
    @PostMapping(value = "/zbjk/kri_info_add")
    public JsonBean kri_info_addZbjk(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");

        //QueryWrapper<Organization> wrapper = new QueryWrapper<>();
        //wrapper.eq("orgid", staff.getLinkOrg().getOrgid());
        Organization organization = iOrganizationService.getById(staff.getLinkDetp().getOrgid());
        String orgid = organization.getOrgid().toString();
        //Organization og = iOrganizationService.getById(orgid);
        Map map = new HashMap();
        map.put("creater", staff.getUsername());
        map.put("creatTime", sf.format(new Date()));
        map.put("orgname", organization.getOrgname());
        map.put("orgid", orgid);
        map.put("staffid", staff.getStaffid());
        map.put("organization", organization);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 指标管理-保存
     *
     * @return
     */
    @Operation(summary = "kri_info_modZbjk")
    @GetMapping(value = "/zbjk/kri_info_mod")
    public JsonBean kri_info_modZbjk(@Parameter(name = "createdatea", description = "createdatea") @RequestParam(value = "createdatea") String createdatea,
                                     @Parameter(name = "indicatordes", description = "indicatordes") @RequestParam(value = "indicatordes") String indicatordes,
                                     @Parameter(name = "indicator", description = "indicator") @RequestBody() Indicator indicator,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        indicatordes = indicatordes.trim();
        if (createdatea != null && !"".equals(createdatea)) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            indicator.setCreatedate(LocalDateTime.parse(createdatea));
        } else {
            indicator.setCreatedate(LocalDateTime.now());
        }
        if (indicator.getIndicatorid() == null) {
            indicator.setRunstatus(new BigDecimal(0));
        }
        indicator.setIndicatordb(Indicator.IS_HY0);
        indicator.setIndicatordes(indicatordes != null ? indicatordes : null);
        iIndicatorService.save(indicator);

        Map map = new HashMap();
        map.put("selectProjectid", indicator.getIndicatorid());

        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-修改
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @Operation(summary = "kri_info_modZbjk_update")
    @GetMapping(value = "/zbjk/kri_info_modife")
    public JsonBean kri_info_modZbjk_update(@Parameter(name = "selectProjectid", description = "指标主键") @RequestParam(value = "selectProjectid") String selectProjectid,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Indicator indicator = null;
        List<Flow> fw = null;
        String flows = "";
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
            if(Objects.isNull(indicator)){
                return new JsonBean(500, selectProjectid + "没有找到指标",null);
            }

            fw = iFlowService.showListWithIndicatorid(indicator.getIndicatorid());
            for (Flow tblFlow : fw) {
                flows += tblFlow.getFlowid() + ",";
            }
        }
        Staff user = iStaffService.getById(indicator.getStaffid().toString());
        Map map = new HashMap();

        map.put("indicator", indicator);
        map.put("org", iOrganizationService.getById(indicator.getOrgid()));
        //全部flow
        map.put("flowList",fw);
        //所有编号
        map.put("flows", flows);
        map.put("creater", user);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-计算公式
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_formulaZbjk")
    @GetMapping(value = "/zbjk/kri_info_formula")
    public JsonBean kri_info_formulaZbjk(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        Indicator indicator = null;
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
        }
        map.put("selectProjectid", selectProjectid);
        map.put("indicator", indicator);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-选择数据源列表
     *
     * @param
     * @return
     */
    @Operation(summary = "zbjk_datasource")
    @GetMapping(value = "/zbjk/ds_data_list")
    public JsonBean zbjk_datasource(@Parameter(name = "pid", description = "pid") @RequestParam(value = "pid",required=false) String pid,

                                    @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber") Integer pageNumber,

                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (StringUtils.isBlank(pid)) {
            pid = "0";
        }
        Map map = new HashMap();
        IPage<TblBiDatasource> ip = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        QueryWrapper<TblBiDatasource> qw = new QueryWrapper<>();
        qw.eq("FATHERID", pid);
        qw.isNotNull("ISLEAF");
        qw.orderByDesc("CREATEDATE", "DSID");
        iTblBiDatasourceService.page(ip,qw);
        map.put("faflowid", pid);
        map.put("pageBean", ip);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-选择数据源左侧菜单
     *
     * @return
     */
    @Operation(summary = "ds_data_left")
    @GetMapping(value = "/zbjk/ds_data_left")
    public JsonBean ds_data_left(@Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid",required = false) String orgid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
            //选则的机构
            attribute = staff.getCurrentOrg();
            //当前用户的机构
            attribute1 = staff.getLinkOrg();
        String treeName = "";
        if (StringUtils.isEmpty(orgid)) {
            orgid = attribute.getOrgid().toString();
        }
        Organization o = treeService.belongToCompany(orgid);
        orgid = o.getOrgid().toString();
        treeName = o.getOrgname();
        final List<TblBiDatasource> list = iTblBiDatasourceService.getTreeList(orgid);
        String str = "";
        for (int i = 0; i < list.size(); i++) {
            TblBiDatasource bid = list.get(i);
            str += "tree.nodes['"+bid.getFatherid()+"_"+bid.getDsid()+"']=\"text:"+bid.getDsname()+";method:check("+bid.getDsid()+","+bid.getFatherid()+");\"\n";
            //str += "tree.nodes['"+ (i==0?-1:cat.getFatherriskcatid())+"_"+cat.getRiskcatid()+"']=\"text:"+cat.getRiskcatname()+";method:check("+cat.getRiskcatid()+","+(cat.getUnit()==null?-1:cat.getUnit())+","+(cat.getIsleaf()==null?0:cat.getIsleaf())+")\";\n";
        }
        Map map = new HashMap();
        map.put("tree", str);
        map.put("treeList",list);
        map.put("orgid", orgid);
        map.put("treeName", treeName);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-指标基准阈值列表
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_range_mngZbjk")
    @GetMapping(value = "/zbjk/kri_info_range_mng")
    public JsonBean kri_info_range_mngZbjk(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                                           @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") String orgId,
                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<Indicatorthreshold> hold = indicatorthresholdService.QueryByIndicatorId(selectProjectid);
        Map map = new HashMap();
        map.put("selectProjectid", selectProjectid);
        map.put("hold", hold);
        map.put("orgId", orgId);
        map.put("tableHistoryRows", hold.size());

        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-添加指标基本值域
     *
     * @param
     * @return
     */
    @Operation(summary = "新增")
    @PostMapping(value = "/zbjk/kri_info_range_save")
    public JsonBean add_zbzy(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                             @Parameter(name = "sequencenumber", description = "sequencenumber") @RequestParam(value = "sequencenumber") String sequencenumber,
                             @Parameter(name = "tolerance", description = "tolerance") @RequestParam(value = "tolerance") String tolerance,
                             @Parameter(name = "thresholdname", description = "thresholdname") @RequestParam(value = "thresholdname") String thresholdname,
                             @Parameter(name = "tolerancelower", description = "tolerancelower") @RequestParam(value = "tolerancelower") String tolerancelower,
                             @Parameter(name = "toleranceupper", description = "toleranceupper") @RequestParam(value = "toleranceupper") String toleranceupper,
                             @Parameter(name = "regionvalue", description = "regionvalue") @RequestParam(value = "regionvalue") String regionvalue,
                             @Parameter(name = "prewarningmethod", description = "prewarningmethod") @RequestParam(value = "prewarningmethod") String prewarningmethod,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Indicator in = new Indicator();
        if (selectProjectid != null) {
            in.setIndicatorid(new BigDecimal(selectProjectid));
        }
        Indicatorthreshold hold = new Indicatorthreshold();
        if (tolerancelower != null && tolerancelower.length() > 0) {
            hold.setTolerancelower(tolerancelower);
        }
        if (toleranceupper != null && toleranceupper.length() > 0) {
            hold.setToleranceupper(toleranceupper);
        }
        hold.setPrewarningmethod(prewarningmethod);
        hold.setRegionvalue(regionvalue);
        hold.setThresholdname(thresholdname);
        hold.setSequencenumber(sequencenumber);
        hold.setTolerance(tolerance);
        hold.setIndicatorid(in.getIndicatorid().intValue());
        indicatorthresholdService.save(hold);
        return new JsonBean(200, "成功", "");
    }

    /**
     * 指标管理-阈值删除
     *
     * @param
     * @return
     */
    @Operation(summary = "删除")
    @DeleteMapping(value = "zbjk/delete_zbglj_del")
    public JsonBean delete_zbglj_del(@Parameter(name = "thresholdid", description = "thresholdid") @RequestParam(value = "thresholdid") BigDecimal thresholdid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (thresholdid != null && !"".equals(thresholdid)) {
            Indicatorthreshold hold = indicatorthresholdService.getById(thresholdid);
            indicatorthresholdService.removeById(thresholdid);
        }
        return new JsonBean(200, "成功", "");
    }


    /**
     * 指标管理-修改权限判断
     *
     * @param
     * @return
     */
    @Operation(summary = "indicatorcheckUpdate")
    @GetMapping(value = "/zbjk/indicatorcheckupdate")
    public JsonBean indicatorcheckUpdate(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid") String selectedid
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
            //选则的机构
            attribute = staff.getCurrentOrg();

        Indicator rule = this.iIndicatorService.getById(selectedid);
        if (rule.getStaffid().toString().equals(staff.getStaffid())) {
            return new JsonBean(200, "成功", "0");
        } else {
            return new JsonBean(200, "权限不足", "1");
        }
    }

    /**
     * 验证指标是否被预警使用
     *
     * @param
     * @return
     */
    @Operation(summary = "kzjz_info_is")
    @GetMapping(value = "/zbjk/kzjz_info_is")
    public JsonBean kzjz_info_is(@Parameter(name = "incid", description = "incid") @RequestParam(value = "incid") String incid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        int num = 0;
        if (incid != null && !"".equals(incid)) {
            Indicator in = iIndicatorService.getById(incid);
            QueryWrapper<MonitorSolution> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("SOLUTIONID", in.getIndcatid());
            List<MonitorSolution> monitorSolutions = iMonitorSolutionService.list(queryWrapper);
            num = monitorSolutions.size();
            if (in != null && in.getRunstatus().toString().equals("2")) {
                num = 2;
            }
        }
        return new JsonBean(200, "成功", num + "");
    }


    /**
     * 指标管理-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_modZbjk_del")
    @DeleteMapping(value = "/zbjk/kri_info_del")
    @Transactional(rollbackFor = Exception.class)
    public JsonBean kri_info_modZbjk_del(@Parameter(name = "selectProjectid", description = "指标主键") @RequestParam(value = "selectProjectid") String selectProjectid,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        return iFlowService.deleteIndicatorById(selectProjectid);
    }


    /**
     * 指标管理-启用和禁用修改指标状态
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_status")
    @GetMapping(value = "/zbjk/kri_info_status")
    public JsonBean kri_info_status(@Parameter(name = "incid", description = "incid") @RequestParam(value = "incid") String incid,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (incid != null && !"".equals(incid)) {
            Indicator indicator = iIndicatorService.getById(incid);
            if(Objects.isNull(indicator)){
                return new JsonBean(500, incid + "指标没有找到", 0);
            }
            if (indicator.getIndicatorstatus().equals("启用")) {
                indicator.setIndicatorstatus("禁用");
            } else {
                indicator.setIndicatorstatus("启用");
            }
            iIndicatorService.updateById(indicator);
        }
        return new JsonBean(200, "成功", 0);
    }


    /**
     * 指标管理 --结果
     *
     * @param pageNumber
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月31日 上午1:58:40
     */
    @Operation(summary = "kri_info_reult")
    @GetMapping(value = "/zbjk/kri_info_result")
    public JsonBean kri_info_reult(@Parameter(name = "id", description = "id") @RequestParam(value = "id",required = false) BigDecimal id,
                                   @Parameter(name = "execId", description = "execId") @RequestParam(value = "execId",required = false) String execId,
                                   @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,

                                   @Parameter(name = "source", description = "source") @RequestParam(value = "source",required = false) Integer source,
                                   @Parameter(name = "mid", description = "mid") @RequestParam(value = "mid",required = false) String mid,
                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId",required = false) String orgId,
                                   @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value = "fhtype",required = false) String fhtype,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Indicator ind = iIndicatorService.getById(id.toString());

        if (StringUtils.isNotBlank(mid)) {
            mid = "1";
        }
        Map map = new HashMap();
        String sql=" select t.*,TBL_STAFF.USERNAME,TBL_STAFF.REALNAME,TI.INDICATORNAME from TBL_MONITOR_INDICATORRESULT  t left join  TBL_STAFF  on t.STAFFID = TBL_STAFF.STAFFID\n" +
                "left join TBL_INDICATOR TI on t.INDICATORID = TI.INDICATORID";
        // 指标
        IPage<Map<String,Object>>  page =new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        String lastId = null;
        if (StringUtils.isNotBlank(mid)) {
            if (source.equals(MonitorSolutionresult.YJYJ)) {
                lastId = iWarningResultService.getLast();
            }

            sql+=" where indicatorid='"+id+"' and  source='"+source+"' " ;
            if (null != lastId) {
                sql+=" and executeId='"+lastId+"'" ;

            }
            sql+=" order by  saveTime desc ";

            map.put("pageBean",pageMapper.getPage(page,sql) );
        } else {
            sql+=" where SOLUTIONRESULTID=  "+id;
            sql+=" and executeId='"+execId+"'  ";

            map.put("pageBean",pageMapper.getPage(page,sql) );
            // 指标方案

        }

        map.put("id", id);
        map.put("mid", mid);
        map.put("orgId", orgId);
        map.put("ind", ind);
        map.put("execId", execId);
        map.put("source", source);
        map.put("fhtype", fhtype);
        String url = "/znjk/gzgl/indicatormodel";
        if (StringUtils.isNotBlank(mid)) {
            url = "/znjk/gzgl/indicator";
        }
        map.put("url", url);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 行业指标库、指标管理,互相复制。
     *
     * @param
     * @return
     */
    @Operation(summary = "copyIndex")
    @GetMapping(value = "/index/copyIndex")
    public JsonBean copyIndex(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                              @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid") String orgid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
            //选则的机构
            attribute = staff.getCurrentOrg();
            //当前用户的机构

        Indicator indicator = iIndicatorService.getById(selectProjectid);

        Indicator tblIndicator = new Indicator();
        if (indicator.getIndicatordb() == 0) {
            tblIndicator.setAuditingstatus(indicator.getAuditingstatus());
            tblIndicator.setConnectionstrings(indicator.getConnectionstrings());
            tblIndicator.setCreatedate(LocalDateTime.now());
            tblIndicator.setDepartmentincharge(indicator.getDepartmentincharge());
            tblIndicator.setForlumachs(indicator.getForlumachs());
            tblIndicator.setFormula(indicator.getFormula());
            tblIndicator.setFormulades(indicator.getFormulades());
            tblIndicator.setIndicatorcode(indicator.getIndicatorcode());
            tblIndicator.setIndicatordb(indicator.getIndicatordb());
            tblIndicator.setIndicatordes(indicator.getIndicatordes());
            tblIndicator.setIndicatorname(indicator.getIndicatorname());
            tblIndicator.setIndicatorstatus(indicator.getIndicatorstatus());
            tblIndicator.setMemo(indicator.getMemo());
            tblIndicator.setRunstatus(new BigDecimal(0));
            tblIndicator.setUnittype(indicator.getUnittype());
            //Organization tblOrganization = new Organization();
            //tblOrganization.setOrgid(new BigDecimal(orgid));
            //iOrganizationService.save(tblOrganization);
            tblIndicator.setOrgid(new BigDecimal(orgid).intValue());
            tblIndicator.setIndicatordb(Indicator.IS_HY1);
            tblIndicator.setStaffid(staff.getStaffid());
            iIndicatorService.save(tblIndicator);
            //复制阈值
            QueryWrapper<Indicatorthreshold> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("INDICATORID", selectProjectid);
            queryWrapper.orderByAsc("SEQUENCENUMBER");
            List<Indicatorthreshold> list = indicatorthresholdService.list(queryWrapper);
            for (Indicatorthreshold tblIndicatorthreshold : list) {
                Indicatorthreshold tbl = new Indicatorthreshold();
                tbl.setMemo(tblIndicatorthreshold.getMemo());
                tbl.setPrewarningmethod(tblIndicatorthreshold.getPrewarningmethod());
                tbl.setRegionvalue(tblIndicatorthreshold.getRegionvalue());
//                tbl.setScore(tblIndicatorthreshold.getScore());
                tbl.setSequencenumber(tblIndicatorthreshold.getSequencenumber());
                tbl.setThresholdname(tblIndicatorthreshold.getThresholdname());
                tbl.setTolerance(tblIndicatorthreshold.getTolerance());
                tbl.setToleranceupper(tblIndicatorthreshold.getToleranceupper());
                tbl.setTolerancelower(tblIndicatorthreshold.getTolerancelower());
                tbl.setIndicatorid(tblIndicator.getIndicatorid().intValue());
                indicatorthresholdService.save(tbl);
            }

            return new JsonBean(200, "成功", "复制成功！");

        } else {
            tblIndicator.setAuditingstatus(indicator.getAuditingstatus());
            tblIndicator.setConnectionstrings(indicator.getConnectionstrings());
            tblIndicator.setCreatedate(LocalDateTime.now());
            tblIndicator.setDepartmentincharge(indicator.getDepartmentincharge());
            tblIndicator.setForlumachs(indicator.getForlumachs());
            tblIndicator.setFormula(indicator.getFormula());
            tblIndicator.setFormulades(indicator.getFormulades());
            tblIndicator.setIndicatorcode(indicator.getIndicatorcode());
            tblIndicator.setIndicatordb(indicator.getIndicatordb());
            tblIndicator.setIndicatordes(indicator.getIndicatordes());
            tblIndicator.setIndicatorname(indicator.getIndicatorname());
            tblIndicator.setIndicatorstatus(indicator.getIndicatorstatus());
            tblIndicator.setMemo(indicator.getMemo());
            tblIndicator.setRunstatus(new BigDecimal(0));
            tblIndicator.setUnittype(indicator.getUnittype());

//            QueryWrapper<Organization> wrapper = new QueryWrapper<>();
//            wrapper.eq("ORGID", staff.getOrgid());
//            Organization organization = iOrganizationService.getById(wrapper);


            tblIndicator.setOrgid(staff.getLinkDetp().getOrgid().intValue());

            tblIndicator.setIndicatordb(Indicator.IS_HY0);
            tblIndicator.setStaffid(staff.getStaffid());
            iIndicatorService.save(tblIndicator);
            //复制阈值
            QueryWrapper<Indicatorthreshold> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("INDICATORID", selectProjectid);
            queryWrapper.orderByAsc("SEQUENCENUMBER");
            List<Indicatorthreshold> list = indicatorthresholdService.list(queryWrapper);
            for (Indicatorthreshold tblIndicatorthreshold : list) {

                Indicatorthreshold tbl = new Indicatorthreshold();
                tbl.setMemo(tblIndicatorthreshold.getMemo());
                tbl.setPrewarningmethod(tblIndicatorthreshold.getPrewarningmethod());
                tbl.setRegionvalue(tblIndicatorthreshold.getRegionvalue());
                //tbl.set
                //tbl.setScore(tblIndicatorthreshold.getScore());
                tbl.setSequencenumber(tblIndicatorthreshold.getSequencenumber());
                tbl.setThresholdname(tblIndicatorthreshold.getThresholdname());
                tbl.setTolerance(tblIndicatorthreshold.getTolerance());
                tbl.setToleranceupper(tblIndicatorthreshold.getToleranceupper());
                tbl.setTolerancelower(tblIndicatorthreshold.getTolerancelower());
                tbl.setIndicatorid(tblIndicator.getIndicatorid().intValue());
                indicatorthresholdService.save(tbl);

            }

            return new JsonBean(200, "成功", "复制成功！");

        }


    }

    /**
     * 指标管理-查看
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @Operation(summary = "kri_info_disp")
    @GetMapping(value = "/zbjk/kri_info_disp")
    public JsonBean kri_info_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Indicator indicator = null;
        List<Flow> fw = null;
        String flows = "";
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
            if(Objects.isNull(indicator)){
                return new JsonBean(500, selectProjectid + "没有找到指标",null);
            }

            fw = iFlowService.showListWithIndicatorid(indicator.getIndicatorid());
            for (Flow tblFlow : fw) {
                flows += tblFlow.getFlowid() + ",";
            }
        }
        Staff user = iStaffService.getById(indicator.getStaffid().toString());
        Map map = new HashMap();

        map.put("indicator", indicator);
        map.put("org", iOrganizationService.getById(indicator.getOrgid()));
        //全部flow
        map.put("flowList",fw);
        //所有编号
        map.put("flows", flows);
        map.put("creater", user);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-计算公式_查看
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_formula_disp")
    @GetMapping(value = "/zbjk/kri_info_formula_disp")
    public JsonBean kri_info_formula_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        Indicator indicator = null;
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
        }
        map.put("selectProjectid", selectProjectid);
        map.put("indicator", indicator);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 指标管理-指标基准阈值列表-查看
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_range_mng_disp")
    @GetMapping(value = "/zbjk/kri_info_range_mng_disp")
    public JsonBean kri_info_range_mng_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid") String selectProjectid,
                                            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId") String orgId,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        List<Indicatorthreshold> hold = indicatorthresholdService.QueryByIndicatorId(selectProjectid);
        Map map = new HashMap();
        map.put("selectProjectid", selectProjectid);
        map.put("hold", hold);
        map.put("orgId", orgId);
        map.put("tableHistoryRows", hold.size());

        return new JsonBean(200, "成功", "0");
    }

    @Autowired
    IAccbookService iAccbookService;
    @Autowired
    CJobTaskService cJobTaskService;
    @Autowired
    JobTaskService jobTaskService;
    /**
     * 指标监控 - 指标管理  - 执行功能
     * @param request
     * @param response
     * @return
     */
    @Operation(summary = "monitorExecute")
    @GetMapping(value = "/gzjk/monitor/monitorExecute")
    public JsonBean monitorExecute(
            @Parameter(name = "id", description = "指标主键") @RequestParam(value = "id") String id,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {

        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}

            System.out.println(staff);
            BigDecimal staffid = staff.getStaffid();
            BigDecimal orgId = staff.getCurrentOrg().getOrgid();
            System.out.println(staffid);
            System.out.println(orgId);
            Accbook accBook = iAccbookService.findUserById(staffid, orgId);
            if(accBook == null){
                return new JsonBean(500, id + "没有在accbook表里找到相关项", "0");
            }

            String book = accBook.getAcctid();
            String bookid = accBook.getBookid();
            String bookyear = accBook.getBookyear();

            Indicator indicator = iIndicatorService.getById(id);
            if(Objects.isNull(indicator)){
                return new JsonBean(500, id + "没有找到对应的指标", "0");
            }

            if (indicator.getIndicatorstatus().equals("启用")) {
                if (indicator.getRunstatus().toString().equals("1")) {
                    return new JsonBean(200, "执行中，请稍等！",null);
                } else {
                    List<Indicator> list = new ArrayList<Indicator>();
                    indicator.setConnectionstrings(accBook == null ? indicator.getConnectionstrings() : bookid);
                    list.add(indicator);
                    ScheduleJob job = null;
                    try {
                        iIndicatorService.modifyStatusRun(list);
                        job = this.cJobTaskService.zbTaskExec(list, MonitorIndicatorresult.ZJ, null, iStaffService.getById(staffid));
                    } catch (SchedulerException e) {
                        e.printStackTrace();
                        try {
                            this.iIndicatorService.modifyStatusRunError(list);
                            this.jobTaskService.changeStatus(job, "stop");
                        } catch (SchedulerException e1) {
                            e1.printStackTrace();
                        }
                    }
                    return new JsonBean(200,"成功",null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  new JsonBean(500, "请先启用",null);
    }


}
