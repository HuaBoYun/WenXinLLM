package com.huabo.fxgl.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.huabo.fxgl.entity.FxglForm;
import com.huabo.fxgl.entity.Mainjobinfo;
import com.huabo.fxgl.entity.MyTask;
import com.huabo.fxgl.entity.ProcessDefinitions;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.CharArrayBuffer;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.*;
import java.util.Map.Entry;

public class HttpClient {

	private static final Log logger = LogFactory.getLog(HttpClient.class);
	
	private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
	private final static String DEFAULT_ENCODING = "UTF-8";
	 
	private static String charset = "UTF-8";
	public static ResourceBundle rb = ResourceBundle.getBundle("setting/process");
	
	public static String fburl =(String) rb.getObject("fburl");
	
	public static String qdurl =(String) rb.getObject("qdurl");
	
	
	public static String rwurl =(String) rb.getObject("rwurl");
	
	public static String blurl =(String) rb.getObject("blurl");
	
	public static String jkurl =(String) rb.getObject("jkurl");

	
	public static String lczxurl =(String) rb.getObject("lczxurl");
	
	
	public static ResourceBundle sxjm = ResourceBundle.getBundle("setting/sxjm");
	
	public static String sxjm_upfile = (String) sxjm.getObject("sxjm_upfile");
	
	public static String sxjm_deletefile = (String) sxjm.getObject("sxjm_deletefile");
	
	public static String sxjm_selecetfrist = (String) sxjm.getObject("sxjm_selecetfrist");

	public static String sxjm_selecetfrist1 = (String) sxjm.getObject("sxjm_selecetfrist1");
	
	public static String sxjm_selecettwoRiskList = (String) sxjm.getObject("sxjm_selecettwoRiskList");
	
	public static String sxjm_selecettwoscRiskList = (String) sxjm.getObject("sxjm_selecettwoscRiskList");
	
	public static String sxjm_shrisklist = (String) sxjm.getObject("sxjm_shrisklist");
	
	public static String sxjm_allrisklist = (String) sxjm.getObject("sxjm_allrisklist");
	
	public static String sxjm_showallrisklist = (String) sxjm.getObject("sxjm_showallrisklist");
	
	public static String sxjm_entityurl = (String) sxjm.getObject("sxjm_entityurl");
	
	public static String sxjm_fromlisturl = (String) sxjm.getObject("sxjm_fromlisturl");
	
	public static String sxjm_fromlisturl1 = (String) sxjm.getObject("sxjm_fromlisturl1");
	
	public static String sxjm_updateRiskurl = (String) sxjm.getObject("sxjm_updateRiskurl");
	
	public static String sxjm_addriskurl = (String) sxjm.getObject("sxjm_addriskurl");
	
	public static String sxjm_deleteurl = (String) sxjm.getObject("sxjm_deleteurl");
	
	public static String sxjm_exporturl = (String) sxjm.getObject("sxjm_exporturl");
	
	public static String sxjm_importurl = (String) sxjm.getObject("sxjm_importurl");
	
	public static String sxjm_processDetail = (String) sxjm.getObject("sxjm_processDetail");
	
	public static String sxjm_is_status = (String) sxjm.getObject("sxjm_is_status");
	
	public static String sxjm_select_allrisk = (String) sxjm.getObject("sxjm_select_allrisk");
	
	public static String sxjm_checksame_risknumber = (String) sxjm.getObject("sxjm_checksame_risknumber");
	
	public static String sxjm_riskcountListurl = (String) sxjm.getObject("sxjm_riskcountListurl");
	//查询
	public static String sxjm_selectFxglBySelect = (String) sxjm.getObject("sxjm_selectFxglBySelect");
	
	public static String sxjm_selecetfristfrom = (String) sxjm.getObject("sxjm_selecetfristfrom");
	
	public static String sxjm_echartTheme = (String) sxjm.getObject("sxjm_echartTheme");
	
	public static String sxjm_selectExportRiskList = (String) sxjm.getObject("sxjm_selectExportRiskList");
	
	
	//风险分类-查询所有分页
	public static String sxjm_selectrisk = (String) sxjm.getObject("sxjm_selectrisk");
	
