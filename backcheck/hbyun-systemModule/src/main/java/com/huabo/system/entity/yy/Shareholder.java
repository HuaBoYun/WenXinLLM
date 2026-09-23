package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;
import java.util.List;


/**
 * 股东信息
 * @author tj
 *
 */
@Data
public class Shareholder {
	private Integer shareholderid;
	private String id; //对应表id
    private String amount; //金额
    private String toco; //拥有公司个数
    private String capitalActl; //实缴
    private String logo;//img.tianyancha.com/logo/human2/345609db75383b411884a401a6f3665e.png@!watermark01 //logo
    private String type; //1-公司 2-人
    private String capital; //认缴
    private String name; //股东名
    
    /**
     * 企业年报-股东信息
     */
    private String investorName; //股东名称
    private String subscribeAmount; //认缴出资额
    private String subscribeTime;//认缴出资时间
    private String subscribeType; //认缴出资方式
    private String paidAmount; //实缴出资额
    private String paidTime; //实缴出资时间
    private String paidType; //实缴出资方式
    private String clickId; //股东id
    private String reportYear; //年份
    
    
    private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间
     
     
     
     private List<Capitals> capitals;
	 private String alias;
	 private String hcgid;




     
}
