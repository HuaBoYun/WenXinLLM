package com.huabo.audit.controller;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.audit.oracle.entity.TblAuditModelDataSourceOracle;
import com.huabo.audit.oracle.entity.TblAuditSituationEntity;
import com.huabo.audit.oracle.entity.TblAuditSituationSubclassEntity;
import com.huabo.audit.oracle.entity.TblComplianceWeekly;
import com.huabo.audit.oracle.entity.TblCurrentPosition;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceTypeEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;
import com.huabo.audit.oracle.entity.TblNbsjAuditprogramEntity;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;
import com.huabo.audit.oracle.entity.TblNbsjCertificate;
import com.huabo.audit.oracle.entity.TblNbsjInnerrule;
import com.huabo.audit.oracle.entity.TblNbsjOuterruleEntity;
import com.huabo.audit.oracle.entity.TblNbsjProject;
import com.huabo.audit.oracle.entity.TblNbsjQuestionType;
import com.huabo.audit.oracle.entity.TblNbsjStatType;
import com.huabo.audit.oracle.entity.TblNbsjTargettypeEntity;
import com.huabo.audit.oracle.entity.TblNbsjTempleteEntity;
import com.huabo.audit.oracle.entity.TblNbsjType;
import com.huabo.audit.oracle.entity.TblOrganization;
import com.huabo.audit.oracle.entity.TblOtherarticle;
import com.huabo.audit.oracle.entity.TblSjsQuestion;
import com.huabo.audit.oracle.entity.TblStaff;
import com.huabo.audit.oracle.mapper.TblCurrentPositionMapper;
import com.huabo.audit.oracle.mapper.TblNbsjCertificateMapper;
import com.huabo.audit.oracle.mapper.TblNbsjProjectMapper;
import com.huabo.audit.oracle.mapper.TblOrganizationMapper;
import com.huabo.audit.oracle.mapper.TblStaffMapper;
import com.huabo.audit.oracle.service.TblAuditModelDataSourceOracleService;
import com.huabo.audit.oracle.vo.TblGkProjectVo;
import com.huabo.audit.oracle.vo.TblGkQuestionVo;
import com.huabo.audit.oracle.vo.TblGkZgQuestionVo;
import com.huabo.audit.oracle.vo.TblNbsjInnerRuleVo;
import com.huabo.audit.oracle.vo.TblNbsjTempleteVo;
import com.huabo.audit.oracle.vo.TblOtherarticleVo;
import com.huabo.audit.oracle.vo.TblSjwtQuestionVo;
import com.huabo.audit.service.AttachmentService;
import com.huabo.audit.service.TblAccBookService;
import com.huabo.audit.service.TblAttachmentService;
import com.huabo.audit.service.TblAuditSituationService;
import com.huabo.audit.service.TblAuditSituationSubclassService;
import com.huabo.audit.service.TblComplianceSendService;
import com.huabo.audit.service.TblNbsjAuditExperienceTypeService;
import com.huabo.audit.service.TblNbsjAuditStepService;
import com.huabo.audit.service.TblNbsjAuditprogramService;
import com.huabo.audit.service.TblNbsjBugCriterionService;
import com.huabo.audit.service.TblNbsjCertificateService;
import com.huabo.audit.service.TblNbsjInnerRuleService;
import com.huabo.audit.service.TblNbsjOuterruleService;
import com.huabo.audit.service.TblNbsjProjectService;
import com.huabo.audit.service.TblNbsjQuestionTypeService;
import com.huabo.audit.service.TblNbsjSheetService;
import com.huabo.audit.service.TblNbsjTargettypeService;
import com.huabo.audit.service.TblNbsjTempleteService;
import com.huabo.audit.service.TblNbsjTypeService;
import com.huabo.audit.service.TblOrganizaService;
import com.huabo.audit.service.TblOtherarticleService;
import com.huabo.audit.service.TblStaffService;
import com.huabo.audit.util.DateUtils;
import com.huabo.audit.util.FileUtil;
import com.huabo.audit.util.FreeMarkerUtil;
import com.huabo.audit.util.JDBCProperties;
import com.huabo.audit.util.PageResult;
import com.huabo.audit.util.R;
import com.huabo.audit.vo.param.CheckConnectionParam;
import com.huabo.audit.vo.param.DatabaseTableFieldParam;
import com.huabo.audit.vo.param.DatabaseTableParam;
import com.huabo.audit.vo.param.ExportSqlParam;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.BookmarksNavigator;

import cn.hutool.core.io.resource.ClassPathResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 审计工作台
 */
@RestController
@Slf4j
@Tag(name="审计工作台、审计管控分析",description="审计工作台、审计管控分析")
@RequestMapping(value = "/nbsjworkSpace")
public class NbsjWorkSpaceController {

    @Resource
    public AttachmentService attachmentService;
    @Resource
    public TblNbsjTempleteService tblNbsjTempleteService;
    @Resource
    public TblNbsjTargettypeService tblNbsjTargettypeService;
    @Resource
    public TblNbsjAuditprogramService tblNbsjAuditprogramService;
    @Resource
    public TblNbsjOuterruleService tblNbsjOuterruleService;
    @Resource
    public TblNbsjInnerRuleService tblNbsjInnerRuleService;
    @Resource
    public TblNbsjBugCriterionService tblNbsjBugCriterionService;
    @Resource
    private TblNbsjTypeService tblNbsjTypeService;
    @Resource
    public FreeMarkerConfig freeMarkerConfig;
    @Resource
    public TblOrganizaService tblOrganizaService;
    @Resource
    public TblOtherarticleService tblOtherarticleService;
    @Resource
    public TblNbsjProjectService tblnbsjProjectService;
    @Resource
    private TblNbsjQuestionTypeService tblNbsjQuestionTypeService;
    @Resource
    private TblNbsjCertificateService tblNbsjCertificateService;
    @Resource
    private TblStaffMapper tblStaffMapper;
    @Resource
    private TblStaffService tblStaffService;
    @Resource
    private TblNbsjProjectMapper tblNbsjProjectMapper;
    @Resource
    private TblNbsjCertificateMapper tblNbsjCertificateMapper;
    @Resource
    private TblAttachmentService tblAttachmentService;

    @Resource
    private TblNbsjAuditExperienceTypeService typeService;

    @Resource
    private TblNbsjAuditStepService stepService;

    @Resource
    private TblAccBookService accbookservice;

    @Resource
    private TblAuditModelDataSourceOracleService tblAuditModelDataSourceOracleService;


    @Resource
    private TblNbsjSheetService tblNbsjSheetService;

    @Resource
    private TblAuditSituationService tblAuditSituationService;

    @Resource
    private TblAuditSituationSubclassService tblAuditSituationSubclassService;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblCurrentPositionMapper tblCurrentPositionMapper;
    
    @Resource
    private TblComplianceSendService tblComplianceSendService;
    
    @Resource
    private UserProvider userProvider;
    

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");//注意月份是MM


