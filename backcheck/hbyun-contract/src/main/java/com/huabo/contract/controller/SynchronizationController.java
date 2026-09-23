package com.huabo.contract.controller;


import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.annotation.Resource;

import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.huabo.contract.service.TblContractCollectionService;
import com.huabo.contract.service.TblContractPaymenService;
import com.huabo.contract.service.TblContractPlannodeService;
import com.huabo.contract.service.TblContractProjectService;
import com.huabo.contract.service.TblContractSpnodeService;
import com.huabo.contract.service.TblCyhwProjectbudgetService;
import com.huabo.contract.service.TblCyhwUnitService;
import com.huabo.contract.service.TblFundPoolService;
import com.huabo.contract.service.TblInvestmentProjectService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;

/**
 * 第三方接口控制器
 * <p>提供合同信息的第三方同步、履行阶段查询、附件信息查询等对外接口</p>
 *
 * @author hbyun
 */
@RestController
@Slf4j
@Tag(name="第三方接口Controller",description="第三方接口Controller")
public class SynchronizationController {
    private static final Log logger = LogFactory.getLog(SynchronizationController.class);
    public static ResourceBundle ncc = ResourceBundle.getBundle("setting/zcncc");

    @Resource
    private TblCyhwProjectbudgetService projectbudgetService;

    @Resource
    private TblFundPoolService tblFundPoolService;

    @Resource
    private TblInvestmentProjectService tblInvestmentProjectService;
    //合同表
    @Resource
    private TblCyhwUnitService tblCyhwUnitService;
    //合同履行阶段
    @Resource
    private TblContractPlannodeService tblContractPlannodeService;

    //合同履行审批
    @Resource
    private TblContractSpnodeService tblContractSpnodeService;
    //收款单
    @Resource
    private TblContractCollectionService tblContractCollectionService;

    //付款单
    @Resource
    private TblContractPaymenService tblContractPaymentServic;

    @Resource
    private TblCyhwProjectbudgetService tblCyhwProjectbudgetService;

    @Resource
    private TblContractProjectService tblContractProjectService;
    
    @Resource
    private UserProvider userProvider;


