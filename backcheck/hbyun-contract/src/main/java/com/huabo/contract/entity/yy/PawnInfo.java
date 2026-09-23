package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

@Data
public class PawnInfo {
	
	private Integer pawninfoid;
	private String detail; //数量、质量、状况、所在地等情况
    private String ownership; //所有权归属
    private String pawnName; //名称
    private String remark; //备注
    
    private Integer companyid;
 	private Date createdate;//保存数据时间
 	private Chattel chattel;

}
