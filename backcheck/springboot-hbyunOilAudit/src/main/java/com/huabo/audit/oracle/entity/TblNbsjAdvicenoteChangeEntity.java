package com.huabo.audit.oracle.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name="TBL_NBSJ_ADVICENOTE_CHANGE")
@Schema(name="审计通知书更改实体类对象")
public class TblNbsjAdvicenoteChangeEntity {

	public final static Integer NO_DEL=0;//未删除 使用中
	public final static Integer YE_DEL=1;//已删除 已作废
	public final static Integer SPZ=2;//审批中
	public final static Integer XTZ=3;//需调整
	public final static Integer YTG=4;//已通过
	public final static Integer YZZ=5;//已终止

	private static final long serialVersionUID = 1L;
	
	@TableId(value = "CHANGEID", type= IdType.AUTO)
	@Schema(name = "主键ID")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
	private BigDecimal changeid;

	@TableId(value = "ADVICEID")
	@Schema(name = "审计通知ID")
	private BigDecimal adviceid;
	
	@TableField(value = "CREATRTIME")
	@Schema(name="创建时间",hidden=true)
	@IgnoreSwaggerParameter
	private Date creatrtime;
	
	@Schema(hidden=true)
	@IgnoreSwaggerParameter
	@Transient
	private TblNbsjProject project;
	
	@TableField(value = "PROGECTID")
	@Schema(name = "项目ID")
	private Integer progectid;
	
	
	@TableField(value = "status")
	@Schema(name = "状态")
	private Integer status;
	
	@TableField(value = "CHANGETHING")
	@Schema(name = "变更事项")
	private String changething;

	@TableField(value = "CHANGEBEFORE")
	@Schema(name = "变更前内容")
	private String changebefore;

	@TableField(value = "CHANGEAFTER")
	@Schema(name = "变更后内容")
	private String changeafter;

	@TableField(value = "CHANGEREASON")
	@Schema(name = "变更原因")
	private String changereason;
	
	@TableField(value = "CREATESTAFFID")
	@Schema(name="员工ID",hidden=true)
	private String createstaffid;

	@TableField(value = "JBR")
	@Schema(name = "经办人")
	private String jbr;

	@TableField(value = "ORGID")
	@Schema(name = "部门ID")
	private String orgid;

	@TableField(value = "CHANGETIME")
	@Schema(name = "审批时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date changetime;


	@Schema(name="开始时间",hidden=true)
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private String startDate;

	@Schema(name="结束时间",hidden=true)
	@TableField(exist = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private String endDate;
	
}
