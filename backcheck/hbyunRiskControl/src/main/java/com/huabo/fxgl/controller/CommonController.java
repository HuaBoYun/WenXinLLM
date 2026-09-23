package com.huabo.fxgl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Flow;
import com.huabo.fxgl.entity.FlowBussiness;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IFlowBussinessService;
import com.huabo.fxgl.service.IFlowService;
import com.huabo.fxgl.service.IInnerruleService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IOuterruleService;
import com.huabo.fxgl.service.IRiskService;
import com.huabo.fxgl.service.IRiskcategoryService;
import com.huabo.fxgl.util.FtpUtil;
import com.huabo.fxgl.util.FxglUtil;
import com.huabo.fxgl.util.HttpClient;
import com.huabo.fxgl.util.Tree;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 行业API控制器
 * <p>提供行业相关的公共API接口</p>
 *
 * @author hbyun
 */
@Tag(name="行业API",description="行业API")
@RestController
@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
public class CommonController {

    public static final String formurl = ResourceBundle.getBundle("setting/jdbc").getString("formurl").toString();
    private static final String activitiModelerUrl = ResourceBundle.getBundle("setting/jdbc").getString("activitiModelerUrl").toString();

    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private IRiskService riskService;
    @Autowired
    private IFlowService flowService;
    @Autowired
    private IControlmatrixService controlmatrixService;
    @Autowired
    private IRiskcategoryService riskcategoryService;
    @Autowired
    private IFlowBussinessService flowBussinessService;
    @Autowired
    private IInnerruleService innerruleService;
    @Autowired
    private IOuterruleService outerruleService;
    
    @Resource
    private UserProvider userProvider;


    @Value("${file.path}")
    private String path;

    @OperationLog(
            success = "行业复制功能中的查看流程分析内规列表处理成功",
            busType = "行业",
            fail = "行业复制功能中的查看流程分析内规列表处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/innerrule")
    @Operation(summary = "行业复制功能中的查看流程分析内规列表 /ywlc/processAnalysis/innerrule")
    public JsonBean innerruleList(HttpServletRequest request,
                                    @Parameter(name="flowid",description="行业业务流程库中选择的行业流程ID, 必填",required=true) @RequestParam(required = true)String flowid,
                                    @Parameter(name="isFlowdb",description="固定值，默认传1",required=true) @RequestParam(required = true) String isFlowdb,
                                   @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                                   @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        IPage page = new Page(pageNo, pageSize);
        innerruleService.findInnerRuleByFlow(page, flowid);

        return new JsonBean(1, "success", page);
    }

    @OperationLog(
            success = "行业复制功能中的查看流程分析外规列表处理成功",
            busType = "行业",
            fail = "行业复制功能中的查看流程分析外规列表处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/outerrule")
    @Operation(summary = "行业复制功能中的查看流程分析外规列表 /ywlc/processAnalysis/outerrule")
    public JsonBean outerruleList(HttpServletRequest request,
                                   @Parameter(name="flowid",description="行业业务流程库中选择的行业流程ID, 必填",required=true) @RequestParam(required = true)String flowid,
                                   @Parameter(name="isFlowdb",description="固定值，默认传1",required=true) @RequestParam(required = true) String isFlowdb,
                                   @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
                                   @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        IPage page = new Page(pageNo, pageSize);
        outerruleService.findOuterRuleByFlow(page, flowid);
        return new JsonBean(1, "success", page);
    }

