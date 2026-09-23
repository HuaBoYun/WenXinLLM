package com.huabo.know.utils;

public class T1 {

	public static void main(String[] args) {
		
		String nextApproval = "系统管理员/system,集团管理员/group-admin,审计管理员/audit-admin";
		String[] approvalUsers = nextApproval.split(",");
		String[] userInfo = new String[2];
		for (String appUserInfo : approvalUsers) {
			userInfo = appUserInfo.split("/");
			System.out.println(userInfo[1]);
			
			
		}
	}

}
