package com.hbfk.util;

public class JudgeRoleRight {

	// 是该角色为true   否则为false
	public static boolean judgeRoleRight(String sjlbqx, String roleNames) {
		if(roleNames == null || "".equals(roleNames)) {
			return false;
		}
		
		String[] names = roleNames.split(",");
		sjlbqx += ",";
		boolean flag = false;
		for (String name : names) {
			if(sjlbqx.indexOf(name+",") != -1) {
				flag = true;
				break;
			}
		}
		return flag;
	}

}
