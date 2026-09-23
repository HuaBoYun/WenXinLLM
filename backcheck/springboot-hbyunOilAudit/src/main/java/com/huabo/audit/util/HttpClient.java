package com.huabo.audit.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.huabo.audit.oracle.entity.TblMyTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class HttpClient {
	
	public static ResourceBundle rb = ResourceBundle.getBundle("setting/process");

	private static String charset = "UTF-8";
	

	public static String IP = (String) rb.getObject("IP");

	public static String fburl = (String) rb.getObject("fburl");

	public static String qdurl = (String) rb.getObject("qdurl");
	
	public static String rwurl = (String) rb.getObject("rwurl");
	
	public static String blurl = (String) rb.getObject("blurl");
	
	public static String lczxurl = (String) rb.getObject("lczxurl");
	
	public static String jkurl = (String) rb.getObject("jkurl");

	public static String formurl = (String) rb.getObject("formurl");
	
	public static String nextapprover =(String) rb.getObject("nextapprover");
	//redis微服务相关路径
	private static final String redisurl= rb.getString("redisurl").toString();
	/**
	 * 获取部门树
	 */
	public static String getDeptString =redisurl+"/redis/getDeptString";
	
	public static Map<String, Object> lczxProcessJson(String taskid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String resultjson = request(lczxurl + taskid, null, null);
			JSONObject jsonObject = JSONObject.fromObject(resultjson);
			String result = jsonObject.getString("result");
			String recordListjson = jsonObject.getString("data");
			System.out.println(result);
			String s = recordListjson.substring(
					recordListjson.indexOf("[") + 1,
					recordListjson.lastIndexOf("]"));
			System.out.println(s);
			map.put("result", result);
			map.put("data", s);
			System.out.println(resultjson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	public static Map<String, Object> handleProcessJson(String userid,
			String blyj, String taskid) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		try {
			String taskurl = blurl + taskid;
			HashMap<String, Object> fields4 = new HashMap<String, Object>();
			fields4.put("userId", userid);
			fields4.put("transitionName", "transition");
			fields4.put("transitionValue", blyj);
			String resultjson = request(taskurl, fields4, null);
			JSONObject jsonObject = JSONObject.fromObject(resultjson);
			String result = jsonObject.getString("result");
			String recordListjson = jsonObject.getString("data");
			map.put("result", result);
			map.put("data", recordListjson);
			System.out.println(resultjson);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * 查询代办任务
	 * 
	 * @param role
	 *            角色
	 * @param userid
	 *            用户id
	 * @param url
	 *            代办路径
	 * @param start
	 *            第几页
	 * @param pageSize
	 *            每页显示条数
	 * @return
	 */
	public static List<TblMyTask> findByTask(String role, String userid,
			Integer start, Integer pageSize) {
		HashMap<String, Object> fields3 = new HashMap<String, Object>();
		if(role==null || role==""){
			role="未定义";
		}
		fields3.put("groupId", role);
		fields3.put("start", start);
		fields3.put("pageSize", pageSize);
		
		String result;
		List<TblMyTask> rules = new ArrayList<TblMyTask>();
		try {
			result = request(rwurl, fields3, null);
			JSONObject jsonObject = JSONObject.fromObject(result);
			String recordListjson = jsonObject.getString("data");
			JSONObject jsonobject = JSONObject.fromObject(recordListjson);
			// 将json格式的字符串转换成JSONObject 对象
			JSONArray array = jsonobject.getJSONArray("recordList");
			JsonConfig jsonConfig = new JsonConfig();
			// 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
			jsonConfig.setExcludes(new String[] { "object" });
			for (int i = 0; i < array.size(); i++) {
				JSONObject object = (JSONObject) array.get(i);
				JSONObject jobject = JSONObject.fromObject(object, jsonConfig);
				// 将array中的数据进行逐条转换
				TblMyTask rule = (TblMyTask) JSONObject.toBean(jobject, TblMyTask.class); // 通过JSONObject.toBean()方法进行对象间的转换
				rules.add(rule);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<TblMyTask> taskgrpoup = findByTaskgrpoup(role, userid, start, pageSize);
		rules.addAll(findByTaskgrrole(role, userid, start, pageSize));
		rules.addAll(taskgrpoup);
		return rules;
	}
	
	public static List<TblMyTask> findByTaskgrrole(String role, String userid,
			Integer start, Integer pageSize) {
		HashMap<String, Object> fields3 = new HashMap<String, Object>();
		
		if (userid != null && !userid.equals("")) {
			fields3.put("groupId", userid);
		}
		fields3.put("start", start);
		fields3.put("pageSize", pageSize);
		String result;
		List<TblMyTask> rules = new ArrayList<TblMyTask>();
		try {
			result = request(rwurl, fields3, null);
			JSONObject jsonObject = JSONObject.fromObject(result);
			if(jsonObject.containsKey("result")){
				String recordListjson = jsonObject.getString("data");
				JSONObject jsonobject = JSONObject.fromObject(recordListjson);
				// 将json格式的字符串转换成JSONObject 对象
				JSONArray array = jsonobject.getJSONArray("recordList");
				JsonConfig jsonConfig = new JsonConfig();
				// 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
				jsonConfig.setExcludes(new String[] { "object" });
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					JSONObject jobject = JSONObject.fromObject(object, jsonConfig);
					// 将array中的数据进行逐条转换
					TblMyTask rule = (TblMyTask) JSONObject.toBean(jobject, TblMyTask.class); // 通过JSONObject.toBean()方法进行对象间的转换
					rules.add(rule);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rules;
	}
	
	public static List<TblMyTask> findByTaskgrpoup(String role, String userid,
			Integer start, Integer pageSize) {
		HashMap<String, Object> fields3 = new HashMap<String, Object>();
		
		if (userid != null && !userid.equals("")) {
			fields3.put("userId", userid);
		}
		fields3.put("start", start);
		fields3.put("pageSize", pageSize);
		String result;
		List<TblMyTask> rules = new ArrayList<TblMyTask>();
		try {
			result = request(rwurl, fields3, null);
			JSONObject jsonObject = JSONObject.fromObject(result);
			if(jsonObject.containsKey("result")){
				String recordListjson = jsonObject.getString("data");
				JSONObject jsonobject = JSONObject.fromObject(recordListjson);
				// 将json格式的字符串转换成JSONObject 对象
				JSONArray array = jsonobject.getJSONArray("recordList");
				JsonConfig jsonConfig = new JsonConfig();
				// 调用setsetExcludes方法 过滤一些属性的值 比喻 “object”。
				jsonConfig.setExcludes(new String[] { "object" });
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					JSONObject jobject = JSONObject.fromObject(object, jsonConfig);
					// 将array中的数据进行逐条转换
					TblMyTask rule = (TblMyTask) JSONObject.toBean(jobject, TblMyTask.class); // 通过JSONObject.toBean()方法进行对象间的转换
					rules.add(rule);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rules;
	}
	
	public static Map<String, Object> startProcessAll(String taskid, String cs) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> fields = new HashMap<String, Object>();
		fields.put("text", cs);
		String resultjson = "";
		try {
			resultjson = request(qdurl + taskid, fields, null);
			JSONObject jsonObject = JSONObject.fromObject(resultjson);
			String result = jsonObject.getString("result");
			String recordListjson = jsonObject.getString("data");
			JSONObject jsonObject1 = JSONObject.fromObject(recordListjson);
			String processInstanceId = jsonObject1
					.getString("processInstanceId");
			String processDefinitionKey = jsonObject1
					.getString("processDefinitionKey");
			map.put("result", result);
			map.put("processInstanceId", processInstanceId);
			map.put("processDefinitionKey", processDefinitionKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
	// 此方法是POST请求上传的参数中包含本地图片信息File类型
		public static String request(String url, HashMap<String, Object> fields,HashMap<String, File> files) throws Exception {

			CloseableHttpClient httpClient = HttpClients.createDefault();
			CloseableHttpResponse response = null;
			String result = null;
			// HttpClient请求的相关设置，可以不用配置，用默认的参数，这里设置连接和超时时长(毫秒)
			//RequestConfig config = RequestConfig.custom().setConnectTimeout(30000).setSocketTimeout(30000).build();
			try {
				MultipartEntityBuilder entityBuilder = MultipartEntityBuilder
						.create();
				if (fields != null) {
					addFields(entityBuilder, fields);
				}
				if (files != null) {
					addFiles(entityBuilder, files);
				}

				HttpPost httpPost = new HttpPost(url);
				// httpPost.addHeader("Content-type","application/json; charset=utf-8");
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
		
		
		private static void addFiles(MultipartEntityBuilder entityBuilder,
				HashMap<String, File> files) {

			if (files == null) {
				return;
			}
			for (String name : files.keySet()) {
				File file = files.get(name);
				FileBody fileBody = new FileBody(file);
				entityBuilder.addPart(name, fileBody);
			}
		}

		private static void addFields(MultipartEntityBuilder entityBuilder,HashMap<String, Object> fields) {
			for (String name : fields.keySet()) {
				if(fields.get(name)!=null){
					String value = fields.get(name).toString();
					ContentType contentType = ContentType.create(HTTP.PLAIN_TEXT_TYPE,
							HTTP.UTF_8);
					StringBody StringBody = new StringBody(value, contentType);
					entityBuilder.addPart(name, StringBody);
				}
			}
		}

		// 此方法是把传进的字节流转化为相应的字符串并返回，此方法一般在网络请求中用到
		private static String streamToString(InputStream inputStream, String charset)
				throws Exception {
			StringBuilder stringBuilder = new StringBuilder();
			
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, charset);
			BufferedReader reader = new BufferedReader(inputStreamReader);
					String line = null;
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line).append("\r\n");
			}
			return stringBuilder.toString();
		}
		/**
		 * 查询当前执行人
		 * @param businesskey
		 * @return
		 */
			public static String nextapprover(String businesskey) {
				String resultjson = "";
				HashMap<String, Object> fields = new HashMap<String, Object>();
				fields.put("businesskey", businesskey);
				try {
					resultjson = request(nextapprover, fields, null);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return resultjson;
			}

//		public static List<TblMyTaskMySql> findByMySqlTask(String string, String string2, int i, int j) {
//			// TODO Auto-generated method stub
//			return null;
//		}
}
