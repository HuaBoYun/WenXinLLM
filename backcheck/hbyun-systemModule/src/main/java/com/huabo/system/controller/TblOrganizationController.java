package com.huabo.system.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipOutputStream;

import javax.annotation.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.OldExcelFormatException;
import org.apache.poi.poifs.filesystem.NotOLE2FileException;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.hbfk.config.SystemStaticValue;
import com.hbfk.config.YMUrlStatic;
import com.hbfk.entity.DealUserToken;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.IpUtil;
import com.hbfk.util.JedisUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.user.UserProvider;
import com.huabo.system.entity.Find;
import com.huabo.system.entity.Node;
import com.huabo.system.entity.ProcessSetting;
import com.huabo.system.entity.RiskClass;
import com.huabo.system.entity.TblAccBook;
import com.huabo.system.entity.TblAssessTarget;
import com.huabo.system.entity.TblAuditOption;
import com.huabo.system.entity.TblAuthorizationRecord;
import com.huabo.system.entity.TblBiReportMenu;
import com.huabo.system.entity.TblCirculation;
import com.huabo.system.entity.TblContractTypeof;
import com.huabo.system.entity.TblControlmatrix;
import com.huabo.system.entity.TblCourse;
import com.huabo.system.entity.TblCwsjDscjInfo;
import com.huabo.system.entity.TblExternalExpert;
import com.huabo.system.entity.TblFinanceData;
import com.huabo.system.entity.TblFlow;
import com.huabo.system.entity.TblFlowBussiness;
import com.huabo.system.entity.TblFlowIOuterrule;
import com.huabo.system.entity.TblFlowInnerRule;
import com.huabo.system.entity.TblFlowdes;
import com.huabo.system.entity.TblHomePageModel;
import com.huabo.system.entity.TblImplog;
import com.huabo.system.entity.TblIndicator;
import com.huabo.system.entity.TblIndicatorthreshold;
import com.huabo.system.entity.TblIndustryInner;
import com.huabo.system.entity.TblInnerrule;
import com.huabo.system.entity.TblInteriorExpert;
import com.huabo.system.entity.TblJob;
import com.huabo.system.entity.TblLoginType;
import com.huabo.system.entity.TblManageRight;
import com.huabo.system.entity.TblManageUserBook;
import com.huabo.system.entity.TblManageUserRight;
import com.huabo.system.entity.TblMonitorIndicatorresult;
import com.huabo.system.entity.TblMonitorModel;
import com.huabo.system.entity.TblMonitorRule;
import com.huabo.system.entity.TblMonitorSolution;
import com.huabo.system.entity.TblMonitorSolutionresult;
import com.huabo.system.entity.TblNbsjAuditplan;
import com.huabo.system.entity.TblNbsjPlanproject;
import com.huabo.system.entity.TblNbsjProject;
import com.huabo.system.entity.TblNbsjSheet;
import com.huabo.system.entity.TblNbsjSheetReport;
import com.huabo.system.entity.TblOrgNo;
import com.huabo.system.entity.TblOrgRight;
import com.huabo.system.entity.TblOrgRightnew;
import com.huabo.system.entity.TblOrganization;
import com.huabo.system.entity.TblOuterrule;
import com.huabo.system.entity.TblRisk;
import com.huabo.system.entity.TblRiskFlow;
import com.huabo.system.entity.TblRiskcategory;
import com.huabo.system.entity.TblRiskevent;
import com.huabo.system.entity.TblRole;
import com.huabo.system.entity.TblStaff;
import com.huabo.system.entity.TblSystemImportLog;
import com.huabo.system.entity.TblSystemModule;
import com.huabo.system.entity.TblUserOrgRelation;
import com.huabo.system.entity.TblVersion;
import com.huabo.system.entity.TblVideoType;
import com.huabo.system.entity.TblYyUserOrder;
import com.huabo.system.entity.TblYyUserQuery;
import com.huabo.system.exception.ServiceException;
import com.huabo.system.manager.FileManager;
import com.huabo.system.mapper.TblMyTaskMapper;
import com.huabo.system.mapper.TblOrganizationMapper;
import com.huabo.system.mapper.TblStaffMapper;
import com.huabo.system.oracle.service.TblSystemProjectOracleService;
import com.huabo.system.oracle.vo.CopyVo;
import com.huabo.system.oracle.vo.TblFlowVo;
import com.huabo.system.page.PageBean;
import com.huabo.system.service.ProcessSettingService;
import com.huabo.system.service.TblAccBookService;
import com.huabo.system.service.TblAcctBookService;
import com.huabo.system.service.TblAcquisitionRecordService;
import com.huabo.system.service.TblAssessStaffService;
import com.huabo.system.service.TblAssessTargetService;
import com.huabo.system.service.TblAttachmentService;
import com.huabo.system.service.TblAuditOptionService;
import com.huabo.system.service.TblAuthorizationRecordService;
import com.huabo.system.service.TblAutonoNumberService;
import com.huabo.system.service.TblBiChartService;
import com.huabo.system.service.TblBiCkEchartsService;
import com.huabo.system.service.TblBiReportMenuService;
import com.huabo.system.service.TblCirculationService;
import com.huabo.system.service.TblContractTypeofService;
import com.huabo.system.service.TblControlmatrixService;
import com.huabo.system.service.TblCourseService;
import com.huabo.system.service.TblExternalExpertService;
import com.huabo.system.service.TblFinanceDataService;
import com.huabo.system.service.TblFlowBussinessService;
import com.huabo.system.service.TblFlowIOuterruleService;
import com.huabo.system.service.TblFlowInnerRuleService;
import com.huabo.system.service.TblFlowService;
import com.huabo.system.service.TblFlowdesService;
import com.huabo.system.service.TblHomePageModelService;
import com.huabo.system.service.TblImplogService;
import com.huabo.system.service.TblIndicatorService;
import com.huabo.system.service.TblIndicatorthresholdService;
import com.huabo.system.service.TblIndustryInnerService;
import com.huabo.system.service.TblInteriorExpertService;
import com.huabo.system.service.TblJobService;
import com.huabo.system.service.TblLoginTypeService;
import com.huabo.system.service.TblManageRightService;
import com.huabo.system.service.TblManageScreenRightService;
import com.huabo.system.service.TblManageUserBookservice;
import com.huabo.system.service.TblMonitorIndicatorresultService;
import com.huabo.system.service.TblMonitorModelService;
import com.huabo.system.service.TblMonitorRuleService;
import com.huabo.system.service.TblMonitorSolutionService;
import com.huabo.system.service.TblMonitorSolutionresultService;
import com.huabo.system.service.TblNbsjAuditplanService;
import com.huabo.system.service.TblNbsjPlanprojectService;
import com.huabo.system.service.TblNbsjProjectService;
import com.huabo.system.service.TblNbsjSheetReportService;
import com.huabo.system.service.TblNbsjSheetService;
import com.huabo.system.service.TblOrgExcelService;
import com.huabo.system.service.TblOrgRightService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblProcessAnalysisService;
import com.huabo.system.service.TblRiskService;
import com.huabo.system.service.TblRiskcategoryService;
import com.huabo.system.service.TblRiskeventService;
import com.huabo.system.service.TblRoleService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.service.TblSystemImportLogService;
import com.huabo.system.service.TblSystemModuleService;
import com.huabo.system.service.TblUserOrgRelationService;
import com.huabo.system.service.TblVersionService;
import com.huabo.system.service.TblVideoTypeService;
import com.huabo.system.service.TblYyOrgDepositService;
import com.huabo.system.service.TblYyUserOrderService;
import com.huabo.system.service.TblYyUserQueryService;
import com.huabo.system.service.TblyypriceService;
import com.huabo.system.service.TreeService;
import com.huabo.system.service.UserService;
import com.huabo.system.service.YMBusinessService;
import com.huabo.system.utils.DepartmentUtils;
import com.huabo.system.utils.ExcelUtil;
import com.huabo.system.utils.HttpClient;
import com.huabo.system.utils.MD5Encrypt;
import com.huabo.system.utils.RedisFinalUtis;
import com.huabo.system.utils.RightCatch;
import com.huabo.system.utils.Tree;

import io.netty.util.internal.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import redis.clients.jedis.Jedis;

