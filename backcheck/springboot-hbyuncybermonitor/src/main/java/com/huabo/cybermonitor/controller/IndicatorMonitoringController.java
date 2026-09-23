package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BiDatasource;
import com.huabo.cybermonitor.entity.BugCriterion;
import com.huabo.cybermonitor.entity.Find;
import com.huabo.cybermonitor.entity.Indicator;
import com.huabo.cybermonitor.entity.IndicatorCm;
import com.huabo.cybermonitor.entity.IndicatorFlow;
import com.huabo.cybermonitor.entity.IndicatorRiskevent;
import com.huabo.cybermonitor.entity.Indicatorthreshold;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Worksheet;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBiDatasourceService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorCmService;
import com.huabo.cybermonitor.service.IIndicatorFlowService;
import com.huabo.cybermonitor.service.IIndicatorRiskeventService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IIndicatorthresholdService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.TreeService;
import com.huabo.cybermonitor.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;





/**
 * 指标监控api接口
 * @author kangjx
 * @createTime 2022/7/11
 */
@RestController
@Slf4j
@Tag(name="指标监控",description="指标监控")
@RequestMapping(value = "/cyber/IndicatorMonitoringController")
public class IndicatorMonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(IndicatorMonitoringController.class);

    @Autowired
    TreeService treeService;

    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IStaffService iStaffService;

    @Autowired
    IIndicatorthresholdService indicatorthresholdService;

    @Autowired
    IIndicatorFlowService iIndicatorFlowService;

    @Autowired
    IIndicatorRiskeventService iIndicatorRiskeventService;

    @Autowired
    IIndicatorCmService iIndicatorCmService;

    @Autowired
    IBiDatasourceService iBiDatasourceService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    IBugCriterionService iBugCriterionService;

    @Resource
    private UserProvider userProvider;
    
    /**
     * 行业指标库---main页面
     * @param Orgid 组织
     * @param ty ty
     * @return
     */
    @Operation(summary = "hy_info_index")
    @GetMapping(value = "/zbjk/hy_info_index")
    public JsonBean hy_info_index(@Parameter(name = "Orgid", description = "Orgid")@RequestParam("Orgid") String Orgid,
                                  @Parameter(name = "ty", description = "ty")@RequestParam("ty") String ty) throws Exception {
        logger.info("行业指标库---main页面");
        Map map = new HashMap();
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        List<Organization> findOrgTree = iOrganizationService.findOrgTreeObjByHY(Orgid);
        map.put("ty", ty);
        map.put("list", findOrgTree);
        map.put("number", findOrgTree.size());
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---左侧菜单
     * @param orgid 组织
     * @param type 类型
     * @return
     */
    @Operation(summary = "hy_info_left")
    @GetMapping(value = "/zbjk/hy_info_left")
    public JsonBean hy_info_left(@Parameter(name = "orgid", description = "orgid")@RequestParam(value="orgid",required = false)String orgid,
                                 @Parameter(name = "type", description = "type")@RequestParam(value="type",required = false)String type) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
    	
        logger.info("行业指标库---左侧菜单");
        List<Organization> findOrgTree = treeService.findOrgTreeObjByHY(orgid);
        String tree = "";
        String treeName="";
        if (StringUtils.isNotEmpty(orgid) && !orgid.equals(findOrgTree.get(0).getOrgid().toString())) {
            iOrganizationService.initOrgByOrgtype(Organization.TYPE_HY_ZBK, "行业指标库", orgid);
            tree = iOrganizationService.findOrgTreeByOrgtypeAndOrgid(orgid, Organization.TYPE_HY_ZBK, null);
            Organization o= (Organization) iOrganizationService.findOrganizationByOrgid(orgid);
            treeName=o.getOrgname();
        }else {
            treeName=findOrgTree.get(0).getOrgname();
        }
        int startNum = tree.indexOf("-1_");
        String defaultId = startNum > 0 ? tree.substring(startNum).substring(3, tree.substring(startNum).indexOf("'"))
                : "";
        Map map = new HashMap();
        map.put("icode", orgid);
        map.put("tree", tree);
        map.put("treeName", treeName.replace("┣", ""));
        map.put("orgTree", findOrgTree);
        map.put("defaultId", defaultId);
        map.put("selectorgtype", Organization.TYPE_HY_ZBK);
        map.put("targetFrame", "mainFramex");
        map.put("type",type);
        return new JsonBean(200,"成功","0");
    }

    /**
     * 行业指标库查询 HYG
     *
     * @param
     * @param find
     * @return
     */
    @Operation(summary = "hy_kri_info")
    @GetMapping(value = "/zbjk/hy_kri_info")
    public JsonBean hy_kri_info(@Parameter(name = "orgid", description = "orgid")@RequestParam(value="orgid",required = false)String orgid,
                                @Parameter(name = "pageNumber", description = "pageNumber")@RequestParam(value="pageNumber",required = false)String pageNumber,
                                @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(value="choiceSearch",required = false)String choiceSearch,
                                @Parameter(name = "find", description = "find")@RequestBody() Find find,
                                @Parameter(name = "limit", description = "limit")@RequestParam(value="limit",required = false)Integer limit) throws Exception {
        logger.info("行业指标库---列表页");
        
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        
        //默认显示第一个行业
        if (orgid==null||orgid.equals("")) {
            Organization org1 = iIndicatorService.getHYFirst();
            orgid=org1.getOrgid().toString();
        }
        IPage<Indicator> a = new Page<>((pageNumber == null ? 1 : Integer.parseInt(pageNumber)),limit);
        QueryWrapper<Indicator> wrapper=new QueryWrapper<>();
        wrapper.eq("ORGID",orgid);
        wrapper.eq("indicatordb",Indicator.IS_HY1);
        wrapper.or().eq("indicatordb",Indicator.IS_HY2);
        if (StringUtils.isNotEmpty(find.getCode())) {
            wrapper.eq("INDICATORCODE", find.getCode());
        }else{
            wrapper.eq("INDICATORNAME", find.getName());
        }
        wrapper.orderByDesc("INDICATORID");
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        Map map = new HashMap();
        map.put("choiceSearch", choiceSearch );
        map.put("pageBean", iIndicatorService.page(a,wrapper));
        map.put("orgid", orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---跳往添加页
     * @param orgid        组织
     * @param indicatorid 指标
     * @param choiceSearch 选择
     * @param staff    用户
     * @return
     */
    @Operation(summary = "hy_kri_info_add")
    @GetMapping(value = "/zbjk/hy_kri_info_add")
    public JsonBean hy_kri_info_add(@Parameter(name = "orgid", description = "orgid")@RequestParam(value="orgid",required = true)String orgid,
                                    @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value="indicatorid",required = true)String indicatorid,
                                    @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(value="choiceSearch",required = true)String choiceSearch,
                                    @Parameter(name = "staff", description = "staff")@RequestBody() Staff staff) throws Exception {
        logger.info("行业指标库---列表页---跳往添加页");
        TblStaffUtil loginstaff = userProvider.get();
		if (loginstaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("user", staff);
        Indicator ind = new Indicator();
        if (StringUtils.isNotEmpty(indicatorid)) {
            ind = iIndicatorService.getById(indicatorid);
            map.put("user",iStaffService.getById(ind.getStaffid().toString()));
            map.put("ind", ind);
        }
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        map.put("orgid", orgid);

        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---详情页
     * @param orgid        组织
     * @param indicatorid  指标
     * @return
     */
    @Operation(summary = "hy_kri_info_find")
    @GetMapping(value = "/zbjk/hy_kri_info_find")
    public JsonBean hy_kri_info_find(@Parameter(name = "orgid", description = "orgid")@RequestParam(value="orgid",required = true)String orgid,
                                     @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value="indicatorid",required = true)String indicatorid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
    	
        logger.info("行业指标库---列表页---详情页");
        Map map = new HashMap();
        Indicator ind = iIndicatorService.getById(indicatorid);
        map.put("user", iStaffService.getById(ind.getStaffid().toString()));
        map.put("ind", ind);
        map.put("orgid", orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---删除
     * @param indicatorids  指标
     * @return
     */
    @Operation(summary = "hy_kri_info_del")
    @GetMapping(value = "/zbjk/hy_kri_info_del")
    public JsonBean hy_kri_info_del(@Parameter(name = "indicatorids", description = "indicatorids") String[] indicatorids) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        logger.info("行业指标库---列表页---删除");
        String str = "0";
        for (int i = 0; i < indicatorids.length; i++) {
            try {
                iIndicatorService.removeById(indicatorids[i]);
            } catch (Exception e) {
                str = "1";
            }
        }
        return new JsonBean(200,"成功",str);
    }

    /**
     * 行业指标库---列表页---添加
     * @param date
     * @param indicator
     * @param choiceSearch
     * @param staff
     * @return
     */
    @Operation(summary = "hy_zbk_add")
    @GetMapping(value = "/zbjk/hy_zbk_add")
    public JsonBean hy_zbk_add(@Parameter(name = "date", description = "date") @RequestParam(value ="date",required = true) String date,
                               @Parameter(name = "indicator", description = "indicator") @RequestBody() Indicator indicator,
                               @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(value="choiceSearch",required = true)String choiceSearch,
                               @Parameter(name = "staff", description = "staff")@RequestBody() Staff staff) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
		if (loginstaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        logger.info("行业指标库---列表页---添加");
        indicator.setIndicatordb(1);
        if (indicator.getIndicatorid() != null) {
            try {
                indicator.setCreatedate(DateUtils.strToLocalDateTime(date,"yyyy-MM-dd"));
            } catch (Exception e) {
                logger.error("行业指标库---列表页---添加---时间转换错误");
            }
            iIndicatorService.updateById(indicator);

        } else {
            indicator.setCreatedate(LocalDateTime.now());
            iIndicatorService.save(indicator);
        }
        return hy_kri_info_add(indicator.getOrgid().toString(),indicator.getIndicatorid().toString(),choiceSearch,staff);
    }

    /**
     * 行业指标库---列表页---添加---跳往计算公式页面
     * @param orgid
     * @param indicatorid
     * @param choiceSearch
     * @return
     */
    @Operation(summary = "hy_zbk_jsgs_toadd")
    @GetMapping(value = "/zbjk/hy_zbk_jsgs_toadd")
    public JsonBean hy_zbk_jsgs_toadd(@Parameter(name = "orgid", description = "orgid") @RequestParam(value ="orgid",required = true)String orgid,
                                      @Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value ="indicatorid",required = true)String indicatorid,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value ="choiceSearch",required = true)String choiceSearch) throws Exception {
        logger.info("行业指标库---列表页---添加---跳往计算公式页面");
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        Indicator ind = iIndicatorService.getById(indicatorid);
        map.put("ind", ind);
        map.put("orgid", orgid);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }


    /**
     * 行业指标库---列表页---添加---跳往计算公式页面
     * @param indicatorid
     * @param orgid
     * @return
     */
    @Operation(summary = "hy_zbk_jsgs_find")
    @GetMapping(value = "/zbjk/hy_zbk_jsgs_find")
    public JsonBean hy_zbk_jsgs_find(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam(value ="indicatorid",required = true)String indicatorid,
                                     @Parameter(name = "orgid", description = "orgid") @RequestParam(value ="orgid",required = true)String orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        logger.info("行业指标库---列表页---添加---跳往计算公式页面");
        Map map = new HashMap();
        Indicator ind = iIndicatorService.getById(indicatorid);
        map.put("ind", ind);
        map.put("orgid", orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---添加---计算公式--添加
     * @param indicator
     * @param indicatorid
     * @param formula
     * @param expressiondesc
     * @param expression
     * @return
     */
    @Operation(summary = "hy_zbk_jsgs_add")
    @GetMapping(value = "/zbjk/hy_zbk_jsgs_add")
    public JsonBean hy_zbk_jsgs_add(@Parameter(name = "indicator", description = "indicator")@RequestBody Indicator indicator,
                                    @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam("indicatorid")String indicatorid,
                                    @Parameter(name = "formula", description = "formula")@RequestParam("formula")String formula,
                                    @Parameter(name = "expressiondesc", description = "expressiondesc")@RequestParam("expressiondesc")String expressiondesc,
                                    @Parameter(name = "expression", description = "expression")@RequestParam("expression")String expression) throws Exception {
        logger.info("行业指标库---列表页---添加---计算公式--添加");
        String str = "0";
        try {
        	TblStaffUtil staff = userProvider.get();
    		if (staff == null) {
    			return ResponseFormat.retParam(0, 20006, null);
    		}
             Indicator indicators = null;
            if (indicatorid != null && !"".equals(indicatorid)) {
                QueryWrapper<Indicator> queryWrapper = new QueryWrapper();
                queryWrapper.eq("indicatorid",new BigDecimal(indicatorid));
                indicators = iIndicatorService.getOne(queryWrapper);
                indicators.setFormula(formula);
                indicators.setForlumachs(expression);
                indicators.setFormulades(expressiondesc);
                iIndicatorService.updateById(indicators);
            }else{
                str = "1";
            }
        } catch (Exception e) {
            str = "1";
        }
        return new JsonBean(200,"成功",str);
    }

    /**
     * 行业指标库---列表页---添加---指标基准阈值--列表
     * @param indicatorid
     * @param orgid
     * @param choiceSearch
     * @return
     */
    @Operation(summary = "hy_kri_info_range_mng")
    @GetMapping(value = "/zbjk/hy_kri_info_range_mng")
    public JsonBean hy_kri_info_range_mng(@Parameter(name = "indicatorid", description = "indicatorid")@RequestParam("indicatorid")String indicatorid,
                                          @Parameter(name = "orgid", description = "orgid")@RequestParam("orgid")String orgid,
                                          @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
        logger.info("行业指标库---列表页---查看---指标基准阈值--列表-hy_kri_info_range_mng");
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        List<Indicatorthreshold> list = indicatorthresholdService.QueryByIndicatorId(indicatorid);
        map.put("list", list);
        map.put("indicatorid", indicatorid);
        map.put("orgid", orgid);
        map.put("tableHistoryRows", list.size());
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---查看---指标基准阈值--列表
     * @param indicatorid
     * @param orgid
     * @return
     */
    @Operation(summary = "hy_kri_info_range_mng_find")
    @GetMapping(value = "/zbjk/hy_kri_info_range_mng_find")
    public JsonBean hy_kri_info_range_mng_find(@Parameter(name = "indicatorid", description = "indicatorid")@RequestParam("indicatorid")String indicatorid,
                                               @Parameter(name = "orgid", description = "orgid")@RequestParam("orgid")String orgid) throws Exception {
        logger.info("行业指标库---列表页---查看---指标基准阈值--列表-hy_kri_info_range_mng_find");
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        List<Indicatorthreshold> list = indicatorthresholdService.QueryByIndicatorId(indicatorid);
        map.put("list", list);
        map.put("indicatorid", indicatorid);
        map.put("orgid", orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 行业指标库---列表页---添加---指标基准阈值--添加
     * @param thre
     * @return
     */
    @Operation(summary = "hy_range_mng_add")
    @GetMapping(value = "/zbjk/hy_range_mng_add")
    public JsonBean hy_range_mng_add( @Parameter(name = "thre", description = "thre")@RequestBody() Indicatorthreshold thre) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        logger.info("行业指标库---列表页---添加---指标基准阈值--添加");
        String str = "0";
        indicatorthresholdService.save(thre);
        return new JsonBean(200,"成功",str);
    }

    /**
     * 行业指标库---列表页---添加---指标基准阈值--删除
     * @param selectIds
     * @param indicatorid
     * @param orgid
     * @param choiceSearch
     * @return
     */
    @Operation(summary = "hy_range_mng_del")
    @GetMapping(value = "/zbjk/hy_range_mng_del")
    public JsonBean hy_range_mng_del(@Parameter(name = "selectIds", description = "selectIds")@RequestParam("selectIds")String[] selectIds,
                                     @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam("indicatorid")String indicatorid,
                                     @Parameter(name = "orgid", description = "orgid")@RequestParam("orgid")String orgid,
                                     @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
        logger.info("行业指标库---列表页---添加---指标基准阈值--删除");
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        for (int i = 0; i < selectIds.length; i++) {
            Indicatorthreshold s = new Indicatorthreshold();
            s.setThresholdid(new BigDecimal(selectIds[i]));
            indicatorthresholdService.removeById(s);
        }
        return hy_kri_info_range_mng(indicatorid,orgid,choiceSearch);
    }



    /**
     * 指标管理-左侧菜单
     *
     * @param
     * @return
     */
    @Operation(summary = "info_left")
    @GetMapping(value = "/zbjk/info_left")
    public JsonBean info_left(@Parameter(name = "userOrgId", description = "userOrgId")@RequestParam("userOrgId")String userOrgId,
                              @Parameter(name = "orgid", description = "orgid")@RequestParam("orgid")String orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap(2);
        map.put("userOrgId", userOrgId);// 查询 TblOrganization 表
        map.put("orgid", orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-删除权限判断
     *
     * @param
     * @return
     */
    /**
     * 指标管理-删除权限判断
     *
     * @param
     * @return
     */
    @Operation(summary = "indicatorcheckdel")
    @GetMapping(value = "/zbjk/indicatorcheckdel", produces = "application/json; charset=utf-8")
    public JsonBean indicatorcheckdel(@Parameter(name = "Staffid", description = "Staffid") @RequestParam("Staffid")String Staffid,
                                      @Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Indicator rule = this.iIndicatorService.getById(selectedid);
        if (rule.getStaffid().toString().equals(Staffid)) {
            if (rule.getRunstatus().toString().equals("0")) {
                return new JsonBean(200,"成功","0");
            } else {
                return new JsonBean(200,"该指标已执行，不能删除和修改","1");
            }
        } else {
            return new JsonBean(200,"权限不足","1");
        }
    }


    /**
     * 指标管理-修改权限判断
     *
     * @param
     * @return
     */
    @Operation(summary = "indicatorcheckUpdate")
    @GetMapping(value = "/zbjk/indicatorcheckupdate", produces = "application/json; charset=utf-8")
    public JsonBean indicatorcheckUpdate(@Parameter(name = "Staffid", description = "Staffid") @RequestParam("Staffid")String Staffid,
                                         @Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid")String selectedid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Indicator rule = this.iIndicatorService.getById(selectedid);
        if (rule.getStaffid().toString().equals(Staffid)) {
            return new JsonBean(200,"成功","0");
        } else {
            return new JsonBean(200,"权限不足","1");
        }
    }

    /**
     * 指标管理-选择数据源列表
     *
     * @param
     * @return
     */
    @Operation(summary = "zbjk_datasource")
    @GetMapping(value = "/zbjk/ds_data_list")
    public JsonBean zbjk_datasource(@Parameter(name = "faflowid", description = "faflowid")@RequestParam(value ="faflowid",name="faflowid",defaultValue = "0") String faflowid,
                                    @Parameter(name = "currentDSPid", description = "currentDSPid")@RequestParam(value ="currentDSPid",name="currentDSPid",defaultValue = "0") String currentDSPid,
                                    @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                    @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit) throws Exception {

        Map map =new HashMap();
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        //pageBean = service.findByDasource(pageNumber, pageBean.getPageSize(), faflowid);
        IPage<BiDatasource> a = new Page<>(pageNumber,limit);
        QueryWrapper<BiDatasource> wrapper=new QueryWrapper<>();
        wrapper.eq("FATHERID",faflowid);
        wrapper.isNotNull("ISLEAF");
        wrapper.orderByDesc("DSID");
        iBiDatasourceService.page(a,wrapper);//分页
        map.put("faflowid", faflowid);
        map.put("pageBean", a);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-选择数据源左侧菜单
     *
     * @return
     */
    @Operation(summary = "ds_data_left")
    @GetMapping(value = "/zbjk/ds_data_left")
    public JsonBean ds_data_left(@Parameter(name = "orgid", description = "orgid")@RequestParam(value ="orgid")String orgid,
                                 @Parameter(name = "hbOrgEntity", description = "hbOrgEntity")@RequestBody() Organization hbOrgEntity) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String treeName = "";
         Organization organization = hbOrgEntity;
        if (StringUtils.isEmpty(orgid)) {
            orgid = organization.getOrgid().toString();
        }
        Organization o = treeService.belongToCompany(orgid);
        orgid = o.getOrgid().toString();
        treeName = o.getOrgname();
        String tree = iBiDatasourceService.getTree(orgid);
        Map map = new HashMap();
        map.put("tree", tree);
        map.put("orgid", orgid);
        map.put("treeName", treeName);
        return new JsonBean(200,"成功",map);
    }


    /**
     * 指标 查询左侧
     * @return
     */
    @Operation(summary = "搜索")
    @GetMapping(value = "/zbjk/search_left")
    public JsonBean search_left() throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String tree = iOrganizationService.getOrgTree("kri_info_seach");
        Map map = new HashMap(2);
        map.put("tree", tree);
        map.put("targetFrame", "mainFramex");
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-左边tree
     *
     * @param
     * @return
     */
    @Operation(summary = "monitor_left")
    @GetMapping(value = "/zbjk/monitor_left")
    public JsonBean monitor_left(@Parameter(name = "Orgid", description = "Orgid") @RequestParam("Orgid")String Orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap(2);
        map.put("targetFrame", "mainFramex");
        map.put("orgid", Orgid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-添加跳转
     *
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "to_solution_add")
    @GetMapping(value = "/zbjk/to_solution_add")
    public JsonBean to_solution_add(@Parameter(name = "Staff", description = "Staff") @RequestBody()Staff Staff,
                                    @Parameter(name = "Orgid", description = "Orgid") @RequestParam("Orgid")BigDecimal orgId,
                                    @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("orgId", orgId);
        map.put("tblStaff", Staff);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-保存方案
     *
     * @param orgId
     * @param
     * @param
     * @return
     */
    @Operation(summary = "solution_save")
    @GetMapping(value = "/zbjk/solution_save")
    public JsonBean solution_save(@Parameter(name = "Orgid", description = "Orgid") @RequestParam("Orgid")BigDecimal orgId,
                                  @Parameter(name = "Staff", description = "Staff")@RequestBody()Staff Staff,
                                  @Parameter(name = "Staff", description = "Staff")@RequestBody()MonitorSolution so,
                                  @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
		if (loginstaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        LocalDateTime  ldt =  LocalDateTime.now();
        so.setCreatedate(ldt);
        so.setRunstatus(new BigDecimal("0"));
        so.setType(new BigDecimal("2"));
        iMonitorSolutionService.updateById(so);
        iOrganizationService.updateById(iOrganizationService.getById(Staff.getOrgid()));
        iStaffService.updateById(Staff);
        map.put("orgid", orgId);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch",choiceSearch);
        return new JsonBean(200,"成功",map);
    }
    /**
     * 指标预警-添加指标
     *
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "ruleslistSelector_zb")
    @GetMapping(value = "/zbjk/ruleslistSelector")
    public JsonBean ruleslistSelector_zb(@Parameter(name = "orgId", description = "orgId") @RequestParam("orgId")BigDecimal orgId,
                                         @Parameter(name = "Staff", description = "Staff")  @RequestBody()Staff Staff,
                                         @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",name = "pageNumber",defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "limit", description = "limit") @RequestParam("limit")Integer limit,
                                         @Parameter(name = "solutionid", description = "solutionid") @RequestParam("solutionid")String  solutionid,
                                         @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        IPage<Indicator> a = new Page<>(pageNumber,limit);
        QueryWrapper<Indicator> wrapper=new QueryWrapper<>();
        wrapper.eq("ORGID",orgId);
        map.put("orgid", orgId);
        map.put("tblStaff", Staff);
        map.put("solutionid", solutionid);
        map.put("pageBean", iIndicatorService.page(a,wrapper));
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-保存指标
     *
     * @param
     * @return
     */
    @Operation(summary = "新增")
    @GetMapping(value = "/zbjk/add_indicator")
    public JsonBean add_indicator_so(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam("indicatorid")String indicatorid,
                                     @Parameter(name = "souceid", description = "souceid") @RequestParam("souceid")String souceid) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (indicatorid != null && souceid != null) {
            Indicator tblIndicator = iIndicatorService.getById(indicatorid);
            MonitorSolution monitorSolution = iMonitorSolutionService.getById(souceid);
            List<Indicator> list = iMonitorSolutionService.getMonitorSolutionIndicators();
            boolean fal = true;
            if (list != null && list.size() > 0) {
                for (Indicator indicator : list) {
                    if (indicator.getIndicatorid().toString().equals(tblIndicator.getIndicatorid().toString())) {
                        fal = false;
                    }
                }
            }
            if (fal) {
                iIndicatorService.save(tblIndicator);
                iMonitorSolutionService.updateById(monitorSolution);
            }
        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标预警-判断修改权限
     *
     * @param
     * @return
     */
    @Operation(summary = "solutionchechUpate_zb")
    @GetMapping(value = "/zbjk/solutioncheckUpdate", produces = "application/json; charset=utf-8")
    public JsonBean solutionchechUpate_zb(@Parameter(name = "Staffid", description = "Staffid") @RequestParam("Staffid")String Staffid,
                                          @Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid")String selectedid) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution rule = this.iMonitorSolutionService.getById(selectedid);
        if (rule.getStaffid().equals(Staffid)) {
            if (rule.getRunstatus().toString().equals("0")) {
                return new JsonBean(200,"成功","0");
            } else {
                return new JsonBean(200,"该预警方案已执行，不能删除和修改","1");
            }
        } else {
            return new JsonBean(200,"权限不足","0");
        }

    }

    /**
     * 指标预警-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "solutionDel_zn")
    @GetMapping(value = "/zbjk/solution_del")
    public JsonBean solutionDel_zn(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid")String  selectedid,
                                   @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch")String choiceSearch) throws Exception {
        Map map = new HashMap();
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (selectedid != null) {
            MonitorSolution monitorSolution = this.iMonitorSolutionService.getById(selectedid);
            //为页面查找区域显隐藏赋值
            if(choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch );
            map.put("orgId", monitorSolution.getOrgid());
           // Wrapper<monitorSolution>  aa = new  Wrapper<monitorSolution>;
            iMonitorSolutionService.removeById(selectedid);
        }
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-删除指标
     *
     * @param
     * @return
     */
    @Operation(summary = "删除")
    @GetMapping(value = "/zbjk/del_indicator")
    public JsonBean del_indicator_so(@Parameter(name = "indicatorid", description = "indicatorid") @RequestParam("indicatorid")String indicatorid,
                                     @Parameter(name = "souceid", description = "souceid") @RequestParam("souceid")String souceid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (indicatorid != null && souceid != null) {
            Indicator tblIndicator = iIndicatorService.getById(indicatorid);
            MonitorSolution monitorSolution = iMonitorSolutionService.getById(souceid);
            iIndicatorService.removeById(indicatorid);
            iMonitorSolutionService.updateById(monitorSolution);
        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标预警-跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "toSolutionModify_zb")
    @GetMapping(value = "/zbjk/to_solution_modify")
    public JsonBean toSolutionModify_zb(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid")String selectedid,
                                        @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        List<Indicator> set = iMonitorSolutionService.getMonitorSolutionIndicators();
        StringBuffer buffer = new StringBuffer();
        Map map = new HashMap();
        if (buffer.toString().length() > 0) {
            map.put("solruleids", buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        if (solution != null) {
            map.put("solution", solution);
            map.put("orgId", solution.getOrgid());
        }
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标预警-查看
     *
     * @param
     * @return
     */
    @Operation(summary = "to_solution_disp")
    @GetMapping(value = "/zbjk/to_solution_disp")
    public JsonBean to_solution_disp(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid")String selectedid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        List<Indicator> set = iMonitorSolutionService.getMonitorSolutionIndicators();
        Map map = new HashMap();
        StringBuffer buffer = new StringBuffer();
        if (buffer.toString().length() > 0) {
            map.put("solruleids", buffer.toString().substring(0, buffer.toString().length() - 1));
        }
        if (solution != null) {
            map.put("solution", solution);
            map.put("orgId", solution.getOrgid());
        }
        return new JsonBean(200,"成功",map);
    }


    @Operation(summary = "fxsbsave")
    @GetMapping(value = "/zbjk/save")
    public JsonBean fxsbsave(@Parameter(name = "worksheetid", description = "worksheetid") @RequestParam(value = "worksheetid",required = false)String worksheetid,
                             @Parameter(name = "Orgid", description = "Orgid") @RequestParam(value = "Orgid",required = false)String Orgid,
                             @Parameter(name = "dest", description = "dest") @RequestParam(value = "dest",required = false)String dest,
                             @Parameter(name = "path", description = "path") @RequestParam(value = "path",required = false)String path
                             ) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String name = LocalDateTime.now().toString() + ".xls";
        String file = path + "/" + name;
        String rFile = "/data/excel/" + name;
        int attid = 91500;
        Attachment a = iAttachmentService.getById("" + attid);
        Map map = new HashMap();
        if (dest != null) {
            if (dest.equals("dg")) {
            } else if (dest.equals("dgfj")) {
                Worksheet work = iWorksheetService.getById(worksheetid);
                map.put("worksheet", work);
            } else if (dest.equals("yd")) {
            } else if (dest.equals("qx")) {
                //选则的机构
                QueryWrapper  qw = new QueryWrapper();
                qw.eq("Orgid",Orgid);
                List<BugCriterion> list1 = iBugCriterionService.list(qw);
                map.put("list", list1);
            } else if (dest.equals("wt")) {
            }
        }
        map.put("a", a);
        return new JsonBean(200,"成功",map);
    }

    /**
     * worksheet 列表
     * @param page
     * @return
     */
    @Operation(summary = "kriseachxzj")
    @GetMapping(value = "/zbjk/xzdgh")
    public JsonBean kriseachxzj( @Parameter(name = "page", description = "page") @RequestParam(value = "page")String page) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String,Object> map = new HashMap<>();
        QueryWrapper<Worksheet> qw = new QueryWrapper();
        qw.eq("WORKSHEETBYSYSTEM","nk%");
        List<Worksheet> list = iWorksheetService.list(qw);
        map.put("list", list);
        map.put("page", page);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 工程编号
     * @param page
     * @param selectProjectid
     * @return
     */
    @Operation(summary = "kriseach")
    @GetMapping(value = "/zbjk/xzdg")
    public JsonBean kriseach( @Parameter(name = "page", description = "page") @RequestParam(value = "page")String page,
                              @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("page",page);
        map.put("worksheetid",selectProjectid);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-列表
     */
    @Operation(summary = "kri_infoZbjk")
    @GetMapping(value = "/zbjk/kri_info")
    public JsonBean kri_infoZbjk(@Parameter(name = "incid", description = "incid") @RequestParam(value = "incid")String  incid,
                                 @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId")String orgId,
                                 @Parameter(name = "kricode", description = "kricode") @RequestParam(value = "kricode")String kricode,
                                 @Parameter(name = "kriname", description = "kriname") @RequestParam(value = "kriname")String kriname,
                                 @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",defaultValue = "1")Integer pageNumber,
                                 @Parameter(name = "hbOrgEntityOrgid", description = "hbOrgEntityOrgid") @RequestParam(value = "")String hbOrgEntityOrgid,
                                 @Parameter(name = "hbOrgNameOrgid", description = "hbOrgNameOrgid") @RequestParam(value = "hbOrgNameOrgid")String hbOrgNameOrgid,
                                 @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff,
                                 @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch
                                 ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
		if (loginstaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (incid != null && !"".equals(incid)) {
            Indicator indicator = iIndicatorService.getById(incid);
            if (indicator.getIndicatorstatus().equals("启用")) {
                indicator.setIndicatorstatus("禁用");
            } else {
                indicator.setIndicatorstatus("启用");
            }
        }
        Map map = new HashMap();
        Boolean isSelect = false;
        IPage ip = new Page();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("INDICATORNAME","'%"+kriname+"%'");
        qw.eq("INDICATORCODE",kricode);
        qw.eq("indicatordb",Indicator.IS_HY0);
        qw.orderByDesc("createDate","INDICATORID");
        if(hbOrgEntityOrgid.equals(hbOrgNameOrgid)){
            qw.eq("ORGID",orgId);
        }else{
            if (StringUtils.isBlank(orgId)) {
                qw.eq("ORGID",hbOrgNameOrgid);
            }
        }
        iIndicatorService.page(ip,qw);
        map.put("orgid", orgId);
        map.put("tblStaff", staff);
        map.put("isSelect", isSelect);
        map.put("kricode", kricode);
        map.put("kriname", kriname);
        map.put("pageBean", ip);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

//    复查到此处 ---->
    /**
     * 指标管理 --结果
     *
     * @author SongXiangYing
     * @date 2016年1月31日 上午1:58:40
     * @param pageNumber
     * @param
     * @return
     */
    @Operation(summary = "kri_info_reult")
    @GetMapping(value = "/zbjk/kri_info_result")
    public JsonBean kri_info_reult(@Parameter(name = "id", description = "id") @RequestParam(value = "id")BigDecimal id,
                                   @Parameter(name = "execId", description = "execId") @RequestParam(value = "execId")String execId,
                                   @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber")Integer pageNumber,
                                   @Parameter(name = "limit", description = "limit") @RequestParam(value = "limit")Integer limit,
                                   @Parameter(name = "source", description = "source") @RequestParam(value = "source")Integer source,
                                   @Parameter(name = "mid", description = "mid") @RequestParam(value = "mid")String mid,
                                   @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId")String orgId,
                                   @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value = "fhtype")String fhtype,
                                   @Parameter(name = "orgId", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {

    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Indicator ind = iIndicatorService.getById(id.toString());

        if (StringUtils.isNotBlank(mid)) {
            mid = "1";
        }
        Map map = new HashMap();
        // 指标
        String lastId = null;
//        if (StringUtils.isNotBlank(mid)) {
//            if (source.equals(TblmonitorIndicatorResult.YJYJ)) {
//                lastId = this.tblWarningResultService.getLast();
//            }
//            pageBean = this.tblmonitorIndicatorResultService.getByYJ(id, source, lastId, pageNumber,
//                    pageBean.getPageSize());
//        } else {
//            // 指标方案
//            pageBean = this.tblmonitorIndicatorResultService.getResultList(id, execId, pageNumber,
//                    pageBean.getPageSize());
//        }
//
//        map.put("pageBean", pageBean);
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
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    // }

    /**
     * 指标预警 --结果
     *
     * @author SongXiangYing
     * @date 2016年1月31日 上午2:18:03
     * @param pageNumber
     * @param
     * @return
     */
    @Operation(summary = "kri_info_yj_reult")
    @GetMapping(value = "/zbjk/kri_yj_info_result")
    public JsonBean kri_info_yj_reult(@Parameter(name = "id", description = "id") @RequestParam(value = "id")BigDecimal id,
                                      @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId")String orgId,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber")Integer pageNumber,
                                      @Parameter(name = "limit", description = "limit") @RequestParam(value = "limit") Integer limit,
                                      @Parameter(name = "source", description = "source") @RequestParam(value = "source")String source,
                                      @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value = "fhtype") String fhtype,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
//        map.put("pageBean", pageBean);
        map.put("id", id);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("source", source);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-添加
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_addZbjk")
    @GetMapping(value = "/zbjk/kri_info_add")
    public JsonBean kri_info_addZbjk(@Parameter(name = "staff", description = "staff") @RequestBody()Staff staff,
                                     @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        TblStaffUtil loginstaff = userProvider.get();
		if (loginstaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
//        String orgid = staff.getTblOrganization().getOrgid().toString();
//        Organization og = iOrganizationService.getById(orgid);
          Map map = new HashMap();
//        map.put("creater", staff.getUsername());
//        map.put("creatTime", sf.format(new Date()));
//        map.put("orgname", og.getOrgname());
//        map.put("orgid", orgid);
         map.put("staffid", staff.getStaffid());
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-指标基准阈值列表
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_range_mngZbjk")
    @GetMapping(value = "/zbjk/kri_info_range_mng")
    public JsonBean kri_info_range_mngZbjk(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                           @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId")String orgId,
                                           @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
        List<Indicatorthreshold> hold = indicatorthresholdService.QueryByIndicatorId(selectProjectid);
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("selectProjectid", selectProjectid);
        map.put("hold", hold);
        map.put("orgId", orgId);
        map.put("tableHistoryRows", hold.size());
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-指标基准阈值列表-查看
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_range_mng_disp")
    @GetMapping(value = "/zbjk/kri_info_range_mng_disp")
    public JsonBean kri_info_range_mng_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                            @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId")String orgId,
                                            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
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
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功","0");
    }

    /**
     * 验证指标是否被预警使用
     *
     * @param
     * @return
     */
    @Operation(summary = "kzjz_info_is")
    @GetMapping(value = "/zbjk/kzjz_info_is")
    public JsonBean kzjz_info_is( @Parameter(name = "incid", description = "incid") @RequestParam(value = "incid")String incid) throws Exception {
        int num = 0;
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (incid != null && !"".equals(incid)) {
            Indicator in = iIndicatorService.getById(incid);
//            Set<TblMonitorSolution> monitorSolutions = in.getTblIndicatorsMonitorSolutions();
//            num = monitorSolutions.size();
//            if (in != null && in.getRunstatus().toString().equals("2")) {
//                num = 2;
//            }
        }
        return new JsonBean(200,"成功",num + "");
    }

    /**
     * 指标管理-计算公式
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_formulaZbjk")
    @GetMapping(value = "/zbjk/kri_info_formula")
    public JsonBean kri_info_formulaZbjk(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String  selectProjectid,
                                         @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
        Map map = new HashMap();
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Indicator indicator = null;
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
        }
        map.put("selectProjectid", selectProjectid);
        map.put("indicator", indicator);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-计算公式_查看
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_formula_disp")
    @GetMapping(value = "/zbjk/kri_info_formula_disp")
    public JsonBean kri_info_formula_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid) throws Exception {
        Map map = new HashMap();
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Indicator indicator = null;
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
        }
        map.put("selectProjectid", selectProjectid);
        map.put("indicator", indicator);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-保存计算公式
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_formula_save")
    @GetMapping(value = "/zbjk/kri_info_formula_save")
    public JsonBean kri_info_formula_save(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                          @Parameter(name = "formula", description = "formula") @RequestParam(value = "formula")String formula,
                                          @Parameter(name = "expressiondesc", description = "expressiondesc") @RequestParam(value = "expressiondesc") String expressiondesc,
                                          @Parameter(name = "expression", description = "expression") @RequestParam(value = "expression")String expression,
                                          @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
        Indicator indicator = null;
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
            indicator.setFormula(formula);
            indicator.setForlumachs(expression);
            indicator.setFormulades(expressiondesc);
            iIndicatorService.updateById(indicator);
        }
        return kri_info_formulaZbjk(selectProjectid,choiceSearch);
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
    public JsonBean kri_info_modZbjk_update( @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                             @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
        Indicator indicator = null;
        String flows = "";
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
//            Set<TblFlow> fw = indicator.getTblIndicatorFlows();
//            for (TblFlow tblFlow : fw) {
//                flows += tblFlow.getFlowid() + ",";
//            }
        }
//        Staff user = userService.findById(indicator.getStaffid().toString());
        Map map = new HashMap();
        map.put("indicator", indicator);
        map.put("flows", flows);
   //     map.put("creater", user.getUsername());
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
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
    public JsonBean kri_info_disp(@Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid) throws Exception{
        Indicator indicator = null;
        String flows = "";
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        if (selectProjectid != null && !"".equals(selectProjectid)) {
            indicator = iIndicatorService.getById(selectProjectid);
//            Set<TblFlow> fw = indicator.getTblIndicatorFlows();
//            for (TblFlow tblFlow : fw) {
//                flows += tblFlow.getFlowid() + ",";
//            }
        }
     //   TblStaff user = userService.findById(indicator.getStaffid().toString());
        Map map = new HashMap();
        map.put("indicator", indicator);
        map.put("flows", flows);
    //    map.put("creater", user.getUsername());
        return new JsonBean(200,"成功",map);
    }

    /**
     * 指标管理-阈值删除
     *
     * @param
     * @return
     */
    @Operation(summary = "删除")
    @GetMapping(value = "zbjk/delete_zbglj_del")
    public JsonBean delete_zbglj_del(@Parameter(name = "thresholdid", description = "thresholdid")@RequestParam(value = "thresholdid") String thresholdid) throws Exception {
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (thresholdid != null && !"".equals(thresholdid)) {
            Indicatorthreshold hold = indicatorthresholdService.getById(thresholdid);
            indicatorthresholdService.removeById(hold);
        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标管理-添加指标基本值域
     *
     * @param
     * @return
     */
    @Operation(summary = "新增")
    @PostMapping(value = "/zbjk/kri_info_range_save", produces = "application/json; charset=utf-8")
    public JsonBean add_zbzy(@Parameter(name = "selectProjectid", description = "selectProjectid")@RequestParam(value = "selectProjectid")String selectProjectid,
                             @Parameter(name = "sequencenumber", description = "sequencenumber")@RequestParam(value = "sequencenumber")String sequencenumber,
                             @Parameter(name = "tolerance", description = "tolerance")@RequestParam(value = "tolerance")String tolerance,
                             @Parameter(name = "thresholdname", description = "thresholdname")@RequestParam(value = "thresholdname")String thresholdname,
                             @Parameter(name = "tolerancelower", description = "tolerancelower")@RequestParam(value = "tolerancelower")String tolerancelower,
                             @Parameter(name = "toleranceupper", description = "toleranceupper")@RequestParam(value = "toleranceupper")String toleranceupper,
                             @Parameter(name = "regionvalue", description = "regionvalue")@RequestParam(value = "regionvalue")String regionvalue,
                             @Parameter(name = "prewarningmethod", description = "prewarningmethod")@RequestParam(value = "prewarningmethod")String prewarningmethod) throws Exception {
    	
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
      //  hold.setTblIndicator(in);
        indicatorthresholdService.save(hold);
        return new JsonBean(200,"成功","");
    }





    /**
     * 指标管理-添加指标基本值域
     *
     * @param
     * @return
     */
    @Operation(summary = "hy_kri_info_range_save")
    @GetMapping(value = "/zbjk/hy_kri_info_range_save", produces = "application/json; charset=utf-8")
    public JsonBean hy_kri_info_range_save(@Parameter(name = "selectProjectid", description = "selectProjectid")@RequestParam(value = "selectProjectid")String selectProjectid,
                                           @Parameter(name = "sequencenumber", description = "sequencenumber")@RequestParam(value = "sequencenumber")String sequencenumber,
                                           @Parameter(name = "tolerance", description = "tolerance")@RequestParam(value = "tolerance")String tolerance,
                                           @Parameter(name = "thresholdname", description = "thresholdname")@RequestParam(value = "thresholdname")String thresholdname,
                                           @Parameter(name = "tolerancelower", description = "tolerancelower")@RequestParam(value = "tolerancelower")String tolerancelower,
                                           @Parameter(name = "toleranceupper", description = "toleranceupper")@RequestParam(value = "toleranceupper")String toleranceupper,
                                           @Parameter(name = "regionvalue", description = "regionvalue")@RequestParam(value = "regionvalue")String regionvalue,
                                           @Parameter(name = "prewarningmethod", description = "prewarningmethod")@RequestParam(value = "prewarningmethod")String prewarningmethod) throws Exception {
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
      //  hold.setTblIndicator(in);
        indicatorthresholdService.save(hold);
        return new JsonBean(200,"成功","");
    }


    /**
     * 指标管理-添加流程
     *
     * @param
     * @return
     */
    @SuppressWarnings({ "unchecked" })
    @Operation(summary = "新增")
    @GetMapping(value = "/zbjk/add_incflows")
    public JsonBean add_incflows(@Parameter(name = "flowid", description = "flowid")@RequestParam(value = "flowid")String flowid,
                                 @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid) throws Exception {
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (flowid != null && indicatorid != null) {
            QueryWrapper<IndicatorFlow> queryWrapper = new QueryWrapper();
            queryWrapper.eq("FLOWID",flowid);
            queryWrapper.eq("INDICATORID",indicatorid);
            List<IndicatorFlow> list = iIndicatorFlowService.list(queryWrapper);
            if (list == null || list.size() == 0) {
//                TblFlow flow = tblFlowService.findById(flowid);
//                TblIndicator in = new TblIndicator();
//                in.setIndicatorid(new BigDecimal(indicatorid));
//                flow.getTblIndicators().add(in);
//                tblFlowService.modify(flow);
            }

        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标管理-添加风险事件
     *
     * @param
     * @return
     */
    @Operation(summary = "新增")
    @GetMapping(value = "/zbjk/add_riskevent")
    public JsonBean add_riskevent(@Parameter(name = "flriskeventidwid", description = "riskeventid")@RequestParam(value = "riskeventid")String riskeventid,
                                  @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid) throws Exception {
    	
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (riskeventid != null && indicatorid != null) {
//            TblRiskeventService service = (TblRiskeventService) SpringContextHolder.getBean("TblRiskeventService");
//            List<TblIndicatorRiskevent> list = tblIndicatorRiskeventService.findByflowid(riskeventid, indicatorid);
//            if (list == null || list.size() == 0) {
//                TblRiskevent en = service.findById(riskeventid);
//                Indicator in = new Indicator();
//                in.setIndicatorid(new BigDecimal(indicatorid));
//                en.getTblIndicators().add(in);
//                service.modify(en);
//            }
        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标管理-风险事件删除
     *
     * @param
     * @return
     */
    @Operation(summary = "inc_riskevent_del")
    @GetMapping(value = "/zbjk/inc_riskevent_del")
    public JsonBean inc_riskevent_del(@Parameter(name = "riskenevt", description = "riskenevt")@RequestParam(value = "riskenevt")String riskenevt,
                                      @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid,
                                      @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (riskenevt != null && indicatorid != null) {
            IndicatorRiskevent ri = new IndicatorRiskevent();
            ri.setIndicatorid(new BigDecimal(indicatorid));
            ri.setRiseveid(new BigDecimal(riskenevt));
            iIndicatorRiskeventService.removeById(ri);
        }
        return kri_info_modZbjk_update(selectProjectid,choiceSearch);
    }

    /**
     * 指标管理-流程删除
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_info_modZbjk_updates")
    @GetMapping(value = "/zbjk/inc_flow_del")
    public JsonBean kri_info_modZbjk_updates(@Parameter(name = "flowid", description = "flowid")@RequestParam(value = "flowid")String flowid,
                                             @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid,
                                             @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                             @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
    	
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (flowid != null && indicatorid != null) {
            IndicatorFlow flo = new IndicatorFlow();
            flo.setFlowid(new BigDecimal(flowid));
            flo.setIndicatorid(new BigDecimal(indicatorid));
            iIndicatorFlowService.removeById(flo);
        }
        return kri_info_modZbjk_update(selectProjectid,choiceSearch);
    }

    /**
     * 指标管理-添加控制措施
     *
     * @param
     * @return
     */
    @SuppressWarnings("unchecked")
    @Operation(summary = "新增")
    @GetMapping(value = "/zbjk/add_conma")
    public JsonBean add_conma(@Parameter(name = "conid", description = "conid")@RequestParam(value = "conid")String conid,
                              @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid) throws Exception {
    	
    	 TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (conid != null && indicatorid != null) {
//            TblControlMatrixService service = (TblControlMatrixService) SpringContextHolder
//                    .getBean("TblControlMatrixService");
//            QueryWrapper<IndicatorCm> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("CONMATID",conid);
//            queryWrapper.eq("INDICATORID",indicatorid);
//            List<IndicatorCm> list = iIndicatorCmService.list(queryWrapper);
//            if (list == null || list.size() == 0) {
//                TblControlmatrix en = service.findById(conid);
//                Indicator in = new Indicator();
//                in.setIndicatorid(new BigDecimal(indicatorid));
//                en.getTblIndicators().add(in);
//                service.modify(en);
//            }
        }
        return new JsonBean(200,"成功","");
    }

    /**
     * 指标管理-删除控制措施
     *
     * @param
     * @return
     */
    @Operation(summary = "inc_mon_del")
    @GetMapping(value = "/zbjk/inc_mon_del")
    public JsonBean inc_mon_del(@Parameter(name = "conid", description = "conid")@RequestParam(value = "conid")String conid,
                                @Parameter(name = "indicatorid", description = "indicatorid")@RequestParam(value = "indicatorid")String indicatorid,
                                @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(value = "selectProjectid")String selectProjectid,
                                @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (conid != null && indicatorid != null) {
            IndicatorCm flo = new IndicatorCm();
            flo.setConmatid(new BigDecimal(conid));
            flo.setIndicatorid(new BigDecimal(indicatorid));
            iIndicatorCmService.removeById(flo);
        }
        return kri_info_modZbjk_update(selectProjectid,choiceSearch);
    }

    /**
     * 指标管理-删除
     *
     * @param
     * @return
     */
//    @SuppressWarnings("unchecked")
//    @GetMapping(value = "/zbjk/kri_info_del")
//    @Operation(summary = "指标管理-删除")
//    public JsonBean kri_info_modZbjk_del(@Parameter(name = "selectProjectid", description = "selectProjectid")@RequestParam(value = "selectProjectid")String selectProjectid) {
//        if (selectProjectid != null && !"".equals(selectProjectid)) {
//            Indicator in = iIndicatorService.getById(selectProjectid);
//            Set<TblFlow> flows = in.getTblIndicatorFlows();
//            for (TblFlow tblFlow : flows) {
//                tblFlowService.deleteBy(tblFlow);
//            }
//            Set<TblControlmatrix> cons = in.getTblIndicatorMatrixes();
//            for (TblControlmatrix con : cons) {
//                TblControlMatrixService conservice = (TblControlMatrixService) SpringContextHolder
//                        .getBean("TblControlMatrixService");
//                conservice.deleteCon(con);
//            }
//            Set<Riskevent> risks = in.getTblIndicatorRiskvents();
//            for (TblRiskevent en : risks) {
//                TblRiskeventService eservice = (TblRiskeventService) SpringContextHolder.getBean("TblRiskeventService");
//                eservice.delete(en.getRiseveid().toString());
//            }
//            iIndicatorService.removeById(selectProjectid);
//        }
//        return kri_infoZbjk();
//    }

    /**
     * 指标管理-保存
     *
     * @return
     */
    @Operation(summary = "kri_info_modZbjk")
    @GetMapping(value = "/zbjk/kri_info_mod")
    public JsonBean kri_info_modZbjk(@Parameter(name = "createdatea", description = "createdatea")@RequestParam(value = "createdatea")String createdatea,
                                     @Parameter(name = "indicatordes", description = "indicatordes")@RequestParam(value = "indicatordes")String indicatordes,
                                     @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(value = "choiceSearch")String choiceSearch,
                                     @Parameter(name = "indicator", description = "indicator")@RequestBody()Indicator indicator) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        indicatordes=indicatordes.trim();
        if (createdatea != null && !"".equals(createdatea)) {
            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
            indicator.setCreatedate( LocalDateTime.parse(createdatea));
        } else {
            indicator.setCreatedate(LocalDateTime.now());
        }
        if (indicator.getIndicatorid() == null) {
           // indicator.setIndicatorsystem(null);
            indicator.setRunstatus(new BigDecimal(0));
        }
        indicator.setIndicatordb(Indicator.IS_HY0);
        indicator.setIndicatordes(indicatordes!=null?indicatordes:null);
        iIndicatorService.save(indicator);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        Map  map = new HashMap();
        map.put("selectProjectid",indicator.getIndicatorid());
        map.put("choiceSearch",choiceSearch);

        return new JsonBean(200,"成功",map);
    }

//    @GetMapping(value = "/zbjk/kri_info_seach")
//    @Operation(summary = "kri_info_seach")
//    public JsonBean kri_info_seachZbjk(@Parameter(name = "page", description = "page")@RequestParam(value = "page")String page,
//                                       @Parameter(name = "pager", description = "pager")@RequestBody() ListPager pager) {
//        if (page == null || pager == null) {
//            List list = iIndicatorService.list();
//            pager = new ListPager(list);
//            request.getSession().setAttribute("dpPager", pager);
//        } else {
//            pager.navigate(page);
//        }
//        Map map = new HashMap(1);
//        map.put("pager", pager);
//        return new JsonBean(200,"成功",map);
//    }

    /**
     * 指标预警-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "kri_monitor_infoZbjk")
    @GetMapping(value = "/zbjk/kri_monitor_info")
    public JsonBean kri_monitor_infoZbjk(@Parameter(name = "solutioncode", description = "solutioncode")@RequestParam(value = "solutioncode")String solutioncode,
                                         @Parameter(name = "solutionname", description = "solutionname")@RequestParam(value = "solutionname")String solutionname,
                                         @Parameter(name = "BigDecimal", description = "BigDecimal")@RequestParam(value = "BigDecimal")BigDecimal orgId,
                                         @Parameter(name = "pageNumber", description = "pageNumber")@RequestParam(value = "pageNumber")Integer pageNumber,
                                         @Parameter(name = "limit", description = "limit")@RequestParam(value = "limit")Integer limit,
                                         @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam(value = "choiceSearch")String choiceSearch,
                                         @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff
                                         ) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");//选则的机构
//        TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgName");//当前用户的机构
//        Boolean isSelect = false;
//        if(attribute.getOrgid().toString().equals(attribute1.getOrgid().toString())){
//            if (null == orgId) {
//
//                orgId = staff.getTblOrganization().getOrgid();
//            }
//            pageBean = tblMonitorSolutionService.findAllZb(orgId, solutioncode, solutionname, pageNumber,
//                    pageBean.getPageSize());
//
//        }else{
//            if (null == orgId) {
//                orgId = attribute.getOrgid();
//            }
//
//            pageBean = tblMonitorSolutionService.findAllZb(orgId, solutioncode, solutionname, pageNumber,
//                    pageBean.getPageSize());
//
//        }
//        map.put("orgId", orgId);
//        map.put("pageBean", pageBean);
        map.put("tblStaff", staff);
//       map.put("isSelect", isSelect);
        map.put("solutioncode", solutioncode);
        map.put("solutionname", solutionname);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }


    /**
     * 指标预警-tree
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "zbyjfindOrganizationByTree")
    @GetMapping(value = "/zbyj/findOrganizationByTreeAll", produces = "application/json; charset=utf-8")
    public JsonBean zbyjfindOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId")@RequestParam(value = "nodeId")BigDecimal nodeId,
                                               @Parameter(name = "type", description = "type")@RequestParam(value = "type")String type,
                                               @Parameter(name = "orgId", description = "orgId")@RequestParam(value = "orgId")BigDecimal orgId) throws Exception {
        String json = "";
        TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (null == nodeId) {
            nodeId = orgId;
        }
        if (StringUtils.isNotBlank(type)) {
          //  List<Tree> list = this.iOrganizationService.getTree(nodeId);

         //   json = JSONObject.toJSONString(list);
        } else {
          //  List<Tree> list = this.iOrganizationService.getNodeAll(nodeId);

        }
        return new JsonBean(200,"成功",json);
    }




    @Operation(summary = "搜索")
    @GetMapping(value = "/zbjk/searchKri")
    public JsonBean searchKriZbjk(@Parameter(name = "kricode", description = "kricode")@RequestParam(value = "kricode")String kricode,
                                  @Parameter(name = "kriname", description = "kriname")@RequestParam(value = "kriname")String kriname,
                                  @Parameter(name = "krilevel", description = "krilevel")@RequestParam(value = "krilevel")String krilevel,
                                  @Parameter(name = "kristatus", description = "kristatus")@RequestParam(value = "kristatus")String kristatus,
                                  @Parameter(name = "createdtimeFrom", description = "createdtimeFrom")@RequestParam(value = "createdtimeFrom")String createdtimeFrom,
                                  @Parameter(name = "createdtimeTo", description = "createdtimeTo")@RequestParam(value = "createdtimeTo")String createdtimeTo,
                                  @Parameter(name = "auditingStatus", description = "auditingStatus")@RequestParam(value = "auditingStatus")String auditingStatus,
                                  @Parameter(name = "page", description = "page")@RequestParam(value = "page")String page
                                  ) throws Exception {
        Map map =new HashMap();
        TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
//        ListPager pager = (ListPager) request.getSession().getAttribute("dpPager");
//        if (page == null || pager == null) {
//            List list = iIndicatorService.search(kricode, kriname, krilevel, kristatus, createdtimeFrom, createdtimeTo,
//                    auditingStatus, "", "");
//            pager = new ListPager(list);
//            request.getSession().setAttribute("dpPager", pager);
//        } else {
//            pager.navigate(page);
//        }
//        map.put("pager", pager);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "搜索")
    @GetMapping(value = "/zbjk/searchKriInfo")
    public JsonBean searchKriInfoZbjk(@Parameter(name = "kricode", description = "kricode")@RequestParam(value = "kricode")String kricode,
                                      @Parameter(name = "kriname", description = "kriname")@RequestParam(value = "kriname")String kriname,
                                      @Parameter(name = "krilevel", description = "krilevel")@RequestParam(value = "krilevel")String krilevel,
                                      @Parameter(name = "kristatus", description = "kristatus")@RequestParam(value = "kristatus")String kristatus,
                                      @Parameter(name = "createdtimeFrom", description = "createdtimeFrom")@RequestParam(value = "createdtimeFrom")String createdtimeFrom,
                                      @Parameter(name = "createdtimeTo", description = "createdtimeTo")@RequestParam(value = "createdtimeTo")String createdtimeTo,
                                      @Parameter(name = "auditingStatus", description = "auditingStatus")@RequestParam(value = "auditingStatus")String auditingStatus,
                                      @Parameter(name = "page", description = "page")@RequestParam(value = "page")String page,
                                      @Parameter(name = "sort", description = "sort")@RequestParam(value = "sort")String sort,
                                      @Parameter(name = "sort_type", description = "sort_type")@RequestParam(value = "sort_type")String sort_type
                                      ) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
//        ListPager pager = (ListPager) request.getSession().getAttribute("dpPager");
//        if (page == null || pager == null) {
//            TblIndicatorService test = SpringContextHolder.getBean("TblIndicatorService");
//            List list = test.search(kricode, kriname, krilevel, kristatus, createdtimeFrom, createdtimeTo,
//                    auditingStatus, sort, sort_type);
//            pager = new ListPager(list);
//            request.getSession().setAttribute("dpPager", pager);
//        } else {
//            pager.navigate(page);
//        }
        Map map = new HashMap();
//        map.put("pager", pager);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "搜索")
    @GetMapping(value = "/zbjk/searchMonitor")
    public JsonBean searchMonitorZbjk(@Parameter(name = "kricode", description = "kricode")@RequestParam(value = "kricode")String kricode,
                                      @Parameter(name = "kriname", description = "kriname")@RequestParam(value = "kriname")String kriname,
                                      @Parameter(name = "krilevel", description = "krilevel")@RequestParam(value = "krilevel")String krilevel,
                                      @Parameter(name = "kristatus", description = "kristatus")@RequestParam(value = "kristatus")String kristatus,
                                      @Parameter(name = "createdtimeFrom", description = "createdtimeFrom")@RequestParam(value = "createdtimeFrom")String createdtimeFrom,
                                      @Parameter(name = "createdtimeTo", description = "createdtimeTo")@RequestParam(value = "createdtimeTo")String createdtimeTo,
                                      @Parameter(name = "auditingStatus", description = "auditingStatus")@RequestParam(value = "auditingStatus")String auditingStatus,
                                      @Parameter(name = "page", description = "page")@RequestParam(value = "page")String page,
                                      @Parameter(name = "sort", description = "sort")@RequestParam(value = "sort")String sort,
                                      @Parameter(name = "sort_type", description = "sort_type")@RequestParam(value = "sort_type")String sort_type
    ) throws Exception{
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
//        ListPager pager = (ListPager) request.getSession().getAttribute("dpPager");
//        if (page == null || pager == null) {
//            TblIndicatorService test = SpringContextHolder.getBean("TblIndicatorService");
//            List list = test.search(kricode, kriname, krilevel, kristatus, createdtimeFrom, createdtimeTo,
//                    auditingStatus, sort, sort_type);
//            pager = new ListPager(list);
//            request.getSession().setAttribute("dpPager", pager);
//        } else {
//            pager.navigate(page);
//        }
        Map map = new HashMap();
//        map.put("pager", pager);
        return new JsonBean(200,"成功",map);
    }
}
