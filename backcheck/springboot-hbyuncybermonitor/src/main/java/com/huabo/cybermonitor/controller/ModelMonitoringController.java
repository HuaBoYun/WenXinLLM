package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Accbook;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.BugCriterion;
import com.huabo.cybermonitor.entity.ListPager;
import com.huabo.cybermonitor.entity.MonitorModel;
import com.huabo.cybermonitor.entity.MonitorModelresult;
import com.huabo.cybermonitor.entity.MonitorModelsolution;
import com.huabo.cybermonitor.entity.MonitorSolution;
import com.huabo.cybermonitor.entity.MonitorSolutionModel;
import com.huabo.cybermonitor.entity.MonitorSolutionresult;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.entity.Worksheet;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IAttachmentService;
import com.huabo.cybermonitor.service.IBugCriterionService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelresultService;
import com.huabo.cybermonitor.service.IMonitorModelsolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionModelService;
import com.huabo.cybermonitor.service.IMonitorSolutionService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IStaffService;
import com.huabo.cybermonitor.service.IWarningResultService;
import com.huabo.cybermonitor.service.IWorksheetService;
import com.huabo.cybermonitor.service.base.CJobTaskService;
import com.huabo.cybermonitor.service.base.DepartmentUtils;
import com.huabo.cybermonitor.task.base.JobTaskService;
import com.huabo.cybermonitor.task.base.ScheduleJob;
import com.huabo.cybermonitor.util.DateUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




/**
 * 模型监控api接口
 * @author kangjx
 * @createTime 2022/7/11
 */
@RestController
@Slf4j
@Tag(name="模型监控",description="模型监控")
@RequestMapping(value = "/cyber/ModelMonitoringController")
public class ModelMonitoringController {

    private static final Logger logger = LoggerFactory.getLogger(ModelMonitoringController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    IMonitorSolutionService iMonitorSolutionService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    IMonitorModelsolutionService iMonitorModelsolutionService;

    @Autowired
    IMonitorModelresultService iMonitorModelresultService;



    @Autowired
    IWarningResultService iWarningResultService;

    @Autowired
    IAccbookService iAccbookService;

    @Autowired
    IAttachmentService iAttachmentService;

    @Autowired
    IWorksheetService iWorksheetService;

    @Autowired
    IBugCriterionService iBugCriterionService;
    @Autowired
    IStaffService  iStaffService;

    @Autowired
    IMonitorSolutionModelService iMonitorSolutionModelService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 模型下组织机构列表
     * @param orgid 级别机构编码
     * @return
     */
    @Operation(summary = "hy_models_index")
    @GetMapping(value = "/hy_models_index")
    public JsonBean hy_models_index(@Parameter(description="模型编号")@RequestParam("orgid") String orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》main页面");
        List<Organization> findOrgTree = iOrganizationService.findOrganizationByOrgid(orgid);
        Map<String,Object> map = new HashMap<>();
        map.put("list",findOrgTree);
        map.put("number",findOrgTree.size());
        return new JsonBean(200,"success",map);
    }

    /**
     * 行业模型库---》左侧菜单
     * @param orgid 组织号码
     * @param type  组织类型
     * @return
     */
    @Operation(summary = "hy_mx_left")
    @GetMapping(value = "/hy_mx_left")
    public JsonBean hy_mx_left(@Parameter(description="模型编号")@RequestParam("orgid")String orgid,
                               @Parameter(description="模型类型")@RequestParam("type")String type) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》左侧菜单");
        List<Organization> findOrgTree = iOrganizationService.findOrganizationAll();
        String tree = "";
        String treeName="";
        if (StringUtils.isNotEmpty(orgid) && !orgid.equals(findOrgTree.get(0).getOrgid().toString())) {

            iOrganizationService.initOrgByOrgtype(Organization.TYPE_HY_MXK, "行业模型库", orgid);

            tree = iOrganizationService.findOrgTreeByOrgtypeAndOrgid(orgid, Organization.TYPE_HY_MXK, null);
            Organization o=iOrganizationService.getById(orgid);
            treeName=o.getOrgname();
        }else {
            treeName=findOrgTree.get(0).getOrgname();
        }
        int startNum = tree.indexOf("-1_");
        String defaultId = startNum > 0 ? tree.substring(startNum).substring(3, tree.substring(startNum).indexOf("'"))
                : "";
        Map<String,Object> map = new HashMap<>();
        map.put("selectorgtype", Organization.TYPE_HY_MXK);
        map.put("icode", orgid);
        map.put("tree", tree);
        map.put("treeName", treeName.replace("┣", ""));
        map.put("orgTree", findOrgTree);
        map.put("defaultId", defaultId);
        map.put("targetFrame", "mainFramex");
        map.put("type", type);
        return new JsonBean(200,"success",map);
    }

