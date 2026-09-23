package com.huabo.audit.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;

public class FileUtil 
{
	public static Map<String, Integer> FILE_TYPE_MAP = new HashMap();
	  static
	  {
	    FILE_TYPE_MAP.put("doc", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("docx", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("xls", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("xlsx", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("ppt", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("pptx", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("txt", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("pdf", Integer.valueOf(1));
	    FILE_TYPE_MAP.put("tif", Integer.valueOf(1));

	    FILE_TYPE_MAP.put("jpg", Integer.valueOf(2));
	    FILE_TYPE_MAP.put("png", Integer.valueOf(2));
	    FILE_TYPE_MAP.put("gif", Integer.valueOf(2));
	    FILE_TYPE_MAP.put("jpeg", Integer.valueOf(2));
	    FILE_TYPE_MAP.put("bmp", Integer.valueOf(2));

	    FILE_TYPE_MAP.put("avi", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("flv", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("mp4", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("wmv", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("rm", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("rmvb", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("mpg", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("mpeg", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("asx", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("asf", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("mp3", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("wma", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("wav", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("aif", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("rm", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("cd", Integer.valueOf(3));
	    FILE_TYPE_MAP.put("mid", Integer.valueOf(3));

	    FILE_TYPE_MAP.put("html", Integer.valueOf(6));
	    FILE_TYPE_MAP.put("htm", Integer.valueOf(6));
	  }
    /**
     * 创建新文件并且保存
     * 
     * @param src
     * @return
     */
    public static int createNewFile(File src, String newFileName, String uploadPath)
    {
        File dir = new File(uploadPath);
        if (!dir.exists())
        {
            dir.mkdir();
        }
        File dst = new File(dir, newFileName);
        InputStream is = null;
        OutputStream os = null;
        int fileSize = 0;
        try
        {
            is = new BufferedInputStream(new FileInputStream(src));
            os = new BufferedOutputStream(new FileOutputStream(dst));
            fileSize = is.available();
            byte buffer[] = new byte[8192];
            int len = 0;
            while ((len = is.read(buffer)) != -1)
            {
                os.write(buffer, 0, len);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                if (is != null)
                {
                    is.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                if (os != null)
                {
                    os.close();
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return fileSize;
    }

    /**
     * 获取文件扩张名
     * @param fileName
     * @return
     */
    public static String getFileExt(String fileName)
    {
        if (fileName == null)
        {
            return "";
        }
        else
        {
            int dotindex = fileName.lastIndexOf(".");
            String extName = fileName.substring(dotindex + 1, fileName.length());
            extName = extName.toLowerCase();
            return extName;
        }
    }

    /**
     * 获取文件名称(不包含扩展名)
     * @param fileName
     * @return
     */
    public static String getFileNameWithoutExt(String fileName)
    {
        int dotindex = fileName.lastIndexOf(".");
        String fName = fileName.substring(0, dotindex);
        fName = fName.toLowerCase();
        return fName;
    }

    public static boolean CopyFile(File file,String filePathSrc,String filePathDes)
    {
        boolean re = false;        
        File fSrc = new File(filePathSrc);
        if (!fSrc.exists())
            return false;
        try
        {
            if (file.isFile())
            {
                FileInputStream input = new FileInputStream(file);
                FileOutputStream output = new FileOutputStream(filePathDes);
                byte b[] = new byte[5120];
                int len;
                while ((len = input.read(b)) != -1)
                    output.write(b, 0, len);
                output.flush();
                output.close();
                input.close();
                re = true;
            }
            else
            {
                System.out.print("Error:" + filePathSrc + " is not found\uFF01");
            }
        }
        catch (IOException e)
        {
        	e.printStackTrace();
            System.out.print(e.getMessage());
        }
        return re;
    }
    
    /**
     * 拷贝文件
     * @param filePathSrc
     * @param filePathDes
     * @return
     */
    public static boolean CopyFile(String filePathSrc, String filePathDes)
    {
        boolean re = false;
        File fSrc = new File(filePathSrc);
        if (!fSrc.exists())
            return false;
        try
        {
            if (fSrc.isFile())
            {
                FileInputStream input = new FileInputStream(fSrc);
                FileOutputStream output = new FileOutputStream(filePathDes);
                byte b[] = new byte[5120];
                int len;
                while ((len = input.read(b)) != -1)
                    output.write(b, 0, len);
                output.flush();
                output.close();
                input.close();
                re = true;
            }
            else
            {
                System.out.print("Error:" + filePathSrc + " is not found\uFF01");
            }
        }
        catch (IOException e)
        {
            System.out.print(e.getMessage());
        }
        return re;
    }

    /**
     * 删除物理文件
     * 
     * @param filePath
     */
    public static void deleteFile(String filePath)
    {
        File file = new File(filePath);
        if (file.exists())
        {
            file.delete();
        }
    }

    /**
     * 删除文件夹下文件
     * @param filepath
     * @throws IOException
     */
    public static void del(String filepath) throws IOException
    {
        File f = new File(filepath);
        if (f.exists())
        {
            if (f.isDirectory())
            {
                if (f.listFiles().length == 0)
                {
                    f.delete();
                }
                else
                {
                    File delFile[] = f.listFiles();
                    int i = f.listFiles().length;
                    for (int j = 0; j < i; j++)
                    {
                        if (delFile[j].isDirectory())
                            del(delFile[j].getAbsolutePath());
                        delFile[j].delete();
                    }

                }
                del(filepath);
            }
        }
        else
        {
            f.delete();
        }
    }

    /**
     * 获取附件原名陈
     * 
     * @param fileName
     * @return
     */
    public static String getFileName(String fileName)
    {
        if (fileName.indexOf(".") >= 0)
        {
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
        }
        return fileName;
    }

    public static void downLoad(String filePath, HttpServletResponse response, boolean isOnLine,HttpServletRequest request) throws Exception
    {
        File f = new File(filePath);
        if (!f.exists())
        {
            response.sendError(404, "File not found!");
            return;
        }
        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); //非常重要
        if (isOnLine)
        { //在线打开方式
            URL u = new URL("file:///" + filePath);
            response.setContentType(u.openConnection().getContentType());
            
            response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
            //文件名应该编码成UTF-8
            response.setCharacterEncoding("UTF-8");
        }
        else
        { //纯下载方式
        	String fileName = f.getName();
        	String agent = (String) request.getHeader("USER-AGENT");
            if (agent != null && agent.indexOf("MSIE") == -1)
            {
                fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";// FF
            }
            else
            {
                fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1"); // IE
            }
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            response.setCharacterEncoding("UTF-8");
        }
        OutputStream out = null;
        try
        {
            out = response.getOutputStream();
            while ((len = br.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        finally
        {
            if (br != null)
            {
                try
                {
                    br.close();
                    out.flush();
                    out.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
    }
    
    public static void downLoad(String filePath, HttpServletResponse response, boolean isOnLine,String fileName)
    {
        try
        {
            File f = new File(filePath);
            if (!f.exists())
            {
                response.sendError(404, "File not found!");
                return;
            }
            BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
            byte[] buf = new byte[1024];
            int len = 0;

            response.reset(); //非常重要
            if (isOnLine)
            { //在线打开方式
                URL u = new URL("file:///" + filePath);
                response.setContentType(u.openConnection().getContentType());
                response.setHeader("Content-Disposition", "inline; filename=" + fileName);
                //文件名应该编码成UTF-8
                response.setCharacterEncoding("UTF-8");
            }
            else
            { //纯下载方式
            	// 定义输出类型
    			//response.setContentType("application/vnd.ms-excel");
    			fileName = URLEncoder.encode(fileName, "UTF-8");
    			if (fileName.length() > 150) {
    				/*
    				 * 根据request的locale
    				 * 得出可能的编码，中文操作系统通常是gb2312
    				 */
    				String guessCharset = "gb2312"; 
    				fileName = new String(fileName.getBytes(guessCharset),"ISO8859-1");
    			}
    			response.setContentType("application/octet-stream");
    			response.setHeader("Content-Disposition", "attachment; filename="+ fileName);
            }
            OutputStream out = null;
            try
            {
                out = response.getOutputStream();
                while ((len = br.read(buf)) != -1)
                {
                    out.write(buf, 0, len);
                }
            }
            catch (IOException e)
            {

            }

            finally
            {
                if (br != null)
                {
                    try
                    {
                        br.close();
                        out.flush();
                        out.close();
                    }
                    catch (IOException e)
                    {
                    }
                }

            }
        }
        catch (IOException e)
        {
        }
    }

    /**
     * 创建新名称
     * 
     * @param seq
     * @param fileNames
     * @return
     */
    @SuppressWarnings("static-access")
    public static String createNewFileName(Long seq, String fileNames)
    {
        Calendar now = Calendar.getInstance();
        String newFileName = String.valueOf(now.get(now.YEAR)) + String.valueOf(now.get(now.MONTH) + 1) + String.valueOf(now.get(now.DATE));
        newFileName += String.valueOf(now.get(now.HOUR)) + String.valueOf(now.get(now.MINUTE)) + String.valueOf(now.get(now.SECOND));
        newFileName = newFileName + "-" + seq + "-" + fileNames;
        return newFileName;
    }

}
