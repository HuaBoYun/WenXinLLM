package com.huabo.cybermonitor.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.net.util.Base64;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.EncryptUtil;
import com.hbfk.util.FtpUtil;
import com.hbfk.util.HttpClient;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.hbfk.util.ResponseFormat;
import com.hbfk.util.user.UserProvider;
import com.huabo.cybermonitor.entity.Attachment;
import com.huabo.cybermonitor.entity.FormControlrule;
import com.huabo.cybermonitor.entity.FormControlruleDto;
import com.huabo.cybermonitor.entity.FormElements;
import com.huabo.cybermonitor.entity.FormInfo;
import com.huabo.cybermonitor.entity.Organization;
import com.huabo.cybermonitor.entity.OutformField;
import com.huabo.cybermonitor.entity.Staff;
import com.huabo.cybermonitor.entity.Tree;
import com.huabo.cybermonitor.service.IFormControlruleService;
import com.huabo.cybermonitor.service.IOrganizationService;
import com.huabo.cybermonitor.service.IOutformFieldService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import net.sf.json.JSONArray;



/**
 * 表单监控api接口
 *
 * @author kangjx
 * @createTime 2022/7/11
 */
@RestController
@Tag(name="表单监控",description="表单监控")
@RequestMapping(value = "/cyber/FormMonitoringController")
public class FormMonitoringController {

	private static final Logger log = LoggerFactory.getLogger(FormMonitoringController.class);

    private static final String formurl = "http://192.0.2.200:8001";

    @Autowired
    private IFormControlruleService iFormControlruleService;

    @Autowired
    IOrganizationService iorganizationService;

    @Autowired
    IOutformFieldService iOutformFieldService;
    
    @Resource
    private UserProvider userProvider;

    /**
     * 组织列表
     */
    @Operation(summary = "rulecontrol_org_list")
    @GetMapping("/rulecontrol/org_list")
    public JsonBean rulecontrol_org_list(@Parameter(name = "organization", description = "organization") @RequestBody() Organization organization) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        return new JsonBean(200, "成功", iorganizationService.getById(organization.getOrgid()));
    }


    /**
     * 生成组织机构树
     *
     * @param nodeId
     * @param type
     * @param orgId
     * @return
     */
    @Operation(summary = "查询树形结构")
    @GetMapping(value = "/rulecontrol/orgTree_list")
    public JsonBean findOrganizationByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId", required = false) BigDecimal nodeId,
                                           @Parameter(name = "type", description = "type") @RequestParam(value = "type", required = false) String type,
                                           @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) BigDecimal orgId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
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

    /**
     * 规则列表
     */
    @Operation(summary = "rule_list")
    @GetMapping("/rulecontrol/rule_list")
    public JsonBean rule_list(@Parameter(name = "Orgid", description = "Orgid") @RequestParam(value = "Orgid", required = false) String Orgid,
                              @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = false) String orgId,
                              @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                              @Parameter(name = "tfcr", description = "tfcr") @RequestBody() FormControlrule tfcr,
                              @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch", required = false) String choiceSearch) throws Exception {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }

            if (orgId == null) {
                orgId = Orgid;
            } else {
                // tfcr.setLinkDeptId(new BigDecimal(orgId)); 缺少字段
            }
            PageInfo<FormControlrule> pageInfo = new PageInfo<FormControlrule>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setCondition(tfcr);
            map.put("orgId", orgId);
            this.iFormControlruleService.findPageInfo(pageInfo);
            map.put("pageInfo", pageInfo);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch);
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
                                      @Parameter(name = "choiceSearch", description = "是否显示") @RequestParam(value = "choiceSearch", required = true) String choiceSearch) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            Organization org = this.iorganizationService.getById(orgId);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("org", org);
            map.put("choiceSearch", choiceSearch);
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
    public JsonBean findFormInfoListInFlow(@Parameter(name = "Orgid", description = "Orgid") @RequestParam("Orgid") String Orgid,
                                           @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                           @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                           @Parameter(name = "FORMNO", description = "FORMNO") @RequestParam(value = "FORMNO", required = false) String formNo,
                                           @Parameter(name = "FORMNAME", description = "FORMNAME") @RequestParam(value = "FORMNAME", required = false) String formName,
                                           @Parameter(name = "FORMNOSTATUS", description = "FORMNOSTATUS") @RequestParam(value = "FORMNOSTATUS", required = false) String formNoStatus,
                                           @Parameter(name = "FORMNAMESTATUS", description = "FORMNAMESTATUS") @RequestParam(value = "FORMNAMESTATUS", required = false) String formNameStatus,
                                           @Parameter(name = "idname", description = "idname") @RequestParam("idname") String idname,
                                           @Parameter(name = "textname", description = "textname") @RequestParam("textname") String textname) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", Orgid);
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
    public JsonBean form_findFormReportListInfo(@Parameter(name = "formId", description = "表单id") @RequestParam(value = "formId", required = true) Integer formId) {
        Map<String, Object> map = new HashMap<String, Object>(0);
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
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
     * 保存规则
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
    public JsonBean save_fromRule(@Parameter(name = "attribute", description = "组织信息") @RequestParam(value = "attribute", required = true) Organization attribute,
                                  @Parameter(name = "longUser", description = "longUser") @RequestParam(value = "longUser", required = true) Staff longUser,
                                  @Parameter(name = "formId", description = "表单id") @RequestParam(value = "formId", required = true) FormControlrule rule,
                                  @Parameter(name = "replacekey", description = "replacekey") @RequestParam(value = "replacekey", required = false) String[] replacekey,
                                  @Parameter(name = "replacekvalue", description = "replacekvalue") @RequestParam(value = "replacekvalue", required = false) String[] replacekvalue,
                                  @Parameter(name = "replacetype", description = "replacetype") @RequestParam(value = "replacetype", required = false) String[] replacetype,
                                  @Parameter(name = "reorg", description = "reorg") @RequestParam(value = "reorg", required = false) String reorg,
                                  @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) String orgid,
                                  @Parameter(name = "eleId", description = "eleId") @RequestParam(value = "eleId", required = false) BigDecimal eleId,
                                  @Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = false) String fieldId) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            String rulesql = rule.getRulesql().toUpperCase();
            if (rulesql.indexOf("FROM") == -1 || rulesql.indexOf("SELECT") == -1 || rulesql.indexOf("CREATE") != -1 || rulesql.indexOf("DROP") != -1 || rulesql.indexOf("UPDATE") != -1 || rulesql.indexOf("DELETE") != -1 || rulesql.indexOf("GRANT") != -1) {
                resultMap.put("result", "规则对应SQL语句不规范！");
                return new JsonBean(200, "错误", resultMap);
            }
