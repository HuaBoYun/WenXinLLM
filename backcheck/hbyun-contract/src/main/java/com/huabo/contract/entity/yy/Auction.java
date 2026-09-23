package com.huabo.contract.entity.yy;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 司法拍卖
 * @author tj
 *
 */
@Data
public class Auction {
	private Integer auctionid;
	private String pubTime; //公告日期
    private String detail; //详细信息
    private String title;//拍卖公告
    private String court; //执行法院
    private String scopeDate; //拍卖期限
    private String url;//公告url
    private String introduction;//介绍
    private String sourceId;
    private String uniqueHash;
    private String _type;
    private String recordHash;
    private String eventTime;
    private String searchType;
    private String uni;
    private Date createdate;//保存数据时间
    
    private List<Details> details;

    private Integer companyid;

    private List<String> imgUrlList;
    private String targetObject;
    private String subtime;
    private String itemAddressDetail;
    private String startingPrice;
    private String content;
    private String evaluationPrice;
    private String itemFormUrl;
    private String auctionType;
    private String biddingInstructionsUrl;

}
