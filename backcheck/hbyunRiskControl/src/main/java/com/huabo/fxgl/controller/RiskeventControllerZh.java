package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.ImportOrExportExcelUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskClaim;
import com.huabo.fxgl.entity.RiskRectifysolution;
import com.huabo.fxgl.entity.Riskevent;
import com.huabo.fxgl.mapper.RiskeventMapper;
import com.huabo.fxgl.service.IAttachmentService;
import com.huabo.fxgl.service.IRiskClaimService;
import com.huabo.fxgl.service.IRiskRectifysolutionService;
import com.huabo.fxgl.service.IRiskeventService;
import com.huabo.fxgl.util.FiexibleNameAssignment;
import com.huabo.fxgl.vo.fieldOrgStaffId;
import com.huabo.fxgl.vo.fieldOrgStaffName;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;


/**
 * <p>
 *  风险事件管理 - 风险事件处理--中核版本
 *  中核版本要求风险事件增加版本处理
 * </p>
 *
 * @version 1.0.1
 * @author CJ
 * @since 2024-11-02
 */
@RestController
@RequestMapping(value = "riskEventZh", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="风险事件管理 - 风险事件处理中核",description="风险事件管理 - 风险事件处理中核")
@Slf4j
public class RiskeventControllerZh {
    @Autowired
    private IRiskeventService riskEventService;

    @Autowired
    private IAttachmentService attachmentService;

    @Autowired
    private IRiskClaimService iRiskClaimService;

    @Autowired
    private RiskeventMapper riskeventMapper;
    
    @Resource
    private UserProvider userProvider;

