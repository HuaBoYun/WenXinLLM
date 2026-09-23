package com.huabo.system.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
@Schema(name = "模板实体类")
@AllArgsConstructor
@NoArgsConstructor
@TableName("TBL_TEMPLATE_UD")
public class TblTemplateDu implements Serializable{
	private static final long serialVersionUID = 1L;

	@TableId(value = "TEMPID",type = IdType.INPUT)
	@Schema(name="模板类型")
	private BigDecimal tempId;
	@TableField("PRE")
	@Schema(name="图片路径，默认pre1.png")
	private String pre;
	@TableField("TITLE")
	@Schema(name="模板标题，不能为空")
	private String title;
	@TableField("PREHTML")
	@Schema(name="图片html代码可以为空")
	private String preHtml;
	@TableField("HTML")
	@Schema(name="模板内容，百度编辑器内部包含HTML代码")
	private String html;
	@TableField("TYPE")
	@Schema(name="模板类型，审计通知书-sjtzs  ")
	private String type;
}
