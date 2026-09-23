package com.hbfk.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class HttpClient {

	private static final Log logger = LogFactory.getLog(HttpClient.class);
	
	private final static int CONNECT_TIMEOUT = 5000; // in milliseconds
	private final static String DEFAULT_ENCODING = "UTF-8";
	 
	private static String charset = "UTF-8";
	
	public static Boolean ifSendOa = false;
	
	//redis微服务相关路径
		public static ResourceBundle sysConfig = ResourceBundle.getBundle("redis");
	
		private static final String redisurl=sysConfig.getString("redisurl").toString();
		
		public static final String generateUrl=sysConfig.getString("generateUrl").toString();
		
		public static final String genDoc=sysConfig.getString("genDoc").toString();
		
		public static final String cwDbUrl=sysConfig.getString("cwDbUrl").toString();
		
		public static final String cwDbUserName=sysConfig.getString("cwUserName").toString();

		public static final String cwDbPassword=sysConfig.getString("cwPassword").toString();
		
		
		

		public static final String HTTPPUT = "put";

		public static final String HTTPPOST = "post";

		public static final String HTTPGET = "get";
		
		public static final String HPPTDELETE = "delete";
		
		public static final Integer PARAMBODY = 0;
		
		public static final Integer PARAMMAP = 1;
		
		public static final Integer PARAMJSON = 2;

	public static final boolean isSendQYWX=false;

	public static final boolean isSendOa=false;
	
		
		//OA同步及消息待办配置
				public static ResourceBundle hr = ResourceBundle.getBundle("humanResources");
				
				public static String OaLoginUrl = (String) hr.getObject("OaLoginUrl");
				
				public static String oaUrl = (String) hr.getObject("oaUrl");
				
				public static String oaCode = (String) hr.getObject("oaCode");
				
				public static String restuname = (String) hr.getObject("restuname");
				
				public static String restpassword = (String) hr.getObject("restpassword");
				
				public static String getToken = (String) hr.getObject("getToken");

				public static String sendToDo = (String) hr.getObject("sendToDo");

				public static String sendToDoDone = (String) hr.getObject("sendToDoDone");
				
				public static String singleMessage = (String) hr.getObject("singleMessage");
				
				public static String messageList = (String) hr.getObject("messageList");

		
	/**
	 * 获取子公司树
	 */
	public static String getOrgChildrenUrl =redisurl+"/redis/getOrgChildren";
	
	/**
	 * 获取父子部门
	 */
	public static String getDeptUrl =redisurl+"/redis/getDept";
	
	
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

		public static String httpPutClient(String url, HashMap<String, Object> filedMap,
				Map<String, String> headerMap, Integer parambody) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String result = null;
			
			try {
				HttpPut httpPut = null; //请求url
				
				switch (parambody) {
					case 0:
						JSONObject jsonString = new JSONObject(filedMap);
			            //使用StringEntity转换成实体类型
						System.out.println(jsonString.toString());
			            StringEntity entity = new StringEntity(jsonString.toString(), "UTF-8");
			            httpPut = new HttpPut(url);
			            //将封装的参数添加到Post请求中
			            httpPut.setEntity(entity);
						break;
					default:
						URIBuilder builder = new URIBuilder(url);
						
						if(!filedMap.isEmpty()) {
							for (String filed : filedMap.keySet()) {
								builder.addParameter(filed, filedMap.get(filed).toString());
							}
						}
						URI uri = builder.build();
						httpPut = new HttpPut(uri);
					break;
				}
				
	            
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpPut.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
				
				
				response = httpClient.execute(httpPut);
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = streamToString(resEntity.getContent(), charset);
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
			return result;
		}

		public static String httpPostClient(String url, HashMap<String, Object> filedMap,
				Map<String, String> headerMap, Integer parambody) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String result = null;
			try {
				HttpPost httpPost = new HttpPost(url); //请求url
				
				switch (parambody) {
				case 0:
					JSONObject jsonString = new JSONObject(filedMap);
		            //使用StringEntity转换成实体类型
					System.out.println(jsonString.toString());
		            //将封装的参数添加到Post请求中
		            httpPost.setEntity(new StringEntity(jsonString.toString(), Consts.UTF_8));
					break;
				case 2:
					
					StringEntity entity1 = new StringEntity(JSON.toJSONString(filedMap),"UTF-8");
					entity1.setContentEncoding("UTF-8");
                    entity1.setContentType("application/json");
                    httpPost.setEntity(entity1);
					break;
				default:
					List<NameValuePair> params = new ArrayList<NameValuePair>(0);
					if (!filedMap.isEmpty()) {
						for (Map.Entry<String, Object> parameter : filedMap.entrySet()) {
							params.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue().toString()));
						}
					}
					httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
					break;
				}
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpPost.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
				response = httpClient.execute(httpPost);
				if(response == null) {
					return null;
				}
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = streamToString(resEntity.getContent(), charset);
				}
				EntityUtils.consume(resEntity);
			} finally {
				if(response != null) {
					response.close();
				}
				httpClient.close();
			}
			return result;
		}

		public static String httpGetClient(String url, HashMap<String, Object> filedMap,
				Map<String, String> headerMap) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String result = null;
			
			try {
				URIBuilder builder = new URIBuilder(url);
				
				if(filedMap != null && !filedMap.isEmpty()) {
					for (String filed : filedMap.keySet()) {
						builder.addParameter(filed, filedMap.get(filed).toString());
					}
				}
				URI uri = builder.build();
				HttpGet httpGet = new HttpGet(uri); //请求url
				
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpGet.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
				response = httpClient.execute(httpGet);
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = streamToString(resEntity.getContent(), charset);
				}
				EntityUtils.consume(resEntity);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
				httpClient.close();
			}
			return result;
		}

		
		
		public static byte[] httpGetByteClient(String url, HashMap<String, Object> filedMap,Map<String, String> headerMap) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			byte[] result = null;
			
			try {
				URIBuilder builder = new URIBuilder(url);
				
				if(filedMap != null && !filedMap.isEmpty()) {
					for (String filed : filedMap.keySet()) {
						builder.addParameter(filed, filedMap.get(filed).toString());
					}
				}
				URI uri = builder.build();
				HttpGet httpGet = new HttpGet(uri); //请求url
				
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpGet.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
				response = httpClient.execute(httpGet);
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toByteArray(resEntity);
				}
				EntityUtils.consume(resEntity);
			}catch (Exception e) {
				e.printStackTrace();
			} finally {
				response.close();
				httpClient.close();
			}
			return result;
		}
		
		public static String httpPostLoginClient(String url, HashMap<String, String> filedMap,
				Map<String, String> headerMap) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String result = null;
			
			try {
				HttpPost httpPost = new HttpPost(url); //请求url
				List<NameValuePair> params = new ArrayList<NameValuePair>(0);
				if (!filedMap.isEmpty()) {
					for (Map.Entry<String, String> parameter : filedMap.entrySet()) {
						params.add(new BasicNameValuePair(parameter.getKey(), parameter.getValue()));
					}
				}
				httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpPost.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
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
			return result;
		}

		public static String httpDeleteClient(String url, HashMap<String, Object> filedMap,
				Map<String, String> headerMap) throws Exception {
			CloseableHttpResponse response = null;// 响应类,
			CloseableHttpClient httpClient = HttpClients.createDefault();
			String result = null;
			
			try {
				URIBuilder builder = new URIBuilder(url);
				if(filedMap != null && !filedMap.isEmpty()) {
					for (String filed : filedMap.keySet()) {
						builder.addParameter(filed, filedMap.get(filed).toString());
					}
				}
				URI uri = builder.build();
				
				HttpDelete httpDelete  = new HttpDelete(uri); //请求url
				
				if (!headerMap.isEmpty()) {
					for (Map.Entry<String, String> vo : headerMap.entrySet()) {
						httpDelete.setHeader(vo.getKey(), vo.getValue());
					}
				}
				
				response = httpClient.execute(httpDelete);
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = streamToString(resEntity.getContent(), charset);
				}
				EntityUtils.consume(resEntity);
			} finally {
				response.close();
				httpClient.close();
			}
			return result;
		}
		
		/**
	     * 向指定URL发送GET方法的请求
	     * 
	     * @param url
	     *            发送请求的URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return URL 所代表远程资源的响应结果
	     */
	    public static String sendGet(String url, String param) {
	        String result = "";
	        BufferedReader in = null;
	        try {
	            String urlNameString = url + "?" + param;
	            URL realUrl = new URL(urlNameString);
	            // 打开和URL之间的连接
	            URLConnection connection = realUrl.openConnection();
	            // 设置通用的请求属性
	            connection.setRequestProperty("accept", "*/*");
	            connection.setRequestProperty("connection", "Keep-Alive");
	            connection.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 建立实际的连接
	            connection.connect();
	            // 获取所有响应头字段
	            Map<String, List<String>> map = connection.getHeaderFields();
	            // 遍历所有的响应头字段
	            for (String key : map.keySet()) {
	                System.out.println(key + "--->" + map.get(key));
	            }
	            // 定义 BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(new InputStreamReader(
	                    connection.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送GET请求出现异常！" + e);
	            e.printStackTrace();
	        }
	        // 使用finally块来关闭输入流
	        finally {
	            try {
	                if (in != null) {
	                    in.close();
	                }
	            } catch (Exception e2) {
	                e2.printStackTrace();
	            }
	        }
	        return result;
	    }


	    /**
	     * 向指定 URL 发送POST方法的请求
	     * 
	     * @param url
	     *            发送请求的 URL
	     * @param param
	     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	     * @return 所代表远程资源的响应结果
	     */
	    public static String sendPost(String url, String param) {
	        PrintWriter out = null;
	        BufferedReader in = null;
	        String result = "";
	        try {
	            URL realUrl = new URL(url);
	            // 打开和URL之间的连接
	            URLConnection conn = realUrl.openConnection();
	            // 设置通用的请求属性
	            conn.setRequestProperty("accept", "*/*");
	            conn.setRequestProperty("connection", "Keep-Alive");
	            conn.setRequestProperty("user-agent",
	                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
	            // 发送POST请求必须设置如下两行
	            conn.setDoOutput(true);
	            conn.setDoInput(true);
	            // 获取URLConnection对象对应的输出流
	            out = new PrintWriter(conn.getOutputStream());
	            // 发送请求参数
	            out.print(param);
	            // flush输出流的缓冲
	            out.flush();
	            // 定义BufferedReader输入流来读取URL的响应
	            in = new BufferedReader(
	                    new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = in.readLine()) != null) {
	                result += line;
	            }
	        } catch (Exception e) {
	            System.out.println("发送 POST 请求出现异常！"+e);
	            e.printStackTrace();
	        }
	        //使用finally块来关闭输出流、输入流
	        finally{
	            try{
	                if(out!=null){
	                    out.close();
	                }
	                if(in!=null){
	                    in.close();
	                }
	            }
	            catch(IOException ex){
	                ex.printStackTrace();
	            }
	        }
	        return result;
	    }    

}
