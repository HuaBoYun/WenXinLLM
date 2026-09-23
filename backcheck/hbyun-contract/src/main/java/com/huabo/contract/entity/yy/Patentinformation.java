package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 专利信息
 * @author tjj
 *
 */
@Data
public class Patentinformation {
	private Integer formationid;
	private String mainCatNum;//主分类号
    private String createTime;//创建时间（毫秒）
    private String pubnumber;
    private String searchType;
    private String appnumber;
    private String id;
    private String _type;
    private String title;
    private String patentName;
    private String connList;
    private String applicationTime;
    private String applicantname;
    private String patentType;
    private String pubDate;
    private String applicationPublishNum;
    private String agency;
    private String uni;
    private String inventor;
    private String agent;
    private String applicationPublishTime;
    private String patentNum;
    private String imgUrl;
    private String allCatNum;
    private String abstracts;
    private String address;
    private String uuid;
    private String eventTime;//修改时间（毫秒）
    private String applicantName;
    private String lawStatus;
    private String cat;
    private String lprs;
    private Integer yycompanyid;
  	private Date yycreatedate;//保存数据时间

	private String patentStatus;
	private String searcType;
	private String grantDate;
	private String postCode;
	private String grantNumber;
	private String priorityInfo;
    
}
