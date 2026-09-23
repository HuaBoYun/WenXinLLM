package com.huabo.monitor.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.monitor.config.DateBaseConfig;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblBug;
import com.huabo.monitor.entity.TblBugCriterionEntity;
import com.huabo.monitor.entity.TblInnerrule;
import com.huabo.monitor.entity.TblOrganization;
import com.huabo.monitor.entity.TblOuterrule;
import com.huabo.monitor.service.AttachmentService;
import com.huabo.monitor.service.ITblBugService;
import com.huabo.monitor.service.TblAssessService;
import com.huabo.monitor.service.TblAutonoNumberService;
import com.huabo.monitor.service.TblBugCriterionService;
import com.huabo.monitor.service.TblInnerRuleService;
import com.huabo.monitor.service.TblOrganizaService;
import com.huabo.monitor.service.TblOuterRuleService;
import com.huabo.monitor.service.TreeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * ICS Internal Control Setting
 */
@RestController
@Slf4j
@Tag(name="内控设置-行业缺陷库",description="内控设置-行业缺陷库")
@RequestMapping(value = "/nbkz")
public class ICSIndustryDefectController {
    @Resource
    TblAssessService tblAssessService;

    @Resource
    TblOrganizaService tblOrganizaService;

    @Resource
    TblBugCriterionService tblBugCriterionService;

    @Resource
    ITblBugService tblBugService;

    @Resource
    TblInnerRuleService tblInnerRuleService;

    @Resource
    TblOuterRuleService tblOuterRuleService;

    @Resource
    AttachmentService attachmentService;

    @Resource
    public TreeService treeService;

    @Resource
    public TblAutonoNumberService tblAutonoNumberService;
    
    @Resource
    private UserProvider userProvider;

    @OperationLog(
            success = "内控设置-行业缺陷库-主页列表查询成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-主页列表查询失败",
            operationType = OperationType.SELECT,
            subType = "行业缺陷库"
    )
    @GetMapping(value = "/qxwt/defect_list_hy")
    @Operation(summary = "内控设置-行业缺陷库-主页列表")
    public JsonBean defect_listQxwthy(HttpServletRequest request,
                                      @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) String orgid,
                                      @Parameter(name="pageNumber",description="pageNumber",required=false) @RequestParam(value = "pageNumber", required = false, defaultValue = "1") String number,
                                      @Parameter(name = "searchbegintime", description = "searchbegintime") @RequestParam(value = "searchbegintime", required = false) String startdate,
                                      @Parameter(name = "searchendtime", description = "searchendtime") @RequestParam(value = "searchendtime", required = false) String enddate,
                                      @Parameter(name = "plancode", description = "plancode") @RequestParam(value = "plancode", required = false) String plancode,
                                      @Parameter(name = "plantype", description = "plantype") @RequestParam(value = "plantype", required = false) String plantype,
                                      @Parameter(name = "state", description = "state") @RequestParam(value = "state", required = false) String state,

                                      @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        // 默认显示第一个行业的内容
        String orgIdGetByDb = null;
        if (DateBaseConfig.DATABASETYPE.equals("Oracle")) {
            orgIdGetByDb = tblOrganizaService.getHY().getOrgid().toString();
        } else {
            orgIdGetByDb = tblOrganizaService.getMySqlHY().getOrgid().toString();
        }
        if (StringUtils.isBlank(orgid)) {
            orgid = orgIdGetByDb;
        }

        TblBug bug = new TblBug();

        String orgtype = "";
        if (plancode != null && plancode.length() > 0) {
            bug.setBugnumber(plancode);
        }
        if (plantype != null && plantype.length() > 0) {
            bug.setBugsource(plantype);
        }
        if (state != null && state.length() > 0) {
            bug.setBugreformstatus(plancode);
        }
        Integer pageNumber = 0;

        IPage<TblBug> ipage = null;
        if (StringUtils.isNotBlank(orgid)) {
            TblOrganization orgNew = tblOrganizaService.findByid(orgid);
            if (orgtype == null || orgtype.equals("")) {
                orgtype = orgNew.getOrgtype().toString();
            }
            if (number != null && number.length() > 0) {
                pageNumber = Integer.parseInt(number);
            }

            ipage = tblBugService.findALLHy(bug, pageNumber, startdate, enddate, orgid, orgtype);
        }

