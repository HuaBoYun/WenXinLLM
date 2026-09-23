package com.huabo.fxgl.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.JudgeRoleRight;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.dto.RiskDto;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Controlmatrix;
import com.huabo.fxgl.entity.Flow;
import com.huabo.fxgl.entity.FlowBussiness;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.RiskCoping;
import com.huabo.fxgl.entity.RiskCopingCmatrix;
import com.huabo.fxgl.entity.RiskInnerrule;
import com.huabo.fxgl.entity.RiskOuterrule;
import com.huabo.fxgl.entity.Riskcategory;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.entity.TblControlEntries;
import com.huabo.fxgl.entity.TblNbsjAuditStepEntity;
import com.huabo.fxgl.mapper.AttachmentMapper;
import com.huabo.fxgl.mapper.FlowMapper;
import com.huabo.fxgl.mapper.RiskMapper;
import com.huabo.fxgl.mapper.TblControlEntriesMapper;
import com.huabo.fxgl.mapper.TblNbsjAuditStepEntityMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IControlmatrixService;
import com.huabo.fxgl.service.IFlowBussinessService;
import com.huabo.fxgl.service.IFlowService;
import com.huabo.fxgl.service.IFlowdesService;
import com.huabo.fxgl.service.IOrganizationService;
import com.huabo.fxgl.service.IRiskAssplanRiskService;
import com.huabo.fxgl.service.IRiskControlmatrixService;
import com.huabo.fxgl.service.IRiskCopingCmatrixService;
import com.huabo.fxgl.service.IRiskCopingService;
import com.huabo.fxgl.service.IRiskFlowService;
import com.huabo.fxgl.service.IRiskInnerruleService;
import com.huabo.fxgl.service.IRiskOuterruleService;
import com.huabo.fxgl.service.IRiskRiskAttService;
import com.huabo.fxgl.service.IRiskRiskeventService;
import com.huabo.fxgl.service.IRiskService;
import com.huabo.fxgl.service.IRiskeventService;
import com.huabo.fxgl.service.IRisktolerabilityService;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.impl.OuterruleServiceImpl;
import com.huabo.fxgl.service.impl.RiskcategoryServiceImpl;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;
import com.vip.vjtools.vjkit.collection.CollectionUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * 风险管控 - 风险识别 - 风险创建
 * </p>
 *
 * @author LiHongXu, YangZeGuo
 * @version 1.0.1
 * @since 2022-08-08
 * , method = {RequestMethod.GET, RequestMethod.POST}
 */

@RestController
@RequestMapping(value = "/risk")
@Tag(name="风险识别 - 风险创建",description="风险识别 - 风险创建")
@Slf4j
public class RiskController {
    @Autowired
    private RiskcategoryServiceImpl riskcategoryService;
    @Autowired
    private IRiskControlmatrixService riskControlmatrixService;
    @Autowired
    private IRiskService riskService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private RiskMapper riskMapper;

    @Resource
    RestTemplate restTemplate;

    @Autowired
    private IRiskeventService riskeventService;
    public static final String formurl = ResourceBundle.getBundle("setting/jdbc").getString("formurl").toString();
    private static final String activitiModelerUrl = ResourceBundle.getBundle("setting/jdbc").getString("activitiModelerUrl").toString();

    public static ResourceBundle mainData = ResourceBundle.getBundle("setting/yy");


    @Autowired
    private FlowMapper flowMapper;
    @Autowired
    private IRiskCopingCmatrixService riskCopingCmatrixService;

    @Autowired
    private IFlowService flowService;

    @Autowired
    private IRiskInnerruleService riskInnerruleService;

    @Autowired
    private IRiskOuterruleService riskOuterruleService;

    @Autowired
    private IRiskRiskeventService riskRiskeventService;

    @Autowired
    private IRisktolerabilityService risktolerabilityService;

    @Autowired
    private IRiskRiskAttService riskRiskAttService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IControlmatrixService controlmatrixService;

    @Autowired
    private IFlowBussinessService flowBussinessService;

    @Autowired
    private IFlowdesService flowdesService;

    @Autowired
    private IRiskFlowService riskFlowService;


    @Autowired
    private OuterruleServiceImpl outerruleService;
    @Autowired
    private IStaffService staffService;

    @Autowired
    private IRiskAssplanRiskService riskAssplanRiskService;

    @Autowired
    private IRiskCopingService copingService;

    @Autowired
    private AttachmentMapper attachmentMapper;

    @Autowired
    private TblControlEntriesMapper tblControlEntriesMapper;

    @Autowired
    private TblNbsjAuditStepEntityMapper tblNbsjAuditStepEntityMapper;

    @Resource
    private UserProvider userProvider;


    @Value("${application.administrators:}")
    private String administrators;


    /**
     * 风险创建---左侧菜单
     *
     * @return
     */
    @OperationLog(
            success = "获取左侧风险类别列表",
            busType = "风险识别",
            fail = "获取左侧风险类别列表",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @Operation(summary = "风险创建---左侧风险类别 /risk/risk_left")
    @RequestMapping(value = "/risk_left", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean risk_left(@Parameter(description="orgid") @RequestParam(required = false) String orgid,
                              @Parameter(description="treeName") @RequestParam(required = false) String treeName,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        if (orgid != null && !"".equals(orgid)) {
            Organization org = organizationService.getById(orgid);
            treeName = org.getOrgname();
            orgid = org.getOrgid().toString();

        } else {
            orgid = staffUtil.getCurrentOrg().getOrgid().toString();
        }
        //初始化三个基本的风险分类 、 "企业风险", "业务风险", "专项风险"
        riskcategoryService.initRiskCategory(orgid, Riskcategory.FXSJK);
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<Riskcategory> catTree = riskcategoryService.getRiskCateTreeByOrgId(orgid, Riskcategory.FXSJK, null);

        resultMap.put("treeName", treeName);
        resultMap.put("tree", catTree);
//        mv.addObject("moduletype", new TblRiskcategory().FXSJK);
        resultMap.put("targetFrame", "mainFramex");
        resultMap.put("orgid", orgid);
        jsonBean = ResponseFormat.retParam(1, 200, resultMap);
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
            success = "风险创建---外规---删除处理成功",
            busType = "风险识别",
            fail = "风险创建---外规---删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险创建"
    )
    @Operation(summary = "风险创建---外规---删除/delete_law_regulations")
    @RequestMapping(value = "/delete_law_regulations", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean delete_law_regulations(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) BigDecimal riskid,
            @Parameter(name = "outrulid", description = "外归主键", required = true) @RequestParam(value = "outrulid", required = true) BigDecimal outrulid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("OUTRULID", outrulid);
        queryWrapper.eq("RISKID", riskid);
        riskOuterruleService.remove(queryWrapper);
        jsonBean = ResponseFormat.retParam(1, 200, null);
        return jsonBean;
    }


    /**
     * 风险信息---外规添加
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/17
     */
    @OperationLog(
            success = "风险信息---外规添加处理成功",
            busType = "风险识别",
            fail = "风险信息---外规添加处理失败",
            operationType = OperationType.ADD,
            subType = "风险创建"
    )
    @RequestMapping(value = "/save_law_regulations", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险信息---外规添加/save_law_regulations")
    public JsonBean save_law_regulations(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) BigDecimal riskid,
                                         @Parameter(name = "outrulid", description = "外规主键", required = true) @RequestParam(value = "outrulid", required = true) BigDecimal outrulid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        RiskOuterrule riskOuterRule = new RiskOuterrule();
        riskOuterRule.setOutrulid(outrulid);
        riskOuterRule.setRiskid(riskid);
        if (riskService.isexist2(riskOuterRule) == 0) {
            riskOuterruleService.save(riskOuterRule);
        }
        return ResponseFormat.retParam(1, 200, null);
    }


    /**
     * 风险创建---内外规
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/17
     */
    @OperationLog(
            success = "风险创建---外规列表处理成功",
            busType = "风险识别",
            fail = "风险创建---外规列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/law_regulations", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险创建---外规列表/law_regulations")
    public JsonBean law_regulations(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
                                    @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                    @Parameter(name = "riskid", description = "风险创建主键Id", required = true) @RequestParam(value = "riskid", required = true, name = "riskid") String riskid,
                                    @Parameter(name = "riskcatName", description = "风险类型名称", required = false) @RequestParam(value = "riskcatName", required = false) String riskcatName,
                                    @Parameter(name = "rulename", description = "查询条件 -发文名称", required = false) @RequestParam(value = "rulename", required = false) String rulename,
                                    @Parameter(name = "rulenumber", description = "查询条件 -发文编号", required = false) @RequestParam(value = "rulenumber", required = false) String rulenumber,
                                    @Parameter(name = "bodyinfo", description = "查询条件-发文内容", required = false) @RequestParam(value = "bodyinfo", required = false) String bodyinfo) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Outerrule outerrule = new Outerrule();
        outerrule.setRulename(rulename);
        outerrule.setRulenumber(rulenumber);
        outerrule.setBodyinfo(bodyinfo);
        Page page = new Page(pageNo, pageSize);
        //查找当前公司下 以及 爬取的 所有法律法规
        IPage all = riskService.findAll(staffUtil.getCurrentOrg().getOrgid(), page, outerrule);

        //根据Tbl_risk riskId 查找所有选中的外归
        List<Outerrule> wglist = riskService.findOuterRuleByRiskId(riskid, outerrule);
        StringBuilder outRuleIds = new StringBuilder();
        // 将该风险已经映射到的 Outerrule ID返回客户端
        for (int i = 0; i < wglist.size(); i++) {
            Outerrule o = (Outerrule) wglist.get(i);
            outRuleIds.append(o.getOutrulid());
            if (i < wglist.size() - 1) {
                outRuleIds.append(",");
            }
        }
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("pageBean", all);
        result.put("riskcatName", riskcatName);
        result.put("riskid", riskid);
        result.put("selectId", outRuleIds.toString());
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    /**
     * 风险创建---内外规--跳转内规列表页
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/16
     */
    @OperationLog(
            success = "风险创建---内规列表页处理成功",
            busType = "风险识别",
            fail = "风险创建---内规列表页处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/internal_regulations", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险创建---内规列表页/internal_regulations")
    public JsonBean internal_regulations(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "riskcatName", description = "风险类型名称", required = false) @RequestParam(value = "riskcatName", required = false) String riskcatName,
            @Parameter(name = "riskid", description = "风险创建主键Id", required = true) @RequestParam(value = "riskid", required = true, name = "riskid") String riskid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "rulename", description = "查询条件 -发文名称", required = false) @RequestParam(value = "rulename", required = false) String rulename,
            @Parameter(name = "rulenumber", description = "查询条件 -发文编号", required = false) @RequestParam(value = "rulenumber", required = false) String rulenumber
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Innerrule innerrule = new Innerrule();
        innerrule.setPublishorg(staffUtil.getCurrentOrg().getOrgid().toString());
        innerrule.setRulename(rulename);
        innerrule.setRulenumber(rulenumber);
        IPage page = new Page(pageNo, pageSize);//分页设置
        IPage<Innerrule> innerRuleByRiskId = riskService.findInnerRuleByRiskId(riskid, innerrule, page);
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("riskcatName", riskcatName);
        result.put("pageBean", innerRuleByRiskId);
        result.put("riskid", riskid);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 风险创建---内外规--跳转内规列表页--保存
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/16
     */
    @OperationLog(
            success = "风险创建---内外规--跳转内规列表页--保存处理成功",
            busType = "风险识别",
            fail = "风险创建---内外规--跳转内规列表页--保存处理失败",
            operationType = OperationType.ADD,
            subType = "风险创建"
    )
    @RequestMapping(value = "/save_internal_regulations", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险创建---内外规--跳转内规列表页--保存/save_internal_regulations")
    public JsonBean save_internal_regulations(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) BigDecimal riskid,
            @Parameter(name = "innrulid", description = "内规主键", required = true) @RequestParam(value = "innrulid", required = true) BigDecimal innrulid) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        RiskInnerrule riskInnerRule = new RiskInnerrule();
        riskInnerRule.setRiskid(riskid);
        riskInnerRule.setInnrulid(innrulid);
        //判断该内规是否已经存在此风险中，如果不存在则新增
        if (riskService.isexist(riskInnerRule) == 0) {
            riskInnerruleService.save(riskInnerRule);
        }
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 风险创建---内外规--内规---删除
     *
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/16
     */
    @OperationLog(
            success = "风险创建---内外规--内规---删除处理成功",
            busType = "风险识别",
            fail = "风险创建---内外规--内规---删除处理失败",
            operationType = OperationType.DELETE,
            subType = "风险创建"
    )
    @RequestMapping(value = "/delete_internal_regulations", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险创建---内外规--内规---删除/delete_internal_regulations")
    public JsonBean delete_internal_regulations(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) BigDecimal riskid,
            @Parameter(name = "innrulid", description = "内规主键", required = true) @RequestParam(value = "innrulid", required = true) BigDecimal innrulid) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("INNRULID", innrulid);
        queryWrapper.eq("RISKID", riskid);
        riskInnerruleService.remove(queryWrapper);
        return ResponseFormat.retParam(1, 200, null);
    }

    /**
     * 版本管理 列表页查询
     *
     * @param
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/6
     */

    @OperationLog(
            success = "查询版本管理",
            busType = "风险识别",
            fail = "查询版本管理失败",
            operationType = OperationType.SELECT,
            subType = "版本管理"
    )
    @RequestMapping(value = "/risk/risk_bbgl_list", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "版本管理 列表页查询/risk/risk_bbgl_list")
    public JsonBean risk_fxsjk_list(/*HttpServletRequest request, TblRisk risk*/
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
            @Parameter(name = "iscurrentversion", description = "版本管理是否展示 1-展示 0-不展示", required = false) @RequestParam(value = "iscurrentversion", required = false) Integer iscurrentversion,
            @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Integer authorityType;
        if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
            authorityType = 1;
        } else {
            authorityType = 0;
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        // 如果客户端未提供riskcatid，那么使用orgid查询对应的风险类别
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskcategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }

        Risk risk = new Risk();
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);
        risk.setIscurrentversion(iscurrentversion);
        risk.setStaffid(staffUtil.getStaffid());
        PageInfo<Risk> pageBean = riskService.getRiskList(riskcatid, risk, authorityType, pageNo, pageSize, staffUtil);

        Riskcategory riskcategory = riskcategoryService.getById(riskcatid);
        String riskcategoryName = null;
        if (riskcategory != null) {
            riskcategoryName = riskcategoryService.findRiskcatByName(riskcategory);
        }
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("riskcategoryName", riskcategoryName);
        result.put("riskcategory", riskcategory);
        result.put("riskcatid", riskcatid);
        result.put("pageBean", pageBean);
        result.put("risk", risk);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;

    }

