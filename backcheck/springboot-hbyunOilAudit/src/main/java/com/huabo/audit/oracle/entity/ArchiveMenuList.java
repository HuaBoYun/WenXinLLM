package com.huabo.audit.oracle.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchiveMenuList {
	private String id;

	private String name;
	
	private String url;
	
	private String type;
	
	private final static String TYPE_TO1="-1";
	private final static String TYPE_NUM1="1";
	private final static String TYPE_NUM2="2";
	
	
	private static List<ArchiveMenuList> getArchiveMenuList=new ArrayList<ArchiveMenuList>();
	
	private static Map<String, ArchiveMenuList> getArchiveMenuMap = new HashMap<String, ArchiveMenuList>();

	
	 static  {
		ArchiveMenuList objArchiveMenuList1=new ArchiveMenuList("001","审计指引","/nbsj/sjgl/project_look", ArchiveMenuList.TYPE_NUM1);      
		ArchiveMenuList objArchiveMenuList2=new ArchiveMenuList("002","项目方案","/nbsj/sjzb/project_proposal_edit", ArchiveMenuList.TYPE_NUM1);   
		ArchiveMenuList objArchiveMenuList3=new ArchiveMenuList("003","审计通知书","/nbsj/sjzb/notice_list",  ArchiveMenuList.TYPE_NUM1);    
		//ArchiveMenuList objArchiveMenuList4=new ArchiveMenuList("004","进场纪要","/nbsj/sjss/in_meet_record_list", ArchiveMenuList.TYPE_NUM1);     
		ArchiveMenuList objArchiveMenuList5=new ArchiveMenuList("004","任务查看","#", ArchiveMenuList.TYPE_NUM1);   
		ArchiveMenuList objArchiveMenuList8=new ArchiveMenuList("005","审计取证单","/nbsj/sjss/confirmation_list", ArchiveMenuList.TYPE_NUM1);     
		ArchiveMenuList objArchiveMenuList6=new ArchiveMenuList("006","工作底稿","/nbsj/sjss/project_standard_listall", ArchiveMenuList.TYPE_NUM1);     
		ArchiveMenuList objArchiveMenuList7=new ArchiveMenuList("007","审计发现","/nbsj/xmzl/question_store_list",ArchiveMenuList.TYPE_NUM1);    
		//ArchiveMenuList objArchiveMenuList9=new ArchiveMenuList("009","离场纪要","/nbsj/sjss/out_meet_record_list", ArchiveMenuList.TYPE_NUM1);     
		ArchiveMenuList objArchiveMenuList10=new ArchiveMenuList("008","审计报告","/nbsj/sjzj/audit_report_list", ArchiveMenuList.TYPE_NUM1);    
		//ArchiveMenuList objArchiveMenuList11=new ArchiveMenuList("0011","审计建议书","/nbsj/sjzj/audit_suggest_list",  ArchiveMenuList.TYPE_NUM1);    
		/*ArchiveMenuList objArchiveMenuList12=new ArchiveMenuList("0012","整改分派","/nbsj/sjzg/question_allocate_list", ArchiveMenuList.TYPE_NUM1);    
		ArchiveMenuList objArchiveMenuList13=new ArchiveMenuList("0013","整改跟踪","/nbsj/sjzg/index",  ArchiveMenuList.TYPE_NUM1);     
		ArchiveMenuList objArchiveMenuList14=new ArchiveMenuList("0014","整改落实","/nbsj/sjzg/index_question_rectify_impl", ArchiveMenuList.TYPE_NUM1);    
		ArchiveMenuList objArchiveMenuList15=new ArchiveMenuList("0015","整改查询","/nbsj/sjzg/question_rectify_distory_list",ArchiveMenuList.TYPE_NUM1);    */
		getArchiveMenuList.add(objArchiveMenuList1);
		getArchiveMenuList.add(objArchiveMenuList2);
		getArchiveMenuList.add(objArchiveMenuList3);
		//getArchiveMenuList.add(objArchiveMenuList4);
		getArchiveMenuList.add(objArchiveMenuList5);
		getArchiveMenuList.add(objArchiveMenuList8);
		getArchiveMenuList.add(objArchiveMenuList6);
		getArchiveMenuList.add(objArchiveMenuList7);
		//getArchiveMenuList.add(objArchiveMenuList9);
		getArchiveMenuList.add(objArchiveMenuList10);
		//getArchiveMenuList.add(objArchiveMenuList11);
		/*getArchiveMenuList.add(objArchiveMenuList12);
		getArchiveMenuList.add(objArchiveMenuList13);
		getArchiveMenuList.add(objArchiveMenuList14);
		getArchiveMenuList.add(objArchiveMenuList15);*/
		getArchiveMenuMap.put("001", objArchiveMenuList1);
		getArchiveMenuMap.put("002", objArchiveMenuList2);
		getArchiveMenuMap.put("003", objArchiveMenuList3);
		getArchiveMenuMap.put("004", objArchiveMenuList5);
		getArchiveMenuMap.put("005", objArchiveMenuList8);
		getArchiveMenuMap.put("006", objArchiveMenuList6);
		getArchiveMenuMap.put("007", objArchiveMenuList7);
		getArchiveMenuMap.put("008", objArchiveMenuList10);
		/*getArchiveMenuMap.put("0012", objArchiveMenuList12);
		getArchiveMenuMap.put("0013", objArchiveMenuList13);
		getArchiveMenuMap.put("0014", objArchiveMenuList14);
		getArchiveMenuMap.put("0015", objArchiveMenuList15);*/
		
	}

	public ArchiveMenuList(String id,String name, String url, String type) {
		this.id=id;
		this.name = name;
		this.url = url;
		this.type = type;
	}


	public static List<ArchiveMenuList> getArchiveMenuList() {                                                                  
		return getArchiveMenuList;
	}

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
}
