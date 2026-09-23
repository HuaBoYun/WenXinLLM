package com.huabo.fxgl.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;


public class UploadFileUtil {
	  
	  
	
		
		private static ResourceBundle rb = ResourceBundle.getBundle("setting/fileftp");
		/**
		 * 流程图相关的xml文件
		 */
		private static String Xmlfilepath =(String) rb.getObject("xmlfilepath");
		/**
		 * 系统上传的文件
		 */
		public static String Uploadfilepath =(String) rb.getObject("uploadfilepath");
		//FTP服务器主机地址 
		private static String Ftpip =(String) rb.getObject("ftpip");
		//FTP登录账号/linux用户 名 
		private static String Username =(String) rb.getObject("username");
		//FTP登录密码 /linux密码 
		private static String Pssword =(String) rb.getObject("password");
		
		// FTP服务器端号 
		private static String Port =(String) rb.getObject("port");
		
		/**  
	     * Description: 向FTP服务器上传文件 
	     * @param filePath 文件夹路径 
	     * @param filename 上传到FTP服务器上的文件名  
	     * @param input 文件输入流  
	     * @return 成功返回true，否则返回false  
	     */    
	    public static boolean uploadfile(  String filename, InputStream input) throws Exception {  
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
	     * 获取路径下的所有文件/上传到ftp
	     * @param directoryPath 需要遍历的文件夹路径
	     * @return
	     */
	    public static void getAllFile(String directoryPath) throws Exception {
	        File baseFile = new File(directoryPath);
	        File[] files = baseFile.listFiles();
	        for (File file : files) {
	        	System.out.println(file.getAbsolutePath());
	        	System.out.println(file.getName());
	            	InputStream in=new FileInputStream(file) ; 
	            	boolean fileExist = isFTPFileExist(Xmlfilepath+file.getName());
	            	if(fileExist){
	            		boolean removeFile = removeFile(file.getName(), Xmlfilepath);
	            		if(removeFile){
	            			boolean xmlfile = xmlfile(file.getName(), in);
	            			if(xmlfile){
		            			System.out.println("上传成功！");
		            		}else{
		            			System.out.println("上传失败！");
		            		}
	            		}else{
	            			System.out.println("上传失败！");
	            		}
	            	}else{
	            		boolean xmlfile = xmlfile(file.getName(), in);
	            		if(xmlfile){
	            			System.out.println("上传成功！");
	            		}else{
	            			System.out.println("上传失败！");
	            		}
	            } 
	        }
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
	    
	    
	    
	    
	    private static boolean isFTPFileExist(String filePath){
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

	    
	    
	   
	    public static void main(String[] args) {  
	        try {    
	            try{  
	            	getAllFile("C:\\Users\\thinkpad\\Desktop\\北师大\\");
	            }catch(Exception e){  
	                e.printStackTrace();  
	            }  
	                
	        } catch (Exception e) {    
	            e.printStackTrace();    
	        }    
	    }  

}