    /**
     * 风险识别 --风险创建 -- 内外规
     *
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/8
     */
    @OperationLog(
            success = "风险识别 --风险创建 -- 内外规列表处理成功",
            busType = "风险识别",
            fail = "风险识别 --风险创建 -- 内外规列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/risk_analysis_io", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险识别 --风险创建 -- 内外规列表risk/risk_analysis_io")
    public JsonBean risk_analysis_io(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) BigDecimal riskid,
            @Parameter(name = "riskcatName", description = "一级风险类型名称") @RequestParam(required = false) String riskcatName,
            @Parameter(name = "innerNumber", description = "查询条件 -内规编号", required = false) @RequestParam(value = "innerNumber", required = false) String innerNumber,
            @Parameter(name = "innerName", description = "查询条件 -内规名称", required = false) @RequestParam(value = "innerName", required = false) String innerName,
            @Parameter(name = "outerNumber", description = "查询条件 -外规编号", required = false) @RequestParam(value = "outerNumber", required = false) String outerNumber,
            @Parameter(name = "outerName", description = "查询条件 -外规名称", required = false) @RequestParam(value = "outerName", required = false) String outerName) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Innerrule innerrule = new Innerrule();
        Outerrule outerrule = new Outerrule();

        innerrule.setRulenumber(innerNumber);
        innerrule.setRulename(innerName);

        outerrule.setRulename(outerName);
        outerrule.setRulenumber(outerNumber);

        IPage page1 = new Page(pageNo, pageSize);
        IPage<Innerrule> pageInner = riskService.getInnerRuleList(page1, riskid, innerrule);

        IPage page2 = new Page(pageNo, pageSize);
        IPage<Outerrule> pageOuter = riskService.findOuterRuleByRiskidPageBean(page2, riskid, outerrule);
        log.info("pageBean: " + "pageBean".hashCode());
        log.info("pageBeanTWO: " + "pageBeanTWO".hashCode());

        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("innerPage", pageInner);//内部规则分页
        result.put("outerPage", pageOuter);//外部规则分页
        result.put("riskcatName", riskcatName);
        result.put("riskid", riskid);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 11
     *
     * @param pageNo
     * @param pageSize
     * @param riskcatid
     * @param token
     * @return
     * @throws Exception
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-风险识别-风险数据库编辑列表
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险管控-风险识别-风险数据库编辑列表处理成功",
            busType = "风险识别",
            fail = "风险管控-风险识别-风险数据库编辑列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/risk_fxsjk_list", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险管控-风险识别-风险数据库编辑列表/risk/risk_fxsjk_list")
    public JsonBean fxsjkList(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
            @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
            @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        // 如果客户端未提供riskcatid，那么使用orgid查询对应的风险类别
        if (StringUtils.isEmpty(riskcatid)) {
            Riskcategory cat = riskcategoryService.findQYFXByOrgid(staffUtil.getCurrentOrg().getOrgid().toString(), Riskcategory.FXSJK);
            if (cat != null) {
                riskcatid = cat.getRiskcatid().toString();
            }
        }
        Risk risk = new Risk();
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);
        risk.setStatus("6");
        PageInfo<Risk> pageBean = riskService.getRiskList(riskcatid, risk, 1, pageNo, pageSize, staffUtil);
        Riskcategory riskcategory = riskcategoryService.getById(riskcatid);
        String riskcategoryName = null;
        if (riskcategory != null) {
            riskcategoryName = riskcategoryService.findRiskcatByName(riskcategory);
        }

        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("riskcategoryName", riskcategoryName);
        result.put("riskcategory", riskcategory);
        result.put("riskcatid", riskcatid);
        result.put("pageBean", pageBean);
        result.put("risk", risk);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /*
        风险基本详情
     */
    @OperationLog(
            success = "查看风险基本详情{{#riskid}}成功",
            busType = "风险识别",
            fail = "查看风险基本详情【{{#riskid}}】失败",
            operationType = OperationType.SELECT,
            subType = "版本管理"
    )
    @RequestMapping(value = "/risk_analysis_detail", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险基本详情 /risk/risk_analysis_detail")
    public JsonBean risk_analysis_detail(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) String riskid) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        FiexibleNameAssignment ment = new FiexibleNameAssignment();
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Controlmatrix controlmatrix = null;
        String conmatid = null;
        FlowBussiness riskBussiness = null;

        // 查看风险
        Risk risk = riskService.getById(riskid);
        if (risk == null) {
            return ResponseFormat.retParam(0, "未查询到风险数据", null);
        }

        //当前风险关联的业务单元信息
        Flow flow = flowService.findFlowByRiskId(riskid.toString());
        risk.setFlow(flow);

        //查找相关控制措施信息
        conmatid = riskService.selectControlMatrixId(risk.getRiskid());
        if (StringUtils.isNotBlank(conmatid)) {
            controlmatrix = controlmatrixService.getById(conmatid);
            fieldOrgStaffId item = new fieldOrgStaffId();
            BeanUtils.copyProperties(controlmatrix, item);
            fieldOrgStaffName nameEntity = ment.setOpenName(item);
            BeanUtils.copyProperties(nameEntity, controlmatrix);
            item = null;
            nameEntity = null;
            List<TblControlEntries> entitys = tblControlEntriesMapper.getList(controlmatrix.getConmatid());
            controlmatrix.setEntries(entitys);
        }

        //查找业务单元中，业务名称，业务描述信息
        if (flow != null) {
            riskBussiness = flowBussinessService.findUniqueByFlowId(flow.getFlowid());
        }
        risk.setZrbmName(organizationService.findOrgByorgId(risk.getBelongsto()));  //查找责任部门名称
        //risk.setXgbmName(organizationService.findOrgByorgId(risk.getReorg()));      //查找相关部门名称
        if (StringUtils.isNotBlank(risk.getLeadership()))
            risk.setLeadershipName(staffService.findRealNameById(risk.getLeadership())); ////查找公司责任领导、

        if (!Objects.isNull(risk.getStaffid()))
            risk.setStaffname(staffService.findRealNameById(risk.getStaffid().toString()));
        if (!Objects.isNull(risk.getLinkDeptId()))
            risk.setLinkDeptName(organizationService.findOrgByorgId(risk.getLinkDeptId().toString()));
        if (!Objects.isNull(risk.getUnit()))
            risk.setUnitname(organizationService.findOrgByorgId(risk.getUnit().toString()));
        //原单选，后改为多选，所以此处理是对旧数据单选不能显示数据单独处理
        if (StringUtils.isNotBlank(risk.getCooperateOrg()) && StringUtils.isBlank(risk.getCooperateOrgName()))
            risk.setCooperateOrgName(organizationService.findOrgByorgId(risk.getCooperateOrg()));

        if (Objects.nonNull(risk.getRiskextid())) {
            Risk risk1 = riskService.getById(risk.getRiskextid());
            if (Objects.nonNull(risk1)) {
                risk.setRiskextname(risk1.getRisknumber());
            }
        }
        //对灵活字段中的姓名名称及机构名称赋值
        fieldOrgStaffId item = new fieldOrgStaffId();
        BeanUtils.copyProperties(risk, item);
        fieldOrgStaffName nameEntity = ment.setOpenName(item);
        BeanUtils.copyProperties(nameEntity, risk);


        if (StringUtils.isNotBlank(risk.getStepid())){
            TblNbsjAuditStepEntity tblNbsjAuditStepEntity = tblNbsjAuditStepEntityMapper.selectByStepId(risk.getStepid());

            risk.setBookid(tblNbsjAuditStepEntity.getBookid());
            risk.setSql(tblNbsjAuditStepEntity.getSqlstr());
            risk.setSteptitle(tblNbsjAuditStepEntity.getSteptitle());
        }

        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        result.put("flow", flow);
        result.put("risk", risk);
        List<Flow> flows = flowMapper.findTblFlowByorgId(selectOrg.getOrgid());

        result.put("flows", flows);
        //mv.addObject("flows", flows);
//        Risk risk1 = riskService.getById(risk.getRiskid());

        //所属风险类型信息
        Riskcategory riskcatselef = riskcategoryService.getById(risk.getRiskcatid());
        risk.setRiskcategory(riskcatselef);
        result.put("orgid", risk.getUnit());
        result.put("type", "jbxx");
        result.put("riskcatselef", riskcatselef);
        result.put("cx", controlmatrix);
        result.put("riskBussiness", riskBussiness);

        List<Attachment> attachmentList = attachmentMapper.findAttListByRisk(riskid);//ce.getByReportId(riskid);
        result.put("attachmentList", attachmentList);

