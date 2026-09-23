package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 对外投资
 * @author tj
 *
 */
@Data
public class Outbound {
	private Integer outboundid;
	private String orgType; // 公司类型
    private String business_scope; // 经营范围
    private String percent; // 投资占比
    private String regStatus; // 经营状态
    private String estiblishTime; // 开业时间 毫秒数
    private String legalPersonName; // 法人
    private String type; // 1-公司 2-人
    private String pencertileScore; // 评分
    private String legalPersonId; // 法人id
    private String amount; // 投资金额
    private String id; // 公司id
    private String category; // 行业
    private String regCapital; // 注册资本
    private String name; // 被投资公司名
    private String base; // 省份简称
    private String creditCode; // 统一社会信用代码
    private String personType; // 1-人 2-公司
    private Integer yycompanyid;
  	private Date yycreatedate;//保存数据时间
     
	private String estiblishDate;// 开业时间 日期
	private String amountSuffix; //金额单位
	private String alias;

}
