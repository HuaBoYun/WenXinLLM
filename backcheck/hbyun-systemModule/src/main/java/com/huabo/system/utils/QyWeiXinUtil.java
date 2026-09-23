package com.huabo.system.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 企业微信工具类
 */
public class QyWeiXinUtil {
    /**
     * 资源配置文件绑定
     */
    public static ResourceBundle rb = ResourceBundle.getBundle("setting/qyweixin");

    /**
     * 企业微信组织标识
     */
    public static String qyweixin_corp_id = rb.getString("qyweixin_corp_id");

    /**
     * 企业微信组织密钥
     */
    public static String qyweixin_corp_secret = rb.getString("qyweixin_corp_secret");

    /**
     * 企业微信应用标识
     */
    public static String qyweixin_agent_id = rb.getString("qyweixin_agent_id");

    /**
     * 企业微信接口前缀
     */
    public static String qyweixin_url_prefix = rb.getString("qyweixin_url_prefix");

    /**
     * 企业微信访问令牌
     */
    public static String qyweixin_get_token = rb.getString("qyweixin_get_token");

    /**
     * 企业微信消息发送
     */
    public static String qyweixin_message_send = rb.getString("qyweixin_message_send");

    /**
     * 回调前缀
     */
    public static String call_back_url_prefix = rb.getString("call_back_url_prefix");

    /**
     * 我的代办
     */
    public static String call_back_url_wddb = rb.getString("call_back_url_wddb");
    
    /**
     * 整改落实
     */
    public static String call_back_url_reminder = rb.getString("call_back_url_reminder");
    
    /**
     * 企业微信访问令牌缓存
     */
    public static String access_token;

    /**
     * 内容模板
     */
    public static String msg_temp = rb.getString("msg_temp");

    /**
     * 设置微服务地址
     */
    public static String setup_service_url = rb.getString("setup_service_url");

    /**
     * 我的代办请求路径
     */
    public static String wddb_url = rb.getString("wddb_url");

    /**
     * 获取令牌
     * @return
     */
    public static String getAccessToken() {
        try {
            String getUrl = qyweixin_url_prefix.concat(qyweixin_get_token);
            String getUrlFinal = getUrl.replaceAll("CORPID", qyweixin_corp_id).replaceAll("CORPSECRET", qyweixin_corp_secret);
            String respStr = HttpUtil.get(getUrlFinal);
            JSONObject respJson = JSONObject.parseObject(respStr);
            if (respJson.getIntValue("errcode") == 0) {
                access_token = respJson.getString("access_token");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return access_token;
    }

    /**
     * 消息发送
     * @param username
     * @param token
     * @return
     */
    public static String sendTxtHrefMessage(String username, String token) {
        try {
            String reqUrl = qyweixin_url_prefix.concat(qyweixin_message_send);
            if (access_token == null) {
                access_token = getAccessToken();
            }
            String reqUrlFinal = reqUrl.replaceAll("ACCESS_TOKEN", access_token);
            Map<String, Object> reqMap = new HashMap<>();
            reqMap.put("touser", username);
            reqMap.put("msgtype", "text");
            reqMap.put("agentid", Integer.valueOf(qyweixin_agent_id));
            String urlWithToken = call_back_url_prefix.concat(call_back_url_wddb).replaceAll("TOKEN", token);
            String tip = new String(msg_temp.getBytes(StandardCharsets.ISO_8859_1));
            String content = new String(tip.getBytes(StandardCharsets.UTF_8));
            HttpRequest request = HttpUtil.createPost(setup_service_url.concat(wddb_url));
            request.header("token", token);
            HttpResponse response = request.execute();
            String todoCnt = response.body();
            boolean isJson = JSONUtil.isJson(todoCnt);
            if(isJson){
                JSONObject todoCntJson = JSONObject.parseObject(todoCnt);
                if("1".equals(todoCntJson.getString("code"))){
                    JSONObject resultJson = JSONObject.parseObject(todoCntJson.getString("data"));
                    int recordCount = resultJson.getIntValue("totalRecord");
                    content = content.replaceAll("N", String.valueOf(recordCount));
                }
            }
            String aHref = "<a href=\"WDDB\">".concat(content).concat("</a>");
            String wddbUrl = aHref.replaceAll("WDDB", urlWithToken);
            JSONObject text = new JSONObject();
            text.put("content", wddbUrl);
            reqMap.put("text", text);
            System.err.println(JSONUtil.parse(reqMap).toStringPretty());
            String respStr = HttpUtil.post(reqUrlFinal, new JSONObject(reqMap).toJSONString());
            JSONObject respJson = JSONObject.parseObject(respStr);
            if(respJson.getIntValue("errcode") == 42001){
                access_token = getAccessToken();
                sendTxtHrefMessage(username, token);
            }
            if (respJson.getIntValue("errcode") == 0) {
                System.err.println("qyweixin send message success...");
                return "0";
            }else {
                System.err.println(QyWeiXinUtil.class.getSimpleName().concat(respJson.getString("errmsg")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
        }
        return null;
    }


}