    @OperationLog(
            success = "行业复制功能中的查看流程分析详情处理成功",
            busType = "行业",
            fail = "行业复制功能中的查看流程分析详情处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/dispaddanalysis")
    @Operation(summary = "行业复制功能中的查看流程分析详情 /ywlc/processAnalysis/dispaddanalysis")
    public JsonBean dispaddanalysis(HttpServletRequest request,
                                 @Parameter(name="flowid",description="行业业务流程库中选择的行业流程ID, 必填",required=true) @RequestParam(required = true)String flowid,
                                 @Parameter(name="isFlowdb",description="固定值，默认传1",required=true) @RequestParam(required = true) String isFlowdb,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        Flow flow = flowService.getById(flowid);
        if (flow == null) {
            return new JsonBean(0, "该flowid在系统中不存在，无法继续", null);
        }

        if (StringUtils.isNotEmpty(flow.getCompany())) {
            BigDecimal oid = new BigDecimal(flow.getCompany());
            flow.setCompanyName(organizationService.getById(oid).getOrgname());
        }
        if (StringUtils.isNotEmpty(flow.getDepartincharge())) {
            BigDecimal oid = new BigDecimal(flow.getDepartincharge());
            flow.setDepartinchargeName(organizationService.getById(oid).getOrgname());
        }
        if (StringUtils.isNotEmpty(flow.getDepartassist())) {
            String[] orgIdList = flow.getDepartassist().split(",");
            StringBuilder sb = new StringBuilder();
            for (int i=0; i<orgIdList.length; i++) {
                Organization organ = organizationService.getById(orgIdList[i]);
                if (organ!=null) {
                    sb.append(organ.getOrgname());
                    if (i<orgIdList.length-1) {
                        sb.append(",");
                    }
                }
            }
            flow.setDepartassistName(sb.toString());
        }

        //根据flowid查找风险控制点信息
        String tcmId = flowService.findControlMatrixIdUniqueByFlowid(flow.getFlowid());
        Controlmatrix tcl = controlmatrixService.getById(tcmId);
        //根据flowid查找riskBussiness
        FlowBussiness riskBussiness = flowBussinessService.findUniqueByFlowId(flow.getFlowid());
        //根据flowid查找Risk
        Risk risk = riskService.findTblRiskByFlowId(flowid);

        Map resultMap = new HashMap();
        resultMap.put("risk", risk);
        resultMap.put("riskBussiness", riskBussiness);
//        mv.addObject("flows", flows);
        resultMap.put("controlMatrix", tcl);
        resultMap.put("flowname", flow.getFlowname());

        resultMap.put("flow", flow);
        resultMap.put("isFlowdb", isFlowdb);

        return new JsonBean(1, "success", resultMap);
    }

