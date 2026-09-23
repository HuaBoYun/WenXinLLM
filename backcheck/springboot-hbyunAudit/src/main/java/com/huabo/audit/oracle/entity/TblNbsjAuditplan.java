package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-05-23
 */
 
@TableName("TBL_NBSJ_AUDITPLAN")
@Data
@Schema(name="审计项目计划管理实体类")
@Accessors(chain = true)
public class TblNbsjAuditplan implements Serializable {

	public final static Integer NO_SHS=0;//未实施
	public final static Integer YE_SHS=1;//已实施
	////审批状态
	/**
	 * 0未审批 
	 */
	public final static Integer SPNO=0;
	/**
	 * 1 审批中
	 */
	public final static Integer SPKA=1;
	/**
	 * 2 需调整  
	 */
	public final static Integer SPTZ=2;
	/**
	 * 3审批完
	 */
	public final static Integer SPWC=3;
	public final static Integer SPZZ=4;
	public final static Integer YSCXM=5;//已生成项目
	
    private static final long serialVersionUID = 1L;

    @Id
    @Schema(name = "计划主键ID")
    @TableId("PLANID")
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
    @Column(name = "PLANID")
    private BigDecimal planid;

    @Schema(name = "计划编码")
    @TableField("PLANCODE")
    @Column(name = "PLANCODE")
    private String plancode;

    @Schema(name = "计划名称")
    @Column(name = "PLANNAME")
    @TableField("PLANNAME")
    private String planname;

    @Schema(name = "计划年度")
    @Column(name = "PALNYEAR")
    @TableField("PALNYEAR")
    private String palnyear;

    @Schema(name = "计划类型")
    @Column(name = "PLANTYPE")
    @TableField("PLANTYPE")
    private String plantype;

    @Schema(name = "计划对象 组织表外键")
    @Column(name = "AUDITORGID")
    @TableField("AUDITORGID")
    private BigDecimal auditorgid;

    @Schema(name = "计划估算费用")
    @Column(name = "PALNCOST")
    @TableField("PALNCOST")
    private BigDecimal palncost;

    @Schema(name="开始时间",hidden=true)
    @Column(name = "STARTTIME")
    @TableField("STARTTIME")
    private Date starttime;

    @Schema(name="结束时间",hidden=true)
    @Column(name = "ENDTIME")
    @TableField("ENDTIME")
    private Date endtime;

    @Schema(name = "计划负责人 用户表外键")
    @Column(name = "PRINCIPALID")
    @TableField("PRINCIPALID")
    private BigDecimal principalid;

    @Schema(name = "审计组长 用户表外键")
    @Column(name = "LEADERID")
    @TableField("LEADERID")
    private BigDecimal leaderid;

    @Schema(name = "审计目标和范围")
    @Column(name = "REMARKS")
    @TableField("REMARKS")
    private String remarks;

    @Schema(name = "创建人 用户表外键")
    @Column(name = "CREATESTAFFID")
    @TableField("CREATESTAFFID")
    private BigDecimal createstaffid;

    @Schema(name="创建时间",hidden=true)
    @Column(name = "CREATETIME")
    @TableField("CREATETIME")
    private Date createtime;

    @Schema(name="修改时间",hidden=true)
    @Column(name = "UPDATETIMR")
    @TableField("UPDATETIMR")
    private Date updatetimr;

    @Schema(name = "项目状态")
    @Column(name = "STATUS")
    @TableField("STATUS")
    private Integer status;

    @Schema(name = "审批状态")
    @Column(name = "OPINIONSTATUS")
    @TableField("OPINIONSTATUS")
    private Integer opinionstatus;

    @Schema(name = "是否外审  1是  0否")
    @Column(name = "ISAUDITOR")
    @TableField("ISAUDITOR")
    private Integer isauditor;