	//风险分类-根据父级查询子级分页
	public static String sxjm_bypanrent = (String) sxjm.getObject("sxjm_bypanrent");
	
	//风险分类-保存或修改
	public static String sxjm_saverisk = (String) sxjm.getObject("sxjm_saverisk");
	
	//风险分类-删除单个风险
	public static String sxjm_deleterisk = (String) sxjm.getObject("sxjm_deleterisk");
	//风险分类-根据id查询单个风险
	public static String sxjm_findone = (String) sxjm.getObject("sxjm_findone");
	//风险分类-导出为excel
	public static String sxjm_exploreexcel = (String) sxjm.getObject("sxjm_exploreexcel");
	
	public static String sxjm_selectbycount = (String) sxjm.getObject("sxjm_selectbycount");
	
	//redis微服务相关路径
	private static final String redisurl=ResourceBundle.getBundle("setting/jdbc").getString("redisurl").toString();
	/**
	 * 获取子公司树
	 */
	public static String getOrgChildrenUrl =redisurl+"/redis/getOrgChildren";
	/**
	 * 获取父子公司树
	 */
	public static String getOrgUrl =redisurl+"/redis/getOrg";	
	/**
	 * 获取父子部门
	 */
	public static String getDeptUrl =redisurl+"/redis/getDept";
	/**
	 * 获取部门树
	 */
	public static String getDeptChildrenUrl =redisurl+"/redis/getDeptChildren";
	/**
	 * 获取部门树
	 */
	public static String getDeptString =redisurl+"/redis/getDeptString";
	/**
	 * 更新单个公司redis
	 */
	public static String insertOrgOneUrl =redisurl+"/redis/insertOrgOne";
	/**
	 * 更新所有组织redis
	 */
	public static String insertOrgAllUrl =redisurl+"/redis/insertOrgAll";
	
	// 此方法是POST请求上传的参数中包含本地图片信息File类型
		public static String requestget(String url, HashMap<String, Object> fields, HashMap<String, File> files) throws Exception {

		
			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = null;
			String result = null;
			// HttpClient请求的相关设置，可以不用配置，用默认的参数，这里设置连接和超时时长(毫秒)
			//RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
			try {
				MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
				if(fields!=null){
					addFields(entityBuilder, fields);
				}
				if(files!=null){
					addFiles(entityBuilder, files);
				}
		
				HttpGet httpGet=new HttpGet(url);
				HttpEntity reqEntity = entityBuilder.build();
				((HttpResponse) httpGet).setEntity(reqEntity);
				/*HttpPost httpPost = new HttpPost(url);
				HttpEntity reqEntity = entityBuilder.build();
				httpPost.setEntity(reqEntity);*/
				//httpPost.setConfig(config);
				// 执行网络请求并返回结果
				response = httpClient.execute(httpGet);
				HttpEntity resEntity = response.getEntity();
			if (resEntity != null) {
				result = streamToString(resEntity.getContent(), charset);
			}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
			// 得到的是JSON类型的数据需要第三方解析JSON的jar包来解析
			return result;
		}

	
	
	// 此方法是POST请求上传的参数中包含本地图片信息File类型
	public static String request(String url, HashMap<String, Object> fields, HashMap<String, File> files) throws Exception {

	
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String result = null;
		// HttpClient请求的相关设置，可以不用配置，用默认的参数，这里设置连接和超时时长(毫秒)
		//RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
		try {
			MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
			if(fields!=null){
				addFields(entityBuilder, fields);
			}
			if(files!=null){
				addFiles(entityBuilder, files);
			}
	
	
	
			HttpPost httpPost = new HttpPost(url);
			HttpEntity reqEntity = entityBuilder.build();
			httpPost.setEntity(reqEntity);
			//httpPost.setConfig(config);
			// 执行网络请求并返回结果
			response = httpClient.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
		if (resEntity != null) {
			result = streamToString(resEntity.getContent(), charset);
		}
			EntityUtils.consume(resEntity);
		} finally {
			response.close();
			httpClient.close();
		}
		// 得到的是JSON类型的数据需要第三方解析JSON的jar包来解析
		return result;
	}


	private static void addFiles(MultipartEntityBuilder entityBuilder, HashMap<String, File> files) {
	
		if (files == null) {
			return;
		}
		for (String name : files.keySet()) {
			File file = files.get(name);
			FileBody fileBody = new FileBody(file);
			entityBuilder.addPart(name, fileBody);
		}
	}


	private static void addFields(MultipartEntityBuilder entityBuilder, HashMap<String, Object> fields) {
		for (String name : fields.keySet()) {
			if(fields.get(name)!=null){
				String value = fields.get(name).toString();
				ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE, HTTP.UTF_8);
				StringBody StringBody = new StringBody(value,contentType);
				entityBuilder.addPart(name, StringBody);
			}
		}
	}

	

