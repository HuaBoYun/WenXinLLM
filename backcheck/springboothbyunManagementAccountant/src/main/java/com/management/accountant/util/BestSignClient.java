package com.management.accountant.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;

import java.io.*;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author whthomas
 * @date 2018/6/17
 *
 * <h3>BestSignClient should be shared</h3>
 *
 * <p>BestSignClient performs best when you create a single {@code BestSignClient} instance and reuse it for
 * all of your API calls.
 *
 * <p>Use {@code new BestSignClient()} to create a shared instance with custom settings:
 * <pre>   {@code
 *
 *   // The singleton BestSignClient client.
 *   public final BestSignClient client = new BestSignClient(
 *                                          "https://api.bestsign.info",
 *                                          "l2ZxwSA6OBs6IgsQ6og9f7ixuPMR7hMQ",
 *                                          "3seTeUqUyhGlYZfD3CbGVXPDrcu1ieG4",
 *                                          privateKey
 * );
 *
 * }</pre>
 *
 * <p>Or use {@code spring} to create a singleton instance with custom settings:
 */
public class BestSignClient {

    private static final MediaType JSON_TYPE = MediaType.parse("application/json; charset=utf-8");

    /**
     * 绯荤粺鐨勫湴鍧�
     */
    private String host;

    /**
     * BestSign鍒嗛厤鐨勫鎴风缂栧彿
     */
    private String clientId;

    /**
     * 瀹㈡埛绔嚟璇�
     */
    private String clientSecret;

    /**
     * 绉侀挜
     */
    private String privateKey;

    /**
     * Token鐨勭紦瀛�
     */
    private String tokenCache;  //

    public BestSignClient(String host, String clientId, String clientSecret, String privateKey) {
        this.host = host;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.privateKey = privateKey;
    }
    public BestSignClient() {
    }
    private final ObjectMapper objectMapper = new ObjectMapper();

    //shared perform best
    private final OkHttpClient okHttpClient = new OkHttpClient
            .Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .build();

    /**
     * @param uriWithParam 璇锋眰鐨刄RL鍜屽弬鏁�
     * @param method       HTTP璇锋眰鍔ㄨ瘝
     * @param requestData  璇锋眰鐨勫唴瀹�
     *
     * @return JSON瀛楃涓�
     */
    public String executeRequest(String uriWithParam, String method, Object requestData) {
        return executeRequest(uriWithParam, method, requestData, 3);
    }

    private String executeRequest(String uriWithParam, String method, Object requestData, int retryTime) {
        while (retryTime > 0) {
            try {
                String token = queryToken();
                Long timestamp = System.currentTimeMillis();
                String urlWithQueryParam = String.format("%s%s", host, uriWithParam);
                final String signRSA = signRequest(uriWithParam, timestamp, requestData);
                Headers headers = new Headers
                        .Builder()
                        .add("Authorization", "bearer " + token)
                        .add("bestsign-sign-timestamp", timestamp.toString())
                        .add("bestsign-client-id", clientId)
                        .add("bestsign-signature-type", "RSA256")
                        .add("bestsign-signature", signRSA)
                        .build();

                final Request.Builder requestBuilder = new Request
                        .Builder()
                        .url(urlWithQueryParam)
                        .headers(headers);

                if (Objects.equals(method.toUpperCase(), "GET")) {
                    requestBuilder.get();
                } else {

                    final RequestBody requestBody = RequestBody.create(JSON_TYPE, requestData == null ? "" : objectMapper.writeValueAsString(requestData));
                    requestBuilder.method(method.toUpperCase(), requestBody);
                }

                final Response response = okHttpClient.newCall(requestBuilder.build()).execute();

                if (response.code() == 401) {
                    // token澶辨晥, 閲嶆柊鑾峰彇token
                    invalidToken(token);
                } else if (response.code() == 200) {
                    if(response.headers().get("Content-Type").equals("application/zip") || response.headers().get("Content-Type").equals("application/pdf")) {
                        return Base64.getEncoder().encodeToString(response.body().bytes());
                    }
                    return response.body() != null ? response.body().string() : null;
                }
                retryTime--;
            } catch (Exception ex) {
                retryTime--;
            }

        }
        return null;
    }
//    public void downloadZip(String content,String pathString) {
//        try {
//        	 BASE64Decoder decoder = new BASE64Decoder();
//             try{
//                 // Base64解码
//                 byte[] b =decoder.decodeBuffer(content);
//                 for (int i = 0; i < b.length; ++i) {
//                     if (b[i] < 0) {// 调整异常数据
//                         b[i] += 256;
//                     }
//                 }
//           
//                 //将byte转换为文件
//                 // 生成图片路径和文件名
//                 File file  = new File(pathString);
//                 FileOutputStream  out = new FileOutputStream(pathString);
//                 out.write(b);
//                 out.flush();
//                 out.close();
//             }catch(Exception e){
//            	 
//             }
//        }catch(Exception e){
//       	 
//        }
//    }
    
    private String signRequest(String uriWithParam, Long timestamp, Object requestData) throws Exception {
        String content = String.format(
                "bestsign-client-id=%sbestsign-sign-timestamp=%sbestsign-signature-type=%srequest-body=%suri=%s",
                clientId,
                timestamp.toString(),
                "RSA256",
                MD5Utils.stringMD5(requestData == null ? "" : objectMapper.writeValueAsString(requestData)),
                uriWithParam);

        return RSAUtils.signRSA(content, privateKey);
    }

    public synchronized String queryToken() throws IOException {
        if (null == tokenCache) {
            Map<String, String> requestData = new HashMap<>(2);
            requestData.put("clientId", clientId);
            requestData.put("clientSecret", clientSecret);
            final RequestBody requestBody = RequestBody.create(JSON_TYPE, objectMapper.writeValueAsString(requestData));
            Request request = new Request
                    .Builder()
                    .url(String.format("%s/api/oa2/client-credentials/token", host))
                    .post(requestBody)
                    .build();

            final Response response = okHttpClient.newCall(request).execute();
            if (response.body() != null) {
                tokenCache = objectMapper.readTree(response.body().string()).get("data").get("accessToken").asText();
                return tokenCache;
            }
        } else {
            return tokenCache;
        }
        return null;
    }

    private synchronized void invalidToken(String oldToken) {
        if (oldToken.equals(tokenCache)) {
            tokenCache = null;
        }
    }
    public   void getFile(byte[] bfile, String filePath,String fileName) {

    	BufferedOutputStream bos= null;

    	FileOutputStream fos= null;

    	File file= null;try{

    	File dir= new File(filePath);if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在

    	dir.mkdirs();

    	}

    	file= new File(filePath+"\\"+fileName);

    	fos= new FileOutputStream(file);

    	bos= new BufferedOutputStream(fos);

    	bos.write(bfile);

    	}catch(Exception e) {

    	e.printStackTrace();

    	}finally{if (bos != null) {try{

    	bos.close();

    	}catch(IOException e1) {

    	e1.printStackTrace();

    	}

    	}if (fos != null) {try{

    	fos.close();

    	}catch(IOException e1) {

    	e1.printStackTrace();

    	}

    	}

    	}

    	}
    
    public static String encodeBase64File(String path) {
		File file = new File(path);
		FileInputStream inputFile;
		byte[] buffer = null;
 
		try {
			inputFile = new FileInputStream(file);
			buffer = new byte[(int) file.length()];
			inputFile.read(buffer);
			inputFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 
		return Base64.getEncoder().encodeToString(buffer);
 
	}
}
