package com.huabo.legal.startup.bbs.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Transient;
import java.sql.Timestamp;

/**
 * 留言表
 *
 * @author zhuhuix
 * @date 2022-06-09
 */
@Schema(name="留言表")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tbl_fwgl_bbs")
public class Bbs {

	@TableId(value = "ID", type = IdType.ASSIGN_ID)
	private String id;

	@TableField(value = "NICKNAME")
	@Schema(name="提问人ID")
	private String nickName;

	@TableField(exist = false)
	@Schema(name="提问人名称")
	private String nickNames;

	@TableField(value = "IP")
	private String ip;

	@TableField(value = "CONTENT")
	private String content;

	@Schema(name="知识类型 1-劳动用工、2-知识产权、3-投融资、4-法律尽调、5-法律纠纷、6-其他")
	@TableField(value = "KNOWLEDGETYPE")
	private Integer knowledgeType;

	@TableField(value = "CREATETIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp createTime;

	@TableField(value = "REPLIEND")
	private Boolean replied = false;

	@Schema(name="回复人ID")
	@TableField(value = "REPLYNAME")
	private String replyName;

	@TableField(exist = false)
	@Schema(name="回复人名称")
	private String replyNames;

	@TableField(value = "REPLYCONTENT")
	private String replyContent;

	@TableField(exist = false)
	@Schema(name="是否展示回复按钮 false-不展示 true-展示")
	private Boolean isReplyDisplay;

	@TableField(value = "REPLYTIME")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp replyTime;

//	@Schema(name="软删除 去掉了")
//	@TableField(value = "ENABLED")
//	@JsonIgnore
//	private Boolean enabled = true;

	@TableField("BBSTYPE")
	@Schema(name="知识留言库类型 1-提问 2-回复")
	private Integer bbsType;

	@TableField("PARENTID")
	@Schema(name="父类 ID (回复主键ID)")
	private String parentId;
}
