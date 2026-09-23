package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 舆情信息
 * @author tj
 *
 */
@Data
public class Publicopinion {

	private Integer publicopinionid;
	private String title; //新闻标题
    private String url; //链接
    private String website; //来源
    private String time; //时间
    private String abstracts;////简介
    private String docid;////新闻唯一标识符 
    private String rtm;// //发布时间 Unix时间戳 毫秒
    private String uri;
    private Integer yycompanyid;
   	private Date yycreatedate;//保存数据时间

	private String emotion;
	private String tags;
}
