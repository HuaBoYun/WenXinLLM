package com.huabo.fxgl.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblOrganizationUtil;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.sdk.log.annotation.OperationLog;
import com.hbfk.sdk.log.enums.OperationType;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PropertyFileReader;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.fxgl.entity.Bug;
import com.huabo.fxgl.entity.BugCriterion;
import com.huabo.fxgl.entity.Criterion;
import com.huabo.fxgl.entity.NbsjBug;
import com.huabo.fxgl.entity.NbsjBugcriterion;
import com.huabo.fxgl.entity.Organization;
import com.huabo.fxgl.entity.Staff;
import com.huabo.fxgl.service.IStaffService;
import com.huabo.fxgl.service.impl.BugCriterionServiceImpl;
import com.huabo.fxgl.service.impl.BugServiceImpl;
import com.huabo.fxgl.service.impl.CriterionServiceImpl;
import com.huabo.fxgl.service.impl.NbkzRiskServiceImpl;
import com.huabo.fxgl.service.impl.NbsjBugServiceImpl;
import com.huabo.fxgl.service.impl.NbsjBugcriterionServiceImpl;
import com.huabo.fxgl.service.impl.OrganizationServiceImpl;
import com.huabo.fxgl.util.Tree;

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
 * @since 2022-08-09
 */
@RestController
@Slf4j
@Tag(name="相关问题汇总-缺陷管理",description="相关问题汇总-缺陷管理")
@RequestMapping(value = "/nbkzRisk", method = {RequestMethod.GET, RequestMethod.POST})
public class NbkzRiskController {
    @Autowired
    private NbsjBugServiceImpl nbsjBugService;

    @Autowired
    private NbkzRiskServiceImpl nbkzRiskService;

    @Autowired
    private OrganizationServiceImpl organizationService;

    @Autowired
    private NbsjBugcriterionServiceImpl nbsjBugcriterionService;

    @Autowired
    private BugCriterionServiceImpl bugcriterionService;

    @Autowired
    private BugServiceImpl bugService;

    @Autowired
    private IStaffService staffService;

    @Autowired
    private CriterionServiceImpl criterionService;
    
    @Resource
    private UserProvider userProvider;


    private static final String GROUP_STRUCTURE = PropertyFileReader.getItem("group.structure");

    /**
     * 集团版 --数据控制
     *
     * @return
     */
    @OperationLog(
            success = "集团版 --数据控制查询处理成功",
            busType = "相关问题汇总",
            fail = "集团版 --数据控制查询处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/findOrganizationByTreeAllss", produces = "application/json; charset=utf-8")
    @Operation(summary = "集团版 --数据控制 /nbkzRisk/findOrganizationByTreeAllss")
    public String findOrganizationByTree(@Parameter(name = "group", description = "group") @RequestParam(required = false) String group,
                                         @Parameter(name = "orgId", description = "orgId") @RequestParam(required = false) BigDecimal orgId,
                                         @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
                                         @Parameter(name = "nodeId", description = "nodeId") @RequestParam(required = false) BigDecimal nodeId,
                                         @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        String json = "";
        if (null == nodeId) {
            nodeId = orgId;
            if (null == orgId) {
                nodeId = staffOrg.getOrgid();
            }
        }
        if (StringUtils.isNotBlank(type)) {
            List<Tree> list = organizationService.getTree(nodeId);
            for (Tree tree : list) {
                if (!tree.getIsParent()) {
                    tree.setTarget("mainFramex");
                    tree.setUrl("/nbkz/user/list?pid=" + tree.getId());
                }
            }
            json = JSONObject.toJSONString(list);
        } else {
            boolean is = false;
            if (StringUtils.isNotBlank(group) && StringUtils.isNotBlank(GROUP_STRUCTURE)) {
                is = true;
            }
            List<Tree> list = new ArrayList<Tree>();
            if (is) {
                list = organizationService.getNodeAllJT(nodeId);
            } else {
                list = organizationService.getNodeAllJT(nodeId);
            }
            for (Tree tree : list) {
                setUrlByTree(tree, "/nbkz/user/list?pid=");
            }
            json = JSONObject.toJSONString(list);

            /** Tree tree = this.tblOrganizaService.getTreeRoot(nodeId); if
             * (!tree.getIsParent()) { tree.setTarget("mainFramex");
             * tree.setUrl("/nbkz/user/list?pid=" + tree.getId()); } json =
             * JSONObject.toJSONString(tree);*/

        }
        return json;
    }

    private void setUrlByTree(Tree tree, String url) {
        for (Tree tre : tree.getChildren()) {
            /// if (!tre.getIsParent()) {
            tre.setTarget("mainFramex");
            tre.setUrl(url + tre.getId());
            // }
            if (tre.getChildren().size() > 0) {
                setUrlByTree(tre, url);
            }
        }
    }

