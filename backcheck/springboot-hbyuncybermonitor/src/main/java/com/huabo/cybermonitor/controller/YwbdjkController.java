package com.huabo.cybermonitor.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.FormControlrule;
import com.huabo.cybermonitor.entity.FormControlruleDto;
import com.huabo.cybermonitor.entity.FormElements;
import com.huabo.cybermonitor.entity.FormInfo;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.service.IFormControlruleService;
import com.huabo.cybermonitor.service.IOrganizationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;




@RestController
@Slf4j
@Tag(name="表单监控-业务表单监控",description="表单监控-业务表单监控")
@RequestMapping(value = "/cyber/ywbdjkController")
@SuppressWarnings("all")
public class YwbdjkController {

	private static final Logger log = LoggerFactory.getLogger(YwbdjkController.class);

    private static final String formurl = "http://192.0.2.200:8001";

    @Autowired
    private IFormControlruleService iFormControlruleService;

    @Autowired
    IOrganizationService iorganizationService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 组织列表
     */
    @Operation(summary = "rulecontrol_org_list")
    @GetMapping("/rulecontrol/org_list")
    public JsonBean rulecontrol_org_list(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        Map map = new HashMap(1);
        map.put("orgid", iorganizationService.getById(staff.getCurrentOrg().getOrgid()));
        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则列表
     */
    @Operation(summary = "rule_list")
    @GetMapping("/rulecontrol/rule_list")
    public JsonBean rule_list(@Parameter(name = "orgId", description = "传deptid条件deptid,不传按orgid查询-116821") @RequestParam(value = "orgId", required = false) String orgId,
                              @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                              @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                              @Parameter(name = "ruleno", description = "nc/01") @RequestParam(value = "ruleno", required = false) String ruleno,
                              @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        FormControlrule tfcr=new FormControlrule();
        tfcr.setRuleno(ruleno);

        Map map = new HashMap();
        try {
            if (pageNumber == null) {
                pageNumber = 1;
            }

            if (orgId==null) {
                orgId = staff.getCurrentOrg().getOrgid().toString();
                tfcr.setOrgid(new BigDecimal(orgId));
            } else {
                tfcr.setDeptid(new BigDecimal(orgId));
            }
            IPage<FormControlrule> iPage = new Page<>(pageNumber, pageSize);
            QueryWrapper<FormControlrule> queryWrapper = new QueryWrapper<>();

            if (tfcr.getOrgid() != null) {
                queryWrapper.eq("ORGID", tfcr.getOrgid());
            }
            if (tfcr.getDeptid() != null) {
                queryWrapper.eq("DEPTID", tfcr.getDeptid());
            }
            if (tfcr.getRuleno() != null) {
                queryWrapper.like("RULENO",  tfcr.getRuleno());
            }
            queryWrapper.orderByDesc("createtime");
            map.put("orgId", orgId);
            map.put("pageInfo", iFormControlruleService.page(iPage, queryWrapper));
            Organization organization = iorganizationService.getById(staff.getCurrentOrg().getOrgid());
            map.put("department",organization);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 规则转到添加页面
     */
    @Operation(summary = "rule_gotoAddPages")
    @GetMapping("/rulecontrol/rule_gotoAddPage")
    public JsonBean rule_gotoAddPages(@Parameter(name = "orgId", description = "组织机构编码") @RequestParam(value = "orgId", required = true) String orgId,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        try {
            Organization org = this.iorganizationService.getById(orgId);
            map.put("org", org);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 保存规则
     *
     * @param attribute
     * @param longUser
     * @param rule
     * @param replacekey
     * @param replacekvalue
     * @param replacetype
     * @param reorg
     * @param orgid
     * @param eleId
     * @param fieldId
     * @return
     */
    @Operation(summary = "新增")
    @PostMapping("/rulecontrol/save_fromRule")
    public JsonBean save_fromRule(
            @Parameter(name = "rule", description = "rule")  FormControlruleDto rule,
            @Parameter(name = "replacekey", description = "replacekey") @RequestParam(value = "replacekey", required = false) String[] replacekey,
            @Parameter(name = "replacekvalue", description = "replacekvalue") @RequestParam(value = "replacekvalue", required = false) String[] replacekvalue,
            @Parameter(name = "replacetype", description = "replacetype") @RequestParam(value = "replacetype", required = false) String[] replacetype,
            @Parameter(name = "reorg", description = "orgid以逗号隔开形式 198333,198328") @RequestParam(value = "reorg", required = false) String reorg,
            @Parameter(name = "orgid", description = "所属部门id") @RequestParam(value = "orgid", required = false) String orgid,
            @Parameter(name = "eleId", description = "eleId") @RequestParam(value = "eleId", required = false) BigDecimal eleId,
            @Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = false) String fieldId,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token)throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}

        FormControlrule fc=new FormControlrule();


        fc.setCreatestaff(staff.getStaffid());
        fc.setCreatetime(LocalDateTime.now());
        fc.setDeptid(new BigDecimal(orgid));
        fc.setOrgid(staff.getCurrentOrg().getOrgid());
        fc.setReturnresult(rule.getReturnresult());
        fc.setRuleno(rule.getRuleno());
        fc.setRulememo(rule.getRulememo());
        fc.setRulesql(rule.getRulesql());
        fc.setRuletip(rule.getRuletip());
        fc.setRulestatus(rule.getRulestatus());
        fc.setRulename(rule.getRulename());

        if(fieldId != null){
            fc.setFieldid(new BigDecimal(fieldId));
        }
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            String rulesql = rule.getRulesql().toUpperCase();
            if (rulesql.indexOf("FROM") == -1 || rulesql.indexOf("SELECT") == -1 || rulesql.indexOf("CREATE") != -1 || rulesql.indexOf("DROP") != -1 || rulesql.indexOf("UPDATE") != -1 || rulesql.indexOf("DELETE") != -1 || rulesql.indexOf("GRANT") != -1) {
                resultMap.put("result", "规则对应SQL语句不规范！");
                return new JsonBean(200, "错误", resultMap);
            }
            String result = iFormControlruleService.insertFormControlRule(fc, replacekey, replacekvalue, replacetype, eleId, reorg);
            resultMap.put("result", 0);
            resultMap.put("ruleId", result);
            return new JsonBean(200, "成功", resultMap);

        } catch (Exception e) {
            resultMap.put("result", "规则对应SQL语句执行失败！");
            return new JsonBean(500, "规则对应SQL语句执行失败！", resultMap);
        }

    }


    /**
     * 规则转到修改页面
     */
    @Operation(summary = "rule_gotoModifyPage")
    @GetMapping("/rulecontrol/rule_gotoModifyPage")
    public JsonBean rule_gotoModifyPage(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid,
                                        @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        try {
            FormControlrule rule = this.iFormControlruleService.getById(ruleid);
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("ruleid", ruleid);
            String result = HttpClient.request(formurl + "/form/findFormEleNameByRuleId", fields, null);
            if (result != null && !"".equals(result)) {
                String[] eles = result.split(",");
                map.put("eleId", eles[0]);
                map.put("eleName", eles[1]);
            }
            map.put("rule", rule);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 自定义表单 表单列表页面  查询所有在业务流程中的表单
     */
    @Operation(summary = "查询列表")
    @GetMapping(value = "/rulecontrol/findFormInfoListInFlow")
    public JsonBean findFormInfoListInFlow(@Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
                                           @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                           @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                           @Parameter(name = "FORMNO", description = "FORMNO") @RequestParam(value = "FORMNO", required = false) String formNo,
                                           @Parameter(name = "FORMNAME", description = "FORMNAME") @RequestParam(value = "FORMNAME", required = false) String formName,
                                           @Parameter(name = "FORMNOSTATUS", description = "FORMNOSTATUS") @RequestParam(value = "FORMNOSTATUS", required = false) String formNoStatus,
                                           @Parameter(name = "FORMNAMESTATUS", description = "FORMNAMESTATUS") @RequestParam(value = "FORMNAMESTATUS", required = false) String formNameStatus,
                                           @Parameter(name = "idname", description = "idname") @RequestParam("idname") String idname,
                                           @Parameter(name = "textname", description = "textname") @RequestParam("textname") String textname) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map map = new HashMap();
        try {
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            // 在token 取到当前用户
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", staff.getCurrentOrg().getOrgid());
            fields.put("pageNumber", pageNumber);
            fields.put("pageSize", pageSize);
            fields.put("formNo", formNo);
            fields.put("formName", formName);
            fields.put("formNoStatus", formNoStatus);
            fields.put("formNameStatus", formNameStatus);
            String result = HttpClient.request(formurl + "/form/selectFormInfoListInTblFlow", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);
            PageInfo<FormInfo> pageInfo = new PageInfo<FormInfo>();
            if ("true".equals(resultjs.get("result").toString())) {
                pageInfo.setTotalRecord(Integer.parseInt(resultjs.get("count").toString()));
                String list = resultjs.get("list").toString();
                List<FormInfo> formList = JSONObject.parseArray(list, FormInfo.class);
                pageInfo.setTlist(formList);
            } else {
                List<FormInfo> list = new ArrayList<FormInfo>(0);
                pageInfo.setTotalRecord(0);
                pageInfo.setTlist(list);
            }
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setPageSize(pageSize);
            map.put("pageInfo", pageInfo);
            map.put("idname", idname);
            map.put("textname", textname);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 自定义表单 表单列表页面
     */
    @Operation(summary = "form_findFormReportListInfo")
    @GetMapping("/rulecontrol/findFormReportListInfo")
    public JsonBean form_findFormReportListInfo(@Parameter(name = "formId", description = "表单id") @RequestParam(value = "formId", required = true) Integer formId,
                                                @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        Map<String, Object> map = new HashMap<String, Object>(0);
        try {
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("formId", formId);
            String result = HttpClient.request(formurl + "/form/findFormReportListInfo", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);
            if ("true".equals(resultjs.get("result").toString())) {
                String list = resultjs.get("eleList").toString();
                List<FormElements> eleList = JSONObject.parseArray(list, FormElements.class);
                map.put("result", 0);
                map.put("eleList", eleList);
            } else {
                map.put("result", 1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 修改表单规则
     */
    @Operation(summary = "更新")
    @GetMapping("/rulecontrol/modify_fromRule")
    public JsonBean modify_fromRule(@Parameter(name = "rule", description = "rule")  FormControlruleDto rule,
                                    @Parameter(name = "reorg", description = "orgid以逗号隔开形式 198333,198328") @RequestParam(value = "reorg", required = false) String reorg,
                                    @Parameter(name = "replacekey", description = "replacekey") @RequestParam(value = "replacekey", required = false) String[] replacekey,
                                    @Parameter(name = "replacekvalue", description = "replacekvalue") @RequestParam(value = "replacekvalue", required = false) String[] replacekvalue,
                                    @Parameter(name = "replacetype", description = "replacetype") @RequestParam(value = "replacetype", required = false) String[] replacetype,
                                    @Parameter(name = "reid", description = "reid") @RequestParam(value = "reid", required = false) String[] reid,
                                    @Parameter(name = "eleId", description = "eleId") @RequestParam(value = "eleId", required = false) BigDecimal eleId,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            String rulesql = rule.getRulesql().toUpperCase();
            if (rulesql.indexOf("FROM") == -1 || rulesql.indexOf("SELECT") == -1 || rulesql.indexOf("CREATE") != -1 || rulesql.indexOf("DROP") != -1 || rulesql.indexOf("UPDATE") != -1 || rulesql.indexOf("DELETE") != -1 || rulesql.indexOf("GRANT") != -1) {
                resultMap.put("result", "规则对应SQL语句不规范！");
                return new JsonBean(200, "失败", resultMap);
            }
            String result = iFormControlruleService.modifyFormControlRule(rule, replacekey, replacekvalue, replacetype, eleId, reid, reorg);
            resultMap.put("result", 0);
            resultMap.put("ruleid", result);
        } catch (Exception e) {
            resultMap.put("result", "规则对应SQL语句执行失败！");
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", resultMap);
    }


    /**
     * 修改表单规则状态
     */
    @Operation(summary = "更新")
    @GetMapping("/rulecontrol/modify_ruleStatus")
    public JsonBean modify_ruleStatus(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid,
                                      @Parameter(name = "status", description = "status") @RequestParam(value = "status", required = true) String status,
                                      @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token
    ) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        try {
            this.iFormControlruleService.modifyFormStatus(ruleid, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "修改成功", "0");
    }


    /**
     * 删除表单规则
     */
    @Operation(summary = "删除")
    @GetMapping("/rulecontrol/remove_fromRule")
    public JsonBean remove_fromRule(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid,
                                    @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
        int tiao=0;
        TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        try {
            tiao=this.iFormControlruleService.remove_fromRule(new BigDecimal(ruleid));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "删除成功", tiao);
    }


    /**
     * 生成组织机构树
     *
     * @param nodeId
     * @param
     * @param orgId
     * @return
     */
    @Operation(summary = "查询树形结构")
    @GetMapping(value = "/rulecontrol/orgTree_list")
    public JsonBean findOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
                                           @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId,
                                           @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) throws Exception {
    	TblStaffUtil staff = userProvider.get();
 		if (staff == null) {
 			return ResponseFormat.retParam(0, 20006, null);
 		}
        String json = "";
        if (nodeId == null) {
            nodeId = orgId;
        }
        List<Tree> list = this.iorganizationService.getNodeAll(nodeId);
        for (Tree tree : list) {
            setUrlByTree(tree, "/formControl/rulecontrol/rule_list?orgId=");
            tree.setTarget("mainFramex");
        }
        return new JsonBean(200, "success", list);


    }

    /**
     * 获取树
     */
    private void setUrlByTree(Tree tree, String url) {
        for (Tree tre : tree.getChildren()) {
            if (tre.getChildren().size() > 0) {
                setUrlByTree(tre, url);
            }
            tre.setTarget("mainFramex");
            tre.setUrl(url + tre.getId());
        }
    }

}
