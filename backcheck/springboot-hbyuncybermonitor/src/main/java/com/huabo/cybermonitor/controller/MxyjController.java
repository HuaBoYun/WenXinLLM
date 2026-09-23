package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.MonitorModelresult;
import com.huabo.cybermonitor.entity.MonitorModelsolution;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionModel;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.mapper.YhrPageMapper;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IIndicatorService;
import com.huabo.cybermonitor.service.IMonitorExeintervalService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelresultService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionIndicatorService;
import com.huabo.cybermonitor.service.IMonitorSolutionModelService;
import com.huabo.cybermonitor.service.IMonitorSolutionRuleService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionStaffService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.ConstClass;
import com.huabo.cybermonitor.util.DynamicDataSource;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




/**
 * 模型监控-模型预警api接口
 *
 * @createTime 2022/8/11
 */

@RestController
@Slf4j
@Tag(name="模型监控-模型预警",description="模型监控-模型预警")
@RequestMapping(value = "/cyber/MxyjController")
@SuppressWarnings("all")
public class MxyjController {

	private static final Logger log = LoggerFactory.getLogger(MxyjController.class);

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;

    @Autowired
    IAccbookService iAccbookService;

    @Autowired
    IMonitorModelresultService iMonitorModelresultService;

    @Autowired
    IWarningResultService iWarningResultService;

    @Autowired
    IMonitorSolutionModelService iMonitorSolutionModelService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    IStaffService iStaffService;
    @Autowired
    IMonitorSolutionStaffService iMonitorSolutionStaffService;



    @Autowired
    IIndicatorService iIndicatorService;

    @Autowired
    IMonitorExeintervalService iMonitorExeintervalService;

    @Autowired
    IMonitorSolutionRuleService iMonitorSolutionRuleService;



    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IBugCriterionService iBugCriterionService;

    @Autowired
    IMonitorSolutionIndicatorService  iMonitorSolutionIndicatorService;

    @Resource
    private UserProvider userProvider;


