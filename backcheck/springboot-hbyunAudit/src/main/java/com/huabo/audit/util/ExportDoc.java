package com.huabo.audit.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ExportDoc{

	private Configuration configuration = null;
	
	public ExportDoc() {
		configuration = new Configuration();
		configuration.setDefaultEncoding("UTF-8");
	}
	
	// 2个参数分别是：模板的名称，导出文件的路径
	public void createDoc(String modelName, String exportFilePath,Map<String, Object> dataMap) {
		Template t = null;
		// 1、导入模板
		configuration.setClassForTemplateLoading(this.getClass(), "/com/hbfk/fxgl/template");
		try {
			// test.ftl为要装载的模板
			t = configuration.getTemplate(modelName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 2、打包数据--每次导出的word文件模板不同，打包数据的方法要单独写
//		Map<String, Object> dataMap = new HashMap<String, Object>();
//		getData(dataMap);
		// 3、导出文件
		// 输出文档路径及名称
		File outFile = new File(exportFilePath);
		Writer out = null;
		try {
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		try {
			t.process(dataMap, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		ExportDoc dh = new ExportDoc();
		String filePath = "F:/pgjg.doc";//导出doc文件的路径
		String modelName = "pgjg.xml";//模板名称
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("assessname","sdg");//项目名称
		dataMap.put("assessid","pj20180529");//评价编号
		dataMap.put("orgname","长江投资（中国）有限公司");//评价机构
		dataMap.put("templename","模板测试");//评价模板
		dataMap.put("startdate","2018-05-29");//评价期限起始日期
		dataMap.put("enddate"," 2018-05-31");//评价期限结束日期
		dataMap.put("asssponsor","系统管理员");//评价发起人
		dh.createDoc(modelName, filePath,dataMap);
//		System.out.println("	导出成功");
	}
}
