package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 认缴
 * @author tj
 *
 */
@Data
public class Capitals {
	private Integer captislsid;
	private String amomon; //认缴金额
    private String percent; //占比
    private String time; //认缴时间
    private String paymet; //认缴形式
    
    private Integer companyid;
	private Date createdate;//保存数据时间
	private Shareholder shareholder;
    

    
}
