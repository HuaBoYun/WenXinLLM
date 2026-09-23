package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.service.IAccbookService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.service.IMonitorModelresultService;
import com.huabo.cybermonitor.service.IMonitorSolutionresultService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IWarningResultService;
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
 * 模型监控-模型管理api接口
 * @createTime 2022/8/11
 */

@RestController
@Slf4j
@Tag(name="模型监控-模型管理",description="模型监控-模型管理")
@RequestMapping(value = "/cyber/MxglController")
@SuppressWarnings("all")
public class MxglController {

    private static final Logger logger = LoggerFactory.getLogger(MxglController.class);

    @Autowired
    IOrganizationService iOrganizationService;

    @Autowired
    IMonitorModelService iMonitorModelService;

    @Autowired
    CJobTaskService cJobTaskService;

    @Autowired
    JobTaskService jobTaskService;

    @Autowired
    IMonitorSolutionresultService iMonitorSolutionresultService;

    @Autowired
    IMonitorModelresultService iMonitorModelresultService;

    @Autowired
    IAccbookService iAccbookService;

    @Autowired
    IWarningResultService iWarningResultService;
    
    @Resource
    private UserProvider userProvider;


    /**
     * 模型管理-左边
     *
     * @param
     * @return
     */
    @Operation(summary = "mx_left")
    @GetMapping(value = "/mxjk/mx_left")
    public JsonBean mx_left( @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {

        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}

            Map<String, Object> map = new HashMap<>();
            map.put("userOrgId",staff.getOrgid());
            map.put("orgid",staff.getCurrentOrg().getOrgid());
            map.put("targetFrame", "mainFramex");

            return new JsonBean(200, "success", map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","没有拿到token");


    }

    /**
     * 模型管理-列表
     *
     * @param
     * @return
     *                String orgId = request.getParameter("orgId");
     * 		String modelid = request.getParameter("modelid");
     * 		String modelmame = request.getParameter("modelname");
     * 		String number = request.getParameter("pageNumber");
     */
    @Operation(summary = "modelmgmt")
    @GetMapping(value = "/mxjk/modelmgmt")
    public JsonBean modelmgmt(@Parameter(name = "orgId", description = "orgId")@RequestParam(value="orgId",required = false)String orgId,
                              @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")String modelid,
                              @Parameter(name = "modelname", description = "modelname")@RequestParam("modelname")String modelname,
                              @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}

            Map map = new HashMap();
            Boolean isSelect = false;
            QueryWrapper<MonitorModel> wrapper = new QueryWrapper<>();
            IPage<MonitorModel> a = new Page<>(pageNumber, ConstClass.DEFAULT_SIZE);

            if(staff.getCurrentOrg().getOrgid().equals(staff.getLinkOrg().getOrgid())){
                if (StringUtils.isBlank(orgId)) {
                    orgId = staff.getLinkDetp().getOrgid().toString();
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
                    orgId = staff.getCurrentOrg().getOrgid().toString();
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

            return new JsonBean(200,"成功",map);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","没有拿到token");
    }


    /**
     * 模型管理-跳转添加
     *
     * @return
     */
    @Operation(summary = "to_model_add")
    @GetMapping(value = "/mxjk/to_model_add")
    public JsonBean to_model_add(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        TblOrganizationUtil attribute = null;
        TblOrganizationUtil attribute1 = null;
        TblStaffUtil staff  = null;
        try {
        	staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            //选则的机构
            attribute = staff.getCurrentOrg();
            //当前用户的机构
            attribute1 = staff.getLinkOrg();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Organization organization = iOrganizationService.getById(staff.getLinkDetp().getOrgid().toString());
        Map<String,Object> mv = new HashMap<>();

        QueryWrapper<Accbook> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("orgid",attribute.getOrgid());
        queryWrapper.inSql("BOOKID","SELECT BOOKID FROM TBL_MANAGE_USER_BOOK WHERE STAFFID = "+staff.getStaffid());

        List<Accbook> accbookList = iAccbookService.list(queryWrapper);
        List<Accbook> books = getAllDepartmentss(accbookList);
        mv.put("books", books);
        mv.put("user", staff);
        mv.put("organization", organization);

        return new JsonBean(200,"跳转成功",mv);
    }


    public static List<Accbook> getAllDepartmentss(List<Accbook> tolist) {
        List<Accbook> list = new ArrayList<Accbook>();
        walkDepartmentListall(tolist,"┣",list);
        return list;
    }

    private static void  walkDepartmentListall(Collection<Accbook> toList, String prefix, List<Accbook> list){
        for(Accbook top:toList){
            //顶级部门
            Accbook copy = new Accbook();//使用副本，因为原对象在Session中
            copy.setBookid(top.getBookid());
            copy.setBookname(prefix+top.getBookname());
            list.add(copy);
            //子树
            walkDepartmentListall(top.getChildren(),"　"+prefix,list);
        }
    }


    private static void  walkDepartmentList(Collection<Accbook> toList,String prefix,List<Accbook> list){
        for(Accbook top:toList){
            //顶级部门
            Accbook copy = new Accbook();//使用副本，因为原对象在Session中
            copy.setId(top.getId());
            copy.setAcctname(prefix+top.getAcctname());
            list.add(copy);
            //子树
            walkDepartmentList(top.getChildren(),"　"+prefix,list);
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
    public JsonBean mxgl_findOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId")@RequestParam("nodeId") BigDecimal nodeId,
                                                @Parameter(name = "type", description = "type")@RequestParam("type")String type,
                                                @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")BigDecimal orgId,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
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
     * 行业模型库---》列表页--->跳往添加页
     * @param orgid
     * @param modelid

     * @return
     */
    @Operation(summary = "行业模型库---》列表页--->跳往添加页")
    @GetMapping(value = "/to_hy_model_add")
    public JsonBean to_hy_model_add(@Parameter(description="模型编号")@RequestParam("orgid")String orgid,
                                    @Parameter(description="modeid")@RequestParam("modelid")String modelid,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
     			return ResponseFormat.retParam(0, 20006, null);
     		}
            MonitorModel model = new MonitorModel();

            if (StringUtils.isNotEmpty(modelid)) {
                model = iMonitorModelService.getById(modelid);
            }
            Map<String,Object> mv = new HashMap<>(3);
            mv.put("orgid", orgid);
            mv.put("model", model);
            mv.put("username", staff.getRealname());
            return new JsonBean(200,"成功",mv);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonBean(200,"failure","没有拿到token");

    }

    /**
     * 模型管理-添加
     * 2022/09/03 修改 500问题 优化页面添加功能
     */
    @Operation(summary = "model_add")
    @PostMapping(value = "/mxjk/model_add")
    public JsonBean model_add(
            @Parameter(name = "modelcode", description = "模型编号", required = true) @RequestParam(value = "modelcode",required = true) String modelcode,
            @Parameter(name = "modelname", description = "模型名称", required = true) @RequestParam(value = "modelname",required = true) String modelname,
            @Parameter(name = "orgid", description = "组织机构编号", required = true) @RequestParam(value = "orgid",required = true) String orgid,
            @Parameter(name = "orgname", description = "组织名称", required = false) @RequestParam(value = "orgname",required = false) String orgname,
            @Parameter(name = "modelcategory", description = "模型分类", required = true) @RequestParam(value = "modelcategory",required = true) String modelcategory,
            @Parameter(name = "modelstatus", description = "状态", required = true) @RequestParam(value = "modelstatus",required = true) String modelstatus,
            @Parameter(name = "staffid", description = "创建人帐号", required = true) @RequestParam(value = "staffid",required = true) String staffid,
            @Parameter(name = "creater", description = "创建人姓名", required = false) @RequestParam(value = "creater",required = false) String creater,
            @Parameter(name = "modelreminder", description = "提醒方式", required = false) @RequestParam(value = "modelreminder",required = false) String modelreminder,
            @Parameter(name = "connectionstrings", description = "连接串", required = true) @RequestParam(value = "connectionstrings",required = true) String connectionstrings,
            @Parameter(name = "modeldes", description = "描述", required = false) @RequestParam(value = "modeldes",required = false) String modeldes,
            @Parameter(name = "modelstep1", description = "步骤1", required = false) @RequestParam(value = "modelstep1",required = false) String modelstep1,
            @Parameter(name = "modelstep2", description = "步骤2", required = false) @RequestParam(value = "modelstep2",required = false) String modelstep2,
            @Parameter(name = "modelstep3", description = "步骤3", required = false) @RequestParam(value = "modelstep3",required = false) String modelstep3,
            @Parameter(name = "modelstep4", description = "步骤4", required = false) @RequestParam(value = "modelstep4",required = false) String modelstep4,
            @Parameter(name = "modelstep5", description = "步骤5", required = false) @RequestParam(value = "modelstep5",required = false) String modelstep5,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception{
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = new MonitorModel();
        model.setCreatedate(LocalDateTime.now());
        System.out.println(model.getCreatedate());
        model.setRunstatus(0);
        model.setSubsystem(1);
        model.setInmodeldb(MonitorModel.IS_HY0.toString());
        model.setModelcode(modelcode);
        model.setModelname(modelname);
        model.setOrgid(orgid);
        model.setModelcategory(modelcategory);
        model.setModelstatus(modelstatus);
        model.setStaffid(staffid);
        model.setModelreminder(staffid);
        model.setConnectionstrings(connectionstrings);
        model.setModeldes(modeldes);
        model.setModelstep1(modelstep1);
        model.setModelstep2(modelstep2);
        model.setModelstep3(modelstep3);
        model.setModelstep4(modelstep4);
        model.setModelstep5(modelstep5);

        boolean ist = iMonitorModelService.save(model);

        return new JsonBean(ist?200:500,String.valueOf(ist),ist);
    }

    /**
     * 模型管理-删除权限判断
     *
     * @param
     * @return
     */
    @Operation(summary = "modelcheckupdate")
    @GetMapping(value = "/mxjk/modelcheckupdate", produces = "application/json; charset=utf-8")
    public JsonBean modelcheckupdate(@Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")BigDecimal modelId,
                                     @Parameter(name = "Staffid", description = "Staffid")@RequestParam("Staffid")String Staffid,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel rule = iMonitorModelService.getById(modelId);
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
     * 模型管理-修改
     *
     * @param
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    @Operation(summary = "model_modify")
    @PostMapping(value = "/mxjk/model_modify")
    public JsonBean model_modify(@Parameter(name = "modelid", description = "modelid")@RequestParam(value="modelid",required = false)String modelid,
                                 @Parameter(name = "modelname", description = "modelname")@RequestParam(value="modelname",required = false)String modelname,
                                 @Parameter(name = "modelcode", description = "modelcode")@RequestParam(value="modelcode",required = false)String modelcode,
                                 @Parameter(name = "modeldes", description = "modeldes")@RequestParam(value="modeldes",required = false)String modeldes,
                                 @Parameter(name = "modelcategory", description = "modelcategory")@RequestParam(value="modelcategory",required = false)String modelcategory,
                                 @Parameter(name = "modelstatus", description = "modelstatus")@RequestParam(value="modelstatus",required = false)String modelstatus,
                                 @Parameter(name = "modelreminder", description = "modelreminder")@RequestParam(value="modelreminder",required = false)String modelreminder,
                                 @Parameter(name = "connectionstrings", description = "connectionstrings")@RequestParam(value="connectionstrings",required = false)String connectionstrings,
                                 @Parameter(name = "modelstep1", description = "modelstep1")@RequestParam(value="modelstep1",required = false)String modelstep1,
                                 @Parameter(name = "modelstep2", description = "modelstep2")@RequestParam(value="modelstep2",required = false)String modelstep2,
                                 @Parameter(name = "modelstep3", description = "modelstep3")@RequestParam(value="modelstep3",required = false)String modelstep3,
                                 @Parameter(name = "modelstep4", description = "modelstep4")@RequestParam(value="modelstep4",required = false)String modelstep4,
                                 @Parameter(name = "modelstep5", description = "modelstep5")@RequestParam(value="modelstep5",required = false)String modelstep5,
                                 @Parameter(name = "exeinterval", description = "exeinterval")@RequestParam(value="exeinterval")String exeinterval,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws ClassNotFoundException, Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
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

        return new JsonBean(200,"成功",null);
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
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        if (selectedid != null) {
            iMonitorModelService.removeById(selectedid);
        }

        map.put("orgId", orgId );

        return new JsonBean(200,"成功",map);
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
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {

        	TblStaffUtil staff = userProvider.get();
     		if (staff == null) {
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
                    mxTaskExec = this.cJobTaskService.mxTaskExec(list, null, staff);
                    model.setRunstatus(1);
                } catch (SchedulerException e) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200,"failure","没有拿到token");


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
                               @Parameter(description="模型类型")@RequestParam("type")String type,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        logger.info("行业模型库-左侧菜单");
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
     * 复制到行业模型库 选定操作
     */
//    @GetMapping(value = "/mx/copyMx")
//    @Operation(summary = "行业模型库---》左侧菜单")
//    public JsonBean copyMx(){
//        Map map = new HashMap();
//        return new JsonBean(200,"成功",map);
//    }

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
                                @Parameter(name = "modelid", description = "114705")@RequestParam("modelid")String modelid,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        IPage ip = new Page<>(pageNumber,ConstClass.DEFAULT_SIZE);
        iMonitorModelresultService.queryPage(ip,modelid);
        Map map = new HashMap();

        map.put("pageBean", ip);
        map.put("modelid", modelid);

        return new JsonBean(200,"成功",map);
    }

    /**
     * 模型管理 --- 二级页面当中的结果
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
    public JsonBean resultmodelInfo(@Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", name = "pageNumber", defaultValue = "1") Integer pageNumber,
                                    @Parameter(name = "modelId", description = "modelId传soluId时为modelId,不传soluId时为resultid") @RequestParam(value = "modelId")  BigDecimal modelId,
                                    @Parameter(name = "soluId", description = "soluId非必填项,看模块")   @RequestParam(value = "soluId",required = false)  BigDecimal soluId,
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
            if (modelResult!=null) {
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
        map.put("list",list);



        return new JsonBean(200, "成功", map);
    }

    /**
     * 模型管理 --第三级页面的详细结果
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
    public JsonBean resultmodelInfo1(@Parameter(name = "index", description = "index")@PathVariable("index")String index,
                                     @Parameter(name = "signId", description = "signId")@PathVariable("signId")String signId,
                                     @Parameter(name = "modelId", description = "modelId")@PathVariable("modelId")BigDecimal modelId,
                                     @Parameter(name = "source", description = "传0或者1,ZJ=0,YJYJ=1")@PathVariable("source")Integer source,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = null;
        if (source == MonitorModelresult.ZJ) {
            MonitorModelresult modelResult = iMonitorModelresultService.getById(modelId);

            model = iMonitorModelService.getById(modelResult.getModelid());
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
        DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
        Map<String, Object> map1 = dynamicDataSource.getData(tableName, signId, pageNumber, 20,"");
        //       try {
//            map = this.hibernateExecuteService.getDate(tableName, acctbook.getAcctid(), signId,
//                    pageNumber, pageBean.getPageSize(), "");
        map.put("column", map1.get("column"));
        map.put("pageBean", map1.get("data"));
        map.put("index", index);
        // map.put("fhtype", fhtype);
        // map.put("orgId", orgId);
        // map.put("modelid", modelid);
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
    public JsonBean resultmodelInfo2(@Parameter(name = "modelId", description = "modelId")@PathVariable("modelId")BigDecimal modelId,
                                     @Parameter(name = "soluId", description = "soluId")@PathVariable("soluId")BigDecimal soluId,
                                     @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value ="pageNumber",name="pageNumber",defaultValue = "1")Integer pageNumber,
                                     @Parameter(name = "limit", description = "limit")@RequestParam(value ="limit",name="limit",defaultValue = "15")Integer limit,
                                     @Parameter(name = "modelIds", description = "modelIds")@RequestParam("modelIds")BigDecimal modelIds,
                                     @Parameter(name = "fhtype", description = "fhtype")@RequestParam("fhtype")BigDecimal fhtype,
                                     @Parameter(name = "orgId", description = "orgId")@RequestParam("orgId")BigDecimal orgId,
                                     @Parameter(name = "modelid", description = "modelid")@RequestParam("modelid")BigDecimal modelid,
                                     @Parameter(name = "solutionResultId", description = "solutionResultId")@RequestParam("solutionResultId")BigDecimal solutionResultId,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
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
        DynamicDataSource dynamicDataSource = new DynamicDataSource(acctbook.getAcctid());
        Map<String, Object> map1 = dynamicDataSource.getData(tableName, modelResult.getSignid(), pageNumber, 20,"");

        //       try {
//           map = this.hibernateExecuteService.getDate(tableName, acctbook.getAcctid(),
//                    modelResult.getSignId(), pageNumber, pageBean.getPageSize(), "");
        map.put("column", map1.get("column"));
        map.put("pageBean", map1.get("data"));
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
//----------------------------------------------------------------------------------------------
    /**
     * 修改模型管理的状态
     *
     * @param
     * @return
     */
    @Operation(summary = "modelUpdateStatus")
    @GetMapping(value = "/mxjk/modelUpdateStatus")
    public JsonBean modelUpdateStatus(@Parameter(name = "modelId", description = "modelId")@RequestParam("modelId")BigDecimal modelId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        MonitorModel model = iMonitorModelService.getById(modelId);
        String status = model.getModelstatus();
        if (status.equalsIgnoreCase("启用")){
            model.setModelstatus("禁用");
        }
        else if (status.equalsIgnoreCase("禁用")){
            model.setModelstatus("启用");
        }
        return new JsonBean(200,"成功",iMonitorModelService.updateById(model));
    }
}
