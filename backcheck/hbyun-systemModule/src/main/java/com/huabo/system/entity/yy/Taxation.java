package com.huabo.system.entity.yy;

import lombok.Data;

/**
 * 税务评级
 * @author Administrator
 *
 */
@Data
public class Taxation {
	 private String grade; //纳税等级
     private String year; //年份
     private String evalDepartment; //评价单位
     private String type; //类型
     private String idNumber; //纳税人识别号
     private String name; //纳税人名称
     
}