    @OperationLog(
            success = "审计指引模板库",
            busType = "智能审计",
            fail = "审计指引模板库",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取基础配置审计指引模板库列表"
    )
    @RequestMapping(value = "/getNbsjTempletePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板-列表页")
    public JsonBean getNbsjTempletePageList(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                            @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                            TblNbsjTempleteVo tblNbsjTempleteVo) {
        JsonBean jsonBean = null;
        try {
            tblNbsjTempleteVo.setTempType("0");
            jsonBean = tblNbsjTempleteService.selectNbsjTempleteListByPageInfo(token, pageNumber, pageSize, tblNbsjTempleteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    @OperationLog(
            success = "列表页",
            busType = "智能审计",
            fail = "列表页",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取指引模板-列表"
    )
    @RequestMapping(value = "/getNbsjZyTempletePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "指引模板-列表页")
    public JsonBean getNbsjZyTempletePageList(HttpServletRequest request,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                              TblNbsjTempleteVo tblNbsjTempleteVo) {
        JsonBean jsonBean = null;
        try {
            tblNbsjTempleteVo.setTempType("1");
            jsonBean = tblNbsjTempleteService.selectNbsjTempleteListByPageInfo(token, pageNumber, pageSize, tblNbsjTempleteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     *
     * TODO
     * 20230807
     * smf
     * 审计对象库-列表页
     * @param request
     * @param token
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @OperationLog(
            success = "审计对象库",
            busType = "智能审计",
            fail = "审计对象库",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计对象库-列表"
    )
    @RequestMapping(value = "/getAuditObjectLibraryPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计对象库-列表页")
    public JsonBean getAuditObjectLibraryPageList(HttpServletRequest request,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize
                                              ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            com.github.pagehelper.PageInfo<TblStaff> page = this.tblStaffService.queryAuditObjectStaffAll(pageNumber,pageSize);

            hashMap.put("pageInfo",page);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    @OperationLog(
            success = "审计对象库-新增",
            busType = "智能审计",
            fail = "审计对象库-新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计对象库-新增"
    )
    @RequestMapping(value = "/auditObjectLibraryAddOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计对象库-新增、修改")
    public JsonBean auditObjectLibraryAddOrUpdate(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "officejson", description = "任职履历", required = false) @RequestParam(value = "officejson",required = false) String officejson
            , @Parameter(name = "tblStaff", description = "用户信息", required = false) TblStaff tblStaff) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            tblStaff.setAuditState(1);
            this.tblStaffMapper.updateByPrimaryKeySelective(tblStaff);
            QueryWrapper<TblCurrentPosition> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("CPSTAFFID",tblStaff.getStaffid());
            this.tblCurrentPositionMapper.delete(queryWrapper);
            if (!StringUtils.isEmpty(officejson)) {
                List<TblCurrentPosition> positions = JSONObject.parseArray(officejson, TblCurrentPosition.class);
                for (TblCurrentPosition position : positions) {
                    position.setCpStaffId(tblStaff.getStaffid());
                    tblCurrentPositionMapper.insert(position);
                }
            }
        } catch (Exception e) {
            return  ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return  ResponseFormat.retParam(1, 200, hashMap);
    }

    @OperationLog(
            success = "查询单个信息",
            busType = "智能审计",
            fail = "查询单个信息",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取审计对象库指定人员信息"
    )
    @RequestMapping(value = "/selectAuditObjectLibraryInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计对象库-查询单个信息")
    public JsonBean selectAuditObjectLibraryInfo(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "staffId", description = "主键", required = true) @RequestParam("staffId") BigDecimal staffId) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            TblStaff staff = this.tblStaffMapper.selectById(staffId);
            QueryWrapper<TblCurrentPosition> wrapper = new QueryWrapper<>();
            wrapper.eq("CPSTAFFID",staff.getStaffid());
            List<TblCurrentPosition> positionList = this.tblCurrentPositionMapper.selectList(wrapper);
            hashMap.put("staff",staff);
            hashMap.put("positionList",positionList);
        } catch (Exception e) {
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return  ResponseFormat.retParam(1, 200, hashMap);
    }

    @OperationLog(
            success = "审计对象库-删除",
            busType = "智能审计",
            fail = "审计对象库-删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计对象库-删除"
    )
    @RequestMapping(value = "/deleteAuditObjectLibrary", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计对象库-删除")
    public JsonBean deleteAuditObjectLibrary(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "staffId", description = "主键", required = true) @RequestParam("staffId") String staffId) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            TblStaff staff = new TblStaff();
            staff.setStaffid(new BigDecimal(staffId));
            staff.setAuditState(0);
            this.tblStaffMapper.updateByPrimaryKeySelective(staff);
        } catch (Exception e) {
            return  ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }



    @OperationLog(
            success = "审计经验库列表页",
            busType = "智能审计",
            fail = "审计经验库列表页",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取审计经验库-列表页"
    )
    @RequestMapping(value = "/getNbsjjyTempletePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计经验库-列表页")
    public JsonBean getNbsjjyTempletePageList(HttpServletRequest request,
                                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                              @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                              @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                              TblNbsjTempleteVo tblNbsjTempleteVo) {
        JsonBean jsonBean = null;
        try {
            tblNbsjTempleteVo.setTempType("2");
            jsonBean = tblNbsjTempleteService.selectNbsjTempleteListByPageInfo(token, pageNumber, pageSize, tblNbsjTempleteVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @OperationLog(
            success = "审计模板新增",
            busType = "智能审计",
            fail = "审计模板新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计模板"
    )
    @RequestMapping(value = "/mergeNbsjTemplete", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板-新增、修改")
    public JsonBean mergeNbsjTemplete(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "orgids", description = "适用机构(格式：1,2,3,4)", required = true) @RequestParam(value = "orgids", required = true) String orgids
            , @Parameter(name = "templete", description = "审计模板实体", required = true) TblNbsjTempleteEntity templete) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTempleteService.mergeNbsjTempleteInfo(templete, token, orgids);
        } catch (Exception e) {
        	e.printStackTrace();
            //ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "状态修改",
            busType = "智能审计",
            fail = "状态修改",
            operationType = OperationType.UPDATE,
            subType = "基础配置——审计模板-修改状态"
    )
    @RequestMapping(value = "/updateTempleteStatus", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板-修改状态")
    public JsonBean updateStatus(HttpServletRequest request
            , @Parameter(name = "templeteId", description = "模板主键", required = true) @RequestHeader("templeteId") String templeteId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTempleteService.updateStatus(templeteId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "模板复制",
            busType = "智能审计",
            fail = "模板复制",
            operationType = OperationType.ADD,
            subType = "基础配置——复制模板到实施审计指引,复制模板为"
    )
    @RequestMapping(value = "/copyTemplete", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "模板复制功能")
    public JsonBean copyTemplete(HttpServletRequest request, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "templeteId", description = "主键", required = true) @RequestHeader("templeteId") String templeteId
            , @Parameter(name = "copytype", description = "复制类型(copytype=0，复制为审计模板；copytype=1，复制为指引模板；copytype=2，复制为审计经验库)", required = true) @RequestHeader("copytype") Integer copytype
    ) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTempleteService.copyTemplete(templeteId, copytype, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "模板信息查询",
            busType = "智能审计",
            fail = "模板信息查询",
            operationType = OperationType.ADD,
            subType = "基础配置——审计模板-查询单个信息"
    )
    @RequestMapping(value = "/selectTempleteInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板-查询单个信息")
    public JsonBean selectInfo(HttpServletRequest request
            , @Parameter(name = "templeteId", description = "主键", required = true) @RequestHeader("templeteId") String templeteId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTempleteService.selectInfo(templeteId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "模板删除",
            busType = "智能审计",
            fail = "模板删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计模板-删除"
    )
    @RequestMapping(value = "/deleteTempleteInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板-删除")
    public JsonBean deleteInfo(HttpServletRequest request
            , @Parameter(name = "templeteId", description = "主键", required = true) @RequestHeader("templeteId") String templeteId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTempleteService.deleteInfo(templeteId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "模板目录新增",
            busType = "智能审计",
            fail = "模板目录新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计模板目录-新增"
    )
    @RequestMapping(value = "/mergeTblNbsjTarget", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板目录-新增、修改")
    public JsonBean mergeTblNbsjTargettype(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "target", description = "审计模板目录实体", required = true) TblNbsjTargettypeEntity target) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjTargettypeService.mergeNbsjTargettypInfo(target, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "信息查询",
            busType = "智能审计",
            fail = "信息查询",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计模板目录-查询单个信息"
    )
    @RequestMapping(value = "/selectNbsjTargetInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板目录-查询单个信息")
    public JsonBean selectNbsjTargetInfo(HttpServletRequest request
            , @Parameter(name = "targetId", description = "审计目标id", required = true) @RequestHeader("targetId") String targetId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTargettypeService.selectInfo(targetId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "信息删除",
            busType = "智能审计",
            fail = "信息删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计模板目录-删除"
    )
    @RequestMapping(value = "/deleteNbsjTarget", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板目录-删除")
    public JsonBean deleteNbsjTarget(HttpServletRequest request
            , @Parameter(name = "targetId", description = "审计目标id", required = true) @RequestHeader("targetId") String targetId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTargettypeService.deleteInfo(targetId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "左侧树结构",
            busType = "智能审计",
            fail = "左侧树结构",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计模板目录-左侧树结构"
    )
    @RequestMapping(value = "/findNbsjTargetTree", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板目录-左侧树结构")
    public JsonBean findNbsjTargetTree(HttpServletRequest request
            , @Parameter(name = "templeteId", description = "模板id", required = true) @RequestHeader("templeteId") String templeteId
            , @Parameter(name = "nodeId", description = "父节点id") @RequestHeader("nodeId") String nodeId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTargettypeService.findNbsjTargetTree(templeteId, nodeId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "左侧树结构2",
            busType = "智能审计",
            fail = "左侧树结构2",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计模板目录-左侧树结构2"
    )
    @RequestMapping(value = "/findNbsjTargetTree2", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模板目录-左侧树结构2")
    public String findNbsjTargetTree2(HttpServletRequest request
            , @Parameter(name = "templeteId", description = "模板id", required = true) @RequestHeader("templeteId") String templeteId
    ) throws Exception {
        String result = null;
        result = tblNbsjTargettypeService.getTargetTree(templeteId);
        return result;
    }

    @OperationLog(
            success = "列表页",
            busType = "智能审计",
            fail = "列表页",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取审计指引-列表页"
    )
    @RequestMapping(value = "/getTblNbsjAuditprogramPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计指引-列表页")
    public JsonBean getTblNbsjAuditprogramPageList(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber
            , @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
            , @Parameter(name = "templeteId", description = "模板id", required = true) @RequestHeader("templeteId") String templeteId
            , @Parameter(name = "targetId", description = "所属目录id", required = false) @RequestHeader("targetId") String targetId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAuditprogramService.selectTblNbsjAuditprogramListByPageInfo(token, pageNumber, pageSize, templeteId, targetId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "列表页新增",
            busType = "智能审计",
            fail = "列表页新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计指引-列表页新增数据"
    )
    @RequestMapping(value = "/mergeTblNbsjAuditprogram", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计指引-新增、修改")
    public JsonBean mergeTblNbsjAuditprogram(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "program", description = "审计指引实体", required = true) TblNbsjAuditprogramEntity program) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjAuditprogramService.mergeTblNbsjAuditprogramInfo(program, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计指引查询单个信息",
            busType = "智能审计",
            fail = "审计指引查询单个信息",
            operationType = OperationType.SELECT,
            subType = "基础配置——获取审计指引-查询单个信息"
    )
    @RequestMapping(value = "/selectNbsjAuditprogram", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计指引-查询单个信息")
    public JsonBean selectNbsjAuditprogram(HttpServletRequest request
            , @Parameter(name = "programId", description = "审计指引id", required = true) @RequestHeader("programId") String programId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditprogramService.selectInfo(programId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计指引-删除",
            busType = "智能审计",
            fail = "审计指引-删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计指引-删除【{{programId}}】"
    )
    @RequestMapping(value = "/deleteNbsjAuditprogram", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计指引-删除")
    public JsonBean deleteNbsjAuditprogram(HttpServletRequest request
            , @Parameter(name = "programId", description = "审计指引id", required = true) @RequestHeader("programId") String programId) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjAuditprogramService.deleteInfo(programId);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @OperationLog(
            success = "审计指引-列表页",
            busType = "智能审计",
            fail = "审计指引-列表页",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计指引-列表页"
    )
    @RequestMapping(value = "/getNbsjTypeListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计类型-列表页")
    public JsonBean getNbsjTypeListPage(HttpServletRequest request,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                        @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                        @Parameter(name = "auditType", description = "审计类型名称", required = false) @RequestParam(value = "auditType", required = false) String auditType,
                                        @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTypeService.getNbsjTypeListPage(token, pageNumber, pageSize,auditType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计类型",
            busType = "智能审计",
            fail = "审计类型",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计类型-项目管理下拉菜单，获取审计类型"
    )
    @GetMapping(value = "/getNbsjTypeAllList", produces = "application/json; charset=utf-8")
    @Operation(summary = "审计类型-项目管理下拉菜单")
    public JsonBean getNbsjTypeAllList(HttpServletRequest request,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTypeService.getNbsjTypeAllList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计类型",
            busType = "智能审计",
            fail = "审计类型",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计类型-项目管理下拉菜单，获取审计类型"
    )
    @RequestMapping(value = "/sjtype/sjtypeinfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计类型-详情页")
    public JsonBean sjtype_update(@RequestParam(value = "typeid", required = true) @Parameter(name = "typeid", description = "审计类型id", required = true) String typeid,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTypeService.SelectNbsjType(typeid, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计类型新增",
            busType = "智能审计",
            fail = "审计类型新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计类型-新增"
    )
    @RequestMapping(value = "/sjtype/sjtype_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计类型-新增及修改")
    public JsonBean sjtype_save(HttpServletRequest request, @Parameter(name = "nbsjType", description = "审计类型实体", required = true) TblNbsjType nbsjType,
                                @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTypeService.saveNbsjType(nbsjType, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计类型删除",
            busType = "智能审计",
            fail = "审计类型删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计问题类型-删除"
    )
    @RequestMapping(value = "/sjtype/sjtype_delete", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计类型-删除")
    public JsonBean removePlanProjectInfo(HttpServletRequest request,
                                          @RequestParam(value = "typeid", required = true) @Parameter(name = "typeid", description = "审计类型id", required = true) String typeid,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjTypeService.delNbsjType(typeid, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "缺陷列表页",
            busType = "智能审计",
            fail = "缺陷列表页",
            operationType = OperationType.SELECT,
            subType = "基础配置——查询获取缺陷标准-列表页"
    )
    @RequestMapping(value = "/getNbsjBugCriterionPageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷标准-列表页")
    public JsonBean getNbsjBugCriterionPageList(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "bugtype", description = "缺陷类型", required = false) @RequestParam(value = "bugtype", required = false) String bugtype
            , @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber
            , @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjBugCriterionService.selectNbsjBugCriterionByPageInfo(token, pageNumber, pageSize,bugtype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @OperationLog(
            success = "缺陷列表新增",
            busType = "智能审计",
            fail = "缺陷列表新增",
            operationType = OperationType.ADD,
            subType = "基础配置——缺陷标准-新增"
    )
    @RequestMapping(value = "/gzdg/def_quexian_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷标准-新增及修改")
    public JsonBean def_quexian_save(HttpServletRequest request, @Parameter(name = "nbsjType", description = "缺陷标准实体", required = true) TblNbsjBugCriterion bug,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugCriterionService.saveNbsjBugCriterion(bug, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "查询单个信息",
            busType = "智能审计",
            fail = "查询单个信息",
            operationType = OperationType.SELECT,
            subType = "基础配置——缺陷标准-查询记录详细信息"
    )
    @RequestMapping(value = "/gzdg/def_quexian_update", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷标准-查询单个信息")
    public JsonBean def_quexian_update(@RequestParam(value = "bugcriid", required = true) @Parameter(name = "bugcriid", description = "缺陷id", required = true) String bugcriid,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugCriterionService.SelectNbsjBugCriterion(bugcriid, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "删除缺陷标准",
            busType = "智能审计",
            fail = "删除缺陷标准",
            operationType = OperationType.DELETE,
            subType = "基础配置——删除缺陷标准"
    )
    @RequestMapping(value = "/gzdg/def_quexian_del", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "缺陷标准-删除")
    public JsonBean def_quexian_del(HttpServletRequest request,
                                    @RequestParam(value = "bugcriid", required = true) @Parameter(name = "bugcriid", description = "缺陷标准id", required = true) String bugcriid,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjBugCriterionService.delNbsjBugCriterion(bugcriid, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    //法律规章  新增、修改、删除、导出、预览、列表页
    @OperationLog(
            success = "法律章程列表页",
            busType = "智能审计",
            fail = "法律章程列表页",
            operationType = OperationType.SELECT,
            subType = "管理制度——法律章程列表页"
    )
    @GetMapping("/gkzk/mag/out_list")
    @Operation(summary = "法律规章列表分页功能")
    public JsonBean getAuditPlanPageList(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                         @Parameter(name = "prulenumber", description = "审计查询条件-发文字号", required = false) @RequestParam(value = "prulenumber", required = false) String prulenumber,
                                         @Parameter(name = "prulename", description = "审计查询条件-文件名称", required = false) @RequestParam(value = "prulename", required = false) String prulename,
                                         @Parameter(name = "content", description = "审计查询条件-内容", required = false) @RequestParam(value = "content", required = false) String content,
                                         @Parameter(name = "type", description = "法律规章类别条件  审计：nbsj", required = true) @RequestParam(value = "type", required = true) String type) {
        JsonBean jsonBean = null;
        try {
            TblNbsjOuterruleEntity paream = new TblNbsjOuterruleEntity();
            if (content != null && content.trim().length() > 0) {
                paream.setBodyinfo(content);
            }
            if (prulename != null && prulename.length() > 0) {
                paream.setRulename(prulename);
            }
            if (prulenumber != null && prulenumber.length() > 0) {
                paream.setRulenumber(prulenumber);
            }
            jsonBean = tblNbsjOuterruleService.findOuterRuleList(token, pageNumber, pageSize, paream, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @OperationLog(
            success = "法律章程记录详情",
            busType = "智能审计",
            fail = "法律章程记录详情",
            operationType = OperationType.SELECT,
            subType = "管理制度——法律章程记录详细内容"
    )
    @GetMapping("/gkzk/mag/modify_out")
    @Operation(summary = "法律规章-详情及修改获取数据及预览获取数据返回类中的bodyinfo")
    public JsonBean getAuditPlanInfo(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "outrulid", description = "法律规章主键", required = true) @RequestParam(value = "outrulid", required = true) String outrulid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjOuterruleService.findById(token, outrulid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "法律章程删除",
            busType = "智能审计",
            fail = "法律章程删除",
            operationType = OperationType.DELETE,
            subType = "管理制度——法律章程删除"
    )
    @GetMapping("/gkzk/mag/delete_out")
    @Operation(summary = "法律规章-删除数据")
    public JsonBean delete_out(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "outrulid", description = "法律规章主键", required = true) @RequestParam(value = "outrulid", required = true) String outrulid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            resultMap = tblNbsjOuterruleService.delete(token, outrulid);
        } catch (Exception e) {
//			resultMap.put("code", "0");
//			resultMap.put("msg", "该条法律章程正在使用！");
            e.printStackTrace();
            return new JsonBean(0, "该条法律章程正在使用！", null);

        }
        return ResponseFormat.retParam(1, 200, resultMap);
//        return resultMap.toString();
    }

    @OperationLog(
            success = "法律章程新增",
            busType = "智能审计",
            fail = "法律章程新增",
            operationType = OperationType.ADD,
            subType = "管理制度——法律章程新增记录"
    )
    @RequestMapping(value = "/gkzk/mag/save_modify_out", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "法律规章新增或修改")
    public JsonBean mergePlanProjectManageInfo(HttpServletRequest request, @Parameter(name = "outer", description = "法律规章项目实体", required = true) TblNbsjOuterruleEntity outer,
                                               @Parameter(name = "outerId", description = "法律规章主键 ，如果主键为空则根据编码新增法律规章信息", required = false) @RequestParam(value = "outerId", required = false) BigDecimal outerId,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attIds", required = false) String attIds) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjOuterruleService.mergeOuterruleInfo(token, outer, outerId, attIds);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    //==

    /**
     * 法律规章-附件列表
     */
    @OperationLog(
            success = "附件列表",
            busType = "智能审计",
            fail = "附件列表",
            operationType = OperationType.SELECT,
            subType = "管理制度——法律章程附件列表"
    )
    @GetMapping("/gkzk/mag/out_file_list")
    @Operation(summary = "法律规章-附件列表")
    public JsonBean out_file_list(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "outerId", description = "业务主键", required = false) @RequestParam(value = "outerId", required = false) BigDecimal outerId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.outFileList(token, outerId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 法律规章-附件删除
     */
    @OperationLog(
            success = "附件删除",
            busType = "智能审计",
            fail = "附件删除",
            operationType = OperationType.DELETE,
            subType = "管理制度——法律章程删除附件"
    )
    @GetMapping("/gkzk/mag/out_file_del")
    @Operation(summary = "法律规章-附件删除")
    public R out_file_del(HttpServletRequest request, HttpServletResponse response,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjOuterruleService.removeAttInfoByAttId(token, attId);
    }


    /**
     * 法律规章-导出
     *
     * @param request
     * @param response
     * @param id
     * @throws Exception
     */
    @OperationLog(
            success = "法律规章导出",
            busType = "智能审计",
            fail = "法律规章导出",
            operationType = OperationType.EXPORT,
            subType = "管理制度——法律章程导出文件"
    )
    @RequestMapping(value = "/expOuterRuleFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "法律规章导出")
    public void expOuterRuleFile(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "id", description = "主键", required = true) @RequestHeader("id") String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        TblNbsjOuterruleEntity info = tblNbsjOuterruleService.findByid(id);

        map.put("repdesc", info.getBodyinfo());
        String fileName = info.getRulename() + ".doc";

        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        Boolean flag = (Boolean) FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        if (!flag) {//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH + "/" + fileName;
        FileUtil.downLoad(fileName1, response, false, fileName);
        FileUtil.deleteFile(fileName);
    }

    @OperationLog(
            success = "行业树",
            busType = "智能审计",
            fail = "行业树",
            operationType = OperationType.SELECT,
            subType = "智能审计——获取行业树相关信息"
    )
    @RequestMapping(value = "/org/hy_left", method = {RequestMethod.POST})
    @Operation(summary = "行业树")
    public String orgHy_Left(HttpServletRequest request) {
        String result = null;
        List<TblOrganization> tree = tblOrganizaService.getHyOrgTree();
        TblOrganization org = tblOrganizaService.getHY();
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        resultMap.put("tree", tree);
        resultMap.put("orgid", org.getOrgid());
        resultMap.put("treeName", org.getOrgname());
        resultMap.put("targetFrame", "mainFramex");
        JSONObject json = new JSONObject(resultMap);
        result = json.toString();
        return result;
    }


    @OperationLog(
            success = "行业知识库列表页",
            busType = "智能审计",
            fail = "行业知识库列表页",
            operationType = OperationType.SELECT,
            subType = "智能审计——获取行业知识库-列表页"
    )
    @RequestMapping(value = "/getOtherarticlePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "行业知识库-列表页")
    public JsonBean getOtherarticlePageList(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                            @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                            TblOtherarticleVo tblOtherarticleVo) {
        JsonBean jsonBean = null;

        try {
            jsonBean = tblOtherarticleService.selectOtherarticlePageInfo(token, pageNumber, pageSize, tblOtherarticleVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "行业知识库新增",
            busType = "智能审计",
            fail = "行业知识库新增",
            operationType = OperationType.ADD,
            subType = "智能审计——获取行业知识库-列表页新增数据"
    )
    @RequestMapping(value = "/mergeOtherarticle", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "行业知识库-新增、修改")
    public JsonBean mergeOtherarticle(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attIds", required = false) String attIds
            , @Parameter(name = "article", description = "知识库实体", required = true) TblOtherarticle tblOtherarticle) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblOtherarticleService.mergeOtherarticleInfo(tblOtherarticle, token, attIds);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "查询知识库单个信息",
            busType = "智能审计",
            fail = "查询知识库单个信息",
            operationType = OperationType.SELECT,
            subType = "智能审计获取行业知识库-单个信息详细内容"
    )
    @RequestMapping(value = "/selectOtherarticleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "行业知识库-查询单个信息")
    public JsonBean selectOtherarticleInfo(HttpServletRequest request
            , @Parameter(name = "othartid", description = "主键", required = true) @RequestHeader("othartid") String othartid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblOtherarticleService.selectInfo(othartid);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "行业知识库删除",
            busType = "智能审计",
            fail = "行业知识库删除",
            operationType = OperationType.DELETE,
            subType = "智能审计——行业知识库-删除数据"
    )
    @RequestMapping(value = "/deleteOtherarticleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "行业知识库-删除")
    public JsonBean deleteOtherarticleInfo(HttpServletRequest request
            , @Parameter(name = "othartid", description = "主键", required = true) @RequestHeader("othartid") String othartid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblOtherarticleService.deleteInfo(othartid);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "集团制度列表页",
            busType = "智能审计",
            fail = "集团制度列表页",
            operationType = OperationType.SELECT,
            subType = "管理制度——集团制度列表相关信息"
    )
    @RequestMapping(value = "/getInnerRulePageList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-列表页")
    public JsonBean getInnerRulePageList(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                         TblNbsjInnerRuleVo tblNbsjInnerRuleVo) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjInnerRuleService.selectInnerrulePageInfo(token, pageNumber, pageSize, tblNbsjInnerRuleVo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "查看类型",
            busType = "智能审计",
            fail = "查看类型",
            operationType = OperationType.SELECT,
            subType = "管理制度——集团制度类型列表相关信息"
    )
    @RequestMapping(value = "/getInnerRuleType", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-查看类型")
    public JsonBean getInnerRuleType(HttpServletRequest request,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "innruletype", description = "分页当前页数", required = false) @RequestParam(value = "innruletype", required = false) String innruletype) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjInnerRuleService.getInnerRuleType(token, innruletype);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    @OperationLog(
            success = "新增数据",
            busType = "智能审计",
            fail = "新增数据",
            operationType = OperationType.ADD,
            subType = "管理制度——集团制度类型列表新增数据"
    )
    @RequestMapping(value = "/mergeInnerRule", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-新增、修改")
    public JsonBean mergeInnerRule(HttpServletRequest request
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
            , @Parameter(name = "attIds", description = "附件主键数组 示例1,2,3,4", required = false) @RequestParam(value = "attIds", required = false) String attIds
            , @Parameter(name = "tblNbsjInnerrule", description = "管理制度", required = true) TblNbsjInnerrule tblNbsjInnerrule) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjInnerRuleService.mergeInnerruleInfo(tblNbsjInnerrule, token, attIds);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "查询单个信息",
            busType = "智能审计",
            fail = "查询单个信息",
            operationType = OperationType.SELECT,
            subType = "管理制度——集团制度指定数据的详细信息"
    )
    @RequestMapping(value = "/selectInnerRuleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-查询单个信息")
    public JsonBean selectInnerRuleInfo(HttpServletRequest request
            , @Parameter(name = "innerid", description = "主键", required = true) @RequestHeader("innerid") String innerid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjInnerRuleService.selectInfo(innerid);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "管理制度删除",
            busType = "智能审计",
            fail = "管理制度删除",
            operationType = OperationType.DELETE,
            subType = "管理制度——集团制度指定数据删除"
    )
    @RequestMapping(value = "/deleteInnerRuleInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "管理制度-删除")
    public JsonBean deleteInnerRuleInfo(HttpServletRequest request
            , @Parameter(name = "innerid", description = "主键", required = true) @RequestHeader("innerid") String innerid) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjInnerRuleService.deleteInfo(innerid);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 管理制度-附件删除
     */
    @OperationLog(
            success = "附件删除",
            busType = "智能审计",
            fail = "附件删除",
            operationType = OperationType.DELETE,
            subType = "管理制度——集团制度指定附件删除"
    )
    @GetMapping("/deleteInnerRuleAtt")
    @Operation(summary = "管理制度-附件删除")
    public R deleteInnerRuleAtt(HttpServletRequest request, HttpServletResponse response,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjInnerRuleService.removeAttInfoByAttId(token, attId);
    }

    /**
     * 管理制度-导出
     *
     * @param request
     * @param response
     * @param id
     * @throws Exception
     */
    @OperationLog(
            success = "管理制度导出",
            busType = "智能审计",
            fail = "管理制度导出",
            operationType = OperationType.EXPORT,
            subType = "管理制度——集团制度数据导出"
    )
    @RequestMapping(value = "/expInnerRuleFile", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "管理制度-导出")
    public void expInnerRuleFile(HttpServletRequest request, HttpServletResponse response,
                                 @Parameter(name = "id", description = "主键", required = true) @RequestHeader("id") String id) throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        TblNbsjInnerrule info = this.tblNbsjInnerRuleService.getInfo(id);

        map.put("repdesc", info.getBodyinfo());
        String fileName = info.getRulename() + ".doc";

        String FREEMARKER_PATH = request.getSession().getServletContext().getRealPath("/template/doc");
        Boolean flag = (Boolean) FreeMarkerUtil.htmlFileHasExist(request, FREEMARKER_PATH, fileName).get("exist");
        if (!flag) {//如何静态文件不存在，重新生成
            FreeMarkerUtil.createHtml(freeMarkerConfig, "static.ftl", request, map, FREEMARKER_PATH, fileName);//根据模板生成静态页面
        }
        String fileName1 = FREEMARKER_PATH + "/" + fileName;
        FileUtil.downLoad(fileName1, response, false, fileName);
        FileUtil.deleteFile(fileName);
    }

    @OperationLog(
            success = "列表页",
            busType = "智能审计",
            fail = "列表页",
            operationType = OperationType.SELECT,
            subType = "成果运用——项目情况分析列表页"
    )
    @RequestMapping(value = "/getGkProjectInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "项目情况分析-列表页")
    public JsonBean getGkProjectInfo(HttpServletRequest request,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                     TblGkProjectVo project) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.getGkProjectInfo(token, pageNumber, pageSize, project);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 项目情况分析-导出
     */
    @OperationLog(
            success = "导出",
            busType = "智能审计",
            fail = "导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——项目情况分析-导出"
    )
    @RequestMapping(value = "/getGkProjectInfoExport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "项目情况分析-导出")
    public void getGkProjectInfoExport(HttpServletRequest request, HttpServletResponse response,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, TblGkProjectVo project) {
        try {
            String[] cNames = {"项目编号", "项目名称", "审计单位", "被审计对象", "项目负责人", "项目目前状态", "计划年度", "计划开始时间", "计划结束时间", "项目实施时间（天）", "项目费用估算（元）"};
            List<Object[]> contractlist = tblnbsjProjectService.getGkProjectInfoExport(token, project);
//            response.setHeader("Content-Disposition", "attachment;filename=" + new String("项目情况分析".getBytes(), "UTF-8") + ".xlsx");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + java.net.URLEncoder.encode("项目情况分析.xlsx", "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OperationLog(
            success = "审计问题分析-列表页",
            busType = "智能审计",
            fail = "审计问题分析-列表页",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计问题分析-列表页内容"
    )
    @RequestMapping(value = "/getGkQuestionInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计问题分析-列表页")
    public JsonBean getGkQuestionInfo(HttpServletRequest request,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                      @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                      TblGkQuestionVo question) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblnbsjProjectService.getGkQuestionInfo(token, pageNumber, pageSize, question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 审计问题分析-导出
     */
    @OperationLog(
            success = "审计问题分析-导出",
            busType = "智能审计",
            fail = "审计问题分析-导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——导出审计问题分析相关内容"
    )
    @RequestMapping(value = "/getGkQuestionInfoExport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计问题分析-导出")
    public void getGkQuestionInfoExport(HttpServletRequest request, HttpServletResponse response,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, TblGkQuestionVo question) {
        try {
            String[] cNames = {"项目编号", "项目名称","问题类型","计划年度", "问题标题", "审计发现", "审计单位", "被审计对象", "发现人"};
            List<Object[]> contractlist = tblnbsjProjectService.getGkQuestionInfoExport(token, question);
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("审计问题分析.xlsx"));
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OperationLog(
            success = "整改问题分析列表",
            busType = "智能审计",
            fail = "整改问题分析列表",
            operationType = OperationType.SELECT,
            subType = "成果运用——整改问题分析-列表页内容"
    )
    @RequestMapping(value = "/getGkZgContentInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改问题分析-列表页")
    public JsonBean getGkZgContentInfo(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                       @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                       TblSjwtQuestionVo vo) {
        JsonBean jsonBean = null;
        try {
            TblGkZgQuestionVo question=new TblGkZgQuestionVo();
            question.setPROJECTCODE(vo.getProjectCode());
            question.setPRJOECTNAME(vo.getPrjoectName());
            question.setAUDITORGNAME(vo.getAuditOrgName());
            question.setORGNAME(vo.getOrgname());
            jsonBean = tblnbsjProjectService.getGkZgContentInfo(token, pageNumber, pageSize, question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 整改问题分析-导出
     */
    @OperationLog(
            success = "整改问题分析导出",
            busType = "智能审计",
            fail = "整改问题分析导出",
            operationType = OperationType.EXPORT,
            subType = "成果运用——整改问题分析-导出相关文件"
    )
    @RequestMapping(value = "/getGkZgContentInfoExport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "整改问题分析-导出")
    public void getGkZgContentInfoExport(HttpServletRequest request, HttpServletResponse response,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         TblGkZgQuestionVo question) {
        try {
            String[] cNames = {"项目编号", "项目名称", "审计单位", "被审计对象", "计划年度", "发现问题数量", "待整改问题数量", "已整改问题数量", "未整改问题数量"};
            List<Object[]> contractlist = tblnbsjProjectService.getGkZgContentInfoExport(token, question);
            response.setHeader("Content-Disposition", IpUtil.encodeContentDisposition("整改问题分析.xlsx"));
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 审计问题类型
     */
    @OperationLog(
            success = "审计问题类型列表",
            busType = "智能审计",
            fail = "审计问题类型列表",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计问题类型列表"
    )
    @RequestMapping(value = "/getNbsjQuestionTypeListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计问题类型-列表")
    public JsonBean getNbsjQuestionTypeListPage(HttpServletRequest request,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.getNbsjQuestionTypeListPage(token, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 整改问题分析-数量详情
     */
    @OperationLog(
            success = "数量详情",
            busType = "智能审计",
            fail = "数量详情",
            operationType = OperationType.SELECT,
            subType = "成果运用——获取整改问题分析-数量详情"
    )
    @GetMapping("/getSheetsl")
    @Operation(summary = "整改问题分析-数量详情")
    public JsonBean getSheetsl(HttpServletRequest request,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "projectid", description = "项目ID", required = false) @RequestParam(value = "projectid", required = false) String projectid,
                               @Parameter(name = "type", description = "类别：1发现问题数量详情，2待整改问题数量详情，3已整改问题数量详情，4未整改问题数量详情", required = false) @RequestParam(value = "type", required = false) String type) {

        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjSheetService.findbywtsl(token, type, projectid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }


    /**
     * 审计问题类型
     */
    @OperationLog(
            success = "问题类型",
            busType = "整改追责",
            fail = "问题类型",
            operationType = OperationType.SELECT,
            subType = "问题汇总——获取审计问题汇总内部问题类型列表"
    )
    @RequestMapping(value = "/getNbsjQuestionTypeList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "供审计底稿选择审计问题类型")
    public JsonBean getNbsjQuestionTypeList(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.getNbsjQuestionTypeList(token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "问题类型详情",
            busType = "智能审计",
            fail = "问题类型详情",
            operationType = OperationType.SELECT,
            subType = "基础配置——审计问题类型-详情页"
    )
    @RequestMapping(value = "/sjwttype/sjwttypeinfo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计问题类型-详情页")
    public JsonBean sjwttypeinfo(@RequestParam(value = "typeId", required = true) @Parameter(name = "typeId", description = "审计问题类型id", required = true) String typeId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.getNbsjQuestionType(typeId, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "问题类型新增",
            busType = "智能审计",
            fail = "问题类型新增",
            operationType = OperationType.ADD,
            subType = "基础配置——审计问题类型新增"
    )
    @RequestMapping(value = "/sjwttype/sjwttype_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计问题类型-新增及修改")
    public JsonBean sjwttype_save(HttpServletRequest request, @Parameter(name = "sqt", description = "审计类型实体", required = true) TblNbsjQuestionType sqt,
                                  @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.saveNbsjQuestionType(sqt, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "问题类型删除",
            busType = "智能审计",
            fail = "问题类型删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——审计问题类型删除【{{typeId}}】"
    )
    @RequestMapping(value = "/sjwttype/sjwttype_del", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "审计问题类型-删除")
    public JsonBean sjwttype_del(HttpServletRequest request,
                                 @RequestParam(value = "typeId", required = true) @Parameter(name = "typeId", description = "审计问题类型id", required = true) String typeId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.delNbsjType(typeId, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计情况统计表列表
     * 20230804
     */
    @OperationLog(
            success = "审计情况统计表",
            busType = "智能审计",
            fail = "审计情况统计表",
            operationType = OperationType.SELECT,
            subType = "成果运用——获取审计情况统计表-列表。"
    )
    @RequestMapping(value = "/qzd/getNbsjSituationListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计情况统计表-列表")
    public JsonBean getNbsjSituationListPage(HttpServletRequest request,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                               @Parameter(name = "year", description = "年份", required = false) @RequestParam(value = "year", required = false) String year,
                                               @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize
                                     ) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
//        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
//        if (staffUtil == null){
//            return  ResponseFormat.retParam(0, 20006, hashMap);
//        }
        try {
            PageResult<TblAuditSituationEntity> entityPage = this.tblAuditSituationService.querySituationListPage(token,pageNumber, pageSize,year);
            hashMap.put("pageInfo",entityPage);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     *   审计情况统计表增加
     *   审计情况统计表修改
     *   20230804
     * @param request
     * @param situationEntity
     * @param token
     * @param situationjson
     * @return
     * @throws Exception
     */
    @OperationLog(
            success = "审计情况统计表新增",
            busType = "智能审计",
            fail = "审计情况统计表新增",
            operationType = OperationType.ADD,
            subType = "成果运用——获取审计情况统计表-列表新增数据。"
    )
    @RequestMapping(value = "/qzd/situationInsertOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计情况统计表-新增与修改")
    public JsonBean SituationSave(HttpServletRequest request,
                                   @Parameter(name = "situationEntity", description = "实体", required = true) TblAuditSituationEntity situationEntity,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                   @Parameter(name = "situationjson", description = "子表拼接字符串", required = false) @RequestParam(value="situationjson",required=false)String situationjson) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            //不为空的话进行修改;
            if (situationEntity.getId() != null){
                tblAuditSituationService.SituationUpdate(situationEntity,situationjson);
            }else {
                situationEntity.setCreateStaffid(staffUtil.getStaffid());
                situationEntity.setCreateTime(new Date());
                tblAuditSituationService.SituationAdd(situationEntity,situationjson);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return   ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 审计情况统计表详情
     * 20230804
     */
    @OperationLog(
            success = "审计情况统计表明细",
            busType = "智能审计",
            fail = "审计情况统计表明细",
            operationType = OperationType.SELECT,
            subType = "成果运用——获取审计情况统计表-内容明细。"
    )
    @GetMapping("/qzd/situation_details")
    @Operation(summary = " 审计情况统计表详情-明细")
    public JsonBean situation_details(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "situationId", description = "主键", required = true) @RequestParam(value = "situationId", required = true) BigDecimal situationId) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            TblAuditSituationEntity tblAuditSituationEntity = tblAuditSituationService.situation_details(situationId);
            List<TblAuditSituationSubclassEntity> tblAuditSituationSubclassEntities = tblAuditSituationSubclassService.situationSubclassDetails(situationId);
            hashMap.put("SituationEntity",tblAuditSituationEntity);
            hashMap.put("SubclassEntities",tblAuditSituationSubclassEntities);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     * 审计情况统计表删除
     * 审计情况统计表效验
     * 20230804
     */
    @OperationLog(
            success = "审计情况统计表删除",
            busType = "智能审计",
            fail = "审计情况统计表删除",
            operationType = OperationType.DELETE,
            subType = "成果运用——获取审计情况统计表-删除记录。"
    )
    @GetMapping("/qzd/situation_del")
    @Operation(summary = "审计情况统计表-删除")
    public JsonBean situation_del(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "situationId", description = "主键", required = true) @RequestParam(value = "situationId", required = true) BigDecimal situationId) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
             tblAuditSituationService.situationDelete(situationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }

    /**
     *
     * 年度审计情况统计表效验
     * 根据年份进行判断当年是否已创建；
     * 20230804
     *
     */
    @OperationLog(
            success = "审计情况统计表-效验",
            busType = "智能审计",
            fail = "审计情况统计表-效验",
            operationType = OperationType.SELECT,
            subType = "成果运用——审计情况统计表-效验"
    )
    @GetMapping("/qzd/situation_validation")
    @Operation(summary = "审计情况统计表-效验")
    public JsonBean situation_validation(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "year", description = "年份", required = true) @RequestParam(value = "year", required = true) Integer year) throws Exception {
        Map<String, Object> hashMap = new HashMap<>();
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null){
            return  ResponseFormat.retParam(0, 20006, hashMap);
        }
        try {
            Integer yearNow = tblAuditSituationService.situationValidationYear(year);
            if (yearNow != 0 ){
                return ResponseFormat.retParam(0, 90011, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseFormat.retParam(1, 200, hashMap);
    }
    /**
     * 审计取证单
     */
    @OperationLog(
            success = "审计情况统计表-效验",
            busType = "智能审计",
            fail = "审计情况统计表-效验",
            operationType = OperationType.SELECT,
            subType = "审计实施——审计取证单-效验"
    )
    @RequestMapping(value = "/qzd/getNbsjCertificateListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计取证单-列表")
    public JsonBean getNbsjCertificateListPage(HttpServletRequest request,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                               @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                               @Parameter(name = "projectId", description = "项目id", required = false) @RequestParam(value = "projectId", required = false) BigDecimal projectId,
                                               @Parameter(name = "projectName", description = "项目名称", required = false) String projectName,
                                               @Parameter(name = "auditMatter", description = "审计事项", required = false) String auditMatter,
                                               @Parameter(name = "auditAbstract", description = "审计事项摘要", required = false) String auditAbstract) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjCertificateService.getNbsjCertificateListPage(token, pageNumber, pageSize, projectName, auditMatter, auditAbstract, projectId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "审计取证单新增",
            busType = "智能审计",
            fail = "审计取证单新增",
            operationType = OperationType.ADD,
            subType = "审计实施——审计取证单新增数据"
    )
    @RequestMapping(value = "/qzd/certificateSave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计取证单-新增与修改")
    public JsonBean workReportSave(HttpServletRequest request,
                                   @Parameter(name = "certificate", description = "实体", required = true) TblNbsjCertificate certificate,
                                   @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token,
                                   @Parameter(name = "attids", description = "附件id数组", required = false) String attids) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjCertificateService.certificateSave(certificate, token, attids);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }

    @OperationLog(
            success = "附件列表",
            busType = "智能审计",
            fail = "附件列表",
            operationType = OperationType.SELECT,
            subType = "审计实施——获取审计取证单附件列表相关内容"
    )
    @GetMapping("/qzd/certificate_file_list")
    @Operation(summary = "审计取证单-附件列表")
    public JsonBean certificate_file_list(HttpServletRequest request,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "certificateId", description = "业务主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblAttachmentService.certificateFileList(token, certificateId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    /**
     * 审计取证单-附件删除
     */
    @OperationLog(
            success = "附件删除",
            busType = "智能审计",
            fail = "附件删除",
            operationType = OperationType.DELETE,
            subType = "审计实施——审计取证单附件列表删除附件"
    )
    @GetMapping("/qzd/certificate_file_del")
    @Operation(summary = "审计取证单-附件删除")
    public R certificate_file_del(HttpServletRequest request, HttpServletResponse response,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "attId", description = "附件主键ID", required = true) @RequestParam("attId") String attId) throws Exception {
        return this.tblNbsjCertificateService.removeAttInfoByAttId(token, attId);
    }

    /**
     * 审计取证单-删除
     */
    @OperationLog(
            success = "审计取证单删除",
            busType = "智能审计",
            fail = "审计取证单删除",
            operationType = OperationType.DELETE,
            subType = "审计实施——审计取证单删除"
    )
    @GetMapping("/qzd/certificate_del")
    @Operation(summary = "审计取证单-删除")
    public JsonBean certificate_del(HttpServletRequest request,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "certificateId", description = "主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId) {

        try {
            return tblNbsjCertificateService.certificateDelete(certificateId, token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 审计取证单-明细
     */
    @OperationLog(
            success = "审计取证单详情",
            busType = "智能审计",
            fail = "审计取证单详情",
            operationType = OperationType.SELECT,
            subType = "查看审计实施——审计取证单指定记录详细信息【{{certificateId}}】"
    )
    @GetMapping("/qzd/certificate_detail")
    @Operation(summary = "审计取证单-明细")
    public JsonBean certificate_detail(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "certificateId", description = "主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjCertificateService.certificateDetail(token, certificateId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    
    /**
     * 审计取证单-盖章附件保存
     */
    @OperationLog(
            success = "盖章附件保存",
            busType = "智能审计",
            fail = "盖章附件保存",
            operationType = OperationType.ADD,
            subType = "审计实施——保存取证单的盖章附件"
    )
    @PostMapping("/qzd/saveStampedDocument")
    @Operation(summary = "审计取证单-盖章附件保存")
    public JsonBean saveStampedDocument(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "certificateId", description = "审计取证单主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId,
            @Parameter(name = "attids", description = "盖章附件id数组", required = true)@RequestParam(value = "attids", required = true) String[] attids) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjCertificateService.saveStampedDocument(certificateId, attids);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    /**
     * 审计取证单-盖章附件删除
     */
    @OperationLog(
            success = "盖章附件删除",
            busType = "智能审计",
            fail = "盖章附件删除",
            operationType = OperationType.DELETE,
            subType = "审计实施——删除取证单的盖章附件"
    )
    @PostMapping("/qzd/removeStampedDocument")
    @Operation(summary = "审计取证单-盖章附件删除")
    public JsonBean removeStampedDocument(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "certificateId", description = "审计取证单主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId,
            @Parameter(name = "attid", description = "盖章附件id数组", required = true)@RequestParam(value = "attid", required = true) String attid) {
        JsonBean jsonBean = null;
        try {
            jsonBean = tblNbsjCertificateService.removeStampedDocument(certificateId, attid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }
    
    /**
    * 审计取证单-获取盖章附件列表
    */
    @OperationLog(
            success = "获取盖章附件列表",
            busType = "智能审计",
            fail = "获取盖章附件列表",
            operationType = OperationType.SELECT,
            subType = "获取审计实施—审计取证单的盖章附件列表"
    )
   @GetMapping("/qzd/getStampedDocumentList")
   @Operation(summary = "审计取证单-获取盖章附件列表")
   public JsonBean getStampedDocumentList(HttpServletRequest request,
   		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
           @Parameter(name = "certificateId", description = "审计取证单主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId) {
       JsonBean jsonBean = null;
       try {
           jsonBean = tblNbsjCertificateService.getStampedDocumentList(certificateId);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return jsonBean;
   }


    @OperationLog(
            success = "取证单导出",
            busType = "智能审计",
            fail = "取证单导出",
            operationType = OperationType.EXPORT,
            subType = "审计实施—审计取证单进行导出操作"
    )
   @RequestMapping(value = "/qzd/certificate_export", method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
    @Operation(summary = "审计取证单-导出")
    public void certificate_export(HttpServletRequest request, HttpServletResponse response,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "certificateId", description = "主键", required = true) @RequestParam(value = "certificateId", required = true) BigDecimal certificateId) {
        try {
            TblNbsjCertificate cate = this.tblNbsjCertificateMapper.selectById(certificateId);
            Document doc = new Document();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            doc = new Document();
            String reportName = "";
            ClassPathResource resource = new ClassPathResource("/template/sjqzd.docx");
            //String path = FtpUtil.sjqzdUrl;
            // doc.loadFromFile(path+"sjqzd.docx");
            doc.loadFromStream(resource.getStream(), FileFormat.Docx);
            BookmarksNavigator bookmarkNavigator = new BookmarksNavigator(doc);
            if (cate.getProjectId() != null) {
                TblNbsjProject pro = tblNbsjProjectMapper.getById(cate.getProjectId().toString());
                bookmarkNavigator.moveToBookmark("xmmc");
                reportName = pro.getPrjoectName();
                bookmarkNavigator.replaceBookmarkContent(pro.getPrjoectName(), false);
            }
            bookmarkNavigator.moveToBookmark("bsjr");
            bookmarkNavigator.replaceBookmarkContent((cate.getAuditStaffName() != null ? cate.getAuditStaffName() : "")
                    + (cate.getOrgIdNames() != null ? cate.getOrgIdNames() : ""), false);
            bookmarkNavigator.moveToBookmark("sjsx");
            bookmarkNavigator.replaceBookmarkContent(cate.getAuditMatter(), false);
            bookmarkNavigator.moveToBookmark("zy");
            bookmarkNavigator.replaceBookmarkContent(cate.getAuditAbstract(), false);
            if (cate.getAuditUserId() != null) {
                bookmarkNavigator.moveToBookmark("sjry");
                TblStaff plan = this.tblStaffMapper.selectById(cate.getAuditUserId());
                bookmarkNavigator.replaceBookmarkContent(plan.getRealname(), false);
            }
            if (cate.getCertificateUser() != null) {
                bookmarkNavigator.moveToBookmark("zjtgz");
                bookmarkNavigator.replaceBookmarkContent(cate.getCertificateUser(), false);
            }
            String time = "";
            if (cate.getCreateDate() != null) {
                bookmarkNavigator.moveToBookmark("bzrq");
                time = new SimpleDateFormat("yyyy-MM-dd").format(cate.getCreateDate());
                bookmarkNavigator.replaceBookmarkContent(time, false);
            }
            bookmarkNavigator.moveToBookmark("yj");
            bookmarkNavigator.replaceBookmarkContent(cate.getEvidenceOpinion(), false);
            String time1 = "";
            if (cate.getCertificateDate() != null) {
                bookmarkNavigator.moveToBookmark("rq");
                time1 = new SimpleDateFormat("yyyy-MM-dd").format(cate.getCertificateDate());
                bookmarkNavigator.replaceBookmarkContent(time1, false);
            }
            String pathNew = System.getProperty("user.dir") + "/" + reportName.trim() + ".doc";
            doc.saveToFile(pathNew, FileFormat.Doc);//FileFormat.Word_Xml
            System.out.println(pathNew);
            FileUtil.downLoad(pathNew, response, false,  "审计取证单.doc");
            // deleteAllFilesOfDir(new File(pathNew));
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
    }

    public static void deleteAllFilesOfDir(File path) {
        if (!path.exists())
            return;
        if (path.isFile()) {
            path.delete();
            return;
        }
        File[] files = path.listFiles();
        for (int i = 0; i < files.length; i++) {
            deleteAllFilesOfDir(files[i]);
        }
        path.delete();
    }


    /**
     * 统计类型维护
     */
    @OperationLog(
            success = "统计类型维护列表",
            busType = "智能审计",
            fail = "统计类型维护列表",
            operationType = OperationType.SELECT,
            subType = "基础配置—统计类型维护列表"
    )
    @RequestMapping(value = "/tjlx/getNbsjStatTypeListPage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "统计类型维护-列表")
    public JsonBean getNbsjStatTypeListPage(HttpServletRequest request,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                            @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.getNbsjStatTypeListPage(token, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonBean;
    }

    @OperationLog(
            success = "统计类型详情页",
            busType = "智能审计",
            fail = "统计类型详情页",
            operationType = OperationType.SELECT,
            subType = "基础配置—统计类型详情"
    )
    @RequestMapping(value = "/tjlx/stattype_info", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "统计类型维护-详情页")
    public JsonBean stattype_info(@RequestParam(value = "typeId", required = true) @Parameter(name = "typeId", description = "统计类型id", required = true) String typeId,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.getNbsjStatType(typeId, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @OperationLog(
            success = "统计类型维护",
            busType = "智能审计",
            fail = "统计类型维护",
            operationType = OperationType.ADD,
            subType = "基础配置—统计类型新增修改操作"
    )
    @RequestMapping(value = "/tjlx/stattype_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "统计类型维护-新增及修改")
    public JsonBean stattype_save(HttpServletRequest request, @Parameter(name = "sqt", description = "统计类型实体", required = true) TblNbsjStatType sqt,
                                  @Parameter(name = "token", description = "登录用户token", required = false) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.saveNbsjStatType(sqt, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @OperationLog(
            success = "统计类型删除",
            busType = "智能审计",
            fail = "统计类型删除",
            operationType = OperationType.DELETE,
            subType = "基础配置—统计类型删除操作"
    )
    @RequestMapping(value = "/tjlx/stattype_del", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "统计类型维护-删除")
    public JsonBean stattype_del(HttpServletRequest request,
                                 @RequestParam(value = "typeId", required = true) @Parameter(name = "typeId", description = "统计类型id", required = true) String typeId,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = this.tblNbsjQuestionTypeService.delNbsjStatType(typeId, token);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-获取左侧tree
     */
    @OperationLog(
            success = "左侧树",
            busType = "智能审计",
            fail = "左侧树",
            operationType = OperationType.SELECT,
            subType = "审计模型—审计模型库左侧列表树"
    )
    @GetMapping("/sjmx/getTree")
    @Operation(summary = "审计模型-获取左侧tree")
    public JsonBean getTree(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "mpdeltype", description = "区分数据来源，风险或审计", required = false) @RequestParam(value = "mpdeltype", required = false) String mpdeltype,
                            @Parameter(name = "nodeId", description = "查询子级需要传typeid", required = false) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = typeService.getRoot(token, nodeId,mpdeltype);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    /**
     * 审计模型-保存或修改左侧tree
     */
    @OperationLog(
            success = "新增/修改",
            busType = "智能审计",
            fail = "新增/修改",
            operationType = OperationType.ADD,
            subType = "审计模型—审计模型库列表进行新增/修改操作"
    )
    @RequestMapping(value = "/sjmx/typeSaveOrupdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-保存或修改左侧tree")
    public JsonBean typeSaveOrupdate(HttpServletRequest request, TblNbsjAuditExperienceTypeEntity type,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = typeService.saveOrupdate(type, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-详情
     */
    @OperationLog(
            success = "审计模型详情",
            busType = "智能审计",
            fail = "审计模型详情",
            operationType = OperationType.SELECT,
            subType = "审计模型—查看审计模型库列表记录的详情内容"
    )
    @GetMapping("/sjmx/gettypeDetail")
    @Operation(summary = "审计模型-详情")
    public JsonBean getDetail(HttpServletRequest request,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "nodeId", description = "查询typeid", required = true) @RequestParam(value = "nodeId", required = true) BigDecimal nodeId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = typeService.findbyid(token, nodeId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-删除左侧tree
     */
    @OperationLog(
            success = "左侧树删除",
            busType = "智能审计",
            fail = "左侧树删除",
            operationType = OperationType.DELETE,
            subType = "审计模型—审计模型库左侧列表树内容进行删除操作"
    )
    @RequestMapping(value = "/sjmx/deletetype", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-删除左侧tree")
    public JsonBean deletetype(HttpServletRequest request,
                               @RequestParam(value = "nodeId", required = true) @Parameter(name="nodeId",description="nodeId",required=true) BigDecimal nodeId,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = typeService.delete(token, nodeId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

 
    /**
     * 审计模型-审计步骤列表
     */
    @OperationLog(
            success = "模型库列表",
            busType = "智能审计",
            fail = "模型库列表",
            operationType = OperationType.SELECT,
            subType = "审计模型—审计模型库列表页内容进行查看"
    )
    @GetMapping("/sjmx/getSteplist")
    @Operation(summary = "审计模型-审计步骤列表")
    public JsonBean getSteplist(HttpServletRequest request,TblNbsjAuditStepEntity setp,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                @Parameter(name = "typeId", description = "左侧的typeid", required = false) @RequestParam(value = "typeId", required = false) BigDecimal typeId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.findByExper(token, typeId, setp, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @OperationLog(
            success = "获取编号",
            busType = "智能审计",
            fail = "获取编号",
            operationType = OperationType.SELECT,
            subType = "审计模型—审计模型库列表页新增时获取编号"
    )
    @Operation(summary = "审计模型-获取编号")
    @GetMapping("/sjmx/gaincode")
    public JsonBean gaincode(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.findBycode(token);
        } catch (Exception e) {
            log.error("审计模型-获取编号 ...接口 异常", e);
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-审计步骤新增或修改
     */
    @OperationLog(
            success = "模型新增或修改",
            busType = "智能审计",
            fail = "模型新增或修改",
            operationType = OperationType.ADD,
            subType = "审计模型—审计模型库列表页进行模型新增/修改操作"
    )
    @RequestMapping(value = "/sjmx/stepsaveOrUpdate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-审计步骤新增或修改")
    public JsonBean stepsaveOrUpdate(HttpServletRequest request, TblNbsjAuditStepEntity step,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try { 
            jsonBean = stepService.saveOrupdate(token, step);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }

    
    
    /**
     * 审计模型-启用或禁用
     */
    @OperationLog(
            success = "模型状态修改",
            busType = "智能审计",
            fail = "模型状态修改",
            operationType = OperationType.UPDATE,
            subType = "审计模型—审计模型库列表记录状态进行修改"
    )
    @RequestMapping(value = "/sjmx/xgStatus", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-启用或禁用")
    public JsonBean xgStatus(HttpServletRequest request, 
    		@Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId,
    		@Parameter(name = "xgstatus", description = "修改的状态", required = true) @RequestParam(value = "xgstatus", required = true) Integer xgstatus,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try { 
            jsonBean = stepService.xgStatus(token, stepId, xgstatus);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    
    /**
        * 审计模型-下发人员
     */
    @OperationLog(
            success = "下发人员",
            busType = "智能审计",
            fail = "下发人员",
            operationType = OperationType.DISPATCH,
            subType = "审计模型—审计模型库模型进行下发操作"
    )
    @RequestMapping(value = "/sjmx/xgry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-下发人员")
    public JsonBean xgry(HttpServletRequest request, 
    		@Parameter(name = "stepIds", description = "主键stepId", required = true) @RequestParam(value = "stepIds", required = true) String stepIds,
    		@Parameter(name = "staffids", description = "下发人员ids", required = true) @RequestParam(value = "staffids", required = true) String staffids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try { 
            jsonBean = stepService.saveXfry(token, stepIds, staffids); 
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    
    /**
     * 审计模型-获取下发人员，以便取消下发
     */
    @OperationLog(
            success = "查看下发人员",
            busType = "智能审计",
            fail = "查看下发人员",
            operationType = OperationType.SELECT,
            subType = "审计模型—审计模型库查询模型所属下发人员"
    )
    @GetMapping("/sjmx/getxfStafflist")
    @Operation(summary = "审计模型-查询模型所属下发人员")
    public JsonBean getxfStafflist(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "realname", description = "查询用户名称", required = false) @RequestParam(value = "realname", required = false) String realname,
                                @Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.findByxfUser(token, stepId,realname);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }
    
    
    
    /**
     * 审计模型-删除下发人员
  */
    @OperationLog(
            success = "删除下发人员",
            busType = "智能审计",
            fail = "删除下发人员",
            operationType = OperationType.DELETE,
            subType = "审计模型—审计模型库删除模型所属下发人员"
    )
 @RequestMapping(value = "/sjmx/deletexgry", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
 @Operation(summary = "审计模型-删除下发人员")
 public JsonBean deletexgry(HttpServletRequest request, 
 		@Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId,
 		@Parameter(name = "staffids", description = "下发人员ids", required = true) @RequestParam(value = "staffids", required = true) String staffids,
         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
     JsonBean jsonBean = null;
     try { 
         jsonBean = stepService.deleteXfry(token, stepId, staffids); 
     } catch (Exception e) {
         e.printStackTrace();
         return ResponseFormat.retParam(1, 1000, e.getMessage());
     }
     return jsonBean;
 }
    
    /**
     * 审计模型-根据下发人员查询
     */
    @OperationLog(
            success = "查询模型",
            busType = "智能审计",
            fail = "查询模型",
            operationType = OperationType.SELECT,
            subType = "审计模型——根据下发人员查询模型列表"
    )
    @GetMapping("/sjmx/getXflist")
    @Operation(summary = "审计模型-根据下发人员查询模型")
    public JsonBean getXflist(HttpServletRequest request,TblNbsjAuditStepEntity setp,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                                @Parameter(name = "stepId", description = "主键stepId", required = false) @RequestParam(value = "stepId", required = false) BigDecimal stepId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.findByxfStep(token, setp, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-审计步骤详情
     */
    @OperationLog(
            success = "查询详情",
            busType = "智能审计",
            fail = "查询详情",
            operationType = OperationType.SELECT,
            subType = "审计模型——审计模型库查看记录内容详情"
    )
    @GetMapping("/sjmx/getStepDetail")
    @Operation(summary = "审计模型-审计步骤详情")
    public JsonBean getStepDetail(HttpServletRequest request,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.getone(token, stepId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-审计步骤删除
     */
    @OperationLog(
            success = "审计模型删除",
            busType = "智能审计",
            fail = "审计模型删除",
            operationType = OperationType.DELETE,
            subType = "审计模型——审计模型库中的记录进行删除操作"
    )
    @RequestMapping(value = "/sjmx/deletestep", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型-审计步骤删除")
    public JsonBean deletestep(HttpServletRequest request,
                               @Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.deleteByExperId(token, stepId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型验证SQL
     */
    @OperationLog(
            success = "SQL验证",
            busType = "智能审计",
            fail = "SQL验证",
            operationType = OperationType.PREVIEW,
            subType = "审计模型——验证审计模型SQL是否异常操作"
    )
    @RequestMapping(value = "/sjmx/sqlyz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型验证SQL")
    public JsonBean sjmxgetList(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "sql", description = "SQL", required = false) @RequestParam(value = "sql", required = false) String sql,
                                @Parameter(name = "bookid", description = "bookid", required = false) @RequestParam(value = "bookid", required = false) String bookid,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {
        JsonBean jsonBean = null;
        try {
            if (bookid != null && bookid.trim().length() > 0) {
                //TblAccBook accBook = accbookservice.findByBookIdOne(bookid);
                PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
                pageInfo.setPageSize(pageSize);
                pageInfo.setCurrentPage(pageNumber);
                //jsonBean = JDBCProperties.GetGather(accBook.getAcctid(), sql, pageInfo);

                TblAuditModelDataSourceOracle data = tblAuditModelDataSourceOracleService.findById(new BigDecimal(bookid));
                if (data == null) {
                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);
                }
                if (data != null && data.getDataBaseType().equals("Oracle")) {
                    jsonBean = JDBCProperties.GetGatheroracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);
                }
                if (data != null && data.getDataBaseType().equals("Mysql")) {
                    jsonBean = JDBCProperties.GetGathermysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);
                }
                if (data != null && data.getDataBaseType().equals("SqlServer")) {
                    jsonBean = JDBCProperties.GetGatherSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);
                }
                if(data != null && data.getDataBaseType().equals("DM8")) {
                	jsonBean = JDBCProperties.GetGatherDMEightServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, pageInfo);
                }


            } else {
                jsonBean = stepService.getList(token, pageNumber, pageSize, sql);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, "SQL有误，请修改", null);
        }
        return jsonBean;
    }

    /**
     * 审计模型sql结果导出
     */
    @OperationLog(
            success = "SQL结果导出",
            busType = "智能审计",
            fail = "SQL结果导出",
            operationType = OperationType.EXPORT,
            subType = "审计模型——对应记录的SQL结果进行导出"
    )
    @RequestMapping(value = "/sjmx/sqljgexport", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "审计模型sql结果导出")
    public void sqljgexport(HttpServletRequest request, HttpServletResponse response,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "sql", description = "SQL", required = false) @RequestParam(value = "sql", required = false) String sql,
                            @Parameter(name = "bookid", description = "bookid", required = false) @RequestParam(value = "bookid", required = false) BigDecimal bookid) {
        try {
            List<Object[]> contractlist = null;
            List<String> cNames = null;
            if (bookid != null && bookid.intValue()> 0) {

                TblAuditModelDataSourceOracle data = tblAuditModelDataSourceOracleService.findById(bookid);

                if (data != null && data.getDataBaseType().equals("Oracle")) {
                    cNames = JDBCProperties.getTbableall(sql, null, data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                    contractlist = JDBCProperties.GetGatheroracleall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
                }
                if (data != null && data.getDataBaseType().equals("Mysql")) {
                    cNames = JDBCProperties.getTbableall(sql, "1", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                    contractlist = JDBCProperties.GetGathermysqlall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
                }
                if (data != null && data.getDataBaseType().equals("SqlServer")) {
                    cNames = JDBCProperties.getTbableall(sql, "2", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                    contractlist = JDBCProperties.GetGatherSqlserverall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
                }
                if(data != null && data.getDataBaseType().equals("DM8")) {
                	cNames = JDBCProperties.getTbableall(sql, "3", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                	contractlist = JDBCProperties.GetGatherDMEightServerAll(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql, cNames.size());
                }

            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("审计模型SQL查询结果".getBytes(), "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcelsj(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OperationLog(
            success = "SQL导出",
            busType = "智能审计",
            fail = "SQL导出",
            operationType = OperationType.EXPORT,
            subType = "审计模型——根据sql导出excel文件"
    )
    @PostMapping("/sjmx/exportsql")
    @Operation(summary = "根据sql导出excel文件")
    public void exportSql(HttpServletResponse response, @RequestHeader("token") String token, @RequestBody @Validated ExportSqlParam param) {
        try {
            List<Object[]> contractlist = null;
            List<String> cNames = null;
            StringBuffer str = new StringBuffer();
            str.append("select ");
            String sql = StringUtils.join(param.getSql(), ",");
            str.append(sql).append(" from ").append(param.getTableName()).append(" where 1=1 ");
            log.info("执行sql输出结果：{}", str.toString());
            TblAuditModelDataSourceOracle data = tblAuditModelDataSourceOracleService.findById(param.getDataBaseId());
            if (data != null && data.getDataBaseType().equals("Oracle")) {
                cNames = JDBCProperties
                        .getTbableall(str.toString(), null, data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                contractlist = JDBCProperties
                        .GetGatheroracleall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), str.toString(),
                                cNames.size());
            }
            if (data != null && data.getDataBaseType().equals("Mysql")) {
                cNames = JDBCProperties
                        .getTbableall(str.toString(), "1", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                contractlist = JDBCProperties
                        .GetGathermysqlall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), str.toString(),
                                cNames.size());
            }
            if (data != null && data.getDataBaseType().equals("SqlServer")) {
                cNames = JDBCProperties
                        .getTbableall(str.toString(), "2", data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord());
                contractlist = JDBCProperties
                        .GetGatherSqlserverall(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(),
                                str.toString(), cNames.size());
            }
            response.setHeader("Content-Disposition", "attachment;filename=" + new String("SQL查询结果".getBytes(), "UTF-8") + ".xlsx");
            ServletOutputStream outputStream = response.getOutputStream();
            ImportOrExportExcelUtil.exportExcelsj(cNames, contractlist, outputStream, null);
        } catch (Exception e) {
            log.error("根据sql导出excel文件：", e);
        }
    }

    @OperationLog(
            success = "链接测试",
            busType = "智能审计",
            fail = "链接测试",
            operationType = OperationType.SELECT,
            subType = "审计模型——审计模型库SQL数据源链接测试"
    )
    @PostMapping("/sjmx/check/connection")
    @Operation(summary = "数据源连接测试")
    public JsonBean checkConnection(@RequestBody @Validated CheckConnectionParam param) {
        boolean flag = false;
        String dataBaseConnectionAddress = param.getDataBaseConnectionAddress();
        String dataBaseUsers = param.getDataBaseUsers();
        String dataBasePassWord = param.getDataBasePassWord();
        if (Objects.equals(param.getDataBaseType(), "Oracle")) {
            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
        }
        if (Objects.equals(param.getDataBaseType(), "Mysql")) {
            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
        }
        if (Objects.equals(param.getDataBaseType(), "SqlServer")) {
            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
        }
        if (Objects.equals(param.getDataBaseType(), "DM8")) {
            flag = JDBCProperties.checkConnection(dataBaseConnectionAddress, dataBaseUsers, dataBasePassWord);
        }
        if (flag) {
            return ResponseFormat.retParam(1, "连接测试成功", null);
        } else {
            return ResponseFormat.retParam(0, "连接测试异常", null);
        }
    }

    @OperationLog(
            success = "数据库列表",
            busType = "智能审计",
            fail = "数据库列表",
            operationType = OperationType.SELECT,
            subType = "审计模型——获取数据源列表"
    )
    @Operation(summary = "审计模型-数据库列表")
    @PostMapping("/sjmx/database/table/get-list")
    public JsonBean getDatabaseTableList(@RequestHeader("token") String token, @RequestBody @Validated DatabaseTableParam param) {
        JsonBean jsonBean = null;
        TblStaffUtil loginStaff = null;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            return ResponseFormat.retParam(0, "token工具类解析异常", null);
        }
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            if (param.getBookid() != null) {
                TblAuditModelDataSourceOracle data = tblAuditModelDataSourceOracleService.findById(new BigDecimal(param.getBookid()));
                if (data == null) {
                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);
                }
                if (data != null && (data.getDataBaseType().equals("Oracle") || data.getDataBaseType().equals("DM8"))) {
                    String sql = "select TABLE_NAME from all_tables where owner='" + data.getDataBaseUsers() + "'";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListoracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }
                if (data != null && data.getDataBaseType().equals("Mysql")) {
                    String cname = data.getDataBaseConnectionAddress().substring((data.getDataBaseConnectionAddress().lastIndexOf("/") + 1), data.getDataBaseConnectionAddress().length());
                    System.out.println(cname);
                    String sql = "select table_name from information_schema.tables where table_schema='" + cname + "'";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }
                if (data != null && data.getDataBaseType().equals("SqlServer")) {
                    String sql = "select name as TABLE_NAME from sysobjects where xtype='u' order by name";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }


            }


//			if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//				TblAccBook accBook = accbookservice.findByBookIdOne(param.getBookid());
//				if (accBook == null) {
//					return ResponseFormat.retParam(0, "bookid对应的数据不存在", null);
//				}
//				String sql = "select TABLE_NAME from all_tables where owner='" + accBook.getAcctid() + "'";
//				jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
//			} else {
//
//			}
        } catch (Exception e) {
            log.error("审计模型-数据库列表 异常：", e);
            return ResponseFormat.retParam(0, "审计模型-数据库列表 异常", null);
        }
        return jsonBean;
    }

    @OperationLog(
            success = "表字段列表",
            busType = "智能审计",
            fail = "表字段列表",
            operationType = OperationType.SELECT,
            subType = "审计模型-数据库-表字段列表"
    )
    @Operation(summary = "审计模型-数据库-表字段列表")
    @PostMapping("/sjmx/database/table/field/get-list")
    public JsonBean getDatabaseTableFieldList(@RequestHeader("token") String token, @RequestBody @Validated DatabaseTableFieldParam param) {
        JsonBean jsonBean = null;
        TblStaffUtil loginStaff = null;
        try {
            loginStaff = userProvider.get();
        } catch (Exception e) {
            return ResponseFormat.retParam(0, "token工具类解析异常", null);
        }
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            if (param.getBookid() != null) {
                TblAuditModelDataSourceOracle data = tblAuditModelDataSourceOracleService.findById(new BigDecimal(param.getBookid()));
                if (data == null) {
                    return ResponseFormat.retParam(0, "选择的数据源不存在", null);
                }
                if (data != null && data.getDataBaseType().equals("Oracle") || data.getDataBaseType().equals("DM8")) {
                    String sql = "select COLUMN_NAME from user_tab_columns where table_name = '" + param.getTable() + "'";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListoracle(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }
                if (data != null && data.getDataBaseType().equals("Mysql")) {
                    String cname = data.getDataBaseConnectionAddress().substring((data.getDataBaseConnectionAddress().lastIndexOf("/") + 1), data.getDataBaseConnectionAddress().length());
                    String sql = "select COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME='" + param.getTable() + "'";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListmysql(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }
                if (data != null && data.getDataBaseType().equals("SQLServer")) {
                    String sql = "select COLUMN_NAME from information_schema.COLUMNS where TABLE_NAME='" + param.getTable() + "'";
                    //jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
                    jsonBean = JDBCProperties.GetGatherListSqlServer(data.getDataBaseConnectionAddress(), data.getDataBaseUsers(), data.getDataBasePassWord(), sql);
                }


            }


//			if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
//				TblAccBook accBook = accbookservice.findByBookIdOne(param.getBookid());
//				if (accBook == null) {
//					return ResponseFormat.retParam(0, "bookid对应的数据不存在", null);
//				}
//				String sql = "select * from user_tab_columns where table_name = '" + param.getTable() + "'";
//				jsonBean = JDBCProperties.GetGatherList(accBook.getAcctid(), sql);
//			} else {

//			}
        } catch (Exception e) {
            log.error("审计模型-数据库列表 异常：", e);
            return ResponseFormat.retParam(0, "审计模型-数据库列表 异常", null);
        }
        return jsonBean;
    }


    /**
     * 审计模型-sql执行
     */
    @OperationLog(
            success = "SQL执行",
            busType = "智能审计",
            fail = "SQL执行",
            operationType = OperationType.SELECT,
            subType = "审计模型-审计模型-SQL执行"
    )
    @GetMapping("/sjmx/zxsql")
    @Operation(summary = "审计模型-SQL执行")
    public JsonBean zxsql(HttpServletRequest request,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "stepId", description = "主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.zxsql(token, stepId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-sql执行过程查询
     */
    @OperationLog(
            success = "sql执行过程查询",
            busType = "智能审计",
            fail = "sql执行过程查询",
            operationType = OperationType.SELECT,
            subType = "审计模型-sql执行过程查询"
    )
    @GetMapping("/sjmx/zxReslut")
    @Operation(summary = "审计模型-查看sql执行过程")
    public JsonBean zxReslut(HttpServletRequest request,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "stepId", description = "模型主键stepId", required = true) @RequestParam(value = "stepId", required = true) BigDecimal stepId) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.getxjjgList(token, stepId);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /** 
     * 审计模型-sql执行结果查询
     */
    @OperationLog(
            success = "sql执行结果查询",
            busType = "智能审计",
            fail = "sql执行结果查询",
            operationType = OperationType.SELECT,
            subType = "审计模型-对sql执行结果进行查询操作"
    )
    @GetMapping("/sjmx/getDatelist")
    @Operation(summary = "审计模型-sql执行结果查询")
    public JsonBean getDatelist(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "resultid", description = "过程记录主键resultid", required = true) @RequestParam(value = "resultid", required = true) BigDecimal resultid,
                                @Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

        JsonBean jsonBean = null;
        try {
            jsonBean = stepService.getDatelistt(token, resultid, pageNumber, pageSize);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return jsonBean;
    }


    /**
     * 审计模型-sql执行结果查询
     */
    @OperationLog(
            success = "问题分类",
            busType = "智能审计",
            fail = "问题分类",
            operationType = OperationType.SELECT,
            subType = "审计模型——审计署底稿问题分类"
    )
    @GetMapping("/sjgk/sjs_dgwt_list")
    @Operation(summary = "审计署底稿问题分类")
    public JsonBean getDatelist(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "startdate1", description = "起始时间", required = false) @RequestParam(value = "startdate1", required = false) String startdate1,
                                @Parameter(name = "enddate1", description = "结束时间", required = false) @RequestParam(value = "enddate1", required = false) String enddate1,
                                @Parameter(name = "projectid", description = "项目名称查询条件", required = false) @RequestParam(value = "projectid", required = false) String projectid) throws Exception {
        JsonBean jsonBean = null;
        TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        try {
            Date enddate = null;
            if (enddate1 == null) {
                enddate = new Date();
            } else {
                enddate = DateUtils.parse(enddate1, "yyyy-MM-dd");
            }
            Date startdate = null;
            if (startdate1 == null) {
                Calendar cal = Calendar.getInstance();
                startdate = DateUtils.parse(cal.get(Calendar.YEAR) + "-01-01", "yyyy-MM-dd");
            } else {
                startdate = DateUtils.parse(startdate1, "yyyy-MM-dd");
            }
            jsonBean = tblnbsjProjectService.selectSjsQuestList(startdate, enddate, projectid, token);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseFormat.retParam(0, 1000, e.getMessage());
        }
        return jsonBean;
    }


    @OperationLog(
            success = "问题导出",
            busType = "智能审计",
            fail = "问题导出",
            operationType = OperationType.EXPORT,
            subType = "审计模型——审计署底稿问题进行导出操作"
    )
    @Operation(summary = "审计署底稿问题-导出)")
    @GetMapping(value = "/exploredSjsDgwt", produces = "application/json; charset=utf-8")
    public JsonBean exportYswh(HttpServletRequest request, HttpServletResponse response,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "startdate1", description = "起始时间", required = false) @RequestParam(value = "startdate1", required = false) String startdate1,
                               @Parameter(name = "enddate1", description = "结束时间", required = false) @RequestParam(value = "enddate1", required = false) String enddate1,
                               @Parameter(name = "projectid", description = "项目名称查询条件", required = false) @RequestParam(value = "projectid", required = false) String projectid) throws Exception {
        TblStaffUtil userToken = userProvider.get();
        if (userToken == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        BigDecimal orgid = userToken.getCurrentOrg().getOrgid();
        ClassPathResource resource = new ClassPathResource("/template/sjs.xlsx");
        String modelName = "sjs.xlsx";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Date enddate = null;
        if (enddate1 == null) {
            enddate = new Date();
        } else {
            enddate = DateUtils.parse(enddate1, "yyyy-MM-dd");
        }
        Date startdate = null;
        if (startdate1 == null) {
            Calendar cal = Calendar.getInstance();
            startdate = DateUtils.parse(cal.get(Calendar.YEAR) + "-01-01", "yyyy-MM-dd");
        } else {
            startdate = DateUtils.parse(startdate1, "yyyy-MM-dd");
        }
        JsonBean jsonBean = tblnbsjProjectService.selectSjsQuestList(startdate, enddate, projectid, token);
        Map<String, Object> resultMap = (Map<String, Object>) jsonBean.getData();
        TblSjsQuestion sjs = (TblSjsQuestion) resultMap.get("data");
        // FileInputStream ins=new FileInputStream(new File(resource+"/"+modelName));
        XSSFWorkbook workbook = new XSSFWorkbook(resource.getStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFRow row0 = sheet.getRow(4);
        row0.getCell(4).setCellValue(sdf.format(new Date()));
        XSSFRow row1 = sheet.getRow(8);
        row1.getCell(4).setCellValue(sjs.getSjxm());
        XSSFRow row2 = sheet.getRow(9);
        row2.getCell(4).setCellValue(sjs.getZdzccssj());
        XSSFRow row3 = sheet.getRow(10);
        row3.getCell(4).setCellValue(sjs.getCwszsj());
        XSSFRow row4 = sheet.getRow(11);
        row4.getCell(4).setCellValue(sjs.getGdzc());
        XSSFRow row5 = sheet.getRow(12);
        row5.getCell(4).setCellValue(sjs.getFxgl());
        XSSFRow row6 = sheet.getRow(13);
        row6.getCell(4).setCellValue(sjs.getZrsj());
        XSSFRow row7 = sheet.getRow(14);
        row7.getCell(4).setCellValue(sjs.getXtsj());
        XSSFRow row8 = sheet.getRow(15);
        row8.getCell(4).setCellValue(sjs.getJwsj());
        XSSFRow row9 = sheet.getRow(16);
        row9.getCell(4).setCellValue(sjs.getQtsj());
        XSSFRow row10 = sheet.getRow(17);
        row10.getCell(4).setCellValue(sjs.getWbsj());
        XSSFRow row42 = sheet.getRow(18);
        row42.getCell(4).setCellValue(sjs.getSjgzl());
        XSSFRow row12 = sheet.getRow(19);
        row12.getCell(4).setCellValue(sjs.getWtje());
        XSSFRow row13 = sheet.getRow(20);
        row13.getCell(4).setCellValue(sjs.getJxlwtje());
        XSSFRow row14 = sheet.getRow(21);
        row14.getCell(4).setCellValue(sjs.getHgxwtje());
        XSSFRow row15 = sheet.getRow(22);
        row15.getCell(4).setCellValue(sjs.getKjhs());
        XSSFRow row16 = sheet.getRow(23);
        row16.getCell(4).setCellValue(sjs.getWgsy());
        XSSFRow row17 = sheet.getRow(24);
        row17.getCell(4).setCellValue(sjs.getJlcd());
        XSSFRow row18 = sheet.getRow(25);
        row18.getCell(4).setCellValue(sjs.getSslf());
        XSSFRow row19 = sheet.getRow(26);
        row19.getCell(4).setCellValue(sjs.getNyzj());
        XSSFRow row20 = sheet.getRow(27);
        row20.getCell(4).setCellValue(sjs.getTsls());
        XSSFRow row21 = sheet.getRow(28);
        row21.getCell(4).setCellValue(sjs.getWgqd());
        XSSFRow row22 = sheet.getRow(29);
        row22.getCell(4).setCellValue(sjs.getWtqtje());
        XSSFRow row23 = sheet.getRow(30);
        row23.getCell(4).setCellValue(sjs.getWtzs());
        XSSFRow row24 = sheet.getRow(31);
        row24.getCell(4).setCellValue(sjs.getJelzs());
        XSSFRow row25 = sheet.getRow(32);
        row25.getCell(4).setCellValue(sjs.getFjelzs());
        XSSFRow row26 = sheet.getRow(33);
        row26.getCell(4).setCellValue(sjs.getGjzc());
        XSSFRow row27 = sheet.getRow(34);
        row27.getCell(4).setCellValue(sjs.getFzgh());
        XSSFRow row28 = sheet.getRow(35);
        row28.getCell(4).setCellValue(sjs.getNbkz());
        XSSFRow row29 = sheet.getRow(36);
        row29.getCell(4).setCellValue(sjs.getWtqt());
        XSSFRow row30 = sheet.getRow(37);
        row30.getCell(4).setCellValue(sjs.getWtzgjel());
        XSSFRow row31 = sheet.getRow(38);
        row31.getCell(4).setCellValue(sjs.getTzzm());
        XSSFRow row32 = sheet.getRow(39);
        row32.getCell(4).setCellValue(sjs.getShzj());
        XSSFRow row33 = sheet.getRow(40);
        row33.getCell(4).setCellValue(sjs.getWhss());
        XSSFRow row34 = sheet.getRow(41);
        row34.getCell(4).setCellValue(sjs.getGhqd());
        XSSFRow row35 = sheet.getRow(42);
        row35.getCell(4).setCellValue(sjs.getBjsf());
        XSSFRow row36 = sheet.getRow(43);
        row36.getCell(4).setCellValue(sjs.getQtje());
        XSSFRow row37 = sheet.getRow(44);
        row37.getCell(4).setCellValue(sjs.getFje());
        XSSFRow row38 = sheet.getRow(45);
        row38.getCell(4).setCellValue(sjs.getXzd());
        XSSFRow row39 = sheet.getRow(46);
        row39.getCell(4).setCellValue(sjs.getXdzd());
        XSSFRow row40 = sheet.getRow(47);
        row40.getCell(4).setCellValue(sjs.getYhlc());
        XSSFRow row41 = sheet.getRow(48);
        row41.getCell(4).setCellValue(sjs.getQt());
        workbook.write(response.getOutputStream());
        response.setContentType("application/octet-stream;charset=UTF-8");
        try {
            response.addHeader("Content-Disposition", "attachment;filename=" + new String("sjs".getBytes(), "iso-8859-1") + ".xlsx");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        workbook.write(response.getOutputStream());
        return ResponseFormat.retParam(1, 200, null);
    }


    @OperationLog(
            success = "内控指引列表",
            busType = "内控设置",
            fail = "内控指引列表",
            operationType = OperationType.SELECT,
            subType = "内控设置-查看内控指引管理-列表内容"
    )
	@GetMapping("/complianceWeenlyList")
	@Operation(summary = "内控指引管理-列表")
	public JsonBean complianceWeenlyList(HttpServletRequest request, TblComplianceWeekly tblComplianceWeekly,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			jsonBean = tblComplianceSendService.complianceWeenlyList(token, pageNumber, pageSize,tblComplianceWeekly);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}

    @OperationLog(
            success = "内控指引列表新增",
            busType = "内控设置",
            fail = "内控指引列表新增",
            operationType = OperationType.ADD,
            subType = "内控基础配置——内控指引-对内控指引列表进行新增修改操作"
    )
	@RequestMapping(value = "/complianceWeenlySave", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
	@Operation(summary = "内控指引-新增修改")
    public JsonBean complianceWeenlySave(HttpServletRequest request,@Parameter(name = "tblComplianceWeekly", description = "实体", required = true)TblComplianceWeekly tblComplianceWeekly,
			 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token)throws Exception{
		JsonBean jsonBean = null;
		try {
			jsonBean = this.tblComplianceSendService.complianceWeenlySave(tblComplianceWeekly,token);
		} catch (Exception e) {
			ResponseFormat.retParam(1,1000,e.getMessage());
		}
		return jsonBean;
    }

    @OperationLog(
            success = "详情页面",
            busType = "内控设置",
            fail = "详情页面",
            operationType = OperationType.SELECT,
            subType = "内控基础配置——查看内控指引指定数据的详情页面【{{id}}】"
    )
	@GetMapping("/complianceWeenlyDetail")
    @Operation(summary = "内控指引-明细页面")
    public JsonBean complianceWeenlyDetail(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "id", description = "主键", required = true)@RequestParam(value = "id", required = true) String id) {
    	JsonBean jsonBean = null;
    	try {
			jsonBean = tblComplianceSendService.complianceWeenlyDetail(token,id);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return jsonBean;
    }

    @OperationLog(
            success = "内控指引删除",
            busType = "内控设置",
            fail = "内控指引删除",
            operationType = OperationType.DELETE,
            subType = "基础配置——内控指引——内控指引删除【{{id}}】"
    )
	@GetMapping("/complianceWeenlyDel")
	@Operation(summary = "内控指引-删除")
    public JsonBean complianceWeenlyDel(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token")String token,
    		@Parameter(name = "id", description = "主键", required = true)@RequestParam(value = "id", required = true) String id) {
        
        try {
			return tblComplianceSendService.complianceWeenlyDel(id, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
        return null;
    }

    @OperationLog(
            success = "内控指引列表",
            busType = "内控设置",
            fail = "内控指引列表",
            operationType = OperationType.SELECT,
            subType = "内控基础配置-内控指引列表查询"
    )
	@GetMapping("/complianceWeenlyRead")
	@Operation(summary = "内控指引-失效")
	public JsonBean complianceWeenlyRead(HttpServletRequest request, TblComplianceWeekly tblComplianceWeekly,
			@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
			@Parameter(name = "pageNumber", description = "分页当前页数", required = false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
			@Parameter(name = "pageSize", description = "每页记录数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize) {

		JsonBean jsonBean = null;
		try {
			tblComplianceWeekly.setEffstatus(1);
			jsonBean = tblComplianceSendService.complianceWeenlyList(token, pageNumber, pageSize,tblComplianceWeekly);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
	}
	
}
