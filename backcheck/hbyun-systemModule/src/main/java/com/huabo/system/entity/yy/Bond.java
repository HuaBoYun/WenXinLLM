package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 债券信息
 * @author tj
 *
 */
@Data
public class Bond {
	private Integer bondid;
	private String bondName; //债券名称
    private String bondNum; //债券名称
    private String bondStopTime; //债券摘牌日
    private String bondTimeLimit; //债券期限
    private String bondTradeTime; //上市交易日
    private String bondType; //债券类型
    private String calInterestType; //计息方式
    private String createTime; //创建时间
    private String creditRatingGov; //信用评级机构
    private String debtRating; //债项评级
    private String escrowAgent; //托管机构
    private String exeRightTime; //行权日期
    private String exeRightType; //行权类型
    private String faceInterestRate; //票面利率(%)
    private String faceValue; //面值
    private String flowRange; //流通范围
    private String id; //对应表id
    private String interestDiff; //利差(BP)
    private String isDelete; //0;未删除 1;已删除
    private String issuedPrice; //发行价格(元)
    private String payInterestHZ; //付息频率
    private String planIssuedQuantity; //计划发行量(亿)
    private String publishExpireTime; //债劵到期日
    private String publishTime; //发布时间
    private String publisherName; //发行人
    private String realIssuedQuantity; //实际发行量(亿)
    private String refInterestRate; //参考利率
    private String remark; //备注
    private String startCalInterestTime; //债券起息日
    private String tip; //注
    private String updateTime; //更新时间
    
    private String publishDate;//发布时间  日期
    private String createDate;//创建时间  日期
    private String updateDate;//更新时间 日期
    private String bondTradeDate;//上市交易日 日期
    private String publishExpireDatel; //债劵到期日  日期
    
    private Integer companyid;
    private Date newcreatedate;//保存数据时间


}
