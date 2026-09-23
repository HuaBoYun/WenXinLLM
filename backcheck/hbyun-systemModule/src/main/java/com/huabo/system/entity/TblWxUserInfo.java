package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Table(name = "TBL_WXUSER_INFO")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Schema(name="用户TBL_STAFF对象", description="")
public class TblWxUserInfo implements Serializable {
	
	private static final long serialVersionUID = 6404391762640633361L;
	@TableId(value="USERID",type = IdType.INPUT)
	@Schema(name="主键ID,自动增长")
	private BigDecimal userId;//主键ID,自动增长
	@TableField("AVATARURL")
	@Column(name = "AVATARURL")
	@Schema(name="头像路径")
	private String avatarUrl;
	@TableField("CITY")
	@Column(name = "CITY")
	@Schema(name="城市")
	private String city;
	@TableField("COUNTRY")
	@Column(name = "COUNTRY")
	@Schema(name="国家")
	private String country;
	@TableField("GENDER")
	@Column(name = "GENDER")
	@Schema(name="性别（1为男，2为女，0为未知）")
	private String gender;
	@TableField("LANGUAGE")
	@Column(name = "LANGUAGE")
	@Schema(name="语言")
	private String language;
	@TableField("NICKNAME")
	@Column(name = "NICKNAME")
	@Schema(name="昵称")
	private String nickName;
	@TableField("XCXOPENID")
	@Column(name = "XCXOPENID")
	@Schema(name="小程序openId")
	private String xcxOpenId;
	@TableField("WEBOPENID")
	@Column(name = "WEBOPENID")
	@Schema(name="公众号OpenId")
	private String webOpenId;
	@TableField("PROVINCE")
	@Column(name = "PROVINCE")
	@Schema(name="省份")
	private String province;
	@TableField("UNIONID")
	@Column(name = "UNIONID")
	@Schema(name="唯一标识")
	private String unionId;
	@TableField("STAFFID")
	@Column(name = "STAFFID")
	@Schema(name="Tbl_Staff主键")
	private BigDecimal staffId;
	
	@TableField("APPNAME")
	@Column(name = "APPNAME")
	@Schema(name="小程序名称")
	private String appName; //外部同步企业来源Id
	
	@Transient
	private BigDecimal orgId;
	@Transient
	private String realName;
	@Transient
	private String email;
	@Transient
	private String miblePhone;
	@Transient
	private Integer registerChocie;
	@Transient
	private Integer isAdmin;//0 是微信管理员 1 不是
	@Transient
	private String password;//密码
	
	

}
