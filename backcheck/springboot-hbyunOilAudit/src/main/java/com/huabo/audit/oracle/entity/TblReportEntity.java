package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_REPORT")
@Schema(name="审计报告实体类对象")
public class TblReportEntity {
	public final static Integer FHZ=5;//复核中
	public final static Integer FHTZ=6;//复核调整
	public final static Integer FHTG=9;//复核通过
	public final static Integer FHZZ=10;//复核终止
	
	
	public final static Integer SPZ=1;//审批中
	public final static Integer XTZ=2;//需调整
	public final static Integer YTG=3;//已通过
	public final static Integer YZZ=4;//已终止
	
	public final static Integer ZQYJ=7;//征求意见
	public final static Integer ZQYJTZ=8;//征求意见调整
	public final static Integer ZQYJTG=11;//征求意见通过
	public final static Integer ZQYJZZ=12;//征求意见终止

//	private static final long serialVersionUID = 1L;
	
	
	@TableId(value = "reportid", type= IdType.AUTO)
	@Schema
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal reportid;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	private TblReporttempleEntity tblReporttemple;
	
	@TableField(value = "reportname")
	@Schema
	private String reportname;
	
	@TableField(value = "reporttime")
	@Schema
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date reporttime;
	
	@TableField(value = "reporttype")
	@Schema
	private String reporttype;
	
	@TableField(value = "reportmode")
	@Schema
	private String reportmode;
	
	@TableField(value = "reporter")
	@Schema
	private String reporter;
	
	@TableField(value = "reportdepartment")
	@Schema
	private String reportdepartment;
	
	@TableField(value = "reportstatus")
	@Schema
	private Integer reportstatus;
	
	@TableField(value = "reportfile")
	@Schema
	private String reportfile;
	
	@TableField(value = "memo")
	@Schema
	private String memo;
	
	@TableField(value = "type")
	@Schema
	private String type;
	
	@TableField(value = "repdesc")
	@Schema
	private String repdesc;
	
	@TableField(value = "projectid")
	@Schema(name = "项目id")
	@Column(name="projectId")
	private BigDecimal projectId;
	
	@TableField(value = "sendtime")
	@Schema(name = "邮件发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	@Column(name="SENDTIME")
	private Date sendTime;
	
	@TableField(value = "FHSTAFFID")
	@Schema(name = "复核人id")
	private String fhstaffid;
	
	@TableField(value = "FHSTAFFNAME")
	@Schema(name = "复核人姓名")
	private String fhstaffname;
	
	@TableField(value = "ZQYJSTAFFID")
	@Schema(name = "征求人id")
	private String zqyjstaffid;
	
	@TableField(value = "ZQYJSTAFFNAME")
	@Schema(name = "征求人姓名")
	private String zqyjstaffname;
	
	@TableField(value = "orgid")
	@Schema(hidden=true)
	private Integer orgid;
	
	@TableField(value = "REPORTERID")
	@Schema
	private Integer reporterid;
	
	@TableField(value = "REPORTDEPARTMENTID")
	@Schema
	private Integer reportdepartmentid;
	
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
