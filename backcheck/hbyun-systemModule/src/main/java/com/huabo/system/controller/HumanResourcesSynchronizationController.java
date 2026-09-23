package com.huabo.system.controller;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.service.TblJobGradeService;
import com.huabo.system.service.TblJobService;
import com.huabo.system.service.TblOrganizaService;
import com.huabo.system.service.TblStaffService;
import com.huabo.system.utils.DesUtil;
import com.huabo.system.utils.SHAUtil;
import com.huabo.system.utils.StringUtil;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 组织架构同步控制器
 * <p>提供人力资源系统的组织架构、岗位职级、人员信息同步接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name = "组织架构同步Controller", description = "组织架构同步")
public class HumanResourcesSynchronizationController {
    private static final Log logger = LogFactory.getLog(HumanResourcesSynchronizationController.class);
    public static ResourceBundle hr = ResourceBundle.getBundle("setting/humanResources");


    @Resource
    private TblStaffService tblStaffService;

    @Resource
    private TblJobService tblJobService;

    @Resource
    private TblOrganizaService TblOrganizaService;

    @Resource
    private TblJobGradeService tblJobGradeService;

    /**
     * 登录手机号
     */
    String phone = hr.getString("phone");

    /**
     * 开发者账号
     */
    String dev_account = hr.getString("dev_account");

    /**
     * 开发者密码
     */
    String dev_password = hr.getString("dev_password");

    /**
     * 缓存token
     */
    String dev_token;

    /**
     * 账号ken
     */
    String userId;

    /**
     * 分页记录数
     */
    int pageSize = 100;

    /**
     * 获取管理员id
     *
     * @param token
     * @return
     */
    public static String getAdminId(String token) {
        Map<String, Object> params = new HashMap<>();
        params.put("dev_token", token);
        String url = hr.getString("serverUrl") + "api/org.do?method=findAdmin";
        String result = HttpUtil.get(url, params);
        JSONObject msg = getResultMsg(result);
        if (msg != null) {
            JSONArray jsonArray = msg.getJSONArray("persons");
            if (jsonArray.size() > 0) {
                JSONObject adminJson = jsonArray.getJSONObject(0);
                return adminJson.getString("id");
            }
        }
        return null;
    }


    /**
     * 必要参数
     *
     * @return
     * @throws Exception
     */
    private String getRequiredParams() throws Exception {
        String devToken = getToken(this.dev_account, this.dev_password);
        userId = getUserId(this.phone, devToken);
        return String.format("&dev_token=%s&user_id=%s&dev_login_type=%s", devToken, userId, "PHONE");
    }