        //风险应对+一体化管控内容
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);

        if (null != copings && copings.size() > 0) {
            RiskCoping coping = copings.get(0);
            if (StringUtils.isNotBlank(coping.getCopinghead())) {
                coping.setCopingheadname(staffService.getById(new BigDecimal(coping.getCopinghead())).getRealname());
            }
            item = new fieldOrgStaffId();
            BeanUtils.copyProperties(coping, item);
            nameEntity = ment.setOpenName(item);
            BeanUtils.copyProperties(nameEntity, coping);
            result.put("copings", coping);
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
            if (CollectionUtil.isNotEmpty(cons)) {
                cons.forEach(entity -> {
                    try {
                        //对灵活字段中的姓名名称及机构名称赋值
                        fieldOrgStaffId field = new fieldOrgStaffId();
                        BeanUtils.copyProperties(entity, field);
                        fieldOrgStaffName nameEntity1 = ment.setOpenName(field);
                        BeanUtils.copyProperties(nameEntity1, entity);
                        field = null; // 处理并解除引用
                        nameEntity1 = null; // 处理并解除引用
                        List<TblControlEntries> entitys = tblControlEntriesMapper.getList(entity.getConmatid());
                        entity.setEntries(entitys);
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                });
            }
            result.put("controls", cons);
        }
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /*
    风险基本详情,No riskcatid
	 */
    @OperationLog(
            success = "风险基本详情处理成功",
            busType = "风险识别",
            fail = "风险基本详情处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/risk_analysis_detail_nr", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险基本详情 /risk/risk_analysis_detail_nr")
    public JsonBean risk_analysis_detail_nr(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) String riskid) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }


        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Controlmatrix controlmatrix = null;
        String conmatid = null;
        FlowBussiness riskBussiness = null;


        // 查看风险
        Risk risk = riskService.getById(riskid);
        if (risk == null) {
            return ResponseFormat.retParam(0, "未查询到风险数据", null);
        }

        //当前风险关联的业务单元信息
        Flow flow = flowService.findFlowByRiskId(riskid.toString());
        risk.setFlow(flow);

        //查找相关控制措施信息
        conmatid = riskService.selectControlMatrixId(risk.getRiskid());
        if (StringUtils.isNotBlank(conmatid)) {
            controlmatrix = controlmatrixService.getById(conmatid);
            List<TblControlEntries> entitys = tblControlEntriesMapper.getList(controlmatrix.getConmatid());
            controlmatrix.setEntries(entitys);
        }

        //查找业务单元中，业务名称，业务描述信息
        if (flow != null) {
            riskBussiness = flowBussinessService.findUniqueByFlowId(flow.getFlowid());
        }
        risk.setZrbmName(organizationService.findOrgByorgId(risk.getBelongsto()));  //查找责任部门名称
        // risk.setXgbmName(organizationService.findOrgByorgId(risk.getReorg()));      //查找相关部门名称
        if (StringUtils.isNotBlank(risk.getLeadership()))
            risk.setLeadershipName(staffService.findRealNameById(risk.getLeadership())); ////查找公司责任领导、
        if (StringUtils.isNotBlank(risk.getCooperateOrg()) && StringUtils.isBlank(risk.getCooperateOrgName()))
            risk.setCooperateOrgName(organizationService.findOrgByorgId(risk.getCooperateOrg()));

        if (StringUtils.isNotBlank(risk.getStepid())){
            TblNbsjAuditStepEntity tblNbsjAuditStepEntity = tblNbsjAuditStepEntityMapper.selectByStepId(risk.getStepid());

            risk.setBookid(tblNbsjAuditStepEntity.getBookid());
            risk.setSql(tblNbsjAuditStepEntity.getSqlstr());
            risk.setSteptitle(tblNbsjAuditStepEntity.getSteptitle());
        }
        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        result.put("flow", flow);
        result.put("risk", risk);
        List<Flow> flows = flowMapper.findTblFlowByorgId(selectOrg.getOrgid());

        result.put("flows", flows);
        //mv.addObject("flows", flows);
        //    Risk risk1 = riskService.getById(risk.getRiskid());

        //所属风险类型信息
        Riskcategory riskcatselef = riskcategoryService.getById(risk.getRiskcatid());
        risk.setRiskcategory(riskcatselef);
        result.put("orgid", risk.getUnit());
        result.put("type", "jbxx");
        result.put("riskcatselef", riskcatselef);
        result.put("cx", controlmatrix);
        result.put("riskBussiness", riskBussiness);

        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /*
        基本详情风险添加 & 修改


     */
    @OperationLog(
            success = "【{{#riskname}}】新增/修改成功",
            busType = "风险识别",
            fail = "【{{#riskname}}】新增/修改失败",
            operationType = OperationType.ADD,
            subType = "风险创建"
    )/*riskDto,staff,type,TOp10Id*/
    @RequestMapping(value = "/risk_analysis_add", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险添加 & 修改/risk/risk_analysis_add")
    public JsonBean risk_analysis_add(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "isflow", description = "isflow") @RequestParam(required = false, defaultValue = "1") String isflow,
//                                      @Parameter(name = "stepId", description = "stepId") @RequestParam(required = false) String stepId,
                                      @RequestBody RiskDto riskDto
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Staff user = staffService.getById(staffUtil.getStaffid());
        Risk ris = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        String oldRiskid = riskDto.getRiskid();
        //放入业务单元业务流程信息
        Flow f = new Flow();
        f.setFlownumber(riskDto.getFlownumber());
        f.setFlowname(riskDto.getFlowname());
        f.setCompany(selectOrg.getOrgid().toString());
        if (riskDto.getFlowid() != null && !"".equals(riskDto.getFlowid())) {
            f.setFlowid(new BigDecimal(riskDto.getFlowid()));
        }
        // 放入业务单元 业务名称 业务描述信息
        FlowBussiness riskBussiness = new FlowBussiness();
        riskBussiness.setBussinessname(riskDto.getBussinessname());
        riskBussiness.setBussinessdes(riskDto.getBussinessdes());
        if (riskDto.getBussinessid() != null && !"".equals(riskDto.getBussinessid())) {
            riskBussiness.setBussinessid((long) Integer.parseInt(riskDto.getBussinessid()));
        }
        //放入风险信息
        Risk risk = new Risk();
        if (riskDto.getRiskid() != null && !"".equals(riskDto.getRiskid())) {
            risk.setRiskid(new BigDecimal(riskDto.getRiskid()));
        }
        risk.setRisknumber(riskDto.getRisknumber());
        risk.setRiskcatname(riskDto.getRiskcatname());
        risk.setRiskname(riskDto.getRiskname());
        risk.setVersion(riskDto.getVersion());
        risk.setRiskdes(riskDto.getRiskdes());
        risk.setBelongsto(riskDto.getBelongsto());
        risk.setLeadership(riskDto.getLeadership());
        risk.setCooperateOrg(riskDto.getCooperateOrg());
        risk.setCooperateOrgName(riskDto.getCooperateOrgName());
        System.out.println(riskDto.getLevelFourRisk());
        risk.setLevelFourRisk(riskDto.getLevelFourRisk());
        // risk.setReorg(reorg);
        risk.setRiskprogram(riskDto.getRiskprogram());
        risk.setRiskexternal(riskDto.getRiskexternal()); // 外部规定
        risk.setRiskcompany(riskDto.getRiskcompany()); // 公司规定
        risk.setRiskcompliance(riskDto.getRiskcompliance()); // 合规红线
        if (StringUtils.isNotBlank(riskDto.getRiskcatid())) {
            risk.setRiskcatid(new BigDecimal(riskDto.getRiskcatid()));
        }
        risk.setRisklevel(riskDto.getRisklevel());
        risk.setRiskcatnametwo(riskDto.getRiskcatnametwo());
        if (StringUtils.isNotBlank(riskDto.getStepid())){
            risk.setStepid(riskDto.getStepid());
        }
        if (StringUtils.isNotBlank(riskDto.getRiskcatidone())) {
            risk.setRiskcatidone(new BigDecimal(riskDto.getRiskcatidone()));
        }
//        risk.setContent(content);
        risk.setRiskcause(riskDto.getRiskcause());
        risk.setComplianceobligation(riskDto.getComplianceobligation());
        risk.setIscurrentversion(1);
        risk.setRevisiontype(riskDto.getRevisiontype());
        //密级
        risk.setSecrectLevelId(riskDto.getSecrectLevelId());
        risk.setStaffScopeNames(riskDto.getStaffScopeNames());
        risk.setStaffScopeIds(riskDto.getStaffScopeIds());
        risk.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
        risk.setRiskcreatedt(new Date());
        risk.setStaffid(staffUtil.getStaffid());
        risk.setUnit(selectOrg.getOrgid() + "");
        RiskCoping cop = new RiskCoping();
        cop.setRiskhopevalue(riskDto.getRiskHopeValue());
        cop.setCopinghead(riskDto.getUserId() != null ? riskDto.getUserId().toString() : staffUtil.getStaffid().toString());
        cop.setCopingplot(riskDto.getCopingPlot());
        cop.setYddes(riskDto.getYddes());
        cop.setRiskcopingid(StringUtils.isNotBlank(riskDto.getCopingId()) ? new BigDecimal(riskDto.getCopingId()) : new BigDecimal(0));
        if (!Objects.isNull(riskDto.getRiskextid())) {
            //制定-初次保存版本号1.0
            if (Objects.isNull(riskDto.getRiskid())) {
                risk.setVersion("1.0");
            }
            if (Objects.equals(riskDto.getRevisiontype(), 2) && Objects.nonNull(riskDto.getRiskextid())) {
                risk.setRiskextid(BigDecimal.valueOf(riskDto.getRiskextid()));
                //修订-原风险版本号➕1
                Risk risk1 = riskService.getById(risk.getRiskextid());
				/*
				if (Objects.isNull(risk1)) {
					return ResponseFormat.retParam(0, "修订数据不存在", null);
				} else {
					BigDecimal currentVersion = new BigDecimal(risk1.getVersion()).add(BigDecimal.valueOf(1L));
					risk1.setVersion(currentVersion.toString());
					//更新原来的风险不展示在版本管理里面
					risk1.setIscurrentversion(0);
					riskService.updateById(risk1);
				}*/

                if (Objects.isNull(risk1)) {
                    return ResponseFormat.retParam(0, "修订数据不存在", null);
                } else {
                    //	BigDecimal currentVersion = new BigDecimal(risk1.getVersion()).add(BigDecimal.valueOf(1L));
                    BigDecimal currentVersion = riskService.getMaxVersion(riskDto.getRiskextid()).add(BigDecimal.valueOf(1L));
//					risk1.setVersion("1.0");
                    risk.setVersion(currentVersion.toString() + ".0");
                    //更新原来的风险不展示在版本管理里面
                    risk1.setIscurrentversion(0);
                    if (StringUtils.isNotBlank(riskDto.getStepid())){
                    	risk1.setStepid(riskDto.getStepid());
                    }
                    riskService.updateById(risk1);
                }

            }
        }
        //灵活字段

        riskService.saveNewRisk(risk, selectOrg, oldRiskid, isflow, user, f, null, riskBussiness, riskDto.getAttids(), cop);
        //新增的时候要求措施一起保存
        //控制措施保存
        Controlmatrix param = riskDto.getParam();
        if (param != null) {
            BigDecimal commatid = param.getConmatid();
            param.setRiskcopingid(cop.getRiskcopingid());
            controlmatrixService.saveOrUpdate(param);
            if (Objects.isNull(commatid)) {
                riskCopingCmatrixService.save(new RiskCopingCmatrix(param.getConmatid(), param.getRiskcopingid()));//保存中间表数据
            }
            //管控措施条目信息保存；
            List<TblControlEntries> entList = param.getEntries();
            if (entList != null) {
                for (TblControlEntries e : entList) {
                    if (e.getId() != null && e.getId().compareTo(new BigDecimal(0)) != 0) {
                        tblControlEntriesMapper.updateById(e);
                    } else {
                        e.setId(RandomUtil.uuBigDecimalId());
                        e.setConmatid(param.getConmatid());
                        e.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
                        e.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());
                        e.setCreateTime(new Date());
                        e.setCreator(staffUtil.getStaffid());
                        tblControlEntriesMapper.insert(e);
                    }
                }
            }
        }
        //应对方案格式
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);
        StringBuffer yddes = new StringBuffer();
        if (null != copings && copings.size() > 0) {
            RiskCoping coping = copings.get(0);
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
            for (Controlmatrix x : cons) {
                yddes.append("一体化管控措施编号：" + x.getControlnumber() + "\n");
                yddes.append("一体化控制目标：" + x.getControldes() + "\n");
                yddes.append("控制措施：" + x.getConkzcs() + "\n");
            }
            coping.setYddes(yddes.toString());
            copingService.updateById(coping);
        }
        
       /*     //如果是执行修改风险信息的逻辑则需要执行这个分支
            if (StringUtils.isNotEmpty(oldRiskid)) {
                // 及时更正风险容忍度中=容忍度编号，名称，描述
                Risk oldRisk = riskService.getById(oldRiskid);
                //tblRiskService.updateRisk(oldRisk, risk.getRiskid().toString());
                // 添加新版本是 需要将关联老版本的所有信息都更新到新版本中
                addRiskByVersion(oldRiskid, risk.getRiskid().toString(), controlmatrix, riskBussiness, f, path);
                Flow oldflow = flowService.findFlowByRiskId(oldRiskid);

                HashMap<String, Object> fields = new HashMap<String, Object>(0);
                fields.put("oldRiskid", oldflow.getFlowid());
                fields.put("newRiskid", f.getFlowid());

            }
*/
        Flow flow = flowService.findFlowByRiskId(risk.getRiskid().toString());
        //conmatid = this.riskService.selectControlMatrixId(risk.getRiskid());
        //controlmatrix = controlmatrixService.getControlmatrix(conmatid);
        riskBussiness = flowBussinessService.findUniqueByFlowId(flow.getFlowid());
        result.put("flow", flow);
        // mv.addObject("flow", flow);
        if (StringUtils.isNotBlank(risk.getBelongsto()))
            risk.setZrbmName(organizationService.findOrgByorgId(risk.getBelongsto()));
        // ris.setXgbmName(organizationService.findOrgByorgId(ris.getReorg()));
        if (StringUtils.isNotBlank(risk.getLeadership()))
            risk.setLeadershipName(staffService.findRealNameById(risk.getLeadership())); ////查找公司责任领导、
        if (StringUtils.isNotBlank(risk.getCooperateOrg()) && StringUtils.isBlank(risk.getCooperateOrgName()))
            risk.setCooperateOrgName(organizationService.findOrgByorgId(risk.getCooperateOrg()));
        result.put("sub", 0);
        QueryWrapper<RiskCoping> queryWrapperC = new QueryWrapper<RiskCoping>();
        queryWrapperC.eq("RISKID", risk.getRiskid());
        List<RiskCoping> list = copingService.list(queryWrapperC);
        if (list != null) {
            result.put("cop", list.get(0));
        }
        if (StringUtils.isNotBlank(risk.getStepid())){
            TblNbsjAuditStepEntity tblNbsjAuditStepEntity = tblNbsjAuditStepEntityMapper.selectByStepId(risk.getStepid());

            risk.setBookid(tblNbsjAuditStepEntity.getBookid());
            risk.setSql(tblNbsjAuditStepEntity.getSqlstr());
            risk.setSteptitle(tblNbsjAuditStepEntity.getSteptitle());
        }


        List<Flow> flows = flowMapper.findTblFlowByorgId(selectOrg.getOrgid());

        result.put("flows", flows);
        //mv.addObject("flows", flows);
        if (risk != null) {
            result.put("risk", risk);
        }