    /**
     * 模型预警-主页
     *
     * @param
     * @return
     */
    @Operation(summary = "modelssolutions_index_index")
    @GetMapping(value = "/mxjk/solutions_index")
    public JsonBean modelssolutions_index_index(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        try {
            map.put("orgId", staff.getOrgid());
        } catch (Exception e) {
            return new JsonBean(200, "没有token", null);
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警-左边
     *
     * @param
     * @return
     */
    @Operation(summary = "mxyj_left")
    @GetMapping(value = "/mxjk/mxyj_left")
    public JsonBean mxyj_left(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            String orgId = staff.getCurrentOrg().getOrgid().toString();
            Map map = new HashMap();
            map.put("orgid", orgId);
            map.put("targetFrame", "mainFramex");
            return new JsonBean(200, "成功", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "failure", "没有拿到token");
    }

    /**
     * 模型预警-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolutionmgmt")
    @GetMapping(value = "/mxjk/modelsolutionmgmt")
    public JsonBean modelsolutionmgmt(@Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value = "solutioncode", required = true) String solutioncode,
                                      @Parameter(name = "solutionname", description = "solutionname") @RequestParam(value = "solutionname", required = true) String solutionname,
                                      @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) String orgId,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            TblOrganizationUtil attribute = null;
            TblOrganizationUtil attribute1 = null;
            try {
                //选则的机构
                attribute = staff.getCurrentOrg();
                //当前用户的机构
                attribute1 = staff.getLinkOrg();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Map map = new LinkedHashMap();
            Boolean isSelect = false;

            String hbOrgEntityOrgid = staff.getCurrentOrg().getOrgid().toString(); //当前用户选择的组织hbOrgEntityOrgid
            String hbOrgNameOrgid = staff.getLinkOrg().getOrgid().toString();

            IPage<MonitorSolution> a = new Page(pageNumber, ConstClass.DEFAULT_SIZE);
            QueryWrapper<MonitorSolution> wrapper = new QueryWrapper<>();
            if (hbOrgEntityOrgid.equals(hbOrgNameOrgid)) {
                if (StringUtils.isBlank(orgId)) {
                    orgId = staff.getLinkDetp().getOrgid().toString();
                }
                wrapper.eq("type", 3);
                wrapper.eq("ORGID", new BigDecimal(orgId));
                if (StringUtils.isNotBlank(solutioncode)) {
                    wrapper.like("SOLUTIONCODE", solutioncode);
                }
                if (StringUtils.isNotBlank(solutionname)) {
                    wrapper.like("SOLUTIONNAME", solutionname);
                }


            } else {
                if (StringUtils.isBlank(orgId)) {
                    orgId = hbOrgEntityOrgid;
                }
                wrapper.eq("type", 3);
                wrapper.eq("ORGID", new BigDecimal(orgId));
                if (StringUtils.isNotBlank(solutioncode)) {
                    wrapper.like("SOLUTIONCODE", solutioncode);
                }
                if (StringUtils.isNotBlank(solutionname)) {
                    wrapper.like("SOLUTIONNAME", solutionname);
                }

            }
            iMonitorSolutionService.page(a, wrapper);

            // 加入 realName
            iStaffService.setRealName(a);

            map.put("orgId", orgId);
            map.put("solutioncode", solutioncode);
            map.put("solutionname", solutionname);
            map.put("pageBean", a);
            map.put("tblStaff", staff);


            return new JsonBean(200, "成功", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "failure", "没有拿到token");

    }




    /**
     * 模型管理-tree
     *
     * @param nodeId
     * @param type
     * @param
     * @return
     */
    @Operation(summary = "mxgl_findOrganizationByTree")
    @GetMapping(value = "/mxjk/findOrganizationByTreeAll")
    public JsonBean mxgl_findOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam("nodeId") BigDecimal nodeId,
                                                @Parameter(name = "type", description = "type") @RequestParam("type") String type,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            BigDecimal orgId = staff.getCurrentOrg().getOrgid();

            String json = "";
            if (null == nodeId) {
                nodeId = orgId;
            }
            //树问题
            if (StringUtils.isNotBlank(type)) {
                List<Tree> list = iOrganizationService.getNodeAll(nodeId);
                json = JSONObject.toJSONString(list);
            } else {
                List<Tree> list = iOrganizationService.getNodeAll(nodeId);

                json = JSONObject.toJSONString(list);

            }
            return new JsonBean(200, "成功", json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "failure", "没有拿到token");
    }

    /**
     * 模型预警-跳转添加
     *
     * @param
     * @return
     */
    @Operation(summary = "toModelSolutionAdd")
    @PostMapping(value = "/mxjk/to_modelsolution_add")
    public JsonBean toModelSolutionAdd(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = true) String orgId,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            String Orgid = staff.getCurrentOrg().getOrgid().toString();
            Map map = new HashMap(2);
            map.put("orgId", orgId);
            map.put("tblStaff", staff);
            return new JsonBean(200, "成功", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "failure", "没有拿到token");
    }