    /**
     * @param flowid
     * @param innerid
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/16
     */
    @OperationLog(
            success = "delete_qx_child处理成功",
            busType = "相关问题汇总",
            fail = "delete_qx_child处理失败",
            operationType = OperationType.DELETE,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/delete_qx_child")
    @Operation(summary = "删除缺陷子项关联")
    public String delete_internal_regulations_qx_ch(
            @Parameter(name = "controlId", description = "controlId") @RequestParam(required = false) String flowid,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "innrulid", description = "innrulid") @RequestParam(required = false) String innerid) {
        if (flowid != null && innerid != null) {
            Bug b = bugService.getById(new BigDecimal(innerid));
            b.setFatherbugid(null);
            bugService.saveOrUpdate(b);
        }
        return "";
    }


    /**
     * 缺陷库-添加关联子缺陷
     *
     * @param
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/16
     */
    @OperationLog(
            success = "缺陷库-添加关联子缺陷处理成功",
            busType = "相关问题汇总",
            fail = "缺陷库-添加关联子缺陷处理失败",
            operationType = OperationType.ADD,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/add_defect_qxwt")
    @Operation(summary = "缺陷库-添加关联子缺陷/qxwt/add_defect_qxwt")
    public String add_defect_qxwt(
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "conid", description = "conid") @RequestParam(required = false) String conid,
            @Parameter(name = "innrulid", description = "innrulid") @RequestParam(required = false) String outerid
    ) {
        if (conid != null && outerid != null) {
            Bug bug = bugService.getById(new BigDecimal(conid));
            if (bug != null) {
                List<Bug> children = bug.getChildren();
                List<Bug> list = new ArrayList<Bug>();
                if (children != null && children.size() > 0) {
                    for (Bug tblBug : children) {
                        if (tblBug.getBugid().toString().equals(outerid)) {
                            continue;
                        }
                        Bug childr = new Bug();
                        childr.setBugid(new BigDecimal(outerid));
                        list.add(childr);
                    }
                } else {
                    Bug childr = new Bug();
                    childr.setBugid(new BigDecimal(outerid));
                    list.add(childr);
                }
                bug.getChildren().addAll(list);
            }
        }
        return "1";
    }


