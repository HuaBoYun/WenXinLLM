package com.huabo.audit.oracle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-05-23
 */
@Data
@TableName("TBL_NBSJ_AUDITPLAN")
@Schema(name="审计计划对象")
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
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
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
    private TblStaff leaderStaff;
    
    @Schema(name = "计划负责人实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblStaff principalStaff;
    
   
    @Schema(name = "审计对象实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblOrganization auditOrgInfo;
    
    @Schema(name = "审计组长实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblStaff createStaff;
    
    @Schema(name = "审计计划项目集合")
    @Transient
    @IgnoreSwaggerParameter
    private List<TblNbsjPlanProject> planProjectList;
    
    @Schema(name = "审计项目集合")
    @Transient
    @IgnoreSwaggerParameter
    private List<TblNbsjProject>  projectList;
    
    @Schema(name="总实施项目",hidden=true)
    @Transient
    private Integer totalItem;
    
    @Schema(name="未实施项目",hidden=true)
    @Transient
    @IgnoreSwaggerParameter
    private Integer unenforcedItem;
    
    @Schema(name="进行中项目",hidden=true)
    @Transient
    private Integer condectItem;
    
    @Schema(name="已完成项目",hidden=true)
    @Transient
    private Integer completeItem;
}
