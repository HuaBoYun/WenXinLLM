package com.huabo.system.entity.yy;

import lombok.Data;

/**
 * 搜索
 * @author tj
 *
 */
@Data
public class Search {
	
	private String id;
	private String regCapital;//注册资本 
	private String name;//公司名称
	private String base;//省份
	private String companyType;//公司类型 1-公司，2-香港公司，3-社会组织，4-律所，5-事业单位，6-基金会
	private String estiblishTime;//开业时间
	private String legalPersonName;//法人
	private String type;//1-公司 2-人
	private String regStatus;// 注册状态
	private String matchType;//匹配类型
	private String creditCode;//信用代码
	private String orgNumber;//公司编号

}
