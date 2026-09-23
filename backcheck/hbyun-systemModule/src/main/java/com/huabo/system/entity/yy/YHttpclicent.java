package com.huabo.system.entity.yy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.ResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
 
 
public class YHttpclicent {
    public static final String DEF_CHATSET = "UTF-8";
    public static final int DEF_CONN_TIMEOUT = 30000;
    public static final int DEF_READ_TIMEOUT = 30000;
    public static String userAgent =  "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36";
    
    
    static ResourceBundle rb = ResourceBundle.getBundle("setting/yy");
    
    /**
     * 搜索接口
     */
    public static  String APICODE_SEARCH =(String) rb.getObject("APICODE_SEARCH");
    
    /**
     * 企业工商信息查询【加强版】
     */
    public static  String APICODE_GSXX =(String) rb.getObject("APICODE_GSXX");
    /**
     * 企业工商信息查询【上市信息】
     */
    public static  String APICODE_SSXX =(String) rb.getObject("APICODE_SSXX");
    
    /**
     * 背景信息
     */
    public static  String APICODE_CHANGE =(String) rb.getObject("APICODE_CHANGE");
    
    
    /**
     * 经营状况
     */
    public static  String APICODE_MANAGE =(String) rb.getObject("APICODE_MANAGE");
    
    /**
     * 经营风险
     */
    public static  String APICODE_RISK =(String) rb.getObject("APICODE_RISK");
    
    /**
     * 司法风险
     */
    public static  String APICODE_JUDICAL =(String) rb.getObject("APICODE_JUDICAL");
    
    /**
     * 发展信息
     */
    public static  String APICODE_DEVE =(String) rb.getObject("APICODE_DEVE");
    
    /**
     * 舆情信息
     */
    public static  String APICODE_OPIN =(String) rb.getObject("APICODE_OPIN");
    
    /**
     * 关系发现
     */
    public static  String APICODE_RELA =(String) rb.getObject("APICODE_RELA");
    
    /**
     * 知识产权
     */
    public static  String INTELLECTUAL_PROPERTY =(String) rb.getObject("INTELLECTUAL_PROPERTY");
    
    
    
	/**
	 * 搜索接口
	 */
	public static String search =(String) rb.getObject("searchurl");
	
	
	/**
	 * 工商信息
	 */
	public static String gsxx =(String) rb.getObject("gsxxurl");
	
	
	
	/**
	 * 变更信息
	 */
	public static String change =(String) rb.getObject("changeurl");
	/**
	 * 企业年报
	 */
	public static String qynburl =(String) rb.getObject("qynburl");
	
	/**
	 * 分支机构
	 */
	public static String fzjgurl =(String) rb.getObject("fzjgurl");
	/**
	 * 注意人员
	 */
	public static String zyrrurl =(String) rb.getObject("zyrrurl");
	/**
	 * 股东信息	
	 */
	public static String gdxxurl =(String) rb.getObject("gdxxurl");
	/**
	 * 对外投资
	 */
	public static String dwtzurl =(String) rb.getObject("dwtzurl");
	
	
	
	
	/**
	 * 招投标
	 */
	public static String manage =(String) rb.getObject("mangeurl");
	
	/**
	 * 税务评级
	 */
	public static String swpjurl =(String) rb.getObject("swpjurl");
	/**
	 * 抽查检查
	 */
	public static String ccjcurl =(String) rb.getObject("ccjcurl");
	/**
	 * 债券信息
	 */
	public static String zqxxurl =(String) rb.getObject("zqxxurl");
	/**
	 * 招聘
	 */
	public static String zpurl =(String) rb.getObject("zpurl");
	
	
	