    @RequestMapping(value = "/postJobGrade", method = {RequestMethod.POST})
    @Operation(summary = "同步职级")
    public String postJobGrade(HttpServletRequest request) {
        try {
            String requiredParams = getRequiredParams();
            String url = hr.getString("serverUrl") + hr.getString("JobGardeUrl") + requiredParams;
            String result = HttpUtil.post(url, "");
            tblJobGradeService.syncJobGrade(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return "";
    }


    @RequestMapping(value = "/postJob", method = {RequestMethod.POST})
    @Operation(summary = "同步岗位")
    public String postJob(HttpServletRequest request) {
        try {
            String requiredParams = getRequiredParams();
            String url = hr.getString("serverUrl") + hr.getString("JobUrl") + "&includeDeleteData=true" + requiredParams;
            String result = HttpUtil.post(url, "");
            tblJobService.syncJob(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return "";
    }


    @RequestMapping(value = "/postOrg", method = {RequestMethod.POST})
    @Operation(summary = "同步部门")
    public String postOrg(HttpServletRequest request) {
        try {
            String requiredParams = getRequiredParams();
            String url = hr.getString("serverUrl") + hr.getString("orgUrl") + "&parentId=1&includeChildren=true&includeSelf=true" + requiredParams;
            String data = HttpUtil.post(url, "");
            TblOrganizaService.syncOrg(data);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping(value = "/postPerson", method = {RequestMethod.POST})
    @Operation(summary = "同步人员")
    public String postPerson(HttpServletRequest request) {
        try {
            String requiredParams = getRequiredParams();
            String url = hr.getString("serverUrl") + hr.getString("personUrl") + "&includeDeleteData=true&includeCustom=true" + requiredParams;
            String urlPage = url.concat("&pageIndex=").concat("1").concat("&pageSize=").concat(hr.getString("userPageSize"));
            String data = HttpUtil.post(urlPage, "");
            net.sf.json.JSONObject srcJson = net.sf.json.JSONObject.fromObject(data);
            net.sf.json.JSONObject msgJson = net.sf.json.JSONObject.fromObject(srcJson.getString("msg"));
            if (srcJson.getBoolean("success") == true) {
                int totalpage = msgJson.getInt("totalPage");
                for (int pageIndex = 1; pageIndex <= totalpage; pageIndex++) {
                    String urlPg = url.concat("&pageIndex=").concat(String.valueOf(pageIndex)).concat("&pageSize=").concat(hr.getString("userPageSize"));
                    String respData = HttpUtil.post(urlPg, "");
                    tblStaffService.syncPerson(respData);
                }
            }
            return "succ";
        } catch (Exception e) {
            e.printStackTrace();
            return "err:" + e.getMessage();
        }
    }

    public static String getUserId(String phone, String token) {
        if (StringUtil.isEmpty(phone)) {
            throw new IllegalArgumentException("需要加密成userId的种子码不能为空");
        }
        try {
            return DesUtil.encode(phone, token + hr.getObject("dev_password"));
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 校验token是否有效
     *
     * @param accessToken
     * @return
     */
    public static boolean verifyAccessToken(String accessToken) {
        String checkTokenURL = hr.getString("serverUrl") + "/api/auth.do?method=checkToken&dev_token=" + accessToken;
        try {
            String checkTokenResult = HttpUtil.post(checkTokenURL, "");
            if (StringUtils.isEmpty(checkTokenResult)) {
                return false;
            }
            cn.hutool.json.JSONObject tokenResultJSON = JSONUtil.parseObj(checkTokenResult);
            if (!tokenResultJSON.getBool("success")) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * {"errcode":0,"msg":{"dev_token":"f1ee5f87-c954-4dca-8b24-c4ebbc8377f2","expires_in":7200},"async":false,"success":true,"waitSecond":0}
     *
     * @param devUser
     * @param devPass
     * @return
     * @throws Exception
     */
    public String getToken(String devUser, String devPass) throws Exception {
        boolean valid = verifyAccessToken(this.dev_token);
        if (!valid) {
            Map<String, Object> params = new HashMap<>();
            params.put("grant_type", "client_credentials");//认证模式
            params.put("dev_account", devUser);//开发帐号
            params.put("dev_password", SHAUtil.SHA1(devPass));//开发者密码
            String url = hr.getString("serverUrl") + hr.getString("tokenUrl");
            String result = HttpUtil.post(url, params);
            if (StringUtil.isNotEmpty(result)) {
                JSONObject msg = getResultMsg(result);
                String devToken = msg.getString("dev_token");
                this.dev_token = devToken;
                return devToken;
            }
        }
        return this.dev_token;
    }

    public static JSONObject getResultMsg(String result) {
        if (StringUtil.isNotEmpty(result)) {
            JSONObject json = com.alibaba.fastjson.JSON.parseObject(result);
            return json.getJSONObject("msg");
        }
        return null;
    }


    //人力资源同步
    @RequestMapping(value = "/syncinterface/syncUser", method = {RequestMethod.POST})
    @Operation(summary="人力资源信息系统--人员同步")
    public Map<String, Object> saveorupdate(HttpServletRequest request,
                                            @Parameter(name = "operaType", description = "操作类型 0 -新增、1-修改、2-启用弃用", required = true) Integer operaType,
                                            @Parameter(name = "data", description = "推送人员集合", required = true) @RequestParam(value = "data", required = true) String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            resultMap = this.tblStaffService.syncUser(operaType, data);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/syncinterface/syncPost", method = {RequestMethod.POST})
    @Operation(summary="人力资源信息系统--岗位同步")
    public Map<String, Object> syncPost(HttpServletRequest request,
                                        @Parameter(name = "operaType", description = "操作类型 0 -新增、1-修改、2-启用弃用", required = true) Integer operaType,
                                        @Parameter(name = "data", description = "推送岗位集合", required = true) @RequestParam(value = "data", required = true) String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            resultMap = this.tblJobService.syncPost(operaType, data);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return resultMap;
    }

    @RequestMapping(value = "/syncinterface/syncAllOrg", method = {RequestMethod.POST})
    @Operation(summary="人力资源信息系统--部门同步")
    public Map<String, Object> syncAllOrg(HttpServletRequest request,
                                          @Parameter(name = "operaType", description = "操作类型 0 -新增、1-修改、2-启用弃用", required = true) Integer operaType,
                                          @Parameter(name = "data", description = "推送部门集合", required = true) @RequestParam(value = "data", required = true) String data) {
        Map<String, Object> resultMap = new HashMap<String, Object>(0);
        try {
            resultMap = this.TblOrganizaService.syncAllOrg(operaType, data);
        } catch (Exception e) {
            ResponseFormat.retParam(1, 1000, e.getMessage());
        }
        return resultMap;
    }
}
