package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.Bug;
import com.huabo.fxgl.entity.BugCriterion;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.service.impl.AttachmentServiceImpl;
import com.huabo.fxgl.service.impl.BugCriterionServiceImpl;
import com.huabo.fxgl.service.impl.BugServiceImpl;
import com.huabo.fxgl.service.impl.InnerruleServiceImpl;
import com.huabo.fxgl.service.impl.OuterruleServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author LiYe
 * @since 2022-08-12
 */
@RestController
@RequestMapping(value = "/nbkz", method = {RequestMethod.GET, RequestMethod.POST})
@Tag(name="缺陷管理",description="缺陷管理")
@Slf4j
public class OrganizationController {
    @Autowired
    private InnerruleServiceImpl innerruleService;

    @Autowired
    private BugServiceImpl bugService;

    @Autowired
    private OuterruleServiceImpl outerruleService;

    @Autowired
    private BugCriterionServiceImpl bugcriterionService;

    @Autowired
    private AttachmentServiceImpl attachmentService;


    /**
     * 流程创建跳转页面 内部控制
     *
     * @param orgid
     * @param str
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/15
     */
    @OperationLog(
            success = "流程创建跳转页面 内部控制处理成功",
            busType = "缺陷管理",
            fail = "流程创建跳转页面 内部控制处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/radio_orgarea_window_nbkz")
    @Operation(summary = "流程创建跳转页面 内部控制/radio_orgarea_window_nbkz")

    public JsonBean radio_orgarea_windowN(String orgid, String str,
                                          @Parameter(name = "textid", description = "textid") @RequestParam(name = "textid", required = false) String parameter,
                                          @Parameter(name = "id_name", description = "id_name") @RequestParam(name = "id_name", required = false) String idName,
                                          @Parameter(name = "text_name", description = "text_name") @RequestParam(name = "text_name", required = false) String textNname,
                                          @Parameter(name = "textname", description = "textname") @RequestParam(name = "textname", required = false) String parameter2
    ) {
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        result.put("id_name", idName);
        result.put("text_name", textNname);
        result.put("nbkz", "nakz");
        result.put("str", str);
        result.put("orgid", orgid);
        if ((Integer.parseInt(str) == 6)) {
            result.put("textid", parameter);
            result.put("textname", parameter2);
        }
        jsonBean.setData(result);
        jsonBean.setCode(200);
        jsonBean.setMsg("success");
        return jsonBean;
    }


