package com.huabo.audit.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
 * 下拉属性填充值
 * 
 * */
public class FillAttribute {
	
	 //审批状态
	 public static Map<BigDecimal, String> statusMap = new HashMap<BigDecimal, String>();
		static{
			try {
		 
		statusMap.put(null, "未审批");
		statusMap.put(new BigDecimal(1), "审批中");
		statusMap.put(new BigDecimal(2), "需调整");
		statusMap.put(new BigDecimal(3), "已撤销");
		statusMap.put(new BigDecimal(4), "已终止");
		statusMap.put(new BigDecimal(5), "已跟踪");
		statusMap.put(new BigDecimal(6), "已完成");
		
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