	public static FxglForm findFrom(String formid) {
		String result;
		FxglForm oneglobal=null;
		try {
			
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1.put("formId", formid);
			result = request(sxjm_entityurl, map1, null);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String resultone = jsonObject.getString("result");
			String global = jsonObject.getString("entity");
			if(resultone!=null && resultone.equals("true")){
				JSONObject oneobject = JSONObject.fromObject(global);
				oneglobal=(FxglForm) JSONObject.toBean(oneobject, FxglForm.class); // 通过JSONObject.toBean()方法进行对象间的转换
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return oneglobal;
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

	
	/**
	 * 
	 * @param filepath 文件路径 
	 * @param filename 文件名称
	 * @param httpurl http的访问地址
	 * @return
	 */
	public static String returnResult(String filepath,String filename,String httpurl){
		String result="";
		HashMap<String, Object> fields = new HashMap<String, Object>();
		//上传参数
		fields.put("module",filename);
		//上传文件
		File file = new File(filepath);
		HashMap<String, File> files = new HashMap<String, File>();
		files.put("file", file);
		try {
			result= request(httpurl, fields, files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	public static String returnResult(HashMap<String, Object> fields,String httpurl){
		String result="";
		try {
			result= requestget(httpurl, fields, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	public static boolean httpPostWithJson(JSONObject jsonObj,String url,String appid){
	    boolean isSuccess = false;
	    
	    HttpPost post = null;
	    try {
	        DefaultHttpClient httpClient = new DefaultHttpClient();

	        // 设置超时时间
	        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 2000);
	        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 2000);
	            
	        post = new HttpPost(url);
	        // 构造消息头
	        post.setHeader("Content-type", "application/json; charset=utf-8");
	        post.setHeader("Connection", "Close");
	        String sessionId = getSessionId();
	        post.setHeader("SessionId", sessionId);
	        post.setHeader("appid", appid);
	                    
	        // 构建消息实体
	        StringEntity entity = new StringEntity(jsonObj.toString(), Charset.forName("UTF-8"));
	        entity.setContentEncoding("UTF-8");
	        // 发送Json格式的数据请求
	        entity.setContentType("application/json");
	        post.setEntity(entity);
	            
	        HttpResponse response = httpClient.execute(post);
	            
	        // 检验返回码
	        int statusCode = response.getStatusLine().getStatusCode();
	        if(statusCode != HttpStatus.SC_OK){
	        	System.out.println("请求出错: "+statusCode);
	            isSuccess = false;
	        }else{
	            int retCode = 0;
	            String sessendId = "";
	            // 返回码中包含retCode及会话Id
	            for(Header header : response.getAllHeaders()){
	                if(header.getName().equals("retcode")){
	                    retCode = Integer.parseInt(header.getValue());
	                }
	                if(header.getName().equals("SessionId")){
	                    sessendId = header.getValue();
	                }
	            }
	            System.out.println(" sessionId: "+sessendId+"\t"+"retCode: "+retCode);
	            
	           /* if(ErrorCodeHelper.IAS_SUCCESS != retCode ){
	                // 日志打印
	            	System.out.println("error return code,  sessionId: "sessendId"\t"+"retCode: "+retCode);
	                isSuccess = false;
	            }else{
	                isSuccess = true;
	            }*/
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        isSuccess = false;
	    }finally{
	        if(post != null){
	            try {
	                post.releaseConnection();
	                Thread.sleep(500);
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    return isSuccess;
	}

	// 构建唯一会话Id
	public static String getSessionId(){
	    UUID uuid = UUID.randomUUID();
	    String str = uuid.toString();
	    return str.substring(0, 8) + str.substring(9, 13) + str.substring(14, 18) + str.substring(19, 23) + str.substring(24);
	}

	
	
	/**
	     * post请求，参数为json字符串
	     * @param url 请求地址
	     * @param jsonString json字符串
	     * @return 响应
	     */
	    public static String postJson(String url,String jsonString)
	    {
	        String result = null;
	        CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpPost post = new HttpPost(url);
	        CloseableHttpResponse response = null;
	        try {
	            post.setEntity(new ByteArrayEntity(jsonString.getBytes("UTF-8")));
	            response = httpClient.execute(post);
	            if(response != null && response.getStatusLine().getStatusCode() == 200)
	            {
	                HttpEntity entity = response.getEntity();
	                result = entityToString(entity);
	            }
	            return result;
	        } catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        } catch (ClientProtocolException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                httpClient.close();
	                if(response != null)
	                {
	                    response.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return null;
	    }
	
	    
	   private static String entityToString(HttpEntity entity) throws IOException {
	       String result = null;
	       if(entity != null)
	       {
	           long lenth = entity.getContentLength();
	           if(lenth != -1 && lenth < 2048)
	           {
	               result = EntityUtils.toString(entity,"UTF-8");
	           }else {
	               InputStreamReader reader1 = new InputStreamReader(entity.getContent(), "UTF-8");
	               CharArrayBuffer buffer = new CharArrayBuffer(2048);
	               char[] tmp = new char[1024];
	               int l;
	               while((l = reader1.read(tmp)) != -1) {
	                   buffer.append(tmp, 0, l);
	               }
	               result = buffer.toString();
	           }
	       }
	       return result;
	   }
	   
	   
	   public static Map<String, Object> JsonToMap (String json){
		   
		   JSONObject jsonObject = JSONObject.fromObject(json);
	         
	        Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
	         
	        for(Entry<String,Object> entry : mapJson.entrySet()){
	            Object strval1 = entry.getValue();
	            JSONObject jsonObjectStrval1 = JSONObject.fromObject(strval1);
	            Map<String, Object> mapJsonObjectStrval1 = JSONObject.fromObject(jsonObjectStrval1);
	            
	            System.out.println("KEY:"+entry.getKey()+"  -->  Value:"+entry.getValue()+"\n");
	            
	            for(Entry<String, Object> entry1:mapJsonObjectStrval1.entrySet()){
	                System.out.println("KEY:"+entry1.getKey()+"  -->  Value:"+entry1.getValue()+"\n");
	            }
	             
	        }
		   return mapJson;
	   }
	   
	   
	   public static Map JsonToMapOne(String jsonArrayData){
		   JSONArray jsonArray = JSONArray.fromObject(jsonArrayData);
		   
	        List<Map<String,Object>> mapListJson = (List)jsonArray;
	        for (int i = 0; i < mapListJson.size(); i++) {
	            Map<String,Object> obj=mapListJson.get(i);
	             
	            for(Entry<String,Object> entry : obj.entrySet()){
	                String strkey1 = entry.getKey();
	                Object strval1 = entry.getValue();
	                System.out.println("KEY:"+strkey1+"  -->  Value:"+strval1+"\n");
	            }
	        }
	        return null;
		   
	   }
	   
	   
	   
	   public static boolean  updateProcess(File file){
		   boolean result=false;
		   HashMap<String, Object> fields = new HashMap<String, Object>();
		   fields.put("module", file.getName());
		   HashMap<String, File> files = new HashMap<String, File>();
		   files.put("file", file);
		   try {
			String resultjson = request("", fields, files);
			Map<String,Object>  map = JsonToMap(resultjson);
			 for(Entry<String,Object> entry : map.entrySet()){
				if(entry.getKey()!=null && entry.getKey().equals("result")){
					result=(boolean) entry.getValue();
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		   return result;
	   }
	    public ProcessDefinitions jsonToSearchFilter(String filters) {
			ProcessDefinitions searchFilter = null;
		        try {
		            JSONObject jsonobject = JSONObject.fromObject(filters);//将json格式的字符串转换成JSONObject 对象
		            JSONArray array = jsonobject.getJSONArray("rules");    //如果json格式的字符串里含有数组格式的属性，将其转换成JSONArray，以方便后面转换成对应的实体
		            List<ProcessDefinitions> rules = new ArrayList<ProcessDefinitions>();
		            for (int i = 0; i < array.size(); i++) {
		                JSONObject object = (JSONObject) array.get(i);     //将array中的数据进行逐条转换
						ProcessDefinitions rule = (ProcessDefinitions) JSONObject.toBean(object, ProcessDefinitions.class);  //通过JSONObject.toBean()方法进行对象间的转换
		                rules.add(rule);
		            }
		            String groupOp = jsonobject.getString("groupOp");  //简单的直接获取值
		            /*searchFilter = new Processdefinitions();                 //对SearchFilter对象进行组装
		            searchFilter.setGroupOp(groupOp);
		            searchFilter.setRules(rules);*/
		        } catch (Exception e) {
		            System.out.println("filters=" + filters.toString() + ".json转换成实体类出错");
		            e.printStackTrace();
		        }
		        return searchFilter;
		    }
	   
	
	    
	    /**
	     * 查询代办任务
	     * @param role
	     * @param userid
	     * @param url
	     * @param start
	     * @param pageSize
	     * @return
	     */
	    public List<MyTask> findByTask(String role, String userid, String url, Integer start, Integer pageSize){
	    	HashMap<String, Object> fields3 = new HashMap<String, Object>();
	    	if(role!=null && !role.equals("")){fields3.put("groupId", role);}
	    	if(userid!=null && !userid.equals("")){fields3.put("userId", userid);}
			fields3.put("start", start);
			fields3.put("pageSize", pageSize);
			String result;
			List<MyTask> rules = new ArrayList<MyTask>();
			try {
				result = request(url, fields3, null);
				JSONObject jsonObject = JSONObject.fromObject(result);
			    String recordListjson=jsonObject.getString("data");
			    JSONObject jsonobject = JSONObject.fromObject(recordListjson);//将json格式的字符串转换成JSONObject 对象
			    JSONArray array = jsonobject.getJSONArray("recordList");
		        JsonConfig jsonConfig = new JsonConfig();
		      
		        for (int i = 0; i < array.size(); i++) {
		            JSONObject object = (JSONObject) array.get(i);
		            JSONObject jobject = JSONObject.fromObject(object,jsonConfig);
		            //将array中的数据进行逐条转换
		            MyTask rule = (MyTask) JSONObject.toBean(jobject, MyTask.class);  //通过JSONObject.toBean()方法进行对象间的转换
		            rules.add(rule);
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
			
	    	return rules;
	    }
	    
	    public static String postData(String urlStr, String data, String contentType) {
	    	    BufferedReader reader = null;
	    	    try {
	    	        URL url = new URL(urlStr);
	    	        URLConnection conn = url.openConnection();
	    	        conn.setDoOutput(true);
	    	        conn.setConnectTimeout(CONNECT_TIMEOUT);
	    	        conn.setReadTimeout(CONNECT_TIMEOUT);
	    	        if(contentType!=null && !"".equals(contentType))
	    	            conn.setRequestProperty("content-type", contentType);
	    	        OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), DEFAULT_ENCODING);
	    	        if(data == null)
	    	            data = "";
	    	        writer.write(data); 
	    	        writer.flush();
	    	        writer.close();  
	    	
	    	        reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), DEFAULT_ENCODING));
	    	        StringBuilder sb = new StringBuilder();
	    	        String line = null;
	    	        while ((line = reader.readLine()) != null) {
	    	            sb.append(line);
	    	            sb.append("\r\n");
	    	        }
	    	        return sb.toString();
	    	    } catch (IOException e) {
	    	    	logger.error("Error connecting to " + urlStr + ": " + e.getMessage());
	    	    } finally {
	    	        try {
	    	            if (reader != null)
	    	                reader.close();
	    	        } catch (IOException e) {
	    	        }
	    	    }
	    	    return null;
	    	}

	    
	    
	    
	
	    
	    
	public static void main(String[] args) throws Exception {

		/* String lcdyurl="http://192.0.2.200:8081/workFlow/process/queryProcessDefinition";
	     HashMap<String, Object> fields6 = new HashMap<String, Object>();
	     fields6.put("processDefinitionKey", "Registrationprocess");
	     //fields6.put("processDefinitionId", "Registrationprocess");
	     fields6.put("start", "2");
	     fields6.put("pageSize", "5");
		 String result = request(lcdyurl, fields6, null);
		 System.out.println(result);
		 JSONObject jsonObject = JSONObject.fromObject(result);
		  String recordListjson=jsonObject.getString("data");
		   JSONObject jsonobject = JSONObject.fromObject(recordListjson);
		    //将json格式的字符串转换成JSONObject 对象
		    JSONArray array = jsonobject.getJSONArray("recordList");
	     for (int i = 0; i < array.size(); i++) {
	         JSONObject object = (JSONObject) array.get(i);
	         //将array中的数据进行逐条转换
	         ProcessView rule = (ProcessView) JSONObject.toBean(object, ProcessView.class);  //通过JSONObject.toBean()方法进行对象间的转换
	     }*/
		
		// 流程定义
				// http:/192.0.2.200:8081/workFlow/process/queryProcessDefinition
				// String processDefinitionKey,String processDefinitionId
				/*String lcdyurl = "http://192.0.2.200:8081/workFlow/process/queryProcessDefinition";
				HashMap<String, Object> fields6 = new HashMap<String, Object>();
				fields6.put("processDefinitionKey", "SJ_JHTZD");
				//fields6.put("processDefinitionId", "Registrationprocess");30024
				fields6.put("start", "1");
				fields6.put("pageSize", "5");
				String result = request(lcdyurl, fields6, null);
				System.out.println(result);*/
				
				/*String sc = "http://192.0.2.200:8081/workFlow/process/deleteProcessDefinition";
				HashMap<String, Object> fields6 = new HashMap<String, Object>();
				fields6.put("deploymentId", "30024");
				fields6.put("cascade", false);
				fields6.put("start", "1");
				fields6.put("pageSize", "5");
				String result = request(sc, fields6, null);
				System.out.println(result);*/
				// http://192.0.2.200:8081/workFlow/process/history/processInstance
		// 查询代办任务
				// 参数 userId:启动流程用户的id groupId:流程审批组的id start:分页起始数据 pageSize:分页显示的条数
		// 查询代办任务
				// 参数 userId:启动流程用户的id groupId:流程审批组的id start:分页起始数据 pageSize:分页显示的条数127822
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("index", "1");
		fields.put("size", "1000");
		String jsonyg="";
		try {
			//jsonyg = YHttpclicent.requestpost(yg_url,fields);
			String url="https://api.yonyoucloud.com/open/dst/staff/page_list?access_token=160dbdf49e2044ddb18d422f736bfcd9";
			JSONObject objectuser=JSONObject.fromObject(fields);
			jsonyg=HttpClient.postJson(url, objectuser.toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//String jsonyg = HttpClient.returnResult(fields, yg_url);
		JSONObject ygobj = JSONObject.fromObject(jsonyg);
		String msg = ygobj.getString("msg");
		if(msg!=null && msg.equals("success")) {
		String datayg = ygobj.getString("data");
		JSONObject contentygobj = JSONObject.fromObject(datayg);
		
		System.out.println(contentygobj);
		String mainjobinfo = contentygobj.getString("content");
		System.out.println(mainjobinfo);
		JSONObject mainjobinfoobj = JSONObject.fromObject(mainjobinfo);
		List<Mainjobinfo>  zns = JSON.parseObject(mainjobinfoobj.getString("mainjobinfo"), new TypeReference<List<Mainjobinfo>>(){});
		
		JsonConfig jsonC = new JsonConfig();
		jsonC.setExcludes(new String[] { "mainjobinfo"});
		JSONObject onezyspfx = JSONObject.fromObject(contentygobj,jsonC);
		String contentygjson = onezyspfx.getString("content");
		System.out.println(contentygjson);
		
		}
	}



}
