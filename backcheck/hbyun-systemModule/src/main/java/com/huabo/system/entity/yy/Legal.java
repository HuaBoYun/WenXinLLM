package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 法律诉讼
 * @author tj
 *
 */
@Data
public class Legal {
	private Integer legalid;
	private String plaintiffs; //原告
    private String court; //法院
    private String casereason; //案由
    private String url;//原文链接地址
    private String caseno; //案件号
    private String id; //对应表id
    private String title; //标题
    private String abstracts; //摘要
    private String submittime; //提交时间 毫秒数
    private String lawsuitUrl;//天眼查显示url
    private String casetype; //案件类型
    private String uuid; //uuid
    private String doctype; //文书类型
    private String defendants; //被告
    private String submitDate; //提交时间 日期
    private String SplitGids;//相关公司id
    private String plaintiffId;//原告id
    private String appellantId;
    private String uni;
    private String _type;
    private String docid;
    private String connList;
    private String appelleeId;
    private String eventTime;
    private String num;
    private Integer yycompanyid;
	private Date yycreatedate;//保存数据时间

	private String docType;
	private String lawsuitH5Url;
	private String judgeTime;
	private String caseNo;
	private String caseType;
	private String caseReason;
	private String casePersons;
	private String caseMoney;
	private String submitTime;

    private String name;
    private Integer cType;


}