/**
 * 系统设置控制器
 * <p>提供组织机构管理、流程管理、权限设置、数据同步等系统核心设置接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "设置Controller", description = "设置所有接口")
public class TblOrganizationController extends BaseController implements ServletConfigAware {

    private static final String separator = System.getProperty("file.separator");
    private static final String GROUP_STRUCTURE = PropertyFileReader.getItem("group.structure");
    private static final String acurl = ResourceBundle.getBundle("setting/process").getString("acurl").toString();
    private static final String filepath = getResourceBasePath() + "/src/main/resources/static";
    @Resource
    private HttpServletRequest request;

    @Resource
    private PageBean pageBean;

    @Resource
    private TblLoginTypeService tblLoginTypeService;

    @Resource
    private TblVideoTypeService tblVideoTypeService;

    @Resource
    private TreeService treeService;

    @Resource
    private RedisOrgController redisOrgController;

    @Resource
    private TblStaffService tblStaffService;

    @Resource
    private TblManageRightService tblManageRightService;

    @Resource
    private TblAutonoNumberService tblAutonoNumberService;

    @Resource
    private TblCourseService courseService;

    @Resource
    private TblIndustryInnerService tblIndustryInnerService;

    @Resource
    private TblYyOrgDepositService tblYyOrgDepositService;

    @Resource
    private TblYyUserOrderService tblYyUserOrderService;

    @Resource
    private TblyypriceService tblyypriceService;

    @Resource
    private TblRoleService tblRoleService;

    @Resource
    private TblJobService tblJobService;

    @Resource
    public TblAcctBookService tblAcctBookService;
    @Resource
    private TblFinanceDataService tblFinanceDataService;

    @Resource
    private TblInteriorExpertService tblInteriorExpertService;

    @Resource
    private TblExternalExpertService tblExternalExpertService;

    @Resource
    private TblYyUserQueryService tblYyUserQueryService;

    @Resource
    private TblAccBookService tblAccBookService;

    @Resource
    private TblAcquisitionRecordService tblAcquisitionRecordService;
    @Resource
    private TblMonitorSolutionresultService tblMonitorSolutionresultService;
    @Resource
    private TblNbsjAuditplanService tblNbsjAuditplanService;
    @Resource
    private TblAuditOptionService tblAuditOptionService;

    @Resource
    private TblProcessAnalysisService tblProcessAnalysisService;
    @Resource
    private TblMyTaskMapper tblMyTaskMapper;

    @Resource
    private YMBusinessService ymBusinessService;

    @Resource
	private TblSystemProjectOracleService tblSystemProjectOracleService;

    @Resource
    private TblSystemImportLogService tblSystemImportLogService;

    @Resource
    private UserProvider userProvider;

//    @Resource
//    private TblUserRolerelationService tblUserRolerelationService;

//    @Value("${acurl}")
//    private String acurl;

    @Value("${formurl}")
    private String formurl;

    @Value("redisurl")
    private String redisurl;

    @Value("${activitiModelerUrl}")
    private String activitiModelerUrl;

    @Value("${getOrgChildrenUrl}")
    private String getOrgChildrenUrl;


    @Resource
    public TblImplogService tblImplogService;

    @Resource
    private TblSystemModuleService tblSystemModuleService;

    @Resource
    private TblBiReportMenuService tblBiReportMenuService;

    @Resource
    private TblBiCkEchartsService tblBiCkEchartsService;

    @Resource
    private TblBiChartService tblBiChartService;

    @Resource
    private TblFlowService tblFlowService;

    @Resource
    public TblOrganizaService tblOrganizaService;

    @Resource
    private UserService userService;

    @Resource
    private TblManageScreenRightService tblManageScreenRightService;

    @Resource
    private TblFlowdesService tblFlowdesService;

    @Resource
    private TblFlowBussinessService tblFlowBussinessService;

    @Resource
    private TblRiskeventService tblRiskeventService;

    @Resource
    private TblFlowInnerRuleService tblFlowInnerRuleService;
    @Resource
    private TblContractTypeofService tblContractTypeofService;

    @Resource
    private ProcessSettingService processSettingService;

    @Resource
    private TblFlowIOuterruleService flowIOuterruleService;

    @Resource
    private TblRiskService tblRiskService;

    @Resource
    public TblOrgRightService tblOrgRightService;

    @Resource
    private TblVersionService tblVersionService;

    @Resource
    private TblOrgExcelService tblOrgExcelService;
    @Resource
    private FileManager fileManager;

    @Resource
    private TblManageUserBookservice tblManageUserBookservice;

    @Resource
    public TblRiskcategoryService tblRiskcategoryService;
    @Resource
    private TblHomePageModelService tblHomePageModelService;
    @Resource
    private TblCirculationService tblCirculationService;
    @Resource
    private TblIndicatorService tblIndicatorService;
    @Resource
    private TblMonitorRuleService tblMonitorRuleService;
    @Resource
    private TblMonitorSolutionService tblMonitorSolutionService;
    @Resource
    private TblIndicatorthresholdService tblIndicatorthresholdService;
    @Resource
    private TblMonitorIndicatorresultService tblMonitorIndicatorresultService;
    @Resource
    private TblMonitorModelService tblMonitorModelService;
    @Resource
    private TblNbsjPlanprojectService TblNbsjPlanprojectService;
    @Resource
    private TblAttachmentService tblAttachmentService;
    /*@Resource
    private RepositoryService repositoryService;
    @Resource
    private HistoryService historyService;
    @Resource
    private ProcessEngineConfiguration processEngineConfiguration;*/
    @Resource
    private TblNbsjSheetService tblNbsjSheetService;
    @Resource
    private TblNbsjSheetReportService tblNbsjSheetReportService;
    @Resource
    private TblNbsjProjectService tblNbsjProjectService;
    @Resource
    private TblAssessTargetService tblAssessTargetService;
    @Resource
    private TblAssessStaffService tblAssessStaffService;
    @Resource
    private TblControlmatrixService tblControlmatrixService;

    @Resource
    private TblOrganizationMapper tblOrganizationMapper;

    @Resource
    private TblUserOrgRelationService tblUserOrgRelationService;


    @Resource
    private TblAuthorizationRecordService tblAuthorizationRecordService;

    public static final Integer DEL_YES = 0;
    public static final Integer DEL_NO = 1;
    /**
     * 切换当前公司
     * @param token
     * @return
     */
    public String OID;



    @OperationLog(
            success = "登录用户切换当前所在公司",
            busType = "系统设置",
            fail = "登录用户切换当前所在公司",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/handoff", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="登录用户切换当前所在公司")
    public @ResponseBody
    String handoff (@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "orgid", description = "切换公司", required = true)   @RequestParam(value = "orgid", required = true) String orgid){
        OID = orgid;
        //获取当前token 中的信息
        TblStaffUtil staffUtil = null;
        String result = null;
        Map<String,Object> resultMap = new HashMap<String,Object>(0);

        try {

        	staffUtil = userProvider.get();


            //通过orgid获取公司数据
            TblOrganization linkOrg = this.tblOrganizationMapper.findByid(orgid);

            TblOrganizationUtil orgUtil = new TblOrganizationUtil();
            orgUtil.setOrgid(linkOrg.getOrgid());
            orgUtil.setOrgname(linkOrg.getOrgname());
            orgUtil.setFatherorgid(linkOrg.getFatherorgid());
            orgUtil.setOrgnumber(linkOrg.getOrgnumber());
            orgUtil.setOrgmeno(linkOrg.getOrgmeno());
            orgUtil.setMemo(linkOrg.getMemo());
            orgUtil.setIcode(linkOrg.getIcode());
            orgUtil.setOrgtype(linkOrg.getOrgtype());
            orgUtil.setStatus(linkOrg.getStatus());
            orgUtil.setIszy(linkOrg.getIszy());
            orgUtil.setUseSecrect(linkOrg.getUseSecrect());
            //将orgid获取到的公司数据封装到 当前
            // 用户中
            staffUtil.setCurrentOrg(orgUtil);
            String newtoken = EncryptUtil.getInstance().AESencode(staffUtil.getStaffid().toString(), EncryptUtil.DESKEY);
            newtoken = UriUtils.encode(newtoken, "utf-8");

            userProvider.add(staffUtil);
            resultMap.put("code", "1");
            resultMap.put("msg", "切换成功");
            resultMap.put("token", newtoken);
        }catch(Exception e) {
        	e.printStackTrace();
        }
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }



    /**
     * 审计 工作底稿--审批
     *
     * @Author: TYB
     * @Date: 2017-03-06 下午 5:02
     * @Des: 审计 工作底稿--审批
     */
    @Operation(summary="审计工作底稿-审批")
    @RequestMapping(value = "/sjss/to_sp_dggl", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String to_sp_dggl(HttpServletRequest request,
                             @RequestParam(value = "spid", required = false) BigDecimal spid,
                             @RequestParam(value = "tid", required = false) String tid,
                             @RequestParam(value = "cyid", required = false) String cyid) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            try {
                TblNbsjSheet sheet = this.tblNbsjSheetService.get(spid);
                if (StringUtils.isNotBlank(tid) && sheet.getState() != TblNbsjSheet.STATE5) {
                    List<String> btnList = processSettingService.getButtonsForTransition(tid);
                    resultMap.put("btnList", btnList);
                }
                if (StringUtils.isNotBlank(cyid)) {
                    TblCirculation cy = tblCirculationService.get(cyid);
                    resultMap.put("cy", cy);
                }
                List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelation(sheet.getSheetid().toString(), cyid);
                resultMap.put("sheet", sheet);
                resultMap.put("ao", ao);
                resultMap.put("tid", tid);
                resultMap.put("v", request.getParameter("v"));
                List<TblAttachment> fj = tblAttachmentService.findAllByTblNBSJSheet(spid.toString());
                List<TblNbsjSheetReport> tnsrList = tblNbsjSheetReportService.findReportListBySheet(spid.toString());
                TblNbsjProject project = tblNbsjProjectService.findBySheetid(spid);
                resultMap.put("project", project);
                resultMap.put("tnsrList", tnsrList);
                resultMap.put("fj", fj);
                resultMap.put("spid", spid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    /**
     * 跟踪流程图
     *
     * @Author: TYB
     * @Date: 2016-12-16 下午 5:43
     * @Des: 跟踪流程图
     */
    @Operation(summary="跟踪流程图")
    @RequestMapping(value = "/gz_lct", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String gz_lct(HttpServletRequest request,
                         @RequestParam(value = "businesskey", required = false) String businesskey,
                         @RequestParam(value = "cyid", required = false) String cyid) {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (!StringUtil.isNullOrEmpty(cyid)) {
            TblCirculation cy = tblCirculationService.get(cyid);
            resultMap.put("cy", cy);
//        if(cy.getDefinitionid().equals("cyhw_jjhtjc")){
            resultMap.put("url", HttpClient.jkurl + cy.getBusinesskey());
//        }
        } else if (!StringUtil.isNullOrEmpty(businesskey)) {
            resultMap.put("url", HttpClient.jkurl + businesskey);
        }
        //  resultMap.put("/nbsj/sjgl/gz_lct");
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
    }

    /**
     * 我的评价
     * 我的评价
     *
     * @return
     */

    @Operation(summary="我的评价列表")
    @RequestMapping(value = "/wdpj/t08_mygrade_list", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String t08_mygrade_list(
            @RequestParam(value = "assid", required = false) String assid,
            @RequestParam(value = "assName", required = false) String assName, HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws Exception {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffid = staff.getStaffid();

        PageInfo<TblAssessTarget> pageInfo = new PageInfo<TblAssessTarget>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);

        this.tblAssessTargetService.MyMark(staffid, assid, assName, pageInfo);

        @SuppressWarnings("unchecked")
        List<TblAssessTarget> list2 = pageInfo.getTlist();
        for (TblAssessTarget tar : list2) {
            String status = this.tblAssessStaffService.getStatusByUserAsss(staffid, tar.getAssid(), tar.getOrgid());
            tar.setStatus(status);
            Map<String, String> map = new HashMap<>();
            resultMap.put("pageInfo", pageInfo);
            resultMap.put("status", tar.getStatus());
            resultMap.put("assessname", tar.getAssessname());
            resultMap.put("orgName", tar.getOrgname());

            // list.add(map);
        }
        //pageBean.setRecordList(list);
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
    }


    /**
     * 我的预警-规则详情
     *
     * @param request
     * @return
     */
    @Operation(summary="我的预警-规则详情")
    @RequestMapping(value = "/wdts/resultmgmtgz", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String resultmgmtgz(@RequestParam(value = "pageNumber", required = true, defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "solutionid", required = false) String solutionid,
                               HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            PageInfo<TblMonitorRule> pageInfo = new PageInfo<TblMonitorRule>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            this.tblMonitorRuleService.findAll(solutionid, pageInfo);
            // mv = new ModelAndView("znjk/gzjk/rule/gz_rulesmgmt");
            TblMonitorSolution solution = tblMonitorSolutionService.findOne(solutionid);
            List<String[]> strs = new ArrayList<String[]>();
            PageInfo<TblMonitorSolutionresult> pageIn = new PageInfo<TblMonitorSolutionresult>();
            pageIn.setCurrentPage(pageNumber);
            pageIn.setPageSize(pageSize);
            if (pageInfo.getTlist() != null && pageInfo.getTotalRecord() > 0) {
                List<TblMonitorRule> list = pageBean.getRecordList();
                for (TblMonitorRule tblMonitorRule : list) {
                    String[] str = new String[2];
                    str[0] = tblMonitorRule.getRuleid().toString();
                    //TblAccBook acctbook = this.tblAccBookService.findAllById(new BigDecimal(tblMonitorRule.getConnectionstrings()));
                    TblAccBook acctbook = tblAccBookService.findByBookIdOne(tblMonitorRule.getConnectionstrings());
                    resultMap = tblMonitorSolutionresultService.findBySoultionIdZK("ZNJK_GZ_" + tblMonitorRule.getRuleid().toString(),
                            new BigDecimal(solutionid), tblMonitorRule.getRuleid().toString(), acctbook.getAcctid(), pageIn);
                    if (pageInfo.getTlist() != null && pageInfo.getTotalRecord() > 0) {
                        str[1] = "1";
                    } else {
                        str[1] = "2";
                    }
                    strs.add(str);
                }

            }
            resultMap.put("strs", strs);
            resultMap.put("solution", solution);
            resultMap.put("pageBean", pageBean);
            resultMap.put("solutionid", solutionid);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    /**
     * 判断是否有权限
     *
     * @return
     */
    @Operation(summary="判断是否有权限")
    @RequestMapping(value = "/jurisdiction", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String jurisdiction(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        //Map<String,Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblHomePageModel> pageInfo = new PageInfo<TblHomePageModel>();
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffid = staff.getStaffid();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        Map<String, Object> resultMap = tblHomePageModelService.homePageModels(pageInfo, staffid);
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
    }

    /**
     * 我的传阅
     *
     * @param request
     * @param pageNumber
     * @param find
     * @return
     */


    @OperationLog(
            success = "我的传阅",
            busType = "系统设置",
            fail = "我的传阅",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/my_circulation", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="我的传阅")
    public @ResponseBody
    String my_circulation(HttpServletRequest request, Find find,
                          TblCirculation tca,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<>(0);
        PageInfo<TblCirculation> pageInfo = new PageInfo<TblCirculation>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        //TblStaff user = (TblStaff) request.getSession().getAttribute("longUser");
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffid = staff.getStaffid();
        resultMap = tblCirculationService.findAll(staffid, tca, pageInfo);

        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;

    }

    /**
     * 指标预警
     */

    @OperationLog(
            success = "指标预警列表",
            busType = "系统设置",
            fail = "指标预警列表",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/zb_list", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="指标预警")
    public @ResponseBody
    String zbyj_list(HttpServletRequest request,
                     @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                     @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        String result = null;
        // Map<String,Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblIndicator> pageInfo = new PageInfo<TblIndicator>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffid = staff.getStaffid();
        Map<String, Object> resultMap = tblIndicatorService.findIndicatorByUseridAndSlouid(pageInfo, staffid);
//        for (int i = 0; i < indicator.size(); i++) {
//            Object[] object = indicator.get(i);
//            resultMap.put("INDICATORID", object[0].toString());
//            resultMap.put("INDICATORCODE", object[1].toString());
//            resultMap.put("INDICATORNAME", object[2].toString());
//            resultMap.put("MEMO", object[3]==null?"":object[3].toString());
//            resultMap.put("SOLUTIONID", object[4].toString());
//        }

        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;

    }

    /**
     * /gz_list
     *
     * @param pageNumber
     * @param pageSize
     * @param token
     * @param request
     * @return
     * @throws Exception
     */
    @Operation(summary="规则预警列表")
    @RequestMapping(value = "/gz_list", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String gzyj_list(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                     @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token, HttpServletRequest request) throws Exception {
    	String result = null;
		PageInfo<TblMonitorRule> pageInfo = new PageInfo<TblMonitorRule>();
		pageInfo.setCurrentPage(pageNumber);
		pageInfo.setPageSize(pageSize);
		TblStaffUtil staff = userProvider.get();
		BigDecimal staffid = staff.getStaffid();
		Map<String, Object> resultMap = tblMonitorRuleService.findTblMonitorRuleByUser(staffid, pageInfo);
		resultMap.put("data", pageInfo);
		JSONObject jsonObjectMV = new JSONObject(resultMap);
		result = jsonObjectMV.toString();
		return result;
    }

    /**
     * 规则方案、指标方案、模型， 预警
     *
     * @param request
     * @param type
     * @return
     */
    @Operation(summary="规则方案、指标方案、模型预警")
    @RequestMapping(value = "/wdts/yjfa", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String yjfa(HttpServletRequest request, @RequestParam(value = "type", required = true) String type,
                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    		String result = null;
            PageInfo<TblMonitorSolution> pageInfo = new PageInfo<>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            TblStaffUtil staff = userProvider.get();
            BigDecimal staffid = staff.getStaffid();
            Map<String, Object> resultMap = tblMonitorSolutionService.tblMonitorSolutionService(staffid, pageInfo, type);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    /**
     * 我的预警-指标详情
     *
     * @param request
     * @return
     */
    @Operation(summary="我的预警-指标详情")
    @RequestMapping(value = "/wdts/resultmgmtzb", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String resultmgmtzb(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                        @RequestParam(value = "solutionid", required = true) String solutionid, HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        PageInfo<TblIndicator> pageInfo = new PageInfo<TblIndicator>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        resultMap = tblIndicatorService.findIndicatorByJKZX(solutionid, pageInfo);

        TblMonitorSolution solution = tblMonitorSolutionService.findOne(solutionid);

        resultMap.put("solution", solution);
        resultMap.put("solutionid", solutionid);
        resultMap.put("data", pageInfo);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    /**
     * 指标管理-查看
     *
     * @param request
     * @return
     */

    @OperationLog(
            success = "查看指标管理 ",
            busType = "系统设置",
            fail = "查看指标管理",
            operationType = OperationType.SELECT,
            subType = "指标管理"
    )
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/zbjk/kri_info_disp", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="指标管理-查看")
    public String kri_info_disp(HttpServletRequest request, TblIndicator indicator,
                                @RequestParam(value = "flows", required = false) String flows) {
    		String result = null;
    		Map<String, Object> resultMap = new HashMap<String, Object>(0);
    		if (indicator.getIndicatorid() != null && !"".equals(indicator.getIndicatorid())) {
    			indicator = tblIndicatorService.findOne(indicator.getIndicatorid().toString());
    			Set<TblFlow> fw = indicator.getTblIndicatorFlows();
    			for (TblFlow tblFlow : fw) {
    				flows += tblFlow.getFlowid() + ",";
    			}
    		}
    		TblStaff user = userService.findById(indicator.getStaffid().toString());
    		resultMap.put("indicator", indicator);
    		resultMap.put("flows", flows);
    		resultMap.put("creater", user.getUsername());

    		JSONObject jsonObj = new JSONObject(resultMap);
    		result = jsonObj.toString();
    		return result;
    }


    /**
     * 指标管理-计算公式_查看
     *
     * @param request
     * @return
     */

    @OperationLog(
            success = "计算公式_查看",
            busType = "系统设置",
            fail = "计算公式_查看",
            operationType = OperationType.SELECT,
            subType = "指标管理"
    )
    @RequestMapping(value = "/zbjk/kri_info_formula_disp", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="指标管理-计算公式_查看")
    public String kri_info_formula_disp(HttpServletRequest request,
                                        @RequestParam(value = "indicatorid", required = false) String indicatorid) {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblIndicator indicator = null;
        if (indicatorid != null && !"".equals(indicatorid)) {
            //TblIndicatorService service = (TblIndicatorService) SpringContextHolder.getBean("TblIndicatorService");
            indicator = tblIndicatorService.findOne(indicatorid);
        }
        // mv.setViewName("znjk/zbjk/kri_info_formula_disp");
        resultMap.put("indicatorid", indicatorid);
        resultMap.put("indicator", indicator);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }


    /**
     * 指标管理-指标基准阈值列表-查看
     *
     * @param request
     * @return
     */
    @OperationLog(
            success = "指标基准阈值列表-查看",
            busType = "系统设置",
            fail = "指标基准阈值列表-查看",
            operationType = OperationType.SELECT,
            subType = "指标管理"
    )
    @RequestMapping(value = "/zbjk/kri_info_range_mng_disp", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="指标管理-指标基准阈值列表-查看")
    public String kri_info_range_mng_disp(HttpServletRequest request,
                                          @RequestParam(value = "thresholdid", required = true) String thresholdid,
                                          @RequestParam(value = "orgid", required = false) String orgid) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        List<TblIndicatorthreshold> hold = tblIndicatorthresholdService.findByIndicatorId(thresholdid);
        resultMap.put("thresholdid", thresholdid);
        resultMap.put("hold", hold);
        resultMap.put("orgId", orgid);
        resultMap.put("tableHistoryRows", hold.size());
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    /**
     * 我的预警-查看指标执行结果
     *
     * @param request
     * @return
     */
    @Operation(summary="我的预警-查看指标执行结果")
    @RequestMapping(value = "/wdts/kri_info_result", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String wdts_kri_info_reult(@RequestParam(value = "pageNumber", required = true, defaultValue = "1") Integer pageNumber,
                                      @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                      @RequestParam(value = "indicatorid", required = true) BigDecimal indicatorid,
                                      @RequestParam(value = "solutionresultid", required = true) BigDecimal solutionresultid,
                                      @RequestParam(value = "type", required = false) String type,
                                      HttpServletRequest request) {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblMonitorIndicatorresult> pageInfo = new PageInfo<TblMonitorIndicatorresult>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        // 指标方案
        resultMap = this.tblMonitorIndicatorresultService.getResultListJKZX(pageInfo, indicatorid, solutionresultid);
        resultMap.put("indicatorid", indicatorid);
        resultMap.put("solutionresultid", solutionresultid);
        resultMap.put("type", type);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }


    /**
     * 我的预警-模型详情
     *
     * @param request
     * @return
     */
    @Operation(summary="我的预警-模型详情")
    @RequestMapping(value = "/wdts/resultmgmtmx", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String resultmgmtmx(@RequestParam(value = "pageNumber", required = true, defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                               @RequestParam(value = "solutionid", required = true) String solutionid, HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        PageInfo<TblMonitorModel> pageInfo = new PageInfo<TblMonitorModel>();
        pageInfo.setCurrentPage(pageNumber);
        pageInfo.setPageSize(pageSize);
        resultMap = tblMonitorModelService.findByModelJKZX(solutionid, pageInfo);
        resultMap.put("solutionid", solutionid);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    /**
     * /gzjk/rule/rule_disp
     *
     * @param request
     * @param ruleid
     * @return
     */
    @Operation(summary="规则监控-规则查看")
    @RequestMapping(value = "/gzjk/rule/rule_disp", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String torule_disp(HttpServletRequest request,
                              @RequestParam(value = "ruleid", required = true) BigDecimal ruleid) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblMonitorRule rule = tblMonitorRuleService.findOne(ruleid);
        if (rule != null) {
            if (rule.getOrgid() != null && rule.getOrgid() != null) {
            	TblOrganization o = tblOrganizaService.findByoId(rule.getOrgid());
                resultMap.put("org", o);
           }
            resultMap.put("rule", rule);
        }
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    /**
     * 我的预警-查看规则执行结果
     *
     * @param request
     * @return
     */
    @Operation(summary="我的预警-查看规则执行结果")
    @RequestMapping(value = "/wdts/resultmgmt", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String resultmgmtgzxq(@RequestParam(value = "pageNumber", required = true, defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "pageSize", required = true, defaultValue = "10") Integer pageSize,
                                 @RequestParam(value = "solutionid", required = false) String solutionid,
                                 @RequestParam(value = "type", required = false) String type,
                                 @RequestParam(value = "ruleid", required = false) BigDecimal ruleid,
                                 HttpServletRequest request) {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (ruleid != null && ruleid.toString().length() > 0) {
			 PageInfo<TblMonitorSolutionresult> pageInfo = new PageInfo<TblMonitorSolutionresult>();
			 pageInfo.setCurrentPage(pageNumber);
			 pageInfo.setPageSize(pageSize);
			 TblMonitorRule rule = this.tblMonitorRuleService.findOne(ruleid);
			 TblAccBook acctbook = tblAccBookService.findByBookIdOne(rule.getConnectionstrings());
			 List<String> list = tblMonitorSolutionresultService.find("ZNJK_GZ_" + ruleid, new BigDecimal(solutionid),
			         ruleid.toString(), acctbook.getAcctid());
			 resultMap.put("list", list);
			 resultMap.put("length", list.size() - 1);
			 resultMap = tblMonitorSolutionresultService.findBySoultionIdZKZX("ZNJK_GZ_" + ruleid,
			         new BigDecimal(solutionid), ruleid.toString(), acctbook.getAcctid(), pageInfo);
        }
        resultMap.put("solutionid", solutionid);
        resultMap.put("ruleid", ruleid);
        resultMap.put("type", type);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }


    /**
     * @Author: TYB
     * @Date: 2016-12-02 下午 3:13
     * @Des:审批计划详情页
     */
    @Operation(summary="审批计划详情页")
    @RequestMapping(value = "/jhgl/to_tjspPlan_info", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String to_tjspPlan_info(HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @RequestParam(value = "planid", required = false) String planid,
                                   @RequestParam(value = "tid", required = false) String tid,
                                   @RequestParam(value = "cyid", required = false) String cyid) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffid = staff.getStaffid();
            if (planid != null && planid.length() > 0) {
                TblNbsjAuditplan plan = tblNbsjAuditplanService.get(planid);
                resultMap.put("plan", plan);
            }
            resultMap.put("t", request.getParameter("t"));

            TblNbsjAuditplan form = tblNbsjAuditplanService.get(planid);
            if (form != null) {
                List<TblAuditOption> ao = tblAuditOptionService.findOptionByRelationId(form.getPlanid());
                List<TblNbsjAuditplan> list = tblNbsjAuditplanService.findCheckJH(planid);
                //TblAttachment att = form.getTblplanAtts();
                TblAttachment att = tblAttachmentService.selectAtt(form.getAttid());
                TblStaff user = userService.findById(form.getStaffid().toString());
                if (StringUtils.isNotBlank(tid)) {
                    form.getOpinionstatus().toString();
                    List<String> btnList = processSettingService.getButtonsForTransition(tid);
                    resultMap.put("btnList", btnList);
                }
                resultMap.put("ao", ao);
                resultMap.put("v", request.getParameter("v"));
                resultMap.put("tId", tid);
                resultMap.put("logUser", user);
                resultMap.put("list", list);
                resultMap.put("form", form);
                resultMap.put("att", att);
                if (StringUtils.isNotBlank(cyid)) {
                    TblCirculation cy = tblCirculationService.get(cyid);
                    resultMap.put("cy", cy);
                }
                //修改
                List<TblNbsjPlanproject> findByPlanId = TblNbsjPlanprojectService.findByPlanId(planid);//计划项目
                resultMap.put("findByPlanId", findByPlanId);
                resultMap.put("projectsize", findByPlanId.size());
            }
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    @OperationLog(
            success = "新建前查询",
            busType = "机构管理",
            fail = "新建前查询",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/org/add", method = {RequestMethod.POST})
    @Operation(summary="公司管理-新建前查询")
    public String orgAdd(HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String pid = request.getParameter("pid");

		Integer orderid = (Integer) tblOrganizaService.countOrg();
		if (pid == null) {
		    resultMap = (Map<String, Object>) new ModelAndView("code", HttpStatus.valueOf("1"));
		} else {
		    TblOrganization org = tblOrganizaService.findById(pid);
		    resultMap = (Map<String, Object>) new ModelAndView("code", HttpStatus.valueOf("1"));
		    JSONObject orgObject = new JSONObject((Map<String, Object>) org);
		}
        JSONObject jsonObject = new JSONObject(resultMap);
        result = jsonObject.toString();
        return result;
    }


    @OperationLog(
            success = "切换公司树",
            busType = "系统设置",
            fail = "切换公司树",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/redisorg/getRootOrganizationTree", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="系统 公司切换功能 公司tree")
    public String getRootOrganizationTree(String type, HttpServletRequest request,
    	@Parameter(name = "nodeId", description = "点击公司主键", required = true) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staff = userProvider.get();
        if (null == nodeId) {
            nodeId = staff.getLinkOrg().getOrgid();
        }
        String str = this.tblOrganizaService.getRootOrganizationTree(nodeId,type);
        this.tblOrganizaService.getRootOrganizationTree(nodeId,type);
        /*HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        if (StringUtils.isNotBlank(type)) {
            try {
                str = HttpClient.request(HttpClient.getOrgChildrenUrl, fields, null);
                if (!StringUtils.isNotBlank(str)) {
                    List<Tree> list = this.tblOrganizaService.getJTTreeNodeId(nodeId);
                    str = JSONObject.toJSONString(list);
                }
                return str;
            } catch (Exception e) {
                List<Tree> list = this.tblOrganizaService.getJTTreeNodeId(nodeId);
                str = JSONObject.toJSONString(list);
            }
        } else {
            try {
                str = HttpClient.request(HttpClient.getOrgUrl, fields, null);
                if (!StringUtils.isNotBlank(str)) {
                    List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                    str = JSONObject.toJSONString(list);
                }
                return str;
            } catch (Exception e) {
                List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                str = JSONObject.toJSONString(list);
            }
        }*/
        return str;
    }

    @OperationLog(
            success = "切换公司树只查当前权限",
            busType = "系统设置",
            fail = "切换公司树，只保留当前查询权限",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/redisorg/getRootOrganizationTreeA", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="系统 公司切换功能 公司tree 圈定权限")
    public String getRootOrganizationTreeA(String type, HttpServletRequest request,
                                          @Parameter(name = "nodeId", description = "点击公司主键", required = true) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        System.out.println("系统 公司切换功能 公司tree 圈定权限");
        TblStaffUtil staff = userProvider.get();
        if (null == nodeId) {
            nodeId = staff.getLinkOrg().getOrgid();
        }
        String str = this.tblOrganizaService.getRootOrganizationTreeA(nodeId,type,staff);
        return str;
    }


    @OperationLog(
            success = "左侧列表",
            busType = "机构管理",
            fail = "左侧列表",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/redisorg/findOrganizationByJTTreeAllGS", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="公司管理-列表-左侧列表")
    public @ResponseBody
    String redisorgFindOrganizationByJTTreeAllGS(String type, HttpServletRequest request,
    		@Parameter(name = "nodeId", description = "公司主键", required = false)@RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
    		@Parameter(name = "orgName", description = "筛选条件-公司名称", required = false) @RequestParam(value = "orgName", required = false) String orgName) throws Exception {
    	TblStaffUtil staff = userProvider.get();
        if (null == nodeId) {
        	nodeId = staff.getCurrentOrg().getOrgid();
        }
        String str;
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        if (StringUtils.isNotBlank(type)) {
            try {
                str = HttpClient.request(HttpClient.getOrgChildrenUrl, fields, null);
                if (!StringUtils.isNotBlank(str)) {
                    List<Tree> list = this.tblOrganizaService.getJTTreeNodeId(nodeId);
                    str = JSONObject.toJSONString(list);
                }
                return str;
            } catch (Exception e) {
                List<Tree> list = this.tblOrganizaService.getJTTreeNodeId(nodeId);
                str = JSONObject.toJSONString(list);
            }
        } else {
        	if(StringUtils.isNotBlank(orgName)) {
        		//筛选公司名称
        		str = this.tblOrganizaService.findCompanyTreeListByOrgName(orgName);
        	}else {
        		//无筛选查询所有
        		try {
                    str = HttpClient.request(HttpClient.getOrgUrl, fields, null);
                    if (!StringUtils.isNotBlank(str)) {
                        List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                        str = JSONObject.toJSONString(list);
                    }
                    return str;
                } catch (Exception e) {
                    List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                    str = JSONObject.toJSONString(list);
                }
        	}
        }
        return str;
    }


    @OperationLog( success = "新建公司", busType = "机构管理", fail = "新建公司",
	 operationType = OperationType.ADD, subType = "公司管理" )
    @RequestMapping(value = "/org/saveorg", method = {RequestMethod.POST})
    @Operation(summary="公司管理-新建")
    public String saveorg(HttpServletRequest request, TblOrganization org,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "selectedUser", description = "登录用户主键", required = false) String selectedUser) throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        TblStaffUtil staff = userProvider.get();
        TblAuthorizationRecord confirm = null;
        String memo = "";

        if(org.getFatherorgid() == null) {
        	org.setFatherorgid(staff.getCurrentOrg().getOrgid());
        }

        //数据校验
        TblOrganization rootOrg = this.tblOrganizaService.findRootCompanyInfoByOrgId(org.getFatherorgid());

        Integer repcount = this.tblOrganizaService.selectRepeatName(org.getOrgname(),rootOrg.getOrgnumber(),org.getOrgid());
    	if(repcount > 0 ) {
    		resultMap.put("code", "0");
            resultMap.put("msg", "公司名称重复！");
            JSONObject jsonObj = new JSONObject(resultMap);
            return jsonObj.toString();
    	}

    	repcount = this.tblOrganizaService.selectRepeatNumber(org.getOrgnumber(),rootOrg.getOrgnumber(),org.getOrgid());
    	if(repcount > 0 ) {
    		resultMap.put("code", "0");
            resultMap.put("msg", "公司编号重复！");
            JSONObject jsonObj = new JSONObject(resultMap);
            return jsonObj.toString();
    	}

    	org.setUniqueNumber(rootOrg.getOrgnumber());

    	if(org.getOrgid() != null) {
    		TblOrganization organization = tblOrganizaService.findById(org.getOrgid().toString());

    		TblOrganization faOrg = this.tblOrganizaService.findByid(org.getFatherorgid().toString());
            org.setOrgtype(faOrg.getOrgtype()==null?1:faOrg.getOrgtype()+1);
            org.setOrganizationTrees(faOrg.getOrganizationTrees()+","+org.getOrgid());
            if(org.getFatherorgid().compareTo(organization.getFatherorgid()) != 0) {
           	 	Integer orderNum = this.tblOrganizationMapper.selectChildrenCount(org.getFatherorgid());
           	 	org.setOrderid(orderNum+1);
            }


            if(SystemStaticValue.REQUIREMENTVALIDATE) {
            	//判断是否有正在审批的数据
            	confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(org.getOrgid().toString());

            	if(StringUtils.isNotBlank(org.getOrgname()) && !org.getOrgname().equals(organization.getOrgname())) {
            		memo += "修改公司名称："+organization.getOrgname()+"——>"+org.getOrgname()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getOrgnumber()) &&!org.getOrgnumber().equals(organization.getOrgnumber())) {
            		memo += "修改该公司编号："+organization.getOrgnumber()+"——>"+org.getOrgnumber()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getOrgmeno()) &&!org.getOrgmeno().equals(organization.getOrgmeno())) {
            		memo += "修改该公司介绍："+organization.getOrgmeno()+"——>"+org.getOrgmeno()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getMemo()) &&!org.getMemo().equals(organization.getMemo())) {
            		memo += "修改该公司备注："+organization.getMemo()+"——>"+org.getMemo()+"；";
            	}
            	if(org.getFatherorgid().compareTo(organization.getFatherorgid()) != 0) {
            		memo += "修改上级公司信息为："+faOrg.getOrgname()+"；";
            	}


            	if(organization.getUseSecrect() != org.getUseSecrect()) {
            		if(org.getUseSecrect() == 0) {
               	 		memo += "密集修改为启用；";
               	 	}else {
               	 		memo += "密级修改为弃用；";
               	 	}
            	}
            	if(organization.getIsautonumber() != org.getIsautonumber()) {
            		if(org.getIsautonumber() == 1) {
               	 		memo += "自动编号修改为启用；";
               	 	}else {
               	 		memo += "自动编号修改为弃用；";
               	 	}
            	}

            	if(StringUtils.isNotBlank(org.getIszy()) && !org.getIszy().equals(organization.getIszy())) {
            		if("0".equals(org.getIszy())) {
               	 		memo += "望远镜修改为弃用；";
               	 	}else {
               	 		memo += "望远镜修改为启用；";
               	 	}
            	}

            	if(confirm != null) {
            		//修改审批中的确认数据
            		confirm.setModifiedTime(new Date());
            		confirm.setModifier(staff.getStaffid());
            		confirm.setModifyerName(staff.getRealname());
            		confirm.setOperationData(JSONObject.toJSONString(org));
            		confirm.setOperationMemo("修改公司信息，"+memo);
            		confirm.setRecordText("修改公司"+organization.getOrgname());
            		this.tblAuthorizationRecordService.modifyEntity(confirm);
            	}else {
            		//新增确认记录需要发起流程
            		confirm = new TblAuthorizationRecord();
            		confirm.setRecordId(RandomUtil.uuStringId());
            		confirm.setCreationTime(new Date());
            		confirm.setCreator(staff.getStaffid());
            		confirm.setCreatorName(staff.getRealname());
               	 	confirm.setOperationData(JSONObject.toJSONString(org));
               	 	confirm.setOperationMemo("修改公司信息，"+memo);
               	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
               	 	confirm.setStatus(0);
               	 	confirm.setTargetId(org.getOrgid().toString());
               	 	confirm.setRecordText("修改公司"+organization.getOrgname());
               	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPECOMPANY);
               	 	this.tblAuthorizationRecordService.addEntity(confirm);
            	}
            	resultMap.put("data", confirm);
            }else {
            	organization.setOrgmeno(org.getOrgmeno());
                organization.setOrgname(org.getOrgname());
                organization.setOrgnumber(org.getOrgnumber());
                organization.setMemo(org.getMemo());
                organization.setIszy(org.getIszy());
                organization.setIsautonumber(org.getIsautonumber());
                organization.setIsautonumber(org.getIsautonumber());
                organization.setWrittenByDept(org.getWrittenByDept());
                organization.setUseSecrect(org.getUseSecrect());
                organization.setOrgtype(org.getOrgtype());
                organization.setOrganizationTrees(org.getOrganizationTrees());
                organization.setOrderid(org.getOrderid());
                organization.setOrgcreate(new Date());
                organization.setBgimage(org.getBgimage());
                organization.setBgname(org.getBgname());
                organization.setLogoimage(org.getLogoimage());
                organization.setLogoname(org.getLogoname());
                organization.setJdztimage(org.getJdztimage());
                organization.setJdztname(org.getJdztname());
                organization.setCtztimage(org.getCtztimage());
                organization.setCtztname(org.getCtztname());
                organization.setBaname(org.getBaname());
            	tblOrganizaService.modiOrganization(organization);
                //公司数据同步至业务中台
                if(YMUrlStatic.status == 0) {
                	this.ymBusinessService.dealUniqueOrgInfo(organization,YMUrlStatic.ORGUPDATE);
                }
                if (org.getIsautonumber() == 1) {
                    tblAutonoNumberService.saveOrgNumber(org.getOrgid());
                }
            }



    	}else {
    		//新增
    		 TblOrganization faOrg = this.tblOrganizaService.findByid(org.getFatherorgid().toString());
             org.setOrgtype(faOrg.getOrgtype()==null?1:faOrg.getOrgtype()+1);
             org.setOrgid(RandomUtil.uuBigDecimalId());
             org.setOrganizationTrees(faOrg.getOrganizationTrees()+","+org.getOrgid());
             org.setOrgcreate(new Date());

             //查询排序默认+1
             if(org.getOrderid() == null) {
            	 Integer orderNum = this.tblOrganizationMapper.selectChildrenCount(org.getFatherorgid());
            	 org.setOrderid(orderNum+1);
             }

             if(SystemStaticValue.REQUIREMENTVALIDATE) {
            	 //需要插入确认表
            	 confirm = new TblAuthorizationRecord();
            	 confirm.setRecordId(RandomUtil.uuStringId());
            	 confirm.setCreationTime(new Date());
            	 confirm.setCreator(staff.getStaffid());
            	 confirm.setCreatorName(staff.getRealname());
            	 confirm.setOperationData(JSONObject.toJSONString(org));
            	 if(org.getUseSecrect() == 0) {
            		 memo += "未启用密级，";
            	 }else {
            		 memo += "已启用密级，";
            	 }
            	 if(org.getIsautonumber() == 1) {
            		 memo += "启用自动编号，";
            	 }else {
            		 memo += "未启用自动编号，";
            	 }
            	 if("0".equals(org.getIszy())) {
            		 memo += "未启用望远镜。";
            	 }else {
            		 memo += "启用望远镜。";
            	 }

            	 confirm.setOperationMemo("新增公司信息，公司名称："+org.getOrgname()+"、公司编号："+org.getOrgnumber()+"、公司介绍："+org.getOrgmeno()+"、公司备注："+org.getMemo()+"，"+memo);
            	 confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
            	 confirm.setStatus(0);
            	 confirm.setTargetId(org.getOrgid().toString());
            	 confirm.setRecordText("新增公司"+org.getOrgname());
            	 confirm.setTargetType(TblAuthorizationRecord.TARGETTYPECOMPANY);
            	 this.tblAuthorizationRecordService.addEntity(confirm);
                 resultMap.put("data", confirm);
             }else {
            	 this.tblOrganizaService.addReturnId(org);
                 //公司数据同步至业务中台
                 if(YMUrlStatic.status == 0) {
                 	//系统管理员登录  新增组织
                 	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.ORGINSERT);
                 }
                 if (org.getIsautonumber() == 1) {
                     tblAutonoNumberService.saveOrgNumber(org.getOrgid());
                 }

             }

             resultMap.put("code", "1");
             resultMap.put("msg", "新增成功");
    	}

        //外聘专家库 处理逻辑丢失
        return JSONObject.toJSONString(resultMap);
    }


    @OperationLog(
            success = "导入",
            busType = "机构管理",
            fail = "导入",
            operationType = OperationType.IMPORT,
            subType = "组织架构"
    )
    @PostMapping(value = "/importOrganClassExcel")
    @Operation(summary="组织架构-导入")
    @ResponseBody
    public String importRiskClassExcel(HttpServletRequest request, MultipartFile file,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        InputStream in = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(in);

        try {
            TblStaffUtil staff = userProvider.get();
            XSSFCell cell = null;
            XSSFRow row = null;
            TblOrganization org = null;
            Integer importCount = 0;//导入数量
            Integer totalCount = 0;//总数量
            Integer repartCount = 0;//验重数量
            TblOrganization fatherOrg = null;
            TblOrganization rootOrg = null;
            TblOrganization companyOrg = null;
            String noFather = "";//记录上级组织为空导入失败的组织信息
            String noNumber = ""; //记录组织编号为空导入失败的组织信息
            String noName = "";//记录组织名称为空导入失败的组织信息
            String noType = "";//记录组织类型为空导入失败的组织信息
            String reName = "";//记录组织名称重复导入失败的组织信息
            String reNumber = "";//记录组织编号重复导入失败的组织信息
            BigDecimal principalStaffId = null;//部门负责人主键
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                //获取工作薄中的工作表
                XSSFSheet sheet = workbook.getSheetAt(i);
                //遍历工作表的行,从第3行开始读数据(索引从0开始)
                for (int x = 1; x < sheet.getPhysicalNumberOfRows(); x++) {
                    org = new TblOrganization();
                    row = sheet.getRow(x);
                    //如果此工作表有数据
                    if (row != null) {
                        //遍历每行中每列的数据 row.getPhysicalNumberOfCells()
                        //获取单元格
                        cell = row.getCell(0);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setOrgnumber(cell.getStringCellValue());
                        }


                        cell = row.getCell(1);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setOrgname(cell.getStringCellValue());
                        }
                        cell = row.getCell(2);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            principalStaffId = this.tblStaffMapper.selectStaffIdByUserName(cell.getStringCellValue());
                            org.setPrincipalStaffId(principalStaffId);

                        }
                        cell = row.getCell(3);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setOrgtype("部门".equals(cell.getStringCellValue())?0:1);

                        }

                        cell = row.getCell(4);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setFahterOrgName(cell.getStringCellValue());

                        }

                        cell = row.getCell(5);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setStatus("启用".equals(cell.getStringCellValue())?0:1);

                        }

                        cell = row.getCell(6);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setAuditType("是".equals(cell.getStringCellValue())?1:0);

                        }

                        cell = row.getCell(7);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setOrgmeno(cell.getStringCellValue());

                        }

                        cell = row.getCell(8);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            org.setMemo(cell.getStringCellValue());

                        }
                    }
                    //判断各个条件是否为空
                    if(StringUtils.isBlank(org.getOrgnumber())) {
                    	totalCount++;
                    	noNumber+= totalCount+"、";
                    	continue;
                    }
                    if(StringUtils.isBlank(org.getOrgname())) {
                    	totalCount++;
                    	noName+= totalCount+"、";
                    	continue;
                    }
                    if(org.getOrgtype() == null) {
                    	totalCount++;
                    	noType+= totalCount+"、";
                    	continue;
                    }
                    if(StringUtils.isBlank(org.getFahterOrgName())) {
                    	totalCount++;
                    	noFather+= totalCount+"、";
                    	continue;
                    }
                    //1.获取上级组织是否存在
                    fatherOrg = this.tblOrganizaService.findByOrgNumber(org.getFahterOrgName());
                    if(fatherOrg == null) {
                    	totalCount++;
                    	noFather+= totalCount+"、";
                    	continue;
                    }

                    org.setFatherorgid(fatherOrg.getOrgid());

                    rootOrg = this.tblOrganizaService.findRootCompanyInfoByOrgId(fatherOrg.getOrgid());
                    //验证公司编号名称是否重复 ，公司 部门两套逻辑
                    if(org.getOrgtype() == 0) {
                    	//部门验证
                    	if(fatherOrg.getOrgtype() == 0) {
                    		//上级组织是部门需要获取所属公司做编号验重
                    		companyOrg = this.tblOrganizaService.findCompanyInfoByDeptId(fatherOrg.getOrgid());
                    	}else {
                    		companyOrg = fatherOrg;
                    	}
                    	repartCount = this.tblOrganizaService.selectRepeatNumber(org.getOrgnumber(),rootOrg.getOrgnumber()+companyOrg.getOrgnumber(), null);
                    	if(repartCount > 0 ) {
                    		totalCount++;
                    		reNumber += org.getOrgname()+"、";
                        	continue;
                    	}
                    	org.setUniqueNumber(rootOrg.getOrgnumber()+companyOrg.getOrgnumber());

                    }else {
                    	//公司验证
                    	repartCount = this.tblOrganizaService.selectRepeatName(org.getOrgname(),rootOrg.getOrgnumber(), null);
                    	if(repartCount > 0) {
                    		totalCount++;
                    		reName += org.getOrgname()+"、";
                        	continue;
                    	}
                    	repartCount = this.tblOrganizaService.selectRepeatNumber(org.getOrgnumber(),rootOrg.getOrgnumber(), null);
                    	if(repartCount > 0) {
                    		totalCount++;
                    		reNumber += org.getOrgname()+"、";
                        	continue;
                    	}
                    	//获取公司orgtype
                    	fatherOrg = this.tblOrganizaService.findCompanyInfoByDeptId(fatherOrg.getOrgid());
                    	org.setOrgtype(fatherOrg.getOrgtype()+1);
                    	org.setUniqueNumber(rootOrg.getOrgnumber());
                    }

                    org.setOrgid(RandomUtil.uuBigDecimalId());
                	org.setOrganizationTrees(fatherOrg.getOrganizationTrees()+","+org.getOrgid());

                    importCount++;
                    totalCount++;
                    org.setOrgcreate(new Date());
                    tblOrganizaService.addReturnId(org);

                    //公司数据同步至业务中台
                    if(YMUrlStatic.status == 0) {
                    	if(org.getOrgtype() == 0) {
                    		this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.DEPTINSERT);
                    	}else {
                    		this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.ORGINSERT);
                    	}
                    }
                }
            }

            //存储导入记录
            TblSystemImportLog imlog = new TblSystemImportLog();
            imlog.setLogId(RandomUtil.uuStringId());
            imlog.setCreateStaff(staff.getStaffid());
            imlog.setCreateTime(new Date());
            imlog.setImportCount(importCount);
            imlog.setTotalCount(totalCount);
            String content = "";
            if(StringUtils.isNotBlank(noNumber)) {
            	content += noNumber.substring(0, noNumber.length()-1) +"行，组织编号为空，";
            }
            if(StringUtils.isNotBlank(noName)) {
            	content += noName.substring(0, noName.length()-1) +"行，组织名称为空，";
            }
            if(StringUtils.isNotBlank(noType)) {
            	content += noType.substring(0, noType.length()-1) +"行，组织类型为空，";
            }
            if(StringUtils.isNotBlank(noFather)) {
            	content += noFather.substring(0, noFather.length()-1) +"行，组织上级信息为空，";
            }
            if(StringUtils.isNotBlank(reNumber)) {
            	content += reNumber.substring(0, reNumber.length()-1) +"组织编号重复，";
            }
            if(StringUtils.isNotBlank(reName)) {
            	content += reName.substring(0, reName.length()-1) +"组织名称重复，";
            }
            if(StringUtils.isNotBlank(content)) {
            	content += "以上组织信息导入失败。";
            }else {
            	content = "导入成功";
            }
            imlog.setImportContent(content);
            imlog.setImportType(TblSystemImportLog.IMPORTORG);

            this.tblSystemImportLogService.save(imlog);
        } finally {
            //读取完毕则关闭流
            in.close();
            workbook.close();
        }
        return JsonBean.success();
    }

    @OperationLog(
            success = "新增",
            busType = "机构管理",
            fail = "新增",
            operationType = OperationType.ADD,
            subType = "组织架构"
    )
    @RequestMapping(value = "/org/save", method = {RequestMethod.POST})
    @Operation(summary="组织架构-新增")
    public String orgSave(HttpServletRequest request, TblOrganization org,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "selectedUser", required = false) String selectedUser) throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        BigDecimal orgid;
        TblStaffUtil staff = userProvider.get();
        String memo = "";
        //编号校验先去掉
        TblOrganization company = null;
        TblAuthorizationRecord confirm = null;
        if(org.getFatherorgid() == null) {
        	org.setFatherorgid(staff.getCurrentOrg().getOrgid());
        }

        TblOrganization organization = tblOrganizaService.findByOrg(org.getFatherorgid());
        if(organization.getOrgtype() == 0) {
        	company = this.tblOrganizaService.findCompanyInfoByDeptId(organization.getOrgid());
        }else {
        	company = organization;
        }

        TblOrganization rootOrg = this.tblOrganizaService.findRootCompanyInfoByOrgId(org.getFatherorgid());

        Integer count = this.tblOrganizaService.selectRepeatNumber(org.getOrgnumber(),rootOrg.getOrgnumber()+company.getOrgnumber(), org.getOrgid());
    	if(count > 0 ) {
    		 resultMap.put("code", "0");
             resultMap.put("msg", "部门编号重复");
             JSONObject jsonObj = new JSONObject(resultMap);
             result = jsonObj.toString();
             return result;
    	}


        org.setOrgtype(0);
        org.setUniqueNumber(rootOrg.getOrgnumber()+company.getOrgnumber());

        if(org.getOrgid() != null) {
        	//修改
        	TblOrganization preOrg = tblOrganizaService.findByOrg(org.getOrgid());

        	if(SystemStaticValue.REQUIREMENTVALIDATE) {
        		//判断是否有正在审批的数据
            	confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(org.getOrgid().toString());

            	if(StringUtils.isNotBlank(org.getOrgnumber()) && !org.getOrgnumber().equals(preOrg.getOrgnumber())) {
            		memo += "修改该机构编号："+preOrg.getOrgnumber()+"——>"+org.getOrgnumber()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getOrgname()) && !org.getOrgname().equals(preOrg.getOrgname())) {
            		memo += "修改机构名称："+preOrg.getOrgname()+"——>"+org.getOrgname()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getWrittenByDept()) && !org.getWrittenByDept().equals(preOrg.getWrittenByDept())) {
            		memo += "修改发文代字："+preOrg.getWrittenByDept()+"——>"+org.getWrittenByDept()+"；";
            	}
            	if(preOrg.getPrincipalStaffId().compareTo(org.getPrincipalStaffId()) != 0) {
            		memo += "修改部门负责人为："+org.getPrincipalName()+"；";
            	}
            	if(preOrg.getAuditType() != org.getAuditType()) {
            		if(org.getAuditType() == 0) {
	               		memo += "修改取消该部门作为主责部门，";
	               	 }else {
	               		memo += "修改该部门设置为主责部门，";
	               	 }
	           	}

            	if(StringUtils.isNotBlank(org.getOrgmeno()) && !org.getOrgmeno().equals(preOrg.getOrgmeno())) {
            		memo += "修改结构简介："+preOrg.getOrgmeno()+"——>"+org.getOrgmeno()+"；";
            	}
            	if(StringUtils.isNotBlank(org.getMemo()) && !org.getMemo().equals(preOrg.getMemo())) {
            		memo += "修改备注："+preOrg.getMemo()+"——>"+org.getMemo()+"；";
            	}
            	if(org.getFatherorgid().compareTo(preOrg.getFatherorgid()) != 0) {
            		memo += "修改上级公司信息为："+organization.getOrgname()+"；";
            	}

            	if(confirm != null) {
            		//修改审批中的确认数据
            		confirm.setModifiedTime(new Date());
            		confirm.setModifier(staff.getStaffid());
            		confirm.setModifyerName(staff.getRealname());
            		confirm.setOperationData(JSONObject.toJSONString(org));
            		confirm.setOperationMemo("修改部门信息，"+memo);
            		confirm.setRecordText("修改部门"+organization.getOrgname());
            		this.tblAuthorizationRecordService.modifyEntity(confirm);
            	}else {
            		//新增确认记录需要发起流程
            		confirm = new TblAuthorizationRecord();
            		confirm.setRecordId(RandomUtil.uuStringId());
            		confirm.setCreationTime(new Date());
            		confirm.setCreator(staff.getStaffid());
            		confirm.setCreatorName(staff.getRealname());
               	 	confirm.setOperationData(JSONObject.toJSONString(org));
               	 	confirm.setOperationMemo("修改部门信息，"+memo);
               	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
               	 	confirm.setStatus(0);
               	 	confirm.setTargetId(org.getOrgid().toString());
               	 	confirm.setRecordText("修改部门信息"+organization.getOrgname());
               	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEDEPT);
               	 	this.tblAuthorizationRecordService.addEntity(confirm);
            	}
            	resultMap.put("data", confirm);
        	}else {
        		preOrg.setOrgnumber(org.getOrgnumber());
                preOrg.setOrgname(org.getOrgname());
                preOrg.setOrgmeno(org.getOrgmeno());
                preOrg.setMemo(org.getMemo());
                preOrg.setAuditType(org.getAuditType());
                preOrg.setWrittenByDept(org.getWrittenByDept());
                preOrg.setPrincipalStaffId(org.getPrincipalStaffId());
                preOrg.setChargeLeaderStaffId(org.getChargeLeaderStaffId());
                tblOrganizaService.updateZuZhi(preOrg);
                if(YMUrlStatic.status == 0) {
                	this.ymBusinessService.dealUniqueOrgInfo(organization,YMUrlStatic.DEPTUPDATE);
                	//当用户的直属主管分管领导在部门维护时，调用此方法 更新当前部门下所有用户的直属主管
                	//this.orgUserInfoSynchronizationService.setUserOrgRelationPostUnique(organization);
                }
                if (org.getOrgtype() != null && org.getOrgtype() != 0) {
                    TblOrganization neworg = tblOrganizaService.isCompanyAddWPZ(org);
                    orgid = neworg.getOrgid();
                }
        	}
        }else {
        	 //查询排序默认+1
            if(org.getOrderid() == null) {
           	 Integer orderNum = this.tblOrganizationMapper.selectChildrenCount(org.getFatherorgid());
           	 org.setOrderid(orderNum+1);
            }
        	org.setOrgid(RandomUtil.uuBigDecimalId());
        	org.setOrganizationTrees(organization.getOrganizationTrees()+","+org.getOrgid());
        	org.setOrgcreate(new Date());
        	if(SystemStaticValue.REQUIREMENTVALIDATE) {
        		 confirm = new TblAuthorizationRecord();
            	 confirm.setRecordId(RandomUtil.uuStringId());
            	 confirm.setCreationTime(new Date());
            	 confirm.setCreator(staff.getStaffid());
            	 confirm.setCreatorName(staff.getRealname());
            	 confirm.setOperationData(JSONObject.toJSONString(org));
            	 if(org.getAuditType() == 0) {
            		 memo += "该部门不是主责部门，";
            	 }else {
            		 memo += "设置为主责部门，";
            	 }
            	 confirm.setOperationMemo("新增部门信息，机构名称："+org.getOrgname()+"、机构编号："+org.getOrgnumber()+"、发文代字："+org.getWrittenByDept()
            	 +"、部门负责人："+org.getPrincipalName()+"、机构简介："+org.getOrgmeno()+"、备注："+org.getMemo()+"，"+memo);
            	 confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
            	 confirm.setStatus(0);
            	 confirm.setTargetId(org.getOrgid().toString());
            	 confirm.setRecordText("部门公司"+org.getOrgname());
            	 confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEDEPT);
            	 this.tblAuthorizationRecordService.addEntity(confirm);
            	 resultMap.put("data", confirm);
        	}else {
        		tblOrganizaService.addReturnOrg(org);
                if(YMUrlStatic.status == 0) {
                	this.ymBusinessService.dealUniqueOrgInfo(org,YMUrlStatic.DEPTINSERT);
                }
                if (org.getOrgtype() != null && org.getOrgtype() != 0) {
                    TblOrganization neworg = tblOrganizaService.isCompanyAddWPZ(org);
                    orgid = neworg.getOrgid();
                }
        	}
        }


        resultMap.put("code", "1");
        resultMap.put("msg", "成功");
        return JSONObject.toJSONString(resultMap);
    }



    @OperationLog(
            success = "保存",
            busType = "机构管理",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "行业架构"
    )
    @RequestMapping(value = "/org/hy_save", method = {RequestMethod.POST})
    @Operation(summary="机构管理-行业架构-保存")
    public String orgHYSave(HttpServletRequest request, TblOrganization org,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "selectedUser", description = "前端传的ID", required = false) String selectedUser) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        BigDecimal orgid;
        TblStaffUtil staff = userProvider.get();
        String pid = String.valueOf(staff.getCurrentOrg().getOrgid());
            if (org.getOrgid() != null) {
                TblOrganization organization = tblOrganizaService.findById(org.getOrgid().toString());
                // orgid = org.getOrgid();
                organization.setFatherorgid(org.getFatherorgid());
                organization.setOrgname(org.getOrgname());
                organization.setOrgnumber(org.getOrgnumber());
                organization.setMemo(org.getMemo());
                organization.setOrgmeno(org.getOrgmeno());
                organization.setOrgtype(org.getOrgtype());
                tblOrganizaService.updateAtionHangYe(organization);
            } else {
                TblOrganization ation = new TblOrganization();
                ation.setFatherorgid(org.getFatherorgid());
                ation.setOrgname(org.getOrgname());
                ation.setOrgnumber(org.getOrgnumber());
                ation.setMemo(org.getMemo());
                ation.setOrgmeno(org.getOrgmeno());
                ation.setOrgtype(100);
                tblOrganizaService.saveAtionHangYe(ation);
            }


            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }


    @OperationLog(
            success = "修改前查询",
            busType = "机构管理",
            fail = "修改前查询",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/org/modife", method = {RequestMethod.POST})
    @Operation(summary="公司管理-修改前查询")
    public String orgmodife(HttpServletRequest request,
                            @Parameter(name = "pid", required = false) String pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        String selectedUser = request.getParameter("selectedUser");
        Integer orderid = (Integer) tblOrganizaService.countOrg();
        JSONObject jsonObj = new JSONObject(orderid);
        result = jsonObj.toString();
            if (pid == null || selectedUser == null) {
                resultMap = (Map<String, Object>) new HashMap<String, Object>(0);
            } else {
                resultMap.put("code", 1);
                TblOrganization organ = tblOrganizaService.findById(selectedUser);
                JSONObject jsonObject = new JSONObject((Map<String, Object>) organ);
                result = jsonObject.toString();

            }
            return result;
    }


    @OperationLog(
            success = "启用/弃用",
            busType = "机构管理",
            fail = "启用/弃用",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/org/gsdel", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="公司管理-启用/弃用")
    public @ResponseBody
    JsonBean gs(HttpServletRequest request,
              @Parameter(name = "orgid", required = false)@RequestParam("orgid")String orgid,
              @Parameter(name = "str", required = false)@RequestParam("str") String str) throws Exception {
	    	TblStaffUtil staff = userProvider.get();
	    	if(staff == null) {
	    		return ResponseFormat.retParam(0, 20006, null);
	    	}
            if (orgid != null && !orgid.equals("")) {
            	return this.tblOrganizaService.enableCompanyInfo(orgid,str,staff);
            }
            return ResponseFormat.retParam(0, "操作失败", null);


    }


    @OperationLog(
            success = "组织架构-删除",
            busType = "机构管理",
            fail = "组织架构-删除",
            operationType = OperationType.DELETE,
            subType = "公司管理"
    )
    @RequestMapping(value = "/org/isQY", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="公司管理/组织架构-删除")
    public @ResponseBody
    String saveall(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
                   @Parameter(name = "str", required = false) String str) throws Exception {
//        String str = request.getParameter("orgid");
        boolean reuslt = false;
        TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return JsonBean.error("用户已失效");
		}
		Map<String, Object> resultMap = new HashMap<String,Object>(0);
            if (str != null && str != "") {
                        TblOrganization organ = tblOrganizaService.findByOrgid(str);
                        if (organ.getStatus() == null) {
                        	Integer count = this.tblOrganizaService.selectChildrenCount(str);
                        	if(count > 0 ) {
                        		resultMap.put("code", 0);
                        		resultMap.put("msg", "该架构下有组织");
                        		return JSONObject.toJSONString(resultMap);
                        	}

                        	if(SystemStaticValue.REQUIREMENTVALIDATE) {
                        		TblAuthorizationRecord confirm = new TblAuthorizationRecord();
                        		confirm.setRecordId(RandomUtil.uuStringId());
                        		confirm.setCreationTime(new Date());
                        		confirm.setCreator(loginStaff.getStaffid());
                        		confirm.setCreatorName(loginStaff.getRealname());
                           	 	confirm.setOperationData(JSONObject.toJSONString(organ));

                           	 	if(organ.getOrgtype() == 0) {
                           	 		confirm.setOperationMemo("删除部门信息，部门名称："+organ.getOrgname());
                           	 		confirm.setRecordText("删除部门"+organ.getOrgname());
                           	 		confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEDEPT);
                           	 	}else {
                           	 		confirm.setOperationMemo("删除公司信息，公司名称："+organ.getOrgname());
                           	 		confirm.setRecordText("删除公司"+organ.getOrgname());
                           	 		confirm.setTargetType(TblAuthorizationRecord.TARGETTYPECOMPANY);
                           	 	}
                           	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
                           	 	confirm.setStatus(0);
                           	 	confirm.setTargetId(organ.getOrgid().toString());

                           	 	this.tblAuthorizationRecordService.addEntity(confirm);
                           	 	resultMap.put("data", confirm);
                        	}else {
                        		tblOrganizaService.deleteOrg(organ);
                                tblAutonoNumberService.deleteOrgNo(organ.getOrgid());
                        	}
                        	reuslt = true;
                        } else {
                            if (organ.getStatus() != null && organ.getStatus() == 0) {
                            	resultMap.put("code", 0);
                        		resultMap.put("msg", "已启用不可对其修改");
                        		return JSONObject.toJSONString(resultMap);
                            }
                            if (organ.getStatus() != null && organ.getStatus() == 1) {
                            	resultMap.put("code", 0);
                        		resultMap.put("msg", "已废弃");
                        		return JSONObject.toJSONString(resultMap);
                            }
                        }
            }
            if (reuslt) {
            	resultMap.put("code", 1);
        		resultMap.put("msg", "操作成功");
        		return JSONObject.toJSONString(resultMap);
            }
            resultMap.put("code", 0);
    		resultMap.put("msg", "请选择");
    		return JSONObject.toJSONString(resultMap);
    }


    @OperationLog(
            success = "组织架构列表",
            busType = "机构管理",
            fail = "组织架构列表",
            operationType = OperationType.SELECT,
            subType = "组织架构"
    )
    @RequestMapping(value = "/org/list", method = {RequestMethod.POST})
    @Operation(summary="组织架构列表")
    public String orgList(HttpServletRequest request, Find find,
                          @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                          @Parameter(name = "token", description = "登录用户token", required = false) String token,
                          @Parameter(name = "pid", required = false) BigDecimal pid,
                          @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch
    ) throws Exception {
        String result = null;
            try {
                TblStaffUtil staff = DealUserToken.parseUserToken(String.valueOf(token));
                if (pid == null) {
                    pid = staff.getCurrentOrg().getOrgid();
                }
                Map<String, Object> resultMap = tblOrganizaService.findAllOrgBM(pageNumber, pageSize, find, pid);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "左侧菜单",
            busType = "机构管理",
            fail = "左侧菜单",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @RequestMapping(value = "/org/hy_left", method = {RequestMethod.POST})
    @Operation(summary="行业架构-左侧菜单")
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
            success = "查询列表",
            busType = "机构管理",
            fail = "查询列表",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @Operation(summary="行业架构--》列表页")
    @RequestMapping(value = "/org/hy_list", method = {RequestMethod.POST})
    public String orgHyList(HttpServletRequest request,
                            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @Parameter(name = "staffId", required = false) String staffId,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            String pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
            TblOrganization organization = tblOrganizaService.findByname("行业");
            if (pid == null || pid == "") {
                pid = organization.getOrgid().toString();
            }
            if (staffId == null || staffId.equals("")) {
                staffId = organization.getOrgid().toString();
            }
            if (staffId == null || staffId.equals("")) {
                resultMap = tblOrganizaService.findAllHYOrg(pageNumber, pageSize);
            } else {
                resultMap = tblOrganizaService.findAllHYOrgStaffid(pageNumber, pageSize, staffId, token, pid);
            }
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }
    @OperationLog(
            success = "验证行业名称重复",
            busType = "机构管理",
            fail = "验证行业名称重复",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @RequestMapping(value = "/org/checkHyName", method = {RequestMethod.POST})
    @Operation(summary="验证行业名称重复")
    public @ResponseBody
    String checkHyName(HttpServletRequest request, TblOrganization org) {
        String pid = request.getParameter("pid");
        org.setOrgnumber(request.getParameter("code"));
        String id = request.getParameter("id");
            if (org.getOrgid() == null && id != null) {
                org.setOrgid(new BigDecimal(id));
            }
            org.setOrgname(request.getParameter("name"));
            List<TblOrganization> findOrgTree = tblOrganizaService.findPid(pid);
            //修改时验证
            if (org.getOrgid() != null) {
                //旧数据
                TblOrganization beforeorg = tblOrganizaService.findByIdOrgid(org.getOrgid().toString());
                //编号,名称相同,可以提交
                if (org.getOrgnumber().equals(beforeorg.getOrgnumber()) && org.getOrgname().equals(beforeorg.getOrgname())) {
                    return JsonBean.success();
                }
                //编号相同,名称不同
                if (org.getOrgnumber().equals(beforeorg.getOrgnumber()) && !org.getOrgname().equals(beforeorg.getOrgname())) {
                    for (TblOrganization tblOrganization : findOrgTree) {
                        if (org.getOrgname().equals(tblOrganization.getOrgname())) {
                            return JsonBean.error("行业名称重复1");
                        }
                        if (org.getOrgnumber().equals(beforeorg.getOrgnumber())) {
                            return JsonBean.success();
                        }
                    }
                }
                //名称相同,编号不同
                if (!org.getOrgnumber().equals(beforeorg.getOrgnumber()) && org.getOrgname().equals(beforeorg.getOrgname())) {
                    for (TblOrganization tblOrganization : findOrgTree) {
                        if (org.getOrgnumber().equals(tblOrganization.getOrgnumber())) {
                            return JsonBean.error("行业编号重复2");
                        }
                        if (org.getOrgname().equals(beforeorg.getOrgname())) {
                            return JsonBean.success();
                        }
                    }
                }
            }
            //新建时验证
            for (TblOrganization tblOrganization : findOrgTree) {
                if (org.getOrgnumber().equals(tblOrganization.getOrgnumber())) {
                    return JsonBean.error("行业编号重复3");
                }
                if (org.getOrgname().equals(tblOrganization.getOrgname())) {
                    return JsonBean.error("行业名称重复4");
                }
            }
            return JsonBean.success();
    }


    @OperationLog(
            success = "新增",
            busType = "机构管理",
            fail = "新增",
            operationType = OperationType.ADD,
            subType = "行业架构"
    )
    @RequestMapping(value = "/org/hy_msave", method = {RequestMethod.POST})
    @Operation(summary="行业架构-新增")
    public String orgHYModifySave(HttpServletRequest request,
                                  @RequestParam(value = "id", required = false) String id,
                                  @RequestParam(value = "pid", required = false) String pid,
                                  @RequestParam(value = "code", required = false) String code,
                                  @RequestParam(value = "name", required = false) String name,
                                  @RequestParam(value = "desc", required = false) String desc,
                                  @RequestParam(value = "memo", required = false) String memo) {
            if (id != null && !id.equals("")) {

                TblOrganization org = new TblOrganization();
                org.setOrgid(new BigDecimal(id));
                org.setFatherorgid(new BigDecimal(pid));
                org.setOrgname(name);
                org.setOrgnumber(code);
                org.setMemo(memo);
                org.setOrgmeno(desc);
                org.setOrgtype(100);
                this.tblOrganizaService.saveModify(org);
            }
            if (pid == null || pid.equals("")) {
                return "访问数据成功";
            } else {
                return "访问数据错误" + pid;
            }
    }

    @OperationLog(
            success = "删除",
            busType = "机构管理",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "行业架构"
    )
    @RequestMapping(value = "/org/hy_del", method = {RequestMethod.POST})
    @Operation(summary="行业架构-删除")
    public @ResponseBody
    String orgHYDel(HttpServletRequest request, String[] ids,
                    @Parameter(name = "orgid", required = false) BigDecimal orgid,
                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            List<TblOrganization> tion = tblOrganizaService.isParent(orgid.toString());
            if (tion.size() > 0) {
                return JsonBean.error("请先删除包含的其他行业");
            }
            TblStaffUtil staff = userProvider.get();
            List<TblOrganization> orgList = tblOrganizaService.findHYAuthorize(orgid.toString(), staff.getCurrentOrg().getOrgid().toString());
            if (orgList.size() > 0) {
                return JsonBean.error("有授权数据,不允许删除");
            }
            tblIndustryInnerService.deleteByOrgid(orgid);
            tblOrganizaService.delete(orgid);
            resultMap.put("code", "1");
            resultMap.put("msg", "删除成功");
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }


    @OperationLog(
            success = "授权分配",
            busType = "机构管理",
            fail = "授权分配",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @RequestMapping(value = "/organAccreditByHY", method = {RequestMethod.POST})
    @Operation(summary="行业架构-授权分配")
    public String organAccreditByHY(HttpServletRequest request, String[] orgids,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblStaffUtil staff = userProvider.get();
            BigDecimal pid = staff.getCurrentOrg().getOrgid();

            //TblStaff tblStaff = new TblStaff();
            String orgid = pid == null ? null
                    : pid.toString();
            List<TblOrganization> findOrgTree = tblOrganizaService.findAllCompany(orgid, staff.getCurrentOrg().getAudittype());
            String ids = "";
            for (int i = 0; i < orgids.length; i++) {
                ids += orgids[i] + ",";
            }
            resultMap.put("ids", ids);
            resultMap.put("findOrgTree", findOrgTree);
            JSONObject jsonObjectm = new JSONObject(resultMap);
            result = jsonObjectm.toString();
            return result;
    }


    @OperationLog(
            success = "授权分配-选定",
            busType = "机构管理",
            fail = "授权分配-选定",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @RequestMapping(value = "/add_hy_accredit", method = {RequestMethod.POST})
    @Operation(summary="行业架构-授权分配-选定接口")
    public @ResponseBody
    String add_hy_accredit(HttpServletRequest request,
                           @Parameter(name = "orgids", required = false) String orgids,
                           @Parameter(name = "selid", required = false) String selid) throws Exception {
            //String selid = request.getParameter("selid");
            List<TblOrganization> parentHy = tblOrganizaService.parentHy(orgids);
            for (int i = 0; i < parentHy.size(); i++) {
                TblOrganization o = parentHy.get(i);
                TblIndustryInner ii = new TblIndustryInner();
                ii.setOrgid(Long.parseLong(o.getOrgid().toString()));
                ii.setIndustryid(Long.parseLong(selid));
                tblIndustryInnerService.delete(ii);
                tblIndustryInnerService.save(ii);
            }
            String[] ids = orgids.split(",");
            for (int i = 0; i < ids.length; i++) {
                TblIndustryInner ii = new TblIndustryInner();
                ii.setOrgid(Long.parseLong(ids[i]));
                ii.setIndustryid(Long.parseLong(selid));
                try {
                    tblIndustryInnerService.delete(ii);
                    tblIndustryInnerService.save(ii);

                } catch (Exception e) {
                }
            }
            return JsonBean.success();
    }

    @OperationLog(
            success = "取消授权列表",
            busType = "机构管理",
            fail = "取消授权列表",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @RequestMapping(value = "/authorize_to_del", method = {RequestMethod.POST})
    @Operation(summary="行业架构-取消授权列表")
    public String authorize_to_del(HttpServletRequest request, String hyid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
            String result = null;
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            //TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
            TblStaffUtil staff = userProvider.get();
            BigDecimal pid = staff.getCurrentOrg().getOrgid();
            List<TblOrganization> orgList = tblOrganizaService.findHYAuthorize(hyid, pid.toString());
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            resultMap.put("data", orgList);
            JSONObject jsonObjectv = new JSONObject(resultMap);
            result = jsonObjectv.toString();
            return result;
    }


    @OperationLog(
            success = "取消授权确定",
            busType = "机构管理",
            fail = "取消授权确定",
            operationType = OperationType.SELECT,
            subType = "行业架构"
    )
    @PostMapping(value = "/authorize_del")
    @Operation(summary="行业架构-取消授权确定")
    public @ResponseBody
    String authorize_del(HttpServletRequest request, String hyid, String[] orgids) {
            for (int i = 0; i < orgids.length; i++) {
                TblIndustryInner ii = new TblIndustryInner();
                ii.setOrgid(Long.parseLong(hyid));
                ii.setIndustryid(Long.parseLong(orgids[i]));
                try {
                    tblIndustryInnerService.delete(ii);
                } catch (Exception e) {
                }
            }
            return JsonBean.success();
    }


    @Operation(summary="用户管理左侧菜单")
    @RequestMapping(value = "/user/lefts", method = {RequestMethod.POST})
    public String lefts(HttpServletRequest request,
                        @RequestParam(value = "acctid", required = false) String acctid,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
            TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
            //Integer.parseInt("common/gsgl/user_left")
            Map<String, Object> resultMap = new HashMap<String, Object>(0);

            String num = request.getParameter("type");
            resultMap.put("type", num);
            resultMap.put("acctid", acctid);
            resultMap.put("orgid", attribute1.getOrgid());
            resultMap.put("targetFrame", "mainFramex");
            String result = null;
            JSONObject jsonObjectMv = new JSONObject(resultMap);
            result = jsonObjectMv.toString();
            return result;
    }



    @OperationLog(
            success = "主题展示-下发",
            busType = "机构管理",
            fail = "主题展示-下发",
            operationType = OperationType.DISPATCH,
            subType = "系统设置"
    )
    @RequestMapping(value = "/user/list/two", method = {RequestMethod.POST})
    @Operation(summary="主题展示-下发")
    public String userListss(HttpServletRequest request,
                             @RequestParam(value = "pageNames", required = false) String pageNames,
                             @RequestParam(value = "pid", required = false) String pid,
                             @RequestParam(value = "type", required = false) String type,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            if (type != null && type.length() > 0) {
                request.getSession().setAttribute("userType", type);
            }
            if (type != null && type.equals("''")) {
                type = null;
            }
            TblStaffUtil staff = userProvider.get();
            BigDecimal orgid = staff.getCurrentOrg().getOrgid();
            PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);

            if (pid != null && pid.length() > 0) {
                resultMap = userService.findByAll(pid, pageInfo);
            } else {
                resultMap = userService.findByAllORGID(orgid, pageInfo);
            }
            resultMap.put("pid", pid);
            resultMap.put("pageBean", pageBean);
            resultMap.put("type", type);
            resultMap.put("pageNames", pageNames);
            resultMap.put("selectM", request.getParameter("selectM"));
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }


    @OperationLog(
            success = "查询列表",
            busType = "权限管理",
            fail = "查询列表",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/list", method = {RequestMethod.POST})
    @Operation(summary="用户管理列表-查询")
    public String userList(HttpServletRequest request, Find find,
                           @RequestParam(value = "staffid", required = false) String staffid,
                           @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "pid", required = false) BigDecimal pid,
                           @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
        @RequestParam(value = "isAll", required = false,defaultValue="0") @Parameter(name = "isAll", description = "是否开启全集团查询0-否，1-是，默认0", required = false)Integer isAll) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            if (pid == null || isAll == 1) {
            	//查询所有公司下的用户
                resultMap = userService.findAllPageBean(find, pageNumber, pageSize, token, staffid);
            } else {
            	//只查询选中公司下的用户
                resultMap = userService.findAllPageBeanPid(staffid, find, pageNumber, pageSize, pid);
            }

            JSONObject jsonObject = new JSONObject(resultMap);
            result = jsonObject.toString();
            return result;
    }


    @OperationLog(
            success = "部门负责人列表",
            busType = "权限管理",
            fail = "部门负责人列表",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/user/bmfzr_list", method = {RequestMethod.POST})
    @Operation(summary="部门负责人列表")
    public String bmfzr_list(HttpServletRequest request,
                           @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        	String result = null;
        	Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap = userService.findAllBmfzrPageBean(token, pageNumber, pageSize);

            JSONObject jsonObject = new JSONObject(resultMap);
            result = jsonObject.toString();
            return result;
    }


    @OperationLog(
            success = "分管领导列表",
            busType = "权限管理",
            fail = "分管领导列表",
            operationType = OperationType.SELECT,
            subType = "权限管理"
    )
    @RequestMapping(value = "/user/fgld_list", method = {RequestMethod.POST})
    @Operation(summary="分管领导列表")
    public String fgld_list(HttpServletRequest request,
                           @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap = userService.findAllFgldPageBean(token, pageNumber, pageSize);
            JSONObject jsonObject = new JSONObject(resultMap);
            result = jsonObject.toString();
            return result;
    }

    @OperationLog(
            success = "新增页面",
            busType = "权限管理",
            fail = "新增页面",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/new", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-新增页面访问接口获取信息")
    public String userNew(HttpServletRequest request, @Parameter(name = "pid", required = false) BigDecimal pid,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
            TblStaffUtil staff = userProvider.get();
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            //获取当前公司所有的角色
            List<TblRole> roles = tblRoleService.findAll(staff.getCurrentOrg().getOrgid());
            //获取当前公司所有的岗位
            List<TblJob> jobs = tblJobService.findAll(staff.getCurrentOrg().getOrgid());
            resultMap.put("pid", pid);
            resultMap.put("jobs", jobs);
            resultMap.put("roles", roles);
            JSONObject jsonCompanyId = new JSONObject(resultMap);
            result = jsonCompanyId.toString();
            return result;
    }


    @OperationLog(
            success = "新建前查询用户名",
            busType = "权限管理",
            fail = "新建前查询用户名",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/isuserName", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "用户权限管理-用户管理-新建前查询用户名")
    public @ResponseBody
    String ruleUpdateStatus(HttpServletRequest request,
                            @Parameter(name = "username", required = false) String username) {
            if (username != null && username != "") {
                TblStaff user = tblStaffService.findByUsername(username);
                if (user != null) {
                    return JsonBean.error("用户名已存在");
                } else {
                    return JsonBean.success();
                }
            } else {
                return JsonBean.error("请输入用户名称");
            }
    }

    @OperationLog(
            success = "新建/修改时查询邮箱是否存在",
            busType = "权限管理",
            fail = "新建/修改时查询邮箱是否存在",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/iseamil", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-新建/修改时查询邮箱是否存在")
    public @ResponseBody
    String iseamil(HttpServletRequest request, @Parameter(name = "email", required = false) String email) {
            if (email != null && email != "") {
                List<TblStaff> list = tblStaffService.findByEmail(email);
                if (list != null && list.size() > 0) {
                    return JsonBean.error("邮箱已存在");
                } else {
                    return JsonBean.success();
                }
            } else {
                return JsonBean.error("请输入邮箱");
            }
    }


    @OperationLog(
            success = "新建",
            busType = "权限管理",
            fail = "新建",
            operationType = OperationType.ADD,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/save", method = {RequestMethod.POST})
    @Operation(summary = "用户权限管理-用户管理-新建")
    public Map<String, Object> userSave(HttpServletRequest request, TblStaff tblStaff,
    		@Parameter(name = "deptIdStrs", description = "用户选择的部门信息Ids，用,号拼接", required = true)@RequestParam(value = "deptIdStrs", required = true) String deptIdStrs,
    		@Parameter(name="iscanpre",description="是否可以操作之前授权角色用户的数据，1-是，0否 默认1，对应下标与roleIdStrs一致",required=false) @RequestParam(value="iscanpre",required=false)String iscanpre,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token)  throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
    	String memo = "";


    	TblStaffUtil loginStaff = userProvider.get();
    	if(loginStaff == null) {
    		resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            return resultMap;
    	}

    	Integer count = this.tblStaffService.selectUniqueCountUserName(tblStaff.getUsername(),tblStaff.getStaffid());
    	if(count > 0) {
    		resultMap.put("code", "0");
            resultMap.put("msg", "账号重复！");
            return resultMap;
        }

    	TblAuthorizationRecord confirm = null;
        String result = null;
        List<TblUserOrgRelation> relaList = null;

            String[] deptIds = deptIdStrs.split(",");
      	   	tblStaff.setOrgid(new BigDecimal(deptIds[0]));
      	   	//获取用户的兼职部门信息集合
            relaList = this.tblUserOrgRelationService.dealInsertRealtionInfo(loginStaff,deptIds);
            TblStaff preStaff = null;
            String preRoleStrs = "";
            if (tblStaff.getStaffid() != null) {
            	preStaff = this.tblStaffService.findById(tblStaff.getStaffid().toString());
            	//preRoleStrs = preStaff.getRoleIdStrs();*/


            	if(SystemStaticValue.REQUIREMENTVALIDATE) {
            		//判断是否有正在审批的数据
                	confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(tblStaff.getStaffid().toString());

                	if(!preStaff.getRealname().equals(tblStaff.getRealname())) {
                		memo += "修改真实姓名："+preStaff.getRealname()+"——>"+tblStaff.getRealname()+"；";
                	}
                	if(!preStaff.getUsername().equals(tblStaff.getUsername())) {
                		memo += "修改用户名："+preStaff.getUsername()+"——>"+tblStaff.getUsername()+"；";
                	}
                	if(preStaff.getStatus()  != tblStaff.getStatus()) {
                		if(tblStaff.getStatus() == 0) {
                			memo += "修改用户状态为弃用。";
                		}else {
                			memo += "修改用户状态为启用。";
                		}
                	}

                	tblStaff.setRelaList(relaList);
                	this.tblStaffService.dealConfirmRelationInfo(tblStaff);

                	if(confirm != null) {
                		//修改审批中的确认数据
                		confirm.setModifiedTime(new Date());
                		confirm.setModifier(tblStaff.getStaffid());
                		confirm.setModifyerName(tblStaff.getRealname());
                		confirm.setOperationData(JSONObject.toJSONString(tblStaff));
                		confirm.setOperationMemo("修改用户信息，"+memo);
                		confirm.setRecordText("修改用户"+preStaff.getOrgname());
                		this.tblAuthorizationRecordService.modifyEntity(confirm);
                	}else {
                		//新增确认记录需要发起流程
                		confirm = new TblAuthorizationRecord();
                		confirm.setRecordId(RandomUtil.uuStringId());
                		confirm.setCreationTime(new Date());
                		confirm.setCreator(tblStaff.getStaffid());
                		confirm.setCreatorName(tblStaff.getRealname());
                   	 	confirm.setOperationData(JSONObject.toJSONString(tblStaff));
                   	 	confirm.setOperationMemo("修改用户信息，"+memo);
                   	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
                   	 	confirm.setStatus(0);
                   	 	confirm.setTargetId(tblStaff.getStaffid().toString());
                   	 	confirm.setRecordText("修改用户"+preStaff.getOrgname());
                   	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEUSER);
                   	 	this.tblAuthorizationRecordService.addEntity(confirm);
                	}

            	}else {
            		tblStaffService.update(tblStaff);
            		this.tblUserOrgRelationService.removeRelationByStaffId(tblStaff.getStaffid());
                    this.tblUserOrgRelationService.InsertRealtionByStaffId(tblStaff.getStaffid(),relaList);
        	        if(YMUrlStatic.status == 0) {
        	        	this.ymBusinessService.dealUniqueStaffInfo(tblStaff.getStaffid(),relaList);
        	        }
            	}
            	resultMap.put("data", confirm);
                resultMap.put("code", "1");
                resultMap.put("msg", "修改成功");
            } else {
            	tblStaff.setStaffid(RandomUtil.uuBigDecimalId());
            	if(SystemStaticValue.REQUIREMENTVALIDATE) {
            		//需要插入确认表
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(loginStaff.getStaffid());
					confirm.setCreatorName(loginStaff.getRealname());

					tblStaff.setRelaList(relaList);
					this.tblStaffService.dealConfirmRelationInfo(tblStaff);
					confirm.setOperationData(JSONObject.toJSONString(tblStaff));
					if(tblStaff.getStatus() == 1) {
						 memo += "用户状态为启用。";
					}else {
						 memo += "用户状态为弃用。";
					}

					confirm.setOperationMemo("新增用户信息，用户名："+tblStaff.getUsername()+"、真实姓名："+tblStaff.getRealname()+memo);
					confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
					confirm.setStatus(0);
					confirm.setTargetId(tblStaff.getStaffid().toString());
					confirm.setRecordText("新增用户"+tblStaff.getRealname());
					confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEUSER);
					this.tblAuthorizationRecordService.addEntity(confirm);
					resultMap.put("data", confirm);
            	}else {
            		tblStaffService.add(tblStaff);
            		this.tblUserOrgRelationService.removeRelationByStaffId(tblStaff.getStaffid());
                    this.tblUserOrgRelationService.InsertRealtionByStaffId(tblStaff.getStaffid(),relaList);
        	        if(YMUrlStatic.status == 0) {
        	        	this.ymBusinessService.dealUniqueStaffInfo(tblStaff.getStaffid(),relaList);
        	        }
            	}
            	resultMap.put("data", confirm);
                resultMap.put("code", "1");
                resultMap.put("msg", "新增成功");
            }
            //this.tblUserRolerelationService.dealUserRoleRelation(staffId,tblStaff.getRoleIdStrs(),iscanpre,loginStaff,preRoleStrs);

            return resultMap;
    }

    @OperationLog(
            success = "修改",
            busType = "权限管理",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/modify", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-修改")
    public String userModify(HttpServletRequest request, @Parameter(name = "pid", required = false) String pid) {
        String result = null;

            String id = request.getParameter("selectedUser");
            if (id != null && !id.equals("")) {
                TblStaff user = tblStaffService.findById(pid);
                Map<String, Object> resultMap = new HashMap<String, Object>(0);
                resultMap.put("user", user);
                TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
                List<TblRole> roles = tblRoleService.findAll(organization.getOrgid());
                List<TblJob> findAll = tblJobService.findAll(organization.getOrgid());
                resultMap.put("roles", roles);
                resultMap.put("jobs", findAll);
                resultMap.put("pid", pid);

                JSONObject jsonChoiceSearch = new JSONObject(resultMap);
                result = jsonChoiceSearch.toString();
            }
            return result;
    }

    @OperationLog(
            success = "重置密码",
            busType = "权限管理",
            fail = "重置密码",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/pmodify", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-重置密码")
    public String userPModify(HttpServletRequest request, String staffid) {
        String result = null;
        //String id = request.getParameter("selectedUser");
            if (staffid != null && !staffid.equals("")) {
                //UserService srvc = (UserService) SpringContextHolder.getBean("UserService");
                TblStaff user = tblStaffService.findById(staffid);
                Map<String, Object> resultMap = new HashMap<String, Object>(0);
                resultMap.put("user", user);
                JSONObject jsonchoiceSerach = new JSONObject(resultMap);
                result = jsonchoiceSerach.toString();
            }
            return result;
    }


    @OperationLog(
            success = "重置密码保存",
            busType = "权限管理",
            fail = "重置密码保存",
            operationType = OperationType.ADD,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/psave", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-重置密码保存")
    public String userPsave(HttpServletRequest request, String id,
                            String password, String password1) throws Exception {
        String result = null;
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
    	TblStaffUtil loginStaff = userProvider.get();
    	if(loginStaff == null) {
    		resultMap.put("code", "0");
            resultMap.put("msg", "用户已失效！");
            JSONObject jsonObj = new JSONObject(resultMap);
            return jsonObj.toString();
    	}

            if (id != null && !id.equals("")) {
                TblStaff user = tblStaffService.findById(id);
                if (password.equals(password1)) {
                    user.setPassword(MD5Encrypt.md5WithEncoding(password,"UTF-8"));

                    if(SystemStaticValue.REQUIREMENTVALIDATE) {
                    	TblAuthorizationRecord confirm = new TblAuthorizationRecord();
    					confirm.setRecordId(RandomUtil.uuStringId());
    					confirm.setCreationTime(new Date());
    					confirm.setCreator(loginStaff.getStaffid());
    					confirm.setCreatorName(loginStaff.getRealname());
    					confirm.setOperationData(JSONObject.toJSONString(user));
    					confirm.setOperationMemo("重置用户密码，用户名："+user.getUsername());
    					confirm.setOperationType(TblAuthorizationRecord.OPERATIONRESETPWD);
    					confirm.setStatus(0);
    					confirm.setTargetId(user.getStaffid().toString());
    					confirm.setRecordText("重置用户密码"+user.getRealname());
    					confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEUSER);
    					this.tblAuthorizationRecordService.addEntity(confirm);
    					resultMap.put("data", confirm);
                    }else {
                    	tblStaffService.modify(user);
                    	resultMap.put("data", user);
                    }
                    resultMap.put("code", "1");
            		resultMap.put("msg", "成功！");
                }else {
                	resultMap.put("code", "0");
            		resultMap.put("msg", "两次密码不一致，修改失败！");
            		resultMap.put("data", user);
                }
            }else {
        		resultMap.put("code", "0");
        		resultMap.put("msg", "失败！");
        		resultMap.put("data", null);

            }
            result = JSONObject.toJSONString(resultMap);
            return result;
    }


    @OperationLog(
            success = "重置密码",
            busType = "权限管理",
            fail = "重置密码",
            operationType = OperationType.UPDATE,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/xgpassword",method = {RequestMethod.POST},produces = "application/html; charset=utf-8")
	@Operation(summary="用户权限管理-用户管理-重置密码")
	public String approval_processhtjy(@Parameter(description="id",required=true)String id,
									   @Parameter(description="password",required=true)String password,
									   @Parameter(description="password1",required=true)String password1) throws Exception {
		String result = null;
		Map<String, Object> resultMap = new HashMap<String, Object>(0);
		if (id != null && !id.equals("")) {
			 TblStaff user = tblStaffService.findById(id);
            if (password.equals(password1)) {
                user.setPassword(MD5Encrypt.md5WithEncoding(password,"UTF-8"));
                tblStaffService.modify(user);
            }
    		resultMap.put("code", "1");
    		resultMap.put("msg", "成功！");
    		resultMap.put("data", user);
    		JSONObject jsonObj = new JSONObject(resultMap);
    		result = jsonObj.toString();
        }else {
    		resultMap.put("code", "0");
    		resultMap.put("msg", "失败！");
    		resultMap.put("data", null);
    		JSONObject jsonObj = new JSONObject(resultMap);
    		result = jsonObj.toString();
        }

		return result;
	}


    @OperationLog(
            success = "列表",
            busType = "权限管理",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/role_list", method = {RequestMethod.POST})
    @Operation(summary="角色管理-列表")
    public String roleList(HttpServletRequest request, TblRole role,
                           @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                           @Parameter(name = "orgIds", description = "公司主键拼接ID用,号分割", required = false)@RequestParam(value = "orgIds", required = false) String orgIds,
                           @Parameter(name = "roleName", description = "筛选条件-角色名称", required = false)@RequestParam(value = "roleName", required = false) String roleName,
                           @Parameter(name = "orgName", description = "筛选条件-公司名称", required = false)@RequestParam(value = "orgName", required = false) String orgName,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap = tblRoleService.findTblRoleAll(role, pageNumber, pageSize, token,orgIds,roleName,orgName);
            JSONObject jsonObjectmv = new JSONObject(resultMap);
            result = jsonObjectmv.toString();
            return result;
    }


    @OperationLog(
            success = "详情",
            busType = "权限管理",
            fail = "详情",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @OperationLog(
            success = "角色详情【{{#roleId}}】",
            busType = "角色管理",
            fail = "角色详情【{{#roleId}}】",
            operationType = OperationType.SELECT,
            subType = "查看角色详情"
    )
    @RequestMapping(value = "/role/role_Detail", method = {RequestMethod.GET})
    @Operation(summary="角色管理-详情")
    public String roleDetail(HttpServletRequest request,
                           @Parameter(name = "roleId", description = "角色主键", required = false)@RequestParam(value = "roleId", required = false) BigDecimal roleId,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            resultMap = tblRoleService.roleDetail(token,roleId);
            JSONObject jsonObjectmv = new JSONObject(resultMap);
            result = jsonObjectmv.toString();
            return result;
    }


    @OperationLog(
            success = "公司授权",
            busType = "权限管理",
            fail = "公司授权",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/rqorg",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-公司授权")
	public JsonBean rqorg(HttpServletRequest request,
											@Parameter(name="orgids",description="所选组织拼接ID",required=true) @RequestParam(value="orgids",required=true)String orgids,
											@Parameter(name="roleid",description="所选角色ID",required=true) @RequestParam(value="roleid",required=true)String roleid,
											 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			TblAuthorizationRecord confirm = null;
			 if(SystemStaticValue.REQUIREMENTVALIDATE) {
	            	//判断是否有正在审批的数据
				confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleid);
				List<String> nameList = this.tblOrganizaService.findNameByOrgIds(orgids);
				String names = String.join(",", nameList);
				TblRole role = this.tblRoleService.findByid(roleid);
				JSONObject dataJson = new JSONObject();
				dataJson.put("rname", role.getRname());
				dataJson.put("orgids", orgids);
				dataJson.put("orgnames", names);
				if(confirm != null) {
					//修改审批中的确认数据
					confirm.setModifiedTime(new Date());
					confirm.setModifier(staff.getStaffid());
					confirm.setModifyerName(staff.getRealname());
					confirm.setOperationData(dataJson.toString());
					confirm.setOperationMemo(role.getRname()+"角色授权以下公司："+names);
					confirm.setRecordText(role.getRname()+"角色公司授权");
					this.tblAuthorizationRecordService.modifyEntity(confirm);
				}else {
					//新增确认记录需要发起流程
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(staff.getStaffid());
					confirm.setCreatorName(staff.getRealname());
				 	confirm.setOperationData(dataJson.toString());
				 	confirm.setOperationMemo(role.getRname()+"角色授权给以下公司："+names);
				 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONGRANTORG);
				 	confirm.setStatus(0);
				 	confirm.setTargetId(roleid);
				 	confirm.setRecordText(role.getRname()+"角色公司授权");
				 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
				 	this.tblAuthorizationRecordService.addEntity(confirm);
				}
				return ResponseFormat.retParam(1,200,confirm);
			 }else{
				//tblRoleService.delOrgandRoleid(roleid);
				this.tblRoleService.grantToOrg(orgids,roleid);
				return ResponseFormat.retParam(1,200,null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"角色授权失败",null);
		}
	}


    @OperationLog(
            success = "授权公司列表",
            busType = "权限管理",
            fail = "授权公司列表",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/getroleOrgList",method = {RequestMethod.GET},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-授权公司列表")
	public JsonBean getroleOrgList(HttpServletRequest request,
			@Parameter(name="pageNumber",description="当前页",required=false)@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
			@Parameter(name="pageSize",description="分页数",required=false)@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
			@Parameter(name="roleid",description="所选角色ID",required=true) @RequestParam(value="roleid",required=true)String roleid,
			@Parameter(name="orgname",description="公司名称，查询条件",required=false) @RequestParam(value="orgname",required=false)String orgname,
			@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			resultMap= tblOrganizaService.selectDeptListByroleid(roleid, pageNumber, pageSize, orgname);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"根据角色查询授权公司失败",null);
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}



    @OperationLog(
            success = "取消公司授权",
            busType = "权限管理",
            fail = "取消公司授权",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/delrqorg",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-取消公司授权")
	public JsonBean delrqorg(HttpServletRequest request,
											@Parameter(name="orgids",description="所选组织拼接ID",required=true) @RequestParam(value="orgids",required=true)String orgids,
											@Parameter(name="roleid",description="所选角色ID",required=true) @RequestParam(value="roleid",required=true)String roleid,
											 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			TblAuthorizationRecord confirm = null;
			 if(SystemStaticValue.REQUIREMENTVALIDATE) {
	            	//判断是否有正在审批的数据
				confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleid);
				List<String> nameList = this.tblOrganizaService.findNameByOrgIds(orgids);
				String names = String.join(",", nameList);
				TblRole role = this.tblRoleService.findByid(roleid);
				JSONObject dataJson = new JSONObject();
				dataJson.put("rname", role.getRname());
				dataJson.put("orgids", orgids);
				dataJson.put("orgnames", names);
				if(confirm != null) {
					//修改审批中的确认数据
					confirm.setModifiedTime(new Date());
					confirm.setModifier(staff.getStaffid());
					confirm.setModifyerName(staff.getRealname());
					confirm.setOperationData(dataJson.toString());
					confirm.setOperationMemo(role.getRname()+"角色取消授权给以下公司："+names);
					confirm.setRecordText(role.getRname()+"角色取消公司授权");
					this.tblAuthorizationRecordService.modifyEntity(confirm);
				}else {
					//新增确认记录需要发起流程
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(staff.getStaffid());
					confirm.setCreatorName(staff.getRealname());
				 	confirm.setOperationData(dataJson.toString());
				 	confirm.setOperationMemo(role.getRname()+"角色取消授权给以下公司："+names);
				 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONUNORG);
				 	confirm.setStatus(0);
				 	confirm.setTargetId(roleid);
				 	confirm.setRecordText(role.getRname()+"角色取消公司授权");
				 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
				 	this.tblAuthorizationRecordService.addEntity(confirm);
				}
				return ResponseFormat.retParam(1,200,confirm);
			 }else{
				//tblRoleService.delOrgandRoleid(roleid);
				this.tblRoleService.unGrantFromOrg(orgids, roleid);
				return ResponseFormat.retParam(1,200,null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"角色取消公司授权失败",null);
		}
	}

    @OperationLog(
            success = "用户授权",
            busType = "权限管理",
            fail = "用户授权",
            operationType = OperationType.UPDATE,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/rquser",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-用户授权")
	public JsonBean rquser(HttpServletRequest request,
											@Parameter(name="staffids",description="所选用户拼接ID",required=true) @RequestParam(value="staffids",required=true)String staffids,
											@Parameter(name="roleid",description="所选角色ID",required=true) @RequestParam(value="roleid",required=true)String roleid,
											@Parameter(name="iscanpre",description="是否可以操作之前授权角色用户的数据，1-是，0否 默认1，对应下标与roleid一致",required=false) @RequestParam(value="iscanpre",required=false,defaultValue = "1")String iscanpre,
											 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			 TblAuthorizationRecord confirm = null;
			 if(SystemStaticValue.REQUIREMENTVALIDATE) {
	            	//判断是否有正在审批的数据
				confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleid);
				List<String> realNameList = this.tblStaffService.selectRealNameListByStaffIds(staffids);
				String realnames = String.join(",", realNameList);
				TblRole role = this.tblRoleService.findByid(roleid);
				JSONObject dataJson = new JSONObject();
				dataJson.put("rname", role.getRname());
				dataJson.put("staffids", staffids);
				dataJson.put("realnames", realnames);
				if(confirm != null) {
					//修改审批中的确认数据
					confirm.setModifiedTime(new Date());
					confirm.setModifier(staff.getStaffid());
					confirm.setModifyerName(staff.getRealname());
					confirm.setOperationData(dataJson.toString());
					confirm.setOperationMemo(role.getRname()+"角色授权给以下用户："+realnames);
					confirm.setRecordText(role.getRname()+"角色用户授权");
					this.tblAuthorizationRecordService.modifyEntity(confirm);
				}else {
					//新增确认记录需要发起流程
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(staff.getStaffid());
					confirm.setCreatorName(staff.getRealname());
				 	confirm.setOperationData(dataJson.toString());
				 	confirm.setOperationMemo(role.getRname()+"角色授权给以下用户："+realnames);
				 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONGRANTUSER);
				 	confirm.setStatus(0);
				 	confirm.setTargetId(roleid);
				 	confirm.setRecordText(role.getRname()+"角色用户授权");
				 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
				 	this.tblAuthorizationRecordService.addEntity(confirm);
				}
				return ResponseFormat.retParam(1,200,confirm);
			 }else{
				this.tblRoleService.grantToUser(staffids,roleid);
				return ResponseFormat.retParam(1,200,null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"角色用户授权失败",null);
		}

	}


    @OperationLog(
            success = "授权用户列表",
            busType = "权限管理",
            fail = "授权用户列表",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/getroleUserList",method = {RequestMethod.GET},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-授权用户列表")
	public JsonBean getroleUserList(HttpServletRequest request,
											@Parameter(name="roleid",description="所选角色ID",required=false) @RequestParam(value="roleid",required=false)String roleid,
											@Parameter(name="pageNumber",description="当前页",required=false)@RequestParam(value = "pageNumber",required = false,defaultValue = "1")Integer pageNumber,
											@Parameter(name="pageSize",description="分页数",required=false)@RequestParam(value = "pageSize",required = false,defaultValue = "20")Integer pageSize,
											@Parameter(name="username",description="用户名",required=false) @RequestParam(value="username",required=false)String username,
											@Parameter(name="realname",description="真实名称",required=false) @RequestParam(value="realname",required=false)String realname,
											 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		Map<String, Object> resultMap = new HashMap<String, Object>();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			resultMap = tblStaffService.selectAllListByroleid(roleid, username, realname, pageNumber, pageSize);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"根据角色查询授权用户失败",null);
		}
		return ResponseFormat.retParam(1,200,resultMap);
	}


    @OperationLog(
            success = "取消用户授权",
            busType = "权限管理",
            fail = "取消用户授权",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
	@RequestMapping(value = "/role/qxuser",method = {RequestMethod.POST},produces = "application/json; charset=utf-8")
	@Operation(summary="角色管理-取消用户授权")
	public JsonBean qxuser(HttpServletRequest request,
											@Parameter(name="staffids",description="所选用户拼接ID",required=true) @RequestParam(value="staffids",required=true)String staffids,
											@Parameter(name="roleid",description="所选角色ID",required=true) @RequestParam(value="roleid",required=true)String roleid,
											 @Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token") String token) throws Exception {
		TblStaffUtil staff = userProvider.get();
		if(staff == null) {
			return ResponseFormat.retParam(0,20006,null);
		}
		try {
			TblAuthorizationRecord confirm = null;
			 if(SystemStaticValue.REQUIREMENTVALIDATE) {
	            	//判断是否有正在审批的数据
				confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(roleid);
				List<String> realNameList = this.tblStaffService.selectRealNameListByStaffIds(staffids);
				String realnames = String.join(",", realNameList);
				TblRole role = this.tblRoleService.findByid(roleid);
				JSONObject dataJson = new JSONObject();
				dataJson.put("rname", role.getRname());
				dataJson.put("staffids", staffids);
				dataJson.put("realnames", realnames);
				if(confirm != null) {
					//修改审批中的确认数据
					confirm.setModifiedTime(new Date());
					confirm.setModifier(staff.getStaffid());
					confirm.setModifyerName(staff.getRealname());
					confirm.setOperationData(dataJson.toString());
					confirm.setOperationMemo(role.getRname()+"角色从以下用户中移除："+realnames);
					confirm.setRecordText(role.getRname()+"角色取消用户授权");
					this.tblAuthorizationRecordService.modifyEntity(confirm);
				}else {
					//新增确认记录需要发起流程
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(staff.getStaffid());
					confirm.setCreatorName(staff.getRealname());
				 	confirm.setOperationData(dataJson.toString());
				 	confirm.setOperationMemo(role.getRname()+"角色从以下用户中移除："+realnames);
				 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONUNUSER);
				 	confirm.setStatus(0);
				 	confirm.setTargetId(roleid);
				 	confirm.setRecordText(role.getRname()+"角色取消用户授权");
				 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEGRANT);
				 	this.tblAuthorizationRecordService.addEntity(confirm);
				}
				return ResponseFormat.retParam(1,200,confirm);
			 }else{
				this.tblRoleService.unGrantFromUser(staffids,roleid);
				return ResponseFormat.retParam(1,200,null);
			 }
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"角色取消用户授权失败",null);
		}


	}


    @OperationLog(
            success = "修改",
            busType = "权限管理",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/modify_save", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-角色管理-修改")
    public String role_modify(HttpServletRequest request, TblRole role) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            TblRole newrole = tblRoleService.findByid(role.getRid().toString());
            newrole.setRdesc(role.getRdesc());
            newrole.setRname(role.getRname());
            newrole.setRstatus(role.getRstatus());
            newrole.setCompanyid(role.getCompanyid());
            tblRoleService.update(newrole);
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();

            return result;
    }


    @OperationLog(
            success = "修改",
            busType = "权限管理",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "岗位管理"
    )
    @RequestMapping(value = "/job/modify_save", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-岗位管理-修改")
    public String job_modify(HttpServletRequest request, TblJob job) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            TblJob newJob = tblJobService.findByJobId(job.getJobid().toString());
            newJob.setJobname(job.getJobname());
            newJob.setCompanyId(job.getCompanyId());
            tblJobService.updateJob(newJob);
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();

            return result;
    }

    @OperationLog(
            success = "保存",
            busType = "权限管理",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/save", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-角色管理-保存")
    public String roleSave(HttpServletRequest request, TblRole role,
    		@Parameter(name="orgids",description="所选组织拼接ID",required=false) @RequestParam(value="orgids",required=false)String orgids,
    		@Parameter(name="targetid",description="操作主键",required=false) @RequestParam(value="targetid",required=false)String targetid,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        JSONObject jsonObj = null;

        TblStaffUtil staff = userProvider.get();
        if (staff == null) {
        	 resultMap.put("code", "0");
             resultMap.put("msg", "用户已失效");
             jsonObj = new JSONObject(resultMap);
             result = jsonObj.toString();
		}
        BigDecimal pid = staff.getCurrentOrg().getOrgid();
        BigDecimal rid = null;
        Integer count = 0;

        count = this.tblRoleService.selectReaprtRoleName(role.getRname(),role.getRid(),pid);
        if(count > 0 ) {
          	resultMap.put("code", "1");
            resultMap.put("msg", "角色名称重复");
            jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
       }
        TblAuthorizationRecord confirm = null;
        TblRole tr = new TblRole();
        if (role.getRid() != null) {
        	rid = role.getRid();
        	if(SystemStaticValue.REQUIREMENTVALIDATE) {
        		confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(rid.toString());
        		tr = this.tblRoleService.findByid(role.getRid().toString());
        		String memo = "";

        		if(StringUtils.isNotBlank(role.getRname()) && !role.getRname().equals(tr.getRname())) {
            		memo += "修改角色名称："+tr.getRname()+"——>"+role.getRname()+"；";
            	}
        		if(StringUtils.isNotBlank(role.getRdesc()) && !role.getRname().equals(tr.getRdesc())) {
            		memo += "修改角色描述："+tr.getRdesc()+"——>"+role.getRdesc()+"；";
            	}
        		if(StringUtils.isNotBlank(role.getRstatus()) && !role.getRstatus().equals(tr.getRstatus())) {
        			if("1".equals(role.getRstatus())) {
        				memo += "修改角色状态为启用；";
        			}else {
        				memo += "修改角色状态为禁用；";
        			}
            	}

        		if(confirm != null) {
            		//修改审批中的确认数据
        			confirm.setRecordText("修改角色："+tr.getRname());
            		confirm.setOperationData(JSONObject.toJSONString(role));
        			confirm.setOperationMemo(memo);
            		confirm.setModifier(staff.getStaffid());
            		confirm.setModifyerName(staff.getRealname());
            		confirm.setOperationMemo("修改公司信息，"+memo);
            		this.tblAuthorizationRecordService.modifyEntity(confirm);
            	}else {
            		//新增确认记录需要发起流程
            		confirm = new TblAuthorizationRecord();
            		confirm.setRecordId(RandomUtil.uuStringId());
            		confirm.setRecordText("修改角色："+tr.getRname());
            		confirm.setOperationMemo(memo);
            		confirm.setOperationData(JSONObject.toJSONString(role));
            		confirm.setCreationTime(new Date());
            		confirm.setCreator(staff.getStaffid());
            		confirm.setCreatorName(staff.getRealname());
               	 	confirm.setOperationType(TblAuthorizationRecord.OPERATIONMODIFY);
               	 	confirm.setStatus(0);
               	 	confirm.setTargetId(role.getRid().toString());
               	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEROLE);
               	 	this.tblAuthorizationRecordService.addEntity(confirm);
            	}
				this.tblAuthorizationRecordService.modifyEntity(confirm);
        	}else {
        		tblRoleService.update(role);
        		if(YMUrlStatic.status == 0 && !TblRole.ADMINAME.equals(role.getRname())){
        			this.ymBusinessService.dealUniqueRoleInfo(rid);
        		}
        	}
		} else {
			if(SystemStaticValue.REQUIREMENTVALIDATE) {
				if(StringUtils.isNotBlank(targetid)) {
					confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(targetid);
					role.setRid(new BigDecimal(targetid));
				}else {
					role.setRid(RandomUtil.uuBigDecimalId());
				}

				role.setCompanyid(pid);

				if(confirm != null) {
					//修改审批中的确认数据
            		confirm.setOperationData(JSONObject.toJSONString(role));
            		confirm.setModifier(staff.getStaffid());
            		confirm.setModifyerName(staff.getRealname());
            		confirm.setOperationMemo("新增信息：角色编号："+role.getRid()+"，角色名称："+role.getRname()+"，角色状态"+("1".equals(role.getRstatus())?"启用":"禁用")+"，角色描述："+role.getRdesc());
					confirm.setRecordText("新增角色："+role.getRname());
            		this.tblAuthorizationRecordService.modifyEntity(confirm);
				}else {
					//新增确认记录需要发起流程
					confirm = new TblAuthorizationRecord();
					confirm.setRecordId(RandomUtil.uuStringId());
					confirm.setCreationTime(new Date());
					confirm.setCreator(staff.getStaffid());
					confirm.setCreatorName(staff.getRealname());
				 	confirm.setOperationData(JSONObject.toJSONString(role));
				 	confirm.setStatus(0);
				 	confirm.setTargetId(role.getRid().toString());
				 	confirm.setOperationMemo("新增信息：角色编号："+role.getRid()+"，角色名称："+role.getRname()+"，角色状态"+("1".equals(role.getRstatus())?"启用":"禁用")+"，角色描述："+role.getRdesc());
					confirm.setRecordText("新增角色："+role.getRname());
					confirm.setOperationType(TblAuthorizationRecord.OPERATIONINSERT);
				 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEROLE);
				 	this.tblAuthorizationRecordService.addEntity(confirm);
				}
			}else {
				tr.setRname(role.getRname());
			    tr.setRdesc(role.getRdesc());
			    tr.setRstatus(role.getRstatus());
			    tr.setCompanyid(pid);
			    tr.setRid(RandomUtil.uuBigDecimalId());
			    tblRoleService.save(tr);
			    rid = tr.getRid();
			    if(YMUrlStatic.status == 0 && !TblRole.ADMINAME.equals(role.getRname())){
					this.ymBusinessService.dealUniqueRoleInfo(rid);
				}
			}
		}
		/*if(StringUtils.isNotBlank(orgids)) {
			String[] orgidlist=orgids.split(",");
			TblOrganization orgInfo = null;
			BigDecimal rootId = null;
			for (String orgid : orgidlist) {
				//获取部门的公司节点
				orgInfo = this.tblOrganizaService.findByid(orgid);
				if(orgInfo.getOrgtype() == 0) {
					orgInfo = this.tblOrganizaService.findCompanyInfoByDeptId(orgInfo.getOrgid());
				}
				rootId = orgInfo.getOrgid();

				tblRoleService.inertOrgandRole(orgid, rid.toString(),rootId);
			}
		}*/

            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            resultMap.put("confirm", confirm);
            jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();

        return result;
    }


    @OperationLog(
            success = "修改",
            busType = "权限管理",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/role_modify", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-角色管理-修改")
    public String role_modify(HttpServletRequest request) {
        String result = null;
            Map<String, Object> resultMap = new HashMap<String, Object>();
            String selectedId = request.getParameter("selectedId");
            if (selectedId != null && selectedId.trim().length() > 0) {
                TblRole role = tblRoleService.findByid(selectedId);
                resultMap.put("role", role);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonChoiceSearch2 = new JSONObject(resultMap);
            result = jsonChoiceSearch2.toString();
            return result;
    }

    @OperationLog(
            success = "判断角色是否使用",
            busType = "权限管理",
            fail = "判断角色是否使用",
            operationType = OperationType.SELECT,
            subType = "角色管理"
    )
    @Operation(summary="用户权限管理-角色管理-角色管理-判断角色是否使用")
    @RequestMapping(value = "/role/is_sy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String rolesy(HttpServletRequest request) {
        String rid = request.getParameter("rid");
            List list = tblRoleService.isSY(rid);
            if (list == null || list.size() <= 0) {
                return JsonBean.success();
            } else {
                return JsonBean.error("正在使用，无法操作");
            }
    }


    @OperationLog(
            success = "启用/禁用",
            busType = "权限管理",
            fail = "启用/禁用",
            operationType = OperationType.UPDATE,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/modify_rstatus", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-角色管理-启用/禁用")
    public String modify_rstatus(HttpServletRequest request,
                                 String selectedId, String str) throws Exception {
    	TblStaffUtil staff = userProvider.get();
    	if(staff == null) {
    		JsonBean jsonBean = new JsonBean(0, "用户已失效！");
    		return JSON.toJSONString(jsonBean);
    	}


            if (selectedId != null && selectedId.trim().length() > 0) {
                TblRole role = tblRoleService.findByid(selectedId);
                TblAuthorizationRecord confirm = null;
                if (str != null && str.equals("1")) {
                    role.setRstatus(TblRole.DEL_YES);
                } else {
                    role.setRstatus(TblRole.DEL_NO);
                }

                if(SystemStaticValue.REQUIREMENTVALIDATE) {
            		confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(selectedId);

            		if(confirm != null) {
                		//修改审批中的确认数据
            			if("1".equals(role.getRstatus())) {
            				confirm.setRecordText("启用角色");
            				confirm.setOperationMemo("角色："+role.getRname()+"修改为启用。");
            				confirm.setOperationType(TblAuthorizationRecord.OPERATIONENABLE);
            			}else {
            				confirm.setRecordText("禁用角色");
            				confirm.setOperationMemo("角色："+role.getRname()+"修改为禁用。");
            				confirm.setOperationType(TblAuthorizationRecord.OPERATIONDEPRECATED);
            			}
                		confirm.setModifiedTime(new Date());
                		confirm.setModifier(staff.getStaffid());
                		confirm.setModifyerName(staff.getRealname());
                		confirm.setOperationData(JSONObject.toJSONString(role));
                		this.tblAuthorizationRecordService.modifyEntity(confirm);
                	}else {
                		//新增确认记录需要发起流程
                		confirm = new TblAuthorizationRecord();
                		if("1".equals(role.getRstatus())) {
            				confirm.setRecordText("启用角色");
            				confirm.setOperationMemo("角色："+role.getRname()+"修改为启用。");
            				confirm.setOperationType(TblAuthorizationRecord.OPERATIONENABLE);
            			}else {
            				confirm.setRecordText("禁用角色");
            				confirm.setOperationMemo("角色："+role.getRname()+"修改为禁用。");
            				confirm.setOperationType(TblAuthorizationRecord.OPERATIONDEPRECATED);
            			}
                		confirm.setRecordId(RandomUtil.uuStringId());
                		confirm.setCreationTime(new Date());
                		confirm.setCreator(staff.getStaffid());
                		confirm.setCreatorName(staff.getRealname());
                   	 	confirm.setOperationData(JSONObject.toJSONString(role));
                   	 	confirm.setStatus(0);
                   	 	confirm.setTargetId(selectedId);
                   	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEROLE);
                   	 	this.tblAuthorizationRecordService.addEntity(confirm);
                	}
            		JsonBean jsonBean = new JsonBean(1, "成功", confirm);
            		return JSON.toJSONString(jsonBean);
                }else {
                     tblRoleService.update(role);
                }
            }
            return JsonBean.success("成功");
    }


    @OperationLog(
            success = "查询列表",
            busType = "权限管理",
            fail = "查询列表",
            operationType = OperationType.SELECT,
            subType = "岗位管理"
    )
    @RequestMapping(value = "/job/job_list", method = {RequestMethod.POST})
    @Operation(summary="岗位管理查询列表")
    public String jobList(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                          @Parameter(name = "orgIds", description = "公司主键拼接ID用,号分割", required = false)@RequestParam(value = "orgIds", required = false) String orgIds,
                          @Parameter(name = "orgName", description = "筛选条件-公司名称", required = false)@RequestParam(value = "orgName", required = false) String orgName,
                          @Parameter(name = "jobName", description = "筛选条件-岗位名称", required = false)@RequestParam(value = "jobName", required = false) String jobName,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        resultMap = tblJobService.listJob(pageNumber, pageSize, token,orgIds,jobName,orgName);
        JSONObject jsonObjectMv = new JSONObject(resultMap);
        result = jsonObjectMv.toString();
        return result;
    }


    @OperationLog(
            success = "删除",
            busType = "权限管理",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "角色管理"
    )
    @RequestMapping(value = "/role/role_del", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-角色管理-删除")
    public String role_del(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
            @Parameter(name = "rid", required = false) String rid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        TblStaffUtil staff = userProvider.get();
		if (staff == null) {
		    return JsonBean.error("用户已失效！");
		}

            if (rid != null && rid.trim().length() > 0) {
                TblRole role = tblRoleService.findByid(rid);
                List<TblStaff> list = tblRoleService.isSY(rid);
                if (list != null && list.size() > 0) {
                    return JsonBean.error("正在使用，无法操作");
                }

                TblAuthorizationRecord confirm = null;

                if(SystemStaticValue.REQUIREMENTVALIDATE) {
                	confirm = this.tblAuthorizationRecordService.findSpzRecordInfoByTargetId(rid);

            		if(confirm != null) {
                		//修改审批中的确认数据
            			confirm.setRecordText("删除角色");
            			confirm.setOperationMemo("删除角色："+role.getRname()+"的角色信息。");
            			confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
                		confirm.setModifiedTime(new Date());
                		confirm.setModifier(staff.getStaffid());
                		confirm.setModifyerName(staff.getRealname());
                		confirm.setOperationData(JSONObject.toJSONString(role));
                		this.tblAuthorizationRecordService.modifyEntity(confirm);
                	}else {
                		//新增确认记录需要发起流程
                		confirm = new TblAuthorizationRecord();
                		confirm.setRecordText("删除角色");
            			confirm.setOperationMemo("删除角色："+role.getRname()+"的角色信息。");
            			confirm.setOperationType(TblAuthorizationRecord.OPERATIONREMOVE);
                		confirm.setRecordId(RandomUtil.uuStringId());
                		confirm.setCreationTime(new Date());
                		confirm.setCreator(staff.getStaffid());
                		confirm.setCreatorName(staff.getRealname());
                   	 	confirm.setOperationData(JSONObject.toJSONString(role));
                   	 	confirm.setStatus(0);
                   	 	confirm.setTargetId(rid);
                   	 	confirm.setTargetType(TblAuthorizationRecord.TARGETTYPEROLE);
                   	 	this.tblAuthorizationRecordService.addEntity(confirm);
                	}
            		resultMap.put("code", "1");
                    resultMap.put("msg", "成功");
                    resultMap.put("confirm", confirm);
                    JSONObject jsonObj = new JSONObject(resultMap);
                    result = jsonObj.toString();
                    return result;
                }else {
                	tblRoleService.delete(role);
                	if(YMUrlStatic.status == 0 && role.getPkYmRoleId() == null && !"".equals(role.getPkYmRoleId())) {
                    	this.ymBusinessService.removeRoleInfo(staff,role.getPkYmRoleId());
                    }
                }


            }
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }

    @OperationLog(
            success = "保存",
            busType = "权限管理",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "岗位管理"
    )
    @RequestMapping(value = "/job/save", method = {RequestMethod.POST})
    @Operation(summary="岗位管理-保存")
    public String jobSave(HttpServletRequest request, TblJob job,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "staffId", required = false) String staffId) throws Exception {
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        BigDecimal jobId = null;
            TblStaffUtil staff = userProvider.get();
            BigDecimal pid = staff.getCurrentOrg().getOrgid();
            if (job.getJobid() != null) {
                tblJobService.updateJob(job);
                jobId = job.getJobid();
            } else {
                TblJob tj = new TblJob();
                // tj.setJobid(job.getJobid());
                tj.setJobname(job.getJobname());
                tj.setCompanyId(pid.toString());
                tj.setJobid(RandomUtil.uuBigDecimalId());
                tblJobService.saveJob(tj);
                jobId = tj.getJobid();
            }

            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();

        if(YMUrlStatic.status == 0) {
        	this.ymBusinessService.dealUniqueJobInfo(jobId);
        }
        return result;
    }


    @OperationLog(
            success = "根据ID查找岗位信息",
            busType = "权限管理",
            fail = "根据ID查找岗位信息",
            operationType = OperationType.SELECT,
            subType = "岗位管理"
    )
    @RequestMapping(value = "/job/job_modify", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-岗位修改-根据ID查找岗位信息")
    public String job_modify(HttpServletRequest request) {
        String result = null;
            Map<String, Object> resultMap = new HashMap<String, Object>(Integer.parseInt("访问数据成功"));
            String selectedId = request.getParameter("selectedId");
            if (selectedId != null && selectedId.trim().length() > 0) {
                TblJob findByid = tblJobService.findByid(selectedId);
                resultMap.put("job", findByid);
            }
            JSONObject jsonModify = new JSONObject(resultMap);
            result = jsonModify.toString();
            return result;
    }


    @OperationLog(
            success = "删除",
            busType = "权限管理",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "岗位管理"
    )
    @RequestMapping(value = "/job/job_del", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-岗位管理-删除")
    public String job_del(HttpServletRequest request,
    		 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          TblJob job,
                          @Parameter(name = "selectedId", required = false) String selectedId) throws Exception {
    	String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<TblStaff> staff = tblStaffService.findByJobid(job.getJobid());
            if (staff.size() > 0) {
                return JsonBean.error("已授权，不能删除");
            }
            if(YMUrlStatic.status == 0 ) {
            	this.ymBusinessService.removeUniqueJobInfo(job.getJobid());
            }
            tblJobService.deleteJob(job.getJobid());
            resultMap.put("code", "1");
            resultMap.put("msg", "删除成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }

    @RequestMapping(value = "/qxsd/qx", method = {RequestMethod.POST})
   @Operation(summary = "权限设定-查询权限信息")
    public String qx(HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        JSONObject jsonObjectMAV = new JSONObject(resultMap);
        result = jsonObjectMAV.toString();
        return result;
    }

    @OperationLog(
            success = "用户信息",
            busType = "权限管理",
            fail = "用户信息",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/user/info", method = {RequestMethod.POST})
    @Operation(summary="用户信息")
    public Object userInfo(HttpServletRequest request,
                           Find find,
                           @RequestParam(name = "staffid", value = "staffid", required = false) String staffid,
                           @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                           @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "pid", required = false) BigDecimal pid,
                           @Parameter(name = "staffId", description = "staffId", required = false) String staffId) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            if (staffId != null && !staffId.equals("")) {
                TblStaff user;
                if (staffid != null) {
                    user = userService.findById(staffid);
                } else {
                    user = userService.findById(staffId);
                }

                TblStaffUtil staff = userProvider.get();
                BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                List<TblRole> roles = tblRoleService.findAll(orgid);
                List<TblJob> findAll = tblJobService.findAll(orgid);
                resultMap.put("roles", roles);
                resultMap.put("jobs", findAll);
                resultMap.put("user", user);
                JSONObject jsonObjectMV = new JSONObject(resultMap);
                result = jsonObjectMV.toString();

            }
            return result;
    }


    @OperationLog(
            success = "公司及其下属部门",
            busType = "系统设置",
            fail = "公司及其下属部门",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/findOrganizationByTreeAllbm", produces = "application/json; charset=utf-8"/*, method = {RequestMethod.POST}*/)
    @Operation(summary="公司及其下属部门")//组织架构-左侧列表
    public @ResponseBody
    String findOrganizationByTrees(BigDecimal nodeId, String type, HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //if (null == nodeId) {
            TblStaffUtil staff = userProvider.get();
            nodeId = staff.getCurrentOrg().getOrgid();
        //}
        String str;
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        List<Tree> list = this.tblOrganizaService.getNodeAllbm(nodeId);
        str = JSONObject.toJSONString(list);
        return str;
    }

    @OperationLog(
            success = "公司及其下属公司",
            busType = "系统设置",
            fail = "公司及其下属公司",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/findOrganizationByJTTreeAll", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary=" 公司及其下属公司")
    public @ResponseBody
    String findOrganizationByJTTree(BigDecimal nodeId, String type, HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	 TblStaffUtil staff = userProvider.get();
        if (null == nodeId) {
            nodeId = staff.getCurrentOrg().getOrgid();
        }
        String str;
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        if (StringUtils.isNotBlank(type)) {
            try {
                str = HttpClient.request(HttpClient.getOrgChildrenUrl, fields, null);
                if (!StringUtils.isNotBlank(str)) {
                    List<Tree> list = this.tblOrganizaService.getJTTreeGS(nodeId);
                    str = JSONObject.toJSONString(list);
                }
                return str;
            } catch (Exception e) {
                List<Tree> list = this.tblOrganizaService.getJTTreeGS(nodeId);
                str = JSONObject.toJSONString(list);
            }
        } else {
            try {
                str = HttpClient.request(HttpClient.getOrgUrl, fields, null);
                if (!StringUtils.isNotBlank(str)) {
                    List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                    str = JSONObject.toJSONString(list);
                }
                return str;
            } catch (Exception e) {
                List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                str = JSONObject.toJSONString(list);
            }
        }
        return str;
    }


    @OperationLog(
            success = "列表查询",
            busType = "机构管理",
            fail = "列表查询",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/org/listorg", method = {RequestMethod.POST})
    @Operation(summary="机构管理-公司管理-列表+查询")
    public String listorg(HttpServletRequest request, Find find,
                          @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "staffId", required = false) String staffId,
                          @Parameter(name = "orgId", description = "组织ID", required = false) BigDecimal orgId,
                          @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch
    ) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblStaffUtil staff = userProvider.get();
            if (orgId == null) {
                orgId = staff.getCurrentOrg().getOrgid();
            }
            resultMap = tblOrganizaService.findAllCommpanyPageBeanGS(pageNumber, pageSize, staffId, token, find, orgId);

            JSONObject jsonObjectMAV = new JSONObject(resultMap);
            result = jsonObjectMAV.toString();
            return result;
    }

    @OperationLog(
            success = "左侧列表",
            busType = "机构管理",
            fail = "左侧列表",
            operationType = OperationType.SELECT,
            subType = "公司管理"
    )
    @RequestMapping(value = "/findOrganizationByJTTreeAllGS", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="机构管理-公司管理-左侧列表")
    public @ResponseBody
    String findOrganizationByJTTreeAllGS(String type, HttpServletRequest request,
                                         @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                         @Parameter(name = "nodeId", required = false) BigDecimal nodeId,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                         @Parameter(name = "staffId", required = false) String staffId) {

        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (nodeId != null) {
            resultMap = tblOrganizaService.getJTNodeAll(pageNumber, pageSize, nodeId);
        } else {
            resultMap = tblOrganizaService.getJTTree(pageNumber, pageSize, token, staffId);
        }
        JSONObject jsonObjectMAV = new JSONObject(resultMap);
        result = jsonObjectMAV.toString();
        return result;
    }

    //这个接口是点击了公司列表Tree后出现在左侧的数据

    @OperationLog(
            success = "右侧菜单列表",
            busType = "权限管理",
            fail = "右侧菜单列表",
            operationType = OperationType.SELECT,
            subType = "菜单管理"
    )
    @RequestMapping(value = "/qxsd/listorg", method = {RequestMethod.POST})
    @Operation(summary="菜单设定-右侧菜单列表")
    public String qxListorg(HttpServletRequest request,
                            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @Parameter(name = "pid", required = false) BigDecimal pid,
                            @Parameter(name = "orgname", description = "公司名称", required = false)@RequestParam(value = "orgname", required = false)String orgname,
                            @Parameter(name = "orgnumber", description = "公司编号", required = false)@RequestParam(value = "orgnumber", required = false)String orgnumber,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal staffId = staff.getStaffid();
        if (pid == null) {
            pid = staff.getCurrentOrg().getOrgid();
        }
        resultMap = tblOrganizaService.findAllCommpanyPageBean(pageNumber, pageSize, pid,orgname,orgnumber);
        JSONObject jsonObjectMAV = new JSONObject(resultMap);
        result = jsonObjectMAV.toString();
        return result;
    }

    @RequestMapping(value = "/org/hy_info", method = {RequestMethod.POST})
    @Operation(summary="机构管理-行业架构-详情")
    public String orgHYInfo(HttpServletRequest request,
                            String orgid) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            // String id = request.getParameter("orgid");
            TblOrganization org = tblOrganizaService.findById(orgid);
            // ModelAndView mv = new ModelAndView("common/zzjg/hy_organ_info");
            resultMap.put("org", org);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    @RequestMapping(value = "/qx/qxInfo", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-详情")
    public String qxInfo(HttpServletRequest request,
                         BigDecimal staffid) {
        String result = null;
        try {
			Map<String, Object> resultMap = new HashMap<String, Object>(0);
			    TblStaff org = tblStaffService.findByStaffid(staffid);
			    resultMap.put("org", org);
			    JSONObject jsonObjectMV = new JSONObject(resultMap);
			    result = jsonObjectMV.toString();
			    return result;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return result;
    }

    @RequestMapping(value = "/user/detailInfo", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-用户管理-详情")
    public JsonBean user_detailInfo(HttpServletRequest request,BigDecimal staffid) {
    	JsonBean jsonBean = null;
    	Map<String, Object> resultMap = new HashMap<String, Object>(0);
		try {
			    TblStaff org = tblStaffService.findByStaffid(staffid);
			    resultMap.put("user", org);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResponseFormat.retParam(1, 200, resultMap);
    }

    @RequestMapping(value = "/org/org_info", method = {RequestMethod.POST})
    @Operation(summary="机构管理-菜单设定-菜单设定查看详情")
    public String org_info(HttpServletRequest request,
                           @Parameter(name = "orgid", required = false) BigDecimal orgid) {
            return String.valueOf(tblOrganizaService.findByOrg(orgid));
    }


    @RequestMapping(value = "/qxsd/screenRightList/qx_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="用户授权功能(已废弃)")
    public @ResponseBody
    String qxsd_screenRightList_qx_save(HttpServletRequest reques,
                                        @RequestParam(value = "userid", required = false) String userid,
                                        @RequestParam(value = "priid", required = false) String priid) throws Exception {
            if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(priid)) {
                TblStaff tblStaff = this.userService.findById(userid);
                if (null != tblStaff) {
                    this.tblManageScreenRightService.grantScreenRight(userid, priid);
                    return JsonBean.success();
                }
                return JsonBean.error("用户不存在");
            }
            return JsonBean.error();
    }

    @OperationLog(
            success = "列表查询",
            busType = "权限管理",
            fail = "列表查询",
            operationType = OperationType.SELECT,
            subType = "权限设定"
    )
    @RequestMapping(value = "/qxsd/list", method = {RequestMethod.POST})
    @Operation(summary="权限设定-查询")
    public String qxList(HttpServletRequest request, Find find,
                         @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "pid", required = false) BigDecimal pid,
                         @Parameter(name = "staffId", required = false) String staffid,
                         @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            if (pid == null) {
                resultMap = userService.findAllPageBean(find, pageNumber, pageSize, token, staffid);
            } else {
                resultMap = userService.findAllPageBeanPid(staffid, find, pageNumber, pageSize, pid);
            }

            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    @OperationLog(
            success = "用户授权功能（已废弃）",
            busType = "权限管理",
            fail = "用户授权功能（已废弃）",
            operationType = OperationType.UPDATE,
            subType = "用户管理"
    )
    @RequestMapping(value = "/qxsd/qx_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="用户授权功能（已废弃）")
    public @ResponseBody
    String qx_save(
            @RequestParam(value = "userid", required = false) String userid,
            @RequestParam(value = "priid", required = false) String priid) {
            if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(priid)) {
                TblStaff tblStaff = this.userService.findByUserId(userid);
                if (null != tblStaff) {
                    Jedis jedis = JedisUtil.getJedis();
                    tblManageRightService.grantScreenRight(userid, priid);

                    String listorgtreeJSON = RightCatch.getRightForUser(tblStaff.getStaffid().toString());
                    jedis.set(RedisFinalUtis.USERMANGERRIGHT + tblStaff.getStaffid().toString(), listorgtreeJSON);
                    jedis.close();
                    return JsonBean.success();
                }
                return JsonBean.error("用户不存在");
            }
            return JsonBean.error();
    }

    @OperationLog(
            success = "报表授权功能",
            busType = "权限管理",
            fail = "报表授权功能",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/qxsd/screenRightList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="报表授权功能")
    public @ResponseBody
    String qxsd_screenRightList(HttpServletRequest reques,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffid", description = "登录用户主键", required = false) String staffid) throws Exception {

            TblStaff tblStaff = this.userService.findById(staffid);
            TblStaffUtil staff = userProvider.get();
            BigDecimal pid = staff.getCurrentOrg().getOrgid();

            if (null != tblStaff) {
                String orgid = pid.toString();
                String treeJson = this.tblManageScreenRightService.getTree(tblStaff);
                return treeJson;
            }
            return null;
    }


    @OperationLog(
            success = "权限设定（选定-选择，出功能授权）",
            busType = "权限管理",
            fail = "权限设定（选定-选择，出功能授权）",
            operationType = OperationType.UPDATE,
            subType = "权限设定"
    )
    @RequestMapping(value = "/qxsd/mgnsq", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="用户权限管理-权限设定-权限设定（选定-选择，出功能授权）")
    public @ResponseBody
    String qx_gnsq(HttpServletRequest reques,
                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                   @Parameter(name = "staffid", description = "登录用户主键", required = false) String staffid) throws Exception {

            TblStaff tblStaff = this.userService.findById(staffid);
            TblStaffUtil staff = userProvider.get();
            if (null != tblStaff) {
                Map<BigDecimal, Object> map = new HashMap<BigDecimal, Object>();
                List<TblManageRight> userRight = this.tblStaffService.findMansgeUserRight(staffid);
                for (TblManageRight tr : userRight) {
                    map.put(tr.getRightid(), 1);
                }
                String orgid = staff.getCurrentOrg().getOrgid().toString();
                String treeJson = tblManageRightService.GetTree(staffid, map, orgid);
                //String treeJson =tblManageRightService.GetTreenew(rightid, orgid);
                return treeJson;
            }
            return null;
    }

    @OperationLog(
            success = "下级公司授权-主页",
            busType = "系统设置",
            fail = "下级公司授权-主页",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/zt/index", method = {RequestMethod.POST})
    @Operation(summary="下级公司授权-主页")
    public String zt_index(HttpServletRequest request) {
        String result = null;
        ModelAndView mv = new ModelAndView("common/gsgl/zt_list");
        Map<String, Object> resultMap = new HashMap<String, Object>(Integer.parseInt("访问数据成功"));
        JSONObject jsonObjectmv = new JSONObject(resultMap);
        result = jsonObjectmv.toString();
        return result;
    }


    @OperationLog(
            success = "查询公司左侧tree的数据",
            busType = "系统设置",
            fail = "查询公司左侧tree的数据",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/zt/leftlist", method = {RequestMethod.POST})
    @Operation(summary="查询公司左侧tree的数据")
    public String zt_leftlist(HttpServletRequest request) {
        String result = null;
        ModelAndView mv = new ModelAndView("common/gsgl/zt_left");
        Map<String, Object> resultMap = new HashMap<String, Object>(Integer.parseInt("访问数据成功"));
        JSONObject jsonObjectMv = new JSONObject(resultMap);
        result = jsonObjectMv.toString();
        return result;
    }


    @OperationLog(
            success = "查询公司的数据",
            busType = "系统设置",
            fail = "查询公司的数据",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/zt/listorg", method = {RequestMethod.POST})
    @Operation(summary="查询公司的数据")
    public String zt_listorg(HttpServletRequest request,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "pid", required = false) BigDecimal pid,
                             @Parameter(name = "staffId", required = false) String staffId
    ) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        resultMap = tblAcctBookService.findByTypeNewZB(token, pageNumber, pageSize, pid);
        JSONObject jsonObjectMav = new JSONObject(resultMap);
        result = jsonObjectMav.toString();
        return result;
    }


    @OperationLog(
            success = "主题仓库一级菜单列表",
            busType = "系统设置",
            fail = "主题仓库一级菜单列表",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/reportlist", method = {RequestMethod.POST})
    @Operation(summary="主题仓库一级菜单列表")
    public String biReportMenu(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @RequestParam(name = "orgid", required = false) String orgid,
                               @RequestParam(name = "pid", required = false) String pid,
                               @RequestParam(name = "type", required = false) String type) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        PageBean pageBean = new PageBean();
        try {
            resultMap = tblOrganizaService.finreportMenuList(pageNumber, pageSize, StringUtil.isNullOrEmpty(pid) ? type : pid, token, orgid);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            resultMap.put("pageBean", pageBean);
            resultMap.put("type", type);
        }
        JSONObject jsonObjectMav = new JSONObject(resultMap);
        result = jsonObjectMav.toString();
        return result;

    }

    @OperationLog(
            success = "右侧列表查询",
            busType = "权限管理",
            fail = "右侧列表查询",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/zt_user/newlist", method = {RequestMethod.POST})
    @Operation(summary="账套授权(点了选择出现的框框【点了左侧列表后，右侧列表出现数据】)")
    public String userNewListss(HttpServletRequest request, String acctid, String page,
                                String pid, String type,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", required = false) String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        Jedis jedis = null;
        if (type != null && type.length() > 0) {
            request.getSession().setAttribute("userType", type);
        }
        if (type != null && type.equals("''")) {
            type = null;
        }

        if (pid != null && pid.trim().length() != 0) {
            resultMap = tblStaffService.findByAll(pid, pageNumber, pageSize, token, staffId);
        } else {
            resultMap = tblStaffService.findAllPageBean(pageNumber, pageSize, token, staffId);
        }
        //resultMap.put("orgid",orgid);
        resultMap.put("pid", pid);
        resultMap.put("type", type);
        resultMap.put("acctid", acctid);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;

    }
    @OperationLog(
            success = "点击选定后",
            busType = "权限管理",
            fail = "点击选定后",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/zt/saveNewAccBookManage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="账套授权（点击选定后的接口）")
    public @ResponseBody
    String saveNewAccBookManage(HttpServletRequest request,
                                @RequestParam(value = "acctid", required = false) String acctid,
                                @RequestParam(value = "staffid", required = false) String staffid,
                                @RequestParam(value = "orgid", required = false) String orgid,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {


            TblStaffUtil staff = userProvider.get();
            if (StringUtils.isNotBlank(staffid)) {
                Set<TblManageUserBook> userBookList = new HashSet<TblManageUserBook>();
                List<TblAccBook> bookList = new ArrayList<TblAccBook>(0);
                //TblManageUserBook userBook = null;
                TblManageUserBook bookId = null;
                String[] acctids = acctid.split(",");
                String[] staffids = staffid.split(",");
                for (String pri : acctids) {
                    if (pri.trim().length() != 0) {
                        for (int i = 0; i < staffids.length; i++) {
                            bookId = new TblManageUserBook();
                            bookId.setBookid(pri);
                            bookId.setStaffid(new BigDecimal(staffids[i]));
                            userBookList.add(bookId);
                        }
                    }
                }

                tblManageUserBookservice.updateUserBook(userBookList, orgid, staffid);
                List<TblAccBook> tablist = tblAccBookService.findBookIdByUserAll(staff.getStaffid(), staff.getCurrentOrg().getOrgid());
                List<TblAccBook> books = DepartmentUtils.getAllDepartmentss(tablist);
                request.getSession().setAttribute("tablist", books);
                return JsonBean.success();
            }
            return JsonBean.error("请选择");
    }


    @OperationLog(
            success = "取消授权，查询用户信息",
            busType = "权限管理",
            fail = "取消授权，查询用户信息",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/user/zbqxindexs", method = {RequestMethod.POST})
    @Operation(summary="账簿数据取消授权，查询用户信息")
    public String zbqxindexs(HttpServletRequest request,
                             @RequestParam(value = "acctid", required = true) String acctid,
                             @RequestParam(value = "realname", required = false) String realname,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        String result = null;
            TblStaffUtil user = userProvider.get();
            PageInfo<TblStaff> pageInfo = new PageInfo<TblStaff>();
            try {
                TblStaff staff = new TblStaff();
                staff.setOrgid(user.getCurrentOrg().getOrgid());
                if (realname != null && realname.trim().length() != 0) {
                    staff.setRealname(realname);
                }
                pageInfo.setCurrentPage(pageNumber);
                pageInfo.setPageSize(pageSize);
                pageInfo.setCondition(staff);
                acctid = "'"+acctid.replace(",", "','")+"'";
                pageInfo.setSqlStr(acctid);
                Map<String, Object> resultMap = tblStaffService.findUserByZbsjForQx(pageInfo);
                JSONObject jsonObjectMv = new JSONObject(resultMap);
                result = jsonObjectMv.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "用户账套授权取消功能",
            busType = "权限管理",
            fail = "用户账套授权取消功能",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/zt/delAccBookManage", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="套账授权-用户账套授权取消功能")
    public @ResponseBody
    String delAccBookManage(@RequestParam(value = "sid", required = false) String sid,
                            @RequestParam(value = "acctid", required = false) String acctid,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

            try {
                //logger.info("取消用户账套授权");
                tblManageUserBookservice.delUserBook(sid, acctid);
                TblStaffUtil staff = userProvider.get();
                List<TblAccBook> bookList = tblAccBookService.findBookIdByUserAll(staff.getStaffid(), staff.getCurrentOrg().getOrgid());
                List<TblAccBook> books = DepartmentUtils.getAllDepartmentss(bookList);
                request.getSession().setAttribute("tablist", books);
                return JsonBean.success();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return JsonBean.error();
    }


    @OperationLog(
            success = "审计模型-查询所属账套",
            busType = "权限管理",
            fail = "审计模型-查询所属账套",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/sjmx/getlist", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="审计模型-查询所属账套")
    public JsonBean sjmxgetlist(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
    	JsonBean jsonBean = null;
    	try {
    		jsonBean = tblAccBookService.findstaffid(token);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseFormat.retParam(0,"SQL有误，请修改",null);
		}
        return jsonBean;
    }




    /**
     * 账套授权-查看详情
     *
     * @param request
     * @param pageNumber
     * @param pageSize
     * @param bookid
     * @param pid
     * @return
     */


    @OperationLog(
            success = "查看详情",
            busType = "权限管理",
            fail = "查看详情",
            operationType = OperationType.SELECT,
            subType = "账套授权"
    )
    @RequestMapping(value = "/zt/accbookdetail", method = {RequestMethod.POST})
    @Operation(summary="账套授权-查看详情")
    public String accbookdetail(HttpServletRequest request,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "bookid", required = false) String bookid,
                                @RequestParam(value = "pid", required = false) String pid) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            PageInfo<TblStaff> pageInfo = new PageInfo<>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            userService.findAllPageInfoByacctid(pageInfo, bookid);

            resultMap.put("pid", pid);
            resultMap.put("bookid", bookid);
            resultMap.put("pageInfo", pageInfo);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }


    @OperationLog(
            success = "内部专家资源库列表",
            busType = "专家资源",
            fail = "内部专家资源库列表",
            operationType = OperationType.SELECT,
            subType = "内部专家"
    )
    @RequestMapping(value = "/sjzyk_nbzj", method = {RequestMethod.POST})
    @Operation(summary="内部专家资源库列表")
    public String sjzyk_nbzj(HttpServletRequest request, Find find,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId
    ) {
        String result = null;
            Map<String, Object> resultMap = this.tblInteriorExpertService.getExperList(find, staffId, pageNumber, pageSize, token);
            JSONObject jsonObjectMv = new JSONObject(resultMap);
            result = jsonObjectMv.toString();
            return result;
    }


    @OperationLog(
            success = "资源库列表",
            busType = "专家资源",
            fail = "资源库列表",
            operationType = OperationType.SELECT,
            subType = "外聘专家"
    )
    @RequestMapping(value = "/sjzyk_wpzj", method = {RequestMethod.POST})
    @Operation(summary="外聘专家资源库列表")
    public String sjzyk_wpzj(HttpServletRequest request, Find find, String company,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
            Map<String, Object> resultMap = this.tblExternalExpertService.findByOrgId(find, pageNumber, pageSize, token, staffId, company);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }


    @OperationLog(
            success = "新增/修改",
            busType = "专家资源",
            fail = "新增/修改",
            operationType = OperationType.ADD,
            subType = "内部专家"
    )
    @RequestMapping(value = "/nbzj_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内部专家  新增  or 修改")
    public @ResponseBody
    String nbzj_save(HttpServletRequest request, BigDecimal userid, TblInteriorExpert interiorExpert,
                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                     @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        BigDecimal orgid;
            TblStaffUtil staff = userProvider.get();
            BigDecimal pid = staff.getCurrentOrg().getOrgid();
            if (null != userid) {
                TblStaff tblStaff = this.tblStaffService.get(userid);
                if (tblStaff != null) {
                    //TODO
                    if (interiorExpert.getInteriorid() == null) {
                        interiorExpert.setOrgId(pid);
                        interiorExpert.setStaffid(tblStaff.getStaffid());
                        this.tblInteriorExpertService.save(interiorExpert);
                    } else {
                        tblInteriorExpertService.update(interiorExpert);
                    }
                    return JsonBean.success();
                }
            }
            return JsonBean.success();
    }

    @OperationLog(
            success = "删除",
            busType = "专家资源",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "内部专家"
    )
    @RequestMapping(value = "/nbzj_del", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内部专家库-删除")
    public @ResponseBody
    String nbzj_save(HttpServletRequest request, BigDecimal interiorid) {
//                                          @Parameter(name="token",value="登录用户token",required=true) @RequestHeader("token") String token,
//                                          @Parameter(name="staffId",value="登录用户ID",required=false)String staffId

        String result = null;
        try {
            Map<String, Object> resultMap = this.tblInteriorExpertService.removeNbzj(interiorid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    @OperationLog(
            success = "新增",
            busType = "专家资源",
            fail = "新增",
            operationType = OperationType.ADD,
            subType = "外聘专家"
    )
    @RequestMapping(value = "/sjzjk_to_add", method = {RequestMethod.POST})
    @Operation(summary="外聘专家库-添加")
    public String sjzjk_to_add(HttpServletRequest request, BigDecimal exterid,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        //String oid = request.getParameter("oid");
            TblStaffUtil tblstaff = userProvider.get();
            BigDecimal pid = tblstaff.getCurrentOrg().getOrgid();
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            if (exterid != null) {
                TblExternalExpert expert = tblExternalExpertService.getExpert(exterid);
                TblStaff staff = tblStaffService.getExpert(expert.getStaffid());
                resultMap.put("expert", expert);
                resultMap.put("staff", staff);
            }
            resultMap.put("pid", pid);
            String result = null;
            JSONObject jsonObjectMv = new JSONObject(resultMap);
            result = jsonObjectMv.toString();
            return result;
    }

    @OperationLog(
            success = "添加",
            busType = "专家资源",
            fail = "添加",
            operationType = OperationType.ADD,
            subType = "外聘专家"
    )
    @RequestMapping(value = "/sjzjk_add", method = {RequestMethod.POST})
    @Operation(summary="外聘专家库-添加")
    public String sjzjk_add(HttpServletRequest request, TblStaff user, TblExternalExpert tee,
                            @Parameter(name = "oid", description = "oid", required = true) String oid,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

            if (tee != null && tee.getExterid() != null) {
                tblExternalExpertService.updateTblExternalExpert(tee);
                TblStaff staff = tblStaffService.get(tee.getStaffid());
                staff.setUsername(user.getUsername());
                staff.setRealname(user.getRealname());
                staff.setEmail(user.getEmail());
                staff.setMiblephone(user.getMiblephone());
                staff.setStatus(user.getStatus());
                tblStaffService.modifyStaff(staff);
                // is = true;
            } else {
                //TblOrganization org = tblOrganizaService.findByoId(new BigDecimal(oid));
                user.setOrgid(new BigDecimal(oid));
                user.setPassword( MD5Encrypt.md5WithEncoding(user.getPassword(),"UTF-8"));
                user.setStaffid(RandomUtil.uuBigDecimalId());
                tblStaffService.add(user);
                tee.setStaffid(user.getStaffid());
                tblExternalExpertService.saveTblExternalExpert(tee);
            }

            return JsonBean.success();
    }

    @OperationLog(
            success = "跳主题添加修改页面",
            busType = "主题仓库",
            fail = "跳主题添加修改页面",
            operationType = OperationType.SELECT,
            subType = "主题展示"
    )
    @RequestMapping(value = "/addReportMenuView", method = {RequestMethod.POST})
    @Operation(summary="主题仓库-主题展示-跳主题添加修改页面")
    public String addReportMenuView(String id, String pid, String type,
                                    @RequestParam(value = "leveltype", required = false) String leveltype) {


            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            String result = null;
            if (!id.equals("") & id != null) {
                TblBiReportMenu tblBiReportMenu = new TblBiReportMenu();
                TblOrganization or = new TblOrganization();
                try {
                    tblBiReportMenu = tblBiReportMenuService.geTblBiReportMenu(id);
                    or = tblOrganizaService.findById(tblBiReportMenu.getUnit());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    resultMap.put("tbp", tblBiReportMenu);
                    resultMap.put("orgname", or.getOrgname());
                    resultMap.put("pid", tblBiReportMenu.getPageid());
                    resultMap.put("a", tblBiReportMenu.getTblAttachments());
                    resultMap.put("type", type);
                    resultMap.put("msg", "访问成功");
                }

            } else {
                TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 当前用户的机构
                resultMap.put("msg", "访问成功");
                resultMap.put("org", attribute1);
                //当新增主题为一级菜单式leveltype为null,二级菜单leveltype为0；用来判断新增的是一级主题还二级主题
                if (leveltype != null && leveltype.trim().length() != 0) {
                    resultMap.put("level", 0);
                } else {
                    resultMap.put("level", 1);
                }
                resultMap.put("pid", pid);
                resultMap.put("type", type);
            }
            JSONObject jsonObjMv = new JSONObject(resultMap);
            result = jsonObjMv.toString();
            return result;
    }

    @Operation(summary="主题仓库编号校验")
    @RequestMapping(value = "/page_byNum", method = {RequestMethod.POST})
    public @ResponseBody
    String page_byNum(HttpServletRequest request) {
        String code = request.getParameter("code");
        return tblBiReportMenuService.isExistBiPageCode(code).toString();
    }

    @OperationLog(
            success = "新建(同手机主题新建)修改",
            busType = "主题仓库",
            fail = "新建(同手机主题新建)修改",
            operationType = OperationType.ADD,
            subType = "主题展示"
    )
    @RequestMapping(value = "/addReportMenu", method = {RequestMethod.POST})
    @Operation(summary="主题仓库-主题展示-新建(同手机主题新建)修改")
    public String addReportMenu(TblBiReportMenu menu,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                                @Parameter(name = "unit", required = false) String unit) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
            try {
                if (menu.getPageid() != null) {
                    TblBiReportMenu page = tblBiReportMenuService.geTblBiReport(menu.getPageid());
                    page.setUnit(unit);
                    page.setPagecode(menu.getPagecode());
                    page.setPagename(menu.getPagename());
                    page.setMemo1(menu.getMemo1());
                    page.setUrl(menu.getUrl());
                    page.setPageDes(menu.getPageDes());
                    if (menu.getType() == null) {
                        menu.setType("0");
                    }
                    page.setType(menu.getType());
                    page.setRqurl(menu.getRqurl());
                    tblBiReportMenuService.updateReportMenu(menu);

                } else {
                    TblStaffUtil staff = userProvider.get();
                    BigDecimal orgid = staff.getCurrentOrg().getOrgid();
                    menu.setUnit(orgid.toString());
                    menu.setCreatedate(new Date());
                    menu.setRightid(new BigDecimal(194));
                    if (menu.getType() == null) {
                        menu.setType("0");
                    }
                    menu.setMemo2("0");
                    if (menu.getPid() != null) {
                        menu.setPagebody(new BigDecimal(menu.getPid()));
                    }
                    menu.setPageid(RandomUtil.uuBigDecimalId());
                    tblBiReportMenuService.saveReportMenu(menu);
                }

            } catch (Exception e) {
                System.out.println("主题菜单添加失败！");
                e.printStackTrace();
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "访问接口成功");
            resultMap.put("data", "成功");
            JSONObject jsonObjMv = new JSONObject(resultMap);
            result = jsonObjMv.toString();

            return result;
    }


    @OperationLog(
            success = "打开模块下发页面,获取模块列表",
            busType = "主题仓库",
            fail = "打开模块下发页面,获取模块列表",
            operationType = OperationType.SELECT,
            subType = "主题展示"
    )
    @RequestMapping(value = "/moduleDistributio", method = {RequestMethod.POST})
    @Operation(summary="主题展示 打开模块下发页面,获取模块列表")
    public JsonBean moduleDistribution(HttpServletRequest request,
    		@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	JsonBean jsonBean;
		try {
			log.info("主题展示 打开模块下发页面,获取模块列表接口");
			jsonBean = tblSystemProjectOracleService.getThemeModuleList(token);
		} catch (Exception e) {
			log.error("主题展示 打开模块下发页面,获取模块列表 异常信息：", e);
			throw new ServiceException(2, e.getMessage());
		}
		return jsonBean;
    }

    @OperationLog(
            success = "一级主题模块下发",
            busType = "主题仓库",
            fail = "一级主题模块下发",
            operationType = OperationType.DISPATCH,
            subType = "主题展示"
    )
    @RequestMapping(value = "/chooseRithgId/disPageId", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="主题仓库一级主题模块下发")
    public JsonBean chooseRithgIdDisPageId(Model model, HttpServletRequest request,
    	@Parameter(name = "moduleTypes", description = "下发的所属模块的模块类型数组", required = true)@RequestParam(value = "moduleTypes", required = true) String[] moduleTypes,
    	@Parameter(name = "pageids", description = "一级主题仓库id数组", required = true)@RequestParam(value = "pageids", required = true) String[] pageids,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
		try {
			jsonBean = this.tblManageRightService.distributionPageIdByModuleType(moduleTypes, pageids, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }


    @OperationLog(
            success = "二级主题模块下发",
            busType = "主题仓库",
            fail = "二级主题模块下发",
            operationType = OperationType.DISPATCH,
            subType = "主题展示"
    )
    @RequestMapping(value = "/chooseRithgId/disPageIdTwo", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="主题仓库二级主题模块下发")
    public JsonBean chooseRithgIdDisPageIdTwo(Model model, HttpServletRequest request,
    		@Parameter(name = "moduleTypes", description = "下发的所属模块的模块类型数组", required = true)@RequestParam(value = "moduleTypes", required = true) String[] moduleTypes,
    		@Parameter(name = "pageids", description = "二级主题仓库id数组", required = true)@RequestParam(value = "pageids", required = true) String[] pageids,
    		@Parameter(name = "pid", description = "一级主题仓库id", required = true)@RequestParam(value = "pid", required = true) String pid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblManageRightService.distributionPageIdByRightIdTwo(moduleTypes, pageids, token, pid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }

    @OperationLog(
            success = "取消模块下发获取所选择的主体的模块列表",
            busType = "主题仓库",
            fail = "取消模块下发获取所选择的主体的模块列表",
            operationType = OperationType.SELECT,
            subType = "主题展示"
    )
    @RequestMapping(value = "/cancelModuleList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="主题仓库取消模块下发获取所选择的主体的模块列表")
    public JsonBean cancelModuleList(Model model, HttpServletRequest request,
    	@Parameter(name = "pageids", description = "主题主键", required = true)@RequestParam(value = "pageids", required = true) String[] pageids,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
		try {
			jsonBean = this.tblManageRightService.cancelModuleList(pageids, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }


    @OperationLog(
            success = "取消模块下数据",
            busType = "主题仓库",
            fail = "取消模块下数据",
            operationType = OperationType.SELECT,
            subType = "主题展示"
    )
    @RequestMapping(value = "/cancelBiModule", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="主题仓库取消模块下数据")
    public JsonBean cancelBiModule(Model model, HttpServletRequest request,
    	@Parameter(name = "pageids", description = "主题主键", required = true)@RequestParam(value = "pageids", required = true) String[] pageids,
    	@Parameter(name = "moduleTypes", description = "模块类型数组", required = true)@RequestParam(value = "moduleTypes", required = true) String[] moduleTypes,
    	@Parameter(name = "type", description = "主题层级 1-一级主题 2-二级主题", required = true)@RequestParam(value = "type", required = true) Integer type,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
		try {
			jsonBean = this.tblManageRightService.cancelBiModule(pageids, moduleTypes,token,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }


    @OperationLog(
            success = "取消用户下数据",
            busType = "主题仓库",
            fail = "取消用户下数据",
            operationType = OperationType.SELECT,
            subType = "主题展示"
    )
    @RequestMapping(value = "/cancelBiStaff", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="主题仓库取消用户下数据")
    public JsonBean cancelBiStaff(Model model, HttpServletRequest request,
    	@Parameter(name = "pageids", description = "主题主键", required = true)@RequestParam(value = "pageids", required = true) String[] pageids,
    	@Parameter(name = "staffids", description = "模块类型数组", required = true)@RequestParam(value = "staffids", required = true) String[] staffids,
    	@Parameter(name = "type", description = "主题层级 1-一级主题 2-二级主题", required = true)@RequestParam(value = "type", required = true) Integer type,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        JsonBean jsonBean = null;
		try {
			jsonBean = this.tblManageRightService.cancelBiStaff(pageids, staffids,token,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }



    @OperationLog(
            success = "选择用户",
            busType = "系统设置",
            fail = "选择用户",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/user/indexs", method = {RequestMethod.POST})
    @Operation(summary="选择用户")
    public String userIndex(HttpServletRequest request,
                            @RequestParam(value = "id", required = false) String id,
                            @RequestParam(value = "start", required = false) String start,
                            @RequestParam(value = "num", required = false) String num) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;

            if (start != null) {
                List<String> pageNameList = new ArrayList<String>();
                TblBiReportMenu geTblBiReportMenu = null;
                for (int i = 0; i < id.length(); i++) {
                    geTblBiReportMenu = this.tblBiReportMenuService.geTblBiReportMenu(id);
                    //  pageNameList.add(geTblBiReportMenu.getPagename());
                }
                resultMap.put("pageNameList", geTblBiReportMenu);
            }
            //String num = request.getParameter("type");
            resultMap.put("type", num);
            resultMap.put("selectM", request.getParameter("selectM"));
            JSONObject jsonObjectMv = new JSONObject(resultMap);
            result = jsonObjectMv.toString();
            return result;
    }


    @OperationLog(
            success = "数据控制（内部专家添加时内部单选人接口）",
            busType = "专家资源",
            fail = "数据控制（内部专家添加时内部单选人接口）",
            operationType = OperationType.SELECT,
            subType = "内部专家"
    )
    @Operation(summary="集团版 --数据控制（内部专家添加时内部单选人接口）")
    @RequestMapping(value = "/findOrganizationByTreeAll", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String findOrganizationByTree(BigDecimal nodeId, String type, BigDecimal orgId
            , @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  HttpServletRequest request,
                                  @RequestParam(value = "str", required = false) String str) throws Exception {
        String json = "";
        TblStaffUtil staff = userProvider.get();
        if (null == nodeId) {
            nodeId = orgId;
            if (null == orgId) {
                nodeId = staff.getCurrentOrg().getOrgid();
            }
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = this.tblOrganizaService.getTree(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("/nbsj/user/list?type=" + str + "&pid=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            // 修改前 Tree tree = this.tblOrganizaService.getTreeRoot(nodeId);
            //tyb修改 项目管理--/项目经理展开左侧树
            List<Tree> list = this.tblOrganizaService.getNodeAll(nodeId);
            for (Tree tree : list) {
                setUrlByTree(tree, "/nbsj/user/list?type=" + str + "&pid=");
            }
            json = JSONObject.toJSONString(list);
        }
        return json;
    }

    private void setUrlByTree(Tree tree, String url) {
        for (Tree tre : tree.getChildren()) {
            if (tre.getChildren().size() > 0) {
                setUrlByTree(tre, url);
            }
            // if (!tre.getIsParent()) {
            tre.setTarget("mainFramex");
            tre.setUrl(url + tre.getId());
            //}
        }
    }

    @OperationLog(
            success = "删除",
            busType = "主题仓库",
            fail = "删除",
            operationType = OperationType.SELECT,
            subType = "主题菜单"
    )
    @RequestMapping(value = "/page_del", method = {RequestMethod.POST})
    @Operation(summary="主题菜单-删除")
    public JsonBean page_del(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="type",description="下发仓库主题 1-一级主题，2-二级主题",required=true)@RequestParam(value="type",required=true)String type,
    		@Parameter(name="selectedId",description="删除的主题仓库主键",required=true)@RequestParam(value="selectedId",required=true) BigDecimal selectedId) {
    	//不知道干啥先注释掉
    	//tblBiCkEchartsService.delete(selectedId);
    	JsonBean jsonBean = null;
		try {
			jsonBean = tblBiReportMenuService.deleteReportMenuPageId(selectedId,token,type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;

    }

    @OperationLog(
            success = "二级报表下发到个人",
            busType = "主题仓库",
            fail = "二级报表下发到个人",
            operationType = OperationType.SELECT,
            subType = "主题菜单"
    )
    @RequestMapping(value = "/issudeUser", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="主题仓库一二级报表下发到个人")
    public JsonBean issuedUser(HttpServletRequest request,
    		@Parameter(name="token",description="登录用户token",required=true) @RequestHeader("token")String token,
    		@Parameter(name="type",description="下发仓库主题 1-一级主题，2-二级主题",required=true)@RequestParam(value="type",required=true)Integer type,
    		@Parameter(name="pageIds",description="下发的仓库主题Id数组",required=true)@RequestParam(value="pageIds",required=true)String[] pageIds,
    		@Parameter(name="fatherId",description="下发二级仓库主题时传入的一级主题id",required=false)@RequestParam(value="fatherId",required=false)String fatherId,
    		@Parameter(name="staffIds",description="下发选择人员ID数组",required=true)@RequestParam(value="staffIds",required=true)String[] staffIds) {
    	JsonBean jsonBean = null;
		try {
			jsonBean = this.tblBiReportMenuService.distributeThemeReportToUser(token,type,pageIds,staffIds,fatherId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonBean;
    }

    //风险分类

    @OperationLog(
            success = "根据当前公司获取风险分类数据默认获取一级分类",
            busType = "主题仓库",
            fail = "根据当前公司获取风险分类数据默认获取一级分类",
            operationType = OperationType.SELECT,
            subType = "风险分类"
    )
    @RequestMapping(value = "/selectallrisk", method = {RequestMethod.POST})
    @Operation(summary="根据当前公司获取风险分类数据默认获取一级分类(主题仓库-风险分类-列表查询(编号、一类))")
    public String selectallrisk(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @RequestParam(value = "riskLevel", required = false) String riskLevel,
                                @RequestParam(value = "parentId", required = false) String parentId,
                                @RequestParam(value = "riskNumber", required = false) String riskNumber,
                                @RequestParam(value = "riskclass", required = false) String riskclass,
                                @RequestParam(value = "type", required = false) String type) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        if (riskLevel == null) {
            riskLevel = "1";
        }
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        PageInfo<RiskClass> pageInfo = null;
        RiskClass risk = new RiskClass();

        if (!riskLevel.equals("1") && "back".equals(type)) {
            choiceMap = new HashMap<String, Object>(0);
            choiceMap.put("riskid", parentId);
            String tree = HttpClient.request(HttpClient.sxjm_findone, choiceMap, null);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);
            String resultone = jsonObject.getString("result");
            if (resultone != null && resultone.equals("true")) {
                String result1 = jsonObject.getString("entity");

                jsonObject = net.sf.json.JSONObject.fromObject(result1);
                risk = (RiskClass) net.sf.json.JSONObject.toBean(jsonObject, RiskClass.class);
                parentId = risk.getParentid();
            }
        } else if ("back".equals(type)) {
            parentId = "";
        }
        choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("parentid", parentId);
        choiceMap.put("level", riskLevel);
        choiceMap.put("pageNumber", pageNumber);
        choiceMap.put("riskNumber", riskNumber);
        choiceMap.put("riskclass", riskclass);
        choiceMap.put("pagesize", pageSize);
        choiceMap.put("orgid", staff.getCurrentOrg().getOrgid());
        String tree = HttpClient.request(HttpClient.sxjm_bypanrent, choiceMap, null);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);
        String resultone = jsonObject.getString("result");
        if (resultone != null && resultone.equals("true")) {
            String result1 = jsonObject.getString("list");
            String count = jsonObject.getString("count");
            List<RiskClass> list = JSON.parseObject(result1, new TypeReference<List<RiskClass>>() {
            });
            pageInfo = new PageInfo<RiskClass>();
            pageInfo.setCurrentPage(pageNumber);
            //pageInfo.setPageSize(15);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotalRecord(Integer.parseInt(count));
            pageInfo.setTlist(list);
        }
        choiceMap.put("riskLevel", riskLevel);
        choiceMap.put("parentId", parentId);
        choiceMap.put("pageInfo", pageInfo);
        resultMap.put("data", choiceMap);
        resultMap.put("code", "1");
        resultMap.put("msg", "数据访问成功");
        JSONObject jsonObjectMav = new JSONObject(resultMap);
        result = jsonObjectMav.toString();
        return result;
    }


    @OperationLog(
            success = "新增风险分类页面",
            busType = "主题仓库",
            fail = "新增风险分类页面",
            operationType = OperationType.SELECT,
            subType = "风险分类"
    )
    @RequestMapping(value = "/toAddRiskClass", method = {RequestMethod.POST})
    @Operation(summary="新增风险分类页面")
    public String toAddRiskClass(HttpServletRequest request,
                                 @RequestParam(value = "riskLevel", required = true) String riskLevel,
                                 @RequestParam(value = "parentId", required = false) String parentId) throws Exception {
        String result1 = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        RiskClass risk = null;
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        if (Integer.parseInt(riskLevel) != 1) {
            choiceMap.put("riskid", parentId);
            String tree = HttpClient.request(HttpClient.sxjm_findone, choiceMap, null);
            net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);
            String resultone = jsonObject.getString("result");
            if (resultone != null && resultone.equals("true")) {
                String result = jsonObject.getString("entity");
                risk = JSON.parseObject(result, new TypeReference<RiskClass>() {
                });
            }
        }
        resultMap.put("parentId", parentId);
        resultMap.put("risk", risk);
        resultMap.put("riskLevel", riskLevel);
        JSONObject jsonObjectmav = new JSONObject(resultMap);
        result1 = jsonObjectmav.toString();
        return result1;
    }

    @OperationLog(
            success = "新增与修改风险分类",
            busType = "主题仓库",
            fail = "新增与修改风险分类",
            operationType = OperationType.ADD,
            subType = "风险分类"
    )
    @RequestMapping(value = "/addRiskClass", method = {RequestMethod.POST})
    @ResponseBody
    @Operation(summary="新增与修改风险分类")
    public Integer addRiskClass(HttpServletRequest request, RiskClass riskClass,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        // TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        TblStaffUtil staff = userProvider.get();
        riskClass.setOrgid(staff.getCurrentOrg().getOrgid().toString());
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("risk", JSONObject.toJSON(riskClass));
        String tree = HttpClient.request(HttpClient.sxjm_saverisk, choiceMap, null);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);

        String resultone = jsonObject.getString("result");
        if (resultone != null && resultone.equals("true")) {
            return 0;
        }
        return -1;
    }

    /**
     * 获取风险分类的列表,新增报表使用
     *
     * @return
     */

    @OperationLog(
            success = "获取风险分类的列表",
            busType = "主题仓库",
            fail = "获取风险分类的列表",
            operationType = OperationType.SELECT,
            subType = "风险分类"
    )
    @Operation(summary="获取风险分类的列表,新增报表使用")
    @RequestMapping(value = "/ristclass/list")
    public Map<String, Object> ristClassList(HttpServletRequest request, @RequestParam(value = "riskLevel", required = false) String riskLevel,
                                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "parentId", required = false) String parentId,
                                             @RequestParam(value = "riskNumber", required = false) String riskNumber,
                                             @RequestParam(value = "riskclass", required = false) String riskclass,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        // ModelAndView mav = new ModelAndView("view/fxgl_riskClassList");
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

        // TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        TblStaffUtil staff = userProvider.get();
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("parentid", parentId);
        choiceMap.put("level", riskLevel);
        choiceMap.put("pageNumber", pageNumber);
        choiceMap.put("riskNumber", riskNumber);
        choiceMap.put("riskclass", riskclass);
        choiceMap.put("pagesize", "15");
        choiceMap.put("orgid", staff.getCurrentOrg().getOrgid());

        String tree = HttpClient.request(HttpClient.sxjm_bypanrent, choiceMap, null);

        PageInfo<RiskClass> pageInfo = null;
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);

        String resultone = jsonObject.getString("result");
        if (resultone != null && resultone.equals("true")) {
            pageInfo = new PageInfo<RiskClass>();
            String result1 = jsonObject.getString("list");
            String count = jsonObject.getString("count");
            List<RiskClass> list = JSON.parseObject(result1, new TypeReference<List<RiskClass>>() {
            });
            //pageInfo.setPageSize(15);
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            pageInfo.setTotalRecord(Integer.parseInt(count));
            pageInfo.setTlist(list);
        }
        resultMap.put("strid", request.getParameter("strid"));
        resultMap.put("riskLevel", riskLevel);
        resultMap.put("parentId", parentId);
        resultMap.put("orgname_2", request.getParameter("strname"));
        resultMap.put("pageInfo", pageInfo);
        resultMap.put("code", "1");
        resultMap.put("msg", "访问接口成功");
        resultMap.put("data", pageInfo);
        return resultMap;
    }



    @OperationLog(
            success = "修改风险分类页面",
            busType = "主题仓库",
            fail = "修改风险分类页面",
            operationType = OperationType.SELECT,
            subType = "风险分类"
    )
    @RequestMapping(value = "/toModifyRiskClass", method = {RequestMethod.POST})
    @Operation(summary="修改风险分类页面")
    public String toModifyRiskClass(HttpServletRequest request,
                                    @RequestParam(value = "risklevel", required = true) String risklevel,
                                    @RequestParam(value = "parentid", required = false) String parentid,
                                    @RequestParam(value = "riskid", required = false) String riskid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        RiskClass parentrisk = null;
        RiskClass risk = null;
        String tree;
        String resultone;
        net.sf.json.JSONObject jsonObject;
        String result = null;
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        if (Integer.parseInt(risklevel) != 1) {
            choiceMap.put("riskid", parentid);
            tree = HttpClient.request(HttpClient.sxjm_findone, choiceMap, null);
            jsonObject = net.sf.json.JSONObject.fromObject(tree);
            resultone = jsonObject.getString("result");
            if (resultone != null && resultone.equals("true")) {
                result = jsonObject.getString("entity");
                parentrisk = JSON.parseObject(result, new TypeReference<RiskClass>() {
                });
            }
        }

        choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("riskid", riskid);
        tree = HttpClient.request(HttpClient.sxjm_findone, choiceMap, null);
        jsonObject = net.sf.json.JSONObject.fromObject(tree);
        resultone = jsonObject.getString("result");
        if (resultone != null && resultone.equals("true")) {
            result = jsonObject.getString("entity");
            risk = JSONObject.parseObject(result, RiskClass.class);
        }
        resultMap.put("risk", risk);
        resultMap.put("parentId", parentid);
        resultMap.put("parentrisk", parentrisk);
        resultMap.put("riskLevel", risklevel);
        JSONObject jsonResultMap = new JSONObject(resultMap);
        result = jsonResultMap.toString();
        return result;
    }


    @OperationLog(
            success = "判断是否有子集",
            busType = "系统设置",
            fail = "判断是否有子集",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/getChildrenRiskClass", method = {RequestMethod.POST})
    @ResponseBody
    @Operation(summary="判断是否有子集")
    public Integer getChildrenRiskClass(HttpServletRequest request,
                                        @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                        @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                        @RequestParam(value = "riskLevel", required = false) String riskLevel,
                                        @RequestParam(value = "parentId", required = false) String parentId) throws Exception {

        Integer num = 0;

        if (riskLevel == null) {
            riskLevel = "1";
        }
        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("parentid", parentId);
        choiceMap.put("level", riskLevel);
        choiceMap.put("pageNumber", pageNumber);
        choiceMap.put("pagesize", pageSize);
        choiceMap.put("orgid", organization.getOrgid().toString());

        String tree = HttpClient.request(HttpClient.sxjm_bypanrent, choiceMap, null);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);

        String resultone = jsonObject.getString("result");
        if (resultone != null && resultone.equals("true")) {
            String count = jsonObject.getString("count");
            num = Integer.parseInt(count);
        }
        return num;
    }

    @OperationLog(
            success = "删除风险分类",
            busType = "主题仓库",
            fail = "删除风险分类",
            operationType = OperationType.DELETE,
            subType = "风险分类"
    )
    @RequestMapping(value = "/removeRiskClass", method = {RequestMethod.POST})
    @ResponseBody
    @Operation(summary="删除风险分类")
    public Integer removeRiskClass(HttpServletRequest request,
                                   @RequestParam(value = "riskId", required = false) String riskId) throws Exception {
        HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
        choiceMap.put("riskid", riskId);

        String tree = HttpClient.request(HttpClient.sxjm_deleterisk, choiceMap, null);
        net.sf.json.JSONObject jsonObject = net.sf.json.JSONObject.fromObject(tree);

        String resultone = jsonObject.getString("result");
        return Integer.parseInt(resultone);
    }



    @OperationLog(
            success = "文件导入",
            busType = "系统设置",
            fail = "文件导入",
            operationType = OperationType.IMPORT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/importRiskClassExcel", method = RequestMethod.POST)
    @ResponseBody
    @Operation(summary="直接使用InputStream file接收参数也可以/导入")
    public Integer importRiskClassExcel(MultipartFile file,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        InputStream in = file.getInputStream();
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        try {
            TblStaffUtil staff = userProvider.get();
            String orgId = staff.getCurrentOrg().getOrgid().toString();
            RiskClass onerisk = null;
            RiskClass tworisk = null;
            RiskClass threerisk = null;
            XSSFCell cell = null;
            XSSFRow row = null;
            String riskNumber = null;
            String oneContent = null;
            String twoContent = null;
            String threeContent = null;
            String oneNumber = null;
            String twoNumber = null;
            String policybasis = null;
            List<RiskClass> oneList = new ArrayList<RiskClass>(0);
            StringBuffer sbNumber = new StringBuffer("(");
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                //获取工作薄中的工作表
                XSSFSheet sheet = workbook.getSheetAt(i);
                //遍历工作表的行,从第3行开始读数据(索引从0开始)
                for (int x = 2; x < sheet.getPhysicalNumberOfRows(); x++) {
                    row = sheet.getRow(x);
                    //如果此工作表有数据
                    if (row != null) {
                        //遍历每行中每列的数据 row.getPhysicalNumberOfCells()
                        //获取单元格
                        cell = row.getCell(0);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            riskNumber = cell.getStringCellValue();

                        }
                        cell = row.getCell(1);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            oneContent = cell.getStringCellValue();
                        }
                        cell = row.getCell(2);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            twoContent = cell.getStringCellValue();

                        }
                        cell = row.getCell(3);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            threeContent = cell.getStringCellValue();

                        }
                        cell = row.getCell(4);
                        if (cell != null) {
                            //获取单元格的值
                            cell.setCellType(CellType.STRING);//字符串
                            policybasis = cell.getStringCellValue();

                        }
                        if (x > 2 && !riskNumber.substring(0, 2).equals(oneNumber)) {
                            oneList.add(onerisk);
                        }
                        if (oneContent != null && oneContent.trim().length() > 0) {
                            onerisk = new RiskClass();
                            oneNumber = riskNumber.substring(0, 2);
                            sbNumber.append("'" + oneNumber + "',");
                            onerisk.setOrgid(orgId);
                            onerisk.setRisklevel("1");
                            onerisk.setRiskNumber(oneNumber);
                            onerisk.setRiskclass(oneContent);
                        }
                        if (twoContent != null && twoContent.trim().length() > 0) {
                            tworisk = new RiskClass();
                            twoNumber = riskNumber.substring(0, 4);
                            sbNumber.append("'" + twoNumber + "',");
                            tworisk.setOrgid(orgId);
                            tworisk.setRisklevel("2");
                            tworisk.setRiskNumber(twoNumber);
                            tworisk.setRiskclass(twoContent);
                            tworisk.setParentid(oneNumber);
                            onerisk.getChildrenList().add(tworisk);
                        }
                        if (threeContent != null && threeContent.trim().length() > 0) {
                            threerisk = new RiskClass();
                            sbNumber.append("'" + riskNumber + "',");
                            threerisk.setOrgid(orgId);
                            threerisk.setRisklevel("3");
                            threerisk.setRiskNumber(riskNumber);
                            threerisk.setRiskclass(threeContent);
                            threerisk.setParentid(twoNumber);
                            threerisk.setPolicybasis(policybasis);
                            tworisk.getChildrenList().add(threerisk);
                        }
                        if (x == (sheet.getPhysicalNumberOfRows() - 1)) {
                            oneList.add(onerisk);
                        }
                    }
                }
            }
            sbNumber.deleteCharAt(sbNumber.length() - 1);
            sbNumber.append(")");
            String number = sbNumber.toString();
            HashMap<String, Object> choiceMap = new HashMap<String, Object>(0);
            System.out.println(JSONObject.toJSON(oneList));
            choiceMap.put("orgId", orgId);
            choiceMap.put("list", JSONObject.toJSON(oneList));
            choiceMap.put("number", number);
            String tree = HttpClient.request(HttpClient.sxjm_exploreexcel, choiceMap, null);
        } finally {
            //读取完毕则关闭流
            in.close();
            workbook.close();
        }
        return 0;
    }

    @Operation(summary="业务创建访问")
    @RequestMapping(value = "/ywlc/processAnalysis/analysis", method = {RequestMethod.POST})
    public String analysisYwlc(HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<>();
        request.getSession().setAttribute("faflowid", "");
        resultMap.put("msg", "访问成功");
        JSONObject jsonObject = new JSONObject();
        result = jsonObject.toString();
        return result;
    }

    /**
     * 业务创建新-新建
     *
     * @param vo
     * @param token
     * @return
     */


    @OperationLog(
            success = "业务创建左侧-新建",
            busType = "系统设置",
            fail = "业务创建左侧-新建",
            operationType = OperationType.ADD,
            subType = "系统设置"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/add_proces_save", method = {RequestMethod.POST})
    @Operation(summary="业务创建左侧-新建")
    @ResponseBody
    public String add_process_ywlces(TblFlowVo vo, @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.SaveTblFlow(vo, token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 业务创建新-修改
     *
     * @param vo
     * @return
     */

    @OperationLog(
            success = "业务创建左侧-修改",
            busType = "系统设置",
            fail = "业务创建左侧-修改",
            operationType = OperationType.UPDATE,
            subType = "系统设置"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/process_update", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="业务创建左侧-修改")
    @ResponseBody
    public String process_modify_save_ywlc_pro(TblFlowVo vo) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.UpdateTblFlow(vo);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "新建",
            busType = "系统设置",
            fail = "新建",
            operationType = OperationType.ADD,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/process_add", method = {RequestMethod.POST})
    @Operation(summary="系统配置-业务创建-新建")
    public String process_add_ywlc(HttpServletRequest request) {
        String result = null;

            Map<String, Object> resultMap = new HashMap<String, Object>();
            String currentpid = (String) request.getSession().getAttribute("currentProcess");
            if (currentpid == null) {
                currentpid = "0";
            }
            String ff = request.getParameter("ffid");
            if (ff != null && !"".equals(ff)) {
                currentpid = ff;
                request.getSession().setAttribute("currentProcess", ff);
            }
            String orgid = request.getParameter("orgid");
            TblFlow flow = tblFlowService.findById(currentpid);
            String isFlowdb = request.getParameter("isflowdb");
            resultMap.put("msg", "访问成功");
            if (currentpid != null && !"".equals(currentpid) && !"0".equals(currentpid)) {
                resultMap.put("father", flow.getFlowname());
                resultMap.put("commpay", flow.getCompany());
                resultMap.put("folwIdNo", flow.getFlownumber());
            } else {
                TblOrganization attribute1 = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
                if (isFlowdb != null && isFlowdb.equals("1") && !"".equals(isFlowdb)) {
                    if (flow.getFlowname().equals("流程创建")) {
                        resultMap.put("father", "行业流程");
                    } else {
                        resultMap.put("father", flow.getFlowname());
                    }
                } else {
                    resultMap.put("father", "流程创建");
                    String orgids = (String) request.getSession().getAttribute("ogridb");
                    if (orgids != null && !"".equals(orgids)) {
                        resultMap.put("commpay", orgids);
                    } else {
                        resultMap.put("commpay", attribute1.getOrgid());
                    }
                }
            }
            resultMap.put("currentpid", currentpid);
            resultMap.put("orgid", orgid);
            resultMap.put("isFlowdb", isFlowdb);
            JSONObject jsonObjectMV = new JSONObject(resultMap);
            result = jsonObjectMV.toString();
            return result;
    }

    @Operation(summary="业务创建保存前校验")
    @RequestMapping(value = "/ywlc/processAnalysis/save_add", method = {RequestMethod.POST})
    public @ResponseBody
    String del_process_ywlcs(HttpServletRequest request) {
        String num = request.getParameter("num");
        String str = "";
        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
        List list = tblFlowService.findByis(num.trim(), attribute.getOrgid().toString());
        if (list != null && list.size() > 0) {
            str = "0";
        }
        return str;
    }



    @OperationLog(
            success = "修改回显",
            busType = "系统设置",
            fail = "修改回显",
            operationType = OperationType.UPDATE,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/process_modify", method = {RequestMethod.POST})
    @Operation(summary="业务创建左侧-修改回显")
    public String process_modify_ywlc(@Parameter(name = "flowid", description = "flowid", required = true) String flowid,
                                      @Parameter(name = "isflowdb", description = "isflowdb", required = false) String isflowdb) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
            if (flowid != null) {
                TblFlow flow = tblFlowService.findById(flowid);
                TblFlow father = tblFlowService.findById(flow.getFatherflowid().toString());
                if (isflowdb != null && !"".equals(isflowdb) && isflowdb.equals("1")) {
                    if (father == null) {
                        resultMap.put("flowid", flow.getFlowid());
                        resultMap.put("isflowdb", isflowdb);
                        resultMap.put("father", "行业流程");
                    } else {
                        if (father.getFlowname().equals("流程创建")) {
                            resultMap.put("father", "行业流程");
                            resultMap.put("isflowdb", isflowdb);
                        } else {
                            resultMap.put("father", father.getFlowname());
                            resultMap.put("isflowdb", isflowdb);
                        }
                    }
                } else {
                    if (father == null) {
                        resultMap.put("father", "流程创建");
                        resultMap.put("flowid", 0);
                    } else {
                        resultMap.put("father", father.getFlowname());
                        resultMap.put("flowid", flow.getFlowid());
                    }
                }
                resultMap.put("flownumber", flow.getFlownumber());
                resultMap.put("flowname", flow.getFlowname());
                JSONObject jsonObjectMV = new JSONObject(resultMap);
                result = jsonObjectMV.toString();
                return result;
            }
            return null;
    }


    /**
     * 业务创建-启用/弃用
     *
     * @param request
     * @param flowid
     * @param firing
     * @param token
     * @return
     * @throws Exception
     */


    @OperationLog(
            success = "启用/弃用",
            busType = "系统设置",
            fail = "启用/弃用",
            operationType = OperationType.UPDATE,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/changeFiringStatus", method = {RequestMethod.POST})
    @ResponseBody
    @Operation(summary="系统配置-业务创建-启用/弃用。注：三级流程启用 同一个二级流程下只能有一个三级流程启用")
    public Integer ywlc_changeFiringStatus(HttpServletRequest request,
                                           @RequestParam(value = "flowid", required = true) BigDecimal flowid,
                                           @RequestParam(value = "firing", required = true) BigDecimal firing,
                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        Integer result = 0;
        FileOutputStream fileOutStream = null;
        OutputStream os = null;
        ZipOutputStream zipOut = null;
        FileOutputStream fous = null;
        try {
            TblStaffUtil user = userProvider.get();
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", user.getCurrentOrg().getOrgid());
            fields.put("flowId", flowid);
            String resultActiviti = null;
            if (firing.compareTo(BigDecimal.valueOf(1)) == 0) {
                Integer count = this.tblFlowService.findTblFlowStartUpFiringStatus(flowid, firing);
                if (count == 0) {
                    // 流程启用时部署工作流
                    resultActiviti = HttpClient.request(activitiModelerUrl + "/deployActivitiModel", fields, null);
                    JSONObject resultjs = JSONObject.parseObject(resultActiviti);
                    result = Integer.parseInt(resultjs.getString("result"));
                    if (result == 0) {
                        String deployKey = resultjs.getString("deployKey");
                        String modelKey = resultjs.getString("modelKey");
                        byte[] modelData = resultjs.getBytes("moduleData");
                        byte[] picData = resultjs.getBytes("picData");
                        String processName = deployKey + ".bpmn";
                        String imgName = modelKey + "." + deployKey + ".png";
                        //String path = this.getClass().getClassLoader().getResource("/process").getPath();
                        // new一个文件对象用来保存图片，默认保存当前工程根目录
                        File imageFile = new File(filepath + imgName);
                        // 创建输出流
                        fileOutStream = new FileOutputStream(imageFile);
                        // 写入数据
                        fileOutStream.write(picData);

                        /**
                         * 流程文件
                         */
                        File dir = new File(filepath + processName);
                        if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                            dir.mkdirs();
                        }
                        os = new FileOutputStream(filepath + processName);
                        os.write(modelData, 0, modelData.length);
                        os.flush();

                        /**
                         * 流程zip文件
                         */
                        // **这个集合就是你想要打包的所有文件*//*
                        List<File> files = new ArrayList<File>();
                        // 压缩的文件url
                        this.getClass().getResourceAsStream(processName);
                        files.add(new File(filepath + processName));
                        files.add(new File(filepath + imgName));
                        String zipName = deployKey + ".zip";
                        String zipUrl = filepath + zipName;
                        File file = new File(zipUrl);
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        // 创建文件输出流
                        fous = new FileOutputStream(file);
                        zipOut = new ZipOutputStream(new BufferedOutputStream(fous));
                        zipFile(files, zipOut);
                        result = this.tblFlowService.modifyTblFlowFiringStatus(flowid, firing);
                    } else {
                        return result;
                    }
                } else {
                    return -1;
                }
            } else {
                result = this.tblFlowService.modifyTblFlowFiringStatus(flowid, firing);
                resultActiviti = HttpClient.request(activitiModelerUrl + "/deleteActivitiModel", fields, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutStream != null) {
                fileOutStream.close();
            }
            if (os != null) {
                os.close();
            }
            if (zipOut != null) {
                zipOut.close();
            }
            if (fous != null) {
                fous.close();
            }
        }
        return result;
    }

    @Operation(summary="把接受的全部文件打成压缩包")
    public static void zipFile(List<File> files, ZipOutputStream outputStream) {
        int size = files.size();
        for (int i = 0; i < size; i++) {
            File file = (File) files.get(i);
            zipFile((List<File>) file, outputStream);
        }
    }

    @OperationLog(
            success = "删除",
            busType = "系统设置",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/del_process", method = {RequestMethod.POST})
    @Operation(summary="系统配置-业务创建-删除")
    @ResponseBody
    public String del_process_ywlc(@Parameter(name = "flowid", description = "flowid", required = true) String flowid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

            TblStaffUtil user = userProvider.get();
            // 中间表
            TblRiskFlow flowid1 = new TblRiskFlow();
            flowid1.setFlowid(new BigDecimal(flowid));
            flowid1 = tblFlowService.findBysql(flowid);
            if (flowid1 != null) {
                tblFlowService.deleteTblRiskFlow(flowid);
                // 风险表
                tblRiskService.delRisk(flowid1.getRiskid());
            }
            if (flowid != null) {
                List<TblFlowdes> list = tblFlowdesService.returnFlowBysqls(flowid);
                if (list.size() > 0 && list != null) {
                    for (int i = 0; i < list.size(); i++) {
                        TblFlowdes flowdes = (TblFlowdes) list.get(i);
                        tblFlowdesService.delete(flowdes.getFlowdesid().toString());
                    }
                }
                TblFlow flow = tblFlowService.findById(flowid);
                List<TblFlowBussiness> findByFlowud = tblFlowBussinessService.findByFlowud(flowid, null);
                if (findByFlowud != null && findByFlowud.size() > 0) {
                    tblFlowBussinessService.delete(findByFlowud.get(0));
                }
                List<TblFlow> flownumber = tblFlowService.findByFlownumber(flow.getFlowid().toString());
                for (TblFlow tblFlow : flownumber) {
                    tblFlowService.deleteBy(tblFlow);
                }
                /*
                 * 之前流程的自定表单以及activit 工作流 删除 暂时先废弃调
                 *
                 * HashMap<String, Object> fields = new HashMap<String, Object>(0);
                fields.put("flowid", flowid);
                fields.put("orgId", user.getCurrentOrg().getOrgid());
                String result = HttpClient.request(formurl + "/form/dropFormInfoAllByFlowId", fields, null);
                JSONObject resultjs = JSONObject.parseObject(result);
                result = HttpClient.request(activitiModelerUrl + "/deleteActivitiAll", fields, null);*/
                tblFlowService.deleteBy(flow);
            }
            return JsonBean.success();
    }


    @OperationLog(
            success = "流程分类",
            busType = "系统设置",
            fail = "流程分类",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/lcfl", method = {RequestMethod.POST})
    @Operation(summary="业务创建-流程分类")
    public String addanalysisYwlc(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowService.findBylcfl(token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "控制责任人",
            busType = "系统设置",
            fail = "控制责任人",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/nbkz/user/list", method = {RequestMethod.POST})
    @Operation(summary="业务创建-控制责任人右")
    public String userListss(@Parameter(name = "pid", description = "pid", required = false) String pid,
                             @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                             @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblStaffService.findAllPageBeanPid(pid, pageNumber, pageSize, token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 业务创建-保存
     *
     * @param flow
     * @param controlmatrix
     * @param risk
     * @param riskBussiness
     * @param token
     * @return
     * @throws Exception
     */


    @OperationLog(
            success = "保存",
            busType = "系统设置",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/process_analysis_add_save", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="业务创建-保存")
    @ResponseBody
    public String process_analysis_add_save(TblFlow flow, TblControlmatrix controlmatrix, TblRisk risk, TblFlowBussiness riskBussiness,
                                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        Map<String, Object> resultMap = new HashMap<String, Object>();
        String result = null;

            TblStaffUtil user = userProvider.get();
            TblFlow newFlow = new TblFlow();
            newFlow.setInflowdb(flow.getInflowdb());
            newFlow.setCompany(user.getCurrentOrg().getOrgid().toString());
            newFlow.setDepartincharge(flow.getDepartincharge());//责任部门
            newFlow.setFlownumber(flow.getFlownumber());//业务编号
            newFlow.setFlowname(flow.getFlowname());//流程名称
            newFlow.setDepartassist(flow.getDepartassist());//相关部门
            newFlow.setFatherflowid(flow.getFatherflowid());
            newFlow.setFlowbysystem("1");
            SimpleDateFormat fo = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            newFlow.setCreatetime(fo.format(date));
            if (flow.getFlowid() != null) {
                newFlow.setVersion(flow.getVersion() + 1);
                newFlow.setFlowid(flow.getFlowid());
                tblFlowService.updateFlow(newFlow);
                tblFlowService.flowElseUpdate(newFlow, risk, controlmatrix, riskBussiness);
            } else {
                newFlow.setVersion(flow.getVersion());
                newFlow.setFlowid(RandomUtil.uuBigDecimalId());
                tblFlowService.insertFlolw(newFlow);
                // 插入其他对象
                String flowElseInsert = tblFlowService.flowElseInsert(newFlow, risk, controlmatrix, riskBussiness);
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            resultMap.put("data", newFlow);
            JSONObject jsonObject = new JSONObject(resultMap);
            result = jsonObject.toString();
            return result;
    }



    @OperationLog(
            success = "子集修改回显",
            busType = "系统设置",
            fail = "子集修改回显",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/analysis_list_add_modify", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="业务创建-子集修改回显")
    public String analysis_add_modifyYwlc(
            @Parameter(name = "flowid", description = "flowid", required = true) String flowid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();

            try {
                TblStaffUtil user = userProvider.get();
                TblFlow dp = this.tblFlowService.findById(flowid);
                if (dp.getCompany() != null) {
                    TblOrganization organ = tblOrganizaService.findById(dp.getCompany());
                    dp.setComName(organ.getOrgname());
                }
                if (dp.getDepartincharge() != null) {
                    TblOrganization organ = tblOrganizaService.findById(dp.getDepartincharge());
                    dp.setDeparChargeName(organ.getOrgname());
                }
                if (dp.getDepartassist() != null && !dp.getDepartassist().equals("")) {
                    String[] orid = dp.getDepartassist().split(",");
                    String r = "";
                    for (String string : orid) {
                        TblOrganization organ = tblOrganizaService.findById(string);
                        r += organ.getOrgname() + ",";
                    }
                    r = r.substring(0, r.lastIndexOf(","));
                    dp.setDepartissName(r);
                }

                String tcmId = this.tblFlowService.findFlowMatrixByFlowid(dp.getFlowid());
                TblControlmatrix tcl = new TblControlmatrix();
                if (StringUtils.isNotEmpty(tcmId)) {
                    tcl = tblControlmatrixService.getControlmatrix(tcmId);
                }
                List<TblFlow> flows = this.tblFlowService.findByOrgid(user.getCurrentOrg().getOrgid());


                TblFlowBussiness riskBussiness = tblFlowBussinessService.findUniqueByFlowId(dp.getFlowid());
                TblRisk risk = tblRiskService.findTblRiskByFlowId(dp.getFlowid());

                dataMap.put("risk", risk);
                dataMap.put("riskBussiness", riskBussiness);
                dataMap.put("flows", flows);
                dataMap.put("controlMatrix", tcl);
                dataMap.put("dp", dp);
                dataMap.put("activitiModelerUrl", activitiModelerUrl);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
                JSONObject jsonObject = new JSONObject(resultMap);
                result = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }



    @OperationLog(
            success = "内外规列表",
            busType = "系统设置",
            fail = "内外规列表",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/ioanalysis", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内外规列表")
    public String ioanalysisYwlc(@Parameter(description = "flowid", required = false) String flowid,
                                 @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowService.findByOrgidAndFlowidobj(flowid, pageNumber, pageSize);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "内规-新建列表",
            busType = "系统设置",
            fail = "内规-新建列表",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/common/inner_common", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内规-新建列表")
    public String inner_commonCommon(@Parameter(description = "flowid", required = false) String flowid,
                                     @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)  throws Exception {
    	//查找内规分页数据
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowInnerRuleService.findByOrgidAndFlowidobj(flowid, pageNumber, pageSize, token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/save_flow_inner", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内规-新建选择")
    public @ResponseBody
    String save_internal_regulations(@Parameter(name = "flowid", required = true) String flowid,
                                     @Parameter(name = "innerid", required = true) String[] innerid) {
            for (String i : innerid) {
                TblFlowInnerRule inner = new TblFlowInnerRule();
                inner.setFlowid(new BigDecimal(flowid));
                inner.setInnrulid(new BigDecimal(i));
                tblFlowInnerRuleService.saveTblFlowInnerRule(inner);
            }
            return JsonBean.success();
    }

    @RequestMapping(value = "/delete_flow_inner", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="内规-删除")
    public @ResponseBody
    String delete_internal_regulations(@Parameter(name = "flowid", required = false) String flowid,
                                       @Parameter(name = "innerid", required = false) String[] innerid) {
    	//删除内规中间表
        if (flowid != null && innerid != null) {
            for (String i : innerid) {
                TblFlowInnerRule inner = new TblFlowInnerRule();
                inner.setFlowid(new BigDecimal(flowid));
                inner.setInnrulid(new BigDecimal(i));
                tblFlowInnerRuleService.delteTblFlowInnerRule(inner);
            }

        }
        return JsonBean.success();
    }

    @RequestMapping(value = "/common/common_outer", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="外规-新建列表")
    public String common_outer(@Parameter(name = "flowid", required = false) String flowid,
                               @Parameter(name = "status", required = false) String status,
                               @Parameter(name = "name", required = false) String name,
                               @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                               @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
    	//查找外归分页信息
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowInnerRuleService.findOutRuleByOrgidAndFlowid(name, status, flowid, pageNumber, pageSize, token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping(value = "/save_flow_outer", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="外规-新建选择")
    public @ResponseBody
    String save_internal_outers(@Parameter(name = "flowid", required = true) String flowid,
                                @Parameter(name = "outrulid", required = true) String[] outrulid) {
        for (String i : outrulid) {
            TblFlowIOuterrule outer = new TblFlowIOuterrule();
            outer.setFlowid(new BigDecimal(flowid));
            outer.setOutrulid(new BigDecimal(i));
            flowIOuterruleService.saveTblFlowInnerRule(outer);
        }
        return JsonBean.success();
    }

    @RequestMapping(value = "/delete_flow_outer", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="外规-删除")
    public @ResponseBody
    String delete_internal_outer(@Parameter(name = "flowid", required = false) String flowid,
                                 @Parameter(name = "outrulid", required = false) String outrulid) {
    	//只删除中间表
            if (flowid != null && outrulid != null) {
                TblFlowIOuterrule outer = new TblFlowIOuterrule();
                outer.setFlowid(new BigDecimal(flowid));
                outer.setOutrulid(new BigDecimal(outrulid));
                flowIOuterruleService.delteTblFlowInnerRule(outer);
            }
            return JsonBean.success();
    }

    @OperationLog(
            success = "业务创建复制到行业-判断",
            busType = "系统设置",
            fail = "业务创建复制到行业-判断",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/isflow", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="业务创建复制到行业-判断")
    public @ResponseBody
    String isflow(HttpServletRequest request) {
            String selectedId = request.getParameter("selectedId");
            if (selectedId != null && !"".equals(selectedId)) {
                List<TblFlow> flows = tblFlowService.findByfaflowid(selectedId);
                if (flows != null && flows.size() > 0) {
                    return JsonBean.error("该流程不允许复制");
                }

            }
            return JsonBean.success();
    }

    //复制到行业列表

    @OperationLog(
            success = "复制到行业列表",
            busType = "系统设置",
            fail = "复制到行业列表",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @Operation(summary="复制到行业列表")
    @RequestMapping(value = "/ywlc/processAnalysis/to_leftanalysishy", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public String to_leftanalysisYwlcHy(HttpServletRequest request,
                                        @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                                        @Parameter(name = "selectedId", description = "selectedId", required = false) String selectedId,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Map<String, Object> dataMap = new HashMap<String, Object>();
            try {
                TblStaffUtil user = userProvider.get();
                List<TblOrganization> findOrgTree = this.tblOrganizaService.findCurrentOrgHyListInfo(user.getCurrentOrg().getOrgid());
                String findOrgId = null;
                if (orgid != null && !"".equals(orgid)) {
                    findOrgId = orgid;
                } else {
                    TblOrganization org1 = tblOrganizaService.getHYFirst();
                    findOrgId = org1.getOrgid().toString();
                }
                String tree = tblFlowService.flowtreehy(findOrgId);
                if (findOrgId == null) {
                    tree = "";
                }
                String treeName = "";
                if (findOrgId != null) {
                    tblRiskcategoryService.initRiskCategory(findOrgId, TblRiskcategory.HYFXSJK);
                    TblOrganization o = tblOrganizaService.findById(findOrgId);
                    treeName = o.getOrgname();

                }
                dataMap.put("orgTree", findOrgTree);
                dataMap.put("tree", tree);
                dataMap.put("treeName", treeName);
                dataMap.put("targetFrame", "mainFramex");
                dataMap.put("orgid", findOrgId);
                dataMap.put("selectedId", selectedId);
                resultMap.put("code", "1");
                resultMap.put("msg", "数据访问成功");
                resultMap.put("data", dataMap);
                JSONObject jsonObject = new JSONObject(resultMap);
                result = jsonObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "复制到行业选择",
            busType = "系统设置",
            fail = "复制到行业选择",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @PostMapping(value = "/ywlc/processAnalysis/to_ywlcbefore", produces = "application/json; charset=utf-8")
    @Operation(summary="复制到行业选择")
    public @ResponseBody
    String tolcbefore(@Parameter(name = "selectedId", description = "复制的流程id", required = true) String selectedId,
                      @Parameter(name = "faflowid", description = "父级流程", required = true) String faflowid,
                      @Parameter(name = "flownumber", description = "流程编号", required = true) String flownumber,
                      @Parameter(name = "orgid", description = "行业的ID", required = false) String orgid,
                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
            TblStaffUtil user = userProvider.get();
            if (faflowid != null && !"".equals(faflowid) && selectedId != null && !"".equals(selectedId)) {
                TblFlow faflow = tblFlowService.findById(faflowid);//查找父级流程信息
                TblFlow oldflow = tblFlowService.findById(selectedId);//查找需要复制的流程信息
                //创建流程信息
                TblFlow flow = new TblFlow();
                flow.setFlowbysystem(oldflow.getFlowbysystem());
                flow.setFlowname(oldflow.getFlowname());
                flow.setFlownumber(flownumber);
                flow.setFlowstatus(oldflow.getFlowstatus());
                flow.setFlowrange(oldflow.getFlowrange());
                if (orgid != null && orgid != "") {
                    flow.setCompany(orgid);
                    flow.setInflowdb(1);
                } else {
                    flow.setCompany(faflow.getCompany() == null ? user.getCurrentOrg().getOrgid().toString() : faflow.getCompany());
                }
                flow.setDepartassist(oldflow.getDepartassist());
                // flow对象的时间是String类型
                flow.setCreatetime(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
                flow.setAffectdegree(oldflow.getAffectdegree());
                if (oldflow.getDepartincharge() == null || oldflow.getDepartincharge() == "") {
                    flow.setDepartincharge(faflow.getDepartincharge());
                } else {
                    flow.setDepartincharge(oldflow.getDepartincharge());
                }

                flow.setEditor(oldflow.getEditor());
                flow.setVersion(1);
                flow.setFatherflowid(new BigDecimal(faflowid));
                flow.setMemo(oldflow.getMemo());
                flow.setRelatedrules(oldflow.getRelatedrules());
                Set inner = oldflow.getTblInnerrules();// 内规
                Set outer = oldflow.getTblOuterrules();// 外规
                Set risks = oldflow.getTblflowRiskEvents();// 风险信息
                Set tblFlowdeses = oldflow.getTblFlowdeses();// 流程描述

                //复制流程图
                if (oldflow.getFlowchart() != null && !oldflow.getFlowchart().equals("")) {
                    File file = new File(filepath + oldflow.getFlowchart());
                    String url = "/data/flow/" + FxglUtil.getTimeString() + ".xml";

                    File file1 = new File(filepath + url);
                    if (file.exists()) {
                        fileChannelCopy(file, file1);
                        InputStream input = new FileInputStream(filepath + url);
                        FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
                        flow.setFlowchart(url);
                    }
                }
                flow.setFlowid(RandomUtil.uuBigDecimalId());
                tblFlowService.add(flow);

                // 添加关联信息
                for (Object object : risks) {
                    TblRiskevent risk = (TblRiskevent) object;
                    risk.getTblFlowsRisk().add(flow);
                    risk.setRiseveid(RandomUtil.uuBigDecimalId());
                    tblRiskeventService.saveTblRiskevent(risk);
                }

                //复制控制矩阵
                Set tblControlmatrixes = oldflow.getTblControlmatrixes();// 控制矩阵
                if (tblControlmatrixes != null && tblControlmatrixes.size() > 0) {
                    for (Object object : tblControlmatrixes) {
                        TblControlmatrix com = (TblControlmatrix) object;
                        com.getTblFlows().add(flow);
                        com.setConmatid(RandomUtil.uuBigDecimalId());
                        tblControlmatrixService.insertMatrix(com);
                    }
                }

                //复制流程描述
                for (Object object : tblFlowdeses) {
                    TblFlowdes flowdes = (TblFlowdes) object;
                    flowdes.setTblFlow(flow);
                    flowdes.setFlowdesid(RandomUtil.uuBigDecimalId());
                    tblFlowdesService.add(flowdes);
                }

                //复制业务描述
                List<TblFlowBussiness> list = tblFlowBussinessService.findByFlowud(oldflow.getFlowid().toString(), null);
                for (TblFlowBussiness tblFlowBussiness : list) {
                    TblFlowBussiness buss = new TblFlowBussiness();
                    buss.setBussinessdes(tblFlowBussiness.getBussinessdes());
                    buss.setBussinessname(tblFlowBussiness.getBussinessname());
                    buss.setBussinessnumber(tblFlowBussiness.getBussinessnumber());
                    buss.setFlowid(flow.getFlowid());
                    buss.setBussinessid(RandomUtil.uuBigDecimalId());
                    tblFlowBussinessService.save(buss);
                }
                //复制内规
                for (Object object : inner) {
                    TblInnerrule inn = (TblInnerrule) object;
                    TblFlowInnerRule fin = new TblFlowInnerRule();
                    fin.setFlowid(flow.getFlowid());
                    fin.setInnrulid(inn.getInnrulid());
                    tblFlowInnerRuleService.saveTblFlowInnerRule(fin);
                }
                //复制外归
                for (Object object : outer) {
                    TblOuterrule out = (TblOuterrule) object;
                    TblFlowIOuterrule ou = new TblFlowIOuterrule();
                    ou.setFlowid(flow.getFlowid());
                    ou.setOutrulid(out.getOutrulid());
                    flow.getTblOuterrules().add(object);
                    flowIOuterruleService.saveTblFlowInnerRule(ou);
                }

                //复制风险事件
                TblRisk risk = tblRiskService.findTblRiskByFlowId(oldflow.getFlowid());
                String sql = "";
                if (risk != null) {
                    TblRisk newrisk = new TblRisk();
                    newrisk.setRisknumber(risk.getRisknumber());
                    newrisk.setRiskname(risk.getRiskname());
                    newrisk.setRiskdes(risk.getRiskdes());
                    newrisk.setRiskprogram(risk.getRiskprogram());
                    newrisk.setRiskid(RandomUtil.uuBigDecimalId());
                    this.tblRiskService.saveRisk(newrisk);
                    this.tblFlowService.InsertRiskFLOW(flow.getFlowid(), newrisk.getRiskid());
                }

                HashMap<String, Object> fields = new HashMap<String, Object>(0);
                fields.put("oldFlowId", selectedId);
                fields.put("newFlowId", flow.getFlowid());
                String result = HttpClient.request(formurl + "/form/copyFormInfoToNewFlow", fields, null);
                HashMap<String, Object> activitifields = new HashMap<String, Object>(0);
                activitifields.put("oldRiskid", selectedId);
                activitifields.put("newRiskid", flow.getFlowid());
                result = HttpClient.request(activitiModelerUrl + "/activitiModuleCopy", activitifields, null);
            } else {
                return JsonBean.error("复制失败");
            }
            return JsonBean.success();
    }

    /**
     * 从行业复制 列表右
     *
     * @param vo
     * @param token
     * @return
     * @throws Exception
     */

    @OperationLog(
            success = "从行业复制",
            busType = "系统设置",
            fail = "从行业复制",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @PostMapping(value = "/ywlc/processAnalysis/listanalysishy_copy", produces = "application/json; charset=utf-8")
    @Operation(summary="从行业复制")
    public String listanalysishy_copy(CopyVo vo,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowService.finsByPageInfo(vo, token);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "从行业复制选择",
            busType = "系统设置",
            fail = "从行业复制选择",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @PostMapping(value = "/ywlc/processAnalysis/to_ywlc_copybefore", produces = "application/json; charset=utf-8")
    @Operation(summary="从行业复制选择")
    public @ResponseBody
    String to_ywlc_copybefore(@Parameter(name = "flowid", description = "复制的流程id", required = true) String flowid,
                              @Parameter(name = "faflowid", description = "父级流程", required = true) String faflowid,
                              @Parameter(name = "flownumber", description = "流程编号", required = true) String flownumber,
                              @Parameter(name = "orgid", description = "行业的ID", required = false) String orgid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

            if (faflowid != null && !"".equals(faflowid) && flowid != null && !"".equals(flowid)) {
                TblFlow faflow = tblFlowService.findById(faflowid);//查找父级流程信息
                TblFlow oldflow = tblFlowService.findById(flowid);//查找需要复制的流程信息
                //创建新流程
                TblFlow flow = new TblFlow();
                flow.setFlowbysystem(oldflow.getFlowbysystem());
                flow.setFlowname(oldflow.getFlowname());
                flow.setFlownumber(oldflow.getFlownumber());
                flow.setFlowstatus(oldflow.getFlowstatus());
                flow.setFlowrange(oldflow.getFlowrange());
                flow.setCompany(faflow.getCompany());
                flow.setDepartassist(oldflow.getDepartassist());
                flow.setAffectdegree(oldflow.getAffectdegree());
                if (oldflow.getDepartincharge() == null || oldflow.getDepartincharge() == "") {
                    flow.setDepartincharge(faflow.getDepartincharge());
                } else {
                    flow.setDepartincharge(oldflow.getDepartincharge());
                }
                flow.setInflowdb(faflow.getInflowdb());
                flow.setEditor(oldflow.getEditor());
                flow.setVersion(1);
                flow.setFatherflowid(new BigDecimal(faflowid));
                flow.setMemo(oldflow.getMemo());
                flow.setRelatedrules(oldflow.getRelatedrules());
                Set inner = oldflow.getTblInnerrules();// 内规
                Set outer = oldflow.getTblOuterrules();// 外规
                Set risks = oldflow.getTblflowRiskEvents();// 风险信息
                Set tblFlowdeses = oldflow.getTblFlowdeses();// 流程描述

                //复制流程图
                if (oldflow.getFlowchart() != null && !oldflow.getFlowchart().equals("")) {
                    File file = new File(filepath + oldflow.getFlowchart());
                    String url = "/data/flow/" + FxglUtil.getTimeString() + ".xml";

                    File file1 = new File(filepath + url);
                    if (file.exists()) {
                        fileChannelCopy(file, file1);
                        InputStream input = new FileInputStream(filepath + url);
                        FtpUtil.xmlfile(url.substring(url.lastIndexOf("/") + 1), input);
                        flow.setFlowchart(url);
                    }
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                flow.setCreatetime(sdf.format(new Date()));
                flow.setFlowid(RandomUtil.uuBigDecimalId());
                tblFlowService.add(flow);
                //复制风险事件
                for (Object object : risks) {
                    TblRiskevent risk = (TblRiskevent) object;
                    risk.getTblFlowsRisk().add(flow);
                    risk.setRiseveid(RandomUtil.uuBigDecimalId());
                    tblRiskeventService.saveTblRiskevent(risk);
                }
                //复制控制矩阵
                Set tblControlmatrixes = oldflow.getTblControlmatrixes();// 控制矩阵
                if (tblControlmatrixes != null && tblControlmatrixes.size() > 0) {
                    for (Object object : tblControlmatrixes) {
                        TblControlmatrix com = (TblControlmatrix) object;
                        com.getTblFlows().add(flow);
                        com.setConmatid(RandomUtil.uuBigDecimalId());
                        tblControlmatrixService.insertMatrix(com);
                    }
                }
                //复制流程描述
                for (Object object : tblFlowdeses) {
                    TblFlowdes flowdes = (TblFlowdes) object;
                    flowdes.setTblFlow(flow);
                    flowdes.setFlowdesid(RandomUtil.uuBigDecimalId());
                    tblFlowdesService.add(flowdes);
                }
                //复制业务描述
                List<TblFlowBussiness> list = tblFlowBussinessService.findByFlowud(oldflow.getFlowid().toString(), null);
                for (TblFlowBussiness tblFlowBussiness : list) {
                    TblFlowBussiness buss = new TblFlowBussiness();
                    buss.setBussinessdes(tblFlowBussiness.getBussinessdes());
                    buss.setBussinessname(tblFlowBussiness.getBussinessname());
                    buss.setBussinessnumber(tblFlowBussiness.getBussinessnumber());
                    buss.setFlowid(flow.getFlowid());
                    buss.setBussinessid(RandomUtil.uuBigDecimalId());
                    tblFlowBussinessService.save(buss);
                }
                //复制内规
                for (Object object : inner) {
                    TblInnerrule inn = (TblInnerrule) object;
                    TblFlowInnerRule fin = new TblFlowInnerRule();
                    fin.setFlowid(flow.getFlowid());
                    fin.setInnrulid(inn.getInnrulid());
                    tblFlowInnerRuleService.saveTblFlowInnerRule(fin);
                }
                //复制外归
                for (Object object : outer) {
                    TblOuterrule out = (TblOuterrule) object;
                    TblFlowIOuterrule ou = new TblFlowIOuterrule();
                    ou.setFlowid(flow.getFlowid());
                    ou.setOutrulid(out.getOutrulid());
                    flow.getTblOuterrules().add(object);
                    flowIOuterruleService.saveTblFlowInnerRule(ou);
                }

                //复制风险
                TblRisk risk = tblRiskService.findTblRiskByFlowId(oldflow.getFlowid());
                String sql = "";
                if (risk != null) {
                    TblRisk newrisk = new TblRisk();
                    newrisk.setRisknumber(risk.getRisknumber());
                    newrisk.setRiskname(risk.getRiskname());
                    newrisk.setRiskdes(risk.getRiskdes());
                    newrisk.setRiskprogram(risk.getRiskprogram());
                    newrisk.setRiskid(RandomUtil.uuBigDecimalId());
                    this.tblRiskService.saveRisk(newrisk);
                    this.tblFlowService.InsertRiskFLOW(flow.getFlowid(), newrisk.getRiskid());
                }
                HashMap<String, Object> fields = new HashMap<String, Object>(0);
                fields.put("oldFlowId", flowid);
                fields.put("newFlowId", flow.getFlowid());
                String result = HttpClient.request(formurl + "/form/copyFormInfoToNewFlow", fields, null);
                HashMap<String, Object> activitifields = new HashMap<String, Object>(0);
                activitifields.put("oldRiskid", flowid);
                activitifields.put("newRiskid", flow.getFlowid());
                result = HttpClient.request(activitiModelerUrl + "/activitiModuleCopy", activitifields, null);
            } else {
                return JsonBean.error("复制失败");
            }
            return JsonBean.success();
    }

    //xin
    @RequestMapping(value = "/NewfindOrganizationByTreeAll", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="查询组织架构返回tree列表")
    public @ResponseBody
    String findOrganizationByTree(BigDecimal nodeId, String type) {

            if (null == nodeId) {
                TblOrganization org = tblOrganizaService.getHY();
                nodeId = org.getOrgid();
            }
            String json = "";
            if (StringUtils.isNotBlank(type)) {
                List<Tree> list = this.tblOrganizaService.getTreeHy(nodeId);
                json = JSONObject.toJSONString(list);
            } else {
                List<Tree> list = this.tblOrganizaService.getNodeAllHy(nodeId);
                json = JSONObject.toJSONString(list);
            }
            return json;
    }

    @OperationLog(
            success = "验证行业流程是否是三级流程",
            busType = "系统设置",
            fail = "验证行业流程是否是三级流程",
            operationType = OperationType.SELECT,
            subType = "业务创建"
    )
    @RequestMapping(value = "/ywlc/processAnalysis/isflow_del", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="验证行业流程是否是三级流程")
    public @ResponseBody
    String isflow_del(HttpServletRequest request) {
        String selectedId = request.getParameter("selectedId");
            if (selectedId != null && !"".equals(selectedId)) {
                TblFlow flow = tblFlowService.findById(selectedId);
                List<TblFlow> flows = tblFlowService.findByfaflowid(selectedId);
                if (flows != null && flows.size() > 0) {
                    return JsonBean.error("请先删除子级");
                }
            }
            return JsonBean.success();
    }




    @Operation(summary="行业架构首页")
    @RequestMapping(value = "/ywlc/processAnalysis/analysishy_index", method = {RequestMethod.POST})
    public String analysishy_index(HttpServletRequest request) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<>();
        String faflowid = request.getParameter("faflowid");
        resultMap.put("msg", "访问成功");
        resultMap.put("faflowid", faflowid);
        String moduletype = request.getParameter("moduletype");
        resultMap.put("moduletype", moduletype);
        // 为页面查找区域显隐藏赋值
        String choiceSearch = request.getParameter("choiceSearch");
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        resultMap.put("choiceSearch", choiceSearch);
        JSONObject jsonObjectMV = new JSONObject(resultMap);
        result = jsonObjectMV.toString();
        return result;
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

    @OperationLog(
            success = "列表",
            busType = "系统设置",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @RequestMapping(value = "/process/setting", method = {RequestMethod.POST})
    @Operation(summary="流程定义列表")
    public String processSetting(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                 @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        String result = null;
        Map<String, Object> resultMap = this.processSettingService.findByLi(pageNumber, pageSize, token);
        JSONObject jsonObjectVIEW = new JSONObject(resultMap);
        result = jsonObjectVIEW.toString();
        return result;
    }

    @OperationLog(
            success = "新建选择按钮",
            busType = "系统设置",
            fail = "新建选择按钮",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @RequestMapping(value = "/findOrganizationByTreeNbkz", method = {RequestMethod.POST})
    @Operation(summary="流程定义-新建选择按钮")
    public String findOrganizationByTreeNukz(HttpServletRequest request, HttpServletResponse response, HttpSession session,
                                             @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                                             @Parameter(name = "str", description = "str", required = false) String str,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        boolean is = false;
        if (StringUtils.isNotBlank(str) && str.equals("3") && StringUtils.isNotBlank(GROUP_STRUCTURE)) {
            is = true;
        }
        String orgtree = "";
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", staff.getCurrentOrg().getOrgid().toString());
        if (is) {
            try {
                orgtree = HttpClient.request(HttpClient.getDeptString, fields, null);
                if (!StringUtils.isNotBlank(orgtree)) {
                    orgtree = tblOrganizaService.findOrgByAllJT(staff.getCurrentOrg().getOrgid().toString());
                }
            } catch (Exception e) {
                orgtree = tblOrganizaService.findOrgByAllJT(staff.getCurrentOrg().getOrgid().toString());
            }
        } else {
            try {
                orgtree = HttpClient.request(HttpClient.getDeptString, fields, null);
                if (!StringUtils.isNotBlank(orgtree)) {
                    orgtree = tblOrganizaService.findOrgByAll(staff.getCurrentOrg().getOrgid().toString());
                }
            } catch (Exception e) {
                orgtree = tblOrganizaService.findOrgByAll(staff.getCurrentOrg().getOrgid().toString());
            }
        }
        return orgtree;
    }

    @OperationLog(
            success = "保存",
            busType = "系统设置",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "流程定义"
    )
    @RequestMapping(value = "/processSave", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary="流程定义-保存")
    public @ResponseBody
    String cateSave(ProcessSetting setting, HttpServletRequest request,
                    @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                    @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {

            TblStaffUtil staff = userProvider.get();
            if (null != setting.getSettingId()) {
                ProcessSetting processSetting = processSettingService.get(setting.getSettingId());
                if (null != processSetting) {
                    processSetting.setStatus(setting.getStatus());
                    processSetting.setModule(setting.getModule());
                    processSetting.setRemark(setting.getRemark());
                    processSetting.setOrgid(new BigDecimal(orgid));
                    processSetting.setCompanyid(staff.getCurrentOrg().getOrgid());
                    processSettingService.update(processSetting);
                    return JsonBean.success();
                } else {
                    return JsonBean.error();
                }
            } else {
                setting.setStatus("ON");
                setting.setOrgid(new BigDecimal(orgid));
                setting.setCompanyid(staff.getCurrentOrg().getOrgid());
                setting.setSettingId(RandomUtil.uuBigDecimalId());
                processSettingService.savemerge(setting);
                return JsonBean.success();
            }
    }


    @OperationLog(
            success = "删除",
            busType = "系统设置",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "流程定义"
    )
    @RequestMapping(value = "/processdelete", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary="流程定义-删除")
    public @ResponseBody
    String processdelete(ProcessSetting setting, BigDecimal settingId) {
            if (null != settingId) {
                ProcessSetting processSetting = processSettingService.get(settingId);
                if (null != processSetting) {
                    processSettingService.delete(processSetting.getSettingId());
                    return JsonBean.success();
                } else {
                    return JsonBean.error();
                }
            } else {
                return JsonBean.error();
            }
    }


    @OperationLog(
            success = "查看流程定义",
            busType = "系统设置",
            fail = "查看流程定义",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @ResponseBody
    @Operation(summary="查看流程定义")
    @RequestMapping(value = "/definition_list", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public String findProcessDefinition(@Parameter(name = "pageNumber", required = false)@RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @Parameter(name = "pageSize", required = false)@RequestParam(value = "pageSize", required = false) Integer pageSize,
                                        @Parameter(name = "definitionName", description = "definitionName", required = false) @RequestParam(value = "definitionName", required = true) String definitionName) {
        String result = null;
        return result;
    }

    //添加流程
    @Operation(summary="添加流程部署")
    @RequestMapping(value = "/adddeploymentzip", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public String adddeploymentzip(HttpServletRequest request,MultipartFile file, String processname, String processid) throws IOException {
        String attPath = "";
        return JsonBean.success();
    }


    @OperationLog(
            success = "流程实例",
            busType = "系统设置",
            fail = "流程实例",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @RequestMapping(value = "/processInstance", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary="流程定义-流程实例")
    public String processInstance(@Parameter(name = "module", description = "module", required = false) String module,
                                  @Parameter(name = "processDefinitionId", description = "processDefinitionId", required = false) String processDefinitionId,
                                  @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                  @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        String result = null;
        return result;
    }

    @OperationLog(
            success = "历史流程",
            busType = "系统设置",
            fail = "历史流程",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @RequestMapping(value = "/historyProcess", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @Operation(summary="流程定义-历史流程")
    public String historyProcess(@Parameter(name = "module", description = "module", required = false) String module,
                                 @Parameter(name = "processDefinitionId", description = "processDefinitionId", required = false) String processDefinitionId,
                                 @RequestParam(required = false, defaultValue = "1") Integer pageNumber,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        String result = null;
        return result;
    }
    @OperationLog(
            success = "历史流程-删除",
            busType = "系统设置",
            fail = "历史流程-删除",
            operationType = OperationType.DELETE,
            subType = "流程定义"
    )
    @PostMapping(value = "/history_delete")
    @Operation(summary="历史流程-删除")
    public String history_delete(String[] ids, String processDefinitionId, String module) {
        return JsonBean.success();
    }

    /**
     * 删除
     * 流程定义
     *
     * @return
     */


    @OperationLog(
            success = "查看流程定义-删除",
            busType = "系统设置",
            fail = "查看流程定义-删除",
            operationType = OperationType.SELECT,
            subType = "流程定义"
    )
    @PostMapping(value = "/definition_delete")
    @Operation(summary="查看流程定义-删除")
    public String definition_delete(String[] ids, String definitionName) {
        return JsonBean.success();
    }


    @Operation(summary="流程实例删除")
    @RequestMapping(value = "/processInstance_delete", method = {RequestMethod.POST})
    public String processInstance_delete(String[] ids) {
        return processSettingService.deleteProcessInstance(ids);
    }



    @OperationLog(
            success = "导入财务数据excel文档",
            busType = "系统设置",
            fail = "导入财务数据excel文档",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @ResponseBody
    @RequestMapping(value = "/data/excetuImport", method = {RequestMethod.POST})
    @Operation(summary="导入财务数据excel文档 , type = 1 导入信息为科目信息；2凭证相关的信息 3.科目的期初余额表")
    public Integer importDataNew(HttpServletRequest request, HttpServletResponse response,
                                 @RequestParam(value = "accFile", required = false) MultipartFile accFile, @RequestParam(value = "accYear", required = false, defaultValue = "") String accYear,
                                 @RequestParam(value = "pzFile", required = false) MultipartFile pzFile, @RequestParam(value = "pzYear", required = false, defaultValue = "") String pzYear,
                                 @RequestParam(value = "balFile", required = false) MultipartFile balFile, @RequestParam(value = "balYear", required = false, defaultValue = "") String balYear
    ) {
        response.setHeader("Access-Control-Allow-Origin", "*"); //?处理ajax跨域问题 或 ?处理返回前台JSON格式数据问题
        Integer result = -1;
        Integer type = 0;
            TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
            try {
                if (accFile != null) {
                    type = 1;
                    HashMap<String, ArrayList<String[]>> hashMap = ExcelUtil.analysisFile(accFile);
                    result = this.tblOrgExcelService.importFinancData(hashMap, accYear, type, organization);
                }
                if (pzFile != null) {
                    type = 2;
                    HashMap<String, ArrayList<String[]>> hashMap = ExcelUtil.analysisFile(pzFile);
                    result = this.tblOrgExcelService.importFinancData(hashMap, pzYear, type, organization);
                }
                if (balFile != null) {
                    type = 3;
                    HashMap<String, ArrayList<String[]>> hashMap = ExcelUtil.analysisFile(balFile);
                    result = this.tblOrgExcelService.importFinancData(hashMap, balYear, type, organization);
                }
            } catch (OldExcelFormatException excelFormatException) {
                switch (type) {
                    case 1:
                        result = -2;
                        break;
                    case 2:
                        result = -3;
                        break;
                    case 3:
                        result = -4;
                        break;
                    default:
                        break;
                }
                excelFormatException.printStackTrace();
            } catch (NotOLE2FileException notOLE2FileException) {
                switch (type) {
                    case 1:
                        result = -6;
                        break;
                    case 2:
                        result = -7;
                        break;
                    case 3:
                        result = -8;
                        break;
                    default:
                        break;
                }
                notOLE2FileException.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    private ServletConfig config;

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.config = servletConfig;
    }



    @OperationLog(
            success = "列表",
            busType = "机构管理",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "组织架构"
    )
    @RequestMapping(value = "/org", method = {RequestMethod.POST})
    @Operation(summary="机构管理-组织架构")
    public String org(HttpServletRequest request) {
        ModelAndView mv = new ModelAndView("common/zzjg/organ");
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("msg", "访问成功");
        String result = null;
        JSONObject jsonObjectMv = new JSONObject(resultMap);
        result = jsonObjectMv.toString();
        return result;
    }

    @OperationLog(
            success = "登录页列表",
            busType = "系统设置",
            fail = "登录页列表",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/loginTypeList", method = {RequestMethod.POST})
    @Operation(summary="登录页列表")
    public String loginTypeList(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblLoginTypeService.findAll(token, staffId, pageNumber, pageSize);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "保存",
            busType = "登录配置",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "登录管理"
    )
    @RequestMapping(value = "/loginType_save", method = {RequestMethod.POST})
    @Operation(summary="登录页管理--保存")
    public String loginType_save(HttpServletRequest request, TblLoginType logintype,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId
    ) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
            TblStaffUtil staff = userProvider.get();
            try {
                if (logintype.getLoginid() != null) {
                    TblLoginType findByid = tblLoginTypeService.findByid(logintype.getLoginid().toString());
                    logintype.setOrgid(staff.getCurrentOrg().getOrgid().toString());
                    logintype.setLoginid(new BigDecimal(findByid.getLoginid().toString()));
                    tblLoginTypeService.updatetblLoginType(logintype);

                } else {
                    logintype.setOrgid(staff.getCurrentOrg().getOrgid().toString());
                    logintype.setLoginid(RandomUtil.uuBigDecimalId());
                    tblLoginTypeService.save(logintype);
                }
                resultMap.put("code", "1");
                resultMap.put("msg", "成功");
                resultMap.put("data", logintype);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }



    @OperationLog(
            success = "修改查询登录页信息",
            busType = "登录配置",
            fail = "修改查询登录页信息",
            operationType = OperationType.SELECT,
            subType = "登录管理"
    )
    @RequestMapping(value = "/loginType_modi", method = {RequestMethod.POST})
    @Operation(summary="登录页管理修改查询登录页信息")
    public String loginType_modi(@RequestParam(value = "loginid", required = false) String loginid) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblLoginTypeService.findByLoginId(loginid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "删除",
            busType = "登录配置",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "登录管理"
    )
    @RequestMapping(value = "/loginType_del", method = {RequestMethod.POST})
    @Operation(summary="登录页管理-删除")
    public String loginType_del(HttpServletRequest request,
                                @RequestParam(value = "loginid", required = true) String loginid) {

        String result = null;
        try {
            Map<String, Object> resultMap = this.tblLoginTypeService.del(loginid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "列表",
            busType = "课程管理",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "课程类别"
    )
    @RequestMapping(value = "/videoType/list", method = {RequestMethod.POST})
    @Operation(summary="课程类别列表")
    public String nbsjTempleTypeList(HttpServletRequest request,
                                     @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {

        String result = null;
        Map<String, Object> resultMap = this.tblVideoTypeService.findAll(token, staffId, pageNumber, pageSize);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }



    @OperationLog(
            success = "保存",
            busType = "课程管理",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "课程类别"
    )
    @Operation(summary="课程类别保存")
    @RequestMapping(value = "/videoType_save", method = {RequestMethod.POST})
    public @ResponseBody
    String sjtype_save(HttpServletRequest request, TblVideoType tnt,
                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);

            String type = tblVideoTypeService.save(tnt, token, staffId);
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            resultMap.put("data", type);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }



    @OperationLog(
            success = "修改",
            busType = "课程管理",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "课程类别"
    )
    @Operation(summary="课程类别修改")
    @RequestMapping(value = "/videoType_update", method = {RequestMethod.POST})
    public String qxbz_update(HttpServletRequest request,
                              @RequestParam(value = "selectid", required = true) String selectid) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblVideoTypeService.findByid(selectid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "删除",
            busType = "课程管理",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "课程类别"
    )
    @ResponseBody
    @Operation(summary="课程类别删除")
    @RequestMapping(value = "/videoType_delete", method = {RequestMethod.POST})
    public String qxbz_del(HttpServletRequest request,
                           @RequestParam(value = "typeId", required = true) String typeId) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        if (typeId != null && !"".equals(typeId)) {
            tblVideoTypeService.deleteById(typeId);
            resultMap.put("code", "1");
            resultMap.put("msg", "3");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
        }
        resultMap.put("code", "2");
        resultMap.put("msg", "9");
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }



    @OperationLog(
            success = "列表",
            busType = "课程管理",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "课程维护"
    )
    @Operation(summary="课程维护列表")
    @RequestMapping(value = "/videolist", method = {RequestMethod.POST})
    public String videolist(String coursename1, String coursetype1,
                            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "5") Integer pageSize,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.courseService.courseList(pageNumber, pageSize, token, staffId, coursename1, coursetype1);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "新增页面",
            busType = "课程管理",
            fail = "新增页面",
            operationType = OperationType.SELECT,
            subType = "课程维护"
    )
    @Operation(summary="课程维护新增页面")
    @RequestMapping(value = "/video_add", method = {RequestMethod.POST})
    public String videoadd(String id, String pid, String type,
                           @RequestParam(value = "leveltype", required = false) String leveltype,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                           @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblVideoTypeService.findAll(token, staffId);
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            resultMap.put("choiceSearch", choiceSearch);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }




    @OperationLog(
            success = "修改页面",
            busType = "课程管理",
            fail = "修改页面",
            operationType = OperationType.SELECT,
            subType = "课程维护"
    )
    @Operation(summary="课程维护修改页面")
    @RequestMapping(value = "/video_videomodi", method = {RequestMethod.POST})
    public String videomodi(@RequestParam(value = "id", required = false) String id,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                            @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) {
            String result = null;
            try {
                TblCourse tblCourse = courseService.geTblCourse(id);
                Map<String, Object> resultMap = this.tblVideoTypeService.findAll(token, staffId);
                if (choiceSearch == null || "".equals(choiceSearch)) {
                    choiceSearch = "hide";
                }
                resultMap.put("choiceSearch", choiceSearch);
                resultMap.put("tblCourse", tblCourse);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }




    @OperationLog(
            success = "保存并跳转修改页面",
            busType = "课程管理",
            fail = "保存并跳转修改页面",
            operationType = OperationType.SELECT,
            subType = "课程维护"
    )
    @Operation(summary="课程维护-保存并跳转修改页面")
    @RequestMapping(value = "/video_save", method = {RequestMethod.POST})
    public @ResponseBody
    String video_save(HttpServletRequest request, TblCourse tblCourse,
                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                      @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            TblStaffUtil staff = userProvider.get();
            tblCourse.setOrgid(staff.getCurrentOrg().getOrgid());
            tblCourse.setUserid(staff.getStaffid());
            try {
                //模块
                TblVideoType findByName = tblVideoTypeService.findByName(tblCourse.getCoursetype());
                tblCourse.setType(findByName.getType());
                if (tblCourse.getCourseid() != null) {
                    TblCourse geTblCourse = courseService.geTblCourse(tblCourse.getCourseid().toString());
                    tblCourse.setCreateDate(new Date());
                    tblCourse.setCourseid(new BigDecimal(tblCourse.getCourseid().toString()));
                    courseService.updatetblCourse(tblCourse);
                } else {
                    tblCourse.setCreateDate(new Date());
                    tblCourse.setCourseid(RandomUtil.uuBigDecimalId());
                    courseService.savetblCourse(tblCourse);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            resultMap.put("code", "1");
            resultMap.put("msg", "成功");
            resultMap.put("data", tblCourse.getCourseid());
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }



    @OperationLog(
            success = "删除判断",
            busType = "课程管理",
            fail = "删除判断",
            operationType = OperationType.SELECT,
            subType = "课程维护"
    )
    @Operation(summary="课程维护-删除判断")
    @RequestMapping(value = "/video/deletes", method = {RequestMethod.POST})
    public @ResponseBody
    String video_delete(HttpServletRequest request,
                        @Parameter(description = "pid", required = false) String pid) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            if (pid != null && !"".equals(pid)) {
                List<TblCourse> list = courseService.findByFatherid(pid);
                resultMap.put("code", "1");
                resultMap.put("msg", "成功");
                resultMap.put("data", list.size() + "");
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
                return result;
            } else {
                resultMap.put("code", "1");
                resultMap.put("msg", "");
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
                return result;
            }
    }


    @OperationLog(
            success = "删除",
            busType = "课程管理",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "课程维护"
    )
    @Operation(summary="课程维护-删除")
    @RequestMapping(value = "/video_del", method = {RequestMethod.POST})
    public String video_del(HttpServletRequest request, String[] selectedId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        try {
            for (int i = 0; i < selectedId.length; i++) {
                resultMap = this.courseService.deletetblCourse(selectedId[i]);
            }
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }



    @OperationLog(
            success = "列表",
            busType = "信息查询",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "充值记录"
    )
    @Operation(summary="信息查询-充值记录列表")
    @RequestMapping(value = "/deposit_money", method = {RequestMethod.POST})
    public String deposit_money(HttpServletRequest request, TblYyUserOrder yuo,
                                @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                                @Parameter(name = "pageSize", required = false) Integer pageSize,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
            try {
                Map<String, Object> resultMap = this.tblYyUserOrderService.selectPageInfoList(token, staffId, pageNumber, pageSize, yuo);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "列表",
            busType = "信息查询",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "费用标准"
    )
    @Operation(summary="信息查询-费用标准")
    @RequestMapping(value = "/price_cost", method = {RequestMethod.POST})
    public String price_cost(HttpServletRequest request, Find find,
                             @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                             @Parameter(name = "pageSize", required = false) Integer pageSize,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
            try {
                Map<String, Object> resultMap = this.tblyypriceService.findListPage(find, token, staffId, pageNumber, pageSize);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "列表",
            busType = "信息查询",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "统计费用"
    )
    @Operation(summary="信息查询-统计费用")
    @RequestMapping(value = "/price_company", method = {RequestMethod.POST})
    public String price_company(HttpServletRequest request, TblYyUserQuery yuq,
                                @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
            try {
                Map<String, Object> resultMap = this.tblYyOrgDepositService.findCostPircePageInfo(pageNumber, pageSize, yuq, token, staffId);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }


    @OperationLog(
            success = "查询统计费用明细",
            busType = "信息查询",
            fail = "查询统计费用明细",
            operationType = OperationType.SELECT,
            subType = "统计费用"
    )
    @Operation(summary="信息查询-查询统计费用明细")
    @RequestMapping(value = "/findQueryRecodeDetail", method = {RequestMethod.POST})
    public String findQueryRecodeDetail(HttpServletRequest request,
                                        @RequestParam(value = "recordId", required = false) Integer recordId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblYyUserQueryService.selectUserQueryById(recordId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "列表",
            busType = "信息查询",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "个人查询"
    )
    @Operation(summary="信息查询-个人查询")
    @RequestMapping(value = "/select_user", method = {RequestMethod.POST})
    public String sjbbuserLeft_risk(HttpServletRequest request, TblYyUserQuery yuq,
                                    @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                    @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                    @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblYyOrgDepositService.findCostPircePageInfo(pageNumber, pageSize, yuq, token, staffId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "数据导入模板",
            busType = "模板管理",
            fail = "数据导入模板",
            operationType = OperationType.DOWNLOAD,
            subType = "模板下载"
    )
    @GetMapping("/mb/download")
    @Operation(summary="数据导入模板")
    public String fileDownLoad(HttpServletResponse response, @RequestParam(value = "type", required = false) Integer type,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staff = userProvider.get();
        String fileName = this.tblFinanceDataService.findModelName(type, staff.getCurrentOrg().getOrgid());
        File file = new File(filepath + '/' + fileName);
        if (!file.exists()) {
            return "下载文件不存在";
        }
        response.reset();
        fileName = response.encodeURL(new String(fileName.getBytes(), "iso8859-1"));//对中文文件名转码
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setCharacterEncoding("GBK");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));) {
            byte[] buff = new byte[1024];
            OutputStream os = response.getOutputStream();
            int i = 0;
            while ((i = bis.read(buff)) != -1) {
                os.write(buff, 0, i);
                os.flush();
            }
        } catch (IOException e) {
            return "下载失败";
        }
        return "下载成功";
    }


    /**
     * 获取项目根路径
     *
     * @return
     */

    private static String getResourceBasePath() {
        // 获取跟目录
        File path = null;
        try {
            path = new File(ResourceUtils.getURL("classpath:").getPath());
        } catch (FileNotFoundException e) {
            // nothing to do
        }
        if (path == null || !path.exists()) {
            path = new File("");
        }

        String pathStr = path.getAbsolutePath();
        // 如果是在eclipse中运行，则和target同级目录,如果是jar部署到服务器，则默认和jar包同级
        pathStr = pathStr.replace("\\target\\classes", "");

        return pathStr;
    }

    @OperationLog(
            success = "列表",
            busType = "数据采集",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "采集配置"
    )
    @Operation(summary="数据采集-采集配置列表")
    @RequestMapping(value = "/zt/sjcj_list", method = {RequestMethod.POST})
    public String sjcj_list(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            //数据采集列表
            Map<String, Object> resultMap = tblFinanceDataService.findByCompanyId(pageNumber, token, staffId, pageSize);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "删除",
            busType = "数据采集",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "采集配置"
    )
    @Operation(summary="数据采集-采集配置删除")
    @RequestMapping(value = "/zt/sjcj_del", method = {RequestMethod.POST})
    public @ResponseBody
    String sjcj_del(HttpServletRequest request,
                    @RequestParam(value = "orderId", required = false) String orderId) {
        String result = null;
        if (orderId != null && !"".equals(orderId)) {
            Map<String, Object> resultMap = tblFinanceDataService.del(orderId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        }
        return result;
    }

    @OperationLog(
            success = "保存",
            busType = "数据采集",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "采集配置"
    )
    @Operation(summary="数据采集-采集配置保存")
    @RequestMapping(value = "/zt/sjcj_save", method = {RequestMethod.POST})
    public @ResponseBody
    String sjtype_save(HttpServletRequest request, TblFinanceData tlf,
                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                       @Parameter(name = "parseStart", description = "parseStart", required = true) String parseStart,
                       @Parameter(name = "pendDate", description = "pendDate", required = true) String pendDate,
                       @Parameter(name = "sid", description = "sid", required = false) String fid) throws Exception {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
            String tbfinanceDate = tblFinanceDataService.selectDateByCompanyid(request, token, staffId, tlf, parseStart, pendDate, fid);
            return tbfinanceDate;
    }



    @OperationLog(
            success = "修改新建单选",
            busType = "数据采集",
            fail = "修改新建单选",
            operationType = OperationType.SELECT,
            subType = "采集配置"
    )
    @Operation(summary="数据采集-采集配置-修改新建单选")
    @RequestMapping(value = "/financial_version_show", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public void csfa_findOrganizationByTree(HttpServletRequest request, HttpServletResponse response, HttpSession session, String orgid) {
        String str = "";
            TblVersion tblVersion = tblVersionService.findbyFid("100");
            List<TblVersion> chil = tblVersionService.selectAllTblVersion();
            //所有财务软件
            List<Node> childDeptList = new ArrayList<Node>();
            for (TblVersion tblVersion2 : chil) {
                Node tree = new Node();
                tree.setId(tblVersion2.getFid().toString());
                tree.setText(tblVersion2.getFvendor());
                tree.setParentId(tblVersion2.getFatherid().toString());
                childDeptList.add(tree);
            }
            List<Node> children = rebuildList2TreeString(childDeptList);
            Node tree = new Node();
            tree.setChildren(children);
            tree.setText(tblVersion.getFvendor());
            tree.setId(tblVersion.getFid().toString());
            str = tree.toString();
            PrintWriter out = null;
            try {
                response.setCharacterEncoding("UTF-8");
                out = response.getWriter();
                out.write("[" + str + "]");
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                out.flush();
                out.close();
            }
    }

    /**
     * 使用递归方法建树
     */
    private static List<Node> rebuildList2TreeString(List<Node> treeNodes) {
        boolean existRootNode = false;
        List<Node> newTree = new ArrayList<Node>();//初始化一个新的列表
        for (Node treeNode : treeNodes) {
            if (isRootNodeString(treeNode, treeNodes)) {//选择根节点数据开始找儿子
                newTree.add(findChildrenString(treeNode, treeNodes));
                existRootNode = true;
            }
        }
        if (!existRootNode) {//也可能大家都是根节点
            return treeNodes;
        }
        return newTree;
    }

    /**
     * 判断节点是否是根节点
     */
    private static boolean isRootNodeString(Node checkNode, List<Node> treeNodes) {
        for (Node treeNode : treeNodes) {
            if (checkNode.getParentId().equals(treeNode.getId())) {//判断checkNode是不是有爸爸
                return false;
            }
        }
        return true;
    }

    /**
     * 递归查找子节点
     */
    private static Node findChildrenString(Node parentNode, List<Node> treeNodes) {
        List<Node> children = parentNode.getChildren();
        for (Node it : treeNodes) {
            if (parentNode.getId().equals(it.getParentId())) {//找儿子，判断parentNode是不是有儿子
                children.add(findChildrenString(it, treeNodes));
            }
        }
        return parentNode;
    }



    @OperationLog(
            success = "修改前查询页面",
            busType = "数据采集",
            fail = "修改前查询页面",
            operationType = OperationType.SELECT,
            subType = "采集配置"
    )
    @Operation(summary="采集配置-修改前查询页面")
    @RequestMapping(value = "/zt/sjcj_modi", method = {RequestMethod.POST})
    public String sjcj_modi(@RequestParam(value = "selectid", required = true) String selectid) throws ParseException {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;

            TblFinanceData tblFinanceData = tblFinanceDataService.get(selectid);
            SimpleDateFormat format = new SimpleDateFormat("yyyy");
            Date startdate = format.parse(tblFinanceData.getStartdate().toString());
            Date enddate = format.parse(tblFinanceData.getEnddate().toString());
            resultMap.put("startdate", startdate);//可采集开始年份
            resultMap.put("enddate", enddate);//可采集结束年份
            resultMap.put("tlf", tblFinanceData);//采集配置
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }


    @OperationLog(
            success = "列表",
            busType = "数据采集",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "采集策略"
    )
    @Operation(summary="数据采集-采集策略列表")
    @RequestMapping(value = "/zt/getAcInfo", method = {RequestMethod.POST})
    public String getAcInfo(HttpServletRequest request,
                            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                            @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                            @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                            @RequestParam(value = "typeName", required = false) String typeName,
                            @RequestParam(value = "strType", required = false) String strType,
                            @RequestParam(value = "strVal", required = false) String strVal,
                            @RequestParam(value = "sqlStr", required = false) String sqlStr,
                            @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        try {
            TblStaffUtil staff = userProvider.get();
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", staff.getCurrentOrg().getOrgid());
            fields.put("pageNumber", pageNumber);
            fields.put("pageSize", pageSize);
            fields.put("typeName", typeName);
            fields.put("strVal", strVal);
            fields.put("strType", strType);
            fields.put("sqlStr", sqlStr);
            //调用数据采集微服务  分页获取采集策略记录
            String results = HttpClient.request(acurl + "/acquisition/getAcTimeInfo", fields, null);
            //String results = HttpClient.request("http://192.0.2.200:8003/acquisition/getAcTimeInfo", fields, null);
            JSONObject resultjs = JSONObject.parseObject(results);
            PageInfo<TblCwsjDscjInfo> pageInfo = new PageInfo<TblCwsjDscjInfo>();
            //result为true 则 有数据  放入到pageInfo中   ，其他情况没有数据 设置为空
            if ("true".equals(resultjs.get("result").toString())) {
                pageInfo.setTotalRecord(Integer.parseInt(resultjs.get("count").toString()));
                String list = resultjs.get("list").toString();
                List<TblCwsjDscjInfo> tcdjList = JSONObject.parseArray(list, TblCwsjDscjInfo.class);
                pageInfo.setTlist(tcdjList);
            } else {
                List<TblCwsjDscjInfo> list = new ArrayList<TblCwsjDscjInfo>(0);
                pageInfo.setTotalRecord(0);
                pageInfo.setTlist(list);
            }
            if (resultjs.get("sqlStr") != null) {
                pageInfo.setSqlStr(resultjs.get("sqlStr").toString());
            }
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            resultMap.put("pageInfo", pageInfo);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            resultMap.put("choiceSearch", choiceSearch);
            resultMap.put("code", "1");
            resultMap.put("msg", "数据访问成功");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "新增",
            busType = "数据采集",
            fail = "新增",
            operationType = OperationType.SELECT,
            subType = "采集策略"
    )
    @Operation(summary="数据采集-采集策略新增")
    @RequestMapping(value = "/zt/saveAvTimeInfo", method = {RequestMethod.POST})
    public @ResponseBody
    Integer saveAvTimeInfo(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestParam(value = "acName", required = true) String acName,
            @RequestParam(value = "hourStr", required = true) String hourStr,
            @RequestParam(value = "weekStr", required = true) String weekStr) {
        try {

            TblStaffUtil staff = userProvider.get();
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", staff.getCurrentOrg().getOrgid().toString());
            fields.put("infoName", acName);
            fields.put("setDate", hourStr);
            fields.put("acWeek", weekStr);
            fields.put("staffId", staff.getStaffid());
            String result = HttpClient.request(acurl + "/acquisition/insertAcTimeInfo", fields, null);
            if (result.indexOf("success") != -1) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    @OperationLog(
            success = "修改查询",
            busType = "数据采集",
            fail = "修改查询",
            operationType = OperationType.SELECT,
            subType = "采集策略"
    )
    @Operation(summary="数据采集-采集策略-修改查询")
    @RequestMapping(value = "/zt/getAcInfoUnique", method = {RequestMethod.POST})
    public @ResponseBody
    String getAcInfoUnique(
            @RequestParam(value = "infoId", required = true) String infoId) {
        String tcdj = "";
        try {
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("infoId", infoId);
            String result = HttpClient.request(acurl + "/acquisition/selectAcInfoUnique", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);
            if ("true".equals(resultjs.get("result").toString())) {
                tcdj = resultjs.get("tcdj").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tcdj;
    }


    @OperationLog(
            success = "修改保存",
            busType = "数据采集",
            fail = "修改保存",
            operationType = OperationType.UPDATE,
            subType = "采集策略"
    )
    @Operation(summary="数据采集-采集策略-修改保存")
    @RequestMapping(value = "/zt/modifyInfo", method = {RequestMethod.POST})
    public @ResponseBody
    Integer modifyInfo(HttpServletRequest request, TblCwsjDscjInfo tcdj) {
        try {
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("infoId", tcdj.getInfoId());
            fields.put("status", tcdj.getStatus());
            fields.put("setDate", tcdj.getSetDate());
            fields.put("acWeek", tcdj.getAcWeek());
            fields.put("infoName", tcdj.getInfoName());
            String result = HttpClient.request(acurl + "/acquisition/updateAcTimeInfo", fields, null);
            if (result.indexOf("success") != -1) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @OperationLog(
            success = "删除",
            busType = "数据采集",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "采集策略"
    )
    @Operation(summary="数据采集-采集策略-删除")
    @RequestMapping(value = "/zt/removeAvTimeInfo", method = {RequestMethod.POST})
    public @ResponseBody
    Integer removeAvTimeInfo(
            @RequestParam(value = "infoStr", required = true) String infoStr) {
        try {
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("infoStr", infoStr);
            String result = HttpClient.request(acurl + "/acquisition/deleteAcTimeInfo", fields, null);
            if (result.indexOf("success") != -1) {
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }



    @OperationLog(
            success = "列表",
            busType = "数据采集",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "采集展现"
    )
    @Operation(summary="数据采集的采集展现页面")
    @RequestMapping(value = "/show/acquisition", method = {RequestMethod.POST})
    public String showAcquisition(HttpServletRequest request,
    		@Parameter(name = "pageNumber", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1")Integer pageNumber,
            @Parameter(name = "pageSize", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblAcquisitionRecordService.findByPage(pageNumber, pageSize, token, staffId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "采集",
            busType = "数据采集",
            fail = "采集",
            operationType = OperationType.SELECT,
            subType = "采集"
    )
    @ResponseBody
    @Operation(summary="数据采集-采集")
    @RequestMapping(value = "/startAcquisition", method = {RequestMethod.POST})
    public String startAcquisition(@Parameter(name = "startYear", required = true) Integer startYear,
                                   @Parameter(name = "endYear", required = true) Integer endYear, HttpServletRequest request,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        BigDecimal staffid = staff.getStaffid();
        String record = "";
        try {
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            String ip = IpUtil.getIpAddr(request);
            fields.put("startYear", startYear);
            fields.put("endYear", endYear);
            fields.put("orgId", orgid);
            fields.put("staffId", staffid);
            fields.put("ip", ip);
            record = HttpClient.request(acurl + "/acquisition/startAcquisitionByOrgId", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return record;
    }




    @OperationLog(
            success = "列表",
            busType = "模板管理",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "模板下载"
    )
    @ResponseBody
    @Operation(summary="模板下载列表")
    @RequestMapping(value = "/excelimp/kzjz/list", method = {RequestMethod.POST})
    public String kzjz(HttpServletRequest request,
                       @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                       @Parameter(name = "pageSize", required = false) Integer pageSize,
                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        Map<String, Object> resultMap = this.tblImplogService.findByTblImplogList(token, staffId, TblImplog.IMPSTATE_KZJZ, pageNumber, pageSize);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    @OperationLog(
            success = "下载模板",
            busType = "模板管理",
            fail = "下载模板",
            operationType = OperationType.SELECT,
            subType = "模板下载"
    )
    @Operation(summary="模板下载")
    @RequestMapping(value = "/downloadTemplate", method = {RequestMethod.POST})
    public ResponseEntity<byte[]> downloadTemplate(String fileName, HttpServletResponse response) throws IOException {

        //String path = ClassUtils.getDefaultClassLoader().getResource("static").getPath();
        if (fileName.trim().equals("kzjz")) {
            fileName = "内部控制（控制矩阵）导入模板.xls";
        } else if (fileName.trim().equals("nwgd")) {
            fileName = "内部控制（内外规）导入模板.xls";
        } else if (fileName.trim().equals("nbkz")) {
            fileName = "内部控制（基本信息）导入模板.xls";
        } else if (fileName.trim().equals("ng")) {
            fileName = "内部控制（规章制度）导入模板.xls";
        } else if (fileName.trim().equals("wg")) {
            fileName = "内部控制（法律规章）导入模板.xls";
        } else if (fileName.trim().equals("xmzl")) {
            fileName = "智能审计（项目资料准备信息）导入模板.xls";
        } else if (fileName.trim().equals("sxtb")) {
            fileName = "填报模板.xlsx";
        } else if (fileName.trim().equals("zzjg")) {
            fileName = "组织架构导入模板.xls";
        } else if (fileName.trim().equals("jcsj")) {
            fileName = "基础数据导入模板.xls";
        }
//        path = path + separator +fileName;
//        File file = new File(path);
        File file = new File(filepath + '/' + fileName);
        HttpHeaders headers = new HttpHeaders();
        String fileName1 = processFileName(request, fileName);// 为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName1);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set("Content-disposition", "attachment;filename=" + fileName1);
        headers.set("Content-Length", String.valueOf(file.length()));
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }

    public static String processFileName(HttpServletRequest request, String fileNames) {
        String codedfilename = null;
        try {
            Integer index = fileNames.indexOf(".");
            if (index < 0) {
                fileNames = fileNames + ".xls";
            }
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedfilename;
    }



    @OperationLog(
            success = "初始化",
            busType = "系统配置",
            fail = "初始化",
            operationType = OperationType.SELECT,
            subType = "数据初始化"
    )
    @Operation(summary="系统配置-数据初始化")
    @RequestMapping(value = "/org/inihy_list", method = {RequestMethod.POST})
    public String orginiHyList(HttpServletRequest request,
                               @Parameter(name = "pageNumber", description = "当前页数", required = true) Integer pageNumber,
                               @Parameter(name = "pageSize", description = "分页条数", required = true) Integer pageSize,
                               @Parameter(name = "pid", required = false) String pid) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
            TblOrganization organization = tblOrganizaService.findByname("行业");
            if (pid == null || pid.equals("")) {
                pid = organization.getOrgid().toString();
            }
            if (pid == null || pid.equals("")) {
                resultMap = tblOrganizaService.findAllHYOrg(pageNumber, pageSize);
            } else {
                resultMap = tblOrganizaService.findAllHYOrgs(pageNumber, pageSize, pid);
            }
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }

    @OperationLog(
            success = "选择",
            busType = "系统配置",
            fail = "选择",
            operationType = OperationType.SELECT,
            subType = "数据初始化"
    )
    @ResponseBody
    @Operation(summary="系统配置-数据初始化选择")
    @RequestMapping(value = "system/startInitializationData", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public String startInitializationData(HttpServletRequest request,
                                          @Parameter(name = "hyId", description = "hyId", required = false) String hyId,
                                          @Parameter(name = "orgId", description = "orgId", required = false) String orgId,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String results = "1";
        Jedis jedis = JedisUtil.getJedis();
        try {
            TblStaffUtil staff = userProvider.get();
//            TblOrganizationUtil organization = null;
//            TblStaffUtil user = null;
//            if(orgId != null && !"".equals(orgId)){
//                organization = tblOrganizaService.selectFatherOrgIdByID(orgId);
//            }else{
//                organization = staff.getCurrentOrg();
//            }
//
//            if(staffId != null && !"".equals(staffId)){
//                user = tblStaffService.findById(staffId);
//            }else{
//                user = staff;
//            }
            String status = tblOrganizaService.findIniStatus(staff.getCurrentOrg().getOrgid());
            if ("0".equals(status)) {
                results = "0";
            } else {
                tblRiskcategoryService.initRiskCategory(staff.getCurrentOrg().getOrgid().toString(), new TblRiskcategory().FXSJK);
                //userService.initializationSystemData(organization,user,hyId);
                String listorgtreeJSON = RightCatch.getRightForUser(staff.getStaffid().toString());
                jedis.set(RedisFinalUtis.USERMANGERRIGHT + staff.getStaffid().toString(), listorgtreeJSON);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	jedis.close();
        }
        return results;
    }


    @OperationLog(
            success = "列表",
            busType = "系统配置",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="系统配置-模块创建")
    @RequestMapping(value = "/flowModuleList", method = {RequestMethod.GET})
    public String flowModuleList(HttpServletRequest request, TblSystemModule module,
                                 @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                                 @Parameter(name = "pageSize", required = false) Integer pageSize,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId
    ) throws Exception {
        String result = null;
            try {
                Map<String, Object> resultMap = tblSystemModuleService.selectPageInfoByOrgId(pageNumber, pageSize, token, staffId, module);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }



    @OperationLog(
            success = "前往新增页面",
            busType = "系统配置",
            fail = "前往新增页面",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="系统配置-模块创建-前往新增页面")
    @RequestMapping(value = "/gotoFlowModule/insert", method = {RequestMethod.POST})
    public String gotoFlowModuleInsert(HttpServletRequest request,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                                       @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) throws Exception {

        String result = null;
        Map<String, Object> resultMap = this.tblManageRightService.findRightListbyModule(token, staffId);
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        resultMap.put("choiceSearch", choiceSearch);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    @OperationLog(
            success = "选择流程",
            busType = "系统配置",
            fail = "选择流程",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="模块创建--选择流程--创建左侧菜单 (多数据库兼容 未改)")
    @RequestMapping(value = "/flowModule/leftanalysis", method = {RequestMethod.POST})
    public String flowModuleLeftanalysisYwlc(HttpServletRequest request,
                                             @Parameter(name = "treeName", description = "treeName", required = false) String treeName,
                                             @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.flowtree(request, token, staffId, treeName, orgid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //标记


    @OperationLog(
            success = "创建右侧菜单",
            busType = "系统配置",
            fail = "创建右侧菜单",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="模块创建--选择流程--创建右侧菜单")
    @RequestMapping(value = "/flowModule/listanalysis", method = {RequestMethod.POST})
    public String flowModuleListanalysisYwlc(HttpServletRequest request,
                                             @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                                             @Parameter(name = "pageSize", required = false) Integer pageSize,
                                             @Parameter(name = "orgid", description = "所属部门id", required = false) String orgid,
                                             @Parameter(name = "pid", description = "pid", required = false) String pid,
                                             @Parameter(name = "faflowid", description = "faflowid", required = false) String faflowid,
                                             @Parameter(name = "faid", description = "faid", required = false) String faid,
                                             @Parameter(name = "flownumber", description = "flownumber", required = false) String flownumber,
                                             @Parameter(name = "flowname", description = "flowname", required = false) String flowname,
                                             @Parameter(name = "stutes", description = "stutes", required = false) String stutes,
                                             @Parameter(name = "desc", description = "desc", required = false) String desc,
                                             @Parameter(name = "view", description = "view", required = false) String view,
                                             @Parameter(name = "belongsto", description = "belongsto", required = false) String belongsto,
                                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.listBySqlPage(token, staffId, faflowid, flowname, flownumber,
                    stutes, desc, pageNumber, pageSize, belongsto, 1, view);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "保存",
            busType = "系统配置",
            fail = "保存",
            operationType = OperationType.ADD,
            subType = "模块创建"
    )
    @Operation(summary="模块创建-保存")
    @RequestMapping(value = "/flowmodule/saveFlowModule", method = {RequestMethod.POST})
    @ResponseBody
    public String saveFlowModule(HttpServletRequest request, TblSystemModule module, BigDecimal[] flowid,
                                 Integer[] flowOrderNo,
                                 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                 @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        String result = null;
            try {
                Integer count = this.tblSystemModuleService.checkReplay(module, 0, token, staffId);
                if (count > 0) {
                    return JsonBean.error("模块编号或模块名称重复，无法新增");
                }
                Map<String, Object> resultMap = this.tblSystemModuleService.saveEntity(token, staffId, module, flowid, flowOrderNo);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }


    @OperationLog(
            success = "新建流程删除",
            busType = "系统配置",
            fail = "新建流程删除",
            operationType = OperationType.DELETE,
            subType = "模块创建"
    )
    @Operation(summary="模块创建-新建流程删除")
    @RequestMapping(value = "/flowmodule/removeModelFlowRelation", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    @ResponseBody
    public String removeModelFlowRelation(HttpServletRequest request,
                                          @RequestParam(value = "flowIds", required = true) String[] flowIds,
                                          @RequestParam(value = "modelId", required = true) Integer modelId) throws Exception {
        this.tblSystemModuleService.removeModelFlowRelation(flowIds, modelId);
        return JsonBean.success();
    }



    @OperationLog(
            success = "前往修改页面",
            busType = "系统配置",
            fail = "前往修改页面",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @Operation(summary="模块创建前往修改页面")
    @RequestMapping(value = "/gotoFlowModule/goToModify", method = {RequestMethod.POST})
    public String gotoFlowModuleModify(HttpServletRequest request,
                                       @RequestParam(value = "moduleId", required = true) BigDecimal moduleId,
                                       @RequestParam(value = "type", required = false) Integer type,
                                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        String result = null;
            TblSystemModule module = this.tblSystemModuleService.findAllInfoById(moduleId);
            Map<String, Object> resultMap = this.tblManageRightService.findRightListbyModule(token, staffId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }

    @OperationLog(
            success = "修改保存",
            busType = "系统配置",
            fail = "修改保存",
            operationType = OperationType.UPDATE,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="模块创建修改保存")
    @RequestMapping(value = "/flowmodule/modifyFlowModule", method = {RequestMethod.POST})
    public String modifyFlowModule(HttpServletRequest request, TblSystemModule module, BigDecimal[] flowid,
                                   Integer[] flowOrderNo,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        String result = null;
            try {
                Integer count = this.tblSystemModuleService.checkReplay(module, 1, token, staffId);
                if (count > 0) {
                    return "-1";
                }
                Map<String, Object> resultMap = this.tblSystemModuleService.modifyEntity(token, staffId, module, flowid, flowOrderNo);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }


    @OperationLog(
            success = "启用弃用",
            busType = "系统配置",
            fail = "启用弃用",
            operationType = OperationType.UPDATE,
            subType = "模块创建"
    )
    @Operation(summary="模块创建启用弃用")
    @RequestMapping(value = "/flowmodule/modifyFlowModuleStatus", method = {RequestMethod.POST})
    @ResponseBody
    public String modifyFlowModuleStatus(HttpServletRequest request,
                                         @RequestParam(value = "moduleStatus", required = true) Integer moduleStatus,
                                         @RequestParam(value = "moduleId", required = true) BigDecimal moduleId) {
        String result = null;
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            this.tblSystemModuleService.modifyFlowModifyStatus(moduleStatus, moduleId);
            resultMap.put("code", "1");
            resultMap.put("msg", "修改成功！");
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultMap.put("code", "0");
        resultMap.put("msg", "修改失败！");
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();
        return result;
    }

    @OperationLog(
            success = "下发选择",
            busType = "系统配置",
            fail = "下发选择",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @Operation(summary="模块创建下发选择")
    @RequestMapping(value = "/flowModule/gettreeorg", method = {RequestMethod.POST})
    public @ResponseBody
    String flowModuleGetTreeOeg(HttpServletRequest request,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
        //TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgName");// 当前用户的机构
        Jedis jedis = JedisUtil.getJedis();
        TblStaffUtil user = userProvider.get();
        String str = null;
        Tree tree = new Tree();
        try {
            tree.setId(user.getCurrentOrg().getOrgid());
            tree.setName(user.getCurrentOrg().getOrgname());
            tree.setOpen(true);
            tree.setIsParent(true);
            //从缓存中获取公司树结构信息，如果缓存中没有则查询数据库
            if (jedis.exists(RedisFinalUtis.COMPANYBYTREE + user.getCurrentOrg().getOrgid())) {
                str = jedis.get(RedisFinalUtis.COMPANYBYTREE + user.getCurrentOrg().getOrgid());
                List<Tree> treeList = JSON.parseArray(str, Tree.class);
                tree.setChildren(treeList);
            } else {
                List<Tree> treeList = this.treeService.findAllCompanyByTree(user.getCurrentOrg().getOrgid());
                str = JSONObject.toJSONString(treeList);
                tree.setChildren(treeList);
            }
        } finally {
        	jedis.close();
            System.out.println(JSONObject.toJSONString(tree));
            return JSONObject.toJSONString(tree);
        }
    }



    @OperationLog(
            success = "下发",
            busType = "系统配置",
            fail = "下发",
            operationType = OperationType.DISPATCH,
            subType = "模块创建"
    )
    @Operation(summary="模块创建下发 (废弃 重新调整)")
    @RequestMapping(value = "/flowmodule/saveFlowOrganizationRelation", method = {RequestMethod.POST})
    @ResponseBody
    public String saveFlowOrganizationRelation(HttpServletRequest request,
                                               @RequestParam(value = "moduleId", required = true) BigDecimal moduleId,
                                               @RequestParam(value = "orgId", required = true) String orgId) {
        try {
            String[] orgIds = orgId.split(",");

            String modelType = this.tblSystemModuleService.findModelTypeByModuleId(moduleId);
            String rightId = null;
            if ("nk".equals(modelType)) {
                rightId = "2";
            } else if ("fxgl".equals(modelType)) {
                rightId = "70";
            } else if ("fcobc".equals(modelType)) {
                rightId = "235727";
            } else if ("nbsj".equals(modelType)) {
                rightId = "102";
            } else if ("znjk".equals(modelType)) {
                rightId = "103";
            } else {
                rightId = modelType;
            }
            HashMap<String, Object> fields = null;
            Integer count = 0;
            for (String id : orgIds) {
                // 判断是否有1及权限
                count = this.tblSystemModuleService.selectOrgManageRight(id, rightId);
                if (count == 0) {
                    if ("nk".equals(modelType) || "fxgl".equals(modelType) || "nbsj".equals(modelType)
                            || "znjk".equals(modelType)) {
                        this.tblSystemModuleService.saveOrgRightRelation(id, rightId, 1);
                    } else {
                        this.tblSystemModuleService.saveOrgRightRelation(id, rightId, 2);
                    }
                    fields = new HashMap<String, Object>();
                    fields.put("nodeId", id);
                }
                //保存模块和业务流程之间的关系
                this.tblSystemModuleService.dealModuleFlowOrganizationRealtion(moduleId, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonBean.success();
    }


    @OperationLog(
            success = "取消下发选择",
            busType = "系统配置",
            fail = "取消下发选择",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @Operation(summary="模块创建取消下发选择")
    @RequestMapping(value = "/flowModule/cancelOrganizationList", method = {RequestMethod.POST})
    public String cancelOrganizationList(HttpServletRequest request,
                                         @RequestParam(value = "moduleId", required = true) BigDecimal moduleId, TblOrganization tblOrganization,
                                         @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                         @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize) {
        String result = null;
            try {
                Map<String, Object> resultMap = tblOrganizaService.findOrganizationInfoByModuleId(pageNumber, pageSize, tblOrganization, moduleId);
                resultMap.put("moduleId", moduleId);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
    }

    @OperationLog(
            success = "取消下发",
            busType = "系统配置",
            fail = "取消下发",
            operationType = OperationType.SELECT,
            subType = "模块创建"
    )
    @ResponseBody
    @Operation(summary="模块创建取消下发")
    @RequestMapping(value = "/flowModule/removeOrgModuleRelation", method = {RequestMethod.POST})
    public String removeStaffModuleRelation(HttpServletRequest request,
                                            @RequestParam(value = "moduleId", required = true) BigDecimal moduleId,
                                            @RequestParam(value = "orgId", required = true) String orgId) {
        try {
            this.tblSystemModuleService.removeStaffModuleRelation(moduleId, orgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return JsonBean.success();
    }


    @OperationLog(
            success = "编号列表",
            busType = "系统配置",
            fail = "编号列表",
            operationType = OperationType.SELECT,
            subType = "编号设置"
    )
    @Operation(summary="编号列表 (多数据库兼容未改)")
    @RequestMapping(value = "/number_list", method = {RequestMethod.POST})
    public String number_list(HttpServletRequest request,
                              @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                              @RequestParam(value = "pageSize", required = false, defaultValue = "15") Integer pageSize,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                              @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                              @Parameter(name = "fatherrightid", description = "fatherrightid", required = false) String fatherrightid
    ) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        TblStaffUtil staff = userProvider.get();
        BigDecimal orgid = staff.getCurrentOrg().getOrgid();
        //默认查询一级下的编号列表信息
        if (fatherrightid == "1" || fatherrightid == null || fatherrightid == "") {
            resultMap = tblAutonoNumberService.getNumberList(orgid, new BigDecimal(1), pageNumber, pageSize);
        } else {
            resultMap = tblAutonoNumberService.getNumberList(orgid, new BigDecimal(fatherrightid), pageNumber, pageSize);
        }
        JSONObject jsonObj = new JSONObject(resultMap);
        String result = jsonObj.toString();
        return result;
    }



    @OperationLog(
            success = "编号树结构",
            busType = "系统配置",
            fail = "编号树结构",
            operationType = OperationType.SELECT,
            subType = "编号设置"
    )
    @Operation(summary="编号树结构")
    @RequestMapping(value = "/numberTree", method = {RequestMethod.POST})
    public @ResponseBody
    String getnumberTree(BigDecimal nodeId, String type, HttpServletRequest request,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                         @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String json = "";
        try {
            if (nodeId == null) {
                json = this.tblAutonoNumberService.getNumberTree(token, staffId, new BigDecimal(1));
            } else {
                json = this.tblAutonoNumberService.getNumberTree(token, staffId, nodeId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @OperationLog(
            success = "修改",
            busType = "系统配置",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "编号设置"
    )
    @Operation(summary="编号设置-修改")
    @RequestMapping(value = "/updateNumber", method = {RequestMethod.POST})
    public @ResponseBody
    String updateNumber(HttpServletRequest request,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                        @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                        TblOrgNo tblOrgNo) {
        String result = null;
            Map<String, Object> resultMap = tblAutonoNumberService.updateNumber(token, staffId, tblOrgNo);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }



    @OperationLog(
            success = "修改",
            busType = "系统配置",
            fail = "修改",
            operationType = OperationType.UPDATE,
            subType = "编号设置"
    )
    @Operation(summary="流程版本管理列表左")
    @RequestMapping(value = "/ywlc/version_left", method = {RequestMethod.POST})
    public String leftanalysisVersion(HttpServletRequest request,
                                      @Parameter(name = "treeName", description = "treeName", required = false) String treeName,
                                      @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                      @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.flowtreevser(request, token, staffId, treeName, orgid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    @OperationLog(
            success = "流程版本管理列表右",
            busType = "系统设置",
            fail = "流程版本管理列表右",
            operationType = OperationType.SELECT,
            subType = "业务流程"
    )
    @Operation(summary="流程版本管理列表右")
    @RequestMapping(value = "/nbkz/ywlc/list_version", method = {RequestMethod.POST})
    public String list_versionYwlc(HttpServletRequest request,
                                   @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                                   @Parameter(name = "faflowid", description = "faflowid", required = true) String faflowid,
                                   @Parameter(name = "name", description = "name", required = true) String name,
                                   @Parameter(name = "code", description = "code", required = true) String code,
                                   @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowService.findBysqAll(token, staffId, faflowid, pageNumber,
                    pageSize, name, code);
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            resultMap.put("choiceSearch", choiceSearch);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "历史版本列表",
            busType = "系统设置",
            fail = "历史版本列表",
            operationType = OperationType.SELECT,
            subType = "流程版本管理"
    )
    @Operation(summary="流程版本管理历史版本列表")
    @RequestMapping(value = "/ywlc/list_single_version", method = {RequestMethod.POST})
    public String list_single_versionYwlc(HttpServletRequest request, String flowid,
                                          @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                          @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                                          @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch
    ) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblFlowService.findBysqAllversion(token, staffId, flowid, pageNumber, pageSize);
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            resultMap.put("choiceSearch", choiceSearch);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "左侧菜单",
            busType = "系统设置",
            fail = "左侧菜单",
            operationType = OperationType.SELECT,
            subType = "业务流程"
    )
    @Operation(summary="业务流程设置左侧菜单")
    @RequestMapping(value = "/from/processAnalysis/leftanalysis", method = {RequestMethod.POST}, produces = "application/json; charset=utf-8")
    public String leftanalysisYwlc(HttpServletRequest request,
                                   @Parameter(name = "treeName", description = "treeName", required = false) String treeName,
                                   @Parameter(name = "orgid", description = "orgid", required = false) String orgid,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.flowtree(request, token, staffId, treeName, orgid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    //标记

    @OperationLog(
            success = "右侧菜单",
            busType = "系统设置",
            fail = "右侧菜单",
            operationType = OperationType.SELECT,
            subType = "业务流程"
    )
    @ResponseBody
    @Operation(summary="业务流程设置右侧菜单")
    @RequestMapping(value = "/from/processAnalysis/listanalysis", method = {RequestMethod.POST})
    public String listanalysis(HttpServletRequest request,
                               @Parameter(name = "pageNumber", required = false) Integer pageNumber,
                               @Parameter(name = "pageSize", required = false) Integer pageSize,
                               @Parameter(name = "orgid", description = "所属部门id", required = false) String orgid,
                               @Parameter(name = "pid", description = "pid", required = false) String pid,
                               @Parameter(name = "faflowid", description = "faflowid", required = false) String faflowid,
                               @Parameter(name = "faid", description = "faid", required = false) String faid,
                               @Parameter(name = "flownumber", description = "flownumber", required = false) String flownumber,
                               @Parameter(name = "flowname", description = "flowname", required = false) String flowname,
                               @Parameter(name = "stutes", description = "stutes", required = false) String stutes,
                               @Parameter(name = "desc", description = "desc", required = false) String desc,
                               @Parameter(name = "view", description = "view", required = false) String view,
                               @Parameter(name = "departincharge", description = "departincharge", required = false) String departincharge,
                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                               @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.tblFlowService.listBySqlPage(token, staffId, faflowid, flowname, flownumber,
                    stutes, desc, pageNumber, pageSize, departincharge, null, null);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 业务流程设置-选择流程保存
     *
     * @param request
     * @param settingid
     * @param flowid
     * @return
     */

    @OperationLog(
            success = "选择流程保存",
            busType = "系统设置",
            fail = "选择流程保存",
            operationType = OperationType.ADD,
            subType = "业务流程"
    )
    @Operation(summary="业务流程设置-选择流程保存")
    @RequestMapping(value = "/ywlc/saveflow", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    @ResponseBody
    public String saveflow(HttpServletRequest request,
                           @Parameter(name = "settingid", description = "settingid", required = false) BigDecimal settingid,
                           @Parameter(name = "flowid", description = "flowid", required = false) String flowid) {
            if (null != flowid && flowid.length() > 0) {
                TblFlow flow = this.tblFlowService.findById(flowid);
                ProcessSetting processSetting = this.processSettingService.get(settingid);
                if (null != processSetting) {
                    flow.setSettingid(processSetting.getModule());
                    this.tblFlowService.update(flow);
                    return JsonBean.success();
                } else {
                    return JsonBean.error();
                }
            } else {
                return JsonBean.error();
            }
    }


    @OperationLog(
            success = "列表",
            busType = "系统设置",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "合同流程设置"
    )
    @Operation(summary="合同流程设置列表")
    @RequestMapping(value = "/contract/typeOfContractList", method = {RequestMethod.POST})
    public String typeOfContractList(HttpServletRequest request,
                                     @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "choiceTypeName", required = false) String choiceTypeName,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId,
                                     @Parameter(name = "choiceSearch", description = "判断查询框显示还是隐藏默认hide", required = false) String choiceSearch) {
        String result = null;
        try {
            Map<String, Object> resultMap = tblContractTypeofService.findPageInfoList(pageNumber, pageSize, choiceTypeName, token, staffId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "合同管理根据类型-关联流程",
            busType = "系统设置",
            fail = "合同管理根据类型-关联流程",
            operationType = OperationType.SELECT,
            subType = "合同流程设置"
    )
    @Operation(summary="合同管理根据类型-关联流程")
    @RequestMapping(value = "/process/htgl/setting", method = {RequestMethod.POST})
    public String htglprocessSetting(HttpServletRequest request,
                                     @RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
                                     @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                     @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String result = null;
        try {
            Map<String, Object> resultMap = this.processSettingService.findByList(pageNumber, pageSize, token, staffId);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @OperationLog(
            success = "合同管理根据类型-保存关联流程",
            busType = "系统设置",
            fail = "合同管理根据类型-保存关联流程",
            operationType = OperationType.ADD,
            subType = "合同流程设置"
    )
    @Operation(summary="合同管理根据类型-保存关联流程")
    @RequestMapping(value = "/htgl/saveflow", method = {RequestMethod.POST})
    public @ResponseBody
    String savehtgl(HttpServletRequest request,
                    @Parameter(name = "settingid", description = "settingid", required = false) BigDecimal settingId,
                    @Parameter(name = "typeId", description = "typeId", required = false) String typeId) {
            if (null != typeId && typeId.length() > 0) {
                try {
                    TblContractTypeof typeof = tblContractTypeofService.findByid(typeId);
                    ProcessSetting processSetting = processSettingService.get(settingId);
                    if (null != processSetting) {
                        typeof.setSettingid(processSetting.getModule());
                        tblContractTypeofService.updateContractTypeof(typeof);
                        processSettingService.update(processSetting);
                        return JsonBean.success();
                    } else {
                        return JsonBean.error();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    return JsonBean.error();
                }
            } else {
                return JsonBean.error();
            }
    }

    @OperationLog(
            success = "列表",
            busType = "菜单设置",
            fail = "列表",
            operationType = OperationType.SELECT,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-系统权限")
    @RequestMapping(value = "/qxsd/mgnsqlist", method = {RequestMethod.POST}, produces = {"application/json; charset=utf-8"})
    public @ResponseBody
    String qx_gnsqTreeList(HttpServletRequest request,
                           @RequestParam(value = "id", required = false) BigDecimal id,
                           @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        String treeJson = null;
        try {
            Map<BigDecimal, Object> map = new HashMap<BigDecimal, Object>();
            if (nodeId != null) {
                id = nodeId;
            }
            treeJson = tblManageRightService.getTreeListSettingMenu(id, map, token, staffId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return treeJson;
    }


    @OperationLog(
            success = "统权限显示的列表页面子级页面数据",
            busType = "菜单设置",
            fail = "统权限显示的列表页面子级页面数据",
            operationType = OperationType.SELECT,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-系统权限显示的列表页面子级页面数据")
    @RequestMapping(value = "/pri_list", method = {RequestMethod.POST})
    public String pri_list(HttpServletRequest request, BigDecimal fatherrightid,
                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                           @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
            Map<String, Object> resultMap = new HashMap<String, Object>(0);
            List<TblManageRight> findByManageParentId = new ArrayList<TblManageRight>();
            String result = null;
            try {
                findByManageParentId = tblManageRightService.findByManageParentId(fatherrightid, token, staffId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //判断集合是否存在，如果存在列表页面展示，如果列表页面没有，证明没有子级
            if (findByManageParentId.size() > 0) {
                resultMap.put("findByManageParentId", findByManageParentId);
            } else {
                TblManageRight objTblManageRight = tblManageRightService.findById(fatherrightid.toString());
                resultMap.put("objTblManageRight", objTblManageRight);
            }
            resultMap.put("fatherrightid", fatherrightid);
            JSONObject jsonObj = new JSONObject(resultMap);
            result = jsonObj.toString();
            return result;
    }

    @OperationLog(
            success = "新建",
            busType = "菜单设置",
            fail = "新建",
            operationType = OperationType.SELECT,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-新建")
    @RequestMapping(value = "/module_name", method = {RequestMethod.POST})
    public @ResponseBody
    String module_name(HttpServletRequest request, TblManageRight vmr,
                       @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                       @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
            if (vmr != null && vmr.getRightid() != null) {
                TblOrgRightnew orgRight = tblOrgRightService.findByTblOrgRightorgname(token, staffId, vmr.getRightname());
                if (orgRight == null) {
                    return JsonBean.success();
                }
            } else {
                TblManageRight tblManageRight = tblManageRightService.findByRightname(vmr.getRightname());
                if (tblManageRight == null) {
                    return JsonBean.success();
                }
            }
            return JsonBean.error("操作失败");
    }


    @OperationLog(
            success = "跳转到修改页面",
            busType = "菜单设置",
            fail = "跳转到修改页面",
            operationType = OperationType.SELECT,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-跳转到修改页面")
    @RequestMapping(value = "/module_modify", method = {RequestMethod.POST})
    public String module_modify(HttpServletRequest request, TblManageRight vmr,
                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
            try {
                TblManageRight tblManageRight = tblManageRightService.findById(vmr.getRightid().toString());
                TblOrgRightnew right = tblOrgRightService.findByTblOrgRightorgid(token, staffId, vmr.getRightid().toString());
                resultMap.put("right", right);
                resultMap.put("TblManageRight", tblManageRight);
                JSONObject jsonObj = new JSONObject(resultMap);
                result = jsonObj.toString();
            } catch (Exception e) {
            }
            return result;
    }


    @OperationLog(
            success = "启用弃用",
            busType = "菜单设置",
            fail = "启用弃用",
            operationType = OperationType.UPDATE,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-启用弃用")
    @RequestMapping(value = "/module_xgstatus", method = {RequestMethod.POST})
    public @ResponseBody
    String gs(HttpServletRequest request, TblManageRight vmr, String status,
              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
              @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
            if (token == null && staffId == null) {
                return null;
            }
            if (status != null && status.length() > 0) {
                String right = tblOrgRightService.findByTblOrgRightreturnname(token, staffId, vmr);
                TblOrgRightnew orgright = new TblOrgRightnew();
                orgright.setRightid(vmr.getRightid());
                orgright.setIndicatorstatus(status);
                if (right != null) {
                    orgright.setRightname(right);
                }
                tblOrgRightService.updateTblOrgRight(token, staffId, orgright);
                String listorgtreeJSON = tblManageRightService.getRightForUser(token, staffId);
                return JsonBean.success();
            }
            return JsonBean.success("操作失败");
    }


    @OperationLog(
            success = "删除",
            busType = "菜单设置",
            fail = "删除",
            operationType = OperationType.DELETE,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-删除")
    @RequestMapping(value = "/module_del", method = {RequestMethod.POST})
    public String module_del(HttpServletRequest request, TblManageRight vmr,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                             @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) {
        BigDecimal fatherrightid = null;
        if (vmr != null) {
            fatherrightid = vmr.getFatherrightid();
            tblManageRightService.delright(vmr);
        }
        return pri_list(request, fatherrightid, token, staffId);
    }

    //点击右上侧列表的选择，出现左下侧的功能授权

    @OperationLog(
            success = "授权（已弃用）",
            busType = "菜单设置",
            fail = "授权（已弃用）",
            operationType = OperationType.UPDATE,
            subType = "菜单设置"
    )
    @RequestMapping(value = "/qxsd/mgnsqorg", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary="菜单设置-授权（已弃用）")
    public @ResponseBody
    String qx_gnsqorg(HttpServletRequest reques, BigDecimal id,
                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                      @Parameter(name = "staffId", description = "登录用户主键", required = false) String staffId) throws Exception {
            TblStaffUtil staff = userProvider.get();
            TblOrganization org = this.tblOrganizaService.findById(id.toString());
            if (null != org) {
                Map<BigDecimal, Object> map = new HashMap<BigDecimal, Object>();
                List<TblManageRight> right = tblManageRightService.findByorgid(id);
                for (TblManageRight tr : right) {
                    map.put(tr.getRightid(), 1);
                }
                String treeJson = tblOrganizaService.GetTreeOrg(id, map, staff.getCurrentOrg().getOrgid().toString());
                return treeJson;

            }
            return null;
    }


    @Operation(summary="用户授权功能")
    @RequestMapping(value = "/qxsd/qx_saveCD", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    public @ResponseBody
    String qx_save_C(String userid, String pri_id) {
//        logger.info("用户授权功能");
            if (StringUtils.isNotBlank(userid) && StringUtils.isNotBlank(pri_id)) {
                TblOrganization orgn = tblOrganizaService.findById(userid);
                Jedis jedis = JedisUtil.getJedis();//获得redis客户端
                String treeJson = null;
                JSONArray json = null;
                if (null != orgn) {
                    List<TblManageRight> list = new ArrayList<TblManageRight>();
                    System.out.println(pri_id);
                    String[] priid = pri_id.split(",");
                    for (String pri : priid) {
                        TblManageRight tmang = tblManageRightService.findById(pri);
                        if (null != tmang) {
                            list.add(tmang);
                        }
                    }
                    //RightCatch.delRightFororg(orgn.getOrgid().toString());
                    if (pri_id.length() >= 1000) {
                        int index = getFromIndex(",", pri_id, 150);
                        String pri_id1 = pri_id.substring(0, index);
                        String pri_id2 = pri_id.substring(index + 1, pri_id.length() - 1);
                        RightCatch.delRightFororg(orgn.getOrgid().toString(), pri_id1);
                        RightCatch.delRightFororg(orgn.getOrgid().toString(), pri_id2);
                        RightCatch.insertRightForUser(pri_id1, orgn.getOrgid().toString());
                        RightCatch.insertRightForUser(pri_id2, orgn.getOrgid().toString());
                    } else {
                        RightCatch.insertRightForUser(pri_id, orgn.getOrgid().toString());
                    }
                    orgn.getTblManageRights().clear();
                    orgn.getTblManageRights().addAll(list);
                    TblOrgRight right = new TblOrgRight();
                    for (TblManageRight org : list) {
                        right.setOrgid(orgn.getOrgid());
                        right.setRightid(org.getRightid());
                        tblOrgRightService.add(right);
                    }
                    tblOrganizaService.updateorgn(orgn);
                    //tblOrganizaService.add(orgn);

                    try {

                        List<Tree> mrList = tblManageRightService.getOrgFatherRightforRedis(orgn);
                        if (mrList != null) {
                            json = JSONArray.fromObject(mrList);
                            treeJson = json.toString();
                            jedis.set(RedisFinalUtis.MANAGEORGLIST + orgn.getOrgid(), treeJson);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                    	jedis.close();
                    }

                    return JsonBean.success();
                }
                return JsonBean.error("公司不存在");
            }
            return JsonBean.error();
    }


    //子字符串modelStr在字符串str中第count次出现时的下标
    private int getFromIndex(String data, String str, Integer num) {
        Pattern pattern = Pattern.compile(data);
        Matcher findMatcher = pattern.matcher(str);
        //标记遍历字符串的位置
        int indexNum = 0;
        while (findMatcher.find()) {
            indexNum++;
            if (indexNum == num) {
                break;
            }
        }
        System.out.println("字符或者字符串" + str + "第" + num + "次出现的位置为：" + findMatcher.start());
        return findMatcher.start();

    }


    @OperationLog(
            success = "保存（目前已弃用）",
            busType = "菜单设置",
            fail = "保存（目前已弃用）",
            operationType = OperationType.ADD,
            subType = "菜单设置"
    )
    @Operation(summary="菜单设置-保存（目前已弃用）")
    @RequestMapping(value = "/module_save", method = {RequestMethod.POST})
    public String module_save(TblManageRight viewTblManageRight,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        TblStaffUtil staff = null;
            try {
                staff = userProvider.get();

                if (viewTblManageRight.getRightid() == null) {
                    //新建
                    if (viewTblManageRight.getFatherrightid() != null && viewTblManageRight.getFatherrightid().toString().equals("1")) {
                        Map<String, Object> resultMap = new HashMap<String, Object>(0);


                        viewTblManageRight.setRighturl("/fcobc/main");
                        viewTblManageRight.setFuncorder(new BigDecimal(6));
                        tblManageRightService.save(viewTblManageRight);


                        TblOrgRightnew orgright = new TblOrgRightnew();
                        orgright.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright.setRightid(viewTblManageRight.getRightid());
                        orgright.setIndicatorstatus(viewTblManageRight.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright);


                        TblManageRight newright = new TblManageRight();
                        newright.setRightname("工作台");
                        newright.setIndicatorstatus("1");
                        newright.setFatherrightid(viewTblManageRight.getRightid());
                        newright.setRighturl("/fcobc/workSurface?mty=" + viewTblManageRight.getRightid());
                        newright.setFuncorder(new BigDecimal(1));
                        tblManageRightService.save(newright);

                        TblOrgRightnew orgright1 = new TblOrgRightnew();
                        orgright1.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright1.setRightid(newright.getRightid());
                        orgright1.setIndicatorstatus(newright.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright1);


                        TblManageRight newrightsj = new TblManageRight();
                        newrightsj.setRightname("行业数据");
                        newrightsj.setIndicatorstatus("1");
                        newrightsj.setFatherrightid(newright.getRightid());
                        newrightsj.setRighturl("submenuXMSJ44");
                        newrightsj.setFuncorder(new BigDecimal(1));
                        tblManageRightService.save(newrightsj);


                        TblOrgRightnew orgright2 = new TblOrgRightnew();
                        orgright2.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright2.setRightid(newrightsj.getRightid());
                        orgright2.setIndicatorstatus(newrightsj.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright2);


                        TblManageRight newrightwyj = new TblManageRight();
                        newrightwyj.setRightname("望远镜");
                        newrightwyj.setIndicatorstatus("1");
                        newrightwyj.setFatherrightid(newrightsj.getRightid());
                        newrightwyj.setRighturl("/common/wangyuanjing?mty=" + viewTblManageRight.getRightid());
                        newrightwyj.setFuncorder(new BigDecimal(1));
                        tblManageRightService.save(newrightwyj);


                        TblOrgRightnew orgright3 = new TblOrgRightnew();
                        orgright3.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright3.setRightid(newrightwyj.getRightid());
                        orgright3.setIndicatorstatus(newrightwyj.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright3);


                        TblManageRight newrightqysj = new TblManageRight();
                        newrightqysj.setRightname("企业数据");
                        newrightqysj.setIndicatorstatus("1");
                        newrightqysj.setFatherrightid(newright.getRightid());
                        newrightqysj.setRighturl("submenuXMSJ46");
                        newrightqysj.setFuncorder(new BigDecimal(2));
                        tblManageRightService.save(newrightqysj);

                        TblOrgRightnew orgright4 = new TblOrgRightnew();
                        orgright4.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright4.setRightid(newrightqysj.getRightid());
                        orgright4.setIndicatorstatus(newrightqysj.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright4);


                        TblManageRight newrightzbsj = new TblManageRight();
                        newrightzbsj.setRightname("账簿数据");
                        newrightzbsj.setIndicatorstatus("1");
                        newrightzbsj.setFatherrightid(newrightqysj.getRightid());
                        newrightzbsj.setRighturl("/prjdata/zbsj?mty=" + viewTblManageRight.getRightid());
                        newrightzbsj.setFuncorder(new BigDecimal(2));
                        tblManageRightService.save(newrightzbsj);


                        TblOrgRightnew orgright5 = new TblOrgRightnew();
                        orgright5.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright5.setRightid(newrightzbsj.getRightid());
                        orgright5.setIndicatorstatus(newrightzbsj.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright5);


                        TblManageRight newrightzbgl = new TblManageRight();
                        newrightzbgl.setRightname("账薄管理");
                        newrightzbgl.setIndicatorstatus("1");
                        newrightzbgl.setFatherrightid(newrightqysj.getRightid());
                        newrightzbgl.setRighturl("/common/listBook?pageNumber=1&mty=" + viewTblManageRight.getRightid());
                        newrightzbgl.setFuncorder(new BigDecimal(1));
                        tblManageRightService.save(newrightzbgl);

                        TblOrgRightnew orgright6 = new TblOrgRightnew();
                        orgright6.setOrgid(staff.getCurrentOrg().getOrgid());
                        orgright6.setRightid(newrightzbgl.getRightid());
                        orgright6.setIndicatorstatus(newrightzbgl.getIndicatorstatus());
                        tblOrgRightService.saveTblOrgRight(orgright6);

                        TblOrganization orgn = tblOrganizaService.findById(staff.getCurrentOrg().getOrgid().toString());
                        Jedis jedis = JedisUtil.getJedis();//获得redis客户端
                        List<TblManageRight> oragnAll = new ArrayList<TblManageRight>();
                        oragnAll.add(viewTblManageRight);
                        oragnAll.add(newright);
                        oragnAll.add(newrightsj);
                        oragnAll.add(newrightwyj);
                        oragnAll.add(newrightqysj);
                        oragnAll.add(newrightzbsj);
                        oragnAll.add(newrightzbgl);
                        //orgn.getTblManageRights().addAll(oragnAll);
                        TblOrgRight right = new TblOrgRight();
                        for (TblManageRight org : oragnAll) {
                            right.setOrgid(staff.getCurrentOrg().getOrgid());
                            right.setOrgid(org.getRightid());
                            tblOrgRightService.add(right);
                        }
                        tblOrganizaService.updateorgn(orgn);
                        String treeJson = null;
                        JSONArray json = null;
                        try {
                            List<Tree> mrList = tblManageRightService.getOrgFatherRightforRedis(orgn);
                            if (mrList != null) {
                                json = JSONArray.fromObject(mrList);
                                treeJson = json.toString();
                                jedis.set(RedisFinalUtis.MANAGEORGLIST + orgn.getOrgid(), treeJson);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            JedisUtil.returnResource(jedis);
                        }


                        //List<TblManageRight> userAll = tblManageRightService.findByUserAll(staff.getStaffid().toString());
                        List<TblManageRight> userAll = new ArrayList<TblManageRight>();
                        userAll.add(viewTblManageRight);
                        userAll.add(newright);
                        userAll.add(newrightsj);
                        userAll.add(newrightwyj);
                        userAll.add(newrightqysj);
                        userAll.add(newrightzbsj);
                        userAll.add(newrightzbgl);

//                    staff.getTblManageRights().clear();
                        // staff.getTblManageRights().addAll(userAll);
                        TblManageUserRight userRight = new TblManageUserRight();
                        for (TblManageRight userR : userAll) {
                            userRight.setRightid(userR.getRightid());
                            userRight.setStaffid(staff.getStaffid());
                            tblManageRightService.inserUserRight(userRight);
                        }
                        Jedis jedi = JedisUtil.getJedis();//获得redis客户端
                        String listorgtreeJSON = tblManageRightService.getRightForUser(staff.getStaffid().toString(), staff.getCurrentOrg().getOrgid().toString());
                        jedi.set(RedisFinalUtis.USERMANGERRIGHT + staff.getStaffid().toString(), listorgtreeJSON);
                        JedisUtil.returnResource(jedi);
                    }


                } else {
                    //TblManageRight tblManageRight = tblManageRightService.findById(viewTblManageRight.getRightid().toString());
                    TblOrgRightnew orgright = new TblOrgRightnew();
                    orgright.setOrgid(staff.getCurrentOrg().getOrgid());
                    orgright.setRightid(viewTblManageRight.getRightid());
                    orgright.setIndicatorstatus(viewTblManageRight.getIndicatorstatus());
                    orgright.setRightname(viewTblManageRight.getRightname());

                    tblOrgRightService.updateTblOrgRights(orgright);
                    tblManageRightService.updateright(viewTblManageRight);


                    Jedis jedis = JedisUtil.getJedis();//获得redis客户端
                    String listorgtreeJSON = tblManageRightService.getRightForUser(staff.getStaffid().toString(), staff.getCurrentOrg().getOrgid().toString());
                    jedis.set(RedisFinalUtis.USERMANGERRIGHT + staff.getStaffid().toString(), listorgtreeJSON);
                    jedis.close();
                }

                //mav.setViewName("redirect:/tygl/pri_list?fatherrightid="+viewTblManageRight.getFatherrightid());
            } catch (Exception e) {
            }
            return JsonBean.success();
    }


    @Operation(summary="上传图片接口")
    @RequestMapping(value = "/setting/upload", method = {RequestMethod.POST})
    public String upload(MultipartFile file) {
        String url = fileManager.upload(file);
        return url;
    }

    @Operation(summary="文件上传")
    @PostMapping("/upload")
    public String fileUpload(MultipartFile file) {
        String attPath = "";
        String imageName;
        try {
            String fileName = file.getOriginalFilename();
            long timeInMillis = Calendar.getInstance().getTimeInMillis();
            String oldname = fileName.substring(0, fileName.lastIndexOf("."));
            imageName = fileName.replace(oldname, "" + timeInMillis);
            attPath = FtpUtil.uploadFilePath(imageName + "", file.getInputStream());
            if (StringUtils.isEmpty(attPath)) {
                return JsonBean.error("文件上传失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonBean.error("文件上传失败");
        }

        //返回当前添加的文件 前端回显
        return attPath + imageName;
    }


    @OperationLog(
            success = "用户选择部门获取左侧公司tree",
            busType = "系统设置",
            fail = "用户选择部门获取左侧公司tree",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/redisorg/getAllCompanyTree", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="用户选择部门获取左侧公司tree")
    public JsonBean redisorgfindAllCompanyTree(HttpServletRequest request,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "nodeId", description = "公司主键ID，初始访问为空", required = false) @RequestParam(value = "nodeId", required = false) BigDecimal nodeId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        //如果传入的主键公司为空则获取 最高级公司的主键
        if(nodeId == null) {
        	nodeId = this.tblOrganizaService.findRootCompanyId(loginStaff.getLinkOrg());
        }
        String str;
        HashMap<String, Object> fields = new HashMap<String, Object>();
        fields.put("nodeId", nodeId);
        try {
        	//调用组织架构微服务获取数据
        	str = HttpClient.request(HttpClient.getOrgUrl, fields, null);
            if (!StringUtils.isNotBlank(str)) {
            	List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
                str = JSONObject.toJSONString(list);
            }
        } catch (Exception e) {
        	//调用组织架构缓存微服务失败，进行查询；
        	List<Tree> list = this.tblOrganizaService.getJTNodeAllGS(nodeId);
            str = JSONObject.toJSONString(list);
        }
    	return ResponseFormat.retParam(1, 200, str);
    }


    @OperationLog(
            success = "组织架构列表",
            busType = "系统设置",
            fail = "组织架构列表",
            operationType = OperationType.SELECT,
            subType = "系统设置"
    )
    @RequestMapping(value = "/redisorg/getDetpList", method = {RequestMethod.GET})
    @Operation(summary="组织架构列表")
    public JsonBean redisorgGetDetpList(HttpServletRequest request, Find find,
    					@Parameter(name = "pageNumber",description = "当前页数", required = false)@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber,
                        @Parameter(name = "pageSize",description = "每页数量", required = false)@RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                        @Parameter(name = "pid",description = "公司主键ID，初始访问为空", required = false)@RequestParam(value = "pid", required = false)  BigDecimal pid
    ) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
      	 if (loginStaff == null) {
               return ResponseFormat.retParam(0, 20006, null);
           }
      	 Map<String, Object> resultMap = null;
              try {
            	  //获取最高级公司的主键
                  if (pid == null) {
                      pid = this.tblOrganizaService.findRootCompanyId(loginStaff.getLinkOrg());
                  }
                  //分页获取当前公司下的所有部门数据
                 resultMap = tblOrganizaService.findAllOrgBM(pageNumber, pageSize, find, pid);
              } catch (Exception e) {
                  e.printStackTrace();
              }
          return ResponseFormat.retParam(1, 200, resultMap);
    }

    @OperationLog(
            success = "用户选择部门获取左侧公司tree",
            busType = "权限管理",
            fail = "用户选择部门获取左侧公司tree",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/org/getDeptInfoDetial", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="用户选择部门获取左侧公司tree")
    public JsonBean getDeptInfoDetial(HttpServletRequest request,
                                               @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                               @Parameter(name = "orgId", description = "部门主键ID", required = true) @RequestParam(value = "orgId", required = true) String orgId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        TblOrganization dept = this.tblOrganizaService.findById(orgId);

        return ResponseFormat.retParam(1, 200, dept);
    }


    @OperationLog(
            success = "左侧获取全集团信息",
            busType = "权限管理",
            fail = "左侧获取全集团信息",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/manageUser/getAllCompanyInfo", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary="用户管理左侧获取全集团信息")
    public JsonBean mangeUserGetAllCompanyInfo(HttpServletRequest request,
        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
        @Parameter(name = "orgId", description = "公司主键", required = false) @RequestParam(value = "orgId", required = false,defaultValue="-1") String orgId,
        @Parameter(name = "orgName", description = "筛选条件-公司名称", required = false) @RequestParam(value = "orgName", required = false) String orgName) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
        if (loginStaff == null) {
            return ResponseFormat.retParam(0, 20006, null);
        }
        return this.tblOrganizaService.getallCompanyInfoTree(orgId,orgName);

    }


    @OperationLog(
            success = "角色分配公司获取所有公司数据",
            busType = "权限管理",
            fail = "角色分配公司获取所有公司数据",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/role/getCompanyListToGrantRole", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
	 @Operation(summary="角色分配公司获取所有公司数据")
	 public JsonBean getCompanyListToGrantRole(HttpServletRequest request,
	        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	        @Parameter(name = "orgId", description = "公司主键", required = false) @RequestParam(value = "orgId", required = false,defaultValue="-1") String orgId,
	        @Parameter(name = "orgName", description = "筛选条件-公司名称", required = false) @RequestParam(value = "orgName", required = false) String orgName,
	        @Parameter(name = "roleId", description = "角色主键", required = true) @RequestParam(value = "roleId", required = true) BigDecimal roleId) throws Exception {
	    	TblStaffUtil loginStaff = userProvider.get();
	        if (loginStaff == null) {
	            return ResponseFormat.retParam(0, 20006, null);
	        }
	        return this.tblOrganizaService.getallCompanyInfoTreeToGrantRole(orgId,orgName,roleId);

	}

    @Resource
    private TblStaffMapper tblStaffMapper;


    @OperationLog(
            success = "查询部门负责人",
            busType = "权限管理",
            fail = "查询部门负责人",
            operationType = OperationType.SELECT,
            subType = "用户管理"
    )
    @RequestMapping(value = "/getOrgMainByOrgid", method = {RequestMethod.POST})
    @Operation(summary="查询部门负责人")
    public String orgSave(HttpServletRequest request, TblOrganization org,
                          @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                          @Parameter(name = "orgid", required = false) Integer orgid) throws Exception {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        String result = null;
        TblStaffUtil staff = userProvider.get();


        TblStaff bmfzr = tblStaffMapper.findByStaffManOrgs(orgid+"");

        resultMap.put("code", "1");
        resultMap.put("msg", "成功");
        resultMap.put("data", bmfzr);
        JSONObject jsonObj = new JSONObject(resultMap);
        result = jsonObj.toString();

        return result;
    }

}