    @Value("${application.administrators:}")
	private String administrators;
    /*
     status:ok
    * @author zuoshun
    * @version v1.0.1
    * @Description 风险分类树型结构
    * @Date 2022/8/2
    * @param orgid
    * @param hbOrgEntity
    * @param treeName
    * @return com.hbfk.util.JsonBean
    * @url:http://localhost:8081/riskevent/fxsj/query/risk_query_left?orgid=456293
    **/
	@OperationLog(
			success = "左侧风险数据树查询处理成功",
			busType = "风险事件管理",
			fail = "左侧风险数据树查询处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "左侧风险数据树查询/riskeventZh/fxsj/query/risk_query_left")
    @RequestMapping("/fxsj/query/risk_query_left")
    public JsonBean riskQueryLeft(@Parameter(description="orgid")@RequestParam(required = false) String orgid,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                  @Parameter(description="treeName") @RequestParam(required = false) String treeName) throws Exception {
        return  riskEventService.riskQueryLeft(orgid,token,treeName);
    }



    /* pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 风险事件全查
     * @Date 2022/8/2
     * @param null  BF14600551D72E393B38CDE1A6710721
     * @return
     * @url:http://localhost:8081/riskevent/disposalmanage?orgid=666666&startDate=2019-11-06&endDate=2019-11-06
     **/
	@OperationLog(
			success = "查询风险事件处理成功",
			busType = "风险事件管理",
			fail = "查询风险事件处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @Operation(summary = "查询风险事件/riskeventZh/disposalmanage")
    @ResponseBody
    @RequestMapping("/disposalmanage")
    public JsonBean disposalmanage(@Parameter(description="riskcatid") @RequestParam(required = false)String riskcatid,
                                   @Parameter(description="orgid") @RequestParam(required = false)String orgid,
                                   @Parameter(description="find") Find find,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(description="pageNumber")  @RequestParam(required = false,defaultValue = "0")Integer pageNo,
                                   @Parameter(description="pageSize") @RequestParam(required = false,defaultValue ="20") Integer pageSize,
                                   @Parameter(description="ty") @RequestParam(required = false) String ty,
                                   @Parameter(description="choiceSearch")@RequestParam(required = false) String choiceSearch) throws Exception {
		return riskEventService.disposalManageZh(riskcatid,orgid,find,pageNo,pageSize,token,ty,choiceSearch);
    }

	@OperationLog(
			success = "风险事件查看节点--总公司查看处理成功",
			busType = "风险事件管理",
			fail = "风险事件查看节点--总公司查看处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @Operation(summary = "风险事件查看节点--总公司查看/riskeventZh/disposalmanageMain")
    @ResponseBody
    @RequestMapping("/disposalmanageMain")
    public JsonBean disposalmanageMain(@Parameter(description="riskcatid") @RequestParam(required = false)String riskcatid,
                                   @Parameter(description="orgId") @RequestParam(required = false)String orgId,
                                   @Parameter(description="find") Find find,
                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                   @Parameter(description="pageNumber")  @RequestParam(required = false,defaultValue = "0")Integer pageNo,
                                   @Parameter(description="pageSize") @RequestParam(required = false,defaultValue ="20") Integer pageSize,
                                   @Parameter(description="ty") @RequestParam(required = false) String ty,
                                   @Parameter(description="losseventcategory") @RequestParam(required = false) String losseventcategory,
                                   @Parameter(description="choiceSearch")@RequestParam(required = false) String choiceSearch) throws Exception {
        return riskEventService.disposalManageZhMain(riskcatid,orgId,find,pageNo,pageSize,token,ty,choiceSearch,losseventcategory);
    }

	@OperationLog(
			success = "获取风险事件编号处理成功",
			busType = "风险事件管理",
			fail = "获取风险事件编号处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @RequestMapping(value = "/get_risksj_no",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
    @Operation(summary = "获取风险事件编号")
    public JsonBean get_risksj_no(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    		) throws Exception {
        JsonBean jsonBean = this.riskEventService.get_risksj_no(token);

        return jsonBean;
    }

    /*
     status:ok pass+1
    * @author zuoshun
    * @version v1.0.1
    * @Description 风险事件详情
    * @Date 2022/8/11
    * @param eventid
    * @return com.hbfk.util.JsonBean
    * @url:http://localhost:8081/riskevent/riskevent_info?eventid=114747
    **/
	@OperationLog(
			success = "风险事件详情处理成功",
			busType = "风险事件管理",
			fail = "风险事件详情处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "风险事件详情/riskeventZh/riskevent_info")
    @RequestMapping(value = "/riskevent_info")
    public JsonBean riskEventInfo(@Parameter(name = "eventid", description = "风险事件ID", required = true) @RequestParam(required = true) String eventid,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
            return new JsonBean(0, "用户已失效", null);
        }
        if (StringUtils.isEmpty(eventid)) {
            return new JsonBean(400, "未提供eventid, 无法继续", null);
        }
        return riskEventService.riskEventDetail(eventid);
    }


    @Autowired
    IRiskClaimService riskClaimService;

    /*
     status:ok pass+1
     * @author zuoshun
     * @version v1.0.1
     * @Description 通过eventId查询索赔信息
     * @Date 2022/8/11
     * @param evenid
     * @return com.hbfk.util.JsonBean
     * @url:http://localhost:8081/riskevent/to_hsxx_info?riseveid=114747
     **/
	@OperationLog(
			success = "损失事件管理 - 维护损失事件处理成功",
			busType = "风险事件管理",
			fail = "损失事件管理 - 维护损失事件处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @Operation(summary = "损失事件管理 - 维护损失事件/riskeventZh/to_hsxx_info")
    @ResponseBody
    @RequestMapping("/to_hsxx_info")
    public JsonBean claimInfo(@Parameter(description="riseveid") @RequestParam String riseveid,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token){
        return riskClaimService.getByEventId(riseveid);
    }




    @Autowired
    private IRiskRectifysolutionService riskRectifysolutionService;

	@OperationLog(
			success = "发起整改处理成功",
			busType = "风险事件管理",
			fail = "发起整改处理失败",
			operationType = OperationType.ADD,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "发起整改  /rectify_add")
    @RequestMapping(value = "/rectify_add")
    public String rectify_add(@Parameter(name = "riskRectifysolution", description = "riskRectifysolution") RiskRectifysolution riskRectifysolution,
                                  @Parameter(name = "start", description = "start") @RequestParam(required = false) String start,
                                  @Parameter(name = "end", description = "end") @RequestParam(required = false) String end,
                                  @Parameter(name = "record", description = "record") @RequestParam(required = false) String record,
                                  @Parameter(name = "riskcatid", description = "riskcatid") @RequestParam(required = false) String riskcatid,
                                  @Parameter(name = "request", description = "request") HttpServletRequest request,
                                  @Parameter(name = "belongstoText", description = "belongstoText") @RequestParam(required = false) String belongstoText,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        //更新和保存数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (StringUtils.isNotEmpty(start)) {
                riskRectifysolution.setStartdate(sdf.parse(start));
            }
            if (StringUtils.isNotEmpty(end)) {
                riskRectifysolution.setEnddate(sdf.parse(end));
            }
            if (StringUtils.isNotEmpty(record)) {
                riskRectifysolution.setRecorddate(sdf.parse(record));
            }
        } catch (ParseException e) {
            log.error("风险事件处理-->整改--》转换时间出错");
        }
        if (riskRectifysolution != null && riskRectifysolution.getRectsolid() != null) {
            riskRectifysolutionService.updateById(riskRectifysolution);
        } else {
            riskRectifysolutionService.save(riskRectifysolution);
        }
        return JsonBean.success();
    }

	@OperationLog(
			success = "整改编号检查唯一处理成功",
			busType = "风险事件管理",
			fail = "整改编号检查唯一处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @Operation(summary = "整改编号检查唯一")
    @RequestMapping(value = "/find_solutioncode")
    public @ResponseBody
    String find_solutioncode(@RequestParam(required = false) String solutioncode,
                             @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        log.info("风险事件处理-->整改-->整改编号检查唯一");
        return riskRectifysolutionService.findSolutioncode(solutioncode).toString();
    }
	@OperationLog(
			success = "查看已有整改处理成功",
			busType = "风险事件管理",
			fail = "查看已有整改处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "查看已有整改  /findByeventid")
    @RequestMapping(value = "/findByeventid")
    public JsonBean findByeventid(@Parameter(name="eventid",description="风险事件ID,必传",required=true) @RequestParam(required = true) String eventid,
                                  @Parameter(name = "code", description = "整改编号") @RequestParam(required = false) String code,
                                  @Parameter(name = "name", description = "整改名称") @RequestParam(required = false) String name,
                                  @Parameter(name = "userName", description = "整改负责人") @RequestParam(required = false) String userName,
                                  @Parameter(name = "riskcatid", description = "riskcatid") @RequestParam(required = false)  String riskcatid,
                                  @Parameter(name = "pageNo", description = "pageNo") @RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                  @Parameter(name = "pageSize", description = "pageSize") @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                  @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false)  String choiceSearch,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        //分页设置
        IPage page = new Page(pageNo, pageSize);
        //获取id
        riskRectifysolutionService.getRiskRectifysolutionPage(page, eventid, code, name, userName);
//        riskRectifysolutionService.getById(eventid);
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        Map result = new HashMap();
        result.put("pageBean", page);
        result.put("choiceSearch", choiceSearch);
        result.put("riskcatid", riskcatid);
        result.put("eventid", eventid);
        jsonBean.setData(result);
        return jsonBean;
    }

	@OperationLog(
			success = "删除已有整改处理成功",
			busType = "风险事件管理",
			fail = "删除已有整改处理失败",
			operationType = OperationType.DELETE,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "删除已有整改  /del_zg")
    @RequestMapping(value = "/del_zg")
    //查看已有整改
    //TblRiskRectifysolutionService查看已有整改表
    public String del_zg(@Parameter(name = "eventid", description = "eventid") @RequestParam(required = false)  String eventid,
                         @Parameter(name = "riskcatid", description = "riskcatid") @RequestParam(required = false)  String riskcatid,
                         @Parameter(name = "rectsolids", description = "rectsolids") @RequestParam(required = false)  String[] rectsolids,
                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String num = "0";
        for (int i = 0; i < rectsolids.length; i++) {
            RiskRectifysolution r = new RiskRectifysolution();
            r.setRectsolid(new BigDecimal(rectsolids[i]));
            try {
                riskRectifysolutionService.removeById(r);
            } catch (Exception e) {
                num = "1";
            }
        }
        return num;
    }

	@OperationLog(
			success = "查看已有整改---->新建处理成功",
			busType = "风险事件管理",
			fail = "查看已有整改---->新建处理失败",
			operationType = OperationType.SELECT,
			subType = "风险事件处理中核"
	)
    @ResponseBody
    @Operation(summary = "查看已有整改---->新建  /rectify_toadd")
    @RequestMapping(value = "/rectify_toadd")
    public JsonBean rectify_toadd(@Parameter(name = "eventid", description = "eventid") @RequestParam(required = false)  String eventid,
                                  @Parameter(name = "id", description = "id") @RequestParam(required = false) String id,
                                  @Parameter(name = "riskcatid", description = "riskcatid") @RequestParam(required = false) String riskcatid,
                                  @Parameter(name = "newDate", description = "newDate") @RequestParam(required = false) String newDate,
                                  @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
                                  @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        RiskRectifysolution rectifysolution = new RiskRectifysolution();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(id)) {
            rectifysolution = riskRectifysolutionService.getById(id);
            /*if (rectifysolution.getStartdate() != null) {
                queryWrapper.eq("start",sdf.format(rectifysolution.getStartdate()));
            }
                //mv.addObject("start",sdf.format(rectifysolution.getStartdate()));
            if (rectifysolution.getEnddate() != null) {
                queryWrapper.eq("end", sdf.format(rectifysolution.getEnddate()));
            }
                //mv.addObject("end", sdf.format(rectifysolution.getEnddate()));
            if (rectifysolution.getRecorddate() != null) {
                queryWrapper.eq("newDate",sdf.format(rectifysolution.getRecorddate()));
            }
                //mv.addObject("newDate",sdf.format(rectifysolution.getRecorddate()));
                queryWrapper.eq("username", rectifysolution.getRecorder());
            //mv.addObject("username", rectifysolution.getRecorder());*/

        }
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        JsonBean jsonBean = new JsonBean();
        jsonBean.setCode(200);
        Map result = new HashMap();
        result.put("rectifysolution", rectifysolution);
        result.put("eventid", eventid);
        result.put("riskcatid", riskcatid);
        result.put("username", rectifysolution.getRecorder());
        jsonBean.setData(result);
        return jsonBean;
    }


    /**
	 * 风险事件 -- 添加
	 * @author tyb
	 * @date 2016-1-15 上午10:59:43
	 */
//	@RequestMapping(value = "/riskevent_add")
//	public ModelAndView riskevent_add(HttpServletRequest request,TblRiskevent riskevent, String startdate, String start1date) {
//

		@OperationLog(
				success = "风险事件库-->新建-->基本信息处理成功",
				busType = "风险事件管理",
				fail = "风险事件库-->新建-->基本信息处理失败",
				operationType = OperationType.ADD,
				subType = "风险事件处理中核"
		)
		@RequestMapping(value = "/riskevent_add", method = { RequestMethod.GET }, produces = "application/json; charset=utf-8")
		@Operation(summary = "风险事件库-->新建-->基本信息:/riskEvent/riskevent_add")
		public JsonBean law_regulations(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @Parameter(name = "startdate", description = "发生日期 ") @RequestParam(required = false) String startdate,
	            @Parameter(name = "start1date", description = "发现日期 ") @RequestParam(required = false) String start1date,
				@Parameter(name = "reporteds", description = "附件id", required = false) @RequestParam(value = "reporteds", required = false) String reporteds,
                @Parameter(name = "removeReporteds", description = "removeReporteds 上传附件ids", required = false) @RequestParam String removeReporteds,
				@Parameter(name = "type", description = "风险id", required = false) @RequestParam(value = "type", required = false) String type,
				Riskevent risk)
						throws Exception {
			TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
			if (staffUtil == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			risk.setSubsystem(type);
			risk.setUnit(staffUtil.getCurrentOrg().getOrgid().toString());
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				if(startdate != null && !"".equals(startdate)) {
					risk.setOccureddate(sdf.parse(startdate));
				}
				if(start1date != null && !"".equals(start1date)) {
					risk.setDiscovereddate(sdf.parse(start1date));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			Map<String, Object> result = riskEventService.riskevent_add(risk, reporteds);
            if (StringUtils.isNotEmpty(removeReporteds)){
                attachmentService.removeByIds(Arrays.asList(removeReporteds.split(",")));
            }
			return ResponseFormat.retParam(1, 200, result);
	}


		/**
		 * 风险信息---新建页---缓释信息保存
		 */

		@OperationLog(
				success = "风险事件库-->新建-->理赔信息处理成功",
				busType = "风险事件管理",
				fail = "风险事件库-->新建-->理赔信息处理失败",
				operationType = OperationType.ADD,
				subType = "风险事件处理中核"
		)
		@RequestMapping(value = "/fxxx_spxx_add", method = { RequestMethod.GET }, produces = "application/json; charset=utf-8")
		@Operation(summary = "风险事件库-->新建-->理赔信息:/riskEvent/fxxx_spxx_add")
		public JsonBean law_regulations(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
	            @Parameter(name = "date1", description = "索赔日 ") @RequestParam(required = false) String date1,
	            @Parameter(name = "date2", description = "入账日 ") @RequestParam(required = false) String date2,
				@Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) String riseveid,
				 RiskClaim  claim) throws Exception {
			 Map<String,Object> result = new HashMap<String,Object>(0);
			TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
			if (staffUtil == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				if (date1 != null && !"".equals(date1))
					claim.setClaimdate(sdf.parse(date1));
				if (date2 != null && !"".equals(date2))
					claim.setIndate(sdf.parse(date2));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			boolean res=false;
			if (claim != null && claim.getClaimid() != null && !"".equals(claim.getClaimid())) {
				res=iRiskClaimService.updateById(claim);
			} else {
				res=iRiskClaimService.save(claim);
			}
			if(res){
				result.put("code", 0);
				result.put("result", "操作成功");
				result.put("claimid", claim.getClaimid());
				return ResponseFormat.retParam(1, 200, result);
			}else{
				result.put("code", 1);
				result.put("result", "操作失败");
				result.put("claimid", null);
				return ResponseFormat.retParam(0, 30001, result);
			}
		}



		@OperationLog(
				success = "风险事件库 -- 删除处理成功",
				busType = "风险事件管理",
				fail = "风险事件库 -- 删除处理失败",
				operationType = OperationType.DELETE,
				subType = "风险事件处理中核"
		)
		@RequestMapping(value = "/riskevent_del", method = { RequestMethod.GET }, produces = "application/json; charset=utf-8")
		@Operation(summary = "风险事件库 -- 删除:/riskEvent/riskevent_del")
		public JsonBean law_regulations(
				@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
				@Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid) throws Exception {
			 Map<String,Object> result = new HashMap<String,Object>(0);
				TblStaffUtil staffUtil = userProvider.get();// 得到了当前登录的用户信息
				if (staffUtil == null) {
					return ResponseFormat.retParam(0, 20006, null);
				}
			Riskevent event=riskEventService.getById(riseveid);
			if(event!=null){
				riskeventMapper.deleteAttchMent(riseveid.intValue());
				boolean res=riskEventService.removeById(riseveid);
				result.put("code", 0);
				result.put("result", "操作成功");
				return ResponseFormat.retParam(1, 200, result);
			}else{
				result.put("code", 1);
				result.put("result", "不存在该数据");
				return ResponseFormat.retParam(0, 30001, result);
			}
	}

			@OperationLog(
					success = "获取风险事件最大版本号处理成功",
					busType = "风险事件管理",
					fail = "获取风险事件最大版本号处理失败",
					operationType = OperationType.SELECT,
					subType = "风险事件处理中核"
			)
		   @RequestMapping(value = "/getMaxVersion",method = {RequestMethod.GET} ,produces = "application/json; charset=utf-8")
		    @Operation(summary = "获取风险事件最大版本号")
		    public JsonBean getMaxVersion(
		            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
		            @Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid
		    		) throws Exception {
		        JsonBean jsonBean = this.riskEventService.getMaxVersion(token,riseveid);

		        return jsonBean;
		    }

			@OperationLog(
					success = "风险事件导出处理成功",
					busType = "风险事件管理",
					fail = "风险事件导出处理失败",
					operationType = OperationType.EXPORT,
					subType = "风险事件处理中核"
			)
			@RequestMapping(value = "/exportRiskEvent",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
			@ResponseBody
		    @Operation(summary = "风险事件导出")
			public void  exportRiskEvent(HttpServletRequest request, HttpServletResponse response,
													 @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token ,
													 @Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid, Object task
												 ) {
				try {
					log.info("风险事件-导出Excel");
					response.setContentType("application/binary;charset=UTF-8");
				    TblStaffUtil user = userProvider.get();
				    Riskevent  event =riskEventService.getById(riseveid);
				    FiexibleNameAssignment ment=new FiexibleNameAssignment();
		        	//对灵活字段中的姓名名称及机构名称赋值
					fieldOrgStaffId item=new fieldOrgStaffId();
					BeanUtils.copyProperties(event,item); 
					fieldOrgStaffName nameEntity=ment.setOpenName(item);
					BeanUtils.copyProperties(nameEntity,event ); 
					String[] cNames = {"事件编号","事件名称","版本号", "发生部门","发生日期","发现日期",
							"损失事件定性类别","经办人", "经办部门", "事件说明",
							"涉及企业名称","涉及企业层级","当期情况描述",
							"损失(风险)金额(万元)","处置进展情况"};
					List<Object[]> contractlist = new ArrayList<Object[]>(0);
					Object[] objs = null;
					//for (TblTesttaskProblemFind task : unitList) {
						objs = new Object[15];
						objs[0] = event.getRiskeventcode();
						objs[1] = event.getRiskeventname();
						objs[2] = event.getVersion();
						objs[3] = event.getOccureddepartment();
						objs[4] =event.getOccureddate()==null?"":com.huabo.fxgl.util.DateUtils.parseDate(event.getOccureddate(), "yyyy-MM-dd");
						objs[5] =event.getDiscovereddate()==null?"":com.huabo.fxgl.util.DateUtils.parseDate(event.getDiscovereddate(), "yyyy-MM-dd");
						objs[6] = event.getLosseventcategory().equals("1")?"一般事件":"重大事件";
						objs[7] = event.getRiskfactor2();
						objs[8] = event.getRecorddepart();
						objs[9] =event.getRiskeventdescription();
						objs[10] =event.getInvolvingenterprisenames();
						objs[11] = event.getInvolvingenterprisehierarchy();
						objs[12] =event.getDescription();
						objs[13] = event.getAmountofdamages();
						objs[14] = event.getProgressofdisposal();
						contractlist.add(objs);
					//}
					response.setHeader("Content-Disposition", "attachment;filename=" + new String("风险事件".getBytes(),"UTF-8") + ".xlsx");
					ServletOutputStream outputStream = response.getOutputStream();
					ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			
			
			@OperationLog(
					success = "风险事件多导出处理成功",
					busType = "风险事件管理",
					fail = "风险事件多导出处理失败",
					operationType = OperationType.EXPORT,
					subType = "风险事件处理中核"
			)
			@RequestMapping(value = "/exportRiskEvents",method = {RequestMethod.POST}, produces = "application/html; charset=utf-8")
			@ResponseBody
		    @Operation(summary = "风险事件多导出")
			public void  exportRiskEvents(HttpServletRequest request, HttpServletResponse response,
					@Parameter(description="riskcatid") @RequestParam(required = false)String riskcatid,
                    @Parameter(description="orgid") @RequestParam(required = false)String orgid,
                    @Parameter(description="find") Find find,
                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                    @Parameter(description="ty") @RequestParam(required = false) String ty,
                    @Parameter(description="ids") @RequestParam(required = false) String ids,
                    @Parameter(description="choiceSearch")@RequestParam(required = false) String choiceSearch )throws Exception {
				try {
					log.info("风险事件-导出Excel");
					response.setContentType("application/binary;charset=UTF-8");
				    TblStaffUtil user = userProvider.get();
				    List<Riskevent>  eventList=riskEventService.disposalManageZhExport(ids,riskcatid,orgid,find,token,ty,choiceSearch);
					String[] cNames = {"事件编号","事件名称","版本号", "发生部门","发生日期","发现日期",
							"损失事件定性类别","经办人", "经办部门", "事件说明",
							"涉及企业名称","涉及企业层级","当期情况描述",
							"损失(风险)金额(万元)","处置进展情况"};
					List<Object[]> contractlist = new ArrayList<Object[]>(0);
					Object[] objs = null;
					 for (Riskevent event : eventList) {
						objs = new Object[15];
						objs[0] = event.getRiskeventcode();
						objs[1] = event.getRiskeventname();
						objs[2] = event.getVersion();
						objs[3] = event.getOccureddepartment();
						objs[4] =event.getOccureddate()==null?"":com.huabo.fxgl.util.DateUtils.parseDate(event.getOccureddate(), "yyyy-MM-dd");
						objs[5] =event.getDiscovereddate()==null?"":com.huabo.fxgl.util.DateUtils.parseDate(event.getDiscovereddate(), "yyyy-MM-dd");
						objs[6] = event.getLosseventcategory().equals("1")?"一般事件":"重大事件";
						objs[7] = event.getRiskfactor2();
						objs[8] = event.getRecorddepart();
						objs[9] =event.getRiskeventdescription();
						objs[10] =event.getInvolvingenterprisenames();
						objs[11] = event.getInvolvingenterprisehierarchy();
						objs[12] =event.getDescription();
						objs[13] = event.getAmountofdamages();
						objs[14] = event.getProgressofdisposal();
						contractlist.add(objs);
					}
					response.setHeader("Content-Disposition", "attachment;filename=" + new String("风险事件".getBytes(),"UTF-8") + ".xlsx");
					ServletOutputStream outputStream = response.getOutputStream();
					ImportOrExportExcelUtil.exportExcel(cNames, contractlist, outputStream, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@OperationLog(
					success = "查询风险事件--查看历史记录处理成功",
					busType = "风险事件管理",
					fail = "查询风险事件--查看历史记录处理失败",
					operationType = OperationType.SELECT,
					subType = "风险事件处理中核"
			)
			@Operation(summary = "查询风险事件--查看历史记录/riskeventZh/getViewHistoricalVersions")
		    @ResponseBody
		    @RequestMapping(value ="/getViewHistoricalVersions")
		    public JsonBean getViewHistoricalVersions(
		    		@Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid,
		                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		                                    ) throws Exception {
		        return riskEventService.getViewHistoricalVersions( token,riseveid);
		    }

			@OperationLog(
					success = "风险事件查看节点--查看历史记录处理成功",
					busType = "风险事件管理",
					fail = "风险事件查看节点--查看历史记录处理失败",
					operationType = OperationType.SELECT,
					subType = "风险事件处理中核"
			)
			@Operation(summary = "风险事件查看节点--查看历史记录/riskeventZh/getViewHistoricalVersionsMain")
		    @ResponseBody
		    @RequestMapping(value ="/getViewHistoricalVersionsMain")
		    public JsonBean getViewHistoricalVersionsMain(
		    		@Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid,
		                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		                                    ) throws Exception {
		        return riskEventService.getViewHistoricalVersionsMain( token,riseveid);
		    }

			@OperationLog(
					success = "风险事件--上报总公司处理成功",
					busType = "风险事件管理",
					fail = "风险事件--上报总公司处理失败",
					operationType = OperationType.UPDATE,
					subType = "风险事件处理中核"
			)
			@Operation(summary = "风险事件--上报总公司/riskeventZh/reportToLeader")
		    @ResponseBody
		    @RequestMapping(value = "/reportToLeader")
		    public JsonBean reportToLeader(
		    		@Parameter(name = "riseveid", description = "风险事件id", required = false) @RequestParam(value = "riseveid", required = false) BigDecimal riseveid,
		                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		                                    ) throws Exception {
		        return riskEventService.reportToLeader(token,riseveid);
		    }

			@OperationLog(
					success = "查询是否有重大事件类型需要提醒处理成功",
					busType = "风险事件管理",
					fail = "查询是否有重大事件类型需要提醒处理失败",
					operationType = OperationType.SELECT,
					subType = "风险事件处理中核"
			)
			@Operation(summary = "查询是否有重大事件类型需要提醒/riskEventZh/getRemindList")
		    @ResponseBody
		    @RequestMapping(value ="/getRemindList")
		    public JsonBean getRemindList(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
		                                    ) throws Exception {
		        return riskEventService.getRemindList(token);
		    }

}

