package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 抽查检查
 * @author tj
 *
 */
@Data
public class Spotcheck {
	private Integer sportcheckid;
	private String checkDate; //日期
    private String checkOrg; //检查实施机关
    private String checkType; //类型
    private String remark; //备注
    private String checkResult; //结果
    
    private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间

}
