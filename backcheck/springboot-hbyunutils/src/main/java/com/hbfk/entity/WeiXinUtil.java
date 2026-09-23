package com.hbfk.entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.util.JsonBean;

public class WeiXinUtil {

	public final static String access_token_url="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	public final static String oauth2_1_url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	public final static String oauth2_2_url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	public final static String get_userInfo_url="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";
	public final static String get_hangye_url = "https://api.weixin.qq.com/cgi-bin/template/api_set_industry?access_token=ACCESS_TOKEN";
	//上面那五个，不用改。把下边的appid和appsecret改了就行
	public final static String pcappid="wxfd02d92a524bef99";
	public final static String pcappSecret="REDACTED";
	public final static String appid="wxb81b0c7f1d45d9b5";
	public final static String appSecret="REDACTED";
	public final static String redirectUrl="https://www.wenxin.example.com/api/setting/wxindex/wxLogin";
	public static final String GRANT_TYPE = "authorization_code";
	
	
	public static AccessToken getAccessToken(String appid, String appSecret) {
		//替换真实appid和appsecret
		String requestUrl = access_token_url.replace("APPID", appid).replace("APPSECRET", appSecret);
		AccessToken accesstoken=new AccessToken();
		//得到json对象
		JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
		
		//将得到的json对象的属性值，存到accesstoken中
		accesstoken.setToken(jsonObject.getString("access_token"));
		accesstoken.setExpiresIn(jsonObject.getIntValue("expires_in"));
		
		return accesstoken;
	} 
	
	
/**
 * 网页授权认证	
 * @param appId
 * @param appSecret
 * @param code
 * @return
 */
	public static WeixinOauth2Token getOauth2AccessToken(String appId,String appSecret,String code) {
		
		String  requestUrl=oauth2_1_url.replace("APPID", appId).replace("SECRET", appSecret).replace("CODE", code);
		//发送请求获取网页授权凭证
		WeixinOauth2Token  wxo=new WeixinOauth2Token();
		try {
			String text = sendGetRequest(requestUrl);
			JSONObject jsonObject = JSONObject.parseObject(text);
			wxo.setAccessToken(jsonObject.getString("access_token"));
			wxo.setExpiresIn(jsonObject.getIntValue("expires_in"));
			wxo.setRefreshToken(jsonObject.getString("refresh_token"));
			wxo.setOpenId(jsonObject.getString("openid"));
			wxo.setScope(jsonObject.getString("scope"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	//	JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, EnumMethod.GET.name(), null);

		return wxo;
		
	}
	
	
	public static String decrypt(String encryptedData, String sessionKey, String iv) throws Exception {
		
		//被加密的数据
        byte[] encryptedDataBytes = Base64.getDecoder().decode(encryptedData.replaceAll(" ","+"));
        //加密秘钥
        byte[] sessionKeyBytes = Base64.getDecoder().decode(sessionKey);
        //偏移量
        byte[] ivBytes = Base64.getDecoder().decode(iv);

        // 初始化AES解密器
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        SecretKeySpec keySpec = new SecretKeySpec(sessionKeyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        // 解密
        byte[] result = cipher.doFinal(encryptedDataBytes);

        // 返回解密后的字符串
        return new String(result, StandardCharsets.UTF_8);
	}
	
	/**
	 * 获取用户的基本信息
	 * @param accessToken
	 * @param openId
	 * @return
	 */
	public static SNSUserInfo getSNSUserInfo(String accessToken,String openId) {
		String requestUrl=get_userInfo_url.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
		System.out.println(requestUrl);
		SNSUserInfo snsuserinfo=new SNSUserInfo();
		//通过网页授权获取用户信息
		System.out.println("rel======="+requestUrl);
		try {
			String text = sendGetRequest(requestUrl);
			System.out.println("text======="+text);
			JSONObject jsonObject = JSONObject.parseObject(text);
		
			snsuserinfo.setOpenId(jsonObject.getString("openid"));
			snsuserinfo.setNickname(jsonObject.getString("nickname"));
			snsuserinfo.setSex(jsonObject.getIntValue("sex"));
			snsuserinfo.setCountry(jsonObject.getString("country"));
			snsuserinfo.setProvince(jsonObject.getString("province"));
			snsuserinfo.setCity(jsonObject.getString("city"));
			snsuserinfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
			snsuserinfo.setUnionid(jsonObject.getString("unionid"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//JSONObject jsonObject=CommonUtil.httpsRequest(requestUrl, EnumMethod.GET.name(), null);
		
	
		//snsuserinfo.setPrivilegeList(JSONArray._fromArray(jsonObject.getString("privilege"),String.class));
		return snsuserinfo; 
	}
	
	/**
	 * 创建网页授权的url
	 * @param redirectUri
	 * @return
	 */
	public static String createUrl(String redirectUri) {
		
		String url =get_userInfo_url.replace("APPID", appid).replace("REDIRECT_URI", CommonUtil.urlEncodeUTF8(redirectUri)).replace("SCOPE", "snsapi_userinfo");
		System.out.println(url);
		return url;
	}	
	
	/**
	 * 长连接转化成短链接，提高扫码速度跟成功率
	 * @param args
	 */
	
	public static String  shortURL(String longURL, String wxAppId, String secret) {
        String requestUrl = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token=ACCESS_TOKEN";
        try {
        	//将更新后的access_token,替换上去
			requestUrl = requestUrl.replace("ACCESS_TOKEN",getAccessToken(wxAppId, secret).getToken());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        String jsonMsg = "{\"action\":\"long2short\",\"long_url\":\"%s\"}";
        //格式化url
        String format = String.format(jsonMsg, longURL);
        System.out.println(format);
        
        JSONObject jsonobject = CommonUtil.httpsRequest(requestUrl, "POST",format);
        //转换成短连接成功
        return jsonobject.getString("short_url");
    }

	
	public static void main(String[] args) {
		AccessToken a =getAccessToken(appid, appSecret);
		//String s= shortURL(sss, appid, appSecret);
		//System.out.println(s);
		System.out.println(a.getToken());
		System.out.println(a.getExpiresIn());
	}
	
	
	
	public static String sendGetRequest(String getUrl) throws IOException
	   {
	      StringBuffer sb = new StringBuffer();
	      InputStreamReader isr = null;
	      BufferedReader br = null;
	      try
	      {
	         URL url = new URL(getUrl);
	         URLConnection urlConnection = url.openConnection();
	         urlConnection.setAllowUserInteraction(false);
	         isr = new InputStreamReader(url.openStream(),"utf-8");
	         br = new BufferedReader(isr);
	         String line;
	         while ((line = br.readLine()) != null)
	         {
	            sb.append(line);
	         }
	      }
	      catch (IOException e)
	      {
	         e.printStackTrace();
	      }
	      finally
	      {
	         isr.close();
	         br.close();
	      }
	      return sb.toString();
	   }



}
	
	
	

