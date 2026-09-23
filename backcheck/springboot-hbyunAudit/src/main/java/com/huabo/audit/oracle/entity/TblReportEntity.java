package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
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
	
	
	@TableId(value = "reportid", type= IdType.INPUT)
	@Schema(name = "主键")
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal reportid;

	@TableField(exist = false)
	@IgnoreSwaggerParameter
	@Transient
	private TblReporttempleEntity tblReporttemple;
	
	@TableField(value = "reportname")
	@Schema(name = "报告名称")
	private String reportname;
	
	@TableField(value = "reporttime")
	@Schema(name = "报告时间")
//	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
	private Date reporttime;
	
	@TableField(value = "reporttype")
	@Schema(name = "报告类型")
	private String reporttype;
	
	@TableField(value = "reportmode")
	@Schema(name = "报告方式")
	private String reportmode;
	
	@TableField(value = "reporter")
	@Schema(name = "报告人")
	private String reporter;
	
	@TableField(value = "reportdepartment")
	@Schema(name = "报告部门")
	private String reportdepartment;
	
	@TableField(value = "reportstatus")
	@Schema(name = "状态")
	private Integer reportstatus;
	
	@TableField(value = "reportfile")
	@Schema(name = "文件")
	private String reportfile;
	
	@TableField(value = "memo")
	@Schema(name = "备注")
	private String memo;
	
	@TableField(value = "type")
	@Schema(name = "类型：区分内控、审计")
	private String type;
	
	@TableField(value = "repdesc")
	@Schema(name = "编辑器内容")
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
	private BigDecimal fhstaffid;
	
	@TableField(value = "FHSTAFFNAME")
	@Schema(name = "复核人姓名")
	private String fhstaffname;
	
	@TableField(value = "ZQYJSTAFFID")
	@Schema(name = "征求人id")
	private BigDecimal zqyjstaffid;
	
	@TableField(value = "ZQYJSTAFFNAME")
	@Schema(name = "征求人姓名")
	private String zqyjstaffname;
	
	@TableField(value = "orgid")
	@Schema(hidden=true)
	private BigDecimal orgid;
	
	@TableField(value = "REPORTERID")
	@Schema
	private BigDecimal reporterid;
	
	@TableField(value = "REPORTDEPARTMENTID")
	@Schema
	private BigDecimal reportdepartmentid;
	
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
	
	
	@TableField(value = "XFRYIDS")
	@Schema(name = "下发人员ids")
	private String xfryids;
	
	@TableField(value = "XFRYNAMES")
	@Schema(name = "下发人员名称")
	private String xfrynames;
	
	@Schema(name = "密级主键")
    @TableField("SECRECTLEVELID")
    @Column(name = "SECRECTLEVELID")
    private BigDecimal secrectLevelId;
    
    @Schema(name = "知悉范围 多个逗号分隔")
    @TableField("STAFFSCOPEIDS")
    @Column(name = "STAFFSCOPEIDS")
    private String staffScopeIds;
    
    @Schema(name = "知悉访问人员姓名 多个逗号分隔")
    @TableField("STAFFSCOPENAMES")
    @Column(name = "STAFFSCOPENAMES")
    private String staffScopeNames;

	//创建人id
	@TableField("CREATESTAFFID")
	private BigDecimal createstaffid;
    
}
