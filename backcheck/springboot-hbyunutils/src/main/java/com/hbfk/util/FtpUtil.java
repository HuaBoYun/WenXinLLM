package com.hbfk.util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.http.HttpHeaders;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.alibaba.fastjson.JSONObject;
import com.hbfk.entity.TblAttachment;
import com.hbfk.entity.TblContractAppendixsigning;

import cn.hutool.core.io.resource.InputStreamResource;
  
/** 
 *  
 * @author tjj
 * ftp文件
 */  
public  class FtpUtil {  
	public static final String DOCDIC = PropertyFileReader.getItem("file.path");
	
	private static final int DEFAULT_CONNECT_TIMEOUT = 0;
	private static final String ENCODING_UTF8="UTF-8";
	private static final String ENCODING_GBK="GBK";
	
	
	public static ResourceBundle rb = ResourceBundle.getBundle("fileftp");
	/**
	 * 系统上传的文件
	 */
	public static String Uploadfilepath =(String) rb.getObject("uploadfilepath");
	/**
	 * 系统通过发送自动生成的文件
	 */
	public static String Sengfilepath =(String) rb.getObject("sengfilepath");
	
	/**
	 * 流程图相关的xml文件
	 */
	public static String Xmlfilepath =(String) rb.getObject("xmlfilepath");
	/**
	 * 模板相关的文件
	 */
	public static String Templatefilepath =(String) rb.getObject("templatefilepath");
	
	/**
	 * 合同相关的文件
	 */
	public static String constractfilepath =(String) rb.getObject("constractfilePath");
	
	/**
	 * openoffice在本地的路径
	 */
	public static String openOfficePath =(String) rb.getObject("openOfficePath");
	
	/**
	 * openoffice端口号
	 */
	public static String openOfficeport =(String) rb.getObject("openOfficeport");
	
	/**
	 * openofficeurl
	 */
	public static String openOfficeip =(String) rb.getObject("openOfficeip");
	
	/**
	 * 系统上传的文件
	 */
	public static String previewurl =(String) rb.getObject("previewurl");
	
	//FTP服务器主机地址 
	public static String Ftpip =(String) rb.getObject("ftpip");
	//FTP登录账号/linux用户 名 
	private static String Username =(String) rb.getObject("username");
	//FTP登录密码 /linux密码 
	private static String Pssword =(String) rb.getObject("password");
	
	// FTP服务器端号 
	public static String Port =(String) rb.getObject("port");
	
	// FTP服务器端号 
	public static String pythonFlfgPath  =(String) rb.getObject("pythonFlfgPath");
	
		
	public static String filePath  =(String) rb.getObject("uploadpath");

	public static String secret  =(String) rb.getObject("secret");

