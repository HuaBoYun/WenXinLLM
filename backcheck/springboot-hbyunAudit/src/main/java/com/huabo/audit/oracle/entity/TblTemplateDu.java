package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 百度编辑器模板
 *<p>Title:</p>
 *<p>Description:</p>
 * @author SongXiangYing
 * @date 2016年6月28日 下午4:21:38
 */
@Data
@Schema(name="模板实体类")
@Table(name="TBL_TEMPLATE_UD")
@AllArgsConstructor
@NoArgsConstructor
public class TblTemplateDu implements Serializable{
	private static final long serialVersionUID = 1L;

	@TableId(value = "TEMPID")
	@Schema(name = "模板类型")
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	@Column(name = "TEMPID")
	private BigDecimal tempId;
	@TableField("PRE")
	@Column(name = "PRE")
	@Schema(name = "图片路径，默认pre1.png")
	private String pre;
	@TableField("TITLE")
	@Column(name = "TITLE")
	@Schema(name = "模板标题，不能为空")
	private String title;
	@TableField("PREHTML")
	@Column(name = "PREHTML")
	@Schema(name = "图片html代码可以为空")
	private String preHtml;
	@TableField("HTML")
	@Column(name = "HTML")
	@Schema(name = "模板内容，百度编辑器内部包含HTML代码")
	private String html;
	@TableField("TYPE")
	@Column(name = "TYPE")
	@Schema(name = "模板类型，审计通知书-sjtzs  ")
	private String type;
}
