package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 行政处罚
 * @author tj
 *
 */
@Data
public class BusinessRisk {
	private Integer businessRiskid;
	private String content; //行政处罚内容
	private String punishNumber;//行政处罚决定书文号
	private String regNum;//注册号
	private String description;//描述
	private String name; //公司名称
	private String base;//省份简称
	private String decisionDate;//作出行政处罚决定日期
	private String legalPersonName;//法定代表人（负责人）姓名
	private String type;//违法行为类型
	private String departmentName;//作出行政处罚决定机关名称
	private String publishDate;//公示日期
	
	private Integer companyid;
	private Date createdate;//保存数据时间

	private String reason;
	private String evidence;
	private String punishStatus;
	private String remark;
	private String source;
	private String punishName;
	private String typeSecond;


}
