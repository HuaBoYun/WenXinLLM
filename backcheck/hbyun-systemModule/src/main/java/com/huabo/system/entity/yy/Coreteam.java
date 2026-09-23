package com.huabo.system.entity.yy;

import lombok.Data;

import java.util.Date;

/**
 * 核心团队
 * @author tj
 *
 */
@Data
public class Coreteam {
	private Integer coreteamid;
	private String companyId; //对应表id
    private String companyName; //公司名
    private String createTime; //创建时间
    private String desc;//描述
    private String graphId; //公司id
    private String icon;//logo
    private String iconOssPath;//logo存放位置
    private String id; //人id
    private String isDeleted; //0-未删除 1-删除
    private String name; //姓名
    private String title;//标签
    private String createDate; //创建时间 日期
    private String sourceWeb;
    private String isDimission;//0-现有成员 1-过往成员
    private String hid; //人id
    private String updateTime;//修改时间
    private Integer yycompanyid;
 	private Date yycreatedate;//保存数据时间

	private String toco;

}
