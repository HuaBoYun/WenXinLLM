package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.system.constant.YesNo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 系统登录页
 */
@Schema(name = "TblSystemLoginPageOracle")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "TBL_SYSTEM_LOGIN_PAGE")
public class TblSystemLoginPageOracle implements Serializable {
	/**
	 * 主键ID
	 */
	@TableId(value = "ID",type = IdType.INPUT)
	@Schema(name="主键ID")
	private BigDecimal id;

	/**
	 * 登录页名称
	 */
	@TableField(value =  "LOGINNAME")
	@Schema(name="登录页名称")
	private String loginName;

	/**
	 * 销售热线
	 */
	@TableField(value =  "SALESHOTLINE")
	@Schema(name="销售热线")
	private String salesHotline;

	/**
	 * 访问路径
	 */
	@TableField(value =  "ACCESSPATH")
	@Schema(name="访问路径")
	private String accessPath;

	/**
	 * 技术支持热线
	 */
	@TableField(value =  "TECHNICALSUPPORTHOTLINE")
	@Schema(name="技术支持热线")
	private String technicalSupportHotline;

	/**
	 * 登录页文字一
	 */
	@TableField(value =  "LOGINTEXTONE")
	@Schema(name="登录页文字一")
	private String loginTextOne;

	/**
	 * 登录页文字二
	 */
	@TableField(value =  "LOGINTEXTTWO")
	@Schema(name="登录页文字二")
	private String loginTextTwo;

	/**
	 * 登录页文字三
	 */
	@TableField(value =  "LOGINTEXTTHIRD")
	@Schema(name="登录页文字三")
	private String loginTextThird;

	/**
	 * 左上角图片
	 */
	@TableField(value =  "LEFTUPPERPICTURE")
	@Schema(name="左上角图片")
	private String leftUpperPicture;

	/**
	 * 首页文字
	 */
	@TableField(value =  "HOMETEXT")
	@Schema(name="首页文字")
	private String homeText;

	/**
	 * 首页图片
	 */
	@TableField(value =  "HOMEPICTURE")
	@Schema(name="首页图片")
	private String homePicture;

	/**
	 * 状态
	 */
	@TableField(value =  "STATE")
	@Schema(name="状态")
	private Integer state;

	/**
	 * 创建人ID
	 */
	@TableField(value =  "CREATOR")
	@Schema(name="创建人ID")
	private BigDecimal creator;

	/**
	 * 工作单位ID
	 */
	@TableField(value =  "WORKUNIT")
	@Schema(name="工作单位ID", hidden = true)
	private BigDecimal workUnit;

	/**
	 * 所属集团ID
	 */
	@TableField(value =  "BELONGGROUP")
	@Schema(name="所属集团ID", hidden = true)
	private BigDecimal belongGroup;

	/**
	 * 创建时间
	 */
	@TableField(value =  "CREATEDTIME")
	@Schema(name="创建时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createdTime;

	/**
	 * 更新时间
	 */
	@TableField(value =  "UPDATEDTIME")
	@Schema(name="更新时间", hidden = true)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updatedTime;

	@TableField(value =  "MODULETEXT")
	@Schema(name="模块页面文字")
	private String moduleText;

	@TableField(value =  "LOGOPICTURE")
	@Schema(name="模块页面logo")
	private String logoPicture;

	private static final long serialVersionUID = 1L;

	public static TblSystemLoginPageOracle ofId(BigDecimal id) {
		TblSystemLoginPageOracle systemLoginPageOracle = new TblSystemLoginPageOracle();
		systemLoginPageOracle.setId(id);
		return systemLoginPageOracle;
	}

	public static TblSystemLoginPageOracle ofState(BigDecimal id, Integer state) {
		TblSystemLoginPageOracle systemLoginPageOracle = new TblSystemLoginPageOracle();
		systemLoginPageOracle.setId(id);
		systemLoginPageOracle.setState(state);
		return systemLoginPageOracle;
	}

	public static TblSystemLoginPageOracle ofState(Integer state) {
		TblSystemLoginPageOracle systemLoginPageOracle = new TblSystemLoginPageOracle();
		systemLoginPageOracle.setState(state);
		return systemLoginPageOracle;
	}

	public static TblSystemLoginPageOracle ofBelongGroup(BigDecimal belongGroup) {
		TblSystemLoginPageOracle systemLoginPageOracle = new TblSystemLoginPageOracle();
		systemLoginPageOracle.setBelongGroup(belongGroup);
		systemLoginPageOracle.setState(YesNo.YES);
		return systemLoginPageOracle;
	}
}