    /**
     * 添加缺陷库-关联缺陷
     *
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/15
     */
    @OperationLog(
            success = "添加缺陷库-关联缺陷处理成功",
            busType = "相关问题汇总",
            fail = "添加缺陷库-关联缺陷处理失败",
            operationType = OperationType.ADD,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/nbkz/qxwt/defect_list_ad")
    @Operation(summary = "添加缺陷库-关联缺陷/nbkz/qxwt/defect_list_ad")
    public JsonBean defect_listQxwt_chil(
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
            @Parameter(name = "czurl", description = "czurl") @RequestParam(required = false) String czurl,
            @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String selectProjectid,
            @Parameter(name = "pageNo", description = "pageNo") @RequestParam(defaultValue = "1") Integer pageNo,
            @Parameter(name = "pageSize", description = "pageSize") @RequestParam(defaultValue = "20") Integer pageSize,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token

    ) throws Exception {

        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织
        Staff staff = staffService.getById(staffUtil.getStaffid());
        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        IPage page = new Page(pageNo, pageSize);
        Boolean isSelect = organizationService.isSJByOrgId(staffOrg.getOrgid().toString());
        if (isSelect) {
            page = bugService.getBugList(null, null, null, page,
                    selectOrg.getOrgid().toString(), selectOrg.getOrgtype().toString(), null, type);
        } else {

            page = bugService.getBugList(null, null, null, page,
                    staffOrg.getOrgid().toString(), staffOrg.getOrgtype().toString(), null, type);
        }
        //ModelAndView mv = new ModelAndView("nbkz/qxwt/defect_list_ad");
        result.put("pageBean", page);
        result.put("czurl", czurl);
        result.put("selectProjectid", selectProjectid);
        result.put("type", type);
        result.put("orgid", orgid);
        //mv.setViewName("nbkz/qxwt/defect_list_ad");
        // 为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        result.put("choiceSearch", choiceSearch);
        jsonBean.setData(result);
        return jsonBean;
    }


    /**
     * 相关问题汇总-缺陷管理-新建
     *
     * @param wt
     * @param type
     * @param orgid
     * @param orgtype
     * @param choiceSearch
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/11
     */
    @OperationLog(
            success = "缺陷管理-新建处理成功",
            busType = "相关问题汇总",
            fail = "缺陷管理-新建处理失败",
            operationType = OperationType.ADD,
            subType = "缺陷管理"
    )
    @Operation(summary = "相关问题汇总-缺陷管理-新建/qxwt/defect_add")
    @RequestMapping(value = "/qxwt/defect_add")
    public JsonBean defect_addQxwt(
            @Parameter(name = "wt", description = "wt") @RequestParam(required = false) String wt,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
            @Parameter(name = "orgtype", description = "orgtype") @RequestParam(required = false) String orgtype,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织*/
        List<Criterion> all = nbkzRiskService.findAll(orgid);
        // 查询框代码
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        Map result = new HashMap();
        result.put("choiceSearch", choiceSearch);
        result.put("wt", wt);
        result.put("list", all);
        result.put("type", type);
        result.put("orgid", orgid);
        result.put("orgtype", orgtype);
        JsonBean jsonBean = new JsonBean();
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * 风险问题汇总 --缺陷管理 --修改数据
     *
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/12
     */

    @OperationLog(
            success = "缺陷管理 --修改数据处理成功",
            busType = "相关问题汇总",
            fail = "缺陷管理 --修改数据处理失败",
            operationType = OperationType.UPDATE,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/defect_modify")
    @Operation(summary = "风险问题汇总 --缺陷管理 --修改数据/qxwt/defect_modify")
    public JsonBean defect_modifyQxwt(
            @Parameter(name = "selectProjectid", description = "selectProjectid") @RequestParam(required = false) String id,
            @Parameter(name = "url", description = "url") @RequestParam(required = false) String url,
            @Parameter(name = "backUrl", description = "backUrl") @RequestParam(required = false) String backUrl,
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "orgid", description = "orgid") @RequestParam(required = false) String orgid,
            @Parameter(name = "orgtype", description = "orgtype") @RequestParam(required = false) String orgtype,
            @Parameter(name = "czurl", description = "czurl") @RequestParam(required = false) String czurl,
            @Parameter(name = "wt", description = "wt") @RequestParam(required = false) String wt,
            @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(required = false) String choiceSearch,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token

    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织*/
        //为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        NbsjBug tblBug = null;
        Set innerRules = null;
        Set outerRules = null;
        Set<NbsjBug> child = null;

        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        if (id != null && !"".equals(id)) {

            NbsjBug bug = nbsjBugService.getById(id);
            log.info("----" + bug);
            innerRules = bug.getTblInnerrules();
            outerRules = bug.getTblOuterrules();
            child = bug.getChildren();
            if (bug.getBugdapartment() != null) {
                Organization organization = organizationService.getById(bug.getBugdapartment());
                result.put("org", organization);
            }
            NbsjBugcriterion tblBugCriterion = nbsjBugcriterionService.findNbsjBugcriterionByorgid(id);
            if (tblBugCriterion != null) {
                result.put("tblBugCriterion", tblBugCriterion);
            }
        }
        List<BugCriterion> tblBugCriterionListByorgid = bugcriterionService.findTblBugCriterionListByorgid(orgid);
        result.put("list", tblBugCriterionListByorgid);
        result.put("choiceSearch", choiceSearch);
        result.put("tblBug", tblBug);
        result.put("innerRules", innerRules);
        result.put("outerRules", outerRules);
        result.put("child", child);
        result.put("wt", wt);
        result.put("url", url);
        result.put("type", type);
        result.put("czurl", czurl);
        result.put("backUrl", backUrl);
        result.put("orgid", orgid);
        result.put("orgtype", orgtype);
        jsonBean.setData(result);
        return jsonBean;
    }

    /**
     * 相关问题汇总 --缺陷管理    展示的分页数据和左侧的数据集
     *
     * @param
     * @param type
     * @return
     * @auther mamingxu
     * @version 1.0.1
     * @since 2022/8/13
     */
    @OperationLog(
            success = "展示的分页数据和左侧的数据集处理成功",
            busType = "相关问题汇总",
            fail = "展示的分页数据和左侧的数据集处理失败",
            operationType = OperationType.SELECT,
            subType = "缺陷管理"
    )
    @RequestMapping(value = "/qxwt/defect_index")
    @Operation(summary = "相关问题汇总 --缺陷管理    展示的分页数据和左侧的数据集/qxwt/defect_index")
    public JsonBean defect_xwt(
            @Parameter(name = "type", description = "type") @RequestParam(required = false) String type,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token

    ) throws Exception {
        TblStaffUtil staffUtil = userProvider.get();//得到了当前登录的用户信息
        if (staffUtil == null) {
		    return ResponseFormat.retParam(1, 20006, null);
		}
        
        TblOrganizationUtil staffOrg = staffUtil.getLinkOrg();  //当前用户所属组织
        TblOrganizationUtil selectOrg = staffUtil.getCurrentOrg(); //当前用户选择的组织

        JsonBean jsonBean = new JsonBean();
        Map result = new HashMap();
        result.put("type", type);
        result.put("orgid", staffOrg.getOrgid());
        result.put("orgtype", staffOrg.getOrgtype());

        jsonBean.setMsg("success");
        jsonBean.setCode(200);
        jsonBean.setData(result);
        return jsonBean;
    }


}
