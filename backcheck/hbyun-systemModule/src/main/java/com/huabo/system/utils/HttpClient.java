package com.huabo.system.utils;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.ProcessView;
import com.huabo.system.entity.TblMyTask;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

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

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class HttpClient {

	private static String charset = "UTF-8";
	public static ResourceBundle rb = ResourceBundle.getBundle("setting/process");

	public static String IP = (String) rb.getObject("IP");

	public static String fburl = (String) rb.getObject("fburl");

	public static String qdurl = (String) rb.getObject("qdurl");
	
	public static String rwurl = (String) rb.getObject("rwurl");
	
	public static String blurl = (String) rb.getObject("blurl");
	
	public static String lcdyurl = (String) rb.getObject("lcdyurl");
	
	public static String lczxurl = (String) rb.getObject("lczxurl");
	
	public static String jkurl = (String) rb.getObject("jkurl");

	public static String formurl = (String) rb.getObject("formurl");

	public static String acurl = (String) rb.getObject("acurl");
	
	//AI大模型调用地址路径
	public static String wxAIurl = (String) rb.getObject("wnaiUrl");
	
	//法律大模型接口
	public static String lawurl = (String) rb.getObject("law");
	
	//文档生成接口
	public static String docurl = (String) rb.getObject("doc");
	
	//财务决策分析接口
	public static String finurl = (String) rb.getObject("fin");
	
	
	public static ResourceBundle sxjm = ResourceBundle.getBundle("setting/sxjm");
	
	//OA同步及消息待办配置
	public static ResourceBundle hr = ResourceBundle.getBundle("setting/humanResources");
	
	public static String OaLoginUrl = (String) hr.getObject("OaLoginUrl");
	
	public static String oaUrl = (String) hr.getObject("oaUrl");
	
	public static String oaCode = (String) hr.getObject("oaCode");
	
	public static String restuname = (String) hr.getObject("restuname");
	
	public static String restpassword = (String) hr.getObject("restpassword");
	
	public static String getToken = (String) hr.getObject("getToken");

	public static String sendToDo = (String) hr.getObject("sendToDo");

	public static String sendToDoDone = (String) hr.getObject("sendToDoDone");
	
	public static String singleMessage = (String) hr.getObject("singleMessage");
	
	public static String oaFlowInfoList = (String) hr.getObject("oaFlowInfoList");
	
	public static Boolean ifSendOa = true;
	
	//redis微服务相关路径
	private static final String redisurl= rb.getString("redisurl").toString();
	/**
	 * 获取部门树
	 */
	public static String getDeptString =redisurl+"/redis/getDeptString";
	/**
	 * 获取父子部门
	 */
	public static String getDeptUrl =redisurl+"/redis/getDept";
	/**
	 * 更新单个公司redis
	 */
	public static String insertOrgOneUrl =redisurl+"/redis/insertOrgOne";
	/**
	 * 获取子公司树
	 */
	public static String getOrgChildrenUrl =redisurl+"/redis/getOrgChildren";
	
	/**
	 * 更新所有组织redis
	 */
	public static String insertOrgAllUrl =redisurl+"/redis/insertOrgAll";
	
	/**
	 * 获取父子公司树
	 */
	public static String getOrgUrl =redisurl+"/redis/getOrg";
	public static String sxjm_findone = (String) sxjm.getObject("sxjm_findone");
	//风险分类-根据父级查询子级分页
	public static String sxjm_bypanrent = (String) sxjm.getObject("sxjm_bypanrent");
	//风险分类-保存或修改
	public static String sxjm_saverisk = (String) sxjm.getObject("sxjm_saverisk");
	//风险分类-删除单个风险
	public static String sxjm_deleterisk = (String) sxjm.getObject("sxjm_deleterisk");
	//风险分类-导出为excel
	public static String sxjm_exploreexcel = (String) sxjm.getObject("sxjm_exploreexcel");

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
				if(null != response) {
					response.close();
				}
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

	public static boolean updateProcess(File file) {
		boolean result = false;
		HashMap<String, Object> fields = new HashMap();
		fields.put("module", file.getName());
		HashMap<String, File> files = new HashMap();
		files.put("file", file);

		try {
			String resultjson = request(fburl, fields, files);
			Map<String, Object> map = JsonToMap(resultjson);
			Iterator var6 = map.entrySet().iterator();

			while(var6.hasNext()) {
				Map.Entry<String, Object> entry = (Map.Entry)var6.next();
				if (entry.getKey() != null && ((String)entry.getKey()).equals("result")) {
					result = (Boolean)entry.getValue();
					break;
				}
			}
		} catch (Exception var8) {
			var8.printStackTrace();
		}

		return result;
	}

	public static Map<String, Object> JsonToMap(String json) {
		JSONObject jsonObject = JSONObject.fromObject(json);
		Map<String, Object> mapJson = JSONObject.fromObject(jsonObject);
		Iterator var3 = mapJson.entrySet().iterator();

		while(var3.hasNext()) {
			Map.Entry<String, Object> entry = (Map.Entry)var3.next();
			Object strval1 = entry.getValue();
			JSONObject jsonObjectStrval1 = JSONObject.fromObject(strval1);
			Map<String, Object> mapJsonObjectStrval1 = JSONObject.fromObject(jsonObjectStrval1);
			System.out.println("KEY:" + (String)entry.getKey() + "  -->  Value:" + entry.getValue() + "\n");
			Iterator var8 = mapJsonObjectStrval1.entrySet().iterator();

			while(var8.hasNext()) {
				Map.Entry<String, Object> entry1 = (Map.Entry)var8.next();
				System.out.println("KEY:" + (String)entry1.getKey() + "  -->  Value:" + entry1.getValue() + "\n");
			}
		}

		return mapJson;
	}
	/**
	 * 流程定义
	 * @param pageNumber
	 * @param size
	 * @param processid
	 * @return
	 */
	public static PageInfo findByProcess(Integer pageNumber, Integer size,String processid) {
		if(pageNumber==null) {
			pageNumber=1;
		}
		HashMap<String, Object> fields6 = new HashMap<String, Object>();
		 fields6.put("processDefinitionKey",processid);
		fields6.put("start", pageNumber);
		fields6.put("pageSize", size);
		String result;
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>();
		try {
			result = request(lcdyurl, fields6, null);
			System.out.println(result);
			if(result!=null &&result!=""){
				JSONObject jsonObject = JSONObject.fromObject(result);
				String recordListjson = jsonObject.getString("data");
				JSONObject jsonobject = JSONObject.fromObject(recordListjson);
				String pageSize= jsonobject.getString("pageSize");
				String currentPage=jsonobject.getString("currentPage");
				String recordCount=jsonobject.getString("recordCount");
				
				pageInfo.setPageSize(Integer.parseInt(pageSize));
				pageInfo.setCurrentPage(Integer.parseInt(currentPage));
				pageInfo.setTotalRecord(Integer.parseInt(recordCount));
				
				// 将json格式的字符串转换成JSONObject 对象
				JSONArray array = jsonobject.getJSONArray("recordList");
				List<Map<String, Object>> pd = new ArrayList<Map<String, Object>>();
				for (int i = 0; i < array.size(); i++) {
					JSONObject object = (JSONObject) array.get(i);
					// 将array中的数据进行逐条转换
					ProcessView rule = (ProcessView) JSONObject.toBean(object,ProcessView.class); // 通过JSONObject.toBean()方法进行对象间的转换
					Map<String, Object> values = new HashMap<String, Object>();
					values.put("id", rule.getId());
					values.put("name", rule.getName());
					values.put("key", rule.getKey());
					values.put("version", rule.getVersion());
					values.put("resourceName", rule.getResourceName());
					values.put("deploymentId", rule.getDeploymentId());
					values.put("DiagramResourceName", rule.getDiagramResourceName());
					pd.add(values);
				}
				pageInfo.setTlist(pd);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageInfo;
	}
}