    /**
     * 缺陷管理-新建 -保存
     *
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/13
     */
    @OperationLog(
            success = "缺陷管理-新建 -保存处理成功",
            busType = "缺陷管理",
            fail = "缺陷管理-新建 -保存处理失败",
            operationType = OperationType.ADD,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/isExistByCode", produces = "application/json;charset=utf-8")
    @Operation(summary = "缺陷管理-新建 -保存/qxwt/isExistByCode")
    public String isExistByCode(
            @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String id,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "belongsto", description = "belongsto") @RequestParam(name = "belongsto", required = false) String orgid,
            @Parameter(name = "strcode", description = "strcode") @RequestParam(required = false) String strcode,
            @Parameter(name = "wt", description = "wt") @RequestParam(required = false) String wt,
            @Parameter(name = "Outerrules", description = "Outerrules") @RequestParam(required = false) String outerId,
            @Parameter(name = "bugnumber", description = "缺陷编号 bugnumber") @RequestParam(required = false) String bugnumber,
            @Parameter(name = "discovertime", description = "发现日期 discovertime") @RequestParam(required = false) String de,
            @Parameter(name = "status", description = "status") @RequestParam(required = false) String status,
            @Parameter(name = "choosedUserName", description = "choosedUserName") @RequestParam(required = false) String choosedUserName,
            @Parameter(name = "bugsource", description = "bugsource") @RequestParam(required = false) String bugsource,
            @Parameter(name = "bugproperty", description = "bugproperty") @RequestParam(required = false) String bugproperty,
            @Parameter(name = "businessDescription", description = "业务描述 businessDescription") @RequestParam(required = false) String businessDescription,
            @Parameter(name = "resonfornoreform", description = "不整改原因 resonfornoreform") @RequestParam(required = false) String resonfornoreform,
            @Parameter(name = "isp", description = "isp") @RequestParam(required = false) String isp,
            @Parameter(name = "buglevel", description = "buglevel") @RequestParam(required = false) String buglevel,
            @Parameter(name = "discovertime", description = "发现日期 discovertime") @RequestParam(required = false) String discovertime,
            @Parameter(name = "belongsto", description = "belongsto") @RequestParam(required = false) String belongsto,
            @Parameter(name = "attid", description = "attid") @RequestParam(required = false) String aid,
            @Parameter(name = "attids", description = "attids") @RequestParam(required = false) String attids,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "bugdescripte", description = "bugdescripte") @RequestParam(required = false) String bugdescripte,
            @Parameter(name = "Innerrules", description = "Innerrules") @RequestParam(required = false) String innerId

    ) {

        Bug tblBug = null;
        Bug Bugold = bugService.findByCode(bugnumber, type, orgid);
        if (strcode != null && strcode.equals("1")) {
            Bugold = null;
        }
        if (Bugold != null) {

            return "1";
        } else {
            if (id != null && id.trim().length() > 0) {
                tblBug = bugService.getById(new BigDecimal(id));
                Bug oldbug = bugService.getById(new BigDecimal(id));
                tblBug.setBugnumber(bugnumber);
                tblBug.setBugsource(bugsource);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    if (de != null && !"".equals(de)) {
                        tblBug.setDiscovertime(sdf.parse(de).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tblBug.setBugreformstatus(status);
                tblBug.setDiscoverperson(choosedUserName);
                tblBug.setBugsource(bugsource);
                tblBug.setBugproperty(bugproperty);
                if ((orgid != null && tblBug.getInBugdb() == null) || tblBug.getInBugdb() == null) {
                    tblBug.setBugdapartment(orgid);
                }
//                tblBug.setBusinessDescription(businessDescription);
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

                            Innerrule tblInnerrule = innerruleService.getById(string);
                            tblBug.getTblInnerrules().add(tblInnerrule);
                        }
                    }

                }
                if (outerId != null) {
                    String[] ids = outerId.split(",");
                    for (String string : ids) {
                        if (string != null && !"".equals(string)) {
                            Outerrule tblOuterrule = outerruleService.getById(string);
                            tblBug.getTblOuterrules().add(tblOuterrule);
                        }
                    }
                }
                Set<BugCriterion> criterions = oldbug.getTblBugCriterions();

                List<BugCriterion> list = null;
                if (criterions != null && criterions.size() > 0) {
                    list = new ArrayList<BugCriterion>();
                    for (BugCriterion criterion : criterions) {
                        if (criterion.getBugcriid().toString().equals(buglevel)) {
                            continue;
                        }
                        list.add(criterion);
                    }
                }

                if (buglevel != null && !"".equals(buglevel)) {
                    BugCriterion tblBugCriterion = bugcriterionService.getById(buglevel);
                    tblBug.getTblBugCriterions().add(tblBugCriterion);
                }
                bugService.saveOrUpdate(tblBug);
                //BugService.update(tblBug);
                if (list != null && list.size() > 0) {
                    for (BugCriterion old : list) {
                        tblBug.getTblBugCriterions().remove(old);
                    }
                }
            } else {
                //TblBugService test = SpringContextHolder.getBean("TblBugService");
                tblBug = new Bug();

                if (isp.equals("1")) {
                    tblBug.setNeedreform("否");
                    tblBug.setResonfornoreform(resonfornoreform);
                } else {
                    tblBug.setNeedreform("是");
                    tblBug.setResonfornoreform("");
                }
                if (buglevel != null && !"".equals(buglevel)) {
                    BugCriterion tblBugCriterion = bugcriterionService.getById(buglevel);
                    tblBug.getTblBugCriterions().add(tblBugCriterion);
                }

                tblBug.setBugnumber(bugnumber);
                tblBug.setBugsource(bugsource);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {

                    if (de != null && !"".equals(de)) {
                        tblBug.setDiscovertime(sdf.parse(discovertime).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tblBug.setBugreformstatus(status);
                tblBug.setDiscoverperson(choosedUserName);
                tblBug.setBugsource(bugsource);
                tblBug.setBugproperty(bugproperty);
                if (wt != null && !"".equals(wt) && wt.equals("1")) {
//                    String orgid = (String) request.getSession().getAttribute("wtorgid");
                    tblBug.setBugdapartment(orgid);
                    tblBug.setInBugdb(1);
                } else {
                    tblBug.setBugdapartment(belongsto);
                }

                tblBug.setResonfornoreform(resonfornoreform);
                tblBug.setBugdescripte(bugdescripte);
                tblBug.setBugbysystem(type);
                tblBug.setBusinessdescription(businessDescription);
                tblBug.setMemo("");
                bugService.save(tblBug);
                if (aid != null && !aid.equals("")) {
                    Attachment a = attachmentService.getById(aid);
                    if (a != null) {
                        a.getTblBugs().add(tblBug);
                        attachmentService.saveOrUpdate(a);
                    }
                }
                // 保存附件

                if (StringUtils.isNotBlank(attids)) {
                    String[] ids = attids.split(",");
                    for (int i = 0; i < ids.length; i++) {
                        Attachment att = attachmentService.getById(ids[i].trim());

                        tblBug.getTblAttachments().add(att);
                    }
                }
            }
            if (tblBug != null && tblBug.getBugid() != null) {
                return tblBug.getBugid().toString();
            } else {
                return "0";
            }

        }
    }


}