	/**
	 * 行政处罚
	 */
	public static String businss =(String) rb.getObject("businriskurl");
	/**
	 * 行政处罚（信用中国）
	 */
	public static String xzcfurl =(String) rb.getObject("xzcfurl");
	/**
	 * 股权出质
	 */
	public static String gqczurl =(String) rb.getObject("gqczurl");
	/**
	 * 严重违法
	 */
	public static String yzwfurl =(String) rb.getObject("yzwfurl");
	/**
	 * 动产抵押
	 */
	public static String dcdyurl =(String) rb.getObject("dcdyurl");
	/**
	 * 司法拍卖
	 */
	public static String sfpmurl =(String) rb.getObject("sfpmurl");
	/**
	 * 欠税公告
	 */
	public static String qsggurl =(String) rb.getObject("qsggurl");
	/**
	 * 经营异常
	 */
	public static String jyycurl =(String) rb.getObject("jyycurl");
	
	
	/**
	 * 法院公告
	 */
	public static String judicial =(String) rb.getObject("judicialurl");
	/**
	 * 失信人
	 */
	public static String sxrurl =(String) rb.getObject("sxrurl");
	/**
	 * 法律诉讼
	 */
	public static String flssurl =(String) rb.getObject("flssurl");
	/**
	 * 被执行人
	 */
	public static String bzxryrl =(String) rb.getObject("bzxryrl");
	/**
	 * 开庭公告
	 */
	public static String ktggurl =(String) rb.getObject("ktggurl");
	
	
	/**
	 * 投资事件
	 */
	public static String deve =(String) rb.getObject("developmenturl");
	/**
	 * 融资历史
	 */
	public static String rzllurl =(String) rb.getObject("rzllurl");
	/**
	 * 核心团队
	 */
	public static String hxtdurl =(String) rb.getObject("hxtdurl");
	/**
	 * 竞品信息
	 */
	public static String jpxxurl =(String) rb.getObject("jpxxurl");
	/**
	 * 企业业务
	 */
	public static String qyywurl =(String) rb.getObject("qyywurl");
	
	
	
	/**
	 * 舆情信息
	 */
	public static String opein =(String) rb.getObject("opeinurl");
	
	/**
	 * 关系图谱
	 */
	public static String rela =(String) rb.getObject("relaurl");
	/**
	 * 股权结构图
	 */
	public static String gqjgturl =(String) rb.getObject("gqjgturl");
	/**
	 * 投资族谱对外接口
	 */
	public static String tzzudwjkurl =(String) rb.getObject("tzzudwjkurl");
	
	
	/**
	 * 行政许可
	 */
	public static String xzxkurl =(String) rb.getObject("xzxkurl");
	/**
	 * 网站备案
	 */
	public static String wzbaurl =(String) rb.getObject("wzbaurl");
	/**
	 * 专利信息
	 */
	public static String zlxxurl =(String) rb.getObject("zlxxurl");
	/**
	 * 著作权
	 */
	public static String zzqurl =(String) rb.getObject("zzqurl");
	/**
	 * 产品信息
	 */
	public static String cpxxurl =(String) rb.getObject("cpxxurl");
	/**
	 * 证书
	 */
	public static String zsurl =(String) rb.getObject("zsurl");
	/**
	 * 企业高管
	 */
	public static String ggurl =(String) rb.getObject("ggurl");
	/**
	 * 企业简介
	 */
	public static String qyjjurl =(String) rb.getObject("qyjjurl");
	
	
	
	
	
	
	
    public static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
     
    /**
     *
     * @param strUrl 请求地址
     * @param params 请求参数
     * @param method 请求方法
     * @return  网络请求字符串
     * @throws Exception
     */
    public static String net(String strUrl, Map<String,Object> params, Map<String,Object> headerParams,String method, String paramFormat) throws Exception {
        HttpURLConnection conn = null;
        BufferedReader reader = null;
        String rs = null;
        try {
            String contentType = null;
            if(headerParams!=null && headerParams.containsKey("Content-Type"))
                contentType = headerParams.get("Content-Type").toString();
                 
            StringBuffer sb = new StringBuffer();
            if(method==null || method.equals("GET")){
                strUrl = strUrl+"?"+urlencode(params);
            }
             
            trustAllHttpsCertificates();
            HttpsURLConnection.setDefaultHostnameVerifier(DO_NOT_VERIFY);
             
            URL url = new URL(strUrl);
            conn = (HttpURLConnection) url.openConnection();
            if(method==null || method.equals("GET")){
                conn.setRequestMethod("GET");
            }else{
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
            }
            conn.setRequestProperty("User-agent", userAgent);
            if(headerParams!=null  ) {
	            for (String i : headerParams.keySet()) {
	                conn.setRequestProperty(i, headerParams.get(i).toString());
	            }
            }
            if("form".equals(paramFormat) && !"application/x-www-form-urlencoded".equals(contentType) && !"application/xml".equals(contentType)) {
                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            }
            conn.setUseCaches(false);
            conn.setConnectTimeout(DEF_CONN_TIMEOUT);
            conn.setReadTimeout(DEF_READ_TIMEOUT);
            conn.setInstanceFollowRedirects(false);
            conn.connect();
            if (params!= null && method.equals("POST")) {
                try {
                    OutputStream out = conn.getOutputStream();
                    if("form".equals(paramFormat)) {
                        if("application/x-www-form-urlencoded".equals(contentType))
                            out.write(urlencode(params).getBytes("utf-8"));
                        else if("application/xml".equals(contentType))
                            out.write(xmlencode(params).getBytes("utf-8"));
                        else
                            out.write(jsonencode(params).getBytes("utf-8"));
                    } else
                        out.write(params.toString().getBytes("utf-8"));
                     
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            InputStream is = conn.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, DEF_CHATSET));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sb.append(strRead);
            }
            rs = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
        return rs;
    }
     