    @Schema(name = "审计组长实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblStaff leaderStaff;
    
    @Schema(name = "计划负责人实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblStaff principalStaff;
    
   
    @Schema(name = "审计对象实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblOrganization auditOrgInfo;
    
    @Schema(name = "审计组长实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblStaff createStaff;
    
    @Schema(name = "审计计划项目集合")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private List<TblNbsjPlanProject> planProjectList;
    
    @Schema(name = "审计项目集合")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private List<TblNbsjProject>  projectList;
    
    @Schema(name="总实施项目",hidden=true)
    @Transient
    @TableField(exist = false)
    private Integer totalItem;
    
    @Schema(name="未实施项目",hidden=true)
    @Transient
    @TableField(exist = false)
    @IgnoreSwaggerParameter
    private Integer unenforcedItem;
    
    @Schema(name="进行中项目",hidden=true)
    @Transient
    @TableField(exist = false)
    private Integer condectItem;
    
    @Schema(name="已完成项目",hidden=true)
    @Transient
    @TableField(exist = false)
    private Integer completeItem;

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

//----新增字段 审计计划管理-----------------------------------------------------------------------------------
    @TableField(value = "PROJECTORG")
    @Column(name = "PROJECTORG")
    @Schema(name = "项目单位名称ID")
    private String projectorg;

    @TableField(value = "PRJOECTNAME")
    @Column(name = "PRJOECTNAME")
    @Schema(name = "项目名称")
    private String prjoectName;

    @TableField(value = "PROJECTTYPE")
    @Column(name = "PROJECTTYPE")
    @Schema(name = "项目类型")
    private String prjoectType;

    @TableField(value = "COSTS")
    @Column(name = "COSTS")
    @Schema(name = "批复总投资（经费）")
    private BigDecimal costs;

    @TableField(value = "APPPROYEARSTART")
    @Column(name = "APPPROYEARSTART")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "批复的项目起止年限—开头")
    private Date appproyearstart;

    @TableField(value = "APPPROYEAREND")
    @Column(name = "APPPROYEAREND")
    @Schema(name = "批复的项目起止年限—结尾")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date appproyearend;

    @TableField(value = "ACTPROYEARSTART")
    @Column(name = "ACTPROYEARSTART")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "实际的起止年限—开头")
    private Date actproyearstart;

    @TableField(value = "ACTPROYEAREND")
    @Column(name = "ACTPROYEAREND")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    @Schema(name = "实际的起止年限—结尾")
    private Date actproyearend;

    @TableField(value = "PROJECTMGDEPTID")
    @Column(name = "PROJECTMGDEPTID")
    @Schema(name = "项目主管部门ID")
    private String projectmgdeptid;

    @TableField(value = "PROJECTMGDEPTNAME")
    @Column(name = "PROJECTMGDEPTNAME")
    @Schema(name = "项目主管部门名称")
    private String projectmgdeptname;

    @TableField(value = "STARTDATE")
    @Column(name = "STARTDATE")
    @Schema(name = "计划审计时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date startDate;

    @TableField(value = "ENDDATE")
    @Column(name = "ENDDATE")
    @Schema(name = "计划验收时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE_TIME)
    private Date endDate;

    @TableField(value = "PROJECTORGADDRESS")
    @Column(name = "PROJECTORGADDRESS")
    @Schema(name = "项目单位地址")
    private String projectorgaddress;

    @TableField(value = "PROJECTLINKMAN")
    @Column(name = "PROJECTLINKMAN")
    @Schema(name = "项目联系人")
    private String projectlinkman;

    @TableField(value = "PROJECTLINKTEL")
    @Column(name = "PROJECTLINKTEL")
    @Schema(name = "联系电话")
    private String projectlinktel;

    @TableField(value = "IMPLEMENTAION")
    @Column(name = "IMPLEMENTAION")
    @Schema(name = "审计实施主体")
    private String implementaion;

    @TableField(value = "AUDITCODE")
    @Column(name = "AUDITCODE")
    @Schema(name = "审计类型ID")
    private String auditCode;

    @TableField(value = "AUDITTYPE")
    @Column(name = "AUDITTYPE")
    @Schema(name = "审计类型")
    private String auditType;

    @TableField(value = "COMMENTS")
    @Column(name = "COMMENTS")
    @Schema(name = "备注")
    private String comments;

}