    /**
     * 模型预警-保存
     *
     * @param
     * @return
     * @throws ParseException
     */
    @Operation(summary = "modelSolutionAdd")
    @PostMapping(value = "/mxjk/modelsolution_add")
    public JsonBean modelSolutionAdd(
            @Parameter(name = "solution", description = "solution") @RequestBody() MonitorSolution solution,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        solution.setCreatedate(LocalDateTime.now());
        solution.setRunstatus(new BigDecimal(0));
        solution.setType(new BigDecimal(3));
        iMonitorSolutionService.save(solution);
        Map map = new HashMap();
        //为页面查找区域显隐藏赋值
        map.put("selectedid", solution.getSolutionid());
        map.put("orgId", solution.getOrgid());// solution.getOrganization().getOrgid() 老代码
        map.put("solution", solution);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警-跳转修改
     * 以及详情采用同一个方法
     *
     * @param
     * @return
     */
    @Operation(summary = "to_modelsolution_modify")
    @GetMapping(value = "/mxjk/to_modelsolution_modify")
    public JsonBean to_modelsolution_modify(@Parameter(name = "infoid", description = "infoid") @RequestParam("infoid") String infoid,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        MonitorSolution solution = iMonitorSolutionService.getById(infoid);
        QueryWrapper qw=new  QueryWrapper();
        qw.eq("SOLUTIONID",infoid);
        List<MonitorSolutionModel> list=iMonitorSolutionModelService.list(qw);
        List<MonitorModel> list3=new ArrayList<MonitorModel>();
        if(list.size()>0) {
            List<BigDecimal> list2 = list.stream().map(x -> x.getModelid()).collect(Collectors.toList());
            QueryWrapper<MonitorModel> qw1 = new QueryWrapper<MonitorModel>();
            qw1.in("modelid", list2);
            list3 = iMonitorModelService.list(qw1);
        }
        Map map = new HashMap();
        //map.put("orgId", orgId);
        map.put("viewType", infoid);
        if (solution != null) {
            map.put("solution", solution);
            map.put("models",list3);
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警-添加模型-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "modellistSelector")
    @PostMapping(value = "/mxjk/modellistSelector")
    public JsonBean modellistSelector(@Parameter(name="orgId",description="orgId") @RequestParam("orgId") String orgId,
                                      @Parameter(name = "modelid", description = "modelid") @RequestParam(value="modelid",required = false) String modelid,
                                      @Parameter(name="solutionid",description="solutionid") @RequestParam("solutionid") String solutionid,
                                      @Parameter(name="pageNumber",description="pageNumber") @RequestParam("pageNumber") Integer pageNumber,
                                      @Parameter(name="limit",description="limit",required=false) @RequestParam(value="limit",required = false) Integer limit,
                                      @Parameter(name = "modelids", description = "modelids") @RequestParam(value="modelids",required = false) Integer modelids,
                                      @Parameter(name = "OrgidOrgid", description = "OrgidOrgid") @RequestParam(value = "OrgidOrgid",required = false) String OrgidOrgid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            String Orgid = staff.getCurrentOrg().getOrgid().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (orgId == null) {
            orgId = OrgidOrgid;
        }
        IPage<MonitorModel> a = new Page<>(pageNumber, limit);
        QueryWrapper<MonitorModel> wrapper = new QueryWrapper<>();
        wrapper.eq("MODELSTATUS", "启用");
        wrapper.eq("SUBSYSTEM", 1);
        wrapper.eq("ORGID", orgId);
        if (modelid != null && !"".equals(modelid)) {
            wrapper.like("MODELID", modelid);
        }
        if (modelids != null && !"".equals(modelids)) {
            wrapper.notIn("MODELID", modelids);

        }
        wrapper.orderByAsc("MODELID");
        Map map = new HashMap();
        map.put("pageBean", iMonitorModelService.page(a, wrapper));
        map.put("orgId", orgId);
        map.put("modelid", modelid);
        map.put("solutionid", solutionid);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 模型预警-保存模型
     *
     * @param request
     * @return
     */
    @Operation(summary = "模型预警-添加模型列表-选定")
    @PostMapping(value = "/mxyj/add_model")
    public  JsonBean add_model(
            @Parameter(name="souceid",description="souceid") @RequestParam(value="souceid")String souceid,
            @Parameter(name="modelid",description="modelid") @RequestParam(value="modelid")String modelid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

		/*if (modelid != null && souceid != null) {
			TblMonitorSolution monitorSolution = tblMonitorSolutionService.findOne(souceid);
			TblMonitorModel model = tblMonitorModelService.findOne(modelid);
			Set<TblMonitorModel> models = monitorSolution.getTblMonitorSolutionModels();
			if (models != null && models.size() > 0) {
				for (TblMonitorModel tblMonitorModel : models) {
					if (tblMonitorModel.getModelid() != model.getModelid()) {
						monitorSolution.getTblMonitorSolutionModels().add(model);
					}
				}
			} else {
				monitorSolution.getTblMonitorSolutionModels().add(model);
			}
			tblMonitorSolutionService.modify(monitorSolution);
		}*/
        if (modelid != null && souceid != null) {
            String[] modelids = modelid.split(",");
            this.iMonitorSolutionService.addyjModel(modelids,souceid);
            return new JsonBean(200, "成功", null);
        }
        return new JsonBean(-1, "modelid,souceid传值有null值", null);
    }

    /**
     * 模型预警-修改
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolution_modify")
    @PostMapping(value = "/mxjk/modelsolution_modify")
    public JsonBean modelsolution_modify(@Parameter(name = "solutionid", description = "solutionid") @RequestParam("solutionid") String solutionid,
                                         @Parameter(name = "orgId", description = "orgId") @RequestParam(value="orgId",required = false) String orgId,
                                         @Parameter(name = "solutioncode", description = "solutioncode") @RequestParam(value="solutioncode",required = false) String solutioncode,
                                         @Parameter(name = "solutionname", description = "solutionname") @RequestParam("solutionname") String solutionname,
                                         @Parameter(name = "solutionstatus", description = "solutionstatus") @RequestParam(value="solutionstatus",required = false) String solutionstatus,
                                         @Parameter(name = "memo", description = "memo") @RequestParam(value="memo",required = false) String memo,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution solution = new MonitorSolution();
        QueryWrapper qw = new QueryWrapper();
        qw.eq("SOLUTIONID", solutionid);
        if (solutionid != null && !"".equals(solutionid)) {
            solution = iMonitorSolutionService.getOne(qw);
        }
        solution.setSolutioncode(solutioncode);
        solution.setSolutionname(solutionname);
        solution.setSolutionstatus(solutionstatus);
        solution.setMemo(memo);
        iMonitorSolutionService.updateById(solution);
        Map map = new HashMap();
        map.put("orgId", orgId);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolution_del")
    @PostMapping(value = "/mxjk/modelsolution_del")
    public JsonBean modelsolution_del(@Parameter(name = "selectedid", description = "selectedid") @RequestParam("selectedid") String selectedid,
                                      @Parameter(name = "orgId", description = "orgId") @RequestParam(value="orgId",required = false) String orgId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedid != null) {
            MonitorSolution monitorSolution = this.iMonitorSolutionService.getById(selectedid);
            //map.put("orgId", monitorSolution.getOrgid());
            QueryWrapper qw = new QueryWrapper();
            qw.eq("RULESOLUTIONID",selectedid);
            iMonitorExeintervalService.remove(qw);
            QueryWrapper qw1 = new QueryWrapper();
            qw1.eq("SOLUTIONID",selectedid);
            iMonitorSolutionRuleService.remove(qw1);

            QueryWrapper qw2 = new QueryWrapper();
            qw2.eq("SOLUTIONID",selectedid);
            iMonitorSolutionStaffService.remove(qw2);
            iMonitorSolutionModelService.remove(qw2);
            iMonitorSolutionIndicatorService.remove(qw2);
            iMonitorSolutionService.removeById(selectedid);


        }

        map.put("orgId", orgId);
        map.put("selectedid", selectedid);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 修改状态
     *
     * @param 、
     * @return
     * @author SongXiangYing
     * @date 2016年1月14日 下午5:12:19
     */
    @Operation(summary = "modelsolutionUpdateStatus")
    @GetMapping(value = "/mxjk/modelsolutionUpdateStatus")
    public JsonBean modelsolutionUpdateStatus(@Parameter(name="solutionid",description="solutionid") @RequestParam("solutionid") BigDecimal solutionid,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        MonitorModelsolution solution = iMonitorModelsolutionService.getById(solutionid);
        System.out.println(solution);
        System.out.println(solutionid);
        if(Objects.isNull(solution)){
            return new JsonBean(200, "表中没有此数据", solutionid);
        }
        String status = solution.getSolutionstatus();
        if (status.equalsIgnoreCase("启用")) {
            solution.setSolutionstatus("停用");
        } else if (status.equalsIgnoreCase("停用")) {
            solution.setSolutionstatus("启用");
        }
        Map map = new HashMap();
        iMonitorModelsolutionService.updateById(solution);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警 ---执行结果
     *
     * @param
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月28日 下午10:09:58
     */
    @Operation(summary = "resultmodelyj")
    @GetMapping(value = "/mxjk/resultmodelyj")
    public JsonBean resultmodelyj(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                  @Parameter(name = "modelid", description = "modelid") @RequestParam("modelid") String modelid,
                                  @Parameter(name = "fhtype", description = "fhtype") @RequestParam("fhtype") String fhtype,
                                  @Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        QueryWrapper<MonitorSolutionresult> wrapper = new QueryWrapper<>();
        IPage<MonitorSolutionresult> a = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        wrapper.eq("solutionid", new BigDecimal(modelid));
        wrapper.eq("source", MonitorSolutionresult.ZX);
        wrapper.orderByDesc("saveTime");

        a=iMonitorSolutionresultService.page(a, wrapper);

        iStaffService.setRealNameForMonitorSolutionresult(a);
        Map map = new LinkedHashMap();
        map.put("orgId", orgId);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("modelid", modelid);
        map.put("pageBean", a);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型预警 ---执行结果---详细结果
     *
     * @param
     * @param
     * @return
     */
    @Operation(summary = "resultModelList")
    @GetMapping(value = "/mxjk/resultmodellist")
    public JsonBean resultModelList(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "modelId", description = "modelId") @RequestParam("modelId") String modelId,
                                    @Parameter(name = "modelid", description = "modelid") @RequestParam("modelid") String modelid,
                                    @Parameter(name = "fhtype", description = "fhtype") @RequestParam("fhtype") String fhtype,
                                    @Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId,
                                    @Parameter(name = "solutionResultId", description = "solutionResultId") @RequestParam("solutionResultId") BigDecimal solutionResultId,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage<MonitorSolutionresult> a = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);
        Map map = new HashMap();
        map.put("solutionResultId", solutionResultId);
        map.put("modelId", modelId);
        map.put("modelid", modelid);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("pageBean", iMonitorModelService.ShowPage(new BigDecimal(modelId), a));
        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型管理 ---结果
     *
     * @param pageNumber
     * @param modelId
     * @param
     * @return
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:05:50
     */
    @Operation(summary = "resultmodelInfo")
    @GetMapping(value = "/mxjk/resultmodelinfo")
    public JsonBean resultmodelInfo(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "modelId", description = "modelId传soluId时为modelId,不传soluId时为resultid") @RequestParam(value = "modelId") BigDecimal modelId,
                                    @Parameter(name = "soluId", description = "soluId非必填项,看模块") @RequestParam(value = "soluId", required = false) BigDecimal soluId,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModelresult modelResult = null;
        BigDecimal mid = null;
        if (null != soluId) {

            QueryWrapper<MonitorModelresult> wrapper = new QueryWrapper<>();
            wrapper.eq("solutionresultid", soluId);
            wrapper.eq("modelid", modelId);
            modelResult = iMonitorModelresultService.getOne(wrapper);
            if (modelResult != null) {
                mid = modelResult.getResultid();
            }
        } else {
            modelResult = iMonitorModelresultService.getById(modelId);
            mid = modelId;
        }
        String signid = modelResult.getSignid();
        MonitorModel model = iMonitorModelService.getById(modelResult.getModelid());
        List<String> list = new ArrayList<String>();
        if (StringUtils.isNotBlank(model.getModelstep1())) {
            list.add("第一步");
        }
        if (StringUtils.isNotBlank(model.getModelstep2())) {
            list.add("第二步");
        }
        if (StringUtils.isNotBlank(model.getModelstep3())) {
            list.add("第三步");
        }
        if (StringUtils.isNotBlank(model.getModelstep4())) {
            list.add("第四步");
        }
        if (StringUtils.isNotBlank(model.getModelstep5())) {
            list.add("第五步");
        }
        Map map = new HashMap();
        map.put("signid", signid);
        map.put("modelId", mid);
        map.put("soluId", soluId);
        map.put("list", list);


        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型管理 --详细结果
     *
     * @param index
     * @param
     * @param modelId
     * @return
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:06:08
     */
    @Operation(summary = "resultmodelInfo1")
    @GetMapping(value = "/mxjk/resultmodelinfo1/{index}/{signId}/{modelId}/{source}")
    public JsonBean resultmodelInfo1(@Parameter(name = "index", description = "index") @PathVariable("index") String index,
                                     @Parameter(name = "signId", description = "signId") @PathVariable("signId") String signId,
                                     @Parameter(name = "modelId", description = "modelId") @PathVariable("modelId") BigDecimal modelId,
                                     @Parameter(name = "source", description = "source") @PathVariable("source") Integer source,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "fhtype", description = "fhtype") @RequestParam("fhtype") String fhtype,
                                     @Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = null;
        if (source.equals(MonitorModelresult.ZJ)) {
            QueryWrapper qw = new QueryWrapper();
            qw.eq("RESULTID", modelId);
            MonitorModelresult modelResult = iMonitorModelresultService.getOne(qw);
            model = iMonitorModelService.getById(modelResult.getModelid());
//            model = iMonitorModelService.getById(659770);
        } else {
            QueryWrapper<MonitorModelresult> wrapper = new QueryWrapper<>();
            wrapper.eq("resultId", modelId);
            wrapper.like("signid", iWarningResultService.getLast() + "_%");
            List<MonitorModelresult> list = iMonitorModelresultService.list(wrapper);
            if (list.size() > 0) {
                model = iMonitorModelService.getById(modelId.toString());
                signId = list.get(0).getSignid();
                index = list.get(0).getIsdata().toString();
            }
        }

        String tableName = "ZNJK_MX_" + model.getModelid().toString() + "_" + index;
        Accbook acctbook = iAccbookService.getById(model.getConnectionstrings());
        if (Objects.isNull(acctbook)) {
            return new JsonBean(500, "通过连库没有找到帐簿!!" + model.getConnectionstrings(), null);
        }
        DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
        Map<String, Object> cols = dynamicDataSource.getData(tableName, signId, pageNumber, ConstClass.DEFAULT_SIZE, "");
        Map<String, Object> map = new HashMap<>(6);
        map.put("column", cols.get("column"));
        map.put("pageBean", cols.get("data"));
        map.put("index", index);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("modelId", modelId);
        map.put("signId", signId);
        map.put("modelId", modelId);
        map.put("source", source);
        map.put("bookId", acctbook.getAcctid());
        return new JsonBean(200, "成功", map);
    }

    @Autowired
    YhrPageMapper yhrPageMapper;
    /**
     * 模型预警 --详细结果
     *
     * @param
     * @param
     * @param modelId
     * @return
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:06:08
     */
    @Operation(summary = "resultmodelInfo2")
    @GetMapping(value = "/mxjk/resultmodelinfo/{modelId}/{soluId}")
    public JsonBean resultmodelInfo2(@Parameter(name = "modelId", description = "modelId", required = true) @PathVariable("modelId") BigDecimal modelId,
                                     @Parameter(name = "soluId", description = "soluId", required = true) @PathVariable("soluId") BigDecimal soluId,
                                     @Parameter(name = "modelIds", description = "modelIds") @RequestParam(value="modelIds",required = false) BigDecimal modelIds,
                                     @Parameter(name = "fhtype", description = "fhtype") @RequestParam(value="fhtype",required = false) BigDecimal fhtype,
                                     @Parameter(name = "orgId", description = "orgId") @RequestParam(value="orgId",required = false) BigDecimal orgId,
                                     @Parameter(name = "modelid", description = "modelid") @RequestParam(value="modelid",required = false) BigDecimal modelid,
                                     @Parameter(name = "solutionResultId", description = "solutionResultId") @RequestParam(value="solutionResultId",required = false) BigDecimal solutionResultId,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber",required = false ) Integer pageNumber,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String sql = "select * " +
                "from TBL_MONITOR_MODELRESULT t " +
                "         left join TBL_MONITOR_SOLUTIONRESULT TMS on t.SOLUTIONRESULTID = TMS.SOLUTIONRESULTID " +
                "where tms.SOLUTIONRESULTID ="+modelId+" and t.MODELID="+soluId;
        Map<String,Object> modelResult = yhrPageMapper.queryBySql(sql);

        MonitorModel model = iMonitorModelService.getById(new BigDecimal(modelResult.get("MODELID").toString()));
        System.out.println(model+"==>");
        int index = 0;
        if (StringUtils.isNotBlank(model.getModelstep1())) {
            index = 1;
            if (StringUtils.isNotBlank(model.getModelstep2())) {
                index = 2;
                if (StringUtils.isNotBlank(model.getModelstep3())) {
                    index = 3;
                    if (StringUtils.isNotBlank(model.getModelstep4())) {
                        index = 4;
                        if (StringUtils.isNotBlank(model.getModelstep5())) {
                            index = 5;
                        }
                    }
                }
            }
        }
        String tableName = "ZNJK_MX_" + model.getModelid().toString() + "_" + index;
        Accbook acctbook = iAccbookService.getById(model.getConnectionstrings());
        if (Objects.isNull(acctbook)) {
            return new JsonBean(500, "通过连库没有找到帐簿!!" + model.getConnectionstrings(), null);
        }
        Map<String, Object> map = new HashMap();
        try {
            DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
            Map<String, Object> cols = dynamicDataSource.getData(tableName, modelResult.get("RESULTID").toString(), pageNumber, ConstClass.DEFAULT_SIZE, "");
            map.put("column", cols.get("column"));
            map.put("pageBean", cols.get("data"));
            map.put("soluId", soluId);
            map.put("modelId", modelId);
            map.put("modelIds", modelIds);
            map.put("fhtype", fhtype);
            map.put("orgId", orgId);
            map.put("modelid", modelid);
            map.put("solutionResultId", solutionResultId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 验证模型是否被预警使用
     *
     * @param
     * @return
     */
    @Operation(summary = "model_info_is")
    @GetMapping(value = "/mxjk/model_info_is")
    public JsonBean model_info_is(@Parameter(name = "incid", description = "incid") @RequestParam("incid") String incid,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        int num = 0;
        if (incid != null && !"".equals(incid)) {
            MonitorModel in = iMonitorModelService.getById(incid);
            QueryWrapper<MonitorSolution> wrapper = new QueryWrapper<>();
            wrapper.eq("modelid", incid);
            List<MonitorSolution> list2 = iMonitorSolutionService.list(wrapper);
            num = list2.size();
            if (String.valueOf(in.getRunstatus()).equals("2")) {
                num = 2;
            }
        }
        return new JsonBean(200, "成功", num);
    }

    /**
     * 模型预警-执行
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolution_execute")
    @GetMapping(value = "/mxjk/modelsolution_execute")
    public JsonBean modelsolution_execute(@Parameter(name = "id", description = "id") @RequestParam("id") String id,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
            String Orgid = staff.getCurrentOrg().getOrgid().toString();
        MonitorSolution modelsolution = iMonitorSolutionService.getById(id);
        QueryWrapper<MonitorSolutionModel> wrapper = new QueryWrapper<>();
        wrapper.eq("solutionid", id);

        List<MonitorSolutionModel> list2 = iMonitorSolutionModelService.list(wrapper);
        if (list2.size() > 0) {
            if (modelsolution.getSolutionstatus().equals("启用")) {
                if (modelsolution.getRunstatus().equals(new BigDecimal("1"))) {
                    return new JsonBean(200, "执行中，请稍等！", "执行中，请稍等！");
                }
                List<MonitorSolution> list = new ArrayList<>();
                list.add(modelsolution);
                ScheduleJob job = null;
                try {
                    Staff staff1 = iStaffService.getById(staff.getStaffid());
                    job = cJobTaskService.mxyjTaskExec(list, staff1, "", MonitorSolutionresult.ZX);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    try {
                        jobTaskService.changeStatus(job, "stop");
                    } catch (SchedulerException e1) {
                        e1.printStackTrace();
                    }
                }
                return new JsonBean(200, "成功", "");
            }
            return new JsonBean(200, "请先启用模型！", "请先启用模型！");
        }
        return new JsonBean(200, "模型预警缺少模型规则！", "模型预警缺少模型规则！");
    }

    @Operation(summary = "模型预警-启用")
    @PostMapping(value = "/gzjk/solution/solutionUpdateStatus")
    public JsonBean solutionUpdateStatus(
            @Parameter(name="solutionid",description="solutionid") @RequestParam("solutionid")BigDecimal  solutionid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution solution = iMonitorSolutionService.getById(solutionid);
        if(solution==null){
            return new JsonBean(-1,"id在表中不存在",solutionid);
        }
        if (solution.getStaffid().equals(staff.getStaffid())) {
            if (null != solution) {
                String status = solution.getSolutionstatus();
                if (status.equalsIgnoreCase("启用")) {
                    solution.setSolutionstatus("停用");
                } else if (status.equalsIgnoreCase("停用")) {
                    solution.setSolutionstatus("启用");
                }
                iMonitorSolutionService.updateById(solution);
                return new JsonBean(200,"修改成功",solution);
            }
        }
        return new JsonBean(-1,"权限不足",null);
        // ModelAndView mv = new ModelAndView();
        // mv.setViewName("redirect:/znjk/gzjk/rule/solutionmgmt?orgId="+orgId+"&pageNumber="+pageNumber);
        //
        // return mv;
    }
}