    @OperationLog(
            success = "发送至行业功能中点击选中完成复制处理成功",
            busType = "行业",
            fail = "发送至行业功能中点击选中完成复制处理失败",
            operationType = OperationType.ADD,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/to_ywlc", produces = "application/json; charset=utf-8")
    @Operation(summary = "发送至行业功能中点击选中完成复制 /ywlc/processAnalysis/to_ywlc")
    public JsonBean to_ywlc(HttpServletRequest request,
                                @Parameter(name="orgid",description="选中的行业（如：建筑行业）ID，必填",required=true) @RequestParam(required = true)String orgid,
                                @Parameter(name="riskid",description="选中了要发送至行业的风险ID，必填",required=true) @RequestParam(required = true)String riskid,
                                @Parameter(name="faflowid",description="选中的行业流程（如：建筑行业）ID，可省略",required=false) @RequestParam(required = false)String faflowid,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if (StringUtils.isEmpty(faflowid)) {
            faflowid = "0";
        }

        // 复制前对象
        Risk oldrisk = riskService.getById(riskid);
        if (oldrisk==null) {
            return new JsonBean(0, "提供的风险ID(riskid)在系统中不存在，操作无法继续", null);
        }
        Flow oldflow = flowService.findTblFlowByRiskId(riskid);
        String conmatid = riskService.selectControlMatrixId(new BigDecimal(riskid));
        Controlmatrix oldcontrolmatrix = controlmatrixService.getById(conmatid);
        FlowBussiness oldriskBussiness = flowBussinessService.findUniqueByFlowId(oldflow.getFlowid());

        // 流程
        Flow newflow = new Flow();
        newflow.setFlownumber(String.valueOf(System.currentTimeMillis()));
        newflow.setFlowname(oldflow.getFlowname());
        newflow.setFatherflowid(new BigDecimal(faflowid));
        newflow.setVersion(new BigDecimal(1));
        newflow.setInflowdb(new BigDecimal(1));

        if (oldflow.getFlowchart() != null && !oldflow.getFlowchart().equals("")) {
//            logger.info("添加流程图");
//            String path = request.getSession().getServletContext().getRealPath("/WEB-INF/");
            File file = new File(path + oldflow.getFlowchart());
            String url = "/data/flow/" + FxglUtil.getTimeString() + ".xml";

            File file1 = new File(path + url);
//            logger.info("判断流程是否有流程图");
            if (file.exists()) {
                fileChannelCopy(file, file1);
                InputStream input = new FileInputStream(path + url);
                FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
                newflow.setFlowchart(url);
            }
            InputStream input = new FileInputStream(path + url);
            FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
        }

        // copy
        String newflowid = flowService.copyfromRiskToFlow(oldrisk, oldflow, newflow, oldcontrolmatrix, oldriskBussiness,
                faflowid, orgid);

        // 风险关联自定义表单
        HashMap<String, Object> fields = new HashMap<String, Object>(0);
        fields.put("oldRiskid", oldflow.getFlowid());
        fields.put("newRiskid", newflowid);
        try {
            String result = HttpClient.request(formurl + "/form/copyRiskFormByNewRisk", fields, null);
            result = HttpClient.request(activitiModelerUrl + "/activitiModuleCopy", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map result = new HashMap();
        result.put("newflowid", newflowid);

        return new JsonBean(1, "复制成功", result);
    }

    @OperationLog(
            success = "行业复制功能中的选择行业业务流程库完成复制处理成功",
            busType = "行业",
            fail = "行业复制功能中的选择行业业务流程库完成复制处理失败",
            operationType = OperationType.ADD,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/to_ywlc_copy", produces = "application/json; charset=utf-8")
    @Operation(summary = "行业复制功能中的选择行业业务流程库完成复制 /ywlc/processAnalysis/to_ywlc_copy")
    public JsonBean to_ywlc_copy(HttpServletRequest request,
            @Parameter(name="flowid",description="行业业务流程库中选择的行业流程ID, 必填",required=true) @RequestParam(required = true)String flowid,
            @Parameter(name="riskcatid",description="选择的风险类别ID, 必填",required=true) @RequestParam(required = true) String riskcatid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        Flow oldFlow = flowService.getById(flowid);
        if (oldFlow == null) {
            return new JsonBean(0, "该flowid在系统中不存在，无法继续", null);
        }
        Risk oldRisk = riskService.findTblRiskByFlowId(flowid);

        Riskcategory oldCate = riskcategoryService.getById(riskcatid);
        if (oldCate == null) {
            return new JsonBean(0, "该riskcatid在系统中不存在，无法继续", null);
        }
        Controlmatrix oldControlmatrix = controlmatrixService.getByFlowId(flowid);
        FlowBussiness oldRiskBussiness = flowBussinessService.findUniqueByFlowId(new BigDecimal(flowid));

        // 流程
        Flow newflow = new Flow();
        newflow.setFlownumber(oldFlow.getFlownumber());
        newflow.setFlowname(oldFlow.getFlowname());
        newflow.setFatherflowid(new BigDecimal(riskcatid));
        newflow.setVersion(new BigDecimal(1));
//        newflow.setInflowdb(null);
        newflow.setInflowdb(oldFlow.getInflowdb());

        if (oldFlow.getFlowchart() != null && !oldFlow.getFlowchart().equals("")) {
//            logger.info("添加流程图");
//            String path = request.getSession().getServletContext().getRealPath("/static/flow/");
            if (!new File(path + "/data/flow/").exists()) {
                new File(path + "/data/flow/").mkdirs();
            }
            File file = new File(path + oldFlow.getFlowchart());
            String url = "/data/flow/" + FxglUtil.getTimeString() + ".xml";

            File file1 = new File(path + url);
//            logger.info("判断流程是否有流程图");
            if (file.exists()) {
                fileChannelCopy(file, file1);
                InputStream input = new FileInputStream(path + url);
                FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
                newflow.setFlowchart(url);
            }
//            InputStream input = new FileInputStream(path + url);
//            FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
        }

        String newRiskid = flowService.copyfromFlowToRisk(oldRisk, oldFlow, newflow, oldControlmatrix,
                oldRiskBussiness, oldCate, riskcatid, selectOrg.getOrgid().toString());
        // 风险关联自定义表单
        HashMap<String, Object> fields = new HashMap<String, Object>(0);
        fields.put("oldRiskid", flowid);
        fields.put("newRiskid", newflow.getFlowid());
        try {
            String result = HttpClient.request(formurl + "/form/copyRiskFormByNewRisk", fields, null);
            result = HttpClient.request(activitiModelerUrl + "/activitiModuleCopy", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new JsonBean(1, "success", null);
    }



    @OperationLog(
            success = "行业复制功能中的行业业务流程库查询处理成功",
            busType = "行业",
            fail = "行业复制功能中的行业业务流程库查询处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/listanalysishy_copy", produces = "application/json; charset=utf-8")
    @Operation(summary = "行业复制功能中的行业业务流程库查询 /ywlc/processAnalysis/listanalysishy_copy")
    public JsonBean listanalysishy_copy(
            @Parameter(name="moduletype",description="模块类型，默认为fxcj",required=true) @RequestParam(required = true) String moduletype,
            @Parameter(name="flowid",description="行业流程树中选择的行业流程ID",required=false) @RequestParam(required = false)String flowid,
            @Parameter(name="flowname",description="查找时填写的流程名称",required=false) @RequestParam(required = false)String flowname,
            @Parameter(name="flownumber",description="查找时填写的流程编号",required=false) @RequestParam(required = false)String flownumber,
            @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        String orgid = selectOrg.getOrgid().toString();
        if (StringUtils.isNotEmpty(flowid)) {
            Flow flow = flowService.getById(flowid);
            orgid = flow.getCompany();
        }

        IPage page = flowService.hyFlowPage(orgid, flowid, flowname, flownumber, pageNo, pageSize);

        Map resultMap = new HashMap();
        resultMap.put("page", page);
        resultMap.put("pageNo", pageNo);
        resultMap.put("pageSize", pageSize);
        resultMap.put("flowid", flowid);
        resultMap.put("flowname", flowname);
        resultMap.put("flownumber", flownumber);


        return new JsonBean(1, "success", resultMap);
    }

    ///nbkz/ywlc/processAnalysis/leftanalysishy_copy?faflowid=120216&moduletype=fxcj
    @OperationLog(
            success = "行业复制功能中的行业流程树查询处理成功",
            busType = "行业",
            fail = "行业复制功能中的行业流程树查询处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/leftanalysishy_copy", produces = "application/json; charset=utf-8")
    @Operation(summary = "行业复制功能中的行业流程树 /ywlc/processAnalysis/leftanalysishy_copy")
    public List<Tree> leftanalysishy_copy(
            @Parameter(name="moduletype",description="模块类型，默认为fxcj",required=true) @RequestParam(required = true) String moduletype,
            @Parameter(name="orgid",description="行业ID，可以为空",required=false) @RequestParam(required = false)String orgid,
//            @Parameter(name = "faflowid", description = "行业流程ID，可以为空，默认为当前选中的风险类别ID") @RequestParam(required = false)String faflowid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        String findOrgid = null;
        if (StringUtils.isNotBlank(orgid)) {
            findOrgid = orgid;
        } else {
            Organization firstHyOrg = organizationService.getHYFirst();
            findOrgid = firstHyOrg.getOrgid().toString();
        }
        List<Tree> trees = null;
        String treeName = "";
        if (StringUtils.isNotEmpty(findOrgid)) {
            trees = flowService.flowtreehy(findOrgid);
            treeName = organizationService.getById(findOrgid).getOrgname();
        } else {
            trees = new LinkedList<>();
        }
//        Map result = new HashMap();
//        result.put("tree", trees);
//        result.put("treeName", treeName);
//
//        result.put("orgid", findOrgid);
//        result.put("faflowid", faflowid);
//        result.put("moduletype", moduletype);
        return trees;
    }

    @OperationLog(
            success = "行业复制功能中的行业树查询处理成功",
            busType = "行业",
            fail = "行业复制功能中的行业树查询处理失败",
            operationType = OperationType.SELECT,
            subType = "行业"
    )
    @RequestMapping(value = "/common/findOrganizationByTreeAll", produces = "application/json; charset=utf-8")
    @Operation(summary = "行业复制功能中的行业树 /common/findOrganizationByTreeAll")
    public List<Tree> findOrganizationByTree(@Parameter(name = "type", description = "行业类型，可以为空") @RequestParam(required = false) String type,
                                  @Parameter(name = "nodeId", description = "节点ID，可以为空") @RequestParam(required = false) String nodeId,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader(name = "token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        if (StringUtils.isBlank(nodeId) ) {
            String sql="SELECT * from TBL_ORGANIZATION where  ORGNAME='行业' and ORGTYPE=100 ORDER BY orderid ASC";
            QueryWrapper<Organization> queryWrapper = new QueryWrapper();
            queryWrapper.eq("ORGNAME", "行业");
            queryWrapper.eq("ORGTYPE", 100);
            queryWrapper.orderByAsc("orderid");
            nodeId = organizationService.list(queryWrapper).get(0).getOrgid().toString();
        }
        String json = "";

        List<Tree> list = new LinkedList<>();
        if (StringUtils.isNotBlank(type)) {
            list = organizationService.getTreeHy(new BigDecimal(nodeId));
        } else {
            list = organizationService.getNodeAllHy(new BigDecimal(nodeId));
        }
        return list;
    }

    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();// 得到对应的文件通道
            out = fo.getChannel();// 得到对应的文件通道
            in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
