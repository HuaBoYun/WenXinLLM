package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_NBSJ_ADVICENOTE")
@Schema(name="审计通知书实体类对象")
public class TblNbsjAdvicenoteEntity {

	public final static Integer NO_DEL=0;//未删除 使用中
	public final static Integer YE_DEL=1;//已删除 已作废
	public final static Integer SPZ=2;//审批中
	public final static Integer XTZ=3;//需调整
	public final static Integer YTG=4;//已通过
	public final static Integer YZZ=5;//已终止

	private static final long serialVersionUID = 1L;
	
	@TableId(value = "adviceid", type= IdType.AUTO)
	@Schema(name = "主键ID")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal adviceid;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	private TblStaff tblCreater;
	
	@TableField(value = "creatrtime")
	@Schema(name="创建时间",hidden=true)
	@IgnoreSwaggerParameter
	private Date creatrtime;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	private TblNbsjProject project;
	
	@TableField(value = "advicecoed")
	@Schema(name = "审计通知书编号")
	private String advicecoed;
	
	@TableField(value = "advicename")
	@Schema(name = "审计通知书名称")
	private String advicename;
	
	@TableField(value = "PROGECTID")
	@Schema(name = "项目ID")
	private String progectid;
	
	
	@TableField(value = "status")
	@Schema(name = "状态")
	private Integer status;
	
	@TableField(value = "content")
	@Schema(name = "内容")
	private String content;
	
	@TableField(value = "des")
	@Schema
	private String des;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	private Set<TblAttachment> tblNoteAtts;
	
	@TableField(value = "CREATESTAFFID")
	@Schema(name="员工ID",hidden=true)
	private String createstaffid;

	@TableField(value = "SJSSTIME")
	@Schema(name = "审计实施时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date sjsstime;

	@TableField(value = "TEAMLEADER")
	@Schema(name = "审计组长")
	private String teamleader;

	@TableField(value = "MAINREVIEWER")
	@Schema(name = "审计主审")
	private String mainreviewer;

	@TableField(value = "HELPREVIEWER")
	@Schema(name = "审计助审")
	private String helpreviewer;

	@TableField(value = "OPERATOR")
	@Schema(name = "经办人")
	private String operator;

	@TableField(value = "PROPOSAL")
	@Schema(name = "审批意见")
	private String proposal;
	
	@TableField(value = "OAID")
	@Schema(name = "关联OAid")
	private String oaid;
	
	@TableField(value = "TITLE")
	@Schema(name = "关联OA标题")
	private String title;
	
	@TableField(value = "URL")
	@Schema(name = "关联OA跳转页面")
	private String url;
	
	@TableField(value = "H5URL")
	@Schema(name = "关联OA移动端跳转页面")
	private String h5url;
	
}
