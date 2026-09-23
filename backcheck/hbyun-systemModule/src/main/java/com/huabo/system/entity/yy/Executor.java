package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 被执行人
 * @author tj
 *
 */
@Data
public class Executor {
	private Integer executorid;
	private String caseCode; //案号
    private String execCourtName; //执行法院
    private String pname; //被执行人名称
    private String partyCardNum;//身份证号／组织机构代码
    private String caseCreateTime; //创建时间 毫秒数
    private String execMoney; //执行标的
    private Integer companyid;
   	private Date yycreatedate;//保存数据时间
    
    private String caseCreateDate;//创建时间 日期

}