    //将map型转为请求参数型
    @SuppressWarnings("rawtypes")
	public static String urlencode(Map<String,Object>data) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry i : data.entrySet()) {
            try {
                if(("").equals(i.getKey())) {
                    sb.append(URLEncoder.encode(i.getValue()+"","UTF-8"));
                } else {
                    sb.append(i.getKey()).append("=").append(URLEncoder.encode(i.getValue()+"","UTF-8")).append("&");
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
     
    //将map型转为请求参数型
    @SuppressWarnings("rawtypes")
	public static String jsonencode(Map<String,Object>data) {
        JSONObject jparam = new JSONObject();
        for (Map.Entry i : data.entrySet())
            jparam.put(i.getKey(), i.getValue());
         
        return jparam.toString();
    }
     
    //将map型转为请求参数型
    @SuppressWarnings("rawtypes")
	public static String xmlencode(Map<String,Object>data) {
        StringBuffer xmlData = new StringBuffer(); 
        xmlData.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        for (Map.Entry i : data.entrySet())
             xmlData.append("<" + i.getKey() + ">" + i.getValue() + "</" + i.getKey() + ">");
         
        return xmlData.toString();
    }
     
        static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
 
        public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }
 
        public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
            return true;
        }
 
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
 
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                throws java.security.cert.CertificateException {
            return;
        }
    }
 
    private static void trustAllHttpsCertificates() throws Exception {
        javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
        javax.net.ssl.TrustManager tm = new miTM();
        trustAllCerts[0] = tm;
        javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, null);
        javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
     
    public static String requestpost(String url,HashMap<String, Object> map) throws Exception {
		 // 声明httpPost请求
   	
      CloseableHttpClient httpClient = HttpClients.createDefault();
      String result = null;
      CloseableHttpResponse response=null;
      // 判断map不为空
      try {  if (map != null) {
          // 声明存放参数的List集合
          List<NameValuePair> params = new ArrayList<NameValuePair>();
          // 遍历map，设置参数到list中
          for (Map.Entry<String, Object> entry : map.entrySet()) {
              params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
          }
          // 创建form表单对象
        	//UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, DEF_CHATSET);
           String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
           paramsStr=paramsStr.replace("%25", "%");
           HttpPost httpget = new HttpPost(url+"?"+paramsStr);
           System.out.println(url+"?"+paramsStr);
           //httpget.setHeader("Authorization", token);
        	 // 执行get请求.
          response = httpClient.execute(httpget);
         
      	}
      	// 获取响应实体
	    	HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = streamToString(resEntity.getContent(), DEF_CHATSET);
			}
			EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
      // 返回结果
      return result;
	}
    
    
    
    public static String request(String url,HashMap<String, Object> map) throws Exception {
		 // 声明httpPost请求
    	
       CloseableHttpClient httpClient = HttpClients.createDefault();
       String result = null;
       CloseableHttpResponse response=null;
       // 判断map不为空
       try {  if (map != null) {
           // 声明存放参数的List集合
           List<NameValuePair> params = new ArrayList<NameValuePair>();
           // 遍历map，设置参数到list中
           for (Map.Entry<String, Object> entry : map.entrySet()) {
               params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
           }
           // 创建form表单对象
         	//UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, DEF_CHATSET);
            String paramsStr = EntityUtils.toString(new UrlEncodedFormEntity(params, "UTF-8"));
            paramsStr=paramsStr.replace("%25", "%");
            HttpGet httpget = new HttpGet(url+"?"+paramsStr);
            System.out.println(url+"?"+paramsStr);
            //httpget.setHeader("Authorization", token);
         	 // 执行get请求.
           response = httpClient.execute(httpget);
          
       	}
       	// 获取响应实体
	    	HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = streamToString(resEntity.getContent(), DEF_CHATSET);
			}
			EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
       // 返回结果
       return result;
	}
	// 此方法是把传进的字节流转化为相应的字符串并返回，此方法一般在网络请求中用到
		private static String streamToString(InputStream inputStream, String charset) throws Exception {
			StringBuilder stringBuilder = new StringBuilder();
			try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset)) {
				try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
					String line = null;
					while ((line = reader.readLine()) != null) {
						stringBuilder.append(line).append("\r\n");
					}
				}
			}
			return stringBuilder.toString();
		}
	
		
		public static String result(String compayName,Map<String, Object> params){
		     
		     //1.API方法
		     String result =null;
		     String method = "GET";
		     String paramFormat = "form";
		     try {
		         result = net(search, params, null, method, paramFormat);
		     } catch (Exception e) {
		         e.printStackTrace();
		     }
			return result;
		    }
		

		
    
    /**
     * 用友-搜索接口
     * @param
     * @param compayName
     * @return
     */
    public static String findSerch(String compayName){
     
     //1.API方法
     String result =null;
     String method = "GET";
     String paramFormat = "form";
     Map<String, Object> params = new HashMap<String, Object>();//请求参数
     params.put("word", compayName);
     Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
     headerParams.put("authoration", "apicode");
     headerParams.put("apicode", APICODE_SEARCH);//APICODE
     try {
         result = net(search, params, headerParams, method, paramFormat);
     } catch (Exception e) {
         e.printStackTrace();
     }
	return result;
    }
    
    /**
     * 用友-工商信息
     * @param id
     * @param compayName
     * @return
     */
    public static String findGSXX(String id,String compayName){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_GSXX);//APICODE
        try {
            result = net(gsxx, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    
    /**
     * 用友-变更信息
     * @param compayName
     * @return
     */
    public static String findchange(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_CHANGE);//APICODE
        try {
            result = net(change, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    /**
     * 企业年报
     * @param id
     * @param compayName
     * @return
     */
    public static String findQynb(String id,String compayName){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_CHANGE);//APICODE
        try {
            result = net(qynburl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    
  /**
   * 分支机构
   * @param id
   * @param compayName
   * @return
   */
 public static String findfzjg(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_CHANGE);//APICODE
        try {
            result = net(fzjgurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
 
 
 /**
  * 主要人员
  * @param id
  * @param compayName
  * @param pageNum
  * @return
  */
 public static String findzyrr(String id,String compayName,Integer pageNum){
     
     //1.API方法
     String result =null;
     String method = "GET";
     String paramFormat = "form";
     Map<String, Object> params = new HashMap<String, Object>();//请求参数
     if(id!=null && id.length()>0){
     	params.put("id", id);
     }
     if(compayName!=null && compayName.length()>0){
     	params.put("name", compayName);
     }
     params.put("pageNum", pageNum);
     Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
     headerParams.put("authoration", "apicode");
     headerParams.put("apicode", APICODE_CHANGE);//APICODE
     try {
         result = net(zyrrurl, params, headerParams, method, paramFormat);
     } catch (Exception e) {
         e.printStackTrace();
     }
	return result;
 }
 /**
  * 股东信息
  * @param id
  * @param compayName
  * @param pageNum
  * @return
  */
public static String findgdxx(String id,String compayName,Integer pageNum){
     
     //1.API方法
     String result =null;
     String method = "GET";
     String paramFormat = "form";
     Map<String, Object> params = new HashMap<String, Object>();//请求参数
     if(id!=null && id.length()>0){
     	params.put("id", id);
     }
     if(compayName!=null && compayName.length()>0){
     	params.put("name", compayName);
     }
     params.put("pageNum", pageNum);
     Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
     headerParams.put("authoration", "apicode");
     headerParams.put("apicode", APICODE_CHANGE);//APICODE
     try {
         result = net(gdxxurl, params, headerParams, method, paramFormat);
     } catch (Exception e) {
         e.printStackTrace();
     }
	return result;
 }
/**
 * 对外投资
 * @param id
 * @param compayName
 * @param pageNum
 * @return
 */
public static String findDwtz(String id,String compayName,Integer pageNum){
    
    //1.API方法
    String result =null;
    String method = "GET";
    String paramFormat = "form";
    Map<String, Object> params = new HashMap<String, Object>();//请求参数
    if(id!=null && id.length()>0){
    	params.put("id", id);
    }
    if(compayName!=null && compayName.length()>0){
    	params.put("name", compayName);
    }
    params.put("pageNum", pageNum);
    Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
    headerParams.put("authoration", "apicode");
    headerParams.put("apicode", APICODE_CHANGE);//APICODE
    try {
        result = net(dwtzurl, params, headerParams, method, paramFormat);
    } catch (Exception e) {
        e.printStackTrace();
    }
	return result;
}
 

 
    
    
    
    
    /**
     * 用友-招投标
     * @param compayName
     * @return
     */
    public static String findmanage(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_MANAGE);//APICODE
        try {
            result = net(manage, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
 /**
  * 税务评级   
  * @param id
  * @param compayName
  * @param pageNum
  * @return
  */
    public static String findswpj(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_MANAGE);//APICODE
        try {
            result = net(swpjurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    /**
     * 抽查检查
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findccjc(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_MANAGE);//APICODE
        try {
            result = net(ccjcurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    /**
     * 债券信息
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findzqxx(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_MANAGE);//APICODE
        try {
            result = net(zqxxurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    /**
     * 招聘
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findzp(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_MANAGE);//APICODE
        try {
            result = net(zpurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
    }
    
    
    /**
     * 用友-行政处罚
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findRisk(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(businss, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 行政处罚（信用中国）
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findxzcf(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(xzcfurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 股权出质
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findgqcz(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(gqczurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 严重违法
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findyzwf(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(yzwfurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 动产抵押
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String finddcdy(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(dcdyurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 司法拍卖
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findsfpm(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_RISK);//APICODE
        try {
            result = net(sfpmurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 欠税公告
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findqsgg(String id,String compayName,Integer pageNum){
       
       //1.API方法
       String result =null;
       String method = "GET";
       String paramFormat = "form";
       Map<String, Object> params = new HashMap<String, Object>();//请求参数
       if(id!=null && id.length()>0){
       	params.put("keyword", id);
       }
       if(compayName!=null && compayName.length()>0){
       	params.put("keyword", compayName);
       }
       params.put("pageNum", pageNum);
       Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
       headerParams.put("authoration", "apicode");
       headerParams.put("apicode", APICODE_RISK);//APICODE
       try {
           result = net(qsggurl, params, headerParams, method, paramFormat);
       } catch (Exception e) {
           e.printStackTrace();
       }
  	return result;
  }
    /**
     * 经营异常
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findjyyc(String id,String compayName,Integer pageNum){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      if(id!=null && id.length()>0){
      	params.put("keyword", id);
      }
      if(compayName!=null && compayName.length()>0){
      	params.put("keyword", compayName);
      }
      params.put("pageNum", pageNum);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_RISK);//APICODE
      try {
          result = net(jyycurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
    
    
    /**
     * 用友-法院公告
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findJudicialrisk(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_JUDICAL);//APICODE
        try {
            result = net(judicial, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 失信人
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findsxr(String id,String compayName,Integer pageNum){
       
       //1.API方法
       String result =null;
       String method = "GET";
       String paramFormat = "form";
       Map<String, Object> params = new HashMap<String, Object>();//请求参数
       if(id!=null && id.length()>0){
       	params.put("keyword", id);
       }
       if(compayName!=null && compayName.length()>0){
       	params.put("keyword", compayName);
       }
       params.put("pageNum", pageNum);
       Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
       headerParams.put("authoration", "apicode");
       headerParams.put("apicode", APICODE_JUDICAL);//APICODE
       try {
           result = net(sxrurl, params, headerParams, method, paramFormat);
       } catch (Exception e) {
           e.printStackTrace();
       }
  	return result;
  }
    /**
     * 法律诉讼
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findfvss(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_JUDICAL);//APICODE
        try {
            result = net(flssurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 被执行人
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findbzxr(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_JUDICAL);//APICODE
        try {
            result = net(bzxryrl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    /**
     * 开庭公告
     * @param id
     * @param compayName
     * @param pageNum
     * @return
     */
    public static String findktgg(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("keyword", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("keyword", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_JUDICAL);//APICODE
        try {
            result = net(ktggurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
    
  /**
   * 投资事件
   * @param id
   * @param compayName
   * @param pageNum
   * @return
   */
  public static String finddeve(String id,String compayName,Integer pageNum){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        if(id!=null && id.length()>0){
        	params.put("id", id);
        }
        if(compayName!=null && compayName.length()>0){
        	params.put("name", compayName);
        }
        params.put("pageNum", pageNum);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", APICODE_DEVE);//APICODE
        try {
            result = net(deve, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
   	return result;
   }
  
  /**
   * 融资历史
   * @param id
   * @param compayName
   * @param pageNum
   * @return
   */
  public static String finddrzls(String id,String compayName,Integer pageNum){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      if(id!=null && id.length()>0){
      	params.put("id", id);
      }
      if(compayName!=null && compayName.length()>0){
      	params.put("name", compayName);
      }
      params.put("pageNum", pageNum);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_DEVE);//APICODE
      try {
          result = net(rzllurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
  
  /**
   * 核心团队
   * @param id
   * @param compayName
   * @param pageNum
   * @return
   */
  public static String findhxtd(String id,String compayName,Integer pageNum){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      if(id!=null && id.length()>0){
      	params.put("id", id);
      }
      if(compayName!=null && compayName.length()>0){
      	params.put("name", compayName);
      }
      params.put("pageNum", pageNum);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_DEVE);//APICODE
      try {
          result = net(hxtdurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
  /**
   * 竞品信息
   * @param id
   * @param compayName
   * @param pageNum
   * @return
   */
 public static String findjpxx(String id,String compayName,Integer pageNum){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      if(id!=null && id.length()>0){
      	params.put("id", id);
      }
      if(compayName!=null && compayName.length()>0){
      	params.put("name", compayName);
      }
      params.put("pageNum", pageNum);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_DEVE);//APICODE
      try {
          result = net(jpxxurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
 
 /**
  * 企业业务
  * @param id
  * @param compayName
  * @param pageNum
  * @return
  */
 	public static String findqyxx(String id,String compayName,Integer pageNum){
     
     //1.API方法
     String result =null;
     String method = "GET";
     String paramFormat = "form";
     Map<String, Object> params = new HashMap<String, Object>();//请求参数
     if(id!=null && id.length()>0){
     	params.put("id", id);
     }
     if(compayName!=null && compayName.length()>0){
     	params.put("name", compayName);
     }
     params.put("pageNum", pageNum);
     Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
     headerParams.put("authoration", "apicode");
     headerParams.put("apicode", APICODE_DEVE);//APICODE
     try {
         result = net(qyywurl, params, headerParams, method, paramFormat);
     } catch (Exception e) {
         e.printStackTrace();
     }
	return result;
}
 
    
  /**
   * 舆情信息
   * @param compayName
   * @return
   */
  public static String findopein(String compayName){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      params.put("name", compayName);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_OPIN);//APICODE
      try {
          result = net(opein, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
  
  
  
  
  
/**
 * 行政许可
 * @param compayName
 * @return
 */
  public static String findxzxk(String compayName){
    
    //1.API方法
    String result =null;
    String method = "GET";
    String paramFormat = "form";
    Map<String, Object> params = new HashMap<String, Object>();//请求参数
    params.put("keyword", compayName);
    Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
    headerParams.put("authoration", "apicode");
    headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
    try {
        result = net(xzxkurl, params, headerParams, method, paramFormat);
    } catch (Exception e) {
        e.printStackTrace();
    }
	return result;
}
  
  /**
   * 网站备案
   * @param compayName
   * @return
   */
    public static String findwzba(String compayName){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      params.put("keyword", compayName);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
      try {
          result = net(wzbaurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
  	return result;
  }
  
    
    /**
     * 专利信息
     * @param compayName
     * @return
     */
      public static String findzlxx(String compayName){
        
        //1.API方法
        String result =null;
        String method = "GET";
        String paramFormat = "form";
        Map<String, Object> params = new HashMap<String, Object>();//请求参数
        params.put("keyword", compayName);
        Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
        headerParams.put("authoration", "apicode");
        headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
        try {
            result = net(zlxxurl, params, headerParams, method, paramFormat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    	return result;
    }
  
      
      /**
       * 著作权
       * @param compayName
       * @return
       */
        public static String findzzq(String compayName){
          
          //1.API方法
          String result =null;
          String method = "GET";
          String paramFormat = "form";
          Map<String, Object> params = new HashMap<String, Object>();//请求参数
          params.put("keyword", compayName);
          Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
          headerParams.put("authoration", "apicode");
          headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
          try {
              result = net(zzqurl, params, headerParams, method, paramFormat);
          } catch (Exception e) {
              e.printStackTrace();
          }
      	return result;
      }
        
        
        /**
         * 产品信息
         * @param compayName
         * @return
         */
          public static String findCpxx(String compayName){
            
            //1.API方法
            String result =null;
            String method = "GET";
            String paramFormat = "form";
            Map<String, Object> params = new HashMap<String, Object>();//请求参数
            params.put("keyword", compayName);
            Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
            headerParams.put("authoration", "apicode");
            headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
            try {
                result = net(cpxxurl, params, headerParams, method, paramFormat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        	return result;
        }
          
          /**
           * 证书
           * @param compayName
           * @return
           */
            public static String findCert(String compayName){
              //1.API方法
              String result =null;
              String method = "GET";
              String paramFormat = "form";
              Map<String, Object> params = new HashMap<String, Object>();//请求参数
              params.put("name", compayName);
              Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
              headerParams.put("authoration", "apicode");
              headerParams.put("apicode", INTELLECTUAL_PROPERTY);//APICODE
              try {
                  result = net(zsurl, params, headerParams, method, paramFormat);
              } catch (Exception e) {
                  e.printStackTrace();
              }
          	return result;
          } 
            /**
             * 企业高管
             * @param compayName
             * @return
             */
            public static String findExecutives(String compayName){
            	//1.API方法
            	String result =null;
            	String method = "GET";
            	String paramFormat = "form";
            	Map<String, Object> params = new HashMap<String, Object>();//请求参数
            	params.put("name", compayName);
            	params.put("pageNum", 1);
            	params.put("pageSize", 20);
            	Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
            	headerParams.put("authoration", "apicode");
            	headerParams.put("apicode", APICODE_SSXX);//APICODE
            	try {
            		result = net(ggurl, params, headerParams, method, paramFormat);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            	return result;
            } 
            /**
             * 企业简介
             * @param compayName
             * @return
             */
            public static String findcompanyintroduct(String compayName){
            	//1.API方法
            	String result =null;
            	String method = "GET";
            	String paramFormat = "form";
            	Map<String, Object> params = new HashMap<String, Object>();//请求参数
            	params.put("name", compayName);
            	params.put("pageNum", 1);
            	params.put("pageSize", 20);
            	Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
            	headerParams.put("authoration", "apicode");
            	headerParams.put("apicode", APICODE_SSXX);//APICODE
            	try {
            		result = net(qyjjurl, params, headerParams, method, paramFormat);
            	} catch (Exception e) {
            		e.printStackTrace();
            	}
            	return result;
            }
  
  /**
   * 关系图谱
   * @param id
   * @return
   */
  public static String findRela(String id){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      params.put("id", id);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_RELA);//APICODE
      try {
          result = net(rela, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
  
  /**
   * 股权结构图
   * @param id
   * @return
   */
  public static String findGqjgt(String id){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      params.put("id", id);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_RELA);//APICODE
      try {
          result = net(gqjgturl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }
  
  
  /**
   * 投资族谱对外接口
   * @param id
   * @return
   */
  public static String findTztudwjk(String id){
      
      //1.API方法
      String result =null;
      String method = "GET";
      String paramFormat = "form";
      Map<String, Object> params = new HashMap<String, Object>();//请求参数
      params.put("id", id);
      Map<String, Object> headerParams = new HashMap<String, Object>();//请求头参数
      headerParams.put("authoration", "apicode");
      headerParams.put("apicode", APICODE_RELA);//APICODE
      try {
          result = net(tzzudwjkurl, params, headerParams, method, paramFormat);
      } catch (Exception e) {
          e.printStackTrace();
      }
 	return result;
 }

  
    
    public static void main(String[] args) {
    	 String str="[{\"title\":\"用友网络科技股份有限公司股东减持股份结果公告\",\"url\":\"http://finance.sina.com.cn/roll/2019-02-15/doc-ihrfqzka5899264.shtml\",\"website\":\"新浪\",\"time\":\"2019-02-15 01:49:00\"}]";
    	 try {
    		 JSONObject resultobj = JSONObject.fromObject(str);
        	 String items=resultobj.getString("items");
        	 List<Publicopinion> list = JSON.parseObject(items, new TypeReference<List<Publicopinion>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}