//        Riskcategory riskcatselef = riskcategoryService.geTblRiskcategory(riskService.findById(risk.getRiskid().toString()).getRiskcategory().getRiskcatid());

        Riskcategory riskcatselef = riskcategoryService.getById(risk.getRiskcatid());
        risk.setRiskcategory(riskcatselef);
        result.put("riskcatid", riskDto.getRiskcatid());
        result.put("orgid", risk.getUnit());
        result.put("type", "jbxx");
        result.put("riskcatselef", riskcatselef);
        //result.put("cx", controlmatrix);
        result.put("riskBussiness", riskBussiness);
        result.put("param", param);
        JsonBean jsonBean = null;
        jsonBean = ResponseFormat.retParam(1, 200, result);

        return jsonBean;
    }

    /**
     * <p>
     * 判断保存时风险是否已经存在
     * </p>
     *
     * @author LiHongXu
     * @version 1.0.1
     * @since 2022-08-09
     */
    @OperationLog(
            success = "风险信息保存时调用 判断保存时风险是否已经存在处理成功",
            busType = "风险识别",
            fail = "风险信息保存时调用 判断保存时风险是否已经存在处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/flow_isnumber_sole", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险信息保存时调用 判断保存时风险是否已经存在/risk/flow_isnumber_sole  返回count>0 业务编号重复无法新增")
    public JsonBean flow_isnumber_sole(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "flownumber", description = "业务编号") @RequestParam(value = "flownumber", required = true) String flownumber) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String is = flowService.flowNumberIsSole(flownumber, selectOrg.getOrgid().toString());
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("count", is);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * <p>
     * 通过risknumber，unit查找指定的风险的数量
     * </p>
     *
     * @author LiHongXu
     * @version 1.0.1
     * @since 2022-08-17
     */
    @OperationLog(
            success = "风险信息保存时调用 通过risknumber查找指定的风险的数量处理成功",
            busType = "风险识别",
            fail = "风险信息保存时调用 通过risknumber查找指定的风险的数量处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/find_unit_risknumber", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险信息保存时调用 通过risknumber查找指定的风险的数量/risk/find_unit_risknumber  count>0 风险编号重复无法新增")
    public JsonBean find_unit_risknumber(@Parameter(name = "risknumber", description = "风险编号", required = true) @RequestParam(value = "risknumber", required = true) String risknumber,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String count = riskService.checkRiskNumber(risknumber, selectOrg.getOrgid().toString()).toString();
        JsonBean jsonBean = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        result.put("count", count);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /* *//**
     * <p>
     * 用于修改风险的方法
     * </p>
     *
     * @author LiHongXu
     * @version 1.0.1
     * @since 2022-08-10
     *//*
    private void addRiskByVersion(String oldRiskid, String newRiskid, Controlmatrix controlmatrix, FlowBussiness riskBussiness, Flow newflow, String path) throws Exception {
        // 风险关联内规
        List<Innerrule> ngList = riskInnerruleService.findInnerRuleByRiskId(oldRiskid, null);
        for (int i = 0; i < ngList.size(); i++) {
            Innerrule t = ngList.get(i);
            RiskInnerrule riskInnerrule = new RiskInnerrule();
            riskInnerrule.setRiskid(new BigDecimal(newRiskid));
            riskInnerrule.setInnrulid(new BigDecimal(t.getInnrulid().toString()));
            if (riskInnerruleService.isexist(riskInnerrule) == 0) {
                riskInnerruleService.save(riskInnerrule);
            }
        }
        // 风险关联外规
        List<Outerrule> wgList = riskOuterruleService.findOuterRuleByRiskId(oldRiskid, null);
        for (int i = 0; i < wgList.size(); i++) {
            Outerrule o = wgList.get(i);
            RiskOuterrule riskOuterrule = new RiskOuterrule();
            riskOuterrule.setRiskid(new BigDecimal(newRiskid));
            riskOuterrule.setOutrulid(new BigDecimal(o.getOutrulid().toString()));
            if (riskOuterruleService.isexist(riskOuterrule) == 0) {
                riskOuterruleService.save(riskOuterrule);
            }
        }
        // 风险关联风险事件
        List<Riskevent> fxsjList = riskeventService.findRiskeventByRiskId(oldRiskid);
        for (int i = 0; i < fxsjList.size(); i++) {
            Riskevent r = fxsjList.get(i);
            RiskRiskevent rre = new RiskRiskevent();
            rre.setRiskid(new BigDecimal(newRiskid));
            rre.setRiseveid(new BigDecimal(r.getRiseveid().toString()));
            riskRiskeventService.save(rre);
        }
        // 风险关联容忍度 //已废弃
        *//*List<Risktolerability> fxrrdList = risktolerabilityService.findRiskTolerByRiskid(oldRiskid);
        for (int i = 0; i < fxrrdList.size(); i++) {
            Risktolerability r = fxrrdList.get(i);
            Risktolerability risktolerability = new Risktolerability();
            risktolerability.setColorstring(r.getColorstring());
            risktolerability.setDescription(r.getDescription());
            risktolerability.setLowerborder(r.getLowerborder());
            risktolerability.setMemo(r.getMemo());
            risktolerability.setRtcode(r.getRtcode());
            Risk risk = new Risk();
            risk.setRiskid(new BigDecimal(newRiskid));
            risktolerability.setRisk(risk);
            risktolerability.setUpperborder(r.getUpperborder());
            risktolerabilityService.save(risktolerability);
        }*//*
        // 风险关联风险源
        List<RiskRiskAtt> fxyList = riskRiskAttService.findRiskAttByRiskid(oldRiskid);
        for (int i = 0; i < fxyList.size(); i++) {
            RiskRiskAtt rra = fxyList.get(i);
            RiskRiskAtt riskAtt = new RiskRiskAtt();
            Risk r = new Risk();
            r.setRiskid(new BigDecimal(newRiskid));
            riskAtt.setRisk(r);
            // riskAtt.getTblRisk().setRiskid(new BigDecimal(newRiskid));
            riskAtt.setAttachment(rra.getAttachment());
            riskAtt.setRiskresname(rra.getRiskresname());
            riskAtt.setRiskresdes(rra.getRiskresdes());
            riskAtt.setRiskrescode(rra.getRiskrescode());
            if (rra.getAttachment() != null && rra.getAttachment().getAttid() != null) {
                Attachment att = attachmentService.getById(rra.getAttachment().getAttid().toString());
                File file = new File(path + att.getAttpath());
                String url = "/upload/fxy/copy-" + file.getName();
                File file1 = new File(path + url);
                if (file1.exists()) {
                    fileChannelCopy(file, file1);
                }
                Attachment attachment = new Attachment();
                attachment.setAttname("copy-" + att.getAttname());
                attachment.setAttpath(url);
                attachment.setAttsize(att.getAttsize());
                attachment.setMemo(att.getMemo());
                attachment.setUploadtime(LocalDateTime.now());
                attachment.setUploader(att.getUploader());
                attachmentService.save(attachment);
                riskAtt.setAttachment(attachment);
            }
            riskRiskAttService.save(riskAtt);
        }
        // 风险关联控制信息
        controlmatrixService.save(controlmatrix);
        RiskControlmatrix riskControlMatrix = new RiskControlmatrix();
        riskControlMatrix.setConmatid(new BigDecimal(controlmatrix.getConmatid().toString()));
        riskControlMatrix.setRiskid(new BigDecimal(newRiskid));
        riskControlmatrixService.save(riskControlMatrix);

        // 风险关联流程图 //已废弃
        *//*Flow flow = flowService.findFlowByRiskId(oldRiskid);
        if (flow != null && flow.getFlowid() != null) {
            if (flow.getFlowchart() != null && !flow.getFlowchart().equals("")) {
                File file = new File(path + flow.getFlowchart());
                String url = "/data/flow/" + FxglUtil.getTimeString() + ".xml";
                File file1 = new File(path + url);
                if (file1.exists()) {
                    fileChannelCopy(file, file1);
                    InputStream input = new FileInputStream(path + url);
                    FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
                    newflow.setFlowchart(url);
                }

            }
        }*//*

        newflow.setFlowid(flowService.newFlowId());
        flowService.save(newflow);
        RiskFlow riskFlow = new RiskFlow();
        riskFlow.setRiskid(new BigDecimal(newRiskid));
        riskFlow.setFlowid(new BigDecimal(newflow.getFlowid().toString()));
        riskFlowService.save(riskFlow);
        // 风险关联业务
		*//*List<TblFlowBussiness> ywList = tblFlowBussinessService.findByRiskid(oldRiskid, null);
		for (int i = 0; i < ywList.size(); i++) {
			TblFlowBussiness rb = ywList.get(i);
			TblFlowBussiness bussiness = new TblFlowBussiness();
			bussiness.setFlowid(Long.parseLong(f.getFlowid().toString()));
			bussiness.setBussinessdes(rb.getBussinessdes());
			bussiness.setBussinessname(rb.getBussinessname());
			bussiness.setBussinessnumber(rb.getBussinessnumber());
			tblFlowBussinessService.save(bussiness);
		}*//*
        riskBussiness.setFlowid(newflow.getFlowid().longValue());
        riskBussiness.setBussinessid(flowBussinessService.newBissinessId().longValue());
        flowBussinessService.save(riskBussiness);
        // 风险关联流程描述 //已废弃
       *//* if (newflow != null && newflow.getFlowid() != null) {
            List<Flowdes> lcList = flowdesService.returnFlowdesByRiskid(oldRiskid);
            for (int i = 0; i < lcList.size(); i++) {
                Flowdes flowdes = lcList.get(i);
                Flowdes newFlowdes = new Flowdes();
                CopyObject.copyPropertiesExclude(flowdes, newFlowdes, new String[]{"flowdesid"});
                newFlowdes.setFlow(newflow);
                flowdesService.save(newFlowdes);
            }
        }*//*

        //风险关联自定义表单 //已废弃
        *//*HashMap<String, Object> fields = new HashMap<String, Object>(0);
        fields.put("oldRiskid", oldRiskid);
        fields.put("newRiskid", newRiskid);
        try {
            String result = HttpClient.request(formurl + "/form/copyRiskFormByNewRisk", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }*//*
    }*/

    /**
     * <p>
     * 文件复制
     * </p>
     *
     * @author LiHongXu
     * @version 1.0.1
     * @since 2022-08-10
     */
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

    /**
     * 风险数据库---列表页查询
     *
     * @return
     * @author yangzeguo
     * @version v1.0.1
     */
    @OperationLog(
            success = "风险数据库-页面处理成功",
            busType = "风险识别",
            fail = "风险数据库-页面处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @Operation(summary = "风险数据库-页面/risk/risk_fxsjk  无意义 无需访问")
    @RequestMapping(value = "/risk_fxsjk", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean risk_fxsjk(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        Map result = new HashMap();
        return new JsonBean(200, "success", result);
    }

    @OperationLog(
            success = "查看风险识别",
            busType = "风险识别",
            fail = "查看风险识别",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @Operation(summary = "风险管控-风险识别-风险创建 编辑列表 /risk/risk_analysis_list")
    @RequestMapping(value = "/risk_analysis_list", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean risk_analysis_list(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
            @Parameter(name = "ty", description = "工作台 行业风险数据库访问时传入hy 其他情况不传") @RequestParam(value = "ty", required = false) String ty,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
            @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname,
            @Parameter(name = "belongsto", description = "查询条件-责任部门 传入部门Id", required = false) @RequestParam(required = false) String belongsto,
            @Parameter(name = "status", description = "查询条件-审批状态", required = false) @RequestParam(required = false) String status,
            @Parameter(name = "busname", description = "查询条件 -业务名称", required = false) @RequestParam(value = "busname", required = false) String busname,
            @Parameter(name = "flowname", description = "查询条件 -流程名称", required = false) @RequestParam(value = "flowname", required = false) String flowname,
            @Parameter(name = "iscurrentversion", description = "查询条件 -是否当前版本", required = false) @RequestParam(value = "iscurrentversion", required = false) Integer iscurrentversion,
            @Parameter(name = "closestatus", description = "查询条件 -风险状态  0已经关闭  1未关闭", required = false) @RequestParam(value = "closestatus", required = false) BigDecimal closestatus

    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //用于判断是否为风险管理员；该角色能看到本公司所有的风险创建信息；
        Integer authorityType;
        if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
            authorityType = 1;
        } else {
            authorityType = 0;
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String organizationId = null;
        if (selectOrg != null && selectOrg.getOrgid() != null) {
            organizationId = selectOrg.getOrgid().toString();
        }
        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
            organizationId = organizationService.getHYFirst().getOrgid().toString();
        }
        if (StringUtils.isEmpty(riskcatid)) {
            riskcatid = "1";
//            Riskcategory cat = riskcategoryService.
//                    findQYFXByOrgid(organizationId, StringUtils.isNotBlank(ty) && ty.equals("hy") ?
//                            Riskcategory.HYFXSJK : Riskcategory.FXSJK);
//            if (cat != null) {
//                riskcatid = cat.getRiskcatid().toString();
//            }
        }
        IPage page = new Page(pageNo, pageSize);//分页设置111271
        Risk risk = new Risk();
        risk.setBelongsto(belongsto);
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);
        risk.setStatus(status);
        risk.setStaffid(staffUtil.getStaffid());
        risk.setClosestatus(closestatus);
        risk.setUnit(staffUtil.getCurrentOrg().getOrgid() + "");
        PageInfo<Risk> pageInfo = riskService.getRiskList2(riskcatid, risk, authorityType, pageNo, pageSize, flowname, busname, staffUtil, iscurrentversion);
        //  PageResult<Risk> pageBean = new PageResult<Risk>().build(pageInfo);
        //查找所属风险类型信息
        Riskcategory riskcategory = riskcategoryService.getById(riskcatid);
        String riskcategoryName = null;
        if (riskcategory != null) {
            riskcategoryName = riskcategoryService.findRiskcatByName(riskcategory);
        }
        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        result.put("riskcategoryName", riskcategoryName);
        result.put("riskcategory", riskcategory);
        result.put("riskcatid", riskcatid);
        result.put("pageBean", pageInfo);
        result.put("risk", risk);
        result.put("belongsto", belongsto);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 1
     *
     * @param riskid
     * @param riskcatid
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-风险识别-风险创建-删除
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "【{{#riskid}}】删除成功",
            busType = "风险识别",
            fail = "【{{#riskid}}】删除失败",
            operationType = OperationType.DELETE,
            subType = "风险创建"
    )
    @Operation(summary = "风险管控-风险识别-风险创建-删除 /risk/risk_analysis_del")
    @RequestMapping(value = "/risk_analysis_del", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    //@ResponseBody
    public JsonBean risk_analysis_del(
            @Parameter(name = "riskid", description = "删除选中的风险信息主键", required = true) @RequestParam(value = "riskid", required = true) String riskid,
            @Parameter(name = "riskcatid", description = "风险类型ID", required = true) @RequestParam(value = "riskcatid", required = true) String riskcatid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            //根据riskid找到risk对象 115653
            Risk tblRisk = riskService.getById(riskid);
            if (tblRisk == null) return ResponseFormat.retParam(1, 200, null);
            //根据risk对象的RISKNUMBER 和 RISKCATID 找到集合 115651
            List<Risk> list = riskMapper.findTblRiskByRiskNumber(tblRisk.getRisknumber(), riskcatid);

            if (list != null && list.size() > 0) {
                StringBuffer sbids = new StringBuffer();
                for (Risk delrisk : list) {
                    //获取取到的riskid
                    sbids.append(delrisk.getRiskid() + ",");
                }

                //去除末尾 ,
                sbids.deleteCharAt(sbids.length() - 1);
                //根据找到的riskid删除所有关信息
                riskService.delRiskInfoAll(sbids.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, null);

    }


    /**
     * @param orgid
     * @param
     * @return
     * @author yangzeguo
     * @version v1.0.1
     * @Description 风险管控-风险识别-风险数据库-左选择树
     * @Date 2022/8/7
     */
    @OperationLog(
            success = "风险管控-风险识别-风险数据库-左选择树处理成功",
            busType = "风险识别",
            fail = "风险管控-风险识别-风险数据库-左选择树处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @Operation(summary = "风险管控-风险识别-风险数据库-左选择树 /risk/risk_fxsjk_left")
    @RequestMapping(value = "/risk_fxsjk_left", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean risk_fxsjk_left(
            @Parameter(name = "orgid", description = "公司主键", required = false) @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        if (StringUtils.isEmpty(orgid)) {
            orgid = selectOrg.getOrgid().toString();
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        List<Riskcategory> catTree = riskcategoryService.getRiskCateTreeByOrgId(orgid, Riskcategory.FXSJK, null);

        resultMap.put("tree", catTree);
//        mv.addObject("moduletype", new TblRiskcategory().FXSJK);
        resultMap.put("targetFrame", "mainFramex");
        resultMap.put("orgid", orgid);
        jsonBean = ResponseFormat.retParam(1, 200, resultMap);
        return jsonBean;
    }

    //    @Operation(summary = "风险数据库-导出风险Excel /risk/risk_export")
//    @RequestMapping(value = "/risk_export",method = {RequestMethod.POST} ,produces = "application/html; charset=utf-8")
//    @ResponseBody
    @OperationLog(
            success = "风险数据库-导出风险Excel处理成功",
            busType = "风险识别",
            fail = "风险数据库-导出风险Excel处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险创建"
    )
    @Operation(summary = "风险数据库-导出风险Excel /risk/risk_export")
    @GetMapping(value = "/risk_export", produces = "application/json; charset=utf-8")
    public void sjfx_export(HttpServletRequest request, HttpServletResponse response,
                            @Parameter(name = "riskid", description = "导出的风险信息主键", required = true) @RequestParam(value = "riskid", required = true) String riskid,
                            @Parameter(name = "riskcatid", description = "风险类型ID", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
                            @Parameter(name = "token", description = "token类型ID", required = true) @RequestParam(value = "token", required = true) String token) throws Exception {
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            if (staffUtil == null) {
                //        	return ResponseFormat.retParam(0, 20006, null);
                return;
            }
            TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
            if (StringUtils.isEmpty(riskcatid)) {
                Riskcategory cat = riskcategoryService.findQYFXByOrgid(selectOrg.getOrgid().toString(), Riskcategory.FXSJK);
                if (cat != null) {
                    riskcatid = cat.getRiskcatid().toString();
                }
            }
            Riskcategory riskevent = new Riskcategory();
            riskevent.setRiskcatid(new BigDecimal(riskcatid));
            List<BigDecimal> riskcatidAll = riskcategoryService.findRiskcatidByChildNode(riskevent);
            log.info("风险识别---风险数据库---导出Excel");

            //==
            String name = new String("风险数据库".getBytes(), "iso-8859-1");
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = name + "_" + date + ".xlsx";
//            String fileName = "fxsjk.xlsx";

            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

            ServletOutputStream outputStream = response.getOutputStream();
            //查询所有测试结果
            List<Object[]> objList = riskService.findexport(riskcatidAll, riskid);
            String[] titles = {"风险编号", "风险名称", "版本", "创建时间", "责任部门", "相关部门", "风险描述"};
            ImportOrExportExcelUtil.exportExcel(titles, objList, outputStream, null);
        } catch (Exception e) {
            log.info("风险识别---风险数据库---导出Excel失败");
            e.printStackTrace();
        }
    }


    /**
     * 版本管理---查看历史版本
     *
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/4
     */
    @OperationLog(
            success = "查看历史版本",
            busType = "风险识别",
            fail = "查看历史版本",
            operationType = OperationType.SELECT,
            subType = "版本管理"
    )
    @RequestMapping(value = "/risk_bbgl_historical_version", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "版本管理---查看历史版本/risk_bbgl_historical_version")
    public JsonBean risk_bbgl_historical_version(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                 @Parameter(name = "riskid", description = "查看版本信息风险信息主键", required = false) @RequestParam(value = "riskid", required = false) BigDecimal riskid)
            throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Page<Risk> page = new Page<>(1, 3);

        Page<Risk> hisVersion = riskService.findRiskByHistoricalVersion(page, riskid);
        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;

        List<Risk> records = hisVersion.getRecords();
        if (CollectionUtil.isNotEmpty(records)) {
            records.forEach(item -> {
                Organization org = organizationService.getById(item.getUnit());
                if (Objects.nonNull(org)) {
                    item.setUnit(org.getOrgname());
                }
            });
        }

        result.put("hisVersion", hisVersion);

        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    @OperationLog(
            success = "风险台账- 根据风险获取历史风险评估结果处理成功",
            busType = "风险识别",
            fail = "风险台账- 根据风险获取历史风险评估结果处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxtz/riskResultCount", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险台账- 根据风险获取历史风险评估结果/risk/fxtz/riskResultCount")
    public JsonBean fxtz_riskResultCount(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="riskid",description="风险主键",required=true) @RequestParam(value = "riskid", required = true) BigDecimal riskid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = this.riskAssplanRiskService.findRiskAssplanRiskCountByRiskId(token, riskid);

        return jsonBean;
    }

    @OperationLog(
            success = "风险台账- 根据风险获取历史风险评估结果处理成功",
            busType = "风险识别",
            fail = "风险台账- 根据风险获取历史风险评估结果处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxtz/riskResult", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险台账- 根据风险获取历史风险评估结果/risk/fxtz/riskResult")
    public JsonBean fxtz_riskResult(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="riskid",description="风险主键",required=true) @RequestParam(value = "riskid", required = true) BigDecimal riskid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = this.riskAssplanRiskService.findRiskAssplanRiskByRiskId(token, riskid);

        return jsonBean;
    }


    @OperationLog(
            success = "风险台账- 根据风险获取风险应对结果数量处理成功",
            busType = "风险识别",
            fail = "风险台账- 根据风险获取风险应对结果数量处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/fxtz/riskCopyCount", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "风险台账- 根据风险获取风险应对结果数量/risk/fxtz/riskCopyCount")
    public JsonBean fxtz_riskCopyCount(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="riskid",description="风险主键",required=true) @RequestParam(value = "riskid", required = true) BigDecimal riskid) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = this.copingService.findRiskCopyingCountByRiskId(token, riskid);

        return jsonBean;
    }

    @OperationLog(
            success = "获取流程字典处理成功",
            busType = "风险识别",
            fail = "获取流程字典处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @OperationLog(
            success = "获取流程字典",
            busType = "风险识别",
            fail = "获取流程字典",
            operationType = OperationType.SELECT,
            subType = "版本管理"
    )
    @RequestMapping(value = "/get_risk_process", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "获取流程字典")
    public JsonBean get_risk_process(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        JsonBean jsonBean = this.riskService.get_risk_process(token);

        return jsonBean;
    }

    @OperationLog(
            success = "获取业务字典By流程名称处理成功",
            busType = "风险识别",
            fail = "获取业务字典By流程名称处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/get_risk_business", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "获取业务字典By流程名称")
    public JsonBean get_risk_business(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="processname",description="流程名称",required=true) @RequestParam(value = "processname", required = true) String processname
    ) throws Exception {
        JsonBean jsonBean = this.riskService.get_risk_business(token, processname);

        return jsonBean;
    }

    @OperationLog(
            success = "获取风险编号By业务编号处理成功",
            busType = "风险识别",
            fail = "获取风险编号By业务编号处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/get_risk_no", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险编号By业务编号")
    public JsonBean get_risk_no(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="businessno",description="业务编号",required=true) @RequestParam(value = "businessno", required = true) String businessno
    ) throws Exception {
        JsonBean jsonBean = this.riskService.get_risk_no(token, businessno);

        return jsonBean;
    }


    @OperationLog(
            success = "获取风险创建编号-根据年度 RISK-2024-001处理成功",
            busType = "风险识别",
            fail = "获取风险创建编号-根据年度 RISK-2024-001失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/get_riskNo", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险创建编号-根据年度 RISK-2024-001")
    public JsonBean get_riskNo(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        JsonBean jsonBean = this.riskService.get_riskNo(token);

        return jsonBean;
    }


    @OperationLog(
            success = "获取控制措施编号",
            busType = "风险识别",
            fail = "获取控制措施编号",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )
    @RequestMapping(value = "/get_riskcontrol_no", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    @Operation(summary = "获取控制措施编号By风险编号")
    public JsonBean get_riskcontrol_no(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name="riskno",description="风险编号",required=true) @RequestParam(value = "riskno", required = true) String riskno
    ) throws Exception {
        JsonBean jsonBean = this.riskService.get_riskcontrol_no(token, riskno);

        return jsonBean;
    }

    @OperationLog(
            success = "内控管理-测试任务-选择风险列表处理成功",
            busType = "风险识别",
            fail = "内控管理-测试任务-选择风险列表处理失败",
            operationType = OperationType.SELECT,
            subType = "风险创建"
    )

    @OperationLog(
            success = "查询风险列表成功",
            busType = "风险评估",
            fail = "查询风险列表失败",
            operationType = OperationType.SELECT,
            subType = "评估计划"
    )
    @Operation(summary = "内控管理-测试任务-选择风险列表")
    @RequestMapping(value = "/testtask_risk_list", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean testtask_risk_list(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "riskcatid", description = "风险类型主键Id", required = false) @RequestParam(value = "riskcatid", required = false) String riskcatid,
            @Parameter(name = "ty", description = "工作台 行业风险数据库访问时传入hy 其他情况不传") @RequestParam(value = "ty", required = false) String ty,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
            @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname,
            @Parameter(name = "belongsto", description = "查询条件-责任部门 传入部门Id", required = false) @RequestParam(required = false) String belongsto,
            @Parameter(name = "status", description = "查询条件-审批状态", required = false) @RequestParam(required = false) String status,
            @Parameter(name = "closestatus", description = "查询条件 -风险状态0已经关闭  1未关闭", required = false) @RequestParam(value = "closestatus", required = false) BigDecimal closestatus,
            @Parameter(name = "secrectLevelId", description = "密级级别", required = false) @RequestParam(value = "secrectLevelId", required = false) BigDecimal secrectLevelId


    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Integer authorityType = 1;
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String organizationId = null;
        if (selectOrg != null && selectOrg.getOrgid() != null) {
            organizationId = selectOrg.getOrgid().toString();
        }
        if (StringUtils.isNotBlank(ty) && ty.equals("hy")) {
            organizationId = organizationService.getHYFirst().getOrgid().toString();
        }
        if (StringUtils.isEmpty(riskcatid)) {
            riskcatid = "1";
        }
        Risk risk = new Risk();
        risk.setBelongsto(belongsto);
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);
        risk.setStatus(status);
        risk.setStaffid(staffUtil.getStaffid());
        risk.setStaffid(closestatus);
        risk.setClosestatus(closestatus);
        risk.setSecrectLevelId(secrectLevelId);
        risk.setUnit(staffUtil.getCurrentOrg().getOrgid() + "");
        PageInfo<Risk> pageInfo = riskService.getTesttaskList2(riskcatid, risk, authorityType, pageNo, pageSize, null, null, staffUtil);
        //查找所属风险类型信息
        Riskcategory riskcategory = riskcategoryService.getById(riskcatid);
        String riskcategoryName = null;
        if (riskcategory != null) {
            riskcategoryName = riskcategoryService.findRiskcatByName(riskcategory);
        }
        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        result.put("riskcategoryName", riskcategoryName);
        result.put("riskcategory", riskcategory);
        result.put("riskcatid", riskcatid);
        result.put("pageBean", pageInfo);
        result.put("risk", risk);
        result.put("belongsto", belongsto);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }


    @OperationLog(
            success = "应对方案内容自动填充",
            busType = "风险识别",
            fail = "应对方案内容自动填充",
            operationType = OperationType.UPDATE,
            subType = "风险创建"
    )
    @Operation(summary = "风险创建-应对方案根据一体化管控修改内容调整 update_control_Responseplan")
    @PostMapping(value = "/update_control_Responseplan")
    public JsonBean update_control_Responseplan(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                @Parameter(name = "riskid", description = "风险主键", required = true) @RequestParam(value = "riskid", required = true) String riskid) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(401, "未查到用户登录信息，用户登录信息或已过期", null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        Risk risk = null;
        if (StringUtils.isEmpty(riskid) || (risk = riskService.getById(riskid)) == null) {
            return new JsonBean(400, "未提供准确的riskid，无法继续", null);
        }
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);
        StringBuffer yddes = new StringBuffer();
        Map<String, Object> result = new HashMap<String, Object>();
        if (null != copings && copings.size() > 0) {
            RiskCoping coping = copings.get(0);
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
            for (Controlmatrix x : cons) {
                yddes.append("一体化管控措施编号：" + x.getControlnumber() + "\n");
                yddes.append("一体化控制目标：" + x.getControldes() + "\n");
                yddes.append("控制措施：" + x.getConkzcs() + "\n");
            }
            coping.setYddes(yddes.toString());
            copingService.updateById(coping);
        }
        return new JsonBean(200, "成功", result);

    }


    @OperationLog(
            success = "查看风险台账",
            busType = "风险识别",
            fail = "查看风险台账",
            operationType = OperationType.SELECT,
            subType = "风险台账"
    )
    @Operation(summary = "风险管控-风险识别-风险台账 编辑列表 /risk/getRiskLedger")
    @RequestMapping(value = "/getRiskLedger", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean getRiskLedger(
            @Parameter(name = "pageNo", description = "起始页数", required = false) @RequestParam(defaultValue = "1", value = "pageNo", required = false) Integer pageNo,
            @Parameter(name = "pageSize", description = "每页数量", required = false) @RequestParam(defaultValue = "20", value = "pageSize", required = false) Integer pageSize,
            @Parameter(name = "orgid", description = "公司主键id", required = false) @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "ty", description = "工作台 行业风险数据库访问时传入hy 其他情况不传") @RequestParam(value = "ty", required = false) String ty,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
            @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname,
            @Parameter(name = "belongsto", description = "查询条件-责任部门 传入部门Id", required = false) @RequestParam(required = false) String belongsto,
            @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
            @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
            @Parameter(name = "unitname", description = "首页穿透传递公司名称参数", required = false) @RequestParam(required = false) String unitname,
            @Parameter(name = "size", description = "风险等级", required = false) @RequestParam(required = false) String size,
            @Parameter(name = "isEvaluated", description = "是否已评估", required = false) @RequestParam(required = false) String isEvaluated,
            @Parameter(name = "year", description = "首页穿透传递年度参数", required = false) @RequestParam(required = false) String year

    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        Integer authorityType;
        if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
            authorityType = 1;
        } else {
            authorityType = 0;
        }
        IPage page = new Page(pageNo, pageSize);//分页设置111271
        Risk risk = new Risk();
        risk.setBelongsto(belongsto);
        risk.setRisknumber(risknumber);
        risk.setRiskname(riskname);
        risk.setSize(size);
        risk.setSfypg(isEvaluated);
        if (StringUtils.isBlank(orgid)) {
            orgid = staffUtil.getCurrentOrg().getOrgid().toString();
        }
        risk.setUnit(orgid);
        risk.setRiskcatidname(riskcatidname);
        risk.setRiskcatname(riskcatname);
        if (StringUtils.isNotBlank(unitname)) {
        	if(StringUtils.isNotBlank(unitname) && unitname.equals("公司本部")){
        		risk.setUnitname(staffUtil.getLinkOrg().getOrgname());
            }else {
            	risk.setUnitname(unitname);
            }
           
        }
        if (StringUtils.isNotBlank(year)) {
            risk.setYear(year);
        }
        PageInfo<Risk> pageInfo = riskService.getRiskList3(risk, pageNo, pageSize, token);
        //  PageResult<Risk> pageBean = new PageResult<Risk>().build(pageInfo);
        Map<String, Object> result = new HashMap<String, Object>(0);
        JsonBean jsonBean = null;
        result.put("pageBean", pageInfo);
        result.put("risk", risk);
        result.put("belongsto", belongsto);
        jsonBean = ResponseFormat.retParam(1, 200, result);
        return jsonBean;
    }

    /**
     * 获取近12个月风险趋势统计
     * 统计每个月创建的风险数量，不区分一般风险和重大风险
     */
    @OperationLog(
            success = "查询近12个月风险趋势统计成功",
            busType = "风险趋势",
            fail = "查询近12个月风险趋势统计失败",
            operationType = OperationType.SELECT,
            subType = "风险趋势统计"
    )
    @Operation(summary = "获取近12个月风险趋势统计 /risk/getRiskTrendLast12Months")
    @RequestMapping(value = "/getRiskTrendLast12Months", method = {RequestMethod.GET}, produces = "application/json; charset=utf-8")
    public JsonBean getRiskTrendLast12Months(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "orgid", description = "公司主键id，不传则查询当前用户所在公司", required = false) @RequestParam(value = "orgid", required = false) String orgid
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

        // 如果没有传入orgid，使用当前用户所在公司
        if (StringUtils.isBlank(orgid)) {
            orgid = staffUtil.getCurrentOrg().getOrgid().toString();
        }

        try {
            // 调用Service层方法获取近12个月的风险趋势数据
            Map<String, Object> trendData = riskService.getRiskTrendLast12Months(orgid);
            return ResponseFormat.retParam(1, 200, trendData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, "查询风险趋势数据失败: " + e.getMessage(), null);
        }
    }

    @OperationLog(
            success = "风险台账-导出风险Excel处理成功",
            busType = "风险台账",
            fail = "风险台账-导出风险Excel处理失败",
            operationType = OperationType.EXPORT,
            subType = "风险创建"
    )
    @Operation(summary = "风险台账-导出风险Excel /risk/exportRiskLedger")
    @GetMapping(value = "/exportRiskLedger", produces = "application/json; charset=utf-8")
    public void exportRiskLedger(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "ids", description = "风险主键", required = false) @RequestParam(value = "ids", required = false) String ids,
                                 @Parameter(name = "orgid", description = "公司主键id", required = false) @RequestParam(value = "orgid", required = false) String orgid,
                                 @Parameter(name = "ty", description = "工作台 行业风险数据库访问时传入hy 其他情况不传") @RequestParam(value = "ty", required = false) String ty,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "risknumber", description = "查询条件 -风险编号", required = false) @RequestParam(value = "risknumber", required = false) String risknumber,
                                 @Parameter(name = "riskname", description = "查询条件 -风险名称", required = false) @RequestParam(value = "riskname", required = false) String riskname,
                                 @Parameter(name = "belongsto", description = "查询条件-责任部门 传入部门Id", required = false) @RequestParam(required = false) String belongsto,
                                 @Parameter(name = "riskcatidname", description = "风险类型", required = false) @RequestParam(required = false) String riskcatidname,
                                 @Parameter(name = "riskcatname", description = "风险领域", required = false) @RequestParam(required = false) String riskcatname,
                                 @Parameter(name = "unitname", description = "首页穿透传递公司名称参数", required = false) @RequestParam(required = false) String unitname,
                                 @Parameter(name = "year", description = "首页穿透传递年度参数", required = false) @RequestParam(required = false) String year
    ) throws Exception {
        try {
            TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
            Integer authorityType;
            if (JudgeRoleRight.judgeRoleRight(administrators, staffUtil.getRoleNames())) {
                authorityType = 1;
            } else {
                authorityType = 0;
            }
            Risk risk = new Risk();
            risk.setBelongsto(belongsto);
            risk.setRisknumber(risknumber);
            risk.setRiskname(riskname);
            if (StringUtils.isBlank(orgid)) {
                orgid = staffUtil.getCurrentOrg().getOrgid().toString();
            }
            risk.setUnit(orgid);
            risk.setRiskcatidname(riskcatidname);
            risk.setRiskcatname(riskcatname);
            if (StringUtils.isNotBlank(unitname)) {
                risk.setUnitname(unitname);
            }
            if (StringUtils.isNotBlank(year)) {
                risk.setYear(year);
            }
            List<Object[]> riskList = riskService.exportRiskList3(risk, token, ids);
            Map<String, Object> result = new HashMap<String, Object>(0);
            String name = new String("风险台账".getBytes(), "iso-8859-1");
            String date = String.valueOf(System.currentTimeMillis());
            String fileName = name + "_" + date + ".xlsx";
            response.setContentType("application/octet-stream;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            ServletOutputStream outputStream = response.getOutputStream();
            String[] titles = {"风险编号", "风险名称", "风险描述", "创建时间", "风险领域", "风险类型", "公司名称", "所属部门", "所属科室", "创建人"};
            int[] cWiths = new int[]{5500, 5500, 12500, 3500, 3500, 5500, 5500, 5500, 5500, 5500};
            ImportOrExportExcelUtil.exportExcelSetWith(titles, riskList, outputStream, cWiths, null);
        } catch (Exception e) {
            log.info("风险识别---风险台账---导出Excel失败");
            e.printStackTrace();
        }
    }


    @OperationLog(
            success = "导入风险创建",
            busType = "风险识别",
            fail = "导入风险创建",
            operationType = OperationType.IMPORT,
            subType = "风险创建"
    )
    @Operation(summary = "风险管控-风险识别-风险创建 导入信息/risk/importRiskInfo")
    @RequestMapping(value = "/importRiskInfo", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public JsonBean importRiskInfo(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            MultipartFile file
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        JsonBean jsonBean = null;
        try {
            Map<String, Object> resultMap = this.riskService.importRiskInfo(token, file);
            jsonBean = ResponseFormat.retParam(1, 200, resultMap);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return jsonBean;
    }





    @Operation(summary = "风险管控-风险识别-风险创建 安全风险TOP10对接接口导入/risk/aqTopTenimportRiskInfo")
    @RequestMapping(value = "/aqTopTenimportRiskInfo",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    public JsonBean aqTopTenimportRiskInfo(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //导入——编写SQL语句
        String sqlText = "select * from lrc_bigdata.mes_sfms_safety_inspection_topten where form_type = 'top10' and is_archived = 'N'";
//        String sqlText = "select * from TBL_RISK";
        JsonBean jsonBean = this.executeSql(sqlText);
        Map<String,Object> data = (Map<String, Object>) jsonBean.getData();
/*        List<Object> colName = new ArrayList<>();
        colName.add("trouble_desc");
        colName.add("result_and_affect");
        colName.add("rectify_measures");
        colName.add("rectify_process");
        colName.add("depart_self");
        colName.add("write_sect_name");
        colName.add("write_sect_code");
        colName.add("resp_person_code");
        colName.add("supervise_leader_code_head");
        colName.add("create_by");
        colName.add("create_time");
        colName.add("id");
        colName.add("membere_unit_id");
        colName.add("rectify_limit_time");*/
        //获取列名
        List<Object> colName = (List<Object>) data.get("colName");
        for (Object s : colName){
            System.out.println(s);
        }
        //获取数据
        List<Object> dataList = (List<Object>) data.get("dataList");
        /*List<Object> dataList = new ArrayList<>();
        Object [] o = {"风险描述","风险原因","控制措施","一体化控制目标","NO01","合规风控部（法务部）","008","星光","rq","rq","2025-09-03 20:19:31","257","NO01","2025-09-03"};
        Object [] o2 = {"风险描述2","风险原因2","控制措施2","一体化控制目标2","NO01","合规风控部（法务部）","008","星光","rq","rq","2025-09-03 20:19:31","258","NO01","2025-09-03"};
        dataList.add(o);
        dataList.add(o2);*/
        for (Object strings : dataList){
            Object [] datas = (Object[]) strings;

            String type = "aqyh";

            String TOp10Id = (String) datas[colName.indexOf("id")];
            String riskId = null;

            try {
                if (TOp10Id != null && !TOp10Id.trim().isEmpty()) {
                    riskId = riskMapper.getRiskIdByTOPID(TOp10Id);
                } else {
                    log.warn("TOp10Id is empty, skipping record");
                    continue;
                }

                if (riskId != null && !riskId.trim().isEmpty()) {
                    log.info("Risk with TOp10Id {} already exists, skipping", TOp10Id);
                    continue;
                }
            } catch (Exception e) {
                log.error("Error checking duplicate for TOp10Id: {}", TOp10Id, e);
                continue; // 或者根据业务需求决定是否终止流程
            }

            //获取删除标记 看是否需要删除
            String deFlag = (String) datas[colName.indexOf("delete_flag")];
            if (deFlag.equals("1")){
                continue;
            }
            //获取风险编号
            String riskNo = riskService.get_riskNo(token).getData().toString();
            System.out.println("风险编号："+riskNo);
            //获取一体化管控措施编号
            String riskcontrolNo = riskService.get_riskcontrol_no(token, riskNo).getData().toString();
            System.out.println("一体化管控措施编号："+riskcontrolNo);
            RiskDto riskDto = new RiskDto();
            riskDto.setRisknumber(riskNo);

            /*riskDto.setRiskcatname("企业风险");
            riskDto.setRiskcatidone("1005118");
            riskDto.setRiskcatnametwo("战略风险");
            riskDto.setRiskcatid("1005137");*/
            //流程分类
            riskDto.setFlowname("安全环保类");
//            riskDto.setFlowid("1008570");
            //牵头责任部门固定数字
            riskDto.setBelongsto("622104920662085");
            //固定版本为1.0
            riskDto.setVersion("1.0");
            //风险描述（隐患描述）
            riskDto.setRiskdes((String) datas[colName.indexOf("trouble_desc")]);
            //风险原因
            riskDto.setRiskcause((String) datas[colName.indexOf("result_and_affect")]);
            //控制措施
            Controlmatrix controlmatrix = new Controlmatrix();
            TblControlEntries entries = new TblControlEntries();
            entries.setField1((String) datas[colName.indexOf("rectify_measures")]);
            controlmatrix.setConkzcs((String) datas[colName.indexOf("rectify_measures")]);
            //一体化控制目标
            controlmatrix.setControldes((String) datas[colName.indexOf("rectify_process")]);
            //牵头责任部门
            String orgnuumber = (String) datas[colName.indexOf("depart_self")];
            if (orgnuumber != null && !"".equals(orgnuumber)){
                riskDto.setBelongsto(riskMapper.getNumberById(orgnuumber));
            }

            //整改部门——填写部门responsibledep
            controlmatrix.setResponsibledepname((String) datas[colName.indexOf("write_sect_name")]);
            orgnuumber =  (String) datas[colName.indexOf("write_sect_code")];
            if (orgnuumber != null && !"".equals(orgnuumber)){
                controlmatrix.setResponsibledep(riskMapper.getNumberById(orgnuumber));
            }


            //风险管控负责人 劳人号
            String username = (String) datas[colName.indexOf("resp_person_code")];
            if (username != null && !"".equals(username)){
                if(riskMapper.getUserById(username)!=null){
                    riskDto.setUserId(riskMapper.getUserById(username).toString());
                }

                //责任人
                username = (String) datas[colName.indexOf("supervise_leader_code_head")];
                controlmatrix.setControlmanager(riskMapper.getUserIdByRealName(username));
                if(riskMapper.getUserById(username)!= null){
                    entries.setField3(riskMapper.getUserById(username).toString());
                }

            }
            //整改时限
            Timestamp YJTime = (Timestamp) datas[colName.indexOf("create_time")];
            if (YJTime == null || "".equals(YJTime)){
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                sdf1.setLenient(false);
                Date date1 = sdf1.parse(String.valueOf(YJTime));
                entries.setField2(date1);
            }
//            rectify_limit_time
            //一体化管控措施编号
            controlmatrix.setControlnumber(riskcontrolNo);
            Staff staff = new Staff();
//            controlmatrix.setConmatid(BigDecimal.valueOf(123465789));
            //创建人
            username = (String) datas[colName.indexOf("create_by")];
            if (username != null && !"".equals(username)){
                staff.setStaffid(riskMapper.getUserById(username));
                //所属部门
                BigDecimal orgidByuser = riskMapper.getOrgidByuser(username);
                staff.setOrgid(orgidByuser);
            }
            //所属公司write_dept_code
            orgnuumber = (String) datas[colName.indexOf("write_dept_code")];
            if (orgnuumber != null && !"".equals(orgnuumber)){
                if(riskMapper.getNumberById(orgnuumber)!=null){
                    staff.setEmail(riskMapper.getNumberById(orgnuumber).toString());
                }
                riskDto.setRiskcatname("安全环保风险");
                String yfx = riskMapper.getOneFxId(riskMapper.getNumberById(orgnuumber));
                riskDto.setRiskcatidone(yfx);
                riskDto.setRiskcatnametwo("放射源失控风险");
                String tfx = riskMapper.getTwoFxId(riskMapper.getNumberById(orgnuumber));
                riskDto.setRiskcatid(tfx);
            }

            //创建时间
            Timestamp createTime = (Timestamp) datas[colName.indexOf("create_time")];
            Date date = null;
            if (createTime!=null && !"".equals(createTime)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                sdf.setLenient(false);
                date= sdf.parse(String.valueOf(createTime));
                System.out.println(sdf.format(date));
            }
            Risk risk = new  Risk();
            //类型


            List<TblControlEntries> list = new ArrayList<>();
            list.add(entries);
            controlmatrix.setEntries(list);
            riskDto.setParam(controlmatrix);
            this.risk_analysis_add2(token,"1",riskDto,staff,type,TOp10Id,date);
        }
        JsonBean result = ResponseFormat.retParam(1, 200, "成功");
        return result;
    }
    
    @Operation(summary = "风险管控-风险识别-风险创建 生产运行风险TOP10对接接口导入/risk/scTopTenimportRiskInfo")
    @RequestMapping(value = "/scTopTenimportRiskInfo",method = {RequestMethod.POST} ,produces = "application/json; charset=utf-8")
    public JsonBean scTopTenimportRiskInfo(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        String OrgIds[] = {"","600405010300","600404010300","600403010300","600402010300","600401010300","600406010300"};
        for (int i = 0; i < OrgIds.length; i++) {
            String orgnum = OrgIds[i];
            System.out.println(orgnum);
            String sql = "select * from lrc_bigdata.mes_top_comprehensive where if_field_risk = 'Y' and level = 'TOP10_DEPT_MANAGER'";
            if (!orgnum.equals("")){
                sql = "select * from lrc_bigdata.mes_top_comprehensive where if_field_risk = 'N' and level = 'TOP10_DEPT_MANAGER' and write_dept = "+orgnum;
            }
            System.out.println( sql);
            JsonBean jsonBean = this.executeSql(sql);
            Map<String,Object> data = (Map<String, Object>) jsonBean.getData();
            //获取列名
            List<Object> colName = (List<Object>) data.get("colName");
           /* *//*测试数据*//*
            List<Object> colName = new ArrayList<>();
            colName.add("delete_flag");
            colName.add("id_key_risk");
            colName.add("risk_description");
            colName.add("risk_subject");
            colName.add("write_code");
            colName.add("write_dept");
            colName.add("write_date");
            List<Object> dataList = new ArrayList<>();
            Object [] o  = {"0","风险编号1","风险原因1","风险名称1","星光","NO01","2025-09-03 20:19:31"};
            Object [] o2 = {"0","风险编号2","风险原因2","风险名称2","rq","NO01","2025-09-03 20:19:31"};
            dataList.add(o);
            dataList.add(o2);
            *//*测试数据*/
            for (Object s : colName){
                System.out.println(s);
            }
            //获取数据
            List<Object> dataList = (List<Object>) data.get("dataList");
            for (Object strings : dataList){
                Object [] datas = (Object[]) strings;

                //获取删除标记 看是否需要删除 1 删除  0 不删除
                String deFlag = (String) datas[colName.indexOf("delete_flag")];
                if (deFlag.equals("1")){
                    continue;
                }
                RiskDto riskDto = new RiskDto();
                //确定TOP10类型为生产类型及获取其对应ID
                String type = "scyx";
                String TOp10Id = (String) datas[colName.indexOf("id_key_risk")];


                //流程分类
                riskDto.setFlowname("安全环保类");
                //风险描述（风险原因）
                riskDto.setRiskdes((String) datas[colName.indexOf("risk_description")]);
                //风险名称、三级风险（风险单主题）
                riskDto.setRiskname((String) datas[colName.indexOf("risk_subject")]);
                //填报人
                Staff staff = new Staff();
                String username = (String) datas[colName.indexOf("write_code")];
                if(username != null && !"".equals(username)){
                    staff.setStaffid(riskMapper.getUserById(username));
                    if(riskMapper.getUserById(username)!=null){
                        riskDto.setUserId(riskMapper.getUserById(username).toString());
                    }
                    //所属部门
                    BigDecimal orgidByuser = riskMapper.getOrgidByuser(username);
                    staff.setOrgid(orgidByuser);
                }
                //所属公司
                String orgnuumber = (String) datas[colName.indexOf("write_dept")];
                if(orgnuumber != null && !"".equals(orgnuumber)){
                    if(riskMapper.getNumberById(orgnuumber)!=null){
                        staff.setEmail(riskMapper.getNumberById(orgnuumber).toString());
                        riskDto.setRiskcatname("安全环保风险");
                        String yfx = riskMapper.getOneFxId(riskMapper.getNumberById(orgnuumber));
                        riskDto.setRiskcatidone(yfx);
                        riskDto.setRiskcatnametwo("安全生产风险");
                        String tfx = riskMapper.getscTwoFxId(riskMapper.getNumberById(orgnuumber));
                        riskDto.setRiskcatid(tfx);
                    }
                }

                //创建时间
                String createTime = (String) datas[colName.indexOf("write_date")];
                Date date = null;
                if(createTime != null && !"".equals(createTime)){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setLenient(false);
                    date = sdf.parse(createTime);
                    System.out.println(sdf.format(date));
                }

                //获取风险点表格数据
                String sqlFxd = "select * from lrc_bigdata.mes_top_risk_key_point where id_key_risk = '" + TOp10Id+"'";
                System.out.println(sqlFxd);
                JsonBean jsonBeanFxd = this.executeSql(sqlFxd);
                Map<String,Object> dataFxd = (Map<String, Object>) jsonBeanFxd.getData();
                //获取列名
                List<Object> colNameFxd = (List<Object>) dataFxd.get("colName");
    /*            *//*测试数据*//*
                List<Object> colNameFxd = new ArrayList<>();
                colNameFxd.add("delete_flag");
                colNameFxd.add("id");
                colNameFxd.add("cs_desc");
                colNameFxd.add("control_measure_description");
                List<Object> dataListFxd = new ArrayList<>();
                Object [] o1  = {"0","风险点1","风险点描述1","纠正措施001"};
                Object [] o12 = {"0","风险点2","风险点描述2","纠正措施002"};
                dataListFxd.add(o1);
                dataListFxd.add(o12);
                *//*测试数据*/
                for (Object s : colNameFxd){
                    System.out.println(s);
                }
                //获取数据
                List<Object> dataListFxd = (List<Object>) dataFxd.get("dataList");
                for (Object stringsFxd : dataListFxd){
                    Object [] datasFxd = (Object[]) stringsFxd;
                    String deFlagFxd = (String) datasFxd[colNameFxd.indexOf("delete_flag")];
                    if (deFlagFxd.equals("1")){
                        continue;
                    }

                    //获取风险编号
                    String riskNo = riskService.get_riskNo(token).getData().toString();
                    System.out.println("风险编号："+riskNo);
                    //获取一体化管控措施编号
                    String riskcontrolNo = riskService.get_riskcontrol_no(token, riskNo).getData().toString();
                    System.out.println("一体化管控措施编号："+riskcontrolNo);
                    riskDto.setRisknumber(riskNo);
                    riskDto.setVersion("1.0");
                    //获取风险点ID
                    String fxdId = (String) datasFxd[colNameFxd.indexOf("id")];

                    //检查是否重复数据
                    String riskId = "";
                    if(fxdId != null && !fxdId.equals("")){
                        try {
                            riskId = riskMapper.getScRiskIdByTOPID(fxdId);
                            if (riskId != null && !riskId.equals("")){
                                log.info("Risk with fxdId {} already exists, skipping", fxdId);
                                continue;
                            }
                        } catch (Exception e) {
                            log.error("Error checking duplicate for fxdId: {}", fxdId, e);
                            // 根据业务需求决定是否继续处理其他数据
                            continue;
                        }
                    } else {
                        continue;
                    }


                    //四级风险——风险点描述
                    riskDto.setLevelFourRisk((String) datasFxd[colNameFxd.indexOf("ca_desc")]);
                    //风险原因
                    riskDto.setRiskcause((String) datasFxd[colNameFxd.indexOf("ca_desc")]);
                    //具体管控措施
                    Controlmatrix controlmatrix = new Controlmatrix();
                    //管控措施编号
                    controlmatrix.setControlnumber(riskcontrolNo);
                    //管控措施一体化控制目标
//                    controlmatrix.setControldes((String) datasFxd[colNameFxd.indexOf("control_measure_description")]);
                    controlmatrix.setControldes((String) datasFxd[colNameFxd.indexOf("control_objectives_of_point")]);
                    List<TblControlEntries> entriesList = new ArrayList<>();
                    //根据风险点获取所有的销降计划
                    //获取风险点表格数据
                    String sqlFxCs = "select * from lrc_bigdata.mes_top_list_temp where id_key_point = '" + fxdId+"'";
                    System.out.println(sqlFxCs);
                    JsonBean jsonBeanFxCs = this.executeSql(sqlFxCs);
                    Map<String,Object> dataFxCs = (Map<String, Object>) jsonBeanFxCs.getData();
                    //获取列名
                    List<Object> colNameFxCs = (List<Object>) dataFxCs.get("colName");
                   /* *//*测试数据*//*
                    List<Object> colNameFxCs = new ArrayList<>();
                    colNameFxCs.add("delete_flag");
                    colNameFxCs.add("duty_dept");
                    colNameFxCs.add("ca_desc");
                    colNameFxCs.add("create_time");
                    colNameFxCs.add("duty_man");
                    List<Object> dataListFxCs = new ArrayList<>();
                    Object [] o11  = {"0","NO01","控制措施001","2025-09-10","星光"};
                    Object [] o112 = {"0","NO01","控制措施002","2025-09-10","星光"};
                    dataListFxCs.add(o11);
                    dataListFxCs.add(o112);
                    *//*测试数据*/
                    for (Object s : colNameFxCs){
                        System.out.println(s);
                    }
                    //获取纠正措施数据
                    List<Object> dataListFxCs = (List<Object>) dataFxCs.get("dataList");

                    for (Object stringsFxCs : dataListFxCs){
                        Object [] datasFxCs = (Object[]) stringsFxCs;
                        //根据删除标记判断是否添加
                        String deFlagFxCs = (String) datasFxCs[colNameFxCs.indexOf("delete_flag")];
                        if (deFlagFxCs.equals("1")){
                            continue;
                        }
                        TblControlEntries entries = new TblControlEntries();
                        //具体责任部门
                        String orgnumber = (String) datasFxCs[colNameFxCs.indexOf("duty_dept")];
                        if (orgnumber!= null && orgnumber.equals("")){
                            entries.setField15(riskMapper.getNumberById(orgnumber).toString());
                        }
                        //具体控制i措施——销降计划
                        entries.setField1((String) datasFxCs[colNameFxCs.indexOf("ca_desc")]);
                        //预计完成时间-计划结束时间
                        Timestamp YJTime = (Timestamp) datasFxCs[colNameFxCs.indexOf("create_time")];
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        sdf1.setLenient(false);
                        Date date1 = sdf1.parse(String.valueOf(YJTime));
                        entries.setField2(date1);
                        //责任人-责任人
                        username = (String) datasFxCs[colNameFxCs.indexOf("duty_man")];
                        if(username != null && !username.equals("")){
                            entries.setField13(riskMapper.getUserById(username));
                            entries.setField3(riskMapper.getUserIdByRealName(username));
                        }
                        entriesList.add(entries);
                    }
                    //纠正措施放到管控措施里面去
                    controlmatrix.setEntries(entriesList);
                    //管控措施放到风险里面去
                    riskDto.setParam(controlmatrix);
                    //牵头责任部门固定数字
                    riskDto.setBelongsto("622104920662085");
//                    String riskId = "";
                    if(fxdId !=null && !fxdId.equals("")){
                        riskId = riskMapper.getScRiskIdByTOPID(fxdId);
                    }
                    if (riskId != null && !riskId.equals("")){
                        riskDto.setRiskid(riskMapper.getScRiskIdByTOPID(fxdId));
                    }
                    this.risk_analysis_add2(token,"1",riskDto,staff,type,fxdId,date);
                }
            }
        }
        JsonBean result = ResponseFormat.retParam(1, 200, "成功");
        return result;
    }





    public JsonBean executeSql(String sql) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if(staff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }

//        String sql = tableInfo.getSqlText().toUpperCase();

        if(sql.indexOf("CREATE") != -1 || sql.indexOf("DELETE") != -1 || sql.indexOf("UPDATE") != -1 || sql.indexOf("DROP") != -1 ) {
            return ResponseFormat.retParam(0, "sql语句参数不符合规范", null);
        }

        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        //获取数据源
        try {
//            con = DriverManager.getConnection("jdbc:dm://"+"192.0.2.200"+":"+"5236"+"/"+"REDACTED"+"?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8&serverTimezone=Asia/Shanghai", "REDACTED", "REDACTED");

            con = DriverManager.getConnection("jdbc:hive2://"+"192.0.2.200"+":"+"10000"+"/"+"lrc_bigdata", "ods_lrc_user", "Ac123456");
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            List<String> columnNameList = new ArrayList<String>(0);
            for (int i = 1 ; i <= columnCount ; i++) {
                columnNameList.add(rsmd.getColumnName(i));
            }
            resultMap.put("colName", columnNameList);

            List<Object[]> dataList = new ArrayList<Object[]>(0);
            Object[] objs = null;
            while (rs.next()) {
                objs = new Object[columnCount];

                for (int i = 0 ; i < columnCount ; i++) {
                    objs[i] = rs.getObject(i+1)==null?null:rs.getObject(i+1);
                }
                dataList.add(objs);
            }
            resultMap.put("dataList", dataList);
        }finally {
//            InceptorBaseDao.getInstance().close(con, rs, ps);
            if(rs != null){
                rs.close();
            }
            if(ps!=null){
                ps.close();
            }
            if(con!=null){
                con.close();
            }
        }
        return ResponseFormat.retParam(1, 200, resultMap);
    }



    public JsonBean risk_analysis_add2(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "isflow", description = "isflow") @RequestParam(required = false, defaultValue = "1") String isflow,
                                      @RequestBody RiskDto riskDto,
                                      @RequestBody Staff staff,
                                      @Parameter(name = "type", description = "TOP10风险类型") @RequestParam(required = false) String type,
                                      @Parameter(name = "TOp10Id", description = "TOp10Id区分添加/修改") @RequestParam(required = false) String TOp10Id,
                                      @Parameter(name = "creaDate", description = "填报时间") @RequestParam(required = false)Date creaDate
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Staff user = staff;
        Risk ris = null;
        Map<String, Object> result = new HashMap<String, Object>(0);
        String oldRiskid = riskDto.getRiskid();
        //放入业务单元业务流程信息
        Flow f = new Flow();
        f.setFlownumber(riskDto.getFlownumber());
        f.setFlowname(riskDto.getFlowname());
        f.setCompany(selectOrg.getOrgid().toString());
        if (riskDto.getFlowid() != null && !"".equals(riskDto.getFlowid())) {
            f.setFlowid(new BigDecimal(riskDto.getFlowid()));
        }
        // 放入业务单元 业务名称 业务描述信息
        FlowBussiness riskBussiness = new FlowBussiness();
        riskBussiness.setBussinessname(riskDto.getBussinessname());
        riskBussiness.setBussinessdes(riskDto.getBussinessdes());
        if (riskDto.getBussinessid() != null && !"".equals(riskDto.getBussinessid())) {
            riskBussiness.setBussinessid((long) Integer.parseInt(riskDto.getBussinessid()));
        }
        //放入风险信息
        Risk risk = new Risk();
        if (riskDto.getRiskid() != null && !"".equals(riskDto.getRiskid())) {
            risk.setRiskid(new BigDecimal(riskDto.getRiskid()));
        }
        risk.setRisknumber(riskDto.getRisknumber());
        risk.setRiskcatname(riskDto.getRiskcatname());
        risk.setRiskname(riskDto.getRiskname());
        risk.setVersion(riskDto.getVersion());
        risk.setRiskdes(riskDto.getRiskdes());
        risk.setBelongsto(riskDto.getBelongsto());
        risk.setLeadership(riskDto.getLeadership());
        risk.setCooperateOrg(riskDto.getCooperateOrg());
        risk.setCooperateOrgName(riskDto.getCooperateOrgName());
        System.out.println(riskDto.getLevelFourRisk());
        risk.setLevelFourRisk(riskDto.getLevelFourRisk());
        // risk.setReorg(reorg);
        risk.setRiskprogram(riskDto.getRiskprogram());
        risk.setRiskexternal(riskDto.getRiskexternal()); // 外部规定
        risk.setRiskcompany(riskDto.getRiskcompany()); // 公司规定
        risk.setRiskcompliance(riskDto.getRiskcompliance()); // 合规红线
        if(riskDto.getRiskcatid() != null && !"".equals(riskDto.getRiskcatid())){
            risk.setRiskcatid(new BigDecimal(riskDto.getRiskcatid()));
        }

        risk.setRisklevel(riskDto.getRisklevel());
        risk.setRiskcatnametwo(riskDto.getRiskcatnametwo());
        if (riskDto.getRiskcatidone() !=null && StringUtils.isNotBlank(riskDto.getRiskcatidone())) {
            risk.setRiskcatidone(new BigDecimal(riskDto.getRiskcatidone()));
        }
//        risk.setContent(content);
        risk.setRiskcause(riskDto.getRiskcause());
        risk.setComplianceobligation(riskDto.getComplianceobligation());
        risk.setIscurrentversion(1);
        risk.setRevisiontype(riskDto.getRevisiontype());
        //密级
        risk.setSecrectLevelId(riskDto.getSecrectLevelId());
        risk.setStaffScopeNames(riskDto.getStaffScopeNames());
        risk.setStaffScopeIds(riskDto.getStaffScopeIds());
        risk.setLinkDeptId(staff.getOrgid());
        risk.setRiskcreatedt(creaDate);
        risk.setStaffid(staff.getStaffid());
        risk.setUnit(staff.getEmail() + "");
        System.out.println(risk.getUnit());
        risk.setTop10type(type);
        risk.setTop10id(TOp10Id);

        RiskCoping cop = new RiskCoping();
        cop.setRiskhopevalue(riskDto.getRiskHopeValue());
        cop.setCopinghead(riskDto.getUserId().toString());
        cop.setCopingplot(riskDto.getCopingPlot());
        cop.setYddes(riskDto.getYddes());
        cop.setRiskcopingid(StringUtils.isNotBlank(riskDto.getCopingId()) ? new BigDecimal(riskDto.getCopingId()) : new BigDecimal(0));
        if (!Objects.isNull(riskDto.getRiskextid())) {
            //制定-初次保存版本号1.0
            if (Objects.isNull(riskDto.getRiskid())) {
                risk.setVersion("1.0");
            }
            if (Objects.equals(riskDto.getRevisiontype(), 2) && Objects.nonNull(riskDto.getRiskextid())) {
                risk.setRiskextid(BigDecimal.valueOf(riskDto.getRiskextid()));
                //修订-原风险版本号➕1
                Risk risk1 = riskService.getById(risk.getRiskextid());

                if (Objects.isNull(risk1)) {
                    return ResponseFormat.retParam(0, "修订数据不存在", null);
                } else {
                    BigDecimal currentVersion = riskService.getMaxVersion(riskDto.getRiskextid()).add(BigDecimal.valueOf(1L));
                    risk.setVersion(currentVersion.toString() + ".0");
                    //更新原来的风险不展示在版本管理里面
                    risk1.setIscurrentversion(0);
                    riskService.updateById(risk1);
                }

            }
        }
        //灵活字段

        riskService.saveNewRisk(risk, selectOrg, oldRiskid, isflow, user, f, null, riskBussiness, riskDto.getAttids(), cop);
        //新增的时候要求措施一起保存
        //控制措施保存
        Controlmatrix param = riskDto.getParam();
        BigDecimal commatid = param.getConmatid();
        param.setRiskcopingid(cop.getRiskcopingid());
        controlmatrixService.saveOrUpdate(param);
        if (Objects.isNull(commatid)) {
            riskCopingCmatrixService.save(new RiskCopingCmatrix(param.getConmatid(), param.getRiskcopingid()));//保存中间表数据
        }
        //管控措施条目信息保存；
        List<TblControlEntries> entList = param.getEntries();
        for (TblControlEntries e : entList) {
            if (e.getId() != null && e.getId().compareTo(new BigDecimal(0)) != 0) {
                tblControlEntriesMapper.updateById(e);
            } else {
                e.setId(RandomUtil.uuBigDecimalId());
                e.setConmatid(param.getConmatid());
                e.setLinkDeptId(staffUtil.getLinkDetp().getOrgid());
                e.setLinkOrgId(staffUtil.getLinkOrg().getOrgid());
                e.setCreateTime(new Date());
                e.setCreator(staffUtil.getStaffid());
                tblControlEntriesMapper.insert(e);
            }
        }
        //应对方案格式
        QueryWrapper<RiskCoping> queryWrapper = new QueryWrapper<RiskCoping>();
        queryWrapper.eq("RISKID", risk.getRiskid());
        List<RiskCoping> copings = copingService.list(queryWrapper);
        StringBuffer yddes = new StringBuffer();
        if (null != copings && copings.size() > 0) {
            RiskCoping coping = copings.get(0);
            List<Controlmatrix> cons = controlmatrixService.findTblControlmatrixByRiskCoping(coping.getRiskcopingid().toString());
            for (Controlmatrix x : cons) {
                yddes.append("一体化管控措施编号：" + x.getControlnumber() + "\n");
                yddes.append("一体化控制目标：" + x.getControldes() + "\n");
                yddes.append("控制措施：" + x.getConkzcs() + "\n");
            }
            coping.setYddes(yddes.toString());
            copingService.updateById(coping);
        }

        Flow flow = flowService.findFlowByRiskId(risk.getRiskid().toString());
        if(flow.getFlowid() != null ){
            riskBussiness = flowBussinessService.findUniqueByFlowId(flow.getFlowid());
            result.put("flow", flow);
        }
        // mv.addObject("flow", flow);
        if (StringUtils.isNotBlank(risk.getBelongsto()))
            risk.setZrbmName(organizationService.findOrgByorgId(risk.getBelongsto()));
        // ris.setXgbmName(organizationService.findOrgByorgId(ris.getReorg()));
        if (StringUtils.isNotBlank(risk.getLeadership()))
            risk.setLeadershipName(staffService.findRealNameById(risk.getLeadership())); ////查找公司责任领导、
        if (StringUtils.isNotBlank(risk.getCooperateOrg()) && StringUtils.isBlank(risk.getCooperateOrgName()))
            risk.setCooperateOrgName(organizationService.findOrgByorgId(risk.getCooperateOrg()));
        result.put("sub", 0);
        QueryWrapper<RiskCoping> queryWrapperC = new QueryWrapper<RiskCoping>();
        queryWrapperC.eq("RISKID", risk.getRiskid());
        List<RiskCoping> list = copingService.list(queryWrapperC);
        if (list != null) {
            result.put("cop", list.get(0));
        }
        if (StringUtils.isNotBlank(risk.getStepid())){
            TblNbsjAuditStepEntity tblNbsjAuditStepEntity = tblNbsjAuditStepEntityMapper.selectByStepId(risk.getStepid());

            risk.setBookid(tblNbsjAuditStepEntity.getBookid());
            risk.setSql(tblNbsjAuditStepEntity.getSqlstr());
            risk.setSteptitle(tblNbsjAuditStepEntity.getSteptitle());
        }
        List<Flow> flows = flowMapper.findTblFlowByorgId(selectOrg.getOrgid());

        result.put("flows", flows);
        //mv.addObject("flows", flows);
        if (risk != null) {
            result.put("risk", risk);
        }
//        Riskcategory riskcatselef = riskcategoryService.geTblRiskcategory(riskService.findById(risk.getRiskid().toString()).getRiskcategory().getRiskcatid());
        Riskcategory riskcatselef = riskcategoryService.getById(risk.getRiskcatid());
        risk.setRiskcategory(riskcatselef);
        result.put("riskcatid", riskDto.getRiskcatid());
        result.put("orgid", risk.getUnit());
        result.put("type", "jbxx");
        result.put("riskcatselef", riskcatselef);
        //result.put("cx", controlmatrix);
        result.put("riskBussiness", riskBussiness);
        result.put("param", param);
        JsonBean jsonBean = null;
        jsonBean = ResponseFormat.retParam(1, 200, result);

        return jsonBean;
    }
}