        Map<String, Object> mv = new HashMap<>();
        mv.put("pageBean", ipage);
        mv.put("bug", bug);
        mv.put("startDate", startdate);
        mv.put("orgid", orgid);
        mv.put("orgtyope", orgtype);
        mv.put("enddate", enddate);

        // 为页面查找区域显隐藏赋值
        String choiceSearch = request.getParameter("choiceSearch");
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        mv.put("choiceSearch", choiceSearch);
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "内控设置-行业缺陷库-详情接口查询成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-详情接口查询失败",
            operationType = OperationType.SELECT,
            subType = "行业缺陷库"
    )
    @GetMapping(value = "/qxwt/defect_detail")
    @Operation(summary = "内控设置-行业缺陷库-详情接口")
    public JsonBean defect_detailQxwt(
            @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") BigDecimal id,
            @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();

        TblBug tblBug = null;
        Set<TblInnerrule> innerRules = new HashSet<>();
        Set<TblOuterrule> outerRules = new HashSet<>();
        Set<TblBug> child = null;
        if (id != null) {
            tblBug = tblBugService.findById(id);
            TblOrganization organization = tblOrganizaService.findById(tblBug.getBugdepartment());
            mv.put("org", organization);
            innerRules = tblBug.getTblInnerrules();
            outerRules = tblBug.getTblOuterrules();
            child = tblBug.getChildren();
        }

        mv.put("tblBug", tblBug);
        mv.put("innerRules", innerRules);
        mv.put("outerRules", outerRules);
        mv.put("child", child);

//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
//        List<TblBugCriterion> list = tblBugCriterionService.findAll(attribute.getOrgid().toString());
        List<TblBugCriterionEntity> list = tblBugCriterionService.findAll(orgid);
        mv.put("list", list);
        TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByTblBugCriterion(String.valueOf(id));
        if (tblBugCriterion != null) {
            mv.put("tblBugCriterion", tblBugCriterion);
        }
        return new JsonBean(200, "success", mv);
    }


    @OperationLog(
            success = "内控设置-行业缺陷库-添加后保存成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-添加后保存失败",
            operationType = OperationType.ADD,
            subType = "行业缺陷库"
    )
    @PostMapping(value = "/qxwt/defect_save")
    @Operation(summary = "内控设置-行业缺陷库-添加后保存")
    public JsonBean defect_saveQxwt(
            @Parameter(name="url",description="url",required=false) @RequestParam(value = "url", required = false) String url,
            @Parameter(name="buglevel",description="buglevel",required=false) @RequestParam(value = "buglevel", required = false) String buglevel,
            @Parameter(name="wt",description="wt",required=false) @RequestParam(value = "wt", required = false) String wt,
            @Parameter(name="isp",description="isp",required=false) @RequestParam(value = "isp", required = false) String isp,
            @Parameter(name="resonfornoreform",description="resonfornoreform",required=false) @RequestParam(value = "resonfornoreform", required = false) String resonfornoreform,

            @Parameter(name="bugnumber",description="bugnumber",required=false) @RequestParam(value = "bugnumber", required = false) String bugnumber,
            @Parameter(name="bugsource",description="bugsource",required=false) @RequestParam(value = "bugsource", required = false) String bugsource,
            @Parameter(name="discovertime",description="discovertime",required=false) @RequestParam(value = "discovertime", required = false) String de,
            @Parameter(name="status",description="status",required=false) @RequestParam(value = "status", required = false) String status,
            @Parameter(name="choosedUserName",description="choosedUserName",required=false) @RequestParam(value = "choosedUserName", required = false) String choosedUserName,
            @Parameter(name="bugproperty",description="bugproperty",required=false) @RequestParam(value = "bugproperty", required = false) String bugproperty,

            @Parameter(name="wtorgid",description="wtorgid",required=false) @RequestParam(value = "wtorgid", required = false) String orgid,
            @Parameter(name="belongsto",description="belongsto",required=false) @RequestParam(value = "belongsto", required = false) String belongsto,

            @Parameter(name="bugdescripte",description="bugdescripte",required=false) @RequestParam(value = "bugdescripte", required = false) String bugdescripte,
            @Parameter(name="type",description="type",required=false) @RequestParam(value = "type", required = false) String type,
            @Parameter(name="businessDescription",description="businessDescription",required=false) @RequestParam(value = "businessDescription", required = false) String businessDescription,
            @Parameter(name="attid",description="attid",required=false) @RequestParam(value = "attid", required = false) String aid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        TblBug tblBug = new TblBug();
        if (isp.equals("1")) {
            tblBug.setNeedreform("否");
            tblBug.setResonfornoreform(resonfornoreform);
        } else {
            tblBug.setNeedreform("是");
            tblBug.setResonfornoreform("");
        }
        if (buglevel != null && !"".equals(buglevel)) {
            TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByid(buglevel);
            tblBug.getTblBugCriterions().add(tblBugCriterion);
        }

        if (StringUtils.isNotBlank(de)) {
            tblBug.setDiscovertime(LocalDateTime.parse(de));
        }

        tblBug.setBugreformstatus(status);
        tblBug.setDiscoverperson(choosedUserName);
        tblBug.setBugsource(bugsource);
        tblBug.setBugproperty(bugproperty);
        if (StringUtils.isNotBlank(wt) && wt.equals("1")) {
            tblBug.setBugdepartment(orgid);
            tblBug.setInbugidb(new BigDecimal(1));
        } else {
            tblBug.setBugdepartment(belongsto);
        }

        tblBug.setResonfornoreform(resonfornoreform);
        tblBug.setBugdescripte(bugdescripte);
        tblBug.setBugbysystem(type);
        tblBug.setBusinessdescription(businessDescription);
        tblBug.setMemo("");

        tblBugService.save(tblBug);
        if (StringUtils.isNotBlank(aid)) {
            TblAttachment attachment = attachmentService.findById(aid);
            if (attachment != null) {
                attachment.getTblBugs().add(tblBug);
                attachmentService.modify(attachment);
            }
        }

        Map<String, Object> mv = new HashMap<>();
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "内控设置-行业缺陷库-编辑后保存成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-编辑后保存失败",
            operationType = OperationType.ADD,
            subType = "行业缺陷库"
    )
    @Operation(summary = "内控设置-行业缺陷库-编辑后保存")
    @PostMapping(value = "/qxwt/defect_modify")
    public JsonBean defect_modifyQxwt(HttpServletRequest request,
                                      @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") String id,
                                      @Parameter(name="url",description="url") @RequestParam(value = "url") String url,
                                      @Parameter(name="backUrl",description="backUrl") @RequestParam(value = "backUrl") String backUrl,
                                      @Parameter(name="type",description="type") @RequestParam(value = "type") String type,
                                      @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") String orgid,
                                      @Parameter(name="orgtype",description="orgtype") @RequestParam(value = "orgtype") String orgtype,
                                      @Parameter(name="czurl",description="czurl") @RequestParam(value = "czurl") String czurl,
                                      @Parameter(name="choiceSearch",description="choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,
                                      @Parameter(name="wt",description="wt") @RequestParam(value = "wt") String wt,

                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}

        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        TblBug tblBug = null;
        Set innerRules = null;
        Set outerRules = null;
        Set<TblBug> child = null;
        Map<String, Object> mv = new HashMap<>();
        if (StringUtils.isNotBlank(id)) {
            tblBug = tblBugService.findById(new BigDecimal(id));
            innerRules = tblBug.getTblInnerrules();
            outerRules = tblBug.getTblOuterrules();
            child = tblBug.getChildren();
            if (tblBug.getBugdepartment() != null) {
                TblOrganization organization = tblOrganizaService.findByid(tblBug.getBugdepartment());
                mv.put("org", organization);
            }
            TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByTblBugCriterion(id);
            if (tblBugCriterion != null) {
                mv.put("tblBugCriterion", tblBugCriterion);
            }
        }

//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选则的机构
//        String string = attribute.getOrgid().toString();

        List<TblBugCriterionEntity> list = tblBugCriterionService.findAll(orgid);
        mv.put("list", list);
        mv.put("choiceSearch", choiceSearch);
        mv.put("tblBug", tblBug);
        mv.put("innerRules", innerRules);
        mv.put("outerRules", outerRules);
        mv.put("child", child);
        mv.put("wt", wt);
        mv.put("url", url);
        mv.put("type", type);
        mv.put("czurl", czurl);
        mv.put("backUrl", backUrl);
        mv.put("orgid", orgid);
        mv.put("orgtype", orgtype);

        return new JsonBean(200, "修改成功", mv);
    }


    @OperationLog(
            success = "内控设置-行业缺陷库-删除成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-删除失败",
            operationType = OperationType.DELETE,
            subType = "行业缺陷库"
    )
    @Operation(summary = "内控设置-行业缺陷库-删除")
    @PostMapping(value = "/qxwt/defect_delete")
    public JsonBean qxwt_delete(
            @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") String id,
            @Parameter(name="wt",description="wt") @RequestParam(value = "wt") String wt,
            @Parameter(name="type",description="type") @RequestParam(value = "type") String type,
            @Parameter(name="wtorgid",description="wtorgid") @RequestParam(value = "wtorgid") String wtorgid,
            @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") String orgid,
            @Parameter(name="choiceSearch",description="choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,

            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        String finalOrgId = StringUtils.isBlank(wtorgid) ? orgid : wtorgid;
        if (StringUtils.isBlank(choiceSearch)) {
            choiceSearch = "hide";
        }
        String url = "redirect:defect_list?orgid=" + finalOrgId + "&type=" + type + "&choiceSearch=" + choiceSearch;
        if (StringUtils.isNotBlank(wt) && wt.equals("1")) {
            url = "redirect:defect_list_hy?orgid=" + finalOrgId + "&type=" + type + "&choiceSearch=" + choiceSearch;
        }
        if (id != null) {
            TblBug bug = tblBugService.findById(new BigDecimal(id));
            bug.setChildren(null);
            tblBugService.saveOrUpdate(bug);
            tblBugService.removeById(id);
        }

        return new JsonBean(200, "删除成功", url);
    }

    /**
     * 缺陷来源：nbsj/fxgl/nk/nbsj_yigl。对应表中BUGBYSYSTEM @param bugnumber 编号名称
     */
    @OperationLog(
            success = "新增时判断缺陷编号是否存在成功",
            busType = "内控设置",
            fail = "新增时判断缺陷编号是否存在失败",
            operationType = OperationType.SELECT,
            subType = "行业缺陷库"
    )
    @Operation(summary = "新增时判断缺陷编号是否存在")
    @GetMapping(value = "/qxwt/isExistByCode")
    public JsonBean isExistByCode(
            @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") String id,
            @Parameter(name="type",description="type") @RequestParam(value = "type") String type,
            @Parameter(name="bugnumber",description="bugnumber") @RequestParam(value = "bugnumber") String bugnumber,
            @Parameter(name="belongsto",description="belongsto") @RequestParam(value = "belongsto") String orgid,
            @Parameter(name="strcode",description="strcode") @RequestParam(value = "strcode") String strcode,

            @Parameter(name="url",description="url") @RequestParam(value = "url") String url,
            @Parameter(name="wt",description="wt") @RequestParam(value = "wt") String wt,
            @Parameter(name="Innerrules",description="Innerrules") @RequestParam(value = "Innerrules") String innerId,
            @Parameter(name="Outerrules",description="Outerrules") @RequestParam(value = "Outerrules") String outerId,
            @Parameter(name="bugsource",description="bugsource") @RequestParam(value = "bugsource") String bugsource,
            @Parameter(name="discovertime",description="discovertime") @RequestParam(value = "discovertime") String de,
            @Parameter(name="status",description="status") @RequestParam(value = "status") String status,
            @Parameter(name="choosedUserName",description="choosedUserName") @RequestParam(value = "choosedUserName") String choosedUserName,
            @Parameter(name="bugproperty",description="bugproperty") @RequestParam(value = "bugproperty") String bugproperty,

            @Parameter(name="belongsto",description="belongsto") @RequestParam(value = "belongsto") String belongsto,
            @Parameter(name="businessDescription",description="businessDescription") @RequestParam(value = "businessDescription") String businessDescription,
            @Parameter(name="resonfornoreform",description="resonfornoreform") @RequestParam(value = "resonfornoreform") String resonfornoreform,
            @Parameter(name="bugdescripte",description="bugdescripte") @RequestParam(value = "bugdescripte") String bugdescripte,
            @Parameter(name="isp",description="isp") @RequestParam(value = "isp") String isp,
            @Parameter(name="buglevel",description="buglevel") @RequestParam(value = "buglevel") String buglevel,
            @Parameter(name="wtorgid",description="wtorgid") @RequestParam(value = "wtorgid") String wtorgid,
            @Parameter(name="attid",description="attid") @RequestParam(value = "attid") String aid,
            @Parameter(name="attids",description="attids") @RequestParam(value = "attids") String attids,

            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {

    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        TblBug tblBug = null;
        TblBug tblBugold = tblBugService.findByCode(bugnumber, type, orgid);
        if (strcode != null && strcode.equals("1")) {
            tblBugold = null;
        }
        if (tblBugold != null) {
            return new JsonBean(200, "success", "1");
        } else {
            if (id != null && id.trim().length() > 0) {

                tblBug = tblBugService.findById(new BigDecimal(id));
                TblBug oldbug = tblBugService.findById(new BigDecimal(id));
                tblBug.setBugnumber(bugnumber);
                tblBug.setBugsource(bugsource);
                if (StringUtils.isNotBlank(de)) {
                    tblBug.setDiscovertime(LocalDateTime.parse(de));
                }

                tblBug.setBugreformstatus(status);
                tblBug.setDiscoverperson(choosedUserName);
                tblBug.setBugsource(bugsource);
                tblBug.setBugproperty(bugproperty);
                if ((orgid != null && tblBug.getInbugidb() == null) || tblBug.getInbugidb() == null) {
                    tblBug.setBugdepartment(belongsto);
                }
                tblBug.setBusinessdescription(businessDescription);
                tblBug.setResonfornoreform(resonfornoreform);
                tblBug.setBugdescripte(bugdescripte);

                if (isp.equals("1")) {
                    tblBug.setNeedreform("否");
                    tblBug.setResonfornoreform(resonfornoreform);
                } else {
                    tblBug.setNeedreform("是");
                    tblBug.setResonfornoreform("");
                }
                if (innerId != null) {
                    String[] ids = innerId.split(",");
                    for (String string : ids) {
                        if (string != null && !"".equals(string)) {
                            /*TblInnerrule tblInnerrule = tblInnerRuleService.findById(new BigDecimal(string));
                            tblBug.getTblInnerrules().add(tblInnerrule);*/
                        }
                    }

                }
                if (outerId != null) {
                    String[] ids = outerId.split(",");
                    for (String string : ids) {
                        if (string != null && !"".equals(string)) {
                            TblOuterrule tblOuterrule = tblOuterRuleService.findById(new BigDecimal(string));
                            tblBug.getTblOuterrules().add(tblOuterrule);
                        }
                    }
                }
                Set<TblBugCriterionEntity> criterions = oldbug.getTblBugCriterions();

                List<TblBugCriterionEntity> list = null;
                if (criterions != null && criterions.size() > 0) {
                    list = new ArrayList<TblBugCriterionEntity>();
                    for (TblBugCriterionEntity criterion : criterions) {
                        if (criterion.getBugcriid().toString().equals(buglevel)) {
                            continue;
                        }
                        list.add(criterion);
                    }
                }

                if (buglevel != null && !"".equals(buglevel)) {
                    TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByid(buglevel);
                    tblBug.getTblBugCriterions().add(tblBugCriterion);
                }

                tblBugService.saveOrUpdate(tblBug);
                if (list != null && list.size() > 0) {
                    for (TblBugCriterionEntity old : list) {
                        tblBug.getTblBugCriterions().remove(old);
                    }
                }
            } else {
                tblBug = new TblBug();

                if (isp.equals("1")) {
                    tblBug.setNeedreform("否");
                    tblBug.setResonfornoreform(resonfornoreform);
                } else {
                    tblBug.setNeedreform("是");
                    tblBug.setResonfornoreform("");
                }
                if (buglevel != null && !"".equals(buglevel)) {
                    TblBugCriterionEntity tblBugCriterion = tblBugCriterionService.findByid(buglevel);
                    tblBug.getTblBugCriterions().add(tblBugCriterion);
                }

                tblBug.setBugnumber(bugnumber);
                tblBug.setBugsource(bugsource);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (de != null && !"".equals(de)) {
                    tblBug.setDiscovertime(LocalDateTime.parse(de));
                }

                tblBug.setBugreformstatus(status);
                tblBug.setDiscoverperson(choosedUserName);
                tblBug.setBugsource(bugsource);
                tblBug.setBugproperty(bugproperty);
                if (wt != null && !"".equals(wt) && wt.equals("1")) {

                    tblBug.setBugdepartment(wtorgid);
                    tblBug.setInbugidb(new BigDecimal(1));
                } else {
                    tblBug.setBugdepartment(belongsto);
                }

                tblBug.setResonfornoreform(resonfornoreform);
                tblBug.setBugdescripte(bugdescripte);
                tblBug.setBugbysystem(type);
                tblBug.setBusinessdescription(businessDescription);
                tblBug.setMemo("");
                tblBugService.add(tblBug);

                if (StringUtils.isNotBlank(aid)) {
                    TblAttachment a = attachmentService.findById(aid);
                    if (a != null) {
                        a.getTblBugs().add(tblBug);
                        attachmentService.modify(a);
                    }
                }
                // 保存附件
                if (StringUtils.isNotBlank(attids)) {
                    String[] ids = attids.split(",");
                    for (int i = 0; i < ids.length; i++) {
                        TblAttachment att = attachmentService.findById(ids[i].trim());
                        tblBug.getTblAttachments().add(att);
                    }
                }
            }
            if (tblBug != null && tblBug.getBugid() != null) {
                return new JsonBean(200, "success", tblBug.getBugid().toString());
            } else {
                return new JsonBean(200, "success", "0");
            }

        }
    }

    @OperationLog(
            success = "内控设置-行业缺陷库-新增成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-新增失败",
            operationType = OperationType.ADD,
            subType = "行业缺陷库"
    )
    @Operation(summary = "内控设置-行业缺陷库-新增")
    @PostMapping(value = "/qxwt/defect_add")
    public JsonBean defect_addQxwt(HttpServletRequest request,
                                   @Parameter(name="wt",description="wt") @RequestParam(value = "wt") String wt,
                                   @Parameter(name="type",description="type") @RequestParam(value = "type") String type,
                                   @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") String orgid,
                                   @Parameter(name="orgtype",description="orgtype") @RequestParam(value = "orgtype") String orgtype,


                                   @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") String id,
                                   @Parameter(name="url",description="url") @RequestParam(value = "url") String url,
                                   @Parameter(name="backUrl",description="backUrl") @RequestParam(value = "backUrl") String backUrl,
                                   @Parameter(name="czurl",description="czurl") @RequestParam(value = "czurl") String czurl,
                                   @Parameter(name="choiceSearch",description="choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,

                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();

        String orgIdFromToken = userToken.getCurrentOrg().getOrgid().toString();


//        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");// 选择的机构
//        List<TblBugCriterion> list = tblBugCriterionService.findAll(attribute.getOrgid().toString());
        List<TblBugCriterionEntity> list = tblBugCriterionService.findAll(orgid);

        // 查询框代码
        if (StringUtils.isNotBlank(choiceSearch)) {
            choiceSearch = "hide";
        }
        mv.put("choiceSearch", choiceSearch);
        mv.put("wt", wt);
        mv.put("list", list);
        mv.put("type", type);
        mv.put("orgid", orgid);
        mv.put("orgtype", orgtype);
        return new JsonBean(200, "success", mv);
    }

    @OperationLog(
            success = "内控设置-行业缺陷库-主页-树菜单展示查询成功",
            busType = "内控设置",
            fail = "内控设置-行业缺陷库-主页-树菜单展示查询失败",
            operationType = OperationType.SELECT,
            subType = "行业缺陷库"
    )
    @Operation(summary = "内控设置-行业缺陷库-主页-树菜单展示")
    @PostMapping(value = "/qxwt/defect_hys")
    public JsonBean listlcHys(HttpServletRequest request,
                              @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") String orgid,


                              @Parameter(name="wt",description="wt") @RequestParam(value = "wt") String wt,
                              @Parameter(name="type",description="type") @RequestParam(value = "type") String type,
                                   @Parameter(name="orgtype",description="orgtype") @RequestParam(value = "orgtype") String orgtype,


                                   @Parameter(name = "selectProjectid", description = "bugid") @RequestParam(value = "selectProjectid") String id,
                                   @Parameter(name="url",description="url") @RequestParam(value = "url") String url,
                                   @Parameter(name="backUrl",description="backUrl") @RequestParam(value = "backUrl") String backUrl,
                                   @Parameter(name="czurl",description="czurl") @RequestParam(value = "czurl") String czurl,
                                   @Parameter(name="choiceSearch",description="choiceSearch") @RequestParam(value = "choiceSearch") String choiceSearch,

                                   @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil userToken = userProvider.get();
		if (userToken == null) {
	        return ResponseFormat.retParam(0, 20006, null);
		}
        Map<String, Object> mv = new HashMap<>();

//        TblOrganization organization = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
//        List<TblOrganization> findOrgTree = treeService.findOrgTreeObjByHY(organization.getOrgid().toString());

        List<TblOrganization> treeObjByHY = treeService.findOrgTreeObjByHY(orgid);
        mv.put("list", treeObjByHY);
        mv.put("number", treeObjByHY.size());
        return new JsonBean(200, "success", mv);
    }

//    @Operation(summary = "内控设置-行业缺陷库-新增-通用获取设置的编号")
//    @PostMapping(value = "/qxwt/defect_hys")
//    public JsonBean getNewCodeByHyZsk(
//            @Parameter(name="orgid",description="orgid") @RequestParam(value = "orgid") String orgid,
//            @Parameter(name="noId",description="noId") @RequestParam(value = "noId") Integer noId,
//            @Parameter(name="parentId",description="parentId") @RequestParam(value = "parentId") String parentId,
//            @Parameter(name="parentTblName",description="parentTblName") @RequestParam(value = "parentTblName") String parentTblName,
//            @Parameter(name="parentIdCol",description="parentIdCol") @RequestParam(value = "parentIdCol") String parentIdCol,
//            @Parameter(name="parentFatherCol",description="parentFatherCol") @RequestParam(value = "parentFatherCol") String parentFatherCol,
//            @Parameter(name="parentNumberCol",description="parentNumberCol") @RequestParam(value = "parentNumberCol") String parentNumberCol,
//            @Parameter(name="ancestorsNo",description="ancestorsNo") @RequestParam(value = "ancestorsNo") String ancestorsNo,
//            @Parameter(name="chilTblName",description="chilTblName") @RequestParam(value = "chilTblName") String chilTblName,
//            @Parameter(name="chilNumberCol",description="chilNumberCol") @RequestParam(value = "chilNumberCol") String chilNumberCol,
//            @Parameter(name="chilOrgCol",description="chilOrgCol") @RequestParam(value = "chilOrgCol") String chilOrgCol,
//            @Parameter(name="chChoiceCol",description="chChoiceCol") @RequestParam(value = "chChoiceCol") String chChoiceCol,
//            @Parameter(name="choiceVal",description="choiceVal") @RequestParam(value = "choiceVal") String choiceVal,
//            @Parameter(name="bjf",description="bjf") @RequestParam(value = "bjf") String bjf,
//
//            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
//            HttpServletResponse response) {
////        TblOrganization attribute = (TblOrganization) request.getSession().getAttribute("hbOrgEntity");
//        String flowNextId = null;
//        try {
//            flowNextId = tblAutonoNumberService.getNewCodeByHyZsk(new BigDecimal(orgid), noId, parentId, parentTblName,
//                    parentIdCol, parentFatherCol, parentNumberCol, ancestorsNo, chilTblName, chilNumberCol, chilOrgCol,
//                    chChoiceCol, choiceVal, bjf, token);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        response.reset();
//        return new JsonBean(200, "success", flowNextId);
//    }



}