//            rule.setTblOrganizationByOrgid(attribute);
//            if (orgid != null) {
//                Organization dept = new Organization();
//                dept.setOrgid(new BigDecimal(orgid));
//                rule.setTblOrganizationByDeptid(dept);
//            }
//            if (fieldId != null) {
//                OutformField toff = new OutformField();
//                toff.setFieldid(new BigDecimal(fieldId));
//                rule.setTblOutformField(toff);
//            }
//
//            rule.setTblStaff(longUser);
            rule.setCreatetime(LocalDateTime.now());
            String result = iFormControlruleService.insertFormControlRule(rule, replacekey, replacekvalue, replacetype, eleId, reorg);
            resultMap.put("result", 0);
            resultMap.put("ruleId", result);
        } catch (Exception e) {
            resultMap.put("result", "规则对应SQL语句执行失败！");
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", resultMap);

    }


    /**
     * 规则转到修改页面
     */
    @Operation(summary = "rule_gotoModifyPage")
    @GetMapping("/rulecontrol/rule_gotoModifyPage")
    public JsonBean rule_gotoModifyPage(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid,
                                        @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch", required = true) String choiceSearch) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
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
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 表单规则删除
     */
    @Operation(summary = "删除")
    @GetMapping("/rulecontrol/remove_formControlEleRela")
    public JsonBean remove_formControlEleRela(@Parameter(name = "eleId", description = "eleId") @RequestParam(value = "eleId", required = true) String eleId) {
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            this.iFormControlruleService.removeById(eleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "删除成功", "0");
    }

    /**
     * 修改表单规则
     */
    @Operation(summary = "更新")
    @GetMapping("/rulecontrol/modify_fromRule")
    public JsonBean modify_fromRule(@Parameter(name = "rule", description = "rule") @RequestParam(value = "rule", required = true) FormControlruleDto rule,
                                    @Parameter(name = "reorg", description = "reorg") @RequestParam(value = "reorg", required = false) String reorg,
                                    @Parameter(name = "replacekey", description = "replacekey") @RequestParam(value = "replacekey", required = false) String[] replacekey,
                                    @Parameter(name = "replacekvalue", description = "replacekvalue") @RequestParam(value = "replacekvalue", required = false) String[] replacekvalue,
                                    @Parameter(name = "replacetype", description = "replacetype") @RequestParam(value = "replacetype", required = false) String[] replacetype,
                                    @Parameter(name = "reid", description = "reid") @RequestParam(value = "reid", required = false) String[] reid,
                                    @Parameter(name = "orgid", description = "orgid") @RequestParam(value = "orgid", required = false) String orgid,
                                    @Parameter(name = "eleId", description = "eleId") @RequestParam(value = "eleId", required = false) BigDecimal eleId) {
        HashMap<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            String rulesql = rule.getRulesql().toUpperCase();
            if (rulesql.indexOf("FROM") == -1 || rulesql.indexOf("SELECT") == -1 || rulesql.indexOf("CREATE") != -1 || rulesql.indexOf("DROP") != -1 || rulesql.indexOf("UPDATE") != -1 || rulesql.indexOf("DELETE") != -1 || rulesql.indexOf("GRANT") != -1) {
                resultMap.put("result", "规则对应SQL语句不规范！");
                return new JsonBean(200, "失败", resultMap);
            }
            String result = iFormControlruleService.modifyFormControlRule(rule, replacekey, replacekvalue, replacetype, eleId, reid, reorg);
            resultMap.put("result", 0);
        } catch (Exception e) {
            resultMap.put("result", "规则对应SQL语句执行失败！");
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", resultMap);
    }

    /**
     * 删除表单规则
     */
    @Operation(summary = "删除")
    @GetMapping("/rulecontrol/remove_fromRule")
    public JsonBean remove_fromRule(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid) {
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            this.iFormControlruleService.removeById(ruleid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "删除成功", "0");
    }

    /**
     * 修改表单规则状态
     */
    @Operation(summary = "更新")
    @GetMapping("/rulecontrol/modify_ruleStatus")
    public JsonBean modify_ruleStatus(@Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid,
                                      @Parameter(name = "status", description = "status") @RequestParam(value = "status", required = true) String status) {
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            this.iFormControlruleService.modifyFormStatus(ruleid, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "修改成功", "0");
    }


    /**
     * 检查表单规则
     */
    @Operation(summary = "校验")
    @GetMapping("/rulecontrol/checkFlowFromRule")
    public JsonBean checkFlowFromRule(@Parameter(name = "realName", description = "realName") @RequestParam(value = "realName", required = true)String realName,
                                      @Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = true)BigDecimal orgId,
                                      @Parameter(name = "jsonArry", description = "jsonArry") @RequestParam(value = "jsonArry", required = true) String jsonArry,
                                      @Parameter(name = "formId", description = "formId") @RequestParam(value = "formId", required = true) String formId) {
        String result = "0";
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
              result = this.iFormControlruleService.checkFlowFromRule(jsonArry,formId,realName,orgId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", "0");
    }

    /**
     * 检查列表表单规则执行
     */
    @Operation(summary = "校验")
    @GetMapping("/rulecontrol/checkArrayFormRuleExcute")
    public JsonBean checkArrayFormRuleExcute(@Parameter(name = "longUser", description = "longUser") @RequestParam(value = "longUser", required = true)Staff longUser,
                                             @Parameter(name = "jsonArry", description = "jsonArry") @RequestParam(value = "jsonArry", required = true) String jsonArry,
                                             @Parameter(name = "ruleNo", description = "ruleNo") @RequestParam(value = "ruleNo", required = true) String ruleNo) {
        String result = "0";
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            result = this.iFormControlruleService.checkFlowFromRuleByjsonArray(jsonArry,longUser.getRealname(),longUser.getOrgid(),ruleNo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", result);
    }

    /**
     * 返回字段列表
     */
    @Operation(summary = "outFileidRuleControl_list")
    @GetMapping("/rulecontrol/outFieldList")
    public JsonBean outFileidRuleControl_list(@Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = true) String fieldId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("fieldId", fieldId);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 字段列表
     */
    @Operation(summary = "field_list")
    @GetMapping("/rulecontrol/field_list")
    public JsonBean field_list(@Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = true) String fieldId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        Map map = new HashMap();
        map.put("fieldId", fieldId);
        return new JsonBean(200, "成功", map);
    }


    /**
     * 通过Tree查找表单列表
     */
    @Operation(summary = "查询列表")
    @GetMapping("/rulecontrol/findTblfieldListByTree")
    public JsonBean findTblfieldListByTree(@Parameter(name = "nodeId", description = "nodeId") @RequestParam(value = "nodeId", required = true) BigDecimal nodeId,
                                           @Parameter(name = "type", description = "type") @RequestParam(value = "type", required = true) String type,
                                           @Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = true) BigDecimal fieldId) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        String json = "";
        if (nodeId == null) {
            nodeId = fieldId;
        }
         List<Tree> list = this.iOutformFieldService.getNodeAll(nodeId);
		for (Tree tree : list) {
			setUrlByTree(tree, "/formControl/rulecontrol/outFileld_rule_list?fieldId=");
		}
        json = JSONObject.toJSONString(list);
        return new JsonBean(200, "成功", json);
    }

    /**
     * 条例列表
     */
    @Operation(summary = "outFileld_rule_list")
    @GetMapping("/rulecontrol/outFileld_rule_list")
    public JsonBean outFileld_rule_list(@Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch", required = false) String choiceSearch,
                                        @Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = false) Integer fieldId,
                                        @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                        @Parameter(name = "tfcr", description = "tfcr") @RequestBody() FormControlrule tfcr) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }
            tfcr.setFieldid(new BigDecimal(fieldId));
            PageInfo<FormControlrule> pageInfo = new PageInfo<FormControlrule>();
            pageInfo.setCurrentPage(pageNumber);
            pageInfo.setCondition(tfcr);
            map.put("fieldId", fieldId);
            this.iFormControlruleService.findFieldRulePageInfo(pageInfo);
            map.put("pageInfo", pageInfo);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 条例添加
     */
    @Operation(summary = "outFieldRule_gotoAddPage")
    @GetMapping("/rulecontrol/outFieldRule_gotoAddPage")
    public JsonBean outFieldRule_gotoAddPage(@Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch", required = true) String choiceSearch,
                                             @Parameter(name = "fieldId", description = "fieldId") @RequestParam(value = "fieldId", required = true) String fieldId) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            map.put("fieldId", fieldId);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 条例
     */
    @Operation(summary = "outFieldRule_gotoModifyPage")
    @GetMapping("/rulecontrol/outFieldRule_gotoModifyPage")
    public JsonBean outFieldRule_gotoModifyPage(@Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam(value = "choiceSearch", required = true) String choiceSearch,
                                                @Parameter(name = "ruleid", description = "ruleid") @RequestParam(value = "ruleid", required = true) String ruleid) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            List<Map<String,Object>> rule = this.iFormControlruleService.getLeftById(ruleid);
            String orgname = "";
            String orgIds = "";
            //数据库字段不匹配
            for(Map<String,Object> m1 : rule){
                orgname += m1.get("orgname");
                orgIds += m1.get("orgid");
            }
            orgname = orgname.substring(0, orgname.length() - 1);
            orgIds = orgIds.substring(0, orgIds.length() - 1);
            //    map.put("rule", rule);
            map.put("orgname", orgname);
            map.put("orgIds", orgIds);
            //为页面查找区域显隐藏赋值
            if (choiceSearch == null || "".equals(choiceSearch)) {
                choiceSearch = "hide";
            }
            map.put("choiceSearch", choiceSearch);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    //F:\工作文件\表单信息\NCV6.5发版说明\NCV6.5发版说明\NCV6.5产品功能介绍.xlsx (设备未就绪。) 前台直接传递 如果前端不写就后台直接写死

    /**
     * 导入Excel文件
     */
    @Operation(summary = "inputNcExcel")
    @GetMapping("/inputNcExcel")
    public JsonBean inputNcExcel(String filePath) throws Exception {
    	TblStaffUtil loginStaff = userProvider.get();
		if (loginStaff == null) {
			return ResponseFormat.retParam(0, 20006, null);
		}
        OutformField father = null;
        OutformField children = null;
        String type = "NC6";
//        String filePath = "F:\\工作文件\\表单信息\\NCV6.5发版说明\\NCV6.5发版说明\\NCV6.5产品功能介绍.xlsx";
        InputStream in = new FileInputStream(filePath);
        XSSFWorkbook workbook = new XSSFWorkbook(in);
        XSSFCell cell = null;
        XSSFRow row = null;
        String oneContent = null;
        String twoContent = null;
        father = new OutformField();
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
                        //cell.setCellType(1);//字符串
                        oneContent = cell.getStringCellValue();
                    }
                    cell = row.getCell(2);
                    if (cell != null) {
                        //获取单元格的值
                        //cell.setCellType(1);//字符串
                        twoContent = cell.getStringCellValue();
                    }
                    if (oneContent != null && oneContent.trim().length() > 0) {
                        father = new OutformField();
                        father.setFieldtype(type);
                        father.setFieldname(oneContent);
                        this.iOutformFieldService.save(father);
                    }
                    if (twoContent != null && twoContent.trim().length() > 0) {
                        children = new OutformField();
                        children.setFieldtype(type);
                        children.setFieldname(twoContent);
                        // children.setOutformField(father); 新项目无次字段
                        this.iOutformFieldService.save(children);
                    }
                }
            }
        }
        return new JsonBean(200, "成功", "0");
    }


    /**
     * 自定义表单 表单列表页面  查询所有在业务流程中的表单
     */
    @Operation(summary = "form_findRelationForm")
    @GetMapping(value = "/form/findRelationForm")
    public JsonBean form_findRelationForm(@Parameter(name = "orgId", description = "orgId") @RequestParam("orgId") String orgId,
                                          @Parameter(name = "idname", description = "idname") @RequestParam("idname") String idname,
                                          @Parameter(name = "textname", description = "textname") @RequestParam("textname") String textname,
                                          @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                          @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                          @Parameter(name = "FORMNO", description = "FORMNO") @RequestParam(value = "FORMNO", required = false) String formNo,
                                          @Parameter(name = "FORMNAME", description = "FORMNAME") @RequestParam(value = "FORMNAME", required = false) String formName,
                                          @Parameter(name = "FORMNOSTATUS", description = "FORMNOSTATUS") @RequestParam(value = "FORMNOSTATUS", required = false) String formNoStatus,
                                          @Parameter(name = "FORMNAMESTATUS", description = "FORMNAMESTATUS") @RequestParam(value = "FORMNAMESTATUS", required = false) String formNameStatus,
                                          @Parameter(name = "chooseId", description = "chooseId") @RequestParam(value = "chooseId", required = false) Integer chooseId) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("orgId", orgId);
            fields.put("pageNumber", pageNumber);
            fields.put("pageSize", pageSize);
            fields.put("formNo", formNo);
            fields.put("formName", formName);
            fields.put("formNoStatus", formNoStatus);
            fields.put("formNameStatus", formNameStatus);
            fields.put("formStatus", 4);
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
            map.put("chooseId", chooseId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


    /**
     * 自定义表单 绑定关联表单的列
     */
    @Operation(summary = "form_findRelationFormElements")
    @GetMapping(value = "/form/findRelationFormElements")
    public JsonBean form_findRelationFormElements(@Parameter(name = "idname", description = "idname") @RequestParam("idname") String idname,
                                                  @Parameter(name = "textname", description = "textname") @RequestParam("textname") String textname,
                                                  @Parameter(name = "chooseId", description = "chooseId") @RequestParam(value = "chooseId", required = false) Integer chooseId,
                                                  @Parameter(name = "elename", description = "elename") @RequestParam(value = "elename", required = false) String elename) {

        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            try {
                HashMap<String, Object> fields = new HashMap<String, Object>(0);
                fields.put("formId", chooseId);
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
            map.put("idname", idname);
            map.put("textname", textname);
            map.put("chooseId", chooseId);
            map.put("elename", elename);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }


//接口返回500 可能是测试数据问题

    /**
     * 自定义表单 表单列表页面  查询所有在业务流程中的表单
     */
    @Operation(summary = "form_chooseFormValueElePage")
    @GetMapping(value = "/form/chooseFormValueElePage")
    public JsonBean form_chooseFormValueElePage(@Parameter(name = "textname", description = "textname") @RequestParam("textname") String textname,
                                                @Parameter(name = "idname", description = "idname") @RequestParam("idname") String idname,
                                                @Parameter(name = "choiceSearch", description = "choiceSearch") @RequestParam("choiceSearch") String choiceSearch,
                                                @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                                                @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                @Parameter(name = "formId", description = "formId") @RequestParam(value = "formId", required = false) Integer formId,
                                                @Parameter(name = "jsonArry", description = "jsonArry") @RequestParam(value = "jsonArry", required = false) String jsonArry,
                                                @Parameter(name = "paramArry", description = "paramArry") @RequestParam(value = "paramArry", required = false) String paramArry,
                                                @Parameter(name = "eleName", description = "eleName") @RequestParam(value = "eleName", required = false) String eleName,
                                                @Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = false) String valueId,
                                                @Parameter(name = "queryType", description = "queryType") @RequestParam(value = "queryType", required = false) Integer queryType,
                                                @Parameter(name = "oldFormId", description = "oldFormId") @RequestParam(value = "oldFormId", required = false) Integer oldFormId) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("formId", formId);
            fields.put("pageNumber", pageNumber);
            fields.put("pageSize", pageSize);
            fields.put("queryType", queryType);
            fields.put("oldFormId", oldFormId);
            fields.put("eleName", eleName);
            if (jsonArry != null && !"".equals(jsonArry) && jsonArry.length() > 2) {
                jsonArry = jsonArry.substring(0, jsonArry.length() - 1) + ",[\"VALUESTATUS\",\"等于\",\"6\"]";
            } else {
                jsonArry = "[\"VALUESTATUS\",\"等于\",\"6\"]";
            }
            fields.put("jsonArry", jsonArry);
            String result = HttpClient.request(formurl + "/formReport/findFormReportList", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);

            PageInfo<FormElements> pageInfo = new PageInfo<FormElements>();
            if ("true".equals(resultjs.get("result").toString())) {
                pageInfo.setTotalRecord(Integer.parseInt(resultjs.get("count").toString()));
                com.alibaba.fastjson.JSONArray array = resultjs.getJSONArray("mapList");
                map.put("array", array);
                String eleListstr = resultjs.get("eleList").toString();
                List<FormElements> eleList = JSONObject.parseArray(eleListstr, FormElements.class);
                map.put("eleList", eleListstr);
                map.put("eleLists", eleList);
                map.put("formName", resultjs.get("formName").toString());
            } else {
                pageInfo.setTotalRecord(0);
            }
            pageInfo.setCurrentPage(pageNumber);
            map.put("formId", formId);
            map.put("pageInfo", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("idname", idname);
        map.put("textname", textname);
        map.put("eleName", eleName);
        map.put("valueId", valueId);
        map.put("arrayMap", printJsonArray(jsonArry, paramArry));
        //为页面查找区域显隐藏赋值
        if (choiceSearch == null || "".equals(choiceSearch)) {
            choiceSearch = "hide";
        }
        map.put("choiceSearch", choiceSearch);
        return new JsonBean(200, "成功", map);
    }

    /**
     * json转换功能
     * @param jsonArry
     * @param paramArry
     * @return
     */
    public JSONObject printJsonArray(String jsonArry, String paramArry) {
        JSONObject object = new JSONObject();
        if (jsonArry != null) {
            JSONArray jsonArray = JSONArray.fromObject(jsonArry);
            Object[] objs = null;
            for (int i = 0; i < jsonArray.size(); i++) {
                objs = jsonArray.getJSONArray(i).toArray();
                object.put(objs[0].toString(), objs[2].toString());
            }
        }
        if (paramArry != null) {
            JSONArray paramArray = JSONArray.fromObject(paramArry);
            Object[] objs = null;
            for (int i = 0; i < paramArray.size(); i++) {
                objs = paramArray.getJSONArray(i).toArray();
                object.put(objs[0].toString(), objs[2].toString());
            }
        }
        return object;
    }


    /**
     * 自定义表单 表单列表页面  查询所有在业务流程中的表单
     */
    @Operation(summary = "form_chooseFormValueElePageByPrivate")
    @GetMapping(value = "/form/chooseFormValueElePageByPrivate")
    public JsonBean form_chooseFormValueElePageByPrivate(@Parameter(name = "eleName", description = "eleName") @RequestParam(value = "eleName", required = true) String eleName,
                                                         @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = true) String textname,
                                                         @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = true) String idname,
                                                         @Parameter(name = "pageNumber", description = "pageNumber") @RequestParam(value = "pageNumber", required = true) Integer pageNumber,
                                                         @Parameter(name = "pageSize", description = "pageSize") @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                         @Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = true) String valueId,
                                                         @Parameter(name = "queryType", description = "queryType") @RequestParam(value = "queryType", required = true) Integer queryType,
                                                         @Parameter(name = "oldFormId", description = "oldFormId") @RequestParam(value = "oldFormId", required = true) Integer oldFormId,
                                                         @Parameter(name = "fromGroupId", description = "fromGroupId") @RequestParam(value = "fromGroupId", required = true) Integer fromGroupId,
                                                         @Parameter(name = "jsonArry", description = "jsonArry") @RequestParam(value = "jsonArry", required = true) String jsonArry) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            if (pageNumber == null) {
                pageNumber = 1;
            }
            if (pageSize == null) {
                pageSize = 15;
            }
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("fromGroupId", fromGroupId);
            fields.put("pageNumber", pageNumber);
            fields.put("pageSize", pageSize);
            fields.put("queryType", queryType);
            fields.put("oldFormId", oldFormId);
            fields.put("eleName", eleName);
            fields.put("noweleName", eleName);
            if (jsonArry != null && !"".equals(jsonArry)) {
                jsonArry = jsonArry.substring(0, jsonArry.length() - 1) + ",[\"VALUESTATUS\",\"等于\",\"6\"]]";
            } else {
                jsonArry = "[\"VALUESTATUS\",\"等于\",\"6\"]";
            }
            fields.put("jsonArry", jsonArry);
            String result = HttpClient.request(formurl + "/formReport/findFormReportListByPrivate", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);

            PageInfo<FormElements> pageInfo = new PageInfo<FormElements>();
            if ("true".equals(resultjs.get("result").toString())) {
                pageInfo.setTotalRecord(Integer.parseInt(resultjs.get("count").toString()));
                com.alibaba.fastjson.JSONArray array = resultjs.getJSONArray("mapList");
                map.put("array", array);
                String eleListstr = resultjs.get("eleList").toString();
                List<FormElements> eleList = JSONObject.parseArray(eleListstr, FormElements.class);
                String formInfo = resultjs.get("formInfo").toString();
                FormInfo info = JSONObject.parseObject(formInfo, FormInfo.class);
                map.put("eleList", eleListstr);
                map.put("eleLists", eleList);
                map.put("formInfo", info);
            } else {
                pageInfo.setTotalRecord(0);
            }
            pageInfo.setCurrentPage(pageNumber);
            map.put("pageInfo", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("idname", idname);
        map.put("textname", textname);
        map.put("eleName", eleName);
        map.put("valueId", valueId);
        map.put("fromGroupId", fromGroupId);
        return new JsonBean(200, "成功", map);
    }

    /**
     * 查找表单参数
     */
    @Operation(summary = "form_findChooseFormValue")
    @GetMapping(value = "/form/findChooseFormValue", produces = "application/json; charset=utf-8")
    public JsonBean form_findChooseFormValue(@Parameter(name = "eleName", description = "eleName") @RequestParam(value = "eleName", required = true) String eleName,
                                             @Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = true) String valueId,
                                             @Parameter(name = "filedsNameStr", description = "filedsNameStr") @RequestParam(value = "filedsNameStr", required = false) String filedsNameStr) {
        String result = "0";
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("valueId", valueId);
            fields.put("eleName", eleName);
            fields.put("filedsNameStr", filedsNameStr);
            result = HttpClient.request(formurl + "/formReport/findChooseFormValue", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);
            if ("0".equals(resultjs.get("result").toString())) {
                result = resultjs.get("eleMap").toString();
            } else {
                result = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", "0");
    }


    /**
     * 表单计划
     */
    @Operation(summary = "cal_calcualte")
    @GetMapping("/calculate/formulacal")
    public JsonBean cal_calcualte(@Parameter(name = "calFormula", description = "calFormula") @RequestParam(value = "calFormula", required = true) String calFormula,
                                  @Parameter(name = "valjson", description = "valjson") @RequestParam(value = "valjson", required = false) String valjson,
                                  @Parameter(name = "eletext", description = "eletext") @RequestParam(value = "eletext", required = false) String eletext) {
        BigDecimal result = null;
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            result = this.iFormControlruleService.calculateFormula(calFormula, valjson, eletext);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", result);
    }

    //   调用有问题，Controller调用没问题，后台数据问题

    /**
     * 详细表单报告
     */
    @Operation(summary = "form_gotoDetailFormReportPage")
    @GetMapping("/form/gotoDetailFormReportPage")
    public JsonBean form_gotoDetailFormReportPage(@Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = true) String valueId,
                                                  @Parameter(name = "formId", description = "formId") @RequestParam(value = "formId", required = true) Integer formId) {
        Map map = new HashMap();
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("valueId", valueId);
            fields.put("formId", formId);
            String result = HttpClient.request(formurl + "/formReport/findFormReportInfo", fields, null);
            JSONObject resultjs = JSONObject.parseObject(result);
            if ("true".equals(resultjs.get("result").toString())) {
                String formInfo = resultjs.get("formInfo").toString();
                FormInfo info = JSONObject.parseObject(formInfo, FormInfo.class);
                String attList = resultjs.get("attList").toString();
                if (attList != null) {
                    List<Attachment> eleList = JSONObject.parseArray(attList, Attachment.class);
                    map.put("attList", eleList);
                }
                map.put("formInfo", info);
                String formValue = resultjs.get("formValue").toString();
                map.put("formValue", formValue);
            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", map);
    }

    /**
     * 表单核对
     */
    @Operation(summary = "form_checkFormNo")
    @GetMapping("/form/checkFormNo")
    public JsonBean form_checkFormNo(@Parameter(name = "orgId", description = "orgId") @RequestParam(value = "orgId", required = true) String orgId,
                                     @Parameter(name = "formId", description = "formId") @RequestParam(value = "formId", required = true) Integer formId,
                                     @Parameter(name = "jsonArry", description = "jsonArry") @RequestParam(value = "jsonArry", required = true) String jsonArry,
                                     @Parameter(name = "dclType", description = "dclType") @RequestParam(value = "dclType", required = true) Integer dclType,
                                     @Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = false) Integer valueId) {
        String result = "0";
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("formId", formId);
            fields.put("jsonArry", jsonArry);
            fields.put("dclType", dclType);
            fields.put("valueId", valueId);
            fields.put("orgId", orgId);
            result = HttpClient.request(formurl + "/formReport/checkFormInfoNo", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", result);
    }

    /**
     * 文件名核对
     */
    @Operation(summary = "file_checkFileName")
    @GetMapping("/file/checkFileName")
    public JsonBean file_checkFileName(@Parameter(name = "filename", description = "filename") @RequestParam(value = "filename", required = true) String filename,
                                       @Parameter(name = "valueId", description = "valueId") @RequestParam(value = "valueId", required = true) Integer valueId) {
        String result = "0";
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            HashMap<String, Object> fields = new HashMap<String, Object>(0);
            fields.put("filename", filename);
            fields.put("valueId", valueId);
            result = HttpClient.request(formurl + "/formReport/file/checkFileName", fields, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", result);
    }

    /**
     * 报告信息
     */
    @Operation(summary = "reportMenu_reportInfo")
    @GetMapping("/reportMenu/reportInfo")
    public JsonBean reportMenu_reportInfo(@Parameter(name = "model", description = "model") @RequestParam(value = "model", required = true) Model model,
                                          @Parameter(name = "menuId", description = "menuId") @RequestParam(value = "menuId", required = true) String menuId) {
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            String url = menuId;
            String token = EncryptUtil.getInstance().DESencode("fengkong1", "hbyun");

            long time = System.currentTimeMillis();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(("hbyfk" + "hbyfk147852" + time).getBytes("utf-8"));
            String encodeBase64String = Base64.encodeBase64String(digest);
            String sign = URLEncoder.encode(encodeBase64String.trim(), "utf-8");
            Properties prop = new Properties();
            prop.load(this.getClass().getClassLoader().getResourceAsStream("setting/jdbc.properties"));
            String reporturl = prop.get("reporturl").toString();
            model.addAttribute("reporturl", reporturl);
            model.addAttribute("time", time);
            model.addAttribute("sign", sign);
            model.addAttribute("url", "/" + url);
            model.addAttribute("token", token);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", "/form/formBiReport");
    }

    /**
     * 媒体播放
     */
    @Operation(summary = "formRpeort_videoPlay")
    @GetMapping("/formRpeort/videoPlay")
    public JsonBean formRpeort_videoPlay(@Parameter(name = "model", description = "model") @RequestParam(value = "model", required = true) Model model,
                                         @Parameter(name = "videoId", description = "videoId") @RequestParam(value = "videoId", required = true) Integer videoId) {
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            String url = this.iFormControlruleService.findVideoPlayUrl(videoId);
            model.addAttribute("videoUrl", url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JsonBean(200, "成功", "/form/fromVideo");
    }
    /************修改到当前位置 **********/
    /**
     * 表单权限
     */
    @Operation(summary = "formRpeort_docPirvew")
    @GetMapping("/formRpeort/docPirvew")
    public JsonBean formRpeort_docPirvew(@Parameter(name = "path", description = "path") @RequestParam(value = "path", required = true) String path,
                                         @Parameter(name = "model", description = "model") @RequestParam(value = "model", required = true) Model model,
                                         @Parameter(name = "docId", description = "docId") @RequestParam(value = "docId", required = true) Integer docId) {
        int bytesRead = 0;
        OutputStream os = null;
        InputStream fileStream = null;
        File docFile = null;
        File outputFile = null;
        OpenOfficeConnection connection = null;
        try {
        	TblStaffUtil loginStaff = userProvider.get();
			if (loginStaff == null) {
				return ResponseFormat.retParam(0, 20006, null);
			}
            String docName = this.iFormControlruleService.findDocFileName(docId);

            byte[] bytes = FtpUtil.getInputStream("uploadfile", docName);
            fileStream = new ByteArrayInputStream(bytes);
            //地址 前台传递或者后台写死
            String docpath = path+"/"+docName;
            String pdfname = docName.substring(0,docName.indexOf(".")-1)+".pdf";

            String pdfpath = path+"/"+pdfname;
            docFile = new File(docpath);
            os = new FileOutputStream(docFile);

            byte[] buffer = new byte[1024];
            while ((bytesRead = fileStream.read(buffer, 0, 1024)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();

            if(docName.indexOf(".pdf") == -1) {
                // 输出文件目录
                outputFile = new File(pdfpath);
                if (!outputFile.getParentFile().exists()) {
                    outputFile.getParentFile().exists();
                }
                // 调用openoffice服务线程
                //String command = "D:\\tools\\OpenOffice 4\\program\\soffice.exe -headless -accept=\"socket,host=127.0.0.1,port=8100\"";
                //Process p = Runtime.getRuntime().exec(command);

                // 连接openoffice服务
                connection = new SocketOpenOfficeConnection(FtpUtil.openOfficeip, Integer.parseInt(FtpUtil.openOfficeport));
                connection.connect();
                // 转换word到pdf
                DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
                converter.convert(docFile, outputFile);
                // 关闭连接
                // 关闭进程
                //p.destroy();
                System.out.println("转换完成！");
                model.addAttribute("pdfurl", pdfname);
                if (docFile != null) {
                    docFile.delete();
                }
            }else {
                model.addAttribute("pdfurl", docName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new JsonBean(200, "成功", "/pdfPreview/pdfPreviewing");
    }


}
