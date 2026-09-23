package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 招投标
 * @author tj
 *
 */
@Data
public class Managementcondition {

	private Integer conditionid;
	private String content;
	private String createTime;//创建时间
	private String title;////标题
	private String abs;// 摘要信息、公告概要
	private String purchaser; //采购人
	private String publishTime;//发布日期
	private String updateTime;//更新时间
	private String proxy;//代理机构
	private String link;//来源链接
	private String pid;//对应表id
	private String uuid; //uuid
	private String bidUrl;//bidUrl	
	private Integer yycompanyid;
	private Date yycreatedate;//保存数据时间
	
	
	
    private String publishDate;//发布日期 日期
    private String updateDate;////更新时间 日期
    private String createDate;//创建时间 日期

	private String id;

}
