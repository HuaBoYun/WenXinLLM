package com.huabo.audit.util;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.hbfk.util.DateUtil;


public class FileUtils {

	private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
	
	public static String openNewFile(String path,String fileName){
		logger.debug("HBFK----------------------->打开一个新文件:");
		
		path=path+DateUtil.getCurrentDate()+"/";
		try {
			File dic=new File(path);
			if(!dic.exists()||!dic.isDirectory()){
				dic.mkdirs();
			}
			File targetFile = new File(path, fileName);
			if (!targetFile.exists()) {
				targetFile.createNewFile();
				logger.debug("HBFK----------------------->打开新文件成功:filePath"+path+"-------------->fileName:"+fileName);
			}
			return path;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.debug("HBFK----------------------->打开新文件失败:");
		}
		return null;
	}
	
	public static String fileUpload(MultipartFile file,String path,String fileName){
		logger.info("HBFK----------------->开始上传文件:"+fileName+",文件路径为:"+path);
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day=calendar.get(Calendar.DAY_OF_MONTH);
		String fileUrl ="fileupload/"+ year+"/"+month+"/"+day+"/";
		path=path+"/"+fileUrl;
		File dic=new File(path);
		String fileType = fileName.substring(fileName.indexOf("."), fileName.length());
		String name =  UUID.randomUUID().toString()+fileType;
		if(!dic.exists()||!dic.isDirectory()){
			dic.mkdirs();
		}
		try {
			File targetFile = new File(path,name);
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			file.transferTo(targetFile);
			logger.info("HBFK----------------->文件上传成功");
			return fileUrl+name;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			logger.info("HBFK----------------->上传失败,原因是因为:"+e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("HBFK----------------->上传失败,原因是因为:"+e.getMessage());
		}
		return null;
	}
}