    /*
     * 5.合同履行阶段信息接口同步。
     * contractId 合同信息主键  必填
     *  ------------------------------------差"wfnodemoney":400000			--该履行阶段未付金额
     * */
    @RequestMapping(value = "/synchronization/contractInfo/getPerformanceStageList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "合同履行阶段信息接口同步")
    public @ResponseBody
    String getPerformanceStageList(
            @Parameter(name = "contractId", description = "合同信息主键", required = true) BigDecimal contractId) throws Exception {
        String json = "";
        JSONObject obj = new JSONObject();
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> map = tblContractPlannodeService.findPlannodeListByContractId(contractId);

            json = JSONObject.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }




    /*
     * 合同收付款单接口
     * HTTP请求方式	HTTPS-POST
     * type（必填）:BXD =报销单   JKD=借款单  SKJSD=收款结算单
     * */


//	@Scheduled(cron = "0 0 22 ? * *")  //间隔1秒
//	public void  scheduledTaskPaymentInformation(){
//		paymentInformation("SKJSD");
//		paymentInformation("BXD");
//		paymentInformation("JKD");
//	}

    public String paymentInformation(String type) {
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        org.apache.http.HttpResponse reponse = null;
        String result = null;
        try {
            List<String> ids = tblContractCollectionService.getSkContractIds();
            PostMethod postMethod = new PostMethod(ncc.getObject("paymenturl").toString());
            com.alibaba.fastjson.JSONObject obj = new com.alibaba.fastjson.JSONObject();
            obj.put("type", type);
            String _id = "";
            if (ids != null && ids.size() > 0) {
                for (String id : ids) {
                    _id += "\"" + id + "\",";
                }
                _id = _id.substring(0, _id.length() - 1);
                obj.put("contractIds", "[" + _id + "]");
                System.out.println(obj.toString());
                postMethod.setRequestHeader("Content-Type", "application/json");
                RequestEntity entity = new StringRequestEntity(obj.toString(), "application/json", "UTF-8");
                postMethod.setRequestEntity(entity);
                //base64位加密传递用户名密码
                //String encodeing=DatatypeConverter.printBase64Binary("testsj001:testsj001".getBytes("utf-8"));
                //postMethod.setRequestHeader("Authorization","Basic "+encodeing); //java.util.Base64.getUrlEncoder().encodeToString(("testsj001"+":"+"testsj001").getBytes("utf-8"))
                client.executeMethod(postMethod);
                java.io.InputStream in = postMethod.getResponseBodyAsStream();
                //下面将stream转换为String
                StringBuffer sb = new StringBuffer();
                InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                char[] b = new char[4096];
                for (int n; (n = isr.read(b)) != -1; ) {
                    sb.append(new String(b, 0, n));
                }
                result = sb.toString();
                System.out.println(result);
                net.sf.json.JSONObject j = net.sf.json.JSONObject.fromObject(result);
                if (j.get("status").toString().equals("200")) {
                    net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(j.get("data"));
                    if (type.equals("BXD") || type.equals("JKD")) { //BXD =报销单   JKD=借款单
                        tblContractPaymentServic.setData(arr, type);
                    } else if (type.equals("SKJSD")) { //SKJSD=收款结算单
                        tblContractCollectionService.setData(arr);
                    }
                    return JsonBean.success();
                } else {
                    return JsonBean.error();
                }
            }
            return JsonBean.success();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return JsonBean.error(e.getMessage());
        }
    }

    /*
     * 6.合同附件接口同步
     * contractId 合同信息主键  必填
     *
     * */
    @RequestMapping(value = "/synchronization/contractFjInfo/getList", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "合同附件接口同步")
    public @ResponseBody
    String contractFjInfoGetList(@Parameter(name = "contractId", description = "合同信息主键", required = true) BigDecimal contractId) throws Exception {
        String json = "";
        JSONObject obj = new JSONObject();
        String date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Map<String, Object> map = tblCyhwUnitService.contractFjInfo(contractId);

            json = JSONObject.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


    /*
     * 6.根据附件的ID查询附件信息
     * contractId 合同信息主键  必填
     *
     * */
    @RequestMapping(value = "/synchronization/contractFjInfo/fjInfoGetList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "合同附件接口同步")
    public @ResponseBody
    String fjInfoGetList(@Parameter(name = "fjid", description = "附件ID", required = true) @RequestParam(value = "fjid", required = true) String fjid,
                         @Parameter(name = "token", description = "接口认证令牌", required = true) @RequestParam(value = "token", required = true) String token) throws Exception {
        String json = "";
        JSONObject obj = new JSONObject();
        String date = null;
        Map<String, Object> map = null;
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
            	map=new HashMap<String, Object>(0);
                map.put("code", "500");
                map.put("msg", "用户已失效！");
                json = JSONObject.toJSONString(map);
                return json;
            }
            map = tblCyhwUnitService.fjInfoGetList(fjid,staff.getUsername());
            json = JSONObject.toJSONString(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }


    /*
     * 4.用印生效合同信息接口同步
     * table TBL_FUNDPOOL
     * @param request
     * */
    @RequestMapping(value = "/synchronization/contractInfo/contractInfogetList", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "用印生效合同信息接口同步")
    public @ResponseBody
    String contractInfogetList(@Parameter(name = "usercode", description = "OA用户编码或者OA用户登录名", required = false) @RequestParam(value = "usercode", required = false) String usercode,
                               @Parameter(name = "token", description = "接口认证令牌", required = true) @RequestHeader("token") String token) throws Exception {
        String json = "";
        Map<String, Object> qqlshMap = null;
        JSONObject obj = new JSONObject();
        try {
            TblStaffUtil staff = userProvider.get();
            if (staff == null) {
                qqlshMap.put("code", "500");
                qqlshMap.put("msg", "用户已失效！");
                json = JSONObject.toJSONString(qqlshMap);
                return json;
            }
            qqlshMap = tblCyhwUnitService.oppsiteInfogetList(staff);
            json = JSONObject.toJSONString(qqlshMap);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    /**
     * 更新付款
     *
     * @return
     */
    @RequestMapping(value = "/synchronization/pay/updatePayment", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "更新付款")
    public String updatePayment(@Parameter(name = "token", description = "接口认证令牌", required = true) @RequestHeader("token") String token,
                                @Parameter(name = "contract", description = "合同号", required = true) @RequestParam(value = "contract", required = true) String contract,
                                @Parameter(name = "payAmount", description = "付款金额", required = true) @RequestParam(value = "payAmount", required = true) String payAmount,
                                @Parameter(name = "payDate", description = "付款日期", required = true) @RequestParam(value = "payDate", required = true) String payDate,
                                @Parameter(name = "mark", description = "备注", required = false) @RequestParam(value = "mark", required = false) String mark,
                                @Parameter(name = "bankName", description = "收款方名称", required = true) @RequestParam(value = "bankName", required = true) String bankName,
                                @Parameter(name = "bank", description = "收款方开户行", required = true) @RequestParam(value = "bank", required = true) String bank,
                                @Parameter(name = "accountName", description = "收款方户名", required = true) @RequestParam(value = "accountName", required = true) String accountName,
                                @Parameter(name = "accountNo", description = "收款方账号", required = true) @RequestParam(value = "accountNo", required = true) String accountNo,
                                @Parameter(name = "payBank", description = "付款方开户行", required = true) @RequestParam(value = "payBank", required = true) String payBank,
                                @Parameter(name = "payAccountName", description = "付款方户名", required = true) @RequestParam(value = "payAccountName", required = true) String payAccountName,
                                @Parameter(name = "payAccountNo", description = "付款方账号", required = true) @RequestParam(value = "payAccountNo", required = true) String payAccountNo,
                                @Parameter(name = "abstracts", description = "摘要", required = false) @RequestParam(value = "abstracts", required = false) String abstracts

    ) {

        String json = "";
        Map<String, Object> qqlshMap = null;
        try {

            qqlshMap = tblContractPaymentServic.setUpdatePayment(token, contract, payAmount, payDate, mark, bankName, bank, accountName, accountNo, payBank, payAccountName, payAccountNo, abstracts);
            json = JSONObject.toJSONString(qqlshMap);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;

    }

    /**
     * 客商接口
     *
     * @param inParameters
     * @return
     */
    @RequestMapping(value = "/synchronization/counterpart/customers", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "客商(相对方)数据接口")
    public String customers(@Parameter(name = "inParameters", description = "入参信息") String inParameters) {
        JSONArray inParametersArr = JSONArray.fromObject(inParameters);
        String json = "";
        Map<String, Object> qqlshMap = null;
        try {
            qqlshMap = tblContractPaymentServic.setCustomers(inParametersArr);
            json = JSONObject.toJSONString(qqlshMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }


    @RequestMapping(value = "/synchronization/collectionInformation/registraion", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "收付款接口1-测试")
    public @ResponseBody
    String collectionInformation(@Parameter(name = "type", description = "收付款类型", required = true) String type) {
        return paymentInformation(type);
    }

    @RequestMapping(value = "/synchronization/collectionInformation/registraion2", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "收付款接口2-测试")
    public String test1(String result, String type) {
        org.apache.commons.httpclient.HttpClient client = new org.apache.commons.httpclient.HttpClient();
        org.apache.http.HttpResponse reponse = null;
        try {
            System.out.println(result);
            //  net.sf.json.JSONObject j=net.sf.json.JSONObject.fromObject(result);
            // if(Integer.valueOf(j.get("status").toString())==2){
            net.sf.json.JSONArray arr = net.sf.json.JSONArray.fromObject(result);
            if (type.equals("BXD") || type.equals("JKD")) { //BXD =报销单   JKD=借款单
                tblContractPaymentServic.setData(arr, type);
            } else if (type.equals("SKJSD")) { //SKJSD=收款结算单
                tblContractCollectionService.setData(arr);
            }
            return JsonBean.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonBean.error(e.getMessage());
        }
    }

}
