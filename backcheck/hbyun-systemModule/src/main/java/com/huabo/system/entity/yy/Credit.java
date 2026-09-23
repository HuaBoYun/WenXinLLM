package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 行政处罚（信用中国）
 * @author tj
 *
 */
@Data
public class Credit {
	private Integer creditid;
	private String punishmentName; //公司名
    private String areaName; //地区
    private String url; //处罚url
    private Integer companyid;
 	private Date createdate;//保存数据时间


}
