package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.huabo.audit.config.IgnoreSwaggerParameter;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author huabo
 * @since 2022-06-27
 */
@TableName("TBL_NBSJ_REFORM_SOLUTION")
@Data
@Schema(name="实体类")
@Accessors(chain = true)
public class TblNbsjReformSolution {

    private static final long serialVersionUID = 1L;

    @TableId("SOLUTIONID")
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
  	@Column(name = "solutionid")
    @Schema(name = "整改方案主键ID")
    private BigDecimal solutionid;

    @TableField("SOLUTIONCODE")
    @Schema(name = "整改方案编号")
    @Column(name = "SOLUTIONCODE")
    private String solutioncode;

    @TableField("SOLUTIONNAME")
    @Column(name = "SOLUTIONNAME")
    @Schema(name = "整改方案名称")
    private String solutionname;

    @TableField("SOLUTIONSTATUS")
    @Schema(hidden=true)
    private String solutionstatus;

    @TableField("STAFFID")
    @Schema(name = "整改方案创建人ID")
    private BigDecimal staffid;

    @TableField("CREATEDATE")
    @Schema(name = "整改方案创建时间")
    @Column(name = "CREATEDATE")
    private Date createdate;

    @TableField("MEMO")
    @Schema(name = "备注")
    @Column(name = "MEMO")
    private String memo;

    @TableField("ORGID")
    @Schema(name="整改方案创建部门ID",hidden=true)
    private BigDecimal orgid;

    @TableField("RUNSTATUS")
    @Schema(name = "状态1、开始整改  2、整改中  3、关闭   4、整改完成")
    @Column(name = "RUNSTATUS")
    private Integer runstatus;

    @TableField("REFORMCOMPANYID")
    @Schema(name="整改责任人ID",hidden=true)
    private BigDecimal reformcompanyid;

    @TableField("REFORMUSERID")
    @Schema(name="整改责任人ID",hidden=true)
    private BigDecimal reformuserid;

    @TableField("REFORMTYPE")
    @Schema(name="整改责任人ID",hidden=true)
    private String reformtype;

    @TableField("ENDDATE")
    @Schema(name = "截止时间")
    private Date enddate;

    @TableField("PROJECTID")
    @Schema(name="整改方案关联审计项目ID",hidden=true)
    private BigDecimal projectid;

    @TableField("ZGSTATUS")
    @Schema(name = "评价状态 1评价中  2退回 3评价完成")
    @Column(name = "zgstatus")
    private Integer zgstatus;
    
    
    @Schema(name = "方案创建人实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblStaff createStaff;
    
    
    @Schema(name = "方案关联审计项目实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblNbsjProject relatedProject;//方案关联审计项目
    
    @Schema(name = "整改责任人实体")
    @Transient
    @IgnoreSwaggerParameter
    private TblStaff reformUser;//整改责任人
    
    
    @Schema(name = "方案关联整改内容集合")
    @Transient
    @IgnoreSwaggerParameter
    private List<TblNbsjRefopm> tblReforms;
    
    @TableField("PMID")
    @Schema(name="项目负责人ID",hidden=true)
    private Integer pmid;
    
    @TableField("BSJDWZRRID")
    @Schema(name="被审计单位整改责任人ID",hidden=true)
    private BigDecimal bsjdwzrrid;
    
    @TableField("BSJDWZFR")
    @Schema(name = "被审计单位整改责任人")
    private String bsjdwzfr;
    
    @TableField("OPSTATUS")
    @Schema(name = "审批状态")
    private Integer opstatus;
    
    
    
    @TableField("PARENTID")
    @Schema(name = "上一级关联方案ID")
    private BigDecimal parentid;
    


}