	public static String downloadUrl  =(String) rb.getObject("downloadUrl");


	
    /**  
     * 系统上传的文件
     * Description: 向FTP服务器上传文件 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean uploadFile( String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  

            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(Uploadfilepath);
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = Uploadfilepath.split("/");  
                String tempPath = Uploadfilepath;
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;
    }
    /**
    * @description 上传文件 文件所在路径
    * @author   lyz
    * @date 2022/4/18 20:12
    */
    public static String uploadFilePath(String filename, InputStream input) throws Exception {
        String  tempPath = Uploadfilepath;
        FTPClient ftp = new FTPClient();

        int reply;
        System.out.println(Ftpip);
        ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器
        // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口
        ftp.login(Username, Pssword);// 登录
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return "";
        }
        //切换到上传路径下
        boolean change = ftp.changeWorkingDirectory(Uploadfilepath);
        //切换到上传目目录
        if (false==change) {
            //如果目录不存在创建目目录
            String[] dirs = Uploadfilepath.split("/");
             tempPath = Uploadfilepath;
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) continue;
                tempPath += "/" + dir;
                if (!ftp.changeWorkingDirectory(tempPath)) {
                    if (!ftp.makeDirectory(tempPath)) {
                        return "";
                    } else {
                        ftp.changeWorkingDirectory(tempPath);
                    }
                }
            }
        }
        //设置上传文件的类型为二进制类型
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        boolean storeFile=true;
        try{
            filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            storeFile = ftp.storeFile(new String(filename), input);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(false==storeFile){
            return "";
        }

        input.close();
        ftp.logout();

        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException ioe) {
            }
        }

        return tempPath;
    }
    /**  
     * 系统上传的文件
     * Description: 向FTP服务器上传文件 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean uploadFilePython( String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(pythonFlfgPath);
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = pythonFlfgPath.split("/");  
                String tempPath = pythonFlfgPath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;  
    }  
    
    
    /**  
     * 系统通过发送自动生成的文件
     * Description: 向FTP服务器上传文件 
     * @param filePath 文件夹路径 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean sengfile( String filePath, String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(Sengfilepath+filePath);  
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = filePath.split("/");  
                String tempPath = Sengfilepath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;  
    }  
    
    
    
    
    /**  
     * 流程图相关的xml文件
     * Description: 向FTP服务器上传文件 
     * @param filePath 文件夹路径 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean xmlfile(String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(Xmlfilepath);  
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = Xmlfilepath.split("/");  
                String tempPath = Xmlfilepath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;  
    }  
    
    
    
    
    
    /**  
     * 模板相关的文件
     * Description: 向FTP服务器上传文件 
     * @param filePath 文件夹路径 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean templatefile(  String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(Templatefilepath);  
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = Templatefilepath.split("/");  
                String tempPath = Templatefilepath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }  
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;  
    }  
    
    
    
    
    /**
     * /**
	 * 向ftp上传文件
     * @param servicePath 指定写入的目录
     * @param fileName 文件名称
     * @param path 设置上传目录
     * @param username 用户
     * @param password  密码
     * @param ip   服务器ip
     */
	public static boolean upload(String servicePath, String fileName,String path,String username,String password,String ip) {
		FTPClient ftpClient = new FTPClient();
		// 指定写入的目录
		File file=new File(servicePath+"/"+fileName);
		try {
			// 2.连接服务器
			ftpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
			ftpClient.connect(ip);
			boolean b = ftpClient.login(username, password);

			// 检测连接是否成功

			int reply = ftpClient.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftpClient.disconnect();
				System.exit(1);
			}
			ftpClient.setControlEncoding(ENCODING_UTF8);
			FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
			conf.setServerLanguageCode("zh");
			FileInputStream fis = null;
			fis = new FileInputStream(file);
			// 设置上传目录
			ftpClient.changeWorkingDirectory(path);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding(ENCODING_UTF8);

			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			// 上传
			fileName=new String(fileName.getBytes("GBK"),"iso-8859-1");
			b = ftpClient.storeFile(fileName, fis);
			if(false==b){  
                return false;  
            }  
			org.apache.commons.io.IOUtils.closeQuietly(fis);
			ftpClient.logout();
		} catch (SocketException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();

		} finally {
			try {
				
				ftpClient.disconnect();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		return true;

	}
	
	
	
	
	
	/**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFile( TblAttachment tblAttachment,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            String[] listNames = ftp.listNames();
            ftp.enterLocalPassiveMode();
            for (String ff : listNames) {  
            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(tblAttachment.getAttpath())) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                    	
                     }
                    System.out.println(bytesToRead);
                    System.out.println(fname);
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
                    break;
            	}else if(fname.equals(tblAttachment.getFileName())){
            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
                    break;
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**  
     * Description: 从FTP服务器下载python爬虫文件
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFilePython( TblAttachment tblAttachment,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(pythonFlfgPath);// 转移到FTP服务器目录下  
            String[] listNames = ftp.listNames();
            ftp.enterLocalPassiveMode();
            for (String ff : listNames) {  
            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(tblAttachment.getAttpath())) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		//toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}else if(fname.equals(tblAttachment.getFileName())){
            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    } 
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadSendFile(TblAttachment tblAttachment,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Sengfilepath);// 转移到FTP服务器目录下  
            String[] listNames = ftp.listNames();
            for (String ff : listNames) {  
            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
                 if (fname.equals(tblAttachment.getAttpath())) {  
                 	InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
             		response.reset();
             		response.setCharacterEncoding("GBK");
             		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
             		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
             		response.setContentType("application/octet-stream");
             		byte[] buffer = new byte[1024];
                     int bytesToRead = -1;
                     // 通过循环将读入的Word文件的内容输出到浏览器中
                     while((bytesToRead = ins.read(buffer)) != -1) {
                     	toClient.write(buffer, 0, bytesToRead);
                      }
             		toClient.write(buffer);
             		ins.close();
             		toClient.flush();
                     toClient.close();
             	} else if(fname.equals(tblAttachment.getFileName())){
            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            result = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadXmlFile(String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Xmlfilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                 if (fname.equals(filename)) {  
             		boolean fileExist = isFTPFileExist(Xmlfilepath+filename);
             		if(fileExist){
             			boolean removeFile = removeFile(filename, Xmlfilepath);
             			if(removeFile){
             				InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
                     		response.reset();
                     		response.setCharacterEncoding("GBK");
                     		response.addHeader("Content-Disposition", "attachment;filename=" + new String( filename.getBytes("GBK"), "ISO8859-1" )); 
                     		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                     		response.setContentType("application/octet-stream");
                     		byte[] buffer = new byte[1024];
                             int bytesToRead = -1;
                             // 通过循环将读入的Word文件的内容输出到浏览器中
                             while((bytesToRead = ins.read(buffer)) != -1) {
                             	toClient.write(buffer, 0, bytesToRead);
                              }
                     		toClient.write(buffer);
                     		ins.close();
                     		toClient.flush();
                            toClient.close();
             			}
             		}else{
             			InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
                 		response.reset();
                 		response.setCharacterEncoding("GBK");
                 		response.addHeader("Content-Disposition", "attachment;filename=" + new String( filename.getBytes("GBK"), "ISO8859-1" )); 
                 		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                 		response.setContentType("application/octet-stream");
                 		byte[] buffer = new byte[1024];
                         int bytesToRead = -1;
                         // 通过循环将读入的Word文件的内容输出到浏览器中
                         while((bytesToRead = ins.read(buffer)) != -1) {
                         	toClient.write(buffer, 0, bytesToRead);
                          }
                 		toClient.write(buffer);
                 		ins.close();
                 		toClient.flush();
                         toClient.close();
             		}
                 	
             	} 
            }  
  
            ftp.logout();  
            result = true;  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    } 
    
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadTemplateFile( TblAttachment tblAttachment,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Templatefilepath);// 转移到FTP服务器目录下  
            ftp.enterLocalPassiveMode();
            String[] listNames = ftp.listNames();
            for (String ff : listNames) {  
            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
                 if (fname.equals(tblAttachment.getAttpath())) {  
                 	InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
             		response.reset();
             		response.setCharacterEncoding("GBK");
             		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
             		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
             		response.setContentType("application/octet-stream");
             		byte[] buffer = new byte[1024];
                     int bytesToRead = -1;
                     // 通过循环将读入的Word文件的内容输出到浏览器中
                     while((bytesToRead = ins.read(buffer)) != -1) {
                     	toClient.write(buffer, 0, bytesToRead);
                      }
             		toClient.write(buffer);
             		ins.close();
             		toClient.flush();
                     toClient.close();
             	}else if(fname.equals(tblAttachment.getFileName())){
            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            result = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    } 
    
    
	
	
      
    
    public static boolean downFtpFile(String fileName,  String localPath) {  
        boolean success = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return success;  
            }  
            ftp.changeWorkingDirectory(Xmlfilepath);// 转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
                String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);  
                if (fname.equals(fileName)) {  
                    File localFile = new File(localPath);  
                    OutputStream is = new FileOutputStream(localFile);  
                    ftp.retrieveFile(ff.getName(), is);  
                    is.close();  
                    break;  
                }  
            }  
            ftp.logout();  
            success = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return success;  
    }  
    
    
    
    public static boolean downFtpFileName(String fileName,HttpServletResponse response) {  
        boolean success = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return success;  
            }  
            ftp.changeWorkingDirectory(Templatefilepath);// 转移到FTP服务器目录  
            ftp.enterLocalPassiveMode();
            FTPFile[] fs = ftp.listFiles();  
            for (FTPFile ff : fs) {  
                String fname = new String(ff.getName().getBytes("iso-8859-1"), ENCODING_GBK);  
                if (fname.equals(fileName)) {  
                 	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
             		response.reset();
             		response.setCharacterEncoding("GBK");
             		response.addHeader("Content-Disposition", "attachment;filename=" + new String( fileName.getBytes("GBK"), "ISO8859-1" )); 
             		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
             		response.setContentType("application/octet-stream");
             		byte[] buffer = new byte[1024];
                     int bytesToRead = -1;
                     // 通过循环将读入的Word文件的内容输出到浏览器中
                     while((bytesToRead = ins.read(buffer)) != -1) {
                     	toClient.write(buffer, 0, bytesToRead);
                      }
             		toClient.write(buffer);
             		ins.close();
             		toClient.flush();
                    toClient.close();
                }  
            }  
            ftp.logout();  
            success = true;  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return success;  
    }  
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFileName( String name,String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Templatefilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            ftp.enterLocalPassiveMode();
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(name)) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String((filename+name.substring(name.lastIndexOf("."))).getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    public static boolean downUploadFileNameall( String name,String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            ftp.enterLocalPassiveMode();
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(name)) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String((filename+name.substring(name.lastIndexOf("."))).getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**
     * 删除ftp文件
     * @param srcFname
     * @return
     * @throws Exception
     */
    public static boolean removeFile(String srcFname,String ftpPath) throws Exception{  
    	boolean flag = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return flag;  
           }  
            ftp.enterLocalPassiveMode();
	        if( ftp!=null ){  
	        	ftp.changeWorkingDirectory(ftpPath);//转移到指定FTP服务器目录
	        	srcFname = new String(srcFname.getBytes("GBK"), "iso-8859-1");
	            ftpPath = new String(ftpPath.getBytes("GBK"), "iso-8859-1");
	            flag = ftp.deleteFile(srcFname);  
	        }  

           
            if (ftp.isConnected()) {  
            	 ftp.logout();  
                 ftp.disconnect();  
            }  
          
        return flag;  
    }  

    /**
     * 删除ftp文件
     * @param srcFname
     * @return
     * @throws Exception
     */
    public static boolean removeFile(String srcFname) throws Exception{  
    	boolean flag = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return flag;  
           }  
            ftp.enterLocalPassiveMode();
	        if( ftp!=null ){  
	        	ftp.changeWorkingDirectory(Uploadfilepath);//转移到指定FTP服务器目录
	        	srcFname = new String(srcFname.getBytes("GBK"), "iso-8859-1");
	            flag = ftp.deleteFile(srcFname);  
	        }  
            if (ftp.isConnected()) {  
            	 ftp.logout();  
                 ftp.disconnect();  
            }  
        return flag;  
    }  
    
    public static List<String>  rederFile(String fileName) {
		List<String> lines = new ArrayList<String>();
		// 连接
		FTPClient ftpClient = new FTPClient();
		InputStream ins = null;
		try {
			ftpClient.setConnectTimeout(DEFAULT_CONNECT_TIMEOUT);
			ftpClient.connect(Ftpip);
			// 登录
			ftpClient.login(Username, Pssword);
			if (Uploadfilepath != null && Uploadfilepath.length() > 0) {
				// 跳转到指定目录
				ftpClient.changeWorkingDirectory(Uploadfilepath);
			}
			
			ftpClient.setControlEncoding(ENCODING_GBK); // 中文支持
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setControlEncoding("GBK");
			// 从服务器上读取指定的文件
			FTPFile[] fs = ftpClient.listFiles();  
            for (FTPFile ff : fs) {  
            	 String fname =ff.getName();
                if (fname.equals(fileName)) {  
                	ins=ftpClient.retrieveFileStream(fileName);
                	
                	int replyCode = ftpClient.getReplyCode();
                	String replyString = ftpClient.getReplyString();
                	String[] replyStrings = ftpClient.getReplyStrings();
                	
                	ins = ftpClient.retrieveFileStream(new String(fname.getBytes("UTF-8"), "ISO-8859-1"));
                	int replyCode1 = ftpClient.getReplyCode();
                	String replyString1 = ftpClient.getReplyString();
                	String[] replyStrings1 = ftpClient.getReplyStrings();
                }  
            }  
			
			//ins = ftpClient.retrieveFileStream(path+fileName);
			InputStreamReader read = new InputStreamReader(ins, ENCODING_UTF8);
			BufferedReader reader = new BufferedReader(read);
			String line;
			
			while ((line = reader.readLine()) != null) {
				//从FTP上读取文件，除去首字符 特殊字符 -,首字符不是数字和字母的视为特殊字符-
				if (!line.trim().equals("")) {
					char[] charArray = line.substring(0, 1).toCharArray();
					if (Character.isDigit(charArray[0])||Character.isLetter(charArray[0])) {
						lines.add(line);
					}else{
						lines.add(line.substring(1,line.length()));
					}
				}
				
			}
			reader.close();
			if (ins != null) {
				ins.close();
			}
			ftpClient.getReply();
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftpClient.isConnected()) {
				try {
					ftpClient.logout();
					ftpClient.disconnect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return lines;

	}
    
    
    public static boolean isFTPFileExist(String filePath){
        FTPClient ftp = new FTPClient();
        try {
            ftp.connect(Ftpip, Integer.parseInt(Port));
            // 登陆    
            ftp.login(Username, Pssword);
           // 检验登陆操作的返回码是否正确
            if(!FTPReply.isPositiveCompletion(ftp.getReplyCode())){
                ftp.disconnect();
                return false;
            }
            
            ftp.enterLocalActiveMode();
            // 设置文件类型为二进制，与ASCII有区别
            ftp.setFileType(FTP.BINARY_FILE_TYPE);
            // 设置编码格式
            ftp.setControlEncoding("GBK");
            
            // 提取绝对地址的目录以及文件名
            filePath = filePath.replace("ftp://"+Ftpip+":"+Port+"/", "");
            String dir = filePath.substring(0, filePath.lastIndexOf("/"));
            String file = filePath.substring(filePath.lastIndexOf("/")+1);
            
            // 进入文件所在目录，注意编码格式，以能够正确识别中文目录
            ftp.changeWorkingDirectory(new String(dir.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING));

            // 检验文件是否存在
            InputStream is = ftp.retrieveFileStream(new String(file.getBytes("GBK"),FTP.DEFAULT_CONTROL_ENCODING));
            if(is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE){
                return false;
            }
            if(is != null){
                is.close();
                ftp.completePendingCommand();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            if(ftp != null){
                try {
                    ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
}
    
    
    
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFile( String name,String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            ftp.enterLocalPassiveMode();
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(name)) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String((filename+name.substring(name.lastIndexOf("."))).getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadReportFile( String name,String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            ftp.enterLocalPassiveMode();
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(name)) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String((filename).getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**  
     * Description: 预览pdf
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean pdfPreview( String name,String filename,HttpServletResponse response) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            FTPFile[] fs = ftp.listFiles();  
            ftp.enterLocalPassiveMode();
            for (FTPFile ff : fs) {  
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(name)) {  
                	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
                    
                    try(
                    	OutputStream outputStream = response.getOutputStream();
                        PDDocument document = PDDocument.load(ins)) {
            			response.setCharacterEncoding("UTF-8");
            			String showName = StringUtils.isNotBlank(filename) ? filename : "aaaaa.pdf";
            			showName = URLEncoder.encode(showName, "UTF-8");
            			response.setHeader("Content-Disposition", "inline;fileName=" + showName + ";fileName*=UTF-8''" + showName);
            			// 从PDF流中获得PDF文档属性对象
            			PDDocumentInformation info = document.getDocumentInformation();
            			// 设置PDF文档属性对象的文件名称（最重要的环节）
            			info.setTitle(StringUtils.isNotBlank(filename) ? filename : "aaaaa.pdf");
            			document.setDocumentInformation(info);
            			// 修改完直接输出到响应体中
            			document.save(outputStream);
            			document.close();
            		} catch (Exception e) {
            			// log.error(this.getClass().getName() + ".viewPdfFile：", e);
            		}
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**  
     	* 系统上传的文件
     * Description: 向FTP服务器上传文件 
     * @param filename 上传到FTP服务器上的文件名  
     * @param input 文件输入流  
     * @return 成功返回true，否则返回false  
     */    
    public static boolean constractUploadFile( String filename, InputStream input) throws Exception {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            //切换到上传路径下  
            boolean change = ftp.changeWorkingDirectory(constractfilepath);
            //切换到上传目目录  
            if (false==change) {  
                //如果目录不存在创建目目录  
                String[] dirs = constractfilepath.split("/");  
                String tempPath = constractfilepath;  
                for (String dir : dirs) {  
                    if (null == dir || "".equals(dir)) continue;  
                    tempPath += "/" + dir;  
                    if (!ftp.changeWorkingDirectory(tempPath)) {  
                        if (!ftp.makeDirectory(tempPath)) {  
                            return result;  
                        } else {  
                            ftp.changeWorkingDirectory(tempPath);  
                        }  
                    }  
                }  
            }
            //设置上传文件的类型为二进制类型  
            ftp.setFileType(FTP.BINARY_FILE_TYPE);  
            ftp.enterLocalPassiveMode();
            boolean storeFile=true;  
            try{  
            	filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            	storeFile = ftp.storeFile(new String(filename), input);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
            if(false==storeFile){  
                return false;  
            }  
          
            input.close();  
            ftp.logout();  
            result = true;  
      
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
          
        return result;  
    }  
    
    
    /**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadFile(TblAttachment tblAttachment, HttpServletResponse response) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(Ftpip,Integer.parseInt(Port));
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(constractfilepath);// 转移到FTP服务器目录下
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
            	 String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                 if (fname.equals(tblAttachment.getAttpath())) {
                 	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));
             		response.reset();
             		response.setCharacterEncoding("GBK");
             		response.addHeader("Content-Disposition", "attachment;filename=" + new String(tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" ));
             		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
             		response.setContentType("application/octet-stream");
             		byte[] buffer = new byte[1024];
                     int bytesToRead = -1;
                     // 通过循环将读入的Word文件的内容输出到浏览器中
                     while((bytesToRead = ins.read(buffer)) != -1) {
                     	toClient.write(buffer, 0, bytesToRead);
                     }
             		toClient.write(buffer);
             		ins.close();
             		toClient.flush();
                    toClient.close();
             	}
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
    public static boolean downloadConstractFile(TblContractAppendixsigning signing, HttpServletResponse response) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(Ftpip,Integer.parseInt(Port));
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(constractfilepath);// 转移到FTP服务器目录下
            FTPFile[] fs = ftp.listFiles();
            for (FTPFile ff : fs) {
                String fname = new String(ff.getName().getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(signing.getSingingPath())) {
                    InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));
                    response.reset();
                    response.setCharacterEncoding("GBK");
                    response.addHeader("Content-Disposition", "attachment;filename=" + new String(signing.getSingingName().getBytes("GBK"), "ISO8859-1" ));
                    OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                   response.setContentType("application/octet-stream");
                    byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                        toClient.write(buffer, 0, bytesToRead);
                    }
                    toClient.write(buffer);
                    ins.close();
                    toClient.flush();
                    toClient.close();
                }
            }

            ftp.logout();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
	/**  
     * Description: 从FTP服务器下载文件 
     * @param remotePath FTP服务器上的相对路径 
     * @param fileName 要下载的文件名 
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFileTo( TblAttachment tblAttachment,HttpServletResponse response,HttpServletRequest request) {  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
      
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            String[] listNames = ftp.listNames();
            ftp.enterLocalPassiveMode();
            for (String ff : listNames) {  
            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
                if (fname.equals(tblAttachment.getAttpath())) {  
                	InputStream ins = ftp.retrieveFileStream(fname);  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            	//	OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		 //写入到文件（注意文件保存路径的后面一定要加上文件的名称） 
            	   
            	    String path=request.getSession().getServletContext().getRealPath("/");
            	    String savePath="/ueditor/jsp/";
            	    File file=new File(path+savePath+fname);
            	    if (!(new File(path+savePath).isDirectory())) {
						new File(path+savePath).mkdir();
					}
            	    FileOutputStream fileOut = new FileOutputStream(file); 
            	     BufferedOutputStream bos = new BufferedOutputStream(fileOut); 
            	//	response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    int length = ins.read(buffer); 
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	 bos.write(buffer, 0, length); 
                     }
                    result = true;  
            		ins.close();
            	    bos.close(); 
            	}else if(fname.equals(tblAttachment.getFileName())){
            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
            		response.reset();
            		response.setCharacterEncoding("GBK");
            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1" )); 
            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            		response.setContentType("application/octet-stream");
            		byte[] buffer = new byte[1024];
                    int bytesToRead = -1;
                    // 通过循环将读入的Word文件的内容输出到浏览器中
                    while((bytesToRead = ins.read(buffer)) != -1) {
                    	toClient.write(buffer, 0, bytesToRead);
                     }
                    result = true;  
            		toClient.write(buffer);
            		ins.close();
            		toClient.flush();
                    toClient.close();
            	}
            }  
  
            ftp.logout();  
            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    }  
    
    /**
                 * 获取ftp上文件的InputStream
     * @param ftpDirName ftp文件夹名称
     * @param ftpFileName ftp文件名称
     * @return
     */
    public static byte[] getInputStream(String ftpDirName, String ftpFileName) {
    	InputStream inputStream = null;
        try {
        	 FTPClient ftp = new FTPClient();  
        	 int reply;  
             ftp.connect(Ftpip,Integer.parseInt(Port));  
             // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
             ftp.login(Username, Pssword);// 登录  
             reply = ftp.getReplyCode();  
             if (!FTPReply.isPositiveCompletion(reply)) {  
                 ftp.disconnect();
                 return null;
             }  
             ftp.enterLocalPassiveMode();
            if ("".equals(ftpDirName)) {
                ftpDirName = "/";
            }
            String dir = new String(ftpDirName.getBytes("GBK"), "iso-8859-1");
            if (!ftp.changeWorkingDirectory(dir)) {
                System.out.println("切换目录失败：" + ftpDirName);
                return null;
            }
            // 一定要加上字符集指定，因为获取文件时有中文，会出现乱码而获取不到。
            String fileName = new String(ftpFileName.getBytes("GBK"), "iso-8859-1");
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            // 每次数据连接之前，ftp client告诉ftp server开通一个端口来传输数据，ftp server可能每次开启不同的端口来传输数据，
            // 但是在Linux上，由于安全限制，可能某些端口没有开启，所以就出现阻塞。
            ftp.enterLocalPassiveMode();
            inputStream = ftp.retrieveFileStream(fileName);
            byte[] bytes = IOUtils.toByteArray(inputStream);
           
            ftp.getReply();
            ftp.logout();
            return bytes;
        } catch (Exception e) {
            return null;
        }finally {
        	 try {
	        	 if (inputStream != null) {
					inputStream.close();
	             }
        	 } catch (IOException e) {
					e.printStackTrace();
			 }
        }
    }
    
    
    /**
     * 删除指定路径下ftp文件
     * @param srcFname
     * @return
     * @throws Exception
     */
    public static boolean removeFilePyton(String ftpPath) throws Exception{  
    	boolean flag = false;  
        FTPClient ftp = new FTPClient();  
      
            int reply;  
            ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口  
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return flag;  
           }  
            ftp.enterLocalPassiveMode();
	        if( ftp!=null ){  
	        	ftp.changeWorkingDirectory(ftpPath);//转移到指定FTP服务器目录
	        	String[] listNames = ftp.listNames();
	            ftp.enterLocalPassiveMode();
	             for (String ff : listNames) {  
	               	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
	            flag = ftp.deleteFile(fname);  
	        }  
	        }
            if (ftp.isConnected()) {  
            	 ftp.logout();  
                 ftp.disconnect();  
            }  
          
        return flag;  
    } 
    
    
    
    public static String hhqDemo(String path) throws Exception {
        String url = "https://ocr-api.ccint.com/cci_ai/service/v1.1/text_recognize_3d1";
        String appKey = "26f1b22a5b5a78bce694cb9303d54a4b"; // your app_key
        String appSecret = "REDACTED"; // your app_secret
        BufferedReader in = null;
        DataOutputStream out = null;
        String whole_text="";
        try {
           byte[] imgData = readfile(path); // image
          // String imgData = imageToBase64(path);
            URL realUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("app-key",appKey);
            conn.setRequestProperty("app-secret",appSecret);
            conn.setRequestProperty("lang_opt", "zho-s");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST"); // 设置请求方式
            out = new DataOutputStream(conn.getOutputStream());
            out.write(imgData);
            out.flush();
            out.close();
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            String result = "";
            while ((line = in.readLine()) != null) {
                result += line;
            }
            whole_text= JSONObject.parseObject(JSONObject.parseObject(result).getString("result")).getString("whole_text").replaceAll("\n", "<br/>");
        System.out.println(whole_text);
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
	     
       return whole_text;
    }
    
    public static byte[] readfile(String path)
    {
        String imgFile = path;
        InputStream in = null;
        byte[] data = null;
        try
        {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return data;
    }
    /**
     * @description 根据文件路径下载文件
     * @author   lyz
     * @date 2022/4/18 17:46
     */
    public static boolean downUploadFileNew(TblAttachment tblAttachment, HttpServletResponse response) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(Ftpip, Integer.parseInt(Port));
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean change = ftp.changeWorkingDirectory(Uploadfilepath);
            ftp.enterLocalPassiveMode();
            InputStream ins = ftp.retrieveFileStream(new String(tblAttachment.getAttpath().getBytes("GBK"), "iso-8859-1"));
            response.reset();
            response.setCharacterEncoding("GBK");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            byte[] buffer = new byte[409600000];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = ins.read(buffer)) != -1) {
                toClient.write(buffer, 0, bytesToRead);
            }
            result = true;
            //toClient.write(buffer);
            ins.close();
            toClient.flush();
            toClient.close();

            ftp.logout();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }

    public static void main(String[] args) {  
        
            //FileInputStream in=new FileInputStream(new File("C:\\Users\\thinkpad\\Desktop\\北师大\\行业风险数据库录入操作步骤.doc"));    
            try{
            	long timeInMillis = Calendar.getInstance().getTimeInMillis();
            	String ss="018018083aeaa62c1754c3291ed33916397ec27.xml";
            	 //1、加载配置文件
                Properties pro = new Properties();
                //使用ClassLoader加载配置文件，获取字节输入流
                InputStream is = FtpUtil.class.getClassLoader().getResourceAsStream("fileftp.properties");
                pro.load(is);

            	System.out.println("1234");
            	System.out.println(pro.getProperty("uploadfilepath"));
            }catch(Exception e){
                e.printStackTrace();
            }
    }


    
    /**
     * @description 根据文件路径下载文件
     * @author   lyz
     * @date 2022/4/18 17:46
     */
    public static boolean downUploadFileNewht(TblContractAppendixsigning sing, HttpServletResponse response) {
        boolean result = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            ftp.connect(Ftpip, Integer.parseInt(Port));
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return result;
            }
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
            boolean change = ftp.changeWorkingDirectory(constractfilepath);
            ftp.enterLocalPassiveMode();
            InputStream ins = ftp.retrieveFileStream(new String(sing.getSingingPath().getBytes("GBK"), "iso-8859-1"));
            response.reset();
            response.setCharacterEncoding("GBK");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(sing.getSingingName().getBytes("GBK"), "ISO8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            byte[] buffer = new byte[409600000];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = ins.read(buffer)) != -1) {
                toClient.write(buffer, 0, bytesToRead);
            }
            result = true;
            //toClient.write(buffer);
            ins.close();
            toClient.flush();
            toClient.close();

            ftp.logout();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return result;
    }
    
    /**
     * @description 根据文件路径下载文件
     * @author   lyz
     * @date 2022/4/18 17:46
     */
    public static boolean downUploadContentContentPdf(String pdfPath ,String pdfName, HttpServletResponse response) {
    	  
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return result;  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(constractfilepath);// 转移到FTP服务器目录下  
            //String[] listNames = ftp.listNames();
            ftp.enterLocalPassiveMode();
            
            InputStream ins = ftp.retrieveFileStream(new String(pdfPath.getBytes("GBK"), "iso-8859-1"));
            response.reset();
            response.setCharacterEncoding("GBK");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes("GBK"), "ISO8859-1"));
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            byte[] buffer = new byte[409600000];
            int bytesToRead = -1;
            // 通过循环将读入的Word文件的内容输出到浏览器中
            while ((bytesToRead = ins.read(buffer)) != -1) {
                toClient.write(buffer, 0, bytesToRead);
            }
            result = true;
            //toClient.write(buffer);
            ins.close();
            toClient.flush();
            toClient.close();

            ftp.logout();
//            for (String ff : listNames) {  
//            	 String fname = new String(ff.getBytes("iso-8859-1"),  ENCODING_GBK);
//                if (fname.equals(pdfPath)) {  
//                	InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
//            		response.reset();
//            		response.setCharacterEncoding("GBK");
//            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( pdfName.getBytes("GBK"), "ISO8859-1" )); 
//            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//            		response.setContentType("application/octet-stream");
//            		byte[] buffer = new byte[1024];
//                    int bytesToRead = -1;
//                    // 通过循环将读入的Word文件的内容输出到浏览器中
//                    while((bytesToRead = ins.read(buffer)) != -1) {
//                    	toClient.write(buffer, 0, bytesToRead);
//                    	
//                     }
//                    System.out.println(bytesToRead);
//                    System.out.println(fname);
//                    result = true;  
//            		toClient.write(buffer);
//            		ins.close();
//            		toClient.flush();
//                    toClient.close();
//                    break;
//            	}else if(fname.equals(pdfPath)){
//            		InputStream ins = ftp.retrieveFileStream(new String(ff.getBytes("GBK"), "iso-8859-1" ));  
//            		response.reset();
//            		response.setCharacterEncoding("GBK");
//            		response.addHeader("Content-Disposition", "attachment;filename=" + new String( pdfName.getBytes("GBK"), "ISO8859-1" )); 
//            		OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
//            		response.setContentType("application/octet-stream");
//            		byte[] buffer = new byte[1024];
//                    int bytesToRead = -1;
//                    // 通过循环将读入的Word文件的内容输出到浏览器中
//                    while((bytesToRead = ins.read(buffer)) != -1) {
//                    	toClient.write(buffer, 0, bytesToRead);
//                     }
//                    result = true;  
//            		toClient.write(buffer);
//            		ins.close();
//            		toClient.flush();
//                    toClient.close();
//                    break;
//            	}
//            }  
//  
//            ftp.logout();  
//            
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return result;  
    
        
    }
    
    
    
    /**
     * 获取ftp上文件的InputStream
     * @param ftpDirName ftp文件夹名称
     * @param ftpFileName ftp文件名称
     * @return
     */
    public static byte[] getInputStreamByOa(String ftpDirName, String ftpFileName) {
    	byte[] buffer=new byte[1024];
        FTPClient ftp = new FTPClient();  
        try {  
            int reply;  
            ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
                return buffer;
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            ftp.enterLocalPassiveMode();
    		InputStream ins = ftp.retrieveFileStream(new String(ftpDirName.getBytes("GBK"), "iso-8859-1" ));  
    		buffer = new byte[1024];
    		buffer= IOUtils.toByteArray(ins);
    		ins.close();
            ftp.logout();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (ftp.isConnected()) {  
                try {  
                    ftp.disconnect();  
                } catch (IOException ioe) {  
                }  
            }  
        }  
        return buffer;  
    }

    public static boolean isFTPFileExistByFilePath(String filePath){
        return isFTPFileExist(Uploadfilepath+filePath);
    }
    
    /**
     * @description 流程审批上传文件
     * @author   lyz
     * @date 2022/4/18 20:12
     */
     public static String uploadFlowFilePath(String filename, InputStream input) throws Exception {
         String  tempPath = Xmlfilepath;
         FTPClient ftp = new FTPClient();

         int reply;
         System.out.println(Ftpip);
         ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器
         // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口
         ftp.login(Username, Pssword);// 登录
         reply = ftp.getReplyCode();
         if (!FTPReply.isPositiveCompletion(reply)) {
             ftp.disconnect();
             return "";
         }
         
         String dataFile = DateUtil.parseDate(new Date(), "yyyyMM");
         tempPath += dataFile;
         //切换到上传路径下
         boolean change = ftp.changeWorkingDirectory(tempPath);
         //切换到上传目目录
         if (false==change) {
             //如果目录不存在创建目目录
             String[] dirs = tempPath.split("/");
             String trmpUrl = "";
             for (String dir : dirs) {
                 if (null == dir || "".equals(dir)) continue;
                 trmpUrl += "/" + dir;
                 if (!ftp.changeWorkingDirectory(trmpUrl)) {
                     if (!ftp.makeDirectory(trmpUrl)) {
                    	 System.out.println("文件夹"+dir+"已存在");
                         continue;
                     } else {
                    	 System.out.println("文件夹"+dir+"创建成功");
                    	 continue;
                     }
                 }
             }
             ftp.changeWorkingDirectory(tempPath);
         }
         //设置上传文件的类型为二进制类型
         ftp.setFileType(FTP.BINARY_FILE_TYPE);
         ftp.enterLocalPassiveMode();
         boolean storeFile=true;
         try{
             filename=new String(filename.getBytes("GBK"),"iso-8859-1");
             storeFile = ftp.storeFile(new String(filename), input);
         }catch(Exception e){
             e.printStackTrace();
         }
         if(false==storeFile){
             return "";
         }

         input.close();
         ftp.logout();

         if (ftp.isConnected()) {
             try {
                 ftp.disconnect();
             } catch (IOException ioe) {
             }
         }

         return tempPath+"/"+filename;
     }

     /**
      * @description 流程附件下载
      */
     public static boolean downUploadFlowFile(TblAttachment tblAttachment, HttpServletResponse response) {
         boolean result = false;
         FTPClient ftp = new FTPClient();
         try {
             int reply;
             ftp.connect(Ftpip, Integer.parseInt(Port));
             // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
             ftp.login(Username, Pssword);// 登录
             reply = ftp.getReplyCode();
             if (!FTPReply.isPositiveCompletion(reply)) {
                 ftp.disconnect();
                 return result;
             }
             ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
             String path  = tblAttachment.getAttpath();
             String fileName = path.substring(path.lastIndexOf("/")+1,path.length());
     		String filePath = path.substring(0,path.lastIndexOf("/")+1);
     		
             boolean change = ftp.changeWorkingDirectory(filePath);
             ftp.enterLocalPassiveMode();
             InputStream ins = ftp.retrieveFileStream(new String(fileName.getBytes("GBK"), "iso-8859-1"));
             response.reset();
             response.setCharacterEncoding("GBK");
             response.addHeader("Content-Disposition", "attachment;filename=" + new String(tblAttachment.getAttname().getBytes("GBK"), "ISO8859-1"));
             OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
             response.setContentType("application/octet-stream");
             byte[] buffer = new byte[409600000];
             int bytesToRead = -1;
             // 通过循环将读入的Word文件的内容输出到浏览器中
             while ((bytesToRead = ins.read(buffer)) != -1) {
                 toClient.write(buffer, 0, bytesToRead);
             }
             result = true;
             //toClient.write(buffer);
             ins.close();
             toClient.flush();
             toClient.close();

             ftp.logout();

         } catch (IOException e) {
             e.printStackTrace();
         } finally {
             if (ftp.isConnected()) {
                 try {
                     ftp.disconnect();
                 } catch (IOException ioe) {
                 }
             }
         }
         return result;
     }

    public static void ftpUploadFile(String path,String filename, InputStream input) throws Exception {
        FTPClient ftp = new FTPClient();

        int reply;
        ftp.connect(Ftpip, Integer.parseInt(Port));// 连接FTP服务器
        // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口
        ftp.login(Username, Pssword);// 登录
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            return ;
        }
        //切换到上传路径下
        boolean change = ftp.changeWorkingDirectory(path);
        //切换到上传目目录
        if (false==change) {
            //如果目录不存在创建目目录
            String[] dirs = path.split("/");
            StringBuffer absoluteDir = new StringBuffer();
            for (String dir : dirs) {
                if (null == dir || "".equals(dir)) continue;
                absoluteDir.append("/");
                absoluteDir.append(dir);
                if (!ftp.changeWorkingDirectory(absoluteDir.toString())) {
                    if (!ftp.makeDirectory(absoluteDir.toString())) {
                        return ;
                    } else {
                        ftp.changeWorkingDirectory(absoluteDir.toString());
                    }
                }
            }
        }
        //设置上传文件的类型为二进制类型
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        ftp.enterLocalPassiveMode();
        boolean storeFile=true;
        try{
            filename=new String(filename.getBytes("GBK"),"iso-8859-1");
            storeFile = ftp.storeFile(new String(filename), input);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(false==storeFile){
            return ;
        }

        input.close();
        ftp.logout();

        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException ioe) {
            }
        }
    }
	public static String getFtpInfo(String url, Integer port2, String userName2, String userp) throws Exception {
		FTPClient ftp = new FTPClient();
		String result = "";
		try {
			ftp.connect(url, port2);// 连接FTP服务器
			  // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务端口
			  boolean success = ftp.login(userName2, userp);// 登录
			  if (success) {
				  result = "FTP连接成功！";
			      // 这里可以执行其他FTP操作
			  } else {
				  result = "FTP登录失败！";
			  }
		} finally {
            ftp.logout(); // 登出FTP服务器
            ftp.disconnect(); // 关闭FTP连接
		}
		
		
		
	      return result;
	}
	
	//根据附件导出到zip包
    public static void downloadFilesAsZip(List<TblAttachment> atts,HttpServletResponse response) throws IOException {
        // 1. 创建字节数组输出流，用于存储 ZIP 文件内容
    	OutputStream fos = new BufferedOutputStream(response.getOutputStream());
        boolean result = false;  
        FTPClient ftp = new FTPClient();  
        try (ZipOutputStream zos = new ZipOutputStream(fos)) {
        	int reply;  
        	   try {
        	ftp.connect(Ftpip,Integer.parseInt(Port));  
            // 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务
            ftp.login(Username, Pssword);// 登录  
            reply = ftp.getReplyCode();  
            if (!FTPReply.isPositiveCompletion(reply)) {  
                ftp.disconnect();  
            }  
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE); 
            ftp.enterLocalPassiveMode();
            ftp.changeWorkingDirectory(Uploadfilepath);// 转移到FTP服务器目录下  
            for (TblAttachment tbl : atts) {
                String fileName = tbl.getFileName();
                
                // 5. 从 FTP 获取文件流
                try (InputStream inputStream = ftp.retrieveFileStream(new String(tbl.getAttpath().getBytes("GBK"), "iso-8859-1"))) {
                    if (inputStream == null) {
                        System.err.println("Failed to retrieve file: " + tbl.getAttpath());
                        continue;
                    }
                    ZipEntry zipEntry = new ZipEntry(fileName);
                    zos.putNextEntry(zipEntry);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) > 0) {
                        zos.write(buffer, 0, length);
                    }
                    zos.closeEntry();
                    
                    // 7. 必须调用 completePendingCommand 完成文件传输
                    if (!ftp.completePendingCommand()) {
                        System.err.println("Failed to complete pending command for: " + tbl.getAttpath());
                    }
                }
            }
        } finally {
            if (ftp.isConnected()) {
                try {
                	ftp.logout();
                	ftp.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        zos.finish();
        }
//        byte[] zipBytes = baos.toByteArray();
//        ByteArrayInputStream bais = new ByteArrayInputStream(zipBytes);
//        return ResponseEntity.ok()
//                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloaded_files.zip\"")
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .contentLength(zipBytes.length)
//                .body(new InputStreamResource(bais));
    }
}
