package com.huabo.contract.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 主要人员
 * @author tj
 *
 */
@Data
public class Keypersonnel {

	private Integer keypersonnelid;
	private String toco;//拥有公司个数
	private String id;//对应表id
	private String name;//人或公司名
	private String typeJoin;////职位
	private String type;//法人类型，1 人 2 公司
	private String logo;
	private Integer yycompanyid;
	private Date yycreatedate;//保存数据时间
	private String hcgid;

}
