package com.huabo.fxgl.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/*
 * 用于给风险模块下拉属性填充值
 * 
 * */
public class FillAttribute {
	
	// 月度评估--风险等级
	public static Map<String, String> assessGradeMap = new HashMap<String, String>();
	// 月度评估--风险变化趋势
	public static Map<String, String> riskChangeMap = new HashMap<String, String>();

	// 月度评估--是否新增风险
	public static Map<BigDecimal, String> isNewRiskMap = new HashMap<BigDecimal, String>();
	// 月度评估--月份
	public static Map<BigDecimal, String> monthMap = new HashMap<BigDecimal, String>();
	
	// 月度评估--月份
	 public static Map<String, Integer> quarterMap = new HashMap<String, Integer>();
	 //审批状态
	 public static Map<BigDecimal, String> statusMap = new HashMap<BigDecimal, String>();
	 //是否关闭 风险状态：0已经关闭  1 未关闭  2 开启  用于重大风险月度评估关闭风险
	 public static Map<BigDecimal, String> riskStatusMap = new HashMap<BigDecimal, String>();
	static{
		try {
			
	
		assessGradeMap.put("1", "极低");
		assessGradeMap.put("2", "低");
		assessGradeMap.put("3", "中");
		assessGradeMap.put("4", "高");
		assessGradeMap.put("5", "极高");
		
		riskChangeMap.put("1", "升高");
		riskChangeMap.put("2", "持平");
		riskChangeMap.put("3", "下降");
		
		isNewRiskMap.put(new BigDecimal(1), "新增风险");
		isNewRiskMap.put(new BigDecimal(2), "已有风险");
		isNewRiskMap.put(new BigDecimal(3), "关闭风险");
		
		monthMap.put(new BigDecimal(1), "一月");
		monthMap.put(new BigDecimal(2), "二月");
		monthMap.put(new BigDecimal(3), "三月");
		monthMap.put(new BigDecimal(4), "四月");
		monthMap.put(new BigDecimal(5), "五月");
		monthMap.put(new BigDecimal(6), "六月");
		monthMap.put(new BigDecimal(7), "七月");
		monthMap.put(new BigDecimal(8), "八月");
		monthMap.put(new BigDecimal(9), "九月");
		monthMap.put(new BigDecimal(10), "十月");
		monthMap.put(new BigDecimal(11), "十一月");
		monthMap.put(new BigDecimal(12), "十二月");
		
		quarterMap.put("一季度", 1);
		quarterMap.put("二季度", 2);
		quarterMap.put("三季度",3);
		quarterMap.put("四季度", 4);
		
		statusMap.put(null, "未审批");
		statusMap.put(new BigDecimal(1), "审批中");
		statusMap.put(new BigDecimal(2), "需调整");
		statusMap.put(new BigDecimal(3), "已撤销");
		statusMap.put(new BigDecimal(4), "已终止");
		statusMap.put(new BigDecimal(5), "已跟踪");
		statusMap.put(new BigDecimal(6), "已完成");
		
		
		riskStatusMap.put(new BigDecimal(0), "已关闭");
		riskStatusMap.put(new BigDecimal(1), "未关闭");
		riskStatusMap.put(null, "未关闭");
		//riskStatusMap.put(new BigDecimal(2), "开启");
		
		
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
