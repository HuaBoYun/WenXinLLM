package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 严重违法
 * @author tj
 *
 */
@Data
public class Serious {
	private Integer seriousid;
	private String removeReason; //移除原因
    private String removeDepartment; //决定移除部门
    private String putDate; //列入日期  毫秒数
    private String putReason; //列入原因
    private String putDepartment; //决定列入部门(作出决定机关)
    private String removeDate; //移除日期 毫秒数
    private Integer yycompanyid;
    private Date yycreatedate;//保存数据时间

    private String putTime;//列入日期 
    private String removeTime; //移除日期
     
}
