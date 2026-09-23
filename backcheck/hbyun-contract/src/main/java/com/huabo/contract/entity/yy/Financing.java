package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 融资历史
 * @author tj
 *
 */
@Data
public class Financing {
	private Integer financingid;
	private String companyId; //对应表id
    private String companyName; //公司名
    private String date; //融资时间 毫秒数
    private String investorName; //投资企业
    private String isDeleted; //0-未删除 1-已删除
    private String money; //金额
    private String newsTitle; //新闻标题
    private String newsUrl; //新闻url
    private String organizationName; //投资公司
    private String rongziMap;//弃用
    private String round; //轮次
    private String share; //投资比例
    private String sourceWeb; //无用
    private String tzrIds; //投资公司
    private String value; //估值
    private String datetime; //融资时间 日期
    private String pubTime;
    private Integer yycompanyid;
   	private Date yycreatedate;//保存数据时间


}
