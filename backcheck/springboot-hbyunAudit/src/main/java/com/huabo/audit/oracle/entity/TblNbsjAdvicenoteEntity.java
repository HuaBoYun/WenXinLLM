package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

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
	
	@TableId(value = "adviceid", type= IdType.INPUT)
	@Schema
	@Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal adviceid;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	@TableField(exist = false)
	private TblStaff tblCreater;
	
	@TableField(value = "creatrtime")
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	private Date creatrtime;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	@TableField(exist = false)
	private TblNbsjProject project;
	
	@TableField(value = "advicecoed")
	@Schema
	private String advicecoed;
	
	@TableField(value = "advicename")
	@Schema
	private String advicename;
	
	@TableField(value = "PROGECTID")
	@Schema
	private String progectid;
	
	
	@TableField(value = "status")
	@Schema
	private Integer status;
	
	@TableField(value = "content")
	@Schema
	private String content;
	
	@TableField(value = "des")
	@Schema
	private String des;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	@TableField(exist = false)
	private Set<TblAttachment> tblNoteAtts;
	
	@TableField(value = "CREATESTAFFID")
	@Schema(hidden=true)
	private String createstaffid;
	
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
	
	@Schema(name = "创建人员部门")
    @TableField("ORGID")
    @Column(name = "ORGID")
    private BigDecimal orgid;
	
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
}
