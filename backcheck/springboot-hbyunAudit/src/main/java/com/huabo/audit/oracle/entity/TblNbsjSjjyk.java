package com.huabo.audit.oracle.entity;

import java.math.BigDecimal;
import com.huabo.audit.config.IgnoreSwaggerParameter;
import com.baomidou.mybatisplus.annotation.TableId;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
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
@Table(name="TBL_NBSJ_SJJYK")
@Schema(name="审计经验库实体")
public class TblNbsjSjjyk implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public final static Integer STATE1 = 1;//未审核
	public final static Integer STATE2 = 2;//审核中
	public final static Integer STATE3 = 3;//审核驳回
	public final static Integer STATE4 = 4;//审核完成
	public final static Integer STATE5 = 5;//需调整

    @TableId(value="JYKID",type= IdType.INPUT)
    //@GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select HIBERNATE_SEQUENCE.nextval from dual")
  	@Column(name = "JYKID")
    @Schema(name = "主键ID")
	@Id
    private BigDecimal jykid;

    @TableField("TATLE")
    @Schema(name = "标题")
    @Column(name = "TATLE")
    private String tatle;

    @TableField("CODE")
    @Column(name = "CODE")
    @Schema(name = "编号")
    private String code;

    @TableField("EXPERIENCETYPE")
    @Schema(name = "经验类型")
    private String experiencetype;
    
    
    @TableField("EXPERIENCETATLE")
    @Schema(name = "经验标签")
    private String experiencetatle;
    
    
    @TableField("OVERVIEW")
    @Schema(name = "概述")
    private String overview;
    
    
    @TableField("JYKCONTENT")
    @Schema(name = "内容	")
    private String jykcontent;
    

    @TableField("STAFFID")
    @Schema(name = "创建人ID")
    private BigDecimal staffid;

    @TableField("CREATEDTIME")
    @Schema(name="创建时间",hidden=true)
    private Date createdtime;


    @TableField("ORGID")
    @Schema(name="创建公司ID",hidden=true)
    private BigDecimal orgid;

   
    @TableField("UPDATEDTIME")
    @Schema(name="修改时间",hidden=true)
    private Date updatedtime;
    
    @TableField(value = "STATE")
	@Schema
	private Integer state;

    
    @Schema(name = "创建人实体")
    @Transient
    @IgnoreSwaggerParameter
    @TableField(exist = false)
    private TblStaff createStaff;


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
