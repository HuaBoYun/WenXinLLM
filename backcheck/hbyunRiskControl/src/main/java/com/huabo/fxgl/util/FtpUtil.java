package com.huabo.fxgl.util;

import com.huabo.fxgl.entity.Attachment;
import com.huabo.fxgl.entity.ContractAppendixsigning;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

/** 
 *  
 * @author tjj
 * ftp文件
 */
@Slf4j
public  class FtpUtil {  
  
	
	private static final int DEFAULT_CONNECT_TIMEOUT = 0;
	private static final String ENCODING_UTF8="UTF-8";
	private static final String ENCODING_GBK="GBK";
	
	
	public static ResourceBundle rb = ResourceBundle.getBundle("setting/fileftp");
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
	
	//FTP服务器主机地址 
	private static String Ftpip =(String) rb.getObject("ftpip");
	//FTP登录账号/linux用户 名 
	private static String Username =(String) rb.getObject("username");
	//FTP登录密码 /linux密码 
	private static String Pssword =(String) rb.getObject("password");
	
	// FTP服务器端号 
	private static String Port =(String) rb.getObject("port");
	
	// FTP服务器端号 
		private static String pythonFlfgPath  =(String) rb.getObject("pythonFlfgPath");
	
	
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
			IOUtils.closeQuietly(fis);
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
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFile(Attachment tblAttachment, HttpServletResponse response) {
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
            		response.addHeader("Content-Length", ""+tblAttachment.getAttsize());
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
     * Description: 从FTP服务器下载python爬虫文件
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFilePython( Attachment tblAttachment,HttpServletResponse response) {
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
            		response.addHeader("Content-Length", ""+tblAttachment.getAttsize());
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
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadSendFile(Attachment tblAttachment,HttpServletResponse response) {
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
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadTemplateFile( Attachment tblAttachment,HttpServletResponse response) {
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
            		response.addHeader("Content-Length", ""+filename+name.substring(name.lastIndexOf(".")));
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
            		response.addHeader("Content-Length", ""+filename+name.substring(name.lastIndexOf(".")));
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
        log.info("----------------------------------filePath: " + filePath);
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
            		response.addHeader("Content-Length", ""+filename+name.substring(name.lastIndexOf(".")));
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
            		response.addHeader("Content-Length", ""+filename);
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
                    
                    try(OutputStream outputStream = response.getOutputStream();
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
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downloadConstractFile(ContractAppendixsigning signing, HttpServletResponse response) {
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
                 if (fname.equals(signing.getSingingpath())) {
                 	InputStream ins = ftp.retrieveFileStream(new String(ff.getName().getBytes("GBK"), "iso-8859-1" ));  
             		response.reset();
             		response.setCharacterEncoding("GBK");
             		response.addHeader("Content-Disposition", "attachment;filename=" + new String(signing.getSingingname().getBytes("GBK"), "ISO8859-1" ));
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
     * @param response 下载后保存到本地的路径 
     * @return  
     */    
    public static boolean downUploadFileTo( Attachment tblAttachment,HttpServletResponse response,HttpServletRequest request) {
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
            		response.addHeader("Content-Length", ""+tblAttachment.getAttsize());
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
    public static void main(String[] args) {  
        
            //FileInputStream in=new FileInputStream(new File("C:\\Users\\thinkpad\\Desktop\\北师大\\行业风险数据库录入操作步骤.doc"));    
            try{  
            	long timeInMillis = Calendar.getInstance().getTimeInMillis();
            	String ss="018018083aeaa62c1754c3291ed33916397ec27.xml";
            	System.out.println(timeInMillis);  
            }catch(Exception e){  
                e.printStackTrace();  
            }  
                
        
    }  
}