    /**
     * 行业模型库---》列表页
     * @param orgid      组织编码
     * @param page 页码
     * @param limit 显示条数
     * @param code    公用类find里面的 code
     * @param choiceSearch       显示隐藏
     * @return
     */
    @Operation(summary = "行业模型库---》列表页")
    @GetMapping(value = "/hy_modelmgmt")
    public JsonBean hy_modelmgmt(@Parameter(description="模型编号")@RequestParam("orgid")String orgid,
                                 @Parameter(description="页码")@RequestParam("page")int page,
                                 @Parameter(description="显示条码")@RequestParam("limit")int limit,
                                 @Parameter(description="公用编码")@RequestParam("code")String code,
                                 @Parameter(description="显示隐藏")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》列表页");
        //默认显示第一个行业
        if (orgid==null||orgid.equals("")) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("FATHERORGID","( SELECT orgid FROM TBL_ORGANIZATION WHERE ORGNAME = '行业' AND ORGTYPE = 100 )");
            queryWrapper.eq("ROWNUM","1");
            Organization org1 = iOrganizationService.getOne(queryWrapper);
            orgid=org1.getOrgid().toString();
        }
        IPage<MonitorModel> ip = new Page<>(page,limit);
        QueryWrapper<MonitorModel> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(orgid)) {
            queryWrapper.eq("inmodeldb", MonitorModel.IS_HY1)
                    .or()
                    .eq("inmodeldb", MonitorModel.IS_HY2);
            queryWrapper.eq("ORGID", orgid);
        }else{
            queryWrapper.in("inmodeldb",MonitorModel.IS_HY1,MonitorModel.IS_HY2);
        }
        if (StringUtils.isNotEmpty(code)) {
            queryWrapper.like("MODELID","%"+code);
        }
        queryWrapper.orderByDesc("modelid");
        iMonitorModelService.page(ip,queryWrapper);
        Map<String,Object> map = new HashMap();

        map.put("pageBean", ip);
        map.put("orgid", orgid);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("orgid", orgid);
        return new JsonBean(200,"success",map);
    }

    /**
     * 行业模型库---》列表页--->跳往添加页
     * @param orgid
     * @param modelid
     * @param user
     * @param choiceSearch
     * @return
     */
    @Operation(summary = "行业模型库---》列表页--->跳往添加页")
    @GetMapping(value = "/行业模型库---》列表页--->跳往添加页")
    public JsonBean to_hy_model_add(@Parameter(description="模型编号")@RequestParam("orgid")String orgid,
                                    @Parameter(description="modeid")@RequestParam("modelid")String modelid,
                                    @Parameter(description="user")@RequestParam("user")Staff user,
                                    @Parameter(description="显示")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》列表页--->跳往添加页");
        MonitorModel model = new MonitorModel();

        if (StringUtils.isNotEmpty(modelid)) {
            model = iMonitorModelService.getById(modelid);
        }
        Map<String,Object> mv = new HashMap<>(4);
        mv.put("orgid", orgid);
        mv.put("model", model);
        mv.put("username", user.getRealname());
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mv.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"success",mv);
    }

    /**
     *行业模型库---》列表页--->跳详情页
     * @return
     */
    @Operation(summary = "行业模型库---》列表页--->跳详情页")
    @GetMapping(value = "/mxjk/to_hy_model_find")
    public JsonBean to_hy_model_find(@Parameter(description="模型编号")@RequestParam("orgid")String orgid,
                                         @Parameter(description="modeid")@RequestParam("modelid")String modelid,
                                         @Parameter(description="真名realName") @RequestParam("realName") String realName) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》列表页--->跳详情页");
        MonitorModel model = iMonitorModelService.getById(modelid);
        Map<String,Object> mv = new HashMap<>();
        mv.put("orgid", orgid);
        mv.put("model", model);
        mv.put("username",realName);
        return new JsonBean(200,"success",mv);
    }

    @Operation(summary = "hy_model_add")
    @GetMapping(value = "/mxjk/hy_model_add")
    public JsonBean hy_model_add(@Parameter(description="日期") String date ,@Parameter(description="choiceSearch") @RequestParam("choiceSearch") String choiceSearch,
    		@Parameter(description="orgid") @RequestParam("orgid") String orgid,MonitorModel model) throws Exception {
        logger.info("行业模型库---》列表页--->添加");
        TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        model.setInmodeldb(model.IS_HY1.toString());
        if (model.getModelid() != null){
            //String data = request.getParameter("date");
            if (StringUtils.isNotEmpty(date)) {
                    model.setCreatedate(DateUtils.strToLocalDateTime(date,"yyyy-MM-dd"));

            }
            iMonitorModelService.updateById(model);
        } else {
            model.setCreatedate(LocalDateTime.now());
            iMonitorModelService.save(model);
        }
        Map map = new HashMap();

        //为页面查找区域显隐藏赋值

        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        map.put("orgid",orgid);
        //mv.setViewName("redirect:/znjk/mxjk/hy_modelmgmt?orgid=" + orgid+"&choiceSearch="+choiceSearch);
        return new JsonBean(200,"成功",map);
       //return hy_modelmgmt(request, new Find());
    }


    /**
     * 行业模型库---》列表页--->添加--->验证编号是否重复
     * @param number 编号
     * @return
     */
    @Operation(summary = "model_valida_number")
    @PostMapping(value = "/model_valida_number")
    public JsonBean model_valida_number(@Parameter(description="编号")@RequestParam("number") String number,
                                        @Parameter(description="编号编码")@RequestParam("orgid") String orgid) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》列表页--->添加--->验证编号是否重复");
        Boolean b = iMonitorModelService.validateByNumberAndOrg(number,orgid);
        return new JsonBean(200,b.toString(),ObjectUtils.NULL);
    }


    /**
     * 行业模型库---》列表页--->删除
     * @param models models编号
     * @return
     */
    @Operation(summary = "hy_model_del")
    @DeleteMapping(value = "/hy_model_del")
    public JsonBean hy_model_del(@Parameter(description="模型主键")@RequestParam("models") List<String> models) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库---》列表页--->删除");
        boolean result = iMonitorModelService.removeByIds(models);
        return new JsonBean(200,"删除成功", ObjectUtils.NULL);
    }



    @Operation(summary = "搜索")
    @GetMapping(value = "/mxjk/search")
    public JsonBean searchModel(@Parameter(name = "模型主键", description = "模型主键")@RequestParam("models") String modelid,
                                @Parameter(name = "pager", description = "pager")@RequestParam("ListPager") ListPager pager,
                                @Parameter(name = "page", description = "page")@RequestParam("page") String page) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (page == null || pager == null) {
            List<?> list = (List<?>) iMonitorModelService.getById(new BigDecimal(modelid));
            pager = new ListPager(list);
            map.put("pager", pager);
        } else {
            pager.navigate(page);
        }
        map.put("pager", pager);

        return new JsonBean(200,"成功",map);
    }


    /**
     * 修改模型管理的状态
     *
     * @param
     * @return
     */
    @Operation(summary = "modelUpdateStatus")
    @GetMapping(value = "/mxjk/modelUpdateStatus")
    public JsonBean modelUpdateStatus(@Parameter(name = "模型主键", description = "模型主键")@RequestParam("models") String modelid,
                                      @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId") String orgId,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch") String choiceSearch

                                      ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = iMonitorModelService.getById(modelid);
        String status = model.getModelstatus();
        if (status.equalsIgnoreCase("启用")){
            model.setModelstatus("禁用");
        }
        else if (status.equalsIgnoreCase("禁用")){
            model.setModelstatus("启用");
        }

        Map map = new HashMap();
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        iMonitorModelService.updateById(model);
        return new JsonBean(200,"成功",map);
    }


    /**
     * 模型预警-主页
     *
     * @param
     * @return
     */
    @Operation(summary = "modelssolutions_index_index")
    @GetMapping(value = "/mxjk/solutions_index")
    public JsonBean modelssolutions_index_index(@Parameter(name = "orgId", description = "orgId")@RequestParam("orgId") String orgId) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        map.put("orgId",orgId);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型预警-左边
     *
     * @param
     * @return
     */
    @Operation(summary = "mxyj_left")
    @GetMapping(value = "/mxjk/mxyj_left")
    public JsonBean mxyj_left(@Parameter(name = "orgId", description = "orgId")@RequestParam("orgId") String orgId) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        map.put("orgid", orgId);
        map.put("targetFrame", "mainFramex");
        return new JsonBean(200,"成功",map);
    }


    /**
     * 模型预警-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolutionmgmt")
    @GetMapping(value = "/mxjk/modelsolutionmgmt")
    public JsonBean modelsolutionmgmt(@Parameter(name = "solutioncode", description = "solutioncode")@RequestParam(value = "solutioncode",required = true)String solutioncode,
                                      @Parameter(name = "solutioncode", description = "solutioncode")@RequestParam(value = "solutionname",required = true)String solutionname,
                                      @Parameter(name = "solutioncode", description = "solutioncode")@RequestParam(value = "orgId",required = true) String orgId,
                                      @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                      @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                      @Parameter(name = "hbOrgEntityOrgid", description = "hbOrgEntityOrgid") @RequestParam(value = "hbOrgEntityOrgid")String hbOrgEntityOrgid,
                                      @Parameter(name = "hbOrgNameOrgid", description = "hbOrgNameOrgid") @RequestParam(value = "hbOrgNameOrgid")String hbOrgNameOrgid,
                                      @Parameter(name = "OrgidOrgid", description = "OrgidOrgid") @RequestParam(value = "OrgidOrgid")String OrgidOrgid,
                                      @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch") String choiceSearch) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        IPage<MonitorSolution> a = new Page<>(pageNumber,limit);
        QueryWrapper<MonitorSolution> wrapper=new QueryWrapper<>();
        if(hbOrgEntityOrgid.equals(hbOrgNameOrgid)){
            if (orgId == null || orgId == "") {
                orgId = OrgidOrgid;
            }
            wrapper.eq("type",3);
            wrapper.eq("ORGID",new BigDecimal(orgId));
            if(StringUtils.isNotBlank(solutioncode)){
                wrapper.like("SOLUTIONCODE",solutioncode);
            }
            if(StringUtils.isNotBlank(solutionname)){
                wrapper.like("SOLUTIONNAME",solutionname);
            }
            wrapper.orderByDesc("SOLUTIONID");

        }else{
            if (orgId == null || orgId == "") {
                orgId = hbOrgEntityOrgid;
            }
            wrapper.eq("type",3);
            wrapper.eq("ORGID",new BigDecimal(orgId));
            if(StringUtils.isNotBlank(solutioncode)){
                wrapper.like("SOLUTIONCODE",solutioncode);
            }
            if(StringUtils.isNotBlank(solutionname)){
                wrapper.like("SOLUTIONNAME",solutionname);
            }
            wrapper.orderByDesc("SOLUTIONID");
        }
        map.put("pageBean", iMonitorSolutionService.page(a,wrapper));
        map.put("orgId", orgId);
        map.put("tblStaff", staff);
        map.put("solutioncode", solutioncode);
        map.put("solutionname", solutionname);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200,"成功",map);

    }

    /**
     * 模型预警-跳转添加
     *
     * @param
     * @return
     */
    @Operation(summary = "toModelSolutionAdd")
    @PostMapping(value = "/mxjk/to_modelsolution_add")
    public JsonBean toModelSolutionAdd(@Parameter(name = "solutioncode", description = "solutioncode")@RequestParam(value = "orgId",required = true) BigDecimal orgId,
                                       @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff,
                                       @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch") String choiceSearch) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        map.put("orgId", orgId);
        map.put("tblStaff", staff);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
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
    public JsonBean modelSolutionAdd(@Parameter(name = "solution", description = "solution")@RequestBody()MonitorSolution solution,
                                     @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch") String choiceSearch
                                     ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        solution.setCreatedate(LocalDateTime.now());
        solution.setRunstatus(new BigDecimal(0));
        solution.setType(new BigDecimal(3));
        iMonitorSolutionService.updateById(solution);
        Map map = new HashMap();
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        map.put("selectedid",solution.getSolutionid());
        map.put("orgId",solution.getOrgid());// solution.getOrganization().getOrgid() 老代码
        return new JsonBean(200,"成功",map);
    }


    /**
     * 模型预警-添加模型-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "modellistSelector")
    @PostMapping(value = "/mxjk/modellistSelector")
    public JsonBean modellistSelector(@Parameter(name = "orgId", description = "orgId")@RequestParam("orgId") String orgId,
                                      @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid") String modelid,
                                      @Parameter(name = "solutionid", description = "solutionid")@RequestParam("solutionid") String solutionid,
                                      @Parameter(name = "pageNumber", description = "pageNumber")@RequestParam("pageNumber") Integer pageNumber,
                                      @Parameter(name = "limit", description = "limit")@RequestParam("limit") Integer limit,
                                      @Parameter(name = "modelids", description = "modelids")@RequestParam("modelids") Integer modelids,
                                      @Parameter(name = "OrgidOrgid", description = "OrgidOrgid") @RequestParam(value = "OrgidOrgid")String OrgidOrgid,
                                      @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch")String choiceSearch
                                      ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (orgId == null) {
            orgId = OrgidOrgid;
        }
        IPage<MonitorModel> a = new Page<>(pageNumber,limit);
        QueryWrapper<MonitorModel> wrapper=new QueryWrapper<>();
        wrapper.eq("MODELSTATUS","启用");
        wrapper.eq("SUBSYSTEM",1);
        wrapper.eq("ORGID",orgId);
        if (modelid!=null && !"".equals(modelid)) {
            wrapper.like("MODELID",modelid);
        }
        if(modelids != null && !"".equals(modelids)){
            wrapper.notIn("MODELID",modelids);

        }
        wrapper.orderByAsc("MODELID");
        Map map = new HashMap();
        map.put("pageBean", iMonitorModelService.page(a,wrapper));
        map.put("orgId", orgId);
        map.put("modelid", modelid);
        map.put("solutionid", solutionid);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型预警-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolution_del")
    @GetMapping(value = "/mxjk/modelsolution_del")
    public JsonBean modelsolution_del(@Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid,
                                      @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                      @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedid != null) {
            iMonitorSolutionService.removeById(new BigDecimal(selectedid));
        }
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        map.put("orgId", orgId );
        return new JsonBean(200,"成功",map);
    }


    /**
     * 模型预警-跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "to_modelsolution_modify")
    @GetMapping(value = "/mxjk/to_modelsolution_modify")
    public JsonBean to_modelsolution_modify(@Parameter(name = "infoid", description = "infoid")@RequestParam("infoid")String infoid,
                                            @Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid,
                                            @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                            @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch
                                            ) throws Exception {
    	
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        if (StringUtils.isNotBlank(infoid)) {
            selectedid = infoid;
            infoid = "info";
        }
        MonitorSolution solution = iMonitorSolutionService.getById(selectedid);
        Map map = new HashMap();
        map.put("orgId", orgId);
        map.put("viewType", infoid);
        if (solution != null) {
            map.put("solution", solution);
        } else
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "modelsolution_modify")
    @PostMapping(value = "/mxjk/modelsolution_modify")
    public JsonBean modelsolution_modify(@Parameter(name = "solutionid", description = "solutionid")@RequestParam("solutionid")String solutionid,
                                         @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                         @Parameter(name = "solutioncode", description = "solutioncode")@RequestParam("solutioncode")String solutioncode,
                                         @Parameter(name = "solutionname", description = "solutionname")@RequestParam("solutionname")String solutionname,
                                         @Parameter(name = "solutionstatus", description = "solutionstatus")@RequestParam("solutionstatus")String solutionstatus,
                                         @Parameter(name = "memo", description = "memo")@RequestParam("memo")String memo,
                                         @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {

    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution solution = new MonitorSolution();
        if (solutionid != null && !"".equals(solutionid)) {
            solution = iMonitorSolutionService.getById(solutionid);
        }
        solution.setSolutioncode(solutioncode);
        solution.setSolutionname(solutionname);
        solution.setSolutionstatus(solutionstatus);
        solution.setMemo(memo);
        iMonitorSolutionService.updateById(solution);
        Map map = new HashMap();
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        map.put("orgId",orgId);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型预警-执行
     *
     * @param
     * @return
     */
    @Operation(summary = "modelsolution_execute")
    @GetMapping(value = "/mxjk/modelsolution_execute", produces = "application/json; charset=utf-8")
    public JsonBean modelsolution_execute(@Parameter(name = "id", description = "id")@RequestParam("id")String id,
                                          @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff
                                                      ) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorSolution modelsolution = iMonitorSolutionService.getById(id);
        QueryWrapper<MonitorSolutionModel> wrapper=new QueryWrapper<>();
        wrapper.eq("solutionid",id);

        List<MonitorSolutionModel> list2=iMonitorSolutionModelService.list(wrapper);
        //Set<MonitorModel> models = modelsolution.getTblMonitorSolutionModels();
        if (list2.size() > 0) {
            if (modelsolution.getSolutionstatus().equals("启用")) {
                if (modelsolution.getRunstatus().equals(new BigDecimal("1"))) {
                    return new JsonBean(200,"执行中，请稍等！","执行中，请稍等！");
                }
                List<MonitorSolution> list = new ArrayList<>();
                list.add(modelsolution);
                ScheduleJob job = null;
                try {
                    job = this.cJobTaskService.mxyjTaskExec(list, staff, "", MonitorSolutionresult.ZX);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                    try {
                        jobTaskService.changeStatus(job, "stop");
                    } catch (SchedulerException e1) {
                        e1.printStackTrace();
                    }
                }
                return new JsonBean(200,"成功","");
            }
            return new JsonBean(200,"请先启用模型！","请先启用模型！");
        }
        return new JsonBean(200,"模型预警缺少模型规则！","模型预警缺少模型规则！");
    }

    /**
     * 模型管理---执行
     *
     * @author SongXiangYing
     * @date 2016年1月28日 下午5:49:44
     * @param
     * @return
     */
    @Operation(summary = "modelEexc")
    @GetMapping(value = "/mxjk/model/execute", produces = "application/json; charset=utf-8")
    public JsonBean modelEexc(@Parameter(name = "id", description = "id")@RequestParam("id")String id,
                              @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = iMonitorModelService.getById(id);
        if (model.getModelstatus().equals("启用")) {
            if (model.getRunstatus().equals("1")) {
                return new JsonBean(200,"执行中，请稍等！","执行中，请稍等！");
            }
            List<MonitorModel> list = new ArrayList<MonitorModel>();
            list.add(model);
            ScheduleJob mxTaskExec = null;
            try {
               // mxTaskExec = this.cJobTaskService.mxTaskExec(list, null, staff);
                model.setRunstatus(1);
            } catch (Exception e) {
                model.setRunstatus(3);
                try {
                    this.jobTaskService.changeStatus(mxTaskExec, "stop");
                } catch (SchedulerException e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
            iMonitorModelService.updateById(model);
            return new JsonBean(200,"成功","");
        }
        return new JsonBean(200,"请先启用模型！","请先启用模型！");
    }

    @Operation(summary = "modelsolutionsearch")
    @GetMapping(value = "/mxjk/modelsolutionsearch")
    public JsonBean modelsolutionsearch(@Parameter(name = "solutionid", description = "solutionid")@RequestParam("solutionid")String solutionid,
                                        @Parameter(name = "pager", description = "pager")@RequestBody()ListPager pager,
                                        @Parameter(name = "page", description = "page")@RequestParam("page")String page
                                        ) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (page == null || pager == null) {
            List<?> list = (List<?>) iMonitorSolutionService.getById(solutionid);

            pager = new ListPager(list);
            map.put("pager", pager);
        } else {
            pager.navigate(page);
        }
        map.put("pager", pager);

        return new JsonBean(200,"成功",map);
    }

    /**
     *
     * @author SongXiangYing
     * @date 2016年1月14日 下午5:12:19
     * @param 、
     * @return
     */
    @Operation(summary = "modelsolutionUpdateStatus")
    @GetMapping(value = "/mxjk/modelsolutionUpdateStatus")
    public JsonBean modelsolutionUpdateStatus(@Parameter(name = "solutionid", description = "solutionid")@RequestParam("solutionid")String solutionid
    ) throws Exception {

    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
    	
        MonitorModelsolution solution = iMonitorModelsolutionService.getById(solutionid);
        String status = solution.getSolutionstatus();
        if (status.equalsIgnoreCase("启用")){
            solution.setSolutionstatus("停用");
        }
        else if (status.equalsIgnoreCase("停用")){
            solution.setSolutionstatus("启用");
        }
        Map map = new HashMap();
        iMonitorModelsolutionService.updateById(solution);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "modelslistSelector")
    @GetMapping(value = "/mxjk/modelslistSelector")
    public JsonBean modelslistSelector( @Parameter(name = "TFPager", description = "TFPager")@RequestBody()ListPager TFPager,
                                        @Parameter(name = "page", description = "page")@RequestParam("page")String page
                                        ) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (page == null || TFPager == null) {

            List<?> list = iMonitorModelService.list();
            TFPager = new ListPager(list);
            map.put("TFPager", TFPager);
        } else {
            TFPager.navigate(page);
        }
        map.put("pager", TFPager);
        return new JsonBean(200,"成功",map);
    }



    /**
     * 模型管理-左边
     *
     * @param
     * @return
     */
    @Operation(summary = "mx_left")
    @GetMapping(value = "/mxjk/mx_left")
    public JsonBean mx_left(@Parameter(name = "hbOrgEntityOrgid", description = "hbOrgEntityOrgid")@RequestParam("hbOrgEntityOrgid")String hbOrgEntityOrgid,
                            @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
       // map.put("userOrgId", Staff.getTblOrganization().getOrgid());
        map.put("orgid",hbOrgEntityOrgid);
        // map.put("tree", tree);
        map.put("orgid",hbOrgEntityOrgid);
        map.put("targetFrame", "mainFramex");
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-列表
     *
     * @param
     * @return
     */
    @Operation(summary = "modelmgmt")
    @GetMapping(value = "/mxjk/modelmgmt")
    public JsonBean modelmgmt(@Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                              @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                              @Parameter(name = "modelname", description = "modelname")@RequestParam("modelname")String modelname,
                              @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                              @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                              @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch,
                              @Parameter(name = "hbOrgEntityOrgid", description = "hbOrgEntityOrgid")@RequestParam("hbOrgEntityOrgid")String hbOrgEntityOrgid,
                              @Parameter(name = "hbOrgNameOrgid", description = "hbOrgNameOrgid")@RequestParam("hbOrgNameOrgid")String hbOrgNameOrgid,
                              @Parameter(name = "staff", description = "staff")@RequestBody()Staff staff

                              ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        Boolean isSelect = false;
        QueryWrapper<MonitorModel> wrapper = new QueryWrapper<>();
        IPage<MonitorModel> a = new Page<>(pageNumber,limit);
        if(hbOrgEntityOrgid.equals(hbOrgNameOrgid)){
            if (StringUtils.isBlank(orgId)) {
                //orgId = staff.getTblOrganization().getOrgid().toString();
            }
            wrapper.eq("inmodeldb",0);
            wrapper.eq("ORGID",orgId);
            if (modelid!=null && !"".equals(modelid)) {
                wrapper.like("modelcode",modelid);
            }
            if(StringUtils.isNotBlank(modelname)){
                wrapper.like("MODELNAME",modelname);
            }

        }else{
            if (StringUtils.isBlank(orgId)) {
                orgId = hbOrgEntityOrgid;
            }

            wrapper.eq("inmodeldb",0);
            wrapper.eq("ORGID",orgId);
            if (modelid!=null && !"".equals(modelid)) {
                wrapper.like("modelcode",modelid);
            }
            if(StringUtils.isNotBlank(modelname)){
                wrapper.like("MODELNAME",modelname);
            }


        }
        wrapper.orderByAsc("MODELID");
        map.put("pageBean", iMonitorModelService.page(a,wrapper));
        map.put("orgId", orgId);
        map.put("tblStaff", staff);
        map.put("modelid", modelid);
        map.put("modelname", modelname);
        map.put("isSelect", isSelect);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理 ---结果
     *
     * @author SongXiangYing
     * @date 2016年1月28日 下午10:09:58
     * @param pageNumber
     * @param
     * @return
     */
    @Operation(summary = "resultmodel")
    @GetMapping(value = "/mxjk/resultmodel")
    public JsonBean resultmodel(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                                @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")String fhtype,
                                @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch
                                ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        QueryWrapper<MonitorSolutionresult> wrapper = new QueryWrapper<>();
        IPage<MonitorSolutionresult> a = new Page<>(pageNumber,limit);
        if (modelid != null && modelid != "") {
            //  regexp_like ( signid,'^[[:digit:]]+$')   这个条件不会生成
            wrapper.eq("modelid",modelid);
            wrapper.orderByDesc("saveTime");
        }
        Map map = new HashMap();
        map.put("fhtype", fhtype);
        map.put("pageBean", iMonitorSolutionresultService.page(a,wrapper));
        map.put("modelid", modelid);
        map.put("orgId", orgId);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型预警 ---执行结果
     *
     * @author SongXiangYing
     * @date 2016年1月28日 下午10:09:58
     * @param
     * @param
     * @return
     */
    @Operation(summary = "resultmodelyj")
    @GetMapping(value = "/mxjk/resultmodelyj")
    public JsonBean resultmodelyj(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                  @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                  @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                                  @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")String fhtype,
                                  @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                  @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch
                                  ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        QueryWrapper<MonitorSolutionresult> wrapper = new QueryWrapper<>();
        IPage<MonitorSolutionresult> a = new Page<>(pageNumber,limit);
        wrapper.eq("solutionid",new BigDecimal(modelid));
        wrapper.eq("source",MonitorSolutionresult.ZX);
        wrapper.orderByDesc("saveTime");
        Map map = new HashMap();
        map.put("orgId", orgId);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("modelid", modelid);
        map.put("pageBean", iMonitorSolutionresultService.page(a,wrapper));
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "resultModelList")
    @GetMapping(value = "/mxjk/resultmodellist")
    public JsonBean resultModelList(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                    @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                    @Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")String modelId,
                                    @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                                    @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")String fhtype,
                                    @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                    @Parameter(name = "solutionResultId", description = "solutionResultId")@RequestParam("solutionResultId")BigDecimal solutionResultId
                                        ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage<MonitorSolutionresult> a = new Page<>(pageNumber,limit);

        Map map = new HashMap();
        map.put("solutionResultId", solutionResultId);
        map.put("modelId", modelId);
        map.put("modelid", modelid);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        map.put("pageBean", iMonitorModelService.ShowPage(new BigDecimal(modelId),a));
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理 ---结果
     *
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:05:50
     * @param pageNumber
     * @param modelId
     * @param
     * @return
     */
    @Operation(summary = "resultmodelInfo")
    @GetMapping(value = "/mxjk/resultmodelinfo")
    public JsonBean resultmodelInfo(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                    @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                    @Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")BigDecimal modelId,
                                    @Parameter(name = "soluId", description = "soluId")@RequestParam("soluId")BigDecimal soluId,
                                    @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")BigDecimal modelid,
                                    @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")BigDecimal fhtype,
                                    @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")BigDecimal orgId
                                        ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModelresult modelResult = null;
        BigDecimal mid = null;
        if (null != soluId) {
            // TODO signid
            QueryWrapper<MonitorModelresult> wrapper = new QueryWrapper<>();
            wrapper.eq("solutionresultid",modelId);
            wrapper.eq("modelid",soluId);
            List<MonitorModelresult> list = iMonitorModelresultService.list(wrapper);
            if (list.size() > 0) {
                modelResult = list.get(0);
                mid = modelResult.getResultid();
            }
        } else {
            modelResult = iMonitorModelresultService.getById(modelId); //这个位置感觉有问题
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
       // map.put("list", list);
        map.put("modelId", mid);
        map.put("soluId", soluId);
        map.put("modelid", modelid);
        map.put("fhtype", fhtype);
        map.put("orgId", orgId);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理 --详细结果
     *
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:06:08
     * @param index
     * @param
     * @param modelId
     * @return
     */
    @Operation(summary = "resultmodelInfo1")
    @GetMapping(value = "/mxjk/resultmodelinfo/{index}/{signId}/{modelId}/{source}")
    public JsonBean resultmodelInfo1(@Parameter(name = "index", description = "index")@RequestParam("index")String index,
                                     @Parameter(name = "signId", description = "signId")@RequestParam("signId")String signId,
                                     @Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")BigDecimal modelId,
                                     @Parameter(name = "source", description = "source")@RequestParam("source")Integer source,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                     @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                     @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")String fhtype,
                                     @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                                     @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid
                                     ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = null;
        if (source == MonitorModelresult.ZJ) {
            MonitorModelresult modelResult = iMonitorModelresultService.getById(modelId);
            model = iMonitorModelService.getById(modelId);
        } else {
            QueryWrapper<MonitorModelresult> wrapper = new QueryWrapper<>();
            wrapper.eq("modelid",modelId);
            wrapper.like("signid",iWarningResultService.getLast()+"_%");
            List<MonitorModelresult> list = iMonitorModelresultService.list(wrapper);
            if (list.size() > 0) {
                model = iMonitorModelService.getById(modelId.toString());
                signId = list.get(0).getSignid();
                index = list.get(0).getIsdata().toString();
            }
        }
        String tableName = "ZNJK_MX_" + model.getModelid().toString() + "_" + index;
        Accbook acctbook=iAccbookService.getById(model.getConnectionstrings());
        Map<String, Object> map = new HashMap<>();
 //       try {
//            map = this.hibernateExecuteService.getDate(tableName, acctbook.getAcctid(), signId,
//                    pageNumber, pageBean.getPageSize(), "");
            map.put("column", map.get("column"));
            map.put("pageBean", map.get("data"));
            map.put("index", index);
            map.put("fhtype", fhtype);
            map.put("orgId", orgId);
            map.put("modelid", modelid);
            map.put("signId", signId);
            map.put("modelId", modelId);
            map.put("source", source);
            map.put("bookId", acctbook.getAcctid());
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型预警 --详细结果
     *
     * @author SongXiangYing
     * @date 2016年1月29日 上午11:06:08
     * @param
     * @param
     * @param modelId
     * @return
     */
    @Operation(summary = "resultmodelInfo2")
    @GetMapping(value = "/mxjk/resultmodelinfo/{modelId}/{soluId}")
    public JsonBean resultmodelInfo2(@Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")BigDecimal modelId,
                                     @Parameter(name = "soluId", description = "soluId")@RequestParam("soluId")BigDecimal soluId,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                     @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                     @Parameter(name = "modelIds", description = "modelIds")@RequestParam("modelIds")BigDecimal modelIds,
                                     @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")BigDecimal fhtype,
                                     @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")BigDecimal orgId,
                                     @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")BigDecimal modelid,
                                     @Parameter(name = "solutionResultId", description = "solutionResultId")@RequestParam("solutionResultId")BigDecimal solutionResultId
                                     ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModelresult modelResult = null;
        QueryWrapper<MonitorModelresult> wrapper = new QueryWrapper<>();
        wrapper.eq("solutionresultid",soluId);
        wrapper.eq("modelid",modelId);
        List<MonitorModelresult> list = iMonitorModelresultService.list(wrapper);
        if (list.size() > 0) {
            modelResult = list.get(0);
        }
        MonitorModel model = iMonitorModelService.getById(modelId);
//        MonitorModel model = modelResult.getMonitorModel();
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
        Accbook acctbook=iAccbookService.getById(model.getConnectionstrings());
        Map<String, Object>  map = new HashMap();
 //       try {
//           map = this.hibernateExecuteService.getDate(tableName, acctbook.getAcctid(),
//                    modelResult.getSignId(), pageNumber, pageBean.getPageSize(), "");
            map.put("column", map.get("column"));
            map.put("pageBean", map.get("data"));
            map.put("soluId", soluId);
            map.put("modelId", modelId);
            map.put("modelIds", modelIds);
            map.put("fhtype", fhtype);
            map.put("orgId", orgId);
            map.put("modelid", modelid);
            map.put("solutionResultId", solutionResultId);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return new JsonBean(200,"成功",map);
    }

    /**
     * 验证模型是否被预警使用
     *
     * @param
     * @return
     */
    @Operation(summary = "model_info_is")
    @GetMapping(value = "/mxjk/model_info_is")
    public JsonBean model_info_is(@Parameter(name = "incid", description = "incid")@RequestParam("incid")String incid) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        int num = 0;
        if (incid != null && !"".equals(incid)) {
            MonitorModel in = iMonitorModelService.getById(incid);


            QueryWrapper<MonitorSolutionModel> wrapper=new QueryWrapper<>();
            wrapper.eq("modelid",incid);

            List<MonitorSolutionModel> list2=iMonitorSolutionModelService.list(wrapper);
            //缺少字段 -解决
//            Set<MonitorSolution> monitorSolutions = in.getTblMonitorModelSolutions();
            num = list2.size();
            if (String.valueOf(in.getRunstatus()).equals("2")) {
                num = 2;
            }
        }
        return new JsonBean(200,"成功",num);
    }

    /**
     * 模型管理-删除权限判断
     *
     * @param
     * @return
     */
    @Operation(summary = "modelcheckupdate")
    @GetMapping(value = "/mxjk/modelcheckupdate", produces = "application/json; charset=utf-8")
    public JsonBean modelcheckupdate(@Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid,
                                     @Parameter(name = "Staffid", description = "Staffid")@RequestParam("Staffid")String Staffid) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel rule = iMonitorModelService.getById(selectedid);
        if (rule.getStaffid().toString().equals(Staffid)) {
            if (rule.getRunstatus().toString().equals("0")) {
                return new JsonBean(200,"成功","");
            } else {
                return new JsonBean(200,"该模型已执行，不能删除和修改","该模型已执行，不能删除和修改");
            }
        } else {
            return new JsonBean(200,"权限不足","权限不足");
        }
    }

    /**
     * 模型管理-tree
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @param
     * @return
     */
    @Operation(summary = "mxgl_findOrganizationByTree")
    @GetMapping(value = "/mxjk/findOrganizationByTreeAll", produces = "application/json; charset=utf-8")
    public JsonBean mxgl_findOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId")@RequestParam("nodeId")BigDecimal nodeId,
                                                @Parameter(name = "type", description = "type")@RequestParam("type")String type,
                                                @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")BigDecimal orgId) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
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
        return new JsonBean(200,"成功",json);
    }

    /**
     * 模型管理-跳转添加
     *
     * @param
     * @return
     */
    @Operation(summary = "to_model_add")
    @GetMapping(value = "/mxjk/to_model_add")
    public JsonBean to_model_add(@Parameter(name = "staff", description = "staff")@RequestBody()Staff staff,
                                 @Parameter(name = "hbOrgEntityOrgid", description = "hbOrgEntityOrgid")@RequestParam("hbOrgEntityOrgid")String hbOrgEntityOrgid,
                                 @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Organization organization = iOrganizationService.getById(staff.getOrgid());
        Map map = new HashMap();
        List<Accbook> bookList = iAccbookService.findBookIdByUserAll(staff.getStaffid(),hbOrgEntityOrgid);
        List<Accbook> books = DepartmentUtils.getAllDepartmentss(bookList);
        map.put("books", books);
        map.put("user", staff);
       // map.put("organization", organization);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-添加
     *
     * @param
     * @param model
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Operation(summary = "model_add")
    @GetMapping(value = "/mxjk/model_add")
    public JsonBean model_add(@Parameter(name = "staff", description = "staff")@RequestBody()MonitorModel model,
                              @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch)
            throws ClassNotFoundException, Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        model.setCreatedate(LocalDateTime.now());
        model.setRunstatus(0);
        model.setSubsystem(1);
        model.setInmodeldb(MonitorModel.IS_HY0.toString());
        iMonitorModelService.save(model);
        Map map = new HashMap();
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-跳转修改
     *
     * @param
     * @return
     */
    @Operation(summary = "to_model_modify")
    @GetMapping(value = "/mxjk/to_model_modify")
    public JsonBean to_model_modify(@Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid,
                                    @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = iMonitorModelService.getById(selectedid);
        Map map = new HashMap();
        String orgname = "";
        String name = "";
        if (model != null) {
            map.put("model", model);
            Organization organization = iOrganizationService.getById(model.getOrgid());
            Staff user = iStaffService.getById(model.getStaffid());
            map.put("user", user);
            map.put("organization", organization);
        } else

        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-查看
     *
     * @param
     * @return
     */
    @Operation(summary = "to_model_disp")
    @GetMapping(value = "/mxjk/to_model_disp")
    public JsonBean to_model_disp(@Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = iMonitorModelService.getById(selectedid);
        Map map = new HashMap();
        map.put("model", model);
        Organization organization = iOrganizationService.getById(model.getOrgid());
        Staff user = iStaffService.getById(model.getStaffid());
        map.put("user", user);
        map.put("organization", organization);

        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-修改
     *
     * @param
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Operation(summary = "model_modify")
    @GetMapping(value = "/mxjk/model_modify")
    public JsonBean model_modify(@Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                                 @Parameter(name = "modelname", description = "modelname")@RequestParam("modelname")String modelname,
                                 @Parameter(name = "modelcode", description = "modelcode")@RequestParam("modelcode")String modelcode,
                                 @Parameter(name = "modeldes", description = "modeldes")@RequestParam("modeldes")String modeldes,
                                 @Parameter(name = "modelcategory", description = "modelcategory")@RequestParam("modelcategory")String modelcategory,
                                 @Parameter(name = "modelstatus", description = "modelstatus")@RequestParam("modelstatus")String modelstatus,
                                 @Parameter(name = "modelreminder", description = "modelreminder")@RequestParam("modelreminder")String modelreminder,
                                 @Parameter(name = "connectionstrings", description = "connectionstrings")@RequestParam("connectionstrings")String connectionstrings,
                                 @Parameter(name = "modelstep1", description = "modelstep1")@RequestParam("modelstep1")String modelstep1,
                                 @Parameter(name = "modelstep2", description = "modelstep2")@RequestParam("modelstep2")String modelstep2,
                                 @Parameter(name = "modelstep3", description = "modelstep3")@RequestParam("modelstep3")String modelstep3,
                                 @Parameter(name = "modelstep4", description = "modelstep4")@RequestParam("modelstep4")String modelstep4,
                                 @Parameter(name = "modelstep5", description = "modelstep5")@RequestParam("modelstep5")String modelstep5,
                                 @Parameter(name = "exeinterval", description = "exeinterval")@RequestParam("exeinterval")String exeinterval,
                                 @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch
                                 ) throws ClassNotFoundException, Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        MonitorModel model = new MonitorModel();
        if (modelid != null) {
            model = iMonitorModelService.getById(modelid);
        }
        model.setModelname(modelname);
        model.setModelcode(modelcode);
        model.setModeldes(modeldes);
        model.setModelcategory(modelcategory);
        model.setModelstatus(modelstatus);
        model.setModelreminder(modelreminder);
        model.setConnectionstrings(connectionstrings);
        model.setModelstep1(modelstep1);
        model.setModelstep2(modelstep2);
        model.setModelstep3(modelstep3);
        model.setModelstep4(modelstep4);
        model.setModelstep5(modelstep5);
        model.setExeinterval(exeinterval);
        model.setInmodeldb(MonitorModel.IS_HY0.toString());
        Map map = new HashMap();
        iMonitorModelService.updateById(model);
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理-删除
     *
     * @param
     * @return
     */
    @Operation(summary = "model_del")
    @GetMapping(value = "/mxjk/model_del")
    public JsonBean model_del(@Parameter(name = "selectedid", description = "selectedid")@RequestParam("selectedid")String selectedid,
                              @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")String orgId,
                              @Parameter(name = "choiceSearch", description = "choiceSearch")@RequestParam("choiceSearch")String choiceSearch
                              ) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedid != null) {
            iMonitorModelService.removeById(selectedid);
        }
        //为页面查找区域显隐藏赋值
        if(choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("orgId", orgId );
        map.put("choiceSearch", choiceSearch );
        return new JsonBean(200,"成功",map);
    }




    @Operation(summary = "mxjksave")
    @GetMapping(value = "/mxjk/save")
    public JsonBean mxjksave(@Parameter(name = "dest", description = "dest")@RequestParam("dest")String dest,
                             @Parameter(name = "worksheetid", description = "worksheetid")@RequestParam("worksheetid")String worksheetid,
                             @Parameter(name = "attributeOrgid", description = "attributeOrgid")@RequestParam("attributeOrgid")String attributeOrgid) throws Exception {
    	
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
//        String path = request.getSession().getServletContext().getRealPath("/WEB-INF/data/excel/");
//        String name = FxglUtil.getTimeString() + ".xls";
//        String file = path + "/" + name;
//        String rFile = "/data/excel/" + name;
        //这段代码买看到干什么了
        int attid = 91500;
        Attachment a = iAttachmentService.getById("" + attid);
        Map map = new HashMap();
        if (dest != null) {
            if (dest.equals("dg")) {
            } else if (dest.equals("dgfj")) {
                Worksheet work = iWorksheetService.getById(new BigDecimal(worksheetid));
                map.put("worksheet", work);
            } else if (dest.equals("yd")) {
            } else if (dest.equals("qx")) {
                QueryWrapper<BugCriterion> wrapper = new QueryWrapper<>();
                wrapper.eq("ORGID",attributeOrgid);//表中无此字段
                List<BugCriterion> list1 = iBugCriterionService.list(wrapper);
                map.put("list", list1);
            } else if (dest.equals("wt")) {
            }
        }
        map.put("a", a);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "mxjkxzj")
    @GetMapping(value = "/mxjk/xzdgh")
    public JsonBean mxjkxzj(@Parameter(name = "pager", description = "pager")@RequestBody()ListPager pager,
                            @Parameter(name = "page", description = "page")@RequestParam("page")String page) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        List list = iWorksheetService.list();
        pager = new ListPager(list);
        map.put("dpPager", pager);
        map.put("pager", pager);
        map.put("page", page);
        return new JsonBean(200,"成功",map);
    }

    @Operation(summary = "mxjk")
    @GetMapping(value = "/mxjk/xzdg")
    public JsonBean mxjk(@Parameter(name = "page", description = "page")@RequestParam("page")String page,
                         @Parameter(name = "selectProjectid", description = "selectProjectid")@RequestParam("selectProjectid")String selectProjectid) throws Exception {
    	TblStaffUtil loginstaff = userProvider.get();
 		if (loginstaff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        map.put("page",page);
        map.put("worksheetid",selectProjectid);
        return new JsonBean(200,"成功",map);
    }








}
